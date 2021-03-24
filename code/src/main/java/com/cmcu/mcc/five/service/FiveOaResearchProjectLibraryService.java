package com.cmcu.mcc.five.service;

import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.five.dao.FiveOaResearchProjectLibraryMapper;
import com.cmcu.mcc.five.dto.FiveOaExternalResearchProjectApplyDto;
import com.cmcu.mcc.five.dto.FiveOaExternalStandardApplyDto;
import com.cmcu.mcc.five.dto.FiveOaInlandProjectApplyDto;
import com.cmcu.mcc.five.dto.FiveOaResearchProjectLibraryDto;
import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
import com.cmcu.mcc.five.entity.FiveOaResearchProjectLibrary;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FiveOaResearchProjectLibraryService extends BaseService {

    @Resource
    FiveOaResearchProjectLibraryMapper fiveOaResearchProjectLibraryMapper;
    @Resource
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    FiveOaInlandProjectApplyService fiveOaInlandProjectApplyService;
    @Resource
    FiveOaExternalResearchProjectApplyService fiveOaExternalResearchProjectApplyService;
    @Resource
    FiveOaExternalStandardApplyService fiveOaExternalStandardApplyService;

    public void remove(int id,String userLogin){
        FiveOaResearchProjectLibrary model = fiveOaResearchProjectLibraryMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin),"该数据是用户"+model.getCreatorName()+"创建");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        fiveOaResearchProjectLibraryMapper.updateByPrimaryKey(model);
    }


    public PageInfo<Object> listPagedData(String uiSref, Map<String,Object> params,String userLogin, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaResearchProjectLibraryMapper.selectAll(params));
        List<FiveOaResearchProjectLibraryDto> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaResearchProjectLibrary item=(FiveOaResearchProjectLibrary) p;
            FiveOaResearchProjectLibraryDto dto = getDto(item);
            list.add(dto);
        });
        return pageInfo;
    }


    public FiveOaResearchProjectLibraryDto getDto(FiveOaResearchProjectLibrary item){
        FiveOaResearchProjectLibraryDto model = FiveOaResearchProjectLibraryDto.adapt(item);


        return model;
    }

    public void update(FiveOaResearchProjectLibraryDto item){
        FiveOaResearchProjectLibrary model = fiveOaResearchProjectLibraryMapper.selectByPrimaryKey(item.getId());
        model.setProjectName(item.getProjectName());
        model.setProjectContent(item.getProjectContent());
        model.setProjectType(item.getProjectType());
        model.setAchievement(item.getAchievement());
        model.setCommencementDate(item.getCommencementDate());
        model.setCompletionDate(item.getCompletionDate());
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
        model.setOutsidePayment(item.getOutsidePayment());
        model.setYearExceptPayment(item.getYearExceptPayment());

        model.setGmtModified(new Date());
        model.setDeptName(item.getDeptName());
        model.setDeptId(item.getDeptId());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);

        fiveOaResearchProjectLibraryMapper.updateByPrimaryKey(model);
    }

    public FiveOaResearchProjectLibraryDto getModelById(int id){
        return getDto(fiveOaResearchProjectLibraryMapper.selectByPrimaryKey(id));
    }

    /**
     * 已完成 内部项目入库
     */
    public void genLibraryByInlandProject(FiveOaInlandProjectApply inland){
        FiveOaResearchProjectLibrary library=new FiveOaResearchProjectLibrary();
        library.setProjectName(inland.getProjectName());
        library.setProjectContent(inland.getProjectContent());
        library.setProjectType("内部科研项目");
        library.setAchievement(inland.getAchievement());
        library.setCommencementDate(inland.getStartTime());
        library.setCompletionDate(inland.getEndTime());
        library.setTotalPrice(inland.getTotalPrice());
        library.setChargeMen(inland.getChargeMen());
        library.setChargeMenName(inland.getChargeMenName());
        library.setAttender(inland.getAttender());
        library.setAttenderName(inland.getAttenderName());
        library.setMaterialsCost(inland.getMaterialsCost());
        library.setAppropriativeCost(inland.getAppropriativeCost());
        library.setOutsourceCost(inland.getOutsourceCost());
        library.setMeetingCost(inland.getMeetingCost());
        library.setTravelCost(inland.getTravelCost());
        library.setSpecialistCost(inland.getSpecialistCost());
        library.setFixeAssetDepreciationCost(inland.getFixeAssetDepreciationCost());
        library.setFuelPowerCost(inland.getFuelPowerCost());
        library.setSalaryServiceCost(inland.getSalaryServiceCost());
        library.setScientificFirstTrial(inland.getScientificFirstTrial());
        library.setScientificFirstTrialName(inland.getScientificFirstTrialName());
        library.setRemark(inland.getRemark());
        library.setSecret(inland.getSecret());
        library.setOutsidePayment("0");
        library.setYearExceptPayment("0");
        library.setGmtCreate(new Date());
        library.setGmtModified(new Date());
        library.setDeptName(inland.getDeptName());
        library.setDeptId(inland.getDeptId());
        library.setCreator(inland.getCreator());
        library.setCreatorName(inland.getCreatorName());
        library.setBusinessKey(inland.getBusinessKey());
         library.setProcessInstanceId(inland.getProcessInstanceId());
        ModelUtil.setNotNullFields(library);
        fiveOaResearchProjectLibraryMapper.insert(library);
    }

    /**
     * 已完成 外部科研项目申请
     */
    public void  genLibraryByResearchProject(FiveOaExternalResearchProjectApplyDto research){
        FiveOaResearchProjectLibrary library=new FiveOaResearchProjectLibrary();
        library.setProjectName(research.getProjectName());

        library.setProjectContent("外部科研项");//主要研究方向
        library.setProjectType("");//项目级别
        library.setAchievement(research.getAchivement());//成果形式及预期效益
        library.setCommencementDate(research.getCommencementDate());
        library.setCompletionDate(research.getCompletionDate());

        library.setChargeMen(research.getTaskCharger());//课题负责人
        library.setChargeMenName(research.getTaskChargerName());
        library.setAttender(research.getMainParticipant());//主要参与人
        library.setAttenderName(research.getMainParticipantName());


        library.setRemark(research.getRemark());
        library.setSecret(research.getSecret());
        library.setOutsidePayment(research.getOutsidePayment());//外拨款
        library.setYearExceptPayment(research.getYearExceptPayment());//本年度预计拨
        library.setYearRepayment(research.getYearRepayment());

        library.setGmtCreate(new Date());
        library.setGmtModified(new Date());
        library.setDeptName(research.getDeptName());
        library.setDeptId(research.getDeptId());
        library.setCreator(research.getCreator());
        library.setCreatorName(research.getCreatorName());
        library.setBusinessKey(research.getBusinessKey());


        library.setTotalPrice(research.getOutsidePayment());
        library.setMaterialsCost("0");
        library.setAppropriativeCost("0");
        library.setOutsourceCost("0");
        library.setMeetingCost("0");
        library.setTravelCost("0");
        library.setSpecialistCost("0");
        library.setFixeAssetDepreciationCost("0");
        library.setFuelPowerCost("0");
        library.setSalaryServiceCost("0");
        library.setScientificFirstTrial("");
        library.setScientificFirstTrialName("");
        library.setProcessInstanceId(research.getProcessInstanceId());
        ModelUtil.setNotNullFields(library);
        fiveOaResearchProjectLibraryMapper.insert(library);
    }

    /**
     * 已完成  外部标准规范、图集项目申请
     */
    public void genLibraryByStandard(FiveOaExternalStandardApplyDto standar){
        FiveOaResearchProjectLibrary library=new FiveOaResearchProjectLibrary();
        library.setProjectName(standar.getTaskName());

        library.setProjectContent("");//主要研究方向
        library.setProjectType("外部标准规范图集项目");//项目级别
        library.setAchievement(standar.getAchivement());//成果形式及预期效益
        library.setCommencementDate(standar.getCommencementDate());
        library.setCompletionDate(standar.getCompletionDate());

        library.setChargeMen(standar.getTaskCharger());//课题负责人
        library.setChargeMenName(standar.getTaskChargerName());
        library.setAttender(standar.getMainParticipant());//主要参与人
        library.setAttenderName(standar.getMainParticipantName());


        library.setRemark(standar.getRemark());
        library.setSecret(standar.getSecret());
        library.setOutsidePayment(standar.getOutsidePayment());//外拨款
        library.setYearExceptPayment(standar.getYearExceptPayment());//本年度预计拨付
        library.setYearRepayment("0");

        library.setGmtCreate(new Date());
        library.setGmtModified(new Date());
        library.setDeptName(standar.getDeptName());
        library.setDeptId(standar.getDeptId());
        library.setCreator(standar.getCreator());
        library.setCreatorName(standar.getCreatorName());
        library.setBusinessKey(standar.getBusinessKey());


        library.setTotalPrice(standar.getOutsidePayment());
        library.setMaterialsCost("0");
        library.setAppropriativeCost("0");
        library.setOutsourceCost("0");
        library.setMeetingCost("0");
        library.setTravelCost("0");
        library.setSpecialistCost("0");
        library.setFixeAssetDepreciationCost("0");
        library.setFuelPowerCost("0");
        library.setSalaryServiceCost("0");
        library.setScientificFirstTrial("");
        library.setScientificFirstTrialName("");
        library.setProcessInstanceId(standar.getProcessInstanceId());
        ModelUtil.setNotNullFields(library);
        fiveOaResearchProjectLibraryMapper.insert(library);

    }


    public List<FiveOaResearchProjectLibrary> listDataByType(String uiSref,String userLogin){
        Map params = Maps.newHashMap();
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        List<FiveOaResearchProjectLibrary> list = fiveOaResearchProjectLibraryMapper.selectAll(params);

        return list;
    }

}
