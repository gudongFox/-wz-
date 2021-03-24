package com.cmcu.mcc.business.service;


import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.business.dao.BusinessBidApplyMapper;
import com.cmcu.mcc.business.dao.BusinessBidAttendMapper;
import com.cmcu.mcc.business.dto.BusinessBidAttendDto;
import com.cmcu.mcc.business.entity.BusinessBidApply;
import com.cmcu.mcc.business.entity.BusinessBidAttend;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
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
public class BusinessBidAttendService  {

    @Resource
    BusinessBidAttendMapper businessBidAttendMapper;
    @Autowired
    ActService actService;

    @Resource
    CommonCodeService commonCodeService;

    @Resource
    BusinessBidApplyMapper businessBidApplyMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;


    public void remove(int id, String userLogin) {

        BusinessBidAttend item = businessBidAttendMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        businessBidAttendMapper.updateByPrimaryKey(item);

    }

    public void update(BusinessBidAttendDto businessBidAttendDto) {
        BusinessBidAttend item = businessBidAttendMapper.selectByPrimaryKey(businessBidAttendDto.getId());
        if (businessBidAttendDto.getBidApplyId()!=0) {
            if (!businessBidApplyMapper.selectByPrimaryKey(businessBidAttendDto.getBidApplyId()).getProjectName().equals(businessBidAttendDto.getProjectName())) {
                item.setBidApplyId(0);
            } else {
                item.setBidApplyId(businessBidAttendDto.getBidApplyId());
            }
        }
        item.setProjectName(businessBidAttendDto.getProjectName());
        item.setCustomerName(businessBidAttendDto.getCustomerName());
        item.setProjectAddress(businessBidAttendDto.getProjectAddress());
        item.setProjectTime(businessBidAttendDto.getProjectTime());
        item.setProjectScale(businessBidAttendDto.getProjectScale());
        item.setProjectType(businessBidAttendDto.getProjectType());
        item.setQualityRequest(businessBidAttendDto.getQualityRequest());
        item.setTenderBond(businessBidAttendDto.getTenderBond());
        item.setTechStandard(businessBidAttendDto.getTechStandard());
        item.setPerformanceBond(businessBidAttendDto.getPerformanceBond());
        item.setBusinessType(businessBidAttendDto.getBusinessType());
        item.setPaymentRule(businessBidAttendDto.getPaymentRule());
        item.setOpenTime(businessBidAttendDto.getOpenTime());
        item.setAttendType(businessBidAttendDto.getAttendType());
        item.setAttendScope(businessBidAttendDto.getAttendScope());
        item.setAttendPrice(businessBidAttendDto.getAttendPrice());
        item.setRemark(businessBidAttendDto.getRemark());
        item.setGmtModified(new Date());
        item.setChargeMan(businessBidAttendDto.getChargeMan());
        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(item.getDeptId());
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(item.getDeptName()+"没有配置部门负责人!");


        businessBidAttendMapper.updateByPrimaryKey(item);

        actService.setVariables(item.getProcessInstanceId(),"deptChargeMen", deptChargeMen);
        actService.setVariables(item.getProcessInstanceId(),"processDescription", item.getCustomerName()+" "+item.getProjectName());
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(item.getProcessInstanceId());
        List<String> deptMen=Lists.newArrayList();
        deptMen.add(selectEmployeeService.getMarketChargeMan());
        if(item.getProjectType().contains("施工")||item.getProjectType().contains("EPC")){
            deptMen.add(selectEmployeeService.getTechChargeMan());
            deptMen.add(selectEmployeeService.getEpcMisChargeMan());
            deptMen.add(selectEmployeeService.getContractChargeMan());
            deptMen.add(selectEmployeeService.getSecChargeMan());
        }
        actService.setVariables(item.getProcessInstanceId(),"deptMen", deptMen);
    }

    public void update(int bidApplyId,int bidAttendId) {
        BusinessBidAttend item = businessBidAttendMapper.selectByPrimaryKey(bidAttendId);
        BusinessBidApply businessBidApply = businessBidApplyMapper.selectByPrimaryKey(bidApplyId);
        item.setBidApplyId(bidApplyId);
        item.setProjectName(businessBidApply.getProjectName());
        item.setCustomerName(businessBidApply.getCustomerName());
        item.setBusinessType(businessBidApply.getBusinessType());
        item.setAttendType(businessBidApply.getAttendType());
        item.setProjectScale(businessBidApply.getProjectScale());
        item.setOpenTime(businessBidApply.getOpenTime());
        item.setGmtModified(new Date());
        businessBidAttendMapper.updateByPrimaryKey(item);





    }

    public BusinessBidAttendDto getModelById(int id) {
        return getDto(businessBidAttendMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin) {
        BusinessBidAttend item = new BusinessBidAttend();

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setProjectName("招标文件评审项目名称");
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setBusinessType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"经营模式").toString());
        item.setAttendType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"投标方式").toString());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型").toString());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        businessBidAttendMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);

        variables.put("processDescription", "项目负责人确认表");
        variables.put("haveContract", false);
        String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_ATTEND, item.getId(), variables);
        item.setProcessInstanceId(processInstanceId);
        businessBidAttendMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public int getNewModel(int bidApplyId, String userLogin) {

        if (isExistAttend(bidApplyId)){
            Assert.state(false, "项目名为："+businessBidApplyMapper.selectByPrimaryKey(bidApplyId).getProjectName()+" 的标前评审审批申请表已创建，请不要重复创建");
            return 0;
        }else {

            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
            BusinessBidAttend item = new BusinessBidAttend();
            BusinessBidApply businessBidApply = businessBidApplyMapper.selectByPrimaryKey(bidApplyId);

            item.setCreator(userLogin);
            item.setCreatorName(hrEmployeeDto.getUserName());
            item.setDeptId(hrEmployeeDto.getDeptId());
            item.setDeptName(hrEmployeeDto.getDeptName());

            item.setBidApplyId(bidApplyId);
            item.setProjectName(businessBidApply.getProjectName());
            item.setCustomerName(businessBidApply.getCustomerName());
            item.setBusinessType(businessBidApply.getBusinessType());
            item.setAttendType(businessBidApply.getAttendType());
            item.setProjectScale(businessBidApply.getProjectScale());
            item.setOpenTime(businessBidApply.getOpenTime());

            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());
            item.setDeleted(false);
            item.setProcessEnd(false);
            ModelUtil.setNotNullFields(item);
            businessBidAttendMapper.insert(item);
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "项目负责人确认表");
            variables.put("haveContract", false);
            String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_ATTEND, item.getId(), variables);
            item.setProcessInstanceId(processInstanceId);
            businessBidAttendMapper.updateByPrimaryKey(item);
            return item.getId();
        }
    }

    public BusinessBidAttendDto getDto(Object item) {
        BusinessBidAttendDto dto=BusinessBidAttendDto.adapt((BusinessBidAttend) item);
        BusinessBidAttend model= (BusinessBidAttend) item;
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            model.setProcessEnd(true);
            businessBidAttendMapper.updateByPrimaryKey(model);
            dto.setProcessEnd(true);
        }
        return dto;
    }

   /* 分页查找*/
    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum, int pageSize) {
        params.put("deleted",false);
        HrEmployeeSysDto hrEmployeeSys=hrEmployeeSysService.getModelByUserLogin(userLogin);
        if(!hrEmployeeSys.getDeptName().contains("市场管理")) {
            params.put("deptIdList", hrEmployeeSysService.getMyDeptList(userLogin));
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessBidAttendMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessBidAttend item=(BusinessBidAttend)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /*查询所有未参加评审的申报表*/
    public List<BusinessBidApply> listAllUnExistApply(int id) {

        BusinessBidAttend businessBidAttend = businessBidAttendMapper.selectByPrimaryKey(id);

        List<BusinessBidApply> list = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", businessBidAttend.getDeptId());
        //params.put("processEnd", true);
        List<BusinessBidApply> oList = businessBidApplyMapper.selectAll(params);

        oList.forEach(p -> {
            Map<String, Object> attendParams = Maps.newHashMap();
            attendParams.put("deleted", false);
            attendParams.put("bidApplyId", p.getId());
            attendParams.put("projectName", p.getProjectName());
            attendParams.put("processEnd",true);
            if (PageHelper.count(() -> businessBidAttendMapper.selectAll(attendParams)) == 0) {
                list.add(p);
            }
        });
        return list;
    }

    public boolean isExistAttend(int bidApplyId){
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("bidApplyId",bidApplyId);
        List<BusinessBidAttend> list = businessBidAttendMapper.selectAll(params);
        return list.size() > 0;

    }
     /*打印*/
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        BusinessBidAttend item = businessBidAttendMapper.selectByPrimaryKey(id);
        //基本数据
        data.put("deptName",item.getDeptName());
        data.put("projectName",item.getProjectName());
        data.put("customerName",item.getCustomerName());
        data.put("projectAddress",item.getProjectAddress());
        data.put("projectTime",item.getProjectTime());
        data.put("projectScale",item.getProjectScale());
        data.put("qualityRequest",item.getQualityRequest());
        data.put("tenderBond",item.getTenderBond());
        data.put("techStandard",item.getTechStandard());
        data.put("performanceBond",item.getPerformanceBond());
        data.put("businessType",item.getBusinessType());
        data.put("paymentRule",item.getPaymentRule());
        data.put("chargeMan",item.getChargeMan());
        data.put("creatorName",item.getCreatorName());

        data.put("gmtCreate",item.getGmtCreate());

        //表1数据
        data.put("openTime",item.getOpenTime());
        data.put("attendType",item.getAttendType());
        data.put("attendScope",item.getAttendScope());
        data.put("attendPrice",item.getAttendPrice());

        //流程数据
        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));
        return data;
    }
}
