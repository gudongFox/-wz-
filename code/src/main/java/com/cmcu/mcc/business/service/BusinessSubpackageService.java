package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.BusinessSubpackageDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceStampTaxMapper;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
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

@Service
public class BusinessSubpackageService extends BaseService {

    @Resource
    BusinessSubpackageMapper businessSubpackageMapper;
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
    BusinessSupplierMapper businessSupplierMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractReviewMapper fiveBusinessContractReviewMapper;
    @Autowired
    FiveBusinessContractReviewDetailMapper fiveBusinessContractReviewDetailMapper;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Autowired
    HandleFormService handleFormService;
    @Autowired
    FiveFinanceStampTaxMapper fiveFinanceStampTaxMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageService fiveBusinessContractLibrarySubpackageService;

    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    CommonFileService commonFileService;

    public void remove(int id, String userLogin) {
        BusinessSubpackage item = businessSubpackageMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");

        //???????????????????????? ??????
        if(item.getContractLibraryId()!=0){
            FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            oldLibrary.setSubpackageIds(MyStringUtil.getNewStringId(oldLibrary.getSubpackageIds(),id));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
        }

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }


    public void update(BusinessSubpackageDto dto) {
        BusinessSubpackage item = businessSubpackageMapper.selectByPrimaryKey(dto.getId());

        //???????????????????????????
        if(dto.getIsReplenish()&&dto.getMainContractLibraryId()!=0){
            //???????????????????????? ??????
            if(item.getMainContractLibraryId()!=0){
                FiveBusinessContractLibrarySubpackage oldLibrary = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(item.getMainContractLibraryId());
                oldLibrary.setReviewIds(MyStringUtil.getNewStringId(oldLibrary.getReviewIds(),item.getId()));
                fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(oldLibrary);
            }
            //????????? ????????????
            FiveBusinessContractLibrarySubpackage library = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(dto.getMainContractLibraryId());
            library.setReviewIds(library.getReviewIds()+","+item.getId());
            fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(library);
        }else{
            //???????????????????????? ??????
            if(item.getMainContractLibraryId()!=0){
                FiveBusinessContractLibrarySubpackage oldLibrary = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(item.getMainContractLibraryId());
                oldLibrary.setReviewIds(MyStringUtil.getNewStringId(oldLibrary.getReviewIds(),item.getId()));
                fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(oldLibrary);
            }
            dto.setMainContractLibraryName("");
            dto.setMainContractLibraryNo("");
            dto.setMainContractLibraryId(0);
            //dto.setSubContractNo("");
        }
        item.setMainContractLibraryId(dto.getMainContractLibraryId());
        item.setMainContractLibraryName(dto.getMainContractLibraryName());
        item.setMainContractLibraryNo(dto.getMainContractLibraryNo());

        //?????????????????????
        if(dto.getContractLibraryId()!=0){
            //???????????????????????? ??????
            if(item.getContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                oldLibrary.setSubpackageIds(MyStringUtil.getNewStringId(oldLibrary.getSubpackageIds(),item.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
            }
            //????????? ????????????
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getContractLibraryId());
            library.setSubpackageIds(library.getSubpackageIds()+","+item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }


        item.setContractLibraryId(dto.getContractLibraryId());
        item.setContractNo(dto.getContractNo());
        item.setContractName(dto.getContractName());
        item.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney()));
        item.setContractType(dto.getContractType());
        item.setProjectNature(dto.getProjectNature());
        item.setSubContractType(dto.getSubContractType());
        item.setSubContractName(dto.getSubContractName());
        item.setSubContractMoney(MyStringUtil.moneyToString(dto.getSubContractMoney()));
        item.setSubContractNo(dto.getSubContractNo());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setSubContractDesc(dto.getSubContractDesc());
        item.setCashDeposit(dto.getCashDeposit());
        item.setCashDepositMoney(MyStringUtil.moneyToString(dto.getCashDepositMoney()));
        item.setBackletter(dto.getBackletter());
        item.setBackletterMoney(MyStringUtil.moneyToString(dto.getBackletterMoney()));
        item.setSign(dto.getSign());
        item.setContractStatus(dto.getContractStatus());
        item.setSupplierId(dto.getSupplierId());
        item.setSupplierName(dto.getSupplierName());
        item.setSupplierCreditCode(dto.getSupplierCreditCode());
        item.setSupplierLinkMan(dto.getSupplierLinkMan());
        item.setSupplierLinkTel(dto.getSupplierLinkTel());
        item.setDepositBank(dto.getDepositBank());
        item.setBankAccount(dto.getBankAccount());
        item.setInCompany(dto.getInCompany());
        item.setInDeptName(dto.getInDeptName());
        item.setInDeptId(dto.getInDeptId());
        item.setReviewLevel(dto.getReviewLevel());
        item.setContractChargeLeader(dto.getContractChargeLeader());
        item.setContractChargeLeaderName(dto.getContractChargeLeaderName());
        item.setReviewUser(dto.getReviewUser());
        item.setReviewUserName(dto.getReviewUserName());
        item.setDeptReviewUser(dto.getDeptReviewUser());
        item.setDeptReviewUserName(dto.getDeptReviewUserName());

        item.setDeptChargeMen(dto.getDeptChargeMen());
        item.setDeptChargeMenName(dto.getDeptChargeMenName());

        item.setContractAttachUrl(dto.getContractAttachUrl());
        item.setTaxType(dto.getTaxType());
        item.setSignTime(dto.getSignTime());

        //?????????????????????
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"?????????????????????");
        for(CommonCodeDto codeDto:commonCodes){
            if(codeDto.getName().equalsIgnoreCase(item.getTaxType())){
                if(codeDto.getName().equalsIgnoreCase("??????")&&StringUtils.isNotEmpty(dto.getTaxNum())){
                    item.setTaxNum(dto.getTaxNum());
                }else{
                    item.setTaxNum(codeDto.getCode());
                }
                item.setStampTaxMoney(MyStringUtil.getNewMultiplyMoney1(item.getSubContractMoney(),item.getTaxNum()));
            }
        }

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);

        Map variables=Maps.newHashMap();
        if(item.getReviewLevel().equals("?????????")){
            variables.put("flag",true);
        }else{
            variables.put("flag",false);
        }

        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }

        variables.put("reviewUsers", MyStringUtil.getStringList(item.getReviewUser()));
        variables.put("deptReviewUsers", MyStringUtil.getStringList(item.getDeptReviewUser()));
        variables.put("contractChargeLeader",item.getContractChargeLeader());
        variables.put("deptChargeMen",MyStringUtil.getStringList(item.getDeptChargeMen()));
        variables.put("processDescription", "??????????????????:"+item.getSubContractName()+"("+item.getSubContractNo()+")");
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);
        businessSubpackageMapper.updateByPrimaryKey(item);
    }


    public BusinessSubpackageDto getModelById(int id) {
        return getDto(businessSubpackageMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin,String uiSref) {
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);

        BusinessSubpackage item = new BusinessSubpackage();
        item.setPurchase(false);
        item.setSupplierId(0);
        HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
        item.setCashDeposit(false);
        item.setBackletter(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setSubContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setReviewLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setTaxType("");

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        ModelUtil.setNotNullFields(item);
        businessSubpackageMapper.insert(item);

        String processKey = EdConst.FIVE_BUSINESS_SUBPACKAGE;

        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
       // variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//?????????????????????
        variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//???????????????????????????
        variables.put("opratorLeader", selectEmployeeService.getDeptChargeMen(29));//???????????????
        variables.put("lawReview", selectEmployeeService.getUserListByRoleName("????????????"));//????????????
        variables.put("processDescription", "??????????????????");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessSubpackageMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public BusinessSubpackageDto getDto(Object item) {
        BusinessSubpackage model= (BusinessSubpackage) item;
        BusinessSubpackageDto dto=BusinessSubpackageDto.adapt(model);
        if(dto.getMainContractLibraryId()!=0){
            dto.setIsReplenish(true);
        }
        if(model.getDeptId()!=0){
            dto.setHeadDeptId(selectEmployeeService.getHeadDeptId(model.getDeptId()));
        }
        if(StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            try{
                CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
                if(customProcessInstance.isFinished()){
                    dto.setProcessName("?????????");
                }
                if(!dto.getProcessEnd()&&customProcessInstance.isFinished()){
                    dto.setProcessEnd(true);
                    businessSubpackageMapper.updateByPrimaryKey(dto);
                    //????????????????????????
                    insertStampTax(model.getId());
                    //???????????????
                    FiveBusinessContractLibrarySubpackage librarySubpackage = fiveBusinessContractLibrarySubpackageService.getModelBySubpackageId(model.getId());
                    librarySubpackage.setSubContractMoney(model.getSubContractMoney());
                    fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(librarySubpackage);
                }
                if(dto.getProcessName().contains("??????????????????")||dto.getProcessName().contains("??????")){
                    //????????????????????????
                    insertContractLibrary(model.getId());
                }
                if(dto.getProcessName().contains("?????????")){
                    //?????????????????????
                    Map map = new HashMap();
                    map.put("subpackageId",dto.getId());
                    List<FiveBusinessContractLibrarySubpackage> librarySubpackages = fiveBusinessContractLibrarySubpackageMapper.selectAll(map);
                    if(librarySubpackages.size()>0){
                        //??????????????????????????????
                        commonFileService.insertByUrl(librarySubpackages.get(0).getBusinessKey(),dto.getContractAttachUrl(),dto.getCreator());
                    }
                }
            }catch (Exception e){

            }
        }

        //????????????6???
        dto.setSubContractMoney(MyStringUtil.moneyToString(dto.getSubContractMoney(),6));
        dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(),6));
        return dto;
    }

    public List<BusinessSubpackage> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  businessSubpackageMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        //????????????
        params.put("purchase",false);
//???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        params.put("isLikeSelect",true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessSubpackageMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessSubpackage item=(BusinessSubpackage)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        BusinessSubpackage item = businessSubpackageMapper.selectByPrimaryKey(id);
        data.put("gmtCreate", item.getGmtCreate());
        data.put("creatorName", item.getCreatorName());
        data.put("nodes", myHistoryService.listPassedHistoryDto(item.getProcessInstanceId()));
        return data;
    }

    public int addSupplier(String SubpackageId, String userLogin) {
        BusinessSubpackageDto item = getModelById(Integer.parseInt(SubpackageId));
        Map map =new HashMap();
        map.put("name",item.getSupplierName());
        BusinessSupplier bs;
        List<BusinessSupplier> businessSuppliers = businessSupplierMapper.selectAll(map);
        if(businessSuppliers.size()>0){
            bs = businessSuppliers.get(0);
            Assert.state(false,"????????????????????????");
           /* bs.setName(item.getSupplierName());
            bs.setCreditCode(item.getSupplierCreditCode());
            bs.setLinkMan(item.getSupplierLinkMan());
            bs.setLinkTel(item.getSupplierLinkTel());
            bs.setGmtModified(new Date());
            businessSupplierMapper.updateByPrimaryKey(bs);*/
        }else{
            bs = new BusinessSupplier();
            bs.setName(item.getSupplierName());
            bs.setCreditCode(item.getSupplierCreditCode());
            bs.setLinkMan(item.getSupplierLinkMan());
            bs.setLinkTel(item.getSupplierLinkTel());
            bs.setDepositBank(item.getDepositBank());
            bs.setBankAccount(item.getBankAccount());
            bs.setInType("?????????"+item.getSubContractName()+" ????????????");

            HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
            bs.setDeptId(hrEmployeeDto.getDeptId());
            bs.setDeptName(hrEmployeeDto.getDeptName());
            bs.setCreator(userLogin);
            bs.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
            bs.setGmtCreate(new Date());
            bs.setGmtModified(new Date());
            ModelUtil.setNotNullFields(bs);
            businessSupplierMapper.insert(bs);

            String businessKey = EdConst.FIVE_BUSINESS_SUPPLIER +"_"+item.getId();
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "??????????????????");
            variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("?????????????????????(??????)"));//?????????????????????????????????
            bs.setBusinessKey(businessKey);
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_SUPPLIER,bs.getBusinessKey(), variables, MccConst.APP_CODE);
            bs.setProcessInstanceId(processInstanceId);

            businessSupplierMapper.updateByPrimaryKey(bs);
            //?????? ????????????id
            item.setSupplierId(bs.getId());
            businessSubpackageMapper.updateByPrimaryKey(item);
        }

        return bs.getId();
    }

    public String insertContractLibrary(int subpackageId) {
        BusinessSubpackage subpackage = businessSubpackageMapper.selectByPrimaryKey(subpackageId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("subpackageId",subpackage.getId());
        List<FiveBusinessContractLibrarySubpackage> librarys = fiveBusinessContractLibrarySubpackageMapper.selectAll(map);
        if(librarys.size()>0){
            return "????????? "+subpackage.getSubContractName()+" ????????????????????????";
        }else{
            FiveBusinessContractLibrarySubpackage item = new FiveBusinessContractLibrarySubpackage();
            item.setContractNo(subpackage.getContractNo());
            item.setContractName(subpackage.getContractName());
            item.setContractMoney(MyStringUtil.moneyToString(subpackage.getContractMoney()));
            item.setContractType(subpackage.getContractType());
            item.setProjectNature(subpackage.getProjectNature());
            item.setSubContractType(subpackage.getSubContractType());
            item.setSubContractName(subpackage.getSubContractName());
            item.setSubContractMoney(MyStringUtil.moneyToString(subpackage.getSubContractMoney()));
            item.setSubContractNo(subpackage.getSubContractNo());
            item.setDeptId(subpackage.getDeptId());
            item.setDeptName(subpackage.getDeptName());
            item.setSubContractDesc(subpackage.getSubContractDesc());
            item.setCashDeposit(subpackage.getCashDeposit());
            item.setCashDepositMoney(MyStringUtil.moneyToString(subpackage.getCashDepositMoney()));
            item.setBackletter(subpackage.getBackletter());
            item.setBackletterMoney(MyStringUtil.moneyToString(subpackage.getBackletterMoney()));
            item.setSign(subpackage.getSign());
            item.setContractStatus(subpackage.getContractStatus());
            item.setSupplierId(subpackage.getSupplierId());
            item.setSupplierName(subpackage.getSupplierName());
            item.setSupplierCreditCode(subpackage.getSupplierCreditCode());
            item.setSupplierLinkMan(subpackage.getSupplierLinkMan());
            item.setSupplierLinkTel(subpackage.getSupplierLinkTel());
            item.setDepositBank(subpackage.getDepositBank());
            item.setBankAccount(subpackage.getBankAccount());
            item.setInCompany(subpackage.getInCompany());
            item.setInDeptId(subpackage.getInDeptId());
            item.setInDeptName(subpackage.getInDeptName());
            item.setRemark(subpackage.getRemark());
            item.setCreator(subpackage.getCreator());
            item.setCreatorName(subpackage.getCreatorName());
            item.setReviewLevel(subpackage.getReviewLevel());
            item.setContractChargeLeader(subpackage.getContractChargeLeader());
            item.setContractChargeLeaderName(subpackage.getContractChargeLeaderName());
            item.setPurchase(subpackage.getPurchase());
            item.setSubpackageId(subpackageId);

            item.setContractAttachUrl(subpackage.getContractAttachUrl());
            item.setTaxType(subpackage.getTaxType());
            item.setTaxNum(subpackage.getTaxNum());
            item.setStampTaxMoney(subpackage.getStampTaxMoney());
            item.setOpenStamp(subpackage.getOpenStamp());
            item.setSignTime(subpackage.getSignTime());
            item.setDeptReviewUser(subpackage.getDeptReviewUser());
            item.setDeptReviewUserName(subpackage.getDeptReviewUserName());
            item.setDeptChargeMen(subpackage.getDeptChargeMen());
            item.setDeptChargeMenName(subpackage.getDeptChargeMenName());

            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibrarySubpackageMapper.insert(item);
            item.setBusinessKey(EdConst.FIVE_BUSINESSCONTRACTLIBRARYSUBPACKAGE+"_"+item.getId());
            fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(item);
            //?????????
            commonFileService.copyFileByBusinessKey(subpackage.getBusinessKey(),item.getBusinessKey(),0);
            return "????????? "+subpackage.getSubContractName()+"  ???????????????????????????";
        }
    }

    public String insertStampTax(int subpackageId) {
        BusinessSubpackage subpackage = businessSubpackageMapper.selectByPrimaryKey(subpackageId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractSubpackageId",subpackageId);
        List<FiveFinanceStampTax> stampTaxs = fiveFinanceStampTaxMapper.selectAll(map);
        if(stampTaxs.size()>0){
            return "??????????????? "+subpackage.getSubContractName()+" ??????????????????";
        }else{
            FiveFinanceStampTax stampTax = new FiveFinanceStampTax();
            //?????????
            stampTax.setCreator("2623");
            stampTax.setCreatorName("?????????");
            stampTax.setContractSubpackageId(subpackage.getId());
            stampTax.setContractName(subpackage.getSubContractName());
            stampTax.setContractNo(subpackage.getSubContractNo());
            stampTax.setCustomerName(subpackage.getSupplierName());
            //?????????????????????????????? ??? ?????????
            if(subpackage.getContractLibraryId()!=0){
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(subpackage.getContractLibraryId());
                stampTax.setProjectName(library.getProjectName());
                stampTax.setProjectNo(library.getProjectNo());
            }
            stampTax.setContractDeptId(subpackage.getDeptId());
            stampTax.setContractDeptName(subpackage.getDeptName());

            stampTax.setSignTime(subpackage.getSignTime());
            stampTax.setTaxType(subpackage.getTaxType());
            stampTax.setContractMoney(subpackage.getSubContractMoney());
            stampTax.setTaxNum(subpackage.getTaxNum());
            stampTax.setStampTaxMoney(subpackage.getStampTaxMoney());
            stampTax.setGmtCreate(new Date());
            stampTax.setGmtModified(new Date());
            stampTax.setDeleted(false);
            ModelUtil.setNotNullFields(stampTax);
            fiveFinanceStampTaxMapper.insert(stampTax);

            String processKey=EdConst.FIVE_FINANCE_STAMPTAX;
            String businessKey=processKey+"_"+stampTax.getId();
/*            Map variables = Maps.newHashMap();
            variables.put("userLogin", "2623");
            variables.put("processDescription", "???????????????");
            String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
            stampTax.setProcessInstanceId(processInstanceId);*/
            stampTax.setBusinessKey(businessKey);
            fiveFinanceStampTaxMapper.updateByPrimaryKey(stampTax);

            //??????????????????????????????
            commonFileService.insertByUrl(stampTax.getBusinessKey(),subpackage.getContractAttachUrl(),subpackage.getCreator());

            return "??????????????? "+subpackage.getContractName()+" ????????????????????????";
        }
    }

    public int insertContractReview(int subpackageId) {
        BusinessSubpackage subpackage = businessSubpackageMapper.selectByPrimaryKey(subpackageId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("subpackageId",subpackage.getId());
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(librarys.size()>0){
            return 0;
        }else{
            FiveBusinessContractReview review = new FiveBusinessContractReview();
            review.setProjectName(subpackage.getSubContractName());

            review.setContractNo(subpackage.getSubContractNo());
            //review.setDeptId(subpackage.getInDeptId());
            //review.setDeptName(subpackage.getInDeptName());
            review.setInvestMoney(MyStringUtil.moneyToString(subpackage.getSubContractMoney()));
            review.setSubpackageId(subpackageId);

            String userLogin =subpackage.getCreator();
            //??????????????????
            List<String> userLogins = hrEmployeeService.selectUserByRoleNames("??????????????????");
            String reviewUser ="";
            String reviewUserName ="";
            for(String login:userLogins){
                HrEmployeeDto employee = hrEmployeeService.getModelByUserLogin(login);
                reviewUser = reviewUser+login+",";
                reviewUserName = reviewUserName+employee.getUserName()+",";
            }
            review.setReviewUser(reviewUser);
            review.setReviewUserName(reviewUserName);
            review.setReviewLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
            review.setProjectNature(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
            review.setInOrOut(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"?????????").toString());
            review.setMilitaryMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
            review.setCivilMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
            review.setForeignTradeMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
            review.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
            review.setProjectMajorTypeSecond(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
            review.setProjectMajorTypeThird(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
            review.setInvestSource(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
            review.setMain(false);
            //item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????"));
            review.setContractType("????????????");
            review.setCustomerId(0);
            review.setBid(false);
            review.setLegalPerson(false);
            review.setEleLegalPerson(false);
            review.setBusinessLegalPerson(false);
            review.setEleBusinessLegalPerson(false);
            review.setCommonSeal(false);
            review.setAttach(false);

            review.setDeleted(false);
            review.setProcessEnd(false);
            review.setCreator(userLogin);
            review.setGmtModified(new Date());
            review.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(review);
            fiveBusinessContractReviewMapper.insert(review);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTREVIEW+ "_" + review.getId();
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "???????????????");
            variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(review.getDeptId()));//?????????????????????
            variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//?????????????????????
            variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//???????????????????????????
            variables.put("opratorLeader", selectEmployeeService.getDeptChargeMen(29));//???????????????
            variables.put("lawReview", selectEmployeeService.getUserListByRoleName("????????????"));//????????????
            review.setBusinessKey(businessKey);
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESSCONTRACTREVIEW,review.getBusinessKey(), variables, MccConst.APP_CODE);
            review.setProcessInstanceId(processInstanceId);
            fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
            //??????????????????
            List<String> baseNames = new ArrayList<>();
            baseNames.add("????????????????????????????????????");
            baseNames.add("????????????????????????????????????????????????????????????????????????????????????");
            baseNames.add("??????????????????????????????????????????????????????????????????????????????");
            baseNames.add("????????????????????????????????????????????????????????????");
            baseNames.add("????????????????????????????????????????????????");
            baseNames.add("??????????????????????????????");
            baseNames.add("????????????????????????????????????????????????????????????????????????????????????");
            baseNames.add("?????????????????????????????????????????????");
            baseNames.add("???????????????");
            initDetail(review.getId(),baseNames);
            return review.getId();
        }

    }

    private void initDetail(Integer contractReviewId, List<String> baseNames){
        for(String baseName:baseNames){
            FiveBusinessContractReviewDetail model=new FiveBusinessContractReviewDetail();
            model.setContractReviewId(contractReviewId);
            model.setReviewContent(baseName);
            model.setReviewResult("??????");
            model.setSeq(fiveBusinessContractReviewMapper.selectAll(new HashMap()).size()+"");
            model.setGmtCreate(new Date());
            model.setGmtModified(new Date());
            ModelUtil.setNotNullFields(model);
            fiveBusinessContractReviewDetailMapper.insert(model);
        }
    }

    public void changeOpenStamp(int id,String userLogin){
        BusinessSubpackage model = businessSubpackageMapper.selectByPrimaryKey(id);
        if(model.getOpenStamp()){
            model.setOpenStamp(false);
        }else {
            model.setOpenStamp(true);
        }
        businessSubpackageMapper.updateByPrimaryKey(model);
        //?????????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("subpackageId",model.getId());
        List<FiveBusinessContractLibrarySubpackage> contractLibrarys = fiveBusinessContractLibrarySubpackageMapper.selectAll(map);
        if(contractLibrarys.size()>0){
            FiveBusinessContractLibrarySubpackage contractLibrary =contractLibrarys.get(0);
            contractLibrary.setOpenStamp(model.getOpenStamp());
            fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(contractLibrary);
        }
    }

    /**
     ?????????????????????????????????????????????-S1???-S2????????????17011001?????????????????????????????????????????????17011001-S1??????
     ????????????????????????????????????????????????????????????-G1???-G2?????????
     ??????????????????????????????????????????????????????-F1???-F2?????????
     ????????????????????????????????????????????????????????????-Y1???-Y2?????????
     * @param id
     */

    public String getSubContractNo(int id){
        try {
            BusinessSubpackage subPackage = businessSubpackageMapper.selectByPrimaryKey(id);
            //???????????????????????????
            if(subPackage.getMainContractLibraryId()!=0){
                String newSubContractNo=subPackage.getMainContractLibraryNo();
                String code ="-";
                //?????? 001
                Map<String, Object> params=Maps.newHashMap();
                params.put("deleted",false);
                params.put("subContractNoHead",newSubContractNo+code);
                List<BusinessSubpackage> subpackages = businessSubpackageMapper.selectAll(params);
                int max=0;
                //???????????????????????????
                if (!subpackages.isEmpty()){
                    for (BusinessSubpackage item:subpackages){
                        //????????????
                        if(!item.getId().equals(subPackage.getId())) {
                            //???????????????
                            String no = item.getSubContractNo();
                            int num = Integer.valueOf(no.substring(no.length() - 1));
                            if (num > max) {
                                max = num;
                            }
                        }
                    }
                }
                max=max+1;
                newSubContractNo=newSubContractNo+code+max;

                subPackage.setSubContractNo(newSubContractNo);
                businessSubpackageMapper.updateByPrimaryKey(subPackage);

                return newSubContractNo;

            }else{
                String newSubContractNo=subPackage.getContractNo();
                String code ="-";
                //????????????????????????
                if(subPackage.getSubContractType().equals("??????")){
                    code = code+"S";
                }else if(subPackage.getSubContractType().equals("????????????")){
                    code = code+"G";
                }else if(subPackage.getSubContractType().equals("??????")){
                    code = code+"F";
                }else if(subPackage.getSubContractType().equals("????????????")){
                    code = code+"J";
                }else if(subPackage.getSubContractType().equals("???????????????")){
                    code = code+"X";
                }else if(subPackage.getSubContractType().equals("????????????")){
                    code = code+"Z";
                }

                //?????? 001
                Map<String, Object> params=Maps.newHashMap();
                params.put("deleted",false);
                params.put("purchase",false);
                params.put("subContractNoHead",newSubContractNo+code);
                List<BusinessSubpackage> businessSubpackages =businessSubpackageMapper.selectAll(params);
                int max=0;
                //???????????????????????????
                if (!businessSubpackages.isEmpty()){
                    for (BusinessSubpackage businessSubpackage:businessSubpackages){
                        //?????? ????????????
                        if(businessSubpackage.getMainContractLibraryId()==0&&!Strings.isNullOrEmpty(businessSubpackage.getSubContractNo())) {
                            String subContractNo = businessSubpackage.getSubContractNo();
                            //???????????? H  ?????? H
                            if (subContractNo.startsWith("H")) {
                                subContractNo = subContractNo.substring(1);
                            }
                            /* if(businessSubpackage.getId()!=subPackage.getId()){*/
                            //??????????????? ????????? ???????????? -??? ?????? ??????
                            String tempNum =subContractNo.substring(subContractNo.length()-3).replaceAll("[a-zA-Z]","");
                            int num = Integer.valueOf(tempNum.replace("-",""));
                            if (num > max) {
                                max = num;
                            }
                            /*}*/
                        }
                    }
                }
                max=max+1;
                newSubContractNo=newSubContractNo+code+max;

                subPackage.setSubContractNo(newSubContractNo);
                businessSubpackageMapper.updateByPrimaryKey(subPackage);

                return newSubContractNo;
            }
        }catch (Exception e){
            Assert.state(false, "?????????????????? ?????????????????????");
            return "";
        }


    }
}
