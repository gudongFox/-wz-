package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaInventPaymentDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaInventPaymentMapper;
import com.cmcu.mcc.five.dto.FiveOaInventPaymentDto;
import com.cmcu.mcc.five.entity.FiveOaInventPayment;
import com.cmcu.mcc.five.entity.FiveOaInventPaymentDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaInventPaymentService extends BaseService {

    @Resource
    FiveOaInventPaymentMapper fiveOaInventPaymentMapper;
    @Resource
    FiveOaInventPaymentDetailMapper fiveOaInventPaymentDetailMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Autowired
    MyActService myActService;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    CommonCodeService commonCodeService;

    public void remove(int id,String userLogin){
        FiveOaInventPayment item = fiveOaInventPaymentMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }
    public void update(FiveOaInventPaymentDto fiveOaInventPaymentDto){
        FiveOaInventPayment model = fiveOaInventPaymentMapper.selectByPrimaryKey(fiveOaInventPaymentDto.getId());
        model.setDeptName(fiveOaInventPaymentDto.getDeptName());
        model.setDeptId(fiveOaInventPaymentDto.getDeptId());
        model.setGmtModified(new Date());
        model.setPaymentTime(fiveOaInventPaymentDto.getPaymentTime());
        model.setRemark(fiveOaInventPaymentDto.getRemark());
        BeanValidator.check(model);
        fiveOaInventPaymentMapper.updateByPrimaryKey(model);
    }

    public FiveOaInventPaymentDto getModelById(int id){

        return getDto(fiveOaInventPaymentMapper.selectByPrimaryKey(id));
    }

    public FiveOaInventPaymentDto getDto(FiveOaInventPayment item) {
        FiveOaInventPaymentDto dto=FiveOaInventPaymentDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaInventPaymentMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }
    public int getNewModel(String userLogin){
        FiveOaInventPayment item=new FiveOaInventPayment();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaInventPaymentMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_INVENT_PAYMENT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "专利项目缴费申请："+item.getDeptName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INVENT_PAYMENT,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaInventPaymentMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInventPaymentMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInventPayment item=(FiveOaInventPayment)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaInventPaymentDetail model = fiveOaInventPaymentDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaInventPaymentDetailMapper.updateByPrimaryKey(model);
    }
    public void updateDetail(FiveOaInventPaymentDetail fiveOaInventPaymentDetail){
        FiveOaInventPaymentDetail model = fiveOaInventPaymentDetailMapper.selectByPrimaryKey(fiveOaInventPaymentDetail.getId());
        model.setInventNo(fiveOaInventPaymentDetail.getInventNo());
        model.setInventName(fiveOaInventPaymentDetail.getInventName());
        model.setKeepGiveUp(fiveOaInventPaymentDetail.getKeepGiveUp());
        model.setReason(fiveOaInventPaymentDetail.getReason());
        model.setGmtModified(new Date());
        model.setYear(fiveOaInventPaymentDetail.getYear());
        model.setMoney(fiveOaInventPaymentDetail.getMoney());
        ModelUtil.setNotNullFields(model);
        fiveOaInventPaymentDetailMapper.updateByPrimaryKey(model);
    }
    public int getNewModelDetail(int paymentId){
        FiveOaInventPaymentDetail item=new FiveOaInventPaymentDetail();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        item.setPaymentId(paymentId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setKeepGiveUp(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"专利缴费意见").toString());
        item.setYear(year);
        ModelUtil.setNotNullFields(item);
        fiveOaInventPaymentDetailMapper.insert(item);
        return item.getId();

    }

    public FiveOaInventPaymentDetail getModelDetailById(int id){
        return fiveOaInventPaymentDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaInventPaymentDetail> listDetail(int paymentId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("paymentId",paymentId);
        List<FiveOaInventPaymentDetail> list = fiveOaInventPaymentDetailMapper.selectAll(params);
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaInventPayment item = fiveOaInventPaymentMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("paymentTime",item.getPaymentTime());
        data.put("remark",item.getRemark());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("paymentId",item.getId());
        map.put("deleted",false);
        List<FiveOaInventPaymentDetail> paymentDetails = fiveOaInventPaymentDetailMapper.selectAll (map);
        data.put("paymentDetails",paymentDetails);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("专家委-审批".equals(dto.getActivityName())){
                data.put("expertCommittee",dto);
            }
            if ("于万河-审批".equals(dto.getActivityName())){
                data.put("chiefEngineer",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}
