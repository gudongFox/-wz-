package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceSelfBankMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceSelfBankDto;
import com.cmcu.mcc.finance.entity.FiveFinanceSelfBank;
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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveFinanceSelfBankService extends BaseService {

    @Resource
    FiveFinanceSelfBankMapper fiveFinanceSelfBankMapper;

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
    @Autowired
    FiveFinanceIncomeMapper fiveFinanceIncomeMapper;
    @Autowired
    ProcessQueryService processQueryService;

    public void remove(int id, String userLogin) {
        FiveFinanceSelfBank item = fiveFinanceSelfBankMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceSelfBankDto dto) {
        FiveFinanceSelfBank item = fiveFinanceSelfBankMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setUserLogin(dto.getUserLogin());
        item.setUserName(dto.getUserName());
        item.setBankName(dto.getBankName());
        item.setBankCode(dto.getBankCode());
        item.setUserBankNo(dto.getUserBankNo());
        item.setUserIdNo(dto.getUserIdNo());
        item.setTitle(dto.getTitle());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceSelfBankMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceSelfBankDto getModelById(int id) throws ParseException {
        return getDto(fiveFinanceSelfBankMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        FiveFinanceSelfBank item = new FiveFinanceSelfBank();
        HrEmployeeDto hrEmployeeDto= hrEmployeeService.getModelByUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        item.setUserName(hrEmployeeDto.getUserName());
        item.setUserLogin(hrEmployeeDto.getUserLogin());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);

        ModelUtil.setNotNullFields(item);
        fiveFinanceSelfBankMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_SELFBANK;
        String businessKey=processKey+"_"+item.getId();
        String title ="个人网银信息管理: "+item.getUserName();
        item.setTitle(title);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", title);
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("deptChargeLeader",selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);

        fiveFinanceSelfBankMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public FiveFinanceSelfBankDto getDto(Object item)  {
        FiveFinanceSelfBank model= (FiveFinanceSelfBank) item;
        FiveFinanceSelfBankDto dto=FiveFinanceSelfBankDto.adapt(model);
        dto.setProcessName("已完成");
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            //MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveFinanceSelfBankMapper.updateByPrimaryKey(dto);
            }
        }
        return dto;
    }

    public List<FiveFinanceSelfBank> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceSelfBankMapper.selectAll(params);
    }



    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        params.remove("userLogin");
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceSelfBankMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceSelfBank item=(FiveFinanceSelfBank)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public PageInfo<Object> selectAllWithSuccess(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.remove("userLogin");
        params.remove("userBankNo");

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        //params.put("processEnd",true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceSelfBankMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceSelfBank item=(FiveFinanceSelfBank)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
