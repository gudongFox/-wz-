package com.cmcu.mcc.supervise.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
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
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseFileMapper;
import com.cmcu.mcc.supervise.dto.FiveOaSuperviseFileDto;
import com.cmcu.mcc.supervise.entity.FiveOaSuperviseFile;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaSuperviseFileService extends BaseService {
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
    ActService actService;
    @Autowired
    FiveActRelevanceService fiveActRelevanceService;
    @Autowired
    FiveOaFileInstructionService fiveOaFileInstructionService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveOaSuperviseFileMapper fiveOaSuperviseFileMapper;

    public void remove(int id,String userLogin){

        FiveOaSuperviseFile item = fiveOaSuperviseFileMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaSuperviseFileDto fiveOaSuperviseFileDto){
        FiveOaSuperviseFile model = fiveOaSuperviseFileMapper.selectByPrimaryKey(fiveOaSuperviseFileDto.getId());
        model.setFileHeader(fiveOaSuperviseFileDto.getFileHeader());
        model.setSuperviseType(fiveOaSuperviseFileDto.getSuperviseType());
        model.setFeedbackTime(fiveOaSuperviseFileDto.getFeedbackTime());
        model.setCompanyLeader(fiveOaSuperviseFileDto.getCompanyLeader());
        model.setCompanyLeaderName(fiveOaSuperviseFileDto.getCompanyLeaderName());
        model.setCompanyLeaderOpinion(fiveOaSuperviseFileDto.getCompanyLeaderOpinion());
        model.setCreator(fiveOaSuperviseFileDto.getCreator());
        model.setCreatorName(fiveOaSuperviseFileDto.getCreatorName());
        model.setDeptId(fiveOaSuperviseFileDto.getDeptId());
        model.setDeptName(fiveOaSuperviseFileDto.getDeptName());
        model.setRemark(fiveOaSuperviseFileDto.getRemark());
        model.setGmtModified(new Date());
        model.setTransactionPlan(fiveOaSuperviseFileDto.getTransactionPlan());
        model.setTransactor(fiveOaSuperviseFileDto.getTransactor());
        model.setTransactorName(fiveOaSuperviseFileDto.getTransactorName());
        model.setTaskDefinition(fiveOaSuperviseFileDto.getTaskDefinition());
        model.setTransactorOpinion(fiveOaSuperviseFileDto.getTransactorOpinion());
        model.setTimeLimit(fiveOaSuperviseFileDto.getTimeLimit());
        fiveOaSuperviseFileMapper.updateByPrimaryKey(model);

        //取流程到选择的办理人
        Map variables = Maps.newHashMap();
        variables.put("transactorList", MyStringUtil.getStringList(fiveOaSuperviseFileDto.getTransactor()));//处理人
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(model.getDeptId()));
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaSuperviseFileDto getModelById(int id){

        return getDto(fiveOaSuperviseFileMapper.selectByPrimaryKey(id));
    }

    public FiveOaSuperviseFileDto getDto(FiveOaSuperviseFile item) {
        FiveOaSuperviseFileDto dto=FiveOaSuperviseFileDto.adapt(item);

        MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        dto.setProcessName(processInstanceDto.getProcessName());

        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            dto.setProcessEnd(true);
            fiveOaSuperviseFileMapper.updateByPrimaryKey(dto);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaSuperviseFile item=new FiveOaSuperviseFile();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setSuperviseType("文件督办");
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());

        ModelUtil.setNotNullFields(item);

        fiveOaSuperviseFileMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_SUPERVISE_FILE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "文件督办："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SUPERVISE_FILE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaSuperviseFileMapper.updateByPrimaryKey(item);
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
        FiveOaSuperviseFile item=new FiveOaSuperviseFile();
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

        fiveOaSuperviseFileMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_SUPERVISE_FILE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "文件督办："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SUPERVISE_FILE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaSuperviseFileMapper.updateByPrimaryKey(item);

        //互相关联流程
        fiveActRelevanceService.eachRelevance(processInstanceId,businessId,userLogin);

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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaSuperviseFileMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaSuperviseFile item=(FiveOaSuperviseFile)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
