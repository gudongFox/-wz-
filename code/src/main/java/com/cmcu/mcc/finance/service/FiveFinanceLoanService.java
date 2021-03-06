package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
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
import com.cmcu.mcc.finance.dto.FiveFinanceLoanDetailDto;
import com.cmcu.mcc.finance.dto.FiveFinanceLoanDto;

import com.cmcu.mcc.finance.entity.FiveFinanceLoan;
import com.cmcu.mcc.finance.entity.FiveFinanceLoanDetail;

import com.cmcu.mcc.finance.dto.FiveFinanceReimburseDto;
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
import java.util.stream.Collectors;

@Service
public class FiveFinanceLoanService extends BaseService {
    @Resource
    FiveFinanceLoanMapper fiveFinanceLoanMapper;
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
    FiveFinanceLoanDetailMapper fiveFinanceLoanDetailMapper;

    @Resource
    FiveFinanceReimburseDeductionMapper fiveFinanceReimburseDeductionMapper;
    @Resource
    FiveFinanceTravelDeductionMapper fiveFinanceTravelDeductionMapper;
    @Resource
    FiveFinanceReimburseService fiveFinanceReimburseService;
    @Resource
    FiveFinanceTravelExpenseService fiveFinanceTravelExpenseService;
    @Resource
    FiveFinanceTransferAccountsMapper fiveFinanceTransferAccountsMapper;


    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveFinanceLoan item = fiveFinanceLoanMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
        List<FiveFinanceLoanDetail> details = listDetail(id);
        for(FiveFinanceLoanDetail detail:details){
            detail.setDeleted(true);
            fiveFinanceLoanDetailMapper.updateByPrimaryKey(detail);
        }
    }

    public void update(FiveFinanceLoanDto dto){
        FiveFinanceLoan model = getModelById(dto.getId());
        model.setItem(dto.getItem());
        model.setProjectType(dto.getProjectType());
        model.setUnit(dto.getUnit());
        model.setReceiptsNumber(dto.getReceiptsNumber());

        if(dto.getIsProject()){
            model.setProjectId(dto.getProjectId());
            model.setProjectName(dto.getProjectName());
            model.setBusinessManager(dto.getBusinessManager());
            model.setBusinessManagerName(dto.getBusinessManagerName());
            model.setProjectChargeMan(dto.getProjectChargeMan());
            model.setProjectChargeManName(dto.getProjectChargeManName());
        }else{
            model.setProjectId(0);
            model.setProjectName("");
            model.setBusinessManager("");
            model.setBusinessManagerName("");
            model.setProjectChargeMan("");
            model.setProjectChargeManName("");
        }

        model.setApplicantTime(dto.getApplicantTime());
        //model.setLoanMoney(dto.getLoanMoney());
        model.setLoanReason(dto.getLoanReason());
        model.setPayName(dto.getPayName());
        model.setPayAccount(dto.getPayAccount());
        model.setPayBank(dto.getPayBank());
        model.setReceiveName(dto.getReceiveName());
        model.setReceiveId(dto.getReceiveId());
        model.setReceiveDept(dto.getReceiveDept());
        model.setReceiveAccount(dto.getReceiveAccount());
        model.setReceiveBank(dto.getReceiveBank());
        model.setApplicant(dto.getApplicant());
        model.setApplicantName(dto.getApplicantName());
        model.setTitle(dto.getTitle());
        model.setBid(dto.getBid());
        model.setScientific(dto.getScientific());

        model.setTitle(dto.getTitle());
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());

        List<FiveFinanceLoanDetail> detailList=listDetail(model.getId());
        String loanMoney = MyStringUtil.moneyToString("0");
        for (FiveFinanceLoanDetail detail:detailList){
            loanMoney= MyStringUtil.getNewAddMoney(loanMoney,detail.getApplyMoney());
        }
        model.setLoanMoney(MyStringUtil.getMoneyW(loanMoney));

        fiveFinanceLoanMapper.updateByPrimaryKey(model);

        List<FiveFinanceLoanDetail> modelDetailList=listDetail(dto.getId());
        //???????????????
        int human=0;
        int attribute=0;
        List<String> attributeList=new ArrayList<>();
        for(FiveFinanceLoanDetail modelDetail:modelDetailList){
            if(modelDetail.getBudgetNo().contains("??????")) {
                attribute = 1;
                attributeList.add(selectEmployeeService.getDeptChargeMen(38).get(0));//??????
                if(modelDetail.getBudgetNo().contains("????????????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(48).get(0));//???????????????
                } else if(modelDetail.getBudgetNo().contains("????????????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(48).get(0));//???????????????
                } else if(modelDetail.getBudgetNo().contains("??????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(11).get(0));//?????????
                } else if(modelDetail.getBudgetNo().contains("?????????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(101).get(0));//??????
                } else if(modelDetail.getBudgetNo().contains("???????????????")){
                    attributeList.add(selectEmployeeService.getUserListByRoleName("??????-???????????????").get(0));//??????
                } else if((modelDetail.getBudgetNo().contains("???????????????????????????")||modelDetail.getBudgetNo().contains("????????????")
                ||modelDetail.getBudgetNo().contains("????????????")||modelDetail.getBudgetNo().contains("????????????"))
                        &&MyStringUtil.compareMoney(modelDetail.getApplyMoney(),"5000")>-1){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(67).get(0));//??????
                }
            }
            if(!modelDetail.getBudgetNo().contains("??????")) {
                if(modelDetail.getBudgetNo().contains("????????????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(48).get(0));//???????????????
                    attribute = 2;
                } else if(modelDetail.getBudgetNo().contains("????????????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(48).get(0));//???????????????
                    attribute = 2;
                } else if(modelDetail.getBudgetNo().contains("??????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(11).get(0));//?????????
                    attribute = 2;
                } else if(modelDetail.getBudgetNo().contains("?????????")){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(101).get(0));//??????
                    attribute = 2;
                } else if(modelDetail.getBudgetNo().contains("???????????????")){
                    attributeList.add(selectEmployeeService.getUserListByRoleName("??????-???????????????").get(0));//??????
                    attribute = 2;
                } else if((modelDetail.getBudgetNo().contains("???????????????")||modelDetail.getBudgetNo().contains("????????????")
                        ||modelDetail.getBudgetNo().contains("????????????")||modelDetail.getBudgetNo().contains("????????????"))
                        &&MyStringUtil.compareMoney(modelDetail.getApplyMoney(),"5000")>-1){
                    attributeList.add(selectEmployeeService.getDeptChargeMen(67).get(0));//??????
                    attribute = 2;
                    
                }
            }
        }
        Map variables = Maps.newHashMap();
        variables.put("attributeList", attributeList);//???????????????
        //totalApplyMoney ????????????????????????????????????????????????
        variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=10000.00?true:false);
        variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=30000.00?true:false);
        variables.put("flag2", Double.valueOf(dto.getTotalApplyMoney())>=50000.00?true:false);
        variables.put("attribute", attribute);
        variables.put("human", human);//????????????

        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//????????????
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//????????????
        variables.put("deptLeader",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//????????????
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//???????????????
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//????????????????????????
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("????????????"));//????????????
        variables.put("generalManager", hrEmployeeService.selectUserByPositionName("?????????"));//?????????
        variables.put("financialAccount", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//????????????

        if (model.getBusinessKey().indexOf("Red") != -1) {
            variables.put("businessManager", dto.getBusinessManager());
        }
        else if (model.getBusinessKey().indexOf("Build") != -1) {
            variables.put("scientific", dto.getScientific().contains("???")?true:false);
        }
        else if (model.getBusinessKey().indexOf("Function") != -1) {//????????????
            HrEmployeeSysDto modelByUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int record=0;
            int dept=0;
            if(modelByUserLogin.getDeptCode().equals("34")||modelByUserLogin.getDeptCode().equals("21")){
                record=1;
            }
            if (selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())) {
                dept=1;
            }
            //totalApplyMoney ????????????????????????????????????????????????
            variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=5000.00?true:false);
            variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=30000.00?true:false);

            variables.put("record", record);
            variables.put("dept", dept);
            variables.put("scientific", dto.getScientific().contains("???")?true:false);

        }
        else if(model.getBusinessKey().indexOf("Common") != -1){//????????????
            //?????????????????????
            int bid=0;
            List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(model.getDeptId());
            if(businessMen.size()!=0){
                variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(model.getDeptId()));//??????????????????
            }else{
                //??????????????????
                bid = 2;
            }
            if(dto.getBid().contains("???")){
                bid = 1;
                if (selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())) {
                    //?????????????????????
                    bid = 3;
                }
            }
            HrEmployeeSysDto modelByUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            String designViceManager="3996";
            String projectViceManager="3996";
            if(modelByUserLogin.getDeptCode().equals("02")){
                designViceManager="2399";
                projectViceManager="2399";
            }else if(modelByUserLogin.getDeptCode().equals("09")){
                designViceManager="2600";
            }else if(modelByUserLogin.getDeptCode().equals("89")){
                designViceManager="0166";
            }
            variables.put("project", dto.getProjectName().length()!=0?true:false);

            variables.put("deputy", dto.getProjectChargeMan());//??????????????????
            variables.put("bid", bid);
            variables.put("designViceManager", designViceManager);
            variables.put("projectViceManager", projectViceManager);
            variables.put("scientific", dto.getScientific().contains("???")?true:false);
            variables.put("projectType", dto.getProjectType().contains("??????")?true:false);
            variables.put("projectManager",dto.getBusinessManager());
            //selectEmployeeService.getFunctionDeptIds()


        }
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveFinanceLoanDto getModelById(int id){

        return getDto(fiveFinanceLoanMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceLoanDto getDto(FiveFinanceLoan item) {
        FiveFinanceLoanDto dto=FiveFinanceLoanDto.adapt(item);

        if(dto.getProjectId()!=0){
            dto.setIsProject(true);
        }

        CustomSimpleProcessInstance customSimpleProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
        dto.setProcessName(customSimpleProcessInstance.getCurrentTaskName());

        if (!item.getProcessEnd() && customSimpleProcessInstance.isFinished()) {
            dto.setProcessEnd(true);
            fiveFinanceLoanMapper.updateByPrimaryKey(dto);
        }
        if (StringUtils.isEmpty(customSimpleProcessInstance.getCurrentTaskName())) {
            dto.setProcessName("???????????????");
        }

        String budgetBalance="0";
        String applyMoney="0";
        List<FiveFinanceLoanDetail> detailList=listDetail(item.getId());
        for (FiveFinanceLoanDetail detail:detailList){
            budgetBalance= MyStringUtil.getNewAddMoney(budgetBalance,detail.getBudgetBalance());
            applyMoney= MyStringUtil.getNewAddMoney(applyMoney,detail.getApplyMoney());
        }
        dto.setTotalBudgetBalance(MyStringUtil.moneyToString(budgetBalance,6));
        dto.setTotalApplyMoney(MyStringUtil.moneyToString(applyMoney,2));

        dto.setRemainMoney(dto.getTotalApplyMoney());

        //?????????????????? ???????????? ??? ???????????? ?????? ???????????? ?????????????????????
        //?????????
        Map travelParams = Maps.newHashMap();
        travelParams.put("relevanceType","loan");
        travelParams.put("relevanceId",dto.getId());
        travelParams.put("deleted",0);
        List<FiveFinanceTravelDeduction> travelDeductions = fiveFinanceTravelDeductionMapper.selectAll(travelParams);
        for(FiveFinanceTravelDeduction travelDeduction:travelDeductions){
            FiveFinanceTravelExpenseDto travelDto = fiveFinanceTravelExpenseService.getModelById(travelDeduction.getTravelId());
            //???????????? ?????? ????????????????????????
            if(Double.valueOf(travelDto.getTotalApplyMoney())<Double.valueOf(travelDto.getLoanRemainMoney())){
                dto.setRemainMoney(MyStringUtil.getNewSubMoney(dto.getRemainMoney(),travelDto.getTotalApplyMoney()));
            }else{
                dto.setRemainMoney(MyStringUtil.moneyToString("0"));
            }
        }
        //??????
        Map reimburseParams = Maps.newHashMap();
        reimburseParams.put("relevanceType","loan");
        reimburseParams.put("relevanceId",dto.getId());
        reimburseParams.put("deleted",0);
        List<FiveFinanceReimburseDeduction> reimburseDeductions = fiveFinanceReimburseDeductionMapper.selectAll(reimburseParams);
        for(FiveFinanceReimburseDeduction reimburseDeduction:reimburseDeductions){
            FiveFinanceReimburseDto reimburseDto = fiveFinanceReimburseService.getModelById(reimburseDeduction.getReimburseId());
            //???????????? ?????? ????????????????????????
            if(Double.valueOf(reimburseDto.getTotalApplyMoney())<Double.valueOf(reimburseDto.getLoanRemainMoney())){
                dto.setRemainMoney(MyStringUtil.getNewSubMoney(dto.getRemainMoney(),reimburseDto.getTotalApplyMoney()));
            }else{
                dto.setRemainMoney(MyStringUtil.moneyToString("0"));
            }
        }
        //??????
        Map transferParams = Maps.newHashMap();
        transferParams.put("loanId",dto.getId());
        transferParams.put("deleted",0);
        List<FiveFinanceTransferAccounts> fiveFinanceTransferAccounts = fiveFinanceTransferAccountsMapper.selectAll(transferParams);
        for(FiveFinanceTransferAccounts transferAccounts:fiveFinanceTransferAccounts){
            dto.setRemainMoney(MyStringUtil.getNewSubMoney(dto.getRemainMoney(),transferAccounts.getTotalMoney()));
        }

        dto.setRemainMoney(MyStringUtil.moneyToString(dto.getRemainMoney(),2));

        return dto;
    }
    public FiveFinanceLoanDetail getDetailDto(FiveFinanceLoanDetail item) {
        item.setControlBalance(MyStringUtil.moneyToString(item.getControlBalance(),6));
//        item.setApplyMoney(MyStringUtil.moneyToString(item.getApplyMoney(),2));
        item.setBudgetBalance(MyStringUtil.moneyToString(item.getBudgetBalance(),6));

        //?????? ????????? ???
        item.setApplyMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyY(item.getApplyMoney()),2));
        return item;
    }

    public int getNewModel(String userLogin, String uiSref){
        FiveFinanceLoan item=new FiveFinanceLoan();
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
        item.setBid("???");
        item.setScientific("???");

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setApplicantTime(MyDateUtil.getStringToday());

        ModelUtil.setNotNullFields(item);
        fiveFinanceLoanMapper.insert(item);
        String title = "??????:" + item.getDeptName();

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", title);

        if (uiSref.indexOf("Red") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_LOAN_RED + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_LOAN_RED, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Build") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_LOAN_BUILD + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_LOAN_BUILD, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Function") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_LOAN_FUNCTION + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_LOAN_FUNCTION, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else {
            String businessKey = EdConst.FIVE_FINANCE_LOAN_COMMON + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_LOAN_COMMON, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        }

        item.setTitle(title);
        fiveFinanceLoanMapper.updateByPrimaryKey(item);
        //?????????????????????
        String receiptsNumber = getReceiptsNumber(item.getId());
        item.setReceiptsNumber(receiptsNumber);
        return item.getId();
    }
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        if (uiSref.indexOf("Red") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_LOAN_RED);
        } else if (uiSref.indexOf("Build") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_LOAN_BUILD);
        } else if (uiSref.indexOf("Function") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_LOAN_FUNCTION);
        } else {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_LOAN_COMMON);
        }

        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptListOa(userLogin, uiSref);
        if (deptIdList.size() == 0) {
            params.put("creator", userLogin);
        } else {
            params.put("deptIdList", deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceLoanMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceLoan item=(FiveFinanceLoan)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveFinanceLoanDetail item){
        //?????????????????? ?????? ?????????????????? ??????
        Assert.state(Double.valueOf(MyStringUtil.getMoneyW(item.getApplyMoney()))<=Double.valueOf(item.getBudgetBalance()),"???????????? ?????? ??????????????????!");
        if (item.getId()==null||item.getId()==0){
            //????????? ??????
            item.setApplyMoney(MyStringUtil.getMoneyW(item.getApplyMoney()));
            fiveFinanceLoanDetailMapper.insert(item);
        }else {
            FiveFinanceLoanDetail model = fiveFinanceLoanDetailMapper.selectByPrimaryKey(item.getId());
            model.setBudgetId(item.getBudgetId());
            model.setBudgetType(item.getBudgetType());
            model.setBudgetNo(item.getBudgetNo());
            model.setBudgetDegree(item.getBudgetDegree());
            model.setControlBalance(item.getControlBalance());
            model.setBudgetBalance(item.getBudgetBalance());

            //??? ????????? ??????
            model.setApplyMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyW(item.getApplyMoney())));
            model.setRemark(item.getRemark());
            fiveFinanceLoanDetailMapper.updateByPrimaryKey(model);
        }
    }

    public FiveFinanceLoanDetail getNewModelDetail(int id){
        FiveFinanceLoanDetail item=new FiveFinanceLoanDetail();
        item.setLoanId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setControlBalance(MyStringUtil.moneyToString("0"));
        item.setBudgetBalance(MyStringUtil.moneyToString("0"));
        item.setApplyMoney(MyStringUtil.moneyToString("0"));

        ModelUtil.setNotNullFields(item);
        //BeanValidator.check(item);
//        fiveFinanceLoanDetailMapper.insert(item);
        if(Double.valueOf(item.getControlBalance()).equals(0.0)){
            item.setControlBalance("");
        }
        if(Double.valueOf(item.getBudgetBalance()).equals(0.0)){
            item.setBudgetBalance("");
        }
        if(Double.valueOf(item.getApplyMoney()).equals(0.0)){
            item.setApplyMoney("");
        }
        //???????????????
        item.setApplyMoney(MyStringUtil.getMoneyY(item.getApplyMoney()));
        return item;

    }

    public FiveFinanceLoanDetail getModelDetailById(int id){
        FiveFinanceLoanDetail fiveFinanceLoanDetail = fiveFinanceLoanDetailMapper.selectByPrimaryKey(id);
        return getDetailDto(fiveFinanceLoanDetail);
    }

    public List<FiveFinanceLoanDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("loanId",id);//??????
        List<FiveFinanceLoanDetail> olist = fiveFinanceLoanDetailMapper.selectAll(params);
        List<FiveFinanceLoanDetail> list = Lists.newArrayList();
        olist.forEach(p -> {
            list.add(getDetailDto(p));
        });
        return list;
    }

    public void removeDetail(int id){
        FiveFinanceLoanDetail model = fiveFinanceLoanDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceLoanDetailMapper.updateByPrimaryKey(model);
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceLoan item = fiveFinanceLoanMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);
        List<FiveFinanceLoanDetail> materialPurchaseDetails = fiveFinanceLoanDetailMapper.selectAll (map);
        data.put("materialPurchaseDetails",materialPurchaseDetails);
        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

    //??????????????????
    public List<FiveFinanceLoan> listLoanByUserLogin(String userLogin) {
        HrEmployeeSysDto userDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("deptId", userDto.getDeptId());//??????????????????????????????
        List<FiveFinanceLoan> oList = fiveFinanceLoanMapper.selectAll(params);

        List<FiveFinanceLoan> list = new ArrayList<>();
        oList.forEach(p -> {
            FiveFinanceLoanDto dto = getDto(p);
            if(Double.valueOf(dto.getRemainMoney())>0){
                list.add(dto);
            }
        });
        return list;
    }
    public List<FiveFinanceLoan> listLoanByDeptId(String userLogin,int deptId) {
        HrEmployeeSysDto userDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("deptId",deptId);
        List<FiveFinanceLoan> fiveFinanceLoans = fiveFinanceLoanMapper.selectAll(params)
                .stream().filter(p->p.getReimburseId()==0).filter(p->p.getTravelId()==0).collect(Collectors.toList());
        return fiveFinanceLoans;
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
            FiveFinanceLoan item = fiveFinanceLoanMapper.selectByPrimaryKey(id);
            FiveFinanceLoanDto loanDto =getModelById(id);
            String newReceiptsNumber ="";


            //????????????
            String deptCode="";

            // if (EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_COMMON.contains(key)){
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
            List<FiveFinanceLoan> fiveFinanceLoans1 = fiveFinanceLoanMapper.selectAll(params);
            int size=0;
            //???????????????????????????
            if (!fiveFinanceLoans1.isEmpty()){
                for (FiveFinanceLoan fiveFinanceLoan:fiveFinanceLoans1){
                    String receiptsNumber = fiveFinanceLoan.getReceiptsNumber();
                    if(fiveFinanceLoan.getId()!=id&&StringUtils.isNotEmpty(receiptsNumber)){
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
            newReceiptsNumber=newReceiptsNumber+deptCode+date+"-01-"+format;
            loanDto.setReceiptsNumber(newReceiptsNumber);
            update(loanDto);
            return newReceiptsNumber;

        }catch (Exception e){
            Assert.state(false,"????????????????????????????????????????????????????????????");
            return "";
        }
    }

}
