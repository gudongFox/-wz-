package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonCodeMapper;
import com.cmcu.common.dto.CommonCodeDto;
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
import com.cmcu.mcc.finance.dao.FiveFinanceStampTaxDetailMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceStampTaxMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceStampTaxDetailDto;
import com.cmcu.mcc.finance.dto.FiveFinanceStampTaxDto;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTaxDetail;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveFinanceStampTaxService {

    @Resource
    FiveFinanceStampTaxMapper fiveFinanceStampTaxMapper;

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
    FiveFinanceStampTaxDetailMapper fiveFinanceStampTaxDetailMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Resource
    CommonCodeMapper commonCodeMapper;
    @Resource
    JdbcTemplate jdbcTemplate;
    @Autowired
    ProcessQueryService processQueryService;


    public void remove(int id, String userLogin) {
        FiveFinanceStampTax item = fiveFinanceStampTaxMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        //跟新 合同 印花税状态
        if(item.getContractId()!=0){
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractId());
            library.setStampTaxId(0);
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceStampTaxDto dto) {
        FiveFinanceStampTax item = fiveFinanceStampTaxMapper.selectByPrimaryKey(dto.getId());
        //如果选择合同
        if(dto.getContractId()!=0){
            //之前选择还原
            if(item.getContractId()!=0){
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractId());
                library.setStampTaxId(0);
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            }
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getContractId());
            library.setStampTaxId(item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        item.setContractId(dto.getContractId());
        item.setContractNo(dto.getContractNo());
        item.setContractName(dto.getContractName());
        item.setProjectName(dto.getProjectName());
        item.setProjectNo(dto.getProjectNo());
        item.setCustomerName(dto.getCustomerName());
        item.setSignTime(dto.getSignTime());
        item.setContractMoney(dto.getContractMoney());
        item.setTaxType(dto.getTaxType());

        item.setContractDeptId(dto.getContractDeptId());
        item.setContractDeptName(dto.getContractDeptName());

        //计算税率和金额
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"合同印花税税目");
        for(CommonCodeDto codeDto:commonCodes){
            if(codeDto.getName().equalsIgnoreCase(item.getTaxType())){
                if(codeDto.getName().equalsIgnoreCase("其他")&&StringUtils.isNotEmpty(dto.getTaxNum())){
                    item.setTaxNum(dto.getTaxNum());
                }else{
                    item.setTaxNum(codeDto.getCode());
                }
                item.setStampTaxMoney(MyStringUtil.getNewMultiplyMoney1(item.getContractMoney(),item.getTaxNum()));
            }
        }
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        BeanValidator.check(item);
        fiveFinanceStampTaxMapper.updateByPrimaryKey(item);
    }

    public FiveFinanceStampTaxDto getModelById(int id) {
        return getDto(fiveFinanceStampTaxMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveFinanceStampTax item = new FiveFinanceStampTax();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        item.setTaxType("勘察设计");
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveFinanceStampTaxMapper.insert(item);

        String processKey=EdConst.FIVE_FINANCE_STAMPTAX;
        String businessKey=processKey+"_"+item.getId();
/*        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "印花税申报");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);*/
        item.setBusinessKey(businessKey);
        fiveFinanceStampTaxMapper.updateByPrimaryKey(item);
        //新增默认子表数据
       // initDetail(item.getId());
        return item.getId();
    }

    private void initDetail(int StampTaxId) {

    }

    public FiveFinanceStampTaxDto getDto(Object item) {
        FiveFinanceStampTax model= (FiveFinanceStampTax) item;
        FiveFinanceStampTaxDto dto=FiveFinanceStampTaxDto.adapt(model);
        dto.setProcessName("已完成");
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
                fiveFinanceStampTaxMapper.updateByPrimaryKey(dto);
            }
        }
        update(dto);
        return dto;
    }

    public FiveFinanceStampTaxDetailDto getDetailDto(Object item) {
        FiveFinanceStampTaxDetail model= (FiveFinanceStampTaxDetail) item;
        FiveFinanceStampTaxDetailDto detailDto=FiveFinanceStampTaxDetailDto.adapt(model);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailDto.setGmtModifiedDto(sdf.format(model.getGmtModified()));
        return detailDto;
    }

    public List<FiveFinanceStampTax> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceStampTaxMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceStampTaxMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceStampTax item=(FiveFinanceStampTax)p;
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

    public List<FiveFinanceStampTaxDetailDto> getDetailById(int id) {
        List<FiveFinanceStampTaxDetailDto> list =new ArrayList<>();
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("stampTaxId",id);
        List<FiveFinanceStampTaxDetail> details = fiveFinanceStampTaxDetailMapper.selectAll(map);
        for(FiveFinanceStampTaxDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }
    public void updateDetail(FiveFinanceStampTaxDetail item,int StampTaxId){
        FiveFinanceStampTaxDetail model;
        if(item.getId()!=null){
            model = fiveFinanceStampTaxDetailMapper.selectByPrimaryKey(item.getId());
            model.setContractId(item.getContractId());
            model.setContractNo(item.getContractNo());
            model.setContractName(item.getContractName());
            model.setProjectName(item.getProjectName());
            model.setProjectNo(item.getProjectNo());
            model.setCustomerName(item.getCustomerName());
            model.setSignTime(item.getSignTime());
            model.setContractMoney(item.getContractMoney());
            model.setTaxType(item.getTaxType());
            //计算税率和金额
            List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"合同印花税税目");
            for(CommonCodeDto dto:commonCodes){
                if(dto.getName().equalsIgnoreCase(item.getTaxType())){
                    model.setTaxNum(dto.getCode());
                    model.setStampTaxMoney(MyStringUtil.getNewMultiplyMoney1(model.getContractMoney(),dto.getCode()));
                }
            }
            model.setRemark(item.getRemark());
            model.setGmtModified(new Date());
            fiveFinanceStampTaxDetailMapper.updateByPrimaryKey(model);
        }else{
            model =new FiveFinanceStampTaxDetail();
            model.setContractId(item.getContractId());
            model.setContractNo(item.getContractNo());
            model.setContractName(item.getContractName());
            model.setProjectName(item.getProjectName());
            model.setProjectNo(item.getProjectNo());
            model.setCustomerName(item.getCustomerName());
            model.setSignTime(item.getSignTime());
            model.setContractMoney(item.getContractMoney());
            model.setTaxType(item.getTaxType());
            //计算税率和金额
            List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"合同印花税税目");
            for(CommonCodeDto dto:commonCodes){
                if(dto.getName().equalsIgnoreCase(item.getTaxType())){
                    model.setTaxNum(dto.getCode());
                    model.setStampTaxMoney(MyStringUtil.getNewMultiplyMoney1(model.getContractMoney(),dto.getCode()));
                }
            }
            model.setRemark(item.getRemark());

            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getCreator());
            model.setCreatorName(hrEmployeeDto.getUserName());
            model.setStampTaxId(StampTaxId);
            model.setGmtModified(new Date());
            model.setGmtCreate(new Date());
            model.setDeleted(false);
            ModelUtil.setNotNullFields(model);
            fiveFinanceStampTaxDetailMapper.insert(model);
            item.setId(model.getId());
        }
        //跟新 合同 印花税状态
        if(item.getContractId()!=0){
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractId());
            library.setStampTaxId(item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
    }
    public FiveFinanceStampTaxDetailDto getNewModelDetail(int  id,String userLogin){
        FiveFinanceStampTaxDetail item=new FiveFinanceStampTaxDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setTaxType("勘察设计");
        //计算税率和金额
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"合同印花税税目");
        for(CommonCodeDto dto:commonCodes){
            if(dto.getName().equalsIgnoreCase(item.getTaxType())){
                item.setTaxNum(dto.getCode());
            }
        }
        item.setStampTaxId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        //fiveFinanceStampTaxDetailMapper.insert(item);
        return getDetailDto(item);
    }
    public FiveFinanceStampTaxDetail getModelDetailById(int id){
        return getDetailDto(fiveFinanceStampTaxDetailMapper.selectByPrimaryKey(id));
    }
    public void removeDetail(int id){
        FiveFinanceStampTaxDetail model = fiveFinanceStampTaxDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveFinanceStampTaxDetailMapper.updateByPrimaryKey(model);
        //跟新 合同 印花税状态
        if(model.getContractId()!=0){
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getContractId());
            library.setStampTaxId(0);
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
    }

    public List<Map> listMapData(Map params) {
        List<Map> list = Lists.newArrayList();

        String  sql ="SELECT contract_name as '合同名称',contract_no as '合同号',project_name as'项目名称',project_no as'项目号',customer_name as'客户名称',sign_time as'签订时间',contract_money as '合同金额',\n" +
                "tax_type as'印花税-税目',tax_num as'印花税-税率',stamp_tax_money as'印花税额（万元）',gmt_create as'创建时间',creator_name as'创建人'" +
                " FROM db_wuzhou.five_finance_stamp_tax_detail where stamp_tax_id ='"+params.get("stampTaxId")+"' and is_deleted =0";
        try {
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(sql);
            list.addAll(oList);
        } catch (Exception ex) {

        }
        return  list;
    }

    public void changeOpenStamp(int id,String userLogin){
        FiveFinanceStampTax model = fiveFinanceStampTaxMapper.selectByPrimaryKey(id);
        if(model.getOpenStamp()){
            model.setOpenStamp(false);
        }else {
            model.setOpenStamp(true);
        }
        fiveFinanceStampTaxMapper.updateByPrimaryKey(model);
    }

    public List<Map> downData(String userLogin) {
        List<Map> list = Lists.newArrayList();

        String  sql ="SELECT contract_name as'合同名称',contract_no as'合同号',customer_name as'客户名称',project_name as'项目名称'," +
                "project_no as'项目号',sign_time as'签订时间',tax_type as '税目',contract_money as'合同金额',tax_num as'税率（%）'\n" +
                " ,stamp_tax_money as'印花税额度',contract_dept_name as'合同签订部门' FROM db_wuzhou.five_finance_stamp_tax where is_deleted =0";

        try {
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(sql);
            list.addAll(oList);
        } catch (Exception ex) {

        }
        return list;
    }
}
