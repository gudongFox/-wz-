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

import com.cmcu.mcc.finance.dto.FiveFinanceTravelExpenseDto;
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
public class FiveFinanceTravelExpenseService {
    @Resource
    FiveFinanceTravelExpenseMapper fiveFinanceTravelExpenseMapper;
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
    FiveFinanceTravelExpenseDetailMapper fiveFinanceTravelExpenseDetailMapper;
    @Autowired
    FiveFinanceTravelDeductionMapper fiveFinanceTravelDeductionMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    FiveFinanceLoanMapper fiveFinanceLoanMapper;
    @Resource
    FiveFinanceLoanService fiveFinanceLoanService;
    @Autowired
    FiveFinanceRefundMapper fiveFinanceRefundMapper;
    @Resource
    ProcessQueryService processQueryService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveFinanceTravelExpense item = fiveFinanceTravelExpenseMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

        List<FiveFinanceTravelExpenseDetail> details = listDetail(id);
        for(FiveFinanceTravelExpenseDetail detail:details){
            detail.setDeleted(true);
            fiveFinanceTravelExpenseDetailMapper.updateByPrimaryKey(detail);
        }

        List<FiveFinanceTravelDeduction> deductions = listDeduction(id);
        for(FiveFinanceTravelDeduction deduction:deductions){
            deduction.setDeleted(true);
            fiveFinanceTravelDeductionMapper.updateByPrimaryKey(deduction);
        }
    }

    public void removeDeduction(int id,String userLogin){
        FiveFinanceTravelDeduction item = fiveFinanceTravelDeductionMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //删除关联
        if(item.getRelevanceType().equals("loan")){
            FiveFinanceLoan loan = fiveFinanceLoanMapper.selectByPrimaryKey(item.getRelevanceId());
            loan.setTravelId(0);
            fiveFinanceLoanMapper.updateByPrimaryKey(loan);
        }else if(item.getRelevanceType().equals("refund")){
            FiveFinanceRefund refund = fiveFinanceRefundMapper.selectByPrimaryKey(item.getRelevanceId());
            refund.setTravelId(0);
            fiveFinanceRefundMapper.updateByPrimaryKey(refund);
        }
        item.setDeleted(true);
        fiveFinanceTravelDeductionMapper.updateByPrimaryKey(item);
    }

    public void update(FiveFinanceTravelExpenseDto dto){
        FiveFinanceTravelExpense model = fiveFinanceTravelExpenseMapper.selectByPrimaryKey(dto.getId());
        model.setStartTime(dto.getStartTime());
        model.setEndTime(dto.getEndTime());
        model.setTravelPlace(dto.getTravelPlace());
        model.setTravelExpense(dto.getTravelExpense());
        model.setScientific(dto.getScientific());
        model.setProjectType(dto.getProjectType());
        model.setExceedStandard(dto.getExceedStandard());
        model.setExceedProjectId(dto.getExceedProjectId());
        model.setExceedProjectName(dto.getExceedProjectName());
        model.setAttachedList(dto.getAttachedList());
        model.setRefundAmount(dto.getRefundAmount());
        model.setChargeAgainst(dto.getChargeAgainst());
        model.setTotalCount(dto.getTotalCount());
        model.setReceiptsNumber(dto.getReceiptsNumber());
        model.setApplyRefundTime(dto.getApplyRefundTime());
        model.setTravelPlaceType(dto.getTravelPlaceType());
        if(dto.getIsProject()){
            model.setProjectId(dto.getProjectId());
            model.setProjectName(dto.getProjectName());
            model.setBussineManager(dto.getBussineManager());
            model.setBussineManagerName(dto.getBussineManagerName());
        }else{
            model.setProjectId(0);
            model.setProjectName("");
            model.setBussineManager("");
            model.setBussineManagerName("");
        }

        model.setApplicant(dto.getApplicant());
        model.setApplicantName(dto.getApplicantName());
        model.setPayName(dto.getPayName());
        model.setPayBank(dto.getPayBank());
        model.setPayAccount(dto.getPayAccount());
        model.setReceiveName(dto.getReceiveName());
        model.setReceiveBank(dto.getReceiveBank());
        model.setReceiveAccount(dto.getReceiveAccount());

        model.setTitle(dto.getTitle());
        model.setApproval(dto.getApproval());
        model.setExtraApprove(dto.getExtraApprove());//特批
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());



        fiveFinanceTravelExpenseMapper.updateByPrimaryKey(model);


        selectEmployeeService.getCompanyLeaders();

        Map variables = Maps.newHashMap();
        variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=1.0?true:false);
        variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=3.0?true:false);
        variables.put("flag2", Double.valueOf(dto.getTotalApplyMoney())>=5.0?true:false);
        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务确认
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门领导
        variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目
        variables.put("train", dto.getTravelExpense().contains("是")?true:false);//培训费
        variables.put("humanDeptChargeMan", selectEmployeeService.getDeptChargeMen(38));//人力负责人
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//财务负责人
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(18));//财务主管副总
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("总会计师"));//总会计师
        variables.put("generalManager", hrEmployeeService.selectUserByPositionName("总经理"));//总经理
        variables.put("financialAccount", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务核算
        variables.put("processDescription",dto.getTitle());

        if (model.getBusinessKey().indexOf("Red") != -1) {//红河项目
            int approval=0;
            int leader=0;
            if(model.getExtraApprove().contains("是")&&selectEmployeeService.getDeptChargeMen(model.getDeptId()).contains(dto.getCreator())){//中层领导
                approval=1;
            }
            if(hrEmployeeService.selectUserByPositionName("公司领导").contains(dto.getCreator())){//公司领导
                leader=1;
            }
            variables.put("leader", leader);//判断是否公司领导
            variables.put("approval", approval);//判断是否特批
            variables.put("businessManager", dto.getBussineManager());//项目主管副总
        } else if (model.getBusinessKey().indexOf("Build") != -1) {//建研院
            int positiveDept=0;
            int leader=0;
            int approval=0;
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(model.getCreator())){//中层正职
                positiveDept=1;
            }
            if(hrEmployeeService.selectUserByPositionName("公司领导").contains(dto.getCreator())){//公司领导
                leader=1;
            }
            if(model.getExtraApprove().contains("是")&&selectEmployeeService.getDeptChargeMen(model.getDeptId()).contains(dto.getCreator())){//中层领导
                approval=1;
            }
            variables.put("positiveDept", positiveDept);

            variables.put("leader", leader);//判断是否公司领导
            variables.put("approval", approval);//判断是否特批

        } else if (model.getBusinessKey().indexOf("Function") != -1) {//职能部门
            HrEmployeeSysDto modelByUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int record=0;
            int positiveDept=0;
            int leader=0;
            int approval=0;

            if(modelByUserLogin.getDeptCode().equals("34")||modelByUserLogin.getDeptCode().equals("21")){//档案图书室行政部
                record=1;
            }
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(dto.getCreator())){//中层正职
                positiveDept=1;
            }
            if(hrEmployeeService.selectUserByPositionName("公司领导").contains(dto.getCreator())){
                leader=1;
            }
            if(model.getExtraApprove().contains("是")&&selectEmployeeService.getDeptChargeMen(model.getDeptId()).contains(dto.getCreator())){//中层领导
                approval=1;
            }

            variables.put("record", record);
            variables.put("deptAuditor", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门审核
            variables.put("positiveDept", positiveDept);
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门审核
            variables.put("approval", approval);//判断是否特批
/*            variables.put("scientific", model.getScientific().contains("是")?true:false);//科研项目
            variables.put("train", model.getTravelExpense().contains("是")?true:false);//培训费*/
            variables.put("leader", leader);//判断是否公司领导

        } else if(model.getBusinessKey().indexOf("Common") != -1){//生产部门
            HrEmployeeSysDto commonUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int middleLeader=0;
            int dept=0;
            int positiveDept=0;
            int leader=0;
            int approval=0;
            int approvalTwo=0;
            if(selectEmployeeService.getCompanyLeaders().contains(model.getCreator())){//中层以上
                middleLeader=1;
            }
            if(commonUserLogin.getDeptCode().equals("20")){
                dept=1;
            }
            if(commonUserLogin.getDeptCode().equals("53")){
                dept=2;
            }
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(model.getCreator())){//中层正职
                positiveDept=1;
            }
            if(model.getExtraApprove().contains("是")&&selectEmployeeService.getDeptChargeMen(model.getDeptId()).contains(dto.getCreator())){//中层领导
                approval=1;
            }
            if(hrEmployeeService.selectUserByPositionName("公司领导").contains(dto.getCreator())){
                leader=1;
            }
            if(approval>0&&selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(dto.getCreator())){
                approvalTwo=1;
            }

            variables.put("dept", dept);
            variables.put("middleLeader", middleLeader);//中层以上
            variables.put("positiveDept", positiveDept);//中层正职
            variables.put("approval", approval);//特批
            variables.put("project", dto.getProjectName().length()>0?true:false);
            variables.put("overproof", dto.getExceedStandard().contains("需要")?true:false);//超标审批
            variables.put("deptDean", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//院长
            variables.put("positiveDeptDean", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//正院长
/*            variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目
            variables.put("train", dto.getTravelExpense().contains("是")?true:false);//培训费*/
            variables.put("leader", leader);//判断是否公司领导
            variables.put("approvalTwo", approvalTwo);//二次特批
        }

        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveFinanceTravelExpenseDto getModelById(int id){

        return getDto(fiveFinanceTravelExpenseMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceTravelExpenseDto getDto(FiveFinanceTravelExpense item) {
        FiveFinanceTravelExpenseDto dto=FiveFinanceTravelExpenseDto.adapt(item);
        if(dto.getProjectId()!=0){
            dto.setIsProject(true);
        }

        CustomSimpleProcessInstance customSimpleProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
        dto.setProcessName(customSimpleProcessInstance.getCurrentTaskName());

        if (!item.getProcessEnd() && customSimpleProcessInstance.isFinished()) {
            dto.setProcessEnd(true);
            fiveFinanceTravelExpenseMapper.updateByPrimaryKey(dto);
        }
        if (StringUtils.isEmpty(customSimpleProcessInstance.getCurrentTaskName())) {
            dto.setProcessName("流程已结束");
        }
        //统计数据
        //报销金额
        String totalApplyMoney="0";
        //在途补助
        String totalOnRoadSubsidy="0";
        //金额小计
        String totalCount="0";
        List<FiveFinanceTravelExpenseDetail> detailList=listDetail(item.getId());
        for (FiveFinanceTravelExpenseDetail detail:detailList){
            totalApplyMoney=MyStringUtil.getNewAddMoney(totalApplyMoney,detail.getApplyMoney());
            totalOnRoadSubsidy=MyStringUtil.getNewAddMoney(totalOnRoadSubsidy,detail.getOnRoadSubsidy());
            totalCount=MyStringUtil.getNewAddMoney(totalCount,detail.getCount());
        }
        dto.setTotalApplyMoney(totalApplyMoney);//总报销金额
        dto.setTotalOnRoadSubsidy(totalOnRoadSubsidy);//总在途补助
        dto.setTotalCount(totalCount);//总金额小计


        //2021-01-07HNZ
        //计算抵扣金额
        List<FiveFinanceTravelDeduction> details = listDeduction(item.getId());
        String totalLoan ="0";
        String totalRefund ="0";
        String totalDeduction ="0";
        String loanRemain ="0";
        for(FiveFinanceTravelDeduction detail:details){
            if(detail.getRelevanceType().equals("loan")){
                totalLoan = MyStringUtil.getNewAddMoney(totalLoan,detail.getRelevanceMoney());
                loanRemain = MyStringUtil.getNewAddMoney(loanRemain,detail.getRelevanceRemainMoney());
            }
            if(detail.getRelevanceType().equals("refund")){
                totalRefund = MyStringUtil.getNewAddMoney(totalRefund,detail.getRelevanceMoney());
            }
        }
        dto.setLoanRemainMoney(loanRemain);
        String deductionMoney = MyStringUtil.getNewSubMoney(totalLoan, totalRefund);
        dto.setDeductionMoney(deductionMoney);


        dto.setTotalApplyMoney(MyStringUtil.moneyToString(dto.getTotalApplyMoney(),6));
        dto.setTotalOnRoadSubsidy(MyStringUtil.moneyToString(dto.getTotalOnRoadSubsidy(),6));
        dto.setTotalCount(MyStringUtil.moneyToString(dto.getCountTotal(),6));
        return dto;
    }
    public FiveFinanceTravelExpenseDetail getDetailDto(FiveFinanceTravelExpenseDetail item) {
        item.setApplyMoney(MyStringUtil.moneyToString(item.getApplyMoney(),6));
        item.setOnRoadSubsidy(MyStringUtil.moneyToString(item.getOnRoadSubsidy(),6));
        item.setCount(MyStringUtil.moneyToString(item.getCount(),6));
        if(Double.valueOf(item.getApplyMoney()).equals(0.0)){
            item.setApplyMoney("");
        }
        if(Double.valueOf(item.getOnRoadSubsidy()).equals(0.0)){
            item.setOnRoadSubsidy("");
        }
        if(Double.valueOf(item.getCount()).equals(0.0)){
            item.setCount("");
        }
        return item;
    }

    public int getNewModel(String userLogin,String uiSref){
        FiveFinanceTravelExpense item=new FiveFinanceTravelExpense();
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
        item.setApplicant(hrEmployeeDto.getUserLogin());
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"财务项目类别").toString());
        item.setTravelExpense("否");
        item.setScientific("否");
        item.setExceedStandard("不需要");
        item.setExtraApprove("否");
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setRefundAmount(MyStringUtil.moneyToString("0"));//退款金额
        item.setChargeAgainst(MyStringUtil.moneyToString("0"));//冲抵及还款金额
        item.setTotalCount(MyStringUtil.moneyToString("0"));//统计
        item.setStartTime(MyDateUtil.getStringToday());
        item.setEndTime(MyDateUtil.getStringToday());
        item.setApplyRefundTime(MyDateUtil.getStringToday());
        ModelUtil.setNotNullFields(item);
        fiveFinanceTravelExpenseMapper.insert(item);
        if(Double.valueOf(item.getRefundAmount()).equals(0.0)){
            item.setRefundAmount("");
        }
        if(Double.valueOf(item.getChargeAgainst()).equals(0.0)){
            item.setChargeAgainst("");
        }
        if(Double.valueOf(item.getTotalCount()).equals(0.0)){
            item.setTotalCount("");
        }

        String title = "差旅费报销:" + item.getDeptName();

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription",title);

        if (uiSref.indexOf("Red") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_RED + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_RED, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Build") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_BUILD + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_BUILD, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Function") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_FUNCTION + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_FUNCTION, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else {
            String businessKey = EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_COMMON + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_COMMON, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        }

        item.setTitle(title);
        fiveFinanceTravelExpenseMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        if (uiSref.indexOf("Red") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_RED);
        } else if (uiSref.indexOf("Build") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_BUILD);
        } else if (uiSref.indexOf("Function") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_FUNCTION);
        } else {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_COMMON);
        }
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceTravelExpenseMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceTravelExpense item=(FiveFinanceTravelExpense)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void updateDetail(FiveFinanceTravelExpenseDetail item){
        //如果申请金额 大于 预算剩余金额 提示
        Assert.state(Double.valueOf(item.getApplyMoney())<=Double.valueOf(item.getBudgetBalance()),"申请金额 大于 预算剩余金额!");
        FiveFinanceTravelExpenseDetail model = fiveFinanceTravelExpenseDetailMapper.selectByPrimaryKey(item.getId());
        model.setApplyRefundProject(item.getApplyRefundProject());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setTravelExpenseDays(item.getTravelExpenseDays());
        model.setOnRoadTime(item.getOnRoadTime());
        model.setApplyStandard(item.getApplyStandard());
        model.setApplyMoney(MyStringUtil.moneyToString(item.getApplyMoney()));
        model.setOnRoadSubsidy(MyStringUtil.moneyToString(item.getOnRoadSubsidy()));
        model.setCount(MyStringUtil.getNewAddMoney(model.getApplyMoney(),model.getOnRoadSubsidy(),8));//金额小计
        model.setFinancialConfirmation(item.getFinancialConfirmation());
        model.setAccountSubject(item.getAccountSubject());
        model.setApplicantName(item.getApplicantName());
        model.setApplicant(item.getApplicant());
        model.setRemark(item.getRemark());
        model.setSiteAllowance(item.getSiteAllowance());
        model.setTravelAllowance(item.getTravelAllowance());
        model.setDinnerAllowance(item.getDinnerAllowance());
        model.setAccommodationAllowance(item.getAccommodationAllowance());

        model.setBudgetId(item.getBudgetId());
        model.setBudgetBalance(item.getBudgetBalance());
        model.setBudgetNo(item.getBudgetNo());
        model.setBudgetType(item.getBudgetType());
        fiveFinanceTravelExpenseDetailMapper.updateByPrimaryKey(model);
        
    }

    public FiveFinanceTravelExpenseDetail getNewModelDetail(int  id,String userLogin){
        FiveFinanceTravelExpense fiveFinanceTravelExpense = fiveFinanceTravelExpenseMapper.selectByPrimaryKey(id);
        FiveFinanceTravelExpenseDetail item=new FiveFinanceTravelExpenseDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setTravelExpenseId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setAccommodationAllowance(false);
        item.setDinnerAllowance(false);
        item.setTravelAllowance(false);
        item.setSiteAllowance(false);
        item.setApplyMoney(MyStringUtil.moneyToString("0"));//报销金额
        item.setOnRoadSubsidy(MyStringUtil.moneyToString("0"));//在途补助
        item.setCount(MyStringUtil.moneyToString("0"));//金额小计

        //获取申请人的报销标准
        String applyStandard = "0";
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(fiveFinanceTravelExpense.getApplicant());
        if(hrEmployeeSysDto.getPositionNames().contains("首席")||hrEmployeeSysDto.getPositionNames().contains("科技带头")
                ||hrEmployeeSysDto.getDeptId().equals(16)){
            if(fiveFinanceTravelExpense.getTravelPlaceType().equals("北上广深")) {
                applyStandard = "1000";
            }else{
                applyStandard = "600";
            }
        }else{
            if(fiveFinanceTravelExpense.getTravelPlaceType().equals("特殊城市")) {
                applyStandard = "400";
            }else{
                applyStandard = "300";
            }
        }
        item.setApplyStandard(applyStandard);
        ModelUtil.setNotNullFields(item);
        fiveFinanceTravelExpenseDetailMapper.insert(item);
        if(Double.valueOf(item.getApplyMoney()).equals(0.0)){
            item.setApplyMoney("");
        }
        if(Double.valueOf(item.getOnRoadSubsidy()).equals(0.0)){
            item.setOnRoadSubsidy("");
        }
        if(Double.valueOf(item.getCount()).equals(0.0)){
            item.setCount("");
        }
        return item;
    }

    public FiveFinanceTravelExpenseDetail getModelDetailById(int id){
        FiveFinanceTravelExpenseDetail detail = fiveFinanceTravelExpenseDetailMapper.selectByPrimaryKey(id);
        return getDetailDto(detail);
    }

    public List<FiveFinanceTravelExpenseDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("travelExpenseId",id);//小写
        List<FiveFinanceTravelExpenseDetail> oList = fiveFinanceTravelExpenseDetailMapper.selectAll(params);
        List<FiveFinanceTravelExpenseDetail> list = new ArrayList<>();
        for(FiveFinanceTravelExpenseDetail detail:oList){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public List<FiveFinanceTravelDeduction> listDeduction(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("travelId",id);//小写
        List<FiveFinanceTravelDeduction> list = fiveFinanceTravelDeductionMapper.selectAll(params);
        for(FiveFinanceTravelDeduction deduction:list){
            deduction.setRelevanceMoney(MyStringUtil.moneyToString(deduction.getRelevanceMoney(),6));
        }
        return list;
    }

    public void removeDetail(int id){
        FiveFinanceTravelExpenseDetail model = fiveFinanceTravelExpenseDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceTravelExpenseDetailMapper.updateByPrimaryKey(model);
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceTravelExpense item = fiveFinanceTravelExpenseMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);
        List<FiveFinanceTravelExpenseDetail> materialPurchaseDetails = fiveFinanceTravelExpenseDetailMapper.selectAll (map);
        data.put("materialPurchaseDetails",materialPurchaseDetails);
/*        Double sum=0.0d;
        for (FiveFinanceTravelExpenseDetail detail:materialPurchaseDetails) {
            sum+=Double.valueOf(detail.getBookNumber());
        }
        data.put("sum",sum);*/

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

    public void saveSelectedDeduction(int travelId,int id, String type) {
        FiveFinanceTravelExpenseDto travelDto = getModelById(travelId);
        if(type.equals("loan")){
            FiveFinanceLoanDto loan = fiveFinanceLoanService.getModelById(id);
            //判断 跟新还是增加
            Map map =new HashMap();
            map.put("deleted",false);
            map.put("travelId",travelId);
            map.put("relevanceId",id);
            map.put("relevanceType","loan");
            List<FiveFinanceTravelDeduction> fiveFinanceTravelDeductions = fiveFinanceTravelDeductionMapper.selectAll(map);
            if(fiveFinanceTravelDeductions.size()==0){
                FiveFinanceTravelDeduction deduction = new FiveFinanceTravelDeduction();
                deduction.setTravelId(travelId);
                deduction.setRelevanceId(loan.getId());
                deduction.setRelevanseBusinessKey(loan.getBusinessKey());
                deduction.setRelevanceName(loan.getTitle()+":"+loan.getCreatorName());
                deduction.setRelevanceType("loan");
                deduction.setRelevanceMoney(loan.getLoanMoney());
                deduction.setRelevanceTime(loan.getApplicantTime());
                deduction.setRelevanceRemainMoney(loan.getRemainMoney());
                deduction.setRelevanceRemark(loan.getLoanReason());
                deduction.setDeptId(loan.getDeptId());
                deduction.setDeptName(loan.getDeptName());
                deduction.setCreator(travelDto.getCreator());
                deduction.setCreatorName(travelDto.getCreatorName());
                deduction.setGmtModified(new Date());
                deduction.setGmtCreate(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceTravelDeductionMapper.insert(deduction);
                //关联id
                loan.setTravelId(travelId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }else{
                FiveFinanceTravelDeduction deduction = fiveFinanceTravelDeductions.get(0);
                deduction.setTravelId(travelId);
                deduction.setRelevanceId(loan.getId());
                deduction.setRelevanseBusinessKey(loan.getBusinessKey());
                deduction.setRelevanceName(loan.getTitle()+":"+loan.getCreatorName());
                deduction.setRelevanceType("loan");
                deduction.setRelevanceMoney(loan.getLoanMoney());
                deduction.setRelevanceTime(loan.getApplicantTime());
                deduction.setRelevanceRemainMoney(loan.getRemainMoney());
                deduction.setRelevanceRemark(loan.getLoanReason());
                deduction.setDeptId(loan.getDeptId());
                deduction.setDeptName(loan.getDeptName());
                deduction.setCreator(travelDto.getCreator());
                deduction.setCreatorName(travelDto.getCreatorName());
                deduction.setGmtModified(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceTravelDeductionMapper.updateByPrimaryKey(deduction);
                //关联id
                loan.setTravelId(travelId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }
        }else if(type.equals("refund")){
            FiveFinanceRefund refund = fiveFinanceRefundMapper.selectByPrimaryKey(id);
            //判断 跟新还是增加
            Map map =new HashMap();
            map.put("deleted",false);
            map.put("travelId",travelId);
            map.put("relevanceId",id);
            map.put("relevanceType","refund");
            List<FiveFinanceTravelDeduction> fiveFinanceTravelDeductions = fiveFinanceTravelDeductionMapper.selectAll(map);
            if(fiveFinanceTravelDeductions.size()==0){
                FiveFinanceTravelDeduction deduction = new FiveFinanceTravelDeduction();
                deduction.setTravelId(travelId);
                deduction.setRelevanceId(refund.getId());
                deduction.setRelevanseBusinessKey(refund.getBusinessKey());
                deduction.setRelevanceName(refund.getTitle()+":"+refund.getCreatorName());
                deduction.setRelevanceType("refund");
                deduction.setRelevanceMoney(refund.getTotalRefundMoney());
                deduction.setRelevanceTime(refund.getApplicantTime());
                deduction.setRelevanceRemark(refund.getRemark());
                deduction.setDeptId(refund.getDeptId());
                deduction.setDeptName(refund.getDeptName());

                deduction.setCreator(travelDto.getCreator());
                deduction.setCreatorName(travelDto.getCreatorName());
                deduction.setGmtModified(new Date());
                deduction.setGmtCreate(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceTravelDeductionMapper.insert(deduction);
                //关联id
                refund.setTravelId(travelId);
                fiveFinanceRefundMapper.updateByPrimaryKey(refund);
            }else{
                FiveFinanceTravelDeduction deduction = fiveFinanceTravelDeductions.get(0);
                deduction.setTravelId(travelId);
                deduction.setRelevanceId(refund.getId());
                deduction.setRelevanseBusinessKey(refund.getBusinessKey());
                deduction.setRelevanceName(refund.getTitle()+":"+refund.getCreatorName());
                deduction.setRelevanceType("refund");
                deduction.setRelevanceMoney(refund.getTotalRefundMoney());
                deduction.setRelevanceTime(refund.getApplicantTime());
                deduction.setRelevanceRemark(refund.getRemark());
                deduction.setDeptId(refund.getDeptId());
                deduction.setDeptName(refund.getDeptName());
                deduction.setCreator(travelDto.getCreator());
                deduction.setCreatorName(travelDto.getCreatorName());
                deduction.setGmtModified(new Date());
                ModelUtil.setNotNullFields(deduction);
                fiveFinanceTravelDeductionMapper.updateByPrimaryKey(deduction);
                //关联id
                refund.setTravelId(travelId);
                fiveFinanceRefundMapper.updateByPrimaryKey(refund);
            }
        }
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
            FiveFinanceTravelExpense item = fiveFinanceTravelExpenseMapper.selectByPrimaryKey(id);
            FiveFinanceTravelExpenseDto travelExpenseDto =getModelById(id);
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
            List<FiveFinanceTravelExpense> fiveFinanceTravelExpenses = fiveFinanceTravelExpenseMapper.selectAll(params);
            int size=0;
            //判断顺序代码最大值
            if (!fiveFinanceTravelExpenses.isEmpty()){
                for (FiveFinanceTravelExpense fiveFinanceTravelExpense:fiveFinanceTravelExpenses){
                    String receiptsNumber = fiveFinanceTravelExpense.getReceiptsNumber();
                    if(fiveFinanceTravelExpense.getId()!=id&&StringUtils.isNotEmpty(receiptsNumber)){
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
            newReceiptsNumber=newReceiptsNumber+deptCode+date+"03"+format;
            travelExpenseDto.setReceiptsNumber(newReceiptsNumber);
            update(travelExpenseDto);
            return newReceiptsNumber;

        }catch (Exception e){
            Assert.state(false,"请准确填写，预计开始时间、xxx、xxx！");
            return "";
        }
    }
}
