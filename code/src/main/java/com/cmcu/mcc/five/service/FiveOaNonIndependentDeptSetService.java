package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaNonIndependentDeptSetMapper;
import com.cmcu.mcc.five.dto.FiveOaNonIndependentDeptSetDto;
import com.cmcu.mcc.five.entity.FiveOaNonIndependentDeptSet;
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
public class FiveOaNonIndependentDeptSetService extends BaseService {

    @Resource
    FiveOaNonIndependentDeptSetMapper fiveOaNonIndependentDeptSetMapper;
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

    @Autowired
    CommonFileService commonFileService;
    @Resource
    HandleFormService handleFormService;


   public void remove(int id,String userLogin){
       FiveOaNonIndependentDeptSet item = fiveOaNonIndependentDeptSetMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaNonIndependentDeptSetDto item){

       FiveOaNonIndependentDeptSet model = fiveOaNonIndependentDeptSetMapper.selectByPrimaryKey(item.getId());

       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setFormNo(item.getFormNo());
       model.setCenterName(item.getCenterName());
       model.setResearchDirection(item.getResearchDirection());
       model.setLinkMan(item.getLinkMan());
       model.setLinkManName(item.getLinkManName());
       model.setLinkManPhone(item.getLinkManPhone());
       model.setDeptChargeMan(item.getDeptChargeMan());
       model.setDeptChargeManName(item.getDeptChargeManName());
       model.setDeptLeaderComment(item.getDeptLeaderComment());
       model.setSpecialistComment(item.getSpecialistComment());
       model.setTechnologyDeptMan(item.getTechnologyDeptMan());
       model.setTechnologyDeptManName(item.getTechnologyDeptManName());
       model.setTechnologyDeptComment(item.getTechnologyDeptComment());

       model.setRemark(item.getRemark());
       model.setGmtModified(new Date());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
      if (item.getDeptId()!=model.getId()){
          variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
          model.setDeptName(item.getDeptName());
          model.setDeptId(item.getDeptId());
      }
       fiveOaNonIndependentDeptSetMapper.updateByPrimaryKey(model);
       variables.put("processDescription","非独立运行中心设立申请表:"+item.getCenterName());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaNonIndependentDeptSetDto getModelById(int id){

           return getDto(fiveOaNonIndependentDeptSetMapper.selectByPrimaryKey(id));
    }

    public FiveOaNonIndependentDeptSetDto getDto(FiveOaNonIndependentDeptSet item) {
        FiveOaNonIndependentDeptSetDto dto=FiveOaNonIndependentDeptSetDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaNonIndependentDeptSetMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaNonIndependentDeptSet item=new FiveOaNonIndependentDeptSet();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaNonIndependentDeptSetMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_NONINDEPENDENTDEPTSET+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","非独立运行中心设立申请表:"+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
       variables.put("qualityDeptChargeMen",selectEmployeeService.getDeptChargeMen(11));//信息化建设与管理部负责人

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_NONINDEPENDENTDEPTSET,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaNonIndependentDeptSetMapper.updateByPrimaryKey(item);
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
        /* List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaNonIndependentDeptSetMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaNonIndependentDeptSet item=(FiveOaNonIndependentDeptSet)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaNonIndependentDeptSet item = fiveOaNonIndependentDeptSetMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("centerName",item.getCenterName());
        data.put("deptName",item.getDeptName());
        data.put("researchDirection",item.getResearchDirection());
        data.put("linkManName",item.getLinkManName());
        data.put("linkManPhone",item.getLinkManPhone());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<CommonFileDto> commonFileDtos = commonFileService.listData(item.getBusinessKey(),0,null);
        data.put("fileList",commonFileDtos);


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
            if ("专家-审批".equals(dto.getActivityName())){
                data.put("specialist",dto);
            }
        }
        data.put("nodes",actHistoryDtos);data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }


}



