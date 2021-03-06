package com.cmcu.mcc.ed.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonEdArrangeUserDto;
import com.cmcu.common.entity.CommonEdBuild;
import com.cmcu.common.service.CommonDirService;
import com.cmcu.common.service.CommonEdArrangeUserService;
import com.cmcu.common.service.CommonEdBuildService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.ed.dto.EdProjectStepDto;
import com.cmcu.mcc.ed.entity.FiveEdDesignDrawingCheck;
import com.cmcu.mcc.ed.entity.FiveEdMajorDrawingCheck;
import com.cmcu.mcc.ed.entity.FiveEdMajorDrawingCheckDetail;
import com.cmcu.mcc.ed.dao.FiveEdMajorDrawingCheckDetailMapper;
import com.cmcu.mcc.ed.dao.FiveEdMajorDrawingCheckMapper;
import com.cmcu.mcc.ed.dto.FiveEdMajorDrawingCheckDto;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FiveEdMajorDrawingCheckService {
    @Resource
    FiveEdMajorDrawingCheckMapper fiveEdMajorDrawingCheckMapper;
    @Resource
    FiveEdMajorDrawingCheckDetailMapper fiveEdMajorDrawingCheckDetailMapper;
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
    EdProjectStepService edProjectStepService;
    @Resource
    CommonEdBuildService commonEdBuildService;
    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;
    @Resource
    FiveEdDesignDrawingService fiveEdDesignDrawingService;

    @Resource
    FiveEdDesignDrawingCheckService fiveEdDesignDrawingCheckService;
    @Resource
    CommonDirService commonDirService;

    public void remove(int id,String userLogin){
        FiveEdMajorDrawingCheck item = fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveEdMajorDrawingCheckDto fiveEdMajorDrawingCheckDto){
        FiveEdMajorDrawingCheck model = fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(fiveEdMajorDrawingCheckDto.getId());
        BeanUtils.copyProperties(fiveEdMajorDrawingCheckDto,model);
        model.setGmtModified(new Date());
        fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        List<String> lanUser = selectEmployeeService.getUserListByRoleName("???????????????(?????????)");
        List<String> diUser = selectEmployeeService.getUserListByRoleName("???????????????(?????????)");
        lanUser.addAll(diUser);
        variables.put("copyMan",MyStringUtil.listToString(lanUser));//
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public FiveEdMajorDrawingCheckDto getModelById(int id){

        return getDto(fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(id));
    }

    public FiveEdMajorDrawingCheckDto getDto(FiveEdMajorDrawingCheck item) {
        FiveEdMajorDrawingCheckDto dto=FiveEdMajorDrawingCheckDto.adapt(item);
        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(dto);
                //????????????????????????
                fiveEdDesignDrawingService.updateModel(dto.getId());
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }


        return dto;
    }

    /**
     *
     * @param userLogin
     * @param stepId ??????Id ed_project_step
     * @return
     */

    public FiveEdMajorDrawingCheck getNewModel(int stepId,String userLogin){
        FiveEdMajorDrawingCheck item=new FiveEdMajorDrawingCheck();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        List<String> majorList = getMajorList(stepId, userLogin);

        //????????????????????????
        EdProjectStepDto edProjectStepDto = edProjectStepService.getModelById(stepId);
        //???????????????????????????????????? ????????????
        FiveEdDesignDrawingCheck defaultItem = fiveEdDesignDrawingCheckService.getDefaultItem(stepId);
        Assert.state(defaultItem!=null,"?????? ???????????????????????????");
        Assert.state(defaultItem.getProcessEnd(),"?????? ???????????????????????????");


        //????????????????????????????????????
        //Assert.state(majorList.size()>0,"????????????????????????????????????");
        item.setMajorName(StringUtils.join(majorList,","));//??????????????????

        item.setProjectName(edProjectStepDto.getProjectName());//????????????
        item.setProjectId(edProjectStepDto.getProjectId());
        item.setProjectNo(edProjectStepDto.getProjectNo());//????????????
        item.setStageName(edProjectStepDto.getStageName());//????????????
        item.setStepId(edProjectStepDto.getId());
        item.setStepName(edProjectStepDto.getStepName());
        item.setApplyMan(hrEmployeeDto.getUserLogin());
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setApplyPhone(hrEmployeeDto.getMobile());
        item.setTotalChargeMan(edProjectStepDto.getProjectChargeMan());//????????????
        item.setTotalChargeManName(edProjectStepDto.getChargeManName());
        item.setFormNo("");//????????????
        item.setContractNo(edProjectStepDto.getContractNo());//?????????
        item.setBuildName(StringUtils.join(getBuildList(stepId+""),","));//???????????????
        item.setDeptId(hrEmployeeDto.getDeptId());//????????????
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCheckTime(MyDateUtil.getStringDateShort());

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        fiveEdMajorDrawingCheckMapper.insert(item);
        String businessKey= EdConst.FIVE_ED_MAJOR_DRAWING_CHECK+ "_" + item.getProjectId()+ "_" + item.getId();
        Map variables = Maps.newHashMap();

        variables.put("userLogin", userLogin);
        variables.put("processDescription", "???????????????????????????"+item.getStageName());
        variables.put("totalDesigner", MyStringUtil.getStringList(item.getTotalChargeMan()));//??????
      //  variables.put("drawingMan","0565,9530,9528,9529");//????????? ????????? ????????? ?????????

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ED_MAJOR_DRAWING_CHECK,item.getBusinessKey(), variables, MccConst.APP_CODE);

        item.setProcessInstanceId(processInstanceId);
        fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(item);
        //?????????????????????
        commonDirService.newDir(businessKey,"?????????",0,userLogin);
        commonDirService.newDir(businessKey,"??????",0,userLogin);
        commonDirService.newDir(businessKey,"??????",0,userLogin);

        return item;
    }

    /**
     * ????????????????????? ????????????
     * @param stepId
     * @return
     */
    public List<String> getBuildList(String stepId){
        List<String> list= Lists.newArrayList();
        List<CommonEdBuild> commonEdBuilds = commonEdBuildService.listData(stepId);
        for (CommonEdBuild build:commonEdBuilds){
            list.add(build.getBuildName());
        }
        return list;
    }

    /**?????????????????????????????????????????????
     * @param stepId
     * @param userLogin
     * @return
     */
    public List<String> getMajorList(int stepId,String userLogin){
        String defaultArrangeBusinessKey = commonEdArrangeUserService.getDefaultArrangeBusinessKey(stepId);
        List<CommonEdArrangeUserDto> listUser = commonEdArrangeUserService.listUser(defaultArrangeBusinessKey);
        List<CommonEdArrangeUserDto> attenderList=Lists.newArrayList();

        for (CommonEdArrangeUserDto userDto:listUser){
           // if (userDto.getAllUser().contains(userLogin)&&userDto.getRoleName().equals("???????????????")){
            if (userDto.getAllUser().contains(userLogin)){
                attenderList.add(userDto);
            }
        }
        String userMajor=",";
        List<String> list=Lists.newArrayList();
        for (CommonEdArrangeUserDto userDto:attenderList){
            if (!userMajor.contains(userDto.getMajorName())){
                userMajor=userDto.getMajorName()+",";
                list.add(userDto.getMajorName());
            }
        }
        return list;
    }

    public List<FiveEdMajorDrawingCheckDto> listDataByStepId(int stepId,String userLogin) {
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("stepId",stepId);
        List<FiveEdMajorDrawingCheck> oList = fiveEdMajorDrawingCheckMapper.selectAll(params);
        List<FiveEdMajorDrawingCheckDto> list = Lists.newArrayList();
        oList.forEach(p -> list.add(getDto(p)));
        return list;
    }

    public void removeDetail(int id){
        FiveEdMajorDrawingCheckDetail model = fiveEdMajorDrawingCheckDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveEdMajorDrawingCheckDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveEdMajorDrawingCheckDetail fiveEdMajorDrawingCheckDetail){
        FiveEdMajorDrawingCheckDetail model = fiveEdMajorDrawingCheckDetailMapper.selectByPrimaryKey(fiveEdMajorDrawingCheckDetail.getId());
        BeanUtils.copyProperties(fiveEdMajorDrawingCheckDetail,model);
        model.setGmtModified(new Date());
        fiveEdMajorDrawingCheckDetailMapper.updateByPrimaryKey(model);
    }

    /**
     * ??????????????????   ???????????????????????????????????????
     * @param checkId ??????ID
     * @param majorName ????????????
     */
    public void getNewModelDetailList(int checkId,String majorName){
        FiveEdMajorDrawingCheck fiveEdMajorDrawingCheck = fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(checkId);
        List<String> list= Lists.newArrayList();
        List<CommonEdBuild> commonEdBuilds = commonEdBuildService.listData(fiveEdMajorDrawingCheck.getStepId()+"");
        for (CommonEdBuild build:commonEdBuilds){
            list.add(build.getBuildName());
            FiveEdMajorDrawingCheckDetail item=new FiveEdMajorDrawingCheckDetail();
            item.setCheckId(fiveEdMajorDrawingCheck.getId());
            item.setStepId(fiveEdMajorDrawingCheck.getStepId());
            item.setBuildName(build.getBuildName());//????????????
            item.setMajorName(majorName);
            item.setBuildId(build.getId());
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            item.setDeptId(fiveEdMajorDrawingCheck.getDeptId()+"");
            item.setDeptName(fiveEdMajorDrawingCheck.getDeptName());
            item.setCreator(fiveEdMajorDrawingCheck.getCreator());
            item.setCreatorName(fiveEdMajorDrawingCheck.getCreatorName());
            item.setDeleted(false);
            ModelUtil.setNotNullFields(item);
            fiveEdMajorDrawingCheckDetailMapper.insert(item);
        }
    }

    public int getNewModelDetail(int checkId){
        FiveEdMajorDrawingCheck fiveEdMajorDrawingCheck = fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(checkId);

        FiveEdMajorDrawingCheckDetail item=new FiveEdMajorDrawingCheckDetail();
        item.setCheckId(fiveEdMajorDrawingCheck.getId());
        item.setStepId(fiveEdMajorDrawingCheck.getStepId());

        item.setDeptId(fiveEdMajorDrawingCheck.getDeptId()+"");
        item.setDeptName(fiveEdMajorDrawingCheck.getDeptName());
        item.setSecretLevel("??????");

        //???????????? ???????????????????????????
        FiveEdDesignDrawingCheck defaultItem = fiveEdDesignDrawingCheckService.getDefaultItem(item.getStepId());
        //??????
        int leaflet=defaultItem.getFinishProduceLeaflet()+defaultItem.getTotalProduceLeaflet();
        //??????
        int paperback=defaultItem.getFinishProducePaperback()+defaultItem.getTotalProducePaperback();
        //????????????
        int foreignHardbound=defaultItem.getFinishProduceForeignHardbound()+defaultItem.getTotalProduceForeignHardbound();
        //????????????
        int inlandHardbound=defaultItem.getFinishProduceInlandHardbound()+defaultItem.getTotalProduceInlandHardbound();
        //??????
        int total=leaflet+paperback+foreignHardbound+inlandHardbound;
        item.setCopyNumber(total+"");

        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setCreator(fiveEdMajorDrawingCheck.getCreator());
        item.setCreatorName(fiveEdMajorDrawingCheck.getCreatorName());
        item.setDeleted(false);
        item.setDeptId(fiveEdMajorDrawingCheck.getDeptId()+"");
        item.setDeptName(fiveEdMajorDrawingCheck.getDeptName());
        ModelUtil.setNotNullFields(item);
        fiveEdMajorDrawingCheckDetailMapper.insert(item);

        return item.getId();
    }


    public FiveEdMajorDrawingCheckDetail getModelDetailById(int id){
        return fiveEdMajorDrawingCheckDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveEdMajorDrawingCheckDetail> listDetail(int checkId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("checkId",checkId);
        List<FiveEdMajorDrawingCheckDetail> list = fiveEdMajorDrawingCheckDetailMapper.selectAll(params);
        return list;
    }


    //????????????
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
            FiveEdMajorDrawingCheckDetail detail = fiveEdMajorDrawingCheckDetailMapper.selectByPrimaryKey(id);//??????
            FiveEdMajorDrawingCheck item = fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(detail.getCheckId());

            FiveEdDesignDrawingCheck defaultItem = fiveEdDesignDrawingCheckService.getDefaultItem(item.getStepId());

            data.put("projectNo",item.getProjectNo());
            data.put("checkTime",item.getCheckTime());
            data.put("formNo",item.getFormNo());//????????????
            data.put("projectName",item.getProjectName());
            data.put("stageName",item.getStageName());
            data.put("gmtModified",item.getGmtModified());//????????????
            data.put("year", MyDateUtil.getYear());
            data.put("applyManName",item.getApplyManName());//?????????
            data.put("applyPhone",item.getApplyPhone());//??????
            data.put("totalChargeMan",item.getTotalChargeManName());


            data.put("drawNo",detail.getDrawNo());//??????
            data.put("secretLevel",detail.getSecretLevel());
            data.put("major",detail.getMajorName());
            data.put("foreignPage",detail.getForeignPage());
            data.put("inlandPage",detail.getInlandPage());
            data.put("inlandA1Page",detail.getInlandA1Page());//??????A1
            data.put("drawNumber",detail.getDrawNumber());//???????????? ??????
            data.put("drawA1Number",detail.getDrawA1Number());//??????A1
            data.put("remark",detail.getRemark());
            data.put("copyNumber",detail.getCopyNumber());//????????????
        if(!detail.getDrawA1Number().equals("")){
            if (detail.getCopyNumber().equals("")){
                data.put("lanDrawing",Double.parseDouble(detail.getDrawA1Number())*1);//???????????? =????????????*??????
            }else {
                data.put("lanDrawing",Double.parseDouble(detail.getDrawA1Number())*Integer.parseInt(detail.getCopyNumber()));//???????????? =????????????*??????
            }
        }
            data.put("deptName",detail.getDeptName());

            //????????????
            //??????
            int leaflet=defaultItem.getFinishProduceLeaflet()+defaultItem.getTotalProduceLeaflet();
            //??????
            int paperback=defaultItem.getFinishProducePaperback()+defaultItem.getTotalProducePaperback();
            //????????????
            int foreignHardbound=defaultItem.getFinishProduceForeignHardbound()+defaultItem.getTotalProduceForeignHardbound();
            //????????????
            int inlandHardbound=defaultItem.getFinishProduceInlandHardbound()+defaultItem.getTotalProduceInlandHardbound();
            //??????
            int total=leaflet+paperback+foreignHardbound+inlandHardbound;
            //????????????
            int totalNumber=defaultItem.getTotalProduceForeignHardbound()+defaultItem.getTotalProduceInlandHardbound()+defaultItem.getTotalProducePaperback()+defaultItem.getTotalProduceLeaflet();

            data.put("leaflet",leaflet);
            data.put("paperback",paperback);
            data.put("foreignHardbound",foreignHardbound);
            data.put("inlandHardbound",inlandHardbound);

            data.put("totalNumber",totalNumber);
            data.put("sendNumber",total-1-totalNumber);//????????????

            List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
            for (ActHistoryDto dto:actHistoryDtos){
                if (dto.getActivityName()==null){
                    break;
                }
                if ("??????".equals(dto.getActivityName())){
                    data.put("pigeonhole",dto.getUserName());//
                }

            }
            data.put("nodes",actHistoryDtos);

        return data;
    }

    //??????EXCL
    public List<Map> listMapData(int stepId){
        List<Map> list = new ArrayList<>();

        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        params.put("stepId",stepId);
        List<FiveEdMajorDrawingCheck> oList = fiveEdMajorDrawingCheckMapper.selectAll(params);

        for (FiveEdMajorDrawingCheck check:oList){

            for (FiveEdMajorDrawingCheckDetail dto:listDetail(check.getId())){
                Map newMap = new LinkedHashMap();
                Map printData = getPrintData(dto.getId());


                newMap.put("????????????",printData.get("deptName").toString());
                newMap.put("????????????(?????????)",printData.get("projectNo").toString());
                newMap.put("??????",printData.get("checkTime").toString());
                newMap.put("????????????",printData.get("formNo").toString());
                newMap.put("??????",printData.get("stageName").toString());
                newMap.put("??????",printData.get("major").toString());
                newMap.put("????????????",printData.get("projectName").toString());
                newMap.put("??????",printData.get("drawNo").toString());
                newMap.put("??????",printData.get("secretLevel").toString());
                newMap.put("????????????",printData.get("gmtModified").toString());
                newMap.put("??????-??????/????????????",printData.get("inlandPage").toString());
                newMap.put("??????-????????????","-");
                newMap.put("??????-??????/????????????",printData.get("foreignPage").toString());
                newMap.put("??????-????????????","-");
                newMap.put("??????-??????/????????????",printData.get("drawNumber").toString());
                newMap.put("??????-????????????",printData.get("copyNumber").toString());

                newMap.put("????????????-??????",printData.get("leaflet").toString());
                newMap.put("????????????-??????",printData.get("paperback").toString());
                newMap.put("????????????-????????????",printData.get("inlandHardbound").toString());
                newMap.put("????????????-????????????",printData.get("foreignHardbound").toString());

                newMap.put("??????A1??????-??????",printData.get("drawA1Number").toString());
                newMap.put("??????A1??????-??????",printData.get("lanDrawing").toString());

                newMap.put("??????",printData.get("sendNumber").toString());
                newMap.put("??????",1);
                newMap.put("??????",printData.get("totalNumber").toString());

                newMap.put("?????????",printData.get("pigeonhole").toString());
                newMap.put("?????????",printData.get("applyManName").toString());
                newMap.put("??????",printData.get("applyPhone").toString());
                newMap.put("?????????",printData.get("totalChargeMan").toString());


                newMap.put("??????", dto.getRemark());
                newMap.put("?????????", dto.getCreatorName());
                newMap.put("????????????", MyDateUtil.dateToStr(dto.getGmtCreate()));//?????????????????? 2021-01-01
                list.add(newMap);
            }
        }





        return list;
    }


}
