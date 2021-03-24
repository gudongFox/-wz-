angular.module('controllers.oa2', [])

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

    })

    .controller("OaMeetingRoomController", function ($state, $scope, oaMeetingRoomService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,roomNames:vm.pageInfo.roomName};
            oaMeetingRoomService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("oa.meetingRoomDetail",{id: id});
        }


        vm.add = function () {
            oaMeetingRoomService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaMeetingRoomService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("OaMeetingRoomDetailController", function ($state,$stateParams ,$scope, oaMeetingRoomService) {
        var vm = this;
        var id = $stateParams.id;

        vm.loadData = function (loadProcess) {
            oaMeetingRoomService.getModelById(id).then(function (value) {
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
            oaMeetingRoomService.update(vm.item).then(function (value) {
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
                    oaMeetingRoomService.update(vm.item).then(function (value) {
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


    })

    .controller("OaMeetingRoomApplyController", function ($state, $scope, oaMeetingRoomService,oaMeetingRoomApplyService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,meetingRoomId:vm.params.roomId};
            oaMeetingRoomApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            oaMeetingRoomService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.meetingRoom = value.data.data.list;

                }
            })
        };
        vm.show = function (id) {
            $state.go("oa.meetingRoomApplyDetail",{id: id});
        }


        vm.add = function () {
            oaMeetingRoomApplyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaMeetingRoomApplyService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("OaMeetingRoomApplyDetailController", function ($state,$stateParams ,$scope, oaMeetingRoomApplyService,oaMeetingRoomService) {
        var vm = this;
        var id = $stateParams.id;

        vm.loadData = function (loadProcess) {
            oaMeetingRoomApplyService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        vm.listAllRoom = function(){
            // var params = {roomNames:vm.pageInfo.roomName};
            oaMeetingRoomService.listAllRoom().then(function (value) {
                if(value.data.ret){
                    vm.listAll = value.data.data;
                    singleCheckBox(".cb_meetingRoom","data-name");
                    $("#meetingRoomModel").modal("show");
                }
            })
        };

        vm.selectMeetingRoom = function(){
            $(".cb_meetingRoom:checked").each(function () {
                var value = $.parseJSON($(this).attr("data-name"));
                vm.item.meetingRoomName = value.roomName;
                vm.item.meetingRoomId = value.id;
            });
            $("#meetingRoomModel").modal("hide");

        };


        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            oaMeetingRoomApplyService.update(vm.item).then(function (value) {
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
                    oaMeetingRoomApplyService.update(vm.item).then(function (value) {
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


    })

    .controller("OaCarController", function ($state, $scope, oaCarService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carNos:vm.pageInfo.carNo};
            oaCarService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };
        vm.show = function (id) {
            $state.go("oa.carDetail",{id: id});
        }


        vm.add = function () {
            oaCarService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaCarService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("OaCarDetailController", function ($state,$stateParams ,$scope, oaCarService) {
        var vm = this;
        var id = $stateParams.id;

        vm.loadData = function () {
            oaCarService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.basicInit(vm.item.businessKey);
                }
                // $("#buyDate").datepicker('setDate', vm.item.buyDate);
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            oaCarService.update(vm.item).then(function (value) {
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
                    oaCarService.update(vm.item).then(function (value) {
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

        vm.loadData();
        return vm;


    })

    .controller("OaCarApplyController", function ($state, $scope, oaCarApplyService,oaCarService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carId:vm.params.carId};
            oaCarApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            oaCarService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.oaCars = value.data.data.list;
                }
            })
        };
        vm.show = function (id) {
            $state.go("oa.carApplyDetail",{id: id});
        }


        vm.add = function () {
            oaCarApplyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaCarApplyService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })

    .controller("OaCarApplyDetailController", function ($state,$stateParams ,$scope, oaCarApplyService,oaCarService) {
        var vm = this;
        var id = $stateParams.id;

        vm.loadData = function (loadProcess) {
            oaCarApplyService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        vm.listAllCar = function(){
            oaCarService.listAllCar().then(function (value) {
                if(value.data.ret){
                    vm.listAll = value.data.data;
                    singleCheckBox(".cb_car","data-name");
                    $("#carModel").modal("show");
                }
            })
        };

        vm.selectCar = function(){
            $(".cb_car:checked").each(function () {
                var value = $.parseJSON($(this).attr("data-name"));
                vm.item.carName = value.carNo;
                vm.item.carId = value.id;
            });
            $("#carModel").modal("hide");

        };


        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            oaCarApplyService.update(vm.item).then(function (value) {
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
                    oaCarApplyService.update(vm.item).then(function (value) {
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


    })


    .controller("OaHardwareController", function ($state, $scope, oaHardwareService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,q:vm.pageInfo.equipmentName};
            oaHardwareService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        vm.show = function (id) {
            $state.go("oa.hardwareDetail",{id: id});
        }


        vm.add = function () {
            oaHardwareService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaHardwareService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }
        $("#uploadModelFile").fileupload({
            dataType: 'text',
            url:'/oa/hardware/receive.do?userLogin='+user.userLogin,
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
                } else {
                    toastr.error(result.msg);
                }
            }
        });

        vm.loadPagedData();

        return vm;

    })

    .controller("OaHardwareDetailController", function ($state,$stateParams ,$scope, oaHardwareService) {
        var vm = this;
        var id = $stateParams.id;

        vm.loadData = function () {
            oaHardwareService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.basicInit(vm.item.businessKey);
                }
                // $("#buyDate").datepicker('setDate', vm.item.buyDate);
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            oaHardwareService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.loadData();
        return vm;


    })

    .controller("OaSoftwareController", function ($state, $scope, oaSoftwareService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,q:vm.pageInfo.softwareName};
            oaSoftwareService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        vm.show = function (id) {
            $state.go("oa.softwareDetail",{id: id});
        }


        vm.add = function () {
            oaSoftwareService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaSoftwareService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        $("#uploadModelFile").fileupload({
            dataType: 'text',
            url:'/oa/software/receive.do?userLogin='+user.userLogin,
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
                } else {
                    toastr.error(result.msg);
                }
            }
        });

        vm.loadPagedData();

        return vm;

    })

    .controller("OaSoftwareDetailController", function ($state,$stateParams ,$scope, oaSoftwareService) {
        var vm = this;
        var id = $stateParams.id;

        vm.loadData = function () {
            oaSoftwareService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.basicInit(vm.item.businessKey);
                }
                // $("#buyDate").datepicker('setDate', vm.item.buyDate);
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            oaSoftwareService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.loadData();
        return vm;


    })
    .controller("OaLinkController", function ($stateParams,$rootScope,$scope,oaLinkService,sysRoleService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var uiSref="oa.link";

        vm.loadPagedData = function () {
            vm.edit=false;
            sysRoleService.getAclInfoByUserLogin(user.userLogin,uiSref).then(function (value) {
                vm.rightData=value.data.data;
                if(value.data.ret)
                {var aclData=value.data.data;
                    if(aclData.selectOpts.indexOf('增加')>-1){
                        vm.edit=true;
                    }
                }
            });
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,q:vm.pageInfo.softwareName};
            oaLinkService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };
        function getDate(value){
            var date = new Date(parseInt(value));
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            m = m < 10 ? ('0' + m) : m;
            var d = date.getDate();
            d = d < 10 ? ('0' + d) : d;
            return y+'-'+m+'-'+d;
        }
        vm.add=function () {
            vm.currentLink={"linkUrl":"","linkName":"新增链接","linkLogo":"","creator":user.userLogin,"creatorName":user.userName,"shownDate":getDate(new Date().getTime())};
            vm.logoAttach={};
            $("#detailModel").modal("show");
        };
        vm.openLink=function(item){
            window.open(item.linkUrl,"_blank");
        }
        vm.showModal=function(item){
            vm.currentLink=item;
            vm.currentLink.shownDate= getDate(vm.currentLink.gmtModified);
            if(vm.currentLink.linkLogo!=""&&vm.currentLink.linkLogo!=undefined){
                oaLinkService.getLogoAttachById(vm.currentLink.linkLogo).then(function (data) {
                    var result=data.data;
                    if (result.ret) {
                        vm.logoAttach=result.data;
                        vm.currentLink.linkLogo=vm.logoAttach.id;
                        $("#fileName").val(vm.logoAttach.name);
                    } else {
                        toastr.error(result.msg);
                    }
                });
            }
            $("#detailModel").modal("show");
        }
        vm.updateItem=function () {
            oaLinkService.update(vm.currentLink).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadPagedData();
                    $("#detailModel").modal("hide");
                }
            })
        };
        vm.remove=function(id){
            oaLinkService.remove(id,user.userLogin).then(function (value) {
                toastr.success("删除成功!")
                vm.loadPagedData();
            });
        };
        vm.init=function(){
            vm.loadPagedData();
            $("#uploadModelFile").fileupload({
                dataType: 'text',
                url:'/oa/link/receive.json?userLogin='+user.userLogin,
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
                        vm.logoAttach=result;
                        vm.currentLink.linkLogo=result.data.id;
                        $("#fileName").val(vm.logoAttach.data.name);
                    } else {
                        toastr.error(result.msg);

                    }
                }
            });
        };

        vm.init();
        return vm;
    })


