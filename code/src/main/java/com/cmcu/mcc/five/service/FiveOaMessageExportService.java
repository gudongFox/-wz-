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
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaMessageExportMapper;
import com.cmcu.mcc.five.dto.FiveOaMessageExportDto;
import com.cmcu.mcc.five.entity.FiveOaMessageExport;
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
public class FiveOaMessageExportService {
    @Resource
    FiveOaMessageExportMapper fiveOaMessageExportMapper;
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
        FiveOaMessageExport item = fiveOaMessageExportMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaMessageExportDto fiveOaMessageExportDto){
        FiveOaMessageExport model = fiveOaMessageExportMapper.selectByPrimaryKey(fiveOaMessageExportDto.getId());
        model.setUserLogin(fiveOaMessageExportDto.getUserLogin());
        model.setUserName(fiveOaMessageExportDto.getUserName());
        model.setPhone(fiveOaMessageExportDto.getPhone());
        model.setHardDiskNo(fiveOaMessageExportDto.getHardDiskNo());
        model.setServiceAddress(fiveOaMessageExportDto.getServiceAddress());
        model.setFileName(fiveOaMessageExportDto.getFileName());
        model.setApplyTime(fiveOaMessageExportDto.getApplyTime());
        model.setGmtModified(new Date());
        BeanValidator.check(model);
        fiveOaMessageExportMapper.updateByPrimaryKey(model);
    }

    public FiveOaMessageExportDto getModelById(int id){

        return getDto(fiveOaMessageExportMapper.selectByPrimaryKey(id));
    }

    public FiveOaMessageExportDto getDto(FiveOaMessageExport item) {
        FiveOaMessageExportDto dto=FiveOaMessageExportDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaMessageExportMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaMessageExport item=new FiveOaMessageExport();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setUserName(hrEmployeeDto.getUserName());
        item.setUserLogin(hrEmployeeDto.getUserLogin());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setPhone(hrEmployeeDto.getMobile());


        //办公室主任默认
        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(59);

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaMessageExportMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_MESSAGE_EXPORT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "个人非密信息导出审批："+item.getUserName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导


        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_MESSAGE_EXPORT,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaMessageExportMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaMessageExportMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaMessageExport item=(FiveOaMessageExport)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaMessageExport item = fiveOaMessageExportMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("userName",item.getUserName());
        data.put("userLogin",item.getUserLogin());
        data.put("phone",item.getPhone());
        data.put("hardDiskNo",item.getHardDiskNo());
        data.put("serviceAddress",item.getServiceAddress());
        data.put("fileName",item.getFileName());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("孙慰梅-协同".equals(dto.getActivityName())){
                data.put("computerEquipmentMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}
