package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;

import com.cmcu.mcc.business.dto.BusinessPreContractDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.service.EdProjectTreeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;

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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessPreContractService extends BaseService {

    @Autowired
    ActService actService;

    @Resource
    BusinessPreContractMapper businessPreContractMapper;

    @Resource
    BusinessRecordMapper businessRecordMapper;

    @Resource
    CommonCodeService commonCodeService;

    @Resource
    BusinessCustomerMapper businessCustomerMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    BusinessBidContractMapper businessBidContractMapper;
    @Resource
    HrDeptService hrDeptService;
    @Autowired
    EdProjectTreeService edProjectTreeService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    MyActService myActService;

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    BusinessContractService businessContractService;

    @Autowired
    TaskHandleService taskHandleService;

    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractReviewMapper fiveBusinessContractReviewMapper;
    @Autowired
    FiveBusinessContractReviewDetailMapper fiveBusinessContractReviewDetailMapper;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    HandleFormService handleFormService;

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin,String uiSref,int pageNum, int pageSize) {
        params.put("deleted", false);
//???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessPreContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessPreContract item = (BusinessPreContract) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void remove(int id, String userLogin) {
        BusinessPreContract model = businessPreContractMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin), "??????????????????" + model.getCreatorName() + "??????!");

        //??????????????? ?????? ??????????????????
        if(model.getRecordId()!=0){
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
            businessRecord.setStatus("0");
            businessRecord.setPreContractIds(MyStringUtil.getNewStringId(businessRecord.getContractReviewIds(),id));
            businessRecordMapper.updateByPrimaryKey(businessRecord);
        }

        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);



    }


    public void update(BusinessPreContractDto item) {
        HrDept hrDept = hrDeptService.getModelById(item.getDeptId());
        BusinessPreContract model = businessPreContractMapper.selectByPrimaryKey(item.getId());

        //????????????????????????
        if(item.getRecordId()!=0){
            //????????????????????? ??????
            if(model.getRecordId()!=0){
                BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
                businessRecord.setStatus("0");
                businessRecord.setPreContractIds(MyStringUtil.getNewStringId(businessRecord.getContractReviewIds(),model.getId()));
                businessRecordMapper.updateByPrimaryKey(businessRecord);
            }
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(item.getRecordId());
            businessRecord.setStatus("1");
            businessRecord.setPreContractIds(businessRecord.getPreContractIds()+","+item.getId());
            businessRecordMapper.updateByPrimaryKey(businessRecord);
            model.setRecordId(item.getRecordId());
        }
        model.setRecordId(item.getRecordId());

        model.setProjectName(item.getProjectName());
        model.setProjectNo(item.getProjectNo());
        model.setProjectAddress(item.getProjectAddress());
        model.setCustomerId(item.getCustomerId());
        model.setCustomerName(item.getCustomerName());
        model.setLinkMan(item.getLinkMan());
        model.setLinkTel(item.getLinkTel());
        model.setLinkTitle(item.getLinkTitle());
        model.setCustomerAddress(item.getCustomerAddress());
        model.setCustomerCode(item.getCustomerCode());
        model.setProjectNature(item.getProjectNature());
        model.setRecordProjectName(item.getRecordProjectName());

        model.setInvestSource(item.getInvestSource());
        model.setInvestMoney(item.getInvestMoney());
        model.setChargeMen(item.getChargeMen());
        model.setChargeMenName(item.getChargeMenName());
        model.setStageName(item.getStageName());
        model.setConstructionArea(item.getConstructionArea());
        model.setInvestMoney(item.getInvestMoney());
        model.setPlanBeginTime(item.getPlanBeginTime());
        model.setPlanEndTime(item.getPlanEndTime());
        model.setDesignType(item.getDesignType());
        model.setDesignContent(item.getDesignContent());
        model.setPreDesc(item.getPreDesc());
        model.setPreCondition(item.getPreCondition());
        model.setPreConditionRemark(item.getPreConditionRemark());
        model.setOtherSituation(item.getOtherSituation());
        model.setDeptName(hrDept.getName());
        model.setDeptId(hrDept.getId());
        model.setDesignMajor(item.getDesignMajor());
        model.setArrangeEndTime(item.getArrangeEndTime());

        model.setTotalDesigner(item.getTotalDesigner());
        model.setTotalDesignerName(item.getTotalDesignerName());
        model.setProjectManagerName(item.getProjectManagerName());
        model.setProjectManager(item.getProjectManager());

        model.setEd(item.getEd());
        model.setReviewContractId(item.getReviewContractId());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);

        Map variables = Maps.newHashMap();
        variables.put("totalChargeMen", model.getTotalDesigner());//????????????
        variables.put("processDescription", "???????????????:"+model.getProjectName());
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        businessPreContractMapper.updateByPrimaryKey(model);

    }

    public BusinessPreContractDto getModelById(int id) {
        return getDto(businessPreContractMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        BusinessPreContract item = new BusinessPreContract();
        item.setContractId(0);
        item.setInvestMoney(MyStringUtil.moneyToString("0"));
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setEd(true);
        item.setReviewContractId(0);

        item.setStageName(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setDesignType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setInvestSource(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setProjectNature(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());

        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());

        Map<String, Object> defaultDept = hrDeptService.getDefaultDept(hrEmployeeDto.getDeptId());
        item.setDeptId(Integer.parseInt(defaultDept.get("deptId").toString()));
        item.setDeptName(defaultDept.get("deptName").toString());
        ModelUtil.setNotNullFields(item);
        businessPreContractMapper.insert(item);

        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("processDescription", "?????????????????????????????????????????????");

        String processKey=EdConst.FIVE_BUSINESS_PRE_CONTRACT;
        String businessKey = processKey+"_"+item.getId();
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables,MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessPreContractMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public BusinessPreContractDto getDto(Object item) {
        BusinessPreContractDto dto = BusinessPreContractDto.adapt((BusinessPreContract) item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd() && StringUtils.isNotEmpty(dto.getProcessInstanceId())) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("?????????");
            }
            //dto.setBusinessKey(processInstanceDto.getBusinessKey());
            if (!dto.getProcessEnd()&&customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                businessPreContractMapper.updateByPrimaryKey(dto);
                //??????????????????
                insertContractLibrary(dto.getId());
            }
        }


        return dto;
    }

    //?????????????????? ???????????????
    public List<BusinessPreContract> selectNotHaveContract(int id) {
        Map map = new HashMap();
        map.put("deleted", false);
       // map.put("processEnd",true);
       // List<BusinessPreContract>  businessPreContracts = businessPreContractMapper.selectAll(map).stream().filter(p -> p.getInputContractId().equals("0")).collect(Collectors.toList());
       /* map.put("contractId",contractId);
        if (businessPreContractMapper.selectAll(map).size()>0){
            businessPreContracts.add(businessPreContractMapper.selectAll(map).get(0));
        }*/
        List<BusinessPreContract>  businessPreContracts = businessPreContractMapper.selectAll(map).stream().filter(p -> p.getContractId().equals(0)).collect(Collectors.toList());
        //?????????????????? ??????
        if(id!=0){
            BusinessPreContract preContract = businessPreContractMapper.selectByPrimaryKey(id);
            businessPreContracts.add(preContract);
        }
        return businessPreContracts;
    }
    //???????????????????????? ???????????????
    public List<BusinessPreContract> selectNotHaveInput(int id) {
        Map map = new HashMap();
        map.put("deleted", false);
        // map.put("processEnd",true);
        // List<BusinessPreContract>  businessPreContracts = businessPreContractMapper.selectAll(map).stream().filter(p -> p.getInputContractId().equals("0")).collect(Collectors.toList());
       /* map.put("contractId",contractId);
        if (businessPreContractMapper.selectAll(map).size()>0){
            businessPreContracts.add(businessPreContractMapper.selectAll(map).get(0));
        }*/
        List<BusinessPreContract>  businessPreContracts = businessPreContractMapper.selectAll(map).stream().filter(p -> p.getReviewContractId().equals(0)).collect(Collectors.toList());
        //?????????????????? ??????
        if(id!=0){
            BusinessPreContract preContract = businessPreContractMapper.selectByPrimaryKey(id);
            businessPreContracts.add(preContract);
        }

        return businessPreContracts;
    }


    /**
     * ?????????????????? ?????????????????????
     * @param contractId
     * @return
     */
    public BusinessPreContractDto getModelByContractId(int contractId){
        Map<String, Object> params=Maps.newHashMap();
        params.put("deleted", false);
        params.put("contractId",contractId);
        List<BusinessPreContract> businessPreContracts = businessPreContractMapper.selectAll(params);
        if (businessPreContracts.size()>0){
            return getDto(businessPreContracts.get(0));
        }else {
            return null;
        }
    }

    /**
     * XX ??????????????????2017?????????17???2018?????????18??????????????????
     * XX ????????????????????????
     * X ?????????????????? ???????????? S ???????????? Z ???????????? C ?????? K ?????? J ????????? W
     * XXX  ?????????????????????????????????????????????001??????
     * @param id
     */

    public String getProjectNo(int id){
        try {
            BusinessPreContractDto businessPreContractDto = getModelById(id);
           // BusinessContractDto businessContractDto = businessContractService.getModelById(Integer.valueOf(businessPreContractDto.getContractId()));
            String newProjectNo = "";
            //???????????? 2019???  19
            String signYear = businessPreContractDto.getPlanBeginTime().split("-")[0];
            String year = signYear.substring(2);
            //?????????????????? 2??? 01
            String deptCode= hrDeptService.getModelById(businessPreContractDto.getDeptId()).getDeptCode();
            //??????????????????
            String typeCode="";

            if(businessPreContractDto.getProjectNature().equals("????????????")){
                typeCode="S";
            }else if(businessPreContractDto.getProjectNature().equals("????????????")){
                typeCode="Z";
            }else if(businessPreContractDto.getProjectNature().equals("????????????")){
                typeCode="C";
            } else if(businessPreContractDto.getProjectNature().equals("??????")){
                typeCode="K";
            } else if(businessPreContractDto.getProjectNature().equals("??????")){
                typeCode="J";
            } else if(businessPreContractDto.getProjectNature().equals("?????????")){
                typeCode="W";
            }
            //??????  ?????? ?????? ???????????? ???????????? ??????????????????
            if (businessPreContractDto.getProjectNo().contains(year+deptCode+typeCode)){
                return businessPreContractDto.getProjectNo();
            }

            //?????? 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("projectNoHead",year+deptCode+typeCode);
            List<BusinessPreContract> businessPreContracts = businessPreContractMapper.selectAll(params);
            int size=1;
            //???????????????????????????
            if (!businessPreContracts.isEmpty()){
                for (BusinessPreContract preContract:businessPreContracts){
                    if(preContract.getId()!=id&&StringUtils.isNotEmpty(preContract.getProjectNo())){
                        String maxSize=  preContract.getProjectNo().substring(5);
                        if (size<Integer.parseInt(maxSize)){
                            size = Integer.parseInt(maxSize);
                        }
                    }
                }
                size=size+1;
            }
            String format = String.format("%03d", size);
            newProjectNo=newProjectNo+year+deptCode+typeCode+format;

            businessPreContractDto.setProjectNo(newProjectNo);
            update(businessPreContractDto);

            return newProjectNo;

        }catch (Exception e){
            Assert.state(false, "?????????????????????????????????????????????????????????????????????");
            return "";
        }


    }
    //??????????????? ????????????
    public String insertContractLibrary(int preContractId) {
        BusinessPreContract pre =businessPreContractMapper.selectByPrimaryKey(preContractId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractPreId", preContractId);
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(librarys.size()>0){
            return "????????? "+pre.getProjectName()+" ??????????????????";
        }else{
            FiveBusinessContractLibrary library = new FiveBusinessContractLibrary();
            library.setProjectName(pre.getProjectName());
            library.setRecordProjectName(pre.getRecordProjectName());

            library.setTotalDesigner(pre.getTotalDesigner());
            library.setTotalDesignerName(pre.getTotalDesignerName());
            library.setProjectManager(pre.getProjectManager());
            library.setProjectManagerName(pre.getProjectManagerName());

            library.setDeptId(pre.getDeptId());
            library.setDeptName(pre.getDeptName());
            library.setInvestMoney(pre.getInvestMoney());
            library.setInvestSource(pre.getInvestSource());
            library.setProjectNature(pre.getProjectNature());
            library.setConstructionScale(pre.getConstructionArea());
            library.setConstructionArea(pre.getProjectAddress());

            library.setStageNames(pre.getStageName());
            library.setGmtModified(new Date());
            library.setGmtCreate(new Date());
            library.setCreator(pre.getCreator());
            library.setCustomerName(pre.getCreatorName());
            //????????????
            if(!Strings.isNullOrEmpty(pre.getCustomerId())){
                library.setCustomerId(Integer.valueOf(pre.getCustomerId()));
            }
            library.setCustomerAddress(pre.getCustomerAddress());
            library.setCustomerCode(pre.getCustomerCode());
            library.setCustomerName(pre.getCustomerName());
            library.setLinkMan(pre.getLinkMan());
            library.setLinkTel(pre.getLinkTel());
            library.setLinkTitle(pre.getLinkTitle());

            library.setProjectNo(pre.getProjectNo());
            library.setContractPreId(preContractId);
            library.setContractIncomeMoney(0+"");
            ModelUtil.setNotNullFields(library);
            fiveBusinessContractLibraryMapper.insert(library);
            library.setBusinessKey(EdConst.FIVE_BUSINESSCONTRACTLIBRARY+"_"+library.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
            return "????????? "+pre.getProjectName()+"  ?????????????????????";
        }

    }
    //????????????
    public int addReviewContract(int preContractId) {
        BusinessPreContractDto preContractDto = getModelById(preContractId);

        String userLogin = preContractDto.getCreator();
        FiveBusinessContractReview item=new FiveBusinessContractReview();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(preContractDto.getDeptId());
        item.setDeptName(preContractDto.getDeptName());
        item.setProjectName(preContractDto.getProjectName());
        item.setInvestSource(preContractDto.getInvestSource());
        item.setInvestMoney(preContractDto.getInvestMoney());
        item.setConstructionArea(preContractDto.getProjectAddress());
        item.setConstructionScale(preContractDto.getConstructionArea());
        item.setPreContractId(preContractId);
        item.setProjectNature(preContractDto.getProjectNature());
        item.setProjectNo(preContractDto.getProjectNo());

        item.setCustomerName(preContractDto.getCustomerName());
        item.setCustomerId(preContractDto.getContractId());
        item.setCustomerCode(preContractDto.getCustomerCode());
        item.setCustomerAddress(preContractDto.getCustomerAddress());
        item.setLinkTitle(preContractDto.getLinkTitle());
        item.setLinkMan(preContractDto.getLinkMan());
        item.setLinkTel(preContractDto.getLinkTel());

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


        //??????????????????
        List<String> userLogins = hrEmployeeService.selectUserByRoleNames("??????????????????");
        String reviewUser ="";
        String reviewUserName ="";
        for(String login:userLogins){
            HrEmployeeDto employee = hrEmployeeService.getModelByUserLogin(login);
            reviewUser = reviewUser+login+",";
            reviewUserName = reviewUserName+employee.getUserName()+",";
        }
        item.setContractType("????????????");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        item.setSignTime(sdf.format(new Date()));
        item.setPlanSignTime(sdf.format(new Date()));

        item.setReviewUser(reviewUser);
        item.setReviewUserName(reviewUserName);
        item.setCustomerId(0);
        item.setBid(false);
        item.setLegalPerson(false);
        item.setEleLegalPerson(false);
        item.setBusinessLegalPerson(false);
        item.setEleBusinessLegalPerson(false);
        item.setCommonSeal(false);
        item.setAttach(false);


        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveBusinessContractReviewMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESSCONTRACTREVIEW+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "???????????????");
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//?????????????????????
        variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//?????????????????????
        variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//???????????????????????????
        variables.put("opratorLeader", selectEmployeeService.getDeptChargeMen(29));//???????????????
        variables.put("lawReview", selectEmployeeService.getUserListByRoleName("????????????"));//????????????
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESSCONTRACTREVIEW,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveBusinessContractReviewMapper.updateByPrimaryKey(item);
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

        //???????????????????????????
        preContractDto.setReviewContractId(item.getId());
        businessPreContractMapper.updateByPrimaryKey(preContractDto);
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

    public int addCustomer(String preContractId, String userLogin) {
        BusinessPreContract item = getModelById(Integer.parseInt(preContractId));
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
            item.setCustomerId(bc.getId()+"");
            businessPreContractMapper.updateByPrimaryKey(item);
        }

        return bc.getId();
    }
}
