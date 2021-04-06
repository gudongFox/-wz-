angular.module('controllers.five.budget', [])
    //独立法人预算
    .controller("FiveBudgetIndependentController", function ($state, $scope, $rootScope, fiveBudgetIndependentService) {
        var vm = this;
        var uiSref = "budget.independent";
        var tableName = $rootScope.loadTableName(uiSref);
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0,uiSref: uiSref});
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
            fiveBudgetIndependentService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.independentDetail", {independentId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetIndependentService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetIndependentService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            fiveBudgetIndependentService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{1:'deptName',2:'budgetYear',3:'budgetTotalMoney',4:'remark',5:'creator',6:'gmtCreate',7:'processName'},handleColumn:8};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.init();

        return vm;
    })
    .controller("FiveBudgetIndependentDetailController", function ($state, $stateParams, $scope, $rootScope, hrDeptService,commonCodeService, fiveBudgetIndependentService)   {
        var vm = this;
        var uiSref = "budget.independent";
        var independentId = $stateParams.independentId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetIndependentService.getModelById(independentId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                       /* vm.getLastYearDetailById();*/
                    }
                }
            })
            fiveBudgetIndependentService.getDetailById(independentId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        showColumns: true,          // 是否显示内容列下拉框
                        showRefresh: true,
                        expandAll: true,
                        condensed: true,        //紧凑
                      /*  expanderExpandedClass: 'glyphicon glyphicon-circle-arrow-down',       // 展开的按钮的图标
                        expanderCollapsedClass: 'glyphicon glyphicon-circle-arrow-up',     // 缩起的按钮的图标*/
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },
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
                                field: 'lastYearMoney',
                                title: '上年预算',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'lastYearSuccess',
                                title: '上年预计完成',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetMoney',
                                title: '本年预算金额',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '本年预算占比',
                                width: '15%',
                                align: "left",
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'remark',
                                title: '备注',
                                width: '35%',
                                align: "left",
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })

        };
        vm.getLastYearDetailById = function () {
            fiveBudgetIndependentService.getLastYearDetailById(independentId,vm.item.budgetYear).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: null,    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            showColumns: true,          // 是否显示内容列下拉框
                            showRefresh: true,
                            expandAll: true,
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
                                    field: 'budgetMoney',
                                    title: '预算金额',
                                    width: '20%',
                                    align: "left",
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '15%',
                                    align: "left",
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'remark',
                                    title: '备注',
                                    width: '35%',
                                    align: "left",
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
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

       //同步预算信息
        vm.refreshBudgetDetail = function () {
            bootbox.confirm("同步数据 会清空已填写数据，是否确认同步？", function (result) {
                if (result) {
                    fiveBudgetIndependentService.refreshBudgetDetail(independentId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("同步数据成功!");
                            vm.loadData(true);
                        }
                    })
                }
            });

        };

        //新增
        vm.showDetailModel = function (id) {
            var detailId = 0;
            var selecteds = $('#treeTable').bootstrapTreeTable('getSelections');
            $.each(selecteds, function (_i, _item) {
                detailId = _item.id;
            });
            if (id === 0) {
                fiveBudgetIndependentService.getNewModelDetail(independentId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetIndependentService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetIndependentService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.independentId = independentId;
            vm.detail.creator = user.userLogin;
            fiveBudgetIndependentService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetIndependentService.getDetailById(independentId).then(function (value) {
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
            fiveBudgetIndependentService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetIndependentService.moneyTurnCapital(vm.item.money).then(function (value) {
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
                fiveBudgetIndependentService.update(vm.item).then(function (value) {
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

        vm.showDeptModal1=function(ids) {
            hrDeptService.selectAll().then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        state: {opened: item.parentId === 0, disabled: false, selected: false}
                    };
                    ids=","+ids+",";
                    if(ids.indexOf(","+item.id+",")>=0){
                        node.state.selected=true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree').jstree("destroy");
                $('#dept_select_tree')
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        },
                        "checkbox" : {
                            "keep_selected_style" : false
                        },
                        "plugins" : [ "wholerow", "checkbox" ]
                    });
            });
            $("#deptSelectModal").modal("show");
        }

        vm.saveSelectDept1=function(){
            var deptList= $('#dept_select_tree').jstree(true).get_selected();
            vm.detail.publicDeptIds=","+deptList.join(',')+",";
            $("#deptSelectModal").modal("hide");
        }

        vm.init();

        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //生产、辅助部门预算
    .controller("FiveBudgetProductionController", function ($state, $scope, $rootScope, fiveBudgetProductionService) {
        var vm = this;
        var uiSref = "budget.production";
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
            fiveBudgetProductionService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.productionDetail", {productionId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetProductionService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetProductionService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetProductionDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetProductionService) {
        var vm = this;
        var uiSref = "budget.production";
        var productionId = $stateParams.productionId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetProductionService.getModelById(productionId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        vm.getLastYearDetailById();
                    }
                }
            })

            fiveBudgetProductionService.getLastYearDetailById(productionId).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
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
                                    field: 'budgetMoney',
                                    title: '预算金额',
                                    width: '20%',
                                    align: "left",
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '15%',
                                    align: "left",
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'remark',
                                    title: '备注',
                                    width: '35%',
                                    align: "left",
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })

            fiveBudgetProductionService.getDetailById(productionId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },
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
                                field: 'budgetMoney',
                                title: '预算金额',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '预算占比',
                                width: '15%',
                                align: "left",
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                              field: 'remark',
                                title: '备注',
                                width: '35%',
                                align: "left",
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })

        };
        vm.getLastYearDetailById = function () {
            fiveBudgetProductionService.getLastYearDetailById(productionId,vm.item.budgetYear).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
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
                                    field: 'budgetMoney',
                                    title: '预算金额',
                                    width: '20%',
                                    align: "left",
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '15%',
                                    align: "left",
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'remark',
                                    title: '备注',
                                    width: '35%',
                                    align: "left",
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
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
                fiveBudgetProductionService.getNewModelDetail(productionId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetProductionService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetProductionService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.productionId = productionId;
            vm.detail.creator = user.userLogin;
            fiveBudgetProductionService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetProductionService.getDetailById(productionId).then(function (value) {
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
            fiveBudgetProductionService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetProductionService.moneyTurnCapital(vm.item.money).then(function (value) {
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
                fiveBudgetProductionService.update(vm.item).then(function (value) {
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


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }


        return vm;

    })
    //职能管理部门
    .controller("FiveBudgetFunctionController", function ($state, $scope, $rootScope, fiveBudgetFunctionService) {
        var vm = this;
        var uiSref = "budget.function";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetFunctionService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.functionDetail", {functionId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetFunctionService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetFunctionService.remove(id, user.userLogin).then(function (value) {
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
            fiveBudgetFunctionService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{1:'deptName',2:'budgetYear',3:'budgetTotalMoney',4:'remark',5:'creator',6:'gmtCreate',7:'processName'},handleColumn:8};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;
    })
    .controller("FiveBudgetFunctionDetailController", function ($state, $stateParams, $scope, $rootScope, hrDeptService,commonCodeService,fiveBudgetIndependentService, fiveBudgetFunctionService ) {
        var vm = this;
        var uiSref = "budget.function";
        var functionId = $stateParams.functionId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetFunctionService.getModelById(functionId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        //vm.getLastYearDetailById();
                    }
                }
            })
            fiveBudgetFunctionService.getDetailById(functionId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },
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
                                field: 'lastYearMoney',
                                title: '上年预算',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'lastYearSuccess',
                                title: '上年预计完成',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetMoney',
                                title: '预算金额',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '本年预算占比',
                                width: '15%',
                                align: "left",
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                              field: 'remark',
                                title: '备注',
                                width: '35%',
                                align: "left",
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })

        };
        vm.getLastYearDetailById = function () {
            fiveBudgetFunctionService.getLastYearDetailById(functionId,vm.item.budgetYear).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,

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
                                    field: 'budgetMoney',
                                    title: '预算金额',
                                    width: '20%',
                                    align: "left",
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '15%',
                                    align: "left",
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'remark',
                                    title: '备注',
                                    width: '35%',
                                    align: "left",
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })
        };
        //新增detail
        $("#addBtn").click(function () {
            var data = [];
            $('#treeTable').bootstrapTreeTable('appendData', data);
        })

        //同步预算信息
        vm.refreshBudgetDetail = function () {
            bootbox.confirm("同步数据 会清空已填写数据，是否确认同步？", function (result) {
                if (result) {
                    fiveBudgetIndependentService.refreshBudgetDetail(functionId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("同步数据成功!");
                            vm.loadData(true);
                        }
                    })
                }
            });

        };

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
                fiveBudgetFunctionService.getNewModelDetail(functionId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetFunctionService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetFunctionService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = functionId;
            vm.detail.creator = user.userLogin;
            fiveBudgetFunctionService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetFunctionService.getDetailById(functionId).then(function (value) {
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
            fiveBudgetFunctionService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetFunctionService.moneyTurnCapital(vm.item.money).then(function (value) {
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
            /*if ($("#detail_form").validate().form()) {*/
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetFunctionService.update(vm.item).then(function (value) {
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
           /* } else {
                toastr.warning("请准确填写数据!")
                return false;
            }*/

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

        vm.showDeptModal1=function(ids) {
            hrDeptService.selectAll().then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        state: {opened: item.parentId === 0, disabled: false, selected: false}
                    };
                    ids=","+ids+",";
                    if(ids.indexOf(","+item.id+",")>=0){
                        node.state.selected=true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree').jstree("destroy");
                $('#dept_select_tree')
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        },
                        "checkbox" : {
                            "keep_selected_style" : false
                        },
                        "plugins" : [ "wholerow", "checkbox" ]
                    });
            });
            $("#deptSelectModal").modal("show");
        }

        vm.saveSelectDept1=function(){
            var deptList= $('#dept_select_tree').jstree(true).get_selected();
            vm.detail.publicDeptIds=","+deptList.join(',')+",";
            $("#deptSelectModal").modal("hide");
        }


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //职务消费
    .controller("FiveBudgetPostExpenseController", function ($state, $scope, $rootScope, fiveBudgetPostExpenseService) {
        var vm = this;
        var uiSref = "budget.postExpense";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetPostExpenseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.postExpenseDetail", {postExpenseId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetPostExpenseService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetPostExpenseService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetPostExpenseDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetPostExpenseService ) {
        var vm = this;
        var uiSref = "budget.postExpense";
        var postExpenseId = $stateParams.postExpenseId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetPostExpenseService.getModelById(postExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            fiveBudgetPostExpenseService.getDetailById(postExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },
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
                                field: 'lastYearMoney',
                                title: '上年预算',
                                width: '12%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'lastYearSuccess',
                                title: '上年预计完成',
                                width: '12%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetMoney',
                                title: '预算金额',
                                width: '20%',
                                align: "left",
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '预算占比',
                                width: '15%',
                                align: "left",
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                              field: 'remark',
                                title: '备注',
                                width: '35%',
                                align: "left",
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })
           /* fiveBudgetPostExpenseService.getLastYearDetailById(postExpenseId).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
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
                                    field: 'budgetMoney',
                                    title: '预算金额',
                                    width: '20%',
                                    align: "left",
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '15%',
                                    align: "left",
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'remark',
                                    title: '备注',
                                    width: '35%',
                                    align: "left",
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })*/

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
                fiveBudgetPostExpenseService.getNewModelDetail(postExpenseId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetPostExpenseService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetPostExpenseService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = postExpenseId;
            vm.detail.creator = user.userLogin;
            fiveBudgetPostExpenseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetPostExpenseService.getDetailById(postExpenseId).then(function (value) {
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
            fiveBudgetPostExpenseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetPostExpenseService.moneyTurnCapital(vm.item.money).then(function (value) {
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
                fiveBudgetPostExpenseService.update(vm.item).then(function (value) {
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

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //专项维修立项预算
    .controller("FiveBudgetMaintainController", function ($state, $scope,$rootScope, fiveBudgetMaintainService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "收费预算",
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
        var uiSref = "budget.maintain";
        var tableName = $rootScope.loadTableName(uiSref);

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
            fiveBudgetMaintainService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("budget.maintainDetail", {maintainId: id});
        }

        vm.add = function () {
            fiveBudgetMaintainService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBudgetMaintainService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetMaintainDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveBudgetMaintainService, hrDeptService) {
        var vm = this;
        var uiSref = "budget.maintain";
        var maintainId = $stateParams.maintainId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveBudgetMaintainService.getModelById(maintainId).then(function (value) {
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
            fiveBudgetMaintainService.listDetail(maintainId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
            /*fiveBudgetMaintainService.listLastYearDetail(maintainId).then(function (value) {
                if (value.data.ret) {
                    vm.lastDetails = value.data.data;
                }
            })*/
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBudgetMaintainService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        };

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

        };

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
                fiveBudgetMaintainService.update(vm.item).then(function (value) {
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

        };

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        };

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveBudgetMaintainService.getNewModelDetail(maintainId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveBudgetMaintainService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        };
        //只读查看
        vm.showDetailModel1 = function (id) {
            fiveBudgetMaintainService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal1").modal("show");
                    }
                })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveBudgetMaintainService.removeDetail(id, user.userLogin).then(function (value) {
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
            fiveBudgetMaintainService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    vm.save();
                    $("#detailModal").modal("hide");
                }
            })
        };

        vm.print = function () {
            fiveBudgetMaintainService.getPrintData(maintainId).then(function (value) {
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
        };
        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        };
        return vm;
    })
    //专项业务支出预算
    .controller("FiveBudgetBusinessController", function ($state, $scope,$rootScope, fiveBudgetBusinessService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "收费预算",
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
        var uiSref = "budget.business";

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
            fiveBudgetBusinessService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("budget.businessDetail", {businessId: id});
        }

        vm.add = function () {
            fiveBudgetBusinessService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBudgetBusinessService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetBusinessDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveBudgetBusinessService, hrDeptService) {
        var vm = this;
        var uiSref = "budget.business";
        var businessId = $stateParams.businessId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveBudgetBusinessService.getModelById(businessId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                    $("#endTime").datepicker('setDate', vm.item.endTime);

                }
            })
            fiveBudgetBusinessService.listDetail(businessId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
          /*  fiveBudgetBusinessService.listLastYearDetail(businessId).then(function (value) {
                if (value.data.ret) {
                    vm.lastDetails = value.data.data;
                }
            })*/
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBudgetBusinessService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

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

        };

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
                fiveBudgetBusinessService.update(vm.item).then(function (value) {
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

        };

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        };

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveBudgetBusinessService.getNewModelDetail(businessId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveBudgetBusinessService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        };
        //只读查看
        vm.showDetailModel1 = function (id) {
            fiveBudgetBusinessService.getModelDetailById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                    $("#detailModal1").modal("show");
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveBudgetBusinessService.removeDetail(id, user.userLogin).then(function (value) {
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
            fiveBudgetBusinessService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    vm.save();
                    $("#detailModal").modal("hide");
                }
            })
        };

        vm.print = function () {
            fiveBudgetBusinessService.getPrintData(businessId).then(function (value) {
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
        };
        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        };
        return vm;
    })
    //科研经费支出预算
    .controller("FiveBudgetScientificFundsOutController", function ($state, $scope, $rootScope, fiveBudgetScientificFundsOutService) {
        var vm = this;
        var uiSref = "budget.scientificFundsOut";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetScientificFundsOutService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.scientificFundsOutDetail", {scientificFundsOutId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetScientificFundsOutService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetScientificFundsOutService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetScientificFundsOutDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetScientificFundsOutService ) {
        var vm = this;
        var uiSref = "budget.scientificFundsOut";
        var scientificFundsOutId = $stateParams.scientificFundsOutId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetScientificFundsOutService.getModelById(scientificFundsOutId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            fiveBudgetScientificFundsOutService.getDetailById(scientificFundsOutId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                       // height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },
                        columns: [{
                            field: 'selectItem',
                            radio: true
                        }, {
                            title: '类型名称',
                            field: 'typeName',
                            width: '10%',
                            formatter: function (value, row, index) {
                                if(row.lv==1){
                                    return '<span style="color:red">'+value+'</span>';
                                }
                                return value;
                            }
                        },
                            {
                                field: 'budgetMoney',
                                title: '预算总金额',
                                width: '8%',
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '预算占比',
                                width: '5%',

                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'designMoney',
                                title: '设计费',
                                width: '5%',

                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'materialsMoney',
                                title: '材料费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'outAssistMoney',
                                title: '外协费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'testMoney',
                                title: '试验费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'dedicatedMoney',
                                title: '专用费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'assetMoney',
                                title: '固定资产使用费',
                                width: '8%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'payrollMoney',
                                title: '职工薪酬',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'mangerMoney',
                                title: '管理费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })
           /* fiveBudgetScientificFundsOutService.getLastYearDetailById(scientificFundsOutId).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
                            columns: [{
                                field: 'selectItem',
                                radio: true
                            }, {
                                title: '类型名称',
                                field: 'typeName',
                                width: '10%',
                                formatter: function (value, row, index) {
                                    if(row.lv==1){
                                        return '<span style="color:red">'+value+'</span>';
                                    }
                                    return value;
                                }
                            },
                                {
                                    field: 'budgetMoney',
                                    title: '预算总金额',
                                    width: '8%',
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '5%',

                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'designMoney',
                                    title: '设计费',
                                    width: '5%',

                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'materialsMoney',
                                    title: '材料费',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'outAssistMoney',
                                    title: '外协费',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'testMoney',
                                    title: '试验费',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'dedicatedMoney',
                                    title: '专用费',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'assetMoney',
                                    title: '固定资产使用费',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'payrollMoney',
                                    title: '职工薪酬',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'mangerMoney',
                                    title: '管理费',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })*/
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
                fiveBudgetScientificFundsOutService.getNewModelDetail(scientificFundsOutId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetScientificFundsOutService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetScientificFundsOutService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = scientificFundsOutId;
            vm.detail.creator = user.userLogin;
            fiveBudgetScientificFundsOutService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetScientificFundsOutService.getDetailById(scientificFundsOutId).then(function (value) {
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
            fiveBudgetScientificFundsOutService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetScientificFundsOutService.moneyTurnCapital(vm.item.money).then(function (value) {
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
                fiveBudgetScientificFundsOutService.update(vm.item).then(function (value) {
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

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //科研经费收入预算
    .controller("FiveBudgetScientificFundsInController", function ($state, $scope, $rootScope, fiveBudgetScientificFundsInService) {
        var vm = this;
        var uiSref = "budget.scientificFundsIn";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetScientificFundsInService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.scientificFundsInDetail", {scientificFundsInId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetScientificFundsInService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetScientificFundsInService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetScientificFundsInDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetScientificFundsInService ) {
        var vm = this;
        var uiSref = "budget.scientificFundsIn";
        var scientificFundsInId = $stateParams.scientificFundsInId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetScientificFundsInService.getModelById(scientificFundsInId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            fiveBudgetScientificFundsInService.getDetailById(scientificFundsInId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        //height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },
                        columns: [{
                            field: 'selectItem',
                            radio: true
                        }, {
                            title: '项目名称',
                            field: 'typeName',
                            width: '10%',
                            formatter: function (value, row, index) {
                                if(row.lv==1){
                                    return '<span style="color:red">'+value+'</span>';
                                }
                                return value;
                            }
                            },
                            {
                                field: 'projectYearMoney',
                                title: '本年预算总金额',
                                width: '8%',
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '预算占比',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'allowNo',
                                title: '批准文号',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'yearCountryMoney',
                                title: '本年国拨资金',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'yearSelfMoney',
                                title: '本年自筹资金',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'yearOtherMoney',
                                title: '本年其他资金',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'projectAddInMoney',
                                title: '项目累计收入',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'projectAddOutMoney',
                                title: '项目累计支出',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                           /* {
                                field: 'totalTarget',
                                title: '研制(研发)总目标',
                                width: '8%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },*/
                        ],
                        data: value.data.data,
                    });
                }
            })
          /*  fiveBudgetScientificFundsInService.getLastYearDetailById(scientificFundsInId).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
                            columns: [{
                                field: 'selectItem',
                                radio: true
                            }, {
                                title: '项目名称',
                                field: 'typeName',
                                width: '10%',
                                formatter: function (value, row, index) {
                                    if(row.lv==1){
                                        return '<span style="color:red">'+value+'</span>';
                                    }
                                    return value;
                                }
                            },
                                {
                                    field: 'projectYearMoney',
                                    title: '本年预算总金额',
                                    width: '8%',
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'allowNo',
                                    title: '批准文号',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'yearCountryMoney',
                                    title: '本年国拨资金',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'yearSelfMoney',
                                    title: '本年自筹资金',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'yearOtherMoney',
                                    title: '本年其他资金',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'projectAddInMoney',
                                    title: '项目累计收入',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'projectAddOutMoney',
                                    title: '项目累计支出',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'totalTarget',
                                    title: '研制(研发)总目标',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })*/
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
                fiveBudgetScientificFundsInService.getNewModelDetail(scientificFundsInId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetScientificFundsInService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetScientificFundsInService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = scientificFundsInId;
            vm.detail.creator = user.userLogin;
            fiveBudgetScientificFundsInService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetScientificFundsInService.getDetailById(scientificFundsInId).then(function (value) {
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
            fiveBudgetScientificFundsInService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetScientificFundsInService.moneyTurnCapital(vm.item.money).then(function (value) {
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
                fiveBudgetScientificFundsInService.update(vm.item).then(function (value) {
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

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //公用经费预算
    .controller("FiveBudgetPublicFundsController", function ($state, $scope, $rootScope, fiveBudgetPublicFundsService) {
        var vm = this;
        var uiSref = "budget.publicFunds";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetPublicFundsService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.publicFundsDetail", {publicFundsId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetPublicFundsService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetPublicFundsService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetPublicFundsDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetPublicFundsService ) {
        var vm = this;
        var uiSref = "budget.publicFunds";
        var publicFundsId = $stateParams.publicFundsId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetPublicFundsService.getModelById(publicFundsId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            fiveBudgetPublicFundsService.getDetailById(publicFundsId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },

                        columns: [{
                            field: 'selectItem',
                            radio: true
                        }, {
                            title: '部门名称',
                            field: 'typeName',
                            width: '12%',
                            formatter: function (value, row, index) {
                                if(row.lv==1){
                                    return '<span style="color:red">'+value+'</span>';
                                }
                                return value;
                            }
                        },
                            {
                                field: 'budgetMoney',
                                title: '预算总金额',
                                width: '6%',
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '预算占比',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'waterMoney',
                                title: '水费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'electricMoney',
                                title: '电费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'heatingMoneySelf',
                                title: '供暖费（个人）',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'heatingMoneyPublic',
                                title: '供暖费（公共）',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'heatingMoneyTotal',
                                title: '供暖费（总计）',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'houseMoney',
                                title: '住房补贴',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'healthMoney',
                                title: '体检费',
                                width: '8%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })
           /* fiveBudgetPublicFundsService.getLastYearDetailById(publicFundsId).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
                            columns: [{
                                field: 'selectItem',
                                radio: true
                            }, {
                                title: '部门名称',
                                field: 'typeName',
                                width: '10%',
                                formatter: function (value, row, index) {
                                    if(row.lv==1){
                                        return '<span style="color:red">'+value+'</span>';
                                    }
                                    return value;
                                }
                            },
                                {
                                    field: 'budgetMoney',
                                    title: '预算总金额',
                                    width: '8%',
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'waterMoney',
                                    title: '水费',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'electricMoney',
                                    title: '电费',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'heatingMoneySelf',
                                    title: '供暖费（个人）',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'heatingMoneyPublic',
                                    title: '供暖费（公共）',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'heatingMoneyTotal',
                                    title: '供暖费（总计）',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'houseMoney',
                                    title: '住房补贴',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'healthMoney',
                                    title: '体检费',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })*/
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
                fiveBudgetPublicFundsService.getNewModelDetail(publicFundsId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetPublicFundsService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetPublicFundsService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = publicFundsId;
            vm.detail.creator = user.userLogin;
            fiveBudgetPublicFundsService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetPublicFundsService.getDetailById(publicFundsId).then(function (value) {
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
            fiveBudgetPublicFundsService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetPublicFundsService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetPublicFundsService.update(vm.item).then(function (value) {
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
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.detail.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.typeName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //资本性支出预算
    .controller("FiveBudgetCapitalOutController", function ($state, $scope, $rootScope, fiveBudgetCapitalOutService) {
        var vm = this;
        var uiSref = "budget.capitalOut";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetCapitalOutService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.capitalOutDetail", {capitalOutId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetCapitalOutService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetCapitalOutService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetCapitalOutDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetCapitalOutService ) {
        var vm = this;
        var uiSref = "budget.capitalOut";
        var capitalOutId = $stateParams.capitalOutId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetCapitalOutService.getModelById(capitalOutId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        fiveBudgetCapitalOutService.getDetailById(capitalOutId).then(function (value) {
                            if (value.data.ret) {
                                vm.details = value.data.data;
                                $('#treeTable').bootstrapTreeTable('destroy')
                                var treeTable = $('#treeTable').bootstrapTreeTable({
                                    toolbar: "#demo-toolbar",    //顶部工具条
                                    expandColumn: 1,            // 在哪一列上面显示展开按钮
                                    showExport: true,
                                    height: 500,
                                    search:true,
                                    expandAll: true,
                                    condensed: true,        //紧凑
                                    onDblClickRow:function(){
                                        vm.showDetailModel(1);
                                    },
                                    columns: [{
                                        field: 'selectItem',
                                        radio: true
                                    }, {
                                        title: '部门名称',
                                        field: 'typeName',
                                        width: '10%',
                                        formatter: function (value, row, index) {
                                            if(row.lv==1){
                                                return '<span style="color:red">'+value+'</span>';
                                            }
                                            return value;
                                        }
                                    },
                                        {
                                            field: 'budgetMoney',
                                            title: '预算总金额',
                                            width: '8%',
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetProportion',
                                            title: '预算占比',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'oaMoney',
                                            title: '办公自动化',
                                            width: '5%',
                                            visible:vm.item.deptId==11,
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'furnitureMoney',
                                            title: '办公家具',
                                            width: '5%',
                                            visible:vm.item.deptId==67,
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'carMoney',
                                            title: '车辆预算',
                                            width: '5%',
                                            visible:vm.item.deptId==59,
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'softMoney',
                                            title: '软件预算',
                                            width: '5%',
                                            visible:vm.item.deptId==11,
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },

                                    ],
                                    data: value.data.data,
                                });
                            }
                        })
                 /*       fiveBudgetCapitalOutService.getLastYearDetailById(capitalOutId).then(function (value) {
                            if (value.data.ret) {
                                vm.lastYeardetails = value.data.data;
                                setTimeout(function () {
                                    $('#lastTreeTable').bootstrapTreeTable('destroy');
                                    var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                                        toolbar: "#demo-toolbar",    //顶部工具条
                                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                                        showExport: true,
                                        height: 0,
                                        search:true,
                                        expandAll: true,
                                        onDblClickRow:function(){
                                            vm.showDetailModel(1);
                                        },
                                        columns: [{
                                            field: 'selectItem',
                                            radio: true
                                        }, {
                                            title: '部门名称',
                                            field: 'typeName',
                                            width: '10%',
                                            formatter: function (value, row, index) {
                                                if(row.lv==1){
                                                    return '<span style="color:red">'+value+'</span>';
                                                }
                                                return value;
                                            }
                                        },
                                            {
                                                field: 'budgetMoney',
                                                title: '预算总金额',
                                                width: '8%',
                                                valign: "top",
                                                formatter: function (value, item, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'budgetProportion',
                                                title: '预算占比',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'oaMoney',
                                                title: '办公自动化',
                                                width: '5%',
                                                visible:vm.item.deptId==11,
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'furnitureMoney',
                                                title: '办公家具',
                                                width: '5%',
                                                visible:vm.item.deptId==67,
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'carMoney',
                                                title: '车辆预算',
                                                width: '5%',
                                                visible:vm.item.deptId==59,
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'softMoney',
                                                title: '软件预算',
                                                width: '5%',
                                                visible:vm.item.deptId==11,
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'stockMoney',
                                                title: '股权投资',
                                                width: '5%',
                                                visible:vm.item.deptId==48,
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                        ],
                                        data: value.data.data,
                                    });
                                },500);
                            }
                        })*/
                    }
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
                fiveBudgetCapitalOutService.getNewModelDetail(capitalOutId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetCapitalOutService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetCapitalOutService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = capitalOutId;
            vm.detail.creator = user.userLogin;
            fiveBudgetCapitalOutService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetCapitalOutService.getDetailById(capitalOutId).then(function (value) {
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
            fiveBudgetCapitalOutService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetCapitalOutService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetCapitalOutService.update(vm.item).then(function (value) {
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
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.detail.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.typeName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //上缴房租预算
    .controller("FiveBudgetTurnInRentController", function ($state, $scope, $rootScope, fiveBudgetTurnInRentService) {
        var vm = this;
        var uiSref = "budget.turnInRent";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetTurnInRentService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.turnInRentDetail", {turnInRentId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetTurnInRentService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetTurnInRentService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetTurnInRentDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetTurnInRentService ) {
        var vm = this;
        var uiSref = "budget.turnInRent";
        var turnInRentId = $stateParams.turnInRentId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetTurnInRentService.getModelById(turnInRentId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        fiveBudgetTurnInRentService.getDetailById(turnInRentId).then(function (value) {
                            if (value.data.ret) {
                                vm.details = value.data.data;
                                $('#treeTable').bootstrapTreeTable('destroy')
                                var treeTable = $('#treeTable').bootstrapTreeTable({
                                    toolbar: "#demo-toolbar",    //顶部工具条
                                    expandColumn: 1,            // 在哪一列上面显示展开按钮
                                    showExport: true,
                                    height: 500,
                                    search:true,
                                    expandAll: true,
                                    condensed: true,        //紧凑
                                    onDblClickRow:function(){
                                        vm.showDetailModel(1);
                                    },
                                    columns: [{
                                        field: 'selectItem',
                                        radio: true
                                    }, {
                                        title: '部门名称',
                                        field: 'typeName',
                                        width: '10%',
                                        formatter: function (value, row, index) {
                                            if(row.lv==1){
                                                return '<span style="color:red">'+value+'</span>';
                                            }
                                            return value;
                                        }
                                    },
                                        {
                                            field: 'lastYearMoney',
                                            title: '上年预算',
                                            width: '5%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'lastYearSuccess',
                                            title: '上年预计完成',
                                            width: '5%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetMoney',
                                            title: '预算总金额',
                                            width: '8%',
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetProportion',
                                            title: '预算占比',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'rentMoney',
                                            title: '上缴房租预算',
                                            width: '5%',
                                            visible:vm.item.deptId==11,
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'remark',
                                            title: '备注',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                    ],
                                    data: value.data.data,
                                });
                            }
                        })
                       /* fiveBudgetTurnInRentService.getLastYearDetailById(turnInRentId).then(function (value) {
                            if (value.data.ret) {
                                vm.lastYeardetails = value.data.data;
                                setTimeout(function () {
                                    $('#lastTreeTable').bootstrapTreeTable('destroy');
                                    var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                                        toolbar: "#demo-toolbar",    //顶部工具条
                                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                                        showExport: true,
                                        height: 0,
                                        search:true,
                                        expandAll: true,
                                        onDblClickRow:function(){
                                            vm.showDetailModel(1);
                                        },
                                        columns: [{
                                            field: 'selectItem',
                                            radio: true
                                        }, {
                                            title: '部门名称',
                                            field: 'typeName',
                                            width: '10%',
                                            formatter: function (value, row, index) {
                                                if(row.lv==1){
                                                    return '<span style="color:red">'+value+'</span>';
                                                }
                                                return value;
                                            }
                                        },
                                            {
                                                field: 'budgetMoney',
                                                title: '预算总金额',
                                                width: '8%',
                                                valign: "top",
                                                formatter: function (value, item, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'budgetProportion',
                                                title: '预算占比',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'rentMoney',
                                                title: '上缴房租预算',
                                                width: '5%',
                                                visible:vm.item.deptId==11,
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'remark',
                                                title: '备注',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },

                                        ],
                                        data: value.data.data,
                                    });
                                },500);
                            }
                        })*/
                    }
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
                fiveBudgetTurnInRentService.getNewModelDetail(turnInRentId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetTurnInRentService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetTurnInRentService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = turnInRentId;
            vm.detail.creator = user.userLogin;
            fiveBudgetTurnInRentService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetTurnInRentService.getDetailById(turnInRentId).then(function (value) {
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
            fiveBudgetTurnInRentService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetTurnInRentService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetTurnInRentService.update(vm.item).then(function (value) {
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
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.detail.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.typeName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //人工成本预算
    .controller("FiveBudgetLaborCostController", function ($state, $scope, $rootScope, fiveBudgetLaborCostService) {
        var vm = this;
        var uiSref = "budget.laborCost";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetLaborCostService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.laborCostDetail", {laborCostId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetLaborCostService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetLaborCostService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetLaborCostDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetLaborCostService ) {
        var vm = this;
        var uiSref = "budget.laborCost";
        var laborCostId = $stateParams.laborCostId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetLaborCostService.getModelById(laborCostId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            fiveBudgetLaborCostService.getDetailById(laborCostId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },

                        columns: [{
                            field: 'selectItem',
                            radio: true
                        }, {
                            title: '部门名称',
                            field: 'typeName',
                            width: '15%',
                            formatter: function (value, row, index) {
                                if(row.lv==1){
                                    return '<span style="color:red">'+value+'</span>';
                                }
                                return value;
                            }
                        },
                            {
                                field: 'budgetMoney',
                                title: '预算总金额',
                                width: '8%',
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '预算占比',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'salaryMoney',
                                title: '工资及聘金',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'bonusMoney',
                                title: '奖金',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'coolingMoney',
                                title: '防暑降温',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'carMoney',
                                title: '车补',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'eatingMoney',
                                title: '餐补',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'healthMoney',
                                title: '体检费',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'houseMoney',
                                title: '住房补贴',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'societyMoney',
                                title: '社会保险',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'fundsMoney',
                                title: '住房公积金',
                                width: '5%',
                                visible:false,
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'laborUnionMoney',
                                title: '工会经费',
                                width: '5%',
                                visible:false,
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'educationMoney',
                                title: '教育经费',
                                visible:false,
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'labourServiceMoney',
                                title: '劳务派遣',
                                visible:false,
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'otherMoney',
                                title: '其他费用',
                                visible:false,
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })
           /* fiveBudgetLaborCostService.getLastYearDetailById(laborCostId).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
                            columns: [{
                                field: 'selectItem',
                                radio: true
                            }, {
                                title: '部门名称',
                                field: 'typeName',
                                width: '10%',
                                formatter: function (value, row, index) {
                                    if(row.lv==1){
                                        return '<span style="color:red">'+value+'</span>';
                                    }
                                    return value;
                                }
                            },
                                {
                                    field: 'budgetMoney',
                                    title: '预算总金额',
                                    width: '8%',
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'salaryMoney',
                                    title: '工资及聘金',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'bonusMoney',
                                    title: '奖金',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'coolingMoney',
                                    title: '福利费（防暑降温）',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'carMoney',
                                    title: '福利费（车补）',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'eatingMoney',
                                    title: '福利费（餐补）',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'healthMoney',
                                    title: '福利费（体检费）',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'houseMoney',
                                    title: '住房补贴',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'societyMoney',
                                    title: '社会保险',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'fundsMoney',
                                    title: '住房公积金',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'laborUnionMoney',
                                    title: '工会经费',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'educationMoney',
                                    title: '教育经费',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'labourServiceMoney',
                                    title: '劳务派遣',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'otherMoney',
                                    title: '其他费用',
                                    width: '8%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })*/
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
                fiveBudgetLaborCostService.getNewModelDetail(laborCostId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetLaborCostService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetLaborCostService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = laborCostId;
            vm.detail.creator = user.userLogin;
            fiveBudgetLaborCostService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetLaborCostService.getDetailById(laborCostId).then(function (value) {
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
            fiveBudgetLaborCostService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetLaborCostService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetLaborCostService.update(vm.item).then(function (value) {
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
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.detail.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.typeName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //职工人数预算
    .controller("FiveBudgetStaffNumberController", function ($state, $scope, $rootScope, fiveBudgetStaffNumberService) {
        var vm = this;
        var uiSref = "budget.staffNumber";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetStaffNumberService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.staffNumberDetail", {staffNumberId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetStaffNumberService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetStaffNumberService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetStaffNumberDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetStaffNumberService ) {
        var vm = this;
        var uiSref = "budget.staffNumber";
        var staffNumberId = $stateParams.staffNumberId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetStaffNumberService.getModelById(staffNumberId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            fiveBudgetStaffNumberService.getDetailById(staffNumberId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                    $('#treeTable').bootstrapTreeTable('destroy');
                    var treeTable = $('#treeTable').bootstrapTreeTable({
                        toolbar: "#demo-toolbar",    //顶部工具条
                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                        showExport: true,
                        height: 500,
                        search:true,
                        expandAll: true,
                        condensed: true,        //紧凑
                        onDblClickRow:function(){
                            vm.showDetailModel(1);
                        },

                        columns: [{
                            field: 'selectItem',
                            radio: true
                        }, {
                            title: '部门名称',
                            field: 'typeName',
                            width: '12%',
                            formatter: function (value, row, index) {
                                if(row.lv==1){
                                    return '<span style="color:red">'+value+'</span>';
                                }
                                return value;
                            }
                        },
                            {
                                field: 'budgetMoney',
                                title: '本年预算总人数',
                                width: '6%',
                                valign: "top",
                                formatter: function (value, item, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'budgetProportion',
                                title: '本年预算占比',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'lastYearEmployee',
                                title: '上年从业人数',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'employeeNumber',
                                title: '本年从业人数',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'lastYearStaff',
                                title: '上年职工人数',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                            {
                                field: 'staffNumber',
                                title: '本年职工人数',
                                width: '5%',
                                formatter: function (value, row, index) {
                                    return value;
                                }
                            },
                        ],
                        data: value.data.data,
                    });
                }
            })
          /*  fiveBudgetStaffNumberService.getLastYearDetailById(staffNumberId).then(function (value) {
                if (value.data.ret) {
                    vm.lastYeardetails = value.data.data;
                    setTimeout(function () {
                        $('#lastTreeTable').bootstrapTreeTable('destroy');
                        var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                            toolbar: "#demo-toolbar",    //顶部工具条
                            expandColumn: 1,            // 在哪一列上面显示展开按钮
                            showExport: true,
                            height: 0,
                            search:true,
                            expandAll: true,
                            columns: [{
                                field: 'selectItem',
                                radio: true
                            }, {
                                title: '部门名称',
                                field: 'typeName',
                                width: '10%',
                                formatter: function (value, row, index) {
                                    if(row.lv==1){
                                        return '<span style="color:red">'+value+'</span>';
                                    }
                                    return value;
                                }
                            },
                                {
                                    field: 'budgetMoney',
                                    title: '预算总人数',
                                    width: '8%',
                                    valign: "top",
                                    formatter: function (value, item, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'budgetProportion',
                                    title: '预算占比',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'employeeNumber',
                                    title: '从业人数',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                                {
                                    field: 'staffNumber',
                                    title: '职工人数',
                                    width: '5%',
                                    formatter: function (value, row, index) {
                                        return value;
                                    }
                                },
                            ],
                            data: value.data.data,
                        });
                    },500);
                }
            })*/
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
                fiveBudgetStaffNumberService.getNewModelDetail(staffNumberId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetStaffNumberService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetStaffNumberService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = staffNumberId;
            vm.detail.creator = user.userLogin;
            fiveBudgetStaffNumberService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetStaffNumberService.getDetailById(staffNumberId).then(function (value) {
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
            fiveBudgetStaffNumberService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetStaffNumberService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetStaffNumberService.update(vm.item).then(function (value) {
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
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.detail.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.typeName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //收费预算
    .controller("FiveBudgetFeeController", function ($state, $scope, $rootScope, fiveBudgetFeeService) {
        var vm = this;
        var uiSref = "budget.fee";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetFeeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.feeDetail", {feeId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetFeeService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetFeeService.remove(id, user.userLogin).then(function (value) {
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
            fiveBudgetFeeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{1:'deptName',2:'budgetYear',3:'budgetTotalMoney',4:'remark',5:'creator',6:'gmtCreate',7:'processName'},handleColumn:8};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;
    })
    .controller("FiveBudgetFeeDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetFeeService ) {
        var vm = this;
        var uiSref = "budget.fee";
        var tableName = $rootScope.loadTableName(uiSref);
        var feeId = $stateParams.feeId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetFeeService.getModelById(feeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        fiveBudgetFeeService.getDetailById(feeId).then(function (value) {
                            if (value.data.ret) {
                                vm.details = value.data.data;
                                $('#treeTable').bootstrapTreeTable('destroy')
                                var treeTable = $('#treeTable').bootstrapTreeTable({
                                    toolbar: "#demo-toolbar",    //顶部工具条
                                    expandColumn: 1,            // 在哪一列上面显示展开按钮
                                    showExport: true,
                                    height: 500,
                                    search:true,
                                    expandAll: true,
                                    condensed: true,        //紧凑
                                    onDblClickRow:function(){
                                        vm.showDetailModel(1);
                                    },
                                    columns: [{
                                        field: 'selectItem',
                                        radio: true
                                    }, {
                                        title: '部门名称',
                                        field: 'typeName',
                                        width: '20%',
                                        formatter: function (value, row, index) {
                                            if(row.lv==1){
                                                return '<span style="color:red">'+value+'</span>';
                                            }
                                            return value;
                                        }
                                    },
                                        {
                                            field: 'lastYearMoney',
                                            title: '上年预算',
                                            width: '5%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'lastYearSuccess',
                                            title: '上年预计完成',
                                            width: '5%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetMoney',
                                            title: '本年预算总金额',
                                            width: '8%',
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetProportion',
                                            title: '本年预算占比',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'designFee',
                                            title: '勘察设计、咨询收费',
                                            width: '10%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'projectFee',
                                            title: '工程承包收费',
                                            width: '10%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'otherFee',
                                            title: '其他收费',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },

                                    ],
                                    data: value.data.data,
                                });
                            }
                        })
                        /*fiveBudgetFeeService.getLastYearDetailById(feeId).then(function (value) {
                            if (value.data.ret) {
                                vm.lastYeardetails = value.data.data;
                                setTimeout(function () {
                                    $('#lastTreeTable').bootstrapTreeTable('destroy');
                                    var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                                        toolbar: "#demo-toolbar",    //顶部工具条
                                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                                        showExport: true,
                                        height: 500,
                                        search:true,
                                        expandAll: true,
                                        condensed: true,        //紧凑
                                        onDblClickRow:function(){
                                            vm.showDetailModel(1);
                                        },
                                        columns: [{
                                            field: 'selectItem',
                                            radio: true
                                        }, {
                                            title: '部门名称',
                                            field: 'typeName',
                                            width: '10%',
                                            formatter: function (value, row, index) {
                                                if(row.lv==1){
                                                    return '<span style="color:red">'+value+'</span>';
                                                }
                                                return value;
                                            }
                                        },
                                            {
                                                field: 'budgetMoney',
                                                title: '预算总金额',
                                                width: '8%',
                                                valign: "top",
                                                formatter: function (value, item, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'budgetProportion',
                                                title: '预算占比',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'designFee',
                                                title: '勘察设计、咨询收费',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'projectFee',
                                                title: '工程承包收费',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'otherFee',
                                                title: '其他收费',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },

                                        ],
                                        data: value.data.data,
                                    });
                                },500);
                            }
                        })*/
                    }
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
                fiveBudgetFeeService.getNewModelDetail(feeId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetFeeService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetFeeService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = feeId;
            vm.detail.creator = user.userLogin;
            fiveBudgetFeeService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetFeeService.getDetailById(feeId).then(function (value) {
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
            fiveBudgetFeeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetFeeService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetFeeService.update(vm.item).then(function (value) {
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
            if(vm.opt == "detailDeptId"){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.detail.deptId,
                    multiple: false
                });
            }else{
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt == "detailDeptId") {
                vm.detail.typeName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }else{
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
        }


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })

    //预算更改
    .controller("FiveBudgetFeeChangeController", function ($state, $scope, $rootScope, fiveBudgetFeeChangeService) {
        var vm = this;
        var uiSref = "budget.feeChange";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetFeeChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.feeChangeDetail", {feeId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetFeeChangeService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetFeeChangeService.remove(id, user.userLogin).then(function (value) {
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
            fiveBudgetFeeChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{1:'deptName',2:'budgetYear',3:'budgetTotalMoney',4:'remark',5:'creator',6:'gmtCreate',7:'processName'},handleColumn:8};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;
    })
    .controller("FiveBudgetFeeChangeDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService,fiveBudgetFeeService, fiveBudgetFeeChangeService,fiveBudgetIndependentService) {
        var vm = this;
        var uiSref = "budget.feeChange";
        var tableName = $rootScope.loadTableName(uiSref);
        var id = $stateParams.feeId;
        var feeId = 0;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetFeeChangeService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        if (vm.item.projectNo != 0){
                            fiveBudgetIndependentService.getModelById(vm.item.projectNo).then(function (value) {
                                if (value.data.ret) {
                                    vm.budgetIndepend = value.data.data;
                                    vm.item.totalBudgetMoney = vm.budgetIndepend.budgetTotalMoney;
                                }
                            })
                            fiveBudgetIndependentService.getDetailById(vm.item.projectNo).then(function (value) {
                                feeId = vm.item.projectNo;
                                if (value.data.ret) {
                                    vm.details = value.data.data;
                                    $('#treeTable').bootstrapTreeTable('destroy')
                                    var treeTable = $('#treeTable').bootstrapTreeTable({
                                        toolbar: "#demo-toolbar",    //顶部工具条
                                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                                        showExport: true,
                                        height: 500,
                                        search:true,
                                        expandAll: true,
                                        condensed: true,        //紧凑
                                        onDblClickRow:function(){
                                            vm.showDetailModel(1);
                                        },
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
                                                field: 'lastYearMoney',
                                                title: '上年预算',
                                                width: '20%',
                                                align: "left",
                                                valign: "top",
                                                formatter: function (value, item, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'lastYearSuccess',
                                                title: '上年预计完成',
                                                width: '20%',
                                                align: "left",
                                                valign: "top",
                                                formatter: function (value, item, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'budgetMoney',
                                                title: '本年预算金额',
                                                width: '20%',
                                                align: "left",
                                                valign: "top",
                                                formatter: function (value, item, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'budgetProportion',
                                                title: '本年预算占比',
                                                width: '15%',
                                                align: "left",
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'remark',
                                                title: '备注',
                                                width: '35%',
                                                align: "left",
                                            },
                                        ],
                                        data: value.data.data,
                                    });
                                }
                            });
                        }
                    }

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
                fiveBudgetIndependentService.getNewModelDetail(feeId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetIndependentService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetIndependentService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = feeId;
            vm.detail.creator = user.userLogin;
            fiveBudgetIndependentService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetIndependentService.getDetailById(feeId).then(function (value) {
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
            fiveBudgetFeeChangeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }


        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetIndependentService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetFeeChangeService.update(vm.item).then(function (value) {
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
            if(vm.opt == "detailDeptId"){
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.detail.deptId,
                    multiple: false
                });
            }else{
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt == "detailDeptId") {
                vm.detail.typeName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }else{
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
        }


        vm.selectFee = function(){
            vm.qFee = "";
            fiveBudgetIndependentService.selectAll(user.userLogin,"budget.independent").then(function (value) {
                vm.fees = value.data.data;
                console.log(vm.fees)
                singleCheckBox(".cb_fee", "data-name");
            });
            $("#selectFeeModal").modal("show");
        };

        vm.saveSelectFee = function(){
            $("#selectFeeModal").modal("hide");
            if ($(".cb_fee:checked").length > 0){
                var fee = $.parseJSON($(".cb_fee:checked").first().attr("data-name"));
                vm.item.projectNo = fee.id;
                feeId = vm.item.projectNo;
                vm.item.totalBudgetMoney = fee.budgetTotalMoney;
                vm.item.budgetYear = fee.budgetYear;
                vm.item.projectName = fee.title;
            }
            vm.save();
        };

        vm.saveChange = function () {
            vm.budgetIndepend.operateUserLogin = user.userLogin;
            fiveBudgetIndependentService.update(vm.budgetIndepend).then(function (value) {
                if (value.data.ret) {
                    toastr.success("变更信息 保存成功!");
                }
            })
            vm.loadData(true);
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })

    //上缴预算
    .controller("FiveBudgetTurnInController", function ($state, $scope, $rootScope, fiveBudgetTurnInService) {
        var vm = this;
        var uiSref = "budget.turnIn";
        var tableName = $rootScope.loadTableName(uiSref);
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
            fiveBudgetTurnInService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("budget.turnInDetail", {turnInId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBudgetTurnInService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBudgetTurnInService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetTurnInDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveBudgetTurnInService ) {
        var vm = this;
        var uiSref = "budget.turnIn";
        var turnInId = $stateParams.turnInId;
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBudgetTurnInService.getModelById(turnInId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        fiveBudgetTurnInService.getDetailById(turnInId).then(function (value) {
                            if (value.data.ret) {
                                vm.details = value.data.data;
                                $('#treeTable').bootstrapTreeTable('destroy')
                                var treeTable = $('#treeTable').bootstrapTreeTable({
                                    toolbar: "#demo-toolbar",    //顶部工具条
                                    expandColumn: 1,            // 在哪一列上面显示展开按钮
                                    showExport: true,
                                    height: 500,
                                    search:true,
                                    expandAll: true,
                                    condensed: true,        //紧凑
                                    onDblClickRow:function(){
                                        vm.showDetailModel(1);
                                    },
                                    columns: [{
                                        field: 'selectItem',
                                        radio: true
                                    }, {
                                        title: '部门名称',
                                        field: 'typeName',
                                        width: '10%',
                                        formatter: function (value, row, index) {
                                            if(row.lv==1){
                                                return '<span style="color:red">'+value+'</span>';
                                            }
                                            return value;
                                        }
                                    },
                                        {
                                            field: 'lastYearMoney',
                                            title: '上年预算',
                                            width: '5%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'lastYearSuccess',
                                            title: '上年预计完成',
                                            width: '5%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetMoney',
                                            title: '本年预算总金额',
                                            width: '8%',
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetProportion',
                                            title: '本年预算占比',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'turnProfits',
                                            title: '上缴利润',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'developmentFund',
                                            title: '发展基金',
                                            width: '5%',
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                    ],
                                    data: value.data.data,
                                });
                            }
                        })
                       /* fiveBudgetTurnInService.getLastYearDetailById(turnInId).then(function (value) {
                            if (value.data.ret) {
                                vm.lastYeardetails = value.data.data;
                                setTimeout(function () {
                                    $('#lastTreeTable').bootstrapTreeTable('destroy');
                                    var treeTable = $('#lastTreeTable').bootstrapTreeTable({
                                        toolbar: "#demo-toolbar",    //顶部工具条
                                        expandColumn: 1,            // 在哪一列上面显示展开按钮
                                        showExport: true,
                                        height: 500,
                                        search:true,
                                        expandAll: true,
                                        condensed: true,        //紧凑
                                        onDblClickRow:function(){
                                            vm.showDetailModel(1);
                                        },
                                        columns: [{
                                            field: 'selectItem',
                                            radio: true
                                        }, {
                                            title: '部门名称',
                                            field: 'typeName',
                                            width: '10%',
                                            formatter: function (value, row, index) {
                                                if(row.lv==1){
                                                    return '<span style="color:red">'+value+'</span>';
                                                }
                                                return value;
                                            }
                                        },
                                            {
                                                field: 'budgetMoney',
                                                title: '预算总金额',
                                                width: '8%',
                                                valign: "top",
                                                formatter: function (value, item, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'budgetProportion',
                                                title: '预算占比',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'turnProfits',
                                                title: '上缴利润',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                            {
                                                field: 'developmentFund',
                                                title: '发展基金',
                                                width: '5%',
                                                formatter: function (value, row, index) {
                                                    return value;
                                                }
                                            },
                                        ],
                                        data: value.data.data,
                                    });
                                },500);
                            }
                        })*/
                    }
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
                fiveBudgetTurnInService.getNewModelDetail(turnInId,detailId, user.userLogin).then(function (value) {
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
                fiveBudgetTurnInService.getModelDetailById(detailId).then(function (value) {
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
                    fiveBudgetTurnInService.removeDetail(detailId, user.userLogin).then(function (value) {
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
            vm.detail.budgetIndependentId = turnInId;
            vm.detail.creator = user.userLogin;
            fiveBudgetTurnInService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {

                    $("#detailModal").modal("hide");
                    $('#treeTable').bootstrapTreeTable('destroy');
                    vm.save();
                    vm.loadData(true);
                }
            })
        };
        vm.showParentTree = function (item) {
            fiveBudgetTurnInService.getDetailById(turnInId).then(function (value) {
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
            fiveBudgetTurnInService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.showBigNum = function () {
            if (vm.item.money != null) {
                fiveBudgetTurnInService.moneyTurnCapital(vm.item.money).then(function (value) {
                    vm.item.moneyMax = value.data.data;
                })
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;

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
            if (vm.status == 'remindReceiveMan') {
                vm.item.remindReceiveManName = $scope.selectedOaUserNames_;
                vm.item.remindReceiveMan = $scope.selectedOaUserLogins_;
            }

        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBudgetTurnInService.update(vm.item).then(function (value) {
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
                title: "请选择部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: vm.detail.deptId,
                multiple: false
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.typeName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //股权投资预算
    .controller("FiveBudgetStockController", function ($state, $scope,$rootScope, fiveBudgetStockService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            dispatchType: "股权投资预算",
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
        var uiSref = "budget.stock";
        var tableName = $rootScope.loadTableName(uiSref);

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
            fiveBudgetStockService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.show = function (id) {
            $state.go("budget.stockDetail", {stockId: id});
        }

        vm.add = function () {
            fiveBudgetStockService.getNewModel(user.userLogin,uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBudgetStockService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBudgetStockDetailController", function ($sce, $state, $stateParams, $rootScope, $scope, hrEmployeeService, fiveBudgetStockService, hrDeptService) {
        var vm = this;
        var uiSref = "budget.stock";
        var stockId = $stateParams.stockId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveBudgetStockService.getModelById(stockId).then(function (value) {
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
            fiveBudgetStockService.listDetail(stockId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
          /*  fiveBudgetStockService.listLastYearDetail(stockId).then(function (value) {
                if (value.data.ret) {
                    vm.lastDetails = value.data.data;
                }
            })*/
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBudgetStockService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        };

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

        };

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
                fiveBudgetStockService.update(vm.item).then(function (value) {
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

        };

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        };

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveBudgetStockService.getNewModelDetail(stockId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveBudgetStockService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        };
        //只读查看
        vm.showDetailModel1 = function (id) {
            fiveBudgetStockService.getModelDetailById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                    $("#detailModal1").modal("show");
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveBudgetStockService.removeDetail(id, user.userLogin).then(function (value) {
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
            fiveBudgetStockService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    vm.save();
                    $("#detailModal").modal("hide");
                }
            })
        };

        vm.print = function () {
            fiveBudgetStockService.getPrintData(stockId).then(function (value) {
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
        };
        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        };
        return vm;
    })
