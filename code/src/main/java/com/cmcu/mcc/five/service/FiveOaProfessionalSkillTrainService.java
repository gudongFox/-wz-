package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaProfessionalSkillTrainMapper;
import com.cmcu.mcc.five.dto.FiveOaProfessionalSkillTrainDto;
import com.cmcu.mcc.five.entity.FiveOaProfessionalSkillTrain;
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
public class FiveOaProfessionalSkillTrainService extends BaseService {

    @Resource
    FiveOaProfessionalSkillTrainMapper fiveOaProfessionalSkillTrainMapper;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;

   public void remove(int id,String userLogin){
       FiveOaProfessionalSkillTrain item = fiveOaProfessionalSkillTrainMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaProfessionalSkillTrainDto item){

       FiveOaProfessionalSkillTrain model = fiveOaProfessionalSkillTrainMapper.selectByPrimaryKey(item.getId());

       model.setFormNo(item.getFormNo());
       model.setTrainTime(item.getTrainTime());
       model.setTrainType(item.getTrainType());
       model.setApplyMan(item.getApplyMan());
       model.setApplyManName(item.getApplyManName());
       model.setTrainAddress(item.getTrainAddress());
       model.setTrainContent(item.getTrainContent());
       model.setTrainPrice(item.getTrainPrice());
       model.setDeptComment(item.getDeptComment());
       model.setTechnologyComment(item.getTechnologyComment());
       model.setPlan(item.getPlan());

       model.setRemark(item.getRemark());
       model.setGmtModified(new Date());
      // BeanValidator.check(model);
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);

       Map variables = Maps.newHashMap();
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getApplyDeptId()));
       model.setApplyDeptName(item.getApplyDeptName());
       model.setApplyDeptId(item.getApplyDeptId());
       BeanValidator.check(model);
       fiveOaProfessionalSkillTrainMapper.updateByPrimaryKey(model);
       variables.put("processDescription","专业技术培训申请表:"+item.getTrainContent());
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }

    public FiveOaProfessionalSkillTrainDto getModelById(int id){

           return getDto(fiveOaProfessionalSkillTrainMapper.selectByPrimaryKey(id));
    }

    public FiveOaProfessionalSkillTrainDto getDto(FiveOaProfessionalSkillTrain item) {
        FiveOaProfessionalSkillTrainDto dto = FiveOaProfessionalSkillTrainDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaProfessionalSkillTrainMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaProfessionalSkillTrain item=new FiveOaProfessionalSkillTrain();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setApplyDeptName(hrEmployeeDto.getUserName());
       item.setApplyDeptId(hrEmployeeDto.getDeptId());
       item.setApplyDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       item.setPlan(true);
       ModelUtil.setNotNullFields(item);
       fiveOaProfessionalSkillTrainMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_PROFESSIONALSKILLTRAIN+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","专业技术培训申请表:"+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getApplyDeptId()));
       variables.put("otherDeptChargeMen",selectEmployeeService.getDeptChargeMen(item.getApplyDeptId()));//部门分管领导
       variables.put("qualityDeptChargeMen",selectEmployeeService.getDeptChargeMen(11));//信息化建设与管理部负责人
       variables.put("hrDeptChargeMen",selectEmployeeService.selectUserByPositionNameAndDeptId("副部长",38));//信息化建设与管理部（副部长）

       variables.put("hrDeptTrainUser",selectEmployeeService.getUserListByRoleName("人力资源部人员(培训)"));//人力资源部人员（培训）
       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_PROFESSIONALSKILLTRAIN,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaProfessionalSkillTrainMapper.updateByPrimaryKey(item);
       return item.getId();
   }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

       /* List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));


        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaProfessionalSkillTrainMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaProfessionalSkillTrain item=(FiveOaProfessionalSkillTrain)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaProfessionalSkillTrain item = fiveOaProfessionalSkillTrainMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("applyDeptName",item.getApplyDeptName());
        data.put("trainTime",item.getTrainTime());
        data.put("applyManName",item.getApplyManName());
        data.put("trainType",item.getTrainType());
        data.put("trainAddress",item.getTrainAddress());
        data.put("trainPrice",item.getTrainPrice());
        data.put("trainContent",item.getTrainContent());
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
                data.put("informationEquipmentChargeMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }



}
