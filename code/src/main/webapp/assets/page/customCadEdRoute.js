function initCustomCadEdRoute($stateProvider) {

    $stateProvider
        .state('five', {
            url: "/five",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('five.detail', {
            url: "/detail?id",
            templateUrl: function () {
                return "/five/detail";
            },
            controller: 'FiveDetailController as vm'
        })
        .state('five.detail.show', {
            url: "/show?nodeId",
            templateUrl: function () {
                return "/five/partial-show";
            },
            controller: 'FiveDetailPartialShowController as vm'
        })
        .state('five.detail.stepShow', {
            url: "/stepShow?nodeId",
            templateUrl: function () {
                return "/five/partial-stepShow";
            },
            controller: 'FiveDetailPartialStepShowController as vm'
        })
        .state('five.detail.cp', {
            url: "/cp?stepId",
            templateUrl: function () {
                return "/ed/project/partial-cp";
            },
            controller: 'CoDetailController as vm'
        })
        .state('five.detail.step', {
            url: "/step?nodeId",
            templateUrl: function () {
                return "/five/partial-step";
            },
            controller: 'FiveDetailPartialStepController as vm'
        })
        .state('five.detail.stepDetail', {
            url: "/stepDetail?stepId",
            templateUrl: function () {
                return "/five/partial-stepDetail";
            },
            controller: 'FiveDetailPartialStepDetailController as vm'
        })
        .state('five.detail.task', {
            url: "/task?nodeId",
            templateUrl: function () {
                return "/five/partial-task";
            },
            controller: 'FiveEdTaskController as vm'
        })
        .state('five.detail.taskDetail', {
            url: "/taskDetail?taskId",
            templateUrl: function () {
                return "/five/partial-taskDetail";
            },
            controller: 'FiveEdTaskDetailController as vm'
        })
        .state('five.detail.arrange', {
            url: "/arrange?nodeId",
            templateUrl: function () {
                return "/five/partial-arrange";
            },
            controller: 'FiveEdArrangeController as vm'
        })
        .state('five.detail.arrangeDetail', {
            url: "/arrangeDetail?arrangeId",
            templateUrl: function () {
                return "/five/partial-arrangeDetail";
            },
            controller: 'FiveEdArrangeDetailController as vm'
        })
        .state('five.detail.designRule', {
            url: "/designRule?nodeId",
            templateUrl: function () {
                return "/five/partial-designRule";
            },
            controller: 'FiveEdDesignRuleController as vm'
        })
        .state('five.detail.designRuleDetail', {
            url: "/designRuleDetail?designRuleId",
            templateUrl: function () {
                return "/five/partial-designRuleDetail";
            },
            controller: 'FiveEdDesignRuleDetailController as vm'
        })
        .state('five.detail.houseRefer', {
            url: "/houseRefer?nodeId",
            templateUrl: function () {
                return "/five/partial-houseRefer";
            },
            controller: 'FiveEdHouseReferController as vm'
        })
        .state('five.detail.houseReferDetail', {
            url: "/houseReferDetail?houseReferId",
            templateUrl: function () {
                return "/five/partial-houseReferDetail";
            },
            controller: 'FiveEdHouseReferDetailController as vm'
        })
        .state('five.detail.reviewApply', {
            url: "/reviewApply?nodeId",
            templateUrl: function () {
                return "/five/partial-reviewApply";
            },
            controller: 'FiveEdReviewApplyController as vm'
        })
        .state('five.detail.reviewApplyDetail', {
            url: "/reviewApplyDetail?reviewApplyId",
            templateUrl: function () {
                return "/five/partial-reviewApplyDetail";
            },
            controller: 'FiveEdReviewApplyDetailController as vm'
        })
        .state('five.detail.reviewMajor', {
            url: "/reviewMajor?nodeId",
            templateUrl: function () {
                return "/five/partial-reviewMajor";
            },
            controller: 'FiveEdReviewMajorController as vm'
        })
        .state('five.detail.reviewMajorDetail', {
            url: "/reviewMajorDetail?reviewMajorId",
            templateUrl: function () {
                return "/five/partial-reviewMajorDetail";
            },
            controller: 'FiveEdReviewMajorDetailController as vm'
        })
        .state('five.detail.reviewPlan', {
            url: "/reviewPlan?nodeId",
            templateUrl: function () {
                return "/five/partial-reviewPlan";
            },
            controller: 'FiveEdReviewPlanController as vm'
        })
        .state('five.detail.reviewPlanDetail', {
            url: "/reviewPlanDetail?reviewPlanId",
            templateUrl: function () {
                return "/five/partial-reviewPlanDetail";
            },
            controller: 'FiveEdReviewPlanDetailController as vm'
        })
        .state('five.detail.reviewSpecial', {
            url: "/reviewSpecial?nodeId",
            templateUrl: function () {
                return "/five/partial-reviewSpecial";
            },
            controller: 'FiveEdReviewSpecialController as vm'
        })
        .state('five.detail.reviewSpecialDetail', {
            url: "/reviewSpecialDetail?reviewSpecialId",
            templateUrl: function () {
                return "/five/partial-reviewSpecialDetail";
            },
            controller: 'FiveEdReviewSpecialDetailController as vm'
        })
        .state('five.detail.expertReview', {
            url: "/expertReview?nodeId",
            templateUrl: function () {
                return "/five/partial-expertReview";
            },
            controller: 'FiveEdExpertReviewController as vm'
        })
        .state('five.detail.expertReviewDetail', {
            url: "/expertReviewDetail?expertReviewId",
            templateUrl: function () {
                return "/five/partial-expertReviewDetail";
            },
            controller: 'FiveEdExpertReviewDetailController as vm'
        })
        .state('five.detail.outReview', {
            url: "/outReview?nodeId",
            templateUrl: function () {
                return "/five/partial-outReview";
            },
            controller: 'FiveEdOutReviewController as vm'
        })
        .state('five.detail.outReviewDetail', {
            url: "/outReviewDetail?outReviewId",
            templateUrl: function () {
                return "/five/partial-outReviewDetail";
            },
            controller: 'FiveEdOutReviewDetailController as vm'
        })
        .state('five.detail.houseValidate', {
            url: "/houseValidate?nodeId",
            templateUrl: function () {
                return "/five/partial-houseValidate";
            },
            controller: 'FiveEdHouseValidateController as vm'
        })
        .state('five.detail.houseValidateDetail', {
            url: "/houseValidateDetail?validateId",
            templateUrl: function () {
                return "/five/partial-houseValidateDetail";
            },
            controller: 'FiveEdHouseValidateDetailController as vm'
        })
        .state('five.detail.serviceChange', {
            url: "/serviceChange?nodeId",
            templateUrl: function () {
                return "/five/partial-serviceChange";
            },
            controller: 'FiveEdServiceChangeController as vm'
        })
        .state('five.detail.serviceChangeDetail', {
            url: "/serviceChangeDetail?serviceChangeId",
            templateUrl: function () {
                return "/five/partial-serviceChangeDetail";
            },
            controller: 'FiveEdServiceChangeDetailController as vm'
        })
        .state('five.detail.countersign', {
            url: "/countersign?nodeId",
            templateUrl: function () {
                return "/five/partial-countersign";
            },
            controller: 'FiveEdCountersignController as vm'
        })
        .state('five.detail.countersignDetail', {
            url: "/countersignDetail?countersignId",
            templateUrl: function () {
                return "/five/partial-countersignDetail";
            },
            controller: 'FiveEdCountersignDetailController as vm'
        })
        .state('five.detail.serviceDiscuss', {
            url: "/serviceDiscuss?nodeId",
            templateUrl: function () {
                return "/five/partial-serviceDiscuss";
            },
            controller: 'FiveEdServiceDiscussController as vm'
        })
        .state('five.detail.serviceDiscussDetail', {
            url: "/serviceDiscussDetail?serviceDiscussId",
            templateUrl: function () {
                return "/five/partial-serviceDiscussDetail";
            },
            controller: 'FiveEdServiceDiscussDetailController as vm'
        })
        .state('five.detail.serviceHandle', {
            url: "/serviceHandle?nodeId",
            templateUrl: function () {
                return "/five/partial-serviceHandle";
            },
            controller: 'FiveEdServiceHandleController as vm'
        })
        .state('five.detail.serviceHandleDetail', {
            url: "/serviceHandleDetail?serviceHandleId",
            templateUrl: function () {
                return "/five/partial-serviceHandleDetail";
            },
            controller: 'FiveEdServiceHandleDetailController as vm'
        })
        .state('five.detail.qualityIssue', {
            url: "/qualityIssue?nodeId",
            templateUrl: function () {
                return "/five/partial-qualityIssue";
            },
            controller: 'FiveEdQualityIssueController as vm'
        })
        .state('five.detail.qualityIssueDetail', {
            url: "/qualityIssueDetail?qualityIssueId",
            templateUrl: function () {
                return "/five/partial-qualityIssueDetail";
            },
            controller: 'FiveEdQualityIssueDetailController as vm'
        })
        .state('five.detail.returnVisit', {
            url: "/returnVisit?nodeId",
            templateUrl: function () {
                return "/five/partial-returnVisit";
            },
            controller: 'FiveEdReturnVisitController as vm'
        })
        .state('five.detail.returnVisitDetail', {
            url: "/returnVisitDetail?returnVisitId",
            templateUrl: function () {
                return "/five/partial-returnVisitDetail";
            },
            controller: 'FiveEdReturnVisitDetailController as vm'
        })
        .state('five.detail.product', {
            url: "/product?nodeId",
            templateUrl: function () {
                return "/five/partial-product";
            },
            controller: 'FiveEdProductController as vm'
        })
        .state('five.detail.productDetail', {
            url: "/productDetail?productId",
            templateUrl: function () {
                return "/five/partial-productDetail";
            },
            controller: 'FiveEdProductDetailController as vm'
        })
        .state('five.detail.plotApply', {
            url: "/plotApply?nodeId",
            templateUrl: function () {
                return "/five/partial-plotApply";
            },
            controller: 'FiveEdPlotApplyController as vm'
        })
        .state('five.detail.plotApplyDetail', {
            url: "/plotApplyDetail?plotId",
            templateUrl: function () {
                return "/five/partial-plotApplyDetail";
            },
            controller: 'FiveEdPlotApplyDetailController as vm'
        })
        .state('five.detail.stamp', {
            url: "/stamp?nodeId",
            templateUrl: function () {
                return "/five/partial-stamp";
            },
            controller: 'FiveEdStampController as vm'
        })
        .state('five.detail.stampDetail', {
            url: "/stampDetail?stampId",
            templateUrl: function () {
                return "/five/partial-stampDetail";
            },
            controller: 'FiveEdStampDetailController as vm'
        })
        .state('five.detail.qualityCheck', {
            url: "/qualityCheck?nodeId",
            templateUrl: function () {
                return "/five/partial-qualityCheck";
            },
            controller: 'FiveEdQualityCheckController as vm'
        })
        .state('five.detail.qualityCheckDetail', {
            url: "/qualityCheckDetail?qualityCheckId",
            templateUrl: function () {
                return "/five/partial-qualityCheckDetail";
            },
            controller: 'FiveEdQualityCheckDetailController as vm'
        })
        .state('five.detail.qualityAnalysis', {
            url: "/qualityAnalysis?nodeId",
            templateUrl: function () {
                return "/five/partial-qualityAnalysis";
            },
            controller: 'FiveEdQualityAnalysisController as vm'
        })
        .state('five.detail.qualityAnalysisDetail', {
            url: "/qualityAnalysisDetail?analysisId",
            templateUrl: function () {
                return "/five/partial-qualityAnalysisDetail";
            },
            controller: 'FiveEdQualityAnalysisDetailController as vm'
        })

    ;
}