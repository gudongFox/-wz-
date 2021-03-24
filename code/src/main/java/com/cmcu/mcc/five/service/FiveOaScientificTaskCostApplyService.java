package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaScientificTaskCostApplyDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaScientificTaskCostApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaScientificTaskCostApplyDto;
import com.cmcu.mcc.five.entity.FiveOaScientificTaskCostApply;
import com.cmcu.mcc.five.entity.FiveOaScientificTaskCostApplyDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaScientificTaskCostApplyService {
    @Resource
    FiveOaScientificTaskCostApplyMapper fiveOaScientificTaskCostApplyMapper;
    @Resource
    FiveOaScientificTaskCostApplyDetailMapper fiveOaScientificTaskCostApplyDetailMapper;
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
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaScientificTaskCostApply item = fiveOaScientificTaskCostApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaScientificTaskCostApplyDto item){
        FiveOaScientificTaskCostApply model = fiveOaScientificTaskCostApplyMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setTaskName(item.getTaskName());
        model.setTaskNo(item.getTaskNo());
        model.setTaskChargeMan(item.getTaskChargeMan());
        model.setTaskChargeManName(item.getTaskChargeManName());
        model.setCostUse(item.getCostUse());
        model.setTaskCostPlan(item.getTaskCostPlan());
        model.setCostUseTime(item.getCostUseTime());


        model.setDeptChargeMen(item.getDeptChargeMen());
        model.setDeptChargeMenName(item.getDeptChargeMenName());

        model.setTechnologyMan(item.getTechnologyMan());
        model.setTechnologyManName(item.getTechnologyManName());
        model.setTechnologyManComment(item.getTechnologyManComment());

        model.setChargeLeaderMan(item.getChargeLeaderMan());
        model.setChargeLeaderManName(item.getChargeLeaderManName());
        model.setChargeLeaderManComment(item.getChargeLeaderManComment());

        model.setTotalAccountantMen(item.getTotalAccountantMen());
        model.setTotalAccountantMenName(item.getTotalAccountantMenName());
        model.setTotalAccountantMenComment(item.getTotalAccountantMenComment());

        model.setTotalManagerMen(item.getTotalManagerMen());
        model.setTotalManagerMenName(item.getTotalManagerMenName());
        model.setTotalManagerMenComment(item.getTotalManagerMenComment());

        model.setRemark(item.getRemark());
        Map variables = Maps.newHashMap();

        variables.put("processDescription","科研课题费用申请:"+ model.getTaskName());
        Assert.state(!Strings.isNullOrEmpty(model.getTaskChargeMan()),"请先填写 课题负责人");
        variables.put("taskChargeMan", MyStringUtil.getStringList(model.getTaskChargeMan()));
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaScientificTaskCostApplyMapper.updateByPrimaryKey(model);
    }

    public FiveOaScientificTaskCostApplyDto getModelById(int id){

        return getDto(fiveOaScientificTaskCostApplyMapper.selectByPrimaryKey(id));
    }

    public FiveOaScientificTaskCostApplyDto getDto(FiveOaScientificTaskCostApply item) {
        FiveOaScientificTaskCostApplyDto dto=FiveOaScientificTaskCostApplyDto.adapt(item);

        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaScientificTaskCostApplyMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        List<String> totalManger = hrEmployeeService.selectUserByPositionName("总经理");
        List<String> totalAccount = hrEmployeeService.selectUserByPositionName("总会计师");

        FiveOaScientificTaskCostApply item=new FiveOaScientificTaskCostApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        //总经理 总会计师 技术质量部负责人
        item.setTotalManagerMen(StringUtils.join(totalManger,","));
        item.setTotalManagerMenName(selectEmployeeService.getNameByUserLogins(item.getTotalManagerMen()));
        item.setTotalAccountantMen(StringUtils.join(totalAccount,","));
        item.setTotalAccountantMenName(selectEmployeeService.getNameByUserLogins(item.getTotalAccountantMen()));

        item.setTechnologyMan(StringUtils.join(selectEmployeeService.getDeptChargeMen(11),","));
        item.setTechnologyManName(selectEmployeeService.getNameByUserLogins(item.getTechnologyMan()));


        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setTaskCostPlan("否");
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaScientificTaskCostApplyMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","科研课题费用申请:"+ item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        variables.put("scientificDeptChargeMen",selectEmployeeService.getDeptChargeMen(101));//科研与技术质量部负责人
        variables.put("scientificDeptOtherChargeMen",selectEmployeeService.getOtherDeptChargeMan(101));//科研与技术质量部负分管副总
        String businessKey=EdConst.FIVE_OA_SCIENTIFICTASKCOSTAPPLY+"_"+item.getId();
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SCIENTIFICTASKCOSTAPPLY,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaScientificTaskCostApplyMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaScientificTaskCostApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaScientificTaskCostApply item=(FiveOaScientificTaskCostApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaScientificTaskCostApplyDetail model = fiveOaScientificTaskCostApplyDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaScientificTaskCostApplyDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaScientificTaskCostApplyDetail item){
        FiveOaScientificTaskCostApplyDetail model = fiveOaScientificTaskCostApplyDetailMapper.selectByPrimaryKey(item.getId());
        model.setItem(item.getItem());
        model.setPrice(item.getPrice());
        model.setRemark(item.getRemark());
        fiveOaScientificTaskCostApplyDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaScientificTaskCostApplyDetail getNewModelDetail(int  id){
        FiveOaScientificTaskCostApplyDetail item=new FiveOaScientificTaskCostApplyDetail();
        item.setScientificTaskCostApplyId(id+"");
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaScientificTaskCostApplyDetailMapper.insert(item);
        return item;

    }

    public FiveOaScientificTaskCostApplyDetail getModelDetailById(int id){
        return fiveOaScientificTaskCostApplyDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaScientificTaskCostApplyDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("scientificTaskCostApplyId",id);
        List<FiveOaScientificTaskCostApplyDetail> list = fiveOaScientificTaskCostApplyDetailMapper.selectAll(params);
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaScientificTaskCostApply item = fiveOaScientificTaskCostApplyMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("taskName",item.getTaskName());
        data.put("taskNo",item.getTaskNo());
        data.put("taskCostPlan",item.getTaskCostPlan());
        data.put("costUse",item.getCostUse());
        data.put("deptName",item.getDeptName());
        data.put("costUseTime",item.getCreatorName());
        data.put("taskChargeManName",item.getTaskChargeManName());
        data.put("deptChargeMenName",item.getDeptChargeMenName());
        data.put("technologyManName",item.getTechnologyManName());
        data.put("chargeLeaderManName",item.getChargeLeaderManName());
        data.put("totalAccountantMenName",item.getTotalAccountantMenName());
        data.put("totalManagerMenName",item.getTotalManagerMenName());
        data.put("technologyManComment",item.getTechnologyManComment());
        data.put("chargeLeaderManComment",item.getChargeLeaderManComment());
        data.put("totalAccountantMenComment",item.getTotalAccountantMenComment());
        data.put("totalManagerMenComment",item.getTotalManagerMenComment());

        data.put("creatorName",item.getCreatorName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        data.put("gmtCreate",formatter.format(item.getGmtCreate()));

        Map map =new HashMap();
        map.put("scientificTaskCostApplyId",item.getId());
        map.put("deleted",false);
        List<FiveOaScientificTaskCostApplyDetail> scientificTaskCostApplyDetails = fiveOaScientificTaskCostApplyDetailMapper.selectAll (map);
        data.put("scientificTaskCostApplyDetails",scientificTaskCostApplyDetails);
        Double sum=0.0d;
        int count=0;
        for (FiveOaScientificTaskCostApplyDetail detail:scientificTaskCostApplyDetails) {
             count++;
             sum+=Double.valueOf(detail.getPrice());
        }
        data.put("sum",sum);
        data.put("count",count);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("课题负责人-审批".equals(dto.getActivityName())){
                data.put("taskChargeMen",dto);
            }
            if ("单位负责人-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("科研与技术质量部负责人".equals(dto.getActivityName())){
                data.put("scientificChargeMen",dto);
            }
            if ("科研与技术质量部分管副总".equals(dto.getActivityName())){
                data.put("scientificVicePresident ",dto);
            }
            if ("总会计师".equals(dto.getActivityName())){
                data.put("chiefAccountant",dto);
            }
            if ("总经理".equals(dto.getActivityName())){
                data.put("generalManager",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}
