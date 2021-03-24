package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.sys.dao.SysScheduleMapper;
import com.cmcu.mcc.sys.dto.SysScheduleDto;
import com.cmcu.mcc.sys.entity.SysSchedule;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SysScheduleService {
    @Resource
    SysScheduleMapper sysScheduleMapper;
    @Resource
    SelectEmployeeService selectEmployeeService;

    public void remove(int id,String userLogin){
        SysSchedule item = sysScheduleMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        item.setDeleted(true);
        item.setGmtModified(new Date());
        sysScheduleMapper.updateByPrimaryKey(item);
    }

    public SysSchedule getNewModel(String userLogin) {
        SysSchedule item = new SysSchedule();
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        return item;
    }

    public SysScheduleDto getModelById(int id){
        return getDto(sysScheduleMapper.selectByPrimaryKey(id));
    }

    public SysScheduleDto getDto(SysSchedule sysSchedule){
        SysScheduleDto dto = SysScheduleDto.adapt(sysSchedule);

        return dto;
    }
    public void update(SysScheduleDto model){
        ModelUtil.setNotNullFields(model);
        SysSchedule item = sysScheduleMapper.selectByPrimaryKey(model.getId());
        if(!Strings.isNullOrEmpty(model.getStart())){
            item.setStart(model.getStart());
        }
        if(!Strings.isNullOrEmpty(model.getEnd())){
            item.setEnd(model.getEnd());
        }
        if(!Strings.isNullOrEmpty(model.getTitle())){
            item.setTitle(model.getTitle());
        }
        item.setType(model.getType());
        switch (model.getType()){
            case "个人日程":
                item.setBackgroundColor("#1bbc9b");
                break;
            case "系统日程":
                item.setBackgroundColor("#F8CB00");
                break;
            case "其他日程":
                item.setBackgroundColor("#95a5a6");
                break;
        }

        item.setAllDay(model.getAllDay());
        item.setRemark(model.getRemark());
        item.setGmtModified(new Date());
        sysScheduleMapper.updateByPrimaryKey(item);

    }

    public List<SysScheduleDto> selectAll(String userLogin) {
        List<SysScheduleDto> dtos = new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("userLogin",userLogin);
        List<SysSchedule> sysSchedules = sysScheduleMapper.selectAll(map);
        for(SysSchedule sysSchedule:sysSchedules){
            SysScheduleDto dto = getDto(sysSchedule);
            dtos.add(dto);
        }
        return dtos;
    }

    public SysSchedule insert(String start,String end,String type,String title,String userLogin) {
        SysSchedule item = new SysSchedule();
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        item.setUserLogin(userLogin);
        item.setUserName(hrEmployeeDto.getUserName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setType(type);
        switch (item.getType()){
            case "个人日程":
                item.setBackgroundColor("#1bbc9b");
                break;
            case "系统日程":
                item.setBackgroundColor("#F8CB00");
                break;
            case "其他日程":
                item.setBackgroundColor("#95a5a6");
                break;
        }
        item.setAllDay(true);
        if(end!=null){
            item.setEnd(end);
        }
        item.setStart(start);
        item.setTitle(title);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        sysScheduleMapper.insert(item);
        return item;
    }
}
