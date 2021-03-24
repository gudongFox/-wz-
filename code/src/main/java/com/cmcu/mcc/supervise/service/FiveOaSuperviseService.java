package com.cmcu.mcc.supervise.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.service.FiveActRelevanceService;
import com.cmcu.mcc.five.service.FiveOaFileInstructionService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseDetailMapper;
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseMapper;
import com.cmcu.mcc.supervise.dto.FiveOaSuperviseDto;
import com.cmcu.mcc.supervise.entity.FiveOaSupervise;
import com.cmcu.mcc.supervise.entity.FiveOaSuperviseDetail;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaSuperviseService {
    @Resource
    FiveOaSuperviseMapper fiveOaSuperviseMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveOaSuperviseDetailMapper fiveOaSuperviseDetailMapper;
    @Autowired
    ActService actService;
    @Autowired
    FiveActRelevanceService fiveActRelevanceService;
    @Autowired
    FiveOaFileInstructionService fiveOaFileInstructionService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){

        FiveOaSupervise item = fiveOaSuperviseMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaSuperviseDto fiveOaSuperviseDto){
        FiveOaSupervise model = fiveOaSuperviseMapper.selectByPrimaryKey(fiveOaSuperviseDto.getId());
        model.setFileHeader(fiveOaSuperviseDto.getFileHeader());
        model.setSuperviseType(fiveOaSuperviseDto.getSuperviseType());
        model.setFeedbackTime(fiveOaSuperviseDto.getFeedbackTime());
        model.setCompanyLeader(fiveOaSuperviseDto.getCompanyLeader());
        model.setCompanyLeaderName(fiveOaSuperviseDto.getCompanyLeaderName());
        model.setCompanyLeaderOpinion(fiveOaSuperviseDto.getCompanyLeaderOpinion());
        model.setCreator(fiveOaSuperviseDto.getCreator());
        model.setCreatorName(fiveOaSuperviseDto.getCreatorName());
        model.setDeptId(fiveOaSuperviseDto.getDeptId());
        model.setDeptName(fiveOaSuperviseDto.getDeptName());
        model.setRemark(fiveOaSuperviseDto.getRemark());
        model.setGmtModified(new Date());
        fiveOaSuperviseMapper.updateByPrimaryKey(model);

        //取流程到选择的办理人
        Map variables = Maps.newHashMap();
        if(!"".equals(fiveOaSuperviseDto.getCompanyLeader())) {
            variables.put("companyLeader", MyStringUtil.getStringList(fiveOaSuperviseDto.getCompanyLeader()));//发起者部门领导
        }
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaSuperviseDto getModelById(int id){

        return getDto(fiveOaSuperviseMapper.selectByPrimaryKey(id));
    }

    public FiveOaSuperviseDto getDto(FiveOaSupervise item) {
        FiveOaSuperviseDto dto=FiveOaSuperviseDto.adapt(item);

        MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        dto.setProcessName(processInstanceDto.getProcessName());

        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            dto.setProcessEnd(true);
            fiveOaSuperviseMapper.updateByPrimaryKey(dto);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }


    public int getNewModel(String userLogin,String superviseType){
        FiveOaSupervise item=new FiveOaSupervise();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setSuperviseType(superviseType);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());

        ModelUtil.setNotNullFields(item);

        fiveOaSuperviseMapper.insert(item);


        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));

        String businessKey;
        String processInstanceId;
        if (superviseType.equals("常规督办")){
            businessKey= EdConst.FIVE_OA_SUPERVISE+ "_" + item.getId();
            variables.put("processDescription", "常规督办："+item.getCreatorName());
            processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SUPERVISE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        }else {
            businessKey= EdConst.FIVE_OA_SUPERVISE_YEAR+ "_" + item.getId();
            variables.put("processDescription", "年度重点任务督办："+item.getCreatorName());
            processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SUPERVISE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        }
        item.setBusinessKey(businessKey);

        item.setProcessInstanceId(processInstanceId);
        fiveOaSuperviseMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * 发起一个新的督办任务
     * @param userLogin 督办人
     * @param superviseType 督办类型
     * @param companyLeader 公司领导
     * @param businessId 主流程关键字
     * @param companyLeader 文件标题
     * @param businessId 批示领导意见
     *
     * @return
     */
    public int getNewModelByType(String userLogin,String superviseType,String companyLeader,String businessId,String fileTitle,String view){
        FiveOaSupervise item=new FiveOaSupervise();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setSuperviseType(superviseType);
        item.setCompanyLeader(companyLeader);
        item.setCompanyLeaderName(selectEmployeeService.getNameByUserLogin(companyLeader));
        item.setFileHeader(fileTitle);
        item.setCompanyLeaderOpinion(view);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaSuperviseMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_SUPERVISE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "督办管理："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SUPERVISE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaSuperviseMapper.updateByPrimaryKey(item);

        //互相关联流程
        fiveActRelevanceService.eachRelevance(processInstanceId,businessId,userLogin);

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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaSuperviseMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaSupervise item=(FiveOaSupervise)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveOaSuperviseDetail item){
        FiveOaSuperviseDetail model = fiveOaSuperviseDetailMapper.selectByPrimaryKey(item.getId());
        model.setTaskSource(item.getTaskSource());
        model.setTaskDefinition(item.getTaskDefinition());
        model.setTaskSource(item.getTaskSource());
        model.setTimeLimit(item.getTimeLimit());
        model.setSuperviseType(item.getSuperviseType());
        model.setTransactionPlan(item.getTransactionPlan());
        model.setFeedbackTime(item.getFeedbackTime());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setRemark(item.getRemark());
        fiveOaSuperviseDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaSuperviseDetail getNewModelDetail(int  id){
        FiveOaSuperviseDetail item=new FiveOaSuperviseDetail();
        item.setSuperviseId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaSuperviseDetailMapper.insert(item);
        return item;
    }

    public FiveOaSuperviseDetail getModelDetailById(int id){
        return fiveOaSuperviseDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaSuperviseDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("superviseId",id);//小写
        List<FiveOaSuperviseDetail> list = fiveOaSuperviseDetailMapper.selectAll(params);
        return list;
    }

    public void removeDetail(int id){
        FiveOaSuperviseDetail model = fiveOaSuperviseDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaSuperviseDetailMapper.updateByPrimaryKey(model);
    }

    public List<FiveOaSupervise> listPageTask(String userLogin){
        Map <String,Object> params=Maps.newHashMap();
        params.put("processEnd",false);
        params.put("deleted",false);
        params.put("creator",userLogin);//小写
        List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
        return list;
    }

}
