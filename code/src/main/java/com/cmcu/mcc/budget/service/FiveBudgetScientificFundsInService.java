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
import com.cmcu.mcc.budget.dao.FiveBudgetScientificFundsInDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetScientificFundsInMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetScientificFundsInDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetScientificFundsInDto;
import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsIn;
import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsInDetail;
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
public class FiveBudgetScientificFundsInService {

    @Resource
    FiveBudgetScientificFundsInMapper fiveBudgetScientificFundsInMapper;

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
    FiveBudgetScientificFundsInDetailMapper fiveBudgetScientificFundsInDetailMapper;
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
        FiveBudgetScientificFundsIn item = fiveBudgetScientificFundsInMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetScientificFundsInDto dto) {
        FiveBudgetScientificFundsIn item = fiveBudgetScientificFundsInMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());


        //计算总金额
        List<FiveBudgetScientificFundsInDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetScientificFundsInDetailDto detail:details){
            //排除父节点
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getProjectYearMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //每项占比
        for(FiveBudgetScientificFundsInDetail detail:details){
            //如果有预算年份有变化 子表预算项跟着改变
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getProjectYearMoney(),item.getBudgetTotalMoney()));
            fiveBudgetScientificFundsInDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        Map variables=Maps.newHashMap();
        variables.put("processDescription", item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetScientificFundsInMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetScientificFundsInDto getModelById(int id) {
        return getDto(fiveBudgetScientificFundsInMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetScientificFundsIn item = new FiveBudgetScientificFundsIn();
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

        fiveBudgetScientificFundsInMapper.insert(item);

        String processKey=EdConst.FIVE_BUDGET_SCINETIFICFUNDSIN;
        String title = "科研经费收入预算:"+item.getDeptName();

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
        fiveBudgetScientificFundsInMapper.updateByPrimaryKey(item);

        initDetail(item.getId());

        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        insertDetail(projectBudgetId, 0, "一、内部自筹项目",1);
        insertDetail(projectBudgetId, 0, "二、外部拨款项目",2);
    }

    public int insertDetail(int projectBudgetId,int parentId,String typeName,int seq){
        FiveBudgetScientificFundsInDetail item=new FiveBudgetScientificFundsInDetail();
        item.setScientificFundsInId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setParentId(parentId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);

        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setYearSelfMoney(MyStringUtil.moneyToString("0"));
        item.setYearOtherMoney(MyStringUtil.moneyToString("0"));
        item.setProjectAddInMoney(MyStringUtil.moneyToString("0"));
        item.setProjectAddOutMoney(MyStringUtil.moneyToString("0"));
        item.setProjectYearMoney(MyStringUtil.moneyToString("0"));
        item.setYearCountryMoney(MyStringUtil.moneyToString("0"));
        item.setSelfMoney(MyStringUtil.moneyToString("0"));
        item.setOtherMoney(MyStringUtil.moneyToString("0"));

        ModelUtil.setNotNullFields(item);
        fiveBudgetScientificFundsInDetailMapper.insert(item);
        return item.getId();
    }


    public FiveBudgetScientificFundsInDto getDto(Object item) {
        FiveBudgetScientificFundsIn model= (FiveBudgetScientificFundsIn) item;
        FiveBudgetScientificFundsInDto dto=FiveBudgetScientificFundsInDto.adapt(model);
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
                fiveBudgetScientificFundsInMapper.updateByPrimaryKey(dto);
            }
        }

        return dto;
    }
    public FiveBudgetScientificFundsInDetailDto getDetailDto(Object item) {
        FiveBudgetScientificFundsInDetail model= (FiveBudgetScientificFundsInDetail) item;
        FiveBudgetScientificFundsInDetailDto detailDto=FiveBudgetScientificFundsInDetailDto.adapt(model);
        if(model.getParentId()!=0){
            FiveBudgetScientificFundsInDetail parent = fiveBudgetScientificFundsInDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("最外层显示");
        }
        detailDto.setIsParent(false);
        //判断是否有子节点 有则计算金额
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("scientificFundsInId",detailDto.getScientificFundsInId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetScientificFundsInDetail> details = fiveBudgetScientificFundsInDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);
            String totalProjectYearMoney = MyStringUtil.moneyToString("0");
            String totalYearCountryMoney = MyStringUtil.moneyToString("0");
            String totalYearSelfMoney = MyStringUtil.moneyToString("0");
            String totalYearOtherMoney = MyStringUtil.moneyToString("0");
            String totalAddIn = MyStringUtil.moneyToString("0");
            String totalAddOut = MyStringUtil.moneyToString("0");

            for(FiveBudgetScientificFundsInDetail detail:details){
                totalProjectYearMoney = MyStringUtil.getNewAddMoney(totalProjectYearMoney,detail.getProjectYearMoney());
                totalYearCountryMoney = MyStringUtil.getNewAddMoney(totalYearCountryMoney,detail.getYearCountryMoney());
                totalYearSelfMoney = MyStringUtil.getNewAddMoney(totalYearSelfMoney,detail.getYearSelfMoney());
                totalYearOtherMoney = MyStringUtil.getNewAddMoney(totalYearOtherMoney,detail.getYearOtherMoney());
                totalAddIn = MyStringUtil.getNewAddMoney(totalAddIn,detail.getProjectAddInMoney());
                totalAddOut = MyStringUtil.getNewAddMoney(totalAddOut,detail.getProjectAddOutMoney());
            }

            detailDto.setProjectYearMoney(totalProjectYearMoney);
            detailDto.setYearCountryMoney(totalYearCountryMoney);
            detailDto.setYearSelfMoney(totalYearSelfMoney);
            detailDto.setYearOtherMoney(totalYearOtherMoney);
            detailDto.setProjectAddInMoney(totalAddIn);
            detailDto.setProjectAddOutMoney(totalAddOut);

            updateDetail(detailDto);
        }else{
            //计算剩余金额
            String totalDeductionMoney = MyStringUtil.moneyToString("0");
            //费用申请
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetType","fiveBudgetScientificFundsIn");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails =
                    fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //借款
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetScientificFundsIn");
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
        detailDto.setProjectYearMoney(MyStringUtil.moneyToString(detailDto.getProjectYearMoney(),6));
        detailDto.setYearCountryMoney(MyStringUtil.moneyToString(detailDto.getYearCountryMoney(),6));
        detailDto.setYearSelfMoney(MyStringUtil.moneyToString(detailDto.getYearSelfMoney(),6));
        detailDto.setYearOtherMoney(MyStringUtil.moneyToString(detailDto.getYearOtherMoney(),6));
        detailDto.setProjectAddInMoney(MyStringUtil.moneyToString(detailDto.getProjectAddInMoney(),6));
        detailDto.setProjectAddOutMoney(MyStringUtil.moneyToString(detailDto.getProjectAddOutMoney(),6));

        return detailDto;
    }


    public List<FiveBudgetScientificFundsIn> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetScientificFundsInMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetScientificFundsInMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetScientificFundsIn item=(FiveBudgetScientificFundsIn)p;
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

    public List<FiveBudgetScientificFundsInDetailDto> getDetailById(int id) {
        List<FiveBudgetScientificFundsInDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("scientificFundsInId",id);
        List<FiveBudgetScientificFundsInDetail> details = fiveBudgetScientificFundsInDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetScientificFundsInDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetScientificFundsInDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public List<FiveBudgetScientificFundsInDetailDto> getLastYearDetailById(int id) {
        List<FiveBudgetScientificFundsInDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetScientificFundsInDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetYear",dto.getLastYear());
        List<FiveBudgetScientificFundsIn> fiveBudgetScientificFundsIns = fiveBudgetScientificFundsInMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetScientificFundsIn::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetScientificFundsIns.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("scientificFundsInId",fiveBudgetScientificFundsIns.get(0).getId());
            List<FiveBudgetScientificFundsInDetail> details = fiveBudgetScientificFundsInDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetScientificFundsInDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetScientificFundsInDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }


    public void updateDetail(FiveBudgetScientificFundsInDetail item){
        if(item.getId()!=null){
        FiveBudgetScientificFundsInDetail model = fiveBudgetScientificFundsInDetailMapper.selectByPrimaryKey(item.getId());
        model.setParentId(item.getParentId());
        model.setTypeName(item.getTypeName());

        model.setProjectAddInMoney(MyStringUtil.moneyToString(item.getProjectAddInMoney()));
        model.setProjectAddOutMoney(MyStringUtil.moneyToString(item.getProjectAddOutMoney()));
        model.setBeginTime(item.getBeginTime());
        model.setEndTime(item.getEndTime());
        model.setAllowNo(item.getAllowNo());
        model.setTotalTarget(item.getTotalTarget());

        model.setCountryMoney(MyStringUtil.moneyToString(item.getCountryMoney()));
        model.setSelfMoney(MyStringUtil.moneyToString(item.getSelfMoney()));
        model.setOtherMoney(MyStringUtil.moneyToString(item.getOtherMoney()));

        //计算项目总金额
        List<String> total = new ArrayList();
        total.add(item.getCountryMoney());
        total.add(item.getSelfMoney());
        total.add(item.getOtherMoney());
        model.setProjectTotalMoney(MyStringUtil.getNewTotalListMoney(total));

        model.setYearCountryMoney(MyStringUtil.moneyToString(item.getYearCountryMoney()));
        model.setYearSelfMoney(MyStringUtil.moneyToString(item.getYearSelfMoney()));
        model.setYearOtherMoney(MyStringUtil.moneyToString(item.getYearOtherMoney()));
        //计算项目本年总金额
        List<String> yearTotal = new ArrayList();
        yearTotal.add(item.getYearCountryMoney());
        yearTotal.add(item.getYearSelfMoney());
        yearTotal.add(item.getYearOtherMoney());
        model.setProjectYearMoney(MyStringUtil.getNewTotalListMoney(yearTotal));


        model.setBudgetMoney(MyStringUtil.moneyToString(item.getBudgetMoney()));
        model.setBudgetProportion(item.getBudgetProportion());
        model.setSeq(item.getSeq());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        fiveBudgetScientificFundsInDetailMapper.updateByPrimaryKey(model);
    }else{
        FiveBudgetScientificFundsInDetail model =new FiveBudgetScientificFundsInDetail();
        model.setScientificFundsInId(item.getScientificFundsInId());
        model.setBusinessKey("");
        model.setParentId(item.getParentId());
        model.setTypeName(item.getTypeName());

        model.setProjectAddInMoney(MyStringUtil.moneyToString(item.getProjectAddInMoney()));
        model.setProjectAddOutMoney(MyStringUtil.moneyToString(item.getProjectAddOutMoney()));
        model.setBeginTime(item.getBeginTime());
        model.setEndTime(item.getEndTime());
        model.setAllowNo(item.getAllowNo());
        model.setTotalTarget(item.getTotalTarget());

        model.setCountryMoney(MyStringUtil.moneyToString(item.getCountryMoney()));
        model.setSelfMoney(MyStringUtil.moneyToString(item.getSelfMoney()));
        model.setOtherMoney(MyStringUtil.moneyToString(item.getOtherMoney()));

        //计算项目总金额
        List<String> total = new ArrayList();
        total.add(item.getCountryMoney());
        total.add(item.getSelfMoney());
        total.add(item.getOtherMoney());
        model.setProjectTotalMoney(MyStringUtil.getNewTotalListMoney(total));

        model.setYearCountryMoney(MyStringUtil.moneyToString(item.getYearCountryMoney()));
        model.setYearSelfMoney(MyStringUtil.moneyToString(item.getYearSelfMoney()));
        model.setYearOtherMoney(MyStringUtil.moneyToString(item.getYearOtherMoney()));
        //计算项目本年总金额
        List<String> yearTotal = new ArrayList();
        yearTotal.add(item.getYearCountryMoney());
        yearTotal.add(item.getYearSelfMoney());
        yearTotal.add(item.getYearOtherMoney());
        model.setProjectYearMoney(MyStringUtil.getNewTotalListMoney(yearTotal));

        model.setBudgetMoney(MyStringUtil.moneyToString(item.getBudgetMoney()));
        model.setBudgetProportion(item.getBudgetProportion());
        model.setSeq(item.getSeq());
        model.setDeleted(false);
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        model.setGmtCreate(new Date());
        model.setCreator(item.getCreator());
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
        model.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(model);
        fiveBudgetScientificFundsInDetailMapper.insert(model);
    }

}

    public FiveBudgetScientificFundsInDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetScientificFundsInDetail item=new FiveBudgetScientificFundsInDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setScientificFundsInId(id);
        item.setParentId(detailId);

        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetScientificFundsInDetailMapper.insert(item);
        return getDetailDto(item);
    }


    public FiveBudgetScientificFundsInDetail getModelDetailById(int id){
        FiveBudgetScientificFundsInDetailDto detailDto = getDetailDto(fiveBudgetScientificFundsInDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getBudgetMoney()).equals(0.0)){
            detailDto.setBudgetMoney("");
        }
        if(Double.valueOf(detailDto.getProjectYearMoney()).equals(0.0)){
            detailDto.setProjectYearMoney("");
        }
        if(Double.valueOf(detailDto.getYearSelfMoney()).equals(0.0)){
            detailDto.setYearSelfMoney("");
        }
        if(Double.valueOf(detailDto.getYearOtherMoney()).equals(0.0)){
            detailDto.setYearOtherMoney("");
        }
        if(Double.valueOf(detailDto.getYearCountryMoney()).equals(0.0)){
            detailDto.setYearCountryMoney("");
        }
        if(Double.valueOf(detailDto.getProjectAddInMoney()).equals(0.0)){
            detailDto.setProjectAddInMoney("");
        }
        if(Double.valueOf(detailDto.getProjectAddOutMoney()).equals(0.0)){
            detailDto.setProjectAddOutMoney("");
        }
        if(Double.valueOf(detailDto.getSelfMoney()).equals(0.0)){
            detailDto.setSelfMoney("");
        }
        if(Double.valueOf(detailDto.getOtherMoney()).equals(0.0)){
            detailDto.setOtherMoney("");
        }
        if(Double.valueOf(detailDto.getCountryMoney()).equals(0.0)){
            detailDto.setCountryMoney("");
        }
        return detailDto;
    }

    public void removeDetail(int id){
        FiveBudgetScientificFundsInDetail model = fiveBudgetScientificFundsInDetailMapper.selectByPrimaryKey(id);
        //判断是否有子节点
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("scientificFundsInId",model.getScientificFundsInId());
        map.put("parentId",model.getId());
        List<FiveBudgetScientificFundsInDetail> details = fiveBudgetScientificFundsInDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"根节点不能删除！");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetScientificFundsInDetailMapper.updateByPrimaryKey(model);
    }
}
