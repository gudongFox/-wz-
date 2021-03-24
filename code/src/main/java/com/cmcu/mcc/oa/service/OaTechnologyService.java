package com.cmcu.mcc.oa.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaTechnologyMapper;
import com.cmcu.mcc.oa.dto.OaTechnologyDto;
import com.cmcu.mcc.oa.entity.OaTechnology;
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
public class OaTechnologyService {

    @Resource
    OaTechnologyMapper oaTechnologyMapper;
    @Autowired
    ActService actService;
    @Autowired
    SelectEmployeeService selectEmployeeService;

    public void remove(int id, String userLogin) {
        OaTechnology item = oaTechnologyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaTechnologyMapper.updateByPrimaryKey(item);
    }

    public void update(OaTechnologyDto oaTechnologyDto) {
        OaTechnology item = oaTechnologyMapper.selectByPrimaryKey(oaTechnologyDto.getId());

        item.setTechnologyTitle(oaTechnologyDto.getTechnologyTitle());
        item.setTechnologyDesc(oaTechnologyDto.getTechnologyDesc());

        item.setRemark(oaTechnologyDto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        oaTechnologyMapper.updateByPrimaryKey(item);
    }

    public OaTechnologyDto getModelById(int id) {
        return getDto(oaTechnologyMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String type) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaTechnology item = new OaTechnology();
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setTechnologyType(type);
        ModelUtil.setNotNullFields(item);
        oaTechnologyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description","科技质量申报:"+type+"-"+item.getCreatorName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        String processInstanceId = actService.startProcess(EdConst.OA_TECHNOLOGY,  item.getId(), variables);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(EdConst.OA_TECHNOLOGY+"_"+item.getId());
        oaTechnologyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public OaTechnologyDto getDto(OaTechnology item) {
        OaTechnologyDto oaNoticeDto = OaTechnologyDto.adapt(item);
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(item.getProcessInstanceId());
        oaNoticeDto.setProcessName(processInstanceDto.getProcessName());
        oaNoticeDto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            item.setProcessEnd(true);
            oaTechnologyMapper.updateByPrimaryKey(item);
        }
        return oaNoticeDto;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, int pageNum, int pageSize) {
        params.put("deleted", false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaTechnologyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaTechnology item = (OaTechnology) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);

        return pageInfo;
    }




}
