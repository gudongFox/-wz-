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
import com.cmcu.mcc.finance.dto.FiveFinanceSubpackagePaymentDto;
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
public class FiveFinanceSubpackagePaymentService {
    @Resource
    FiveFinanceSubpackagePaymentMapper fiveFinanceSubpackagePaymentMapper;
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
    FiveFinanceSubpackagePaymentDetailMapper fiveFinanceSubpackagePaymentDetailMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Resource
    ProcessQueryService processQueryService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveFinanceSubpackagePayment item = fiveFinanceSubpackagePaymentMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

        List<FiveFinanceSubpackagePaymentDetail> details = listDetail(id);
        for(FiveFinanceSubpackagePaymentDetail detail:details){
            detail.setDeleted(true);
            fiveFinanceSubpackagePaymentDetailMapper.updateByPrimaryKey(detail);
        }
    }


    public void update(FiveFinanceSubpackagePaymentDto dto){
        FiveFinanceSubpackagePayment model = fiveFinanceSubpackagePaymentMapper.selectByPrimaryKey(dto.getId());

        model.setProjectType(dto.getProjectType());
        model.setAttachedList(dto.getAttachedList());
        model.setTotalCount(dto.getTotalCount());
        model.setReceiptsNumber(dto.getReceiptsNumber());

        model.setPayName(dto.getPayName());
        model.setPayBank(dto.getPayBank());
        model.setPayAccount(dto.getPayAccount());
        model.setReceiveName(dto.getReceiveName());
        model.setReceiveBank(dto.getReceiveBank());
        model.setReceiveAccount(dto.getReceiveAccount());

        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());
        model.setPlan(dto.getPlan());

        fiveFinanceSubpackagePaymentMapper.updateByPrimaryKey(model);
        selectEmployeeService.getCompanyLeaders();

        Map variables = Maps.newHashMap();

        variables.put("dept",selectEmployeeService.getHeadDeptName(model.getDeptId()).contains("一院")?true:false);
        variables.put("flag", Double.valueOf(dto.getTotalCount())>=1.00?true:false);
        variables.put("flag1", Double.valueOf(dto.getTotalCount())>=3.00?true:false);
        variables.put("flag2", Double.valueOf(dto.getTotalCount())>=5.00?true:false);
        variables.put("flag3", Double.valueOf(dto.getTotalCount())>=50.00?true:false);
        variables.put("flag4", Double.valueOf(dto.getTotalCount())>=100.00?true:false);
        variables.put("flag5", Double.valueOf(dto.getTotalCount())>=2000.00?true:false);
        variables.put("projectFunds",model.getProjectType()=="分包工程款"?true:false);
        variables.put("plan",model.getPlan()?true:false);
        int choose = 0;
        if (model.getProjectType().equals("分包工程款、设备款")){
            choose = 3;
            variables.put("engineeringDeptChargeman",selectEmployeeService.getDeptChargeMen(29));// 工程部负责人
            variables.put("financialSecretary",selectEmployeeService.getOtherDeptChargeMan(18));// 财务部长
            variables.put("engineeringVicePresident",selectEmployeeService.getOtherDeptChargeMan(29));// 工程副总
        }else if (model.getProjectType().equals("分包设计费")){
            choose = 2;
        }else if (model.getProjectType().equals("科研支出")){
            choose = 1;
            variables.put("qualityDepartment",selectEmployeeService.getDeptChargeMen(101));// 科质部负责人
        }
        variables.put("contractReview",selectEmployeeService.getDeptChargeMen(model.getDeptId()));// 合同审核
        variables.put("designVicePresident",selectEmployeeService.getOtherDeptChargeMan(101));// 设计副总
        variables.put("choose",choose);
        variables.put("financeConfirm", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务确认
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门领导
        variables.put("chargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));//院长
        variables.put("financeDeputy", selectEmployeeService.getOtherDeptChargeMan(18));//财务主管副总
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("总会计师"));//总会计师
        variables.put("generalManager", hrEmployeeService.selectUserByPositionName("总经理"));//总经理
        variables.put("chairman",hrEmployeeService.selectUserByPositionName("董事长"));//董事长
        variables.put("financialAccount", selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//财务核算

        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveFinanceSubpackagePaymentDto getModelById(int id){

        return getDto(fiveFinanceSubpackagePaymentMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceSubpackagePaymentDto getDto(FiveFinanceSubpackagePayment item) {
        FiveFinanceSubpackagePaymentDto dto=FiveFinanceSubpackagePaymentDto.adapt(item);
        if(dto.getProjectId()!=0){
            dto.setIsProject(true);
        }

        CustomSimpleProcessInstance customSimpleProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
        dto.setProcessName(customSimpleProcessInstance.getCurrentTaskName());

        if (!item.getProcessEnd() && customSimpleProcessInstance.isFinished()) {
            dto.setProcessEnd(true);
            fiveFinanceSubpackagePaymentMapper.updateByPrimaryKey(dto);
        }
        if (StringUtils.isEmpty(customSimpleProcessInstance.getCurrentTaskName())) {
            dto.setProcessName("流程已结束");
        }

        String totalApplyMoney="0";
        List<FiveFinanceSubpackagePaymentDetail> detailList=listDetail(item.getId());
        for (FiveFinanceSubpackagePaymentDetail detail:detailList){
            totalApplyMoney=MyStringUtil.getNewAddMoney(totalApplyMoney,detail.getApplyMoney());
        }
        dto.setTotalApplyMoney(totalApplyMoney);//总报销金额
        dto.setTotalApplyMoney(MyStringUtil.moneyToString(dto.getTotalApplyMoney(),2));
        dto.setTotalCount(MyStringUtil.moneyToString(MyStringUtil.getMoneyW(totalApplyMoney),6));
        return dto;
    }

    public FiveFinanceSubpackagePaymentDetail getDetailDto(FiveFinanceSubpackagePaymentDetail item) {
        //万元 转换为 元
        item.setApplyMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyY(item.getApplyMoney()),2));
        item.setReplayMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyY(item.getReplayMoney()),2));
        if(Double.valueOf(item.getApplyMoney()).equals(0.0)){
            item.setApplyMoney("");
        }
        if(Double.valueOf(item.getReplayMoney()).equals(0.0)){
            item.setReplayMoney("");
        }

        return item;
    }

    public int getNewModel(String userLogin,String uiSref){
        FiveFinanceSubpackagePayment item=new FiveFinanceSubpackagePayment();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setPlan(false);
        item.setProjectType("其他");
        item.setTotalCount(MyStringUtil.moneyToString("0",6));//统计
        ModelUtil.setNotNullFields(item);
        fiveFinanceSubpackagePaymentMapper.insert(item);
        if(Double.valueOf(item.getTotalCount()).equals(0.0)){
            item.setTotalCount("");
        }

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));

        String businessKey = EdConst.FIVE_FINANCE_SUBPACKAGE_PAYMENT + "_" + item.getId();
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_SUBPACKAGE_PAYMENT, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);

        fiveFinanceSubpackagePaymentMapper.updateByPrimaryKey(item);
        //自动生成单据号
        String receiptsNumber = getReceiptsNumber(item.getId());
        item.setReceiptsNumber(receiptsNumber);

        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);

        params.put("qBusinessKey", EdConst.FIVE_FINANCE_SUBPACKAGE_PAYMENT);

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceSubpackagePaymentMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceSubpackagePayment item=(FiveFinanceSubpackagePayment)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveFinanceSubpackagePaymentDetail item){
        if (item.getId()==null||item.getId()==0){
            //元转为 万元
            item.setApplyMoney(MyStringUtil.getMoneyW(item.getApplyMoney()));
            item.setReplayMoney(MyStringUtil.getMoneyW(item.getReplayMoney()));
            fiveFinanceSubpackagePaymentDetailMapper.insert(item);
        }else {
            FiveFinanceSubpackagePaymentDetail model = fiveFinanceSubpackagePaymentDetailMapper.selectByPrimaryKey(item.getId());
            model.setDeptId(item.getDeptId());
            model.setDeptName(item.getDeptName());
            //元 转换为 万元
            model.setApplyMoney(MyStringUtil.moneyToString(MyStringUtil.getMoneyW(item.getApplyMoney())));
            model.setReplayMoney(MyStringUtil.getMoneyW(item.getReplayMoney()));
            model.setApplicantName(item.getApplicantName());
            model.setApplicant(item.getApplicant());
            model.setRemark(item.getRemark());
            fiveFinanceSubpackagePaymentDetailMapper.updateByPrimaryKey(model);
        }
        
    }

    public FiveFinanceSubpackagePaymentDetail getNewModelDetail(int  id,String userLogin){
        FiveFinanceSubpackagePayment fiveFinanceSubpackagePayment = fiveFinanceSubpackagePaymentMapper.selectByPrimaryKey(id);
        FiveFinanceSubpackagePaymentDetail item=new FiveFinanceSubpackagePaymentDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setSubpackagePaymentId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setApplyMoney(MyStringUtil.moneyToString("0",2));//申请金额
        item.setReplayMoney(MyStringUtil.moneyToString("0",2));//批复金额

        return item;
    }

    public FiveFinanceSubpackagePaymentDetail getModelDetailById(int id){
        FiveFinanceSubpackagePaymentDetail detail = fiveFinanceSubpackagePaymentDetailMapper.selectByPrimaryKey(id);
        return getDetailDto(detail);
    }

    public List<FiveFinanceSubpackagePaymentDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("subpackagePaymentId",id);//小写
        List<FiveFinanceSubpackagePaymentDetail> oList = fiveFinanceSubpackagePaymentDetailMapper.selectAll(params);
        List<FiveFinanceSubpackagePaymentDetail> list = new ArrayList<>();
        for(FiveFinanceSubpackagePaymentDetail detail:oList){
            list.add(getDetailDto(detail));
        }
        return list;
    }


    public void removeDetail(int id){
        FiveFinanceSubpackagePaymentDetail model = fiveFinanceSubpackagePaymentDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceSubpackagePaymentDetailMapper.updateByPrimaryKey(model);
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceSubpackagePayment item = fiveFinanceSubpackagePaymentMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);
        List<FiveFinanceSubpackagePaymentDetail> materialPurchaseDetails = fiveFinanceSubpackagePaymentDetailMapper.selectAll (map);
        data.put("materialPurchaseDetails",materialPurchaseDetails);
/*        Double sum=0.0d;
        for (FiveFinanceTravelExpenseDetail detail:materialPurchaseDetails) {
            sum+=Double.valueOf(detail.getBookNumber());
        }
        data.put("sum",sum);*/

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }


    public String getReceiptsNumber(int id){
        try{
            FiveFinanceSubpackagePayment item = fiveFinanceSubpackagePaymentMapper.selectByPrimaryKey(id);
            FiveFinanceSubpackagePaymentDto subpackagePaymentDto =getModelById(id);
            String newReceiptsNumber ="";

            //单位代码
            String deptCode="S";

            //日期代码
            String date=MyDateUtil.dateToStr1(item.getGmtCreate());

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            // params.put("projectNoHead",year+deptCode+typeCode);
            List<FiveFinanceSubpackagePayment> fiveFinanceSubpackagePayments = fiveFinanceSubpackagePaymentMapper.selectAll(params);
            int size=0;
            //判断顺序代码最大值
            if (!fiveFinanceSubpackagePayments.isEmpty()){
                for (FiveFinanceSubpackagePayment fiveFinanceSubpackagePayment:fiveFinanceSubpackagePayments){
                    String receiptsNumber = fiveFinanceSubpackagePayment.getReceiptsNumber();
                    if(fiveFinanceSubpackagePayment.getId()!=id&&StringUtils.isNotEmpty(receiptsNumber)){
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
            subpackagePaymentDto.setReceiptsNumber(newReceiptsNumber);
            update(subpackagePaymentDto);
            return newReceiptsNumber;

        }catch (Exception e){
            Assert.state(false,"请准确填写，预计开始时间、xxx、xxx！");
            return "";
        }
    }
}
