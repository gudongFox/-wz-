package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.business.dao.BusinessRecordMapper;
import com.cmcu.mcc.business.entity.BusinessRecord;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaBidApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaBidApplyDto;
import com.cmcu.mcc.five.entity.FiveOaBidApply;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FiveOaBidApplyService {
    @Autowired
    FiveOaBidApplyMapper fiveOaBidApplyMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
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

    @Autowired
    BusinessRecordMapper businessRecordMapper;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaBidApply item = fiveOaBidApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //如果原来有备案 还原
        if(item.getRecordId()!=0){
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(item.getRecordId());
            businessRecord.setBidApplyId(0);
            businessRecordMapper.updateByPrimaryKey(businessRecord);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }
    public void update(FiveOaBidApplyDto fiveOaBidApplyDto){
        FiveOaBidApply model = fiveOaBidApplyMapper.selectByPrimaryKey(fiveOaBidApplyDto.getId());
        model.setRecordNo(fiveOaBidApplyDto.getRecordNo());
        model.setDeptId(fiveOaBidApplyDto.getDeptId());
        model.setDeptName(fiveOaBidApplyDto.getDeptName());
        model.setBidMan(fiveOaBidApplyDto.getBidMan());
        model.setBidManName(fiveOaBidApplyDto.getBidManName());
        model.setBidTime(fiveOaBidApplyDto.getBidTime());
        model.setProjectName(fiveOaBidApplyDto.getProjectName());
        model.setProjectType(fiveOaBidApplyDto.getProjectType());
        model.setProjectIndustry(fiveOaBidApplyDto.getProjectIndustry());
        model.setBidder(fiveOaBidApplyDto.getBidder());
        model.setInformationSource(fiveOaBidApplyDto.getInformationSource());
        model.setBidderLinkMan(fiveOaBidApplyDto.getBidderLinkMan());
        model.setBidderLinkTel(fiveOaBidApplyDto.getBidderLinkTel());
        model.setContractMoney(fiveOaBidApplyDto.getContractMoney());
        model.setProjectAddress(fiveOaBidApplyDto.getProjectAddress());
        model.setProjectScale(fiveOaBidApplyDto.getProjectScale());
        model.setMoneySource(fiveOaBidApplyDto.getMoneySource());
        model.setMoneySourceOther(fiveOaBidApplyDto.getMoneySourceOther());
        model.setQualification(fiveOaBidApplyDto.getQualification());
        model.setQualificationOther(fiveOaBidApplyDto.getQualificationOther());
        model.setBidType(fiveOaBidApplyDto.getBidType());
        model.setBidTypeOther(fiveOaBidApplyDto.getBidTypeOther());
        model.setBidScope(fiveOaBidApplyDto.getBidScope());
        model.setContractType(fiveOaBidApplyDto.getContractType());
        model.setContractTypeOther(fiveOaBidApplyDto.getContractTypeOther());
        model.setProjectTime(fiveOaBidApplyDto.getProjectTime());
        model.setScheduleTarget(fiveOaBidApplyDto.getScheduleTarget());
        model.setDepositMoney(fiveOaBidApplyDto.getDepositMoney());
        model.setDepositTime(fiveOaBidApplyDto.getDepositTime());
        model.setFileDataCost(fiveOaBidApplyDto.getFileDataCost());
        model.setPaymentRule(fiveOaBidApplyDto.getPaymentRule());
        model.setOpponent(fiveOaBidApplyDto.getOpponent());
        model.setCooperationExperience(fiveOaBidApplyDto.getCooperationExperience());
        model.setTechnologyFeasibility(fiveOaBidApplyDto.getTechnologyFeasibility());
        model.setTechnologyFeasibilityOther(fiveOaBidApplyDto.getTechnologyFeasibilityOther());
        model.setProductFeasibility(fiveOaBidApplyDto.getProductFeasibility());
        model.setProductFeasibilityOther(fiveOaBidApplyDto.getProductFeasibilityOther());
        model.setOtherFeasibility(fiveOaBidApplyDto.getOtherFeasibility());
        model.setContractRisk(fiveOaBidApplyDto.getContractRisk());
        model.setWin(fiveOaBidApplyDto.getWin());

        model.setReviewUser(fiveOaBidApplyDto.getReviewUser());
        model.setReviewUserName(fiveOaBidApplyDto.getReviewUserName());
        model.setBidRank(fiveOaBidApplyDto.getBidRank());
        model.setChargeMan(fiveOaBidApplyDto.getChargeMan());
        model.setChargeManName(fiveOaBidApplyDto.getChargeManName());

        model.setProjectNo(fiveOaBidApplyDto.getProjectNo());
        model.setCustomerName(fiveOaBidApplyDto.getCustomerName());
        model.setCustomerCode(fiveOaBidApplyDto.getCustomerCode());

        model.setRecordId(fiveOaBidApplyDto.getRecordId());
        //如果选择项目备案
        if(fiveOaBidApplyDto.getRecordId()!=0){
            //如果原来有备案 还原
            if(model.getRecordId()!=0){
                BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
                businessRecord.setBidApplyId(0);
                businessRecordMapper.updateByPrimaryKey(businessRecord);
            }
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(fiveOaBidApplyDto.getRecordId());
            businessRecord.setBidApplyId(fiveOaBidApplyDto.getId());
            businessRecordMapper.updateByPrimaryKey(businessRecord);
            model.setRecordId(fiveOaBidApplyDto.getRecordId());
        }

        model.setFailReason(fiveOaBidApplyDto.getFailReason());
        model.setGmtModified(new Date());

        Map variables = Maps.newHashMap();
        variables.put("processDescription", "投标申请:"+model.getProjectName());
        variables.put("flag",model.getBidRank());//投标级别
       // variables.put("functionDeptOtherChargeMen",selectEmployeeService.getCompanyLeader());//职能管理部门副总
        variables.put("chargeMan",model.getChargeMan());//投标分管副总
        variables.put("reviewMen", MyStringUtil.getStringList(model.getReviewUser()));
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaBidApplyMapper.updateByPrimaryKey(model);

    }

    public FiveOaBidApplyDto getModelById(int id){

        return getDto(fiveOaBidApplyMapper.selectByPrimaryKey(id));
    }

    public FiveOaBidApplyDto getDto(FiveOaBidApply item) {
        FiveOaBidApplyDto dto=FiveOaBidApplyDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaBidApplyMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaBidApply item=new FiveOaBidApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setBidMan(hrEmployeeDto.getUserLogin());
        item.setBidManName(hrEmployeeDto.getUserName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setWin(true);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());
        item.setBidType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"投标方式").toString());
        item.setBidRank(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"投标级别").toString());
        //item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型"));
        item.setContractType("设计合同");

        //默认参评人员
        List<String> userLogins = hrEmployeeService.selectUserByRoleNames("合同章评审人");
        String reviewUser ="";
        String reviewUserName ="";
        for(String login:userLogins){
            HrEmployeeDto employee = hrEmployeeService.getModelByUserLogin(login);
            reviewUser = reviewUser+login+",";
            reviewUserName = reviewUserName+employee.getUserName()+",";
        }
        item.setReviewUser(reviewUser);
        item.setReviewUserName(reviewUserName);


        ModelUtil.setNotNullFields(item);
        fiveOaBidApplyMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_BID_APPLY+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "投标申请:"+item.getCreatorName());
        variables.put("businessLeader",selectEmployeeService.getDeptChargeMen(48));//经营发展部门负责人
        variables.put("deptChargeMen",selectEmployeeService.getDeptAllChargeMen(item.getDeptId()));//申请部门领导 正副职
        variables.put("functionDeptChargeMen",selectEmployeeService.getFunctionDeptChargeMen());//职能管理部门负责人
        variables.put("deptMenList",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//部门成员
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_BID_APPLY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaBidApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaBidApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaBidApply item=(FiveOaBidApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaBidApply item = fiveOaBidApplyMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("bidManName",item.getBidManName());
        data.put("bidTime",item.getBidTime());
        data.put("projectName",item.getProjectName());
        data.put("projectType",item.getProjectType());
        data.put("projectIndustry",item.getProjectIndustry());
        data.put("bidder",item.getBidder());
        data.put("informationSource",item.getInformationSource());
        data.put("contractMoney",item.getContractMoney());
        data.put("bidderLinkMan",item.getBidderLinkMan());
        data.put("bidderLinkTel",item.getBidderLinkTel());
        data.put("recordNo",item.getRecordNo());
        data.put("projectAddress",item.getProjectAddress());
        data.put("projectScale",item.getProjectScale());
        data.put("moneySource",item.getMoneySource());
        data.put("moneySourceOther",item.getMoneySourceOther());
        data.put("qualification",item.getQualification());
        data.put("qualificationOther",item.getQualificationOther());
        data.put("bidType",item.getBidType());
        data.put("bidTypeOther",item.getBidTypeOther());
        data.put("contractType",item.getContractType());
        data.put("contractTypeOther",item.getContractTypeOther());
        data.put("projectTime",item.getProjectTime());
        data.put("scheduleTarget",item.getScheduleTarget());
        data.put("bidScope",item.getBidScope());
        data.put("depositMoney",item.getDepositMoney());
        data.put("depositTime",item.getDepositTime());
        data.put("fileDataCost",item.getFileDataCost());
        data.put("paymentRule",item.getPaymentRule());
        data.put("cooperationExperience",item.getCooperationExperience());
        data.put("opponent",item.getOpponent());
        data.put("technologyFeasibility",item.getTechnologyFeasibility());
        data.put("technologyFeasibilityOther",item.getTechnologyFeasibilityOther());
        data.put("productFeasibility",item.getProductFeasibility());
        data.put("productFeasibilityOther",item.getProductFeasibilityOther());
        data.put("otherFeasibility",item.getOtherFeasibility());
        data.put("contractRisk",item.getContractRisk());
        if(item.getWin()){
            data.put("win","是");
        }else{
            data.put("win","否");
        }
        data.put("failReason",item.getFailReason());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("经营发展部-审批".equals(dto.getActivityName())){
                data.put("manageDevelopMen",dto);
            }
            if ("公司领导-审批".equals(dto.getActivityName())){
                data.put("companyLeader",dto);
            }
        }
        data.put("nodes",actHistoryDtos);



        return data;
    }
    /**
     * 五洲 查询当前登录人权限下 所有已完成的项目信息备案
     * @param userLogin
     * @return
     */
    public List<BusinessRecord> listRecordByUserLogin(String userLogin, int recordId){
        List<BusinessRecord> businessRecords =new ArrayList<>();
        //如果有备案 添加备案
        if(recordId!=0){
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(recordId);
            businessRecords.add(businessRecord);
        }
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,"five.businessRecord");
        params.put("deptIdList",deptIdList);
        params.put("processEnd",true);
        businessRecords.addAll(businessRecordMapper.selectAll(params).stream().filter(p ->p.getBidApplyId()==0).collect(Collectors.toList()));

        return businessRecords;
    }

    public String updateRecord(int applyId) {
        FiveOaBidApply apply = fiveOaBidApplyMapper.selectByPrimaryKey(applyId);
        if(apply.getRecordId()!=0){
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(apply.getRecordId());
            businessRecord.setAttend(true);
            businessRecord.setWin(apply.getWin());
        }
        return apply.getProjectName();
    }
}
