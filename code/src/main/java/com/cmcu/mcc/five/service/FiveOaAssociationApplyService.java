package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaAssociationApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaAssociationApplyDto;
import com.cmcu.mcc.five.entity.FiveOaAssociationApply;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaAssociationApplyService extends BaseService {
    @Resource
    FiveOaAssociationApplyMapper fiveOaAssociationApplyMapper;
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
    @Resource
    TaskHandleService taskHandleService;
    @Autowired
    TaskHandleService myTaskService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaAssociationApply item = fiveOaAssociationApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaAssociationApplyDto fiveOaAssociationApplyDto){
        FiveOaAssociationApply model = fiveOaAssociationApplyMapper.selectByPrimaryKey(fiveOaAssociationApplyDto.getId());

        model.setAssociationRole(fiveOaAssociationApplyDto.getAssociationRole());
        model.setDeptId(fiveOaAssociationApplyDto.getDeptId());
        model.setDeptName(fiveOaAssociationApplyDto.getDeptName());
        model.setAssociationNo(fiveOaAssociationApplyDto.getAssociationNo());
        model.setHandleMan(fiveOaAssociationApplyDto.getHandleMan());
        model.setHandleManName(fiveOaAssociationApplyDto.getHandleManName());
        model.setAssociationName(fiveOaAssociationApplyDto.getAssociationName());
        model.setDeptChargeName(fiveOaAssociationApplyDto.getDeptChargeName());
        model.setAssociationType(fiveOaAssociationApplyDto.getAssociationType());
        model.setAssociationLevel(fiveOaAssociationApplyDto.getAssociationLevel());
        model.setAssociationSummarize(fiveOaAssociationApplyDto.getAssociationSummarize());
        model.setAttendReason(fiveOaAssociationApplyDto.getAttendReason());
        model.setRecommendMan(fiveOaAssociationApplyDto.getRecommendMan());
        model.setRecommendManName(fiveOaAssociationApplyDto.getRecommendManName());
        model.setHoldRole(fiveOaAssociationApplyDto.getHoldRole());
        model.setLinkMan(fiveOaAssociationApplyDto.getLinkMan());
        model.setAssociationFee(fiveOaAssociationApplyDto.getAssociationFee());
        model.setRemark(fiveOaAssociationApplyDto.getRemark());
        model.setCompanyLeader(fiveOaAssociationApplyDto.getCompanyLeader());
        model.setCompanyLeaderName(fiveOaAssociationApplyDto.getCompanyLeaderName());
        model.setGmtModified(new Date());

        Map variables = Maps.newHashMap();
        if ("二类".equals(model.getAssociationLevel())){
            variables.put("flag", 0);
        }else {
            variables.put("flag", 1);
            //Assert.state(!Strings.isNullOrEmpty(model.getCompanyLeader()),"请填写公司领导");
            //variables.put("companyLeaderList", MyStringUtil.getStringList(model.getCompanyLeader()));
        }
        variables.put("deptChargeManLeader", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));//发起者部门领导
        variables.put("processDescription", "入协会申请表:"+model.getAssociationName());
        myActService.setVariables(model.getProcessInstanceId(),variables);

        BeanValidator.check(model);
        fiveOaAssociationApplyMapper.updateByPrimaryKey(model);
    }


    public FiveOaAssociationApplyDto getModelById(int id){
        return getDto(fiveOaAssociationApplyMapper.selectByPrimaryKey(id));
    }

    public FiveOaAssociationApplyDto getDto(FiveOaAssociationApply item) {
        FiveOaAssociationApplyDto dto=FiveOaAssociationApplyDto.adapt(item);
        dto.setProcessName("已完成");
        if(!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                item.setProcessEnd(true);
                fiveOaAssociationApplyMapper.updateByPrimaryKey(item);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName()))
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaAssociationApply item=new FiveOaAssociationApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);


        item.setAssociationRole(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"协会角色").toString());
        item.setAssociationLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"协会级别").toString());
        item.setAssociationType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"协会性质").toString());


        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setHandleMan(hrEmployeeDto.getUserLogin());
        item.setHandleManName(hrEmployeeDto.getUserName());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaAssociationApplyMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_ASSOCIATION_APPLY+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "入协会申请表:"+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        variables.put("qualityDeptMen",selectEmployeeService.selectUserByPositionNameAndDeptId("副部长",11));//信息化建设与管理部（副部长）
        variables.put("totalFinanceMen",hrEmployeeService.selectUserByPositionName("总经理"));//总经理

        variables.put("qualityDeptChargeMen",selectEmployeeService.getDeptChargeMen(11));//信息化建设与管理部负责人审批
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_ASSOCIATION_APPLY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaAssociationApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));


        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaAssociationApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaAssociationApply item=(FiveOaAssociationApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 查询所有协会申请信息
     * @return
     */
    public List<FiveOaAssociationApplyDto> listAssociation(){
        List<FiveOaAssociationApplyDto> relist=Lists.newArrayList();
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        List<FiveOaAssociationApply> list = fiveOaAssociationApplyMapper.selectAll(params);
        list.forEach(p ->{
            relist.add(getDto(p));
        });
        return relist;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaAssociationApply item = fiveOaAssociationApplyMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("associationNo",item.getAssociationNo());
        data.put("associationName",item.getAssociationName());
        data.put("handleManName",item.getHandleManName());
        data.put("deptName",item.getDeptName());
        data.put("deptChargeName",item.getDeptChargeName());
        data.put("associationType",item.getAssociationType());
        data.put("associationLevel",item.getAssociationLevel());
        data.put("recommendManName",item.getRecommendManName());
        data.put("holdRole",item.getHoldRole());
        data.put("linkMan",item.getLinkMan());
        data.put("associationFee",item.getAssociationFee());
        data.put("associationRole",item.getAssociationRole());
        data.put("associationSummarize",item.getAssociationSummarize());
        data.put("attendReason",item.getAttendReason());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

}
