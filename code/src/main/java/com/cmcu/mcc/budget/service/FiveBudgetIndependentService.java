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
import com.cmcu.mcc.budget.dao.*;
import com.cmcu.mcc.budget.dto.*;
import com.cmcu.mcc.budget.entity.*;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.BusinessIncomeMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.service.BusinessIncomeService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.*;
import com.cmcu.mcc.finance.dto.FiveFinanceLoanDto;
import com.cmcu.mcc.finance.entity.FiveFinanceLoanDetail;
import com.cmcu.mcc.finance.entity.FiveFinanceReimburseDetail;
import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccountsDetail;
import com.cmcu.mcc.finance.entity.FiveFinanceTravelExpenseDetail;
import com.cmcu.mcc.finance.service.FiveFinanceIncomeConfirmService;
import com.cmcu.mcc.finance.service.FiveFinanceLoanService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
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
public class FiveBudgetIndependentService {

    @Resource
    FiveBudgetIndependentMapper fiveBudgetIndependentMapper;

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
    FiveBudgetIndependentDetailMapper fiveBudgetIndependentDetailMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Autowired
    FiveBudgetPublicFundsMapper fiveBudgetPublicFundsMapper;
    @Autowired
    FiveBudgetPublicFundsService fiveBudgetPublicFundsService;
    @Autowired
    FiveBudgetLaborCostMapper fiveBudgetLaborCostMapper;
    @Autowired
    FiveBudgetLaborCostService fiveBudgetLaborCostService;
    @Autowired
    FiveBudgetFeeService fiveBudgetFeeService;
    @Autowired
    FiveBudgetFeeMapper fiveBudgetFeeMapper;
    @Autowired
    FiveBudgetCapitalOutMapper fiveBudgetCapitalOutMapper;
    @Autowired
    FiveBudgetCapitalOutService fiveBudgetCapitalOutService;
    @Resource
    FiveFinanceTransferAccountsDetailMapper fiveFinanceTransferAccountsDetailMapper;
    @Resource
    FiveFinanceLoanDetailMapper fiveFinanceLoanDetailMapper;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    FiveFinanceReimburseDetailMapper fiveFinanceReimburseDetailMapper;
    @Autowired
    FiveFinanceTravelExpenseDetailMapper fiveFinanceTravelExpenseDetailMapper;
    @Autowired
    FiveFinanceLoanService fiveFinanceLoanService;


    public void remove(int id, String userLogin) {
        FiveBudgetIndependent item = fiveBudgetIndependentMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetIndependentDto dto) {
        FiveBudgetIndependent item = fiveBudgetIndependentMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());

        //计算总金额
        List<FiveBudgetIndependentDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetIndependentDetailDto detail:details){
            //排除父节点
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //获取去年的统计数据
        List<FiveBudgetIndependentDetailDto> lastYearDetails = getLastYearDetailById(dto.getId(), dto.getBudgetYear());
        //每项占比
        for(FiveBudgetIndependentDetail detail:details){
            //如果有预算年份有变化 子表预算项跟着改变
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
                detail.setLastYearMoney(MyStringUtil.moneyToString("0"));
                //匹配去年数据
                for(FiveBudgetIndependentDetailDto detailDto:lastYearDetails){
                    //不是父节点 节点名字相同
                    if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(detail.getTypeName())){
                        detail.setLastYearMoney(detailDto.getBudgetMoney());
                    }
                }
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getBudgetTotalMoney()));
            fiveBudgetIndependentDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", item.getTitle());
        variables.put("test", false);
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetIndependentMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetIndependentDto getModelById(int id) {
        return getDto(fiveBudgetIndependentMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetIndependent item = new FiveBudgetIndependent();
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

        fiveBudgetIndependentMapper.insert(item);
        //独立法人单位
        String processKey=EdConst.FIVE_BUDGET_INDEPENDENT;
        String title ="独立法人单位、生产辅助部门预算:"+item.getDeptName();
        if(uiSref.equalsIgnoreCase("budget.production")){ //生产辅助部门
            processKey=EdConst.FIVE_BUDGET_PRODUCTION;
            title = "生产、辅助部门预算:"+item.getDeptName();
        }else if(uiSref.equalsIgnoreCase("budget.postExpense")){ //职务消费
            processKey=EdConst.FIVE_BUDGET_POSTEXPENSE;
            title = "职务消费预算:"+item.getDeptName();
           // item.setRemark("1.本表中的配备公务用车，是指以购置方式为企业负责人配备公务活动专车的管理方式。 2.公务用车运行费用包括保养、维修费用，以及燃料费、停车和过桥过路费、保险费、年检费、车船使用税等日常使用费用。");
        }else if(uiSref.equalsIgnoreCase("budget.function")){ //职能部门
            processKey=EdConst.FIVE_BUDGET_FUNCTION;
            title = "职能部门预算:"+item.getDeptName();
        }

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("testMen", Arrays.asList("2863","luodong","2623"));
        variables.put("otherDeptChargeMen", selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));

        String businessKey=processKey+"_"+item.getId();
        variables.put("processDescription", item.getTitle());

        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        item.setTitle(title);
        fiveBudgetIndependentMapper.updateByPrimaryKey(item);
        //新增默认子表数据
        if(processKey==EdConst.FIVE_BUDGET_FUNCTION){//职能管理部门
            initDetailFunction(item.getId());
        } else if(processKey==EdConst.FIVE_BUDGET_POSTEXPENSE){//职务消费
            initDetailPostExpense(item.getId());
        }else{
            initDetail(item.getId());
        }
        //获取最新的统计数据
        refreshBudgetDetail(item.getId());
        return item.getId();
    }
    //独立法人 生产辅助部门预算
    private void initDetail(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";

        //获取去年的统计数据
        List<FiveBudgetIndependentDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);

        int parentId = insertDetail(lastYearDetails,projectBudgetId, 0, "一、"+year+"年收费合计",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"1.勘察设计",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"2.工程承包",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"3.其他收费",3);
        int parentId2 = insertDetail(lastYearDetails,projectBudgetId, 0, "二、"+year+"年支出合计",2);

        int parentId3 = insertDetail(lastYearDetails,projectBudgetId,parentId2,"1.业务经费",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"1）差旅费",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"2）邮电通讯费",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"3）出国费",3);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"4）图书资料费",4);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"5）物料消耗",5);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"6）办公费",6);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"7）房屋租赁费",7);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"8）业务招待费",8);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"9）会议费",9);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"10）保险费",10);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"11）招投标费",11);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"12）油料费",12);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"13）修理费",13);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"14）其他支出",14);

        int parentId4 = insertDetail(lastYearDetails,projectBudgetId,parentId2,"2.固定经费",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId4,"1）水电费",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId4,"2）取暖费",2);


        int parentId7 = insertDetail(lastYearDetails, projectBudgetId, parentId2, "3.人员成本", 3);
        insertDetail(lastYearDetails,projectBudgetId,parentId7,"1）咨询劳务",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId7,"2）培训费",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId7,"3）其他费用",3);

        int parentId5 = insertDetail(lastYearDetails,projectBudgetId,parentId2,"4.工程外付成本",4);
        insertDetail(lastYearDetails,projectBudgetId,parentId5,"1）分包工程款、设备材料购置",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId5,"2）分包设计费、外协费",2);


        int parentId6 = insertDetail(lastYearDetails,projectBudgetId,parentId2,"5.办公设备支出",5);
        insertDetail(lastYearDetails,projectBudgetId,parentId6,"1）办公自动化设备购置",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId6,"2）软件购置",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId6,"3）车辆购置",3);
        insertDetail(lastYearDetails,projectBudgetId,parentId6,"4）办公家具",4);

        insertDetail(lastYearDetails,projectBudgetId,parentId2,"6.税金",6);
    }
    //职能管理部门预算
    private void initDetailFunction(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";

        //获取去年的统计数据
        List<FiveBudgetIndependentDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);
        int parentId = insertDetail(lastYearDetails,projectBudgetId, 0, "一、部门可控经费",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"1.差旅费",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"2.邮电通讯费",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"3.出国费",3);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"4.图书资料费",4);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"5.物料消耗",5);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"6.办公费",6);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"7.租赁费",7);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"8.业务招待费",8);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"9.会议费",9);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"10.保险费",10);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"11.油料费",11);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"12.修理费",12);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"13.股权投资",13);

        int parentId7 = insertDetail(lastYearDetails, projectBudgetId, parentId, "14.人员成本", 14);
        insertDetail(lastYearDetails,projectBudgetId,parentId7,"1）咨询劳务",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId7,"2）培训费",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId7,"3）其他费用",3);

        insertDetail(lastYearDetails,projectBudgetId,parentId,"15.其他支出",15);

        int parent2 = insertDetail(lastYearDetails,projectBudgetId, 0, "二、办公设备支出", 2);
        insertDetail(lastYearDetails,projectBudgetId,parent2,"1.办公自动化支出",1);
        insertDetail(lastYearDetails,projectBudgetId,parent2,"2.办公家具",2);
        int parent3 = insertDetail(lastYearDetails,projectBudgetId, 0, "三、专项支出", 3);


    }
    //职务消费预算
    private void initDetailPostExpense(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";

        //获取去年的统计数据
        List<FiveBudgetIndependentDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);

        int parentId = insertDetail(lastYearDetails,projectBudgetId, 0, "一、公务用车费用小计",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId,"（一）配备公务用车费用",1);
        int parentId2 = insertDetail(lastYearDetails,projectBudgetId,parentId,"（二）车辆运行费用",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"（1）维修保养费用",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"（2）其他日常使用费用",2);

        insertDetail(lastYearDetails,projectBudgetId,parentId,"（三）发放交通补贴金额（不以任何方式配备公务用车）",4);


       insertDetail(lastYearDetails,projectBudgetId, 0, "二、通信费用", 2);

        int parentId4 = insertDetail(lastYearDetails,projectBudgetId, 0, "三、业务招待费用小计", 3);
        insertDetail(lastYearDetails,projectBudgetId,parentId4,"（一）餐费",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId4,"（二）住宿费",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId4,"（三）其他 ",3);

        int parentId5 = insertDetail(lastYearDetails,projectBudgetId,0,"四、差旅费用合计",4);
        insertDetail(lastYearDetails,projectBudgetId,parentId5,"（一）交通住宿",1);
        insertDetail(lastYearDetails,projectBudgetId,parentId5,"（二）差旅补助",2);
        insertDetail(lastYearDetails,projectBudgetId,parentId5,"（三）其他 ",3);

        insertDetail(lastYearDetails,projectBudgetId,0,"五、国（境）外考察费用",5);
        insertDetail(lastYearDetails,projectBudgetId,0,"六、培训费用",6);

    }

    public int insertDetail(List<FiveBudgetIndependentDetailDto> lastYearDetails,int projectBudgetId,int parentId,String typeName,int seq){

        FiveBudgetIndependentDetail item=new FiveBudgetIndependentDetail();
        //匹配去年数据
        for(FiveBudgetIndependentDetailDto detailDto:lastYearDetails){
            //不是父节点 节点名字相同
            if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(typeName)){
                item.setLastYearMoney(detailDto.getBudgetMoney());
            }
        }
        item.setBudgetIndependentId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));
        item.setLastYearSuccess(MyStringUtil.moneyToString("0"));
        item.setParentId(parentId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveBudgetIndependentDetailMapper.insert(item);
        return item.getId();
    }


    public FiveBudgetIndependentDto getDto(Object item) {
        FiveBudgetIndependent model= (FiveBudgetIndependent) item;
        FiveBudgetIndependentDto dto=FiveBudgetIndependentDto.adapt(model);
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
            //dto.setBusinessKey(processInstanceDto.getBusinessKey());
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBudgetIndependentMapper.updateByPrimaryKey(dto);
            }
        }
        dto.setBudgetTotalMoney(MyStringUtil.moneyToString(dto.getBudgetTotalMoney(),6));

        return dto;
    }

    public FiveBudgetIndependentDetailDto getDetailDto(Object item) {
        FiveBudgetIndependentDetail model= (FiveBudgetIndependentDetail) item;
        FiveBudgetIndependentDetailDto detailDto=FiveBudgetIndependentDetailDto.adapt(model);
        //父节点名称
        if(model.getParentId()!=0){
            FiveBudgetIndependentDetail parent = fiveBudgetIndependentDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("最外层显示");
        }

        //判断是否有子节点 有则计算金额
        detailDto.setIsParent(false);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetIndependentId",detailDto.getBudgetIndependentId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetIndependentDetail> details = fiveBudgetIndependentDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);
            String totalMoney =MyStringUtil.moneyToString("0");
            String totalLastYearMoney =MyStringUtil.moneyToString("0");
            String totalLastYearSuccess =MyStringUtil.moneyToString("0");
            for(FiveBudgetIndependentDetail detail:details){
                totalMoney = MyStringUtil.getNewAddMoney(totalMoney,detail.getBudgetMoney());
                totalLastYearMoney = MyStringUtil.getNewAddMoney(totalLastYearMoney,detail.getLastYearMoney());
                totalLastYearSuccess = MyStringUtil.getNewAddMoney(totalLastYearSuccess,detail.getLastYearSuccess());
            }
            detailDto.setBudgetMoney(totalMoney);
            detailDto.setLastYearMoney(totalLastYearMoney);
            detailDto.setLastYearSuccess(totalLastYearSuccess);
            updateDetail(detailDto);
        }else{
            //计算剩余金额
            String totalDeductionMoney = MyStringUtil.moneyToString("0");

            //费用报销
            Map map1 = new HashMap();
            map1.put("deleted",false);
            //map1.put("budgetType","fiveBudgetIndependent");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceReimburseDetail> fiveFinanceReimburseDetails = fiveFinanceReimburseDetailMapper.selectAll(map1);
            for(FiveFinanceReimburseDetail detail:fiveFinanceReimburseDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }

            //借款
            Map map2 = new HashMap();
            map2.put("deleted",false);
            //map2.put("budgetType","fiveBudgetIndependent");
            map2.put("budgetId",detailDto.getId());
            List<FiveFinanceLoanDetail> fiveFinanceLoanDetails = fiveFinanceLoanDetailMapper.selectAll(map2);
            for(FiveFinanceLoanDetail detail:fiveFinanceLoanDetails){
                FiveFinanceLoanDto loanDto = fiveFinanceLoanService.getModelById(detail.getLoanId());
                //转换金额 万元
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,
                        MyStringUtil.getMoneyW(loanDto.getRemainMoney()));
            }

            //差旅费
            Map map3 = new HashMap();
            map3.put("deleted",false);
            //map3.put("budgetType","fiveBudgetIndependent");
            map3.put("budgetId",detailDto.getId());
            List<FiveFinanceTravelExpenseDetail> fiveFinanceTravelExpenseDetails = fiveFinanceTravelExpenseDetailMapper.selectAll(map3);
            for(FiveFinanceTravelExpenseDetail detail:fiveFinanceTravelExpenseDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }

            detailDto.setRemainMoney(MyStringUtil.getNewSubMoney(detailDto.getBudgetMoney(),totalDeductionMoney));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));

        //显示金额
        detailDto.setLastYearMoney(MyStringUtil.moneyToString(detailDto.getLastYearMoney(),6));
        detailDto.setLastYearSuccess(MyStringUtil.moneyToString(detailDto.getLastYearSuccess(),6));
        detailDto.setBudgetMoney(MyStringUtil.moneyToString(detailDto.getBudgetMoney(),6));
        detailDto.setRemainMoney(MyStringUtil.moneyToString(detailDto.getRemainMoney(),6));
        return detailDto;
    }


    public List<FiveBudgetIndependent> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetIndependentMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        if(uiSref.equalsIgnoreCase("budget.independent")){
            params.put("qBusinessKey","fiveBudgetIndependent");
            params.put("isLikeSelect","true");
        }else if(uiSref.equalsIgnoreCase("budget.production")){
            params.put("qBusinessKey","fiveBudgetProduction");
        }else if(uiSref.equalsIgnoreCase("budget.function")){
            params.put("qBusinessKey","fiveBudgetFunction");
            params.put("isLikeSelect","true");
        }else if(uiSref.equalsIgnoreCase("budget.postExpense")){
            params.put("qBusinessKey","fiveBudgetPostExpense");
        }else if(uiSref.equalsIgnoreCase("budget.feeChange")){
            params.put("qBusinessKey","fiveBudgetIndependent");
            params.put("qBusinessKey1","fiveBudgetFunction");
            params.put("isLikeSelect","true");
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetIndependentMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetIndependent item=(FiveBudgetIndependent)p;
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

    public List<FiveBudgetIndependentDetailDto> getDetailById(int id) {
        List<FiveBudgetIndependentDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetIndependentId",id);
        List<FiveBudgetIndependentDetail> details = fiveBudgetIndependentDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetIndependentDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetIndependentDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }
    public List<FiveBudgetIndependentDetailDto> getLastYearDetailById(int id,String budgetYear) {
        List<FiveBudgetIndependentDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetIndependentDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);

        if(Strings.isNullOrEmpty(budgetYear)){
            map.put("budgetYear",dto.getLastYear());
        }else{
            map.put("budgetYear",Integer.valueOf(budgetYear)-1);
        }
        map.put("deptId",dto.getDeptId());
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        //排除自己
        List<FiveBudgetIndependent> fiveBudgetIndependents = fiveBudgetIndependentMapper.selectAll(map).stream().filter(p->p.getId()!=id)
                .sorted(Comparator.comparing(FiveBudgetIndependent::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetIndependents.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetIndependentId",fiveBudgetIndependents.get(0).getId());
            List<FiveBudgetIndependentDetail> details = fiveBudgetIndependentDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetIndependentDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetIndependentDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }

    public List<FiveBudgetIndependentDetailDto> changeBudgetYear(int id,String budgetYear) {
        List<FiveBudgetIndependentDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetIndependentDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetYear",budgetYear);
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        List<FiveBudgetIndependent> fiveBudgetIndependents = fiveBudgetIndependentMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetIndependent::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetIndependents.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetIndependentId",fiveBudgetIndependents.get(0).getId());
            List<FiveBudgetIndependentDetail> details = fiveBudgetIndependentDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetIndependentDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetIndependentDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }

    public void updateDetail(FiveBudgetIndependentDetail item){
        if(item.getId()!=null){
            FiveBudgetIndependentDetail model = fiveBudgetIndependentDetailMapper.selectByPrimaryKey(item.getId());
            model.setPublicBudget(item.getPublicBudget());
            model.setPublicDeptIds(item.getPublicDeptIds());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setPurchaseNo(item.getPurchaseNo());
            model.setBudgetMoney(MyStringUtil.moneyToString(item.getBudgetMoney()));
            model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));
            model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));
            model.setBudgetProportion(item.getBudgetProportion());
            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBudgetIndependentDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveBudgetIndependentDetail model =new FiveBudgetIndependentDetail();
            model.setPublicBudget(false);
            model.setBudgetIndependentId(item.getBudgetIndependentId());
            model.setBusinessKey("");
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setPurchaseNo(item.getPurchaseNo());
            model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));
            model.setBudgetMoney(MyStringUtil.moneyToString(item.getBudgetMoney()));
            model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));
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
            fiveBudgetIndependentDetailMapper.insert(model);
        }

    }

    public FiveBudgetIndependentDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetIndependentDetail item=new FiveBudgetIndependentDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setBudgetIndependentId(id);
        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetIndependentDetailMapper.insert(item);
        return getDetailDto(item);
    }

    public FiveBudgetIndependentDetail getModelDetailById(int id){
        FiveBudgetIndependentDetailDto detailDto = getDetailDto(fiveBudgetIndependentDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getBudgetMoney()).equals(0.0)){
            detailDto.setBudgetMoney("");
        }
        if(Double.valueOf(detailDto.getLastYearMoney()).equals(0.0)){
            detailDto.setLastYearMoney("");
        }
        if(Double.valueOf(detailDto.getLastYearSuccess()).equals(0.0)){
            detailDto.setLastYearSuccess("");
        }
        return detailDto;
    }

    public void removeDetail(int id){
        FiveBudgetIndependentDetail model = fiveBudgetIndependentDetailMapper.selectByPrimaryKey(id);
        //判断是否有子节点
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetIndependentId",model.getBudgetIndependentId());
        map.put("parentId",model.getId());
        List<FiveBudgetIndependentDetail> details = fiveBudgetIndependentDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"根节点不能删除！");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetIndependentDetailMapper.updateByPrimaryKey(model);
        //如果存在父节点 跟新父节点信息
        if(model.getParentId()!=0){
            FiveBudgetIndependentDetail parentDetail =fiveBudgetIndependentDetailMapper.selectByPrimaryKey(model.getParentId());
            parentDetail.setBudgetMoney(MyStringUtil.getNewSubMoney(parentDetail.getBudgetMoney(),model.getBudgetMoney()));
            fiveBudgetIndependentDetailMapper.updateByPrimaryKey(parentDetail);
        }
    }

    public void refreshBudgetDetail(int id) {
        FiveBudgetIndependentDto dto = getModelById(id);
        List<FiveBudgetIndependentDetailDto> details = getDetailById(id);
        //统计部门
/*        List<Integer> indepdentDeptIds = selectEmployeeService.getIndependentDeptIds();
        List<Integer> productDeptIds = selectEmployeeService.getProductDeptIds();
        List<Integer> functionDeptIds = selectEmployeeService.getFunctionDeptIds();*/

        for(FiveBudgetIndependentDetailDto detailDto:details){
            //先清空 然后匹配
            detailDto.setBudgetMoney(MyStringUtil.moneyToString("0"));
            detailDto.setForeignKey(0);
            if(detailDto.getTypeName().equals("勘察设计")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetFee");
                List<FiveBudgetFee> fiveBudgetFees = fiveBudgetFeeMapper.selectAll(map);
                if(fiveBudgetFees.size()>0){
                    FiveBudgetFee fee = fiveBudgetFees.get(0);
                    List<FiveBudgetFeeDetailDto> feeDetailDtos = fiveBudgetFeeService.getDetailById(fee.getId());
                    for(FiveBudgetFeeDetailDto feeDetailDto :feeDetailDtos){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(feeDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(feeDetailDto.getDesignFee());
                                detailDto.setForeignKey(feeDetailDto.getId());
                            }
                        }
                    }
                }
            }
            if(detailDto.getTypeName().equals("工程承包")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetFee");
                List<FiveBudgetFee> fiveBudgetFees = fiveBudgetFeeMapper.selectAll(map);
                if(fiveBudgetFees.size()>0){
                    FiveBudgetFee fee = fiveBudgetFees.get(0);
                    List<FiveBudgetFeeDetailDto> feeDetailDtos = fiveBudgetFeeService.getDetailById(fee.getId());
                    for(FiveBudgetFeeDetailDto feeDetailDto :feeDetailDtos){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(feeDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(feeDetailDto.getProjectFee());
                                detailDto.setForeignKey(feeDetailDto.getId());
                            }
                        }
                    }
                }
            }
            if(detailDto.getTypeName().equals("其他收费")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetFee");
                List<FiveBudgetFee> fiveBudgetFees = fiveBudgetFeeMapper.selectAll(map);
                if(fiveBudgetFees.size()>0){
                    FiveBudgetFee fee = fiveBudgetFees.get(0);
                    List<FiveBudgetFeeDetailDto> feeDetailDtos = fiveBudgetFeeService.getDetailById(fee.getId());
                    for(FiveBudgetFeeDetailDto feeDetailDto :feeDetailDtos){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(feeDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(feeDetailDto.getOtherFee());
                                detailDto.setForeignKey(feeDetailDto.getId());
                            }
                        }
                    }
                }

            }
            if(detailDto.getTypeName().contains("租赁费")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetTurnInRent");
                List<FiveBudgetCapitalOut> fiveBudgetCapitalOuts = fiveBudgetCapitalOutMapper.selectAll(map);
                if(fiveBudgetCapitalOuts.size()>0){
                    FiveBudgetCapitalOut fiveBudgetCapitalOut = fiveBudgetCapitalOuts.get(0);
                    List<FiveBudgetCapitalOutDetailDto> capitalOutDetailDtos = fiveBudgetCapitalOutService.getDetailById(fiveBudgetCapitalOut.getId());
                    for(FiveBudgetCapitalOutDetailDto capitalOutDetailDto :capitalOutDetailDtos){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(capitalOutDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(capitalOutDetailDto.getBudgetMoney());
                                detailDto.setForeignKey(capitalOutDetailDto.getId());
                            }
                        } else if(dto.getBusinessKey().startsWith("fiveBudgetFunction")){
                            if(dto.getDeptId().equals(capitalOutDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(capitalOutDetailDto.getBudgetMoney());
                                detailDto.setForeignKey(capitalOutDetailDto.getId());
                            }
                        }
                    }
                }

            }
            if(detailDto.getTypeName().contains("水电")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetPublicFunds");
                List<FiveBudgetPublicFunds> fiveBudgetPublicFunds = fiveBudgetPublicFundsMapper.selectAll(map);
                if(fiveBudgetPublicFunds.size()>0){
                    FiveBudgetPublicFunds funds = fiveBudgetPublicFunds.get(0);
                    List<FiveBudgetPublicFundsDetailDto> fundsDetails = fiveBudgetPublicFundsService.getDetailById(funds.getId());
                    for(FiveBudgetPublicFundsDetailDto fundsDetailDto :fundsDetails){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(fundsDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(MyStringUtil.getNewAddMoney(fundsDetailDto.getWaterMoney(),fundsDetailDto.getElectricMoney()));
                                detailDto.setForeignKey(fundsDetailDto.getId());
                            }
                        }
                    }
                }
            }
            if(detailDto.getTypeName().equals("取暖")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetPublicFunds");
                List<FiveBudgetPublicFunds> fiveBudgetPublicFunds = fiveBudgetPublicFundsMapper.selectAll(map);
                if(fiveBudgetPublicFunds.size()>0){
                    FiveBudgetPublicFunds funds = fiveBudgetPublicFunds.get(0);
                    List<FiveBudgetPublicFundsDetailDto> fundsDetails = fiveBudgetPublicFundsService.getDetailById(funds.getId());
                    for(FiveBudgetPublicFundsDetailDto fundsDetailDto :fundsDetails){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(fundsDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(fundsDetailDto.getHeatingMoneyTotal());
                                detailDto.setForeignKey(fundsDetailDto.getId());
                            }
                        }
                    }
                }

            }
            if(detailDto.getTypeName().equals("人员成本")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetLaborCost");
                List<FiveBudgetLaborCost> fiveBudgetLaborCosts = fiveBudgetLaborCostMapper.selectAll(map);
                if(fiveBudgetLaborCosts.size()>0){
                    FiveBudgetLaborCost laborCost = fiveBudgetLaborCosts.get(0);
                    List<FiveBudgetLaborCostDetailDto> laborCostDetailDtos = fiveBudgetLaborCostService.getDetailById(laborCost.getId());
                    for(FiveBudgetLaborCostDetailDto laborCostDetailDto :laborCostDetailDtos){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(laborCostDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(laborCostDetailDto.getBudgetMoney());
                                detailDto.setForeignKey(laborCostDetailDto.getId());
                            }
                        }
                    }
                }
            }

            if(detailDto.getTypeName().contains("办公自动化")){
                Map map = new HashMap();
                map.put("deleted",false);
                map.put("budgetYear",dto.getBudgetYear());
                map.put("qBusinessKey","fiveBudgetCapitalOut");
                List<FiveBudgetCapitalOut> fiveBudgetCapitalOuts = fiveBudgetCapitalOutMapper.selectAll(map);
                if(fiveBudgetCapitalOuts.size()>0){
                    FiveBudgetCapitalOut capitalOut = fiveBudgetCapitalOuts.get(0);
                    List<FiveBudgetCapitalOutDetailDto> capitalOutDetailDtos = fiveBudgetCapitalOutService.getDetailById(capitalOut.getId());
                    for(FiveBudgetCapitalOutDetailDto capitalOutDetailDto :capitalOutDetailDtos){
                        if(dto.getBusinessKey().startsWith("fiveBudgetIndependent")){
                            if(dto.getDeptId().equals(capitalOutDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(capitalOutDetailDto.getBudgetMoney());
                                detailDto.setForeignKey(capitalOutDetailDto.getId());
                            }
                        } else if(dto.getBusinessKey().startsWith("fiveBudgetFunction")){
                            if(dto.getDeptId().equals(capitalOutDetailDto.getDeptId())){
                                detailDto.setBudgetMoney(capitalOutDetailDto.getBudgetMoney());
                                detailDto.setForeignKey(capitalOutDetailDto.getId());
                            }
                        }
                    }
                }

            }
            fiveBudgetIndependentDetailMapper.updateByPrimaryKey(detailDto);
        }

    }

    public SelectBudgetDto getBudgetIdByDeptId(String budgetType,int deptId,String budgetYear) {
        SelectBudgetDto selectBudgetDto = new SelectBudgetDto();
        HrDeptDto hrDeptDto = hrDeptService.getModelById(deptId);
        selectBudgetDto.setBudgetDeptName(hrDeptDto.getName());
        selectBudgetDto.setBudgetYear(budgetYear);
        //职能 独立法人
        if(budgetType.equals(EdConst.FIVE_BUDGET_INDEPENDENT)||budgetType.equals(EdConst.FIVE_BUDGET_FUNCTION)){
            //判断类型
            if(selectEmployeeService.getFunctionDeptIds().contains(deptId)){
                selectBudgetDto.setBudgetType(EdConst.FIVE_BUDGET_FUNCTION);
            }else{
                selectBudgetDto.setBudgetType(EdConst.FIVE_BUDGET_INDEPENDENT);
            }
            Map inMap = new HashMap();
            inMap.put("deleted",false);
            inMap.put("deptId",deptId);
            inMap.put("budgetYear",budgetYear);
            List<FiveBudgetIndependent> budgetIndependents = fiveBudgetIndependentMapper.selectAll(inMap).stream()
                    .filter(p->p.getBusinessKey().startsWith(selectBudgetDto.getBudgetType()))
                    .sorted(Comparator.comparing(FiveBudgetIndependent::getGmtCreate).reversed()).collect(Collectors.toList());
            if(budgetIndependents.size()>0){
                selectBudgetDto.setBudgetId(budgetIndependents.get(0).getId());
            }else{
                selectBudgetDto.setBudgetId(0);
            }
        }

        return selectBudgetDto;
    }
    public List<FiveBudgetIndependentDetailDto> getPublicBudgetById(int deptId,String budgetYear) {
        List<FiveBudgetIndependentDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        List<FiveBudgetIndependentDetail> details = fiveBudgetIndependentDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetIndependentDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetIndependentDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public FiveBudgetIndependentDetailDto getDetailByDetailId(int id) {
        FiveBudgetIndependentDetailDto detailDto = getDetailDto(fiveBudgetIndependentDetailMapper.selectByPrimaryKey(id));
        return detailDto;
    }
    //根据预算类型名字 选取部门当年的预算
    public FiveBudgetIndependentDetailDto getDetailByTypeName(int deptId,String budgetType,String budgetYear,String typeName) {
        SelectBudgetDto selectBudgetDto = getBudgetIdByDeptId(budgetType, deptId, budgetYear);
        List<FiveBudgetIndependentDetailDto> details = getDetailById(selectBudgetDto.getBudgetId());
        for(FiveBudgetIndependentDetailDto detailDto:details){
            if(detailDto.getTypeName().contains(typeName)){
                return detailDto;
            }
        }
        return null;
    }

}
