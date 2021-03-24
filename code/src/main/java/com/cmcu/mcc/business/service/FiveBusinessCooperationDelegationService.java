package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonCodeMapper;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.FiveBusinessCooperationDelegationDto;
import com.cmcu.mcc.business.entity.FiveBusinessCooperationDelegation;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveBusinessCooperationDelegationService extends BaseService {

    @Resource
    FiveBusinessCooperationDelegationMapper fiveBusinessCooperationDelegationMapper;

    @Resource
    CommonCodeService commonCodeService;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    HrDeptService hrDeptService;

    @Autowired
    MyActService myActService;

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Resource
    HandleFormService handleFormService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    ProcessQueryService processQueryService;


    public void remove(int id, String userLogin) {
        FiveBusinessCooperationDelegation item = fiveBusinessCooperationDelegationMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBusinessCooperationDelegationDto dto) {
        FiveBusinessCooperationDelegation item = fiveBusinessCooperationDelegationMapper.selectByPrimaryKey(dto.getId());

        item.setRecordId(dto.getRecordId());
        item.setProjectName(dto.getProjectName());
        item.setProjectNo(dto.getProjectNo());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setInteriorProjectNo(dto.getInteriorProjectNo());
        item.setInteriorProjectName(dto.getInteriorProjectName());
        item.setInteriorProjectType(dto.getInteriorProjectType());
        item.setDelegationDeptId(dto.getDelegationDeptId());
        item.setDelegationDeptName(dto.getDelegationDeptName());
        item.setCooperationDeptName(dto.getCooperationDeptName());
        item.setCooperationDeptId(dto.getCooperationDeptId());
        item.setDeptId(dto.getDelegationDeptId());
        item.setDeptName(dto.getDelegationDeptName());
        item.setDelegationTime(dto.getDelegationTime());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        item.setTitle(dto.getTitle());
        BeanValidator.check(item);
        fiveBusinessCooperationDelegationMapper.updateByPrimaryKey(item);

        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        if(item.getDelegationDeptId()!=0){
            variables.put("delegationDeptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDelegationDeptId()));//委托方第一负责人
            variables.put("delegationDeptLeader",selectEmployeeService.getOtherDeptChargeMan(item.getDelegationDeptId()));//委托方分管领导
        }
        if(item.getCooperationDeptId()!=0){
            variables.put("cooperationDeptChargeMen",selectEmployeeService.getDeptChargeMen(item.getCooperationDeptId()));//协作方第一负责人
            variables.put("cooperationDeptLeader",selectEmployeeService.getOtherDeptChargeMan(item.getCooperationDeptId()));//协作方分管领导
        }
        variables.put("businessChargeMen",selectEmployeeService.getDeptChargeMen(48));//经营发展部
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
    }

    public FiveBusinessCooperationDelegationDto getModelById(int id) {
        return getDto(fiveBusinessCooperationDelegationMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBusinessCooperationDelegation item = new FiveBusinessCooperationDelegation();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        item.setInteriorProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());
        item.setDelegationTime(sdf.format(new Date()));
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setTitle("经营合作项目确认:"+item.getCreatorName());
        ModelUtil.setNotNullFields(item);
        fiveBusinessCooperationDelegationMapper.insert(item);

        String processKey=EdConst.FIVE_BUSINESS_COOPERATION_DELEGATION;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", item.getTitle());


        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveBusinessCooperationDelegationMapper.updateByPrimaryKey(item);

        return item.getId();
    }


    public FiveBusinessCooperationDelegationDto getDto(Object item) {
        FiveBusinessCooperationDelegation model= (FiveBusinessCooperationDelegation) item;
        FiveBusinessCooperationDelegationDto dto=FiveBusinessCooperationDelegationDto.adapt(model);
        dto.setProcessName("已完成");
        if(!dto.getProcessEnd() && StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBusinessCooperationDelegationMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    //内部合作合同 选择使用
    public List<FiveBusinessCooperationDelegation> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        return  fiveBusinessCooperationDelegationMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessCooperationDelegationMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessCooperationDelegation item=(FiveBusinessCooperationDelegation)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<Map> downData(String userLogin,int outAssistId) {
        List<Map> list = Lists.newArrayList();

        String  sql ="";


        try {
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(sql);
            list.addAll(oList);
        } catch (Exception ex) {

        }
        return list;
    }

    public void getInteriorProjectNo(int id) {
        try {
            FiveBusinessCooperationDelegationDto cooperationDelegationDto = getModelById(id);
            String newProjectNo = "";
            //年份代码 2019年  19
            String signYear = cooperationDelegationDto.getDelegationTime().split("-")[0];
            String year = signYear.substring(2);
            //承办单位代码 2位 01
            String deptCode= hrDeptService.getModelById(cooperationDelegationDto.getCooperationDeptId()).getDeptCode();
            //合同类别代码
            String typeCode="N";

            //如果  年份 单位 合同类型 没有改变 返回原合同号
            /*if (recordDto.getProjectNo().contains(year+deptCode+typeCode)){
                return recordDto.getProjectNo();
            }*/

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("projectNoHead",year+deptCode+typeCode);
            List<FiveBusinessCooperationDelegation> cooperationDelegations = fiveBusinessCooperationDelegationMapper.selectAll(params);
            int size=0;
            //判断顺序代码最大值
            if (!cooperationDelegations.isEmpty()){
                for (FiveBusinessCooperationDelegation cooperationDelegation:cooperationDelegations){
                    if(cooperationDelegation.getId()!=id&&StringUtils.isNotEmpty(cooperationDelegation.getInteriorProjectNo())){
                        String maxSize = cooperationDelegation.getInteriorProjectNo().substring(5);
                        if (size<Integer.parseInt(maxSize)){
                            size = Integer.parseInt(maxSize);
                        }
                    }
                }
            }
            size=size+1;
            String format = String.format("%03d", size);
            newProjectNo=newProjectNo+year+deptCode+typeCode+format;

            cooperationDelegationDto.setInteriorProjectNo(newProjectNo);
            update(cooperationDelegationDto);

        }catch (Exception e){
            Assert.state(false, "请准确填写，委托时间、协作方、备案项目！");
        }
    }
}
