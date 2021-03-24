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
import com.cmcu.mcc.five.dao.FiveOaResearchProjectReviewDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaResearchProjectReviewMapper;
import com.cmcu.mcc.five.dto.FiveOaResearchProjectReviewDto;
import com.cmcu.mcc.five.entity.FiveOaResearchProjectReview;
import com.cmcu.mcc.five.entity.FiveOaResearchProjectReviewDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaResearchProjectReviewSevice extends BaseService {

    @Resource
    FiveOaResearchProjectReviewMapper fiveOaResearchProjectReviewMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    FiveOaResearchProjectReviewDetailMapper fiveOaResearchProjectReviewDetailMapper;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    MyActService myActService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaResearchProjectReview item = fiveOaResearchProjectReviewMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    //update
    public void update(FiveOaResearchProjectReviewDto fiveOaResearchProjectReviewDto){
        FiveOaResearchProjectReview model = fiveOaResearchProjectReviewMapper.selectByPrimaryKey(fiveOaResearchProjectReviewDto.getId());
        model.setDeptId(fiveOaResearchProjectReviewDto.getDeptId());
        model.setCreator(fiveOaResearchProjectReviewDto.getCreator());
        model.setCreatorName(fiveOaResearchProjectReviewDto.getCreatorName());
        model.setDeptName(fiveOaResearchProjectReviewDto.getDeptName());
        model.setGmtCreate(fiveOaResearchProjectReviewDto.getGmtCreate());
        model.setGmtModified(new Date());
        model.setExpert(fiveOaResearchProjectReviewDto.getExpert());
        model.setExpertName(fiveOaResearchProjectReviewDto.getExpertName());
        model.setFormNo(fiveOaResearchProjectReviewDto.getFormNo());
        model.setRemark(fiveOaResearchProjectReviewDto.getRemark());
        model.setResearchProjectName(fiveOaResearchProjectReviewDto.getResearchProjectName());
        model.setProjectId(fiveOaResearchProjectReviewDto.getProjectId());
        fiveOaResearchProjectReviewMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("expertManList", MyStringUtil.getStringList(model.getExpert()));
        //获取部门总工 如果不存在 则选择部门正副或签
        List<String> chiefEngineer = selectEmployeeService.listUserByPositionName("部门总工", model.getDeptId());
        if (chiefEngineer.size()>0){
            variables.put("chiefEngineer",chiefEngineer);//部门总工
        }else {
            variables.put("chiefEngineer",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),3,false));
        }
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }


    public FiveOaResearchProjectReviewDto getModelById(int id){
        return getDto(fiveOaResearchProjectReviewMapper.selectByPrimaryKey(id));
    }

    public FiveOaResearchProjectReviewDto getDto(FiveOaResearchProjectReview item) {
        FiveOaResearchProjectReviewDto dto= FiveOaResearchProjectReviewDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaResearchProjectReviewMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaResearchProjectReview item=new FiveOaResearchProjectReview();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaResearchProjectReviewMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_RESEARCH_PROJECT_REVIEW+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);

        variables.put("processDescription", "科技开发费项目评审："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_RESEARCH_PROJECT_REVIEW,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaResearchProjectReviewMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaResearchProjectReviewMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaResearchProjectReview item=(FiveOaResearchProjectReview) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveOaResearchProjectReviewDetail item){
        FiveOaResearchProjectReviewDetail model = fiveOaResearchProjectReviewDetailMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setPerson(item.getPerson());
        model.setPersonName(item.getPersonName());
        model.setPersonNo(item.getPersonNo());
        model.setRemark(item.getRemark());
        model.setResearchName(item.getResearchName());
        model.setCreator(item.getCreator());
        model.setCreatorName(item.getCreatorName());
        model.setOpinion(item.getOpinion());
        model.setGmtCreate(new Date());
        model.setSeq(item.getSeq());
        model.setApproved(item.getApproved());
        fiveOaResearchProjectReviewDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaResearchProjectReviewDetail getNewModelDetail(int researchReviewId,String userLogin){
        FiveOaResearchProjectReviewDetail item=new FiveOaResearchProjectReviewDetail();
        FiveOaResearchProjectReviewDto model = getModelById(researchReviewId);
        int seq=listDetail(researchReviewId).size()+1;
        item.setResearchReviewId(researchReviewId);
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setProjectId(model.getProjectId());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaResearchProjectReviewDetailMapper.insert(item);
        return item;

    }

    public FiveOaResearchProjectReviewDetail getModelDetailById(int id){
        return fiveOaResearchProjectReviewDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaResearchProjectReviewDetail> listDetail(int researchReviewId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("researchReviewId",researchReviewId);//小写
        List<FiveOaResearchProjectReviewDetail> list = fiveOaResearchProjectReviewDetailMapper.selectAll(params);
        return list;
    }

    public void removeDetail(int id){
        FiveOaResearchProjectReviewDetail model = fiveOaResearchProjectReviewDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaResearchProjectReviewDetailMapper.updateByPrimaryKey(model);
    }
}
