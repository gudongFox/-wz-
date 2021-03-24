package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.BusinessSupplierDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessSupplierService extends BaseService {
    @Resource
    BusinessSupplierMapper businessSupplierMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    BusinessSupplierAptitudeMapper businessSupplierAptitudeMapper;
    @Autowired
    BusinessRecordMapper businessRecordMapper;
    @Autowired
    BusinessPreContractMapper businessPreContractMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Autowired
    BusinessSubpackageMapper businessSubpackageMapper;
    @Autowired
    HandleFormService handleFormService;
    @Resource
    BusinessSupplierUsedNameMapper businessSupplierUsedNameMapper;

    public void remove(int id, String userLogin) {
        BusinessSupplier model = businessSupplierMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin), "该数据是用户" + model.getCreatorName() + "创建");

        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);

    }

    public void update(BusinessSupplierDto dto) {

        //验证纳税人识别号是否存在,没有填写不做判断，点击纳税人识别号的时候验证
        if (!Strings.isNullOrEmpty(dto.getTaxNo())){
            checkTaxNo(dto.getTaxNo(),dto.getId());
        }

        //验证供方名称
        checkSupplier(dto.getName(),dto.getId());
        /*//验证供方名称是否存在,没有填写不做判断，点击供方名称的时候验证
        if (!Strings.isNullOrEmpty(dto.getName())){
            checkSupplier(dto.getName(),dto.getId());
        }*/
        BusinessSupplier model = businessSupplierMapper.selectByPrimaryKey(dto.getId());

        //如果是变更 增加曾用名
        if(model.getProcessEnd()&&!model.getName().equals(dto.getName())){
            int newUsedNameId = getNewUsedName(dto.getOperateUserLogin(), dto.getId());
            BusinessSupplierUsedName usedName = getUsedNameById(newUsedNameId);
            usedName.setOldName(model.getName());
            usedName.setName(dto.getName());
            usedName.setRemark("供方变更流程创建");
            updateUsedName(usedName);
        }

        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setCode(dto.getCode());
        model.setName(dto.getName());
        model.setInType(dto.getInType());
        model.setAddress(dto.getAddress());
        model.setCreditCode(dto.getCreditCode());
        model.setLegalPerson(dto.getLegalPerson());
        model.setLegalPersonTel(dto.getLegalPersonTel());
        model.setLinkMan(dto.getLinkMan());
        model.setLinkTitle(dto.getLinkTitle());
        model.setLinkTel(dto.getLinkTel());
        model.setLinkMail(dto.getLinkMail());
        model.setDepositBank(dto.getDepositBank());
        model.setBankAccount(dto.getBankAccount());
        model.setTaxNo(dto.getTaxNo());
        model.setTaxType(dto.getTaxType());
        model.setBusinessScope(dto.getBusinessScope());
        model.setSupplierType(dto.getSupplierType());
        model.setRegisteredFund(dto.getRegisteredFund());
        model.setGmtModified(new Date());

        model.setSystemInName(dto.getSystemInName());

        Map variables = Maps.newHashMap();
        variables.put("processDescription", "供方信息录入:"+model.getName());
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        businessSupplierMapper.updateByPrimaryKey(model);
    }

    public BusinessSupplierDto getModelById(int id) {
        return getDto(businessSupplierMapper.selectByPrimaryKey(id));
    }

    public BusinessSupplierDto getDto(BusinessSupplier item) {
        BusinessSupplierDto dto = BusinessSupplierDto.adapt(item);
        dto.setProcessName("已完成");
        if(!dto.getProcessEnd() && StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                businessSupplierMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        List<FiveBusinessContractLibrarySubpackage> librarySubpackages = listCooperationProject(item.getId());
        dto.setCooperProjectNum(librarySubpackages.size());
        return dto;
    }

    public int getNewModel(String userLogin) {

        BusinessSupplier item = new BusinessSupplier();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        //默认生成5位数编号
        Map map = new HashMap();
        map.put("deleted",false);
        String num =String.valueOf(businessSupplierMapper.selectAll(map).size()+1);
        int length =num.length();
        for(int i = 0;i<5-length;i++){
            num = "0"+num;
        }
        item.setCode(num);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setBusinessScope(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"客户行业类别").toString());
        item.setSupplierType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"客户单位类型").toString());
        item.setTaxType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"纳税主体资格").toString());
        item.setInType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"供方入库类别").toString());

        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        businessSupplierMapper.insert(item);
        String businessKey = EdConst.FIVE_BUSINESS_SUPPLIER +"_"+item.getId();
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "供方信息录入");
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_SUPPLIER,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessSupplierMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String, Object> params, int pageNum, int pageSize, String uiSref) {
        params.put("deleted", false);
        String userLogin = params.get("userLogin").toString();

        params.put("isLikeSelect","true");
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        //params.putAll(getDefaultRightParams(map));

        params.put("processEndPo",userLogin);//看见自己创建的和其他流程已完成的
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessSupplierMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessSupplier item = (BusinessSupplier) p;
            BusinessSupplierDto dto = getDto(item);
            list.add(dto);
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public Object listDetailById(int SupplierId) {
        Map map = new HashMap();
        map.put("deleted", false);
        map.put("supplierId", SupplierId);
        return businessSupplierAptitudeMapper.selectAll(map);
    }

    public Object getDetailModelById(int id) {
        return businessSupplierAptitudeMapper.selectByPrimaryKey(id);
    }

    public Object getNewDetailModel(int SupplierId) {
        BusinessSupplierAptitude detail = new BusinessSupplierAptitude();
        detail.setSupplierId(SupplierId);
        detail.setSeq(businessSupplierAptitudeMapper.selectAll(new HashMap()).size());
        detail.setDeleted(false);
        detail.setGmtCreate(new Date());
        detail.setGmtModified(new Date());
        ModelUtil.setNotNullFields(detail);
        businessSupplierAptitudeMapper.insert(detail);
        return detail.getId();
    }

    public void removeDetail(int id, String userLogin) {
        BusinessSupplierAptitude detail = businessSupplierAptitudeMapper.selectByPrimaryKey(id);
        detail.setDeleted(true);
        detail.setGmtModified(new Date());
        businessSupplierAptitudeMapper.updateByPrimaryKey(detail);
    }

    public void updateDetail(BusinessSupplierAptitude item) {
        businessSupplierAptitudeMapper.updateByPrimaryKey(item);
    }

    public List<BusinessSupplier> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  businessSupplierMapper.selectAll(params);
    }

    public List<FiveBusinessContractLibrarySubpackage> listCooperationProject(int supplierId) {
        List<FiveBusinessContractLibrary> list = new ArrayList<>();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        List<FiveBusinessContractLibrarySubpackage> libraries = fiveBusinessContractLibrarySubpackageMapper.selectAll(params)
                .stream().filter(p->p.getSupplierId()==supplierId).collect(Collectors.toList());
        return libraries;
    }
    //检查 纳税人识别号是否重复
    public void checkTaxNo(String taxNo,int supplierId){
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("taxNo",taxNo);
        List<BusinessSupplier>businessSuppliers=businessSupplierMapper.selectAll(map).stream().filter(p->p.getId()!=supplierId).collect(Collectors.toList());;
        Assert.state(businessSuppliers.size()==0,"纳税人识别号："+taxNo+"已存在！");

    }

    //检查 供方名称是否重复
    public void checkSupplier(String name,int supplierId) {
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("name",name);
        List<BusinessSupplier> businessSuppliers = businessSupplierMapper.selectAll(map).stream().filter(p->p.getId()!=supplierId).collect(Collectors.toList());
        Assert.state(businessSuppliers.size()==0,"供方名称为："+name+" 已存在！");
    }


    public List<BusinessSupplierUsedName> listUsedNamesById(int customerId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("customerId", customerId);
        List<BusinessSupplierUsedName> businessSupplierUsedNames = businessSupplierUsedNameMapper.selectAll(params);
        return businessSupplierUsedNames;
    }

    public BusinessSupplierUsedName getUsedNameById(int id) {
        return businessSupplierUsedNameMapper.selectByPrimaryKey(id);
    }

    public void updateUsedName(BusinessSupplierUsedName usedName) {
        usedName.setGmtModified(new Date());
        businessSupplierUsedNameMapper.updateByPrimaryKey(usedName);
    }

    public int getNewUsedName(String userLogin, int customerId) {
        //BusinessSupplier businessSupplier = businessSupplierMapper.selectByPrimaryKey(customerId);
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        BusinessSupplierUsedName item = new BusinessSupplierUsedName();
        item.setSupplierId(customerId);
        item.setDeleted(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setSeq(0);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        ModelUtil.setNotNullFields(item);
        businessSupplierUsedNameMapper.insert(item);
        return item.getId();
    }

    public void removeUsedName(int id, String userLogin) {
        businessSupplierUsedNameMapper.deleteByPrimaryKey(id);
    }
}


