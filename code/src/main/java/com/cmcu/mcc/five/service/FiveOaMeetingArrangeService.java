package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.five.dao.FiveOaMeetingArrangeMapper;
import com.cmcu.mcc.five.dao.FiveOaMeetingRoomMapper;
import com.cmcu.mcc.five.dto.FiveOaMeetingArrangeDto;
import com.cmcu.mcc.five.entity.FiveOaMeetingArrange;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
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
import java.text.ParseException;
import java.util.*;

@Service
public class FiveOaMeetingArrangeService {

    @Resource
    FiveOaMeetingArrangeMapper fiveOaMeetingArrangeMapper;
    @Autowired
    FiveOaMeetingRoomService fiveOaMeetingRoomService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    ActService actService;
    @Autowired
    FiveOaMeetingRoomMapper fiveOaMeetingRoomMapper;
    @Resource
    TaskHandleService taskHandleService;

    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    HandleFormService handleFormService;
    @Autowired
    MyActService myActService;


    public void remove(int id,String userLogin) {
        FiveOaMeetingArrange item = fiveOaMeetingArrangeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaMeetingArrangeDto dto) throws ParseException {

        FiveOaMeetingArrange item = fiveOaMeetingArrangeMapper.selectByPrimaryKey(dto.getId());
        item.setDeptName(dto.getDeptName());
        item.setMeetingName(dto.getMeetingName());
        item.setHostMan(dto.getHostMan());
        item.setHostManName(dto.getHostManName());
        item.setChargeLeader(dto.getChargeLeader());
        item.setChargeLeaderName(dto.getChargeLeaderName());
        item.setAttendUser(dto.getAttendUser());
        item.setAttendUserName(dto.getAttendUserName());
        item.setMeetingDes(dto.getMeetingDes());
        item.setMeetingTime(dto.getMeetingTime());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        item.setRoom(dto.getRoom());
        BeanValidator.check(item);

        fiveOaMeetingArrangeMapper.updateByPrimaryKey(item);

        List<String> attendUsers = new ArrayList<>();
        List<String> attendUser= MyStringUtil.getStringList(item.getAttendUser());
        List<String> chargeLeader = MyStringUtil.getStringList(item.getChargeLeader());
        List<String> hostMan= MyStringUtil.getStringList(item.getHostMan());
        attendUsers.addAll(attendUser);
        attendUsers.addAll(chargeLeader);
        attendUsers.addAll(hostMan);
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("attendUsers",attendUsers);
        myActService.setVariables(item.getProcessInstanceId(),variables);
    }


    public FiveOaMeetingArrangeDto getModelById(int id) {
        return getDto(fiveOaMeetingArrangeMapper.selectByPrimaryKey(id));
    }

    public FiveOaMeetingArrangeDto getDto(FiveOaMeetingArrange item) {
        FiveOaMeetingArrangeDto dto = FiveOaMeetingArrangeDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                item.setProcessEnd(true);
                fiveOaMeetingArrangeMapper.updateByPrimaryKey(item);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);   //创建者信息
        FiveOaMeetingArrange item = new FiveOaMeetingArrange();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setMeetingTime(MyDateUtil.getStringToday());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        fiveOaMeetingArrangeMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin",userLogin);
        variables.put("processDescription","会议安排："+item.getCreatorName());
        String processInstanceId =taskHandleService.startProcess(EdConst.FIVE_OA_MEETING_ARRANGE, EdConst.FIVE_OA_MEETING_ARRANGE+'_'+item.getId(),variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(EdConst.FIVE_OA_MEETING_ARRANGE+'_'+item.getId());
        fiveOaMeetingArrangeMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaMeetingArrangeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaMeetingArrange item=(FiveOaMeetingArrange)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
