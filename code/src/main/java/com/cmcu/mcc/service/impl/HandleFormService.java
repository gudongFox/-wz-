package com.cmcu.mcc.service.impl;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessHandleService;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dao.CommonFormMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.DeptDto;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.service.CommonEdArrangeUserService;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.budget.dao.*;
import com.cmcu.mcc.budget.entity.*;
import com.cmcu.mcc.business.dao.*;


import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.business.entity.BusinessCustomer;
import com.cmcu.mcc.business.entity.BusinessSubpackage;
import com.cmcu.common.service.IHandleFormService;
import com.cmcu.mcc.business.service.BusinessContractService;
import com.cmcu.mcc.business.service.FiveBusinessAdvanceSevice;
import com.cmcu.mcc.business.service.FiveBusinessContractReviewService;
import com.cmcu.mcc.ed.dao.EdProjectTreeMapper;
import com.cmcu.mcc.ed.entity.EdProjectTree;
import com.cmcu.mcc.finance.dao.*;
import com.cmcu.mcc.finance.dao.FiveFinanceLoanMapper;
import com.cmcu.mcc.finance.entity.*;
import com.cmcu.mcc.finance.entity.FiveFinanceLoan;
import com.cmcu.mcc.five.dao.*;
import com.cmcu.mcc.five.entity.*;
import com.cmcu.mcc.five.service.*;
import com.cmcu.mcc.hr.dao.HrInventMapper;
import com.cmcu.mcc.hr.entity.HrInvent;
import com.cmcu.mcc.oa.dao.OaNoticeApplyMapper;
import com.cmcu.mcc.oa.dao.OaStampApplyMapper;
import com.cmcu.mcc.oa.entity.OaNoticeApply;
import com.cmcu.mcc.oa.entity.OaStampApply;
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseDetailMapper;
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseFileMapper;
import com.cmcu.mcc.supervise.dao.FiveOaSuperviseMapper;
import com.cmcu.mcc.supervise.entity.FiveOaSupervise;
import com.cmcu.mcc.supervise.entity.FiveOaSuperviseDetail;
import com.cmcu.mcc.supervise.entity.FiveOaSuperviseFile;
import com.cmcu.mcc.supervise.service.FiveOaSuperviseService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HandleFormService implements IHandleFormService {



    @Resource
    CommonFormMapper commonFormMapper;

    @Resource
    CommonFormDetailMapper commonFormDetailMapper;

    @Resource
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    ProcessQueryService processQueryService;
    @Autowired
    BusinessCustomerMapper businessCustomerMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    ProcessHandleService processHandleService;

    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;
    @Resource
    FiveOaDecisionMakingMapper fiveOaDecisionMakingMapper;
    @Resource
    FiveOaInformationPlanMapper fiveOaInformationPlanMapper;
    @Resource
    FiveOaSecretSystemMapper fiveOaSecretSystemMapper;
    @Resource
    FiveEdMajorDrawingCheckMapper fiveEdMajorDrawingCheckMapper;
    @Resource
    FiveEdDesignDrawingCheckMapper fiveEdDesignDrawingCheckMapper;
    @Resource
    FiveOaInformationEquipmentExamineListMapper fiveOaInformationEquipmentExamineListMapper;
    @Resource
    FiveOaExternalResearchProjectApplyMapper fiveOaExternalResearchProjectApplyMapper;
    @Resource
    FiveOaExternalStandardApplyMapper fiveOaExternalStandardApplyMapper;
    @Resource
    FiveOaResearchProjectReviewMapper fiveOaResearchProjectReviewMapper;
    @Resource
    EdProjectTreeMapper edProjectTreeMapper;
    @Resource
    CommonFormDataService commonFormDataService;
    @Resource
    FiveOaFurniturePurchaseService fiveOaFurniturePurchaseService;
    @Resource
    FiveOaSuperviseService fiveOaSuperviseService;
    @Resource
    FiveOaCardChangeService fiveOaCardChangeService;
    @Resource
    FiveOaResearchProjectReviewSevice fiveOaResearchProjectReviewSevice;
    @Resource
    FiveOaCarService fiveOaCarService;
    @Resource
    FiveOaInformationEquipmentExamineListService fiveOaInformationEquipmentExamineListService;
    @Resource
    FiveBusinessContractReviewService fiveBusinessContractReviewService;
    @Resource
    BusinessContractService businessContractService;
    @Resource
    FiveBusinessTenderDocumentReviewMapper fiveBusinessTenderDocumentReviewMapper;



    /**
     * 1.删除流程 2.删除通用表单 3.删除单独表单
     * @param businessKey
     * @param enLogin
     */
    @Override
    public void removeProcessInstance(String businessKey, String enLogin) {
        if (StringUtils.isNotEmpty(businessKey)) {
            processHandleService.deleteProcessInstanceByBusinessKey(businessKey, enLogin, "");
            deleteFormData(businessKey,enLogin);
        }
    }


    /**
     * 2.删除通用表单 3.删除单独表单
     * @param businessKey
     * @param enLogin
     */
    @Override
    public void deleteFormData(String businessKey, String enLogin) {
        if (StringUtils.isNotEmpty(businessKey)) {

            Map params = Maps.newHashMap();
            params.put("businessKey", businessKey);
            params.put("deleted", false);
            commonFormDataMapper.selectAll(params).forEach(p -> {
                p.setDeleted(true);
                p.setGmtModified(new Date());
                commonFormDataMapper.updateByPrimaryKey(p);
            });

            if (businessKey.startsWith("fiveEdTask_")) {//设计任务书
                fiveEdTaskMapper.selectAll(params).forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdTaskMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaGoAbroadTrainAsk_")) { //出国培训申请
                List<FiveOaGoAbroadTrainAsk> list = fiveOaGoAbroadTrainAskMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaGoAbroadTrainAskMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdReviewMajor_")) { //专业方案讨论
                List<FiveEdReviewMajor> list = fiveEdReviewMajorMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdReviewMajorMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaMeetingRoomApply_")) { //会议室申请
                List<FiveOaMeetingRoomApply> list = fiveOaMeetingRoomApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMeetingRoomApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaDecisionMaking_")) { //决策会议
                List<FiveOaDecisionMaking> list = fiveOaDecisionMakingMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDecisionMakingMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessCustomer_")) {//客户信息录入
                List<BusinessCustomer> list = fiveBusinessCustomerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessCustomerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessSubpackage_")||businessKey.startsWith("fiveBusinessPurchase")) {//分包采购评审
                List<BusinessSubpackage> list = businessSubpackageMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessSubpackageMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdQualityCheck_")) {//质量抽查审校记录单
                List<FiveEdQualityCheck> list = fiveEdQualityCheckMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdQualityCheckMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdQualityAnalysis_")) {//设计质量问题剖析
                List<FiveEdQualityAnalysis> list = fiveEdQualityAnalysisMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdQualityAnalysisMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdReturnVisit_")) {//工程设计回访记录
                List<FiveEdReturnVisit> list = fiveEdReturnVisitMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdReturnVisitMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdStamp_")) {//设计成果用印申请
                List<FiveEdStamp> list = fiveEdStampMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdStampMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdArrange_")) {//人员与计划安排
                List<FiveEdArrange> list = fiveEdArrangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdArrangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdDesignRule_")) {//设计指导性文件
                List<FiveEdDesignRule> list = fiveEdDesignRuleMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdDesignRuleMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdDesignDrawingCheck_")) {//设计图纸资料交验
                List<FiveEdDesignDrawingCheck> list = fiveEdDesignDrawingCheckMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdDesignDrawingCheckMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdMajorDrawingCheck_")) {//专业图纸验收清单
                List<FiveEdMajorDrawingCheck> list = fiveEdMajorDrawingCheckMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdReviewPlan_")) {//总体方案评审
                List<FiveEdReviewPlan> list = fiveEdReviewPlanMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdReviewPlanMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdReviewSpecial_")) {//专项评审
                List<FiveEdReviewSpecial> list = fiveEdReviewSpecialMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdReviewSpecialMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdHouseRefer_")) {//设计协作提资单
                List<FiveEdHouseRefer> list = fiveEdHouseReferMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdHouseReferMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdValidate_")) {//设计文件校审单 fiveEdOutReview
                List<FiveEdHouseValidate> list = fiveEdHouseValidateMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdHouseValidateMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdOutReview_")) {//外部设计评审
                List<FiveEdOutReview> list = fiveEdOutReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdOutReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdServiceChange_")) {//设计变更通知单
                List<FiveEdServiceChange> list = fiveEdServiceChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdServiceChangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdServiceDiscuss_")) {//建工、施工单位变更设计洽商单
                List<FiveEdServiceDiscuss> list = fiveEdServiceDiscussMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdServiceDiscussMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdServiceHandle_")) {//技术服务问题处理单
                List<FiveEdServiceHandle> list = fiveEdServiceHandleMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdServiceHandleMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdPlotApply_")) {//出图申请单
                List<FiveEdPlotApply> list = fiveEdPlotApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdPlotApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSuperviseDetail_")) {//督办子项
                List<FiveOaSuperviseDetail> list = fiveOaSuperviseDetailMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseDetailMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSupervise_")) {//常规督办
                List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSuperviseYear_")) {//年度重点任务督办
                List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSuperviseFile_")) {//文件督办
                List<FiveOaSuperviseFile> list = fiveOaSuperviseFileMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseFileMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrQualifyProChief_")) {//兼职总设计师资格认定
                List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyChiefMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrQualifyChief_")) {//总设计师资格认定
                List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyChiefMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrApproveApply_")) {//审定人资格认定申请
                List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrDesignQualifyApply_")) {//设计校审人员资格认定
                List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaNewsExamine_")) {//新闻宣传及信息报送审查
                List<FiveOaNewsexamine> list = fiveOaNewsexamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaNewsexamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaProjectFundPlan_")) {//项目资金使用计划
                List<FiveOaProjectfundplan> list = fiveOaProjectfundplanMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaProjectfundplanMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInventPayment_")) {//专利缴费申请
                List<FiveOaInventPayment> list = fiveOaInventPaymentMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInventPaymentMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaScientificTaskCostApply_")) {//科研课题费用申请
                List<FiveOaScientificTaskCostApply> list = fiveOaScientificTaskCostApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaScientificTaskCostApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInventApply_")) {//专利申请
                List<HrInvent> list = hrInventMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    hrInventMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaNoIndependentDeptSet_")) {//非独立运行中心设立申请表
                List<FiveOaNonIndependentDeptSet> list = fiveOaNonIndependentDeptSetMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaNonIndependentDeptSetMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaMaterialPurchase_")) {// 资料采购申请表
                List<FiveOaMaterialPurchase> list = fiveOaMaterialPurchaseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMaterialPurchaseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaMaterialBorrow_")) {//  档案资料借阅/电子复制申请表
                List<FiveOaMaterialBorrow> list = fiveOaMaterialBorrowMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMaterialBorrowMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaBidApply_")) {//   投标申请
                List<FiveOaBidApply> list = fiveOaBidApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaBidApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaContractSign_")) {//   业务合同签发单
                List<FiveOaContractSign> list = fiveOaContractSignMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaContractSignMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaPlatformRecord_")) {//   各地公共资源平台备案
                List<FiveOaPlatformRecord> list = fiveOaPlatformRecordMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaPlatformRecordMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeRetireExamine_")) {//   职工退休审批单
                List<FiveOaEmployeeRetireExamine> list = fiveOaEmployeeRetireExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeRetireExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaFurniturePurchase_")) {//   办公家具采购
                List<FiveOaFurniturePurchase> list = fiveOaFurniturePurchaseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaFurniturePurchaseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessRecord_")) {//   项目信息备案
                List<BusinessRecord> list = businessRecordMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessRecordMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeDimissionExamine_")) {//   职工离职审批单
                List<FiveOaEmployeeDimissionExamine> list = fiveOaEmployeeDimissionExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeDimissionExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeTransferExamine_")) {//   职工内部调整审批单
                List<FiveOaEmployeeTransferExamine> list = fiveOaEmployeeTransferExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeTransferExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeEntryExamine_")) {//   职工入职审批单
                List<FiveOaEmployeeEntryExamine> list = fiveOaEmployeeEntryExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeEntryExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaGoAbroad_")) {//    因公出国内部审批文单
                List<FiveOaGoAbroad> list = fiveOaGoAbroadMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaGoAbroadMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerChange_")) {//    策略变更申请
                List<FiveOaComputerChange> list = fiveOaComputerChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerChangeMapper.updateByPrimaryKey(p);
                });
            }  else if (businessKey.startsWith("fiveOaInformationPlan")) {// 年度软件采购预算
                List<FiveOaInformationPlan> list = fiveOaInformationPlanMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationPlanMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaProcessDevelopApply_")) {//流程开发申请
                List<FiveOaProcessDevelopApply> list = fiveOaProcessDevelopApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaProcessDevelopApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerApplication_")) {//公用计算机及U盘申请
                List<FiveOaComputerApplication> list = fiveOaComputerApplicationMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerApplicationMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSecretSystem_")) {//涉密信息系统账户及权限开通、变更申请
                List<FiveOaSecretSystem> list = fiveOaSecretSystemMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSecretSystemMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentProcurement_")) {//    年度信息化设备采购预算表
                List<FiveOaInformationEquipmentProcurement> list = fiveOaInformationEquipmentProcurementMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentProcurementMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerNetwork_")) {//     非密计算机入网审批
                List<FiveOaComputerNetwork> list = fiveOaComputerNetworkMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerNetworkMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentApply_")) {//     信息化设备购置申请单
                List<FiveOaInformationEquipmentApply> list = fiveOaInformationEquipmentApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentApplyMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaSoftware_")) {//     软件购置、审计、服务申请
                List<FiveOaSoftware> list = fiveOaSoftwareMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSoftwareMapper.updateByPrimaryKey(p);
                });
            }else if (businessKey.startsWith("fiveOaPrivilegeManagement_")) {//权限跳转
                List<FiveOaPrivilegeManagement> list = fiveOaPrivilegeManagementMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaPrivilegeManagementMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaNonSecretEquipmentScrap_")) {// 非密计算机及外设报废申请
                List<FiveOaNonSecretEquipmentScrap> list = fiveOaNonSecretEquipmentScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaNonSecretEquipmentScrapMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaComputerMaintain_")) {//     计算机及外设维修服务
                List<FiveOaComputerMaintain> list = fiveOaComputerMaintainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerMaintainMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerPersonRepair_")) {//     计算机及外设维修服务
                List<FiveOaComputerPersonRepair> list = fiveOaComputerPersonRepairMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerPersonRepairMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaPressurePipSealExamine_")) {//     压力管道设计资格印章使用审批表
                List<FiveOaPressurePipSealExamine> list = fiveOaPressurePipSealExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaPressurePipSealExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaTechnologyArticleExamine_")) {//     对外发表科技论文审查单
                List<FiveOaTechnologyArticleExamine> list = fiveOaTechnologyArticleExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaTechnologyArticleExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaOutTechnicalExchange_")) {//     参加外部技术交流会议审批
                List<FiveOaOutTechnicalExchange> list = fiveOaOutTechnicalExchangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaOutTechnicalExchangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaProfessionalSkillTrain_")) {//     专业技术培训申请表
                List<FiveOaProfessionalSkillTrain> list = fiveOaProfessionalSkillTrainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaProfessionalSkillTrainMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaDeptJournal_")) {//     院刊审查
                List<FiveOaDeptJournal> list = fiveOaDeptJournalMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDeptJournalMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaGoAbroadTrainDeclare_")) {//     技术培训申报
                List<FiveOaGoAbroadTrainDeclare> list = fiveOaGoAbroadTrainDeclareMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaGoAbroadTrainDeclareMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaOutSpecialist_")) {//     外部专家申请表
                List<FiveOaOutSpecialist> list = fiveOaOutSpecialistMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaOutSpecialistMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssociationPayment_")) {//     协会缴费
                List<FiveOaAssociationPayment> list = fiveOaAssociationPaymentMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaAssociationPaymentMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssociationChange_")) {//     协会信息变更
                List<FiveOaAssociationChange> list = fiveOaAssociationChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaAssociationChangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInlandProjectApply_")) {//内部项目申请
                List<FiveOaInlandProjectApply> list = fiveOaInlandProjectApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInlandProjectApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssociationApply_")) {//     入协会申请
                List<FiveOaAssociationApply> list = fiveOaAssociationApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaAssociationApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaDepartmentPost_")) {//     部门发文单
                List<FiveOaDepartmentPost> list = fiveOaDepartmentPostMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDepartmentPostMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaFileSynergy_")) {//     协同文件
                List<FiveOaFileSynergy> list = fiveOaFileSynergyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaFileSynergyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentExamine_")) {//     信息化设备验收审批表
                List<FiveOaInformationEquipmentExamine> list = fiveOaInformationEquipmentExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentExamineList_")) {//   信息化设备验收多台审批表
                List<FiveOaInformationEquipmentExamineList> list = fiveOaInformationEquipmentExamineListMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentExamineListMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetComputer_")) {//     公司非密计算机及信息化设备台帐
                List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetComputerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessContractReview_")) {//     合同评审
                List<FiveBusinessContractReview> list = fiveBusinessContractReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessContractReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessPreContract_")) {//     超前任务单
                List<BusinessPreContract> list = businessPreContractMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessPreContractMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessContract_")) {//     项目启动
                BusinessContract businessContract= businessContractMapper.selectByPrimaryKey(Integer.valueOf(businessKey.split("_")[1]));
                businessContract.setDeleted(true);
                businessContractMapper.updateByPrimaryKey(businessContract);
            } else if (businessKey.startsWith("businessContract_")) {//     项目启动
                BusinessContract businessContract = businessContractMapper.selectByPrimaryKey(Integer.valueOf(businessKey.split("_")[1]));
                businessContract.setDeleted(true);
                businessContractMapper.updateByPrimaryKey(businessContract);
            }
          else if (businessKey.startsWith("oaNoticeApply_")) {//     信息发布
                List<OaNoticeApply> list = oaNoticeApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    oaNoticeApplyMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessIncome_")) {//     合同收费
                List<BusinessIncome> list = businessIncomeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessIncomeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaDispatch_")) {//公司发文
                List<FiveOaDispatch> list = fiveOaDispatchMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDispatchMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaFileInstruction_")) {//文件批示单
                List<FiveOaFileInstruction> list = fiveOaFileInstructionMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaFileInstructionMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaReport_")) {//报告文单
                List<FiveOaReport> list = fiveOaReportMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaReportMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaContractLawExamine_")) {//合同法律审查工作单
                List<FiveOaContractLawExamine> list = fiveOaContractLawExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaContractLawExamineMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaSignQuote_")) {//签报
                List<FiveOaSignQuote> list = fiveOaSignQuoteMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSignQuoteMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveEdQualityIssue_")) {//重大设计问题
                List<FiveEdQualityIssue> list = fiveEdQualityIssueMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdQualityIssueMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceTravelExpense")) {//差旅费报销
                List<FiveFinanceTravelExpense> list = fiveFinanceTravelExpenseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceTravelExpenseMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceReimburse")) {//费用报销
                List<FiveFinanceReimburse> list = fiveFinanceReimburseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceReimburseMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaMeetingArrange_")) {//会议安排
                List<FiveOaMeetingArrange> list = fiveOaMeetingArrangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMeetingArrangeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceIncome_")) {//收入管理
                List<FiveFinanceIncome> list = fiveFinanceIncomeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceIncomeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceInvoice_")) {//发票申请
                List<FiveFinanceInvoice> list = fiveFinanceInvoiceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceInvoiceMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceInvoiceCancel_")) {//发票作废申请
                List<FiveFinanceInvoiceCancel> list = fiveFinanceInvoiceCancelMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceInvoiceCancelMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceInvoiceCollection_")) {//发票应收款催款
                List<FiveFinanceInvoiceCollection> list = fiveFinanceInvoiceCollectionMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceInvoiceCollectionMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceIncomeConfirm_")) {//收入确认
                List<FiveFinanceIncomeConfirm> list = fiveFinanceIncomeConfirmMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceNode_")) {//票据管理
                List<FiveFinanceNode> list = fiveFinanceNodeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceNodeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceReceipt_")) {//收据管理
                List<FiveFinanceReceipt> list = fiveFinanceReceiptMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceReceiptMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceTransferAccountsFinance_")||businessKey.startsWith("fiveFinanceTransferAccountsCommon")||businessKey.startsWith("fiveFinanceTransferAccountsRed")||businessKey.startsWith("fiveFinanceTransferAccountsBuild")||businessKey.startsWith("fiveFinanceTransferFee")) {//费用申请
                List<FiveFinanceTransferAccounts> list = fiveFinanceTransferAccountsMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceTransferAccountsMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceLoanFunction")||businessKey.startsWith("fiveFinanceLoanCommon")
                    ||businessKey.startsWith("fiveFinanceLoanRed")||businessKey.startsWith("fiveFinanceLoanBuild")) {//借款
                List<FiveFinanceLoan> list = fiveFinanceLoanMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceLoanMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessContractLibrary_")) {//合同库
                List<FiveBusinessContractLibrary> list = fiveBusinessContractLibraryMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessContractLibrarySubpackage_")) {//分包合同库
                List<FiveBusinessContractLibrarySubpackage> list = fiveBusinessContractLibrarySubpackageMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("oaCarMaintain")) {//车辆维护
                List<FiveOaCarMaintain> list = fiveOaCarMaintainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCarMaintainMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaSelfCarApply")) {//个人用车
                List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCarApplyMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaLeaderCarApply")) {//领导用车
                List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCarApplyMapper.updateByPrimaryKey(p);
                });
            }else if (businessKey.startsWith("fiveOaCardChange")) {//职工充值卡变动
                List<FiveOaCardChange> list = fiveOaCardChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCardChangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEquipmentAcceptance")) {//设备验收
                List<FiveOaEquipmentAcceptance> list = fiveOaEquipmentAcceptanceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEquipmentAcceptanceMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetAllot_")) {//固定资产调拨单
                List<FiveAssetAllot> list = fiveAssetAllotMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetAllotMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveComputerScrap_")) {//非密计算机及外设报废审批
                List<FiveComputerScrap> list = fiveComputerScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveComputerScrapMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetScrap_")) {//非密计算机及外设报废审批
                List<FiveAssetScrap> list = fiveAssetScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetScrapMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssetComputer_")) {//非密计算机及台账
                List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetComputerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetScrap_")) {//非密计算机及外设报废审批
                List<FiveAssetScrap> list = fiveAssetScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetScrapMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceBalance")) {//资金余额上报
                List<FiveFinanceBalance> list = fiveFinanceBalanceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceBalanceMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceProjectBudget")) {//项目预算
                List<FiveFinanceProjectBudget> list = fiveFinanceProjectBudgetMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceProjectBudgetMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceDeptBudget")) {//事业部预算
                List<FiveFinanceDeptBudget> list = fiveFinanceDeptBudgetMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceDeptBudgetMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessSupplier")) {//供方信息管理
                List<BusinessSupplier> list = businessSupplierMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessSupplierMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceStampTax")) {//印花税申报
                List<FiveFinanceStampTax> list = fiveFinanceStampTaxMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceStampTaxMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceOutSupply")) {//对外提供资料
                List<FiveFinanceOutSupply> list = fiveFinanceOutSupplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceOutSupplyMapper.updateByPrimaryKey(p);
                });
            }

            else if (businessKey.startsWith("fiveFinanceBackLetter")) {//保函单
                List<FiveFinanceBackLetter> list = fiveFinanceBackLetterMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceBackLetterMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceBackLetter")) {//对外提供资料
                List<FiveFinanceBackLetter> list = fiveFinanceBackLetterMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceBackLetterMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetIndependent")
                    ||businessKey.startsWith("fiveBudgetProduction")
                    ||businessKey.startsWith("fiveBudgetFunction")
                    ||businessKey.startsWith("fiveBudgetPostExpense")) {//独立法人单位预算
                List<FiveBudgetIndependent> list = fiveBudgetIndependentMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetIndependentMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetMaintain")
                    ||businessKey.startsWith("fiveBudgetBusiness")||businessKey.startsWith("fiveBudgetStock")) {//专项维修预算 业务支出 股权投资预算
                List<FiveBudgetMaintain> list = fiveBudgetMaintainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetMaintainMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetScientificFundsOut")) {//科研经费支出
                List<FiveBudgetScientificFundsOut> list = fiveBudgetScientificFundsOutMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetScientificFundsOutMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetScientificFundsIn")) {//科研经费收入
                List<FiveBudgetScientificFundsIn> list = fiveBudgetScientificFundsInMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetScientificFundsInMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetPublicFunds")) {//公用经费预算
                List<FiveBudgetPublicFunds> list = fiveBudgetPublicFundsMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetPublicFundsMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetCapitalOut")
                    ||businessKey.startsWith("fiveBudgetTurnInRent")) {//资本性支出预算
                List<FiveBudgetCapitalOut> list = fiveBudgetCapitalOutMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetCapitalOutMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetLaborCost")
                    ||businessKey.startsWith("fiveBudgetStaffNumber")) {//人工成本 职工人数
                List<FiveBudgetLaborCost> list = fiveBudgetLaborCostMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetLaborCostMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetFeeChange")) {//收费预算更改
                List<FiveBudgetFeeChange> list = fiveBudgetFeeChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetFeeChangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetFee")) {//上缴预算
                List<FiveBudgetFee> list = fiveBudgetFeeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetFeeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetTurnIn")) {//上缴预算
                List<FiveBudgetTurnIn> list = fiveBudgetTurnInMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetTurnInMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceSelfBank")) {//个人网银
                List<FiveFinanceSelfBank> list = fiveFinanceSelfBankMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceSelfBankMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceCompanyBank")) {//公司网银
                List<FiveFinanceCompanyBank> list = fiveFinanceCompanyBankMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceCompanyBankMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessChangeCustomer")) {//客户信息管理
                List<BusinessChangeCustomer> list = businessChangeCustomerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessChangeCustomerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessChangeSupplier")) {//供方信息管理
                List<BusinessChangeSupplier> list = businessChangeSupplierMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessChangeSupplierMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessOutAssist")) {//外协支出
                List<FiveBusinessOutAssist> list = fiveBusinessOutAssistMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessOutAssistMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessCooperationDelegation")) {//经营合作项目确认
                List<FiveBusinessCooperationDelegation> list = fiveBusinessCooperationDelegationMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessCooperationDelegationMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessCooperationContract")) {//内部协作协议合同
                List<FiveBusinessCooperationContract> list = fiveBusinessCooperationContractMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessCooperationContractMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceRefund")) {//还款
                List<FiveFinanceRefund> list = fiveFinanceRefundMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceRefundMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaExternalResearchProjectApply_")) {//外部科研项目申请
                List<FiveOaExternalResearchProjectApply> list = fiveOaExternalResearchProjectApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaExternalResearchProjectApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaExternalStandardApply_")) {//外部标准规范、图集项目申请
                List<FiveOaExternalStandardApply> list = fiveOaExternalStandardApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaExternalStandardApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaResearchProjectReview_")) {//科研项目专家审批
                List<FiveOaResearchProjectReview> list = fiveOaResearchProjectReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaResearchProjectReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessAdvance_")) {//支出明细
                List<FiveBusinessAdvance> list = fiveBusinessAdvanceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessAdvanceMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessAdvanceCollect")) {//支出签发单
                List<FiveBusinessAdvanceCollect> list = fiveBusinessAdvanceCollectMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessAdvanceCollectMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessTenderDocumentReview")) {//工程承包项目招标文件评审（含联合体评审）
                List<FiveBusinessTenderDocumentReview> list = fiveBusinessTenderDocumentReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessTenderDocumentReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("oaStampApplyOffice")) {//用印申请
                List<FiveOaStampApplyOffice> list = fiveOaStampApplyOfficeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaStampApplyOfficeMapper.updateByPrimaryKey(p);
                });
            }


        }
    }



    @Override
    public LinkedHashMap getDefaultFormData(String referType, int referId, String enLogin) {
        return new LinkedHashMap();
    }

    @Override
    public Map getDefaultProcessVariables(String referType, int referId, String enLogin) {
        Map variables = Maps.newHashMap();
        variables.put("initiator", enLogin);
        List<String> deptChargeMen = listDeptChargeMen(enLogin);
        if (deptChargeMen.size() > 0) {
            variables.put("deptChargeMen", deptChargeMen);
            variables.put("deptChargeMan", deptChargeMen.get(0));
        }
        return variables;
    }

    private List<String> listDeptChargeMen(String enLogin) {
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        Optional<DeptDto> deptDto = commonUserService.selectAllDept(userDto.getTenetId()).stream().filter(p -> p.getId()==userDto.getDeptId()).findFirst();
        if (deptDto.isPresent()) {
            List<String> deptChargeMen = deptDto.get().getDeptChargeMen();
            return deptChargeMen;
        }
        return Lists.newArrayList();
    }

    @Override
    public TplConfigDto getTplConfigDto(String processInstanceId,String businessKey, String enLogin) {

        CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(processInstanceId,businessKey,enLogin);
        if (StringUtils.isEmpty(businessKey)&&customProcessInstance!=null) {
            businessKey = customProcessInstance.getInstance().getBusinessKey();
        }
        TplConfigDto item = new TplConfigDto();

        if(StringUtils.isEmpty(businessKey)) return item;

        item.setBusinessKey(businessKey);
        String formKey = getFormKey(businessKey);
        Map params = Maps.newHashMap();
        params.put("formKey", formKey);
        params.put("deleted", false);
        List<CommonForm> commonForms = commonFormMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonForm::getFormVersion).reversed()).collect(Collectors.toList());
        if(commonForms.size()==0&&customProcessInstance!=null) {
            CommonForm commonForm = new CommonForm();
            commonForm.setTenetId(customProcessInstance.getInstance().getTenantId());
            commonForm.setFormKey(formKey);
            commonForm.setFormIcon("icon-note");
            commonForm.setFormDesc(customProcessInstance.getInstance().getProcessDefinitionName());
            commonForm.setFormCategory(customProcessInstance.getProcessCategory());
            commonForm.setFormVersion(1);
            commonForm.setPublished(true);
            commonForm.setDeleted(false);
            commonForm.setCreator(enLogin);
            commonForm.setCreatorName(commonUserService.getCnNames(enLogin));
            commonForm.setGmtModified(new Date());
            commonForm.setGmtCreate(new Date());
            commonForm.setOtherTplConfig("{\"showBusinessFile\":true}");
            ModelUtil.setNotNullFields(commonForm);
            commonFormMapper.insert(commonForm);
            commonForms=commonFormMapper.selectAll(params);
        }


        if (commonForms.stream().anyMatch(CommonForm::getPublished)) {
            CommonForm commonForm = commonForms.stream().filter(CommonForm::getPublished).findFirst().get();
            TplConfigDto findTplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
            item.setShowBusinessFile(findTplConfigDto.isShowBusinessFile());
            item.setShowFileType(findTplConfigDto.isShowFileType());
            item.setFileTypeCode(findTplConfigDto.getFileTypeCode());
            item.setProcessKey(findTplConfigDto.getProcessKey());
            item.setShowFileDir(findTplConfigDto.isShowFileDir());
            item.setShowProcessIntegration(findTplConfigDto.isShowProcessIntegration());
            item.setBusinessFileTip(findTplConfigDto.getBusinessFileTip());
            item.setMarkRoleNames(findTplConfigDto.getMarkRoleNames());
            item.setSelectCoFileType(findTplConfigDto.getSelectCoFileType());
            item.setAutoSubmit(findTplConfigDto.isAutoSubmit());

            if(item.getMarkRoleNames().size()>0) {
                for(String roleName:item.getMarkRoleNames()){
                    if(customProcessInstance.getMyRunningTaskNameList().stream().anyMatch(p->p.startsWith(roleName))){
                        item.setMarkAddRoleName(roleName);
                        break;
                    }
                }
            }

            CommonFormData commonFormData=commonFormDataMapper.selectByBusinessKey(businessKey);
            if(commonFormData!=null) {
                Map formData = JsonMapper.string2Map(commonFormData.getFormData());
                item.setMajorNames(formData.getOrDefault("majorName", "").toString());
                if(StringUtils.isEmpty(item.getMajorNames())){
                    item.setMajorNames(formData.getOrDefault("sourceMajor", "").toString());
                }
                item.setBuildIds(formData.getOrDefault("buildIds", "").toString());
                item.setStepId(Integer.parseInt(formData.getOrDefault("stepId", "0").toString()));
            }

            checkFormDetail(commonForm,businessKey);
        }
        if (customProcessInstance!=null&&customProcessInstance.getInstance()!=null) {
            item.setTaskList(customProcessInstance.getMyRunningTaskNameList());
            item.setSaveAble(customProcessInstance.isFirstTask());
            item.setEditable(customProcessInstance.getMyRunningTaskNameList().size()>0);
            if (businessKey.contains("fiveBusinessCustomer")){
                String[] key = businessKey.split("_");
                BusinessCustomer businessCustomer = businessCustomerMapper.selectByPrimaryKey(Integer.parseInt(key[1]));
                if(businessCustomer.getCreator().equalsIgnoreCase(enLogin)){
                    item.setSaveAble(true);
                    item.setEditable(true);
                }
            }
            else if (businessKey.contains("fiveOaSuperviseDetail")){
                if (item.getTaskList().stream().anyMatch(p->p.contains("办理人"))) {
                    item.setSaveAble(true);
                    item.setEditable(true);
                }
            }
            else if(businessKey.startsWith("fiveOaLeaderCarApply")||businessKey.startsWith("fiveOaSelfCarApply")){
                if(item.isEditable()){
                    if(customProcessInstance.getMyRunningTaskNameList().stream().anyMatch(p->p.startsWith("司机回执"))){
                        item.setSaveAble(true);
                    }
                }
            }
        }
        else {
            item.setSaveAble(true);
            item.setEditable(true);
            if (businessKey == "ed_arrange") {
                if (item.getTaskList().contains("审定人")) {
                    item.setSaveAble(true);
                }
            }
        }

        return item;
    }

    private String getFormKey(String businessKey) {
        String formKey = "";
        if (businessKey.lastIndexOf("_") >= 0) {
            for(String key: StringUtils.split(businessKey,"_")){
                try {
                    Integer.parseInt(key);
                    break;
                }catch (Exception ex){
                    formKey = formKey + key+"_";
                }
            }
        }
        return formKey.endsWith("_")?formKey.substring(0,formKey.length()-1):formKey;
    }

    @Resource
    FiveOaCarMapper fiveOaCarMapper;
    @Override
    public List<CommonCodeDto> listDataSource(CommonFormData commonFormData, String dataSourceKey, String enLogin) {
        List<CommonCodeDto> list = Lists.newArrayList();
        if(dataSourceKey.equalsIgnoreCase("选择车辆")){
            Map map = new HashMap();
            map.put("deleted",false);
            List<FiveOaCar> cars =fiveOaCarMapper.selectAll(map);
            for(FiveOaCar fiveOaCar:cars) {
                CommonCodeDto dto = new CommonCodeDto();
                dto.setCodeValue(fiveOaCar.getCarNo());
                dto.setName(fiveOaCar.getCarNo());
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public List<Map> listUserJsTree(Map params) {
        return null;
    }

    @Override
    public void checkFormData(String businessKey, String enLogin) {

        if(businessKey.startsWith("fiveEdTask")){





        }
    }

    @Override
    public String getProcessInformation(String businessKey) {
        return "";
    }

    @Override
    public Map afterSaveData(CommonFormData commonFormData, Map newData) {
        String referType=commonFormData.getReferType();
        if(referType.equalsIgnoreCase("fiveEdStamp")){
            if(newData.containsKey("useStamp")){
                List<String> stamps=JsonMapper.getListValue(newData,"useStamp");
                newData.put("pressureStamp",stamps.stream().anyMatch(p->p.contains("压力")));
                newData.put("plotStamp",stamps.stream().anyMatch(p->p.contains("出图")));
                newData.put("constructionStamp",stamps.stream().anyMatch(p->p.contains("施工")));
            }
        }else if(referType.equalsIgnoreCase("fiveEdArrange")){
            commonEdArrangeUserService.buildCoDirData(commonFormData.getReferId(), commonFormData.getReferId().toString());
        } else  if (referType.equalsIgnoreCase("oaStampApplyOffice")){
            //2021-03-01 HNZ 公司用印特殊配置 类型选择了保密办用印或者 军工保密 需要保密审查
            List<String> stamps=JsonMapper.getListValue(newData,"stampName");
            List<String> secrecy= JsonMapper.getListValue(newData,"secrecy");
          if (secrecy.stream().anyMatch(p -> p.contains("true"))||stamps.stream().anyMatch(p -> p.contains("保密办用印"))){
              newData.put("secrecy",true);
          }

        }
        return newData;
    }

    @Resource
    FiveEdMajorDrawingCheckService fiveEdMajorDrawingCheckService;
    @Resource
    FiveEdDesignDrawingCheckService fiveEdDesignDrawingCheckService;

    /**
     * todo:CAD新发起流程
     * @param nodeId
     * @param enLogin
     * @return
     */
    @Override
    public String getNewEdFormData(int nodeId, String enLogin) {
        EdProjectTree tree = edProjectTreeMapper.selectByPrimaryKey(nodeId);
        String businessKey = "";
        String processInstanceId = "";
        if (tree.getNodeUrl().contains("designDrawingCheck")) {//设计与开发策划书
            processInstanceId = fiveEdDesignDrawingCheckService.getNewModel(tree.getForeignKey(), enLogin).getProcessInstanceId();
        } else if (tree.getNodeUrl().contains("designDrawingCheck")) {
            processInstanceId = fiveEdMajorDrawingCheckService.getNewModel(tree.getForeignKey(), enLogin).getProcessInstanceId();
        } else { //通用表单发起
            commonFormDataService.getNewModel(tree.getReferType(), tree.getForeignKey(), enLogin);
        }
        if (StringUtils.isNotEmpty(businessKey) || StringUtils.isNotEmpty(processInstanceId)) {
            TplConfigDto tplConfigDto = getTplConfigDto(processInstanceId, businessKey, enLogin);
            if (StringUtils.isEmpty(businessKey)) businessKey = tplConfigDto.getBusinessKey();
        }

        Assert.state(StringUtils.isNotEmpty(businessKey), "该节点直接发起流程功能,暂未开放,请先在设计管理（树）发起!");
        return businessKey;
    }

    private  void checkFormDetail(CommonForm commonForm,String businessKey) {
        Map<String,Object> data=getMapData(businessKey);
        if(data!=null) {
            Map params = Maps.newHashMap();
            params.put("formId", commonForm.getId());
            params.put("deleted", false);
            if (PageHelper.count(() -> commonFormDetailMapper.selectAll(params)) == 0) {

                List<String> excludeList = Arrays.asList("id", "deleted", "gmtCreate", "gmtModified", "creator", "creatorName","processInstanceId","processEnd");

                Map showNameList = Maps.newHashMap();
                showNameList.put("formNo","表单编号");
                showNameList.put("projectName", "项目名称");
                showNameList.put("stageName", "设计阶段");
                showNameList.put("stepName", "分期名称");
                showNameList.put("projectNo", "工程号");
                showNameList.put("contractNo", "合同号");
                showNameList.put("deptName", "部门名称");
                showNameList.put("deptChargeMenName","部门负责人");
                showNameList.put("remark","备注");
                showNameList.put("majorName","专业");
                showNameList.put("beginTime","开始时间");
                showNameList.put("endTime","结束时间");


                Map hideNameList = Maps.newHashMap();
                hideNameList.put("businessKey","业务标识");
                hideNameList.put("deptId", "部门名称");
                hideNameList.put("stepId", "分期名称");
                hideNameList.put("projectId", "项目名称");
                hideNameList.put("deptChargeMen","部门负责人");
                hideNameList.put("proofreadMen","校对人");
                int seq = 1;
                for (String key : data.keySet()) {
                    if (!excludeList.contains(key)) {
                        CommonFormDetail detail = new CommonFormDetail();
                        detail.setFormId(commonForm.getId());
                        detail.setGroupName("基础信息");
                        detail.setInputType("text");
                        detail.setInputCode(key);
                        detail.setInputText(key);
                        detail.setInputSize(6);
                        detail.setDetailHidden(false);
                        detail.setListHidden(true);
                        Map config=Maps.newHashMap();
                        if(key.endsWith("Men")||key.endsWith("Man")) {
                            config.put("processVariable",true);
                            detail.setDetailHidden(true);
                        }
                        detail.setInputConfig(JsonMapper.obj2String(config));
                        detail.setDetailSeq(seq++);
                        detail.setListSeq(detail.getDetailSeq());
                        if (showNameList.containsKey(key)) {
                            detail.setInputText(showNameList.get(key).toString());
                            detail.setDetailHidden(false);
                        } else if (hideNameList.containsKey(key)) {
                            detail.setInputText(hideNameList.get(key).toString());
                            detail.setDetailHidden(true);
                        }
                        detail.setEditable(false);
                        detail.setEditableTag("creator");
                        detail.setRequired(false);
                        detail.setDeleted(false);
                        detail.setGmtCreate(new Date());
                        detail.setGmtModified(new Date());
                        ModelUtil.setNotNullFields(detail);
                        commonFormDetailMapper.insert(detail);
                    }
                }
            }

            Map formDataParams=Maps.newHashMap();
            formDataParams.put("businessKey",businessKey);

            String formkey=getFormKey(businessKey);
            String creator=data.get("creator").toString();
            UserDto user= commonUserService.selectByEnLogin(creator);

            Date gmtCreate= null;
            Date gmtModified=null;
            try {
                gmtCreate = DateUtils.parseDate(data.get("gmtCreate").toString(),"yyyy-MM-dd HH:mm:ss");
                gmtModified= DateUtils.parseDate(data.get("gmtModified").toString(),"yyyy-MM-dd HH:mm:ss");

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String processInstanceId=data.getOrDefault("processInstanceId","").toString();
            boolean processEnd=Boolean.parseBoolean( data.getOrDefault("processEnd","0").toString());

            List<CommonFormData> formDataList=commonFormDataMapper.selectAll(formDataParams);
            if(formDataList.size()==0) {
                CommonFormData item = new CommonFormData();
                item.setTenetId("wuzhou");
                item.setReferType(formkey);
                item.setReferId(0);
                if(data.containsKey("stepId")){
                    item.setReferId(Integer.parseInt(data.get("stepId").toString()));
                } else if(data.containsKey("projectId")){
                    item.setReferId(Integer.parseInt(data.get("projectId").toString()));
                }else if(data.containsKey("deptId")){
                    item.setReferId(Integer.parseInt(data.get("deptId").toString()));
                }


                item.setBusinessKey(businessKey);
                item.setFormKey(commonForm.getFormKey());
                item.setFormVersion(commonForm.getFormVersion());
                item.setFormData(JsonMapper.obj2String(data));
                item.setSeq(1);
                item.setDeleted(false);
                item.setCreator(user.getEnLogin());
                item.setCreatorName(user.getCnName());
                item.setDeptId(user.getDeptId());
                item.setDeptName(user.getDeptName());
                item.setGmtCreate(gmtCreate);
                item.setGmtModified(gmtModified);
                item.setProcessInstanceId(processInstanceId);
                item.setProcessEnd(processEnd);
                item.setRemark("同步数据");
                ModelUtil.setNotNullFields(item);
                commonFormDataMapper.insert(item);
            }else{
                CommonFormData item=formDataList.get(0);

                if(item.getGmtModified().getTime()<gmtModified.getTime()||item.getProcessEnd()!=processEnd) {
                    item.setFormData(JsonMapper.obj2String(data));
                    item.setGmtModified(gmtModified);
                    item.setProcessEnd(processEnd);
                    commonFormDataMapper.updateByPrimaryKey(item);
                }
            }

        }
    }

    @Resource
    FiveEdTaskMapper fiveEdTaskMapper;
    @Resource
    FiveOaGoAbroadTrainAskMapper fiveOaGoAbroadTrainAskMapper;
    @Resource
    FiveEdReviewMajorMapper fiveEdReviewMajorMapper;
    @Resource
    FiveOaMeetingRoomApplyMapper fiveOaMeetingRoomApplyMapper;
    @Resource
    BusinessCustomerMapper fiveBusinessCustomerMapper;
    @Resource
    FiveOaNewsexamineMapper fiveOaNewsexamineMapper;
    @Resource
    FiveOaProjectfundplanMapper fiveOaProjectfundplanMapper;
    @Resource
    FiveOaInventPaymentMapper fiveOaInventPaymentMapper;
    @Resource
    FiveOaScientificTaskCostApplyMapper fiveOaScientificTaskCostApplyMapper;
    @Resource
    HrInventMapper hrInventMapper;
    @Resource
    FiveOaNonIndependentDeptSetMapper fiveOaNonIndependentDeptSetMapper;
    @Resource
    FiveOaMaterialPurchaseMapper fiveOaMaterialPurchaseMapper;
    @Resource
    FiveOaMaterialBorrowMapper fiveOaMaterialBorrowMapper;
    @Resource
    FiveOaBidApplyMapper fiveOaBidApplyMapper;
    @Resource
    FiveOaContractSignMapper fiveOaContractSignMapper;
    @Resource
    FiveOaPlatformRecordMapper fiveOaPlatformRecordMapper;
    @Resource
    FiveOaEmployeeRetireExamineMapper fiveOaEmployeeRetireExamineMapper;
    @Resource
    BusinessRecordMapper businessRecordMapper;
    @Resource
    FiveOaEmployeeDimissionExamineMapper fiveOaEmployeeDimissionExamineMapper;
    @Resource
    FiveOaEmployeeTransferExamineMapper fiveOaEmployeeTransferExamineMapper;
    @Resource
    FiveOaEmployeeEntryExamineMapper fiveOaEmployeeEntryExamineMapper;
    @Resource
    FiveOaGoAbroadMapper fiveOaGoAbroadMapper;
    @Resource
    FiveOaComputerChangeMapper fiveOaComputerChangeMapper;
    @Resource
    FiveOaInformationEquipmentProcurementMapper fiveOaInformationEquipmentProcurementMapper;
    @Resource
    FiveOaComputerNetworkMapper fiveOaComputerNetworkMapper;
    @Resource
    FiveOaInformationEquipmentApplyMapper fiveOaInformationEquipmentApplyMapper;
    @Resource
    FiveOaSoftwareMapper fiveOaSoftwareMapper;
    @Resource
    FiveOaNonSecretEquipmentScrapMapper fiveOaNonSecretEquipmentScrapMapper;
    @Resource
    FiveOaComputerMaintainMapper fiveOaComputerMaintainMapper;
    @Resource
    FiveOaComputerPersonRepairMapper fiveOaComputerPersonRepairMapper;

    @Resource
    FiveOaPressurePipSealExamineMapper fiveOaPressurePipSealExamineMapper;
    @Resource
    FiveOaTechnologyArticleExamineMapper fiveOaTechnologyArticleExamineMapper;
    @Resource
    FiveOaOutTechnicalExchangeMapper fiveOaOutTechnicalExchangeMapper;
    @Resource
    FiveOaProfessionalSkillTrainMapper fiveOaProfessionalSkillTrainMapper;
    @Resource
    FiveOaDeptJournalMapper fiveOaDeptJournalMapper;
    @Resource
    FiveOaGoAbroadTrainDeclareMapper fiveOaGoAbroadTrainDeclareMapper;
    @Resource
    FiveOaOutSpecialistMapper fiveOaOutSpecialistMapper;
    @Resource
    FiveOaAssociationPaymentMapper fiveOaAssociationPaymentMapper;
    @Resource
    FiveOaAssociationChangeMapper fiveOaAssociationChangeMapper;
    @Resource
    FiveOaAssociationApplyMapper fiveOaAssociationApplyMapper;
    @Resource
    FiveOaDepartmentPostMapper fiveOaDepartmentPostMapper;
    @Resource
    FiveOaInformationEquipmentExamineMapper fiveOaInformationEquipmentExamineMapper;
    @Resource
    FiveOaPrivilegeManagementMapper fiveOaPrivilegeManagementMapper;
    @Resource
    FiveBusinessContractReviewMapper fiveBusinessContractReviewMapper;
    @Resource
    BusinessPreContractMapper businessPreContractMapper;
    @Resource
    BusinessContractMapper businessContractMapper;
    @Resource
    OaNoticeApplyMapper oaNoticeApplyMapper;
    @Resource
    BusinessIncomeMapper businessIncomeMapper;
    @Resource
    BusinessChangeCustomerMapper businessChangeCustomerMapper;
    @Resource
    BusinessChangeSupplierMapper businessChangeSupplierMapper;

    @Resource
    BusinessSubpackageMapper businessSubpackageMapper;
    @Resource
    FiveEdQualityCheckMapper fiveEdQualityCheckMapper;
    @Resource
    FiveEdQualityAnalysisMapper fiveEdQualityAnalysisMapper;
    @Resource
    FiveEdReturnVisitMapper fiveEdReturnVisitMapper;
    @Resource
    FiveEdStampMapper fiveEdStampMapper;
    @Resource
    FiveEdArrangeMapper fiveEdArrangeMapper;
    @Resource
    FiveEdDesignRuleMapper fiveEdDesignRuleMapper;
    @Resource
    FiveEdReviewPlanMapper fiveEdReviewPlanMapper;
    @Resource
    FiveEdReviewSpecialMapper fiveEdReviewSpecialMapper;
    @Resource
    FiveEdHouseReferMapper fiveEdHouseReferMapper;
    @Resource
    FiveEdHouseValidateMapper fiveEdHouseValidateMapper;
    @Resource
    FiveEdOutReviewMapper fiveEdOutReviewMapper;
    @Resource
    FiveEdServiceChangeMapper fiveEdServiceChangeMapper;
    @Resource
    FiveEdServiceDiscussMapper fiveEdServiceDiscussMapper;
    @Resource
    FiveEdServiceHandleMapper fiveEdServiceHandleMapper;
    @Resource
    FiveEdPlotApplyMapper fiveEdPlotApplyMapper;
    @Resource
    FiveOaSuperviseDetailMapper fiveOaSuperviseDetailMapper;
    @Resource
    FiveOaSuperviseMapper fiveOaSuperviseMapper;
    @Resource
    FiveOaSuperviseFileMapper fiveOaSuperviseFileMapper;
    @Resource
    FiveHrQualifyChiefMapper fiveHrQualifyChiefMapper;
    @Resource
    FiveHrQualifyApplyMapper fiveHrQualifyApplyMapper;
    @Resource
    FiveOaDispatchMapper fiveOaDispatchMapper;
    @Resource
    FiveOaFileInstructionMapper fiveOaFileInstructionMapper;
    @Resource
    FiveOaReportMapper fiveOaReportMapper;
    @Resource
    FiveOaContractLawExamineMapper fiveOaContractLawExamineMapper;
    @Resource
    FiveOaSignQuoteMapper fiveOaSignQuoteMapper;
    @Resource
    FiveEdQualityIssueMapper fiveEdQualityIssueMapper;
    @Resource
    FiveFinanceTravelExpenseMapper fiveFinanceTravelExpenseMapper;
    @Autowired
    FiveOaMeetingArrangeMapper fiveOaMeetingArrangeMapper;
    @Autowired
    FiveFinanceIncomeMapper fiveFinanceIncomeMapper;
    @Autowired
    FiveFinanceNodeMapper fiveFinanceNodeMapper;
    @Resource
    FiveFinanceReimburseMapper fiveFinanceReimburseMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Autowired
    FiveFinanceRefundMapper fiveFinanceRefundMapper;
    @Autowired
    FiveFinanceLoanMapper fiveFinanceLoanMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Autowired
    FiveFinanceInvoiceMapper fiveFinanceInvoiceMapper;
    @Autowired
    FiveFinanceInvoiceCancelMapper fiveFinanceInvoiceCancelMapper;
    @Autowired
    FiveFinanceReceiptMapper fiveFinanceReceiptMapper;
    @Resource
    FiveOaFileSynergyMapper fiveOaFileSynergyMapper;
    @Resource
    FiveAssetAllotMapper fiveAssetAllotMapper;
    @Resource
    FiveComputerScrapMapper fiveComputerScrapMapper;
    @Resource
    FiveAssetScrapMapper fiveAssetScrapMapper;
    @Resource
    FiveFinanceInvoiceCollectionMapper fiveFinanceInvoiceCollectionMapper;
    @Resource
    FiveOaCarMaintainMapper fiveOaCarMaintainMapper;
    @Resource
    FiveFinanceProjectBudgetMapper fiveFinanceProjectBudgetMapper;
    @Resource
    FiveFinanceDeptBudgetMapper fiveFinanceDeptBudgetMapper;
    @Resource
    FiveFinanceBalanceMapper fiveFinanceBalanceMapper;
    @Resource
    BusinessSupplierMapper businessSupplierMapper;
    @Resource
    FiveOaCarApplyMapper fiveOaCarApplyMapper;
    @Resource
    FiveOaCardChangeMapper fiveOaCardChangeMapper;
    @Resource
    FiveOaEquipmentAcceptanceMapper fiveOaEquipmentAcceptanceMapper;
    @Resource
    FiveFinanceStampTaxMapper fiveFinanceStampTaxMapper;
    @Resource
    FiveFinanceOutSupplyMapper fiveFinanceOutSupplyMapper;
    @Resource
    FiveOaProcessDevelopApplyMapper fiveOaProcessDevelopApplyMapper;
    @Resource
    FiveOaComputerApplicationMapper fiveOaComputerApplicationMapper;
    @Resource
    FiveAssetComputerMapper fiveAssetComputerMapper;
    @Resource
    FiveFinanceBackLetterMapper fiveFinanceBackLetterMapper;
    @Resource
    FiveBudgetIndependentMapper fiveBudgetIndependentMapper;
    @Resource
    FiveBudgetMaintainMapper fiveBudgetMaintainMapper;
    @Resource
    FiveBudgetTurnInMapper fiveBudgetTurnInMapper;
    @Resource
    FiveBudgetFeeMapper fiveBudgetFeeMapper;
    @Resource
    FiveBudgetFeeChangeMapper fiveBudgetFeeChangeMapper;
    @Resource
    FiveBudgetScientificFundsOutMapper fiveBudgetScientificFundsOutMapper;
    @Resource
    FiveBudgetScientificFundsInMapper fiveBudgetScientificFundsInMapper;
    @Resource
    FiveBudgetPublicFundsMapper fiveBudgetPublicFundsMapper;
    @Resource
    FiveBudgetCapitalOutMapper fiveBudgetCapitalOutMapper;
    @Resource
    FiveBudgetLaborCostMapper fiveBudgetLaborCostMapper;
    @Resource
    FiveFinanceTransferAccountsMapper fiveFinanceTransferAccountsMapper;
    @Resource
    FiveFinanceSelfBankMapper fiveFinanceSelfBankMapper;
    @Resource
    FiveFinanceCompanyBankMapper fiveFinanceCompanyBankMapper;
    @Resource
    FiveBusinessOutAssistMapper fiveBusinessOutAssistMapper;
    @Resource
    FiveOaFurniturePurchaseMapper fiveOaFurniturePurchaseMapper;
    @Resource
    FiveBusinessCooperationContractMapper fiveBusinessCooperationContractMapper;
    @Resource
    FiveBusinessCooperationDelegationMapper fiveBusinessCooperationDelegationMapper;
    @Resource
    FiveOaInlandProjectApplyMapper fiveOaInlandProjectApplyMapper;
    @Resource
    FiveOaEquipmentAcceptanceService fiveOaEquipmentAcceptanceService;
    @Resource
    FiveAssetScrapService fiveAssetScrapService;
    @Resource
    FiveBusinessAdvanceMapper fiveBusinessAdvanceMapper;
    @Resource
    FiveBusinessAdvanceCollectMapper fiveBusinessAdvanceCollectMapper;
    @Resource
    FiveComputerScrapService fiveComputerScrapService;
    @Resource
    FiveOaStampApplyOfficeMapper fiveOaStampApplyOfficeMapper;
    @Resource
    FiveOaMeetingRoomMapper fiveOaMeetingRoomMapper;
    @Resource
    FiveBusinessAdvanceSevice fiveBusinessAdvanceSevice;



    private Map getMapData(String businessKey) {
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        Object item = null;
        if (businessKey.startsWith("fiveEdTask")) {//设计任务书
            List<FiveEdTask> list = fiveEdTaskMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveOaGoAbroadTrainAsk")) { //出国培训申请
            List<FiveOaGoAbroadTrainAsk> list = fiveOaGoAbroadTrainAskMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveEdReviewMajor")) { //专业方案讨论
            List<FiveEdReviewMajor> list = fiveEdReviewMajorMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveOaMeetingRoomApply")){ //会议室申请
            List<FiveOaMeetingRoomApply> list = fiveOaMeetingRoomApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaDecisionMaking")){ //决策会议
            List<FiveOaDecisionMaking> list = fiveOaDecisionMakingMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveBusinessCustomer")) {//客户信息录入
            List<BusinessCustomer> list = fiveBusinessCustomerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessSubpackage")||businessKey.startsWith("fiveBusinessPurchase")) {//分包采购评审
            List<BusinessSubpackage> list = businessSubpackageMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdQualityCheck")) {//质量抽查审校记录单
            List<FiveEdQualityCheck> list = fiveEdQualityCheckMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdQualityAnalysis")) {//设计质量问题剖析
            List<FiveEdQualityAnalysis> list = fiveEdQualityAnalysisMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdReturnVisit")) {//工程设计回访记录
            List<FiveEdReturnVisit> list = fiveEdReturnVisitMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdStamp")) {//设计成果用印申请
            List<FiveEdStamp> list = fiveEdStampMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdArrange")) {//人员与计划安排
            List<FiveEdArrange> list = fiveEdArrangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdDesignRule")) {//设计指导性文件
            List<FiveEdDesignRule> list = fiveEdDesignRuleMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdDesignDrawingCheck")) {//设计图纸资料交验
            List<FiveEdDesignDrawingCheck> list = fiveEdDesignDrawingCheckMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdMajorDrawingCheck")) {//专业图纸验收清单
            List<FiveEdMajorDrawingCheck> list = fiveEdMajorDrawingCheckMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveEdReviewPlan")) {//总体方案评审
            List<FiveEdReviewPlan> list = fiveEdReviewPlanMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdReviewSpecial")) {//专项评审
            List<FiveEdReviewSpecial> list = fiveEdReviewSpecialMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdHouseRefer")) {//设计协作提资单
            List<FiveEdHouseRefer> list = fiveEdHouseReferMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdValidate")) {//设计文件校审单 fiveEdOutReview
            List<FiveEdHouseValidate> list = fiveEdHouseValidateMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdOutReview")) {//外部设计评审
            List<FiveEdOutReview> list = fiveEdOutReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdServiceChange")) {//设计变更通知单
            List<FiveEdServiceChange> list = fiveEdServiceChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdServiceDiscuss")) {//建工、施工单位变更设计洽商单
            List<FiveEdServiceDiscuss> list = fiveEdServiceDiscussMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdServiceHandle")) {//技术服务问题处理单
            List<FiveEdServiceHandle> list = fiveEdServiceHandleMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdPlotApply")) {//出图申请单
            List<FiveEdPlotApply> list = fiveEdPlotApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaSuperviseDetail")) {//督办子项
            List<FiveOaSuperviseDetail> list = fiveOaSuperviseDetailMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaSupervise_")) {//常规管理
            List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveOaSuperviseYear_")) {//年度重点任务督办
            List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaSuperviseFile_")) {//文件督办
            List<FiveOaSuperviseFile> list = fiveOaSuperviseFileMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrQualifyProChief")) {//兼职总设计师资格认定
            List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrQualifyChief")) {//总设计师资格认定
            List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrApproveApply")) {//审定人资格认定申请
            List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrDesignQualifyApply")) {//设计校审人员资格认定
            List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaNewsExamine")) {//新闻宣传及信息报送审查
            List<FiveOaNewsexamine> list = fiveOaNewsexamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaProjectFundPlan_")) {//新闻宣传及信息报送审查
            List<FiveOaProjectfundplan> list = fiveOaProjectfundplanMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInventPayment")) {//专利缴费申请
            List<FiveOaInventPayment> list = fiveOaInventPaymentMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaScientificTaskCostApply")) {//科研课题费用申请
            List<FiveOaScientificTaskCostApply> list = fiveOaScientificTaskCostApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInventApply")) {//专利申请
            List<HrInvent> list = hrInventMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaNoIndependentDeptSet")) {//非独立运行中心设立申请表
            List<FiveOaNonIndependentDeptSet> list = fiveOaNonIndependentDeptSetMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaMaterialPurchase")) {// 资料采购申请表
            List<FiveOaMaterialPurchase> list = fiveOaMaterialPurchaseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaMaterialBorrow")) {//档案资料借阅/电子复制申请表
            List<FiveOaMaterialBorrow> list = fiveOaMaterialBorrowMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaBidApply")) {//投标申请
            List<FiveOaBidApply> list = fiveOaBidApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaContractSign")) {//业务合同签发单
            List<FiveOaContractSign> list = fiveOaContractSignMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPlatformRecord")) {//各地公共资源平台备案
            List<FiveOaPlatformRecord> list = fiveOaPlatformRecordMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeRetireExamine")) {//职工退休审批单
            List<FiveOaEmployeeRetireExamine> list = fiveOaEmployeeRetireExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaFurniturePurchase_")) {//办公家具采购
            List<FiveOaFurniturePurchase> list = fiveOaFurniturePurchaseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessRecord")) {// 项目信息备案
            List<BusinessRecord> list = businessRecordMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeDimissionExamine")) {//   职工离职审批单
            List<FiveOaEmployeeDimissionExamine> list = fiveOaEmployeeDimissionExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeTransferExamine")) {//   职工内部调整审批单
            List<FiveOaEmployeeTransferExamine> list = fiveOaEmployeeTransferExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeEntryExamine")) {//   职工入职审批单
            List<FiveOaEmployeeEntryExamine> list = fiveOaEmployeeEntryExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaGoAbroad_")) {//    因公出国内部审批文单
            List<FiveOaGoAbroad> list = fiveOaGoAbroadMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerChange")) {//    策略变更申请
            List<FiveOaComputerChange> list = fiveOaComputerChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentProcurement")) {//年度信息化设备采购预算表
            List<FiveOaInformationEquipmentProcurement> list = fiveOaInformationEquipmentProcurementMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSecretSystem")) {//涉密信息系统账户及权限开通、变更申请
            List<FiveOaSecretSystem> list = fiveOaSecretSystemMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerApplication")) {//流程开发申请
            List<FiveOaComputerApplication> list = fiveOaComputerApplicationMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaProcessDevelopApply")) {//公用计算机及U盘申请表
            List<FiveOaProcessDevelopApply> list = fiveOaProcessDevelopApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerNetwork")) {//     非密计算机入网审批
            List<FiveOaComputerNetwork> list = fiveOaComputerNetworkMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentApply")) {//     信息化设备购置申请单
            List<FiveOaInformationEquipmentApply> list = fiveOaInformationEquipmentApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSoftware")) {//     软件购置、审计、服务申请
            List<FiveOaSoftware> list = fiveOaSoftwareMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssetComputer")) {//公司非密计算机及信息化设备台帐
            List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationPlan")) {// 年度软件采购预算
            List<FiveOaInformationPlan> list = fiveOaInformationPlanMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaNonSecretEquipmentScrap")) {//     非密计算机及外设报废申请
            List<FiveOaNonSecretEquipmentScrap> list = fiveOaNonSecretEquipmentScrapMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerMaintain")) {//     计算机及外设维修服务单
            List<FiveOaComputerMaintain> list = fiveOaComputerMaintainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerPersonRepair")) {//     信息化设备外部人员现场维修情况记录表
            List<FiveOaComputerPersonRepair> list = fiveOaComputerPersonRepairMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPressurePipSealExamine")) {//     压力管道设计资格印章使用审批表
            List<FiveOaPressurePipSealExamine> list = fiveOaPressurePipSealExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaTechnologyArticleExamine")) {//     对外发表科技论文审查单
            List<FiveOaTechnologyArticleExamine> list = fiveOaTechnologyArticleExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaOutTechnicalExchange")) {//     参加外部技术交流会议审批
            List<FiveOaOutTechnicalExchange> list = fiveOaOutTechnicalExchangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaProfessionalSkillTrain")) {//     专业技术培训申请表
            List<FiveOaProfessionalSkillTrain> list = fiveOaProfessionalSkillTrainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaDeptJournal")) {//     院刊审查
            List<FiveOaDeptJournal> list = fiveOaDeptJournalMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaGoAbroadTrainDeclare")) {//     技术培训申报
            List<FiveOaGoAbroadTrainDeclare> list = fiveOaGoAbroadTrainDeclareMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaOutSpecialist")) {//     外部专家申请表
            List<FiveOaOutSpecialist> list = fiveOaOutSpecialistMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssociationPayment")) {//     协会缴费
            List<FiveOaAssociationPayment> list = fiveOaAssociationPaymentMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInlandProjectApply_")) {// 内部项目申请
            List<FiveOaInlandProjectApply> list = fiveOaInlandProjectApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssociationChange")) {//     协会信息变更
            List<FiveOaAssociationChange> list = fiveOaAssociationChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssociationApply")) {//     入协会申请
            List<FiveOaAssociationApply> list = fiveOaAssociationApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaDepartmentPost")) {//     部门发文单
            List<FiveOaDepartmentPost> list = fiveOaDepartmentPostMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaFileSynergy_")) {//     协同文件
            List<FiveOaFileSynergy> list = fiveOaFileSynergyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentExamine_")) {//     信息化设备验收审批表
            List<FiveOaInformationEquipmentExamine> list = fiveOaInformationEquipmentExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentExamineList")) {//     信息化设备验收(多台)审批表
            List<FiveOaInformationEquipmentExamineList> list = fiveOaInformationEquipmentExamineListMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPrivilegeManagement")) {//     权限调整表
            List<FiveOaPrivilegeManagement> list = fiveOaPrivilegeManagementMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessContractReview")) {//     合同评审
            List<FiveBusinessContractReview> list = fiveBusinessContractReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessPreContract")) {//     超前任务单
            List<BusinessPreContract> list = businessPreContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessContract_")) {//     项目启动
            List<BusinessContract> list = businessContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("businessContract")) {//     项目启动
            List<BusinessContract> list = businessContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
       /* else if (businessKey.startsWith("oaStampApply")) {//     用印审批公司章 合同章 施工章 压力章
            List<OaStampApply> list = oaStampApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }*/
        else if (businessKey.startsWith("oaNoticeApply")) {//     信息发布
            List<OaNoticeApply> list = oaNoticeApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessIncome")) {//     合同收费
            List<BusinessIncome> list = businessIncomeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaDispatch")) {//公司发文
            List<FiveOaDispatch> list = fiveOaDispatchMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaFileInstruction")) {//文件批示单
            List<FiveOaFileInstruction> list = fiveOaFileInstructionMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaReport")) {//报告文单
            List<FiveOaReport> list = fiveOaReportMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaContractLawExamine")) {//合同法律审查工作单
            List<FiveOaContractLawExamine> list = fiveOaContractLawExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSignQuote")) {//签报
            List<FiveOaSignQuote> list = fiveOaSignQuoteMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveEdQualityIssue")) {//重大设计问题
            List<FiveEdQualityIssue> list = fiveEdQualityIssueMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceTravelExpenseCommon")|businessKey.startsWith("fiveFinanceTravelExpenseFunction")||businessKey.startsWith("fiveFinanceTravelExpenseAccountsRed")||businessKey.startsWith("fiveFinanceTravelExpenseBuild")) {//差旅费报销
            List<FiveFinanceTravelExpense> list = fiveFinanceTravelExpenseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceReimburseCommon")||businessKey.startsWith("fiveFinanceReimburseFunction")||businessKey.startsWith("fiveFinanceReimburseRed")||businessKey.startsWith("fiveFinanceReimburseBuild")) {//费用报销
            List<FiveFinanceReimburse> list = fiveFinanceReimburseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaMeetingArrange")) {//会议安排
            List<FiveOaMeetingArrange> list = fiveOaMeetingArrangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceIncome_")) {//收入管理
            List<FiveFinanceIncome> list = fiveFinanceIncomeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceIncomeConfirm")) {//收入管理
            List<FiveFinanceIncomeConfirm> list = fiveFinanceIncomeConfirmMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceInvoice_")) {//发票申请
            List<FiveFinanceInvoice> list = fiveFinanceInvoiceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceInvoiceCancel")) {//发票作废申请
            List<FiveFinanceInvoiceCancel> list = fiveFinanceInvoiceCancelMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceNode")) {//票据管理
            List<FiveFinanceNode> list = fiveFinanceNodeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceRefund")) {//还款
            List<FiveFinanceRefund> list = fiveFinanceRefundMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceLoanCommon")||businessKey.startsWith("fiveFinanceLoanFunction")||businessKey.startsWith("fiveFinanceLoanAccountsRed")||businessKey.startsWith("fiveFinanceLoanBuild")) {//还款
            List<FiveFinanceLoan> list = fiveFinanceLoanMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceReceipt")) {//收据管理
            List<FiveFinanceReceipt> list = fiveFinanceReceiptMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceInvoiceCollection")) {//发票应收款催款
            List<FiveFinanceInvoiceCollection> list = fiveFinanceInvoiceCollectionMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("oaCarMaintain")) {//车辆维护
            List<FiveOaCarMaintain> list = fiveOaCarMaintainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSelfCarApply")) {//个人用车
            List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaLeaderCarApply")) {//领导用车
            List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveAssetAllot_")) {//固定资产调拨单
            List<FiveAssetAllot> list = fiveAssetAllotMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaCardChange")) {//充值卡变动
            List<FiveOaCardChange> list = fiveOaCardChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveComputerScrap")) {//非密计算机报废审批
            List<FiveComputerScrap> list = fiveComputerScrapMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveAssetScrap")) {//固定资产调拨单
            List<FiveAssetScrap> list = fiveAssetScrapMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceProjectBudget")) {//项目预算
            List<FiveFinanceProjectBudget> list = fiveFinanceProjectBudgetMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceDeptBudget")) {//事业部预算
            List<FiveFinanceDeptBudget> list = fiveFinanceDeptBudgetMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceBalance")) {//资金余额上报
            List<FiveFinanceBalance> list = fiveFinanceBalanceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessSupplier")) {//供方信息管理
            List<BusinessSupplier> list = businessSupplierMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPrivilegeManagement")) {//供方信息管理
            List<FiveOaPrivilegeManagement> list = fiveOaPrivilegeManagementMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceStampTax")) {//印花税申报
            List<FiveFinanceStampTax> list = fiveFinanceStampTaxMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceOutSupply")) {//对外提供资料申报
            List<FiveFinanceOutSupply> list = fiveFinanceOutSupplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceBackLetter")) {//保函管理
            List<FiveFinanceBackLetter> list = fiveFinanceBackLetterMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetIndependent")
                ||businessKey.startsWith("fiveBudgetProduction")
                ||businessKey.startsWith("fiveBudgetFunction")
                ||businessKey.startsWith("fiveBudgetPostExpense")) {//独立法人单位预算
            List<FiveBudgetIndependent> list = fiveBudgetIndependentMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetMaintain")
                ||businessKey.startsWith("fiveBudgetBusiness")
                ||businessKey.startsWith("fiveBudgetStock")) {//专项维修预算 股权投资
            List<FiveBudgetMaintain> list = fiveBudgetMaintainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetScientificFundsOut")) {//科研经费支出
            List<FiveBudgetScientificFundsOut> list = fiveBudgetScientificFundsOutMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetScientificFundsIn")) {//科研经费收入
            List<FiveBudgetScientificFundsIn> list = fiveBudgetScientificFundsInMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetPublicFunds")) {//公用经费预算
            List<FiveBudgetPublicFunds> list = fiveBudgetPublicFundsMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetCapitalOut")
                ||businessKey.startsWith("fiveBudgetTurnInRent")) {//资本性支出预算 上缴房租预算
            List<FiveBudgetCapitalOut> list = fiveBudgetCapitalOutMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetLaborCost")
                ||businessKey.startsWith("fiveBudgetStaffNumber")) {//人工成本 职工人数预算
            List<FiveBudgetLaborCost> list = fiveBudgetLaborCostMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }

        else if (businessKey.startsWith("fiveBudgetTurnIn")) {//上缴预算
            List<FiveBudgetTurnIn> list = fiveBudgetTurnInMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetFee")) {//收费预算
            List<FiveBudgetFee> list = fiveBudgetFeeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetFeeChange")) {//收费预算变更
            List<FiveBudgetFeeChange> list = fiveBudgetFeeChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceTransferAccountsCommon")||businessKey.startsWith("fiveFinanceTransferAccountsRed")||businessKey.startsWith("fiveFinanceTransferAccountsBuild")||businessKey.startsWith("fiveFinanceTransferAccountsFinance")) {//费用申请
            List<FiveFinanceTransferAccounts> list = fiveFinanceTransferAccountsMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceSelfBank")) {//个人网银
            List<FiveFinanceSelfBank> list = fiveFinanceSelfBankMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceCompanyBank")) {//公司网银
            List<FiveFinanceCompanyBank> list = fiveFinanceCompanyBankMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessChangeCustomer")) {//客户信息变更
            List<BusinessChangeCustomer> list = businessChangeCustomerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessChangeSupplier")) {//供方信息变更
            List<BusinessChangeSupplier> list = businessChangeSupplierMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessOutAssist")) {//外协支出
            List<FiveBusinessOutAssist> list = fiveBusinessOutAssistMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessCooperationDelegation")) {//经营合作项目确认
            List<FiveBusinessCooperationDelegation> list = fiveBusinessCooperationDelegationMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessCooperationContract")) {//内部协作协议合同
            List<FiveBusinessCooperationContract> list = fiveBusinessCooperationContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaExternalResearchProjectApply")) {//外部科研项目申请
            List<FiveOaExternalResearchProjectApply> list = fiveOaExternalResearchProjectApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaExternalStandardApply")) {//外部标准规范、图集项目申请
            List<FiveOaExternalStandardApply> list = fiveOaExternalStandardApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEquipmentAcceptance")) {//物资验收
            List<FiveOaEquipmentAcceptance> list = fiveOaEquipmentAcceptanceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessAdvance_")) {//月度支出明细
            List<FiveBusinessAdvance> list = fiveBusinessAdvanceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessAdvanceCollect")) {//月度支出签发
            List<FiveBusinessAdvanceCollect> list = fiveBusinessAdvanceCollectMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("oaStampApplyOffice")) {//公司章用印申请
        }
        else if (businessKey.startsWith("oaStampApplyOffice_")) {//公司章用印申请
            List<FiveOaStampApplyOffice> list = fiveOaStampApplyOfficeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessTenderDocumentReview")) {//工程承包项目招标文件评审（含联合体评审）
            List<FiveBusinessTenderDocumentReview> list = fiveBusinessTenderDocumentReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaResearchProjectReview")) {//科研项目费用
            List<FiveOaResearchProjectReview> list = fiveOaResearchProjectReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaMeetRoom_")) {//会议室详情
            List<FiveOaMeetingRoom> list = fiveOaMeetingRoomMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveAssetComputer_")) {//非密计算机台账
            List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaInformationEquipmentExamineList")) {//信息化设备验收(多台)审批
            List<FiveOaInformationEquipmentExamineList> list = fiveOaInformationEquipmentExamineListMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveBusinessContractLibrary")) {//合同库
            List<FiveBusinessContractLibrary> list = fiveBusinessContractLibraryMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessContractLibrarySubpackage_")) {//信息化设备验收(多台)审批
            List<FiveBusinessContractLibrarySubpackage> list = fiveBusinessContractLibrarySubpackageMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);        }

        if (item != null) {
            return JsonMapper.string2Map(JsonMapper.obj2String(item));
        }

        return null;
    }


   /**
    * 打印子表
    * */
   @Override
    public List<Map> listFormChildList(String businessKey) {

        List<Map> result = Lists.newArrayList();
        //多子表请 result.add(item2);
        if (businessKey.contains("fiveOaEquipmentAcceptance_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","验收物资");
            item1.put("list",list1);
            List<FiveOaEquipmentAcceptanceDetail> detailList1 = fiveOaEquipmentAcceptanceService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaEquipmentAcceptanceDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getEquipmentName(),"名称",6));
                item.add(new CommonCode(detail.getInvoiceNo(),"发票编号",6));
                item.add(new CommonCode(detail.getSpecification(),"设备规格",6));
                item.add(new CommonCode(detail.getDeptName(),"所属部门",6));
                item.add(new CommonCode(detail.getPrice(),"单价(元)",6));
                item.add(new CommonCode(detail.getNumber().toString(),"数量",6));
                item.add(new CommonCode(detail.getTotalPrice(),"总价",6));
                item.add(new CommonCode("","",6));//前面seq 为6的不是%2 主动新增一行空数据占位

                item.add(new CommonCode(detail.getRemark(),"备注",6));//前面seq 为6的不是%2 主动新增一行空数据占位
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveAssetScrap_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","设备信息");
            item1.put("list",list1);
            List<FiveAssetScrapDetail> detailList1 = fiveAssetScrapService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveAssetScrapDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getDeviceType(),"设备类型",6));
                item.add(new CommonCode(detail.getDeviceNo(),"设备编号",6));
                item.add(new CommonCode(detail.getDeviceModel(),"品牌型号",6));
                item.add(new CommonCode(detail.getDutyPersonName(),"责任人",6));
                item.add(new CommonCode(detail.getStartTime().toString(),"启用时间",6));
                item.add(new CommonCode(detail.getOriginalValue().toString(),"原值",6));
                item.add(new CommonCode(detail.getDepreciableLife().toString(),"已提折旧",6));
                item.add(new CommonCode(detail.getNetWorth().toString(),"净值",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveComputerScrap_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","设备信息");
            item1.put("list",list1);
            List<FiveComputerScrapDetail> detailList1 = fiveComputerScrapService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveComputerScrapDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getDeviceType(),"设备类型",6));
                item.add(new CommonCode(detail.getDeviceNo(),"设备编号",6));
                item.add(new CommonCode(detail.getDeviceModel(),"品牌型号",6));
               // item.add(new CommonCode(detail.getDutyPersonName(),"责任人",6));
                item.add(new CommonCode(detail.getStartTime().toString(),"启用时间",6));
                item.add(new CommonCode(detail.getOriginalValue().toString(),"原值",6));
                item.add(new CommonCode(detail.getDepreciableLife().toString(),"已提折旧",6));
                item.add(new CommonCode(detail.getNetWorth().toString(),"净值",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaFurniturePurchase_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","采购明细");
            item1.put("list",list1);
            List<FiveOaFurniturePurchaseDetail> detailList1 = fiveOaFurniturePurchaseService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaFurniturePurchaseDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getFurnitureName(),"名称",6));
                item.add(new CommonCode(detail.getNumber(),"数量",6));
                item.add(new CommonCode(detail.getPrice(),"单价（元）",6));
                item.add(new CommonCode(detail.getTotalPrice(),"总价（元）",6));
                item.add(new CommonCode(detail.getRemark(),"备注",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaSuperviseYear_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","年度重点任务督办");
            item1.put("list",list1);
            List<FiveOaSuperviseDetail> detailList1 = fiveOaSuperviseService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaSuperviseDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getSuperviseType(),"督办类型",6));
                item.add(new CommonCode(detail.getTaskSource(),"任务来源",6));
                item.add(new CommonCode(detail.getTaskDefinition(),"任务内容",6));
                item.add(new CommonCode(detail.getTransactorName(),"办理人",6));
                item.add(new CommonCode(detail.getTimeLimit(),"办结时限",6));
                item.add(new CommonCode(detail.getDeptName(),"办理部门",6));
                item.add(new CommonCode(detail.getFeedbackTime(),"反馈周期",6));
                item.add(new CommonCode(detail.getTransactionPlan(),"办理进度",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaSupervise_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","常规督办");
            item1.put("list",list1);
            List<FiveOaSuperviseDetail> detailList1 = fiveOaSuperviseService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaSuperviseDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getSuperviseType(),"督办类型",6));
                item.add(new CommonCode(detail.getTaskSource(),"任务来源",6));
                item.add(new CommonCode(detail.getTaskDefinition(),"任务内容",6));
                item.add(new CommonCode(detail.getTransactorName(),"办理人",6));
                item.add(new CommonCode(detail.getTimeLimit(),"办结时限",6));
                item.add(new CommonCode(detail.getDeptName(),"办理部门",6));
                item.add(new CommonCode(detail.getFeedbackTime(),"反馈周期",6));
                item.add(new CommonCode(detail.getTransactionPlan(),"办理进度",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaCardChange_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","职工卡修改列表");
            item1.put("list",list1);
            List<FiveOaCardChangeDetail> detailList1 = fiveOaCardChangeService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaCardChangeDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getUserName(),"姓名",6));
                item.add(new CommonCode(detail.getUserLogin(),"职工号",6));
                item.add(new CommonCode(detail.getUserType(),"员工类别",6));
                item.add(new CommonCode(detail.getCardTypeNow(),"充值卡当前类型",6));
                item.add(new CommonCode(detail.getCardTypeChange(),"充值卡调整类型",6));
                item.add(new CommonCode(detail.getRemark(),"备注",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaResearchProjectReview_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","专家意见");
            item1.put("list",list1);
            List<FiveOaResearchProjectReviewDetail> detailList1 = fiveOaResearchProjectReviewSevice.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaResearchProjectReviewDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getPersonName(),"专家姓名",6));
                item.add(new CommonCode(detail.getOpinion(),"专家意见",6));
                item.add(new CommonCode(detail.getApproved(),"是否通过评审",6));
                item.add(new CommonCode(detail.getRemark(),"建议补充",6));
                item.add(new CommonCode(detail.getCreatorName(),"创建人",6));
                item.add(new CommonCode(detail.getGmtCreate().toString(),"创建时间",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaInformationEquipmentExamineList_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","购置设备信息");
            item1.put("list",list1);
            List<FiveOaInformationEquipmentExamineListDetail> detailList1 = fiveOaInformationEquipmentExamineListService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaInformationEquipmentExamineListDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getEquipmentName(),"设备名称",6));
                item.add(new CommonCode(detail.getEquipmentNo(),"设备序列号",6));
                item.add(new CommonCode(detail.getFormNo(),"设备编号",6));
                item.add(new CommonCode(detail.getChargeMan(),"责任人",6));
                item.add(new CommonCode(detail.getUserName(),"使用人",6));
                item.add(new CommonCode(detail.getOsInstallTime(),"设备类型",6));
                item.add(new CommonCode(detail.getFixedAssetNo(),"资产编号",6));
                item.add(new CommonCode(detail.getDeptName(),"所属单位",6));
                item.add(new CommonCode(detail.getUseType(),"用途",6));
                item.add(new CommonCode(detail.getStartTime(),"启用时间",6));
                item.add(new CommonCode(detail.getCheckPrice(),"验收价格",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveBusinessContractReview_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","评审记录");
            item1.put("list",list1);
            List<FiveBusinessContractReviewDetail> detailList1 = fiveBusinessContractReviewService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveBusinessContractReviewDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getReviewContent(),"评审内容",10));
                item.add(new CommonCode(detail.getReviewResult(),"评审结果",2));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveBusinessAdvance_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","预支详情");
            item1.put("list",list1);
            List<FiveBusinessAdvanceDetail> detailList1 = fiveBusinessAdvanceSevice.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveBusinessAdvanceDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getPersonNo(),"职工号",6));
                item.add(new CommonCode(detail.getPersonName(),"姓名",6));
                item.add(new CommonCode(detail.getDeptName(),"具体部门",6));
                item.add(new CommonCode(detail.getPersonnelCategory(),"人员类别",6));
                item.add(new CommonCode(detail.getProjectBonus(),"金额（元）",6));
                item.add(new CommonCode(detail.getRemark(),"备注",6));
                list1.add(item);
            }
            result.add(item1);
        }
        return result;
    }
}
