package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceCollectionMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceInvoiceCollectionDto;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceCollection;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveFinanceInvoiceCollectionService extends BaseService {

    @Resource
    FiveFinanceInvoiceCollectionMapper fiveFinanceInvoiceCollectionMapper;

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
    @Resource
    FiveFinanceInvoiceMapper fiveFinanceInvoiceMapper;


    public void remove(int id, String userLogin) {
        FiveFinanceInvoiceCollection item = fiveFinanceInvoiceCollectionMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //如果关联发票 还原
        if(item.getInvoiceId()!=0){
            FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
            invoice.setInvoiceCollectionIds(MyStringUtil.getNewStringId(invoice.getInvoiceCollectionIds(),id));
            fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }


    public void update(FiveFinanceInvoiceCollectionDto dto) {
        FiveFinanceInvoiceCollection item = fiveFinanceInvoiceCollectionMapper.selectByPrimaryKey(dto.getId());


        //如果选择发票
        if(dto.getInvoiceId()!=0){
            //如果原来有发票 还原
            if(item.getInvoiceId()!=0){
                FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
                invoice.setInvoiceCollectionIds(MyStringUtil.getNewStringId(invoice.getInvoiceCollectionIds(),item.getId()));
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
            }
            FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(dto.getInvoiceId());
            invoice.setInvoiceCollectionIds(invoice.getInvoiceCollectionIds()+","+dto.getId());
            fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
        }
        item.setInvoiceId(dto.getInvoiceId());
        item.setInvoiceNo(dto.getInvoiceNo());
        item.setContractNo(dto.getContractNo());
        item.setContractName(dto.getContractName());
        item.setContractMoney(dto.getContractMoney());
        item.setInvoiceMoney(dto.getInvoiceMoney());
        item.setInvoiceGetMoney(dto.getInvoiceGetMoney());
        item.setInvoiceGetMoneyIng(dto.getInvoiceGetMoneyIng());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setRemindReceiveMan(dto.getRemindReceiveMan());
        item.setRemindReceiveManName(dto.getRemindReceiveManName());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "发票应收款催款:"+item.getInvoiceNo());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceInvoiceCollectionMapper.updateByPrimaryKey(item);
    }


    public FiveFinanceInvoiceCollectionDto getModelById(int id) {
        return getDto(fiveFinanceInvoiceCollectionMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin,String uiSref) {
        FiveFinanceInvoiceCollection item = new FiveFinanceInvoiceCollection();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveFinanceInvoiceCollectionMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_INVOICE_COLLECTION;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "发票应收款催款");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceInvoiceCollectionMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public FiveFinanceInvoiceCollectionDto getDto(Object item) {
        FiveFinanceInvoiceCollection model= (FiveFinanceInvoiceCollection) item;
        FiveFinanceInvoiceCollectionDto dto=FiveFinanceInvoiceCollectionDto.adapt(model);
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            //MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(model.getProcessInstanceId(),"");
            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                    (), "", "");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }

            if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveFinanceInvoiceCollectionMapper.updateByPrimaryKey(dto);
                //跟新合同库 发票开具金额
                if(model.getInvoiceId()!=0){
                    FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(model.getInvoiceId());
                    FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(invoice.getContractLibraryId());
                    BigDecimal bd =BigDecimal.valueOf(Double.valueOf(library.getContractInvoiceMoney())).subtract(BigDecimal.valueOf(Double.valueOf(invoice.getInvoiceMoney())));
                    library.setContractInvoiceMoney(bd.toString());
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                }
            }
        }
        return dto;
    }


    public List<FiveFinanceInvoiceCollection> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceInvoiceCollectionMapper.selectAll(params);
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceInvoiceCollectionMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceInvoiceCollection item=(FiveFinanceInvoiceCollection)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }





}
