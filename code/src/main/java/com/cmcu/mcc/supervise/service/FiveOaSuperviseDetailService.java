package com.cmcu.mcc.supervise.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseDetailMapper;
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseMapper;
import com.cmcu.mcc.supervise.dto.FiveOaSuperviseDetailDto;
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
public class  FiveOaSuperviseDetailService {
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
    @Resource
    FiveOaSuperviseMapper fiveOaSuperviseMapper;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaSuperviseDetail item = fiveOaSuperviseDetailMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaSuperviseDetailDto fiveOaSuperviseDto){
        FiveOaSuperviseDetail model = fiveOaSuperviseDetailMapper.selectByPrimaryKey(fiveOaSuperviseDto.getId());
        model.setTaskSource(fiveOaSuperviseDto.getTaskSource());
        model.setTaskDefinition(fiveOaSuperviseDto.getTaskDefinition());
        model.setTimeLimit(fiveOaSuperviseDto.getTimeLimit());
        model.setFeedbackTime(fiveOaSuperviseDto.getFeedbackTime());
        model.setTransactionPlan(fiveOaSuperviseDto.getTransactionPlan());//办理进度
        model.setSuperviseType(fiveOaSuperviseDto.getSuperviseType());
        model.setTransactor(fiveOaSuperviseDto.getTransactor());
        model.setTransactorName(fiveOaSuperviseDto.getTransactorName());
        model.setCompanyLeaderOpinion(fiveOaSuperviseDto.getCompanyLeaderOpinion());

        model.setCreator(fiveOaSuperviseDto.getCreator());
        model.setCreatorName(fiveOaSuperviseDto.getCreatorName());
        model.setDeptId(fiveOaSuperviseDto.getDeptId());
        model.setDeptName(fiveOaSuperviseDto.getDeptName());
        model.setRemark(fiveOaSuperviseDto.getRemark());
        model.setGmtModified(new Date());
        fiveOaSuperviseDetailMapper.updateByPrimaryKey(model);



        Map variables = Maps.newHashMap();

        if(!"".equals(fiveOaSuperviseDto.getTransactor())) {
            variables.put("transactor", MyStringUtil.getStringList(fiveOaSuperviseDto.getTransactor()));
        }
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));

        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaSuperviseDetailDto getModelById(int id){

        return getDto(fiveOaSuperviseDetailMapper.selectByPrimaryKey(id));
    }

    public FiveOaSuperviseDetailDto getDto(FiveOaSuperviseDetail item) {
        FiveOaSuperviseDetailDto dto=FiveOaSuperviseDetailDto.adapt(item);

        MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        dto.setProcessName(processInstanceDto.getProcessName());

        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            dto.setProcessEnd(true);
            fiveOaSuperviseDetailMapper.updateByPrimaryKey(dto);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    public int getNewModelById(String userLogin,int superviseId){
        FiveOaSuperviseDetail item=new FiveOaSuperviseDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        FiveOaSupervise fiveOaSupervise = fiveOaSuperviseMapper.selectByPrimaryKey(superviseId);
        item.setSuperviseId(superviseId);
        item.setSuperviseType(fiveOaSupervise.getSuperviseType());

        item.setFeedbackTime(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"反馈周期").toString());
        item.setTransactionPlan(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"办理进度").toString());
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

        fiveOaSuperviseDetailMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_SUPERVISE_DETAIL+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "督办子项："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SUPERVISE_DETAIL,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaSuperviseDetailMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaSuperviseDetailMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaSuperviseDetail item=(FiveOaSuperviseDetail)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public List<FiveOaSuperviseDetailDto> listDate(int superviseId){
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("superviseId",superviseId);
        List<FiveOaSuperviseDetail> fiveOaSuperviseDetails = fiveOaSuperviseDetailMapper.selectAll(map);
        List<FiveOaSuperviseDetailDto> list=Lists.newArrayList();
        fiveOaSuperviseDetails.forEach(p->{
            list.add(getDto(p));
        });
        return list;
    }

}
