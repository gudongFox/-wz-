package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaInlandProjectApplyMapper;
import com.cmcu.mcc.five.dao.FiveOaInlandProjectReviewMapper;
import com.cmcu.mcc.five.dto.FiveOaInlandProjectApplyDto;
import com.cmcu.mcc.five.dto.FiveOaInlandProjectReviewDto;
import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
import com.cmcu.mcc.five.entity.FiveOaInlandProjectReview;
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
public class FiveOaInlandProjectReviewService extends BaseService {

    @Resource
    FiveOaInlandProjectReviewMapper fiveOaInlandProjectReviewMapper;
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

    public void remove(int id,String userLogin){
        FiveOaInlandProjectReview item = fiveOaInlandProjectReviewMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaInlandProjectReviewDto item){

        FiveOaInlandProjectReview model = fiveOaInlandProjectReviewMapper.selectByPrimaryKey(item.getId());
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
        model.setProjectNo(item.getProjectNo());
        model.setReviewType(item.getReviewType());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();

        fiveOaInlandProjectReviewMapper.updateByPrimaryKey(model);

        List<String> list = new ArrayList<>();
        list.add("2625");
        list.add("2816");

        variables.put("scientificMan",list);//取角色传入流程模型业务办理人

        //线上或线下评审
        variables.put("line",model.getReviewType());
        if (model.getReviewType()){
            variables.put("expertCommitteeList", MyStringUtil.getStringList(model.getScientificFirstTrial()));//公司专家委
        }

        variables.put("scientificDeptLeader",selectEmployeeService.getParentDeptChargeMen(101,1,false));// 科质部领导

        variables.put("scientificLeader",selectEmployeeService.getParentDeptChargeMen(101,4,false));// 公司主管科质部领导

        variables.put("deptScientificMen", model.getChargeMen());


        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);


    }

    public FiveOaInlandProjectReviewDto getModelById(int id){

        return getDto(fiveOaInlandProjectReviewMapper.selectByPrimaryKey(id));
    }

    public FiveOaInlandProjectReviewDto getDto(FiveOaInlandProjectReview item) {
        item.setMaterialsCost(MyStringUtil.moneyToString(item.getMaterialsCost(),6));
        item.setAppropriativeCost(MyStringUtil.moneyToString(item.getAppropriativeCost(),6));
        item.setOutsourceCost(MyStringUtil.moneyToString(item.getOutsourceCost(),6));
        item.setMeetingCost(MyStringUtil.moneyToString(item.getMeetingCost(),6));
        item.setTravelCost(MyStringUtil.moneyToString(item.getTravelCost(),6));
        item.setSpecialistCost(MyStringUtil.moneyToString(item.getSpecialistCost(),6));
        item.setFuelPowerCost(MyStringUtil.moneyToString(item.getFuelPowerCost(),6));
        item.setFixeAssetDepreciationCost(MyStringUtil.moneyToString(item.getFixeAssetDepreciationCost(),6));
        item.setSalaryServiceCost(MyStringUtil.moneyToString(item.getSalaryServiceCost(),6));
        FiveOaInlandProjectReviewDto dto=FiveOaInlandProjectReviewDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
           CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
               dto.setProcessEnd(true);
                fiveOaInlandProjectReviewMapper.updateByPrimaryKey(dto);
           }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
               dto.setProcessName(customProcessInstance.getCurrentTaskName());
           }
       }

        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaInlandProjectReview item=new FiveOaInlandProjectReview();
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

        fiveOaInlandProjectReviewMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_INLAND_PROJECT_REVIEW+ "_" + item.getId();

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INLAND_PROJECT_REVIEW,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaInlandProjectReviewMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInlandProjectReviewMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInlandProjectReview item=(FiveOaInlandProjectReview) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 导出excel
     * @param startTime1 开始时间
     * @param endTime1 结束时间
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
        List<Map> list = new ArrayList<>();
        //模糊匹配查询
        params.put("isLikeSelect",true);
        //数据权限验证
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//字段中含有userLogin 排除干扰因素
        //为删除 流程已完成
        params.put("deleted",false);
        params.put("processEnd",true);
        //时间端参数
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);


        List<FiveOaInlandProjectReview> fiveOaInlandProjectReviews=fiveOaInlandProjectReviewMapper.selectAll(params);
        for (FiveOaInlandProjectReview dto:fiveOaInlandProjectReviews){
            Map map1=new LinkedHashMap();
            map1.put("课题名称",dto.getProjectName());
            map1.put("申请单位",dto.getDeptName());
            map1.put("开工日期",dto.getStartTime());
            map1.put("完工日期",dto.getEndTime());
            map1.put("课题负责人",dto.getChargeMenName() );
            map1.put("主要参加人", dto.getAttenderName());
            map1.put("项目类别",dto.getProjectType() );
            map1.put("主要研究内容",dto.getProjectContent() );
            map1.put("成果形式及预期效益", dto.getAchievement());
            map1.put("合计",dto.getTotalPrice());
            map1.put("材料费",dto.getMaterialsCost());
            map1.put("专用费",dto.getAppropriativeCost());
            map1.put("外协费",dto.getOutsourceCost());
            map1.put("事务费-会议费",dto.getMeetingCost());
            map1.put("事务费-差旅费",dto.getTravelCost());
            map1.put("事务费-专家咨询费",dto.getSpecialistCost());
            map1.put("固定资产折旧费",dto.getFixeAssetDepreciationCost());
            map1.put("燃料动力费",dto.getFuelPowerCost());
            map1.put("工资及劳务费",dto.getSalaryServiceCost());
            map1.put("备注",dto.getRemark());
            list.add(map1);
        }
        return list;
    }


}
