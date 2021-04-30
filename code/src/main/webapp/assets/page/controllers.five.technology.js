angular.module('controllers.five.technology', [])

    .controller("FiveTechnologyReturnVisitController", function ($state, $stateParams, $scope, fiveEdReturnVisitService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.detail.returnVisit";

        vm.init = function () {
          vm.loadPagedData();
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};

            fiveEdReturnVisitService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.technologyReturnVisitDetail", {returnVisitId: id});
        }

        vm.add = function () {
            fiveEdReturnVisitService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveEdReturnVisitService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.init();

        return vm;
    })

    .controller("FiveTechnologyReturnVisitDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdReturnVisitService) {
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
                        }, function (processInstanceId,result) {
                            if (result.completeTask){
                                $scope.back();
                            }else {
                                $scope.refresh();
                            }
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

        vm.loadData(true);

        return vm;

    })

    .controller("FiveTechnologyQualityCheckController", function ($state, $stateParams, $scope, fiveEdQualityCheckService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.technology.qualityCheck";

        vm.init = function () {
            vm.loadPagedData();
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};

            fiveEdQualityCheckService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.technologyQualityCheckDetail", {checkId: id});
        }

        vm.add = function () {
            fiveEdQualityCheckService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveEdQualityCheckService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.init();

        return vm;
    })

    .controller("FiveTechnologyQualityCheckDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdQualityCheckService,fiveEdArrangeUserService) {
        var vm = this;
        var checkId = $stateParams.checkId;

        vm.loadData = function (loadProcess) {
            fiveEdQualityCheckService.getModelById(checkId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    vm.selectDetail();
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdQualityCheckService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }



        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdQualityCheckService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId,result) {
                            if (result.completeTask){
                                $scope.back();
                            }else {
                                $scope.refresh();
                            }
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.selectDetail=function(){
            fiveEdQualityCheckService.listDetail(checkId).then(function (value) {
                if (value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.addDetail=function(){
            fiveEdQualityCheckService.getNewDetail(checkId).then(function (value) {
                if (value.data.ret){
                    vm.editDetail(value.data.data);
                }
            })
        }

        vm.editDetail=function(detailId){
            fiveEdQualityCheckService.getDetailById(detailId).then(function (value) {
                if (value.data.ret){
                    vm.qualityCheckDetail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.removeDetail=function(detailId){
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveEdQualityCheckService.removeDetail(detailId,user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.selectDetail();
                        }
                    })
                }
            })

        }

        vm.updateDetail=function(){
            fiveEdQualityCheckService.updateDetail(vm.qualityCheckDetail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.selectDetail();
                    $("#detailModel").modal("hide");
                }
            })
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

        vm.showUserModel = function () {
            $scope.showSelectEmployeeModal_({title:"质量部门负责人选择", userLoginList: vm.item.qualityDepartmentMen,multiple:true});
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            vm.item.qualityDepartmentMen = $scope.selectedUserLogins_;
            vm.item.qualityDepartmentMenName =  $scope.selectedUserNames_;
        };

        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.loadData(true);

        return vm;
    })


    .controller("FiveTechnologyQualityAnalysisController", function ($state, $stateParams, $scope, fiveEdQualityAnalysisService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.technology.qualityAnalysis";

        vm.init = function () {
            vm.loadPagedData();
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};

            fiveEdQualityAnalysisService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.technologyQualityAnalysisDetail", {analysisId: id});
        }

        vm.add = function () {
            fiveEdQualityAnalysisService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveEdQualityAnalysisService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.init();

        return vm;
    })

    .controller("FiveTechnologyQualityAnalysisDetailController", function ($rootScope,$state, $stateParams, $scope, fiveEdQualityAnalysisService) {
        var vm = this;
        var analysisId = $stateParams.analysisId;

        vm.loadData = function (loadProcess) {
            fiveEdQualityAnalysisService.getModelById(analysisId).then(function (value) {
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
            fiveEdQualityAnalysisService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdQualityAnalysisService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId,result) {
                            if (result.completeTask){
                                $scope.back();
                            }else {
                                $scope.refresh();
                            }
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showUserModel = function () {
            $scope.showSelectEmployeeModal_({title:"质量部门负责人选择", userLoginList: vm.item.qualityDepartmentMen,multiple:true});
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            vm.item.qualityDepartmentMen = $scope.selectedUserLogins_;
            vm.item.qualityDepartmentMenName =  $scope.selectedUserNames_;
        };
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.loadData(true);


        return vm;
    })


    .controller("FiveTechnologyQualityIssueController", function ($state, $stateParams, $scope, fiveEdQualityIssueService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.technology.qualityIssue";

        vm.init = function () {
            vm.loadPagedData();
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};

            fiveEdQualityIssueService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.technologyQualityIssueDetail", {issueId: id});
        }

        vm.add = function () {
            fiveEdQualityIssueService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveEdQualityIssueService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.init();

        return vm;
    })

    .controller("FiveTechnologyQualityIssueDetailController", function ($state, $stateParams, $scope, $rootScope,fiveEdQualityIssueService) {
        var vm = this;
        var issueId = $stateParams.issueId;

        vm.loadData = function (loadProcess) {
            fiveEdQualityIssueService.getModelById(issueId).then(function (value) {
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
                        }, function (processInstanceId,result) {
                            if (result.completeTask){
                                $scope.back();
                            }else {
                                $scope.refresh();
                            }
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
            fiveEdQualityIssueService.getPrintData(issueId).then(function (value) {
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

