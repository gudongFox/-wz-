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
import com.cmcu.mcc.budget.dao.FiveBudgetTurnInDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetTurnInMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetTurnInDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetTurnInDto;
import com.cmcu.mcc.budget.entity.FiveBudgetTurnIn;
import com.cmcu.mcc.budget.entity.FiveBudgetTurnInDetail;
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
public class FiveBudgetTurnInService {

    @Resource
    FiveBudgetTurnInMapper fiveBudgetTurnInMapper;

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
    FiveBudgetTurnInDetailMapper fiveBudgetTurnInDetailMapper;
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
        FiveBudgetTurnIn item = fiveBudgetTurnInMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetTurnInDto dto) {
        FiveBudgetTurnIn item = fiveBudgetTurnInMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());

        //???????????????
        List<FiveBudgetTurnInDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetTurnInDetailDto detail:details){
            //???????????????
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //???????????????????????????
        List<FiveBudgetTurnInDetailDto> lastYearDetails = getLastYearDetailById(dto.getId(), dto.getBudgetYear());
        //????????????
        for(FiveBudgetTurnInDetail detail:details){
            //?????????????????????????????? ???????????????????????????
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
                detail.setLastYearMoney(MyStringUtil.moneyToString("0"));
                //??????????????????
                for(FiveBudgetTurnInDetailDto detailDto:lastYearDetails){
                    //??????????????? ??????????????????
                    if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(detail.getTypeName())){
                        detail.setLastYearMoney(detailDto.getBudgetMoney());
                    }
                }
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getBudgetTotalMoney()));
            fiveBudgetTurnInDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetTurnInMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetTurnInDto getModelById(int id) {
        return getDto(fiveBudgetTurnInMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetTurnIn item = new FiveBudgetTurnIn();
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

        fiveBudgetTurnInMapper.insert(item);

        String processKey=EdConst.FIVE_BUDGET_TURN_IN;
        String title = "?????????????????????:"+item.getDeptName();

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
        fiveBudgetTurnInMapper.updateByPrimaryKey(item);
        initDetail(item.getId());
        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";
        //???????????????????????????
        List<FiveBudgetTurnInDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);

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
      /*  insertDetail(lastYearDetails,projectBudgetId,parentId1,"12)???????????????(?????????????????????",59,12);
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
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"24)????????????",49,24);*/

        int parentId2 = insertDetail(lastYearDetails,projectBudgetId, parentId, "2.??????2",0,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"1)???????????????????????????????????????",125,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"2)????????????",45,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"3)????????????",34,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"4)????????????",12,4);
        int parentId3 = insertDetail(lastYearDetails,projectBudgetId, parentId, "3.?????????????????????",0, 3);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"??????????????????????????????????????????",0,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"?????????????????????????????????",0,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"??????????????????????????????",0,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"????????????????????????????????????",0,4);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"???????????????????????????????????????",0,5);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"????????????????????????????????????????????????",0,6);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"??????????????????????????????",0,7);
        insertDetail(lastYearDetails,projectBudgetId,parentId3,"??????????????????",0,8);
    }

    public int insertDetail(List<FiveBudgetTurnInDetailDto> lastYearDetails,int projectBudgetId,int parentId,String typeName,int deptId,int seq){
        FiveBudgetTurnInDetail item=new FiveBudgetTurnInDetail();
        //??????????????????
        for(FiveBudgetTurnInDetailDto detailDto:lastYearDetails){
            //??????????????? ??????????????????
            if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(typeName)){
                item.setLastYearMoney(detailDto.getBudgetMoney());
            }
        }

        item.setTurnInId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setLastYearMoney(MyStringUtil.moneyToString("0"));
        item.setLastYearSuccess(MyStringUtil.moneyToString("0"));
        item.setDevelopmentFund(MyStringUtil.moneyToString("0"));
        item.setTurnProfits(MyStringUtil.moneyToString("0"));
        item.setParentId(parentId);
        item.setDeptId(deptId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveBudgetTurnInDetailMapper.insert(item);
        return item.getId();
    }


    public FiveBudgetTurnInDto getDto(Object item) {
        FiveBudgetTurnIn model= (FiveBudgetTurnIn) item;
        FiveBudgetTurnInDto dto=FiveBudgetTurnInDto.adapt(model);
        if(!Strings.isNullOrEmpty(model.getBudgetYear())){
            dto.setLastYear((Integer.valueOf(model.getBudgetYear())-1)+"");
        }
        dto.setProcessName("?????????");
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
           // MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("?????????");
            }
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBudgetTurnInMapper.updateByPrimaryKey(dto);
            }
        }


        return dto;
    }
    public FiveBudgetTurnInDetailDto getDetailDto(Object item) {
        FiveBudgetTurnInDetail model= (FiveBudgetTurnInDetail) item;
        FiveBudgetTurnInDetailDto detailDto=FiveBudgetTurnInDetailDto.adapt(model);
        if(model.getParentId()!=0){
            FiveBudgetTurnInDetail parent = fiveBudgetTurnInDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("???????????????");
        }
        detailDto.setIsParent(false);
        //???????????????????????? ??????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("turnInId",detailDto.getTurnInId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetTurnInDetail> details = fiveBudgetTurnInDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);

            String totalBudgetMoney = MyStringUtil.moneyToString("0");
            String totalDevelopmentFund = MyStringUtil.moneyToString("0");
            String totalTurnProfits = MyStringUtil.moneyToString("0");

            String totalLastYearMoney =MyStringUtil.moneyToString("0");
            String totalLastYearSuccess =MyStringUtil.moneyToString("0");

            for(FiveBudgetTurnInDetail detail:details){
                totalBudgetMoney = MyStringUtil.getNewAddMoney(totalBudgetMoney,detail.getBudgetMoney());
                totalDevelopmentFund = MyStringUtil.getNewAddMoney(totalDevelopmentFund,detail.getDevelopmentFund());
                totalTurnProfits = MyStringUtil.getNewAddMoney(totalTurnProfits,detail.getTurnProfits());
                totalLastYearMoney = MyStringUtil.getNewAddMoney(totalLastYearMoney,detail.getLastYearMoney());
                totalLastYearSuccess = MyStringUtil.getNewAddMoney(totalLastYearSuccess,detail.getLastYearSuccess());

            }

            detailDto.setBudgetMoney(totalBudgetMoney);
            detailDto.setDevelopmentFund(totalDevelopmentFund);
            detailDto.setTurnProfits(totalTurnProfits);
            detailDto.setLastYearMoney(totalLastYearMoney);
            detailDto.setLastYearSuccess(totalLastYearSuccess);

            fiveBudgetTurnInDetailMapper.updateByPrimaryKey(detailDto);
        }else{
            //??????????????????
            String totalDeductionMoney = MyStringUtil.moneyToString("0");
            //????????????
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetType","fiveBudgetTurnInRent");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails = fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //??????
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetTurnInRent");
            map2.put("budgetId",detailDto.getId());
            List<FiveFinanceLoanDetail> fiveFinanceLoanDetails = fiveFinanceLoanDetailMapper.selectAll(map2);
            for(FiveFinanceLoanDetail detail:fiveFinanceLoanDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }

            detailDto.setRemainMoney(MyStringUtil.getNewSubMoney(detailDto.getBudgetMoney(),totalDeductionMoney));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));

        if(detailDto.getDeptId()==0){
            detailDto.setOutAssert(true);
        }else{
            detailDto.setOutAssert(false);
        }

        //????????????
        detailDto.setLastYearMoney(MyStringUtil.moneyToString(detailDto.getLastYearMoney(),6));
        detailDto.setLastYearSuccess(MyStringUtil.moneyToString(detailDto.getLastYearSuccess(),6));
        detailDto.setBudgetMoney(MyStringUtil.moneyToString(detailDto.getBudgetMoney(),6));
        detailDto.setRemainMoney(MyStringUtil.moneyToString(detailDto.getRemainMoney(),6));
        detailDto.setTurnProfits(MyStringUtil.moneyToString(detailDto.getTurnProfits(),6));
        detailDto.setDevelopmentFund(MyStringUtil.moneyToString(detailDto.getDevelopmentFund(),6));

        return detailDto;
    }


    public List<FiveBudgetTurnIn> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetTurnInMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetTurnInMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetTurnIn item=(FiveBudgetTurnIn)p;
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

    public List<FiveBudgetTurnInDetailDto> getDetailById(int id) {
        List<FiveBudgetTurnInDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("turnInId",id);
        List<FiveBudgetTurnInDetail> details = fiveBudgetTurnInDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetTurnInDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetTurnInDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public void updateDetail(FiveBudgetTurnInDetail item){
        if(item.getId()!=null){
            FiveBudgetTurnInDetail model = fiveBudgetTurnInDetailMapper.selectByPrimaryKey(item.getId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setTurnProfits(MyStringUtil.moneyToString(item.getTurnProfits()));
            model.setDevelopmentFund(MyStringUtil.moneyToString(item.getDevelopmentFund()));

            //?????????????????????
            List<String> total = new ArrayList();
            total.add(item.getTurnProfits());
            total.add(item.getDevelopmentFund());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));
            model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));
            model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));

            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBudgetTurnInDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveBudgetTurnInDetail model =new FiveBudgetTurnInDetail();
            model.setTurnInId(item.getTurnInId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setTurnProfits(MyStringUtil.moneyToString(item.getTurnProfits()));
            model.setDevelopmentFund(MyStringUtil.moneyToString(item.getDevelopmentFund()));

            //?????????????????????
            List<String> total = new ArrayList();
            total.add(item.getTurnProfits());
            total.add(item.getDevelopmentFund());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));
            model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));
            model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));

            model.setSeq(item.getSeq());
            model.setDeleted(false);
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            model.setGmtCreate(new Date());
            model.setCreator(item.getCreator());
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
            model.setCreatorName(hrEmployeeDto.getUserName());
            ModelUtil.setNotNullFields(model);
            fiveBudgetTurnInDetailMapper.insert(model);
        }

    }

    public FiveBudgetTurnInDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetTurnInDetail item=new FiveBudgetTurnInDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setTurnInId(id);
        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetTurnInDetailMapper.insert(item);
        return getDetailDto(item);
    }

    public FiveBudgetTurnInDetail getModelDetailById(int id){
        FiveBudgetTurnInDetailDto detailDto = getDetailDto(fiveBudgetTurnInDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getBudgetMoney()).equals(0.0)){
            detailDto.setBudgetMoney("");
        }
        if(Double.valueOf(detailDto.getDevelopmentFund()).equals(0.0)){
            detailDto.setDevelopmentFund("");
        }
        if(Double.valueOf(detailDto.getTurnProfits()).equals(0.0)){
            detailDto.setTurnProfits("");
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
        FiveBudgetTurnInDetail model = fiveBudgetTurnInDetailMapper.selectByPrimaryKey(id);
        //????????????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("turnInId",model.getTurnInId());
        map.put("parentId",model.getId());
        List<FiveBudgetTurnInDetail> details = fiveBudgetTurnInDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"????????????????????????");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetTurnInDetailMapper.updateByPrimaryKey(model);
    }

    public List<FiveBudgetTurnInDetailDto> getLastYearDetailById(int id,String budgetYear) {
        List<FiveBudgetTurnInDetailDto> list =new ArrayList<>();
        //??????????????????
        FiveBudgetTurnInDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        if(Strings.isNullOrEmpty(budgetYear)){
            map.put("budgetYear",dto.getLastYear());
        }else{
            map.put("budgetYear",Integer.valueOf(budgetYear)-1);
        }
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        //????????????
        List<FiveBudgetTurnIn> fiveBudgetTurnIns = fiveBudgetTurnInMapper.selectAll(map).stream().filter(p->p.getId()!=id)
                .sorted(Comparator.comparing(FiveBudgetTurnIn::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetTurnIns.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("turnInId",fiveBudgetTurnIns.get(0).getId());
            List<FiveBudgetTurnInDetail> details = fiveBudgetTurnInDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetTurnInDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetTurnInDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }
}
