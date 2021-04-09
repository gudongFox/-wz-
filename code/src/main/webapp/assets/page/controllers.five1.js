angular.module('controllers.five1', [])

    .controller("FiveEdFormController", function ($state, $stateParams, $scope,edProjectTreeService,commonFormDataService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;
        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                    vm.loadData();
                }
            });

            $("#searchKey").on('keypress', function () {
                if (event.keyCode == 13) {
                    vm.loadData();
                }
            })
        };

        vm.loadData = function () {
            commonFormDataService.listData(vm.treeData.referType, vm.treeData.foreignKey, "", user.enLogin).then(function (value) {
                vm.data = value.data.data;
            })
        }

        vm.newData = function () {
            bootbox.confirm("确定发起新的数据流程吗?", function (result) {
                if (result) {
                    commonFormDataService.getNewModel(vm.treeData.referType, vm.treeData.foreignKey, user.enLogin).then(function (value) {
                        vm.loadData();
                    })
                }
            });
        }

        vm.showDetail = function (id) {
            $state.go("five.detail.formDetail", {formDataId: id})
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

    .controller('FiveEdFormDetailController',function ($state,$stateParams,$scope,$rootScope,commonFormDataService,actTaskQueryService){
        var vm = this;
        var formDataId=$stateParams.formDataId;

        vm.init=function() {
            vm.loadData();
        }

        vm.loadData=function() {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                vm.data = value.data.data.data;
                vm.groupList = value.data.data.groupList;
                vm.template = value.data.data.template;
                if (vm.data.processInstanceId) {
                    $scope.loadNewProcessInstance(vm.data.processInstanceId);
                } else {
                    $scope.processInstance={};
                    $scope.getTplConfig(vm.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
            })
        }

        vm.save=function(){
            var data=$scope.getFormData(vm.data.id,vm.groupList);
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    toastr.success("保存成功!");
                    vm.loadData();
                }
            })
        }

        $scope.refresh=function(){
            vm.loadData();
        }

        vm.init();
        return vm;
    })

    .controller("FiveEdCoController", function ($state, $stateParams, $scope,commonEdArrangeUserService) {
        var vm = this;
        var stepId = $stateParams.stepId;
        var businessKey="co_"+stepId;

        vm.parentId=0;
        vm.init=function(){
            $scope.fileTplTitle='协同设计';
            $scope.portletClass="portlet box blue"
            $scope.tplConfig={showBusinessFile:true,businessKey:businessKey,showFileType:false,editable:false};
        }

        vm.buildCoDirData=function(){
            commonEdArrangeUserService.buildCoDirData(stepId,stepId).then(function (value) {
                if(value.data.ret){
                    toastr.success("重新生成成功,请自行检查!");
                }
            })
        }
        vm.init();
        return vm;
    })

    .controller("FiveEdDocController", function ($state, $stateParams, $scope,edProjectTreeService,fiveEdService) {

        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init=function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                }
            });

            vm.loadData();
        }


        vm.loadData=function(){
            fiveEdService.listInputReviewFile(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.files=value.data.data;
                }
            })
        }

        $scope.downloadFile=function(id){
            $scope.commonDownload("/common/attach/download/file/"+id);
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdTaskController", function ($state, $stateParams, $scope,commonFormDataService,  edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            commonFormDataService.listData(vm.treeData.referType, vm.treeData.foreignKey, "", user.enLogin).then(function (value) {
                vm.data = value.data.data;
            })
        }

        vm.showDetail = function (id) {
            $state.go("five.detail.taskDetail", {formDataId: id})
        }

        vm.newData = function () {
            bootbox.confirm("确定发起新的数据流程吗?", function (result) {
                if (result) {
                    commonFormDataService.getNewModel(vm.treeData.referType, vm.treeData.foreignKey, user.enLogin).then(function (value) {
                        vm.loadData();
                    })
                }
            });
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

    .controller("FiveEdTaskDetailController", function ($rootScope,$state, $stateParams, $scope, commonFormDataService,commonEdArrangeUserService) {
        var formDataId = $stateParams.formDataId;

        $scope.init=function() {
            $scope.loadData();

            $("#uploadUserData").fileupload({
                dataType: 'json',
                url:"/common/edArrangeUser/upload.json",
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
                    }
                    data.formData = {enLogin: user.enLogin,businessKey:$scope.data.businessKey,buildBusinessKey:$scope.data.referId};
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
                            $scope.loadUser();
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });

        }

        $scope.exportUser=function(){
            $scope.commonDownload('/common/edArrangeUser/download.json',{businessKey:$scope.data.businessKey,buildBusinessKey:$scope.data.referId});
        }

        $scope.loadData=function() {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                $scope.data = value.data.data.data;
                $scope.groupList = value.data.data.groupList;
                $scope.template = value.data.data.template;
                if ($scope.data.processInstanceId) {
                    $scope.loadNewProcessInstance($scope.data.processInstanceId);
                } else {
                    $scope.processInstance={};
                    $scope.processInstanceId=undefined;
                    $scope.getTplConfig($scope.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
                $scope.loadUser();
            })
        }

        $scope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    toastr.success("保存成功!");
                    $scope.loadData();
                }
            })
        }





        $scope.loadUser=function() {
            commonEdArrangeUserService.listData($scope.data.businessKey, $scope.data.referId).then(function (value) {
                $scope.users = value.data.data.list;
                $scope.buildNameList=value.data.data.buildNameList;
                $scope.majorNameList=value.data.data.majorNameList;
            })
        }


        $scope.showUser=function(index,property){
            if(index>=2&&$scope.processInstance.sendAble){
                jQuery.showJsTreeSelectUserModal({title:'选择'+property.config.roleName,multiple:property.config.multiple,selects:property.code,
                    qualify:property.config.roleCode
                },function (selects) {
                    var enLoginList=[];
                    for(var i=0;i<selects.length;i++){
                        enLoginList.push(selects[i].id);
                    }
                    commonEdArrangeUserService.setUser(property.config.arrangeId,property.config.roleName,property.config.roleCode,enLoginList.join(',')).then(function (value) {
                        $scope.loadUser();
                        toastr.success("保存成功");
                    });
                })
            }
        }


        $scope.refresh=function(){
            $scope.loadData();
        }

        $scope.init();
    })

    .controller("FiveEdArrangeController", function ($state, $stateParams, $scope,commonFormDataService,commonEdArrangeUserService,edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            commonFormDataService.listData(vm.treeData.referType, vm.treeData.foreignKey, "", user.enLogin).then(function (value) {
                vm.data = value.data.data;
            });

            commonEdArrangeUserService.getDefaultArrangeBusinessKey(vm.treeData.foreignKey).then(function (value) {
                vm.businessKey = value.data.data;

            });
        }

        vm.showDetail = function (id) {
            $state.go("five.detail.arrangeDetail", {formDataId: id})
        }

        vm.newData = function () {
            bootbox.confirm("确定发起新的数据流程吗?", function (result) {
                if (result) {
                    commonFormDataService.getNewModel(vm.treeData.referType, vm.treeData.foreignKey, user.enLogin).then(function (value) {
                        vm.loadData();
                    })
                }
            });
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

    .controller("FiveEdArrangeDetailController", function ($rootScope,$state, $stateParams, $scope, commonFormDataService,commonEdArrangeUserService,commonEdArrangePlanService,fiveEdService) {
        var formDataId = $stateParams.formDataId;

        $scope.init=function() {
            $scope.loadData();

            $scope.initFileUpload("uploadUserData",function (e,data){
                var file = data.files[0];
                if (file.size > 20 * 1024 * 1024) {
                    toastr.error('文件大小超过最大限制20MB!');
                    return false;
                }
                if(file.name.indexOf(".xls")!==file.name.length-4){
                    toastr.error("只可以上传xls文件!");
                    return false;
                }
                data.formData = {enLogin: user.enLogin,businessKey:$scope.data.businessKey,buildBusinessKey:$scope.data.referId};
                return true;
            },function (){
                $scope.loadUser();
            },{dropZone:'div_user'});
        }

        $scope.exportUser=function(){
            $scope.commonDownload('/common/edArrangeUser/download.json',{businessKey:$scope.data.businessKey,buildBusinessKey:$scope.data.referId});
        }

        $scope.loadData=function() {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                $scope.data = value.data.data.data;
                $scope.groupList = value.data.data.groupList;
                $scope.template = value.data.data.template;
                if ($scope.data.processInstanceId) {
                    $scope.loadNewProcessInstance($scope.data.processInstanceId);
                } else {
                    $scope.processInstance = {};
                    $scope.processInstanceId = undefined;
                    $scope.getTplConfig($scope.data.businessKey, user.enLogin);
                }

                $scope.initWebControl();

                $scope.loadUser();
                $scope.loadPlan();
            })
        }

        $scope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    toastr.success("保存成功!");
                    $scope.loadData();
                }
            })
        }

        $scope.initArrangeUserFromLatestEdTask=function(){
            bootbox.confirm("确定要从最新得设计与开发任务书中同步人员安排吗?", function (result) {
                if (result) {
                    fiveEdService.initArrangeUserFromLatestEdTask(formDataId).then(function (value) {
                        if(value.data.ret){
                            toastr.success("同步数据成功");
                            $scope.loadData();
                        }
                    })
                }
            });
        }


        $scope.loadUser=function() {
            commonEdArrangeUserService.listData($scope.data.businessKey, $scope.data.referId).then(function (value) {
                $scope.users = value.data.data.list;
                $scope.buildNameList=value.data.data.buildNameList;
                $scope.majorNameList=value.data.data.majorNameList;
            })
        }



        $scope.showUser=function(index,property){
            if(index>=2&&$scope.processInstance.firstTask){
                jQuery.showJsTreeSelectUserModal({title:'选择'+property.config.roleName,
                    multiple:property.config.multiple,
                    selects:property.code,qualify:property.config.roleCode},function (selects) {
                    var enLoginList=[];
                    for(var i=0;i<selects.length;i++){
                        enLoginList.push(selects[i].id);
                    }
                    commonEdArrangeUserService.setUser(property.config.arrangeId,property.config.roleName,property.config.roleCode,enLoginList.join(',')).then(function (value) {
                        $scope.loadUser();
                        toastr.success("保存成功");
                    });
                })
            }
        }


        $scope.loadPlan=function() {
            commonEdArrangePlanService.listData($scope.data.businessKey, $scope.data.referId).then(function (value) {
                $scope.plans = value.data.data.list;
                $scope.planBuildNameList = value.data.data.buildNameList;

            })
        }

        $scope.sortPlan=function(id,up){
            commonEdArrangePlanService.setSeq(id,up).then(function (value) {
                if(value.data.ret){
                    $scope.loadPlan();
                }
            })
        }

        $scope.removePlan=function(id) {
            bootbox.confirm("确定删除该数据吗?", function (result) {
                if (result) {
                    commonEdArrangePlanService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            $scope.loadPlan();
                        }
                    })
                }
            });
        }

        $scope.savePlan=function(plan){
            commonEdArrangePlanService.save(plan.id,plan.planName,plan.planArea,plan.planDesc).then(function (value) {
                if(value.data.ret){
                    toastr.success("保存成功");
                }
            })
        }

        $scope.newPlan=function(){
            var buildName="";
            $(".tab-pane.active").each(function(){
                if($(this).attr("id").indexOf('tb_plan_build_')>=0){
                    buildName=$(this).attr("id").replace("tb_plan_build_","");
                }
            })
            commonEdArrangePlanService.getNewModel($scope.data.businessKey,$scope.data.referId,buildName).then(function (value) {
                toastr.success("新增数据成功(排列在最后)");
                $scope.loadPlan();
            })
        }


        $scope.refresh=function(){
            $scope.loadData();
        }


        $scope.init();
    })

    .controller("FiveEdValidateController", function ($state, $stateParams, $scope,commonFormDataService,  edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            commonFormDataService.listData(vm.treeData.referType, vm.treeData.foreignKey, "", user.enLogin).then(function (value) {
                vm.data = value.data.data;
            })
        }

        vm.showDetail = function (id) {
            $state.go("five.detail.formDetail", {formDataId: id})
        }

        vm.newData = function () {
            bootbox.confirm("确定发起新的数据流程吗?", function (result) {
                if (result) {
                    commonFormDataService.getNewModel(vm.treeData.referType, vm.treeData.foreignKey, user.enLogin).then(function (value) {
                        vm.loadData();
                    })
                }
            });
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


    .controller("BakFiveEdTaskDetailController", function ($rootScope,$state, $stateParams, $scope, fiveEdTaskService,fiveEdTaskUserService,hrEmployeeService,hrDeptService,commonCodeService) {
        var vm = this;
        var taskId = $stateParams.taskId;
        vm.qEmployee="";
        vm.qEmployeeDeptName="";
        vm.qEmployeeMajor="";


        vm.loadData = function (loadProcess) {

            hrEmployeeService.selectAll().then(function(value) {
                vm.employees=value.data.data;
            });

            fiveEdTaskService.getModelById(taskId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.qEmployeeDeptName=vm.item.deptName;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })

            vm.loadUser();

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdTaskService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadUser();
                }
            })

        };

        vm.loadUser = function () {
            fiveEdTaskUserService.listDataByTaskId(taskId).then(function (value) {
                if (value.data.ret) {
                    vm.users = value.data.data;
                }
            })
        }

        vm.showDeptModal=function(id) {
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
                        state: {opened: item.parentId === 0, disabled: false, selected: false}
                    };
                    if (id == item.id) {
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
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            if (dept.id!=vm.item.deptId){
                vm.item.deptChargeMenName='';
                vm.item.deptChargeMen='';
            }
            vm.item.deptId = dept.id;
            vm.item.deptName = dept.text;
            $("#deptSelectModal").modal("hide");
        }


        vm.showSelectMajorModal=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"设计专业").then(function (value) {
                vm.majorNames=value.data.data;
            })
            $("#selectMajorListModal").modal("show");
        }

        /*保存所选专业 */
        vm.saveMajorList=function() {
            var majors = [];
            $(".cb_major:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            vm.item.majorName = majors.join(',');
            $("#selectMajorListModal").modal("hide");
            vm.save();

        }

        /*人员列表*/
        vm.showSelectEmployeeModal=function(){
            if($rootScope.processInstance.myRunningTaskName.indexOf("配合部门负责人")==-1){
                if (vm.optUserType!='配合部门负责人'){
                    return;
                }
            }
            vm.bindSelectUser(vm.optUser);

            if (vm.item.deptId==75){
                vm.qEmployeeDeptName="一院";
            }else if (vm.item.deptId==74){
                vm.qEmployeeDeptName="二院";
            }else if (vm.item.deptId==76){
                vm.qEmployeeDeptName="环能";
            }else {
                vm.qEmployeeDeptName=vm.item.deptName;
            }
            if (vm.optUserType=='配合部门负责人'){
                vm.qEmployeeMajor="";
            }else {
                vm.qEmployeeMajor=vm.currentUser.majorName;
            }
            $("#selectEmployeeModal").modal("show");
        }

        vm.bindSelectUser=function(optUser){
            vm.selectedUserNames_="";
            vm.selectedUserLogins_="";
            hrEmployeeService.listDataByUserLoginList(optUser).then(function (value) {
                vm.userList=value.data.data;
                var userNameList=[];
                var userLoginList=[];
                for(var i=0;i<vm.userList.length;i++){
                    var user=vm.userList[i];
                    userLoginList.push(user.userLogin);
                    userNameList.push(user.userName);
                }
                vm.selectedUserNames_=','+userNameList.join(",")+',';
                vm.selectedUserLogins_=','+userLoginList.join(",")+',';
            })
        }

        vm.removeSelectedUser=function(userLogin){
            var users=vm.selectedUserLogins_.split(",");
            var userLoginList1 = ("," +users.join(",") + ",").replace(","+userLogin+",",",");
            vm.selectedUserLogins_=userLoginList1;
            hrEmployeeService.listDataByUserLoginList(vm.selectedUserLogins_).then(function (value) {
                vm.userList=value.data.data;
            })
        }

        vm.toggleSelectedUser=function(userLogin) {
            if (vm.selectedUserLogins_.indexOf(',' + userLogin + ',') >= 0) {
                vm.removeSelectedUser(userLogin);
            } else {
                if (vm.optUserType=='配合部门负责人'){//单选
                    vm.selectedUserLogins_= "," + userLogin + ",";
                }else {
                    var users = vm.selectedUserLogins_.split(",");
                    vm.selectedUserLogins_ = "," + users.join(",") + "," + userLogin + ",";
                }
                vm.bindSelectUser(vm.selectedUserLogins_);
            }
        }

        /*保存人员任命 多选*/
        vm.saveSelectEmployee=function(){
            if (vm.optUserType=='配合部门负责人'){

                vm.item.deptChargeMenName= vm.selectedUserNames_.replace(/^(\s|,)+|(\s|,)+$/g,'');
                vm.item.deptChargeMen= vm.selectedUserLogins_.replace(/^(\s|,)+|(\s|,)+$/g,'');
                //vm.save();
            }else {
                fiveEdTaskUserService.updateSelect(vm.currentUser.id,vm.optUserType,vm.selectedUserLogins_).then(function (value) {
                    if(value.data.ret){
                        toastr.success("保存成功!");
                        vm.loadUser();
                        vm.currentUser=value.data.data;
                    }
                });
            }
            $("#selectEmployeeModal").modal("hide");
        };

        //全选
        vm.selectAllFile=function(){
            $(".cb_major").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }
        //反选
        vm.toggleSelectFile=function(){
            $(".cb_major").each(function () {
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

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdTaskService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
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

        vm.print=function () {
            fiveEdArrangeService.getPrintData(arrangeId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("项目设计与校审人员表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + $("#print_area").html() + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.loadData(true);

        $scope.refresh = function () {
            vm.loadData(true);
        }

        return vm;
    })

    .controller("BakFiveEdArrangeController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdArrangeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdArrangeService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.arrangeDetail", {arrangeId: id});
        }

        vm.add = function () {
            fiveEdArrangeService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdArrangeService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.init();

        return vm;
    })

    .controller("BakFiveEdArrangeDetailController", function ($rootScope,$state, $stateParams, $scope, fiveEdArrangeService,fiveEdArrangeTimetableService,fiveEdArrangeUserService,hrEmployeeService,fiveEdArrangePlanService) {
        var vm = this;
        var arrangeId = $stateParams.arrangeId;
        vm.qEmployee="";
        vm.qEmployeeDeptName="";
        vm.qEmployeeMajor="";

        vm.loadData = function (loadProcess) {
            fiveEdArrangePlanService.listMajorCode(arrangeId).then(function (value) {
                vm.planMajorNames=value.data.data;
            });
            fiveEdArrangeService.getModelById(arrangeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.qEmployeeDeptName=vm.item.deptName;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#planDiscussTime").datepicker('setDate', vm.item.planDiscussTime);
                    $("#designReviewTime").datepicker('setDate', vm.item.designReviewTime);
                    $("#submitTotalDesignerTime").datepicker('setDate', vm.item.submitTotalDesignerTime);
                    $("#acceptTime").datepicker('setDate', vm.item.acceptTime);
                    $("#sendPictureTime").datepicker('setDate', vm.item.sendPictureTime);
                }
            })
            vm.loadUser();
            vm.loadTimetable();
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdArrangeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        vm.loadUser = function () {
            fiveEdArrangeUserService.listDataByDesignPlanId(arrangeId).then(function (value) {
                if (value.data.ret) {
                    vm.users = value.data.data;
                }
            })
        }

        vm.removeUser = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveEdArrangeUserService.remove(id).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadUser();
                            vm.loadData(false);
                        }
                    })
                }
            });

        }
        /*设计内容（专业）  多选*/
        vm.showSelectMajorListModal=function(){
            fiveEdArrangeUserService.listMajorCode(arrangeId).then(function (value) {
                vm.majorNames=value.data.data;
            })
            $("#selectMajorListModal").modal("show");
        }

        vm.selectAllFile=function(){
            $(".cb_major").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_major").each(function () {
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

        /*保存所选专业 */
        vm.saveMajorList=function() {
            var majors = [];
            $(".cb_major:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            $("#selectMajorListModal").modal("hide");
            fiveEdArrangeUserService.updateMajorList(arrangeId, majors.join(',')).then(function (value) {
                if (value.data.ret) {
                    vm.item.designMajorList=majors.join(',');
                    toastr.success("保存成功!");
                    vm.loadData();
                }
            });
        }

        /*人员列表*/
        vm.showSelectEmployeeModal=function(){
            if(!$rootScope.processInstance.firstTask){
                return;
            }
            fiveEdArrangeService.getAllCanAttendUser(arrangeId,user.deptId).then(function(value) {
                vm.employees=value.data.data;

            });
            vm.bindSelectUser(vm.optUser);

            /*   if (user.deptName.indexOf("一院")>=0){
                   vm.qEmployeeDeptName="一院";
               }else if (user.deptName.indexOf("二院")>=0){
                   vm.qEmployeeDeptName="二院";
               }else if (user.deptName.indexOf("环能")>=0){
                   vm.qEmployeeDeptName="环能";
               }else {
                   vm.qEmployeeDeptName=user.deptName;
               }*/
            if (vm.currentUser.majorName.indexOf("总设计师")>=0){
                vm.qEmployeeMajor="";
                vm.qChiefDesigner="true";
            }else {
                vm.qEmployeeMajor=vm.currentUser.majorName;
                vm.qChiefDesigner="true"+"false";
            }
            $("#selectEmployeeModal").modal("show");
        }

        vm.bindSelectUser=function(optUser){
            vm.selectedUserNames_="";
            vm.selectedUserLogins_="";
            hrEmployeeService.listDataByUserLoginList(optUser).then(function (value) {
                vm.userList=value.data.data;
                var userNameList=[];
                var userLoginList=[];
                for(var i=0;i<vm.userList.length;i++){
                    var user=vm.userList[i];
                    userLoginList.push(user.userLogin);
                    userNameList.push(user.userName);
                }
                vm.selectedUserNames_=','+userNameList.join(",")+',';
                vm.selectedUserLogins_=','+userLoginList.join(",")+',';
            })
        }

        vm.toggleSelectedUser=function(userLogin) {
            if (vm.selectedUserLogins_.indexOf(',' + userLogin + ',') >= 0) {
                vm.removeSelectedUser(userLogin);
            } else {
                var users = vm.selectedUserLogins_.split(",");
                vm.selectedUserLogins_ = "," + users.join(",") + "," + userLogin + ",";
                vm.bindSelectUser(vm.selectedUserLogins_);
            }
        }

        vm.removeSelectedUser=function(userLogin){
            var users=vm.selectedUserLogins_.split(",");
            var userLoginList1 = ("," +users.join(",") + ",").replace(","+userLogin+",",",");
            vm.selectedUserLogins_=userLoginList1;
            hrEmployeeService.listDataByUserLoginList(vm.selectedUserLogins_).then(function (value) {
                vm.userList=value.data.data;
            })
        }
        /*保存人员任命 多选*/
        vm.saveSelectEmployee=function(){
            fiveEdArrangeUserService.updateSelect(vm.currentUser.id,vm.optUserType,vm.selectedUserLogins_).then(function (value) {
                if(value.data.ret){
                    $("#selectEmployeeModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadUser();
                    vm.currentUser=value.data.data;
                }
            });
        };

        /*快速配置 list*/
        vm.showHistory=function(){

            fiveEdArrangeUserService.listHistoryConfig(vm.currentUser.id).then(function (value) {
                vm.historys=value.data.data;
                singleCheckBox(".cb_history","data-index");
            })
            $("#selectHistoryModal").modal("show");
        };
        /*快速配置 保存*/
        vm.saveSelectHistory=function(){
            if( $(".cb_history:checked").length==0){
                toastr.warning("请先选择合适的历史数据");
                return;
            }
            var arrangeUserId= $(".cb_history:checked").eq(0).attr("data-index");
            fiveEdArrangeUserService.updateUserByHistory(vm.currentUser.id,arrangeUserId).then(function (value) {
                if(value.data.ret){
                    toastr.success("保存成功!");
                    $("#selectHistoryModal").modal("hide");
                    vm.loadUser();
                }
            });
        }
        /*详情配置 */
        vm.showArrangeUserModal=function(){
            $("#arrangeUserModal").modal("show");
        }


        vm.loadTimetable=function(){
            fiveEdArrangeTimetableService.listDataByArrangeId(arrangeId).then(function (value) {
                if (value.data.ret) {
                    vm.newmajorNames=value.data.data.majors;
                    vm.listTimetable=value.data.data.listTimetable;
                }
            })
        }

        vm.showTimetable=function(sendMajor){

            fiveEdArrangeTimetableService.listDataByMajor(arrangeId,sendMajor).then(function (value) {
                if (value.data.ret) {
                    vm.timetableList=value.data.data;
                    $("#timetableMajorListModal").modal("show");
                    setTimeout(function () {
                        $scope.basicInit();
                    },100);
                }
            })


        }
        vm.saveTimetable=function(timetableList){
            for (var i=0;i<vm.timetableList.length;i++){
                fiveEdArrangeTimetableService.saveTimetable(vm.timetableList[i]).then(function (value) {

                })
            }
            $("#timetableMajorListModal").modal("hide");
            vm.loadTimetable();
        }

        vm.showPlanMajorList=function(){
            $("#planMajorListModal").modal("show");
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdArrangeService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
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
        vm.print=function () {
            fiveEdArrangeService.getPrintData(arrangeId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("项目设计与校审人员表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + $("#print_area").html() + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.loadData(true);
        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })

    .controller("FiveEdHouseReferController", function ($state, $stateParams, $scope, fiveEdHouseReferService, edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            fiveEdHouseReferService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            fiveEdHouseReferService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    fiveEdHouseReferService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("five.detail.houseReferDetail",{houseReferId:id});
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdHouseReferDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdHouseReferService,fiveEdArrangeUserService) {
        var vm = this;
        var houseReferId = $stateParams.houseReferId;
        vm.houseReferId = houseReferId;


        vm.init = function () {
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveEdHouseReferService.getModelById(houseReferId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };
        vm.selectAllFile=function(){
            $(".cb_destMajor").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_destMajor").each(function () {
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

        vm.showSelectDestMajor=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.users=value.data.data;
                $("#selectDestMajorModal").modal("show");
            })
        };

        vm.selectDestMajor=function(){
            var destMajors = [];
            $(".cb_destMajor:checked").each(function () {
                destMajors.push($(this).attr("data-name"));
            });
            vm.item.destMajor = destMajors.join(',');
            $("#selectDestMajorModal").modal("hide");
        }

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdHouseReferService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdHouseReferService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
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

        vm.init();

        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })

    .controller("FiveEdReviewApplyController", function ($state, $stateParams, $scope, fiveEdReviewApplyService, edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            fiveEdReviewApplyService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            fiveEdReviewApplyService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    fiveEdReviewApplyService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("five.detail.reviewApplyDetail",{reviewApplyId:id});
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdReviewApplyDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdReviewApplyService,fiveEdArrangeUserService) {
        var vm = this;
        var reviewApplyId = $stateParams.reviewApplyId;

        vm.init = function () {
            vm.loadData(true);

        };

        vm.loadData = function (loadProcess) {
            fiveEdReviewApplyService.getModelById(reviewApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                        $("#planReviewTime").datepicker('setDate', vm.item.planReviewTime);
                        $("#applyTime").datepicker('setDate', vm.item.applyTime);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdReviewApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }
        vm.showSelectReferSpecialty=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.users=value.data.data;
                $("#selectReferSpecialtyModal").modal("show");
            })
        };
        vm.selectAllFile=function(){
            $(".cb_referSpecialty").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_referSpecialty").each(function () {
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

        vm.selectReferSpecialty=function(){
            var referSpecialtys = [];
            $(".cb_referSpecialty:checked").each(function () {
                referSpecialtys.push($(this).attr("data-name"));
            });
            vm.item.referSpecialty = referSpecialtys.join(',');
            $("#selectReferSpecialtyModal").modal("hide");
        }

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdReviewApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
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

        $scope.refresh = function () {
            vm.loadData(true);
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdReviewSpecialController", function ($state, $stateParams, $scope, fiveEdReviewSpecialService, edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if(value.data.ret){
                    vm.treeNode=value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData=function(){
            fiveEdReviewSpecialService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if(value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.add=function(){
            fiveEdReviewSpecialService.getNewModel(vm.treeNode.foreignKey,user.userLogin).then(function (value) {
                if(value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id){
            bootbox.confirm("确认要删除该条数据吗",function (result) {
                if(result){
                    fiveEdReviewSpecialService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show=function(id){
            $state.go("five.detail.reviewSpecialDetail",{reviewSpecialId:id});
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdReviewSpecialDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdReviewSpecialService,edFileService) {
        var vm = this;
        var reviewSpecialId = $stateParams.reviewSpecialId;

        vm.init = function () {
            vm.loadData(true);

        };

        vm.loadData = function (loadProcess) {
            fiveEdReviewSpecialService.getModelById(reviewSpecialId).then(function (value) {
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
            fiveEdReviewSpecialService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }


        //发送流程验证
        $scope.showSendTask = function (send) {
            edFileService.fileCheckByBusinessKey(vm.item.businessKey).then(function (value) {
                if (value.data.ret){
                    var map=value.data.data;
                    if (!map.isExist){
                        toastr.warning("请上传-"+map.lackType+"-文件")
                    }else {
                        if ($("#detail_form").validate().form()) {
                            vm.item.operateUserLogin = user.userLogin;
                            fiveEdReviewSpecialService.update(vm.item).then(function (value) {
                                if (value.data.ret) {
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
                }
            })

        }

        vm.showUserModel = function () {
            $scope.showSelectEmployeeModal_({title:"主持人选择", userLoginList: vm.item.hostMen,multiple:true});
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            vm.item.hostMen = $scope.selectedUserLogins_;
            vm.item.hostMenName =  $scope.selectedUserNames_;
        };
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.init();


        return vm;
    })

    .controller("BakFiveEdHouseValidateController", function ($state, $stateParams, $scope, edProjectTreeService, fiveEdHouseValidateService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdHouseValidateService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            });
            fiveEdHouseValidateService.getValidateInfo(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.validateInfo=value.data.data;
                }
            });
        }

        vm.add = function () {
            fiveEdHouseValidateService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdHouseValidateService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.detail.houseValidateDetail", {validateId: id});
        }

        vm.init();

        return vm;
    })

    .controller("BakFiveEdHouseValidateDetailController", function ($rootScope,$state, $stateParams, $scope, actService,commonFileService, edFileService, fiveEdHouseValidateService, edHouseValidateMarkService,fiveEdArrangeUserService) {
        var vm = this;
        var validateId = $stateParams.validateId;
        $scope.showSelectCo =true;
        vm.init = function () {
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveEdHouseValidateService.getModelById(validateId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        fiveEdHouseValidateService.listEdValidateUserByLogin(vm.item.stepId,user.userLogin).then(function(value){
                            vm.arrangeUsers=value.data.data;
                        });
                    }
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdHouseValidateService.update(vm.item).then(function (value) {
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
                fiveEdHouseValidateService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
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

        vm.showArrangeUserModal=function(){
            singleCheckBox(".cb_arrange","data-name");
            $("#arrangeUserModal").modal("show");
        }

        vm.selectAllFile=function(){
            $(".cb_arrange").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_arrange").each(function () {
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

        vm.selectAllCoFile=function(){
            $(".cb_file").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("coFile") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectCoFile=function(){
            $(".cb_file").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("coFile") == 0) {
                    var checked=$(this).attr("checked");
                    if(checked) {
                        $(this).removeAttr("checked");
                    }else{
                        $(this).attr("checked", true);
                    }
                }
            })
        }

        vm.saveSelectMajor=function(){
            if($(".cb_arrange:checked").length>0) {
                var arrange = $.parseJSON($(".cb_arrange:checked").first().attr("data-name"));
                fiveEdHouseValidateService.resetMajorName(validateId,arrange.id).then(function (value) {
                    if(value.data.ret){
                        $("#arrangeUserModal").modal("hide");
                        toastr.success("保存成功");
                        vm.loadData();

                    }
                })
            }
        }

        vm.showCountersignModal=function(){
            fiveEdArrangeUserService.getAllDefaultUser(vm.item.stepId).then(function (value) {
                if (value.data.ret){
                    vm.list=value.data.data;
                }
            })
            $("#countersignModel").modal("show");
        }

        vm.saveCountersign = function () {
            var userLogins = [];
            var userNames = [];
            $(".cb_map:checked").each(function () {
                var map = $.parseJSON( $(this).attr("data-name"));
                var u=userLogins.join(",");

                if (u.indexOf(map.userLogin)==-1){
                    userLogins.push(map.userLogin);
                }
                var name=userNames.join(",");
                if (name.indexOf(map.userName)==-1){
                    userNames.push(map.userName);
                }
            });

            vm.item.otherMen = userLogins.join(',');
            vm.item.otherMenName = userNames.join(',');

            $("#countersignModel").modal("hide");
        }


        vm.showSelectCoModel=function(){
            if(vm.item.majorName ==''){
                toastr.warn("请先选择评审专业");
                return;
            }
            fiveEdHouseValidateService.getCoPublishFile(vm.item.majorName,vm.item.stepId,user.userLogin).then(function (value) {
                if (value.data.ret){
                    vm.commonCoFiles=value.data.data;
                    commonFileService.listData(vm.item.businessKey,0).then(function (value) {
                        vm.files = value.data.data;

                        vm.fileSourceIds = "";
                        for(var i=0;i<vm.files.length;i++){
                            vm.fileSourceIds = vm.fileSourceIds+","+vm.files[i].sourceId+",";
                        }
                        $("#selectCoModel").modal("show");
                    });
                }
            })
        }

        vm.saveSelectCo = function () {
            var commonFiles = [];
            $(".cb_file:checked").each(function () {
                var file = $.parseJSON( $(this).attr("data-name"));
                commonFiles.push(file);
            });
            commonFileService.transferCoToEd(commonFiles,vm.item.businessKey,user.userLogin).then(function () {
                vm.loadData(true);
            });

            $("#selectCoModel").modal("hide");
        }


        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.init();

        return vm;
    })

    .controller("FiveEdProductController", function ($state, $stateParams, $scope, edProjectTreeService, fiveEdProductService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdProductService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.add = function () {
            fiveEdProductService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdProductService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.detail.productDetail", {productId: id});
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdProductDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdProductService,fiveEdArrangeUserService) {
        var vm = this;
        var productId = $stateParams.productId;

        vm.loadData = function (loadProcess) {
            fiveEdProductService.getModelById(productId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value1) {
                        vm.arrangeUsers=value1.data.data;
                    })
                    $("#productDate").datepicker('setDate', vm.item.productDate);
                }
            })

            vm.selectDetail();
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdProductService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }

        vm.selectDetail=function(){
            fiveEdProductService.listDetail(productId).then(function (value) {
                if (value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.addDetail=function(){
            fiveEdProductService.getNewDetail(productId).then(function (value) {
                if (value.data.ret){
                    vm.editDetail(value.data.data);
                }
            })
        }

        vm.editDetail=function(detailId){
            fiveEdProductService.getDetailById(detailId).then(function (value) {
                if (value.data.ret){
                    vm.productDetail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.removeDetail=function(detailId){
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveEdProductService.removeDetail(detailId,user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.selectDetail();
                        }
                    })
                }
            })

        }

        vm.updateDetail=function(){
            fiveEdProductService.updateDetail(vm.productDetail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.selectDetail();
                    $("#detailModel").modal("hide");
                }
            })
        }

        vm.showSelectNeedChangeMajorModel=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.arrangeUsers=value.data.data;
            })
            $("#selectNeedChangeMajorModal").modal("show");

        }
        vm.selectNeedChangeMajor=function(){
            var majors = [];
            $(".cb_needChangeMajor:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            vm.item.majorName = majors.join(',');
            $("#selectNeedChangeMajorModal").modal("hide");
        }

        vm.selectStageName = function () {
            var stageNames = [];
            $(".cb_stage:checked").each(function () {
                stageNames.push($(this).attr("name"));
            });
            vm.item.stageNames = stageNames.join(',');
            $("#contractStageNameModel").modal("hide");
        }



        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdProductService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh ();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.loadData(true);

        $scope.refresh = function () {
            vm.loadData(true);
        }
        return vm;
    })

    .controller("FiveEdQualityCheckController", function ($state, $stateParams, $scope, edProjectTreeService, fiveEdQualityCheckService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdQualityCheckService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.add = function () {
            fiveEdQualityCheckService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdQualityCheckService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.detail.qualityCheckDetail", {qualityCheckId: id});
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdQualityCheckDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdQualityCheckService,fiveEdArrangeUserService) {
        var vm = this;
        var qualityCheckId = $stateParams.qualityCheckId;

        vm.loadData = function (loadProcess) {
            fiveEdQualityCheckService.getModelById(qualityCheckId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    vm.selectDetail();
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdQualityCheckService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }



        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdQualityCheckService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
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

        vm.selectDetail=function(){
            fiveEdQualityCheckService.listDetail(qualityCheckId).then(function (value) {
                if (value.data.ret){
                    vm.list=value.data.data;
                }
            })
        }

        vm.addDetail=function(){
            fiveEdQualityCheckService.getNewDetail(qualityCheckId).then(function (value) {
                if (value.data.ret){
                    vm.editDetail(value.data.data);
                }
            })
        }

        vm.editDetail=function(detailId){
            fiveEdQualityCheckService.getDetailById(detailId).then(function (value) {
                if (value.data.ret){
                    vm.qualityCheckDetail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.removeDetail=function(detailId){
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveEdQualityCheckService.removeDetail(detailId,user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.selectDetail();
                        }
                    })
                }
            })

        }

        vm.updateDetail=function(){
            fiveEdQualityCheckService.updateDetail(vm.qualityCheckDetail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.selectDetail();
                    $("#detailModel").modal("hide");
                }
            })
        }

        vm.showSelectNeedChangeMajorModel=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.arrangeUsers=value.data.data;
            })
            $("#selectNeedChangeMajorModal").modal("show");

        }
        vm.selectAllFile=function(){
            $(".cb_needChangeMajor").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_needChangeMajor").each(function () {
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
        vm.selectNeedChangeMajor=function(){
            var majors = [];
            $(".cb_needChangeMajor:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            vm.item.majorName = majors.join(',');
            $("#selectNeedChangeMajorModal").modal("hide");
        }

        vm.selectStageName = function () {
            var stageNames = [];
            $(".cb_stage:checked").each(function () {
                stageNames.push($(this).attr("name"));
            });
            vm.item.stageNames = stageNames.join(',');
            $("#contractStageNameModel").modal("hide");
        }

        vm.showUserModel = function () {
            $scope.showSelectEmployeeModal_({title:"质量部门负责人选择", userLoginList: vm.item.qualityDepartmentMen,multiple:true});
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            vm.item.qualityDepartmentMen = $scope.selectedUserLogins_;
            vm.item.qualityDepartmentMenName =  $scope.selectedUserNames_;
        };
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.loadData(true);
        return vm;
    })

    .controller("FiveEdStampController", function ($state, $stateParams, $scope, edProjectTreeService, fiveEdStampService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;


        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdStampService.listDataByStepId(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.add = function () {
            fiveEdStampService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdStampService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.detail.stampDetail", {stampId: id});
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdStampDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdStampService,fiveEdArrangeUserService) {
        var vm = this;
        var stampId = $stateParams.stampId;


        vm.loadData = function (loadProcess) {
            fiveEdStampService.getModelById(stampId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#useTime").datepicker('setDate', vm.item.useTime);

                }
            })
        };


        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdStampService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }

        vm.listPlotApply=function(){
            fiveEdStampService.listPlotApply(stampId).then(function (value) {
                if (value.data.ret){
                    vm.list=value.data.data;
                }
            })
            $("#plotApplyModel").modal("show");
        }

        vm.savePlotApply=function(){
            var ids=[];
            var majors=[];
            $(".cb_plot:checked").each(function () {
                var value=$.parseJSON($(this).attr("data-name"));
                ids.push(value.id);
                majors.push(value.majorName);
            });
            var realMajor= majors.join(',').split(',');
            var major=[];
            for (var i=0;i<realMajor.length;i++){
                if (major.indexOf(realMajor[i])==-1){
                    major.push(realMajor[i]);
                }
            }
            fiveEdStampService.saveEdFiles(ids.join(','),vm.item.businessKey).then(function (value) {
                if (value.data.ret){
                    vm.item.majors=major.join(',');

                    vm.save();
                    $("#plotApplyModel").modal("hide");
                    $scope.loadEdFiles(vm.item.businessKey);
                }
            })


        }


        vm.showAllMajor=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.edArrangeUsers=value.data.data;
                $("#arrangeUserModal").modal("show");
            })
        }
        vm.selectAllFile=function(){
            $(".cb_arrange").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_arrange").each(function () {
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
        vm.saveMajors=function(){
            var majors = [];
            $(".cb_arrange:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });

            vm.item.majors=majors.join(',');
            $("#arrangeUserModal").modal("hide");
        }

        /*用印明细 */
        vm.showStampModel=function(){
            fiveEdStampService.listStampCode().then(function (value) {
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


        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdStampService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh ();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.loadData(true);
        return vm;
    })

    .controller("FiveEdPlotApplyController", function ($state, $stateParams, $scope, edProjectTreeService, fiveEdPlotApplyService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;


        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdPlotApplyService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.add = function () {
            fiveEdPlotApplyService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdPlotApplyService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.detail.plotApplyDetail", {plotId: id});

        }
        vm.init();

        return vm;
    })

    .controller("FiveEdPlotApplyDetailController", function ($rootScope,$state, $stateParams,$scope,fiveEdPlotApplyService,fiveEdPlotApplyDetailService,fiveEdArrangeUserService) {
        var vm = this;
        var plotId = $stateParams.plotId;


        vm.loadData = function (loadProcess) {
            fiveEdPlotApplyService.getModelById(plotId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if(loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#plotTime").datepicker('setDate', vm.item.plotTime);

                }
            })
            vm.loadDetail();

        };
        vm.loadDetail=function(){
            fiveEdPlotApplyDetailService.listData(plotId).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        }

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveEdPlotApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        }

        vm.addDetail=function(){
            fiveEdPlotApplyDetailService.getNewModel(plotId).then(function (value) {
                if (value.data.ret){

                    vm.edit(value.data.data);
                }
            })
        }

        vm.edit=function(id){
            fiveEdPlotApplyDetailService.getModelById(id).then(function (value) {
                if (value.data.ret){
                    vm.detail=value.data.data;
                    $("#plotApplyModal").modal("show");
                }
            })
        }

        vm.saveDetail=function(){
            fiveEdPlotApplyDetailService.update(vm.detail).then(function (value) {
                if (value.data.ret){
                    toastr.success("保存成功!");
                    $("#plotApplyModal").modal("hide");
                }
            })
            vm.loadDetail();
        }
        vm.removeDetail=function(id){
            fiveEdPlotApplyDetailService.remove(id,user.userLogin).then(function (value) {
                if (value.data.ret){
                    toastr.success("删除成功!");
                }
            })
        }

        vm.showAllMajor=function(){
            fiveEdArrangeUserService.listDefaultArrangeUser(vm.item.stepId).then(function (value) {
                vm.edArrangeUsers=value.data.data;
                $("#arrangeUserModal").modal("show");
            })
        }
        vm.saveMajors=function(){
            var majors = [];
            $(".cb_arrange:checked").each(function () {
                majors.push($(this).attr("data-name"));
            });
            vm.item.majorName=majors.join(',');
            $("#arrangeUserModal").modal("hide");
        }

        vm.selectAllFile=function(){
            $(".cb_arrange").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_arrange").each(function () {
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


        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdPlotApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
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
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.loadData(true);
        return vm;
    })

    .controller("FiveEdQualityAnalysisController", function ($state, $stateParams, $scope,edProjectTreeService, fiveEdQualityAnalysisService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeNode = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdQualityAnalysisService.listDataByForeignKey(vm.treeNode.foreignKey).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })

        }

        vm.show = function (id) {
            $state.go("five.detail.qualityAnalysisDetail", {analysisId: id});
        }

        vm.add = function () {
            fiveEdQualityAnalysisService.getNewModel(vm.treeNode.foreignKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    fiveEdQualityAnalysisService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        }


        vm.init();

        return vm;
    })

    .controller("FiveEdQualityAnalysisDetailController", function ($rootScope,$state, $stateParams, $scope, fiveEdQualityAnalysisService) {
        var vm = this;
        var analysisId = $stateParams.analysisId;

        vm.loadData = function (loadProcess) {
            fiveEdQualityAnalysisService.getModelById(analysisId).then(function (value) {
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
            fiveEdQualityAnalysisService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                }
            })
        };

        //发送流程验证
        $scope.showSendTask = function (send) {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdQualityAnalysisService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh ();
                        });
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showUserModel = function () {
            $scope.showSelectEmployeeModal_({title:"质量部门负责人选择", userLoginList: vm.item.qualityDepartmentMen,multiple:true});
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            vm.item.qualityDepartmentMen = $scope.selectedUserLogins_;
            vm.item.qualityDepartmentMenName =  $scope.selectedUserNames_;
        };
        $scope.refresh = function () {
            vm.loadData(true);
        }
        vm.loadData(true);


        return vm;
    })


    //设计图纸资料交验
    .controller("FiveEdDesignDrawingCheckController", function ($state, $stateParams, $scope,fiveEdDesignDrawingCheckService,edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdDesignDrawingCheckService.listDataByStepId(vm.treeData.foreignKey, "", user.userLogin).then(function (value) {
                vm.list = value.data.data;
            })
        }

        vm.showDetail = function (id) {
            $state.go("five.detail.designDrawingCheckDetail", {checkId: id})
        }

        vm.add = function () {
            bootbox.confirm("确定发起新的数据流程吗?", function (result) {
                if (result) {
                    fiveEdDesignDrawingCheckService.getNewModel( vm.treeData.foreignKey, user.enLogin).then(function (value) {
                        vm.showDetail(value.data.data);
                    })
                }
            });
        }

        vm.remove = function (id) {
            bootbox.confirm("确定删除该数据吗?", function (result) {
                if (result) {
                    fiveEdDesignDrawingCheckService.remove(id, user.enLogin).then(function (value) {
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

    .controller("FiveEdDesignDrawingCheckDetailController", function ($rootScope,$state, $stateParams, $scope, fiveEdDesignDrawingCheckService) {
        var vm = this;
        var checkId = $stateParams.checkId;

        vm.init=function() {
            vm.loadData(true);
        }

        vm.loadData=function(loadProcess) {
            fiveEdDesignDrawingCheckService.getModelById(checkId).then(function (value) {
                if (value.data.ret){
                    vm.item=value.data.data;
                }
                if (loadProcess) {
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);
                }
                $("#checkTime").datepicker('setDate', vm.item.checkTime);
            })
        }

        vm.save=function(){
            vm.item.operateUserLogin = user.userLogin;
            fiveEdDesignDrawingCheckService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdDesignDrawingCheckService.update(vm.item).then(function (value) {
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
                toastr.warning("请准确填写数据!");
                return false;
            }

        };

        $scope.refresh=function(){
            vm.loadData(true);
        }


        vm.init();
    })


    //专业图纸验收清单
    .controller("FiveEdMajorDrawingCheckController", function ($state, $stateParams, $scope,fiveEdMajorDrawingCheckService,edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdMajorDrawingCheckService.listDataByStepId(vm.treeData.foreignKey, user.userLogin).then(function (value) {
                vm.list = value.data.data;
            })
        }

        vm.showDetail = function (id) {
            $state.go("five.detail.majorDrawingCheckDetail", {checkId: id})
        }

        vm.add = function () {
            bootbox.confirm("确定发起新的数据流程吗?", function (result) {
                if (result) {
                    fiveEdMajorDrawingCheckService.getNewModel( vm.treeData.foreignKey, user.enLogin).then(function (value) {
                        if ( value.data.ret){
                            vm.showDetail(value.data.data);
                        }
                    })
                }
            });
        }

        vm.remove = function (id) {
            bootbox.confirm("确定删除该数据吗?", function (result) {
                if (result) {
                    fiveEdMajorDrawingCheckService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData();
                        }
                    })
                }
            });
        }

        vm.downExcel=function(){


            fiveEdMajorDrawingCheckService.downTempleXls(vm.treeData.foreignKey).then(function (response) {
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

        vm.init();

        return vm;
    })

    .controller("FiveEdMajorDrawingCheckDetailController", function ($rootScope,$state, $stateParams, $scope, fiveEdMajorDrawingCheckService) {
        var vm = this;
        var checkId = $stateParams.checkId;

        vm.init=function() {
            vm.loadData(true);



        }

        vm.loadData=function(loadProcess) {
            fiveEdMajorDrawingCheckService.getModelById(checkId).then(function (value) {
                if (value.data.ret){
                    vm.item=value.data.data;
                }
                if (loadProcess) {
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);
                    //可选择子项
                    fiveEdMajorDrawingCheckService.getBuildList( vm.item.stepId).then(function (value) {
                        if (value.data.ret){
                            vm.buildNameList=value.data.data;
                        }
                    })
                    //可选择专业
                    fiveEdMajorDrawingCheckService.getMajorList(vm.item.stepId,user.userLogin).then(function (value) {
                        if (value.data.ret){
                            vm.majorList=value.data.data;
                        }
                    })
                }
                $("#checkTime").datepicker('setDate', vm.item.checkTime);
            })



            vm.loadDetail();
        }

        vm.loadDetail=function() {

            fiveEdMajorDrawingCheckService.listDetail(checkId).then(function (value) {
                if (value.data.ret){
                    vm.listDetail=value.data.data;
                }
            })
        }


        vm.save=function(){
            vm.item.operateUserLogin = user.userLogin;
            fiveEdMajorDrawingCheckService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveEdMajorDrawingCheckService.update(vm.item).then(function (value) {
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
                toastr.warning("请准确填写数据!");
                return false;
            }

        };

        vm.addDetail=function(){
            fiveEdMajorDrawingCheckService.getNewModelDetail(checkId).then(function(value){
                if (value.data.ret){
                   vm.showDetail(value.data.data);
                }
            })
        }

        vm.showDetail=function(id){
            fiveEdMajorDrawingCheckService.getModelDetailById(id).then(function(value){
                if (value.data.ret){
                    vm.detail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.removeDetail=function(id){
            bootbox.confirm("确定删除该数据吗?", function (result) {
                if (result) {
                    fiveEdMajorDrawingCheckService.removeDetail(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        }
        vm.saveDetail=function(){
            fiveEdMajorDrawingCheckService.updateDetail(vm.detail).then(function(value){
                if (value.data.ret){
                    vm.detail=value.data.data;
                    vm.loadDetail();
                    $("#detailModel").modal("hide");
                }
            })
        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择设计单位",type:"选部门", deptIdList: vm.detail.deptId ,
                multiple:false,deptIds:"1",parentDeptId:vm.item.deptId});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();

            vm.detail.deptName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = $scope.selectedOaDeptIds_;
        }

        vm.print=function (id) {
            fiveEdMajorDrawingCheckService.getPrintData(id).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("专业图纸验收清单");

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2,0,0,"A4");
                        lodop.SET_SHOW_MODE("HIDE_PBUTTIN_PREVIEW",1);//隐藏预览窗口的打印按钮
                        lodop.SET_SHOW_MODE("HIDE_SBUTTIN_PREVIEW",1);//隐藏预览窗口的打印设置按钮
                        lodop.SET_SHOW_MODE("PREVIEW_IN_BROWSE",1);//打印预览界面是否内嵌到网页内部
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $scope.refresh=function(){
            vm.loadData(true);
        }


        vm.init();
    })

    //设计文件图纸验收清单
    .controller("FiveEdDesignDrawingController", function ($state, $stateParams, $scope,fiveEdDesignDrawingService,edProjectTreeService) {
        var vm = this;
        var nodeId = $stateParams.nodeId;

        vm.init = function () {
            edProjectTreeService.getModelById(nodeId).then(function (value) {
                if (value.data.ret) {
                    vm.treeData = value.data.data;
                    vm.loadData();
                }
            });
        };

        vm.loadData = function () {
            fiveEdDesignDrawingService.listDataByStepId(vm.treeData.foreignKey, user.userLogin).then(function (value) {
                vm.list = value.data.data;
            })
        }

        vm.showDetail=function(id){
            $state.go("five.detail.designDrawingDetail", {drawingId: id})
        }

        vm.init();

        return vm;
    })

    .controller("FiveEdDesignDrawingDetailController", function ($rootScope,$state, $stateParams, $scope, fiveEdDesignDrawingService) {
        var vm = this;
        var drawingId = $stateParams.drawingId;

        vm.init=function() {
            vm.loadData(true);
        }

        vm.loadData=function(loadProcess) {
            fiveEdDesignDrawingService.getModelById(drawingId).then(function (value) {
                if (value.data.ret){
                    vm.item=value.data.data;
                }
                if (loadProcess) {
                    $scope.basicInit(vm.item.businessKey);
                }
                $("#checkTime").datepicker('setDate', vm.item.checkTime);
            })

            vm.loadDetail();
        }

        vm.loadDetail=function() {

            fiveEdDesignDrawingService.listDetail(drawingId).then(function (value) {
                if (value.data.ret){
                    vm.listDetail=value.data.data;
                }
            })
        }

        vm.showDetail=function(id){
            fiveEdDesignDrawingService.getModelDetailById(id).then(function(value){
                if (value.data.ret){
                    vm.detail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }
        vm.init();
    })
