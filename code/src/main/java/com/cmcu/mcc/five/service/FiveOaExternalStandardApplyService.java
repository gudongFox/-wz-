package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaExternalStandardApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaExternalStandardApplyDto;
import com.cmcu.mcc.five.entity.FiveOaExternalStandardApply;
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
import java.util.*;
@Service
public class FiveOaExternalStandardApplyService extends BaseService {
    @Resource
    FiveOaExternalStandardApplyMapper fiveOaExternalStandardApplyMapper;

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
    @Autowired
    CommonFileService commonFileService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveOaResearchProjectLibraryService fiveOaResearchProjectLibraryService;


    public void remove(int id,String userLogin) {
        FiveOaExternalStandardApply item = fiveOaExternalStandardApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"???????????????????????????");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaExternalStandardApply fiveOaExternalStandardDto){
        FiveOaExternalStandardApply model = fiveOaExternalStandardApplyMapper.selectByPrimaryKey(fiveOaExternalStandardDto.getId());

        model.setFormNo(fiveOaExternalStandardDto.getFormNo());
        model.setTaskNo(fiveOaExternalStandardDto.getTaskNo());
        model.setTaskName(fiveOaExternalStandardDto.getTaskName());
        model.setAchivement(fiveOaExternalStandardDto.getAchivement());
        model.setMajorResearchContent(fiveOaExternalStandardDto.getMajorResearchContent());
        model.setRemark(fiveOaExternalStandardDto.getRemark());
        model.setCompletionDate(fiveOaExternalStandardDto.getCompletionDate());
        model.setCommencementDate(fiveOaExternalStandardDto.getCommencementDate());
        model.setTaskCharger(fiveOaExternalStandardDto.getTaskCharger());
        model.setTaskChargerName(fiveOaExternalStandardDto.getTaskChargerName());
        model.setMainParticipant(fiveOaExternalStandardDto.getMainParticipant());
        model.setMainParticipantName(fiveOaExternalStandardDto.getMainParticipantName());
        model.setOutsidePayment(fiveOaExternalStandardDto.getOutsidePayment());//?????????????????????
        model.setYearExceptPayment(fiveOaExternalStandardDto.getYearExceptPayment());//?????????????????????????????????

        model.setSecret(fiveOaExternalStandardDto.getSecret());

        model.setScientificFirstTrial(fiveOaExternalStandardDto.getScientificFirstTrial());//?????????
        model.setScientificFirstTrialName(fiveOaExternalStandardDto.getScientificFirstTrialName());
        model.setDeptName(fiveOaExternalStandardDto.getDeptName());
        model.setDeptId(fiveOaExternalStandardDto.getDeptId());
        model.setGmtModified(new Date());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();



        variables.put("deptScientificMen", selectEmployeeService.getDeptRoleUser(model.getDeptId(),"?????????????????????"));
        //?????????????????? ??????????????? ???????????????????????????
        List<String> chiefEngineer = selectEmployeeService.listUserByPositionName("???????????????/?????????", model.getDeptId());
        if (chiefEngineer.size()>0){
            variables.put("chiefEngineer",chiefEngineer);//????????????
        }else {
            variables.put("chiefEngineer",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),3,false));
        }
        fiveOaExternalStandardApplyMapper.updateByPrimaryKey(model);
        variables.put("processDescription","??????????????????????????????????????????:"+model.getTaskName());
        variables.put("expertCommitteeList", MyStringUtil.getStringList(model.getScientificFirstTrial()));//?????????????????????


        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaExternalStandardApplyDto getModelById(int id) {
        return getDto(fiveOaExternalStandardApplyMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin){
        FiveOaExternalStandardApply item= new FiveOaExternalStandardApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());//???????????????????????????
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setSecret(false);
        ModelUtil.setNotNullFields(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "???????????????????????????????????????:"+item.getCreatorName());




        variables.put("scientificMen",selectEmployeeService.getUserListByRoleName("????????????????????????(?????????)"));//??????????????????????????????????????????
        variables.put("scientificFirstTrial",selectEmployeeService.getUserListByRoleName("????????????????????????(????????????)"));//???????????????????????????????????????)

        fiveOaExternalStandardApplyMapper.insert(item);


        String BusinessKey= EdConst.FIVE_OA_EXTERNAL_STANDARD_APPLY+ "_" + item.getId();//BusinessKey?????????????????????+??????
        item.setBusinessKey(BusinessKey);//??????????????????BusinessKey??????

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_EXTERNAL_STANDARD_APPLY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        //????????????id????????????????????????????????????????????????????????????????????????????????????item??????????????????getBusinessKey?????????variables??????

        item.setProcessInstanceId(processInstanceId);//?????????????????????id??????????????????
        //????????????
        fiveOaExternalStandardApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

       /* //2020-12-28 ????????????+1???
        if (params.get("endTime").toString()!=""){
            params.put("endTime",MyDateUtil.getNextDay(params.get("endTime").toString(),"1"));
        }*/
        Map map=Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));




        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaExternalStandardApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaExternalStandardApply item=(FiveOaExternalStandardApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public FiveOaExternalStandardApplyDto getDto(FiveOaExternalStandardApply item) {
        FiveOaExternalStandardApplyDto dto=FiveOaExternalStandardApplyDto.adapt(item);
        dto.setProcessName("?????????");

        CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

        if (!dto.getProcessEnd()) {

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaExternalStandardApplyMapper.updateByPrimaryKey(dto);
                fiveOaResearchProjectLibraryService.genLibraryByStandard(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    /**
     * ??????excl
     * @param startTime ????????????
     * @param endTime ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object>params,String uiSref,String startTime,String endTime,String userLogin) {
        List<Map> list = new ArrayList<>();

        //??????????????????
        params.put("isLikeSelect", true);
        //??????????????????
        Map head = Maps.newHashMap();
        head.put("myDeptData", false);
        head.put("uiSref", uiSref);
        head.put("enLogin", userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//???????????????userLogin ??????????????????
        //????????? ???????????????
        params.put("deleted", false);
        params.put("processEnd", true);
        //???????????????
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        List<FiveOaExternalStandardApply> fiveOaExternalStandardApplies = fiveOaExternalStandardApplyMapper.selectAll(params);
        for (FiveOaExternalStandardApply dto : fiveOaExternalStandardApplies) {

            Map map1 = new LinkedHashMap();
            map1.put("????????????",dto.getTaskName());
            map1.put("????????????",dto.getDeptName());
            map1.put("????????????",dto.getCommencementDate());
            map1.put("????????????",dto.getCompletionDate());
            map1.put("???????????????",dto.getTaskChargerName());
            map1.put("???????????????",dto.getMainParticipantName());
            map1.put("?????????????????????",dto.getOutsidePayment());
            map1.put("?????????????????????????????????",dto.getYearExceptPayment());
            map1.put("?????????????????????",dto.getScientificFirstTrialName());
            map1.put("??????????????????",dto.getMajorResearchContent());
            map1.put("???????????????????????????",dto.getAchivement());
            map1.put("??????",dto.getRemark());
            list.add(map1);
        }

        //???????????????????????????????????? ????????????
        Map map = new LinkedHashMap();
        Map map1 = list.get(0);
        for (Object key : map1.keySet()) {
            map.put(key, "");
        }
        list.add(map);
        Collections.swap(list, list.size() - 1, 0);

        return list;
    }
}
