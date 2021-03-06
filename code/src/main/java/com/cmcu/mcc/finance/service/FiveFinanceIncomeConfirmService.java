package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.NumberChangeUtils;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.BusinessIncomeMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.dto.BusinessIncomeDto;
import com.cmcu.mcc.business.entity.BusinessIncome;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.service.BusinessIncomeService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeConfirmDto;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeDto;
import com.cmcu.mcc.finance.dto.FiveFinanceInvoiceDto;
import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceIncomeConfirm;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
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
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveFinanceIncomeConfirmService extends BaseService {

    @Resource
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;

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
    FiveFinanceIncomeMapper fiveFinanceIncomeMapper;
    @Autowired
    FiveFinanceIncomeService fiveFinanceIncomeService;
    @Autowired
    FiveFinanceInvoiceService fiveFinanceInvoiceService;
    @Autowired
    FiveFinanceInvoiceMapper fiveFinanceInvoiceMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;


    public void remove(int id, String userLogin) {
        FiveFinanceIncomeConfirm item = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");
        //1.?????????????????????????????? ????????????
        List<Integer> incomeIds = MyStringUtil.getIntList(item.getIncomeIds());
        Assert.state(incomeIds.size()==0, "????????? ????????????????????????????????? ????????????!");

        // 2.?????????????????????????????? ????????????
        List<Integer> invoiceIds = MyStringUtil.getIntList(item.getInvoiceIds());
        Assert.state(invoiceIds.size()==0, "????????? ??????????????????????????? ????????????!");

        //????????????????????? ??????????????????
        if(item.getIncomeId()!=0){
            FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(item.getIncomeId());
            fiveFinanceIncome.setConfirmIds(MyStringUtil.getNewStringId(fiveFinanceIncome.getConfirmIds(),item.getId()));
            fiveFinanceIncome.setIncomeConfirmMoneyIng(MyStringUtil.getNewSubMoney(fiveFinanceIncome.getIncomeConfirmMoneyIng(),item.getMoney()));
            fiveFinanceIncomeMapper.updateByPrimaryKey(fiveFinanceIncome);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public int update(FiveFinanceIncomeConfirmDto dto) {
        FiveFinanceIncomeConfirm item;
        if(dto.getId()!=0){
            item= fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(dto.getId());
        }else{
            item = new FiveFinanceIncomeConfirm();
        }

        //??????????????????
       /* if(dto.getIncomeId()!=0){
            //????????????????????? ??????
            if(item.getIncomeId()!=0){
                FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(item.getIncomeId());
                fiveFinanceIncome.setConfirmIds(MyStringUtil.getNewStringId(fiveFinanceIncome.getConfirmIds(),item.getId()));
                fiveFinanceIncome.setIncomeConfirmMoneyIng(MyStringUtil.getNewSubMoney(fiveFinanceIncome.getIncomeConfirmMoneyIng(),item.getMoney()));
                fiveFinanceIncomeMapper.updateByPrimaryKey(fiveFinanceIncome);
            }
            FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(dto.getIncomeId());
            fiveFinanceIncome.setConfirmIds(fiveFinanceIncome.getConfirmIds()+","+item.getId());
            fiveFinanceIncome.setIncomeConfirmMoneyIng(MyStringUtil.getNewAddMoney(fiveFinanceIncome.getIncomeConfirmMoneyIng(),dto.getMoney()));
            fiveFinanceIncomeMapper.updateByPrimaryKey(fiveFinanceIncome);
        }*/
        item.setIncomeId(dto.getIncomeId());
        item.setIncomeRemark(dto.getIncomeRemark());
        item.setType(dto.getType());
        item.setSourceAccount(dto.getSourceAccount());
        item.setTargetAccount(dto.getTargetAccount());
        //??? ??? ????????????
        item.setMoney(MyStringUtil.getMoneyW(dto.getMoney()));
        item.setMoneyMax(dto.getMoneyMax());
        item.setIncomeTime(dto.getIncomeTime());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        //??? ??? ????????????
        item.setDesignTargetMoney(MyStringUtil.getMoneyW(dto.getDesignTargetMoney()));
        item.setDesignSucessMoney(MyStringUtil.getMoneyW(dto.getDesignSucessMoney()));

        item.setDesignAskRate(dto.getDesignAskRate());
        item.setManagerRate(dto.getManagerRate());
        item.setTotalTargetMoney(dto.getTotalTargetMoney());
        item.setTotalSucessMoney(dto.getTotalSucessMoney());
        item.setTotalRate(dto.getTotalRate());
        item.setRemark(dto.getRemark());

        item.setRefund(dto.getRefund());

        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(dto.getCreator());
        item.setCreator(dto.getCreator());
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(dto.getCreator()).getUserName());
/*        item.setDeptId(hrEmployeeSysDto.getDeptId());
        item.setDeptName(hrEmployeeSysDto.getDeptName());*/
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "????????????:"+item.getSourceAccount());
        myActService.setVariables(item.getProcessInstanceId(),variables);

        if(dto.getId()==0){
            FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(item.getIncomeId());

            Assert.state(MyStringUtil.getIntList(fiveFinanceIncome.getConfirmIds()).size()==0,"????????????????????????");
            fiveFinanceIncomeConfirmMapper.insert(item);
            //??????????????????
            fiveFinanceIncome.setConfirmIds(fiveFinanceIncome.getConfirmIds() + "," + item.getId());
            //???????????? ???????????? ??????????????????
            fiveFinanceIncome.setIncomeConfirmMoneyIng(fiveFinanceIncome.getMoney());
            fiveFinanceIncomeMapper.updateByPrimaryKey(fiveFinanceIncome);
        }else{
            BeanValidator.check(item);
            fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(item);
        }

        String processKey = EdConst.FIVE_FINANCE_INCOME_CONFIRM;
        String businessKey = processKey + "_" + item.getId();
        item.setBusinessKey(businessKey);
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(item);

        return item.getId();
    }

    public FiveFinanceIncomeConfirmDto getModelById(int id) {
        FiveFinanceIncomeConfirmDto dto = getDto(fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(id));
        //0.000000????????????''
        try{
            if(Double.valueOf(dto.getMoney()).equals(0.0)){
                dto.setMoney("");
            }
        }catch (Exception e){
            dto.setMoney("");
        }
        return dto;
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        FiveFinanceIncomeConfirm item = new FiveFinanceIncomeConfirm();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setDeptId(hrEmployeeSysDto.getDeptId());
        item.setDeptName(hrEmployeeSysDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveFinanceIncomeConfirmMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_INCOME_CONFIRM;
        String businessKey=processKey+"_"+item.getId();
       /* Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "????????????");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);*/
        item.setBusinessKey(businessKey);
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public FiveFinanceIncomeConfirmDto getDto(Object item) {
        FiveFinanceIncomeConfirm model= (FiveFinanceIncomeConfirm) item;
        FiveFinanceIncomeConfirmDto dto=FiveFinanceIncomeConfirmDto.adapt(model);

        dto.setProcessName("?????????");
        //???????????????
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            //MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("?????????");
            }
            //dto.setBusinessKey(processInstanceDto.getBusinessKey());
              /*if(!dto.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            dto.setProcessEnd(true);
            fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(dto);
            //???????????? ?????????????????????
            if(dto.getIncomeId()!=0){
                FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(dto.getIncomeId());
                fiveFinanceIncome.setIncomeConfirmMoney(MyStringUtil.getNewAddMoney(fiveFinanceIncome.getIncomeConfirmMoney(),model.getMoney()));
                fiveFinanceIncome.setIncomeConfirmMoneyIng(MyStringUtil.getNewSubMoney(fiveFinanceIncome.getIncomeConfirmMoneyIng(),model.getMoney()));
                fiveFinanceIncomeMapper.updateByPrimaryKey(fiveFinanceIncome);
            }
        }*/
        }


        if(model.getIncomeId()!=0){
            FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(model.getIncomeId());
            dto.setFiveFinanceIncome(fiveFinanceIncome);
        }
        //??????????????????/??????
        if(MyStringUtil.getStringList(dto.getInvoiceIds()).size()==0&&MyStringUtil.getStringList(dto.getIncomeIds()).size()==0){
            dto.setChooseInvoiceOrContract(false);
        }else{
            dto.setChooseInvoiceOrContract(true);
            //??????
            if(MyStringUtil.getStringList(dto.getInvoiceIds()).size()>0){
                FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(Integer.valueOf(MyStringUtil.getStringList(dto.getInvoiceIds()).get(0)));
                dto.setInvoiceNo(invoice.getInvoiceNo());
                dto.setContractName(invoice.getContractName());
                dto.setContractNo(invoice.getContractNo());
            }else{
                BusinessIncome businessIncome = businessIncomeMapper.selectByPrimaryKey(Integer.valueOf(MyStringUtil.getStringList(dto.getIncomeIds()).get(0)));
                dto.setInvoiceNo("???????????????");
                dto.setContractName(businessIncome.getContractName());
                dto.setContractNo(businessIncome.getContractNo());
            }
        }


        //?????? ?????? ??? ??????
        dto.setMoney(MyStringUtil.getMoneyY(dto.getMoney()));
        dto.setDesignTargetMoney(MyStringUtil.getMoneyY(dto.getDesignTargetMoney()));
        dto.setDesignSucessMoney(MyStringUtil.getMoneyY(dto.getDesignSucessMoney()));
        //?????????????????????
        if(dto.getIncomeId()!=0){
            FiveFinanceIncome income = fiveFinanceIncomeMapper.selectByPrimaryKey(dto.getIncomeId());
            //?????? ?????? ??? ??????
            dto.setIncomeConfirmMoney(MyStringUtil.getMoneyY(income.getIncomeConfirmMoney()));
            dto.setIncomeConfirmMoneyIng(MyStringUtil.getMoneyY(income.getIncomeConfirmMoneyIng()));
            dto.setIncomeMoney(MyStringUtil.getMoneyY(income.getMoney()));
            dto.setIncomeMoneyRemain(MyStringUtil.getMoneyY(MyStringUtil.getNewSubMoney(income.getMoney()
                    ,MyStringUtil.getNewAddMoney(income.getIncomeConfirmMoney(),income.getIncomeConfirmMoneyIng()))));
        }

        return dto;
    }

    public List<FiveFinanceIncomeConfirm> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceIncomeConfirmMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);

        //1.???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map = new HashMap();
        map.put("myDeptData", true);
        map.put("uiSref", uiSref);
        map.put("enLogin", userLogin);
        params.putAll(getDefaultRightParams(map));

        //2.????????????????????????
        int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
        List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
        deptIds.add(headDeptId);
        //???????????????????????????
        if(params.containsKey("deptId")){
            params.remove("deptId");
        }
        if(params.containsKey("deptIdList")){
            deptIds.addAll((List<Integer>)params.get("deptIdList"));
            params.put("deptIdList",deptIds);
        }else{
            params.put("deptIdList", deptIds);
        }


        //3.???????????????????????????????????????
        List<Integer> financeDeptIdList = selectEmployeeService.getMyFinanceDeptIds(userLogin);
        if(financeDeptIdList.size()>0){
            //????????????????????????
            financeDeptIdList.addAll((List<Integer>)params.get("deptIdList"));
            params.put("deptIdList", financeDeptIdList);
        }


        params.put("isLikeSelect","true");

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceIncomeConfirmMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceIncomeConfirm item=(FiveFinanceIncomeConfirm)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * ???????????????????????????
     * @param money
     * @return
     */
    public String moneyTurnCapital(Double money){
        return NumberChangeUtils.moneyToChinese(money);
    }
    //??????????????????
    public List<FiveFinanceIncomeDto> getIncomeList(int incomeConfirmId,String userLogin, String uiSref) throws ParseException {
        List<FiveFinanceIncomeDto> list =new ArrayList<>();
        FiveFinanceIncomeConfirmDto incomeConfirm = getModelById(incomeConfirmId);
        if(incomeConfirm.getIncomeId()!=0){
            list.add(fiveFinanceIncomeService.getModelById(incomeConfirm.getIncomeId()));
        }
        Map map =new HashMap();
        map.put("deleted",false);
        List<FiveFinanceIncome> incomes = fiveFinanceIncomeMapper.selectAll(map);
        for(FiveFinanceIncome income:incomes){
            FiveFinanceIncomeDto dto = fiveFinanceIncomeService.getDto(income);
            //?????? ?????????????????? ????????????????????????
            if(!dto.getId().equals(incomeConfirm.getIncomeId())&&!dto.getIsFullMoney()) {
                list.add(dto);
            }
        }
        return list;
    }

    public String saveSelectInvoice(int incomeConfirmId, String invoiceIds) {
        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);
        //???????????????id
        List<Integer> preInvoiceIds = MyStringUtil.getIntList(incomeConfirm.getInvoiceIds());
        //?????????????????????id
        List<Integer> invoiceIdList = MyStringUtil.getIntList(invoiceIds);

        //???????????????
        for(int invoiceId:invoiceIdList){
            FiveFinanceInvoiceDto invoice = fiveFinanceInvoiceService.getModelById(invoiceId);
            //?????????????????? ??????
            if(!preInvoiceIds.contains(invoiceId)){
                //??????????????????
                int businessIncomeId = businessIncomeService.getNewModelByConfirm(incomeConfirmId, invoiceId,incomeConfirm.getCreator());
                //???????????????????????? ????????????
                BusinessIncomeDto businessIncome = businessIncomeService.getModelById(businessIncomeId);
                businessIncome.setInvoiceId(invoiceId);
                businessIncome.setContractLibraryId(invoice.getContractLibraryId());
                businessIncome.setContractName(invoice.getContractName());
                businessIncome.setContractNo(invoice.getContractNo());
                businessIncome.setInvoiceNo(invoice.getInvoiceNo());
                businessIncome.setLegalDept(invoice.getLegalDept());

                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(invoice.getContractLibraryId());
                businessIncome.setProjectNo(library.getProjectNo());
                businessIncome.setProjectName(library.getProjectName());
                businessIncome.setContractIncomeMoney(MyStringUtil.moneyToString(library.getContractIncomeMoney()));
                businessIncome.setContractMoney(MyStringUtil.moneyToString(library.getInvestMoney()));
                businessIncomeMapper.updateByPrimaryKey(businessIncome);

                //?????? ????????????????????????
                invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoneyIng(),businessIncome.getIncomeMoney()));
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);

                //??????????????????????????? ?????????????????????
                incomeConfirm =fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);
                incomeConfirm.setInvoiceIds(incomeConfirm.getInvoiceIds()+","+invoiceId);
                fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
                invoice.setIncomeConfirmIds(invoice.getIncomeConfirmIds()+","+incomeConfirmId);
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
            }
        }

        return "??????????????????????????????";

    }

    //?????? ????????????
    public int getNotarizeIncome(int incomeConfirmId, String userLogin) {
        Assert.state(incomeConfirmId!=0,"???????????????!");
        FiveFinanceIncomeConfirm item = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);
        //???????????? ????????????
        List<Integer> incomeIds = MyStringUtil.getIntList(item.getIncomeIds());
        List<Integer> invoiceIds =  MyStringUtil.getIntList(item.getInvoiceIds());

        Assert.state(incomeIds.size()!=0,"?????????????????? ??? ?????????");

        //??????????????????????????????
        for(int incomeId :incomeIds){
            BusinessIncome businessIncome = businessIncomeMapper.selectByPrimaryKey(incomeId);
            Assert.state(businessIncome.getProcessEnd(),"?????????????????? ??????????????????");
        }
        for(int invoiceId :invoiceIds){
            FiveFinanceInvoice fiveFinanceInvoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(invoiceId);
            Assert.state(fiveFinanceInvoice.getProcessEnd(),"?????????????????? ????????????");
        }

        if (item.getProcessEnd()) {
            item.setProcessEnd(false);
        } else {
            item.setProcessEnd(true);
        }
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(item);
        return item.getId();
    }
}
