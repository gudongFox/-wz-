package com.cmcu.mcc.service.impl;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessHandleService;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dao.CommonFormMapper;
import com.cmcu.common.dto.*;
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
import com.cmcu.mcc.business.service.FiveBusinessAdvanceCollectService;
import com.cmcu.mcc.business.service.FiveBusinessAdvanceSevice;
import com.cmcu.mcc.business.service.FiveBusinessContractReviewService;
import com.cmcu.mcc.ed.dao.*;
import com.cmcu.mcc.ed.entity.*;
import com.cmcu.mcc.ed.service.FiveEdDesignDrawingCheckService;
import com.cmcu.mcc.ed.service.FiveEdMajorDrawingCheckService;
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
import com.cmcu.mcc.oa.entity.OaNoticeApply;
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
    @Resource
    FiveFinanceSubpackagePaymentMapper fiveFinanceSubpackagePaymentMapper;



    /**
     * 1.???????????? 2.?????????????????? 3.??????????????????
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
     * 2.?????????????????? 3.??????????????????
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
            if (businessKey.startsWith("fiveOaGoAbroadTrainAsk_")) { //??????????????????
                List<FiveOaGoAbroadTrainAsk> list = fiveOaGoAbroadTrainAskMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaGoAbroadTrainAskMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaMeetingRoomApply_")) { //???????????????
                List<FiveOaMeetingRoomApply> list = fiveOaMeetingRoomApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMeetingRoomApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaDecisionMaking_")) { //????????????
                List<FiveOaDecisionMaking> list = fiveOaDecisionMakingMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDecisionMakingMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessCustomer_")) {//??????????????????
                List<BusinessCustomer> list = fiveBusinessCustomerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessCustomerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessSubpackage_")||businessKey.startsWith("fiveBusinessPurchase")) {//??????????????????
                List<BusinessSubpackage> list = businessSubpackageMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessSubpackageMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdDesignDrawingCheck_")) {//????????????????????????
                List<FiveEdDesignDrawingCheck> list = fiveEdDesignDrawingCheckMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdDesignDrawingCheckMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveEdMajorDrawingCheck_")) {//????????????????????????
                List<FiveEdMajorDrawingCheck> list = fiveEdMajorDrawingCheckMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveEdMajorDrawingCheckMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSuperviseDetail_")) {//????????????
                List<FiveOaSuperviseDetail> list = fiveOaSuperviseDetailMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseDetailMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSupervise_")) {//????????????
                List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSuperviseYear_")) {//????????????????????????
                List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSuperviseFile_")) {//????????????
                List<FiveOaSuperviseFile> list = fiveOaSuperviseFileMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSuperviseFileMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrQualifyProChief_")) {//??????????????????????????????
                List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyChiefMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrQualifyChief_")) {//????????????????????????
                List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyChiefMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrApproveApply_")) {//???????????????????????????
                List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveHrDesignQualifyApply_")) {//??????????????????????????????
                List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveHrQualifyApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaNewsExamine_")) {//?????????????????????????????????
                List<FiveOaNewsexamine> list = fiveOaNewsexamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaNewsexamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaProjectFundPlan_")) {//????????????????????????
                List<FiveOaProjectfundplan> list = fiveOaProjectfundplanMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaProjectfundplanMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInventPayment_")) {//??????????????????
                List<FiveOaInventPayment> list = fiveOaInventPaymentMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInventPaymentMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaScientificTaskCostApply_")) {//????????????????????????
                List<FiveOaScientificTaskCostApply> list = fiveOaScientificTaskCostApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaScientificTaskCostApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInventApply_")) {//????????????
                List<HrInvent> list = hrInventMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    hrInventMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaNoIndependentDeptSet_")) {//????????????????????????????????????
                List<FiveOaNonIndependentDeptSet> list = fiveOaNonIndependentDeptSetMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaNonIndependentDeptSetMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaMaterialPurchase_")) {// ?????????????????????
                List<FiveOaMaterialPurchase> list = fiveOaMaterialPurchaseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMaterialPurchaseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaMaterialBorrow_")) {//  ??????????????????/?????????????????????
                List<FiveOaMaterialBorrow> list = fiveOaMaterialBorrowMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMaterialBorrowMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaBidApply_")) {//   ????????????
                List<FiveOaBidApply> list = fiveOaBidApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaBidApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaContractSign_")) {//   ?????????????????????
                List<FiveOaContractSign> list = fiveOaContractSignMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaContractSignMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaPlatformRecord_")) {//   ??????????????????????????????
                List<FiveOaPlatformRecord> list = fiveOaPlatformRecordMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaPlatformRecordMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeRetireExamine_")) {//   ?????????????????????
                List<FiveOaEmployeeRetireExamine> list = fiveOaEmployeeRetireExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeRetireExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaFurniturePurchase_")) {//   ??????????????????
                List<FiveOaFurniturePurchase> list = fiveOaFurniturePurchaseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaFurniturePurchaseMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessRecord_")) {//   ??????????????????
                List<BusinessRecord> list = businessRecordMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessRecordMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeDimissionExamine_")) {//   ?????????????????????
                List<FiveOaEmployeeDimissionExamine> list = fiveOaEmployeeDimissionExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeDimissionExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeTransferExamine_")) {//   ???????????????????????????
                List<FiveOaEmployeeTransferExamine> list = fiveOaEmployeeTransferExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeTransferExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEmployeeEntryExamine_")) {//   ?????????????????????
                List<FiveOaEmployeeEntryExamine> list = fiveOaEmployeeEntryExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEmployeeEntryExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaGoAbroad_")) {//    ??????????????????????????????
                List<FiveOaGoAbroad> list = fiveOaGoAbroadMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaGoAbroadMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerChange_")) {//    ??????????????????
                List<FiveOaComputerChange> list = fiveOaComputerChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerChangeMapper.updateByPrimaryKey(p);
                });
            }  else if (businessKey.startsWith("fiveOaInformationPlan")) {// ????????????????????????
                List<FiveOaInformationPlan> list = fiveOaInformationPlanMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationPlanMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaProcessDevelopApply_")) {//??????????????????
                List<FiveOaProcessDevelopApply> list = fiveOaProcessDevelopApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaProcessDevelopApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerApplication_")) {//??????????????????U?????????
                List<FiveOaComputerApplication> list = fiveOaComputerApplicationMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerApplicationMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaSecretSystem_")) {//??????????????????????????????????????????????????????
                List<FiveOaSecretSystem> list = fiveOaSecretSystemMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSecretSystemMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentProcurement_")) {//    ????????????????????????????????????
                List<FiveOaInformationEquipmentProcurement> list = fiveOaInformationEquipmentProcurementMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentProcurementMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerNetwork_")) {//     ???????????????????????????
                List<FiveOaComputerNetwork> list = fiveOaComputerNetworkMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerNetworkMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentApply_")) {//     ??????????????????????????????
                List<FiveOaInformationEquipmentApply> list = fiveOaInformationEquipmentApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentApplyMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaSoftware_")) {//     ????????????????????????????????????
                List<FiveOaSoftware> list = fiveOaSoftwareMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSoftwareMapper.updateByPrimaryKey(p);
                });
            }else if (businessKey.startsWith("fiveOaPrivilegeManagement_")) {//????????????
                List<FiveOaPrivilegeManagement> list = fiveOaPrivilegeManagementMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaPrivilegeManagementMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaNonSecretEquipmentScrap_")) {// ????????????????????????????????????
                List<FiveOaNonSecretEquipmentScrap> list = fiveOaNonSecretEquipmentScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaNonSecretEquipmentScrapMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaComputerMaintain_")) {//     ??????????????????????????????
                List<FiveOaComputerMaintain> list = fiveOaComputerMaintainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerMaintainMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaComputerPersonRepair_")) {//     ??????????????????????????????
                List<FiveOaComputerPersonRepair> list = fiveOaComputerPersonRepairMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaComputerPersonRepairMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaPressurePipSealExamine_")) {//     ?????????????????????????????????????????????
                List<FiveOaPressurePipSealExamine> list = fiveOaPressurePipSealExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaPressurePipSealExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaTechnologyArticleExamine_")) {//     ?????????????????????????????????
                List<FiveOaTechnologyArticleExamine> list = fiveOaTechnologyArticleExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaTechnologyArticleExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaOutTechnicalExchange_")) {//     ????????????????????????????????????
                List<FiveOaOutTechnicalExchange> list = fiveOaOutTechnicalExchangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaOutTechnicalExchangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaProfessionalSkillTrain_")) {//     ???????????????????????????
                List<FiveOaProfessionalSkillTrain> list = fiveOaProfessionalSkillTrainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaProfessionalSkillTrainMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaDeptJournal_")) {//     ????????????
                List<FiveOaDeptJournal> list = fiveOaDeptJournalMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDeptJournalMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaGoAbroadTrainDeclare_")) {//     ??????????????????
                List<FiveOaGoAbroadTrainDeclare> list = fiveOaGoAbroadTrainDeclareMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaGoAbroadTrainDeclareMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaOutSpecialist_")) {//     ?????????????????????
                List<FiveOaOutSpecialist> list = fiveOaOutSpecialistMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaOutSpecialistMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssociationPayment_")) {//     ????????????
                List<FiveOaAssociationPayment> list = fiveOaAssociationPaymentMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaAssociationPaymentMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssociationChange_")) {//     ??????????????????
                List<FiveOaAssociationChange> list = fiveOaAssociationChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaAssociationChangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInlandProjectApply_")) {//??????????????????
                List<FiveOaInlandProjectApply> list = fiveOaInlandProjectApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInlandProjectApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInlandProjectReview_")) {//??????????????????
                List<FiveOaInlandProjectReview> list = fiveOaInlandProjectReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInlandProjectReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssociationApply_")) {//     ???????????????
                List<FiveOaAssociationApply> list = fiveOaAssociationApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaAssociationApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaDepartmentPost_")) {//     ???????????????
                List<FiveOaDepartmentPost> list = fiveOaDepartmentPostMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDepartmentPostMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaFileSynergy_")) {//     ????????????
                List<FiveOaFileSynergy> list = fiveOaFileSynergyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaFileSynergyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentExamine_")) {//     ??????????????????????????????
                List<FiveOaInformationEquipmentExamine> list = fiveOaInformationEquipmentExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaInformationEquipmentExamineList_")) {//   ????????????????????????????????????
                List<FiveOaInformationEquipmentExamineList> list = fiveOaInformationEquipmentExamineListMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaInformationEquipmentExamineListMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetComputer_")) {//     ?????????????????????????????????????????????
                List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetComputerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessContractReview_")) {//     ????????????
                List<FiveBusinessContractReview> list = fiveBusinessContractReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessContractReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessPreContract_")) {//     ???????????????
                List<BusinessPreContract> list = businessPreContractMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessPreContractMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessContract_")) {//     ????????????
                BusinessContract businessContract= businessContractMapper.selectByPrimaryKey(Integer.valueOf(businessKey.split("_")[1]));
                businessContract.setDeleted(true);
                businessContractMapper.updateByPrimaryKey(businessContract);
            } else if (businessKey.startsWith("businessContract_")) {//     ????????????
                BusinessContract businessContract = businessContractMapper.selectByPrimaryKey(Integer.valueOf(businessKey.split("_")[1]));
                businessContract.setDeleted(true);
                businessContractMapper.updateByPrimaryKey(businessContract);
            }
          else if (businessKey.startsWith("oaNoticeApply_")) {//     ????????????
                List<OaNoticeApply> list = oaNoticeApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    oaNoticeApplyMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessIncome_")) {//     ????????????
                List<BusinessIncome> list = businessIncomeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessIncomeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaDispatch_")) {//????????????
                List<FiveOaDispatch> list = fiveOaDispatchMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaDispatchMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaFileInstruction_")) {//???????????????
                List<FiveOaFileInstruction> list = fiveOaFileInstructionMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaFileInstructionMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaReport_")) {//????????????
                List<FiveOaReport> list = fiveOaReportMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaReportMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaContractLawExamine_")) {//???????????????????????????
                List<FiveOaContractLawExamine> list = fiveOaContractLawExamineMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaContractLawExamineMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaSignQuote_")) {//??????
                List<FiveOaSignQuote> list = fiveOaSignQuoteMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaSignQuoteMapper.updateByPrimaryKey(p);
                });
            }else if (businessKey.startsWith("fiveFinanceTravelExpense")) {//???????????????
                List<FiveFinanceTravelExpense> list = fiveFinanceTravelExpenseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceTravelExpenseMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceReimburse")) {//????????????
                List<FiveFinanceReimburse> list = fiveFinanceReimburseMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceReimburseMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaMeetingArrange_")) {//????????????
                List<FiveOaMeetingArrange> list = fiveOaMeetingArrangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaMeetingArrangeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceIncome_")) {//????????????
                List<FiveFinanceIncome> list = fiveFinanceIncomeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceIncomeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceInvoice_")) {//????????????
                List<FiveFinanceInvoice> list = fiveFinanceInvoiceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceInvoiceMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceInvoiceCancel_")) {//??????????????????
                List<FiveFinanceInvoiceCancel> list = fiveFinanceInvoiceCancelMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceInvoiceCancelMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceInvoiceCollection_")) {//?????????????????????
                List<FiveFinanceInvoiceCollection> list = fiveFinanceInvoiceCollectionMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceInvoiceCollectionMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceIncomeConfirm_")) {//????????????
                List<FiveFinanceIncomeConfirm> list = fiveFinanceIncomeConfirmMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceIncomeConfirmMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceNode_")) {//????????????
                List<FiveFinanceNode> list = fiveFinanceNodeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceNodeMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceReceipt_")) {//????????????
                List<FiveFinanceReceipt> list = fiveFinanceReceiptMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceReceiptMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceTransferAccountsFinance_")||businessKey.startsWith("fiveFinanceTransferAccountsCommon")||businessKey.startsWith("fiveFinanceTransferAccountsRed")||businessKey.startsWith("fiveFinanceTransferAccountsBuild")||businessKey.startsWith("fiveFinanceTransferFee")) {//????????????
                List<FiveFinanceTransferAccounts> list = fiveFinanceTransferAccountsMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceTransferAccountsMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveFinanceLoanFunction")||businessKey.startsWith("fiveFinanceLoanCommon")
                    ||businessKey.startsWith("fiveFinanceLoanRed")||businessKey.startsWith("fiveFinanceLoanBuild")) {//??????
                List<FiveFinanceLoan> list = fiveFinanceLoanMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceLoanMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessContractLibrary_")) {//?????????
                List<FiveBusinessContractLibrary> list = fiveBusinessContractLibraryMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveBusinessContractLibrarySubpackage_")) {//???????????????
                List<FiveBusinessContractLibrarySubpackage> list = fiveBusinessContractLibrarySubpackageMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("oaCarMaintain")) {//????????????
                List<FiveOaCarMaintain> list = fiveOaCarMaintainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCarMaintainMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaSelfCarApply")) {//????????????
                List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCarApplyMapper.updateByPrimaryKey(p);
                });
            } else if (businessKey.startsWith("fiveOaLeaderCarApply")) {//????????????
                List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCarApplyMapper.updateByPrimaryKey(p);
                });
            }else if (businessKey.startsWith("fiveOaCardChange")) {//?????????????????????
                List<FiveOaCardChange> list = fiveOaCardChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaCardChangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaEquipmentAcceptance")) {//????????????
                List<FiveOaEquipmentAcceptance> list = fiveOaEquipmentAcceptanceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaEquipmentAcceptanceMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetAllot_")) {//?????????????????????
                List<FiveAssetAllot> list = fiveAssetAllotMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetAllotMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveComputerScrap_")) {//????????????????????????????????????
                List<FiveComputerScrap> list = fiveComputerScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveComputerScrapMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetScrap_")) {//????????????????????????????????????
                List<FiveAssetScrap> list = fiveAssetScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetScrapMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaAssetComputer_")) {//????????????????????????
                List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetComputerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveAssetScrap_")) {//????????????????????????????????????
                List<FiveAssetScrap> list = fiveAssetScrapMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveAssetScrapMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceBalance")) {//??????????????????
                List<FiveFinanceBalance> list = fiveFinanceBalanceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceBalanceMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceProjectBudget")) {//????????????
                List<FiveFinanceProjectBudget> list = fiveFinanceProjectBudgetMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceProjectBudgetMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceDeptBudget")) {//???????????????
                List<FiveFinanceDeptBudget> list = fiveFinanceDeptBudgetMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceDeptBudgetMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessSupplier")) {//??????????????????
                List<BusinessSupplier> list = businessSupplierMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessSupplierMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceStampTax")) {//???????????????
                List<FiveFinanceStampTax> list = fiveFinanceStampTaxMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceStampTaxMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceOutSupply")) {//??????????????????
                List<FiveFinanceOutSupply> list = fiveFinanceOutSupplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceOutSupplyMapper.updateByPrimaryKey(p);
                });
            }

            else if (businessKey.startsWith("fiveFinanceBackLetter")) {//?????????
                List<FiveFinanceBackLetter> list = fiveFinanceBackLetterMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceBackLetterMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceBackLetter")) {//??????????????????
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
                    ||businessKey.startsWith("fiveBudgetPostExpense")) {//????????????????????????
                List<FiveBudgetIndependent> list = fiveBudgetIndependentMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetIndependentMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetMaintain")
                    ||businessKey.startsWith("fiveBudgetBusiness")||businessKey.startsWith("fiveBudgetStock")) {//?????????????????? ???????????? ??????????????????
                List<FiveBudgetMaintain> list = fiveBudgetMaintainMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetMaintainMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetScientificFundsOut")) {//??????????????????
                List<FiveBudgetScientificFundsOut> list = fiveBudgetScientificFundsOutMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetScientificFundsOutMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetScientificFundsIn")) {//??????????????????
                List<FiveBudgetScientificFundsIn> list = fiveBudgetScientificFundsInMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetScientificFundsInMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetPublicFunds")) {//??????????????????
                List<FiveBudgetPublicFunds> list = fiveBudgetPublicFundsMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetPublicFundsMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetCapitalOut")
                    ||businessKey.startsWith("fiveBudgetTurnInRent")) {//?????????????????????
                List<FiveBudgetCapitalOut> list = fiveBudgetCapitalOutMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetCapitalOutMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetLaborCost")
                    ||businessKey.startsWith("fiveBudgetStaffNumber")) {//???????????? ????????????
                List<FiveBudgetLaborCost> list = fiveBudgetLaborCostMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetLaborCostMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetFeeChange")) {//??????????????????
                List<FiveBudgetFeeChange> list = fiveBudgetFeeChangeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetFeeChangeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetFee")) {//????????????
                List<FiveBudgetFee> list = fiveBudgetFeeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetFeeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBudgetTurnIn")) {//????????????
                List<FiveBudgetTurnIn> list = fiveBudgetTurnInMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBudgetTurnInMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceSelfBank")) {//????????????
                List<FiveFinanceSelfBank> list = fiveFinanceSelfBankMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceSelfBankMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceCompanyBank")) {//????????????
                List<FiveFinanceCompanyBank> list = fiveFinanceCompanyBankMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceCompanyBankMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessChangeCustomer")) {//??????????????????
                List<BusinessChangeCustomer> list = businessChangeCustomerMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessChangeCustomerMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessChangeSupplier")) {//??????????????????
                List<BusinessChangeSupplier> list = businessChangeSupplierMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    businessChangeSupplierMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessOutAssist")) {//????????????
                List<FiveBusinessOutAssist> list = fiveBusinessOutAssistMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessOutAssistMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessCooperationDelegation")) {//????????????????????????
                List<FiveBusinessCooperationDelegation> list = fiveBusinessCooperationDelegationMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessCooperationDelegationMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessCooperationContract")) {//????????????????????????
                List<FiveBusinessCooperationContract> list = fiveBusinessCooperationContractMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessCooperationContractMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceRefund")) {//??????
                List<FiveFinanceRefund> list = fiveFinanceRefundMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceRefundMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaExternalResearchProjectApply_")) {//????????????????????????
                List<FiveOaExternalResearchProjectApply> list = fiveOaExternalResearchProjectApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaExternalResearchProjectApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaExternalStandardApply_")) {//???????????????????????????????????????
                List<FiveOaExternalStandardApply> list = fiveOaExternalStandardApplyMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaExternalStandardApplyMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveOaResearchProjectReview_")) {//????????????????????????
                List<FiveOaResearchProjectReview> list = fiveOaResearchProjectReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaResearchProjectReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessAdvance_")) {//????????????
                List<FiveBusinessAdvance> list = fiveBusinessAdvanceMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessAdvanceMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessAdvanceCollect")) {//???????????????
                List<FiveBusinessAdvanceCollect> list = fiveBusinessAdvanceCollectMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessAdvanceCollectMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveBusinessTenderDocumentReview")) {//????????????????????????????????????????????????????????????
                List<FiveBusinessTenderDocumentReview> list = fiveBusinessTenderDocumentReviewMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveBusinessTenderDocumentReviewMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("oaStampApplyOffice")) {//????????????
                List<FiveOaStampApplyOffice> list = fiveOaStampApplyOfficeMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveOaStampApplyOfficeMapper.updateByPrimaryKey(p);
                });
            }
            else if (businessKey.startsWith("fiveFinanceSubpackagePayment")) {//??????????????????
                List<FiveFinanceSubpackagePayment> list = fiveFinanceSubpackagePaymentMapper.selectAll(params);
                list.forEach(p -> {
                    p.setDeleted(true);
                    p.setGmtModified(new Date());
                    fiveFinanceSubpackagePaymentMapper.updateByPrimaryKey(p);
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
        FastUserDto userDto = commonUserService.getFastByEnLogin(enLogin);
        Optional<DeptDto> deptDto = commonUserService.selectAllDept(userDto.getTenetId()).stream().filter(p -> p.getId()==userDto.getDeptId()).findFirst();
        if (deptDto.isPresent()) {
            List<String> deptChargeMen = deptDto.get().getDeptChargeMen();
            return deptChargeMen;
        }
        return Lists.newArrayList();
    }

    @Override
    public TplConfigDto getTplConfigDto(String processInstanceId,String businessKey, String enLogin) {

        //CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(processInstanceId,businessKey,enLogin);

        CustomSimpleProcessInstance customProcessInstance=processQueryService.getCustomSimpleProcessInstance(processInstanceId,businessKey,enLogin);

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
            commonForm.setFormCategory("????????????");
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
                if (item.getTaskList().stream().anyMatch(p->p.contains("?????????"))) {
                    item.setSaveAble(true);
                    item.setEditable(true);
                }
            }
            else if(businessKey.startsWith("fiveOaLeaderCarApply")||businessKey.startsWith("fiveOaSelfCarApply")){
                if(item.isEditable()){
                    if(customProcessInstance.getMyRunningTaskNameList().stream().anyMatch(p->p.startsWith("????????????"))){
                        item.setSaveAble(true);
                    }
                }
            }
        }
        else {
            item.setSaveAble(true);
            item.setEditable(true);
            if (businessKey == "ed_arrange") {
                if (item.getTaskList().contains("?????????")) {
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
        if(dataSourceKey.equalsIgnoreCase("????????????")){
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
                newData.put("pressureStamp",stamps.stream().anyMatch(p->p.contains("??????")));
                newData.put("plotStamp",stamps.stream().anyMatch(p->p.contains("??????")));
                newData.put("constructionStamp",stamps.stream().anyMatch(p->p.contains("??????")));
            }
        }else if(referType.equalsIgnoreCase("fiveEdArrange")){
            commonEdArrangeUserService.buildCoDirData(commonFormData.getReferId(), commonFormData.getReferId().toString());
        } else  if (referType.equalsIgnoreCase("oaStampApplyOffice")){
            //2021-03-01 HNZ ???????????????????????? ???????????????????????????????????? ???????????? ??????????????????
            List<String> stamps=JsonMapper.getListValue(newData,"stampName");
            List<String> secrecy= JsonMapper.getListValue(newData,"secrecy");
          if (secrecy.stream().anyMatch(p -> p.contains("true"))||stamps.stream().anyMatch(p -> p.contains("???????????????"))){
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
     * todo:CAD???????????????
     * @param nodeId
     * @param enLogin
     * @return
     */
    @Override
    public String getNewEdFormData(int nodeId, String enLogin) {
        EdProjectTree tree = edProjectTreeMapper.selectByPrimaryKey(nodeId);
        String businessKey = "";
        String processInstanceId = "";
        if (tree.getNodeUrl().contains("designDrawingCheck")) {//????????????????????????
            processInstanceId = fiveEdDesignDrawingCheckService.getNewModel(tree.getForeignKey(), enLogin).getProcessInstanceId();
        } else if (tree.getNodeUrl().contains("designDrawingCheck")) {
            processInstanceId = fiveEdMajorDrawingCheckService.getNewModel(tree.getForeignKey(), enLogin).getProcessInstanceId();
        } else { //??????????????????
            commonFormDataService.getNewModel(tree.getReferType(), tree.getForeignKey(), enLogin);
        }
        if (StringUtils.isNotEmpty(businessKey) || StringUtils.isNotEmpty(processInstanceId)) {
            TplConfigDto tplConfigDto = getTplConfigDto(processInstanceId, businessKey, enLogin);
            if (StringUtils.isEmpty(businessKey)) businessKey = tplConfigDto.getBusinessKey();
        }

        Assert.state(StringUtils.isNotEmpty(businessKey), "?????????????????????????????????,????????????,????????????????????????????????????!");
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
                showNameList.put("formNo","????????????");
                showNameList.put("projectName", "????????????");
                showNameList.put("stageName", "????????????");
                showNameList.put("stepName", "????????????");
                showNameList.put("projectNo", "?????????");
                showNameList.put("contractNo", "?????????");
                showNameList.put("deptName", "????????????");
                showNameList.put("deptChargeMenName","???????????????");
                showNameList.put("remark","??????");
                showNameList.put("majorName","??????");
                showNameList.put("beginTime","????????????");
                showNameList.put("endTime","????????????");


                Map hideNameList = Maps.newHashMap();
                hideNameList.put("businessKey","????????????");
                hideNameList.put("deptId", "????????????");
                hideNameList.put("stepId", "????????????");
                hideNameList.put("projectId", "????????????");
                hideNameList.put("deptChargeMen","???????????????");
                hideNameList.put("proofreadMen","?????????");
                int seq = 1;
                for (String key : data.keySet()) {
                    if (!excludeList.contains(key)) {
                        CommonFormDetail detail = new CommonFormDetail();
                        detail.setFormId(commonForm.getId());
                        detail.setGroupName("????????????");
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
                item.setRemark("????????????");
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
    FiveOaGoAbroadTrainAskMapper fiveOaGoAbroadTrainAskMapper;

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
    FiveOaInlandProjectReviewMapper fiveOaInlandProjectReviewMapper;
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
    @Resource
    FiveBusinessAdvanceCollectService fiveBusinessAdvanceCollectService;



    private Map getMapData(String businessKey) {
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        Object item = null;
        if (businessKey.startsWith("fiveOaGoAbroadTrainAsk")) { //??????????????????
            List<FiveOaGoAbroadTrainAsk> list = fiveOaGoAbroadTrainAskMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveOaMeetingRoomApply")){ //???????????????
            List<FiveOaMeetingRoomApply> list = fiveOaMeetingRoomApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaDecisionMaking")){ //????????????
            List<FiveOaDecisionMaking> list = fiveOaDecisionMakingMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveBusinessCustomer")) {//??????????????????
            List<BusinessCustomer> list = fiveBusinessCustomerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessSubpackage")||businessKey.startsWith("fiveBusinessPurchase")) {//??????????????????
            List<BusinessSubpackage> list = businessSubpackageMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdDesignDrawingCheck")) {//????????????????????????
            List<FiveEdDesignDrawingCheck> list = fiveEdDesignDrawingCheckMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveEdMajorDrawingCheck")) {//????????????????????????
            List<FiveEdMajorDrawingCheck> list = fiveEdMajorDrawingCheckMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaSuperviseDetail")) {//????????????
            List<FiveOaSuperviseDetail> list = fiveOaSuperviseDetailMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaSupervise_")) {//????????????
            List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveOaSuperviseYear_")) {//????????????????????????
            List<FiveOaSupervise> list = fiveOaSuperviseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaSuperviseFile_")) {//????????????
            List<FiveOaSuperviseFile> list = fiveOaSuperviseFileMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrQualifyProChief")) {//??????????????????????????????
            List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrQualifyChief")) {//????????????????????????
            List<FiveHrQualifyChief> list = fiveHrQualifyChiefMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrApproveApply")) {//???????????????????????????
            List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveHrDesignQualifyApply")) {//??????????????????????????????
            List<FiveHrQualifyApply> list = fiveHrQualifyApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaNewsExamine")) {//?????????????????????????????????
            List<FiveOaNewsexamine> list = fiveOaNewsexamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaProjectFundPlan_")) {//?????????????????????????????????
            List<FiveOaProjectfundplan> list = fiveOaProjectfundplanMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInventPayment")) {//??????????????????
            List<FiveOaInventPayment> list = fiveOaInventPaymentMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaScientificTaskCostApply")) {//????????????????????????
            List<FiveOaScientificTaskCostApply> list = fiveOaScientificTaskCostApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInventApply")) {//????????????
            List<HrInvent> list = hrInventMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaNoIndependentDeptSet")) {//????????????????????????????????????
            List<FiveOaNonIndependentDeptSet> list = fiveOaNonIndependentDeptSetMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaMaterialPurchase")) {// ?????????????????????
            List<FiveOaMaterialPurchase> list = fiveOaMaterialPurchaseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaMaterialBorrow")) {//??????????????????/?????????????????????
            List<FiveOaMaterialBorrow> list = fiveOaMaterialBorrowMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaBidApply")) {//????????????
            List<FiveOaBidApply> list = fiveOaBidApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaContractSign")) {//?????????????????????
            List<FiveOaContractSign> list = fiveOaContractSignMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPlatformRecord")) {//??????????????????????????????
            List<FiveOaPlatformRecord> list = fiveOaPlatformRecordMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeRetireExamine")) {//?????????????????????
            List<FiveOaEmployeeRetireExamine> list = fiveOaEmployeeRetireExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaFurniturePurchase_")) {//??????????????????
            List<FiveOaFurniturePurchase> list = fiveOaFurniturePurchaseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessRecord")) {// ??????????????????
            List<BusinessRecord> list = businessRecordMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeDimissionExamine")) {//   ?????????????????????
            List<FiveOaEmployeeDimissionExamine> list = fiveOaEmployeeDimissionExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeTransferExamine")) {//   ???????????????????????????
            List<FiveOaEmployeeTransferExamine> list = fiveOaEmployeeTransferExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEmployeeEntryExamine")) {//   ?????????????????????
            List<FiveOaEmployeeEntryExamine> list = fiveOaEmployeeEntryExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaGoAbroad_")) {//    ??????????????????????????????
            List<FiveOaGoAbroad> list = fiveOaGoAbroadMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerChange")) {//    ??????????????????
            List<FiveOaComputerChange> list = fiveOaComputerChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentProcurement")) {//????????????????????????????????????
            List<FiveOaInformationEquipmentProcurement> list = fiveOaInformationEquipmentProcurementMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSecretSystem")) {//??????????????????????????????????????????????????????
            List<FiveOaSecretSystem> list = fiveOaSecretSystemMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerApplication")) {//??????????????????
            List<FiveOaComputerApplication> list = fiveOaComputerApplicationMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaProcessDevelopApply")) {//??????????????????U????????????
            List<FiveOaProcessDevelopApply> list = fiveOaProcessDevelopApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerNetwork")) {//     ???????????????????????????
            List<FiveOaComputerNetwork> list = fiveOaComputerNetworkMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentApply")) {//     ??????????????????????????????
            List<FiveOaInformationEquipmentApply> list = fiveOaInformationEquipmentApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSoftware")) {//     ????????????????????????????????????
            List<FiveOaSoftware> list = fiveOaSoftwareMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssetComputer")) {//?????????????????????????????????????????????
            List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationPlan")) {// ????????????????????????
            List<FiveOaInformationPlan> list = fiveOaInformationPlanMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaNonSecretEquipmentScrap")) {//     ????????????????????????????????????
            List<FiveOaNonSecretEquipmentScrap> list = fiveOaNonSecretEquipmentScrapMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerMaintain")) {//     ?????????????????????????????????
            List<FiveOaComputerMaintain> list = fiveOaComputerMaintainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaComputerPersonRepair")) {//     ??????????????????????????????????????????????????????
            List<FiveOaComputerPersonRepair> list = fiveOaComputerPersonRepairMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPressurePipSealExamine")) {//     ?????????????????????????????????????????????
            List<FiveOaPressurePipSealExamine> list = fiveOaPressurePipSealExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaTechnologyArticleExamine")) {//     ?????????????????????????????????
            List<FiveOaTechnologyArticleExamine> list = fiveOaTechnologyArticleExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaOutTechnicalExchange")) {//     ????????????????????????????????????
            List<FiveOaOutTechnicalExchange> list = fiveOaOutTechnicalExchangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaProfessionalSkillTrain")) {//     ???????????????????????????
            List<FiveOaProfessionalSkillTrain> list = fiveOaProfessionalSkillTrainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaDeptJournal")) {//     ????????????
            List<FiveOaDeptJournal> list = fiveOaDeptJournalMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaGoAbroadTrainDeclare")) {//     ??????????????????
            List<FiveOaGoAbroadTrainDeclare> list = fiveOaGoAbroadTrainDeclareMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaOutSpecialist")) {//     ?????????????????????
            List<FiveOaOutSpecialist> list = fiveOaOutSpecialistMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssociationPayment")) {//     ????????????
            List<FiveOaAssociationPayment> list = fiveOaAssociationPaymentMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInlandProjectApply_")) {// ??????????????????
            List<FiveOaInlandProjectApply> list = fiveOaInlandProjectApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInlandProjectReview_")) {// ??????????????????
            List<FiveOaInlandProjectReview> list = fiveOaInlandProjectReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssociationChange")) {//     ??????????????????
            List<FiveOaAssociationChange> list = fiveOaAssociationChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaAssociationApply")) {//     ???????????????
            List<FiveOaAssociationApply> list = fiveOaAssociationApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaDepartmentPost")) {//     ???????????????
            List<FiveOaDepartmentPost> list = fiveOaDepartmentPostMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaFileSynergy_")) {//     ????????????
            List<FiveOaFileSynergy> list = fiveOaFileSynergyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentExamine_")) {//     ??????????????????????????????
            List<FiveOaInformationEquipmentExamine> list = fiveOaInformationEquipmentExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaInformationEquipmentExamineList")) {//     ?????????????????????(??????)?????????
            List<FiveOaInformationEquipmentExamineList> list = fiveOaInformationEquipmentExamineListMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPrivilegeManagement")) {//     ???????????????
            List<FiveOaPrivilegeManagement> list = fiveOaPrivilegeManagementMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessContractReview")) {//     ????????????
            List<FiveBusinessContractReview> list = fiveBusinessContractReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessPreContract")) {//     ???????????????
            List<BusinessPreContract> list = businessPreContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessContract_")) {//     ????????????
            List<BusinessContract> list = businessContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("businessContract")) {//     ????????????
            List<BusinessContract> list = businessContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
       /* else if (businessKey.startsWith("oaStampApply")) {//     ????????????????????? ????????? ????????? ?????????
            List<OaStampApply> list = oaStampApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }*/
        else if (businessKey.startsWith("oaNoticeApply")) {//     ????????????
            List<OaNoticeApply> list = oaNoticeApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessIncome")) {//     ????????????
            List<BusinessIncome> list = businessIncomeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaDispatch")) {//????????????
            List<FiveOaDispatch> list = fiveOaDispatchMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaFileInstruction")) {//???????????????
            List<FiveOaFileInstruction> list = fiveOaFileInstructionMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaReport")) {//????????????
            List<FiveOaReport> list = fiveOaReportMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaContractLawExamine")) {//???????????????????????????
            List<FiveOaContractLawExamine> list = fiveOaContractLawExamineMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSignQuote")) {//??????
            List<FiveOaSignQuote> list = fiveOaSignQuoteMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        } else if (businessKey.startsWith("fiveFinanceTravelExpenseCommon")|businessKey.startsWith("fiveFinanceTravelExpenseFunction")||businessKey.startsWith("fiveFinanceTravelExpenseAccountsRed")||businessKey.startsWith("fiveFinanceTravelExpenseBuild")) {//???????????????
            List<FiveFinanceTravelExpense> list = fiveFinanceTravelExpenseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceReimburseCommon")||businessKey.startsWith("fiveFinanceReimburseFunction")||businessKey.startsWith("fiveFinanceReimburseRed")||businessKey.startsWith("fiveFinanceReimburseBuild")) {//????????????
            List<FiveFinanceReimburse> list = fiveFinanceReimburseMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaMeetingArrange")) {//????????????
            List<FiveOaMeetingArrange> list = fiveOaMeetingArrangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceIncome_")) {//????????????
            List<FiveFinanceIncome> list = fiveFinanceIncomeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceIncomeConfirm")) {//????????????
            List<FiveFinanceIncomeConfirm> list = fiveFinanceIncomeConfirmMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceInvoice_")) {//????????????
            List<FiveFinanceInvoice> list = fiveFinanceInvoiceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceInvoiceCancel")) {//??????????????????
            List<FiveFinanceInvoiceCancel> list = fiveFinanceInvoiceCancelMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceNode")) {//????????????
            List<FiveFinanceNode> list = fiveFinanceNodeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceRefund")) {//??????
            List<FiveFinanceRefund> list = fiveFinanceRefundMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceLoanCommon")||businessKey.startsWith("fiveFinanceLoanFunction")||businessKey.startsWith("fiveFinanceLoanAccountsRed")||businessKey.startsWith("fiveFinanceLoanBuild")) {//??????
            List<FiveFinanceLoan> list = fiveFinanceLoanMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceReceipt")) {//????????????
            List<FiveFinanceReceipt> list = fiveFinanceReceiptMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceInvoiceCollection")) {//?????????????????????
            List<FiveFinanceInvoiceCollection> list = fiveFinanceInvoiceCollectionMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("oaCarMaintain")) {//????????????
            List<FiveOaCarMaintain> list = fiveOaCarMaintainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaSelfCarApply")) {//????????????
            List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaLeaderCarApply")) {//????????????
            List<FiveOaCarApply> list = fiveOaCarApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveAssetAllot_")) {//?????????????????????
            List<FiveAssetAllot> list = fiveAssetAllotMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaCardChange")) {//???????????????
            List<FiveOaCardChange> list = fiveOaCardChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveComputerScrap")) {//???????????????????????????
            List<FiveComputerScrap> list = fiveComputerScrapMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveAssetScrap")) {//?????????????????????
            List<FiveAssetScrap> list = fiveAssetScrapMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceProjectBudget")) {//????????????
            List<FiveFinanceProjectBudget> list = fiveFinanceProjectBudgetMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceDeptBudget")) {//???????????????
            List<FiveFinanceDeptBudget> list = fiveFinanceDeptBudgetMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceBalance")) {//??????????????????
            List<FiveFinanceBalance> list = fiveFinanceBalanceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessSupplier")) {//??????????????????
            List<BusinessSupplier> list = businessSupplierMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaPrivilegeManagement")) {//??????????????????
            List<FiveOaPrivilegeManagement> list = fiveOaPrivilegeManagementMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceStampTax")) {//???????????????
            List<FiveFinanceStampTax> list = fiveFinanceStampTaxMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceOutSupply")) {//????????????????????????
            List<FiveFinanceOutSupply> list = fiveFinanceOutSupplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceBackLetter")) {//????????????
            List<FiveFinanceBackLetter> list = fiveFinanceBackLetterMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetIndependent")
                ||businessKey.startsWith("fiveBudgetProduction")
                ||businessKey.startsWith("fiveBudgetFunction")
                ||businessKey.startsWith("fiveBudgetPostExpense")) {//????????????????????????
            List<FiveBudgetIndependent> list = fiveBudgetIndependentMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetMaintain")
                ||businessKey.startsWith("fiveBudgetBusiness")
                ||businessKey.startsWith("fiveBudgetStock")) {//?????????????????? ????????????
            List<FiveBudgetMaintain> list = fiveBudgetMaintainMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetScientificFundsOut")) {//??????????????????
            List<FiveBudgetScientificFundsOut> list = fiveBudgetScientificFundsOutMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetScientificFundsIn")) {//??????????????????
            List<FiveBudgetScientificFundsIn> list = fiveBudgetScientificFundsInMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetPublicFunds")) {//??????????????????
            List<FiveBudgetPublicFunds> list = fiveBudgetPublicFundsMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetCapitalOut")
                ||businessKey.startsWith("fiveBudgetTurnInRent")) {//????????????????????? ??????????????????
            List<FiveBudgetCapitalOut> list = fiveBudgetCapitalOutMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetLaborCost")
                ||businessKey.startsWith("fiveBudgetStaffNumber")) {//???????????? ??????????????????
            List<FiveBudgetLaborCost> list = fiveBudgetLaborCostMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }

        else if (businessKey.startsWith("fiveBudgetTurnIn")) {//????????????
            List<FiveBudgetTurnIn> list = fiveBudgetTurnInMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetFee")) {//????????????
            List<FiveBudgetFee> list = fiveBudgetFeeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBudgetFeeChange")) {//??????????????????
            List<FiveBudgetFeeChange> list = fiveBudgetFeeChangeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceTransferAccountsCommon")||businessKey.startsWith("fiveFinanceTransferAccountsRed")||businessKey.startsWith("fiveFinanceTransferAccountsBuild")||businessKey.startsWith("fiveFinanceTransferAccountsFinance")) {//????????????
            List<FiveFinanceTransferAccounts> list = fiveFinanceTransferAccountsMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceSelfBank")) {//????????????
            List<FiveFinanceSelfBank> list = fiveFinanceSelfBankMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceCompanyBank")) {//????????????
            List<FiveFinanceCompanyBank> list = fiveFinanceCompanyBankMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessChangeCustomer")) {//??????????????????
            List<BusinessChangeCustomer> list = businessChangeCustomerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessChangeSupplier")) {//??????????????????
            List<BusinessChangeSupplier> list = businessChangeSupplierMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessOutAssist")) {//????????????
            List<FiveBusinessOutAssist> list = fiveBusinessOutAssistMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessCooperationDelegation")) {//????????????????????????
            List<FiveBusinessCooperationDelegation> list = fiveBusinessCooperationDelegationMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessCooperationContract")) {//????????????????????????
            List<FiveBusinessCooperationContract> list = fiveBusinessCooperationContractMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaExternalResearchProjectApply")) {//????????????????????????
            List<FiveOaExternalResearchProjectApply> list = fiveOaExternalResearchProjectApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaExternalStandardApply")) {//???????????????????????????????????????
            List<FiveOaExternalStandardApply> list = fiveOaExternalStandardApplyMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaEquipmentAcceptance")) {//????????????
            List<FiveOaEquipmentAcceptance> list = fiveOaEquipmentAcceptanceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessAdvance_")) {//??????????????????
            List<FiveBusinessAdvance> list = fiveBusinessAdvanceMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessAdvanceCollect")) {//??????????????????
            List<FiveBusinessAdvanceCollect> list = fiveBusinessAdvanceCollectMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("oaStampApplyOffice")) {//?????????????????????
        }
        else if (businessKey.startsWith("oaStampApplyOffice_")) {//?????????????????????
            List<FiveOaStampApplyOffice> list = fiveOaStampApplyOfficeMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessTenderDocumentReview")) {//????????????????????????????????????????????????????????????
            List<FiveBusinessTenderDocumentReview> list = fiveBusinessTenderDocumentReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveOaResearchProjectReview")) {//??????????????????
            List<FiveOaResearchProjectReview> list = fiveOaResearchProjectReviewMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaMeetRoom_")) {//???????????????
            List<FiveOaMeetingRoom> list = fiveOaMeetingRoomMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveAssetComputer_")) {//?????????????????????
            List<FiveAssetComputer> list = fiveAssetComputerMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveOaInformationEquipmentExamineList")) {//?????????????????????(??????)??????
            List<FiveOaInformationEquipmentExamineList> list = fiveOaInformationEquipmentExamineListMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }else if (businessKey.startsWith("fiveBusinessContractLibrary")) {//?????????
            List<FiveBusinessContractLibrary> list = fiveBusinessContractLibraryMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveBusinessContractLibrarySubpackage_")) {//?????????????????????(??????)??????
            List<FiveBusinessContractLibrarySubpackage> list = fiveBusinessContractLibrarySubpackageMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }
        else if (businessKey.startsWith("fiveFinanceSubpackagePayment_")) {//??????????????????
            List<FiveFinanceSubpackagePayment> list = fiveFinanceSubpackagePaymentMapper.selectAll(params);
            if (list.size() > 0) item = list.get(0);
        }

        if (item != null) {
            return JsonMapper.string2Map(JsonMapper.obj2String(item));
        }

        return null;
    }


   /**
    * ????????????
    * */
   @Override
    public List<Map> listFormChildList(String businessKey) {

        List<Map> result = Lists.newArrayList();
        //???????????? result.add(item2);
        if (businessKey.contains("fiveOaEquipmentAcceptance_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveOaEquipmentAcceptanceDetail> detailList1 = fiveOaEquipmentAcceptanceService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaEquipmentAcceptanceDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getEquipmentName(),"??????",6));
                item.add(new CommonCode(detail.getInvoiceNo(),"????????????",6));
                item.add(new CommonCode(detail.getSpecification(),"????????????",6));
                item.add(new CommonCode(detail.getDeptName(),"????????????",6));
                item.add(new CommonCode(detail.getPrice(),"??????(???)",6));
                item.add(new CommonCode(detail.getNumber().toString(),"??????",6));
                item.add(new CommonCode(detail.getTotalPrice(),"??????",6));
                item.add(new CommonCode("","",6));//??????seq ???6?????????%2 ?????????????????????????????????

                item.add(new CommonCode(detail.getRemark(),"??????",6));//??????seq ???6?????????%2 ?????????????????????????????????
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveAssetScrap_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveAssetScrapDetail> detailList1 = fiveAssetScrapService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveAssetScrapDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getDeviceType(),"????????????",6));
                item.add(new CommonCode(detail.getDeviceNo(),"????????????",6));
                item.add(new CommonCode(detail.getDeviceModel(),"????????????",6));
                item.add(new CommonCode(detail.getDutyPersonName(),"?????????",6));
                item.add(new CommonCode(detail.getStartTime().toString(),"????????????",6));
                item.add(new CommonCode(detail.getOriginalValue().toString(),"??????",6));
                item.add(new CommonCode(detail.getDepreciableLife().toString(),"????????????",6));
                item.add(new CommonCode(detail.getNetWorth().toString(),"??????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveComputerScrap_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveComputerScrapDetail> detailList1 = fiveComputerScrapService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveComputerScrapDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getDeviceType(),"????????????",6));
                item.add(new CommonCode(detail.getDeviceNo(),"????????????",6));
                item.add(new CommonCode(detail.getDeviceModel(),"????????????",6));
               // item.add(new CommonCode(detail.getDutyPersonName(),"?????????",6));
                item.add(new CommonCode(detail.getStartTime().toString(),"????????????",6));
                item.add(new CommonCode(detail.getOriginalValue().toString(),"??????",6));
                item.add(new CommonCode(detail.getDepreciableLife().toString(),"????????????",6));
                item.add(new CommonCode(detail.getNetWorth().toString(),"??????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaFurniturePurchase_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveOaFurniturePurchaseDetail> detailList1 = fiveOaFurniturePurchaseService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaFurniturePurchaseDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getFurnitureName(),"??????",6));
                item.add(new CommonCode(detail.getNumber(),"??????",6));
                item.add(new CommonCode(detail.getPrice(),"???????????????",6));
                item.add(new CommonCode(detail.getTotalPrice(),"???????????????",6));
                item.add(new CommonCode(detail.getRemark(),"??????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaSuperviseYear_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????????????????");
            item1.put("list",list1);
            List<FiveOaSuperviseDetail> detailList1 = fiveOaSuperviseService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaSuperviseDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getSuperviseType(),"????????????",6));
                item.add(new CommonCode(detail.getTaskSource(),"????????????",6));
                item.add(new CommonCode(detail.getTaskDefinition(),"????????????",6));
                item.add(new CommonCode(detail.getTransactorName(),"?????????",6));
                item.add(new CommonCode(detail.getTimeLimit(),"????????????",6));
                item.add(new CommonCode(detail.getDeptName(),"????????????",6));
                item.add(new CommonCode(detail.getFeedbackTime(),"????????????",6));
                item.add(new CommonCode(detail.getTransactionPlan(),"????????????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaSupervise_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveOaSuperviseDetail> detailList1 = fiveOaSuperviseService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaSuperviseDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getSuperviseType(),"????????????",6));
                item.add(new CommonCode(detail.getTaskSource(),"????????????",6));
                item.add(new CommonCode(detail.getTaskDefinition(),"????????????",6));
                item.add(new CommonCode(detail.getTransactorName(),"?????????",6));
                item.add(new CommonCode(detail.getTimeLimit(),"????????????",6));
                item.add(new CommonCode(detail.getDeptName(),"????????????",6));
                item.add(new CommonCode(detail.getFeedbackTime(),"????????????",6));
                item.add(new CommonCode(detail.getTransactionPlan(),"????????????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaCardChange_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","?????????????????????");
            item1.put("list",list1);
            List<FiveOaCardChangeDetail> detailList1 = fiveOaCardChangeService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaCardChangeDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getUserName(),"??????",6));
                item.add(new CommonCode(detail.getUserLogin(),"?????????",6));
                item.add(new CommonCode(detail.getUserType(),"????????????",6));
                item.add(new CommonCode(detail.getCardTypeNow(),"?????????????????????",6));
                item.add(new CommonCode(detail.getCardTypeChange(),"?????????????????????",6));
                item.add(new CommonCode(detail.getRemark(),"??????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaResearchProjectReview_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveOaResearchProjectReviewDetail> detailList1 = fiveOaResearchProjectReviewSevice.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaResearchProjectReviewDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getPersonName(),"????????????",6));
                item.add(new CommonCode(detail.getOpinion(),"????????????",6));
                item.add(new CommonCode(detail.getApproved(),"??????????????????",6));
                item.add(new CommonCode(detail.getRemark(),"????????????",6));
                item.add(new CommonCode(detail.getCreatorName(),"?????????",6));
                item.add(new CommonCode(detail.getGmtCreate().toString(),"????????????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveOaInformationEquipmentExamineList_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","??????????????????");
            item1.put("list",list1);
            List<FiveOaInformationEquipmentExamineListDetail> detailList1 = fiveOaInformationEquipmentExamineListService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveOaInformationEquipmentExamineListDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getEquipmentName(),"????????????",6));
                item.add(new CommonCode(detail.getEquipmentNo(),"???????????????",6));
                item.add(new CommonCode(detail.getFormNo(),"????????????",6));
                item.add(new CommonCode(detail.getChargeMan(),"?????????",6));
                item.add(new CommonCode(detail.getUserName(),"?????????",6));
                item.add(new CommonCode(detail.getOsInstallTime(),"????????????",6));
                item.add(new CommonCode(detail.getFixedAssetNo(),"????????????",6));
                item.add(new CommonCode(detail.getDeptName(),"????????????",6));
                item.add(new CommonCode(detail.getUseType(),"??????",6));
                item.add(new CommonCode(detail.getStartTime(),"????????????",6));
                item.add(new CommonCode(detail.getCheckPrice(),"????????????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveBusinessContractReview_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveBusinessContractReviewDetail> detailList1 = fiveBusinessContractReviewService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveBusinessContractReviewDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getReviewContent(),"????????????",10));
                item.add(new CommonCode(detail.getReviewResult(),"????????????",2));
                list1.add(item);
            }
            result.add(item1);
        }
        else if (businessKey.contains("fiveBusinessAdvance_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveBusinessAdvanceDetail> detailList1 = fiveBusinessAdvanceSevice.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveBusinessAdvanceDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getPersonNo(),"?????????",6));
                item.add(new CommonCode(detail.getPersonName(),"??????",6));
                item.add(new CommonCode(detail.getDeptName(),"????????????",6));
                item.add(new CommonCode(detail.getPersonnelCategory(),"????????????",6));
                item.add(new CommonCode(detail.getProjectBonus(),"???????????????",6));
                item.add(new CommonCode(detail.getRemark(),"??????",6));
                list1.add(item);
            }
            result.add(item1);
        }else if (businessKey.contains("fiveBusinessAdvanceCollect_")){
            List<List<CommonCode>> list1 = Lists.newArrayList();
            Map item1=Maps.newHashMap();
            item1.put("name","????????????");
            item1.put("list",list1);
            List<FiveBusinessAdvanceCollectDetail> detailList1 = fiveBusinessAdvanceCollectService.listDetail(Integer.parseInt(businessKey.split("_")[1]));
            for (FiveBusinessAdvanceCollectDetail detail:detailList1){
                List<CommonCode> item=Lists.newArrayList();
                item.add(new CommonCode(detail.getDeptName(),"????????????",6));
                item.add(new CommonCode(detail.getApplyMoney(),"????????????????????????",6));
                item.add(new CommonCode(detail.getCompanyMoney(),"??????????????????????????????",6));
                item.add(new CommonCode(detail.getRealMoney(),"??????????????????????????????",6));
                list1.add(item);
            }
            result.add(item1);
        }
        return result;
    }
}
