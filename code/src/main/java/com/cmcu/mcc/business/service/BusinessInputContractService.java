package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.dto.BusinessInputContractDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.service.EdProjectTreeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.util.*;

@Service
public class BusinessInputContractService extends BaseService {
    @Resource
    BusinessInputContractMapper businessInputContractMapper;

    @Resource
    CommonCodeService commonCodeService;


    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    EdProjectTreeService edProjectTreeService;
    @Autowired
    BusinessContractService businessContractService;
    @Autowired
    BusinessPreContractMapper businessPreContractMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Resource
    HandleFormService handleFormService;

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessInputContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessInputContract item = (BusinessInputContract) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void remove(int id, String userLogin) {
        BusinessInputContract item = businessInputContractMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        Map preParams =new HashMap();
        preParams.put("inputContractId",id);
        List<BusinessPreContract> businessPreContracts = businessPreContractMapper.selectAll(preParams);
        //如果为补录 跟新超前任务单
        if(businessPreContracts.size()!=0) {
            BusinessPreContract preContract = businessPreContracts.get(0);
            preContract.setReviewContractId(0);
            businessPreContractMapper.updateByPrimaryKey(preContract);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(BusinessInputContractDto model) {
        BusinessInputContract item =  businessInputContractMapper.selectByPrimaryKey(model.getId());


        //如果之前为补录 将原超前任务单设置为未补录
        if(item.getPreId()!= 0){
            BusinessPreContract oldPreContract = businessPreContractMapper.selectByPrimaryKey(item.getPreId());
            oldPreContract.setReviewContractId(0);
            businessPreContractMapper.updateByPrimaryKey(oldPreContract);
        }
        //跟新超前任务单 状态
        BusinessPreContract preContract = businessPreContractMapper.selectByPrimaryKey(model.getPreId());
        preContract.setReviewContractId(item.getId());
        businessPreContractMapper.updateByPrimaryKey(preContract);

        item.setRecordId(model.getRecordId());
        item.setPreId(model.getPreId());
        item.setProjectName(model.getProjectName());
        item.setContractNo(model.getContractNo());
        item.setProjectNo(model.getProjectNo());
        item.setDeptId(model.getDeptId());
        item.setDeptName(model.getDeptName());
        item.setContractMark(model.getContractMark());
        item.setContractSchedule(model.getContractSchedule());
        item.setContractDes(model.getContractDes());
        item.setContractMoney(MyStringUtil.moneyToString(model.getContractMoney()));
        item.setInOrOut(model.getInOrOut());
        item.setSignTime(model.getSignTime());
        item.setChargeMen(model.getChargeMen());
        item.setChargeMenName(model.getChargeMenName());
        item.setEnterTime(model.getEnterTime());
        item.setQuarter(model.getQuarter());
        item.setQuarter(model.getQuarter());
        item.setBankAccount(model.getBankAccount());
        item.setPostalAddress(model.getPostalAddress());
        item.setPostalCode(model.getPostalCode());
        item.setLinkPhone(model.getLinkPhone());
        item.setContractType(model.getContractType());
        item.setCustomerId(model.getCustomerId());
        item.setCustomerName(model.getCustomerName());
        item.setAgency(model.getAgency());
        item.setProjectNature(model.getProjectNature());
        item.setIndustryType(model.getIndustryType());
        item.setMilitaryMark(model.getMilitaryMark());
        item.setForeignTradeMark(model.getForeignTradeMark());
        item.setCivilMark(model.getCivilMark());
        item.setProjectMajorTypeFirst(model.getProjectMajorTypeFirst());
        item.setProjectMajorTypeSecond(model.getProjectMajorTypeSecond());
        item.setProjectMajorTypeThird(model.getProjectMajorTypeThird());
        item.setRemark(model.getRemark());
        item.setItemNum(model.getItemNum());
        item.setDepositBank(model.getDepositBank());
        item.setGmtModified(new Date());
        item.setEd(model.getEd());
        businessInputContractMapper.updateByPrimaryKey(item);
        //  BeanValidator.check(item);
        Map variables = Maps.newHashMap();
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public BusinessInputContractDto getModelById(int id) {
        return getDto(businessInputContractMapper.selectByPrimaryKey(id));
    }

    private BusinessInputContractDto getDto(BusinessInputContract item) {
        BusinessInputContractDto dto = BusinessInputContractDto.adapt(item);
       // MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(item.getProcessInstanceId(), "");
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if (customProcessInstance==null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                businessInputContractMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin,String uiSref) {
        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, uiSref);

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        BusinessInputContract item = new BusinessInputContract();

        item.setContractMoney(MyStringUtil.moneyToString(""));
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setContractSchedule(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同进度").toString());
        item.setInOrOut(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"部内外").toString());
        item.setQuarter(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"季度").toString());
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型").toString());
        item.setProjectNature(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目性质").toString());
        item.setIndustryType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"甲方行业分类").toString());
        item.setMilitaryMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"军品标记").toString());
        item.setCivilMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"民用标记").toString());
        item.setForeignTradeMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"外贸标记").toString());
        item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目专业分类一级").toString());
        item.setProjectMajorTypeSecond(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目专业分类二级").toString());
        item.setProjectMajorTypeThird(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目专业分类三级").toString());
        item.setEd(true);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        Map<String, Object> defaultDept = hrDeptService.getDefaultDept(hrEmployeeDto.getDeptId());
        item.setDeptId(Integer.parseInt(defaultDept.get("deptId").toString()));
        item.setDeptName(defaultDept.get("deptName").toString());

        ModelUtil.setNotNullFields(item);
        businessInputContractMapper.insert(item);

        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("financeMan", selectEmployeeService.getFinanceChargeMan());
        variables.put("processDescription", "合同录入");

        String processKey= EdConst.FIVE_BUSINESS_INPUT_CONTRACT;
        String businessKey = processKey+"_"+item.getId();
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(processKey, item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);

        businessInputContractMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * 通过合同号查 录入合同信息
     * @param contractId
     * @return
     */
    public BusinessInputContractDto getModelByContractId(int contractId){
        Map<String, Object> params=Maps.newHashMap();
        params.put("deleted", false);
        params.put("contractId",contractId);
        List<BusinessInputContract> businessInputContracts = businessInputContractMapper.selectAll(params);
        if (businessInputContracts.size()>0){
            return getDto(businessInputContracts.get(0));
        }else {
            return null;
        }



    }

    /**
     * XX 年份代码（如2017年写为17，2018年写为18，其余类推）
     * XX 合同承办单位代码
     * X 合同类别代码 设计合同 1 ;技术合同2;	工程总承包包（项目管理）合同，建设监理合同	3;	科研、技术开发、技术转让合同4 ; 销售合同；纪要、协议5;  无合同	6;
     * XXX  顺序代码，各类合同自成序列，从001开始
     * @param id
     */

    public String getContractNo(int id){
        try {
            BusinessInputContractDto businessInputContractDto = getModelById(id);
            String newContractNo="H";
            //年份代码 2019年  19
            String signYear = businessInputContractDto.getSignTime().split("-")[0];
            String year = signYear.substring(2);
            //承办单位代码 2位 01
            String deptCode= hrDeptService.getModelById(businessInputContractDto.getDeptId()).getDeptCode();
            //合同类别代码
            List<CommonCodeDto> sysCodeDtoList = commonCodeService.listDataByCatalog("","合同类型");
            String typeCode="";
            for ( CommonCode dto: sysCodeDtoList) {
                if (dto.getName().equals(businessInputContractDto.getContractType())){
                    typeCode=dto.getCode();
                }
            }
            //如果  年份 单位 合同类型 没有改变 返回原合同号
            if (businessInputContractDto.getContractNo().contains("H"+year+deptCode+typeCode)){
                return businessInputContractDto.getContractNo();
            }

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("contractNoHead","H"+year+deptCode+typeCode);
            List<BusinessInputContract> businessInputContracts = businessInputContractMapper.selectAll(params);
            int size=1;
            //判断顺序代码最大值
            if (!businessInputContracts.isEmpty()){
                for (BusinessInputContract inputContract:businessInputContracts){
                    String maxSize=  inputContract.getContractNo().substring(6);
                    if (size<Integer.parseInt(maxSize)){
                        size = Integer.parseInt(maxSize);
                    }
                }
                size=size+1;
            }
            String format = String.format("%03d", size);
            newContractNo=newContractNo+year+deptCode+typeCode+format;

            businessInputContractDto.setContractNo(newContractNo);
            update(businessInputContractDto);

            return newContractNo;

        }catch (Exception e){
            Assert.state(false, "请准确填写，签约日期、承办单位、合同类型！");
            return "";
        }


    }

    /**
     * XX 年份代码（如2017年写为17，2018年写为18，其余类推）
     * XX 合同承办单位代码
     * X 项目类别代码 工程设计 S 工程咨询 Z 工程承包 C 勘察 K 监理 J 无合同 W
     * XXX  顺序代码，各类合同自成序列，从001开始
     * @param id
     */

    public String getProjectNo(int id){
        try {
            BusinessInputContractDto businessInputContractDto = getModelById(id);
            BusinessContractDto businessContractDto = businessContractService.getModelById(Integer.valueOf(businessInputContractDto.getContractId()));
            String newProjectNo = "";
            //年份代码 2019年  19
            String signYear = businessInputContractDto.getSignTime().split("-")[0];
            String year = signYear.substring(2);
            //承办单位代码 2位 01
            String deptCode= hrDeptService.getModelById(businessInputContractDto.getDeptId()).getDeptCode();
            //合同类别代码
            String typeCode="";
            /*List<SysCodeDto> sysCodeDtoList = sysCodeMapper.listDataByCatalog("五洲项目类型");
            for ( SysCodeDto dto: sysCodeDtoList) {
                if (dto.getName().equals(businessContractDto.getProjectType())){
                    typeCode=dto.getCode();
                }
            }*/
            if(businessContractDto.getProjectType().equals("工程设计")){
                typeCode="S";
            }else if(businessContractDto.getProjectType().equals("工程咨询")){
                typeCode="Z";
            }else if(businessContractDto.getProjectType().equals("工程承包")){
                typeCode="C";
            } else if(businessContractDto.getProjectType().equals("勘察")){
                typeCode="K";
            } else if(businessContractDto.getProjectType().equals("监理")){
                typeCode="J";
            } else if(businessContractDto.getProjectType().equals("无合同")){
                typeCode="W";
            }
            //如果  年份 单位 合同类型 没有改变 返回原合同号
            if (businessInputContractDto.getProjectNo().contains(year+deptCode+typeCode)){
                return businessInputContractDto.getProjectNo();
            }

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("projectNoHead",year+deptCode+typeCode);
            List<BusinessInputContract> businessInputContracts = businessInputContractMapper.selectAll(params);
            int size=1;
            //判断顺序代码最大值
            if (!businessInputContracts.isEmpty()){
                for (BusinessInputContract inputContract:businessInputContracts){
                    String maxSize=  inputContract.getProjectNo().substring(5);
                    if (size<Integer.parseInt(maxSize)){
                        size = Integer.parseInt(maxSize);
                    }
                }
                size=size+1;
            }
            String format = String.format("%03d", size);
            newProjectNo=newProjectNo+year+deptCode+typeCode+format;

            businessInputContractDto.setProjectNo(newProjectNo);
            update(businessInputContractDto);

            return newProjectNo;

        }catch (Exception e){
            Assert.state(false, "请准确填写，签约日期、承办单位、项目类型！");
            return "";
        }


    }
    //保存信息到 合同库 改为跟新数据
    public String insertContractLibrary(int inputContractId) {
        BusinessInputContract input =businessInputContractMapper.selectByPrimaryKey(inputContractId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractPreId", input.getPreId());
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(librarys.size()==0){
            return "未查找到项目："+input.getProjectName()+"合同库信息";
        }
        FiveBusinessContractLibrary library =librarys.get(0);

        library.setContractMoney(MyStringUtil.moneyToString(input.getContractMoney()));
        library.setContractNo(input.getContractNo());

        return "项目： "+input.getProjectName()+"  信息已跟新到合同库";

    }


}
