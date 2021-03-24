angular.module('controllers.me', [])


    .controller("MeEdProjectController", function ($state, $scope,businessContractService) {
        var vm = this;
        var uiSref="me.edProject";

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{q: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};


        $scope.basicInit();

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            businessContractService.listAttendPagedData(
                {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,q:vm.params.q}
                ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };

        vm.show = function (item) {
            $state.go("five.detail.show", {id: item.id});
        }

        vm.loadPagedData();

        return vm;
    })

    .controller("MeAllController", function ($state, $scope,businessContractService) {
        var vm = this;
        var uiSref="me.all";
        var key = $state.current.name + "_" + user.userLogin;
        vm.appCode=user.appCode;
        vm.params = getCacheParams(key,{q: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,uiSref:uiSref,total:vm.params.total};

        $scope.basicInit();

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

        };

        vm.downloadModelExcel =function(){

            businessContractService.downloadModelSimpleExcel(user.userLogin).then(function (response) {
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

        vm.loadPagedData = function () {
            businessContractService.listAllPagedData(
                {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,q:vm.params.q}
                ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (item) {
            $state.go("five.detail.show", {id: item.id});
        }

        vm.loadPagedData();

        return vm;
    })

    .controller("MeFiveAllProjectController", function ($state, $scope,businessContractService) {
        var vm = this;


        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{q: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="me.all";
        $scope.basicInit();

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

        };

        vm.loadPagedData = function () {
            businessContractService.listAllPagedData(
                {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,q:vm.params.q}
            ).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (item) {
            $state.go("five.detail.show", {id: item.id});
        }

        vm.loadPagedData();

        return vm;
    })

    .controller("MeCpStepController", function ($state, $scope, edProjectStepService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qProjectName: "", qStepName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        $scope.basicInit();

        vm.queryData=function(){
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }


        vm.loadPagedData=function(){
            var params={userLogin:user.userLogin,qSignTime:vm.params.qSignTime,
                qProjectName:vm.params.qProjectName,qStepName:vm.params.qStepName,pageNum:vm.pageInfo.pageNum,pageSize:vm.pageInfo.pageSize}

            edProjectStepService.listPagedAttendStep(params).then(function (value) {
                if (value.data.ret){
                    vm.pageInfo=value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        }

        vm.updateCadHide=function(id){
            edProjectStepService.toggleCadHide(id,user.userLogin).then(function (value) {
                if (value.data.ret){
                    vm.loadPagedData();
                }
            })
        }

        vm.showCp=function(id){
            $state.go("cp.detail", {stepId: id});
        }

        vm.loadPagedData();
        return vm;
    })

    .controller("MeDetailPartialStampAllController", function ($state, $stateParams, $scope, edStampService,actService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qProjectName: "", qDocType: "",pageNum: 1, pageSize: $scope.pageSize,total:0});

        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        var uiSref='me.stampAll';

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        }

        vm.queryData=function(){
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        }

        vm.loadPagedData=function(){
            var params = {uiSref:uiSref, qProjectName: vm.params.qProjectName, qDocType: vm.params.qDocType, userLogin:user.userLogin,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            edStampService.listAllStamp(params).then(function (value) {
                if(value.data.ret){
                    vm.pageInfo=value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        }

        vm.show=function(id){
            $state.go("me.stampAllDetail",{stampId:id});
        }

        vm.add=function(){
           vm.show(0);
        }
        vm.showDetail=function(businessKey){
            actService.getNgRedirectUrl(businessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    $state.go(result.url,result.params);
                }
            })
        }
        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    edStampService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }

        vm.loadPagedData();

        return vm;
    })

    .controller("MeDetailPartialStampAllDetailController", function ($state,$rootScope, $stateParams, $scope, edStampService,hrDeptService,edProjectStepService,edArrangeUserService) {
        var vm = this;
        var stampId = $stateParams.stampId;
        var uiSref='me.stampAll';



        vm.loadData=function(loadProcess) {
            $scope.loadRightData(user.userLogin, uiSref);
            if (stampId == 0) {
                edStampService.getNewModelAll(user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.item = value.data.data;
                        $scope.basicInit();
                        $rootScope.historyList='';
                        $rootScope.processInstance='';
                        $("#useTime").datepicker('setDate', vm.item.useTime);
                    }
                })
            } else {
                edStampService.getModelById(stampId).then(function (value) {
                    if (value.data.ret) {
                        vm.item = value.data.data;
                        if (loadProcess) {
                            $scope.basicInit(vm.item.businessKey);
                            $scope.loadProcessInstance(vm.item.processInstanceId);
                            $("#useTime").datepicker('setDate', vm.item.useTime);
                        }
                    }
                });
            }
        };

        vm.showMajorModal=function(){
            if (vm.item.stepId!=0){
                edArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                    if (value.data.ret){
                        vm.majors=value.data.data;
                    }
                })
            }
            $("#listMajorsModal").modal("show");
        }
        vm.saveMajor=function() {
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.majors = value.join(',');

            $("#listMajorsModal").modal("hide");
        }
        vm.save=function(){
            vm.item.operateUserLogin=user.userLogin;
            edStampService.update(vm.item).then(function (value) {
                if(value.data.ret){
                    stampId=value.data.data;
                    vm.loadData(true);
                    toastr.success("保存成功!");
                }
            })
        };

        vm.listDeptSteps=function(){
            edProjectStepService.listAllStepByDeptId(vm.item.deptId).then(function (value) {
                if (value.data.ret){
                    vm.stepList=value.data.data;
                    singleCheckBox(".cb_step","data-name");
                    $("#stepModal").modal("show");
                }
            })
        }

        vm.saveStep=function(){
            if($(".cb_step:checked").length>0){
                var step= $.parseJSON($(".cb_step:checked").first().attr("data-name"));
                vm.item.stepId=step.id;
                vm.item.projectName=step.projectName;
                vm.item.stepName=step.stepName;
                vm.item.stageName=step.stageName;
                vm.item.contractMoney=step.contractMoney;
                vm.item.projectId=step.projectId;
                $("#stepModal").modal("hide");
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
        vm.showStampModel=function(){
            edStampService.listStampCode().then(function (value) {
                if(value.data.ret){
                    vm.useStamps=value.data.data;
                    $("#selectStampModal").modal("show");
                }
            })

        }
        vm.saveSelectStamp=function(){
            var value = [];
            $(".cb_stamp:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.useStamp = value.join(',');
            $("#selectStampModal").modal("hide");
        }

        vm.print=function () {
            edStampService.getPrintData(stampId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("其他用印申请表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //发送流程
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edStampService.update(vm.item).then(function (value) {
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
        vm.loadData(true);
        return vm;
    })

    .controller("MeDetailPartialStampNoDetailController", function ($state, $stateParams, $scope, edStampService) {
        var vm = this;
        var stampId = $stateParams.stampId;

        vm.loadData=function(loadProcess){
            edStampService.getModelById(stampId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                    if(loadProcess) {
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                    }
                    $("#useTime").datepicker('setDate', vm.item.useTime);
                }
            })

        };
        vm.save=function(){
            vm.item.operateUserLogin=user.userLogin;
            edStampService.update(vm.item).then(function (value) {
                if(value.data.ret){
                    vm.loadData(false);
                    toastr.success("保存成功!");
                }
            })
        };
        vm.showStampModel=function(){
            edStampService.listStampCode().then(function (value) {
                if(value.data.ret){
                    vm.useStamps=value.data.data;
                    $("#selectStampModal").modal("show");
                }
            })

        }
        vm.saveSelectStamp=function(){
            var value = [];
            $(".cb_stamp:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.useStamp = value.join(',');
            $("#selectStampModal").modal("hide");
        }
        //发送流程
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    edStampService.update(vm.item).then(function (value) {
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

        vm.print=function () {
            edStampService.getPrintData(stampId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("其他用印申请表");
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

    .controller("FiveMeStampController", function ($state,$stateParams ,$scope, oaStampApplyService,commonCodeService) {
        var vm = this;
        vm.keyWords="成果用印审批" ;
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.params={types:'施工图报审章,施工图专用章,压力管道章,合同章',fileNames:''}

        var uiSref='five.meStamp';

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData(true);
            vm.loadCommonCode();

        }

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,types: vm.params.types,fileNames:vm.params.fileNames};
            oaStampApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.loadCommonCode=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"印章审批类型").then(function(value){
                if (value.data.ret){
                    vm.stampList=value.data.data;
                }
            })
        }

        vm.add = function () {
            oaStampApplyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.showStamp=function(){
            singleCheckBox(".cb_stamp","data-name");
            $("#stampModal").modal("show");
        }

        vm.saveStamp=function(){
            var types=[];
            $(".cb_stamp:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#stampModal").modal("hide");
            var type = types.join(',');
            oaStampApplyService.getNewModelByStampName(user.userLogin,type).then(function (value) {
                if (value.data.ret){
                    vm.show(value.data.data);
                }
            });

        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaStampApplyService.remove(id,user.userLogin).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.meStampDetail",{stampId: id});
        }

        vm.init();

        return vm;
    })

    .controller("FiveMeStampDetailController", function ($state,$stateParams,$rootScope ,$scope, oaStampApplyService,commonCodeService) {
        var vm = this;
        vm.keyWords="成果用印审批";
        var stampId = $stateParams.stampId;
        var uiSref="five.meStamp";

        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:0};

        vm.init=function(){
            $scope.loadRightData(user.userLogin, uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            oaStampApplyService.getModelById(stampId).then(function (value) {
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
            oaStampApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                oaStampApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
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

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司领导", type:"部门",userLoginList: vm.item.companyLeader,multiple:true,deptIds:"16,59,72",parentDeptId:16});
            }else if (vm.status=='viceLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择副职领导领导", type:"部门",userLoginList: vm.item.viceLeader,multiple:false,deptIds:"16",parentDeptId:16});
            }else if (vm.status=='qualityCompanyMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择压力管道技术员", type:"角色",userLoginList: vm.item.qualityCompanyMan,multiple:false,roleIds:",67" ,parentRoleId:"67"});
            }else if (vm.status=='contractSealMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择合同评审人员", type:"角色",userLoginList: vm.item.contractSealMan,multiple:false,roleIds:",68" ,parentRoleId:"68"});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='viceLeader'){
                vm.item.viceLeader = $scope.selectedOaUserLogins_;
                vm.item.viceLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='qualityCompanyMan'){
                vm.item.qualityCompanyMan = $scope.selectedOaUserLogins_;
                vm.item.qualityCompanyManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='contractSealMan'){
                vm.item.contractSealMan = $scope.selectedOaUserLogins_;
                vm.item.contractSealManName = $scope.selectedOaUserNames_;
            }

        };

        vm.showDeptModal=function() {
            $scope.showOaSelectEmployeeModal_({title:"请选择归口管理部门",type:"选部门", deptIdList: vm.item.functionDeptId+"",
                multiple:false,deptIds:"1",parentDeptId:1});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.functionDeptName = $scope.selectedOaDeptNames_;
            vm.item.functionDeptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showStampModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"印章审批类型").then(function(value){
                if (value.data.ret){
                    vm.stampList=value.data.data;
                    $("#stampModal").modal("show");
                }
            })
        }

        vm.saveStamp=function(){
            var type=[];
            $(".cb_stamp:checked").each(function () {
                type.push($(this).attr("data-name"));
            });
            vm.item.stampName = type.join(',');
            $("#stampModal").modal("hide");
        }

        vm.selectAllFile=function(){
            $(".cb_stamp").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_stamp").each(function () {
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

        vm.print=function () {
            oaStampApplyService.getPrintData(stampId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");

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
