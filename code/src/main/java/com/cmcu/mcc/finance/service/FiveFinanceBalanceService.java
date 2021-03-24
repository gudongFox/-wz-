package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceBalanceMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceBalanceDto;
import com.cmcu.mcc.finance.entity.FiveFinanceBalance;
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
import java.util.*;

@Service
public class FiveFinanceBalanceService extends BaseService {

    @Resource
    FiveFinanceBalanceMapper fiveFinanceBalanceMapper;

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

    public void remove(int id, String userLogin) {
        FiveFinanceBalance item = fiveFinanceBalanceMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceBalanceDto dto) {
        FiveFinanceBalance item = fiveFinanceBalanceMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "资金余额上报:"+item.getRemark());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceBalanceMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceBalanceDto getModelById(int id) throws ParseException {
        return getDto(fiveFinanceBalanceMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        FiveFinanceBalance item = new FiveFinanceBalance();
        HrEmployeeDto hrEmployeeDto= hrEmployeeService.getModelByUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);

        ModelUtil.setNotNullFields(item);
        fiveFinanceBalanceMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_BALANCE;
/*        String processKey="test";*/
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
/*        List list = new ArrayList();
        list.add("1461");
        list.add("9254");
        list.add("2700");
        variables.put("users",list);*/
        variables.put("processDescription", "财务资金上报");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceBalanceMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public FiveFinanceBalanceDto getDto(Object item) throws ParseException {
        FiveFinanceBalance model= (FiveFinanceBalance) item;
        FiveFinanceBalanceDto dto=FiveFinanceBalanceDto.adapt(model);
        dto.setProcessName("已完成");

        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            //MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }
            //dto.setBusinessKey(processInstanceDto.getBusinessKey());
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveFinanceBalanceMapper.updateByPrimaryKey(dto);
            }
        }
        return dto;
    }

    public List<FiveFinanceBalance> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceBalanceMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceBalanceMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceBalance item=(FiveFinanceBalance)p;
            try {
                list.add(getDto(item));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
