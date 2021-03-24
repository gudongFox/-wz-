package com.cmcu.mcc.budget.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.budget.dao.FiveBudgetMaintainDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetMaintainMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetMaintainDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetMaintainDto;
import com.cmcu.mcc.budget.entity.FiveBudgetMaintain;
import com.cmcu.mcc.budget.entity.FiveBudgetMaintainDetail;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceLoanDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceTransferAccountsDetailMapper;
import com.cmcu.mcc.finance.entity.FiveFinanceLoanDetail;
import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccountsDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
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
public class FiveBudgetMaintainService {
    @Resource
    FiveBudgetMaintainMapper fiveBudgetMaintainMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveBudgetMaintainDetailMapper fiveBudgetMaintainDetailMapper;

    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;
    @Autowired
    ProcessQueryService processQueryService;

    @Resource
    FiveFinanceTransferAccountsDetailMapper fiveFinanceTransferAccountsDetailMapper;
    @Resource
    FiveFinanceLoanDetailMapper fiveFinanceLoanDetailMapper;


    public void remove(int id,String userLogin){
        FiveBudgetMaintain item = fiveBudgetMaintainMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetMaintainDto dto){
        FiveBudgetMaintain model = fiveBudgetMaintainMapper.selectByPrimaryKey(dto.getId());
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setTitle(dto.getTitle());
        String totalBudgetMoney ="0";
        List<FiveBudgetMaintainDetail> details = listDetail(model.getId());
        for(FiveBudgetMaintainDetail detail:details){
            totalBudgetMoney = MyStringUtil.getNewAddMoney(totalBudgetMoney,detail.getBudgetMoney());
        }
        model.setTotalBudgetMoney(totalBudgetMoney);
        //判断年份是否修改
        if(!dto.getBudgetYear().equalsIgnoreCase(model.getBudgetYear())){
            //删除修改前 去年的项目
            List<FiveBudgetMaintainDetail> fiveBudgetMaintainDetails = listDetail(model.getId());
            for(FiveBudgetMaintainDetail detail:fiveBudgetMaintainDetails){
                if(detail.getLastYearId()!=0){
                    detail.setDeleted(true);
                    fiveBudgetMaintainDetailMapper.updateByPrimaryKey(detail);
                }
            }
            //获取去年项目
            List<FiveBudgetMaintainDetailDto> lastYearDetails = getLastYearDetailById(model.getId(), dto.getBudgetYear());
            for(FiveBudgetMaintainDetailDto detailDto:lastYearDetails){
                FiveBudgetMaintainDetail lastDetail = new FiveBudgetMaintainDetail();
                lastDetail.setMaintainId(model.getId());
                lastDetail.setGmtModified(new Date());
                lastDetail.setGmtCreate(new Date());
                lastDetail.setDeleted(false);
                lastDetail.setBudgetMoney(MyStringUtil.moneyToString("0"));
                lastDetail.setLastYearMoney(detailDto.getBudgetMoney());
                lastDetail.setLastYearSuccess(MyStringUtil.moneyToString("0"));
                lastDetail.setLastYearSystemName(detailDto.getSystemName());
                lastDetail.setLastYearId(detailDto.getId());
                ModelUtil.setNotNullFields(lastDetail);
                fiveBudgetMaintainDetailMapper.insert(lastDetail);
            }
        }

        model.setBudgetYear(dto.getBudgetYear());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());
        fiveBudgetMaintainMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("processDescription",model.getTitle());
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveBudgetMaintainDto getModelById(int id){
        return getDto(fiveBudgetMaintainMapper.selectByPrimaryKey(id));
    }

    public FiveBudgetMaintainDto getDto(FiveBudgetMaintain item) {
        FiveBudgetMaintainDto dto=FiveBudgetMaintainDto.adapt(item);
        if(!Strings.isNullOrEmpty(item.getBudgetYear())){
            dto.setLastYear((Integer.valueOf(item.getBudgetYear())-1)+"");
        }
        CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId
                (), "", "");
        //MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        dto.setProcessName("已完成");
        dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }

        if(!item.getProcessEnd()&&customProcessInstance.isFinished()){
            dto.setProcessEnd(true);
            fiveBudgetMaintainMapper.updateByPrimaryKey(dto);
        }
        return dto;
    }
    public FiveBudgetMaintainDetailDto getDetailDto(FiveBudgetMaintainDetail item) {
        FiveBudgetMaintainDetailDto detailDto=FiveBudgetMaintainDetailDto.adapt(item);

        //计算剩余金额
        String totalDeductionMoney = MyStringUtil.moneyToString("0");
        //费用申请
        Map map1 = new HashMap();
        map1.put("deleted",false);
        map1.put("budgetType","fiveBudgetMaintain");
        map1.put("budgetId",detailDto.getId());
        List<FiveFinanceTransferAccountsDetail> transferAccountsDetails =
                fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
        for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
            totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
        }
        //借款
        Map map2 = new HashMap();
        map2.put("deleted",false);
        map2.put("budgetType","fiveBudgetMaintain");
        map2.put("budgetId",detailDto.getId());
        List<FiveFinanceLoanDetail> fiveFinanceLoanDetails = fiveFinanceLoanDetailMapper.selectAll(map2);
        for(FiveFinanceLoanDetail detail:fiveFinanceLoanDetails){
            totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
        }

        detailDto.setRemainMoney(MyStringUtil.getNewSubMoney(detailDto.getBudgetMoney(),totalDeductionMoney));

        //金额显示
        detailDto.setBudgetMoney(MyStringUtil.moneyToString(detailDto.getBudgetMoney(),6));
        detailDto.setLastYearMoney(MyStringUtil.moneyToString(detailDto.getLastYearMoney(),6));
        detailDto.setLastYearSuccess(MyStringUtil.moneyToString(detailDto.getLastYearSuccess(),6));

        return detailDto;
    }

    public int getNewModel(String userLogin,String uiSref){
        FiveBudgetMaintain item=new FiveBudgetMaintain();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";
        item.setBudgetYear(year);

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());

        ModelUtil.setNotNullFields(item);
        fiveBudgetMaintainMapper.insert(item);
        String processKey =EdConst.FIVE_BUDGET_MAINTAIN;
        String title ="专项维修经费预算："+item.getDeptName();
        if(uiSref.equalsIgnoreCase("budget.business")){//专项业务支出
            processKey= EdConst.FIVE_BUDGET_BUSINESS;
            title = "专项业务支出预算："+item.getDeptName();
        }
        if(uiSref.equalsIgnoreCase("budget.stock")){//股权投资预算
            processKey= EdConst.FIVE_BUDGET_STOCK;
            title = "股权投资预算："+item.getDeptName();
        }

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("otherDeptChargeMen", selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));

        variables.put("processDescription",title);
        String businessKey=processKey+ "_" + item.getId();
        item.setBusinessKey(businessKey);
        item.setTitle(title);
        String processInstanceId = taskHandleService.startProcess(processKey,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveBudgetMaintainMapper.updateByPrimaryKey(item);
        //获取去年项目
        List<FiveBudgetMaintainDetailDto> lastYearDetails = getLastYearDetailById(item.getId(), item.getBudgetYear());
        for(FiveBudgetMaintainDetailDto detailDto:lastYearDetails){
            FiveBudgetMaintainDetail newDetail = new FiveBudgetMaintainDetail();
            newDetail.setMaintainId(item.getId());
            newDetail.setGmtModified(new Date());
            newDetail.setGmtCreate(new Date());
            newDetail.setDeleted(false);
            newDetail.setBudgetMoney(MyStringUtil.moneyToString("0"));
            newDetail.setLastYearMoney(detailDto.getBudgetMoney());
            newDetail.setLastYearSuccess(MyStringUtil.moneyToString("0"));
            newDetail.setLastYearSystemName(detailDto.getSystemName());
            newDetail.setLastYearId(detailDto.getId());
            ModelUtil.setNotNullFields(newDetail);
            fiveBudgetMaintainDetailMapper.insert(newDetail);
        }
        return item.getId();
    }
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
/*         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
         if(uiSref.equalsIgnoreCase("budget.maintain")){
            params.put("qBusinessKey","fiveBudgetMaintain");
        }else if(uiSref.equalsIgnoreCase("budget.business")){
             params.put("qBusinessKey","fiveBudgetBusiness");
         } else if(uiSref.equalsIgnoreCase("budget.stock")){
             params.put("qBusinessKey","fiveBudgetStock");
         }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetMaintainMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetMaintain item=(FiveBudgetMaintain)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<FiveBudgetMaintainDetailDto> getLastYearDetailById(int id, String budgetYear) {
        List<FiveBudgetMaintainDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetMaintainDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        if(Strings.isNullOrEmpty(budgetYear)){
            map.put("budgetYear",dto.getLastYear());
        }else{
            map.put("budgetYear",Integer.valueOf(budgetYear)-1);
        }
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        map.put("deptId",dto.getDeptId());
        //排除自己
        List<FiveBudgetMaintain> fiveBudgetMaintains = fiveBudgetMaintainMapper.selectAll(map).stream().filter(p->p.getId()!=id)
                .sorted(Comparator.comparing(FiveBudgetMaintain::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetMaintains.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("maintainId",fiveBudgetMaintains.get(0).getId());
            List<FiveBudgetMaintainDetail> details = fiveBudgetMaintainDetailMapper.selectAll(map1);
            for(FiveBudgetMaintainDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }
    public void updateDetail(FiveBudgetMaintainDetail item){

        FiveBudgetMaintainDetail model = fiveBudgetMaintainDetailMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setSystemName(item.getSystemName());
        model.setProjectType(item.getProjectType());
        model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));
        model.setWorkContent(item.getWorkContent());
        model.setProjectContent(item.getProjectContent());
        model.setBudgetMoney(MyStringUtil.moneyToString(item.getBudgetMoney()));
        model.setRemark(item.getRemark());
        model.setProjectName(item.getProjectName());
        model.setLastYearSystemName(item.getLastYearSystemName());
        model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));
        fiveBudgetMaintainDetailMapper.updateByPrimaryKey(model);
    }

    public FiveBudgetMaintainDetail getNewModelDetail(int  id,String userLogin){
        FiveBudgetMaintainDetail item=new FiveBudgetMaintainDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setMaintainId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));

        ModelUtil.setNotNullFields(item);
        fiveBudgetMaintainDetailMapper.insert(item);
        return item;

    }

    public FiveBudgetMaintainDetail getModelDetailById(int id){
        return fiveBudgetMaintainDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveBudgetMaintainDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("maintainId",id);
        List<FiveBudgetMaintainDetail> list = fiveBudgetMaintainDetailMapper.selectAll(params);
        return list;
    }
    public List<FiveBudgetMaintainDetail> listLastYearDetail(int id){
        List<FiveBudgetMaintainDetail> list = new ArrayList<>();
        //查询去年预算
        FiveBudgetMaintainDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("budgetYear",dto.getLastYear());
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        List<FiveBudgetMaintain> fiveBudgetMaintains = fiveBudgetMaintainMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetMaintain::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetMaintains.size()>0) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("maintainId", fiveBudgetMaintains.get(0).getId());
            list = fiveBudgetMaintainDetailMapper.selectAll(params);
        }
        return list;
    }

    public void removeDetail(int id){
        FiveBudgetMaintainDetail model = fiveBudgetMaintainDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetMaintainDetailMapper.updateByPrimaryKey(model);
    }

    public Object getPrintData(int id) {
        return "";
    }
}
