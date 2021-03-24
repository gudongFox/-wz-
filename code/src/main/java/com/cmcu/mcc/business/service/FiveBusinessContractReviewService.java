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
        Assert.state(model.getCreator().equals(userLogin),"该数据是用户"+model.getCreatorName()+"创建");
        //如果有存在 备案 恢复已选备案
        if(model.getRecordId()!=0){
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(model.getRecordId());
            businessRecord.setStatus("0");
            businessRecord.setContractReviewIds(MyStringUtil.getNewStringId(businessRecord.getContractReviewIds(),model.getId()));
            businessRecordMapper.updateByPrimaryKey(businessRecord);
        }
        //如果有存在 超前任务单 恢复
        if(model.getPreContractId()!=0){
            BusinessPreContract businessPreContract = businessPreContractMapper.selectByPrimaryKey(model.getPreContractId());
            businessPreContract.setReviewContractId(0);
            businessPreContractMapper.updateByPrimaryKey(businessPreContract);
        }
        //如果存在 补充合同 还原
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
        //跟新合同库状态
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
        //跟新合同库状态
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
            model.setContractMoney(MyStringUtil.moneyToString(dto.getInvestMoney()));
        }else{
            model.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney()));
        }
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
        //如果选择主合同
        if(dto.getMainContractLibraryId()!=0){
            //如果原来有备案 还原
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
            //之前的合同库补充 还原
            if(model.getMainContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getMainContractLibraryId());
                oldLibrary.setMainReviewIds(MyStringUtil.getNewStringId(oldLibrary.getMainReviewIds(),model.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
            }
            //合同库 补充添加
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getMainContractLibraryId());
            library.setMainReviewIds(library.getMainReviewIds()+","+model.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
        model.setMainContractLibraryId(dto.getMainContractLibraryId());
        model.setMainContractLibraryName(dto.getMainContractLibraryName());
        model.setMainContractLibraryNo(dto.getMainContractLibraryNo());

        model.setBackletter(dto.getBackletter());
        model.setBackletterMoney(MyStringUtil.moneyToString(dto.getBackletterMoney()));


        //如果选择项目备案 并且不是补充合同
        if(dto.getRecordId()!=0&&!dto.getMain()){
            //之前的合同库补充 还原
            if(model.getMainContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getMainContractLibraryId());
                oldLibrary.setMainReviewIds(MyStringUtil.getNewStringId(oldLibrary.getMainReviewIds(),model.getId()));
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);

                model.setMainContractLibraryId(0);
                model.setMainContractLibraryNo("");
                model.setMainContractLibraryName("");
                model.setContractNo("");
            }
            //如果原来有备案 还原
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

        model.setPlanSignTime(dto.getPlanSignTime());
        model.setContractAttachUrl(dto.getContractAttachUrl());
        model.setTaxType(dto.getTaxType());

        //计算税率和金额
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"合同印花税税目");
        for(CommonCodeDto codeDto:commonCodes){
            if(codeDto.getName().equalsIgnoreCase(model.getTaxType())){
                if(codeDto.getName().equalsIgnoreCase("其他")&&StringUtils.isNotEmpty(dto.getTaxNum())){
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
        //判断时间 30之前的添加 公司级
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2020-11-30");
        if(model.getGmtCreate().before(date)){
            variables.put("flag",model.getReviewLevel());
        }else{
            if(model.getReviewLevel().equals("公司级")){
                variables.put("flag",true);
            }else{
                variables.put("flag",false);
            }
        }
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(model.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(model.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }


        variables.put("totalDesigner", model.getTotalDesigner());
        variables.put("processDescription", "合同评审单:"+model.getContractName()+"("+model.getContractNo()+")");
        variables.put("reviewUsers", MyStringUtil.getStringList(model.getReviewUser()));
        variables.put("deptReviewUsers", MyStringUtil.getStringList(model.getDeptReviewUser()));
        variables.put("businessChargeLeader", model.getBusinessChargeLeader());//经营发展部分管领导

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
            dto.setProcessName("已完成");
            if (!dto.getProcessEnd()){
                CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
                if (customProcessInstance.isFinished() || customProcessInstance == null) {
                    dto.setProcessEnd(true);
                    fiveBusinessContractReviewMapper.updateByPrimaryKey(dto);
                    //添加进印花税申报
                    insertStampTax(item.getId());
                    //跟新合同额
                    if (item.getContractLibraryId() != 0) {
                        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
                        library.setContractMoney(item.getContractMoney());
                        fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                    }
                }else {
                    dto.setProcessName(customProcessInstance.getCurrentTaskName());
                }

                if (dto.getProcessName().contains("盖章合同上传") || dto.getProcessName().contains("确认")) {
                    //添加进合同库
                    insertContractLibrary(item.getId());
                }
                if (dto.getProcessName().contains("印花税")) {
                    if (dto.getContractLibraryId() != 0) {
                        FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(dto.getContractLibraryId());
                        //添加附件到合同库附件
                        commonFileService.insertByUrl(library.getBusinessKey(), dto.getContractAttachUrl(), dto.getCreator());
                    }
                }
            }

        } catch (Exception e) {
            //万元显示6位
            dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(), 6));
            dto.setInvestMoney(MyStringUtil.moneyToString(dto.getInvestMoney(), 6));
            return dto;
        }
        //万元显示6位
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
        //默认参评人员
        List<String> userLogins = hrEmployeeService.selectUserByRoleNames("合同章评审人");
        String reviewUser ="";
        String reviewUserName ="";
        for(String login:userLogins){
            HrEmployeeDto employee = hrEmployeeService.getModelByUserLogin(login);
            reviewUser = reviewUser+login+",";
            reviewUserName = reviewUserName+employee.getUserName()+",";
        }
        item.setReviewUser(reviewUser);
        item.setReviewUserName(reviewUserName);
        item.setReviewLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"评审级别").toString());
        item.setProjectNature(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());
        item.setInOrOut(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"部内外").toString());
        item.setMilitaryMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"军品标记").toString());
        item.setCivilMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"民用标记").toString());
        item.setForeignTradeMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"外贸标记").toString());
        item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目专业分类一级").toString());
        item.setProjectMajorTypeSecond(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目专业分类二级").toString());
        item.setProjectMajorTypeThird(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目专业分类三级").toString());
        item.setInvestSource(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"投资来源").toString());
        item.setTaxType("");
        item.setMain(false);
        item.setMainContractLibraryId(0);
        //item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型"));
        item.setContractType("设计合同");
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
        //判断是否为 合同评审录入人员
        if(selectEmployeeService.getUserListByRoleName("合同评审录入").contains(userLogin)){
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
                variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
                variables.put("businessMenFlag",true);
            }else{
                variables.put("businessMenFlag",false);
            }
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "合同评审单");
            variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),1,true));//发起者部门领导 正副职
            variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//经营发展部领导
            variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//经营发展部分管领导
            variables.put("opratorLeader", selectEmployeeService.getDeptChargeMen(29));//运营部领导
            variables.put("lawReview", selectEmployeeService.getUserListByRoleName("法律审查"));//法律审查

            item.setBusinessKey(businessKey);

            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESSCONTRACTREVIEW,item.getBusinessKey(), variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
            fiveBusinessContractReviewMapper.updateByPrimaryKey(item);
        }



        //默认评审内容
        List<String> baseNames = new ArrayList<>();
        baseNames.add("顾客及产品要求是否明确？");
        baseNames.add("顾客要求与法律、法规、标准规范和环保及建设程序是否一致？");
        baseNames.add("与以前表述不一致的口头或书面的设计委托是否予以解决？");
        baseNames.add("有无满足顾客要求的人力、技术、基础设施？");
        baseNames.add("进度要求是否合理、能否按期完成？");
        baseNames.add("项目无特殊性及难点？");
        baseNames.add("合同条款是否符合质量、环境、安全和法律、法规及其他要求？");
        baseNames.add("合同风险是否我院承受的范围内？");
        baseNames.add("可否签约？");
        initDetail(item.getId(),baseNames);
        return item.getId();
    }

    private void initDetail(Integer contractReviewId, List<String> baseNames){
        for(String baseName:baseNames){
            FiveBusinessContractReviewDetail model=new FiveBusinessContractReviewDetail();
            model.setContractReviewId(contractReviewId);
            model.setReviewContent(baseName);
            model.setReviewResult("满足");
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
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
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
            Assert.state(false,"已有同名客户");
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
            variables.put("processDescription", "客户信息录入");
            variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_CUSTOMER,bc.getBusinessKey(), variables, MccConst.APP_CODE);
            bc.setProcessInstanceId(processInstanceId);
            businessCustomerMapper.updateByPrimaryKey(bc);
            //新增的客户id 跟新回原表单
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
            return "合同： "+review.getContractName()+" 已申报印花税";
        }else{
            FiveFinanceStampTax stampTax = new FiveFinanceStampTax();
            //封金城
            stampTax.setCreator("2623");
            stampTax.setCreatorName("封金成");
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
            variables.put("processDescription", "印花税申报");
            String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
            stampTax.setProcessInstanceId(processInstanceId);*/
            stampTax.setBusinessKey(businessKey);
            fiveFinanceStampTaxMapper.updateByPrimaryKey(stampTax);

            //添加附件到印花税附件
            commonFileService.insertByUrl(stampTax.getBusinessKey(),review.getContractAttachUrl(),review.getCreator());


            return "合同： "+review.getContractName()+" 印花税申报已创建";
        }
    }

    public String insertContractLibrary(int contractReviewId) {
        FiveBusinessContractReview review = fiveBusinessContractReviewMapper.selectByPrimaryKey(contractReviewId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractReviewId",contractReviewId);
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(librarys.size()>0){
            return "项目： "+review.getProjectName()+" 已录入合同库";
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

                //客户信息
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
                library.setOpen(review.getOpen());
                library.setOpenStamp(review.getOpenStamp());
                ModelUtil.setNotNullFields(library);
                fiveBusinessContractLibraryMapper.insert(library);
                library.setBusinessKey(EdConst.FIVE_BUSINESSCONTRACTLIBRARY+"_"+library.getId());
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);

                //跟新 合同评审
                review.setContractLibraryId(library.getId());
                fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
                //转附件
                commonFileService.copyFileByBusinessKey(review.getBusinessKey(),library.getBusinessKey());
                return "项目： "+review.getProjectName()+"  成功录入合同库";
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

                //客户信息
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
                library.setOpen(review.getOpen());
                library.setOpenStamp(review.getOpenStamp());

                library.setContractReviewId(contractReviewId);
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
                //跟新 合同评审
                review.setContractLibraryId(library.getId());
                fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
                //转附件
                commonFileService.copyFileByBusinessKey(review.getBusinessKey(),library.getBusinessKey());
                return "项目： "+review.getProjectName()+"  成功补录入 合同库";
            }
        }
    }



    /**
     * XX 年份代码（如2017年写为17，2018年写为18，其余类推）
     * XX 合同承办单位代码
     * X 合同类别代码 设计合同 1 ;技术合同2;	工程总承包包（项目管理）合同，建设监理合同	3;	科研、技术开发、技术转让合同4 ; 销售合同；纪要、协议5;  无合同	6;
     * XXX  顺序代码，各类合同自成序列，从001开始
     * @param id
     */

    public String getContractNo(int id){
        try {
            FiveBusinessContractReviewDto fiveBusinessContractReviewDto = getModelById(id);
            String newContractNo="H";

            //年份代码 2019年  19
            String signYear = fiveBusinessContractReviewDto.getPlanSignTime().split("-")[0];
            String year = signYear.substring(2);
            //承办单位代码 2位 01
            String deptCode= hrDeptService.getModelById(fiveBusinessContractReviewDto.getDeptId()).getDeptCode();
            //合同类别代码
            List<CommonCodeDto> sysCodeDtoList = commonCodeService.listDataByCatalog("","合同类型");
            String typeCode="";
            for ( CommonCode dto: sysCodeDtoList) {
                if (dto.getName().equals(fiveBusinessContractReviewDto.getContractType())){
                    typeCode=dto.getCode();
                }
            }
            //如果  年份 单位 合同类型 没有改变 返回原合同号
            /*if (fiveBusinessContractReviewDto.getContractNo().contains("H"+year+deptCode+typeCode)){
                return fiveBusinessContractReviewDto.getContractNo();
            }*/

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("contractNoHead","H"+year+deptCode+typeCode);
            List<FiveBusinessContractReview> reviews = fiveBusinessContractReviewMapper.selectAll(params);
            int size=0;
            //判断顺序代码最大值
            if (!reviews.isEmpty()){
                for (FiveBusinessContractReview review :reviews){
                    if(!review.getId().equals(id)&&StringUtils.isNotEmpty(review.getContractNo())){
                        String contractNo = review.getContractNo();
                        //如果存在 H  去除 H
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
            Assert.state(false, "请准确填写，签约日期、承办单位、合同类型！");
            return "";
        }
    }

    public String getMainContractNo(int id){
        try {
            FiveBusinessContractReviewDto reviewDto = getModelById(id);
            String newContractNo=reviewDto.getMainContractLibraryNo();

            String code ="-";

            //编号 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("mainContractNoHead",newContractNo+code);
            List<FiveBusinessContractReview> reviews = fiveBusinessContractReviewMapper.selectAll(params);
            int max=0;
            //判断顺序代码最大值
            if (!reviews.isEmpty()){
                for (FiveBusinessContractReview review:reviews){
                    if(!review.getId().equals(reviewDto.getId())){
                        //已有合同号
                        String no =review.getContractNo();
                        //取后一位 （如果一个合同存在10个补充合同 存在问题）
                        int num =Integer.valueOf(no.substring(no.length()-1));
                        if(num>max){
                            max =num;
                        }
                    }
                }
            }
            max=max+1;
            newContractNo=newContractNo+code+max;

            //排除已被使用的特殊合同号
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
            Assert.state(false, "请选择主合同信息！");
            return "";
        }
    }

    //获取未启动项目的 合同评审
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
        //如果本来就有 添加
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

    //将 attachId 添加到 正式签章合同 文件夹下
    public void addSuccessContractDir(String businessKey, int attachId,String userLogin) {
        CommonAttach commonAttach = commonAttachService.getModelById(attachId);

        int dirId = 0;
        //判断是否存在文件夹
        List<CommonDirDto> commonDirDtos = commonDirService.listData(businessKey, 0, userLogin);
        for(CommonDirDto commonDirDto : commonDirDtos){
            if(commonDirDto.getCnName().equals("正式签章合同")){
                dirId =commonDirDto.getId();
            }
        }
        if(dirId==0){
            dirId = commonDirService.newDir(businessKey, "正式签章合同", 0, userLogin);
        }
        commonFileService.insertByExistAttach(businessKey,dirId,"正式签章合同-盖章页."+commonAttach.getExtensionName(),attachId,0,0,userLogin);
    }
}
