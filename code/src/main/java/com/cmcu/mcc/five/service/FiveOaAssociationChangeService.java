package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaAssociationApplyMapper;
import com.cmcu.mcc.five.dao.FiveOaAssociationChangeMapper;
import com.cmcu.mcc.five.dto.FiveOaAssociationChangeDto;
import com.cmcu.mcc.five.entity.FiveOaAssociationApply;
import com.cmcu.mcc.five.entity.FiveOaAssociationChange;
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
public class FiveOaAssociationChangeService extends BaseService {
    @Resource
    FiveOaAssociationChangeMapper fiveOaAssociationChangeMapper;
    @Resource
    FiveOaAssociationApplyMapper fiveOaAssociationApplyMapper;
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
    TaskHandleService myTaskService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaAssociationChange item = fiveOaAssociationChangeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaAssociationChangeDto dto){
        FiveOaAssociationChange model = fiveOaAssociationChangeMapper.selectByPrimaryKey(dto.getId());
        model.setChangeContent(dto.getChangeContent());
        model.setChangeItem(dto.getChangeItem());
        model.setGmtModified(new Date());
        BeanValidator.check(model);
        fiveOaAssociationChangeMapper.updateByPrimaryKey(model);
    }

    public FiveOaAssociationChangeDto getModelById(int id){

        return getDto(fiveOaAssociationChangeMapper.selectByPrimaryKey(id));
    }
    public FiveOaAssociationChangeDto getDto(FiveOaAssociationChange item) {
        FiveOaAssociationChangeDto dto=FiveOaAssociationChangeDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaAssociationChangeMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaAssociationChange item=new FiveOaAssociationChange();
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

        fiveOaAssociationChangeMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_ASSOCIATION_CHANGE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "协会信息变更:"+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导

        variables.put("qualityDeptMen",selectEmployeeService.selectUserByPositionNameAndDeptId("副部长",11));//信息化建设与管理部（副部长）
        variables.put("totalFinanceMen",hrEmployeeService.selectUserByPositionName("总经理"));//总经理


        item.setBusinessKey(businessKey);
        String processInstanceId = myTaskService.startProcess(EdConst.FIVE_OA_ASSOCIATION_CHANGE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaAssociationChangeMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaAssociationChangeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaAssociationChange item=(FiveOaAssociationChange)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void saveAssociation(int applyId,int id){
        FiveOaAssociationApply apply = fiveOaAssociationApplyMapper.selectByPrimaryKey(applyId);
        FiveOaAssociationChange change = fiveOaAssociationChangeMapper.selectByPrimaryKey(id);
        change.setApplyId(apply.getId());
        change.setDeptId(apply.getDeptId());
        change.setDeptName(apply.getDeptName());
        change.setAssociationName(apply.getAssociationName());
        change.setAssociationLevel(apply.getAssociationLevel());
        change.setRemark(apply.getRemark());
        Map variables = Maps.newHashMap();
        if ("二类".equals(change.getAssociationLevel())){
            variables.put("flag", 0);
        }else {
            variables.put("flag", 1);
            variables.put("companyLeaderList",selectEmployeeService.getOtherDeptChargeMan(change.getDeptId()));
        }
        variables.put("processDescription", "协会信息变更:"+change.getAssociationName());

        myActService.setVariables(change.getProcessInstanceId(),variables);

        //修改
        fiveOaAssociationChangeMapper.updateByPrimaryKey(change);
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaAssociationChange item = fiveOaAssociationChangeMapper.selectByPrimaryKey(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("gmtCreate",simpleDateFormat.format(item.getGmtCreate()));

        data.put("creatorName",item.getCreatorName());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

}
