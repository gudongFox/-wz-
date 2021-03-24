package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyFormNo;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.dto.EdFileDto;
import com.cmcu.mcc.ed.service.EdFileService;
import com.cmcu.mcc.five.dao.FiveHrQualifyApplyDetailMapper;
import com.cmcu.mcc.five.dao.FiveHrQualifyApplyMapper;
import com.cmcu.mcc.five.dto.FiveHrQualifyApplyDto;
import com.cmcu.mcc.five.entity.FiveHrQualifyApply;
import com.cmcu.mcc.five.entity.FiveHrQualifyApplyDetail;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrQualifyDto;
import com.cmcu.mcc.hr.entity.HrQualify;
import com.cmcu.mcc.hr.service.*;
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
import java.util.stream.Collectors;

@Service
public class FiveHrQualifyApplyService extends BaseService {

    @Resource
    FiveHrQualifyApplyMapper fiveHrQualifyApplyMapper;

    @Resource
    FiveHrQualifyApplyDetailMapper fiveHrQualifyApplyDetailMapper;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    HrDeptService hrDeptService;

    @Autowired
    HrQualifyService hrQualifyService;

    @Resource
    MyActService myActService;

    @Resource
    ProcessQueryService processQueryService;
    @Autowired
    TaskHandleService taskHandleService;
    @Resource
    HrCertificationService hrCertificationService;
    @Resource
    HrEducationService hrEducationService;
    @Resource
    EdFileService edFileService;
    @Resource
    HrHonorService hrHonorService;
    @Autowired
    CommonCodeService commonCodeService;
    @Resource
    HandleFormService handleFormService;

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        /*boolean IsChangreMen = selectEmployeeService.getPrincipalByUserLogin(userLogin);
        if (IsChangreMen){
            List<Integer> deptIdList =new ArrayList<>();
            deptIdList.add(selectEmployeeService.selectByUserLogin(userLogin).getDeptId());
            params.put("deptIdList", deptIdList);
        }else {
            params.put("creator",userLogin);
        }
        params.put("applyType",uiSref.contains("Approve")?"审定人":"设计人");*/
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveHrQualifyApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto((FiveHrQualifyApply) p)));
        pageInfo.setList(list);
        return pageInfo;
    }

    public FiveHrQualifyApplyDto getModelById(int id){
        return getDto(fiveHrQualifyApplyMapper.selectByPrimaryKey(id));
    }

    public void update(FiveHrQualifyApplyDto item){
        FiveHrQualifyApply model=fiveHrQualifyApplyMapper.selectByPrimaryKey(item.getId());
        model.setCheckYear(item.getCheckYear());
        model.setRemark(item.getRemark());

        model.setDeptId(item.getDeptId());
        model.setDeptName(hrDeptService.getDeptNameById(item.getDeptId()));
        model.setGmtModified(new Date());
        model.setPlanPost(item.getPlanPost());
        fiveHrQualifyApplyMapper.updateByPrimaryKey(model);

    }

    public int getNewModel(String uiSref, String userLogin) {
        List<Integer> deptIdList = selectEmployeeService.getMyDeptList(userLogin, uiSref);
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        String type = uiSref.contains("Approve") ? "审定人" : "设计人";
        Date now = new Date();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        FiveHrQualifyApply model = new FiveHrQualifyApply();
        model.setUserLogin(hrEmployeeDto.getUserLogin());
        model.setUserName(hrEmployeeDto.getUserName());
        model.setIdentity(hrEmployeeDto.getIdCardNo());
        model.setGender(hrEmployeeDto.getGender());
        model.setBirthDay(hrEmployeeDto.getBirthDay());

        model.setRanks(hrEmployeeDto.getRanks());
        model.setRankTime(hrEmployeeDto.getRankTime());
        model.setUserType(hrEmployeeDto.getUserType());
        model.setEducationBackground(hrEmployeeDto.getEducationBackground());
        model.setJoinCompanyTime(hrEmployeeDto.getJoinCompanyTime());

        model.setGmtCreate(now);
        model.setGmtModified(now);
        model.setApplyType(type);
        model.setCheckYear(year);
        model.setIdentity(hrEmployeeDto.getIdCardNo());
        model.setDeleted(false);
        model.setGmtCreate(new Date());
        model.setGmtModified(new Date());
        model.setProcessEnd(false);
        model.setHandled(false);
        model.setCreator(userLogin);

        model.setCreatorName(hrEmployeeDto.getUserName());
        model.setPlanPost(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());
        if (deptIdList.contains(hrEmployeeDto.getDeptId())) {
            model.setDeptId(hrEmployeeDto.getDeptId());
            model.setDeptName(hrEmployeeDto.getDeptName());
        } else {
            model.setDeptId(deptIdList.get(0));
            model.setDeptName(hrDeptService.getDeptNameById(model.getDeptId()));
        }

        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", type + "资格认定申报");
        variables.put("flag", 0);
        if (selectEmployeeService.getDeptDirector(model.getDeptId()).size()>0){
            variables.put("flag", 1);
            variables.put("deptChargeMen",selectEmployeeService.getDeptDirector(model.getDeptId()));//获取室主任
        }
        if (uiSref.contains("Approve")){
            variables.put("qualityChargeMan", selectEmployeeService.getUserListByRoleName("技术质量管理员"));
        }

        //部门总工
        variables.put("deptChiefEngineer", selectEmployeeService.selectUserByPositionNameAndDeptId("部门总工",model.getDeptId()));
        fiveHrQualifyApplyMapper.insert(model);
        String processKey = uiSref.contains("Approve") ? EdConst.FIVE_HR_APPROVE_APPLY :EdConst.FIVE_HR_DESIGN_QUALIFY_APPLY;
        String businessKey = processKey+"_" + model.getId();
        variables.put("businessKey",businessKey);
        String processInstanceId=  taskHandleService.startProcess(processKey,businessKey, variables, MccConst.APP_CODE);

        //自动编号
        model.setFormNo(MyFormNo.getFormNo(taskHandleService.getDeploymentId(processKey),model.getId()));

        model.setProcessInstanceId(processInstanceId);
        model.setBusinessKey(businessKey);
        if (model.getBusinessKey()=="fiveHrQualifyApply"){
           // getFile(userLogin,model.getBusinessKey()); 获取申请人 学历学位证书
        }
        fiveHrQualifyApplyMapper.updateByPrimaryKey(model);
        initDetailList(model.getId());
        return model.getId();
    }

    /**
     * 获取个人的  学历学位证书  获奖证书  资格证书
     * @param userLogin
     */
    public void getFile(String userLogin,String businessKey){
        List<EdFileDto> list=new ArrayList<>();
        //执业资格附件
        List<EdFileDto> cerFile = hrCertificationService.getAllEdFile(userLogin);
        //学位学历证书
        List<EdFileDto> educationFile = hrEducationService.getAllEdFile(userLogin);
        //获奖证书
        List<EdFileDto> honorFile = hrHonorService.getAllEdFile(userLogin);
        list.addAll(cerFile);
        list.addAll(educationFile);
        list.addAll(honorFile);

        for (EdFileDto dto:list){
            //直接新增
            edFileService.insertByFileDto(dto,businessKey);
        }

    }

    public void remove(int id,String userLogin){
        FiveHrQualifyApply item = fiveHrQualifyApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
        clearDetail(id);
    }

    private FiveHrQualifyApplyDto getDto(FiveHrQualifyApply item){
        FiveHrQualifyApplyDto dto=FiveHrQualifyApplyDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                autoHrQualify(dto);
                dto.setHandled(true);
                fiveHrQualifyApplyMapper.updateByPrimaryKey(dto);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        //申请人 个人基本信息
        dto.setHrEmployeeDto(selectEmployeeService.selectByUserLogin(item.getCreator()));

        List<FiveHrQualifyApplyDetail> details=listDetail(item.getId());
        dto.setUserCount(details.size());
        dto.setUserLoginList(","+StringUtils.join(details.stream().map(FiveHrQualifyApplyDetail::getUserLogin).distinct().collect(Collectors.toList()), ",")+",");
        return dto;
    }

    public void clearDetail(int qualifyApplyId){
        Map params = Maps.newHashMap();
        params.put("qualifyApplyId", qualifyApplyId);
        params.put("deleted",false);
        List<FiveHrQualifyApplyDetail> details = fiveHrQualifyApplyDetailMapper.selectAll(params);
        details.forEach(p->{
            p.setDeleted(true);
            p.setGmtModified(new Date());
            fiveHrQualifyApplyDetailMapper.updateByPrimaryKey(p);
        });
    }

    public List<FiveHrQualifyApplyDetail> listDetail(int qualifyApplyId){
        Map params = Maps.newHashMap();
        params.put("qualifyApplyId", qualifyApplyId);
        params.put("deleted",false);
        List<FiveHrQualifyApplyDetail> details = fiveHrQualifyApplyDetailMapper.selectAll(params);
        return details;
    }

    public void initDetailList(int qualifyApplyId) {
        Date now = new Date();
        FiveHrQualifyApply model = fiveHrQualifyApplyMapper.selectByPrimaryKey(qualifyApplyId);
        Map params = Maps.newHashMap();
        params.put("qualifyApplyId", qualifyApplyId);
        List<FiveHrQualifyApplyDetail> details = fiveHrQualifyApplyDetailMapper.selectAll(params);
        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(model.getCreator());
        if(model.getApplyType().contains("设计")) {

                if (details.stream().anyMatch(p -> p.getUserLogin().equalsIgnoreCase(hrEmployeeDto.getUserLogin()))) {
                    FiveHrQualifyApplyDetail detail = details.stream().filter(p -> p.getUserLogin().equalsIgnoreCase(hrEmployeeDto.getUserLogin())).findFirst().get();
                    if (detail.getDeleted()) {
                        detail.setDeleted(false);
                        detail.setGmtModified(now);
                        fiveHrQualifyApplyDetailMapper.updateByPrimaryKey(detail);
                    }
                } else {
                    FiveHrQualifyApplyDetail detail = new FiveHrQualifyApplyDetail();
                    detail.setQualifyApplyId(qualifyApplyId);
                    detail.setUserLogin(hrEmployeeDto.getUserLogin());
                    detail.setUserName(hrEmployeeDto.getUserName());
                    detail.setDeptId(hrEmployeeDto.getDeptId());
                    detail.setDeptName(hrEmployeeDto.getDeptName());
                    detail.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());
                    detail.setCheckYear(model.getCheckYear());
                    detail.setProjectCharge(false);
                    detail.setMajorCharge(false);

                    detail.setApprove(false);
                    detail.setChiefDesigner(false);
                    detail.setProChief(false);
                    detail.setDeleted(false);
                    //工程设计
                    detail.setDesign(true);
                    detail.setProofread(false);
                    detail.setAudit(false);
                    //工程咨询
                    detail.setConsultDesign(false);
                    detail.setConsultAudit(false);
                    detail.setConsultProofread(false);
                    //工程承包
                    detail.setContractDesign(false);
                    detail.setContractAudit(false);
                    detail.setContractProofread(false);
                    //勘察
                    detail.setExploreAudit(false);
                    detail.setExploreEngineer(false);
                    detail.setExplorePrincipal(false);
                    //监理
                    detail.setSupervisorAudit(false);
                    detail.setSupervisorDesign(false);
                    detail.setSupervisorProofread(false);


                    detail.setGmtCreate(now);
                    detail.setGmtModified(now);
                    ModelUtil.setNotNullFields(detail);
                    if (hrEmployeeDto.getHrQualify() != null) {
                        HrQualify latest =hrEmployeeDto.getHrQualify();
                        detail.setDesign(latest.getDesign());
                        detail.setProofread(latest.getProofread());
                        detail.setAudit(latest.getAudit());
                        detail.setApprove(latest.getApprove());
                        detail.setChiefDesigner(latest.getChiefDesigner());
                        detail.setProChief(latest.getProChief());
                        detail.setProjectCharge(latest.getProjectCharge());
                        //工程设计
                        detail.setMajorCharge(latest.getMajorCharge());
                        detail.setProjectType(latest.getProjectType());
                        detail.setMajorName(latest.getMajorName());
                        //工程咨询
                        detail.setConsultDesign(latest.getConsultDesign());
                        detail.setConsultProofread(latest.getConsultProofread());
                        detail.setConsultAudit(latest.getConsultAudit());
                        //工程承包
                        detail.setContractDesign(latest.getContractDesign());
                        detail.setContractAudit(latest.getContractAudit());
                        detail.setContractProofread(latest.getContractProofread());
                        //勘察
                        detail.setExploreAudit(latest.getExploreAudit());
                        detail.setExploreEngineer(latest.getExploreEngineer());
                        detail.setExplorePrincipal(latest.getExplorePrincipal());
                        //监理
                        detail.setSupervisorAudit(latest.getSupervisorAudit());
                        detail.setSupervisorDesign(latest.getSupervisorDesign());
                        detail.setSupervisorProofread(latest.getSupervisorProofread());
                    }
                    fiveHrQualifyApplyDetailMapper.insert(detail);
                }
        }else{
                if (details.stream().noneMatch(p -> p.getUserLogin().equalsIgnoreCase(hrEmployeeDto.getUserLogin()))) {
                    FiveHrQualifyApplyDetail detail = new FiveHrQualifyApplyDetail();
                    detail.setQualifyApplyId(qualifyApplyId);
                    detail.setUserLogin(hrEmployeeDto.getUserLogin());
                    detail.setUserName(hrEmployeeDto.getUserName());
                    detail.setDeptId(hrEmployeeDto.getDeptId());
                    detail.setDeptName(hrEmployeeDto.getDeptName());
                    detail.setCheckYear(model.getCheckYear());
                    detail.setProjectCharge(false);
                    detail.setMajorCharge(false);
                    detail.setDesign(true);
                    detail.setProofread(false);
                    detail.setAudit(false);
                    detail.setApprove(false);
                    detail.setChiefDesigner(false);
                    detail.setProChief(false);
                    detail.setDeleted(false);

                    //工程设计
                    detail.setDesign(true);
                    detail.setProofread(false);
                    detail.setAudit(false);
                    //工程咨询
                    detail.setConsultDesign(false);
                    detail.setConsultAudit(false);
                    detail.setConsultProofread(false);
                    //工程承包
                    detail.setContractDesign(false);
                    detail.setContractAudit(false);
                    detail.setContractProofread(false);
                    //勘察
                    detail.setExploreAudit(false);
                    detail.setExploreEngineer(false);
                    detail.setExplorePrincipal(false);
                    //监理
                    detail.setSupervisorAudit(false);
                    detail.setSupervisorDesign(false);
                    detail.setSupervisorProofread(false);

                    detail.setGmtCreate(now);
                    detail.setGmtModified(now);
                    //如果有资格 复制
                    if(hrEmployeeDto.getHrQualify()!=null) {
                        detail.setDesign(hrEmployeeDto.getHrQualify().getDesign());
                        detail.setProofread(hrEmployeeDto.getHrQualify().getProofread());
                        detail.setAudit(hrEmployeeDto.getHrQualify().getAudit());
                        detail.setApprove(hrEmployeeDto.getHrQualify().getApprove());
                        detail.setChiefDesigner(hrEmployeeDto.getHrQualify().getChiefDesigner());
                        detail.setProChief(hrEmployeeDto.getHrQualify().getProChief());
                        detail.setProjectCharge(hrEmployeeDto.getHrQualify().getProjectCharge());
                        detail.setMajorCharge(hrEmployeeDto.getHrQualify().getMajorCharge());
                        detail.setProjectType(hrEmployeeDto.getHrQualify().getProjectType());
                        detail.setMajorName(hrEmployeeDto.getHrQualify().getMajorName());
                  }
                    ModelUtil.setNotNullFields(detail);
                    fiveHrQualifyApplyDetailMapper.insert(detail);
            }
        }

    }

    public void insertDetail(int qualifyApplyId, String userLoginList) {
        FiveHrQualifyApply model = fiveHrQualifyApplyMapper.selectByPrimaryKey(qualifyApplyId);
        Date now = new Date();
        for(String userLogin:MyStringUtil.getStringList(userLoginList)) {
            Map params = Maps.newHashMap();
            params.put("qualifyApplyId", qualifyApplyId);
            params.put("userLogin", userLogin);
            HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
            long exist = PageHelper.count(() -> fiveHrQualifyApplyDetailMapper.selectAll(params));
            if (exist > 0) {
                FiveHrQualifyApplyDetail detail = fiveHrQualifyApplyDetailMapper.selectAll(params).get(0);
                detail.setDeleted(false);
                detail.setGmtModified(now);
                fiveHrQualifyApplyDetailMapper.updateByPrimaryKey(detail);
            } else {
                FiveHrQualifyApplyDetail detail = new FiveHrQualifyApplyDetail();
                detail.setQualifyApplyId(qualifyApplyId);
                detail.setUserLogin(hrEmployeeDto.getUserLogin());
                detail.setUserName(hrEmployeeDto.getUserName());
                detail.setDeptId(hrEmployeeDto.getDeptId());
                detail.setDeptName(hrEmployeeDto.getDeptName());
                detail.setCheckYear(model.getCheckYear());
                detail.setProjectCharge(false);
                detail.setMajorCharge(false);
                detail.setDesign(true);
                detail.setProofread(false);
                detail.setAudit(false);
                detail.setApprove(false);
                detail.setChiefDesigner(false);
                detail.setProChief(false);
                detail.setDeleted(false);
                detail.setGmtCreate(now);
                detail.setGmtModified(now);
                ModelUtil.setNotNullFields(detail);
                HrQualify latest = hrQualifyService.selectLatestByUserLogin(detail.getUserLogin());
                if (latest != null) {
                    detail.setDesign(latest.getDesign());
                    detail.setProofread(latest.getProofread());
                    detail.setAudit(latest.getAudit());
                    detail.setApprove(latest.getApprove());
                    detail.setChiefDesigner(latest.getChiefDesigner());
                    detail.setProChief(latest.getProChief());
                    detail.setProjectCharge(latest.getProjectCharge());
                    detail.setMajorCharge(latest.getMajorCharge());
                    detail.setProjectType(latest.getProjectType());
                    detail.setMajorName(latest.getMajorName());
                }
                fiveHrQualifyApplyDetailMapper.insert(detail);
            }
        }
    }

    public void removeDetail(int id){
        FiveHrQualifyApplyDetail detail = fiveHrQualifyApplyDetailMapper.selectByPrimaryKey(id);
        if(detail!=null){
            detail.setGmtModified(new Date());
            detail.setDeleted(true);
            fiveHrQualifyApplyDetailMapper.updateByPrimaryKey(detail);
        }
    }

    public void toggleDetail(int id,String optType){
        FiveHrQualifyApplyDetail detail=fiveHrQualifyApplyDetailMapper.selectByPrimaryKey(id);
        if(detail!=null){
            if ("majorCharge".equals(optType)){
                detail.setMajorCharge(!detail.getMajorCharge());
            }else if ("design".equals(optType)){
                detail.setDesign(!detail.getDesign());
            }else if ("proofread".equals(optType)){
                detail.setProofread(!detail.getProofread());
            }else if ("approve".equals(optType)){
                detail.setApprove(!detail.getApprove());
            }else if ("audit".equals(optType)){
                detail.setAudit(!detail.getAudit());
            }else if ("proChief".equals(optType)){
                detail.setProChief(!detail.getProChief());
            }else if ("chiefDesigner".equals(optType)){
                detail.setChiefDesigner(!detail.getChiefDesigner());
            }else if("consultDesign".equals(optType)){ //工程咨询
                detail.setConsultDesign(!detail.getConsultDesign());
            }else if("consultProofread".equals(optType)){
                detail.setConsultProofread(!detail.getConsultProofread());
            }else if("consultAudit".equals(optType)){
                detail.setConsultAudit(!detail.getConsultAudit());
            }else if("contractDesign".equals(optType)){//工程承包
                detail.setContractDesign(!detail.getContractDesign());
            }else if("contractProofread".equals(optType)){
                detail.setContractProofread(!detail.getContractProofread());
            }else if("contractAudit".equals(optType)){
                detail.setContractAudit(!detail.getContractAudit());
            }else if("exploreEngineer".equals(optType)){//勘察
                detail.setExploreEngineer(!detail.getExploreEngineer());
            }else if("exploreAudit".equals(optType)){
                detail.setExploreAudit(!detail.getExploreAudit());
            }else if("explorePrincipal".equals(optType)){
                detail.setExplorePrincipal(!detail.getExplorePrincipal());
            }else if("supervisorDesign".equals(optType)){//监理
                detail.setSupervisorDesign(!detail.getSupervisorDesign());
            }else if("supervisorProofread".equals(optType)){
                detail.setSupervisorProofread(!detail.getSupervisorProofread());
            }else if("supervisorAudit".equals(optType)){
                detail.setSupervisorAudit(!detail.getSupervisorAudit());
            }

            detail.setGmtModified(new Date());
            fiveHrQualifyApplyDetailMapper.updateByPrimaryKey(detail);
        }
    }

    public void copyDetail(int id){
        FiveHrQualifyApplyDetail detail=fiveHrQualifyApplyDetailMapper.selectByPrimaryKey(id);
        detail.setGmtModified(new Date());
        detail.setGmtCreate(new Date());
        fiveHrQualifyApplyDetailMapper.insert(detail);
    }

    public void updateDetail(int id,String majorName,String projectType){
        FiveHrQualifyApplyDetail detail=fiveHrQualifyApplyDetailMapper.selectByPrimaryKey(id);
        detail.setMajorName(majorName);
        detail.setProjectType(projectType);
        detail.setGmtModified(new Date());
        fiveHrQualifyApplyDetailMapper.updateByPrimaryKey(detail);
    }

    /**
     * 自动将审批通过的设计资格 跟新
     * @param dto
     */
    public void autoHrQualify(FiveHrQualifyApplyDto dto){
        FiveHrQualifyApplyDetail detail=listDetail(dto.getId()).get(0);
        HrQualify hrQualify = new HrQualify();

        if (hrQualifyService.selectLatestByUserLogin(detail.getUserLogin())!=null){
                hrQualify=hrQualifyService.selectLatestByUserLogin(detail.getUserLogin());
                if (dto.getApplyType().contains("设计")){
                    if (detail.getProjectType().equals("工程设计")){
                        hrQualify.setDesign(detail.getDesign());
                        hrQualify.setAudit(detail.getAudit());
                        hrQualify.setProofread(detail.getProofread());
                    }else if (detail.getProjectType().equals("工程咨询")){
                        hrQualify.setConsultDesign(detail.getConsultDesign());
                        hrQualify.setConsultProofread(detail.getConsultProofread());
                        hrQualify.setConsultAudit(detail.getConsultAudit());
                    }else if (detail.getProjectType().equals("工程承包")){
                        hrQualify.setContractDesign(detail.getContractDesign());
                        hrQualify.setContractAudit(detail.getContractAudit());
                        hrQualify.setContractProofread(detail.getContractProofread());
                    }else if (detail.getProjectType().equals("勘察")){
                        hrQualify.setExploreEngineer(detail.getExploreEngineer());
                        hrQualify.setExploreAudit(detail.getExploreAudit());
                        hrQualify.setExplorePrincipal(detail.getExplorePrincipal());
                    }else if (detail.getProjectType().equals("监理")){
                        hrQualify.setSupervisorAudit(detail.getSupervisorAudit());
                        hrQualify.setSupervisorDesign(detail.getSupervisorDesign());
                        hrQualify.setSupervisorProofread(detail.getSupervisorProofread());
                    }
                }else {
                    hrQualify.setMajorCharge(detail.getMajorCharge());
                    hrQualify.setApprove(detail.getApprove());
                }

                hrQualifyService.update(HrQualifyDto.adapt(hrQualify));
            }else {
                HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(detail.getUserLogin());
                hrQualify.setUserLogin(hrEmployeeDto.getUserLogin());
                hrQualify.setUserName(hrEmployeeDto.getUserName());
                hrQualify.setDeptId(hrEmployeeDto.getDeptId());
                hrQualify.setDeptName(hrEmployeeDto.getDeptName());
                hrQualify.setCheckYear(MyDateUtil.getYear());
                hrQualify.setProjectCharge(false);
                hrQualify.setMajorCharge(false);
                hrQualify.setDesign(true);
                hrQualify.setProofread(false);
                hrQualify.setAudit(false);
                hrQualify.setApprove(false);
                hrQualify.setChiefDesigner(false);
                hrQualify.setProChief(false);
                hrQualify.setDeleted(false);
                //工程设计
                hrQualify.setDesign(false);
                hrQualify.setProofread(false);
                hrQualify.setAudit(false);
                //工程咨询
                hrQualify.setConsultDesign(false);
                hrQualify.setConsultAudit(false);
                hrQualify.setConsultProofread(false);
                //工程承包
                hrQualify.setContractDesign(false);
                hrQualify.setContractAudit(false);
                hrQualify.setContractProofread(false);
                //勘察
                hrQualify.setExploreAudit(false);
                hrQualify.setExploreEngineer(false);
                hrQualify.setExplorePrincipal(false);
                //监理
                hrQualify.setSupervisorAudit(false);
                hrQualify.setSupervisorDesign(false);
                hrQualify.setSupervisorProofread(false);
                hrQualify.setGmtCreate(new Date());
                hrQualify.setGmtModified(new Date());

                if (dto.getApplyType().contains("设计")){
                    if (detail.getProjectType().equals("工程设计")){
                        hrQualify.setDesign(detail.getDesign());
                        hrQualify.setAudit(detail.getAudit());
                        hrQualify.setProofread(detail.getProofread());
                    }else if (detail.getProjectType().equals("工程咨询")){
                        hrQualify.setConsultDesign(detail.getConsultDesign());
                        hrQualify.setConsultProofread(detail.getConsultProofread());
                        hrQualify.setConsultAudit(detail.getConsultAudit());
                    }else if (detail.getProjectType().equals("工程承包")){
                        hrQualify.setContractDesign(detail.getContractDesign());
                        hrQualify.setContractAudit(detail.getContractAudit());
                        hrQualify.setContractProofread(detail.getContractProofread());
                    }else if (detail.getProjectType().equals("勘察")){
                        hrQualify.setExploreEngineer(detail.getExploreEngineer());
                        hrQualify.setExploreAudit(detail.getExploreAudit());
                        hrQualify.setExplorePrincipal(detail.getExplorePrincipal());
                    }else if (detail.getProjectType().equals("监理")){
                        hrQualify.setSupervisorAudit(detail.getSupervisorAudit());
                        hrQualify.setSupervisorDesign(detail.getSupervisorDesign());
                        hrQualify.setSupervisorProofread(detail.getSupervisorProofread());
                    }
                }else {
                    hrQualify.setMajorCharge(detail.getMajorCharge());
                    hrQualify.setApprove(detail.getApprove());
                }
                hrQualifyService.insertByDate(hrQualify);
            }
    }
}
