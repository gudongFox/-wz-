angular.module('controllers.five.hr', [])

    .controller("FiveHrQualifyController", function ($state, $scope, fiveCommonCodeService, hrDeptService,hrQualifyService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var selectId=0;

        vm.init = function () {
            vm.params =getCacheParams(key,{qDeptName: "", qUserName: "", deptName: "", userName: "",seqType:"工程设计",parentDeptId:0, pageNum: 1, pageSize: $scope.pageSize,total:0 }) ;
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
            vm.showDate={design:false,consult:false,contract:false,explore:false,supervisor:false};

            vm.buildTree();
            fiveCommonCodeService.listAllMajorName().then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            })
            $scope.loadRightData(user.userLogin,"five.hrQualify");
            $scope.basicInit();
            vm.changeType();
        }

        vm.changeType=function(){

            if (vm.params.seqType.indexOf('工程设计')>=0){
                vm.showDate.design=true;
                vm.showDate.consult=false;
                vm.showDate.contract=false;
                vm.showDate.explore=false;
                vm.showDate.supervisor=false;
            }
            else if (vm.params.seqType.indexOf('工程咨询')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=true;
                vm.showDate.contract=false;
                vm.showDate.explore=false;
                vm.showDate.supervisor=false;
            }
            else if (vm.params.seqType.indexOf('工程承包')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=false;
                vm.showDate.contract=true;
                vm.showDate.explore=false;
                vm.showDate.supervisor=false;
            }
            else if (vm.params.seqType.indexOf('勘察')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=false;
                vm.showDate.contract=false;
                vm.showDate.explore=true;
                vm.showDate.supervisor=false;
            }else if (vm.params.seqType.indexOf('监理')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=false;
                vm.showDate.contract=false;
                vm.showDate.explore=false;
                vm.showDate.supervisor=true;
            }

        }

        vm.queryData = function () {
            vm.params.parentDeptId=selectId;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.buildTree = function () {
            selectId=parseInt(vm.params.parentDeptId);
            hrDeptService.selectAll().then(function (value) {
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
                        node.state.opened=true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_tree').jstree("destroy");
                $('#dept_tree')
                    .on('changed.jstree', function (e, data) {
                        var node=data.instance.get_node(data.selected[0]);
                        if(selectId===parseInt(node.id)){
                            vm.loadPagedData();
                        }else{
                            selectId=node.id;
                            vm.params.qUserName = "";
                            vm.params.qDeptName = "";
                            vm.queryData();

                        }
                    })
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        }
                    });

            });
        };

        vm.loadPagedData = function () {
            hrQualifyService.listPagedData({
                parentDeptId:vm.params.parentDeptId,
                deptName: vm.params.qDeptName,
                q: vm.params.qUserName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize
            }).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            });
        };

        vm.show = function (qualifyId,edit) {
            if (edit){
                $state.go("five.hrQualifyDetail", {qualifyId: qualifyId});
            }
        }


        vm.initWuzhouData = function () {
            hrQualifyService.initData().then(function (value) {
                if (value.data.ret) {
                    vm.queryData();
                    toastr.success("初始化成功!");
                } else {
                    toastr.error(value.data.msg);
                }
            });
        }

        vm.toggleEnable = function (id, role,edit) {
            if (edit){
                hrQualifyService.toggleEnable(id, role).then(function (value) {
                    if (value.data.ret) {
                        vm.loadPagedData();
                    } else {
                        toastr.error(value.data.msg);
                    }
                });
            }
        }

        vm.showSelectMajor = function (edit) {
            if (edit){
                singleCheckBox(".cb_major", "data-name");
                $("#selectMajorModal").modal("show");
            }
        }

        vm.saveSelectMajor = function () {
            var id = vm.qualify.id;
            var majorName = $(".cb_major:checked").first().attr("data-name");
            hrQualifyService.updateMajor(id, majorName).then(function (value) {
                if (value.data.ret) {
                    vm.loadPagedData();
                    $("#selectMajorModal").modal("hide");
                } else {
                    toastr.error(value.data.msg);
                }
            })
        }

        vm.add = function () {
            hrQualifyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrQualifyService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            })
        }

        vm.copy = function (item) {
            bootbox.confirm("您确定要新增用户"+item.userName+"设计资格数据吗?", function (result) {
                if (result) {
                    hrQualifyService.copy(item.id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("复制成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            });
        }



        vm.init();

        return vm;
    })

    .controller("FiveHrQualifyDetailController", function ($state, $stateParams, $scope, fiveCommonCodeService,commonCodeService,sysCodeService,hrQualifyService) {
        var vm = this;
        var qualifyId = $stateParams.qualifyId;

        vm.init=function(){
            vm.loadData();

            fiveCommonCodeService.listAllMajorName().then(function (value) {
                if(value.data.ret){
                    vm.majorNames=value.data.data;

                }
            })

        }

        vm.loadData = function () {
            hrQualifyService.getModelById(qualifyId).then(function (value) {
                if (value.data.ret) {
                    vm.qualify = value.data.data;
                    $scope.processInstance={myTaskFirst:true};
                    $scope.basicInit(vm.qualify.businessKey);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.qualify.operateUserLogin = user.userLogin;
            hrQualifyService.update(vm.qualify).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    hrQualifyService.update(vm.item).then(function (value) {
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


        vm.showSelectMajor=function() {
            singleCheckBox(".cb_major", "data-name");
            $("#selectMajorModal").modal("show");
        }
        vm.saveSelectMajor=function(){
            var id=vm.qualify.id;
            var majorName= $(".cb_major:checked").first().attr("data-name");
            hrQualifyService.updateMajor(id,majorName).then(function (value) {
                if (value.data.ret) {
                    vm.loadData();
                    $("#selectMajorModal").modal("hide");
                }else{
                    toastr.error(value.data.msg);
                }
            })
        }

        //选择项目类型
        vm.showProjectTypeModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"五洲项目类型").then(function (value) {
                if (value.data.ret) {
                    vm.projectTypes = value.data.data;
                    $("#selectProjectTypeModal").modal("show");
                }
            })

        }
        vm.saveSelectProjectType=function(){
            var value = [];
            $(".cb_projectType:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.qualify.projectType = value.join(',');
            $("#selectProjectTypeModal").modal("hide");
        };





        vm.init();


        return vm;
    })

    .controller("FiveHrQualifyApplyController", function ($state, $scope, fiveHrQualifyApplyService) {
        var vm = this;
        var uiSref="five.hrQualifyApply";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qYear: "", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {uiSref: uiSref, qYear: vm.params.qYear, qDeptName: vm.params.qDeptName, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin};
            fiveHrQualifyApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };

        vm.show = function (qualifyApplyId) {
            $state.go("five.hrQualifyApplyDetail", {qualifyApplyId: qualifyApplyId});
        }


        vm.add = function () {
            fiveHrQualifyApplyService.getNewModel(uiSref, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveHrQualifyApplyService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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

    .controller("FiveHrQualifyApplyDetailController", function ($state, $stateParams, $scope,$rootScope,fiveHrQualifyApplyService,sysCodeService, hrEmployeeService,hrDeptService) {
        var vm = this;
        var qualifyApplyId = $stateParams.qualifyApplyId;
        var uiSref="five.hrQualifyApply";

        vm.init=function(){
            vm.params={seqType:""};
            vm.showDate={design:false,consult:false,contract:false,explore:false,supervisor:false};

            vm.loadData(true);
            vm.loadDetail();
            $scope.loadRightData(user.userLogin,uiSref);


        }
        vm.changeType=function(){

            if (vm.params.seqType.indexOf('工程设计')>=0){
                vm.showDate.design=true;
                vm.showDate.consult=false;
                vm.showDate.contract=false;
                vm.showDate.explore=false;
                vm.showDate.supervisor=false;
            }
            else if (vm.params.seqType.indexOf('工程咨询')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=true;
                vm.showDate.contract=false;
                vm.showDate.explore=false;
                vm.showDate.supervisor=false;
            }
            else if (vm.params.seqType.indexOf('工程承包')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=false;
                vm.showDate.contract=true;
                vm.showDate.explore=false;
                vm.showDate.supervisor=false;
            }
            else if (vm.params.seqType.indexOf('勘察')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=false;
                vm.showDate.contract=false;
                vm.showDate.explore=true;
                vm.showDate.supervisor=false;
            }else if (vm.params.seqType.indexOf('监理')>=0){
                vm.showDate.design=false;
                vm.showDate.consult=false;
                vm.showDate.contract=false;
                vm.showDate.explore=false;
                vm.showDate.supervisor=true;
            }

        }

        vm.loadData = function (process) {
            fiveHrQualifyApplyService.getModelById(qualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.params.seqType=vm.item.planPost;
                    if(process) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                    }
                    $scope.basicInit(vm.item.businessKey);
                    vm.changeType();
                }
            })
        };

        vm.selectDeptModal=function(id) {
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
            $("#selectDeptModal").modal("show");
        }

        vm.saveSelectDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.deptId = dept.id;
            vm.item.deptName = dept.text;
            $("#selectDeptModal").modal("hide");
            vm.save();
        }


        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveHrQualifyApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程 并验证保存
         */
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveHrQualifyApplyService.update(vm.item).then(function (value) {
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

        vm.loadDetail = function () {
            fiveHrQualifyApplyService.listDetail(qualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.initDetailList=function() {
            fiveHrQualifyApplyService.initDetailList(qualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("初始化成功!");
                    vm.loadDetail();
                }
            })
        }

        vm.toggleDetail=function(id,optType,edit) {
            if (edit){
                fiveHrQualifyApplyService.toggleDetail(id, optType).then(function (value) {
                    if (value.data.ret) {
                        vm.loadDetail();
                    }
                });
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveHrQualifyApplyService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.init();
                        }
                    })
                }
            });
        };

        vm.clearDetail=function(){
            bootbox.confirm("确定要删除以上所有数据吗?", function (result) {
                if (result) {
                    fiveHrQualifyApplyService.clearDetail(qualifyApplyId).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.init();
                        }
                    })
                }
            });
        }

        vm.copyDetail=function(id) {
            fiveHrQualifyApplyService.copyDetail(id).then(function (value) {
                if (value.data.ret) {
                    toastr.success("删除成功");
                    vm.init();
                }
            })
        }

        vm.selectEmployeeModal = function () {
            $scope.selectEmployeeModal(vm.item.userLoginList,false,"",vm.item.deptName);
        }

        $rootScope.saveSelectEmployee = function () {
            var userLoginList=$scope.getSelectEmployeeList();
            fiveHrQualifyApplyService.insertDetail(qualifyApplyId, userLoginList.join(',')).then(function (value) {
                vm.init();
            });
        };


        //选择专业
        vm.showMajorModal=function(detail){
            if (detail!=undefined){
                vm.detail=detail;
            }else {
                vm.detail=vm.list[0];
            }
            $("#selectMajorModal").modal("show");
        }

        vm.saveSelectMajor=function(){
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            $("#selectMajorModal").modal("hide");
            vm.detail.majorName = value.join(',');

            fiveHrQualifyApplyService.updateDetail(vm.detail.id,vm.detail.majorName,vm.detail.projectType).then(function (value) {
                if(!value.data.ret){
                    vm.loadDetail();
                }
            })
        }

        //选择项目类型
        vm.showProjectTypeModal=function(detail) {
            if (detail!=undefined){
                vm.detail=detail;
            }else {
                vm.detail=vm.list[0];
            }
            singleCheckBox(".cb_projectType", "data-name");
            $("#selectProjectTypeModal").modal("show");
        }
        vm.saveSelectProjectType=function(){
            var value = [];
            $(".cb_projectType:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            $("#selectProjectTypeModal").modal("hide");
            vm.detail.projectType = value.join(',');
            vm.item.planPost=value.join(',');
            fiveHrQualifyApplyService.updateDetail(vm.detail.id,vm.detail.majorName,vm.detail.projectType).then(function (value) {
                if(!value.data.ret){
                    vm.loadDetail();
                }
            })
             vm.save();
            vm.changeType();

        }


        vm.downloadModel=function(){
            fiveHrQualifyApplyService.downloadModel().then(function (response) {

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

    .controller("FiveHrApproveApplyController", function ($state, $scope, fiveHrQualifyApplyService) {
        var vm = this;
        var uiSref="five.hrApproveApply";

        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qYear: "", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});

        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {uiSref: uiSref, qYear: vm.params.qYear, qDeptName: vm.params.qDeptName, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin};
            fiveHrQualifyApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };

        vm.show = function (qualifyApplyId) {
            $state.go("five.hrApproveApplyDetail", {qualifyApplyId: qualifyApplyId});
        }


        vm.add = function () {
            fiveHrQualifyApplyService.getNewModel(uiSref, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveHrQualifyApplyService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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

    .controller("FiveHrApproveApplyDetailController", function ($state, $stateParams, $scope,$rootScope,fiveHrQualifyApplyService,sysCodeService, hrEmployeeService,hrDeptService) {
        var vm = this;
        var qualifyApplyId = $stateParams.qualifyApplyId;
        var uiSref="five.hrApproveApply";

        vm.init=function(){
            vm.loadData(true);
            vm.loadDetail();
            $scope.loadRightData(user.userLogin,uiSref);
        }

        vm.loadData = function (process) {
            fiveHrQualifyApplyService.getModelById(qualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(process) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                    }
                    $scope.basicInit(vm.item.businessKey);
                }
            })
        };

        vm.selectDeptModal=function(id) {
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
                    if (id === item.id) {
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
            $("#selectDeptModal").modal("show");
        }

        vm.saveSelectDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.deptId = dept.id;
            vm.item.deptName = dept.text;
            $("#selectDeptModal").modal("hide");
            vm.save();
        }


        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveHrQualifyApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程 并验证保存
         */
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveHrQualifyApplyService.update(vm.item).then(function (value) {
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

        vm.loadDetail = function () {
            fiveHrQualifyApplyService.listDetail(qualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.initDetailList=function() {
            fiveHrQualifyApplyService.initDetailList(qualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    toastr.success("初始化历史审定人成功!");
                    vm.loadDetail();
                }
            })
        }

        vm.toggleDetail=function(id,optType,edit) {
            if (edit){
                fiveHrQualifyApplyService.toggleDetail(id, optType).then(function (value) {
                    if (value.data.ret) {
                        vm.loadDetail();
                    }
                });
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveHrQualifyApplyService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.init();
                        }
                    })
                }
            });
        };

        vm.clearDetail=function(){
            bootbox.confirm("确定要删除以上所有数据吗?", function (result) {
                if (result) {
                    fiveHrQualifyApplyService.clearDetail(qualifyApplyId).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.init();
                        }
                    })
                }
            });
        }

        vm.copyDetail=function(id) {
            fiveHrQualifyApplyService.copyDetail(id).then(function (value) {
                if (value.data.ret) {
                    toastr.success("删除成功");
                    vm.init();
                }
            })
        }


        vm.selectEmployeeModal = function () {
            $scope.selectEmployeeModal(vm.item.userLoginList,false,"",vm.item.deptName);
        }

        $rootScope.saveSelectEmployee = function () {
            var userLoginList=$scope.getSelectEmployeeList();
            fiveHrQualifyApplyService.insertDetail(qualifyApplyId, userLoginList.join(',')).then(function (value) {
                vm.init();
            });
        };

        //选择专业
        vm.showMajorModal=function(detail){
            vm.detail=detail;
            $("#selectMajorModal").modal("show");
        }
        vm.saveSelectMajor=function(){
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            $("#selectMajorModal").modal("hide");
            vm.detail.majorName = value.join(',');
            fiveHrQualifyApplyService.updateDetail(vm.detail.id,vm.detail.majorName,vm.detail.projectType).then(function (value) {
                if(!value.data.ret){
                    vm.loadDetail();
                }
            })
        }

        //选择项目类型
        vm.showProjectTypeModal=function(detail) {
            vm.detail=detail;
            $("#selectProjectTypeModal").modal("show");
        }
        vm.saveSelectProjectType=function(){
            var value = [];
            $(".cb_projectType:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            $("#selectProjectTypeModal").modal("hide");
            vm.detail.projectType = value.join(',');
            fiveHrQualifyApplyService.updateDetail(vm.detail.id,vm.detail.majorName,vm.detail.projectType).then(function (value) {
                if(!value.data.ret){
                    vm.loadDetail();
                }
            })

        }

        vm.downloadModel=function(){
            fiveHrQualifyApplyService.downloadModel().then(function (response) {

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

    .controller("FiveHrProChiefApplyController", function ($state, $scope, fiveHrQualifyChiefService) {
        var vm = this;
        var uiSref="five.hrProChiefApply";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qYear: "", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.type="兼职总设计师";

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };
        vm.loadPagedData = function () {
            var params = {
                qYear: vm.params.qYear,
                qDeptName: vm.params.qDeptName,
                q: vm.params.name,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref:uiSref,
                userLogin:user.userLogin
            };
            fiveHrQualifyChiefService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };


        vm.show = function (id) {
            $state.go("five.hrProChiefApplyDetail", {id: id});
        }

        vm.add = function () {
            fiveHrQualifyChiefService.getNewModel(uiSref, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveHrQualifyChiefService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret){
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
    .controller("FiveHrProChiefApplyDetailController", function ($state, $stateParams, $scope, $rootScope,sysCodeService,fiveHrQualifyChiefService, hrEmployeeService) {
        var vm = this;
        var proChiefQualifyApplyId = $stateParams.id;

        vm.loadData = function () {
            $scope.basicInit();
            fiveHrQualifyChiefService.getModelById(proChiefQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);

                    $("#majorTime").datepicker('setDate', vm.item.majorTime);
                    $("#titleTime").datepicker('setDate', vm.item.titleTime);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveHrQualifyChiefService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程 并验证保存
         */
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveHrQualifyChiefService.update(vm.item).then(function (value) {
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


        vm.selectEmployeeModal = function () {
            $scope.selectEmployeeModal(','+vm.item.chiefUserLogin+',',true,"",user.deptName);
        }

        $rootScope.saveSelectEmployee = function () {
            var userLoginList=$scope.getSelectEmployeeList();
            if(userLoginList.length===1) {
                hrEmployeeService.getModelByUserLogin(userLoginList[0]).then(function (value) {
                    if (value.data.ret) {
                        var hrEmployee = value.data.data;
                        vm.item.chiefUserName = hrEmployee.userName;
                        vm.item.chiefUserLogin = hrEmployee.userLogin;
                        vm.item.position = hrEmployee.positionNames;
                        vm.item.graduateCollege = hrEmployee.graduateCollege;
                        vm.item.graduateMajor = hrEmployee.specialty;
                        vm.save();
                    }
                })
            }
        };


        vm.loadData();

        vm.print = function () {
            fiveHrQualifyChiefService.getPrintData(proChiefQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("兼职总设计师资格认定申报表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_areaProChief").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        //选择专业
        vm.showMajorModel=function(){
            sysCodeService.listDataByCatalog("设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majors = value.data.data;
                    $("#selectMajorModal").modal("show");
                }
            })
        }

        vm.saveSelectMajor=function(){
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.major = value.join(',');
            $("#selectMajorModal").modal("hide");
        }

        //选择现承担项目类型
        vm.showProjectTypeNowModel=function(opt){
            vm.opt=opt;
            $("#selectProjectTypeNowModal").modal("show");
        }
        vm.saveSelectProjectTypeNow=function(){
            var value = [];
            $(".cb_projectTypeNow:checked").each(function () {
                value.push($(this).attr("data-name"));
            });

            if (vm.opt=='projectTypeNow'){

                vm.item.projectTypeNow = value.join(',');
            } else if (vm.opt=='projectTypeApply'){

                vm.item.projectTypeApply = value.join(',');
            }
            $("#selectProjectTypeNowModal").modal("hide");
        }

        vm.downloadModel=function(){
            fiveHrQualifyChiefService.downloadModel().then(function (response) {

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
        vm.loadData();
        return vm;

    })

    .controller("FiveHrChiefApplyController", function ($state, $scope, fiveHrQualifyChiefService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qYear: "", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.hrChiefApply";
        vm.type="总设计师"
        vm.loadPagedData = function () {
            var params = {
                qYear: vm.params.qYear,
                qDeptName: vm.params.qDeptName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                uiSref:uiSref,
                userLogin:user.userLogin
            };
            fiveHrQualifyChiefService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };
        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.show = function (chiefQualifyApplyId) {
            $state.go("five.hrChiefApplyDetail", {id: chiefQualifyApplyId});
        }


        vm.add = function () {
            fiveHrQualifyChiefService.getNewModel(uiSref, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveHrQualifyChiefService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret){
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
    .controller("FiveHrChiefApplyDetailController", function ($state, $stateParams, $scope,$rootScope,sysCodeService,fiveHrQualifyChiefService, hrEmployeeService) {
        var vm = this;
        var chiefQualifyApplyId = $stateParams.id;

        vm.loadData = function () {
            $scope.basicInit();
            fiveHrQualifyChiefService.getModelById(chiefQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);
                    $("#majorTime").datepicker('setDate', vm.item.majorTime);
                    $("#titleTime").datepicker('setDate', vm.item.titleTime);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveHrQualifyChiefService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    fiveHrQualifyChiefService.update(vm.item).then(function (value) {
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

        vm.selectEmployeeModal = function () {
            $scope.selectEmployeeModal(','+vm.item.chiefUserLogin+',',true,"",user.deptName);
        }

        $rootScope.saveSelectEmployee = function () {
            var userLoginList=$scope.getSelectEmployeeList();
            if(userLoginList.length===1) {
                hrEmployeeService.getModelByUserLogin(userLoginList[0]).then(function (value) {
                    if (value.data.ret) {
                        var hrEmployee = value.data.data;
                        vm.item.chiefUserName = hrEmployee.userName;
                        vm.item.chiefUserLogin = hrEmployee.userLogin;
                        vm.item.position = hrEmployee.positionNames;
                        vm.item.graduateCollege = hrEmployee.graduateCollege;
                        vm.item.graduateMajor = hrEmployee.specialty;
                        vm.save();
                    }
                })
            }
        };



        vm.print = function () {
            fiveHrQualifyChiefService.getPrintData(chiefQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("总设计师资格认定申报表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_areaChief").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //选择专业
        vm.showMajorModel=function(){
            sysCodeService.listDataByCatalog("设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majors = value.data.data;
                    $("#selectMajorModal").modal("show");
                }
            })

        }
        vm.saveSelectMajor=function(){
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.major = value.join(',');

            $("#selectMajorModal").modal("hide");
        }

        //选择现承担项目类型
        vm.showProjectTypeNowModel=function(opt){
            vm.opt=opt;
            $("#selectProjectTypeNowModal").modal("show");
        }
        vm.saveSelectProjectTypeNow=function(){
            var value = [];
            $(".cb_projectTypeNow:checked").each(function () {
                value.push($(this).attr("data-name"));
            });

            $("#selectProjectTypeNowModal").modal("hide");
            if (vm.opt=='projectTypeNow'){

                vm.item.projectTypeNow = value.join(',');
            } else if (vm.opt=='projectTypeApply'){

                vm.item.projectTypeApply = value.join(',');
            }
        }
        vm.downloadModel=function(){
            fiveHrQualifyChiefService.downloadModel().then(function (response) {

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
        vm.loadData();
        return vm;

    })

    .controller("FiveHrExternalApplyController", function ($state, $scope, fiveHrQualifyExternalService) {
        var vm = this;
        var uiSref="five.hrExternalApply";
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qYear: "", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});

        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.loadPagedData = function () {
            var params = { qYear: vm.params.qYear, qDeptName: vm.params.qDeptName, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize, uiSref:uiSref, userLogin:user.userLogin};
            fiveHrQualifyExternalService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };
        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.show = function (externalQualifyApplyId) {
            $state.go("five.hrExternalApplyDetail", {id: externalQualifyApplyId});
        }


        vm.add = function () {
            fiveHrQualifyExternalService.getNewModel(uiSref, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveHrQualifyExternalService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret){
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
    .controller("FiveHrExternalApplyDetailController", function ($state, $stateParams, $scope, commonCodeService,fiveHrQualifyExternalService,hrDeptService) {
        var vm = this;
        var externalQualifyApplyId = $stateParams.id;

        vm.init=function(){
            vm.loadData(true);

            $scope.loadRightData(user.userLogin,"five.hrExternalApply");
        }

        vm.loadData = function () {
            $scope.basicInit();
            fiveHrQualifyExternalService.getModelById(externalQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveHrQualifyExternalService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    fiveHrQualifyExternalService.update(vm.item).then(function (value) {
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
        vm.print=function () {
            fiveHrQualifyExternalService.getPrintData(externalQualifyApplyId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("外聘技术人员录用审批表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //选择专业
        vm.showMajorModel=function(){
            commonCodeService.listDataByCatalog(user.enLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majors = value.data.data;
                    $("#selectMajorModal").modal("show");
                }
            })

        }
        vm.saveSelectMajor=function(){
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.planMajor = value.join(',');
            $("#selectMajorModal").modal("hide");
        }
        //选择岗位
        vm.showPostModel=function(){
               commonCodeService.listDataByCatalog(user.enLogin,"拟聘岗位").then(function (value) {
                if (value.data.ret) {
                    vm.posts = value.data.data;
                    $("#selectPostModal").modal("show");
                }
            })

        }
        vm.saveSelectPost=function(){
            var value = [];
            $(".cb_post:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.planPost = value.join(',');
            $("#selectPostModal").modal("hide");
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

        vm.downloadModel=function(){
            fiveHrQualifyExternalService.downloadModel().then(function (response) {

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
