package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
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
import com.cmcu.mcc.business.dto.BusinessIncomeDto;
import com.cmcu.mcc.business.entity.BusinessIncome;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.service.BusinessIncomeService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceCollectionMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeConfirmDto;
import com.cmcu.mcc.finance.dto.FiveFinanceInvoiceDto;
import com.cmcu.mcc.finance.entity.*;
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
public class FiveFinanceInvoiceService extends BaseService {

    @Resource
    FiveFinanceInvoiceMapper fiveFinanceInvoiceMapper;

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
    FiveFinanceInvoiceDetailMapper fiveFinanceInvoiceDetailMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Autowired
    FiveFinanceInvoiceCollectionMapper fiveFinanceInvoiceCollectionMapper;

    public void remove(int id, String userLogin) {
        FiveFinanceInvoice item = fiveFinanceInvoiceMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //如果有收入确认 不允许删除
        Assert.state(MyStringUtil.getIntList(item.getIncomeConfirmIds()).size()>=0, "该发票已经在确认中使用!");
        //如果有合同库确认 删除关联
        if(item.getContractLibraryId()!=0){
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setInvoiceIds(MyStringUtil.getNewStringId(library.getInvoiceIds(),id));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void removeReplenish(int id, String userLogin) {
        FiveFinanceInvoice item = fiveFinanceInvoiceMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //如果有合同库确认 删除关联
        if(item.getContractLibraryId()!=0){
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setInvoiceIds(MyStringUtil.getNewStringId(library.getInvoiceIds(),id));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        //还原合同收费关联
        BusinessIncome businessIncome = businessIncomeMapper.selectByPrimaryKey(item.getIncomeId());
        businessIncome.setInvoiceReplenishId(0);
        businessIncomeMapper.updateByPrimaryKey(businessIncome);
        //还原收入确认
        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(businessIncome.getIncomeConfirmId());
        incomeConfirm.setInvoiceIds(MyStringUtil.getNewStringId(incomeConfirm.getInvoiceIds(),item.getId()));
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);


        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void removeInIncomeConfirm(int id, int incomeConfirmId,String userLogin) {
        FiveFinanceInvoice item = fiveFinanceInvoiceMapper.selectByPrimaryKey(id);
        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);
        //如果流程已完成 删除创建的合同收费
        if(item.getProcessEnd()){
            List<Integer> incomeIds = MyStringUtil.getIntList(incomeConfirm.getIncomeIds());
            for(int incomeId :incomeIds){
                BusinessIncome businessIncome = businessIncomeMapper.selectByPrimaryKey(incomeId);
                if(businessIncome.getInvoiceId().equals(id)){
                    //如果由改发票创建的合同收费已完结 则不能删除
                    Assert.state(!businessIncome.getProcessEnd(), "该发票创建的合同收费已完结");
                    businessIncomeService.remove(incomeId,businessIncome.getCreator());
                }
            }
        }else{
            //取消发票与收入确认关联
            item.setIncomeConfirmIds(MyStringUtil.getNewStringId(item.getIncomeConfirmIds(),incomeConfirmId));
            item.setIncomeConfirmId(0);
            fiveFinanceInvoiceMapper.updateByPrimaryKey(item);
            //取消收入确认与发票关联
            incomeConfirm.setInvoiceIds(MyStringUtil.getNewStringId(incomeConfirm.getInvoiceIds(),id));
            fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);

            handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
        }
    }

    public void update(FiveFinanceInvoiceDto dto) {
        FiveFinanceInvoice item = fiveFinanceInvoiceMapper.selectByPrimaryKey(dto.getId());

        item.setIncomeConfirmIds(dto.getIncomeConfirmIds());
        item.setApplyMan(dto.getApplyMan());
        item.setApplyManName(dto.getApplyManName());
        item.setUserTel(dto.getUserTel());
        item.setInvoiceTime(dto.getInvoiceTime());
        item.setContractCustomer(dto.getContractCustomer());
        item.setLocalInvoiceType(dto.getLocalInvoiceType());
        item.setOtherInvoiceType(dto.getOtherInvoiceType());
        item.setInvoiceHeadName(dto.getInvoiceHeadName());
        item.setTaxNo(dto.getTaxNo());
        item.setCustomerAdderss(dto.getCustomerAdderss());
        item.setCustomerTel(dto.getCustomerTel());
        item.setCustomerTaxCode(dto.getCustomerTaxCode());
        item.setBank(dto.getBank());
        item.setBankAccount(dto.getBankAccount());
        item.setReceiveTime(dto.getReceiveTime());
        item.setRemindReceiveMan(dto.getRemindReceiveMan());
        item.setRemindReceiveManName(dto.getRemindReceiveManName());
        item.setDeptChargeMan(dto.getDeptChargeMan());
        item.setFinanceConfirmTime(dto.getFinanceConfirmTime());
        item.setInvoiceRemark(dto.getInvoiceRemark());

        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());

        item.setLegalDept(dto.getLegalDept());
        item.setDeptChargeMan(dto.getDeptChargeMan());
        item.setDeptChargeManName(dto.getDeptChargeManName());
        if(dto.getContractLibraryId()!=0){
            item.setContractLibraryId(dto.getContractLibraryId());
            //如果之前有合同库确认 删除关联
            if(item.getContractLibraryId()!=0){
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                library.setInvoiceIds(MyStringUtil.getNewStringId(library.getInvoiceIds(),item.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
            //关联合同库
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setInvoiceIds(library.getInvoiceIds()+","+item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }

        item.setContractNo(dto.getContractNo());
        item.setContractName(dto.getContractName());
        item.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney()));
        item.setContractGetInvoice(dto.getContractGetInvoice());
        item.setReceiveType(dto.getReceiveType());
        item.setInvoiceMoney(MyStringUtil.moneyToString(dto.getInvoiceMoney()));
        item.setChargeAgainstPreMoney(MyStringUtil.moneyToString(dto.getChargeAgainstPreMoney()));
        item.setSaleMoney(MyStringUtil.moneyToString(dto.getSaleMoney()));
        item.setOutPutTaxMoney(MyStringUtil.moneyToString(dto.getOutPutTaxMoney()));
        item.setInvoiceNo(dto.getInvoiceNo());
        item.setPromiseRemittanceNo(dto.getPromiseRemittanceNo());
        item.setNoteMoney(MyStringUtil.moneyToString(dto.getNoteMoney()));
        item.setIncomeStage(dto.getIncomeStage());
        item.setIncomeBuild(dto.getIncomeBuild());


        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "发票申请:"+item.getContractCustomer());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceInvoiceMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceInvoiceDto getModelById(int id) {
        return getDto(fiveFinanceInvoiceMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveFinanceInvoice item = new FiveFinanceInvoice();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setApplyMan(userLogin);
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setUserTel(hrEmployeeDto.getMobile());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setLocalInvoiceType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"发票类型").toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        item.setReceiveTime(simpleDateFormat.format(new Date()));
        item.setInvoiceTime(simpleDateFormat.format(new Date()));
        item.setFinanceConfirmTime(simpleDateFormat.format(new Date()));
        ModelUtil.setNotNullFields(item);

        fiveFinanceInvoiceMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_INVOICE;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "发票申请");
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),3,true));//发起者部门领导 正副职
        variables.put("copyMen",MyStringUtil.listToString(selectEmployeeService.getDeptFinanceMan(item.getDeptId())));//抄送各单位财务
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceInvoiceMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public FiveFinanceInvoiceDto getDto(Object item) {
        FiveFinanceInvoice model= (FiveFinanceInvoice) item;
        FiveFinanceInvoiceDto dto=FiveFinanceInvoiceDto.adapt(model);
        if(model.getContractLibraryId()!=0){
            FiveBusinessContractLibrary library =fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getContractLibraryId());
            dto.setPreContractMoney(MyStringUtil.moneyToString(library.getPreContractMoney()));
            dto.setShouldContractMoney(MyStringUtil.moneyToString(library.getShouldContractMoney()));
        }
        //如果发票已收金额为空 设置为0
        if(StringUtils.isEmpty(dto.getInvoiceGetMoney())){
            dto.setInvoiceGetMoney(MyStringUtil.moneyToString(""));
        }
        if(StringUtils.isEmpty(dto.getPreContractMoney())){
            dto.setPreContractMoney(MyStringUtil.moneyToString(""));
        }
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
                fiveFinanceInvoiceMapper.updateByPrimaryKey(dto);
                //是否为补领发票
                if(dto.getIncomeId()!=0){
                    //增加 合同开票额
                    FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getContractLibraryId());
                    library.setContractInvoiceMoney(MyStringUtil.getNewAddMoney(library.getContractInvoiceMoney(),dto.getInvoiceMoney()));
                    //减少 预收款额
                    library.setPreContractMoney(MyStringUtil.getNewSubMoney(library.getPreContractMoney(),dto.getInvoiceMoney()));
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                }else{
                    //如果是收入确认中创建 新增合同收费
                    if(model.getIncomeConfirmId()!=0){
                        int businessIncomeId = businessIncomeService.getNewModelByConfirm(model.getIncomeConfirmId(),model.getId(), model.getCreator());
                        BusinessIncomeDto businessIncomeDto = businessIncomeService.getModelById(businessIncomeId);
                        businessIncomeDto.setInvoiceId(model.getId());
                        businessIncomeDto.setContractLibraryId(model.getContractLibraryId());
                        businessIncomeDto.setContractName(model.getContractName());
                        businessIncomeDto.setContractNo(model.getContractNo());
                        businessIncomeDto.setInvoiceNo(model.getInvoiceNo());
                        businessIncomeDto.setLegalDept(model.getLegalDept());

                        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getContractLibraryId());
                        businessIncomeDto.setProjectNo(library.getProjectNo());
                        businessIncomeDto.setProjectName(library.getProjectName());
                        businessIncomeDto.setContractIncomeMoney(MyStringUtil.moneyToString(library.getContractIncomeMoney(),8));
                        businessIncomeDto.setContractMoney(MyStringUtil.moneyToString(library.getContractMoney(),8));
                        businessIncomeMapper.updateByPrimaryKey(businessIncomeDto);
                        //跟新 发票正在收款金额
                        model.setInvoiceGetMoneyIng(MyStringUtil.getNewAddMoney(model.getInvoiceGetMoneyIng(),businessIncomeDto.getIncomeMoney()));
                        fiveFinanceInvoiceMapper.updateByPrimaryKey(model);
                    }
                    //流程完成 添加合同开具发票金额
                    if(model.getContractLibraryId()!=0){
                        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getContractLibraryId());
                        library.setContractInvoiceMoney(MyStringUtil.getNewAddMoney(library.getContractInvoiceMoney(),model.getInvoiceMoney()));
                        fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                    }
                }

            }
        }
        //万元 显示小数点后6位数
        dto.setPreContractMoney(MyStringUtil.moneyToString(dto.getPreContractMoney(),6));
        dto.setShouldContractMoney(MyStringUtil.moneyToString(dto.getShouldContractMoney(),6));
        dto.setInvoiceMoney(MyStringUtil.moneyToString(dto.getInvoiceMoney(),6));
        dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(),6));
        return dto;
    }

    public List<FiveFinanceInvoice> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceInvoiceMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        params.put("invoiceCancelId",0);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceInvoiceMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceInvoice item=(FiveFinanceInvoice)p;
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

    public List<FiveFinanceInvoice> getInvoicesByIncomeConfirmId(int incomeConfirmId) {
        List<FiveFinanceInvoice> list =new ArrayList<>();
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("incomeConfirmId",incomeConfirmId);
        List<FiveFinanceInvoice> fiveFinanceInvoices = fiveFinanceInvoiceMapper.selectAll(params);
        for(FiveFinanceInvoice fiveFinanceInvoice:fiveFinanceInvoices){
            if(MyStringUtil.getIntList(fiveFinanceInvoice.getIncomeConfirmIds()).contains(incomeConfirmId)){
                list.add(getDto(fiveFinanceInvoice));
            }
        }
        return  list;
    }

    //收入确认中 新增发票
    public int getNewModelByConfirm(int incomeConfirmId, String userLogin, String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveFinanceInvoice item = new FiveFinanceInvoice();

        item.setIncomeConfirmId(incomeConfirmId);
        item.setIncomeConfirmIds(incomeConfirmId+",");

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setApplyMan(userLogin);
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setUserTel(hrEmployeeDto.getMobile());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        ModelUtil.setNotNullFields(item);

        fiveFinanceInvoiceMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_INVOICE;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "发票申请");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        //关联收入确认
        FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(incomeConfirmId);
        item.setInvoiceMoney(incomeConfirm.getMoney());
        item.setBusinessKey(businessKey);
        fiveFinanceInvoiceMapper.updateByPrimaryKey(item);


        incomeConfirm.setInvoiceIds(incomeConfirm.getInvoiceIds()+","+item.getId());
        fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        return item.getId();
    }

    //发票可以多次认领
    public List<FiveFinanceInvoice> getInvoicesWithoutConfirm(int incomeConfirmId, String userLogin, String uiSref) {
        List<FiveFinanceInvoice> list = new ArrayList<>();

       /* for(int invoiceId:invoiceIds){
            list.add(getModelById(invoiceId));
        }*/
        //添加未被冲抵的发票  未被报废的发票
        Map map =new HashMap();
        map.put("deleted",false);
        map.put("processEnd",true);
        List<FiveFinanceInvoice> fiveFinanceInvoices =fiveFinanceInvoiceMapper.selectAll(map).stream()
                .filter(p->p.getInvoiceCancelId()==0).collect(Collectors.toList());
        for(FiveFinanceInvoice invoice:fiveFinanceInvoices){
            //判断是否已经使用完 或申请完
            if(!invoice.getInvoiceGetMoney().equalsIgnoreCase(invoice.getInvoiceMoney())){
                list.add(invoice);
            }
        }

        return list;
    }

    public List<FiveFinanceIncomeConfirmDto> selectIncomeConfirm(String incomeConfirmIds) {
        List<FiveFinanceIncomeConfirmDto> list = new ArrayList<>();

        List<Integer> incomeConfirmIdList = MyStringUtil.getIntList(incomeConfirmIds);
        for(int incomeConfirm:incomeConfirmIdList){
            list.add( fiveFinanceIncomeConfirmService.getModelById(incomeConfirm));
        }
        return list;
    }

    /**
     * 查询所有发票申请信息
     * @return
     */
    public List<FiveFinanceInvoiceDto> listInvoice(int invoiceId){
        List<FiveFinanceInvoiceDto> relist=Lists.newArrayList();
        //如果已选中 添加
        if(invoiceId!=0){
            FiveFinanceInvoiceDto invoiceDto = getModelById(invoiceId);
            relist.add(invoiceDto);
        }
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        List<FiveFinanceInvoice> list = fiveFinanceInvoiceMapper.selectAll(params);
        for(FiveFinanceInvoice invoice:list){
            //排除已认领的发票
            if(MyStringUtil.getIntList(invoice.getIncomeConfirmIds()).size()==0){
                //排除已报销
                if(invoice.getInvoiceCancelId()==0){
                    relist.add(getDto(invoice));
                }
            }
        }

        return relist;
    }
    /**
     * 查询所有未收款完成
     * @return
     */
    public List<FiveFinanceInvoice> listInvoiceByCollection(int invoiceId){
        List<FiveFinanceInvoice> list = new ArrayList<>();
        //如果当前已选中
        if(invoiceId!=0){
            list.add(getModelById(invoiceId));
        }
        //添加未被冲抵的发票  未被报废的发票
        Map map =new HashMap();
        map.put("deleted",false);
        map.put("processEnd",true);
        List<FiveFinanceInvoice> fiveFinanceInvoices =fiveFinanceInvoiceMapper.selectAll(map).stream()
                .filter(p->p.getInvoiceCancelId()==0).collect(Collectors.toList());
        for(FiveFinanceInvoice invoice:fiveFinanceInvoices){
            //判断是否已经使用完 或申请完
            if(!invoice.getInvoiceGetMoney().equalsIgnoreCase(invoice.getInvoiceMoney())&&invoice.getId()!=invoiceId){
                list.add(invoice);
            }
        }

        return list;
    }

    public int  replenishInvoiceByIncome(int incomeId,String userLogin) {
        BusinessIncome businessIncome = businessIncomeMapper.selectByPrimaryKey(incomeId);
        //判断流程是否结束
        Assert.state(businessIncome.getProcessEnd(),"合同收费流程尚未结束！");
        //创建新的发票
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveFinanceInvoice item = new FiveFinanceInvoice();
        item.setIncomeId(businessIncome.getId());
        //同步合同信息
        item.setContractLibraryId(businessIncome.getContractLibraryId());
        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
        item.setContractNo(library.getContractNo());
        item.setContractName(library.getContractName());
        item.setContractMoney(MyStringUtil.moneyToString(library.getContractMoney()));
        item.setContractGetInvoice(library.getContractInvoiceMoney());
        item.setInvoiceMoney(MyStringUtil.moneyToString(businessIncome.getIncomeMoney()));
        item.setInvoiceGetMoney(MyStringUtil.moneyToString(businessIncome.getIncomeMoney()));
        item.setChargeAgainstPreMoney(MyStringUtil.moneyToString(businessIncome.getIncomeMoney()));
        //发票关联收入确认
        item.setIncomeConfirmId(businessIncome.getIncomeConfirmId());
        item.setIncomeConfirmIds(","+businessIncome.getIncomeConfirmId());

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setApplyMan(userLogin);
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setUserTel(hrEmployeeDto.getMobile());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setLocalInvoiceType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"发票类型").toString());

        ModelUtil.setNotNullFields(item);

        fiveFinanceInvoiceMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_INVOICE;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "发票补领申请");
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),3,true));//发起者部门领导 正副职
        variables.put("copyMen",MyStringUtil.listToString(selectEmployeeService.getDeptFinanceMan(item.getDeptId())));//抄送各单位财务
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceInvoiceMapper.updateByPrimaryKey(item);

        businessIncome.setInvoiceReplenishId(item.getId());
        businessIncomeMapper.updateByPrimaryKey(businessIncome);

        //收入确认关联发票 排除导入
        if(businessIncome.getIncomeConfirmId()!=-1){
            FiveFinanceIncomeConfirm incomeConfirm = fiveFinanceIncomeConfirmMapper.selectByPrimaryKey(businessIncome.getIncomeConfirmId());
            incomeConfirm.setInvoiceIds(incomeConfirm.getInvoiceIds()+","+item.getId());
            fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(incomeConfirm);
        }
        return item.getId();
    }
}
