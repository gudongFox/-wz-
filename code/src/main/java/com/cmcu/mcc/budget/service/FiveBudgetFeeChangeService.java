package com.cmcu.mcc.budget.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.NumberChangeUtils;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.budget.dao.FiveBudgetFeeChangeMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetFeeDetailMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetFeeChangeDto;
import com.cmcu.mcc.budget.dto.FiveBudgetFeeDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetIndependentDetailDto;
import com.cmcu.mcc.budget.entity.FiveBudgetFeeChange;
import com.cmcu.mcc.budget.entity.FiveBudgetFeeDetail;
import com.cmcu.mcc.budget.entity.FiveBudgetIndependent;
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
public class FiveBudgetFeeChangeService {

    @Resource
    FiveBudgetFeeChangeMapper fiveBudgetFeeChangeMapper;

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
    CommonConfigService sysConfigService;
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
    FiveBudgetFeeDetailMapper fiveBudgetFeeDetailMapper;
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
        FiveBudgetFeeChange item = fiveBudgetFeeChangeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetFeeChangeDto dto) {
        FiveBudgetFeeChange item = fiveBudgetFeeChangeMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());
        item.setChangeContent(dto.getChangeContent());  //保存变更信息
        item.setTotalBudgetMoney(dto.getTotalBudgetMoney());
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        item.setProjectName(dto.getProjectName());
        item.setProjectNo(dto.getProjectNo());
        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetFeeChangeMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetFeeChangeDto getModelById(int id) {
        return getDto(fiveBudgetFeeChangeMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetFeeChange item = new FiveBudgetFeeChange();
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
        item.setProjectNo("0");

        ModelUtil.setNotNullFields(item);

        fiveBudgetFeeChangeMapper.insert(item);

        String processKey=EdConst.FIVE_BUDGET_FEE_Change;
        String title = "收费预算更改:"+item.getDeptName();

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
        fiveBudgetFeeChangeMapper.updateByPrimaryKey(item);

        return item.getId();
    }



    public FiveBudgetFeeChangeDto getDto(Object item) {
        FiveBudgetFeeChange model= (FiveBudgetFeeChange) item;
        FiveBudgetFeeChangeDto dto=FiveBudgetFeeChangeDto.adapt(model);
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
           // dto.setBusinessKey(processInstanceDto.getBusinessKey());
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBudgetFeeChangeMapper.updateByPrimaryKey(dto);
            }
        }
        return dto;
    }
    public FiveBudgetFeeDetailDto getDetailDto(Object item) {
        FiveBudgetFeeDetail model= (FiveBudgetFeeDetail) item;
        FiveBudgetFeeDetailDto detailDto=FiveBudgetFeeDetailDto.adapt(model);
        if(model.getParentId()!=0){
            FiveBudgetFeeDetail parent = fiveBudgetFeeDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("最外层显示");
        }
        detailDto.setIsParent(false);
        //判断是否有子节点 有则计算金额
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("feeId",detailDto.getFeeId());
        map.put("parentId",detailDto.getId());
        List<FiveBudgetFeeDetail> details = fiveBudgetFeeDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);

            String totalBudgetMoney = MyStringUtil.moneyToString("0");
            String totalDesignFee = MyStringUtil.moneyToString("0");
            String totalProjectFee = MyStringUtil.moneyToString("0");
            String totalOtherFee = MyStringUtil.moneyToString("0");
            String totalLastYearMoney = MyStringUtil.moneyToString("0");
            String totalLastYearSuccess = MyStringUtil.moneyToString("0");

            for(FiveBudgetFeeDetail detail:details){
                totalBudgetMoney = MyStringUtil.getNewAddMoney(totalBudgetMoney,detail.getBudgetMoney());
                totalDesignFee = MyStringUtil.getNewAddMoney(totalDesignFee,detail.getDesignFee());
                totalProjectFee = MyStringUtil.getNewAddMoney(totalProjectFee,detail.getProjectFee());
                totalOtherFee = MyStringUtil.getNewAddMoney(totalOtherFee,detail.getOtherFee());
                totalLastYearMoney = MyStringUtil.getNewAddMoney(totalLastYearMoney,detail.getLastYearMoney());
                totalLastYearSuccess = MyStringUtil.getNewAddMoney(totalLastYearSuccess,detail.getLastYearSuccess());
            }

            detailDto.setBudgetMoney(totalBudgetMoney);
            detailDto.setDesignFee(totalDesignFee);
            detailDto.setProjectFee(totalProjectFee);
            detailDto.setOtherFee(totalOtherFee);
            detailDto.setLastYearMoney(totalLastYearMoney);
            detailDto.setLastYearSuccess(totalLastYearSuccess);

            fiveBudgetFeeDetailMapper.updateByPrimaryKey(detailDto);
        }else{
            //计算剩余金额
            String totalDeductionMoney = MyStringUtil.moneyToString("0");
            //费用申请
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("budgetType","fiveBudgetFeeChange");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails = fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //借款
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetFeeChange");
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
        detailDto.setLastYearMoney(MyStringUtil.moneyToString(detailDto.getLastYearMoney(),6));
        detailDto.setLastYearSuccess(MyStringUtil.moneyToString(detailDto.getLastYearSuccess(),6));
        detailDto.setBudgetMoney(MyStringUtil.moneyToString(detailDto.getBudgetMoney(),6));
        detailDto.setRemainMoney(MyStringUtil.moneyToString(detailDto.getRemainMoney(),6));
        detailDto.setDesignFee(MyStringUtil.moneyToString(detailDto.getDesignFee(),6));
        detailDto.setProjectFee(MyStringUtil.moneyToString(detailDto.getProjectFee(),6));
        detailDto.setOtherFee(MyStringUtil.moneyToString(detailDto.getOtherFee(),6));
        return detailDto;
    }


    public List<FiveBudgetFeeChange> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetFeeChangeMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect","true");
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetFeeChangeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetFeeChange item=(FiveBudgetFeeChange) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
