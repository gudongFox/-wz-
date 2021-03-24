angular.module('controllers.five.asset', [])



    //职工卡充值变动
    .controller("FiveOaCardChangeController", function ($state, $scope,$rootScope, fiveOaCardChangeService,hrDeptService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        vm.pageInfo = {/*q:vm.params.qName,*/pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaCardChange";
        var tableName = $rootScope.loadTableName(uiSref);

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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaCardChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaCardChangeDetail", {changeId: id});
        };


        vm.add = function () {
            fiveOaCardChangeService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaCardChangeService.remove(id, user.userLogin).then(function (value) {
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
            fiveOaCardChangeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'remark',placeholder:'请输入事项..'},
                    2:{type:"input",colName:'gmtCreate'},
                    3:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:4};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();
        vm.init();

        return vm;

    })
    .controller("FiveOaCardChangeDetailController", function ($state,$stateParams,$rootScope,$scope,fiveOaCardChangeService,commonCodeService,commonPrintTableService) {
        var vm = this;
        var uiSref="five.oaCardChange";
        var tableName = $rootScope.loadTableName(uiSref);

        var changeId = $stateParams.changeId;

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);
            commonCodeService.listDataByCatalog(user.enLogin,"充卡类型").then(function (value) {
                if (value.data.ret){
                    vm.cardTypeList=value.data.data;
                }
            })
        };

        vm.loadData = function (loadProcess) {
            fiveOaCardChangeService.getModelById(changeId).then(function (value) {
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
            fiveOaCardChangeService.update(vm.item).then(function (value) {
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
            }
        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='totalManger'){
                vm.item.totalManger = $scope.selectedOaUserLogins_;
                vm.item.totalMangerName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveOaCardChangeService.update(vm.item).then(function (value) {
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

        vm.showDetailModel = function (id,type) {
            if (id === 0) {
                fiveOaCardChangeService.getNewModelDetail(changeId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            } else {
                fiveOaCardChangeService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                    }
                })
            }
        };

        vm.loadDetail=function(){
            fiveOaCardChangeService.listDetail(changeId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaCardChangeService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            fiveOaCardChangeService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadData(true);
                }

            })
        };

        vm.chooseCardType=function(type){
            if (type=='cardNow'){
                var cardTypeNows = [];
                $(".cb_cardTypeNow:checked").each(function () {
                    cardTypeNows.push($(this).attr("data-name"));
                });
                vm.detail.cardTypeNow = cardTypeNows.join(',');
            }else {
                var cardTypeChanges = [];
                $(".cb_cardTypeChange:checked").each(function () {
                    cardTypeChanges.push($(this).attr("data-name"));
                });
                vm.detail.cardTypeChange = cardTypeChanges.join(',');
            }
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;

                    lodop.PRINT_INIT("固定资产调拨");
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

    //物资验收(办公家具)
    .controller("FiveOaEquipmentAcceptanceController", function ($state, $scope,$rootScope, fiveOaEquipmentAcceptanceService,hrDeptService) {
        var vm = this;
        var today=new Date()
        var day_30 = new Date().setMonth((new Date().getMonth()-1));    //日期是30天前的对应时间戳
        day_30 = new Date(day_30);


        vm.params ={qName: "",pageNum: 1,startTime:day_30,endTime:today,userName:"", pageSize: $scope.pageSize,total:0,projectNames:"",contactNos:"",selectDeptId:""};

        vm.pageInfo = {q:vm.params.qName,pageNum: vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        var uiSref="five.oaEquipmentAcceptance";
        var tableName = $rootScope.loadTableName(uiSref);


        vm.init=function(){
            hrDeptService.listMultipleDept().then(function (value) {
                if(value.data.ret){
                    vm.parents=value.data.data;
                }
            })

            $scope.loadRightData(user.userLogin,uiSref);
            $scope.basicInit("");
            vm.loadPagedData();
        }

        vm.downExcel=function(){
            var params={startTime:vm.params.startTime,endTime:vm.params.endTime,creatorName:vm.params.userName};
            fiveOaEquipmentAcceptanceService.downTempleXls(params).then(function (response) {

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
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
          //  var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime:vm.params.startTime,endTime:vm.params.endTime,creatorName:vm.params.userName};
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaEquipmentAcceptanceService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })

        };

        vm.show = function (id) {
            $state.go("five.oaEquipmentAcceptanceDetail", {equipmentId: id});
        };


        vm.add = function () {
            fiveOaEquipmentAcceptanceService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        };

        vm.fuzzySearch = function () {
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveOaEquipmentAcceptanceService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'associationName',placeholder:'请输入事项名称..'},
                    2:{type:"input",colName:'deptChargeName',placeholder:'创建人'},
                    3:{type:"input",colName:'gmtCreate'},
                    4:{type:"select",colName:'processEnd',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"运行中",value:0},{title:"已完成",value:1}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:5};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });
        vm.queryData();
        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveOaEquipmentAcceptanceService.remove(id, user.userLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.queryData();
                        }
                    });
                }
            })
        };

        vm.init();

        return vm;

    })
    .controller("FiveOaEquipmentAcceptanceDetailController", function ($state,$stateParams,$rootScope,$scope,commonPrintTableService,fiveOaEquipmentAcceptanceService,commonCodeService,commonFileService) {
        var vm = this;
        var uiSref="five.oaEquipmentAcceptance";
        var tableName = $rootScope.loadTableName(uiSref);

        var equipmentId = $stateParams.equipmentId;

        vm.init=function(){
            $scope.loadRightData(user.userLogin,uiSref);
            vm.loadData(true);

            commonCodeService.listDataByCatalog(user.enLogin,"充卡类型").then(function (value) {
                if (value.data.ret){
                    vm.cardTypeList=value.data.data;
                }
            })

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/file/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 50 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制50MB!');
                        return false;
                    }
                    data.formData = {businessKey:vm.detail.businessKey,dirId:0,enLogin: user.enLogin};
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
            fiveOaEquipmentAcceptanceService.getModelById(equipmentId).then(function (value) {
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
            fiveOaEquipmentAcceptanceService.update(vm.item).then(function (value) {
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
            }
        };

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='totalManger'){
                vm.item.totalManger = $scope.selectedOaUserLogins_;
                vm.item.totalMangerName = $scope.selectedOaUserNames_;
            }

        };

        //发送流程验证
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                var flag=true;
                vm.item.operateUserLogin = user.userLogin;
                 /*for(var i=0;i<vm.details.length;i++){
                     if (!vm.details[i].uploadfile){
                         flag=false;
                         toastr.warning("子项:"+vm.details[i].equipmentName+'  请上传对应实物图附件!');
                     }
                }
                 if (flag){

                 }*/
                fiveOaEquipmentAcceptanceService.update(vm.item).then(function (value) {
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
                fiveOaEquipmentAcceptanceService.getNewModelDetail(equipmentId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                        vm.loadDetailFile();
                    }
                })
            } else {
                fiveOaEquipmentAcceptanceService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                        vm.loadDetail();
                        vm.loadDetailFile();
                    }
                })
            }

        };

        vm.loadDetail=function(){
            fiveOaEquipmentAcceptanceService.listDetail(equipmentId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveOaEquipmentAcceptanceService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        vm.showDeptModal=function(id) {
            $scope.showOaSelectEmployeeModal_({title:"请选择部门",type:"选部门", deptIdList: vm.item.deptId+"",
                multiple:false,deptIds:"1",parentDeptId:2});
        };

        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            vm.detail.deptName = $scope.selectedOaDeptNames_;
            vm.detail.deptId = Number($scope.selectedOaDeptIds_);
        };


        vm.countTotalPrice=function(){
            var fin=vm.detail.price*vm.detail.number;
            vm.detail.totalPrice=fin.toFixed(2);
        }

        /*保存子项数据 */
        vm.updateDetail=function(){
            fiveOaEquipmentAcceptanceService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    vm.loadDetail();
                }
            })
        }

        vm.saveDetail = function () {
            fiveOaEquipmentAcceptanceService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $("#detailModal").modal("hide");
                    vm.loadData(true);
                }
            })
        };

        //子表图片上传
        vm.loadDetailFile=function(){
            commonFileService.listData(vm.detail.businessKey,0).then(function(value){
                if (value.data.ret){
                    vm.detailFileList=value.data.data;
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
                    //删除图片附件 保存一次detail 修改
                    vm.updateDetail();
                }
            })

        }

        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("物资验收(办公家具)");
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

    //固定资产调拨(低值易耗)
    .controller("FiveAssetAllotController", function ($state, $scope,$rootScope, fiveAssetAllotService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,uiSref:uiSref,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.assetAllot";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveAssetAllotService.downTempleXls(params).then(function (response) {

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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveAssetAllotService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;

                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.assetAllotDetail", {assetAllotId: id});
        }

        vm.add = function () {
            fiveAssetAllotService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveAssetAllotService.remove(id, user.userLogin).then(function (value) {
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
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin
            });
            fiveAssetAllotService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'assetName',placeholder:'请输入名称..'},
                    2:{type:"input",colName:'model',placeholder:'型号规格'},
                    3:{type:"input",colName:'useUnit',placeholder:'原使用单位'},
                    4:{type:"input",colName:'creatorName',placeholder:'创建人'},
                    5:{type:"input",colName:'gmtCreate',placeholder:'创建时间'},
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
    .controller("FiveAssetAllotDetailController", function ($state,$stateParams,$rootScope,$scope,fiveAssetAllotService,commonPrintTableService) {
        var vm = this;
        var assetAllotId = $stateParams.assetAllotId;
        var uiSref="five.assetAllot";
        var tableName = $rootScope.loadTableName(uiSref);
        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.assetAllot");
        }

        vm.loadData = function (loadProcess) {
            fiveAssetAllotService.getModelById(assetAllotId).then(function (value) {
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
            fiveAssetAllotService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applicant'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",deptIds:"1",userLoginList: vm.item.applicant,multiple:true});
            }
            else if(vm.status=='useMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择原保管人", type:"部门",deptIds:"1",userLoginList: vm.item.useMan,multiple:true});
            }
            else if(vm.status=='reserveMan'){
                $scope.showOaSelectEmployeeModal_({title:"请选择现保管人", type:"部门",deptIds:"1",userLoginList: vm.item.reserveMan,multiple:true});
            }
            else if(vm.status=='receive'){
                $scope.showOaSelectEmployeeModal_({title:"请选择现接收人", type:"部门",deptIds:"1",userLoginList: vm.item.receive,multiple:true});
            }



        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='applicant'){
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            else if ( vm.status=='useMan'){
                vm.item.useMan = $scope.selectedOaUserLogins_;
                vm.item.useManName = $scope.selectedOaUserNames_;
            }
            else if ( vm.status=='reserveMan'){
                vm.item.reserveMan = $scope.selectedOaUserLogins_;
                vm.item.reserveManName = $scope.selectedOaUserNames_;
            }
            else if ( vm.status=='receive'){
                vm.item.receive = $scope.selectedOaUserLogins_;
                vm.item.receiveName = $scope.selectedOaUserNames_;
            }
        };

        vm.showDeptModal=function(opt) {
            vm.opt = opt;
            if(vm.opt=='deptName'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请部门",type:"选部门", deptIdList: vm.item.deptId+"",
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt=='useUnit'){
                $scope.showOaSelectEmployeeModal_({title:"请选择原使用单位",type:"选部门", deptIdList: vm.item.useUnitId,
                    multiple:false,deptIds:"1",parentDeptId:2});
            }else if(vm.opt=='transferUnit'){
                $scope.showOaSelectEmployeeModal_({title:"请选择转入单位",type:"选部门", deptIdList: vm.item.transferUnitId,
                    multiple:false,deptIds:"1",parentDeptId:2});
            }

        }
        $rootScope.saveSelectDept_ =function() {
            $scope.closeOaSelectEmployeeModal_();
            if(vm.opt=='deptName'){
                vm.item.deptName = $scope.selectedOaDeptNames_;
                vm.item.deptId = Number($scope.selectedOaDeptIds_);
            } else if(vm.opt=='useUnit'){
                vm.item.useUnit = $scope.selectedOaDeptNames_;
                vm.item.useUnitId = Number($scope.selectedOaDeptIds_);
            } else if(vm.opt=='transferUnit'){
                vm.item.transferUnit = $scope.selectedOaDeptNames_;
                vm.item.transferUnitId = Number($scope.selectedOaDeptIds_);
            }

        }

        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveAssetAllotService.update(vm.item).then(function (value) {
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

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveAssetAllotService.getNewModelDetail(assetAllotId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveAssetAllotService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveAssetAllotService.removeDetail(id, user.userLogin).then(function (value) {
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
            fiveAssetAllotService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    fiveAssetAllotService.listDetail(assetAllotId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    })
                    $("#detailModal").modal("hide");
                }
            })
        };

        //打印
        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;

                    lodop.PRINT_INIT("固定资产调拨(低值易耗)");
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

        vm.init();

        return vm;

    })

    //固定资产设备报废(办公家具)
    .controller("FiveAssetScrapController", function ($state, $scope,$rootScope, fiveAssetScrapService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.assetScrap";
        var tableName = $rootScope.loadTableName(uiSref);


        vm.downExcel=function(){
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveAssetScrapService.downTempleXls(params).then(function (response) {

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
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:user.userLogin,uiSref:uiSref
            });
            fiveAssetScrapService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("five.assetScrapDetail", {assetScrapId: id});
        }

        vm.add = function () {
            fiveAssetScrapService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveAssetScrapService.remove(id, user.userLogin).then(function (value) {
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
            fiveAssetScrapService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'remark',placeholder:'请输入报废事项..'},
                    2:{type:"input",colName:'formNo',placeholder:'报废理由'},
                    3:{type:"input",colName:'deptName',placeholder:'申请单位'},
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


        return vm;

    })
    .controller("FiveAssetScrapDetailController", function ($state,$stateParams,$rootScope,$scope,commonCodeService,commonPrintTableService,fiveAssetScrapService,commonFileService) {
        var vm = this;
        var assetScrapId = $stateParams.assetScrapId;
        var uiSref="five.assetScrap";
        var tableName = $rootScope.loadTableName(uiSref);

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.assetScrap");
            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/file/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 50 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制50MB!');
                        return false;
                    }
                    data.formData = {businessKey:vm.detail.businessKey,dirId:0,enLogin: user.enLogin};
                    data.submit();
                },

                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        //toastr.success("上传成功!");
                        vm.save();
                        vm.loadDetailFile();
                        vm.updateDetail();
                        $scope.$apply();
                        //  $scope.loadPagedData();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        vm.loadData = function (loadProcess) {
            fiveAssetScrapService.getModelById(assetScrapId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.loadDetails();
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                }
            })


        };
        vm.loadDetails = function () {
            fiveAssetScrapService.listDetail(assetScrapId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveAssetScrapService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applicant'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",deptIds:"1",userLoginList: vm.item.applicant,multiple:false});
            }
            if (vm.status=='dutyPerson'){
                $scope.showOaSelectEmployeeModal_({title:"请选择责任人", type:"部门",deptIds:"1",userLoginList: vm.detail.dutyPerson,multiple:false});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='applicant'){
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if ( vm.status=='dutyPerson'){
                vm.detail.dutyPerson = $scope.selectedOaUserLogins_;
                vm.detail.dutyPersonName = $scope.selectedOaUserNames_;
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

         //发送流程
        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                var flag=true;
                vm.item.operateUserLogin = user.userLogin;
                vm.loadDetails();
                for(var i=0;i<vm.details.length;i++){
                    console.log(vm.details[i].uploadfile);
                    if (!vm.details[i].uploadfile){
                        flag=false;
                        toastr.warning("设备编号:"+vm.details[i].deviceNo+'  请上传对应实物图附件!');
                    }
                }
                if (flag){
                fiveAssetScrapService.update(vm.item).then(function (value) {
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

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveAssetScrapService.getNewModelDetail(assetScrapId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        vm.loadDetailFile();
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveAssetScrapService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        vm.loadDetailFile();
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveAssetScrapService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadData(false);
                        }
                    })
                }
            });
        };
        //保存
        vm.updateDetail =function(){
            fiveAssetScrapService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    vm.loadDetail();
                }
            })
        }
        //保存
        vm.saveDetail = function () {
            fiveAssetScrapService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    fiveAssetScrapService.listDetail(assetScrapId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    })
                    $("#detailModal").modal("hide");
                }
            })
        };
        //选择计算机所设备检定
        vm.showTypeModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"设备处置过程").then(function(value){
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
            var type = types.join(',');
            vm.item.handleResult =type;
            vm.save();
            $("#typeModal").modal("hide");
        }


        vm.loadDetail=function(){
            fiveOaEquipmentAcceptanceService.listDetail(assetScrapId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveAssetScrapService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                        }
                    })
                }
            });

        };

        //子表图片上传
        vm.loadDetailFile=function(){
            commonFileService.listData(vm.detail.businessKey,0).then(function(value){
                if (value.data.ret){
                    vm.detailFileList=value.data.data;
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
                    //删除图片附件 保存一次detail 修改
                    vm.updateDetail();
                }
            })

        }

        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("物资验收(办公家具)");
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

        vm.init();

        return vm;

    })

    //涉密计算机及外设设备报废审批
    .controller("FiveComputerScrapController", function ($state, $scope,$rootScope, fiveComputerScrapService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize: $scope.pageSize,total:0,uiSref:uiSref,startTime1:'',endTime1:''};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1};
        var uiSref="five.computerScrap";
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
            fiveComputerScrapService.downTempleXls(params).then(function (response) {

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
           // var params = {qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin};
            var params = $.extend(tablefilter.params, {
                qName:vm.params.qName,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,uiSref:uiSref,userLogin:user.userLogin,startTime1:vm.params.startTime1,endTime1:vm.params.endTime1
            });
            fiveComputerScrapService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
            $scope.loadRightData(user.userLogin,uiSref);
        };

        vm.show = function (id) {
            $state.go("five.computerScrapDetail", {computerScrapId: id});
        }

        vm.add = function () {
            fiveComputerScrapService.getNewModel( user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    fiveComputerScrapService.remove(id, user.userLogin).then(function (value) {
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
            fiveComputerScrapService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'applicantName',placeholder:'请输入申请人..'},
                    2:{type:"input",colName:'deptName',placeholder:'申请部门'},
                    3:{type:"input",colName:'applicantReason',placeholder:'申请理由'},
                    4:{type:"input",colName:'authenticateResult',placeholder:'计算机所设备检定意见'},
                    5:{type:"input",colName:'creatorName',placeholder:'创建人'},
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
    .controller("FiveComputerScrapDetailController", function ($state,$stateParams,$rootScope,$scope,commonCodeService,fiveComputerScrapService,commonPrintTableService) {
        var vm = this;
        var computerScrapId = $stateParams.computerScrapId;
        var tableName = $rootScope.loadTableName("five.computerScrap");

        vm.init=function(){
            vm.loadData(true);
            $scope.loadRightData(user.userLogin,"five.computerScrap");

        }

        vm.loadData = function (loadProcess) {
            fiveComputerScrapService.getModelById(computerScrapId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.loadDetails();
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#applicantTime").datepicker('setDate', vm.item.applicantTime);
                    $("#startTime").datepicker('setDate', vm.item.startTime);
                }
            })


        };
        vm.loadDetails = function () {
            fiveComputerScrapService.listDetail(computerScrapId).then(function (value) {
                if (value.data.ret) {
                    vm.details = value.data.data;
                }
            })
        };

        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveComputerScrapService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='applicant'){
                $scope.showOaSelectEmployeeModal_({title:"请选择申请人", type:"部门",deptIds:"1",userLoginList: vm.item.applicant,multiple:true});
            }
            if (vm.status=='dutyPerson'){
                $scope.showOaSelectEmployeeModal_({title:"请选择责任人", type:"部门",deptIds:"1",userLoginList: vm.item.dutyPerson,multiple:true});
            }

        }

        $rootScope.saveSelectEmployee_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='applicant'){
                vm.item.applicant = $scope.selectedOaUserLogins_;
                vm.item.applicantName = $scope.selectedOaUserNames_;
            }
            if ( vm.status=='dutyPerson'){
                vm.item.dutyPerson = $scope.selectedOaUserLogins_;
                vm.item.dutyPersonName = $scope.selectedOaUserNames_;
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

        $scope.showSendTask=function(send){
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                fiveComputerScrapService.update(vm.item).then(function (value) {
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

        vm.showDetailModel = function (id) {
            if (id === 0) {
                fiveComputerScrapService.getNewModelDetail(computerScrapId,user.userLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
                //修改
            } else {
                fiveComputerScrapService.getModelDetailById(id).then(function (value) {
                    if (value.data.ret) {
                        vm.detail = value.data.data;
                        $("#detailModal").modal("show");
                    }
                })
            }
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveComputerScrapService.removeDetail(id, user.userLogin).then(function (value) {
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
            fiveComputerScrapService.updateDetail(vm.detail).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    fiveComputerScrapService.listDetail(computerScrapId).then(function (value) {
                        if (value.data.ret) {
                            vm.details = value.data.data;
                        }
                    })
                    $("#detailModal").modal("hide");
                }
            })
        };
        //设备处理过程
        vm.showTypeModel=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"网络运维中心设备鉴定").then(function(value){
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

            var type = types.join(',');

            vm.item.authenticateResult =type;
            vm.save();
            $("#typeModal").modal("hide");
        }

        vm.showTypeModel1=function(){
            commonCodeService.listDataByCatalog(user.userLogin,"非密计算机设备处置过程").then(function(value){
                if (value.data.ret){
                    vm.types=value.data.data;
                    $("#typeModal1").modal("show");
                }
            })
        }

        vm.saveType1=function(){
            var types=[];
            $(".cb_type:checked").each(function () {
                types.push($(this).attr("data-name"));
            });

            var type = types.join(',');

            vm.item.handleResult =type;
            vm.save();
            $("#typeModal1").modal("hide");
        }

        vm.print=function () {
            commonPrintTableService.getPrintDate(vm.item.businessKey,user.userLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    vm.printData=value.data.data;
                    lodop.PRINT_INIT("涉密计算机及外设设备报废");
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

        vm.init();

        return vm;

    })