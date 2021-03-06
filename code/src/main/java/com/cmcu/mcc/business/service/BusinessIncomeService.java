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
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");
        //???????????????????????? ??????
        if(item.getContractLibraryId()!=0){
            FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            oldLibrary.setIncomeIds(MyStringUtil.getNewStringId(oldLibrary.getIncomeIds(),id));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
        }
        //????????????????????? ????????????
        if(item.getIncomeConfirmId()!=0){
            FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(item.getIncomeConfirmId());
            incomeConfirm.setIncomeIds(MyStringUtil.getNewStringId(incomeConfirm.getIncomeIds(),id));
            fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        }
        //????????????????????????????????????????????? ?????????????????????????????????
        if(item.getIncomeConfirmId()!=0&&item.getInvoiceId()!=0){
            FiveFinanceInvoice fiveFinanceInvoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
            fiveFinanceInvoice.setIncomeConfirmIds(MyStringUtil.getNewStringId(fiveFinanceInvoice.getIncomeConfirmIds(),item.getIncomeConfirmId()));
            fiveFinanceInvoice.setIncomeConfirmId(0);
            //????????????????????????
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
        //?????????????????????
        if(item.getContractLibraryId()!=0){
            //?????????????????? ??????
            if(item.getContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                oldLibrary.setIncomeIds(MyStringUtil.getNewStringId(oldLibrary.getIncomeIds(),item.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
            }
            //????????? ????????????
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setIncomeIds(library.getIncomeIds()+","+item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        //????????????????????????
        if(item.getInvoiceId()!=0){
            //?????????????????? ?????????????????? ???????????????
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
        variables.put("processDescription", "????????????:"+item.getContractName());
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
        variables.put("processDescription", "????????????");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("?????????????????????(??????)"));//?????????????????????????????????
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessIncomeMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public BusinessIncomeDto getDto(Object item) {
        BusinessIncome model= (BusinessIncome) item;
        BusinessIncomeDto dto=BusinessIncomeDto.adapt(model);
        //??????????????????
        dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(),6));
        dto.setContractIncomeMoney(MyStringUtil.moneyToString(dto.getContractIncomeMoney(),6));
        dto.setIncomeMoney(MyStringUtil.moneyToString(dto.getIncomeMoney(),6));
        //???????????????
        if(model.getInvoiceId()!=0){
            FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(model.getInvoiceId());
            dto.setInvoiceGetMoney(MyStringUtil.moneyToString(invoice.getInvoiceGetMoney(),6));
            dto.setInvoiceMoney(MyStringUtil.moneyToString(invoice.getInvoiceMoney(),6));
        }
        dto.setProcessName("?????????");
        if(!dto.getProcessEnd()&&StringUtils.isNotEmpty(dto.getProcessInstanceId())) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessName("?????????");
                dto.setProcessEnd(true);
                businessIncomeMapper.updateByPrimaryKey(dto);
                //??????????????????
                fiveBusinessContractLibraryService.getIncomeMoney(dto.getId());
                //??????????????? ??????????????????
                if (dto.getInvoiceId() != 0) {
                    FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(dto.getInvoiceId());
                    invoice.setInvoiceGetMoney(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoney(), dto.getIncomeMoney()));
                    //??????????????????????????????
                    invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewSubMoney(invoice.getInvoiceGetMoneyIng(), dto.getIncomeMoney()));
                    fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
                } else {
                    //????????????  ?????????????????????
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

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
/*        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));*/
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        if (deptIdList.size()==0){
            //??????????????????deptIds
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
     * ???????????????????????????
     * @param money
     * @return
     */
    public String moneyTurnCapital(Double money){
        return NumberChangeUtils.moneyToChinese(money*10000);
    }
    //??????????????? ??????????????????
    public int getNewModelByConfirm(int incomeConfirmId,int invoiceId, String userLogin) {

        //????????????????????????????????????
        List<BusinessIncome> businessIncomes = getIncomesByIncomeConfirmId(incomeConfirmId);
        for(BusinessIncome businessIncome:businessIncomes){
            if(businessIncome.getInvoiceId().equals(invoiceId)){
                Assert.state(false,"???????????????"+businessIncome.getInvoiceNo()+" ??????????????? ????????????");
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
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "????????????");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("?????????????????????(??????)"));//?????????????????????????????????
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);

        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);

        //?????????????????????
        item.setIncomeMoney(incomeConfirm.getMoney());
        item.setIncomeMoneyMax(incomeConfirm.getMoneyMax());

        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessIncomeMapper.updateByPrimaryKey(item);


        //??????????????????
        incomeConfirm.setIncomeIds(incomeConfirm.getIncomeIds()+","+item.getId());
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        return item.getId();
    }

    //??????????????? ??????????????????  ???????????????
    public int getNewModelByConfirm2(int incomeConfirmId,int libraryId,int invoiceId, String userLogin) {
        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(libraryId);


        //????????????????????????????????????
        List<BusinessIncome> businessIncomes = getIncomesByIncomeConfirmId(incomeConfirmId);
        for(BusinessIncome businessIncome:businessIncomes){
            if(businessIncome.getInvoiceId().equals(invoiceId)){
                Assert.state(false,"???????????????"+businessIncome.getInvoiceNo()+" ??????????????? ????????????");
            }
        }

        BusinessIncome item = new BusinessIncome();
        item.setIncomeConfirmId(incomeConfirmId);
        //??????????????????
        item.setContractLibraryId(libraryId);

        item.setContractName(library.getContractName());
        //?????????
        item.setContractMoney(library.getContractMoney());
        //?????????
        item.setContractNo(library.getContractNo());
        //??????????????????
        item.setContractIncomeMoney(library.getContractIncomeMoney());
        //????????????
        item.setProjectName(library.getProjectName());
        //?????????
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
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "????????????");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("?????????????????????(??????)"));//?????????????????????????????????
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);

        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);

        //?????????????????????
        item.setIncomeMoney(incomeConfirm.getMoney());
        item.setIncomeMoneyMax(incomeConfirm.getMoneyMax());

        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        businessIncomeMapper.updateByPrimaryKey(item);

        //???????????????
        library.setIncomeIds(library.getIncomeIds()+","+item.getId());
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);

        //??????????????????
        incomeConfirm.setIncomeIds(incomeConfirm.getIncomeIds()+","+item.getId());
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        return item.getId();
    }
    //?????????????????? ????????????
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
            //????????????
            String verifyTime = map.getOrDefault(1, "").toString();
            //??????
            String remark = map.getOrDefault(2, "").toString();
            //????????????
            String incomeMoney = map.getOrDefault(3, "").toString();
            //?????????
            String contractNo = map.getOrDefault(4, "").toString();
            //??????
            String deptName = map.getOrDefault(5, "").toString();

            if (Strings.isNullOrEmpty(verifyTime) || Strings.isNullOrEmpty(incomeMoney) || Strings.isNullOrEmpty(deptName)) {
                Assert.state(false, "???" + (i + 1) + "?????????????????????");
            }

           /* HrDept hrDept = hrDeptService.selectByName(deptName);
            if (hrDept == null) {
                Map map2 = new LinkedHashMap();
                map2.put("????????????",map.getOrDefault(1,"").toString());
                map2.put("??????",map.getOrDefault(2,"").toString());
                map2.put("????????????",map.getOrDefault(3,"").toString());
                map2.put("?????????",map.getOrDefault(4,"").toString());
                map2.put("????????????",map.getOrDefault(5,"").toString());
                map2.put("??????????????????"," ???????????????"+deptName + "????????????????????????");
                list.add(map2);
                continue;
            }*/

            //??????????????????????????? ????????? ????????????????????????
            FiveBusinessContractLibraryDto contractLibrary = fiveBusinessContractLibraryService.getModelByContractNo(contractNo);
            if(contractLibrary==null){
                Map map2 = new LinkedHashMap();
                map2.put("????????????",map.getOrDefault(1,"").toString());
                map2.put("??????",map.getOrDefault(2,"").toString());
                map2.put("????????????",map.getOrDefault(3,"").toString());
                map2.put("?????????",map.getOrDefault(4,"").toString());
                map2.put("????????????",map.getOrDefault(5,"").toString());
                map2.put("??????????????????"," ????????????"+contractNo + "????????????????????????");
                list.add(map2);
                continue;
            }

            BusinessIncome item;
            //????????? ??????????????? ??????????????????  ????????????
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
                    Assert.state(false, "???" + (i + 1) + "??? ???????????????"+deptName + "????????????????????????");
                }*/

                ModelUtil.setNotNullFields(item);
                businessIncomeMapper.updateByPrimaryKey(item);
            } else {
                try{
                    //??????
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
                    //????????????
                    contractLibrary.setIncomeIds(contractLibrary.getIncomeIds()+","+item.getId());
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(contractLibrary);
                    //????????????????????????
                    fiveBusinessContractLibraryService.getIncomeMoney(item.getId());
                    Map map2 = new LinkedHashMap();
                    map2.put("????????????",map.getOrDefault(1,"").toString());
                    map2.put("??????",map.getOrDefault(2,"").toString());
                    map2.put("????????????",map.getOrDefault(3,"").toString());
                    map2.put("?????????",map.getOrDefault(4,"").toString());
                    map2.put("????????????",map.getOrDefault(5,"").toString());
                    map2.put("??????????????????","");
                    list.add(map2);
                }catch(Exception e){
                    Map map2 = new LinkedHashMap();
                    map2.put("????????????",map.getOrDefault(1,"").toString());
                    map2.put("??????",map.getOrDefault(2,"").toString());
                    map2.put("????????????",map.getOrDefault(3,"").toString());
                    map2.put("?????????",map.getOrDefault(4,"").toString());
                    map2.put("????????????",map.getOrDefault(5,"").toString());
                    map2.put("??????????????????","????????????????????????????????????");
                    list.add(map2);
                }
            }
        }

        return list;
    }

    //?????? ????????????
    public int getNotarizeIncome(int incomeId, String userLogin) {
        BusinessIncome item = businessIncomeMapper.selectByPrimaryKey(incomeId);
        //????????????????????????
        Assert.state(item.getContractLibraryId()!=0,"?????????????????????????????????????????????");
        if (item.getProcessEnd()) {
            item.setProcessEnd(false);
            //?????????????????? ??????
            fiveBusinessContractLibraryService.subIncomeMoney(item.getId());
            //??????????????? ??????????????????
            if(item.getInvoiceId()!=0){
                FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
                invoice.setInvoiceGetMoney(MyStringUtil.getNewSubMoney(invoice.getInvoiceGetMoney(),item.getIncomeMoney(),8));
                //??????????????????????????????
                invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoneyIng(),item.getIncomeMoney(),8));
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
            }else{
                //????????????  ?????????????????????
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                library.setPreContractMoney(MyStringUtil.getNewSubMoney(library.getPreContractMoney(),item.getIncomeMoney(),8));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
        } else {
            item.setProcessEnd(true);
            //??????????????????  ??????
            fiveBusinessContractLibraryService.getIncomeMoney(item.getId());
            //??????????????? ??????????????????
            if(item.getInvoiceId()!=0){
                FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
                invoice.setInvoiceGetMoney(MyStringUtil.getNewAddMoney(invoice.getInvoiceGetMoney(),item.getIncomeMoney()));
                //??????????????????????????????
                invoice.setInvoiceGetMoneyIng(MyStringUtil.getNewSubMoney(invoice.getInvoiceGetMoneyIng(),item.getIncomeMoney()));
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
            }else{
                //????????????  ?????????????????????
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                library.setPreContractMoney(MyStringUtil.getNewAddMoney(library.getPreContractMoney(),item.getIncomeMoney()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
        }
        businessIncomeMapper.updateByPrimaryKey(item);
        return item.getId();
    }
}
