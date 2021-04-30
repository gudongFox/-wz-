package com.cmcu.mcc.business.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/business")
public class BusinessController {


    @RequestMapping("/customer")
    public ModelAndView customer(){
        return new ModelAndView("/business/customer");
    }

    @RequestMapping("/customerDetail")
    public ModelAndView customerDetail(){
        return new ModelAndView("business/customerDetail");
    }


    @RequestMapping("/deptContract")
    public ModelAndView deptContract(){
        return new ModelAndView("/business/deptContract");
    }

    @RequestMapping("/bidApply")
    public ModelAndView bidApply(){
        return new ModelAndView("/business/bidApply");
    }

    @RequestMapping("/bidApplyDetail")
    public ModelAndView bidApplyDetail(){
        return new ModelAndView("/business/bidApplyDetail");
    }

    @RequestMapping("/bidAttend")
    public ModelAndView bidAttend(){
        return new ModelAndView("/business/bidAttend");
    }

    @RequestMapping("/bidAttendDetail")
    public ModelAndView bidAttendDetail(){
        return new ModelAndView("/business/bidAttendDetail");
    }

    @RequestMapping("/bidProjectCharge")
    public ModelAndView bidProjectCharge(){
        return new ModelAndView("/business/bidProjectCharge");
    }

    @RequestMapping("/bidProjectChargeDetail")
    public ModelAndView bidProjectChargeDetail(){
        return new ModelAndView("/business/bidProjectChargeDetail");
    }

    @RequestMapping("/bidContract")
    public ModelAndView bidContract(){
        return new ModelAndView("/business/bidContract");
    }

    @RequestMapping("/bidContractDetail")
    public ModelAndView bidContractDetail(){
        return new ModelAndView("/business/bidContractDetail");
    }

    @RequestMapping("/record")
    public ModelAndView record(){
        return new ModelAndView("/business/record");
    }

    @RequestMapping("/recordDetail")
    public ModelAndView recordDetail(){
        return new ModelAndView("/business/recordDetail");
    }


    @RequestMapping("/contractReview")
    public ModelAndView contractReview(){
        return new ModelAndView("five/business/contractReview");
    }

    @RequestMapping("/contractReviewDetail")
    public ModelAndView contractReviewDetail(){
        return new ModelAndView("five/business/contractReviewDetail");
    }

    @RequestMapping("/contractLibrary")
    public ModelAndView contractLibrary(){
        return new ModelAndView("five/business/contractLibrary");
    }

    @RequestMapping("/contractLibraryDetail")
    public ModelAndView contractLibraryDetail(){
        return new ModelAndView("five/business/contractLibraryDetail");
    }

    @RequestMapping("/contractLibrarySubpackage")
    public ModelAndView contractLibrarySubpackage(){
        return new ModelAndView("five/business/contractLibrarySubpackage");
    }

    @RequestMapping("/contractLibrarySubpackageDetail")
    public ModelAndView contractLibrarySubpackageDetail(){
        return new ModelAndView("five/business/contractLibrarySubpackageDetail");
    }

    @RequestMapping("/contract")
    public ModelAndView contract(){
        return new ModelAndView("five/business/contract");
    }

    @RequestMapping("/contractDetail")
    public ModelAndView contractDetail(){
        return new ModelAndView("five/business/contractDetail");
    }

    @RequestMapping("/supplier")
    public ModelAndView supplier(){
        return new ModelAndView("five/business/supplier");
    }

    @RequestMapping("/supplierDetail")
    public ModelAndView supplierDetail(){
        return new ModelAndView("five/business/supplierDetail");
    }

    @RequestMapping("/subpackage")
    public ModelAndView subpackage(){
        return new ModelAndView("five/business/subpackage");
    }

    @RequestMapping("/subpackageDetail")
    public ModelAndView subpackageDetail(){
        return new ModelAndView("five/business/subpackageDetail");
    }

    @RequestMapping("/income")
    public ModelAndView income(){
        return new ModelAndView("five/business/income");
    }

    @RequestMapping("/incomeDetail")
    public ModelAndView incomeDetail(){
        return new ModelAndView("five/business/incomeDetail");
    }

    @RequestMapping("/purchase")
    public ModelAndView purchase(){
        return new ModelAndView("five/business/purchase");
    }

    @RequestMapping("/purchaseDetail")
    public ModelAndView purchaseDetail(){
        return new ModelAndView("five/business/purchaseDetail");
    }

    @RequestMapping("/outAssist")
    public ModelAndView outAssist(){
        return new ModelAndView("five/business/outAssist");
    }

    @RequestMapping("/outAssistDetail")
    public ModelAndView outAssistDetail(){
        return new ModelAndView("five/business/outAssistDetail");
    }

    @RequestMapping("/cooperationContract")
    public ModelAndView cooperationContract(){
        return new ModelAndView("five/business/cooperationContract");
    }
    @RequestMapping("/cooperationContractDetail")
    public ModelAndView cooperationContractDetail(){
        return new ModelAndView("five/business/cooperationContractDetail");
    }

    @RequestMapping("/cooperationDelegation")
    public ModelAndView cooperationDelegation(){
        return new ModelAndView("five/business/cooperationDelegation");
    }
    @RequestMapping("/cooperationDelegationDetail")
    public ModelAndView cooperationDelegationDetail(){
        return new ModelAndView("five/business/cooperationDelegationDetail");
    }

    @RequestMapping("/advance")
    public ModelAndView advance(){
        ModelAndView modelAndView=new ModelAndView("/five/business/advance");
        return modelAndView;
    }
    @RequestMapping("/advanceDetail")
    public ModelAndView advanceDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/business/advanceDetail");
        return modelAndView;
    }

    @RequestMapping("/advanceCollect")
    public ModelAndView advanceCollect(){
        ModelAndView modelAndView=new ModelAndView("/five/business/advanceCollect");
        return modelAndView;
    }
    @RequestMapping("/advanceCollectDetail")
    public ModelAndView advanceCollectDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/business/advanceCollectDetail");
        return modelAndView;
    }

    @RequestMapping("/tenderDocumentReview")
    public ModelAndView tenderDocumentReview(){
        ModelAndView modelAndView=new ModelAndView("/five/business/tenderDocumentReview");
        return modelAndView;
    }
    @RequestMapping("/tenderDocumentReviewDetail")
    public ModelAndView tenderDocumentReviewDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/business/tenderDocumentReviewDetail");
        return modelAndView;
    }

    @RequestMapping("/statistics")
    public ModelAndView statistics(){
        ModelAndView modelAndView=new ModelAndView("/five/business/statistics");
        return modelAndView;
    }
    @RequestMapping("/statisticsDetail")
    public ModelAndView statisticsDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/business/statisticsDetail");
        return modelAndView;
    }

}

