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
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
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
        model.setProjectSituation(fiveBusinessTenderDocumentReviewDto.getProjectSituation());  // ????????????
        model.setDeptCharge(fiveBusinessTenderDocumentReviewDto.getDeptCharge());
        model.setDeptChargeName(fiveBusinessTenderDocumentReviewDto.getDeptChargeName());  // ????????????
        model.setDeptChargeMan(fiveBusinessTenderDocumentReviewDto.getDeptChargeMan());
        model.setDeptChargeManName(fiveBusinessTenderDocumentReviewDto.getDeptChargeManName());  // ???????????????
        fiveBusinessTenderDocumentReviewMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),3,true));
        variables.put("projectManager",model.getProjectManager());
        if (model.getProjectLevel().equals("?????????")){
            variables.put("flag",false);
            variables.put("reviewUsers",MyStringUtil.getStringList(model.getReviewUser()));
            if (model.getDeptChargeName().equals("???????????????")){
                variables.put("temp",false);
                variables.put("businessDeptChargeMan",selectEmployeeService.getParentDeptChargeMen(48,1,false));//???????????????????????????
                variables.put("businessDeptLeader",selectEmployeeService.getParentDeptChargeMen(48,4,false));//???????????????????????????
                variables.put("copyMan2", MyStringUtil.listToString(hrEmployeeService.selectUserByRoleNames("?????????????????????(?????????)")));
            }else {
                variables.put("temp",true);
                variables.put("engineeringDeptChargeMan",selectEmployeeService.getParentDeptChargeMen(29,1,false));//???????????????????????????
                variables.put("engineeringDeptLeader",selectEmployeeService.getParentDeptChargeMen(29,4,false));//???????????????????????????
                variables.put("copyMan3", MyStringUtil.listToString(hrEmployeeService.selectUserByRoleNames("???????????????(?????????)")));
            }

        }else {
            variables.put("flag",true);
            variables.put("deptReviewUsers",MyStringUtil.getStringList(model.getDeptReviewUser()));
            List<String> list = hrEmployeeService.selectUserByRoleNames("???????????????(?????????)");
            list.addAll(hrEmployeeService.selectUserByRoleNames("?????????????????????(?????????)"));
            variables.put("copyMan", MyStringUtil.listToString(list));
        }
        variables.put("projectManager",model.getProjectManager()); // ????????????
        variables.put("deptChargeMen",MyStringUtil.getStringList(model.getDeptChargeMan())); // ???????????????


        variables.put("lawReview", hrEmployeeService.selectUserByRoleNames("????????????"));//????????????
        variables.put("copyMan1", MyStringUtil.listToString(hrEmployeeService.selectUserByRoleNames("???????????????")));

        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveBusinessTenderDocumentReviewDto getModelById(int id){
        return getDto(fiveBusinessTenderDocumentReviewMapper.selectByPrimaryKey(id));
    }

    public FiveBusinessTenderDocumentReviewDto getDto(FiveBusinessTenderDocumentReview item) {
        FiveBusinessTenderDocumentReviewDto dto= FiveBusinessTenderDocumentReviewDto.adapt(item);
        dto.setProcessName("?????????");
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
        item.setProjectLevel("??????");
        item.setCombo("???");

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        //?????????????????????
        String reviewUser=  MyStringUtil.listToString(hrEmployeeService.selectUserByRoleNames("??????????????????")) ;
        String reviewUsername = selectEmployeeService.getNameByUserLogins(reviewUser);
        item.setReviewUser(reviewUser);
        item.setReviewUsername(reviewUsername);

        ModelUtil.setNotNullFields(item);

        fiveBusinessTenderDocumentReviewMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESS_TENDER_DOCUMENT_REVIEW+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "???????????????????????????????????????"+item.getCreatorName());
//        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),3,true));//????????????
        variables.put("lawReview", hrEmployeeService.selectUserByRoleNames("????????????"));//????????????

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_TENDER_DOCUMENT_REVIEW,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveBusinessTenderDocumentReviewMapper.updateByPrimaryKey(item);
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
