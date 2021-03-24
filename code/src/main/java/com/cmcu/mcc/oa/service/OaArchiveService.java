package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaArchiveMapper;
import com.cmcu.mcc.oa.dto.OaArchiveDto;
import com.cmcu.mcc.oa.dto.OaNoticeDto;
import com.cmcu.mcc.oa.entity.OaArchive;
import com.cmcu.mcc.oa.entity.OaNotice;
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
public class OaArchiveService {
    @Resource
    OaArchiveMapper oaArchiveMapper;
    @Autowired
    ActService actService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    SelectEmployeeService selectEmployeeService;


    public void remove(int id, String userLogin) {
        OaArchive item = oaArchiveMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }

        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaArchiveMapper.updateByPrimaryKey(item);
    }

    public void update(OaArchiveDto oaArchiveDto) {
        OaArchive item = oaArchiveMapper.selectByPrimaryKey(oaArchiveDto.getId());
        item.setDeptName(oaArchiveDto.getDeptName());
        item.setDeptId(oaArchiveDto.getDeptId());
        item.setProjectName(oaArchiveDto.getProjectName());
        item.setStepName(oaArchiveDto.getStepName());
        item.setContractNo(oaArchiveDto.getContractNo());
        item.setStageName(oaArchiveDto.getStageName());
        item.setDesignTime(oaArchiveDto.getDesignTime());
        item.setProjectScale(oaArchiveDto.getProjectScale());
        item.setProjectType(oaArchiveDto.getProjectType());
        item.setProjectDesc(oaArchiveDto.getProjectDesc());
        item.setConstructionOrg(oaArchiveDto.getConstructionOrg());
        item.setArchiveTime(oaArchiveDto.getArchiveTime());
        item.setRemark(oaArchiveDto.getRemark());
        item.setDeptOwn(oaArchiveDto.getDeptOwn());
        item.setGmtModified(new Date());
        oaArchiveMapper.updateByPrimaryKey(item);
    }

    public OaArchiveDto getModelById(int id) {
        return getDto(oaArchiveMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        Map parms = Maps.newHashMap();
        parms.put("deleted", false);
        int seq= (int) PageHelper.count(()->oaArchiveMapper.selectAll(parms));

        OaArchive item=new OaArchive();
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setSeq(seq+1);
        item.setDeptOwn(true);
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        oaArchiveMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description","工程档案管理:"+item.getCreatorName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        String processInstanceId = taskHandleService.startProcess(EdConst.OA_ARCHIVE,EdConst.OA_ARCHIVE+'_'+item.getId()
                ,variables,MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        oaArchiveMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public OaArchiveDto getDto(OaArchive item) {
        OaArchiveDto oaArchiveDto = OaArchiveDto.adapt(item);
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(item.getProcessInstanceId());
        oaArchiveDto.setProcessName(processInstanceDto.getProcessName());
        oaArchiveDto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            item.setProcessEnd(true);
            oaArchiveMapper.updateByPrimaryKey(item);
        }
        return oaArchiveDto;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, int pageNum, int pageSize) {
        params.put("deleted", false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaArchiveMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaArchive item = (OaArchive) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);

        return pageInfo;
    }


}
