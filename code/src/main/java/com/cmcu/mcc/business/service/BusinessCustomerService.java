package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessContractMapper;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.BusinessCustomerUsedNameMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dto.BusinessCustomerDto;

import com.cmcu.mcc.business.entity.BusinessCustomer;
import com.cmcu.mcc.business.entity.BusinessCustomerUsedName;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BusinessCustomerService extends BaseService {

    @Resource
    BusinessCustomerMapper businessCustomerMapper;
    @Resource
    BusinessContractMapper businessContractMapper;
    @Resource
    CommonCodeService commonCodeService;

    @Resource
    HrEmployeeSysService hrEmployeeSysService;

    @Resource
    HrDeptService hrDeptService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    MyActService myActService;
    @Resource
    HandleFormService handleFormService;
    @Autowired
    ProcessQueryService processQueryService;
    @Resource
    BusinessCustomerUsedNameMapper businessCustomerUsedNameMapper;


    public void remove(int id, String userLogin) {

        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("customerId", id);
        if (PageHelper.count(() -> businessContractMapper.selectAll(params)) > 0) {
            throw new IllegalArgumentException("该客户存在合作项目无法删除!");
        }

        BusinessCustomer model = businessCustomerMapper.selectByPrimaryKey(id);
        //流程作废
        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);

    }

    public void update(BusinessCustomerDto dto) {

        //验证纳税人识别号是否存在,没有填写不做判断，点击纳税人识别号的时候验证
        if (!Strings.isNullOrEmpty(dto.getTaxNo())){
            checkTaxNo(dto.getTaxNo(),dto.getId());
        }
        //验证客户信息是否存在,2021.3.17注释客户不需要验证
       // checkCustomer(dto.getName(),dto.getId());
        //验证客户编号是否存在
        if(!Strings.isNullOrEmpty(dto.getCode())){
            checkCustomerCode(dto.getCode(),dto.getId());
        }

        BusinessCustomer model = businessCustomerMapper.selectByPrimaryKey(dto.getId());
        //如果是变更 增加曾用名
        if(model.getProcessEnd()&&!model.getName().equals(dto.getName())){
            int newUsedNameId = getNewUsedName(dto.getOperateUserLogin(), dto.getId());
            BusinessCustomerUsedName usedName = getUsedNameById(newUsedNameId);
            usedName.setOldName(model.getName());
            usedName.setName(dto.getName());
            usedName.setRemark("客户变更流程创建");
            updateUsedName(usedName);
        }
        model.setName(dto.getName());
        model.setCode(dto.getCode());
        model.setAddress(dto.getAddress());
        model.setCustomerScope(dto.getCustomerScope());
        model.setCustomerType(dto.getCustomerType());
        model.setLinkMan(dto.getLinkMan());
        model.setLinkTel(dto.getLinkTel());
        model.setLinkFax(dto.getLinkFax());
        model.setLinkTitle(dto.getLinkTitle());
        model.setLinkMail(dto.getLinkMail());
        model.setDepositBank(dto.getDepositBank());
        model.setBankAccount(dto.getBankAccount());
        model.setTaxType(dto.getTaxType());
        model.setTaxNo(dto.getTaxNo());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());
        model.setSeq(0);
        if(!model.getDeptId().equals(dto.getDeptId())) {
            model.setDeptId(dto.getDeptId());
            model.setDeptName(hrDeptService.getModelById(model.getDeptId()).getName());
        }
        model.setSystemInName(dto.getSystemInName());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);

        Map variables = Maps.newHashMap();
        variables.put("processDescription", "客户信息录入:"+model.getName()+"("+model.getCode()+")");
        myActService.setVariables(model.getProcessInstanceId(),variables);

        businessCustomerMapper.updateByPrimaryKey(model);
    }


    public BusinessCustomerDto getModelById(int id) {
        return getDto(businessCustomerMapper.selectByPrimaryKey(id));
    }

    public BusinessCustomerDto getModelByNameAndLinMan(String customerName,String linkMan) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("name", customerName);
        params.put("linkMan", linkMan);
        if (PageHelper.count(()->businessCustomerMapper.selectAll(params))>0){
            return getDto(businessCustomerMapper.selectAll(params).get(0));
        }else {
            return null;
        }

    }

    public int getNewModel(String userLogin,String uiSref) {
        //判断是否为总师角色
       // Assert.state(selectEmployeeService.isTotalChargeMan(userLogin),"该流程只能由 总师角色 发起");
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        //HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLogin(userLogin);
        BusinessCustomer item = new BusinessCustomer();

        item.setDeleted(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setSeq(0);
        item.setTaxType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"纳税主体资格").toString());
        item.setCustomerType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"客户单位类型").toString());
        item.setCustomerScope(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"客户类别").toString());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
        ModelUtil.setNotNullFields(item);
        businessCustomerMapper.insert(item);

        item.setBusinessKey(EdConst.FIVE_BUSINESS_CUSTOMER+"_" + item.getId());
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "客户信息录入");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_CUSTOMER,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        businessCustomerMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public BusinessCustomerDto getDto(BusinessCustomer item) {
        BusinessCustomerDto dto = BusinessCustomerDto.adapt((BusinessCustomer) item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

            if(customProcessInstance==null||customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                businessCustomerMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null&& StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }

        }

        //合作项目数量
        dto.setContractCount(listCooperationProject(dto.getId()).size());
        return dto;
    }

    public PageInfo<BusinessCustomer> loadPagedData(String userLogin,String uiSref,Map<String, Object> params, int pageNum, int pageSize) {
        params.put("deleted", false);
       
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
/*        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));*/

        params.put("processEndPo",userLogin);//看见自己创建的和其他流程已完成的
        params.put("isLikeSelect","true");

        //前端分页缓存 不去除导致分页失败
        params.remove("total");
        PageInfo<BusinessCustomer> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessCustomerMapper.selectAll(params));
        List<BusinessCustomer> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto(p)));
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<BusinessCustomer> selectAll(String userLogin,String uiSref) {
        if(StringUtils.isEmpty(uiSref)){
            uiSref="business.customer";
        }
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("processEnd",true);
    /*    List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, uiSref);
        params.put("deptIdList", deptIdList);*/
        return businessCustomerMapper.selectAll(params);
    }

    public List<FiveBusinessContractLibrary> listCooperationProject(int customerId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("customerId", customerId);
        List<FiveBusinessContractLibrary> fiveBusinessContractLibraries = fiveBusinessContractLibraryMapper.selectAll(params);
        return fiveBusinessContractLibraries;
    }


    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        HrEmployeeSysDto hrEmployeeSysDto=hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        int updateNum=0;
        int seq=(int)PageHelper.count(()->businessCustomerMapper.selectAll(Maps.newHashMap()));
        List<BusinessCustomer> insertList=Lists.newArrayList();
        for(int i=1;i<=list.size();i++) {
            try {
                Map map = list.get(i - 1);
                Map params = Maps.newHashMap();
                params.put("name", map.get(0).toString());
                BusinessCustomer item = new BusinessCustomer();
                if (PageHelper.count(() -> businessCustomerMapper.selectAll(params)) > 0) {
                    item = businessCustomerMapper.selectAll(params).get(0);
                }
                item.setCode(map.get(0).toString());
                item.setName(map.get(1).toString());
                item.setLinkMan(map.get(2).toString());
                item.setLinkTel(map.get(3).toString());
                item.setLinkTitle(map.get(4).toString());
                item.setCustomerScope(map.get(5).toString());
                item.setCustomerType(map.get(6).toString());
                item.setAddress(map.get(7).toString());
                item.setLinkFax(map.get(8).toString());
                item.setLinkMail(map.get(9).toString());
                item.setDepositBank(map.get(10).toString());
                item.setBankAccount(map.get(11).toString());
                item.setTaxType(map.get(12).toString());
                item.setTaxNo(map.get(13).toString());
                item.setDeptId(hrEmployeeSysDto.getDeptId());
                item.setDeptName(hrEmployeeSysDto.getDeptName());
                if (StringUtils.isNotEmpty(map.get(14).toString())) {
                    item.setRemark(map.get(14).toString());
                } else {
                    item.setRemark("  ");
                }
                String deptName = map.get(15).toString();
                if (StringUtils.isNotEmpty(deptName)) {
                    HrDeptDto hrDeptDto = hrDeptService.getModelByName(deptName);
                    Assert.state(hrDeptDto!=null,(i+1)+"行数据的部门名称"+deptName+"在系统中未匹配上!");
                    item.setDeptName(hrDeptDto.getName());
                    item.setDeptId(hrDeptDto.getId());
                }
                item.setDeleted(false);
                if (item.getId() == null) {
                    item.setGmtCreate(new Date());
                    item.setGmtModified(new Date());
                    item.setSeq(seq++);
                    item.setId(0);
                    item.setBusinessKey(" ");
                    ModelUtil.setNotNullFields(item);
                }
                BeanValidator.check(item);
                insertList.add(item);
            } catch (Exception e) {
                throw new IllegalArgumentException("第" + (i + 1) + "数据错误：" + e.getMessage());
            }
        }

        for (BusinessCustomer businessCustomer:insertList){
           if (businessCustomer.getId()!=0){
               businessCustomerMapper.updateByPrimaryKey(businessCustomer);
               updateNum++;
           }else {
               businessCustomerMapper.insert(businessCustomer);
               businessCustomer.setBusinessKey("businessCustomer_" + businessCustomer.getId());
               businessCustomerMapper.updateByPrimaryKey(businessCustomer);
           }
        }
        return updateNum+","+(insertList.size()-updateNum);
    }

    //导出数据
    public HSSFWorkbook downloadExcel(String userLogin) throws IOException {
        List<BusinessCustomer> businessCustomerList = selectAll(userLogin,"business.customer");
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/客户信息.xls";
        //得到模板文件
        InputStream in = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFCellStyle cellStyle = sheet.getRow(0).getCell(0).getCellStyle();
        List<Map> list = new ArrayList<>();
        int rowIndex = 1;//起始行
        for (int i = 0; i < businessCustomerList.size(); i++) {
            HSSFRow row = sheet.createRow(rowIndex++);

            HSSFCell cell = row.createCell(0);
            cell.setCellValue(businessCustomerList.get(i).getName());
            row.setRowStyle(cellStyle);

            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(businessCustomerList.get(i).getLinkMan());
            row.setRowStyle(cellStyle);

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(businessCustomerList.get(i).getLinkTel());
            row.setRowStyle(cellStyle);

            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(businessCustomerList.get(i).getLinkTitle());
            row.setRowStyle(cellStyle);

            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(businessCustomerList.get(i).getCustomerType());
            row.setRowStyle(cellStyle);

            HSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(businessCustomerList.get(i).getCustomerScope());
            row.setRowStyle(cellStyle);

            HSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(businessCustomerList.get(i).getAddress());
            row.setRowStyle(cellStyle);

            HSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(businessCustomerList.get(i).getLinkFax());
            row.setRowStyle(cellStyle);

            HSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(businessCustomerList.get(i).getLinkMail());
            row.setRowStyle(cellStyle);

            HSSFCell cell9 = row.createCell(9);
            cell9.setCellValue(businessCustomerList.get(i).getDepositBank());
            row.setRowStyle(cellStyle);

            HSSFCell cell10 = row.createCell(10);
            cell10.setCellValue(businessCustomerList.get(i).getBankAccount());
            row.setRowStyle(cellStyle);

            HSSFCell cell11 = row.createCell(11);
            cell11.setCellValue(businessCustomerList.get(i).getTaxType());
            row.setRowStyle(cellStyle);

            HSSFCell cell12 = row.createCell(12);
            cell12.setCellValue(businessCustomerList.get(i).getTaxNo());
            row.setRowStyle(cellStyle);

            HSSFCell cell13 = row.createCell(13);
            cell13.setCellValue(businessCustomerList.get(i).getRemark());
            row.setRowStyle(cellStyle);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        byte[] data = IOUtils.toByteArray(is);
        return workbook;
    }

    //检查 客户名称是否重复
    public void checkCustomer(String name,int customerId) {
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("name",name);
        //排除自己
        List<BusinessCustomer> businessCustomers = businessCustomerMapper.selectAll(map).stream().filter(p->p.getId()!=customerId).collect(Collectors.toList());
        Assert.state(businessCustomers.size()==0,"客户名称为："+name+" 已存在！");
    }
    //检查 纳税人识别号是否重复
    public void checkTaxNo(String taxNo,int customerId){
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("taxNo",taxNo);
        List<BusinessCustomer> businessCustomers=businessCustomerMapper.selectAll(map).stream().filter(p->p.getId()!=customerId).collect(Collectors.toList());
        Assert.state(businessCustomers.size()==0,"纳税人识别号："+taxNo+"已存在！");
    }
    //检查 客户编号是否重复
    public void checkCustomerCode(String code,int customerId) {
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("code",code);
        //排除自己
        List<BusinessCustomer> businessCustomers = businessCustomerMapper.selectAll(map).stream().filter(p->p.getId()!=customerId).collect(Collectors.toList());
        Assert.state(businessCustomers.size()==0,"客户编号为："+code+" 已存在！");
    }

    public List<BusinessCustomerUsedName> listUsedNamesById(int customerId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("customerId", customerId);
        List<BusinessCustomerUsedName> businessCustomerUsedNames = businessCustomerUsedNameMapper.selectAll(params);
        return businessCustomerUsedNames;
    }

    public BusinessCustomerUsedName getUsedNameById(int id) {
       return businessCustomerUsedNameMapper.selectByPrimaryKey(id);
    }

    public void updateUsedName(BusinessCustomerUsedName usedName) {
        usedName.setGmtModified(new Date());
        businessCustomerUsedNameMapper.updateByPrimaryKey(usedName);
    }

    public int getNewUsedName(String userLogin, int customerId) {
        //BusinessCustomer businessCustomer = businessCustomerMapper.selectByPrimaryKey(customerId);
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        BusinessCustomerUsedName item = new BusinessCustomerUsedName();
        item.setCustomerId(customerId);
        item.setDeleted(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setSeq(0);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        businessCustomerUsedNameMapper.insert(item);
        return item.getId();
    }

    public void removeUsedName(int id, String userLogin) {
        businessCustomerUsedNameMapper.deleteByPrimaryKey(id);
    }
}
