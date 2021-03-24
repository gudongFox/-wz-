package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.exception.ParamException;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.dao.EdProjectStepMapper;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.service.EdProjectStepService;
import com.cmcu.mcc.ed.service.EdProjectTreeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.service.HrDeptService;
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
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BusinessContractService extends BaseService {

    @Autowired
    ActService actService;

    @Resource
    BusinessContractMapper businessContractMapper;

    @Resource
    BusinessContractDetailMapper businessContractDetailMapper;

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
    CommonConfigService commonConfigService;
    @Resource
    TaskHandleService taskHandleService;
    @Autowired
    EdProjectStepMapper edProjectStepMapper;

    @Autowired
    EdProjectStepService edProjectStepService;

    @Autowired
    FiveBusinessContractReviewMapper fiveBusinessContractReviewMapper;
    @Autowired
    BusinessPreContractMapper businessPreContractMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Resource
    HandleFormService handleFormService;
    @Autowired
    ProcessQueryService processQueryService;

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin,String uiSref,int pageNum, int pageSize) {
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map = new HashMap();
        map.put("myDeptData", true);
        map.put("uiSref", uiSref);
        map.put("enLogin", userLogin);
        params.put("deleted", false);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto(p)));
        pageInfo.setList(list);
        return pageInfo;
    }

    public void remove(int id, String userLogin) {
        BusinessContract model = businessContractMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin), "只允许创建人" + model.getCreatorName() + "删除!");
        //之前有合同库 还原
        if(model.getContractLibraryId()!=0){
            FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getContractLibraryId());
            oldLibrary.setContractId(0);
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
        }
        if(!model.getProcessInstanceId().equals("0")) {
            //删除流程+ 表单数据 没得BusinessKey
            String businessKey = getDto(model).getBusinessKey();
            handleFormService.removeProcessInstance(businessKey,userLogin);
        }

    }
    //删除整条数据
    public void deleted(int id) {
        businessContractMapper.deleteByPrimaryKey(id);
    }

    public String getNewContractNo(int id) {
        try {
            BusinessContract model = businessContractMapper.selectByPrimaryKey(id);
            HrDept hrDept = hrDeptService.getModelById(model.getDeptId());
            String contractNo = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String[] sts = sdf.format(model.getGmtCreate()).split("-");
            int year = Integer.parseInt(sts[0]);

            //获取合同类型编号
            Map codeParams = Maps.newHashMap();
            codeParams.put("deleted", false);
            codeParams.put("codeCatalog", "合同类型");
            codeParams.put("name", model.getContractType());
            CommonCode sysCode=new CommonCode();
            List<CommonCodeDto> commonCodeList = commonCodeService.listDataByCatalog("", "合同类型");
            for (CommonCode code:commonCodeList){
                if (code.getName().equals(model.getContractType())){
                    sysCode=code;
                }
            }


            //获取原合同编号
            String deptContractNum;
            if (StringUtils.isNotEmpty(model.getContractNo())) {
                deptContractNum = model.getContractNo().split("-")[2];
            } else {
                deptContractNum = "000";
            }
            //匹配%2019_00_%
            Map contractParams = Maps.newHashMap();
            contractParams.put("param",year+"_"+hrDept.getDeptCode()+"_");

            List<BusinessContract> businessContracts = businessContractMapper.selectNumByDeptIdAndTime(contractParams);
            //如果修改保存后是该部门的第一条，编号001
            if (businessContracts.size() == 1) {
                deptContractNum = String.format("%03d", 1);
            } else {
                //获取部门最大合同编号
                int maxNum = 0;
                for (BusinessContract businessContract : businessContracts) {
                    int contractNum = 0;
                    if (StringUtils.isNotEmpty(businessContract.getContractNo())) {
                        contractNum = Integer.valueOf(businessContract.getContractNo().split("-")[2]);
                    }
                    //排除最大合同编号 是跟新的数据
                    if (contractNum >= maxNum && !businessContract.getId().equals(model.getId())) {
                        maxNum = contractNum;
                        deptContractNum = String.format("%03d", maxNum + 1);
                    }
                }

            }
            //判断类型编号
            if (hrDept.getDeptType().equals("勘察") && sysCode.getName().equals("设计")) {
                contractNo = "KS" + year + "-" + hrDept.getDeptCode() + "-" + deptContractNum;
            } else if (hrDept.getDeptType().equals("设计") && sysCode.getName().equals("勘察")) {
                contractNo = "SK" + year + "-" + hrDept.getDeptCode() + "-" + deptContractNum;
            } else {
                contractNo = sysCode.getCode() + year + "-" + hrDept.getDeptCode() + "-" + deptContractNum;
            }

            model.setContractNo(contractNo);
            model.setGmtModified(new Date());
            businessContractMapper.updateByPrimaryKey(model);
            return model.getContractNo();
        } catch (Exception ex) {
            return "";
        }
    }

    public void update(BusinessContractDto item) {
        if(item.getDeptId()==0) throw  new IllegalArgumentException("请选择执行所示");
        HrDept hrDept = hrDeptService.getModelById(item.getDeptId());
        BusinessContract model = businessContractMapper.selectByPrimaryKey(item.getId());
        //传入合同评审
        if(item.getContractReviewId()!=0){
            //评审转为已用
            FiveBusinessContractReview review = fiveBusinessContractReviewMapper.selectByPrimaryKey(item.getContractReviewId());
            review.setContractId(item.getId());
            fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
            //之前有评审 还原
            if(model.getContractReviewId()!=0){
                FiveBusinessContractReview oldReview = fiveBusinessContractReviewMapper.selectByPrimaryKey(model.getContractReviewId());
                oldReview.setContractId(0);
                fiveBusinessContractReviewMapper.updateByPrimaryKey(oldReview);
            }
            //之前的超前任务单 还原
            if(model.getPreContractId()!=0){
                BusinessPreContract oldPreContract = businessPreContractMapper.selectByPrimaryKey(model.getPreContractId());
                oldPreContract.setContractId(0);
                businessPreContractMapper.updateByPrimaryKey(oldPreContract);
            }
        }
        //传入 超前任务单
        if(item.getPreContractId()!=0){
            //超前任务单 转为已启动
            BusinessPreContract preContract = businessPreContractMapper.selectByPrimaryKey(item.getPreContractId());
            preContract.setContractId(item.getPreContractId());
            businessPreContractMapper.updateByPrimaryKey(preContract);
            //之前的超前任务单 还原
            if(model.getPreContractId()!=0){
                BusinessPreContract oldPreContract = businessPreContractMapper.selectByPrimaryKey(model.getPreContractId());
                oldPreContract.setContractId(0);
                businessPreContractMapper.updateByPrimaryKey(oldPreContract);
            }
            //之前有评审 还原
            if(model.getContractReviewId()!=0){
                FiveBusinessContractReview oldReview = fiveBusinessContractReviewMapper.selectByPrimaryKey(model.getContractReviewId());
                oldReview.setContractId(0);
                fiveBusinessContractReviewMapper.updateByPrimaryKey(oldReview);
            }
        }
        //传入 合同库
        if(item.getContractLibraryId()!=0){
            //判断 合同 是否已经启动
            Map map = new HashMap();
            map.put("deleted",false);
            List<BusinessContract> businessContracts = businessContractMapper.selectAll(map);
            for(BusinessContract contract:businessContracts){
                if(contract.getId()!=item.getId()&&item.getContractLibraryId().equals(contract.getContractLibraryId())){
                    Assert.state(false,"合同名称为："+contract.getProjectName()+" 已被启动");
                }
            }
            //之前的合同库 还原
            if(model.getContractLibraryId()!=0){
                FiveBusinessContractLibrary oldLibrary = fiveBusinessContractLibraryMapper.selectByPrimaryKey(model.getContractLibraryId());
                oldLibrary.setContractId(0);
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(oldLibrary);
            }
            //合同库 转为已启动
            FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(item.getContractLibraryId());
            library.setContractId(item.getId());
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }

        model.setPreContractId(item.getPreContractId());
        model.setContractReviewId(item.getContractReviewId());
        model.setContractLibraryId(item.getContractLibraryId());
        model.setContractNo(item.getContractNo());
        model.setCustomerId(item.getCustomerId());
        model.setCustomerName(item.getCustomerName());
        model.setLinkMan(item.getLinkMan());
        model.setLinkTel(item.getLinkTel());
        model.setLinkFax(item.getLinkFax());
        model.setLinkMail(item.getLinkMail());
        model.setDepositBank(item.getDepositBank());
        model.setBankAccount(item.getBankAccount());
        model.setTaxType(item.getTaxType());
        model.setTaxNo(item.getTaxNo());
        model.setContractNo(item.getContractNo());
        model.setContractType(item.getContractType());
        model.setProjectType(item.getProjectType());
        model.setProjectName(item.getProjectName());
        if(item.getContractMoney()!=null){
            model.setContractMoney(item.getContractMoney());
        }else {
            model.setContractMoney(BigDecimal.ZERO);
        }

        model.setProjectAddress(item.getProjectAddress());
        model.setChargeMen(MyStringUtil.getMultiDotString(item.getChargeMen()));
        model.setChargeMenName(item.getChargeMenName());

        model.setBusinessManager(MyStringUtil.getMultiDotString(item.getBusinessManager()));
        model.setBusinessManagerName(item.getBusinessManagerName());

        model.setExeChargeMen(model.getBusinessManager());
        model.setExeChargeMenName(model.getBusinessManagerName());


        model.setSigner(item.getSigner());
        model.setSignerName(item.getSignerName());
        model.setSignDate(item.getSignDate());
        model.setDeptId(item.getDeptId());
        model.setDeptName(hrDept.getName());

        model.setDeptIds(item.getDeptIds());
        model.setDeptNames(item.getDeptNames());
        model.setStageNames(item.getStageNames());
        model.setEd(item.getEd());
        model.setGmtModified(new Date());
        model.setRemark(item.getRemark());



        model.setCustomerType(item.getCustomerType());
        model.setContractScope(item.getContractScope());
        model.setInvestMoney(item.getInvestMoney());
        model.setProjectProvince(item.getProjectProvince());
        model.setProjectCity(item.getProjectCity());
        model.setConstructionArea(item.getConstructionArea());
        model.setFloorArea(item.getFloorArea());
        model.setRoadLength(item.getRoadLength());
        model.setConstructionTime(item.getConstructionTime());
        model.setAssociated(item.getAssociated());
        model.setAcceptMode(item.getAcceptMode());
        model.setOtherScale(item.getOtherScale());
        model.setReportAmount(item.getReportAmount());
        model.setBidBack(item.getBidBack());
        model.setBidNotice(item.getBidNotice());
        model.setGroup(item.getGroup());
        model.setSigned(item.getSigned());
        model.setCompany(item.getCompany());
        model.setStampTime(item.getStampTime());
        model.setBackTime(item.getBackTime());
        model.setCopyCount(item.getCopyCount());
        model.setOriginalCount(item.getOriginalCount());
        model.setStampCount(item.getStampCount());

        model.setRecordNoEarly(item.getRecordNoEarly());
        model.setRecordNoFirst(item.getRecordNoFirst());
        model.setRecordNoConstruction(item.getRecordNoConstruction());

        //ModelUtil.setNotNullFields(model);
        BeanValidator.validateObject(model);
        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(item.getDeptId());
/*        if(sysConfigService.getConfig().getAppCode().equals("mcc")) {
            actService.setVariables(model.getProcessInstanceId(), "deptChargeMen", deptChargeMen);
        }*/

        BusinessContractDetail modelDetail=getContractDetail(item.getId());
        BusinessContractDetail businessContractDetail = item.getBusinessContractDetail();
        modelDetail.setOtherDesigner(businessContractDetail.getOtherDesigner());
        modelDetail.setOtherDesignerName(businessContractDetail.getOtherDesignerName());
        modelDetail.setTotalDesigner(businessContractDetail.getTotalDesigner());
        modelDetail.setTotalDesignerName(businessContractDetail.getTotalDesignerName());
        modelDetail.setProjectCode(businessContractDetail.getProjectCode());
        modelDetail.setSecType(businessContractDetail.getSecType());
        modelDetail.setPlanBeginTime(businessContractDetail.getPlanBeginTime());
        modelDetail.setPlanEndTime(businessContractDetail.getPlanEndTime());
        modelDetail.setPartTimeJob(businessContractDetail.getPartTimeJob());
        modelDetail.setReviewType(businessContractDetail.getReviewType());
        modelDetail.setProjectNo(businessContractDetail.getProjectNo());
        modelDetail.setChargeMen(businessContractDetail.getChargeMen());
        modelDetail.setChargeMenName(businessContractDetail.getChargeMenName());
        ModelUtil.setNotNullFields(modelDetail);
        BeanValidator.validateObject(modelDetail);
        businessContractDetailMapper.updateByPrimaryKey(modelDetail);
        model.setTotalDesigner(businessContractDetail.getTotalDesigner());
        ModelUtil.setNotNullFields(model);
        businessContractMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("processDescription", "项目启动:"+model.getProjectName());
        List<String> totalDesigners = new ArrayList<>();
        totalDesigners.add(modelDetail.getTotalDesigner());
        totalDesigners.addAll(MyStringUtil.getStringList(modelDetail.getOtherDesigner()));
        variables.put("totalDesigners",totalDesigners);
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public BusinessContractDto getModelById(int id) {
        return getDto(businessContractMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, uiSref);

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        BusinessContract item = new BusinessContract();
        item.setContractMoney(BigDecimal.ZERO);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setSignerName(hrEmployeeDto.getUserName());
        item.setSigner(userLogin);
        item.setSignDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型").toString());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCustomerType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"客户单位类型").toString());
        item.setTaxType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"纳税主体资格").toString());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,uiSref.contains("five") ? "五洲项目类型" : "设计作业类型").toString());
        item.setEd(true);
        String stageNames = "";
        List<CommonCodeDto> commonCodeDtos = commonCodeService.listDataByCatalog(MccConst.APP_CODE, "设计评审阶段");
        for(CommonCodeDto dto:commonCodeDtos){
            stageNames =stageNames+dto.getName()+",";
        }
        item.setStageNames(stageNames.substring(0,stageNames.length()-1));
        item.setContractScope(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目行业").toString());
        item.setProjectProvince("重庆市");
        item.setProjectCity("大渡口区");
        if (uiSref.contains("five")) {
            item.setProjectProvince("北京市");
            item.setProjectCity("西城区");
        }
        item.setInvestMoney(BigDecimal.ZERO);
        item.setConstructionArea(BigDecimal.ZERO);
        item.setFloorArea(BigDecimal.ZERO);
        item.setRoadLength(BigDecimal.ZERO);
        item.setConstructionTime(BigDecimal.ZERO);
        item.setAssociated(false);
        item.setBidNotice(false);
        item.setBidBack(false);
        item.setGroup(false);
        item.setSigned(true);
        item.setCompany(true);
        ModelUtil.setNotNullFields(item);
        businessContractMapper.insert(item);
        BusinessContractDetail businessContractDetail = new BusinessContractDetail();
        businessContractDetail.setContractId(item.getId());
        businessContractDetail.setReviewType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"审图级别").toString());
        businessContractDetail.setPartTimeJob("否");
        businessContractDetail.setSecType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目密级").toString());
        ModelUtil.setNotNullFields(businessContractDetail);
        businessContractDetailMapper.insert(businessContractDetail);
        getNewContractNo(item.getId());
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//部门经营人员
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("financeMan", selectEmployeeService.getFinanceChargeMan());
        variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("经营发展部人员(合同)"));//经营发展部人员（合同）

        variables.put("processDescription", "项目启动");

        String processKey=EdConst.FIVE_BUSINESS_CONTRACT;
        String businessKey = processKey+"_"+item.getId();
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);

        businessContractMapper.updateByPrimaryKey(item);
        //edProjectTreeService.genProjectTree(item.getId());
        return item.getId();
    }

    public int getNewModelById(String userLogin, int bidContractId) {
        if (isExitBidContract(bidContractId)) {
            Assert.state(false, "项目名为：" + businessBidContractMapper.selectByPrimaryKey(bidContractId).getProjectName()
                    + " 的合同台账已创建，请不要重复创建");
            return 0;
        }
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        List<String> deptChargeMen=hrDeptService.getDeptChargeMen(hrEmployeeDto.getDeptId());
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(hrEmployeeDto.getDeptName()+"没有配置部门负责人!");

        BusinessBidContract businessBidContract = businessBidContractMapper.selectByPrimaryKey(bidContractId);
        BusinessContract item = new BusinessContract();
        Map params = Maps.newHashMap();
        params.put("customerName", businessBidContract.getCustomerName());
        params.put("deleted", false);

        List<BusinessCustomer> businessCustomerList = businessCustomerMapper.selectAll(params);
        if (businessCustomerList.size() == 1) {
            BusinessCustomer businessCustomer = businessCustomerList.get(0);
            item.setCustomerId(businessCustomer.getId());
            item.setCustomerName(businessCustomer.getName());
            item.setLinkMan(businessCustomer.getLinkMan());
            item.setLinkTel(businessCustomer.getLinkTel());
            item.setLinkFax(businessCustomer.getLinkFax());
            item.setLinkMail(businessCustomer.getLinkMail());
            item.setDepositBank(businessCustomer.getDepositBank());
            item.setBankAccount(businessCustomer.getBankAccount());
            item.setTaxType(businessCustomer.getTaxType());
            item.setTaxNo(businessCustomer.getTaxNo());
        }

        item.setContractMoney(BigDecimal.ZERO);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setSignerName(hrEmployeeDto.getUserName());
        item.setSigner(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同类型").toString());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setTaxType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"纳税主体资格").toString());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"设计作业类型").toString());

        item.setBidContractId(businessBidContract.getId());
        item.setProjectType(businessBidContract.getProjectType());
        item.setProjectName(businessBidContract.getProjectName());
        item.setProjectAddress(businessBidContract.getProjectAddress());
        item.setContractMoney(businessBidContract.getContractMoney());
        item.setContractType(businessBidContract.getContractType());

        ModelUtil.setNotNullFields(item);

        businessContractMapper.insert(item);
        getNewContractNo(item.getId());

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("passed", true);
        variables.put("deptChargeMen",deptChargeMen);
        variables.put("processDescription", "");
        String processInstanceId = actService.startProcess(EdConst.BUSINESS_CONTRACT, item.getId(), variables);
        item.setProcessInstanceId(processInstanceId);
        businessContractMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public BusinessContractDto getDto(Object item) {



        BusinessContractDto dto = BusinessContractDto.adapt((BusinessContract) item);
        //客户编号
        if(dto.getCustomerId()!=0){
            BusinessCustomer businessCustomer = businessCustomerMapper.selectByPrimaryKey(dto.getCustomerId());
            dto.setCustomerCode(businessCustomer.getCode());
        }else{
            dto.setCustomerCode("");
        }
        //子表信息
        BusinessContractDetail contractDetail = businessContractDetailMapper.selectByContractId(dto.getId());
        dto.setBusinessContractDetail(contractDetail);
        dto.setProcessName("已完成");
        dto.setGoodProjectName(FileUtil.getGoodName(dto.getProjectName()));
        dto.setBusinessKey(EdConst.BUSINESS_CONTRACT+"_"+dto.getId());
        if(!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if(customProcessInstance==null||customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                businessContractMapper.updateByPrimaryKey(dto);
                //流程结束 生成项目树
                edProjectTreeService.genProjectTree(dto.getId());
            }else{
                dto.setProcessName(customProcessInstance.currentTaskName);
            }
        }
        return dto;
    }

    public void saveCustomer(BusinessContractDto item, int id) {
        BusinessCustomer businessCustomer = businessCustomerMapper.selectByPrimaryKey(id);


        businessContractMapper.updateByPrimaryKey(item);
    }

    public List<BusinessBidContract> listBusinessBidContract(int id) {
        List<BusinessBidContract> list = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        List<BusinessBidContract> oList = businessBidContractMapper.selectAll(params);
        oList.forEach(p -> {
            Map<String, Object> attendParams = Maps.newHashMap();
            attendParams.put("deleted", false);
            attendParams.put("bidContractId", p.getId());
            if (PageHelper.count(() -> businessContractMapper.selectAll(attendParams)) == 0) {
                list.add(p);
            }
        });
        return list;
    }

    private boolean isExitBidContract(int id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("bidContractId", id);
        return PageHelper.count(() -> businessContractMapper.selectAll(params)) > 0;
    }

    /**
     * 得到参与的合同列表
     * @param params
     * @param userLogin
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Object> listAttendPagedData(Map params, String userLogin, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("attendUserLogin",userLogin);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessContract item = (BusinessContract) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    /**
     * 得到全部运行的项目
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Object> listAllPagedData(Map params, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("processEnd", true);
        String userLogin = (String) params.get("userLogin");
        String uiSref = "me.all";
        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, uiSref);
        params.put("deptIdList", deptIdList);
        //判断是项目号 项目名称是否为空
        params.put("s", true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessContract item = (BusinessContract) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public BusinessContractDetail getContractDetail(int  contractId) {
        Map params = Maps.newHashMap();
        params.put("contractId", contractId);
        BusinessContractDetail contractDetail = null;
        if (PageHelper.count(() -> businessContractDetailMapper.selectAll(params)) > 0) {
            contractDetail = businessContractDetailMapper.selectAll(params).get(0);
        } else {
            contractDetail = new BusinessContractDetail();
        }
        return contractDetail;
    }
    //导入数据
    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = MyPoiUtil.listTableData(inputStream, 2, 0, false);
        int seq= (int) PageHelper.count(()->businessContractMapper.selectAll(new HashMap()));
        List<BusinessContract> insertList=Lists.newArrayList();
        for(int i=1;i<=list.size();i++) {
            Map map = list.get(i - 1);
            Map params = Maps.newHashMap();
            params.put("projectName", map.get(0).toString());
            params.put("deptName", map.get(6).toString());
            params.put("deleted",false);
            BusinessContract item = new BusinessContract();
            if (PageHelper.count(() -> businessContractMapper.selectAll(params)) > 0) {
                item = businessContractMapper.selectAll(params).get(0);
            }
            item.setProjectName(map.get(0).toString());
            item.setContractType(map.get(1).toString());
            item.setContractNo(map.get(2).toString());
            item.setContractScope(map.get(3).toString());
            item.setContractMoney(BigDecimal.valueOf(Double.valueOf(map.get(4).toString())));
            item.setInvestMoney(BigDecimal.valueOf(Double.valueOf(map.get(5).toString())));//合同额
            item.setDeptName(map.get(6).toString());
            if (hrDeptService.selectByName(map.get(6).toString()) != null) {
                item.setDeptId(hrDeptService.selectByName(map.get(6).toString()).getId());
            } else {
                item.setDeptId(1);
            }

            item.setDeptNames(map.get(7).toString());
            if (hrDeptService.selectByName(map.get(7).toString()) != null) {
                item.setDeptIds(hrDeptService.selectByName(map.get(7).toString()).getId().toString());
            } else {
                item.setDeptIds("0");
            }
            //是否有中标通知书
            if ("是".equals(map.get(8).toString())) {
                item.setBidNotice(true);
            } else {
                item.setBidNotice(false);
            }

            if ("是".equals(map.get(9).toString())) {
                item.setBidBack(true);
            } else {
                item.setBidBack(false);
            }

            item.setAcceptMode(map.get(10).toString());  //业务承接方式

            if ("是".equals(map.get(11).toString())) {
                item.setGroup(true);
            } else {
                item.setGroup(false);
            }

            if ("是".equals(map.get(12).toString())) {
                item.setSigned(true);
            } else {
                item.setSigned(false);
            }
            //是否院签合同
            if ("是".equals(map.get(13).toString())) {
                item.setCompany(true);
            } else {
                item.setCompany(false);
            }
            String signerName = map.get(14).toString().replaceAll("，", ",");
            item.setSignerName(signerName);
            item.setSigner(hrEmployeeSysService.selectByUserName(signerName));
            item.setSignDate(map.get(15).toString());

            if ("是".equals(map.get(16).toString())) {
                item.setEd(true);
            } else {
                item.setEd(false);
            }
            item.setProjectType(map.get(17).toString());
            //项目负责人
            String chargeMenName = map.get(18).toString().replaceAll("，", ",");
            item.setChargeMenName(chargeMenName);
            item.setChargeMen(hrEmployeeSysService.selectByUserName(chargeMenName));
            //执行负责人
            String exeChargeMenName = map.get(19).toString().replaceAll("，", ",");
            item.setExeChargeMenName(exeChargeMenName);
            item.setExeChargeMen(hrEmployeeSysService.selectByUserName(exeChargeMenName));

            item.setProjectProvince(map.get(20).toString());

            item.setProjectCity(map.get(21).toString());

            item.setProjectAddress(map.get(22).toString());

            item.setConstructionArea(BigDecimal.valueOf(Double.valueOf((map.get(23).toString()))));

            item.setFloorArea(BigDecimal.valueOf(Double.valueOf((map.get(24).toString()))));

            item.setRoadLength(BigDecimal.valueOf(Double.valueOf((map.get(25).toString()))));

            item.setOtherScale(map.get(26).toString());

            item.setReportAmount(map.get(27).toString());

            item.setConstructionTime(BigDecimal.valueOf(Double.valueOf(map.get(28).toString())));


            item.setCustomerName(map.get(29).toString()); //客户名称

            item.setCustomerType(map.get(30).toString());

            item.setLinkMan(map.get(31).toString());

            item.setLinkTel(map.get(32).toString());

            item.setLinkMail(map.get(33).toString());

            item.setLinkFax(map.get(34).toString());

            item.setDepositBank(map.get(35).toString()); //开户银行

            item.setBankAccount(map.get(36).toString()); //银行账号

            item.setTaxNo(map.get(37).toString());

            item.setTaxType(map.get(38).toString());
            //用印时间
            item.setStampTime(map.get(39).toString());
            //返回时间
            item.setBackTime(map.get(40).toString());
            //原件份数
            item.setOriginalCount(Integer.parseInt(map.get(41).toString()));

            item.setCopyCount(Integer.parseInt(map.get(42).toString()));

            item.setStampCount(Integer.parseInt(map.get(42).toString()));

            if (StringUtils.isNotEmpty(map.get(44).toString())) {
                item.setRemark(map.get(44).toString());
            } else {
                item.setRemark("  ");
            }
            item.setGmtModified(new Date());
            item.setDeleted(false);
            item.setStageNames("前期咨询阶段,初步设计阶段,施工图设计阶段");
            if (item.getId() == null) {
                item.setCreator(userLogin);
                item.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
                item.setGmtCreate(new Date());
                item.setProcessEnd(true);
                item.setProcessInstanceId("");
                item.setId(0);
                item.setCustomerId(0);
                item.setBidContractId(0);
                item.setSeq(seq++);
            }
            ModelUtil.setNotNullFields(item);
            try {
                BeanValidator.check(item);
            } catch (ParamException e) {
                throw new ParamException("第" + (i + 1) + "行导入失败：" + e.getMessage());
            }
            insertList.add(item);
        }
        int updateNum=0;

        for (BusinessContract businessContract:insertList){
            if (businessContract.getId()!=0){
                businessContractMapper.updateByPrimaryKey(businessContract);
                updateNum++;
            }else {
                businessContract.setProcessInstanceId(0+"");
                businessContractMapper.insert(businessContract);

                EdProjectStep edProjectStep = new EdProjectStep();
                edProjectStep.setProjectId(businessContract.getId());
                edProjectStep.setProjectName(businessContract.getProjectName());
                edProjectStep.setProjectNo(businessContract.getContractNo());
                edProjectStep.setContractNo(businessContract.getContractNo());
                edProjectStep.setStageName("默认阶段");
                edProjectStep.setStepName("默认分期");
                edProjectStep.setCp(false);
                edProjectStep.setCpClosed(false);

                String enLogin_=MyStringUtil.getMultiDotString(userLogin);
                String cnName_=selectEmployeeService.getNameByUserLogin(userLogin);
                edProjectStep.setChargeMan(enLogin_);
                edProjectStep.setChargeManName(cnName_);
                edProjectStep.setExeChargeMan(enLogin_);
                edProjectStep.setExeChargeManName(cnName_);
                edProjectStep.setCreator(enLogin_);
                edProjectStep.setCreatorName(cnName_);
                edProjectStep.setGmtCreate(new Date());
                edProjectStep.setGmtModified(new Date());
                ModelUtil.setNotNullFields(edProjectStep);
                edProjectStepMapper.insert(edProjectStep);
            }
        }
        return updateNum+","+(insertList.size()-updateNum);
    }

    /**
     * 导出数据
     * */
    public HSSFWorkbook downloadExcel(String userLogin) throws IOException {
        Map params=Maps.newHashMap();
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,"business.contract");
        params.put("deptIdList",deptIdList);
        params.put("deleted", false);
        List<BusinessContract> businessContracts = businessContractMapper.selectAll(params);

        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/导出合同台账.xls";
        InputStream in = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFCellStyle cellStyle = sheet.getRow(0).getCell(0).getCellStyle();
        int rowIndex = 1;//起始行
        String regex = "^,*|,*$";//去除字符串前后逗号
        for (BusinessContract item:businessContracts){
            HSSFRow row = sheet.createRow(rowIndex++);
            //工程名称
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(item.getProjectName());
            row.setRowStyle(cellStyle);
            //项目类型
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(item.getContractType());
            row.setRowStyle(cellStyle);
            //合同号
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(item.getContractNo());
            row.setRowStyle(cellStyle);
            //项目行业
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(item.getContractScope());
            row.setRowStyle(cellStyle);
            //合同额
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(item.getContractMoney().toString());
            row.setRowStyle(cellStyle);
            //投资额
            HSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(item.getInvestMoney().toString());
            row.setRowStyle(cellStyle);
            //执行所室
            HSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(item.getDeptName());
            row.setRowStyle(cellStyle);
            //合作部门
            HSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(item.getDeptNames());
            row.setRowStyle(cellStyle);

            //业务承接方式
            HSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(item.getAcceptMode());
            row.setRowStyle(cellStyle);
            //是否又中标通知书
            HSSFCell cell9 = row.createCell(9);
            if (item.getBidNotice()){
                cell9.setCellValue("是");
            }else {
                cell9.setCellValue("否");
            }
            row.setRowStyle(cellStyle);
            //中标通知书是否返回市场
            HSSFCell cell10 = row.createCell(10);
            if (item.getBidBack()){
                cell10.setCellValue("是");
            }else {
                cell10.setCellValue("否");
            }
            row.setRowStyle(cellStyle);
            //主合同是否签订
            HSSFCell cell11 = row.createCell(11);
            if (item.getSigned()){
                cell11.setCellValue("是");
            }else {
                cell11.setCellValue("否");
            }
            row.setRowStyle(cellStyle);
            //是否院签合同
            HSSFCell cell12 = row.createCell(12);
            if (item.getCompany()){
                cell12.setCellValue("是");
            }else {
                cell12.setCellValue("否");
            }
            row.setRowStyle(cellStyle);
            //是否集团内部签订
            HSSFCell cell13 = row.createCell(13);
            if (item.getGroup()){
                cell13.setCellValue("是");
            }else {
                cell13.setCellValue("否");
            }
            row.setRowStyle(cellStyle);
            //签订人
            HSSFCell cell14 = row.createCell(14);
            cell14.setCellValue(item.getSignerName());
            row.setRowStyle(cellStyle);
            //签订日期
            HSSFCell cell15 = row.createCell(15);
            cell15.setCellValue(item.getSignDate());
            row.setRowStyle(cellStyle);
            //项目负责人
            HSSFCell cell16 = row.createCell(16);
            cell16.setCellValue(item.getChargeMenName());
            row.setRowStyle(cellStyle);
            //执行负责人
            HSSFCell cell17 = row.createCell(17);
            cell17.setCellValue(item.getChargeMenName());
            row.setRowStyle(cellStyle);
            //项目区域（省份）
            HSSFCell cell18 = row.createCell(18);
            cell18.setCellValue(item.getProjectProvince());
            row.setRowStyle(cellStyle);

            //项目区域（区域）
            HSSFCell cell19 = row.createCell(19);
            cell19.setCellValue(item.getProjectCity());
            row.setRowStyle(cellStyle);

            //工程地址
            HSSFCell cell20 = row.createCell(20);
            cell20.setCellValue(item.getProjectAddress());
            row.setRowStyle(cellStyle);
            //建筑面积
            HSSFCell cell21 = row.createCell(21);
            cell21.setCellValue(item.getConstructionArea().toString());
            row.setRowStyle(cellStyle);
            //占地面积
            HSSFCell cell22 = row.createCell(22);
            cell22.setCellValue(item.getFloorArea().toString());
            row.setRowStyle(cellStyle);
            //道路长度
            HSSFCell cell23 = row.createCell(23);
            cell23.setCellValue(item.getRoadLength().toString());
            row.setRowStyle(cellStyle);
            //其他规模
            HSSFCell cell24 = row.createCell(24);
            cell24.setCellValue(item.getOtherScale());
            row.setRowStyle(cellStyle);
            //报量情况
            HSSFCell cell25 = row.createCell(25);
            cell25.setCellValue(item.getReportAmount());
            row.setRowStyle(cellStyle);
            //工期
            HSSFCell cell26 = row.createCell(26);
            cell26.setCellValue(item.getConstructionTime().toString());
            row.setRowStyle(cellStyle);
            //客户名称
            HSSFCell cell27= row.createCell(27);
            cell27.setCellValue(item.getCustomerName());
            row.setRowStyle(cellStyle);
            //单位性质
            HSSFCell cell28 = row.createCell(28);
            cell28.setCellValue(item.getCustomerType());
            row.setRowStyle(cellStyle);
            //联系人
            HSSFCell cell29 = row.createCell(29);
            cell29.setCellValue(item.getLinkMan());
            row.setRowStyle(cellStyle);
            //联系电话
            HSSFCell cell30 = row.createCell(30);
            cell30.setCellValue(item.getLinkTel());
            row.setRowStyle(cellStyle);
            //电子邮件
            HSSFCell cell31 = row.createCell(31);
            cell31.setCellValue(item.getLinkMail());
            row.setRowStyle(cellStyle);
            //传真
            HSSFCell cell32 = row.createCell(32);
            cell32.setCellValue(item.getLinkFax());
            row.setRowStyle(cellStyle);
            //开户银行
            HSSFCell cell33 = row.createCell(33);
            cell33.setCellValue(item.getDepositBank());
            row.setRowStyle(cellStyle);
            //纳税人识别号
            HSSFCell cell34 = row.createCell(34);
            cell34.setCellValue(item.getTaxNo());
            row.setRowStyle(cellStyle);

            //纳税人主体资格
            HSSFCell cell35 = row.createCell(35);
            cell35.setCellValue(item.getTaxType());
            row.setRowStyle(cellStyle);

            //用印时间
            HSSFCell cell36 = row.createCell(36);
            cell36.setCellValue(item.getStampTime());
            row.setRowStyle(cellStyle);

            //返回时间
            HSSFCell cell37 = row.createCell(37);
            cell37.setCellValue(item.getBackTime());
            row.setRowStyle(cellStyle);

            //原件份数
            HSSFCell cell38 = row.createCell(38);
            cell38.setCellValue(item.getOriginalCount());
            row.setRowStyle(cellStyle);

            //复印件份数
            HSSFCell cell39 = row.createCell(39);
            cell39.setCellValue(item.getCopyCount());
            row.setRowStyle(cellStyle);
            //用印份数
            HSSFCell cell40 = row.createCell(40);
            cell40.setCellValue(item.getStampCount());
            row.setRowStyle(cellStyle);
            //备注
            HSSFCell cell41 = row.createCell(41);
            cell40.setCellValue(item.getRemark());
            row.setRowStyle(cellStyle);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return workbook;

    }

    /**
     * 导出精简数据
     * */
    public HSSFWorkbook downloadSimpleExcel(String userLogin) throws IOException {
        Map params=Maps.newHashMap();
        params.put("deleted", false);
        List<BusinessContract> businessContracts = businessContractMapper.selectAll(params);

        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/合同台账(精简).xls";
        InputStream in = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFCellStyle cellStyle = sheet.getRow(1).getCell(1).getCellStyle();
        int rowIndex = 2;//起始行
        String regex = "^,*|,*$";//去除字符串前后逗号
        for (BusinessContract item:businessContracts){
            HSSFRow row = sheet.createRow(rowIndex++);
            //工程名称
            HSSFCell cell0 = row.createCell(0);
            if(!Strings.isNullOrEmpty(item.getProjectName())){
                cell0.setCellValue(item.getProjectName());
            }
            row.setRowStyle(cellStyle);

            //合同号
            HSSFCell cell2 = row.createCell(1);
            if(!Strings.isNullOrEmpty(item.getContractNo())) {
                cell2.setCellValue(item.getContractNo());
            }
            row.setRowStyle(cellStyle);

            //执行所室
            HSSFCell cell6 = row.createCell(2);
            if(!Strings.isNullOrEmpty(item.getDeptName())) {
                cell6.setCellValue(item.getDeptName());
            }
            row.setRowStyle(cellStyle);

            //项目类型
            HSSFCell cell1 = row.createCell(3);
            if(!Strings.isNullOrEmpty(item.getContractType())) {
                cell1.setCellValue(item.getContractType());
            }
            row.setRowStyle(cellStyle);


            //项目负责人
            HSSFCell cell16 = row.createCell(4);
            if(!Strings.isNullOrEmpty(item.getChargeMenName())) {
                cell16.setCellValue(item.getChargeMenName());
            }
            row.setRowStyle(cellStyle);
            //执行负责人
            HSSFCell cell17 = row.createCell(5);
            if(!Strings.isNullOrEmpty(item.getExeChargeMenName())) {
                cell17.setCellValue(item.getExeChargeMenName());
            }
            row.setRowStyle(cellStyle);
            //创建时间
            HSSFCell cell18 = row.createCell(6);

            cell18.setCellValue(DateFormatUtils.format(item.getGmtCreate(),"yyyy-MM-dd"));

            row.setRowStyle(cellStyle);

        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return workbook;

    }

    /**
     * 按照权限部门 统计相应数据
     * @param userLogin
     * @param searchTime 2020-1
     * @return
     */
    public List<Map<String ,Object>>  statisticByUserLogin(String userLogin,String searchTime){

        List<Map<String ,Object>> list=Lists.newArrayList();

        Map params=Maps.newHashMap();
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,"business.contract");
        params.put("deptIdList",deptIdList);
        params.put("deleted", false);
        List<BusinessContract> businessContracts = businessContractMapper.selectAll(params);
        if ("".equals(searchTime)){
            list.add(searchByMonth(businessContracts,"",""));
        }else {
            String[] split = searchTime.split("-");
            if (split.length>1){
                String month = String.format("%02d", Integer.parseInt(split[1]));
                list.add(searchByMonth(businessContracts,split[0],month));
            }else if (split.length==1){
                for (int i=1;i<13;i++){
                    String format = String.format("%02d", i);
                    list.add(searchByMonth(businessContracts,split[0],format ));
                }
            }
        }

        return list;
    }

    private Map<String ,Object> searchByMonth(List<BusinessContract> businessContracts,String year,String month){
        Map<String ,Object> contractMessage=Maps.newHashMap();
        int contractNum=0;
        double contractMoney=0.0;
       if (!"".equals(month)){
           for (BusinessContract item:businessContracts) {
               if (item.getSignDate().contains(year+"-"+month)){
                   contractNum++;
                   contractMoney=contractMoney+Double.valueOf(item.getContractMoney().toString());
               }
           }
           //年份
           contractMessage.put("year",year);
           //月份
           contractMessage.put("month",month);
       }else {
           for (BusinessContract item:businessContracts) {
               contractNum++;
               contractMoney=contractMoney+Double.valueOf(item.getContractMoney().toString());
           }
           //年份
           contractMessage.put("year","-");
           //月份
           contractMessage.put("month","-");
       }
        //合同个数
        contractMessage.put("contractNum",contractNum);
        //合同金额
        contractMessage.put("contractMoney",contractMoney);
        return contractMessage;
    }

}
