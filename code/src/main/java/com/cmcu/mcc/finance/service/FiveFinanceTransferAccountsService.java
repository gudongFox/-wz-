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
import com.cmcu.mcc.business.entity.BusinessRecord;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceTransferAccountsDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceTransferAccountsMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceTransferAccountsDetailDto;
import com.cmcu.mcc.finance.dto.FiveFinanceTransferAccountsDto;
import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccounts;
import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccountsDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

import static jdk.nashorn.internal.objects.NativeString.indexOf;

@Service
public class FiveFinanceTransferAccountsService {
    @Resource
    FiveFinanceTransferAccountsMapper fiveFinanceTransferAccountsMapper;
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
    HrDeptService hrDeptService;
    @Autowired
    FiveFinanceTransferAccountsDetailMapper fiveFinanceTransferAccountsDetailMapper;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id, String userLogin) {
        FiveFinanceTransferAccounts item = fiveFinanceTransferAccountsMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);
        //删除子表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("transferAccountsId", id);//小写
        List<FiveFinanceTransferAccountsDetail> details = fiveFinanceTransferAccountsDetailMapper.selectAll(params);
        for(FiveFinanceTransferAccountsDetail detail:details){
            detail.setDeleted(true);
            fiveFinanceTransferAccountsDetailMapper.updateByPrimaryKey(detail);
        }
    }

    public void update(FiveFinanceTransferAccountsDto dto) {
        FiveFinanceTransferAccountsDto model = getModelById(dto.getId());

        model.setOutBankAccount(dto.getOutBankAccount());
        model.setOutBankName(dto.getOutBankName());
        model.setInBankName(dto.getInBankName());
        model.setInBankAccount(dto.getInBankAccount());
        model.setAccountNumber(dto.getAccountNumber());
        model.setApplicant(dto.getApplicant());
        model.setApplicantName(dto.getApplicantName());
        model.setTitle(dto.getTitle());

        if(dto.getLoan()){
            model.setLoan(dto.getLoan());
            model.setLoanId(dto.getLoanId());
            model.setLoanTitle(dto.getLoanTitle());
        }else{
            model.setLoan(dto.getLoan());
            model.setLoanId(0);
            model.setLoanTitle("");
        }

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

        model.setAccountName(dto.getAccountName());
        model.setReceiveName(dto.getReceiveName());
        model.setReceiveDeptName(dto.getReceiveDeptName());
        model.setApplicantTime(dto.getApplicantTime());
        //元转为万元
        model.setTotalMoney(MyStringUtil.getMoneyW(dto.getTotalMoney()));
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);
        fiveFinanceTransferAccountsMapper.updateByPrimaryKey(model);

        int project = 0;
        if (dto.getProjectName().trim().length()==0) {
            project = 1;
            if (selectEmployeeService.getDeptChargeMen(dto.getDeptId()).contains(dto.getCreator())){
                project = 2;
            }
        }

        Map variables = Maps.newHashMap();
        variables.put("project", project);
        variables.put("flag", Double.valueOf(model.getTotalMoney())>=1.0?true:false);
        variables.put("flag1", Double.valueOf(model.getTotalMoney())>=3.0?true:false);
        variables.put("flag2", Double.valueOf(model.getTotalMoney())>=5.0?true:false);

        variables.put("deputy", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//副院长
        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务确认
        variables.put("projectManager",dto.getBusinessManager());//项目经理
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门负责人
        variables.put("deptLeader",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//分管领导
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//财务负责人
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(18));//主管副职领导
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("总会计师"));
        variables.put("generalManager", hrEmployeeService.selectUserByPositionName("总经理"));
        variables.put("financialAccount", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务核算

        String title = "费用退款:" + model.getDeptName();
        variables.put("processDescription", title);

        myActService.setVariables(model.getProcessInstanceId(), variables);
    }

    public FiveFinanceTransferAccountsDto getModelById(int id) {
        return getDto(fiveFinanceTransferAccountsMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceTransferAccountsDto getDto(FiveFinanceTransferAccounts item) {
        FiveFinanceTransferAccountsDto dto = FiveFinanceTransferAccountsDto.adapt(item);
        if(dto.getProjectId()!=0){
            dto.setIsProject(true);
        }

        CustomSimpleProcessInstance customSimpleProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
        dto.setProcessName(customSimpleProcessInstance.getCurrentTaskName());

        if (!item.getProcessEnd() && customSimpleProcessInstance.isFinished()) {
            dto.setProcessEnd(true);
            fiveFinanceTransferAccountsMapper.updateByPrimaryKey(dto);
        }
        if (StringUtils.isEmpty(customSimpleProcessInstance.getCurrentTaskName())) {
            dto.setProcessName("流程已结束");
        }

        //如果不是借款退款
        if(!item.getLoan()){
            String applyMoney = "0";
            List<FiveFinanceTransferAccountsDetailDto> detailList = listDetail(item.getId());
            for (FiveFinanceTransferAccountsDetailDto detail : detailList) {
                applyMoney = MyStringUtil.getNewAddMoney(applyMoney, detail.getApplyMoney());
            }
            //万元 转为 元
            dto.setTotalMoney(MyStringUtil.moneyToString(applyMoney,2));
        }

        return dto;
    }

    public FiveFinanceTransferAccountsDetailDto getDetailDto(FiveFinanceTransferAccountsDetail item) {
        // 万元 转为 元
        item.setApplyMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyY(item.getApplyMoney()),2));
        FiveFinanceTransferAccountsDetailDto dto = FiveFinanceTransferAccountsDetailDto.adapt(item);

        return dto;
    }

    public int getNewModel(String userLogin, String uiSref) {
        FiveFinanceTransferAccounts item = new FiveFinanceTransferAccounts();
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
        item.setLoan(false);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setApplicantTime(MyDateUtil.getStringToday());

        ModelUtil.setNotNullFields(item);
        fiveFinanceTransferAccountsMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));

        String title = "费用退款:"+item.getDeptName();
        if (uiSref.indexOf("Red") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_RED + "_" + item.getId();
            title = "费用退款(红河):" + item.getDeptName();
            variables.put("processDescription", title);
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_RED, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Build") != -1) {
            String businessKey = EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_BUILD + "_" + item.getId();
            title = "费用退款(建研院):" + item.getDeptName();
            variables.put("processDescription", title);
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_BUILD, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else if (uiSref.indexOf("Fee") != -1) {
            // title = "费用退款(财务):" + item.getDeptName();
            variables.put("processDescription", title);
            String businessKey = EdConst.FIVE_FINANCE_TRANSFER_FEE + "_" + item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRANSFER_FEE, businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            item.setBusinessKey(businessKey);
        } else {
            if (item.getDeptId().equals(36)) {
                title = "费用退款(财务):" + item.getDeptName();
                variables.put("processDescription", title);
                String businessKey = EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_FINANCE + "_" + item.getId();
                String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_FINANCE, businessKey, variables, MccConst.APP_CODE);
                item.setProcessInstanceId(processInstanceId);
                item.setBusinessKey(businessKey);
            } else {
                title = "费用退款(生产单位):" + item.getDeptName();
                variables.put("processDescription", title);
                String businessKey = EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_COMMON + "_" + item.getId();
                String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_COMMON, businessKey, variables, MccConst.APP_CODE);
                item.setProcessInstanceId(processInstanceId);
                item.setBusinessKey(businessKey);
            }
        }

        item.setTitle(title);
        fiveFinanceTransferAccountsMapper.updateByPrimaryKey(item);
        //自动生成单据号
        String accountNumber = getAccountNumber(item.getId());
        item.setAccountNumber(accountNumber);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("isLikeSelect",true);

        if (uiSref.indexOf("Red") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_RED);
        } else if (uiSref.indexOf("Build") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_BUILD);
        } else if (uiSref.indexOf("Fee") != -1) {
            params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRANSFER_FEE);
        } else {
            if (hrEmployeeService.getModelByUserLogin(userLogin).getDeptId().equals(36)) {
                params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_FINANCE);
            } else {
                params.put("qBusinessKey", EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_COMMON);
            }
        }

        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptListOa(userLogin, uiSref);
        if (deptIdList.size() == 0) {
            params.put("creator", userLogin);
        } else {
            params.put("deptIdList", deptIdList);
        }

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceTransferAccountsMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceTransferAccounts item = (FiveFinanceTransferAccounts) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveFinanceTransferAccountsDetail item) {
        if (item.getFlag()==1){
            //元 转为 万元
            item.setApplyMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyW(item.getApplyMoney())));
            fiveFinanceTransferAccountsDetailMapper.insert(item);
        }
        FiveFinanceTransferAccountsDetail model = fiveFinanceTransferAccountsDetailMapper.selectByPrimaryKey(item.getId());
        model.setBudgetId(item.getBudgetId());
        model.setBudgetType(item.getBudgetType());
        model.setChargePlan(item.getChargePlan());
        model.setChargeProject(item.getChargeProject());
        model.setItem(item.getItem());
        //元 转为 万元
        model.setApplyMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyW(item.getApplyMoney())));
        model.setRemark(item.getRemark());
        model.setChargeAgainst(item.getChargeAgainst());
        model.setDeduction(item.getDeduction());
        fiveFinanceTransferAccountsDetailMapper.updateByPrimaryKey(model);
    }

    public FiveFinanceTransferAccountsDetailDto getNewModelDetail(int id) {
        FiveFinanceTransferAccountsDetail item = new FiveFinanceTransferAccountsDetail();
        item.setTransferAccountsId(id);
        item.setApplyMoney(MyStringUtil.moneyToString("0"));//报销金额
        item.setDeduction(MyStringUtil.moneyToString("0"));
        item.setChargeAgainst(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"冲销状态").toString());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setFlag(1);  //新建item的标志，在保存时判断，若为1则插入，为0则update
        ModelUtil.setNotNullFields(item);
        return getDetailDto(item);

    }

    public FiveFinanceTransferAccountsDetail getModelDetailById(int id) {
        return getDetailDto(fiveFinanceTransferAccountsDetailMapper.selectByPrimaryKey(id));
    }

    public List<FiveFinanceTransferAccountsDetailDto> listDetail(int id) {
        List<FiveFinanceTransferAccountsDetailDto> list = new ArrayList<>();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("transferAccountsId", id);//小写
        List<FiveFinanceTransferAccountsDetail> details = fiveFinanceTransferAccountsDetailMapper.selectAll(params);
        for(FiveFinanceTransferAccountsDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public void removeDetail(int id) {
        FiveFinanceTransferAccountsDetail model = fiveFinanceTransferAccountsDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceTransferAccountsDetailMapper.updateByPrimaryKey(model);
    }

    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceTransferAccounts item = fiveFinanceTransferAccountsMapper.selectByPrimaryKey(id);
        data.put("deptName", item.getDeptName());

        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        Map map = new HashMap();
        map.put("materialPurchaseId", item.getId());
        map.put("deleted", false);
        List<FiveFinanceTransferAccountsDetail> materialPurchaseDetails = fiveFinanceTransferAccountsDetailMapper.selectAll(map);
        data.put("materialPurchaseDetails", materialPurchaseDetails);
/*        Double sum=0.0d;
        for (FiveFinanceTransferAccountsDetail detail:materialPurchaseDetails) {
            sum+=Double.valueOf(detail.getBookNumber());
        }
        data.put("sum",sum);*/
        data.put("nodes", actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

    /**
     借款 01
     费用报销 02
     差旅费报销 03
     退款（费用退款）04

     职能单位Z
     生产单位S
     红河项目H
     建研院总部J

     2021 03 12 01（类型编号）001（该类型的第几个）Z 20210312 01001
     */
    public String getAccountNumber(int id){
        try{FiveFinanceTransferAccounts item = fiveFinanceTransferAccountsMapper.selectByPrimaryKey(id);
            FiveFinanceTransferAccountsDto transferAccountsDto =getModelById(id);
            String newAccountNumber ="";

            String key ="";
            key=item.getBusinessKey();
            System.out.println(key);
            //单位代码
            String deptCode="";
            // if (EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_COMMON.contains(key)){
            if(key.contains("Red")){//EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_RED
                deptCode="H";//红河项目
            }else {
                if (hrDeptService.getModelById(item.getDeptId()).getDeptType().equals("设计")){
                    deptCode="S";//生产单位
                }else if(hrDeptService.getModelById(item.getDeptId()).getDeptType().equals("职能")){
                    deptCode="Z";//职能单位
                }else {
                    deptCode = "J";//建研院总部
                }
            }
            //日期代码
            String date=MyDateUtil.dateToStr1(item.getGmtCreate());

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            // params.put("projectNoHead",year+deptCode+typeCode);
            List<FiveFinanceTransferAccounts> fiveFinanceTransferAccounts1 = fiveFinanceTransferAccountsMapper.selectAll(params);
            int size=0;
            //判断顺序代码最大值
            if (!fiveFinanceTransferAccounts1.isEmpty()){
                for (FiveFinanceTransferAccounts fiveFinanceTransferAccounts:fiveFinanceTransferAccounts1){
                    String accountNumber = fiveFinanceTransferAccounts.getAccountNumber();
                    if(fiveFinanceTransferAccounts.getId()!=id&&StringUtils.isNotEmpty(accountNumber)){
                        String maxSize=  accountNumber.substring(accountNumber.length()-3);
                        if (size<Integer.parseInt(maxSize)){
                            size = Integer.parseInt(maxSize);
                        }
                    }
                }
            }
            size=size+1;

            String format=String.format("%03d", size);//format为顺序号限定10进制补零
            //部门+时间+类型+编号
            newAccountNumber=newAccountNumber+deptCode+date+"-04-"+format;
            transferAccountsDto.setAccountNumber(newAccountNumber);
            update(transferAccountsDto);
            return newAccountNumber;

        }catch (Exception e){
            Assert.state(false,"请准确填写，预计开始时间、xxx、xxx！");
            return "";
        }
    }

}
