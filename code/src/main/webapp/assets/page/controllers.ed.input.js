angular.module('controllers.ed.input', [])


    .controller("EdDetailPartialInputController", function ($state, $stateParams, $scope,edProjectTreeService,edInputAcService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputAcService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }



        vm.show=function(id){
            $state.go("ed.detail.edInputAcDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })


    .controller("EdDetailPartialInputAcController", function ($state, $stateParams, $scope,edProjectTreeService,edInputAcService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };


        vm.loadData=function(){
            edInputAcService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputAcService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputAcService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }
        vm.show=function(id){
            $state.go("ed.detail.inputAcDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputAcDetailController", function ($state, $stateParams, $scope,edInputAcService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin=user.userLogin;
                    edInputAcService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })

                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }

            }else {
                $scope.showSendFlow();
            }
        }

        vm.loadData = function (loadProcess) {
            edInputAcService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                    }
                    $("#contractReceiveTime").datepicker('setDate', vm.item.contractReceiveTime);
                    $("#reviewReceiveTime").datepicker('setDate', vm.item.reviewReceiveTime);
                    $("#projectApproveReceiveTime").datepicker('setDate', vm.item.projectApproveReceiveTime);
                    $("#redLineReceiveTime").datepicker('setDate', vm.item.redLineReceiveTime);
                    $("#planLicenseReceiveTime").datepicker('setDate', vm.item.planLicenseReceiveTime);
                    $("#customerQualityReceiveTime").datepicker('setDate', vm.item.customerQualityReceiveTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputAcService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                }
            })
        }

        /*打印*/
        vm.print=function () {
            edInputAcService.getPrintData(inputId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("输入清单评审（建筑）");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        vm.exportData=function() {
            edInputAcService.exportData(inputId).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        }

        vm.loadData(true);

        return vm;
    })


    .controller("EdDetailPartialInputConstructionController", function ($state, $stateParams, $scope,edProjectTreeService,edInputConstructionService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputConstructionService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputConstructionService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputConstructionService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputConstructionDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputConstructionDetailController", function ($state, $stateParams, $scope,edInputConstructionService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;
        var businessId = "edInputConstruction_" + inputId;

        vm.init = function () {
            vm.loadData(true);
            $scope.basicInit(businessId);
            $scope.loadEdFile(businessId);
            vm.loadComment(businessId);
        };

        vm.loadData = function (loadProcess) {
            edInputConstructionService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputConstructionService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.loadComment=function(){
            edCommentService.listDataByBusinessId(businessId).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;
                }
            })
        };

        vm.showComment=function(comment){
            bootbox.prompt({title:comment.baseName,value:comment.baseComment,callback:function(result){
                    if(result) {
                        vm.saveComment(comment.id, result);
                    }
                }});
            //
        }

        vm.saveComment=function(id,baseComment){
            edCommentService.update(id,baseComment).then(function(){
                vm.loadComment();
            });
        }

        vm.init();

        return vm;
    })


    .controller("EdDetailPartialInputDeController", function ($state, $stateParams, $scope,edProjectTreeService,edInputDeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputDeService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputDeService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputDeService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputDeDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputDeDetailController", function ($state, $stateParams, $scope,edInputDeService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputDeService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    vm.loadComment(vm.item.businessKey);
                }
            })
        };

        //发送流程
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edInputDeService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                }else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }else{
                $scope.showSendFlow();
            }
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputDeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.loadComment=function(businessKey){
            edCommentService.listDataByBusinessId(businessKey).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;
                }
            })
        };
        vm.showDetail=function(id){
            edCommentService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                }
            })
            $("#detailModal").modal("show");
        }
        vm.saveDetail=function(){
            if ($("#comment_form").validate().form()) {
                edCommentService.update(vm.detail).then(function () {
                    vm.loadComment(vm.item.businessKey);
                });
                $("#detailModal").modal("hide");
            }
        };
        vm.addDetail=function(){
            edCommentService.getNewModel(vm.item.businessKey).then(function(value){
                if (value.data.ret) {
                    vm.showDetail(value.data.data);
                }

            });
        };
        vm.removeDetail=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edCommentService.remove(id).then(function (value) {
                        if(value.data.ret) {
                            vm.loadComment(vm.item.businessKey);
                        }
                    })
                }
            })
        }
        vm.exportData=function() {
            edInputDeService.exportData(inputId).then(function (response) {

            var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
            var contentDisposition = response.data.headers['content-disposition'];
            var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = decodeURI(fileName);
            a.href = objectUrl;
            a.click();
        });
}

vm.print=function () {
            edInputDeService.getPrintData(inputId).then(function (value) {
            if(value.data.ret){
                lodop=getLodop();
                vm.printData=value.data.data;
                lodop.PRINT_INIT("设计与开发输入清单及评审记录（电气）");//替换
                var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                setTimeout(function () {
                    var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                    lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                    lodop.PREVIEW();
                }, 500);
            }
        })
}

        vm.loadData(true);

        return vm;
    })

    .controller("EdDetailPartialInputHvacController", function ($state, $stateParams, $scope,edProjectTreeService,edInputHvacService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputHvacService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputHvacService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputHvacService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputHvacDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputHvacDetailController", function ($state, $stateParams, $scope,edInputHvacService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputHvacService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    vm.loadComment(vm.item.businessKey);
                }
            })
        };

        //发送流程
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edInputHvacService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                }else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }else{
                $scope.showSendFlow();
            }
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputHvacService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.loadComment=function(businessKey){
            edCommentService.listDataByBusinessId(businessKey).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;
                }
            })
        };
        vm.showDetail=function(id){
            edCommentService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                }
            })
            $("#detailModal").modal("show");
        }
        vm.saveDetail=function(){
            if ($("#comment_form").validate().form()) {
                edCommentService.update(vm.detail).then(function () {
                    vm.loadComment(vm.item.businessKey);
                });
                $("#detailModal").modal("hide");
            }
        };
        vm.addDetail=function(){
            edCommentService.getNewModel(vm.item.businessKey).then(function(value){
                if (value.data.ret) {
                    vm.showDetail(value.data.data);
                }

            });
        };
        vm.removeDetail=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edCommentService.remove(id).then(function (value) {
                        if(value.data.ret) {
                            vm.loadComment(vm.item.businessKey);
                        }
                    })
                }
            })
        }
        vm.loadData(true);

        return vm;
    })

    .controller("EdDetailPartialInputWaterController", function ($state, $stateParams, $scope,edProjectTreeService,edInputWaterService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputWaterService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputWaterService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputWaterService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputWaterDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputWaterDetailController", function ($state, $stateParams, $scope,edInputWaterService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputWaterService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    vm.loadComment(vm.item.businessKey);
                }
            })
        };

        //发送流程
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edInputWaterService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                }else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }else{
                $scope.showSendFlow();
            }
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputWaterService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.loadComment=function(businessKey){
            edCommentService.listDataByBusinessId(businessKey).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;
                }
            })
        };
        vm.showDetail=function(id){
            edCommentService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                }
            })
            $("#detailModal").modal("show");
        }
        vm.saveDetail=function(){
            if ($("#comment_form").validate().form()) {
                edCommentService.update(vm.detail).then(function () {
                    vm.loadComment(vm.item.businessKey);
                });
                $("#detailModal").modal("hide");
            }
        };
        vm.addDetail=function(){
            edCommentService.getNewModel(vm.item.businessKey).then(function(value){
                if (value.data.ret) {
                    vm.showDetail(value.data.data);
                }

            });
        };
        vm.removeDetail=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edCommentService.remove(id).then(function (value) {
                        if(value.data.ret) {
                            vm.loadComment(vm.item.businessKey);
                        }
                    })
                }
            })
        }
        vm.loadData(true);

        return vm;
    })


    .controller("EdDetailPartialInputGsController", function ($state, $stateParams, $scope,edProjectTreeService,edInputGsService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputGsService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputGsService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputGsService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputGsDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputGsDetailController", function ($state, $stateParams, $scope,edInputGsService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputGsService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                    }
                }
            })
        };

        //发送流程
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    if (vm.item.betonType=='无'&&vm.item.mixType=='无'&&vm.item.brickworkType=='无'&&vm.item.steelType=='无'&&vm.item.otherType==''){
                        toastr.warning("混泥土，混合，砌体，钢，其他结构至少一个不为空!")
                        return;
                    }
                    edInputGsService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                }else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }else{
                $scope.showSendFlow();
            }
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputGsService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /*打印*/
        vm.print=function () {
            edInputGsService.getPrintData(inputId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("输入清单评审（结构）");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        vm.exportData=function() {
            edInputGsService.exportData(inputId).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        }
        vm.loadData(true);

        return vm;
    })


    .controller("EdDetailPartialInputGovController", function ($state, $stateParams, $scope,edProjectTreeService,edInputGovService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputGovService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputGovService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputGovService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputGovDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputGovDetailController", function ($state, $stateParams, $scope,edInputGovService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputGovService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputGovService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        /**
         * 发送流程
         * */
        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }

        vm.loadData(true);

        return vm;
    })


    .controller("EdDetailPartialInputHouseDesignController", function ($state, $stateParams, $scope,edProjectTreeService,edInputHouseDesignService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputHouseDesignService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputHouseDesignService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputHouseDesignService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputHouseDesignDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputHouseDesignDetailController", function ($state, $stateParams, $scope,edInputHouseDesignService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputHouseDesignService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputHouseDesignService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.loadComment=function(){
            edCommentService.listDataByBusinessId(businessId).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;
                }
            })
        };

        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }

        vm.showComment=function(comment){
            bootbox.prompt({title:comment.baseName,value:comment.baseComment,callback:function(result){
                    if(result) {
                        vm.saveComment(comment.id, result);
                    }
                }});
            //
        }

        vm.saveComment=function(id,baseComment){
            edCommentService.update(id,baseComment).then(function(){
                vm.loadComment();
            });
        }

        vm.loadData(true);

        return vm;
    })

    .controller("EdDetailPartialInputPlanController", function ($state, $stateParams, $scope,edProjectTreeService,edInputPlanService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputPlanService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputPlanService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputPlanService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputPlanDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputPlanDetailController", function ($state, $stateParams, $scope,edInputPlanService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputPlanService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                        vm.loadComment(vm.item.businessKey);
                    }
                }
            })
        };
        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputPlanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        vm.loadComment=function(){
            edCommentService.listDataByBusinessId(businessId).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;
                }
            })
        };

        vm.showComment=function(comment){
            bootbox.prompt({title:comment.baseName,value:comment.baseComment,callback:function(result){
                if(result) {
                    vm.saveComment(comment.id, result);
                }
                }});
            //
        }

        vm.saveComment=function(id,baseComment){
            edCommentService.update(id,baseComment).then(function(){
                vm.loadComment();
            });
        }

        vm.loadData(true);

        return vm;
    })

    .controller("EdDetailPartialInputApproveController", function ($state, $stateParams, $scope,edProjectTreeService,edInputApproveService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputApproveService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputApproveService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputApproveService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputApproveDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputApproveDetailController", function ($state, $stateParams, $scope,edInputApproveService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;


        vm.loadData = function (loadProcess) {
            edInputApproveService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;

                    if(vm.item.stageName == '前期咨询阶段'){
                        vm.fileRemark ="应上传资料： 依据文件、主管部门的立项批复、规划选址意见书、规划设计红线、设计委托书、气象\\水文\\地震等资料、工程设计条件要求通知书、总包单位经济意见、其他要求"
                    }else if(vm.item.stageName == '初步设计阶段'){
                        vm.fileRemark ="应上传资料： 依据文件、规划部门审查意见、消防部门审查意见、人防部门审查意见、环保部门审查意见、园林绿化审查意见、人防工程审查意见、市政设施接口资料、地质灾害评价报告、地勘报告、环境影响评价报告、总包单位经济意见、顾客其他要求、其他要求"
                    }else if(vm.item.stageName == '施工图设计阶段'){
                        vm.fileRemark ="应上传资料： 依据文件、初步设计审查意见、综合管网审查意见、初步消防审查意见、园林绿化审查意见、人防工程审查意见、地勘报告、防雷审查意见、顾客的特殊要求、总包单位经济意见、其他要求"
                    }
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        vm.loadComment(vm.item.businessKey);

                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputApproveService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        vm.exportData=function() {
            edInputApproveService.exportData(inputId).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        }

        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edInputApproveService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        };

        vm.loadComment=function(businessKey){
            edCommentService.listDataByBusinessId(businessKey).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;

                }

            })
        };

        vm.showDetail=function(id){
            edCommentService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                }
            })
            $("#detailModal").modal("show");
        }
        vm.saveDetail=function(){
            if ($("#comment_form").validate().form()) {
                edCommentService.update(vm.detail).then(function () {
                    vm.loadComment(vm.item.businessKey);
                });
                $("#detailModal").modal("hide");
            }
        };
        vm.addDetail=function(){
            edCommentService.getNewModel(vm.item.businessKey).then(function(value){
                if (value.data.ret) {
                    vm.showDetail(value.data.data);
                }

            });
        };
        vm.removeDetail=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edCommentService.remove(id).then(function (value) {
                        if(value.data.ret) {
                            vm.loadComment(vm.item.businessKey);
                        }
                    })
                }
            })
        }

        vm.print=function () {
            edInputApproveService.getPrintData(inputId).then(function (value) {
            if(value.data.ret){
                vm.printData=value.data.data;

                var edComments=vm.printData.edComments;
                var length=edComments.length;
                if(edComments.length<10) {
                    for (var i = 0; i < 10 - length; i++) {
                        var edComment = {};
                        edComments.push(edComment);
                    }
                }
                vm.edComments=edComments;

                lodop=getLodop();

                lodop.PRINT_INIT("设计与开发输入清单及评审记录(部门批文)");
                var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                setTimeout(function () {
                    var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                    lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);

                    lodop.PREVIEW();
                }, 500);
            }
        })
}

        vm.loadData(true);

        return vm;
    })

    .controller("EdDetailPartialInputReviewController", function ($state, $stateParams, $scope,edProjectTreeService,edInputReviewService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputReviewService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputReviewService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputReviewService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputReviewDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputReviewDetailController", function ($state,$stateParams, $scope,edInputReviewService,edCommentService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.init = function () {
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            edInputReviewService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        vm.loadComment(vm.item.businessKey);

                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //发送流程
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edInputReviewService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                }else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }else{
                $scope.showSendFlow();
            }
        };

        vm.loadComment=function(businessKey){
            edCommentService.listDataByBusinessId(businessKey).then(function (value) {
                if (value.data.ret) {
                    vm.comments = value.data.data;
                }
            })
        };
        vm.showDetail=function(id){

            edCommentService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                }
            })
            $("#detailModal").modal("show");
        }
        vm.saveDetail=function(){
            if ($("#comment_form").validate().form()) {
                edCommentService.update(vm.detail).then(function () {
                    vm.loadComment(vm.item.businessKey);
                });
                $("#detailModal").modal("hide");
            }
        };
        vm.addDetail=function(){
            edCommentService.getNewModel(vm.item.businessKey).then(function(value){
                if (value.data.ret) {
                    vm.showDetail(value.data.data);
                }

            });
        };
        vm.removeDetail=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edCommentService.remove(id).then(function (value) {
                        if(value.data.ret) {
                            vm.loadComment(vm.item.businessKey);
                        }
                    })
                }
            })
        }



        vm.print=function () {
            edInputReviewService.getPrintData(inputId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    var edComments=vm.printData.edComments;
                    var length=edComments.length;
                    if(edComments.length<12) {
                        for (var i = 0; i < 12 - length; i++) {
                            var edComment = {};
                            edComments.push(edComment);
                        }
                    }
                    vm.edComments=edComments;
                    lodop.PRINT_INIT("设计与开发输入清单及评审记录(审查意见)");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.exportData=function() {
            edInputReviewService.exportData(inputId).then(function (response) {
            var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
            var contentDisposition = response.data.headers['content-disposition'];
            var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = decodeURI(fileName);
            a.href = objectUrl;
            a.click();
        });
 }

vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputRoadController", function ($state, $stateParams, $scope,edProjectTreeService,edInputRoadService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            edInputRoadService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            edInputRoadService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edInputRoadService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("ed.detail.inputRoadDetail",{inputId:id});
        }

        vm.init();

        return vm;
    })

    .controller("EdDetailPartialInputRoadDetailController", function ($state, $stateParams, $scope,edInputRoadService) {
        var vm = this;
        var inputId = $stateParams.inputId;

        vm.loadData = function (loadProcess) {
            edInputRoadService.getModelById(inputId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin=user.userLogin;
            edInputRoadService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         * */
        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edInputRoadService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }else{
                $scope.showSendFlow();
            }

        }

        vm.print=function () {
            edInputRoadService.getPrintData(inputId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("设计与开发输入清单及评审记录(道路)");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        vm.loadData(true);

        return vm;
    })
