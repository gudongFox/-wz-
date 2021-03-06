package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaInlandProjectApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaInlandProjectApplyDto;
import com.cmcu.mcc.five.entity.FiveOaExternalResearchProjectApply;
import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
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
public class FiveOaInlandProjectApplyService extends BaseService {

    @Resource
    FiveOaInlandProjectApplyMapper fiveOaInlandProjectApplyMapper;
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
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveOaResearchProjectLibraryService fiveOaResearchProjectLibraryService;

    public void remove(int id,String userLogin){
        FiveOaInlandProjectApply item = fiveOaInlandProjectApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaInlandProjectApplyDto item){

        FiveOaInlandProjectApply model = fiveOaInlandProjectApplyMapper.selectByPrimaryKey(item.getId());
        model.setProjectName(item.getProjectName());
        model.setProjectContent(item.getProjectContent());
        model.setProjectType(item.getProjectType());
        model.setAchievement(item.getAchievement());
        model.setStartTime(item.getStartTime());
        model.setEndTime(item.getEndTime());
        model.setTotalPrice(item.getTotalPrice());
        model.setChargeMen(item.getChargeMen());
        model.setChargeMenName(item.getChargeMenName());
        model.setAttender(item.getAttender());
        model.setAttenderName(item.getAttenderName());
        model.setMaterialsCost(item.getMaterialsCost());
        model.setAppropriativeCost(item.getAppropriativeCost());
        model.setOutsourceCost(item.getOutsourceCost());
        model.setMeetingCost(item.getMeetingCost());
        model.setTravelCost(item.getTravelCost());
        model.setSpecialistCost(item.getSpecialistCost());
        model.setFixeAssetDepreciationCost(item.getFixeAssetDepreciationCost());
        model.setFuelPowerCost(item.getFuelPowerCost());
        model.setSalaryServiceCost(item.getSalaryServiceCost());
        model.setScientificFirstTrial(item.getScientificFirstTrial());
        model.setScientificFirstTrialName(item.getScientificFirstTrialName());
        model.setRemark(item.getRemark());
        model.setSecret(item.getSecret());

        model.setGmtModified(new Date());
        model.setDeptName(item.getDeptName());
        model.setDeptId(item.getDeptId());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();

        fiveOaInlandProjectApplyMapper.updateByPrimaryKey(model);
        //?????????????????? ??????????????? ???????????????????????????
        List<String> chiefEngineer = selectEmployeeService.listUserByPositionName("????????????", item.getDeptId());
        if (chiefEngineer.size()>0){
            variables.put("chiefEngineer",chiefEngineer);//????????????
        }else {
            variables.put("chiefEngineer",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),3,false));
        }

        variables.put("processDescription","??????????????????"+item.getProjectName());

        variables.put("deptScientificMen", selectEmployeeService.getDeptRoleUser(model.getDeptId(),"?????????????????????"));
        //????????????
        variables.put("flag",false);
        if (model.getProjectType().equals("?????????")){
            variables.put("flag",true);
            variables.put("expertCommitteeList", MyStringUtil.getStringList(model.getScientificFirstTrial()));//?????????????????????
        }

        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);


    }

    public FiveOaInlandProjectApplyDto getModelById(int id){

        return getDto(fiveOaInlandProjectApplyMapper.selectByPrimaryKey(id));
    }

    public FiveOaInlandProjectApplyDto getDto(FiveOaInlandProjectApply item) {

        item.setMaterialsCost(MyStringUtil.moneyToString(item.getMaterialsCost(),6));
        item.setAppropriativeCost(MyStringUtil.moneyToString(item.getAppropriativeCost(),6));
        item.setOutsourceCost(MyStringUtil.moneyToString(item.getOutsourceCost(),6));
        item.setMeetingCost(MyStringUtil.moneyToString(item.getMeetingCost(),6));
        item.setTravelCost(MyStringUtil.moneyToString(item.getTravelCost(),6));
        item.setSpecialistCost(MyStringUtil.moneyToString(item.getSpecialistCost(),6));
        item.setFuelPowerCost(MyStringUtil.moneyToString(item.getFuelPowerCost(),6));
        item.setFixeAssetDepreciationCost(MyStringUtil.moneyToString(item.getFixeAssetDepreciationCost(),6));
        item.setSalaryServiceCost(MyStringUtil.moneyToString(item.getSalaryServiceCost(),6));

        FiveOaInlandProjectApplyDto dto=FiveOaInlandProjectApplyDto.adapt(item);
        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()){
           CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
               dto.setProcessEnd(true);
               fiveOaInlandProjectApplyMapper.updateByPrimaryKey(dto);
              fiveOaResearchProjectLibraryService.genLibraryByInlandProject(dto);
           }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
               dto.setProcessName(customProcessInstance.getCurrentTaskName());
           }
       }

        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaInlandProjectApply item=new FiveOaInlandProjectApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());


        item.setAppropriativeCost("0");
        item.setOutsourceCost("0");
        item.setMaterialsCost("0");
        item.setMeetingCost("0");
        item.setTravelCost("0");
        item.setSpecialistCost("0");
        item.setFixeAssetDepreciationCost("0");
        item.setFuelPowerCost("0");
        item.setSalaryServiceCost("0");



        item.setTotalPrice("0");
        item.setSpecialistCost("0");

        item.setSecret(false);


        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);


        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);




        variables.put("scientificMen",selectEmployeeService.getUserListByRoleName("????????????????????????(?????????)"));//??????????????????????????????????????????
        variables.put("scientificFirstTrial",selectEmployeeService.getUserListByRoleName("????????????????????????(????????????)"));//???????????????????????????????????????)


        fiveOaInlandProjectApplyMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_INLAND_PROJECT_APPLY+ "_" + item.getId();

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INLAND_PROJECT_APPLY,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaInlandProjectApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInlandProjectApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInlandProjectApply item=(FiveOaInlandProjectApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * ??????excel
     * @param startTime1 ????????????
     * @param endTime1 ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
        List<Map> list = new ArrayList<>();
        //??????????????????
        params.put("isLikeSelect",true);
        //??????????????????
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//???????????????userLogin ??????????????????
        //????????? ???????????????
        params.put("deleted",false);
        params.put("processEnd",true);
        //???????????????
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);


        List<FiveOaInlandProjectApply> fiveOaInlandProjectApplies=fiveOaInlandProjectApplyMapper.selectAll(params);
        for (FiveOaInlandProjectApply dto:fiveOaInlandProjectApplies){
            Map map1=new LinkedHashMap();
            map1.put("????????????",dto.getProjectName());
            map1.put("????????????",dto.getDeptName());
            map1.put("????????????",dto.getStartTime());
            map1.put("????????????",dto.getEndTime());
            map1.put("???????????????",dto.getChargeMenName() );
            map1.put("???????????????", dto.getAttenderName());
            map1.put("????????????",dto.getProjectType() );
            map1.put("??????????????????",dto.getProjectContent() );
            map1.put("???????????????????????????", dto.getAchievement());
            map1.put("??????",dto.getTotalPrice());
            map1.put("?????????",dto.getMaterialsCost());
            map1.put("?????????",dto.getAppropriativeCost());
            map1.put("?????????",dto.getOutsourceCost());
            map1.put("?????????-?????????",dto.getMeetingCost());
            map1.put("?????????-?????????",dto.getTravelCost());
            map1.put("?????????-???????????????",dto.getSpecialistCost());
            map1.put("?????????????????????",dto.getFixeAssetDepreciationCost());
            map1.put("???????????????",dto.getFuelPowerCost());
            map1.put("??????????????????",dto.getSalaryServiceCost());
            map1.put("??????",dto.getRemark());
            list.add(map1);
        }
        return list;
    }


}
