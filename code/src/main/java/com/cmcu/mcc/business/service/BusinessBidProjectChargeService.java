package com.cmcu.mcc.business.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.business.dao.BusinessBidAttendMapper;
import com.cmcu.mcc.business.dao.BusinessBidProjectChargeMapper;
import com.cmcu.mcc.business.dto.BusinessBidProjectChargeDto;
import com.cmcu.mcc.business.entity.BusinessBidAttend;
import com.cmcu.mcc.business.entity.BusinessBidProjectCharge;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
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
public class BusinessBidProjectChargeService  {
    @Resource
    BusinessBidProjectChargeMapper businessBidProjectChargeMapper;
    @Resource
    BusinessBidAttendMapper businessBidAttendMapper;

    @Autowired
    ActService actService;

    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    public void checkSendAble(int id,String userLogin){
        BusinessBidProjectCharge model=businessBidProjectChargeMapper.selectByPrimaryKey(id);
        if(model.getProcessEnd()){
            throw new IllegalArgumentException("该流程已经结束!");
        }
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId(),userLogin);
        Assert.state(processInstanceDto.isMyTaskFirst(),"该任务属于其他人处理中!");
        List<String> projectChargeMen= MyStringUtil.getStringList(model.getExploreMan()+","+model.getDesignMan()+","+model.getConstructionMan());
        actService.setVariables(model.getProcessInstanceId(),"projectChargeMen",projectChargeMen);
        actService.setVariables(model.getProcessInstanceId(),"",selectEmployeeService.getMarketRegister());
    }

    public void remove(int id, String userLogin) {
        BusinessBidProjectCharge item = businessBidProjectChargeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        businessBidProjectChargeMapper.updateByPrimaryKey(item);
    }

    public void update(BusinessBidProjectChargeDto businessBidProjectChargeDto) {
        BusinessBidProjectCharge item = businessBidProjectChargeMapper.selectByPrimaryKey(businessBidProjectChargeDto.getId());
        if (businessBidProjectChargeDto.getBidAttendId() != 0) {
            if (!businessBidAttendMapper.selectByPrimaryKey(businessBidProjectChargeDto.getBidAttendId()).equals(businessBidProjectChargeDto.getProjectName())) {
                item.setBidAttendId(0);
            } else {
                item.setBidAttendId(businessBidProjectChargeDto.getBidAttendId());
            }
        }
        item.setProjectName(businessBidProjectChargeDto.getProjectName());
        item.setCustomerName(businessBidProjectChargeDto.getCustomerName());
        item.setProjectScale(businessBidProjectChargeDto.getProjectScale());
        item.setProjectType(businessBidProjectChargeDto.getProjectType());
        item.setProjectAddress(businessBidProjectChargeDto.getProjectAddress());
        item.setProjectContent(businessBidProjectChargeDto.getProjectContent());
        item.setChargeRule(businessBidProjectChargeDto.getChargeRule());
        item.setOpenTime(businessBidProjectChargeDto.getOpenTime());
        item.setExploreMan(businessBidProjectChargeDto.getExploreMan());
        item.setDesignMan(businessBidProjectChargeDto.getDesignMan());
        item.setMajorMen(businessBidProjectChargeDto.getMajorMen());
        item.setExploreManName(businessBidProjectChargeDto.getExploreManName());
        item.setDesignManName(businessBidProjectChargeDto.getDesignManName());
        item.setConstructionMan(businessBidProjectChargeDto.getConstructionMan());
        item.setConstructionManName(businessBidProjectChargeDto.getConstructionManName());
        item.setRemark(businessBidProjectChargeDto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(item.getDeptId());
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(item.getDeptName()+"没有配置部门负责人!");

        businessBidProjectChargeMapper.updateByPrimaryKey(item);


        actService.setVariables(item.getProcessInstanceId(), "deptChargeMen", deptChargeMen);
        actService.setVariables(item.getProcessInstanceId(), "projectChargeMen", MyStringUtil.getStringList(item.getExploreMan() + "," + item.getDesignMan() + "," + item.getConstructionMan()));
        actService.setVariables(item.getProcessInstanceId(), "processDescription", item.getProjectName());
    }

    public void update(int attendId,int id) {
        BusinessBidProjectCharge item = businessBidProjectChargeMapper.selectByPrimaryKey(id);
        BusinessBidAttend businessBidAttend = businessBidAttendMapper.selectByPrimaryKey(attendId);
        item.setBidAttendId(attendId);
        item.setProjectName(businessBidAttend.getProjectName());
        item.setProjectAddress(businessBidAttend.getProjectAddress());
        item.setProjectScale(businessBidAttend.getProjectScale());
        item.setProjectType(businessBidAttend.getProjectType());
        item.setOpenTime(businessBidAttend.getOpenTime());
        item.setCustomerName(businessBidAttend.getCustomerName());
        item.setGmtModified(new Date());
        businessBidProjectChargeMapper.updateByPrimaryKey(item);
    }

    public BusinessBidProjectChargeDto getModelById(int id) {
        return getDto(businessBidProjectChargeMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin) {
        BusinessBidProjectCharge item = new BusinessBidProjectCharge();
        item.setProjectName("示例项目名称");
        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        businessBidProjectChargeMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_PROJECT_CHARGE, item.getId(), variables);
        item.setProcessInstanceId(processInstanceId);
        businessBidProjectChargeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public int getNewModel(int attendId,String userLogin) {
        if(isExistBidProjectCharge(attendId)){
            Assert.state(false, "项目名为："+businessBidAttendMapper.selectByPrimaryKey(attendId).getProjectName()+" 的项目负责人确认表已创建，请不要重复创建");
            return 0;
        }else {
            BusinessBidProjectCharge item = new BusinessBidProjectCharge();
            HrEmployeeDto hrEmployeeDto=hrEmployeeService.getModelByUserLogin(userLogin);
            BusinessBidAttend businessBidAttend = businessBidAttendMapper.selectByPrimaryKey(attendId);

            item.setBidAttendId(attendId);
            item.setProjectName(businessBidAttend.getProjectName());
            item.setProjectAddress(businessBidAttend.getProjectAddress());
            item.setProjectScale(businessBidAttend.getProjectScale());
            item.setProjectType(businessBidAttend.getProjectType());
            item.setOpenTime(businessBidAttend.getOpenTime());
            item.setCustomerName(businessBidAttend.getCustomerName());

            item.setDeptId(hrEmployeeDto.getDeptId());
            item.setDeptName(hrEmployeeDto.getDeptName());
            item.setCreator(userLogin);
            item.setCreatorName(hrEmployeeDto.getUserName());

            item.setDeleted(false);
            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());
            item.setProcessEnd(false);
            ModelUtil.setNotNullFields(item);
            businessBidProjectChargeMapper.insert(item);
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "项目负责人确认表");
            variables.put("projectChargeMen", "xxin");
            variables.put("chargeMen", "liuting");
            String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_PROJECT_CHARGE, item.getId(), variables);
            item.setProcessInstanceId(processInstanceId);
            businessBidProjectChargeMapper.updateByPrimaryKey(item);
            return item.getId();
        }
    }

    public BusinessBidProjectChargeDto getDto(Object item) {
        BusinessBidProjectChargeDto dto=BusinessBidProjectChargeDto.adapt((BusinessBidProjectCharge) item);
        BusinessBidProjectCharge model= (BusinessBidProjectCharge) item;
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&&StringUtils.isEmpty(processInstanceDto.getProcessName())){
            model.setProcessEnd(true);
            businessBidProjectChargeMapper.updateByPrimaryKey(model);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin, int pageNum, int pageSize) {
        params.put("deleted",false);
        HrEmployeeSysDto hrEmployeeSys=hrEmployeeSysService.getModelByUserLogin(userLogin);
        if(!hrEmployeeSys.getDeptName().contains("市场管理")) {
            params.put("deptIdList", hrEmployeeSysService.getMyDeptList(userLogin));
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessBidProjectChargeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessBidProjectCharge item=(BusinessBidProjectCharge)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /*查询所有未进行项目负责人审核的标前评审*/
    public List<BusinessBidAttend> listAllUnExistAttend(int id){

        BusinessBidProjectCharge businessBidProjectCharge = businessBidProjectChargeMapper.selectByPrimaryKey(id);

        List<BusinessBidAttend> list = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", businessBidProjectCharge.getDeptId());
        //params.put("processEnd", true);
        List<BusinessBidAttend> oList = businessBidAttendMapper.selectAll(params);

        oList.forEach(p -> {
            Map<String, Object> attendParams = Maps.newHashMap();
            attendParams.put("deleted", false);
            attendParams.put("bidAttendId", p.getId());
            attendParams.put("projectName", p.getProjectName());
            attendParams.put("processEnd",true);
            if (PageHelper.count(() -> businessBidProjectChargeMapper.selectAll(attendParams)) == 0) {
                list.add(p);
            }
        });
        return list;
    }
    /*打印*/
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        BusinessBidProjectCharge item= businessBidProjectChargeMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("projectName",item.getProjectName());
        data.put("projectAddress",item.getProjectAddress());
        data.put("projectScale",item.getProjectScale());
        data.put("projectContent",item.getProjectContent());
        data.put("chargeRule",item.getChargeRule());
        data.put("majorMen",item.getMajorMen());
        data.put("exploreManName",item.getExploreManName());
        data.put("designManName",item.getDesignManName());
        data.put("constructionManName",item.getConstructionManName());
        data.put("gmtCreate",item.getGmtCreate());
        data.put("openTime",item.getOpenTime());
        data.put("creatorName",item.getCreatorName());
        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));
        return data;
    }

    public boolean isExistBidProjectCharge(int attendId){
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("bidApplyId",attendId);
        List<BusinessBidProjectCharge> list = businessBidProjectChargeMapper.selectAll(params);
        return list.size() > 0;

    }

}
