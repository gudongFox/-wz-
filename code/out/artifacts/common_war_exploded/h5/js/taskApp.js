var taskApp = angular.module("wuzhouApp", ['ui.router','services.act','services.common','controllers.task']);

taskApp.config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider,$sceDelegateProvider) {

    $locationProvider.html5Mode({
        enable: true,
        requireBase: false
    });
    $sceDelegateProvider.resourceUrlWhitelist([
        // Allow same origin resource loads.
        'self',
        // Allow loading from our assets domain.  Notice the difference between * and **.
        'https://owa.wuzhou.com.cn/**']);

    $stateProvider
        .state('task', {
            url: "/task",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('task.notice', {
            url: "/notice?businessKey",
            templateUrl: function () {
                return "/h5/task/noticeDetail.html";
            },
            controller: 'TaskNoticeDetailController as vm'
        })
        .state('task.commonFormDetail', {
            url: "/commonFormDetail?businessKey",
            templateUrl: function () {
                return "/h5/task/commonFormDetail.html";
            },
            controller: 'TaskCommonFormDetailController as vm'
        })
        .state('task.oaDecisionMaking', {
            url: "/oaDecisionMaking?businessKey",
            templateUrl: function () {
                return "/h5/task/oaDecisionMaking.html";
            },
            controller: 'TaskOaDecisionMakingController as vm'
        })
        .state('task.oaSignQuote', {
            url: "/oaSignQuote?businessKey",
            templateUrl: function () {
                return "/h5/task/oaSignQuote.html";
            },
            controller: 'TaskOaSignQuoteController as vm'
        })
        .state('task.oaDispatch', {
            url: "/oaDispatch?businessKey",
            templateUrl: function () {
                return "/h5/task/oaDispatch.html";
            },
            controller: 'TaskOaDispatchController as vm'
        })
        .state('task.oaDepartmentPost', {
            url: "/oaDepartmentPost?businessKey",
            templateUrl: function () {
                return "/h5/task/oaDepartmentPost.html";
            },
            controller: 'TaskOaDepartmentPostController as vm'
        })
        .state('task.oaFileInstruction', {
            url: "/oaFileInstruction?businessKey",
            templateUrl: function () {
                return "/h5/task/oaFileInstruction.html";
            },
            controller: 'TaskOaFileInstructionController as vm'
        })
        .state('task.oaReport', {
            url: "/oaReport?businessKey",
            templateUrl: function () {
                return "/h5/task/oaReport.html";
            },
            controller: 'TaskOaReportController as vm'
        })
        .state('task.businessCustomer', {
            url: "/businessCustomer?businessKey",
            templateUrl: function () {
                return "/h5/task/businessCustomer.html";
            },
            controller: 'TaskBusinessCustomerController as vm'
        })
        .state('task.oaBidApply', {
            url: "/oaBidApply?businessKey",
            templateUrl: function () {
                return "/h5/task/oaBidApply.html";
            },
            controller: 'TaskOaBidApplyController as vm'
        })
        .state('task.businessContractReview', {
            url: "/businessContractReview?businessKey",
            templateUrl: function () {
                return "/h5/task/businessContractReview.html";
            },
            controller: 'TaskBusinessContractReviewController as vm'
        })
        .state('task.oaSoftware', {
            url: "/oaSoftware?businessKey",
            templateUrl: function () {
                return "/h5/task/oaSoftware.html";
            },
            controller: 'TaskOaSoftwareController as vm'
        })
        .state('task.oaNoticeApply', {
            url: "/oaNoticeApply?businessKey",
            templateUrl: function () {
                return "/h5/task/oaNoticeApply.html";
            },
            controller: 'TaskOaNoticeApplyController as vm'
        })
        .state('task.oaInformationPlan', {
            url: "/oaInformationPlan?businessKey",
            templateUrl: function () {
                return "/h5/task/oaInformationPlan.html";
            },
            controller: 'TaskOaInformationPlanController as vm'
        })
        .state('task.oaInformationEquipmentProcurement', {
            url: "/oaInformationEquipmentProcurement?businessKey",
            templateUrl: function () {
                return "/h5/task/oaInformationEquipmentProcurement.html";
            },
            controller: 'TaskOaInformationEquipmentProcurementController as vm'
        })
        .state('task.oaInformationEquipmentApply', {
            url: "/oaInformationEquipmentApply?businessKey",
            templateUrl: function () {
                return "/h5/task/oaInformationEquipmentApply.html";
            },
            controller: 'TaskOaInformationEquipmentApplyController as vm'
        })
        .state('task.businessOutAssist', {
            url: "/businessOutAssist?businessKey",
            templateUrl: function () {
                return "/h5/task/businessOutAssist.html";
            },
            controller: 'TaskBusinessOutAssistController as vm'
        })
        .state('task.oaProjectFundPlan', {
            url: "/oaProjectFundPlan?businessKey",
            templateUrl: function () {
                return "/h5/task/oaProjectFundPlan.html";
            },
            controller: 'TaskOaProjectFundPlanController as vm'
        })
        .state('task.businessSubpackage', {
            url: "/businessSubpackage?businessKey",
            templateUrl: function () {
                return "/h5/task/businessSubpackage.html";
            },
            controller: 'TaskBusinessSubpackageController as vm'
        })
        .state('task.businessPurchase', {
            url: "/businessPurchase?businessKey",
            templateUrl: function () {
                return "/h5/task/businessPurchase.html";
            },
            controller: 'TaskBusinessPurchaseController as vm'
        })
        .state('task.oaEquipmentAcceptance', {
            url: "/oaEquipmentAcceptance?businessKey",
            templateUrl: function () {
                return "/h5/task/oaEquipmentAcceptance.html";
            },
            controller: 'TaskOaEquipmentAcceptanceController as vm'
        })
        .state('task.oaFileSynergy', {
            url: "/oaFileSynergy?businessKey",
            templateUrl: function () {
                return "/h5/task/oaFileSynergy.html";
            },
            controller: 'TaskOaFileSynergyController as vm'
        })
        .state('task.financeBalance', {
            url: "/financeBalance?businessKey",
            templateUrl: function () {
                return "/h5/task/financeBalance.html";
            },
            controller: 'TaskFinanceBalanceController as vm'
        })
        .state('task.businessContract', {
            url: "/businessContract?businessKey",
            templateUrl: function () {
                return "/h5/task/businessContract.html";
            },
            controller: 'TaskBusinessContractController as vm'
        })
        .state('task.furniturePurchase', {
            url: "/furniturePurchase?businessKey",
            templateUrl: function () {
                return "/h5/task/furniturePurchase.html";
            },
            controller: 'TaskFurniturePurchaseController as vm'
        })
        .state('task.assetScrap', {
            url: "/assetScrap?businessKey",
            templateUrl: function () {
                return "/h5/task/assetScrap.html";
            },
            controller: 'TaskAssetScrapController as vm'
        })
        .state('task.inlandProjectApply', {
            url: "/inlandProjectApply?businessKey",
            templateUrl: function () {
                return "/h5/task/inlandProjectApply.html";
            },
            controller: 'TaskInlandProjectApplyController as vm'
        })
        .state('task.externalStandardApply', {
            url: "/externalStandardApply?businessKey",
            templateUrl: function () {
                return "/h5/task/externalStandardApply.html";
            },
            controller: 'TaskExternalStandardApplyController as vm'
        })
        .state('task.stampOfficeDetail', {
            url: "/stampOfficeDetail?businessKey",
            templateUrl: function () {
                return "/h5/task/stampOfficeDetail.html";
            },
            controller: 'TaskStampOfficeDetailController as vm'
        })
});


taskApp.controller('CommonBaseController', function ($scope, $rootScope, $http,commonFormDataService,actProcessQueryService, actTaskQueryService, actTaskHandleService, actProcessHandleService,commonUserService) {

        $scope.init=function(){
            $http({
                method: 'POST',
                url: '/common/getOwaStatus.json'
            }).then(function (value) {
                $rootScope.owaInfo=value.data.data;
                ;
            })
        }



        $rootScope.initTopTab=function () {
            $(".mui-control-item").bind('click', function () {
                var html = $(this).html();
                var findIndex = 0;
                $(".mui-control-item").each(function (index) {
                    if ($(this).html() == html) {
                        findIndex = index;
                        $(this).addClass("mui-active");
                    } else {
                        $(this).removeClass("mui-active");
                    }
                });

                $(".mui-control-content").each(function (index) {
                    if (findIndex == index) {
                        $(this).addClass("mui-active");
                    } else {
                        $(this).removeClass("mui-active");
                    }
                });
            })
        }

        $rootScope.loadProcessInstance=function(businessKey,enLogin) {

            commonFormDataService.getTplConfig("", businessKey, enLogin).then(function (value) {
                $rootScope.tplConfig = value.data.data;
                if ($rootScope.tplConfig.saveAble) {
                    $rootScope.tplConfig.saveAble = false;
                    for (var i = 0; i < openTypeList.length; i++) {
                        if (openTypeList[i] === $rootScope.tplConfig.processKey) {
                            $rootScope.tplConfig.saveAble = true;
                        }
                    }
                }
            });

            actProcessQueryService.getCustomProcessInstance("", businessKey, enLogin).then(function (value) {
                $rootScope.processInstance = value.data.data;
                if ($rootScope.processInstance) {
                    document.title = $scope.processInstance.instance.processDefinitionName;
                    $rootScope.refreshTime = new Date().getTime();
                    $rootScope.processInstanceId = $scope.processInstance.instance.id;
                    actTaskQueryService.listHistoricTaskInstance($scope.processInstanceId).then(function (value) {
                        $rootScope.tasks = value.data.data;
                    });
                } else {
                    document.title = "任务详情";
                }
            });
        }

        $rootScope.initWebControl=function () {
            setTimeout(function () {
                $(".mui-date-picker").each(function () {
                    var txtDate= $(this)[0];
                    txtDate.addEventListener('tap', function() {

                        document.activeElement.blur();

                        var value = $(txtDate).val();

                        var picker = new mui.DtPicker({
                            type: "date",
                            value: value,
                            beginYear: 2020,
                            endYear: 2030
                        });

                        picker.show(function (rs) {
                            $(txtDate).val(rs.text).trigger('change');;
                            picker.dispose();
                        });

                    });
                });

                $(".mui-date-time-picker").each(function () {
                    var txtDate= $(this)[0];
                    txtDate.addEventListener('tap', function() {

                        document.activeElement.blur();

                        var value = $(txtDate).val();

                        var picker = new mui.DtPicker({
                            type: "datetime",
                            value: value,
                            beginYear: 2020,
                            endYear: 2030
                        });

                        picker.show(function (rs) {
                            $(txtDate).val(rs.text).trigger('change');;
                            picker.dispose();
                        });

                    });
                });

            },100);
        }

        /**
         * 得到页面数据
         * @param id
         * @param groupList
         * @returns {{id: *, enLogin: *}}
         */
        $rootScope.getFormData=function (id,groupList) {
            var data = {id: id, enLogin: enLogin};
            for (var i = 0; i < groupList.length; i++) {
                var group = groupList[i];
                for (var j = 0; j < group.items.length; j++) {
                    var item = group.items[j];
                    if (item.commonFormDetail.editable) {
                        data[item.commonFormDetail.inputCode] = item.inputValue;
                    }
                }
            }
            return data;
        }

        $rootScope.downloadAttach = function (id,preview) {
            if (preview) {
                if($rootScope.owaInfo.owa){

                }
            }
            if (!preview) {
                window.open("/common/attach/download/" + id);
            }
        }

        $rootScope.downloadFile = function (file,preview) {
            var url = "/common/attach/download/" + file.commonAttach.id;
            if (preview) {
                if ($scope.owaInfo.owa) {
                    if ($.inArray(file.commonAttach.extensionName, $scope.owaInfo.owaFileType) >= 0) {
                        url = "/common/previewFile/" + file.id;
                    }
                }
            }
            window.location.href = url;
        }



        $scope.init();

    })

    .controller('TaskActionController', function ($scope, $rootScope, $http, actProcessQueryService, actTaskQueryService, actTaskHandleService, actProcessHandleService,commonUserService) {

        $rootScope.enLogin=enLogin;

        $rootScope.showSendTask=function (send) {
            var optName='打回';
            if(send){
                optName="发送";
                if($scope.processInstance.firstTask){
                    optName="提交";
                }
            }
            mui.prompt("请输入"+optName + "意见", function (result) {
                if (result.index > 0) {
                    if (send) {
                        actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin,"h5").then(function (value) {
                            if (value.data.ret) {
                                mui.toast("发送成功!");
                                $scope.loadProcessInstance();
                            } else {
                                mui.alert(value.data.msg);
                            }
                        });
                    } else {
                        actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin,"h5").then(function (value) {
                            if (value.data.ret) {
                                mui.toast("打回成功!");
                                $scope.loadProcessInstance();
                            } else {
                                mui.alert(value.data.msg);
                            }
                        });
                    }
                }
            })
        }

        $rootScope.showTakeTask = function () {
            mui.prompt("确定要接受该任务吗?", function (result) {
                if (result.index > 0) {
                    actTaskHandleService.takeTask($scope.processInstance.taskId, enLogin,"h5").then(function (value) {
                        if (value.data.ret) {
                            $scope.loadProcessInstance();
                        } else {
                            mui.alert(value.data.msg);
                        }
                    });
                }
            })
        }

        $rootScope.showResolveTask = function () {
            mui.prompt("请输入处理意见", function (result) {
                if (result.index > 0) {
                    actTaskHandleService.resolveTask($scope.processInstance.taskId, enLogin, result.value,"h5").then(function (value) {
                        if (value.data.ret) {
                            mui.toast("处理成功!");
                            $scope.loadProcessInstance();
                        } else {
                            mui.alert(value.data.msg);
                        }
                    });
                }
            })
        }

        $rootScope.showCcFinishTask = function () {
            mui.prompt("请输入处理意见", function (result) {
                if (result.index > 0) {
                    var taskId=$rootScope.processInstance.ccTaskId;
                    actTaskHandleService.ccFinishTask(taskId, result.value,"h5").then(function (value) {
                        if (value.data.ret) {
                            mui.toast("办结完成!");
                            $scope.loadProcessInstance();
                        } else {
                            mui.alert(value.data.msg);
                        }
                    });
                }
            })
        }

        $rootScope.showFetchTask = function () {
            mui.prompt("确定要取回该任务吗?", function (result) {
                if (result.index > 0) {
                    actTaskHandleService.fetchTask($scope.processInstance.fetchTaskId, enLogin,"h5").then(function (value) {
                        if (value.data.ret) {
                            mui.toast("取回成功!");
                            $scope.loadProcessInstance();
                        } else {
                            mui.alert(value.data.msg);
                        }
                    });
                }
            })
        }

        $rootScope.showTransferTask = function () {
            $rootScope.handleTask = {
                modal: 'transferModal',
                taskId: $scope.processInstance.taskId,
                enLogin: enLogin,
                comment: '移交任务给新用户',
                newUser: '',
                newUserName: ''
            };
            $(".actionModal").each(function () {
                $(this).css("display", "none");
            });
            $("#transferModal").css("display", "block");
            $("#contentForm").css("display", "none");
        }

        $rootScope.showDelegateTask = function () {
            $rootScope.handleTask = {
                modal: 'delegateModal',
                taskId: $scope.processInstance.taskId,
                enLogin: enLogin,
                comment: '委托其他用户办理',
                newUser: '',
                newUserName: ''
            };

            $(".actionModal").each(function () {
                $(this).css("display", "none");
            });
            $("#delegateModal").css("display", "block");
            $("#mainContent").css("display", "none");
        }

        $rootScope.doTransferTask = function () {
            if($scope.handleTask.newUser==''){
                mui.toast("请先指定新用户!");
                return;
            }
            actTaskHandleService.transferTask($scope.handleTask.taskId, $scope.handleTask.enLogin, $scope.handleTask.newUser, $scope.handleTask.comment,"h5").then(function (value) {
                if (value.data.ret) {
                    mui.toast("移交成功!");
                    $scope.backShowMain();
                    $scope.loadProcessInstance();
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        $rootScope.doDelegateTask = function () {
            if($scope.handleTask.newUser==''){
                mui.toast("请先指定新用户!");
                return;
            }


            actTaskHandleService.delegateTask($scope.handleTask.taskId, $scope.handleTask.enLogin, $scope.handleTask.newUser, $scope.handleTask.comment,"h5").then(function (value) {
                if (value.data.ret) {
                    mui.toast("委托成功!");
                    $scope.backShowMain();
                    $scope.loadProcessInstance();
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        $rootScope.doResolveTask = function () {
            actTaskHandleService.delegateTask($scope.handleTask.taskId, $scope.handleTask.enLogin, $scope.handleTask.newUser, $scope.handleTask.comment,"h5").then(function (value) {
                if (value.data.ret) {
                    mui.toast("委托成功!");
                    $scope.showOnly("mainContent");
                    $scope.loadProcessInstance();
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }


        $rootScope.showSelectUser=function(destModalId){
            $scope.destModalId=destModalId;
            $(".actionModal").each(function () {
                $(this).css("display", "none");
            });
            $("#actSelectUserModal").css("display", "block");


            $scope.loadUser(0);
        }

        $rootScope.loadUser = function (id) {
            if (id > 0) {
                commonUserService.getDept(id).then(function (value) {
                    $scope.currentDept = value.data.data;
                })
            } else {
                $scope.currentDept = {id: 0, name: 'root', parentId: 0};
            }
            commonUserService.listDept(enLogin, id).then(function (value) {
                $scope.depts = value.data.data;
            })

            commonUserService.listUser(enLogin, id).then(function (value) {
                $scope.users = value.data.data;
            })
        }

        $rootScope.doSelectUser = function (user) {
            $scope.handleTask.newUser = user.enLogin;
            $scope.handleTask.newUserName = user.cnName;
            $("#" + $scope.destModalId).css("display", "block");
            $("#actSelectUserModal").css("display", "none");
        }

        $rootScope.backShowMain=function (){
            $(".actionModal").each(function () {
                $(this).css("display", "none");
            });
            $("#contentForm").css("display", "block");
        }

    })

    .controller('TaskDetailController', function ($scope, $rootScope, $http,$state,actProcessQueryService, actTaskQueryService, commonFormDataService) {
        $http({
            method: 'POST',
            url: '/wx/getH5FormDataUrl.json',
            params: {businessKey: businessKey}
        }).then(function (response) {
            if (response.data.ret) {
                $state.go(response.data.data.url, response.data.data.params);
            }
        });
    })

    .controller('TaskBusinessFileController', function ($scope, $rootScope, $http, commonDirService,commonFileService) {


        $scope.init=function(){
            $http({
                method: 'POST',
                url: '/common/getOwaStatus.json'
            }).then(function (value) {
                $scope.owaInfo=value.data.data;
            })
        }

        $scope.loadDirAndFile = function (dirId) {

            $scope.dirId=dirId;

            $scope.loadDir(dirId);

            $scope.loadFile(dirId);
        }

        $scope.loadDir = function (dirId) {

            commonDirService.listBreadcrumb(businessKey, dirId).then(function (value) {
                $scope.breadcrumbs = value.data.data;
            })

            commonDirService.listData(businessKey, dirId).then(function (value) {
                $scope.dirs = value.data.data;
            })
        }

        $scope.loadFile = function (dirId) {
            commonFileService.listData(businessKey, dirId).then(function (value) {
                $scope.files = value.data.data;
            })
        }

        $scope.downloadFile = function (file,preview) {
            var url = "/common/attach/download/" + file.commonAttach.id;
            if (preview) {
                if ($scope.owaInfo.owa) {
                    if ($.inArray(file.commonAttach.extensionName, $scope.owaInfo.owaFileType) >= 0) {
                        url = "/common/previewFile/" + file.id;
                    }
                }
            }
            window.location.href = url;
        }


        $("#txtImageUpload").bind("change",function(){
            uploadFile();
        });

        function uploadFile(){
            var e = window.event || event;
            // 获取当前选中的文件
            var oFile = e.target.files[0];
            var formData = new FormData();
            formData.append("file", oFile);
            formData.append("dirId",$scope.dirId);
            formData.append("enLogin",enLogin);
            formData.append("businessKey",businessKey);
            $.ajax({
                type: 'POST',
                url: "/common/file/receive.json",
                data: formData,
                processData: false,
                contentType: false,
                dataType: "json",
                success: function (data) {
                    if(data.ret) {
                        mui.alert('上传成功');
                        $scope.loadDirAndFile($scope.dirId);
                    }else{
                        mui.alert(data.msg);
                    }
                }
            });
        }

        $scope.loadDirAndFile(0);

        $scope.removeFile=function (file) {
            mui.confirm("确定删除该文件吗？",function (result) {
                if(result.index){
                    commonFileService.remove(file.id,enLogin).then(function (value) {
                        if (value.data.ret) {
                            mui.toast('删除成功!');
                            $scope.loadDirAndFile($scope.dirId);
                        } else {
                            mui.alert(value.data.msg);
                        }
                    })
                }

            })
        }

        $scope.init();

    })

    .controller('TaskCommonFormDetailController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;

        vm.init = function () {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                } else {
                    mui.alert("未找到数据(" + value.data.msg + ")");
                    window.location.href = "/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey, enLogin);
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared ? '取消关注!' : '关注成功!');
                    $scope.loadProcessInstance(businessKey, enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain = function () {
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal = function (item, group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown', function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q = "";
            vm.loadUser();
        }

        vm.loadUser = function () {
            var config = $.extend({loginItem: {}, item: {}}, vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser = function (user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers = function () {

            var users = [];
            for (var i = 0; i < vm.users.length; i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.getUserRequestConfig = function (item, group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {inputValue: ""};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }
            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem: loginItem,
                item: item,
            };
            return config;
        }

        vm.showDeptModal = function (item, group) {
            document.activeElement.blur();
            if (!item.commonFormDetail.editable) return;
            var multiple = item.commonFormDetail.multiple;
            var idKey = item.commonFormDetail.inputCode.replace('Name', '');
            var chargeManKey=item.commonFormDetail.inputCode.replace('Name', '')+"ChargeMan";
            var chargeMenKey=item.commonFormDetail.inputCode.replace('Name', '')+"ChargeMen";
            var idItem = {inputValue: ""};
            var chargeManItem=undefined;
            var chargeMenItem=undefined;
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === idKey||code===idKey+"Id") {
                    idItem = currentItem;
                }else if(code===chargeManKey){
                    chargeManItem=currentItem;
                }else if(code===chargeMenKey){
                    chargeMenItem=currentItem;
                }
            }
            var selects = idItem.inputValue;
            if (idItem.inputValue.constructor === Array) {
                selects = idItem.inputValue.join(',');
            }
            var dataSourceConfig = {};
            try {
                dataSourceConfig = jQuery.parseJSON(item.commonFormDetail.dataSource);
            } catch (e) {

            }
            var params = $.extend({}, dataSourceConfig, {
                selects: selects,
                multiple: multiple
            });
            ;
            $.showJsTreeSelectDept(params, function (selects, ids, names,charges) {
                item.inputValue = names;
                idItem.inputValue = ids;
                if(chargeManItem){
                    chargeManItem.inputValue="";
                    if(charges.length>0){
                        chargeManItem.inputValue=charges[0];
                    }
                }
                if(chargeMenItem){
                    chargeMenItem.inputValue=charges;
                }
               // $scope.$apply();
            });
        }

        vm.toggleMultipleModal = function (detail) {
            vm.detail = detail;
            if (vm.detail.inputValue.length > 0) {
                for (var i = 0; i < vm.detail.dataSource.length; i++) {
                    var code = vm.detail.dataSource[i];
                    for (var j = 0; j < vm.detail.inputValue.length; j++) {
                        if (vm.detail.inputValue[j] == code.name) {
                            code.selected = true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple = function () {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        vm.remove=function(){
            mui.confirm("确定要删除该流程吗?", function (result) {
                if (result.index > 0) {
                    actProcessHandleService.deleteProcessInstanceById($scope.processInstance.instance.id, enLogin, "no why").then(function (value) {
                        if (value.data.ret) {
                            mui.toast('删除成功');
                            vm.backUrl();
                        } else {
                            mui.alert(value.data.msg);
                        }
                    })
                }
            });
        }


        $rootScope.save = function () {
            var data = $scope.getFormData($scope.data.id, $scope.groupList);
            data["autoSubmit"] = false;
            commonFormDataService.save(data).then(function (value) {
                if (value.data.ret) {
                    mui.alert('保存成功!')
                } else {
                    mui.alert(value.data.msg);
                }
            });
        }


        $scope.showSendTask=function(send) {
            if ($scope.tplConfig.saveAble) {
                var data = $scope.getFormData($scope.data.id, $scope.groupList);
                data["autoSubmit"] = true;
                commonFormDataService.save(data).then(function (value) {
                    if (value.data.ret) {
                        $rootScope.showSendTask(send);
                    } else {
                        mui.alert(value.data.msg);
                    }
                });
            } else {
                $rootScope.showSendTask(send);
            }
        }


        $scope.$watch("groupList",function (newValue,oldValue) {
            if($scope.groupList) {
            }
        },true);

        vm.init();
    });

