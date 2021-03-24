package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
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
import com.cmcu.mcc.finance.dao.FiveFinanceOutSupplyMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceOutSupplyDto;
import com.cmcu.mcc.finance.entity.FiveFinanceOutSupply;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveFinanceOutSupplyService{

    @Resource
    FiveFinanceOutSupplyMapper fiveFinanceOutSupplyMapper;

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
        FiveFinanceOutSupply item = fiveFinanceOutSupplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceOutSupplyDto dto) {
        FiveFinanceOutSupply item = fiveFinanceOutSupplyMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setType(dto.getType());

        item.setBeginYear(dto.getBeginYear());
        item.setEndYear(dto.getEndYear());

        item.setBankBalance(dto.getBankBalance());
        item.setAssetLiabilities(dto.getAssetLiabilities());
        item.setProfitsAllocation(dto.getProfitsAllocation());
        item.setCash(dto.getCash());
        item.setOther(dto.getOther());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        List<String> types = MyStringUtil.getStringList(item.getType());
        List<String> copyUsers =new ArrayList<>();
        Map variables=Maps.newHashMap();
        if(types.contains("资信证明")||types.contains("保函")){
            //五特 五环
            if(item.getDeptId().equals(34)||item.getDeptId().equals(45)
        ||selectEmployeeService.getHeadDeptId(item.getDeptId())==34
            ||selectEmployeeService.getHeadDeptId(item.getDeptId())==45){
                copyUsers.add("9523");//李明月
            } else{
                copyUsers.add("4043");// 刁子莺
            }
        }
        if(types.contains("财务报表")){
            //五特 五环
            if(item.getDeptId().equals(34) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==34){
                copyUsers.add("3003");//余赛赛
            } else if(item.getDeptId().equals(45) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==45){
                copyUsers.add("7468");//杨威
            } else{
                copyUsers.add("2775");// 焦羽虹
            }
        }
        if(types.contains("审计报告")){
            //五特 五环
            if(item.getDeptId().equals(34) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==34){
                copyUsers.add("3003");//余赛赛
            } else if(item.getDeptId().equals(45)){
                copyUsers.add("7468");//杨威
            } else{
                copyUsers.add("2955");// 白皛
            }
        }
        if(types.contains("完税证明")||types.contains("一证通")){
            //五特 五环
            if(item.getDeptId().equals(34) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==34){
                copyUsers.add("3003");//余赛赛
            } else if(item.getDeptId().equals(45) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==45){
                copyUsers.add("7468");//杨威
            } else{
                copyUsers.add("9408");// 付兴
            }
        }
        if(types.contains("其他")){
            //五特 五环
            if(item.getDeptId().equals(34) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==34){
                copyUsers.add("3003");//余赛赛
            } else if(item.getDeptId().equals(45) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==45){
                copyUsers.add("7468");//杨威
            } else{
                copyUsers.add("9521");// 何梦怡
                copyUsers.add("2621");// 潘金楠
            }
        }
        if(types.contains("社保缴纳证明")){
            //五特 五环
            if(item.getDeptId().equals(34) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==34){
                copyUsers.add("3003");//余赛赛
            } else if(item.getDeptId().equals(45) ||selectEmployeeService.getHeadDeptId(item.getDeptId())==45){
                copyUsers.add("7468");//杨威
            } else{
                copyUsers.add("9521");// 何梦怡
            }
        }
        //单位财务人员
        List<String> financeChargeMen = selectEmployeeService.getFinanceChargeMen(item.getDeptId());
        for(String s:financeChargeMen){
            copyUsers.add(s);
        }
        if(item.getType().equalsIgnoreCase("社保缴纳证明")){
            variables.put("flag",true);
        }else {
            variables.put("flag",false);
        }


        variables.put("copyUsers", MyStringUtil.listToString(copyUsers));
        variables.put("processDescription", "对外提供资料审批:"+item.getRemark());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceOutSupplyMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceOutSupplyDto getModelById(int id) throws ParseException {
        return getDto(fiveFinanceOutSupplyMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        FiveFinanceOutSupply item = new FiveFinanceOutSupply();
        HrEmployeeDto hrEmployeeDto= hrEmployeeService.getModelByUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        item.setBeginYear(simpleDateFormat.format(new Date()));
        item.setEndYear(simpleDateFormat.format(new Date()));
        item.setBankBalance(true);
        item.setAssetLiabilities(true);
        item.setProfitsAllocation(true);
        item.setCash(true);
        item.setOther(true);

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);

        ModelUtil.setNotNullFields(item);
        fiveFinanceOutSupplyMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_OUTSUPPLY;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "对外提供资料");
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("deptChargeLeader",selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceOutSupplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public FiveFinanceOutSupplyDto getDto(Object item)  {
        FiveFinanceOutSupply model= (FiveFinanceOutSupply) item;
        FiveFinanceOutSupplyDto dto=FiveFinanceOutSupplyDto.adapt(model);
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
           // MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                    (), "", "");

            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveFinanceOutSupplyMapper.updateByPrimaryKey(dto);
            }
        }
        return dto;
    }

    public List<FiveFinanceOutSupply> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceOutSupplyMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceOutSupplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceOutSupply item=(FiveFinanceOutSupply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
