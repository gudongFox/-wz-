package com.cmcu.mcc.business.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.business.dao.BusinessBidAttendMapper;
import com.cmcu.mcc.business.dao.BusinessBidContractMapper;
import com.cmcu.mcc.business.dao.BusinessBidProjectChargeMapper;
import com.cmcu.mcc.business.dto.BusinessBidContractDto;
import com.cmcu.mcc.business.entity.BusinessBidAttend;
import com.cmcu.mcc.business.entity.BusinessBidContract;
import com.cmcu.mcc.business.entity.BusinessBidProjectCharge;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BusinessBidContractService {
    @Resource
    BusinessBidContractMapper businessBidContractMapper;
    @Resource
    BusinessBidProjectChargeMapper businessBidProjectChargeMapper;
    @Resource
    BusinessBidAttendMapper businessBidAttendMapper;

    @Autowired
    ActService actService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;




    public void remove(int id, String userLogin) {

        BusinessBidContract item = businessBidContractMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        businessBidContractMapper.updateByPrimaryKey(item);

    }

    public void update(BusinessBidContractDto businessBidContractDto) {
        BusinessBidContract item = businessBidContractMapper.selectByPrimaryKey(businessBidContractDto.getId());
        if (businessBidContractDto.getBidProjectChargeId()!=0){
        if (!businessBidProjectChargeMapper.selectByPrimaryKey(businessBidContractDto.getBidProjectChargeId())
                .getProjectName().equals(businessBidContractDto.getProcessName())){
            item.setBidProjectChargeId(0);
            item.setBidApplyId(0);
            item.setBidAttendId(0);
        }else {
            item.setBidProjectChargeId(businessBidContractDto.getBidProjectChargeId());
            item.setBidApplyId(businessBidContractDto.getBidApplyId());
            item.setBidAttendId(businessBidContractDto.getBidAttendId());
        }
        }
        item.setProjectName(businessBidContractDto.getProjectName());
        item.setCustomerName(businessBidContractDto.getCustomerName());
        item.setProjectAddress(businessBidContractDto.getProjectAddress());
        item.setProjectTime(businessBidContractDto.getProjectTime());
        item.setProjectScale(businessBidContractDto.getProjectScale());
        item.setProjectType(businessBidContractDto.getProjectType());
        item.setProjectQuality(businessBidContractDto.getProjectQuality());
        item.setContractMoney(businessBidContractDto.getContractMoney());
        item.setContractRate(businessBidContractDto.getContractRate());
        item.setContractType(businessBidContractDto.getContractType());
        item.setGuaranteeRule(businessBidContractDto.getGuaranteeRule());
        item.setBusinessType(businessBidContractDto.getBusinessType());
        item.setPaymentRule(businessBidContractDto.getPaymentRule());
        item.setRemark(businessBidContractDto.getRemark());
        item.setProjectContent(businessBidContractDto.getProjectContent());
        item.setGmtModified(new Date());


        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(item.getDeptId());
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(item.getDeptName()+"没有配置部门负责人!");

        actService.setVariables(item.getProcessInstanceId(),"deptChargeMen", deptChargeMen);
        actService.setVariables(item.getProcessInstanceId(),"processDescription", item.getCustomerName()+" "+item.getProjectName());
        businessBidContractMapper.updateByPrimaryKey(item);
    }

    public void update(int projectChargeId,int id){
        BusinessBidContract item = businessBidContractMapper.selectByPrimaryKey(id);
        BusinessBidProjectCharge businessBidProjectCharge = businessBidProjectChargeMapper.selectByPrimaryKey(projectChargeId);
        BusinessBidAttend businessBidAttend = businessBidAttendMapper.selectByPrimaryKey(businessBidProjectCharge.getBidAttendId());

        item.setBidProjectChargeId(projectChargeId);
        item.setProjectContent(businessBidProjectCharge.getProjectContent());//工程内容
        item.setProjectName(businessBidProjectCharge.getProjectName());//项目名称
        item.setProjectScale(businessBidProjectCharge.getProjectScale());//工程类别规模
        item.setProjectAddress(businessBidProjectCharge.getProjectAddress());//工程地址
        item.setProjectType(businessBidProjectCharge.getProjectType());

        item.setBidAttendId(businessBidProjectCharge.getBidAttendId());
        item.setProjectTime(businessBidAttend.getProjectTime());//合同工期
        item.setProjectQuality(businessBidAttend.getQualityRequest());//质量要求
        item.setPaymentRule(businessBidAttend.getPaymentRule());//工程款支付条件
        item.setBusinessType(businessBidAttend.getBusinessType());//经营性质

        item.setBidApplyId(businessBidAttend.getBidApplyId());
        item.setGmtModified(new Date());

        businessBidContractMapper.updateByPrimaryKey(item);
    }

    public int getNewModel(String userLogin) {
        BusinessBidContract item=new BusinessBidContract();

        item.setProjectName("示例项目名称");
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());

        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId());
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(hrEmployeeDto.getDeptName()+"没有配置部门负责人!");

        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setBusinessType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"经营模式").toString());
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型").toString());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"设计作业类型").toString());
        item.setContractMoney(new BigDecimal(0));
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        businessBidContractMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen",deptChargeMen);
        variables.put("processDescription", "经营(经济)合同评审表");
        variables.put("marketMan",selectEmployeeService.getMarketChargeMan());
        variables.put("contractMan",selectEmployeeService.getContractChargeMan());
        variables.put("techMan",selectEmployeeService.getTechChargeMan());
        variables.put("financeMan",selectEmployeeService.getFinanceChargeMan());
        variables.put("secMan",selectEmployeeService.getSecChargeMan());
        variables.put("lawMan",selectEmployeeService.getLawChargeMan());

        String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_CONTRACT,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        businessBidContractMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public int getNewModel(int projectChargeId,String userLogin) {
        if (isExistContract(projectChargeId)) {
            Assert.state(false, "项目名为：" + businessBidProjectChargeMapper.selectByPrimaryKey(projectChargeId).getProjectName() + " 的经营合同申请表已创建，请不要重复创建");
            return 0;
        }

        BusinessBidContract item = new BusinessBidContract();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        BusinessBidProjectCharge businessBidProjectCharge = businessBidProjectChargeMapper.selectByPrimaryKey(projectChargeId);
        BusinessBidAttend businessBidAttend = businessBidAttendMapper.selectByPrimaryKey(businessBidProjectCharge.getBidAttendId());
        item.setCreator(userLogin);
        item.setContractMoney(new BigDecimal(0));
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型").toString());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setBidProjectChargeId(projectChargeId);
        item.setProjectContent(businessBidProjectCharge.getProjectContent());//工程内容
        item.setProjectName(businessBidProjectCharge.getProjectName());//项目名称
        item.setProjectScale(businessBidProjectCharge.getProjectScale());//工程类别规模
        item.setProjectType(businessBidProjectCharge.getProjectType());
        item.setProjectAddress(businessBidProjectCharge.getProjectAddress());//工程地址

        item.setBidAttendId(businessBidProjectCharge.getBidAttendId());
        item.setProjectTime(businessBidAttend.getProjectTime());//合同工期
        item.setProjectQuality(businessBidAttend.getQualityRequest());//质量要求
        item.setPaymentRule(businessBidAttend.getPaymentRule());//工程款支付条件
        item.setBusinessType(businessBidAttend.getBusinessType());//经营性质
        item.setBidApplyId(businessBidAttend.getBidApplyId());


        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);

        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(item.getDeptId());
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(item.getDeptName()+"没有配置部门负责人!");

        businessBidContractMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan",deptChargeMen);
        variables.put("processDescription", "经营(经济)合同评审表");
        variables.put("marketMan",selectEmployeeService.getMarketChargeMan());
        variables.put("contractMan",selectEmployeeService.getContractChargeMan());
        variables.put("techMan",selectEmployeeService.getTechChargeMan());
        variables.put("financeMan",selectEmployeeService.getFinanceChargeMan());
        variables.put("secMan",selectEmployeeService.getSecChargeMan());
        variables.put("lawMan",selectEmployeeService.getLawChargeMan());

        String processInstanceId = actService.startProcess(EdConst.BUSINESS_BID_CONTRACT, item.getId(), variables);
        item.setProcessInstanceId(processInstanceId);
        businessBidContractMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public BusinessBidContractDto getModelById(int id) {
        return getDto(businessBidContractMapper.selectByPrimaryKey(id));
    }

    public BusinessBidContractDto getDto(Object item) {
        BusinessBidContractDto dto=BusinessBidContractDto.adapt((BusinessBidContract) item);
        BusinessBidContract model= (BusinessBidContract) item;
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            model.setProcessEnd(true);
            businessBidContractMapper.updateByPrimaryKey(model);
            dto.setProcessEnd(true);
        }
        return dto;
    }

    /* 分页查找*/
    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin ,int pageNum, int pageSize) {
        params.put("deleted",false);
        HrEmployeeSysDto hrEmployeeSys=hrEmployeeSysService.getModelByUserLogin(userLogin);
        if(!hrEmployeeSys.getDeptName().contains("市场管理")) {
            params.put("deptIdList", hrEmployeeSysService.getMyDeptList(userLogin));
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessBidContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessBidContract item=(BusinessBidContract)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public List<BusinessBidProjectCharge> listAllUnExistProjectCharge(int id){

        BusinessBidContract businessBidContract = businessBidContractMapper.selectByPrimaryKey(id);

        List<BusinessBidProjectCharge> list = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", businessBidContract.getDeptId());
        //params.put("processEnd", true);
        List<BusinessBidProjectCharge> oList = businessBidProjectChargeMapper.selectAll(params);

        oList.forEach(p -> {
            Map<String, Object> attendParams = Maps.newHashMap();
            attendParams.put("deleted", false);
            attendParams.put("bidProjectChargeId", p.getId());
            attendParams.put("projectName", p.getProjectName());
            attendParams.put("processEnd",true);
            if (PageHelper.count(() -> businessBidContractMapper.selectAll(attendParams)) == 0) {
                list.add(p);
            }
        });
        return list;
    }

    public boolean isExistContract(int bidProjectChargeId){
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("bidProjectChargeId",bidProjectChargeId);
        List<BusinessBidContract> list = businessBidContractMapper.selectAll(params);
        return list.size() > 0;

    }
    /*打印*/
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        BusinessBidContract item = businessBidContractMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("projectName",item.getProjectName());
        data.put("customerName",item.getCustomerName());
        data.put("projectAddress",item.getProjectAddress());
        data.put("projectTime",item.getProjectTime());
        data.put("projectScale",item.getProjectScale());
        data.put("projectQuality",item.getProjectQuality());
        data.put("projectContent",item.getProjectContent());
        data.put("contractMoney",item.getContractMoney());
        data.put("contractRate",item.getContractRate());
        data.put("contractType",item.getContractType());
        data.put("guaranteeRule",item.getGuaranteeRule());
        data.put("businessType",item.getBusinessType());
        data.put("paymentRule",item.getPaymentRule());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());
        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));
        return data;
    }


}
