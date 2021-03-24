function initEdDetailRoute($stateProvider) {


    $stateProvider.state('five.edTaskDetail', {
        url: "/edTaskDetail?formDataId",
        templateUrl: function () {
            return "/five/partial-taskDetail";
        },
        controller: 'FiveEdTaskDetailController'
    })
        .state('five.edArrangeDetail', {
            url: "/edArrangeDetail?formDataId",
            templateUrl: function () {
                return "/five/partial-arrangeDetail";
            },
            controller: 'FiveEdArrangeDetailController'
        })

        .state('five.detail', {
        url: "/detail?id",
        templateUrl: function () {
            return "/five/detail";
        },
        controller: 'FiveDetailController as vm'
    })
        .state('five.detail.co', {
            url: "/co?stepId",
            templateUrl: function () {
                return "/five/partial-co";
            },
            controller: 'FiveEdCoController as vm'
        })
        .state('five.detail.form', {
            url: "/form?nodeId",
            templateUrl: function () {
                return "/web/v1/formData/edDetailForm.html";
            },
            controller: 'FiveEdFormController as vm'
        })
        .state('five.detail.formDetail', {
            url: "/formDetail?formDataId",
            templateUrl: function () {
                return "/web/v1/formData/edDetailFormDetail.html";
            },
            controller: 'FiveEdFormDetailController as vm'
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
        .state('five.detail.doc', {
            url: "/doc?nodeId",
            templateUrl: function () {
                return "/five/partial-doc";
            },
            controller: 'FiveEdDocController as vm'
        })
        .state('five.detail.task', {
            url: "/task?nodeId",
            templateUrl: function () {
                return "/five/partial-form";
            },
            controller: 'FiveEdTaskController as vm'
        })
        .state('five.detail.taskDetail', {
            url: "/taskDetail?formDataId",
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
            url: "/arrangeDetail?formDataId",
            templateUrl: function () {
                return "/five/partial-arrangeDetail";
            },
            controller: 'FiveEdArrangeDetailController as vm'
        })
        .state('five.detail.designDrawingCheck', {
            url: "/designDrawingCheck?nodeId",
            templateUrl: function () {
                return "/five/partial-designDrawingCheck";
            },
            controller: 'FiveEdDesignDrawingCheckController as vm'
        })
        .state('five.detail.designDrawingCheckDetail', {
            url: "/designDrawingCheckDetail?checkId",
            templateUrl: function () {
                return "/five/partial-designDrawingCheckDetail";
            },
            controller: 'FiveEdDesignDrawingCheckDetailController as vm'
        })
        .state('five.detail.majorDrawingCheck', {
            url: "/majorDrawingCheck?nodeId",
            templateUrl: function () {
                return "/five/partial-majorDrawingCheck";
            },
            controller: 'FiveEdMajorDrawingCheckController as vm'
        })
        .state('five.detail.majorDrawingCheckDetail', {
            url: "/majorDrawingCheckDetail?checkId",
            templateUrl: function () {
                return "/five/partial-majorDrawingCheckDetail";
            },
            controller: 'FiveEdMajorDrawingCheckDetailController as vm'
        })
        .state('five.detail.designDrawing', {
            url: "/designDrawing?nodeId",
            templateUrl: function () {
                return "/five/partial-designDrawing";
            },
            controller: 'FiveEdDesignDrawingController as vm'
        })
        .state('five.detail.designDrawingDetail', {
            url: "/designDrawingDetail?drawingId",
            templateUrl: function () {
                return "/five/partial-designDrawingDetail";
            },
            controller: 'FiveEdDesignDrawingDetailController as vm'
        })
}

function initFiveAndEdDetailRoute($stateProvider) {
    $stateProvider
        .state('five', {
            url: "/five",
            template: "<div ui-view></div>",
            abstract: true
        });

    initEdDetailRoute($stateProvider);
}