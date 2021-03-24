package com.cmcu.mcc.budget.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.NumberChangeUtils;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.budget.dao.FiveBudgetScientificFundsOutDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetScientificFundsOutMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetScientificFundsOutDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetScientificFundsOutDto;
import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsOut;
import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsOutDetail;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.BusinessIncomeMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.service.BusinessIncomeService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceLoanDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceTransferAccountsDetailMapper;
import com.cmcu.mcc.finance.entity.FiveFinanceLoanDetail;
import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccountsDetail;
import com.cmcu.mcc.finance.service.FiveFinanceIncomeConfirmService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveBudgetScientificFundsOutService {

    @Resource
    FiveBudgetScientificFundsOutMapper fiveBudgetScientificFundsOutMapper;

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
    CommonConfigService commonConfigService;
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
    FiveFinanceIncomeConfirmService fiveFinanceIncomeConfirmService;
    @Autowired
    FiveBudgetScientificFundsOutDetailMapper fiveBudgetScientificFundsOutDetailMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Autowired
    ProcessQueryService processQueryService;
    @Resource
    FiveFinanceTransferAccountsDetailMapper fiveFinanceTransferAccountsDetailMapper;
    @Resource
    FiveFinanceLoanDetailMapper fiveFinanceLoanDetailMapper;


    public void remove(int id, String userLogin) {
        FiveBudgetScientificFundsOut item = fiveBudgetScientificFundsOutMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetScientificFundsOutDto dto) {
        FiveBudgetScientificFundsOut item = fiveBudgetScientificFundsOutMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());

        //计算总金额
        List<FiveBudgetScientificFundsOutDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetScientificFundsOutDetailDto detail:details){
            //排除父节点
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //每项占比
        for(FiveBudgetScientificFundsOutDetail detail:details){
            //如果有预算年份有变化 子表预算项跟着改变
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getBudgetTotalMoney()));
            fiveBudgetScientificFundsOutDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        Map variables=Maps.newHashMap();
        variables.put("processDescription", item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetScientificFundsOutMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetScientificFundsOutDto getModelById(int id) {
        return getDto(fiveBudgetScientificFundsOutMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetScientificFundsOut item = new FiveBudgetScientificFundsOut();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";
        item.setBudgetYear(year);

        ModelUtil.setNotNullFields(item);

        fiveBudgetScientificFundsOutMapper.insert(item);
        //独立法人单位
        String processKey=EdConst.FIVE_BUDGET_SCINETIFICFUNDSOUT;
        String title = "科研经费支出预算:"+item.getDeptName();
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", title);
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("otherDeptChargeMen", selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));

        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        item.setTitle(title);
        fiveBudgetScientificFundsOutMapper.updateByPrimaryKey(item);

        initDetail(item.getId());

        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        insertDetail(projectBudgetId, 0, "一、内部自筹项目",1);
        insertDetail(projectBudgetId, 0, "二、外部拨款项目",2);
    }

    public int insertDetail(int projectBudgetId,int parentId,String typeName,int seq){
        FiveBudgetScientificFundsOutDetail item=new FiveBudgetScientificFundsOutDetail();
        item.setScientificFundsOutId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setParentId(parentId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);

        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setDedicatedMoney(MyStringUtil.moneyToString("0"));
        item.setMangerMoney(MyStringUtil.moneyToString("0"));
        item.setPayrollMoney(MyStringUtil.moneyToString("0"));
        item.setAssetMoney(MyStringUtil.moneyToString("0"));
        item.setDedicatedMoney(MyStringUtil.moneyToString("0"));
        item.setTestMoney(MyStringUtil.moneyToString("0"));
        item.setOutAssistMoney(MyStringUtil.moneyToString("0"));
        item.setMaterialsMoney(MyStringUtil.moneyToString("0"));
        item.setDesignMoney(MyStringUtil.moneyToString("0"));

        ModelUtil.setNotNullFields(item);
        fiveBudgetScientificFundsOutDetailMapper.insert(item);
        return item.getId();
    }

    public FiveBudgetScientificFundsOutDto getDto(Object item) {
        FiveBudgetScientificFundsOut model= (FiveBudgetScientificFundsOut) item;
        FiveBudgetScientificFundsOutDto dto=FiveBudgetScientificFundsOutDto.adapt(model);
        if(!Strings.isNullOrEmpty(model.getBudgetYear())){
            dto.setLastYear((Integer.valueOf(model.getBudgetYear())-1)+"");
        }

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
                fiveBudgetScientificFundsOutMapper.updateByPrimaryKey(dto);
            }
        }

        return dto;
    }
    public FiveBudgetScientificFundsOutDetailDto getDetailDto(Object item) {
        FiveBudgetScientificFundsOutDetail model= (FiveBudgetScientificFundsOutDetail) item;
        FiveBudgetScientificFundsOutDetailDto detailDto=FiveBudgetScientificFundsOutDetailDto.adapt(model);
        if(model.getParentId()!=0){
            FiveBudgetScientificFundsOutDetail parent = fiveBudgetScientificFundsOutDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("最外层显示");
        }
        detailDto.setIsParent(false);
        //判断是否有子节点 有则计算金额
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("scientificFundsOutId",detailDto.getScientificFundsOutId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetScientificFundsOutDetail> details = fiveBudgetScientificFundsOutDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);
            String totalMoney =MyStringUtil.moneyToString("0");

            String totalDesignMoney =MyStringUtil.moneyToString("0");
            String totalMaterialsMoney =MyStringUtil.moneyToString("0");
            String totalOutAssistMoney =MyStringUtil.moneyToString("0");
            String totalTestMoney =MyStringUtil.moneyToString("0");
            String totalDedicatedMoney =MyStringUtil.moneyToString("0");
            String totalAssetMoney =MyStringUtil.moneyToString("0");
            String totalPayrollMoney =MyStringUtil.moneyToString("0");
            String totalMangerMoney =MyStringUtil.moneyToString("0");

            for(FiveBudgetScientificFundsOutDetail detail:details){
                totalMoney = MyStringUtil.getNewAddMoney(totalMoney,detail.getBudgetMoney());
                totalMaterialsMoney = MyStringUtil.getNewAddMoney(totalMaterialsMoney,detail.getMaterialsMoney());
                totalDesignMoney = MyStringUtil.getNewAddMoney(totalDesignMoney,detail.getDesignMoney());
                totalOutAssistMoney = MyStringUtil.getNewAddMoney(totalOutAssistMoney,detail.getOutAssistMoney());
                totalTestMoney = MyStringUtil.getNewAddMoney(totalTestMoney,detail.getTestMoney());
                totalDedicatedMoney = MyStringUtil.getNewAddMoney(totalDedicatedMoney,detail.getDedicatedMoney());
                totalAssetMoney = MyStringUtil.getNewAddMoney(totalAssetMoney,detail.getAssetMoney());
                totalPayrollMoney = MyStringUtil.getNewAddMoney(totalPayrollMoney,detail.getPayrollMoney());
                totalMangerMoney = MyStringUtil.getNewAddMoney(totalMangerMoney,detail.getMangerMoney());

            }
            detailDto.setBudgetMoney(totalMoney);
            detailDto.setMangerMoney(totalMangerMoney);
            detailDto.setPayrollMoney(totalPayrollMoney);
            detailDto.setAssetMoney(totalAssetMoney);
            detailDto.setDedicatedMoney(totalDedicatedMoney);
            detailDto.setTestMoney(totalTestMoney);
            detailDto.setOutAssistMoney(totalOutAssistMoney);
            detailDto.setMaterialsMoney(totalMaterialsMoney);
            detailDto.setDesignMoney(totalDesignMoney);
            updateDetail(detailDto);
        }else{
            //计算剩余金额
            String totalDeductionMoney = MyStringUtil.moneyToString("0");
            //费用申请
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetType","fiveBudgetScientificFundsOut");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails =
                    fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //借款
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetScientificFundsOut");
            map2.put("budgetId",detailDto.getId());
            List<FiveFinanceLoanDetail> fiveFinanceLoanDetails = fiveFinanceLoanDetailMapper.selectAll(map2);
            for(FiveFinanceLoanDetail detail:fiveFinanceLoanDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }

            detailDto.setRemainMoney(MyStringUtil.getNewSubMoney(detailDto.getBudgetMoney(),totalDeductionMoney));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));

        //金额显示
        detailDto.setBudgetMoney(MyStringUtil.moneyToString(detailDto.getBudgetMoney(),6));
        detailDto.setRemainMoney(MyStringUtil.moneyToString(detailDto.getRemainMoney(),6));
        detailDto.setMangerMoney(MyStringUtil.moneyToString(detailDto.getMangerMoney(),6));
        detailDto.setPayrollMoney(MyStringUtil.moneyToString(detailDto.getPayrollMoney(),6));
        detailDto.setAssetMoney(MyStringUtil.moneyToString(detailDto.getAssetMoney(),6));
        detailDto.setDedicatedMoney(MyStringUtil.moneyToString(detailDto.getDedicatedMoney(),6));
        detailDto.setTestMoney(MyStringUtil.moneyToString(detailDto.getTestMoney(),6));
        detailDto.setOutAssistMoney(MyStringUtil.moneyToString(detailDto.getOutAssistMoney(),6));
        detailDto.setMaterialsMoney(MyStringUtil.moneyToString(detailDto.getMaterialsMoney(),6));
        detailDto.setDesignMoney(MyStringUtil.moneyToString(detailDto.getDesignMoney(),6));

        return detailDto;
    }

    public List<FiveBudgetScientificFundsOut> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetScientificFundsOutMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetScientificFundsOutMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetScientificFundsOut item=(FiveBudgetScientificFundsOut)p;
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
        return NumberChangeUtils.moneyToChinese(money*10000);
    }

    public List<FiveBudgetScientificFundsOutDetailDto> getDetailById(int id) {
        List<FiveBudgetScientificFundsOutDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("scientificFundsOutId",id);
        List<FiveBudgetScientificFundsOutDetail> details = fiveBudgetScientificFundsOutDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetScientificFundsOutDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetScientificFundsOutDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public List<FiveBudgetScientificFundsOutDetailDto> getLastYearDetailById(int id) {
        List<FiveBudgetScientificFundsOutDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetScientificFundsOutDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetYear",dto.getLastYear());
        List<FiveBudgetScientificFundsOut> fiveBudgetScientificFundsOuts = fiveBudgetScientificFundsOutMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetScientificFundsOut::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetScientificFundsOuts.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("scientificFundsOutId",fiveBudgetScientificFundsOuts.get(0).getId());
            List<FiveBudgetScientificFundsOutDetail> details = fiveBudgetScientificFundsOutDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetScientificFundsOutDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetScientificFundsOutDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }

    public void updateDetail(FiveBudgetScientificFundsOutDetail item){
        if(item.getId()!=null){
            FiveBudgetScientificFundsOutDetail model = fiveBudgetScientificFundsOutDetailMapper.selectByPrimaryKey(item.getId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());

            model.setDesignMoney(MyStringUtil.moneyToString(item.getDesignMoney()));
            model.setMangerMoney(MyStringUtil.moneyToString(item.getMangerMoney()));
            model.setMaterialsMoney(MyStringUtil.moneyToString(item.getMaterialsMoney()));
            model.setOutAssistMoney(MyStringUtil.moneyToString(item.getOutAssistMoney()));
            model.setDedicatedMoney(MyStringUtil.moneyToString(item.getDedicatedMoney()));
            model.setTestMoney(MyStringUtil.moneyToString(item.getTestMoney()));
            model.setAssetMoney(MyStringUtil.moneyToString(item.getAssetMoney()));
            model.setPayrollMoney(MyStringUtil.moneyToString(item.getPayrollMoney()));

            //计算总金额
            List<String> total = new ArrayList();
            total.add(item.getDesignMoney());
            total.add(item.getMangerMoney());
            total.add(item.getMaterialsMoney());
            total.add(item.getOutAssistMoney());
            total.add(item.getDedicatedMoney());
            total.add(item.getTestMoney());
            total.add(item.getAssetMoney());
            total.add(item.getPayrollMoney());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));

            model.setBudgetMoney(MyStringUtil.moneyToString(item.getBudgetMoney()));
            model.setBudgetProportion(item.getBudgetProportion());
            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBudgetScientificFundsOutDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveBudgetScientificFundsOutDetail model =new FiveBudgetScientificFundsOutDetail();
            model.setScientificFundsOutId(item.getScientificFundsOutId());
            model.setBusinessKey("");
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());

            model.setDesignMoney(MyStringUtil.moneyToString(item.getDesignMoney()));
            model.setMangerMoney(MyStringUtil.moneyToString(item.getMangerMoney()));
            model.setMaterialsMoney(MyStringUtil.moneyToString(item.getMaterialsMoney()));
            model.setOutAssistMoney(MyStringUtil.moneyToString(item.getOutAssistMoney()));
            model.setDedicatedMoney(MyStringUtil.moneyToString(item.getDedicatedMoney()));
            model.setTestMoney(MyStringUtil.moneyToString(item.getTestMoney()));
            model.setAssetMoney(MyStringUtil.moneyToString(item.getAssetMoney()));
            model.setPayrollMoney(MyStringUtil.moneyToString(item.getPayrollMoney()));

            //计算总金额
            List<String> total = new ArrayList();
            total.add(item.getDesignMoney());
            total.add(item.getMangerMoney());
            total.add(item.getMaterialsMoney());
            total.add(item.getOutAssistMoney());
            total.add(item.getDedicatedMoney());
            total.add(item.getTestMoney());
            total.add(item.getAssetMoney());
            total.add(item.getPayrollMoney());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));

            model.setSeq(item.getSeq());
            model.setDeleted(false);
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            model.setGmtCreate(new Date());
            model.setCreator(item.getCreator());
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
            model.setCreatorName(hrEmployeeDto.getUserName());
            ModelUtil.setNotNullFields(model);
            fiveBudgetScientificFundsOutDetailMapper.insert(model);
        }

    }

    public FiveBudgetScientificFundsOutDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetScientificFundsOutDetail item=new FiveBudgetScientificFundsOutDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setScientificFundsOutId(id);

        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetScientificFundsOutDetailMapper.insert(item);
        return getDetailDto(item);
    }


    public FiveBudgetScientificFundsOutDetail getModelDetailById(int id){
        FiveBudgetScientificFundsOutDetailDto detailDto = getDetailDto(fiveBudgetScientificFundsOutDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getBudgetMoney()).equals(0.0)){
            detailDto.setBudgetMoney("");
        }
        if(Double.valueOf(detailDto.getMangerMoney()).equals(0.0)){
            detailDto.setMangerMoney("");
        }
        if(Double.valueOf(detailDto.getPayrollMoney()).equals(0.0)){
            detailDto.setPayrollMoney("");
        }
        if(Double.valueOf(detailDto.getAssetMoney()).equals(0.0)){
            detailDto.setAssetMoney("");
        }
        if(Double.valueOf(detailDto.getDedicatedMoney()).equals(0.0)){
            detailDto.setDedicatedMoney("");
        }
        if(Double.valueOf(detailDto.getTestMoney()).equals(0.0)){
            detailDto.setTestMoney("");
        }
        if(Double.valueOf(detailDto.getOutAssistMoney()).equals(0.0)){
            detailDto.setOutAssistMoney("");
        }
        if(Double.valueOf(detailDto.getMaterialsMoney()).equals(0.0)){
            detailDto.setMaterialsMoney("");
        }
        if(Double.valueOf(detailDto.getDesignMoney()).equals(0.0)){
            detailDto.setDesignMoney("");
        }
        return detailDto;
    }

    public void removeDetail(int id){
        FiveBudgetScientificFundsOutDetail model = fiveBudgetScientificFundsOutDetailMapper.selectByPrimaryKey(id);
        //判断是否有子节点
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("scientificFundsOutId",model.getScientificFundsOutId());
        map.put("parentId",model.getId());
        List<FiveBudgetScientificFundsOutDetail> details = fiveBudgetScientificFundsOutDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"根节点不能删除！");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetScientificFundsOutDetailMapper.updateByPrimaryKey(model);
        //如果存在父节点 跟新父节点信息
        if(model.getParentId()!=0){
            FiveBudgetScientificFundsOutDetail parentDetail =fiveBudgetScientificFundsOutDetailMapper.selectByPrimaryKey(model.getParentId());
            parentDetail.setBudgetMoney(MyStringUtil.getNewSubMoney(parentDetail.getBudgetMoney(),model.getBudgetMoney()));
            fiveBudgetScientificFundsOutDetailMapper.updateByPrimaryKey(parentDetail);
        }
    }
}
