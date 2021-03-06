package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonAttachMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.CommonDirDto;
import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.*;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.FiveBusinessContractReviewDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;

import com.cmcu.mcc.finance.dao.FiveFinanceStampTaxMapper;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamineListDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveBusinessContractReviewService extends BaseService {
    @Resource
    FiveBusinessContractReviewMapper fiveBusinessContractReviewMapper;
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
    FiveBusinessContractReviewDetailMapper fiveBusinessContractReviewDetailMapper;
    @Autowired
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    BusinessRecordMapper businessRecordMapper;
    @Autowired
    BusinessPreContractMapper businessPreContractMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    HandleFormService handleFormService;
    @Resource
    FiveFinanceStampTaxMapper fiveFinanceStampTaxMapper;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    CommonFileService commonFileService;
    @Resource
    CommonDirService commonDirService;
    @Resource
    CommonAttachService commonAttachService;


    public void remove(int id,String userLogin){
        FiveBusinessContractReview model = fiveBusinessContractReviewMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin),"??????????????????"+model.getCreatorName()+"??????");
        //??????????????? ?????? ??????????????????
        if(model.getRecordId()!=0){
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
            businessRecord.setStatus("0");
            businessRecord.setContractReviewIds(MyStringUtil.getNewStringId(businessRecord.getContractReviewIds(),model.getId()));
            businessRecordMapper.updateByPrimaryKey(businessRecord);
        }
        //??????????????? ??????????????? ??????
        if(model.getPreContractId()!=0){
            BusinessPreContract businessPreContract = businessPreContractMapper.selectByPrimaryKey(model.getPreContractId());
            businessPreContract.setReviewContractId(0);
            businessPreContractMapper.updateByPrimaryKey(businessPreContract);
        }
        //???????????? ???????????? ??????
        if(model.getMainContractLibraryId()!=0){
            FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getMainContractLibraryId());

            oldLibrary.setMainReviewIds(MyStringUtil.getNewStringId(oldLibrary.getMainReviewIds(),id));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
        }

        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);


    }
    public void changeOpen(int id,String userLogin){
        FiveBusinessContractReview model = fiveBusinessContractReviewMapper.selectByPrimaryKey(id);
        if(model.getOpen()){
            model.setOpen(false);
        }else {
            model.setOpen(true);
        }
        fiveBusinessContractReviewMapper.updateByPrimaryKey(model);
        //?????????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractReviewId",model.getId());
        List<FiveBusinessContractLibrary> contractLibrarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(contractLibrarys.size()>0){
            FiveBusinessContractLibrary contractLibrary =contractLibrarys.get(0);
            contractLibrary.setOpen(model.getOpen());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(contractLibrary);
        }
    }
    public void changeOpenStamp(int id,String userLogin){
        FiveBusinessContractReview model = fiveBusinessContractReviewMapper.selectByPrimaryKey(id);
        if(model.getOpenStamp()){
            model.setOpenStamp(false);
        }else {
            model.setOpenStamp(true);
        }
        fiveBusinessContractReviewMapper.updateByPrimaryKey(model);
        //?????????????????????
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractReviewId",model.getId());
        List<FiveBusinessContractLibrary> contractLibrarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(contractLibrarys.size()>0){
            FiveBusinessContractLibrary contractLibrary =contractLibrarys.get(0);
            contractLibrary.setOpenStamp(model.getOpenStamp());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(contractLibrary);
        }
    }


    public void update(FiveBusinessContractReviewDto dto) throws ParseException {
        FiveBusinessContractReview model = fiveBusinessContractReviewMapper.selectByPrimaryKey(dto.getId());
        model.setDeptId(dto.getDeptId());
        model.setDeptName(dto.getDeptName());
        model.setProjectName(dto.getProjectName());
        model.setStageName(dto.getStageName());
        model.setContractNo(dto.getContractNo());
        if(StringUtils.isEmpty(dto.getContractMoney())){
            model.setContractMoney(MyStringUtil.moneyToString("0"));
        }

        /*if(StringUtils.isEmpty(dto.getContractMoney())){
            model.setContractMoney(MyStringUtil.moneyToString(dto.getInvestMoney()));
        }else{
            model.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney()));
        }*/
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
        //?????????????????????
        if(dto.getMainContractLibraryId()!=0){
            //????????????????????? ??????
            if(model.getRecordId()!=0){
                BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
                businessRecord.setStatus("0");
                businessRecord.setContractReviewIds(MyStringUtil.getNewStringId(businessRecord.getContractReviewIds(),model.getId()));
                businessRecordMapper.updateByPrimaryKey(businessRecord);
                model.setRecordId(0);
                model.setProjectName("");
                model.setProjectNo("");
                model.setContractNo("");
            }
            //???????????????????????? ??????
            if(model.getMainContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getMainContractLibraryId());
                oldLibrary.setMainReviewIds(MyStringUtil.getNewStringId(oldLibrary.getMainReviewIds(),model.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
            }
            //????????? ????????????
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getMainContractLibraryId());
            library.setMainReviewIds(library.getMainReviewIds()+","+model.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        model.setMainContractLibraryId(dto.getMainContractLibraryId());
        model.setMainContractLibraryName(dto.getMainContractLibraryName());
        model.setMainContractLibraryNo(dto.getMainContractLibraryNo());

        model.setBackletter(dto.getBackletter());
        model.setBackletterMoney(MyStringUtil.moneyToString(dto.getBackletterMoney()));


        //???????????????????????? ????????????????????????
        if(dto.getRecordId()!=0&&!dto.getMain()){
            //???????????????????????? ??????
            if(model.getMainContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getMainContractLibraryId());
                oldLibrary.setMainReviewIds(MyStringUtil.getNewStringId(oldLibrary.getMainReviewIds(),model.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);

                model.setMainContractLibraryId(0);
                model.setMainContractLibraryNo("");
                model.setMainContractLibraryName("");
                model.setContractNo("");
            }
            //????????????????????? ??????
            if(model.getRecordId()!=0){
                BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
                businessRecord.setStatus("0");
                businessRecord.setContractReviewIds(MyStringUtil.getNewStringId(businessRecord.getContractReviewIds(),model.getId()));
                businessRecordMapper.updateByPrimaryKey(businessRecord);
            }
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(dto.getRecordId());
            businessRecord.setStatus("1");
            businessRecord.setContractReviewIds(businessRecord.getContractReviewIds()+","+dto.getId());

            businessRecordMapper.updateByPrimaryKey(businessRecord);
            model.setRecordId(dto.getRecordId());
        }

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
        model.setDeptReviewUser(dto.getDeptReviewUser());
        model.setDeptReviewUserName(dto.getDeptReviewUserName());
        model.setTotalDesigner(dto.getTotalDesigner());
        model.setTotalDesignerName(dto.getTotalDesignerName());

        model.setContractName(dto.getContractName());

        model.setProjectManager(dto.getProjectManager());
        model.setProjectManagerName(dto.getProjectManagerName());

        model.setBusinessChargeLeader(dto.getBusinessChargeLeader());
        model.setBusinessChargeLeaderName(dto.getBusinessChargeLeaderName());

        model.setProjectChargeMan(dto.getProjectChargeMan());
        model.setProjectChargeManName(dto.getProjectChargeManName());

        model.setPlanSignTime(dto.getPlanSignTime());
        model.setContractAttachUrl(dto.getContractAttachUrl());
        model.setTaxType(dto.getTaxType());

        //?????????????????????
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"?????????????????????");
        for(CommonCodeDto codeDto:commonCodes){
            if(codeDto.getName().equalsIgnoreCase(model.getTaxType())){
                if(codeDto.getName().equalsIgnoreCase("??????")&&StringUtils.isNotEmpty(dto.getTaxNum())){
                    model.setTaxNum(dto.getTaxNum());
                }else{
                    model.setTaxNum(codeDto.getCode());
                }
                model.setStampTaxMoney(MyStringUtil.getNewMultiplyMoney1(model.getContractMoney(),model.getTaxNum()));
            }
        }


        model.setMain(dto.getMain());
        model.setAttach(dto.getAttach());
        model.setRemark(dto.getRemark());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);

        Map variables=Maps.newHashMap();
        //???????????? 30??????????????? ?????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2020-11-30");
        if(model.getGmtCreate().before(date)){
            variables.put("flag",model.getReviewLevel());
        }else{
            if(model.getReviewLevel().equals("?????????")){
                variables.put("flag",true);
            }else{
                variables.put("flag",false);
            }
        }
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(model.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(model.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }


        variables.put("totalDesigner", model.getTotalDesigner());
        variables.put("processDescription", "???????????????:"+model.getContractName()+"("+model.getContractNo()+")");
        variables.put("reviewUsers", MyStringUtil.getStringList(model.getReviewUser()));
        variables.put("deptReviewUsers", MyStringUtil.getStringList(model.getDeptReviewUser()));
        variables.put("businessChargeLeader", model.getBusinessChargeLeader());//???????????????????????????

        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveBusinessContractReviewMapper.updateByPrimaryKey(model);
    }

    public FiveBusinessContractReviewDto getModelById(int id){
        return getDto(fiveBusinessContractReviewMapper.selectByPrimaryKey(id));
    }

    public FiveBusinessContractReviewDto getDto(FiveBusinessContractReview item) {
        FiveBusinessContractReviewDto dto = FiveBusinessContractReviewDto.adapt(item);
        if (item.getDeptId() != 0) {
            dto.setHeadDeptId(selectEmployeeService.getHeadDeptId(item.getDeptId()));
        }

        try {
            dto.setProcessName("?????????");
            if (!dto.getProcessEnd()){
                CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
                if (customProcessInstance.isFinished() || customProcessInstance == null) {
                    dto.setProcessEnd(true);
                    fiveBusinessContractReviewMapper.updateByPrimaryKey(dto);
                    //????????????????????????
                    insertStampTax(item.getId());
                    //???????????????
                    if (item.getContractLibraryId() != 0) {
                        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                        library.setContractMoney(item.getContractMoney());
                        fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                    }
                }else {
                    dto.setProcessName(customProcessInstance.getCurrentTaskName());
                }

                if (dto.getProcessName().contains("??????????????????") || dto.getProcessName().contains("??????")) {
                    //??????????????????
                    insertContractLibrary(item.getId());
                }
                if (dto.getProcessName().contains("?????????")) {
                    if (dto.getContractLibraryId() != 0) {
                        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getContractLibraryId());
                        //??????????????????????????????
                        commonFileService.insertByUrl(library.getBusinessKey(), dto.getContractAttachUrl(), dto.getCreator());
                    }
                }
            }

        } catch (Exception e) {
            //????????????6???
            dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(), 6));
            dto.setInvestMoney(MyStringUtil.moneyToString(dto.getInvestMoney(), 6));
            return dto;
        }
        //????????????6???
        dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(), 6));
        dto.setInvestMoney(MyStringUtil.moneyToString(dto.getInvestMoney(), 6));
        return dto;
    }

    public int getNewModel(String userLogin){

        FiveBusinessContractReview item=new FiveBusinessContractReview();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        //??????????????????
        List<String> userLogins = hrEmployeeService.selectUserByRoleNames("??????????????????");
        String reviewUser ="";
        String reviewUserName ="";
        for(String login:userLogins){
            HrEmployeeDto employee = hrEmployeeService.getModelByUserLogin(login);
            reviewUser = reviewUser+login+",";
            reviewUserName = reviewUserName+employee.getUserName()+",";
        }
        item.setReviewUser(reviewUser);
        item.setReviewUserName(reviewUserName);
        item.setReviewLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setProjectNature(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setInOrOut(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"?????????").toString());
        item.setMilitaryMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setCivilMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setForeignTradeMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setProjectMajorTypeSecond(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setProjectMajorTypeThird(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setInvestSource(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setTaxType("");
        item.setMain(false);
        item.setMainContractLibraryId(0);
        //item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????"));
        item.setContractType("????????????");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        item.setSignTime(sdf.format(new Date()));
        item.setPlanSignTime(sdf.format(new Date()));
        item.setCustomerId(0);
        item.setBid(false);
        item.setLegalPerson(false);
        item.setEleLegalPerson(false);
        item.setBusinessLegalPerson(false);
        item.setEleBusinessLegalPerson(false);
        item.setCommonSeal(false);
        item.setAttach(false);

        item.setOpen(true);
        item.setOpenStamp(false);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveBusinessContractReviewMapper.insert(item);
        //??????????????? ????????????????????????
        if(selectEmployeeService.getUserListByRoleName("??????????????????").contains(userLogin)){
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTREVIEWEASY+ "_" + item.getId();
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            item.setBusinessKey(businessKey);

            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESSCONTRACTREVIEWEASY,item.getBusinessKey(), variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            fiveBusinessContractReviewMapper.updateByPrimaryKey(item);
        }else{
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTREVIEW+ "_" + item.getId();
            Map variables = Maps.newHashMap();
            List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
            if(businessMen.size()!=0){
                variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
                variables.put("businessMenFlag",true);
            }else{
                variables.put("businessMenFlag",false);
            }
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "???????????????");
            variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),1,true));//????????????????????? ?????????
            variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//?????????????????????
            variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//???????????????????????????
            variables.put("opratorLeader", selectEmployeeService.getDeptChargeMen(29));//???????????????
            variables.put("lawReview", selectEmployeeService.getUserListByRoleName("????????????"));//????????????

            item.setBusinessKey(businessKey);

            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESSCONTRACTREVIEW,item.getBusinessKey(), variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            fiveBusinessContractReviewMapper.updateByPrimaryKey(item);
        }



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
        initDetail(item.getId(),baseNames);
        return item.getId();
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

    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum,int pageSize ,String uiSref) {
        params.put("deleted",false);
        String userLogin = params.get("userLogin").toString();
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        params.put("isLikeSelect","true");

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessContractReviewMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessContractReview item=(FiveBusinessContractReview) p;
            FiveBusinessContractReviewDto dto = getDto(item);
          //  if (dto.getAttendUser().contains(userLogin)){
                list.add(dto);
           // }
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public Object listDetailById(int contractReviewId) {
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractReviewId",contractReviewId);
        return fiveBusinessContractReviewDetailMapper.selectAll(map);
    }

    public Object getDetailModelById(int id) {
        return fiveBusinessContractReviewDetailMapper.selectByPrimaryKey(id);
    }

    public Object getNewDetailModel(int contractReviewId) {
        FiveBusinessContractReviewDetail detail = new FiveBusinessContractReviewDetail();
        detail.setContractReviewId(contractReviewId);
        detail.setSeq(fiveBusinessContractReviewMapper.selectAll(new HashMap()).size()+"");
        detail.setDeleted(false);
        detail.setGmtCreate(new Date());
        detail.setGmtModified(new Date());
        ModelUtil.setNotNullFields(detail);
        fiveBusinessContractReviewDetailMapper.insert(detail);
        return detail.getId();
    }

    public void removeDetail(int id, String userLogin) {
        FiveBusinessContractReviewDetail detail = fiveBusinessContractReviewDetailMapper.selectByPrimaryKey(id);
        detail.setDeleted(true);
        detail.setGmtModified(new Date());
        fiveBusinessContractReviewDetailMapper.updateByPrimaryKey(detail);
    }

    public void updateDetail(FiveBusinessContractReviewDetail item) {
        fiveBusinessContractReviewDetailMapper.updateByPrimaryKey(item);
    }

    public int addCustomer(String recordId, String userLogin) {
        FiveBusinessContractReview item = getModelById(Integer.parseInt(recordId));
        Map map =new HashMap();
        map.put("name",item.getCustomerName());
        map.put("deleted",false);
        BusinessCustomer bc;
        List<BusinessCustomer> businessCustomers = businessCustomerMapper.selectAll(map);
        if(businessCustomers.size()>0){
            Assert.state(false,"??????????????????");
            bc = businessCustomers.get(0);
/*            bc.setCode(item.getCustomerCode());
            bc.setName(item.getCustomerName());
            bc.setAddress(item.getCustomerAddress());
            bc.setLinkMan(item.getLinkMan());
            bc.setLinkTitle(item.getLinkTitle());
            bc.setLinkTel(item.getLinkTel());
            bc.setGmtModified(new Date());
            businessCustomerMapper.updateByPrimaryKey(bc);*/
        }else{
            bc = new BusinessCustomer();
            bc.setCode(item.getCustomerCode());
            bc.setName(item.getCustomerName());
            bc.setAddress(item.getCustomerAddress());
            bc.setLinkMan(item.getLinkMan());
            bc.setLinkTitle(item.getLinkTitle());
            bc.setLinkTel(item.getLinkTel());

            HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
            bc.setDeptId(hrEmployeeDto.getDeptId());
            bc.setDeptName(hrEmployeeDto.getDeptName());
            bc.setCreator(userLogin);
            bc.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
            bc.setGmtCreate(new Date());
            bc.setGmtModified(new Date());
            ModelUtil.setNotNullFields(bc);
            businessCustomerMapper.insert(bc);

            bc.setBusinessKey(EdConst.FIVE_BUSINESS_CUSTOMER+"_" + item.getId());
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "??????????????????");
            variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("?????????????????????(??????)"));//?????????????????????????????????
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_CUSTOMER,bc.getBusinessKey(), variables, MccConst.APP_CODE);
            bc.setProcessInstanceId(processInstanceId);
            businessCustomerMapper.updateByPrimaryKey(bc);
            //???????????????id ??????????????????
            item.setCustomerId(bc.getId());
            fiveBusinessContractReviewMapper.updateByPrimaryKey(item);
        }

        return bc.getId();
    }


    public String insertStampTax(int contractReviewId) {
        FiveBusinessContractReview review = fiveBusinessContractReviewMapper.selectByPrimaryKey(contractReviewId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractId",review.getContractLibraryId());
        List<FiveFinanceStampTax> stampTaxs = fiveFinanceStampTaxMapper.selectAll(map);
        if(stampTaxs.size()>0){
            return "????????? "+review.getContractName()+" ??????????????????";
        }else{
            FiveFinanceStampTax stampTax = new FiveFinanceStampTax();
            //?????????
            stampTax.setCreator("2623");
            stampTax.setCreatorName("?????????");
            stampTax.setContractId(review.getContractLibraryId());
            stampTax.setContractName(review.getContractName());
            stampTax.setContractNo(review.getContractNo());
            stampTax.setCustomerName(review.getCustomerName());
            stampTax.setProjectName(review.getContractName());
            stampTax.setProjectNo(review.getProjectNo());
            stampTax.setSignTime(review.getSignTime());
            stampTax.setTaxType(review.getTaxType());
            stampTax.setContractMoney(review.getContractMoney());
            stampTax.setTaxNum(review.getTaxNum());
            stampTax.setStampTaxMoney(review.getStampTaxMoney());

            stampTax.setContractDeptId(review.getDeptId());
            stampTax.setContractDeptName(review.getDeptName());

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
            commonFileService.insertByUrl(stampTax.getBusinessKey(),review.getContractAttachUrl(),review.getCreator());


            return "????????? "+review.getContractName()+" ????????????????????????";
        }
    }

    public String insertContractLibrary(int contractReviewId) {
        FiveBusinessContractReview review = fiveBusinessContractReviewMapper.selectByPrimaryKey(contractReviewId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractReviewId",contractReviewId);
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(librarys.size()>0){
            return "????????? "+review.getProjectName()+" ??????????????????";
        }else{
            if(review.getPreContractId()==0){
                FiveBusinessContractLibrary library = new FiveBusinessContractLibrary();
                library.setProjectName(review.getContractName());
                library.setRecordProjectName(review.getProjectName());

                library.setContractName(review.getContractName());
                library.setProjectManager(review.getProjectManager());
                library.setProjectManagerName(review.getProjectManagerName());
                library.setMainContractLibraryName(review.getMainContractLibraryName());
                library.setMainContractLibraryNo(review.getMainContractLibraryNo());
                library.setMainContractLibraryId(review.getMainContractLibraryId());

                library.setContractReviewId(review.getId());
                library.setContractNo(review.getContractNo());
                library.setDeptId(review.getDeptId());
                library.setDeptName(review.getDeptName());
                library.setInvestMoney(MyStringUtil.moneyToString(review.getInvestMoney()));
                library.setContractMoney(MyStringUtil.moneyToString(review.getContractMoney()));

                library.setInvestSource(review.getInvestSource());
                library.setProjectNature(review.getProjectNature());
                library.setContractType(review.getContractType());
                library.setSignTime(review.getSignTime());
                library.setConstructionArea(review.getConstructionArea());
                library.setConstructionScale(review.getConstructionScale());
                library.setReviewLevel(review.getReviewLevel());
                library.setReviewTime(review.getReviewTime());
                library.setMainDescription(review.getMainDescription());
                library.setForeignTradeMark(review.getForeignTradeMark());
                library.setCivilMark(review.getCivilMark());
                library.setMilitaryMark(review.getMilitaryMark());
                library.setProjectMajorTypeFirst(review.getProjectMajorTypeFirst());
                library.setProjectMajorTypeSecond(review.getProjectMajorTypeSecond());
                library.setProjectMajorTypeThird(review.getProjectMajorTypeThird());
                library.setLegalPerson(review.getLegalPerson());
                library.setBusinessLegalPerson(review.getBusinessLegalPerson());
                library.setCommonSeal(review.getCommonSeal());
                library.setEleLegalPerson(review.getEleLegalPerson());
                library.setEleBusinessLegalPerson(review.getEleBusinessLegalPerson());
                library.setAttach(review.getAttach());
                library.setMain(review.getMain());
                library.setProjectNo(review.getProjectNo());

                library.setStageNames(review.getStageName());
                library.setBid(review.getBid());

                library.setShortDesc(review.getMainDescription());
                library.setGmtModified(new Date());
                library.setGmtCreate(new Date());
                library.setCreator(review.getCreator());
                library.setCustomerName(review.getCreatorName());
                library.setMain(review.getMain());
                library.setTotalDesigner(review.getTotalDesigner());
                library.setTotalDesignerName(review.getTotalDesignerName());
                library.setReviewUser(review.getReviewUser());
                library.setReviewUserName(review.getReviewUserName());

                //????????????
                library.setCustomerId(review.getCustomerId());
                library.setCustomerAddress(review.getCustomerAddress());
                library.setCustomerCode(review.getCustomerCode());
                library.setCustomerName(review.getCustomerName());
                library.setCustomerType(review.getContractType());
                library.setLinkMan(review.getLinkMan());
                library.setLinkTel(review.getLinkTel());
                library.setLinkTitle(review.getLinkTitle());

                library.setContractReviewId(contractReviewId);
                library.setContractIncomeMoney(MyStringUtil.moneyToString(""));

                library.setBusinessChargeLeader(review.getBusinessChargeLeader());
                library.setBusinessChargeLeaderName(review.getBusinessChargeLeaderName());

                library.setProjectChargeMan(review.getProjectChargeMan());
                library.setProjectChargeManName(review.getProjectChargeManName());

                library.setOpen(review.getOpen());
                library.setOpenStamp(review.getOpenStamp());
                ModelUtil.setNotNullFields(library);
                fiveBusinessContractLibraryMapper.insert(library);
                library.setBusinessKey(EdConst.FIVE_BUSINESSCONTRACTLIBRARY+"_"+library.getId());
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);

                //?????? ????????????
                review.setContractLibraryId(library.getId());
                fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
                //?????????
                commonFileService.copyFileByBusinessKey(review.getBusinessKey(),library.getBusinessKey(),0);
                return "????????? "+review.getProjectName()+"  ?????????????????????";
            }else {
                Map map1 =new HashMap();
                map1.put("deleted",false);
                map1.put("contractPreId",review.getPreContractId());
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectAll(map1).get(0);

                library.setRecordProjectName(review.getProjectName());

                library.setContractName(review.getContractName());
                library.setProjectManager(review.getProjectManager());
                library.setProjectManagerName(review.getProjectManagerName());
                library.setMainContractLibraryName(review.getMainContractLibraryName());
                library.setMainContractLibraryNo(review.getMainContractLibraryNo());
                library.setMainContractLibraryId(review.getMainContractLibraryId());
                library.setContractReviewId(review.getId());
                library.setContractNo(review.getContractNo());
                library.setDeptId(review.getDeptId());
                library.setDeptName(review.getDeptName());
                library.setInvestMoney(MyStringUtil.moneyToString(review.getInvestMoney()));
                library.setContractMoney(MyStringUtil.moneyToString(review.getContractMoney()));

                library.setInvestSource(review.getInvestSource());
                library.setProjectNature(review.getProjectNature());
                library.setContractType(review.getContractType());
                library.setSignTime(review.getSignTime());
                library.setConstructionArea(review.getConstructionArea());
                library.setConstructionScale(review.getConstructionScale());
                library.setReviewLevel(review.getReviewLevel());
                library.setReviewTime(review.getReviewTime());
                library.setMainDescription(review.getMainDescription());
                library.setForeignTradeMark(review.getForeignTradeMark());
                library.setCivilMark(review.getCivilMark());
                library.setMilitaryMark(review.getMilitaryMark());
                library.setProjectMajorTypeFirst(review.getProjectMajorTypeFirst());
                library.setProjectMajorTypeSecond(review.getProjectMajorTypeSecond());
                library.setProjectMajorTypeThird(review.getProjectMajorTypeThird());
                library.setLegalPerson(review.getLegalPerson());
                library.setBusinessLegalPerson(review.getBusinessLegalPerson());
                library.setCommonSeal(review.getCommonSeal());
                library.setEleLegalPerson(review.getEleLegalPerson());
                library.setEleBusinessLegalPerson(review.getEleBusinessLegalPerson());
                library.setAttach(review.getAttach());
                library.setTotalDesigner(review.getTotalDesigner());
                library.setTotalDesignerName(review.getTotalDesignerName());
                library.setReviewUser(review.getReviewUser());
                library.setReviewUserName(review.getReviewUserName());

                library.setStageNames(review.getStageName());
                library.setBid(review.getBid());
                library.setMain(review.getMain());
                library.setProjectNo(review.getProjectNo());

                library.setShortDesc(review.getMainDescription());
                library.setGmtModified(new Date());
                library.setGmtCreate(new Date());
                library.setCreator(review.getCreator());
                library.setCustomerName(review.getCreatorName());
                library.setMain(review.getMain());

                //????????????
                library.setCustomerId(review.getCustomerId());
                library.setCustomerAddress(review.getCustomerAddress());
                library.setCustomerCode(review.getCustomerCode());
                library.setCustomerName(review.getCustomerName());
                library.setCustomerType(review.getContractType());
                library.setLinkMan(review.getLinkMan());
                library.setLinkTel(review.getLinkTel());
                library.setLinkTitle(review.getLinkTitle());

                library.setBusinessChargeLeader(review.getBusinessChargeLeader());
                library.setBusinessChargeLeaderName(review.getBusinessChargeLeaderName());

                library.setProjectChargeMan(review.getProjectChargeMan());
                library.setProjectChargeManName(review.getProjectChargeManName());

                library.setOpen(review.getOpen());
                library.setOpenStamp(review.getOpenStamp());

                library.setContractReviewId(contractReviewId);
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                //?????? ????????????
                review.setContractLibraryId(library.getId());
                fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
                //?????????
                commonFileService.copyFileByBusinessKey(review.getBusinessKey(),library.getBusinessKey(),0);
                return "????????? "+review.getProjectName()+"  ??????????????? ?????????";
            }
        }
    }



    /**
     * XX ??????????????????2017?????????17???2018?????????18??????????????????
     * XX ????????????????????????
     * X ?????????????????? ???????????? 1 ;????????????2;	???????????????????????????????????????????????????????????????	3;	??????????????????????????????????????????4 ; ??????????????????????????????5;  ?????????	6;
     * XXX  ?????????????????????????????????????????????001??????
     * @param id
     */

    public String getContractNo(int id){
        try {
            FiveBusinessContractReviewDto fiveBusinessContractReviewDto = getModelById(id);
            String newContractNo="H";

            //???????????? 2019???  19
            String signYear = fiveBusinessContractReviewDto.getPlanSignTime().split("-")[0];
            String year = signYear.substring(2);
            //?????????????????? 2??? 01
            String deptCode= hrDeptService.getModelById(fiveBusinessContractReviewDto.getDeptId()).getDeptCode();
            //??????????????????
            List<CommonCodeDto> sysCodeDtoList = commonCodeService.listDataByCatalog("","????????????");
            String typeCode="";
            for ( CommonCode dto: sysCodeDtoList) {
                if (dto.getName().equals(fiveBusinessContractReviewDto.getContractType())){
                    typeCode=dto.getCode();
                }
            }
            //??????  ?????? ?????? ???????????? ???????????? ??????????????????
            /*if (fiveBusinessContractReviewDto.getContractNo().contains("H"+year+deptCode+typeCode)){
                return fiveBusinessContractReviewDto.getContractNo();
            }*/

            //?????? 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("contractNoHead","H"+year+deptCode+typeCode);
            List<FiveBusinessContractReview> reviews = fiveBusinessContractReviewMapper.selectAll(params);
            int size=0;
            //???????????????????????????
            if (!reviews.isEmpty()){
                for (FiveBusinessContractReview review :reviews){
                    if(!review.getId().equals(id)&&StringUtils.isNotEmpty(review.getContractNo())){
                        String contractNo = review.getContractNo();
                        //???????????? H  ?????? H
                        if(contractNo.startsWith("H")){
                            contractNo = contractNo.substring(1);
                        }
                        String maxSize = contractNo.substring(5);
                        if (size<Integer.parseInt(maxSize)){
                            size = Integer.parseInt(maxSize);
                        }
                    }
                }
            }
            size=size+1;
            String format = String.format("%03d", size);
            newContractNo=newContractNo+year+deptCode+typeCode+format;

            fiveBusinessContractReviewDto.setContractNo(newContractNo);
            update(fiveBusinessContractReviewDto);

            return newContractNo;

        }catch (Exception e){
            Assert.state(false, "???????????????????????????????????????????????????????????????");
            return "";
        }
    }

    public String getMainContractNo(int id){
        try {
            FiveBusinessContractReviewDto reviewDto = getModelById(id);
            String newContractNo=reviewDto.getMainContractLibraryNo();

            String code ="-";

            //?????? 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("mainContractNoHead",newContractNo+code);
            List<FiveBusinessContractReview> reviews = fiveBusinessContractReviewMapper.selectAll(params);
            int max=0;
            //???????????????????????????
            if (!reviews.isEmpty()){
                for (FiveBusinessContractReview review:reviews){
                    if(!review.getId().equals(reviewDto.getId())){
                        //???????????????
                        String no =review.getContractNo();
                        //???????????? ???????????????????????????10??????????????? ???????????????
                        int num =Integer.valueOf(no.substring(no.length()-1));
                        if(num>max){
                            max =num;
                        }
                    }
                }
            }
            max=max+1;
            newContractNo=newContractNo+code+max;

            //????????????????????????????????????
            List<String> specialContractNos = new ArrayList<>();
            specialContractNos.add("18401043-1");
            if(specialContractNos.contains(newContractNo)){
                max=max+1;
                newContractNo=reviewDto.getMainContractLibraryNo()+code+max;
            }

            reviewDto.setContractNo(newContractNo);
            update(reviewDto);

            return newContractNo;
        }catch (Exception e){
            Assert.state(false, "???????????????????????????");
            return "";
        }
    }

    //???????????????????????? ????????????
    public List<FiveBusinessContractReview> selectNotHaveContract(int id) {
        Map map = new HashMap();
        map.put("deleted", false);
        // map.put("processEnd",true);
        // List<BusinessPreContract>  businessPreContracts = businessPreContractMapper.selectAll(map).stream().filter(p -> p.getInputContractId().equals("0")).collect(Collectors.toList());
       /* map.put("contractId",contractId);
        if (businessPreContractMapper.selectAll(map).size()>0){
            businessPreContracts.add(businessPreContractMapper.selectAll(map).get(0));
        }*/
        List<FiveBusinessContractReview>  fiveBusinessContractReviews = fiveBusinessContractReviewMapper.selectAll(map).stream().filter(p -> p.getContractId().equals(0)).collect(Collectors.toList());
        //?????????????????? ??????
        if(id!=0){
            FiveBusinessContractReview fiveBusinessContractReview = fiveBusinessContractReviewMapper.selectByPrimaryKey(id);
            fiveBusinessContractReviews.add(fiveBusinessContractReview);
        }
        return fiveBusinessContractReviews;
    }

    public List<FiveBusinessContractReviewDetail> listDetail(int contractReviewId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("contractReviewId",contractReviewId);
        List<FiveBusinessContractReviewDetail> list = fiveBusinessContractReviewDetailMapper.selectAll(params);
        return list;
    }

    //??? attachId ????????? ?????????????????? ????????????
    public void addSuccessContractDir(String businessKey, int attachId,String userLogin) {
        CommonAttach commonAttach = commonAttachService.getModelById(attachId);

        int dirId = 0;
        //???????????????????????????
        List<CommonDirDto> commonDirDtos = commonDirService.listData(businessKey, 0, userLogin);
        for(CommonDirDto commonDirDto : commonDirDtos){
            if(commonDirDto.getCnName().equals("??????????????????")){
                dirId =commonDirDto.getId();
            }
        }
        if(dirId==0){
            dirId = commonDirService.newDir(businessKey, "??????????????????", 0, userLogin);
        }
        commonFileService.insertByExistAttach(businessKey,dirId,"??????????????????-?????????."+commonAttach.getExtensionName(),attachId,0,0,userLogin);
    }
}
