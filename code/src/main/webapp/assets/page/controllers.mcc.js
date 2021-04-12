angular.module('controllers.mcc', [])


    .controller("FootController", function ($state,$rootScope,$scope,$http,actTaskQueryService,actProcessQueryService,
                                            sysRoleService,sysAttachService, commonCodeService, edFileService,fiveBusinessContractLibrarySubpackageService,
                                            edProjectService, edProjectTreeService, actService,myActService,fiveBusinessContractLibraryService,
                                            hrDeptService,hrEmployeeService,fiveBudgetIndependentService,fiveFinanceSelfBankService,businessRecordService,
                                            fiveFinanceCompanyBankService,fiveBudgetFeeService,sysAclService) {

        $rootScope.forceH5Mode=function(){
            window.localStorage.removeItem("forcePcMode");
            window.location.replace("/h5/index.html");
        }

        //选择部门预算
        $rootScope.showBudgetSelectModal_=function(config) {
            $rootScope.budgetConfig =$.extend({
                budgetType:"fiveBudgetIndependent", //类型
                userLogin: user.userLogin},config);
            $rootScope.getBudgetInformation();
            $("#selectBudgetModal").modal("show");
        };
        $rootScope.getBudgetInformation = function() {
            //根据部门id 获取最新的部门预算
            fiveBudgetIndependentService.getBudgetIdByDeptId($rootScope.budgetConfig.budgetType,$rootScope.budgetConfig.deptId,$rootScope.budgetConfig.budgetYear).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selectBudget= value.data.data;
                    if($rootScope.selectBudget.budgetId==0){
                        toastr.warning($rootScope.selectBudget.budgetDeptName+" 缺少 "+ $rootScope.selectBudget.budgetYear+"年 部门预算记录！");
                        $('#budgetTreeTable').bootstrapTreeTable('destroy');
                        return;
                    }
                    //独立法人 职能单位
                    if($rootScope.selectBudget.budgetType=="fiveBudgetIndependent"||$rootScope.selectBudget.budgetType=="fiveBudgetFunction"){
                        fiveBudgetIndependentService.getDetailById($rootScope.selectBudget.budgetId).then(function (value) {
                            if (value.data.ret) {
                                $rootScope.selectBudgetDetails = value.data.data;
                                $('#budgetTreeTable').bootstrapTreeTable('destroy');
                                var treeTable = $('#budgetTreeTable').bootstrapTreeTable({
                                    toolbar: "#demo-toolbar1",    //顶部工具条
                                    expandColumn: 1,            // 在哪一列上面显示展开按钮
                                    showExport: true,
                                    height: 500,
                                    showColumns: true,          // 是否显示内容列下拉框
                                    showRefresh: true,
                                    expandAll: true,
                                    onClickRow:function(){
                                        $rootScope.getSelectBudgetInformation();
                                    },
                                    condensed: true,        //紧凑
                                    columns: [{
                                        field: 'selectItem',
                                        radio: true
                                    },
                                        {
                                            title: '类型名称',
                                            field: 'typeName',
                                            width: '40%',
                                            formatter: function (value, row, index) {
                                                if(row.lv==1){
                                                    return '<span style="color:red">'+value+'</span>';
                                                }
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetMoney',
                                            title: '本年预算金额',
                                            width: '20%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'budgetProportion',
                                            title: '本年预算占比',
                                            width: '15%',
                                            align: "left",
                                            formatter: function (value, row, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'remainMoney',
                                            title: '本年预算剩余金额',
                                            width: '20%',
                                            align: "left",
                                            valign: "top",
                                            formatter: function (value, item, index) {
                                                return value;
                                            }
                                        },
                                        {
                                            field: 'remark',
                                            title: '备注',
                                            width: '25%',
                                            align: "left",
                                        },
                                    ],
                                    data: value.data.data,
                                });
                                setTimeout(function () {
                                    $('#budgetTreeTable').bootstrapTreeTable('refresh');
                                },200);
                            }
                        })
                    }
                }
            })
        }

        $rootScope.getSelectBudgetInformation=function(){
            setTimeout(function () {
                var selecteds = $('#budgetTreeTable').bootstrapTreeTable('getSelections');
                var budgetDetailId = 0;
                $.each(selecteds, function (_i, _item) {
                    budgetDetailId = _item.id;
                });
                fiveBudgetIndependentService.getDetailByDetailId(budgetDetailId).then(function (value) {
                    $rootScope.selectBudgetDetail = value.data.data;
                })
            },200)
        }
        $rootScope.closeBudgetSelectModal_=function(){
            $('#budgetTreeTable').bootstrapTreeTable('destroy');
            $("#selectBudgetModal").modal("hide");
        }

        //选择网银 userBankNo 已选择的账号 userLogin uiSref
        $rootScope.showBankSelectModal_=function(config) {
            //当前选择的银行账号
            $rootScope.selectedUserBankNo =config.userBankNo;
            $rootScope.selectBank={};
            $rootScope.bankParams = $.extend({
                type:"", //类型
                userName:"",//账户名称
                bankNo:"",//账号
                bankName:"",//银行名称
                uiSref:"",
                pageNum: 1,
                pageSize: 5,
                userLogin: user.userLogin},config);
            //获取个人网银
            fiveFinanceSelfBankService.selectAllWithSuccess($rootScope.bankParams).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selfBankPageInfo = value.data.data;
                    //获取公司网银
                    fiveFinanceCompanyBankService.selectAll(config.userLogin,config.uiSref).then(function (value) {
                        if (value.data.ret) {
                            $rootScope.companyBanks = value.data.data;
                            $("#selectBankModal").modal("show");
                            singleCheckBox(".cb_selfBank", "data-name");
                            singleCheckBox(".cb_companyBank", "data-name");
                            $rootScope.getSelectBankInformation();
                            //选择 公司/个人 company/self
                            if(config.selectBankType=='company'){
                                $("#companyBankTab").attr("aria-expanded",true);
                                $("#companyBankLi").attr("class","active");
                                $("#tab_15_company").attr("class","tab-pane active");
                                $("#selfBankTab").attr("aria-expanded",false);
                                $("#selfBankLi").attr("class","");
                                $("#tab_15_self").attr("class","tab-pane ");
                            }else{
                                $("#companyBankTab").attr("aria-expanded",false);
                                $("#companyBankLi").attr("class","");
                                $("#tab_15_company").attr("class","tab-pane ");
                                $("#selfBankTab").attr("aria-expanded",true);
                                $("#selfBankLi").attr("class","active");
                                $("#tab_15_self").attr("class","tab-pane active");
                            }
                        }
                    })
                }
            })
        };
        $rootScope.loadBankPagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.bankParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.bankParams);
            params.pageNum=$rootScope.selfBankPageInfo.pageNum;
            params.pageSize=$rootScope.selfBankPageInfo.pageSize;

            fiveFinanceSelfBankService.selectAllWithSuccess(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selfBankPageInfo = value.data.data;
                }
            })
            //获取公司网银
            fiveFinanceCompanyBankService.selectAll(config.userLogin,config.uiSref).then(function (value) {
                if (value.data.ret) {
                    $rootScope.companyBanks = value.data.data;
                    $("#selectBankModal").modal("show");
                    singleCheckBox(".cb_selfBank", "data-name");
                    singleCheckBox(".cb_companyBank", "data-name");
                    $rootScope.getSelectBankInformation();
                    //选择 公司/个人 company/self
                    if(config.selectBankType=='company'){
                        $("#companyBankTab").attr("aria-expanded",true);
                        $("#companyBankLi").attr("class","active");
                        $("#tab_15_company").attr("class","tab-pane active");
                        $("#selfBankTab").attr("aria-expanded",false);
                        $("#selfBankLi").attr("class","");
                        $("#tab_15_self").attr("class","tab-pane ");
                    }else{
                        $("#companyBankTab").attr("aria-expanded",false);
                        $("#companyBankLi").attr("class","");
                        $("#tab_15_company").attr("class","tab-pane ");
                        $("#selfBankTab").attr("aria-expanded",true);
                        $("#selfBankLi").attr("class","active");
                        $("#tab_15_self").attr("class","tab-pane active");
                    }
                }
            })
        };
        $rootScope.getSelectBankInformation=function(){
            setTimeout(function () {
                if ($(".cb_companyBank:checked").length > 0) {
                    var companyBank = $.parseJSON($(".cb_companyBank:checked").first().attr("data-name"));
                    $rootScope.selectBank.type = "公司网银";
                    $rootScope.selectBank.userName = companyBank.userName;
                    $rootScope.selectBank.bankNo = companyBank.userBankNo;
                    $rootScope.selectBank.bankName = companyBank.bankName;
                }else if ($(".cb_selfBank:checked").length > 0) {
                    var selfBank = $.parseJSON($(".cb_selfBank:checked").first().attr("data-name"));
                    $rootScope.selectBank.type = "个人网银";
                    $rootScope.selectBank.userName = selfBank.userName;
                    $rootScope.selectBank.bankNo = selfBank.userBankNo;
                    $rootScope.selectBank.bankName = selfBank.bankName;
                }else{
                    $rootScope.selectBank.type = "";
                    $rootScope.selectBank.userName = "";
                    $rootScope.selectBank.bankNo = "";
                    $rootScope.selectBank.bankName = "";
                }
                $rootScope.$apply();
            },200)
        }
        $rootScope.closeBankSelectModal_=function(){
            $("#selectBankModal").modal("hide");
        }



        //选择签入合同库 selectLibraryId 已经选择的合同库id  userLogin uiSref
        $rootScope.showLibrarySelectModal_=function(config) {
            $rootScope.libraryParams = $.extend({
                contractName:"",
                contractNo:"",
                projectNo:"",
                signTime:"",
                contractType:"",
                contractMoney:"",
                deptName:"",
                creatorName:"",
                uiSref:"",
                pageNum: 1,
                pageSize: 10,
                userLogin: user.userLogin},config);
            //当前选择的合同信息
            $rootScope.selectLibraryId =config.selectLibraryId;
            if($rootScope.selectLibraryId!=0&&$rootScope.selectLibraryId!=''){
                fiveBusinessContractLibraryService.getModelById($rootScope.selectLibraryId).then(function (value) {
                    if (value.data.ret) {
                        $rootScope.selectedLibrary = value.data.data;
                    }
                })
            }

            $rootScope.libraryPageInfo = {pageNum: $rootScope.libraryParams.pageNum, pageSize:
                $rootScope.libraryParams.pageSize, total: $rootScope.libraryParams.total};
            fiveBusinessContractLibraryService.listPagedDataCommon($rootScope.libraryParams).then(function (value) {
                if (value.data.ret) {
                    $rootScope.libraryPageInfo = value.data.data;
                    $("#commonSelectLibraryModal").modal("show");
                    singleCheckBox(".cb_commonLibrary", "data-name");
                    //绑定当前已选中的合同
                    $rootScope.getSelectLibraryInformation();
                }
            })
        };
        $rootScope.loadLibraryPagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.libraryParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.libraryParams);
            params.pageNum=$rootScope.libraryPageInfo.pageNum;
            params.pageSize=$rootScope.libraryPageInfo.pageSize;

            fiveBusinessContractLibraryService.listPagedDataCommon(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.libraryPageInfo = value.data.data;
                    singleCheckBox(".cb_commonLibrary", "data-name");
                }
            })
        };
        $rootScope.searchLibraryPagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.libraryParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.libraryParams);
            params.pageNum=1;
            params.pageSize=10;
            fiveBusinessContractLibraryService.listPagedDataCommon(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.libraryPageInfo = value.data.data;
                    singleCheckBox(".cb_commonLibrary", "data-name");
                }
            })
        };
        $rootScope.getSelectLibraryInformation=function(){
            setTimeout(function () {
                if ($(".cb_commonLibrary:checked").length > 0) {
                    var selectedLibraryId = $.parseJSON($(".cb_commonLibrary:checked").first().attr("data-name"));
                    fiveBusinessContractLibraryService.getModelById(selectedLibraryId).then(function (value) {
                        if (value.data.ret) {
                            $rootScope.selectedLibrary = value.data.data;
                        }
                    })
                }else{
                   /* $rootScope.selectedLibrary ={id:0,contractName:"",contractNo:"",projectName:"",projectNo:"",
                        contractMoney:"",contractType:"",projectNature:""}*/
                }
            },200)
        }

        $rootScope.closeLibrarySelectModal_=function(){
            $("#commonSelectLibraryModal").modal("hide");
        }
        
        //选择签出合同库 selectLibraryId 已经选择的合同库id  userLogin uiSref
        $rootScope.showLibrarySubpackageSelectModal_=function(config) {
            $rootScope.librarySubpackageParams = $.extend({
                purchase:false, //是否为采购
                subContractName:"",
                subContractNo:"",
                supplierName:"",
                contractNo:"",
                subContractMoney:"",
                deptName:"",
                creatorName:"",
                uiSref:"",
                pageNum: 1,
                pageSize: 10,
                userLogin: user.userLogin},config);
            //当前选择的合同信息
            $rootScope.selectLibrarySubpackageId =config.selectLibrarySubpackageId;
            if($rootScope.selectLibrarySubpackageId!=0&&$rootScope.selectLibrarySubpackageId!=''){
                fiveBusinessContractLibrarySubpackageService.getModelById($rootScope.selectLibrarySubpackageId).then(function (value) {
                    if (value.data.ret) {
                        $rootScope.selectedLibrarySubpackage = value.data.data;
                    }
                })
            }

            $rootScope.librarySubpackagePageInfo = {pageNum: $rootScope.librarySubpackageParams.pageNum, pageSize: $rootScope.librarySubpackageParams.pageSize,
                total: $rootScope.librarySubpackageParams.total};
            fiveBusinessContractLibrarySubpackageService.listPagedDataSelect($rootScope.librarySubpackageParams).then(function (value) {
                if (value.data.ret) {
                    $rootScope.librarySubpackagePageInfo = value.data.data;
                    $("#commonSelectLibrarySubpackageModal").modal("show");
                    singleCheckBox(".cb_commonLibrarySubpackage", "data-name");
                    //绑定当前已选中的合同
                    $rootScope.getSelectLibrarySubpackageInformation();
                }
            })
        };
        $rootScope.loadLibrarySubpackagePagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.librarySubpackageParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.librarySubpackageParams);
            params.pageNum=$rootScope.librarySubpackagePageInfo.pageNum;
            params.pageSize=$rootScope.librarySubpackagePageInfo.pageSize;
            fiveBusinessContractLibrarySubpackageService.listPagedDataSelect(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.librarySubpackagePageInfo = value.data.data;
                    singleCheckBox(".cb_commonLibrarySubpackage", "data-name");
                }
            })
        };
        $rootScope.searchLibrarySubpackagePagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.librarySubpackageParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.librarySubpackageParams);
            params.pageNum=1;
            params.pageSize=10;
            fiveBusinessContractLibrarySubpackageService.listPagedDataSelect(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.librarySubpackagePageInfo = value.data.data;
                    singleCheckBox(".cb_commonLibrarySubpackage", "data-name");
                }
            })
        };
        $rootScope.getSelectLibrarySubpackageInformation=function(){
            setTimeout(function () {
                if ($(".cb_commonLibrarySubpackage:checked").length > 0) {
                    var selectedLibrarySubpackageId = $.parseJSON($(".cb_commonLibrarySubpackage:checked").first().attr("data-name"));
                    fiveBusinessContractLibrarySubpackageService.getModelById(selectedLibrarySubpackageId).then(function (value) {
                        if (value.data.ret) {
                            $rootScope.selectedLibrarySubpackage = value.data.data;
                        }
                    })
                }
            },200)
        }

        $rootScope.closeLibrarySubpackageSelectModal_=function(){
            $("#commonSelectLibrarySubpackageModal").modal("hide");
        }


        //选择备案项目 selectRecordId 已经选择的备案id  userLogin uiSref
        $rootScope.showRecordSelectModal_=function(config) {
            $rootScope.recordParams = $.extend({
                projectName:"",
                projectNo:"",
                deptName:"",
                customerName:"",
                businessUserName:"",
                projectType:"",
                uiSref:"",
                pageNum: 1,
                pageSize: 10,
                userLogin: user.userLogin},config);
            //当前选择的合同信息
            $rootScope.selectRecordId =config.selectRecordId;
            if($rootScope.selectRecordId!=0&&$rootScope.selectRecordId!=''){
                businessRecordService.getModelById($rootScope.selectRecordId).then(function (value) {
                    if (value.data.ret) {
                    $rootScope.selectedRecord = value.data.data;
                }
                })
            }
            $rootScope.recordPageInfo = {pageNum: $rootScope.recordParams.pageNum, pageSize: $rootScope.recordParams.pageSize, total: $rootScope.recordParams.total};
            businessRecordService.listPagedDataCommon($rootScope.recordParams).then(function (value) {
                if (value.data.ret) {
                    $rootScope.recordPageInfo = value.data.data;
                    $("#commonSelectRecordModal").modal("show");
                    singleCheckBox(".cb_commonRecord", "data-name");
                    //绑定当前已选中的合同
                    $rootScope.getSelectRecordInformation();
                }
            })
        };
        $rootScope.loadRecordPagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.recordParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.recordParams);
            params.pageNum=$rootScope.recordPageInfo.pageNum;
            params.pageSize=$rootScope.recordPageInfo.pageSize;

            businessRecordService.listPagedDataCommon(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.recordPageInfo = value.data.data;
                    singleCheckBox(".cb_commonRecord", "data-name");
                }
            })
        };
        $rootScope.searchRecordPagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.recordParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.recordParams);
            params.pageNum=1;
            params.pageSize=10;
            businessRecordService.listPagedDataCommon(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.recordPageInfo = value.data.data;
                    singleCheckBox(".cb_commonRecord", "data-name");
                }
            })
        };
        $rootScope.getSelectRecordInformation=function(){
            setTimeout(function () {
                if ($(".cb_commonRecord:checked").length > 0) {
                    var selectedRecordId = $.parseJSON($(".cb_commonRecord:checked").first().attr("data-name"));
                    businessRecordService.getModelById(selectedRecordId).then(function (value) {
                        if (value.data.ret) {
                            $rootScope.selectedRecord = value.data.data;
                        }
                    })
                }
            },200)
        }

        $rootScope.closeRecordSelectModal_=function(){
            $("#commonSelectRecordModal").modal("hide");
        }


        //选择客户信息 selectCustomerId 已经选择的合同库id  userLogin uiSref
        $rootScope.showCustomerSelectModal_=function(config) {
            $rootScope.customerParams = $.extend({
                contractName:"",
                contractNo:"",
                projectNo:"",
                signTime:"",
                contractType:"",
                contractMoney:"",
                deptName:"",
                creatorName:"",
                uiSref:"",
                pageNum: 1,
                pageSize: 10,
                userLogin: user.userLogin},config);
            //当前选择的合同信息
            $rootScope.selectCustomerId =config.selectCustomerId;
            if($rootScope.selectCustomerId!=0&&$rootScope.selectCustomerId!=''){
                fiveBusinessContractCustomerService.getModelById($rootScope.selectCustomerId).then(function (value) {                    if (value.data.ret) {
                    $rootScope.selectedCustomer = value.data.data;
                }
                })
            }

            $rootScope.customerPageInfo = {pageNum: $rootScope.customerParams.pageNum, pageSize: $rootScope.customerParams.pageSize, total: $rootScope.customerParams.total};
            fiveBusinessContractCustomerService.listPagedDataCommon($rootScope.customerParams).then(function (value) {
                if (value.data.ret) {
                    $rootScope.customerPageInfo = value.data.data;
                    $("#commonSelectCustomerModal").modal("show");
                    singleCheckBox(".cb_commonCustomer", "data-name");
                    //绑定当前已选中的合同
                    $rootScope.getSelectCustomerInformation();
                }
            })
        };
        $rootScope.loadCustomerPagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.customerParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.customerParams);
            params.pageNum=$rootScope.customerPageInfo.pageNum;
            params.pageSize=$rootScope.customerPageInfo.pageSize;

            fiveBusinessContractCustomerService.listPagedDataCommon(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.customerPageInfo = value.data.data;
                    singleCheckBox(".cb_commonCustomer", "data-name");
                }
            })
        };
        $rootScope.searchCustomerPagedData = function () {
            var params = $.extend({
                uiSref:$rootScope.customerParams.uiSref,
                userLogin: user.userLogin
            },$rootScope.customerParams);
            params.pageNum=1;
            params.pageSize=10;
            fiveBusinessContractCustomerService.listPagedDataCommon(params).then(function (value) {
                if (value.data.ret) {
                    $rootScope.customerPageInfo = value.data.data;
                    singleCheckBox(".cb_commonCustomer", "data-name");
                }
            })
        };
        $rootScope.getSelectCustomerInformation=function(){
            setTimeout(function () {
                if ($(".cb_commonCustomer:checked").length > 0) {
                    var selectedCustomerId = $.parseJSON($(".cb_commonCustomer:checked").first().attr("data-name"));
                    fiveBusinessContractCustomerService.getModelById(selectedCustomerId).then(function (value) {
                        if (value.data.ret) {
                            $rootScope.selectedCustomer = value.data.data;
                        }
                    })
                }
            },200)
        }

        $rootScope.closeCustomerSelectModal_=function(){
            $("#commonSelectCustomerModal").modal("hide");
        }






        commonCodeService.selectAll().then(function (value) {
            if(value.data.ret){
                $rootScope.sysCodes=value.data.data;
                $rootScope.commonCodes=value.data.data;
            }
        });

        $rootScope.judgePassword=function(){
            //判断上一次修改时间距今多少天
            var dateSpan,
                tempDate,
                iDays;
            sDate1 = user.gmtModified;
            sDate2 = Date.parse(new Date());
            dateSpan = sDate2 - sDate1;
            dateSpan = Math.abs(dateSpan);
            iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
            if (iDays>30){
                toastr.warning("您的密码已过期，请及时修改!")
            }
        }

        $rootScope.showResetInformation=function(){
            $rootScope.pwd1="";
            $rootScope.pwd2="";
            $("#personalInformationModal").modal("show");

            $(".uploadUserAttach").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'json',
                send: function (e, data) {
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {enLogin: user.enLogin};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    if (data.result.ret) {
                        $scope.loadLoginUser();
                        toastr.success("上传成功!");
                    }
                }
            });


        }

        $rootScope.updateUserInformation=function() {
            if ($rootScope.pwd1.length == 0) {
                toastr.error("密码不能为空!");
                return;
            }
            if ($rootScope.pwd1 != $rootScope.pwd2) {
                $rootScope.pwd2 = "";
                toastr.error("两次输入密码不一致!");
                return;
            }
            if (checkPass($rootScope.pwd1) < 4) {
                $rootScope.pwd2 = "";
                toastr.error("新密码复杂度不够，请重新设置！需6-16位，且包含大小写字母和数字和特殊符号。");
                return;
            }
            hrEmployeeService.resetPassword(user.userLogin, $rootScope.pwd1).then(function (value) {
                if (value.data.ret) {
                    toastr.success("密码修改成功!");
                    $("#personalInformationModal").modal("hide");
                }
            })
        }


        $scope.loadLoginUser=function () {
            $http({
                method: 'POST',
                url: '/user/loginByLogin.json',
                params: {
                    userLogin:user.enLogin
                }
            }).then(function (value) {
                $rootScope.user=value.data.data;
                            })
        }


        function checkPass(pass){
            if(pass.length < 6&&pass.length>16){
                return 0;
            }
            var str = 0;
            if(pass.match(/([a-z])+/)){
                str++;
            }
            if(pass.match(/([0-9])+/)){
                str++;
            }
            if(pass.match(/([A-Z])+/)){
                str++;
            }
            if(pass.match(/[ _`~!@#$%^&*()\-+=|{}':;',\[\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+/)){
                str++;
            }
            return str;
        }
        $rootScope.showVersion=function(edFileId) {
            edFileService.showVersion(edFileId).then(function (value) {
                if (value.data.ret) {
                    $rootScope.edFileHistoryList = value.data.data;
                    $("#edFileHistoryModal").modal("show");
                }
            })
        }


        /**
         * 通用用户选择框
         * @param selectUserLoginList 默认选中的userLoginList  ,luodong,
         * @param singleCheck 是否单选
         * @param employeeName 默认查询姓名
         * @param deptName 默认查询部门
         * @param modalTitle 弹出窗口名称
         */
        $rootScope.selectEmployeeModal=function(selectUserLoginList,singleCheck,employeeName,deptName,modalTitle){
            $rootScope.qSelectEmployeeDeptName = deptName;
            $rootScope.qSelectEmployeeName=employeeName;
            $rootScope.qSelectUserLoginList=selectUserLoginList;
            $rootScope.selectEmployeeCommonModalTitle="人员信息";
            if(modalTitle){
                $rootScope.selectEmployeeCommonModalTitle=modalTitle;
            }
            hrEmployeeService.selectAll().then(function (value) {
                $rootScope.selectEmployees = value.data.data;

                setTimeout(function () {
                    if(singleCheck){
                        singleCheckBox(".cb_select_employee", "data-name");
                    }else{
                        initCheckBox(".cb_select_employee");
                    }
                },500);

            });
            $("#selectEmployeeCommonModal").modal("show");
        }

        $rootScope.getSelectEmployeeList=function(noCloseModal){
            var userLoginList = [];
            $(".cb_select_employee:checked").each(function () {
                var userLogin = $(this).attr("data-name");
                userLoginList.push(userLogin);
            });
            if(!noCloseModal){
                $("#selectEmployeeCommonModal").modal("hide");
            }
            return userLoginList;
        }

        $rootScope.download=function(attachId){
            sysAttachService.download(attachId).then(function (response) {
                handleDownloadResponse(response);
            });
        }

        $rootScope.downloadEdFileWithXml=function(edFile){

            if($rootScope.browserVersion=='ie9') {
                window.open("/sys/attach/downloadEdFile/"+edFile.id);
            }else {
                sysAttachService.downloadEdFile(edFile.id).then(function (response) {
                    handleDownloadResponse(response);
                });
                if (edFile.fileName.indexOf(".dwg") >= 0) {
                    sysAttachService.downloadEdFileXml(edFile.id).then(function (response) {
                        handleDownloadResponse(response);
                    });
                }
            }
        }

        $rootScope.batchDownloadEdFile=function () {
            $(".cb_edFile:checked").each(function () {
                var index = $(this).attr("data-index");
                if (index < $rootScope.files.length) {
                    $rootScope.downloadEdFileWithXml($rootScope.files[index]);
                }
            });
        }

        $rootScope.selectAllEdFile=function () {
            $(".cb_edFile").each(function () {
                $(this).attr("checked",true);
            });
        }

        $rootScope.cancelAllEdFile=function () {
            $(".cb_edFile").each(function () {
                $(this).removeAttr("checked");
            });
        }

        function handleDownloadResponse(response){
            try{
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9).replace("%2B","+");
                var blobData = new Blob([response.data.data], { type: response.data.data.type });
                // for IE
                if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                    window.navigator.msSaveOrOpenBlob(blobData, fileName);
                }else {
                    var objectUrl = URL.createObjectURL(blobData);
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = decodeURI(fileName);
                    a.href = objectUrl;
                    a.click();
                }
            }catch (e) {

            }
        }

        /**
         * 基础的控件初始化
         * @param businessId
         */
        $rootScope.basicInit=function(businessId){
            $('.form-validate').validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block',
                ignore: "",
                highlight: function (element) { // hightlight error inputs
                    $(element).closest('div').addClass('has-error'); // set error class to the control group
                },
                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },
                errorPlacement: function (error, element) { // render error placement for each input type
                    if (element.parent(".input-group").size() > 0) {
                        error.insertAfter(element.parent(".input-group"));
                    } else if (element.attr("data-error-container")) {
                        error.appendTo(element.attr("data-error-container"));
                    } else if (element.parents('.radio-list').size() > 0) {
                        error.appendTo(element.parents('.radio-list').attr("data-error-container"));
                    } else if (element.parents('.radio-inline').size() > 0) {
                        error.appendTo(element.parents('.radio-inline').attr("data-error-container"));
                    } else if (element.parents('.checkbox-list').size() > 0) {
                        error.appendTo(element.parents('.checkbox-list').attr("data-error-container"));
                    } else if (element.parents('.checkbox-inline').size() > 0) {
                        error.appendTo(element.parents('.checkbox-inline').attr("data-error-container"));
                    } else {
                        error.insertAfter(element); // for other inputs, just perform default behavior
                    }
                }
            });

            $.fn.datetimepicker.dates['zh-CN'] = {
                days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
                daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
                daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
                months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                today: "今天",
                suffix: [],
                meridiem: ["上午", "下午"]
            };

            $('.date-picker').datepicker({
                orientation: "auto",
                autoclose: true,
                language: 'zh-CN'
            });

            $('.date-picker-year').datepicker({
                format: 'yyyy',
                language: "zh-CN",
                autoclose:true,
                startView: 2,
                minViewMode: 2,
                maxViewMode: 2
            });
            $('.date-picker-year-month').datepicker({
                format: 'yyyy-mm',
                language: "zh-CN",
                autoclose:true,
                startView: 1,
                minViewMode: 1,
                maxViewMode: 1
            });
            $('.date-picker-start-now').datepicker({
                orientation: "auto",
                autoclose: true,
                language: 'zh-CN',
                startDate:new Date()
            });

            $('.form_datetime').datetimepicker({
                autoclose: true,
                language: 'zh-CN',
                isRTL: Metronic.isRTL(),
                minuteStep:15,
                pickerPosition: (Metronic.isRTL() ? "bottom-right" : "bottom-left")
            });

           /* $('#editable-select').editableSelect({
                bg_iframe: false,
                case_sensitive: false,
                items_then_scroll: 10 ,
                isFilter:false,
                effects:'slide'
            });*/

            $("#detail_form").validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block',
                ignore: "",
                highlight: function (element) { // hightlight error inputs
                    $(element).closest('div').addClass('has-error'); // set error class to the control group
                },
                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },
                errorPlacement: function (error, element) { // render error placement for each input type
                    if (element.parent(".input-group").size() > 0) {
                        error.insertAfter(element.parent(".input-group"));
                    } else if (element.attr("data-error-container")) {
                        error.appendTo(element.attr("data-error-container"));
                    } else if (element.parents('.radio-list').size() > 0) {
                        error.appendTo(element.parents('.radio-list').attr("data-error-container"));
                    } else if (element.parents('.radio-inline').size() > 0) {
                        error.appendTo(element.parents('.radio-inline').attr("data-error-container"));
                    } else if (element.parents('.checkbox-list').size() > 0) {
                        error.appendTo(element.parents('.checkbox-list').attr("data-error-container"));
                    } else if (element.parents('.checkbox-inline').size() > 0) {
                        error.appendTo(element.parents('.checkbox-inline').attr("data-error-container"));
                    } else {
                        error.insertAfter(element); // for other inputs, just perform default behavior
                    }
                }
            });

            $(document).on("show.bs.modal", ".modal.draggable-modal", function(){
                $(this).draggable();
                $(this).css("overflow-y", "scroll");
            });

            // 通过该方法来为每次弹出的模态框设置最新的zIndex值，从而使最新的modal显示在最前面
            $(document).on('show.bs.modal', '.modal', function() {
                var zIndex = 1040 + (10 * $('.modal:visible').length);
                $(this).css('z-index', zIndex);
                setTimeout(function() {
                    $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
                }, 0);
            });

            if(businessId) {
                $scope.loadEdFiles(businessId);
                $("#uploadEdFile").fileupload({
                    dataType: 'text',
                    url:'/ed/file/receive.do',
                    formData: {businessId: businessId,userLogin:user.userLogin,source:"ed"},
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
                            toastr.success("上传成功!");
                            $scope.loadEdFiles(result.data.businessId);
                        } else {
                            toastr.error("上传失败!");
                        }
                    }
                });

            }


            $('.page-container input').keypress(function (e) {
                if (e.which == 13) {

                    if ($(".defaultBtn").length == 1) {
                        $(".defaultBtn").click();
                    }
                    return false;
                }
            });

        }

        $rootScope.validateForm = function (formId, rules, messages) {
            if (rules) {
                if (!messages) {
                    messages = {};
                }

                $("#" + formId).validate({
                    errorElement: 'span', //default input error message container
                    errorClass: 'help-block',
                    highlight: function (element) { // hightlight error inputs
                        $(element).closest('div').addClass('has-error'); // set error class to the control group
                    },
                    success: function (label) {
                        label.closest('div').removeClass('has-error');
                        label.remove();
                    },
                    rules: rules,
                    messages: messages
                });
            }
        };

        /**
         * 加载流程情况
         * @param processInstanceId
         */
        $rootScope.loadProcessInstance = function (processInstanceId) {
            if (processInstanceId) {
                $rootScope.loadNewProcessInstance(processInstanceId);
            }
        };

        /**
         * 加载流程情况 签名意见
         * @param processInstanceId
         */
        $rootScope.loadOpinionProcessInstance = function (processInstanceId) {
            if (processInstanceId) {
                //五洲待签名附件
                actTaskQueryService.listHistoricTaskInstanceByInstanceId(processInstanceId, user.enLogin).then(function (value) {
                    var list = value.data.data;
                    list.reverse();//2020-12-19HNZ 倒序取最新意见
                    var optionlist=[];
                    for (var i=0,l=list.length;i<l;i++){
                        for(var j = i + 1; j < l; j++){
                            //去除重复数据，没有处理人或者是取回任务
                            if (list[i].name == list[j].name&&list[i].assigneeName==list[j].assigneeName
                                && list[j].assigneeName!=""&&list[j].latestComment.indexOf("取回")<0){
                                ++i;
                            }
                        }
                        optionlist.push(list[i]);
                    }
                    optionlist.reverse(); //倒序 顺序展示意见
                    $rootScope.optionlist=optionlist;

                    //流程意见分类 2020-12-19HNZ 使用去除重复后意见
                    var optionlistCountSign=[];
                    var optionlistLeader=[];
                    var optionlistOther=[];
                    for (var i=0,l=optionlist.length;i<l;i++){
                            if(optionlist[i].name.indexOf("会签")>0){
                                optionlistCountSign.push(optionlist[i])
                            }else if(optionlist[i].name.indexOf("领导")>0&&optionlist[i].name.indexOf("机要秘书")<0){
                                optionlistLeader.push(optionlist[i])
                            }else {
                                optionlistOther.push(optionlist[i])
                            }
                    }
                    $rootScope.optionlistCountSign=optionlistCountSign;
                    $rootScope.optionlistLeader=optionlistLeader.reverse();
                    $rootScope.optionlistOther=optionlistOther;
                   // $scope.$apply();
                });
            }
        };

        $rootScope.loadRightData=function(userLogin,uiSref){
            sysRoleService.getAclInfoByUserLogin(userLogin,uiSref).then(function (value) {
                $rootScope.rightData=value.data.data;
            })
        }

        /**
         * 加载业务附件
         * @param businessId
         */
        $rootScope.loadEdFiles = function (businessId) {
            edFileService.listData(businessId).then(function (value) {
                if (value.data.ret) {
                    $rootScope.files = value.data.data;
                    $rootScope.$broadcast("loadEdFile", "已重新加载业务附件列表!");
                }
            });

        }

        $rootScope.showEdFile = function (item) {
            $rootScope.edFile = $.extend({}, item);
            $("#edFileModal").modal("show");
        }

        $rootScope.saveEdFile = function () {
            $("#edFileModal").modal("hide");
            edFileService.updateFileType($rootScope.edFile.id, $rootScope.edFile.fileType, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $scope.loadEdFiles($rootScope.edFile.businessId);
                }
            })
        }

        $rootScope.removeEdFile = function (item) {
            bootbox.confirm("您确认要删除附件吗?", function (result) {
                if (result) {
                    edFileService.remove(item.id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!");
                            $scope.loadEdFiles(item.businessId);
                        }
                    });
                }
            });
        }

        /**
         * 发起人取回任务
         * @param processInstanceId
         * @param userLogin
         */
        $rootScope.fetchFlow = function (processInstanceId,userLogin) {
            bootbox.prompt("请输入取回理由?", function(result) {
                if (result) {
                    myActService.fetchFlow(processInstanceId,userLogin,result).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("取回成功!");
                            $rootScope.$broadcast("fetchFlowSuccess", "已重新取回任务!");
                            $rootScope.loadProcessInstance(processInstanceId);
                        }
                    })
                }
            });
        }

        $rootScope.commonFetchFlow=function(){
            $rootScope.fetchFlow($rootScope.processInstance.processInstanceId,user.userLogin);
        }

        $rootScope.showSendFlow = function (params) {
            if (!params) {
                params = {taskId: $rootScope.processInstance.myTaskId,userLogin:user.userLogin};
            }
            $rootScope.sendFlowParams = params;
            myActService.listNextStep(params).then(function (value) {
                if (value.data.ret) {
                    var jsTreeData = value.data.data;
                    $('#sendFlowTree').jstree("destroy")
                    $('#sendFlowTree').jstree({
                        'core': {
                            'data': jsTreeData
                        },
                        // "force_text": true,
                        "plugins": ["checkbox"],
                        "checkbox" : {
                            "keep_selected_style" : false
                        }
                    });

                    $('#sendFlowTree').on('changed.jstree', function (e, data) {
                        if (data != undefined && data.node != undefined && data.action == "deselect_node") {
                            var currentNodeId = data.node.id;
                            if(data.node.parent==="#"){
                                var children = data.instance.get_node(currentNodeId).children;
                                if(children.length===1){
                                    data.instance.check_node(currentNodeId);
                                    toastr.info("处理人不能为空!")
                                }else{
                                    for (var i = 0; i < children.length; i++) {
                                        var childNode = data.instance.get_node(children[i]);
                                        if (childNode.state.selected) {
                                            return;
                                        }
                                    }
                                    if(children.length>1){
                                        data.instance.check_node(children[0]);
                                        toastr.info("处理人不能为空!")
                                    }
                                }
                            }else {
                                var children = data.instance.get_node(data.node.parent).children;
                                if (children.length === 1) {
                                    data.instance.check_node(data.node.parent);
                                    toastr.info("处理人不能为空!")
                                } else {
                                    for (var i = 0; i < children.length; i++) {
                                        var childNode = data.instance.get_node(children[i]);
                                        if (childNode.state.selected) {
                                            return;
                                        }
                                    }
                                    data.instance.check_node(currentNodeId);
                                    toastr.info("处理人不能为空!")
                                }
                            }
                        }




                        if (data != undefined && data.node != undefined && data.action == "select_node") {


                            var currentNodeId = data.node.id;
                            var selectedNodes = data.instance.get_selected(true);



                            if (currentNodeId.split('_').length > 1) {
                                $.each(selectedNodes, function (i, nd) {
                                    if(nd.id.split('_').length>1) {
                                        var ndParent = nd.parent;
                                        if (!data.instance.get_node(ndParent).original.multiple) {
                                            if (nd.parent == data.node.parent && nd.id != data.node.id) {
                                                data.instance.uncheck_node(nd);
                                            }
                                        }
                                    }
                                });
                            } else {
                                if (!data.instance.get_node(currentNodeId).original.multiple) {
                                    var checked = false;
                                    for (var i = 0; i < selectedNodes.length; i++) {
                                        if (selectedNodes[i].parent == currentNodeId) {
                                            if (checked) {
                                                data.instance.uncheck_node(selectedNodes[i]);
                                            } else {
                                                checked = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    $("#sendFlowComment").val("同意");
                    $("#sendFlowModal").modal("show");
                }
            });


            $("#sendFlowForm").validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block',
                highlight: function (element) { // hightlight error inputs
                    $(element).closest('div').addClass('has-error'); // set error class to the control group
                },
                success: function (label) {
                    label.closest('div').removeClass('has-error');
                    label.remove();
                }
            });

        };

        $rootScope.sendFlow = function () {
            if ($("#sendFlowForm").validate().form()) {
                var users = $('#sendFlowTree').jstree().get_selected();
                $("#sendFlowModal").modal("hide");
                myActService.sendFlow($rootScope.processInstance.myTaskId, users, user.userLogin, $("#sendFlowComment").val()).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("发送成功!");
                        $scope.loadProcessInstance($scope.processInstanceId);
                        $scope.$broadcast("sendFlowSuccess", "发送流程成功!");
                    }
                });
            } else {
                toastr.warning("请准确填写数据!")
            }
        }

        $rootScope.showBackFlow = function (taskId,userLogin) {
            $scope.backFlowParams = {taskId:taskId,userLogin:userLogin};
            myActService.listBackStep($scope.backFlowParams).then(function (value) {
                if (value.data.ret) {
                    var jsTreeData = value.data.data;
                    $('#backFlowTree').jstree("destroy")
                    $('#backFlowTree').jstree({
                        'core': {
                            'data': jsTreeData
                        },
                        "plugins": ["checkbox"]
                    });

                    $('#backFlowTree').on('changed.jstree', function (e, data) {
                        if (data != undefined && data.node != undefined && data.action == "select_node") {
                            var currentNodeId = data.node.id;
                            var selectedNodes = data.instance.get_selected(true);
                            if (currentNodeId.split('_').length ===1) {
                                $.each(selectedNodes, function (i, nd) {
                                    //如果是选择的父节点员工 或者 就是父节点自己 或者是并行任务
                                    if(nd.parent===currentNodeId||nd.id===currentNodeId ||(nd.id.split('-')[0]===currentNodeId.split('-')[0])){

                                    }else{
                                        data.instance.uncheck_node(nd);
                                    }
                                });
                            } else {
                                var parent=currentNodeId.split('_')[0];
                                $.each(selectedNodes, function (i, nd) {
                                    //如果是选择的父节点员工 或者 就是父节点自己
                                    if(nd.id===parent){

                                    }else if(nd.parent===parent){
                                        if (!data.instance.get_node(parent).original.multiple) {
                                            if (nd.id != currentNodeId) {
                                                data.instance.uncheck_node(nd);
                                            }
                                        }
                                    }else{
                                        if(parent.split('-')[0]!==nd.id.split('-')[0]) {
                                            data.instance.uncheck_node(nd);
                                        }
                                    }
                                });

                            }
                        }
                    });
                    $("#backFlowComment").val("打回");
                    $("#backFlowModal").modal("show");
                }
            })
            $("#backFlowForm").validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block',
                highlight: function (element) { // hightlight error inputs
                    $(element).closest('div').addClass('has-error'); // set error class to the control group
                },
                success: function (label) {
                    label.closest('div').removeClass('has-error');
                    label.remove();
                }
            });
        }

        $rootScope.commonBackFlow = function () {
            $rootScope.showBackFlow($scope.processInstance.myTaskId,user.userLogin);
        }

        $rootScope.backFlow = function () {
            if ($("#backFlowForm").validate().form()) {
                var users = $('#backFlowTree').jstree().get_selected();
                if (users.length == 0) {
                    toastr.error("请选择打回的流程节点!");
                    return;
                }
                $("#backFlowModal").modal("hide");
                myActService.backFlow($scope.processInstance.myTaskId,users,user.userLogin,$("#backFlowComment").val()).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("打回成功!");
                        $scope.loadProcessInstance($scope.processInstanceId);
                        $scope.$broadcast("backFlowSuccess", "打回流程成功!");
                    }
                });
            } else {
                toastr.warning("请准确填写数据!")
            }
        }



        /** OA 选人（根据部门,角色,被选人员） 选部门 通用
         * title:窗口名字
         * 查询条件：  type:”部门”，按照部门查询 ”角色”，按照角色查询，”选部门”，选择部门,“人员”,按照传递人员 查询
         * *            deptIds:可查询的部门及其子部门  parentDeptId:当前查询的部门及它下级
         *            roleIds:可查询的角色  parentRoleId:当前查询的角色
         *              userLoginList：勾选上的人,multiple:单选还是多选
         *               deptIdList:已选中部门
         *
         *
         * @param config
         * @private
         */
        $rootScope.showOaSelectEmployeeModal_=function(config) {
            $rootScope.selectOaEmployeeConfig=$.extend($.extend({},$rootScope.defaultOaSelectEmployeeConfig),config);
            $rootScope.selectOaEmployeeParam={name:"",qualify:" ",specialty:"",position:"",
                parentDeptId:$rootScope.selectOaEmployeeConfig.parentDeptId,
                parentDeptIds:$rootScope.selectOaEmployeeConfig.parentDeptIds,
                deptId:0,
                parentRoleId:$rootScope.selectOaEmployeeConfig.parentRoleId,
                roleId:0,
                appCode:$rootScope.selectOaEmployeeConfig.appCode,
                pageNum:1,pageSize:10};
            var deptTreeId = "oa_employee_dept_tree_";
            //根据是否传入deptIds 生成对应树
            if($rootScope.selectOaEmployeeConfig.type=="角色"){
                $rootScope.selectOaEmployeeParam.type='角色';
                /* 根据deptId生成角色树*/
                $rootScope.initOaRoleEmployeeTree();
            }else if( $rootScope.selectOaEmployeeConfig.type=="部门") {
                $rootScope.selectOaEmployeeParam.type='部门';
                /* 根据deptId生成部门树*/
                $rootScope.initOaDeptEmployeeTree();
            } else if( $rootScope.selectOaEmployeeConfig.type=="人员") {
                $rootScope.selectOaEmployeeParam.type='人员';
                /* 根据deptId生成部门树*/
                $rootScope.initOaUserEmployeeTree();
            } else if( $rootScope.selectOaEmployeeConfig.type=="选部门") {
                $rootScope.selectOaEmployeeParam.type='部门';
                //处理deptIdList 配合单选多选
                var tempDeptIdList = ","+$rootScope.selectOaEmployeeConfig.deptIdList+",";
                $rootScope.selectOaEmployeeConfig.deptIdList = tempDeptIdList;
                /* 根据deptId生成部门树*/
                $rootScope.initOaDeptTree();
            }else if ($rootScope.selectOaEmployeeConfig.type=="领导"){
                $rootScope.selectOaEmployeeParam.type='领导';
                /* 根据deptId生成部门树*/
                $rootScope.initOaDeptLeaderTree();
            }
            $rootScope.initMetronic();
        };
        $rootScope.closeOaSelectEmployeeModal_=function(){
            $("#OaSelectEmployeeModal").modal("hide");
        }

        $rootScope.initOaEmployeeModal=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            $rootScope.defaultOaSelectEmployeeConfig = {
                title: "人员信息",
                multiple: true,
                userLoginList: "",
                deptIdList: "",
                type:"",
                parentDeptId: 1,
                parentDeptIds: "",
                parentRoleId: 1,
                parentRoleIds: "",
                containChild: true,
                appCode:user.appCode,
                bigDept:false,
            };
            $rootScope.selectOaTreeNodeIds=[];
            $rootScope.selectOaTreeOpenIds=[];
        }
        //人员 -人员
        $rootScope.initOaUserEmployeeTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            hrEmployeeService.selectByUserLoginList($rootScope.selectOaEmployeeConfig.userLogins).then(function (value) {
                var list = value.data.data;
                $rootScope.userTreeList=list;
                // 输入框内容 联想匹配
                for (var i = 0; i < list.length; i++) {
                    //添加姓名 全拼写
                    list[i].userNamePY =pinyin.getFullChars(list[i].userName);
                    //添加姓名 简拼
                    list[i].userNamePY2 =pinyin.getCamelChars(list[i].userName);
                }
                var userName = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.userName, data: { category: '人员姓名',data:team}};
                    }
                });
                var userNamePY = $.map(list, function (team) {
                    if (team.userLogin != "") {
                        return {value: team.userNamePY, data: {category: '姓名拼音', data: team}};
                    }
                });
                var userNamePY2 = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.userNamePY2, data: { category: '姓名拼音简写',data:team}};
                    }
                });
                var userLogin = $.map(list, function (team) {
                    if (team.userLogin != "") {
                        return {value: team.userLogin, data: {category: '职工号', data: team}};
                    }
                });

                var team1 = userName;
                var team2 = team1.concat(userLogin);
                var team3 = team2.concat(userNamePY);
                var team4 = team3.concat(userNamePY2);
                //初始化 模糊搜索
                $('#autocomplete').devbridgeAutocomplete({
                    lookup: team4,
                    //查询条数限制
                    lookupLimit:10,
                    minChars: 1,
                    showNoSuggestionNotice: true,
                    noSuggestionNotice: '未匹配到相应数据',
                    groupBy: 'category',
                    //匹配条件过滤
                    lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
                        return  suggestion.value.toLowerCase().indexOf(queryLowerCase) !== -1;
                    },
                    //返回结果格式
                    formatResult:function(suggestion, currentValue){
                        return suggestion.value +" - "+ suggestion.data.data.userName;
                    },
                    //选择事件
                    onSelect: function (suggestion) {
                        if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + suggestion.data.data.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + suggestion.data.data.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + suggestion.data.data.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                    },
                });
                //加载jstree数据
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.userName,
                        icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                        a_attr:{
                            userLogin:item.userLogin,
                            style:
                                item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                        },
                        state: {opened: (item.parentId === 0),
                            disabled: false,
                            selected: false}
                    };
                    treeData.push(node);
                }
                $('#' + deptTreeId).jstree("destroy");
                $('#' + deptTreeId).on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if(data.action==="select_node") {
                            //判断是否为人员节点
                            if(node.a_attr.userLogin!=''){
                                //存在
                                if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                                }else{
                                    //判断是否多选
                                    if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                    } else {
                                        var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                    }
                                    $rootScope.bindOaSelectedUser_();
                                }
                            }else {
                                data.instance.open_node(node.id);
                            }
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData,
                            'multiple':false
                        },
                        "plugins" :
                            ["search"],
                        'search' : {
                            'search_leaves_only' : false,
                            'search_callback' : function(string,node){
                                if(string !== '' && node.text.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                    $rootScope.selectOaTreeNodeIds.push(node.id);
                                    $rootScope.selectOaTreeOpenIds.push(node.parents);
                                }
                            }
                        },
                    });
                $rootScope.bindOaSelectedUser_();
                if($rootScope.selectOaEmployeeConfig.userLoginList){
                    hrEmployeeService.listDataByUserLoginList($rootScope.selectOaEmployeeConfig.userLoginList).then(function (value) {
                        $rootScope.selectedOaUsers_=value.data.data;
                    })
                }
                $("#OaSelectEmployeeModal").modal("show");
            });

        }
        $rootScope.refreshOaUserEmployeeTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            var list = $rootScope.userTreeList;
            //加载jstree数据
            var treeData = [];
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                var node = {
                    id: item.id,
                    parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                    text: item.userName,
                    icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                    a_attr:{
                        userLogin:item.userLogin,
                        style:
                            item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                    },
                    state: {opened: (item.parentId === 0),
                        disabled: false,
                        selected: false}
                };
                if(item.parentId===0){
                    $rootScope.defaultOaSelectEmployeeConfig.parentDeptId=item.id;
                }
                //如果被查询 标记为红色
                if($rootScope.selectOaTreeNodeIds.indexOf(item.id+"")>-1){
                    node.a_attr.style="color:red";
                }
                treeData.push(node);
            }
            $('#' + deptTreeId).jstree("destroy");
            $('#' + deptTreeId)
                .on('changed.jstree', function (e, data) {
                    var node = data.instance.get_node(data.selected[0]);
                    if(data.action==="select_node") {
                        //判断是否为人员节点
                        if(node.a_attr.userLogin!=''){
                            //存在
                            if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                        }else {
                            data.instance.open_node(node.id);
                        }
                    }
                })
                .jstree({
                    'core': {
                        'data': treeData,
                        'multiple':false
                    },
                    "plugins" :
                        ["search"],
                    'search' : {
                        'search_leaves_only' : false,
                        'search_callback' : function(string,node){
                            //判断人员姓名
                            if(string !== '' && node.text.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                            //判断姓名全拼音
                            if(string !== '' && pinyin.getFullChars(node.text).toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                            //判断姓名简拼音
                            if(string !== '' && pinyin.getCamelChars(node.text).toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                            //判断登录名
                            if(string !== '' && node.a_attr.userLogin.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                        }
                    },
                });
        }
        //人员 -部门-领导
        $rootScope.initOaDeptLeaderTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            hrDeptService.selectByDeptLeader($rootScope.selectOaEmployeeConfig.deptIds).then(function (value) {
                var list = value.data.data;
                $rootScope.deptTreeList=list;
                // 输入框内容 联想匹配
                for (var i = 0; i < list.length; i++) {
                    //添加姓名 全拼写
                    list[i].userNamePY =pinyin.getFullChars(list[i].name);
                    //添加姓名 简拼
                    list[i].userNamePY2 =pinyin.getCamelChars(list[i].name);
                }
                var userName = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.name, data: { category: '人员姓名',data:team}};
                    }
                });
                var userNamePY = $.map(list, function (team) {
                    if (team.userLogin!= "") {
                        return {value: team.userNamePY, data: {category: '姓名拼音', data: team}};
                    }
                });
                var userNamePY2 = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.userNamePY2, data: { category: '姓名拼音简写',data:team}};
                    }
                });
                var userLogin = $.map(list, function (team) {
                    if (team.userLogin != "") {
                        return {value: team.userLogin, data: {category: '职工号', data: team}};
                    }
                });
                var deptName = $.map(list, function (team) {
                    if(team.userLogin==""){
                        return { value: team.name, data: { category: '部门名称',data:team} };
                    }
                });

                var team1 = userName.concat(deptName);
                var team2 = team1.concat(userLogin);
                var team3 = team2.concat(userNamePY);
                var team4 = team3.concat(userNamePY2);
                //初始化 模糊搜索
                $('#autocomplete').devbridgeAutocomplete({
                    lookup: team4,
                    //查询条数限制
                    lookupLimit:10,
                    minChars: 1,
                    width:300,
                    showNoSuggestionNotice: true,
                    noSuggestionNotice: '未匹配到相应数据',
                    groupBy: 'category',
                    //匹配条件过滤
                    lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
                        return  suggestion.value.toLowerCase().indexOf(queryLowerCase) !== -1;
                    },
                    //返回结果格式
                    formatResult:function(suggestion, currentValue){
                        if(suggestion.data.category=="部门名称"){
                            return suggestion.value
                        }else{
                            return suggestion.value +" - "+ suggestion.data.data.name
                                +" - "+suggestion.data.data.userDeptName;
                        }
                    },
                    //选择事件
                    onSelect: function (suggestion) {
                        if(suggestion.data.category=="部门名称"){
                            //tree 查找
                            //$('#' + deptTreeId).jstree(true).search(suggestion.value);
                            $rootScope.selectOaEmployeeConfig.parentDeptId = Number(suggestion.data.data.id);
                            $rootScope.refreshOaDeptEmployeeTree();
                        }else{
                            if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + suggestion.data.data.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + suggestion.data.data.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + suggestion.data.data.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                        }
                    },
                });
                //加载jstree数据
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                        a_attr:{
                            userLogin:item.userLogin,
                            style:
                                item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                        },
                        state: {opened: (item.parentId === 0||item.id===$rootScope.selectOaEmployeeConfig.parentDeptId
                                ||$rootScope.selectOaEmployeeConfig.parentDeptIds.indexOf(item.id+"") > -1),
                            disabled: false,
                            selected: false}
                    };
                    if(item.parentId===0){
                        $rootScope.defaultOaSelectEmployeeConfig.parentDeptId=item.id;
                    }
                    treeData.push(node);
                }
                $('#' + deptTreeId).jstree("destroy");
                $('#' + deptTreeId)
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if(data.action==="select_node") {
                            //判断是否为人员节点
                            if(node.a_attr.userLogin!=''){
                                //存在
                                if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                                }else{
                                    //判断是否多选
                                    if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                    } else {
                                        var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                    }
                                    $rootScope.bindOaSelectedUser_();
                                }
                            }else {
                                data.instance.open_node(node.id);
                            }
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData,
                            'multiple':false
                        },
                        "plugins" :
                            ["search"],
                        'search' : {
                            'search_leaves_only' : false,
                            'search_callback' : function(string,node){
                                if(string ==node.text){
                                    //修改被查询颜色
                                    /* node.a_attr.style ='color:red';
                                     $("#"+node.id+'_anchor').css("color","red");*/
                                }
                            }
                        },
                    });

                $('#'+deptTreeId).jstree("deselect_all", true);
                $('#'+deptTreeId).jstree('select_node',$rootScope.selectOaEmployeeConfig.parentDeptId);

                //显示初始人员
                $rootScope.bindOaSelectedUser_();
                if($rootScope.selectOaEmployeeConfig.userLoginList){
                    hrEmployeeService.listDataByUserLoginList($rootScope.selectOaEmployeeConfig.userLoginList).then(function (value) {
                        $rootScope.selectedOaUsers_=value.data.data;
                    })
                }
                $("#OaSelectEmployeeModal").modal("show");
            });

        }
        $rootScope.refreshOaDeptLeaderTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            var list = $rootScope.deptTreeList;
            //加载jstree数据
            var treeData = [];
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                var node = {
                    id: item.id,
                    parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                    text: item.name,
                    icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                    a_attr:{
                        userLogin:item.userLogin,
                        style:
                            item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                    },
                    state: {opened: (item.parentId === 0||item.id===$rootScope.selectOaEmployeeConfig.parentDeptId
                            ||$rootScope.selectOaEmployeeConfig.parentDeptIds.indexOf(item.id+"") > -1
                            ||$rootScope.selectOaTreeOpenIds.indexOf(item.id+"") > -1),
                        disabled: false,
                        selected: false}
                };
                if(item.parentId===0){
                    $rootScope.defaultOaSelectEmployeeConfig.parentDeptId=item.id;
                }
                //如果被查询 标记为红色
                if($rootScope.selectOaTreeNodeIds.indexOf(item.id+"")>-1){
                    node.a_attr.style="color:red";
                }
                treeData.push(node);
            }
            $('#' + deptTreeId).jstree("destroy");
            $('#' + deptTreeId)
                .on('changed.jstree', function (e, data) {
                    var node = data.instance.get_node(data.selected[0]);
                    if(data.action==="select_node") {
                        //判断是否为人员节点
                        if(node.a_attr.userLogin!=''){
                            //存在
                            if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                        }else {
                            data.instance.open_node(node.id);
                        }
                    }
                })
                .jstree({
                    'core': {
                        'data': treeData,
                        'multiple':false
                    },
                    "plugins" :
                        ["search"],
                    'search' : {
                        'search_leaves_only' : false,
                        'search_callback' : function(string,node){

                        }
                    },
                });
        }
        //人员 -部门
        $rootScope.initOaDeptEmployeeTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            hrDeptService.selectByDeptIds($rootScope.selectOaEmployeeConfig.deptIds).then(function (value) {
                var list = value.data.data;
                $rootScope.deptTreeList=list;
                // 输入框内容 联想匹配
                for (var i = 0; i < list.length; i++) {
                    //添加姓名 全拼写
                    list[i].userNamePY =pinyin.getFullChars(list[i].name);
                    //添加姓名 简拼
                    list[i].userNamePY2 =pinyin.getCamelChars(list[i].name);
                }
                var userName = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.name, data: { category: '人员姓名',data:team}};
                    }
                });
                var userNamePY = $.map(list, function (team) {
                    if (team.userLogin!= "") {
                        return {value: team.userNamePY, data: {category: '姓名拼音', data: team}};
                    }
                });
                var userNamePY2 = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.userNamePY2, data: { category: '姓名拼音简写',data:team}};
                    }
                });
                var userLogin = $.map(list, function (team) {
                    if (team.userLogin != "") {
                        return {value: team.userLogin, data: {category: '职工号', data: team}};
                    }
                });
                var deptName = $.map(list, function (team) {
                    if(team.userLogin==""){
                        return { value: team.name, data: { category: '部门名称',data:team} };
                    }
                });

                var team1 = userName.concat(deptName);
                var team2 = team1.concat(userLogin);
                var team3 = team2.concat(userNamePY);
                var team4 = team3.concat(userNamePY2);
                //初始化 模糊搜索
                $('#autocomplete').devbridgeAutocomplete({
                    lookup: team4,
                    //查询条数限制
                    lookupLimit:10,
                    minChars: 1,
                    width:300,
                    showNoSuggestionNotice: true,
                    noSuggestionNotice: '未匹配到相应数据',
                    groupBy: 'category',
                    //匹配条件过滤
                    lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
                        return  suggestion.value.toLowerCase().indexOf(queryLowerCase) !== -1;
                    },
                    //返回结果格式
                    formatResult:function(suggestion, currentValue){
                        if(suggestion.data.category=="部门名称"){
                            return suggestion.value
                        }else{
                            return suggestion.value +" - "+ suggestion.data.data.name
                                +" - "+suggestion.data.data.userDeptName;
                        }
                    },
                    //选择事件
                    onSelect: function (suggestion) {
                        if(suggestion.data.category=="部门名称"){
                            //tree 查找
                            //$('#' + deptTreeId).jstree(true).search(suggestion.value);
                            $rootScope.selectOaEmployeeConfig.parentDeptId = Number(suggestion.data.data.id);
                            $rootScope.refreshOaDeptEmployeeTree();
                        }else{
                            if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + suggestion.data.data.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + suggestion.data.data.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + suggestion.data.data.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                        }
                    },
                });
                //加载jstree数据
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                        a_attr:{
                            userLogin:item.userLogin,
                            style:
                                item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                        },
                        state: {opened: (item.parentId === 0||item.id===$rootScope.selectOaEmployeeConfig.parentDeptId
                                ||$rootScope.selectOaEmployeeConfig.parentDeptIds.indexOf(item.id+"") > -1),
                            disabled: false,
                            selected: false}
                    };
                    if(item.parentId===0){
                        $rootScope.defaultOaSelectEmployeeConfig.parentDeptId=item.id;
                    }
                    treeData.push(node);
                }
                $('#' + deptTreeId).jstree("destroy");
                $('#' + deptTreeId)
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if(data.action==="select_node") {
                            //判断是否为人员节点
                            if(node.a_attr.userLogin!=''){
                                //存在
                                if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                                }else{
                                    //判断是否多选
                                    if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                    } else {
                                        var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                    }
                                    $rootScope.bindOaSelectedUser_();
                                }
                            }else {
                                data.instance.open_node(node.id);
                            }
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData,
                            'multiple':false
                        },
                        "plugins" :
                            ["search"],
                        'search' : {
                            'search_leaves_only' : false,
                            'search_callback' : function(string,node){
                                if(string ==node.text){
                                    //修改被查询颜色
                                   /* node.a_attr.style ='color:red';
                                    $("#"+node.id+'_anchor').css("color","red");*/
                                }
                            }
                        },
                    });

                $('#'+deptTreeId).jstree("deselect_all", true);
                $('#'+deptTreeId).jstree('select_node',$rootScope.selectOaEmployeeConfig.parentDeptId);

                //显示初始人员
                $rootScope.bindOaSelectedUser_();
                if($rootScope.selectOaEmployeeConfig.userLoginList){
                    hrEmployeeService.listDataByUserLoginList($rootScope.selectOaEmployeeConfig.userLoginList).then(function (value) {
                        $rootScope.selectedOaUsers_=value.data.data;
                    })
                }
                $("#OaSelectEmployeeModal").modal("show");
            });

        }
        $rootScope.refreshOaDeptEmployeeTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            var list = $rootScope.deptTreeList;
            //加载jstree数据
            var treeData = [];
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                var node = {
                    id: item.id,
                    parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                    text: item.name,
                    icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                    a_attr:{
                        userLogin:item.userLogin,
                        style:
                            item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                    },
                    state: {opened: (item.parentId === 0||item.id===$rootScope.selectOaEmployeeConfig.parentDeptId
                            ||$rootScope.selectOaEmployeeConfig.parentDeptIds.indexOf(item.id+"") > -1
                            ||$rootScope.selectOaTreeOpenIds.indexOf(item.id+"") > -1),
                        disabled: false,
                        selected: false}
                };
                if(item.parentId===0){
                    $rootScope.defaultOaSelectEmployeeConfig.parentDeptId=item.id;
                }
                //如果被查询 标记为红色
                if($rootScope.selectOaTreeNodeIds.indexOf(item.id+"")>-1){
                    node.a_attr.style="color:red";
                }
                treeData.push(node);
            }
            $('#' + deptTreeId).jstree("destroy");
            $('#' + deptTreeId)
                .on('changed.jstree', function (e, data) {
                    var node = data.instance.get_node(data.selected[0]);
                    if(data.action==="select_node") {
                        //判断是否为人员节点
                        if(node.a_attr.userLogin!=''){
                            //存在
                            if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                        }else {
                            data.instance.open_node(node.id);
                        }
                    }
                })
                .jstree({
                    'core': {
                        'data': treeData,
                        'multiple':false
                    },
                    "plugins" :
                        ["search"],
                    'search' : {
                        'search_leaves_only' : false,
                        'search_callback' : function(string,node){

                        }
                    },
                });
        }
        //人员 -角色
        $rootScope.initOaRoleEmployeeTree=function() {
            var roleTreeId = "oa_employee_dept_tree_";
            sysRoleService.listSortedByRoleIds($rootScope.selectOaEmployeeConfig.roleIds).then(function (value) {
                var list = value.data.data;
                $rootScope.roleTreeList=list;
                // 输入框内容 联想匹配
                for (var i = 0; i < list.length; i++) {
                    //添加姓名 全拼写
                    list[i].userNamePY =pinyin.getFullChars(list[i].name);
                    //添加姓名 简拼
                    list[i].userNamePY2 =pinyin.getCamelChars(list[i].name);
                }
                var userName = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.name, data: { category: '人员姓名',data:team}};
                    }
                });
                var userNamePY = $.map(list, function (team) {
                    if (team.userLogin != "") {
                        return {value: team.userNamePY, data: {category: '姓名拼音', data: team}};
                    }
                });
                var userNamePY2 = $.map(list, function (team) {
                    if(team.userLogin!=""){
                        return { value: team.userNamePY2, data: { category: '姓名拼音简写',data:team}};
                    }
                });
                var userLogin = $.map(list, function (team) {
                    if (team.userLogin != "") {
                        return {value: team.userLogin, data: {category: '职工号', data: team}};
                    }
                });
                var roleName = $.map(list, function (team) {
                    if(team.userLogin==""){
                        return { value: team.name, data: { category: '角色名称',data:team} };
                    }
                });

                var team1 = userName.concat(roleName);
                var team2 = team1.concat(userLogin);
                var team3 = team2.concat(userNamePY);
                var team4 = team3.concat(userNamePY2);
                //初始化 模糊搜索
                $('#autocomplete').devbridgeAutocomplete({
                    lookup: team4,
                    //查询条数限制
                    lookupLimit:10,
                    minChars: 1,
                    showNoSuggestionNotice: true,
                    noSuggestionNotice: '未匹配到相应数据',
                    groupBy: 'category',
                    //匹配条件过滤
                    lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
                        return  suggestion.value.toLowerCase().indexOf(queryLowerCase) !== -1;
                    },
                    //返回结果格式
                    formatResult:function(suggestion, currentValue){
                        if(suggestion.data.category=="角色名称"){
                            return suggestion.value
                        }else{
                            return suggestion.value +" - "+ suggestion.data.data.name;
                        }
                    },
                    //选择事件
                    onSelect: function (suggestion) {
                        if(suggestion.data.category=="角色名称"){
                            //tree 查找
                            $('#' + deptTreeId).jstree(true).search(suggestion.value);
                        }else{
                            if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + suggestion.data.data.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + suggestion.data.data.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + suggestion.data.data.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                        }
                    },
                });
                //加载jstree数据
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId),
                        text: item.name,
                        icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                        a_attr:{
                            userLogin:item.userLogin,
                            style:
                                item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                        },
                        state: {opened: (item.parentId === 0||item.id===parseInt($rootScope.selectOaEmployeeConfig.parentRoleId)
                                ||$rootScope.selectOaEmployeeConfig.parentRoleIds.indexOf(item.id+"") > -1),
                            disabled: false,
                            selected: false}
                    };
                    if ( $rootScope.selectOaEmployeeParam.parentRoleId == parseInt(item.id)) {
                        node.state.selected = true;
                    }
                    if(item.deleted){
                        node.text=node.text+"(作废)";
                    }
                    treeData.push(node);
                }
                $('#'+roleTreeId).jstree("destroy");
                $('#'+roleTreeId)
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if(data.action==="select_node") {
                            //判断是否为人员节点
                            if(node.a_attr.userLogin!=''){
                                //存在
                                if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                                }else{
                                    //判断是否多选
                                    if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                    } else {
                                        var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                        $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                    }
                                    $rootScope.bindOaSelectedUser_();
                                }
                            }else {
                                data.instance.open_node(node.id);
                            }
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData
                        },
                        "plugins" : [
                            "search"],
                        'search' : {
                            'search_leaves_only' : false,
                            'search_callback' : function(string,node){
                                if(string !== '' && node.text.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                    $rootScope.selectOaTreeNodeIds.push(node.id);
                                    $rootScope.selectOaTreeOpenIds.push(node.parents);
                                }
                            }
                        },
                    });

                var selectedNodes=$('#'+roleTreeId).jstree(true).get_selected(true);
                if(selectedNodes.length===1&&parseInt(selectedNodes[0].id)===$rootScope.selectOaEmployeeConfig.parentRoleId){
                    $rootScope.queryOaRoleSelectEmployee_();
                }else{
                    $('#'+roleTreeId).jstree("deselect_all", true);
                    $('#'+roleTreeId).jstree('select_node',$rootScope.selectOaEmployeeConfig.parentRoleId);
                }
                $rootScope.bindOaSelectedUser_();
                if($rootScope.selectOaEmployeeConfig.userLoginList){
                    hrEmployeeService.listDataByUserLoginList($rootScope.selectOaEmployeeConfig.userLoginList).then(function (value) {
                        $rootScope.selectedOaUsers_=value.data.data;
                    })
                }

                $("#OaSelectEmployeeModal").modal("show");
            });

        }
        $rootScope.refreshOaRoleEmployeeTree=function() {
            var roleTreeId = "oa_employee_dept_tree_";
            var list = $rootScope.roleTreeList;
            //加载jstree数据
            var treeData = [];
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                var node = {
                    id: item.id,
                    parent: (item.parentId === 0 ? "#" : item.parentId),
                    text: item.name,
                    icon: (item.userLogin!=''&&$rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "icon-check":item.icon),
                    a_attr:{
                        userLogin:item.userLogin,
                        style:
                            item.userLogin!=''&&($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + item.userLogin + ",") >= 0 ? "color:green":"")
                    },
                    state: {opened: (item.parentId === 0||item.id===$rootScope.selectOaEmployeeConfig.parentRoleId
                            ||$rootScope.selectOaEmployeeConfig.parentRoleIds.indexOf(item.id+"") > -1
                            ||$rootScope.selectOaTreeOpenIds.indexOf(item.id+"") > -1),
                        disabled: false,
                        selected: false}
                };
                if ( $rootScope.selectOaEmployeeParam.parentRoleId == parseInt(item.id)) {
                    node.state.selected = true;
                }
                //如果被查询 标记为红色
                if($rootScope.selectOaTreeNodeIds.indexOf(item.id+"")>-1){
                    node.a_attr.style="color:red";
                }
                if(item.deleted){
                    node.text=node.text+"(作废)";
                }
                treeData.push(node);
            }
            $('#'+roleTreeId).jstree("destroy");
            $('#'+roleTreeId)
                .on('changed.jstree', function (e, data) {
                    var node = data.instance.get_node(data.selected[0]);
                    if(data.action==="select_node") {
                        //判断是否为人员节点
                        if(node.a_attr.userLogin!=''){
                            //存在
                            if($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + node.a_attr.userLogin + ",") >= 0 ){

                            }else{
                                //判断是否多选
                                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + node.a_attr.userLogin + ",";
                                } else {
                                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + node.a_attr.userLogin + ",";
                                }
                                $rootScope.bindOaSelectedUser_();
                            }
                        }else {
                            data.instance.open_node(node.id);
                        }
                    }
                })
               /* .on('hover_node.jstree', function (e, data) { //鼠标移上事件
                    var node = data.node.original;
                    //判断是否为人员节点
                    if(node.a_attr.userLogin!=''){
                        hrEmployeeService.getModelByUserLogin(node.a_attr.userLogin).then(function (value) {
                            if (value.data.ret) {
                                $rootScope.selectOaEmployee = value.data.data;
                                $rootScope.selectOaEmployeeParam.type='人员'
                            }
                        });
                    }else {
                        if(node.id!=9999){
                            sysRoleService.getModelById(node.id).then(function (value) {
                                if (value.data.ret) {
                                    $rootScope.selectOaRole = value.data.data;
                                    $rootScope.selectOaEmployeeParam.type='角色'
                                }
                            });
                        }
                    }
                })*/
                .jstree({
                    'core': {
                        'data': treeData
                    },
                    "plugins" : [
                        "search"],
                    'search' : {
                        'search_leaves_only' : false,
                        'search_callback' : function(string,node){
                            //判断人员姓名
                            if(string !== '' && node.text.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                            //判断姓名全拼音
                            if(string !== '' && pinyin.getFullChars(node.text).toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                            //判断姓名简拼音
                            if(string !== '' && pinyin.getCamelChars(node.text).toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                            //判断登录名
                            if(string !== '' && node.a_attr.userLogin.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                        }
                    },
                });
        }
        //选部门
        $rootScope.initOaDeptTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            hrDeptService.selectDeptByDeptIds($rootScope.selectOaEmployeeConfig.deptIds,$rootScope.selectOaEmployeeConfig.bigDept).then(function (value) {
                var list = value.data.data;
                $rootScope.deptTreeList=list;
                // 输入框内容 联想匹配
                for (var i = 0; i < list.length; i++) {
                    if(list[i].userLogin!=""){
                    }else{
                        list[i].deptNamePY = pinyin.getFullChars(list[i].name);
                        list[i].deptNamePY2 = pinyin.getCamelChars(list[i].name)
                    }
                }
                var deptName = $.map(list, function (team) { return { value: team.name, data: { category: '部门名称',data:team } }; });
                var deptNamePY = $.map(list, function (team) { return { value: team.deptNamePY, data: { category: '名称拼音',data:team } }; });
                var deptNamePY2 = $.map(list, function (team) { return { value: team.deptNamePY2, data: { category: '名称首字母',data:team } }; });

                var teams =deptName.concat(deptNamePY).concat(deptNamePY2);
                // Initialize autocomplete with local lookup:
                $('#autocomplete').devbridgeAutocomplete({
                    lookup: teams,
                    minChars: 1,
                    width:300,
                   // selectFirst:false,
                    showNoSuggestionNotice: true,
                    noSuggestionNotice: '未匹配到相应数据',
                    groupBy: 'category',
                //匹配条件过滤
                lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
                    return  suggestion.value.toLowerCase().indexOf(queryLowerCase) !== -1;
                },
                //返回结果格式
                formatResult:function(suggestion, currentValue){
                        return suggestion.data.data.name +" - "+ suggestion.data.data.id;
                },
                //选择事件
                onSelect: function (suggestion) {
                    if ($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + suggestion.data.data.id + ",") >= 0) {
                        $rootScope.removeOaSelectedDept_(deptId);
                    } else {
                        if (!$rootScope.selectOaEmployeeConfig.multiple) {
                            $rootScope.selectOaEmployeeConfig.deptIdList = "," + suggestion.data.data.id + ",";
                        } else {
                            var depts = $rootScope.selectOaEmployeeConfig.deptIdList.split(",");
                            $rootScope.selectOaEmployeeConfig.deptIdList = "," + depts.join(",") + "," + suggestion.data.data.id + ",";
                        }
                        $rootScope.bindOaSelectedDept_();
                    }
                },
                });
                //加载jstree数据
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    if(item.userLogin==''){
                        var node = {
                            id: item.id,
                            parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                            text: item.name,
                            icon: ($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + item.id + ",") >= 0 ? "icon-check":item.icon),
                            a_attr:{
                                style: ($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + item.id + ",") >= 0 ? "color:green":""),
                                title:item.remark
                            },
                            state: {opened: (item.parentId === 0||item.id===$rootScope.selectOaEmployeeConfig.parentDeptId
                                    ||$rootScope.selectOaEmployeeConfig.parentDeptIds.indexOf(item.id+"") > -1),
                                disabled: false,
                                selected: false}
                        };
                        if(item.parentId===0){
                            $rootScope.defaultOaSelectEmployeeConfig.parentDeptId=item.id;
                        }
                        treeData.push(node);
                    }
                }
                $('#' + deptTreeId).jstree("destroy");
                $('#' + deptTreeId)
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if(data.action==="select_node") {
                            $rootScope.selectOaEmployeeParam.parentDeptId = Number(node.parent);
                            //重新加载树 修改展开节点为点击节点的父节点
                            $rootScope.selectOaEmployeeConfig.parentDeptId = Number(node.parent);
                            //如果层级目录 全部展开
                            $rootScope.selectOaEmployeeConfig.parentDeptIds =node.parents;
                            $rootScope.toggleOaSelectedDept_(node.id);
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData,
                            'multiple':false
                        },
                        "plugins" :
                            ["search"],
                        'search' : {
                            'search_leaves_only' : false,
                            'search_callback' : function(string,node){
                                if(string !== '' && node.text.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                    $rootScope.selectOaTreeNodeIds.push(node.id);
                                    $rootScope.selectOaTreeOpenIds.push(node.parents);
                                }
                            }
                        },
                    });
                var selectedNodes=$('#'+deptTreeId).jstree(true).get_selected(true);
                $rootScope.bindOaSelectedDept_();
                if($rootScope.selectOaEmployeeConfig.deptIdList){
                    hrDeptService.listDataByDeptIdList($rootScope.selectOaEmployeeConfig.deptIdList).then(function (value) {
                        $rootScope.selectedOaDepts_ = value.data.data;
                    })
                }
                $("#OaSelectEmployeeModal").modal("show");
            });

        }
        $rootScope.refreshOaDeptTree=function() {
            var deptTreeId = "oa_employee_dept_tree_";
            var list = $rootScope.deptTreeList;
            //加载jstree数据
            var treeData = [];
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                if(item.userLogin=='') {
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        icon: ($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + item.id + ",") >= 0 ? "icon-check" : item.icon),
                        a_attr: {
                            userLogin: item.userLogin,
                            style:
                                ($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + item.id + ",") >= 0 ? "color:green" : "")
                        },
                        state: {
                            opened: (item.parentId === 0 || item.id === $rootScope.selectOaEmployeeConfig.parentDeptId
                                || $rootScope.selectOaEmployeeConfig.parentDeptIds.indexOf(item.id + "") > -1
                                || $rootScope.selectOaTreeOpenIds.indexOf(item.id + "") > -1),
                            disabled: false,
                            selected: false
                        }
                    };
                    if (item.parentId === 0) {
                        $rootScope.defaultOaSelectEmployeeConfig.parentDeptId = item.id;
                    }
                    //如果被查询 标记为红色
                    if ($rootScope.selectOaTreeNodeIds.indexOf(item.id + "") > -1) {
                        node.a_attr.style = "color:red";
                    }
                    treeData.push(node);
                }
            }
            $('#' + deptTreeId).jstree("destroy");
            $('#' + deptTreeId)
                .on('changed.jstree', function (e, data) {
                    var node = data.instance.get_node(data.selected[0]);
                    if(data.action==="select_node") {
                        $rootScope.selectOaEmployeeParam.parentDeptId = Number(node.parent);
                        //重新加载树 修改展开节点为点击节点的父节点
                        $rootScope.selectOaEmployeeConfig.parentDeptId = Number(node.parent);
                        //如果层级目录 全部展开
                        $rootScope.selectOaEmployeeConfig.parentDeptIds =node.parents;
                        $rootScope.toggleOaSelectedDept_(node.id);
                        //点击不刷新 只改变颜色
/*                        if($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + node.id + ",") >= 0 ){
                            node.a_attr.style ='color:green';
                            $("#"+node.a_attr.id).css("color","green");
                        }else{
                            node.a_attr.style ='';
                            $("#"+node.a_attr.id).css("color","");
                        }*/
                    }
                })
/*                .on('hover_node.jstree', function (e, data) { //鼠标移上事件
                    var node = data.node.original;
                    //判断是否为人员节点
                    if(node.a_attr.userLogin!=''){
                        hrEmployeeService.getModelByUserLogin(node.a_attr.userLogin).then(function (value) {
                            if (value.data.ret) {
                                $rootScope.selectOaEmployee = value.data.data;
                                $rootScope.selectOaEmployeeParam.type='人员'
                            }
                        });
                    }else {

                        hrDeptService.getModelById(node.id).then(function (value) {
                            if (value.data.ret) {
                                $rootScope.selectOaDept = value.data.data;
                                $rootScope.selectOaEmployeeParam.type='部门'
                            }
                        });
                    }
                })*/
                .jstree({
                    'core': {
                        'data': treeData,
                        'multiple':false
                    },
                    "plugins" :
                        ["search"],
                    'search' : {
                        'search_leaves_only' : false,
                        'search_callback' : function(string,node){
                            //判断部门名称
                            if(string !== '' && node.text.toLocaleLowerCase().match(string.toLocaleLowerCase())) {
                                $rootScope.selectOaTreeNodeIds.push(node.id);
                                //所有展开节点
                                for(var i =0;i<node.parents.length;i++){
                                    $rootScope.selectOaTreeOpenIds.push(node.parents[i]);
                                }
                            }
                        }
                    },
                });
        }

        $rootScope.queryOaDeptSelectEmployee_=function(parentDeptId,userLogin){
            //选择人员点击跳转
            if(parentDeptId!=null) {
                $rootScope.selectOaEmployeeParam.parentDeptId = parentDeptId;
                //查询部门的上级部门
                var parentDeptIds =[];
                parentDeptIds.push(parentDeptId+"");
                parentDeptIds.push("1");
                parentDeptIds.push("#");
                hrDeptService.getModelById(parentDeptId).then(function (value) {
                    if (value.data.ret) {
                        parentDeptIds.push(value.data.data.parentId+"");
                        //如果层级目录 全部展开
                        $rootScope.selectOaEmployeeConfig.parentDeptIds =parentDeptIds;
                    }
                });
            }
            $rootScope.selectOaEmployeeParam.userLogin=userLogin;
            $rootScope.loadOaDeptSelectEmployee_();
        }
        $rootScope.queryOaRoleSelectEmployee_=function(parentRoleIds,userLogin){
            //底部点击跳转
            if(parentRoleIds!=null) {
                $rootScope.selectOaEmployeeParam.parentRoleId = parentRoleIds;
            }
            $rootScope.selectOaEmployeeParam.userLogin=userLogin;
            //$rootScope.loadOaRoleSelectEmployee_();
        }

        $rootScope.loadOaDeptSelectEmployee_=function() {
            hrDeptService.getModelById($rootScope.selectOaEmployeeParam.parentDeptId).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selectOaDept = value.data.data;
                    $rootScope.selectOaEmployeeConfig.parentDeptId=$rootScope.selectOaDept.id;
                    $rootScope.refreshOaDeptEmployeeTree();
                }
            });
            hrEmployeeService.getModelByUserLogin($rootScope.selectOaEmployeeParam.userLogin).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selectOaEmployee = value.data.data;
                    $rootScope.selectOaEmployeeParam.type='人员';
                }
            });
        }
        /*        $rootScope.loadOaTreeSelectEmployee_=function() {
                $rootScope.selectOaTreeNodeIds=[];
                $rootScope.selectOaTreeOpenIds=[];
                //$('#oa_employee_dept_tree_').jstree(true).search($rootScope.selectOaEmployeeParam.keyInAll);
                if( $rootScope.selectOaEmployeeConfig.type=='部门'){
                    $rootScope.refreshOaDeptEmployeeTree();
                }else if($rootScope.selectOaEmployeeConfig.type=='角色'){
                    $rootScope.refreshOaRoleEmployeeTree();
                }else if($rootScope.selectOaEmployeeConfig.type=='选部门'){
                    $rootScope.refreshOaDeptTree();
                }else if($rootScope.selectOaEmployeeConfig.type=='人员'){
                    $rootScope.refreshOaUserEmployeeTree();
                }
        }*/
        /*        $rootScope.loadOaRoleSelectEmployee_=function() {
            sysRoleService.getModelById( $rootScope.selectOaEmployeeParam.parentRoleId).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selectOaRole = value.data.data;
                    $rootScope.selectOaEmployeeConfig.parentRoleId=$rootScope.selectOaRole.id;
                    //$rootScope.refreshOaRoleEmployeeTree();
                }
            });
            hrEmployeeService.getModelByUserLogin($rootScope.selectOaEmployeeParam.userLogin).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selectOaEmployee = value.data.data;
                    $rootScope.selectOaEmployeeParam.type='人员';
                }
            });
        }*/

        //选人
        $rootScope.removeOaSelectedUser_=function(userLogin){
            var users=$rootScope.selectOaEmployeeConfig.userLoginList.split(",");
            $rootScope.selectOaEmployeeConfig.userLoginList = ("," +users.join(",") + ",").replace(","+userLogin+",",",");
            $rootScope.bindOaSelectedUser_();
        }
        $rootScope.bindOaSelectedUser_=function() {
            $rootScope.selectOaEmployeeConfig.userLoginList = "," + $rootScope.selectOaEmployeeConfig.userLoginList + ",";
            hrEmployeeService.listDataByUserLoginList($rootScope.selectOaEmployeeConfig.userLoginList).then(function (value) {
                $rootScope.selectedOaUsers_ = value.data.data;
                var userNameList=[];
                var userLoginList=[];
                for(var i=0;i<$rootScope.selectedOaUsers_.length;i++){
                    var user=$rootScope.selectedOaUsers_[i];
                    userLoginList.push(user.userLogin);
                    userNameList.push(user.userName);
                }
                $rootScope.selectedOaUserNames_=userNameList.join(",");
                $rootScope.selectedOaUserLogins_=userLoginList.join(",");
            })
        }
        $rootScope.toggleOaSelectedUser_=function(userLogin) {
            //判断是否存在
            if ($rootScope.selectOaEmployeeConfig.userLoginList.indexOf("," + userLogin + ",") >= 0) {
                $rootScope.removeOaSelectedUser_(userLogin);
            } else {
                //判断是否多选
                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + userLogin + ",";
                } else {
                    var users = $rootScope.selectOaEmployeeConfig.userLoginList.split(",");
                    $rootScope.selectOaEmployeeConfig.userLoginList = "," + users.join(",") + "," + userLogin + ",";
                }
                $rootScope.bindOaSelectedUser_();
            }
        }

        //选部门
        $rootScope.removeOaSelectedDept_=function(deptId){
            var depts=$rootScope.selectOaEmployeeConfig.deptIdList.split(",");
            $rootScope.selectOaEmployeeConfig.deptIdList = ("," +depts.join(",") + ",").replace(","+deptId+",",",");
            $rootScope.bindOaSelectedDept_();
            //$rootScope.refreshOaDeptTree();
        }
        $rootScope.bindOaSelectedDept_=function() {
            $rootScope.selectOaEmployeeConfig.deptIdList = "," + $rootScope.selectOaEmployeeConfig.deptIdList + ",";
            hrDeptService.listDataByDeptIdList($rootScope.selectOaEmployeeConfig.deptIdList).then(function (value) {
                $rootScope.selectedOaDepts_ = value.data.data;
                var deptNameList=[];
                var deptIdList=[];
                for(var i=0;i<$rootScope.selectedOaDepts_.length;i++){
                    var dept=$rootScope.selectedOaDepts_[i];
                    deptNameList.push(dept.name);
                    deptIdList.push(dept.id);
                }
                $rootScope.selectedOaDeptNames_=deptNameList.join(",");
                $rootScope.selectedOaDeptIds_=deptIdList.join(",");
            })
        }
        $rootScope.toggleOaSelectedDept_=function(deptId) {
            if ($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + deptId + ",") >= 0) {
                $rootScope.removeOaSelectedDept_(deptId);
            } else {
                if (!$rootScope.selectOaEmployeeConfig.multiple) {
                    $rootScope.selectOaEmployeeConfig.deptIdList = "," + deptId + ",";
                } else {
                    var depts = $rootScope.selectOaEmployeeConfig.deptIdList.split(",");
                    $rootScope.selectOaEmployeeConfig.deptIdList = "," + depts.join(",") + "," + deptId + ",";
                }
                $rootScope.bindOaSelectedDept_();
            }
           // $rootScope.selectOaEmployeeConfig.parentDeptId=$rootScope.selectOaDept.id;
            // $rootScope.refreshOaDeptTree();
        }

        //用户选择相关
        $rootScope.initEmployeeModal=function() {
            var deptTreeId = "employee_dept_tree_";
            $rootScope.defaultSelectEmployeeConfig = {
                title: "人员信息",
                multiple: true,
                userLoginList: "",
                name: "",
                qualify: "",
                position:"",
                specialty: "",
                parentDeptId: 1,
                containChild: true,
                appCode:user.appCode
            };

            hrDeptService.selectAll().then(function (value) {
                var list = value.data.data;
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        state: {opened: item.parentId === 0, disabled: false, selected: false}
                    };
                    if(item.parentId===0){
                        $rootScope.defaultSelectEmployeeConfig.parentDeptId=item.id;
                    }
                    treeData.push(node);
                }
                $('#employee_dept_tree_' ).jstree("destroy");
                $('#employee_dept_tree_' )
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if(data.action==="select_node") {
                            if ($rootScope.selectEmployeeParam.parentDeptId === parseInt(node.id)) {
                                $rootScope.loadSelectEmployee_();
                            } else {
                                $rootScope.selectEmployeeParam.parentDeptId = node.id;
                                $rootScope.querySelectEmployee_();
                            }
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData,
                            'multiple':false
                        }
                    });

            });
        }

        /**
         * title:窗口名字,appCode:"",
         * 查询条件：name:用户名（姓名）,qualify:"设计资格,设计、校核，审核",specialty:"专业",position:"职务",parentDeptId:查询的部门及它下级
         * userLoginList：勾选上的人,multiple:单选还是多选
         * @param config
         * @private
         */
        $rootScope.showSelectEmployeeModal_=function(config) {
            $rootScope.selectEmployeeConfig=$.extend($.extend({},$rootScope.defaultSelectEmployeeConfig),config);
            $rootScope.selectEmployeePageInfo={pageNum:1,pageSize:10,total:100};
            $rootScope.selectEmployeeParam={name:"",qualify:" ",specialty:"",position:"",
                qName:$rootScope.selectEmployeeConfig.name,
                qQualify:$rootScope.selectEmployeeConfig.qualify,
                qSpecialty:$rootScope.selectEmployeeConfig.specialty,
                qPosition:$rootScope.selectEmployeeConfig.position,
                parentDeptId:$rootScope.selectEmployeeConfig.parentDeptId,deptId:0,
                appCode:$rootScope.selectEmployeeConfig.appCode,
                pageNum:1,pageSize:10};
            var selectedNodes=$('#employee_dept_tree_').jstree(true).get_selected(true);

            if(selectedNodes.length===1&&parseInt(selectedNodes[0].id)===$rootScope.selectEmployeeConfig.parentDeptId){
                $rootScope.querySelectEmployee_();
            }else{
                $('#employee_dept_tree_').jstree("deselect_all", true);
                $('#employee_dept_tree_').jstree('select_node',$rootScope.selectEmployeeConfig.parentDeptId);
            }

            $rootScope.bindSelectedUser_();

            if($rootScope.selectEmployeeConfig.userLoginList){
                hrEmployeeService.listDataByUserLoginList($rootScope.selectEmployeeConfig.userLoginList).then(function (value) {
                    $rootScope.selectedUsers_=value.data.data;

                })
            }

            $("#commonSelectEmployeeModal").modal("show");

        };

        $rootScope.closeSelectEmployeeModal_=function(){
            $("#commonSelectEmployeeModal").modal("hide");
        }

        $rootScope.querySelectEmployee_=function(){
            $rootScope.selectEmployeePageInfo.pageNum=1;
            $rootScope.selectEmployeeParam.qName=$rootScope.selectEmployeeParam.name;
            $rootScope.selectEmployeeParam.qSpecialty=$rootScope.selectEmployeeParam.specialty;
            $rootScope.selectEmployeeParam.qQualify=$rootScope.selectEmployeeParam.qualify;
            $rootScope.selectEmployeeParam.qPosition=$rootScope.selectEmployeeParam.position;
            $rootScope.loadSelectEmployee_();
        }

        $rootScope.loadSelectEmployee_=function() {
            $rootScope.selectEmployeeParam.pageNum= $rootScope.selectEmployeePageInfo.pageNum;
            $rootScope.selectEmployeeParam.pageSize= $rootScope.selectEmployeePageInfo.pageSize;
            if(!$rootScope.selectEmployeeConfig.containChild){
                $rootScope.selectEmployeeParam.deptId=$rootScope.selectEmployeeParam.parentDeptId;
            }
            hrEmployeeService.listSimplePagedData($rootScope.selectEmployeeParam).then(function (value) {
                if (value.data.ret) {
                    $rootScope.selectEmployeePageInfo = value.data.data;
                }
            });
        }

        $rootScope.removeSelectedUser_=function(userLogin){
            var users=$rootScope.selectEmployeeConfig.userLoginList.split(",");
            $rootScope.selectEmployeeConfig.userLoginList = ("," +users.join(",") + ",").replace(","+userLogin+",",",");
            $rootScope.bindSelectedUser_();
        }

        $rootScope.bindSelectedUser_=function() {
            $rootScope.selectEmployeeConfig.userLoginList = "," + $rootScope.selectEmployeeConfig.userLoginList + ",";
            hrEmployeeService.listDataByUserLoginList($rootScope.selectEmployeeConfig.userLoginList).then(function (value) {
                $rootScope.selectedUsers_ = value.data.data;
                var userNameList=[];
                var userLoginList=[];
                for(var i=0;i<$rootScope.selectedUsers_.length;i++){
                    var user=$rootScope.selectedUsers_[i];
                    userLoginList.push(user.userLogin);
                    userNameList.push(user.userName);
                }
                $rootScope.selectedUserNames_=userNameList.join(",");
                $rootScope.selectedUserLogins_=userLoginList.join(",");
            })
        }

        $rootScope.toggleSelectedUser_=function(userLogin) {
            if ($rootScope.selectEmployeeConfig.userLoginList.indexOf("," + userLogin + ",") >= 0) {
                $rootScope.removeSelectedUser_(userLogin);
            } else {
                if (!$rootScope.selectEmployeeConfig.multiple) {
                    $rootScope.selectEmployeeConfig.userLoginList = "," + userLogin + ",";
                } else {
                    var users = $rootScope.selectEmployeeConfig.userLoginList.split(",");
                    $rootScope.selectEmployeeConfig.userLoginList = "," + users.join(",") + "," + userLogin + ",";
                }
                $rootScope.bindSelectedUser_();
            }
        }

        //权限名称 与 页面标题名称 导航名称 统一
        $rootScope.loadTableName=function(uiSref){
            sysAclService.listAclByModules(1,uiSref).then(function (value) {
                let acls = value.data.data;
                var i = 0;
                if(acls.length>1){
                    for(element of acls){
                        if(element.code == uiSref){
                            $rootScope.tableName= element.name;
                        }
                    }
                }else {
                    $rootScope.tableName= acls[i].name;
                }
            });

        }

        $rootScope.initEmployeeModal();
        $rootScope.initOaEmployeeModal();
        $rootScope.judgePassword();

    })


    .controller("DashboardController", function ($state){
        $state.go("five.dashboard");
    })

    .controller("FiveDashboardController", function ($state, $scope,$rootScope,sysUserService,actService,myActService,actProcessQueryService,edFileService,oaNoticeService,hrDeptService,hrEmployeeService,actTaskQueryService, actTaskHandleService,actProcessQueryService,actProcessHandleService, commonCodeService) {
        var vm = this;

        vm.taskPageInfo = {pageNum: 1, pageSize: $scope.pageSize,total: $scope.pageSize};
        vm.taskParams={processDefinitionName:"",description:""};

        vm.ccTaskPageInfo = {pageNum: 1, pageSize: $scope.pageSize,total: 0};
        vm.ccTaskParams={initiator:"",description:"",qInitiator:"",qDescription:""};

        vm.taskDonePageInfo = {pageNum: 1, pageSize: $scope.pageSize,total: $scope.pageSize};
        vm.doneParams={processDefinitionName:"",description:"",initiator:""};

        vm.myPageInfo = {pageNum: 1, pageSize: $scope.pageSize,total: $scope.pageSize};
        vm.myParams={processDefinitionName:"",description:"",finish:""};

        vm.taskCondition={modelCategory:"",selectText:"全部",tableName:"待办任务"};
        vm.init=function() {

            $("#btnUploadHead").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'json',
                add: function (e, data) {
                    var file = data.files[0];

                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source: "web", jwt: user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        vm.sysUser.headUrl = '/sys/attach/download/' + result.data.id;
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
                    if (file.name.indexOf(".dwg") <= 0) {
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source: "web", jwt: user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        vm.sysUser.signUrl = '/sys/attach/download/' + result.data.id;
                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });

            vm.reloadTask();
            vm.reloadCcTask();
            vm.reloadDoneTask();
            vm.reloadMyProcess();

            vm.loadTaskType();
            vm.loadTree();
            $scope.basicInit();
        }
        //树形结构类型
        vm.loadTree=function(refresh) {
            var cacheParams = getNgParam($state, {modelCategory: "", processDefinitionKey: ""});
            if(refresh) {
                cacheParams.modelCategory = "";
                cacheParams.processDefinitionKey = "";
            }
            actProcessQueryService.listProcessCategoryTree(user.enLogin).then(function (value) {
                vm.processCategoryList = value.data.data;
                if (cacheParams.processDefinitionKey) {
                    var parentId = "";
                    for (var i = 0; i < vm.processCategoryList.length; i++) {
                        var nodeData = vm.processCategoryList[i];
                        if (nodeData.data == cacheParams.processDefinitionKey) {
                            nodeData.state.selected = true;
                            parentId = nodeData.parent;
                            break;
                        }
                    }
                    for (var i = 0; i < vm.processCategoryList.length; i++) {
                        var nodeData = vm.processCategoryList[i];
                        if (nodeData.id == parentId) {
                            nodeData.state.opened = true;
                            break;
                        }
                    }
                } else if (cacheParams.modelCategory) {
                    for (var i = 0; i < vm.processCategoryList.length; i++) {
                        var nodeData = vm.processCategoryList[i];
                        if (nodeData.id ==cacheParams.modelCategory) {
                            nodeData.state.opened = true;
                            nodeData.state.selected=true;
                            break;
                        }
                    }
                }

                vm.modelCategory = "";
                vm.processDefinitionKey = "";
                $('#js_tree').jstree("destroy");
                $('#js_tree')
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if (node.data) {
                            vm.modelCategory = "";
                            vm.processDefinitionKey = node.data;
                        } else {
                            vm.modelCategory = node.id;
                            vm.processDefinitionKey = "";
                        }
                        setNgCache($state, {
                            modelCategory: vm.modelCategory,
                            processDefinitionKey: vm.processDefinitionKey
                        });
                        vm.reloadAllTask();
                    })
                    .jstree({
                        'core': {
                            'data': vm.processCategoryList
                        }
                    });

                vm.reloadAllTask();
            });
        }

        //查询调度方法
        vm.reloadAllTask=function() {
            if (vm.taskCondition.tableName == "待办任务") {
                vm.reloadTask();
            } else if (vm.taskCondition.tableName == "抄送我的") {
                vm.reloadCcTask();
            } else if (vm.taskCondition.tableName == "我审批的") {
                vm.reloadDoneTask();
            } else if (vm.taskCondition.tableName == "我发起的") {
                vm.reloadMyProcess();
            }
        }

        vm.loadTaskType=function(){
            commonCodeService.listDataByCatalog(user.enLogin,"流程类别").then(function (value) {
                vm.taskTypeList=value.data.data;
            })
        }
        //状态切换
        vm.showTable=function(tableName){
            vm.taskCondition.tableName=tableName;
        }

        vm.showNoticeDetail = function (id,attachId) {
            if (attachId){
                $rootScope.readNoticeWord(attachId);
            }else {
                $state.go("oa.articleDetail",{id: id});
            }
        }

        //待办任务
        vm.loadTask=function () {
            var pa= {
                pageNum: vm.taskPageInfo.pageNum,
                pageSize: vm.taskPageInfo.pageSize,
                containCc: false,
                enLogin: user.enLogin,
                qInitiator: vm.qInitiator,
                processDescription: vm.qDescription,
                processDefinitionName: vm.qProcessDefinitionName,
                modelCategory: vm.modelCategory,
                processDefinitionKey: vm.processDefinitionKey
            }
            actTaskQueryService.listPagedTask(pa).then(function (value) {
                if (value.data.ret) {
                    vm.taskList = value.data.data.list;
                    vm.taskList.forEach(function (item) {
                        item.outTime = vm.jugeOutTime(item.createTime);
                    })
                    vm.taskPageInfo = value.data.data;
                }
            })
        }
        //判断是否超时
        vm.jugeOutTime=function(timeStamp){
            if (timeStamp==undefined || timeStamp==''){
                return 0;
            }
            var yellow=60*60*24*3;//2天后变为黄色
            var red=60*60*24*15;//15天超期
            var now=new Date().valueOf();
            var temporyValue=(parseInt(now)-parseInt(timeStamp))/1000;
            if(temporyValue<yellow){
                return 1;
            }
            else if(temporyValue<red){
                return 2;
            }
            else
                return 3;
        }

        vm.reloadTask=function() {
            vm.taskPageInfo.pageNum = 1;
            vm.qProcessDefinitionName = vm.taskParams.processDefinitionName;
            vm.qDescription = vm.taskParams.description;
            vm.qInitiator=vm.taskParams.initiator;
            vm.loadTask();
            actTaskQueryService.getTaskCount({enLogin:user.enLogin,containCc:false}).then(function (value){
                vm.taskTotal=value.data.data;
            })
        }


        vm.reloadCcTask=function() {
            vm.ccTaskPageInfo.pageNum = 1;
            vm.ccTaskParams.qDescription = vm.ccTaskParams.description;
            vm.loadCcTask();
            actTaskQueryService.getCcTaskCount({enLogin:user.enLogin}).then(function (value){
                vm.ccTaskTotal=value.data.data;
            })
        }

        vm.loadCcTask=function () {
            var pa= {
                pageNum: vm.ccTaskPageInfo.pageNum,
                pageSize: vm.ccTaskPageInfo.pageSize,
                enLogin: user.enLogin,
                qInitiator: vm.ccTaskParams.qInitiator,
                processDescription: vm.ccTaskParams.qDescription,
                modelCategory: vm.modelCategory,
                processDefinitionKey: vm.processDefinitionKey,
            }
            actTaskQueryService.listPagedCcTask(pa).then(function (value) {
                if (value.data.ret) {
                    vm.ccTaskList = value.data.data.list;
                    vm.ccTaskList.forEach(function (item) {
                        item.outTime = vm.jugeOutTime(item.startTime);
                    })
                    vm.ccTaskPageInfo = value.data.data;
                }
            });
        }


        vm.showTaskProcess=function(task) {
            vm.task = task;
            $rootScope.loadNewProcessInstance(task.processInstanceId);
            $("#taskProcessModal").modal("show");
        };

       /*我参与的*/
        vm.loadDoneTask=function () {
            var pa= {
                pageNum: vm.taskDonePageInfo.pageNum,
                pageSize: vm.taskDonePageInfo.pageSize,
                enLogin:user.userLogin,
                involvedUser: user.userLogin,
                processDescription: vm.dDescription,
                qInitiator: vm.dInitiator,
                myProcessDefinitionName: vm.dProcessDefinitionName,
                modelCategory: vm.modelCategory,
                processDefinitionKey: vm.processDefinitionKey
            }
            actProcessQueryService.listPagedProcessInstance(pa).then(function (value) {
                if(value.data.ret){
                    var data=value.data.data;
                    vm.taskDonePageInfo=data;
                    vm.taskDoneList=data.list;
                }
            })

        }
        
        vm.reloadDoneTask=function(){
            vm.taskDonePageInfo.pageNum=1;
            vm.dProcessDefinitionName= vm.doneParams.processDefinitionName;
            vm.dDescription= vm.doneParams.description;
            vm.dInitiator=vm.doneParams.initiator;
            vm.loadDoneTask();
        }

        vm.reloadMyProcess=function(){
            vm.myPageInfo.pageNum=1;
            vm.myProcessDefinitionName= vm.myParams.processDefinitionName;
            vm.myDescription= vm.myParams.description;
            vm.myFinish=vm.myParams.finish;
            vm.loadMyProcess();
        }

        //我发起的
        vm.loadMyProcess=function () {
            var params={
                initiator:user.enLogin,
                enLogin:user.enLogin,
                pageNum:vm.myPageInfo.pageNum,pageSize:vm.myPageInfo.pageSize,
                finished:vm.myFinish,
                myProcessDefinitionName:vm.myProcessDefinitionName,
                processDescription:vm.myDescription,
                modelCategory:vm.modelCategory,
                processDefinitionKey:vm.processDefinitionKey
            }
            actProcessQueryService.listPagedProcessInstance(params).then(function (value) {
                if(value.data.ret){
                    var data=value.data.data;
                    vm.myPageInfo=data;
                    vm.myProcessList=data.list;
                }
            })
        }

        vm.showDetail=function(businessKey){
            actService.getNgRedirectUrl(businessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                    $state.go(result.url,result.params);
                }
            })
        }

        vm.removeProcess=function(processInstanceId,enLogin){
            bootbox.confirm("您确认要永久删除流程及对应数据吗?", function (result) {
                if (result) {
                    actProcessHandleService.deleteProcessInstanceById(processInstanceId,enLogin,"no why").then(function(value){
                        if(value.data.ret) {
                            toastr.success("删除成功!");
                            vm.init();
                        }
                    });
                }
            });
        }

        vm.showDetailByModal=function(){
            $("#taskProcessModal").modal("hide");
            setTimeout(function () {
                vm.showDetail(vm.task.businessKey);
            },500);
        }

        vm.showUserInfo=function () {
            sysUserService.getModelById(user.id).then(function (value) {
                if(value.data.ret){
                    vm.sysUser=value.data.data;
                    $("#userModal").modal("show");
                }

            })


        }

        vm.saveUser = function () {
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    if ($("#user_from").validate().form()) {
                        sysUserService.update(vm.sysUser).then(function (value) {
                            if (value.data.ret) {
                                $("#userModal").modal("hide");
                                toastr.success("更新成功!");
                                $rootScope.user.headUrl=vm.sysUser.headUrl;
                                $rootScope.user.signUrl=vm.sysUser.signUrl;
                                $rootScope.$apply();
                            }
                        });
                    } else {
                        toastr.warning("请准确填写数据!")
                        return;
                    }
                }
            });
        }

        vm.init();

        return vm;
    })

    .controller("NoticeShowController", function ($state, $scope,$rootScope,oaNoticeService,edFileService) {
        var vm = this;
        var editor;
        vm.init =function(){
            vm.loadData();
            vm.loadNotice();

        }
        vm.loadData = function () {
            oaNoticeService.selectAll().then(function (value) {
                vm.notices = value.data.data;
            })
        }
        vm.loadNotice=function(){
            var params={noticeType:"通知公告","publish":1,pageNum:1,pageSize:10,userLogin:user.userLogin};
            oaNoticeService.listPagedData(params).then(function (value) {
                vm.noticeTops=value.data.data.list;
                setTimeout(function () {
                    $('#noticeList').liMarquee();
                },100);
            })
        }

        vm.showDetail=function(id){
            oaNoticeService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.notice = value.data.data;
                    if(editor) {
                        editor.html(vm.notice.noticeContent);
                    }
                    edFileService.listData(vm.notice.businessKey).then(function (value) {
                        if (value.data.ret) {
                            vm.files = value.data.data;

                        }
                    });

                    $("#noticeShowModel").modal("show");
                }
            })
        }

        vm.batchDownloadEdFile=function () {
            $(".cb_edFile:checked").each(function () {
                var index = $(this).attr("data-index");
                if (index < vm.files.length) {
                    $rootScope.downloadEdFileWithXml(vm.files[index]);
                }
            });
        }
        vm.init();


        return vm;
    })

    .controller("RedHeaderController",function ($state, $scope,$rootScope,fiveContentFileService,oaNoticeService) {
        var vm = this;
        var businessKey;
        $scope.fileName=22222;
        /**
         * 加载文档控件
         */
        $rootScope.loadContentFiles=function (businessId,loadFlag) {
            businessKey=businessId;
            fiveContentFileService.getModelByBusinessKey(businessId).then(function (value) {
                if (value.data.ret) {
                    $rootScope.contentFile = value.data.data.content;
                    $rootScope.redheadFile = value.data.data.redhead;
                }
            })
            if (loadFlag){
                vm.uploadContent(businessKey);
                vm.uploadRedHead(businessKey);
            }
        }

        //上传正文
        vm.uploadContent=function(businessId){
            $('#btnUpload1').fileupload({
                url: '/wuzhou/file/receive.do',
                //是否自动上传
                //autoUpload: true,
                maxNumberOfFiles: 1,//最大上传文件数目
                dataType: 'text',
                //
                add: function (e, data) {
                    data.formData={businessKey:businessId,userLogin:user.userLogin,fileType:1}
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        $rootScope.loadContentFiles(businessId);
                        toastr.success("上传成功!");
                    } else {
                        toastr.error("上传失败!");
                    }

                }
            });

        }
        //上传红头文件
        vm.uploadRedHead=function(businessId){
            $('#btnUpload0').fileupload({
                url: '/wuzhou/file/receive.do',
                //是否自动上传
                //autoUpload: true,
                maxNumberOfFiles: 1,//最大上传文件数目
                dataType: 'text',
                //
                add: function (e, data) {
                    data.formData={businessKey:businessId,userLogin:user.userLogin,fileType:0}
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        $rootScope.loadContentFiles(businessId);
                        toastr.success("上传成功!");
                    } else {
                        toastr.error(result.msg);
                    }

                }
            });
        }
        //编辑正文
        vm.editContent=function (businessId) {
            var  path="/wuzhou/file/downloadContent/"+businessId;
            var ntkoed=ntkoBrowser.ExtensionInstalled();
            if(ntkoed){

                ntkoBrowser.openWindow("/nochange/rh-office-control/context.html?path=" + path);
            }
            else{
                var iTop = ntkoBrowser.NtkoiTop();   //获得窗口的垂直位置;
                var iLeft = ntkoBrowser.NtkoiLeft();        //获得窗口的水平位置;
                window.open("/nochange/rh-office-control/exeindex.html","","height=200px,width=500px,top="+iTop+"px,left="+iLeft+"px,titlebar=no,toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no");
            }
        }
        //编辑套红
        vm.editRedHead=function (item) {

            var businessId=item.businessKey;
            var  path="/wuzhou/file/downloadRedHead/"+businessId;
            var redhead='';
            var data={
                wordSize:item.dispatch,
                send:item.realSendManName,
                copy:item.copySendManName,
                deptName:item.deptName,
                sign:item.checkManName
            };

            if(businessId.indexOf("fiveOaDispatch")>-1){
                redhead=getRedHead(0,item.dispatchType);
                //wordSize 文号 send  主送 copy 抄送 deptName 主办单位 content 正文
            }else {
                redhead=getRedHead(1,item.deptId);
            }
            var ntkoed=ntkoBrowser.ExtensionInstalled();
            if(ntkoed){
                ntkoBrowser.openWindow("/nochange/rh-office-control/redhead.html?path=" + path+"&redhead="+redhead+"&data="+escape(JSON.stringify(data)));
            }
            else{
                var iTop = ntkoBrowser.NtkoiTop();   //获得窗口的垂直位置;
                var iLeft = ntkoBrowser.NtkoiLeft();        //获得窗口的水平位置;
                window.open("/nochange/rh-office-control/exeindex.html","","height=200px,width=500px,top="+iTop+"px,left="+iLeft+"px,titlebar=no,toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no");
            }
        }

        /**
         * @param key 判断红头关键字
         * @param type 部门发文 1 公司发问 0
         */
        function getRedHead(type,key){
            var redhead="redHead.doc";
            if (type==0){
               if (key=="中国五洲工程设计集团文件"){
                    redhead='fiveOaDispatchFile.doc';
                }else if (key=="中国五洲工程设计集团保密委员会会议纪要"){
                    redhead='fiveOaDispatchSecurityMinutes.doc';
                }else if (key=="中国五洲工程设计集团工会简报"){
                    redhead='fiveOaDispatchUnionBriefing.doc';
                }else if (key=="中国五洲工程设计集团情况通报"){
                    redhead='fiveOaDispatchInformed.doc';
                }else if (key=="中共中国五洲工程设计集团纪律检查委员会文件"){
                    redhead='fiveOaDispatchDisciplineFile.doc';
                } else if (key=="中共中国五洲工程设计集团委员会文件"){
                    redhead='fiveOaDispatchCPC.doc';
                }else if (key=="中国五洲工程设计集团会议纪要"){
                    redhead='fiveOaDispatchMinutes.doc';
                }else if (key=="中国五洲工程设计有限公司监事会文件"){
                    redhead='fiveOaDispatchSupervisorFile.doc';
                }else if (key=="中国五洲工程设计集团简报"){
                    redhead='fiveOaDispatchBriefing.doc';
                }else if (key=="共青团中国五洲工程设计集团委员会文件"){
                    redhead='fiveOaDispatchCYLFile.doc';
                }
                /*else if (key=="五洲工程设计研究院发文单"){
                    redhead='fiveOaDispatchAcademy.doc';
                }else if (key=="中共五洲工程设计研究院委员会发文单"){
                    redhead='fiveOaDispatchCPCAcademy.doc';
                }else if (key=="中国五洲工程设计集团工会委员会文件"){
                    redhead='fiveOaDispatchUnionFile.doc';
                }else if (key=="中国五洲工程设计集团董事会文件"){
                    redhead='fiveOaDispatchBoard.doc';
                } if (key=="中国五洲工程设计集团有限公司发文单"){
                    redhead='fiveOaDispatchCompany.doc';
                }else */
            }else {
                redhead=key+".doc";
            }

            return redhead;
        }
        //只读 readOnly
        vm.readDocOnly=function(id,fileName){
            //套红插件只读
          /*  var  path="/wuzhou/file/download/"+id;
            if(fileName.toLocaleString().indexOf(".pdf")>0){
                window.open(path);
            } else {
                var ntkoed=ntkoBrowser.ExtensionInstalled();
                if(ntkoed){
                    ntkoBrowser.openWindow("/nochange/rh-office-control/readonly.html?path=" + path);
                } else{
                    var iTop = ntkoBrowser.NtkoiTop();   //获得窗口的垂直位置;
                    var iLeft = ntkoBrowser.NtkoiLeft();  //获得窗口的水平位置;
                    window.open("/nochange/rh-office-control/exeindex.html","","height=200px,width=500px,top="+iTop+"px,left="+iLeft+"px,titlebar=no,toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no");
                }
            }*/

           /* var hrefOWA="https://owa.wuzhou.com.cn/op/embed.aspx?src="+
                encodeURIComponent("https://co.wuzhou.com.cn/wuzhou/file/download/"+id);
            window.open(hrefOWA);*/

            //$state.go("oa.mainWordOWA",{id:id});

           // window.open("/act/plotIndex#?id="+ id+"&&url=oa.mainWordOWA"+result.url+"&&name="+name);

           // $sce.trustAsResourceUrl(vm.hrefOWA);


            url = "/wuzhou/file/preview/" + id;
            window.open(url, "_blank");

        }
        /**
         * 首页 只读页面选择
         * @param id
         */
        $rootScope.readNoticeWord=function(id,businessKey){
            var  path="/common/attach/download/"+id;//新版 附件访问地址
            var noticeId= businessKey.split("_")[1];
            var ntkoed=ntkoBrowser.ExtensionInstalled();
            if(ntkoed){
                ntkoBrowser.openWindow("/nochange/rh-office-control/readonlyIndex.html?path=" + path+"&businessKey="+businessKey);
            }
            else{
                var iTop = ntkoBrowser.NtkoiTop();   //获得窗口的垂直位置;
                var iLeft = ntkoBrowser.NtkoiLeft(); //获得窗口的水平位置;
                window.open("/nochange/rh-office-control/exeindex.html","","height=200px,width=500px,top="+iTop+"px,left="+iLeft+"px,titlebar=no,toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no");
            }
        }


        vm.remove=function (businessKey,id) {
            fiveContentFileService.remove(id).then(function (value) {
                if (value.data.ret){
                    $rootScope.loadContentFiles(businessKey);
                    toastr.success("删除成功!");
                }
            })
        }

        //在父页面定义的跨浏览器插件应用程序关闭事件响应方法，且方法名不能自定义，必须是ntkoCloseEvent
        $scope.ntkoCloseEvent=function(){
            toastr.success("刷新成功!");
            $rootScope.loadContentFiles(businessKey,false);
        }

    })

    .controller("ActRelevanceController", function ($state, $scope,$rootScope,sysUserService,actService,myActService,commonFormDataService,fiveActRelevanceService) {
        var vm = this;
        vm.params={referType:'',uiSref:'',qName:'',flag:false,businessId:''}
        vm.pageInfo = {pageNum: 1, pageSize: 7,total:999};


        /**
         * @param referType 需要关联流程类型
         * @param uiSref 有值按照查看权限查看
         * @param flag 是否需要查询部门或者个人 默认只查询自己发起的流程
         */
        $rootScope.loadActRelevance=function(businessId,referType,uiSref,flag){
            vm.params.referType=referType;
            vm.params.uiSref=uiSref;
            vm.params.flag=flag;
            vm.params.businessId=businessId;
            vm.loadActRelevance2();
        }

        vm.loadActRelevance2=function(){
            fiveActRelevanceService.listActRelevance(vm.params.businessId).then(function (value) {
                if (value.data.ret){
                    vm.actRelevanceList=value.data.data;
                    var list=value.data.data;
                    var ids=[];
                    for (var i=0;i<list.length;i++){
                        ids.push(list[i].businessKey);
                    }
                    vm.stringKey="";
                    vm.stringKey=ids.join(",");
                }
            })
        }

        vm.loadDoneTask=function () {

           var params={pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,referType:vm.params.referType,q:vm.params.qName,uiSref: vm.params.uiSref,enLogin:user.enLogin};
            if (vm.params.flag){
                params.creator=user.userLogin;
            }
            commonFormDataService.listActPagedData(params).then(function (value) {
                 if (value.data.ret){
                     vm.pageInfo=value.data.data.pageInfo;
                     vm.heads=value.data.data.heads;
                     vm.template=value.data.data.template;
                 }
            })
        }

        vm.showDoneTask=function(){
            vm.loadDoneTask();

            $("#selectDoneTaskModal").modal("show");
        }

        vm.reloadDoneTask=function(){
            vm.pageInfo = {pageNum: 1, pageSize: 7,total:999};
            vm.loadDoneTask();
        }

        vm.saveSelectTask=function(item){
            var map=[];
            var parms={processInstanceId:item.processInstanceId,businessKey:item.businessKey,processDefinitionName:item.formDesc,
                name:item.creatorName,businessId:vm.params.businessId,userLogin:user.userLogin,
                processDescription:item.propertyList[0].head+':'+item.propertyList[0].text}
            map.push(parms);
            fiveActRelevanceService.getNewModel(map).then(function (value) {
                if (value.data.ret){
                    vm.loadActRelevance2();
                    toastr.success("新增完成!");
                    $("#selectDoneTaskModal").modal("hide");
                }
            })

        }

        vm.removeActRelevance=function(id){
            fiveActRelevanceService.remove(id,user.userLogin).then(function (value) {
                if (value.data.ret){
                    toastr.success("删除成功!");
                    vm.loadActRelevance2();
                }
            })

        }

        vm.showDetail=function(businessKey){
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

        return vm;
    })

