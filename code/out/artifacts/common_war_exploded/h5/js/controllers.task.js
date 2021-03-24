angular.module('controllers.task', [])

    .controller('TaskOaDecisionMakingController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService,commonFileService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/decisionMaking/listDetails.json',
                params: {
                    businessKey:businessKey,
                    userLogin:enLogin
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }


        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.item.stampName = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })
    //签报
    .controller('TaskOaSignQuoteController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,fiveOaSignQuoteService2) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var signQuoteId= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        /*新增议题*/
        vm.createDecisionBySignBusinessKey=function(){
            $http({
                method: 'POST',
                url: '/five/oa/decisionMaking/createDecisionBySignBusinessKey.json',
                params: {
                    businessId:businessKey,
                }
            }).then(function(value){
                if (value.data.ret){
                    mui.alert("议题已进入议题库!");
                }
                else{
                    mui.error("新增议题出现异常，请联系管理员!");
                }
            })
        }
        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                if (vm.item.meetingType!=''){
                    vm.createDecisionBySignBusinessKey();
                }
                if (vm.item.flag!="不会签"&& $rootScope.processInstance.myRunningTaskName.indexOf('部门负责人')>=0&&vm.item.instructDeptName==''){
                    mui.warning("请选择会签部门!")
                    return;
                }
                if ($rootScope.processInstance.myRunningTaskName.indexOf('部门负责人')>=0&&vm.item.belongThreeOne==''){
                    mui.warning("请选择是否属于“三重一大”事项")
                    return;
                }
                if(vm.item.belongThreeOne=='是'&&(vm.item.belongType==''||vm.item.belongContent=='')){
                    mui.warning("请选择，“三重一大”事项，具体内容和分类")
                    return;
                }


                if ( $rootScope.processInstance.myRunningTaskName.indexOf('公司办')>=0&&vm.item.instructLeaderName==''){
                    mui.warning("请选择批示领导!")
                    return;
                }
                fiveOaSignQuoteService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        //决策会议发起
                        if (vm.item.meetingType!=''){
                            vm.createDecisionBySignBusinessKey();
                        }
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })
            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }

        vm.loadData = function (loadProcess) {
            fiveOaSignQuoteService2.getModelById(signQuoteId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                    vm.loadRead();
                }
            })
        };

        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='companyCheckMan'){
                    vm.item.companyCheckMan= user.enLogin;
                    vm.item.companyCheckManName = user.cnName;
                }
                if(vm.userType=='instructLeader'){
                    vm.item.instructLeader= user.enLogin;
                    vm.item.instructLeaderName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType =='companyLeader'){
                    vm.item.companyLeader = enLoginList.join(',');
                    vm.item.companyLeaderName = cnNameList.join(',');
                }

            }
        }

        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,multiple:multiple,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        },
                        "checkbox" : {
                            "keep_selected_style" :  false , //是否默认选中
                            "three_state" :  false , //父子级别级联选择
                        },
                        "plugins" : [ "wholerow", "checkbox","types","themes"]
                    });
            });
        }

        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "instructDept") {
                vm.item.instructDeptId = ids.join(',');
                vm.item.instructDeptName = names.join(',');
            } else {
                vm.item.deptId = ids.join(',');
                vm.item.deptName = names.join(',');
            }
        }

        vm.loadRead = function () {
            $http({
                method: 'POST',
                url:   '/wuzhou/file/getModelByBusinessKey.json',
                params: {
                    businessKey: vm.item.businessKey,
                    fileType:0,
                }
            }).then(function (response) {
                vm.redheadFile = response.data.data;
            });

        };

        vm.openRead=function(id){
           var url = "/wuzhou/file/preview/" + id;
            window.location.href=url;
        };

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //公司发文
    .controller('TaskOaDispatchController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,fiveOaDispatchService2) {
    var vm = this;
    var businessKey = $stateParams.businessKey;
    var signQuoteId= businessKey.split('_')[1];

    vm.init = function () {
        vm.loadData(true);

        $scope.initTopTab();

        commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
            if(value.data.ret) {
                $scope.data = value.data.data.data;
                $scope.groupList = value.data.data.groupList;
                $rootScope.initWebControl();
            }else{
                mui.alert("未找到数据("+value.data.msg+")");
                window.location.href="/h5/index.html";
            }
        });

        $scope.loadProcessInstance(businessKey,enLogin);

        commonCodeService.selectAll().then(function (value) {
            if(value.data.ret){
                $scope.sysCodes=value.data.data;
            }
        });

        vm.loadRead();
    }


    vm.save = function () {
        vm.item.operateUserLogin = enLogin;

        fiveOaDispatchService2.update(vm.item).then(function (value) {
            if (value.data.ret) {
                toastr.success("保存成功!")
                vm.loadData(false);
            }
        })
    }

    vm.loadData = function (loadProcess) {
            fiveOaDispatchService2.getModelById(signQuoteId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            })
        };

    vm.loadRead = function (loadProcess) {
            fiveOaDispatchService2.getModelByBusinessKey(businessKey,0).then(function (value) {
                if (value.data.ret) {
                    vm.redheadFile = value.data.data;
                }
            })
        };


    vm.openRead=function(id){
        var url = "/wuzhou/file/preview/" + id;
        window.location.href=url;
    };
    //发送流程验证 流程发送的时候 没有调用这个方法。
    $scope.showSendTask=function(send){
        if ($("#detail_form").validate().form()) {
            vm.item.operateUserLogin = enLogin;
            fiveOaDispatchService2.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                        if (result.index > 0) {
                            if (send) {
                                actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                    if (value.data.ret) {
                                        mui.toast("发送成功!");
                                        $scope.loadProcessInstance();
                                    } else {
                                        mui.alert(value.data.msg);
                                    }
                                });
                            } else {
                                actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                    if (value.data.ret) {
                                        mui.toast("打回成功!");
                                        $scope.loadProcessInstance();
                                    } else {
                                        mui.alert(value.data.msg);
                                    }
                                });
                            }
                        }
                    })
                }
            })
        }else {
            mui.alert("请准确填写数据!");
            return false;
        }
    }


    vm.showUserModal=function(type,dataSource,multiple,selects) {
        vm.userType =type;
        document.activeElement.blur();
        $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
            if (event.keyCode === 13)  //回车键的键值为13
                vm.loadUser();
        })
        $("#contentForm").css("display", "none");
        $("#contentFormUser").css("display", "block");
        vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
        vm.loadUser();
    }

    vm.loadUser=function() {
        var config=vm.config;
        $http({
            method: 'POST',
            url: '/common/user/listFormUserData.json',
            params: config
        }).then(function (response) {
            vm.users = response.data.data;
        });
    }

    vm.saveSelectUser=function(user) {
        vm.backMain();
        if (!$.isArray(user)) {
            if(vm.userType=='signer'){
                vm.item.signer= user.enLogin;
                vm.item.signerName = user.cnName;
            }else if (vm.userType=='companyOffice'){
                vm.item.companyOffice= user.enLogin;
                vm.item.companyOfficeName = user.cnName;
            }
        } else {
            var enLoginList = [];
            var cnNameList = [];
            for (var i = 0; i < user.length; i++) {
                var selectedUser = user[i];
                enLoginList.push(selectedUser.enLogin);
                cnNameList.push(selectedUser.cnName);
            }
            if(vm.userType =='companyLeader'){
                vm.item.companyLeader = enLoginList.join(',');
                vm.item.companyLeaderName = cnNameList.join(',');
            }

        }
    }

    vm.saveSelectUsers=function(){
        var users=[];
        for(var i=0;i<vm.users.length;i++) {
            if (vm.users[i].selected) {
                users.push(vm.users[i]);
            }
        }
        vm.saveSelectUser(users);
    }


    /**
     * 选择部门
     * @param inputCode 字段表示
     * @param dataSource 筛选数据表示
     * @param multiple 是否多选
     * @param selects 已选中
     */
    vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
        document.activeElement.blur();
        $("#contentForm").css("display", "none");
        $("#contentFormDept").css("display", "block");
        vm.selectDeptInputCode=inputCode;
        var config = {dataSource: dataSource, selects: selects,enLogin: enLogin}
        $http({
            method: 'POST',
            url: '/common/user/listFormDeptTree.json',
            params: config
        }).then(function (response) {
            vm.deptTree = response.data.data;
            $('#deptTree').jstree("destroy");
            $('#deptTree')
                .jstree({
                    'core': {
                        'data': vm.deptTree,
                        "multiple": multiple
                    }
                });
        });
    }

    /**
     * 保存部门
     */
    vm.saveSelectDept=function() {
        vm.backMain();
        var nodes = $('#deptTree').jstree(true).get_selected(true);
        var names = [];
        var ids = [];
        for (var i = 0; i < nodes.length; i++) {
            names.push(nodes[i].text);
            ids.push(nodes[i].id);
        }
        if (vm.selectDeptInputCode == "deptId") {
            vm.item.deptId = ids.join(',');
            vm.item.deptName = names.join(',');
        } else if (vm.selectDeptInputCode == "realSendMan") {
            vm.item.realSendManName = names.join(',');
        } else if (vm.selectDeptInputCode == "copySendMan") {
            vm.item.copySendManName = names.join(',');
        }
    }


    vm.backMain=function(){
        $("#contentForm").css("display", "block");
        $("#contentFormUser").css("display", "none");
        $("#contentFormDept").css("display", "none");
    }

    vm.init();
})
    //部门发文
    .controller('TaskOaDepartmentPostController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,fiveOaDepartmentPostService2) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        vm.loadData = function (loadProcess) {
            fiveOaDepartmentPostService2.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                    vm.loadRead();
                }
            })
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                fiveOaDepartmentPostService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })
            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='drafter'){
                    vm.item.drafter= user.enLogin;
                    vm.item.drafterName = user.cnName;
                }else if (vm.userType=='checkMan'){
                    vm.item.checkMan= user.enLogin;
                    vm.item.checkManName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }

            }
        }

        vm.saveSelectUsers=function(){
            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }


        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
                params: config
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        }
                    });
            });
        }

        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "deptId") {
                vm.item.deptId = ids.join(',');
                vm.item.deptName = names.join(',');
            } else if (vm.selectDeptInputCode == "realSendMan") {
                vm.item.realSendManName = names.join(',');
            } else if (vm.selectDeptInputCode == "copySendMan") {
                vm.item.copySendManName = names.join(',');
            }
        }

        vm.loadRead = function () {
            $http({
                method: 'POST',
                url:   '/wuzhou/file/getModelByBusinessKey.json',
                params: {
                    businessKey: businessKey,
                    fileType:0,
                }
            }).then(function (response) {
                vm.redheadFile = response.data.data;
            });

        };

        vm.openRead=function(id){
            var url = "/wuzhou/file/preview/" + id;
            window.location.href=url;
        };

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //公司收文
    .controller('TaskOaFileInstructionController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,fiveOaFileInstructionService2) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        vm.loadData = function (loadProcess) {
            fiveOaFileInstructionService2.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                    vm.loadRead();
                }
            })
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                fiveOaFileInstructionService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })
            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }

        vm.saveSupervise = function () {
            mui.prompt("请输入督办意见", function (result) {
                if (result.index > 0) {
                    fiveOaFileInstructionService2.getNewModelByType(2802,'文件督办',vm.item.companyLeader,businessKey,vm.item.fileTitle,result.value).then(function(value){
                        if (value.data.ret){
                            mui.alert("创建督办任务成功!");
                        }
                    })
                }
            })


        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='signer'){
                    vm.item.signer= user.enLogin;
                    vm.item.signerName = user.cnName;
                }else if (vm.userType=='companyLeader'){
                    vm.item.companyLeader= user.enLogin;
                    vm.item.companyLeaderName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType =='readLeader'){
                    vm.item.readLeader = enLoginList.join(',');
                    vm.item.readLeaderName = cnNameList.join(',');
                }
            }
        }



        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
                params: config
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        }
                    });
            });
        }

        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "undertakeDeptId") {
                vm.item.undertakeDeptId = ids.join(',');
                vm.item.undertakeDeptName = names.join(',');
            }
        }
        vm.loadRead = function () {
            $http({
                method: 'POST',
                url:   '/wuzhou/file/getModelByBusinessKey.json',
                params: {
                    businessKey: businessKey,
                    fileType:0,
                }
            }).then(function (response) {
                vm.redheadFile = response.data.data;
            });

        };
        vm.openRead=function(id){
            var url = "/wuzhou/file/preview/" + id;
            window.location.href=url;
        };
        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //报告文单
    .controller('TaskOaReportController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,fiveOaReportService2) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        vm.loadData = function (loadProcess) {
            fiveOaReportService2.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            })
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                fiveOaReportService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })
            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='companyOffice'){
                    vm.item.companyOffice= user.enLogin;
                    vm.item.companyOfficeName = user.cnName;
                }else if (vm.userType=='chargeMan'){
                    vm.item.chargeMan= user.enLogin;
                    vm.item.chargeManName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
               if (vm.userType=='companyLeader'){
                    vm.item.companyLeader= user.enLogin;
                    vm.item.companyLeaderName = user.cnName;
                }
            }
        }



        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
                params: config
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        }
                    });
            });
        }

        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "deptId") {
                vm.item.deptId = ids.join(',');
                vm.item.deptName = names.join(',');
            }
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
     //客户信息
    .controller('TaskBusinessCustomerController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,businessCustomerService2) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);



        }


        vm.loadData = function (loadProcess) {
            businessCustomerService2.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            })
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($rootScope.processInstance.currentTaskName.indexOf("档案人员") >= 0  && vm.item.code == "") {
                mui.alert("请先填写 客户编号");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                businessCustomerService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }else {
                        mui.alert("客户编号重复!");
                    }
                })
            }else {
                mui.alert("请准确填客户信息!");
                return false;
            }
        }

        vm.init();
    })
    //投标申请
    .controller('TaskOaBidApplyController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,fiveOaBidApplyService2) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        vm.loadData = function (loadProcess) {
            fiveOaBidApplyService2.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            })
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if (send == true && $rootScope.processInstance.myRunningTaskName.indexOf('经营发展部人员（合同）') >= 0) {
                if (vm.item.reviewUser == '') {
                    mui.alert("请填写评审人员！");
                    return;
                }
            }
            if ($rootScope.processInstance.myRunningTaskName.indexOf('发起人') >= 0 &&
                vm.item.reviewUser == '' && vm.item.bidRank == '非公司级') {
                mui.alert("请填写评审人员！");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                fiveOaBidApplyService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })
            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {

            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if (vm.userType=='reviewUser'){
                    vm.item.reviewUser= user.enLogin;
                    vm.item.reviewUserName = user.cnName;
                }
            }
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //合同评审
    .controller('TaskBusinessContractReviewController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService,fiveBusinessContractReviewService2) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        vm.save = function () {
            vm.item.operateUserLogin = enLogin;

            fiveBusinessContractReviewService2.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    vm.loadData(false);
                }
            })
        }

        vm.loadData = function (loadProcess) {
            fiveBusinessContractReviewService2.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            })
            fiveBusinessContractReviewService2.listDetailById(id).then(function (value) {
                if (value.data.ret) {
                    vm.contractReviewDetails = value.data.data;
                }
            })
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {

                if (vm.item.projectName == ""&&!vm.item.main) {
                    mui.alert("请在PC端填写选择备案项目");
                    return;
                }
                if (vm.item.contractNo == "") {
                    mui.alert("请先填写 合同号");
                    return;
                }
                if(vm.item.reviewLevel=='院级'&&vm.item.deptReviewUserName==''){
                    mui.alert("请先填写 院级评审人员");
                    return;
                }
                if (vm.item.totalDesignerName == ""||vm.item.projectManagerName == ""||vm.item.businessChargeLeaderName == "") {
                    mui.alert("请先填写 项目经理/总设计师/合同分管副总!");
                    return;
                }
                //如果是 确认 节点 填写签约时间 和 扫描件
                if(($rootScope.processInstance.currentTaskName.indexOf("盖章合同上传")>=0||$rootScope.processInstance.currentTaskName.indexOf("确认")>=0)&&
                    (vm.item.signTime ==""||vm.item.contractAttachUrl=="")){
                    mui.alert("请在PC端填写 签约时间 合同盖章扫描附件");
                    return;
                }
                //如果是 印花税 节点 填写到账金额
                if($rootScope.processInstance.currentTaskName.indexOf("印花税")>=0&&(vm.item.contractMoney ==""||vm.item.taxType=="")){
                    mui.alert("请先填写 合同额 印花税目");
                    return;
                }

                vm.item.operateUserLogin = enLogin;
                fiveBusinessContractReviewService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })
            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='totalDesigner'){
                    vm.item.totalDesigner= user.enLogin;
                    vm.item.totalDesignerName = user.cnName;
                }else if (vm.userType=='projectManager'){
                    vm.item.projectManager= user.enLogin;
                    vm.item.projectManagerName = user.cnName;
                }else if (vm.userType=='businessChargeLeader'){
                    vm.item.businessChargeLeader= user.enLogin;
                    vm.item.businessChargeLeaderName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType =='reviewUser'){
                    vm.item.reviewUser = enLoginList.join(',');
                    vm.item.reviewUserName = cnNameList.join(',');
                }else if(vm.userType =='deptReviewUser'){
                    vm.item.deptReviewUser = enLoginList.join(',');
                    vm.item.deptReviewUserName = cnNameList.join(',');
                }

            }
        }



        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
                params: config
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        }
                    });
            });
        }

        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "deptId") {
                vm.item.deptId = ids.join(',');
                vm.item.deptName = names.join(',');
            }
        }


        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })

    //软件购置申请
    .controller('TaskOaSoftwareController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var softwareId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/software/listDetail.json',
                params: {
                    softwareId:softwareId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })
    //信息发布
    .controller('TaskOaNoticeApplyController', function ($scope,$sce, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService,actTaskHandleService,commonCodeService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var id=businessKey.split('_')[1];
        vm.showHtml=true;

        vm.init=function() {

            $scope.initTopTab();

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

            vm.loadDate();

        }

        vm.loadDate=function(){
            $http({
                method: 'POST',
                url: '/oa/noticeApply/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                vm.item = response.data.data;
                vm.item.text = $sce.trustAsHtml(vm.item.noticeContent);
                vm.hrefOWA="https://owa.wuzhou.com.cn/op/embed.aspx?src="+encodeURIComponent("https://co.wuzhou.com.cn/common/attach/download/"+vm.item.attachIds);
                $sce.trustAsResourceUrl(vm.hrefOWA);
                vm.change();
            });
        }
        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                $http({
                    method: 'POST',
                    url: '/oa/noticeApply/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                                vm.loadDate();
                            }
                        })
                    }
                })

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.change=function(){
            if ("公司新闻,通知公告,文件简报,集团制度,图片新闻".indexOf(vm.item.noticeLevel)>-1){
                vm.showType=false;
                vm.item.noticeType=vm.item.noticeLevel;
            }else {
                vm.showType=true;
                commonCodeService.listDataByCatalog(enLogin,vm.item.noticeLevel+'_电子公告').then(function (value) {
                    if (value.data.ret){
                        vm.listCode=value.data.data;
                    }
                })
            }
            if (vm.item.noticeType=='图片新闻'||vm.item.attachIds==''){
                vm.showHtml=false;
            }else {
                vm.showHtml=true;
            }
        }

        vm.init();
    })
    //年度软件预算
    .controller('TaskOaInformationPlanController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var planId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/informationPlan/listDetail.json',
                params: {
                    planId:planId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })
    //年度信息化采购预算
    .controller('TaskOaInformationEquipmentProcurementController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var planId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/informationEquipmentProcurement/listDetail.json',
                params: {
                    id:planId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })
    //信息化设备采购事项审批
    .controller('TaskOaInformationEquipmentApplyController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var planId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/informationEquipmentApply/listDetail.json',
                params: {
                    id:planId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })
    //月度外协费费用支出明细
    .controller('TaskBusinessOutAssistController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var planId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/business/outAssist/getDetailById.json',
                params: {
                    id:planId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })
    //项目资金使用计划
    .controller('TaskOaProjectFundPlanController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var planId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/projectFundPlan/listDetail.json',
                params: {
                    id:planId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })

    //分包合同评审
    .controller('TaskBusinessSubpackageController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        vm.loadData = function () {
            $http({
                method: 'POST',
                url: '/business/subpackage/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                vm.item = response.data.data;
            });
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                //如果是 确认 节点 填写签约时间 和 扫描件
                if(($rootScope.processInstance.currentTaskName.indexOf("盖章合同上传")>=0||$rootScope.processInstance.currentTaskName.indexOf("确认")>=0)&&(vm.item.signTime ==""||vm.item.contractAttachUrl=="")){
                    mui.alert("请先填写 签约时间 合同盖章扫描附件");
                    return;
                }
                //如果是 印花税 节点 填写到账金额
                if($rootScope.processInstance.currentTaskName.indexOf("印花税")>=0&&(vm.item.contractMoney ==""||vm.item.taxType.length==0)){
                    mui.alert("请先填写 合同额 印花税目");
                    return;
                }
                //如果是 经营发展部 节点 公司级 填写评审人员
                if($rootScope.processInstance.currentTaskName.indexOf("经营发展部人员")>=0&&(vm.item.reviewLevel=='公司级'&&vm.item.reviewUserName=='')){
                    mui.alert("请先填写 公司级评审人员");
                    return;
                }
                vm.item.operateUserLogin = enLogin;

                $http({
                    method: 'POST',
                    url: '/business/subpackage/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                });

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='totalDesigner'){
                    vm.item.totalDesigner= user.enLogin;
                    vm.item.totalDesignerName = user.cnName;
                }else if (vm.userType=='projectManager'){
                    vm.item.projectManager= user.enLogin;
                    vm.item.projectManagerName = user.cnName;
                }else if (vm.userType=='businessChargeLeader'){
                    vm.item.businessChargeLeader= user.enLogin;
                    vm.item.businessChargeLeaderName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType =='reviewUser'){
                    vm.item.reviewUser = enLoginList.join(',');
                    vm.item.reviewUserName = cnNameList.join(',');
                }else if(vm.userType =='deptReviewUser'){
                    vm.item.deptReviewUser = enLoginList.join(',');
                    vm.item.deptReviewUserName = cnNameList.join(',');
                }

            }
        }



        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
                params: config
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        }
                    });
            });
        }

        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "deptId") {
                vm.item.deptId = ids.join(',');
                vm.item.deptName = names.join(',');
            }
        }


        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //采购合同评审
    .controller('TaskBusinessPurchaseController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }


        vm.loadData = function () {
            $http({
                method: 'POST',
                url: '/business/subpackage/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                vm.item = response.data.data;
            });
        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                //如果是 确认 节点 填写签约时间 和 扫描件
                if(($rootScope.processInstance.currentTaskName.indexOf("盖章合同上传")>=0||$rootScope.processInstance.currentTaskName.indexOf("确认")>=0)&&(vm.item.signTime ==""||vm.item.contractAttachUrl=="")){
                    mui.alert("请先填写 签约时间 合同盖章扫描附件");
                    return;
                }
                //如果是 印花税 节点 填写到账金额
                if($rootScope.processInstance.currentTaskName.indexOf("印花税")>=0&&(vm.item.contractMoney ==""||vm.item.taxType.length==0)){
                    mui.alert("请先填写 合同额 印花税目");
                    return;
                }
                //如果是 经营发展部 节点 公司级 填写评审人员
                if($rootScope.processInstance.currentTaskName.indexOf("经营发展部人员")>=0&&(vm.item.reviewLevel=='公司级'&&vm.item.reviewUserName=='')){
                    mui.alert("请先填写 公司级评审人员");
                    return;
                }
                vm.item.operateUserLogin = enLogin;

                $http({
                    method: 'POST',
                    url: '/business/purchase/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                });

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='totalDesigner'){
                    vm.item.totalDesigner= user.enLogin;
                    vm.item.totalDesignerName = user.cnName;
                }else if (vm.userType=='projectManager'){
                    vm.item.projectManager= user.enLogin;
                    vm.item.projectManagerName = user.cnName;
                }else if (vm.userType=='businessChargeLeader'){
                    vm.item.businessChargeLeader= user.enLogin;
                    vm.item.businessChargeLeaderName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType =='reviewUser'){
                    vm.item.reviewUser = enLoginList.join(',');
                    vm.item.reviewUserName = cnNameList.join(',');
                }else if(vm.userType =='deptReviewUser'){
                    vm.item.deptReviewUser = enLoginList.join(',');
                    vm.item.deptReviewUserName = cnNameList.join(',');
                }

            }
        }



        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
                params: config
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        }
                    });
            });
        }

        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "deptId") {
                vm.item.deptId = ids.join(',');
                vm.item.deptName = names.join(',');
            }
        }


        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //行政事务 设备验收
    .controller('TaskOaEquipmentAcceptanceController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var planId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/equipmentAcceptance/listDetail.json',
                params: {
                    id:planId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })
    //协同文件
    .controller('TaskOaFileSynergyController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService,commonUserService,commonFormDataService,actProcessHandleService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

            vm.loadRead();
        }



        vm.loadData = function (loadProcess) {

            $http({
                method: 'POST',
                url: '/five/oa/fileSynergy/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                if (response.data.ret) {
                    vm.item = response.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                    vm.loadRead();
                }
            });

        };

        vm.loadRead = function () {
            $http({
                method: 'POST',
                url:   '/wuzhou/file/getModelByBusinessKey.json',
                params: {
                    businessKey: businessKey,
                    fileType:0,
                }
            }).then(function (response) {
                vm.redheadFile = response.data.data;
            });

        };

        vm.openRead=function(id){
            var url = "/wuzhou/file/preview/" + id;
            window.location.href=url;
        };
        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                $http({
                    method: 'POST',
                    url: '/five/oa/fileSynergy/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }


        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='signer'){
                    vm.item.signer= user.enLogin;
                    vm.item.signerName = user.cnName;
                }else if (vm.userType=='chargeMen'){
                    vm.item.chargeMen= user.enLogin;
                    vm.item.chargeMenName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType=='signer'){
                    vm.item.signer = enLoginList.join(',');
                    vm.item.signerName = cnNameList.join(',');
                }else if (vm.userType=='chargeMen'){
                    vm.item.chargeMen = enLoginList.join(',');
                    vm.item.chargeMenName = cnNameList.join(',');
                }
            }
        }

        vm.saveSelectUsers=function(){
            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }
        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //财务资金余额上报
    .controller('TaskFinanceBalanceController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();


            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

            vm.loadRead();
        }



        vm.loadData = function (loadProcess) {

            $http({
                method: 'POST',
                url: '/five/finance/balance/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                if (response.data.ret) {
                    vm.item = response.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                    vm.loadRead();
                }
            });

        };

        vm.loadRead = function () {
            $http({
                method: 'POST',
                url:   '/wuzhou/file/getModelByBusinessKey.json',
                params: {
                    businessKey: businessKey,
                    fileType:0,
                }
            }).then(function (response) {
                vm.redheadFile = response.data.data;
            });

        };

        vm.openRead=function(id){
            var url = "/wuzhou/file/preview/" + id;
            window.location.href=url;
        };
        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                $http({
                    method: 'POST',
                    url: '/five/finance/balance/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //项目启动
    .controller('TaskBusinessContractController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            $scope.loadProcessInstance(businessKey,enLogin);

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });

        }

        vm.loadData = function (loadProcess) {
            $http({
                method: 'POST',
                url: '/business/contract/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                if (response.data.ret) {
                    vm.item = response.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            });

        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){
            if(vm.item.recordNo==''&&$rootScope.processInstance.myRunningTaskName.indexOf('档案人员')>=0){
                mui.alert("请填写档案号!");
                return;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                $http({
                    method: 'POST',
                    url: '/business/contract/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })

    //行政事务 办公家具采购
    .controller('TaskFurniturePurchaseController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var planId=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/oa/furniturePurchase/listDetail.json',
                params: {
                    id:planId,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })

    //行政事务 办公家具采购
    .controller('TaskAssetScrapController', function ($scope, $rootScope,$http,$stateParams,commonFormDataService,actProcessHandleService) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var id=businessKey.split('_')[1];

        vm.init=function() {

            $scope.initTopTab();

            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                if(value.data.ret) {
                    $scope.data = value.data.data.data;
                    $scope.groupList = value.data.data.groupList;
                    $rootScope.initWebControl();
                }else{
                    mui.alert("未找到数据("+value.data.msg+")");
                    window.location.href="/h5/index.html";
                }
            });

            $scope.loadProcessInstance(businessKey,enLogin);

            vm.loadDetail();
        }

        vm.loadDetail=function(){
            $http({
                method: 'POST',
                url: '/five/asset/assetScrap/listDetail.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                vm.decisions = response.data.data;
            });
        }

        vm.backUrl = function () {
            if (window.localStorage.getItem("back-url")) {
                window.location.replace(window.localStorage.getItem("back-url"));
            } else {
                window.location.replace("/h5/index.html");
            }
        }

        vm.showLink=function(businessKey){
            window.location.href="/h5/taskDetail.html?businessKey="+businessKey+"&enLogin="+enLogin;
        }


        vm.toggleStar = function () {
            actProcessHandleService.toggleStar($scope.processInstance.instance.id, enLogin).then(function (value) {
                if (value.data.ret) {
                    mui.toast($scope.processInstance.stared?'取消关注!':'关注成功!');
                    $scope.loadProcessInstance(businessKey,enLogin);
                } else {
                    mui.alert(value.data.msg);
                }
            })
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
        }

        vm.showUserModal=function(item,group) {
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })

            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = vm.getUserRequestConfig(item, group);
            vm.config.q="";
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=$.extend({loginItem:{},item:{}},vm.config);
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.getUserRequestConfig=function(item,group) {
            if (!item.commonFormDetail.editable) return;

            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {};
            var majorName = '';
            var buildName = "";
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === key) {
                    loginItem = currentItem;
                } else if (code === 'majorName') {
                    majorName = currentItem.inputValue;
                } else if (code == "buildNameList") {
                    buildName = currentItem.inputValue.join(",");
                } else if (code == "buildName") {
                    buildName = currentItem.inputValue;
                }
            }
            var selects = loginItem.inputValue;
            if (loginItem.inputValue.constructor === Array) {
                selects = loginItem.inputValue.join(',');
            }

            var config = {
                title: item.commonFormDetail.inputText,
                enLogin: enLogin,
                multiple: item.commonFormDetail.multiple,
                selects: selects,
                formDataId: group.formDataId,
                roleCode: key.replace('Man', '').replace('Men', ''),
                majorName: majorName,
                buildName: buildName,
                dataSource: item.commonFormDetail.dataSource,
                loginItem:loginItem,
                item:item,
            };
            return config;
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                vm.config.loginItem.inputValue = user.enLogin;
                vm.config.item.inputValue = user.cnName;
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                vm.config.loginItem.inputValue = enLoginList.join(',');
                vm.config.item.inputValue = cnNameList.join(',');
            }
        }

        vm.saveSelectUsers=function(){

            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.toggleMultipleModal=function(detail){
            vm.detail=detail;
            if(vm.detail.inputValue.length>0){
                for(var i=0;i<vm.detail.dataSource.length;i++){
                    var code=vm.detail.dataSource[i];
                    for(var j=0;j<vm.detail.inputValue.length;j++){
                        if(vm.detail.inputValue[j]==code.name){
                            code.selected=true;
                            break;
                        }
                    }
                }
            }
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.detail.dataSource.length; i++) {
                var code = vm.detail.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.detail.inputValue = inputValue;
        }

        $rootScope.save=function(){
            var data=$scope.getFormData($scope.data.id,$scope.groupList);
            data["autoSubmit"]=false;
            commonFormDataService.save(data).then(function (value) {
                if(value.data.ret){
                    mui.alert('保存成功!')
                }else{
                    mui.alert(value.data.msg);
                }
            });
        }

        vm.init();
    })

    //科研审批流程 -内部科研项目申请
    .controller('TaskInlandProjectApplyController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            $scope.loadProcessInstance(businessKey,enLogin);

        }

        vm.loadData = function (loadProcess) {
            $http({
                method: 'POST',
                url: '/five/oa/inlandProjectApply/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                if (response.data.ret) {
                    vm.item = response.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            });

        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                $http({
                    method: 'POST',
                    url: '/five/oa/inlandProjectApply/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }

        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='scientificFirstTrial'){
                    vm.item.scientificFirstTrial= user.enLogin;
                    vm.item.scientificFirstTrialName = user.cnName;
                }else if (vm.userType=='companyOffice'){
                    vm.item.companyOffice= user.enLogin;
                    vm.item.companyOfficeName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType=='scientificFirstTrial'){
                    vm.item.scientificFirstTrial= enLoginList.join(',');;
                    vm.item.scientificFirstTrialName = cnNameList.join(',');
                }

            }
        }

        vm.saveSelectUsers=function(){
            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
    //科研审批流程 -外部标准规范、图集项目申请
    .controller('TaskExternalStandardApplyController', function ($scope, $rootScope,$http,$stateParams,commonCodeService,actTaskHandleService) {
        var vm = this;
        var businessKey = $stateParams.businessKey;
        var id= businessKey.split('_')[1];

        vm.init = function () {
            vm.loadData(true);

            $scope.initTopTab();

            $scope.loadProcessInstance(businessKey,enLogin);

        }

        vm.loadData = function (loadProcess) {
            $http({
                method: 'POST',
                url: '/five/oa/externalStandardApply/getModelById.json',
                params: {
                    id:id,
                }
            }).then(function (response) {
                if (response.data.ret) {
                    vm.item = response.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            });

        };

        //发送流程验证 流程发送的时候 没有调用这个方法。
        $scope.showSendTask=function(send){

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                $http({
                    method: 'POST',
                    url: '/five/oa/externalStandardApply/update.json',
                    data:vm.item
                }).then(function (response) {
                    if (response.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })

            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }

        vm.showUserModal=function(type,dataSource,multiple,selects) {
            vm.userType =type;
            document.activeElement.blur();
            $("#contentFormUser_q").unbind('keydown').bind('keydown',function () {
                if (event.keyCode === 13)  //回车键的键值为13
                    vm.loadUser();
            })
            $("#contentForm").css("display", "none");
            $("#contentFormUser").css("display", "block");
            vm.config = {dataSource:dataSource,selects:selects,multiple:multiple};
            vm.loadUser();
        }

        vm.loadUser=function() {
            var config=vm.config;
            $http({
                method: 'POST',
                url: '/common/user/listFormUserData.json',
                params: config
            }).then(function (response) {
                vm.users = response.data.data;
            });
        }

        vm.saveSelectUser=function(user) {
            vm.backMain();
            if (!$.isArray(user)) {
                if(vm.userType=='scientificFirstTrial'){
                    vm.item.scientificFirstTrial= user.enLogin;
                    vm.item.scientificFirstTrialName = user.cnName;
                }else if (vm.userType=='companyOffice'){
                    vm.item.companyOffice= user.enLogin;
                    vm.item.companyOfficeName = user.cnName;
                }
            } else {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < user.length; i++) {
                    var selectedUser = user[i];
                    enLoginList.push(selectedUser.enLogin);
                    cnNameList.push(selectedUser.cnName);
                }
                if(vm.userType=='scientificFirstTrial'){
                    vm.item.scientificFirstTrial= enLoginList.join(',');;
                    vm.item.scientificFirstTrialName = cnNameList.join(',');
                }

            }
        }

        vm.saveSelectUsers=function(){
            var users=[];
            for(var i=0;i<vm.users.length;i++) {
                if (vm.users[i].selected) {
                    users.push(vm.users[i]);
                }
            }
            vm.saveSelectUser(users);
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }

        vm.init();
    })
     //用印申请
    .controller('TaskStampOfficeDetailController', function ($rootScope,$scope,$http,$stateParams,commonCodeService,fiveOaStampApplyOfficeService2) {
        var vm=this;
        var businessKey=$stateParams.businessKey;
        var applyId= businessKey.split('_')[1];

        vm.init=function () {
            vm.loadData(true);

            $scope.initTopTab();

            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });
        }

        vm.loadData = function (loadProcess) {
            fiveOaStampApplyOfficeService2.getModelById(applyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $rootScope.initWebControl();
                        $scope.loadProcessInstance(businessKey,enLogin);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        $scope.save = function () {
            vm.item.operateUserLogin = enLogin;
            fiveOaStampApplyOfficeService2.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    mui.alert("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = enLogin;
                fiveOaStampApplyOfficeService2.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        mui.prompt("请输入" + (send ? "发送" : "打回") + "意见", function (result) {
                            console.log("showSendTask");
                            if (result.index > 0) {
                                if (send) {
                                    actTaskHandleService.sendTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("发送成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                } else {
                                    actTaskHandleService.backTaskSimple($scope.processInstance.taskId, result.value, enLogin).then(function (value) {
                                        if (value.data.ret) {
                                            mui.toast("打回成功!");
                                            $scope.loadProcessInstance();
                                        } else {
                                            mui.alert(value.data.msg);
                                        }
                                    });
                                }
                            }
                        })
                    }
                })
            }else {
                mui.alert("请准确填写数据!");
                return false;
            }
        }

        vm.toggleMultipleModal=function(detail,codeCatalog){
            commonCodeService.listDataByCatalog("",codeCatalog).then(function (value) {
                 if (value.data.ret){
                     vm.dataSource=value.data.data;
                     for(var i=0;i<vm.dataSource.length;i++) {
                         var code = vm.dataSource[i];
                         if (detail.indexOf(code.name) != -1) {
                             code.selected = true;
                         }
                     }
                 }
            })
            document.activeElement.blur();
            mui('#multipleSheet').popover('toggle');
        }

        vm.saveMultiple=function() {
            mui('#multipleSheet').popover('toggle');
            var inputValue = [];
            for (var i = 0; i < vm.dataSource.length; i++) {
                var code = vm.dataSource[i];
                if (code.selected) {
                    inputValue.push(code.name);
                }
            }
            vm.item.stampName = inputValue.join(",");
        }

        /**
         * 选择部门
         * @param inputCode 字段表示
         * @param dataSource 筛选数据表示
         * @param multiple 是否多选
         * @param selects 已选中
         */
        vm.showDeptModal=function(inputCode,dataSource,multiple,selects) {
            document.activeElement.blur();
            $("#contentForm").css("display", "none");
            $("#contentFormDept").css("display", "block");
            vm.selectDeptInputCode=inputCode;
            var config = {dataSource: dataSource, selects: selects,multiple:multiple,enLogin: enLogin}
            $http({
                method: 'POST',
                url: '/common/user/listFormDeptTree.json',
            }).then(function (response) {
                vm.deptTree = response.data.data;
                $('#deptTree').jstree("destroy");
                $('#deptTree')
                    .jstree({
                        'core': {
                            'data': vm.deptTree,
                            "multiple": multiple
                        },
                        "checkbox" : {
                            "keep_selected_style" :  false , //是否默认选中
                            "three_state" :  false , //父子级别级联选择
                        },
                        "plugins" : [ "wholerow", "checkbox","types","themes"]
                    });
            });
        }
        /**
         * 保存部门
         */
        vm.saveSelectDept=function() {
            vm.backMain();
            var nodes = $('#deptTree').jstree(true).get_selected(true);
            var names = [];
            var ids = [];
            for (var i = 0; i < nodes.length; i++) {
                names.push(nodes[i].text);
                ids.push(nodes[i].id);
            }
            if (vm.selectDeptInputCode == "itemDept") {
                vm.item.itemDept = ids.join(',');
                vm.item.itemDeptName = names.join(',');
            } else {
                vm.item.deptId = ids.join(',');
                vm.item.deptName = names.join(',');
            }
        }

        vm.backMain=function(){
            $("#contentForm").css("display", "block");
            $("#contentFormUser").css("display", "none");
            $("#contentFormDept").css("display", "none");
        }
        vm.init();
        return vm;
    })
