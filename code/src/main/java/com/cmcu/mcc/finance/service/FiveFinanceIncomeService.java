package com.cmcu.mcc.finance.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;

import com.cmcu.mcc.business.dao.BusinessIncomeMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;

import com.cmcu.mcc.business.entity.BusinessIncome;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceNodeMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeConfirmDto;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeDto;
import com.cmcu.mcc.finance.dto.FiveFinanceNodeDto;
import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceIncomeConfirm;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.entity.FiveFinanceNode;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
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
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveFinanceIncomeService extends BaseService {

    @Resource
    FiveFinanceIncomeMapper fiveFinanceIncomeMapper;

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
    @Autowired
    FiveFinanceNodeService fiveFinanceNodeService;
    @Autowired
    FiveFinanceNodeMapper fiveFinanceNodeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Resource
    FiveFinanceIncomeConfirmService fiveFinanceIncomeConfirmService;
    @Resource
    FiveFinanceInvoiceMapper fiveFinanceInvoiceMapper;
    @Resource
    BusinessIncomeMapper businessIncomeMapper;

    public void remove(int id, String userLogin) throws ParseException {
        FiveFinanceIncome item = fiveFinanceIncomeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //判断是否有票据申请
        Assert.state(fiveFinanceNodeService.getNodesByIncomeId(id).size() == 0, "该收入 存在处理的票据流程");
        //判断是否被认领
        Assert.state(MyStringUtil.getStringList(item.getConfirmIds()).size() == 0, "该收入 存在被认领流程");
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);
    }

    public void update(FiveFinanceIncomeDto dto) {
        FiveFinanceIncome item = fiveFinanceIncomeMapper.selectByPrimaryKey(dto.getId());
        item.setType(dto.getType());
        item.setSourceAccount(dto.getSourceAccount());
        item.setTargetAccount(dto.getTargetAccount());
        //保存万元存
        item.setMoney(MyStringUtil.getMoneyW(dto.getMoney()));
        item.setMoneyMax(dto.getMoneyMax());

/*        item.setMoneyCash(MyStringUtil.moneyToString(dto.getMoneyCash()));
        item.setMoneyCashMax(dto.getMoneyCashMax());

        item.setMoneyRemit(MyStringUtil.moneyToString(dto.getMoneyRemit()));
        item.setMoneyRemitMax(dto.getMoneyRemitMax());*/

        item.setSourceBank(dto.getSourceBank());
        item.setSourceName(dto.getSourceName());
        item.setTargetBank(dto.getTargetBank());
        item.setDescription(dto.getDescription());

        item.setIncomeTime(dto.getIncomeTime());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setDesignTargetMoney(MyStringUtil.getMoneyW(dto.getDesignTargetMoney()));
        item.setDesignSucessMoney(MyStringUtil.getMoneyW(dto.getDesignSucessMoney()));
        item.setDesignAskRate(dto.getDesignAskRate());
        item.setManagerRate(dto.getManagerRate());
        item.setTotalTargetMoney(MyStringUtil.getMoneyW(dto.getTotalTargetMoney()));
        item.setTotalSucessMoney(MyStringUtil.getMoneyW(dto.getTotalSucessMoney()));
        item.setTotalRate(dto.getTotalRate());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        /*Map variables=Maps.newHashMap();
        variables.put("processDescription", "收入管理:"+item.getSourceAccount());
        myActService.setVariables(item.getProcessInstanceId(),variables);*/
        BeanValidator.check(item);
        fiveFinanceIncomeMapper.updateByPrimaryKey(item);
    }


    public FiveFinanceIncomeDto getModelById(int id) throws ParseException {
        FiveFinanceIncomeDto dto = getDto(fiveFinanceIncomeMapper.selectByPrimaryKey(id));
        //0.000000显示改为''
        if (!Strings.isNullOrEmpty(dto.getMoneyRemit()) && Double.valueOf(dto.getMoneyRemit()).equals(0.0)) {
            dto.setMoneyRemit("");
        }
        if (!Strings.isNullOrEmpty(dto.getMoneyCash()) && Double.valueOf(dto.getMoneyCash()).equals(0.0)) {
            dto.setMoneyCash("");
        }
        return dto;
    }


    public int getNewModel(String userLogin, String uiSref) {

        FiveFinanceIncome item = new FiveFinanceIncome();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        item.setIncomeTime(simpleDateFormat.format(new Date()));

        item.setIncomeConfirmMoney(MyStringUtil.moneyToString("0", 8));
        item.setIncomeConfirmMoneyIng(MyStringUtil.moneyToString("0", 8));
        ModelUtil.setNotNullFields(item);
        fiveFinanceIncomeMapper.insert(item);

        String processKey = EdConst.FIVE_FINANCE_INCOME;
        String businessKey = processKey + "_" + item.getId();
/*        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "收入管理");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);*/
        item.setProcessInstanceId("0");
        item.setBusinessKey(businessKey);
        fiveFinanceIncomeMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public FiveFinanceIncomeDto getDto(Object item) {
        FiveFinanceIncome model = (FiveFinanceIncome) item;
        FiveFinanceIncomeDto dto = FiveFinanceIncomeDto.adapt(model);
        //如果有收入认领 添加认领人 同时判断是否确认
        if(MyStringUtil.getStringList(dto.getConfirmIds()).size()>0){
            FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(Integer.valueOf(MyStringUtil.getStringList(dto.getConfirmIds()).get(0)));
            dto.setConfirmCreatorName(incomeConfirm.getCreatorName());
            dto.setIsConfirmed(incomeConfirm.getProcessEnd());
        }
        //判断是否认领完
        if (StringUtils.isEmpty(dto.getMoney()) || dto.getMoney().equalsIgnoreCase
                (MyStringUtil.getNewAddMoney(dto.getIncomeConfirmMoney(), dto.getIncomeConfirmMoneyIng()))) {
            dto.setIsFullMoney(true);
        } else {
            dto.setIsFullMoney(false);
        }
        //金额显示为元
        dto.setMoney(MyStringUtil.getMoneyY(model.getMoney()));
/*      dto.setIncomeConfirmMoney(MyStringUtil.getMoneyY(model.getIncomeConfirmMoney()));
        dto.setIncomeConfirmMoneyIng(MyStringUtil.getMoneyY(model.getIncomeConfirmMoneyIng()));*/
        //获取票据信息
        if (dto.getType().contains("票据")) {
            List<FiveFinanceNodeDto> nodes = fiveFinanceNodeService.getNodesByIncomeId(dto.getId());
            String moneyNode = "0";
            for (FiveFinanceNode node : nodes) {
                moneyNode = MyStringUtil.getNewAddMoney(moneyNode, node.getMoney());
            }
            dto.setMoneyNode(moneyNode);
            dto.setMoneyNodeMax(NumberChangeUtils.moneyToChinese(Double.valueOf(dto.getMoneyNode())));
        }
        return dto;
    }


    public List<FiveFinanceIncome> selectAll(String userLogin, String uiSref) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, uiSref);
        params.put("deptIdList", deptIdList);
        return fiveFinanceIncomeMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
/*        Map map = new HashMap();
        map.put("myDeptData", true);
        map.put("uiSref", uiSref);
        map.put("enLogin", userLogin);
        params.putAll(getDefaultRightParams(map));*/

        //如果是 认领库 就查询已完成的 且未被认领
/*        if (uiSref.equals("finance.incomeLibrary")) {
            params.put("processEnd", true);
            params.put("isConfirm",true);
        }*/
        if (uiSref.equals("finance.incomeLibrary")) {
            params.put("isConfirm", "false");
            params.put("processEnd", true);
        }
        if(params.containsKey("processEnd")&&params.get("processEnd").equals("已确认")){
            params.put("processEnd",1);
        }else  if(params.containsKey("processEnd")&&params.get("processEnd").equals("未确认")){
            params.put("processEnd",0);
        }
        params.put("isLikeSelect","true");
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceIncomeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceIncome item = (FiveFinanceIncome) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    //获取 已认领的
    public PageInfo<Object> listPagedDataConfirmed(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
/*        Map map = new HashMap();
        map.put("myDeptData", true);
        map.put("uiSref", uiSref);
        map.put("enLogin", userLogin);
        params.putAll(getDefaultRightParams(map));*/

        //如果是 认领库 就查询已完成的 且未被认领
        params.put("processEnd", true);
        params.put("isConfirm","true");
        params.put("isLikeSelect","true");
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceIncomeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceIncome item = (FiveFinanceIncome) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 收入金额转中文大写
     *
     * @param incomeId
     * @return
     */
    public String moneyTurnCapital(int incomeId, String money) throws ParseException {
        FiveFinanceIncomeDto income = getModelById(incomeId);
        income.setMoney(MyStringUtil.getMoneyW(money));
        income.setMoneyMax(NumberChangeUtils.moneyToChinese(Double.valueOf(money)));
        fiveFinanceIncomeMapper.updateByPrimaryKey(income);
        return income.getMoneyMax();
    }

    public FiveFinanceIncomeConfirmDto getNewIncomeConfirm(int incomeId, String userLogin) {
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        FiveFinanceIncome fiveFinanceIncome = fiveFinanceIncomeMapper.selectByPrimaryKey(incomeId);

        FiveFinanceIncomeConfirm item = new FiveFinanceIncomeConfirm();
        item.setId(0);
        //收入信息
        item.setIncomeId(incomeId);
        item.setSourceAccount(fiveFinanceIncome.getSourceAccount());
        item.setTargetAccount(fiveFinanceIncome.getTargetAccount());
        item.setType(fiveFinanceIncome.getType());
        item.setIncomeTime(fiveFinanceIncome.getIncomeTime());
        item.setIncomeRemark(fiveFinanceIncome.getRemark());

        //认领金额 一次收入 一次认领完成
        item.setMoney(fiveFinanceIncome.getMoney());
        item.setMoneyMax(fiveFinanceIncome.getMoneyMax());

        item.setDeptId(hrEmployeeSysDto.getDeptId());
        item.setDeptName(hrEmployeeSysDto.getDeptName());

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        //fiveFinanceIncomeConfirmMapper.insert(item);

        String processKey = EdConst.FIVE_FINANCE_INCOME_CONFIRM;
        String businessKey = processKey + "_" + item.getId();
/*        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "收入管理");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);*/
        item.setBusinessKey(businessKey);
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(item);

        return fiveFinanceIncomeConfirmService.getDto(item);
    }

    //确认 取消收入
    public int getNotarizeIncome(int incomeId, String userLogin) {
        FiveFinanceIncome item = fiveFinanceIncomeMapper.selectByPrimaryKey(incomeId);
        //判断是否 有被认领
        Assert.state(MyStringUtil.getStringList(item.getConfirmIds()).size()==0,"该收入已被认领，无法取消确认！");
        if (item.getProcessEnd()) {
            item.setProcessEnd(false);
        } else {
            item.setProcessEnd(true);
        }
        fiveFinanceIncomeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public Object saveSelectNodes(int incomeId, String nodeIds) throws ParseException {
        List<Integer> nodeIdList = MyStringUtil.getIntList(nodeIds);
        for (int nodeId : nodeIdList) {
            FiveFinanceIncomeDto income = getModelById(incomeId);
            FiveFinanceNode node = fiveFinanceNodeMapper.selectByPrimaryKey(nodeId);
            node.setIncomeId(incomeId);
            fiveFinanceNodeMapper.updateByPrimaryKey(node);
            //计算收入金额
            income.setMoney(MyStringUtil.getNewAddMoney(income.getMoney(), node.getMoney()));
            income.setMoneyMax(NumberChangeUtils.moneyToChinese(Double.valueOf(income.getMoney())));
            update(income);
        }
        return "选择成功";
    }

    //导入数据
    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        int updateNum = 0;
        int insertNum = 0;

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            String targetAccount = map.getOrDefault(1, "").toString();
            String sourceAccount = map.getOrDefault(2, "").toString();
            String sourceName = map.getOrDefault(3, "").toString();
            String sourceBank = map.getOrDefault(4, "").toString();
            String type = map.getOrDefault(5, "").toString();
            String incomeTime = map.getOrDefault(6, "").toString();
            String money = map.getOrDefault(7, "").toString();
            String remark = map.getOrDefault(8, "").toString();

            if (Strings.isNullOrEmpty(targetAccount) || Strings.isNullOrEmpty(sourceAccount) || Strings.isNullOrEmpty(money)) {
                Assert.state(false, "第" + (i + 1) + "行存在空数据！");
            }

            FiveFinanceIncome item;
            Map map1 = new HashMap();
            map1.put("targetAccount", targetAccount);
            map1.put("sourceAccount", sourceAccount);
            map1.put("incomeTime", incomeTime);
            map1.put("money", MyStringUtil.getMoneyW(money));
            map1.put("processEnd", false);
            List<FiveFinanceIncome> incomes = fiveFinanceIncomeMapper.selectAll(map1);
            if (incomes.size() > 0) {
                item = incomes.get(0);
                item.setCreator(userLogin);
                item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
                item.setGmtCreate(new Date());
                item.setGmtModified(new Date());
                item.setDeleted(false);
                try {
                    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    item.setIncomeTime(incomeTime);
                } catch (Exception e) {
                    Assert.state(false, "第" + (i + 1) + "行日期格式存在问题！");
                }

                item.setTargetAccount(targetAccount);
                item.setSourceAccount(sourceAccount);
                item.setSourceName(sourceName);
                item.setSourceBank(sourceBank);
                item.setType(type);
                item.setMoney(MyStringUtil.getMoneyW(money));
                item.setMoneyMax(NumberChangeUtils.moneyToChinese(Double.valueOf(money)));

                item.setIncomeConfirmMoney(MyStringUtil.getMoneyW("0"));
                item.setIncomeConfirmMoneyIng(MyStringUtil.getMoneyW("0"));

                item.setRemark(remark);
                item.setDescription("系统导入");
                ModelUtil.setNotNullFields(item);
                fiveFinanceIncomeMapper.updateByPrimaryKey(item);
                updateNum++;
            } else {
                item = new FiveFinanceIncome();
                item.setCreator(userLogin);
                item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
                item.setGmtCreate(new Date());
                item.setGmtModified(new Date());
                item.setDeleted(false);
                try {
                    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    item.setIncomeTime(incomeTime);
                } catch (Exception e) {
                    Assert.state(false, "第" + (i + 1) + "行日期格式存在问题！");
                }

                item.setTargetAccount(targetAccount);
                item.setSourceAccount(sourceAccount);
                item.setSourceName(sourceName);
                item.setSourceBank(sourceBank);
                item.setType(type);
                item.setMoney(MyStringUtil.getMoneyW(money));
                item.setMoneyMax(NumberChangeUtils.moneyToChinese(Double.valueOf(money)));

                item.setIncomeConfirmMoney(MyStringUtil.getMoneyW("0"));
                item.setIncomeConfirmMoneyIng(MyStringUtil.getMoneyW("0"));

                item.setRemark(remark);
                item.setDescription("系统导入");
/*                item.setProcessInstanceId("0");
                item.setProcessEnd(true);*/
                ModelUtil.setNotNullFields(item);

                fiveFinanceIncomeMapper.insert(item);
                String processKey = EdConst.FIVE_FINANCE_INCOME;
                String businessKey = processKey + "_" + item.getId();
               /* Map variables = Maps.newHashMap();
                variables.put("userLogin", userLogin);
                variables.put("processDescription", "收入管理");
                String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
                item.setProcessInstanceId(processInstanceId);*/
                item.setProcessInstanceId("0");
                item.setBusinessKey(businessKey);
                fiveFinanceIncomeMapper.updateByPrimaryKey(item);
                insertNum++;
            }
        }

        return updateNum + "," + insertNum;
    }

    public List<Map> listMapData() {
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map map = new LinkedHashMap();
            map.put(" 接收账户 ", "");
            map.put(" 对方账户 ", "");
            map.put(" 对方账户名称 ", "");
            map.put(" 对方账户银行 ", "");
            map.put(" 收入类型 ", "");
            map.put(" 交易时间(请按照 2020-06-13 格式填写)", "");
            map.put(" 收入总金额（元） ", "");
            map.put(" 收入备注 ", "");
            list.add(map);
        }
        return list;
    }
    public List<Map> downIncome() {
        List<Map> list = new ArrayList<>();
        //查询所有收入数据
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        List<FiveFinanceIncome> fiveFinanceIncomes = fiveFinanceIncomeMapper.selectAll(params);

        for(FiveFinanceIncome income :fiveFinanceIncomes){
            Map map = new LinkedHashMap();
            map.put("收入类型", income.getType());
            map.put("接收账户", income.getTargetAccount());
            map.put("对方账户", income.getSourceAccount());
            map.put("对方账户名称", income.getSourceName());
            map.put("对方账户银行", income.getSourceBank());
            map.put("收入时间", income.getIncomeTime());
            map.put("收入总金额（元）", MyStringUtil.getMoneyY(income.getMoney()));
            map.put("收入金额大写", income.getMoneyMax());
            map.put("收入备注", income.getRemark());
            map.put("收入认领状态","");
            map.put("认领部门","");
            map.put("发票号","");
            map.put("合同号","");
            map.put("合同名称","");
            //收入认领状态
            List<Integer> confirmIds =MyStringUtil.getIntList(income.getConfirmIds());
            if(confirmIds.size()>0){
                FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(confirmIds.get(confirmIds.size() - 1));
                map.put("收入认领状态",incomeConfirm.getCreatorName()+" 已认领");
                map.put("认领部门",incomeConfirm.getDeptName());
                //发票信息
                List<Integer> invoiceIds =MyStringUtil.getIntList(incomeConfirm.getInvoiceIds());
                if(invoiceIds.size()>0){
                    FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(invoiceIds.get(invoiceIds.size() - 1));
                    map.put("发票号",invoice.getInvoiceNo());
                }else{
                    map.put("发票号","待补录发票");
                }
                //合同收费信息
                List<Integer> incomeIds =MyStringUtil.getIntList(incomeConfirm.getIncomeIds());
                if(incomeIds.size()>0){
                    BusinessIncome businessIncome = businessIncomeMapper.selectByPrimaryKey(incomeIds.get(incomeIds.size() - 1));
                    map.put("合同名称",businessIncome.getContractName());
                    map.put("合同号",businessIncome.getContractNo());
                }else{
                    map.put("合同名称","");
                    map.put("合同号","未关联合同");
                }
            }else{
                map.put("收入认领状态","未认领");
            }
            list.add(map);
        }
        return list;
    }
}
