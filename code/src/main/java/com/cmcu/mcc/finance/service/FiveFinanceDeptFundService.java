package com.cmcu.mcc.finance.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.act.dto.UserTaskDto;
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

import com.cmcu.mcc.finance.dao.*;

import com.cmcu.mcc.finance.dto.FiveFinanceDeptFundDetailDto;
import com.cmcu.mcc.finance.dto.FiveFinanceDeptFundDto;
import com.cmcu.mcc.finance.entity.*;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.xml.internal.ws.api.addressing.OneWayFeature;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class FiveFinanceDeptFundService extends BaseService {

    @Resource
    FiveFinanceDeptFundMapper fiveFinanceDeptFundMapper;

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

    @Resource
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Resource
    FiveFinanceTravelExpenseMapper fiveFinanceTravelExpenseMapper;
    @Autowired
    FiveFinanceDeptBudgetMapper fiveFinanceDeptBudgetMapper;
    @Autowired
    FiveFinanceDeptBudgetDetailMapper fiveFinanceDeptBudgetDetailMapper;



    public void remove(int id, String userLogin) {
        FiveFinanceDeptFund item = fiveFinanceDeptFundMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
    }

    public FiveFinanceDeptFundDto getModelById(int id) {
        return getDto(fiveFinanceDeptFundMapper.selectByPrimaryKey(id));
    }
    public FiveFinanceDeptFundDto getModelByDeptId(int deptId) {
        Map map =new HashMap();
        map.put("deleted",false);
        map.put("deptId",deptId);
        List<FiveFinanceDeptFund> fiveFinanceDeptFunds = fiveFinanceDeptFundMapper.selectAll(map);
        if(fiveFinanceDeptFunds.size()>0){
            return getDto(fiveFinanceDeptFunds.get(0));
        }else{
            return new FiveFinanceDeptFundDto();
        }

    }
    //根据部门表 创建部门资金
    public void refreshDept () {
        List<HrDeptDto> hrDeptDtos = hrDeptService.selectAll();
        Map map =new HashMap();
        map.put("deleted",false);
        List<FiveFinanceDeptFund> deptFunds = fiveFinanceDeptFundMapper.selectAll(map);
        for(HrDeptDto hrDeptDto:hrDeptDtos){
            boolean isExist = false;
            for(FiveFinanceDeptFund deptFund:deptFunds){
                if(hrDeptDto.getId().equals(deptFund.getDeptId())){
                    isExist=true;
                }
            }
            //如果不存在 添加部门资金
            if(!isExist){
                FiveFinanceDeptFund deptFund =new FiveFinanceDeptFund();
                deptFund.setCreator("sys");
                deptFund.setCreatorName("系统创建");
                deptFund.setDeptId(hrDeptDto.getId());
                deptFund.setDeptName(hrDeptDto.getName());
                deptFund.setGmtCreate(new Date());
                deptFund.setGmtModified(new Date());
                deptFund.setDeleted(false);
                ModelUtil.setNotNullFields(deptFund);
                fiveFinanceDeptFundMapper.insert(deptFund);
            }
        }
    }
    //刷新部门资金
    public void refreshMoney (int deptId) {
        FiveFinanceDeptFundDto deptFund = getModelByDeptId(deptId);
        //计算总收入
        Map map =new HashMap();
        map.put("deleted",false);
        map.put("deptId",deptId);
       // map.put("processEnd",true);
        List<FiveFinanceIncomeConfirm> incomeConfirms = fiveFinanceIncomeConfirmMapper.selectAll(map);
        String totalMoney =MyStringUtil.moneyToString("0");
        for(FiveFinanceIncomeConfirm incomeConfirm:incomeConfirms){
            totalMoney =MyStringUtil.getNewAddMoney(totalMoney,incomeConfirm.getMoney());
        }
        deptFund.setTotalMoney(totalMoney);
        //计算已支出
        deptFund.setSubMoney(MyStringUtil.moneyToString("10"));

        //计算已正在支出 流程未完结
        deptFund.setSubMoneyIng(MyStringUtil.moneyToString("20"));
        //网络运维中心有支出
        deptFund.setSubMoneyTotal(MyStringUtil.getNewAddMoney(deptFund.getSubMoney(),deptFund.getSubMoneyIng()));

        deptFund.setGmtModified(new Date());
        fiveFinanceDeptFundMapper.updateByPrimaryKey(deptFund);
    }

    public FiveFinanceDeptFundDto getDto(Object item) {
        FiveFinanceDeptFund model= (FiveFinanceDeptFund) item;
        FiveFinanceDeptFundDto dto=FiveFinanceDeptFundDto.adapt(model);
        //计算剩余金额 总使用金额
        dto.setSubMoneyTotal(MyStringUtil.getNewAddMoney(model.getSubMoney(),model.getSubMoneyIng()));
        dto.setNowMoney(MyStringUtil.getNewSubMoney(dto.getTotalMoney(),dto.getSubMoneyTotal()));
        //查询部门预算 1.判断是否为事业部
        if(selectEmployeeService.getHeadDeptId(dto.getDeptId())==dto.getDeptId()){
            Map map =Maps.newHashMap();
            map.put("deleted",false);
            map.put("deptId",dto.getDeptId());
            List<FiveFinanceDeptBudget> deptBudgets = fiveFinanceDeptBudgetMapper.selectAll(map).stream()
                    .sorted(Comparator.comparing(FiveFinanceDeptBudget::getGmtCreate).reversed()).collect(Collectors.toList());
            if(deptBudgets.size()>0){
                dto.setDeptBudgetId(deptBudgets.get(0).getId());
                dto.setDeptBudgetMoney(deptBudgets.get(0).getBudgetTotalMoney());
            }
        }else{
            Map map =Maps.newHashMap();
            map.put("deleted",false);
            map.put("deptId",selectEmployeeService.getHeadDeptId(dto.getDeptId()));
            List<FiveFinanceDeptBudget> deptBudgets = fiveFinanceDeptBudgetMapper.selectAll(map).stream()
                    .sorted(Comparator.comparing(FiveFinanceDeptBudget::getGmtCreate).reversed()).collect(Collectors.toList());
            if(deptBudgets.size()>0){
                Map detailMap = new HashMap();
                detailMap.put("deleted",false);
                detailMap.put("deptBudgetId",deptBudgets.get(0).getId());
                detailMap.put("deptId",dto.getDeptId());
                List<FiveFinanceDeptBudgetDetail> details = fiveFinanceDeptBudgetDetailMapper.selectAll(detailMap);
                if(details.size()>0){
                    dto.setDeptBudgetId(deptBudgets.get(0).getId());
                    dto.setDeptBudgetMoney(details.get(0).getBudgetMoney());
                }
            }
        }

        //使用信息
        List<FiveFinanceDeptFundDetailDto> detailDtos =new ArrayList<>();

        FiveFinanceDeptFundDetailDto detailDto =new FiveFinanceDeptFundDetailDto();

        detailDto.setCreator("xxin");
        detailDto.setGmtModified(new Date());
        detailDto.setType(model.getDeptName()+"-差旅费");
        detailDto.setUrl("finance.travelExpenseDetail");
        detailDto.setUrlIdName("travelExpenseId");
        detailDto.setUrlId(3);
        detailDto.setProcessName("已完成");
        detailDto.setMoney("1000.000000");


        detailDtos.add(detailDto);

        dto.setDetails(detailDtos);
        return dto;
    }


    public List<FiveFinanceDeptFund> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceDeptFundMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceDeptFundMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceDeptFund item=(FiveFinanceDeptFund)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
