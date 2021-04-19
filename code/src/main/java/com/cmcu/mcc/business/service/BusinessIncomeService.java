package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.BusinessIncomeMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.dto.BusinessIncomeDto;
import com.cmcu.mcc.business.dto.FiveBusinessContractLibraryDto;
import com.cmcu.mcc.business.entity.BusinessIncome;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeConfirmDto;
import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceIncomeConfirm;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.service.FiveFinanceIncomeConfirmService;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessIncomeService extends BaseService {

    @Resource
    BusinessIncomeMapper businessIncomeMapper;

    @Resource
    CommonCodeService commonCodeService;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    HrDeptService hrDeptService;

    @Autowired
    MyActService myActService;

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Resource
    HandleFormService handleFormService;
    @Autowired
    FiveBusinessContractLibraryService fiveBusinessContractLibraryService;
    @Autowired
    FiveFinanceIncomeConfirmService fiveFinanceIncomeConfirmService;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Autowired
    FiveFinanceInvoiceMapper fiveFinanceInvoiceMapper;
    @Autowired
    ProcessQueryService processQueryService;
    public void remove(int id, String userLogin) {
        BusinessIncome item = businessIncomeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //之前的合同库关联 还原
        if(item.getContractLibraryId()!=0){
            FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            oldLibrary.setIncomeIds(MyStringUtil.getNewStringId(oldLibrary.getIncomeIds(),id));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
        }
        //如果有收入确认 删除关联
        if(item.getIncomeConfirmId()!=0){
            FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(item.getIncomeConfirmId());
            incomeConfirm.setIncomeIds(MyStringUtil.getNewStringId(incomeConfirm.getIncomeIds(),id));
            fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        }
        //如果由收入确认中的发票申请创建 删除发票与收入确认关联
        if(item.getIncomeConfirmId()!=0&&item.getInvoiceId()!=0){
            FiveFinanceInvoice fiveFinanceInvoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
            fiveFinanceInvoice.setIncomeConfirmIds(MyStringUtil.getNewStringId(fiveFinanceInvoice.getIncomeConfirmIds(),item.getIncomeConfirmId()));
            fiveFinanceInvoice.setIncomeConfirmId(0);
            //跟新发票使用金额
            fiveFinanceInvoice.setInvoiceGetMoneyIng(MyStringUtil.getNewSubMoney(fiveFinanceInvoice.getInvoiceGetMoneyIng(),item.getIncomeMoney()));
            fiveFinanceInvoiceMapper.updateByPrimaryKey(fiveFinanceInvoice);

            FiveFinanceIncomeConfirm fiveFinanceIncomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(item.getIncomeConfirmId());
            fiveFinanceIncomeConfirm.setInvoiceIds(MyStringUtil.getNewStringId(fiveFinanceIncomeConfirm.getInvoiceIds(),item.getInvoiceId()));
            fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(fiveFinanceIncomeConfirm);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(BusinessIncomeDto dto) {
        BusinessIncome item = businessIncomeMapper.selectByPrimaryKey(dto.getId());
        //跟新主合同信息
        if(item.getContractLibraryId()!=0){
            //之前的合同库 还原
            if(item.getContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                oldLibrary.setIncomeIds(MyStringUtil.getNewStringId(oldLibrary.getIncomeIds(),item.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
            }
            //合同库 分包添加
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setIncomeIds(library.getIncomeIds()+","+item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        //跟新发票使用金额
        if(item.getInvoiceId()!=0){
            //跟新收入金额 先减掉原来的 在加上新的
            FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
            invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewSubMoney(invoice.getInvoiceGetMoneyIng(),item.getIncomeMoney()));
            invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoneyIng(),dto.getIncomeMoney()));
            fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
        }
        item.setContractLibraryId(dto.getContractLibraryId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setIncomeMoney(MyStringUtil.moneyToString(dto.getIncomeMoney()));
        item.setIncomeMoneyMax(dto.getIncomeMoneyMax());
        item.setVerifyTime(dto.getVerifyTime());
        item.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney()));
        item.setContractIncomeMoney(MyStringUtil.moneyToString(dto.getContractIncomeMoney()));
        item.setContractName(dto.getContractName());
        item.setContractNo(dto.getContractNo());
        item.setManagePercent(dto.getManagePercent());
        item.setProjectName(dto.getProjectName());
        item.setProjectNo(dto.getProjectNo());
        item.setRecordProjectName(dto.getRecordProjectName());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "合同收费:"+item.getContractName());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        businessIncomeMapper.updateByPrimaryKey(item);
    }

    public BusinessIncomeDto getModelById(int id) {
        return getDto(businessIncomeMapper.selectByPrimaryKey(id));
    }

    public BusinessIncomeDto selectByInvoiceId(int invoiceId) {
        Map map = new HashMap();
        map.put("invoiceId",invoiceId);
        map.put("deleted",false);
        List<BusinessIncome> businessIncomes = businessIncomeMapper.selectAll(map);
        if(businessIncomes.size()>0){
            return getDto(businessIncomes.get(0));
        }else{
            return null;
        }

    }

    public int getNewModel(String userLogin,String uiSref) {
        BusinessIncome item = new BusinessIncome();
        HrEmployeeDto employeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(employeeDto.getUserName());
        item.setDeptId(employeeDto.getDeptId());
        item.setDeptName(employeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        item.setVerifyTime(sdf.format(new Date()));
        ModelUtil.setNotNullFields(item);
        businessIncomeMapper.insert(item);

        String processKey=EdConst.FIVE_BUSINESS_INCOME;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "合同收费");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessIncomeMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public BusinessIncomeDto getDto(Object item) {
        BusinessIncome model= (BusinessIncome) item;
        BusinessIncomeDto dto=BusinessIncomeDto.adapt(model);
        //金额显示位数
        dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(),6));
        dto.setContractIncomeMoney(MyStringUtil.moneyToString(dto.getContractIncomeMoney(),6));
        dto.setIncomeMoney(MyStringUtil.moneyToString(dto.getIncomeMoney(),6));
        //如果有发票
        if(model.getInvoiceId()!=0){
            FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(model.getInvoiceId());
            dto.setInvoiceGetMoney(MyStringUtil.moneyToString(invoice.getInvoiceGetMoney(),6));
            dto.setInvoiceMoney(MyStringUtil.moneyToString(invoice.getInvoiceMoney(),6));
        }
        dto.setProcessName("已完成");
        if(!dto.getProcessEnd()&&StringUtils.isNotEmpty(dto.getProcessInstanceId())) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessName("已完成");
                dto.setProcessEnd(true);
                businessIncomeMapper.updateByPrimaryKey(dto);
                //合同收入跟新
                fiveBusinessContractLibraryService.getIncomeMoney(dto.getId());
                //如果有发票 发票收入跟新
                if (dto.getInvoiceId() != 0) {
                    FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(dto.getInvoiceId());
                    invoice.setInvoiceGetMoney(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoney(), dto.getIncomeMoney()));
                    //还原发票正在认领金额
                    invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewSubMoney(invoice.getInvoiceGetMoneyIng(), dto.getIncomeMoney()));
                    fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
                } else {
                    //没有发票  跟新合同预收款
                    FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getContractLibraryId());
                    library.setPreContractMoney(MyStringUtil.getNewAddMoney(library.getPreContractMoney(), dto.getIncomeMoney()));
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                }

            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }


    public List<BusinessIncome> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  businessIncomeMapper.selectAll(params);
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
/*        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));*/
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        if (deptIdList.size()==0){
            //获取事业部的deptIds
            int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
            List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(headDeptId);
            params.put("deptIdList",deptIds);
        }else {
            if(deptIdList.contains(1)){
                deptIdList.add(0);
            }
            params.put("deptIdList",deptIdList);
        }

        params.put("isLikeSelect","true");

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessIncomeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessIncome item=(BusinessIncome)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 收入金额转中文大写
     * @param money
     * @return
     */
    public String moneyTurnCapital(Double money){
        return NumberChangeUtils.moneyToChinese(money*10000);
    }
    //收入确认中 新建合同收费
    public int getNewModelByConfirm(int incomeConfirmId,int invoiceId, String userLogin) {

        //判断合同收费是否已被创建
        List<BusinessIncome> businessIncomes = getIncomesByIncomeConfirmId(incomeConfirmId);
        for(BusinessIncome businessIncome:businessIncomes){
            if(businessIncome.getInvoiceId().equals(invoiceId)){
                Assert.state(false,"发票号为："+businessIncome.getInvoiceNo()+" 的合同收费 已被创建");
            }
        }

        BusinessIncome item = new BusinessIncome();
        item.setIncomeConfirmId(incomeConfirmId);
        HrEmployeeDto employeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(employeeDto.getUserName());
        item.setDeptId(employeeDto.getDeptId());
        item.setDeptName(employeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);

        ModelUtil.setNotNullFields(item);
        businessIncomeMapper.insert(item);

        String processKey=EdConst.FIVE_BUSINESS_INCOME;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "合同收费");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);

        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);

        //收入直接认领完
        item.setIncomeMoney(incomeConfirm.getMoney());
        item.setIncomeMoneyMax(incomeConfirm.getMoneyMax());

        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessIncomeMapper.updateByPrimaryKey(item);


        //关联收入认领
        incomeConfirm.setIncomeIds(incomeConfirm.getIncomeIds()+","+item.getId());
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        return item.getId();
    }

    //收入确认中 新建合同收费  先选择合同
    public int getNewModelByConfirm2(int incomeConfirmId,int libraryId,int invoiceId, String userLogin) {
        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(libraryId);


        //判断合同收费是否已被创建
        List<BusinessIncome> businessIncomes = getIncomesByIncomeConfirmId(incomeConfirmId);
        for(BusinessIncome businessIncome:businessIncomes){
            if(businessIncome.getInvoiceId().equals(invoiceId)){
                Assert.state(false,"发票号为："+businessIncome.getInvoiceNo()+" 的合同收费 已被创建");
            }
        }

        BusinessIncome item = new BusinessIncome();
        item.setIncomeConfirmId(incomeConfirmId);
        //绑定合同信息
        item.setContractLibraryId(libraryId);

        item.setContractName(library.getContractName());
        //投资额
        item.setContractMoney(library.getContractMoney());
        //合同号
        item.setContractNo(library.getContractNo());
        //合同已领金额
        item.setContractIncomeMoney(library.getContractIncomeMoney());
        //项目名称
        item.setProjectName(library.getProjectName());
        //项目号
        item.setProjectNo(library.getProjectNo());

        HrEmployeeDto employeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(employeeDto.getUserName());
        item.setDeptId(employeeDto.getDeptId());
        item.setDeptName(employeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);

        ModelUtil.setNotNullFields(item);
        businessIncomeMapper.insert(item);

        String processKey=EdConst.FIVE_BUSINESS_INCOME;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "合同收费");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);

        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);

        //收入直接认领完
        item.setIncomeMoney(incomeConfirm.getMoney());
        item.setIncomeMoneyMax(incomeConfirm.getMoneyMax());

        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        businessIncomeMapper.updateByPrimaryKey(item);

        //关联合同库
        library.setIncomeIds(library.getIncomeIds()+","+item.getId());
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);

        //关联收入认领
        incomeConfirm.setIncomeIds(incomeConfirm.getIncomeIds()+","+item.getId());
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        return item.getId();
    }
    //收入确认中的 合同收费
    public List<BusinessIncome> getIncomesByIncomeConfirmId(int incomeConfirmId) {
        List<BusinessIncome> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("incomeConfirmId",incomeConfirmId);
        List<BusinessIncome> businessIncomes = businessIncomeMapper.selectAll(map);
        for(BusinessIncome businessIncome:businessIncomes){
            list.add(getDto(businessIncome));
        }
        return list;
    }

    public List<Map> listMapData() {

        return new ArrayList<>();
    }

    public List<Map> insertBatch(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = new ArrayList<>();

        List<Map> oList = MyPoiUtil.listTableData(inputStream, 1, 0, false);

        for (int i = 0; i < oList.size(); i++) {
            Map map = oList.get(i);
            //打款日期
            String verifyTime = map.getOrDefault(1, "").toString();
            //备注
            String remark = map.getOrDefault(2, "").toString();
            //收入金额
            String incomeMoney = map.getOrDefault(3, "").toString();
            //合同号
            String contractNo = map.getOrDefault(4, "").toString();
            //部门
            String deptName = map.getOrDefault(5, "").toString();

            if (Strings.isNullOrEmpty(verifyTime) || Strings.isNullOrEmpty(incomeMoney) || Strings.isNullOrEmpty(deptName)) {
                Assert.state(false, "第" + (i + 1) + "行存在空数据！");
            }

           /* HrDept hrDept = hrDeptService.selectByName(deptName);
            if (hrDept == null) {
                Map map2 = new LinkedHashMap();
                map2.put("打款日期",map.getOrDefault(1,"").toString());
                map2.put("备注",map.getOrDefault(2,"").toString());
                map2.put("收入金额",map.getOrDefault(3,"").toString());
                map2.put("合同号",map.getOrDefault(4,"").toString());
                map2.put("部门名称",map.getOrDefault(5,"").toString());
                map2.put("导入失败原因"," 部门名称："+deptName + "未匹配到对应数据");
                list.add(map2);
                continue;
            }*/

            //判断合同号是否存在 不存在 加入返回文件列表
            FiveBusinessContractLibraryDto contractLibrary = fiveBusinessContractLibraryService.getModelByContractNo(contractNo);
            if(contractLibrary==null){
                Map map2 = new LinkedHashMap();
                map2.put("打款日期",map.getOrDefault(1,"").toString());
                map2.put("备注",map.getOrDefault(2,"").toString());
                map2.put("收入金额",map.getOrDefault(3,"").toString());
                map2.put("合同号",map.getOrDefault(4,"").toString());
                map2.put("部门名称",map.getOrDefault(5,"").toString());
                map2.put("导入失败原因"," 合同号："+contractNo + "未匹配到对应数据");
                list.add(map2);
                continue;
            }

            BusinessIncome item;
            //同一天 同一个合同 收到相同金额  视为跟新
            Map map1 = new HashMap();
            map1.put("verifyTime", verifyTime);
            map1.put("deptName", deptName);
            map1.put("incomeMoney", MyStringUtil.getMoneyW(incomeMoney));
            map1.put("contractNo",contractNo);
            List<BusinessIncome> incomes = businessIncomeMapper.selectAll(map1);
            if (incomes.size() > 0) {
                item = incomes.get(0);
                item.setCreator(userLogin);
                item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
                item.setGmtCreate(new Date());
                item.setGmtModified(new Date());
                item.setDeleted(false);

                item.setRemark(remark);
                item.setIncomeMoney(MyStringUtil.getMoneyW(incomeMoney));
                item.setIncomeMoneyMax(moneyTurnCapital(Double.valueOf(item.getIncomeMoney())));

                /*HrDept hrDept = hrDeptService.selectByName(deptName);
                if (hrDept != null) {
                    item.setDeptName(hrDept.getName());
                    item.setDeptId(hrDept.getId());
                } else {
                    Assert.state(false, "第" + (i + 1) + "行 部门名称："+deptName + "未匹配到对应数据");
                }*/

                ModelUtil.setNotNullFields(item);
                businessIncomeMapper.updateByPrimaryKey(item);
            } else {
                try{
                    //新增
                    item = new BusinessIncome();

                    /*item.setDeptName(hrDept.getName());
                    item.setDeptId(hrDept.getId());*/
                    item.setDeptName(contractLibrary.getDeptName());
                    item.setDeptId(contractLibrary.getDeptId());

                    item.setCreator(userLogin);
                    item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
                    item.setGmtCreate(new Date());
                    item.setGmtModified(new Date());
                    item.setDeleted(false);

                    item.setVerifyTime(verifyTime);

                    item.setContractLibraryId(contractLibrary.getId());
                    item.setIncomeConfirmId(-1);
                    item.setInvoiceId(0);
                    item.setInvoiceNo("");
                    item.setLegalDept("");
                    item.setContractName(contractLibrary.getContractName());
                    item.setContractNo(contractLibrary.getContractNo());
                    item.setProjectName(contractLibrary.getProjectName());
                    item.setProjectNo(contractLibrary.getProjectNo());
                    item.setRecordProjectName(contractLibrary.getRecordProjectName());
                    item.setContractMoney(contractLibrary.getContractMoney());
                    item.setContractIncomeMoney(contractLibrary.getContractIncomeMoney());
                    item.setManagePercent("");

                    item.setIncomeMoney(MyStringUtil.getMoneyW(incomeMoney));
                    item.setIncomeMoneyMax(moneyTurnCapital(Double.valueOf(item.getIncomeMoney())));

                    item.setVerifyTime(verifyTime);

                    item.setRemark(remark);
                    item.setProcessInstanceId("0");
                    item.setProcessEnd(true);
                    ModelUtil.setNotNullFields(item);

                    businessIncomeMapper.insert(item);
                    String processKey = EdConst.FIVE_FINANCE_INCOME;
                    String businessKey = processKey + "_" + item.getId();
                    item.setProcessInstanceId("0");
                    item.setBusinessKey(businessKey);
                    item.setProcessEnd(true);
                    businessIncomeMapper.updateByPrimaryKey(item);
                    //绑定合同
                    contractLibrary.setIncomeIds(contractLibrary.getIncomeIds()+","+item.getId());
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(contractLibrary);
                    //跟新合同认领金额
                    fiveBusinessContractLibraryService.getIncomeMoney(item.getId());
                    Map map2 = new LinkedHashMap();
                    map2.put("打款日期",map.getOrDefault(1,"").toString());
                    map2.put("备注",map.getOrDefault(2,"").toString());
                    map2.put("收入金额",map.getOrDefault(3,"").toString());
                    map2.put("合同号",map.getOrDefault(4,"").toString());
                    map2.put("部门名称",map.getOrDefault(5,"").toString());
                    map2.put("导入失败原因","");
                    list.add(map2);
                }catch(Exception e){
                    Map map2 = new LinkedHashMap();
                    map2.put("打款日期",map.getOrDefault(1,"").toString());
                    map2.put("备注",map.getOrDefault(2,"").toString());
                    map2.put("收入金额",map.getOrDefault(3,"").toString());
                    map2.put("合同号",map.getOrDefault(4,"").toString());
                    map2.put("部门名称",map.getOrDefault(5,"").toString());
                    map2.put("导入失败原因","未知错误！检查数据合理性");
                    list.add(map2);
                }
            }
        }

        return list;
    }

    //确认 取消收入
    public int getNotarizeIncome(int incomeId, String userLogin) {
        BusinessIncome item = businessIncomeMapper.selectByPrimaryKey(incomeId);
        //判断是否选择合同
        Assert.state(item.getContractLibraryId()!=0,"该收入已被认领，无法取消确认！");
        if (item.getProcessEnd()) {
            item.setProcessEnd(false);
            //合同收入跟新 减少
            fiveBusinessContractLibraryService.subIncomeMoney(item.getId());
            //如果有发票 发票收入跟新
            if(item.getInvoiceId()!=0){
                FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
                invoice.setInvoiceGetMoney(MyStringUtil.getNewSubMoney(invoice.getInvoiceGetMoney(),item.getIncomeMoney(),8));
                //还原发票正在认领金额
                invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoneyIng(),item.getIncomeMoney(),8));
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
            }else{
                //没有发票  跟新合同预收款
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                library.setPreContractMoney(MyStringUtil.getNewSubMoney(library.getPreContractMoney(),item.getIncomeMoney(),8));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
        } else {
            item.setProcessEnd(true);
            //合同收入跟新  增加
            fiveBusinessContractLibraryService.getIncomeMoney(item.getId());
            //如果有发票 发票收入跟新
            if(item.getInvoiceId()!=0){
                FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
                invoice.setInvoiceGetMoney(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoney(),item.getIncomeMoney()));
                //还原发票正在认领金额
                invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewSubMoney(invoice.getInvoiceGetMoneyIng(),item.getIncomeMoney()));
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
            }else{
                //没有发票  跟新合同预收款
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                library.setPreContractMoney(MyStringUtil.getNewAddMoney(library.getPreContractMoney(),item.getIncomeMoney()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
        }
        businessIncomeMapper.updateByPrimaryKey(item);
        return item.getId();
    }
}
