angular.module('controllers.sys', [])


    .controller("SysRequestController", function ($state, $scope, sysRequestService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.pageInfoAdmin = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.pageInfoSafety = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.pageInfoAudit = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.pageInfoUser = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            if (user.userLogin=="audit"){//安全审计员
                vm.listSysAdminData();
                vm.listSafetyData();
                vm.listAuditData();
            } else if (user.userLogin=="safety"){//安全保密员
                vm.listUserData();
                vm.listAuditData();
            }else {
                vm.loadPagedData();
            }
        };

        vm.loadPagedData = function () {
            sysRequestService.listPagedRequest(vm.params.qName, vm.pageInfo.pageNum, vm.pageInfo.pageSize).then(function (value) {
                vm.pageInfo = value.data;
                setCacheParams(key,vm.params,vm.pageInfo);
            })
        };

        vm.listSysAdminData = function () {
            sysRequestService.listSysAdminData(vm.params.qName, vm.pageInfoAdmin.pageNum, vm.pageInfoAdmin.pageSize).then(function (value) {
                vm.pageInfoAdmin = value.data;
            })
        };

        vm.listSafetyData = function () {
            sysRequestService.listSafetyData(vm.params.qName, vm.pageInfoSafety.pageNum, vm.pageInfoSafety.pageSize).then(function (value) {
                vm.pageInfoSafety = value.data;
            })
        };

        vm.listAuditData = function () {
            sysRequestService.listAuditData(vm.params.qName, vm.pageInfoAudit.pageNum, vm.pageInfoAudit.pageSize).then(function (value) {
                vm.pageInfoAudit = value.data;
            })
        };
        vm.listUserData = function () {
            sysRequestService.listUserData(vm.params.qName, vm.pageInfoUser.pageNum, vm.pageInfoUser.pageSize).then(function (value) {
                vm.pageInfoUser = value.data;
            })
        };
        vm.queryData();

        return vm;
    })

    .controller("SysDeptController", function ($state, $scope, sysDeptService,sysRequestService,sysUserService) {
        var vm = this;
        var deptId = 1;


        vm.init=function(){
            $("#btnUploadHead").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'json',
                add: function (e, data) {
                    var file = data.files[0];

                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    if (data.result.ret) {
                        vm.sysUser.headUrl='/sys/attach/download/'+data.result.data.id;
                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });

            $("#btnUploadSign").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'json',
                add: function (e, data) {
                    var file = data.files[0];
                    if(file.name.indexOf(".dwg")<=0){
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    if (data.result.ret) {
                        vm.sysUser.signUrl='/sys/attach/download/'+data.result.data.id;
                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });

            //回车事件监听
            $('#autocomplete').bind('keydown',function(event){
                if(event.keyCode == "13") {
                    vm.queryData();
                }
            });
        }


        vm.buildTree = function () {
            sysDeptService.listSortedAll().then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId == 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        state: {opened: true, disabled: false, selected: false}
                    };
                    if (item.id == deptId) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_tree').jstree("destroy");
                $('#dept_tree')
                    .on('changed.jstree', function (e, data) {
                        var node=data.instance.get_node(data.selected[0]);
                        deptId = node.id;
                        vm.currentDeptId=node.id;
                        vm.currentDeptName=node.text;
                        vm.queryUserData();
                    })
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        }
                    });

            });
        };

        vm.addDept = function () {
            sysDeptService.getNewModel(deptId).then(function (value) {
                vm.sysDept = value.data.data;

                $("#deptModal").modal("show");
            });
        }

        vm.editDept = function () {
            sysDeptService.getModelById(deptId).then(function (value) {
                vm.sysDept = value.data.data;

                $("#deptModal").modal("show");
            });
        }

        vm.removeDept = function () {

            bootbox.confirm("您确认要删除该部门吗?", function (result) {
                if (result) {
                    sysDeptService.removeDept(vm.sysDept.id,user.userLogin).then(function (value) {
                        vm.sysDept = value.data.data;

                        $("#deptModal").modal("show");
                    });
                }
            });
        }

        vm.saveDept = function () {
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    if (vm.sysDept.id != undefined && vm.sysDept.id > 0) {
                        if ($("#dept_form").validate().form()) {
                            sysDeptService.update(vm.sysDept).then(function (value) {
                                if (value.data.ret) {
                                    toastr.success("更新成功!");
                                    $("#deptModal").modal("hide");
                                    vm.buildTree();
                                }
                            });
                        } else {
                            toastr.warning("请准确填写数据!")
                            return;
                        }

                    } else {
                        if ($("#dept_form").validate().form()) {
                            sysDeptService.insert(vm.sysDept).then(function (value) {
                                if (value.data.ret) {
                                    toastr.success("新增成功!");
                                    $("#deptModal").modal("hide");
                                    vm.buildTree();
                                }
                            });
                        } else {
                            toastr.warning("请准确填写数据!")
                            return;
                        }
                    }
                }
            });
        }

        vm.showDeptTree = function (sysDept) {
            if (sysDept.id == 1) {
                toastr.warning("该部门无法选择上级部门");
                return;
            }
            sysDeptService.listSortedAll(sysDept.id).then(function (value) {
                if (value.data.ret) {
                    var list = value.data.data;
                    var treeData = [];
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        var node = {
                            id: item.id,
                            parent: (item.parentId == 0 ? "#" : item.parentId.toString()),
                            text: item.name,
                            state: {opened: true, disabled: false, selected: false}
                        };
                        if (item.id == sysDept.parentId) {
                            node.state.selected = true;
                        }
                        treeData.push(node);
                    }
                    $('#dept_select_tree').jstree("destroy");
                    $('#dept_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            }
                        });
                    $("#deptTreeModal").modal("show");
                }
            });
        };

        vm.chooseDept = function () {
            var data = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.sysDept.parentId = data.id;
            vm.sysDept.parentName = data.text;
            $("#deptTreeModal").modal("hide");
        }

        vm.params = {rootId: deptId, qq: "", q: "", pageNum: 1, pageSize: 10};
        vm.pageInfo = {pageNum: 1, pageSize: 10};

        vm.queryUserData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.name = vm.params.qName;
            vm.params.rootId=deptId;
            vm.params.q=vm.params.qq;
            vm.loadPagedUserData();
        };

        vm.loadPagedUserData = function () {
            vm.params.pageNum = vm.pageInfo.pageNum;
            vm.params.pageSize = vm.pageInfo.pageSize;
            sysUserService.listPagedData(vm.params).then(function (value) {
                if(value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.addUser = function () {
            sysUserService.getNewModel(deptId).then(function (value) {
                vm.sysUser = value.data.data;
                $("#userModal").modal("show");
            });
        };

        vm.editUser = function (id) {
            sysUserService.getModelById(id).then(function (value) {
                vm.sysUser = value.data.data;
                $("#userModal").modal("show");
            });
        }

        vm.saveUser = function () {
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    if (vm.sysUser.id != undefined && vm.sysUser.id > 0) {
                        if ($("#user_from").validate().form()) {
                            sysUserService.update(vm.sysUser).then(function (value) {
                                if (value.data.ret) {
                                    toastr.success("更新成功!");
                                    $("#userModal").modal("hide");
                                    vm.queryUserData();
                                }
                            });
                        } else {
                            toastr.warning("请准确填写数据!")
                            return;
                        }
                    } else {
                        if ($("#user_from").validate().form()) {
                            sysUserService.insert(vm.sysUser).then(function (value) {
                                if (value.data.ret) {
                                    toastr.success("新增成功!");
                                    $("#userModal").modal("hide");
                                    vm.queryUserData();
                                }
                            });
                        } else {
                            toastr.warning("请准确填写数据!")
                            return;
                        }

                    }
                }
            });
        }

        vm.showUserDeptTree = function () {
            sysDeptService.listSortedAll().then(function (value) {
                if (value.data.ret) {
                    var list = value.data.data;
                    var treeData = [];
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        var node = {
                            id: item.id,
                            parent: (item.parentId == 0 ? "#" : item.parentId.toString()),
                            text: item.name,
                            state: {opened: true, disabled: false, selected: false}
                        };
                        if (item.id == vm.sysUser.deptId) {
                            node.state.selected = true;
                        }
                        treeData.push(node);
                    }
                    $('#user_dept_select_tree').jstree("destroy");
                    $('#user_dept_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            }
                        });
                    $("#userDeptTreeModal").modal("show");
                }
            });
        };

        vm.chooseUserDept = function () {
            var data = $('#user_dept_select_tree').jstree(true).get_selected(true)[0];
            vm.sysUser.deptId = data.id;
            vm.sysUser.deptName = data.text;
            $("#userDeptTreeModal").modal("hide");
        }

        vm.resetPwd = function (id) {
            bootbox.confirm("您确认要重置用户密码(mcc1234)吗?", function (result) {
                if (result) {
                    sysUserService.resetPwd(id).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("重置密码成功!");
                        }
                    });
                }
            });
        }

        $("#uploadFile").fileupload({
            maxFileSize: 99 * 1024 * 1024,
            dataType: 'json',
            formData:{
                source:"cp"
            },
            done: function (e, data) {
                if (data.result.ret) {
                    toastr.success("上传成功!");

                    sysUserService.batchAdd(data.result.data.id).then(function (value) {

                        if (value.data.ret) {
                            toastr.success("批量增加成功!");
                        }

                    });

                } else {
                    toastr.error("上传失败!");
                }
            }
        });

        vm.showUserModel = function (selected) {
            sysUserService.selectSimpleAll(selected).then(function (value) {
                vm.users = value.data.data;
                $("#deptUserModal").modal("show");
            })
        };

        vm.selectUser = function () {
            var logins = [];
            var names = []
            $(".cb_dept_user:checked").each(function () {
                var info = $(this).attr("name");
                logins.push(info.split('_')[0]);
                names.push(info.split('_')[1]);
            });
            vm.sysDept.chargeMan = logins[0];
            vm.sysDept.chargeName = names[0];


            $("#deptUserModal").modal("hide");
        }

        vm.buildTree();

        vm.init();

        return vm;

    })

    .controller("SysAclModuleController", function ($state, $scope,sysAclModuleService,sysAclService) {
        var vm = this;
        vm.currentAclModuleId=1;
        vm.params={q:"",qAcl:""};

        vm.buildTree = function () {
            sysAclModuleService.listSortedAll().then(function (value) {
                var list = value.data.data;
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        icon: item.icon,
                        state: {opened: true, disabled: false, selected: false}
                    };
                    if(item.deleted){
                        node.text=node.text+"(无效)";
                    }

                    if (item.id === vm.currentAclModuleId) {
                        node.state.selected = true;
                    }
                    treeData.push(node);
                }
                $('#acl_module_tree').jstree("destroy");
                $('#acl_module_tree')
                    .on('changed.jstree', function (e, data) {
                        var node=data.instance.get_node(data.selected[0]);
                        vm.currentAclModuleId=node.id;
                        vm.currentAclModuleName=node.text;
                        vm.loadAcl();
                    })
                    .jstree({
                        'core': {
                            'data': treeData
                        }
                    });

            });
        };

        vm.addAclModule = function () {
            sysAclModuleService.getNewModel(vm.currentAclModuleId).then(function (value) {
                vm.aclModule = value.data.data;
                $("#aclModuleModal").modal("show");
            });
        }

        vm.editAclModule = function () {

            if(vm.currentAclModuleId==1){
                toastr.warning("根节点无法修改!");
                return;
            }
            sysAclModuleService.getModelById(vm.currentAclModuleId).then(function (value) {
                vm.aclModule = value.data.data;
                $("#aclModuleModal").modal("show");
            });
        }

        vm.saveAclModule = function () {
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    if (vm.aclModule.id != undefined && vm.aclModule.id > 0) {
                        sysAclModuleService.update(vm.aclModule).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#aclModuleModal").modal("hide");
                                vm.buildTree();
                            }
                        });
                    } else {
                        sysAclModuleService.insert(vm.aclModule).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("新增成功!");
                                $("#aclModuleModal").modal("hide");
                                vm.buildTree();
                            }
                        });
                    }
                }
            });
        }

        vm.showParentTree = function (item) {
            sysAclModuleService.listSortedAll(item.id).then(function (value) {
                if (value.data.ret) {
                    var list = value.data.data;
                    var treeData = [];
                    for (var i = 0; i < list.length; i++) {
                        var data = list[i];
                        var node = {
                            id: data.id,
                            parent: (data.parentId == 0 ? "#" : data.parentId.toString()),
                            text: data.name,
                            icon: data.icon,
                            state: {opened: true, disabled: false, selected: false}
                        };
                        if (data.id == item.parentId) {
                            node.state.selected = true;
                        }
                        treeData.push(node);
                    }
                    $('#acl_module_parent_tree').jstree("destroy");
                    $('#acl_module_parent_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            }
                        });
                    $("#aclModuleParentTreeModal").modal("show");
                }
            });
        };

        vm.chooseAclModuleParent = function () {
            var data = $('#acl_module_parent_tree').jstree(true).get_selected(true)[0];
            vm.aclModule.parentId = data.id;
            vm.aclModule.parentName = data.text;
            $("#aclModuleParentTreeModal").modal("hide");
        }

        vm.clearCache1=function(){
            $http({
                method: 'POST',
                url: '/common/invalidateCache.json',
            }).then(function (response) {
                if (response.data.ret){
                    toastr.success("清除缓存成功!");
                }
            });
        };

        vm.loadAcl = function () {
            sysAclService.listAclByModules(vm.currentAclModuleId,vm.params.qAcl).then(function (value) {
                vm.acls=value.data.data
            })
        };

        vm.addAcl = function () {
            sysAclService.getNewModel(vm.currentAclModuleId).then(function (value) {
                vm.sysAcl = value.data.data;
                $("#aclModal").modal("show");
            });
        };

        vm.editAcl = function (id) {
            sysAclService.getModelById(id).then(function (value) {
                vm.sysAcl = value.data.data;
                $("#aclModal").modal("show");
            });
        }

        vm.saveAcl = function () {
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    if (vm.sysAcl.id != undefined && vm.sysAcl.id > 0) {
                        sysAclService.update(vm.sysAcl).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#aclModal").modal("hide");
                                vm.loadAcl();
                            }
                        });
                    } else {
                        sysAclService.insert(vm.sysAcl).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("新增成功!");
                                $("#aclModal").modal("hide");
                                vm.loadAcl();
                            }
                        });
                    }
                }
            });
        }

        vm.showAclModuleSelectTree = function () {
            sysAclModuleService.listSortedAll().then(function (value) {
                if (value.data.ret) {
                    var list = value.data.data;
                    var treeData = [];
                    for (var i = 0; i < list.length; i++) {
                        var data = list[i];
                        var node = {
                            id: data.id,
                            parent: (data.parentId == 0 ? "#" : data.parentId.toString()),
                            text: data.name,
                            icon: data.icon,
                            state: {opened: true, disabled: false, selected: false}
                        };
                        if (data.id == vm.sysAcl.aclModuleId) {
                            node.state.selected = true;
                        }
                        treeData.push(node);
                    }
                    $('#acl_module_select_tree').jstree("destroy");
                    $('#acl_module_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            }
                        });
                    $("#aclModuleSelectModal").modal("show");
                }
            });
        };

        vm.selectAclModule = function () {
            var data = $('#acl_module_select_tree').jstree(true).get_selected(true)[0];
            vm.sysAcl.aclModuleId = data.id;
            vm.sysAcl.aclModuleName = data.text;
            $("#aclModuleSelectModal").modal("hide");
        }


        vm.buildTree();

        $scope.basicInit();

        return vm;

    })

    .controller("SysRoleController", function ($state, $scope,sysRoleService,hrEmployeeService,hrDeptService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qUserName: "",qAclName:"", queryName:"",currentRoleId: 1,pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.currentRoleId=vm.params.currentRoleId;
        vm.init = function (){
            vm.buildTree();
            $scope.basicInit();
        }

        vm.queryData = function (){
            vm.pageInfo.pageNum=1;
            vm.loadEmployee();
            vm.loadAcl();
        }

        vm.loadEmployee=function(){
            var params={roleId:vm.params.currentRoleId,q:vm.params.qUserName,pageNum:vm.pageInfo.pageNum,pageSize:vm.pageInfo.pageSize}
            hrEmployeeService.listPagedDataByRoleId(params).then(function (value) {
                vm.pageInfo=value.data.data;
                setCacheParams(key,vm.params,vm.pageInfo);
            })
        }

        vm.buildTree = function () {
            var queryName=vm.params.queryName;
            sysRoleService.listSortedAll(queryName).then(function (value) {
                var list = value.data.data;
                var treeData = [];
                var rootNodes=[];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    if(rootNodes.join(',').indexOf(item.roleCategory)===-1){
                        rootNodes.push(item.roleCategory);
                    }
                }
                rootNodes.push("其它");

                var selectNode=false;

                for(var i=0;i<rootNodes.length;i++){
                    var node = {
                        id: rootNodes[i],
                        parent: "#",
                        text: rootNodes[i],
                        state: {opened: i==0, disabled: false, selected: false}
                    };
                    treeData.push(node);
                }

                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: item.roleCategory==""?"其它":item.roleCategory,
                        text: item.name,
                        state: {opened: false, disabled: false, selected: false}
                    };
                    if (vm.params.currentRoleId == parseInt(item.id)) {
                        node.state.selected = true;
                        selectNode=true;
                        vm.params.currentRoleName=node.text;
                    }
                    if(item.deleted){
                        node.text=node.text+"(作废)";
                    }
                    treeData.push(node);
                }

                if(!selectNode) treeData[i].state.selected=true;

                $('#role_tree').jstree("destroy");
                $('#role_tree')
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if(node.parent!="#") {
                            if (vm.params.currentRoleId == parseInt(node.id)) {
                                vm.loadAcl();
                                vm.loadEmployee();
                            } else {
                                vm.params.currentRoleId = node.id;
                                vm.params.currentRoleName = node.text;
                                vm.params.qUserName = "";
                                vm.params.qAclName = "";
                                vm.queryData();
                            }
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData
                        }
                    });
            });
        };


        vm.addRole = function () {
            sysRoleService.getNewModel().then(function (value) {
                vm.sysRole = value.data.data;
                $("#roleModal").modal("show");
            });
        }

        vm.editRole = function () {

            sysRoleService.getModelById(vm.params.currentRoleId).then(function (value) {
                vm.sysRole = value.data.data;
                $("#roleModal").modal("show");
            });
        }

        vm.saveRole = function () {
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    if (vm.sysRole.id != undefined && vm.sysRole.id > 0) {
                        sysRoleService.update(vm.sysRole).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#roleModal").modal("hide");
                                vm.buildTree();
                            }
                        });
                    } else {
                        sysRoleService.insert(vm.sysRole).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("新增成功!");
                                $("#roleModal").modal("hide");
                                vm.buildTree();
                            }
                        });
                    }
                }
            });
        }

        vm.clearCache1=function(){
            $http({
                method: 'POST',
                url: '/common/invalidateCache.json',
            }).then(function (response) {
                if (response.data.ret){
                    toastr.success("清除缓存成功!");
                }
            });
        };


        vm.showRoleModal=function(item){
            vm.current=item;
            sysRoleService.selectAll().then(function (value) {
                if(value.data.ret){
                    vm.roles=value.data.data;
                }
            })
            $("#roleSelectModal").modal("show");
        }

        vm.saveSelectRole=function(){
            var roleIds = [];
            $(".cb_role:checked").each(function () {
                roleIds.push($(this).attr("data-id"));
            });
            hrEmployeeService.updateRoleIds(vm.current.userLogin,roleIds.join(',')).then(function (value) {
                if(value.data.ret){
                    $("#roleSelectModal").modal("hide");
                    toastr.success("保存成功!");
                    vm.loadEmployee();
                }
            });

        }
        vm.loadAcl = function () {
            sysRoleService.listAclByRoleId(vm.params.qAclName,vm.params.currentRoleId).then(function (value) {
                vm.acls=value.data.data;
            })
        };

        vm.deleteAcl = function (id) {
            bootbox.confirm("您确认要删除数据吗?", function (result) {
                if (result) {
                    sysRoleService.deleteAcl(id).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.loadAcl();
                        }
                    });

                }
            });
        }

        vm.deleteUser = function (id) {
            bootbox.confirm("您确认要删除数据吗?", function (result) {
                if (result) {
                    sysRoleService.deleteUser(id).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryUserData();
                        }
                    });

                }
            });
        }

        vm.showUserSelectTree = function () {
            sysRoleService.getUserTree(vm.params.currentRoleId).then(function (value) {
                if (value.data.ret) {
                    var treeData = value.data.data;
                    $('#user_select_tree').jstree("destroy");
                    $('#user_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            },
                            "checkbox" : {
                                "keep_selected_style" : false
                            },
                            "plugins" : [ "wholerow", "checkbox" ]
                        });
                    $("#userSelectModal").modal("show");
                }
            });
        };

        vm.saveSelectUser = function () {
            var userList = $('#user_select_tree').jstree(true).get_selected().join(',');
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    sysRoleService.saveRoleUserList(vm.params.currentRoleId,userList).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("更新成功!");
                            $("#userSelectModal").modal("hide");
                            vm.loadEmployee();
                        }
                    });
                }
            });
        };



        vm.showRoleAclSelectTree = function () {
            sysRoleService.getAclTreeByRoleId(vm.params.currentRoleId).then(function (value) {
                if (value.data.ret) {
                    var treeData = value.data.data;
                    $('#acl_select_tree').jstree("destroy");
                    $('#acl_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            },
                            "checkbox" : {
                                "keep_selected_style" : false
                            },
                            "plugins" : [ "wholerow", "checkbox" ]
                        });
                    $("#roleAclSelectModal").modal("show");
                }
            });
        };

        vm.saveRoleSelectAcl = function () {
            var aclList = $('#acl_select_tree').jstree(true).get_selected().join(',');
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {

                    sysRoleService.saveRoleAclList(vm.params.currentRoleId,aclList).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("更新成功!");
                            $("#roleAclSelectModal").modal("hide");
                            vm.loadAcl();
                        }
                    });
                }
            });
        };

        vm.showUserAclTree = function (item) {
            vm.currentUser=item;
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
                            vm.loadEmployee();
                        }
                    });
                }
            });
        };



        vm.removeUserAcl=function(){
            bootbox.confirm("您确认要删除用户的独立权限配置吗?", function (result) {
                if (result) {
                    sysRoleService.removeUserAcl(vm.currentUser.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("更新成功!");
                            $("#userAclSelectModal").modal("hide");
                            vm.loadEmployee();
                        }
                    });
                }
            });
        }

        vm.showDeptModal=function(ids) {
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

        vm.saveSelectDept=function(){
            var deptList= $('#dept_select_tree').jstree(true).get_selected();
            vm.sysRole.childDeptIds=","+deptList.join(',')+",";
            $("#deptSelectModal").modal("hide");
        }


        vm.init();

        return vm;

    })

    .controller("SysAttachController", function ($state, $scope, sysAttachService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{fileNames: "",userName:"",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.init=function(){
            vm.loadPagedData();
            $("#uploadFile").fileupload({
                dataType: 'text',
                url:'/sys/attach/receive.do',
                formData:{source:"web",creator:user.userLogin,},
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
                    if(result.ret) {
                        toastr.success("上传成功!");
                        vm.queryData();
                    }else{
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        vm.download=function(id){
            sysAttachService.download(id).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9);
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };


        vm.loadPagedData = function () {
            var params={q:vm.params.q,pageNum:vm.pageInfo.pageNum,pageSize:vm.pageInfo.pageSize,fileNames: vm.params.fileNames,userName:vm.params.userName};
            sysAttachService.listPagedData(params).then(function (value) {
                vm.pageInfo = value.data.data;
                setCacheParams(key,vm.params,vm.pageInfo);
            });
        };

        vm.init();

        return vm;
    })


    .controller("SysConfigController", function ($state, $http,$scope,sysConfigService) {
        var vm = this;

        vm.init=function(){
            sysConfigService.getConfig().then(function (value) {
                vm.item=value.data.data;
            })
        }

        vm.update=function() {
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    sysConfigService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("更新成功!");
                        }
                    });
                }
            });

        }

        vm.clearCache1=function(){
            $http({
                method: 'POST',
                url: '/common/invalidateCache.json',
            }).then(function (response) {
                if (response.data.ret){
                    toastr.success("清除缓存成功!");
                }
            });
        }

        vm.init();

        return vm;
    })

    .controller("SysSoftwareController", function ($state, $scope,sysSoftwareService) {
        var vm = this;
        vm.params = {softwareNames: "",deptNames:""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var uiSref="sys.software";
        vm.init = function () {
            vm.queryData();

            $scope.loadRightData(user.userLogin,uiSref);

            $("#uploadFile").fileupload({
                maxFileSize: 10*1024 * 1024 * 1024,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator: user.userLogin
                },
                send:function(e,data){
                    $("#softwareModel").modal("hide");
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
                        toastr.success("上传成功!");
                        vm.item.attachId = result.data.id;
                        vm.item.softwareName=data.files[0].name;
                        $scope.$apply();
                        setTimeout(function () {
                            $("#softwareModel").modal("show");
                        },500);

                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        vm.queryData = function () {
            vm.params.name = vm.params.qName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

        };

        vm.loadPagedData = function () {
            var params = {softwareNames: vm.params.softwareNames,deptNames:vm.params.deptNames, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin};
            sysSoftwareService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.add = function () {
            sysSoftwareService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $("#softwareModel").modal("show");
                }
            })
        };

        vm.update = function () {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                sysSoftwareService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!");
                        $("#softwareModel").modal("hide");
                        vm.loadPagedData();
                    }
                });
            }
        }

        vm.edit = function (id) {
            sysSoftwareService.getModelById(id).then(function (value) {
                vm.item = value.data.data;
                $("#softwareModel").modal("show");
            });
        }

        vm.remove = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    sysSoftwareService.remove(id,user.userLogin).then(function (value) {
                        vm.queryData();
                    })
                }
            })
        }

        vm.init();
        return vm;
    })

    .controller("SysRecoverFileController", function ($state, $scope,edFileService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{fileNames: "",userName:"",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

        };

        vm.loadPagedData = function () {
            var params = {fileNames: vm.params.fileNames,userName:vm.params.userName, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin};
            edFileService.listDeletedFile(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };

        vm.recoverFile = function (id) {
            bootbox.confirm("确定要恢复数据吗?", function (result) {
                if (result) {
                    edFileService.recoverFile(id).then(function (value) {
                        if (value.data.ret){
                            toastr.success("恢复成功!");
                            vm.queryData();
                        }
                    })
                }
            })
        }
        vm.loadPagedData();
        return vm;
    })

    .controller("SysRecoverCpFileController", function ($state, $scope,cpFileService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{fileNames: "",userName:"",pageNum: 1, pageSize: $scope.pageSize,total:0});

        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};


        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();

        };

        vm.loadPagedData = function () {
            var params = {fileNames: vm.params.fileNames,userName:vm.params.userName, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            cpFileService.listDeletedCpFile(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };

        vm.recoverFile = function (id) {
            bootbox.confirm("确定要恢复数据吗?", function (result) {
                if (result) {
                    cpFileService.recoverFile(id).then(function (value) {
                        if (value.data.ret){
                            toastr.success("恢复成功!");
                            vm.queryData();
                        }
                    })
                }
            })
        }
        vm.loadPagedData();
        return vm;
    })

    .controller("FiveFileContentController", function ($state, $stateParams,$rootScope,$scope,fiveHomeService,actService,fiveContentFileService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params ={fileNames: "",userName:"",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="sys.contentFile";

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.loadPagedData = function () {
            /*var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });*/
            /*var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,
                userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            };*/
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveContentFileService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                   // setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };



        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveContentFileService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'fileName',placeholder:'请输文件名..'},
                    2:{type:"input",colName:'deptName',placeholder:'本地路径'},
                    3:{type:"input",colName:'drafterName',placeholder:'创建人'},
                    4:{type:"input",colName:'gmtCreate'},
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:5};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        $(".cb_edFile:checked").each(function () {
            var index = $(this).attr("data-index");
            if (index < $rootScope.files.length) {
                $rootScope.downloadEdFileWithXml($rootScope.files[index]);
            }
        });

        vm.init();
        vm.loadPagedData();
        return vm;
    })

    .controller("FiveOaWordSizeController", function ($state, $stateParams,$rootScope,$scope,fiveHomeService,actService,fiveOaWordSizeService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params ={fileNames: "",userName:"",pageNum: 1, pageSize:10,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="sys.wordSize";

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
        };



        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaWordSizeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    // setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadItem=function(){
            fiveOaWordSizeService.loadItem(word).then(function (value) {
                if (value.data.ret) {
                    vm.items = value.data.data;
                }
            })
        };

        vm.showItemModel = function (id) {
            /*if (id === 0) {
                fiveOaWordSizeService.getNewModelItem(word).then(function (value) {
                    if (value.data.ret) {
                        vm.item = value.data.data;
                        $("#detailModel").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {*/
                fiveOaWordSizeService.getModelItemById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.item = value.data.data;
                        $("#itemModal").modal("show");
                    }
                })
        };



        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaWordSizeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'word',placeholder:'请输入字..'},
                    2:{type:"input",colName:'mark',placeholder:'号'},
                    3:{type:"input",colName:'year',placeholder:'年份'},
                    4:{type:"input",colName:'type',placeholder:'类型'},
                    5:{type:"input",colName:'abandonMark',placeholder:'跳号'},
                    6:{type:"input",colName:'deptName',placeholder:'部门'},
                    7:{type:"input",colName:'gmtCreate'},
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        $(".cb_edFile:checked").each(function () {
            var index = $(this).attr("data-index");
            if (index < $rootScope.files.length) {
                $rootScope.downloadEdFileWithXml($rootScope.files[index]);
            }
        });

        vm.init();
        vm.loadPagedData();
        return vm;
    })



