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
import com.cmcu.mcc.budget.dao.FiveBudgetPublicFundsDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetPublicFundsMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetPublicFundsDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetPublicFundsDto;
import com.cmcu.mcc.budget.entity.FiveBudgetPublicFunds;
import com.cmcu.mcc.budget.entity.FiveBudgetPublicFundsDetail;
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
public class FiveBudgetPublicFundsService {

    @Resource
    FiveBudgetPublicFundsMapper fiveBudgetPublicFundsMapper;

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
    FiveBudgetPublicFundsDetailMapper fiveBudgetPublicFundsDetailMapper;
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
        FiveBudgetPublicFunds item = fiveBudgetPublicFundsMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetPublicFundsDto dto) {
        FiveBudgetPublicFunds item = fiveBudgetPublicFundsMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());


        //计算总金额
        List<FiveBudgetPublicFundsDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetPublicFundsDetailDto detail:details){
            //排除父节点
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //每项占比
        for(FiveBudgetPublicFundsDetail detail:details){
            //如果有预算年份有变化 子表预算项跟着改变
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getBudgetTotalMoney()));
            fiveBudgetPublicFundsDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetPublicFundsMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetPublicFundsDto getModelById(int id) {
        return getDto(fiveBudgetPublicFundsMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetPublicFunds item = new FiveBudgetPublicFunds();
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

        fiveBudgetPublicFundsMapper.insert(item);

        String processKey=EdConst.FIVE_BUDGET_PUBLICFUNDS;
        String title = "公用经费预算:"+item.getDeptName();

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
        fiveBudgetPublicFundsMapper.updateByPrimaryKey(item);

        initDetail(item.getId());

        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        int parentId = insertDetail(projectBudgetId, 0, "总计",0, 1);
        int parentId1 = insertDetail(projectBudgetId, parentId, "1.小计1",0, 1);
        insertDetail(projectBudgetId,parentId1,"1)第一设计研究院",75,1);
        insertDetail(projectBudgetId,parentId1,"2)第二设计研究院",74,2);
        insertDetail(projectBudgetId,parentId1,"3)建筑规划设计研究院",47,3);
        insertDetail(projectBudgetId,parentId1,"4)环境与能源设计研究院",76,4);
        insertDetail(projectBudgetId,parentId1,"5)钢结构工程技术中心",20,5);
        insertDetail(projectBudgetId,parentId1,"6)五源现代项目管理中心",36,6);
        insertDetail(projectBudgetId,parentId1,"7)石油化工设计研究院",53,7);
        insertDetail(projectBudgetId,parentId1,"8)轨道交通设计研究院",63,8);
        insertDetail(projectBudgetId,parentId1,"9)火炸药工程技术研究中心",7,9);
        insertDetail(projectBudgetId,parentId1,"10)计算机应用研究所",50,10);
        insertDetail(projectBudgetId,parentId1,"11)成品室",13,11);
        insertDetail(projectBudgetId,parentId1,"12)公司办公室(董事会办公室）",59,12);
        insertDetail(projectBudgetId,parentId1,"13)保密办公室",100,13);
        insertDetail(projectBudgetId,parentId1,"14)党群工作部",72,14);
        insertDetail(projectBudgetId,parentId1,"15)经营发展部",48,15);
        insertDetail(projectBudgetId,parentId1,"16)信息化建设与管理部",11,16);
        insertDetail(projectBudgetId,parentId1,"17)财务金融部",18,17);
        insertDetail(projectBudgetId,parentId1,"18)人力资源部",38,18);
        insertDetail(projectBudgetId,parentId1,"19)工程管理部",29,19);
        insertDetail(projectBudgetId,parentId1,"20)纪检工作部、审计与风险管理部",9,20);
        insertDetail(projectBudgetId,parentId1,"21)科研与技术质量部",101,21);
        insertDetail(projectBudgetId,parentId1,"22)行政事务部",67,22);
        insertDetail(projectBudgetId,parentId1,"23)档案图书室",58,23);
        insertDetail(projectBudgetId,parentId1,"24)物业开发",49,24);

        int parentId2 = insertDetail(projectBudgetId, parentId, "2.小计2",0,2);
        insertDetail(projectBudgetId,parentId2,"1)中兵勘察设计研究院有限公司",125,1);
        insertDetail(projectBudgetId,parentId2,"2)五环监理",45,2);
        insertDetail(projectBudgetId,parentId2,"3)五特公司",34,3);
        insertDetail(projectBudgetId,parentId2,"4)五洲中兴",12,4);
    }

    public int insertDetail(int projectBudgetId,int parentId,String typeName,int deptId,int seq){
        FiveBudgetPublicFundsDetail item=new FiveBudgetPublicFundsDetail();
        item.setPublicFundsId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setWaterMoney(MyStringUtil.moneyToString("0"));
        item.setElectricMoney(MyStringUtil.moneyToString("0"));
        item.setHeatingMoneySelf(MyStringUtil.moneyToString("0"));
        item.setHeatingMoneyPublic(MyStringUtil.moneyToString("0"));
        item.setHeatingMoneyTotal(MyStringUtil.moneyToString("0"));
        item.setHouseMoney(MyStringUtil.moneyToString("0"));
        item.setHealthMoney(MyStringUtil.moneyToString("0"));
        item.setParentId(parentId);
        item.setDeptId(deptId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveBudgetPublicFundsDetailMapper.insert(item);
        return item.getId();
    }


    public FiveBudgetPublicFundsDto getDto(Object item) {
        FiveBudgetPublicFunds model= (FiveBudgetPublicFunds) item;
        FiveBudgetPublicFundsDto dto=FiveBudgetPublicFundsDto.adapt(model);

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
                fiveBudgetPublicFundsMapper.updateByPrimaryKey(dto);
            }
        }

        return dto;
    }
    public FiveBudgetPublicFundsDetailDto getDetailDto(Object item) {
        FiveBudgetPublicFundsDetail model= (FiveBudgetPublicFundsDetail) item;
        FiveBudgetPublicFundsDetailDto detailDto=FiveBudgetPublicFundsDetailDto.adapt(model);
        if(model.getParentId()!=0){
            FiveBudgetPublicFundsDetail parent = fiveBudgetPublicFundsDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("最外层显示");
        }
        detailDto.setIsParent(false);
        //判断是否有子节点 有则计算金额
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("publicFundsId",detailDto.getPublicFundsId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetPublicFundsDetail> details = fiveBudgetPublicFundsDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);

            String totalBudgetMoney = MyStringUtil.moneyToString("0");
            String totalWaterMoney = MyStringUtil.moneyToString("0");
            String totalElectricMoney = MyStringUtil.moneyToString("0");
            String totalHeatingMoneyTotal = MyStringUtil.moneyToString("0");
            String totalHeatingMoneySelf = MyStringUtil.moneyToString("0");
            String totalHeatingMoneyPublic = MyStringUtil.moneyToString("0");
            String totalHouseMoney = MyStringUtil.moneyToString("0");
            String totalHealthMoney = MyStringUtil.moneyToString("0");

            for(FiveBudgetPublicFundsDetail detail:details){
                totalBudgetMoney = MyStringUtil.getNewAddMoney(totalBudgetMoney,detail.getBudgetMoney());
                totalWaterMoney = MyStringUtil.getNewAddMoney(totalWaterMoney,detail.getWaterMoney());
                totalElectricMoney = MyStringUtil.getNewAddMoney(totalElectricMoney,detail.getElectricMoney());
                totalHeatingMoneyTotal = MyStringUtil.getNewAddMoney(totalHeatingMoneyTotal,detail.getHeatingMoneyTotal());
                totalHeatingMoneySelf = MyStringUtil.getNewAddMoney(totalHeatingMoneySelf,detail.getHeatingMoneySelf());
                totalHeatingMoneyPublic = MyStringUtil.getNewAddMoney(totalHeatingMoneyPublic,detail.getHeatingMoneyPublic());
                totalHouseMoney = MyStringUtil.getNewAddMoney(totalHouseMoney,detail.getHouseMoney());
                totalHealthMoney = MyStringUtil.getNewAddMoney(totalHealthMoney,detail.getHealthMoney());
            }

            detailDto.setBudgetMoney(totalBudgetMoney);
            detailDto.setWaterMoney(totalWaterMoney);
            detailDto.setElectricMoney(totalElectricMoney);
            detailDto.setHeatingMoneyTotal(totalHeatingMoneyTotal);
            detailDto.setHeatingMoneySelf(totalHeatingMoneySelf);
            detailDto.setHeatingMoneyPublic(totalHeatingMoneyPublic);
            detailDto.setHouseMoney(totalHouseMoney);
            detailDto.setHealthMoney(totalHealthMoney);

            fiveBudgetPublicFundsDetailMapper.updateByPrimaryKey(detailDto);
        }else{
            //计算剩余金额
            String totalDeductionMoney = MyStringUtil.moneyToString("0");
            //费用申请
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetType","fiveBudgetPublicFunds");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails = fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //借款
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetPublicFunds");
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

        detailDto.setWaterMoney(MyStringUtil.moneyToString(detailDto.getWaterMoney(),6));
        detailDto.setElectricMoney(MyStringUtil.moneyToString(detailDto.getElectricMoney(),6));
        detailDto.setHeatingMoneyTotal(MyStringUtil.moneyToString(detailDto.getHeatingMoneyTotal(),6));
        detailDto.setHeatingMoneySelf(MyStringUtil.moneyToString(detailDto.getHeatingMoneySelf(),6));
        detailDto.setHeatingMoneyPublic(MyStringUtil.moneyToString(detailDto.getHeatingMoneyPublic(),6));
        detailDto.setHouseMoney(MyStringUtil.moneyToString(detailDto.getHouseMoney(),6));
        detailDto.setHealthMoney(MyStringUtil.moneyToString(detailDto.getHealthMoney(),6));

        return detailDto;
    }


    public List<FiveBudgetPublicFunds> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetPublicFundsMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetPublicFundsMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetPublicFunds item=(FiveBudgetPublicFunds)p;
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

    public List<FiveBudgetPublicFundsDetailDto> getDetailById(int id) {
        List<FiveBudgetPublicFundsDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("publicFundsId",id);
        List<FiveBudgetPublicFundsDetail> details = fiveBudgetPublicFundsDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetPublicFundsDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetPublicFundsDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public void updateDetail(FiveBudgetPublicFundsDetail item){
        if(item.getId()!=null){
            FiveBudgetPublicFundsDetail model = fiveBudgetPublicFundsDetailMapper.selectByPrimaryKey(item.getId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setWaterMoney(MyStringUtil.moneyToString(item.getWaterMoney()));
            model.setElectricMoney(MyStringUtil.moneyToString(item.getElectricMoney()));
            model.setHeatingMoneyPublic(MyStringUtil.moneyToString(item.getHeatingMoneyPublic()));
            model.setHeatingMoneySelf(MyStringUtil.moneyToString(item.getHeatingMoneySelf()));
            model.setHouseMoney(MyStringUtil.moneyToString(item.getHouseMoney()));
            model.setHealthMoney(MyStringUtil.moneyToString(item.getHealthMoney()));

            //计算供暖费总额
            List<String> total = new ArrayList();
            total.add(item.getHeatingMoneySelf());
            total.add(item.getHeatingMoneyPublic());
            model.setHeatingMoneyTotal(MyStringUtil.getNewTotalListMoney(total));
            //计算部门总预算
            List<String> total1 = new ArrayList();
            total1.add(item.getWaterMoney());
            total1.add(item.getElectricMoney());
            total1.add(item.getHeatingMoneyTotal());
            total1.add(item.getHouseMoney());
            total1.add(item.getHealthMoney());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total1));

            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBudgetPublicFundsDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveBudgetPublicFundsDetail model =new FiveBudgetPublicFundsDetail();
            model.setPublicFundsId(item.getPublicFundsId());
            model.setBusinessKey("");
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setWaterMoney(MyStringUtil.moneyToString(item.getWaterMoney()));
            model.setElectricMoney(MyStringUtil.moneyToString(item.getElectricMoney()));
            model.setHeatingMoneyPublic(MyStringUtil.moneyToString(item.getHeatingMoneyPublic()));
            model.setHeatingMoneySelf(MyStringUtil.moneyToString(item.getHeatingMoneySelf()));
            model.setHouseMoney(MyStringUtil.moneyToString(item.getHouseMoney()));
            model.setHealthMoney(MyStringUtil.moneyToString(item.getHealthMoney()));

            //计算供暖费总额
            List<String> total = new ArrayList();
            total.add(item.getHeatingMoneySelf());
            total.add(item.getHeatingMoneyPublic());
            model.setHeatingMoneyTotal(MyStringUtil.getNewTotalListMoney(total));
            //计算部门总预算
            List<String> total1 = new ArrayList();
            total1.add(item.getWaterMoney());
            total1.add(item.getElectricMoney());
            total1.add(item.getHeatingMoneyTotal());
            total1.add(item.getHouseMoney());
            total1.add(item.getHealthMoney());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total1));

            model.setSeq(item.getSeq());
            model.setDeleted(false);
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            model.setGmtCreate(new Date());
            model.setCreator(item.getCreator());
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
            model.setCreatorName(hrEmployeeDto.getUserName());
            ModelUtil.setNotNullFields(model);
            fiveBudgetPublicFundsDetailMapper.insert(model);
        }

    }

    public FiveBudgetPublicFundsDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetPublicFundsDetail item=new FiveBudgetPublicFundsDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setPublicFundsId(id);
        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetPublicFundsDetailMapper.insert(item);
        return getDetailDto(item);
    }


    public FiveBudgetPublicFundsDetail getModelDetailById(int id){
        FiveBudgetPublicFundsDetailDto detailDto = getDetailDto(fiveBudgetPublicFundsDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getBudgetMoney()).equals(0.0)){
            detailDto.setBudgetMoney("");
        }
        if(Double.valueOf(detailDto.getWaterMoney()).equals(0.0)){
            detailDto.setWaterMoney("");
        }
        if(Double.valueOf(detailDto.getElectricMoney()).equals(0.0)){
            detailDto.setElectricMoney("");
        }
        if(Double.valueOf(detailDto.getHeatingMoneyTotal()).equals(0.0)){
            detailDto.setHeatingMoneyTotal("");
        }
        if(Double.valueOf(detailDto.getHeatingMoneySelf()).equals(0.0)){
            detailDto.setHeatingMoneySelf("");
        }
        if(Double.valueOf(detailDto.getHeatingMoneyPublic()).equals(0.0)){
            detailDto.setHeatingMoneyPublic("");
        }
        if(Double.valueOf(detailDto.getHouseMoney()).equals(0.0)){
            detailDto.setHouseMoney("");
        }
        if(Double.valueOf(detailDto.getHealthMoney()).equals(0.0)){
            detailDto.setHealthMoney("");
        }
        return detailDto;
    }

    public List<FiveBudgetPublicFundsDetailDto> getLastYearDetailById(int id) {
        List<FiveBudgetPublicFundsDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetPublicFundsDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetYear",dto.getLastYear());
        List<FiveBudgetPublicFunds> fiveBudgetPublicFundss = fiveBudgetPublicFundsMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetPublicFunds::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetPublicFundss.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("publicFundsId",fiveBudgetPublicFundss.get(0).getId());
            List<FiveBudgetPublicFundsDetail> details = fiveBudgetPublicFundsDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetPublicFundsDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetPublicFundsDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }

    public void removeDetail(int id){
        FiveBudgetPublicFundsDetail model = fiveBudgetPublicFundsDetailMapper.selectByPrimaryKey(id);
        //判断是否有子节点
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("publicFundsId",model.getPublicFundsId());
        map.put("parentId",model.getId());
        List<FiveBudgetPublicFundsDetail> details = fiveBudgetPublicFundsDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"根节点不能删除！");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetPublicFundsDetailMapper.updateByPrimaryKey(model);
    }
}
