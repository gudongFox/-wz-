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
import com.cmcu.mcc.budget.dao.FiveBudgetCapitalOutDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetCapitalOutMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetCapitalOutDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetCapitalOutDto;
import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOut;
import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOutDetail;
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
public class FiveBudgetCapitalOutService {

    @Resource
    FiveBudgetCapitalOutMapper fiveBudgetCapitalOutMapper;

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
    FiveBudgetCapitalOutDetailMapper fiveBudgetCapitalOutDetailMapper;
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
        FiveBudgetCapitalOut item = fiveBudgetCapitalOutMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetCapitalOutDto dto) {
        FiveBudgetCapitalOut item = fiveBudgetCapitalOutMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());

        //???????????????
        List<FiveBudgetCapitalOutDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetCapitalOutDetailDto detail:details){
            //???????????????
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //???????????????????????????
        List<FiveBudgetCapitalOutDetailDto> lastYearDetails = getLastYearDetailById(dto.getId(), dto.getBudgetYear());
        //????????????
        for(FiveBudgetCapitalOutDetail detail:details){
            //?????????????????????????????? ???????????????????????????
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
                detail.setLastYearMoney(MyStringUtil.moneyToString("0"));
                //??????????????????
                for(FiveBudgetCapitalOutDetailDto detailDto:lastYearDetails){
                    //??????????????? ??????????????????
                    if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(detail.getTypeName())){
                        detail.setLastYearMoney(detailDto.getBudgetMoney());
                    }
                }
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getBudgetTotalMoney()));
            fiveBudgetCapitalOutDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetCapitalOutMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetCapitalOutDto getModelById(int id) {
        return getDto(fiveBudgetCapitalOutMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetCapitalOut item = new FiveBudgetCapitalOut();
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

        fiveBudgetCapitalOutMapper.insert(item);

        String processKey=EdConst.FIVE_BUDGET_CAPITALOUT;
        String title = "?????????????????????:"+item.getDeptName();

        if(uiSref.equalsIgnoreCase("budget.turnInRent")){ //??????????????????
            processKey=EdConst.FIVE_BUDGET_TURNINRENT;
            title = "??????????????????:"+item.getDeptName();
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
        fiveBudgetCapitalOutMapper.updateByPrimaryKey(item);
        if(uiSref.equalsIgnoreCase("budget.turnInRent")) { //??????????????????
            initRentDetail(item.getId());
        }else{
            initDetail(item.getId());
        }


        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";

        //???????????????????????????
        List<FiveBudgetCapitalOutDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);
        
        int parentId = insertDetail(lastYearDetails,projectBudgetId, 0, "??????",0, 1);
        int parentId1 = insertDetail(lastYearDetails,projectBudgetId, parentId, "1.??????1",0, 1);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"1)?????????????????????",75,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"2)?????????????????????",74,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"3)???????????????????????????",47,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"4)??????????????????????????????",76,4);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"5)???????????????????????????",20,5);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"6)??????????????????????????????",36,6);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"7)???????????????????????????",53,7);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"8)???????????????????????????",63,8);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"9)?????????????????????????????????",7,9);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"10)????????????????????????",50,10);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"11)?????????",13,11);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"12)???????????????(?????????????????????",59,12);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"13)???????????????",100,13);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"14)???????????????",72,14);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"15)???????????????",48,15);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"16)???????????????????????????",11,16);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"17)???????????????",18,17);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"18)???????????????",38,18);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"19)???????????????",29,19);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"20)??????????????????????????????????????????",9,20);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"21)????????????????????????",101,21);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"22)???????????????",67,22);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"23)???????????????",58,23);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"24)????????????",49,24);

        int parentId2 = insertDetail(lastYearDetails,projectBudgetId, parentId, "2.??????2",0,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"1)???????????????????????????????????????",125,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"2)????????????",45,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"3)????????????",34,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"4)????????????",12,4);
    }
    //??????????????????
    private void initRentDetail(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";

        //???????????????????????????
        List<FiveBudgetCapitalOutDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);

        int parentId = insertDetail(lastYearDetails,projectBudgetId, 0, "??????",0, 1);
        int parentId1 = insertDetail(lastYearDetails,projectBudgetId, parentId, "1.??????1",0, 1);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"1)?????????????????????",75,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"2)?????????????????????",74,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"3)???????????????????????????",47,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"4)??????????????????????????????",76,4);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"5)???????????????????????????",20,5);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"6)??????????????????????????????",36,6);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"7)???????????????????????????",53,7);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"8)???????????????????????????",63,8);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"9)?????????????????????????????????",7,9);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"10)????????????????????????",50,10);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"11)?????????",13,11);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"12)???????????????????????????????????????",125,12);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"13)????????????",45,13);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"14)????????????",34,14);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"15)????????????",12,15);

        int parentId2 = insertDetail(lastYearDetails,projectBudgetId, parentId, "2.??????2",0,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"1)????????????",0,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"2)????????????",0,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"3)?????????",0,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"4)217???",0,4);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"5)??????????????????",0,5);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"6)??????????????????",0,6);
    }


    public int insertDetail(List<FiveBudgetCapitalOutDetailDto> lastYearDetails,int projectBudgetId,int parentId,String typeName,int deptId,int seq){
        FiveBudgetCapitalOutDetail item=new FiveBudgetCapitalOutDetail();
        //??????????????????
        for(FiveBudgetCapitalOutDetailDto detailDto:lastYearDetails){
            //??????????????? ??????????????????
            if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(typeName)){
                item.setLastYearMoney(detailDto.getBudgetMoney());
            }
        }
        item.setCapitalOutId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setLastYearMoney(MyStringUtil.moneyToString("0"));

        item.setRentMoney(MyStringUtil.moneyToString("0"));
        item.setStockMoney(MyStringUtil.moneyToString("0"));
        item.setSoftMoney(MyStringUtil.moneyToString("0"));
        item.setCarMoney(MyStringUtil.moneyToString("0"));
        item.setFurnitureMoney(MyStringUtil.moneyToString("0"));
        item.setOaMoney(MyStringUtil.moneyToString("0"));
        item.setLastYearSuccess(MyStringUtil.moneyToString("0"));

        item.setParentId(parentId);
        item.setDeptId(deptId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveBudgetCapitalOutDetailMapper.insert(item);
        return item.getId();
    }


    public FiveBudgetCapitalOutDto getDto(Object item) {
        FiveBudgetCapitalOut model= (FiveBudgetCapitalOut) item;
        FiveBudgetCapitalOutDto dto=FiveBudgetCapitalOutDto.adapt(model);
        if(!Strings.isNullOrEmpty(model.getBudgetYear())){
            dto.setLastYear((Integer.valueOf(model.getBudgetYear())-1)+"");
        }
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            //MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("?????????");
            }
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBudgetCapitalOutMapper.updateByPrimaryKey(dto);
            }
        }

        return dto;
    }
    public FiveBudgetCapitalOutDetailDto getDetailDto(Object item) {
        FiveBudgetCapitalOutDetail model= (FiveBudgetCapitalOutDetail) item;
        FiveBudgetCapitalOutDetailDto detailDto=FiveBudgetCapitalOutDetailDto.adapt(model);
        if(model.getParentId()!=0){
            FiveBudgetCapitalOutDetail parent = fiveBudgetCapitalOutDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("???????????????");
        }
        detailDto.setIsParent(false);
        //???????????????????????? ??????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("capitalOutId",detailDto.getCapitalOutId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetCapitalOutDetail> details = fiveBudgetCapitalOutDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);

            String totalBudgetMoney = MyStringUtil.moneyToString("0");
            String totalOaMoney = MyStringUtil.moneyToString("0");
            String totalFurnitureMoney = MyStringUtil.moneyToString("0");
            String totalCarMoney = MyStringUtil.moneyToString("0");
            String totalSoftMoney = MyStringUtil.moneyToString("0");
            String totalStockMoney = MyStringUtil.moneyToString("0");
            String totalLastYearSuccess =MyStringUtil.moneyToString("0");

            for(FiveBudgetCapitalOutDetail detail:details){
                totalBudgetMoney = MyStringUtil.getNewAddMoney(totalBudgetMoney,detail.getBudgetMoney());
                totalOaMoney = MyStringUtil.getNewAddMoney(totalOaMoney,detail.getOaMoney());
                totalFurnitureMoney = MyStringUtil.getNewAddMoney(totalFurnitureMoney,detail.getFurnitureMoney());
                totalCarMoney = MyStringUtil.getNewAddMoney(totalCarMoney,detail.getCarMoney());
                totalSoftMoney = MyStringUtil.getNewAddMoney(totalSoftMoney,detail.getSoftMoney());
                totalStockMoney = MyStringUtil.getNewAddMoney(totalStockMoney,detail.getSoftMoney());
                totalLastYearSuccess = MyStringUtil.getNewAddMoney(totalLastYearSuccess,detail.getLastYearSuccess());

            }

            detailDto.setBudgetMoney(totalBudgetMoney);
            detailDto.setOaMoney(totalOaMoney);
            detailDto.setFurnitureMoney(totalFurnitureMoney);
            detailDto.setCarMoney(totalCarMoney);
            detailDto.setSoftMoney(totalSoftMoney);
            detailDto.setStockMoney(totalStockMoney);
            detailDto.setLastYearSuccess(totalLastYearSuccess);
            fiveBudgetCapitalOutDetailMapper.updateByPrimaryKey(detailDto);
        }else{
            //??????????????????
            String totalDeductionMoney = MyStringUtil.moneyToString("0");
            //????????????
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetType","fiveBudgetCapitalOut");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails = fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //??????
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetCapitalOut");
            map2.put("budgetId",detailDto.getId());
            List<FiveFinanceLoanDetail> fiveFinanceLoanDetails = fiveFinanceLoanDetailMapper.selectAll(map2);
            for(FiveFinanceLoanDetail detail:fiveFinanceLoanDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }

            detailDto.setRemainMoney(MyStringUtil.getNewSubMoney(detailDto.getBudgetMoney(),totalDeductionMoney));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));

        //????????????
        detailDto.setLastYearMoney(MyStringUtil.moneyToString(detailDto.getLastYearMoney(),6));
        detailDto.setLastYearSuccess(MyStringUtil.moneyToString(detailDto.getLastYearSuccess(),6));
        detailDto.setBudgetMoney(MyStringUtil.moneyToString(detailDto.getBudgetMoney(),6));
        detailDto.setRemainMoney(MyStringUtil.moneyToString(detailDto.getRemainMoney(),6));
        detailDto.setOaMoney(MyStringUtil.moneyToString(detailDto.getOaMoney(),6));
        detailDto.setFurnitureMoney(MyStringUtil.moneyToString(detailDto.getFurnitureMoney(),6));
        detailDto.setCarMoney(MyStringUtil.moneyToString(detailDto.getCarMoney(),6));
        detailDto.setSoftMoney(MyStringUtil.moneyToString(detailDto.getSoftMoney(),6));
        detailDto.setStockMoney(MyStringUtil.moneyToString(detailDto.getStockMoney(),6));

        return detailDto;
    }


    public List<FiveBudgetCapitalOut> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetCapitalOutMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/

        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        if(uiSref.equalsIgnoreCase("budget.turnInRent")){ //??????????????????
            params.put("qBusinessKey","fiveBudgetTurnInRent");
        } else if(uiSref.equalsIgnoreCase("budget.capitalOut")){ //?????????????????????
            params.put("qBusinessKey","fiveBudgetCapitalOut");
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetCapitalOutMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetCapitalOut item=(FiveBudgetCapitalOut)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * ???????????????????????????
     * @param money
     * @return
     */
    public String moneyTurnCapital(Double money){
        return NumberChangeUtils.moneyToChinese(money*10000);
    }

    public List<FiveBudgetCapitalOutDetailDto> getDetailById(int id) {
        List<FiveBudgetCapitalOutDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("capitalOutId",id);
        List<FiveBudgetCapitalOutDetail> details = fiveBudgetCapitalOutDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetCapitalOutDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetCapitalOutDetail detail:details){
            FiveBudgetCapitalOutDetailDto detailDto = getDetailDto(detail);
            list.add(detailDto);
        }
        return list;
    }

    public void updateDetail(FiveBudgetCapitalOutDetail item){
        if(item.getId()!=null){
            FiveBudgetCapitalOutDetail model = fiveBudgetCapitalOutDetailMapper.selectByPrimaryKey(item.getId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setOaMoney(MyStringUtil.moneyToString(item.getOaMoney()));
            model.setFurnitureMoney(MyStringUtil.moneyToString(item.getFurnitureMoney()));
            model.setCarMoney(MyStringUtil.moneyToString(item.getCarMoney()));
            model.setSoftMoney(MyStringUtil.moneyToString(item.getSoftMoney()));
            model.setStockMoney(MyStringUtil.moneyToString(item.getStockMoney()));
            model.setRentMoney(MyStringUtil.moneyToString(item.getRentMoney()));

            //?????????????????????
            List<String> total = new ArrayList();
            total.add(item.getOaMoney());
            total.add(item.getFurnitureMoney());
            total.add(item.getCarMoney());
            total.add(item.getSoftMoney());
            total.add(item.getStockMoney());
            total.add(item.getRentMoney());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));
            model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));
            model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));

            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBudgetCapitalOutDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveBudgetCapitalOutDetail model =new FiveBudgetCapitalOutDetail();
            model.setCapitalOutId(item.getCapitalOutId());
            model.setBusinessKey("");
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setOaMoney(MyStringUtil.moneyToString(item.getOaMoney()));
            model.setFurnitureMoney(MyStringUtil.moneyToString(item.getFurnitureMoney()));
            model.setCarMoney(MyStringUtil.moneyToString(item.getCarMoney()));
            model.setSoftMoney(MyStringUtil.moneyToString(item.getSoftMoney()));
            model.setStockMoney(MyStringUtil.moneyToString(item.getStockMoney()));
            model.setRentMoney(MyStringUtil.moneyToString(item.getRentMoney()));

            //?????????????????????
            List<String> total = new ArrayList();
            total.add(item.getOaMoney());
            total.add(item.getFurnitureMoney());
            total.add(item.getCarMoney());
            total.add(item.getSoftMoney());
            total.add(item.getStockMoney());
            total.add(item.getRentMoney());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));
            model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));
            model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));

            model.setSeq(item.getSeq());
            model.setDeleted(false);
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            model.setGmtCreate(new Date());
            model.setCreator(item.getCreator());
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
            model.setCreatorName(hrEmployeeDto.getUserName());
            ModelUtil.setNotNullFields(model);
            fiveBudgetCapitalOutDetailMapper.insert(model);
        }

    }

    public FiveBudgetCapitalOutDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetCapitalOutDetail item=new FiveBudgetCapitalOutDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCapitalOutId(id);
        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetCapitalOutDetailMapper.insert(item);
        return getDetailDto(item);
    }

    public FiveBudgetCapitalOutDetail getModelDetailById(int id){
        FiveBudgetCapitalOutDetailDto detailDto = getDetailDto(fiveBudgetCapitalOutDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getBudgetMoney()).equals(0.0)){
            detailDto.setBudgetMoney("");
        }
        if(Double.valueOf(detailDto.getLastYearMoney()).equals(0.0)){
            detailDto.setLastYearMoney("");
        }
        if(Double.valueOf(detailDto.getOaMoney()).equals(0.0)){
            detailDto.setOaMoney("");
        }
        if(Double.valueOf(detailDto.getFurnitureMoney()).equals(0.0)){
            detailDto.setFurnitureMoney("");
        }
        if(Double.valueOf(detailDto.getCarMoney()).equals(0.0)){
            detailDto.setCarMoney("");
        }
        if(Double.valueOf(detailDto.getSoftMoney()).equals(0.0)){
            detailDto.setSoftMoney("");
        }
        if(Double.valueOf(detailDto.getStockMoney()).equals(0.0)){
            detailDto.setCarMoney("");
        }
        if(Double.valueOf(detailDto.getLastYearSuccess()).equals(0.0)){
            detailDto.setLastYearSuccess("");
        }
        if(Double.valueOf(detailDto.getRentMoney()).equals(0.0)){
            detailDto.setRentMoney("");
        }
        return detailDto;
    }

    public void removeDetail(int id){
        FiveBudgetCapitalOutDetail model = fiveBudgetCapitalOutDetailMapper.selectByPrimaryKey(id);
        //????????????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("capitalOutId",model.getCapitalOutId());
        map.put("parentId",model.getId());
        List<FiveBudgetCapitalOutDetail> details = fiveBudgetCapitalOutDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"????????????????????????");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetCapitalOutDetailMapper.updateByPrimaryKey(model);
    }

    public List<FiveBudgetCapitalOutDetailDto> getLastYearDetailById(int id,String budgetYear) {
        List<FiveBudgetCapitalOutDetailDto> list =new ArrayList<>();
        //??????????????????
        FiveBudgetCapitalOutDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        if(Strings.isNullOrEmpty(budgetYear)){
            map.put("budgetYear",dto.getLastYear());
        }else{
            map.put("budgetYear",Integer.valueOf(budgetYear)-1);
        }
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        //????????????
        List<FiveBudgetCapitalOut> fiveBudgetCapitalOuts = fiveBudgetCapitalOutMapper.selectAll(map).stream().filter(p->p.getId()!=id)
                .sorted(Comparator.comparing(FiveBudgetCapitalOut::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetCapitalOuts.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("capitalOutId",fiveBudgetCapitalOuts.get(0).getId());
            List<FiveBudgetCapitalOutDetail> details = fiveBudgetCapitalOutDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetCapitalOutDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetCapitalOutDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }
}
