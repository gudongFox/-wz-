angular.module('controllers.five.finance', [])


    //收入管理
    .controller("FiveFinanceIncomeController", function ($state, $scope, $rootScope, fiveFinanceIncomeService, fiveFinanceInvoiceService) {
        var vm = this;
        var uiSref = "finance.income";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
            $("#uploadModelFile").fileupload({
                dataType: 'text',
                url:'/five/finance/income/receive.do?userLogin='+user.userLogin,
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                progress:function(e,data){
                    var speed=(data.bitrate/(8*1024*1024)).toFixed(2)+"Mbit/s";
                    var percent = parseInt(data.loaded / data.total * 100, 10);
                    $(".loading-message.loading-message-boxed span").html(" "+speed+" "+percent+"%");
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        var obj= result.msg.split(",");
                        toastr.success("导入数据成功!<br>"+"修改："+obj[0]+"条   新增："+obj[1]+"条");
                        vm.loadPagedData();
                    } else {
                        toastr.error(result.msg);
                    }
                }
            });
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                uiSref: uiSref,
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin
            });
            fiveFinanceIncomeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.incomeDetail", {incomeId: id});

        }

        vm.showIncomeConfirm = function (incomeConfirmIds) {
            fiveFinanceInvoiceService.selectIncomeConfirm(incomeConfirmIds).then(function (value) {
                if (value.data.ret) {
                    vm.incomeConfirms = value.data.data;
                }
            })
            $("#incomeConfirmModal").modal("show");
        }
        vm.jumpIncomeConfirm = function (id) {
            $("#incomeConfirmModal").modal("hide");
            setTimeout(function () {
                $state.go("finance.incomeConfirmDetail", {incomeConfirmId: id});
            }, 200);

        }

        //收入确认 取消
        vm.getNotarizeIncome = function (incomeId,creator) {
           /* if(creator!=user.userLogin){
                toastr.error("收入确认 由收入创建人 确认！")
                return;
            }*/
            bootbox.confirm("是否确认 修改收入状态！", function (result) {
                if (result) {
                    fiveFinanceIncomeService.getNotarizeIncome(incomeId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("收入状态修改成功！")
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceIncomeService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceIncomeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.downTempleXml =function(){
            fiveFinanceIncomeService.downTempleXls(user.userLogin).then(function (response) {
                var blob = new Blob([response.data.data], {type: response.data.data.type});
                if (response.data.data.type === "application/json") {
                    var reader = new FileReader();
                    reader.readAsText(blob, 'utf-8');
                    reader.onload = function (e) {
                        var result = jQuery.parseJSON(reader.result);
                        toastr.error(result.msg);
                    }
                    return;
                }
                var objectUrl = URL.createObjectURL(blob);
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                if ('msSaveOrOpenBlob' in navigator) {
                    //ie使用的下载方式
                    window.navigator.msSaveOrOpenBlob(blob, fileName);
                } else {
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = decodeURI(fileName);
                    a.href = objectUrl;
                    a.click();
                }
            })
        }
        vm.downIncome =function(){
            fiveFinanceIncomeService.downIncome(user.userLogin).then(function (response) {
                var blob = new Blob([response.data.data], {type: response.data.data.type});
                if (response.data.data.type === "application/json") {
                    var reader = new FileReader();
                    reader.readAsText(blob, 'utf-8');
                    reader.onload = function (e) {
                        var result = jQuery.parseJSON(reader.result);
                        toastr.error(result.msg);
                    }
                    return;
                }
                var objectUrl = URL.createObjectURL(blob);
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                if ('msSaveOrOpenBlob' in navigator) {
                    //ie使用的下载方式
                    window.navigator.msSaveOrOpenBlob(blob, fileName);
                } else {
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = decodeURI(fileName);
                    a.href = objectUrl;
                    a.click();
                }
            })
        }


        $("#uploadModelFile").fileupload({
            dataType: 'text',
            url: '/five/finance/income/receive.do?userLogin=' + user.userLogin,
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
                var result = JSON.parse(data.result);
                if (result.ret) {
                    var obj = result.data.split(",");
                    toastr.success("导入数据成功!<br>" + "新增：" + obj[1] + "条   修改：" + obj[0] + "条");
                    vm.loadPagedData();
                } else {
                    toastr.error(result.msg);
                }
            }
        });

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params =$.extend(params,{ userLogin: user.userLogin,uiSref:uiSref});
            fiveFinanceIncomeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"select",colName:'type',option:[{"title":"全部","value":""},{"title":"现金","value":"现金"},{"title":"打款收入","value":"打款收入"},
                            {"title":"票据收入","value":"票据收入"},{"title":"其他收入","value":"其他收入"}]},
                    2:{colName:'sourceName',placeholder:'请输入对方账户名称..'},
                    3:{colName:'sourceAccount',placeholder:'请输入对方账户..'},
                    4:{colName:'incomeTime',placeholder:'请输入交易日期..'},
                    5:{colName:'money',placeholder:'请输入收入金额..'},
                    6:{colName:'remark',placeholder:'请输入收入备注..'},
                    7:{colName:'creatorName',placeholder:'请输入发起人..'},
                    8:{colName:'gmtCreate',placeholder:'请输入创建时间..'},
                    9:{type:"select",colName:'processEnd',option:[{"title":"全部","value":""},{"title":"已确认","value":"已确认"},
                            {"title":"未确认","value":"未确认"}]},
                },handleColumn:11};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        vm.init();

        return vm;
    })
    .controller("FiveFinanceIncomeDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceInvoiceService,fiveFinanceNodeService, fiveFinanceIncomeService) {
        var vm = this;
        var uiSref = "finance.income";
        var incomeId = $stateParams.incomeId;


        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);

        }

        vm.loadData = function (loadProcess) {
            fiveFinanceIncomeService.getModelById(incomeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        //$scope.loadProcessInstance(vm.item.processInstanceId);
                        $rootScope.getTplConfig("",vm.item.businessKey);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    vm.getIncomeConfirmByIncomeId(vm.item.confirmIds);
                    vm.getNodesByIncomeId();
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceIncomeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceIncomeService.moneyTurnCapital(incomeId,vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                    //toastr.success("跟新成功!")
                })
            }
        }

        vm.showUserModel = function () {
            if (vm.opt == 'businessUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参与经营人员",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'businessUser') {
                vm.item.businessUserName = $scope.selectedOaUserNames_;
                vm.item.businessUser = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceIncomeService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }
        //收入类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "收入类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                    singleCheckBox(".cb_type", "data-name");
                }
            })

        }

        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type = type;
            vm.save();
        }
        //选择票据信息
        vm.showNodeModel = function () {
            //选择已开具的票据信息
            fiveFinanceNodeService.getNodesInIncome(incomeId, user.userLogin, uiSref).then(function (value) {
                vm.selectNodes = value.data.data;
                //singleCheckBox(".cb_invoice", "data-name");
            });
            $("#selectNodeModal").modal("show");
        }
        vm.saveSelectNode = function () {
            $("#selectNodeModal").modal("hide");
            var nodeIdList = [];
            $(".cb_node:checked").each(function () {
                nodeIdList.push($(this).attr("data-id"));
            });
            var nodeIds = nodeIdList.join(',');
            //1.添加已有发票 2.启动合同收费
            fiveFinanceIncomeService.saveSelectNodes(incomeId, nodeIds).then(function (value) {
                toastr.success(value.data.data);
                vm.loadData(true);
            });

        }

        vm.getNodesByIncomeId = function () {
            fiveFinanceNodeService.getNodesByIncomeId(incomeId).then(function (value) {
                if (value.data.ret) {
                    vm.nodes = value.data.data;
                }
            })
        }

        vm.showNode = function (id) {
            vm.save();
            $state.go("finance.nodeDetail", {nodeId: id});
        }
        vm.addNode = function () {
            var userLogin = user.userLogin;
            bootbox.confirm("确认要创建票据流程", function (result) {
                if (result) {
                    fiveFinanceNodeService.getNewModelByIncome(incomeId, userLogin, uiSref).then(function (value) {
                        if (value.data.ret) {
                            vm.showNode(value.data.data);
                        }
                    })
                }
            })

        }
        vm.removeNode = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceNodeService.removeInIncome(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData(true);
                        }
                    })
                }
            })
        }

        vm.getIncomeConfirmByIncomeId = function (incomeConfirmIds) {
            fiveFinanceInvoiceService.selectIncomeConfirm(incomeConfirmIds).then(function (value) {
                if (value.data.ret) {
                    vm.incomeConfirms = value.data.data;
                }
            })
        }
        vm.jumpIncomeConfirm = function (id) {
            $("#incomeConfirmModal").modal("hide");
            setTimeout(function () {
                $state.go("finance.incomeConfirmDetail", {incomeConfirmId: id});
            }, 200);

        }
        //收入确认 取消
        vm.getNotarizeIncome = function (incomeId) {
            bootbox.confirm("是否确认 修改收入状态！", function (result) {
                if (result) {
                    fiveFinanceIncomeService.getNotarizeIncome(incomeId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("收入状态修改成功！")
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //收入认领
    .controller("FiveFinanceIncomeLibraryController", function ($state, $scope, $rootScope, fiveFinanceIncomeService, fiveFinanceInvoiceService) {
        var vm = this;
        var uiSref = "finance.incomeLibrary";
        var key = $state.current.name + "_" + user.userLogin;
        var keyConfirmed = $state.current.name + "_" + user.userLogin +"_confirmed";
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: 10, total: 0});
        vm.paramsConfirmed = getCacheParams(key, {qName: "", pageNumConfirmed: 1, pageSizeConfirmed: 10, totalConfirmed: 0});

        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.pageInfoConfirmed = {pageNum: vm.paramsConfirmed.pageNumConfirmed, pageSize: vm.paramsConfirmed.pageSizeConfirmed, total: vm.paramsConfirmed.totalConfirmed};

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
            vm.loadPagedDataConfirmed();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.pageInfoConfirmed.pageNum = 1;
            vm.loadPagedData();
            vm.loadPagedDataConfirmed();

        }

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                uiSref: uiSref,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin
            });
            fiveFinanceIncomeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }
        vm.loadPagedDataConfirmed = function () {
            var params = $.extend(tablefilter.params, {
                uiSref: uiSref,
                pageNum: vm.pageInfoConfirmed.pageNum,
                pageSize: vm.pageInfoConfirmed.pageSize,
                userLogin: user.userLogin
            });
            //已确认的
            fiveFinanceIncomeService.listPagedDataConfirmed(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfoConfirmed = value.data.data;
                    setCacheParams(keyConfirmed, vm.paramsConfirmed, vm.pageInfoConfirmed);
                }
            })

        }


        vm.show = function (id) {
            $state.go("finance.incomeDetail", {incomeId: id});
        }

        vm.showIncomeConfirm = function (incomeConfirmIds) {
            fiveFinanceInvoiceService.selectIncomeConfirm(incomeConfirmIds).then(function (value) {
                if (value.data.ret) {
                    vm.incomeConfirms = value.data.data;
                }
            })
            $("#incomeConfirmModal").modal("show");
        }
        vm.jumpIncomeConfirm = function (id) {
            $("#incomeConfirmModal").modal("hide");
            setTimeout(function () {
                $state.go("finance.incomeConfirmDetail", {incomeConfirmId: id});
            }, 200);

        }

        vm.getNewIncomeConfirm = function (incomeId) {
            $state.go("finance.incomeConfirmDetailAdd", {incomeId: incomeId});
            /*bootbox.confirm("确认要创建收入确认?", function (result) {
                if (result) {
                    fiveFinanceIncomeService.getNewIncomeConfirm(incomeId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            $state.go("finance.incomeConfirmDetail", {incomeConfirmId: incomeId});
                        }
                    })
                }
            })*/

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceIncomeService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceIncomeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params =$.extend(params,{ userLogin: user.userLogin,uiSref:uiSref});
            fiveFinanceIncomeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            fiveFinanceIncomeService.listPagedDataConfirmed(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfoConfirmed = value.data.data;
                }
            })

        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"select",colName:'type',option:[{"title":"全部","value":""},{"title":"现金","value":"现金"},{"title":"打款收入","value":"打款收入"},
                            {"title":"票据收入","value":"票据收入"},{"title":"其他收入","value":"其他收入"}]},
                    2:{colName:'sourceName',placeholder:'请输入对方账户名称..'},
                    3:{colName:'sourceAccount',placeholder:'请输入对方账户..'},
                    4:{colName:'incomeTime',placeholder:'请输入交易日期..'},
                    5:{colName:'money',placeholder:'请输入收入金额..'},
                    6:{colName:'remark',placeholder:'请输入收入备注..'},
                    8:{colName:'creatorName',placeholder:'请输入发起人..'},
                    9:{colName:'gmtCreate',placeholder:'请输入创建时间..'},
                    handleColumn:10}};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;
    })

    //收入确认库
    .controller("FiveFinanceIncomeConfirmController", function ($state, $scope, $rootScope, fiveFinanceIncomeService,fiveFinanceIncomeConfirmService) {
        var vm = this;
        var uiSref = "finance.incomeConfirm";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                uiSref: uiSref,
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin
            });
            fiveFinanceIncomeConfirmService.listPagedData(params).then(function (value) {
                  if (value.data.ret) {
                      vm.pageInfo = value.data.data;
                      setCacheParams(key, vm.params, vm.pageInfo);
                  }
              })
        }

        vm.show = function (id) {
            $state.go("finance.incomeConfirmDetail", {incomeConfirmId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceIncomeConfirmService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceIncomeConfirmService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params =$.extend(params,{ userLogin: user.userLogin,uiSref:uiSref});
            fiveFinanceIncomeConfirmService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"select",colName:'type',option:[{"title":"全部","value":""},{"title":"现金","value":"现金"},{"title":"打款收入","value":"打款收入"},
                            {"title":"票据收入","value":"票据收入"},{"title":"其他收入","value":"其他收入"}]},
                    2:{colName:'sourceAccount',placeholder:'请输入收入来源账户..'},
                    3:{colName:'targetAccount',placeholder:'请输入收入接收账户..'},
                    4:{colName:'money',placeholder:'请输入收入金额..'},
                    5:{colName:'invoiceNo',placeholder:'请输入发票号..'},
                    6:{colName:'contractName',placeholder:'请输入合同名称..'},
                    7:{colName:'contractNo',placeholder:'请输入合同号..'},
                  /*  8:{colName:'incomeRemark',placeholder:'请输入收入备注..'},*/
                    8:{colName:'deptName',placeholder:'请输入部门名称..'},
                    9:{colName:'creatorName',placeholder:'请输入发起人..'},
                    10:{colName:'gmtCreate',placeholder:'请输入创建时间..'},
                  /*  11:{colName:'processName',placeholder:'请输入流程状态..'},*/
                },handleColumn:11};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;
    })
    .controller("FiveFinanceIncomeConfirmDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceInvoiceService, businessIncomeService, fiveBusinessContractLibraryService, fiveFinanceNodeService, fiveFinanceIncomeConfirmService) {
        var vm = this;
        var uiSref = "finance.incomeConfirm";
        var incomeConfirmId = $stateParams.incomeConfirmId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceIncomeConfirmService.getModelById(incomeConfirmId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {

                        if(vm.item.processInstanceId==""){
                            $rootScope.getTplConfig("",vm.item.businessKey);
                        }else{
                            $scope.loadProcessInstance(vm.item.processInstanceId);
                        }

                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#verifyTime").datepicker('setDate', vm.item.verifyTime);
                }
            })
            vm.getIncomesByIncomeConfirmId();
            vm.getInvoiceByIncomeConfirmId();
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceIncomeConfirmService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceIncomeConfirmService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }
        //选人
        vm.showUserModel = function () {
            if (vm.opt == 'businessUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参与经营人员",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'businessUser') {
                vm.item.businessUserName = $scope.selectedOaUserNames_;
                vm.item.businessUser = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            //存在判断发票合同收费流程是否完结 或者存在
            if(vm.invoices.length==0&&vm.incomes.length==0){
                toastr.warning("请先选择 发票 或者 合同 再发送");
                return ;
            }
            for(var v in vm.invoices){
                if(!vm.invoices[v].processEnd){
                    toastr.warning("请先完成 发票申请 流程再发送");
                    return ;
                }
            }
            for(var v in vm.incomes){
                if(!vm.incomes[v].processEnd){
                    toastr.warning("请先完成 合同收费 流程再发送");
                    return ;
                }
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceIncomeConfirmService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
              $scope.showOaSelectEmployeeModal_({
                  title: "请选择部门",
                  type: '选部门',
                  deptIds: "1",
                  deptIdList: vm.item.deptId,
                  multiple: false
              });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }
        //收入类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "收入类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                }
            })
        }
        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type = type;
            vm.save();
        }

        //收入信息
        vm.showIncomeModel = function () {
            fiveFinanceIncomeConfirmService.getIncomeList(incomeConfirmId, user.userLogin, uiSref).then(function (value) {
                vm.incomes = value.data.data;
                singleCheckBox(".cb_income", "data-name");
            });
            $("#selectIncomeModal").modal("show");
        }
        vm.saveSelectIncome = function () {
            $("#selectIncomeModal").modal("hide");
            if ($(".cb_income:checked").length > 0) {
                var income = $.parseJSON($(".cb_income:checked").first().attr("data-name"));
                //保存收入数据
                vm.item.incomeId = income.id;
                vm.item.sourceAccount = income.sourceName;
                vm.item.targetAccount = income.targetAccount;
                vm.item.type = income.type;
                vm.item.incomeTime = income.incomeTime;
                vm.item.incomeRemark = income.remark;
                vm.save();
            }
        }

        //选择发票信息
        vm.showInvoiceModel = function () {
            //选择已开具的发票信息
            fiveFinanceInvoiceService.getInvoicesWithoutConfirm(incomeConfirmId, user.userLogin, uiSref).then(function (value) {
                vm.selectInvoices = value.data.data;
                //singleCheckBox(".cb_invoice", "data-name");
            });
            $("#selectInvoiceModal").modal("show");
        }
        vm.saveSelectInvoice = function () {
            $("#selectInvoiceModal").modal("hide");
            var invoiceIdList = [];
            $(".cb_invoice:checked").each(function () {
                invoiceIdList.push($(this).attr("data-id"));
            });
            var invoiceIds = invoiceIdList.join(',');
            //1.添加已有发票 2.启动合同收费
            fiveFinanceIncomeConfirmService.saveSelectInvoice(incomeConfirmId, invoiceIds).then(function (value) {
                toastr.success(value.data.data);
                vm.loadData(true);
            });
            vm.save();

        }

        //发票申请
        vm.getInvoiceByIncomeConfirmId = function () {
            fiveFinanceInvoiceService.getInvoicesByIncomeConfirmId(incomeConfirmId).then(function (value) {
                if (value.data.ret) {
                    vm.invoices = value.data.data;
                }
            })
        }
        vm.showInvoice = function (id) {
            $state.go("finance.invoiceDetail", {invoiceId: id});
        }
        vm.addInvoice = function () {
            vm.save();
            var userLogin = user.userLogin;
            bootbox.confirm("确认要创建发票申请流程", function (result) {
                if (result) {
                    fiveFinanceInvoiceService.getNewModelByConfirm(incomeConfirmId, userLogin, uiSref).then(function (value) {
                        if (value.data.ret) {
                            vm.showInvoice(value.data.data);
                        }
                    })
                }
            })

        }
        vm.removeInvoice = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceService.removeInIncomeConfirm(id, incomeConfirmId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData(true)
                        }
                    })
                }
            })
        }
        //补领发票
        vm.replenishInvoice = function (incomeId) {
            fiveFinanceInvoiceService.replenishInvoiceByIncome(incomeId, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    var incomeId = value.data.data;
                    $state.go("finance.invoiceDetail", {invoiceId: incomeId});
                }
            })
        }
        //补领删除
        vm.removeReplenish = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceService.removeReplenish(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData(true)
                        }
                    })
                }
            })
        }


        //合同收费
        vm.getIncomesByIncomeConfirmId = function () {
            businessIncomeService.getIncomesByIncomeConfirmId(incomeConfirmId).then(function (value) {
                if (value.data.ret) {
                    vm.incomes = value.data.data;
                }
            })
        }
        vm.showContractIncome = function (id) {
            $state.go("five.businessIncomeDetail", {incomeId: id});
        }
        vm.addContractIncome = function () {
            vm.save();
            $scope.showLibrarySelectModal_({
                selectLibraryId:0,
                uiSref: "five.businessContractLibrary",
                multiple: false
            });
        }
        //合同库
        $rootScope.saveSelectLibrary_ = function () {
            //需要从 合同库 中同步到 收入内转 的数据
            var libraryId = $rootScope.selectedLibrary.id;
            businessIncomeService.getNewModelByConfirm2(incomeConfirmId,libraryId,user.userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    $("#commonSelectLibraryModal").modal("hide");
                    $state.go("five.businessIncomeDetail", {incomeId: value.data.data});
                }
            })

        }
        vm.removeContractIncome = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessIncomeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData(true)
                        }
                    })
                }
            })
        }


        //收入确认 取消
        vm.getNotarizeIncome = function (incomeId,creator) {
            /* if(creator!=user.userLogin){
                 toastr.error("收入确认 由收入创建人 确认！")
                 return;
             }*/
            bootbox.confirm("是否修改 确认状态！", function (result) {
                if (result) {
                    fiveFinanceIncomeConfirmService.getNotarizeIncome(incomeConfirmId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("收入状态修改成功！");
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //保存后 新增
    .controller("FiveFinanceIncomeConfirmDetailAddController", function ($state, $stateParams, $scope, $rootScope,fiveFinanceIncomeService, commonCodeService, fiveFinanceInvoiceService, businessIncomeService, fiveBusinessContractLibraryService, fiveFinanceNodeService, fiveFinanceIncomeConfirmService) {
        var vm = this;
        var uiSref = "finance.incomeConfirm";
        var incomeId = $stateParams.incomeId;

        var incomeConfirmId = 0;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            //取消保存按钮
            setTimeout(function () {
                $scope.rightData.selectOpts ='';
                $scope.$apply();
            }, 500);

            vm.loadData(true);
        }

        vm.loadData = function () {
            fiveFinanceIncomeService.getNewIncomeConfirm(incomeId, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $rootScope.getTplConfig("",vm.item.businessKey);
                    $("#verifyTime").datepicker('setDate', vm.item.verifyTime);
                }
            })
            if(incomeConfirmId!=0){
                vm.getIncomesByIncomeConfirmId();
                vm.getInvoiceByIncomeConfirmId();
            }
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceIncomeConfirmService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("认领成功!请选择发票或者合同确认！");
                    incomeConfirmId = value.data.data;
                    //跳转
                    $state.go("finance.incomeConfirmDetail", {incomeConfirmId: incomeConfirmId});
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceIncomeConfirmService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }
        //选人
        vm.showUserModel = function () {
            if (vm.opt == 'businessUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参与经营人员",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'businessUser') {
                vm.item.businessUserName = $scope.selectedOaUserNames_;
                vm.item.businessUser = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            //存在判断发票合同收费流程是否完结 或者存在
            if(vm.invoices.length==0&&vm.incomes.length==0){
                toastr.warning("请先选择 发票 或者 合同 再发送");
                return ;
            }
            for(var v in vm.invoices){
                if(!vm.invoices[v].processEnd){
                    toastr.warning("请先完成 发票申请 流程再发送");
                    return ;
                }
            }
            for(var v in vm.incomes){
                if(!vm.incomes[v].processEnd){
                    toastr.warning("请先完成 合同收费 流程再发送");
                    return ;
                }
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceIncomeConfirmService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }
        //收入类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "收入类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                }
            })
        }
        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type = type;
            vm.save();
        }

        //收入信息
        vm.showIncomeModel = function () {
            fiveFinanceIncomeConfirmService.getIncomeList(incomeConfirmId, user.userLogin, uiSref).then(function (value) {
                vm.incomes = value.data.data;
                singleCheckBox(".cb_income", "data-name");
            });
            $("#selectIncomeModal").modal("show");
        }
        vm.saveSelectIncome = function () {
            $("#selectIncomeModal").modal("hide");
            if ($(".cb_income:checked").length > 0) {
                var income = $.parseJSON($(".cb_income:checked").first().attr("data-name"));
                //保存收入数据
                vm.item.incomeId = income.id;
                vm.item.sourceAccount = income.sourceName;
                vm.item.targetAccount = income.targetAccount;
                vm.item.type = income.type;
                vm.item.incomeTime = income.incomeTime;
                vm.item.incomeRemark = income.remark;
                vm.save();
            }
        }

        //选择发票信息
        vm.showInvoiceModel = function () {
            //选择已开具的发票信息
            fiveFinanceInvoiceService.getInvoicesWithoutConfirm(incomeConfirmId, user.userLogin, uiSref).then(function (value) {
                vm.selectInvoices = value.data.data;
                singleCheckBox(".cb_invoice", "data-name");
            });
            $("#selectInvoiceModal").modal("show");
        }
        vm.saveSelectInvoice = function () {
            $("#selectInvoiceModal").modal("hide");
            var invoiceIdList = [];
            $(".cb_invoice:checked").each(function () {
                invoiceIdList.push($(this).attr("data-id"));
            });
            var invoiceIds = invoiceIdList.join(',');
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceIncomeConfirmService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    incomeConfirmId = value.data.data;
                    //跳转
                    $state.go("finance.incomeConfirmDetail", {incomeConfirmId: incomeConfirmId});
                    //1.添加已有发票 2.启动合同收费
                    fiveFinanceIncomeConfirmService.saveSelectInvoice(incomeConfirmId, invoiceIds).then(function (value) {
                        toastr.success(value.data.data);
                        vm.loadData(true);
                    });
                }
            })
        }

        //发票申请
        vm.getInvoiceByIncomeConfirmId = function () {
            vm.save();
        }
        vm.showInvoice = function (id) {
            $state.go("finance.invoiceDetail", {invoiceId: id});
        }
        vm.addInvoice = function () {
            bootbox.confirm("确认要创建发票申请流程", function (result) {
                if (result) {
                    vm.item.operateUserLogin = user.userLogin;
                    fiveFinanceIncomeConfirmService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            incomeConfirmId = value.data.data;
                            //跳转
                            $state.go("finance.incomeConfirmDetail", {incomeConfirmId: incomeConfirmId});
                            fiveFinanceInvoiceService.getNewModelByConfirm(incomeConfirmId, user.userLogin, uiSref).then(function (value) {
                                if (value.data.ret) {
                                    vm.showInvoice(value.data.data);
                                }
                            })
                        }
                    })
                }
            })
        }
        vm.removeInvoice = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceService.removeInIncomeConfirm(id, incomeConfirmId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData(true)
                        }
                    })
                }
            })
        }
        //补领发票
        vm.replenishInvoice = function (incomeId) {
            fiveFinanceInvoiceService.replenishInvoiceByIncome(incomeId, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    var incomeId = value.data.data;
                    $state.go("finance.invoiceDetail", {invoiceId: incomeId});
                }
            })
        }
        //补领删除
        vm.removeReplenish = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceService.removeReplenish(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData(true)
                        }
                    })
                }
            })
        }


        //合同收费
        vm.getIncomesByIncomeConfirmId = function () {
            vm.save();
        }
        vm.showContractIncome = function (id) {
            $state.go("five.businessIncomeDetail", {incomeId: id});
        }
        vm.addContractIncome = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:0,
                uiSref: "five.businessContractLibrary",
                multiple: false
            });
        }
        //合同库
        $rootScope.saveSelectLibrary_ = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceIncomeConfirmService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    incomeConfirmId = value.data.data;
                    //跳转
                    $state.go("finance.incomeConfirmDetail", {incomeConfirmId: incomeConfirmId});
                    //需要从 合同库 中同步到 收入内转 的数据
                    var libraryId = $rootScope.selectedLibrary.id;
                    businessIncomeService.getNewModelByConfirm2(incomeConfirmId,libraryId,user.userLogin, uiSref).then(function (value) {
                        if (value.data.ret) {
                            $("#commonSelectLibraryModal").modal("hide");
                            $state.go("five.businessIncomeDetail", {incomeId: value.data.data});
                        }
                    })
                }
            })

        }
        vm.removeContractIncome = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessIncomeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData(true)
                        }
                    })
                }
            })
        }


        //收入确认 取消
        vm.getNotarizeIncome = function (incomeId,creator) {
            /* if(creator!=user.userLogin){
                 toastr.error("收入确认 由收入创建人 确认！")
                 return;
             }*/
            /*bootbox.confirm("是否修改 确认状态！", function (result) {
                if (result) {
                    fiveFinanceIncomeConfirmService.getNotarizeIncome(incomeConfirmId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("收入状态修改成功！");
                            vm.loadData();
                        }
                    })
                }
            })*/
            vm.save();
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })

    //发票申请
    .controller("FiveFinanceInvoiceController", function ($state, $scope, $rootScope, $sce,fiveFinanceInvoiceService) {
        var vm = this;
        var uiSref = "finance.invoice";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
            vm.invoiceMoneyHead = $sce.trustAsHtml("开票金额<br>(万元)");
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceInvoiceService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.invoiceDetail", {invoiceId: id});
        }
        vm.showIncome = function (id) {
            $state.go("five.businessIncomeDetail", {incomeId: id});
        }

        vm.showIncomeConfirm = function (incomeConfirmIds) {
            fiveFinanceInvoiceService.selectIncomeConfirm(incomeConfirmIds).then(function (value) {
                if (value.data.ret) {
                    vm.incomeConfirms = value.data.data;
                }
            })
            $("#incomeConfirmModal").modal("show");
        }
        vm.jumpIncomeConfirm = function (id) {
            $("#incomeConfirmModal").modal("hide");
            setTimeout(function () {
                $state.go("finance.incomeConfirmDetail", {incomeConfirmId: id});
            }, 200);

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceInvoiceService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }
        vm.removeReplenish = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceService.removeReplenish(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveFinanceInvoiceDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveFinanceInvoiceService, fiveBusinessContractLibraryService) {
        var vm = this;
        var uiSref = "finance.invoice";
        var invoiceId = $stateParams.invoiceId;


        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceInvoiceService.getModelById(invoiceId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#invoiceTime").datepicker('setDate', vm.item.invoiceTime);
                    $("#receiveTime").datepicker('setDate', vm.item.receiveTime);
                    $("#financeConfirmTime").datepicker('setDate', vm.item.financeConfirmTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceInvoiceService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceInvoiceService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applyMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.status == 'deptChargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门负责人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
            if (vm.status == 'remindReceiveMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择催收责任人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.remindReceiveMan,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applyMan') {
                vm.item.applyManName = $scope.selectedOaUserNames_;
                vm.item.applyMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'deptChargeMan') {
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.contractLibraryId == 0) {
                toastr.error("请先选择开具发票的合同");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceInvoiceService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }
        //收入类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "收入类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                }
            })

        }
        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type = type;
            vm.save();
        }

        //合同库
    /*    vm.showContractLibraryModal = function () {
            fiveBusinessContractLibraryService.selectAll(user.userLogin, "five.businessContractLibrary").then(function (value) {
                if (value.data.ret) {
                    vm.librarys = value.data.data;
                    singleCheckBox(".cb_library", "data-name");
                }
            })
            $("#selectLibraryModal").modal("show");
        };
        vm.saveSelectModel = function () {
            if ($(".cb_library:checked").length > 0) {
                var library = $.parseJSON($(".cb_library:checked").first().attr("data-name"));
                //需要从 合同库 中同步到 发票申请 的数据
                vm.item.contractName = library.projectName;
                vm.item.contractLibraryId = library.id;
                vm.item.contractNo = library.contractNo;
                vm.item.contractMoney = library.contractMoney;
                vm.item.contractGetInvoice = library.contractInvoiceMoney;
            }
            vm.save();
            $("#selectLibraryModal").modal("hide");
        }*/
        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.contractLibraryId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.item.contractName =  $rootScope.selectedLibrary.projectName;
            vm.item.contractLibraryId =  $rootScope.selectedLibrary.id;
            vm.item.contractNo =  $rootScope.selectedLibrary.contractNo;
            vm.item.contractMoney =  $rootScope.selectedLibrary.contractMoney;
            vm.item.contractGetInvoice =  $rootScope.selectedLibrary.contractInvoiceMoney;
            $scope.closeLibrarySelectModal_();
        }



        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //发票作废申请
    .controller("FiveFinanceInvoiceCancelController", function ($state, $scope, $rootScope, fiveFinanceInvoiceCancelService) {
        var vm = this;
        var uiSref = "finance.invoiceCancel";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }


        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceInvoiceCancelService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.invoiceCancelDetail", {invoiceCancelId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceInvoiceCancelService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceCancelService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveFinanceInvoiceCancelDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveFinanceInvoiceCancelService, fiveFinanceInvoiceService) {
        var vm = this;
        var uiSref = "finance.invoiceCancel";
        var invoiceCancelId = $stateParams.invoiceCancelId;


        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceInvoiceCancelService.getModelById(invoiceCancelId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#verifyTime").datepicker('setDate', vm.item.verifyTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceInvoiceCancelService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceInvoiceCancelService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applyMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applyMan') {
                vm.item.applyManName = $scope.selectedOaUserNames_;
                vm.item.applyMan = $scope.selectedOaUserLogins_;
            }


        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceInvoiceCancelService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }
        //收入类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "收入类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                }
            })

        }

        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type = type;
            vm.save();
        }

        vm.showInvoice = function () {
            fiveFinanceInvoiceService.listInvoice(vm.item.invoiceId).then(function (value) {
                if (value.data.ret) {
                    vm.listInvoice = value.data.data;
                    singleCheckBox(".cb_invoice", "data-name");
                    $("#showInvoiceModel").modal("show");
                }
            })
        }
        vm.saveInvoice = function () {
            if ($(".cb_invoice:checked").length > 0) {
                var invoice = $.parseJSON($(".cb_invoice:checked").first().attr("data-name"));
                //同步到报废申请的数据
                vm.item.invoiceId = invoice.id;
                vm.item.legalDept = invoice.legalDept;
                vm.item.contractName = invoice.contractName;
                vm.item.invoiceNo = invoice.invoiceNo;
                vm.item.contractNo = invoice.contractNo;
                vm.item.invoiceMoney = invoice.invoiceMoney;
                vm.save();
                $("#showInvoiceModel").modal("hide");
            }
        }


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //发票应收款管理
    .controller("FiveFinanceInvoiceCollectionController", function ($state, $scope, $rootScope, fiveFinanceInvoiceCollectionService) {
        var vm = this;
        var uiSref = "finance.invoiceCollection";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }


        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceInvoiceCollectionService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.invoiceCollectionDetail", {invoiceCollectionId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceInvoiceCollectionService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceInvoiceCollectionService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })
    .controller("FiveFinanceInvoiceCollectionDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveFinanceInvoiceCollectionService, fiveFinanceInvoiceService) {
        var vm = this;
        var uiSref = "finance.invoiceCollection";
        var invoiceCollectionId = $stateParams.invoiceCollectionId;


        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceInvoiceCollectionService.getModelById(invoiceCollectionId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#verifyTime").datepicker('setDate', vm.item.verifyTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceInvoiceCollectionService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceInvoiceCollectionService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applyMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applyMan') {
                vm.item.applyManName = $scope.selectedOaUserNames_;
                vm.item.applyMan = $scope.selectedOaUserLogins_;
            }


        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceInvoiceCollectionService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }
        //收入类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "收入类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                }
            })

        }

        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type = type;
            vm.save();
        }

        vm.showInvoice = function () {
            fiveFinanceInvoiceService.listInvoiceByCollection(vm.item.invoiceId).then(function (value) {
                if (value.data.ret) {
                    vm.listInvoice = value.data.data;
                    singleCheckBox(".cb_invoice", "data-name");
                    $("#showInvoiceModel").modal("show");
                }
            })
        }
        vm.saveInvoice = function () {
            if ($(".cb_invoice:checked").length > 0) {
                var invoice = $.parseJSON($(".cb_invoice:checked").first().attr("data-name"));
                //同步到报废申请的数据
                vm.item.invoiceId = invoice.id;
                vm.item.legalDept = invoice.legalDept;
                vm.item.contractName = invoice.contractName;
                vm.item.invoiceNo = invoice.invoiceNo;
                vm.item.contractNo = invoice.contractNo;
                vm.item.invoiceMoney = invoice.invoiceMoney;
                vm.item.invoiceGetMoney = invoice.invoiceGetMoney;
                vm.item.invoiceGetMoneyIng = invoice.invoiceGetMoneyIng;
                vm.item.deptId = invoice.deptId;
                vm.item.deptName = invoice.deptName;
                vm.item.remindReceiveMan = invoice.remindReceiveMan;
                vm.item.remindReceiveManName = invoice.remindReceiveManName;
                vm.save();
                $("#showInvoiceModel").modal("hide");
            }
        }


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //票据管理
    .controller("FiveFinanceNodeController", function ($state, $scope, $rootScope, fiveFinanceNodeService) {
        var vm = this;
        var uiSref = "finance.node";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceNodeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.nodeDetail", {nodeId: id});

        }
        vm.showIncome = function (id) {
            $state.go("finance.incomeDetail", {incomeId: id});
        }
        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceNodeService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceNodeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceNodeDetailController", function ($state, $stateParams, $scope, $rootScope, fiveFinanceNodeService) {
        var vm = this;
        var uiSref = "finance.node";
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceNodeService.getModelById(nodeId).then(function (value) {
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
            fiveFinanceNodeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceNodeService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }


        vm.showUserModel = function () {
            if (vm.opt == 'businessUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参与经营人员",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'businessUser') {
                vm.item.businessUserName = $scope.selectedOaUserNames_;
                vm.item.businessUser = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceNodeService.update(vm.item).then(function (value) {
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


        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择登记部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }
            if (vm.opt == 'messageDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.messageDeptId + ",",
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'deptId') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (vm.opt == 'messageDeptId') {
                vm.item.messageDeptName = $scope.selectedOaDeptNames_;
                vm.item.messageDeptId = Number($scope.selectedOaDeptIds_);
            }
        }


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //个人网银
    .controller("FiveFinanceSelfBankController", function ($state, $scope, $rootScope, fiveFinanceSelfBankService) {
        var vm = this;
        var uiSref = "finance.selfBank";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceSelfBankService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.selfBankDetail", {selfBankId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceSelfBankService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceSelfBankService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceSelfBankDetailController", function ($state, $stateParams, $scope, $rootScope,commonCodeService,fiveContentFileService,fiveFinanceSelfBankService) {
        var vm = this;
        var uiSref = "finance.selfBank";
        var selfBankId = $stateParams.selfBankId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceSelfBankService.getModelById(selfBankId).then(function (value) {
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
            fiveFinanceSelfBankService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        vm.showUserModel = function () {
            if (vm.opt == 'userName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申报人员",
                    type: '部门',
                    deptIds: vm.item.deptId,
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'userName') {
                vm.item.userName = $scope.selectedOaUserNames_;
                vm.item.userLogin = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceSelfBankService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择登记部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }
            if (vm.opt == 'messageDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.messageDeptId + ",",
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'deptId') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (vm.opt == 'messageDeptId') {
                vm.item.messageDeptName = $scope.selectedOaDeptNames_;
                vm.item.messageDeptId = Number($scope.selectedOaDeptIds_);
            }
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.getBankName = function () {
            var bankCard = bankCardAttribution(vm.item.userBankNo);

            vm.item.bankName = bankCard.bankName
        }

        return vm;

    })
    //公司网银
    .controller("FiveFinanceCompanyBankController", function ($state, $scope, $rootScope, fiveFinanceCompanyBankService) {
        var vm = this;
        var uiSref = "finance.companyBank";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceCompanyBankService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.companyBankDetail", {companyBankId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceCompanyBankService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceCompanyBankService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceCompanyBankDetailController", function ($state, $stateParams, $scope, $rootScope,commonCodeService,fiveContentFileService,fiveFinanceCompanyBankService) {
        var vm = this;
        var uiSref = "finance.companyBank";
        var companyBankId = $stateParams.companyBankId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceCompanyBankService.getModelById(companyBankId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        /*  $scope.loadProcessInstance(vm.item.processInstanceId);*/
                        $rootScope.getTplConfig("",vm.item.businessKey,user.userLogin);
                        $scope.basicInit(vm.item.businessKey);

                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceCompanyBankService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        vm.showUserModel = function () {
            if (vm.opt == 'userName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申报人员",
                    type: '部门',
                    deptIds: vm.item.deptId,
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'userName') {
                vm.item.userName = $scope.selectedOaUserNames_;
                vm.item.userLogin = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceCompanyBankService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择登记部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }
            if (vm.opt == 'messageDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.messageDeptId + ",",
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'deptId') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (vm.opt == 'messageDeptId') {
                vm.item.messageDeptName = $scope.selectedOaDeptNames_;
                vm.item.messageDeptId = Number($scope.selectedOaDeptIds_);
            }
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.getBankName = function () {
            var bankCard = bankCardAttribution(vm.item.userBankNo);

            vm.item.bankName = bankCard.bankName
        }

        return vm;

    })


    //收据管理
    .controller("FiveFinanceReceiptController", function ($state, $scope, fiveFinanceReceiptService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "收据管理",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.receipt";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceReceiptService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.receiptDetail", {receiptId: id});
        }


        vm.add = function () {
            fiveFinanceReceiptService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceReceiptService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceReceiptDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceReceiptService, fiveBusinessContractLibraryService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.receipt";
        var receiptId = $stateParams.receiptId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceReceiptService.getModelById(receiptId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceReceiptService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceReceiptService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showContractLibraryModal = function () {
            fiveBusinessContractLibraryService.selectAll(user.userLogin, "five.businessContractLibrary").then(function (value) {
                if (value.data.ret) {
                    vm.librarys = value.data.data;
                    singleCheckBox(".cb_library", "data-name");
                }
            })
            $("#selectLibraryModal").modal("show");
        };
        vm.saveSelectModel = function () {
            if ($(".cb_library:checked").length > 0) {
                var library = $.parseJSON($(".cb_library:checked").first().attr("data-name"));
                //需要从 合同库 中同步到 发票申请 的数据
                vm.item.contractName = library.projectName;
                vm.item.contractLibraryId = library.id;
                vm.item.contractNo = library.contractNo;
                vm.item.contractMoney = library.contractMoney;
                vm.item.contractGetInvoice = library.contractInvoiceMoney;
            }
            vm.save();
            $("#selectLibraryModal").modal("hide");
        }

        vm.print = function () {
            fiveFinanceReceiptService.getPrintData(receiptId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //项目预算
    .controller("FiveFinanceProjectBudgetController", function ($state, $scope, $rootScope, fiveFinanceProjectBudgetService) {
        var vm = this;
        var uiSref = "finance.projectBudget";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceProjectBudgetService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.projectBudgetDetail", {projectBudgetId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceProjectBudgetService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceProjectBudgetService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }
        vm.removeReplenish = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceProjectBudgetService.removeReplenish(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceProjectBudgetDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveFinanceProjectBudgetService, fiveBusinessContractLibraryService) {
        var vm = this;
        var uiSref = "finance.projectBudget";
        var projectBudgetId = $stateParams.projectBudgetId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceProjectBudgetService.getModelById(projectBudgetId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#invoiceTime").datepicker('setDate', vm.item.invoiceTime);
                    $("#receiveTime").datepicker('setDate', vm.item.receiveTime);
                    $("#financeConfirmTime").datepicker('setDate', vm.item.financeConfirmTime);
                }
            })
            fiveFinanceProjectBudgetService.getDetailById(projectBudgetId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        search:true,
                        columns: [{
                            field: 'selectItem',
                            radio: true
                        }, {
                            title: '类型名称',
                            field: 'typeName',
                            width: '30%',
                            formatter: function (value, row, index) {
                                if(row.lv==1){
                                    return '<span style="color:red">'+value+'</span>';
                                }
                                return value;
                            }
                        },
                            {
                                field: 'purchaseNo',
                                title: '采购编号',
                                width: '20%',
                                align: "left"
                            },
                            {
                                field: 'budgetMoney',
                                title: '预算金额',
                                width: '20%',
                                align: "center",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '预算占比',
                                width: '15%',
                                align: "center",
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'seq',
                                title: '排序',
                                width: '15%',
                                align: "center",
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })
        };
        //新增detail
        $("#addBtn").click(function () {
            var data = [];
            $('#treeTable').bootstrapTreeTable('appendData', data);
        })
        //展开隐藏全部
        var _expandFlag_all = false;
        vm.expandAll = function () {
            if (_expandFlag_all) {
                $('#treeTable').bootstrapTreeTable('expandAll');
            } else {
                $('#treeTable').bootstrapTreeTable('collapseAll');
            }
            _expandFlag_all = _expandFlag_all ? false : true;
        }


        //新增
        vm.showDetailModel = function (id) {
            var detailId = 0;
            var selecteds = $('#treeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                detailId = _item.id;
            });
            if (id === 0) {
                fiveFinanceProjectBudgetService.getNewModelDetail(projectBudgetId,detailId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            } else {
                if (detailId == 0) {
                    toastr.warning("请先选择要修改的记录！")
                    return;
                }
                fiveFinanceProjectBudgetService.getModelDetailById(detailId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        vm.removeDetail = function () {
            var selecteds = $('#treeTable').bootstrapTreeTable('getSelections');
            var detailId = 0;
            $.each(selecteds, function (_i, _item) {
                detailId = _item.id;
            });
            if (detailId == 0) {
                toastr.warning("请先选择要修改的记录！")
                return;
            }
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceProjectBudgetService.removeDetail(detailId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            $('#treeTable').bootstrapTreeTable('destroy');
                            vm.save();
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            vm.detail.projectBudgetId = projectBudgetId;
            vm.detail.creator = user.userLogin;
            fiveFinanceProjectBudgetService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();

                }
            })
        };
        vm.showParentTree = function (item) {
            fiveFinanceProjectBudgetService.getDetailById(projectBudgetId).then(function (value) {
                if (value.data.ret) {
                    var list = value.data.data;
                    var treeData = [];
                    for (var i = 0; i < list.length; i++) {
                        var data = list[i];
                        var node = {
                            id: data.id,
                            parent: (data.parentId == 0 ? "#" : data.parentId.toString()),
                            text: data.typeName,
                            icon: data.icon,
                            state: {opened: true, disabled: false, selected: false}
                        };
                        if (data.id == item.parentId) {
                            node.state.selected = true;
                        }
                        treeData.push(node);
                    }
                    $('#detail_parent_tree').jstree("destroy");
                    $('#detail_parent_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            }
                        });
                    $("#detailParentTreeModal").modal("show");
                }
            });
        };
        vm.chooseDetailParent = function () {
            var data = $('#detail_parent_tree').jstree(true).get_selected(true)[0];
            vm.detail.parentId = data.id;
            vm.detail.parentName = data.text;
            $("#detailParentTreeModal").modal("hide");
        }
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceProjectBudgetService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceInvoiceService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applyMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.status == 'deptChargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门负责人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
            if (vm.status == 'remindReceiveMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择催收责任人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applyMan') {
                vm.item.applyManName = $scope.selectedOaUserNames_;
                vm.item.applyMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'deptChargeMan') {
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceProjectBudgetService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }


        //合同库
        vm.showContractLibraryModal = function () {
            fiveBusinessContractLibraryService.selectAll(user.userLogin, "five.businessContractLibrary").then(function (value) {
                if (value.data.ret) {
                    vm.librarys = value.data.data;
                    singleCheckBox(".cb_library", "data-name");
                }
            })
            $("#selectLibraryModal").modal("show");
        };
        vm.saveSelectModel = function () {
            if ($(".cb_library:checked").length > 0) {
                var library = $.parseJSON($(".cb_library:checked").first().attr("data-name"));
                //需要从 合同库 中同步到 发票申请 的数据
                vm.item.contractName = library.contractName;
                vm.item.contractNo = library.contractNo;
                vm.item.contractLibraryId = library.id;
                vm.item.projectName = library.projectName;
                vm.item.projectType = library.projectNature;
            }
            vm.save();
            $("#selectLibraryModal").modal("hide");
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //资金余额上报
    .controller("FiveFinanceBalanceController", function ($state, $scope, $rootScope, fiveFinanceBalanceService) {
        var vm = this;
        var uiSref = "finance.balance";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceBalanceService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.balanceDetail", {balanceId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceBalanceService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceBalanceService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceBalanceDetailController", function ($state, $stateParams, $scope, $rootScope,fiveContentFileService,fiveFinanceBalanceService) {
        var vm = this;
        var uiSref = "finance.balance";
        var balanceId = $stateParams.balanceId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        /*加载 红头文件和文档*/
        vm.loadDoc=function(){
            fiveContentFileService.getModelByBusinessKey(vm.item.businessKey,0).then(function (value) {
                if (value.data.ret){
                    vm.redHead=value.data.data;
                }
            })
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceBalanceService.getModelById(balanceId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.loadContentFiles(vm.item.businessKey,true);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceBalanceService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceBalanceService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }


        vm.showUserModel = function () {
            if (vm.opt == 'businessUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参与经营人员",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'businessUser') {
                vm.item.businessUserName = $scope.selectedOaUserNames_;
                vm.item.businessUser = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceBalanceService.update(vm.item).then(function (value) {
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


        vm.showDeptModal = function () {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择上报部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //事业部预算
    .controller("FiveFinanceDeptBudgetController", function ($state, $scope, $rootScope, fiveFinanceDeptBudgetService) {
        var vm = this;
        var uiSref = "finance.deptBudget";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceDeptBudgetService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.deptBudgetDetail", {deptBudgetId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceDeptBudgetService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceDeptBudgetService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }
        vm.removeReplenish = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceDeptBudgetService.removeReplenish(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceDeptBudgetDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveFinanceDeptBudgetService, fiveBusinessContractLibraryService) {
        var vm = this;
        var uiSref = "finance.deptBudget";
        var deptBudgetId = $stateParams.deptBudgetId;
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceDeptBudgetService.getModelById(deptBudgetId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#invoiceTime").datepicker('setDate', vm.item.invoiceTime);
                    $("#receiveTime").datepicker('setDate', vm.item.receiveTime);
                    $("#financeConfirmTime").datepicker('setDate', vm.item.financeConfirmTime);
                }
            })

            fiveFinanceDeptBudgetService.getDetailById(deptBudgetId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceDeptBudgetService.getNewModelDetail(deptBudgetId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            } else {
                fiveFinanceDeptBudgetService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {

            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceDeptBudgetService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            $('#treeTable').bootstrapTreeTable('destroy');
                            vm.save();
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            vm.detail.deptBudgetId = deptBudgetId;
            vm.detail.creator = user.userLogin;
            fiveFinanceDeptBudgetService.updateDetail(vm.detail,deptBudgetId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceDeptBudgetService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceInvoiceService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applyMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.status == 'deptChargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门负责人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
            if (vm.status == 'remindReceiveMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择催收责任人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applyMan') {
                vm.item.applyManName = $scope.selectedOaUserNames_;
                vm.item.applyMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'deptChargeMan') {
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceDeptBudgetService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (type) {
            vm.deptType = type;
            if (vm.deptType == "detail") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: vm.item.deptId,
                    deptIdList: vm.item.deptId,
                    parentDeptId: vm.item.deptId,
                    multiple: false
                });
            } else if (vm.deptType == "item") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: '1',
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }

        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptType == 'item') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            } else if (vm.deptType == 'detail') {
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })
    //部门资金
    .controller("FiveFinanceDeptFundController", function ($state, $scope, $rootScope, fiveFinanceDeptFundService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.deptFund";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.buildTree();
        }
        vm.refreshDept = function(){
            fiveFinanceDeptFundService.refreshDept().then(function (value) {
                if (value.data.ret) {
                    toastr.success("同步部门成功!");
                }
            })
        }
        vm.buildTree = function () {
            selectId=parseInt(user.deptId);
            hrDeptService.selectAllByUiSref(user.userLogin,uiSref).then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        state: {opened: item.parentId===0, disabled: false, selected: false}
                    };
                    if (item.id === selectId||(selectId===0&&item.parentId===0)) {
                        node.state.selected = true;
                        node.state.opened = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_tree').jstree("destroy");
                $('#dept_tree')
                    .on('changed.jstree', function (e, data) {
                        var node=data.instance.get_node(data.selected[0]);
                        deptId=node.id;
                        fiveFinanceDeptFundService.getModelByDeptId(deptId).then(function (value) {
                            if (value.data.ret) {
                               vm.item =value.data.data;
                            }
                        })

                    })
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        }
                    });

            });
        };

        vm.show = function (url,urlIdName,urlId) {
            if(urlIdName=="travelExpenseId"){
                $state.go(url,{travelExpenseId:urlId});
            }
        }
        vm.refreshMoney = function(deptId){
            fiveFinanceDeptFundService.refreshMoney(deptId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("跟新部门金额成功!");
                    fiveFinanceDeptFundService.getModelByDeptId(deptId).then(function (value) {
                        if (value.data.ret) {
                            vm.item =value.data.data;
                        }
                    })
                }
            })
        }
        vm.showDeptBudget = function (id) {
            $state.go("finance.deptBudgetDetail", {deptBudgetId: id});
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceDeptFundDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveFinanceDeptFundService, fiveBusinessContractLibraryService) {
        var vm = this;
        var uiSref = "finance.deptFund";
        var deptFundId = $stateParams.deptFundId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceDeptFundService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applyMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.status == 'deptChargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门负责人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
            if (vm.status == 'remindReceiveMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择催收责任人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applyMan') {
                vm.item.applyManName = $scope.selectedOaUserNames_;
                vm.item.applyMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'deptChargeMan') {
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        vm.showDeptModal = function (type) {
            vm.deptType = type;
            if (vm.deptType == "detail") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: vm.item.deptId,
                    deptIdList: vm.item.deptId,
                    parentDeptId: vm.item.deptId,
                    multiple: false
                });
            } else if (vm.deptType == "item") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: '1',
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }

        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptType == 'item') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            } else if (vm.deptType == 'detail') {
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        vm.init();

        return vm;

    })
    //印花税申报
    .controller("FiveFinanceStampTaxController", function ($state, $scope, $rootScope, fiveFinanceStampTaxService) {
        var vm = this;
        var uiSref = "finance.stampTax";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceStampTaxService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.stampTaxDetail", {stampTaxId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceStampTaxService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceStampTaxService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }
        vm.removeReplenish = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceStampTaxService.removeReplenish(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.changeOpenStamp = function (id) {
            bootbox.confirm("您确定修改该合同印花税状态吗", function (result) {
                if (result) {
                    fiveFinanceStampTaxService.changeOpenStamp(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("切换成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        }

        //导出excel
        vm.downData =function(){
            fiveFinanceStampTaxService.downData(user.userLogin).then(function (response) {
                var blob = new Blob([response.data.data], {type: response.data.data.type});
                if (response.data.data.type === "application/json") {
                    var reader = new FileReader();
                    reader.readAsText(blob, 'utf-8');
                    reader.onload = function (e) {
                        var result = jQuery.parseJSON(reader.result);
                        toastr.error(result.msg);
                    }
                    return;
                }
                var objectUrl = URL.createObjectURL(blob);
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                if ('msSaveOrOpenBlob' in navigator) {
                    //ie使用的下载方式
                    window.navigator.msSaveOrOpenBlob(blob, fileName);
                } else {
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = decodeURI(fileName);
                    a.href = objectUrl;
                    a.click();
                }
            })
        }
        vm.init();

        return vm;
    })
    .controller("FiveFinanceStampTaxDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveFinanceStampTaxService, fiveBusinessContractLibraryService) {
        var vm = this;
        var uiSref = "finance.stampTax";
        var stampTaxId = $stateParams.stampTaxId;
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceStampTaxService.getModelById(stampTaxId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        //$scope.loadProcessInstance(vm.item.processInstanceId);
                        $rootScope.getTplConfig("",vm.item.businessKey,user.userLogin);
                        $scope.basicInit(vm.item.businessKey);

                    }
                    $("#invoiceTime").datepicker('setDate', vm.item.invoiceTime);
                    $("#receiveTime").datepicker('setDate', vm.item.receiveTime);
                    $("#financeConfirmTime").datepicker('setDate', vm.item.financeConfirmTime);
                }
            })

           /* fiveFinanceStampTaxService.getDetailById(stampTaxId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })*/
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceStampTaxService.getNewModelDetail(stampTaxId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            } else {
                fiveFinanceStampTaxService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceStampTaxService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            $('#treeTable').bootstrapTreeTable('destroy');
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            vm.detail.stampTaxId = stampTaxId;
            vm.detail.creator = user.userLogin;
            fiveFinanceStampTaxService.updateDetail(vm.detail,stampTaxId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadData(true)
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceStampTaxService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveFinanceInvoiceService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applyMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.status == 'deptChargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门负责人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
            if (vm.status == 'remindReceiveMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择催收责任人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applyMan') {
                vm.item.applyManName = $scope.selectedOaUserNames_;
                vm.item.applyMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'deptChargeMan') {
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
            }
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }
        };



        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceStampTaxService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (type) {
            vm.deptType = type;
            if (vm.deptType == "detail") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: vm.item.deptId,
                    deptIdList: vm.item.deptId,
                    parentDeptId: vm.item.deptId,
                    multiple: false
                });
            } else if (vm.deptType == "item") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: '1',
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }

        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptType == 'item') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            } else if (vm.deptType == 'detail') {
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //合同库
     /*   vm.showContractLibraryModal = function () {
            fiveBusinessContractLibraryService.selectAllNotHaveStampTax(vm.item.contractId,user.userLogin, "five.businessContractLibrary").then(function (value) {
                if (value.data.ret) {
                    vm.librarys = value.data.data;
                    singleCheckBox(".cb_library", "data-name");
                }
            })
            $("#selectLibraryModal").modal("show");
        };
        vm.saveSelectModel = function () {
            if ($(".cb_library:checked").length > 0) {
                var library = $.parseJSON($(".cb_library:checked").first().attr("data-name"));
                //需要从 合同库 中同步到 印花税申报 的数据
                vm.item.contractName = library.contractName;
                vm.item.contractNo = library.contractNo;
                vm.item.contractId = library.id;
                vm.item.projectNo =library.projectNo;
                vm.item.projectName = library.projectName;
                vm.item.signTime = library.signTime;
                vm.item.contractMoney = library.contractMoney;
                vm.item.customerName = library.customerName;
                vm.item.contractDeptId = library.deptId;
                vm.item.contractDeptName = library.deptName;
                vm.save();
            }

            $("#selectLibraryModal").modal("hide");
        }*/
        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.contractId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.item.contractName = $rootScope.selectedLibrary.contractName;
            vm.item.contractNo = $rootScope.selectedLibrary.contractNo;
            vm.item.contractId = $rootScope.selectedLibrary.id;
            vm.item.projectNo =$rootScope.selectedLibrary.projectNo;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.signTime = $rootScope.selectedLibrary.signTime;
            vm.item.contractMoney = $rootScope.selectedLibrary.contractMoney;
            vm.item.customerName = $rootScope.selectedLibrary.customerName;
            vm.item.contractDeptId = $rootScope.selectedLibrary.deptId;
            vm.item.contractDeptName = $rootScope.selectedLibrary.deptName;
            $scope.closeLibrarySelectModal_();
        }

        vm.downloadModelExcel =function(){
            fiveFinanceStampTaxService.downloadModelExcel({stampTaxId:stampTaxId,
                userLogin:user.userLogin,uiSref:uiSref}).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=","");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            })
        }


        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })
    //对外提供资料
    .controller("FiveFinanceOutSupplyController", function ($state, $scope, $rootScope, fiveFinanceOutSupplyService) {
        var vm = this;
        var uiSref = "finance.outSupply";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            fiveFinanceOutSupplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("finance.outSupplyDetail", {outSupplyId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveFinanceOutSupplyService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveFinanceOutSupplyService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })
    .controller("FiveFinanceOutSupplyDetailController", function ($state, $stateParams, $scope, $rootScope,commonCodeService,fiveContentFileService,fiveFinanceOutSupplyService) {
        var vm = this;
        var uiSref = "finance.outSupply";
        var outSupplyId = $stateParams.outSupplyId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceOutSupplyService.getModelById(outSupplyId).then(function (value) {
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
            fiveFinanceOutSupplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //资料类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "对外提供资料类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                }
            })
        }
        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type = type;
            vm.save();
        }

        vm.showUserModel = function () {
            if (vm.opt == 'businessUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参与经营人员",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.businessUser,
                    multiple: true
                });
            }
            if (vm.opt == 'messageUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.messageUser,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'businessUser') {
                vm.item.businessUserName = $scope.selectedOaUserNames_;
                vm.item.businessUser = $scope.selectedOaUserLogins_;
            }
            if (vm.opt == 'messageUser') {
                vm.item.messageUserName = $scope.selectedOaUserNames_;
                vm.item.messageUser = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceOutSupplyService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择登记部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }
            if (vm.opt == 'messageDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择信息提供部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.messageDeptId + ",",
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'deptId') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (vm.opt == 'messageDeptId') {
                vm.item.messageDeptName = $scope.selectedOaDeptNames_;
                vm.item.messageDeptId = Number($scope.selectedOaDeptIds_);
            }
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //保函管理
    .controller("FiveFinanceBackLetterController", function ($state, $scope, fiveFinanceBackLetterService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "保函管理",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.backLetter";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceBackLetterService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.backLetterDetail", {backLetterId: id});
        }


        vm.add = function () {
            fiveFinanceBackLetterService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceBackLetterService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceBackLetterDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceBackLetterService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.backLetter";
        var backLetterId = $stateParams.backLetterId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceBackLetterService.getModelById(backLetterId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#assureDate").datepicker('setDate', vm.item.assureDate);
                    $("#assureDateEnd").datepicker('setDate', vm.item.assureDateEnd);
                    $("#backLetterDate").datepicker('setDate', vm.item.backLetterDate);
                    $("#continueDate").datepicker('setDate', vm.item.continueDate);
                    $("#continueDateEnd").datepicker('setDate', vm.item.continueDateEnd);
                    $("#cancelDate").datepicker('setDate', vm.item.cancelDate);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceBackLetterService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'user') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择填报",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'user') {
                vm.item.user = $scope.selectedOaUserLogins_;
                vm.item.userName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceBackLetterService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.print = function () {
            fiveFinanceBackLetterService.getPrintData(backLetterId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //金算盘链接
    .controller("FiveFinanceEntranceController", function ($state, $scope) {
        var vm = this;

        vm.init = function () {
            window.open("http://10.56.230.36:8888/core/main");
        };

        vm.init();

        return vm;

    })



    //费用申请-生产部门
    .controller("FiveFinanceTransferAccountsController", function ($state, $scope, fiveFinanceTransferAccountsService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用申请",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.transferAccounts";
        vm.showTopTitle='费用退款(生产部门)';

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTransferAccountsService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.transferAccountsDetail", {transferAccountsId: id});
        }


        vm.add = function () {
            fiveFinanceTransferAccountsService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTransferAccountsService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTransferAccountsDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceTransferAccountsService,fiveBusinessContractLibraryService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.transferAccounts";
        var transferAccountsId = $stateParams.transferAccountsId;
        vm.showTopTitle='费用退款(生产部门)';

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTransferAccountsService.getModelById(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);

                }
            })
            fiveFinanceTransferAccountsService.listDetail(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTransferAccountsService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
        };
        //单据号
        vm.getAccountNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceTransferAccountsService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceTransferAccountsService.getAccountNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.projectName.length!=0&&vm.item.businessManagerName == "") {
                toastr.warning("请填写 项目经理/总设计师!");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTransferAccountsService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear:vm.item.applicantTime.substring(0,4),
                budgetType:vm.detail.budgetType
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.chargePlan=_item.typeName;
                vm.detail.deduction=_item.remainMoney;
            });
            vm.detail.budgetType=$rootScope.budgetConfig.budgetType;
            $scope.closeBudgetSelectModal_();
        }

        //选择个人网银
        vm.showBankSelect = function (id) {

            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.accountName = $rootScope.selectBank.userName ;
                vm.item.outBankAccount = $rootScope.selectBank.bankNo ;
                vm.item.outBankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.inBankAccount = $rootScope.selectBank.bankNo ;
                vm.item.inBankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }
        //新增
        vm.showDetailModel = function (id) {
            if (id==0) {
                fiveFinanceTransferAccountsService.getNewModelDetail(transferAccountsId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadData();
                    }
                })
                //修改
            } else {
                fiveFinanceTransferAccountsService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }

        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTransferAccountsService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTransferAccountsService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            //vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }


        vm.print = function () {
            fiveFinanceTransferAccountsService.getPrintData(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })

    //费用申请-红河项目
    .controller("FiveFinanceTransferAccountsRedController", function ($state, $scope, fiveFinanceTransferAccountsService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用申请",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.transferAccountsRed";
        vm.showTopTitle='费用退款(红河项目)';

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTransferAccountsService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.transferAccountsRedDetail", {transferAccountsId: id});
        }

        vm.add = function () {
            fiveFinanceTransferAccountsService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTransferAccountsService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTransferAccountsRedDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceTransferAccountsService,fiveBusinessContractLibraryService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.transferAccountsRed";
        var transferAccountsId = $stateParams.transferAccountsId;
        vm.showTopTitle='费用退款(红河项目)';
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTransferAccountsService.getModelById(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);

                }
            })
            fiveFinanceTransferAccountsService.listDetail(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTransferAccountsService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
        };

        //单据号
        vm.getAccountNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceTransferAccountsService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceTransferAccountsService.getAccountNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.projectName.length!=0&&vm.item.businessManagerName == "") {
                toastr.warning("请填写 项目经理/总设计师!");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTransferAccountsService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear:vm.item.applicantTime.substring(0,4)
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.chargePlan=_item.typeName;
                vm.detail.deduction=_item.remainMoney;
            });
            $scope.closeBudgetSelectModal_();
        }

        //选择个人网银
        vm.showBankSelect = function (id) {

            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.accountName = $rootScope.selectBank.userName ;
                vm.item.outBankAccount = $rootScope.selectBank.bankNo ;
                vm.item.outBankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.inBankAccount = $rootScope.selectBank.bankNo ;
                vm.item.inBankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }
        //新增
        vm.showDetailModel = function (id) {
            if (id==0) {
                fiveFinanceTransferAccountsService.getNewModelDetail(transferAccountsId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceTransferAccountsService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTransferAccountsService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTransferAccountsService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true)
                }
            })
        };

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };

        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            //vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }

        vm.print = function () {
            fiveFinanceTransferAccountsService.getPrintData(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })

    //费用申请-建研院
    .controller("FiveFinanceTransferAccountsBuildController", function ($state, $scope, fiveFinanceTransferAccountsService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用申请",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.transferAccountsBuild";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTransferAccountsService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.transferAccountsBuildDetail", {transferAccountsId: id});
        }


        vm.add = function () {

            fiveFinanceTransferAccountsService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTransferAccountsService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTransferAccountsBuildDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceTransferAccountsService,fiveBusinessContractLibraryService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.transferAccountsBuild";
        var transferAccountsId = $stateParams.transferAccountsId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTransferAccountsService.getModelById(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);

                }
            })
            fiveFinanceTransferAccountsService.listDetail(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTransferAccountsService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
        };

       //单据号
        vm.getAccountNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceTransferAccountsService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceTransferAccountsService.getAccountNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
/*            if (vm.item.projectName.length!=0&&vm.item.businessManagerName == "") {
                toastr.warning("请填写 项目经理/总设计师!");
                return;
            }*/
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTransferAccountsService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear:vm.item.applicantTime.substring(0,4)
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.chargePlan=_item.typeName;
                vm.detail.deduction=_item.remainMoney;
            });
            $scope.closeBudgetSelectModal_();
        }

        //选择个人网银
        vm.showBankSelect = function (id) {

            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.accountName = $rootScope.selectBank.userName ;
                vm.item.outBankAccount = $rootScope.selectBank.bankNo ;
                vm.item.outBankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.inBankAccount = $rootScope.selectBank.bankNo ;
                vm.item.inBankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }
        //新增
        vm.showDetailModel = function (id) {
            if (id==0) {
                fiveFinanceTransferAccountsService.getNewModelDetail(transferAccountsId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceTransferAccountsService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTransferAccountsService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTransferAccountsService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            //vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }

        vm.print = function () {
            fiveFinanceTransferAccountsService.getPrintData(transferAccountsId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })

    //费用申请-财务转账
    .controller("FiveFinanceTransferFeeController", function ($state, $scope, fiveFinanceTransferFeeService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用申请",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.transferFee";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTransferFeeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.transferFeeDetail", {transferFeeId: id});
        }

        vm.add = function () {
            fiveFinanceTransferFeeService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTransferFeeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTransferFeeDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceTransferFeeService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.transferFee";
        var transferFeeId = $stateParams.transferFeeId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTransferFeeService.getModelById(transferFeeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);

                }
            })
            fiveFinanceTransferFeeService.listDetail(transferFeeId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTransferFeeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTransferFeeService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceTransferFeeService.getNewModelDetail(transferFeeId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceTransferFeeService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTransferFeeService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTransferFeeService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };

        vm.print = function () {
            fiveFinanceTransferFeeService.getPrintData(transferFeeId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })

    //借款-生产部门
    .controller("FiveFinanceLoanController", function ($state, $scope, fiveFinanceLoanService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "借款",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.loan";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceLoanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.loanDetail", {loanId: id});
        }


        vm.add = function () {
            fiveFinanceLoanService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceLoanService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceLoanDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceLoanService) {
        var vm = this;
        var uiSref = "finance.loan";
        var loanId = $stateParams.loanId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceLoanService.getModelById(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceLoanService.listDetail(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceLoanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showUserModel = function () {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择申报人员",
                type: '部门',
                deptIds: 1,
                userLoginList: vm.item.userLogin,
                multiple: true
            });

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.applicantName = $scope.selectedOaUserNames_;
            vm.item.applican = $scope.selectedOaUserLogins_;
        };

        //单据号
        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceLoanService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceLoanService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceLoanService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {

            $scope.showOaSelectEmployeeModal_({
                title: "请选择所属单位",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.item.deptId,
                multiple: false


            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceLoanService.getNewModelDetail(loanId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceLoanService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadData(true);
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceLoanService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceLoanService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };

        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }

        vm.showBankSelect = function (id) {
            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }


        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear: vm.item.applicantTime.substring(0,4)
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetBalance=_item.remainMoney;
                vm.detail.budgetNo=_item.typeName;
                vm.detail.budgetId=_item.id; //预算子表id
            });
            $scope.closeBudgetSelectModal_();
        }


        vm.print = function () {
            fiveFinanceLoanService.getPrintData(loanId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();

        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })

    //借款-管理部门
    .controller("FiveFinanceLoanFunctionController", function ($state, $scope, fiveFinanceLoanService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "借款",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.loanFunction";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceLoanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.loanFunctionDetail", {loanId: id});
        }


        vm.add = function () {
            fiveFinanceLoanService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceLoanService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceLoanFunctionDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceLoanService, fiveBusinessContractLibraryService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.loanFunction";
        var loanId = $stateParams.loanId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceLoanService.getModelById(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceLoanService.listDetail(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceLoanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceLoanService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceLoanService.getNewModelDetail(loanFunctionId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceLoanService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceLoanService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceLoanService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }

        vm.showBankSelect = function (id) {

            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear:vm.item.applicantTime.substring(0,4)
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetBalance=_item.remainMoney;
                vm.detail.budgetNo=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.print = function () {
            fiveFinanceLoanService.getPrintData(loanId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //借款-红河项目
    .controller("FiveFinanceLoanRedController", function ($state, $scope, fiveFinanceLoanService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "借款",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.loanRed";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceLoanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.loanRedDetail", {loanId: id});
        }


        vm.add = function () {
            fiveFinanceLoanService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceLoanService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceLoanRedDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceLoanService, fiveBusinessContractLibraryService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.loanRed";
        var loanId = $stateParams.loanId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceLoanService.getModelById(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceLoanService.listDetail(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceLoanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
        };
        //单据号
        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceLoanService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceLoanService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceLoanService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceLoanService.getNewModelDetail(loanId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceLoanService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceLoanService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceLoanService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }

            })
        };

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.businessChargeLeader;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessChargeLeaderName;
            $scope.closeLibrarySelectModal_();
        }

        vm.showBankSelect = function (id) {
            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear:vm.item.applicantTime.substring(0,4)
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.budgetBalance=_item.remainMoney;
                vm.detail.budgetNo=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.print = function () {
            fiveFinanceLoanService.getPrintData(loanRedId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //借款-建研院
    .controller("FiveFinanceLoanBuildController", function ($state, $scope, fiveFinanceLoanService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "借款",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.loanBuild";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceLoanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.loanBuildDetail", {loanId: id});
        }


        vm.add = function () {
            fiveFinanceLoanService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceLoanService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceLoanBuildDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceLoanService, fiveBusinessContractLibraryService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.loanBuild";
        var loanId = $stateParams.loanId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceLoanService.getModelById(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceLoanService.listDetail(loanId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceLoanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
        };
        //单据号
        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceLoanService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceLoanService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceLoanService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceLoanService.getNewModelDetail(loanId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceLoanService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceLoanService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceLoanService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }

            })
        };

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }

        vm.showBankSelect = function (id) {

            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear:vm.item.applicantTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetBalance=_item.remainMoney;
                vm.detail.budgetNo=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.print = function () {
            fiveFinanceLoanService.getPrintData(loanId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })

    //还款-生产部门、管理部门、红河项目、建研院
    .controller("FiveFinanceRefundController", function ($state, $scope, fiveFinanceRefundService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "还款",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.refund";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceRefundService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.refundDetail", {refundId: id});
        }


        vm.add = function () {
            fiveFinanceRefundService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceRefundService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceRefundDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveFinanceRefundService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.refund";

        var refundId = $stateParams.refundId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceRefundService.getModelById(refundId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);

                }
            })
            fiveFinanceRefundService.listDetail(refundId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceRefundService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'refundMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择借款人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.refundMan,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'refundMan') {
                vm.detail.refundMan = $scope.selectedOaUserLogins_;
                vm.detail.refundManName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceRefundService.update(vm.item).then(function (value) {
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

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //新增
        vm.showDetailModel = function (id) {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceRefundService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    if (id === 0) {
                        fiveFinanceRefundService.getNewModelDetail(refundId).then(function (value) {
                            if (value.data.ret) {
                                vm.detail = value.data.data;
                                $("#detailModal").modal("show");
                            }
                        })
                    } else {
                        fiveFinanceRefundService.getModelDetailById(id).then(function (value) {
                            if (value.data.ret) {
                                vm.detail = value.data.data;
                                $("#detailModal").modal("show");
                            }
                        })
                    }
                }
            })

        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceRefundService.removeDetail(id, user.userLogin).then(function (value) {
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
            fiveFinanceRefundService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadData(true);
                }
            })
        };

        vm.showBankSelect = function (id) {
            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            $scope.closeLibrarySelectModal_();
        }

        vm.print = function () {
            fiveFinanceRefundService.getPrintData(refundId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })


    //费用报销-生产
    .controller("FiveFinanceReimburseController", function ($state, $scope, fiveFinanceReimburseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用报销",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.reimburse";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceReimburseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.reimburseDetail", {reimburseId: id});
        }


        vm.add = function () {
            fiveFinanceReimburseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceReimburseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceReimburseDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, actService,hrEmployeeService, fiveFinanceLoanService,fiveFinanceReimburseService, fiveFinanceRefundService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.reimburse";
        var reimburseId = $stateParams.reimburseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceReimburseService.getModelById(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceReimburseService.listDetail(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceReimburseService.listDeduction(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //选人
        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.businessManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }
        //保存选人
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }

        };
        //单据号
        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceReimburseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceReimburseService.update(vm.item).then(function (value) {
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
        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceReimburseService.getNewModelDetail(reimburseId, user.userLogin,uiSref).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceReimburseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadData(true);
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceReimburseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }
        vm.print = function () {
            fiveFinanceReimburseService.getPrintData(reimburseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }

        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
            /*if(deduction.relevanceType=='loan'){
                $state.go("finance.loanDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundDetail", {refundId: deduction.relevanceId});
            }*/

            actService.getNgRedirectUrl(deduction.relevanceBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })

        }

        vm.showBankSelect = function (type) {
            vm.bankType=type;
            if(type==1){
                $scope.showBankSelectModal_({
                    selectBankType:"company",
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.bankAccount
                });
            } else if(type==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.accountName
                });
            }
        }
        $rootScope.saveSelectBank_ = function () {
            if(vm.bankType==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.accountName = $rootScope.selectBank.userName ;
                vm.item.bankAccount = $rootScope.selectBank.bankNo ;
                vm.item.bankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.bankType==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }
        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear: vm.item.applicantTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {

            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetBalance=_item.remainMoney;
                vm.detail.budgetNo=_item.typeName;
                vm.detail.budgetId=_item.id; //预算子表id
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.getNewReplenishRefund = function () {
            fiveFinanceReimburseService.getNewReplenishRefund(reimburseId,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.loadData(false);
                }
            })
        };




        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //费用报销-职能
    .controller("FiveFinanceReimburseFunctionController", function ($state, $scope, fiveFinanceReimburseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用报销",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.reimburseFunction";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceReimburseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.reimburseFunctionDetail", {reimburseId: id});
        }


        vm.add = function () {
            fiveFinanceReimburseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceReimburseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceReimburseFunctionDetailController", function ($sce, $state, $stateParams, $rootScope, $scope,actService, hrEmployeeService, fiveFinanceLoanService,fiveFinanceReimburseService, fiveFinanceRefundService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.reimburseFunction";
        var reimburseId = $stateParams.reimburseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceReimburseService.getModelById(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceReimburseService.listDetail(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceReimburseService.listDeduction(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //选人
        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.businessManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }
        //保存选人
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }

        };
        //单据号
        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceReimburseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceReimburseService.update(vm.item).then(function (value) {
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
        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceReimburseService.getNewModelDetail(reimburseId, user.userLogin,uiSref).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceReimburseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceReimburseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }
        vm.print = function () {
            fiveFinanceReimburseService.getPrintData(reimburseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }
        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
          /*  if(deduction.relevanceType=='loan'){
                $state.go("finance.loanFunctionDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundFunctionDetail", {refundId: deduction.relevanceId});
            }*/
            actService.getNgRedirectUrl(deduction.relevanceBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })

        }
        vm.showBankSelect = function (type) {
            vm.bankType=type;
            if(type==1){
                $scope.showBankSelectModal_({
                    selectBankType:"company",
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.bankAccount
                });
            } else if(type==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.accountName
                });
            }
        }
        $rootScope.saveSelectBank_ = function () {
            if(vm.bankType==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.accountName = $rootScope.selectBank.userName ;
                vm.item.bankAccount = $rootScope.selectBank.bankNo ;
                vm.item.bankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.bankType==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }
        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear: vm.item.applicantTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {

            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                 vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.costProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }
        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //费用报销-红河
    .controller("FiveFinanceReimburseRedController", function ($state, $scope, fiveFinanceReimburseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用报销",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.reimburseRed";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceReimburseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.reimburseRedDetail", {reimburseId: id});
        }


        vm.add = function () {
            fiveFinanceReimburseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceReimburseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceReimburseRedDetailController", function ($sce, $state, $stateParams, $rootScope, $scope,actService, hrEmployeeService, fiveFinanceLoanService,fiveFinanceReimburseService, fiveFinanceRefundService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.reimburseRed";
        var reimburseId = $stateParams.reimburseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceReimburseService.getModelById(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceReimburseService.listDetail(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceReimburseService.listDeduction(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //选人
        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.businessManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }
        //保存选人
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }

        };
        //单据号
        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceReimburseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceReimburseService.update(vm.item).then(function (value) {
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
        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceReimburseService.getNewModelDetail(reimburseId, user.userLogin,uiSref).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceReimburseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceReimburseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
/*            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;*/
            vm.item.businessManager= $rootScope.selectedLibrary.businessChargeLeader;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessChargeLeaderName;
            $scope.closeLibrarySelectModal_();
        }
        vm.print = function () {
            fiveFinanceReimburseService.getPrintData(reimburseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }
        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
            /*if(deduction.relevanceType=='loan'){
                $state.go("finance.loanRedDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundRedDetail", {refundId: deduction.relevanceId});
            }*/
            actService.getNgRedirectUrl(deduction.relevanceBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })

        }
        vm.showBankSelect = function (type) {
            vm.bankType=type;
            if(type==1){
                $scope.showBankSelectModal_({
                    selectBankType:"company",
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.bankAccount
                });
            } else if(type==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.accountName
                });
            }
        }
        $rootScope.saveSelectBank_ = function () {
            if(vm.bankType==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.accountName = $rootScope.selectBank.userName ;
                vm.item.bankAccount = $rootScope.selectBank.bankNo ;
                vm.item.bankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.bankType==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }
        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear: vm.item.applicantTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {

            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                 vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.costProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }
        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //费用报销-建研院
    .controller("FiveFinanceReimburseBuildController", function ($state, $scope, fiveFinanceReimburseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "费用报销",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.reimburseBuild";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceReimburseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.reimburseBuildDetail", {reimburseId: id});
        }


        vm.add = function () {
            fiveFinanceReimburseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceReimburseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceReimburseBuildDetailController", function ($sce, $state, $stateParams, $rootScope, $scope,actService, hrEmployeeService, fiveFinanceLoanService,fiveFinanceReimburseService, fiveFinanceRefundService,hrDeptService) {
        var vm = this;
        var uiSref = "finance.reimburseBuild";
        var reimburseId = $stateParams.reimburseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceReimburseService.getModelById(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                }
            })
            fiveFinanceReimburseService.listDetail(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceReimburseService.listDeduction(reimburseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //选人
        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicantMan,
                    multiple: true
                });
            }
            if (vm.status == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.businessManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }
        //保存选人
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }

        };
        //单据号
        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceReimburseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceReimburseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceReimburseService.update(vm.item).then(function (value) {
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
        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceReimburseService.getNewModelDetail(reimburseId, user.userLogin,uiSref).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceReimburseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceReimburseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:vm.item.projectId,
                uiSref:uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            /*            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
                        vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
                        vm.detail.contractId = $rootScope.selectedLibrary.id;
                        vm.detail.projectNo =$rootScope.selectedLibrary.projectNo;
                        vm.detail.signTime = $rootScope.selectedLibrary.signTime;
                        vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
                        vm.detail.customerName = $rootScope.selectedLibrary.customerName;*/
            vm.item.projectId = $rootScope.selectedLibrary.id;
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            vm.item.projectType = $rootScope.selectedLibrary.projectNature;
            vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
            $scope.closeLibrarySelectModal_();
        }
        vm.print = function () {
            fiveFinanceReimburseService.getPrintData(reimburseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByDeptId(user.userLogin,158).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByDeptId(user.userLogin,158).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));
                fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        vm.loadData(false);
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }
        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceReimburseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
          /*  if(deduction.relevanceType=='loan'){
                $state.go("finance.loanBuildDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundBuildDetail", {refundId: deduction.relevanceId});
            }*/
            actService.getNgRedirectUrl(deduction.relevanceBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })

        }

        vm.showBankSelect = function (type) {
            vm.bankType=type;
            if(type==1){
                $scope.showBankSelectModal_({
                    selectBankType:"company",
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.bankAccount
                });
            } else if(type==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.accountName
                });
            }
        }
        $rootScope.saveSelectBank_ = function () {
            if(vm.bankType==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.accountName = $rootScope.selectBank.userName ;
                vm.item.bankAccount = $rootScope.selectBank.bankNo ;
                vm.item.bankName = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.bankType==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptId,
                budgetYear: vm.item.applicantTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {

            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.costProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }
        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })

    //差旅费报销-生产
    .controller("FiveFinanceTravelExpenseController", function ($state, $scope, fiveFinanceTravelExpenseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "差旅费",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.travelExpense";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTravelExpenseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.travelExpenseDetail", {travelExpenseId: id});
        }

        vm.add = function () {
            fiveFinanceTravelExpenseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTravelExpenseDetailController", function ($sce, $state, $stateParams, $rootScope, $scope,actService, fiveFinanceRefundService,fiveFinanceLoanService, hrEmployeeService, fiveFinanceTravelExpenseService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.travelExpense";
        var travelExpenseId = $stateParams.travelExpenseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.countAllowance = function(isChecked,name){
            console.log(name)
            var count = 0;
            // vm.detail.onRoadSubsidy = vm.detail.onRoadSubsidy==null? 0 : parseFloat(vm.detail.onRoadSubsidy);

            if(name == "住宿费补助" && isChecked){
                vm.detail.accommodationAllowance = true
            }
            if(name == "出差补助" && isChecked){
                vm.detail.travelAllowance = true
            }
            if(name == "夜间伙补" && isChecked){
                vm.detail.dinnerAllowance = true
            }
            if(name == "工地补贴" && isChecked){
                vm.detail.siteAllowance = true
            }
            if (vm.detail.accommodationAllowance){
                count = count + 0.0050
            }
            if (vm.detail.travelAllowance){
                count = count + 0.0150
            }
            if (vm.detail.dinnerAllowance){
                count = count + 0.0100
            }
            if (vm.detail.siteAllowance){
                count = count + 0.0050
            }
            var bonus = vm.detail.travelExpenseDays;
            //确保输入的是数字
            bonus = bonus.replace(/[^\d\.]/g, '');
            //确保第一个输入的是数字
            bonus = bonus.replace(/^\./g,'');
            //确保不能输入两个小数点
            bonus = bonus.replace(/\.{2,}/g,'.');
            //保证小数点只出现一次，而不能出现两次以上
            bonus = bonus.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
            //确保只能输入两位小数
            bonus = bonus.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
            if (bonus == null){
                vm.detail.travelExpenseDays = 0;
            }else {
                vm.detail.travelExpenseDays = bonus.toString();
            }
            console.log(vm.detail.travelExpenseDays)
            count = (count * vm.detail.travelExpenseDays).toFixed(6)
            vm.detail.onRoadSubsidy = count.toString()
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTravelExpenseService.getModelById(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                    $("#endTime").datepicker('setDate', vm.item.endTime);
                    $("#applyRefundTime").datepicker('setDate', vm.item.applyRefundTime);

                }
            })
            fiveFinanceTravelExpenseService.listDetail(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicant,
                    multiple: true
                });
            }
            if (vm.status == 'bussineManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目负责人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.bussineManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'bussineManager') {
                vm.item.bussineManager = $scope.selectedOaUserLogins_;
                vm.item.bussineManagerName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }
        };

        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceTravelExpenseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
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

        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceTravelExpenseService.getNewModelDetail(travelExpenseId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceTravelExpenseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadData(true);
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTravelExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })

        };
        //金额小计=报销金额-在途补助
        vm.coutFinalPrice=function(){
            fiveFinanceTravelExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                }
            })
        };

        vm.print = function () {
            fiveFinanceTravelExpenseService.getPrintData(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));

                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }

        vm.showPlanTypeModal = function () {
            var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptName,
                budgetYear: year
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.costProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
           /* if(deduction.relevanceType=='loan'){
                $state.go("finance.loanDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundDetail", {refundId: deduction.relevanceId});
            }*/
            actService.getNgRedirectUrl(deduction.relevanseBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })

        }

        vm.showSelectPreOrReviewModal = function (projectId) {
            vm.projectId=projectId;
            if(vm.projectId=="projectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.projectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
            if(vm.projectId=="exceedProjectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.exceedProjectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
        };
        $rootScope.saveSelectLibrary_ = function () {
            if(vm.projectId=="projectId"){
                vm.item.projectId = $rootScope.selectedLibrary.id;
                vm.item.projectName = $rootScope.selectedLibrary.projectName;
                vm.item.projectType = $rootScope.selectedLibrary.projectNature;
                vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
                vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
                /*                vm.detail.applyRefundProject = $rootScope.selectedLibrary.projectName;
                                vm.detail.projectType = $rootScope.selectedLibrary.projectNature;*/
                $scope.closeLibrarySelectModal_();
            }
            if(vm.projectId=="exceedProjectId"){
                vm.item.exceedProjectId = $rootScope.selectedLibrary.id;
                vm.item.exceedProjectName = $rootScope.selectedLibrary.projectName;
                $scope.closeLibrarySelectModal_();
            }
        }

        vm.showBankSelect = function (id) {
            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {

            //var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                /*              deptId: vm.item.deptId,
                                budgetYear: year*/
                deptId: vm.item.deptId,
                budgetYear: vm.item.applyRefundTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetBalance=_item.remainMoney;
                vm.detail.budgetNo=_item.typeName;
                vm.detail.budgetId=_item.id; //预算子表id
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //差旅费报销-职能
    .controller("FiveFinanceTravelExpenseFunctionController", function ($state, $scope, fiveFinanceTravelExpenseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "差旅费",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.travelExpenseFunction";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTravelExpenseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.travelExpenseFunctionDetail", {travelExpenseId: id});
        }

        vm.add = function () {
            fiveFinanceTravelExpenseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTravelExpenseFunctionDetailController", function ($sce, $state, $stateParams, $rootScope, $scope,actService, fiveFinanceRefundService,fiveFinanceLoanService, hrEmployeeService, fiveFinanceTravelExpenseService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.travelExpenseFunction";
        var travelExpenseId = $stateParams.travelExpenseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTravelExpenseService.getModelById(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                    $("#endTime").datepicker('setDate', vm.item.endTime);
                    $("#applyRefundTime").datepicker('setDate', vm.item.applyRefundTime);

                }
            })
            fiveFinanceTravelExpenseService.listDetail(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicant,
                    multiple: true
                });
            }
            if (vm.status == 'bussineManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目负责人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.bussineManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'bussineManager') {
                vm.item.bussineManager = $scope.selectedOaUserLogins_;
                vm.item.bussineManagerName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }
        };

        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceTravelExpenseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
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

        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceTravelExpenseService.getNewModelDetail(travelExpenseId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceTravelExpenseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTravelExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        //金额小计=报销金额-在途补助
        vm.coutFinalPrice=function(){
            vm.detail.count=vm.detail.applyMoney+vm.detail.onRoadSubsidy;
        };

        vm.print = function () {
            fiveFinanceTravelExpenseService.getPrintData(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));

                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }

        vm.showPlanTypeModal = function () {
            var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptName,
                budgetYear: year
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.costProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
           /* if(deduction.relevanceType=='loan'){
                $state.go("finance.loanDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundDetail", {refundId: deduction.relevanceId});
            }*/
            actService.getNgRedirectUrl(deduction.relevanseBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })
        }

        vm.showSelectPreOrReviewModal = function (projectId) {
            vm.projectId=projectId;
            if(vm.projectId=="projectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.projectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
            if(vm.projectId=="exceedProjectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.exceedProjectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
        };
        $rootScope.saveSelectLibrary_ = function () {
            if(vm.projectId=="projectId"){
                vm.item.projectId = $rootScope.selectedLibrary.id;
                vm.item.projectName = $rootScope.selectedLibrary.projectName;
                vm.item.projectType = $rootScope.selectedLibrary.projectNature;
                vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
                vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
                /*                vm.detail.applyRefundProject = $rootScope.selectedLibrary.projectName;
                                vm.detail.projectType = $rootScope.selectedLibrary.projectNature;*/
                $scope.closeLibrarySelectModal_();
            }
            if(vm.projectId=="exceedProjectId"){
                vm.item.exceedProjectId = $rootScope.selectedLibrary.id;
                vm.item.exceedProjectName = $rootScope.selectedLibrary.projectName;
                $scope.closeLibrarySelectModal_();
            }
        }

        vm.showBankSelect = function (id) {
            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {

            //var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                /*              deptId: vm.item.deptId,
                                budgetYear: year*/
                deptId: vm.item.deptId,
                budgetYear: vm.item.applyRefundTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.applyRefundProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //差旅费报销-红河
    .controller("FiveFinanceTravelExpenseRedController", function ($state, $scope, fiveFinanceTravelExpenseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "差旅费",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.travelExpenseRed";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTravelExpenseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.travelExpenseRedDetail", {travelExpenseId: id});
        }

        vm.add = function () {
            fiveFinanceTravelExpenseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTravelExpenseRedDetailController", function ($sce, $state, $stateParams, $rootScope, $scope,actService, fiveFinanceRefundService,fiveFinanceLoanService, hrEmployeeService, fiveFinanceTravelExpenseService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.travelExpenseRed";
        var travelExpenseId = $stateParams.travelExpenseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTravelExpenseService.getModelById(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                    $("#endTime").datepicker('setDate', vm.item.endTime);
                    $("#applyRefundTime").datepicker('setDate', vm.item.applyRefundTime);

                }
            })
            fiveFinanceTravelExpenseService.listDetail(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicant,
                    multiple: true
                });
            }
            if (vm.status == 'bussineManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目负责人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.bussineManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'bussineManager') {
                vm.item.bussineManager = $scope.selectedOaUserLogins_;
                vm.item.bussineManagerName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }
        };

        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceTravelExpenseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
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

        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceTravelExpenseService.getNewModelDetail(travelExpenseId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceTravelExpenseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTravelExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        //金额小计=报销金额+在途补助
        vm.coutFinalPrice=function(){
            fiveFinanceTravelExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                }
            })
        };

        vm.print = function () {
            fiveFinanceTravelExpenseService.getPrintData(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));

                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }

        vm.showPlanTypeModal = function () {
            var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptName,
                budgetYear: year
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.costProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
            /*if(deduction.relevanceType=='loan'){
                $state.go("finance.loanDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundDetail", {refundId: deduction.relevanceId});
            }*/
            actService.getNgRedirectUrl(deduction.relevanseBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })
        }

        vm.showSelectPreOrReviewModal = function (projectId) {
            vm.projectId=projectId;
            if(vm.projectId=="projectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.projectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
            if(vm.projectId=="exceedProjectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.exceedProjectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
        };
        $rootScope.saveSelectLibrary_ = function () {
            if(vm.projectId=="projectId"){
                vm.item.projectId = $rootScope.selectedLibrary.id;
                vm.item.projectName = $rootScope.selectedLibrary.projectName;
                vm.item.projectType = $rootScope.selectedLibrary.projectNature;
                vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
                vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
                /*                vm.detail.applyRefundProject = $rootScope.selectedLibrary.projectName;
                                vm.detail.projectType = $rootScope.selectedLibrary.projectNature;*/
                $scope.closeLibrarySelectModal_();
            }
            if(vm.projectId=="exceedProjectId"){
                vm.item.exceedProjectId = $rootScope.selectedLibrary.id;
                vm.item.exceedProjectName = $rootScope.selectedLibrary.projectName;
                $scope.closeLibrarySelectModal_();
            }
        }

        vm.showBankSelect = function (id) {
            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {

            //var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                /*              deptId: vm.item.deptId,
                                budgetYear: year*/
                deptId: vm.item.deptId,
                budgetYear: vm.item.applyRefundTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.applyRefundProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //差旅费报销-建研院
    .controller("FiveFinanceTravelExpenseBuildController", function ($state, $scope, fiveFinanceTravelExpenseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "差旅费",
            qDeptName: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });
        vm.pageInfo = {
            q: vm.params.qName,
            pageNum: vm.params.pageNum,
            pageSize: vm.params.pageSize,
            total: vm.params.total
        };
        var uiSref = "finance.travelExpenseBuild";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveFinanceTravelExpenseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("finance.travelExpenseBuildDetail", {travelExpenseId: id});
        }

        vm.add = function () {
            fiveFinanceTravelExpenseService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveFinanceTravelExpenseBuildDetailController", function ($sce, $state, $stateParams, $rootScope, $scope,actService, fiveFinanceRefundService,fiveFinanceLoanService, hrEmployeeService, fiveFinanceTravelExpenseService, hrDeptService) {
        var vm = this;
        var uiSref = "finance.travelExpenseBuild";
        var travelExpenseId = $stateParams.travelExpenseId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveFinanceTravelExpenseService.getModelById(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                    $("#endTime").datepicker('setDate', vm.item.endTime);
                    $("#applyRefundTime").datepicker('setDate', vm.item.applyRefundTime);

                }
            })
            fiveFinanceTravelExpenseService.listDetail(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.deductions = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'applicant') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.applicant,
                    multiple: true
                });
            }
            if (vm.status == 'bussineManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目负责人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.bussineManager,
                    multiple: true
                });
            }
            if (vm.status == 'applicantName') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择支列人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.detail.applicant,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicant') {
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'bussineManager') {
                vm.item.bussineManager = $scope.selectedOaUserLogins_;
                vm.item.bussineManagerName = $scope.selectedOaUserNames_;
                /*                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                                    var user = value.data.data;
                                    vm.item.applicantNo = user.userLogin;
                                    vm.item.applicantTel=user.mobile;
                                })*/
            }
            if (vm.status == 'applicantName') {
                vm.detail.applicant = $scope.selectedOaUserLogins_;
                vm.detail.applicantName = $scope.selectedOaUserNames_;
            }
        };

        vm.getReceiptsNumber=function(){
            vm.item.operateUserLogin=user.userLogin;
            fiveFinanceTravelExpenseService.update(vm.item).then(function(value){
                if (value.data.ret) {
                    fiveFinanceTravelExpenseService.getReceiptsNumber(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("单据号已更新!");
                        }
                    })
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveFinanceTravelExpenseService.update(vm.item).then(function (value) {
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

        //选部门
        vm.showDeptModal = function (id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门", type: "选部门", deptIdList: vm.detail.deptId + "",
                    multiple: false, deptIds: "1", parentDeptId: 2
                });
            }

        }
        //保存选部门
        $rootScope.saveSelectDept_ = function () {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveFinanceTravelExpenseService.getNewModelDetail(travelExpenseId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveFinanceTravelExpenseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveFinanceTravelExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        //金额小计=报销金额+在途补助
        vm.coutFinalPrice=function(){
            fiveFinanceTravelExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                }
            })
        };

        vm.print = function () {
            fiveFinanceTravelExpenseService.getPrintData(travelExpenseId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        //选择借款流程
        vm.showLoanModel = function () {
            fiveFinanceLoanService.listLoanByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listLoans = value.data.data;
                    singleCheckBox(".cb_loan", "data-name");
                }
            })
            $("#selectLoanModal").modal("show");
        }
        vm.saveSelectLoanModel = function () {
            if ($(".cb_loan:checked").length > 0) {
                var loan = $.parseJSON($(".cb_loan:checked").first().attr("data-name"));
                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,loan.id,"loan").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectLoanModal").modal("hide");
        }
        //选择还款流程
        vm.showRefundModel = function () {
            fiveFinanceRefundService.listRefundByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.listRefunds = value.data.data;
                    singleCheckBox(".cb_refund", "data-name");
                }
            })
            $("#selectRefundModal").modal("show");
        }
        vm.saveSelectRefundModel = function () {
            if ($(".cb_refund:checked").length > 0) {
                var refund = $.parseJSON($(".cb_refund:checked").first().attr("data-name"));

                fiveFinanceTravelExpenseService.saveSelectedDeduction(travelExpenseId,refund.id,"refund").then(function (value) {
                    if (value.data.ret) {
                        //刷新抵扣
                        fiveFinanceTravelExpenseService.listDeduction(travelExpenseId).then(function (value) {
                            if (value.data.ret) {
                                vm.deductions = value.data.data;
                            }
                        })
                    }
                })
            }
            $("#selectRefundModal").modal("hide");
        }

        vm.showPlanTypeModal = function () {
            var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                deptId: vm.item.deptName,
                budgetYear: year
            });
        }

        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                vm.detail.budgetId= _item.id;
                vm.detail.controlBalance=_item.remainMoney;
                vm.detail.costProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.removeDeduction = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveFinanceTravelExpenseService.removeDeduction(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        vm.showRelevanceModel  = function (deduction){
         /*   if(deduction.relevanceType=='loan'){
                $state.go("finance.loanDetail", {loanId: deduction.relevanceId});
            }else if(deduction.relevanceType=='refund'){
                $state.go("finance.refundDetail", {refundId: deduction.relevanceId});
            }*/
            actService.getNgRedirectUrl(deduction.relevanseBusinessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })
        }

        vm.showSelectPreOrReviewModal = function (projectId) {
            vm.projectId=projectId;
            if(vm.projectId=="projectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.projectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
            if(vm.projectId=="exceedProjectId"){
                $scope.showLibrarySelectModal_({
                    selectLibraryId:vm.item.exceedProjectId,
                    uiSref:uiSref,
                    multiple: false
                });
            }
        };
        $rootScope.saveSelectLibrary_ = function () {
            if(vm.projectId=="projectId"){
                vm.item.projectId = $rootScope.selectedLibrary.id;
                vm.item.projectName = $rootScope.selectedLibrary.projectName;
                vm.item.projectType = $rootScope.selectedLibrary.projectNature;
                vm.item.businessManager= $rootScope.selectedLibrary.projectManager;
                vm.item.businessManagerName= $rootScope.selectedLibrary.businessManagerName;
                /*                vm.detail.applyRefundProject = $rootScope.selectedLibrary.projectName;
                                vm.detail.projectType = $rootScope.selectedLibrary.projectNature;*/
                $scope.closeLibrarySelectModal_();
            }
            if(vm.projectId=="exceedProjectId"){
                vm.item.exceedProjectId = $rootScope.selectedLibrary.id;
                vm.item.exceedProjectName = $rootScope.selectedLibrary.projectName;
                $scope.closeLibrarySelectModal_();
            }
        }

        vm.showBankSelect = function (id) {
            vm.id=id;
            if(id==1){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.outBankAccount
                });
            }
            if(id==0){
                $scope.showBankSelectModal_({
                    userLogin: "",
                    uiSref:uiSref,
                    userBankNo:vm.item.inBankAccount
                });
            }
        }

        $rootScope.saveSelectBank_ = function () {
            if(vm.id==1){
                var v1 = $rootScope.selectBank.type ;
                vm.item.payName = $rootScope.selectBank.userName ;
                vm.item.payAccount = $rootScope.selectBank.bankNo ;
                vm.item.payBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
            if(vm.id==0){
                var v2 = $rootScope.selectBank.type ;
                vm.item.receiveName = $rootScope.selectBank.userName ;
                vm.item.receiveAccount = $rootScope.selectBank.bankNo ;
                vm.item.receiveBank = $rootScope.selectBank.bankName ;
                $scope.closeBankSelectModal_();
            }
        }

        vm.showPlanTypeModal = function () {

            //var year = vm.item.applicantTime.substring(0,4);
            $scope.showBudgetSelectModal_({
                /*              deptId: vm.item.deptId,
                                budgetYear: year*/
                deptId: vm.item.deptId,
                budgetYear: vm.item.applyRefundTime.substring(0,4)
            });
        }
        $rootScope.saveSelectBudget_ = function () {
            var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                // vm.detail.budgetId= _item.id;
                //vm.detail.controlBalance=_item.remainMoney;
                vm.detail.applyRefundProject=_item.typeName;
            });
            $scope.closeBudgetSelectModal_();
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })