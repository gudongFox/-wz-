angular.module('controllers.common', [])


    .controller('CommonBaseController',function ($state,$stateParams,$scope,$http,$rootScope,commonFormDataService,commonFileService,actProcessQueryService,actTaskQueryService,commonCodeService){



        /**
         * 通用下载
         * @param url 下载的URL地址
         * @param params 其他参数
         */
        $rootScope.commonDownload=function(url,params){
            if(!params){
                params={enLogin: user.enLogin};
            }
            if((navigator.userAgent.indexOf("compatible") > -1 && navigator.userAgent.indexOf("MSIE") > -1)) {
                window.location.href = url;
                return;
            }

            commonFileService.download(url,params).then(function (response) {
                var blob=new Blob([response.data.data], {type: response.data.data.type});
                if(response.data.data.type==="application/json"){
                    var reader = new FileReader();
                    reader.readAsText(blob, 'utf-8');
                    reader.onload = function (e) {
                        var result=jQuery.parseJSON(reader.result);
                        toastr.error(result.msg);
                    }
                    return;
                }
                var objectUrl = URL.createObjectURL(blob);
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                if('msSaveOrOpenBlob' in navigator){
                    //ie使用的下载方式
                    window.navigator.msSaveOrOpenBlob(blob, fileName);
                }else{
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = decodeURI(fileName);
                    a.href = objectUrl;
                    a.click();
                }

            });
        }

        $rootScope.commonAttachDownload=function(id){
          $rootScope.commonDownload('/common/attach/download/'+id,undefined);
        }

        $rootScope.loadNewProcessInstance=function(processInstanceId,businessKey) {

            $rootScope.refreshTime = new Date().getTime();
            $rootScope.tasks = [];
            $rootScope.tplConfig = {};
            $rootScope.processInstanceId = processInstanceId;
            if (processInstanceId||businessKey) {
                commonFormDataService.getTplConfig(processInstanceId, businessKey, user.enLogin).then(function (value) {
                    $rootScope.tplConfig = value.data.data;
                });
                actTaskQueryService.listHistoricTaskMap(processInstanceId).then(function (value) {
                    $rootScope.tasks = value.data.data;
                });
                actProcessQueryService.getCustomProcessInstance(processInstanceId, businessKey, user.enLogin).then(function (value) {
                    $rootScope.processInstance = value.data.data;
                });
            }
        }

        $rootScope.getTplConfig=function (processInstanceId,businessKey,enLogin) {
            commonFormDataService.getTplConfig(processInstanceId, businessKey, enLogin).then(function (value) {
                $rootScope.tplConfig = value.data.data;
            });
        }

        /**
         * 下载/预览文件
         * @param file 文件
         * @param preview true 就直接进入预览模式，如果不可以预览，则直接下载
         */
        $rootScope.downloadFile=function(file,preview){
            var url = "/common/attach/download/" + file.commonAttach.id;
            if (preview) {
                if ($scope.owaInfo.owa) {
                    if ($.inArray(file.commonAttach.extensionName, $scope.owaInfo.owaFileType) >= 0) {
                        url = "/common/previewFile/" + file.id;
                        window.open(url, "_blank");
                        return;
                    }
                }
            }
            $rootScope.commonDownload("/common/attach/download/file/" + file.id);
        }


        /**
         * 页面控件初始化
         */
        $rootScope.initWebControl=function () {
            setTimeout(function () {

                try {
                    $('.date-picker').datepicker({
                        orientation: "auto",
                        autoclose: true,
                        language: 'zh-CN'
                    });
                }catch (e) {
                }


                try {
                    $('.kt-select2').select2({
                        placeholder: "请选择合适的项",
                    });
                }catch (e) {

                }

                try{
                    $('.bs-select').selectpicker({
                        iconBase: 'fa',
                        tickIcon: 'fa-check',
                        noneSelectedText:'----未选择----'
                    });

                }catch (e) {

                }


                try{
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

                }catch (e) {

                }

            }, 600);
        }


        /**
         * 得到页面数据
         * @param id
         * @param groupList
         * @returns {{id: *, enLogin: *}}
         */
        $rootScope.getFormData=function (id,groupList) {
            var data = {id: id, enLogin: user.enLogin};
            for (var i = 0; i < groupList.length; i++) {
                var group = groupList[i];
                for (var j = 0; j < group.items.length; j++) {
                    var item = group.items[j];
                    if (item.commonFormDetail.editable) {
                        data[item.commonFormDetail.inputCode] = item.inputValue;
                    }
                }
            }

            $(".kt-select2").each(function (a) {
                data[$(this).attr("name")] = $(this).val();
            });
            return data;
        }

        $rootScope.checkFormValidate=function() {
            var errorList = [];
            $('.form-validate').each(function () {
                var formValidate = $(this).validate();
                if (!formValidate.form()) {
                    for(var i=0;i<formValidate.errorList.length;i++) {
                        errorList.push(formValidate.errorList[i]);
                    }
                }
            });
            return errorList;
        }

        $rootScope.showFormSelectUser=function(item,group) {
            if(!item.commonFormDetail.editable) return;

            var multiple = item.commonFormDetail.multiple;
            var key = item.commonFormDetail.inputCode.replace('Name', '');
            var loginItem = {inputValue:""};
            var majorName='';
            var buildName="";
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

            var config={
                title: item.commonFormDetail.inputText,
                enLogin:user.enLogin,
                multiple: multiple,
                selects: selects,
                formDataId:group.formDataId,
                roleCode:key.replace('Man','').replace('Men',''),
                majorName:majorName,
                buildName:buildName,
                dataSource:item.commonFormDetail.dataSource,
            };


            jQuery.showJsTreeSelectUserModal(config, function (selects) {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < selects.length; i++) {
                    enLoginList.push(selects[i].id);
                    cnNameList.push(selects[i].text);
                }
                loginItem.inputValue = enLoginList;
                if(item.commonFormDetail.inputCode.indexOf('Man')>=0||item.commonFormDetail.inputCode.indexOf('Men')>=0){
                    loginItem.inputValue = enLoginList.join(',');
                }
                item.inputValue = cnNameList.join(',');
                $scope.$apply();
            })
        }

        $rootScope.showFormSelectDept=function(item,group) {
            if (!item.commonFormDetail.editable) return;
            if (!item.commonFormDetail.editable) return;
            var multiple = item.commonFormDetail.multiple;
            var idKey = item.commonFormDetail.inputCode.replace('Name', '');
            var chargeManKey=item.commonFormDetail.inputCode.replace('Name', '')+"ChargeMan";
            var chargeMenKey=item.commonFormDetail.inputCode.replace('Name', '')+"ChargeMen";
            ;
            var idItem = {inputValue: ""};
            var chargeManItem=undefined;
            var chargeMenItem=undefined;
            for (var j = 0; j < group.items.length; j++) {
                var currentItem = group.items[j];
                var code = currentItem.commonFormDetail.inputCode;
                if (code === idKey||code===idKey+"Id") {
                    idItem = currentItem;
                }else if(code===chargeManKey){
                    chargeManItem=currentItem;
                }else if(code===chargeMenKey){
                    chargeMenItem=currentItem;
                }
            }
            var selects = idItem.inputValue;
            if (idItem.inputValue.constructor === Array) {
                selects = idItem.inputValue.join(',');
            }
            var dataSourceConfig = {};
            try {
                dataSourceConfig = jQuery.parseJSON(item.commonFormDetail.dataSource);
            } catch (e) {

            }
            var params = $.extend({}, dataSourceConfig, {
                selects: selects,
                multiple: multiple
            });

            ;
            jQuery.showJsTreeSelectDeptModal(params, function (selects, ids, names,charges) {
                item.inputValue = names;
                idItem.inputValue = ids;
                if(chargeManItem){
                    chargeManItem.inputValue="";
                    if(charges.length>0){
                        chargeManItem.inputValue=charges[0];
                    }
                }
                if(chargeMenItem){
                    chargeMenItem.inputValue=charges;
                }
                $scope.$apply();
            });
        }


        $rootScope.showFormTree=function(item,group) {

            jQuery.showJsTreeDataSourceModal({
                treeData: item.dataSource,
                multiple: item.commonFormDetail.multiple,
                title:item.commonFormDetail.inputText,
                selects:item.inputValue
            }, function (selects, codes, names) {
                item.inputValue = codes;
                $scope.$apply();
            });
        }


        $rootScope.initFileUpload=function (id,addCallBack,doneCallBack,config){

            var uploadFile=$("#"+id);
            var dropZone=null;
            if(config&&config.dropZone){
                dropZone="#"+config.dropZone;
            }
            if(uploadFile) {
                uploadFile.fileupload({
                    dataType: 'json',
                    dropZone: dropZone,
                    progress: function (e, data) {
                        var speed = (data.bitrate / (8 * 1024 * 1024)).toFixed(2) + "Mbit/s";
                        var percent = parseInt(data.loaded / data.total * 100, 10);
                        $("#uploadProgress").css('width', percent + "%");
                        $(".blockui span:eq(0)").html(" " + speed + " " + percent + "%");
                    },
                    send: function (e, data) {
                        Metronic.blockUI({
                            boxed: true
                        });
                    },
                    add: function (e, data) {
                        var file = data.files[0];
                        if (file.size > 5 * 1024 * 1024 * 1024) {
                            toastr.error('文件大小超过最大限制5GB!');
                            return false;
                        }
                        data.formData = {enLogin: user.enLogin};
                        if(addCallBack(e, data)) {
                            data.submit();
                        }
                    },
                    done: function (e, data) {
                        Metronic.unblockUI();
                        if (data.result.ret) {
                            toastr.success("上传成功!");
                        }
                        doneCallBack(e, data);
                    }
                });
            }
        }

        $scope.init=function () {
            commonCodeService.listDataByCatalog(user.enLogin,"").then(function (value) {
                if(value.data.ret){
                    $rootScope.commonCodes=value.data.data;
                }
            });

            $http({
                method: 'POST',
                url: '/common/getOwaStatus.json'
            }).then(function (value) {
                $scope.owaInfo = value.data.data;
            })
        }



        $scope.init();
    })

    .controller('CommonActActionController',function ($state,$stateParams,$scope,$rootScope,actTaskHandleService,actProcessHandleService,commonFormDataService){


        $rootScope.toggleStar=function () {
            var msg=$rootScope.processInstance.stared?'取消关注':'关注';
            bootbox.confirm("你要"+msg+"流程吗？", function (result) {
                if(result) {
                    actProcessHandleService.toggleStar($rootScope.processInstance.instance.id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success(msg + '成功');
                            $scope.refresh();
                        }
                    });
                }
            });
        }

        $rootScope.showSendTask=function (send) {

            debugger;
            //拥有保存权限、尝试提交任务,则判断对应数据是否有正确填写
            if($scope.tplConfig.saveAble) {
                var errorList=$rootScope.checkFormValidate();
                if(errorList.length>0) {
                    var msg="";
                    for(var i=0;i<errorList.length;i++){
                        msg=msg+errorList[i].element.name+'：'+errorList[i].message+'<br/>';
                    }
                    toastr.error(msg);
                    return;
                }
            }

            var tplConfig=$scope.tplConfig;
            if(tplConfig.autoSubmit&&$scope.groupList){
                var data=$scope.getFormData($scope.data.id,$scope.groupList);
                data["autoSubmit"]=true;
                commonFormDataService.save(data).then(function (value) {
                    if(value.data.ret){
                        jQuery.showActHandleModal({
                            taskId: $rootScope.processInstance.taskId,
                            processInstanceId: $rootScope.processInstance.instance.id,
                            send: send,
                            enLogin: user.enLogin
                        }, function () {
                            return true;
                        }, function (processInstanceId) {
                            $scope.back();
                        });
                    }
                })
            }else {
                jQuery.showActHandleModal({
                    taskId: $rootScope.processInstance.taskId,
                    processInstanceId: $rootScope.processInstance.instance.id,
                    send: send,
                    enLogin: user.enLogin
                }, function () {
                    return true;
                }, function (processInstanceId) {
                    $scope.back();
                });
            }
        }

        $rootScope.showTakeTask=function() {
            var taskId=$rootScope.processInstance.taskId;
            bootbox.confirm("你确定要接受该任务吗？", function (result) {
                if (result) {
                    actTaskHandleService.takeTask(taskId, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("接受任务成功");
                            $scope.refresh();
                        }
                    });
                }
            })
        }

        $rootScope.showFetchTask=function(){
            var taskId=$rootScope.processInstance.fetchTaskId;
            bootbox.confirm("你确定要取回该任务吗？", function (result) {
                if (result) {
                    actTaskHandleService.fetchTask(taskId, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("取回成功");
                            $scope.refresh();
                        }
                    });
                }
            })
        }

        $rootScope.showResolveTask=function() {
            jQuery.showActResolveModal({
                taskId: $rootScope.processInstance.taskId,
                processInstanceId:$rootScope.processInstance.instance.id,
                enLogin: user.enLogin
            }, function () {
                return true;
            }, function (processInstanceId) {
                $scope.back();
            });
        }

        $rootScope.showTransferTask=function () {
            jQuery.showActTransferModal({
                taskId: $rootScope.processInstance.taskId,
                processInstanceId:$rootScope.processInstance.instance.id,
                enLogin: user.enLogin
            }, function () {
                return true;
            }, function (processInstanceId) {
                $scope.back();
            });
        }

        $rootScope.showDelegateTask=function () {
            jQuery.showActDelegateModal({
                    taskId: $rootScope.processInstance.taskId,
                    processInstanceId:$rootScope.processInstance.instance.id,
                    enLogin: user.enLogin
                }, function () {
                    return true;
                }, function (processInstanceId) {
                    $scope.back();
                });
        }

        $rootScope.showCcFinishTask=function() {
            bootbox.confirm("你要办结该抄送任务吗？", function (result) {
                if(result) {
                    var taskId=$rootScope.processInstance.ccTaskId;
                    actTaskHandleService.ccFinishTask(taskId,result.value).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("已办结抄送任务!");
                            $scope.back();
                        }
                    });
                }
            });
        }

        $rootScope.adminRemoveProcess=function() {
            bootbox.prompt("即将永久删除该流程,请输入原因！", function (result) {
                if(result) {
                    actProcessHandleService.deleteProcessInstanceById($rootScope.processInstance.instance.id,"",result).then(function (value) {
                        if(value.data.ret){
                            toastr.success("删除成功!");
                            $scope.back();
                        }
                    })
                }
            });
        }


        $rootScope.refresh=function () {
            console.log("nothing");
        }

    })

    /**
     * $scope.tplConfig={businessKey:"",editable:false,showBusinessFile:true,showFileType:true,fileTypeCode:"文件类型"}
     */
    .controller('CloudDirAndFileController',function ($state,$http,$stateParams,$scope,commonFileService,commonFileCoService,commonDirService) {
        var businessKey;

        $scope.init = function () {

            $scope.loadData(0);

            $scope.initUpload();

            if ($scope.tplConfig.showFileType) {
                commonFileService.listFileType(user.tenetId, $scope.tplConfig.fileTypeCode).then(function (value) {
                    $scope.fileTypes = value.data.data;
                })
            }

            $("#cb_all").unbind('click').bind('click', function (e) {
                var checked = $(this).prop('checked');
                $(".cbDirOrFile").each(function () {
                    $(this).prop("checked", checked);
                })
            });
            $("#co_cb_all").unbind('click').bind('click', function (e) {
                var checked = $(this).prop('checked');
                $(".coCbDir").each(function () {
                    $(this).prop("checked", checked);
                })
                $(".coCbFile").each(function () {
                    $(this).prop("checked", checked);
                })
            });


            $http({
                method: 'POST',
                url: '/common/getOwaStatus.json'
            }).then(function (value) {
                $scope.owaInfo = value.data.data;
            })

        }

        //选择发布区文件va
        var coParentId = 0;
        $scope.showSelectCoModel = function () {
            $scope.listCoFileAndDirs(0);
            $("#selectCoModel").modal("show");
        }
        $scope.listCoFileAndDirs = function (parentId) {
            coParentId = parentId;
            commonFileCoService.listSelectFile(businessKey, parentId, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.commonCoFiles = value.data.data;
                }
            })
            commonFileCoService.listSelectDir(businessKey, parentId, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.commonCoDirs = value.data.data;
                }
            })
            commonFileCoService.listBreadcrumb(businessKey, parentId, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.coBreadcrumbs = value.data.data;
                }
            })
        }
        $scope.saveSelectCo = function () {
            var dirIds = "";
            var fileIds = "";
            $(".coCbDir:checked").each(function () {
                var dirId = $.parseJSON($(this).attr("data-id"));
                dirIds = dirIds + dirId + ",";
            });
            $(".coCbFile:checked").each(function () {
                var fileId = $.parseJSON($(this).attr("data-id"));
                fileIds = fileIds + fileId + ",";
            });
            commonFileCoService.insertSelect(businessKey, coParentId, dirIds, fileIds, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功");
                    $scope.loadData(0);
                }
            });
            $("#selectCoModel").modal("hide");
        }

        $scope.loadData = function (currentDirId) {
            if (businessKey) {
                if (currentDirId !== undefined) {
                    $scope.currentDirId = currentDirId;
                }

                commonDirService.listBreadcrumb(businessKey, $scope.currentDirId).then(function (value) {
                    $scope.breadcrumbs = value.data.data;
                })

                commonDirService.listData(businessKey, $scope.currentDirId).then(function (value) {
                    $scope.dirs = value.data.data;
                })

                commonFileService.listData(businessKey, $scope.currentDirId).then(function (value) {
                    $scope.files = value.data.data;
                });


                $scope.currentDir = {};
                if (currentDirId > 0) {
                    commonDirService.getModelById(currentDirId).then(function (value) {
                        $scope.currentDir = value.data.data;
                        if (businessKey.indexOf('co_') === 0) {
                            $scope.tplConfig.editable = $scope.currentDir.buildId > 0;
                        }
                    })
                }
            }
        }

        $scope.newDir = function () {
            bootbox.prompt("请输入文件夹名称", function (result) {
                if (result) {
                    commonDirService.newDir(businessKey, result, $scope.currentDirId, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            $scope.loadData($scope.currentDirId);
                        }
                    })
                }
            });
        }

        $scope.removeFile = function (id) {
            bootbox.confirm("确认要删除该文件吗?", function (result) {
                if (result) {
                    commonFileService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            $scope.loadData();
                        }
                    });
                }
            })
        }

        $scope.showFile = function (id) {
            commonFileService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
                $scope.loadHistory();
            });
            $("#commonFileModal").modal("show");
        }

        $scope.showDir = function (id) {
            commonDirService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
            });
            $("#commonDirModal").modal("show");
        }

        $scope.loadHistory = function () {
            commonFileService.listHistory($scope.item.id).then(function (value) {
                $scope.historyList = value.data.data;
            })
        }

        $scope.removeHistory = function (attachId) {
            bootbox.confirm("确认要删除该版本吗?", function (result) {
                if (result) {
                    commonFileService.removeHistory($scope.item.id, attachId, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            $scope.loadHistory();
                            toastr.success("删除成功");
                        }
                    });
                }
            })
        }


        $scope.downloadDir = function (id) {
            $scope.commonDownload("/common/attach/download/dir/" + id);
        }

        $scope.downloadAttach = function (attachId) {
            $scope.commonDownload("/common/attach/download/" + attachId);
        }

        $scope.saveFile = function (item) {
            if (item.seq === undefined) {
                toastr.error("排序必须为数字类型不能为空!");
                return;
            }
            commonFileService.save(item.id, item.fileName, item.fileType, item.seq, item.remark, user.enLogin).then(function (value) {
                $("#commonFileModal").modal("hide");
                if (value.data.ret) {
                    toastr.success("保存成功");
                    $scope.loadData();
                }
            })
        }

        $scope.removeDir = function (id) {
            bootbox.confirm("确认要删除该文件夹及所有文件吗?", function (result) {
                if (result) {
                    commonDirService.remove(id, user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            $scope.loadData();
                        }
                    });
                }
            })
        }

        $scope.saveDir = function (item) {
            if (item.seq === undefined) {
                toastr.error("排序必须为数字类型不能为空!");
                return;
            }

            commonDirService.save(item.id, item.cnName, item.seq, item.remark, user.enLogin).then(function (value) {
                $("#commonDirModal").modal("hide");
                if (value.data.ret) {
                    toastr.success("保存成功");
                    $scope.loadData();
                }
            })
        }


        $scope.removeMore = function () {
            var ids = [];
            $(".cbDirOrFile").each(function () {
                if ($(this).prop("checked")) {
                    ids.push($(this).attr("data-id"));
                }
            });

            if (ids.length == 0) {
                toastr.warning("请先勾选合适的文件!")
                return;
            }
            bootbox.confirm("确认要删除选中的文件夹及所有文件吗?", function (result) {
                if (result) {
                    commonDirService.removeMore(ids.join(','), user.enLogin).then(function (value) {
                        if (value.data.ret) {
                            $scope.loadData();
                        }
                    });
                }
            })
        }

        $scope.downloadMore = function () {
            var ids = [];
            $(".cbDirOrFile").each(function () {
                if ($(this).prop("checked")) {
                    ids.push($(this).attr("data-id"));
                }
            });
            if (ids.length == 0) {
                toastr.warning("请先勾选合适的文件!")
                return;
            }
            $scope.commonDownload("/common/attach/downloadMore", {enLogin: user.enLogin, ids: ids.join(',')});
        }


        var refreshTimer;

        $scope.initUpload = function () {

            $("#uploadCommonFile").fileupload({
                dropZone: $scope.tplConfig.editable ? $("#businessFileZone") : null,
                dataType: 'json',
                url: "/common/file/receive.json",
                progress: function (e, data) {
                    var speed = (data.bitrate / (8 * 1024 * 1024)).toFixed(2) + "Mbit/s";
                    var percent = parseInt(data.loaded / data.total * 100, 10);
                    $("#uploadProgress").css('width', percent + "%");
                    if (APP_VERSION == 1) {
                        $(".loading-message.loading-message-boxed span").html(" " + speed + " " + percent + "%");
                    } else {
                        $(".blockui span:eq(0)").html(" " + speed + " " + percent + "%");
                    }
                },
                send: function (e, data) {
                    if (APP_VERSION === 1) {
                        Metronic.blockUI({
                            boxed: true
                        });
                    } else {
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
                    if (file.size > 1024 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制1GB!');
                        return false;
                    }
                    data.formData = {enLogin: user.enLogin, businessKey: businessKey, dirId: $scope.currentDirId};
                    data.submit();
                },
                done: function (e, data) {

                    if (refreshTimer) {
                        clearTimeout(refreshTimer);
                    }

                    refreshTimer = setTimeout(function () {
                        if (APP_VERSION === 1) {
                            Metronic.unblockUI();
                        } else {
                            KTApp.unblock('body');
                        }
                        if (data.result.ret) {
                            toastr.success("上传成功!");
                            $scope.loadData();
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });



            $("#uploadCommonDirectory").fileupload({
                dropZone: null,
                dataType: 'json',
                url: "/common/file/receiveDirectoryFile.json",
                add: function (e, data) {
                    var file = data.files[0];
                    data.formData = {
                        enLogin: user.enLogin,
                        businessKey: businessKey,
                        dirId: $scope.currentDirId,
                        webkitRelativePath: file.webkitRelativePath
                    };
                    data.submit();
                },
                send: function (e, data) {
                    if (APP_VERSION == 1) {
                        Metronic.blockUI({
                            boxed: true
                        });
                    } else {
                        KTApp.block('body', {
                            overlayColor: '#000000',
                            type: 'v2',
                            state: 'primary',
                            message: 'Processing...'
                        });
                    }
                },
                progress: function (e, data) {
                    var speed = (data.bitrate / (8 * 1024 * 1024)).toFixed(2) + "Mbit/s";
                    var percent = parseInt(data.loaded / data.total * 100, 10);
                    $("#uploadProgress").css('width', percent + "%");
                    if (APP_VERSION == 1) {
                        $(".blockui span:eq(0)").html(" " + speed + " " + percent + "%");
                    } else {
                        $(".loading-message.loading-message-boxed span").html(" " + speed + " " + percent + "%");
                    }
                },
                done: function (e, data) {
                    if (refreshTimer) {
                        clearTimeout(refreshTimer);
                    }

                    refreshTimer = setTimeout(function () {
                        if (APP_VERSION === 1) {
                            Metronic.unblockUI();
                        } else {
                            KTApp.unblock('body');
                        }
                        if (data.result.ret) {
                            toastr.success("上传成功!");
                            $scope.loadData();
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });
        }

        $scope.$watch("tplConfig", function () {
            if ($scope.tplConfig) {
                businessKey = $scope.tplConfig.businessKey;
                $scope.init();
            }
        });


    })

    .controller('CommonEdMarkController',function ($state,$stateParams,$scope,commonFileService,commonEdMarkService) {



        var businessKey;

        $scope.loadData =function(){
            commonEdMarkService.listData(businessKey,user.enLogin).then(function (value) {
                $scope.marks=value.data.data;
                $scope.roles=[];
                for(var i=0;i<$scope.tplConfig.markRoleNames.length;i++){
                    var role={name:$scope.tplConfig.markRoleNames[i],count:0};
                    for(var j=0;j<$scope.marks.length;j++){
                        if($scope.marks[j].roleName==role.name){
                            role.count=role.count+1;
                        }
                    }
                    $scope.roles.push(role);
                }
            })
            commonFileService.listData(businessKey,-1).then(function (value) {
                $scope.files=value.data.data;
            })
        }

        $scope.showMark = function (mark) {
            commonEdMarkService.getModelById(mark.id,user.enLogin).then(function (value) {
                $scope.targerMark = value.data.data;
            })
            $("#markModal").modal("show");
        }

        $scope.newMark = function () {
            commonEdMarkService.getNewModel(businessKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.targerMark = value.data.data;
                    $("#markModal").modal("show");
                }
            })
        }
        $scope.downloadMark = function () {
            commonEdMarkService.downloadMark(businessKey,user.enLogin).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=", "");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        }

        $scope.removeMark = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    commonEdMarkService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            $scope.loadData();
                        }
                    });

                }
            })
        }

        $scope.saveMark = function () {
            $scope.targerMark.enLogin = user.userLogin;
            commonEdMarkService.save($scope.targerMark).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!");
                        $scope.loadData();
                    }
            })
            $("#markModal").modal("hide");
        }

        $scope.showImage = function (item) {
            $scope.item = $.extend({}, item);
            $("#definitionImageModal").modal("show");
        }

        $scope.initUploadMark=function () {
            $("#uploadMark").fileupload({
                dropZone: $("#edMarkZone"),
                dataType: 'json',
                progress: function (e, data) {
                    var speed = (data.bitrate / (8 * 1024 * 1024)).toFixed(2) + "Mbit/s";
                    var percent = parseInt(data.loaded / data.total * 100, 10);
                    $("#uploadProgress").css('width', percent + "%");
                    $(".loading-message.loading-message-boxed span").html(" " + speed + " " + percent + "%");
                },
                send: function (e, data) {
                    Metronic.blockUI({
                        boxed: true
                    });
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
                    data.formData = {enLogin: user.enLogin,businessKey:businessKey};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    if (data.result.ret) {
                        toastr.success("上传成功!");
                        $scope.loadData();
                    } else {
                        toastr.error(data.result.msg);
                    }
                }
            });
        }


        $scope.$watch("tplConfig",function () {
            if($scope.tplConfig!=null) {
                businessKey = $scope.tplConfig.businessKey;
                $scope.roleNames = $scope.tplConfig.markRoleNames;
                $scope.loadData();
                $scope.initUploadMark();
            }
        });



    })

    .controller('CommonFileMarkController',function ($state,$stateParams,$scope,commonFileService,commonEdMarkService) {

        $scope.loadData =function(){
            commonEdMarkService.listDataByFileId(fileId).then(function (value) {
                $scope.marks=value.data.data;
            })
            commonFileService.listData(businessKey,-1).then(function (value) {
                $scope.files=value.data.data;
            })
        }

        $scope.showMark = function (mark) {
            commonEdMarkService.getModelById(mark.id,user.enLogin).then(function (value) {
                $scope.targerMark = value.data.data;
            })
            $("#markModal").modal("show");
        }

        $scope.newMark = function () {
            commonEdMarkService.getNewModel(businessKey, user.userLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.targerMark = value.data.data;
                    $("#markModal").modal("show");
                }
            })
        }

        $scope.removeMark = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    commonEdMarkService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            $scope.loadData();
                        }
                    });

                }
            })
        }

        $scope.saveMark = function () {
            $scope.targerMark.enLogin = user.userLogin;
            commonEdMarkService.save($scope.targerMark).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    $scope.loadData();
                }
            })
            $("#markModal").modal("hide");
        }

        $scope.showImage = function (item) {
            $scope.item = $.extend({}, item);
            $("#definitionImageModal").modal("show");
        }
        $scope.$watch("tplConfig",function () {
            if($scope.tplConfig!=null) {
                businessKey = $scope.tplConfig.businessKey;
                $scope.roleNames = $scope.tplConfig.markRoleNames;
                $scope.loadData();
            }
        });
    })

    .controller('CommonEdQuestionController',function ($state,$stateParams,$scope,commonEdQuestionService){

        var refreshTimer;

        $scope.init=function(){

            $scope.queryData();

            $("#uploadFile").fileupload({
                dataType: 'json',
                url:"/common/edQuestion/upload.json",
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
                    data.formData = {enLogin: user.enLogin};
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
                            $scope.queryData();
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });

            $("input[type='search']").keypress(function (e) {
                if (e.which === 13) {
                    $scope.queryData();
                    return false;
                }
            });
        }

        $scope.params = getNgParam($state, {
            majorName: "",
            questionLv:"",
            q:"",
        });

        $scope.pageInfo = {pageNum: 1, pageSize: pageSize};

        $scope.queryData = function () {
            $scope.pageInfo.pageNum = 1;
            $scope.params.qq = $scope.params.q;
            $scope.params.qMajorName = $scope.params.majorName;
            $scope.params.qQuestionLv = $scope.params.questionLv;
            $scope.loadPagedData();
        };

        $scope.loadPagedData = function () {
            var params = {
                q:$scope.params.qq,
                majorName: $scope.params.qMajorName,
                questionLv: $scope.params.qQuestionLv,
                enLogin: user.enLogin,
                pageNum: $scope.pageInfo.pageNum,
                pageSize: $scope.pageInfo.pageSize
            };
            commonEdQuestionService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    $scope.pageInfo = value.data.data;
                    setNgCache($state, $scope.params, $scope.pageInfo);
                }
            })
        };

        $scope.newData=function() {
            commonEdQuestionService.getNewModel(user.enLogin).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        $scope.showDetail=function(id){
            commonEdQuestionService.getModelById(id).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        $scope.remove=function(id){
            bootbox.confirm("确定删除该数据吗?",function (result) {
                if(result){
                    commonEdQuestionService.remove(id,user.enLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功");
                            $scope.loadPagedData();
                        }
                    })
                }
            });

        }

        $scope.save=function(){
            commonEdQuestionService.save($scope.item).then(function (value) {
                if(value.data.ret) {
                    $("#itemModal").modal("hide");
                    $scope.loadPagedData();
                    toastr.success("保存成功");
                }
            })
        }


        $scope.downloadData=function(){
            $scope.commonDownload('/common/edQuestion/download.json',{tenetId:user.tenetId});
        }

        $scope.init();

    })

    .controller('CommonBlackController',function ($rootScope,$state,$stateParams,$scope,commonBlackService){

        $scope.loadData=function(){
            commonBlackService.selectAll(user.enLogin).then(function (value) {
                $scope.list=value.data.data;
                $('.form_datetime').datetimepicker({
                    autoclose: true,
                    language: 'zh-CN',
                    isRTL: Metronic.isRTL(),
                    minuteStep:15,
                    pickerPosition: (Metronic.isRTL() ? "bottom-right" : "bottom-left")
                });
            });

        }

        $scope.newData=function() {
            commonBlackService.getNewModel(user.enLogin).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        $scope.showDetail=function(id){
            commonBlackService.getModelById(id).then(function (value) {
                $scope.item=value.data.data;
                var date =new Date($scope.item.gmtExpired);
                $scope.item.gmtExpired =date.pattern('yyyy-MM-dd HH:mm:ss');
                ;
                $("#itemModal").modal("show");
            })
        }
        Date.prototype.pattern=function(fmt) {
            var o = {
                "M+" : this.getMonth()+1, //月份
                "d+" : this.getDate(), //日
                "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
                "H+" : this.getHours(), //小时
                "m+" : this.getMinutes(), //分
                "s+" : this.getSeconds(), //秒
                "q+" : Math.floor((this.getMonth()+3)/3), //季度
                "S" : this.getMilliseconds() //毫秒
            };
            var week = {
                "0" : "/u65e5",
                "1" : "/u4e00",
                "2" : "/u4e8c",
                "3" : "/u4e09",
                "4" : "/u56db",
                "5" : "/u4e94",
                "6" : "/u516d"
            };
            if(/(y+)/.test(fmt)){
                fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
            }
            if(/(E+)/.test(fmt)){
                fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);
            }
            for(var k in o){
                if(new RegExp("("+ k +")").test(fmt)){
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
                }
            }
            return fmt;
        }

        $scope.remove=function(id){
            bootbox.confirm("确定删除该数据吗?",function (result) {
                if(result){
                    commonBlackService.remove(id,user.enLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功");
                            $scope.loadData();
                            $("#itemModal").modal("hide");
                        }
                    })
                }
            });

        }

        $scope.save=function(){
            $scope.item.gmtExpired =Date.parse($scope.item.gmtExpired);
            commonBlackService.save($scope.item).then(function (value) {
                if(value.data.ret) {
                    $("#itemModal").modal("hide");
                    $scope.loadData();
                    toastr.success("保存成功");
                }
            })
        }

        $scope.loadData();
    })

    .controller('CommonExportController',function ($state,$stateParams,$scope,$rootScope,commonExportService,commonUserService){
        $scope.params={selectId:'',selectName:'',deptId:'',startTime:'',endTime:'',creator:''}

        $scope.init=function(){
            $rootScope.initWebControl();
            $scope.buildTree();

            commonUserService.listDept(user.enLogin,0).then(function (value) {
                $scope.deptList=value.data.data;
                $scope.deptList.push({name:''})
            })
        }

        $scope.buildTree = function () {
            commonExportService.listData(user.enLogin).then(function (value) {
                var list = value.data.data;
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: "#",
                        text: item.name,
                        state: {opened: false, disabled: false, selected: false}
                    };
                    if ($scope.params.selectId == parseInt(item.id)) {
                        node.state.selected = true;
                    }

                    treeData.push(node);
                }
                $('#position_tree').jstree("destroy");
                $('#position_tree')
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if( $scope.params.selectId==parseInt(node.id)){
                            $scope.listSelectData();
                        }else{
                            $scope.params.selectId = node.id;
                            $scope.params.selectName = node.text;
                            $scope.listSelectData();
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData
                        }
                    });

            });
        };


        $scope.newData=function() {
            commonExportService.getNewModel(user.enLogin).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        $scope.showDetail=function(){
            commonExportService.getModelById($scope.params.selectId).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        $scope.remove=function(id){
            bootbox.confirm("确定删除该数据吗?",function (result) {
                if(result){
                    commonExportService.remove(id,user.enLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功");
                            $scope.buildTree();
                            $("#itemModal").modal("hide");
                        }
                    })
                }
            });

        }

        $scope.save=function(){
            commonExportService.save($scope.item).then(function (value) {
                if(value.data.ret) {
                    $("#itemModal").modal("hide");
                    $scope.buildTree();
                    toastr.success("保存成功");
                }
            })
        }

        $scope.exportExcel=function(){
            var par={id:$scope.params.selectId,deptId:$scope.params.deptId,startTime:$scope.params.startTime,endTime:$scope.params.endTime,creator:$scope.params.creator}
            $scope.commonDownload('/common/export/download/'+par.id,par);
        }

        $scope.listSelectData=function(){
            var par={id:$scope.params.selectId,deptId:$scope.params.deptId,startTime:$scope.params.startTime,endTime:$scope.params.endTime,creator:$scope.params.creator}
            commonExportService.listSelectData(par).then(function (value) {
                if(value.data.ret) {
                    $scope.list=value.data.data;
                }
            })
        }

        $scope.init();

    })

    .controller('CommonCodeController',function ($state,$stateParams,$scope,commonCodeService){


        var refreshTimer;

        $scope.init=function(){
            commonCodeService.listCodeCategory(user.enLogin).then(function (value) {
                $scope.codeCatalogs=value.data.data;
            });

            $scope.queryData();

            $("#uploadFile").fileupload({
                dataType: 'json',
                url:"/common/code/receive.json",
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
                    data.formData = {enLogin: user.enLogin};
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
                            $scope.queryData();
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });

            $("input[type='search']").keypress(function (e) {
                if (e.which === 13) {
                    $scope.queryData();
                    return false;
                }
            });
        }

        $scope.params = getNgParam($state, {
            codeCatalog: "",
            equalCodeCatalog:"",
            code: "",
            name:"",
            q:"",
        });

        $scope.pageInfo = {pageNum: 1, pageSize: pageSize};

        $scope.queryData = function () {
            $scope.pageInfo.pageNum = 1;
            $scope.params.qq = $scope.params.q;
            $scope.params.qCode = $scope.params.code;
            $scope.params.qName = $scope.params.name;
            $scope.params.qCodeCatalog = $scope.params.codeCatalog;
            $scope.params.qEqualCodeCatalog = $scope.params.equalCodeCatalog;
            $scope.loadPagedData();
        };

        $scope.loadPagedData = function () {
            var params = {
                code: $scope.params.qCode,
                name: $scope.params.qName,
                q:$scope.params.qq,
                codeCatalog: $scope.params.qCodeCatalog,
                equalCodeCatalog:$scope.params.qEqualCodeCatalog,
                enLogin: user.enLogin,
                pageNum: $scope.pageInfo.pageNum,
                pageSize: $scope.pageInfo.pageSize
            };
            commonCodeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    $scope.pageInfo = value.data.data;
                    setNgCache($state, $scope.params, $scope.pageInfo);
                }
            })
        };

        $scope.newData=function() {
            commonCodeService.getNewModel(user.enLogin).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        $scope.showDetail=function(id){
            commonCodeService.getModelById(id).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }

        $scope.remove=function(id){
            bootbox.confirm("确定删除该数据吗?",function (result) {
                if(result){
                    commonCodeService.remove(id,user.enLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功");
                            $scope.loadPagedData();
                            $("#itemModal").modal("hide");
                        }
                    })
                }
            });

        }

        $scope.save=function(){
            commonCodeService.save($scope.item).then(function (value) {
                if(value.data.ret) {
                    $("#itemModal").modal("hide");
                    $scope.loadPagedData();
                    toastr.success("保存成功");
                }
            })
        }

        $scope.init();

    })

    .controller('CommonAttachController',function ($state,$stateParams,$scope,commonAttachService){


        var refreshTimer;

        $scope.init=function(){

            $scope.queryData();

            $("#uploadFile").fileupload({
                dataType: 'json',
                url:"/common/attach/receive.json",
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
                    if (file.size > 3072 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制3GB!');
                        return false;
                    }
                    data.formData = {creator: user.enLogin};
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
                            $scope.queryData();
                        } else {
                            toastr.error(data.result.msg);
                        }
                    }, 2000);
                }
            });

            $("input[type='search']").keypress(function (e) {
                if (e.which === 13) {
                    $scope.queryData();
                    return false;
                }
            });
        }

        $scope.params = getNgParam($state, {
            q:"",
        });

        $scope.pageInfo = {pageNum: 1, pageSize: pageSize};

        $scope.queryData = function () {
            $scope.pageInfo.pageNum = 1;
            $scope.params.qq = $scope.params.q;
            $scope.loadPagedData();
        };

        $scope.loadPagedData = function () {
            var params = {
                q:$scope.params.qq,
                enLogin: user.enLogin,
                pageNum: $scope.pageInfo.pageNum,
                pageSize: $scope.pageInfo.pageSize
            };
            commonAttachService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    $scope.pageInfo = value.data.data;
                    setNgCache($state, $scope.params, $scope.pageInfo);
                }
            })
        };



        $scope.init();

    })

    .controller('CommonFormPagedListController',function ($state,$stateParams,$scope,$rootScope,commonFormDataService){
        var vm = this;
        var referType=$stateParams.referType;
        var uiSref=$state.current.name;
        if(referType){
            uiSref=uiSref+"({referType:'"+referType+"'})";
        }

        ;

        if(!referType) {
            toastr.error("参数配置错误!");
            return;
        }

        vm.init = function () {

            vm.pageInfo = {pageNum: 1, pageSize: pageSize};
            vm.params={qq:""};

            $("input[type=search]").on('keypress',function () {
                if(event.keyCode==13){
                    vm.queryData();
                }
            })

            vm.queryData();
        };

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.q = vm.params.qq;
            vm.loadPagedData();
        };

        vm.loadPagedData=function () {
            var params = {
                pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,
                uiSref: uiSref, referType: referType,
                enLogin: user.enLogin, q: vm.params.q
            };
            commonFormDataService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.data = value.data.data;
                    vm.pageInfo = vm.data.pageInfo;
                }
            })
        }

        vm.newData=function() {
            // bootbox.confirm("确定发起新的数据流程吗?",function (result) {
            //     if(result){
                    commonFormDataService.getNewModelByUser(referType,user.enLogin).then(function (value) {
                        if(value.data.ret) {
                            vm.showDetail(value.data.data.id);
                        }
                    })
            //     }
            // });
        }

        vm.showDetail=function(id){
            $state.go("formData.detail",{formDataId:id})
        }

        vm.remove=function(id){
            bootbox.confirm("确定删除该数据吗?",function (result) {
                if(result){
                    commonFormDataService.remove(id,user.enLogin).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadPagedData();
                        }
                    })
                }
            });

        }

        vm.init();

        return vm;
    })

    .controller('CommonFormListController',function ($state,$stateParams,$scope,$rootScope,commonFormDataService){
        var vm = this;
        var referType=$stateParams.referType;
        var referId=$stateParams.referId;
        vm.q="";

        vm.init = function () {

            vm.loadData();

            $("input[type=search]").on('keypress',function () {
                if(event.keyCode==13){
                    vm.loadData();
                }
            })
        };

        vm.loadData=function () {
            commonFormDataService.listDataByUser(referType,vm.q,user.enLogin).then(function (value) {
                if(value.data.ret){
                    vm.data = value.data.data;
                }
            })
        }

        vm.newData=function() {
            // bootbox.confirm("确定发起新的数据流程吗?",function (result) {
            //     if(result){
            commonFormDataService.getNewModelByUser(referType, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    vm.showDetail(value.data.data.id);
                } else {
                    toastr.error(value.data.msg);
                }
            })
            //     }
            // });
        }

        vm.showDetail=function(id){
            if(id==581){ //"hrTrain"
                $state.go("formData.hrTrainDetail",{formDataId:id} )
            }else {
                $state.go("formData.detail", {formDataId: id})
            }
        }

        vm.remove=function(id){
            bootbox.confirm("确定删除该数据吗?",function (result) {
                if(result){
                    commonFormDataService.remove(id,user.enLogin).then(function (value) {
                        if(value.data.ret) {
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

    .controller('CommonRequestController',function ($state,$stateParams,$scope,commonRequestService){



        $scope.init=function(){
            commonRequestService.listRequestName(user.enLogin).then(function (value) {
                $scope.requestNames=value.data.data;
            });

            $scope.queryData();
            $scope.basicInit("");
        }


        $scope.params = getNgParam($state, {
            requestUrl: "",
            requestLogin: "",
            requestIp:"",
            startTime1:'',
            endTime1:''
        });

        $scope.pageInfo = {pageNum: 1, pageSize: pageSize};

        $scope.queryData = function () {
            console.log(' $scope.queryData');
            $scope.pageInfo.pageNum = 1;
            $scope.params.qRequestUrl = $scope.params.requestUrl;
            $scope.params.qRequestLogin = $scope.params.requestLogin;
            $scope.params.qRequestIp = $scope.params.requestIp;
            $scope.params.qStartTime1 = $scope.params.startTime1;
            $scope.params.qEndTime1 = $scope.params.endTime1;


            $scope.loadRightData(user.userLogin);
            $scope.loadPagedData();

        };

        $scope.loadPagedData = function () {
            console.log(' $scope.loadPagedData');
            var params = {requestIp: $scope.params.qRequestIp, requestLogin: $scope.params.qRequestLogin,requestUrl: $scope.params.qRequestUrl,
                            startTime1:$scope.params.qStartTime1, endTime1:$scope.params.qEndTime1,
                enLogin: user.enLogin, pageNum: $scope.pageInfo.pageNum,pageSize: $scope.pageInfo.pageSize,
            };
            console.log(params);
            commonRequestService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    $scope.pageInfo = value.data.data;

                    setNgCache($state, $scope.params, $scope.pageInfo);
                }
            })
        };

        $scope.init();

    })
    //
/*    .controller('CommonHrTrainDetailDetailController',function ($state,$stateParams,$scope,$rootScope,commonFormDataService,actTaskQueryService){   $scope.init=function(){
            commonRequestService.listRequestName(user.enLogin).then(function (value) {
                $scope.requestNames=value.data.data;
            });

            $scope.queryData();
        }


        $scope.params = getNgParam($state, {
            requestName: "",
            requestLogin: "",
            requestIp:""
        });

        $scope.pageInfo = {pageNum: 1, pageSize: pageSize};

        $scope.queryData = function () {
            $scope.pageInfo.pageNum = 1;
            $scope.params.qRequestName = $scope.params.requestName;
            $scope.params.qRequestLogin = $scope.params.requestLogin;
            $scope.params.qRequestIp = $scope.params.requestIp;
            $scope.loadPagedData();
        };

        $scope.loadPagedData = function () {
            var params = {
                requestName: $scope.params.qRequestName,
                requestLogin: $scope.params.qRequestLogin,
                enLogin: user.enLogin,
                pageNum: $scope.pageInfo.pageNum,
                pageSize: $scope.pageInfo.pageSize
            };
            commonRequestService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    $scope.pageInfo = value.data.data;

                    setNgCache($state, $scope.params, $scope.pageInfo);
                }
            })
        };

        $scope.init();

    })*/
    .controller('CommonHrTrainDetailController',function ($state,$stateParams,$scope,$rootScope,commonFormDataService,actTaskQueryService,commonHrTrainDetailService){
    //        var formDataId=$stateParams.formDataId;

        $scope.init=function() {
            $scope.loadData();
        }

        $scope.loadData=function() {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                $scope.data = value.data.data.data;
                $scope.groupList = value.data.data.groupList;
                $scope.template = value.data.data.template;

                if ($scope.data.processInstanceId) {
                    $scope.loadNewProcessInstance($scope.data.processInstanceId);
                } else {
                    $scope.getTplConfig($scope.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
                commonHrTrainDetailService.listHrTrainMember($scope.data.businessKey).then(function (value){
                    $scope.members=value.data.data;
                })
            })


        }
        $scope.loadDetailData=function() {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                $scope.data = value.data.data.data;
                $scope.groupList = value.data.data.groupList;
                $scope.template = value.data.data.template;

                if ($scope.data.processInstanceId) {
                    $scope.loadNewProcessInstance($scope.data.processInstanceId);
                } else {
                    $scope.getTplConfig($scope.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
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
        $scope.selectUser=function(){

            var selects = "";
            for (var i = 0; i < $scope.members.length; i++) {
                selects = selects+","+ $scope.members[i].userLogin;
            }

            var config={
                title: "请选择培训人员",
                enLogin:user.enLogin,
                multiple: true,
                selects:selects,
                formDataId:"",
                dataSource:"",
            };


            jQuery.showJsTreeSelectUserModal(config, function (selects) {
                var enLoginList = [];
                var cnNameList = [];
                for (var i = 0; i < selects.length; i++) {
                    enLoginList.push(selects[i].id);
                    cnNameList.push(selects[i].text);
                }
                commonHrTrainDetailService.newData($scope.data.businessKey,enLoginList,user.enLogin).then(function (value){
                    $scope.loadData();
                })
                $scope.$apply();
            })
        }

        $scope.removeDetail=function(id){
            commonHrTrainDetailService.removeHrMember(id).then(function (value){
                $scope.loadData();
            })
        }
        $scope.showDetailModel=function(detail){
            $scope.selectDetail = detail;
            $("#detailModal").modal("show");
        }
        $scope.saveDetail=function(){

            commonHrTrainDetailService.save($scope.selectDetail).then(function (value){
                $("#detailModal").modal("hide");
                $scope.loadData();
            })
        }

        $scope.refresh=function(){
            $scope.loadData();
        }

        $scope.init();
    })

    .controller('CommonFormDataDetailController2',function ($state,$stateParams,$scope,$rootScope,commonFormDataService,actTaskQueryService){
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

    .controller('CommonFormDataDetailController',function ($state,$stateParams,$scope,$rootScope,commonFormDataService,actTaskQueryService,commonPrintTableService){
        var formDataId=$stateParams.formDataId;

        $scope.init=function() {
            $scope.loadData();
        }

        $scope.loadData=function() {
            commonFormDataService.getModelById(formDataId, user.enLogin).then(function (value) {
                $scope.data = value.data.data.data;
                $scope.groupList = value.data.data.groupList;
                $scope.template = value.data.data.template;

                if ($scope.data.processInstanceId) {
                    $scope.loadNewProcessInstance($scope.data.processInstanceId);
                } else {
                    $scope.getTplConfig($scope.data.businessKey, user.enLogin);
                }
                $scope.initWebControl();
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
        $scope.print=function () {
            commonPrintTableService.getPrintDate($scope.data.businessKey, user.enLogin).then(function (value) {
                if(value.data.ret){
                    lodop=getLodop();
                    $scope.printData=value.data.data;

                    lodop.PRINT_INIT("");
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
            $scope.loadData();
        }

        $scope.init();
    })

    .controller('CommonEdDwgPictureController',function ($state,$stateParams,$scope,commonEdDwgPictureService){

        $scope.params = {q: "",pageNum: 1, pageSize: $scope.pageSize,total:0};
        $scope.pageInfo = {pageNum:  $scope.params.pageNum, pageSize: $scope.params.pageSize,total:$scope.params.total};

        $scope.init = function () {
            $scope.loadPagedData();

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/edDwgPicture/receiveDwg.do",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.name.indexOf(".dwg") <= 0) {
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 20 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制20MB!');
                        return false;
                    }
                    data.formData = {enLogin: user.enLogin, id: $scope.item.id};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        $scope.item = result.data;
                        $scope.$apply();
                        $scope.loadPagedData();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });

            $("#uploadDwgList").fileupload({
                dataType: 'text',
                url: "/common/edDwgPicture/receiveDwgList.do",
                formData: {enLogin:user.enLogin},
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        $scope.queryData=function(){
            $scope.pageInfo.pageNum = 1;
            $scope.loadPagedData();
        }

        $scope.loadPagedData = function () {
            commonEdDwgPictureService.listPagedData(
                {q:$scope.params.q,pageNum:$scope.pageInfo.pageNum,pageSize:$scope.pageInfo.pageSize,enLogin:user.enLogin}
            ).then(function (value) {
                $scope.pageInfo = value.data.data;
            })
        };



        $scope.add = function () {
            commonEdDwgPictureService.getNewModel(user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.item = value.data.data;
                    ;
                    $("#sysDwgPictureModal").modal("show");
                }
            })
        };

        $scope.update = function () {
            if ($("#detail_form").validate().form()) {
                bootbox.confirm("您确认要保存数据吗?", function (result) {
                    if (result) {
                        commonEdDwgPictureService.update($scope.item).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#sysDwgPictureModal").modal("hide");
                                $scope.loadPagedData();
                            }
                        });
                    }
                });
            }
        }

        $scope.edit = function (id) {
            commonEdDwgPictureService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
                $("#sysDwgPictureModal").modal("show");
            });
        }

        $scope.remove = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    commonEdDwgPictureService.remove(id).then(function () {
                        $scope.loadPagedData();
                    })
                }
            })
        }
        $scope.downloadAttach=function(attachId){
            $scope.commonDownload("/common/attach/download/"+attachId);
        }
        $scope.init();
    })

    .controller('CommonEdDwgStdController',function ($state,$stateParams,$scope,commonEdDwgStdService){

        $scope.params = {q: "",majorName:"",pageNum: 1, pageSize: $scope.pageSize,total:0};
        $scope.pageInfo = {pageNum:  $scope.params.pageNum, pageSize: $scope.params.pageSize,total:$scope.params.total};

        $scope.init = function () {
            $scope.loadPagedData();

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/attach/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.name.indexOf(".dwg") <= 0) {
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 20 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制20MB!');
                        return false;
                    }
                    data.formData = {creator: user.enLogin};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        $scope.item.attachId = result.data;
                        $scope.item.stdName = data.files[0].name;
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        $scope.queryData=function(){
            $scope.pageInfo.pageNum = 1;
            $scope.loadPagedData();
        }

        $scope.loadPagedData = function () {
            commonEdDwgStdService.listPagedData(
                {q:$scope.params.q,pageNum:$scope.pageInfo.pageNum,pageSize:$scope.pageInfo.pageSize,enLogin:user.enLogin,majorName:$scope.params.majorName}
            ).then(function (value) {
                $scope.pageInfo = value.data.data;
            })
        };



        $scope.add = function () {
            commonEdDwgStdService.getNewModel(user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.item = value.data.data;
                    $("#dwgStdModal").modal("show");
                }
            })
        };

        $scope.update = function () {
            if ($("#detail_form").validate().form()) {
                bootbox.confirm("您确认要保存数据吗?", function (result) {
                    if (result) {
                        commonEdDwgStdService.update($scope.item).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#dwgStdModal").modal("hide");
                                $scope.loadPagedData();
                            }
                        });
                    }
                });
            }
        }

        $scope.edit = function (id) {
            commonEdDwgStdService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
                $("#dwgStdModal").modal("show");
            });
        }

        $scope.remove = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    commonEdDwgStdService.remove(id).then(function () {
                        $scope.loadPagedData();
                    })
                }
            })
        }
        $scope.downloadAttach=function(attachId){
            $scope.commonDownload("/common/attach/download/"+attachId);
        }

        $scope.init();
    })

    .controller('CommonEdStampController',function ($state,$stateParams,$scope,commonEdStampService){

        $scope.init = function () {
            $scope.loadData();

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/attach/receive.json",
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.name.indexOf(".png") <= 0) {
                        toastr.error('请上传png格式文件!');
                        return false;
                    }
                    if (file.size > 20 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制20MB!');
                        return false;
                    }
                    data.formData = {creator: user.enLogin};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        $scope.item.attachId = result.data;
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        $scope.loadData = function () {
            commonEdStampService.selectAll(user.appCode).then(function (value) {
                if(value.data.ret){
                    $scope.list=value.data.data;
                }
            })
        };

        $scope.newData = function () {
            commonEdStampService.getNewModel(user.appCode).then(function (value) {
                if (value.data.ret) {
                    $scope.item = value.data.data;
                    $("#itemModal").modal("show");
                }
            })
        };

        $scope.save = function () {
            if ($("#detail_form").validate().form()) {
                bootbox.confirm("您确认要保存数据吗?", function (result) {
                    if (result) {
                        commonEdStampService.save($scope.item).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#itemModal").modal("hide");
                                $scope.loadData();
                            }
                        });
                    }
                });
            }
        }

        $scope.showDetail = function (id) {
            commonEdStampService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
                $("#itemModal").modal("show");
            });
        }

        $scope.remove = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    commonEdStampService.remove(id).then(function () {
                        $scope.loadData();
                    })
                }
            })
        }

        $scope.init();
    })

    .controller('CommonEdLayerExtractionController',function ($state,$stateParams,$scope,commonEdLayerExtractionService){

        $scope.params = {q: "",sourceMajor:"",pageNum: 1, pageSize: $scope.pageSize,total:0};
        $scope.pageInfo = {pageNum:  $scope.params.pageNum, pageSize: $scope.params.pageSize,total:$scope.params.total};

        $scope.init = function () {
            $scope.loadPagedData();
        }

        $scope.queryData=function(){
            $scope.pageInfo.pageNum = 1;
            $scope.loadPagedData();
        }

        $scope.loadPagedData = function () {
            commonEdLayerExtractionService.listPagedData(
                {q:$scope.params.q,pageNum:$scope.pageInfo.pageNum,pageSize:$scope.pageInfo.pageSize,enLogin:user.enLogin,sourceMajor:$scope.params.majorName}
            ).then(function (value) {
                $scope.pageInfo = value.data.data;
            })
        };



        $scope.add = function () {
            commonEdLayerExtractionService.getNewModel(user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.item = value.data.data;
                    $("#dwgExtractionModal").modal("show");
                }
            })
        };

        $scope.update = function () {
            if ($("#detail_form").validate().form()) {
                bootbox.confirm("您确认要保存数据吗?", function (result) {
                    if (result) {
                        commonEdLayerExtractionService.update($scope.item).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#dwgExtractionModal").modal("hide");
                                $scope.loadPagedData();
                            }
                        });
                    }
                });
            }
        }

        $scope.edit = function (id) {
            commonEdLayerExtractionService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
                $("#dwgExtractionModal").modal("show");
            });
        }

        $scope.remove = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    commonEdLayerExtractionService.remove(id).then(function () {
                        $scope.loadPagedData();
                    })
                }
            })
        }
        $scope.downloadAttach=function(attachId){
            $scope.commonDownload("/common/attach/download/"+attachId);
        }

        $scope.init();
    })

    .controller('CommonCadPluginController',function ($state,$stateParams,$scope,commonCadPluginService){

        $scope.params = {q: "",majorName:"",pageNum: 1, pageSize: $scope.pageSize,total:0};
        $scope.pageInfo = {pageNum:  $scope.params.pageNum, pageSize: $scope.params.pageSize,total:$scope.params.total};

        $scope.init = function () {
            $scope.loadPagedData();

            $("#uploadFile").fileupload({
                dataType: 'text',
                url: "/common/attach/receive.json",
                add: function (e, data) {
                    data.formData = {creator: user.enLogin};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        $scope.item.attachId = result.data;
                        $scope.item.zipName = data.files[0].name;
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }

        $scope.queryData=function(){
            $scope.pageInfo.pageNum = 1;
            $scope.loadPagedData();
        }

        $scope.loadPagedData = function () {
            commonCadPluginService.listPagedData(
                {q:$scope.params.q,pageNum:$scope.pageInfo.pageNum,pageSize:$scope.pageInfo.pageSize,enLogin:user.enLogin,majorName:$scope.params.majorName}
            ).then(function (value) {
                $scope.pageInfo = value.data.data;
            })
        };

        $scope.add = function () {
            commonCadPluginService.getNewModel(user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.item = value.data.data;
                    $("#cadPluginModal").modal("show");
                    $scope.loadPagedData();
                }
            })
        };

        $scope.update = function () {
            if ($("#detail_form").validate().form()) {
                bootbox.confirm("您确认要保存数据吗?", function (result) {
                    if (result) {
                        commonCadPluginService.update($scope.item).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $("#cadPluginModal").modal("hide");
                                $scope.loadPagedData();
                            }
                        });
                    }
                });
            }
        }

        $scope.edit = function (id) {
            commonCadPluginService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
                $("#cadPluginModal").modal("show");
            });
        }

        $scope.remove = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    commonCadPluginService.remove(id).then(function () {
                        $scope.loadPagedData();
                    })
                }
            })
        }

        $scope.downloadAttach=function(attachId){
            $scope.commonDownload("/common/attach/download/"+attachId);
        }

        $scope.init();
    })

    .controller('CommonFormController',function ($state,$stateParams,$scope,commonFormService){
        $scope.params = {q: "",pageNum: 1, pageSize: $scope.pageSize};
        $scope.pageInfo = {pageNum:  $scope.params.pageNum, pageSize: $scope.params.pageSize,total:0};


        $scope.init = function () {
            $scope.loadPagedData();
        }

        $scope.queryData=function(){
            $scope.pageInfo.pageNum = 1;
            $scope.loadPagedData();
        }

        $scope.loadPagedData = function () {
            commonFormService.listPagedData({
                    q: $scope.params.q,
                    pageNum: $scope.pageInfo.pageNum,
                    pageSize: $scope.pageInfo.pageSize,
                    tenetId: user.tenetId
                }
            ).then(function (value) {
                $scope.pageInfo = value.data.data;

            })
        }

        $scope.add = function () {
            commonFormService.getNewModel(user.enLogin).then(function (value) {
                if (value.data.ret) {
                    $scope.show( value.data.data);
                }
            })
        };

        $scope.show = function (formId) {
            $state.go("common.formDetail",{formId:formId} )
        }

        $scope.remove = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    commonFormService.remove(id,user.enLogin).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功");
                            $scope.loadPagedData();
                        }
                    })
                }
            })
        }


        $scope.init();
    })

    .controller('CommonFormDetailController',function ($state,$stateParams,$scope,commonFormService){
        var formId = $stateParams.formId;

        $scope.init = function () {
            $scope.loadData();
            $scope.listDetails();
        }

        $scope.loadData = function () {
            commonFormService.getModelById(formId).then(function (value) {
                $scope.item = value.data.data;
            })
        };

        $scope.listDetails=function(){
            commonFormService.listDetail(formId).then(function (value) {
                $scope.listDetail = value.data.data;
            })

        }

        $scope.update = function () {
            if ($("#detail_form").validate().form()) {
                bootbox.confirm("您确认要保存数据吗?", function (result) {
                    if (result) {
                        commonFormService.update($scope.item).then(function (value) {
                            if (value.data.ret) {
                                toastr.success("更新成功!");
                                $scope.loadData();
                            }
                        });
                    }
                });
            }
        }


        $scope.updateDetail = function () {
            if ($("#detailModel_form").validate().form()) {
                bootbox.confirm("您确认要保存数据吗?", function (result) {
                    if (result) {
                        commonFormService.updateDetail($scope.detail).then(function (value) {
                            if (value.data.ret) {
                                $("#detailModel").modal("hide");
                                toastr.success("更新成功!");
                                $scope.listDetails();
                            }
                        });
                    }
                });
            }
        }

        $scope.addDetail=function(){
            commonFormService.getNewModelDetail(formId).then(function (value) {
                $scope.detail=value.data.data;
                $("#detailModel").modal("show");
            })
        }

        $scope.showDetail = function (id) {
          commonFormService.getModelDetailById(id).then(function (value) {
              $scope.detail=value.data.data;
              $("#detailModel").modal("show");
          })
        }

        $scope.removeDetail = function (id) {
            bootbox.confirm("确定要删除数据吗?", function (result) {
                if (result) {
                    commonFormService.removeDetail(id,user.enLogin).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功");
                            $scope.listDetails();
                        }
                    })
                }
            })
        }

        $scope.back=function() {
            window.location.href="/login"
        }

        $scope.init();
    })

    .controller('CommonWxMessageController',function ($state,$stateParams,$scope,commonWxMessageService){

        $scope.params = {userName: "", msgTitle:"", gmtCreate: "",pageNum: 1, pageSize: $scope.pageSize};
        $scope.pageInfo = {pageNum:  $scope.params.pageNum, pageSize: $scope.params.pageSize,total:0};

        $scope.init=function(){
            $scope.queryData();
        }

        $scope.queryData = function () {
            $scope.pageInfo.pageNum = 1;

            $scope.loadPagedData();
        };

        $scope.loadPagedData = function () {
            var params={userName:$scope.params.userName,msgTitle: $scope.params.msgTitle,gmtCreate:$scope.params.gmtCreate,pageNum:$scope.pageInfo.pageNum,pageSize:$scope.pageInfo.pageSize};

            commonWxMessageService.listPagedData(params).then(function (value) {
                $scope.pageInfo=value.data.data;
            });
        };


        $scope.showDetail=function(id){
            commonWxMessageService.getModelById(id).then(function (value) {
                $scope.item=value.data.data;
                $("#itemModal").modal("show");
            })
        }


        $scope.init();

    })