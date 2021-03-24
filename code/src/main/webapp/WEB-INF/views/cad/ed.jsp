<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>任务详情</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="/m/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME STYLES -->
    <link href="/m/global/css/components.css" id="style_components" rel="stylesheet" type="text/css"/>
    <link href="/m/global/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
    <link id="style_color" href="/m/admin/layout/css/themes/darkblue.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="/m/jstree/themes/default/style.min.css" rel="stylesheet"/>
    <link href="/m/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <link rel="shortcut icon" href="/m/mcc.ico"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
<!-- DOC: Apply "page-sidebar-closed" class to the body and "page-sidebar-menu-closed" class to the sidebar menu element to hide the sidebar by default -->
<!-- DOC: Apply "page-sidebar-hide" class to the body to make the sidebar completely hidden on toggle -->
<!-- DOC: Apply "page-sidebar-closed-hide-logo" class to the body element to make the logo hidden on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-hide" class to body element to completely hide the sidebar on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-fixed" class to have fixed sidebar -->
<!-- DOC: Apply "page-footer-fixed" class to the body element to have fixed footer -->
<!-- DOC: Apply "page-sidebar-reversed" class to put the sidebar on the right side -->
<!-- DOC: Apply "page-full-width" class to the body element to have full width page without the sidebar menu -->
<body class="page-full-width" ng-app="taskApp" ng-controller="FootController">

<div class="clearfix" ng-controller="CommonBaseController">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN CONTENT -->
    <div class="page-content-wrapper">
        <div class="page-content"  style="background-color: #F1F3FA;">
            <div ui-view></div>
        </div>
    </div>
    <!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="/m/global/plugins/respond.min.js"></script>
<script src="/m/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script src="/m/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="/m/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<script src="/m/global/scripts/metronic.js" type="text/javascript"></script>
<script src="/m/admin/layout/scripts/layout.js" type="text/javascript"></script>
<script charset="UTF-8" src="/assets/js/moment.js"></script>
<script src="/m/global/plugins/bootstrap-toastr/toastr.min.js"></script>
<script src="/m/jstree/jstree.min.js"></script>
<script src="/assets/js/bootbox.js"></script>

<script charset="UTF-8" src="/assets/js/angular.js"></script>
<script charset="UTF-8" src="/assets/js/angular-ui-router.min.js"></script>
<script charset="UTF-8" src="/assets/js/stateEvents.js"></script>
<script charset="UTF-8" src="/assets/js/my-directives.js"></script>
<script charset="UTF-8" src="/assets/js/cmcu-common.js"></script>
<script charset="UTF-8" src="/assets/js/moment.js"></script>
<%--套红--%>
<script charset="utf-8" src="/assets/js/cmcu-upload.js" ></script>
<%--日程控件--%>
<script charset="UTF-8"  src="/m/global/plugins/fullcalendar/fullcalendar.min.js" ></script>
<script charset="UTF-8"  src="/m/global/plugins/fullcalendar/lang/zh-cn.js"></script>

<script charset="UTF-8" src="/editor-app/custom/js/services.act.js?version=${version}"></script>
<script charset="UTF-8" src="/editor-app/custom/js/basic.act.js?version=${version}"></script>
<script charset="UTF-8" src="/editor-app/custom/js/controllers.act.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.hr1.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.hr2.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.sys.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.cp.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.ed.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.business.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.five.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.five.home.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.five.supervise.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.five1.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.five.oa.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.five.hr.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.five.asset.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.oa1.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/services.oa2.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/route.common.js?version=${version}"></script>

<script charset="UTF-8" src="/assets/page/services.common.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.common.js?version=${version}"></script>

<script charset="UTF-8" src="/assets/page/controllers.business.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.hr1.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.hr2.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.sys.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.mcc.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.cp.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.ed.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.ed.part2.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.ed.input.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five1.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five.business.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five.hr.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five.asset.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.me.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.oa2.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five.oa.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five.home.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/controllers.five.supervise.js?version=${version}"></script>
<script charset="UTF-8" src="/assets/page/customCadEdRoute.js?version=${version}"></script>


<script>
    jQuery(document).ready(function () {
        Metronic.init();
        Layout.init();
    });

    var APP_VERSION=1;
    var projectId ='${projectId}';
    var enLogin ='${enLogin}';
    var user ={enLogin:enLogin,userLogin:enLogin};
    var taskApp = angular.module('taskApp', ['ui.router', 'ui.router.state.events', 'ui.my.directives', "services.common", "controllers.common", "services.act", "controllers.act",
        'controllers.mcc', 'controllers.sys',
        'controllers.cp', "controllers.hr1", "controllers.hr2", "controllers.ed", 'controllers.ed.part2', 'controllers.ed.input',
         "controllers.oa2",
        'controllers.me', 'controllers.business', 'controllers.five.oa',
        "controllers.five", "controllers.five1", 'controllers.five.business', 'controllers.five.hr', 'controllers.five.home','controllers.five.supervise', 'controllers.five.asset',
        'services.business', "services.oa1", "services.oa2",
        'services.sys', 'services.cp', 'services.hr1', 'services.hr2', 'services.ed',
        'services.five', 'services.five1', 'services.five.hr', 'services.five.oa', 'services.five.home','services.five.supervise', 'services.five.asset'])
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider) {
        $locationProvider.html5Mode({
            enable: true,
            requireBase: false
        });

        initCustomCadEdRoute($stateProvider);
        /**
         * 加载流程情况
         * @param processInstanceId
         */
    });

    taskApp.run(function ($rootScope, $state){
        $rootScope.user=user;
        $state.go("five.detail", {id: projectId});
    });





    taskApp.controller("FootController", function ($state,$rootScope,$scope,actTaskQueryService,actProcessQueryService,
                                            sysRoleService,sysAttachService, commonCodeService, edFileService,
                                            edProjectService, edProjectTreeService, actService,myActService,
                                            hrDeptService,hrEmployeeService) {


        commonCodeService.selectAll().then(function (value) {
            if(value.data.ret){
                $rootScope.sysCodes=value.data.data;
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
        }

        $rootScope.updateUserInformation=function(){

            if ($rootScope.pwd1.length>0){
                if($rootScope.pwd1!=$rootScope.pwd2){
                    $rootScope.pwd2="";
                    toastr.error("两次输入密码不一致!");
                    return;
                }
                if (checkPass($rootScope.pwd1)<3){
                    $rootScope.pwd2="";
                    toastr.error("新密码复杂度不够，请重新设置！需6-16位，且包含大小写字母和数字。");
                    return;
                }
                hrEmployeeService.resetPassword(user.userLogin,$rootScope.pwd1).then(function (value) {
                    if(value.data.ret){
                        toastr.success("密码修改成功!");
                        $("#personalInformationModal").modal("hide");
                    }
                })
            }
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
            if(pass.match(/[^a-zA-Z0-9]+/)){
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

            $('.form_datetime').datetimepicker({
                autoclose: true,
                language: 'zh-CN',
                isRTL: Metronic.isRTL(),
                minuteStep:15,
                pickerPosition: (Metronic.isRTL() ? "bottom-right" : "bottom-left")
            });

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

                //五洲待签名附件
                actTaskQueryService.listHistoricTaskInstanceByInstanceId(processInstanceId, user.enLogin).then(function (value) {
                    var list = value.data.data;
                    var optionlist=[];
                    for (var i=0,l=list.length;i<l;i++){
                        for(var j = i + 1; j < l; j++){
                            if (list[i].name == list[j].name&&list[i].assigneeName==list[j].assigneeName){
                                ++i;
                            }
                        }
                        optionlist.push(list[i]);
                    }
                    optionlist.reverse();
                    $rootScope.optionlist=optionlist;
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
                appCode:user.appCode
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
            hrDeptService.selectDeptByDeptIds($rootScope.selectOaEmployeeConfig.deptIds).then(function (value) {
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
                                style: ($rootScope.selectOaEmployeeConfig.deptIdList.indexOf("," + item.id + ",") >= 0 ? "color:green":"")
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
                $('#' + deptTreeId).jstree("destroy");
                $('#' + deptTreeId)
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
            var deptTreeId = "employee_dept_tree_";
            var selectedNodes=$('#'+deptTreeId).jstree(true).get_selected(true);

            if(selectedNodes.length===1&&parseInt(selectedNodes[0].id)===$rootScope.selectEmployeeConfig.parentDeptId){
                $rootScope.querySelectEmployee_();
            }else{
                $('#'+deptTreeId).jstree("deselect_all", true);
                $('#'+deptTreeId).jstree('select_node',$rootScope.selectEmployeeConfig.parentDeptId);
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

        $rootScope.initEmployeeModal();
        $rootScope.initOaEmployeeModal();
        $rootScope.judgePassword();

    })


</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>