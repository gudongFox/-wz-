package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.business.dao.BusinessRecordMapper;
import com.cmcu.mcc.business.entity.BusinessRecord;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaPlatformRecordDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaPlatformRecordMapper;
import com.cmcu.mcc.five.dto.FiveOaPlatformRecordDto;
import com.cmcu.mcc.five.entity.FiveOaPlatformRecord;
import com.cmcu.mcc.five.entity.FiveOaPlatformRecordDetail;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FiveOaPlatformRecordService {
    @Resource
    FiveOaPlatformRecordMapper fiveOaPlatformRecordMapper;
    @Resource
    FiveOaPlatformRecordDetailMapper fiveOaPlatformRecordDetailMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
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
    @Autowired
    BusinessRecordMapper businessRecordMapper;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id, String userLogin) {
        FiveOaPlatformRecord item = fiveOaPlatformRecordMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");

        //如果原来有备案 还原
        if (item.getRecordId() != 0) {
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(item.getRecordId());
            businessRecord.setPlatformId(0);
            businessRecordMapper.updateByPrimaryKey(businessRecord);
        }
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);

    }

    public void update(FiveOaPlatformRecordDto dto) {
        FiveOaPlatformRecord model = fiveOaPlatformRecordMapper.selectByPrimaryKey(dto.getId());
        model.setRecordNo(dto.getRecordNo());
        //如果选择项目备案
        if (dto.getRecordId() != 0) {
            //如果原来有备案 还原
            if (model.getRecordId() != 0) {
                BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
                businessRecord.setPlatformId(0);
                businessRecordMapper.updateByPrimaryKey(businessRecord);
            }
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(dto.getRecordId());
            businessRecord.setPlatformId(dto.getId());
            businessRecordMapper.updateByPrimaryKey(businessRecord);
            model.setRecordId(dto.getRecordId());
        }
        model.setRecordId(dto.getRecordId());
        model.setRecordName(dto.getRecordName());
        model.setProjectNo(dto.getProjectNo());
        model.setGmtModified(new Date());
        model.setRemark(dto.getRemark());
        BeanValidator.check(model);
        fiveOaPlatformRecordMapper.updateByPrimaryKey(model);
    }

    public FiveOaPlatformRecordDto getModelById(int id) {

        return getDto(fiveOaPlatformRecordMapper.selectByPrimaryKey(id));
    }

    public FiveOaPlatformRecordDto getDto(FiveOaPlatformRecord item) {
        FiveOaPlatformRecordDto dto = FiveOaPlatformRecordDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaPlatformRecordMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin) {
        List<String> business = hrEmployeeService.selectUserByRoleNames("经营管理");
        Assert.state(business.size() > 0, "未找到职务为 经营管理 人员");
        FiveOaPlatformRecord item = new FiveOaPlatformRecord();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaPlatformRecordMapper.insert(item);
        String businessKey = EdConst.FIVE_OA_PLATFORM_RECORD + "_" + item.getId();
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if (businessMen.size() != 0) {
            variables.put("businessMen", selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag", true);
        } else {
            variables.put("businessMenFlag", false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "各地公共资源平台：" + item.getDeptName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        variables.put("businessLeader", business);
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_PLATFORM_RECORD, item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaPlatformRecordMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptListOa(userLogin, uiSref);
        if (deptIdList.size() == 0) {
            params.put("creator", userLogin);
        } else {
            params.put("deptIdList", deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaPlatformRecordMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaPlatformRecord item = (FiveOaPlatformRecord) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void removeDetail(int id) {
        FiveOaPlatformRecordDetail model = fiveOaPlatformRecordDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaPlatformRecordDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaPlatformRecordDetail fiveOaPlatformRecordDetail) {
        FiveOaPlatformRecordDetail model = fiveOaPlatformRecordDetailMapper.selectByPrimaryKey(fiveOaPlatformRecordDetail.getId());
        model.setRecordMan(fiveOaPlatformRecordDetail.getRecordMan());
        model.setRecordTime(fiveOaPlatformRecordDetail.getRecordTime());
        model.setProvince(fiveOaPlatformRecordDetail.getProvince());
        model.setCity(fiveOaPlatformRecordDetail.getCity());
        model.setPlatformName(fiveOaPlatformRecordDetail.getPlatformName());
        model.setPlatformUrl(fiveOaPlatformRecordDetail.getPlatformUrl());
        model.setRecordContent(fiveOaPlatformRecordDetail.getRecordContent());
        model.setRecordValidity(fiveOaPlatformRecordDetail.getRecordValidity());
        model.setPassword(fiveOaPlatformRecordDetail.getPassword());
        model.setPasswordCustodian(fiveOaPlatformRecordDetail.getPasswordCustodian());
        model.setRemark(fiveOaPlatformRecordDetail.getRemark());
        fiveOaPlatformRecordDetailMapper.updateByPrimaryKey(model);
    }

    public int getNewModelDetail(int recordId) {
        FiveOaPlatformRecordDetail item = new FiveOaPlatformRecordDetail();
        FiveOaPlatformRecordDto dto = getModelById(recordId);
        item.setRecordId(recordId);
        item.setRecordTime(MyDateUtil.getStringDateShort());
        item.setProvince("北京市");
        item.setCity("西城区");
        item.setPassword(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);

        item.setRecordMan(dto.getCreatorName());
        ModelUtil.setNotNullFields(item);
        fiveOaPlatformRecordDetailMapper.insert(item);
        return item.getId();

    }

    public FiveOaPlatformRecordDetail getModelDetailById(int id) {
        return fiveOaPlatformRecordDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaPlatformRecordDetail> listDetail(int recordId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("recordId", recordId);
        List<FiveOaPlatformRecordDetail> list = fiveOaPlatformRecordDetailMapper.selectAll(params);
        return list;
    }

    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaPlatformRecord item = fiveOaPlatformRecordMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName", item.getDeptName());
        data.put("recordNo", item.getRecordNo());

        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        Map map = new HashMap();
        map.put("recordId", item.getId());
        map.put("deleted", false);
        List<FiveOaPlatformRecordDetail> recordDetails = fiveOaPlatformRecordDetailMapper.selectAll(map);
        data.put("recordDetails", recordDetails);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto : actHistoryDtos) {
            if (dto.getActivityName() == null) {
                break;
            }
            if ("发起人".equals(dto.getActivityName())) {
                data.put("startMen", dto);
            }
            if ("部门领导-审批".equals(dto.getActivityName())) {
                data.put("deptChargeMen", dto);
            }
            if ("经营发展部-审批".equals(dto.getActivityName())) {
                data.put("manageDevelopmentDept", dto);
            }
        }
        data.put("nodes", actHistoryDtos);

        return data;
    }

    /**
     * 五洲 查询当前登录人权限下 所有已完成的项目信息备案
     *
     * @param userLogin
     * @return
     */
    public List<BusinessRecord> listRecordByUserLogin(String userLogin, int recordId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,"five.businessRecord");
        params.put("deptIdList",deptIdList);*/
        // params.put("processEnd",true);
        List<BusinessRecord> businessRecords = businessRecordMapper.selectAll(params).stream().filter(p -> p.getPlatformId() == 0).collect(Collectors.toList());
        //如果有备案 添加备案
        if (recordId != 0) {
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(recordId);
            businessRecords.add(businessRecord);
        }
        return businessRecords;
    }
}
