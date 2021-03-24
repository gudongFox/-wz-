package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
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
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.BusinessIncomeMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.service.BusinessIncomeService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceProjectBudgetDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceProjectBudgetMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceProjectBudgetDetailDto;
import com.cmcu.mcc.finance.dto.FiveFinanceProjectBudgetDto;
import com.cmcu.mcc.finance.entity.FiveFinanceProjectBudget;
import com.cmcu.mcc.finance.entity.FiveFinanceProjectBudgetDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
public class FiveFinanceProjectBudgetService {

    @Resource
    FiveFinanceProjectBudgetMapper fiveFinanceProjectBudgetMapper;

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
    FiveFinanceProjectBudgetDetailMapper fiveFinanceProjectBudgetDetailMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Autowired
    ProcessQueryService processQueryService;


    public void remove(int id, String userLogin) {
        FiveFinanceProjectBudget item = fiveFinanceProjectBudgetMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //如果之前有合同库确认 删除关联
        if(item.getContractLibraryId()!=0){
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setProjectBudgetIds(MyStringUtil.getNewStringId(library.getProjectBudgetIds(),item.getId()));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceProjectBudgetDto dto) {
        FiveFinanceProjectBudget item = fiveFinanceProjectBudgetMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setContractName(dto.getContractName());
        item.setContractNo(dto.getContractNo());
        item.setProjectName(dto.getProjectName());
        item.setProjectType(dto.getProjectType());
        //计算总金额
        List<FiveFinanceProjectBudgetDetailDto> details = getDetailById(dto.getId());
        String budgetMoney ="" ;
        for(FiveFinanceProjectBudgetDetailDto detail:details){
            //排除父节点
            if(!detail.getIsParent()){
                budgetMoney = MyStringUtil.getNewAddMoney(budgetMoney,detail.getBudgetMoney());
            }
        }
        item.setBudgetTotalMoney(budgetMoney);
        //每项占比
        for(FiveFinanceProjectBudgetDetail detail:details){
            detail.setBudgetProportion(MyStringUtil.getNewDivideMoney(detail.getBudgetMoney(),item.getBudgetTotalMoney()));
            fiveFinanceProjectBudgetDetailMapper.updateByPrimaryKey(detail);
        }

        item.setDutyCost(dto.getDutyCost());
        //责任占比
        item.setDutyCostProportion(MyStringUtil.getNewDivideMoney(item.getDutyCost(),item.getBudgetTotalMoney()));
        item.setProfitProportion(dto.getProfitProportion());

        if(dto.getContractLibraryId()!=0){
            item.setContractLibraryId(dto.getContractLibraryId());
            //如果之前有合同库确认 删除关联
            if(item.getContractLibraryId()!=0){
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                library.setProjectBudgetIds(MyStringUtil.getNewStringId(library.getProjectBudgetIds(),item.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
            //关联合同库
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setProjectBudgetIds(library.getProjectBudgetIds()+","+item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "项目预算:"+item.getContractName());
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceProjectBudgetMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceProjectBudgetDto getModelById(int id) {
        return getDto(fiveFinanceProjectBudgetMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveFinanceProjectBudget item = new FiveFinanceProjectBudget();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        ModelUtil.setNotNullFields(item);

        fiveFinanceProjectBudgetMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_PROJECT_BUDGET;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "项目预算");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceProjectBudgetMapper.updateByPrimaryKey(item);
        //新增默认子表数据
        initDetail(item.getId());
        return item.getId();
    }

    private void initDetail(int projectBudgetId) {
        int parentId = insertDetail(projectBudgetId, 0, "一、项目直接成本",1);
        insertDetail(projectBudgetId,parentId,"1.劳务分包",1);
        insertDetail(projectBudgetId,parentId,"2.专业分包",2);
        insertDetail(projectBudgetId,parentId,"3.设备采购",3);
        insertDetail(projectBudgetId,parentId,"4.材料采购",4);
        insertDetail(projectBudgetId,parentId,"5.租赁",5);
        insertDetail(projectBudgetId,parentId,"6.其他",6);
        insertDetail(projectBudgetId, 0, "二、项目管理费用",2);
        insertDetail(projectBudgetId, 0, "三、财务成本",3);
        insertDetail(projectBudgetId, 0, "四、税金",4);
        insertDetail(projectBudgetId, 0, "五、预备费",5);
        insertDetail(projectBudgetId, 0, "六、利润",6);
    }
    public int insertDetail(int projectBudgetId,int parentId,String typeName,int seq){
        FiveFinanceProjectBudgetDetail item=new FiveFinanceProjectBudgetDetail();
        item.setProjectBudgetId(projectBudgetId);
        item.setTypeName(typeName);
        item.setParentId(parentId);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveFinanceProjectBudgetDetailMapper.insert(item);
        return item.getId();
    }


    public FiveFinanceProjectBudgetDto getDto(Object item) {
        FiveFinanceProjectBudget model= (FiveFinanceProjectBudget) item;
        FiveFinanceProjectBudgetDto dto=FiveFinanceProjectBudgetDto.adapt(model);
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
          //  MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveFinanceProjectBudgetMapper.updateByPrimaryKey(dto);
            }
        }

        return dto;
    }
    public FiveFinanceProjectBudgetDetailDto getDetailDto(Object item) {
        FiveFinanceProjectBudgetDetail model= (FiveFinanceProjectBudgetDetail) item;
        FiveFinanceProjectBudgetDetailDto detailDto=FiveFinanceProjectBudgetDetailDto.adapt(model);
        if(model.getParentId()!=0){
            FiveFinanceProjectBudgetDetail parent = fiveFinanceProjectBudgetDetailMapper.selectByPrimaryKey(model.getParentId());
            detailDto.setParentName(parent.getTypeName());
        }else{
            detailDto.setParentName("最外层显示");
        }
        detailDto.setIsParent(false);
        //判断是否有子节点 有则计算金额
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("projectBudgetId",detailDto.getProjectBudgetId());
        map.put("parentId",detailDto.getId());
        List<FiveFinanceProjectBudgetDetail> details = fiveFinanceProjectBudgetDetailMapper.selectAll(map);
        if(details.size()>0&&detailDto.getId()!=null){
            detailDto.setIsParent(true);
            String totalMoney =MyStringUtil.moneyToString("0");
            for(FiveFinanceProjectBudgetDetail detail:details){
                totalMoney = MyStringUtil.getNewAddMoney(totalMoney,detail.getBudgetMoney());
            }
            detailDto.setBudgetMoney(totalMoney);
            updateDetail(detailDto);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));
        return detailDto;
    }


    public List<FiveFinanceProjectBudget> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceProjectBudgetMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        params.put("ProjectBudgetCancelId",0);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceProjectBudgetMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceProjectBudget item=(FiveFinanceProjectBudget)p;
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

    public List<FiveFinanceProjectBudgetDetailDto> getDetailById(int id) {
        List<FiveFinanceProjectBudgetDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("projectBudgetId",id);
        List<FiveFinanceProjectBudgetDetail> details = fiveFinanceProjectBudgetDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveFinanceProjectBudgetDetail::getSeq)).collect(Collectors.toList());
        for(FiveFinanceProjectBudgetDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public void updateDetail(FiveFinanceProjectBudgetDetail item){
        if(item.getId()!=null){
            FiveFinanceProjectBudgetDetail model = fiveFinanceProjectBudgetDetailMapper.selectByPrimaryKey(item.getId());
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setPurchaseNo(item.getPurchaseNo());
            model.setBudgetMoney(item.getBudgetMoney());
            model.setBudgetProportion(item.getBudgetProportion());
            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveFinanceProjectBudgetDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveFinanceProjectBudgetDetail model =new FiveFinanceProjectBudgetDetail();
            model.setProjectBudgetId(item.getProjectBudgetId());
            model.setBusinessKey("");
            model.setParentId(item.getParentId());
            model.setTypeName(item.getTypeName());
            model.setPurchaseNo(item.getPurchaseNo());
            model.setBudgetMoney(item.getBudgetMoney());
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
            fiveFinanceProjectBudgetDetailMapper.insert(model);
        }

    }

    public FiveFinanceProjectBudgetDetailDto getNewModelDetail(int  id,int detailId,String userLogin){
        FiveFinanceProjectBudgetDetail item=new FiveFinanceProjectBudgetDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setProjectBudgetId(id);
        item.setParentId(detailId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        //fiveFinanceProjectBudgetDetailMapper.insert(item);
        return getDetailDto(item);
    }


    public FiveFinanceProjectBudgetDetail getModelDetailById(int id){
        return getDetailDto(fiveFinanceProjectBudgetDetailMapper.selectByPrimaryKey(id));
    }

    public void removeDetail(int id){
        FiveFinanceProjectBudgetDetail model = fiveFinanceProjectBudgetDetailMapper.selectByPrimaryKey(id);
        //判断是否有子节点
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("projectBudgetId",model.getProjectBudgetId());
        map.put("parentId",model.getId());
        List<FiveFinanceProjectBudgetDetail> details = fiveFinanceProjectBudgetDetailMapper.selectAll(map);
        Assert.state(details.size()==0,"根节点不能删除！");
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceProjectBudgetDetailMapper.updateByPrimaryKey(model);
        //如果存在父节点 跟新父节点信息
        if(model.getParentId()!=0){
            FiveFinanceProjectBudgetDetail parentDetail =fiveFinanceProjectBudgetDetailMapper.selectByPrimaryKey(model.getParentId());
            parentDetail.setBudgetMoney(MyStringUtil.getNewSubMoney(parentDetail.getBudgetMoney(),model.getBudgetMoney()));
            fiveFinanceProjectBudgetDetailMapper.updateByPrimaryKey(parentDetail);
        }
    }
}
