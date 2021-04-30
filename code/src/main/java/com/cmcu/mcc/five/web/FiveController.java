package com.cmcu.mcc.five.web;

import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.CookieSessionUtils;
import com.cmcu.mcc.five.entity.FiveContentFile;
import com.cmcu.mcc.five.service.FiveContentFileService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/five")
public class FiveController {




    //成果用印审批
    @RequestMapping("/me/stamp")
    public ModelAndView stamp(){
        return new ModelAndView("/five/me/stamp");
    }

    @RequestMapping("/me/stampDetail")
    public ModelAndView stampDetail(){
        return new ModelAndView("/five/me/stampDetail");
    }



    @RequestMapping("/detail")
    public ModelAndView detail(){
        return new ModelAndView("/five/detail");
    }

    @RequestMapping("/partial-show")
    public ModelAndView partialShow(){
        return new ModelAndView("/five/partial-show");
    }
    @RequestMapping("/partial-stepShow")
    public ModelAndView partialStepShow(){
        return new ModelAndView("/five/partial-stepShow");
    }
    @RequestMapping("/partial-step")
    public ModelAndView partialStep(){
        return new ModelAndView("/five/partial-step");
    }
    @RequestMapping("/partial-stepDetail")
    public ModelAndView partialStepDetail(){
        return new ModelAndView("/five/partial-stepDetail");
    }

    @RequestMapping("/partial-form")
    public ModelAndView form(){
        return new ModelAndView("/five/partial-form");
    }

    @RequestMapping("/partial-formDetail")
    public ModelAndView formDetail(){
        return new ModelAndView("/five/partial-formDetail");
    }

    @RequestMapping("/partial-doc")
    public ModelAndView doc(){
        return new ModelAndView("/five/partial-doc");
    }

    @RequestMapping("/partial-designRule")
    public ModelAndView partialDesignRule(){
        return new ModelAndView("/five/partial-designRule");
    }
    @RequestMapping("/partial-designRuleDetail")
    public ModelAndView partialDesignRuleDetail(){
        return new ModelAndView("/five/partial-designRuleDetail");
    }
    @RequestMapping("/partial-designSign")
    public ModelAndView partialDesignSign(){
        return new ModelAndView("/five/partial-designSign");
    }
    @RequestMapping("/partial-designSignDetail")
    public ModelAndView partialDesignSignDetail(){
        return new ModelAndView("/five/partial-designSignDetail");
    }

    @RequestMapping("/partial-reviewMajor")
    public ModelAndView reviewMajor(){
        return new ModelAndView("/five/partial-reviewMajor");
    }
    @RequestMapping("/partial-reviewMajorDetail")
    public ModelAndView reviewMajorDetail(){
        return new ModelAndView("/five/partial-reviewMajorDetail");
    }

    @RequestMapping("/partial-reviewPlan")
    public ModelAndView reviewPlan(){
        return new ModelAndView("/five/partial-reviewPlan");
    }
    @RequestMapping("/partial-reviewPlanDetail")
    public ModelAndView reviewPlanDetail(){
        return new ModelAndView("/five/partial-reviewPlanDetail");
    }

    @RequestMapping("/partial-specialReview")
    public ModelAndView partialSpecialReview(){
        return new ModelAndView("/five/partial-specialReview");
    }
    @RequestMapping("/partial-specialReviewDetail")
    public ModelAndView partialSpecialReviewDetail(){
        return new ModelAndView("/five/partial-specialReviewDetail");
    }

    @RequestMapping("/partial-companyReview")
    public ModelAndView partialCompanyReview(){
        return new ModelAndView("/five/partial-companyReview");
    }
    @RequestMapping("/partial-companyReviewDetail")
    public ModelAndView partialCompanyReviewDetail(){
        return new ModelAndView("/five/partial-companyReviewDetail");
    }

    @RequestMapping("/partial-expertReview")
    public ModelAndView partialExpertReview(){
        return new ModelAndView("/five/partial-expertReview");
    }
    @RequestMapping("/partial-expertReviewDetail")
    public ModelAndView partialExpertReviewDetail(){
        return new ModelAndView("/five/partial-expertReviewDetail");
    }


    @RequestMapping("/partial-outReview")
    public ModelAndView partialOutReview(){
        return new ModelAndView("/five/partial-outReview");
    }
    @RequestMapping("/partial-outReviewDetail")
    public ModelAndView partialOutReviewDetail(){
        return new ModelAndView("/five/partial-outReviewDetail");
    }

    @RequestMapping("/partial-returnVisit")
    public ModelAndView partialReturnVisit(){
        return new ModelAndView("/five/partial-returnVisit");
    }
    @RequestMapping("/partial-returnVisitDetail")
    public ModelAndView partialReturnVisitDetail(){
        return new ModelAndView("/five/partial-returnVisitDetail");
    }

    @RequestMapping("/partial-serviceChange")
    public ModelAndView partialServiceChange(){
        return new ModelAndView("/five/partial-serviceChange");
    }
    @RequestMapping("/partial-serviceChangeDetail")
    public ModelAndView partialServiceChangeDetail(){
        return new ModelAndView("/five/partial-serviceChangeDetail");
    }

    @RequestMapping("/partial-serviceDiscuss")
    public ModelAndView partialServiceDiscuss(){
        return new ModelAndView("/five/partial-serviceDiscuss");
    }
    @RequestMapping("/partial-serviceDiscussDetail")
    public ModelAndView partialServiceDiscussDetail(){
        return new ModelAndView("/five/partial-serviceDiscussDetail");
    }

    @RequestMapping("/partial-serviceHandle")
    public ModelAndView partialServiceHandle(){
        return new ModelAndView("/five/partial-serviceHandle");
    }
    @RequestMapping("/partial-serviceHandleDetail")
    public ModelAndView partialServiceHandleDetail(){
        return new ModelAndView("/five/partial-serviceHandleDetail");
    }

    @RequestMapping("/partial-arrange")
    public ModelAndView arrange(){
        return new ModelAndView("/five/partial-arrange");
    }
    @RequestMapping("/partial-arrangeDetail")
    public ModelAndView arrangeDetail(){
        return new ModelAndView("/five/partial-arrangeDetail");
    }
    @RequestMapping("/partial-task")
    public ModelAndView task(){
        return new ModelAndView("/five/partial-task");
    }
    @RequestMapping("/partial-taskDetail")
    public ModelAndView taskDetail(){
        return new ModelAndView("/five/partial-taskDetail");
    }


    @RequestMapping("/partial-houseRefer")
    public ModelAndView houseRefer(){
        return new ModelAndView("/five/partial-houseRefer");
    }
    @RequestMapping("/partial-houseReferDetail")
    public ModelAndView houseReferDetail(){
        return new ModelAndView("/five/partial-houseReferDetail");
    }

    @RequestMapping("/partial-reviewApply")
    public ModelAndView reviewApply(){
        return new ModelAndView("/five/partial-reviewApply");
    }
    @RequestMapping("/partial-reviewApplyDetail")
    public ModelAndView reviewApplyDetail(){
        return new ModelAndView("/five/partial-reviewApplyDetail");
    }

    @RequestMapping("/partial-reviewSpecial")
    public ModelAndView reviewSpecial(){
        return new ModelAndView("/five/partial-reviewSpecial");
    }
    @RequestMapping("/partial-reviewSpecialDetail")
    public ModelAndView reviewSpecialDetail(){
        return new ModelAndView("/five/partial-reviewSpecialDetail");
    }

    @RequestMapping("/partial-houseValidate")
    public ModelAndView houseValidate(){
        return new ModelAndView("/five/partial-houseValidate");
    }
    @RequestMapping("/partial-houseValidateDetail")
    public ModelAndView houseValidateDetail(){
        return new ModelAndView("/five/partial-houseValidateDetail");
    }

    @RequestMapping("/partial-countersign")
    public ModelAndView countersign(){
        return new ModelAndView("/five/partial-countersign");
    }
    @RequestMapping("/partial-countersignDetail")
    public ModelAndView countersignDetail(){
        return new ModelAndView("/five/partial-countersignDetail");
    }

    @RequestMapping("/partial-qualityIssue")
    public ModelAndView partialQualityIssue(){
        return new ModelAndView("/five/partial-qualityIssue");
    }
    @RequestMapping("/partial-qualityIssueDetail")
    public ModelAndView partialQualityIssueDetail(){
        return new ModelAndView("/five/partial-qualityIssueDetail");
    }

    @RequestMapping("/partial-plotApply")
    public ModelAndView partialPlotApply(){
        return new ModelAndView("/five/partial-plotApply");
    }
    @RequestMapping("/partial-plotApplyDetail")
    public ModelAndView partialPlotApplyDetail(){
        return new ModelAndView("/five/partial-plotApplyDetail");
    }

    @RequestMapping("/partial-stamp")
    public ModelAndView partialStamp(){
        return new ModelAndView("/five/partial-stamp");
    }
    @RequestMapping("/partial-stampDetail")
    public ModelAndView partialStampDetail(){
        return new ModelAndView("/five/partial-stampDetail");
    }

    @RequestMapping("/partial-designDrawingCheck")
    public ModelAndView designDrawingCheck(){
        return new ModelAndView("/five/partial-designDrawingCheck");
    }
    @RequestMapping("/partial-designDrawingCheckDetail")
    public ModelAndView designDrawingCheckDetail(){
        return new ModelAndView("/five/partial-designDrawingCheckDetail");
    }
    //专业图纸验收清单
    @RequestMapping("/partial-majorDrawingCheck")
    public ModelAndView majorDrawingCheck(){
        return new ModelAndView("/five/partial-majorDrawingCheck");
    }
    @RequestMapping("/partial-majorDrawingCheckDetail")
    public ModelAndView majorDrawingCheckDetail(){
        return new ModelAndView("/five/partial-majorDrawingCheckDetail");
    }
   //设计图纸验收清单
    @RequestMapping("/partial-designDrawing")
    public ModelAndView designDrawing(){
        return new ModelAndView("/five/partial-designDrawing");
    }
    @RequestMapping("/partial-designDrawingDetail")
    public ModelAndView designDrawingDetail(){
        return new ModelAndView("/five/partial-designDrawingDetail");
    }

    @RequestMapping("/partial-product")
    public ModelAndView product(){
        return new ModelAndView("/five/partial-product");
    }
    @RequestMapping("/partial-productDetail")
    public ModelAndView productDetail(){
        return new ModelAndView("/five/partial-productDetail");
    }


    @RequestMapping("/partial-qualityCheck")
    public ModelAndView qualityCheck(){
        return new ModelAndView("/five/partial-qualityCheck");
    }

    @RequestMapping("/partial-qualityCheckDetail")
    public ModelAndView qualityCheckDetail(){
        return new ModelAndView("/five/partial-qualityCheckDetail");
    }

    @RequestMapping("/partial-qualityAnalysis")
    public ModelAndView qualityAnalysis(){
        return new ModelAndView("/five/partial-qualityAnalysis");
    }

    @RequestMapping("/partial-qualityAnalysisDetail")
    public ModelAndView qualityAnalysisDetail(){
        return new ModelAndView("/five/partial-qualityAnalysisDetail");
    }

    //经营管理
    @RequestMapping("/business/preContract")
    public ModelAndView businessPreContract(){
        return new ModelAndView("/five/business/preContract");
    }
    @RequestMapping("/business/preContractDetail")
    public ModelAndView businessPreContractDetail(){
        return new ModelAndView("/five/business/preContractDetail");
    }

    @RequestMapping("/business/inputContract")
    public ModelAndView businessInputContract(){
        return new ModelAndView("/five/business/inputContract");
    }
    @RequestMapping("/business/inputContractDetail")
    public ModelAndView businessInputContractDetail(){
        return new ModelAndView("/five/business/inputContractDetail");
    }

    @RequestMapping("/business/contract")
    public ModelAndView businessContract(){
        return new ModelAndView("/five/business/contract");
    }
    @RequestMapping("/business/contractDetail")
    public ModelAndView businessContractDetail(){
        return new ModelAndView("/five/business/contractDetail");
    }

    @RequestMapping("/business/customer")
    public ModelAndView businessCustomer(){
        return new ModelAndView("/five/business/customer");
    }
    @RequestMapping("/business/customerDetail")
    public ModelAndView businessCustomerDetail(){
        return new ModelAndView("/five/business/customerDetail");
    }

    @RequestMapping("/business/changeCustomer")
    public ModelAndView changeCustomer(){
        return new ModelAndView("five/business/changeCustomer");
    }
    @RequestMapping("/business/changeCustomerDetail")
    public ModelAndView changeCustomerDetail(){
        return new ModelAndView("five/business/changeCustomerDetail");
    }

    @RequestMapping("/business/changeSupplier")
    public ModelAndView changeSupplier(){
        return new ModelAndView("five//business/changeSupplier");
    }
    @RequestMapping("/business/changeSupplierDetail")
    public ModelAndView changeSupplierDetail(){
        return new ModelAndView("five/business/changeSupplierDetail");
    }



    @RequestMapping("/business/record")
    public ModelAndView businessRecord(){
        return new ModelAndView("/five/business/record");
    }
    @RequestMapping("/business/recordDetail")
    public ModelAndView businessRecordDetail(){
        return new ModelAndView("/five/business/recordDetail");
    }

    @RequestMapping("/business/recordDetailManger")
    public ModelAndView businessRecordDetailManger(){
        return new ModelAndView("/five/business/recordDetailManger");
    }


    @RequestMapping("/me/contract")
    public ModelAndView meContract(){
        return new ModelAndView("/five/me/contract");
    }



    @RequestMapping("/hr/qualify")
    public ModelAndView qualify(){
        return new ModelAndView("/five/hr/qualify");
    }

    @RequestMapping("/hr/qualifyDetail")
    public ModelAndView qualifyDetail(){
        return new ModelAndView("/five/hr/qualifyDetail");
    }


    @RequestMapping("/hr/qualifyApply")
    public ModelAndView qualifyApply(){
        return new ModelAndView("/five/hr/qualifyApply");
    }

    @RequestMapping("/hr/qualifyApplyDetail")
    public ModelAndView qualifyApplyDetail(){
        return new ModelAndView("/five/hr/qualifyApplyDetail");
    }


    @RequestMapping("/hr/chiefApply")
    public ModelAndView chiefApply(){
        return new ModelAndView("/five/hr/chiefApply");
    }

    @RequestMapping("/hr/chiefApplyDetail")
    public ModelAndView chiefApplyDetail(){
        return new ModelAndView("/five/hr/chiefApplyDetail");
    }



    @RequestMapping("/hr/externalApply")
    public ModelAndView externalApply(){
        return new ModelAndView("/five/hr/externalApply");
    }

    @RequestMapping("/hr/externalApplyDetail")
    public ModelAndView externalApplyDetail(){
        return new ModelAndView("/five/hr/externalApplyDetail");
    }


    @RequestMapping("/hr/approveApply")
    public ModelAndView approveApply(){
        return new ModelAndView("/five/hr/approveApply");
    }

    @RequestMapping("/hr/approveApplyDetail")
    public ModelAndView approveApplyDetail(){
        return new ModelAndView("/five/hr/approveApplyDetail");
    }

    @RequestMapping("/oa/dispatch")
    public ModelAndView dispatchDraft(){
        return new ModelAndView("/five/oa/dispatch");
    }

    @RequestMapping("/oa/dispatchDetail")
    public ModelAndView dispatchDraftDetail(){
        return new ModelAndView("/five/oa/dispatchDetail");
    }

    @RequestMapping("/oa/departmentPost")
    public ModelAndView oaDepartmentPost(){
        return new ModelAndView("/five/oa/oaDepartmentPost");
    }
    @RequestMapping("/oa/departmentPostDetail")
    public ModelAndView oaDepartmentPostDetail(){
        return new ModelAndView("/five/oa/oaDepartmentPostDetail");
    }

    @RequestMapping("/oa/goAbroad")
    public ModelAndView oaGoAbroad(){
        return new ModelAndView("/five/oa/oaGoAbroad");
    }
    @RequestMapping("/oa/goAbroadDetail")
    public ModelAndView oaGoAbroadDetail(){
        return new ModelAndView("/five/oa/oaGoAbroadDetail");
    }

    @RequestMapping("/oa/contractLawExamine")
    public ModelAndView contractLawExamine(){
        return new ModelAndView("/five/oa/oaContractLawExamine");
    }
    @RequestMapping("/oa/contractLawExamineDetail")
    public ModelAndView contractLawExamineDetail(){
        return new ModelAndView("/five/oa/oaContractLawExamineDetail");
    }

    @RequestMapping("/oa/ruleLawExamine")
    public ModelAndView ruleLawExamine(){
        return new ModelAndView("/five/oa/oaRuleLawExamine");
    }
    @RequestMapping("/oa/ruleLawExamineDetail")
    public ModelAndView ruleLawExamineDetail(){
        return new ModelAndView("/five/oa/oaRuleLawExamineDetail");
    }


    @RequestMapping("/oa/fileInstruction")
    public ModelAndView fileInstruction(){
        return new ModelAndView("/five/oa/fileInstruction");
    }
    @RequestMapping("/oa/fileInstructionDetail")
    public ModelAndView fileInstructionDetail(){
        return new ModelAndView("/five/oa/fileInstructionDetail");
    }

    @RequestMapping("/oa/fileTransfer")
    public ModelAndView fileTransfer(){
        return new ModelAndView("/five/oa/fileTransfer");
    }
    @RequestMapping("/oa/fileTransferDetail")
    public ModelAndView fileTransferDetail(){
        return new ModelAndView("/five/oa/fileTransferDetail");
    }

    @RequestMapping("/oa/report")
    public ModelAndView report(){
        return new ModelAndView("/five/oa/report");
    }
    @RequestMapping("/oa/reportDetail")
    public ModelAndView reportDetail(){
        return new ModelAndView("/five/oa/reportDetail");
    }

    @RequestMapping("/oa/generalCountersign")
    public ModelAndView generalCountersign(){
        return new ModelAndView("/five/oa/generalCountersign");
    }
    @RequestMapping("/oa/generalCountersignDetail")
    public ModelAndView generalCountersignDetail(){
        return new ModelAndView("/five/oa/generalCountersignDetail");
    }


    @RequestMapping("/oa/platformRecord")
    public ModelAndView platformRecord(){
        return new ModelAndView("/five/oa/platformRecord");
    }
    @RequestMapping("/oa/platformRecordDetail")
    public ModelAndView platformRecordDetail(){
        return new ModelAndView("/five/oa/platformRecordDetail");
    }

    @RequestMapping("/oa/reviewContract")
    public ModelAndView reviewContract(){
        return new ModelAndView("/five/oa/reviewContract");
    }
    @RequestMapping("/oa/reviewContractDetail")
    public ModelAndView reviewContractDetail(){
        return new ModelAndView("/five/oa/reviewContractDetail");
    }

    @RequestMapping("/oa/contractSign")
    public ModelAndView contractSign(){
        return new ModelAndView("/five/oa/contractSign");
    }
    @RequestMapping("/oa/contractSignDetail")
    public ModelAndView contractSignDetail(){
        return new ModelAndView("/five/oa/contractSignDetail");
    }

    @RequestMapping("/oa/bidApply")
    public ModelAndView bidApply(){
        return new ModelAndView("/five/oa/bidApply");
    }
    @RequestMapping("/oa/bidApplyDetail")
    public ModelAndView bidApplyDetail(){
        return new ModelAndView("/five/oa/bidApplyDetail");
    }

    @RequestMapping("/oa/associationApply")
    public ModelAndView associationApply(){
        return new ModelAndView("/five/oa/associationApply");
    }
    @RequestMapping("/oa/associationApplyDetail")
    public ModelAndView associationApplyDetail(){
        return new ModelAndView("/five/oa/associationApplyDetail");
    }

    @RequestMapping("/oa/projectFundPlan")
    public ModelAndView projectFundPlan() {
        return new ModelAndView("/five/oa/oaProjectFundPlan");
    }
    @RequestMapping("/oa/projectFundPlanDetail")
    public ModelAndView projectFundPlanDetail() {
        return new ModelAndView("/five/oa/oaProjectFundPlanDetail");
    }

    @RequestMapping("/oa/associationChange")
    public ModelAndView associationChange(){
        return new ModelAndView("/five/oa/associationChange");
    }
    @RequestMapping("/oa/associationChangeDetail")
    public ModelAndView associationChangeDetail(){
        return new ModelAndView("/five/oa/associationChangeDetail");
    }



    @RequestMapping("/oa/associationPayment")
    public ModelAndView associationPayment(){
        return new ModelAndView("/five/oa/associationPayment");
    }
    @RequestMapping("/oa/associationPaymentDetail")
    public ModelAndView associationPaymentDetail(){
        return new ModelAndView("/five/oa/associationPaymentDetail");
    }

    @RequestMapping("/oa/newsExamine")
    public ModelAndView newsExamine(){
        return new ModelAndView("/five/oa/oaNewsExamine");
    }
    @RequestMapping("/oa/newsExamineDetail")
    public ModelAndView newsExamineDetail(){
        return new ModelAndView("/five/oa/oaNewsExamineDetail");
    }

    @RequestMapping("/oa/outSpecialist")
    public ModelAndView outSpecialist(){
        return new ModelAndView("/five/oa/oaOutSpecialist");
    }
    @RequestMapping("/oa/outSpecialistDetail")
    public ModelAndView outSpecialistDetail(){
        return new ModelAndView("/five/oa/oaOutSpecialistDetail");
    }
    @RequestMapping("/oa/deptJournal")
    public ModelAndView deptJournal(){
        return new ModelAndView("/five/oa/deptJournal");
    }
    @RequestMapping("/oa/deptJournalDetail")
    public ModelAndView deptJournalDetail(){
        return new ModelAndView("/five/oa/deptJournalDetail");
    }
    @RequestMapping("/oa/inventPayment")
    public ModelAndView inventPayment(){
        return new ModelAndView("/five/oa/inventPayment");
    }
    @RequestMapping("/oa/inventPaymentDetail")
    public ModelAndView inventPaymentDetail(){
        return new ModelAndView("/five/oa/inventPaymentDetail");
    }
    @RequestMapping("/oa/inventApply")
    public ModelAndView inventApply(){
        return new ModelAndView("/five/oa/inventApply");
    }
    @RequestMapping("/oa/inventApplyDetail")
    public ModelAndView inventApplyDetail(){
        return new ModelAndView("/five/oa/inventApplyDetail");
    }
    @RequestMapping("/oa/nonIndependentDeptSet")
    public ModelAndView nonIndependentDeptSet(){
        return new ModelAndView("/five/oa/oaNonIndependentDeptSet");
    }
    @RequestMapping("/oa/nonIndependentDeptSetDetail")
    public ModelAndView nonIndependentDeptSetDetail(){
        return new ModelAndView("/five/oa/oaNonIndependentDeptSetDetail");
    }
    //外部科研项目申请
    @RequestMapping("/oa/externalResearchProjectApply")
    public ModelAndView externalResearchProjectApply(){ return new ModelAndView("/five/oa/oaExternalResearchProjectApply");
    }
    @RequestMapping("/oa/externalResearchProjectApplyDetail")
    public ModelAndView externalResearchProjectApplyDetail(){
        return new ModelAndView("/five/oa/oaExternalResearchProjectApplyDetail");
    }
    //外部标准规范、图集项目申请
    @RequestMapping("/oa/externalStandardApply")
    public ModelAndView externalStandardApply(){return new ModelAndView("/five/oa/oaExternalStandardApply");
    }
    @RequestMapping("/oa/externalStandardApplyDetail")
        public ModelAndView externalStandardApplyDetail(){
        return new ModelAndView("/five/oa/oaExternalStandardApplyDetail");
    }

    //next
    @RequestMapping("/oa/goAbroadTrainDeclare")
    public ModelAndView goAbroadTrainDeclare(){
        return new ModelAndView("/five/oa/oaGoAbroadTrainDeclare");
    }
    @RequestMapping("/oa/goAbroadTrainDeclareDetail")
    public ModelAndView goAbroadTrainDeclareDetail(){
        return new ModelAndView("/five/oa/oaGoAbroadTrainDeclareDetail");
    }
    @RequestMapping("/oa/professionalSkillTrain")
    public ModelAndView professionalSkillTrain(){
        return new ModelAndView("/five/oa/oaProfessionalSkillTrain");
    }
    @RequestMapping("/oa/professionalSkillTrainDetail")
    public ModelAndView professionalSkillTrainDetail(){
        return new ModelAndView("/five/oa/oaProfessionalSkillTrainDetail");
    }
    @RequestMapping("/oa/outTechnicalExchange")
    public ModelAndView outTechnicalExchange(){
        return new ModelAndView("/five/oa/oaOutTechnicalExchange");
    }
    @RequestMapping("/oa/outTechnicalExchangeDetail")
    public ModelAndView outTechnicalExchangeDetail(){
        return new ModelAndView("/five/oa/oaOutTechnicalExchangeDetail");
    }
    @RequestMapping("/oa/technologyArticleExamine")
    public ModelAndView technologyArticleExamine(){
        return new ModelAndView("/five/oa/oaTechnologyArticleExamine");
    }
    @RequestMapping("/oa/technologyArticleExamineDetail")
    public ModelAndView technologyArticleExamineDetail(){
        return new ModelAndView("/five/oa/oaTechnologyArticleExamineDetail");
    }
    @RequestMapping("/oa/pressurePipSealExamine")
    public ModelAndView pressurePipSealExamine(){
        return new ModelAndView("/five/oa/oaPressurePipSealExamine");
    }
    @RequestMapping("/oa/pressurePipSealExamineDetail")
    public ModelAndView pressurePipSealExamineDetail(){
        return new ModelAndView("/five/oa/oaPressurePipSealExamineDetail");
    }
    @RequestMapping("/oa/scientificTaskCostApply")
    public ModelAndView scientificTaskCostApply(){
        return new ModelAndView("/five/oa/oaScientificTaskCostApply");
    }
    @RequestMapping("/oa/scientificTaskCostApplyDetail")
    public ModelAndView scientificTaskCostApplyDetail(){
        return new ModelAndView("/five/oa/oaScientificTaskCostApplyDetail");
    }


    @RequestMapping("/oa/software")
    public ModelAndView software(){
        return new ModelAndView("/five/oa/software");
    }
    @RequestMapping("/oa/softwareDetail")
    public ModelAndView softwareDetail(){
        return new ModelAndView("/five/oa/softwareDetail");
    }

    @RequestMapping("/oa/computerNetwork")
    public ModelAndView computerNetwork(){
        return new ModelAndView("/five/oa/computerNetwork");
    }
    @RequestMapping("/oa/computerNetworkDetail")
    public ModelAndView computerNetworkDetail(){
        return new ModelAndView("/five/oa/computerNetworkDetail");
    }

    @RequestMapping("/oa/computerChange")
    public ModelAndView computerChange(){
        return new ModelAndView("/five/oa/computerChange");
    }
    @RequestMapping("/oa/computerChangeDetail")
    public ModelAndView computerChangeDetail(){
        return new ModelAndView("/five/oa/computerChangeDetail");
    }

    @RequestMapping("/oa/messageExport")
    public ModelAndView messageExport(){
        return new ModelAndView("/five/oa/messageExport");
    }
    @RequestMapping("/oa/messageExportDetail")
    public ModelAndView messageExportDetail(){
        return new ModelAndView("/five/oa/messageExportDetail");
    }

    @RequestMapping("/oa/nonSecretEquipmentScrap")
    public ModelAndView nonSecretEquipmentScrap(){
        return new ModelAndView("/five/oa/oaNonSecretEquipmentScrap");
    }
    @RequestMapping("/oa/nonSecretEquipmentScrapDetail")
    public ModelAndView nonSecretEquipmentScrapDetail(){
        return new ModelAndView("/five/oa/oaNonSecretEquipmentScrapDetail");
    }
    @RequestMapping("/oa/secretSystem")
    public ModelAndView secretSystem(){
        return new ModelAndView("/five/oa/secretSystem");
    }
    @RequestMapping("/oa/secretSystemDetail")
    public ModelAndView secretSystemDetail(){
        return new ModelAndView("/five/oa/secretSystemDetail");
    }
    @RequestMapping("/oa/informationEquipmentExamine")
    public ModelAndView informationEquipmentExamine(){
        return new ModelAndView("/five/oa/oaInformationEquipmentExamine");
    }
    @RequestMapping("/oa/informationEquipmentExamineDetail")
    public ModelAndView informationEquipmentExamineDetail(){
        return new ModelAndView("/five/oa/oaInformationEquipmentExamineDetail");
    }
    //信息化设备验收审批（多台）
    @RequestMapping("/oa/oaInformationEquipmentExamineList")
    public ModelAndView oaInformationEquipmentExamineList(){
        return new ModelAndView("/five/oa/oaInformationEquipmentExamineList");
    }
    @RequestMapping("/oa/oaInformationEquipmentExamineListDetail")
    public ModelAndView oaInformationEquipmentExamineListDetail(){
        return new ModelAndView("/five/oa/oaInformationEquipmentExamineListDetail");
    }
    @RequestMapping("/oa/goAbroadTrainAsk")
    public ModelAndView goAbroadTrainAsk(){
        return new ModelAndView("/five/oa/oaGoAbroadTrainAsk");
    }
    @RequestMapping("/oa/goAbroadTrainAskDetail")
    public ModelAndView goAbroadTrainAskDetail(){
        return new ModelAndView("/five/oa/oaGoAbroadTrainAskDetail");
    }

    @RequestMapping("/oa/informationEquipmentApply")
    public ModelAndView informationEquipmentApply(){
        return new ModelAndView("/five/oa/oaInformationEquipmentApply");
    }
    @RequestMapping("/oa/informationEquipmentApplyDetail")
    public ModelAndView informationEquipmentApplyDetail(){
        return new ModelAndView("/five/oa/oaInformationEquipmentApplyDetail");
    }
    @RequestMapping("/oa/informationEquipmentProcurement")
    public ModelAndView informationEquipmentProcurement(){
        return new ModelAndView("/five/oa/oaInformationEquipmentProcurement");
    }
    @RequestMapping("/oa/informationEquipmentProcurementDetail")
    public ModelAndView informationEquipmentProcurementDetail(){
        return new ModelAndView("/five/oa/oaInformationEquipmentProcurementDetail");
    }

    @RequestMapping("/oa/signQuote")
    public ModelAndView signQuote(){
        return new ModelAndView("/five/oa/oaSignQuote");
    }
    @RequestMapping("/oa/signQuoteDetail")
    public ModelAndView signQuoteDetail(){
        return new ModelAndView("/five/oa/oaSignQuoteDetail");
    }

    @RequestMapping("/oa/institutionCountSign")
    public ModelAndView institutionCountSign(){
        return new ModelAndView("/five/oa/oaInstitutionCountSign");
    }
    @RequestMapping("/oa/institutionCountSignDetail")
    public ModelAndView institutionCountSignDetail(){
        return new ModelAndView("/five/oa/oaInstitutionCountSignDetail");
    }

    @RequestMapping("/oa/materialBorrow")
    public ModelAndView materialBorrow() {
        return new ModelAndView("/five/oa/oaMaterialBorrow");
    }
    @RequestMapping("/oa/materialBorrowDetail")
    public ModelAndView materialBorrowDetail(){
        return new ModelAndView("/five/oa/oaMaterialBorrowDetail");
    }

    @RequestMapping("/oa/materialPurchase")
    public ModelAndView materialPurchase(){
        return new ModelAndView("/five/oa/materialPurchase");
    }
    @RequestMapping("/oa/materialPurchaseDetail")
    public ModelAndView materialPurchaseDetail(){
        return new ModelAndView("/five/oa/materialPurchaseDetail");
    }

   @RequestMapping("/oa/computerApplication")
    public ModelAndView computerApplication() {
       return new ModelAndView("/five/oa/oaComputerApplication");
   }

    @RequestMapping("/oa/computerApplicationDetail")
    public ModelAndView computerApplicationDetail(){
        return new ModelAndView("/five/oa/oaComputerApplicationDetail");
    }

    @RequestMapping("/oa/computerPurchase")
    public ModelAndView computerPurchase() {
        return new ModelAndView("/five/oa/oaComputerPurchase");
    }

    @RequestMapping("/oa/computerPurchaseDetail")
    public ModelAndView computerPurchaseDetail(){
        return new ModelAndView("/five/oa/oaComputerPurchaseDetail");
    }

    @RequestMapping("/oa/computerMaintain")
    public ModelAndView computerMaintain() {
        return new ModelAndView("/five/oa/oaComputerMaintain");
    }

    @RequestMapping("/oa/computerMaintainDetail")
    public ModelAndView computerMaintainDetail(){
        return new ModelAndView("/five/oa/oaComputerMaintainDetail");
    }

    @RequestMapping("/oa/computerPersonRepair")
    public ModelAndView computerPersonRepair() {
        return new ModelAndView("/five/oa/oaComputerPersonRepair");
    }

    @RequestMapping("/oa/computerPersonRepairDetail")
    public ModelAndView computerPersonRepairDetail(){
        return new ModelAndView("/five/oa/oaComputerPersonRepairDetail");
    }


    @RequestMapping("/oa/computerSafe")
    public ModelAndView computerSafe() {
        return new ModelAndView("/five/oa/oaComputerSafe");
    }

    @RequestMapping("/oa/computerSafeDetail")
    public ModelAndView computerSafeDetail(){
        return new ModelAndView("/five/oa/oaComputerSafeDetail");
    }

    @RequestMapping("/oa/car")
    public ModelAndView car(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/car");
        return modelAndView;
    }
    @RequestMapping("/oa/carDetail")
    public ModelAndView carDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/carDetail");
        return modelAndView;
    }
    @RequestMapping("/oa/carApply")
    public ModelAndView carApply(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/carApply");
        return modelAndView;
    }

    @RequestMapping("/oa/carApplyDetail")
    public ModelAndView carApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/carApplyDetail");
        return modelAndView;
    }
    @RequestMapping("/oa/carMaintain")
    public ModelAndView carMaintain(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/carMaintain");
        return modelAndView;
    }

    @RequestMapping("/oa/carMaintainDetail")
    public ModelAndView carMaintainDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/carMaintainDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/meetingRoom")
    public ModelAndView meetingRoom(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/meetingRoom");
        return modelAndView;
    }
    @RequestMapping("/oa/meetingRoomDetail")
    public ModelAndView meetingRoomDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/meetingRoomDetail");
        return modelAndView;
    }
    @RequestMapping("/oa/meetingRoomApply")
    public ModelAndView meetingRoomApply(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/meetingRoomApply");
        return modelAndView;
    }

    @RequestMapping("/oa/meetingRoomApplyDetail")
    public ModelAndView meetingRoomApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/meetingRoomApplyDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/meetingArrange")
    public ModelAndView meetingArrange(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/meetingArrange");
        return modelAndView;
    }

    @RequestMapping("/oa/meetingArrangeDetail")
    public ModelAndView meetingArrangeDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/meetingArrangeDetail");
        return modelAndView;
    }
    @RequestMapping("/oa/fileSynergy")
    public ModelAndView fileSynergy(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/fileSynergy");
        return modelAndView;
    }

    @RequestMapping("/oa/fileSynergyDetail")
    public ModelAndView fileSynergyDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/fileSynergyDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeEntryExamine")
    public ModelAndView employeeEntryExamine(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeEntryExamine");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeEntryExamineDetail")
    public ModelAndView employeeEntryExamineDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeEntryExamineDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeTransferExamine")
    public ModelAndView employeeTransferExamine(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeTransferExamine");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeTransferExamineDetail")
    public ModelAndView employeeTransferExamineDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeTransferExamineDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeDimissionExamine")
    public ModelAndView employeeDimissionExamine(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeDimissionExamine");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeDimissionExamineDetail")
    public ModelAndView employeeDimissionExamineDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeDimissionExamineDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeRetireExamine")
    public ModelAndView employeeRetireExamine(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeRetireExamine");
        return modelAndView;
    }

    @RequestMapping("/oa/employeeRetireExamineDetail")
    public ModelAndView employeeRetireExamineDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaEmployeeRetireExamineDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/processDevelopApply")
    public ModelAndView processDevelopApply(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaProcessDevelopApply");
        return modelAndView;
    }

    @RequestMapping("/oa/processDevelopApplyDetail")
    public ModelAndView processDevelopApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaProcessDevelopApplyDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/decisionMakingList")
    public ModelAndView decisionMakingList(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/decisionMakingList");
        return modelAndView;
    }
    @RequestMapping("/oa/decisionMaking")
    public ModelAndView decisionMaking(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/decisionMaking");
        return modelAndView;
    }

    @RequestMapping("/oa/decisionMakingDetail")
    public ModelAndView decisionMakingDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/decisionMakingDetail");
        return modelAndView;
    }
    @RequestMapping("/asset/cardChange")
    public ModelAndView cardChange(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/cardChange");
        return modelAndView;
    }

    @RequestMapping("/asset/cardChangeDetail")
    public ModelAndView cardChangeDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/cardChangeDetail");
        return modelAndView;
    }
    @RequestMapping("/asset/furniturePurchase")
    public ModelAndView furniturePurchase(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/furniturePurchase");
        return modelAndView;
    }

    @RequestMapping("/asset/furniturePurchaseDetail")
    public ModelAndView furniturePurchaseDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/furniturePurchaseDetail");
        return modelAndView;
    }
    @RequestMapping("/asset/equipmentAcceptance")
    public ModelAndView equipmentAcceptance(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/equipmentAcceptance");
        return modelAndView;
    }

    @RequestMapping("/asset/equipmentAcceptanceDetail")
    public ModelAndView equipmentAcceptanceDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/equipmentAcceptanceDetail");
        return modelAndView;
    }
    @RequestMapping("/asset/assetComputer")
    public ModelAndView assetComputer(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/assetComputer");
        return modelAndView;
    }

    @RequestMapping("/asset/assetComputerDetail")
    public ModelAndView assetComputerDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/assetComputerDetail");
        return modelAndView;
    }
    @RequestMapping("/asset/trackAssetComputer")
    public ModelAndView trackAssetComputer(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/trackAssetComputer");
        return modelAndView;
    }
    @RequestMapping("/asset/trackAssetComputerDetail")
    public ModelAndView trackAssetComputerDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/trackAssetComputerDetail");
        return modelAndView;
    }

    @RequestMapping("/asset/assetAllot")
    public ModelAndView assetAllot(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/assetAllot");
        return modelAndView;
    }

    @RequestMapping("/asset/assetAllotDetail")
    public ModelAndView assetAllotDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/assetAllotDetail");
        return modelAndView;
    }

    @RequestMapping("/asset/assetScrap")
    public ModelAndView assetScrap(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/assetScrap");
        return modelAndView;
    }

    @RequestMapping("/asset/assetScrapDetail")
    public ModelAndView assetScrapDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/assetScrapDetail");
        return modelAndView;
    }

    @RequestMapping("/asset/computerScrap")
    public ModelAndView computerScrap(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/computerScrap");
        return modelAndView;
    }

    @RequestMapping("/asset/computerScrapDetail")
    public ModelAndView computerScrapDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/asset/computerScrapDetail");
        return modelAndView;
    }

    @RequestMapping("/technology/returnVisit")
    public ModelAndView technologyReturnVisit(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/returnVisit");
        return modelAndView;
    }
    @RequestMapping("/technology/returnVisitDetail")
    public ModelAndView technologyReturnVisitDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/returnVisitDetail");
        return modelAndView;
    }
    @RequestMapping("/technology/qualityCheck")
    public ModelAndView technologyQualityCheck(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/qualityCheck");
        return modelAndView;
    }
    @RequestMapping("/technology/qualityCheckDetail")
    public ModelAndView technologyQualityCheckDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/qualityCheckDetail");
        return modelAndView;
    }
    @RequestMapping("/technology/qualityAnalysis")
    public ModelAndView technologyQualityAnalysis(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/qualityAnalysis");
        return modelAndView;
    }
    @RequestMapping("/technology/qualityAnalysisDetail")
    public ModelAndView technologyQualityAnalysisDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/qualityAnalysisDetail");
        return modelAndView;
    }
    @RequestMapping("/technology/qualityIssue")
    public ModelAndView technologyQualityIssue(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/qualityIssue");
        return modelAndView;
    }
    @RequestMapping("/technology/qualityIssueDetail")
    public ModelAndView technologyQualityIssueDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/technology/qualityIssueDetail");
        return modelAndView;
    }
    @RequestMapping("/oa/oaPrivilegeManagement")
    public ModelAndView privilegeManagement() {
        return new ModelAndView("/five/oa/oaPrivilegeManagement");
    }

    @RequestMapping("/oa/oaPrivilegeManagementDetail")
    public ModelAndView privilegeManagementDetail(){
        return new ModelAndView("/five/oa/oaPrivilegeManagementDetail");
    }

    @RequestMapping("/partial-co")
    public ModelAndView co(){
        return new ModelAndView("/five/partial-co");
    }

    @RequestMapping("/oa/informationPlan")
    public ModelAndView informationPlan(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/informationPlan");
        return modelAndView;
    }
    @RequestMapping("/oa/informationPlanDetail")
    public ModelAndView informationPlanDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/informationPlanDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/inlandProjectApply")
    public ModelAndView inlandProjectApply(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaInlandProjectApply");
        return modelAndView;
    }

    @RequestMapping("/oa/inlandProjectApplyDetail")
    public ModelAndView inlandProjectApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaInlandProjectApplyDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/inlandProjectReview")
    public ModelAndView inlandProjectReview(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaInlandProjectReview");
        return modelAndView;
    }

    @RequestMapping("/oa/inlandProjectReviewDetail")
    public ModelAndView inlandProjectReviewDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaInlandProjectReviewDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/oaResearchProjectReview")
    public ModelAndView researchProjectReview(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaResearchProjectReview");
        return modelAndView;
    }
    @RequestMapping("/oa/oaResearchProjectReviewDetail")
    public ModelAndView researchProjectReviewDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/oaResearchProjectReviewDetail");
        return modelAndView;
    }

    @RequestMapping("/oa/oaResearchProjectLibrary")
    public ModelAndView researchProjectLibrary(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/researchProjectLibrary");
        return modelAndView;
    }
    @RequestMapping("/oa/oaResearchProjectLibraryDetail")
    public ModelAndView researchProjectLibraryDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/researchProjectLibraryDetail");
        return modelAndView;
    }
    @RequestMapping("/oa/stampApplyOffice")
    public ModelAndView stampApplyOffice(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/stampApplyOffice");
        return modelAndView;
    }
    @RequestMapping("/oa/stampApplyOfficeDetail")
    public ModelAndView stampApplyOfficeDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/oa/stampApplyOfficeDetail");
        return modelAndView;
    }


    @RequestMapping("/wuzhou/file")
    public ModelAndView contentFile(){
        ModelAndView modelAndView=new ModelAndView("/sys/contentFile");
        return modelAndView;
    }


}
