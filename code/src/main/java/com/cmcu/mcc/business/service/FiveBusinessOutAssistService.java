package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.FiveBusinessOutAssistDetailDto;
import com.cmcu.mcc.business.dto.FiveBusinessOutAssistDto;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.entity.FiveBusinessOutAssist;
import com.cmcu.mcc.business.entity.FiveBusinessOutAssistDetail;
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
public class FiveBusinessOutAssistService {

    @Resource
    FiveBusinessOutAssistMapper fiveBusinessOutAssistMapper;

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
    FiveBusinessOutAssistDetailMapper fiveBusinessOutAssistDetailMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;

    @Resource
    JdbcTemplate jdbcTemplate;

    @Autowired
    ProcessQueryService processQueryService;


    public void remove(int id, String userLogin) {
        FiveBusinessOutAssist item = fiveBusinessOutAssistMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBusinessOutAssistDto dto) {
        FiveBusinessOutAssist item = fiveBusinessOutAssistMapper.selectByPrimaryKey(dto.getId());
        //如果选择合同
        if(dto.getContractId()!=0){
            //之前选择还原
            if(item.getContractId()!=0){
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractId());
                library.setStampTaxId(0);
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getContractId());
            library.setStampTaxId(item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        item.setContractId(dto.getContractId());
        item.setContractNo(dto.getContractNo());
        item.setContractName(dto.getContractName());
        item.setProjectName(dto.getProjectName());
        item.setProjectNo(dto.getProjectNo());
        item.setCustomerName(dto.getCustomerName());
        item.setSignTime(dto.getSignTime());
        item.setContractMoney(dto.getContractMoney());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setApplyMonth(dto.getApplyMonth());
        item.setTitle(dto.getTitle());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        BeanValidator.check(item);
        fiveBusinessOutAssistMapper.updateByPrimaryKey(item);

        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
    }

    public FiveBusinessOutAssistDto getModelById(int id) {
        return getDto(fiveBusinessOutAssistMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBusinessOutAssist item = new FiveBusinessOutAssist();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        item.setApplyMonth(sdf.format(new Date()));
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setTitle("月度外协费用支出明细:"+item.getCreatorName());
        ModelUtil.setNotNullFields(item);
        fiveBusinessOutAssistMapper.insert(item);

        String processKey=EdConst.FIVE_BUSINESS_OUTASSIST;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", item.getTitle());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//经营发展部领导
        variables.put("financeDeptChargeMan", selectEmployeeService.getDeptChargeMen(18));//财务金融部负责人
        variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//经营发展部分管领导
        variables.put("totalManger", hrEmployeeService.selectUserByPositionName("总经理"));//总经理
        variables.put("totalAccount", hrEmployeeService.selectUserByPositionName("总会计师"));//总会计师

        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveBusinessOutAssistMapper.updateByPrimaryKey(item);
        //新增默认子表数据
        // initDetail(item.getId());
        return item.getId();
    }


    public FiveBusinessOutAssistDto getDto(Object item) {
        FiveBusinessOutAssist model= (FiveBusinessOutAssist) item;
        FiveBusinessOutAssistDto dto=FiveBusinessOutAssistDto.adapt(model);
        dto.setProcessName("已完成");
        if(!dto.getProcessEnd() && StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            //MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBusinessOutAssistMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        update(dto);
        return dto;
    }

    public FiveBusinessOutAssistDetailDto getDetailDto(Object item) {
        FiveBusinessOutAssistDetail model= (FiveBusinessOutAssistDetail) item;
        FiveBusinessOutAssistDetailDto detailDto=FiveBusinessOutAssistDetailDto.adapt(model);
        return detailDto;
    }

    public List<FiveBusinessOutAssist> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        return  fiveBusinessOutAssistMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessOutAssistMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessOutAssist item=(FiveBusinessOutAssist)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<FiveBusinessOutAssistDetailDto> getDetailById(int id) {
        List<FiveBusinessOutAssistDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("outAssistId",id);
        List<FiveBusinessOutAssistDetail> details = fiveBusinessOutAssistDetailMapper.selectAll(map);
        for(FiveBusinessOutAssistDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public void updateDetail(FiveBusinessOutAssistDetail item,int outAssistId){
        FiveBusinessOutAssistDetail model;
        if(item.getId()!=null){
            model = fiveBusinessOutAssistDetailMapper.selectByPrimaryKey(item.getId());
            model.setContractId(item.getContractId());
            model.setContractNo(item.getContractNo());
            model.setContractName(item.getContractName());
            model.setProjectName(item.getProjectName());
            model.setProjectNo(item.getProjectNo());
            model.setCustomerName(item.getCustomerName());
            model.setSignTime(item.getSignTime());
            model.setContractMoney(item.getContractMoney());

            model.setShouldPayMoney(item.getShouldPayMoney());
            model.setRealPayMoney(item.getRealPayMoney());
            model.setAssistOutDept(item.getAssistOutDept());
            model.setCreator(item.getCreator());
            model.setCreatorName(item.getCreatorName());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBusinessOutAssistDetailMapper.updateByPrimaryKey(model);
        }else{
            model =new FiveBusinessOutAssistDetail();
            model.setContractId(item.getContractId());
            model.setContractNo(item.getContractNo());
            model.setContractName(item.getContractName());
            model.setProjectName(item.getProjectName());
            model.setProjectNo(item.getProjectNo());
            model.setCustomerName(item.getCustomerName());
            model.setSignTime(item.getSignTime());
            model.setContractMoney(item.getContractMoney());
            model.setShouldPayMoney(item.getShouldPayMoney());
            model.setRealPayMoney(item.getRealPayMoney());
            model.setAssistOutDept(item.getAssistOutDept());
            model.setRemark(item.getRemark());

            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
            model.setCreatorName(hrEmployeeDto.getUserName());
            model.setOutAssistId(outAssistId);
            model.setCreator(item.getCreator());
            model.setCreatorName(item.getCreatorName());
            model.setGmtModified(new Date());
            model.setGmtCreate(new Date());
            model.setDeleted(false);
            ModelUtil.setNotNullFields(model);
            fiveBusinessOutAssistDetailMapper.insert(model);
            item.setId(model.getId());
        }

    }

    public FiveBusinessOutAssistDetailDto getNewModelDetail(int  id,String userLogin){
        FiveBusinessOutAssistDetail item=new FiveBusinessOutAssistDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setOutAssistId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        //fiveBusinessOutAssistDetailMapper.insert(item);
        return getDetailDto(item);
    }

    public FiveBusinessOutAssistDetail getModelDetailById(int id){
        return getDetailDto(fiveBusinessOutAssistDetailMapper.selectByPrimaryKey(id));
    }

    public void removeDetail(int id){
        FiveBusinessOutAssistDetail model = fiveBusinessOutAssistDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBusinessOutAssistDetailMapper.updateByPrimaryKey(model);
    }



    public List<Map> downData(String userLogin,int outAssistId) {
        List<Map> list = Lists.newArrayList();

        String  sql ="SELECT assist_out_dept as '外协单位名称',contract_name as '合同名称',contract_no as '合同号',project_name as'项目名称',project_no as'项目号',customer_name as'客户名称',sign_time as'签订时间',contract_money as '合同金额',\n" +
                "should_pay_money as'应付款（万元）',real_pay_money as'实付款（万元）',gmt_create as'创建时间',creator_name as'创建人'" +
                " FROM db_wuzhou.five_business_out_assist_detail where out_assist_id ="+outAssistId+" and is_deleted =0";


        try {
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(sql);
            list.addAll(oList);
        } catch (Exception ex) {

        }
        return list;
    }
}
