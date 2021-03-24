angular.module('controllers.business', [])

    .controller("BusinessCustomerController", function ($state, $scope, $stateParams, businessCustomerService) {
        var vm = this;
        var uiSref="business.customer";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{name: "", qName: "",code:"",qCode:"",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.init = function () {
            vm.queryData();
            $scope.basicInit("");
            $scope.loadRightData(user.userLogin,uiSref);

            $("#uploadModelFile").fileupload({
                dataType: 'text',
                url:'/business/customer/receive.do?userLogin='+user.userLogin,
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
                        vm.queryData();
                    } else {
                        toastr.error(result.msg);
                    }
                }
            });
        };

        vm.queryData = function () {
            vm.params.name = vm.params.qName;
            vm.params.code = vm.params.qCode;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            businessCustomerService.loadPagedData(
                {qName: vm.params.name,qCode:vm.params.code,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin}
                ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        }


        vm.show = function (customerId) {
            $state.go("business.customerDetail", {customerId: customerId});
        }

        vm.add = function () {
            businessCustomerService.getNewModel(user.userLogin,uiSref).then(function (value) {
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

        vm.downloadModel = function() {
            businessCustomerService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=","");
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
    .controller("BusinessCustomerDetailController", function ($state, $stateParams, $scope, businessCustomerService,hrDeptService) {
        var vm = this;
        var customerId = $stateParams.customerId;

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"business.customer");
        }

        vm.loadData = function (loadProcess) {

            businessCustomerService.getModelById(customerId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            businessCustomerService.listCooperationProject(customerId).then(function (value) {

                vm.list=value.data.data;
            })

        };

        vm.save = function () {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;

                businessCustomerService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!");
                    }
                })
            }
        };

        vm.showDeptModal=function(id) {
            $('#dept_select_tree').jstree("destroy");
            var deptIds=$scope.rightData.selectDepts;
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
                    if (id == item.id) {
                        node.state.selected = true;
                    }
                    if(deptIds.indexOf(','+item.id+',')<0){
                        node.state.disabled=true;
                    }
                    vm.treeData.push(node);
                }


                $('#dept_select_tree').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModal").modal("show");
        }

        vm.saveDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.deptId = dept.id;
            vm.item.deptName = dept.text;
            $("#deptSelectModal").modal("hide");
        }


        vm.showContract = function (contractId) {
            $state.go("business.contractDetail", {contractId: contractId});
        }

        vm.init();
        return vm;
    })

    .controller("BusinessDeptContractController", function ($state, $scope, businessContractService) {
        var vm = this;
        vm.params = {name: "", qName: "", qCName: "",deptId:user.deptId};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        vm.init = function () {
            vm.queryData();
        };

        vm.queryData = function () {
            vm.params.name = vm.params.qName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {q: vm.params.name, deptId:user.deptId,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            businessContractService.loadPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })

        }

        vm.show = function (contractId) {
            $state.go("business.contractDetail", {contractId: contractId});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessContractService.getNewModel(userLogin).then(function (value) {
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

    .controller("BusinessBidApplyController", function ($state, $scope,businessBidApplyService,businessBidAttendService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qName:"",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.queryData=function(){
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            businessBidApplyService.listPagedData(
                {q: vm.params.qName,userLogin:user.userLogin,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize}
                ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        }

        vm.show = function (id) {
            $state.go("business.bidApplyDetail", {bidApplyId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessBidApplyService.getNewModel(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }
        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessBidApplyService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.addAttend=function(id){
            businessBidAttendService.getNewModelByApplyId(id,user.userLogin).then(function (value) {
                if (value.data.ret){
                    $state.go("business.bidAttendDetail", {attendId: value.data.data});
                }
            })
        }

        vm.loadPagedData();

        return vm;
    })

    .controller("BusinessBidApplyDetailController", function ($state,$stateParams,$scope,businessBidApplyService,businessCustomerService) {
        var vm = this;
        var bidApplyId = $stateParams.bidApplyId;

        vm.loadData = function (loadProcess) {
            businessBidApplyService.getModelById(bidApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#openTime").datepicker('setDate', vm.item.openTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessBidApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.selectCustomer=function(){
            vm.qCustomer="";
            businessCustomerService.selectAll(user.userLogin).then(function (value) {
                vm.customers=value.data.data;
                singleCheckBox(".cb_customer","data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer=function(){
            $("#selectCustomerModal").modal("hide");
            if($(".cb_customer:checked").length>0){
                vm.item.customerName=$(".cb_customer:checked").first().attr("data-name");
            }
        }

        vm.listAgent=function(){
            vm.qAgent="";
            businessBidApplyService.listAgent().then(function (value) {
                if (value.data.ret){
                    vm.agents=value.data.data;
                    singleCheckBox(".cb_agent","data-name");
                }
            })
            $("#selectAgentModal").modal("show");
        }
        vm.saveSelectAgent=function(){
            $("#selectAgentModal").modal("hide");
            if($(".cb_agent:checked").length>0){
                vm.item.agentName=$(".cb_agent:checked").first().attr("data-name");
            }
        }

        vm.listRecord=function(){
            vm.deptName=vm.item.deptName;
            businessBidApplyService.listAllUnExistRecord(vm.item.id).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                    singleCheckBox(".cb_record", "data-name");
                    $("#recordModel").modal("show");
                }
            })
        }

        vm.chooseRecord=function(){
            var record='';
            $(".cb_record:checked").each(function () {
                record=$.parseJSON($(this).attr("data-name"));
            });
            vm.item.recordId= record.id;
            vm.item.projectName=record.projectName;
            vm.item.customerName=record.customerName;
            vm.item.projectScale=record.projectScale;
            vm.item.openTime=record.bidRealOpen;
            vm.item.projectType=record.businessContent;
            $("#recordModel").modal("hide");
        }


        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    businessBidApplyService.update(vm.item).then(function (value) {
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


        /*打印*/
        vm.print=function () {
            businessBidApplyService.getPrintData(bidApplyId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("项目投标报名申请表");
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

    .controller("BusinessBidAttendController", function ($state, $scope,businessBidAttendService,businessBidProjectChargeService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.queryData=function(){
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            businessBidAttendService.listPagedData(
                {q: vm.params.qName,userLogin:user.userLogin,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize}
                ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        }

        vm.show = function (id) {
            $state.go("business.bidAttendDetail", {attendId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessBidAttendService.getNewModel(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessBidAttendService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.addProjectCharge=function(id){
            businessBidProjectChargeService.getNewModelById(id,user.userLogin).then(function (value) {
                if (value.data.ret){
                    $state.go("business.bidProjectChargeDetail", {projectChargeId: value.data.data});
                }
            })
        }

        vm.loadPagedData();

        return vm;


    })
    .controller("BusinessBidAttendDetailController", function ($state,$stateParams,$scope,businessBidAttendService,businessCustomerService,edFileService) {

        var vm = this;
        var attendId = $stateParams.attendId;

        vm.loadData = function (loadProcess) {
            businessBidAttendService.getModelById(attendId).then(function (value) {
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
            businessBidAttendService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.listApply=function(){
            vm.deptName=vm.item.deptName;
            businessBidAttendService.listAllUnExistApply(vm.item.id).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                    singleCheckBox(".cb_bigApply", "data-name");
                    $("#bigApply").modal("show");
                }
            })
        }

        vm.chooseApply=function(){
            var apply='';
            $(".cb_bigApply:checked").each(function () {
               apply=$.parseJSON($(this).attr("data-name"));
            });
            vm.item.bidApplyId= apply.id;
            vm.item.projectName=apply.projectName;
            vm.item.customerName=apply.customerName;
            vm.item.businessType=apply.businessType;
            vm.item.attendType=apply.attendType;
            vm.item.projectScale=apply.projectScale;
            vm.item.openTime=apply.openTime;
            vm.item.projectType=apply.projectType;
            $("#bigApply").modal("hide");
        }

        vm.selectCustomer=function(){
            vm.qCustomer="";
            businessCustomerService.selectAll(user.userLogin).then(function (value) {
                vm.customers=value.data.data;
                singleCheckBox(".cb_customer","data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer=function(){
            $("#selectCustomerModal").modal("hide");
            if($(".cb_customer:checked").length>0){
                vm.item.customerName=$(".cb_customer:checked").first().attr("data-name");
            }
        }

        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    businessBidAttendService.update(vm.item).then(function (value) {
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

        vm.showFile = function (item) {
            vm.file = $.extend({}, item);
            $("#edFileModal").modal("show");
        }

        vm.saveFile = function () {
            $("#edFileModal").modal("hide");
            edFileService.updateFileType(vm.file.id, vm.file.fileType, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }


        /*打印*/
        vm.print=function () {
            businessBidAttendService.getPrintData(attendId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("招标文件标前评审表");
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

    .controller("BusinessBidProjectChargeController", function ($state, $scope,businessBidProjectChargeService,businessBidContractService) {

        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qName:"",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.queryData=function(){
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            businessBidProjectChargeService.listPagedData(
                {q: vm.params.qName,userLogin:user.userLogin,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize}
                ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("business.bidProjectChargeDetail", {projectChargeId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessBidProjectChargeService.getNewModel(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessBidProjectChargeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.addContract=function(id){
            businessBidContractService.getNewModelById(id,user.userLogin).then(function (value) {
                if (value.data.ret){
                    $state.go("business.bidContractDetail", {contractId: value.data.data});
                }
            })
        }

        vm.loadPagedData();

        return vm;
    })
    .controller("BusinessBidProjectChargeDetailController", function ($rootScope,$state,$stateParams, hrEmployeeService,$scope,businessBidProjectChargeService,businessCustomerService) {

        var vm = this;
        var projectChargeId = $stateParams.projectChargeId;

        vm.loadData = function (loadProcess) {
            businessBidProjectChargeService.getModelById(projectChargeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#openTime").datepicker('setDate', vm.item.openTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessBidProjectChargeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    businessBidProjectChargeService.update(vm.item).then(function (value) {
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
        /*打印*/
        vm.print=function () {
            businessBidProjectChargeService.getPrintData(projectChargeId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("项目负责人确认表");
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

        vm.showSelectEmployeeModal=function(){
/*            vm.qEmployeeDeptName=vm.item.deptName;
            hrEmployeeService.selectAll().then(function(value) {
                vm.employees=value.data.data;
                singleCheckBox(".cb_employee","data-name");
            });
            if (vm.optUserType == "勘察") {
               vm.titleName='勘察负责人任命';
            } else if (vm.optUserType == "设计") {
                vm.titleName='设计负责人任命';
            }else if (vm.optUserType == "施工") {
                vm.titleName='施工负责人任命';
            }
            $("#selectEmployeeModal").modal("show")*/;

            $scope.showSelectEmployeeModal_({title:"请选择"+vm.optUserType+"负责人任命", userLoginList: vm.optUser,multiple:true});
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();

            if (vm.optUserType == "勘察") {
                vm.item.exploreMan = $scope.selectedUserLogins_;
                vm.item.exploreManName = $scope.selectedUserNames_;
            } else if (vm.optUserType == "设计") {
                vm.item.designMan = $scope.selectedUserLogins_;
                vm.item.designManName = $scope.selectedUserNames_;
            }else if (vm.optUserType == "施工") {
                vm.item.constructionMan = $scope.selectedUserLogins_;
                vm.item.constructionManName = $scope.selectedUserNames_;
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

            if (vm.optUserType == "勘察") {
                vm.item.exploreMan = logins.join(",");
                vm.item.exploreManName = names.join(",");
            } else if (vm.optUserType == "设计") {
                vm.item.designMan = logins.join(",");
                vm.item.designManName = names.join(",");
            }else if (vm.optUserType == "施工") {
                vm.item.constructionMan = logins.join(",");
                vm.item.constructionManName = names.join(",");
            }
            $("#selectEmployeeModal").modal("hide");
        };

        vm.selectCustomer=function(){
            vm.qCustomer="";
            businessCustomerService.selectAll(user.userLogin).then(function (value) {
                vm.customers=value.data.data;
                singleCheckBox(".cb_customer","data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer=function(){
            $("#selectCustomerModal").modal("hide");
            if($(".cb_customer:checked").length>0){
                vm.item.customerName=$(".cb_customer:checked").first().attr("data-name");
            }
        }

        vm.listAttend=function() {
            vm.deptName=vm.item.deptName;
            businessBidProjectChargeService.listAllUnExistAttend(vm.item.id).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                    singleCheckBox(".cb_bigAttend","data-name");
                    $("#bigAttend").modal("show");
                }
            })
        }
        vm.chooseAttend=function() {
            var attend='';
            $(".cb_bigAttend:checked").each(function () {
                attend=$.parseJSON($(this).attr("data-name"));
            });
            vm.item.bidAttendId=attend.id;
            vm.item.projectName=attend.projectName;
            vm.item.projectAddress=attend.projectAddress;
            vm.item.projectScale=attend.projectScale;
            vm.item.projectType=attend.projectType;
            vm.item.openTime=attend.openTime;
            vm.item.customerName=attend.customerName;
            $("#bigAttend").modal("hide");
        }
        return vm;

    })

    .controller("BusinessBidContractController", function ($state, $scope,businessBidContractService,businessContractService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});

        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.queryData=function(){
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {

            var params = {q: vm.params.qName,userLogin:user.userLogin,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            businessBidContractService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);

                }
            })
        }

        vm.show = function (id) {
            $state.go("business.bidContractDetail",{contractId: id});
        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessBidContractService.getNewModel(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    businessBidContractService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.addContract=function(bigContractId){
            businessContractService.getNewModelById(user.userLogin,bigContractId).then(function (value) {

                $state.go("business.contractDetail", {contractId: value.data.data});
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("BusinessBidContractDetailController", function ($state, $stateParams,$scope,businessBidContractService,businessBidAttendService,businessCustomerService) {
        var vm = this;
        var contractId = $stateParams.contractId;

        vm.loadData = function (loadProcess) {
            businessBidContractService.getModelById(contractId).then(function (value) {
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
            businessBidContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showSendFlow=function () {

            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    businessBidContractService.update(vm.item).then(function (value) {
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
        /*打印*/
        vm.print=function () {
            businessBidContractService.getPrintData(contractId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("经营(经济)合同评审表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.listProjectCharge=function() {
            vm.deptName=vm.item.deptName;
            businessBidContractService.listAllUnExistProjectCharge(vm.item.id).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                    singleCheckBox(".cb_bigProjectCharge","data-name");
                    $("#projectCharge").modal("show");
                }
            })
        }

        vm.chooseProjectCharge=function() {
            var projectCharge='';
            $(".cb_bigProjectCharge:checked").each(function () {
                projectCharge=$.parseJSON($(this).attr("data-name"));
                vm.item.bidProjectChargeId=projectCharge.id;
                vm.item.projectContent=projectCharge.projectContent;
                vm.item.projectName=projectCharge.projectName;
                vm.item.projectScale=projectCharge.projectScale;
                vm.item.contractType=projectCharge.projectType;
                vm.item.projectAddress=projectCharge.projectAddress;
                vm.item.customerName=projectCharge.customerName;
                if(projectCharge.bidAttendId>0) {
                    businessBidAttendService.getModelById(projectCharge.bidAttendId).then(function (value) {
                        var attend = value.data.data;
                        vm.item.bidAttendId = attend.id;
                        vm.item.projectTime = attend.projectTime;
                        vm.item.projectQuality = attend.qualityRequest;
                        vm.item.paymentRule = attend.paymentRule;
                        vm.item.businessType = attend.businessType;
                        vm.item.bidApplyId = attend.bidApplyId;
                    })
                }

                $("#projectCharge").modal("hide");
            });

        }

        vm.selectCustomer=function(){
            vm.qCustomer="";
            businessCustomerService.selectAll(user.userLogin).then(function (value) {
                vm.customers=value.data.data;
                singleCheckBox(".cb_customer","data-name");
            });
            $("#selectCustomerModal").modal("show");
        }

        vm.saveSelectCustomer=function(){
            $("#selectCustomerModal").modal("hide");
            if($(".cb_customer:checked").length>0){
                vm.item.customerName=$(".cb_customer:checked").first().attr("data-name");
            }
        }


        vm.loadData(true);

        return vm;

    })

    .controller("BusinessRecordController", function ($state, $scope,businessRecordService,businessBidApplyService) {
        var vm = this;
        var uiSref="business.record";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{ qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.queryData=function(){
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            businessRecordService.listPagedData(
                {q: vm.params.qName,userLogin:user.userLogin,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize}
                ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        }

        vm.show = function (id) {
            $state.go("business.recordDetail", {recordId: id});

        }

        vm.add = function () {
            var userLogin = user.userLogin;
            businessRecordService.getNewModel(userLogin,uiSref).then(function (value) {
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

        vm.addApply=function(id){
            businessBidApplyService.getNewModelById(id,user.userLogin).then(function (value) {
                if (value.data.ret){
                    $state.go("business.bidApplyDetail", {bidApplyId: value.data.data});
                }
            })
        }

        vm.downloadModel = function() {
            businessRecordService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=","");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        $("#uploadModelFile").fileupload({
            dataType: 'text',
            url:'/business/record/receive.do?userLogin='+user.userLogin,
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

        vm.loadPagedData();

        return vm;
    })

    .controller("BusinessRecordDetailController", function ($rootScope,$state,$stateParams,$scope,businessRecordService,businessCustomerService,hrEmployeeService,hrDeptService) {
        var vm = this;
        var recordId = $stateParams.recordId;

        vm.init=function(){
            $scope.loadRightData(user.userLogin,"business.record");
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
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            businessRecordService.update(vm.item,"business.customer").then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.selectCustomer=function(){
            vm.qCustomer="";
            businessCustomerService.selectAll(user.userLogin).then(function (value) {
                vm.customers=value.data.data;
                singleCheckBox(".cb_customer","data-name");
            });
            $("#selectCustomerModal").modal("show");


        }


        vm.saveSelectCustomer=function(){
            $("#selectCustomerModal").modal("hide");
            if($(".cb_customer:checked").length>0){
                var customer= $.parseJSON($(".cb_customer:checked").first().attr("data-name"));
                vm.item.customerName= customer.name;
                vm.item.linkMan= customer.linkMan;
                vm.item.linkTel= customer.linkTel;
                vm.item.customerAddress= customer.address;
                vm.item.linkTitle=customer.linkTitle;
            }
        }

        vm.showUserModel = function () {
/*            vm.qEmployeeDeptName=vm.item.deptName;
            hrEmployeeService.selectAll().then(function(value) {
                vm.employees=value.data.data;
            });
            $("#selectEmployeeModal").modal("show");*/
            $scope.showSelectEmployeeModal_({title:"请选择参与经营人员", userLoginList: vm.item.businessUser,multiple:true});
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            vm.item.businessUserName = $scope.selectedUserNames_;
            vm.item.businessUser = $scope.selectedUserLogins_;
        };

        vm.selectUser = function () {
            var logins = [];
            var names = []
            $(".cb_employee:checked").each(function () {
                var info = $(this).attr("data-name");
                logins.push(info.split('_')[0]);
                names.push(info.split('_')[1]);
            });
            vm.item.businessUser = logins.join(",");
            vm.item.businessUserName = names.join(",");

            $("#selectEmployeeModal").modal("hide");
        }

        vm.showSendFlow=function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    businessRecordService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $('#dept_select_tree').jstree("destroy");
            var deptIds=$scope.rightData.selectDepts;
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
                    if (id == item.id) {
                        node.state.selected = true;
                    }
                    if(deptIds.indexOf(','+item.id+',')<0){
                        node.state.disabled=true;
                    }
                    vm.treeData.push(node);
                }


                $('#dept_select_tree').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModal").modal("show");
        }

        vm.saveDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.deptId= dept.id;
            vm.item.deptName=dept.text;
            $("#deptSelectModal").modal("hide");
        }

        /*打印*/
        vm.print=function () {
            businessRecordService.getPrintData(recordId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("项目信息备案");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.init();

        return vm;

    })
