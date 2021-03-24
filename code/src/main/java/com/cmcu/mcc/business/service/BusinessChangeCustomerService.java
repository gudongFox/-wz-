package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessChangeCustomerMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dto.BusinessChangeCustomerDto;
import com.cmcu.mcc.business.entity.BusinessChangeCustomer;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;


@Service
public class BusinessChangeCustomerService extends BaseService {

    @Resource
    BusinessChangeCustomerMapper businessChangeCustomerMapper;


    @Resource
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    MyActService myActService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    ProcessQueryService processQueryService;


    public void remove(int id, String userLogin) {

        BusinessChangeCustomer model = businessChangeCustomerMapper.selectByPrimaryKey(id);
        //流程作废
        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);

    }

    public void update(BusinessChangeCustomerDto dto) {
        BusinessChangeCustomer model = businessChangeCustomerMapper.selectByPrimaryKey(dto.getId());
        model.setChangeContent(dto.getChangeContent());
        model.setTitle(dto.getTitle());
        model.setCustomerId(dto.getCustomerId());
        model.setCustomerName(dto.getCustomerName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());
        model.setSeq(0);

        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        variables.put("processDescription", "客户信息变更申请:"+model.getCreatorName());
        variables.put("title",model.getTitle());
        myActService.setVariables(model.getProcessInstanceId(),variables);

        businessChangeCustomerMapper.updateByPrimaryKey(model);
    }


    public BusinessChangeCustomerDto getModelById(int id) {
        return getDto(businessChangeCustomerMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin,String uiSref) {

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        BusinessChangeCustomer item = new BusinessChangeCustomer();
        item.setDeleted(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setSeq(0);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
        ModelUtil.setNotNullFields(item);
        businessChangeCustomerMapper.insert(item);
        item.setTitle("客户信息变更："+hrEmployeeDto.getHrEmployee().getUserName());

        item.setBusinessKey(EdConst.FIVE_BUSINESS_CHANGECUSTOMER+"_" + item.getId());
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription",item.getTitle());

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_CHANGECUSTOMER,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        businessChangeCustomerMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public BusinessChangeCustomerDto getDto(BusinessChangeCustomer item) {
        BusinessChangeCustomerDto dto = BusinessChangeCustomerDto.adapt((BusinessChangeCustomer) item);
        dto.setProcessName("已完成");
        if (!item.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

            if(customProcessInstance==null||customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                businessChangeCustomerMapper.updateByPrimaryKey(dto);
            }

            if(customProcessInstance!=null&& StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
             //2021-03-02 HNZ 好像没用用了 暂时屏蔽
            //dto.setAttendUser(myHistoryService.getAttendUser(dto.getProcessInstanceId()));
        }


        return dto;
    }

    public PageInfo<BusinessChangeCustomer> loadPagedData(String userLogin,String uiSref,Map<String, Object> params, int pageNum, int pageSize) {
        params.put("deleted", false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<BusinessChangeCustomer> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessChangeCustomerMapper.selectAll(params));
        List<BusinessChangeCustomer> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto(p)));
        pageInfo.setList(list);
        return pageInfo;
    }


    public List<FiveBusinessContractLibrary> listCooperationProject(int changeCustomerId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("changeCustomerId", changeCustomerId);
        List<FiveBusinessContractLibrary> fiveBusinessContractLibraries = fiveBusinessContractLibraryMapper.selectAll(params);
        return fiveBusinessContractLibraries;
    }

}
