angular.module('controllers.act', [])

    .controller("ActModelController", function ($state, $scope, $http, actModelService) {
        var vm = this;
        vm.params = getNgParam($state, {
            modelName: "",
            modelCategory: "",
            modelTenetId: "",
            qModelName: "",
            qModelCategory: "",
            qModelTenetId: ""
        });
        vm.pageInfo = {pageNum: 1, pageSize: pageSize};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.qModelName = vm.params.modelName;
            vm.params.qModelCategory = vm.params.modelCategory;
            vm.params.qModelTenetId = vm.params.modelTenetId;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                modelName: vm.params.qModelName,
                modelCategory: vm.params.qModelCategory,
                modelTenetId: vm.params.qModelTenetId,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize
            };
            actModelService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    for (var i = 0; i < vm.pageInfo.list.length; i++) {
                        vm.pageInfo.list[i].metaInfo = JSON.parse(vm.pageInfo.list[i].metaInfo);
                    }
                    setNgCache($state, vm.params, vm.pageInfo);
                }
            })
        };

        vm.newModel = function () {
            bootbox.confirm("请您确定将创建一个新的流程模型!", function (result) {
                if (result) {
                    actModelService.getNewModel(user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("请您确定删除该流程模型吗?", function (result) {
                if (result) {
                    actModelService.remove(id).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.deployment = function (id) {
            bootbox.confirm("请您确定部署选中的流程模型!", function (result) {
                if (result) {
                    actModelService.deployment(id).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("部署成功!");
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init = function () {
            vm.queryData();
            $("input[type='search']").keypress(function (e) {
                if (e.which === 13) {
                    vm.queryData();
                    return false;
                }
            });

            actModelService.listModelCategory(user.enLogin).then(function (value) {
                vm.modelCategoryList=value.data.data;
            });

            $("#uploadModelFile").fileupload({
                dataType: 'text',
                url: '/act/model/receive.do?enLogin='+user.enLogin,
                send: function (e, data) {
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                progress: function (e, data) {
                    var speed = (data.bitrate / (8 * 1024 * 1024)).toFixed(2) + "Mbit/s";
                    var percent = parseInt(data.loaded / data.total * 100, 10);
                    $(".loading-message.loading-message-boxed span").html(" " + speed + " " + percent + "%");
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    ;
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("导入数据成功!");
                        vm.loadPagedData();
                    } else {
                        toastr.error(result.msg);
                    }
                }
            });
        };

        vm.init();

        return vm;
    })

    .controller("ActHistoryTaskController", function ($state, $scope, $http, actModelService,actTaskQueryService) {
        $scope.showTaskComments=function (task) {
            $scope.task=task;
            $('#commentsModal').modal('show');
        }
    })

    .controller("ActProcessDefinitionController", function ($state, $scope, $http, actProcessDefinitionService,actModelService) {
        var vm = this;

        vm.params = getNgParam($state, {
            modelKey: "",
            modelName: "",
            modelCategory: "",
            modelTenetId: "",
            qModelKey: "",
            qModelName: "",
            qModelCategory: "",
            qModelTenetId: ""
        });
        vm.pageInfo = {pageNum: 1, pageSize: pageSize};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.qModelKey = vm.params.modelKey;
            vm.params.qModelName = vm.params.modelName;
            vm.params.qModelCategory = vm.params.modelCategory;
            vm.params.qModelTenetId = vm.params.modelTenetId;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                modelKey: vm.params.qModelKey,
                modelName: vm.params.qModelName,
                modelCategory: vm.params.qModelCategory,
                modelTenetId: vm.params.qModelTenetId,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize
            };
            actProcessDefinitionService.listPagedDefinition(params).then(function (value) {
                vm.pageInfo = value.data.data;
                setNgCache($state, vm.params, vm.pageInfo);
            });
        }

        vm.init = function () {

            vm.queryData();
            $("input[type='search']").keypress(function (e) {
                if (e.which === 13) {
                    vm.queryData();
                    return false;
                }
            });
            actModelService.listModelCategory(user.enLogin).then(function (value) {
                vm.modelCategoryList=value.data.data;
            });

        };

        vm.remove = function (id) {
            bootbox.confirm("请您确定删除该流程定义吗?", function (result) {
                if (result) {
                    actProcessDefinitionService.removeDefinition(id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.showImage = function (item) {
            vm.item = $.extend({}, item);
            $("#definitionImageModal").modal("show");
        }

        vm.init();

        return vm;
    })

    .controller("ActProcessInstanceController", function ($state, $scope, $http, actProcessDefinitionService, actProcessQueryService, actProcessHandleService,actModelService) {
        var vm = this;
        vm.params = getNgParam($state, {
            modelKey: "",
            modelName: "",
            modelCategory: "",
            businessKey:"",
            modelTenetId: "",
            qModelName: "",
            qModelCategory: "",
            qModelTenetId: "",
            qBusinessKey:"",
            initiator: "",
            qInitiator: "",
            initiatorName: "",
            qInitiatorName: "",
            pageNum: 1,
            pageSize: pageSize
        });

        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.qModelKey = vm.params.modelKey;
            vm.params.qProcessDefinitionName = vm.params.processDefinitionName;
            vm.params.qModelCategory = vm.params.modelCategory;
            vm.params.qModelTenetId = vm.params.modelTenetId;
            vm.params.qBusinessKey = vm.params.businessKey;
            vm.params.qInitiator = vm.params.initiator;
            vm.params.qInitiatorName = vm.params.initiatorName;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                modelKey:vm.params.qModelKey,
                processDefinitionName: vm.params.qProcessDefinitionName,
                modelCategory: vm.params.qModelCategory,
                modelTenetId: vm.params.qModelTenetId,
                businessKey: vm.params.qBusinessKey,
                initiatorName: vm.params.qInitiatorName,
                initiator: vm.params.qInitiator,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize
            };
            actProcessQueryService.listPagedProcessInstance(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setNgCache($state, vm.params, vm.pageInfo);
                }
            });
        }

        vm.init = function () {

            commonWebInit(vm);

            vm.queryData();

            actModelService.listModelCategory(user.enLogin).then(function (value) {
                vm.modelCategoryList=value.data.data;
            });

        };

        vm.remove = function (id) {
            bootbox.confirm("请您确定删除该流程定义吗??", function (result) {
                if (result) {
                    actProcessDefinitionService.removeDefinition(id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.showImage = function (item) {
            vm.item = $.extend({}, item);
            $("#instanceImageModal").modal("show");
        }

        vm.showDetail = function (processInstanceId) {
            $state.go("act.processInstanceDetail", {processInstanceId: processInstanceId});
        }

        vm.deleteProcessInstanceById = function (processInstanceId) {

            bootbox.prompt("请输入删除原因,删除后,无法恢复!", function (result) {
                actProcessHandleService.deleteProcessInstanceById(processInstanceId, result).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("删除流程成功!");
                        vm.loadPagedData();
                    }
                })
            });
        }

        vm.deleteProcessDataById = function (processInstanceId) {
            bootbox.confirm("确认删除表单以及历史流程数据?", function (result) {
                if (result != null) {
                    actProcessHandleService.deleteProcessDataById(processInstanceId, result).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.init();
                        }
                    })
                }
            });
        }


        vm.init();

        return vm;
    })

    .controller("ActProcessInstanceDetailController", function ($state, $stateParams, $scope,$rootScope,$http,commonFormDataService,actProcessQueryService, actTaskQueryService, actProcessHandleService,actTaskHandleService) {
        var vm = this;
        var processInstanceId = $stateParams.processInstanceId;
        $scope.adminMode = true;

        vm.init = function () {

            actProcessQueryService.getAdminCustomProcessInstance(processInstanceId).then(function (value) {
                $rootScope.processInstance=value.data.data;
                $rootScope.refeshTime = new Date().getSeconds();
                $scope.tplConfig={businessKey:$rootScope.processInstance.instance.businessKey,showBusinessFile:true};
                vm.loadData();
            })

            actTaskQueryService.listHistoricTaskInstance(processInstanceId).then(function (value) {
                $scope.tasks = value.data.data;
            });
        }


        vm.loadData=function(){
            commonFormDataService.getModelByBusinessKey($rootScope.processInstance.instance.businessKey,$rootScope.processInstance.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.formData = value.data.data.formData;
                $scope.fileTplTitle=(vm.groupList.length+1)+'.业务附件';
                $scope.initWebControl();
            })
        }

        vm.refresh=function(){
            vm.init();
        }

        vm.activateProcessInstanceById = function (processInstanceId) {
            actProcessHandleService.activateProcessInstanceById(processInstanceId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("恢复流程成功!");
                    vm.init();
                }
            })
        }

        vm.suspendProcessInstanceById = function (processInstanceId) {
            actProcessHandleService.suspendProcessInstanceById(processInstanceId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("暂停流程成功!");
                    vm.init();
                }
            })
        }

        vm.forceNewAssignee=function(){
            jQuery.showJsTreeSelectUserModal({multiple: false,title:'选择新的办理人'}, function (selectedUsers,enLoginList,cnNameList) {
                if(enLoginList) {
                    bootbox.confirm("任务节点("+$rootScope.processInstance.currentTaskName+")指定新的办理人？"+cnNameList, function (result) {
                        if (result) {
                            actTaskHandleService.forceNewAssignee($rootScope.processInstance.taskId, enLoginList).then(function (value) {
                                if (value.data.ret) {
                                    toastr.success("设定办理人成功!");
                                    vm.init();
                                }
                            })
                        }
                    })
                }
            });
        }

        $scope.refresh=function(){
            vm.init();
        }

        vm.back=function(){
            $state.go("act.processInstance")
        }

        vm.init();

        return vm;
    })
