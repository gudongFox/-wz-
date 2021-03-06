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
import com.cmcu.mcc.business.service.FiveBusinessContractLibraryService;
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
    @Autowired
    FiveBusinessContractLibraryService fiveBusinessContractLibraryService;

    public void remove(int id,String userLogin){
        FiveFinanceReimburse item = fiveFinanceReimburseMapper.selectByPrimaryKey(id);

        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
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
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
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
        // ?????????????????????????????????
        if (selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())) {
            dept=1;
        }
        List<String> attributeList=new ArrayList<>();
        for(FiveFinanceReimburseDetail modelDetail:modelDetailList){
            if(modelDetail.getBudgetNo().contains("????????????")){
                attributeList.add(selectEmployeeService.getDeptChargeMen(48).get(0));//???????????????
                attribute = 1;
            } else if(modelDetail.getBudgetNo().contains("????????????")){
                attributeList.add(selectEmployeeService.getDeptChargeMen(48).get(0));//???????????????
                attribute = 1;
            } else if(modelDetail.getBudgetNo().contains("??????")){
                attributeList.add(selectEmployeeService.getDeptChargeMen(11).get(0));//?????????
                attribute = 1;
            } else if(modelDetail.getBudgetNo().contains("?????????")){
                attributeList.add(selectEmployeeService.getDeptChargeMen(101).get(0));//??????
                attribute = 1;
            } else if(modelDetail.getBudgetNo().contains("???????????????")){
                attributeList.add(selectEmployeeService.getUserListByRoleName("??????-???????????????").get(0));//??????
                attribute = 1;
            } else if((modelDetail.getBudgetNo().contains("???????????????????????????")||modelDetail.getBudgetNo().contains("????????????")
                    ||modelDetail.getBudgetNo().contains("????????????")||modelDetail.getBudgetNo().contains("????????????"))
                    &&MyStringUtil.compareMoney(modelDetail.getApplyMoney(),"5000")>-1){
                attributeList.add(selectEmployeeService.getDeptChargeMen(67).get(0));//??????
                attribute = 1;
            }else if(modelDetail.getBudgetNo().contains("??????")) {
                attributeList.add(selectEmployeeService.getDeptChargeMen(38).get(0));//??????
                train=1;
            }
            if(modelDetail.getBudgetNo().contains("?????????")){
                phone=1;
            }
        }
        Map variables = Maps.newHashMap();
        variables.put("balance", dto.getGreaterThan());//????????????????????????????????? true
        variables.put("phone", phone);
        variables.put("attributeList", attributeList);//???????????????
        variables.put("train", train);
        //totalApplyMoney ????????????????????????????????????????????????
        variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=10000.00?true:false);
        variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=30000.00?true:false);
        variables.put("flag2", Double.valueOf(dto.getTotalApplyMoney())>=50000.00?true:false);
        variables.put("attribute", attribute);
        variables.put("dept", dept);//??????????????????????????????
        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//????????????
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//????????????
        variables.put("deptLeader",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//????????????
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//???????????????
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(18));//??????????????????
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("????????????"));//????????????
        variables.put("generalManager", hrEmployeeService.selectUserByPositionName("?????????"));//?????????
        variables.put("financialAccount", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//????????????
        //variables.put("scientific", dto.getScientific().contains("???")?true:false);//????????????
        if (model.getBusinessKey().indexOf("Red") != -1) {//????????????
            variables.put("scientific", dto.getScientific().contains("???")?true:false);//????????????

            variables.put("businessManager", dto.getBusinessManager());//????????????????????????

        }
        else if (model.getBusinessKey().indexOf("Build") != -1) {//?????????
            //????????????
            int middleLeader=0;
            if(selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()).contains(dto.getCreator())){
                middleLeader=1;
            }

            variables.put("middleLeader", middleLeader);
            variables.put("scientific", dto.getScientific().contains("???")?true:false);//????????????
            variables.put("vicePresident", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//????????????

        }
        else if (model.getBusinessKey().indexOf("Function") != -1) {//????????????
            HrEmployeeSysDto modelByUserLogin = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
            int record=0;
            if(modelByUserLogin.getDeptCode().equals("58")||modelByUserLogin.getDeptCode().equals("67")){
                record=1;
            }
            //totalApplyMoney ????????????????????????????????????????????????
            variables.put("flag", Double.valueOf(dto.getTotalApplyMoney())>=5000.00?true:false);
            variables.put("flag1", Double.valueOf(dto.getTotalApplyMoney())>=30000.00?true:false);
            variables.put("flag2", Double.valueOf(dto.getTotalApplyMoney())>=50000.00?true:false);
            variables.put("record", record);
            variables.put("scientific", dto.getScientific().contains("???")?true:false);//????????????

        }
        else if(model.getBusinessKey().indexOf("Common") != -1){//????????????
            int project=0;
            if(model.getProjectName().length()==0){
                if(selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())){
                    project=2;
                }else{
                    project=1;
                }
            }
            variables.put("project", project);
            variables.put("projectManager", model.getBusinessManager());//??????????????????
            variables.put("scientific", dto.getScientific().contains("???")?true:false);//????????????
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
        //????????????
        if(item.getProjectId()!=0){
            dto.setProjectNo(fiveBusinessContractLibraryService.getModelById(item.getProjectId()).getProjectNo());
        }

        CustomSimpleProcessInstance customSimpleProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
        dto.setProcessName(customSimpleProcessInstance.getCurrentTaskName());

        if (!item.getProcessEnd() && customSimpleProcessInstance.isFinished()) {
            dto.setProcessEnd(true);
            fiveFinanceReimburseMapper.updateByPrimaryKey(dto);
        }
        if (StringUtils.isEmpty(customSimpleProcessInstance.getCurrentTaskName())) {
            dto.setProcessName("???????????????");
        }

        //?????????????????????
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
        //??????????????????
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
        //?????? ?????????  ???????????????????????????
        dto.setLoanRemainMoney(loanRemain);

        String shouldRefundMoney = MyStringUtil.getNewSubMoney(deductionMoney,dto.getTotalApplyMoney());
        dto.setShouldRefundMoney(shouldRefundMoney);
        if(Double.valueOf(shouldRefundMoney)>=0.0){
            //???????????????
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

        //?????? ????????? ???
        item.setApplyMoney(MyStringUtil.getMoneyY(item.getApplyMoney()));
        item.setConfirmMoney(MyStringUtil.getMoneyY(item.getConfirmMoney()));
        return item;
    }

    public int getNewModel(String userLogin,String uiSref){
        FiveFinanceReimburse item=new FiveFinanceReimburse();
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
        item.setScientific("???");
        item.setGreaterThan(false);
        item.setExtraApprove(false);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setCount(0.0);
        item.setApplicantTime(MyDateUtil.getStringToday());
        item.setScientific("???");

        ModelUtil.setNotNullFields(item);
        fiveFinanceReimburseMapper.insert(item);
        String title = "????????????:" + item.getDeptName();

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
        //?????????????????????
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
        //?????????????????? ?????? ?????????????????? ??????
        Assert.state(Double.valueOf(MyStringUtil.getMoneyW(item.getApplyMoney()))<=Double.valueOf(item.getBudgetBalance()),"???????????? ?????? ??????????????????!");
        if (item.getId()==null||item.getId()==0){
            fiveFinanceReimburseDetailMapper.insert(item);
        }
        FiveFinanceReimburseDetail model = fiveFinanceReimburseDetailMapper.selectByPrimaryKey(item.getId());
        model.setProjectType(item.getProjectType());
        model.setCostProject(item.getCostProject());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setProject(item.getProject());

        //??? ????????? ??????
        model.setApplyMoney(MyStringUtil.getMoneyW(item.getApplyMoney()));
        model.setConfirmMoney(MyStringUtil.getMoneyW(item.getConfirmMoney()));
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
            item.setDeptName("???????????????");
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
        params.put("reimburseId",id);//??????
        List<FiveFinanceReimburseDetail> olist = fiveFinanceReimburseDetailMapper.selectAll(params);
        for(FiveFinanceReimburseDetail detail:olist){
            list.add(getDetailDto(detail));
        }
        return list;
    }
    public List<FiveFinanceReimburseDeduction> listDeduction(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("reimburseId",id);//??????
        List<FiveFinanceReimburseDeduction> list = fiveFinanceReimburseDeductionMapper.selectAll(params);
        for(FiveFinanceReimburseDeduction deduction:list){
            //?????? ???
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
            //?????? ??????????????????
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
                //????????????
                deduction.setRelevanceMoney(MyStringUtil.getMoneyW(loan.getTotalApplyMoney()));
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
                //??????id
                loan.setReimburseId(reimburseId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }else{
                FiveFinanceReimburseDeduction deduction = fiveFinanceReimburseDeductions.get(0);
                deduction.setReimburseId(reimburseId);
                deduction.setRelevanceId(loan.getId());
                deduction.setRelevanceBusinessKey(loan.getBusinessKey());
                deduction.setRelevanceName(loan.getTitle()+":"+loan.getCreatorName());
                deduction.setRelevanceType("loan");
                //????????????
                deduction.setRelevanceMoney(MyStringUtil.getMoneyW(loan.getTotalApplyMoney()));
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
                //??????id
                loan.setReimburseId(reimburseId);
                fiveFinanceLoanMapper.updateByPrimaryKey(loan);
            }
        }else if(type.equals("refund")){
            FiveFinanceRefund refund = fiveFinanceRefundMapper.selectByPrimaryKey(id);
            //?????? ??????????????????
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
                //??????id
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
                //??????id
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
        String title = "????????????:" + item.getDeptName();

        String businessKey= EdConst.FIVE_FINANCE_REFUND+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", reimburseDto.getCreator());
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription",title);
        //??????????????????
        variables.put("replenishRefund",true);

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_REFUND,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setTitle(title);

        //????????????
        item.setReimburseId(reimburseId);
        item.setShouldRefundMoney(reimburseDto.getShouldRefundMoney());

        fiveFinanceRefundMapper.updateByPrimaryKey(item);
        //????????????
        saveSelectedDeduction(reimburseId,item.getId(),"refund");

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
            FiveFinanceReimburse item = fiveFinanceReimburseMapper.selectByPrimaryKey(id);
            FiveFinanceReimburseDto reimburseDto =getModelById(id);
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
            List<FiveFinanceReimburse> fiveFinanceReimburses = fiveFinanceReimburseMapper.selectAll(params);
            int size=0;
            //???????????????????????????
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

            String format=String.format("%03d", size);//format??????????????????10????????????
            //??????+??????+??????+??????
            newReceiptsNumber=newReceiptsNumber+deptCode+date+"-02-"+format;
            reimburseDto.setReceiptsNumber(newReceiptsNumber);
            update(reimburseDto);
            return newReceiptsNumber;

        }catch (Exception e){
            Assert.state(false,"???????????????????????????????????????xxx???xxx???");
            return "";
        }
    }
}
