package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
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
import com.cmcu.mcc.business.service.BusinessIncomeService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceDeptBudgetDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceDeptBudgetMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceDeptBudgetDetailDto;
import com.cmcu.mcc.finance.dto.FiveFinanceDeptBudgetDto;
import com.cmcu.mcc.finance.entity.FiveFinanceDeptBudget;
import com.cmcu.mcc.finance.entity.FiveFinanceDeptBudgetDetail;
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
public class FiveFinanceDeptBudgetService extends BaseService {

    @Resource
    FiveFinanceDeptBudgetMapper fiveFinanceDeptBudgetMapper;

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
    FiveFinanceDeptBudgetDetailMapper fiveFinanceDeptBudgetDetailMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;

    public void remove(int id, String userLogin) {
        FiveFinanceDeptBudget item = fiveFinanceDeptBudgetMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceDeptBudgetDto dto) {
        FiveFinanceDeptBudget item = fiveFinanceDeptBudgetMapper.selectByPrimaryKey(dto.getId());
        //如果替换事业部 原子表内容删除
        if(item.getDeptId()!=dto.getDeptId()){
            List<FiveFinanceDeptBudgetDetailDto> details = getDetailById(dto.getId());
            for(FiveFinanceDeptBudgetDetailDto detailDto:details){
                if(detailDto.getDeptId()==item.getDeptId()){
                    detailDto.setDeleted(true);
                    fiveFinanceDeptBudgetDetailMapper.updateByPrimaryKey(detailDto);
                }
            }
            item.setDeptId(dto.getDeptId());
            item.setDeptName(dto.getDeptName());
        }
        item.setBudgetTotalMoney(dto.getBudgetTotalMoney());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        BeanValidator.check(item);
        fiveFinanceDeptBudgetMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceDeptBudgetDto getModelById(int id) {
        return getDto(fiveFinanceDeptBudgetMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveFinanceDeptBudget item = new FiveFinanceDeptBudget();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        item.setBudgetTime(sdf.format(new Date()));
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);

        item.setDeptId(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        item.setDeptName(selectEmployeeService.getHeadDeptName(hrEmployeeDto.getDeptId()));

        ModelUtil.setNotNullFields(item);
        fiveFinanceDeptBudgetMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_DEPT_BUDGET;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "事业部预算");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceDeptBudgetMapper.updateByPrimaryKey(item);
        //新增默认子表数据
        initDetail(item.getId());
        return item.getId();
    }

    private void initDetail(int deptBudgetId) {

    }

    public FiveFinanceDeptBudgetDto getDto(Object item) {
        FiveFinanceDeptBudget model= (FiveFinanceDeptBudget) item;
        FiveFinanceDeptBudgetDto dto=FiveFinanceDeptBudgetDto.adapt(model);
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
           // MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                    (), "", "");

            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }
            //dto.setBusinessKey(processInstanceDto.getBusinessKey());
            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveFinanceDeptBudgetMapper.updateByPrimaryKey(dto);
            }
        }
        //计算总金额
        String totalMoney =MyStringUtil.moneyToString("0");
        List<FiveFinanceDeptBudgetDetailDto> details = getDetailById(dto.getId());
        for(FiveFinanceDeptBudgetDetailDto detailDto:details){
            totalMoney = MyStringUtil.getNewAddMoney(totalMoney,detailDto.getBudgetMoney());
        }
        dto.setBudgetTotalMoney(totalMoney);
        update(dto);
        return dto;
    }

    public FiveFinanceDeptBudgetDetailDto getDetailDto(Object item) {
        FiveFinanceDeptBudgetDetail model= (FiveFinanceDeptBudgetDetail) item;
        FiveFinanceDeptBudgetDetailDto detailDto=FiveFinanceDeptBudgetDetailDto.adapt(model);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));
        return detailDto;
    }

    public List<FiveFinanceDeptBudget> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceDeptBudgetMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceDeptBudgetMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceDeptBudget item=(FiveFinanceDeptBudget)p;
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

    public List<FiveFinanceDeptBudgetDetailDto> getDetailById(int id) {
        List<FiveFinanceDeptBudgetDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("deptBudgetId",id);
        List<FiveFinanceDeptBudgetDetail> details = fiveFinanceDeptBudgetDetailMapper.selectAll(map).stream()
                .sorted(Comparator.comparing(FiveFinanceDeptBudgetDetail::getSeq)).collect(Collectors.toList());
        for(FiveFinanceDeptBudgetDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }
    public void updateDetail(FiveFinanceDeptBudgetDetail item,int deptBudgetId){
        if(item.getId()!=null){
            FiveFinanceDeptBudgetDetail model = fiveFinanceDeptBudgetDetailMapper.selectByPrimaryKey(item.getId());
            model.setLastYearRemainMoney(item.getLastYearRemainMoney());
            model.setBudgetMoney(MyStringUtil.moneyToString(item.getBudgetMoney()));
            model.setTravelMoney(item.getTravelMoney());
            model.setCarMoney(item.getCarMoney());
            model.setMaterialsMoney(item.getMaterialsMoney());
            model.setDeptId(item.getDeptId());
            model.setDeptName(item.getDeptName());
            model.setSeq(item.getSeq());
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveFinanceDeptBudgetDetailMapper.updateByPrimaryKey(model);
        }else{
            FiveFinanceDeptBudgetDetail model =new FiveFinanceDeptBudgetDetail();
            model.setLastYearRemainMoney(item.getLastYearRemainMoney());
            model.setBudgetMoney(item.getBudgetMoney());
            model.setTravelMoney(item.getTravelMoney());
            model.setCarMoney(item.getCarMoney());
            model.setMaterialsMoney(item.getMaterialsMoney());
            model.setDeptId(item.getDeptId());
            model.setDeptName(item.getDeptName());
            model.setSeq(item.getSeq());
            model.setDeleted(false);
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            model.setGmtCreate(new Date());
            model.setCreator(item.getCreator());
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
            model.setCreatorName(hrEmployeeDto.getUserName());
            model.setDeptBudgetId(deptBudgetId);
            ModelUtil.setNotNullFields(model);
            fiveFinanceDeptBudgetDetailMapper.insert(model);
        }
    }
    public FiveFinanceDeptBudgetDetailDto getNewModelDetail(int  id,String userLogin){
        FiveFinanceDeptBudgetDetail item=new FiveFinanceDeptBudgetDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setDeptBudgetId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        //fiveFinanceDeptBudgetDetailMapper.insert(item);
        return getDetailDto(item);
    }
    public FiveFinanceDeptBudgetDetail getModelDetailById(int id){
        return getDetailDto(fiveFinanceDeptBudgetDetailMapper.selectByPrimaryKey(id));
    }
    public void removeDetail(int id){
        FiveFinanceDeptBudgetDetail model = fiveFinanceDeptBudgetDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceDeptBudgetDetailMapper.updateByPrimaryKey(model);
    }
}
