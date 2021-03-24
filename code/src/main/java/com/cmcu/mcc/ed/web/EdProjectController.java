package com.cmcu.mcc.ed.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ed/project")
public class EdProjectController {



    @RequestMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("/ed/project/index");
    }

    @RequestMapping("/detail")
    public ModelAndView detail(){
        return new ModelAndView("/ed/project/detail");
    }

    @RequestMapping("/partial-show")
    public ModelAndView partialShow(){
        return new ModelAndView("/ed/project/partial-show");
    }

    @RequestMapping("/partial-stepShow")
    public ModelAndView partialStepShow(){
        return new ModelAndView("/ed/project/partial-stepShow");
    }

    @RequestMapping("/partial-step")
    public ModelAndView partialStep(){
        return new ModelAndView("/ed/project/partial-step");
    }

    @RequestMapping("/partial-stepDetail")
    public ModelAndView partialStepDetail(){
        return new ModelAndView("/ed/project/partial-stepDetail");
    }


    @RequestMapping("/partial-task")
    public ModelAndView partialTask(){
        return new ModelAndView("/ed/project/partial-task");
    }

    @RequestMapping("/partial-taskDetail")
    public ModelAndView partialTaskDetail(){
        return new ModelAndView("/ed/project/partial-taskDetail");
    }

    @RequestMapping("/partial-arrange")
    public ModelAndView partialArrange(){
        return new ModelAndView("/ed/project/partial-arrange");
    }

    @RequestMapping("/partial-arrangeDetail")
    public ModelAndView partialArrangeDetail(){
        return new ModelAndView("/ed/project/partial-arrangeDetail");
    }

    @RequestMapping("/partial-houseService")
    public ModelAndView partialHouseService(){
        return new ModelAndView("/ed/project/partial-houseService");
    }

    @RequestMapping("/partial-houseServiceDetail")
    public ModelAndView partialServiceDetail(){
        return new ModelAndView("/ed/project/partial-houseServiceDetail");
    }

    @RequestMapping("/partial-houseChange")
    public ModelAndView partialHouseChange(){
        return new ModelAndView("/ed/project/partial-houseChange");
    }

    @RequestMapping("/partial-houseChangeDetail")
    public ModelAndView partialHouseChangeDetail(){
        return new ModelAndView("/ed/project/partial-houseChangeDetail");
    }

    @RequestMapping("/partial-letter")
    public ModelAndView partialLetter(){
        return new ModelAndView("/ed/project/partial-letter");
    }

    @RequestMapping("/partial-letterDetail")
    public ModelAndView partialLetterDetail(){
        return new ModelAndView("/ed/project/partial-letterDetail");
    }

    @RequestMapping("/partial-houseReturnVisit")
    public ModelAndView partialHouseReturnVisit(){
        return new ModelAndView("/ed/project/partial-houseReturnVisit");
    }

    @RequestMapping("/partial-houseReturnVisitDetail")
    public ModelAndView partialHouseReturnVisitDetail(){
        return new ModelAndView("/ed/project/partial-houseReturnVisitDetail");
    }

    @RequestMapping("/partial-houseRefer")
    public ModelAndView partialHouseRefer(){
        return new ModelAndView("/ed/project/partial-houseRefer");
    }

    @RequestMapping("/partial-houseReferDetail")
    public ModelAndView partialHouseReferDetail(){
        return new ModelAndView("/ed/project/partial-houseReferDetail");
    }


    @RequestMapping("/partial-houseException")
    public ModelAndView partialHouseException(){
        return new ModelAndView("/ed/project/partial-houseException");
    }

    @RequestMapping("/partial-houseExceptionDetail")
    public ModelAndView partialHouseExceptionDetail(){
        return new ModelAndView("/ed/project/partial-houseExceptionDetail");
    }


    @RequestMapping("/partial-product")
    public ModelAndView partialProduct(){
        return new ModelAndView("/ed/project/partial-product");
    }

    @RequestMapping("/partial-productDetail")
    public ModelAndView partialProductDetail(){
        return new ModelAndView("/ed/project/partial-productDetail");
    }

    @RequestMapping("/partial-houseReview")
    public ModelAndView partialReview(){
        return new ModelAndView("ed/project/partial-houseReview");
    }

    @RequestMapping("/partial-houseReviewDetail")
    public ModelAndView partialHouseReviewDetail(){
        return new ModelAndView("ed/project/partial-houseReviewDetail");
    }


    @RequestMapping("/partial-houseValidate")
    public ModelAndView partialHouseValidate(){
        return new ModelAndView("ed/project/partial-houseValidate");
    }

    @RequestMapping("/partial-houseValidateDetail")
    public ModelAndView partialHouseValidateDetail(){
        return new ModelAndView("ed/project/partial-houseValidateDetail");
    }

    @RequestMapping("/partial-houseArchive")
    public ModelAndView partialHouseArchive(){
        return new ModelAndView("ed/project/partial-houseArchive");
    }

    @RequestMapping("/partial-houseArchiveDetail")
    public ModelAndView partialHouseArchiveDetail(){
        return new ModelAndView("ed/project/partial-houseArchiveDetail");
    }


    @RequestMapping("/partial-input")
    public ModelAndView partialInput(){
        return new ModelAndView("ed/project/partial-input");
    }

    @RequestMapping("/partial-inputAc")
    public ModelAndView partialInputAc(){
        return new ModelAndView("ed/project/partial-inputAc");
    }

    @RequestMapping("/partial-inputAcDetail")
    public ModelAndView partialInputAcDetail(){
        return new ModelAndView("ed/project/partial-inputAcDetail");
    }


    @RequestMapping("/partial-inputDe")
    public ModelAndView partialInputDe(){
        return new ModelAndView("ed/project/partial-inputDe");
    }

    @RequestMapping("/partial-inputDeDetail")
    public ModelAndView partialInputDeDetail(){
        return new ModelAndView("ed/project/partial-inputDeDetail");
    }
    @RequestMapping("/partial-inputHvac")
    public ModelAndView partialInputHvac(){
        return new ModelAndView("ed/project/partial-inputHvac");
    }

    @RequestMapping("/partial-inputHvacDetail")
    public ModelAndView partialInputHvacDetail(){
        return new ModelAndView("ed/project/partial-inputHvacDetail");
    }
    @RequestMapping("/partial-inputWater")
    public ModelAndView partialInputWater(){
        return new ModelAndView("ed/project/partial-inputWater");
    }

    @RequestMapping("/partial-inputWaterDetail")
    public ModelAndView partialInputWaterDetail(){
        return new ModelAndView("ed/project/partial-inputWaterDetail");
    }


    @RequestMapping("/partial-inputGs")
    public ModelAndView partialInputGs(){
        return new ModelAndView("ed/project/partial-inputGs");
    }
    @RequestMapping("/partial-inputGsDetail")
    public ModelAndView partialInputGsDetail(){
        return new ModelAndView("ed/project/partial-inputGsDetail");
    }


    @RequestMapping("/partial-inputRoad")
    public ModelAndView partialInputRoad(){
        return new ModelAndView("ed/project/partial-inputRoad");
    }

    @RequestMapping("/partial-inputRoadDetail")
    public ModelAndView partialInputRoadDetail(){
        return new ModelAndView("ed/project/partial-inputRoadDetail");
    }

    @RequestMapping("/partial-inputApprove")
    public ModelAndView partialInputApprove(){
        return new ModelAndView("ed/project/partial-inputApprove");
    }

    @RequestMapping("/partial-inputApproveDetail")
    public ModelAndView partialInputApproveDetail(){
        return new ModelAndView("ed/project/partial-inputApproveDetail");
    }

    @RequestMapping("/partial-inputReview")
    public ModelAndView partialInputReview(){
        return new ModelAndView("ed/project/partial-inputReview");
    }

    @RequestMapping("/partial-inputReviewDetail")
    public ModelAndView partialInputReviewDetail(){
        return new ModelAndView("ed/project/partial-inputReviewDetail");
    }



    @RequestMapping("/partial-inputConstruction")
    public ModelAndView partialInputConstruction(){
        return new ModelAndView("ed/project/partial-inputConstruction");
    }

    @RequestMapping("/partial-inputConstructionDetail")
    public ModelAndView partialInputConstructionDetail(){
        return new ModelAndView("ed/project/partial-inputConstructionDetail");
    }

    @RequestMapping("/partial-inputHouseDesign")
    public ModelAndView partialInputHouseDesign(){
        return new ModelAndView("ed/project/partial-inputHouseDesign");
    }

    @RequestMapping("/partial-inputHouseDesignDetail")
    public ModelAndView partialInputHouseDesignDetail(){
        return new ModelAndView("ed/project/partial-inputHouseDesignDetail");
    }


    @RequestMapping("/partial-inputPlan")
    public ModelAndView partialInputPlan(){
        return new ModelAndView("ed/project/partial-inputPlan");
    }

    @RequestMapping("/partial-inputPlanDetail")
    public ModelAndView partialInputPlanDetail(){
        return new ModelAndView("ed/project/partial-inputPlanDetail");
    }


    @RequestMapping("/partial-cp")
    public ModelAndView partialCp(){
        return new ModelAndView("ed/project/partial-cp");
    }

    @RequestMapping("/partial-validateBatch")
    public ModelAndView validateBatch(){
        return new ModelAndView("ed/project/partial-validateBatch");
    }

    @RequestMapping("/partial-validateBatchDetail")
    public ModelAndView validateBatchDetail(){
        return new ModelAndView("ed/project/partial-validateBatchDetail");
    }

    @RequestMapping("/partial-stampOther")
    public ModelAndView stampOther(){
        return new ModelAndView("ed/project/partial-stampOther");
    }

    @RequestMapping("/partial-stampOtherDetail")
    public ModelAndView stampOtherDetail(){
        return new ModelAndView("ed/project/partial-stampOtherDetail");
    }

    @RequestMapping("/partial-stampEd")
    public ModelAndView stampEd(){
        return new ModelAndView("ed/project/partial-stampEd");
    }

    @RequestMapping("/partial-stampEdDetail")
    public ModelAndView stampEdDetail(){
        return new ModelAndView("ed/project/partial-stampEdDetail");
    }

    @RequestMapping("/partial-stampChangeAndProcess")
    public ModelAndView stampChangeAndProcess(){
        return new ModelAndView("ed/project/partial-stampChangeAndProcess");
    }

    @RequestMapping("/partial-stampChangeAndProcessDetail")
    public ModelAndView stampChangeAndProcessDetail(){
        return new ModelAndView("ed/project/partial-stampChangeAndProcessDetail");
    }

    @RequestMapping("/partial-fileCheck")
    public ModelAndView fileCheck(){
        return new ModelAndView("ed/project/partial-fileCheck");
    }

    @RequestMapping("/partial-fileCheckDetail")
    public ModelAndView fileCheckDetail(){
        return new ModelAndView("ed/project/partial-fileCheckDetail");
    }


}
