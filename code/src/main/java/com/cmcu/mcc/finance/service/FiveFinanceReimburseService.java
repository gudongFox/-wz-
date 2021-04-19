package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.*;
import com.cmcu.mcc.finance.dto.FiveFinanceLoanDto;
import com.cmcu.mcc.finance.dto.FiveFinanceReimburseDto;
import com.cmcu.mcc.finance.dto.FiveFinanceTransferAccountsDetailDto;
import com.cmcu.mcc.finance.entity.*;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
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
public class FiveFinanceReimburseService {
    @Resource
    FiveFinanceReimburseMapper fiveFinanceReimburseMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveFinanceReimburseDetailMapper fiveFinanceReimburseDetailMapper;
    @Autowired
    FiveFinanceReimburseDeductionMapper fiveFinanceReimburseDeductionMapper;
    @Autowired
    FiveFinanceLoanMapper fiveFinanceLoanMapper;
    @Resource
    FiveFinanceLoanService fiveFinanceLoanService;
    @Autowired
    FiveFinanceRefundMapper fiveFinanceRefundMapper;
    @Resource
    ProcessQueryService processQueryService;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveFinanceReimburse item = fiveFinanceReimburseMapper.selectByPrimaryKey(id);

        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

        List<FiveFinanceReimburseDetail> details = listDetail(id);
        for(FiveFinanceReimburseDetail detail:details){
            detail.setDeleted(true);
            fiveFinanceReimburseDetailMapper.updateByPrimaryKey(detail);
        }
        List<FiveFinanceReimburseDeduction> deductions = listDeduction(id);
        for(FiveFinanceReimburseDeduction deduction:deductions){
            deduction.setDeleted(true);
            fiveFinanceReimburseDeductionMapper.updateByPrimaryKey(deduction);
        }

    }
    public void removeDeduction(int id,String userLogin){
        FiveFinanceReimburseDeduction item = fiveFinanceReimburseDeductionMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //删除关联
        if(item.getRelevanceType().equals("loan")){
            FiveFinanceLoan loan = fiveFinanceLoanMapper.selectByPrimaryKey(item.getRelevanceId());
            loan.setReimburseId(0);
            fiveFinanceLoanMapper.updateByPrimaryKey(loan);
        }else if(item.getRelevanceType().equals("refund")){
            FiveFinanceRefund refund = fiveFinanceRefundMapper.selectByPrimaryKey(item.getRelevanceId());
            refund.setReimburseId(0);
            fiveFinanceRefundMapper.updateByPrimaryKey(refund);
        }
        item.setDeleted(true);
        fiveFinanceReimburseDeductionMapper.updateByPrimaryKey(item);
    }


    public void update(FiveFinanceReimburseDto dto){
        FiveFinanceReimburse model = getModelById(dto.getId());
        model.setMoneyItem(dto.getMoneyItem());
        if(dto.getIsProject()){
            model.setProjectId(dto.getProjectId());
            model.setProjectName(dto.getProjectName());
            model.setBusinessManager(dto.getBusinessManager());
            model.setBusinessManagerName(dto.getBusinessManagerName());
        }else{
            model.setProjectId(0);
            model.setProjectName("");
            model.setBusinessManager("");
            model.setBusinessManagerName("");
        }

        model.setProjectType(dto.getProjectType());
        model.setGreaterThan(dto.getGreaterThan());
        model.setReceiptsNumber(dto.getReceiptsNumber());
        model.setApplicant(dto.getApplicant());
        model.setApplicantName(dto.getApplicantName());
        model.setApplicantTime(dto.getApplicantTime());
        model.setAccountName(dto.getAccountName());
        model.setBankName(dto.getBankName());
        model.setBankAccount(dto.getBankAccount());
        model.setReceive(dto.getReceive());
        model.setReceiveName(dto.getReceiveName());
        model.setReceiveDeptId(dto.getReceiveDeptId());
        model.setReceiveDeptName(dto.getReceiveDeptName());
        model.setBankAccount(dto.getBankAccount());
        model.setCount(dto.getCount());
        model.setReceiptsNumber(dto.getReceiptsNumber());
        model.setReceiveBank(dto.getReceiveBank());
        model.setReceiveAccount(dto.getReceiveAccount());
        model.setScientific(dto.getScientific());
        model.setTitle(dto.getTitle());

        model.setExtraApprove(dto.getExtraApprove());
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());


        fiveFinanceReimburseMapper.updateByPrimaryKey(model);

        List<FiveFinanceReimburseDetail> modelDetailList=listDetail(dto.getId());

        int attribute=0;
        int dept=0;
        int phone=0;
        int train=0;
        // 如果发起人是部门负责人
        if (selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())) {
            dept=1;
        }
        List<String> attributeList=new ArrayList<>();
        for(FiveFinanceReimburseDetail modelDetail:modelDetailList){
            if(modelDetail.getCostProject().contains("咨询劳务")){
                attributeList.add("2169");
                attribute = 1;
            } else if(modelDetail.getCostProject().contains("股权投资")){
                attributeList.add("2169");
                attribute = 1;
            } else if(modelDetail.getCostProject().contains("软件")){
                attributeList.add("2887");
                attribute = 1;
            } else if(modelDetail.getCostProject().contains("团体会费")){
                attributeList.add("2887");
                attribute = 1;
            } else if(modelDetail.getCostProject().contains("图书资料费")){
                attributeList.add("1543");
                attribute = 1;
            } else if(modelDetail.getCostProject().contains("固定资产")){
                attributeList.add("2275");
                attribute = 1;
            } else if(modelDetail.getCostProject().contains("培训")) {
                attributeList.add("2767");
                train=1;
            }
            if(modelDetail.getCostProject().contains("通讯费")){
                phone=1;
            }
        }
        Map variables = Maps.newHashMap();
        variables.put("balance", dto.getGreaterThan());//抵扣后金额小于申请金额 true
        variables.put("phone", phone);
        variables.put("attributeList", attributeList);//归口负责人
        variables.put("train", train);
        //totalApplyMoney 字段未存数据库，需判断的单位为元
        variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=10000.00?true:false);
        variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=30000.00?true:false);
        variables.put("flag2", Double.valueOf(dto.getTotalApplyMoney())>=50000.00?true:false);
        variables.put("attribute", attribute);
        variables.put("dept", dept);//发起人是否部门负责人
        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务确认
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门领导
        variables.put("deptLeader",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//分管领导
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//财务负责人
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(18));//主管副职领导
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("总会计师"));//总会计师
        variables.put("generalManager", hrEmployeeService.selectUserByPositionName("总经理"));//总经理
        variables.put("financialAccount", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务核算
        //variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目
        if (model.getBusinessKey().indexOf("Red") != -1) {//红河项目
            variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目

            variables.put("businessManager", dto.getBusinessManager());//项目主管副总？？

        }
        else if (model.getBusinessKey().indexOf("Build") != -1) {//建研院
            //中层正职
            int middleLeader=0;
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(dto.getCreator())){
                middleLeader=1;
            }

            variables.put("middleLeader", middleLeader);
            variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目
            variables.put("vicePresident", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//主管副总

        }
        else if (model.getBusinessKey().indexOf("Function") != -1) {//职能部门
            HrEmployeeSysDto modelByUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int record=0;
            if(modelByUserLogin.getDeptCode().equals("58")||modelByUserLogin.getDeptCode().equals("67")){
                record=1;
            }
            variables.put("record", record);
            variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目

        }
        else if(model.getBusinessKey().indexOf("Common") != -1){//生产部门
            int project=0;
            if(model.getProjectName().length()==0){
                if(selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())){
                    project=2;
                }else{
                    project=1;
                }
            }
            variables.put("project", project);
            variables.put("projectManager", model.getBusinessManager());//项目主管副总
            variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目
            variables.put("deputy", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));


        }
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }
    
    public FiveFinanceReimburseDto getModelById(int id){

        return getDto(fiveFinanceReimburseMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceReimburseDto getDto(FiveFinanceReimburse item) {
        FiveFinanceReimburseDto dto=FiveFinanceReimburseDto.adapt(item);
        if(dto.getProjectId()!=0){
            dto.setIsProject(true);
        }

        CustomSimpleProcessInstance customSimpleProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
        dto.setProcessName(customSimpleProcessInstance.getCurrentTaskName());

        if (!item.getProcessEnd() && customSimpleProcessInstance.isFinished()) {
            dto.setProcessEnd(true);
            fiveFinanceReimburseMapper.updateByPrimaryKey(dto);
        }
        if (StringUtils.isEmpty(customSimpleProcessInstance.getCurrentTaskName())) {
            dto.setProcessName("流程已结束");
        }

        //计算子表总金额
        String applyMoney="0";
        String confirmMoney="0";
        List<FiveFinanceReimburseDetail> detailList=listDetail(item.getId());
        for(FiveFinanceReimburseDetail detail:detailList){
            applyMoney=MyStringUtil.getNewAddMoney(applyMoney,detail.getApplyMoney());
            confirmMoney=MyStringUtil.getNewAddMoney(confirmMoney,detail.getConfirmMoney());
            //totalCount=MyStringUtil.getNewAddMoney(totalCount,detail.getCount());
        }
        dto.setTotalApplyMoney(applyMoney);
        dto.setTotalConfirmMoney(confirmMoney);
        //计算抵扣金额
        List<FiveFinanceReimburseDeduction> details = listDeduction(item.getId());
        String totalLoan ="0";
        String totalRefund ="0";
        String totalDeduction ="0";
        String loanRemain ="0";
        for(FiveFinanceReimburseDeduction detail:details){
            if(detail.getRelevanceType().equals("loan")){
                totalLoan = MyStringUtil.getNewAddMoney(totalLoan,detail.getRelevanceMoney());
                loanRemain = MyStringUtil.getNewAddMoney(loanRemain,detail.getRelevanceRemainMoney());
            }
            if(detail.getRelevanceType().equals("refund")){
                totalRefund = MyStringUtil.getNewAddMoney(totalRefund,detail.getRelevanceMoney());
            }
        }
        String deductionMoney = MyStringUtil.getNewSubMoney(totalLoan, totalRefund);
        dto.setDeductionMoney(deductionMoney);
        //金额 转为元  借款中计算剩余金额
        dto.setLoanRemainMoney(loanRemain);

        String shouldRefundMoney = MyStringUtil.getNewSubMoney(deductionMoney,dto.getTotalApplyMoney());
        dto.setShouldRefundMoney(shouldRefundMoney);
        if(Double.valueOf(shouldRefundMoney)>=0.0){
            //还需要还款
            dto.setGreaterThan(false);
        }else {
            dto.setGreaterThan(true);
        }

        dto.setTotalApplyMoney(MyStringUtil.moneyToString(dto.getTotalApplyMoney(),2));
        dto.setTotalConfirmMoney(MyStringUtil.moneyToString(dto.getTotalConfirmMoney(),6));
        dto.setDeductionMoney(MyStringUtil.moneyToString(dto.getDeductionMoney(),6));
        return dto;
    }
    public FiveFinanceReimburseDetail getDetailDto(FiveFinanceReimburseDetail item) {

        //万元 转化为 元
        item.setApplyMoney(MyStringUtil.getMoneyY(item.getApplyMoney()));
        item.setConfirmMoney(MyStringUtil.moneyToString(item.getConfirmMoney(),6));
        return item;
    }

    public int getNewModel(String userLogin,String uiSref){
        FiveFinanceReimburse item=new FiveFinanceReimburse();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        if(uiSref.indexOf("Build") != -1){
            item.setDeptId(158);
            item.setDeptName("建研院总部");
        }else{
            item.setDeptId(hrEmployeeDto.getDeptId());
            item.setDeptName(hrEmployeeDto.getDeptName());
        }
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        item.setDeptName(selectEmployeeService.getHeadDeptName(hrEmployeeDto.getDeptId()));

        item.setApplicant(hrEmployeeDto.getUserLogin());
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());
        item.setScientific("否");
        item.setGreaterThan(false);
        item.setExtraApprove(false);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setCount(0.0);
        item.setApplicantTime(MyDateUtil.getStringToday());
        item.setScientific("否");

        ModelUtil.setNotNullFields(item);
        fiveFinanceReimburseMapper.insert(item);
        String title = "费用报销:" + item.getDeptName();

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", title);

        if (uiSref.indexOf("Red") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_REIMBURSE_RED + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_REIMBURSE_RED, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Build") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_REIMBURSE_BUILD + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_REIMBURSE_BUILD, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Function") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_REIMBURSE_FUNCTION + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_REIMBURSE_FUNCTION, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else {
            String businessKey = EdConst.FIVE_FINANCE_REIMBURSE_COMMON + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_REIMBURSE_COMMON, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        }

        item.setTitle(title);
        fiveFinanceReimburseMapper.updateByPrimaryKey(item);
        //自动生成单据号
        String receiptsNumber = getReceiptsNumber(item.getId());
        item.setReceiptsNumber(receiptsNumber);
        return item.getId();
    }
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        if (uiSref.indexOf("Red") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_REIMBURSE_RED);
        } else if (uiSref.indexOf("Build") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_REIMBURSE_BUILD);
        } else if (uiSref.indexOf("Function") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_REIMBURSE_FUNCTION);
        } else {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_REIMBURSE_COMMON);
        }

         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceReimburseMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceReimburse item=(FiveFinanceReimburse)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void updateDetail(FiveFinanceReimburseDetail item){
        //如果申请金额 大于 预算剩余金额 提示
        Assert.state(Double.valueOf(MyStringUtil.getMoneyW(item.getApplyMoney()))<=Double.valueOf(item.getBudgetBalance()),"申请金额 大于 预算剩余金额!");
        if (item.getFlag() == 1){
            fiveFinanceReimburseDetailMapper.insert(item);
        }
        FiveFinanceReimburseDetail model = fiveFinanceReimburseDetailMapper.selectByPrimaryKey(item.getId());
        model.setProjectType(item.getProjectType());
        model.setCostProject(item.getCostProject());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setProject(item.getProject());

        //元 转换为 万元
        model.setApplyMoney(MyStringUtil.getMoneyW(item.getApplyMoney()));
        model.setConfirmMoney(MyStringUtil.moneyToString(item.getConfirmMoney()));
        model.setCount(MyStringUtil.moneyToString(item.getCount()));
        model.setAccountSubject(item.getAccountSubject());
        model.setApplicant(item.getApplicant());
        model.setApplicantName(item.getApplicantName());
        model.setRemark(item.getRemark());

        model.setBudgetId(item.getBudgetId());
        model.setBudgetBalance(item.getBudgetBalance());
        model.setBudgetNo(item.getBudgetNo());
        model.setBudgetType(item.getBudgetType());
        fiveFinanceReimburseDetailMapper.updateByPrimaryKey(model);
    }

    public FiveFinanceReimburseDetail getNewModelDetail(int  id,String userLogin,String uiSref){
        FiveFinanceReimburseDetail item=new FiveFinanceReimburseDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        if(uiSref.indexOf("Build")!=-1){
            item.setDeptName("建研院总部");
        }else {
            item.setDeptName(hrEmployeeDto.getDeptName());
        }
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setReimburseId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setConfirmMoney(MyStringUtil.moneyToString("0"));
        item.setApplyMoney(MyStringUtil.moneyToString("0"));
        item.setCount(MyStringUtil.moneyToString("0"));
        item.setFlag(1);
        ModelUtil.setNotNullFields(item);
        return getDetailDto(item);
    }

    public FiveFinanceReimburseDetail getModelDetailById(int id){
        FiveFinanceReimburseDetail detail = fiveFinanceReimburseDetailMapper.selectByPrimaryKey(id);
        if(Double.valueOf(detail.getApplyMoney()).equals(0.0)){
            detail.setApplyMoney("");
        }
        if(Double.valueOf(detail.getConfirmMoney()).equals(0.0)){
            detail.setConfirmMoney("");
        }
        return getDetailDto(detail);
    }

    public List<FiveFinanceReimburseDetail> listDetail(int id){
        List<FiveFinanceReimburseDetail> list = new ArrayList<>();
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("reimburseId",id);//小写
        List<FiveFinanceReimburseDetail> olist = fiveFinanceReimburseDetailMapper.selectAll(params);
        for(FiveFinanceReimburseDetail detail:olist){
            list.add(getDetailDto(detail));
        }
        return list;
    }
    public List<FiveFinanceReimburseDeduction> listDeduction(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("reimburseId",id);//小写
        List<FiveFinanceReimburseDeduction> list = fiveFinanceReimburseDeductionMapper.selectAll(params);
        for(FiveFinanceReimburseDeduction deduction:list){
            //转为 元
            deduction.setRelevanceMoney(MyStringUtil.getMoneyY(deduction.getRelevanceMoney()));
            deduction.setRelevanceRemainMoney(MyStringUtil.getMoneyY(deduction.getRelevanceRemainMoney()));
        }
        return list;
    }

    public void removeDetail(int id){
        FiveFinanceReimburseDetail model = fiveFinanceReimburseDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceReimburseDetailMapper.updateByPrimaryKey(model);
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceReimburse item = fiveFinanceReimburseMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());
        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);
        List<FiveFinanceReimburseDetail> materialPurchaseDetails = fiveFinanceReimburseDetailMapper.selectAll (map);
        data.put("materialPurchaseDetails",materialPurchaseDetails);
/*        Double sum=0.0d;
        for (FiveFinanceReimburseDetail detail:materialPurchaseDetails) {
            sum+=Double.valueOf(detail.getBookNumber());
        }
        data.put("sum",sum);*/

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));
        return data;
    }

    public void saveSelectedDeduction(int reimburseId,int id, String type) {
        FiveFinanceReimburseDto reimburseDto = getModelById(reimburseId);
        if(type.equals("loan")){
            FiveFinanceLoanDto loan = fiveFinanceLoanService.getModelById(id);
            //判断 跟新还是增加
            Map map =new HashMap();
            map.put("deleted",false);
            map.put("reimburseId",reimburseId);
            map.put("relevanceId",id);
            map.put("relevanceType","loan");
            List<FiveFinanceReimburseDeduction> fiveFinanceReimburseDeductions = fiveFinanceReimburseDeductionMapper.selectAll(map);
            if(fiveFinanceReimburseDeductions.size()==0){
                FiveFinanceReimburseDeduction deduction = new FiveFinanceReimburseDeduction();
                deduction.setReimburseId(reimburseId);
                deduction.setRelevanceBusinessKey(loan.getBusinessKey());
                deduction.setRelevanceId(loan.getId());
                deduction.setRelevanceName(loan.getTitle()+":"+loan.getCreatorName());
                deduction.setRelevanceType("loan");
                //万元存储
                deduction.setRelevanceMoney(MyStringUtil.getMoneyW(loan.getLoanMoney()));
                deduction.setRelevanceRemainMoney(MyStringUtil.getMoneyW(loan.getRemainMoney()));

                deduction.setRelevanceTime(loan.getApplicantTime());
                deduction.setRelevanceRemark(loan.getLoanReason());
                deduction.setDeptId(loan.getDeptId());
                deduction.setDeptName(loan.getDeptName());
                deduction.setCreator(reimburseDto.getCreator());
                deduction.setCreatorName(reimburseDto.getCreatorName());
                deduction.setGmtModified(new Date());
                deduction.setGmtCreate(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceReimburseDeductionMapper.insert(deduction);
                //关联id
                loan.setReimburseId(reimburseId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }else{
                FiveFinanceReimburseDeduction deduction = fiveFinanceReimburseDeductions.get(0);
                deduction.setReimburseId(reimburseId);
                deduction.setRelevanceId(loan.getId());
                deduction.setRelevanceBusinessKey(loan.getBusinessKey());
                deduction.setRelevanceName(loan.getTitle()+":"+loan.getCreatorName());
                deduction.setRelevanceType("loan");
                //万元存储
                deduction.setRelevanceMoney(MyStringUtil.getMoneyW(loan.getLoanMoney()));
                deduction.setRelevanceRemainMoney(MyStringUtil.getMoneyW(loan.getRemainMoney()));

                deduction.setRelevanceTime(loan.getApplicantTime());
                deduction.setRelevanceRemark(loan.getLoanReason());
                deduction.setDeptId(loan.getDeptId());
                deduction.setDeptName(loan.getDeptName());
                deduction.setCreator(reimburseDto.getCreator());
                deduction.setCreatorName(reimburseDto.getCreatorName());
                deduction.setGmtModified(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceReimburseDeductionMapper.updateByPrimaryKey(deduction);
                //关联id
                loan.setReimburseId(reimburseId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }
        }else if(type.equals("refund")){
            FiveFinanceRefund refund = fiveFinanceRefundMapper.selectByPrimaryKey(id);
            //判断 跟新还是增加
            Map map =new HashMap();
            map.put("deleted",false);
            map.put("reimburseId",reimburseId);
            map.put("relevanceId",id);
            map.put("relevanceType","refund");
            List<FiveFinanceReimburseDeduction> fiveFinanceReimburseDeductions = fiveFinanceReimburseDeductionMapper.selectAll(map);
            if(fiveFinanceReimburseDeductions.size()==0){
                FiveFinanceReimburseDeduction deduction = new FiveFinanceReimburseDeduction();
                deduction.setReimburseId(reimburseId);
                deduction.setRelevanceBusinessKey(refund.getBusinessKey());
                deduction.setRelevanceId(refund.getId());
                deduction.setRelevanceName(refund.getTitle()+":"+refund.getCreatorName());
                deduction.setRelevanceType("refund");
                if(refund.getShouldRefundMoney().equals("0")||refund.getShouldRefundMoney().equals("")){
                    deduction.setRelevanceMoney(refund.getTotalRefundMoney());
                }else{
                    deduction.setRelevanceMoney(refund.getShouldRefundMoney());
                }

                deduction.setRelevanceTime(refund.getApplicantTime());
                deduction.setRelevanceRemark(refund.getRemark());
                deduction.setDeptId(refund.getDeptId());
                deduction.setDeptName(refund.getDeptName());

                deduction.setCreator(reimburseDto.getCreator());
                deduction.setCreatorName(reimburseDto.getCreatorName());
                deduction.setGmtModified(new Date());
                deduction.setGmtCreate(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceReimburseDeductionMapper.insert(deduction);
                //关联id
                refund.setReimburseId(reimburseId);
                fiveFinanceRefundMapper.updateByPrimaryKey(refund);
            }else{
                FiveFinanceReimburseDeduction deduction = fiveFinanceReimburseDeductions.get(0);
                deduction.setReimburseId(reimburseId);
                deduction.setRelevanceBusinessKey(refund.getBusinessKey());
                deduction.setRelevanceId(refund.getId());
                deduction.setRelevanceName(refund.getTitle()+":"+refund.getCreatorName());
                deduction.setRelevanceType("refund");
                deduction.setRelevanceMoney(refund.getTotalRefundMoney());
                deduction.setRelevanceTime(refund.getApplicantTime());
                deduction.setRelevanceRemark(refund.getRemark());
                deduction.setDeptId(refund.getDeptId());
                deduction.setDeptName(refund.getDeptName());
                deduction.setCreator(reimburseDto.getCreator());
                deduction.setCreatorName(reimburseDto.getCreatorName());
                deduction.setGmtModified(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceReimburseDeductionMapper.updateByPrimaryKey(deduction);
                //关联id
                refund.setReimburseId(reimburseId);
                fiveFinanceRefundMapper.updateByPrimaryKey(refund);
            }
        }
    }

    public void getNewReplenishRefund(int reimburseId, String userLogin) {
        FiveFinanceReimburseDto reimburseDto = getModelById(reimburseId);

        FiveFinanceRefund item=new FiveFinanceRefund();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setApplicant(hrEmployeeDto.getUserLogin());
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(reimburseDto.getCreator());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setApplicantTime(MyDateUtil.getStringToday());

        ModelUtil.setNotNullFields(item);
        fiveFinanceRefundMapper.insert(item);
        String title = "补充还款:" + item.getDeptName();

        String businessKey= EdConst.FIVE_FINANCE_REFUND+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", reimburseDto.getCreator());
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription",title);
        //是否补充还款
        variables.put("replenishRefund",true);

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_REFUND,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setTitle(title);

        //额外信息
        item.setReimburseId(reimburseId);
        item.setShouldRefundMoney(reimburseDto.getShouldRefundMoney());

        fiveFinanceRefundMapper.updateByPrimaryKey(item);
        //关联还款
        saveSelectedDeduction(reimburseId,item.getId(),"refund");

    }

    /**
     借款 01
     费用报销 02
     差旅费报销 03
     退款（费用申请）04

     职能单位Z
     生产单位S
     红河项目H
     建研院总部J

     2021 03 12 01（类型编号）001（该类型的第几个）Z 20210312 01001
     */
    public String getReceiptsNumber(int id){
        try{
            FiveFinanceReimburse item = fiveFinanceReimburseMapper.selectByPrimaryKey(id);
            FiveFinanceReimburseDto reimburseDto =getModelById(id);
            String newReceiptsNumber ="";


            //单位代码
            String deptCode="";

            if(item.getBusinessKey().contains("Red")){
                deptCode="H";//红河项目
            }else {
                if (hrDeptService.getModelById(item.getDeptId()).getDeptType().equals("设计")){
                    deptCode="S";//生产单位
                }else if(hrDeptService.getModelById(item.getDeptId()).getDeptType().equals("职能")){
                    deptCode="Z";//职能单位
                }else if(item.getBusinessKey().contains("Build")){
                    deptCode = "J";//建研院总部
                }
            }
            //日期代码
            String date=MyDateUtil.dateToStr1(item.getGmtCreate());

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            // params.put("projectNoHead",year+deptCode+typeCode);
            List<FiveFinanceReimburse> fiveFinanceReimburses = fiveFinanceReimburseMapper.selectAll(params);
            int size=0;
            //判断顺序代码最大值
            if (!fiveFinanceReimburses.isEmpty()){
                for (FiveFinanceReimburse fiveFinanceReimburse:fiveFinanceReimburses){
                    String receiptsNumber = fiveFinanceReimburse.getReceiptsNumber();
                    if(fiveFinanceReimburse.getId()!=id&&StringUtils.isNotEmpty(receiptsNumber)){
                        String maxSize=  receiptsNumber.substring(receiptsNumber.length()-3);
                        if (size<Integer.parseInt(maxSize)){
                            size = Integer.parseInt(maxSize);
                        }
                    }
                }
            }
            size=size+1;

            String format=String.format("%03d", size);//format为顺序号限定10进制补零
            //部门+时间+类型+编号
            newReceiptsNumber=newReceiptsNumber+deptCode+date+"02"+format;
            reimburseDto.setReceiptsNumber(newReceiptsNumber);
            update(reimburseDto);
            return newReceiptsNumber;

        }catch (Exception e){
            Assert.state(false,"请准确填写，预计开始时间、xxx、xxx！");
            return "";
        }
    }
}
