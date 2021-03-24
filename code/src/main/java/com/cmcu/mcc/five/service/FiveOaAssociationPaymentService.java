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
import com.cmcu.mcc.five.dao.FiveOaAssociationApplyMapper;
import com.cmcu.mcc.five.dao.FiveOaAssociationPaymentMapper;
import com.cmcu.mcc.five.dto.FiveOaAssociationPaymentDto;
import com.cmcu.mcc.five.entity.FiveOaAssociationApply;
import com.cmcu.mcc.five.entity.FiveOaAssociationPayment;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaAssociationPaymentService extends BaseService {

    @Resource
    FiveOaAssociationPaymentMapper fiveOaAssociationPaymentMapper;
    @Resource
    FiveOaAssociationApplyMapper fiveOaAssociationApplyMapper;
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
        FiveOaAssociationPayment item = fiveOaAssociationPaymentMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }
    public void update(FiveOaAssociationPaymentDto fiveOaAssociationPaymentDto){
        FiveOaAssociationPayment model = fiveOaAssociationPaymentMapper.selectByPrimaryKey(fiveOaAssociationPaymentDto.getId());
        model.setApplyId(fiveOaAssociationPaymentDto.getApplyId());
        model.setAssociationRole(fiveOaAssociationPaymentDto.getAssociationRole());
        model.setDeptId(fiveOaAssociationPaymentDto.getDeptId());
        model.setDeptName(fiveOaAssociationPaymentDto.getDeptName());
        model.setHandleMan(fiveOaAssociationPaymentDto.getHandleMan());
        model.setHandleManName(fiveOaAssociationPaymentDto.getHandleManName());
        model.setAssociationName(fiveOaAssociationPaymentDto.getAssociationName());
        model.setDeptChargeName(fiveOaAssociationPaymentDto.getDeptChargeName());
        model.setAssociationLevel(fiveOaAssociationPaymentDto.getAssociationLevel());
        model.setLinkMan(fiveOaAssociationPaymentDto.getLinkMan());
        model.setAssociationFee(fiveOaAssociationPaymentDto.getAssociationFee());
        model.setRemark(fiveOaAssociationPaymentDto.getRemark());
        model.setPaymentMoney(fiveOaAssociationPaymentDto.getPaymentMoney());
        model.setGmtModified(new Date());
        Map variables = Maps.newHashMap();
        if ("二类".equals(model.getAssociationLevel())){
            variables.put("flag", 0);
        }else {
            variables.put("flag", 1);
        }
        variables.put("processDescription", "协会缴费申请:"+model.getAssociationName());

        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaAssociationPaymentMapper.updateByPrimaryKey(model);
    }

    public FiveOaAssociationPaymentDto getModelById(int id){

        return getDto(fiveOaAssociationPaymentMapper.selectByPrimaryKey(id));
    }

    public FiveOaAssociationPaymentDto getDto(FiveOaAssociationPayment item) {
        FiveOaAssociationPaymentDto dto=FiveOaAssociationPaymentDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                item.setProcessEnd(true);
                fiveOaAssociationPaymentMapper.updateByPrimaryKey(item);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaAssociationPayment item=new FiveOaAssociationPayment();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);


        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setAssociationRole(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"协会角色").toString());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaAssociationPaymentMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_ASSOCIATION_PAYMENT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "协会缴费申请:"+item.getCreatorName());
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_ASSOCIATION_PAYMENT,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaAssociationPaymentMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);//模糊查询

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));


        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaAssociationPaymentMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaAssociationPayment item=(FiveOaAssociationPayment)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void saveAssociation(int applyId,int id) {
        FiveOaAssociationApply apply = fiveOaAssociationApplyMapper.selectByPrimaryKey(applyId);
        FiveOaAssociationPayment change = fiveOaAssociationPaymentMapper.selectByPrimaryKey(id);
        change.setAssociationRole(apply.getAssociationRole());
        change.setApplyId(apply.getId());
        change.setDeptId(apply.getDeptId());
        change.setDeptName(apply.getDeptName());
        change.setHandleMan(apply.getHandleMan());
        change.setHandleManName(apply.getHandleManName());
        change.setAssociationName(apply.getAssociationName());
        change.setAssociationLevel(apply.getAssociationLevel());
        change.setDeptChargeName(apply.getDeptChargeName());
        change.setLinkMan(apply.getLinkMan());
        change.setAssociationFee(apply.getAssociationFee());
        change.setRecommendMan(apply.getRecommendManName());
        fiveOaAssociationPaymentMapper.updateByPrimaryKey(change);
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaAssociationPayment item = fiveOaAssociationPaymentMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("handleManName",item.getHandleManName());
        data.put("associationName",item.getAssociationName());
        data.put("deptChargeName",item.getDeptChargeName());
        data.put("recommendMan",item.getRecommendMan());
        data.put("associationRole",item.getAssociationRole());
        data.put("linkMan",item.getLinkMan());
        data.put("associationFee",item.getAssociationFee());
        data.put("paymentMoney",item.getPaymentMoney());

        data.put("creatorName",item.getCreatorName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        data.put("gmtCreate",simpleDateFormat.format(item.getGmtCreate()));

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if(dto.getActivityName()==null){
                break;
            }
            if ("信息化建设与管理部（副部长）".equals(dto.getActivityName())){
                data.put("informationViceOffice",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}
