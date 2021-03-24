package com.cmcu.mcc.hr.service;

import com.cmcu.common.data.IDataService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dao.HrLeaveMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrLeaveDto;
import com.cmcu.mcc.hr.entity.HrLeave;
import com.cmcu.mcc.service.ActService;
import com.cmcu.common.dao.CommonConfigMapper;
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
public class HrLeaveService implements IDataService<HrLeaveDto> {
    @Resource
    HrLeaveMapper hrLeaveMapper;
    @Autowired
    ActService actService;
    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    HrDeptService hrDeptService;
    @Resource
    CommonConfigMapper commonConfigMapper;

    @Override
    public void remove(int id, String userLogin) {
        HrLeave item = hrLeaveMapper.selectByPrimaryKey(id);
        item.setGmtModified(new Date());
        item.setDeleted(true);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        hrLeaveMapper.updateByPrimaryKey(item);

    }

    @Override
    public void update(HrLeaveDto hrLeaveDto) {
        HrLeave item = hrLeaveMapper.selectByPrimaryKey(hrLeaveDto.getId());

        item.setMobile(hrLeaveDto.getMobile());
        item.setLeaveReason(hrLeaveDto.getLeaveReason());
        item.setApplyLeaveTime(hrLeaveDto.getApplyLeaveTime());
        item.setApproveLeaveTime(hrLeaveDto.getApproveLeaveTime());
        item.setDeptResult(hrLeaveDto.getDeptResult());
        item.setFinanceResult(hrLeaveDto.getFinanceResult());
        item.setOfficeResult(hrLeaveDto.getOfficeResult());

        item.setHrRemark(hrLeaveDto.getHrRemark());
        item.setHrDest(hrLeaveDto.getHrDest());

        item.setGmtModified(new Date());

        hrLeaveMapper.updateByPrimaryKey(item);
    }

    @Override
    public HrLeaveDto getModelById(int id) {
        return getDto(hrLeaveMapper.selectByPrimaryKey(id));
    }

    @Override
    public int getNewModel(int foreignKey, String userLogin) {
        HrLeave item = new HrLeave();
        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);

        item.setUserLogin(userLogin);
        item.setUserName(hrEmployeeDto.getUserName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setCompanyName(commonConfigMapper.selectByPrimaryKey(1).getCompanyName());
        item.setJoinCompanyTime(hrEmployeeDto.getJoinCompanyTime());
        item.setMobile(hrEmployeeDto.getMobile());
        item.setUserType(hrEmployeeDto.getUserType());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        hrLeaveMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description", "员工离职申请:"+item.getCreatorName());
        //variables.put("departmentMan",hrDeptService.getDeptChargeMan(hrEmployeeDto.getDeptId()));
        variables.put("financialMan", "chenling");
        variables.put("manpowerMan","liushan");
        String processInstanceId = actService.startProcess(EdConst.HR_LEAVE,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        hrLeaveMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    @Override
    public HrLeaveDto getDto(Object item) {
        HrLeaveDto dto=HrLeaveDto.adapt((HrLeave) item);
        HrLeave model= (HrLeave) item;

        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());

        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&& StringUtils.isEmpty(processInstanceDto.getProcessName())){
            model.setProcessEnd(true);
            hrLeaveMapper.updateByPrimaryKey(model);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    @Override
    public List<HrLeaveDto> listDataByForeignKey(int foreignKey) {
        return null;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrLeaveMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrLeave item=(HrLeave)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
