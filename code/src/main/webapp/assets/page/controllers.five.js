angular.module('controllers.five', [])


    .controller("FiveDetailController", function ($state, $stateParams,$rootScope,$scope,businessContractService,edProjectTreeService) {
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

                        if(nodeId) {
                            node.state.selected=(node.id==nodeId);
                        }else if(stepId) {
                            if (item.nodeUrl.indexOf("stepShow")>0&& item.foreignKey===parseInt(stepId)) {
                                node.state.selected = true;
                                node.state.opened=true;
                            }else{
                                node.state.selected =false;
                            }
                        }
                        else{
                            node.state.selected=item.selected;
                        }
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
                            if (data.action === "select_node" || data.action === 'ready') {
                                var node = data.instance.get_node(data.selected[0]).data;
                                edProjectTreeService.rememberNodeState(node.id, true, 'selected', user.enLogin);

                                $state.go(node.nodeUrl, {nodeId: node.id});
                            }
                        }
                    });

                    $('#taskTree').on('select_node.jstree',function (e,data) {

                        if(data.node.state.opened&&(data.node.text.indexOf('阶段')>=0||data.node.data.nodeUrl.indexOf('five.detail.stepShow')>=0)){

                        }else{
                            $('#taskTree').jstree("toggle_node", "#"+data.node.id);
                        }
                    });

                    $('#taskTree').on('after_open.jstree',function (e,data) {
                        edProjectTreeService.rememberNodeState(data.node.id,true,'opened',user.userLogin)
                    });

                    $('#taskTree').on('after_close.jstree',function (e,data) {
                        edProjectTreeService.rememberNodeState(data.node.id,false,'opened',user.enLogin);
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
                    var treeData=value.data.data;
                    $('#taskTree').jstree("destroy");
                    $('#taskTree').jstree({
                        'core': {
                            'data':treeData
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

    .controller("FiveDetailPartialStepController", function ($state, $stateParams, $scope, edProjectService, edProjectTreeService, edProjectStepService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    edProjectStepService.checkIsChargeUser(vm.treeNode.projectId, 0, user.userLogin).then(function (value1) {
                        if (value1.data.ret) {
                            vm.isShow = value1.data.data;

                        }
                    })
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            edProjectStepService.listDataByStageNodeId(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.add = function () {
            edProjectStepService.getNewModel(nodeId, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.loadTree();
                    vm.show(value.data.data);
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.detail.stepDetail", {stepId: id});
        }

        vm.showCp = function (id) {
            $state.go("five.detail.co", {stepId: id});
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除该分期吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    edProjectStepService.remove(id, user.userLogin).then(function (value) {
                        vm.loadData();
                        $scope.loadTree();
                    });
                }
            })
        }
        vm.init();

        return vm;
    })
    .controller("FiveDetailPartialStepDetailController", function ($rootScope,$state, $stateParams,$scope,hrEmployeeService,edProjectStepService,commonEdBuildService) {
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
            edProjectStepService.getModelById(stepId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
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

                vm.item.operateUserLogin=user.userLoign;
                edProjectStepService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData();
                        setTimeout(function () {
                            $scope.loadTree();
                        },1500);
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
            bootbox.confirm("确认要删除该条数据吗,删除后协同的对应文件夹也会删除！",function (result) {
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
            if(vm.optUserType=='项目总师'){
                vm.item.chargeMan=$scope.selectedUserLogins_;
                vm.item.chargeManName=$scope.selectedUserNames_;
            }else if (vm.optUserType=='项目经理') {
                vm.item.exeChargeMan=$scope.selectedUserLogins_;
                vm.item.exeChargeManName=$scope.selectedUserNames_;
            }else if (vm.optUserType=='其它总师') {
                vm.item.otherChargeMan=$scope.selectedUserLogins_;
                vm.item.otherChargeManName=$scope.selectedUserNames_;
            }else if (vm.optUserType=='项目分管院长') {
                vm.item.projectChargeMan=$scope.selectedUserLogins_;
                vm.item.projectChargeManName=$scope.selectedUserNames_;
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
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;
    })

    .controller("FiveDetailPartialStepShowController", function ($state, $stateParams, $scope, edProjectTreeService, actService) {
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
            actService.getExploreStepTimeline(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.data = value.data.data;
                }
            });
        }

        vm.showStepDetail=function(){
            $state.go("five.detail.stepDetail", {stepId: vm.treeNode.foreignKey});
        }

        vm.init();
        return vm;
    })
    .controller("FiveDetailPartialShowController", function ($state, $stateParams, $scope, edProjectTreeService,actService) {
        var vm = this;
        var projectId = $stateParams.id;
        vm.loadData=function(){
            actService.getProjectTimeline(projectId).then(function (value) {
                vm.steps=value.data.data;
            })
        }
        vm.loadData();
        return vm;
    })

    .controller("FiveEdDesignRuleController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdDesignRuleService) {
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
            fiveEdDesignRuleService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.designRuleDetail", {designRuleId: id});
        }

        vm.add = function () {
            fiveEdDesignRuleService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdDesignRuleService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdDesignRuleDetailController", function ($state, $stateParams, $rootScope,$scope,edFileService, fiveEdDesignRuleService) {
        var vm = this;
        var designRuleId = $stateParams.designRuleId;

        vm.loadData = function (loadProcess) {
            fiveEdDesignRuleService.getModelById(designRuleId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdDesignRuleService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            edFileService.fileCheckByBusinessKey(vm.item.businessKey).then(function (value) {
                if (value.data.ret){
                    var map=value.data.data;
                    if (!map.isExist){
                        toastr.warning("请上传-"+map.lackType+"-文件")
                    }else {
                        if ($("#detail_form").validate().form()) {
                            vm.item.operateUserLogin = user.userLogin;
                            fiveEdDesignRuleService.update(vm.item).then(function (value) {
                                if (value.data.ret) {
                                    jQuery.showActHandleModal({
                                        taskId: $scope.processInstance.taskId,
                                        send: send,
                                        enLogin: user.enLogin
                                    }, function () {
                                        return true;
                                    }, function () {
                                        $scope.refresh();
                                    });
                                }
                            })
                        } else {
                            toastr.warning("请准确填写数据!")
                            return false;
                        }
                    }
                }
            })

        }

        vm.loadData(true);

        $scope.refresh = function () {
            vm.loadData(true);
        }

/*        vm.print=function () {
            fiveEdDesignRuleService.getPrintData(taskId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("设计与开发任务书");
                    var fileTypes=vm.printData.fileTypes;
                    var length=fileTypes.length;
                    if(fileTypes.length<12){
                        for(var i=0;i<12-length;i++){
                            var fileType={};
                            fileTypes.push(fileType);
                        }
                    }
                    vm.fileTypes=fileTypes;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + $("#print_area").html() + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }*/
        return vm;
    })

    .controller("FiveEdDesignSignController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdDesignSignService) {
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
            fiveEdDesignSignService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.designSignDetail", {designSignId: id});
        }

        vm.add = function () {
            fiveEdDesignSignService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdDesignSignService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdDesignSignDetailController", function ($state, $stateParams,$rootScope, $scope, fiveEdDesignSignService) {
        var vm = this;
        var designSignId = $stateParams.designSignId;

        vm.loadData = function (loadProcess) {
            fiveEdDesignSignService.getModelById(designSignId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdDesignSignService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };


        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdDesignSignService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                           $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showFile = function (item) {
            vm.file = $.extend({}, item);
            $("#edFileModal").modal("show");
        }

        vm.saveFile = function () {
            $("#edFileModal").modal("hide");
            fiveEdDesignSignService.updateFileType(vm.file.id, vm.file.fileType, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $scope.loadEdFile(vm.item.businessKey);
                }
            })
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })

    .controller("FiveEdCompanyReviewController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdCompanyReviewService) {
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
            fiveEdCompanyReviewService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.companyReviewDetail", {companyReviewId: id});
        }

        vm.add = function () {
            fiveEdCompanyReviewService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdCompanyReviewService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdCompanyReviewDetailController", function ($state, $stateParams, $rootScope,$scope, fiveEdCompanyReviewService) {
        var vm = this;
        var companyReviewId = $stateParams.companyReviewId;

        vm.loadData = function (loadProcess) {
            fiveEdCompanyReviewService.getModelById(companyReviewId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                    $("#applyTime").datepicker('setDate', vm.item.applyTime);
                    $("#reviewTime").datepicker('setDate', vm.item.reviewTime);
                    $("#chargeTime").datepicker('setDate', vm.item.chargeTime)
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdCompanyReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };


        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdCompanyReviewService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showFile = function (item) {
            vm.file = $.extend({}, item);
            $("#edFileModal").modal("show");
        }

        vm.saveFile = function () {
            $("#edFileModal").modal("hide");
            fiveEdCompanyReviewService.updateFileType(vm.file.id, vm.file.fileType, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $scope.loadEdFile(vm.item.businessKey);
                }
            })
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
         vm.print=function () {
             fiveEdCompanyReviewService.getPrintData(companyReviewId).then(function (value) {
                    if(value.data.ret){
                        lodop=getLodop();
                        vm.printData=value.data.data;
                        lodop.PRINT_INIT("公司级设计评审申报表");
    /*                  var fileTypes=vm.printData.fileTypes;
                        var length=fileTypes.length;
                        if(fileTypes.length<12){
                            for(var i=0;i<12-length;i++){
                                var fileType={};
                                fileTypes.push(fileType);
                            }
                        }
                        vm.fileTypes=fileTypes;*/
                        var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                        setTimeout(function () {
                            var strFormHtml =strBodyStyle+ "<body>" + $("#print_area").html() + "</body>";
                            lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                            lodop.PREVIEW();
                        }, 500);
                    }
                })
            }
        return vm;
    })

    .controller("FiveEdSpecialReviewController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdSpecialReviewService) {
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
            fiveEdSpecialReviewService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.specialReviewDetail", {specialReviewId: id});
        }

        vm.add = function () {
            fiveEdSpecialReviewService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdSpecialReviewService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdSpecialReviewDetailController", function ($state, $stateParams, $rootScope,$scope, fiveEdSpecialReviewService,edFileService) {

        var vm = this;
        var specialReviewId = $stateParams.specialReviewId;

        vm.loadData = function (loadProcess) {
            fiveEdSpecialReviewService.getModelById(specialReviewId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);

                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdSpecialReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };


        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdSpecialReviewService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showFile = function (item) {
            vm.file = $.extend({}, item);
            $("#edFileModal").modal("show");
        }

        vm.saveFile = function () {
            $("#edFileModal").modal("hide");
            fiveEdSpecialReviewService.updateFileType(vm.file.id, vm.file.fileType, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $scope.loadEdFile(vm.item.businessKey);
                }
            })
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
        /*        vm.print=function () {
                    fiveEdProjectReviewService.getPrintData(projectReviewId).then(function (value) {
                        if(value.data.ret){
                            lodop=getLodop();
                            vm.printData=value.data.data;
                            lodop.PRINT_INIT("总体方案评审");
                            /!*                  var fileTypes=vm.printData.fileTypes;
                                                var length=fileTypes.length;
                                                if(fileTypes.length<12){
                                                    for(var i=0;i<12-length;i++){
                                                        var fileType={};
                                                        fileTypes.push(fileType);
                                                    }
                                                }
                                                vm.fileTypes=fileTypes;*!/
                            var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                            setTimeout(function () {
                                var strFormHtml =strBodyStyle+ "<body>" + $("#print_area").html() + "</body>";
                                lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                                lodop.PREVIEW();
                            }, 500);
                        }
                    })
                }
                return vm;*/
    })

    .controller("FiveEdExpertReviewController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdExpertReviewService) {
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
            fiveEdExpertReviewService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.expertReviewDetail", {expertReviewId: id});
        }

        vm.add = function () {
            fiveEdExpertReviewService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdExpertReviewService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdExpertReviewDetailController", function ($state, $stateParams, $scope,$rootScope, fiveEdExpertReviewService) {
        var vm = this;
        var expertReviewId = $stateParams.expertReviewId;

        vm.loadData = function (loadProcess) {
            fiveEdExpertReviewService.getModelById(expertReviewId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                    $("#time").datepicker('setDate', vm.item.time);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdExpertReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdExpertReviewService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.saveFile = function () {
            $("#edFileModal").modal("hide");
            fiveEdExpertReviewService.updateFileType(vm.file.id, vm.file.fileType, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $scope.loadEdFile(vm.item.businessKey);
                }
            })
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.print=function () {
            fiveEdExpertReviewService.getPrintData(expertReviewId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("专家评审意见表");
/*                  var fileTypes=vm.printData.fileTypes;
                    var length=fileTypes.length;
                    if(fileTypes.length<12){
                        for(var i=0;i<12-length;i++){
                            var fileType={};
                            fileTypes.push(fileType);
                        }
                    }
                    vm.fileTypes=fileTypes;*/
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + $("#print_area").html() + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        return vm;
    })

    .controller("FiveEdOutReviewController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdOutReviewService) {
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
            fiveEdOutReviewService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.outReviewDetail", {outReviewId: id});
        }

        vm.add = function () {
            fiveEdOutReviewService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdOutReviewService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdOutReviewDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdOutReviewService,edFileService) {
        var vm = this;
        var outReviewId = $stateParams.outReviewId;

        vm.loadData = function (loadProcess) {
            fiveEdOutReviewService.getModelById(outReviewId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdOutReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            edFileService.fileCheckByBusinessKey(vm.item.businessKey).then(function (value) {
                if (value.data.ret){
                    var map=value.data.data;
                    if (!map.isExist){
                        toastr.warning("请上传-"+map.lackType+"-文件")
                    }else {
                        if ($("#detail_form").validate().form()) {
                            vm.item.operateUserLogin = user.userLogin;
                            fiveEdOutReviewService.update(vm.item).then(function (value) {
                                if (value.data.ret) {
                                    jQuery.showActHandleModal({
                                        taskId: $scope.processInstance.taskId,
                                        send: send,
                                        enLogin: user.enLogin
                                    }, function () {
                                        return true;
                                    }, function (processInstanceId) {
                                       $scope.refresh();
                                    });
                                }
                            })
                        } else {
                            toastr.warning("请准确填写数据!")
                            return false;
                        }
                    }
                }
            })

        }

        vm.showFile = function (item) {
            vm.file = $.extend({}, item);
            $("#edFileModal").modal("show");
        }

        vm.saveFile = function () {
            $("#edFileModal").modal("hide");
            fiveEdOutReviewService.updateFileType(vm.file.id, vm.file.fileType, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $scope.loadEdFile(vm.item.businessKey);
                }
            })
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
    })

    .controller("FiveEdServiceChangeController", function ($state, $stateParams, $scope, fiveEdServiceChangeService, edProjectTreeService) {
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
            fiveEdServiceChangeService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.add = function () {
            fiveEdServiceChangeService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdServiceChangeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show = function (serviceChangeId) {
            $state.go("five.detail.serviceChangeDetail", {serviceChangeId: serviceChangeId});
        }

        vm.init();

        return vm;
    })
    .controller("FiveEdServiceChangeDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdServiceChangeService,fiveEdArrangeUserService) {
        var vm = this;
        var serviceChangeId = $stateParams.serviceChangeId;

        vm.loadData = function (loadProcess) {
            fiveEdServiceChangeService.getModelById(serviceChangeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadEdFiles(vm.item.businessKey);
                    }
                    fiveEdServiceChangeService.listMajorCode(vm.item.id).then(function (value) {
                        if(value.data.ret){
                            vm.majorNames=value.data.data;
                        }
                    });
                    fiveEdArrangeUserService.listEdValidateUserByLogin(vm.item.stepId,user.userLogin).then(function (value) {
                        if(value.data.ret){
                            vm.arrangeUsers=value.data.data;
                        }
                    });
                }

            })
        };

        vm.showArrangeUserModal=function(){
            singleCheckBox(".cb_arrange","data-name");
            $("#arrangeUserModal").modal("show");
        }

        vm.saveSelectMajor=function(){
            if($(".cb_arrange:checked").length>0) {
                var arrange = $.parseJSON($(".cb_arrange:checked").first().attr("data-name"));
                fiveEdServiceChangeService.resetMajorName(serviceChangeId,arrange.id).then(function (value) {
                    if(value.data.ret){
                        $("#arrangeUserModal").modal("hide");
                        toastr.success("保存成功");
                        vm.loadData();

                    }
                })
            }
        }

        vm.showReasonModel=function(){
            $("#Date1").datepicker('setDate', new Date());
            $("#Date2").datepicker('setDate', new Date());
            $("#contractStageNameModel").modal("show");

        }

        vm.showSelectDestMajorModel=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.arrangeUsers=value.data.data;
            })
            $("#selectDestMajorModal").modal("show");

        }
        vm.selectAllFile=function(){
            $(".cb_destMajor").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_destMajor").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    var checked=$(this).attr("checked");
                    if(checked) {
                        $(this).removeAttr("checked");
                    }else{
                        $(this).attr("checked", true);
                    }
                }
            })
        }
        vm.selectDestMajor=function(){
            var majors = [];
            $(".cb_destMajor:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            if (vm.opt=='majorName'){
                vm.item.majorName = majors.join(',');
            } else {
                vm.item.needChangeMajor = majors.join(',');
            }
            $("#selectDestMajorModal").modal("hide");
        }

        vm.selectStageName = function () {
            var stageNames = [];
            $(".cb_stage:checked").each(function () {
                stageNames.push($(this).attr("name"));
            });
            vm.item.stageNames = stageNames.join(',');
            $("#contractStageNameModel").modal("hide");
        }
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdServiceChangeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdServiceChangeService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                           $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.print=function () {
            fiveEdServiceChangeService.getPrintData(serviceChangeId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("设计过程函件");//替换
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        return vm;
    })

    .controller("FiveEdServiceDiscussController", function ($state, $stateParams, $scope, fiveEdServiceDiscussService, edProjectTreeService) {
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
            fiveEdServiceDiscussService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.add = function () {
            fiveEdServiceDiscussService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdServiceDiscussService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show = function (serviceChangeId) {
            $state.go("five.detail.serviceDiscussDetail", {serviceDiscussId: serviceChangeId});
        }

        vm.init();

        return vm;
    })
    .controller("FiveEdServiceDiscussDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdServiceDiscussService,fiveEdArrangeUserService) {
        var vm = this;
        var serviceDiscussId = $stateParams.serviceDiscussId;

        vm.loadData = function (loadProcess) {
            fiveEdServiceDiscussService.getModelById(serviceDiscussId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadEdFiles(vm.item.businessKey);
                    }
                    fiveEdServiceDiscussService.listMajorCode(vm.item.id).then(function (value) {
                        if(value.data.ret){
                            vm.majorNames=value.data.data;
                        }
                    });

                }

            })
        };
        vm.showReasonModel=function(){
            $("#Date1").datepicker('setDate', new Date());
            $("#Date2").datepicker('setDate', new Date());
            $("#contractStageNameModel").modal("show");

        }

        vm.showSelectNeedChangeMajorModel=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.arrangeUsers=value.data.data;
            })
            $("#selectNeedChangeMajorModal").modal("show");

        }
        vm.selectAllFile=function(){
            $(".cb_needChangeMajor").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_needChangeMajor").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    var checked=$(this).attr("checked");
                    if(checked) {
                        $(this).removeAttr("checked");
                    }else{
                        $(this).attr("checked", true);
                    }
                }
            })
        }
        vm.selectNeedChangeMajor=function(){
            var majors = [];
            $(".cb_needChangeMajor:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            vm.item.majorName = majors.join(',');
            $("#selectNeedChangeMajorModal").modal("hide");
        }

        vm.selectStageName = function () {
            var stageNames = [];
            $(".cb_stage:checked").each(function () {
                stageNames.push($(this).attr("name"));
            });
            vm.item.stageNames = stageNames.join(',');
            $("#contractStageNameModel").modal("hide");
        }
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdServiceDiscussService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }



        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdServiceDiscussService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                           $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.print=function () {
            fiveEdServiceDiscussService.getPrintData(serviceDiscussId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("建设、施工单位变更设计洽商单");//替换
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        return vm;
    })

    .controller("FiveEdServiceHandleController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdServiceHandleService) {
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
            fiveEdServiceHandleService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {

            $state.go("five.detail.serviceHandleDetail", {serviceHandleId: id});
        }

        vm.add = function () {
            fiveEdServiceHandleService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdServiceHandleService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdServiceHandleDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdServiceHandleService,edFileService) {
        var vm = this;
        var serviceHandleId = $stateParams.serviceHandleId;

        vm.loadData = function (loadProcess) {
            fiveEdServiceHandleService.getModelById(serviceHandleId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                    $("#registerTime").datepicker('setDate', vm.item.registerTime);
                    $("#replyTime").datepicker('setDate', vm.item.replyTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdServiceHandleService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdServiceHandleService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                           $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showFile = function (item) {
            vm.file = $.extend({}, item);
            $("#edFileModal").modal("show");
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.print=function () {
            fiveEdServiceHandleService.getPrintData(serviceHandleId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("技术服务问题处理单");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + $("#print_area").html() + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        return vm;

    })

    .controller("FiveEdQualityIssueController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdQualityIssueService) {
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
            fiveEdQualityIssueService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {

            $state.go("five.detail.qualityIssueDetail", {qualityIssueId: id});
        }

        vm.add = function () {
            fiveEdQualityIssueService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdQualityIssueService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdQualityIssueDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdQualityIssueService) {
        var vm = this;
        var qualityIssueId = $stateParams.qualityIssueId;

        vm.loadData = function (loadProcess) {
            fiveEdQualityIssueService.getModelById(qualityIssueId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                    $("#registerTime").datepicker('setDate', vm.item.registerTime);
                    $("#replyTime").datepicker('setDate', vm.item.replyTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdQualityIssueService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };



        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdQualityIssueService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                           $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.print=function () {
            fiveEdQualityIssueService.getPrintData(qualityIssueId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("设计质量问题登记");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + $("#print_area").html() + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        return vm;

    })

    .controller("FiveEdReturnVisitController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdReturnVisitService) {
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
            fiveEdReturnVisitService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {

            $state.go("five.detail.returnVisitDetail", {returnVisitId: id});
        }

        vm.add = function () {
            fiveEdReturnVisitService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdReturnVisitService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdReturnVisitDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdReturnVisitService) {
        var vm = this;
        var returnVisitId = $stateParams.returnVisitId;

        vm.loadData = function (loadProcess) {
            vm.loadDetail();
            fiveEdReturnVisitService.getModelById(returnVisitId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);
                    $("#designFinishTime").datepicker('setDate', vm.item.designFinishTime);
                    $("#constructionFinishTime").datepicker('setDate', vm.item.constructionFinishTime);
                    $("#visitTime").datepicker('setDate', vm.item.visitTime);

                }
            })
        };

        vm.loadDetail = function() {
            fiveEdReturnVisitService.listDetailByVisitId(returnVisitId).then(function (value) {
                if (value.data.ret) {
                    vm.visitDetails = value.data.data;
                }
            })
        }

        vm.showDetail = function(detail) {
            bootbox.prompt(
                {
                    title:detail.appraiseType,
                    value:detail.appraiseResult,
                    inputType: 'select',
                    inputOptions: [
                        {
                            text: '很满意',
                            value: '很满意',
                        },
                        {
                            text: '满意',
                            value: '满意',
                        },
                        {
                            text: '较满意',
                            value: '较满意',
                        },
                        {
                            text: '基本满意',
                            value: '基本满意',
                        },
                        {
                            text: '不满意',
                            value: '不满意',
                        }
                    ],

                    callback:function(result){
                        if(result) {
                            fiveEdReturnVisitService.updateDetail(detail.id,result).then(function(){
                                vm.loadDetail();
                            });
                        }
                    }});
        }

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdReturnVisitService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };


        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdReturnVisitService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                           $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.print=function () {
            fiveEdReturnVisitService.getPrintData(returnVisitId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("工程设计回访记录");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + $("#print_area").html() + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        return vm;

    })

    .controller("FiveEdReviewMajorController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdReviewMajorService) {
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
            fiveEdReviewMajorService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (reviewMajorId) {
            $state.go("five.detail.reviewMajorDetail", {reviewMajorId: reviewMajorId});
        }

        vm.add = function () {
            fiveEdReviewMajorService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdReviewMajorService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveEdReviewMajorDetailController", function ( $state, $stateParams,$scope, $rootScope,edFileService,fiveEdReviewMajorService,fiveEdArrangeUserService) {
        var vm = this;
        var reviewMajorId = $stateParams.reviewMajorId;

        vm.init=function() {
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveEdReviewMajorService.getModelById(reviewMajorId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }

            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdReviewMajorService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        vm.showArrangeUserModal=function(){
            //全选 反选 需要数据更新
            fiveEdArrangeUserService.listEdValidateUserByLogin(vm.item.stepId,user.userLogin).then(function (value) {
                vm.arrangeUsers=value.data.data;
            })
            singleCheckBox(".cb_arrange","data-name");
            $("#arrangeUserModal").modal("show");
        }

        vm.selectAllFile=function(){
            $(".cb_arrange").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_arrange").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    var checked=$(this).attr("checked");
                    if(checked) {
                        $(this).removeAttr("checked");
                    }else{
                        $(this).attr("checked", true);
                    }
                }
            })
        }

        vm.saveSelectMajor=function(){
            if($(".cb_arrange:checked").length>0) {
                var arrange = $.parseJSON($(".cb_arrange:checked").first().attr("data-name"));
                fiveEdReviewMajorService.resetMajorName(reviewMajorId,arrange.id).then(function (value) {
                    if(value.data.ret){
                        $("#arrangeUserModal").modal("hide");
                        toastr.success("保存成功");
                        vm.loadData();
                    }
                })
            }
        }

        vm.showUserModel = function () {
            if (vm.opt =='attendUser'){
                $scope.showSelectEmployeeModal_({title:"参与人选择", userLoginList: vm.item.hostMen,multiple:true});
            }else if (vm.opt =='hostMen'){
                $scope.showSelectEmployeeModal_({title:"主持人选择", userLoginList: vm.item.hostMen,multiple:false});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            if (vm.opt =='attendUser'){
                $scope.closeSelectEmployeeModal_();
                vm.item.attendUser = $scope.selectedUserLogins_;
                vm.item.attendUserName =  $scope.selectedUserNames_;
            }else if (vm.opt =='hostMen'){
                $scope.closeSelectEmployeeModal_();
                vm.item.hostMen = $scope.selectedUserLogins_;
                vm.item.hostMenName =  $scope.selectedUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdReviewMajorService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                           $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.init();
    })

    .controller("FiveEdReviewPlanController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdReviewPlanService) {
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
            fiveEdReviewPlanService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.reviewPlanDetail", {reviewPlanId: id});
        }

        vm.add = function () {
            fiveEdReviewPlanService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdReviewPlanService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveEdReviewPlanDetailController", function ($state, $stateParams, $scope, $rootScope,edFileService,fiveEdReviewPlanService) {
        var vm = this;
        var reviewPlanId = $stateParams.reviewPlanId;

        vm.loadData = function (loadProcess) {
            fiveEdReviewPlanService.getModelById(reviewPlanId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#finishTime").datepicker('setDate', vm.item.finishTime);

                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdReviewPlanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdReviewPlanService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $scope.refresh();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showUserModel = function () {
            if (vm.opt =='attendUser'){
                $scope.showSelectEmployeeModal_({title:"参与人选择", userLoginList: vm.item.hostMen,multiple:true});
            }else if (vm.opt =='hostMen'){
                $scope.showSelectEmployeeModal_({title:"主持人选择", userLoginList: vm.item.hostMen,multiple:false});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            if (vm.opt =='attendUser'){
                $scope.closeSelectEmployeeModal_();
                vm.item.attendUser = $scope.selectedUserLogins_;
                vm.item.attendUserName =  $scope.selectedUserNames_;
            }else if (vm.opt =='hostMen'){
                $scope.closeSelectEmployeeModal_();
                vm.item.hostMen = $scope.selectedUserLogins_;
                vm.item.hostMenName =  $scope.selectedUserNames_;
            }

        };

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
    })


