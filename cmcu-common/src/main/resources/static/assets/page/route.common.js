function  initCommonRoute($stateProvider,version) {

    $stateProvider
        .state('common', {
            url: "/common",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('common.attach', {
            url: "/attach",
            templateUrl: function () {
                if (version) {
                    return "/web/v1/tpl/attach.html";
                } else {
                    return "/web/attach.html";
                }
            },
            controller: 'CommonAttachController as vm'
        })
        .state('common.code', {
            url: "/code",
            templateUrl: function () {
                if (version) {
                    return "/web/v1/tpl/code.html";
                } else {
                    return "/web/code.html";
                }
            },
            controller: 'CommonCodeController as vm'
        })
        .state('common.edQuestion', {
            url: "/edQuestion",
            templateUrl: function () {
                if (version) {
                    return "/web/v1/tpl/commonEdQuestion.html";
                } else {
                    return "/web/tpl/commonEdQuestion.html";
                }
            },
            controller: 'CommonEdQuestionController as vm'
        })
        .state('common.designReview', {
            url: "/designReview",
            templateUrl: function () {
                return "/common/designReview";
            },
            controller: 'CommonDesignReviewController as vm'
        })
        .state('common.black', {
            url: "/black",
            templateUrl: function () {
                return "/web/v1/tpl/black.html";
            },
            controller: 'CommonBlackController as vm'
        })
        .state('common.request', {
            url: "/request",
            templateUrl: function () {
                return "/web/v1/tpl/request.html";
            },
            controller: 'CommonRequestController'
        })
        .state('common.export', {
            url: "/export",
            templateUrl: function () {
                return "/web/v1/tpl/export.html";
            },
            controller: 'CommonExportController'
        })
        .state('common.edDwgPicture', {
            url: "/edDwgPicture",
            templateUrl: function () {
                return "/web/v1/tpl/commonEdDwgPicture.html";
            },
            controller: 'CommonEdDwgPictureController'
        })
        .state('common.edDwgStd', {
            url: "/edDwgStd",
            templateUrl: function () {
                return "/web/v1/tpl/commonEdDwgStd.html";
            },
            controller: 'CommonEdDwgStdController'
        })
        .state('common.edStamp', {
            url: "/edStamp",
            templateUrl: function () {
                return "/web/v1/tpl/commonEdStamp.html";
            },
            controller: 'CommonEdStampController'
        })
        .state('common.edLayerExtraction', {
            url: "/edLayerExtraction",
            templateUrl: function () {
                return "/web/v1/tpl/commonEdLayerExtraction.html";
            },
            controller: 'CommonEdLayerExtractionController'
        })
        .state('common.cadPlugin', {
            url: "/cadPlugin",
            templateUrl: function () {
                return "/web/v1/tpl/commonCadPlugin.html";
            },
            controller: 'CommonCadPluginController'
        })
        .state('common.wxMessage', {
            url: "/wxMessage",
            templateUrl: function () {
                return "/web/v1/tpl/commonWxMessage.html";
            },
            controller: 'CommonWxMessageController'
        })
        .state('common.form', {
            url: "/form",
            templateUrl: function () {
                return "/web/v1/tpl/commonForm.html";
            },
            controller: 'CommonFormController'
        })
        .state('common.formDetail', {
            url: "/formDetail?formId",
            templateUrl: function () {
                return "/web/v1/tpl/commonFormDetail.html";
            },
            controller: 'CommonFormDetailController'
        })
       ;


}

function initFormRoute($stateProvider,version) {
    $stateProvider
        .state('formData', {
            url: "/formData",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('formData.list', {
            url: "/list?referType&referId",
            templateUrl: function () {
                if(version) return "/web/"+version+"/formData/list.html";
                return "/web/formData/list.html";
            },
            controller: 'CommonFormListController as vm'
        })
        .state('formData.pagedList', {
            url: "/pagedList?referType",
            templateUrl: function () {
                if(version) return "/web/"+version+"/formData/pagedList.html";
                return "/web/formData/pagedList.html";
            },
            controller: 'CommonFormPagedListController as vm'
        })
        .state('formData.detail', {
            url: "/detail?formDataId",
            templateUrl: function () {
                if(version) return "/web/"+version+"/formData/detail.html";
                return "/web/formData/detail.html";
            },
            controller: 'CommonFormDataDetailController as vm'
        })
        .state('formData.hrTrainDetail', {
            url: "/hrTrainDetail?formDataId",
            templateUrl: function () {
                 return "/web/v1/formData/hrTrainDetail.html";
            },
            controller: 'CommonHrTrainDetailController as vm'
        })

    ;
}

