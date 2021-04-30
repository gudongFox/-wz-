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
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveEdMajorDrawingCheckDto fiveEdMajorDrawingCheckDto){
        FiveEdMajorDrawingCheck model = fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(fiveEdMajorDrawingCheckDto.getId());
        BeanUtils.copyProperties(fiveEdMajorDrawingCheckDto,model);
        model.setGmtModified(new Date());
        fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        List<String> lanUser = selectEmployeeService.getUserListByRoleName("档案图书室(蓝图岗)");
        List<String> diUser = selectEmployeeService.getUserListByRoleName("档案图书室(底图岗)");
        lanUser.addAll(diUser);
        variables.put("copyMan",MyStringUtil.listToString(lanUser));//
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public FiveEdMajorDrawingCheckDto getModelById(int id){

        return getDto(fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(id));
    }

    public FiveEdMajorDrawingCheckDto getDto(FiveEdMajorDrawingCheck item) {
        FiveEdMajorDrawingCheckDto dto=FiveEdMajorDrawingCheckDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(dto);
                //同步设计图纸清单
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
     * @param stepId 阶段Id ed_project_step
     * @return
     */

    public FiveEdMajorDrawingCheck getNewModel(int stepId,String userLogin){
        FiveEdMajorDrawingCheck item=new FiveEdMajorDrawingCheck();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        List<String> majorList = getMajorList(stepId, userLogin);

        //设计阶段分期信息
        EdProjectStepDto edProjectStepDto = edProjectStepService.getModelById(stepId);
        //如果设计图纸校验没有完成 不能发起
        FiveEdDesignDrawingCheck defaultItem = fiveEdDesignDrawingCheckService.getDefaultItem(stepId);
        Assert.state(defaultItem!=null,"请先 完成设计图纸校验！");
        Assert.state(defaultItem.getProcessEnd(),"请先 完成设计图纸校验！");


        //限制创建人为个专业负责人
        //Assert.state(majorList.size()>0,"只能由各专业负责人发起！");
        item.setMajorName(StringUtils.join(majorList,","));//去除前后逗号

        item.setProjectName(edProjectStepDto.getProjectName());//项目名称
        item.setProjectId(edProjectStepDto.getProjectId());
        item.setProjectNo(edProjectStepDto.getProjectNo());//项目编号
        item.setStageName(edProjectStepDto.getStageName());//阶段名称
        item.setStepId(edProjectStepDto.getId());
        item.setStepName(edProjectStepDto.getStepName());
        item.setApplyMan(hrEmployeeDto.getUserLogin());
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setApplyPhone(hrEmployeeDto.getMobile());
        item.setTotalChargeMan(edProjectStepDto.getProjectChargeMan());//项目总师
        item.setTotalChargeManName(edProjectStepDto.getChargeManName());
        item.setFormNo("");//表单编号
        item.setContractNo(edProjectStepDto.getContractNo());//合同号
        item.setBuildName(StringUtils.join(getBuildList(stepId+""),","));//建筑物名称
        item.setDeptId(hrEmployeeDto.getDeptId());//设计单位
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
        variables.put("processDescription", "专业图纸验收清单："+item.getStageName());
        variables.put("totalDesigner", MyStringUtil.getStringList(item.getTotalChargeMan()));//总师
      //  variables.put("drawingMan","0565,9530,9528,9529");//苗文彦 段爱荣 王东英 马亚莉

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ED_MAJOR_DRAWING_CHECK,item.getBusinessKey(), variables, MccConst.APP_CODE);

        item.setProcessInstanceId(processInstanceId);
        fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(item);
        //生成默认文件夹
        commonDirService.newDir(businessKey,"硫酸图",0,userLogin);
        commonDirService.newDir(businessKey,"底图",0,userLogin);
        commonDirService.newDir(businessKey,"蓝图",0,userLogin);

        return item;
    }

    /**
     * 获取建筑物名称 子项名称
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

    /**获取发起人作为专业负责人的专业
     * @param stepId
     * @param userLogin
     * @return
     */
    public List<String> getMajorList(int stepId,String userLogin){
        String defaultArrangeBusinessKey = commonEdArrangeUserService.getDefaultArrangeBusinessKey(stepId);
        List<CommonEdArrangeUserDto> listUser = commonEdArrangeUserService.listUser(defaultArrangeBusinessKey);
        List<CommonEdArrangeUserDto> attenderList=Lists.newArrayList();

        for (CommonEdArrangeUserDto userDto:listUser){
           // if (userDto.getAllUser().contains(userLogin)&&userDto.getRoleName().equals("专业负责人")){
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
     * 新增详情数据   根据子项新增对应子项的名称
     * @param checkId 主表ID
     * @param majorName 对应专业
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
            item.setBuildName(build.getBuildName());//建筑物号
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
        item.setSecretLevel("非密");

        //复制份数 统一获取原设计总数
        FiveEdDesignDrawingCheck defaultItem = fiveEdDesignDrawingCheckService.getDefaultItem(item.getStepId());
        //活页
        int leaflet=defaultItem.getFinishProduceLeaflet()+defaultItem.getTotalProduceLeaflet();
        //简装
        int paperback=defaultItem.getFinishProducePaperback()+defaultItem.getTotalProducePaperback();
        //国外精装
        int foreignHardbound=defaultItem.getFinishProduceForeignHardbound()+defaultItem.getTotalProduceForeignHardbound();
        //国外精装
        int inlandHardbound=defaultItem.getFinishProduceInlandHardbound()+defaultItem.getTotalProduceInlandHardbound();
        //总计
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


    //打印预览
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
            FiveEdMajorDrawingCheckDetail detail = fiveEdMajorDrawingCheckDetailMapper.selectByPrimaryKey(id);//子项
            FiveEdMajorDrawingCheck item = fiveEdMajorDrawingCheckMapper.selectByPrimaryKey(detail.getCheckId());

            FiveEdDesignDrawingCheck defaultItem = fiveEdDesignDrawingCheckService.getDefaultItem(item.getStepId());

            data.put("projectNo",item.getProjectNo());
            data.put("checkTime",item.getCheckTime());
            data.put("formNo",item.getFormNo());//验收单号
            data.put("projectName",item.getProjectName());
            data.put("stageName",item.getStageName());
            data.put("gmtModified",item.getGmtModified());//完成时间
            data.put("year", MyDateUtil.getYear());
            data.put("applyManName",item.getApplyManName());//交验人
            data.put("applyPhone",item.getApplyPhone());//电话
            data.put("totalChargeMan",item.getTotalChargeManName());


            data.put("drawNo",detail.getDrawNo());//图号
            data.put("secretLevel",detail.getSecretLevel());
            data.put("major",detail.getMajorName());
            data.put("foreignPage",detail.getForeignPage());
            data.put("inlandPage",detail.getInlandPage());
            data.put("inlandA1Page",detail.getInlandA1Page());//中文A1
            data.put("drawNumber",detail.getDrawNumber());//图纸张数 底图
            data.put("drawA1Number",detail.getDrawA1Number());//图纸A1
            data.put("remark",detail.getRemark());
            data.put("copyNumber",detail.getCopyNumber());//复制份数
        if(!detail.getDrawA1Number().equals("")){
            if (detail.getCopyNumber().equals("")){
                data.put("lanDrawing",Double.parseDouble(detail.getDrawA1Number())*1);//蓝图份数 =复制份数*底图
            }else {
                data.put("lanDrawing",Double.parseDouble(detail.getDrawA1Number())*Integer.parseInt(detail.getCopyNumber()));//蓝图份数 =复制份数*底图
            }
        }
            data.put("deptName",detail.getDeptName());

            //装订规格
            //活页
            int leaflet=defaultItem.getFinishProduceLeaflet()+defaultItem.getTotalProduceLeaflet();
            //简装
            int paperback=defaultItem.getFinishProducePaperback()+defaultItem.getTotalProducePaperback();
            //国外精装
            int foreignHardbound=defaultItem.getFinishProduceForeignHardbound()+defaultItem.getTotalProduceForeignHardbound();
            //国外精装
            int inlandHardbound=defaultItem.getFinishProduceInlandHardbound()+defaultItem.getTotalProduceInlandHardbound();
            //总计
            int total=leaflet+paperback+foreignHardbound+inlandHardbound;
            //总师份数
            int totalNumber=defaultItem.getTotalProduceForeignHardbound()+defaultItem.getTotalProduceInlandHardbound()+defaultItem.getTotalProducePaperback()+defaultItem.getTotalProduceLeaflet();

            data.put("leaflet",leaflet);
            data.put("paperback",paperback);
            data.put("foreignHardbound",foreignHardbound);
            data.put("inlandHardbound",inlandHardbound);

            data.put("totalNumber",totalNumber);
            data.put("sendNumber",total-1-totalNumber);//发图份数

            List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
            for (ActHistoryDto dto:actHistoryDtos){
                if (dto.getActivityName()==null){
                    break;
                }
                if ("归档".equals(dto.getActivityName())){
                    data.put("pigeonhole",dto.getUserName());//
                }

            }
            data.put("nodes",actHistoryDtos);

        return data;
    }

    //导出EXCL
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


                newMap.put("设计单位",printData.get("deptName").toString());
                newMap.put("项目代码(项目号)",printData.get("projectNo").toString());
                newMap.put("日期",printData.get("checkTime").toString());
                newMap.put("验收单号",printData.get("formNo").toString());
                newMap.put("阶段",printData.get("stageName").toString());
                newMap.put("专业",printData.get("major").toString());
                newMap.put("项目名称",printData.get("projectName").toString());
                newMap.put("图号",printData.get("drawNo").toString());
                newMap.put("密级",printData.get("secretLevel").toString());
                newMap.put("完成时间",printData.get("gmtModified").toString());
                newMap.put("中文-文稿/底图张数",printData.get("inlandPage").toString());
                newMap.put("中文-复制份数","-");
                newMap.put("外文-文稿/底图张数",printData.get("foreignPage").toString());
                newMap.put("外文-复制份数","-");
                newMap.put("图纸-文稿/底图张数",printData.get("drawNumber").toString());
                newMap.put("图纸-复制份数",printData.get("copyNumber").toString());

                newMap.put("装订规格-活页",printData.get("leaflet").toString());
                newMap.put("装订规格-简装",printData.get("paperback").toString());
                newMap.put("装订规格-国内精装",printData.get("inlandHardbound").toString());
                newMap.put("装订规格-国外精装",printData.get("foreignHardbound").toString());

                newMap.put("折合A1张数-底图",printData.get("drawA1Number").toString());
                newMap.put("折合A1张数-蓝图",printData.get("lanDrawing").toString());

                newMap.put("发图",printData.get("sendNumber").toString());
                newMap.put("入库",1);
                newMap.put("总师",printData.get("totalNumber").toString());

                newMap.put("验收人",printData.get("pigeonhole").toString());
                newMap.put("交验人",printData.get("applyManName").toString());
                newMap.put("电话",printData.get("applyPhone").toString());
                newMap.put("审批人",printData.get("totalChargeMan").toString());


                newMap.put("备注", dto.getRemark());
                newMap.put("创建人", dto.getCreatorName());
                newMap.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));//时间格式转换 2021-01-01
                list.add(newMap);
            }
        }





        return list;
    }


}
