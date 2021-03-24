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
import com.cmcu.mcc.five.dao.FiveOaGoAbroadTrainAskMapper;
import com.cmcu.mcc.five.dao.FiveOaGoAbroadTrainDeclareMapper;
import com.cmcu.mcc.five.dto.FiveOaGoAbroadTrainDeclareDto;
import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainAsk;
import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainDeclare;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
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
public class FiveOaGoAbroadTrainDeclareService extends BaseService {

    @Resource
    FiveOaGoAbroadTrainDeclareMapper fiveOaGoAbroadTrainDeclareMapper;
    @Resource
    FiveOaGoAbroadTrainAskMapper fiveOaGoAbroadTrainAskMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;


   public void remove(int id,String userLogin){
       FiveOaGoAbroadTrainDeclare item = fiveOaGoAbroadTrainDeclareMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaGoAbroadTrainDeclareDto item){

       FiveOaGoAbroadTrainDeclare model = fiveOaGoAbroadTrainDeclareMapper.selectByPrimaryKey(item.getId());

       model.setFormNo(item.getFormNo());
       model.setDeclareTime(item.getDeclareTime());
       model.setNoticName(item.getNoticName());
       model.setApplyDeptName(item.getApplyDeptName());
       model.setApplyDeptId(item.getApplyDeptId());
       model.setTrainDeptId(item.getTrainDeptId());
       model.setTrainDeptName(item.getTrainDeptName());
       model.setOtherNotic(item.getOtherNotic());
       model.setTrainName(item.getTrainName());
       model.setAttendMan(item.getAttendMan());
       model.setAttendManName(item.getAttendManName());
       model.setAttendReason(item.getAttendReason());
       model.setDeptComment(item.getDeptComment());
       model.setTechnologyComment(item.getTechnologyComment());
       model.setRemark(item.getRemark());
       model.setGmtModified(new Date());


       // BeanValidator.check(model);
       Map variables = Maps.newHashMap();
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getApplyDeptId()));
       if("国外培训".contains(model.getDeclareTime())){
           variables.put("flag", 1);

       }else{
           variables.put("flag", 0);
       }
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);

       variables.put("processDescription","出国培训申报表:"+item.getNoticName());
       myActService.setVariables(model.getProcessInstanceId(),variables);
       BeanValidator.check(model);
       fiveOaGoAbroadTrainDeclareMapper.updateByPrimaryKey(model);
   }


    public FiveOaGoAbroadTrainDeclareDto getModelById(int id){

           return getDto(fiveOaGoAbroadTrainDeclareMapper.selectByPrimaryKey(id));
    }

    public FiveOaGoAbroadTrainDeclareDto getDto(FiveOaGoAbroadTrainDeclare item) {
        FiveOaGoAbroadTrainDeclareDto dto=FiveOaGoAbroadTrainDeclareDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaGoAbroadTrainDeclareMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }
        dto.setBusinessKey(EdConst.FIVE_OA_DEPATRMENTPOST+ "_" + item.getId());
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaGoAbroadTrainDeclare item=new FiveOaGoAbroadTrainDeclare();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setApplyDeptId(hrEmployeeDto.getDeptId());
       item.setApplyDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());
       item.setDeclareTime(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"培训类型").toString());

       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaGoAbroadTrainDeclareMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_GOABROADTRAINDECLARE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","出国培训申报表:"+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getApplyDeptId()));
       variables.put("qualityDeptChargeMen",selectEmployeeService.getDeptChargeMen(11));//信息化建设与管理部负责人

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_GOABROADTRAINDECLARE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaGoAbroadTrainDeclareMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaGoAbroadTrainDeclareMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaGoAbroadTrainDeclare item=(FiveOaGoAbroadTrainDeclare)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaGoAbroadTrainDeclare item = fiveOaGoAbroadTrainDeclareMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("noticName",item.getNoticName());
        data.put("declareTime",item.getDeclareTime());
        data.put("applyDeptName",item.getApplyDeptName());
        data.put("trainDeptName",item.getTrainDeptName());
        data.put("trainName",item.getTrainName());
        data.put("otherNotic",item.getOtherNotic());
        data.put("attendManName",item.getAttendManName());
        data.put("attendReason",item.getAttendReason());
        data.put("deptComment",item.getDeptComment());
        data.put("technologyComment",item.getTechnologyComment());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("信息化建设与管理部负责人".equals(dto.getActivityName())){
                data.put("informationEquipmentMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }


    public void saveAssociation(int applyId, int id) {
        FiveOaGoAbroadTrainAsk fiveOaGoAbroadTrainAsk=fiveOaGoAbroadTrainAskMapper.selectByPrimaryKey(applyId);
        FiveOaGoAbroadTrainDeclare fiveOaGoAbroadTrainDeclare=fiveOaGoAbroadTrainDeclareMapper.selectByPrimaryKey(id);
        fiveOaGoAbroadTrainDeclare.setTrainName(fiveOaGoAbroadTrainAsk.getTrainName());

       fiveOaGoAbroadTrainDeclareMapper.updateByPrimaryKey(fiveOaGoAbroadTrainDeclare);
    }
}
