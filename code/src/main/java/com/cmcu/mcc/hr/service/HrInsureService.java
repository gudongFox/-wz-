package com.cmcu.mcc.hr.service;

import com.cmcu.common.data.IDataService;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dao.HrInsureMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrInsureDto;
import com.cmcu.mcc.hr.entity.HrEmployeeProof;
import com.cmcu.mcc.hr.entity.HrInsure;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HrInsureService  {
    @Resource
    HrInsureMapper hrInsureMapper;
    @Resource
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Autowired
    CommonConfigService commonConfigService;

    public void remove(int id, String userLogin) {
        HrInsure item = hrInsureMapper.selectByPrimaryKey(id);

        item.setDeleted(true);
        Assert.state(item.getUserLogin().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        hrInsureMapper.updateByPrimaryKey(item);
    }


    public void update(HrInsureDto hrInsureDto) {
        HrInsure item = hrInsureMapper.selectByPrimaryKey(hrInsureDto.getId());
        item.setCompanyName(hrInsureDto.getCompanyName());
        item.setApplyReason(hrInsureDto.getApplyReason());
        item.setMobile(hrInsureDto.getMobile());
        item.setSocialNo(hrInsureDto.getSocialNo());
        item.setLiveAddress(hrInsureDto.getLiveAddress());
        item.setIdCardNo(hrInsureDto.getIdCardNo());
        item.setIdCardType(hrInsureDto.getIdCardType());
        hrInsureMapper.updateByPrimaryKey(item);
    }

    public HrInsureDto getModelById(int id) {
        return getDto(hrInsureMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin) {
        HrInsure item=new HrInsure();
        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        item.setUserLogin(hrEmployeeDto.getUserLogin());
        item.setUserName(hrEmployeeDto.getUserName());
        item.setLiveAddress(hrEmployeeDto.getLiveAddress());
        item.setMobile(hrEmployeeDto.getMobile());
        item.setIdCardType(hrEmployeeDto.getIdCardType());
        item.setIdCardNo(hrEmployeeDto.getIdCardNo());
        item.setGmtCreate(new Date());
        item.setCompanyName(commonConfigService.getConfig().getCompanyName());

        ModelUtil.setNotNullFields(item);
        hrInsureMapper.insert(item);
        //流程发起
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description", "参保证明申请:"+item.getUserName());
        variables.put("managementMan", "chenling");
        String processInstanceId = actService.startProcess(EdConst.HR_INSURE,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        hrInsureMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public HrInsureDto getDto(Object item) {
        HrInsureDto dto=HrInsureDto.adapt((HrInsure) item);
        HrInsure model= (HrInsure) item;

        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&& StringUtils.isEmpty(processInstanceDto.getProcessName())){
            model.setProcessEnd(true);
            hrInsureMapper.updateByPrimaryKey(model);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrInsureMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrInsure item=(HrInsure)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        HrInsure item = hrInsureMapper.selectByPrimaryKey(id);
        data.put("userName",item.getUserName());
        data.put("idCardNo",item.getIdCardNo());
        data.put("companyName",item.getCompanyName());
        data.put("gmtCreate",item.getGmtCreate());
        data.put("socialNo",item.getSocialNo());
        return data;
    }

}
