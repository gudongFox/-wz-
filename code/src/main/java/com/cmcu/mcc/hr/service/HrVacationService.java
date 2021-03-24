package com.cmcu.mcc.hr.service;

import com.cmcu.common.data.IDataService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dao.HrVacationMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrVacationDto;
import com.cmcu.mcc.hr.entity.HrVacation;
import com.cmcu.mcc.service.ActService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HrVacationService implements IDataService<HrVacationDto> {
    @Resource
    HrVacationMapper hrVacationMapper;
    @Autowired
    ActService actService;
    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    HrDeptService hrDeptService;





    @Override
    public void remove(int id, String userLogin) {
        HrVacation item = hrVacationMapper.selectByPrimaryKey(id);
        item.setGmtModified(new Date());
        item.setDeleted(true);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        hrVacationMapper.updateByPrimaryKey(item);
    }

    @Override
    public void update(HrVacationDto hrVacationDto) {
        HrVacation item = hrVacationMapper.selectByPrimaryKey(hrVacationDto.getId());
        item.setRemark(hrVacationDto.getRemark());
        item.setGmtModified(new Date());
        hrVacationMapper.updateByPrimaryKey(item);
    }

    @Override
    public HrVacationDto getModelById(int id) {
        return getDto(hrVacationMapper.selectByPrimaryKey(id));
    }

    @Override
    public int getNewModel(int foreignKey, String userLogin) {
        HrVacation item=new HrVacation();


        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        item.setUserLogin(userLogin);
        item.setUserName(hrEmployeeDto.getUserName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setJoinCompanyTime(hrEmployeeDto.getJoinCompanyTime());
        item.setJoinWorkTime(hrEmployeeDto.getJoinWorkTime());
        item.setUserType(hrEmployeeDto.getUserType());

        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setVocationYear(Calendar.getInstance().get(Calendar.YEAR));
        item.setAnnualTotal(0);
        item.setForceHoliday(0);
        item.setAnnualLeft(0);

        item.setDeleted(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        hrVacationMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description", "员工休假申请");
        //variables.put("departmentMan", hrDeptService.getDeptChargeMan(hrEmployeeDto.getDeptId()));
        variables.put("manpowerMan","liushan");
        String processInstanceId = actService.startProcess(EdConst.HR_VACATION,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        hrVacationMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    @Override
    public HrVacationDto getDto(Object item) {
        HrVacationDto dto=HrVacationDto.adapt((HrVacation) item);
        HrVacation model= (HrVacation) item;

        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());

        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&& StringUtils.isEmpty(processInstanceDto.getProcessName())){
            model.setProcessEnd(true);
            hrVacationMapper.updateByPrimaryKey(model);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    @Override
    public List<HrVacationDto> listDataByForeignKey(int foreignKey) {
        return null;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrVacationMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrVacation item=(HrVacation)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
