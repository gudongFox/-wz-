angular.module('controllers.hr1', [])

    .controller("HrDetailController", function ($state, $scope,$rootScope,$stateParams,hrEmployeeService) {
        var vm = this;
        var owner=$stateParams.owner;
        vm.init = function () {

            vm.loadTree();

            hrEmployeeService.getModelByUserLogin(owner).then(function (value) {
                if(value.data.ret){
                    $scope.ownerInfo=value.data.data;
                }
            });
        }

        vm.loadTree=function(){
            var tree = $("#detailTree");

            var treeData=[
                { "id" : "hrBasic", "parent" : "#", "text" : "基础信息" ,"icon":"fa fa-user","state":{'selected':true}},
                { "id" : "hrResume", "parent" : "#", "text" : "简历情况" ,"icon":"fa fa-file-text"},
                { "id" : "hrEducation", "parent" : "#", "text" : "学历学位" ,"icon":"fa fa-graduation-cap"},
                { "id" : "hrTechnology", "parent" : "#", "text" : "专业技术职务" ,"icon":"fa fa-wrench"},
                { "id" : "hrProfession", "parent" : "#", "text" : "职业技能资格" ,"icon":"fa fa-tachometer"},
                { "id" : "hrFamily", "parent" : "#", "text" : "家庭成员" ,"icon":"fa fa-group"},
                { "id" : "hrExamine", "parent" : "#", "text" : "考核记录" ,"icon":"fa fa-pencil-square"},
                { "id" : "hrPartyPosition", "parent" : "#", "text" : "党政职务" ,"icon":"fa fa-university"},
                { "id" : "hrImportantGroup", "parent" : "#", "text" : "重要群体" ,"icon":"fa fa-star"},
                { "id" : "hrPartTimeJob", "parent" : "#", "text" : "兼任职务" ,"icon":"fa fa-life-ring"},
                { "id" : "hrQualification", "parent" : "#", "text" : "职业资格" ,"icon":"fa fa-bookmark"},
                { "id" : "hrAward", "parent" : "#", "text" : "奖励情况" ,"icon":"fa fa-gift"},
                { "id" : "hrHonor", "parent" : "#", "text" : "荣誉称号" ,"icon":"fa fa-tag"},
                { "id" : "hrPaper", "parent" : "#", "text" : "论文著作" ,"icon":"fa fa-file-text-o"},
                { "id" : "hrPatent", "parent" : "#", "text" : "专利情况" ,"icon":"fa fa-space-shuttle"},
                { "id" : "hrPunish", "parent" : "#", "text" : "惩处记录" ,"icon":"fa fa-arrows"},
                { "id" : "hrGoAbroad", "parent" : "#", "text" : "出国情况" ,"icon":"fa fa-paper-plane"},
                { "id" : "hrStandard", "parent" : "#", "text" : "标准规范" ,"icon":"fa fa-location-arrow"},
                // { "id" : "hrTrain", "parent" : "#", "text" : "培训计划" ,"icon":"fa fa-suitcase"},
                { "id" : "hrTrainMember", "parent" : "#", "text" : "培训参与" ,"icon":"fa fa-group"},
                { "id" : "hrCourse", "parent" : "#", "text" : "授课情况" ,"icon":"fa fa-slack"},
                { "id" : "hrProject", "parent" : "#", "text" : "重点项目" ,"icon":"fa fa-building"}
            ];
            tree.jstree("destroy");
            tree.jstree({
                'core': {
                    'data': treeData,
                    "multiple": false,
                    "themes": {
                        "responsive": false
                    }
                }
            });
            tree.on('changed.jstree', function (e, data) {
                if (data.selected.length > 0) {
                    var node = data.instance.get_node(data.selected[0]);
                    var redirect=getRedirectInfo(node.id);
                    $state.go(redirect.url,redirect.params);
                }
            });
        }


        function getRedirectInfo(nodeId) {
            var result={url:"hr.detail.form",params:{referType:nodeId}};
            if(nodeId=="hrPatent") {
              //怎么怎么
            }
            return result;
        }

        vm.init();

        return vm;
    })

    .controller("HrDetailFormController", function ($state, $scope,$state,$rootScope,$stateParams,hrDetailService,hrEmployeeService,commonFormService,commonFormDataService) {
        var vm = this;
        var owner = $stateParams.owner;
        var referType = $stateParams.referType;


        vm.params = {qq: "", q: "", owner: "", qOwner: owner,referId:0};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize, total: $scope.pageSize};

        vm.init = function () {
            commonFormService.getModelByKey(user.tenetId, referType).then(function (value) {
                if (value.data.ret) {
                    vm.template = value.data.data;
                }
            })
            vm.queryData();
        }

        vm.keyDown = function () {
            if (event.keyCode == 13)
                vm.queryData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.q = vm.params.qq;
            vm.params.owner = vm.params.qOwner;
            vm.params.referId = 0;
            if (vm.params.owner) {
                hrEmployeeService.getModelByUserLogin(vm.params.owner).then(function (value) {
                    if (value.data.ret && value.data.data) {
                        vm.params.referId = value.data.data.id;
                    }
                    vm.loadPagedData();
                });
            } else {
                vm.loadPagedData();
            }
        }

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                enLogin: user.enLogin,
                referType: referType,
                referId: vm.params.referId,
                q: vm.params.q
            };
            commonFormDataService.listNewPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data.pageInfo;
                    vm.heads = value.data.data.heads;
                }
            });

        };

        vm.newData = function () {
            hrDetailService.getNewModel(referType, owner, user.enLogin).then(function (value) {
                vm.loadPagedData();
            })
        }

        vm.detail = function (item) {
            if (item.businessKey.indexOf('hrBasic') >= 0) {
                $state.go("hr.detail.basicDetail", {formDataId: item.id});
            } else {
                $state.go("hr.detail.formDetail", {formDataId: item.id});
            }
        }

        vm.remove = function (id) {
            bootbox.confirm("确定要删除该表单记录吗", function (result) {
                if (result) {
                    commonFormDataService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.back=function(){
            $state.go("hr.dept");
        }

        vm.exportData=function(){
            var url="/hr/detail/downloadExcel.json";
            $scope.commonDownload(url,{referType:referType,owner:vm.params.owner});
        }

        $scope.$watch("ownerInfo", function () {
            if ($scope.ownerInfo) {
                vm.init();
            }
        });

        return vm;
    })

    .controller("HrDetailFormDetailController", function ($state, $scope, $rootScope,$stateParams, commonFormService,commonFormDataService) {
        var vm = this;
        var formDataId = $stateParams.formDataId;

        vm.init = function () {
            vm.loadData();
        }

        vm.loadData = function () {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.template = value.data.data.template;
                $scope.loadNewProcessInstance(vm.data.processInstanceId);
                if (!vm.data.processInstanceId) {
                    $scope.getTplConfig(vm.data.processInstanceId, vm.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
            })
        }

        vm.save=function() {
            var data = $scope.getFormData(vm.data.id, vm.groupList);
            data["autoSubmit"] = $scope.tplConfig.autoSubmit && vm.groupList;
            commonFormDataService.save(data).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            });
        }

        vm.back=function(){
            $state.go("hr.detail.form",{referType:vm.template.formKey});
        }

        vm.init();

        return vm;
    })

    .controller("HrDetailBasicDetailController", function ($state, $scope, $rootScope,$stateParams,hrEmployeeService,commonFormService,commonFormDataService) {
        var vm = this;
        var formDataId = $stateParams.formDataId;

        vm.init = function () {
            vm.loadData();
        }

        vm.loadData = function () {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.template = value.data.data.template;
                $scope.loadNewProcessInstance(vm.data.processInstanceId);
                if (!vm.data.processInstanceId) {
                    $scope.getTplConfig(vm.data.processInstanceId, vm.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
                vm.loadOther(vm.data.referId);
            });
        }

        vm.loadOther=function(employeeId){
            commonFormDataService.listNewData({referId:employeeId,referType:'hrEducation'}).then(function(value){
                vm.hrEducation=value.data.data;
            })
            commonFormDataService.listNewData({referId:employeeId,referType:'hrTechnology'}).then(function(value){
                vm.hrTechnology=value.data.data;
            })
            commonFormDataService.listNewData({referId:employeeId,referType:'hrQualification'}).then(function(value){
                vm.hrQualification=value.data.data;
            })
        }


        vm.save=function() {
            var data = $scope.getFormData(vm.data.id, vm.groupList);
            data["autoSubmit"] = $scope.tplConfig.autoSubmit && vm.groupList;
            commonFormDataService.save(data).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            });
        }

        vm.back=function(){
            $state.go("hr.detail.form",{referType:vm.template.formKey});
        }

        vm.init();

        return vm;
    })

    .controller("HrFormController", function ($state, $scope,$rootScope,$stateParams,hrDetailService,hrEmployeeService,commonFormService,commonFormDataService) {
        var vm = this;
        var owner = $stateParams.owner;
        var referType = $stateParams.referType;

        vm.params = {qq: "", q: "", owner: "", qOwner: owner,referId:0};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize, total: $scope.pageSize};

        vm.init = function () {
            commonFormService.getModelByKey(user.tenetId, referType).then(function (value) {
                if (value.data.ret) {
                    vm.template = value.data.data;
                }
            })
            vm.queryData();
        }

        vm.keyDown = function () {
            if (event.keyCode == 13)
                vm.queryData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.q = vm.params.qq;
            vm.params.owner = vm.params.qOwner;
            vm.params.referId = 0;
            if (vm.params.owner) {
                hrEmployeeService.getModelByUserLogin(vm.params.owner).then(function (value) {
                    if (value.data.ret && value.data.data) {
                        vm.params.referId = value.data.data.id;
                    }
                    vm.loadPagedData();
                });
            } else {
                vm.loadPagedData();
            }
        }

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                enLogin: user.enLogin,
                referType: referType,
                referId: vm.params.referId,
                q: vm.params.q
            };
            commonFormDataService.listNewPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data.pageInfo;
                    vm.heads = value.data.data.heads;
                }
            });

        };

        vm.newData = function () {
            hrDetailService.getNewModel(referType, owner, user.enLogin).then(function (value) {
                vm.loadPagedData();
            })
        }

        vm.detail = function (item) {
            $state.go("hr.formDetail", {formDataId: item.id});
        }

        vm.remove = function (id) {
            bootbox.confirm("确定要删除该表单记录吗", function (result) {
                if (result) {
                    commonFormDataService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.exportData=function(){
            var url="/hr/detail/downloadExcel.json";
            $scope.commonDownload(url,{referType:referType,owner:vm.params.owner});
        }


        if(!owner) {
            //单人入口
            owner = user.enLogin;
            vm.params.qOwner = owner;
            vm.onlyMe = true;
            vm.init();
        }


        return vm;
    })

    .controller("HrFormDetailController", function ($state, $scope, $rootScope,$stateParams, commonFormService,commonFormDataService) {
        var vm = this;
        var formDataId = $stateParams.formDataId;

        vm.init = function () {
            vm.loadData();
        }

        vm.loadData = function () {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.template = value.data.data.template;
                $scope.loadNewProcessInstance(vm.data.processInstanceId);
                if (!vm.data.processInstanceId) {
                    $scope.getTplConfig(vm.data.processInstanceId, vm.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
            })
        }

        vm.save=function() {
            var data = $scope.getFormData(vm.data.id, vm.groupList);
            data["autoSubmit"] = $scope.tplConfig.autoSubmit && vm.groupList;
            commonFormDataService.save(data).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            });
        }

        vm.back=function() {
            $state.go("hr.form", {referType: vm.template.formKey});
        }
        vm.init();
        return vm;
    })

    .controller("HrTrainController", function ($state, $scope,$state,$rootScope,$stateParams,hrDetailService,hrEmployeeService,commonFormService,commonFormDataService) {

        var vm = this;
        var referType = "hrTrain";
        var referId = 0;
        vm.q = "";

        vm.init = function () {

            vm.loadData();

            $("input[type=search]").on('keypress', function () {
                if (event.keyCode == 13) {
                    vm.loadData();
                }
            })
        };

        vm.loadData = function () {
            commonFormDataService.listDataByUser(referType, vm.q, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    vm.data = value.data.data;

                }
            })
        }

        vm.newData = function () {

            commonFormDataService.getNewModelByUser(referType, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    vm.showDetail(value.data.data.id);
                } else {
                    toastr.error(value.data.msg);
                }
            })
        }

        vm.showDetail = function (id) {
            $state.go("hr.trainDetail", {formDataId: id})
        }

        vm.remove = function (id) {
            bootbox.confirm("确定删除该数据吗?", function (result) {
                if (result) {
                    commonFormDataService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData();
                        }
                    })
                }
            });

        }

        vm.init();
        return vm;
    })

    .controller("HrTrainDetailController", function ($state, $scope, $rootScope,$stateParams, commonFormService,commonFormDataService,hrTrainMemberService) {

        var vm = this;

        var formDataId = $stateParams.formDataId;

        vm.init = function () {
            vm.loadTrain();
            vm.loadData();
        }

        vm.loadTrain=function() {
            commonFormDataService.getFormDataById(formDataId).then(function (value) {
                vm.trainKey = value.data.data.trainKey;
                vm.loadHrMember();
            })
        }

        vm.loadData = function () {

            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.template = value.data.data.template;
                $scope.loadNewProcessInstance(vm.data.processInstanceId);
                if (!vm.data.processInstanceId) {
                    $scope.getTplConfig(vm.data.processInstanceId, vm.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
            })
        }

        vm.save=function() {
            var data = $scope.getFormData(vm.data.id, vm.groupList);
            data["autoSubmit"] = $scope.tplConfig.autoSubmit && vm.groupList;
            hrTrainMemberService.save(data).then(function (value) {
                if (value.data.ret) {
                    vm.loadTrain();
                    toastr.success("保存成功!");
                }
            });
        }

        vm.back=function(){
            $state.go("hr.train",{referType:vm.template.formKey});
        }

        vm.loadHrMember=function(){

            if(vm.trainKey) {
                hrTrainMemberService.listHrTrainMember(vm.trainKey).then(function (value) {
                    vm.hrTrainMembers = value.data.data;
                })
            }else{
                vm.hrTrainMembers=[];
            }
        }

        vm.selectHrTrainMember=function() {

            var selects = "";
            for (var i = 0; i < vm.hrTrainMembers.length; i++) {
                selects = selects + "," + vm.hrTrainMembers[i].userLogin;
            }

            var config = {
                title: "请选择培训人员",
                enLogin: user.enLogin,
                multiple: true,
                selects: selects
            };

            jQuery.showJsTreeSelectUserModal(config, function (nodes,ids,names) {
                hrTrainMemberService.newData(vm.trainKey, ids, user.enLogin).then(function (value) {
                    if(value.data.ret){
                        vm.loadHrMember();
                    }
                })
            })

        }

        vm.removeHrMember=function(id){
            hrTrainMemberService.removeHrMember(id).then(function (value){
                vm.loadHrMember();
            })
        }

        vm.showHrMember=function(detail){
            vm.hrMember = $.extend( {}, detail );
            $("#hrMemberModal").modal("show");
        }

        vm.saveHrMember=function() {
            vm.hrMember.formDataId=formDataId;
            hrTrainMemberService.saveHrMember(vm.hrMember).then(function (value) {
                if(value.data.ret) {
                    $("#hrMemberModal").modal("hide");
                    vm.loadHrMember();
                }
            })
        }

        vm.init();

        return vm;
    })

    .controller("HrIncomeProofController", function ($state, $scope, hrIncomeProofService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum,userLogin:user.userLogin, pageSize: vm.pageInfo.pageSize};
            hrIncomeProofService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };


        vm.show = function (id) {
            $state.go("hr.incomeProofDetail",{incomeProofId: id});
        }


        vm.add = function () {
            hrIncomeProofService.getNewModel(1, user.userLogin).then(function (value) {
                if (value.data.ret) {
                   vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    hrIncomeProofService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("HrIncomeProofDetailController", function ($state,$stateParams, $scope, hrIncomeProofService) {
        var vm = this;
        var incomeProofId=$stateParams.incomeProofId;

        vm.loadData = function (loadProcess) {

            hrIncomeProofService.getModelById(incomeProofId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadEdFile(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrIncomeProofService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
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

    .controller("HrLeaveController", function ($state, $scope, hrLeaveService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum,userLogin:user.userLogin,pageSize: vm.pageInfo.pageSize};
            hrLeaveService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };


        vm.show = function (id) {
            $state.go("hr.leaveDetail",{leaveId: id});
        }


        vm.add = function () {
            hrLeaveService.getNewModel(1, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    hrLeaveService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("HrLeaveDetailController", function ($state,$stateParams, $scope, hrLeaveService) {
        var vm = this;
        var leaveId=$stateParams.leaveId;

        vm.loadData = function (loadProcess) {

            hrLeaveService.getModelById(leaveId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#approveLeaveTime").datepicker('setDate',vm.item.approveLeaveTime);
                    $("#applyLeaveTime").datepicker('setDate',vm.item.applyLeaveTime);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrLeaveService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow=function () {
            if ($("#detail_form").validate().form()) {
                vm.save();
                $scope.showSendFlow();
            } else {
                toastr.warning("请准确填写数据!")
                return;
            }

        }

        vm.loadData(true);
        return vm;

    })

    .controller("HrVacationController", function ($state, $scope, hrVacationService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum,userLogin:user.userLogin, pageSize: vm.pageInfo.pageSize};
            hrVacationService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };


        vm.show = function (id) {
            $state.go("hr.vacationDetail",{vacationId: id});
        }

        vm.add = function () {
            hrVacationService.getNewModel(1, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    hrVacationService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("HrVacationDetailController", function ($state,$stateParams, $scope, hrVacationService,hrVacationDetailService) {
        var vm = this;
        var vacationId=$stateParams.vacationId;

        vm.loadData = function (loadProcess) {
            hrVacationService.getModelById(vacationId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadEdFile(vm.item.businessKey);
                    }
                }
            })
            vm.listVacationDetail();
        };

        vm.listVacationDetail=function(){
            hrVacationDetailService.listDataByForeignKey(vacationId).then(function (value) {
                if (value.data.ret){
                    vm.list=value.data.data;
                }
            })
        };

        vm.addVacationDetail=function(){
            hrVacationDetailService.getNewModel(vacationId).then(function (value) {
                vm.editVacationDetail(value.data.data);
            })
        }

        vm.editVacationDetail = function (id) {
            hrVacationDetailService.getModelById(id).then(function (value) {
                vm.vacationDetail = value.data.data;
                vm.planEnd=value.data.data.planEnd;
                vm.planBegin=value.data.data.planBegin;
                $("#vacationDetail").modal("show");
            });
        }

        vm.deleteVacationDetail=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    hrVacationDetailService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.listVacationDetail();
                        }
                    })
                }
            })
        };


        vm.updateVacationDetail=function(){
            if($("#VacationDetail_form").validate().form()){
                vm.item.operateUserLogin=user.userLogin;
                hrVacationDetailService.update(vm.vacationDetail).then(function (value) {
                    if(value.data.ret){
                        toastr.success("修改成功!");
                        $("#vacationDetail").modal("hide");
                        vm.loadData();
                    }
                })
            }
        }

        vm.changePlanDay=function(){
            var beginTime=vm.vacationDetail.planBegin;
            var endTime=vm.vacationDetail.planEnd;
            beginTime=  beginTime.replace(/年/g,"/");
            beginTime=  beginTime.replace(/月/g,"/");
            beginTime=  beginTime.replace(/日/g,"/");
            endTime=  endTime.replace(/年/g,"/");
            endTime=  endTime.replace(/月/g,"/");
            endTime=  endTime.replace(/日/g,"/");
            var beginDate=(Number)(new Date(Date.parse(beginTime)));
            var endDate=(Number)(new Date(Date.parse(endTime)));
            var planDay=(endDate-beginDate)/(1000*3600*24)
            if (planDay<0){
                toastr.error("计划开始时间不能大于计划结束时间!");
            }else {
                vm.vacationDetail.planDay=planDay;
            }
        }
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrVacationService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
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

    .controller("HrDeptUserController", function ($state, $scope, hrDeptService,commonCodeService,hrEmployeeService,sysRoleService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var selectId=0;

        vm.init=function(){
            vm.params= getCacheParams(key, {qDeptName: "", qUserName: "", deptName: "", userName: "",parentDeptId:0, pageNum: 1, pageSize: $scope.pageSize,total:0 });
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
            vm.buildTree();
            hrDeptService.selectAll().then(function (value) {
                vm.depts=value.data.data;
            });
            $scope.basicInit("");

            //回车事件监听
            $('#applyCertNum').bind('keydown',function(event){
                if(event.keyCode == "13") {
                    vm.queryData();
                }
            });
        }

        vm.loadPagedData = function () {
            hrEmployeeService.listPagedData({
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
        vm.queryData = function () {
            vm.params.parentDeptId=selectId;
            vm.params.qUserName = vm.params.userName;
            vm.params.qDeptName = vm.params.deptName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

            hrDeptService.getModelById(selectId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                }
            })

            vm.loadCandicate(selectId);

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
                        node.state.opened = true;
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

        vm.loadDept=function(){

            hrDeptService.getModelById(selectId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                }
            })

            vm.loadCandicate(selectId);

            vm.loadEmployee(selectId);
        }

        vm.loadCandicate=function(id){
            hrDeptService.listParentCandicate(id).then(function (value) {
                if(value.data.ret){
                    vm.parents=value.data.data;
                }
            })
        }

        vm.loadEmployee=function(id){
            hrEmployeeService.listEmployeeByDeptId(id,true).then(function (value) {
                if(value.data.ret){
                    vm.employees=value.data.data;
                }
            })
        }

        vm.showDeptModal=function(item) {
            vm.current = item;
            $('#dept_select_tree').jstree("destroy");
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
                    if (item.id ===  vm.current.deptId) {
                        node.state.selected = true;
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

        vm.showDeptModalById=function(deptId) {
            $('#dept_select_tree1').jstree("destroy");
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
                    if (item.id ==deptId) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree1').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModalId").modal("show");

        }

        vm.showTypeModal=function(item){
            vm.current=item;
            commonCodeService.listDataByCatalog(user.userLogin,"员工类型").then(function (value) {
                if(value.data.ret){
                    vm.type=value.data.data;
                    singleCheckBox(".cb_type","data-name");
                }
            })
            $("#typeSelectModal").modal("show");
        }

        vm.showUserAclTree = function () {
            sysRoleService.getAclTreeByUserLogin(vm.currentUser.userLogin).then(function (value) {
                if (value.data.ret) {
                    var treeData = value.data.data;
                    $('#user_acl_select_tree').jstree("destroy");
                    $('#user_acl_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            },
                            "checkbox" : {
                                "keep_selected_style" : false
                            },
                            "plugins" : [ "wholerow", "checkbox" ]
                        });
                    $("#userAclSelectModal").modal("show");
                }
            });
        };

        vm.init();

        return vm;

    })

    .controller("HrEmployeeController", function ($state, $scope, hrEmployeeService,commonCodeService,sysRoleService,hrPositionService,hrDeptService) {

        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params= getCacheParams(key, {q: "", deptName: "", selectText:"人员信息-在职",userStatus: "", pageNum: 1, pageSize: $scope.pageSize,total:0 });
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.init = function () {

            commonCodeService.listDataByCatalog(user.userLogin,"员工状态").then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                var root = {id: 0, parent: "#", text: "人员信息", state: {opened: true, disabled: false, selected: false}};
                if(root.text=== vm.params.selectText){
                    root.state.selected=true;
                }
                vm.treeData.push(root);
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent:"0",
                        text: '人员信息-'+item.name,
                        state: {opened: false, disabled: false, selected: false}
                    };
                    if (node.text ===  vm.params.selectText) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#js_tree').jstree("destroy");
                $('#js_tree')
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if( vm.params.selectText==node.text){
                            vm.loadPagedData();
                        }else{
                            vm.params.selectText=node.text;
                            vm.params.q="";
                            vm.params.qDeptName="";
                            vm.queryData();
                        }
                    })
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        }
                    });
            });

            $scope.basicInit();
        };

        vm.queryData = function () {

            if( vm.params.selectText.split("-").length>1){
                vm.params.userStatus= vm.params.selectText.split("-")[1];
            }
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {q: vm.params.q,deptName: vm.params.deptName,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrEmployeeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    params.selectText=vm.params.selectText;
                    setCacheParams(key,params,vm.pageInfo);
                }
            })

        }

        vm.show = function (userLogin) {
            $state.go("hr.basicDetail", {userLogin: userLogin});
        }
        vm.remove = function (userLogin) {
            bootbox.confirm("你确认要删除该条数据吗？删除后无法恢复", function (result) {
                if (result) {
                    hrEmployeeService.remove(userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.addEmployee = function () {
            hrEmployeeService.getNewModel().then(function (value) {
                vm.employee=value.data.data;
                hrDeptService.selectAll().then(function (value) {
                    vm.depts=value.data.data;
                    for (var i=0;i<vm.depts.length;i++) {
                        if (vm.depts[i].id == vm.employee.deptId) {
                            vm.deptNameId = vm.depts[i].name;
                        }
                    }
                })
            });

            $("#addEmployeeModal").modal("show");
        }

        vm.insertEmployee=function(){
            if ($("#employee_form").validate().form()) {
                bootbox.confirm("你确认要新增用户吗?", function (result) {
                    if (result) {
                        hrEmployeeService.insert(vm.employee.userLogin, vm.employee.userName, vm.employee.deptId, vm.employee.userType).then(function (value) {
                            if (value.data.ret) {
                                $("#addEmployeeModal").modal("hide");
                                setTimeout(function () {
                                    $state.go("hr.employeeDetail", {userLogin:vm.employee.userLogin});
                                },500);

                            }
                        })
                    }

                });
            }

        }


        vm.showRoleModal=function(item){
            vm.current=item;
            sysRoleService.selectAll().then(function (value) {
                if(value.data.ret){
                    vm.roles=value.data.data;
                }
            })
            $("#roleSelectModal").modal("show");
        }

        vm.showPositionModal=function(item){
            vm.current=item;
            hrPositionService.selectAll().then(function (value) {
                if(value.data.ret){
                    vm.positions=value.data.data;
                }
            })
            $("#positionSelectModal").modal("show");
        }

        vm.showDeptModal=function(item) {
            vm.current = item;
            $('#dept_select_tree').jstree("destroy");
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
                    if (item.id ===  vm.current.deptId) {
                        node.state.selected = true;
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
            var select = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            hrEmployeeService.updateDeptId(vm.current.userLogin, select.id).then(function (value) {
                if (value.data.ret) {
                    $("#deptSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });
        }

        vm.saveRole=function(){
            var roleIds = [];
            $(".cb_role:checked").each(function () {
                roleIds.push($(this).attr("data-id"));
            });

            hrEmployeeService.updateRoleIds(vm.current.userLogin,roleIds.join(',')).then(function (value) {
                if(value.data.ret){
                    $("#roleSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });

        }

        vm.savePosition=function(){
            var positionIds = [];
            $(".cb_position:checked").each(function () {
                positionIds.push($(this).attr("data-id"));
            });
            hrEmployeeService.updatePositionIds(vm.current.userLogin,positionIds.join(',')).then(function (value) {
                if(value.data.ret){
                    $("#positionSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });
        }

        vm.resetPwd=function(userLogin){
            bootbox.confirm("你确认要重置"+userLogin+"的密码吗?", function (result) {
                if (result) {
                    hrEmployeeService.resetPwd(userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("重置密码(" + value.data.data + ")成功");
                        }
                    })
                }
            });
        }
        /*新增用户-部门选择*/
        vm.showDeptModalById=function(deptId) {
            $('#dept_select_tree1').jstree("destroy");
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
                    if (item.id ==deptId) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree1').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModalId").modal("show");

        }
        /*新增用户-部门选择*/
        vm.saveDeptById=function(){
            var select = $('#dept_select_tree1').jstree(true).get_selected(true)[0];
            vm.employee.deptId=select.id;
            vm.deptNameId=select.text;
            $("#deptSelectModalId").modal("hide");
        }

        vm.init();

        return vm;

    })

    .controller("HrRegisterController", function ($state, $scope, hrRegisterService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;

        vm.init=function(){
            vm.params = getCacheParams(key,{ q: "",deptName:"",userStatus:"", pageNum: 1, pageSize: $scope.pageSize,total:0 });
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
            vm.loadPagedData();
        }


        vm.loadPagedData = function () {
            var params = {q: vm.params.q,deptName: vm.params.deptName,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrRegisterService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,params,vm.pageInfo);
                }
            })
        }


        vm.show = function (registerId) {
            $state.go("hr.registerDetail", {registerId: registerId});
        }


        vm.add = function () {
            hrRegisterService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    var v=value.data.data;
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?", function (result) {
                if (result) {
                    hrRegisterService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData(user.userLogin);
                    });
                }
            })
        }

        vm.downloadModel = function() {
            hrRegisterService.downloadModel(user.userLogin).then(function (response) {
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
            url:'/hr/register/receive.do?userLogin='+user.userLogin,
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
                    vm.loadPagedData();
                }
            }
        });
        vm.init();

        return vm;

    })

    .controller("HrRegisterDetailController", function ($state, $stateParams, $scope, hrRegisterService,commonCodeService,hrEmployeeService) {
        var vm = this;
        var registerId = $stateParams.registerId;


        vm.loadData = function () {
            $scope.basicInit();

            hrRegisterService.getModelById(registerId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.provinceId=""+provinceArr.indexOf(vm.item.province);
                    vm.cityId =""+cityArr[vm.provinceId].indexOf(vm.item.city);
                    vm.getCityArr = vm.cityArr[vm.provinceId] ; //根据索引写入城市数据
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                    $("#endTime").datepicker('setDate', vm.item.endTime);
                    $("#gmtCreate").datepicker('setDate', new Date());
                    $("#gmtModified").datepicker('setDate', new Date());

                    $scope.processInstance={myTaskFirst:true};
                    $scope.basicInit(vm.item.businessKey);
                }
            })


        };
        vm.showSelectMajorModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            });
            $("#selectMajorModal").modal("show");

        }
        vm.selectMajor=function(){
            var majors = [];
            $(".cb_destMajor:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            vm.item.specialty = majors.join(',');
            $("#selectMajorModal").modal("hide");
        }

        vm.showEmployeeModel=function(){

            hrEmployeeService.selectAll().then(function(value) {
                vm.employees=value.data.data;
                singleCheckBox(".cb_employee","data-name");
            });
            $("#selectEmployeeModel").modal("show");
        }

        vm.saveSelectEmployee=function(){

            var login;
            var name ;
            $(".cb_employee:checked").each(function () {
                var info = $(this).attr("data-name");
                login=info.split('_')[0];
                name=info.split('_')[1];
            });
            vm.item.userName=name;
            vm.item.userLogin=login;

            $("#selectEmployeeModel").modal("hide");
        };



        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrRegisterService.update(vm.item).then(function (value) {
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
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }

        vm.loadData();


        vm.provinceArr = provinceArr ; //省份数据
        vm.cityArr = cityArr;    //城市数据
        vm.getCityArr = vm.cityArr[0]; //默认为省份
        vm.getCityIndexArr = ['0','0'] ; //这个是索引数组，根据切换得出切换的索引就可以得到省份及城市
        //改变省份触发的事件 [根据索引更改城市数据]
        vm.provinceChange = function(index)
        {
            vm.getCityArr = vm.cityArr[index] ; //根据索引写入城市数据
            vm.getCityIndexArr[1] = '0' ; //清除残留的数据
            vm.getCityIndexArr[0] = index ;
            vm.item.province=vm.provinceArr[index];
       }
        //改变城市触发的事件
        vm.cityChange = function(index)
        {
            vm.item.city=vm.getCityArr[index];
        }
        return vm;

    })

    .controller("HrEmployeeProofController", function ($state, $scope, hrEmployeeProofService) {
        var vm = this;

        vm.params = {name: "", qName: "",deptName:"",userStatus:""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            var params = {q: vm.params.name,userLogin:user.userLogin,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrEmployeeProofService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        }


        vm.show = function (id) {
            $state.go("hr.employeeProofDetail", {employeeProofId: id});
        }


        vm.add = function () {
            hrEmployeeProofService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?", function (result) {
                if (result) {
                    hrEmployeeProofService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData(user.userLogin);
                    });
                }
            })
        }

        vm.loadPagedData(user.userLogin);

        return vm;

    })

    .controller("HrEmployeeProofDetailController", function ($state, $stateParams, $scope, hrEmployeeProofService) {
        var vm = this;
        var employeeProofId = $stateParams.employeeProofId;

        vm.loadData = function (loadProcess) {
            hrEmployeeProofService.getModelById(employeeProofId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrEmployeeProofService.update(vm.item).then(function (value) {
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
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }
        /*打印*/
        vm.print=function () {
            hrEmployeeProofService.getPrintData(employeeProofId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("在职证明");
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

    .controller("HrInsureController", function ($state, $scope, hrInsureService) {
        var vm = this;

        vm.params = {name: "", qName: "",deptName:"",userStatus:""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            var params = {q: vm.params.name,userLogin:user.userLogin,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrInsureService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        }

        vm.show = function (id) {
            $state.go("hr.insureDetail", {insureId: id});
        }


        vm.add = function () {
            hrInsureService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?", function (result) {
                if (result) {
                    hrInsureService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData(user.userLogin);
                    });
                }
            })
        }

        vm.loadPagedData(user.userLogin);

        return vm;

    })

    .controller("HrInsureDetailController", function ($state, $stateParams, $scope, hrInsureService) {
        var vm = this;
        var insureId = $stateParams.insureId;

        vm.loadData = function (loadProcess) {
            hrInsureService.getModelById(insureId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrInsureService.update(vm.item).then(function (value) {
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
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }
        /*打印*/
        vm.print=function () {
            hrInsureService.getPrintData(insureId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("在职证明");
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

    .controller("HrDetailController", function ($state, $scope,$rootScope,$stateParams,hrEmployeeService) {
        var vm = this;
        var owner=$stateParams.owner;
        vm.init = function () {

            vm.loadTree();

            hrEmployeeService.getModelByUserLogin(owner).then(function (value) {
                if(value.data.ret){
                    $scope.ownerInfo=value.data.data;
                }
            });
        }

        vm.loadTree=function(){
            var tree = $("#detailTree");

            var treeData=[
                { "id" : "hrBasic", "parent" : "#", "text" : "基础信息" ,"icon":"fa fa-user","state":{'selected':true}},
                { "id" : "hrResume", "parent" : "#", "text" : "简历情况" ,"icon":"fa fa-file-text"},
                { "id" : "hrEducation", "parent" : "#", "text" : "学历学位" ,"icon":"fa fa-graduation-cap"},
                { "id" : "hrTechnology", "parent" : "#", "text" : "专业技术职务" ,"icon":"fa fa-wrench"},
                { "id" : "hrProfession", "parent" : "#", "text" : "职业技能资格" ,"icon":"fa fa-tachometer"},
                { "id" : "hrFamily", "parent" : "#", "text" : "家庭成员" ,"icon":"fa fa-group"},
                { "id" : "hrExamine", "parent" : "#", "text" : "考核记录" ,"icon":"fa fa-pencil-square"},
                { "id" : "hrPartyPosition", "parent" : "#", "text" : "党政职务" ,"icon":"fa fa-university"},
                { "id" : "hrImportantGroup", "parent" : "#", "text" : "重要群体" ,"icon":"fa fa-star"},
                { "id" : "hrPartTimeJob", "parent" : "#", "text" : "兼任职务" ,"icon":"fa fa-life-ring"},
                { "id" : "hrQualification", "parent" : "#", "text" : "职业资格" ,"icon":"fa fa-bookmark"},
                { "id" : "hrAward", "parent" : "#", "text" : "奖励情况" ,"icon":"fa fa-gift"},
                { "id" : "hrHonor", "parent" : "#", "text" : "荣誉称号" ,"icon":"fa fa-tag"},
                { "id" : "hrPaper", "parent" : "#", "text" : "论文著作" ,"icon":"fa fa-file-text-o"},
                { "id" : "hrPatent", "parent" : "#", "text" : "专利情况" ,"icon":"fa fa-space-shuttle"},
                { "id" : "hrPunish", "parent" : "#", "text" : "惩处记录" ,"icon":"fa fa-arrows"},
                { "id" : "hrGoAbroad", "parent" : "#", "text" : "出国情况" ,"icon":"fa fa-paper-plane"},
                { "id" : "hrStandard", "parent" : "#", "text" : "标准规范" ,"icon":"fa fa-location-arrow"},
                // { "id" : "hrTrain", "parent" : "#", "text" : "培训计划" ,"icon":"fa fa-suitcase"},
                { "id" : "hrTrainMember", "parent" : "#", "text" : "培训参与" ,"icon":"fa fa-group"},
                { "id" : "hrCourse", "parent" : "#", "text" : "授课情况" ,"icon":"fa fa-slack"},
                { "id" : "hrProject", "parent" : "#", "text" : "重点项目" ,"icon":"fa fa-building"}
            ];
            tree.jstree("destroy");
            tree.jstree({
                'core': {
                    'data': treeData,
                    "multiple": false,
                    "themes": {
                        "responsive": false
                    }
                }
            });
            tree.on('changed.jstree', function (e, data) {
                if (data.selected.length > 0) {
                    var node = data.instance.get_node(data.selected[0]);
                    var redirect=getRedirectInfo(node.id);
                    $state.go(redirect.url,redirect.params);
                }
            });
        }


        function getRedirectInfo(nodeId) {
            var result={url:"hr.detail.form",params:{referType:nodeId}};
            if(nodeId=="hrPatent") {
              //怎么怎么
            }
            return result;
        }

        vm.init();

        return vm;
    })

    .controller("HrDetailFormController", function ($state, $scope,$state,$rootScope,$stateParams,hrDetailService,hrEmployeeService,commonFormService,commonFormDataService) {
        var vm = this;
        var owner = $stateParams.owner;
        var referType = $stateParams.referType;

        vm.params = {qq: "", q: "", qEnLogin: "", qqEnLogin: owner,referId:0};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize, total: $scope.pageSize};

        vm.init = function () {
            vm.params.referId=$scope.ownerInfo.id;
            commonFormService.getModelByKey(user.tenetId, referType).then(function (value) {
                if (value.data.ret) {
                    vm.template = value.data.data;
                }
            })
            vm.queryData();
        }

        vm.keyDown = function () {
            if (event.keyCode == 13)
                vm.queryData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.q = vm.params.qq;
            vm.params.qEnLogin = vm.params.qqEnLogin;
            vm.params.referId = 0;
            if (vm.params.qEnLogin) {
                hrEmployeeService.getModelByUserLogin(owner).then(function (value) {
                    if (value.data.ret && value.data.data) {
                        vm.params.referId = value.data.data.id;
                    }
                    vm.loadPagedData();
                });
            } else {
                vm.loadPagedData();
            }
        }

        vm.loadPagedData = function () {
            var params = {
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                enLogin: user.enLogin,
                referType: referType,
                referId: vm.params.referId,
                q: vm.params.q
            };
            commonFormDataService.listNewPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data.pageInfo;
                    vm.heads = value.data.data.heads;
                }
            });

        };

        vm.newData = function () {
            hrDetailService.getNewModel(referType, owner, user.enLogin).then(function (value) {
                vm.loadPagedData();
            })
        }

        vm.detail = function (item) {
            $state.go("hr.detail.formDetail", {formDataId: item.id})
        }

        vm.remove = function (id) {
            bootbox.confirm("确定要删除该表单记录吗", function (result) {
                if (result) {
                    commonFormDataService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.back=function(){
            $state.go("hr.dept");
        }

        vm.exportData=function(){
            var url="/hr/detail/downloadExcel.json";
            $scope.commonDownload(url,{referType:referType,owner:vm.params.qEnLogin});
        }

        $scope.$watch("ownerInfo", function () {
            if ($scope.ownerInfo) {
                vm.init();
            }
        });

        return vm;
    })

    .controller("HrDetailFormDetailController", function ($state, $scope, $rootScope,$stateParams, commonFormService,commonFormDataService) {
        var vm = this;
        var formDataId = $stateParams.formDataId;

        vm.init = function () {
            vm.loadData();
        }

        vm.loadData = function () {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.template = value.data.data.template;
                $scope.loadNewProcessInstance(vm.data.processInstanceId);
                if (!vm.data.processInstanceId) {
                    $scope.getTplConfig(vm.data.processInstanceId, vm.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
            })
        }

        vm.save=function() {
            var data = $scope.getFormData(vm.data.id, vm.groupList);
            data["autoSubmit"] = $scope.tplConfig.autoSubmit && vm.groupList;
            commonFormDataService.save(data).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            });
        }

        vm.back=function(){
            $state.go("hr.detail.form",{referType:vm.template.formKey});
        }

        vm.init();

        return vm;
    })

    .controller("HrTrainController", function ($state, $scope,$state,$rootScope,$stateParams,hrDetailService,hrEmployeeService,commonFormService,commonFormDataService) {

        var vm = this;
        var referType = "hrTrain";
        var referId = 0;
        vm.q = "";

        vm.init = function () {

            vm.loadData();

            $("input[type=search]").on('keypress', function () {
                if (event.keyCode == 13) {
                    vm.loadData();
                }
            })
        };

        vm.loadData = function () {
            commonFormDataService.listDataByUser(referType, vm.q, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    vm.data = value.data.data;

                }
            })
        }

        vm.newData = function () {

            commonFormDataService.getNewModelByUser(referType, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    vm.showDetail(value.data.data.id);
                } else {
                    toastr.error(value.data.msg);
                }
            })
        }

        vm.showDetail = function (id) {
            $state.go("hr.trainDetail", {formDataId: id})
        }

        vm.remove = function (id) {
            bootbox.confirm("确定删除该数据吗?", function (result) {
                if (result) {
                    commonFormDataService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData();
                        }
                    })
                }
            });

        }

        vm.init();
        return vm;
    })

    .controller("HrTrainDetailController", function ($state, $scope, $rootScope,$stateParams, commonFormService,commonFormDataService,hrTrainMemberService) {

        var vm = this;

        var formDataId = $stateParams.formDataId;

        vm.init = function () {
            vm.loadTrain();
            vm.loadData();
        }

        vm.loadTrain=function() {
            commonFormDataService.getFormDataById(formDataId).then(function (value) {
                vm.trainKey = value.data.data.trainKey;
                vm.loadHrMember();
            })
        }

        vm.loadData = function () {

            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.template = value.data.data.template;
                $scope.loadNewProcessInstance(vm.data.processInstanceId);
                if (!vm.data.processInstanceId) {
                    $scope.getTplConfig(vm.data.processInstanceId, vm.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
            })
        }

        vm.save=function() {
            var data = $scope.getFormData(vm.data.id, vm.groupList);
            data["autoSubmit"] = $scope.tplConfig.autoSubmit && vm.groupList;
            hrTrainMemberService.save(data).then(function (value) {
                if (value.data.ret) {
                    vm.loadTrain();
                    toastr.success("保存成功!");
                }
            });
        }

        vm.back=function(){
            $state.go("hr.train",{referType:vm.template.formKey});
        }

        vm.loadHrMember=function(){

            if(vm.trainKey) {
                hrTrainMemberService.listHrTrainMember(vm.trainKey).then(function (value) {
                    vm.hrTrainMembers = value.data.data;
                })
            }else{
                vm.hrTrainMembers=[];
            }
        }

        vm.selectHrTrainMember=function() {

            var selects = "";
            for (var i = 0; i < vm.hrTrainMembers.length; i++) {
                selects = selects + "," + vm.hrTrainMembers[i].userLogin;
            }

            var config = {
                title: "请选择培训人员",
                enLogin: user.enLogin,
                multiple: true,
                selects: selects
            };

            jQuery.showJsTreeSelectUserModal(config, function (nodes,ids,names) {
                hrTrainMemberService.newData(vm.trainKey, ids, user.enLogin).then(function (value) {
                    if(value.data.ret){
                        vm.loadHrMember();
                    }
                })
            })

        }

        vm.removeHrMember=function(id){
            hrTrainMemberService.removeHrMember(id).then(function (value){
                vm.loadHrMember();
            })
        }

        vm.showHrMember=function(detail){
            vm.hrMember = $.extend( {}, detail );
            $("#hrMemberModal").modal("show");
        }

        vm.saveHrMember=function() {
            vm.hrMember.formDataId=formDataId;
            hrTrainMemberService.saveHrMember(vm.hrMember).then(function (value) {
                if(value.data.ret) {
                    $("#hrMemberModal").modal("hide");
                    vm.loadHrMember();
                }
            })
        }

        vm.init();

        return vm;
    })

    .controller("HrIncomeProofController", function ($state, $scope, hrIncomeProofService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum,userLogin:user.userLogin, pageSize: vm.pageInfo.pageSize};
            hrIncomeProofService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };


        vm.show = function (id) {
            $state.go("hr.incomeProofDetail",{incomeProofId: id});
        }


        vm.add = function () {
            hrIncomeProofService.getNewModel(1, user.userLogin).then(function (value) {
                if (value.data.ret) {
                   vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    hrIncomeProofService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("HrIncomeProofDetailController", function ($state,$stateParams, $scope, hrIncomeProofService) {
        var vm = this;
        var incomeProofId=$stateParams.incomeProofId;

        vm.loadData = function (loadProcess) {

            hrIncomeProofService.getModelById(incomeProofId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadEdFile(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrIncomeProofService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
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

    .controller("HrLeaveController", function ($state, $scope, hrLeaveService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum,userLogin:user.userLogin,pageSize: vm.pageInfo.pageSize};
            hrLeaveService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };


        vm.show = function (id) {
            $state.go("hr.leaveDetail",{leaveId: id});
        }


        vm.add = function () {
            hrLeaveService.getNewModel(1, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    hrLeaveService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("HrLeaveDetailController", function ($state,$stateParams, $scope, hrLeaveService) {
        var vm = this;
        var leaveId=$stateParams.leaveId;

        vm.loadData = function (loadProcess) {

            hrLeaveService.getModelById(leaveId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#approveLeaveTime").datepicker('setDate',vm.item.approveLeaveTime);
                    $("#applyLeaveTime").datepicker('setDate',vm.item.applyLeaveTime);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrLeaveService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow=function () {
            if ($("#detail_form").validate().form()) {
                vm.save();
                $scope.showSendFlow();
            } else {
                toastr.warning("请准确填写数据!")
                return;
            }

        }

        vm.loadData(true);
        return vm;

    })

    .controller("HrVacationController", function ($state, $scope, hrVacationService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum,userLogin:user.userLogin, pageSize: vm.pageInfo.pageSize};
            hrVacationService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };


        vm.show = function (id) {
            $state.go("hr.vacationDetail",{vacationId: id});
        }

        vm.add = function () {
            hrVacationService.getNewModel(1, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    hrVacationService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("HrVacationDetailController", function ($state,$stateParams, $scope, hrVacationService,hrVacationDetailService) {
        var vm = this;
        var vacationId=$stateParams.vacationId;

        vm.loadData = function (loadProcess) {
            hrVacationService.getModelById(vacationId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadEdFile(vm.item.businessKey);
                    }
                }
            })
            vm.listVacationDetail();
        };

        vm.listVacationDetail=function(){
            hrVacationDetailService.listDataByForeignKey(vacationId).then(function (value) {
                if (value.data.ret){
                    vm.list=value.data.data;
                }
            })
        };

        vm.addVacationDetail=function(){
            hrVacationDetailService.getNewModel(vacationId).then(function (value) {
                vm.editVacationDetail(value.data.data);
            })
        }

        vm.editVacationDetail = function (id) {
            hrVacationDetailService.getModelById(id).then(function (value) {
                vm.vacationDetail = value.data.data;
                vm.planEnd=value.data.data.planEnd;
                vm.planBegin=value.data.data.planBegin;
                $("#vacationDetail").modal("show");
            });
        }

        vm.deleteVacationDetail=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    hrVacationDetailService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.listVacationDetail();
                        }
                    })
                }
            })
        };


        vm.updateVacationDetail=function(){
            if($("#VacationDetail_form").validate().form()){
                vm.item.operateUserLogin=user.userLogin;
                hrVacationDetailService.update(vm.vacationDetail).then(function (value) {
                    if(value.data.ret){
                        toastr.success("修改成功!");
                        $("#vacationDetail").modal("hide");
                        vm.loadData();
                    }
                })
            }
        }

        vm.changePlanDay=function(){
            var beginTime=vm.vacationDetail.planBegin;
            var endTime=vm.vacationDetail.planEnd;
            beginTime=  beginTime.replace(/年/g,"/");
            beginTime=  beginTime.replace(/月/g,"/");
            beginTime=  beginTime.replace(/日/g,"/");
            endTime=  endTime.replace(/年/g,"/");
            endTime=  endTime.replace(/月/g,"/");
            endTime=  endTime.replace(/日/g,"/");
            var beginDate=(Number)(new Date(Date.parse(beginTime)));
            var endDate=(Number)(new Date(Date.parse(endTime)));
            var planDay=(endDate-beginDate)/(1000*3600*24)
            if (planDay<0){
                toastr.error("计划开始时间不能大于计划结束时间!");
            }else {
                vm.vacationDetail.planDay=planDay;
            }
        }
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrVacationService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
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

    .controller("HrDeptController", function ($state, $scope, $rootScope, hrDeptService,commonCodeService,hrEmployeeService,sysRoleService,hrPositionService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var selectId=0;
        var tableName = $rootScope.loadTableName("hr.dept");

        vm.init=function(){
            vm.params= getCacheParams(key, {qDeptName: "", qUserName: "", deptName: "", userName: "",parentDeptId:0, pageNum: 1, pageSize: $scope.pageSize,total:0 });
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
            vm.buildTree();
            hrDeptService.selectAll().then(function (value) {
                vm.depts=value.data.data;
            });
            $scope.basicInit("");
            //回车事件监听
            $('#applyCertNum').bind('keydown',function(event){
                if(event.keyCode == "13") {
                    vm.queryData();
                }
            });


            $('#btnUpload').fileupload({
                url: '/hr/uploadExcel.json',
                maxNumberOfFiles: 1,//最大上传文件数目
                add: function (e, data) {
                    data.formData={enLogin:user.enLogin}
                    data.submit();
                },
                done: function (e, data) {
                    if (data.result.ret) {
                        toastr.success("上传成功!");
                    } else {
                        toastr.error(data.result.msg);
                    }
                }
            });

        }

        vm.loadPagedData = function () {
            hrEmployeeService.listPagedData({
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

        vm.queryData = function () {
            vm.params.parentDeptId=selectId;
            vm.params.qUserName = vm.params.userName;
            vm.params.qDeptName = vm.params.deptName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

            hrDeptService.getModelById(selectId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                }
            })
            vm.loadCandicate(selectId);

        };

        vm.enterEvent = function(e) {
            var keycode = window.event?e.keyCode:e.which;
            if(keycode==13){
                vm.queryData();
            }
        }

        vm.show = function (userLogin) {
            $state.go("hr.basicAdmin", {userLogin: userLogin});
        }

        vm.showUserNo=function(id){
            hrEmployeeService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.hrEmployee=value.data.data;
                    $("#updateEmployeeModal").modal("show");
                }
            })
        }
        vm.saveUserNo=function(){
            vm.hrEmployee.operateUserLogin=user.userLogin;
            hrEmployeeService.update(vm.hrEmployee).then(function (value) {
                if (value.data.ret) {
                    toastr.success("修改成功!");
                    $("#updateEmployeeModal").modal("hide");
                    vm.queryData();
                }
            })
        }
        vm.remove = function (userLogin) {
            bootbox.confirm("你确认要删除该条数据吗？删除后无法恢复", function (result) {
                if (result) {
                    hrEmployeeService.remove(userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.loadEmployee(selectId);
                        }
                    })
                }
            })
        }

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
                        node.state.opened = true;
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

        vm.loadDept=function(){

            hrDeptService.getModelById(selectId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                }
            })

            vm.loadCandicate(selectId);

            vm.loadEmployee(selectId);
        }

        vm.loadCandicate=function(id){
            hrDeptService.listParentCandicate(id).then(function (value) {
                if(value.data.ret){
                    vm.parents=value.data.data;
                }
            })
        }

        vm.loadEmployee=function(id){
            hrEmployeeService.listEmployeeByDeptId(id,true).then(function (value) {
                if(value.data.ret){
                    vm.employees=value.data.data;
                }
            })
        }

        vm.add=function(){
            hrDeptService.getNewModel(selectId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                    vm.loadCandicate(vm.item.id);
                    vm.employees=[];

                    toastr.success("新增部门成功!");
                    vm.buildTree();
                    selectId=vm.item.id;
                    vm.queryData();

                    $("#infoTab").attr('class','active');
                    $("#deptTab").attr('class','');

                    $("#info_tab").attr('class','tab-pane active');
                    $("#dept_tab").attr('class','tab-pane');
                }
            });
        }

        vm.update=function() {
            hrDeptService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("更新成功!");
                    vm.buildTree();
                }
            });
        }


        vm.showUserModel = function (flag) {
            vm.flag =flag;
            if(flag=="first"){
                $scope.showSelectEmployeeModal_({title:"请选择部门正职领导", userLoginList: vm.item.deptFirstLeader,multiple:true,parentDeptId:vm.params.parentDeptId});
            }else if(flag=="second"){
                $scope.showSelectEmployeeModal_({title:"请选择部门副职领导", userLoginList: vm.item.deptSecondLeader,multiple:true,parentDeptId:vm.params.parentDeptId});
            }else if (flag=="charge"){
                $scope.showSelectEmployeeModal_({title:"请选择部门分管领导", userLoginList: vm.item.deptChargeMan,multiple:true,parentDeptId:16});
            }else if (flag=="finance"){
                $scope.showSelectEmployeeModal_({title:"请选择部门财务人员", userLoginList: vm.item.deptFinanceMan,multiple:true,parentDeptId:vm.params.parentDeptId});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if(vm.flag=="first"){
                vm.item.deptFirstLeader = $scope.selectedUserLogins_;
                vm.item.deptFirstLeaderName = $scope.selectedUserNames_;
            }else if(vm.flag=="second"){
                vm.item.deptSecondLeader = $scope.selectedUserLogins_;
                vm.item.deptSecondLeaderName = $scope.selectedUserNames_;
            }else if (vm.flag=="charge"){
                vm.item.deptChargeMan = $scope.selectedUserLogins_;
                vm.item.deptChargeManName = $scope.selectedUserNames_;
            }else if (vm.flag=="finance"){
                vm.item.deptFinanceMan = $scope.selectedUserLogins_;
                vm.item.deptFinanceManName = $scope.selectedUserNames_;
            }
        };

        vm.showEmployee=function(userLogin){
            $state.go("hr.employeeDetail",{userLogin:userLogin});
        }

        /*     vm.downloadModel = function () {
            hrEmployeeService.downloadModel(user.userLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("filename=")).replace("filename=","");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            })
        };*/

        vm.insertEmployee=function(){
            if ($("#employee_form").validate().form()) {
                bootbox.confirm("你确认要新增用户吗?", function (result) {
                    if (result) {
                        hrEmployeeService.insert(vm.employee.userLogin, vm.employee.userName, vm.employee.deptId, vm.employee.userType).then(function (value) {
                            if (value.data.ret) {
                                $("#addEmployeeModal").modal("hide");
                                vm.loadPagedData();
                            }
                        })
                    }

                });
            }
        }
        vm.addEmployee = function () {
            hrEmployeeService.getNewModel().then(function (value) {
                vm.employee=value.data.data;
                vm.selectAllDept(vm.employee.deptId)
            });
            $("#addEmployeeModal").modal("show");
        }

        vm.exportData=function(){
            var url="/hr/detail/downloadExcel.json";
            $scope.commonDownload(url);
        }


        vm.selectAllDept=function(deptId){
            hrDeptService.selectAll().then(function (value) {
                vm.depts=value.data.data;
                for (var i=0;i<vm.depts.length;i++) {
                    if (vm.depts[i].id == deptId) {
                        vm.deptNameId = vm.depts[i].name;
                    }
                }
            })
        }
        vm.showRoleModal=function(item){
            vm.current=item;
            sysRoleService.selectAll().then(function (value) {
                if(value.data.ret){
                    vm.roles=value.data.data;
                }
            })
            $("#roleSelectModal").modal("show");
        }

        vm.showPositionModal=function(item){
            vm.current=item;
            hrPositionService.selectAll().then(function (value) {
                if(value.data.ret){
                    vm.positions=value.data.data;
                }
            })

            $("#positionSelectModal").modal("show");
        }
        vm.showDeptModal=function(item) {
            vm.current = item;
            $('#dept_select_tree').jstree("destroy");
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
                    if (item.id ===  vm.current.deptId) {
                        node.state.selected = true;
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

        vm.showDeptModalById=function(deptId) {
            $('#dept_select_tree1').jstree("destroy");
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
                    if (item.id ==deptId) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree1').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModalId").modal("show");

        }

        vm.showTypeModal=function(item){
            vm.current=item;
            commonCodeService.listDataByCatalog(user.userLogin,"员工类型").then(function (value) {
                if(value.data.ret){
                    vm.type=value.data.data;
                    singleCheckBox(".cb_type","data-name");
                }
            })
            $("#typeSelectModal").modal("show");
        }
        //员工状态
        vm.showStatusModal=function(item){
            vm.current=item;
            commonCodeService.listDataByCatalog(user.userLogin,"员工状态").then(function (value) {
                if(value.data.ret){
                    vm.status=value.data.data;
                    singleCheckBox(".cb_status","data-name");
                }
            })
            $("#statusSelectModal").modal("show");
        }
        vm.saveStatus=function(){
            var status;
            $(".cb_status:checked").each(function () {
                status=$(this).attr("data-name");
            });

            hrEmployeeService.updateStatus(vm.current.userLogin,status).then(function (value) {
                if(value.data.ret){
                    $("#statusSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });

        }
        //默认不显示用户状态 且只保留在职
        vm.userStatus =false;
        vm.userFilfter ='在职';
        vm.showUserStatus = function () {
            setTimeout(function () {
                if($("#showUserStatus").prop('checked')){
                    vm.userStatus =true;
                    vm.userFilfter ="";
                    vm.loadPagedData();
                }else{
                    vm.userStatus =false;
                    vm.userFilfter ='在职';
                    vm.loadPagedData();
                }
            },200)
        }


        vm.saveDeptById=function(){
            var select = $('#dept_select_tree1').jstree(true).get_selected(true)[0];
            vm.employee.deptId=select.id;
            vm.deptNameId=select.text;
            $("#deptSelectModalId").modal("hide");
        }

        vm.saveDept=function() {
            var select = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            hrEmployeeService.updateDeptId(vm.current.userLogin, select.id).then(function (value) {
                if (value.data.ret) {
                    $("#deptSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });
        }
        vm.saveRole=function(){
            var roleIds = [];
            $(".cb_role:checked").each(function () {
                roleIds.push($(this).attr("data-id"));
            });

            hrEmployeeService.updateRoleIds(vm.current.userLogin,roleIds.join(',')).then(function (value) {
                if(value.data.ret){
                    $("#roleSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });

        }
        vm.savePosition=function(){
            var positionIds = [];
            $(".cb_position:checked").each(function () {
                positionIds.push($(this).attr("data-id"));
            });
            hrEmployeeService.updatePositionIds(vm.current.userLogin,positionIds.join(',')).then(function (value) {
                if(value.data.ret){
                    $("#positionSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });
        }
        vm.saveType=function(){
            var type;
            $(".cb_type:checked").each(function () {
                type=$(this).attr("data-name");
            });

            hrEmployeeService.updateType(vm.current.userLogin,type).then(function (value) {
                if(value.data.ret){
                    $("#typeSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });

        }



        vm.resetPwd=function(userLogin){
            bootbox.confirm("你确认要重置"+userLogin+"的密码吗?", function (result) {
                if (result) {
                    hrEmployeeService.resetPwd(userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("重置密码(" + value.data.data + ")成功");
                        }
                    })
                }
            });
        }

        vm.showUserDeptConfig=function(employee){
            if(employee) {
                vm.currentUser = employee;
            }
            sysRoleService.listMyAclInfoByUserLogin(vm.currentUser.userLogin).then(function (value) {
                vm.myAclInfoList=value.data.data;
            });
            $("#userAclDeptConfigModal").modal("show");
        }

        vm.showUserAclDeptSelectModal=function(item){
            vm.aclInfo=item;
            var ids=item.selectDepts;
            ;
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
                    if(ids.indexOf(","+item.id+",")>=0){
                        node.state.selected=true;
                    }
                    vm.treeData.push(node);
                }
                $('#user_acl_dept_select_tree').jstree("destroy");
                $('#user_acl_dept_select_tree')
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
            $("#userAclDeptSelectModal").modal("show");
        }

        vm.saveAclSelectDept=function() {
            var deptList = $('#user_acl_dept_select_tree').jstree(true).get_selected();
            var selectDepts = '';
            if (deptList.length > 0) {
                selectDepts = ',' + deptList.join(',') + ',';
            }
            sysRoleService.saveUserAclDeptConfig(vm.currentUser.userLogin, vm.aclInfo.aclId, selectDepts).then(function (value) {
                if(value.data.ret){
                    toastr.success("保存成功!");
                    vm.showUserDeptConfig();
                }
            });
            $("#userAclDeptSelectModal").modal("hide");
        }

        vm.showUserAclTree = function () {
            sysRoleService.getAclTreeByUserLogin(vm.currentUser.userLogin).then(function (value) {
                if (value.data.ret) {
                    var treeData = value.data.data;
                    $('#user_acl_select_tree').jstree("destroy");
                    $('#user_acl_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            },
                            "checkbox" : {
                                "keep_selected_style" : false
                            },
                            "plugins" : [ "wholerow", "checkbox" ]
                        });
                    $("#userAclSelectModal").modal("show");
                }
            });
        };

        vm.saveUserSelectAcl = function () {
            var userList = $('#user_acl_select_tree').jstree(true).get_selected().join(',');
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    sysRoleService.saveUserAclList(vm.currentUser.userLogin,userList).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("更新成功!");
                            $("#userAclSelectModal").modal("hide");
                            vm.showUserDeptConfig();
                        }
                    });
                }
            });
        };


        vm.removeUserAclDeptById=function(item){
            sysRoleService.removeUserAcl(vm.currentUser.userLogin,item.aclId,"dept").then(function (value) {
                if(value.data.ret){
                    toastr.success("删除特殊数据权限配置成功!");
                    vm.showUserDeptConfig();
                }
            });
        }

        vm.removeUserAclOptById=function(item){
            sysRoleService.removeUserAcl(vm.currentUser.userLogin,item.aclId,"opt").then(function (value) {
                if(value.data.ret){
                    toastr.success("删除特殊数据权限配置成功!");
                    vm.showUserDeptConfig();
                }
            });
        }

        vm.removeUserAclById=function(item){
            sysRoleService.removeUserAcl(vm.currentUser.userLogin,item.aclId,"all").then(function (value) {
                if(value.data.ret){
                    toastr.success("删除特殊数据权限配置成功!");
                    vm.showUserDeptConfig();
                }
            });
        }

        vm.detail=function(owner){
            $state.go("hr.detail",{owner:owner});
        }

        vm.init();

        return vm;

    })

    .controller("HrDeptUserController", function ($state, $scope, $rootScope, hrDeptService,commonCodeService,hrEmployeeService,sysRoleService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var selectId=0;
        var tableName = $rootScope.loadTableName("hr.deptUser");

        vm.init=function(){
            vm.params= getCacheParams(key, {qDeptName: "", qUserName: "", deptName: "", userName: "",parentDeptId:0, pageNum: 1, pageSize: $scope.pageSize,total:0 });
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
            vm.userFilfter ='在职';
            vm.buildTree();
            hrDeptService.selectAll().then(function (value) {
                vm.depts=value.data.data;
            });
            $scope.basicInit("");

            //回车事件监听
            $('#applyCertNum').bind('keydown',function(event){
                if(event.keyCode == "13") {
                    vm.queryData();
                }
            });
        }

        vm.loadPagedData = function () {
            hrEmployeeService.listPagedData({
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

        vm.queryData = function () {
            vm.params.parentDeptId=selectId;
            vm.params.qUserName = vm.params.userName;
            vm.params.qDeptName = vm.params.deptName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

            hrDeptService.getModelById(selectId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                }
            })

            vm.loadCandicate(selectId);

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
                        node.state.opened = true;
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

        vm.loadDept=function(){

            hrDeptService.getModelById(selectId).then(function (value) {
                if(value.data.ret){
                    vm.item=value.data.data;
                }
            })

            vm.loadCandicate(selectId);

            vm.loadEmployee(selectId);
        }

        vm.loadCandicate=function(id){
            hrDeptService.listParentCandicate(id).then(function (value) {
                if(value.data.ret){
                    vm.parents=value.data.data;
                }
            })
        }

        vm.loadEmployee=function(id){
            hrEmployeeService.listEmployeeByDeptId(id,true).then(function (value) {
                if(value.data.ret){
                    vm.employees=value.data.data;
                }
            })
        }

        vm.showDeptModal=function(item) {
            vm.current = item;
            $('#dept_select_tree').jstree("destroy");
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
                    if (item.id ===  vm.current.deptId) {
                        node.state.selected = true;
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

        vm.showDeptModalById=function(deptId) {
            $('#dept_select_tree1').jstree("destroy");
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
                    if (item.id ==deptId) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree1').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModalId").modal("show");

        }

        vm.showTypeModal=function(item){
            vm.current=item;
            commonCodeService.listDataByCatalog(user.userLogin,"员工类型").then(function (value) {
                if(value.data.ret){
                    vm.type=value.data.data;
                    singleCheckBox(".cb_type","data-name");
                }
            })
            $("#typeSelectModal").modal("show");
        }

        vm.showUserAclTree = function () {
            sysRoleService.getAclTreeByUserLogin(vm.currentUser.userLogin).then(function (value) {
                if (value.data.ret) {
                    var treeData = value.data.data;
                    $('#user_acl_select_tree').jstree("destroy");
                    $('#user_acl_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            },
                            "checkbox" : {
                                "keep_selected_style" : false
                            },
                            "plugins" : [ "wholerow", "checkbox" ]
                        });
                    $("#userAclSelectModal").modal("show");
                }
            });
        };

        vm.init();

        return vm;

    })

    .controller("HrEmployeeController", function ($state, $scope, hrEmployeeService,commonCodeService,sysRoleService,hrPositionService,hrDeptService) {

        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params= getCacheParams(key, {q: "", deptName: "", selectText:"人员信息-在职",userStatus: "", pageNum: 1, pageSize: $scope.pageSize,total:0 });
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.init = function () {

            commonCodeService.listDataByCatalog(user.userLogin,"员工状态").then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                var root = {id: 0, parent: "#", text: "人员信息", state: {opened: true, disabled: false, selected: false}};
                if(root.text=== vm.params.selectText){
                    root.state.selected=true;
                }
                vm.treeData.push(root);
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent:"0",
                        text: '人员信息-'+item.name,
                        state: {opened: false, disabled: false, selected: false}
                    };
                    if (node.text ===  vm.params.selectText) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#js_tree').jstree("destroy");
                $('#js_tree')
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if( vm.params.selectText==node.text){
                            vm.loadPagedData();
                        }else{
                            vm.params.selectText=node.text;
                            vm.params.q="";
                            vm.params.qDeptName="";
                            vm.queryData();
                        }
                    })
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        }
                    });
            });

            $scope.basicInit();
        };

        vm.queryData = function () {

            if( vm.params.selectText.split("-").length>1){
                vm.params.userStatus= vm.params.selectText.split("-")[1];
            }
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {q: vm.params.q,deptName: vm.params.deptName,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrEmployeeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    params.selectText=vm.params.selectText;
                    setCacheParams(key,params,vm.pageInfo);
                }
            })

        }

        vm.show = function (userLogin) {
            $state.go("hr.basicDetail", {userLogin: userLogin});
        }
        vm.remove = function (userLogin) {
            bootbox.confirm("你确认要删除该条数据吗？删除后无法恢复", function (result) {
                if (result) {
                    hrEmployeeService.remove(userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.loadPagedData();
                        }
                    })
                }
            })
        }


        vm.addEmployee = function () {
            hrEmployeeService.getNewModel().then(function (value) {
                vm.employee=value.data.data;
                hrDeptService.selectAll().then(function (value) {
                    vm.depts=value.data.data;
                    for (var i=0;i<vm.depts.length;i++) {
                        if (vm.depts[i].id == vm.employee.deptId) {
                            vm.deptNameId = vm.depts[i].name;
                        }
                    }
                })
            });

            $("#addEmployeeModal").modal("show");
        }

        vm.insertEmployee=function(){
            if ($("#employee_form").validate().form()) {
                bootbox.confirm("你确认要新增用户吗?", function (result) {
                    if (result) {
                        hrEmployeeService.insert(vm.employee.userLogin, vm.employee.userName, vm.employee.deptId, vm.employee.userType).then(function (value) {
                            if (value.data.ret) {
                                $("#addEmployeeModal").modal("hide");
                                setTimeout(function () {
                                    $state.go("hr.employeeDetail", {userLogin:vm.employee.userLogin});
                                },500);

                            }
                        })
                    }

                });
            }

        }




        vm.showRoleModal=function(item){
            vm.current=item;
            sysRoleService.selectAll().then(function (value) {
                if(value.data.ret){
                    vm.roles=value.data.data;
                }
            })
            $("#roleSelectModal").modal("show");
        }

        vm.showPositionModal=function(item){
            vm.current=item;
            hrPositionService.selectAll().then(function (value) {
                if(value.data.ret){
                    vm.positions=value.data.data;
                }
            })
            $("#positionSelectModal").modal("show");
        }

        vm.showDeptModal=function(item) {
            vm.current = item;
            $('#dept_select_tree').jstree("destroy");
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
                    if (item.id ===  vm.current.deptId) {
                        node.state.selected = true;
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
            var select = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            hrEmployeeService.updateDeptId(vm.current.userLogin, select.id).then(function (value) {
                if (value.data.ret) {
                    $("#deptSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });
        }

        vm.saveRole=function(){
            var roleIds = [];
            $(".cb_role:checked").each(function () {
                roleIds.push($(this).attr("data-id"));
            });

            hrEmployeeService.updateRoleIds(vm.current.userLogin,roleIds.join(',')).then(function (value) {
                if(value.data.ret){
                    $("#roleSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });

        }

        vm.savePosition=function(){
            var positionIds = [];
            $(".cb_position:checked").each(function () {
                positionIds.push($(this).attr("data-id"));
            });
            hrEmployeeService.updatePositionIds(vm.current.userLogin,positionIds.join(',')).then(function (value) {
                if(value.data.ret){
                    $("#positionSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadPagedData();
                }
            });
        }

        vm.resetPwd=function(userLogin){
            bootbox.confirm("你确认要重置"+userLogin+"的密码吗?", function (result) {
                if (result) {
                    hrEmployeeService.resetPwd(userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("重置密码(" + value.data.data + ")成功");
                        }
                    })
                }
            });
        }
        /*新增用户-部门选择*/
        vm.showDeptModalById=function(deptId) {
            $('#dept_select_tree1').jstree("destroy");
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
                    if (item.id ==deptId) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree1').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModalId").modal("show");

        }
        /*新增用户-部门选择*/
        vm.saveDeptById=function(){
            var select = $('#dept_select_tree1').jstree(true).get_selected(true)[0];
            vm.employee.deptId=select.id;
            vm.deptNameId=select.text;
            $("#deptSelectModalId").modal("hide");
        }

        vm.init();

        return vm;

    })

    .controller("HrRegisterController", function ($state, $scope, hrRegisterService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;

        vm.init=function(){
            vm.params = getCacheParams(key,{ q: "",deptName:"",userStatus:"", pageNum: 1, pageSize: $scope.pageSize,total:0 });
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
            vm.loadPagedData();
        }


        vm.loadPagedData = function () {
            var params = {q: vm.params.q,deptName: vm.params.deptName,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrRegisterService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,params,vm.pageInfo);
                }
            })
        }


        vm.show = function (registerId) {
            $state.go("hr.registerDetail", {registerId: registerId});
        }


        vm.add = function () {
            hrRegisterService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    var v=value.data.data;
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?", function (result) {
                if (result) {
                    hrRegisterService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData(user.userLogin);
                    });
                }
            })
        }

        vm.downloadModel = function() {
            hrRegisterService.downloadModel(user.userLogin).then(function (response) {
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
            url:'/hr/register/receive.do?userLogin='+user.userLogin,
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
                    vm.loadPagedData();
                }
            }
        });
        vm.init();

        return vm;

    })

    .controller("HrRegisterDetailController", function ($state, $stateParams, $scope, hrRegisterService,commonCodeService,hrEmployeeService) {
        var vm = this;
        var registerId = $stateParams.registerId;


        vm.loadData = function () {
            $scope.basicInit();

            hrRegisterService.getModelById(registerId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.provinceId=""+provinceArr.indexOf(vm.item.province);
                    vm.cityId =""+cityArr[vm.provinceId].indexOf(vm.item.city);
                    vm.getCityArr = vm.cityArr[vm.provinceId] ; //根据索引写入城市数据
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                    $("#endTime").datepicker('setDate', vm.item.endTime);
                    $("#gmtCreate").datepicker('setDate', new Date());
                    $("#gmtModified").datepicker('setDate', new Date());

                    $scope.processInstance={myTaskFirst:true};
                    $scope.basicInit(vm.item.businessKey);
                }
            })


        };
        vm.showSelectMajorModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            });
            $("#selectMajorModal").modal("show");

        }
        vm.selectMajor=function(){
            var majors = [];
            $(".cb_destMajor:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            vm.item.specialty = majors.join(',');
            $("#selectMajorModal").modal("hide");
        }

        vm.showEmployeeModel=function(){

            hrEmployeeService.selectAll().then(function(value) {
                vm.employees=value.data.data;
                singleCheckBox(".cb_employee","data-name");
            });
            $("#selectEmployeeModel").modal("show");
        }

        vm.saveSelectEmployee=function(){

            var login;
            var name ;
            $(".cb_employee:checked").each(function () {
                var info = $(this).attr("data-name");
                login=info.split('_')[0];
                name=info.split('_')[1];
            });
            vm.item.userName=name;
            vm.item.userLogin=login;

            $("#selectEmployeeModel").modal("hide");
        };



        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrRegisterService.update(vm.item).then(function (value) {
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
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }

        vm.loadData();


        vm.provinceArr = provinceArr ; //省份数据
        vm.cityArr = cityArr;    //城市数据
        vm.getCityArr = vm.cityArr[0]; //默认为省份
        vm.getCityIndexArr = ['0','0'] ; //这个是索引数组，根据切换得出切换的索引就可以得到省份及城市
        //改变省份触发的事件 [根据索引更改城市数据]
        vm.provinceChange = function(index)
        {
            vm.getCityArr = vm.cityArr[index] ; //根据索引写入城市数据
            vm.getCityIndexArr[1] = '0' ; //清除残留的数据
            vm.getCityIndexArr[0] = index ;
            vm.item.province=vm.provinceArr[index];
       }
        //改变城市触发的事件
        vm.cityChange = function(index)
        {
            vm.item.city=vm.getCityArr[index];
        }
        return vm;

    })

    .controller("HrEmployeeProofController", function ($state, $scope, hrEmployeeProofService) {
        var vm = this;

        vm.params = {name: "", qName: "",deptName:"",userStatus:""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            var params = {q: vm.params.name,userLogin:user.userLogin,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrEmployeeProofService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        }


        vm.show = function (id) {
            $state.go("hr.employeeProofDetail", {employeeProofId: id});
        }


        vm.add = function () {
            hrEmployeeProofService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?", function (result) {
                if (result) {
                    hrEmployeeProofService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData(user.userLogin);
                    });
                }
            })
        }

        vm.loadPagedData(user.userLogin);

        return vm;

    })

    .controller("HrEmployeeProofDetailController", function ($state, $stateParams, $scope, hrEmployeeProofService) {
        var vm = this;
        var employeeProofId = $stateParams.employeeProofId;

        vm.loadData = function (loadProcess) {
            hrEmployeeProofService.getModelById(employeeProofId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrEmployeeProofService.update(vm.item).then(function (value) {
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
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }
        /*打印*/
        vm.print=function () {
            hrEmployeeProofService.getPrintData(employeeProofId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("在职证明");
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

    .controller("HrInsureController", function ($state, $scope, hrInsureService) {
        var vm = this;

        vm.params = {name: "", qName: "",deptName:"",userStatus:""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            var params = {q: vm.params.name,userLogin:user.userLogin,userStatus:vm.params.userStatus,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrInsureService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        }

        vm.show = function (id) {
            $state.go("hr.insureDetail", {insureId: id});
        }


        vm.add = function () {
            hrInsureService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?", function (result) {
                if (result) {
                    hrInsureService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData(user.userLogin);
                    });
                }
            })
        }

        vm.loadPagedData(user.userLogin);

        return vm;

    })

    .controller("HrInsureDetailController", function ($state, $stateParams, $scope, hrInsureService) {
        var vm = this;
        var insureId = $stateParams.insureId;

        vm.loadData = function (loadProcess) {
            hrInsureService.getModelById(insureId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrInsureService.update(vm.item).then(function (value) {
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
                    vm.save();
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            }
            $scope.showSendFlow();
        }
        /*打印*/
        vm.print=function () {
            hrInsureService.getPrintData(insureId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("在职证明");
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
