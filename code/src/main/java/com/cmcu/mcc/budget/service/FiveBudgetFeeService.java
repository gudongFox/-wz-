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
import com.cmcu.mcc.budget.dao.FiveBudgetFeeDetailMapper;
import com.cmcu.mcc.budget.dao.FiveBudgetFeeMapper;
import com.cmcu.mcc.budget.dto.FiveBudgetFeeDetailDto;
import com.cmcu.mcc.budget.dto.FiveBudgetFeeDto;
import com.cmcu.mcc.budget.entity.FiveBudgetFee;
import com.cmcu.mcc.budget.entity.FiveBudgetFeeDetail;
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
public class FiveBudgetFeeService {

    @Resource
    FiveBudgetFeeMapper fiveBudgetFeeMapper;

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
        FiveBudgetFee item = fiveBudgetFeeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBudgetFeeDto dto) {
        FiveBudgetFee item = fiveBudgetFeeMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setTitle(dto.getTitle());

        //计算总金额
        List<FiveBudgetFeeDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveBudgetFeeDetailDto detail:details){
            //排除父节点
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setTotalBudgetMoney(budgetMoney);
        //获取去年的统计数据
        List<FiveBudgetFeeDetailDto> lastYearDetails = getLastYearDetailById(dto.getId(), dto.getBudgetYear());
        //每项占比
        for(FiveBudgetFeeDetail detail:details){
            //如果有预算年份有变化 子表预算项跟着改变
            if(!dto.getBudgetYear().equalsIgnoreCase(item.getBudgetYear())){
                detail.setTypeName(detail.getTypeName().replace(item.getBudgetYear(),dto.getBudgetYear()));
                detail.setLastYearMoney(MyStringUtil.moneyToString("0"));
                //匹配去年数据
                for(FiveBudgetFeeDetailDto detailDto:lastYearDetails){
                    //不是父节点 节点名字相同
                    if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(detail.getTypeName())){
                        detail.setLastYearMoney(detailDto.getBudgetMoney());
                    }
                }
            }
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getTotalBudgetMoney()));
            fiveBudgetFeeDetailMapper.updateByPrimaryKey(detail);
        }
        item.setBudgetYear(dto.getBudgetYear());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveBudgetFeeMapper.updateByPrimaryKey(item);
    }

    public FiveBudgetFeeDto getModelById(int id) {
        return getDto(fiveBudgetFeeMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBudgetFee item = new FiveBudgetFee();
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

        fiveBudgetFeeMapper.insert(item);

        String processKey=EdConst.FIVE_BUDGET_FEE;
        String title = "收费预算:"+item.getDeptName();

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
        fiveBudgetFeeMapper.updateByPrimaryKey(item);

        initDetail(item.getId());
        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =Integer.valueOf(sdf.format(new Date()))+1+"";

        //获取去年的统计数据
        List<FiveBudgetFeeDetailDto> lastYearDetails = getLastYearDetailById(projectBudgetId, year);
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
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"10)计算机应用研究所",50,10);
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"11)成品室",13,11);
      /*  insertDetail(lastYearDetails,projectBudgetId,parentId1,"12)公司办公室(董事会办公室）",59,12);
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
        insertDetail(lastYearDetails,projectBudgetId,parentId1,"24)物业开发",49,24);*/

        int parentId2 = insertDetail(lastYearDetails,projectBudgetId, parentId, "2.小计2",0,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"1)中兵勘察设计研究院有限公司",125,1);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"2)五环监理",45,2);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"3)五特公司",34,3);
        insertDetail(lastYearDetails,projectBudgetId,parentId2,"4)五洲中兴",12,4);
    }

    public int insertDetail(List<FiveBudgetFeeDetailDto> lastYearDetails,int projectBudgetId,int parentId,String typeName,int deptId,int seq){
        FiveBudgetFeeDetail item=new FiveBudgetFeeDetail();
        //匹配去年数据
        for(FiveBudgetFeeDetailDto detailDto:lastYearDetails){
            //不是父节点 节点名字相同
            if(!detailDto.getIsParent()&&detailDto.getTypeName().equalsIgnoreCase(typeName)){
                item.setLastYearMoney(detailDto.getBudgetMoney());
            }
        }
        item.setFeeId(projectBudgetId);
        item.setTypeName(typeName);
        item.setBudgetMoney(MyStringUtil.moneyToString("0"));
        item.setLastYearMoney(MyStringUtil.moneyToString("0"));
        item.setLastYearSuccess(MyStringUtil.moneyToString("0"));

        item.setOtherFee(MyStringUtil.moneyToString("0"));
        item.setProjectFee(MyStringUtil.moneyToString("0"));
        item.setDesignFee(MyStringUtil.moneyToString("0"));

        item.setParentId(parentId);
        item.setDeptId(deptId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveBudgetFeeDetailMapper.insert(item);
        return item.getId();
    }


    public FiveBudgetFeeDto getDto(Object item) {
        FiveBudgetFee model= (FiveBudgetFee) item;
        FiveBudgetFeeDto dto=FiveBudgetFeeDto.adapt(model);
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
                fiveBudgetFeeMapper.updateByPrimaryKey(dto);
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
            map1.put("budgetType","fiveBudgetFee");
            map1.put("budgetId",detailDto.getId());
            List<FiveFinanceTransferAccountsDetail> transferAccountsDetails = fiveFinanceTransferAccountsDetailMapper.selectAll(map1);
            for(FiveFinanceTransferAccountsDetail detail:transferAccountsDetails){
                totalDeductionMoney=MyStringUtil.getNewAddMoney(totalDeductionMoney,detail.getApplyMoney());
            }
            //借款
            Map map2 = new HashMap();
            map2.put("deleted",false);
            map2.put("budgetType","fiveBudgetFee");
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


    public List<FiveBudgetFee> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBudgetFeeMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect","true");
/*        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        if(!selectEmployeeService.getBudgetAdmins().contains(userLogin)){
            params.put("creator",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBudgetFeeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBudgetFee item=(FiveBudgetFee)p;
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

    public List<FiveBudgetFeeDetailDto> getDetailById(int id) {
        List<FiveBudgetFeeDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("feeId",id);
        List<FiveBudgetFeeDetail> details = fiveBudgetFeeDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveBudgetFeeDetail::getSeq)).collect(Collectors.toList());
        for(FiveBudgetFeeDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public void updateDetail(FiveBudgetFeeDetail item){
        if(item.getId()!=null){
            FiveBudgetFeeDetail model = fiveBudgetFeeDetailMapper.selectByPrimaryKey(item.getId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setDesignFee(MyStringUtil.moneyToString(item.getDesignFee()));
            model.setProjectFee(MyStringUtil.moneyToString(item.getProjectFee()));
            model.setOtherFee(MyStringUtil.moneyToString(item.getOtherFee()));

            //计算部门总预算
            List<String> total = new ArrayList();
            total.add(item.getDesignFee());
            total.add(item.getProjectFee());
            total.add(item.getOtherFee());
            model.setBudgetMoney(MyStringUtil.getNewTotalListMoney(total));
            model.setLastYearMoney(MyStringUtil.moneyToString(item.getLastYearMoney()));
            model.setLastYearSuccess(MyStringUtil.moneyToString(item.getLastYearSuccess()));

            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveBudgetFeeDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveBudgetFeeDetail model =new FiveBudgetFeeDetail();
            model.setFeeId(item.getFeeId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setDeptId(item.getDeptId());

            model.setDesignFee(MyStringUtil.moneyToString(item.getDesignFee()));
            model.setProjectFee(MyStringUtil.moneyToString(item.getProjectFee()));
            model.setOtherFee(MyStringUtil.moneyToString(item.getOtherFee()));

            //计算部门总预算
            List<String> total = new ArrayList();
            total.add(item.getDesignFee());
            total.add(item.getProjectFee());
            total.add(item.getOtherFee());
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
            fiveBudgetFeeDetailMapper.insert(model);
        }

    }

    public FiveBudgetFeeDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveBudgetFeeDetail item=new FiveBudgetFeeDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setFeeId(id);
        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveBudgetFeeDetailMapper.insert(item);
        return getDetailDto(item);
    }

    public FiveBudgetFeeDetail getModelDetailById(int id){
        FiveBudgetFeeDetailDto detailDto = getDetailDto(fiveBudgetFeeDetailMapper.selectByPrimaryKey(id));
        if(Double.valueOf(detailDto.getBudgetMoney()).equals(0.0)){
            detailDto.setBudgetMoney("");
        }
        if(Double.valueOf(detailDto.getDesignFee()).equals(0.0)){
            detailDto.setDesignFee("");
        }
        if(Double.valueOf(detailDto.getProjectFee()).equals(0.0)){
            detailDto.setProjectFee("");
        }
        if(Double.valueOf(detailDto.getOtherFee()).equals(0.0)){
            detailDto.setOtherFee("");
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
        FiveBudgetFeeDetail model = fiveBudgetFeeDetailMapper.selectByPrimaryKey(id);
        //判断是否有子节点
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("feeId",model.getFeeId());
        map.put("parentId",model.getId());
        List<FiveBudgetFeeDetail> details = fiveBudgetFeeDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"根节点不能删除！");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBudgetFeeDetailMapper.updateByPrimaryKey(model);
    }

    public List<FiveBudgetFeeDetailDto> getLastYearDetailById(int id,String budgetYear) {
        List<FiveBudgetFeeDetailDto> list =new ArrayList<>();
        //查询去年预算
        FiveBudgetFeeDto dto = getModelById(id);
        Map map = new HashMap();
        map.put("deleted",false);
        if(Strings.isNullOrEmpty(budgetYear)){
            map.put("budgetYear",dto.getLastYear());
        }else{
            map.put("budgetYear",Integer.valueOf(budgetYear)-1);
        }
        map.put("qBusinessKey",dto.getBusinessKey().split("_")[0]);
        //排除自己
        List<FiveBudgetFee> fiveBudgetFees = fiveBudgetFeeMapper.selectAll(map).stream().filter(p->p.getId()!=id)
                .sorted(Comparator.comparing(FiveBudgetFee::getGmtCreate).reversed()).collect(Collectors.toList());
        if(fiveBudgetFees.size()>0){
            Map map1 = new HashMap();
            map1.put("deleted",false);
            map1.put("feeId",fiveBudgetFees.get(0).getId());
            List<FiveBudgetFeeDetail> details = fiveBudgetFeeDetailMapper.selectAll(map1).stream()
                    .sorted(Comparator.comparing(FiveBudgetFeeDetail::getSeq)).collect(Collectors.toList());
            for(FiveBudgetFeeDetail detail:details){
                list.add(getDetailDto(detail));
            }
        }
        return list;
    }
}
