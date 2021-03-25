angular.module('controllers.five.business', [])
    .controller("FiveBusinessContractController", function ($state, $stateParams, $rootScope, $scope, businessContractService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessContract";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            businessContractService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })

        }

        vm.show = function (contractId) {
            $state.go("five.businessContractDetail", {contractId: contractId});
        }
        vm.showEd = function (item) {
            $state.go("five.detail.show", {id: item.id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessContractService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessContractService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBusinessContractDetailController", function ($state, $rootScope, $stateParams, $scope, commonCodeService, hrDeptService, businessContractService, sysDeptService, businessRecordService, businessCustomerService, hrEmployeeService) {
        var vm = this;
        var contractId = $stateParams.contractId;
        vm.params = {name: "", qName: "", qCName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: 10};
        vm.optNum = 0;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            vm.loadData(true);

            $scope.loadRightData(user.userLogin, "five.businessContract");
        }

        vm.loadData = function (loadProcess) {
            businessContractService.getModelById(contractId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (vm.item.projectCity == null) {
                        vm.provinceId = "0";
                        vm.cityId = "0";
                    } else {
                        vm.provinceId = "" + provinceArr.indexOf(vm.item.projectProvince);
                        vm.cityId = "" + cityArr[vm.provinceId].indexOf(vm.item.projectCity);
                    }
                    vm.getCityArr = vm.cityArr[vm.provinceId]; //根据索引写入城市数据
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#signDate").datepicker('setDate', vm.item.signDate);
                    $("#planEndTime").datepicker('setDate', vm.item.businessContractDetail.planEndTime);
                    $("#planBeginTime").datepicker('setDate', vm.item.businessContractDetail.planBeginTime);
                }
            })
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };
        vm.showSendFlow = function () {
            if (vm.item.recordNo && $rootScope.processInstance.myRunningTaskName.indexOf('档案人员') < 0) {
                toastr.warning("请填写档案号！");
                return;
            }
            if ($scope.processInstance.myTaskFirst) {
                if (vm.item.businessContractDetail.totalDesignerName == undefined || vm.item.businessContractDetail.totalDesignerName == "") {
                    toastr.error("项目总师不能为空!");
                    return;
                }
                if (vm.item.chargeMenName == undefined || vm.item.chargeMenName == "") {
                    toastr.error("项目主管院长/所长不能为空!");
                    return;
                }
                if (vm.item.exeChargeMen == undefined || vm.item.exeChargeMen == "") {
                    toastr.error("项目经理不能为空!");
                    return;
                }
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    businessContractService.update(vm.item).then(function (value) {
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

        }


        vm.selectStageName = function () {
            var stageNames = [];
            $(".cb_stage:checked").each(function () {
                stageNames.push($(this).attr("name"));
            });
            vm.item.stageNames = stageNames.join(',');
            $("#contractStageNameModel").modal("hide");
        }


        vm.showCooperDeptModal = function (ids) {
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
                    if (ids.indexOf("," + item.id + ",") >= 0) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#cooper_dept_select_tree').jstree("destroy");
                $('#cooper_dept_select_tree')
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        },
                        "checkbox": {
                            "keep_selected_style": false
                        },
                        "plugins": ["wholerow", "checkbox"]
                    });
            });
            $("#cooperDeptSelectModal").modal("show");
        }

        vm.saveCooperDept = function () {
            var deptList = $('#cooper_dept_select_tree').jstree(true).get_selected(true);
            var ids = [];
            var names = [];
            for (var i = 0; i < deptList.length; i++) {
                ids.push(deptList[i].id)
                names.push(deptList[i].text)
            }
            vm.item.deptIds = "," + ids.join(",") + ",";
            vm.item.deptNames = names.join(",");
            $("#cooperDeptSelectModal").modal("hide");
        }

        vm.showUserModel = function () {
            if (vm.opt == "totalDesigner") {
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.item.businessContractDetail.totalDesigner,
                    multiple: false,
                    title: '项目总师-人员任命'
                });
            } else if (vm.opt == 'chargeMen') {
                var parentDeptId = user.deptId;
                if (user.deptName.indexOf("一院") >= 0) {
                    parentDeptId = 23;
                } else if (user.deptName.indexOf("二院") >= 0) {
                    parentDeptId = 69;
                } else if (user.deptName.indexOf("环能") >= 0) {
                    parentDeptId = 31;
                }
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.item.chargeMen,
                    title: '项目主管院长-人员任命',
                    multiple: false,
                    parentDeptId: parentDeptId
                });
            } else if (vm.opt == 'otherDesigner') {
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.item.businessContractDetail.otherDesigner,
                    title: '其他总师-人员任命'
                });
            } else if (vm.opt == 'exeChargeMen') {
                $scope.showSelectEmployeeModal_({userLoginList: vm.item.exeChargeMen, title: '项目经理-人员任命'});
            }
        }


        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if (vm.opt == "totalDesigner") {
                vm.item.businessContractDetail.totalDesignerName = $scope.selectedUserNames_;
                vm.item.businessContractDetail.totalDesigner = $scope.selectedUserLogins_;
            } else if (vm.opt == 'chargeMen') {
                vm.item.chargeMen = $scope.selectedUserLogins_;
                vm.item.chargeMenName = $scope.selectedUserNames_;
            } else if (vm.opt == 'otherDesigner') {
                vm.item.businessContractDetail.otherDesigner = $scope.selectedUserLogins_;
                vm.item.businessContractDetail.otherDesignerName = $scope.selectedUserNames_;
            } else if (vm.opt == 'exeChargeMen') {
                vm.item.exeChargeMen = $scope.selectedUserLogins_;
                vm.item.exeChargeMenName = $scope.selectedUserNames_;
            }
        };

        vm.showRecordModal = function () {
            vm.qRecord = "";
            businessRecordService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.records = value.data.data;
                singleCheckBox(".cb_record", "data-name");
            });
            $("#selectRecordModal").modal("show");
        }

        vm.saveSelectRecord = function () {
            $("#selectRecordModal").modal("hide");
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                vm.item.projectName = record.projectName;
                vm.item.projectType = record.projectType;
                vm.item.projectAddress = record.projectAddress;
                vm.item.investMoney = (Number)(record.projectInvest);
                if (record.customerId != 0) {
                    businessCustomerService.getModelByNameAndLinMan(record.customerName, record.linkMan).then(function (value) {
                        var customer = value.data.data;
                        vm.item.customerId = customer.id;
                        vm.item.customerName = customer.name;
                        vm.item.linkMan = customer.linkMan;
                        vm.item.linkTel = customer.linkTel;
                        vm.item.linkFax = customer.linkFax;
                        vm.item.linkMail = customer.linkMail;
                        vm.item.depositBank = customer.depositBank;
                        vm.item.bankAccount = customer.bankAccount;
                        vm.item.taxType = customer.taxType;
                        vm.item.taxNo = customer.taxNo;
                        vm.item.customerType = customer.customerType;
                    })
                } else {
                    vm.item.customerName = record.customerName;
                    vm.item.linkMan = record.linkMan;
                    vm.item.linkTel = record.linkTel;
                    vm.item.linkFax = record.linkFax;
                    vm.item.linkMail = record.linkMail;
                }
            }
        }


        vm.showCustomerModal = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerId = customer.id;
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.linkFax = customer.linkFax;
                vm.item.linkMail = customer.linkMail;
                vm.item.depositBank = customer.depositBank;
                vm.item.bankAccount = customer.bankAccount;
                vm.item.taxType = customer.taxType;
                vm.item.taxNo = customer.taxNo;
                vm.item.customerType = customer.customerType;
            }
        }


        vm.listBidContract = function () {
            vm.deptName = vm.item.deptName;
            businessContractService.listBusinessBidContract(vm.item.id).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                    singleCheckBox(".cb_bidContract", "data-name");
                    $("#bidContract").modal("show");
                }
            })
        }

        vm.chooseBidContract = function () {
            var bidContract = '';
            $(".cb_bidContract:checked").each(function () {
                bidContract = $.parseJSON($(this).attr("data-name"));
                vm.item.bidContractId = bidContract.id;
                vm.item.projectType = bidContract.projectType;
                vm.item.projectName = bidContract.projectName;
                vm.item.contractMoney = bidContract.contractMoney;
                vm.item.projectAddress = bidContract.projectAddress;
                vm.item.contractType = bidContract.contractType;
            });
            $("#bidContract").modal("hide");
        }

        //先保存再获取生成的合同号
        vm.refreshContractNo = function () {

            vm.item.operateUserLogin = user.userLogin;
            businessContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    businessContractService.getNewContractNo(vm.item.id).then(function (value) {
                        vm.item.contractNo = value.data.data;
                    })
                }
            })
        }

        vm.sortChiefDesigner = function () {
            vm.optNum = vm.optNum + 1;
            vm.employees.sort(
                function (a, b) {
                    if (vm.optNum % 2 === 0) {
                        return b.hrQualify.chiefDesigner - a.hrQualify.chiefDesigner;
                    } else {
                        return a.hrQualify.chiefDesigner - b.hrQualify.chiefDesigner;
                    }
                });
        }


        vm.provinceArr = provinceArr; //省份数据
        vm.cityArr = cityArr;    //城市数据
        vm.getCityArr = vm.cityArr[0]; //默认为省份
        vm.getCityIndexArr = ['0', '0']; //这个是索引数组，根据切换得出切换的索引就可以得到省份及城市
        //改变省份触发的事件 [根据索引更改城市数据]
        vm.provinceChange = function (index) {
            vm.getCityArr = vm.cityArr[index]; //根据索引写入城市数据
            vm.getCityIndexArr[1] = '0'; //清除残留的数据
            vm.getCityIndexArr[0] = index;
            vm.item.projectProvince = vm.provinceArr[index];
        }
        //改变城市触发的事件
        vm.cityChange = function (index) {
            vm.item.projectCity = vm.getCityArr[index];
        }

        vm.init();

        return vm;
    })

    .controller("FiveMeContractController", function ($state, $scope, businessContractService) {
        var vm = this;
        var uiSref = "five.businessContract";
        var tableName = $rootScope.loadTableName(uiSref);

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {q: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};

        $scope.basicInit();

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

        };

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref,
                q: vm.params.q
            };
            businessContractService.listAttendPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        };

        vm.show = function (item) {
            $state.go("five.detail.show", {id: item.id});
        }

        vm.loadPagedData();
        return vm;
    })
    //客户管理
    .controller("FiveBusinessCustomerController", function ($state, $scope, $rootScope,$stateParams, businessCustomerService) {
        var vm = this;
        var uiSref = "five.businessCustomer";
        var tableName = $rootScope.loadTableName(uiSref);

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = {
            qName: "",
            pageNum: 1,
            userLogin: user.userLogin,
            uiSref: uiSref,
            pageSize: $scope.pageSize,
            total: 0
        };
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};

        vm.init = function () {
            vm.loadPagedData();
            $scope.basicInit("");
            $scope.loadRightData(user.userLogin, uiSref);

            $("#uploadModelFile").fileupload({
                dataType: 'text',
                url: '/business/customer/receive.do?userLogin=' + user.userLogin,
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
                        var obj = result.msg.split(",");
                        toastr.success("导入数据成功!<br>" + "修改：" + obj[0] + "条   新增：" + obj[1] + "条");
                        vm.queryData();
                    } else {
                        toastr.error(result.msg);
                    }
                }
            });

        };


        vm.queryData = function () {
            vm.params.code = vm.params.qCode;
            vm.params.name = vm.params.qName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            businessCustomerService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    //setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (customerId) {
            $state.go("five.businessCustomerDetail", {customerId: customerId});
        }

        vm.add = function () {
            businessCustomerService.getNewModel(user.userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessCustomerService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.downloadModel = function () {
            businessCustomerService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params = $.extend(params, {userLogin: user.userLogin, uiSref: uiSref});
            businessCustomerService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'code', placeholder: '请输入客户编号..'},
                    2: {colName: 'name', placeholder: '请输入客户名称..'},
                    3: {colName: 'address', placeholder: '请输入客户地址..'},
                    4: {colName: 'deptName', placeholder: '请输入录入部门..'},
                    5: {colName: 'contractCount', placeholder: '请输入合作项目数量..'},
                    6: {colName: 'linkMan', placeholder: '请输入联系人..'},
                    7: {colName: 'linkTel', placeholder: '请输入联系电话..'},
                }, handleColumn: 10
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });


        /*  var date = new Date();
          var d = date.getDate();
          var m = date.getMonth();
          var y = date.getFullYear();



          /!* event source that contains custom events on the scope *!/
          $scope.events = [
              {title: 'All Day Event',start: new Date(y, m, 1)},
              {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
              {id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
              {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
              {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
              {title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'},
              {title:'zql',start:new Date(y,m,3)}
          ];

          /!* config object *!/
          $scope.uiConfig = {
              calendar:{
                  height: 450,
                  editable: false,
                  header:{
                      left: '',
                      center: 'title',
                      right: 'today prev,next'
                  }
              }
          };

          /!* event sources array*!/
          $scope.eventSources = [$scope.events];*/

        vm.init();

        return vm;
    })
    .controller("FiveBusinessCustomerDetailController", function ($rootScope, $state, commonFormService, $stateParams, $scope, commonFileService,businessCustomerService, hrDeptService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessCustomer";
        var tableName = $rootScope.loadTableName(uiSref);

        var customerId = $stateParams.customerId;

        vm.init = function () {
            vm.loadData(true);
            $scope.loadRightData(user.userLogin, uiSref);
            $rootScope.getTplConfig("fiveBusinessCustomer_" + customerId, user.enLogin);
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            /*if (send&&$rootScope.processInstance.currentTaskName.indexOf("档案人员") >= 0  && vm.item.code == "") {
                toastr.warning("请先填写 客户编号");
                return;
            }*/
            commonFileService.listDataCount(vm.item.businessKey, '-1').then(function (value) {
                if (value.data.data == 0) {
                    toastr.warning("请上传供方单位营业执照与相关资料!");
                    return;
                } else {
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        businessCustomerService.update(vm.item).then(function (value) {
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
            })
        }


        vm.loadData = function (loadProcess) {
            businessCustomerService.getModelById(customerId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        setInterval(function () {
                            vm.codeFlag = true;
                            if ($rootScope.processInstance != null && $rootScope.processInstance.myRunningTaskName != '') {
                                vm.codeFlag = $rootScope.processInstance.myRunningTaskName.indexOf("档案人员") < 0;
                            }
                        }, 50)
                    }
                }
            })
            businessCustomerService.listCooperationProject(customerId).then(function (value) {
                vm.list = value.data.data;
            })
            vm.loadUsedNames(customerId);
        };
        vm.save = function () {
                vm.item.operateUserLogin = user.userLogin;
                businessCustomerService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!");
                    }
                })
        };
        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId,
                multiple: false, deptIds: "1"
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showContractLibrary = function (id) {
            $state.go("five.businessContractLibraryDetail", {contractLibraryId: id});
        }

        vm.loadUsedNames = function (customerId) {
            businessCustomerService.listUsedNamesById(customerId).then(function (value) {
                if (value.data.ret) {
                    vm.usedNames = value.data.data;
                }
            })
        };
        vm.showUsedName = function (id) {
            businessCustomerService.getUsedNameById(id).then(function (value) {
                if (value.data.ret) {
                    vm.usedName = value.data.data;
                }
            })
            $("#usedNameModal").modal("show");
        }
        vm.updateUsedName = function () {
            if ($("#comment_form").validate().form()) {
                businessCustomerService.updateUsedName(vm.usedName).then(function () {
                    vm.loadUsedNames(customerId);
                });
                $("#usedNameModal").modal("hide");
            }
        };
        vm.addUsedName = function () {
            businessCustomerService.getNewUsedName(customerId,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.showUsedName(value.data.data);
                }
            });
        };
        vm.removeUsedName= function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessCustomerService.removeUsedName(id,user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadUsedNames(customerId);
                        }
                    })
                }
            })
        }

        vm.checkCustomer = function (name) {
            vm.item.operateUserLogin = user.userLogin;
            businessCustomerService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    businessCustomerService.checkCustomer(name, customerId).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("客户名称 验证通过")
                        }
                    })
                }
            })
        };


        vm.checkTaxNo = function (taxNo) {
            vm.item.operateUserLogin = user.userLogin;
            businessCustomerService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    businessCustomerService.checkTaxNo(taxNo, customerId).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("纳税人识别号 验证通过")
                        }
                    })
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("客户信息管理");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })
    //客户信息变更
    .controller("FiveBusinessChangeCustomerController", function ($state, $scope,$rootScope, $stateParams, businessChangeCustomerService) {
        var vm = this;
        var uiSref = "five.businessChangeCustomer";
        var tableName = $rootScope.loadTableName(uiSref);

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            qName: "",
            qCode: "",
            name: "",
            code: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });

        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};

        vm.init = function () {
            vm.loadPagedData();
            $scope.basicInit("");
            $scope.loadRightData(user.userLogin, uiSref);

            $("#uploadModelFile").fileupload({
                dataType: 'text',
                url: '/business/changeCustomer/receive.do?userLogin=' + user.userLogin,
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
                        var obj = result.msg.split(",");
                        toastr.success("导入数据成功!<br>" + "修改：" + obj[0] + "条   新增：" + obj[1] + "条");
                        vm.queryData();
                    } else {
                        toastr.error(result.msg);
                    }
                }
            });

        };

        vm.queryData = function () {
            vm.params.code = vm.params.qCode;
            vm.params.name = vm.params.qName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            businessChangeCustomerService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (changeCustomerId) {
            $state.go("five.businessChangeCustomerDetail", {changeCustomerId: changeCustomerId});
        }

        vm.add = function () {
            businessChangeCustomerService.getNewModel(user.userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessChangeCustomerService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.downloadModel = function () {
            businessChangeCustomerService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        vm.init();

        return vm;
    })
    .controller("FiveBusinessChangeCustomerDetailController", function ($rootScope, $state, commonFormService, $stateParams, $scope, businessCustomerService, businessChangeCustomerService, hrDeptService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessChangeCustomer";
        var tableName = $rootScope.loadTableName(uiSref);

        var changeCustomerId = $stateParams.changeCustomerId;

        vm.init = function () {
            vm.loadData(true);
            $scope.loadRightData(user.userLogin, uiSref);
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.customerId == 0) {
                toastr.error("请先选择需要变更的 客户信息!");
                return
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessChangeCustomerService.update(vm.item).then(function (value) {
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
        vm.loadData = function (loadProcess) {
            businessChangeCustomerService.getModelById(changeCustomerId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    if (vm.item.customerId != 0) {
                        businessCustomerService.getModelById(vm.item.customerId).then(function (value) {
                            if (value.data.ret) {
                                vm.customer = value.data.data;
                            }
                        })
                    }

                }
            })
        };
        vm.save = function () {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessChangeCustomerService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!");
                    }
                })
            }
        };
        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId,
                multiple: false, deptIds: "1"
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.customer.deptName = $scope.selectedOaDeptNames_;
            vm.customer.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.saveChange = function () {
            if ($("#detail_form1").validate().form()) {
                vm.customer.operateUserLogin = user.userLogin;
                businessCustomerService.update(vm.customer).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("变更信息 保存成功!");
                    }
                })
            }
        };

        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerId = customer.id;
                vm.item.customerName = customer.name;
            }
            vm.save();
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("客户信息变更");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })
    //项目信息备案
    .controller("FiveBusinessRecordController", function ($rootScope,$state, $scope, businessRecordService, businessBidApplyService) {
        var vm = this;
        var uiSref = "five.businessRecord";
        var tableName = $rootScope.loadTableName(uiSref);

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            $scope.basicInit("");

            vm.params.timeType = "无";
            vm.params.startTime = " ";
            vm.params.endTime = " ";
            vm.loadPagedData();
        }


        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                timeType: vm.params.timeType,
                startTime: vm.params.startTime,
                endTime: vm.params.endTime,
                uiSref: uiSref
            });
            businessRecordService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (item) {
            if(item.processEnd&&$rootScope.rightData.selectOpts.indexOf('管理')>=0){
                $state.go("five.businessRecordDetailManger", {recordId: item.id});
            }else{
                $state.go("five.businessRecordDetail", {recordId: item.id});
            }
        }

        vm.showPre = function (id) {
            $("#preModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessPreContractDetail", {preContractId: id});
            }, 200)

        }
        vm.showReview = function (id) {
            $("#reviewModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessContractReviewDetail", {contractReviewId: id});
            }, 200)

        }

        //合同评审情况
        vm.showReviewModal = function (recordId) {
            businessRecordService.selectReview(user.userLogin, recordId).then(function (value) {
                if (value.data.ret) {
                    vm.reviews = value.data.data;
                }
            })
            $("#reviewModal").modal("show");
        };
        //超前任务情况
        vm.showPreModal = function (recordId) {
            businessRecordService.selectPre(user.userLogin, recordId).then(function (value) {
                if (value.data.ret) {
                    vm.pres = value.data.data;
                }
            })
            $("#preModal").modal("show");
        };

        vm.add = function () {
            var userLogin = user.userLogin;
            businessRecordService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessRecordService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.addApply = function (id) {
            businessBidApplyService.getNewModelById(id, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    $state.go("business.bidApplyDetail", {bidApplyId: value.data.data});
                }
            })
        }

        vm.downloadModel = function () {
            businessRecordService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params.uiSref = uiSref;
            params.userLogin = user.userLogin;
            businessRecordService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'projectName', placeholder: '请输入项目名称..'},
                    2: {colName: 'projectNo', placeholder: '请输入项目号..'},
                    3: {colName: 'customerName', placeholder: '请输入客户名称..'},
                    4: {colName: 'deptName', placeholder: '请输入部门名称..'},
                    5: {colName: 'creatorName', placeholder: '请输入经办人..'},
                    6: {colName: 'gmtCreate', placeholder: '请输入创建时间..'},
                }, handleColumn: 10
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.init();

        return vm;
    })
    .controller("FiveBusinessRecordDetailController", function ($state, $stateParams, $rootScope, $scope, businessRecordService, fiveBusinessContractLibrarySubpackageService, businessCustomerService, hrEmployeeService, hrDeptService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessRecord";
        var tableName = $rootScope.loadTableName(uiSref);

        var recordId = $stateParams.recordId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            businessRecordService.getModelById(recordId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#bidPlanOpen").datepicker('setDate', vm.item.bidPlanOpen);
                    $("#bidRealOpen").datepicker('setDate', vm.item.bidRealOpen);
                    $("#planEndTime").datepicker('setDate', vm.item.planEndTime);
                    $("#planBeginTime").datepicker('setDate', vm.item.planBeginTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessRecordService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.showCustomer = function (customerId) {
            if (customerId == 0) {
                toastr.error("请选择客户信息 或者 填写后点击保存客户信息！")
            } else {
                $state.go("five.businessCustomerDetail", {customerId: customerId});
            }
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerId = customer.id;
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.customerAddress = customer.address;
                vm.item.linkTitle = customer.linkTitle;
                vm.item.customerCode = customer.code
            }
            vm.save();
        }

        vm.addCustomer = function (recordId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.customerName == '' || vm.item.customerName == null) {
                toastr.error("请先填写客户信息");
            } else {
                businessRecordService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        businessRecordService.addCustomer(user.userLogin, recordId).then(function (value) {
                            if (value.data.ret) {
                                businessCustomerService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.customerId = value.data.data.id;
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                        vm.save();
                                    }
                                })
                            }
                        });
                    }
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

        //项目编号
        vm.getProjectNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessRecordService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    businessRecordService.getProjectNo(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("项目号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.projectNo == "") {
                toastr.error("请先填写 项目号");
                return;
            }
            if (vm.item.taxNo == "") {
                toastr.error("请先补录 客户信息的纳税人识别号！");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessRecordService.update(vm.item).then(function (value) {
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

        //对内分包
        vm.showSubpackageModal = function () {
            fiveBusinessContractLibrarySubpackageService.listSubpackageByUserLogin(user.userLogin, vm.item.subpackageId).then(function (value) {
                if (value.data.ret) {
                    vm.lists = value.data.data;
                    singleCheckBox(".cb_subpackage", "data-name");
                }
            })
            $("#selectSubpackageModal").modal("show");
        }
        vm.saveSelectSubpackage = function () {
            if ($(".cb_subpackage:checked").length > 0) {
                var subpackage = $.parseJSON($(".cb_subpackage:checked").first().attr("data-name"));
                //需要同步到 项目备案 的数据
                vm.item.subpackageId = subpackage.id;
                //项目名称
                vm.item.projectName = subpackage.subContractName;
                //投资额
                vm.item.investMoney = subpackage.subContractMoney;
                //信息提供部门
                vm.item.messageDeptId = subpackage.deptId;
                vm.item.messageDeptName = subpackage.deptName;
            }
            vm.save();
            $("#selectSubpackageModal").modal("hide");
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

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("信息化设备验收(多台)审批");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }


        return vm;

    })
    .controller("FiveBusinessRecordDetailMangerController", function ($state, $stateParams, $rootScope, $scope, businessRecordService, fiveBusinessContractLibrarySubpackageService, businessCustomerService, hrEmployeeService, hrDeptService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessRecord";
        var tableName = $rootScope.loadTableName(uiSref);

        var recordId = $stateParams.recordId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            businessRecordService.getModelById(recordId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#bidPlanOpen").datepicker('setDate', vm.item.bidPlanOpen);
                    $("#bidRealOpen").datepicker('setDate', vm.item.bidRealOpen);
                    $("#planEndTime").datepicker('setDate', vm.item.planEndTime);
                    $("#planBeginTime").datepicker('setDate', vm.item.planBeginTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessRecordService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.showCustomer = function (customerId) {
            if (customerId == 0) {
                toastr.error("请选择客户信息 或者 填写后点击保存客户信息！")
            } else {
                $state.go("five.businessCustomerDetail", {customerId: customerId});
            }
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerId = customer.id;
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.customerAddress = customer.address;
                vm.item.linkTitle = customer.linkTitle;
                vm.item.customerCode = customer.code
            }
            vm.save();
        }

        vm.addCustomer = function (recordId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.customerName == '' || vm.item.customerName == null) {
                toastr.error("请先填写客户信息");
            } else {
                businessRecordService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        businessRecordService.addCustomer(user.userLogin, recordId).then(function (value) {
                            if (value.data.ret) {
                                businessCustomerService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.customerId = value.data.data.id;
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                        vm.save();
                                    }
                                })
                            }
                        });
                    }
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

        //项目编号
        vm.getProjectNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessRecordService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    businessRecordService.getProjectNo(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("项目号已更新!");
                        }
                    })
                }
            })
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.projectNo == "") {
                toastr.error("请先填写 项目号");
                return;
            }
            if (vm.item.taxNo == "") {
                toastr.error("请先补录 客户信息的纳税人识别号！");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessRecordService.update(vm.item).then(function (value) {
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

        //对内分包
        vm.showSubpackageModal = function () {
            fiveBusinessContractLibrarySubpackageService.listSubpackageByUserLogin(user.userLogin, vm.item.subpackageId).then(function (value) {
                if (value.data.ret) {
                    vm.lists = value.data.data;
                    singleCheckBox(".cb_subpackage", "data-name");
                }
            })
            $("#selectSubpackageModal").modal("show");
        }
        vm.saveSelectSubpackage = function () {
            if ($(".cb_subpackage:checked").length > 0) {
                var subpackage = $.parseJSON($(".cb_subpackage:checked").first().attr("data-name"));
                //需要同步到 项目备案 的数据
                vm.item.subpackageId = subpackage.id;
                //项目名称
                vm.item.projectName = subpackage.subContractName;
                //投资额
                vm.item.investMoney = subpackage.subContractMoney;
                //信息提供部门
                vm.item.messageDeptId = subpackage.deptId;
                vm.item.messageDeptName = subpackage.deptName;
            }
            vm.save();
            $("#selectSubpackageModal").modal("hide");
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

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("信息化设备验收(多台)审批");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }


        return vm;

    })

    //合同评审
    .controller("FiveBusinessContractReviewController", function ($state, $scope,$rootScope, fiveBusinessContractReviewService) {
        var vm = this;
        vm.params = {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0};
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessContractReview";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                uiSref: uiSref,
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin
            });
            fiveBusinessContractReviewService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.businessContractReviewDetail", {contractReviewId: id});
        }

        vm.add = function () {
            fiveBusinessContractReviewService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessContractReviewService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.queryData();

        vm.changeOpen = function (id) {
            bootbox.confirm("您确定修改该合同关闭状态吗？", function (result) {
                if (result) {
                    fiveBusinessContractReviewService.changeOpen(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("切换成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.changeOpenStamp = function (id) {
            bootbox.confirm("您确定修改该合同印花税状态吗", function (result) {
                if (result) {
                    fiveBusinessContractReviewService.changeOpenStamp(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("切换成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.showPreContract = function (preContractId) {
            $state.go("five.businessPreContractDetail", {preContractId: preContractId});
        }
        vm.showSubpackage = function (subpackageId) {
            $state.go("five.businessSubpackageDetail", {subpackageId: subpackageId});
        }


        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params = $.extend(params, {userLogin: user.userLogin, uiSref: uiSref});
            fiveBusinessContractReviewService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'projectName', placeholder: '请输入备案项目名称..'},
                    2: {colName: 'contractName', placeholder: '请输入合同名称..'},
                    3: {colName: 'contractNo', placeholder: '请输入合同号..'},
                    4: {colName: 'projectNo', placeholder: '请输入项目号..'},
                    5: {colName: 'contractMoney', placeholder: '请输入合同额..'},
                    6: {colName: 'customerName', placeholder: '请输入客户名称..'},
                    6: {colName: 'reviewLevel', placeholder: '请输入评审级别..'},
                    12: {colName: 'processName', placeholder: '流程状态..'}
                }, handleColumn: 13
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;

    })
    .controller("FiveBusinessContractReviewDetailController", function ($state, $scope, $stateParams, $rootScope, commonCodeService, fiveCommonCodeService, fiveBusinessContractLibraryService, businessRecordService, businessCustomerService, fiveBusinessContractReviewService,commonPrintTableService) {
        var vm = this;
        var contractReviewId = $stateParams.contractReviewId;
        var uiSref = "five.businessContractReview";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            vm.loadData(true);
            vm.loadDetail(contractReviewId);
            commonCodeService.listDataByCatalog(user.userLogin, "项目专业分类一级").then(function (value) {
                if (value.data.ret) {
                    vm.projectMajorTypeFirst = value.data.data;
                }
            })
          /*  commonCodeService.listDataByCatalog(user.userLogin, "项目专业分类二级").then(function (value) {
                if (value.data.ret) {
                    vm.projectMajorTypeSecond = value.data.data;
                }
            })
            commonCodeService.listDataByCatalog(user.userLogin, "项目专业分类三级").then(function (value) {
                if (value.data.ret) {
                    vm.projectMajorTypeThird = value.data.data;
                }
            })*/
            $("#contractAttachUrl").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url: '/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator: user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star = file.name.lastIndexOf(".");
                    var fileType = file.name.substring(star, file.name.length);
                    if (".pdf,.png,.jpg,.bmp,.jpeg".indexOf(fileType) == -1) {
                        toastr.error('请上传.pdf,.png,.jpg,.bmp,.jpeg格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.contractAttachUrl = '/common/attach/download/' + result.data.id;
                        //新增正式签章合同文件夹
                        fiveBusinessContractReviewService.addSuccessContractDir(vm.item.businessKey,result.data.id,user.userLogin).then(function (value) {
                            if (value.data.ret) {
                                fiveBusinessContractReviewService.update(vm.item).then(function (value) {
                                    if (value.data.ret) {
                                        toastr.success("正式签章合同文件夹已创建!其他相关资料请在附件中上传");
                                        //刷新附件
                                        $scope.loadProcessInstance(vm.item.processInstanceId);
                                    }
                                })

                            }
                        })


                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.projectName == "" && !vm.item.main) {
                toastr.error("请先填写选择备案项目");
                return;
            }
            if (vm.item.contractNo == "") {
                toastr.error("请先填写 合同号");
                return;
            }
            if (vm.item.deptReviewUserName == '') {
                toastr.error("请先填写 院级评审人员");
                return;
            }
            if (vm.item.totalDesignerName == "" || vm.item.projectManagerName == "" || vm.item.businessChargeLeaderName == "") {
                toastr.warning("请先填写 项目经理/总设计师/合同分管副总!");
                return;
            }
            //如果是 盖章合同上传 节点 填写签约时间 和 扫描件
            if (($rootScope.processInstance.currentTaskName.indexOf("盖章合同上传") >= 0 || $rootScope.processInstance.currentTaskName.indexOf("确认") >= 0) && (vm.item.signTime == "" || vm.item.contractAttachUrl == "")) {
                toastr.warning("请先填写 签约时间 合同盖章扫描附件");
                return;
            }
            //如果是 印花税 节点 填写到账金额
            if ($rootScope.processInstance.currentTaskName.indexOf("印花税") >= 0 && (vm.item.contractMoney == "" || vm.item.taxType == "")) {
                toastr.warning("请先填写 合同额 印花税目");
                return;
            }
            if ($("#detail_form").validate().form() && $("#detail_form4").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBusinessContractReviewService.update(vm.item).then(function (value) {
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

        vm.loadData = function (loadProcess) {
            fiveBusinessContractReviewService.getModelById(contractReviewId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadNewProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#contractTime").datepicker('setDate', vm.item.contractTime);
                    $("#reviewTime").datepicker('setDate', vm.item.reviewTime);
                }
            })
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessContractReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        }

        //项目信息备案
        vm.showRecordModal = function () {
            $scope.showRecordSelectModal_({
                selectRecordId: vm.item.recordId,
                uiSref: uiSref,
                multiple: false
            });
        }
        $rootScope.saveSelectRecord_ = function () {

            //需要从 备案 中同步到 评审的数据
            vm.item.recordId =  $rootScope.selectedRecord.id;
            //项目名称
            vm.item.projectName =  $rootScope.selectedRecord.projectName;
            //建设规模
            vm.item.constructionScale = $rootScope.selectedRecord.projectScale;
            //投资额
            vm.item.investMoney = $rootScope.selectedRecord.projectInvest;
            //合同额
            //vm.item.contractMoney = record.projectInvest;
            vm.item.contractMoney = "";
            //建设地址
            vm.item.constructionArea = $rootScope.selectedRecord.projectAddress;
            //投资来源
            vm.item.investSource = $rootScope.selectedRecord.investSource;
            //项目性质
            vm.item.projectNature = $rootScope.selectedRecord.projectType;
            //项目号
            vm.item.projectNo = $rootScope.selectedRecord.projectNo;
            //客户信息
            vm.item.customerId = $rootScope.selectedRecord.customerId;
            vm.item.customerCode = $rootScope.selectedRecord.customerCode;
            vm.item.customerName = $rootScope.selectedRecord.customerName;
            vm.item.customerAddress = $rootScope.selectedRecord.customerAddress;
            vm.item.linkMan = $rootScope.selectedRecord.linkMan;
            vm.item.linkTel = $rootScope.selectedRecord.linkTel;
            vm.item.linkTitle = $rootScope.selectedRecord.linkTitle;
            $scope.closeRecordSelectModal_();
            vm.save();
        }


        vm.saveSelectRecordModel = function () {
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                //需要同步到 合同评审 的数据
                vm.item.recordId = record.id;
                //项目名称
                vm.item.projectName = record.projectName;
                //建设规模
                vm.item.constructionScale = record.projectScale;
                //投资额
                vm.item.investMoney = record.projectInvest;
                //合同额
                //vm.item.contractMoney = record.projectInvest;
                vm.item.contractMoney = "";
                //建设地址
                vm.item.constructionArea = record.projectAddress;
                //投资来源
                vm.item.investSource = record.investSource;
                //项目性质
                vm.item.projectNature = record.projectType;
                //项目号
                vm.item.projectNo = record.projectNo;
                //客户信息
                vm.item.customerId = record.customerId;
                vm.item.customerCode = record.customerCode;
                vm.item.customerName = record.customerName;
                vm.item.customerAddress = record.customerAddress;
                vm.item.linkMan = record.linkMan;
                vm.item.linkTel = record.linkTel;
                vm.item.linkTitle = record.linkTitle;
                //如果选择的备案已有合同 自动显示为补充合同
                /* if(record.contractReviewId!=0){
                     vm.item.main =true;
                 }*/
            }
            vm.save();
            $("#selectRecordModal").modal("hide");
        }

        //项目专业分类 三级联动
        vm.changeMajorTypeFirst = function () {
            fiveCommonCodeService.listDataByCodeAndCodeCatalog(vm.item.projectMajorTypeFirst, "项目专业分类二级").then(function (value) {
                if (value.data.ret) {
                    vm.projectMajorTypeSecond = value.data.data;
                    vm.projectMajorTypeThird = "";
                }
            });
        }
        vm.changeMajorTypeSecond = function () {
            if (vm.projectMajorTypeSecond.size != 0) {
                fiveCommonCodeService.listDataByCodeAndCodeCatalog(vm.item.projectMajorTypeSecond, "项目专业分类三级").then(function (value) {
                    if (value.data.ret) {
                        vm.projectMajorTypeThird = value.data.data;
                    }
                })
            }
        }

        $scope.changeSelect = function () {
            $("#_input").val($("#select option:selected").text());
        }

        vm.showCustomer = function (customerId) {
            if (customerId == 0) {
                toastr.error("请选择客户信息 或者 填写后点击保存客户信息！")
            } else {
                $state.go("five.businessCustomerDetail", {customerId: customerId});
            }
        }
        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.customerAddress = customer.address;
                vm.item.linkTitle = customer.linkTitle;
                vm.item.customerCode = customer.code;
                vm.item.customerId = customer.id;
            }
            vm.save();
        }
        vm.addCustomer = function (recordId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.customerName == '' || vm.item.customerName == null) {
                toastr.error("请先填写客户信息");
            } else {

                fiveBusinessContractReviewService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        fiveBusinessContractReviewService.addCustomer(user.userLogin, recordId).then(function (value) {
                            if (value.data.ret) {
                                businessCustomerService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.customerId = value.data.data.id;
                                        vm.item.customerName = value.data.data.name;
                                        vm.save();
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                    }
                                })
                            }
                        });
                    }
                })
            }
        }

        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择承接部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.deptId + ",",
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
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'reviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择公司级参加评审人员",
                    type: "部门", deptIds: "1", userLoginList: vm.item.reviewUser, multiple: true
                });
            }
            if (vm.status == 'deptReviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择院级参加评审人员",
                    type: "部门",
                    deptIds: vm.item.headDeptId,
                    parentDeptId: vm.item.headDeptId,
                    userLoginList: vm.item.deptReviewUser,
                    multiple: true
                });
            }
            if (vm.status == 'totalDesigner') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择总设计师人员",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.totalDesigner,
                    multiple: false,
                    parentDeptId: user.deptId
                });
            }
            if (vm.status == 'projectManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.projectManager,
                    multiple: false,
                    parentDeptId: user.deptId
                });
            }
            if (vm.status == 'businessChargeLeader') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择合同分管副总",
                    type: "人员",
                    userLogins: "3996,2192,0166,2873,2996",
                    userLoginList: vm.item.businessChargeLeader,
                    multiple: false,
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'reviewUser') {
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'deptReviewUser') {
                vm.item.deptReviewUser = $scope.selectedOaUserLogins_;
                vm.item.deptReviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'totalDesigner') {
                vm.item.totalDesigner = $scope.selectedOaUserLogins_;
                vm.item.totalDesignerName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'projectManager') {
                vm.item.projectManager = $scope.selectedOaUserLogins_;
                vm.item.projectManagerName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'businessChargeLeader') {
                vm.item.businessChargeLeader = $scope.selectedOaUserLogins_;
                vm.item.businessChargeLeaderName = $scope.selectedOaUserNames_;
            }
        };

        vm.loadDetail = function (contractReviewId) {
            fiveBusinessContractReviewService.listDetailById(contractReviewId).then(function (value) {
                if (value.data.ret) {
                    vm.contractReviewDetails = value.data.data;
                }
            })
        };
        vm.showDetail = function (id) {
            fiveBusinessContractReviewService.getDetailModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                }
            })
            $("#detailModal").modal("show");
        }
        vm.saveDetail = function () {
            if ($("#comment_form").validate().form()) {
                fiveBusinessContractReviewService.updateDetail(vm.detail).then(function () {
                    vm.loadDetail(contractReviewId);
                });
                $("#detailModal").modal("hide");
            }
        };
        vm.addDetail = function () {
            fiveBusinessContractReviewService.getNewDetailModel(contractReviewId).then(function (value) {
                if (value.data.ret) {
                    vm.showDetail(value.data.data);
                }
            });
        };
        vm.removeDetail = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBusinessContractReviewService.removeDetail(id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadDetail(contractReviewId);
                        }
                    })
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("信息化设备验收(多台)审批");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        //合同编号
        vm.getContractNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessContractReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    if (!vm.item.main) {
                        fiveBusinessContractReviewService.getContractNo(vm.item.id).then(function (value) {
                            if (value.data.ret) {
                                vm.loadData();
                                toastr.success("合同号已更新!");
                            }
                        })
                    } else {
                        fiveBusinessContractReviewService.getMainContractNo(vm.item.id).then(function (value) {
                            if (value.data.ret) {
                                vm.loadData();
                                toastr.success("补充合同号已更新!");
                            }
                        })
                    }

                }
            })

        }

        //合同库
        /*        vm.showSelectMainLibraryModal = function () {
                    fiveBusinessContractLibraryService.selectMainContract("").then(function (value) {
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
                        //需要从 合同库 中同步到 评审的数据
                        vm.item.mainContractLibraryId = library.id;
                        vm.item.mainContractLibraryName = library.contractName;
                        vm.item.mainContractLibraryNo =library.contractNo;
                        vm.item.projectNo =library.projectNo;
                        //客户信息
                        vm.item.customerId = library.customerId;
                        vm.item.customerCode = library.customerCode;
                        vm.item.customerName = library.customerName;
                        vm.item.customerAddress = library.customerAddress;
                        vm.item.linkMan = library.linkMan;
                        vm.item.linkTel = library.linkTel;
                        vm.item.linkTitle = library.linkTitle;
                    }
                    vm.save();
                    $("#selectLibraryModal").modal("hide");
                }*/

        vm.showSelectMainLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: vm.item.mainContractLibraryId,
                uiSref: uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            //需要从 合同库 中同步到 评审的数据
            vm.item.mainContractLibraryId = $rootScope.selectedLibrary.id;
            vm.item.mainContractLibraryName = $rootScope.selectedLibrary.contractName;
            vm.item.mainContractLibraryNo = $rootScope.selectedLibrary.contractNo;
            vm.item.projectNo = $rootScope.selectedLibrary.projectNo;
            //客户信息
            vm.item.customerId = $rootScope.selectedLibrary.customerId;
            vm.item.customerCode = $rootScope.selectedLibrary.customerCode;
            vm.item.customerName = $rootScope.selectedLibrary.customerName;
            vm.item.customerAddress = $rootScope.selectedLibrary.customerAddress;
            vm.item.linkMan = $rootScope.selectedLibrary.linkMan;
            vm.item.linkTel = $rootScope.selectedLibrary.linkTel;
            vm.item.linkTitle = $rootScope.selectedLibrary.linkTitle;
            $scope.closeLibrarySelectModal_();
            vm.save();
        }


        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })
    //合同库
    .controller("FiveBusinessContractLibraryController", function ($state, $scope, $rootScope, fiveBusinessContractLibraryService) {
        var vm = this;
        var uiSref = "five.businessContractLibrary";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.params = {
            qName: "",
            pageNum: 1,
            userLogin: user.userLogin,
            uiSref: uiSref,
            pageSize: $scope.pageSize,
            total: 0
        };
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                uiSref: uiSref,
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin
            });
            fiveBusinessContractLibraryService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id, subpackageId) {
            if (subpackageId != 0) {
                $state.go("five.businessSubpackageDetail", {subpackageId: subpackageId});
            } else {
                $state.go("five.businessContractLibraryDetail", {contractLibraryId: id});
            }
        }
        vm.showNew = function (id) {
            if (vm.opt == 1) {
                $state.go("five.businessPreContractDetail", {preContractId: id});
            } else {
                $state.go("five.businessContractReviewDetail", {contractReviewId: id});
            }
        }
        vm.showEd = function (contractId) {
            $state.go("business.contractDetail", {contractId: contractId});
        }

        vm.add = function () {
            fiveBusinessContractLibraryService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data, 0);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessContractLibraryService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }
        //分包情况
        vm.showSubpackageModal = function (libraryId) {
            fiveBusinessContractLibraryService.selectSubpackage(user.userLogin, "", libraryId).then(function (value) {
                if (value.data.ret) {
                    vm.subpackages = value.data.data;
                }
            })
            $("#subpackageModal").modal("show");
        };
        //收入情况
        vm.showIncomeModal = function (libraryId) {
            fiveBusinessContractLibraryService.selectIncome(user.userLogin, "", libraryId).then(function (value) {
                if (value.data.ret) {
                    vm.incomes = value.data.data;
                }
            })
            $("#incomeModal").modal("show");
        };
        //发票情况
        vm.showInvoiceModal = function (libraryId) {
            fiveBusinessContractLibraryService.selectInvoice(user.userLogin, "", libraryId).then(function (value) {
                if (value.data.ret) {
                    vm.invoices = value.data.data;
                }
            })
            $("#invoiceModal").modal("show");
        };
        //补充情况
        vm.showMainReviewModal = function (libraryId) {
            fiveBusinessContractLibraryService.selectMainReviewByIds(user.userLogin, "", libraryId).then(function (value) {
                if (value.data.ret) {
                    vm.mainReviews = value.data.data;
                }
            })
            $("#mainReviewModal").modal("show");
        };

        vm.jumpSubpackage = function (subpackageId) {
            $("#subpackageModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessSubpackageDetail", {subpackageId: subpackageId});
            }, 200);

        }
        vm.jumpIncome = function (incomeId) {
            $("#incomeModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessIncomeDetail", {incomeId: incomeId});
            }, 200);

        }
        vm.jumpMainReview = function (mainReviewId) {
            $("#mainReviewModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessContractReviewDetail", {contractReviewId: mainReviewId});
            }, 200);

        }
        vm.jumpInvoice = function (invoiceId) {
            $("#invoiceModal").modal("hide");
            setTimeout(function () {
                $state.go("finance.invoiceDetail", {invoiceId: invoiceId});
            }, 200);

        }

        //导出合同
        vm.downloadModelExcel = function () {
            $rootScope.commonDownload('/common/export/download/' + 5, {enLogin: user.enLogin});
        }
        vm.queryData();

        //下载模板
        vm.downTempleXml = function () {
            fiveBusinessContractLibraryService.downTempleXls(user.userLogin).then(function (response) {
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


        vm.statisticalData = function () {
            fiveBusinessContractLibraryService.statisticalData(user.userLogin).then(function (response) {
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
            url: '/business/contractLibrary/receive.do?userLogin=' + user.userLogin,
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
                    toastr.success("导入数据成功!<br>" + "修改：" + obj[1] + "条   新增：" + obj[0] + "条");
                    vm.loadPagedData();
                } else {
                    toastr.error(result.msg);
                }
            }
        });


        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            fiveBusinessContractLibraryService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'contractName', placeholder: '请输入合同名称..'},
                    2: {colName: 'contractNo', placeholder: '请输入合同号..'},
                    3: {colName: 'projectNo', placeholder: '请输入项目号..'},
                    4: {colName: 'signTime', placeholder: '请输入签约日期..'},
                    5: {colName: 'contractMoney', placeholder: '请输入合同额..'},
                    6: {colName: 'deptName', placeholder: '请输入承接部门..'},
                }, handleColumn: 14
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });
        return vm;

    })
    .controller("FiveBusinessContractLibraryDetailController", function ($state, $stateParams, $rootScope, $scope, commonCodeService, businessRecordService, businessCustomerService, fiveBusinessContractLibraryService,commonPrintTableService) {
        var vm = this;
        var contractLibraryId = $stateParams.contractLibraryId;
        var uiSref = "five.businessContractLibrary";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
            $rootScope.getTplConfig("","fiveBusinessContractLibrary_" + contractLibraryId, user.enLogin);
            $("#contractAttachUrl").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url: '/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator: user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star = file.name.lastIndexOf(".");
                    var fileType = file.name.substring(star, file.name.length);
                    if (".pdf,.png,.jpg,.bmp,.jpeg".indexOf(fileType) == -1) {
                        toastr.error('请上传.pdf,.png,.jpg,.bmp,.jpeg格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.contractAttachUrl = '/common/attach/download/' + result.data.id;
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        vm.loadData = function (loadProcess) {
            //分包情况
            fiveBusinessContractLibraryService.selectSubpackage(user.userLogin, "", contractLibraryId).then(function (value) {
                if (value.data.ret) {
                    vm.subpackages = value.data.data;
                }
            })
            //收入情况
            fiveBusinessContractLibraryService.selectIncome(user.userLogin, "", contractLibraryId).then(function (value) {
                if (value.data.ret) {
                    vm.incomes = value.data.data;
                }
            })
            //发票情况
            fiveBusinessContractLibraryService.selectInvoice(user.userLogin, "", contractLibraryId).then(function (value) {
                if (value.data.ret) {
                    vm.invoices = value.data.data;
                }
            })
            //补充情况
            fiveBusinessContractLibraryService.selectMainReviewByIds(user.userLogin, "", contractLibraryId).then(function (value) {
                if (value.data.ret) {
                    vm.mainReviews = value.data.data;
                }
            })

            fiveBusinessContractLibraryService.getModelById(contractLibraryId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        //$scope.loadProcessInstance(vm.item.processInstanceId);
                        $rootScope.getTplConfig("", vm.item.businessKey, user.userLogin);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#contractTime").datepicker('setDate', vm.item.contractTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessContractLibraryService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    fiveBusinessContractLibraryService.update(vm.item).then(function (value) {
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
        }
        vm.showCustomer = function (customerId) {
            if (customerId == 0) {
                toastr.error("请选择客户信息 或者 填写后点击保存客户信息！")
            } else {
                $state.go("five.businessCustomerDetail", {customerId: customerId});
            }

        }
        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.customerAddress = customer.address;
                vm.item.linkTitle = customer.linkTitle;
                vm.item.customerCode = customer.code;
                vm.item.customerId = customer.id;
            }
            vm.save();
        }
        vm.addCustomer = function (recordId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.customerName == '' || vm.item.customerName == null) {
                toastr.error("请先填写客户信息");
            } else {

                fiveBusinessContractReviewService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        fiveBusinessContractReviewService.addCustomer(user.userLogin, recordId).then(function (value) {
                            if (value.data.ret) {
                                businessCustomerService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.customerId = value.data.data.id;
                                        vm.item.customerName = value.data.data.name;
                                        vm.save();
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                    }
                                })
                            }
                        });
                    }
                })
            }
        }

        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择承接部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.deptId + ",",
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

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'reviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参加评审人员",
                    type: "部门", deptIds: "1", userLoginList: vm.item.reviewUser, multiple: true
                });
            }
            if (vm.status == 'totalDesigner') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择总设计师人员",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.totalDesigner,
                    multiple: false,
                    parentDeptId: user.deptId
                });
            }
            if (vm.status == 'projectManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.projectManager,
                    multiple: false,
                    parentDeptId: user.deptId
                });
            }
            if (vm.status == 'businessChargeLeader') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择合同分管副总",
                    type: "人员",
                    userLogins: "3996,2192,0166,2873",
                    userLoginList: vm.item.businessChargeLeader,
                    multiple: false,
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'reviewUser') {
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'totalDesigner') {
                vm.item.totalDesigner = $scope.selectedOaUserLogins_;
                vm.item.totalDesignerName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'projectManager') {
                vm.item.projectManager = $scope.selectedOaUserLogins_;
                vm.item.projectManagerName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'businessChargeLeader') {
                vm.item.businessChargeLeader = $scope.selectedOaUserLogins_;
                vm.item.businessChargeLeaderName = $scope.selectedOaUserNames_;
            }
        };

        vm.jumpSubpackage = function (subpackageId) {
            $("#subpackageModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessSubpackageDetail", {subpackageId: subpackageId});
            }, 200);

        }
        vm.jumpIncome = function (incomeId) {
            $("#incomeModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessIncomeDetail", {incomeId: incomeId});
            }, 200);

        }
        vm.jumpMainReview = function (mainReviewId) {
            $("#mainReviewModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessContractReviewDetail", {contractReviewId: mainReviewId});
            }, 200);

        }
        vm.jumpInvoice = function (invoiceId) {
            $("#invoiceModal").modal("hide");
            setTimeout(function () {
                $state.go("finance.invoiceDetail", {invoiceId: invoiceId});
            }, 200);

        }

        //项目信息备案
        vm.showRecordModal = function () {
            businessRecordService.listRecordByUserLogin(user.userLogin, 0).then(function (value) {
                if (value.data.ret) {
                    vm.listRecord = value.data.data;
                    singleCheckBox(".cb_record", "data-name");
                }
            })
            vm.showChooseStatus = '项目信息备案-' + vm.item.projectName;
            $("#selectRecordModal").modal("show");
        }
        vm.saveSelectRecordModel = function () {
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                //需要同步到 合同库 的数据
                vm.item.recordId = record.id;
                //备案项目名称
                vm.item.recordProjectName = record.projectName;
                //项目名称
                vm.item.projectName = record.projectName;
                //建设规模
                vm.item.constructionScale = record.projectScale;
                //投资额
                vm.item.investMoney = record.projectInvest;
                //合同额
                vm.item.contractMoney = "";
                //建设地址
                vm.item.constructionArea = record.projectAddress;
                //投资来源
                vm.item.investSource = record.investSource;
                //项目性质
                vm.item.projectNature = record.projectType;
                //项目号
                vm.item.projectNo = record.projectNo;
                //客户信息
                vm.item.customerId = record.customerId;
                vm.item.customerCode = record.customerCode;
                vm.item.customerName = record.customerName;
                vm.item.customerAddress = record.customerAddress;
                vm.item.linkMan = record.linkMan;
                vm.item.linkTel = record.linkTel;
                vm.item.linkTitle = record.linkTitle;
            }
            vm.save();
            $("#selectRecordModal").modal("hide");
        }

        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: 0,
                uiSref: uiSref,
                multiple: false,
            });
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("合同库");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $rootScope.saveSelectLibrary_ = function () {
            //需要的数据
            vm.item.mainContractLibraryId = $rootScope.selectedLibrary.id;
            vm.item.mainContractLibraryName = $rootScope.selectedLibrary.contractName;
            vm.item.mainContractLibraryNo = $rootScope.selectedLibrary.contractNo;
            vm.item.projectNo = $rootScope.selectedLibrary.projectNo;
            //客户信息
            vm.item.customerId = $rootScope.selectedLibrary.customerId;
            vm.item.customerCode = $rootScope.selectedLibrary.customerCode;
            vm.item.customerName = $rootScope.selectedLibrary.customerName;
            vm.item.customerAddress = $rootScope.selectedLibrary.customerAddress;
            vm.item.linkMan = $rootScope.selectedLibrary.linkMan;
            vm.item.linkTel = $rootScope.selectedLibrary.linkTel;
            vm.item.linkTitle = $rootScope.selectedLibrary.linkTitle;
            $scope.closeLibrarySelectModal_();
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //分包合同库
    .controller("FiveBusinessContractLibrarySubpackageController", function ($state, $scope, $rootScope, fiveBusinessContractLibrarySubpackageService) {
        var vm = this;
        vm.params = {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0};
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessContractLibrarySubpackage";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            });
            fiveBusinessContractLibrarySubpackageService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })

        };


        vm.show = function (id) {
            $state.go("five.businessContractLibrarySubpackageDetail", {contractLibrarySubpackageId: id});
        }
        vm.showNew = function (id) {
            if (vm.opt == 1) {
                $state.go("five.businessPreContractDetail", {preContractId: id});
            } else {
                $state.go("five.businessContractReviewDetail", {contractReviewId: id});
            }
        }
        vm.showEd = function (contractId) {
            $state.go("business.contractDetail", {contractId: contractId});
        }

        vm.add = function () {
            fiveBusinessContractLibrarySubpackageService.getNewModel(user.userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessContractLibrarySubpackageService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }
        //合同库
        vm.showSubpackageModal = function (librarySubpackageId) {
            fiveBusinessContractLibrarySubpackageService.selectSubpackage(user.userLogin, "", librarySubpackageId).then(function (value) {
                if (value.data.ret) {
                    vm.subpackages = value.data.data;
                }
            })
            $("#subpackageModal").modal("show");
        };
        vm.jumpSubpackage = function (subpackageId) {
            $("#subpackageModal").modal("hide");
            setTimeout(function () {
                $state.go("five.businessSubpackageDetail", {subpackageId: subpackageId});
            }, 200);

        }

        //导出合同
        vm.downloadModelExcel = function (type) {
            if (type == 'purchase') {
                $rootScope.commonDownload('/common/export/download/' + 13, {enLogin: user.enLogin});
            } else {
                $rootScope.commonDownload('/common/export/download/' + 12, {enLogin: user.enLogin});
            }

        }
        vm.queryData();

        vm.downTempleXml = function () {
            fiveBusinessContractLibrarySubpackageService.downTempleXls(user.userLogin).then(function (response) {
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
            url: '/business/contractLibrarySubpackage/receive.do?userLogin=' + user.userLogin,
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
                    var obj = result.data.split(",");
                    toastr.success("导入数据成功!<br>" + "修改：" + obj[1] + "条   新增：" + obj[0] + "条");
                    vm.loadPagedData();
                } else {
                    toastr.error(result.msg);
                }
            }
        });

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params.uiSref = uiSref;
            params.userLogin = user.userLogin;
            fiveBusinessContractLibrarySubpackageService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'subContractName', placeholder: '请输入合同名称..'},
                    2: {colName: 'subContractNo', placeholder: '请输入合同号..'},
                    3: {colName: 'supplierName', placeholder: '请输入供方名称..'},
                    4: {colName: 'deptName', placeholder: '请输入部门名称..'},
                    5: {colName: 'contractNo', placeholder: '请输入主合同号..'},
                    6: {colName: 'subContractMoney', placeholder: '请输入合同额..'},
                }, handleColumn: 11
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;

    })
    .controller("FiveBusinessContractLibrarySubpackageDetailController", function ($state, $stateParams, $rootScope, $scope, businessCustomerService, fiveBusinessContractLibrarySubpackageService,commonPrintTableService) {
        var vm = this;
        var contractLibrarySubpackageId = $stateParams.contractLibrarySubpackageId;
        var uiSref = "five.businessContractLibrarySubpackage";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
            $rootScope.getTplConfig("fiveBusinessContractLibrarySubpackage_" + contractLibrarySubpackageId, user.enLogin);
        }

        vm.loadData = function (loadProcess) {
            fiveBusinessContractLibrarySubpackageService.getModelById(contractLibrarySubpackageId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $rootScope.getTplConfig("", vm.item.businessKey, user.userLogin);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#contractTime").datepicker('setDate', vm.item.contractTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessContractLibrarySubpackageService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    fiveBusinessContractLibrarySubpackageService.update(vm.item).then(function (value) {
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
        }
        vm.showCustomer = function (customerId) {
            if (customerId == 0) {
                toastr.error("请选择客户信息 或者 填写后点击保存客户信息！")
            } else {
                $state.go("five.businessCustomerDetail", {customerId: customerId});
            }

        }
        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.customerAddress = customer.address;
                vm.item.linkTitle = customer.linkTitle;
                vm.item.customerCode = customer.code;
                vm.item.customerId = customer.id;
            }
            vm.save();
        }
        vm.addCustomer = function (recordId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.customerName == '' || vm.item.customerName == null) {
                toastr.error("请先填写客户信息");
            } else {

                fiveBusinessContractReviewService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        fiveBusinessContractReviewService.addCustomer(user.userLogin, recordId).then(function (value) {
                            if (value.data.ret) {
                                businessCustomerService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.customerId = value.data.data.id;
                                        vm.item.customerName = value.data.data.name;
                                        vm.save();
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                    }
                                })
                            }
                        });
                    }
                })
            }
        }

        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择发包部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            } else {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择对内分包部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.inDeptId,
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'deptId') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            } else {
                vm.item.inDeptName = $scope.selectedOaDeptNames_;
                vm.item.inDeptId = Number($scope.selectedOaDeptIds_);
            }

        }


        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'contractChargeLeader') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择合同分管副总",
                    type: "人员",
                    userLogins: "3996,2192,0166,2873,2996",
                    userLoginList: vm.item.businessChargeLeader,
                    multiple: false,
                });
            } else if (vm.status == 'reviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参加评审人员",
                    type: "部门", deptIds: "1", userLoginList: vm.item.reviewUser, multiple: true
                });
            } else if (vm.status == 'deptReviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择院级参加评审人员",
                    type: "部门",
                    deptIds: '1',
                    parentDeptId: vm.item.headDeptId,
                    userLoginList: vm.item.deptReviewUser,
                    multiple: true
                });
            } else if (vm.status == 'deptChargeMen') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门领导人员",
                    type: "部门",
                    deptIds: "1",
                    parentDeptId: vm.item.deptId,
                    userLoginList: vm.item.deptChargeMen,
                    multiple: true
                });
            }

        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'contractChargeLeader') {
                vm.item.contractChargeLeader = $scope.selectedOaUserLogins_;
                vm.item.contractChargeLeaderName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'reviewUser') {
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'deptReviewUser') {
                vm.item.deptReviewUser = $scope.selectedOaUserLogins_;
                vm.item.deptReviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'deptChargeMen') {
                vm.item.deptChargeMen = $scope.selectedOaUserLogins_;
                vm.item.deptChargeMenName = $scope.selectedOaUserNames_;
            }
        };


        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: 0,
                uiSref: uiSref,
                multiple: false,
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            //需要同步的数据
            vm.item.contractName = $rootScope.selectedLibrary.projectName;
            vm.item.contractLibraryId = $rootScope.selectedLibrary.id;
            vm.item.contractNo = $rootScope.selectedLibrary.contractNo;
            vm.item.contractMoney = $rootScope.selectedLibrary.contractMoney;
            vm.item.contractType = $rootScope.selectedLibrary.contractType;
            vm.item.projectNature = $rootScope.selectedLibrary.projectNature;
            $scope.closeLibrarySelectModal_();
        }

        vm.showReview = function (id,purchase) {
            if(purchase){
                $state.go("five.businessPurchaseDetail", {purchaseId: id});
            }else{
                $state.go("five.businessSubpackageDetail", {subpackageId: id});
            }

        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("分包/采购合同库");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;

    })
    //项目启动
    .controller("BusinessContractController", function ($state, $scope,$rootScope, businessContractService) {
        var vm = this;
        var uiSref = "business.contract";
        var tableName = $rootScope.loadTableName(uiSref);

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", searchTime: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});

        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: 10, total: vm.params.total};

        vm.init = function () {
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.queryData = function () {
            vm.searchTime = '';
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.showStatisticByUserLogin = function () {
            businessContractService.statisticByUserLogin(user.userLogin, vm.params.searchTime).then(function (value) {
                if (value.data.ret) {
                    vm.contractMap = value.data.data;
                    $("#statisticContract").modal("show");
                }
            })
        }

        vm.loadPagedData = function () {
            businessContractService.loadPagedData(
                {
                    q: vm.params.qName,
                    userLogin: user.userLogin,
                    pageNum: vm.pageInfo.pageNum,
                    pageSize: vm.pageInfo.pageSize,
                    uiSref: uiSref
                }
            ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })

        }

        vm.downloadModelExcel = function () {
            businessContractService.downloadModelExcel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            })
        }

        vm.show = function (contractId) {
            $state.go("business.contractDetail", {contractId: contractId});
        }
        vm.showEd = function (item) {
            $state.go("five.detail.show", {id: item.id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessContractService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessContractService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.downloadModel = function () {
            businessContractService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        $("#uploadModelFile").fileupload({
            dataType: 'text',
            url: '/business/contract/receive.do?userLogin=' + user.userLogin,
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
                    var obj = result.msg.split(",");
                    toastr.success("导入数据成功!<br>" + "修改：" + obj[0] + "条   新增：" + obj[1] + "条");
                    vm.loadPagedData();
                } else {
                    toastr.error(result.msg);
                }
            }
        });

        vm.init();

        return vm;
    })
    .controller("BusinessContractDetailController", function ($rootScope, $state, $stateParams, $scope, edProjectTreeService, fiveBusinessContractLibraryService, commonCodeService, fiveBusinessContractReviewService, businessPreContractService, hrDeptService, businessContractService, sysDeptService, businessCustomerService, hrEmployeeService,commonPrintTableService) {
        var vm = this;
        var contractId = $stateParams.contractId;
        var uiSref = "business.contract";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.params = {name: "", qName: "", qCName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: 10};

        vm.init = function () {
            vm.loadData(true);
            $scope.loadRightData(user.userLogin, "business.contract");
        }

        vm.loadData = function (loadProcess) {
            businessContractService.getModelById(contractId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (vm.item.projectCity == null) {
                        vm.provinceId = "0";
                        vm.cityId = "0";
                    } else {
                        vm.provinceId = "" + provinceArr.indexOf(vm.item.projectProvince);
                        vm.cityId = "" + cityArr[vm.provinceId].indexOf(vm.item.projectCity);
                    }
                    vm.getCityArr = vm.cityArr[vm.provinceId]; //根据索引写入城市数据
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#signDate").datepicker('setDate', vm.item.signDate);
                    $("#stampTime").datepicker('setDate', vm.item.stampTime);
                    $("#backTime").datepicker('setDate', vm.item.backTime);
                }
            })
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData();
                }
            })
        };
        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessContractService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        if (vm.item.recordNo == '' && $rootScope.processInstance.myRunningTaskName.indexOf('档案人员') >= 0) {
                            toastr.warning("请填写档案号！");
                            return;
                        }
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

        vm.showStageModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "设计评审阶段").then(function (value) {
                vm.stageNames = value.data.data;
                $("#contractStageNameModel").modal("show");
            })
        }
        vm.selectStageName = function () {
            var stageNames = [];
            $(".cb_stage:checked").each(function () {
                stageNames.push($(this).attr("name"));
            });
            vm.item.stageNames = stageNames.join(',');
            $("#contractStageNameModel").modal("hide");
        }
        vm.saveDept = function () {
            if (vm.opt == '所属部门') {
                vm.item.deptId = dept.id;
                vm.item.deptName = dept.text;
            } else if (vm.opt == '合作部门') {
                vm.item.deptIds = dept.id;
                vm.item.deptNames = dept.text;
            }
            $("#deptSelectModal").modal("hide");
        }

        vm.showDeptModal = function () {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择所属部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.deptId + ",",
                    multiple: false
                });
            }
            if (vm.opt == 'deptIds') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择合作部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: "," + vm.item.deptIds + ",",
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'deptId') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (vm.opt == 'deptIds') {
                vm.item.deptNames = $scope.selectedOaDeptNames_;
                vm.item.deptIds = $scope.selectedOaDeptIds_;
            }
        }

        vm.showCooperDeptModal = function (ids) {

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
                    if (ids.indexOf("," + item.id + ",") >= 0) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#cooper_dept_select_tree').jstree("destroy");
                $('#cooper_dept_select_tree')
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        },
                        "checkbox": {
                            "keep_selected_style": false
                        },
                        "plugins": ["wholerow", "checkbox"]
                    });
            });
            $("#cooperDeptSelectModal").modal("show");
        }
        vm.saveCooperDept = function () {
            var deptList = $('#cooper_dept_select_tree').jstree(true).get_selected(true);
            var ids = [];
            var names = [];
            for (var i = 0; i < deptList.length; i++) {
                ids.push(deptList[i].id)
                names.push(deptList[i].text)
            }
            vm.item.deptIds = "," + ids.join(",") + ",";
            vm.item.deptNames = names.join(",");
            $("#cooperDeptSelectModal").modal("hide");
        }

        vm.showUserModel = function () {
            if (vm.opt == "chargeMen") {
                $scope.showOaSelectEmployeeModal_({
                    title: "项目负责人选择",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.chargeMen,
                    multiple: true
                });
                /*                $scope.showOaSelectEmployeeModal_({title:"项目负责人选择", type:"角色",roleIds:"0",parentRoleId:"18",userLoginList: vm.item.chargeMen,multiple:true});*/
            } else if (vm.opt == 'exeChargeMen') {
                $scope.showOaSelectEmployeeModal_({
                    title: "执行负责人选择",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.exeChargeMen,
                    multiple: true
                });
            } else if (vm.opt == 'businessManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "项目经理选择",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.businessManager,
                    multiple: true
                });
            } else if (vm.opt == 'totalDesigner') {
                $scope.showOaSelectEmployeeModal_({
                    title: "项目总师选择",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.optUser,
                    multiple: false
                });
            } else if (vm.opt == 'otherDesigner') {
                $scope.showOaSelectEmployeeModal_({
                    title: "项目其他总师选择",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.optUser,
                    multiple: true
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == "chargeMen") {
                vm.item.chargeMen = $scope.selectedOaUserLogins_;
                vm.item.chargeMenName = $scope.selectedOaUserNames_;

            } else if (vm.opt == 'exeChargeMen') {
                vm.item.exeChargeMen = $scope.selectedOaUserLogins_;
                vm.item.exeChargeMenName = $scope.selectedOaUserNames_;
            } else if (vm.opt == 'businessManager') {
                vm.item.businessManager = $scope.selectedOaUserLogins_;
                vm.item.businessManagerName = $scope.selectedOaUserNames_;
            } else if (vm.opt == 'totalDesigner') {
                vm.item.businessContractDetail.totalDesigner = $scope.selectedOaUserLogins_;
                vm.item.businessContractDetail.totalDesignerName = $scope.selectedOaUserNames_;
            } else if (vm.opt == 'otherDesigner') {
                vm.item.businessContractDetail.otherDesigner = $scope.selectedOaUserLogins_;
                vm.item.businessContractDetail.otherDesignerName = $scope.selectedOaUserNames_;
            }

        };

        vm.selectUser = function () {
            var logins = [];
            var names = []
            $(".cb_employee:checked").each(function () {
                var info = $(this).attr("data-name");
                logins.push(info.split('_')[0]);
                names.push(info.split('_')[1]);
            });
            if (vm.opt == "chargeMen") {
                vm.item.chargeMen = logins.join(",");
                vm.item.chargeMenName = names.join(",");
            } else if (vm.opt == 'exeChargeMen') {
                vm.item.exeChargeMen = logins.join(",");
                vm.item.exeChargeMenName = names.join(",");
            }
            $("#selectEmployeeModal").modal("hide");
        }

        vm.showCustomerModal = function () {
            businessCustomerService.selectAll(user.userLogin, "business.customer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerId = customer.id;
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.linkFax = customer.linkFax;
                vm.item.linkMail = customer.linkMail;
                vm.item.depositBank = customer.depositBank;
                vm.item.bankAccount = customer.bankAccount;
                vm.item.taxType = customer.taxType;
                vm.item.taxNo = customer.taxNo;
                vm.item.customerType = customer.customerType;
            }
        }

        //合同库
        /*        vm.showSelectPreOrReviewModal = function () {
                    fiveBusinessContractLibraryService.selectNotHaveContract(vm.item.contractLibraryId,user.userLogin).then(function (value) {
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
                        //需要从 合同库 中同步到 项目启用的数据
                        vm.item.projectName = library.projectName;
                        //承接部门
                        vm.item.deptId = library.deptId;
                        vm.item.deptName = library.deptName;
                        //投资额
                        vm.item.investMoney = library.investMoney;
                        //项目号
                        vm.item.businessContractDetail.projectNo = library.projectNo;
                        //合同号
                        vm.item.contractNo = library.contractNo;
                        //建设地址
                        vm.item.projectAddress = library.constructionArea;
                        //客户名称
                        vm.item.customerName = library.customerName;
                        //客户联系人
                        vm.item.linkMan = library.linkMan;
                        //联系电话
                        vm.item.linkTel = library.linkTel;
                        //联系人职务
                        vm.item.linkFax = library.linkTitle;
                        //项目总师
                        vm.item.businessContractDetail.totalDesignerName = library.totalDesignerName;
                        vm.item.businessContractDetail.totalDesigner = library.totalDesigner;
                        //项目经理
                        vm.item.businessManager = library.projectManager;
                        vm.item.businessManagerName = library.projectManagerName;

                        vm.item.contractLibraryId = library.id;
                    }
                    vm.save();
                    $("#selectLibraryModal").modal("hide");
                }*/
        vm.showSelectPreOrReviewModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: vm.item.contractLibraryId,
                uiSref: uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            //需要从 合同库 中同步到 项目启用的数据
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            //承接部门
            vm.item.deptId = $rootScope.selectedLibrary.deptId;
            vm.item.deptName = $rootScope.selectedLibrary.deptName;
            //投资额
            vm.item.investMoney = $rootScope.selectedLibrary.investMoney;
            //项目号
            vm.item.businessContractDetail.projectNo = $rootScope.selectedLibrary.projectNo;
            //合同号
            vm.item.contractNo = $rootScope.selectedLibrary.contractNo;
            //建设地址
            vm.item.projectAddress = $rootScope.selectedLibrary.constructionArea;
            //客户名称
            vm.item.customerId = $rootScope.selectedLibrary.customerId;
            //客户名称
            vm.item.customerName = $rootScope.selectedLibrary.customerName;
            //客户联系人
            vm.item.linkMan = $rootScope.selectedLibrary.linkMan;
            //联系电话
            vm.item.linkTel = $rootScope.selectedLibrary.linkTel;
            //联系人职务
            vm.item.linkFax = $rootScope.selectedLibrary.linkTitle;
            //项目总师
            vm.item.businessContractDetail.totalDesignerName = $rootScope.selectedLibrary.totalDesignerName;
            vm.item.businessContractDetail.totalDesigner = $rootScope.selectedLibrary.totalDesigner;
            //项目经理
            vm.item.businessManager = $rootScope.selectedLibrary.projectManager;
            vm.item.businessManagerName = $rootScope.selectedLibrary.projectManagerName;

            vm.item.contractLibraryId = $rootScope.selectedLibrary.id;
            $scope.closeLibrarySelectModal_();
            vm.save();
        }

        vm.saveSelectPreContract = function () {
            if ($(".cb_preContract:checked").length > 0) {
                var preContract = $.parseJSON($(".cb_preContract:checked").first().attr("data-name"));
                //需要从 超前任务单 中同步到 项目启用的数据
                vm.item.projectName = preContract.projectName;
                vm.item.deptId = preContract.deptId;
                vm.item.deptName = preContract.deptName;
                vm.item.contractMoney = preContract.investMoney;
                vm.item.contractId = preContract.contractId;
                vm.item.projectNo = preContract.projectNo;
                vm.item.preContractId = preContract.id;
            }
            vm.save();
        }

        vm.saveSelectReview = function () {
            if ($(".cb_review:checked").length > 0) {
                var review = $.parseJSON($(".cb_review:checked").first().attr("data-name"));
                //需要从合同评审 同步到 项目启动的数据
                vm.item.recordId = review.id;
                vm.item.projectName = review.projectName;
                vm.item.customerId = review.customerId;
                vm.item.customerName = review.customerName;
                vm.item.agency = review.linkMan;
                vm.item.contractReviewId = review.id;
            }
            vm.save();
        }

        vm.chooseBidContract = function () {
            var bidContract = '';
            $(".cb_bidContract:checked").each(function () {
                bidContract = $.parseJSON($(this).attr("data-name"));
                vm.item.bidContractId = bidContract.id;
                vm.item.projectType = bidContract.projectType;
                vm.item.projectName = bidContract.projectName;
                vm.item.contractMoney = bidContract.contractMoney;
                vm.item.projectAddress = bidContract.projectAddress;
                vm.item.contractType = bidContract.contractType;
            });
            $("#bidContract").modal("hide");
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("项目启动");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        //先保存再获取生成的合同号
        vm.refreshContractNo = function () {

            vm.item.operateUserLogin = user.userLogin;
            businessContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    businessContractService.getNewContractNo(vm.item.id).then(function (value) {
                        vm.item.contractNo = value.data.data;
                    })
                }
            })
        }

        vm.provinceArr = provinceArr; //省份数据
        vm.cityArr = cityArr;    //城市数据
        vm.getCityArr = vm.cityArr[0]; //默认为省份
        vm.getCityIndexArr = ['0', '0']; //这个是索引数组，根据切换得出切换的索引就可以得到省份及城市
        //改变省份触发的事件 [根据索引更改城市数据]
        vm.provinceChange = function (index) {
            vm.getCityArr = vm.cityArr[index]; //根据索引写入城市数据
            vm.getCityIndexArr[1] = '0'; //清除残留的数据
            vm.getCityIndexArr[0] = index;
            vm.item.projectProvince = vm.provinceArr[index];
        }
        //改变城市触发的事件
        vm.cityChange = function (index) {
            vm.item.projectCity = vm.getCityArr[index];
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;
    })

    //超前任务单
    .controller("FiveBusinessPreContractController", function ($state, $stateParams, $rootScope, $scope, businessPreContractService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessPreContract";
        var tableName = $rootScope.loadTableName(uiSref);


        vm.init = function () {
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            businessPreContractService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })

        }

        vm.show = function (preContractId) {
            $state.go("five.businessPreContractDetail", {preContractId: preContractId});
        }

        vm.showEd = function (contractId) {
            $state.go("business.contractDetail", {contractId: contractId});
        }

        vm.showReviewContract = function (contractReviewId) {
            $state.go("five.businessContractReviewDetail", {contractReviewId: contractReviewId});
        }
        vm.addReviewContract = function (preContract) {
            if (!preContract.processEnd) {
                toastr.warning("请先完成该流程!");
                return;
            } else {
                businessPreContractService.addReviewContract(preContract.id).then(function (value) {
                    if (value.data.ret) {
                        var contractReviewId = value.data.data;
                        $state.go("five.businessContractReviewDetail", {contractReviewId: contractReviewId});
                    }
                })
            }
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessPreContractService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessPreContractService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBusinessPreContractDetailController", function ($state, $rootScope, $stateParams, $scope, businessContractService, commonCodeService, businessCustomerService, hrDeptService, businessPreContractService, businessRecordService,commonPrintTableService) {
        var vm = this;
        var preContractId = $stateParams.preContractId;
        var uiSref = "five.businessPreContract";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.params = {name: "", qName: "", qCName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: 10};
        vm.optNum = 0;

        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.projectNo == "") {
                toastr.error("请先填写 项目号");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessPreContractService.update(vm.item).then(function (value) {
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
        vm.init = function () {
            vm.loadData(true);
            $scope.loadRightData(user.userLogin, "five.businessPreContract");
        }

        vm.loadData = function (loadProcess) {
            businessPreContractService.getModelById(preContractId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#arrangeEndTime").datepicker('setDate', vm.item.arrangeEndTime);
                    $("#planEndTime").datepicker('setDate', vm.item.planEndTime);
                    $("#planBeginTime").datepicker('setDate', vm.item.planBeginTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessPreContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //项目编号
        vm.getProjectNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessPreContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    businessPreContractService.getProjectNo(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("项目号已更新!");
                        }
                    })
                }
            })
        }
        vm.showDeptModal = function () {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择承接部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: "," + vm.item.deptId + ",",
                multiple: false
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }
        //无评审合同备案
       /* vm.showRecordModal = function () {
            businessRecordService.listRecordByUserLogin(user.userLogin, vm.item.recordId).then(function (value) {
                if (value.data.ret) {
                    vm.listRecord = value.data.data;
                    singleCheckBox(".cb_record", "data-name");
                    $("#selectRecordModal").modal("show");
                }
            })
        };
        vm.saveSelectRecord = function () {
            $("#selectRecordModal").modal("hide");
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                //需要从备案传递到 超前任务单的 数据
                vm.item.recordId = record.id;
                //项目名称
                vm.item.projectName = record.projectName;
                vm.item.recordProjectName = record.projectName;
                //建设规模
                vm.item.constructionArea = record.projectScale;
                //投资额
                vm.item.investMoney = record.projectInvest;
                //建设地址
                vm.item.projectAddress = record.projectAddress;
                //投资来源
                vm.item.investSource = record.investSource;
                //项目性质
                vm.item.projectNature = record.projectType;
                //项目号
                vm.item.projectNo = record.projectNo;
                //计划开始时间
                vm.item.planBeginTime = record.planBeginTime;
                //计划结束时间
                vm.item.planEndTime = record.planEndTime;
                //客户信息
                vm.item.customerId = record.customerId;
                vm.item.customerCode = record.customerCode;
                vm.item.customerName = record.customerName;
                vm.item.customerAddress = record.customerAddress;
                vm.item.linkMan = record.linkMan;
                vm.item.linkTel = record.linkTel;
                vm.item.linkTitle = record.linkTitle;
                vm.save();
            }
        }*/

        //项目信息备案
        vm.showRecordModal = function () {
            $scope.showRecordSelectModal_({
                selectRecordId: vm.item.recordId,
                uiSref: uiSref,
                multiple: false
            });
        }
        $rootScope.saveSelectRecord_ = function () {
            //需要从备案传递到 超前任务单的 数据
            vm.item.recordId = $rootScope.selectedRecord.id;
            //项目名称
            vm.item.projectName = $rootScope.selectedRecord.projectName;
            vm.item.recordProjectName = $rootScope.selectedRecord.projectName;
            //建设规模
            vm.item.constructionArea = $rootScope.selectedRecord.projectScale;
            //投资额
            vm.item.investMoney = $rootScope.selectedRecord.projectInvest;
            //建设地址
            vm.item.projectAddress = $rootScope.selectedRecord.projectAddress;
            //投资来源
            vm.item.investSource = $rootScope.selectedRecord.investSource;
            //项目性质
            vm.item.projectNature = $rootScope.selectedRecord.projectType;
            //项目号
            vm.item.projectNo = $rootScope.selectedRecord.projectNo;
            //计划开始时间
            vm.item.planBeginTime = $rootScope.selectedRecord.planBeginTime;
            //计划结束时间
            vm.item.planEndTime = $rootScope.selectedRecord.planEndTime;
            //客户信息
            vm.item.customerId = $rootScope.selectedRecord.customerId;
            vm.item.customerCode = $rootScope.selectedRecord.customerCode;
            vm.item.customerName = $rootScope.selectedRecord.customerName;
            vm.item.customerAddress = $rootScope.selectedRecord.customerAddress;
            vm.item.linkMan = $rootScope.selectedRecord.linkMan;
            vm.item.linkTel = $rootScope.selectedRecord.linkTel;
            vm.item.linkTitle = $rootScope.selectedRecord.linkTitle;
            $scope.closeRecordSelectModal_();
            vm.save();
        }

        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    businessPreContractService.update(vm.item).then(function (value) {
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

        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'totalDesigner') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择总设计师人员",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.totalDesigner,
                    multiple: false,
                    parentDeptId: user.deptId
                });
            } else if (vm.status == 'projectManager') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.projectManager,
                    multiple: false,
                    parentDeptId: user.deptId
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'totalDesigner') {
                vm.item.totalDesigner = $scope.selectedOaUserLogins_;
                vm.item.totalDesignerName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'projectManager') {
                vm.item.projectManager = $scope.selectedOaUserLogins_;
                vm.item.projectManagerName = $scope.selectedOaUserNames_;
            }
            vm.save();
        };

        //客户信息
        vm.showCustomerModal = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerName = customer.name;
                vm.item.customerId = customer.id;
            }
        }

        vm.showSelectMajorListModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "设计专业").then(function (value) {
                vm.majorNames = value.data.data;
            });
            $("#selectMajorListModal").modal("show");
        }

        vm.saveMajorList = function () {
            var majors = [];
            $(".cb_major:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            $("#selectMajorListModal").modal("hide");

            vm.item.designMajor = majors.join(',');
        }

        vm.showSelectPreDescModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "非常规涉及条件情形的说明").then(function (value) {
                vm.preDescs = value.data.data;

            });
            $("#selectPreDescModal").modal("show");

        }

        vm.savePreDesc = function () {
            var preDescs = [];
            $(".cb_preDesc:checked").each(function () {
                preDescs.push($(this).attr("data-name"));
            });
            $("#selectPreDescModal").modal("hide");
            vm.item.preDesc = preDescs.join(',');
        }

        vm.showSelectPreConditionModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "应对措施及具备的条件").then(function (value) {
                vm.preConditions = value.data.data;
            });
            $("#selectPreConditionModal").modal("show");
        }

        vm.savePreCondition = function () {
            var preConditions = [];
            $(".cb_preCondition:checked").each(function () {
                preConditions.push($(this).attr("data-name"));
            });
            $("#selectPreConditionModal").modal("hide");
            vm.item.preCondition = preConditions.join(',');
        }

        vm.showSelectStageModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "设计评审阶段").then(function (value) {
                vm.stages = value.data.data;
            });
            $("#selectStageModal").modal("show");
        }

        vm.saveStage = function () {
            var stages = [];
            $(".cb_stage:checked").each(function () {
                stages.push($(this).attr("data-name"));
            });
            $("#selectStageModal").modal("hide");
            vm.item.stageName = stages.join(',');
        }

        vm.showCustomer = function (customerId) {
            if (customerId == 0) {
                toastr.error("请选择客户信息 或者 填写后点击保存客户信息！")
            } else {
                $state.go("five.businessCustomerDetail", {customerId: customerId});
            }
        }
        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.customerAddress = customer.address;
                vm.item.linkTitle = customer.linkTitle;
                vm.item.customerCode = customer.code;
                vm.item.customerId = customer.id;
            }
            vm.save();
        }
        vm.addCustomer = function (recordId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.customerName == '' || vm.item.customerName == null) {
                toastr.error("请先填写客户信息");
            } else {
                businessPreContractService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        businessPreContractService.addCustomer(user.userLogin, recordId).then(function (value) {
                            if (value.data.ret) {
                                businessCustomerService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.customerId = value.data.data.id;
                                        vm.item.customerName = value.data.data.name;
                                        vm.save();
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                    }
                                })
                            }
                        });
                    }
                })
            }
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("信息化设备验收(多台)审批");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;
    })
    //合同补录
    .controller("FiveBusinessInputContractController", function ($state, $stateParams, $rootScope, $scope, businessInputContractService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", qDeptName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessInputContract";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin, uiSref);
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };


        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                qDeptName: vm.params.qDeptName,
                userLogin: user.userLogin,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref
            };
            businessInputContractService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })

        }

        vm.show = function (inputContractId) {
            $state.go("five.businessInputContractDetail", {inputContractId: inputContractId});
        }

        vm.showEd = function (contractId) {
            $state.go("five.detail.show", {id: contractId});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessInputContractService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessInputContractService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBusinessInputContractDetailController", function ($state, $rootScope, $stateParams, $scope, businessContractService, commonCodeService, businessPreContractService, businessCustomerService, hrDeptService, businessInputContractService, businessRecordService) {
        var vm = this;
        var inputContractId = $stateParams.inputContractId;
        vm.params = {name: "", qName: "", qCName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: 10};
        vm.optNum = 0;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            vm.loadData(true);
            $scope.loadRightData(user.userLogin, "five.businessInputContract");
            commonCodeService.listDataByCatalog(user.userLogin, "项目专业分类一级").then(function (value) {
                if (value.data.ret) {
                    vm.projectMajorTypeFirst = value.data.data;

                }
            })
            commonCodeService.listDataByCatalog(user.userLogin, "项目专业分类二级").then(function (value) {
                if (value.data.ret) {
                    vm.projectMajorTypeSecond = value.data.data;

                }
            })
            commonCodeService.listDataByCatalog(user.userLogin, "项目专业分类三级").then(function (value) {
                if (value.data.ret) {
                    vm.projectMajorTypeThird = value.data.data;

                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessInputContractService.update(vm.item).then(function (value) {
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
        //合同编号
        vm.getContractNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            vm.contract.operateUserLogin = user.userLogin;

            businessInputContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    businessInputContractService.getContractNo(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("合同号已更新!");
                        }
                    })
                }
            })
        }
        //项目编号
        vm.getProjectNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            vm.contract.operateUserLogin = user.userLogin;

            businessInputContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    businessInputContractService.getProjectNo(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("项目号已更新!");
                        }
                    })
                }
            })

        }

        // 超前任务单
        vm.showPreContractModal = function () {
            businessPreContractService.selectNotHaveInput(vm.item.preId).then(function (value) {
                vm.preContracts = value.data.data;
                singleCheckBox(".cb_preContract", "data-name");
            });
            vm.showChooseStatus = '超前任务单-' + vm.item.projectName;
            $("#selectPreContractModal").modal("show");
        };

        vm.saveSelectModel = function () {
            if ($(".cb_preContract:checked").length > 0) {
                var preContract = $.parseJSON($(".cb_preContract:checked").first().attr("data-name"));
                //需要从超前任务单 同步到合同补录的数据
                vm.item.preId = preContract.id;
                vm.item.projectName = preContract.projectName;
                vm.item.deptId = preContract.deptId;
                vm.item.deptName = preContract.deptName;
                vm.item.contractMoney = preContract.investMoney;
                vm.item.contractId = preContract.contractId;
                vm.item.projectNo = preContract.projectNo;
            }
            vm.save();
            $("#selectPreContractModal").modal("hide");
        }


        vm.loadData = function (loadProcess) {
            businessInputContractService.getModelById(inputContractId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#signTime").datepicker('setDate', vm.item.signTime);
                    $("#enterTime").datepicker('setDate', vm.item.enterTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessInputContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })

        };
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {

                    vm.item.operateUserLogin = user.userLogin;
                    businessInputContractService.update(vm.item).then(function (value) {
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
        }

        vm.showUserModel = function () {
            var parentDeptId = user.deptId;
            if (vm.opt == 'chargeMen1') {
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.contract.businessContractDetail.chargeMen,
                    title: '项目负责人(总师或项目经理)-人员任命',
                    multiple: false,
                    parentDeptId: parentDeptId
                });
            } else if (vm.opt == 'chargeMen') {
                $scope.showOaSelectEmployeeModal_({
                    userLoginList: vm.item.chargeMen,
                    title: '承办人-人员任命',
                    type: '部门',
                    deptIds: '1',
                    multiple: false,
                    parentDeptId: parentDeptId
                });
            } else if (vm.opt == "totalDesigner") {
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.contract.businessContractDetail.totalDesigner,
                    multiple: false,
                    parentDeptId: parentDeptId,
                    title: '项目总师-人员任命'
                });
            } else if (vm.opt == 'otherDesigner') {
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.contract.businessContractDetail.otherDesigner,
                    parentDeptId: parentDeptId,
                    title: '其他总师-人员任命'
                });
            } else if (vm.opt == 'exeChargeMen') {
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.contract.exeChargeMen,
                    parentDeptId: parentDeptId,
                    title: '项目经理-人员任命'
                });
            } else if (vm.opt == 'contractChargeMen') {
                if (user.deptName.indexOf("一院") >= 0) {
                    parentDeptId = 23;
                } else if (user.deptName.indexOf("二院") >= 0) {
                    parentDeptId = 69;
                } else if (user.deptName.indexOf("环能") >= 0) {
                    parentDeptId = 31;
                }
                $scope.showSelectEmployeeModal_({
                    userLoginList: vm.contract.chargeMen,
                    multiple: false,
                    parentDeptId: parentDeptId,
                    title: '项目主管院长-人员任命'
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'chargeMen1') {
                vm.contract.businessContractDetail.chargeMen = $scope.selectedUserLogins_;
                vm.contract.businessContractDetail.chargeMenName = $scope.selectedUserNames_;
            } else if (vm.opt == 'contractChargeMen') {
                vm.contract.chargeMen = $scope.selectedUserLogins_;
                vm.contract.chargeMenName = $scope.selectedUserNames_;

            } else if (vm.opt == "totalDesigner") {
                vm.contract.businessContractDetail.totalDesignerName = $scope.selectedUserNames_;
                vm.contract.businessContractDetail.totalDesigner = $scope.selectedUserLogins_;
                if (vm.contract.businessContractDetail.chargeMen == "") {
                    vm.contract.businessContractDetail.chargeMen = $scope.selectedUserLogins_;
                    vm.contract.businessContractDetail.chargeMenName = $scope.selectedUserNames_;
                }
            } else if (vm.opt == 'otherDesigner') {
                vm.contract.businessContractDetail.otherDesigner = $scope.selectedUserLogins_;
                vm.contract.businessContractDetail.otherDesignerName = $scope.selectedUserNames_;
            } else if (vm.opt == 'exeChargeMen') {
                vm.contract.exeChargeMen = $scope.selectedUserLogins_;
                vm.contract.exeChargeMenName = $scope.selectedUserNames_;
            } else if (vm.opt == 'chargeMen') {
                vm.item.chargeMen = $scope.selectedOaUserLogins_;
                vm.item.chargeMenName = $scope.selectedOaUserNames_;

            }
            vm.save();
        };

        vm.showCustomerModal = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerName = customer.name;
                vm.item.customerId = customer.id;
                vm.item.depositBank = customer.depositBank;
                vm.item.bankAccount = customer.bankAccount;
                vm.item.linkPhone = customer.linkTel;
                vm.item.postalAddress = customer.address;
                vm.item.agency = customer.linkMan;
            }
        }

        vm.showDeptModal = function () {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择承接部门",
                type: '选部门',
                deptIds: "1",
                deptIdList: "," + vm.item.deptId + ",",
                multiple: false
            });
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showSelectMajorListModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "设计专业").then(function (value) {
                vm.majorNames = value.data.data;
            });
            $("#selectMajorListModal").modal("show");
        }

        vm.saveMajorList = function () {
            var majors = [];
            $(".cb_major:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            $("#selectMajorListModal").modal("hide");

            vm.item.designMajor = majors.join(',');
        }

        vm.showSelectPreDescModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "非常规涉及条件情形的说明").then(function (value) {
                vm.preDescs = value.data.data;

            });
            $("#selectPreDescModal").modal("show");

        }

        vm.savePreDesc = function () {
            var preDescs = [];
            $(".cb_preDesc:checked").each(function () {
                preDescs.push($(this).attr("data-name"));
            });
            $("#selectPreDescModal").modal("hide");
            vm.item.preDesc = preDescs.join(',');
        }

        vm.showSelectPreConditionModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "应对措施及具备的条件").then(function (value) {
                vm.preConditions = value.data.data;
            });
            $("#selectPreConditionModal").modal("show");
        }

        vm.savePreCondition = function () {
            var preConditions = [];
            $(".cb_preCondition:checked").each(function () {
                preConditions.push($(this).attr("data-name"));
            });
            $("#selectPreConditionModal").modal("hide");
            vm.item.preCondition = preConditions.join(',');
        }

        vm.showSelectStageModal = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "设计评审阶段").then(function (value) {
                vm.stages = value.data.data;
            });
            $("#selectStageModal").modal("show");
        }

        vm.saveStage = function () {
            var stages = [];
            $(".cb_stage:checked").each(function () {
                stages.push($(this).attr("data-name"));
            });
            $("#selectStageModal").modal("hide");
            vm.contract.stageNames = stages.join(',');
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;
    })
    //供方信息管理
    .controller("FiveBusinessSupplierController", function ($state, $scope,$rootScope, fiveBusinessSupplierService) {
        var vm = this;

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            qName: "",
            qCode: "",
            name: "",
            code: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });

        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessSupplier";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveBusinessSupplierService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.businessSupplierDetail", {supplierId: id});
        }

        vm.add = function () {
            fiveBusinessSupplierService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessSupplierService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params = $.extend(params, {userLogin: user.userLogin, uiSref: uiSref});
            fiveBusinessSupplierService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'code', placeholder: '请输入客户编号..'},
                    2: {colName: 'name', placeholder: '请输入客户名称..'},
                    3: {colName: 'inType', placeholder: '请输入入库类别..'},
                    4: {colName: 'deptName', placeholder: '请输入录入部门..'},
                    5: {colName: 'supplierType', placeholder: '请输入单位类型..'},
                    6: {colName: 'cooperProjectNum', placeholder: '请输入项目数量..'},
                    7: {colName: 'linkMan', placeholder: '请输入联系人..'},
                    8: {colName: 'linkTel', placeholder: '请输入联系电话..'},
                }, handleColumn: 11
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveBusinessSupplierDetailController", function ($state, $stateParams, $rootScope, commonFileService, commonCodeService, $scope, fiveBusinessSupplierService,commonPrintTableService) {
        var vm = this;
        var supplierId = $stateParams.supplierId;
        var uiSref = "five.businessSupplier";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            vm.loadData(true);
            vm.loadDetail(supplierId);
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            commonFileService.listDataCount(vm.item.businessKey, '-1').then(function (value) {
                if (value.data.data == 0) {
                    toastr.warning("请上传供方单位营业执照与相关资料!");
                    return;
                } else {
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        fiveBusinessSupplierService.update(vm.item).then(function (value) {
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
            })
        }

        vm.loadData = function (loadProcess) {
            fiveBusinessSupplierService.getModelById(supplierId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#contractTime").datepicker('setDate', vm.item.contractTime);
                    $("#reviewTime").datepicker('setDate', vm.item.reviewTime);
                }
            })
            fiveBusinessSupplierService.listCooperationProject(supplierId).then(function (value) {
                vm.list = value.data.data;
            })
            vm.loadUsedNames(supplierId);
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessSupplierService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择承接部门",
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

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'reviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参加评审人员",
                    type: "部门", deptIds: "1", userLoginList: vm.item.reviewUser, multiple: true
                });
            }
            if (vm.status == 'totalDesigner') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择项目经理/总设计师人员",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.totalDesigner,
                    multiple: false,
                    parentDeptId: user.deptId
                });
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'reviewUser') {
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'totalDesigner') {
                vm.item.totalDesigner = $scope.selectedOaUserLogins_;
                vm.item.totalDesignerName = $scope.selectedOaUserNames_;
            }
        };

        vm.loadDetail = function (supplierId) {
            fiveBusinessSupplierService.listDetailById(supplierId).then(function (value) {
                if (value.data.ret) {
                    vm.supplierDetails = value.data.data;
                }
            })
        };
        vm.showDetail = function (id) {
            fiveBusinessSupplierService.getDetailModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                }
            })
            $("#detailModal").modal("show");
        }
        vm.saveDetail = function () {
            if ($("#comment_form").validate().form()) {
                fiveBusinessSupplierService.updateDetail(vm.detail).then(function () {
                    vm.loadDetail(supplierId);
                });
                $("#detailModal").modal("hide");
            }
        };
        vm.addDetail = function () {
            fiveBusinessSupplierService.getNewDetailModel(supplierId).then(function (value) {
                if (value.data.ret) {
                    vm.showDetail(value.data.data);
                }
            });
        };
        vm.removeDetail = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBusinessSupplierService.removeDetail(id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadDetail(supplierId);
                        }
                    })
                }
            })
        }

        vm.showContractLibrary = function (id) {
            $state.go("five.businessContractLibrarySubpackageDetail", {contractLibrarySubpackageId: id});
        }

        vm.checkSupplier = function (name) {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessSupplierService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    fiveBusinessSupplierService.checkSupplier(name, supplierId).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("客户名称 验证通过")
                        }
                    })
                }
            })
        };
        vm.checkTaxNo = function (taxNo) {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessSupplierService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    fiveBusinessSupplierService.checkTaxNo(taxNo, supplierId).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("纳税人识别号 验证通过")
                        }
                    })
                }
            })
        };
        vm.loadUsedNames = function (supplierId) {
            fiveBusinessSupplierService.listUsedNamesById(supplierId).then(function (value) {
                if (value.data.ret) {
                    vm.usedNames = value.data.data;
                }
            })
        };
        vm.showUsedName = function (id) {
            fiveBusinessSupplierService.getUsedNameById(id).then(function (value) {
                if (value.data.ret) {
                    vm.usedName = value.data.data;
                }
            })
            $("#usedNameModal").modal("show");
        }
        vm.updateUsedName = function () {
            if ($("#comment_form").validate().form()) {
                fiveBusinessSupplierService.updateUsedName(vm.usedName).then(function () {
                    vm.loadUsedNames(supplierId);
                });
                $("#usedNameModal").modal("hide");
            }
        };
        vm.addUsedName = function () {
            fiveBusinessSupplierService.getNewUsedName(supplierId,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.showUsedName(value.data.data);
                }
            });
        };
        vm.removeUsedName= function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBusinessSupplierService.removeUsedName(id,user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadUsedNames(customerId);
                        }
                    })
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("供方信息管理");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    //供方信息变更
    .controller("FiveBusinessChangeSupplierController", function ($state, $scope,$rootScope, $stateParams, businessChangeSupplierService) {
        var vm = this;
        var uiSref = "five.businessChangeSupplier";
        var tableName = $rootScope.loadTableName(uiSref);

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {
            qName: "",
            qCode: "",
            name: "",
            code: "",
            pageNum: 1,
            pageSize: $scope.pageSize,
            total: 0
        });

        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};

        vm.init = function () {
            vm.loadPagedData();
            $scope.basicInit("");
            $scope.loadRightData(user.userLogin, uiSref);

            $("#uploadModelFile").fileupload({
                dataType: 'text',
                url: '/business/changeSupplier/receive.do?userLogin=' + user.userLogin,
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
                        var obj = result.msg.split(",");
                        toastr.success("导入数据成功!<br>" + "修改：" + obj[0] + "条   新增：" + obj[1] + "条");
                        vm.queryData();
                    } else {
                        toastr.error(result.msg);
                    }
                }
            });

        };

        vm.queryData = function () {
            vm.params.code = vm.params.qCode;
            vm.params.name = vm.params.qName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            businessChangeSupplierService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (changeSupplierId) {
            $state.go("five.businessChangeSupplierDetail", {changeSupplierId: changeSupplierId});
        }

        vm.add = function () {
            businessChangeSupplierService.getNewModel(user.userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessChangeSupplierService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.downloadModel = function () {
            businessChangeSupplierService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        vm.init();

        return vm;
    })
    .controller("FiveBusinessChangeSupplierDetailController", function ($rootScope, $state, commonFormService, commonFileService, $stateParams, $scope, fiveBusinessSupplierService, businessChangeSupplierService, hrDeptService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessChangeSupplier";
        var tableName = $rootScope.loadTableName(uiSref);

        var changeSupplierId = $stateParams.changeSupplierId;

        vm.init = function () {
            vm.loadData(true);
            $scope.loadRightData(user.userLogin, uiSref);
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            if (vm.item.suppilerId == 0) {
                toastr.error("请先选择需要变更的 供方信息!");
                return
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessChangeSupplierService.update(vm.item).then(function (value) {
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
        vm.loadData = function (loadProcess) {
            businessChangeSupplierService.getModelById(changeSupplierId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    if (vm.item.supplierId != 0) {
                        fiveBusinessSupplierService.getModelById(vm.item.supplierId).then(function (value) {
                            if (value.data.ret) {
                                vm.supplier = value.data.data;
                            }
                        })
                    }
                }
            })
        };
        vm.save = function () {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                businessChangeSupplierService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!");
                    }
                })
            }
        };
        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId,
                multiple: false, deptIds: "1"
            });
        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.supplier.deptName = $scope.selectedOaDeptNames_;
            vm.supplier.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.saveChange = function () {
            if ($("#detail_form1").validate().form()) {
                vm.supplier.operateUserLogin = user.userLogin;
                fiveBusinessSupplierService.update(vm.supplier).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("变更信息 保存成功!");
                    }
                })
            }
        };
        vm.selectSupplier = function () {
            vm.qSupplier = "";
            fiveBusinessSupplierService.selectAll(user.userLogin, "five.businessSupplier").then(function (value) {
                vm.suppliers = value.data.data;
                singleCheckBox(".cb_supplier", "data-name");
            });
            $("#selectSupplierModal").modal("show");
        }
        vm.saveSelectSupplier = function () {
            $("#selectSupplierModal").modal("hide");
            if ($(".cb_supplier:checked").length > 0) {
                var supplier = $.parseJSON($(".cb_supplier:checked").first().attr("data-name"));
                vm.item.supplierId = supplier.id;
                vm.item.supplierName = supplier.name;
            }
            vm.save();
        };
        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("供方信息管理");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })

    //分包评审
    .controller("FiveBusinessSubpackageController", function ($state, $scope,$rootScope, fiveBusinessSubpackageService) {
        var vm = this;
        vm.params = {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0};
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessSubpackage";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            });
            fiveBusinessSubpackageService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.businessSubpackageDetail", {subpackageId: id});
        }

        vm.add = function () {
            fiveBusinessSubpackageService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessSubpackageService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }
        vm.changeOpenStamp = function (id) {
            bootbox.confirm("您确定修改该合同印花税状态吗", function (result) {
                if (result) {
                    fiveBusinessSubpackageService.changeOpenStamp(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("切换成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        }
        vm.queryData();
        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params.uiSref = uiSref;
            params.userLogin = user.userLogin;
            fiveBusinessSubpackageService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'subContractName', placeholder: '请输入分包合同名称..'},
                    2: {colName: 'subContractNo', placeholder: '请输入分包合同号..'},
                    3: {colName: 'supplierName', placeholder: '请输入供方名称..'},
                    4: {colName: 'deptName', placeholder: '请输入发包部门名称..'},
                    5: {colName: 'contractName', placeholder: '请输入主合同名称..'},
                    6: {colName: 'subContractMoney', placeholder: '请输入合同金额..'},
                }, handleColumn: 10
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;

    })
    .controller("FiveBusinessSubpackageDetailController", function ($state, $stateParams, $rootScope, commonCodeService, $scope, fiveBusinessContractLibraryService, fiveBusinessSupplierService, fiveBusinessContractReviewService,fiveBusinessSubpackageService,commonPrintTableService) {
        var vm = this;
        var subpackageId = $stateParams.subpackageId;
        var uiSref = "five.businessSubpackage";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init = function () {
            vm.loadData(true);
            $("#contractAttachUrl").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url: '/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator: user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star = file.name.lastIndexOf(".");
                    var fileType = file.name.substring(star, file.name.length);
                    if (".pdf,.png,.jpg,.bmp,.jpeg".indexOf(fileType) == -1) {
                        toastr.error('请上传.pdf,.png,.jpg,.bmp,.jpeg格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.contractAttachUrl = '/common/attach/download/' + result.data.id;

                        //新增正式签章合同文件夹
                        fiveBusinessContractReviewService.addSuccessContractDir(vm.item.businessKey,result.data.id,user.userLogin).then(function (value) {
                            if (value.data.ret) {
                                vm.item.operateUserLogin = user.userLogin;
                                fiveBusinessSubpackageService.update(vm.item).then(function (value) {
                                    if (value.data.ret) {
                                        toastr.success("正式签章合同文件夹已创建!其他相关资料请在附件中上传");
                                        //刷新附件
                                        $scope.loadProcessInstance(vm.item.processInstanceId);
                                    }
                                })
                            }
                        })
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }
        //发送流程验证
        $scope.showSendTask = function (send) {

            //如果是 盖章合同上传 节点 填写签约时间 和 扫描件
            if (($rootScope.processInstance.currentTaskName.indexOf("盖章合同上传") >= 0 || $rootScope.processInstance.currentTaskName.indexOf("确认") >= 0) && (vm.item.signTime == "" || vm.item.contractAttachUrl == "")) {
                toastr.warning("请先填写 签约时间 合同盖章扫描附件");
                return;
            }
            //如果是 印花税 节点 填写到账金额
            if ($rootScope.processInstance.currentTaskName.indexOf("印花税") >= 0 && (vm.item.contractMoney == "" || vm.item.taxType.length == 0)) {
                toastr.warning("请先填写 合同额 印花税目");
                return;
            }
            //如果是 经营发展部 节点 公司级 填写评审人员
            if ($rootScope.processInstance.currentTaskName.indexOf("经营发展部人员") >= 0 && (vm.item.reviewLevel == '公司级' && vm.item.reviewUserName == '')) {
                toastr.warning("请先填写 公司级评审人员");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBusinessSubpackageService.update(vm.item).then(function (value) {
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

        vm.loadData = function (loadProcess) {
            fiveBusinessSubpackageService.getModelById(subpackageId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#contractTime").datepicker('setDate', vm.item.contractTime);
                    $("#reviewTime").datepicker('setDate', vm.item.reviewTime);
                }
            })
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessSubpackageService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showDeptModal = function (id) {
            if (vm.opt == 'deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择发包部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            } else {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择对内分包部门",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.inDeptId,
                    multiple: false
                });
            }

        }
        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt == 'deptId') {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            } else {
                vm.item.inDeptName = $scope.selectedOaDeptNames_;
                vm.item.inDeptId = Number($scope.selectedOaDeptIds_);
            }

        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'contractChargeLeader') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择合同分管副总",
                    type: "人员",
                    userLogins: "3996,2192,0166,2873,2996",
                    userLoginList: vm.item.businessChargeLeader,
                    multiple: false,
                });
            } else if (vm.status == 'reviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参加评审人员",
                    type: "部门", deptIds: "1", userLoginList: vm.item.reviewUser, multiple: true
                });
            } else if (vm.status == 'deptReviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择院级参加评审人员",
                    type: "部门",
                    deptIds: '1',
                    parentDeptId: vm.item.headDeptId,
                    userLoginList: vm.item.deptReviewUser,
                    multiple: true
                });
            } else if (vm.status == 'deptChargeMen') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择部门领导人员",
                    type: "部门",
                    deptIds: "1",
                    parentDeptId: vm.item.deptId,
                    userLoginList: vm.item.deptChargeMen,
                    multiple: true
                });
            }

        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'contractChargeLeader') {
                vm.item.contractChargeLeader = $scope.selectedOaUserLogins_;
                vm.item.contractChargeLeaderName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'reviewUser') {
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'deptReviewUser') {
                vm.item.deptReviewUser = $scope.selectedOaUserLogins_;
                vm.item.deptReviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'deptChargeMen') {
                vm.item.deptChargeMen = $scope.selectedOaUserLogins_;
                vm.item.deptChargeMenName = $scope.selectedOaUserNames_;
            }
        };

        vm.selectSupplier = function () {
            vm.qSupplier = "";
            fiveBusinessSupplierService.selectAll(user.userLogin, "five.businessSupplier").then(function (value) {
                vm.suppliers = value.data.data;
                singleCheckBox(".cb_supplier", "data-name");
            });
            $("#selectSupplierModal").modal("show");
        }
        vm.showSupplier = function (supplierId) {
            if (supplierId == 0) {
                toastr.error("请选择供方信息 或者 填写后点击保存供方信息！")
            } else {
                $state.go("five.businessSupplierDetail", {supplierId: supplierId});
            }
        }

        vm.saveSelectSupplier = function () {
            $("#selectSupplierModal").modal("hide");
            if ($(".cb_supplier:checked").length > 0) {
                var supplier = $.parseJSON($(".cb_supplier:checked").first().attr("data-name"));
                vm.item.supplierId = supplier.id;
                vm.item.supplierName = supplier.name;
                vm.item.supplierCreditCode = supplier.creditCode;
                vm.item.supplierLinkMan = supplier.linkMan;
                vm.item.supplierLinkTel = supplier.linkTel;
                vm.item.depositBank = supplier.depositBank;
                vm.item.bankAccount = supplier.bankAccount;
            }
            vm.save();
        }
        vm.addSupplier = function (subpackageId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.supplierName == '' || vm.item.supplierName == null) {
                toastr.error("请先填写供方信息");
            } else {
                fiveBusinessSubpackageService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        fiveBusinessSubpackageService.addSupplier(user.userLogin, subpackageId).then(function (value) {
                            if (value.data.ret) {
                                fiveBusinessSupplierService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.supplierId = value.data.data.id;
                                        vm.save();
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                    }
                                })
                            }
                        });
                    }
                })
            }

        }
        //合同库
        /*  vm.showContractLibraryModal = function () {
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
                  //需要从 合同库 中同步到 项目启用的数据
                  vm.item.contractName = library.projectName;
                  vm.item.contractLibraryId = library.id;
                  vm.item.contractNo = library.contractNo;
                  vm.item.contractMoney = library.contractMoney;
                  vm.item.contractType = library.contractType;
                  vm.item.projectNature = library.projectNature;
              }
              vm.save();
              $("#selectLibraryModal").modal("hide");
          }*/
        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: vm.item.contractLibraryId,
                uiSref: "five.businessContractLibrary",
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.item.contractName = $rootScope.selectedLibrary.contractName;
            vm.item.contractLibraryId = $rootScope.selectedLibrary.id;
            vm.item.contractNo = $rootScope.selectedLibrary.contractNo;
            vm.item.contractMoney = $rootScope.selectedLibrary.contractMoney;
            vm.item.contractType = $rootScope.selectedLibrary.contractType;
            vm.item.projectNature = $rootScope.selectedLibrary.projectNature;
            $scope.closeLibrarySelectModal_();
            vm.save();
        }

        //选择补充主合同
        vm.showSelectMainLibraryModal = function () {
            $scope.showLibrarySubpackageSelectModal_({
                isPurchase: false, //是否为采购
                selectLibrarySubpackageId: vm.item.mainContractLibraryId,
                uiSref: uiSref,
                multiple: false
            });
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("分包评审");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $rootScope.saveSelectLibrarySubpackage_ = function () {
            //需要从 合同库 中同步到 评审的数据
            vm.item.mainContractLibraryId = $rootScope.selectedLibrarySubpackage.id;
            vm.item.mainContractLibraryName = $rootScope.selectedLibrarySubpackage.subContractName;
            vm.item.mainContractLibraryNo = $rootScope.selectedLibrarySubpackage.subContractNo;
            $scope.closeLibrarySubpackageSelectModal_();
            vm.save();
        }

        //分包合同编号
        vm.getSubContractNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessSubpackageService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    fiveBusinessSubpackageService.getSubContractNo(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("分包合同号已更新!");
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
    //采购评审
    .controller("FiveBusinessPurchaseController", function ($state, $scope,$rootScope, fiveBusinessPurchaseService) {
        var vm = this;
        vm.params = {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0};
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.businessSubpackage";
        var tableName = $rootScope.loadTableName("five.businessPurchase");

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            });
            fiveBusinessPurchaseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.businessPurchaseDetail", {purchaseId: id});
        }

        vm.add = function () {
            fiveBusinessPurchaseService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.changeOpenStamp = function (id) {
            bootbox.confirm("您确定修改该合同印花税状态吗", function (result) {
                if (result) {
                    fiveBusinessPurchaseService.changeOpenStamp(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("切换成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessPurchaseService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }
        vm.queryData();

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function (params) {
            params.uiSref = uiSref;
            params.userLogin = user.userLogin;
            fiveBusinessPurchaseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var option = {
                filterColumns: {
                    1: {colName: 'subContractName', placeholder: '请输入采购合同名称..'},
                    2: {colName: 'subContractNo', placeholder: '请输入采购合同号..'},
                    3: {colName: 'supplierName', placeholder: '请输入供方名称..'},
                    4: {colName: 'deptName', placeholder: '请输入采购部门名称..'},
                    5: {colName: 'contractName', placeholder: '请输入主合同名称..'},
                    6: {colName: 'subContractMoney', placeholder: '请输入合同金额..'},
                }, handleColumn: 10
            };
            tablefilter.queryFunction = vm.refreshPagedData;
            tablefilter.params = vm.params;
            tablefilter.initializeFilter(option);
        });
        return vm;

    })
    .controller("FiveBusinessPurchaseDetailController", function ($state, $stateParams, $rootScope, commonCodeService, $scope, businessRecordService, fiveBusinessContractLibraryService,
                                                                  fiveBusinessContractReviewService,fiveBusinessSupplierService, fiveBusinessPurchaseService,commonPrintTableService) {
        var vm = this;
        var purchaseId = $stateParams.purchaseId;
        var uiSref = "five.businessPurchase";
        var tableName = $rootScope.loadTableName(uiSref);


        vm.init = function () {
            vm.loadData(true);
            $("#contractAttachUrl").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url: '/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator: user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star = file.name.lastIndexOf(".");
                    var fileType = file.name.substring(star, file.name.length);
                    if (".pdf,.png,.jpg,.bmp,.jpeg".indexOf(fileType) == -1) {
                        toastr.error('请上传.pdf,.png,.jpg,.bmp,.jpeg格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.contractAttachUrl = '/common/attach/download/' + result.data.id;

                        //新增正式签章合同文件夹
                        fiveBusinessContractReviewService.addSuccessContractDir(vm.item.businessKey,result.data.id,user.userLogin).then(function (value) {
                            if (value.data.ret) {
                                vm.item.operateUserLogin = user.userLogin;
                                fiveBusinessPurchaseService.update(vm.item).then(function (value) {
                                    if (value.data.ret) {
                                        toastr.success("正式签章合同文件夹已创建!其他相关资料请在附件中上传");
                                        //刷新附件
                                        $scope.loadProcessInstance(vm.item.processInstanceId);
                                    }
                                })
                            }
                        })
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }
        //发送流程验证
        $scope.showSendTask = function (send) {
            //如果是 盖章合同上传 节点 填写签约时间 和 扫描件
            if (($rootScope.processInstance.currentTaskName.indexOf("盖章合同上传") >= 0 || $rootScope.processInstance.currentTaskName.indexOf("确认") >= 0)
                && (vm.item.signTime == "" || vm.item.contractAttachUrl == "")) {
                toastr.warning("请先填写 签约时间 合同盖章扫描附件");
                return;
            }
            //如果是 印花税 节点 填写到账金额
            if ($rootScope.processInstance.currentTaskName.indexOf("印花税") >= 0 && (vm.item.contractMoney == "" || vm.item.taxType == "")) {
                toastr.warning("请先填写 合同额 印花税目");
                return;
            }
            //如果是 经营发展部 节点 公司级 填写评审人员
            if ($rootScope.processInstance.currentTaskName.indexOf("经营发展部人员") >= 0 && (vm.item.reviewLevel == '公司级' && vm.item.reviewUserName == '')) {
                toastr.warning("请先填写 公司级评审人员");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBusinessPurchaseService.update(vm.item).then(function (value) {
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

        vm.loadData = function (loadProcess) {
            fiveBusinessPurchaseService.getModelById(purchaseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#contractTime").datepicker('setDate', vm.item.contractTime);
                    $("#reviewTime").datepicker('setDate', vm.item.reviewTime);
                }
            })
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessPurchaseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择采购部门",
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

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'contractChargeLeader') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择合同分管副总",
                    type: "人员",
                    userLogins: "3996,2192,0166,2873,2996",
                    userLoginList: vm.item.businessChargeLeader,
                    multiple: false,
                });
            } else if (vm.status == 'reviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择参加评审人员",
                    type: "部门", deptIds: "1", userLoginList: vm.item.reviewUser, multiple: true
                });
            }
            if (vm.status == 'deptReviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择院级参加评审人员",
                    type: "部门",
                    deptIds: vm.item.headDeptId,
                    parentDeptId: vm.item.headDeptId,
                    userLoginList: vm.item.deptReviewUser,
                    multiple: true
                });
            }

        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'contractChargeLeader') {
                vm.item.contractChargeLeader = $scope.selectedOaUserLogins_;
                vm.item.contractChargeLeaderName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'reviewUser') {
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'deptReviewUser') {
                vm.item.deptReviewUser = $scope.selectedOaUserLogins_;
                vm.item.deptReviewUserName = $scope.selectedOaUserNames_;
            }
        };

        vm.selectSupplier = function () {
            vm.qSupplier = "";
            fiveBusinessSupplierService.selectAll(user.userLogin, "five.businessSupplier").then(function (value) {
                vm.suppliers = value.data.data;
                singleCheckBox(".cb_supplier", "data-name");
            });
            $("#selectSupplierModal").modal("show");
        }
        vm.showSupplier = function (supplierId) {
            if (supplierId == 0) {
                toastr.error("请选择供方信息 或者 填写后点击保存供方信息！")
            } else {
                $state.go("five.businessSupplierDetail", {supplierId: supplierId});
            }
        }
        vm.saveSelectSupplier = function () {
            $("#selectSupplierModal").modal("hide");
            if ($(".cb_supplier:checked").length > 0) {
                var supplier = $.parseJSON($(".cb_supplier:checked").first().attr("data-name"));
                vm.item.supplierId = supplier.id;
                vm.item.supplierName = supplier.name;
                vm.item.supplierCreditCode = supplier.creditCode;
                vm.item.supplierLinkMan = supplier.linkMan;
                vm.item.supplierLinkTel = supplier.linkTel;
                vm.item.depositBank = supplier.depositBank;
                vm.item.bankAccount = supplier.bankAccount;
            }
            vm.save();
        }
        vm.addSupplier = function (purchaseId) {
            vm.item.operateUserLogin = user.userLogin;
            if (vm.item.supplierName == '' || vm.item.supplierName == null) {
                toastr.error("请先填写供方信息");
            } else {
                fiveBusinessSubpackageService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        fiveBusinessSubpackageService.addSupplier(user.userLogin, purchaseId).then(function (value) {
                            if (value.data.ret) {
                                fiveBusinessSupplierService.getModelById(value.data.data).then(function (value) {
                                    if (value.data.ret) {
                                        vm.item.supplierId = value.data.data.id;
                                        vm.save();
                                        toastr.success("成功添加 " + value.data.data.name + " 信息");
                                    }
                                })
                            }
                        });
                    }
                })
            }

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
                vm.item.contractName = library.projectName;
                vm.item.contractLibraryId = library.id;
                vm.item.contractNo = library.contractNo;
                vm.item.contractMoney = library.contractMoney;
                vm.item.contractType = library.contractType;
                vm.item.projectNature = library.projectNature;
            }
            vm.save();
            $("#selectLibraryModal").modal("hide");
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("采购评审");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        //采购合同编号
        vm.getSubContractNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessPurchaseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    fiveBusinessPurchaseService.getPurContractNo(vm.item.id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("采购合同号已更新!");
                        }
                    })
                }
            })

        }


        //项目信息备案
        vm.showRecordModal = function () {
            businessRecordService.listRecordByUserLogin(user.userLogin, vm.item.recordId).then(function (value) {
                if (value.data.ret) {
                    vm.listRecord = value.data.data;
                    singleCheckBox(".cb_record", "data-name");
                }
            })
            vm.showChooseStatus = '项目信息备案-' + vm.item.projectName;
            $("#selectRecordModal").modal("show");
        }
        vm.saveSelectRecordModel = function () {
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                //需要同步到 合同评审 的数据
                vm.item.recordId = record.id;
                //项目名称
                vm.item.projectName = record.projectName;
                //项目号
                vm.item.projectNo = record.projectNo;
            }
            vm.save();
            $("#selectRecordModal").modal("hide");
        }

        //选择补充主合同
        vm.showSelectMainLibraryModal = function () {
            $scope.showLibrarySubpackageSelectModal_({
                isPurchase: true, //是否为采购
                selectLibrarySubpackageId: vm.item.mainContractLibraryId,
                uiSref: uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrarySubpackage_ = function () {
            //需要从 合同库 中同步到 评审的数据
            vm.item.mainContractLibraryId = $rootScope.selectedLibrarySubpackage.id;
            vm.item.mainContractLibraryName = $rootScope.selectedLibrarySubpackage.subContractName;
            vm.item.mainContractLibraryNo = $rootScope.selectedLibrarySubpackage.subContractNo;
            $scope.closeLibrarySubpackageSelectModal_();
            vm.save();
        }

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })

    //合同收入
    .controller("FiveBusinessIncomeController", function ($state, $rootScope, $scope, businessIncomeService, fiveFinanceInvoiceService, businessBidApplyService) {
        var vm = this;
        var uiSref = "five.businessIncome";
        var tableName = $rootScope.loadTableName(uiSref);

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key, {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();

            $scope.initFileUpload("uploadModelFile", function (e, data) {
                var file = data.files[0];
                return true;
            }, function (e,data) {
                if (data.result.ret) {
                    $rootScope.commonAttachDownload(data.result.data);
                } else {
                    toastr.error(data.result.msg);
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
            businessIncomeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.businessIncomeDetail", {incomeId: id});
        }
        vm.showNew = function (id) {
            if (vm.opt == 1) {
                $state.go("five.businessPreContractDetail", {preContractId: id});
            } else {
                $state.go("five.businessContractReviewDetail", {contractReviewId: id});
            }
        }
        vm.showInvoice = function (id) {
            $state.go("finance.invoiceDetail", {invoiceId: id});
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

        vm.add = function () {
            var userLogin = user.userLogin;
            businessIncomeService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessIncomeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.addApply = function (id) {
            businessBidApplyService.getNewModelById(id, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    $state.go("business.bidApplyDetail", {bidApplyId: value.data.data});
                }
            })
        }

        vm.downloadModel = function () {
            businessIncomeService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        vm.init();

        vm.downTempleXml = function () {
            businessIncomeService.downTempleXls(user.userLogin).then(function (response) {
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

       /* $("#uploadModelFile").fileupload({
            dataType: 'text',
            url: '/business/income/receive.json?userLogin=' + user.userLogin,
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
                    $rootScope.commonAttachDownload(result.data);
                   // toastr.success(result.data);

                } else {
                    toastr.error(result.msg);
                }
            }
        });
*/



        vm.refreshPagedData = function (params) {
            params =$.extend(params,{ userLogin: user.userLogin,uiSref:uiSref});

            businessIncomeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{colName:'contractName',placeholder:'请输入合同名称..'},
                    2:{colName:'contractNo',placeholder:'请输入合同号..'},
                    3:{colName:'contractMoney',placeholder:'请输入合同金额(万元)..'},
                    4:{colName:'incomeMoney',placeholder:'请输入收入金额(万元)..'},
                    5:{colName:'contractIncomeMoney',placeholder:'请输入合同已领金额'},
                    7:{colName:'creatorName',placeholder:'请输入创建人'},
                    8:{colName:'gmtCreate'},
                },handleColumn:9};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;
    })
    .controller("FiveBusinessIncomeDetailController", function ($state, $stateParams, $rootScope, $scope, fiveBusinessContractLibraryService, businessIncomeService, fiveBusinessContractLibrarySubpackageService, businessCustomerService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessIncome";
        var tableName = $rootScope.loadTableName(uiSref);

        var incomeId = $stateParams.incomeId;

        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            businessIncomeService.getModelById(incomeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.item.incomeMoney = (Number)(vm.item.incomeMoney);
                    vm.item.projectInvest = (Number)(vm.item.projectInvest);
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
            businessIncomeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.selectCustomer = function () {
            vm.qCustomer = "";
            businessCustomerService.selectAll(user.userLogin, "five.businessCustomer").then(function (value) {
                vm.customers = value.data.data;
                singleCheckBox(".cb_customer", "data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.showBigNum = function () {
            if (vm.item.incomeMoney != null) {
                businessIncomeService.moneyTurnCapital(vm.item.incomeMoney).then(function (value) {
                    vm.item.incomeMoneyMax = value.data.data;
                })
            }
        }


        vm.showCustomer = function (customerId) {
            if (customerId == 0) {
                toastr.error("请选择客户信息 或者 填写后点击保存客户信息！")
            } else {
                $state.go("five.businessCustomerDetail", {customerId: customerId});
            }
        }
        vm.saveSelectCustomer = function () {
            $("#selectCustomerModal").modal("hide");
            if ($(".cb_customer:checked").length > 0) {
                var customer = $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerId = customer.id;
                vm.item.customerName = customer.name;
                vm.item.linkMan = customer.linkMan;
                vm.item.linkTel = customer.linkTel;
                vm.item.customerAddress = customer.address;
                vm.item.linkTitle = customer.linkTitle;
                vm.item.customerCode = customer.code
            }
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
                businessIncomeService.update(vm.item).then(function (value) {
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

        //合同库
        vm.showLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: vm.item.contractLibraryId,
                uiSref: "five.businessContractLibrary",
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            //需要从 合同库 中同步到 收入内转 的数据
            vm.item.contractName = $rootScope.selectedLibrary.contractName;
            //投资额
            vm.item.contractMoney = $rootScope.selectedLibrary.contractMoney;
            //合同号
            vm.item.contractNo = $rootScope.selectedLibrary.contractNo;
            //合同已领金额
            vm.item.contractIncomeMoney = $rootScope.selectedLibrary.contractIncomeMoney;
            //项目名称
            vm.item.projectName = $rootScope.selectedLibrary.projectName;
            //项目号
            vm.item.projectNo = $rootScope.selectedLibrary.projectNo;

            vm.item.contractLibraryId = $rootScope.selectedLibrary.id;
            $scope.closeLibrarySelectModal_();
            vm.save();
            $("#selectLibraryModal").modal("hide");
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

        /*打印*/
        vm.print = function () {
            businessIncomeService.getPrintData(incomeId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("项目信息备案");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        //收入确认 取消
        vm.getNotarizeIncome = function (incomeId) {
            bootbox.confirm("是否确认 修改合同收入状态！", function (result) {
                if (result) {
                    businessIncomeService.getNotarizeIncome(incomeId, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("收入状态修改成功！")
                            vm.loadData();
                        }
                    })
                }
            })
        };

        //打印
        vm.print=function () {
            console.log(vm.item.businessKey);
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("合同收费");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;

    })
    
    //预支明细表
    .controller("FiveBusinessAdvanceController", function ($state, $scope,$rootScope, fiveBusinessAdvanceService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.businessAdvance";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime,endTime1:vm.params.endTime
            });
            fiveBusinessAdvanceService.downTempleXls(params).then(function (response) {

                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            })
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveBusinessAdvanceService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.businessAdvanceDetail", {advanceId: id});
        }

        vm.add = function () {
            fiveBusinessAdvanceService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }


        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessAdvanceService.remove(id, user.userLogin).then(function (value) {
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
            fiveBusinessAdvanceService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'部门'},
                    2:{type:"input",colName:'month',placeholder:'申报时间'},
                    3:{type:"input",colName:'month',placeholder:'预支总额'},
                    4:{type:"input",colName:'creatorName',placeholder:'创建人'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveBusinessAdvanceDetailController", function ($state,$stateParams,$rootScope,$scope,fiveBusinessAdvanceService,commonPrintTableService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.businessAdvance";
        var tableName = $rootScope.loadTableName(uiSref);

        var advanceId = $stateParams.advanceId;



        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
            $("#btnUpload").fileupload({
                dataType: 'json',
                url:"/business/advance/updateExcl.json",
                progress:function(e,data){
                    var speed=(data.bitrate/(8*1024*1024)).toFixed(2)+"Mbit/s";
                    var percent = parseInt(data.loaded / data.total * 100, 10);
                    $("#uploadProgress").css('width', percent + "%");
                    if(APP_VERSION){
                        $(".loading-message.loading-message-boxed span").html(" "+speed+" "+percent+"%");
                    }else{
                        $(".blockui span:eq(0)").html(" "+speed+" "+percent+"%");
                    }
                },
                send:function(e,data){
                    if(APP_VERSION){
                        Metronic.blockUI({
                            boxed: true
                        });
                    }else {
                        KTApp.block('body', {
                            overlayColor: '#000000',
                            type: 'v2',
                            state: 'primary',
                            message: 'Processing...'
                        });
                    }
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 20 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制20MB!');
                        return false;
                    }
                    if(file.name.indexOf(".xls")!==file.name.length-4){
                        toastr.error("只可以上传xls文件!");
                        return ;
                    };
                    data.formData = {advanceId: advanceId,userLogin:user.enLogin};
                    data.submit();
                },
                done: function (e, data) {
                    if (refreshTimer) {
                        clearTimeout(refreshTimer);
                    }
                    refreshTimer = setTimeout(function () {
                        if(APP_VERSION){
                            Metronic.unblockUI();
                        }else {
                            KTApp.unblock('body');
                        }
                        if (data.result.ret) {
                            toastr.success("上传成功!");
                            vm.loadDetail();
                            vm.loadData();
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });
        }
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='personName'){
                $scope.showOaSelectEmployeeModal_({title:"请选择人员",type:"部门",deptIds:vm.item.deptId, userLoginList: vm.detail.personName,multiple:false});
            }
        };
        //保存选人的login和名字
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='personName'){
                vm.detail.personNo = $scope.selectedOaUserLogins_;
                vm.detail.personName = $scope.selectedOaUserNames_;
            }
        };
        //选部门模块
        vm.showDeptModal=function(id) {

            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //加载
        vm.loadData = function (loadProcess) {
            fiveBusinessAdvanceService.getModelById(advanceId).then(function (value) {
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
            fiveBusinessAdvanceService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.loadDetail=function(){
            fiveBusinessAdvanceService.listDetail(advanceId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveBusinessAdvanceService.getNewModelDetail(advanceId).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModel").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveBusinessAdvanceService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModel").modal("show");
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveBusinessAdvanceService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                            vm.loadData();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveBusinessAdvanceService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModel").modal("hide");
                    vm.loadDetail();
                    vm.loadData();
                }

            })
        };

        vm.countTotalPrice =function(){
            var bonus = vm.detail.projectBonus;
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
            if (bonus == ""){
                vm.detail.projectBonus = 0;
            }else {
                vm.detail.projectBonus = bonus.toString();
            }
        };

        vm.downExcel=function(){
            var params =  {
                advanceId:advanceId
            };
            fiveBusinessAdvanceService.downTempleXls(params).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            })
        }


        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBusinessAdvanceService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $scope.refresh();});
                    }
                })

            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }


        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;

                    lodop.PRINT_INIT("预支明细表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();

        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //工程承包项目招标文件评审（含联合体评审）
    .controller("FiveBusinessTenderDocumentReviewController", function ($state, $scope, $rootScope, fiveBusinessTenderDocumentReviewService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.businessTenderDocumentReview";

        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveBusinessTenderDocumentReviewService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.businessTenderDocumentReviewDetail", {tenderId: id});
        }

        vm.add = function () {
            fiveBusinessTenderDocumentReviewService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }


        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessTenderDocumentReviewService.remove(id, user.userLogin).then(function (value) {
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
            fiveBusinessTenderDocumentReviewService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'projectName',placeholder:'项目名称'},
                    2:{type:"input",colName:'projectLocation',placeholder:'项目地点'},
                    3:{type:"input",colName:'totalPrice',placeholder:'总投资'},
                    4:{type:"input",colName:'creatorName',placeholder:'创建人'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveBusinessTenderDocumentReviewDetailController", function ($state,$stateParams,$rootScope,$scope,fiveBusinessTenderDocumentReviewService,commonPrintTableService,businessRecordService,commonFileService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.businessTenderDocumentReviewDetail";
        var tenderId = $stateParams.tenderId;
        var tableName = $rootScope.loadTableName("five.businessTenderDocumentReview");

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        }
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='projectManager'){
                $scope.showOaSelectEmployeeModal_({title:"请选择项目经理",type:"部门",deptIds:"1", userLoginList: vm.item.projectManager,multiple:true});
            }
            if (vm.status=='reviewUserName'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司级评审人员",type:"部门",deptIds:"1", userLoginList: vm.item.reviewUser,multiple:true});
            }
            if (vm.status=='deptReviewUsername'){
                $scope.showOaSelectEmployeeModal_({title:"请选择院级评审人员",type:"部门",deptIds:"1", userLoginList: vm.item.deptReviewUser,multiple:true});
            }
            if (vm.status=='deptChargeManName'){
                $scope.showOaSelectEmployeeModal_({title:"请选择院级评审人员",type:"部门",deptIds:"1", userLoginList: vm.item.deptReviewUser,multiple:true});
            }
        };
        //保存选人的login和名字
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='projectManager'){
                vm.item.projectManager = $scope.selectedOaUserLogins_;
                vm.item.projectManagerName = $scope.selectedOaUserNames_;
            }
            else if ( vm.status=='reviewUserName'){
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUsername = $scope.selectedOaUserNames_;
            }
            else if ( vm.status=='deptReviewUsername'){
                vm.item.deptReviewUser = $scope.selectedOaUserLogins_;
                vm.item.deptReviewUsername = $scope.selectedOaUserNames_;
            }
            else if ( vm.status=='deptChargeManName'){
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
            }
        };
        //选部门模块
        vm.showDeptModal=function(id) {
            if (id = 0){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }
            if (id = 1){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptCharge+"",
                    multiple:false,deptIds:"48,29",parentDeptId:2});
            }


        };

        $rootScope.saveSelectDept_ =function() {
            if (id = 0){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if (id = 1){
                $scope.closeOaSelectEmployeeModal_();
                vm.item.deptChargeName = $scope.selectedOaDeptNames_;
                vm.item.deptCharge = Number($scope.selectedOaDeptIds_);
            }
        };

        vm.countTotalPrice =function(){
            var bonus = vm.item.totalPrice;
            //确保输入的是数字
            bonus = bonus.replace(/[^\d\.]/g, '');
            //确保第一个输入的是数字
            bonus = bonus.replace(/^\./g,'');
            //确保不能输入两个小数点
            bonus = bonus.replace(/\.{2,}/g,'.');
            //保证小数点只出现一次，而不能出现两次以上
            bonus = bonus.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
            //确保只能输入两位小数
            bonus = bonus.replace(/^(\-)*(\d+)\.(\d\d\d\d\d\d).*$/,'$1$2.$3');

            vm.item.totalPrice = bonus.toString();
        };

        //加载
        vm.loadData = function (loadProcess) {
            fiveBusinessTenderDocumentReviewService.getModelById(tenderId).then(function (value) {
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
            fiveBusinessTenderDocumentReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(true);
                }
            })
        };

        vm.downExcel=function(){
            var params =  {
                tenderId:tenderId
            };
            fiveBusinessTenderDocumentReviewService.downTempleXls(params).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            })
        }


        //发送流程验证
        $scope.showSendTask=function(send){
            commonFileService.listDataCount(vm.item.businessKey, '-1').then(function (value) {
                if (vm.item.combo == "是" && value.data.data == 0) {
                    toastr.warning("请上传联合体附件!");
                    return;
                } else {
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        fiveBusinessTenderDocumentReviewService.update(vm.item).then(function (value) {
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
            })

        };

        //项目信息备案
        vm.showRecordModal = function () {
            businessRecordService.listRecordByUserLogin(user.userLogin, 0).then(function (value) {
                if (value.data.ret) {
                    vm.listRecord = value.data.data;
                    singleCheckBox(".cb_record", "data-name");
                }
            })
            vm.showChooseStatus = '项目信息备案-' + vm.item.projectName;
            $("#selectRecordModal").modal("show");
        }
        $rootScope.saveSelectRecord_ = function () {
            //需要从 备案 中同步到 评审的数据
            vm.item.recordId =  $rootScope.selectedRecord.id;
            //项目名称
            vm.item.projectName =  $rootScope.selectedRecord.projectName;
            //建设地址
            vm.item.projectLocation = $rootScope.selectedRecord.projectAddress;
            $scope.closeRecordSelectModal_();
            vm.save();
        }


        vm.saveSelectRecordModel = function () {
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                //需要同步到 合同评审 的数据
                vm.item.recordId = record.id;
                //项目名称
                vm.item.projectName = record.projectName;
                //建设地址
                vm.item.projectLocation = record.projectAddress;
            }
            vm.save();
            $("#selectRecordModal").modal("hide");
        }


        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;

                    lodop.PRINT_INIT("工程承包项目招标文件评审");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();

        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //预支明细 统计签发单
    .controller("FiveBusinessAdvanceCollectController", function ($state, $scope,$rootScope, fiveBusinessAdvanceCollectService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.businessAdvanceCollect";
        var tableName = $rootScope.loadTableName(uiSref);


        vm.show = function (id) {
            $state.go("five.businessAdvanceCollectDetail", {advanceCollectId: id});
        }
        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveBusinessAdvanceCollectService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.add = function () {
            fiveBusinessAdvanceCollectService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }


        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveBusinessAdvanceCollectService.remove(id, user.userLogin).then(function (value) {
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
            fiveBusinessAdvanceCollectService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'year',placeholder:'请输入项目名称..'},
                    2:{type:"input",colName:'deptName',placeholder:'部门'},
                    3:{type:"input",colName:'month',placeholder:'月份'},
                    4:{type:"input",colName:'creatorName',placeholder:'创建人'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveBusinessAdvanceCollectDetailController", function ($state,$stateParams,$rootScope,$scope,fiveBusinessAdvanceCollectService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.businessAdvanceCollect";
        var tableName = $rootScope.loadTableName(uiSref);

        var advanceCollectId = $stateParams.advanceCollectId;
        debugger;

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
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
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='expert'){
                $scope.showOaSelectEmployeeModal_({title:"请选择专家",type:"部门",deptIds:"1", userLoginList: vm.item.expert,multiple:true});
            } else if (vm.status=='attender'){
                $scope.showOaSelectEmployeeModal_({title:"主要参加人",type:"部门",deptIds:"1", userLoginList: vm.item.attender,multiple:true});
            }else if (vm.status=='scientificFirstTrial'){
                $scope.showOaSelectEmployeeModal_({title:"专家委员会常委",type:"角色",roleIds:"132", userLoginList: vm.item.scientificFirstTrial,multiple:true,parentRoleId:'132'});
            }
        };
        //保存选人的login和名字
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='expert'){
                vm.item.expert = $scope.selectedOaUserLogins_;
                vm.item.expertName = $scope.selectedOaUserNames_;
            }else if (vm.status=='attender'){
                vm.item.attender = $scope.selectedOaUserLogins_;
                vm.item.attenderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='scientificFirstTrial'){
                vm.item.scientificFirstTrial = $scope.selectedOaUserLogins_;
                vm.item.scientificFirstTrialName = $scope.selectedOaUserNames_;
            }
        };
        //选部门模块
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //加载
        vm.loadData = function (loadProcess) {
            fiveBusinessAdvanceCollectService.getModelById(advanceCollectId).then(function (value) {
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
            fiveBusinessAdvanceCollectService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.downCollect = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessAdvanceCollectService.downCollect(vm.item.id,vm.item.collectMonth).then(function (response) {
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


        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveBusinessAdvanceCollectService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $scope.refresh();});
                    }
                })

            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }


        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;

                    lodop.PRINT_INIT("月度预支奖金签发单");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.init();

        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //经营发展部-各地公共资源平台备案
    .controller("FiveOaPlatformRecordController", function ($state, $scope,$rootScope, fiveOaPlatformRecordService) {
        var vm = this;
        vm.params = {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0};
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.oaPlatformRecord";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                q: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveOaPlatformRecordService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaPlatformRecordDetail", {recordId: id});
        }

        vm.add = function () {
            fiveOaPlatformRecordService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaPlatformRecordService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
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
    .controller("FiveOaPlatformRecordDetailController", function ($state, $stateParams, $rootScope, $scope, businessRecordService, fiveOaPlatformRecordService) {
        var vm = this;
        var uiSref = "five.oaPlatformRecord";

        var tableName = $rootScope.loadTableName(uiSref);

        var recordId = $stateParams.recordId;

        vm.init = function () {
            vm.loadData(true);
            vm.listDetail();
        }

        vm.loadData = function (loadProcess) {
            fiveOaPlatformRecordService.getModelById(recordId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };
        //合同备案
        vm.showRecordModal = function () {
            fiveOaPlatformRecordService.listRecordByUserLogin(user.userLogin, vm.item.recordId).then(function (value) {
                if (value.data.ret) {
                    vm.listRecord = value.data.data;
                    singleCheckBox(".cb_record", "data-name");
                    $("#selectRecordModal").modal("show");
                }
            })
        };
        vm.saveSelectRecord = function () {
            $("#selectRecordModal").modal("hide");
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                //需要从备案传递到 超前任务单的 数据
                vm.item.recordId = record.id;
                //项目名称
                vm.item.recordName = record.projectName;
                //项目号
                vm.item.projectNo = record.projectNo;
                vm.save();
            }
        }

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaPlatformRecordService.update(vm.item).then(function (value) {
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
                fiveOaPlatformRecordService.update(vm.item).then(function (value) {
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

        vm.addDetail = function () {
            fiveOaPlatformRecordService.getNewModelDetail(recordId).then(function (value) {
                if (value.data.ret) {
                    vm.editDetail(value.data.data);
                    vm.init();
                }
            })
        }

        vm.updateDetail = function () {
            fiveOaPlatformRecordService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModel").modal("hide");
                    vm.listDetail();
                }
            })
        }

        vm.removeDetail = function (id) {
            fiveOaPlatformRecordService.removeDetail(id).then(function (value) {
                if (value.data.ret) {
                    toastr.success("删除成功!");
                    vm.listDetail();
                }
            })
        }

        vm.editDetail = function (id) {
            fiveOaPlatformRecordService.getModelDetailById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                    if (vm.detail.city == null) {
                        vm.provinceId = "0";
                        vm.cityId = "0";
                    } else {
                        vm.provinceId = "" + provinceArr.indexOf(vm.detail.province);
                        vm.cityId = "" + cityArr[vm.provinceId].indexOf(vm.detail.city);
                    }
                    vm.getCityArr = vm.cityArr[vm.provinceId]; //根据索引写入城市数据
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.listDetail = function () {
            fiveOaPlatformRecordService.listDetail(recordId).then(function (value) {
                if (value.data.ret) {
                    vm.detalList = value.data.data;
                }
            })
        }

        vm.provinceArr = provinceArr; //省份数据
        vm.cityArr = cityArr;    //城市数据
        vm.getCityArr = vm.cityArr[0]; //默认为省份
        vm.getCityIndexArr = ['0', '0']; //这个是索引数组，根据切换得出切换的索引就可以得到省份及城市
        //改变省份触发的事件 [根据索引更改城市数据]
        vm.provinceChange = function (index) {
            vm.getCityArr = vm.cityArr[index]; //根据索引写入城市数据
            vm.getCityIndexArr[1] = '0'; //清除残留的数据
            vm.getCityIndexArr[0] = index;
            vm.detail.province = vm.provinceArr[index];
        }
        //改变城市触发的事件
        vm.cityChange = function (index) {
            vm.detail.city = vm.getCityArr[index];
        }

        vm.print = function () {
            fiveOaPlatformRecordService.getPrintData(recordId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");

                    var recordDetails = vm.printData.recordDetails;
                    vm.printDetails = recordDetails;

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
    //经营发展部-投标申请表
    .controller("FiveOaBidApplyController", function ($state, $scope,$rootScope, fiveOaBidApplyService) {
        var vm = this;
        vm.params = {qName: "", pageNum: 1, pageSize: $scope.pageSize, total: 0};
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize, total: vm.params.total};
        var uiSref = "five.oaBidApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {
                qName: vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin: user.userLogin,
                uiSref: uiSref
            };
            fiveOaBidApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaBidApplyDetail", {applyId: id});
        }

        vm.add = function () {
            fiveOaBidApplyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaBidApplyService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
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
    .controller("FiveOaBidApplyDetailController", function ($state, $stateParams, $rootScope, $scope, fiveOaBidApplyService, commonFileService, businessRecordService) {
        var vm = this;
        var uiSref = "five.oaBidApply";

        var tableName = $rootScope.loadTableName(uiSref);

        var applyId = $stateParams.applyId;

        vm.init = function () {
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveOaBidApplyService.getModelById(applyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                       /* setTimeout(function () {
                            vm.lastDisableFlag = true;
                            if ($rootScope.processInstance.myRunningTaskName != '') {
                                vm.lastDisableFlag = $rootScope.processInstance.myRunningTaskName.indexOf("发起人") < 0;
                            }
                        }, 200)*/
                    }
                    $("#bidTime").datepicker('setDate', vm.item.bidTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaBidApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if (send == true && $rootScope.processInstance.myRunningTaskName.indexOf('经营发展部人员（合同）') >= 0) {
                if (vm.item.reviewUser == '') {
                    toastr.warning("请填写评审人员！");
                    return;
                }
            }
            if ($rootScope.processInstance.myRunningTaskName.indexOf('发起人') >= 0 &&
                vm.item.reviewUser == '' && vm.item.bidRank == '非公司级') {
                toastr.warning("请填写评审人员！");
                return;
            }
            if ($("#detail_form").validate().form() && $("#detail_form2").validate().form() && $("#detail_form3").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaBidApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $scope.refresh();
                            setTimeout(function () {
                                if ($rootScope.processInstance.finished) {
                                    fiveOaBidApplyService.updateRecord(applyId).then(function (value) {
                                        toastr.success("备案信息：" + value.data.data + " 投标状态已跟新");
                                    })
                                }
                            }, 500)
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'bidMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择投标申请人",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.bidMan,
                    multiple: true
                });
            } else if (vm.status == 'chargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择投标分管副总",
                    type: "部门",
                    deptIds: "16",
                    parentDeptId: 16,
                    userLoginList: vm.item.chargeMan,
                    multiple: false
                });
            } else if (vm.status == 'reviewUser') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择评审人员",
                    type: "部门",
                    deptIds: "1",
                    userLoginList: vm.item.reviewUser,
                    multiple: true
                });
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'bidMan') {
                vm.item.bidMan = $scope.selectedOaUserLogins_;
                vm.item.bidManName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'chargeMan') {
                vm.item.chargeMan = $scope.selectedOaUserLogins_;
                vm.item.chargeManName = $scope.selectedOaUserNames_;
            } else if (vm.status == 'reviewUser') {
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            }
        };

        //项目信息备案
        vm.showRecordModal = function () {
            fiveOaBidApplyService.listRecordByUserLogin(user.userLogin, vm.item.recordId).then(function (value) {
                if (value.data.ret) {
                    vm.listRecord = value.data.data;
                    singleCheckBox(".cb_record", "data-name");
                }
            })
            vm.showChooseStatus = '项目信息备案-' + vm.item.projectName;
            $("#selectRecordModal").modal("show");
        }

        vm.saveSelectRecordModel = function () {
            if ($(".cb_record:checked").length > 0) {
                var record = $.parseJSON($(".cb_record:checked").first().attr("data-name"));
                //需要同步到 合同评审 的数据
                vm.item.recordId = record.id;
                //项目名称
                vm.item.projectName = record.projectName;
                //投资额
                vm.item.contractMoney = record.projectInvest;
                //信息来源
                vm.item.informationSource = record.investSource;
                //资金来源
                vm.item.moneySource = record.investSource;
                //项目性质
                vm.item.projectType = record.projectType;
                //招标商名称
                vm.item.bidder = record.customerName;
                //联系人
                vm.item.bidderLinkMan = record.linkMan;
                //项目行业
                vm.item.projectIndustry = record.industryType;
                //联系电话
                vm.item.bidderLinkTel = record.linkTel;
                //建设地点
                vm.item.projectAddress = record.projectAddress;
                //建设规模
                vm.item.projectScale = record.projectScale;
                //合同方式
                //vm.item.contractType = record.projectType;
                //项目编号
                vm.item.projectNo = record.projectNo;
                //客户名称
                vm.item.customerName = record.customerName;
                //客户编号
                vm.item.customerCode = record.customerCode;

            }
            vm.save();
            $("#selectRecordModal").modal("hide");
        }
        //非公司级 取消默认评审人员
        vm.changeBidRank = function () {
            if(vm.item.bidRank == '非公司级'){
                vm.item.reviewUser ="";
                vm.item.reviewUserName ="";
            }else{
                vm.item.reviewUser ="4047,2623,2542,2268,";
                vm.item.reviewUserName ="陈琦,封金成,陈乾,裴蕾,";
            }
        }

        vm.print = function () {
            fiveOaBidApplyService.getPrintData(applyId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
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
    //月度外协费用申报
    .controller("FiveBusinessOutAssistController", function ($state, $scope, $rootScope, fiveBusinessOutAssistService) {
        var vm = this;
        var uiSref = "five.businessOutAssist";
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
            fiveBusinessOutAssistService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.businessOutAssistDetail", {outAssistId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBusinessOutAssistService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBusinessOutAssistService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBusinessOutAssistDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveBusinessOutAssistService, fiveBusinessContractLibraryService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessOutAssist";
        var tableName = $rootScope.loadTableName(uiSref);

        var outAssistId = $stateParams.outAssistId;
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBusinessOutAssistService.getModelById(outAssistId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        //$rootScope.getTplConfig("",vm.item.businessKey,user.userLogin);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })

            fiveBusinessOutAssistService.getDetailById(outAssistId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveBusinessOutAssistService.getNewModelDetail(outAssistId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            } else {
                fiveBusinessOutAssistService.getModelDetailById(id).then(function (value) {
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
                    fiveBusinessOutAssistService.removeDetail(id, user.userLogin).then(function (value) {
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
            vm.detail.outAssistId = outAssistId;
            vm.detail.creator = user.userLogin;
            fiveBusinessOutAssistService.updateDetail(vm.detail, outAssistId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadData(true)
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessOutAssistService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("月度外协费用申报");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

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
                fiveBusinessOutAssistService.update(vm.item).then(function (value) {
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
        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: vm.detail.contractId,
                uiSref: uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
            vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
            vm.detail.contractId = $rootScope.selectedLibrary.id;
            vm.detail.projectNo = $rootScope.selectedLibrary.projectNo;
            vm.detail.projectName = $rootScope.selectedLibrary.projectName;
            vm.detail.signTime = $rootScope.selectedLibrary.signTime;
            vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
            vm.detail.customerName = $rootScope.selectedLibrary.customerName;
            $scope.closeLibrarySelectModal_();
        }

        /*        //合同库
                vm.showContractLibraryModal = function () {
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
                        vm.detail.contractName = library.contractName;
                        vm.detail.contractNo = library.contractNo;
                        vm.detail.contractId = library.id;
                        vm.detail.projectNo =library.projectNo;
                        vm.detail.projectName = library.projectName;
                        vm.detail.signTime = library.signTime;
                        vm.detail.contractMoney = library.contractMoney;
                        vm.detail.customerName = library.customerName;
                       // vm.saveDetail();
                    }

                    $("#selectLibraryModal").modal("hide");
                }*/


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

        //导出excel
        vm.downData = function () {
            fiveBusinessOutAssistService.downData(user.userLogin, outAssistId).then(function (response) {
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

        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })

    //经营合作项目确认
    .controller("FiveBusinessCooperationDelegationController", function ($state, $scope, $rootScope, fiveBusinessCooperationDelegationService) {
        var vm = this;
        var uiSref = "five.businessCooperationDelegation";
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
            fiveBusinessCooperationDelegationService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.businessCooperationDelegationDetail", {cooperationDelegationId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBusinessCooperationDelegationService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBusinessCooperationDelegationService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBusinessCooperationDelegationDetailController", function ($state, $stateParams, $scope, $rootScope, commonCodeService, fiveFinanceNodeService, fiveBusinessCooperationDelegationService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessCooperationDelegation";
        var tableName = $rootScope.loadTableName(uiSref);

        var cooperationDelegationId = $stateParams.cooperationDelegationId;
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBusinessCooperationDelegationService.getModelById(cooperationDelegationId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        //$rootScope.getTplConfig("",vm.item.businessKey,user.userLogin);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessCooperationDelegationService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }


        //项目编号
        vm.getInteriorProjectNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessCooperationDelegationService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    fiveBusinessCooperationDelegationService.getInteriorProjectNo(cooperationDelegationId).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("内部项目号已更新!");
                        }
                    })
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
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("经营合作项目确认");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

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
                fiveBusinessCooperationDelegationService.update(vm.item).then(function (value) {
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
            if (vm.deptType == "delegation") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择委托方",
                    type: '选部门',
                    deptIds: '1',
                    deptIdList: vm.item.delegationDeptId,
                    parentDeptId: vm.item.delegationDeptId,
                    multiple: false,
                    bigDept:true
                });
            } else if (vm.deptType == "cooperation") {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择协作方",
                    type: '选部门',
                    deptIds: '1',
                    deptIdList: vm.item.cooperationDeptId,
                    multiple: false,
                    bigDept:true
                });
            }

        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptType == 'delegation') {
                vm.item.delegationDeptName = $scope.selectedOaDeptNames_;
                vm.item.delegationDeptId = Number($scope.selectedOaDeptIds_);
            } else if (vm.deptType == 'cooperation') {
                vm.item.cooperationDeptName = $scope.selectedOaDeptNames_;
                vm.item.cooperationDeptId = Number($scope.selectedOaDeptIds_);
            }

        }

        //合同库
        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: vm.detail.contractId,
                uiSref: uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
            vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
            vm.detail.contractId = $rootScope.selectedLibrary.id;
            vm.detail.projectNo = $rootScope.selectedLibrary.projectNo;
            vm.detail.projectName = $rootScope.selectedLibrary.projectName;
            vm.detail.signTime = $rootScope.selectedLibrary.signTime;
            vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
            vm.detail.customerName = $rootScope.selectedLibrary.customerName;
            $scope.closeLibrarySelectModal_();
        }

        //项目信息备案
        vm.showSelectRecordModel = function () {
            $scope.showRecordSelectModal_({
                selectRecordId: vm.item.recordId,
                uiSref: uiSref,
                multiple: false
            });
        }
        $rootScope.saveSelectRecord_ = function () {
            //需要从备案传递到 超前任务单的 数据
            vm.item.recordId = $rootScope.selectedRecord.id;
            //项目名称
            vm.item.projectName = $rootScope.selectedRecord.projectName;
            //项目号
            vm.item.projectNo = $rootScope.selectedRecord.projectNo;
            $scope.closeRecordSelectModal_();
            vm.save();
        }

        
        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })

    //内部协作协议合同
    .controller("FiveBusinessCooperationContractController", function ($state, $scope, $rootScope, fiveBusinessCooperationContractService) {
        var vm = this;
        var uiSref = "five.businessCooperationContract";
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
            fiveBusinessCooperationContractService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key, vm.params, vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.businessCooperationContractDetail", {cooperationContractId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            fiveBusinessCooperationContractService.getNewModel(userLogin, uiSref).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveBusinessCooperationContractService.remove(id, user.userLogin).then(function (value) {
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
    .controller("FiveBusinessCooperationContractDetailController", function ($state, $stateParams, $scope, $rootScope,fiveBusinessCooperationDelegationService , commonCodeService, fiveFinanceNodeService, fiveBusinessCooperationContractService, fiveBusinessContractLibraryService,commonPrintTableService) {
        var vm = this;
        var uiSref = "five.businessCooperationContract";
        var tableName = $rootScope.loadTableName(uiSref);

        var cooperationContractId = $stateParams.cooperationContractId;
        vm.init = function () {
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveBusinessCooperationContractService.getModelById(cooperationContractId).then(function (value) {
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
            fiveBusinessCooperationContractService.update(vm.item).then(function (value) {
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
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("内部协作协议合同");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

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
                fiveBusinessCooperationContractService.update(vm.item).then(function (value) {
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
        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId: vm.detail.contractId,
                uiSref: uiSref,
                multiple: false
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.detail.contractName = $rootScope.selectedLibrary.contractName;
            vm.detail.contractNo = $rootScope.selectedLibrary.contractNo;
            vm.detail.contractId = $rootScope.selectedLibrary.id;
            vm.detail.projectNo = $rootScope.selectedLibrary.projectNo;
            vm.detail.projectName = $rootScope.selectedLibrary.projectName;
            vm.detail.signTime = $rootScope.selectedLibrary.signTime;
            vm.detail.contractMoney = $rootScope.selectedLibrary.contractMoney;
            vm.detail.customerName = $rootScope.selectedLibrary.customerName;
            $scope.closeLibrarySelectModal_();
        }

        //项目编号
        vm.getInteriorContractNo = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveBusinessCooperationContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    fiveBusinessCooperationContractService.getInteriorContractNo(cooperationContractId).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                            toastr.success("内部项目号已更新!");
                        }
                    })
                }
            })
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

        vm.showSelectDelegationModel = function () {
            fiveBusinessCooperationDelegationService.selectAll(user.userLogin, "five.businessCooperationDelegation").then(function (value) {
                vm.delegations = value.data.data;
                singleCheckBox(".cb_delegation", "data-name");
            });
            $("#selectDelegationModal").modal("show");
        }
        vm.saveSelectDelegation = function () {
            $("#selectDelegationModal").modal("hide");
            if ($(".cb_delegation:checked").length > 0) {
                var delegation = $.parseJSON($(".cb_delegation:checked").first().attr("data-name"));
                vm.item.cooperationDelegationId = delegation.id;

                vm.item.delegationDeptId = delegation.delegationDeptId;
                vm.item.delegationDeptName = delegation.delegationDeptName;
                vm.item.cooperationDeptId = delegation.cooperationDeptId;
                vm.item.cooperationDeptName = delegation.cooperationDeptName;

                vm.item.cooperationProjectName = delegation.interiorProjectName;
                vm.item.cooperationProjectNo = delegation.interiorProjectNo;
                vm.item.cooperationProjectType = delegation.interiorProjectType;
            }
            vm.save();
        }



        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })


