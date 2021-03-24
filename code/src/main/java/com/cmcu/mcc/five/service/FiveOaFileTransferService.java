package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaFileTransferMapper;
import com.cmcu.mcc.five.dto.FiveOaFileTransferDto;
import com.cmcu.mcc.five.entity.FiveOaFileTransfer;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaFileTransferService {
    @Resource
    FiveOaFileTransferMapper fiveOaFileTransferMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaFileTransfer item = fiveOaFileTransferMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaFileTransferDto fiveOaFileTransferDto){
        FiveOaFileTransfer model = fiveOaFileTransferMapper.selectByPrimaryKey(fiveOaFileTransferDto.getId());
        model.setDeptId(fiveOaFileTransferDto.getDeptId());
        model.setDeptName(fiveOaFileTransferDto.getDeptName());
        model.setSender(fiveOaFileTransferDto.getSender());
        model.setSenderName(fiveOaFileTransferDto.getSenderName());
        model.setSendTime(fiveOaFileTransferDto.getSendTime());
        model.setFileName(fiveOaFileTransferDto.getFileName());
        model.setReceiver(fiveOaFileTransferDto.getReceiver());
        model.setReceiverName(fiveOaFileTransferDto.getReceiverName());
        model.setFileType(fiveOaFileTransferDto.getFileType());
        model.setRemark(fiveOaFileTransferDto.getRemark());
        model.setGmtModified(new Date());
        fiveOaFileTransferMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        variables.put("receiver", model.getReceiver());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaFileTransferDto getModelById(int id){

        return getDto(fiveOaFileTransferMapper.selectByPrimaryKey(id));
    }

    public FiveOaFileTransferDto getDto(FiveOaFileTransfer item) {
        FiveOaFileTransferDto dto=FiveOaFileTransferDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaFileTransferMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){

        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("机要秘书");
        Assert.state(secretary.size()>0,"未找到职务为 机要秘书 人员");

        FiveOaFileTransfer item=new FiveOaFileTransfer();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setSender(hrEmployeeDto.getUserLogin());
        item.setSenderName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setFileType("非密");

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setAffirm(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaFileTransferMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_FILE_TRANSFER+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "非密网文件传送流程："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_FILE_TRANSFER,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaFileTransferMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaFileTransferMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaFileTransfer item=(FiveOaFileTransfer)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaFileTransfer item = fiveOaFileTransferMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("senderName",item.getSenderName());
        data.put("fileName",item.getFileName());
        data.put("fileType",item.getFileType());
        data.put("receiverName",item.getReceiverName());
        data.put("affirm",item.getAffirm());
        data.put("sendTime",item.getSendTime());
        data.put("remark",item.getRemark());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }
}
