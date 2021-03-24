package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.NumberChangeUtils;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceCancelMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceInvoiceCancelDto;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceCancel;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveFinanceInvoiceCancelService extends BaseService {

    @Resource
    FiveFinanceInvoiceCancelMapper fiveFinanceInvoiceCancelMapper;

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
        FiveFinanceInvoiceCancel item = fiveFinanceInvoiceCancelMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //如果关联发票 还原
        if(item.getInvoiceId()!=0){
            FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
            invoice.setInvoiceCancelId(0);
            fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }


    public void update(FiveFinanceInvoiceCancelDto dto) {
        FiveFinanceInvoiceCancel item = fiveFinanceInvoiceCancelMapper.selectByPrimaryKey(dto.getId());
        item.setLegalDept(dto.getLegalDept());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setApplyMan(dto.getApplyMan());
        item.setApplyManName(dto.getApplyManName());
        item.setApplyManTel(dto.getApplyManTel());
        item.setApplyTime(dto.getApplyTime());
        item.setApplyReason(dto.getApplyReason());

        //如果选择发票
        if(dto.getInvoiceId()!=0){
            //如果原来有发票 还原
            if(item.getInvoiceId()!=0){
                FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(item.getInvoiceId());
                invoice.setInvoiceCancelId(0);
                fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
            }
            FiveFinanceInvoice invoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(dto.getInvoiceId());
            invoice.setInvoiceCancelId(dto.getId());
            fiveFinanceInvoiceMapper.updateByPrimaryKey(invoice);
        }
        item.setInvoiceId(dto.getInvoiceId());
        item.setContractName(dto.getContractName());
        item.setContractNo(dto.getContractNo());
        item.setInvoiceNo(dto.getInvoiceNo());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        Map variables=Maps.newHashMap();
        variables.put("processDescription", "发票作废申请:"+item.getApplyMan());
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        fiveFinanceInvoiceCancelMapper.updateByPrimaryKey(item);
    }


    public FiveFinanceInvoiceCancelDto getModelById(int id) {
        return getDto(fiveFinanceInvoiceCancelMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin,String uiSref) {
        FiveFinanceInvoiceCancel item = new FiveFinanceInvoiceCancel();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setApplyMan(userLogin);
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setApplyManTel(hrEmployeeDto.getMobile());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        item.setApplyTime(simpleDateFormat.format(new Date()));
        ModelUtil.setNotNullFields(item);
        fiveFinanceInvoiceCancelMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_INVOICE_CANCEL;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "发票作废申请");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceInvoiceCancelMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public FiveFinanceInvoiceCancelDto getDto(Object item) {
        FiveFinanceInvoiceCancel model= (FiveFinanceInvoiceCancel) item;
        FiveFinanceInvoiceCancelDto dto=FiveFinanceInvoiceCancelDto.adapt(model);
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
                fiveFinanceInvoiceCancelMapper.updateByPrimaryKey(dto);
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


    public List<FiveFinanceInvoiceCancel> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceInvoiceCancelMapper.selectAll(params);
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceInvoiceCancelMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceInvoiceCancel item=(FiveFinanceInvoiceCancel)p;
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

    public void saveInvoice(int invoiceId, int id) {
        FiveFinanceInvoice invoice=fiveFinanceInvoiceMapper.selectByPrimaryKey(invoiceId);
        FiveFinanceInvoiceCancel invoiceCancel=fiveFinanceInvoiceCancelMapper.selectByPrimaryKey(id);
        invoiceCancel.setLegalDept(invoice.getLegalDept());
        invoiceCancel.setContractName(invoice.getContractName());
        invoiceCancel.setInvoiceNo(invoice.getInvoiceNo());
        fiveFinanceInvoiceCancelMapper.updateByPrimaryKey(invoiceCancel);
    }

}
