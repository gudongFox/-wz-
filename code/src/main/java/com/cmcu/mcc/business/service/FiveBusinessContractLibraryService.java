package com.cmcu.mcc.business.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.BusinessIncomeDto;
import com.cmcu.mcc.business.dto.FiveBusinessContractLibraryDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceStampTaxDetailMapper;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoice;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTaxDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrDept;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveBusinessContractLibraryService extends BaseService {
    @Resource
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
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
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    BusinessSubpackageMapper  businessSubpackageMapper;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveBusinessContractReviewMapper fiveBusinessContractReviewMapper;
    @Autowired
    HandleFormService handleFormService;
    @Autowired
    FiveFinanceInvoiceMapper fiveFinanceInvoiceMapper;
    @Autowired
    FiveFinanceStampTaxDetailMapper fiveFinanceStampTaxDetailMapper;
    @Resource
    HrDeptService hrDeptService;
    @Resource
    BusinessIncomeService businessIncomeService;

    public void remove(int id,String userLogin){
        FiveBusinessContractLibrary model = fiveBusinessContractLibraryMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin),"??????????????????"+model.getCreatorName()+"??????");
        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);
    }

    public void update(FiveBusinessContractLibraryDto dto){
        FiveBusinessContractLibrary model = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getId());
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setProjectName(dto.getProjectName());
        model.setContractNo(dto.getContractNo());
        model.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney()));
        model.setContractType(dto.getContractType());
        model.setSignTime(dto.getSignTime());
        model.setBid(dto.getBid());
        model.setInvestMoney(MyStringUtil.moneyToString(dto.getInvestMoney()));
        model.setInvestSource(dto.getInvestSource());
        model.setInOrOut(dto.getInOrOut());
        model.setProjectMajorTypeFirst(dto.getProjectMajorTypeFirst());
        model.setProjectMajorTypeSecond(dto.getProjectMajorTypeSecond());
        model.setProjectMajorTypeThird(dto.getProjectMajorTypeThird());
        model.setProjectNature(dto.getProjectNature());
        model.setIndustryType(dto.getIndustryType());
        model.setCivilMark(dto.getCivilMark());
        model.setMilitaryMark(dto.getMilitaryMark());
        model.setForeignTradeMark(dto.getForeignTradeMark());
        model.setProjectNo(dto.getProjectNo());
        model.setMainContractLibraryId(dto.getMainContractLibraryId());
        model.setMainContractLibraryName(dto.getMainContractLibraryName());
        model.setMainContractLibraryNo(dto.getMainContractLibraryNo());
        model.setBackletter(dto.getBackletter());
        model.setBackletterMoney(MyStringUtil.moneyToString(dto.getBackletterMoney()));
        model.setReviewLevel(dto.getReviewLevel());
        model.setReviewTime(dto.getReviewTime());
        model.setInvestMoney(MyStringUtil.moneyToString(dto.getInvestMoney()));
        model.setConstructionScale(dto.getConstructionScale());
        model.setConstructionArea(dto.getConstructionArea());
        model.setMainDescription(dto.getMainDescription());
        model.setCustomerId(dto.getCustomerId());
        model.setCustomerCode(dto.getCustomerCode());
        model.setCustomerName(dto.getCustomerName());
        model.setCustomerAddress(dto.getCustomerAddress());
        model.setLinkMan(dto.getLinkMan());
        model.setLinkTel(dto.getLinkTel());
        model.setLinkTitle(dto.getLinkTitle());

        model.setLegalPerson(dto.getLegalPerson());
        model.setBusinessLegalPerson(dto.getBusinessLegalPerson());
        model.setEleLegalPerson(dto.getEleLegalPerson());
        model.setEleBusinessLegalPerson(dto.getEleBusinessLegalPerson());
        model.setReviewUser(dto.getReviewUser());
        model.setReviewUserName(dto.getReviewUserName());
        model.setTotalDesigner(dto.getTotalDesigner());
        model.setTotalDesignerName(dto.getTotalDesignerName());
        model.setReviewUser(dto.getReviewUser());
        model.setReviewUserName(dto.getReviewUserName());
        model.setContractName(dto.getContractName());

        model.setProjectManager(dto.getProjectManager());
        model.setProjectManagerName(dto.getProjectManagerName());

        model.setProjectChargeMan(dto.getProjectChargeMan());
        model.setProjectChargeManName(dto.getProjectChargeManName());

        model.setBusinessChargeLeader(dto.getBusinessChargeLeader());
        model.setBusinessChargeLeaderName(dto.getBusinessChargeLeaderName());
        model.setRecordProjectName(dto.getRecordProjectName());

        model.setMain(dto.getMain());
        model.setAttach(dto.getAttach());
        model.setRemark(dto.getRemark());
        BeanValidator.check(model);
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(model);
    }

    public FiveBusinessContractLibraryDto getModelById(int id){
        return getDto(fiveBusinessContractLibraryMapper.selectByPrimaryKey(id));
    }

    public FiveBusinessContractLibraryDto getModelByContractNo(String contractNo){
        if ("".equals(contractNo)){
            return null;
        }
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("contractNo", contractNo);
        if(PageHelper.count(()->fiveBusinessContractLibraryMapper.selectAll(params))==0)return null;
        return getDto(fiveBusinessContractLibraryMapper.selectAll(params).get(0));
    }

    public FiveBusinessContractLibraryDto getDto(FiveBusinessContractLibrary item) {
        FiveBusinessContractLibraryDto dto=FiveBusinessContractLibraryDto.adapt(item);

        //?????? ???????????? ?????????-???????????????-????????????
        String temp =MyStringUtil.getNewSubMoney(dto.getContractIncomeMoney(),dto.getPreContractMoney());
        dto.setShouldContractMoney(MyStringUtil.getNewSubMoney(dto.getContractInvoiceMoney(),temp));
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(dto);
        //?????? ????????? ??????????????? ??????0
        if(StringUtils.isEmpty(dto.getPreContractMoney())){
            dto.setPreContractMoney(MyStringUtil.moneyToString(""));
        }
        if(StringUtils.isEmpty(dto.getShouldContractMoney())){
            dto.setShouldContractMoney(MyStringUtil.moneyToString(""));
        }
        //?????????
        if(dto.getStampTaxId()!=0){
            FiveFinanceStampTaxDetail fiveFinanceStampTaxDetail = fiveFinanceStampTaxDetailMapper.selectByPrimaryKey(dto.getStampTaxId());
            dto.setFiveFinanceStampTaxDetail(fiveFinanceStampTaxDetail);
        }
        //????????????
        if(dto.getContractReviewId()!=0){
            FiveBusinessContractReview fiveBusinessContractReview = fiveBusinessContractReviewMapper.selectByPrimaryKey(dto.getContractReviewId());
            dto.setFiveBusinessContractReview(fiveBusinessContractReview);
        }

        //????????????6???
        dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(),6));
        dto.setInvestMoney(MyStringUtil.moneyToString(dto.getInvestMoney(),6));
        dto.setContractIncomeMoney(MyStringUtil.moneyToString(dto.getContractIncomeMoney(),6));
        dto.setContractInvoiceMoney(MyStringUtil.moneyToString(dto.getContractInvoiceMoney(),6));
        dto.setShouldContractMoney(MyStringUtil.moneyToString(dto.getShouldContractMoney(),6));
        dto.setPreContractMoney(MyStringUtil.moneyToString(dto.getPreContractMoney(),6));
        return dto;
    }

    public int getNewModel(String userLogin){

        FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setReviewLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setProjectNature(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setInOrOut(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"?????????").toString());
        item.setMilitaryMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setCivilMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setForeignTradeMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setProjectMajorTypeSecond(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setProjectMajorTypeThird(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setInvestSource(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());

        item.setCustomerId(0);
        item.setBid(false);
        item.setAttach(false);
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());

        item.setInsertType(1);

        ModelUtil.setNotNullFields(item);
        fiveBusinessContractLibraryMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
        item.setBusinessKey(businessKey);
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public PageInfo<Object> listPagedData(String uiSref,Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        String userLogin = params.get("userLogin").toString();
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
/*        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));*/

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        if (deptIdList.size()==0){
            //??????????????????deptIds
            int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
            List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(headDeptId);
            params.put("deptIdList",deptIds);
        }else {
            if(deptIdList.contains(1)){
                deptIdList.add(0);
            }
            params.put("deptIdList",deptIdList);
        }

        params.put("isLikeSelect",true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessContractLibraryMapper.selectAll(params));
        List<FiveBusinessContractLibraryDto> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessContractLibrary item=(FiveBusinessContractLibrary) p;
            FiveBusinessContractLibraryDto dto = getDto(item);
            list.add(dto);
        });
        pageInfo.setList(list.stream().sorted(Comparator.comparing(FiveBusinessContractLibrary::getSignTime).reversed()).collect(Collectors.toList()));
        return pageInfo;
    }

    public PageInfo<Object> listPagedDataCommon(String uiSref,Map<String,Object> params, int pageNum, int pageSize) {
        //?????? ??????????????????????????????
        uiSref = "five.businessContractLibrary";
        params.put("deleted",false);
        String userLogin = params.get("userLogin").toString();
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        //??????????????????????????????????????? ?????????????????????????????????
       /* List<Integer> deptIdList = hrEmployeeSysService.getMyDeptListOa(userLogin, uiSref);
        if(deptIdList.size()==0){
            params.put("creator", userLogin);
        }else{
            params.put("deptIdList", deptIdList);
        }*/
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        if (deptIdList.size()==0){
            //??????????????????deptIds
            int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
            List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(headDeptId);
            params.put("deptIdList",deptIds);
        }else {
            params.put("deptIdList",deptIdList);
        }
        params.put("isLikeSelect",true);
        //???????????????????????????
        params.put("open",true);
        //????????????????????????????????????
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessContractLibraryMapper.selectAll(params)
        .stream().filter(p->Strings.isNullOrEmpty(p.getContractName())).collect(Collectors.toList()));
        List<FiveBusinessContractLibraryDto> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessContractLibrary item=(FiveBusinessContractLibrary) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list.stream().sorted(Comparator.comparing(FiveBusinessContractLibrary::getSignTime).reversed()).collect(Collectors.toList()));
        return pageInfo;
    }
    //???????????????????????? ??????
    public List<FiveBusinessContractLibrary> selectNotHaveContract(int id ,String userLogin) {
        List<FiveBusinessContractLibrary>  fiveBusinessContractLibraries =new ArrayList<>();
        //?????????????????? ??????
        if(id!=0){
            FiveBusinessContractLibrary fiveBusinessContractLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(id);
            fiveBusinessContractLibraries.add(fiveBusinessContractLibrary);
        }
        Map map = new HashMap();
        map.put("deleted", false);
        //??????????????????????????????????????? ?????????????????????????????????
        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, "five.businessContractLibrary");
        if(deptIdList.size()==0){
            map.put("creator", userLogin);
        }else{
            map.put("deptIdList", deptIdList);
        }
        fiveBusinessContractLibraries.addAll(fiveBusinessContractLibraryMapper.selectAll(map)
                .stream().filter(p -> p.getContractId().equals(0)).filter(p->p.getSubpackageId()==0).filter(p->!p.getMain()).collect(Collectors.toList()));

        return fiveBusinessContractLibraries;

    }
    //???????????????
    public List<FiveBusinessContractLibrary> selectMainContract(String contractType) {
        Map map = new HashMap();
        map.put("deleted", false);
        map.put("contractType",contractType);
        List<FiveBusinessContractLibrary>  fiveBusinessContractLibraries = fiveBusinessContractLibraryMapper.selectAll(map)
                .stream().filter(p ->!p.getMain()).filter(p->p.getSubpackageId()==0).collect(Collectors.toList());
        return fiveBusinessContractLibraries;
    }
    //?????????????????????
    public List<FiveBusinessContractLibrary> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList2(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveBusinessContractLibraryMapper.selectAll(params).stream().filter(p->p.getSubpackageId()==0).collect(Collectors.toList());
    }
    //?????????????????? ??????
    public List<FiveBusinessContractLibrary> selectAllNotHaveStampTax(int contractId,String userLogin, String uiSref) {
        List<FiveBusinessContractLibrary> list = new ArrayList<>();
        if(contractId!=0){
            list.add(fiveBusinessContractLibraryMapper.selectByPrimaryKey(contractId));
        }
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);//getMyDeptList() getMyDeptListOa()  ??????2????????? ??????????????????
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        List<FiveBusinessContractLibrary> libraries=fiveBusinessContractLibraryMapper.selectAll(params).stream().filter(p->p.getStampTaxId()==0).collect(Collectors.toList());
       for(FiveBusinessContractLibrary library:libraries){
           list.add(library);
       }
        return  list;
    }
    //??????????????????
    public List<FiveBusinessContractLibrary> selectAllByIncome(int contractLibraryId) {
        Map map = new HashMap();
        map.put("deleted", false);

        List<FiveBusinessContractLibrary>  fiveBusinessContractLibraries = fiveBusinessContractLibraryMapper.selectAll(map)
                .stream().collect(Collectors.toList());
       /* //?????????????????? ??????
        if(contractLibraryId!=0){
            FiveBusinessContractLibrary fiveBusinessContractLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(contractLibraryId);
            fiveBusinessContractLibraries.add(fiveBusinessContractLibrary);
        }*/
        return fiveBusinessContractLibraries;

    }
    //??????????????????
    public List<BusinessSubpackage> selectSubpackage(String userLogin, String uiSref,int libraryId) {
        Map params=Maps.newHashMap();
/*        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        List<BusinessSubpackage> list = new ArrayList<>();
        FiveBusinessContractLibraryDto contractLibrary = getModelById(libraryId);
        List<Integer> subpackageIds = MyStringUtil.getIntList(contractLibrary.getSubpackageIds());
        for(int id:subpackageIds){
            BusinessSubpackage businessSubpackage = businessSubpackageMapper.selectByPrimaryKey(id);
            list.add(businessSubpackage);
        }
        return list;
    }
    //?????? ????????????
    public List<FiveBusinessContractReview> selectMainReviewByIds(String userLogin, String uiSref,int libraryId) {
        Map params=Maps.newHashMap();
        List<FiveBusinessContractReview> list = new ArrayList<>();
        FiveBusinessContractLibraryDto contractLibrary = getModelById(libraryId);
        List<Integer> mainReviewIds = MyStringUtil.getIntList(contractLibrary.getMainReviewIds());
        for(int id:mainReviewIds){
            FiveBusinessContractReview fiveBusinessContractReview =fiveBusinessContractReviewMapper.selectByPrimaryKey(id);
            list.add(fiveBusinessContractReview);
        }
        return list;
    }
    //?????? ????????????
    public List<BusinessIncome> selectIncome(String userLogin, String uiSref,int libraryId) {
        Map params=Maps.newHashMap();
/*        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        List<BusinessIncome> list = new ArrayList<>();
        FiveBusinessContractLibraryDto contractLibrary = getModelById(libraryId);
        List<Integer> incomeIds = MyStringUtil.getIntList(contractLibrary.getIncomeIds());
        for(int id:incomeIds){
            BusinessIncomeDto businessIncome = businessIncomeService.getModelById(id);
            list.add(businessIncome);
        }
        return list;
    }
    //?????? ??????
    public List<FiveFinanceInvoice> selectInvoice(String userLogin, String uiSref, int libraryId) {
        Map params=Maps.newHashMap();
/*        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);*/
        List<FiveFinanceInvoice> list = new ArrayList<>();
        FiveBusinessContractLibraryDto contractLibrary = getModelById(libraryId);
        List<Integer> invoiceIds = MyStringUtil.getIntList(contractLibrary.getInvoiceIds());
        for(int id:invoiceIds){
            FiveFinanceInvoice fiveFinanceInvoice = fiveFinanceInvoiceMapper.selectByPrimaryKey(id);
            list.add(fiveFinanceInvoice);
        }
        return list;
    }


    public List<Map> listMapData() {
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map map = new LinkedHashMap();
            map.put("       ????????????       ","");
            map.put("  ?????????  ", "");
            map.put(" ???????????? ", "");
            map.put(" ???????????? ", "");
            map.put(" ???????????? (????????? 2021.01.01 ????????????)", "");
            map.put(" ???????????? ", "");
            map.put(" ?????????(??????) ", "");
            map.put(" ????????? ", "");
            map.put(" ?????? ", "");
            list.add(map);
        }
        return list;
    }
    //???????????? ???????????? ???????????????
    public List<Map> statisticalData() {
        List<Map> list = new ArrayList<>();

        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE, "????????????");
        //?????????????????? ?????? ???????????????????????? ???deptName???contractType???contractMoney???
        List<Map<String,Object>> data = new ArrayList<>();
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(params);
        for(FiveBusinessContractLibrary contractLibrary:librarys){
            if(contractLibrary.getDeptId()==0){
                continue;
            }
            //??????????????????????????????
            List<Map<String,Object>> filterDeptData = checkListMap(data, "deptName", selectEmployeeService.getHeadDeptName(contractLibrary.getDeptId()));
            //???????????????????????????
            List<Map<String,Object>> filterTypeData = checkListMap(filterDeptData, "contractType", contractLibrary.getContractType());
            if(filterTypeData.size()>0){
                for(Map map:data){
                    if(map.get("deptName").equals(filterTypeData.get(0).get("deptName"))&&
                            map.get("contractType").equals(filterTypeData.get(0).get("contractType"))){
                        map.put("contractMoney",MyStringUtil.getNewAddMoney(map.get("contractMoney").toString(),
                                contractLibrary.getContractMoney()));
                    }
                }
            }else{
                Map map = new HashMap();
                map.put("deptName",selectEmployeeService.getHeadDeptName(contractLibrary.getDeptId()));
                map.put("contractType",contractLibrary.getContractType());
                map.put("contractMoney",contractLibrary.getContractMoney());
                data.add(map);
            }
        }

        //?????????????????????
        List<String> deptNames = new ArrayList<>();
        for(Map map:data){
            if(!deptNames.contains(map.get("deptName").toString())){
                deptNames.add(map.get("deptName").toString());
            }
        }

        for(String deptName:deptNames){
            Map result = new LinkedHashMap();
            result.put("????????????",deptName);
            List<Map<String,Object>> filterDeptData = checkListMap(data, "deptName", deptName);
            for (CommonCodeDto commonCodeDto:commonCodes) {
                List<Map<String,Object>> filterTypeData = checkListMap(filterDeptData, "contractType", commonCodeDto.getName());
                if(filterTypeData.size()>0){
                    result.put(commonCodeDto.getName()+"(??????)",MyStringUtil.moneyToString(filterTypeData.get(0).get("contractMoney").toString(),6));
                }else{
                    result.put(commonCodeDto.getName()+"(??????)","");
                }
            }
            list.add(result);
        }

        return list;
    }




    public String insertBatch(InputStream inputStream, String userLogin) throws IOException {
        HrEmployeeDto dto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        int updateNum = 0;
        int insertNum = 0;
        String contractNoA = "";

        for (int i = 0; i < list.size(); i++) {
            /*   try {*/
            Map map = list.get(i);
            FiveBusinessContractLibrary item = new FiveBusinessContractLibrary();

            //????????????
            String contractName = map.getOrDefault(1, "").toString();
            item.setContractName(contractName);
            if (Strings.isNullOrEmpty(contractName)) {
                Assert.state(false, "???" + (i + 1) + "????????? ??????????????????");
            }
            item.setProjectName(contractName);//????????????
            item.setRecordProjectName(contractName);//??????????????????
            //?????????
            String contractNo = map.getOrDefault(2, "").toString();
            if (Strings.isNullOrEmpty(contractNo)) {
                Assert.state(false, "???" + (i + 1) + "????????? ???????????????");
            }
            // if (contractNo.contains("-") || contractNo.contains("F")||contractNo.contains("G")||contractNo.contains("A")) {
            item.setContractNo(contractNo);
                /*} else {
                    item.setContractNo(new BigDecimal(contractNo).toPlainString());
                }*/
            contractNoA = item.getContractNo();

            //??????????????????
            Map map1 = new HashMap();
            map1.put("deleted", false);
            map1.put("contractNo", item.getContractNo());
            Boolean flag = true;
            if (fiveBusinessContractLibraryMapper.selectAll(map1).size() > 0) {
                item = fiveBusinessContractLibraryMapper.selectAll(map1).get(0);
                flag = false;
            }

            String deptName = map.getOrDefault(3, "").toString();
            HrDept hrDept = hrDeptService.selectByName(deptName);
            if (hrDept != null) {
                item.setDeptName(hrDept.getName());
                item.setDeptId(hrDept.getId());
            } else if (deptName.equals("????????????")) {
                item.setDeptName("????????????????????????????????????");
                item.setDeptId(1);
            } else if (deptName.contains("217")) {
                item.setDeptName("??????217");
                item.setDeptId(0);
            } else {
                Assert.state(false, "???????????????" + deptName + " ???????????????????????????????????????" + item.getContractNo());
            }
            if (!Strings.isNullOrEmpty(map.getOrDefault(4, "").toString())) {
                item.setContractType(map.getOrDefault(4, "").toString());
            } else {
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }
            try {
                String[] split = map.getOrDefault(5, "").toString().split("\\.");
                /*if (Integer.parseInt(split[0]) > 90) {
                    item.setSignTime("19" + split[0] + "-" + split[1] + "-" + split[2]);//????????????
                } else {
                    item.setSignTime("20" + split[0] + "-" + split[1] + "-" + split[2]);//????????????
                }*/
                item.setSignTime(split[0] + "-" + split[1] + "-" + split[2]);//????????????
            } catch (Exception e) {
                Assert.state(false, "???" + (i + 1) + "????????? ???????????????????????? ????????????" + contractNoA);
            }

            item.setProjectNo("");//?????????
            //item.setConstructionArea(map.getOrDefault(12,"").toString());//????????????
            item.setCustomerName(map.getOrDefault(6, "").toString());//????????????
            item.setLinkMan("");//?????????
            item.setLinkTel("");
            item.setCustomerAddress("");
            item.setContractMoney(MyStringUtil.moneyToString(map.getOrDefault(7, "").toString()));

            //??????	2623
            if (map.getOrDefault(8, "").toString().equals("")) {
                item.setCreator("2623");
                item.setCreatorName("?????????");
            } else {
                //???????????????????????????
                if (hrEmployeeSysService.selectByUserName(map.getOrDefault(8, "").toString()).equals("")) {
                    item.setCreator("2623");
                    item.setCreatorName("?????????");
                } else {
                    item.setCreator(hrEmployeeSysService.selectByUserName(map.getOrDefault(8, "").toString()));
                    item.setCreatorName(map.getOrDefault(8, "").toString());
                }
            }

            item.setConstructionScale("");
            item.setReviewLevel("??????");
            item.setInOrOut("");
            item.setMilitaryMark("??????");
            item.setInvestSource("????????????");
            item.setForeignTradeMark("????????????");
            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            item.setBackletter(false);//????????????
            item.setMain(false);
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());

            item.setRemark("????????????");
            ModelUtil.setNotNullFields(item);
            if (flag) {
                fiveBusinessContractLibraryMapper.insert(item);
                String businessKey = EdConst.FIVE_BUSINESSCONTRACTLIBRARY + "_" + item.getId();
                item.setBusinessKey(businessKey);
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
                insertNum++;
            } else {
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
                updateNum++;
            }

           /* } catch (Exception e) {
                Assert.state(false, contractNoA + ":??????" + updateNum + "   ????????????");
            }*/
        }
        return insertNum + "," + updateNum;
    }

    //??????????????????
    public String subIncomeMoney(int incomeId) {
        BusinessIncome income = businessIncomeMapper.selectByPrimaryKey(incomeId);

        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(income.getContractLibraryId());
        library.setContractIncomeMoney(MyStringUtil.getNewSubMoney(library.getContractIncomeMoney(),income.getIncomeMoney(),8));
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);

        income.setContractIncomeMoney(library.getContractIncomeMoney());
        businessIncomeMapper.updateByPrimaryKey(income);

        return "?????????"+income.getContractName()+" ?????????????????????";
    }
    //??????????????????
    public String getIncomeMoney(int incomeId) {
        BusinessIncome income = businessIncomeMapper.selectByPrimaryKey(incomeId);
        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(income.getContractLibraryId());
        library.setContractIncomeMoney(MyStringUtil.getNewAddMoney(library.getContractIncomeMoney(),income.getIncomeMoney()));
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);

        income.setContractIncomeMoney(library.getContractIncomeMoney());
        businessIncomeMapper.updateByPrimaryKey(income);
        return "?????????"+income.getContractName()+" ?????????????????????";
    }


    //?????? data ??? map ??? key ??? ?????? value ?????????   flag ????????????index
    private List<Map<String,Object>> checkListMap(List<Map<String,Object>> data,String key,String value){
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i =0;i<data.size();i++){
            Map map = data.get(i);
            if(map.get(key).equals(value)){
                list.add(map);
            }
        }
        return list;
    }

    public void changeOpen(int id,String userLogin){
        FiveBusinessContractLibrary model = fiveBusinessContractLibraryMapper.selectByPrimaryKey(id);
        if(model.getOpen()){
            model.setOpen(false);
        }else {
            model.setOpen(true);
        }
        fiveBusinessContractLibraryMapper.updateByPrimaryKey(model);
        //????????????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractLibraryId",model.getId());
        List<FiveBusinessContractReview> contractReviews = fiveBusinessContractReviewMapper.selectAll(map);
        if(contractReviews.size()>0){
            FiveBusinessContractReview contractReview =contractReviews.get(0);
            contractReview.setOpen(model.getOpen());
            fiveBusinessContractReviewMapper.updateByPrimaryKey(contractReview);
        }
    }
}
