package com.cmcu.mcc.hr.service;

import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dao.HrEmployeeProofMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeProofDto;
import com.cmcu.mcc.hr.entity.HrEmployeeProof;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.sys.service.SysConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HrEmployeeProofService  {
    @Resource
    HrEmployeeProofMapper hrEmployeeProofMapper;
    @Autowired
    ActService actService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    SysConfigService sysConfigService;

    public void remove(int id, String userLogin) {
        HrEmployeeProof item = hrEmployeeProofMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        Assert.state(item.getUserLogin().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        hrEmployeeProofMapper.updateByPrimaryKey(item);
    }

    public void update(HrEmployeeProofDto hrEmployeeProofDto) {
        HrEmployeeProof item = hrEmployeeProofMapper.selectByPrimaryKey(hrEmployeeProofDto.getId());
        item.setCompanyName(hrEmployeeProofDto.getCompanyName());
        item.setIdCardNo(hrEmployeeProofDto.getIdCardNo());
        item.setIdCardType(hrEmployeeProofDto.getIdCardType());
        item.setRemark(hrEmployeeProofDto.getRemark());
        hrEmployeeProofMapper.updateByPrimaryKey(item);
    }

    public HrEmployeeProofDto getModelById(int id) {
        return getDto(hrEmployeeProofMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin) {
        //1.基础信息
        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        HrEmployeeProof item=new HrEmployeeProof();
        item.setUserLogin(hrEmployeeDto.getUserLogin());
        item.setUserName(hrEmployeeDto.getUserName());
        item.setIdCardType(hrEmployeeDto.getIdCardType());
        item.setIdCardNo(hrEmployeeDto.getIdCardNo());
        item.setJoinCompanyTime(hrEmployeeDto.getJoinCompanyTime());
        item.setCompanyName(sysConfigService.getConfig().getCompanyName());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        hrEmployeeProofMapper.insert(item);
        //流程发起
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description", "在职证明:"+item.getUserName());
        variables.put("departmentMan", "tangzhiming");
        variables.put("managementMan", "chenling");
        String processInstanceId = actService.startProcess(EdConst.HR_EMPLOYEE_PROOF,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        hrEmployeeProofMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public HrEmployeeProofDto getDto(Object item) {
        HrEmployeeProofDto dto=HrEmployeeProofDto.adapt((HrEmployeeProof) item);
        HrEmployeeProof model= (HrEmployeeProof) item;

        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&& StringUtils.isEmpty(processInstanceDto.getProcessName())){
            model.setProcessEnd(true);
            hrEmployeeProofMapper.updateByPrimaryKey(model);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }
    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrEmployeeProofMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrEmployeeProof item=(HrEmployeeProof)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();

        HrEmployeeProof item = hrEmployeeProofMapper.selectByPrimaryKey(id);

        data.put("userName",item.getUserName());
        data.put("idCardNo",item.getIdCardNo());
        data.put("joinCompanyTime",item.getJoinCompanyTime());
        data.put("companyName",item.getCompanyName());
        data.put("gmtCreate", item.getGmtCreate());

        return data;
    }
}
