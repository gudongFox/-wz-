package com.cmcu.mcc.oa.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaSoftwareApplyMapper;

import com.cmcu.mcc.oa.dto.OaSoftwareApplyDto;
import com.cmcu.mcc.oa.entity.OaNotice;
import com.cmcu.mcc.oa.entity.OaSoftwareApply;
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
public class OaSoftwareApplyService {
    @Resource
    OaSoftwareApplyMapper oaSoftwareApplyMapper;
    @Autowired
    ActService actService;
    @Autowired
    MyActService myActService;
    @Autowired
    SelectEmployeeService selectEmployeeService;

    public void remove(int id, String userLogin) {
        OaSoftwareApply item = oaSoftwareApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }

        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaSoftwareApplyMapper.updateByPrimaryKey(item);
    }

    public void update(OaSoftwareApplyDto oaSoftwareApplyDto) {
        OaSoftwareApply item = oaSoftwareApplyMapper.selectByPrimaryKey(oaSoftwareApplyDto.getId());
        boolean updateTitle=oaSoftwareApplyDto.getSoftwareName().equalsIgnoreCase(item.getSoftwareName());

        item.setSoftwareName(oaSoftwareApplyDto.getSoftwareName());
        item.setSoftwareDesc(oaSoftwareApplyDto.getSoftwareDesc());
        item.setSoftwarePurpose(oaSoftwareApplyDto.getSoftwarePurpose());
        item.setRemark(oaSoftwareApplyDto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        oaSoftwareApplyMapper.updateByPrimaryKey(item);
        if(updateTitle){
            myActService.setVariable(item.getProcessInstanceId(),"description",item.getSoftwareName());
        }
    }

    public OaSoftwareApplyDto getModelById(int id) {
        return getDto(oaSoftwareApplyMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin) { //knowledge
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaSoftwareApply item=new OaSoftwareApply();

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);

        ModelUtil.setNotNullFields(item);
        oaSoftwareApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description","购置软件申报:"+item.getCreatorName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        String processInstanceId = actService.startProcess(EdConst.OA_SOFTWARE_APPLY,  item.getId(), variables);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(EdConst.OA_SOFTWARE_APPLY+"_"+item.getId());
        oaSoftwareApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public OaSoftwareApplyDto getDto(OaSoftwareApply item) {
        OaSoftwareApplyDto oaNoticeDto = OaSoftwareApplyDto.adapt(item);
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(item.getProcessInstanceId());
        oaNoticeDto.setProcessName(processInstanceDto.getProcessName());
        oaNoticeDto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            item.setProcessEnd(true);
            oaSoftwareApplyMapper.updateByPrimaryKey(item);
        }
        return oaNoticeDto;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, int pageNum, int pageSize) {
        params.put("deleted", false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaSoftwareApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaSoftwareApply item = (OaSoftwareApply) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);

        return pageInfo;
    }

}
