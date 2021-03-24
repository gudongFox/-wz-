angular.module('controllers.oa1', [])

    .controller("OaSoftwareApplyController", function ($state,$stateParams,$scope,oaSoftwareApplyService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{softwareNames:"",deptNames:"",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,
                deptNames:vm.params.deptNames,softwareNames:vm.params.softwareNames};
            oaSoftwareApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,params,vm.pageInfo);
                }
            })
        };
        vm.show = function (id) {
            $state.go("oa.softwareApplyDetail",{id: id});
        }

        vm.add = function () {
            oaSoftwareApplyService.getNewModel(user.userLogin,"knowledge").then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaSoftwareApplyService.remove(id,user.userLogin).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;
    })

    .controller("OaSoftwareApplyDetailController", function ($state,$stateParams,$scope, oaSoftwareApplyService) {
        var vm = this;
        var id = $stateParams.id;

        vm.loadData = function (loadProcess) {
            oaSoftwareApplyService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
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
            oaSoftwareApplyService.update(vm.item).then(function (value) {
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
                    oaSoftwareApplyService.update(vm.item).then(function (value) {
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

        vm.loadData(true);
        return vm;

    });