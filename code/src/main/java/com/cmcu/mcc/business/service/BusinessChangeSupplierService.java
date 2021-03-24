package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessChangeSupplierMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dto.BusinessChangeSupplierDto;
import com.cmcu.mcc.business.entity.BusinessChangeSupplier;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BusinessChangeSupplierService extends BaseService {

    @Resource
    BusinessChangeSupplierMapper businessChangeSupplierMapper;


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

        BusinessChangeSupplier model = businessChangeSupplierMapper.selectByPrimaryKey(id);
        //流程作废
        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);

    }

    public void update(BusinessChangeSupplierDto dto) {
        BusinessChangeSupplier model = businessChangeSupplierMapper.selectByPrimaryKey(dto.getId());
        model.setChangeContent(dto.getChangeContent());
        model.setTitle(dto.getTitle());
        model.setSupplierId(dto.getSupplierId());
        model.setSupplierName(dto.getSupplierName());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());
        model.setSeq(0);

        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        variables.put("processDescription", "供方信息变更申请:"+model.getCreatorName());
        variables.put("title",model.getTitle());
        myActService.setVariables(model.getProcessInstanceId(),variables);

        businessChangeSupplierMapper.updateByPrimaryKey(model);
    }


    public BusinessChangeSupplierDto getModelById(int id) {
        return getDto(businessChangeSupplierMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin,String uiSref) {

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        BusinessChangeSupplier item = new BusinessChangeSupplier();
        item.setDeleted(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setSeq(0);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
        ModelUtil.setNotNullFields(item);
        businessChangeSupplierMapper.insert(item);
        item.setTitle("供方信息变更："+hrEmployeeDto.getHrEmployee().getUserName());

        item.setBusinessKey(EdConst.FIVE_BUSINESS_CHANGESUPPLIER+"_" + item.getId());
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription",item.getTitle());

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_CHANGESUPPLIER,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        businessChangeSupplierMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public BusinessChangeSupplierDto getDto(BusinessChangeSupplier item) {
        BusinessChangeSupplierDto dto = BusinessChangeSupplierDto.adapt((BusinessChangeSupplier) item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

           // dto.setAttendUser(myHistoryService.getAttendUser(dto.getProcessInstanceId()));

            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                businessChangeSupplierMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null&& StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public PageInfo<BusinessChangeSupplier> loadPagedData(String userLogin,String uiSref,Map<String, Object> params, int pageNum, int pageSize) {
        params.put("deleted", false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<BusinessChangeSupplier> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessChangeSupplierMapper.selectAll(params));
        List<BusinessChangeSupplier> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto(p)));
        pageInfo.setList(list);
        return pageInfo;
    }


    public List<FiveBusinessContractLibrary> listCooperationProject(int changeSupplierId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("changeSupplierId", changeSupplierId);
        List<FiveBusinessContractLibrary> fiveBusinessContractLibraries = fiveBusinessContractLibraryMapper.selectAll(params);
        return fiveBusinessContractLibraries;
    }

}
