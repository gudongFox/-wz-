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
import com.cmcu.mcc.finance.dao.FiveFinanceRefundDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceRefundMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceRefundDto;
import com.cmcu.mcc.finance.entity.FiveFinanceRefund;
import com.cmcu.mcc.finance.entity.FiveFinanceRefundDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FiveFinanceRefundService {
    @Resource
    FiveFinanceRefundMapper fiveFinanceRefundMapper;
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
    FiveFinanceRefundDetailMapper fiveFinanceRefundDetailMapper;
    @Resource
    ProcessQueryService processQueryService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveFinanceRefund item = fiveFinanceRefundMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceRefundDto fiveFinanceRefundDto){
        FiveFinanceRefund model = getModelById(fiveFinanceRefundDto.getId());
        model.setReceiptsNumber(fiveFinanceRefundDto.getReceiptsNumber());
        model.setReceiptsState(fiveFinanceRefundDto.getReceiptsState());
        model.setUnit(fiveFinanceRefundDto.getUnit());
        model.setProjectId(fiveFinanceRefundDto.getProjectId());
        model.setProjectName(fiveFinanceRefundDto.getProjectName());
        model.setApplicantTime(fiveFinanceRefundDto.getApplicantTime());
        model.setReceiptsNumber(fiveFinanceRefundDto.getReceiptsNumber());
        model.setApplicant(fiveFinanceRefundDto.getApplicant());
        model.setApplicantName(fiveFinanceRefundDto.getApplicantName());
        model.setDeptId(fiveFinanceRefundDto.getDeptId());
        model.setDeptName(fiveFinanceRefundDto.getDeptName());
        model.setRemark(fiveFinanceRefundDto.getRemark());
        model.setGmtModified(new Date());
        model.setPayName(fiveFinanceRefundDto.getPayName());
        model.setPayId(fiveFinanceRefundDto.getPayId());
        model.setPayDept(fiveFinanceRefundDto.getPayDept());
        model.setPayAccount(fiveFinanceRefundDto.getPayAccount());
        model.setReceiveName(fiveFinanceRefundDto.getReceiveName());
        model.setReceiveId(fiveFinanceRefundDto.getReceiveId());
        model.setReceiveDept(fiveFinanceRefundDto.getReceiveDept());
        model.setReceiveBank(fiveFinanceRefundDto.getReceiveBank());
        model.setReceiveAccount(fiveFinanceRefundDto.getReceiveAccount());
        model.setTotalRefundMoney(fiveFinanceRefundDto.getTotalRefundMoney());
        model.setTitle(fiveFinanceRefundDto.getTitle());


        fiveFinanceRefundMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
/*        variables.put("train", fiveFinanceRefundDto.getRefund().equalsIgnoreCase("是"));
        variables.put("scientific",false);
        variables.put("other",fiveFinanceRefundDto.getRefund().equalsIgnoreCase("否"));
        variables.put("money", fiveFinanceRefundDto.getCount());*/

        myActService.setVariables(model.getProcessInstanceId(),variables);
    }


    public FiveFinanceRefundDto getModelById(int id){

        return getDto(fiveFinanceRefundMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceRefundDto getDto(FiveFinanceRefund item) {
        FiveFinanceRefundDto dto=FiveFinanceRefundDto.adapt(item);
        CustomSimpleProcessInstance customSimpleProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
        dto.setProcessName(customSimpleProcessInstance.getCurrentTaskName());
        if (!item.getProcessEnd() && customSimpleProcessInstance.isFinished()) {
            dto.setProcessEnd(true);
            fiveFinanceRefundMapper.updateByPrimaryKey(dto);
        }
        if (StringUtils.isEmpty(customSimpleProcessInstance.getCurrentTaskName())) {
            dto.setProcessName("流程已结束");
        }
        String returnMoney="0";
        List<FiveFinanceRefundDetail> detailList=listDetail(item.getId());
        for(FiveFinanceRefundDetail detail:detailList){
            returnMoney=MyStringUtil.getNewAddMoney(returnMoney,detail.getReturnMoney());
        }
        dto.setTotalRefundMoney(returnMoney);

        return dto;
    }

    public int getNewModel(String userLogin){
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
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setApplicantTime(MyDateUtil.getStringToday());

        ModelUtil.setNotNullFields(item);
        fiveFinanceRefundMapper.insert(item);
        String title = "还款:" + item.getDeptName();

        String businessKey= EdConst.FIVE_FINANCE_REFUND+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription",title);
        //是否补充还款
        variables.put("replenishRefund",false);

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_REFUND,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setTitle(title);
        fiveFinanceRefundMapper.updateByPrimaryKey(item);
        return item.getId();
    }
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceRefundMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceRefund item=(FiveFinanceRefund)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void updateDetail(FiveFinanceRefundDetail item){
        FiveFinanceRefundDetail model = fiveFinanceRefundDetailMapper.selectByPrimaryKey(item.getId());
        model.setRefundMan(item.getRefundMan());
        model.setRefundManName(item.getRefundManName());
        model.setRefundReceipts(item.getRefundReceipts());
        model.setRefundMoney(item.getRefundMoney());
        model.setReturnMoney(MyStringUtil.moneyToString(item.getReturnMoney()));
        model.setRemark(item.getRemark());
        fiveFinanceRefundDetailMapper.updateByPrimaryKey(model);
    }

    public FiveFinanceRefundDetail getNewModelDetail(int  id){
        FiveFinanceRefundDetail item=new FiveFinanceRefundDetail();
        item.setRefundId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setRefundMoney(MyStringUtil.moneyToString("0"));
        item.setReturnMoney(MyStringUtil.moneyToString("0"));
        ModelUtil.setNotNullFields(item);
        fiveFinanceRefundDetailMapper.insert(item);
        return item;
    }

    public FiveFinanceRefundDetail getModelDetailById(int id){
        return fiveFinanceRefundDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveFinanceRefundDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("refundId",id);
        List<FiveFinanceRefundDetail> list = fiveFinanceRefundDetailMapper.selectAll(params);
        return list;
    }
    public void removeDetail(int id){
        FiveFinanceRefundDetail model = fiveFinanceRefundDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceRefundDetailMapper.updateByPrimaryKey(model);
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceRefund item = fiveFinanceRefundMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());
        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);
        List<FiveFinanceRefundDetail> materialPurchaseDetails = fiveFinanceRefundDetailMapper.selectAll (map);
        data.put("materialPurchaseDetails",materialPurchaseDetails);
/*        Double sum=0.0d;
        for (FiveFinanceRefundDetail detail:materialPurchaseDetails) {
            sum+=Double.valueOf(detail.getBookNumber());
        }
        data.put("sum",sum);*/

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

    public List<FiveFinanceRefund> listRefundByUserLogin(String userLogin) {
        HrEmployeeSysDto userDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("deptId", userDto.getDeptId());//自己部门下的还款款流程
        List<FiveFinanceRefund> fiveFinanceRefunds = fiveFinanceRefundMapper.selectAll(params)
                .stream().filter(p->p.getReimburseId()==0).filter(p->p.getTravelId()==0).collect(Collectors.toList());
        return fiveFinanceRefunds;
    }
    public List<FiveFinanceRefund> listRefundByDeptId(String userLogin,int deptId) {
        HrEmployeeSysDto userDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("deptId", deptId);
        List<FiveFinanceRefund> fiveFinanceRefunds = fiveFinanceRefundMapper.selectAll(params)
                .stream().filter(p->p.getReimburseId()==0).filter(p->p.getTravelId()==0).collect(Collectors.toList());
        return fiveFinanceRefunds;
    }

}
