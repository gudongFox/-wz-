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
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
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
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
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
            model.setScientific("???");
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
        model.setExtraApprove(dto.getExtraApprove());//??????
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());

        fiveFinanceTravelExpenseMapper.updateByPrimaryKey(model);

        selectEmployeeService.getCompanyLeaders();

        Map variables = Maps.newHashMap();
        //totalApplyMoney ????????????????????????????????????????????????
        variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=10000.00?true:false);
        variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=30000.00?true:false);
        variables.put("flag2", Double.valueOf(dto.getTotalApplyMoney())>=50000.00?true:false);
        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//????????????
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//????????????
        variables.put("deptLeader",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//????????????
        variables.put("projectManager",model.getBussineManager());//????????????
        variables.put("projectDeputy",model.getProjectDeputy());//??????????????????


        variables.put("scientific", dto.getScientific().contains("???")?true:false);//????????????
        variables.put("train", dto.getTravelExpense().contains("???")?true:false);//?????????
        variables.put("humanDeptChargeMan", selectEmployeeService.getDeptChargeMen(38));//???????????????
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//???????????????
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(dto.getDeptId()));//???????????? ????????????
        variables.put("deputy", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//?????????
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("????????????"));//????????????
        variables.put("generalManager", hrEmployeeService.selectUserByPositionName("?????????"));//?????????
        variables.put("financialAccount", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//????????????
        variables.put("processDescription",dto.getTitle());

        if (model.getBusinessKey().indexOf("Red") != -1) {//????????????
            int approval=0;
            int leader=0;
            if(model.getExtraApprove().contains("???")&&selectEmployeeService.getDeptChargeMen(model.getDeptId()).contains(dto.getCreator())){//????????????
                approval=1;
            }
            if(hrEmployeeService.selectUserByPositionName("????????????").contains(dto.getCreator())){//????????????
                leader=1;
            }
            variables.put("leader", leader);//????????????????????????
            variables.put("approval", approval);//??????????????????
            variables.put("businessManager", dto.getBussineManager());//??????????????????
        }
        else if (model.getBusinessKey().indexOf("Build") != -1) {//?????????
            int positiveDept=0;
            int leader=0;
            int approval=0;
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(model.getCreator())){//????????????
                positiveDept=1;
            }
            if(hrEmployeeService.selectUserByPositionName("????????????").contains(dto.getCreator())){//????????????
                leader=1;
            }
            if(model.getExtraApprove().contains("???")&&selectEmployeeService.getDeptChargeMen(model.getDeptId()).contains(dto.getCreator())){//????????????
                approval=1;
            }
            variables.put("positiveDept", positiveDept);

            variables.put("leader", leader);//????????????????????????
            variables.put("approval", approval);//??????????????????

        }
        else if (model.getBusinessKey().indexOf("Function") != -1) {//????????????
            HrEmployeeSysDto modelByUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int record=0;
            int positiveDept=0;
            int leader=0;
            int approval=0;

            if(modelByUserLogin.getDeptCode().equals("34")||modelByUserLogin.getDeptCode().equals("21")){//????????????????????????
                record=1;
            }
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(dto.getCreator())){//????????????
                positiveDept=1;
            }
            if(hrEmployeeService.selectUserByPositionName("????????????").contains(dto.getCreator())){
                leader=1;
            }
            if(model.getExtraApprove().contains("???")||selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),1,false).contains(dto.getCreator())){//????????????
                approval=1;
            }

            variables.put("record", record);
            variables.put("deptAuditor", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//????????????
            variables.put("positiveDept", positiveDept);
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//????????????
            variables.put("approvalTwo", approval);//??????????????????
            variables.put("leader", leader);//????????????????????????

        }
        else if(model.getBusinessKey().indexOf("Common") != -1){//????????????
            HrEmployeeSysDto commonUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int middleLeader=0;
            int dept=0;
            int positiveDept=0;
            int leader=0;
            int approval=0;
            int project = 1;
            if(selectEmployeeService.getCompanyLeaders().contains(model.getCreator())){//????????????
                middleLeader=1;
            }
            if(commonUserLogin.getDeptCode().equals("20")){
                dept=1;
            }
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(model.getCreator())){//????????????
                positiveDept=1;
            }
            if(model.getExtraApprove().contains("???")){
                approval=1;//??????
            }
            if(model.getExtraApprove().contains("???")&&selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),2,false).contains(dto.getCreator())){//????????????
                approval=2;//??????????????????
            }
            if(hrEmployeeService.selectUserByPositionName("????????????").contains(dto.getCreator())){
                leader=1;
            }

            if (dto.getProjectName().trim().length()==0) {
                project = 0;
                if (selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())){
                    project = 2;
                }
            }

            variables.put("dept", dept);
            variables.put("middleLeader", middleLeader);//????????????
            variables.put("positiveDept", positiveDept);//????????????
            variables.put("approval", approval); //?????? ???????????? 1
            variables.put("project", project);
           /* variables.put("overproof", dto.getExceedStandard().contains("??????")?true:false);//????????????*/
             variables.put("overproof","");//????????????
            //variables.put("dean53",selectEmployeeService.getParentDeptChargeMen(53,2,false));//???????????????
            variables.put("dean20",selectEmployeeService.getOtherDeptChargeMan(20));//?????????
            variables.put("deptDean", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//??????
            variables.put("positiveDeptDean", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//?????????

            variables.put("leader", leader);//????????????????????????

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
        //????????????
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
            dto.setProcessName("???????????????");
        }
        //????????????
        //????????????
        String totalApplyMoney="0";
        //????????????
        String totalSubsidyMoney="0";

        //????????????
        String totalCount="0";
        List<FiveFinanceTravelExpenseDetail> detailList=listDetail(item.getId());
        for (FiveFinanceTravelExpenseDetail detail:detailList){
            if(detail.getExpenseType().equals("??????")){
                totalApplyMoney=MyStringUtil.getNewAddMoney(totalApplyMoney,detail.getApplyMoney());
            } else if(detail.getExpenseType().equals("??????")){
                totalSubsidyMoney=MyStringUtil.getNewAddMoney(totalSubsidyMoney,detail.getApplyMoney());
            }
            totalCount=MyStringUtil.getNewAddMoney(totalCount,detail.getApplyMoney());
        }
        dto.setTotalApplyMoney(totalApplyMoney);//????????????
        dto.setTotalSubsidyMoney(totalSubsidyMoney);//????????????
        dto.setTotalCount(totalCount);//???????????????


        String loanRemainMoney = "0";
        List<FiveFinanceTravelDeduction> details = listDeduction(item.getId());
        for(FiveFinanceTravelDeduction detail:details){
            if(detail.getRelevanceType().equals("loan")){
                loanRemainMoney =detail.getRelevanceRemainMoney();
            }
        }
        dto.setLoanRemainMoney(loanRemainMoney);


        //?????????????????? ???????????? + ???????????? - ????????????????????????
        dto.setRealMoney(MyStringUtil.getNewSubMoney(dto.getTotalCount(),loanRemainMoney,2));
        dto.setTotalApplyMoney(MyStringUtil.moneyToString(dto.getTotalApplyMoney(),2));
        dto.setTotalSubsidyMoney(MyStringUtil.moneyToString(dto.getTotalSubsidyMoney(),2));

        return dto;
    }
    public FiveFinanceTravelExpenseDetail getDetailDto(FiveFinanceTravelExpenseDetail item) {
        //?????? ????????? ???
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
            item.setDeptName("???????????????");
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
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setTravelExpense("???");
        item.setScientific("???");
        item.setExceedStandard("?????????");
        item.setExtraApprove("???");
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setRefundAmount(MyStringUtil.moneyToString("0"));//????????????
        item.setChargeAgainst(MyStringUtil.moneyToString("0"));//?????????????????????
        item.setTotalCount(MyStringUtil.moneyToString("0"));//??????
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

        String title = "???????????????:" + item.getDeptName();

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
        //?????????????????????
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

        //????????? ??????
        item.setApplyMoney(MyStringUtil.getMoneyW(item.getApplyMoney()));
        //?????????????????? ?????? ?????????????????? ??????
        Assert.state(MyStringUtil.compareMoney(item.getBudgetBalance(),item.getApplyMoney())!=-1,"???????????? ?????? ??????????????????!");
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

        //??? ????????? ??????
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

        //?????????????????? ???????????? ???????????? ??????
        FiveFinanceTravelExpenseDto fiveFinanceTravelExpenseDto = getModelById(item.getTravelExpenseId());

        String remainMoney = MyStringUtil.compareMoney(fiveFinanceTravelExpenseDto.getRealMoney(),"0")>=0?fiveFinanceTravelExpenseDto.getRealMoney():"0";
        List<FiveFinanceTravelExpenseUser> users = listUserDetail(item.getTravelExpenseId());
        for(FiveFinanceTravelExpenseUser user:users){
            //?????????id???????????????
            if(user.getId()!=item.getId()){
                remainMoney=MyStringUtil.getNewSubMoney(remainMoney,user.getMoney());
            }
        }
        //???????????????
        remainMoney=MyStringUtil.getNewSubMoney(remainMoney,item.getMoney());
        Assert.state(MyStringUtil.compareMoney(remainMoney,"0")!=-1,"???????????? ?????? ?????????!");
        // ??? ?????? ??????
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
        item.setApplyMoney(MyStringUtil.moneyToString("0"));//????????????
        item.setOnRoadSubsidy(MyStringUtil.moneyToString("0"));//????????????
        item.setCount(MyStringUtil.moneyToString("0"));//????????????

        item.setExpenseType("??????");
        if(!Strings.isNullOrEmpty(fiveFinanceTravelExpense.getStartTime())&&
                !Strings.isNullOrEmpty(fiveFinanceTravelExpense.getEndTime())){
            //??????????????????
            long days = MyDateUtil.getDays(fiveFinanceTravelExpense.getEndTime(),fiveFinanceTravelExpense.getStartTime());
            item.setTravelExpenseDays(days+"");
        }



        //????????????????????? ??????????????? ?????????
        FiveBudgetIndependentDetailDto independentDetailDto = fiveBudgetIndependentService.getDetailByTypeName(fiveFinanceTravelExpense.getDeptId(), EdConst.FIVE_BUDGET_INDEPENDENT
                , fiveFinanceTravelExpense.getApplyRefundTime().substring(0, 4), "?????????");
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

    //????????????????????????-????????????
    public String getApplyStandard(FiveFinanceTravelExpenseDetail detail){
        String applyStandard = "0";

        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(detail.getApplicant());
        if(detail.getRealType().equals("???????????????")){
            Assert.state(!Strings.isNullOrEmpty(detail.getTravelPlaceType()),"?????????????????????????????????");
            if(hrEmployeeSysDto.getPositionNames().contains("??????")||hrEmployeeSysDto.getPositionNames().contains("????????????")
                    ||hrEmployeeSysDto.getDeptId().equals(16)){
                if(detail.getTravelPlaceType().equals("????????????")) {
                    applyStandard = "1000";
                }else{
                    applyStandard = "600";
                }
            }else{
                if(detail.getTravelPlaceType().equals("????????????")||detail.getTravelPlaceType().equals("????????????")) {
                    applyStandard = "400";
                }else{
                    applyStandard = "300";
                }
            }
        }else if(detail.getRealType().equals("???????????????")){
            applyStandard ="50.00";
        }else if(detail.getRealType().equals("????????????")){
            applyStandard ="150.00";
        }else if(detail.getRealType().equals("????????????")){
            applyStandard ="100.00";
        }else if(detail.getRealType().equals("????????????")){
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
        
        //????????????????????????
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
        //?????? ?????????
        user.setMoney(MyStringUtil.getMoneyY(user.getMoney()));
        return user;
    }
    

    public List<FiveFinanceTravelExpenseDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("travelExpenseId",id);//??????
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
        params.put("travelExpenseId",id);//??????
        List<FiveFinanceTravelExpenseUser> oList = fiveFinanceTravelExpenseUserMapper.selectAll(params);
        for(FiveFinanceTravelExpenseUser user:oList){
            //????????? ???
            user.setMoney(MyStringUtil.getMoneyY(user.getMoney()));
        }
        return oList;
    }


    public List<FiveFinanceTravelDeduction> listDeduction(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("travelId",id);//??????
        List<FiveFinanceTravelDeduction> list = fiveFinanceTravelDeductionMapper.selectAll(params);
        for(FiveFinanceTravelDeduction deduction:list){
            //?????? ???
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
            //?????? ??????????????????
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
                //????????????
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
                //??????id
                loan.setTravelId(travelId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }else{
                FiveFinanceTravelDeduction deduction = fiveFinanceTravelDeductions.get(0);
                deduction.setTravelId(travelId);
                deduction.setRelevanceId(loan.getId());
                deduction.setRelevanseBusinessKey(loan.getBusinessKey());
                deduction.setRelevanceName(loan.getTitle()+":"+loan.getCreatorName());
                deduction.setRelevanceType("loan");
                //????????????
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
                //??????id
                loan.setTravelId(travelId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }
        }else if(type.equals("refund")){
            FiveFinanceRefund refund = fiveFinanceRefundMapper.selectByPrimaryKey(id);
            //?????? ??????????????????
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
                //??????id
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
                //??????id
                refund.setTravelId(travelId);
                fiveFinanceRefundMapper.updateByPrimaryKey(refund);
            }
        }
    }
    /**
     ?????? 01
     ???????????? 02
     ??????????????? 03
     ????????????????????????04

     ????????????Z
     ????????????S
     ????????????H
     ???????????????J

     2021 03 12 01??????????????????001???????????????????????????Z 20210312 01001
     */
    public String getReceiptsNumber(int id){
        try{
            FiveFinanceTravelExpense item = fiveFinanceTravelExpenseMapper.selectByPrimaryKey(id);
            FiveFinanceTravelExpenseDto travelExpenseDto =getModelById(id);
            String newReceiptsNumber ="";

            //????????????
            String deptCode="";


            if(item.getBusinessKey().contains("Red")){
                deptCode="H";//????????????
            }else {
                if (hrDeptService.getModelById(item.getDeptId()).getDeptType().equals("??????")){
                    deptCode="S";//????????????
                }else if(hrDeptService.getModelById(item.getDeptId()).getDeptType().equals("??????")){
                    deptCode="Z";//????????????
                }else if(item.getBusinessKey().contains("Build")){
                    deptCode = "J";//???????????????
                }
            }
            //????????????
            String date=MyDateUtil.dateToStr1(item.getGmtCreate());

            //?????? 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            // params.put("projectNoHead",year+deptCode+typeCode);
            List<FiveFinanceTravelExpense> fiveFinanceTravelExpenses = fiveFinanceTravelExpenseMapper.selectAll(params);
            int size=0;
            //???????????????????????????
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

            String format=String.format("%03d", size);//format??????????????????10????????????
            //??????+??????+??????+??????
            newReceiptsNumber=newReceiptsNumber+deptCode+date+"-03-"+format;
            travelExpenseDto.setReceiptsNumber(newReceiptsNumber);
            update(travelExpenseDto);
            return newReceiptsNumber;

        }catch (Exception e){
            Assert.state(false,"???????????????????????????????????????xxx???xxx???");
            return "";
        }
    }
}
