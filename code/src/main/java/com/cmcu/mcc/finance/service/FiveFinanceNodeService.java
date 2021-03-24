package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.NumberChangeUtils;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceNodeMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceNodeDto;
import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceNode;
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
import java.util.stream.Collectors;

@Service
public class FiveFinanceNodeService {

    @Resource
    FiveFinanceNodeMapper fiveFinanceNodeMapper;

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
        FiveFinanceNode item = fiveFinanceNodeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }
    public void removeInIncome(int id, String userLogin) {
        FiveFinanceNode item = fiveFinanceNodeMapper.selectByPrimaryKey(id);
        //Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if(item.getIncomeId()!=0){
            //还原收入金额
            FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(item.getIncomeId());
            fiveFinanceIncome.setMoney(MyStringUtil.getNewSubMoney(fiveFinanceIncome.getMoney(),item.getMoney(),4));
            fiveFinanceIncome.setMoneyMax(moneyTurnCapital(Double.valueOf(fiveFinanceIncome.getMoney())));
            fiveFinanceIncomeMapper.updateByPrimaryKey(fiveFinanceIncome);
            //还原选择状态
            item.setIncomeId(0);
        }
        fiveFinanceNodeMapper.updateByPrimaryKey(item);
        //handleFormService.deleteFormData(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceNodeDto dto) {
        FiveFinanceNode item = fiveFinanceNodeMapper.selectByPrimaryKey(dto.getId());
        item.setType(dto.getType());
        item.setSourceAccount(dto.getSourceAccount());
        item.setTargetAccount(dto.getTargetAccount());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setModality(dto.getModality());
        item.setState(dto.getState());
        item.setUseTime(dto.getUseTime());
        item.setNodeNo(dto.getNodeNo());
        item.setUseNum(dto.getUseNum());
        item.setFinanceVerifyTime(dto.getFinanceVerifyTime());
        item.setRemainTime(dto.getRemainTime());
        item.setNodeStartTime(dto.getNodeStartTime());
        item.setNodeEndTime(dto.getNodeEndTime());
        item.setBank(dto.getBank());
        if(item.getIncomeId()!=0){
            //跟新收入金额 先减掉原来的 在加上新的
            FiveFinanceIncome income = fiveFinanceIncomeMapper.selectByPrimaryKey(item.getIncomeId());
            income.setMoney(MyStringUtil.getNewSubMoney(income.getMoney(),item.getMoney(),4));
            income.setMoney(MyStringUtil.getNewAddMoney(income.getMoney(),dto.getMoney(),4));
            income.setMoneyMax(moneyTurnCapital(Double.valueOf(income.getMoney())));
            fiveFinanceIncomeMapper.updateByPrimaryKey(income);
        }
        item.setMoney(MyStringUtil.moneyToString(dto.getMoney(),4));
        item.setMoneyMax(dto.getMoneyMax());
        item.setDrawDept(dto.getDrawDept());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "票据管理:"+item.getSourceAccount());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceNodeMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceNodeDto getModelById(int id) throws ParseException {
        return getDto(fiveFinanceNodeMapper.selectByPrimaryKey(id));
    }

    public int getNewModelByIncome(int incomeId,String userLogin,String uiSref) {
        FiveFinanceNode item = new FiveFinanceNode();
        HrEmployeeDto hrEmployeeDto= hrEmployeeService.getModelByUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setIncomeId(incomeId);
        //同步收入信息
        FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(incomeId);
        item.setSourceAccount(fiveFinanceIncome.getSourceAccount());
        item.setTargetAccount(fiveFinanceIncome.getTargetAccount());

        item.setModality(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"票据形式").toString());
        item.setType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"票据种类").toString());
        item.setState(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"票据状态").toString());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveFinanceNodeMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_NODE;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "票据管理");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceNodeMapper.updateByPrimaryKey(item);
        return item.getId();
    }
    public int getNewModel(String userLogin,String uiSref) {
        FiveFinanceNode item = new FiveFinanceNode();
        HrEmployeeDto hrEmployeeDto= hrEmployeeService.getModelByUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setModality(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"票据形式").toString());
        item.setType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"票据种类").toString());
        item.setState(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"票据状态").toString());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        item.setUseTime(simpleDateFormat.format(new Date()));
        item.setNodeStartTime(simpleDateFormat.format(new Date()));
        item.setNodeEndTime(simpleDateFormat.format(new Date()));
        item.setFinanceVerifyTime(simpleDateFormat.format(new Date()));
        ModelUtil.setNotNullFields(item);
        fiveFinanceNodeMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_NODE;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "票据管理");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceNodeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public FiveFinanceNodeDto getDto(Object item)  {
        FiveFinanceNode model= (FiveFinanceNode) item;
        FiveFinanceNodeDto dto=FiveFinanceNodeDto.adapt(model);
        //跟新剩余到期时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(StringUtils.isNotEmpty(dto.getNodeEndTime())){
            try{
                Date endTime =df.parse(dto.getNodeEndTime());
                Date now =df.parse(df.format(new Date()));
                dto.setRemainTime(( endTime.getTime() -now.getTime()) / (24 * 60 * 60 * 1000)+"");
            }catch (Exception e){
                Assert.state(false,"日期格式转化出错");
            }


            if(Integer.valueOf(dto.getRemainTime())<0){
                dto.setRemainTime("已到期");
                dto.setState("已失效");
                update(dto);
            }
            update(dto);
        }
        dto.setProcessName("已完成");
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            //MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveFinanceNodeMapper.updateByPrimaryKey(dto);
            }
        }
        return dto;
    }

    public List<FiveFinanceNode> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceNodeMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceNodeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceNode item=(FiveFinanceNode)p;
            list.add(getDto(item));

        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 收入金额转中文大写
     * @param money
     * @return
     */
    public String moneyTurnCapital(Double money){
        return NumberChangeUtils.moneyToChinese(money);
    }

    public List<FiveFinanceNodeDto> getNodesByIncomeId(int incomeId)  {
        List<FiveFinanceNodeDto> list =new ArrayList<>();
        Map map= new HashMap();
        map.put("deleted",false);
        map.put("incomeId",incomeId);
        List<FiveFinanceNode> fiveFinanceNodes = fiveFinanceNodeMapper.selectAll(map);
        for(FiveFinanceNode node:fiveFinanceNodes){
            list.add(getDto(node));
        }
        return list;
    }

    public List<FiveFinanceNodeDto> getNodesInIncome(int incomeId, String userLogin, String uiSref) throws ParseException {
        List<FiveFinanceNodeDto> list =new ArrayList<>();
        Map map= new HashMap();
        map.put("deleted",false);
        //未被选择的发票
        List<FiveFinanceNode> fiveFinanceNodes = fiveFinanceNodeMapper.selectAll(map).stream().filter(p->p.getIncomeId()==0).collect(Collectors.toList());
        for(FiveFinanceNode node:fiveFinanceNodes){
            list.add(getDto(node));
        }
        return list;
    }
}
