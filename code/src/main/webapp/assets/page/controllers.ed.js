angular.module('controllers.ed', [])


    .controller("EdProjectController", function ($state, $scope, edProjectService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: 10};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };


        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            edProjectService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.edit = function (item) {

            if (item.projectType.indexOf("勘察") >= 0) {
                $state.go("explore.detail.show", {id: item.id});
            } else if (item.projectType.indexOf("测试") >= 0) {
                $state.go("five.detail.show", {id: item.id});
            } else {
                $state.go("ed.detail.show", {id: item.id});
            }
        }

        vm.queryData();

        return vm;
    })

    .controller("EdDetailPartialStepShowController", function ($state, $stateParams, $scope, edProjectTreeService, actService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;
        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };
        vm.loadData = function () {
            actService.getEdStepTimeline(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.data = value.data.data;
                }
            });
        }

        vm.showStepDetail = function () {
            $state.go("ed.detail.stepDetail", {stepId: vm.treeNode.foreignKey});
        }
        vm.showPng = function (item) {

            vm.processDefinitionName = item.processDefinitionName;
            vm.processDefinitionKey = item.processDefinitionKey;
            $("#showPngModal").modal("show");
        }

        vm.init();
        return vm;
    })

    .controller("EdDetailPartialShowController", function ($state, $stateParams, $scope, edProjectTreeService, actService) {
        var vm = this;
        var projectId = $stateParams.id;

        vm.loadData = function () {
            actService.getProjectTimeline(projectId).then(function (value) {
                vm.steps = value.data.data;
            })
        }


        vm.loadData();
        return vm;
    })

    .controller("EdDetailController", function ($state, $stateParams,$rootScope,$scope,businessContractService,edProjectTreeService) {
        var vm = this;
        var id = $stateParams.id;

        vm.init = function () {
            $scope.loadTree();
            businessContractService.getModelById(id).then(function (value) {
                if(value.data.ret){
                    vm.businessContract=value.data.data;
                }
            })
        };

        vm.projectShow=function(){
            businessContractService.getModelById(id).then(function (value) {
                if(value.data.ret){
                    vm.businessContractdetail=value.data.data;
                }
            })
            $("#projectMessageModel").modal("show");
        }

        $scope.loadTree2=function(){
            edProjectTreeService.listProjectTree(id,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    var tree = value.data.data;
                    var jsTreeData = new Array();

                    for (var i = 0; i < tree.length; i++) {
                        var item = tree[i];
                        var node = {
                            id: item.id,
                            text: item.nodeName,
                            data: item,
                            icon: item.icon,
                            parent: item.parentId == 0 ? '#' : item.parentId,
                            state: {opened: item.opened, selected: item.selected, disabled: item.disabled}
                        };
                        jsTreeData.push(node);
                    };

                    $('#taskTree').jstree("destroy");
                    $('#taskTree').jstree({
                        'core': {
                            'data': jsTreeData
                        }
                    });
                    $('#taskTree').on('changed.jstree', function (e, data) {
                        if (data.selected.length > 0) {
                            if(data.action==="model"||(data.action==="ready"&&($state.current.name!=="ed.detail.show"&&$state.current.name.indexOf("ed.detail.")>=0))){
                                return;
                            }
                            var node = data.instance.get_node(data.selected[0]).data;
                            edProjectTreeService.rememberNodeState(node.id, true, 'selected', user.userLogin);


                            $state.go(node.nodeUrl, {nodeId: node.id});
                        }
                    });
                    $('#taskTree').on('select_node.jstree',function (e,data) {
                        if(data.node.state.opened&&(data.node.text.indexOf('阶段')>=0||data.node.data.nodeUrl.indexOf('ed.detail.stepShow')>=0)) {

                        }
                        else{
                            $('#taskTree').jstree("toggle_node", "#"+data.node.id);
                        }
                    });
                    $('#taskTree').on('after_open.jstree',function (e,data) {
                        edProjectTreeService.rememberNodeState(data.node.id,true,'opened',user.userLogin)
                    });
                    $('#taskTree').on('after_close.jstree',function (e,data) {
                        edProjectTreeService.rememberNodeState(data.node.id,false,'opened',user.userLogin);
                    });
                }
            });


            if(nodeId||stepId){
                $("#pageBar").css("display","none");
            }
            else {
                $("#pageBar").css("display","block");
            }
        };

        $scope.loadTree=function() {
            var params = {projectId: id, nodeId: nodeId, stepId: stepId, enLogin: user.enLogin};
            edProjectTreeService.listProjectJsTree(params).then(function (value) {
                if (value.data.ret) {
                    $('#taskTree').jstree("destroy");
                    $('#taskTree').jstree({
                        'core': {
                            'data': value.data.data
                        }
                    });

                    $('#taskTree').on('changed.jstree', function (e, data) {
                        if (data.selected.length > 0) {
                            var redirectUrl = false;
                            if (data.action === "select_node" || data.action === 'ready') {
                                redirectUrl = true;
                            }
                            if (data.action === 'ready') {
                                for (var p in $stateParams) {
                                    if ($stateParams[p]) {
                                        if (p != "id" && p != "nodeId") {
                                            redirectUrl = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            var node = data.instance.get_node(data.selected[0]).data;
                            if (redirectUrl) {
                                edProjectTreeService.rememberNodeState(node.id, true, 'selected', user.userLogin);
                                $state.go(node.nodeUrl, {nodeId: node.id});
                            }

                        }
                    });


                    $('#taskTree').on('after_open.jstree', function (e, data) {
                        edProjectTreeService.rememberNodeState(data.node.id, true, 'opened', user.userLogin)
                    });

                    $('#taskTree').on('after_close.jstree', function (e, data) {
                        edProjectTreeService.rememberNodeState(data.node.id, false, 'opened', user.userLogin);
                    });
                }
            });
        }

        vm.init();
        return vm;
    })

    .controller("EdDetailPartialStepController", function ($state, $stateParams, $scope, edProjectService, edProjectTreeService, edProjectStepService,sysCodeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {

            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;

                    if(vm.treeNode.nodeName =='其他相关资料') {
                        $state.go("ed.detail.stepDetail", {stepId: vm.treeNode.foreignKey});
                    }
                    edProjectStepService.checkIsChargeUser(vm.treeNode.projectId,0,user.userLogin).then(function (value1) {
                        if (value1.data.ret){
                            vm.isShow=value1.data.data;
                        }
                    })
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edProjectStepService.listDataByStageNodeId(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;

                }
            })

        }

        vm.add=function(){
            edProjectStepService.getNewModel(nodeId,user.userLogin).then(function (value) {
                if(value.data.ret){
                    $scope.loadTree();
                    vm.show(value.data.data);
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.stepDetail",{stepId:id});
        }

        vm.showCp=function(id){
            $state.go("ed.detail.co",{stepId:id});
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除该分期吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    edProjectStepService.remove(id,user.userLogin).then(function (value) {
                        vm.loadData();
                        $scope.loadTree();
                    });
                }
            })
        }
        vm.init();

        return vm;
    })

    .controller("EdDetailPartialStepDetailController", function ($rootScope,$state, $stateParams,$scope, hrEmployeeService,edProjectStepService,commonEdBuildService) {
        var vm = this;
        var stepId = $stateParams.stepId;


        vm.init = function () {
            vm.loadData();
            vm.loadBuild();
            setTimeout(function () {
                $scope.basicInit("");
            },1000);

        };

        vm.loadData = function () {
            vm.fileRemark = "应上传资料： 外审合格报告等、竣工备案登记表、会议纪要及工作函件";
            edProjectStepService.getModelById(stepId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.basicInit(vm.item.businessKey);
                    edProjectStepService.checkIsChargeUser(vm.item.projectId,vm.item.id,user.userLogin).then(function (value1) {
                        if (value1.data.ret){
                            vm.isShow=value1.data.data;
                        }
                    })
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            if($("#detail_form").valid()) {
                edProjectStepService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData();
                    }
                })
            }else{
                toastr.warning("请准确填写数据!")
            }
        };

        vm.loadBuild=function(){
            commonEdBuildService.listData(stepId).then(function (value) {
                vm.buildList=value.data.data;
            })
        }

        vm.addBuild=function(){
            commonEdBuildService.getNewModel(stepId,user.enLogin).then(function (value) {
                if(value.data.ret){
                    vm.build=value.data.data;
                    $("#buildModal").modal("show");
                }
            })
        }

        vm.showBuild=function(id){
            commonEdBuildService.getModelById(id).then(function (value) {
                if(value.data.ret){
                    vm.build=value.data.data;
                    $("#buildModal").modal("show");
                }
            })
        }

        vm.saveBuild = function () {
            commonEdBuildService.save(vm.build).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#buildModal").modal("hide");
                    vm.loadBuild();
                }
            })
        }

        vm.removeBuild=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    commonEdBuildService.remove(id,user.enLogin).then(function (value) {
                        if(value.data.ret){
                            toastr.success("删除成功!");
                            vm.loadBuild();
                        }
                    })
                }
            })

        }


        vm.showSelectEmployeeModal=function(){
            $scope.showSelectEmployeeModal_({title:"请选择 "+vm.optUserType, userLoginList:vm.optUser,multiple:true});
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();

            if(vm.optUserType=='分期负责人'){
                vm.item.chargeMan=$scope.selectedUserLogins_;
                vm.item.chargeManName=$scope.selectedUserNames_;
            }else if (vm.optUserType=='执行负责人') {
                vm.item.exeChargeMan=$scope.selectedUserLogins_;
                vm.item.exeChargeManName=$scope.selectedUserNames_;
            }
        };

        vm.saveSelectEmployee=function(){
            var logins = [];
            var names = []
            $(".cb_employee:checked").each(function () {
                var info = $(this).attr("data-name");
                logins.push(info.split('_')[0]);
                names.push(info.split('_')[1]);

            });
            if(vm.optUserType=='分期负责人'){
                vm.item.chargeMan=logins.join(",");
                vm.item.chargeManName=names.join(",");
            }else if (vm.optUserType=='执行负责人') {
                vm.item.exeChargeMan=logins.join(",");
                vm.item.exeChargeManName=names.join(",");
            }

            $("#selectEmployeeModal").modal("hide");
        };

        vm.init();

        return vm;
    })



