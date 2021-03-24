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
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaGoAbroadTrainAskDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaGoAbroadTrainAskMapper;
import com.cmcu.mcc.five.dto.FiveOaGoAbroadTrainAskDto;
import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainAsk;
import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainAskDetail;
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
public class FiveOaGoAbroadTrainAskService extends BaseService {
    @Resource
    FiveOaGoAbroadTrainAskMapper fiveOaGoAbroadTrainAskMapper;
    @Resource
    FiveOaGoAbroadTrainAskDetailMapper fiveOaGoAbroadTrainAskDetailMapper;
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
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaGoAbroadTrainAsk item = fiveOaGoAbroadTrainAskMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaGoAbroadTrainAskDto item){
        FiveOaGoAbroadTrainAsk model = fiveOaGoAbroadTrainAskMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setApplyTime(item.getApplyTime());
        model.setAskTitle(item.getAskTitle());
        model.setGoAbroadTitle(item.getGoAbroadTitle());
        model.setTrainName(item.getTrainName());
        model.setStaffName(item.getStaffName());
        model.setStaffCost(item.getStaffCost());
        model.setStaffPlace(item.getStaffPlace());
        model.setGmtModified(new Date());

        model.setRemark(item.getRemark());
        Map variables = Maps.newHashMap();
        variables.put("processDescription","参见出国培训请示:"+ model.getAskTitle());
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaGoAbroadTrainAskMapper.updateByPrimaryKey(model);
    }

    public FiveOaGoAbroadTrainAskDto getModelById(int id){

        return getDto(fiveOaGoAbroadTrainAskMapper.selectByPrimaryKey(id));
    }

    public FiveOaGoAbroadTrainAskDto getDto(FiveOaGoAbroadTrainAsk item) {
        FiveOaGoAbroadTrainAskDto dto=FiveOaGoAbroadTrainAskDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaGoAbroadTrainAskMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaGoAbroadTrainAsk item=new FiveOaGoAbroadTrainAsk();
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
        fiveOaGoAbroadTrainAskMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","参见出国培训请示:"+ item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
/*         variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//经营发展部领导
        variables.put("opratorLeader", selectEmployeeService.getDeptChargeMen(29));//运营部领导*/

        variables.put("qualityDeptChargeMen",selectEmployeeService.getDeptChargeMen(11));//信息化建设与管理部负责人
        variables.put("qualityDeptOtherChargeMen",selectEmployeeService.getOtherDeptChargeMan(11));//信息化建设与管理部分管领导

        String businessKey=EdConst.FIVE_OA_GOABROADTRAINASK + "_"+item.getId();
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_GOABROADTRAINASK, businessKey , variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaGoAbroadTrainAskMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaGoAbroadTrainAskMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaGoAbroadTrainAsk item=(FiveOaGoAbroadTrainAsk)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaGoAbroadTrainAskDetail model = fiveOaGoAbroadTrainAskDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaGoAbroadTrainAskDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaGoAbroadTrainAskDetail item){
        FiveOaGoAbroadTrainAskDetail model = fiveOaGoAbroadTrainAskDetailMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setAttendUser(item.getAttendUser());
        model.setAttendUserName(item.getAttendUserName());
        model.setTrainName(item.getTrainName());

        model.setRemark(item.getRemark());
        fiveOaGoAbroadTrainAskDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaGoAbroadTrainAskDetail getNewModelDetail(int  id){
        FiveOaGoAbroadTrainAskDetail item=new FiveOaGoAbroadTrainAskDetail();
        item.setGoAbroadTrainAskId(id+"");
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaGoAbroadTrainAskDetailMapper.insert(item);
        return item;

    }

    public FiveOaGoAbroadTrainAskDetail getModelDetailById(int id){
        return fiveOaGoAbroadTrainAskDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaGoAbroadTrainAskDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("goAbroadTrainAskId",id);
        List<FiveOaGoAbroadTrainAskDetail> list = fiveOaGoAbroadTrainAskDetailMapper.selectAll(params);
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaGoAbroadTrainAsk item = fiveOaGoAbroadTrainAskMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("applyTime",item.getApplyTime());
        data.put("askTitle",item.getGoAbroadTitle());
        data.put("goAbroadTitle",item.getGoAbroadTitle());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("goAbroadTrainAskId",item.getId());
        map.put("deleted",false);
        List<FiveOaGoAbroadTrainAskDetail> goAbroadTrainAskDetails = fiveOaGoAbroadTrainAskDetailMapper.selectAll (map);
        data.put("goAbroadTrainAskDetails",goAbroadTrainAskDetails);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("专家委-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("信息化建设与管理部分管领导".equals(dto.getActivityName())){
                data.put("informationTechnologyLeader ",dto);
            }
            if ("信息化建设与管理部负责人".equals(dto.getActivityName())){
                data.put("informationTechnologyChargeMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

    public List<FiveOaGoAbroadTrainAskDto> listAssociation() {
        List<FiveOaGoAbroadTrainAskDto> relist=Lists.newArrayList();
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        List<FiveOaGoAbroadTrainAsk> list = fiveOaGoAbroadTrainAskMapper.selectAll(params);
        list.forEach(p ->{
            relist.add(getDto(p));
        });
        return relist;
    }
}
