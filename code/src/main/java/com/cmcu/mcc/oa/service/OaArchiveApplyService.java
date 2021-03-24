package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.data.IDataService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaArchiveApplyMapper;
import com.cmcu.mcc.oa.dao.OaArchiveDetailMapper;
import com.cmcu.mcc.oa.dto.OaArchiveApplyDto;
import com.cmcu.mcc.oa.dto.OaArchiveDetailDto;
import com.cmcu.mcc.oa.dto.OaArchiveDto;
import com.cmcu.mcc.oa.entity.OaArchive;
import com.cmcu.mcc.oa.entity.OaArchiveApply;
import com.cmcu.mcc.oa.entity.OaArchiveDetail;
import com.cmcu.mcc.service.ActService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OaArchiveApplyService {
    @Resource
    OaArchiveApplyMapper oaArchiveApplyMapper;
   @Autowired
   OaArchiveService oaArchiveService;
    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    ActService actService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;



    public void remove(int id, String userLogin) {

        OaArchiveApply item = oaArchiveApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaArchiveApplyMapper.updateByPrimaryKey(item);

    }

    public void update(OaArchiveApplyDto oaArchiveApplyDto) {
        OaArchiveApply item = oaArchiveApplyMapper.selectByPrimaryKey(oaArchiveApplyDto.getId());
        item.setApplyPurpose(oaArchiveApplyDto.getApplyPurpose());
        item.setArchiveId(oaArchiveApplyDto.getArchiveId());
        item.setStartTime(oaArchiveApplyDto.getStartTime());
        item.setEndTime(oaArchiveApplyDto.getEndTime());
        item.setDeptId(oaArchiveApplyDto.getDeptId());
        item.setDeptName(oaArchiveApplyDto.getDeptName());
        item.setApproveIds(oaArchiveApplyDto.getApproveIds());
        item.setRemark(oaArchiveApplyDto.getRemark());
        MyProcessInstance processInstance= myHistoryService.getMyProcessInstance(item.getProcessInstanceId(),oaArchiveApplyDto.getOperateUserLogin());
        if(processInstance.getMyTaskFirst()){
            item.setDetailIds(oaArchiveApplyDto.getDetailIds());
            item.setApproveIds(oaArchiveApplyDto.getDetailIds());
        }else{
            item.setApproveIds(oaArchiveApplyDto.getDetailIds());
        }
        oaArchiveApplyMapper.updateByPrimaryKey(item);
    }
    
    public OaArchiveApplyDto getModelById(int id) {
        return getDto(oaArchiveApplyMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaArchiveApply item=new OaArchiveApply();
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId().toString());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        oaArchiveApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description","工程资料借阅:"+item.getCreatorName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        String processInstanceId =taskHandleService.startProcess(EdConst.OA_ARCHIVE_APPLY, EdConst.OA_ARCHIVE_APPLY+'_'+item.getId(),variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        oaArchiveApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public OaArchiveApplyDto getDto(OaArchiveApply item) {
        OaArchiveApplyDto model = OaArchiveApplyDto.adapt(item);
       if (item.getArchiveId()!=0){
           OaArchiveDto oaArchiveDto = oaArchiveService.getModelById(item.getArchiveId());
           model.setProjectName(oaArchiveDto.getProjectName());
       }

        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(item.getProcessInstanceId());
        model.setProcessName(processInstanceDto.getProcessName());
        model.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            item.setProcessEnd(true);
            oaArchiveApplyMapper.updateByPrimaryKey(item);
        }
        return model;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, int pageNum, int pageSize) {
        params.put("deleted", false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaArchiveApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaArchiveApply item = (OaArchiveApply) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);

        return pageInfo;
    }


}
