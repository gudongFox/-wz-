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
import com.cmcu.mcc.budget.dto.FiveBudgetIndependentDetailDto;
import com.cmcu.mcc.budget.service.FiveBudgetIndependentService;
import com.cmcu.mcc.business.service.FiveBusinessContractLibraryService;
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
import com.google.common.base.Strings;
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
    @Autowired
    FiveFinanceTravelExpenseUserMapper fiveFinanceTravelExpenseUserMapper;
    @Autowired
    FiveBudgetIndependentService fiveBudgetIndependentService;
    @Autowired
    FiveBusinessContractLibraryService fiveBusinessContractLibraryService;

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
            model.setProjectDeputy(dto.getProjectDeputy());
            model.setProjectDeputyName(dto.getProjectDeputyName());
        }else{
            model.setProjectId(0);
            model.setProjectName("");
            model.setBussineManager("");
            model.setBussineManagerName("");
            model.setProjectDeputy("");
            model.setProjectDeputyName("");
            model.setScientific("否");
            model.setProjectType("");
        }

        model.setApplicant(dto.getApplicant());
        model.setApplicantName(dto.getApplicantName());
        model.setPayName(dto.getPayName());
        model.setPayBank(dto.getPayBank());
        model.setPayAccount(dto.getPayAccount());
        model.setReceiveName(dto.getReceiveName());
        model.setReceiveBank(dto.getReceiveBank());
        model.setReceiveAccount(dto.getReceiveAccount());
        model.setExtraApproveReason(dto.getExtraApproveReason());

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
        //totalApplyMoney 字段未存数据库，需判断的单位为元
        variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=10000.00?true:false);
        variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=30000.00?true:false);
        variables.put("flag2", Double.valueOf(dto.getTotalApplyMoney())>=50000.00?true:false);
        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务确认
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门领导
        variables.put("deptLeader",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//分管领导
        variables.put("projectManager",model.getBussineManager());//项目总师
        variables.put("projectDeputy",model.getProjectDeputy());//项目主管院长


        variables.put("scientific", dto.getScientific().contains("是")?true:false);//科研项目
        variables.put("train", dto.getTravelExpense().contains("是")?true:false);//培训费
        variables.put("humanDeptChargeMan", selectEmployeeService.getDeptChargeMen(38));//人力负责人
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//财务负责人
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(dto.getDeptId()));//发起部门 分管领导
        variables.put("deputy", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//副院长
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
        }
        else if (model.getBusinessKey().indexOf("Build") != -1) {//建研院
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

        }
        else if (model.getBusinessKey().indexOf("Function") != -1) {//职能部门
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
            if(model.getExtraApprove().contains("是")||selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),1,false).contains(dto.getCreator())){//中层领导
                approval=1;
            }

            variables.put("record", record);
            variables.put("deptAuditor", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门审核
            variables.put("positiveDept", positiveDept);
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门审核
            variables.put("approvalTwo", approval);//判断是否特批
            variables.put("leader", leader);//判断是否公司领导

        }
        else if(model.getBusinessKey().indexOf("Common") != -1){//生产部门
            HrEmployeeSysDto commonUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int middleLeader=0;
            int dept=0;
            int positiveDept=0;
            int leader=0;
            int approval=0;
            int project = 1;
            if(selectEmployeeService.getCompanyLeaders().contains(model.getCreator())){//中层以上
                middleLeader=1;
            }
            if(commonUserLogin.getDeptCode().equals("20")){
                dept=1;
            }
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(model.getCreator())){//中层正职
                positiveDept=1;
            }
            if(model.getExtraApprove().contains("是")){
                approval=1;//特批
            }
            if(model.getExtraApprove().contains("是")&&selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),2,false).contains(dto.getCreator())){//中层领导
                approval=2;//特批且是副职
            }
            if(hrEmployeeService.selectUserByPositionName("公司领导").contains(dto.getCreator())){
                leader=1;
            }

            if (dto.getProjectName().trim().length()==0) {
                project = 0;
                if (selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())){
                    project = 2;
                }
            }

            variables.put("dept", dept);
            variables.put("middleLeader", middleLeader);//中层以上
            variables.put("positiveDept", positiveDept);//中层正职
            variables.put("approval", approval); //特批 且是副职 1
            variables.put("project", project);
           /* variables.put("overproof", dto.getExceedStandard().contains("需要")?true:false);//超标审批*/
             variables.put("overproof","");//超标审批
            //variables.put("dean53",selectEmployeeService.getParentDeptChargeMen(53,2,false));//石化院副职
            variables.put("dean20",selectEmployeeService.getOtherDeptChargeMan(20));//钢结构
            variables.put("deptDean", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//院长
            variables.put("positiveDeptDean", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//正院长

            variables.put("leader", leader);//判断是否公司领导

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
        //合同信息
        if(item.getProjectId()!=0){
            dto.setProjectNo(fiveBusinessContractLibraryService.getModelById(item.getProjectId()).getProjectNo());
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
        //补助金额
        String totalSubsidyMoney="0";

        //金额合计
        String totalCount="0";
        List<FiveFinanceTravelExpenseDetail> detailList=listDetail(item.getId());
        for (FiveFinanceTravelExpenseDetail detail:detailList){
            if(detail.getExpenseType().equals("报销")){
                totalApplyMoney=MyStringUtil.getNewAddMoney(totalApplyMoney,detail.getApplyMoney());
            } else if(detail.getExpenseType().equals("补助")){
                totalSubsidyMoney=MyStringUtil.getNewAddMoney(totalSubsidyMoney,detail.getApplyMoney());
            }
            totalCount=MyStringUtil.getNewAddMoney(totalCount,detail.getApplyMoney());
        }
        dto.setTotalApplyMoney(totalApplyMoney);//报销合计
        dto.setTotalSubsidyMoney(totalSubsidyMoney);//补助合计
        dto.setTotalCount(totalCount);//总金额小计


        String loanRemainMoney = "0";
        List<FiveFinanceTravelDeduction> details = listDeduction(item.getId());
        for(FiveFinanceTravelDeduction detail:details){
            if(detail.getRelevanceType().equals("loan")){
                loanRemainMoney =detail.getRelevanceRemainMoney();
            }
        }
        dto.setLoanRemainMoney(loanRemainMoney);


        //实际报销金额 报销金额 + 补助金额 - 借款金额剩余金额
        dto.setRealMoney(MyStringUtil.getNewSubMoney(dto.getTotalCount(),loanRemainMoney,2));
        dto.setTotalApplyMoney(MyStringUtil.moneyToString(dto.getTotalApplyMoney(),2));
        dto.setTotalSubsidyMoney(MyStringUtil.moneyToString(dto.getTotalSubsidyMoney(),2));

        return dto;
    }
    public FiveFinanceTravelExpenseDetail getDetailDto(FiveFinanceTravelExpenseDetail item) {
        //万元 转换为 元
        item.setApplyMoney(MyStringUtil.getMoneyY(item.getApplyMoney()));
        if(Double.valueOf(item.getApplyMoney()).equals(0.0)){
            item.setApplyMoney("");
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
        item.setDeptId(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        item.setDeptName(selectEmployeeService.getHeadDeptName(hrEmployeeDto.getDeptId()));

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
        //自动生成单据号
        String receiptsNumber = getReceiptsNumber(item.getId());
        item.setReceiptsNumber(receiptsNumber);
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

        //元转为 万元
        item.setApplyMoney(MyStringUtil.getMoneyW(item.getApplyMoney()));
        //如果申请金额 大于 预算剩余金额 提示
        Assert.state(MyStringUtil.compareMoney(item.getBudgetBalance(),item.getApplyMoney())!=-1,"申请金额 大于 预算剩余金额!");
        if (item.getId()==null||item.getId()==0){
            fiveFinanceTravelExpenseDetailMapper.insert(item);
        }
        FiveFinanceTravelExpenseDetail model = fiveFinanceTravelExpenseDetailMapper.selectByPrimaryKey(item.getId());
        model.setApplyRefundProject(item.getApplyRefundProject());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setTravelExpenseDays(item.getTravelExpenseDays());
        model.setTravelDuringDate(item.getTravelDuringDate());
        model.setOnRoadTime(item.getOnRoadTime());
        model.setApplyStandard(item.getApplyStandard());

        //元 转换为 万元
        model.setApplyMoney(item.getApplyMoney());
        model.setFinancialConfirmation(item.getFinancialConfirmation());

        model.setAccountSubject(item.getAccountSubject());
        model.setApplicantName(item.getApplicantName());
        model.setApplicant(item.getApplicant());
        model.setRemark(item.getRemark());

        model.setExpenseType(item.getExpenseType());
        model.setRealType(item.getRealType());
        model.setTravelPlace(item.getTravelPlace());
        model.setTravelPlaceType(item.getTravelPlaceType());
        model.setApplyStandard(item.getApplyStandard());

        model.setBudgetId(item.getBudgetId());
        model.setBudgetBalance(item.getBudgetBalance());
        model.setBudgetNo(item.getBudgetNo());
        model.setBudgetType(item.getBudgetType());
        fiveFinanceTravelExpenseDetailMapper.updateByPrimaryKey(model);
        
    }

    public void updateUserDetail(FiveFinanceTravelExpenseUser item){

        //如果填写金额 总额大于 报销金额 提示
        FiveFinanceTravelExpenseDto fiveFinanceTravelExpenseDto = getModelById(item.getTravelExpenseId());

        String remainMoney = MyStringUtil.compareMoney(fiveFinanceTravelExpenseDto.getRealMoney(),"0")>=0?fiveFinanceTravelExpenseDto.getRealMoney():"0";
        List<FiveFinanceTravelExpenseUser> users = listUserDetail(item.getTravelExpenseId());
        for(FiveFinanceTravelExpenseUser user:users){
            //排除本id之前的数据
            if(user.getId()!=item.getId()){
                remainMoney=MyStringUtil.getNewSubMoney(remainMoney,user.getMoney());
            }
        }
        //加上这次的
        remainMoney=MyStringUtil.getNewSubMoney(remainMoney,item.getMoney());
        Assert.state(MyStringUtil.compareMoney(remainMoney,"0")!=-1,"分配金额 大于 总金额!");
        // 元 转为 万元
        item.setMoney(MyStringUtil.getMoneyW(item.getMoney()));
        if (item.getId()==null){
            ModelUtil.setNotNullFields(item);
            fiveFinanceTravelExpenseUserMapper.insert(item);
        }else{
            fiveFinanceTravelExpenseUserMapper.updateByPrimaryKey(item);
        }

    }



    public FiveFinanceTravelExpenseDetail getNewModelDetail(int  id,String userLogin){
        FiveFinanceTravelExpense fiveFinanceTravelExpense = fiveFinanceTravelExpenseMapper.selectByPrimaryKey(id);
        FiveFinanceTravelExpenseDetail item=new FiveFinanceTravelExpenseDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setApplicant(userLogin);
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
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

        item.setExpenseType("报销");
        if(!Strings.isNullOrEmpty(fiveFinanceTravelExpense.getStartTime())&&
                !Strings.isNullOrEmpty(fiveFinanceTravelExpense.getEndTime())){
            //计算报销天数
            long days = MyDateUtil.getDays(fiveFinanceTravelExpense.getEndTime(),fiveFinanceTravelExpense.getStartTime());
            item.setTravelExpenseDays(days+"");
        }



        //默认扣减预算为 申请部门的 差旅费
        FiveBudgetIndependentDetailDto independentDetailDto = fiveBudgetIndependentService.getDetailByTypeName(fiveFinanceTravelExpense.getDeptId(), EdConst.FIVE_BUDGET_INDEPENDENT
                , fiveFinanceTravelExpense.getApplyRefundTime().substring(0, 4), "差旅费");
        item.setBudgetBalance(independentDetailDto.getRemainMoney());
        item.setBudgetNo(independentDetailDto.getTypeName());
        item.setBudgetId(independentDetailDto.getId());


        ModelUtil.setNotNullFields(item);
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

    //获取申请人的报销-补助标准
    public String getApplyStandard(FiveFinanceTravelExpenseDetail detail){
        String applyStandard = "0";

        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(detail.getApplicant());
        if(detail.getRealType().equals("住宿费报销")){
            Assert.state(!Strings.isNullOrEmpty(detail.getTravelPlaceType()),"请先填写出差地点信息！");
            if(hrEmployeeSysDto.getPositionNames().contains("首席")||hrEmployeeSysDto.getPositionNames().contains("科技带头")
                    ||hrEmployeeSysDto.getDeptId().equals(16)){
                if(detail.getTravelPlaceType().equals("北上广深")) {
                    applyStandard = "1000";
                }else{
                    applyStandard = "600";
                }
            }else{
                if(detail.getTravelPlaceType().equals("特殊城市")||detail.getTravelPlaceType().equals("北上广深")) {
                    applyStandard = "400";
                }else{
                    applyStandard = "300";
                }
            }
        }else if(detail.getRealType().equals("住宿费补助")){
            applyStandard ="50.00";
        }else if(detail.getRealType().equals("出差补助")){
            applyStandard ="150.00";
        }else if(detail.getRealType().equals("夜间伙补")){
            applyStandard ="100.00";
        }else if(detail.getRealType().equals("工地补贴")){
            applyStandard ="50.00";
        }
        detail.setApplyStandard(applyStandard);


        return applyStandard;
    }

    public FiveFinanceTravelExpenseUser getNewModelUserDetail(int  id,String userLogin){
        FiveFinanceTravelExpenseDto fiveFinanceTravelExpenseDto = getModelById(id);

        FiveFinanceTravelExpenseUser item=new FiveFinanceTravelExpenseUser();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setUser(userLogin);
        item.setUserName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setTravelExpenseId(id);
        
        //获取当前剩余金额
        String remainMoney = MyStringUtil.compareMoney(fiveFinanceTravelExpenseDto.getRealMoney(),"0")>=0?fiveFinanceTravelExpenseDto.getRealMoney():"0";
        List<FiveFinanceTravelExpenseUser> users = listUserDetail(id);
        for(FiveFinanceTravelExpenseUser user:users){
            remainMoney=MyStringUtil.getNewSubMoney(remainMoney,user.getMoney());
        }
        item.setMoney(MyStringUtil.moneyToString(remainMoney,2));
        

        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);


        return item;
    }



    public FiveFinanceTravelExpenseDetail getModelDetailById(int id){
        FiveFinanceTravelExpenseDetail detail = fiveFinanceTravelExpenseDetailMapper.selectByPrimaryKey(id);
        return getDetailDto(detail);
    }
    public FiveFinanceTravelExpenseUser getModelUserDetailById(int id){
        FiveFinanceTravelExpenseUser user = fiveFinanceTravelExpenseUserMapper.selectByPrimaryKey(id);
        //金额 显示元
        user.setMoney(MyStringUtil.getMoneyY(user.getMoney()));
        return user;
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
    public List<FiveFinanceTravelExpenseUser> listUserDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("travelExpenseId",id);//小写
        List<FiveFinanceTravelExpenseUser> oList = fiveFinanceTravelExpenseUserMapper.selectAll(params);
        for(FiveFinanceTravelExpenseUser user:oList){
            //显示为 元
            user.setMoney(MyStringUtil.getMoneyY(user.getMoney()));
        }
        return oList;
    }


    public List<FiveFinanceTravelDeduction> listDeduction(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("travelId",id);//小写
        List<FiveFinanceTravelDeduction> list = fiveFinanceTravelDeductionMapper.selectAll(params);
        for(FiveFinanceTravelDeduction deduction:list){
            //转为 元
            deduction.setRelevanceMoney(MyStringUtil.getMoneyY(deduction.getRelevanceMoney()));
            deduction.setRelevanceRemainMoney(MyStringUtil.getMoneyY(deduction.getRelevanceRemainMoney()));
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
    public void removeUserDetail(int id){
        FiveFinanceTravelExpenseUser model = fiveFinanceTravelExpenseUserMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceTravelExpenseUserMapper.updateByPrimaryKey(model);
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
                //万元存储
                deduction.setRelevanceMoney(MyStringUtil.getMoneyW(loan.getTotalApplyMoney()));
                deduction.setRelevanceRemainMoney(MyStringUtil.getMoneyW(loan.getRemainMoney()));
                deduction.setRelevanceTime(loan.getApplicantTime());

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
                //万元存储
                deduction.setRelevanceMoney(MyStringUtil.getMoneyW(loan.getTotalApplyMoney()));
                deduction.setRelevanceRemainMoney(MyStringUtil.getMoneyW(loan.getRemainMoney()));

                deduction.setRelevanceTime(loan.getApplicantTime());
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
            newReceiptsNumber=newReceiptsNumber+deptCode+date+"-03-"+format;
            travelExpenseDto.setReceiptsNumber(newReceiptsNumber);
            update(travelExpenseDto);
            return newReceiptsNumber;

        }catch (Exception e){
            Assert.state(false,"请准确填写，预计开始时间、xxx、xxx！");
            return "";
        }
    }
}
