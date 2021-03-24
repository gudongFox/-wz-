package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.business.dao.BusinessBidApplyMapper;
import com.cmcu.mcc.business.dao.BusinessRecordMapper;
import com.cmcu.mcc.business.dto.BusinessBidApplyDto;
import com.cmcu.mcc.business.entity.BusinessBidApply;
import com.cmcu.mcc.business.entity.BusinessRecord;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;


import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
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
public class BusinessBidApplyService   {

    @Resource
    BusinessBidApplyMapper businessBidApplyMapper;
    @Resource
    BusinessRecordMapper businessRecordMapper;
    @Resource
    CommonCodeService commonCodeService;

    @Autowired
    ActService actService;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    HrDeptService hrDeptService;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    ProcessQueryService processQueryService;
    public void remove(int id, String userLogin) {
        BusinessBidApply item = businessBidApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        businessBidApplyMapper.updateByPrimaryKey(item);

    }


    public void update(BusinessBidApplyDto businessBidApplyDto) {
        BusinessBidApply item = businessBidApplyMapper.selectByPrimaryKey(businessBidApplyDto.getId());
        if (businessBidApplyDto.getRecordId()!=0) {
            if (!businessRecordMapper.selectByPrimaryKey(businessBidApplyDto.getRecordId()).getProjectName().equals(businessBidApplyDto.getProcessName())) {
                item.setRecordId(0);
            } else {
                item.setRecordId(businessBidApplyDto.getRecordId());
            }
        }
        item.setProjectName(businessBidApplyDto.getProjectName());
        item.setCreatorName(businessBidApplyDto.getCreatorName());
        item.setAgentName(businessBidApplyDto.getAgentName());
        item.setProjectScale(businessBidApplyDto.getProjectScale());
        item.setCustomerName(businessBidApplyDto.getCustomerName());
        item.setProjectType(businessBidApplyDto.getProjectType());
        item.setBusinessType(businessBidApplyDto.getBusinessType());
        item.setAttendType(businessBidApplyDto.getAttendType());
        if (businessBidApplyDto.getOpenTime() != null) {
            item.setOpenTime(businessBidApplyDto.getOpenTime());
        }
        item.setRemark(businessBidApplyDto.getRemark());
        item.setOnline(businessBidApplyDto.getOnline());
        item.setGmtModified(new Date());
        //ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        businessBidApplyMapper.updateByPrimaryKey(item);

        actService.setVariables(item.getProcessInstanceId(),"processDescription",item.getCustomerName()+" "+item.getProjectName());
        actService.setVariables(item.getProcessInstanceId(),"projectType",item.getProjectType());

    }

    public BusinessBidApplyDto getModelById(int id) {
        return getDto(businessBidApplyMapper.selectByPrimaryKey(id));
    }

    public int getNewModelById(int recordId,String userLogin) {
        if (isExistRecord(recordId)){
            Assert.state(false, "项目名为："+businessRecordMapper.selectByPrimaryKey(recordId).getProjectName()+" 的投标报名申请表已创建，请不要重复创建");
            return 0;
        }else {
            BusinessBidApply item = new BusinessBidApply();
            HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(recordId);
            item.setDeptId(hrEmployeeDto.getDeptId());
            item.setDeptName(hrEmployeeDto.getDeptName());

            List<String> deptChargeMen = hrDeptService.getDeptChargeMen(hrEmployeeDto.getDeptId());
            if(deptChargeMen.size()==0) throw  new IllegalArgumentException(hrEmployeeDto.getDeptName()+"没有配置部门负责人!");

            item.setRecordId(recordId);
            item.setProjectName(businessRecord.getProjectName());
            item.setCustomerName(businessRecord.getCustomerName());
            item.setProjectScale(businessRecord.getProjectScale());
            item.setProjectType(businessRecord.getBusinessContent());
            item.setOpenTime(businessRecord.getBidRealOpen());

            item.setBusinessType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"经营模式").toString());
            item.setAttendType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"投标方式").toString());
            item.setOnline(false);
            item.setCreator(userLogin);
            item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());
            item.setDeleted(false);
            item.setProcessEnd(false);
            ModelUtil.setNotNullFields(item);
            businessBidApplyMapper.insert(item);
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "项目投标报名申请表");
            variables.put("deptChargeMen", deptChargeMen);
            String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_APPLY, item.getId(), variables);
            item.setProcessInstanceId(processInstanceId);
            businessBidApplyMapper.updateByPrimaryKey(item);
            return item.getId();
        }
    }

    public int getNewModel(String userLogin) {
        BusinessBidApply item=new BusinessBidApply();
        item.setProjectName("投标项目名称");
        HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());



        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("deptId",hrEmployeeDto.getDeptId());
        if(PageHelper.count(()->businessBidApplyMapper.selectAll(params))>0){
            PageInfo<BusinessBidApply> pageInfo = PageHelper.startPage(1, 1).doSelectPageInfo(() -> businessBidApplyMapper.selectAll(params));
            BusinessBidApply pre=pageInfo.getList().get(0);
            item.setProjectType(pre.getProjectType());
            item.setBusinessType(pre.getBusinessType());
            item.setAttendType(pre.getAttendType());
            item.setOnline(pre.getOnline());
            item.setProjectScale(pre.getProjectScale());
        }else {
            item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型").toString());
            item.setBusinessType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"经营模式").toString());
            item.setAttendType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"投标方式").toString());
            item.setOnline(false);
        }
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        businessBidApplyMapper.insert(item);
        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId());
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(hrEmployeeDto.getDeptName()+"没有配置部门负责人!");
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "投标申请");
        variables.put("deptChargeMen", deptChargeMen);
        String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_APPLY,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        businessBidApplyMapper.updateByPrimaryKey(item);
        return item.getId();

    }

    public BusinessBidApplyDto getDto(Object item) {
        BusinessBidApplyDto dto=BusinessBidApplyDto.adapt((BusinessBidApply) item);

        dto.setBusinessKey(EdConst.BUSINESS_BID_APPLY+"_"+dto.getId());

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if (customProcessInstance==null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                businessBidApplyMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum, int pageSize) {
        params.put("deleted",false);
        HrEmployeeSysDto hrEmployeeSys=hrEmployeeSysService.getModelByUserLogin(userLogin);

        if(!hrEmployeeSys.getDeptName().contains("市场管理")) {
            params.put("deptIdList", hrEmployeeSysService.getMyDeptList(userLogin));
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessBidApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessBidApply item=(BusinessBidApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<String> listAgent(){
        List<String> strings = businessBidApplyMapper.listAgent();
        List<String> list=Lists.newArrayList();
        for (String str:strings){
            if (!str.equals("")){
                list.add(str);
            }
        }
        return list;
    }

    /*查询所有未参加评审的申报表*/
    public List<BusinessRecord> listAllUnExistRecord(int id) {

        BusinessBidApply businessBidApply = businessBidApplyMapper.selectByPrimaryKey(id);

        List<BusinessRecord> list = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", businessBidApply.getDeptId());
        //params.put("processEnd", true);
        List<BusinessRecord> oList = businessRecordMapper.selectAll(params);

        oList.forEach(p -> {
            Map<String, Object> attendParams = Maps.newHashMap();
            attendParams.put("deleted", false);
            attendParams.put("bidApplyId", p.getId());
            attendParams.put("projectName", p.getProjectName());
            attendParams.put("processEnd",true);
            if (PageHelper.count(() -> businessBidApplyMapper.selectAll(attendParams)) == 0) {
                list.add(p);
            }
        });
        return list;
    }

    public boolean isExistRecord(int recordId){
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("recordId",recordId);
        List<BusinessBidApply> list = businessBidApplyMapper.selectAll(params);
        return list.size() > 0;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        BusinessBidApply item = businessBidApplyMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("projectName",item.getProjectName());
        data.put("customerName",item.getCustomerName());
        data.put("agentName",item.getAgentName());
        data.put("projectScale",item.getProjectScale());
        data.put("projectType",item.getProjectType());
        data.put("businessType",item.getBusinessType());
        data.put("attendType",item.getAttendType());
        data.put("openTime",item.getOpenTime());
        data.put("creatorName",item.getCreatorName());
        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));
        return data;
    }



}
