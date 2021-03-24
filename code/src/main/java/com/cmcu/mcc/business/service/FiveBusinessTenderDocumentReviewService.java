package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.business.dao.BusinessRecordMapper;
import com.cmcu.mcc.business.dao.FiveBusinessAdvanceDetailMapper;
import com.cmcu.mcc.business.dao.FiveBusinessTenderDocumentReviewMapper;
import com.cmcu.mcc.business.dto.FiveBusinessTenderDocumentReviewDto;
import com.cmcu.mcc.business.entity.BusinessRecord;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceDetail;
import com.cmcu.mcc.business.entity.FiveBusinessTenderDocumentReview;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveBusinessTenderDocumentReviewService extends BaseService {

    @Resource
    FiveBusinessTenderDocumentReviewMapper fiveBusinessTenderDocumentReviewMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    FiveBusinessAdvanceDetailMapper fiveBusinessAdvanceDetailMapper;
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
    @Resource
    HrDeptService hrDeptService;
    @Autowired
    BusinessRecordMapper businessRecordMapper;

    public void remove(int id,String userLogin){
        FiveBusinessTenderDocumentReview item = fiveBusinessTenderDocumentReviewMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    //update
    public void update(FiveBusinessTenderDocumentReviewDto fiveBusinessTenderDocumentReviewDto){
        FiveBusinessTenderDocumentReview model = fiveBusinessTenderDocumentReviewMapper.selectByPrimaryKey(fiveBusinessTenderDocumentReviewDto.getId());
        model.setDeptId(fiveBusinessTenderDocumentReviewDto.getDeptId());
        model.setCreator(fiveBusinessTenderDocumentReviewDto.getCreator());
        model.setCreatorName(fiveBusinessTenderDocumentReviewDto.getCreatorName());
        model.setDeptName(fiveBusinessTenderDocumentReviewDto.getDeptName());
        model.setGmtCreate(fiveBusinessTenderDocumentReviewDto.getGmtCreate());

        model.setRecordId(fiveBusinessTenderDocumentReviewDto.getRecordId());
        model.setFormNo(fiveBusinessTenderDocumentReviewDto.getFormNo());
        model.setRemark(fiveBusinessTenderDocumentReviewDto.getRemark());
        model.setBeginTime(fiveBusinessTenderDocumentReviewDto.getBeginTime());
        model.setPlanBeginTime(fiveBusinessTenderDocumentReviewDto.getPlanBeginTime());
        model.setTotalPrice(fiveBusinessTenderDocumentReviewDto.getTotalPrice());
        model.setProjectManager(fiveBusinessTenderDocumentReviewDto.getProjectManager());
        model.setProjectLevel(fiveBusinessTenderDocumentReviewDto.getProjectLevel());
        model.setProjectLocation(fiveBusinessTenderDocumentReviewDto.getProjectLocation());
        model.setProjectName(fiveBusinessTenderDocumentReviewDto.getProjectName());
        model.setProjectManagerName(fiveBusinessTenderDocumentReviewDto.getProjectManagerName());
        model.setTenderSource(fiveBusinessTenderDocumentReviewDto.getTenderSource());
        model.setCombo(fiveBusinessTenderDocumentReviewDto.getCombo());
        model.setReviewUsername(fiveBusinessTenderDocumentReviewDto.getReviewUsername());
        model.setReviewUser(fiveBusinessTenderDocumentReviewDto.getReviewUser());
        model.setDeptReviewUser(fiveBusinessTenderDocumentReviewDto.getDeptReviewUser());
        model.setDeptReviewUsername(fiveBusinessTenderDocumentReviewDto.getDeptReviewUsername());
        model.setGmtModified(new Date());
        fiveBusinessTenderDocumentReviewMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),3,true));

        if (model.getProjectLevel().equals("公司级")){
            variables.put("flag",false);
            variables.put("reviewUsers",MyStringUtil.getStringList(model.getReviewUser()));

            variables.put("engineeringDeptChargeMan",selectEmployeeService.getParentDeptChargeMen(29,1,false));//工程管理部主管领导
            variables.put("engineeringDeptLeader",selectEmployeeService.getParentDeptChargeMen(29,4,false));//工程管理部分管领导

        }else {
            variables.put("flag",true);
            variables.put("deptReviewUsers",MyStringUtil.getStringList(model.getDeptReviewUser()));
        }

        List<String> list = hrEmployeeService.selectUserByRoleNames("财务金融部(工资岗)");
        list.addAll(hrEmployeeService.selectUserByRoleNames("经营发展部人员(合同)"));
        variables.put("copyMan", MyStringUtil.listToString(list));
        variables.put("lawReview", hrEmployeeService.selectUserByRoleNames("法律审查"));//法律审查
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveBusinessTenderDocumentReviewDto getModelById(int id){
        return getDto(fiveBusinessTenderDocumentReviewMapper.selectByPrimaryKey(id));
    }

    public FiveBusinessTenderDocumentReviewDto getDto(FiveBusinessTenderDocumentReview item) {
        FiveBusinessTenderDocumentReviewDto dto= FiveBusinessTenderDocumentReviewDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBusinessTenderDocumentReviewMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveBusinessTenderDocumentReview item=new FiveBusinessTenderDocumentReview();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setProjectLevel("院级");
        item.setCombo("否");

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        //公司级评审人员
        String reviewUser=  MyStringUtil.listToString(hrEmployeeService.selectUserByRoleNames("合同章评审人")) ;
        String reviewUsername = selectEmployeeService.getNameByUserLogins(reviewUser);
        item.setReviewUser(reviewUser);
        item.setReviewUsername(reviewUsername);

        ModelUtil.setNotNullFields(item);

        fiveBusinessTenderDocumentReviewMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESS_TENDER_DOCUMENT_REVIEW+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "工程承包项目招标文件评审："+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),3,true));//部门领导
        variables.put("lawReview", hrEmployeeService.selectUserByRoleNames("法律审查"));//法律审查

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_TENDER_DOCUMENT_REVIEW,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveBusinessTenderDocumentReviewMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessTenderDocumentReviewMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessTenderDocumentReview item=(FiveBusinessTenderDocumentReview) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
