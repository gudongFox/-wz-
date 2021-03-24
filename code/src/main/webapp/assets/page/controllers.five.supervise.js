angular.module('controllers.five.supervise', [])

    //督办
    .controller("FiveSuperviseController", function ($state,$stateParams,$rootScope,$scope,fiveSuperviseService) {
        var vm = this;
        vm.params ={dispatchType: "常规督办", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.supervise";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,superviseType:vm.params.dispatchType};
            fiveSuperviseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.superviseDetail", {superviseId: id});
        }


        vm.add = function () {
            fiveSuperviseService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveSuperviseService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.queryData();

        return vm;

    })

    .controller("FiveSuperviseDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,hrEmployeeService ,fiveSuperviseService,fiveSuperviseDetailService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.supervise";
        var tableName = $rootScope.loadTableName(uiSref);

        var superviseId = $stateParams.superviseId;
        /*跳转 打开一个新的标签页*/
        vm.show = function (id) {
            var url=$state.href("five.superviseDetailChild", {detailId: id});
            window.open(url,'_blank');

        }


        vm.add = function () {
            fiveSuperviseDetailService.getNewModelById( user.userLogin,superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        }

        vm.loadData = function (loadProcess) {
            fiveSuperviseService.getModelById(superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                       // $rootScope.loadActRelevance(vm.item.businessKey);
                    }
                    $("#superviseTime").datepicker('setDate', vm.item.superviseTime);

                }
            })
            fiveSuperviseDetailService.listDate(superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveSuperviseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择批示领导",type:"部门", userLoginList: vm.item.companyLeader,multiple:false,deptIds:"16",parentDeptId:16});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='companyLeader') {
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveSuperviseService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $rootScope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $rootScope.loadNewProcessInstance(processInstanceId);
                            $rootScope.loadProcessInstance(processInstanceId);
                        });
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.loadDetail=function(){
            fiveSuperviseDetailService.listDate(superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;

                }
            })
        }
        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveSuperviseService.getNewModelDetail(superviseId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $state.go("five.superviseDetailChild", {detailId: id});
                    }
                })
            //修改
            } else {
                fiveSuperviseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $state.go("five.superviseDetailChild", {detailId: id});
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveSuperviseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveSuperviseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    fiveSuperviseService.listDetail(superviseId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    })
                    $("#detailModal").modal("hide");

                }

            })

        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("常规督办");

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })

    .controller("FiveSuperviseDetailChildController", function ($sce,$state,$stateParams,$rootScope,$scope,hrEmployeeService ,fiveSuperviseDetailService) {
        var vm = this;
        var uiSref="five.superviseDetail";
        var tableName = $rootScope.loadTableName(uiSref);

        var detailId = $stateParams.detailId;


        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveSuperviseDetailService.getModelById(detailId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);

                        $scope.basicInit(vm.item.businessKey);
                    }

                }
                $("#timeLimit").datepicker('setDate', vm.item.timeLimit);
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveSuperviseDetailService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='transactor'){
                $scope.showOaSelectEmployeeModal_({title:"请选择办理人", type:"部门",deptIds:"1",userLoginList: vm.item.applicantMan,multiple:true});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='transactor'){
                vm.item.transactor = $scope.selectedOaUserLogins_;
                vm.item.transactorName = $scope.selectedOaUserNames_;
/*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                    var user = value.data.data;
                    vm.item.applicantNo = user.userLogin;
                    vm.item.applicantTel=user.mobile;
                })*/
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveSuperviseDetailService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $rootScope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $rootScope.loadNewProcessInstance(processInstanceId);
                            $rootScope.loadProcessInstance(processInstanceId);
                        });
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })
    //文件督办
    .controller("FiveSuperviseFileController", function ($state,$stateParams,$rootScope,$scope,fiveSuperviseFileService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        //vm.params ={ qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.params ={qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.superviseFile";
        var tableName = $rootScope.loadTableName(uiSref);


        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveSuperviseFileService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                   // setCacheParams(key,vm.params,vm.pageInfo);

                }
            })
        };

        vm.show = function (id) {
            $state.go("five.superviseFileDetail", {superviseId: id});
        }


        vm.add = function () {
            fiveSuperviseFileService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveSuperviseFileService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }
        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveSuperviseFileService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'fileHeader',placeholder:'请输入文件标题..'},
                    2:{type:"input",colName:'deptName',placeholder:'督办单位'},
                    3:{type:"input",colName:'transactorName',placeholder:'办理人'},
                    4:{type:"select",colName:'feedbackTime',placeholder:'反馈周期',option:[{title:"全部",value:""},{title:"按日",value:'按日'},{title:"按周",value:'按周'},{title:"按月",value:'按月'}]},
                    5:{type:"input",colName:'transactionPlan',placeholder:'办理进度'},
                    6:{type:"input",colName:'companyLeaderName',placeholder:'批示领导'},
                    7:{type:"input",colName:'gmtCreate'},
                    8:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:9};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
      /*  $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'title',placeholder:'请输入标题..'},
                    2:{type:"input",colName:'deptName',placeholder:'送审单位'},
                    3:{type:"input",colName:'sendManName',placeholder:'送审人'},
                    4:{type:"input",colName:'sendManLink',placeholder:'联系方式'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });*/
        vm.queryData();

        return vm;

    })

    .controller("FiveSuperviseFileDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,hrEmployeeService ,fiveSuperviseFileService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.supervise";
        var superviseId = $stateParams.superviseId;
        var tableName = $rootScope.loadTableName("five.superviseFile");



        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveSuperviseFileService.getModelById(superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.loadActRelevance(vm.item.businessKey,"fiveOaSignQuote","",false);
                    }
                    $("#superviseTime").datepicker('setDate', vm.item.superviseTime);

                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveSuperviseFileService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择批示领导",type:"部门", userLoginList: vm.item.companyLeader,multiple:false,deptIds:"16",parentDeptId:16});
            }if (vm.status=='transactor'){
                $scope.showOaSelectEmployeeModal_({title:"请选择办理人",type:"部门", userLoginList: vm.item.transactor,multiple:false,deptIds:"1",parentDeptId:vm.item.deptId});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='companyLeader') {
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }
            if ( vm.status=='transactor') {
                vm.item.transactor = $scope.selectedOaUserLogins_;
                vm.item.transactorName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveSuperviseFileService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $rootScope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $rootScope.loadNewProcessInstance(processInstanceId);
                            $rootScope.loadProcessInstance(processInstanceId);
                        });
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("文件督办");

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })

    //年度重点任务督办
    .controller("FiveSuperviseYearController", function ($state,$stateParams,$rootScope,$scope,fiveSuperviseService) {
        var vm = this;
        vm.params ={dispatchType: "年度重点任务督办", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.superviseYear";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,superviseType:vm.params.dispatchType};
            fiveSuperviseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.superviseYearDetail", {superviseId: id});
        }


        vm.add = function () {
            fiveSuperviseService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveSuperviseService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.queryData();

        return vm;

    })

    .controller("FiveSuperviseYearDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,hrEmployeeService ,fiveSuperviseService,fiveSuperviseDetailService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.supervise";
        var tableName = $rootScope.loadTableName("five.superviseYear");

        var superviseId = $stateParams.superviseId;
        /*跳转 打开一个新的标签页*/
        vm.show = function (id) {
            var url=$state.href("five.superviseDetailChild", {detailId: id});
            window.open(url,'_blank');

        }


        vm.add = function () {
            fiveSuperviseDetailService.getNewModelById( user.userLogin,superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        }

        vm.loadData = function (loadProcess) {
            fiveSuperviseService.getModelById(superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        //$rootScope.loadActRelevance(vm.item.businessKey);
                    }
                    $("#superviseTime").datepicker('setDate', vm.item.superviseTime);

                }
            })
            fiveSuperviseDetailService.listDate(superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveSuperviseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择批示领导",type:"部门", userLoginList: vm.item.companyLeader,multiple:false,deptIds:"16",parentDeptId:16});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='companyLeader') {
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveSuperviseService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $rootScope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $rootScope.loadNewProcessInstance(processInstanceId);
                            $rootScope.loadProcessInstance(processInstanceId);
                        });
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.loadDetail=function(){
            fiveSuperviseDetailService.listDate(superviseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;

                }
            })
        }
        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveSuperviseService.getNewModelDetail(superviseId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $state.go("five.superviseDetailChild", {detailId: id});
                    }
                })
                //修改
            } else {
                fiveSuperviseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $state.go("five.superviseDetailChild", {detailId: id});
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveSuperviseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveSuperviseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    fiveSuperviseService.listDetail(superviseId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    })
                    $("#detailModal").modal("hide");

                }

            })

        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("年度重点任务督办");

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })
