angular.module('controllers.five.oa', [])

    //公文管理 签报
    .controller("FiveOaSignQuoteController", function ($state, $scope,$rootScope, fiveOaSignQuoteService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;

        vm.params = getCacheParams(key,{dispatchType: "签报", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaSignQuote";

        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
        };
        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaSignQuoteService.downTempleXls(params).then(function (response) {

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
            fiveOaSignQuoteService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaSignQuoteDetail", {signQuoteId: id});
        }


        vm.add = function () {
            fiveOaSignQuoteService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaSignQuoteService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.loadPagedData();

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaSignQuoteService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'item',placeholder:'请输事项..'},
                    2:{type:"input",colName:'deptName',placeholder:'签送单位'},
                    3:{type:"input",colName:'drafterName',placeholder:'负责人'},
                    4:{type:"select",colName:'flag',placeholder:'会签类型..',option:[{title:"全部",value:""},{title:"不会签",value:"不会签"},{title:"通用会签",value:"通用会签"},{title:"制度会签",value:"制度会签"}]},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.init();
        return vm;

    })
    .controller("FiveOaSignQuoteDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,fiveOaSignQuoteService,fiveContentFileService,commonCodeService,fiveOaDecisionMakingService,hrDeptService,myActService) {
        var vm = this;
        var uiSref="five.oaSignQuote";
        var signQuoteId = $stateParams.signQuoteId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true)

        }

        /*加载 红头文件和文档*/
        vm.loadDoc=function(){
            fiveContentFileService.getModelByBusinessKey(vm.item.businessKey,0).then(function (value) {
                if (value.data.ret){
                    vm.redHead=value.data.data;
                }
            })
        }

        vm.loadData = function (loadProcess) {
            fiveOaSignQuoteService.getModelById(signQuoteId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $rootScope.loadOpinionProcessInstance(vm.item.processInstanceId);
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.loadActRelevance(vm.item.businessKey,"","",false);
                        $rootScope.loadContentFiles(vm.item.businessKey,true);

                    }
                    vm.loadDoc();
                    vm.loadDecisionMaking();
                    $("#submitTime").datepicker('setDate', vm.item.submitTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;

            fiveOaSignQuoteService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='agent'){
                $scope.showOaSelectEmployeeModal_({title:"请选择经办人",
                    type:"部门",userLoginList: vm.item.agent,multiple:false,deptIds:user.deptId,parentDeptId:user.deptId});
            }else if (vm.status=='companyCheckMan'){
                hrDeptService.getModelById(59).then(function(value){
                    vm.hrdept=value.data.data;
                })
                $scope.showOaSelectEmployeeModal_({title:"请选择推荐负责人", type:"人员",userLoginList: vm.item.companyCheckMan,userLogins:vm.hrdept.deptFirstLeader,multiple:true});
            }else if (vm.status=='deptChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送签单位负责人", type:"部门",userLoginList: vm.item.deptChargeMan,multiple:false,deptIds:user.deptId,parentDeptId:user.deptId});
            } else if (vm.status=='partyChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择党群工作部负责人", type:"部门",userLoginList: vm.item.partyChargeMan,multiple:false,deptIds:"72",parentDeptId:72});
            }else if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择会签领导", type:"部门",userLoginList: vm.item.companyLeader,multiple:true,deptIds:"16",parentDeptId:16});
            }else if (vm.status=='instructLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择批示领导",type:"部门", userLoginList: vm.item.instructLeader,multiple:false,deptIds:"16",parentDeptId:16});

            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='agent'){
                vm.item.agent = $scope.selectedOaUserLogins_;
                vm.item.agentName = $scope.selectedOaUserNames_;
            }else if (vm.status=='companyCheckMan'){
                vm.item.companyCheckMan = $scope.selectedOaUserLogins_;
                vm.item.companyCheckManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMan'){
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
            } else if (vm.status=='partyChargeMan'){
                vm.item.partyChargeMan = $scope.selectedOaUserLogins_;
                vm.item.partyChargeManName = $scope.selectedOaUserNames_;
            } else if (vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
                vm.save();
            }else if (vm.status=='instructLeader'){
                vm.item.instructLeader = $scope.selectedOaUserLogins_;
                vm.item.instructLeaderName = $scope.selectedOaUserNames_;
                vm.save();
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaSignQuoteService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        //决策会议发起
                        if (vm.item.meetingType!=''){
                            vm.createDecisionBySignBusinessKey();
                        }
                        if (vm.item.flag!="不会签"&& $rootScope.processInstance.myRunningTaskName.indexOf('部门负责人')>=0&&vm.item.instructDeptName==''){
                            toastr.warning("请选择会签部门!")
                            return;
                        }
                        if ($rootScope.processInstance.myRunningTaskName.indexOf('部门负责人')>=0&&vm.item.belongThreeOne==''){
                            toastr.warning("请选择是否属于“三重一大”事项")
                            return;
                        }
                        if(vm.item.belongThreeOne=='是'&&(vm.item.belongType==''||vm.item.belongContent=='')){
                            toastr.warning("请选择，“三重一大”事项，具体内容和分类")
                            return;
                        }


                        if ( $rootScope.processInstance.myRunningTaskName.indexOf('公司办')>=0&&vm.item.instructLeaderName==''){
                            toastr.warning("请选择批示领导!")
                            return;
                        }

                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId,result) {
                            //todo跳转到首页
                            if (result.completeTask){
                                $state.go("five.dashboard");
                            }else {
                                $scope.refresh();
                            }
                        });
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function() {
            if (vm.opt=='instructDeptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择会签部门",type:"选部门", deptIdList: ","+vm.item.instructDeptId,
                    multiple:true,deptIds:"1"});
            }else if (vm.opt=='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送签部门",type:"选部门", deptIdList: vm.item.deptId+",",
                    multiple:false,deptIds:"1"});
            }



        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.opt=='instructDeptId'){
                vm.item.instructDeptName = $scope.selectedOaDeptNames_;
                vm.item.instructDeptId = $scope.selectedOaDeptIds_;
            }else if (vm.opt=='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = $scope.selectedOaDeptIds_;
            }

        }

        vm.showMeetingModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"会议类型").then(function(value){
                if (value.data.ret){
                    vm.meetingList=value.data.data;
                    $("#meetingTypeModal").modal("show");
                }
            })
        }

        vm.saveMeetingType=function(){
            var type=[];
            $(".cb_meet:checked").each(function () {
                type.push($(this).attr("data-name"));
            });
            vm.item.meetingType = type.join(',');
            $("#meetingTypeModal").modal("hide");
            vm.save();
        }

        vm.selectAllFile=function(){
            $(".cb_meet").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }

        vm.toggleSelectFile=function(){
            $(".cb_meet").each(function () {
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

        /*新增议题*/
        vm.createDecisionBySignBusinessKey=function(){
            fiveOaDecisionMakingService.createDecisionBySignBusinessKey(vm.item.businessKey).then(function(value){
                if (value.data.ret){
                    toastr.success("议题已进入议题库!");
                }
                else{
                    toastr.error("新增议题出现异常，请联系管理员!");
                }
            })
        }

        vm.loadDecisionMaking=function(){
            fiveOaDecisionMakingService.getModelByLinkBusiness(vm.item.businessKey).then(function(value){
                if (value.data.ret){
                    vm.decisionMakingList=value.data.data;
                }
            })
        }

        vm.getDecisionMakingModel=function(item){
            vm.currenDetail=item;
            $("#decisionDetailModal").modal("show")
        }

        vm.print=function () {
            fiveOaSignQuoteService.getPrintData(signQuoteId).then(function (value) {
                if(value.data.ret){
                    vm.printData=value.data.data;
                    lodop=getLodop();
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    if (vm.printData.flag=='不会签'){
                        var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                        setTimeout(function () {
                            var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                            lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                            lodop.PREVIEW();
                        }, 500);
                    }else if (vm.printData.flag=='通用会签'){
                        var strBodyStyle = "<style>" + document.getElementById("print_style_common").innerHTML + "</style>";
                        setTimeout(function () {
                            var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area_common").innerHTML + "</body>";
                            lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                            lodop.PREVIEW();
                        }, 500);
                    }else if (vm.printData.flag=='制度会签'){
                        var strBodyStyle = "<style>" + document.getElementById("print_area_system").innerHTML + "</style>";
                        setTimeout(function () {
                            var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area_system").innerHTML + "</body>";
                            lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                            lodop.PREVIEW();
                        }, 500);
                    }

                }
            })
        }

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })

    //公文管理 公司收文
    .controller("FiveOaFileInstructionController", function ($state, $scope,$rootScope, fiveOaFileInstructionService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaFileInstruction";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime:vm.params.startTime,endTime:vm.params.endTime
            });
            fiveOaFileInstructionService.downTempleXls(params).then(function (response) {

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
            var params = $.extend(tablefilter.params,{
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaFileInstructionService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaFileInstructionDetail", {instructionId: id});
        }

        vm.add = function () {
            fiveOaFileInstructionService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaFileInstructionService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaFileInstructionService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'fileTitle',placeholder:'请输文件标题..'},
                    2:{type:"input",colName:'sendDeptName',placeholder:'来文单位'},
                    3:{type:"input",colName:'signerName'},
                    4:{type:"input",colName:'receiveTime'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();
        return vm;

    })
    .controller("FiveOaFileInstructionDetailController", function ($state,$stateParams,$rootScope,$scope,actTaskQueryService,fiveContentFileService,fiveOaFileInstructionService,fiveOaWordSizeService,fiveSuperviseFileService) {
        var vm = this;
        var instructionId = $stateParams.instructionId;
        var uiSref="five.oaFileInstruction";
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init=function(){
            vm.year=new Date().getFullYear();
            vm.selectword=" ";
            vm.selectyear=""+vm.year;
            vm.selectmark=0;
            $scope.loadRightData(user.userLogin,"five.oaFileInstruction");
            vm.loadData(true);
        }

        vm.optionlist=function(){
            actTaskQueryService.listHistoricTaskInstanceByInstanceId(vm.item.processInstanceId, user.userLogin).then(function (value) {
                var list = value.data.data;
                //流程意见分类
                list.reverse();//2020-12-19HNZ 倒序取最新意见
                var optionlist=[];
                for (var i=0,l=list.length;i<l;i++){
                    for(var j = i + 1; j < l; j++){
                        if (list[i].name == list[j].name&&list[i].assigneeName==list[j].assigneeName
                            && list[j].assigneeName!=""&&list[j].latestComment.indexOf("取回")<0){
                            ++i;
                        }
                    }
                    optionlist.push(list[i]);
                }
                list=optionlist.reverse(); //倒序 顺序展示意见

                var optionlistCountSign=[];
                var optionlistLeader=[];
                var optionlistOther=[];
                for (var i=0,l=list.length;i<l;i++){
                    if(list[i].name.indexOf("各单位")>0&&list[i].assigneeName!=""){
                        optionlistCountSign.push(list[i])
                    }else if(list[i].name.indexOf("领导")>0||list[i].name.indexOf("核稿")>0&&list[i].assigneeName!=""){
                        optionlistLeader.push(list[i])
                    }else {
                        optionlistOther.push(list[i])
                    }
                }
                vm.optionlistCountSign=optionlistCountSign;
                vm.optionlistLeader=optionlistLeader.reverse();
                vm.optionlistOther=optionlistOther;
            });
        }

        /*加载 红头文件和文档*/
        vm.loadDoc=function(){
            fiveContentFileService.getModelByBusinessKey(vm.item.businessKey,0).then(function (value) {
                if (value.data.ret){
                    vm.redHead=value.data.data;
                }
            })
        }

        vm.loadData = function (loadProcess) {
            fiveOaFileInstructionService.getModelById(instructionId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.loadActRelevance(vm.item.businessKey,"","",false);
                        $rootScope.loadContentFiles(vm.item.businessKey,true);
                        vm.loadWordSize(new Date().getFullYear());
                        vm.optionlist();
                    }
                    vm.loadDoc();
                    $("#receiveTime").datepicker('setDate', vm.item.receiveTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaFileInstructionService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='signer'){
                $scope.showOaSelectEmployeeModal_({title:"请选择签收人员", type:"部门",userLoginList: vm.item.signer,multiple:false,deptIds:"59",parentDeptId:59});//公司办公室
            }else if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司领导人员",type:"部门", userLoginList: vm.item.companyLeader,multiple:false,deptIds:"16",parentDeptId:16});
            }else if (vm.status=='userLogin'){
                $scope.showOaSelectEmployeeModal_({title:"请选择督办",type:"部门", userLoginList: vm.super.companyLeader,multiple:true,deptIds:"59",parentDeptId:59});
            }else if (vm.status=='readLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司领导人员",type:"部门", userLoginList: vm.item.readLeader,multiple:true,deptIds:"16",parentDeptId:16});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='signer'){
                vm.item.signer = $scope.selectedOaUserLogins_;
                vm.item.signerName = $scope.selectedOaUserNames_;
            }else if (vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='userLogin'){
                vm.super.userLogin = $scope.selectedOaUserLogins_;
                vm.super.userName = $scope.selectedOaUserNames_;
            }else if (vm.status=='readLeader'){
                vm.item.readLeader = $scope.selectedOaUserLogins_;
                vm.item.readLeaderName = $scope.selectedOaUserNames_;
            }
        };



        //发送流程验证
        $scope.showSendTask=function(send){
            if (vm.item.security=='是'){
                toastr.warning("注：此平台为非密平台，严禁填写涉密信息!")
                return false;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaFileInstructionService.update(vm.item).then(function (value) {
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
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            if(vm.opt=="deptId"){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请单位",type:"选部门", deptIdList: vm.item.deptId ,
                    multiple:false,deptIds:"1",parentDeptId:vm.item.deptId});
            }else{
                $scope.showOaSelectEmployeeModal_({title:"请选择承办单位",type:"选部门", deptIdList: vm.item.undertakeDeptId ,
                    multiple:true,deptIds:"1",parentDeptId:vm.item.deptId});
            }

        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt=="deptId"){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = $scope.selectedOaDeptIds_;
            }else{
                vm.item.undertakeDeptName = $scope.selectedOaDeptNames_;
                vm.item.undertakeDeptId = $scope.selectedOaDeptIds_;
            }

        }

        vm.changeWord=function(size){
            if (size<3){
                fiveOaWordSizeService.getMarkByChange(vm.selectword,vm.selectyear).then(function(value){
                    if (value.data.ret){
                        vm.selectmark=value.data.data.mark ;
                    }
                })
            }
        }

        vm.loadWordSize=function(year){
            var key="59_公司办公室(董事会办公室）,38_人力资源部";
            fiveOaWordSizeService.getCanUseWord(key,year,"收文").then(function (value) {
                if (value.data.ret){
                    vm.wordSizeList=value.data.data;
                }
            })
        }

        vm.loadHistory=function(key){
            fiveOaWordSizeService.listUserWord(vm.selectword,vm.selectyear,key).then(function (value) {
                if (value.data.ret){
                    vm.wordHistoryList=value.data.data;
                    $("#wordSizeModel").modal("show");
                }
            })
        }

        vm.updateWordSize=function(){
            var params={selectWord:vm.selectword,selectYear:vm.selectyear,selectMark:vm.selectmark,businessKey:vm.item.businessKey,userLogin:user.userLogin}
            var message="您确定要申请该文号吗?请谨慎操作!";
            if(vm.item.receiveWordSize!=""){
                message="您确定要申请该文号吗?已有文号："+vm.item.receiveWordSize+",请谨慎操作!";
            }
            bootbox.confirm(message, function (result) {
                if (result) {
                    fiveOaWordSizeService.updateWordSize(params).then(function(value){
                        if (value.data.ret){
                            if (vm.wordsizeId!=0){
                                toastr.success("文号保存更新!");
                                vm.item.receiveWordSize=vm.selectword+"〔"+vm.selectyear+"〕"+vm.selectmark+'号';
                                vm.item.word=vm.selectword;
                                vm.item.year=vm.selectyear;
                                vm.item.mark=vm.selectmark;
                                vm.save();
                            }
                        }
                    })
                }
            })
        }

        vm.print=function () {
            fiveOaFileInstructionService.getPrintData(instructionId).then(function (value) {
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

        //展示督办窗口
        vm.showSupervise=function(){
            vm.super={userLogin:'2802',userName:'武彦宁',companyLeader:vm.item.companyLeader,businessId:vm.item.processInstanceId,superviseType:'文件督办',fileTitle:vm.item.fileTitle,view:''}
            $("#superviseModal").modal("show");

        }

        //保存督办
        vm.saveSupervise = function () {
            fiveSuperviseFileService.getNewModelByType(vm.super.userLogin,vm.super.superviseType,vm.super.companyLeader,vm.super.businessId,vm.super.fileTitle,vm.super.view).then(function(value){
                if (value.data.ret){
                    toastr.success("创建督办任务成功!");
                    vm.loadData(true);
                }
            })
            $("#superviseModal").modal("hide");
        }

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();


        return vm;


    })

    //公文管理 报告文单
    .controller("FiveOaReportController", function ($state, $scope,$rootScope, fiveOaReportService) {
        var vm = this;

        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaReport";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaReportService.downTempleXls(params).then(function (response) {

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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaReportService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaReportDetail", {reportId: id});
        }

        vm.add = function () {
            fiveOaReportService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaReportService.remove(id, user.userLogin).then(function (value) {
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
                qName:vm.params.qName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin:user.userLogin,
                uiSref:uiSref
            });
            fiveOaReportService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'officeNo',placeholder:'请输入公司办编号..'},
                    2:{type:"input",colName:'reportContent',placeholder:'事项'},
                    3:{type:"input",colName:'deptName',placeholder:''},
                    4:{type:"input",colName:'chargeManName',placeholder:''},
                    5:{type:"input",colName:'reportTime',placeholder:''},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        vm.queryData();
        return vm;

    })
    .controller("FiveOaReportDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaReportService,fiveContentFileService) {
        var vm = this;

        var reportId = $stateParams.reportId;

        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,"five.oaReport");
            vm.loadData(true);
        }



        vm.loadData = function (loadProcess) {
            fiveOaReportService.getModelById(reportId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.loadOpinionProcessInstance(vm.item.processInstanceId);
                    }
                    $("#reportTime").datepicker('setDate', vm.item.reportTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaReportService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='companyOfficeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择办公室主任",type:"部门", userLoginList: vm.item.companyOfficeMan,multiple:false,deptIds:"59",parentDeptId:59});//公司办公室
            }else if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择批示领导",type:"部门", userLoginList: vm.item.companyLeader,multiple:true,deptIds:"16",parentDeptId:16});
            }else if (vm.status=='viceLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司副职领导人员", type:"部门",userLoginList: vm.item.viceLeader,multiple:true,deptIds:"16",parentDeptId:16});
            }else if (vm.status=='chargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择负责人",type:"部门", userLoginList: vm.item.chargeMan,multiple:true,deptIds:"16",parentDeptId:16});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='companyOfficeMan'){
                vm.item.companyOfficeMan = $scope.selectedOaUserLogins_;
                vm.item.companyOfficeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='viceLeader'){
                vm.item.viceLeader = $scope.selectedOaUserLogins_;
                vm.item.viceLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='chargeMan'){
                vm.item.chargeMan = $scope.selectedOaUserLogins_;
                vm.item.chargeManName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaReportService.update(vm.item).then(function (value) {
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
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId
                    +"",multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.print=function () {
            fiveOaReportService.getPrintData(reportId).then(function (value) {
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

        $scope.refresh=function(){
            vm.loadData(true);
        }

        return vm;

    })

    //公文管理-协同文件
    .controller("FiveOaFileSynergyController", function ($state, $scope,$rootScope, fiveOaFileSynergyService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaFileSynergy";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaFileSynergyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaFileSynergyDetail", {synergyId: id});
        }

        vm.add = function () {
            fiveOaFileSynergyService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaFileSynergyService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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
    .controller("FiveOaFileSynergyDetailController", function ($state,$stateParams,$rootScope,$scope,fiveContentFileService,fiveOaFileSynergyService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaFileSynergy";
        var synergyId = $stateParams.synergyId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.year=new Date().getFullYear();
            vm.selectword=" ";
            vm.selectyear=""+vm.year;
            vm.selectmark=0;
            $scope.loadRightData(user.userLogin,"five.oaFileSynergy");
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

        vm.loadData = function (loadProcess) {
            fiveOaFileSynergyService.getModelById(synergyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.loadContentFiles(vm.item.businessKey,true);
                    }
                    vm.loadDoc();
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaFileSynergyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("协同文件");
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
            vm.status=status;
            if (vm.status=='signer'){
                $scope.showOaSelectEmployeeModal_({title:"请选择会签人员", type:"部门",userLoginList: vm.item.signer,multiple:true,deptIds:"1",parentDeptId:user.deptId});//公司办公室
            }else if (vm.status=='chargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择呈阅领导人员",type:"部门", userLoginList: vm.item.companyLeader,multiple:true,deptIds:"16",parentDeptId:16});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='signer'){
                vm.item.signer = $scope.selectedOaUserLogins_;
                vm.item.signerName = $scope.selectedOaUserNames_;
            }else if (vm.status=='chargeMen'){
                vm.item.chargeMen = $scope.selectedOaUserLogins_;
                vm.item.chargeMenName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if (vm.item.security=='是'){
                toastr.warning("注：此平台为非密平台，严禁填写涉密信息!")
                return false;
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaFileSynergyService.update(vm.item).then(function (value) {
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
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            if(vm.opt=="deptId"){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请单位",type:"选部门", deptIdList: vm.item.deptId ,
                    multiple:false,deptIds:"1",parentDeptId:vm.item.deptId});
            }else{
                $scope.showOaSelectEmployeeModal_({title:"请选择承办单位",type:"选部门", deptIdList: vm.item.undertakeDeptId ,
                    multiple:true,deptIds:"1",parentDeptId:vm.item.deptId});
            }

        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt=="deptId"){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = $scope.selectedOaDeptIds_;
            }else{
                vm.item.undertakeDeptName = $scope.selectedOaDeptNames_;
                vm.item.undertakeDeptId = $scope.selectedOaDeptIds_;
            }

        }

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();


        return vm;


    })

    // 公文管理 公司发文
    .controller("FiveOaDispatchController", function ($rootScope,$state, $scope, fiveOaDispatchService,commonCodeService) {
        var vm = this;
        vm.params = {dispatchType: "", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        var uiSref="five.oaDispatch";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaDispatchService.downTempleXls(params).then(function (response) {

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

            commonCodeService.listDataByCatalog(user.userLogin,"发文类型").then(function (value) {
                if (value.data.ret){
                    vm.listType=value.data.data;
                    vm.listType.push({name:"全部",code:""});
                }
            })

            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.loadPagedData = function () {

            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,
                dispatchType:vm.params.dispatchType,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin:user.userLogin,
                uiSref:uiSref});

            fiveOaDispatchService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaDispatchDetail", {dispatchId: id});
        };

        vm.add = function () {
            fiveOaDispatchService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaDispatchService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.queryData();
        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,
                dispatchType:vm.params.dispatchType,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin:user.userLogin,
                uiSref:uiSref
            });
            fiveOaDispatchService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'dispatchTitle',placeholder:'请输入标题..'},
                    2:{type:"select",colName:'dispatchType',placeholder:'',option:[{"title":"全部","value":""},{"title":"中国五洲工程设计集团文件","value":"中国五洲工程设计集团文件"},{"title":"中国五洲工程设计集团保密委员会会议纪要","value":"中国五洲工程设计集团保密委员会会议纪要"},
                            {"title":"中国五洲工程设计集团工会简报","value":"中国五洲工程设计集团工会简报"},{"title":"中国五洲工程设计集团情况通报","value":"中国五洲工程设计集团情况通报"},{"title":"中共中国五洲工程设计集团纪律检查委员会文件","value":"中共中国五洲工程设计集团纪律检查委员会文件"},
                            {"title":"中共中国五洲工程设计集团委员会文件","value":"中共中国五洲工程设计集团委员会文件"}, {"title":"中国五洲工程设计集团工会委员会文件","value":"中国五洲工程设计集团工会委员会文件"},{"title":"中国五洲工程设计集团会议纪要","value":"中国五洲工程设计集团会议纪要"},
                            {"title":"中国五洲工程设计有限公司监事会文件","value":"中国五洲工程设计有限公司监事会文件"},{"title":"中国五洲工程设计集团简报","value":"中国五洲工程设计集团简报"},{"title":"共青团中国五洲工程设计集团委员会文件","value":"共青团中国五洲工程设计集团委员会文件"},
                            {"title":"中国五洲工程设计集团董事会文件","value":"中国五洲工程设计集团董事会文件"}]},
                    3:{colName:'deptName',placeholder:'请输入主办单位..'},
                    4:{type:"input",colName:'signerName'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    },handleColumn:7};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;

    })
    .controller("FiveOaDispatchDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaDispatchService,hrDeptService,fiveContentFileService,fiveOaWordSizeService,oaNoticeService) {
        var vm = this;
        var uiSref="five.oaDispatch";
        var dispatchId = $stateParams.dispatchId;
        // var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.year=new Date().getFullYear();
            vm.selectword=" ";
            vm.selectyear=""+vm.year;
            vm.selectmark=0;
            vm.showDis=true;
            $rootScope.loadRightData(user.userLogin,"five.oaDispatch");
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

        vm.loadData = function (loadProcess) {
            fiveOaDispatchService.getModelById(dispatchId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (vm.item.mark!=0){
                        vm.selectword= vm.item.word;
                        vm.selectyear= vm.item.year;
                        vm.selectmark=vm.item.mark;
                    }
                    if (loadProcess) {
                        $rootScope.loadOpinionProcessInstance(vm.item.processInstanceId);
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.loadContentFiles(vm.item.businessKey,true);
                        $rootScope.loadActRelevance(vm.item.businessKey,"fiveOaSignQuote","five.oaSignQuote",false);
                    }
                    vm.loadDoc();
                    vm.loadWordSize(vm.year);
                    $("#allottedTime").datepicker('setDate', vm.item.allottedTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaDispatchService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        $scope.showSendTask=function(send){
            //查询是否有发文正文
            fiveContentFileService.getModelByBusinessKey(vm.item.businessKey,0).then(function (value) {
                if (value.data.ret){
                    vm.redHead=value.data.data;
                    //正文不存在提示
                    if (vm.redHead.redhead==null){
                        toastr.warning("请上传发文正文word!")
                        return ;
                    }
                    //表单验证  流程发送
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        fiveOaDispatchService.update(vm.item).then(function (value) {
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

                    }else {
                        toastr.warning("请准确填写数据!")
                        return false;
                    }
                }
            })

        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='signer'){
                $scope.showOaSelectEmployeeModal_({title:"请选择签发人员", type:"部门",userLoginList: vm.item.signer,multiple:true,deptIds:"16",parentDeptId:16});
            }else if (vm.status=='countersign'){
                $scope.showOaSelectEmployeeModal_({title:"请选择会签人员",type:"部门" ,userLoginList: vm.item.countersign,multiple:true,deptIds:"1",parentDeptId:1});
            }else if (vm.status=='companyOffice'){
                hrDeptService.getModelById(59).then(function(value){
                    vm.hrdept=value.data.data;
                })
                $scope.showOaSelectEmployeeModal_({title:"请选择公司办人员", type:"人员",userLoginList: vm.item.companyOffice,userLogins:vm.hrdept.deptFirstLeader,multiple:false});
            }else if (vm.status=='companySecurity'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司保密委员会",type:"角色" ,userLoginList: vm.item.companySecurity,multiple:false,roleIds:"62,63,64,65",parentRoleId:"62"});//公司办公室
            }else if (vm.status=='copyMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择抄送人员",type:"领导" ,userLoginList: vm.item.copyMen,multiple:true,deptIds:"1",parentRoleId:"16"});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='signer'){
                vm.item.signer = $scope.selectedOaUserLogins_;
                vm.item.signerName = $scope.selectedOaUserNames_;
            }else if (vm.status=='countersign'){
                vm.item.countersign = $scope.selectedOaUserLogins_;
                vm.item.countersignName = $scope.selectedOaUserNames_;
            }else if (vm.status=='companyOffice'){
                vm.item.companyOffice = $scope.selectedOaUserLogins_;
                vm.item.companyOfficeName = $scope.selectedOaUserNames_;
            }else if (vm.status=='companySecurity'){
                vm.item.companySecurity = $scope.selectedOaUserLogins_;
                vm.item.companySecurityName = $scope.selectedOaUserNames_;
            }else if (vm.status=='copyMen'){
                vm.item.copyMen = $scope.selectedOaUserLogins_;
                vm.item.copyMenName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal=function(deptStatus) {
            vm.deptStatus=deptStatus;
            if (vm.deptStatus=='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择承办单位",type:"选部门", deptIdList: vm.item.deptId,multiple:false,deptIds:"1",parentDeptId:2});
            }else if (vm.deptStatus=='realSendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择主送部门",type:"选部门", deptIdList: 0,multiple:true,deptIds:"1",parentDeptId:2});
            }else if (vm.deptStatus=='copySendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择抄送部门",type:"选部门", deptIdList:0,multiple:true,deptIds:"1",parentDeptId:2 });
            }
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptStatus=='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = $scope.selectedOaDeptIds_;
            }else if (vm.deptStatus=='realSendMan'){
                if (vm.item.realSendManName!=undefined&&vm.item.realSendManName!=""){
                    vm.item.realSendManName = vm.item.realSendManName+","+ $scope.selectedOaDeptNames_;
                }else {
                    vm.item.realSendManName =$scope.selectedOaDeptNames_;
                }
            }else if (vm.deptStatus=='copySendMan'){
                if ( vm.item.copySendManName!=undefined&&vm.item.copySendManName!=""){
                    vm.item.copySendManName = vm.item.copySendManName+","+ $scope.selectedOaDeptNames_;
                }else {
                    vm.item.copySendManName =  $scope.selectedOaDeptNames_;
                }
            }
        }

        vm.changeWord=function(size){
            if (size<3){
                fiveOaWordSizeService.getMarkByChange(vm.selectword,vm.selectyear).then(function(value){
                    if (value.data.ret){
                        vm.selectmark=value.data.data.mark ;
                    }
                })
            }else {
            }
        }

        vm.loadWordSize=function(year){
            var key=vm.item.deptId+"_"+vm.item.deptName+","+"0_"+vm.item.dispatchType;
            fiveOaWordSizeService.getCanUseWord(key,year,"公司发文").then(function (value) {
                if (value.data.ret){
                    vm.wordSizeList=value.data.data;
                    if (vm.wordSizeList.length==1&&user.userLogin=='2766'){
                        vm.showDis=false;
                    }
                }
            })

        }

        vm.loadHistory=function(key){
            fiveOaWordSizeService.listUserWord(vm.selectword,vm.selectyear,key).then(function (value) {
                if (value.data.ret){
                    vm.wordHistoryList=value.data.data;
                    $("#wordSizeModel").modal("show");
                }
            })
        }
        /*文号*/
        vm.updateWordSize=function(){
            var params={selectWord:vm.selectword,selectYear:vm.selectyear,selectMark:vm.selectmark,businessKey:vm.item.businessKey,userLogin:user.userLogin}
            var message="您确定要申请该文号吗?请谨慎操作!";
            if(vm.item.mark!=0){
                message="您确定要申请该文号吗?已有文号："+vm.item.dispatch+",请谨慎操作!";
            }
            bootbox.confirm(message, function (result) {
                if (result) {
                    fiveOaWordSizeService.updateWordSize(params).then(function(value){
                        if (value.data.ret){
                            if (vm.wordsizeId!=0){
                                toastr.success("文号保存更新!");
                                vm.item.dispatch=vm.selectword+"〔"+vm.selectyear+"〕"+vm.selectmark+'号';
                                vm.item.word=vm.selectword;
                                vm.item.year=vm.selectyear;
                                vm.item.mark=vm.selectmark;
                                vm.save();
                            }
                        }
                    })
                }
            })

        }

        /*转新闻*/
        vm.notice={types:"文件简报",noticeLevel:"",noticeSystemType:""};
        vm.showSystemNotice=function(){
            vm.notice.types="公司制度";
            $("#planTypeListModal").modal("show");
        }
        vm.goNotice=function(){
            if (vm.item.processEnd==1&&vm.item.deleted==0){
                var types=vm.notice.types+","+user.deptName;
                oaNoticeService.getNewModelByDispatch(vm.item.businessKey,types,vm.notice.noticeLevel,vm.notice.noticeSystemType).then(function(value){
                    if (value.data.ret){
                        toastr.success("公司发文自动转入"+types+"完成!请到信息管理查看");
                    }
                })
            }
            $("#planTypeListModal").modal("hide");
        }
        vm.print=function () {
            fiveOaDispatchService.getPrintData(dispatchId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    setTimeout(function () {
                        lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                        var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_STYLEA(0,"HtmWaitMilSecs",500);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();

        return vm;

    })

    //公文管理 部门发文单
    .controller("FiveOaDepartmentPostController", function ($state, $scope,$rootScope, fiveOaDepartmentPostService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "部门发文单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaDepartmentPost";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
        };

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime:vm.params.startTime,endTime:vm.params.endTime
            });
            fiveOaDepartmentPostService.downTempleXls(params).then(function (response) {

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
            var params = $.extend(tablefilter.params,{
                dispatchType:vm.params.dispatchType,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin:user.userLogin,
                uiSref:uiSref,
                qName:vm.params.qName,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1});
            fiveOaDepartmentPostService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaDepartmentPostDetail", {departmentPostId: id});
        }


        vm.add = function () {
            fiveOaDepartmentPostService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaDepartmentPostService.remove(id, user.userLogin).then(function (value) {
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
                dispatchType:vm.params.dispatchType,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize,
                userLogin:user.userLogin,
                uiSref:uiSref,
                qName:vm.params.qName
            });
            fiveOaDepartmentPostService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'title',placeholder:'请输标题..'},
                    2:{type:"input",colName:'deptName',placeholder:'主办单位'},
                    3:{type:"input",colName:'drafterName',placeholder:'拟稿人'},
                    4:{type:"input",colName:'checkManName'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();
        vm.init();
        return vm;

    })
    .controller("FiveOaDepartmentPostDetailController", function ($state,$stateParams,$rootScope,$scope,fiveContentFileService ,fiveOaDepartmentPostService,fiveOaWordSizeService,oaNoticeService) {
        var vm = this;
        var departmentPostId = $stateParams.departmentPostId;
        var uiSref="five.oaDepartmentPost";
        var tableName = $rootScope.loadTableName("five.oaDepartmentPost");

        vm.init=function(){
            vm.year=new Date().getFullYear();
            vm.selectword=" ";
            vm.selectyear=""+vm.year;
            vm.selectmark=0;
            vm.showDis=true;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (

        ) {
            fiveOaDepartmentPostService.getModelById(departmentPostId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $rootScope.loadOpinionProcessInstance(vm.item.processInstanceId);
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        vm.loadWordSize(vm.year);
                        $rootScope.loadContentFiles(vm.item.businessKey,true);

                    }
                    vm.loadDoc();
                }
            })
        };

        /*加载 红头文件和文档*/
        vm.loadDoc=function(){
            fiveContentFileService.getModelByBusinessKey(vm.item.businessKey,0).then(function (value) {
                if (value.data.ret){
                    vm.redHead=value.data.data;
                }
            })
        }

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaDepartmentPostService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='checkMan'){
                $rootScope.rightData.selectDepts;
                $scope.showOaSelectEmployeeModal_({title:"请选择核稿人", userLoginList: vm.item.checkMan,multiple:false,type:"部门",deptIds:"1",parentDeptId:2});
            }else if (vm.status=='drafter'){
                $scope.showOaSelectEmployeeModal_({title:"请选择拟稿人", userLoginList: vm.item.drafter,type:"部门",multiple:false,deptIds:"1",parentDeptId:user.deptId});
            }else if (vm.status=='realSendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择主送人员", userLoginList: vm.item.realSendMan,type:"部门",multiple:true,deptIds:"1",parentDeptId:user.deptId});
            }else if (vm.status=='copySendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择抄送人员", userLoginList: vm.item.copySendMan,type:"部门",multiple:true,deptIds:"1",parentDeptId:user.deptId});
            }else if (vm.status=='typer'){
                $scope.showOaSelectEmployeeModal_({title:"请选择打字员人员", userLoginList: vm.item.typer,type:"部门",multiple:false,deptIds:"1",parentDeptId:user.deptId});
            }else if (vm.status=='proofreader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择校对人员", userLoginList: vm.item.proofreadMan,type:"部门",multiple:false,deptIds:"1",parentDeptId:user.deptId});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='checkMan'){
                vm.item.checkMan = $scope.selectedOaUserLogins_;
                vm.item.checkManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='drafter'){
                vm.item.drafter = $scope.selectedOaUserLogins_;
                vm.item.drafterName = $scope.selectedOaUserNames_;
            }else if (vm.status=='realSendMan'){
                vm.item.realSendMan = $scope.selectedOaUserLogins_;
                vm.item.realSendManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='copySendMan'){
                vm.item.copySendMan = $scope.selectedOaUserLogins_;
                vm.item.copySendManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='typer'){
                vm.item.typer = $scope.selectedOaUserLogins_;
                vm.item.typerName = $scope.selectedOaUserNames_;
            }else if (vm.status=='proofreader'){
                vm.item.proofreadMan = $scope.selectedOaUserLogins_;
                vm.item.proofreadManName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            //查询是否有发文正文
            fiveContentFileService.getModelByBusinessKey(vm.item.businessKey,0).then(function (value) {
                if (value.data.ret){
                    vm.redHead=value.data.data;
                    //正文不存在提示
                    if (vm.redHead.redhead==null){
                        toastr.warning("请上传发文正文word!")
                        return ;
                    }
                    //表单验证  流程发送
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        fiveOaDepartmentPostService.update(vm.item).then(function (value) {
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

                    }else {
                        toastr.warning("请准确填写数据!")
                        return false;
                    }
                }
            })
        }

        vm.showDeptModal=function(deptStatus) {
            vm.deptStatus=deptStatus;
            if (vm.deptStatus=='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择承办单位",type:"选部门", deptIdList: vm.item.deptId,multiple:false,deptIds:"1",parentDeptId:2});
            }else if (vm.deptStatus=='realSendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择主送部门",type:"选部门", deptIdList: vm.item.realSendMan,multiple:true,deptIds:"1",parentDeptId:2});
            }else if (vm.deptStatus=='copySendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择抄送部门",type:"选部门", deptIdList: vm.item.copySendMan,multiple:true,deptIds:"1",parentDeptId:2 });
            }
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptStatus=='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = $scope.selectedOaDeptIds_;
            }else if (vm.deptStatus=='realSendMan'){
                vm.item.realSendManName = $scope.selectedOaDeptNames_;
                vm.item.realSendMan = $scope.selectedOaDeptIds_;
            }else if (vm.deptStatus=='copySendMan'){
                vm.item.copySendManName = $scope.selectedOaDeptNames_;
                vm.item.copySendMan = $scope.selectedOaDeptIds_;
            }
        }

        vm.changeWord=function(size){
            if (size<3){
                fiveOaWordSizeService.getMarkByChange(vm.selectword,vm.selectyear).then(function(value){
                    if (value.data.ret){
                        vm.selectmark=value.data.data.mark ;
                    }
                })
            }
        }

        vm.loadWordSize=function(year){
            var key=vm.item.deptId+"_"+vm.item.deptName;
            fiveOaWordSizeService.getCanUseWord(key,year,"部门发文").then(function (value) {
                if (value.data.ret){
                    vm.wordSizeList=value.data.data;
                    if (vm.wordSizeList.length==1){
                        vm.showDis=false;
                    }
                }
            })
        }

        vm.loadHistory=function(key){
            fiveOaWordSizeService.listUserWord(vm.selectword,vm.selectyear,key).then(function (value) {
                if (value.data.ret){
                    vm.wordHistoryList=value.data.data;
                    $("#wordSizeModel").modal("show");
                }
            })
        }

        vm.saveWordSize=function(){
            $(".cb_wordsize:checked").each(function () {
                var value = $.parseJSON($(this).attr("data-name"));
                vm.item.dispatch=value.word+"〔"+value.year+"〕"+value.mark+"号";
                vm.wordsizeId=value.id;
            })
            $("#wordSizeModel").modal("hide");
        }

        vm.updateWordSize=function(){
            var params={selectWord:vm.selectword,selectYear:vm.selectyear,selectMark:vm.selectmark,businessKey:vm.item.businessKey,userLogin:user.userLogin}
            var message="您确定要申请该文号吗?请谨慎操作!";
            if(vm.item.dispatch!=""){
                message="您确定要申请该文号吗?已有文号："+vm.item.dispatch+",请谨慎操作!";
            }
            bootbox.confirm(message, function (result) {
                if (result) {
                    fiveOaWordSizeService.updateWordSize(params).then(function(value){
                        if (value.data.ret){
                            if (vm.wordsizeId!=0){
                                toastr.success("文号保存更新!");
                                vm.item.dispatch=vm.selectword+"〔"+vm.selectyear+"〕"+vm.selectmark+'号';
                                vm.item.word=vm.selectword;
                                vm.item.year=vm.selectyear;
                                vm.item.mark=vm.selectmark;
                                vm.save();
                            }
                        }
                    })
                }
            })

        }

        /*转新闻*/
        vm.notice={types:"",noticeLevel:"",noticeSystemType:""};

        vm.goNotice=function(){
            //流程已完成 自动转入 对应部门空间 通知公告
            if (vm.item.processEnd==1&&vm.item.deleted==0){
                var types='通知公告,'+user.deptName;
                oaNoticeService.getNewModelByDispatch(vm.item.businessKey,types,vm.notice.noticeLevel,vm.notice.noticeSystemType).then(function(value){
                    if (value.data.ret){
                        toastr.success("部门发文自动转入"+types+"完成!请到信息管理查看");
                    }
                })
            }

        }

        vm.print=function () {
            fiveOaDepartmentPostService.getPrintData(departmentPostId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var str_BodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =str_BodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();


        return vm;

    })

    //公文管理 信息发布
    .controller("OaNoticeApplyController", function ($state, $scope,$rootScope, oaNoticeApplyService,commonCodeService) {
        var vm = this;
        var uiSref="oa.noticeApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.params = {noticeTitles:"",pageNum: 1, pageSize: $scope.pageSize,total:999};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.init=function(){
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        }

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,noticeTypes:vm.params.noticeTypes,uiSref:uiSref,userLogin:user.userLogin,
                noticeTitles:vm.params.noticeTitles};
            oaNoticeApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("oa.noticeApplyDetail",{id: id});
        }

        vm.add = function () {
            oaNoticeApplyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaNoticeApplyService.remove(id,user.userLogin).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            })
        }

        vm.listCommonType = function () {
            var params = {pageNum: 1, pageSize: 99,
                codeCatalog:user.deptName+"_电子公告"};
            commonCodeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.commonList = value.data.data.list;
                    $("#commonTypeModal").modal("show");
                }
            })
        };

        vm.showCommonType = function (id) {
            commonCodeService.getModelById(id).then(function (value) {
                vm.commonType=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        vm.addCommonType= function () {
            commonCodeService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.commonType=value.data.data;
                    vm.commonType.codeCatalog=user.deptName+"_电子公告";
                    $("#itemModal").modal("show");
                }
            })
        }

        vm.saveCommonType=function(){
            vm.commonType.code=vm.commonType.name;
            vm.commonType.defaulted=0;

            commonCodeService.save(vm.commonType).then(function (value) {
                if(value.data.ret) {
                    $("#itemModal").modal("hide");
                    vm.listCommonType();
                    toastr.success("保存成功");
                }
            })
        }

        vm.removeCommonType=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    commonCodeService.remove(id).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功!")
                            vm.listCommonType();
                        }
                    });
                }
            })
        }

        vm.fuzzySearch = function (params) {
            oaNoticeApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'noticeTitle',placeholder:'请输信息标题..'},
                    2:{type:"input",colName:'deptName',placeholder:'请输发布部门..'},
                    3:{type:"input",colName:'noticeLevel',placeholder:'公告类别'},
                    4:{type:"input",colName:'publishUserName'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已发布",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.init();

        return vm;

    })
    .controller("OaNoticeApplyDetailController", function ($state,$stateParams,$rootScope,$scope,$sce, sysRoleService,oaNoticeApplyService,myActService ,commonCodeService,commonPrintTableService) {
        var vm = this;
        var id = $stateParams.id;
        var uiSref="oa.noticeApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.showType=false;//是否显示子分类
        vm.showHtml=false;

        vm.init=function(){

            vm.loadData(true);

            vm.loadFile();

            //水印 HNZ发布信息暂时不加水印
            setTimeout(function(){
                __canvasWM({
                    content: user.userName + ' ' + user.userLogin
                });},0);

        }
        /*加载上传控件*/
        vm.loadFile=function(){
            $("#btnUploadWord").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator:user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star=  file.name.lastIndexOf(".");
                    var  fileType=file.name.substring(star,file.name.length);
                    if(".doc,.docx,.pdf,.xls,.xlsx,.ppt,.pptx".indexOf(fileType)==-1){
                        toastr.error('请上传.doc,.docx,.pdf,.xls,.xlsx,.ppt,.pptx格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.attachIds = result.data.id;
                        vm.item.attachName=data.files[0].name;
                        vm.save();
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });

            $("#btnUploadPhoto").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator:user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star=  file.name.lastIndexOf(".");
                    var  fileType=file.name.substring(star,file.name.length);
                    if(".png,.jpg,.bmp,.tif,.jpge".indexOf(fileType)==-1){
                        toastr.error('请上传.png,.jpg,.bmp,.tif,.jpge 格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.photoAttachId = result.data.id;
                        vm.item.photoAttachName=data.files[0].name;
                        vm.save();
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        /*判断发起人所属的的权限 板块权限*/
        vm.showTypeList=function(){
            vm.listType=[{name:'通知公告'},
                {name:'文件简报'}];

            sysRoleService.getAclInfoByUserLogin(user.userLogin,"oa.noticeParty").then(function (value) {
                vm.rightData=value.data.data;
                if (vm.rightData.add){
                    vm.listType.push( {name:'公司新闻'});
                }
            })
            sysRoleService.getAclInfoByUserLogin(user.userLogin,"oa.noticeGroup").then(function (value) {
                vm.rightData=value.data.data;
                if (vm.rightData.add){
                    vm.listType.push({name:'集团制度'});
                }
            })
            sysRoleService.getAclInfoByUserLogin(user.userLogin,"oa.noticeCompany").then(function (value) {
                vm.rightData=value.data.data;
                if (vm.rightData.add){
                    vm.listType.push({name:'公司制度'});
                }
            })
            sysRoleService.getAclInfoByUserLogin(user.userLogin,"oa.noticePhoto").then(function (value) {
                vm.rightData=value.data.data;
                if (vm.rightData.add){
                    vm.listType.push({name:'图片新闻'});
                }
            })
            vm.listType.push({name:vm.item.deptName});
        }

        vm.change=function(noticeType){
            if ("公司新闻,通知公告,文件简报,集团制度,图片新闻".indexOf(noticeType)>-1){
                vm.showType=false;
                vm.item.noticeLevel=vm.item.noticeType;
            }else {
                vm.showType=true;
                commonCodeService.listDataByCatalog(user.userLogin,noticeType+'_电子公告').then(function (value) {
                    if (value.data.ret){
                        vm.listCode=value.data.data;
                    }
                })
            }

            if (vm.item.attachIds!=''){
                vm.showHtml=true;
            }

        }

        vm.loadData = function (loadProcess) {
            oaNoticeApplyService.getModelById(id,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        vm.showTypeList();
                    }
                    vm.change(vm.item.noticeType);
                    vm.hrefOWA="https://owa.wuzhou.com.cn/op/embed.aspx?src="+encodeURIComponent("https://co.wuzhou.com.cn/common/attach/download/"+vm.item.attachIds);
                    $sce.trustAsResourceUrl(vm.hrefOWA);

                }
            })

        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;

            oaNoticeApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("协同文件");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                if ( vm.item.noticeType.indexOf("图片新闻")>=0){
                    if (item.getPhotoAttachId()==""){
                        Assert.state(false,"请上传一张新闻图片！！！");
                    }
                }
                oaNoticeApplyService.update(vm.item).then(function (value) {
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
                            vm.loadData();
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
            if (vm.status=='viewMans'){
                $scope.showOaSelectEmployeeModal_({ title: "请选择发布范围", type: '选部门', deptIds: "1", deptIdList: vm.item.viewMans+"", multiple: true,deptIds:"1",parentDeptId:1});

            }
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='viewMans'){
                vm.item.viewMans = ','+$scope.selectedOaDeptIds_+',';
                vm.item.viewMansName = $scope.selectedOaDeptNames_;
            }
        };

        vm.init();

        $scope.refresh=function(){
            vm.loadData(true);
        }

        return vm;
    })


    //会议管理 会议室申请
    .controller("FiveOaMeetingRoomApplyController", function ($state, $scope,$rootScope, fiveOaMeetingRoomService,fiveOaMeetingRoomApplyService) {
    var vm = this;
    vm.params = {name: "", qName: ""};
    vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
    var uiSref="five.oa.meetingRoomApply";
    var tableName = $rootScope.loadTableName(uiSref);

    vm.init = function () {
        $scope.basicInit();
        vm.now = vm.dateFormat(new Date(),'yyyy-MM-dd');
        $("#endDate").datepicker('setDate', vm.now);
        vm.loadData();
    };

    vm.loadData = function () {

        fiveOaMeetingRoomService.listRecentBookData("",vm.now).then(function(value){
            if (value.data.ret){
                vm.list=value.data.data;
            }
        })
    };
    vm.dateFormat = function (date,fmt) {
        var o = {
            "M+": date.getMonth() + 1, //月份
            "d+": date.getDate(), //日
            "h+": date.getHours(), //小时
            "m+": date.getMinutes(), //分
            "s+": date.getSeconds(), //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    /* vm.show = function (id) {
         $state.go("five.oaMeetingRoomApplyDetail",{id: id});
     }*/

    vm.add = function () {
        bootbox.alert("请直接在企业微信中提交会议室申请！");
    }

    vm.remove=function(id) {
        bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
            if(result){
                fiveOaMeetingRoomApplyService.remove(id,user.userLogin).then(function (value) {
                    toastr.success("删除成功!")
                    vm.loadPagedData();
                });
            }
        })
    }

    vm.init();

    return vm;

})
    .controller("FiveOaMeetingRoomApplyDetailController", function ($rootScope,$state,$sce,$stateParams ,$scope, fiveOaMeetingRoomApplyService,fiveOaMeetingRoomService) {
        var vm = this;
        var id = $stateParams.id;
        $scope.loadRightData(user.userLogin,"oa.meetingRoomApply");
        var tableName = $rootScope.loadTableName(uiSref);

        vm.loadData = function () {
            fiveOaMeetingRoomApplyService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);
                    $("#applyDay").datepicker('setDate', new Date());
                    if(vm.item.beginTime!=""){
                        vm.applyDay = vm.dateFormat(new Date(vm.item.beginTime),"yyyy-MM-dd");
                    }
                    //显示不可选时间
                    vm.getApplyTime();
                }
            })
            fiveOaMeetingRoomService.listRoomStatusByDay(id).then(function (value) {
                if (value.data.ret) {
                    vm.roomlist = value.data.data;
                }
            });
        };

        vm.selectTime=function(time,event){
            if(vm.item.creator!=user.userLogin||!$rootScope.processInstance.firstTask){
                return;
            }
            var nowTd =event.target;
            var nowTr = nowTd.parentNode;
            //渲染节点的位置
            var oldTdIndex;
            //点击前是否有绿色
            var preflag =false;
            //点击后是否有绿色
            var flag =false;
            //点击如果为不可选用 返回
            if(nowTd.bgColor=='#ff0000'||nowTd.bgColor=='#A5A6B2'){
                return;
            }
            var roomTable = document.getElementById("roomTable");
            for (var i = 1; i < roomTable.rows.length; i++) {
                var tr = roomTable.rows[i];
                for (var j = 1; j < roomTable.rows[i].cells.length; j++) {
                    var td = roomTable.rows[i].cells[j]
                    //如果点击的其他行有绿色 清空
                    if(tr.rowIndex!=nowTr.rowIndex&& td.bgColor == "#0bb20c"){
                        td.bgColor ='';
                    }else{
                        //点击前 有绿色 记录前面的第一个绿色
                        if(nowTd.cellIndex>td.cellIndex&& td.bgColor == "#0bb20c"&&!preflag){
                            oldTdIndex =td.cellIndex;
                            preflag =true;
                        }
                    }
                }
            }
            //如果前没有 才去找后面的
            if(!preflag){
                for (var j = nowTr.cells.length-1; j >1; j--) {
                    var td = nowTr.cells[j];
                    //点击后 有绿色 记录后面的最后一个绿色
                    if(nowTd.cellIndex<td.cellIndex-1&& td.bgColor == "#0bb20c"&&!flag){
                        oldTdIndex =td.cellIndex;
                        flag=true;
                    }
                }
            }
            //如果点击前有
            if(preflag){
                //如果 中间有红色 如果有 取红色后一个为oldIndex
                for (var j = nowTr.cells.length-1; j >1; j--) {
                    var td = nowTr.cells[j];
                    if(td.cellIndex>=oldTdIndex&&td.cellIndex<=nowTd.cellIndex&&td.bgColor == "#ff0000"){
                        oldTdIndex =td.cellIndex+1;
                        toastr.error("选择时间段内 有已申请的时间段 请重新选择");
                    }
                }
                //渲染
                for (var j = 1; j < nowTr.cells.length; j++) {
                    var td = nowTr.cells[j];
                    if(td.cellIndex>=oldTdIndex&&td.cellIndex<=nowTd.cellIndex){
                        td.bgColor = "#0bb20c";
                    }else{
                        if( td.bgColor != "#ff0000" && td.bgColor != "#A5A6B2"){
                            td.bgColor = "";
                        }
                    }
                }
            }
            //如果点击后有
            if(!preflag&&flag){
                //如果 中间有红色 如果有 取红色前一个为oldIndex
                for (var j = 1; j <nowTr.cells.length; j++) {
                    var td = nowTr.cells[j];
                    if(td.cellIndex>=nowTd.cellIndex&&td.cellIndex<=oldTdIndex&&td.bgColor == "#ff0000"){
                        oldTdIndex =td.cellIndex-1;
                        toastr.error("选择时间段内 有已申请的时间段 请重新选择");
                    }
                }
                //渲染
                for (var j = 1; j < nowTr.cells.length; j++) {
                    var td = nowTr.cells[j];
                    if(td.cellIndex>=nowTd.cellIndex&&td.cellIndex<=oldTdIndex){
                        td.bgColor = "#0bb20c";
                    }else{
                        if( td.bgColor != "#ff0000" && td.bgColor != "#A5A6B2"){
                            td.bgColor = "";
                        }
                    }
                }
            }
            //如果都没有
            if(!preflag&&!flag){
                if(nowTd.bgColor=='') {
                    nowTd.bgColor = "#0bb20c";
                }else{
                    nowTd.bgColor ='';
                }
            }
            //跟新表单所选会议室
            vm.item.meetingRoomId = nowTr.attributes.roomid.nodeValue;
            vm.item.meetingRoomName = nowTr.attributes.roomname.nodeValue;
            //跟新时间
            var times = [];
            for (var j = 1; j < nowTr.cells.length; j++) {
                var td = nowTr.cells[j];
                if(td.bgColor == "#0bb20c"){
                    times.push(td.attributes.datatime.nodeValue)
                }
            }
            vm.item.beginTime = vm.dateFormat(new Date(parseFloat(times[0])),"yyyy-MM-dd hh:mm");
            vm.item.endTime = vm.dateFormat(new Date(parseFloat(parseFloat(times[times.length-1])+30*60*1000+"")),"yyyy-MM-dd hh:mm");
        }

        vm.dateFormat = function (date,fmt) {
            var o = {
                "M+": date.getMonth() + 1, //月份
                "d+": date.getDate(), //日
                "h+": date.getHours(), //小时
                "m+": date.getMinutes(), //分
                "s+": date.getSeconds(), //秒
                "q+": Math.floor((date.getMonth() + 3) / 3), //季度
                "S": date.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }

        vm.getApplyTime=function(){
            //显示所有时间
            vm.allTime=[];
            //8-23点 每30分钟一个节点
            for(var i=8;i<=23;i++){
                for(var j=0;j<60;j=j+30){
                    var day = new Date(vm.applyDay);
                    day.setHours(i);
                    day.setMinutes(j);
                    day.setSeconds(0);
                    vm.allTime.push(day.getTime());
                }
            }
            setTimeout(function() {
                var roomTable = document.getElementById("roomTable");
                for (var i = 1; i < roomTable.rows.length; i++) {
                    for (var j = 1; j < roomTable.rows[i].cells.length; j++) {
                        var td = roomTable.rows[i].cells[j];
                        var time = td.attributes.datatime.nodeValue;
                        //遍历 roomlist的 已申请时间
                        for (var k = 0; k < vm.roomlist.length; k++) {
                            var usedTimes = vm.roomlist[k].usedTimes;
                            for (var l = 0; l < usedTimes.length; l++) {
                                var beginTime = usedTimes[l].beginTime;
                                var endTime = usedTimes[l].endTime;
                                var nowApply = usedTimes[l].nowApply;
                                //判断行
                                if(roomTable.rows[i].attributes.roomid.nodeValue == vm.roomlist[k].id) {
                                    if (time >= beginTime && time < endTime) {
                                        if(nowApply){
                                            td.bgColor = "#0bb20c";
                                        }else{
                                            td.bgColor = "#ff0000";
                                        }
                                    }
                                }
                            }
                            //判断是否在当前时间之前
                            if(time<new Date().getTime()){
                                td.bgColor = "#A5A6B2";
                            }
                            //判断会议室状态
                            if(roomTable.rows[i].attributes.roomid.nodeValue==vm.roomlist[k].id&&vm.roomlist[k].roomStatus=="维修中"){
                                if(td.bgColor !="#ff0000"){
                                    td.bgColor = "#A5A6B2";
                                }
                            }
                        }
                    }
                }
            },500);
        }

        vm.showDetail = function (detail) {
            bootbox.prompt(
                {
                    title: "请选择该申请 的申请状态",
                    value: detail.applyState,
                    inputType: 'select',
                    inputOptions: [
                        {
                            text: '申请生效',
                            value: '申请生效',
                        },
                        {
                            text: '申请未生效',
                            value: '申请未生效',
                        },
                    ],

                    callback: function (result) {
                        if (result) {
                            detail.applyState = result;
                            detail.operateUserLogin = user.userLogin;
                            fiveOaMeetingRoomApplyService.changeType(detail).then(function () {
                                vm.loadData();
                            });
                        }
                    }
                });
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='hostMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择会议主持人", type:"部门",userLoginList: vm.item.hostMan,multiple:true,deptIds:user.deptId,parentDeptId:user.deptId});
            }else if (vm.status=='chargeLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择参会领导", type:"部门",userLoginList: vm.item.chargeLeader,deptIds:user.deptId,parentDeptId:user.deptId,multiple:true});
            }else if (vm.status=='attendUser'){
                $scope.showOaSelectEmployeeModal_({title:"请选择参会人员", type:"部门",userLoginList: vm.item.attendUser,multiple:true,deptIds:user.deptId,parentDeptId:user.deptId});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='hostMan'){
                vm.item.hostMan = $scope.selectedOaUserLogins_;
                vm.item.hostManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='chargeLeader'){
                vm.item.chargeLeader = $scope.selectedOaUserLogins_;
                vm.item.chargeLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='attendUser'){
                vm.item.attendUser = $scope.selectedOaUserLogins_;
                vm.item.attendUserName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择申请部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //时间对比函数，如果a>b返回1，如果a<b返回-1，相等返回0
        function comptime(dateA,dateB) {
            if(isNaN(dateA) || isNaN(dateB)) return toastr.error("请重新选择申请时间!");
            if(dateA > dateB) return 1;
            if(dateA < dateB) return -1;
            return 0;
        }

        vm.listAllRoom = function(){
            var beginTime =new  Date(vm.item.beginTime.replace(/-/g,   "/"));
            var endTime =new  Date(vm.item.endTime.replace(/-/g,   "/"));
            if(vm.item.beginTime ==''||vm.item.endTime==''){
                toastr.error("请先选择申请的 起止时间！");
                return;
            }else if(comptime(endTime,beginTime) == -1)  {
                toastr.error("开始时间必须小于结束时间!");
                return;
            } else  {
                vm.save();
                setTimeout(function () {
                    fiveOaMeetingRoomService.listAllRoom(id).then(function (value) {
                        if (value.data.ret) {
                            vm.listAll = value.data.data;
                            for(var item=0;item<vm.listAll.length;item++){  //遍历对象数组，item表示某个具体的对象
                                for(var i in vm.listAll[item]){  //使用for in 遍历对象属性
                                    if(i=="remark") {
                                        vm.listAll[item][i] = $sce.trustAsHtml(vm.listAll[item][i]);  //objectList[item][i]表示某个对象的某个
                                    }
                                }
                            }
                            singleCheckBox(".cb_meetingRoom", "data-name");
                            $("#meetingRoomModel").modal("show");
                        }
                    })
                }, 500);

            }
        };

        vm.selectMeetingRoom = function(){
            $(".cb_meetingRoom:checked").each(function () {
                var value = $.parseJSON($(this).attr("data-name"));
                if(value.roomStatus=='正常') {
                    vm.item.meetingRoomName = value.roomName;
                    vm.item.meetingRoomId = value.id;
                    toastr.success("选择成功!")
                }else{
                    toastr.error("请选择可使用状态为 正常 的会议室");
                }

            });
            $("#meetingRoomModel").modal("hide");
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            if(vm.item.beginTime==""||vm.item.endTime==""){
                toastr.error("请先选择申请时间！")
                return
            }else {
                fiveOaMeetingRoomApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData(false);
                    }
                })
            }
        }
        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaMeetingRoomApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();});
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.loadData(true);

        $scope.refresh=function(){
            vm.loadData(true);
        }
        return vm;

    })

    //会议管理 会议安排
    .controller("FiveOaMeetingArrangeController", function ($state, $scope,$rootScope,fiveOaMeetingArrangeService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var uiSref="five.oa.meetingArrange";

        var tableName = $rootScope.loadTableName("five.oaMeetingArrange");

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {uiSref:uiSref,pageNum: vm.pageInfo.pageNum,userLogin:user.userLogin, pageSize: vm.pageInfo.pageSize,meetingRoomName:vm.roomName};
            fiveOaMeetingArrangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        vm.show = function (id) {
            $state.go("five.oaMeetingArrangeDetail",{id: id});
        }


        vm.add = function () {
            fiveOaMeetingArrangeService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveOaMeetingArrangeService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaMeetingArrangeDetailController", function ($rootScope,$state,$sce,$stateParams ,$scope, fiveOaMeetingArrangeService,commonPrintTableService) {
        var vm = this;
        var id = $stateParams.id;
        $scope.loadRightData(user.userLogin,"oa.meetingArrange");
        var tableName = $rootScope.loadTableName("five.oaMeetingArrange");

        vm.loadData = function () {
            fiveOaMeetingArrangeService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);
                    //显示不可选时间
                    vm.getApplyTime();
                }
            })
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择申请部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaMeetingArrangeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })

        }

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("会议安排");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };
        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaMeetingArrangeService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();});
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='hostMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择会议主持人", type:"部门",userLoginList: vm.item.hostMan,multiple:false,deptIds:user.deptId,parentDeptId:user.deptId});
            }else if (vm.status=='chargeLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择参会领导", type:"部门",userLoginList: vm.item.chargeLeader,deptIds:'16',parentDeptId:'16',multiple:true});
            }else if (vm.status=='attendUser'){
                $scope.showOaSelectEmployeeModal_({title:"请选择参会人员", type:"部门",userLoginList: vm.item.attendUser,multiple:true,deptIds:'1',parentDeptId:user.deptId});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='hostMan'){
                vm.item.hostMan = $scope.selectedOaUserLogins_;
                vm.item.hostManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='chargeLeader'){
                vm.item.chargeLeader = $scope.selectedOaUserLogins_;
                vm.item.chargeLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='attendUser'){
                vm.item.attendUser = $scope.selectedOaUserLogins_;
                vm.item.attendUserName = $scope.selectedOaUserNames_;
            }
        };
        vm.loadData(true);

        $scope.refresh=function(){
            vm.loadData(true);
        }
        return vm;

    })

    //会议管理 会议室管理
    .controller("FiveOaMeetingRoomController", function ($state, $scope,$rootScope, fiveOaMeetingRoomService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        var tableName = $rootScope.loadTableName("five.oaMeetingRoom");

        vm.loadData = function () {
            fiveOaMeetingRoomService.selectAll().then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;
                }
            })
        };
        vm.show = function (id) {
            $state.go("five.oaMeetingRoomDetail",{id: id});
        }

        vm.add = function () {
            var item={};item.meetingroom_id ="",item.name ="新增会议室";
            fiveOaMeetingRoomService.save(item).then(function (value) {
                if (value.data.ret) {
                    vm.loadData();
                }
            })
        }
        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveOaMeetingRoomService.remove(id).then(function (value) {
                        toastr.success("删除成功!");
                        vm.loadData();
                    });
                }
            })
        }
        vm.loadData();

        return vm;

    })
    .controller("FiveOaMeetingRoomDetailController", function ($state,$stateParams,$rootScope,fiveOaMeetingRoomApplyService,$scope, commonCodeService,fiveOaMeetingRoomService,commonPrintTableService) {
        var vm = this;
        var meetingId = $stateParams.id;
        var tableName = $rootScope.loadTableName("five.oaMeetingRoom");

        vm.loadData = function () {
            $scope.basicInit();
            vm.now = vm.dateFormat(new Date(),'yyyy-MM-dd');
            $("#now").datepicker('setDate', vm.now);
            fiveOaMeetingRoomService.getModelById(meetingId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.loadDetail();
                }
            })
        };
        vm.loadDetail=function(){
            fiveOaMeetingRoomService.listRecentBookData(meetingId,vm.now).then(function(value){
                if (value.data.ret){
                    vm.listDetail=value.data.data;

                }
            })
        }
        vm.dateFormat = function (date,fmt) {
            var o = {
                "M+": date.getMonth() + 1, //月份
                "d+": date.getDate(), //日
                "h+": date.getHours(), //小时
                "m+": date.getMinutes(), //分
                "s+": date.getSeconds(), //秒
                "q+": Math.floor((date.getMonth() + 3) / 3), //季度
                "S": date.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
        /**
         * 保存基础信息
         */
        vm.save = function () {
            fiveOaMeetingRoomService.save(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData();
                }
            })
        };
        //打印
        vm.print=function () {
            console.log(vm.item.businessKey);
            console.log(user.userLogin);
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {

                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("车辆详情");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        //设备类型 多选
        vm.showTypeModel = function () {
            commonCodeService.listDataByCatalog(user.userLogin, "会议室设备类型").then(function (value) {
                if (value.data.ret) {
                    vm.types = value.data.data;
                    $("#typeModal").modal("show");
                }
            })
        }
        vm.saveType = function () {
            var types = [];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.equipmentNames = type;
            vm.save();
        }
        vm.loadData();
        return vm;

    })

    //会议管理 决策会议管理
    .controller("FiveOaDecisionMakingController", function ($state, $scope,$rootScope, fiveOaDecisionMakingService,commonFileService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qDeptName: "",pageNum: 1, pageSize: 10,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaDecisionMaking";
        var tableName = $rootScope.loadTableName(uiSref);


        vm.init=function(){
            vm.loadPagedData();
            vm.listNotCompletedDetail();

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/file/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 50 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制50MB!');
                        return false;
                    }
                    data.formData = {businessKey:vm.currenDetail.businessKey,dirId:0,enLogin: user.enLogin};
                    data.submit();
                },

                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.currenDetail.attachId= vm.currenDetail.attachId+','+result.data;
                        vm.loadDetailFile();
                        $scope.$apply();
                        //  $scope.loadPagedData();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaDecisionMakingService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaDecisionMakingDetail", {makingId: id});
        }

        vm.add = function () {
            fiveOaDecisionMakingService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaDecisionMakingService.remove(id,user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        //议题相关
        //获取未处理议题
        vm.signQutePageInfo = {pageNum: 1, pageSize: 8};
        vm.signQuteParams={keyWord:""};
        vm.listNotCompletedDetail=function () {
            var params = {pageNum: vm.signQutePageInfo.pageNum, pageSize: vm.signQutePageInfo.pageSize,userLogin:user.userLogin,keyWord: vm.signQuteParams.keyWord};
            fiveOaDecisionMakingService.listNotCompletedDetail(params).then(function (value) {
                if (value.data.ret) {
                    vm.signQutePageInfo = value.data.data;
                }
            });
        };

        vm.showDetail=function(item){
            vm.currenDetail=item;
            vm.loadDetailFile();
            $("#decisionDetailModal").modal("show");
        }
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.currenDetail.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.currenDetail.deptName = $scope.selectedOaDeptNames_;
            vm.currenDetail.deptId = Number($scope.selectedOaDeptIds_);
        };
        vm.addDecisionDetailModal=function(){
            fiveOaDecisionMakingService.getNewModelDetail("",user.userLogin).then(function(value){
                if (value.data.ret){
                    vm.currenDetail=value.data.data;
                }
            })
            $("#decisionDetailModal").modal("show");
        };

        vm.updateDetail = function () {
            fiveOaDecisionMakingService.updateDetail(vm.currenDetail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("议题保存成功!");
                    $("#decisionDetailModal").modal("hide");
                    vm.listNotCompletedDetail();

                };
            });
        };
        vm.removeDetail = function (id) {
            fiveOaDecisionMakingService.removeDetail(id,true).then(function (value) {
                if (value.data.ret) {
                    toastr.success("议题已删除!")
                    vm.listNotCompletedDetail();
                };
            });
        };

        vm.loadDetailFile=function(){
            commonFileService.listData(vm.currenDetail.businessKey,0).then(function(value){
                if (value.data.data){
                    vm.currenDetailFileList=value.data.data;
                }
            })
        }
        vm.loadPagedData();

        vm.init();

        return vm;

    })
    .controller("FiveOaDecisionMakingDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,actService,myActService,commonCodeService,commonFileService,fiveOaDecisionMakingService) {
        var vm = this;
        var uiSref="five.oaSignQuote";
        var makingId = $stateParams.makingId;
        var tableName = $rootScope.loadTableName("five.oaDecisionMaking");

        vm.canEdit=false;
        vm.temporySeq=1;//顺序
        vm.init=function(){
            vm.signQuteParams = {pageNum: 1, pageSize: 7,keyWord:"",userLogin:user.userLogin};
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/file/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 50 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制50MB!');
                        return false;
                    }
                    data.formData = {businessKey:vm.currenDetail.businessKey,dirId:0,enLogin: user.enLogin};
                    data.submit();
                },

                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.currenDetail.attachId= vm.currenDetail.attachId+','+result.data;
                        vm.loadDetailFile();
                        $scope.$apply();
                        //  $scope.loadPagedData();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        };

        vm.loadData = function (loadProcess) {
            fiveOaDecisionMakingService.getModelById(makingId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        vm.listDetails();

                        var intervalfunction=setInterval(function(){
                            if($rootScope.processInstance!=null){
                                if(vm.item.creator==user.userLogin)//&&$rootScope.processInstance.firstTask
                                    vm.canEdit=true;
                                else
                                    vm.canEdit=false;
                                $scope.$apply();
                                clearInterval(intervalfunction);
                            }
                        }, 200);
                    }
                    $("#meetingTime").datepicker('setDate', vm.item.meetingTime);
                    $("#meetingTimePlan").datepicker('setDate', vm.item.meetingTimePlan);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaDecisionMakingService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'companyLeader') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择出席领导",
                    type: "部门",
                    userLoginList: vm.item.companyLeader,
                    multiple: true,
                    deptIds: "16",
                    parentDeptId: 16
                });
            } else if (vm.status == 'attender') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择列席领导",
                    type: "部门",
                    userLoginList: vm.item.attender,
                    multiple: true,
                    deptIds: "16",
                    parentDeptId: 16
                });
            } else if (vm.status == 'compere') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择主持人",
                    type: "部门",
                    userLoginList: vm.item.compere,
                    multiple: true,
                    deptIds: "16",
                    parentDeptId: 16
                });
            } else if (vm.status == 'recordMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择记录人",
                    type: "部门",
                    userLoginList: vm.item.recordMan,
                    multiple: true,
                    deptIds: "59,72",
                    parentDeptId: 59
                });
            } else if (vm.status == 'detailAttendance') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择议题列席人员",
                    type: "部门",
                    userLoginList: vm.currenDetail.attendance,
                    multiple: true,
                    deptIds: "1",
                    parentDeptId: 1
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='attender'){
                vm.item.attender = $scope.selectedOaUserLogins_;
                vm.item.attenderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='compere'){
                vm.item.compere = $scope.selectedOaUserLogins_;
                vm.item.compereName = $scope.selectedOaUserNames_;
            }else if (vm.status=='recordMan'){
                vm.item.recordMan = $scope.selectedOaUserLogins_;
                vm.item.recordManName = $scope.selectedOaUserNames_;
            }
            else if (vm.status == 'detailAttendance') {
                vm.currenDetail.attendance = $scope.selectedOaUserLogins_;
                vm.currenDetail.attendanceName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        //议题相关
        vm.listDetails=function() {
            vm.detailParams={
                businessKey:vm.item.businessKey,
                userLogin:user.userLogin
            };
            fiveOaDecisionMakingService.listDetail(vm.detailParams).then(function (value) {
                if (value.data.ret) {
                    vm.detailList=value.data.data;
                    vm.temporySeq=vm.detailList.length+1;
                    for(var i=0;i<vm.detailList.length;i++){
                        var item=vm.detailList[i];
                        if(item.detailType=="flow")
                            item.isFlowLink=true;
                        else
                            item.isFlowLink=false;
                    }
                }
            });
        };

        vm.updateDetail = function (item) {
            item.detailType=vm.item.meetingType;
            fiveOaDecisionMakingService.updateDetail(item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("议题保存成功!")
                    vm.listDetails();
                };
            });
        };
        //删除议题应该是 取消选择这个议题回到议题库 ，并设置为待办中
        vm.removeDetail = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaDecisionMakingService.removeDetail(id,false).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("议题已删除!")
                            vm.listDetails();
                        };
                    });
                }
            })
        };

        vm.addDecisionDetailModal=function(){
            /*   if(vm.detailList!=null){
                   vm.temporySeq=vm.detailList.length+1;
               }

               vm.currenDetail={mainBusiness:vm.item.businessKey,title:"",seq:  vm.temporySeq,gmt};
   */
            fiveOaDecisionMakingService.getNewModelDetail(vm.item.businessKey,user.userLogin).then(function(value){
                if (value.data.ret){
                    vm.currenDetail=value.data.data;
                }
            })
            $("#decisionDetailModal").modal("show");
        };

        vm.editDecisionDetailModal=function(item){
            vm.currenDetail=item;
            vm.loadDetailFile();
            $("#decisionDetailModal").modal("show");
        };

        vm.saveDecisionDetailModal=function(){
            vm.currenDetail.arrangeMan=user.userLogin;
            vm.currenDetail.arrangeName=user.userName;
            vm.updateDetail(vm.currenDetail);
            $("#decisionDetailModal").modal("hide");
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaDecisionMakingService.update(vm.item).then(function (value) {
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

        vm.selectAllFile=function(){
            $(".cb_meet").each(function () {
                var file_check_id = $(this).attr("data-id");
                if (file_check_id.indexOf("file") == 0) {
                    $(this).attr("checked",true);
                }
            })
        }



        vm.print=function () {
            fiveOaDecisionMakingService.getPrintData(makingId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("决策会议");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        vm.printItem=function () {
            fiveOaDecisionMakingService.getPrintData(makingId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("决策会议纪要");
                    if (vm.printData.meetingType.indexOf("董事")>=0){
                        var strBodyStyle = "<style>" + document.getElementById("print_style2").innerHTML + "</style>";
                        setTimeout(function () {
                            var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area2").innerHTML + "</body>";
                            lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                            lodop.PREVIEW();
                        }, 500);
                    } else if (vm.printData.meetingType.indexOf("总经理办公会")>=0) {
                        var strBodyStyle = "<style>" + document.getElementById("print_style1").innerHTML + "</style>";
                        setTimeout(function () {
                            var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area1").innerHTML + "</body>";
                            lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                            lodop.PREVIEW();
                        }, 500);
                    }else {
                        var strBodyStyle = "<style>" + document.getElementById("print_style3").innerHTML + "</style>";
                        setTimeout(function () {
                            var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area3").innerHTML + "</body>";
                            lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                            lodop.PREVIEW();
                        }, 500);
                    }

                }
            })
        }


        //region 流程关联相关

        //获取未处理议题
        vm.listNotCompletedDetail=function () {
            vm.signQuteParams.keyWord="";
            vm.signQuteParams.detailType =vm.item.meetingType;
            fiveOaDecisionMakingService.listNotCompletedDetail(vm.signQuteParams).then(function (value) {
                if (value.data.ret) {
                    var data = value.data.data;
                    vm.signQutePageInfo=data;
                    vm.signQuteList = data.list;
                    singleCheckBox(".cb_relevance","data-name");

                }
            });
        };
        //搜索
        vm.researchSignQute=function(){
            vm.signQuteParams.pageNum=1;
            vm.signQuteParams.pageSize=7;
            vm.listNotCompletedDetail();
        };

        vm.showSignQuteModal=function(){
            vm.listNotCompletedDetail();
            $("#signQuteModal").modal("show");
        }

        vm.saveSignQute=function(){
            var selectedItem={};
            $(".cb_relevance:checked").each(function () {
                selectedItem = $.parseJSON($(this).attr("data-name"));

            });


            //修改当前子项
            vm.currenDetail=selectedItem;
            vm.currenDetail.seq=  vm.temporySeq;
            vm.currenDetail.issueStatus= "已排会";
            vm.currenDetail.detail= "审议事项";
            vm.currenDetail.mainBusiness=vm.item.businessKey;
            $("#signQuteModal").modal("hide");
        }

        vm.showTextDetail=function(item){
            vm.currenDetail=item;
            $("#decisionDetailReadModal").modal("show");
            vm.loadDetailFile();
        }

        vm.loadDetailFile=function(){
            commonFileService.listData(vm.currenDetail.businessKey,0).then(function(value){
                if (value.data.ret){
                    vm.currenDetailFileList=value.data.data;
                }
            })
        }
        vm.removeDetailFile=function(id){
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    commonFileService.remove(id,user.userLogin).then(function(value){
                        if (value.data.ret){
                            toastr.success("删除附件成功!")
                            vm.loadDetailFile();
                        }
                    })
                }
            })

        }
        vm.showFlowDetail=function(businessKey){
            actService.getNgRedirectUrl(businessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    var name='';
                    var id='';
                    for (var key in  result.params){
                        name=key;
                        id=result.params[key];
                    }
                    window.open("/act/plotIndex#?id="+ id+"&&url="+result.url+"&&name="+name);
                }
            })
        }
        //endregion
        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();
        return vm;

    })

    //会议管理 议题库
    
    .controller("FiveOaDecisionMakingListController", function ($state, $scope,$rootScope, fiveOaDecisionMakingService,commonFileService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qDeptName: "",pageNum: 1, pageSize: 10,total:0,detailType:""});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaDecisionMakingList";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){

            vm.listNotCompletedDetail();

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/file/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 50 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制50MB!');
                        return false;
                    }
                    data.formData = {businessKey:vm.currenDetail.businessKey,dirId:0,enLogin: user.enLogin};
                    data.submit();
                },

                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.currenDetail.attachId= vm.currenDetail.attachId+','+result.data;
                        vm.loadDetailFile();
                        $scope.$apply();
                        //  $scope.loadPagedData();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        //议题相关
        //获取未处理议题
        vm.signQuteParams = {pageNum: 1, pageSize: 15,keyWord:"",detailType:"",userLogin:user.userLogin};

        vm.listNotCompletedDetail=function () {
            fiveOaDecisionMakingService.listDetailPagedData(vm.signQuteParams).then(function (value) {
                if (value.data.ret) {
                    vm.signQutePageInfo = value.data.data;
                }
            });
        };

        vm.showDetail=function(item){
            vm.currenDetail=item;
            vm.loadDetailFile();
            $("#decisionDetailModal").modal("show");
        }
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.currenDetail.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.currenDetail.deptName = $scope.selectedOaDeptNames_;
            vm.currenDetail.deptId = Number($scope.selectedOaDeptIds_);
        };
        vm.addDecisionDetailModal=function(){
            fiveOaDecisionMakingService.getNewModelDetail("",user.userLogin).then(function(value){
                if (value.data.ret){
                    vm.currenDetail=value.data.data;
                }
            })
            $("#decisionDetailModal").modal("show");
        };

        vm.updateDetail = function () {
            fiveOaDecisionMakingService.updateDetail(vm.currenDetail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("议题保存成功!");
                    $("#decisionDetailModal").modal("hide");
                    vm.listNotCompletedDetail();

                };
            });
        };
        vm.removeDetail = function (id) {
            fiveOaDecisionMakingService.removeDetail(id,false).then(function (value) {
                if (value.data.ret) {
                    toastr.success("议题已删除!")
                    vm.listNotCompletedDetail();
                };
            });
        };

        vm.loadDetailFile=function(){
            commonFileService.listData(vm.currenDetail.businessKey,0).then(function(value){
                if (value.data.data){
                    vm.currenDetailFileList=value.data.data;
                }
            })
        }
        vm.init();

        return vm;

    })

    //用印管理 公司章用印申请
    .controller("FiveOaStampApplyOfficeController", function ($state, $scope,$rootScope, fiveOaStampApplyOfficeService) {
        var vm = this;
        var uiSref="five.oaStampApplyOffice";
        vm.params = { qName: "",noticeTitles:"",pageNum: 1, pageSize: $scope.pageSize,total:999};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var tableName = $rootScope.loadTableName(uiSref);


        vm.init=function(){
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
           /* var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,noticeTypes:vm.params.noticeTypes,uiSref:uiSref,userLogin:user.userLogin,
                noticeTitles:vm.params.noticeTitles};*/
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaStampApplyOfficeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaStampApplyOfficeDetail",{applyId: id});
        }

        vm.add = function () {
            fiveOaStampApplyOfficeService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveOaStampApplyOfficeService.remove(id,user.userLogin).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            })
        }

        vm.fuzzySearch = function (params) {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaStampApplyOfficeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'item',placeholder:'请输入事项..'},
                    2:{type:"input",colName:'stampName',placeholder:'印章类型'},
                    3:{type:"input",colName:'itemDeptName',placeholder:'事项归口管理部门'},
                    4:{type:"input",colName:'deptName',placeholder:'申请部门'},
                    5:{type:"input",colName:'creatorName',placeholder:'创建者'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"input",colName:'processEnd',placeholder:'任务状态'}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        vm.init();

        return vm;

    })
    .controller("FiveOaStampApplyOfficeDetailController", function ($state,$stateParams,$rootScope,$scope,$sce, commonCodeService,fiveOaStampApplyOfficeService,commonPrintTableService) {
        var vm = this;
        var applyId = $stateParams.applyId;
        var uiSref="five.oaStampApplyOffice";

        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);

            commonCodeService.listDataByCatalog("","办公室印章审批类型").then(function(value){
                if (value.data.ret){
                    vm.dataSource=value.data.data;
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("协同文件");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.loadData = function (loadProcess) {
            fiveOaStampApplyOfficeService.getModelById(applyId).then(function (value) {
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
            fiveOaStampApplyOfficeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //发送流程验证
        $scope.showSendTask=function(send){
            if(vm.item.stampName==""){
                toastr.warning("请先选择用印类型!")
                return
            }
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;

                fiveOaStampApplyOfficeService.update(vm.item).then(function (value) {
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
                            vm.loadData();
                        });
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }
        }

        vm.checkBoxType=function(status,name) {
            if (status == 'stampName') {
                if (vm.item.stampName.indexOf(name) >= 0) {
                    vm.item.stampName = vm.item.stampName.replace(name + ',', '');
                } else {
                    vm.item.stampName = vm.item.stampName + name + ',';
                }
            }
        }


        vm.showDeptModal=function(deptStatus) {
            vm.deptStatus=deptStatus;
            if (vm.deptStatus=='dept'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请部门",type:"选部门", deptIdList: vm.item.deptId,multiple:false,deptIds:"1",parentDeptId:vm.item.deptId});
            }else if (vm.deptStatus=='itemDept'){
                $scope.showOaSelectEmployeeModal_({title:"请选择事项归口管理部门",type:"选部门", deptIdList:vm.item.itemDept,multiple:false,deptIds:"59,100,72,48,11,18,38,29,9,67,101",parentDeptId:vm.item.itemDept});
            }
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptStatus=='dept'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = $scope.selectedOaDeptIds_;
            }else if (vm.deptStatus=='itemDept'){
                vm.item.itemDeptName = $scope.selectedOaDeptNames_;
                vm.item.itemDept = $scope.selectedOaDeptIds_;
            }
        }

        vm.init();

        $scope.refresh=function(){
            vm.loadData(true);
        }

        return vm;
    })


    //用车管理 个人车辆申请
    .controller("FiveOaSelfCarApplyController", function ($state, $scope,$rootScope, fiveOaCarApplyService,fiveOaCarService) {
        var vm = this;
        vm.params = {name: "", qName: "",type:"self",showTitle:true};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var  uiSref="five.oaSelfCarApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carId:vm.params.carId,userLogin:user.userLogin,type:vm.params.type,uiSref:uiSref};
            fiveOaCarApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            fiveOaCarService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.oaCars = value.data.data.list;
                }
            })
        };
        vm.show = function (id) {
            $state.go("five.oaSelfCarApplyDetail",{id: id});
        }


        vm.add = function () {
            fiveOaCarApplyService.getNewModel(user.userLogin,vm.params.type).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveOaCarApplyService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaCarApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaSelfCarApplyDetailController", function ($state,$sce,$stateParams,$rootScope ,$scope, fiveOaCarApplyService,fiveOaCarService ) {
        var vm = this;
        var id = $stateParams.id;
        $scope.loadRightData(user.userLogin,"five.oaSelfCarApply");
        vm.params = {name: "", qName: "",type:"self",showTitle:true};
        var tableName = $rootScope.loadTableName(uiSref);

        vm.loadData = function (loadProcess) {
            fiveOaCarApplyService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            });
        };
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择申请部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //时间对比函数，如果a>b返回1，如果a<b返回-1，相等返回0
        function comptime(dateA,dateB) {
            if(isNaN(dateA) || isNaN(dateB)) return toastr.error("请重新选择申请时间!");
            if(dateA > dateB) return 1;
            if(dateA < dateB) return -1;
            return 0;
        }


        vm.listAllCar = function(id){
            var beginTime =new  Date(vm.item.beginTime.replace(/-/g,   "/"));
            var endTime =new  Date(vm.item.endTime.replace(/-/g,   "/"));
            if(vm.item.beginTime==""||vm.item.endTime==""){
                toastr.error("请先填写申请时间!");
                return;
            }else if(comptime(endTime,beginTime) == -1)  {
                toastr.error("开始时间必须小于结束时间!");
                return;
            }
            /*            else if(comptime(beginTime,new Date()) == -1)  {
                            toastr.error("开始时间必须大于当前时间!");
                            return;
                        } */
            else  {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaCarApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData(false);
                        fiveOaCarService.listAllCar(id).then(function (value) {
                            if (value.data.ret) {
                                vm.listAll = value.data.data;
                                for(var item=0;item<vm.listAll.length;item++){  //遍历对象数组，item表示某个具体的对象
                                    for(var i in vm.listAll[item]){  //使用for in 遍历对象属性
                                        if(i=="remark") {
                                            vm.listAll[item][i] = $sce.trustAsHtml(vm.listAll[item][i]);  //objectList[item][i]表示某个对象的某个
                                        }
                                    }
                                }
                                singleCheckBox(".cb_car", "data-name");
                                $("#carModel").modal("show");
                            }
                        })
                    }
                })

            }
        };

        vm.selectCar = function(){
            $(".cb_car:checked").each(function () {
                var value = $.parseJSON($(this).attr("data-name"));
                if(value.carStatus=='正常') {
                    vm.item.carName = value.carNo;
                    vm.item.carId = value.id;
                    vm.save();
                }else{
                    toastr.error("请选择可使用状态为 正常 的车辆");
                }
            });
            $("#carModel").modal("hide");
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='driver'){
                $scope.showOaSelectEmployeeModal_({title:"请选择司机", userLoginList: vm.item.checkMan,multiple:true,type:"部门",deptIds:"99",parentDeptId:99,parentDeptIds:'99,59'});
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='driver'){
                vm.item.driver = $scope.selectedOaUserLogins_;
                vm.item.driverName = $scope.selectedOaUserNames_;
            }
        };



        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            if(vm.item.beginTime==""||vm.item.endTime==""){
                toastr.error("请先选择申请时间！")
                return
            }else {
                fiveOaCarApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData(false);
                    }
                })
            }
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaCarApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();});
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }



        vm.loadData(true);

        vm.print=function () {
            fiveOaCarApplyService.getPrintData(id).then(function (value) {
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

        $scope.refresh=function(){
            vm.loadData(true);
        }
        return vm;

    })

    //用车管理 领导车辆申请
    .controller("FiveOaLeaderCarApplyController", function ($state, $scope,$rootScope, fiveOaCarApplyService,fiveOaCarService) {
        var vm = this;
        vm.params = {name: "", qName: "",type:"leader",showTitle:false};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var  uiSref="five.oaLeaderCarApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carId:vm.params.carId,userLogin:user.userLogin,type:vm.params.type,uiSref:uiSref};
            fiveOaCarApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })

        };
        vm.show = function (id) {
            $state.go("five.oaLeaderCarApplyDetail",{id: id});
        }


        vm.add = function () {
            fiveOaCarApplyService.getNewModel(user.userLogin,vm.params.type).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveOaCarApplyService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaLeaderCarApplyDetailController", function ($state,$sce,$stateParams,$rootScope ,$scope, fiveOaCarApplyService,fiveOaCarService ) {
        var vm = this;
        var id = $stateParams.id;
        vm.params = {name: "", qName: "",type:"leader",showTitle:false};
        $scope.loadRightData(user.userLogin,"five.oaLeaderCarApply");
        var tableName = $rootScope.loadTableName(uiSref);

        vm.loadData = function (loadProcess) {
            fiveOaCarApplyService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            });
        };
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择申请部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        //时间对比函数，如果a>b返回1，如果a<b返回-1，相等返回0
        function comptime(dateA,dateB) {
            if(isNaN(dateA) || isNaN(dateB)) return toastr.error("请重新选择申请时间!");
            if(dateA > dateB) return 1;
            if(dateA < dateB) return -1;
            return 0;
        }


        vm.listAllCar = function(id){
            var beginTime =new  Date(vm.item.beginTime.replace(/-/g,   "/"));
            var endTime =new  Date(vm.item.endTime.replace(/-/g,   "/"));
            if(vm.item.beginTime==""||vm.item.endTime==""){
                toastr.error("请先填写申请时间!");
                return;
            }else if(comptime(endTime,beginTime) == -1)  {
                toastr.error("开始时间必须小于结束时间!");
                return;
            }
            /*            else if(comptime(beginTime,new Date()) == -1)  {
                            toastr.error("开始时间必须大于当前时间!");
                            return;
                        } */
            else  {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaCarApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData(false);
                        fiveOaCarService.listAllCar(id).then(function (value) {
                            if (value.data.ret) {
                                vm.listAll = value.data.data;
                                for(var item=0;item<vm.listAll.length;item++){  //遍历对象数组，item表示某个具体的对象
                                    for(var i in vm.listAll[item]){  //使用for in 遍历对象属性
                                        if(i=="remark") {
                                            vm.listAll[item][i] = $sce.trustAsHtml(vm.listAll[item][i]);  //objectList[item][i]表示某个对象的某个
                                        }
                                    }
                                }
                                singleCheckBox(".cb_car", "data-name");
                                $("#carModel").modal("show");
                            }
                        })
                    }
                })

            }
        };

        vm.selectCar = function(){
            $(".cb_car:checked").each(function () {
                var value = $.parseJSON($(this).attr("data-name"));
                if(value.carStatus=='正常') {
                    vm.item.carName = value.carNo;
                    vm.item.carId = value.id;
                    vm.save();
                }else{
                    toastr.error("请选择可使用状态为 正常 的车辆");
                }
            });
            $("#carModel").modal("hide");
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='driver'){
                $scope.showOaSelectEmployeeModal_({title:"请选择司机", userLoginList: vm.item.checkMan,multiple:true,type:"部门",deptIds:"99",parentDeptId:99,parentDeptIds:'99,59'});
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='driver'){
                vm.item.driver = $scope.selectedOaUserLogins_;
                vm.item.driverName = $scope.selectedOaUserNames_;
            }
        };



        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            if(vm.item.beginTime==""||vm.item.endTime==""){
                toastr.error("请先选择申请时间！")
                return
            }else {
                fiveOaCarApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData(false);
                    }
                })
            }
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaCarApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();});
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }
        vm.loadData(true);

        vm.print=function () {
            fiveOaCarApplyService.getPrintData(id).then(function (value) {
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

        $scope.refresh=function(){
            vm.loadData(true);
        }
        return vm;

    })

    //用车管理 车辆管理
    .controller("FiveOaCarController", function ($state, $scope,$rootScope, fiveOaCarService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var tableName = $rootScope.loadTableName("five.oaCar");

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carNos:vm.pageInfo.carNo};
            fiveOaCarService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
        };
        vm.show = function (id) {
            $state.go("five.oaCarDetail",{id: id});
        }


        vm.add = function () {
            fiveOaCarService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveOaCarService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaCarDetailController", function ($state,$stateParams ,$rootScope,$scope, fiveOaCarService ,fiveOaCarMaintainService,fiveOaCarApplyService,commonPrintTableService) {
        var vm = this;
        var id = $stateParams.id;
        var myDate=new Date();
        var tYear = myDate.getFullYear();
        var tMonth = myDate.getMonth()+1;
        vm.params = {name: "", qName: "",qNameType: "",selectYear:tYear,selectMonth:tMonth};
        vm.pageInfo = {pageNum: 1, pageSize: 10,total:999};
        vm.pageInfoApply = {pageNum: 1, pageSize: 10,total:999};

        vm.downExcl={};
        var  uiSref="five.oaCarMaintain";//车辆维护
        var  uiSref2="five.oaCarApply";//车辆使用
        var tableName = $rootScope.loadTableName("five.oaCar");


        vm.loadData = function () {
            fiveOaCarService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.basicInit(vm.item.businessKey);
                    $rootScope.getTplConfig("",vm.item.businessKey,user.userLogin);
                }
                // $("#buyDate").datepicker('setDate', vm.item.buyDate);
            })

            vm.loadCarMaintain();
            vm.loadCarApply();
        };
        //维护
        vm.loadCarMaintain=function(){
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carId:id,userLogin:user.userLogin,uiSref:uiSref,qName:vm.params.qName,selectYear:vm.params.selectYear,selectMonth:vm.params.selectMonth};
            fiveOaCarMaintainService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        }

        vm.downExcelMaintain=function(){
            var  selectPa={selectType:"车辆维护",selectYear:vm.params.selectYear,selectCarId:id,selectMonth:vm.params.selectMonth,qType:vm.params.qName}
            fiveOaCarService.downloadModelExcel(selectPa).then(function(response){
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
        //申请
        vm.loadCarApply=function(){
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carId:id,userLogin:user.userLogin,uiSref:uiSref2,qNameType:vm.params.qNameType,selectYear:vm.params.selectYear,selectMonth:vm.params.selectMonth};
            fiveOaCarApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfoApply = value.data.data;
                }
            })
        }

        vm.downExcelApply=function(){
            var  selectPa={selectType:"车辆使用",selectYear:vm.params.selectYear,selectCarId:id,selectMonth:vm.params.selectMonth,qType:vm.params.qNameType}
            fiveOaCarService.downloadModelExcel(selectPa).then(function(response){
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
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaCarService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.show = function (id) {
            $state.go("five.oaCarMaintainDetail",{id: id});
        }

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("车辆详情");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaCarService.update(vm.item).then(function (value) {
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

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择车辆负责人",type:"部门",deptIds:"1",parentDeptId:vm.item.deptId,userLoginList: vm.item.chargeMan,multiple:false});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='chargeMan'){
                vm.item.chargeMan = $scope.selectedOaUserLogins_;
                vm.item.chargeManName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择车辆所在单位",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.loadData();

        return vm;


    })

    //用车管理 车辆维护
    .controller("FiveOaCarMaintainController", function ($state, $scope,$rootScope, fiveOaCarMaintainService,fiveOaCarService) {
        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var  uiSref="five.oaCarMaintain";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.loadPagedData = function () {
            vm.pageInfo.pageNum = 1;
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,carId:vm.params.carId,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaCarMaintainService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            fiveOaCarService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.oaCars = value.data.data.list;
                }
            })
        };
        vm.show = function (id) {
            $state.go("five.oaCarMaintainDetail",{id: id});
        }


        vm.add = function () {
            fiveOaCarMaintainService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    fiveOaCarMaintainService.remove(id,user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadPagedData();
                    });
                }
            })
        }

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaCarMaintainDetailController", function ($state,$sce,$stateParams,$rootScope ,$scope, commonCodeService,fiveOaCarMaintainService,fiveOaCarService,commonPrintTableService) {
        var vm = this;
        var id = $stateParams.id;
        $scope.loadRightData(user.userLogin,"five.oaCarMaintain");
        var tableName = $rootScope.loadTableName("five.oaCarMaintain");

        vm.loadData = function (loadProcess) {
            fiveOaCarMaintainService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#soilTime").datepicker('setDate', vm.item.soilTime);
                    $("#upkeepTime").datepicker('setDate', vm.item.upkeepTime);
                    $("#checkTime").datepicker('setDate', vm.item.checkTime);
                    $("#etcTime").datepicker('setDate', vm.item.etcTime);
                }
            });

        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择申请部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.listAllCar = function(){
            vm.item.operateUserLogin = user.userLogin;
            fiveOaCarService.listAllCarByMaintain(id).then(function (value) {
                vm.listAll = value.data.data;
                singleCheckBox(".cb_car", "data-name");
                $("#carModel").modal("show");
            })
        };

        vm.selectCar = function(){
            $(".cb_car:checked").each(function () {
                var value = $.parseJSON($(this).attr("data-name"));
                vm.item.carNo = value.carNo;
                vm.item.carId = value.id;
                vm.save();
            });
            $("#carModel").modal("hide");
        };

        vm.showTypeModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"车辆维护类型").then(function(value){
                if (value.data.ret){
                    vm.types=value.data.data;
                    $("#typeModal").modal("show");
                }
            })

        }

        vm.saveType=function(){
            var types=[];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });
            $("#typeModal").modal("hide");
            var type = types.join(',');
            vm.item.type =type;
            vm.save();
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='driver'){
                $scope.showOaSelectEmployeeModal_({title:"请选择司机", userLoginList: vm.item.checkMan,multiple:true,type:"部门",deptIds:"99",parentDeptId:99,parentDeptIds:'99,59'});
            }
        }
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='driver'){
                vm.item.driver = $scope.selectedOaUserLogins_;
                vm.item.driverName = $scope.selectedOaUserNames_;
            }
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            if(vm.item.beginTime==""||vm.item.endTime==""){
                toastr.error("请先选择申请时间！")
                return
            }else {
                fiveOaCarMaintainService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.loadData(false);
                    }
                })
            }
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaCarMaintainService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function () {
                            $scope.refresh();});
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.loadData(true);

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("车辆详情");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $scope.refresh=function(){
            vm.loadData(true);
        }
        return vm;
    })
    
    //信息化审批  年度信息化设备采购预算
    .controller("FiveOaInformationEquipmentProcurementController", function ($state, $scope,$rootScope, fiveOaInformationEquipmentProcurementService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var uiSref="five.oaInformationEquipmentProcurement";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.params = getCacheParams(key,{dispatchType: "年度信息化设备采购预算表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,uiSref:uiSref,startTime1:'',endTime1:''});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
            $scope.basicInit("");
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaInformationEquipmentProcurementService.downTempleXls(params).then(function (response) {

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

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            };
            fiveOaInformationEquipmentProcurementService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaInformationEquipmentProcurementDetail", {informationEquipmentProcurementId: id});
        };


        vm.add = function () {
            fiveOaInformationEquipmentProcurementService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaInformationEquipmentProcurementService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationEquipmentProcurementService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'请输入申请单位..'},
                    2:{type:"input",colName:'procurementTime',placeholder:'采购时间'},
                    3:{type:"input",colName:'firstMangerMan'},
                    4:{type:"input",colName:'creatorName'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        vm.queryData();

        vm.init();

        return vm;

    })
    .controller("FiveOaInformationEquipmentProcurementDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaInformationEquipmentProcurementService) {
        var vm = this;
        var uiSref="five.oaInformationEquipmentProcurement";
        var informationEquipmentProcurementId = $stateParams.informationEquipmentProcurementId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        };

        vm.loadData = function (loadProcess) {
            fiveOaInformationEquipmentProcurementService.getModelById(informationEquipmentProcurementId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#procurementTime").datepicker('setDate', vm.item.procurementTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaInformationEquipmentProcurementService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='listMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择制表人",type:"部门",deptIds:"1", userLoginList: vm.item.listMen,multiple:true});
            } else if (vm.status=='firstMangerMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择第一管理者",type:"部门",deptIds:"1", userLoginList: vm.item.firstMangerMan,multiple:false});
            }else if (vm.status=='chargeLeaderMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司主管领导",type:"部门",deptIds:"1", parentDeptId:16,userLoginList: vm.item.chargeLeaderMen,multiple:false});
            }
        };
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='listMen'){
                vm.item.listMen = $scope.selectedOaUserLogins_;
                vm.item.listMenName = $scope.selectedOaUserNames_;
            }else if (vm.status=='firstMangerMan'){
                vm.item.firstMangerMan = $scope.selectedOaUserLogins_;
                vm.item.firstMangerManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='chargeLeaderMen'){
                vm.item.chargeLeaderMen = $scope.selectedOaUserLogins_;
                vm.item.chargeLeaderMenName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaInformationEquipmentProcurementService.update(vm.item).then(function (value) {
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

        //总计计算  = 数量*单价
        vm.coutTotal=function(){
            var fin=vm.detail.price*vm.detail.number;
            vm.detail.totalPrice=fin.toFixed(2);
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.loadDetail=function(){
            fiveOaInformationEquipmentProcurementService.listDetail(informationEquipmentProcurementId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaInformationEquipmentProcurementService.getNewModelDetail(informationEquipmentProcurementId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaInformationEquipmentProcurementService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaInformationEquipmentProcurementService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaInformationEquipmentProcurementService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadDetail();
                }

            })
        };

        vm.print=function () {
            fiveOaInformationEquipmentProcurementService.getPrintData(informationEquipmentProcurementId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var informationEquipmentProcurementDetails = vm.printData.informationEquipmentProcurementDetails;
                    vm.printDetails = informationEquipmentProcurementDetails;

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

    //信息化审批  信息化设备采购事项审批
    .controller("FiveOaInformationEquipmentApplyController", function ($state, $scope,$rootScope, fiveOaInformationEquipmentApplyService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "信息化设备采购事项审批", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaInformationEquipmentApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaInformationEquipmentApplyService.downTempleXls(params).then(function (response) {

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

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationEquipmentApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaInformationEquipmentApplyDetail", {informationEquipmentApplyId: id});
        };


        vm.add = function () {
            fiveOaInformationEquipmentApplyService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaInformationEquipmentApplyService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };


        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationEquipmentApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'formNo',placeholder:'请输采购审批编号..'},
                    2:{type:"input",colName:'linkManName',placeholder:'联系人'},
                    3:{type:"input",colName:'deptName'},
                    4:{type:"select",colName:'plan',option:[{title:"全部",value:""},{title:"是",value:1},{title:"否",value:0}]},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        vm.queryData();

        return vm;

    })
    .controller("FiveOaInformationEquipmentApplyDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaInformationEquipmentApplyService) {
        var vm = this;
        var uiSref="five.oaInformationEquipmentApply";
        var informationEquipmentApplyId = $stateParams.informationEquipmentApplyId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        };

        vm.loadData = function (loadProcess) {
            fiveOaInformationEquipmentApplyService.getModelById(informationEquipmentApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applyTime").datepicker('setDate', vm.item.applyTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaInformationEquipmentApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='linkMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择联系人",type:"部门",deptIds:"1", userLoginList: vm.item.linkMan,multiple:true,parentDeptId:vm.item.deptId});
            } else if (vm.status=='deptChargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门负责人",type:"部门",deptIds:""+vm.item.deptId, userLoginList: vm.item.deptChargeMen,multiple:false,parentDeptId:vm.item.deptId});
            }else if (vm.status=='affairsMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择行政事务部负责人", type:"部门",deptIds:"67",userLoginList: vm.item.affairsMan,multiple:false,parentDeptId:67});
            }else if (vm.status=='technologyDeptMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择信息化建设与管理部负责人",type:"部门",deptIds:"11", userLoginList: vm.item.technologyDeptMan,multiple:false,parentDeptId:11});
            }else if (vm.status=='equipmentDeptMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择设备购置部门领导", type:"部门",deptIds:"1",userLoginList: vm.item.equipmentDeptMen,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='linkMan'){
                vm.item.linkMan = $scope.selectedOaUserLogins_;
                vm.item.linkManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMen'){
                vm.item.deptChargeMen = $scope.selectedOaUserLogins_;
                vm.item.deptChargeMenName = $scope.selectedOaUserNames_;
            }else if (vm.status=='affairsMan'){
                vm.item.affairsMan = $scope.selectedOaUserLogins_;
                vm.item.affairsManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='technologyDeptMan'){
                vm.item.technologyDeptMan = $scope.selectedOaUserLogins_;
                vm.item.technologyDeptManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='equipmentDeptMen'){
                vm.item.equipmentDeptMen = $scope.selectedOaUserLogins_;
                vm.item.equipmentDeptMenName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaInformationEquipmentApplyService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        if (vm.detalList.length==0){
                            toastr.warning("请填写购置设备信息数据!")
                            return false;
                        }
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

        vm.loadDetail=function(){
            fiveOaInformationEquipmentApplyService.listDetail(informationEquipmentApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.detalList = value.data.data;
                }
            })
        };

        vm.showDeptModal=function(id) {
            if(vm.opt=="deptId"){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt=="detailDeptId"){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.detail.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }

        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt=="deptId") {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }else if(vm.opt=="detailDeptId"){
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }
        };

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaInformationEquipmentApplyService.getNewModelDetail(informationEquipmentApplyId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaInformationEquipmentApplyService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaInformationEquipmentApplyService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaInformationEquipmentApplyService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadDetail();
                    vm.loadDate();
                }

            })
        };

        //总计计算  = 数量*单价
        vm.coutTotal=function(){
            var fin=vm.detail.price*vm.detail.number;
            vm.detail.totalPrice=fin.toFixed(2);
        };

        vm.print=function () {
            fiveOaInformationEquipmentApplyService.getPrintData(informationEquipmentApplyId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var informationEquipmentApplyDetails=vm.printData.informationEquipmentApplyDetails;
                    vm.printDetails=informationEquipmentApplyDetails;
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

    //信息化审批  信息化设备验收（多台）审批表
    .controller("FiveOaInformationEquipmentExamineListController", function ($state,$rootScope, $scope, fiveOaInformationEquipmentExamineListService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var uiSref="five.oaInformationEquipmentExamine";
        var tableName = $rootScope.loadTableName("five.oaInformationEquipmentExamineList");

        vm.params = getCacheParams(key,{dispatchType: "信息化设备验收审批表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,userLogin:user.userLogin,uiSref:uiSref});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};


        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationEquipmentExamineListService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaInformationEquipmentExamineListDetail", {listId: id});
        };

        vm.add = function () {
            fiveOaInformationEquipmentExamineListService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaInformationEquipmentExamineListService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationEquipmentExamineListService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'equipmentName',placeholder:'请输入设备名称..'},
                    2:{type:"input",colName:'formNo',placeholder:'设备编号'},
                    3:{type:"input",colName:'deptName',placeholder:'所属单位'},
                    4:{type:"input",colName:'osInstallTime'},
                    5:{type:"input",colName:'chargeManName'},
                    6:{type:"input",colName:'startTime'},
                    7:{type:"input",colName:'gmtCreate'},
                    8:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:9};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        vm.queryData();

        return vm;

    })
    .controller("FiveOaInformationEquipmentExamineListDetailController", function ( $state,$stateParams,$rootScope,$scope,fiveOaInformationEquipmentExamineListService,fiveOaInformationEquipmentApplyService,commonFileService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaInformationEquipmentExamine";
        var listId = $stateParams.listId;
        var tableName = $rootScope.loadTableName("five.oaInformationEquipmentExamineList");


        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/file/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 50 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制50MB!');
                        return false;
                    }
                    data.formData = {businessKey:vm.examine.businessKey,dirId:0,enLogin: user.enLogin};
                    data.submit();
                },

                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.loadDetailFile();
                        vm.updateDetail();
                        $scope.$apply();
                        //  $scope.loadPagedData();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });

        };

        vm.loadData = function (loadProcess) {
            fiveOaInformationEquipmentExamineListService.getModelById(listId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#acceptTime").datepicker('setDate', vm.item.acceptTime);
                    $("#osInstallTime").datepicker('setDate', vm.item.osInstallTime);
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                }

                vm.listDetails();
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaInformationEquipmentExamineListService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };
        //加载验收设备子表
        vm.listDetails=function(){
            fiveOaInformationEquipmentExamineListService.listDetail(listId).then(function (value) {
                if (value.data.ret) {
                    vm.examineList=value.data.data;
                }
            })
        }
        //显示验收设备子表
        vm.showDetail=function(id){
            if (id==0){
                $("#detailModal").modal("show");
            }else {
                fiveOaInformationEquipmentExamineListService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.examine=value.data.data;
                        vm.loadDetailFile();
                    }
                })
            }

            $("#detailModal").modal("show");

        }
        //新增验收设备子表
        vm.addDetail=function(){
            fiveOaInformationEquipmentExamineListService.getNewModelDetail(listId).then(function (value) {
                if (value.data.ret) {
                    vm.examine=value.data.data;
                    vm.showDetail(value.data.data.id);
                }
            })
        }
        //保存验收设备子表
        vm.saveDetail=function(){
            if ($("#detailForm").validate().form()) {
                fiveOaInformationEquipmentExamineListService.updateDetail( vm.examine).then(function (value) {
                    if (value.data.ret) {
                        $("#detailModal").modal("hide");
                        vm.listDetails();
                    }
                })
            }else {
                toastr.warning("请准确填写数据!");
                return false;
            }

        }
        //保存验收设备子表
        vm.updateDetail=function(){
            fiveOaInformationEquipmentExamineListService.updateDetail(vm.examine).then(function (value) {
                    if (value.data.ret) {
                        vm.listDetails();
                    }
                })
        }
        //删除验收设备子表
        vm.removeDetail=function(id){
            fiveOaInformationEquipmentExamineListService.removeDetail(id).then(function (value) {
                if (value.data.ret) {
                    vm.listDetails();
                    toastr.success("删除成功!");
                }
            })
        }
         //显示选人控件
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择设备责任人", type:"部门",deptIds:"1",userLoginList: vm.item.chargeMan,multiple:true,parentDeptId:16});
            }else if (vm.status=='userMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择设备使用人", type:"部门",deptIds:"1",userLoginList: vm.item.userMan,multiple:false});
            }
        };
        //保存选人
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='chargeMan'){
                vm.examine.chargeMan = $scope.selectedOaUserLogins_;
                vm.examine.chargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='userMan'){
                vm.examine.user = $scope.selectedOaUserLogins_;
                vm.examine.userName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;

                var flag=true;
                vm.item.operateUserLogin = user.userLogin;
                for(var i=0;i<vm.examineList.length;i++){
                    if (!vm.examineList[i].uploadfile){
                        flag=false;
                        toastr.warning("子项:"+vm.examineList[i].equipmentNo+'  请上传对应实物图附件!');
                    }
                }
                if (flag){
                    fiveOaInformationEquipmentExamineListService.update(vm.item).then(function (value) {
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
                }


            }else {
                toastr.warning("请准确填写数据!");
                return false;
            }

        };

        //选择部门
        vm.showDeptModal=function(deptType) {
            vm.deptType=deptType;
            if (deptType=='examine'){

            }
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptType=='examine'){
                vm.examine.deptName = $scope.selectedOaDeptNames_;
                vm.examine.deptId = Number($scope.selectedOaDeptIds_);
            }else {
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }

        };

        vm.params ={formNo:"",qName: "",pageNum: 1, pageSize: 8,total:0}

        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        //加载 信息化设备采购事项审批 列表
        vm.loadEquipmentApply=function(){
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,formNo:vm.params.formNo,linkManName:vm.params.qName};

            fiveOaInformationEquipmentApplyService.listPagedDataExamine(params).then(function(value){
                if (value.data.ret){
                    vm.listDetail=value.data.data.list;
                    vm.pageInfo=value.data.data;
                }
            })
        }
        //弹出选择框 信息化设备采购事项审批列表
        vm.showEquipmentApply=function() {
            vm.loadEquipmentApply();
            singleCheckBox(".cb_apply", "data-name");
            $("#selectEquipmentApplyModal").modal("show");
        };

        //信息化设备采购事项审批 子项
        vm.loadApplyDetail=function(id){
            fiveOaInformationEquipmentApplyService.listDetail(id).then(function (value) {
                if (value.data.ret) {
                    vm.detalList = value.data.data;
                }
            })
        };

        //信息化设备采购事项审批 详情
        vm.showDetailApply=function() {
            vm.equipmentApply=vm.item.equipmentApply;
            if (vm.equipmentApply!=null){
                vm.loadApplyDetail(vm.equipmentApply.id);
                $("#equipmentApplyModal").modal("show");
            }

        };
        //保存 选择采购事项编号
        vm.saveEquipmentApply=function(){
            if ($(".cb_apply:checked").length > 0) {
                var apply = $.parseJSON($(".cb_apply:checked").first().attr("data-name"));
                vm.item.diskNo = apply.formNo;
                vm.save();
            }
            $("#selectEquipmentApplyModal").modal("hide");
        }


        //显示采购事项对应子项
        vm.showApplyDetail=function(){
            vm.equipmentApply=vm.item.equipmentApply;
            if (vm.equipmentApply!=null){
                vm.loadApplyDetail(vm.equipmentApply.id);
                $("#applyDetailModal").modal("show");
            }
        };

        //保存选择验收申请子项 生成验收设备子项
        vm.saveApplyDetail=function(){
            if ($(".cb_applyDetail:checked").length > 0) {
                var apply = $.parseJSON($(".cb_applyDetail:checked").first().attr("data-name"));

                fiveOaInformationEquipmentExamineListService.getNewDetailByApplyDetail(apply.id,listId,user.userLogin).then(function(value){
                     if (value.data.ret){
                         vm.listDetails();
                         $("#applyDetailModal").modal("hide");
                         toastr.success("生成验收子项成功!");
                     }
                })

            }

        }

        //生成设备序列编号
        vm.getEquipmentNo=function(id){
            fiveOaInformationEquipmentExamineListService.getEquipmentExamineNo(id).then(function(value){
                if (value.data.ret){
                    vm.loadData(false);
                    toastr.success("生成设备编号成功!入台账完成");
                }
            })
        }

        //子表图片上传
        vm.loadDetailFile=function(){
            commonFileService.listData(vm.examine.businessKey,0).then(function(value){
                if (value.data.ret){
                    vm.examineFileList=value.data.data;
                }
            })
        }

        vm.removeDetailFile=function(id){
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    commonFileService.remove(id,user.userLogin).then(function(value){
                        if (value.data.ret){
                            toastr.success("删除附件成功!")
                            vm.loadDetailFile();
                            vm.updateDetail();
                        }
                    })
                }
            })

        }

        //打印
        vm.print=function () {
            console.log(vm.item.businessKey);
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

        $scope.refresh=function(){
            vm.loadData(true);
        };

        return vm;

    })

    //信息化审批  信息化设备验收审批表
    .controller("FiveOaInformationEquipmentExamineController", function ($state,$rootScope, $scope, fiveOaInformationEquipmentExamineService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var uiSref="five.oaInformationEquipmentExamine";
        vm.params = getCacheParams(key,{dispatchType: "信息化设备验收审批表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,userLogin:user.userLogin,uiSref:uiSref,startTime1:'',endTime1:''});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
        };

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaInformationEquipmentExamineService.downTempleXls(params).then(function (response) {

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
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaInformationEquipmentExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    /*setCacheParams(key,vm.params,vm.pageInfo);*/
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaInformationEquipmentExamineDetail", {informationEquipmentExamineId: id});
        };

        vm.add = function () {
            fiveOaInformationEquipmentExamineService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaInformationEquipmentExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationEquipmentExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'equipmentName',placeholder:'请输入设备名称..'},
                    2:{type:"input",colName:'formNo',placeholder:'设备编号'},
                    3:{type:"input",colName:'deptName',placeholder:'所属单位'},
                    4:{type:"input",colName:'osInstallTime'},
                    5:{type:"input",colName:'chargeManName'},
                    6:{type:"input",colName:'startTime'},
                    7:{type:"input",colName:'gmtCreate'},
                    8:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:9};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.downloadModelExcel = function(){
            $rootScope.commonDownload('/common/export/download/'+21,{enLogin:user.enLogin});
        }

        vm.queryData();

        return vm;

    })
    .controller("FiveOaInformationEquipmentExamineDetailController", function ( $state,$stateParams,$rootScope,$scope,fiveOaInformationEquipmentExamineService,fiveOaInformationEquipmentApplyService) {
        var vm = this;
        var uiSref="five.oaInformationEquipmentExamine";
        var informationEquipmentExamineId = $stateParams.informationEquipmentExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaInformationEquipmentExamineService.getModelById(informationEquipmentExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#acceptTime").datepicker('setDate', vm.item.acceptTime);
                    $("#osInstallTime").datepicker('setDate', vm.item.osInstallTime);
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaInformationEquipmentExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择设备责任人", type:"部门",deptIds:"1",userLoginList: vm.item.chargeMan,multiple:true,parentDeptId:16});
            }else if (vm.status=='userMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择设备使用人", type:"部门",deptIds:"1",userLoginList: vm.item.userMan,multiple:false});
            }else if (vm.status=='examineMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择验收人", type:"部门",deptIds:"1",userLoginList: vm.item.examineMan,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='chargeMan'){
                vm.item.chargeMan = $scope.selectedOaUserLogins_;
                vm.item.chargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='userMan'){
                vm.item.user = $scope.selectedOaUserLogins_;
                vm.item.userName = $scope.selectedOaUserNames_;
            }else if (vm.status=='examineMan'){
                vm.item.examineMan = $scope.selectedOaUserLogins_;
                vm.item.examineManName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaInformationEquipmentExamineService.update(vm.item).then(function (value) {
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



        vm.params ={formNo:"",qName: "",pageNum: 1, pageSize: 10,total:0}
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        vm.loadEquipmentApply=function(){
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,formNo:vm.params.formNo,linkManName:vm.params.qName};

            fiveOaInformationEquipmentApplyService.listPagedDataExamine(params).then(function(value){
                if (value.data.ret){
                    vm.listDetail=value.data.data.list;
                    vm.pageInfo=value.data.data;
                }
            })
        }

        vm.showEquipmentApply=function() {
            vm.loadEquipmentApply();
            singleCheckBox(".cb_apply", "data-name");
            $("#selectEquipmentApplyModal").modal("show");
        };

        //信息化设备采购事项审批 子项
        vm.loadDetail=function(id){
            fiveOaInformationEquipmentApplyService.listDetail(id).then(function (value) {
                if (value.data.ret) {
                    vm.detalList = value.data.data;
                }
            })
        };

        //信息化设备采购事项审批 详情
        vm.showDetailApply=function() {
            vm.equipmentApply=vm.item.equipmentApply;
            if (vm.equipmentApply!=null){
                vm.loadDetail(vm.equipmentApply.id);
                $("#equipmentApplyModal").modal("show");
            }

        };

        vm.saveEquipmentApply=function(){
            if ($(".cb_apply:checked").length > 0) {
                var apply = $.parseJSON($(".cb_apply:checked").first().attr("data-name"));
                vm.item.diskNo = apply.formNo;
                vm.save();
            }

            $("#selectEquipmentApplyModal").modal("hide");
        }
        //生成设备序列编号
        vm.getEquipmentNo=function(){
            fiveOaInformationEquipmentExamineService.getEquipmentExamineNo(vm.item.id).then(function(value){
                if (value.data.ret){
                    vm.loadData(false);
                    toastr.success("生成设备编号成功!");
                }
            })
        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaInformationEquipmentExamineService.getPrintData(informationEquipmentExamineId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })


    //信息化审批 非密信息化设备安全准入审批
    .controller("FiveOaComputerNetworkController", function ($state, $scope,$rootScope, fiveOaComputerNetworkService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaComputerNetwork";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaComputerNetworkService.downTempleXls(params).then(function (response) {

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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerNetworkService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaComputerNetworkDetail", {networkId: id});
        }

        vm.add = function () {
            fiveOaComputerNetworkService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerNetworkService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.queryData();

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerNetworkService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'请输入申请部门..'},
                    2:{type:"input",colName:'userName',placeholder:'使用人'},
                    3:{type:"input",colName:'chargeManName',placeholder:'责任人'},
                    4:{type:"input",colName:'equipmentNo'},
                    5:{type:"input",colName:'serialNo'},
                    6:{type:"input",colName:'equipmentName'},
                    7:{type:"input",colName:'gmtCreate'},
                    8:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:9};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();

        return vm;

    })
    .controller("FiveOaComputerNetworkDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaComputerNetworkService,fiveAssetComputerService) {
        var vm = this;
        var uiSref="five.oaComputerNetwork";

        var networkId = $stateParams.networkId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveOaComputerNetworkService.getModelById(networkId).then(function (value) {
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
            fiveOaComputerNetworkService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showDeptModal=function(deptStatus) {
            vm.deptStatus=deptStatus;
            if (vm.deptStatus=='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择承办单位",type:"选部门", deptIdList: vm.item.deptId,multiple:false,deptIds:"1",parentDeptId:2});
            }
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.deptStatus=='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = $scope.selectedOaDeptIds_;
            }
        }
        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaComputerNetworkService.update(vm.item).then(function (value) {
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



        vm.showComputer=function(){
            fiveAssetComputerService.listDate(user.userLogin).then(function (value) {
                if (value.data.ret){
                    vm.listComputer=value.data.data;
                }
                $("#selectComputerModal").modal("show");
            })
        }

        vm.saveComputer = function (computer) {
            vm.item.equipmentNo=computer.computerNo;
            vm.item.chargeMan=computer.chargeMan;//责任人
            vm.item.chargeManName=computer.chargeManName;
            vm.item.userLogin=computer.useLogin;
            vm.item.userName=computer.useName;
            vm.item.deptId=computer.deptId;
            vm.item.deptName=computer.deptName;
            vm.item.equipmentName=computer.computerName;
            vm.item.equipmentType=computer.equipmentType;
            vm.item.applyReason=computer.useWay;
            vm.item.serialNo=computer.equipmentNo;//设备序列号
            $("#selectComputerModal").modal("hide");
            vm.save();
        }

        vm.onchange1=function(status){
            if(status=='networkEach'){
                vm.item.networkNoSecret=0;
                vm.item.networkMiddle=0;
                vm.item.networkAlone=0;
                vm.item.networkOther=0;
            }else if (status=='networkNoSecret') {
                vm.item.networkEach=0;
                vm.item.networkMiddle=0;
                vm.item.networkAlone=0;
                vm.item.networkOther=0;
            }else if (status=='networkMiddle') {
                vm.item.networkEach=0;
                vm.item.networkNoSecret=0;
                vm.item.networkAlone=0;
                vm.item.networkOther=0;
            }else if (status=='networkAlone') {
                vm.item.networkEach=0;
                vm.item.networkMiddle=0;
                vm.item.networkNoSecret=0;
                vm.item.networkOther=0;
            }else if (status=='networkOther') {
                vm.item.networkEach=0;
                vm.item.networkMiddle=0;
                vm.item.networkAlone=0;
                vm.item.networkNoSecret=0;
            }
        }

        vm.print=function () {
            fiveOaComputerNetworkService.getPrintData(networkId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var computerNetworkDetails = vm.printData.computerNetworkDetails;
                    vm.printDetails = computerNetworkDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }

        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //信息化审批 非密信息化设备使用状态变更
    .controller("FiveOaComputerChangeController", function ($state, $scope,$rootScope, fiveOaComputerChangeService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaComputerChange";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaComputerChangeService.downTempleXls(params).then(function (response) {

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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin ,uiSref:uiSref
            });
            fiveOaComputerChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaComputerChangeDetail", {changeId: id});
        }

        vm.add = function () {
            fiveOaComputerChangeService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerChangeService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.queryData();


        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin ,uiSref:uiSref
            });
            fiveOaComputerChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'computerNo',placeholder:'请输入设备编号..'},
                    2:{type:"input",colName:'computerName',placeholder:'设备名称'},
                    3:{type:"input",colName:'applyUserName',placeholder:'申请人'},
                    4:{type:"input",colName:'reason'},
                    5:{type:"input",colName:'newDutyName'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;

    })
    .controller("FiveOaComputerChangeDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaComputerChangeService,fiveAssetComputerService) {
        var vm = this;
        var uiSref="five.oaComputerChange";
        var changeId = $stateParams.changeId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.oaComputerChange");
        }

        vm.loadData = function (loadProcess) {
            fiveOaComputerChangeService.getModelById(changeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    /*$("#changeTime").datepicker('setDate', vm.item.changeTime);*/
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaComputerChangeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()&&$("#detail_form1").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaComputerChangeService.update(vm.item).then(function (value) {
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

        vm.showComputer=function(){
            fiveAssetComputerService.listDate(user.userLogin).then(function (value) {
                if (value.data.ret){
                    vm.listComputer=value.data.data;
                }

                $("#selectComputerModal").modal("show");
            })
        }

        vm.saveComputer = function (computer) {
            vm.item.assetId=computer.id;
            vm.item.dutyLogin=computer.chargeMan;
            vm.item.dutyName=computer.chargeManName;
            vm.item.dutyDeptId=computer.deptId;
            vm.item.dutyDeptName=computer.deptName;
            vm.item.usersLogin=computer.useLogin;
            vm.item.usersName=computer.useName;
            vm.item.usersDeptId=computer.deptId;
            vm.item.usersDeptName=computer.deptName;
            vm.item.deptId=computer.deptId;
            vm.item.deptName=computer.deptName;
            vm.item.useSituation=computer.useStatus;
            vm.item.useWay=computer.useWay;
            vm.item.computerNo=computer.computerNo;
            vm.item.computerName=computer.computerName;
            vm.item.assetNo=computer.fixedAssetNo;
            vm.item.macAddress=computer.macAddress;
            vm.item.network=computer.network;
            vm.item.room=computer.room;
            vm.item.hardDisk=computer.cdType;
            vm.item.usb=computer.usbStatus;
            vm.item.useType=computer.securityLevel;
            $("#selectComputerModal").modal("hide");
            vm.save();
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='dutyLogin'){
                $scope.showOaSelectEmployeeModal_({title:"变更后责任人", type:"部门",deptIds:"1",userLoginList: vm.item.userLoginChange,multiple:false});
            }
            if (vm.status=='usersLogin'){
                $scope.showOaSelectEmployeeModal_({title:"变更后责任人", type:"部门",deptIds:"1",userLoginList: vm.item.userLoginChange,multiple:false});
            }
            if (vm.status=='newDutyLogin'){
                $scope.showOaSelectEmployeeModal_({title:"变更后责任人", type:"部门",deptIds:"1",userLoginList: vm.item.userLoginChange,multiple:false});
            }
            if (vm.status=='newUsersLogin'){
                $scope.showOaSelectEmployeeModal_({title:"变更后责任人", type:"部门",deptIds:"1",userLoginList: vm.item.userLoginChange,multiple:false});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='dutyLogin'){
                vm.item.dutyLogin = $scope.selectedOaUserLogins_;
                vm.item.dutyName = $scope.selectedOaUserNames_;

            }
            if ( vm.status=='usersLogin'){
                vm.item.usersLogin = $scope.selectedOaUserLogins_;
                vm.item.usersName = $scope.selectedOaUserNames_;
            }
            if ( vm.status=='newDutyLogin'){
                vm.item.newDutyLogin = $scope.selectedOaUserLogins_;
                vm.item.newDutyName = $scope.selectedOaUserNames_;
            }
            if ( vm.status=='newUsersLogin'){
                vm.item.newUsersLogin = $scope.selectedOaUserLogins_;
                vm.item.newUsersName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal = function (deptStatus)  {
            vm.deptStatus=deptStatus;
            if (vm.deptStatus=='dutyDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择所属单位",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.dutyDeptId,
                    multiple: false
                });
            }
            if (vm.deptStatus=='usersDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择所属单位",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.usersDeptId,
                    multiple: false
                });
            }
            if (vm.deptStatus=='deptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择设备所属单位",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.deptId,
                    multiple: false
                });
            }
            vm.deptStatus=deptStatus;
            if (vm.deptStatus=='newDutyDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择所属单位",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.newDutyDeptId,
                    multiple: false
                });
            }
            if (vm.deptStatus=='newUsersDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择所属单位",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.newUsersDeptId,
                    multiple: false
                });
            }
            if (vm.deptStatus=='newDeptId') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择设备所属单位",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.newDeptId,
                    multiple: false
                });
            }
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.deptStatus=='dutyDeptId'){
                vm.item.dutyDeptName = $scope.selectedOaDeptNames_;
                vm.item.dutyDeptId = Number($scope.selectedOaDeptIds_);
            }
            if(vm.deptStatus=='usersDeptId'){
                vm.item.usersDeptName = $scope.selectedOaDeptNames_;
                vm.item.usersDeptId = Number($scope.selectedOaDeptIds_);
            }
            if(vm.deptStatus=='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }
            if(vm.deptStatus=='newDutyDeptId'){
                vm.item.newDutyDeptName = $scope.selectedOaDeptNames_;
                vm.item.newDutyDeptId = Number($scope.selectedOaDeptIds_);
            }
            if(vm.deptStatus=='newUsersDeptId'){
                vm.item.newUsersDeptName = $scope.selectedOaDeptNames_;
                vm.item.newUsersDeptId = Number($scope.selectedOaDeptIds_);
            }
            if(vm.deptStatus=='newDeptId'){
                vm.item.newDeptName = $scope.selectedOaDeptNames_;
                vm.item.newDeptId = Number($scope.selectedOaDeptIds_);
            }

        }

        vm.print=function () {
            fiveOaComputerChangeService.getPrintData(changeId).then(function (value) {
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

        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //信息化审批  非密信息化设备报废审批
    .controller("FiveOaNonSecretEquipmentScrapController", function ($state, $scope,$rootScope, fiveOaNonSecretEquipmentScrapService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "非密计算机及外设报废申请", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaNonSecretEquipmentScrap";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
        };

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaNonSecretEquipmentScrapService.downTempleXls(params).then(function (response) {
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
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaNonSecretEquipmentScrapService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaNonSecretEquipmentScrapDetail", {nonSecretEquipmentScrapId: id});
        };


        vm.add = function () {
            fiveOaNonSecretEquipmentScrapService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaNonSecretEquipmentScrapService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();


        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaNonSecretEquipmentScrapService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'equipmentNo',placeholder:'请输入设备编号..'},
                    2:{type:"input",colName:'equipmentSerial',placeholder:'设备序列号'},
                    3:{type:"input",colName:'scrapReason',placeholder:'报废原因'},
                    4:{type:"input",colName:'deptName'},
                    5:{type:"input",colName:'equipmentName'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        vm.init();


        return vm;

    })
    .controller("FiveOaNonSecretEquipmentScrapDetailController", function ($state,$stateParams,$rootScope,$scope,fiveAssetComputerService,fiveOaNonSecretEquipmentScrapService,commonFileService) {
        var vm = this;
        var uiSref="five.oaNonSecretEquipmentScrap";
        var nonSecretEquipmentScrapId = $stateParams.nonSecretEquipmentScrapId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        };

        vm.loadData = function (loadProcess) {
            fiveOaNonSecretEquipmentScrapService.getModelById(nonSecretEquipmentScrapId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applyTime").datepicker('setDate', vm.item.applyTime);
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaNonSecretEquipmentScrapService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applyMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",deptIds:user.deptId,userLoginList: vm.item.applyMan,multiple:false});
            }else if (vm.status=='deptChargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门负责人",type:"部门",deptIds:user.deptId, userLoginList: vm.item.deptChargeMen,multiple:true});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='applyMan'){
                vm.detail.applyMan = $scope.selectedOaUserLogins_;
                vm.detail.applyManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMen'){
                vm.detail.deptChargeMen = $scope.selectedOaUserLogins_;
                vm.detail.deptChargeMenName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                //验证是否上传附件内容
               /* commonFileService.listData(vm.item.businessKey,0).then(function(value){
                    if (value.data.data.length<=0){
                        toastr.warning("请上传实物照片!");
                        return false;
                    }
                })*/


                fiveOaNonSecretEquipmentScrapService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.loadDetail=function(){
            fiveOaNonSecretEquipmentScrapService.listDetail(nonSecretEquipmentScrapId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.showDetailModel = function (id,type) {
            if (id === 0) {
                fiveOaNonSecretEquipmentScrapService.getNewModelDetail(nonSecretEquipmentScrapId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaNonSecretEquipmentScrapService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaNonSecretEquipmentScrapService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaNonSecretEquipmentScrapService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadDetail();
                }

            })
        };

        //选择设备台账
        vm.showComputer=function(){
            fiveAssetComputerService.listDate(user.userLogin).then(function (value) {
                if (value.data.ret){
                    vm.listComputer=value.data.data;
                }
                $("#selectComputerModal").modal("show");
            })
        }

        vm.saveComputer = function (computer) {
            vm.item.equipmentNo=computer.computerNo;//设备编号
            vm.item.equipmentName=computer.computerName;//设备名称
            vm.item.equipmentSerial=computer.equipmentNo;
            vm.item.hardNo=computer.hardNo;
            vm.item.assetsNo= computer.fixedAssetNo;//固定资产编号
            $("#selectComputerModal").modal("hide");
           // vm.save();
        }

        vm.print=function () {
            fiveOaNonSecretEquipmentScrapService.getPrintData(nonSecretEquipmentScrapId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var nonSecretEquipmentScrapDetails = vm.printData.nonSecretEquipmentScrapDetails;

                    vm.printDetails = nonSecretEquipmentScrapDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2,0,0,"A4");
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

    //信息化审批 年度软件采购预算
    .controller("FiveOaInformationPlanController", function ($state, $scope,$rootScope, fiveOaInformationPlanService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,uiSref:uiSref,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaInformationPlan"
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaInformationPlanService.downTempleXls(params).then(function (response) {

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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationPlanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaInformationPlanDetail", {planId: id});
        }

        vm.add = function () {
            fiveOaInformationPlanService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaInformationPlanService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.queryData();

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInformationPlanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'请输入申请单位..'},
                    2:{type:"input",colName:'remark',placeholder:''},
                    3:{type:"input",colName:'year',placeholder:'预算年份'},
                    4:{type:"input",colName:'gmtCreate'},
                    5:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:6};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;

    })
    .controller("FiveOaInformationPlanDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaInformationPlanService) {
        var vm = this;
        var uiSref="five.oaInformationPlan"
        var planId = $stateParams.planId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            vm.listDetail();

        }

        vm.loadData = function (loadProcess) {
            fiveOaInformationPlanService.getModelById(planId).then(function (value) {
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
            fiveOaInformationPlanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaInformationPlanService.update(vm.item).then(function (value) {
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

        vm.addDetail=function(){
            fiveOaInformationPlanService.getNewModelDetail(planId).then(function (value) {
                if (value.data.ret){
                    vm.editDetail(value.data.data);
                }
            })
        }

        vm.updateDetail=function(){
            fiveOaInformationPlanService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret){
                    $("#detailModel").modal("hide");
                    vm.listDetail();
                }
            })
        }

        vm.removeDetail=function(id){
            fiveOaInformationPlanService.removeDetail(id).then(function (value) {
                if (value.data.ret){
                    toastr.success("删除成功!") ;
                    vm.listDetail();
                }
            })
        }

        vm.editDetail=function(id){
            fiveOaInformationPlanService.getModelDetailById(id).then(function (value) {
                if (value.data.ret){
                    vm.detail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.listDetail=function(){
            fiveOaInformationPlanService.listDetail(planId).then(function (value) {
                if (value.data.ret){
                    vm.detalList=value.data.data;
                }
            })
        }
        //总计计算  = 数量*单价
        vm.coutTotal=function(){
            var fin=vm.detail.softwareNumber*vm.detail.softwarePrice;
            vm.detail.softwareTotal=fin.toFixed(2);
        };
        vm.print=function () {
            fiveOaInformationPlanService.getPrintData(planId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("年度软件采购预算");

                    var paymentDetails = vm.printData.paymentDetails;
                    vm.printDetails = paymentDetails;

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //信息化审批-软件购置
    .controller("FiveOaSoftwareController", function ($state, $scope,$rootScope, fiveOaSoftwareService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaSoftware";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });

            fiveOaSoftwareService.downTempleXls(params).then(function (response) {

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
            fiveOaSoftwareService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaSoftwareDetail", {softwareId: id});
        }

        vm.add = function () {
            fiveOaSoftwareService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaSoftwareService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaSoftwareService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'softwareName',placeholder:'请输入软件名称..'},
                    2:{type:"input",colName:'deptName',placeholder:'申请单位'},
                    3:{type:"select",colName:'applyStyle',option:[{"title":"全部","value":""},{"title":"软件购置","value":"软件购置"},{"title":"软件升级","value":"软件升级"},{"title":"软件服务","value":"软件服务"}]},
                    4:{type:"input",colName:'softwareOffer'},
                    5:{type:"input",colName:'softwareUseMajor'},
                    6:{type:"input",colName:'softwareCompanyName'},
                    7:{type:"input",colName:'gmtCreate'},
                    8:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:9};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        vm.queryData();

        return vm;

    })
    .controller("FiveOaSoftwareDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaSoftwareService) {
        var vm = this;
        var uiSref="five.oaSoftware";
        var softwareId = $stateParams.softwareId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            vm.listDetail();
            $scope.loadRightData(user.userLogin,"five.oaSoftware");
        }

        vm.loadData = function (loadProcess) {
            fiveOaSoftwareService.getModelById(softwareId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applyTime").datepicker('setDate', vm.item.applyTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaSoftwareService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaSoftwareService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        ;
                        if (vm.detalList.length==0){
                            toastr.warning("请填写软件购置及费用分摊审批数据!")
                            return false;
                        }
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

        vm.addDetail=function(){
            fiveOaSoftwareService.getNewModelDetail(softwareId).then(function (value) {
                if (value.data.ret){
                    vm.editDetail(value.data.data);
                }
            })
        }

        vm.updateDetail=function(){
            fiveOaSoftwareService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret){
                    $("#detailModel").modal("hide");
                    vm.listDetail();
                }
            })
        }

        vm.removeDetail=function(id){
            fiveOaSoftwareService.removeDetail(id).then(function (value) {
                if (value.data.ret){
                    toastr.success("删除成功!") ;
                    vm.listDetail();
                }
            })
        }

        vm.editDetail=function(id){
            fiveOaSoftwareService.getModelDetailById(id).then(function (value) {
                if (value.data.ret){
                    vm.detail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.listDetail=function(){
            fiveOaSoftwareService.listDetail(softwareId).then(function (value) {
                if (value.data.ret){
                    vm.detalList=value.data.data;
                }
            })
        }

        vm.showDeptModal=function() {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.detail.softwareUseId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.softwareUseId = $scope.selectedOaDeptIds_;
            vm.detail.softwareUserName = $scope.selectedOaDeptNames_;
        }

        vm.print=function () {
            fiveOaSoftwareService.getPrintData(softwareId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var softwareCostDetails = vm.printData.softwareCostDetails;
                    vm.printDetails = softwareCostDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })


    //网络运维中心  信息化设备外部人员现场维修情况记录表
    .controller("FiveOaComputerPersonRepairController", function ($state, $scope, fiveOaComputerPersonRepairService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "计算机及U盘申请表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaComputerPersonRepair";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaComputerPersonRepairService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaComputerPersonRepairDetail", {computerPersonRepairId: id});
        };


        vm.add = function () {

            fiveOaComputerPersonRepairService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerPersonRepairService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerPersonRepairService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deviceNo',placeholder:'请输入设备编号..'},
                    2:{type:"input",colName:'deviceName',placeholder:'设备名称'},
                    3:{type:"input",colName:'dutyPersonName'},
                    4:{type:"input",colName:'deptName'},
                    5:{type:"input",colName:'repairManName'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        vm.queryData();

        return vm;

    })
    .controller("FiveOaComputerPersonRepairDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaComputerPersonRepairService) {
        var vm = this;
        var uiSref="five.oaComputerPersonRepair";
        var computerPersonRepairId = $stateParams.computerPersonRepairId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaComputerPersonRepairService.getModelById(computerPersonRepairId).then(function (value) {
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
            fiveOaComputerPersonRepairService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='dutyPersonId'){
                $scope.showOaSelectEmployeeModal_({title:"设备责任人",type:"部门",deptIds:1,
                    userLoginList: vm.item.dutyPersonName,multiple:false});
            }else if (vm.status=='repairMan'){
                $scope.showOaSelectEmployeeModal_({title:"维修人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.repairManName,multiple:false});
            }else if (vm.status=='removelMan'){
                $scope.showOaSelectEmployeeModal_({title:"拆除部件接收人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.removelManName,multiple:false});
            }else if (vm.status=='accompanyMan'){
                $scope.showOaSelectEmployeeModal_({title:"陪同人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.accompanyManName,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'dutyPersonId') {
                vm.item.dutyPersonId = $scope.selectedOaUserLogins_;
                vm.item.dutyPersonName = $scope.selectedOaUserNames_;
            }else if(vm.status == 'repairMan'){
                vm.item.repairMan = $scope.selectedOaUserLogins_;
                vm.item.repairManName = $scope.selectedOaUserNames_;
            }else if(vm.status == 'removelMan'){
                vm.item.removelMan = $scope.selectedOaUserLogins_;
                vm.item.removelManName = $scope.selectedOaUserNames_;
            }
            else if(vm.status == 'accompanyMan'){
                vm.item.accompanyMan = $scope.selectedOaUserLogins_;
                vm.item.accompanyManName = $scope.selectedOaUserNames_;
            }
        };
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaComputerPersonRepairService.update(vm.item).then(function (value) {
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

        vm.print=function () {
            fiveOaComputerPersonRepairService.getPrintData(computerPersonRepairId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;
    })


    //信息化审批  非密信息化设备安全技术处理工作记录
    .controller("FiveOaComputerSafeController", function ($state, $scope,$rootScope, fiveOaComputerSafeService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "计算机及U盘申请表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaComputerSafe";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaComputerSafeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaComputerSafeDetail", {computerSafeId: id});
        };


        vm.add = function () {
            fiveOaComputerSafeService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerSafeService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerSafeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deviceNo',placeholder:'请输入设备编号..'},
                    2:{type:"input",colName:'deviceName',placeholder:'设备名称'},
                    3:{type:"input",colName:'dutyPersonName'},
                    4:{type:"input",colName:'deptName'},
                    5:{type:"input",colName:'repairManName'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        return vm;

    })
    .controller("FiveOaComputerSafeDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaComputerSafeService) {
        var vm = this;
        var uiSref="five.oaComputerSafe";
        var computerSafeId = $stateParams.computerSafeId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaComputerSafeService.getModelById(computerSafeId).then(function (value) {
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
            fiveOaComputerSafeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='dutyPersonId'){
                $scope.showOaSelectEmployeeModal_({title:"设备责任人",type:"部门",deptIds:1,
                    userLoginList: vm.item.dutyPersonName,multiple:false});
            }else if (vm.status=='repairMan'){
                $scope.showOaSelectEmployeeModal_({title:"维修人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.repairManName,multiple:false});
            }else if (vm.status=='removelMan'){
                $scope.showOaSelectEmployeeModal_({title:"拆除部件接收人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.removelManName,multiple:false});
            }else if (vm.status=='accompanyMan'){
                $scope.showOaSelectEmployeeModal_({title:"陪同人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.accompanyManName,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'dutyPersonId') {
                vm.item.dutyPersonId = $scope.selectedOaUserLogins_;
                vm.item.dutyPersonName = $scope.selectedOaUserNames_;
            }else if(vm.status == 'repairMan'){
                vm.item.repairMan = $scope.selectedOaUserLogins_;
                vm.item.repairManName = $scope.selectedOaUserNames_;
            }else if(vm.status == 'removelMan'){
                vm.item.removelMan = $scope.selectedOaUserLogins_;
                vm.item.removelManName = $scope.selectedOaUserNames_;
            }
            else if(vm.status == 'accompanyMan'){
                vm.item.accompanyMan = $scope.selectedOaUserLogins_;
                vm.item.accompanyManName = $scope.selectedOaUserNames_;
            }
        };
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaComputerSafeService.update(vm.item).then(function (value) {
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

        //新增
        vm.showDetailModel = function (id) {

            if (id === 0) {
                fiveOaComputerSafeService.getNewModelDetail(computerSafeId, user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveOaComputerSafeService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }
        //删除
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaComputerSafeService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(true);
                        }
                    })
                }
            });
        };
        //保存
        vm.saveDetail = function () {
            fiveOaComputerSafeService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    vm.save();
                    vm.loadData(true);
                }
            })
        };

        vm.print=function () {
            fiveOaComputerSafeService.getPrintData(computerSafeId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;
    })

    //信息化审批  非密信息化设备维修审批表
    .controller("FiveOaComputerMaintainController", function ($state, $scope,$rootScope, fiveOaComputerMaintainService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "计算机及U盘申请表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaComputerMaintain";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerMaintainService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaComputerMaintainDetail", {computerMaintainId: id});
        };


        vm.add = function () {
            fiveOaComputerMaintainService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerMaintainService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerMaintainService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'maintainManName',placeholder:'请输入责任人..'},
                    2:{type:"input",colName:'deviceName',placeholder:'设备编号'},
                    3:{type:"input",colName:'deviceNo',placeholder:'设备名称'},
                    4:{type:"input",colName:'deviceRoom',placeholder:'维修地点'},
                    5:{type:"input",colName:'deviceLevel',placeholder:'维修方式'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });


        return vm;

    })
    .controller("FiveOaComputerMaintainDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaComputerMaintainService,fiveAssetComputerService) {
        var vm = this;
        var uiSref="five.oaComputerMaintain";
        var computerMaintainId = $stateParams.computerMaintainId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaComputerMaintainService.getModelById(computerMaintainId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }

                }
                $("#repairTime").datepicker('setDate', vm.item.repairTime);
                $("#maintainTime").datepicker('setDate', vm.item.maintainTime);
                $("#receiveTime").datepicker('setDate', vm.item.receiveTime);
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaComputerMaintainService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='maintainMan'){
                $scope.showOaSelectEmployeeModal_(
                    {title:"责任人",type:"部门",deptIds:1,
                        userLoginList: vm.item.maintainMan,multiple:false
                    });
            }/*else if (vm.status=='receiveMan'){
                $scope.showOaSelectEmployeeModal_({title:"设备领取人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.receiveMan,multiple:false});
            }else if (vm.status=='deptSecurityMan'){
                $scope.showOaSelectEmployeeModal_({title:"部门保密责任人",type:"部门",deptIds:user.deptId,
                    userLoginList: vm.item.deptSecurityMan,multiple:true});
            }*/

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'maintainMan') {
                vm.item.maintainMan = $scope.selectedOaUserLogins_;
                vm.item.maintainManName = $scope.selectedOaUserNames_;
            }/*else if(vm.status == 'receiveMan'){
                vm.item.receiveMan = $scope.selectedOaUserLogins_;
                vm.item.receiveManName = $scope.selectedOaUserNames_;
            }else if(vm.status == 'deptSecurityMan'){
                vm.item.deptSecurityMan = $scope.selectedOaUserLogins_;
                vm.item.deptSecurityManName = $scope.selectedOaUserNames_;
            }*/
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //选择设备台账
        vm.showComputer=function(){
            fiveAssetComputerService.listDate(user.userLogin).then(function (value) {
                if (value.data.ret){
                    vm.listComputer=value.data.data;
                }
                $("#selectComputerModal").modal("show");
            })
        }

        vm.saveComputer = function (computer) {
            vm.item.maintainMan=computer.chargeMan;//责任人
            vm.item.maintainManName=computer.chargeManName;
            vm.item.deviceNo=computer.computerNo;//设备编号
            vm.item.deviceName=computer.computerName;
            vm.item.deptId=computer.deptId;//所属部门
            vm.item.deptName=computer.deptName;
            vm.item.chargeComment=computer.room;//放置地点
            $("#selectComputerModal").modal("hide");
            vm.save();
        }


        vm.showComputerDetail=function(computerNo){
            fiveAssetComputerService.getModelByComputerNo(computerNo).then(function (value) {
                if (value.data.ret){
                    vm.computer=value.data.data;
                    $("#computerModal").modal("show");
                }

            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaComputerMaintainService.update(vm.item).then(function (value) {
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

        vm.print=function () {
            fiveOaComputerMaintainService.getPrintData(computerMaintainId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;
    })

    //信息化审批 公用计算机及U盘申请
    .controller("FiveOaComputerApplicationController", function ($state, $scope,$rootScope, fiveOaComputerApplicationService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "计算机及U盘申请表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaComputerApplication";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize, userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerApplicationService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaComputerApplicationDetail", {computerApplicationId: id});
        };


        vm.add = function () {
            fiveOaComputerApplicationService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerApplicationService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize, userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaComputerApplicationService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'申请部门'},
                    2:{type:"input",colName:'applicationManName',placeholder:'责任人'},
                    3:{type:"input",colName:'ipAddress'},
                    4:{type:"input",colName:'equipmentNo'},
                    5:{type:"input",colName:'roomNo'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveOaComputerApplicationDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaComputerApplicationService) {
        var vm = this;
        var uiSref="five.oaComputerApplication";
        var tableName = $rootScope.loadTableName(uiSref);

        var computerApplicationId = $stateParams.computerApplicationId;
        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };
        vm.loadData = function (loadProcess) {
            fiveOaComputerApplicationService.getModelById(computerApplicationId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);

                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicationTime").datepicker('setDate', vm.item.applicationTime);
                }
            })
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaComputerApplicationService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='sendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送审人",type:"部门",deptIds:"1", userLoginList: vm.item.sendMan,multiple:true,parentDeptId:16});
            }else if (vm.status=='drafter'){
                $scope.showOaSelectEmployeeModal_({title:"请选择拟稿人人员",type:"部门",deptIds:"1", userLoginList: vm.item.drafter,multiple:false});
            }

        };
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'ApplicationMan') {
                vm.item.ApplicationMan = $scope.selectedOaUserLogins_;
                vm.item.ApplicationManName = $scope.selectedOaUserNames_;
            }
        };

        vm.show = function (id) {
            $state.go("five.oaComputerPurchaseDetail", {computerPurchaseId: id});
        };
        vm.add = function () {
            fiveOaComputerPurchaseService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaComputerApplicationService.update(vm.item).then(function (value) {
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

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerPurchaseService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.print=function () {
            fiveOaComputerApplicationService.getPrintData(computerApplicationId).then(function (value) {
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
        };
        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //信息化审批 流程开发申请
    .controller("FiveOaProcessDevelopApplyController", function ($state, $scope,$rootScope, fiveOaProcessDevelopApplyService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{ qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaProcessDevelopApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaProcessDevelopApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaProcessDevelopApplyDetail", {processDevelopApplyId: id});
        };


        vm.add = function () {
            fiveOaProcessDevelopApplyService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaProcessDevelopApplyService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaProcessDevelopApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'title',placeholder:'标题'},
                    2:{type:"input",colName:'content',placeholder:'内容'},
                    3:{type:"input",colName:'deptName'},
                    4:{type:"input",colName:'gmtCreate'},
                    5:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:6};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveOaProcessDevelopApplyDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaProcessDevelopApplyService,hrDeptService) {
        var vm = this;
        var uiSref="five.oaProcessDevelopApply";
        var processDevelopApplyId = $stateParams.processDevelopApplyId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaProcessDevelopApplyService.getModelById(processDevelopApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $("#retireTime").datepicker('setDate', vm.item.retireTime);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaProcessDevelopApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeLeader'){
                $scope.showSelectEmployeeModal_({title:"请选择因公出国领导", userLoginList: vm.item.chargeLeader,multiple:true,parentDeptId:16});
            }else if (vm.status=='businessChargeLeader'){
                $scope.showSelectEmployeeModal_({title:"请选择业务主管领导", userLoginList: vm.item.businessChargeLeader,multiple:true});
            }else if (vm.status=='departmentChargeMen'){
                $scope.showSelectEmployeeModal_({title:"请选择单位负责人", userLoginList: vm.item.departmentChargeMen,multiple:true,parentDeptId:59});//公司办公室
            }else if (vm.status=='drafter'){
                $scope.showSelectEmployeeModal_({title:"请选择拟稿人人员", userLoginList: vm.item.drafter,multiple:false});
            }else if (vm.status=='realSendMan'){
                $scope.showSelectEmployeeModal_({title:"请选择主送人员", userLoginList: vm.item.realSendMan,multiple:true});
            }else if (vm.status=='copySendMan'){
                $scope.showSelectEmployeeModal_({title:"请选择抄送人员", userLoginList: vm.item.copySendMan,multiple:true});
            }else if (vm.status=='typist'){
                $scope.showSelectEmployeeModal_({title:"请选择打字员人员", userLoginList: vm.item.typist,multiple:false});
            }else if (vm.status=='proofreader'){
                $scope.showSelectEmployeeModal_({title:"请选择校对人员", userLoginList: vm.item.proofreader,multiple:false});
            }


        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if ( vm.status=='chargeLeader'){
                vm.item.chargeLeader = $scope.selectedUserLogins_;
                vm.item.chargeLeaderName = $scope.selectedUserNames_;
            }else if (vm.status=='businessChargeLeader'){
                vm.item.businessChargeLeader = $scope.selectedUserLogins_;
                vm.item.businessChargeLeaderName = $scope.selectedUserNames_;
            }else if (vm.status=='departmentChargeMen'){
                vm.item.departmentChargeMen = $scope.selectedUserLogins_;
                vm.item.departmentChargeMenName = $scope.selectedUserNames_;
            }else if (vm.status=='drafter'){
                vm.item.drafter = $scope.selectedUserLogins_;
                vm.item.drafterName = $scope.selectedUserNames_;
            }else if (vm.status=='realSendMan'){
                vm.item.realSendMan = $scope.selectedUserLogins_;
                vm.item.realSendManName = $scope.selectedUserNames_;
            }else if (vm.status=='copySendMan'){
                vm.item.copySendMan = $scope.selectedUserLogins_;
                vm.item.copySendManName = $scope.selectedUserNames_;
            }else if (vm.status=='typist'){
                vm.item.typist = $scope.selectedUserLogins_;
                vm.item.typistName = $scope.selectedUserNames_;
            }else if (vm.status=='proofreader'){
                vm.item.proofreader = $scope.selectedUserLogins_;
                vm.item.proofreaderName = $scope.selectedUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaProcessDevelopApplyService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.showDeptModal2=function(id) {
            $('#dept_select_tree2').jstree("destroy");
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

                $('#dept_select_tree2').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });

            });
            $("#deptSelectModal2").modal("show");
        };

        vm.saveDept2=function() {
            var dept = $('#dept_select_tree2').jstree(true).get_selected(true)[0];
            vm.item.retireDeptId= dept.id;
            vm.item.retireDeptName=dept.text;
            $("#deptSelectModal2").modal("hide");
        };

        vm.print=function () {
            fiveOaProcessDevelopApplyService.getPrintData(processDevelopApplyId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //信息化审批 权限调整
    .controller("FiveOaPrivilegeManagementController", function ($state, $scope,$rootScope, fiveOaPrivilegeManagementService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "权限调整", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaPrivilegeManagement";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize, userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaPrivilegeManagementService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaPrivilegeManagementDetail", {privilegeManagementId: id});
        };


        vm.add = function () {
            fiveOaPrivilegeManagementService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaPrivilegeManagementService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize, userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaPrivilegeManagementService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'applicationManName',placeholder:'请输入申请人..'},
                    2:{type:"input",colName:'deptName',placeholder:'申请部门'},
                    3:{type:"input",colName:'deptName',flow:'流程名称'},
                    4:{type:"input",colName:'delegationManName'},
                    5:{type:"input",colName:'delegationPrivilege'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();

        return vm;

    })
    .controller("FiveOaPrivilegeManagementDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaPrivilegeManagementService) {
        var vm = this;
        var uiSref="five.oaPrivilegeManagement";
        var privilegeManagementId = $stateParams.privilegeManagementId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };
        vm.loadData = function (loadProcess) {
            fiveOaPrivilegeManagementService.getModelById(privilegeManagementId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);

                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicationTime").datepicker('setDate', vm.item.applicationTime);
                }
            })
        };
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaPrivilegeManagementService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };
        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applicationMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送审人",type:"部门",deptIds:"1", userLoginList: vm.item.applicationMan,multiple:true,parentDeptId:16});
            }else if (vm.status=='delegationMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择委托人",type:"部门",deptIds:"1", userLoginList: vm.item.delegationMan,multiple:false});
            }

        };
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'applicationMan') {
                vm.item.applicationMan = $scope.selectedOaUserLogins_;
                vm.item.ApplicationManName = $scope.selectedOaUserNames_;
            }
            if (vm.status == 'delegationMan') {
                vm.item.delegationMan = $scope.selectedOaUserLogins_;
                vm.item.delegationManName = $scope.selectedOaUserNames_;
            }
        };

        vm.show = function (id) {
            $state.go("five.oaPrivilegeManagementDetail", {privilegeManagementId: id});
        };

        vm.add = function () {
            fiveOaPrivilegeManagementService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaPrivilegeManagementService.update(vm.item).then(function (value) {
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

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaPrivilegeManagementService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.print=function () {
            fiveOaPrivilegeManagementService.getPrintData(privilegeManagementId).then(function (value) {
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
        };
        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //信息化审批 公司非密计算机及信息化设备台帐
    .controller("FiveAssetComputerController", function ($state, $scope,$rootScope, fiveAssetComputerService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};

        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaAssetComputer";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.queryData(true);
        };
        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveAssetComputerService.downExcel(params).then(function (response) {

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

        //下载模板
        vm.downTempleXml = function () {
            fiveAssetComputerService.downTempleXls(user.userLogin).then(function (response) {
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
        //导入数据
        $("#uploadModelFile").fileupload({
            dataType: 'text',
            url: '/five/asset/assetComputer/receive.do?userLogin=' + user.userLogin,
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


        vm.loadPagedData = function () {
           // var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin};
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveAssetComputerService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            fiveAssetComputerService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (vm.item.processEnd){
                        $state.go("five.oaAssetComputerDetail", {computerId: vm.item.id});
                    }else {
                        $state.go("five.oaTrackAssetComputerDetail", {computerId: vm.item.id});
                    }
                }
            })
        }

        vm.add = function () {
            fiveAssetComputerService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveAssetComputerService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.fuzzySearch = function (params) {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin
            });
            fiveAssetComputerService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };




        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'chargeManName',placeholder:'请输入责任人..'},
                    2:{type:"input",colName:'useName',placeholder:'使用人'},
                    3:{type:"input",colName:'deptName',placeholder:'所属部门'},
                    4:{type:"input",colName:'computerNo',placeholder:'设备编号'},
                    5:{type:"input",colName:'computerName',placeholder:'设备名称'},
                    6:{type:"select",colName:'equipmentType',placeholder:'设备类型..',option:[{title:"全部",value:""},{title:"台式计算机",value:"台式计算机"},
                            {title:"便携式计算机",value:"便携式计算机"},{title:"图文加工设备",value:"图文加工设备"},{title:"移动存储设备",value:"移动存储设备"},{title:"服务器",value:"服务器"},
                            {title:"网络设备",value:"网络设备"},{title:"安全设备",value:"安全设备"},{title:"加密USBKey",value:"加密USBKey"},{title:"声像设备",value:"声像设备"},{title:"其他",value:"其他"}]},
                    7:{type:"select",colName:'network',placeholder:'联网类型',option:[{"title":"全部","value":""},{"title":"互联网","value":"互联网"},{"title":"非密内网","value":"非密内网"},{"title":"单机","value":"单机"},{"title":"中间机","value":"中间机"},{"title":"其他","value":"其他"}]},
                    8:{type:"select",colName:'securityLevel',placeholder:'使用类别',option:[{"title":"全部","value":""},{"title":"公开","value":"公开"},{"title":"内部","value":"内部"},{"title":"涉密","value":"涉密"}]},
                    9:{type:"select",colName:'useStatus',placeholder:'使用情况',option:[{"title":"全部","value":""},{"title":"未启用","value":"未启用"},{"title":"在用","value":"在用"},{"title":"停用","value":"停用"},{"title":"报废","value":"报废"}]},
                    10:{type:"input",colName:'gmtCreate'},
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:11};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveAssetComputerDetailController", function ($state,$stateParams,$rootScope,$scope,fiveAssetComputerService,hrEmployeeService,commonPrintTableService) {
        var vm = this;

        var computerId = $stateParams.computerId;
        var uiSref="five.oaAssetComputer";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.canSave=false;

        vm.init=function(){
            vm.loadData(true);

            hrEmployeeService.getModelByUserLogin(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.hrUser = value.data.data;
                    if (vm.hrUser.roleNames.indexOf("信息化建设与管理部(信息化设备)")>=0){
                        vm.canSave=true;
                    }
                }
            })
        }

        vm.loadData = function (loadProcess) {
            fiveAssetComputerService.getModelById(computerId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveAssetComputerService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'useLogin') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.useLogin,
                    multiple: true
                });
            }
            if (vm.status == 'chargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.chargeMan,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'useLogin') {
                vm.item.useName = $scope.selectedOaUserNames_;
                vm.item.useLogin = $scope.selectedOaUserLogins_;
            }if (vm.status == 'chargeMan') {
                vm.item.chargeManName = $scope.selectedOaUserNames_;
                vm.item.chargeMan = $scope.selectedOaUserLogins_;
            }
        };

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("公司非密计算机及信息化设备台帐");

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();

        return vm;

    })

    //带流程的台账补录
    .controller("FiveTrackAssetComputerController", function ($state, $scope,$rootScope, fiveAssetComputerService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,uiSref:uiSref,startTime1:'',endTime1:''};

        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.oaTrackOaAssetComputer";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveAssetComputerService.downTempleXls(params).then(function (response) {

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

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin,flag:true,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveAssetComputerService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaTrackAssetComputerDetail", {computerId: id});
        }
        vm.add = function () {
            fiveAssetComputerService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveAssetComputerService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!")
                            vm.queryData();
                        }
                    });
                }
            })
        }

        vm.fuzzySearch = function (params) {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin
            });
            fiveAssetComputerService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };



        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'chargeManName',placeholder:'请输入责任人..'},
                    2:{type:"input",colName:'useName',placeholder:'使用人'},
                    3:{type:"input",colName:'deptName',placeholder:'所属部门'},
                    4:{type:"input",colName:'computerNo',placeholder:'设备编号'},
                    5:{type:"input",colName:'computerName',placeholder:'设备名称'},
                    6:{type:"select",colName:'equipmentType',placeholder:'设备类型..',option:[{title:"全部",value:""},{title:"台式计算机",value:"台式计算机"},
                            {title:"便携式计算机",value:"便携式计算机"},{title:"图文加工设备",value:"图文加工设备"},{title:"移动存储设备",value:"移动存储设备"},{title:"服务器",value:"服务器"},
                            {title:"网络设备",value:"网络设备"},{title:"安全设备",value:"安全设备"},{title:"加密USBKey",value:"加密USBKey"},{title:"声像设备",value:"声像设备"},{title:"其他",value:"其他"}]},
                    7:{type:"select",colName:'network',placeholder:'联网类型',option:[{"title":"全部","value":""},{"title":"互联网","value":"互联网"},{"title":"非密内网","value":"非密内网"},{"title":"单机","value":"单机"},{"title":"中间机","value":"中间机"},{"title":"其他","value":"其他"}]},
                    8:{type:"input",colName:'gmtCreate'},
                    9:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:10};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveTrackAssetComputerDetailController", function ($state,$stateParams,$rootScope,$scope,fiveAssetComputerService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaTrackOaAssetComputer";
        var computerId = $stateParams.computerId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveAssetComputerService.getModelById(computerId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.basicInit(vm.item.businessKey);
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                    }

                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveAssetComputerService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        vm.showUserModel = function (status) {
            vm.status = status;
            if (vm.status == 'useLogin') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.useLogin,
                    multiple: true
                });
            }
            if (vm.status == 'chargeMan') {
                $scope.showOaSelectEmployeeModal_({
                    title: "请选择申请人",
                    type: '部门',
                    deptIds: "1",
                    userLoginList: vm.item.chargeMan,
                    multiple: true
                });
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if (vm.status == 'useLogin') {
                vm.item.useName = $scope.selectedOaUserNames_;
                vm.item.useLogin = $scope.selectedOaUserLogins_;
            }if (vm.status == 'chargeMan') {
                vm.item.chargeManName = $scope.selectedOaUserNames_;
                vm.item.chargeMan = $scope.selectedOaUserLogins_;
            }
        };

        vm.showDeptModal = function (id) {
            $scope.showOaSelectEmployeeModal_({
                title: "请选择部门", type: "选部门", deptIdList: vm.item.deptId + "",
                multiple: false, deptIds: "1", parentDeptId: 2
            });
        }

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("公司非密计算机及信息化设备台帐(补录)");

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveAssetComputerService.update(vm.item).then(function (value) {
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

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.init();

        return vm;

    })

    // 信息化审批  涉密信息系统账户及权限开通、变更申请
    .controller("FiveOaSecretSystemController", function ($state,$rootScope, $scope, fiveOaSecretSystemService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var uiSref="five.oaSecretSystem";
        vm.params = getCacheParams(key,{dispatchType: "涉密信息系统账户及权限开通、变更申请", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,userLogin:user.userLogin,uiSref:uiSref});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};

        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaSecretSystemService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaSecretSystemDetail", {id: id});
        };

        vm.add = function () {
            fiveOaSecretSystemService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaSecretSystemService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.  ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaSecretSystemService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'请输部门名称..'},
                    2:{type:"input",colName:'userName',placeholder:''},
                    3:{type:"input",colName:'systemName',placeholder:'系统名称'},
                    4:{type:"select",colName:'secretLevel',placeholder:'系统名称',option:[{title:"全部",value:""},{title:"一般",value:'一般'},{title:"重要",value:'重要'}]},
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
    .controller("FiveOaSecretSystemDetailController", function ( $state,$stateParams,$rootScope,$scope,fiveOaSecretSystemService,hrEmployeeService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaSecretSystem";
        var id = $stateParams.id;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaSecretSystemService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#acceptTime").datepicker('setDate', vm.item.acceptTime);
                    $("#osInstallTime").datepicker('setDate', vm.item.osInstallTime);
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;

            vm.item.systemName= vm.item.systemName.replace(/,+$/,"");
            vm.item.secretLevel= vm.item.secretLevel.replace(/,+$/,"");
            vm.item.accountType= vm.item.accountType.replace(/,+$/,"");
            vm.item.jurisdictionType= vm.item.jurisdictionType.replace(/,+$/,"");
            fiveOaSecretSystemService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='user'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",deptIds:"1",userLoginList: vm.item.applyUserLogin,multiple:false,parentDeptId:1});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='user'){
                vm.item.applyUserLogin = $scope.selectedOaUserLogins_;
                vm.item.applyUserName = $scope.selectedOaUserNames_;
                hrEmployeeService.getModelByUserLogin( vm.item.applyUserLogin).then(function(value){
                    var employee=value.data.data;
                    vm.item.applyUserNo=employee.userNo;
                    vm.item.deptId=employee.deptId;
                    vm.item.deptName=employee.deptName;
                    vm.item.phone=employee.mobile;
                })
            }

        };

        vm.checkBoxType=function(status,name){
            if (status=='systemName'){
                if (vm.item.systemName.indexOf(name)>=0){
                    vm.item.systemName=vm.item.systemName.replace(name+',','');
                }else {
                    vm.item.systemName= vm.item.systemName+name+',';
                }
            }else if (status=='secretLevel'){
                if (vm.item.secretLevel.indexOf(name)>=0){
                    vm.item.secretLevel=vm.item.secretLevel.replace(name+',','');
                }else {
                    vm.item.secretLevel= vm.item.secretLevel+name+',';
                }
            }else if (status=='accountType'){
                if (vm.item.accountType.indexOf(name)>=0){
                    vm.item.accountType=vm.item.accountType.replace(name+',','');
                }else {
                    vm.item.accountType= vm.item.accountType+name+',';
                }
            }else if (status=='jurisdictionType'){
                if (vm.item.jurisdictionType.indexOf(name)>=0){
                    vm.item.jurisdictionType=vm.item.jurisdictionType.replace(name+',','');
                }else {
                    vm.item.jurisdictionType= vm.item.jurisdictionType+name+',';
                }
            }
        }


        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaSecretSystemService.update(vm.item).then(function (value) {
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

        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //协会学会审批 入协会申请
    .controller("FiveOaAssociationApplyController", function ($state, $scope,$rootScope, fiveOaAssociationApplyService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaAssociationApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaAssociationApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaAssociationApplyDetail", {applyId: id});
        }

        vm.add = function () {
            fiveOaAssociationApplyService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaAssociationApplyService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaAssociationApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'associationName',placeholder:'请输协会名称..'},
                    2:{type:"input",colName:'deptChargeName',placeholder:'主管单位'},
                    3:{type:"select",colName:'associationType',placeholder:'协会类型',option:[{title:"全部",value:""},{title:"综合类",value:'综合类'},{title:"专业类",value:'专业类'},{title:"管理类",value:'管理类'}]},
                    4:{type:"input",colName:'recommendManName'},
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
    .controller("FiveOaAssociationApplyDetailController", function ($state,$stateParams,$rootScope,$scope,commonFileService,fiveOaAssociationApplyService) {
        var vm = this;
        var uiSref="five.oaAssociationApply";
        var applyId = $stateParams.applyId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.oaAssociationApply");
        }

        vm.loadData = function (loadProcess) {
            fiveOaAssociationApplyService.getModelById(applyId).then(function (value) {
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
            fiveOaAssociationApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }
        //发送流程验证
        $scope.showSendTask=function(send){
            commonFileService.listDataCount(vm.item.businessKey,'-1').then(function (value) {
                if(value.data.data == 0){
                    toastr.warning("请上传协会入会邀请证明材料!");
                    return ;
                }else{
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        fiveOaAssociationApplyService.update(vm.item).then(function (value) {
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
            });
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='handleMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",userLoginList: vm.item.handleMan,deptIds:"1",multiple:false});
            }else if (vm.status=='recommendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择推荐负责人", type:"部门",userLoginList: vm.item.recommendMan,deptIds:"1",multiple:false});
            }else if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司领导", type:"部门",userLoginList: vm.item.companyLeader,deptIds:"16",multiple:true,parentDeptId:16});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='handleMan'){
                vm.item.handleMan = $scope.selectedOaUserLogins_;
                vm.item.handleManName = $scope.selectedOaUserNames_;
            }else  if ( vm.status=='recommendMan'){
                vm.item.recommendMan = $scope.selectedOaUserLogins_;
                vm.item.recommendManName = $scope.selectedOaUserNames_;
            }else  if ( vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.print=function () {
            fiveOaAssociationApplyService.getPrintData(applyId).then(function (value) {
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

        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //协会学会审批 协会信息变更
    .controller("FiveOaAssociationChangeController", function ($state, $scope,$rootScope, fiveOaAssociationChangeService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaAssociationChange";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaAssociationChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaAssociationChangeDetail", {changeId: id});
        }

        vm.add = function () {
            fiveOaAssociationChangeService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaAssociationChangeService.remove(id, user.userLogin).then(function (value) {
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
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaAssociationChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'associationName',placeholder:'请输协会名称..'},
                    2:{type:"select",colName:'changeItem',placeholder:'变更事项',option:[{title:"全部",value:""},{title:"协会负责人",value:"协会负责人"},{title:"负责人",value:"负责人"},{title:"公司承担角色",value:"公司承担角色"},{title:"会费",value:"会费"}]},
                    3:{type:"input",colName:'gmtCreate'},
                    4:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:5};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveOaAssociationChangeDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaAssociationChangeService,fiveOaAssociationApplyService) {
        var vm = this;
        var uiSref="five.oaAssociationChange";

        var changeId = $stateParams.changeId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.oaAssociationApply");
        }

        vm.loadData = function (loadProcess) {
            fiveOaAssociationChangeService.getModelById(changeId).then(function (value) {
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
            fiveOaAssociationChangeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaAssociationChangeService.update(vm.item).then(function (value) {
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

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='handleMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",userLoginList: vm.item.handleMan,deptIds:"1",multiple:false});
            }else if (vm.status=='changeRecommendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择推荐负责人",type:"部门", userLoginList: vm.item.changeRecommendMan,deptIds:"1",multiple:false});
            }else if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司领导",type:"部门", userLoginList: vm.item.companyLeader,deptIds:"1",multiple:true,parentDeptId:16});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='handleMan'){
                vm.item.handleMan = $scope.selectedOaUserLogins_;
                vm.item.handleManName = $scope.selectedOaUserNames_;
            }else  if ( vm.status=='changeRecommendMan'){
                vm.item.changeRecommendMan = $scope.selectedOaUserLogins_;
                vm.item.changeRecommendManName = $scope.selectedOaUserNames_;
            }else  if ( vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.showAssociation=function(){
            fiveOaAssociationApplyService.listAssociation().then(function (value) {
                if (value.data.ret){
                    vm.listAssociation=value.data.data;
                    singleCheckBox(".cb_association","data-name");
                    $("#selectAssociationModal").modal("show");
                }
            })
        }

        vm.saveAssociation=function(){
            if($(".cb_association:checked").length>0){
                var id=$(".cb_association:checked").first().attr("data-name");
                fiveOaAssociationChangeService.saveAssociation(id,vm.item.id).then(function (value) {
                    if (value.data.ret){
                        $("#selectAssociationModal").modal("hide");
                        vm.loadData();
                    }
                })
            }
        }

        vm.showChangeItem=function(){
            var types="协会联系人,负责人,公司担任角色,会费";
            vm.items=types.split(',');
            $("#selectChangeItemModal").modal("show");
        }
        vm.saveChangeItem=function(){
            var types=[];
            $(".cb_item:checked").each(function () {
                var type = $(this).attr("data-name");
                types.push(type);
            })
            vm.item.changeItem=types.join(",");
            $("#selectChangeItemModal").modal("hide");
        }

        vm.print=function () {

            fiveOaAssociationChangeService.getPrintData(changeId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //协会学会审批 协会缴费
    .controller("FiveOaAssociationPaymentController", function ($state, $scope,$rootScope, fiveOaAssociationPaymentService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaAssociationPayment";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaAssociationPaymentService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaAssociationPaymentDetail", {paymentId: id});
        }

        vm.add = function () {
            fiveOaAssociationPaymentService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaAssociationPaymentService.remove(id, user.userLogin).then(function (value) {
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
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaAssociationPaymentService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'associationName',placeholder:'请输协会名称..'},
                    2:{type:"input",colName:'deptChargeName',placeholder:'主管单位'},
                    3:{type:"input",colName:'associationNo'},
                    4:{type:"input",colName:'recommendMan'},
                    5:{type:"input",colName:'paymentMoney'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveOaAssociationPaymentDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaAssociationPaymentService,fiveOaAssociationApplyService,commonFileService) {
        var vm = this;
        var uiSref="five.oaAssociationPayment";

        var paymentId = $stateParams.paymentId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.oaAssociationApply");
        }

        vm.loadData = function (loadProcess) {
            fiveOaAssociationPaymentService.getModelById(paymentId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#paymentTime").datepicker('setDate', vm.item.paymentTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaAssociationPaymentService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            commonFileService.listDataCount(vm.item.businessKey,'-1').then(function (value) {
                if(value.data.data == 0){
                    toastr.warning("请上传缴费通知单!");
                    return ;
                }else{
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        fiveOaAssociationPaymentService.update(vm.item).then(function (value) {
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
            });

        }


        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='handleMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门", userLoginList: vm.item.handleMan,deptIds:"1",multiple:false});
            }else if (vm.status=='changeRecommendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择推荐负责人", type:"部门", userLoginList: vm.item.changeRecommendMan,deptIds:"1",multiple:false});
            }else if (vm.status=='companyLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司领导", type:"部门", userLoginList: vm.item.companyLeader,deptIds:"16",multiple:true,parentDeptId:16});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='handleMan'){
                vm.item.handleMan = $scope.selectedOaUserLogins_;
                vm.item.handleManName = $scope.selectedOaUserNames_;
            }else  if ( vm.status=='changeRecommendMan'){
                vm.item.changeRecommendMan = $scope.selectedOaUserLogins_;
                vm.item.changeRecommendManName = $scope.selectedOaUserNames_;
            }else  if ( vm.status=='companyLeader'){
                vm.item.companyLeader = $scope.selectedOaUserLogins_;
                vm.item.companyLeaderName = $scope.selectedOaUserNames_;
            }
        };

        vm.showAssociation=function(){
            fiveOaAssociationApplyService.listAssociation().then(function (value) {
                if (value.data.ret){
                    vm.listAssociation=value.data.data;
                    singleCheckBox(".cb_association","data-name");
                    $("#selectAssociationModal").modal("show");
                }
            })
        }

        vm.saveAssociation=function(){
            if($(".cb_association:checked").length>0){
                var id=$(".cb_association:checked").first().attr("data-name");

                fiveOaAssociationPaymentService.saveAssociation(id,vm.item.id).then(function (value) {
                    if (value.data.ret){
                        $("#selectAssociationModal").modal("hide");
                        vm.loadData();
                    }
                })
            }


        }

        vm.print=function () {
            fiveOaAssociationPaymentService.getPrintData(paymentId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //协会学会审批  外部专家申请表
    .controller("FiveOaOutSpecialistController", function ($state, $scope,$rootScope, fiveOaOutSpecialistService) {
        var vm = this;
       // var key = $state.current.name + "_" + user.userLogin;
        //vm.params = getCacheParams(key,{dispatchType: "外部专家申请表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaOutSpecialist";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaOutSpecialistService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                  //  setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaOutSpecialistDetail", {outSpecialistId: id});
        };


        vm.add = function () {
            fiveOaOutSpecialistService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaOutSpecialistService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaOutSpecialistService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'name',placeholder:'请输姓名..'},
                    2:{type:"input",colName:'deptName',placeholder:'身份号'},
                    3:{type:"input",colName:'associationType',placeholder:'部门单位'},
                    4:{type:"input",colName:'submitTime'},
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
    .controller("FiveOaOutSpecialistDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaOutSpecialistService,commonFileService) {
        var vm = this;
        var uiSref="five.oaOutSpecialist";
        var outSpecialistId = $stateParams.outSpecialistId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaOutSpecialistService.getModelById(outSpecialistId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#submitTime").datepicker('setDate', vm.item.submitTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaOutSpecialistService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='sendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送审人", type:"部门",userLoginList: vm.item.sendMan,deptIds:"1",multiple:true,parentDeptId:16});
            }else if (vm.status=='drafter'){
                $scope.showOaSelectEmployeeModal_({title:"请选择拟稿人人员", type:"部门",userLoginList: vm.item.drafter,deptIds:"1",multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='sendMan'){
                vm.item.sendMan = $scope.selectedOaUserLogins_;
                vm.item.sendManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='drafter'){
                vm.item.drafter = $scope.selectedOaUserLogins_;
                vm.item.drafterName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            commonFileService.listData(vm.item.businessKey,0).then(function (value) {
                if(value.data.data.length == 0){
                    toastr.warning("请上传相关函件和专家申报表!");
                }else{
                    if ($("#detail_form").validate().form()) {
                        vm.item.operateUserLogin = user.userLogin;
                        fiveOaOutSpecialistService.update(vm.item).then(function (value) {
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
                }
            });
        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择送审部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaOutSpecialistService.getPrintData(outSpecialistId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //协会学会审批   技术培训审批
    .controller("FiveOaGoAbroadTrainAskController", function ($state, $scope,$rootScope, fiveOaGoAbroadTrainAskService) {
        var vm = this;
     //var key = $state.current.name + "_" + user.userLogin;
        vm.params = {qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaGoAbroadTrainAsk";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaGoAbroadTrainAskService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                   // setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaGoAbroadTrainAskDetail", {goAbroadTrainAskId: id});
        };


        vm.add = function () {
            fiveOaGoAbroadTrainAskService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaGoAbroadTrainAskService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaGoAbroadTrainAskService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'trainName',placeholder:'请输培训名称..'},
                    2:{type:"input",colName:'deptName',placeholder:'组织单位'},
                    3:{type:"input",colName:'applyTime',placeholder:'请示时间'},
                   // 4:{type:"input",colName:'recommendManName'},
                    4:{type:"input",colName:'gmtCreate'},
                    5:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:6};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();

        return vm;

    })
    .controller("FiveOaGoAbroadTrainAskDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaGoAbroadTrainAskService) {
        var vm = this;
        var uiSref="five.oaGoAbroadTrainAsk";
        var goAbroadTrainAskId = $stateParams.goAbroadTrainAskId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaGoAbroadTrainAskService.getModelById(goAbroadTrainAskId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applyTime").datepicker('setDate', vm.item.applyTime);
                }
            });
            fiveOaGoAbroadTrainAskService.listDetail(goAbroadTrainAskId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaGoAbroadTrainAskService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='attendUser'){
                $scope.showOaSelectEmployeeModal_({title:"请选择参与人员", userLoginList: vm.detail.attendUser,multiple:true});
            }
        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='attendUser'){
                vm.detail.attendUser = $scope.selectedOaUserLogins_;
                vm.detail.attendUserName = $scope.selectedOaUserNames_;
            }

        };

        vm.showDeptModal=function(id) {
            if(vm.opt=="deptId"){
                $scope.showOaSelectEmployeeModal_({title:"请选择组织单位",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt=="detailDeptId"){
                $scope.showOaSelectEmployeeModal_({title:"请选择参与部门",type:"选部门", deptIdList: vm.detail.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt=="deptId"){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }else if(vm.opt=="detailDeptId"){
                vm.detail.deptName = $scope.selectedOaDeptNames_;
                vm.detail.deptId = Number($scope.selectedOaDeptIds_);
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaGoAbroadTrainAskService.update(vm.item).then(function (value) {
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


        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaGoAbroadTrainAskService.getNewModelDetail(goAbroadTrainAskId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.save();
                    }
                })
            } else {
                fiveOaGoAbroadTrainAskService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.save();
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaGoAbroadTrainAskService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaGoAbroadTrainAskService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    $("#detailModal").modal("hide");
                    fiveOaGoAbroadTrainAskService.listDetail(goAbroadTrainAskId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    })

                }

            })
        };

        vm.print=function () {
            fiveOaGoAbroadTrainAskService.getPrintData(goAbroadTrainAskId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var goAbroadTrainAskDetails = vm.printData.goAbroadTrainAskDetails;
                    vm.printDetails = goAbroadTrainAskDetails;
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

    //协会学会审批  技术培训申报
    .controller("FiveOaGoAbroadTrainDeclareController", function ($state, $scope,$rootScope, fiveOaGoAbroadTrainDeclareService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaGoAbroadTrainDeclare";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qpageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaGoAbroadTrainDeclareService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    //setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaGoAbroadTrainDeclareDetail", {goAbroadTrainDeclareId: id});
        };

        vm.add = function () {
            fiveOaGoAbroadTrainDeclareService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaGoAbroadTrainDeclareService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaGoAbroadTrainDeclareService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'trainName',placeholder:'请输培训名称..'},
                    2:{type:"input",colName:'applyDeptName',placeholder:'申请单位'},
                    3:{type:"input",colName:'trainDeptName',placeholder:'培训单位'},
                    4:{type:"select",colName:'declareTime',placeholder:'培训类型',option:[{title:"全部",value:""},{title:"国内专业技术类培训",value:'国内专业技术类培训'},{title:"国内外部会议交流",value:'国内外部会议交流'},{title:"国外培训",value:'国外培训'}]},
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
    .controller("FiveOaGoAbroadTrainDeclareDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaGoAbroadTrainDeclareService,fiveOaGoAbroadTrainAskService) {
        var vm = this;
        var uiSref="five.oaGoAbroadTrainDeclare";
        var goAbroadTrainDeclareId = $stateParams.goAbroadTrainDeclareId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaGoAbroadTrainDeclareService.getModelById(goAbroadTrainDeclareId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#declareTime").datepicker('setDate', vm.item.declareTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaGoAbroadTrainDeclareService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='attendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择拟派人员",type:"部门", userLoginList: vm.item.attendMan,deptIds:"1",multiple:true});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='attendMan'){
                vm.item.attendMan = $scope.selectedOaUserLogins_;
                vm.item.attendManName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaGoAbroadTrainDeclareService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            if(vm.opt=="apply"){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请单位",type:"选部门", deptIdList: vm.item.applyDeptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt=="train"){
                $scope.showOaSelectEmployeeModal_({title:"请选择培训单位",type:"选部门", deptIdList: vm.item.trainDeptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }

        };
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt=="apply") {
                vm.item.applyDeptName = $scope.selectedOaDeptNames_;
                vm.item.applyDeptId = Number($scope.selectedOaDeptIds_);
            }else if(vm.opt=="train"){
                vm.item.trainDeptName=$scope.selectedOaDeptNames_;
                vm.item.trainDeptId= Number($scope.selectedOaDeptIds_);
            }
        };

        vm.showAssociation=function(){
            fiveOaGoAbroadTrainAskService.listAssociation().then(function (value) {
                if (value.data.ret){
                    vm.listAssociation=value.data.data;
                    singleCheckBox(".cb_association","data-name");
                    $("#selectAssociationModal").modal("show");
                }
            })
        };

        vm.saveAssociation=function(){
            if($(".cb_association:checked").length>0){
                var id=$(".cb_association:checked").first().attr("data-name");
                fiveOaGoAbroadTrainDeclareService.saveAssociation(id,vm.item.id).then(function (value) {
                    if (value.data.ret){
                        $("#selectAssociationModal").modal("hide");
                        vm.loadData();
                    }
                })
            }


        };

        vm.print=function () {
            fiveOaGoAbroadTrainDeclareService.getPrintData(goAbroadTrainDeclareId).then(function (value) {
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
        };


        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //协会学会审批 院刊审查
    .controller("FiveOaDeptJournalController", function ($state, $scope,$rootScope, fiveOaDeptJournalService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaDeptJournal";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaDeptJournalService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaDeptJournalDetail", {journalId: id});
        }

        vm.add = function () {
            fiveOaDeptJournalService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaDeptJournalService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaDeptJournalService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })

        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'manuscriptTitle',placeholder:'请输入稿件题目..'},
                    2:{type:"input",colName:'firstAuthor',placeholder:'第一作者'},
                    3:{type:"input",colName:'readerName',placeholder:'审稿人'},
                    4:{type:"input",colName:'submitTime',placeholder:'投稿日期'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
           /* var option={filterColumns:{
                    1:{type:"input",colName:'manuscriptTitle',placeholder:'请输入稿件题目..'},
                    2:{type:"input",colName:'firstAuthor',placeholder:'第一作者'},
                    3:{type:"input",colName:'readerName',placeholder:'审稿人'},
                    4:{type:"select",colName:'submitTime',placeholder:'投稿日期'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};*/
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.queryData();

        return vm;

    })
    .controller("FiveOaDeptJournalDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaDeptJournalService) {
        var vm = this;
        var uiSref="five.oaDeptJournal";

        var journalId = $stateParams.journalId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.oaDeptJournal");
        }

        vm.loadData = function (loadProcess) {
            fiveOaDeptJournalService.getModelById(journalId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#submitTime").datepicker('setDate', vm.item.submitTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaDeptJournalService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaDeptJournalService.update(vm.item).then(function (value) {
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

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='reader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择审稿人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.reader,multiple:false});
            }else if (vm.status=='deptSecrecyUser'){
                $scope.showOaSelectEmployeeModal_({title:"请选择单位密保人",type:"部门",deptIds:"1",
                    userLoginList: vm.item.deptSecrecyUser,multiple:false});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='reader'){
                vm.item.reader = $scope.selectedOaUserLogins_;
                vm.item.readerName = $scope.selectedOaUserNames_;
            }else if ( vm.status=='deptSecrecyUser'){
                vm.item.deptSecrecyUser = $scope.selectedOaUserLogins_;
                vm.item.deptSecrecyUserName = $scope.selectedOaUserNames_;
            }
        };

        vm.print=function () {
            fiveOaDeptJournalService.getPrintData(journalId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //协会学会审批  专业技术培训申请表
    .controller("FiveOaProfessionalSkillTrainController", function ($state, $scope,$rootScope, fiveOaProfessionalSkillTrainService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        //vm.params = getCacheParams(key,{dispatchType: "专业技术培训申请表", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.params = {qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaProfessionalSkillTrain";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaProfessionalSkillTrainService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    //setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaProfessionalSkillTrainDetail", {professionalSkillTrainId: id});
        };


        vm.add = function () {
            fiveOaProfessionalSkillTrainService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaProfessionalSkillTrainService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaProfessionalSkillTrainService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'applyDeptName',placeholder:'请输入申请单位..'},
                    2:{type:"input",colName:'trainType',placeholder:'培训类别'},
                    3:{type:"input",colName:'trainAddress',placeholder:'培训地点'},
                    4:{type:"input",colName:'trainTime'},//培训时间
                    5:{type:"input",colName:'gmtCreate'},//创建时间
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
    .controller("FiveOaProfessionalSkillTrainDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaProfessionalSkillTrainService) {
        var vm = this;
        var uiSref="five.oaProfessionalSkillTrain";
        var professionalSkillTrainId = $stateParams.professionalSkillTrainId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaProfessionalSkillTrainService.getModelById(professionalSkillTrainId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#trainTime").datepicker('setDate', vm.item.trainTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaProfessionalSkillTrainService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applyMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",deptIds:"1",userLoginList: vm.item.applyMan,multiple:true});
            }else if (vm.status=='userMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择设备使用人", type:"部门",deptIds:"1",userLoginList: vm.item.userMan,multiple:false});
            }else if (vm.status=='examineMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择验收人", type:"部门",deptIds:"1",userLoginList: vm.item.examineMan,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='applyMan'){
                vm.item.applyMan = $scope.selectedOaUserLogins_;
                vm.item.applyManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='userMan'){
                vm.item.user = $scope.selectedOaUserLogins_;
                vm.item.userName = $scope.selectedOaUserNames_;
            }else if (vm.status=='examineMan'){
                vm.item.examineMan = $scope.selectedOaUserLogins_;
                vm.item.examineManName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaProfessionalSkillTrainService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择申请单位",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.applyDeptName = $scope.selectedOaDeptNames_;
            vm.item.applyDeptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaProfessionalSkillTrainService.getPrintData(professionalSkillTrainId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })
    
    //协会学会审批  参加外部技术交流会议审批
    .controller("FiveOaOutTechnicalExchangeController", function ($state, $scope, $rootScope,fiveOaOutTechnicalExchangeService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        vm.params = {qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaOutTechnicalExchange";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaOutTechnicalExchangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                   // setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaOutTechnicalExchangeDetail", {outTechnicalExchangeId: id});
        };


        vm.add = function () {
            fiveOaOutTechnicalExchangeService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaOutTechnicalExchangeService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaOutTechnicalExchangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'meetName',placeholder:'请输会议名称..'},
                    2:{type:"input",colName:'meetNotic',placeholder:'会议通知'},
                    3:{type:"select",colName:'attend',placeholder:'是否派人参加',option:[{title:"全部",value:""},{title:"是",value:'是'},{title:"否",value:'否'}]},
                    4:{type:"input",colName:'attendManName',placeholder:'拟派人员'},
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
    .controller("FiveOaOutTechnicalExchangeDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaOutTechnicalExchangeService) {
        var vm = this;
        var uiSref="five.oaOutTechnicalExchange";
        var outTechnicalExchangeId = $stateParams.outTechnicalExchangeId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaOutTechnicalExchangeService.getModelById(outTechnicalExchangeId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#trainTime").datepicker('setDate', vm.item.trainTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaOutTechnicalExchangeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='attendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择拟派人员", type:"部门",deptIds:"1",userLoginList: vm.item.attendMan,multiple:true});
            }else if (vm.status=='userMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择设备使用人", type:"部门",deptIds:"1",userLoginList: vm.item.userMan,multiple:false});
            }else if (vm.status=='examineMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择验收人", type:"部门",deptIds:"1",userLoginList: vm.item.examineMan,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='attendMan'){
                vm.item.attendMan = $scope.selectedOaUserLogins_;
                vm.item.attendManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='userMan'){
                vm.item.user = $scope.selectedOaUserLogins_;
                vm.item.userName = $scope.selectedOaUserNames_;
            }else if (vm.status=='examineMan'){
                vm.item.examineMan = $scope.selectedOaUserLogins_;
                vm.item.examineManName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaOutTechnicalExchangeService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.applyDeptName = $scope.selectedOaDeptNames_;
            vm.item.applyDeptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaOutTechnicalExchangeService.getPrintData(outTechnicalExchangeId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //协会学会审批  对外发表论文审查
    .controller("FiveOaTechnologyArticleExamineController", function ($state, $scope,$rootScope, fiveOaTechnologyArticleExamineService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaTechnologyArticleExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {

            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaTechnologyArticleExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaTechnologyArticleExamineDetail", {technologyArticleExamineId: id});
        };

        vm.add = function () {
            fiveOaTechnologyArticleExamineService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaTechnologyArticleExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaTechnologyArticleExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'title',placeholder:'请输入论文标题...'},
                    2:{type:"input",colName:'author',placeholder:'作者姓名'},
                    3:{type:"input",colName:'deptName',placeholder:'作者单位'},
                    4:{type:"input",colName:'authorLink'},
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
    .controller("FiveOaTechnologyArticleExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaTechnologyArticleExamineService) {
        var vm = this;
        var uiSref="five.oaTechnologyArticleExamine";
        var technologyArticleExamineId = $stateParams.technologyArticleExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaTechnologyArticleExamineService.getModelById(technologyArticleExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#trainTime").datepicker('setDate', vm.item.trainTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaTechnologyArticleExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='author'){
                $scope.showOaSelectEmployeeModal_({title:"请选择作者",
                    type:"部门",deptIds:"1",userLoginList: vm.item.author,multiple:true});
            }else if (vm.status=='deptChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门负责人",
                    type:"部门",deptIds:"1",userLoginList: vm.item.deptChargeMan,multiple:false,parentDeptId:user.deptId});
            }else if (vm.status=='technologyChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择信息化建设与管理部人",
                    type:"部门",deptIds:"11", userLoginList: vm.item.examineMan,multiple:false,parentDeptId:11});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='author'){
                vm.item.author = $scope.selectedOaUserLogins_;
                vm.item.authorName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMan'){
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='technologyChargeMan'){
                vm.item.technologyChargeMan = $scope.selectedOaUserLogins_;
                vm.item.technologyChargeManName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaTechnologyArticleExamineService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择作者单位",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaTechnologyArticleExamineService.getPrintData(technologyArticleExamineId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //协会学会审批  压力管道设计资格印章申请
    .controller("FiveOaPressurePipSealExamineController", function ($state, $scope,$rootScope, fiveOaPressurePipSealExamineService) {
        var vm = this;
       // var key = $state.current.name + "_" + user.userLogin;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaPressurePipSealExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaPressurePipSealExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
            //        setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaPressurePipSealExamineDetail", {pressurePipSealExamineId: id});
        };


        vm.add = function () {
            fiveOaPressurePipSealExamineService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaPressurePipSealExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaPressurePipSealExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"select",colName:'seal',placeholder:'印章',option:[{title:"全部",value:""},{title:"线上",value:'线上'},{title:"线下",value:'线下'}]},
                    2:{type:"input",colName:'applyMan',placeholder:'申请人'},
                    3:{type:"input",colName:'deptName',placeholder:'用印单位'},
                    4:{type:"input",colName:'applyManLink',placeholder:'联系电话'},
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
    .controller("FiveOaPressurePipSealExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaPressurePipSealExamineService) {
        var vm = this;
        var uiSref="five.oaPressurePipSealExamine";
        var pressurePipSealExamineId = $stateParams.pressurePipSealExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaPressurePipSealExamineService.getModelById(pressurePipSealExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#useTime").datepicker('setDate', vm.item.useTime);
                    $("#drawCompleteTime").datepicker('setDate', vm.item.drawCompleteTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaPressurePipSealExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applyMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选 择申请人",
                    type:"部门",deptIds:"1",userLoginList: vm.item.applyMan,multiple:true});
            }else if (vm.status=='deptChargeMan'){
                if(vm.item.deptId!=0||vm.item.deptId!=''){
                    $scope.showOaSelectEmployeeModal_({title:"请选 择部门负责人",
                        type:"部门",deptIds:""+vm.item.deptId,userLoginList: vm.item.deptChargeMan,multiple:false});
                }else {
                    toastr.error("请先选择用印单位")
                }
            }else if (vm.status=='technologyChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择 信息化建设与管理部人",
                    type:"部门",deptIds:"11",userLoginList: vm.item.examineMan,multiple:false});
            }
            else if (vm.status=='pressurePipChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择 压力管道设计技术负责人",
                    type:"部门",deptIds:"1", userLoginList: vm.item.pressurePipChargeMan,multiple:false});
            }else if (vm.status=='sealMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择 盖章人",
                    type:"部门",deptIds:"1", userLoginList: vm.item.sealMan,multiple:false});
            }else if (vm.status=='superviseMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择 监章人",
                    type:"部门",deptIds:"1", userLoginList: vm.item.superviseMan,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='applyMan'){
                vm.item.applyMan = $scope.selectedOaUserLogins_;
                vm.item.applyManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMan'){
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='technologyChargeMan'){
                vm.item.technologyChargeMan = $scope.selectedOaUserLogins_;
                vm.item.technologyChargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='pressurePipChargeMan'){
                vm.item.pressurePipChargeMan = $scope.selectedOaUserLogins_;
                vm.item.pressurePipChargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='sealMan'){
                vm.item.sealMan = $scope.selectedOaUserLogins_;
                vm.item.sealManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='superviseMan'){
                vm.item.superviseMan = $scope.selectedOaUserLogins_;
                vm.item.superviseManName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaPressurePipSealExamineService.update(vm.item).then(function (value) {
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


        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaPressurePipSealExamineService.getPrintData(pressurePipSealExamineId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })


    //科研审批流程  非独立运行中心设立申请
    .controller("FiveOaNonIndependentDeptSetController", function ($state, $scope,$rootScope, fiveOaNonIndependentDeptSetService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        vm.params = {qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaNonIndependentDeptSet";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaNonIndependentDeptSetService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                //    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaNonIndependentDeptSetDetail", {nonIndependentDeptSetId: id});
        };


        vm.add = function () {
            fiveOaNonIndependentDeptSetService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaNonIndependentDeptSetService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaNonIndependentDeptSetService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'centerName',placeholder:'请输入中心名称..'},
                    2:{type:"input",colName:'deptName',placeholder:'依托单位'},
                    3:{type:"input",colName:'researchDirection',placeholder:'研究方向'},
                    4:{type:"input",colName:'linkMan',placeholder:'联系人'},
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
    .controller("FiveOaNonIndependentDeptSetDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaNonIndependentDeptSetService) {
        var vm = this;
        var uiSref="five.oaNonIndependentDeptSet";
        var nonIndependentDeptSetId = $stateParams.nonIndependentDeptSetId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaNonIndependentDeptSetService.getModelById(nonIndependentDeptSetId).then(function (value) {
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
            fiveOaNonIndependentDeptSetService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='linkMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择联系人", type:"部门",deptIds:"1",userLoginList: vm.item.linkMan,multiple:true,parentDeptId:16});
            }else if (vm.status=='deptChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择依托部门负责人",type:"部门",deptIds:"1", userLoginList: vm.item.deptChargeMan,multiple:false});
            }
            else if (vm.status=='technologyDeptMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择信息化建设与管理部负责人", type:"部门",deptIds:"1",userLoginList: vm.item.technologyDeptMan,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='linkMan'){
                vm.item.linkMan = $scope.selectedOaUserLogins_;
                vm.item.linkManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMan'){
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='technologyDeptMan'){
                vm.item.technologyDeptMan = $scope.selectedOaUserLogins_;
                vm.item.technologyDeptManName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaNonIndependentDeptSetService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {

            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});

        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaNonIndependentDeptSetService.getPrintData(nonIndependentDeptSetId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //科研审批流程-外部科研项目申请
    .controller("FiveOaExternalResearchProjectApplyController", function ($state, $scope,$rootScope, fiveOaExternalResearchProjectApplyService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaExternalResearchProjectApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime:vm.params.startTime,endTime:vm.params.endTime
            });
            fiveOaExternalResearchProjectApplyService.downTempleXls(params).then(function (response) {

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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaExternalResearchProjectApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaExternalResearchProjectApplyDetail", {projectId: id});
        }

        vm.add = function () {
            fiveOaExternalResearchProjectApplyService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaExternalResearchProjectApplyService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaExternalResearchProjectApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'projectName',placeholder:'请输入课题名称..'},
                    2:{type:"input",colName:'commencementDate',placeholder:'开工日期'},
                    3:{type:"input",colName:'deptName',placeholder:'单位'},
                    4:{type:"input",colName:'taskChargerName',placeholder:'课题负责人'},
                    5:{type:"input",colName:'mainParticipantName',placeholder:'主要参加人'},
                    6:{type:"input",colName:'outsidePayment',placeholder:'外拨款（万元）'},
                    7:{type:"input",colName:'yearExceptPayment',placeholder:'本年度预计拨付（万元）'},
                    8:{type:"input",colName:'yearRepayment',placeholder:'本年度还款（万元）'},
                    9:{type:"input",colName:'gmtCreate'},
                    10:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:11};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();

        return vm;

    })
    .controller("FiveOaExternalResearchProjectApplyDetailController", function ($state,$stateParams,$rootScope,$scope,commonPrintTableService,fiveOaExternalResearchProjectApplyService) {
        var vm = this;
        var uiSref="five.oaExternalResearchProjectApply";
        var projectId = $stateParams.projectId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);

        }
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='taskCharger'){
                $scope.showOaSelectEmployeeModal_({title:"课题负责人",type:"部门",deptIds:"1", userLoginList: vm.item.taskCharger,multiple:false});
            } else if (vm.status=='mainParticipant'){
                $scope.showOaSelectEmployeeModal_({title:"主要参加人",type:"部门",deptIds:"1", userLoginList: vm.item.mainParticipant,multiple:true});
            }
        };
        //保存选人的login和名字
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='taskCharger'){
                vm.item.taskCharger = $scope.selectedOaUserLogins_;
                vm.item.taskChargerName = $scope.selectedOaUserNames_;
            }else if (vm.status=='mainParticipant'){
                vm.item.mainParticipant = $scope.selectedOaUserLogins_;
                vm.item.mainParticipantName = $scope.selectedOaUserNames_;
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
            fiveOaExternalResearchProjectApplyService.getModelById(projectId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }

                    $("#commencementDate").datepicker('setDate', vm.item.commencementDate);
                    $("#completionDate").datepicker('setDate',vm.item.completionDate);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaExternalResearchProjectApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                if (!vm.item.secret) {
                    toastr.warning("请确认项目是否脱密，本平台是非密平台！")
                } else {
                    fiveOaExternalResearchProjectApplyService.update(vm.item).then(function (value) {
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
                }
            }
            else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.init();

        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;

                    lodop.PRINT_INIT("内部科研项目申请");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //科研审批流程-外部标准规范、图集项目申请
    .controller("FiveOaExternalStandardApplyController", function ($state, $scope,$rootScope, fiveOaExternalStandardApplyService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaExternalStandardApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime:vm.params.startTime,endTime:vm.params.endTime
            });
            fiveOaExternalStandardApplyService.downTempleXls(params).then(function (response) {

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

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaExternalStandardApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaExternalStandardApplyDetail", {externalStandardApplyId: id});
        }

        vm.add = function () {
            fiveOaExternalStandardApplyService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaExternalStandardApplyService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaScientificTaskCostApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'taskName',placeholder:'请输入课题名称..'},
                    2:{type:"input",colName:'commencementDate',placeholder:'开工日期'},
                    3:{type:"input",colName:'deptName',placeholder:'单位'},
                    4:{type:"input",colName:'taskChargerName',placeholder:'课题负责人'},
                    5:{type:"input",colName:'mainParticipantName',placeholder:'主要参加人'},
                    6:{type:"input",colName:'outsidePayment',placeholder:'外拨款（万元）'},
                    7:{type:"input",colName:'yearExceptPayment',placeholder:'本年度预计拨付（万元）'},
                    8:{type:"input",colName:'gmtCreate'},
                    9:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:10};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();

        return vm;

    })
    .controller("FiveOaExternalStandardApplyDetailController", function ($state,$stateParams,$rootScope,$scope,commonPrintTableService,fiveOaExternalStandardApplyService) {
        var vm = this;
        var uiSref="five.oaExternalStandardApply";
        var externalStandardApplyId = $stateParams.externalStandardApplyId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        }
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='taskCharger'){
                $scope.showOaSelectEmployeeModal_({title:"课题负责人",type:"部门",deptIds:"1", userLoginList: vm.item.taskCharger,multiple:false});
            } else if (vm.status=='mainParticipant'){
                $scope.showOaSelectEmployeeModal_({title:"主要参加人",type:"部门",deptIds:"1", userLoginList: vm.item.mainParticipant,multiple:true});
            }else if (vm.status=='scientificFirstTrial'){
                $scope.showOaSelectEmployeeModal_({title:"专家委员会常委",type:"角色",roleIds:"132", userLoginList: vm.item.scientificFirstTrial,multiple:true,parentRoleId:'132'});
            }
        };
        //保存选人的login和名字
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='taskCharger'){
                vm.item.taskCharger = $scope.selectedOaUserLogins_;
                vm.item.taskChargerName = $scope.selectedOaUserNames_;
            }else if (vm.status=='mainParticipant'){
                vm.item.mainParticipant = $scope.selectedOaUserLogins_;
                vm.item.mainParticipantName = $scope.selectedOaUserNames_;
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
            fiveOaExternalStandardApplyService.getModelById(externalStandardApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }


                    $("#commencementDate").datepicker('setDate',vm.item.commencementDate);
                    $("#completionDate").datepicker('setDate', vm.item.completionDate);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaExternalStandardApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;

                if (!vm.item.secret){
                    toastr.warning("请确认项目是否脱密，本平台是非密平台！")

                }else {
                    fiveOaExternalStandardApplyService.update(vm.item).then(function (value) {
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
                }

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

                    lodop.PRINT_INIT("内部科研项目申请");
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

    //科研审批流程  内部科研项目申请
    .controller("FiveOaInlandProjectApplyController", function ($state, $scope, $rootScope,fiveOaInlandProjectApplyService) {
    var vm = this;
    vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
    vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
    var uiSref="five.oaInlandProjectApply";
    var tableName = $rootScope.loadTableName(uiSref);

    vm.downExcel=function(){
        var params = $.extend(tablefilter.params, {
            qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime,endTime1:vm.params.endTime
        });
        fiveOaInlandProjectApplyService.downTempleXls(params).then(function (response) {

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
        fiveOaInlandProjectApplyService.listPagedData(params).then(function (value) {
            if (value.data.ret) {
                vm.pageInfo = value.data.data;
            }
        })
    };

    vm.show = function (id) {
        $state.go("five.oaInlandProjectApplyDetail", {applyId: id});
    }

    vm.add = function () {
        fiveOaInlandProjectApplyService.getNewModel( user.userLogin).then(function (value) {
            if (value.data.ret) {
                vm.show(value.data.data);
            }
        })
    }

    vm.remove = function (id) {
        bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
            if (result) {
                fiveOaInlandProjectApplyService.remove(id, user.userLogin).then(function (value) {
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
        fiveOaInlandProjectApplyService.listPagedData(params).then(function (value) {
            if (value.data.ret) {
                vm.pageInfo = value.data.data;
            }
        })
    };

    $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
        var option={filterColumns:{
                1:{type:"input",colName:'taskName',placeholder:'请输入课题名称..'},
                2:{type:"input",colName:'commencementDate',placeholder:'开工日期'},
                3:{type:"input",colName:'deptName',placeholder:'单位'},
                4:{type:"input",colName:'chargeMenName',placeholder:'课题负责人'},
                5:{type:"input",colName:'attenderName',placeholder:'主要参加人'},
                6:{type:"input",colName:'totalPrice',placeholder:'合计'},
                7:{type:"select",colName:'projectType',option:[{title:"全部",value:""},{title:"公司级",value:'公司级'},{title:"自主开发",value:'自主开发'}]},
                8:{type:"input",colName:'gmtCreate'},
                9:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                //注：当type为select时 会根据option创建下拉列表 option中
            },handleColumn:10};
        tablefilter.queryFunction=vm.fuzzySearch;
        tablefilter.params=vm.params;
        tablefilter.initializeFilter(option);
    });
    vm.queryData();

    return vm;

})
    .controller("FiveOaInlandProjectApplyDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaInlandProjectApplyService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaInlandProjectApply";
        var applyId = $stateParams.applyId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        }
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"课题负责人",type:"部门",deptIds:"1", userLoginList: vm.item.chargeMen,multiple:false});
            } else if (vm.status=='attender'){
                $scope.showOaSelectEmployeeModal_({title:"主要参加人",type:"部门",deptIds:"1", userLoginList: vm.item.attender,multiple:true});
            }else if (vm.status=='scientificFirstTrial'){
                $scope.showOaSelectEmployeeModal_({title:"专家委员会常委",type:"角色",roleIds:"132", userLoginList: vm.item.scientificFirstTrial,multiple:true,parentRoleId:'132'});
            }
        };
        //保存选人的login和名字
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='chargeMen'){
                vm.item.chargeMen = $scope.selectedOaUserLogins_;
                vm.item.chargeMenName = $scope.selectedOaUserNames_;
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
            fiveOaInlandProjectApplyService.getModelById(applyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }


                    $("#commencementDate").datepicker('setDate',vm.item.commencementDate);
                    $("#completionDate").datepicker('setDate', vm.item.completionDate);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaInlandProjectApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }



        vm.countTotalPrice =function(){
            vm.item.materialsCost = vm.item.materialsCost==null? 0 : parseFloat(vm.item.materialsCost);
            vm.item.appropriativeCost = vm.item.appropriativeCost==null? 0 : parseFloat(vm.item.appropriativeCost);
            vm.item.outsourceCost = vm.item.outsourceCost==null? 0 : parseFloat(vm.item.outsourceCost);
            vm.item.meetingCost = vm.item.meetingCost==null? 0 : parseFloat(vm.item.meetingCost);
            vm.item.travelCost = vm.item.travelCost==null? 0 : parseFloat(vm.item.travelCost);
            vm.item.specialistCost = vm.item.specialistCost==null? 0 : parseFloat(vm.item.specialistCost);
            vm.item.fixeAssetDepreciationCost = vm.item.fixeAssetDepreciationCost==null? 0 : parseFloat(vm.item.fixeAssetDepreciationCost);
            vm.item.fuelPowerCost = vm.item.fuelPowerCost==null? 0 : parseFloat(vm.item.fuelPowerCost);
            vm.item.salaryServiceCost = vm.item.salaryServiceCost==null? 0 : parseFloat(vm.item.salaryServiceCost);
            vm.item.totalPrice= (vm.item.materialsCost+vm.item.appropriativeCost+vm.item.outsourceCost+vm.item.meetingCost
            +vm.item.travelCost+vm.item.specialistCost+vm.item.fixeAssetDepreciationCost+vm.item.fuelPowerCost+vm.item.salaryServiceCost).toFixed(6);
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;

                if (!vm.item.secret){
                    toastr.warning("请确认项目是否脱密，本平台是非密平台！")

                }else {
                    fiveOaInlandProjectApplyService.update(vm.item).then(function (value) {
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
                }

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

                    lodop.PRINT_INIT("内部科研项目申请");
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
    //科研审批流程  科研项目库
    .controller("FiveOaResearchProjectLibraryController", function ($state, $scope,$rootScope,fiveOaResearchProjectLibraryService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaResearchProjectLibrary";
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
            fiveOaResearchProjectLibraryService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaResearchProjectLibraryDetail", {libraryId: id});
        }

        vm.add = function () {
            fiveOaResearchProjectLibraryService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaResearchProjectLibraryService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaResearchProjectLibraryService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'taskName',placeholder:'请输入课题名称..'},
                    2:{type:"input",colName:'commencementDate',placeholder:'开工日期'},
                    3:{type:"input",colName:'deptName',placeholder:'单位'},
                    4:{type:"input",colName:'chargeMenName',placeholder:'课题负责人'},
                    5:{type:"input",colName:'attenderName',placeholder:'主要参加人'},
                    6:{type:"input",colName:'totalPrice',placeholder:'合计'},
                    7:{type:"select",colName:'projectType',option:[{title:"内部科研项目",value:'内部科研项目'},{title:"外部科研项目",value:'外部科研项目'},{title:"外部标准规范图集项目",value:'外部标准规范图集项目'}]},
                    8:{type:"input",colName:'gmtCreate'},
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:9};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();

        return vm;

    })
    .controller("FiveOaResearchProjectLibraryDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaResearchProjectLibraryService,commonPrintTableService) {
        var vm = this;
        var libraryId = $stateParams.libraryId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        }
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"课题负责人",type:"部门",deptIds:"1", userLoginList: vm.item.chargeMen,multiple:false});
            } else if (vm.status=='attender'){
                $scope.showOaSelectEmployeeModal_({title:"主要参加人",type:"部门",deptIds:"1", userLoginList: vm.item.attender,multiple:true});
            }else if (vm.status=='scientificFirstTrial'){
                $scope.showOaSelectEmployeeModal_({title:"专家委员会常委",type:"角色",roleIds:"132", userLoginList: vm.item.scientificFirstTrial,multiple:true,parentRoleId:'132'});
            }
        };
        //保存选人的login和名字
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='chargeMen'){
                vm.item.chargeMen = $scope.selectedOaUserLogins_;
                vm.item.chargeMenName = $scope.selectedOaUserNames_;
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
            fiveOaResearchProjectLibraryService.getModelById(libraryId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }

                    $("#commencementDate").datepicker('setDate',vm.item.commencementDate);
                    $("#completionDate").datepicker('setDate', vm.item.completionDate);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaResearchProjectLibraryService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        }

        vm.countTotalPrice =function(){
            vm.item.materialsCost = vm.item.materialsCost==null? 0 : parseFloat(vm.item.materialsCost);
            vm.item.appropriativeCost = vm.item.appropriativeCost==null? 0 : parseFloat(vm.item.appropriativeCost);
            vm.item.outsourceCost = vm.item.outsourceCost==null? 0 : parseFloat(vm.item.outsourceCost);
            vm.item.meetingCost = vm.item.meetingCost==null? 0 : parseFloat(vm.item.meetingCost);
            vm.item.travelCost = vm.item.travelCost==null? 0 : parseFloat(vm.item.travelCost);
            vm.item.specialistCost = vm.item.specialistCost==null? 0 : parseFloat(vm.item.specialistCost);
            vm.item.fixeAssetDepreciationCost = vm.item.fixeAssetDepreciationCost==null? 0 : parseFloat(vm.item.fixeAssetDepreciationCost);
            vm.item.fuelPowerCost = vm.item.fuelPowerCost==null? 0 : parseFloat(vm.item.fuelPowerCost);
            vm.item.salaryServiceCost = vm.item.salaryServiceCost==null? 0 : parseFloat(vm.item.salaryServiceCost);
            vm.item.totalPrice= (vm.item.materialsCost+vm.item.appropriativeCost+vm.item.outsourceCost+vm.item.meetingCost
                +vm.item.travelCost+vm.item.specialistCost+vm.item.fixeAssetDepreciationCost+vm.item.fuelPowerCost+vm.item.salaryServiceCost).toFixed(6);
        }

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("科研项目库");
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

        return vm;

    })

    //科研审批流程  科技开发费项目评审
    .controller("FiveOaResearchProjectReviewController", function ($state, $scope,$rootScope, fiveOaResearchProjectReviewService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime:'',endTime:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaResearchProjectReview";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime,endTime1:vm.params.endTime
            });
            fiveOaResearchProjectReviewService.downTempleXls(params).then(function (response) {

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
            fiveOaResearchProjectReviewService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaResearchProjectReviewDetail", {projectId: id});
        }

        vm.add = function () {
            fiveOaResearchProjectReviewService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }


        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaResearchProjectReviewService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaResearchProjectReviewService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'researchProjectName',placeholder:'请输入科研项目名称..'},
                    2:{type:"input",colName:'deptName',placeholder:'单位'},
                    3:{type:"input",colName:'expertName',placeholder:'专家'},
                    4:{type:"input",colName:'remark',placeholder:'备注'},
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
    .controller("FiveOaResearchProjectReviewDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaResearchProjectReviewService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaResearchProjectReviewDetail";
        var projectId = $stateParams.projectId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        }
        //选人模块
        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='expert'){
                $scope.showOaSelectEmployeeModal_({title:"请选择专家",type:"部门",deptIds:"1", userLoginList: vm.item.expert,multiple:true});
            } else if (vm.status=='attender'){
                $scope.showOaSelectEmployeeModal_({title:"主要参加人",type:"部门",deptIds:"1", userLoginList: vm.item.attender,multiple:true});
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
            fiveOaResearchProjectReviewService.getModelById(projectId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            });
            vm.loadDetail();
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaResearchProjectReviewService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(true);
                }
            })
        };

        vm.loadDetail=function(){
            fiveOaResearchProjectReviewService.listDetail(projectId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaResearchProjectReviewService.getNewModelDetail(projectId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaResearchProjectReviewService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaResearchProjectReviewService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaResearchProjectReviewService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadDetail();
                }

            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("科研项目库");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };


        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaResearchProjectReviewService.update(vm.item).then(function (value) {
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
        };

        vm.init();

        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //科研审批流程 专利申请
    .controller("FiveOaInventApplyController", function ($state, $scope,$rootScope, hrInventService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaInventApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            hrInventService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaInventApplyDetail", {inventId: id});
        }

        vm.add = function () {
            hrInventService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrInventService.remove(id, user.userLogin).then(function (value) {
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
            hrInventService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'inventName',placeholder:'请输入发明名称..'},
                    2:{type:"input",colName:'firstInventManName',placeholder:'第一发明人'},
                    3:{type:"input",colName:'deptName',placeholder:'申报单位'},
                    4:{type:"select",colName:'majorName',placeholder:'所属专业',option:[{title:"全部",value:""},{title:"总设计师",value:'总设计师'},{title:"总图",value:'总图'},{title:"工艺",value:'工艺'},{title:"AC",value:'AC'},{title:"GS",value:'GS'},{title:"给排水",value:'给排水'},{title:"暖通",value:'暖通'},{title:"热机",value:'热机'},{title:"电气",value:'电气'},{title:"自控",value:'自控'},{title:"设备",value:'设备'},{title:"电信",value:'电信'},{title:"环卫工艺",value:'环卫工艺'},{title:"技经",value:'技经'}]},
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
    .controller("FiveOaInventApplyDetailController", function ($state,$stateParams,$rootScope,$scope,hrInventService,commonCodeService) {
        var vm = this;
        var uiSref="five.oaInventApply";

        var inventId = $stateParams.inventId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            hrInventService.getModelById(inventId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applyTime").datepicker('setDate', vm.item.applyTime);

                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrInventService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                hrInventService.update(vm.item).then(function (value) {
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
        //选择专业
        vm.showMajorModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"设计专业").then(function (value) {
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
            vm.item.majorName = value.join(',');

            $("#selectMajorModal").modal("hide");
        }

        vm.print=function () {
            hrInventService.getPrintData(inventId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //科研审批流程  科研课题费用申报
    .controller("FiveOaScientificTaskCostApplyController", function ($state, $scope,$rootScope, fiveOaScientificTaskCostApplyService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        vm.params ={qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaScientificTaskCostApply";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaScientificTaskCostApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                   // setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaScientificTaskCostApplyDetail", {scientificTaskCostApplyId: id});
        };


        vm.add = function () {
            fiveOaScientificTaskCostApplyService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaScientificTaskCostApplyService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaScientificTaskCostApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'inventName',placeholder:'请输入课题名称..'},
                    2:{type:"input",colName:'firstInventManName',placeholder:'课题编号'},
                    3:{type:"input",colName:'deptName',placeholder:'经费用途'},
                    4:{type:"input",colName:'majorName',placeholder:'使用时间'},
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
    .controller("FiveOaScientificTaskCostApplyDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaScientificTaskCostApplyService,hrDeptService) {
        var vm = this;
        var uiSref="five.oaScientificTaskCostApply";
        var scientificTaskCostApplyId = $stateParams.scientificTaskCostApplyId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        };

        vm.loadData = function (loadProcess) {
            fiveOaScientificTaskCostApplyService.getModelById(scientificTaskCostApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#costUseTime").datepicker('setDate', vm.item.costUseTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaScientificTaskCostApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='taskChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择课题负责人", type:"部门",deptIds:"1",userLoginList: vm.item.taskChargeMan,multiple:true,parentDeptId:16});
            } else if (vm.status=='deptChargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请单位负责人",type:"部门",deptIds:"1", userLoginList: vm.item.deptChargeMen,multiple:false});
            }else if (vm.status=='chargeLeaderMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择主管领导", type:"部门",deptIds:"1",userLoginList: vm.item.chargeLeaderMan,multiple:false});
            }else if (vm.status=='technologyMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择信息化建设与管理部负责人", type:"部门",deptIds:"1",userLoginList: vm.item.technologyMan,multiple:false});
            }else if (vm.status=='totalAccountantMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择总会计师",type:"部门",deptIds:"1", userLoginList: vm.item.totalAccountantMen,multiple:false});
            }else if (vm.status=='totalManagerMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择总经理", type:"部门",deptIds:"1",userLoginList: vm.item.totalManagerMen,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='taskChargeMan'){
                vm.item.taskChargeMan = $scope.selectedOaUserLogins_;
                vm.item.taskChargeManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMen'){
                vm.item.deptChargeMen = $scope.selectedOaUserLogins_;
                vm.item.deptChargeMenName = $scope.selectedOaUserNames_;
            }else if (vm.status=='chargeLeaderMan'){
                vm.item.chargeLeaderMan = $scope.selectedOaUserLogins_;
                vm.item.chargeLeaderManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='technologyMan'){
                vm.item.technologyMan = $scope.selectedOaUserLogins_;
                vm.item.technologyManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='totalAccountantMen'){
                vm.item.totalAccountantMen = $scope.selectedOaUserLogins_;
                vm.item.totalAccountantMenName = $scope.selectedOaUserNames_;
            }else if (vm.status=='totalManagerMen'){
                vm.item.totalManagerMen = $scope.selectedOaUserLogins_;
                vm.item.totalManagerMenName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaScientificTaskCostApplyService.update(vm.item).then(function (value) {
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


        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.showDeptDetailModal=function(id) {
            $('#dept_select_tree_detail').jstree("destroy");
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

                $('#dept_select_tree_detail').jstree({
                    'core': {
                        'data': vm.treeData
                    }
                });
            });
            $("#deptSelectDetailModal").modal("show");
        };

        vm.saveDetailDept=function() {
            var dept = $('#dept_select_tree_detail').jstree(true).get_selected(true)[0];
            vm.detail.deptId= dept.id;
            vm.detail.deptName=dept.text;
            $("#deptSelectDetailModal").modal("hide");
        };

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaScientificTaskCostApplyService.getNewModelDetail(scientificTaskCostApplyId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaScientificTaskCostApplyService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            }
        };

        vm.loadDetail=function(){
            fiveOaScientificTaskCostApplyService.listDetail(scientificTaskCostApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaScientificTaskCostApplyService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaScientificTaskCostApplyService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadDetail();
                }

            })
        };

        vm.print=function () {
            fiveOaScientificTaskCostApplyService.getPrintData(scientificTaskCostApplyId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var scientificTaskCostApplyDetails = vm.printData.scientificTaskCostApplyDetails;
                    vm.printDetails = scientificTaskCostApplyDetails;

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

    //科研审批流程-专利缴费申请
    .controller("FiveOaInventPaymentController", function ($state, $scope,$rootScope, fiveOaInventPaymentService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaInventPayment";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaInventPaymentService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaInventPaymentDetail", {paymentId: id});
        }

        vm.add = function () {
            fiveOaInventPaymentService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaInventPaymentService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaScientificTaskCostApplyService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'请输入专利产生、使用单位..'},
                    2:{type:"input",colName:'paymentTime',placeholder:'日期'},
                    3:{type:"input",colName:'gmtCreate'},
                    4:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:5};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();

        return vm;

    })
    .controller("FiveOaInventPaymentDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaInventPaymentService) {
        var vm = this;
        var uiSref="five.oaInventPayment";

        var paymentId = $stateParams.paymentId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            vm.listDetail();

        }

        vm.loadData = function (loadProcess) {
            fiveOaInventPaymentService.getModelById(paymentId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#paymentTime").datepicker('setDate', vm.item.paymentTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaInventPaymentService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaInventPaymentService.update(vm.item).then(function (value) {
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

        vm.addDetail=function(){
            fiveOaInventPaymentService.getNewModelDetail(paymentId).then(function (value) {
                if (value.data.ret){
                    vm.editDetail(value.data.data);
                }
            })
        }

        vm.updateDetail=function(){
            fiveOaInventPaymentService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret){
                    $("#detailModel").modal("hide");
                    vm.listDetail();
                }
            })
        }

        vm.removeDetail=function(id){
            fiveOaInventPaymentService.removeDetail(id).then(function (value) {
                if (value.data.ret){
                    toastr.success("删除成功!") ;
                    vm.listDetail();
                }
            })
        }

        vm.editDetail=function(id){
            fiveOaInventPaymentService.getModelDetailById(id).then(function (value) {
                if (value.data.ret){
                    vm.detail=value.data.data;
                    $("#detailModel").modal("show");
                }
            })
        }

        vm.listDetail=function(){
            fiveOaInventPaymentService.listDetail(paymentId).then(function (value) {
                if (value.data.ret){
                    vm.detalList=value.data.data;
                }
            })
        }

        vm.print=function () {
            fiveOaInventPaymentService.getPrintData(paymentId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");

                    var paymentDetails = vm.printData.paymentDetails;
                    vm.printDetails = paymentDetails;

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //档案申请  档案资料借阅/电子复制申请表
    .controller("FiveOaMaterialBorrowController", function ($state, $scope,$rootScope, fiveOaMaterialBorrowService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        vm.params = {qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaMaterialBorrow";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);//tth添加0125
            vm.loadPagedData();
            $scope.basicInit("");
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaMaterialBorrowService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    //setCacheParams(key,vm.params,vm.pageInfo);
                }
            });
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaMaterialBorrowDetail", {materialBorrowId: id});
        };

        vm.add = function () {
            fiveOaMaterialBorrowService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaMaterialBorrowService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaMaterialBorrowService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'applicantName',placeholder:'请输入申请人..'},
                    2:{type:"input",colName:'applicantNo',placeholder:'职工号'},
                    3:{type:"input",colName:'applicantTel',placeholder:'电话'},
                    4:{type:"input",colName:'companyCharge',placeholder:'单位负责人'},
                    5:{type:"input",colName:'consult',placeholder:'查阅'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};
           /* var option={filterColumns:{
                    1:{type:"input",colName:'applicantName',placeholder:'请输入申请人..'},
                    2:{type:"input",colName:'applicantNo',placeholder:'职工号'},
                    3:{type:"input",colName:'applicantTel',placeholder:'电话'},
                    4:{type:"input",colName:'companyCharge',placeholder:'单位负责人'},
                    5:{type:"input",colName:'consult',placeholder:'查阅'},
                    6:{type:"input",colName:'gmtCreate'},
                    7:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:8};*/
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();
        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaMaterialBorrowDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,hrEmployeeService,fiveOaMaterialBorrowService) {
        var vm = this;
        var uiSref="five.oaMaterialBorrow";
        var materialBorrowId = $stateParams.materialBorrowId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            vm.loadDetail();
        };

        vm.loadData = function (loadProcess) {
            fiveOaMaterialBorrowService.getModelById(materialBorrowId).then(function (value) {
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
            fiveOaMaterialBorrowService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applicant'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人",type:"部门",deptIds:"1", userLoginList: vm.item.applicant,multiple:false});
            } else if (vm.status=='agent'){
                $scope.showOaSelectEmployeeModal_({title:"请选择经办人", type:"部门",deptIds:"1",userLoginList: vm.item.agent,multiple:false,parentDeptId:user.userLogin});
            }else if (vm.status=='companyCheckMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司办核收人", type:"部门",deptIds:"1",userLoginList: vm.item.companyCheckMan,multiple:true,parentDeptId:59});
            }else if (vm.status=='deptChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送签单位负责人",type:"部门",deptIds:"1", userLoginList: vm.item.deptChargeMan,multiple:false,parentDeptId:user.userLogin});
            } else if (vm.status=='partyChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择党群工作部负责人", type:"部门",deptIds:"1",userLoginList: vm.item.partyChargeMan,multiple:false,parentDeptId:72});
            }else if (vm.status=='companyCharge'){
                $scope.showOaSelectEmployeeModal_({title:"单位负责人",type:"部门",deptIds:"1", userLoginList: vm.item.applicantMan,multiple:true});
            }

        };
        //赋默认值
        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if (vm.status=='applicant'){
                vm.item.applicant = $scope.selectedUserLogins_;
                vm.item.applicantName = $scope.selectedUserNames_;

                hrEmployeeService.getModelByUserLogin(vm.item.applicant).then(function (value) {
                    var user = value.data.data;
                    vm.item.applicantMajor =user.specialty;
                    vm.item.deptName =user.deptName;
                    vm.item.deptId =user.deptId;
                    vm.item.applicantNo = user.userLogin;
                })
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaMaterialBorrowService.update(vm.item).then(function (value) {
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


        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.loadDetail=function(){
            fiveOaMaterialBorrowService.listDetail(materialBorrowId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };
        //新增
        vm.showDetailModel = function (id) {
            fiveOaMaterialBorrowService.getModelDetailById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                    $("#detailModal").modal("show");
                }
            })
        };

        vm.addDetail=function(){
            fiveOaMaterialBorrowService.getNewModelDetail(materialBorrowId,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                    $("#detailModal").modal("show");
                    vm.loadDetail();
                }
            })
        };
        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaMaterialBorrowService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveOaMaterialBorrowService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadDetail();
                }

            })

        };

        vm.print=function () {
            fiveOaMaterialBorrowService.getPrintData(materialBorrowId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialBorrow = vm.printData.materialBorrow;

                    vm.printDetails = materialBorrow;

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2,0,0,"A4");
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

    //档案申请  资料采购申请
    .controller("FiveOaMaterialPurchaseController", function ($state, $scope,$rootScope, fiveOaMaterialPurchaseService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        //vm.params = getCacheParams(key,{dispatchType: "资料采购申请表", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.params = {qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaMaterialPurchase";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;

            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaMaterialPurchaseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                 //   setCacheParams(key,vm.params,vm.pageInfo);
                }
            });
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaMaterialPurchaseDetail", {materialPurchaseId: id});
        };


        vm.add = function () {
            fiveOaMaterialPurchaseService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaMaterialPurchaseService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };
        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaMaterialPurchaseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'applicantManName',placeholder:'请输入申请人..'},
                    2:{type:"input",colName:'applicantNo',placeholder:'职工号'},
                    3:{type:"input",colName:'applicantTel',placeholder:'电话'},
                    4:{type:"input",colName:'companyCharge',placeholer:'单位负责人'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaMaterialPurchaseDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,hrEmployeeService ,fiveOaMaterialPurchaseService,hrDeptService) {
        var vm = this;
        var uiSref="five.oaMaterialPurchase";
        var materialPurchaseId = $stateParams.materialPurchaseId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaMaterialPurchaseService.getModelById(materialPurchaseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }

                }
            });
            fiveOaMaterialPurchaseService.listDetail(materialPurchaseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaMaterialPurchaseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applicantMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",deptIds:"1",userLoginList: vm.item.applicantMan,multiple:true});
            } else if (vm.status=='agent'){
                $scope.showOaSelectEmployeeModal_({title:"请选择经办人",type:"部门",deptIds:"1", userLoginList: vm.item.agent,multiple:false,parentDeptId:user.userLogin});
            }else if (vm.status=='companyCheckMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择公司办核收人", type:"部门",deptIds:"1",userLoginList: vm.item.companyCheckMan,multiple:true,parentDeptId:59});
            }else if (vm.status=='deptChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送签单位负责人",type:"部门",deptIds:"1", userLoginList: vm.item.deptChargeMan,multiple:false,parentDeptId:user.userLogin});
            } else if (vm.status=='partyChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择党群工作部负责人",type:"部门",deptIds:"1", userLoginList: vm.item.partyChargeMan,multiple:false,parentDeptId:72});
            }else if (vm.status=='companyCharge'){
                $scope.showOaSelectEmployeeModal_({title:"单位负责人", type:"部门",deptIds:"1",userLoginList: vm.item.applicantMan,multiple:true});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='applicantMan'){
                vm.item.applicantMan = $scope.selectedOaUserLogins_;
                vm.item.applicantManName = $scope.selectedOaUserNames_;
                hrEmployeeService.getModelByUserLogin(vm.item.applicantMan).then(function (value) {
                    var user = value.data.data;
                    vm.item.applicantNo = user.userLogin;
                    vm.item.applicantTel=user.mobile;
                })
            }
            if ( vm.status=='companyCharge'){
                vm.item.companyCharge = $scope.selectedOaUserLogins_;
                vm.item.companyChargeName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaMaterialPurchaseService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaMaterialPurchaseService.getNewModelDetail(materialPurchaseId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveOaMaterialPurchaseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaMaterialPurchaseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveOaMaterialPurchaseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    fiveOaMaterialPurchaseService.listDetail(materialPurchaseId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    });
                    $("#detailModal").modal("hide");

                }

            })

        };

        vm.print=function () {
            fiveOaMaterialPurchaseService.getPrintData(materialPurchaseId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");
                    var materialPurchaseDetails = vm.printData.materialPurchaseDetails;

                    vm.printDetails = materialPurchaseDetails;
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2,0,0,"A4");
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


    //工程管理部 项目资金使用计划
    .controller("FiveOaProjectFundPlanController", function ($state, $scope,$rootScope, fiveOaProjectFundPlanService,hrDeptService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var uiSref="five.oaProjectFundPlan";
        vm.params = getCacheParams(key,{qName: "",userLogin:user.userLogin,uiSref:uiSref,pageNum: 1, pageSize: $scope.pageSize,total:0,projectNames:"",contactNos:"",selectDeptId:""});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var tableName = $rootScope.loadTableName(uiSref);

        console.log("项目资金使用计划");
        vm.init=function(){
            hrDeptService.listMultipleDept().then(function (value) {
                if(value.data.ret){
                    vm.parents=value.data.data;
                }
            })

            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,
                projectNames:vm.params.projectNames,contactNos:vm.params.contactNos,deptId:vm.params.selectDeptId
            });
            fiveOaProjectFundPlanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaProjectFundPlanDetail", {projectFundPlanId: id});
        };


        vm.add = function () {
            fiveOaProjectFundPlanService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaProjectFundPlanService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.init();

        //蒋 新建带参页面刷新方法
        vm.refreshPagedData = function () {
            var params = $.extend(tablefilter.params, {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,
                projectNames:vm.params.projectNames,contactNos:vm.params.contactNos,deptId:vm.params.selectDeptId
            });
            fiveOaProjectFundPlanService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'projectName',placeholder:'请输入项目名称..'},
                    2:{type:"input",colName:'contractNo',placeholder:'请输入合同编号..'},
                    3:{type:"input",colName:'deptName',placeholder:'请输入单位名称..'},
                    4:{type:"input",colName:'planTime'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"进行中",value:"0"},{title:"已完成",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.refreshPagedData;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        return vm;

    })
    .controller("FiveOaProjectFundPlanDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaProjectFundPlanService,fiveBusinessContractLibraryService) {
        var vm = this;
        var uiSref="five.oaProjectFundPlan";
        var projectFundPlanId = $stateParams.projectFundPlanId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);

            $("#uploadFile1").fileupload({
                dataType: 'json',
                url:"/five/oa/projectFundPlan/updateExcl.json",
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

                    ;
                    data.formData = {id: projectFundPlanId,userLogin:user.enLogin};
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
                            vm.loadData(true);
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });

            vm.loadData(true);

        };

        vm.loadData = function (loadProcess) {
            fiveOaProjectFundPlanService.getModelById(projectFundPlanId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            vm.loadDetail();
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaProjectFundPlanService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='totalManger'){
                $scope.showOaSelectEmployeeModal_({title:"请选择总经理人",type:"部门",deptIds:","+vm.item.deptId+",", userLoginList: vm.item.totalManger,multiple:true,parentDeptId:vm.item.deptId});
            }else if (vm.status=='totalAccountant'){
                $scope.showOaSelectEmployeeModal_({title:"请选择总会计师",type:"部门",deptIds:"1", userLoginList: vm.item.totalAccountant,multiple:false,parentDeptId:16});
            } else if (vm.status=='chargeLeaderMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择项目经理",type:"部门",deptIds:"1", userLoginList: vm.item.chargeLeaderMan,multiple:true,parentDeptId:vm.item.deptId});
            } else if (vm.status=='financeDeptMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择财务金融部",type:"部门",deptIds:"1", userLoginList: vm.item.financeDeptMen,multiple:false,parentDeptId:18});
            } else if (vm.status=='projectManagementMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择工程管理部",type:"部门",deptIds:"1", userLoginList: vm.item.projectManagementMen,multiple:false,parentDeptId:29});
            }else if (vm.status=='deptChargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择部门负责人", type:"部门",deptIds:"1",userLoginList: vm.item.deptChargeMen,multiple:true,parentDeptId:user.deptId});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='totalManger'){
                vm.item.totalManger = $scope.selectedOaUserLogins_;
                vm.item.totalMangerName = $scope.selectedOaUserNames_;
            }else if (vm.status=='totalAccountant'){
                vm.item.totalAccountant = $scope.selectedOaUserLogins_;
                vm.item.totalAccountantName = $scope.selectedOaUserNames_;
            }else if (vm.status=='chargeLeaderMan'){
                vm.item.chargeLeaderMan = $scope.selectedOaUserLogins_;
                vm.item.chargeLeaderManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='financeDeptMen'){
                vm.item.financeDeptMen = $scope.selectedOaUserLogins_;
                vm.item.financeDeptMenName = $scope.selectedOaUserNames_;
            }else if (vm.status=='projectManagementMen'){
                vm.item.projectManagementMen = $scope.selectedOaUserLogins_;
                vm.item.projectManagementMenName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMen'){
                vm.item.deptChargeMen = $scope.selectedOaUserLogins_;
                vm.item.deptChargeMenName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if(vm.item.chargeLeaderManName ==""){
                toastr.warning("请先选择项目经理");
                return;
            }

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaProjectFundPlanService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.showDetailModel = function (id,type) {
            if (id === 0) {
                fiveOaProjectFundPlanService.getNewModelDetail(projectFundPlanId,type,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaProjectFundPlanService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            }
        };

        vm.loadDetail=function(){
            fiveOaProjectFundPlanService.listDetail(projectFundPlanId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaProjectFundPlanService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaProjectFundPlanService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadData(true);
                }

            })
        };

        vm.print=function () {
            fiveOaProjectFundPlanService.getPrintData(projectFundPlanId).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("中国五洲工程设计集团有限公司发文单");

                    var projectFundPlanDetails = vm.printData.projectFundPlanDetails;
                    vm.details = projectFundPlanDetails;

                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml =strBodyStyle+ "<body>" + document.getElementById("print_area").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.SET_PRINT_PAGESIZE(2,0,0,"A4");
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        };

        vm.showContractLibraryModal = function () {
            $scope.showLibrarySelectModal_({
                selectLibraryId:0,
                uiSref:uiSref,
                multiple: false,
                contractType:"工程承包、监理",
            });
        };
        $rootScope.saveSelectLibrary_ = function () {
            vm.item.projectName = $rootScope.selectedLibrary.contractName;
            vm.item.contractNo =$rootScope.selectedLibrary.contractNo;
            vm.item.projectNo =$rootScope.selectedLibrary.projectNo;
            $scope.closeLibrarySelectModal_();
        }
        //合同库
        /* vm.showSelectMainLibraryModal = function () {
             fiveBusinessContractLibraryService.selectMainContract("工程承包、监理").then(function (value) {
                 if (value.data.ret) {
                     vm.librarys = value.data.data;
                     singleCheckBox(".cb_library", "data-name");
                 }
             });
             $("#selectLibraryModal").modal("show");
         };
         vm.saveSelectModel = function () {
             if ($(".cb_library:checked").length > 0) {
                 var library = $.parseJSON($(".cb_library:checked").first().attr("data-name"));
                 //需要从 合同库 中同步到 评审的数据
                 vm.item.projectName = library.contractName;
                 vm.item.contractNo =library.contractNo;
                 vm.item.projectNo =library.projectNo;
             }
             vm.save();
             $("#selectLibraryModal").modal("hide");
         };*/
        //尾款计算 尾款计算=合同额-累计收款-本月因收
        vm.coutFinalPrice=function(){
            var fin=vm.detail.contractPrice-vm.detail.accumulativePrice-vm.detail.receivablePrice;
            vm.detail.finalPrice=fin.toFixed(6);
        };
        //获取相同合同号下的上一次最新数据
        vm.upLoadMonth=function(){
            fiveOaProjectFundPlanService.getUpMonthsDate(projectFundPlanId).then(function (value) {
                vm.loadDetail();
            })
        }

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //新闻报送审查  新闻宣传及信息报送审查
    .controller("FiveOaNewsExamineController", function ($state, $scope,$rootScope, fiveOaNewsExamineService) {
        var vm = this;
        //var key = $state.current.name + "_" + user.userLogin;
        vm.params ={qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaNewsExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaNewsExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                  //  setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaNewsExamineDetail", {newsExamineId: id});
        };


        vm.add = function () {
            fiveOaNewsExamineService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaNewsExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaNewsExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'title',placeholder:'请输入标题..'},
                    2:{type:"input",colName:'deptName',placeholder:'送审单位'},
                    3:{type:"input",colName:'sendManName',placeholder:'送审人'},
                    4:{type:"input",colName:'sendManLink',placeholder:'联系方式'},
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
    .controller("FiveOaNewsExamineDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,fiveOaNewsExamineService) {
        var vm = this;
        var uiSref="five.oaNewsExamineExamine";
        var newsExamineId = $stateParams.newsExamineId;
        var tableName = $rootScope.loadTableName("five.oaNewsExamine");

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaNewsExamineService.getModelById(newsExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#sendTime").datepicker('setDate', vm.item.sendTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaNewsExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='author'){
                $scope.showOaSelectEmployeeModal_({title:"请选择作者", type:"部门",deptIds:"1",userLoginList: vm.item.author,multiple:true,parentDeptId:16});
            }else if (vm.status=='sendMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送审人", type:"部门",deptIds:"1",userLoginList: vm.item.sendMan,multiple:false});
            }else if (vm.status=='deptChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送审单位负责人", type:"部门",deptIds:"1",userLoginList: vm.item.deptChargeMan,multiple:false});
            } else if (vm.status=='partyChargeMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择党群工作部负责人",type:"部门",deptIds:"1", userLoginList: vm.item.partyChargeMan,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='author'){
                vm.item.author = $scope.selectedOaUserLogins_;
                vm.item.authorName = $scope.selectedOaUserNames_;
            }else if (vm.status=='sendMan'){
                vm.item.sendMan = $scope.selectedOaUserLogins_;
                vm.item.sendManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='deptChargeMan'){
                vm.item.deptChargeMan = $scope.selectedOaUserLogins_;
                vm.item.deptChargeManName = $scope.selectedOaUserNames_;
            } else if (vm.status=='partyChargeMan'){
                vm.item.partyChargeMan = $scope.selectedOaUserLogins_;
                vm.item.partyChargeManName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaNewsExamineService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.print=function () {
            fiveOaNewsExamineService.getPrintData(newsExamineId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })


    //信息化审批-非密网文件传送流程
    .controller("FiveOaFileTransferController", function ($state, $scope,$rootScope, fiveOaFileTransferService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaFileTransfer";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaFileTransferService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaFileTransferDetail", {transferId: id});
        }

        vm.add = function () {
            fiveOaFileTransferService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaFileTransferService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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
    .controller("FiveOaFileTransferDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaFileTransferService) {
        var vm = this;

        var transferId = $stateParams.transferId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.oaAssociationApply");
        }

        vm.loadData = function (loadProcess) {
            fiveOaFileTransferService.getModelById(transferId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#sendTime").datepicker('setDate', vm.item.sendTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaFileTransferService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaFileTransferService.update(vm.item).then(function (value) {
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

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='receiver'){
                $scope.showOaSelectEmployeeModal_({title:"请选择接收人", type:"部门",deptIds:"1",userLoginList: vm.item.receiver,multiple:false});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='receiver'){
                vm.item.receiver = $scope.selectedOaUserLogins_;
                vm.item.receiverName = $scope.selectedOaUserNames_;
            }
        };

        vm.print=function () {
            fiveOaFileTransferService.getPrintData(transferId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })


    //信息化审批-个人非密信息导出审批
    .controller("FiveOaMessageExportController", function ($state, $scope,$rootScope, fiveOaMessageExportService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaMessageExport";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin, uiSref:uiSref};
            fiveOaMessageExportService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaMessageExportDetail", {exportId: id});
        }

        vm.add = function () {
            fiveOaMessageExportService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaMessageExportService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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
    .controller("FiveOaMessageExportDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaMessageExportService) {
        var vm = this;

        var exportId = $stateParams.exportId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.oaComputerChange");
        }

        vm.loadData = function (loadProcess) {
            fiveOaMessageExportService.getModelById(exportId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applyTime").datepicker('setDate', vm.item.applyTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaMessageExportService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaMessageExportService.update(vm.item).then(function (value) {
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
            fiveOaMessageExportService.getPrintData(exportId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //经营发展部-合同评审记录表
    .controller("FiveOaReviewContractController", function ($state, $scope,$rootScope, fiveOaReviewContractService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaReviewContract";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaReviewContractService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaReviewContractDetail", {contractId: id});
        }

        vm.add = function () {
            fiveOaReviewContractService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaReviewContractService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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
    .controller("FiveOaReviewContractDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaReviewContractService) {
        var vm = this;

        var contractId = $stateParams.contractId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);


        }

        vm.loadData = function (loadProcess) {
            fiveOaReviewContractService.getModelById(contractId).then(function (value) {
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
            fiveOaReviewContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaReviewContractService.update(vm.item).then(function (value) {
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

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='reviewUser'){
                $scope.showOaSelectEmployeeModal_({title:"请选择参加评审人员",
                    type:"部门",deptIds:"1", userLoginList: vm.item.reviewUser,multiple:true});
            }
            if (vm.status=='totalDesigner'){
                $scope.showOaSelectEmployeeModal_({title:"请选择项目经理/总设计师人员",
                    type:"部门",deptIds:"1",userLoginList: vm.item.totalDesigner,multiple:false,parentDeptId:16});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='reviewUser'){
                vm.item.reviewUser = $scope.selectedOaUserLogins_;
                vm.item.reviewUserName = $scope.selectedOaUserNames_;
            }else if ( vm.status=='totalDesigner'){
                vm.item.totalDesigner = $scope.selectedOaUserLogins_;
                vm.item.totalDesignerName = $scope.selectedOaUserNames_;
            }
        };

        vm.print=function () {
            fiveOaReviewContractService.getPrintData(contractId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })


    //行政事务-办公家具采购
    .controller("FiveOaFurniturePurchaseController", function ($state, $scope,$rootScope, fiveOaFurniturePurchaseService) {
        var vm = this;
       // var key = $state.current.name + "_" + user.userLogin;
        var uiSref="five.oaFurniturePurchase";
        vm.params = {qName: "",userLogin:user.userLogin,uiSref:uiSref,pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var tableName = $rootScope.loadTableName(uiSref);


        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaFurniturePurchaseService.downTempleXls(params).then(function (response) {

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
            //var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveOaFurniturePurchaseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                   // setCacheParams(key,vm.params,vm.pageInfo);
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaFurniturePurchaseDetail", {furnitureId: id});
        };


        vm.add = function () {
            fiveOaFurniturePurchaseService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.loadPagedData();

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaFurniturePurchaseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'deptName',placeholder:'请输入采购部门..'},
                    2:{type:"input",colName:'purchaseReason',placeholder:'采购理由'},
                   3:{type:"input",colName:'creatorName',placeholder:'创建人'},
                    4:{type:"input",colName:'gmtCreate'},
                    5:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:6};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaFurniturePurchaseService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };


        vm.queryData();

        return vm;

    })
    .controller("FiveOaFurniturePurchaseDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaFurniturePurchaseService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaFurniturePurchase";
        var furnitureId = $stateParams.furnitureId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaFurniturePurchaseService.getModelById(furnitureId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
            vm.loadDetail();
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaFurniturePurchaseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };



        //发送流程验证
        $scope.showSendTask=function(send){

            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaFurniturePurchaseService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function() {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        };

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaFurniturePurchaseService.getNewModelDetail(furnitureId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaFurniturePurchaseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            }
        };

        vm.loadDetail=function(){
            fiveOaFurniturePurchaseService.listDetail(furnitureId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaFurniturePurchaseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaFurniturePurchaseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadData(true);
                }

            })
        };

        vm.countTotalPrice=function(){
            var fin=vm.detail.price*vm.detail.number;
            vm.detail.totalPrice=fin.toFixed(2);
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("办公家具采购");
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



    //人力资源部- 因公出国内部审批文单
    .controller("FiveOaGoAbroadController", function ($state, $scope,$rootScope, fiveOaGoAbroadService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "因公出国内部审批文单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaGoAbroad";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {dispatchType:vm.params.dispatchType, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,qName:vm.params.qName};
            fiveOaGoAbroadService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaGoAbroadDetail", {goAbroadId: id});
        }


        vm.add = function () {
            fiveOaGoAbroadService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaGoAbroadService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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
    .controller("FiveOaGoAbroadDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaGoAbroadService) {
        var vm = this;
        var uiSref="five.oaGoAbroad";
        var goAbroadId = $stateParams.goAbroadId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveOaGoAbroadService.getModelById(goAbroadId).then(function (value) {
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
            fiveOaGoAbroadService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择因公出国领导", type:"部门",deptIds:"1",userLoginList: vm.item.chargeLeader,multiple:true,parentDeptId:16});
            }else if (vm.status=='businessChargeLeader'){
                $scope.showOaSelectEmployeeModal_({title:"请选择业务主管领导", type:"部门",deptIds:"1",userLoginList: vm.item.businessChargeLeader,multiple:true});
            }else if (vm.status=='departmentChargeMen'){
                $scope.showOaSelectEmployeeModal_({title:"请选择单位负责人", type:"部门",deptIds:"1",userLoginList: vm.item.departmentChargeMen,multiple:true});//公司办公室
            }else if (vm.status=='drafter'){
                $scope.showOaSelectEmployeeModal_({title:"请选择拟稿人人员", type:"部门",deptIds:"1",userLoginList: vm.item.drafter,multiple:false});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='chargeLeader'){
                vm.item.chargeLeader = $scope.selectedOaUserLogins_;
                vm.item.chargeLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='businessChargeLeader'){
                vm.item.businessChargeLeader = $scope.selectedOaUserLogins_;
                vm.item.businessChargeLeaderName = $scope.selectedOaUserNames_;
            }else if (vm.status=='departmentChargeMen'){
                vm.item.departmentChargeMen = $scope.selectedOaUserLogins_;
                vm.item.departmentChargeMenName = $scope.selectedOaUserNames_;
            }else if (vm.status=='drafter'){
                vm.item.drafter = $scope.selectedOaUserLogins_;
                vm.item.drafterName = $scope.selectedOaUserNames_;
            }};

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaGoAbroadService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        jQuery.showActHandleModal({
                            taskId: $scope.processInstance.taskId,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                           /* $rootScope.loadNewProcessInstance(processInstanceId);
                            $rootScope.loadProcessInstance(processInstanceId);*/
                           setTimeout(function () {
                               $scope.refresh();
                           },2000);

                            //vm.loadData(true);
                        });
                    }
                })
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.print=function () {
            fiveOaGoAbroadService.getPrintData(goAbroadId).then(function (value) {
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
        };

        vm.init();
        $scope.refresh = function () {
            vm.loadData(true);
        };

        return vm;

    })

    //人力资源部- 职工入职审批单
    .controller("FiveOaEmployeeEntryExamineController", function ($state, $scope,$rootScope, fiveOaEmployeeEntryExamineService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "职工入职审批单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaEmployeeEntryExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {dispatchType:vm.params.dispatchType, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,qName:vm.params.qName};
            fiveOaEmployeeEntryExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaEmployeeEntryExamineDetail", {employeeEntryExamineId: id});
        };


        vm.add = function () {
            fiveOaEmployeeEntryExamineService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaEmployeeEntryExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        return vm;

    })
    .controller("FiveOaEmployeeEntryExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaEmployeeEntryExamineService) {
        var vm = this;
        var uiSref="five.oaEmployeeEntryExamine";
        var employeeEntryExamineId = $stateParams.employeeEntryExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaEmployeeEntryExamineService.getModelById(employeeEntryExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $("#entryTime").datepicker('setDate', vm.item.entryTime);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaEmployeeEntryExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='chargeLeader'){
                $scope.showSelectEmployeeModal_({title:"请选择因公出国领导", userLoginList: vm.item.chargeLeader,multiple:true,parentDeptId:16});
            }else if (vm.status=='businessChargeLeader'){
                $scope.showSelectEmployeeModal_({title:"请选择业务主管领导", userLoginList: vm.item.businessChargeLeader,multiple:true});
            }else if (vm.status=='departmentChargeMen'){
                $scope.showSelectEmployeeModal_({title:"请选择单位负责人", userLoginList: vm.item.departmentChargeMen,multiple:true,parentDeptId:59});//公司办公室
            }else if (vm.status=='drafter'){
                $scope.showSelectEmployeeModal_({title:"请选择拟稿人人员", userLoginList: vm.item.drafter,multiple:false});
            }else if (vm.status=='realSendMan'){
                $scope.showSelectEmployeeModal_({title:"请选择主送人员", userLoginList: vm.item.realSendMan,multiple:true});
            }else if (vm.status=='copySendMan'){
                $scope.showSelectEmployeeModal_({title:"请选择抄送人员", userLoginList: vm.item.copySendMan,multiple:true});
            }else if (vm.status=='typist'){
                $scope.showSelectEmployeeModal_({title:"请选择打字员人员", userLoginList: vm.item.typist,multiple:false});
            }else if (vm.status=='proofreader'){
                $scope.showSelectEmployeeModal_({title:"请选择校对人员", userLoginList: vm.item.proofreader,multiple:false});
            }


        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if ( vm.status=='chargeLeader'){
                vm.item.chargeLeader = $scope.selectedUserLogins_;
                vm.item.chargeLeaderName = $scope.selectedUserNames_;
            }else if (vm.status=='businessChargeLeader'){
                vm.item.businessChargeLeader = $scope.selectedUserLogins_;
                vm.item.businessChargeLeaderName = $scope.selectedUserNames_;
            }else if (vm.status=='departmentChargeMen'){
                vm.item.departmentChargeMen = $scope.selectedUserLogins_;
                vm.item.departmentChargeMenName = $scope.selectedUserNames_;
            }else if (vm.status=='drafter'){
                vm.item.drafter = $scope.selectedUserLogins_;
                vm.item.drafterName = $scope.selectedUserNames_;
            }else if (vm.status=='realSendMan'){
                vm.item.realSendMan = $scope.selectedUserLogins_;
                vm.item.realSendManName = $scope.selectedUserNames_;
            }else if (vm.status=='copySendMan'){
                vm.item.copySendMan = $scope.selectedUserLogins_;
                vm.item.copySendManName = $scope.selectedUserNames_;
            }else if (vm.status=='typist'){
                vm.item.typist = $scope.selectedUserLogins_;
                vm.item.typistName = $scope.selectedUserNames_;
            }else if (vm.status=='proofreader'){
                vm.item.proofreader = $scope.selectedUserLogins_;
                vm.item.proofreaderName = $scope.selectedUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaEmployeeEntryExamineService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            if(vm.opt =='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入前单位",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt =='entryDeptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入后单位",type:"选部门", deptIdList: vm.item.entryDeptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt =='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }else if(vm.opt =='entryDeptId'){
                vm.item.entryDeptId= Number($scope.selectedOaDeptIds_);
                vm.item.entryDeptName= $scope.selectedOaDeptNames_;
            }
        };

        vm.print=function () {
            fiveOaEmployeeEntryExamineService.getPrintData(employeeEntryExamineId).then(function (value) {
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

        $scope.refresh=function(){
            vm.loadData(true);
        };
        vm.init();

        return vm;

    })

    //人力资源部 职工内部调整审批单
    .controller("FiveOaEmployeeTransferExamineController", function ($state, $scope,$rootScope, fiveOaEmployeeTransferExamineService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "职工内部调整审批单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaEmployeeTransferExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {dispatchType:vm.params.dispatchType, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,qName:vm.params.qName};
            fiveOaEmployeeTransferExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaEmployeeTransferExamineDetail", {employeeTransferExamineId: id});
        };


        vm.add = function () {
            fiveOaEmployeeTransferExamineService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaEmployeeTransferExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        return vm;

    })
    .controller("FiveOaEmployeeTransferExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaEmployeeTransferExamineService){
        var vm = this;
        var uiSref="five.oaEmployeeTransferExamine";
        var employeeTransferExamineId = $stateParams.employeeTransferExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaEmployeeTransferExamineService.getModelById(employeeTransferExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $("#transferTime").datepicker('setDate', vm.item.transferTime);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaEmployeeTransferExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };


        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaEmployeeTransferExamineService.update(vm.item).then(function (value) {
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

        vm.showUserModel = function () {
            $scope.showOaSelectEmployeeModal_({title:"请选择内部调整人员",type:"部门",deptIds:"1", userLoginList: vm.item.login,multiple:true});
        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.login = $scope.selectedOaUserLogins_;
            vm.item.name = $scope.selectedOaUserNames_;
        };

        vm.showDeptModal=function(id) {
            if(vm.opt =='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入前单位",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt =='transferDeptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入后单位",type:"选部门", deptIdList: vm.item.transferDeptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt =='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }else if(vm.opt =='transferDeptId'){
                vm.item.transferDeptId= Number($scope.selectedOaDeptIds_);
                vm.item.transferDeptName= $scope.selectedOaDeptNames_;
            }
        };

        vm.print=function () {
            fiveOaEmployeeTransferExamineService.getPrintData(employeeTransferExamineId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //人力资源部 职工离职审批单
    .controller("FiveOaEmployeeDimissionExamineController", function ($state, $scope,$rootScope, fiveOaEmployeeDimissionExamineService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "职工离职审批单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaEmployeeDimissionExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {dispatchType:vm.params.dispatchType, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,qName:vm.params.qName};
            fiveOaEmployeeDimissionExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaEmployeeDimissionExamineDetail", {employeeDimissionExamineId: id});
        };


        vm.add = function () {
            fiveOaEmployeeDimissionExamineService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaEmployeeDimissionExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        return vm;

    })
    .controller("FiveOaEmployeeDimissionExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaEmployeeDimissionExamineService) {
        var vm = this;
        var uiSref="five.oaEmployeeDimissionExamine";
        var employeeDimissionExamineId = $stateParams.employeeDimissionExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaEmployeeDimissionExamineService.getModelById(employeeDimissionExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $("#dimissionTime").datepicker('setDate', vm.item.dimissionTime);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaEmployeeDimissionExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };


        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaEmployeeDimissionExamineService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            if(vm.opt =='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入前单位",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt =='dimissionDeptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入后单位",type:"选部门", deptIdList: vm.item.dimissionDeptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt =='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }else if(vm.opt =='dimissionDeptId'){
                vm.item.dimissionDeptId= Number($scope.selectedOaDeptIds_);
                vm.item.dimissionDeptName= $scope.selectedOaDeptNames_;
            }
        };

        vm.showUserModel = function () {
            $scope.showOaSelectEmployeeModal_({title:"请选择离职人员",type:"部门",deptIds:"1", userLoginList: vm.item.login,multiple:true});
        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.login = $scope.selectedOaUserLogins_;
            vm.item.name = $scope.selectedOaUserNames_;
        };

        vm.print=function () {
            fiveOaEmployeeDimissionExamineService.getPrintData(employeeDimissionExamineId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //人力资源部 职工退休审批单
    .controller("FiveOaEmployeeRetireExamineController", function ($state, $scope,$rootScope, fiveOaEmployeeRetireExamineService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "职工退休审批单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaEmployeeRetireExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {dispatchType:vm.params.dispatchType, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,qName:vm.params.qName};
            fiveOaEmployeeRetireExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaEmployeeRetireExamineDetail", {employeeRetireExamineId: id});
        };


        vm.add = function () {
            fiveOaEmployeeRetireExamineService.getNewModel( user.userLogin,vm.params.dispatchType).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaEmployeeRetireExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        return vm;

    })
    .controller("FiveOaEmployeeRetireExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaEmployeeRetireExamineService) {
        var vm = this;
        var uiSref="five.oaEmployeeRetireExamine";
        var employeeRetireExamineId = $stateParams.employeeRetireExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaEmployeeRetireExamineService.getModelById(employeeRetireExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                        $("#retireTime").datepicker('setDate', vm.item.retireTime);
                    }
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaEmployeeRetireExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };



        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaEmployeeRetireExamineService.update(vm.item).then(function (value) {
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

        vm.showDeptModal=function(id) {
            if(vm.opt =='deptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入前单位",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt =='retireDeptId'){
                $scope.showOaSelectEmployeeModal_({title:"请选择调入后单位",type:"选部门", deptIdList: vm.item.retireDeptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }
        };
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt =='deptId'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            }else if(vm.opt =='retireDeptId'){
                vm.item.retireDeptId= Number($scope.selectedOaDeptIds_);
                vm.item.retireDeptName= $scope.selectedOaDeptNames_;
            }
        };

        vm.showUserModel = function () {
            $scope.showOaSelectEmployeeModal_({title:"请选择退休人员",type:"部门",deptIds:"1", userLoginList: vm.item.login,multiple:true});
        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.login = $scope.selectedOaUserLogins_;
            vm.item.name = $scope.selectedOaUserNames_;
        };

        vm.print=function () {
            fiveOaEmployeeRetireExamineService.getPrintData(employeeRetireExamineId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })


    //网络运维中心 新购计算机信息一览表（未使用）
    .controller("FiveOaComputerPurchaseController", function ($state, $scope,$rootScope, fiveOaComputerPurchaseService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "新购计算机信息一览表", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaComputerPurchase";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize, userLogin:user.userLogin,uiSref:uiSref};
            fiveOaComputerPurchaseService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            });
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.oaComputerPurchaseDetail", {computerPurchaseId: id});
        };


        vm.add = function () {
            fiveOaComputerPurchaseService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaComputerPurchaseService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.loadPagedData();

        return vm;

    })
    .controller("FiveOaComputerPurchaseDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,fiveOaComputerPurchaseService,hrDeptService) {
        var vm = this;
        var uiSref="five.oaComputerPurchase";
        var computerPurchaseId = $stateParams.computerPurchaseId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaComputerPurchaseService.getModelById(computerPurchaseId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicationTime").datepicker('setDate', vm.item.applicationTime);
                }
            });
            fiveOaComputerPurchaseService.listDetail(computerPurchaseId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaComputerPurchaseService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applicantMan') {
                $scope.showSelectEmployeeModal_({title: "请选择申请人", userLoginList: vm.item.applicantMan, multiple: true});
                if (vm.status == 'agent') {
                    $scope.showSelectEmployeeModal_({
                        title: "请选择经办人",
                        userLoginList: vm.item.agent,
                        multiple: false,
                        parentDeptId: user.userLogin
                    });
                } else if (vm.status == 'companyCheckMan') {
                    $scope.showSelectEmployeeModal_({
                        title: "请选择公司办核收人",
                        userLoginList: vm.item.companyCheckMan,
                        multiple: true,
                        parentDeptId: 59
                    });
                } else if (vm.status == 'deptChargeMan') {
                    $scope.showSelectEmployeeModal_({
                        title: "请选择送签单位负责人",
                        userLoginList: vm.item.deptChargeMan,
                        multiple: false,
                        parentDeptId: user.userLogin
                    });
                } else if (vm.status == 'partyChargeMan') {
                    $scope.showSelectEmployeeModal_({
                        title: "请选择党群工作部负责人",
                        userLoginList: vm.item.partyChargeMan,
                        multiple: false,
                        parentDeptId: 72
                    });
                } else if (vm.status == 'companyLeader') {
                    $scope.showSelectEmployeeModal_({
                        title: "请选择批示领导",
                        userLoginList: vm.item.companyLeader,
                        multiple: false,
                        parentDeptId: 16
                    });
                } else if (vm.status == 'instructLeader') {
                    $scope.showSelectEmployeeModal_({
                        title: "请选择会签领导",
                        userLoginList: vm.item.instructLeader,
                        multiple: true,
                        parentDeptId: 16
                    });
                }
                if (vm.status == 'companyCharge') {
                    $scope.showSelectEmployeeModal_({
                        title: "单位负责人",
                        userLoginList: vm.item.applicantMan,
                        multiple: true
                    });
                }
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if ( vm.status=='applicantMan'){
                vm.item.applicantMan = $scope.selectedUserLogins_;//
                vm.item.applicantManName = $scope.selectedUserNames_;//
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaComputerApplicationService.update(vm.item).then(function (value) {
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
        };

        vm.saveDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.deptId= dept.id;
            vm.item.deptName=dept.text;
            $("#deptSelectModal").modal("hide");
        };

        //新增
        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveOaComputerPurchaseService.getNewModelDetail(computerPurchaseId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveOaComputerPurchaseService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaComputerPurchaseService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });


        };
        //保存
        vm.saveDetail = function () {
            fiveOaComputerPurchaseService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    fiveOaComputerPurchaseService.listDetail(computerPurchaseId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    });
                    $("#detailModal").modal("hide");

                }

            })

        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //公司办公室 规章制度法律审查工作单（未使用）
    .controller("FiveOaRuleLawExamineController", function ($state, $scope,$rootScope, fiveOaRuleLawExamineService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{dispatchType: "规章制度法律审查工作单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaRuleLawExamine";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaRuleLawExamineService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaRuleLawExamineDetail", {ruleLawExamineId: id});
        };


        vm.add = function () {
            fiveOaRuleLawExamineService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaRuleLawExamineService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        return vm;

    })
    .controller("FiveOaRuleLawExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaRuleLawExamineService,hrDeptService) {
        var vm = this;
        var uiSref="five.oaRuleLawExamine";
        var ruleLawExamineId = $stateParams.ruleLawExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaRuleLawExamineService.getModelById(ruleLawExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#sendTime").datepicker('setDate', vm.item.sendTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaRuleLawExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='sendMan'){
                $scope.showSelectEmployeeModal_({title:"请选择送审人", userLoginList: vm.item.sendMan,multiple:true,parentDeptId:16});
            }else if (vm.status=='drafter'){
                $scope.showSelectEmployeeModal_({title:"请选择拟稿人人员", userLoginList: vm.item.drafter,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if ( vm.status=='sendMan'){
                vm.item.sendMan = $scope.selectedUserLogins_;
                vm.item.sendManName = $scope.selectedUserNames_;
            }else if (vm.status=='drafter'){
                vm.item.drafter = $scope.selectedUserLogins_;
                vm.item.drafterName = $scope.selectedUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaRuleLawExamineService.update(vm.item).then(function (value) {
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
        };

        vm.saveDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.deptId= dept.id;
            vm.item.deptName=dept.text;
            $("#deptSelectModal").modal("hide");
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //经营发展部-业务合同签发单（未使用）
    .controller("FiveOaContractSignController", function ($state, $scope,$rootScope, fiveOaContractSignService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaContractSign";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaContractSignService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaContractSignDetail", {signId: id});
        }

        vm.add = function () {
            fiveOaContractSignService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaContractSignService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
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
    .controller("FiveOaContractSignDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaContractSignService) {
        var vm = this;

        var signId = $stateParams.signId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveOaContractSignService.getModelById(signId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#signTime").datepicker('setDate', vm.item.signTime);
                }
            })

        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaContractSignService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaContractSignService.update(vm.item).then(function (value) {
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

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='countersign'){
                $scope.showOaSelectEmployeeModal_({title:"请选择会签人员",type:"部门",deptIds:"1", userLoginList: vm.item.countersign,multiple:true});
            }else if (vm.status=='auditMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择核稿人",type:"部门",deptIds:"1", userLoginList: vm.item.auditMan,multiple:false});
            }else if (vm.status=='signer'){
                $scope.showOaSelectEmployeeModal_({title:"请选择签发人员",type:"部门",deptIds:"1", userLoginList: vm.item.signer,multiple:true,parentDeptId:16});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='countersign'){
                vm.item.countersign = $scope.selectedOaUserLogins_;
                vm.item.countersignName = $scope.selectedOaUserNames_;
            }else if (vm.status=='auditMan'){
                vm.item.auditMan = $scope.selectedOaUserLogins_;
                vm.item.auditManName = $scope.selectedOaUserNames_;
            }else if (vm.status=='signer'){
                vm.item.signer = $scope.selectedOaUserLogins_;
                vm.item.signerName = $scope.selectedOaUserNames_;
            }
        };

        vm.print=function () {
            fiveOaContractSignService.getPrintData(signId).then(function (value) {
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
        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;

    })

    //用印管理-用印审批（法人章 公式章 法人签名章）（未使用）
    .controller("OaStampApplyController", function ($state,$stateParams ,$scope,$rootScope, oaStampApplyService,commonCodeService) {
        var vm = this;
        vm.keyWords="用印审批" ;
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.params={types:'公司章,法人章,法人签名',fileNames:''}
        var uiSref='oa.stampApply';
        var tableName = $rootScope.loadTableName(uiSref);


        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData(true);
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

        vm.add = function () {
            oaStampApplyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.showStamp=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"办公室印章审批类型").then(function(value){
                if (value.data.ret){
                    vm.stampList=value.data.data;
                    $("#stampModal").modal("show");
                }
            })

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
            $state.go("oa.stampApplyDetail",{stampId: id});
        }

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();

        return vm;
    })
    .controller("OaStampApplyDetailController", function ($state,$stateParams,$rootScope ,$scope, oaStampApplyService,commonCodeService) {
        var vm = this;
        vm.keyWords="用印审批";
        var stampId = $stateParams.stampId;
        var uiSref="oa.stampApply";
        var tableName = $rootScope.loadTableName(uiSref);

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
                            setTimeout(function(){
                                $scope.refresh();
                            },2000)
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

        $scope.refresh=function(){
            vm.loadData(true);
        }
        vm.init();

        return vm;
    })

    //公司办 制度会签单（未使用）
    .controller("FiveOaInstitutionCountSignController", function ($state, $scope,$rootScope, fiveOaInstitutionCountSignService) {
    var vm = this;
    var key = $state.current.name + "_" + user.userLogin;
    vm.params = getCacheParams(key,{dispatchType: "制度会签单", qDeptName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
    vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
    var uiSref="five.oaInstitutionCountSign";
    var tableName = $rootScope.loadTableName(uiSref);

    vm.queryData = function () {
        vm.pageInfo.pageNum = 1;

        vm.loadPagedData();
    };

    vm.loadPagedData = function () {
        var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
        fiveOaInstitutionCountSignService.listPagedData(params).then(function (value) {
            if (value.data.ret) {
                vm.pageInfo = value.data.data;
                setCacheParams(key,vm.params,vm.pageInfo);
            }
        });
        $scope.loadRightData(user.userLogin,uiSref);
    };

    vm.show = function (id) {
        $state.go("five.oaInstitutionCountSignDetail", {institutionCountSignId: id});
    };


    vm.add = function () {
        fiveOaInstitutionCountSignService.getNewModel( user.userLogin).then(function (value) {
            if (value.data.ret) {
                vm.show(value.data.data);
            }
        })
    };

    vm.remove = function (id) {
        bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
            if (result) {
                fiveOaInstitutionCountSignService.remove(id, user.userLogin).then(function (value) {
                    if(value.data.ret) {
                        toastr.success("删除成功!");
                        vm.queryData();
                    }
                });
            }
        })
    };

    vm.loadPagedData();

    return vm;

})
    .controller("FiveOaInstitutionCountSignDetailController", function ($sce,$state,$stateParams,$rootScope,$scope,fiveOaInstitutionCountSignService,hrDeptService) {
        var vm = this;
        var uiSref="five.oaInstitutionCountSign";
        var institutionCountSignId = $stateParams.institutionCountSignId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaInstitutionCountSignService.getModelById(institutionCountSignId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#submitTime").datepicker('setDate', vm.item.submitTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaInstitutionCountSignService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='agent'){
                $scope.showSelectEmployeeModal_({title:"请选择经办人", userLoginList: vm.item.agent,multiple:true,parentDeptId:16});
            }else if (vm.status=='companyCheckMan'){
                $scope.showSelectEmployeeModal_({title:"请选择公司办核收人", userLoginList: vm.item.companyCheckMan,multiple:false});
            }else if (vm.status=='deptChargeMan'){
                $scope.showSelectEmployeeModal_({title:"请选择送签单位负责人", userLoginList: vm.item.deptChargeMan,multiple:false});
            } else if (vm.status=='partyChargeMan'){
                $scope.showSelectEmployeeModal_({title:"请选择党群工作部负责人", userLoginList: vm.item.partyChargeMan,multiple:false});
            }

        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if ( vm.status=='agent'){
                vm.item.agent = $scope.selectedUserLogins_;
                vm.item.agentName = $scope.selectedUserNames_;
            }else if (vm.status=='companyCheckMan'){
                vm.item.companyCheckMan = $scope.selectedUserLogins_;
                vm.item.companyCheckManName = $scope.selectedUserNames_;
            }else if (vm.status=='deptChargeMan'){
                vm.item.deptChargeMan = $scope.selectedUserLogins_;
                vm.item.deptChargeManName = $scope.selectedUserNames_;
            } else if (vm.status=='partyChargeMan'){
                vm.item.partyChargeMan = $scope.selectedUserLogins_;
                vm.item.partyChargeManName = $scope.selectedUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaInstitutionCountSignService.update(vm.item).then(function (value) {
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
        };

        vm.saveDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.deptId= dept.id;
            vm.item.deptName=dept.text;
            $("#deptSelectModal").modal("hide");
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //公司办 通用会签单（未使用）
    .controller("FiveOaGeneralCountersignController", function ($state, $scope,$rootScope, fiveOaGeneralCountersignService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaGeneralCountersign";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
            fiveOaGeneralCountersignService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.oaGeneralCountersignDetail", {generalId: id});
        };

        vm.add = function () {
            fiveOaGeneralCountersignService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaGeneralCountersignService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.queryData();

        return vm;

    })
    .controller("FiveOaGeneralCountersignDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaGeneralCountersignService,hrDeptService) {
        var vm = this;

        var generalId = $stateParams.generalId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,"five.oaGeneralCountersign");
            vm.loadData(true);
        };

        vm.loadData = function (loadProcess) {
            fiveOaGeneralCountersignService.getModelById(generalId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }

                    $("#reportTime").datepicker('setDate', vm.item.reportTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaGeneralCountersignService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(false);
                }
            })
        };


        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='manager'){
                $scope.showSelectEmployeeModal_({title:"请选择经办人", userLoginList: vm.item.manager,multiple:false});//公司办公室
            }else if (vm.status=='chargeMan'){
                $scope.showSelectEmployeeModal_({title:"请选择负责人", userLoginList: vm.item.chargeMan,multiple:true});
            }


        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeSelectEmployeeModal_();
            if ( vm.status=='manager'){
                vm.item.manager = $scope.selectedUserLogins_;
                vm.item.managerName = $scope.selectedUserNames_;
            }else if (vm.status=='chargeMan'){
                vm.item.chargeMan = $scope.selectedUserLogins_;
                vm.item.chargeManName = $scope.selectedUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaGeneralCountersignService.update(vm.item).then(function (value) {
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
        };

        vm.saveDept=function() {
            var dept = $('#dept_select_tree').jstree(true).get_selected(true)[0];
            vm.item.undertakeDeptId= dept.id;
            vm.item.undertakeDeptName=dept.text;
            $("#deptSelectModal").modal("hide");
        };

        vm.init();
        $scope.refresh=function(){
            vm.loadData(true);
        };
        return vm;

    })

    //公司办 合同法律审查工作单(未使用）
    .controller("FiveOaContractLawExamineController", function ($state, $scope,$rootScope, fiveOaContractLawExamineService) {
    var vm = this;
    var key = $state.current.name + "_" + user.userLogin;
    vm.params = getCacheParams(key,{dispatchType: "合同法律审查工作单", qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0});
    vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
    var uiSref="five.oaContractLawExamine";
        var tableName = $rootScope.loadTableName(uiSref);

    vm.queryData = function () {
        vm.pageInfo.pageNum = 1;
        $scope.loadRightData(user.userLogin,uiSref);
        vm.loadPagedData();
    };

    vm.loadPagedData = function () {
        var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref};
        fiveOaContractLawExamineService.listPagedData(params).then(function (value) {
            if (value.data.ret) {
                vm.pageInfo = value.data.data;
                setCacheParams(key,vm.params,vm.pageInfo);
            }
        })

    };

    vm.show = function (id) {
        $state.go("five.oaContractLawExamineDetail", {contractLawExamineId: id});
    }


    vm.add = function () {
        fiveOaContractLawExamineService.getNewModel( user.userLogin).then(function (value) {
            if (value.data.ret) {
                vm.show(value.data.data);
            }
        })
    }

    vm.remove = function (id) {
        bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
            if (result) {
                fiveOaContractLawExamineService.remove(id, user.userLogin).then(function (value) {
                    if(value.data.ret) {
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
    .controller("FiveOaContractLawExamineDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaContractLawExamineService) {
        var vm = this;
        var uiSref="five.oaContractLawExamine";
        var contractLawExamineId = $stateParams.contractLawExamineId;
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
        }

        vm.loadData = function (loadProcess) {
            fiveOaContractLawExamineService.getModelById(contractLawExamineId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#examineTime").datepicker('setDate', vm.item.examineTime);
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveOaContractLawExamineService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='submitMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择送审人",type:"部门", userLoginList: vm.item.submitMan,multiple:false,deptIds:"1",parentDeptId:16});
            }
        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='submitMan'){
                vm.item.submitMan = $scope.selectedOaUserLogins_;
                vm.item.submitManName = $scope.selectedOaUserNames_;
            }
        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaContractLawExamineService.update(vm.item).then(function (value) {
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
            }else {
                toastr.warning("请准确填写数据!")
                return false;
            }

        }

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        }

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.item.deptName = $scope.selectedOaDeptNames_;
            vm.item.deptId = Number($scope.selectedOaDeptIds_);
        }

        vm.print=function () {
            fiveOaContractLawExamineService.getPrintData(contractLawExamineId).then(function (value) {
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

        $scope.refresh=function(){
            vm.loadData(true);
        }

        vm.init();


        return vm;

    });
