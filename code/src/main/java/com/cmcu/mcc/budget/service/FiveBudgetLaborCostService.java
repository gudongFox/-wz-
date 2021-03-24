package com.cmcu.mcc.budget.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
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
import com.cmcu.mcc.budget.dao.FiveBudgetLaborCostDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetLaborCostMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetLaborCostDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetLaborCostDto;
import com.cmcu.mcc.budget.entity.FiveBudgetLaborCost;
import com.cmcu.mcc.budget.entity.FiveBudgetLaborCostDetail;
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
public class FiveBudgetLaborCostService {

    @Resource
    FiveBudgetLaborCostMapper fiveBudgetLaborCostMapper;

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
    FiveFinanceIncomeConfirmService fiveFinanceIncomeConfirmService;
    @Autowired
    FiveBudgetLaborCostDetailMapper fiveBudgetLaborCostDetailMapper;
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
        FiveBudgetLaborCost item = fiveBudgetLaborCostMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetLaborCostDto dto) {
        FiveBudgetLaborCost item = fiveBudgetLaborCostMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());

        //计算总金额
        List<FiveBudgetLaborCostDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetLaborCostDetailDto detail:details){
            //排除父节点
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //获取去年的统计数据
        List<FiveBudgetLaborCostDetailDto> lastYearDetails = getLastYearDetailById(dto.getId(), dto.getBudgetYear());
        //每项占比
        for(FiveBudgetLaborCostDetail detail:details){
            //如果有预算年份有变化 子表预算项跟着改变
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
                detail.setLastYearStaff("0");
                //匹配去年数据
                for(FiveBudgetLaborCostDetailDto detailDto:lastYearDetails){
                    //不是父节点 节点名字相同
                    if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(detail.getTypeName())){
                        detail.setLastYearStaff(detailDto.getStaffNumber());
                        detail.setLastYearEmployee(detailDto.getEmployeeNumber());
                    }
                }
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getBudgetTotalMoney()));
            fiveBudgetLaborCostDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetLaborCostMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetLaborCostDto getModelById(int id) {
        return getDto(fiveBudgetLaborCostMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetLaborCost item = new FiveBudgetLaborCost();
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

        fiveBudgetLaborCostMapper.insert(item);

        String processKey=EdConst.FIVE_BUDGET_LABORCOST;
        String title = "人工成本预算:"+item.getDeptName();

        if(uiSref.equalsIgnoreCase("budget.staffNumber")){ //职工人数
            processKey=EdConst.FIVE_BUDGET_STAFFNUMBER;
            title = "职工人数预算:"+item.getDeptName();
        }
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
        fiveBudgetLaborCostMapper.updateByPrimaryKey(item);

        initDetail(item.getId());
        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";

        //获取去年的统计数据
        List<FiveBudgetLaborCostDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);
        
        int parentId = insertDetail(lastYearDetails,projectBudgetId, 0, "总计",0, 1);
        int parentId1 = insertDetail(lastYearDetails,projectBudgetId, parentId, "1.小计1",0, 1);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"1)第一设计研究院",75,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"2)第二设计研究院",74,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"3)建筑规划设计研究院",47,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"4)环境与能源设计研究院",76,4);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"5)钢结构工程技术中心",20,5);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"6)五源现代项目管理中心",36,6);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"7)石油化工设计研究院",53,7);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"8)轨道交通设计研究院",63,8);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"9)火炸药工程技术研究中心",7,9);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"9)火炸药工程技术研究中心",7,9);

        insertDetail(lastYearDetails,projectBudgetId,parentId1,"10)计算机应用研究所",50,10);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"11)成品室",13,11);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"12)公司办公室(董事会办公室）",59,12);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"13)保密办公室",100,13);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"14)党群工作部",72,14);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"15)经营发展部",48,15);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"16)信息化建设与管理部",11,16);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"17)财务金融部",18,17);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"18)人力资源部",38,18);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"19)工程管理部",29,19);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"20)纪检工作部、审计与风险管理部",9,20);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"21)科研与技术质量部",101,21);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"22)行政事务部",67,22);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"23)档案图书室",58,23);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"24)物业开发",49,24);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"25)其他",0,24);

        int parentId2 = insertDetail(lastYearDetails,projectBudgetId, parentId, "2.小计2",0,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"1)中兵勘察设计研究院有限公司",125,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"2)五环监理",45,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"3)五特公司",34,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"4)五洲中兴",12,4);
    }


    public int insertDetail(List<FiveBudgetLaborCostDetailDto> lastYearDetails,int projectBudgetId,int parentId,String typeName,int deptId,int seq){
        FiveBudgetLaborCostDetail item=new FiveBudgetLaborCostDetail();
        //匹配去年数据
        for(FiveBudgetLaborCostDetailDto detailDto:lastYearDetails){
            //不是父节点 节点名字相同
            if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(typeName)){
                item.setLastYearStaff(detailDto.getStaffNumber());
                item.setLastYearEmployee(detailDto.getEmployeeNumber());
            }
        }
        item.setLaborCostId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setSalaryMoney(MyStringUtil.moneyToString("0"));
        item.setBonusMoney(MyStringUtil.moneyToString("0"));
        item.setCoolingMoney(MyStringUtil.moneyToString("0"));
        item.setCarMoney(MyStringUtil.moneyToString("0"));
        item.setEatingMoney(MyStringUtil.moneyToString("0"));
        item.setHealthMoney(MyStringUtil.moneyToString("0"));
        item.setHouseMoney(MyStringUtil.moneyToString("0"));
        item.setHeatingMoney(MyStringUtil.moneyToString("0"));
        item.setSocietyMoney(MyStringUtil.moneyToString("0"));
        item.setFundsMoney(MyStringUtil.moneyToString("0"));
        item.setLaborUnionMoney(MyStringUtil.moneyToString("0"));
        item.setEducationMoney(MyStringUtil.moneyToString("0"));
        item.setLabourServiceMoney(MyStringUtil.moneyToString("0"));
        item.setOtherMoney(MyStringUtil.moneyToString("0"));
        item.setStaffNumber(MyStringUtil.moneyToString("0"));
        item.setEmployeeNumber(MyStringUtil.moneyToString("0"));
        item.setStaffNumber(MyStringUtil.moneyToString("0"));
        item.setLastYearStaff(MyStringUtil.moneyToString("0"));
        item.setLastYearEmployee(MyStringUtil.moneyToString("0"));



        item.setParentId(parentId);
        item.setDeptId(deptId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveBudgetLaborCostDetailMapper.insert(item);
        return item.getId();
    }


    public FiveBudgetLaborCostDto getDto(Object item) {
        FiveBudgetLaborCost model= (FiveBudgetLaborCost) item;
        FiveBudgetLaborCostDto dto=FiveBudgetLaborCostDto.adapt(model);
        dto.setProcessName("已完成");
        if(!Strings.isNullOrEmpty(model.getBudgetYear())){
            dto.setLastYear((Integer.valueOf(model.getBudgetYear())-1)+"");
        }
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
                fiveBudgetLaborCostMapper.updateByPrimaryKey(dto);
            }
        }
        if(dto.getBusinessKey().startsWith("fiveBudgetStaffNumber")) { //职工人数

            if(Strings.isNullOrEmpty(dto.getBudgetTotalMoney())){
                dto.setBudgetTotalMoney(0+"");
            }else{
                dto.setBudgetTotalMoney(Double.valueOf(dto.getBudgetTotalMoney()).intValue()+"");
            }
        }

        return dto;
    }
    public FiveBudgetLaborCostDetailDto getDetailDto(Object item) {
        FiveBudgetLaborCostDetail model= (FiveBudgetLaborCostDetail) item;
        FiveBudgetLaborCostDetailDto detailDto=FiveBudgetLaborCostDetailDto.adapt(model);
        FiveBudgetLaborCostDto laborCost = getModelById(detailDto.getLaborCostId());
        if(model.getParentId()!=0){
            FiveBudgetLaborCostDetail parent = fiveBudgetLaborCostDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("最外层显示");
        }
        detailDto.setIsParent(false);
        //判断是否有子节点 有则计算金额
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("LaborCostId",detailDto.getLaborCostId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetLaborCostDetail> details = fiveBudgetLaborCostDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);
            String totalBudgetMoney = MyStringUtil.moneyToString("0");
            String totalSalaryMoney = MyStringUtil.moneyToString("0");
            String totalBonusMoney = MyStringUtil.moneyToString("0");
            String totalCoolingMoney = MyStringUtil.moneyToString("0");
            String totalCarMoney = MyStringUtil.moneyToString("0");
            String totalEatingMoney = MyStringUtil.moneyToString("0");
            String totalHealthMoney = MyStringUtil.moneyToString("0");
            String totalHouseMoney = MyStringUtil.moneyToString("0");
            String totalHeatingMoney = MyStringUtil.moneyToString("0");
            String totalSocietyMoney = MyStringUtil.moneyToString("0");
            String totalFundsMoney = MyStringUtil.moneyToString("0");
            String totalLaborUnionMoney = MyStringUtil.moneyToString("0");
            String totalEducationMoney = MyStringUtil.moneyToString("0");
            String totalLabourServiceMoney = MyStringUtil.moneyToString("0");
            String totalOtherMoney = MyStringUtil.moneyToString("0");
            String totalStaffNumber = MyStringUtil.moneyToString("0");
            String totalEmployeeNumber = MyStringUtil.moneyToString("0");
            String totalLastYearEmployee = MyStringUtil.moneyToString("0");
            String totalLastYearStaff = MyStringUtil.moneyToString("0");

            for(FiveBudgetLaborCostDetail detail:details){
                totalBudgetMoney = MyStringUtil.getNewAddMoney(totalBudgetMoney,detail.getBudgetMoney());
                totalSalaryMoney = MyStringUtil.getNewAddMoney(totalSalaryMoney,detail.getSalaryMoney());
                totalBonusMoney = MyStringUtil.getNewAddMoney(totalBonusMoney,detail.getBonusMoney());
                totalCoolingMoney = MyStringUtil.getNewAddMoney(totalCoolingMoney,detail.getCoolingMoney());
                totalCarMoney = MyStringUtil.getNewAddMoney(totalCarMoney,detail.getCarMoney());
                totalEatingMoney = MyStringUtil.getNewAddMoney(totalEatingMoney,detail.getEatingMoney());
                totalHealthMoney = MyStringUtil.getNewAddMoney(totalHealthMoney,detail.getHealthMoney());
                totalHouseMoney = MyStringUtil.getNewAddMoney(totalHouseMoney,detail.getHouseMoney());
                totalHeatingMoney = MyStringUtil.getNewAddMoney(totalHeatingMoney,detail.getHeatingMoney());
                totalSocietyMoney = MyStringUtil.getNewAddMoney(totalSocietyMoney,detail.getSocietyMoney());
                totalFundsMoney = MyStringUtil.getNewAddMoney(totalFundsMoney,detail.getFundsMoney());
                totalLaborUnionMoney = MyStringUtil.getNewAddMoney(totalLaborUnionMoney,detail.getLaborUnionMoney());
                totalEducationMoney = MyStringUtil.getNewAddMoney(totalEducationMoney,detail.getEducationMoney());
                totalLabourServiceMoney = MyStringUtil.getNewAddMoney(totalLabourServiceMoney,detail.getLabourServiceMoney());
                totalOtherMoney = MyStringUtil.getNewAddMoney(totalOtherMoney,detail.getOtherMoney());
                totalStaffNumber = MyStringUtil.getNewAddMoney(totalStaffNumber,detail.getStaffNumber());
                totalEmployeeNumber = MyStringUtil.getNewAddMoney(totalEmployeeNumber,detail.getEmployeeNumber());
                totalLastYearEmployee = MyStringUtil.getNewAddMoney(totalLastYearEmployee,detail.getLastYearEmployee());
                totalLastYearStaff = MyStringUtil.getNewAddMoney(totalLastYearStaff,detail.getLastYearStaff());

            }

            detailDto.setBudgetMoney(totalBudgetMoney);
            detailDto.setSalaryMoney(totalSalaryMoney);
            detailDto.setBonusMoney(totalBonusMoney);
            detailDto.setCarMoney(totalCarMoney);
            detailDto.setCoolingMoney(totalCoolingMoney);
            detailDto.setEatingMoney(totalEatingMoney);
            detailDto.setHealthMoney(totalHealthMoney);
            detailDto.setHouseMoney(totalHouseMoney);
            detailDto.setHeatingMoney(totalHeatingMoney);
            detailDto.setSocietyMoney(totalSocietyMoney);
            detailDto.setLaborUnionMoney(totalLaborUnionMoney);
            detailDto.setEducationMoney(totalEducationMoney);
            detailDto.setLabourServiceMoney(totalLabourServiceMoney);
            detailDto.setFundsMoney(totalFundsMoney);
            detailDto.setOtherMoney(totalOtherMoney);
            detailDto.setStaffNumber(totalStaffNumber);
            detailDto.setEmployeeNumber(totalEmployeeNumber);
            detailDto.setLastYearEmployee(totalLastYearEmployee);
            detailDto.setLastYearStaff(totalLastYearStaff);


            fiveBudgetLaborCostDetailMapper.updateByPrimaryKey(detailDto);
        }else{
            //计算剩余金额
            String totalDeductionMoney = MyStringUtil.moneyToString("0");
            //费用申请
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetType","fiveBudgetLaborCost");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails =
                    fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //借款
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetLaborCost");
            map2.put("budgetId",detailDto.getId());
            List<FiveFinanceLoanDetail> fiveFinanceLoanDetails = fiveFinanceLoanDetailMapper.selectAll(map2);
            for(FiveFinanceLoanDetail detail:fiveFinanceLoanDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }

            detailDto.setRemainMoney(MyStringUtil.getNewSubMoney(detailDto.getBudgetMoney(),totalDeductionMoney));
        }


        if(laborCost.getBusinessKey().startsWith("fiveBudgetStaffNumber")) { //职工人数
            if(Strings.isNullOrEmpty(detailDto.getStaffNumber())){
                detailDto.setStaffNumber(0+"");
            }else{
                detailDto.setStaffNumber(Double.valueOf(detailDto.getStaffNumber()).intValue()+"");
            }
            if(Strings.isNullOrEmpty(detailDto.getEmployeeNumber())){
                detailDto.setEmployeeNumber(0+"");
            }else{
                detailDto.setEmployeeNumber(Double.valueOf(detailDto.getEmployeeNumber()).intValue()+"");
            }
            if(Strings.isNullOrEmpty(detailDto.getBudgetMoney())){
                detailDto.setStaffNumber(0+"");
            }else{
                detailDto.setBudgetMoney(Double.valueOf(detailDto.getBudgetMoney()).intValue()+"");
            }
            if(Strings.isNullOrEmpty(detailDto.getLastYearEmployee())){
                detailDto.setLastYearEmployee(0+"");
            }else{
                detailDto.setLastYearEmployee(Double.valueOf(detailDto.getLastYearEmployee()).intValue()+"");
            }
            if(Strings.isNullOrEmpty(detailDto.getLastYearStaff())){
                detailDto.setLastYearStaff(0+"");
            }else{
                detailDto.setLastYearStaff(Double.valueOf(detailDto.getLastYearStaff()).intValue()+"");
            }
        }else{
            //金额显示
            detailDto.setBudgetMoney(MyStringUtil.moneyToString(detailDto.getBudgetMoney(),6));
            detailDto.setRemainMoney(MyStringUtil.moneyToString(detailDto.getRemainMoney(),6));

            detailDto.setSalaryMoney(MyStringUtil.moneyToString(detailDto.getSalaryMoney(),6));
            detailDto.setBonusMoney(MyStringUtil.moneyToString(detailDto.getBonusMoney(),6));
            detailDto.setCarMoney(MyStringUtil.moneyToString(detailDto.getCarMoney(),6));
            detailDto.setCoolingMoney(MyStringUtil.moneyToString(detailDto.getCoolingMoney(),6));
            detailDto.setEatingMoney(MyStringUtil.moneyToString(detailDto.getEatingMoney(),6));
            detailDto.setHealthMoney(MyStringUtil.moneyToString(detailDto.getHealthMoney(),6));
            detailDto.setHouseMoney(MyStringUtil.moneyToString(detailDto.getHouseMoney(),6));
            detailDto.setHeatingMoney(MyStringUtil.moneyToString(detailDto.getHeatingMoney(),6));
            detailDto.setSocietyMoney(MyStringUtil.moneyToString(detailDto.getSocietyMoney(),6));
            detailDto.setLaborUnionMoney(MyStringUtil.moneyToString(detailDto.getLaborUnionMoney(),6));
            detailDto.setEducationMoney(MyStringUtil.moneyToString(detailDto.getEducationMoney(),6));
            detailDto.setLabourServiceMoney(MyStringUtil.moneyToString(detailDto.getLabourServiceMoney(),6));
            detailDto.setFundsMoney(MyStringUtil.moneyToString(detailDto.getFundsMoney(),6));
            detailDto.setOtherMoney(MyStringUtil.moneyToString(detailDto.getOtherMoney(),6));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));

        return detailDto;
    }


    public List<FiveBudgetLaborCost> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetLaborCostMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        if(uiSref.equalsIgnoreCase("budget.laborCost")){ //人工成本预算
            params.put("qBusinessKey","fiveBudgetLaborCost");
        } else if(uiSref.equalsIgnoreCase("budget.staffNumber")){ //职工人数预算
            params.put("qBusinessKey","fiveBudgetStaffNumber");
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetLaborCostMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetLaborCost item=(FiveBudgetLaborCost)p;
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

    public List<FiveBudgetLaborCostDetailDto> getDetailById(int id) {
        List<FiveBudgetLaborCostDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("laborCostId",id);
        List<FiveBudgetLaborCostDetail> details = fiveBudgetLaborCostDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetLaborCostDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetLaborCostDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public void updateDetail(FiveBudgetLaborCostDetail item){
        if(item.getId()!=null){
            FiveBudgetLaborCostDetail model = fiveBudgetLaborCostDetailMapper.selectByPrimaryKey(item.getId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setSalaryMoney(MyStringUtil.moneyToString(item.getSalaryMoney()));
            model.setBonusMoney(MyStringUtil.moneyToString(item.getBonusMoney()));
            model.setCoolingMoney(MyStringUtil.moneyToString(item.getCoolingMoney()));
            model.setCarMoney(MyStringUtil.moneyToString(item.getCarMoney()));
            model.setEatingMoney(MyStringUtil.moneyToString(item.getEatingMoney()));
            model.setHealthMoney(MyStringUtil.moneyToString(item.getHealthMoney()));
            model.setHouseMoney(MyStringUtil.moneyToString(item.getHouseMoney()));
            model.setHeatingMoney(MyStringUtil.moneyToString(item.getHeatingMoney()));
            model.setSocietyMoney(MyStringUtil.moneyToString(item.getSocietyMoney()));
            model.setFundsMoney(MyStringUtil.moneyToString(item.getFundsMoney()));
            model.setLaborUnionMoney(MyStringUtil.moneyToString(item.getLaborUnionMoney()));
            model.setEducationMoney(MyStringUtil.moneyToString(item.getEducationMoney()));
            model.setLabourServiceMoney(MyStringUtil.moneyToString(item.getLabourServiceMoney()));
            model.setOtherMoney(MyStringUtil.moneyToString(item.getOtherMoney()));
            model.setStaffNumber(MyStringUtil.moneyToString(item.getStaffNumber()));
            model.setEmployeeNumber(MyStringUtil.moneyToString(item.getEmployeeNumber()));
            model.setLastYearEmployee(MyStringUtil.moneyToString(item.getLastYearEmployee()));
            model.setLastYearStaff(MyStringUtil.moneyToString(item.getLastYearStaff()));


            //计算部门总预算
            List<String> total = new ArrayList();
            total.add(item.getSalaryMoney());
            total.add(item.getBonusMoney());
            total.add(item.getCoolingMoney());
            total.add(item.getCarMoney());
            total.add(item.getEatingMoney());
            total.add(item.getHealthMoney());
            total.add(item.getHouseMoney());
            total.add(item.getHeatingMoney());
            total.add(item.getSocietyMoney());
            total.add(item.getFundsMoney());
            total.add(item.getLaborUnionMoney());
            total.add(item.getEducationMoney());
            total.add(item.getLabourServiceMoney());
            total.add(item.getOtherMoney());
            total.add(item.getStaffNumber());
            total.add(item.getEmployeeNumber());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));

            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBudgetLaborCostDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveBudgetLaborCostDetail model =new FiveBudgetLaborCostDetail();
            model.setLaborCostId(item.getLaborCostId());
            model.setBusinessKey("");
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setSalaryMoney(MyStringUtil.moneyToString(item.getSalaryMoney()));
            model.setBonusMoney(MyStringUtil.moneyToString(item.getBonusMoney()));
            model.setCoolingMoney(MyStringUtil.moneyToString(item.getCoolingMoney()));
            model.setCarMoney(MyStringUtil.moneyToString(item.getCarMoney()));
            model.setEatingMoney(MyStringUtil.moneyToString(item.getEatingMoney()));
            model.setHealthMoney(MyStringUtil.moneyToString(item.getHealthMoney()));
            model.setHouseMoney(MyStringUtil.moneyToString(item.getHouseMoney()));
            model.setHeatingMoney(MyStringUtil.moneyToString(item.getHeatingMoney()));
            model.setSocietyMoney(MyStringUtil.moneyToString(item.getSocietyMoney()));
            model.setFundsMoney(MyStringUtil.moneyToString(item.getFundsMoney()));
            model.setLaborUnionMoney(MyStringUtil.moneyToString(item.getLaborUnionMoney()));
            model.setEducationMoney(MyStringUtil.moneyToString(item.getEducationMoney()));
            model.setLabourServiceMoney(MyStringUtil.moneyToString(item.getLabourServiceMoney()));
            model.setOtherMoney(MyStringUtil.moneyToString(item.getOtherMoney()));
            model.setStaffNumber(MyStringUtil.moneyToString(item.getStaffNumber()));
            model.setEmployeeNumber(MyStringUtil.moneyToString(item.getEmployeeNumber()));
            model.setLastYearEmployee(MyStringUtil.moneyToString(item.getLastYearEmployee()));
            model.setLastYearStaff(MyStringUtil.moneyToString(item.getLastYearStaff()));

            //计算部门总预算
            List<String> total = new ArrayList();
            total.add(item.getSalaryMoney());
            total.add(item.getBonusMoney());
            total.add(item.getCoolingMoney());
            total.add(item.getCarMoney());
            total.add(item.getEatingMoney());
            total.add(item.getHealthMoney());
            total.add(item.getHouseMoney());
            total.add(item.getHeatingMoney());
            total.add(item.getSocietyMoney());
            total.add(item.getFundsMoney());
            total.add(item.getLaborUnionMoney());
            total.add(item.getEducationMoney());
            total.add(item.getLabourServiceMoney());
            total.add(item.getOtherMoney());
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
            fiveBudgetLaborCostDetailMapper.insert(model);
        }

    }

    public FiveBudgetLaborCostDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetLaborCostDetail item=new FiveBudgetLaborCostDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setLaborCostId(id);
        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetLaborCostDetailMapper.insert(item);
        return getDetailDto(item);
    }

    public FiveBudgetLaborCostDetail getModelDetailById(int id){
        FiveBudgetLaborCostDetailDto detailDto = getDetailDto(fiveBudgetLaborCostDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getSalaryMoney()).equals(0.0)){
            detailDto.setSalaryMoney("");
        }
        if(Double.valueOf(detailDto.getBonusMoney()).equals(0.0)){
            detailDto.setBonusMoney("");
        }
        if(Double.valueOf(detailDto.getCoolingMoney()).equals(0.0)){
            detailDto.setCoolingMoney("");
        }
        if(Double.valueOf(detailDto.getCarMoney()).equals(0.0)){
            detailDto.setCarMoney("");
        }
        if(Double.valueOf(detailDto.getEatingMoney()).equals(0.0)){
            detailDto.setEatingMoney("");
        }
        if(Double.valueOf(detailDto.getHealthMoney()).equals(0.0)){
            detailDto.setHealthMoney("");
        }
        if(Double.valueOf(detailDto.getHouseMoney()).equals(0.0)){
            detailDto.setHouseMoney("");
        }
        if(Double.valueOf(detailDto.getHeatingMoney()).equals(0.0)){
            detailDto.setHeatingMoney("");
        }
        if(Double.valueOf(detailDto.getSocietyMoney()).equals(0.0)){
            detailDto.setSocietyMoney("");
        }
        if(Double.valueOf(detailDto.getFundsMoney()).equals(0.0)){
            detailDto.setFundsMoney("");
        }
        if(Double.valueOf(detailDto.getLaborUnionMoney()).equals(0.0)){
            detailDto.setLaborUnionMoney("");
        }
        if(Double.valueOf(detailDto.getEducationMoney()).equals(0.0)){
            detailDto.setEducationMoney("");
        }
        if(Double.valueOf(detailDto.getLabourServiceMoney()).equals(0.0)){
            detailDto.setLabourServiceMoney("");
        }
        if(Double.valueOf(detailDto.getOtherMoney()).equals(0.0)){
            detailDto.setOtherMoney("");
        }
        if(Double.valueOf(detailDto.getStaffNumber()).equals(0.0)){
            detailDto.setStaffNumber("");
        }
        if(Double.valueOf(detailDto.getEmployeeNumber()).equals(0.0)){
            detailDto.setEmployeeNumber("");
        }
        if(Double.valueOf(detailDto.getLastYearEmployee()).equals(0.0)){
            detailDto.setLastYearEmployee("");
        }
        if(Double.valueOf(detailDto.getLastYearStaff()).equals(0.0)){
            detailDto.setLastYearStaff("");
        }


        return detailDto;
    }

    public void removeDetail(int id){
        FiveBudgetLaborCostDetail model = fiveBudgetLaborCostDetailMapper.selectByPrimaryKey(id);
        //判断是否有子节点
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("laborCostId",model.getLaborCostId());
        map.put("parentId",model.getId());
        List<FiveBudgetLaborCostDetail> details = fiveBudgetLaborCostDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"根节点不能删除！");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetLaborCostDetailMapper.updateByPrimaryKey(model);
    }

    public List<FiveBudgetLaborCostDetailDto> getLastYearDetailById(int id,String budgetYear) {
        List<FiveBudgetLaborCostDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetLaborCostDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        if(Strings.isNullOrEmpty(budgetYear)){
            map.put("budgetYear",dto.getLastYear());
        }else{
            map.put("budgetYear",Integer.valueOf(budgetYear)-1);
        }
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        //排除自己
        List<FiveBudgetLaborCost> fiveBudgetLaborCosts = fiveBudgetLaborCostMapper.selectAll(map).stream().filter(p->p.getId()!=id)
                .sorted(Comparator.comparing(FiveBudgetLaborCost::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetLaborCosts.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("laborCostId",fiveBudgetLaborCosts.get(0).getId());
            List<FiveBudgetLaborCostDetail> details = fiveBudgetLaborCostDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetLaborCostDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetLaborCostDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }
}
