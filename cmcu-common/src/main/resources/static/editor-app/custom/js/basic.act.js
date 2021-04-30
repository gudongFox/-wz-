function initActRoute($stateProvider, version) {
    $stateProvider
        .state('act', {
            url: "/act",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('act.model', {
            url: "/model",
            templateUrl: function () {
                if (version) return "/editor-app/custom/tpl/" + version + "/model.html";
                return "/editor-app/custom/tpl/model.html";
            },
            controller: 'ActModelController as vm'
        })
        .state('act.processDefinition', {
            url: "/processDefinition",
            templateUrl: function () {
                if (version) return "/editor-app/custom/tpl/" + version + "/processDefinition.html";
                return "/editor-app/custom/tpl/processDefinition.html";
            },
            controller: 'ActProcessDefinitionController as vm'
        }).state('act.processInstance', {
        url: "/processInstance",
        templateUrl: function () {
            if (version) return "/editor-app/custom/tpl/" + version + "/processInstance.html";
            return "/editor-app/custom/tpl/processInstance.html";
        },
        controller: 'ActProcessInstanceController as vm'
    }).state('act.processInstanceDetail', {
        url: "/processInstanceDetail?processInstanceId",
        templateUrl: function () {
            if (version) return "/editor-app/custom/tpl/" + version + "/processInstanceDetail.html";
            return "/editor-app/custom/tpl/processInstanceDetail.html";
        },
        controller: 'ActProcessInstanceDetailController as vm'
    });


}


function initActFunction($rootScope, actProcessQueryService, actProcessHandleService, actTaskQueryService,actTaskHandleService) {

    var currentProcessInstanceId = "";


    $rootScope.loadActProcessInstance = function (processInstanceId) {

        $rootScope.refeshTime = new Date().getSeconds();
        $rootScope.tasks = [];
        $rootScope.currentTask = {};
        if (processInstanceId) {
            currentProcessInstanceId = processInstanceId;
        }
        actTaskQueryService.getHistoricTaskInstanceByInstanceId(currentProcessInstanceId, user.enLogin).then(function (value) {
            $rootScope.currentTask = value.data.data;
        });

        actTaskQueryService.listHistoricTaskInstanceByInstanceId(currentProcessInstanceId, user.enLogin).then(function (value) {
            $rootScope.tasks = value.data.data;
        });
    }

    $rootScope.loadActTaskList = function (processInstanceId,enLogin) {
        actTaskQueryService.listHistoricTaskInstanceByInstanceId(processInstanceId,enLogin).then(function (value) {
            if (value.data.ret) {
                $rootScope.actTaskList = value.data.data;
            } else {
                $rootScope.actTaskList = [];
            }
        })
    }

    $rootScope.loadActTask = function (taskId, optComment) {
        actTaskQueryService.getHistoricTaskInstance(taskId).then(function (value) {
            if (value.data.ret) {
                var item = value.data.data;
                if (!item.currentComment) {
                    if(optComment==="delegate"){
                        item.currentComment = "该任务委托处理.";
                    }else if(optComment==="send") {
                        item.currentComment = "同意.";
                    }else if(optComment==="back"){
                        item.currentComment="打回.";
                    }else if(optComment==="resolve"){
                        item.currentComment="已处理完该任务.";
                    }else if(optComment==="transfer"){
                        item.currentComment="该任务移交处理.";
                    }else{
                        item.currentComment="同意.";
                    }
                }
                $rootScope.actTaskDetail = $.extend({}, item, {
                    processVariables_: JSON.stringify(item.processVariables),
                    taskLocalVariables_: JSON.stringify(item.taskLocalVariables),
                    comments_: JSON.stringify(item.comments)
                });
            }
        });
    }

    $rootScope.showActTaskModal = function (taskId) {
        $rootScope.loadActTask(taskId);
        $("#actTaskModal").modal("show");
    }

    $rootScope.showActCommentModal=function(taskId){
        $rootScope.loadActTask(taskId);
        $("#actCommentModal").modal("show");
    }

}


function initSelectUserModal($rootScope,commonUserService) {

    var configData={};

    $rootScope.showUserJsTreeModal = function (config) {
        $("#userJsTree_Key").keypress(function (e) {
            if (e.which === 13) {
                configData.q=$("#userJsTree_Key").val();
                $rootScope.showUserJsTreeModal(configData);
                return false;
            }
        });

        configData = $.extend({parentDeptId: "0", multiple: false, selects: "", q: "",enLogin:user.enLogin}, config);
        $("#userJsTree_Key").val(configData.q);
        initUserTree();
        $("#userJsTreeModal").modal("show");
        return "btnSelectJsTreeUser";
    }

    $rootScope.rebuildJsTreeUser = function () {
        configData.q=$("#userJsTree_Key").val();
        initUserTree();
    }

    $rootScope.closeUserJsTreeModal=function () {
        var selectedUsers = [];
        var selectedNodes = $('#userJsTreeModal_tree').jstree().get_selected(true);
        for (var i = 0; i < selectedNodes.length; i++) {
            if (selectedNodes[i].id.indexOf('dept_') === -1) {
                selectedUsers.push(selectedNodes[i]);
            }
        }
        if (selectedUsers.length === 0) {
            toastr.error("请选择合适的用户!");
            return;
        }
        $("#userJsTreeModal").modal("hide");

        if(configData.multiple) return selectedUsers;
        return selectedUsers[0];
    }

    function initUserTree() {
        commonUserService.listUserJsTreeData(configData).then(function (value) {
            if (value.data.ret) {
                var treeData = value.data.data;
                $('#userJsTreeModal_tree').jstree("destroy");
                $('#userJsTreeModal_tree').jstree({
                    'core': {
                        'data': treeData,
                        "multiple" : configData.multiple,
                        "themes": {
                            "responsive": false
                        }
                    },
                    "checkbox": {
                        "keep_selected_style": false,
                        "three_state": configData.multiple
                    },
                    "plugins": ["wholerow", "checkbox"]
                });
            }
        });
    }

}
