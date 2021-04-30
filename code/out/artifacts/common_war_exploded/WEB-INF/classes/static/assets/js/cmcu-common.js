function getNgParam($state, defaultParams) {
    var key = $state.current.name + "_" + user.enLogin;
    return getCacheParams(key, defaultParams);
}

function setNgCache($state, params, pageInfo) {
    var key = $state.current.name + "_" + user.enLogin;
    setCacheParams(key, params, pageInfo)
}

function commonWebInit(vm) {
    $("input[type='search']").keypress(function (e) {
        if (e.which === 13) {
            vm.queryData();
            return false;
        }
    });
}

/**
 * 得到缓存的参数
 * @param key
 * @param params
 * @returns {*}
 */
function getCacheParams(key, params) {
    if (window.localStorage.getItem(key) != null) {
        var cacheParams = jQuery.parseJSON(window.localStorage.getItem(key));
        for (var name in params) {
            for (var cacheName in cacheParams) {
                if (cacheName == name) {
                    params[name] = cacheParams[name];
                    break;
                }
            }
        }
    }
    return params;
}


/**
 * 设置缓存的参数
 * @param key
 * @param params
 * @returns {*}
 */
function setCacheParams(key, params, pageInfo) {
    if (pageInfo) {
        params.pageNum = pageInfo.pageNum;
        params.pageSize = pageInfo.pageSize;
        params.total = pageInfo.total;
    }
    ;
    window.localStorage.setItem(key, JSON.stringify(params));
}

(function ($) {
    var defaults = {
        title: "",
        parentDeptId: "0",
        url:'/common/user/listUserJsTreeData.json',
        multiple: false,
        selects: "",
        qualify:"",
        q: ""
    };
    var options;

    var init = function (config, closeCallback) {
        options = $.extend({}, defaults, config);
        if (!options.title) {
            options.title = "选择用户";
        }
        $("#jsTreeUser_title").html(options.title + (options.multiple ? '（多选）' : '（单选）'));
        $("#jsTreeSelectUserModal").modal("show");

        $("#jsTreeUser_key").val(options.q);
        $("#jsTreeUser_key").unbind("keypress").keypress(function (e) {
            if (e.which === 13) {
                options.q = $("#jsTreeUser_key").val();
                loadData();
                return false;
            }
        });
        $("#jsTreeUser_search").unbind("click").on('click', function () {
            options.q = $("#jsTreeUser_key").val();
            loadData();
        });
        $("#jsTreeUser_btn").unbind("click").on('click', function () {
            var selectedUsers = [];
            var selectedNodes = $("#jsTreeUser_tree").jstree().get_selected(true);
            for (var i = 0; i < selectedNodes.length; i++) {
                if (selectedNodes[i].id.indexOf('_') === -1) {
                    selectedUsers.push(selectedNodes[i]);
                }
            }
            $("#jsTreeSelectUserModal").modal("hide");
            var enLoginList = [];
            var cnNameList = [];
            for (var i = 0; i < selectedUsers.length; i++) {
                var selectedUser = selectedUsers[i];
                enLoginList.push(selectedUser.id);
                cnNameList.push(selectedUser.text);
            }
            closeCallback(selectedUsers, enLoginList.join(','), cnNameList.join(','));
        });
        loadData();
    }

    var loadData = function () {
        jQuery.ajax({
            type: "POST",
            url: options.url,
            data: options,
            success: function (data) {
                if (data.ret) {

                    $("#jsTreeUser_tree").jstree("destroy");
                    $("#jsTreeUser_tree").jstree({
                        'core': {
                            'data': data.data,
                            "multiple": options.multiple,
                            "themes": {
                                "responsive": false
                            }
                        },
                        "checkbox": {
                            "keep_selected_style": false,
                            "three_state": options.multiple
                        },
                        "plugins": ["wholerow", "checkbox"]
                    });
                }
            },
            dataType: "json"
        });
    }

    $.showJsTreeSelectUserModal = function (config, closeCallback) {
        return init(config, closeCallback);
    }

})(jQuery);

//部门选择
(function ($) {
    var defaults = {
        title: "选择部门",
        treeRootId:1,
        treeId:"jsTreeDept_tree",
        parentId: -1,
        deptIdScopeList:"",
        url:'/common/user/listDeptJsTreeData.json',
        multiple: false,
        selects: "",
        q: ""
    };
    var options;

    var treeData;

    var init = function (config, closeCallback) {
        options = $.extend({}, defaults, config);
        $("#jsTreeDept_title").html(options.title + (options.multiple ? '（多选）' : '（单选）'));
        $("#jsTreeSelectDeptModal").modal("show");

        $("#jsTreeDept_key").val(options.q);
        $("#jsTreeDept_key").unbind("keypress").keypress(function (e) {
            if (e.which === 13) {
                options.q = $("#jsTreeUser_key").val();
                loadData();
                return false;
            }
        });

        $("#jsTreeDept_search").unbind("click").on('click', function () {
            options.q = $("#jsTreeDept_key").val();
            loadData();
        });

        $("#jsTreeDept_btn").unbind("click").on('click', function () {
            $("#jsTreeSelectDeptModal").modal("hide");
            var tree = $("#" + options.treeId);
            var idList = [];
            var nameList = [];
            var chargeList=[];
            var nodes=[];
            for(var i=0;i<treeData.length;i++) {
                var node = tree.jstree().get_node(treeData[i].id);
                var currentIcon = tree.jstree().get_icon(node).toString();
                if (currentIcon.indexOf('custom-selected') >= 0) {
                    idList.push(node.id);
                    nameList.push(node.text);
                    nodes.push(node);
                    chargeList = chargeList.concat(node.data.deptChargeMen);
                }
            }
            closeCallback(nodes,idList.join(','),nameList.join(','),chargeList);
        });

        $("#" + options.treeId).jstree()

        loadData();
    }

    var loadData = function () {
        jQuery.ajax({
            type: "POST",
            url: options.url,
            data: options,
            success: function (data) {
                if (data.ret) {
                    var tree = $("#jsTreeDept_tree");
                    treeData = data.data;
                    tree.jstree("destroy");
                    tree.jstree({
                        'core': {
                            'data': data.data,
                            "multiple": options.multiple,
                            "themes": {
                                "responsive": false
                            }
                        }
                    });
                    tree.on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        jstreeNodeChanged(node);
                    });
                    tree.on('ready.jstree', function (event, obj) {
                        recursiveCheckNode(options.selects);
                    });
                }
            },
            dataType: "json"
        });
    }

    var jstreeNodeChanged=function (node) {
        var tree = $("#" + options.treeId);
        var selected = true;
        var classIcon = tree.jstree().get_icon(node).toString();
        if (classIcon.indexOf('custom-selected') > 0) {
            selected = false;
        }
        if (options.multiple) {
            if(selected) {
                optTreeNode(node, true);
            }else {
                optTreeNode(node, false);
            }
        } else {
            if (selected) {
                recursiveCheckNode(node.id.toString());
            } else {
                optTreeNode(node, false);
            }
        }
    }

    var recursiveCheckNode=function(selects) {
        var selectArray=selects.split(',');
        for(var i=0;i<treeData.length;i++) {
            var item = treeData[i];
            var selected = jQuery.inArray(item.id.toString(), selectArray)>=0;
            optTreeNode(item, selected);
        }
    }

    var optTreeNode=function(node,select) {
        var tree = $("#" + options.treeId);
        var originalNodeData = getOriginalNodeData(node.id);
        var originalIcon = "";
        if (originalNodeData&&originalNodeData.icon) originalIcon = originalNodeData.icon;
        if (select) {
            tree.jstree().set_icon(node, originalIcon + " fa fa-check font-green custom-selected");
        } else {
            tree.jstree().set_icon(node, originalIcon);
        }
    }

    var getOriginalNodeData=function(id) {
        for(var i=0;i<treeData.length;i++) {
            var item = treeData[i];
            if (item.id === id) {
                return item;
            }
        }
        return undefined;
    }




    $.showJsTreeSelectDeptModal = function (config, closeCallback) {
        return init(config, closeCallback);
    }

})(jQuery);

//数据源tree选择
(function ($) {
    var defaults = {
        title: "选择合适的项",
        treeRootId:1,
        treeId:"jsTreeDataSource_tree",
        parentId: -1,
        deptIdScopeList:"",
        url:'/common/user/listDeptJsTreeData.json',
        multiple: false,
        selects: "",
        q: ""
    };
    var options;

    var treeData;

    var init = function (config, closeCallback) {
        options = $.extend({}, defaults, config);
        $("#jsTreeDataSource_title").html(options.title + (options.multiple ? '（多选）' : '（单选）'));
        var tree = $("#"+options.treeId);
        treeData = config.treeData;


        tree.jstree("destroy");
        tree.jstree({
            'core': {
                'data': treeData,
                "multiple": options.multiple,
                "themes": {
                    "responsive": false
                }
            },
            "plugins": [ "checkbox" ],
            "checkbox" : {
                "keep_selected_style" : false,
                "three_state":false
            }
        });
        // tree.on('changed.jstree', function (e, data) {
        //     var node = data.instance.get_node(data.selected[0]);
        //     jstreeNodeChanged(node);
        // });
        tree.on('ready.jstree', function (event, obj) {
            recursiveCheckNode();
        });
        $("#jsTreeDataSource_btn").unbind("click").on('click', function () {
            $("#jsTreeDataSourceModal").modal("hide");
            var tree = $("#" + options.treeId);
            var codeList = [];
            var nameList = [];
            var nodes=tree.jstree(true).get_selected(true);
            for(var i=0;i<nodes.length;i++) {
                var node = nodes[i];
                codeList.push(node.data.codeValue);
                nameList.push(node.data.name);
            }
            closeCallback(nodes,codeList.join(','),nameList.join(','));
        });

        $("#jsTreeDataSourceModal").modal("show");
    }

    var jstreeNodeChanged=function (node) {
        var tree = $("#" + options.treeId);
        var selected = true;
        var classIcon = tree.jstree().get_icon(node).toString();
        if (classIcon.indexOf('custom-selected') > 0) {
            selected = false;
        }
        if (options.multiple) {
            if(selected) {
                optTreeNode(node, true);
            }else {
                optTreeNode(node, false);
            }
        } else {
            if (selected) {
                recursiveCheckNode(node.id.toString());
            } else {
                optTreeNode(node, false);
            }
        }
    }

    var recursiveCheckNode=function() {
        var selectArray=options.selects.split(',');
        var tree = $("#" + options.treeId);
        for(var i=0;i<treeData.length;i++) {
            var item = treeData[i];
            var selected = jQuery.inArray(item.data.codeValue, selectArray)>=0;
            if(selected) {
                tree.jstree('select_node', item.id, true);
            }
        }
    }

    var optTreeNode=function(node,select) {
        var tree = $("#" + options.treeId);
        var originalNodeData = getOriginalNodeData(node.id);
        var originalIcon = "";
        if (originalNodeData&&originalNodeData.icon) originalIcon = originalNodeData.icon;
        if (select) {
            tree.jstree().set_icon(node, originalIcon + " fa fa-check font-green custom-selected");
        } else {
            tree.jstree().set_icon(node, originalIcon);
        }
    }

    var getOriginalNodeData=function(id) {
        for(var i=0;i<treeData.length;i++) {
            var item = treeData[i];
            if (item.id === id) {
                return item;
            }
        }
        return undefined;
    }



    $.showJsTreeDataSourceModal = function (config, closeCallback) {
        return init(config, closeCallback);
    }

})(jQuery);



(function ($) {
    var defaults = {
        modalId: "_userJsTreeSelectModal_" + new Date().getSeconds(),
        title: "",
        multiple: true,
        selects: "",
        q: ""
    };
    var options;

    var init = function (config, closeCallback) {
        options = $.extend({}, defaults, config);
        ;
        var id = options.modalId;
        var modalDialog = "<div class=\"modal\"style=\"z-index: 111111;\" id=\"" + id + "\" data-backdrop=\"static\">" +
            "  <div class=\"modal-dialog\" role=\"document\">" +
            "    <div class=\"modal-content\">" +
            "      <div class=\"modal-header\">" +
            "        <h4 class=\"modal-title\" id=\"" + id + "_title\">选择职务和角色</h4>" +
            "      </div>" +
            "      <div class=\"modal-body\">" +
            "<div class=\"input-group input-group-sm\">" +
            "<input type=\"text\" class=\"form-control\" placeholder=\"职务/角色\" id=\"" + id + "_key\"/>" +
            "<span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-search\" id=\"" + id + "_search\"></i></span></div>" +
            "        <div id=\"" + id + "_tree\" style=\"margin-top:10px;max-height: 350px;overflow-y: auto;\"></div>" +
            "      </div>" +
            "      <div class=\"modal-footer\">" +
            "        <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">取消</button>" +
            "        <button type=\"button\" class=\"btn btn-primary\" id=\"" + id + "_btn\">确定</button>" +
            "      </div>" +
            "    </div>" +
            "  </div>" +
            "</div>";
        $('body').append(modalDialog);

        $("#" + id + "_key").val(options.q);
        $("#" + id + "_key").keypress(function (e) {
            if (e.which === 13) {
                options.q = $("#" + id + "_key").val();
                loadData();
                return false;
            }
        });
        $("#" + id + "_search").on('click', function () {
            options.q = $("#" + id + "_key").val();
            loadData();
        });
        $("#" + id + "_btn").on('click', function () {
            var modal = $("#" + id);
            var tree = $("#" + id + "_tree")
            var selectedUsers = [];
            var selectedNodes = tree.jstree().get_selected(true);
            for (var i = 0; i < selectedNodes.length; i++) {
                if (selectedNodes[i].id.indexOf('dept_') === -1) {
                    selectedUsers.push(selectedNodes[i]);
                }
            }
            if (selectedUsers.length === 0) {
                var msg = "请选择合适的用户！";
                if (toastr) {
                    toastr.error(msg);
                } else {
                    alert(msg);
                }
                return;
            }
            modal.modal("hide");
            modal.remove();
            if (options.multiple) {
                closeCallback(selectedNodes);
            } else {
                closeCallback(selectedNodes[0]);
            }

        });
        $("#" + id).modal({
            backdrop: false,//点击空白zd处不关闭对专话框
            keyboard: false,//键盘关闭对话框
            show: true//弹出属对话框
        });
        loadData();
    }

    var loadData = function () {

        var tree = $("#" + options.modalId + "_tree")

        jQuery.ajax({
            type: "POST",
            url: '/common/user/listPositionRoleJsTreeData.json',
            data: options,
            success: function (data) {
                if (data.ret) {
                    tree.jstree("destroy");
                    tree.jstree({
                        'core': {
                            'data': data.data,
                            "multiple": options.multiple,
                            "themes": {
                                "responsive": false
                            }
                        },
                        "checkbox": {
                            "keep_selected_style": false,
                            "three_state": options.multiple
                        },
                        "plugins": ["wholerow", "checkbox"]
                    });
                }
            },
            dataType: "json"
        });
    }

    //选择职务和角色
    $.showJsTreeSelectPRModal = function (config, closeCallback) {
        return init(config, closeCallback);
    }

})(jQuery);

//发送打回任务
(function ($) {
    var defaults = {
        send: true,
        taskId: "",
        modalId: "_actSendTaskModal_" + new Date().getSeconds(),
        title: "",
        comment:'处理任务',
        forceClose:false
    };
    var options;
    var task;


    var init = function (config, beforeCall, closeCallback) {

        $(".actTaskModal").each(function () {
            $(this).remove();
        })

        defaults.modalId = "_actTaskModal_" + new Date().getSeconds();
        if (beforeCall()) {

            options = $.extend({}, defaults, config);
            options.comment = options.send ? "已阅，同意。" : "请检查后重新提交!";
            var modalDialog = "<div class=\"modal actTaskModal fade\" id=\"" + options.modalId + "\">\n" +
                "    <div class=\"modal-dialog\">\n" +
                "        <div class=\"modal-content\">\n" +
                "            <div class=\"modal-header\">\n" +
                "                <h4 class=\"modal-title\" id=\"" + options.modalId + "_title\">处理任务</h4>\n" +
                "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "                </button>\n" +
                "            </div>\n" +
                "            <div class=\"modal-body row\">\n" +
                "                <div class=\"col-md-7\">\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>开始时间</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>办理人</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" id=\"" + options.modalId + "_cc_block\" style='display: none;margin-bottom: 0.5rem;'>\n" +
                "                        <label>抄送人</label>\n" +
                "     <div class=\"input-group\" >\n" +
                "            <input type=\"text\" class=\"form-control\" placeholder=\"抄送用户..\" disabled='disabled' id=\"" + options.modalId + "_cc_txt\" />\n" +
                "            <div class=\"input-group-append input-group-addon\" id=\"" + options.modalId + "_cc\"><span class=\"input-group-text\"><i class=\"fa fa-user\" style='color:blue;'></i></span></div>\n" +
                "        </div>" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label style='font-weight: bold;'>办理意见<span style=\"color: #ff0000;\">*</span></label>\n" +
                "                        <textarea rows=\"5\" class=\"form-control form-control-sm\" id=\"" + options.modalId + "_comment\" />\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"col-md-5\">\n" +
                "                    <div id=\"" + options.modalId + "_tree\"></div>\n" +
                "            </div>\n" +
                "            </div>\n" +
                "            <div class=\"modal-footer\">\n" +
                "                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>\n" +
                "                <button type=\"button\" class=\"btn btn-primary \" id=\"" + options.modalId + "_save\">保存意见</button>\n" +
                "                <button type=\"button\" class=\"btn btn-primary \" id=\"" + options.modalId + "_ok\">保存并发送</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n";

            $('body').append(modalDialog);
            $("#" + options.modalId).modal("show");

            if (options.send) {
                $("#" + options.modalId + "_ok").html("保存并发送");
            } else {
                $("#" + options.modalId + "_ok").html("保存并打回");
            }

            $("#" + options.modalId + "_ok").on('click', function () {
                sendTask(true, closeCallback);
            });
            $("#" + options.modalId + "_save").on('click', function () {
                sendTask(false,closeCallback);
            });


            $("#" + options.modalId + "_cc").on('click', function () {
                jQuery.showJsTreeSelectUserModal({
                    multiple: true,
                    selects: task.ccUser,
                    enLogin: user.enLogin
                }, function (selectedUsers, enLoginList, cnNameList) {
                    task.ccUser = enLoginList;
                    task.ccUserName = cnNameList;
                    $("#" + options.modalId + "_cc_txt").val(task.ccUserName);
                });
            });

            $("#" + options.modalId).on('hidden.bs.modal', function () {
                if(!options.forceClose) {
                    var comment = $("#" + options.modalId + "_comment").val();
                    if(comment) {
                        jQuery.ajax({
                            type: "POST",
                            url: '/act/taskHandle/setComment.json',
                            data: {
                                taskId: options.taskId,
                                enLogin: options.enLogin,
                                comment: comment,
                                agent: 'pc'
                            },
                            traditional: true,
                            success: function (data) {
                                if (data.ret) {
                                    $("#comment_"+options.taskId).html(comment);
                                }
                            }
                        });
                    }
                }
            });

            loadCandidate();
            loadTask();
        }
    }


    var sendTask = function (completeTask, closeCallback) {
        var comment = $("#" + options.modalId + "_comment").val();
        var candidateUsers = $("#" + options.modalId + "_tree").jstree().get_selected();
        //if (candidateUsers.length == 1) candidateUsers = [];

        if (!comment) {
            toastr.error("办理意见不能为空!");
            return;
        }
        options.forceClose=true;
        $("#" + options.modalId).modal("hide");
        setTimeout(function () {
            $("#" + options.modalId).remove();
        }, 1000);
        var url = '/act/taskHandle/backTask.json';
        if (options.send) {
            url = '/act/taskHandle/sendTask.json';
        }

        jQuery.ajax({
            type: "POST",
            url: url,
            data: {
                taskId: options.taskId,
                enLogin: options.enLogin,
                comment: comment,
                ccUser: task.ccUser,
                candidateUsers: candidateUsers.join(','),
                completeTask: completeTask
            },
            traditional: true,
            success: function (data) {
                if (data.ret) {
                    if (completeTask) {
                        toastr.success("处理成功");
                        closeCallback(task.processInstanceId,{completeTask:completeTask,processInstanceId:task.processInstanceId});
                    } else {
                        toastr.success("保存成功");
                    }

                } else {
                    toastr.error(data.msg);
                }
            }
        });
    }

    var loadTask = function () {
        jQuery.ajax({
            type: "POST",
            url: '/act/taskQuery/getHistoricTaskInstance.json',
            data: {
                taskId: options.taskId
            },
            success: function (data) {
                if (data.ret) {
                    task = data.data;
                    if (options.send) {
                        $("#" + options.modalId + "_title").html("发送任务(" + task.name + ")");
                    } else {
                        $("#" + options.modalId + "_title").html("打回任务(" + task.name + ")");
                    }
                    $("#" + options.modalId + " .form-group").each(function () {
                        var childs = $(this).children();
                        if (childs.length === 2) {
                            var text = childs[0].innerText;
                            if (text.indexOf("开始时间") >= 0) {
                                $(childs[1]).val(moment(task.startTime).format('YYYY-MM-DD HH:mm:ss'));
                            } else if (text.indexOf("办理人") >= 0) {
                                $(childs[1]).val(task.assigneeName);
                            } else if (text.indexOf("办理意见") >= 0) {
                                if (task.currentComment!=''){
                                    $(childs[1]).val(task.currentComment);
                                }else {
                                    $(childs[1]).val(options.comment);
                                }
                            }
                        }
                    });
                    ;
                    if (task.ccAble) {
                        $("#" + options.modalId + "_cc_block").css("display", "block");
                        $("#" + options.modalId + "_cc_txt").val(task.ccUserName);
                    }
                }
            }
        });
    }

    var loadCandidate = function () {

        var tree = $("#" + options.modalId + "_tree")

        var url = "/act/taskHandle/listBackCandidateTask.json";
        if (options.send) {
            url = "/act/taskHandle/listNextCandidateTask.json";
        }
        jQuery.ajax({
            type: "POST",
            url: url,
            data: {
                taskId: options.taskId
            },
            success: function (data) {
                if (data.ret) {
                    tree.jstree("destroy");
                    tree.jstree({
                        'core': {
                            'data': data.data,
                            "multiple": options.multiple,
                            "themes": {
                                "responsive": false
                            }
                        },
                        "checkbox": {
                            "keep_selected_style": false,
                            "three_state": options.multiple
                        },
                        "plugins": ["wholerow", "checkbox"]
                    });


                    if (!options.send) {

                        tree.on('changed.jstree', function (e, data) {
                            if (data !== undefined && data.node !== undefined && data.action === "select_node") {
                                var selectedNodes = data.instance.get_selected(true);
                                var taskKey = data.node.original.taskKey;
                                $.each(selectedNodes, function (i, nd) {
                                    if (nd.original.taskKey !== taskKey) {
                                        data.instance.uncheck_node(nd);
                                    }
                                });
                            }
                        });

                    } else {
                        tree.on('changed.jstree', function (e, data) {
                            if (data !== undefined && data.node !== undefined && data.action === "deselect_node") {
                                var currentNodeId = data.node.id;
                                if (data.node.parent === "#") {
                                    data.instance.check_node(currentNodeId);
                                    toastr.error("处理人不能为空!")
                                } else {
                                    var children = data.instance.get_node(data.node.parent).children;
                                    if (children.length === 1) {
                                        data.instance.check_node(data.node.parent);
                                        toastr.error("处理人不能为空!")
                                    } else {
                                        for (var i = 0; i < children.length; i++) {
                                            var tempChild = data.instance.get_node(children[i]);
                                            if (tempChild.state.selected) {
                                                return;
                                            }
                                        }
                                        data.instance.check_node(currentNodeId);
                                        toastr.error("处理人不能为空!")
                                    }
                                }
                            }
                        });
                    }
                }
            },
            dataType: "json"
        });
    }

    //选择职务和角色
    $.showActHandleModal = function (config, beforeCall, closeCallback) {
        return init(config, beforeCall, closeCallback);
    }

})(jQuery);

//完成任务resolve
(function ($) {
    var defaults = {
        taskId: "",
        modalId: "_actResolveTaskModal_",
        title: "",
        comment:'任务处理完成'
    };
    var options;
    var task;


    var init = function (config, beforeCall, closeCallback) {
        $(".actTaskModal").each(function () {
            $(this).remove();
        })
        if (beforeCall()) {

            options = $.extend({}, defaults, config);
            var modalDialog = "<div class=\"modal actTaskModal fade\" id=\"" + options.modalId + "\">\n" +
                "    <div class=\"modal-dialog\">\n" +
                "        <div class=\"modal-content\">\n" +
                "            <div class=\"modal-header\">\n" +
                "                <h4 class=\"modal-title\" id=\"" + options.modalId + "_title\">处理任务</h4>\n" +
                "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "                </button>\n" +
                "            </div>\n" +
                "            <div class=\"modal-body\">\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>开始时间</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>办理人</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label style='font-weight: bold;'>办理意见<span style=\"color: #ff0000;\">*</span></label>\n" +
                "                        <textarea rows=\"5\" class=\"form-control form-control-sm\" id=\"" + options.modalId + "_comment\" />\n" +
                "                    </div>\n" +
                "            </div>\n" +
                "            <div class=\"modal-footer\">\n" +
                "                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>\n" +
                "                <button type=\"button\" class=\"btn btn-primary\" id=\"" + options.modalId + "_ok\">完成任务</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n";

            $('body').append(modalDialog);
            $("#" + options.modalId).modal("show");


            $("#" + options.modalId + "_ok").on('click', function () {
                resolveTask(closeCallback);
            });
            loadTask();
        }
    }

    var resolveTask = function (closeCallback) {

        var comment = $("#" + options.modalId + "_comment").val();
        if (!comment) {
            toastr.error("办理意见不能为空!");
            return;
        }
        $("#" + options.modalId).modal("hide");
        var url = '/act/taskHandle/resolveTask.json';

        jQuery.ajax({
            type: "POST",
            url: url,
            data: {
                taskId: options.taskId,
                enLogin: options.enLogin,
                comment: comment
            },
            traditional: true,
            success: function (data) {
                if (data.ret) {
                    toastr.success("操作成功");
                    closeCallback(task.processInstanceId);
                } else {
                    toastr.error(data.msg);
                }
            }
        });
    }

    var loadTask = function () {

        jQuery.ajax({
            type: "POST",
            url: '/act/taskQuery/getHistoricTaskInstance.json',
            data: {
                taskId: options.taskId
            },
            success: function (data) {
                if (data.ret) {
                    task = data.data;
                    $("#" + options.modalId + " .form-group").each(function () {
                        var childs = $(this).children();
                        if (childs.length === 2) {
                            var text = childs[0].innerText;
                            if (text.indexOf("开始时间") >= 0) {
                                $(childs[1]).val(moment(task.startTime).format('YYYY-MM-DD HH:mm:ss'));
                            } else if (text.indexOf("办理人") >= 0) {
                                $(childs[1]).val(task.assigneeName);
                            } else if (text.indexOf("办理意见") >= 0) {
                                if (task.currentComment!=''){
                                    $(childs[1]).val(task.currentComment);
                                }else {
                                    $(childs[1]).val(options.comment);
                                }

                            }
                        }
                    });
                }
            }
        });

    }


    $.showActResolveModal = function (config, beforeCall, closeCallback) {
        return init(config, beforeCall, closeCallback);
    }

})(jQuery);

//委托任务delegate
(function ($) {
    var defaults = {
        taskId: "",
        modalId: "_actDelegateTaskModal_" + new Date().getSeconds(),
        title: "",
        comment:'任务委托其他用户办理'
    };
    var options;
    var task;

    var init = function (config, beforeCall, closeCallback) {
        $(".actTaskModal").each(function () {
            $(this).remove();
        })
        defaults.modalId = "_actDelegateTaskModal_" + new Date().getSeconds();
        if (beforeCall()) {
            options = $.extend({}, defaults, config);
            var modalDialog = "<div class=\"modal actTaskModal fade\" id=\"" + options.modalId + "\">\n" +
                "    <div class=\"modal-dialog\">\n" +
                "        <div class=\"modal-content\">\n" +
                "            <div class=\"modal-header\">\n" +
                "                <h4 class=\"modal-title\" id=\"" + options.modalId + "_title\">委托任务</h4>\n" +
                "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "                </button>\n" +
                "            </div>\n" +
                "            <div class=\"modal-body\">\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>开始时间</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>办理人</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\"  style='margin-bottom: 0.5rem;'>\n" +
                "                        <label>被委托人<span style=\"color: #ff0000;\">*</span></label></label>\n" +
                "     <div class=\"input-group\" >\n" +
                "            <input type=\"text\" class=\"form-control\" placeholder=\"请选择合适的用户..\" disabled='disabled' id=\"" + options.modalId + "_delegate_txt\" />\n" +
                "            <div class=\"input-group-append input-group-addon\" id=\"" + options.modalId + "_delegate\"><span class=\"input-group-text\"><i class=\"fa fa-user\"></i></span></div>\n" +
                "        </div>" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label style='font-weight: bold;'>办理意见<span style=\"color: #ff0000;\">*</span></label>\n" +
                "                        <textarea rows=\"5\" class=\"form-control form-control-sm\" id=\"" + options.modalId + "_comment\" />\n" +
                "                    </div>\n" +
                "            </div>\n" +
                "            <div class=\"modal-footer\">\n" +
                "                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>\n" +
                "                <button type=\"button\" class=\"btn btn-primary\" id=\"" + options.modalId + "_ok\">确认</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n";

            $('body').append(modalDialog);
            $("#" + options.modalId).modal("show");


            $("#" + options.modalId + "_ok").on('click', function () {
                sendTask(closeCallback);
            });

            $("#" + options.modalId + "_delegate").on('click', function () {
                jQuery.showJsTreeSelectUserModal({multiple: false, selects: task.newAssignee,enLogin:user.enLogin}, function (selectedUsers,enLoginList,cnNameList) {
                    task.newAssignee = enLoginList;
                    task.newAssigneeName = cnNameList;
                    $("#" + options.modalId + "_delegate_txt").val(task.newAssigneeName);
                });
            });

            loadTask();
        }
    }


    var sendTask = function (closeCallback) {
        var comment = $("#" + options.modalId + "_comment").val();
        if (!comment) {
            toastr.error("办理意见不能为空!");
            return;
        }
        if (!task.newAssignee) {
            toastr.error("被委托人不能为空!");
            return;
        }
        $("#" + options.modalId).modal("hide");
        var url = '/act/taskHandle/delegateTask.json';
        jQuery.ajax({
            type: "POST",
            url: url,
            data: {
                taskId: options.taskId,
                enLogin: options.enLogin,
                comment: comment,
                newAssignee: task.newAssignee
            },
            traditional: true,
            success: function (data) {
                if (data.ret) {
                    toastr.success("委托成功");
                    closeCallback(task.processInstanceId);
                } else {
                    toastr.error(data.msg);
                }
            }
        });
    }

    var loadTask = function () {
        jQuery.ajax({
            type: "POST",
            url: '/act/taskQuery/getHistoricTaskInstance.json',
            data: {
                taskId: options.taskId
            },
            success: function (data) {
                if (data.ret) {
                    task = data.data;
                    if (!task.currentComment) {
                        task.currentComment = options.send ? "同意!" : "请检查后重新提交!";
                    }
                    $("#" + options.modalId + " .form-group").each(function () {
                        var childs = $(this).children();
                        if (childs.length === 2) {
                            var text = childs[0].innerText;
                            if (text.indexOf("开始时间") >= 0) {
                                $(childs[1]).val(moment(task.startTime).format('YYYY-MM-DD HH:mm:ss'));
                            } else if (text.indexOf("办理人") >= 0) {
                                $(childs[1]).val(task.assigneeName);
                            } else if (text.indexOf("办理意见") >= 0) {
                                if (task.currentComment!=''){
                                    $(childs[1]).val(task.currentComment);
                                }else {
                                    $(childs[1]).val(options.comment);
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    //选择职务和角色
    $.showActDelegateModal = function (config, beforeCall, closeCallback) {
        return init(config, beforeCall, closeCallback);
    }

})(jQuery);

//移交任务transfer
(function ($) {
    var defaults = {
        taskId: "",
        modalId: "_actTransferTaskModal_",
        title: "",
        comment:"任务移交其他用户处理"
    };
    var options;
    var task;


    var init = function (config, beforeCall, closeCallback) {
        $(".actTaskModal").each(function () {
            $(this).remove();
        })
        defaults.modalId = "_actTransferTaskModal_" + new Date().getSeconds();
        if (beforeCall()) {
            options = $.extend({}, defaults, config);
            var modalDialog = "<div class=\"modal actTaskModal fade\" id=\"" + options.modalId + "\">\n" +
                "    <div class=\"modal-dialog\">\n" +
                "        <div class=\"modal-content\">\n" +
                "            <div class=\"modal-header\">\n" +
                "                <h4 class=\"modal-title\" id=\"" + options.modalId + "_title\">移交任务</h4>\n" +
                "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "                </button>\n" +
                "            </div>\n" +
                "            <div class=\"modal-body\">\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>开始时间</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label>办理人</label>\n" +
                "                        <input type=\"text\" class=\"form-control form-control-sm\" disabled='disabled' />\n" +
                "                    </div>\n" +
                "                    <div class=\"form-group\"  style='margin-bottom: 0.5rem;'>\n" +
                "                        <label>新任务人<span style=\"color: #ff0000;\">*</span></label>\n" +
                "     <div class=\"input-group\" >\n" +
                "            <input type=\"text\" class=\"form-control\" placeholder=\"请选择用户\" disabled='disabled' id=\"" + options.modalId + "_transfer_txt\" />\n" +
                "            <div class=\"input-group-append input-group-addon\" id=\"" + options.modalId + "_transfer\"><span class=\"input-group-text\"><i class=\"fa fa-user\"></i></span></div>\n" +
                "        </div>" +
                "                    </div>\n" +
                "                    <div class=\"form-group\" style='margin-bottom:0.5rem;'>\n" +
                "                        <label style='font-weight: bold;'>办理意见<span style=\"color: #ff0000;\">*</span></label>\n" +
                "                        <textarea rows=\"5\" class=\"form-control form-control-sm\" id=\"" + options.modalId + "_comment\" />\n" +
                "                    </div>\n" +
                "            </div>\n" +
                "            <div class=\"modal-footer\">\n" +
                "                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>\n" +
                "                <button type=\"button\" class=\"btn btn-primary\" id=\"" + options.modalId + "_ok\">确认</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n";
            $('body').append(modalDialog);
            $("#" + options.modalId).modal("show");

            $("#" + options.modalId + "_ok").on('click', function () {
                sendTask(closeCallback);
            });

            $("#" + options.modalId + "_transfer").on('click', function () {
                jQuery.showJsTreeSelectUserModal({multiple: false, selects: task.newOwner,enLogin:user.enLogin}, function (selectedUsers,enLoginList,cnNameList) {
                    task.newOwner = enLoginList;
                    task.newOwnerName = cnNameList;
                    $("#" + options.modalId + "_transfer_txt").val(task.newOwnerName);
                });
            });

            loadCandidate();
            loadTask();
        }
    }


    var sendTask = function (closeCallback) {
        var comment = $("#" + options.modalId + "_comment").val();
        if (!comment) {
            toastr.error("办理意见不能为空!");
            return;
        }

        if (!task.newOwner) {
            toastr.error("任务新的拥有人不能为空!");
            return;
        }

        $("#" + options.modalId).modal("hide");
        var url = '/act/taskHandle/transferTask.json';

        jQuery.ajax({
            type: "POST",
            url: url,
            data: {
                taskId: options.taskId,
                enLogin: options.enLogin,
                comment: comment,
                newOwner: task.newOwner
            },
            traditional: true,
            success: function (data) {
                if (data.ret) {
                    toastr.success("移交成功");
                    closeCallback(task.processInstanceId);
                } else {
                    toastr.error(data.msg);
                }
            }
        });
    }

    var loadTask = function () {

        jQuery.ajax({
            type: "POST",
            url: '/act/taskQuery/getHistoricTaskInstance.json',
            data: {
                taskId: options.taskId
            },
            success: function (data) {
                if (data.ret) {
                    task = data.data;
                    $("#" + options.modalId + " .form-group").each(function () {
                        var childs = $(this).children();
                        if (childs.length === 2) {
                            var text = childs[0].innerText;
                            if (text.indexOf("开始时间") >= 0) {
                                $(childs[1]).val(moment(task.startTime).format('YYYY-MM-DD HH:mm:ss'));
                            } else if (text.indexOf("办理人") >= 0) {
                                $(childs[1]).val(task.assigneeName);
                            } else if (text.indexOf("办理意见") >= 0) {
                                if (task.currentComment!=''){
                                    $(childs[1]).val(task.currentComment);
                                }else {
                                    $(childs[1]).val(options.comment);
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    var loadCandidate = function () {

        var tree = $("#" + options.modalId + "_tree")

        var url = "/act/taskHandle/listBackCandidateTask.json";
        if (options.send) {
            url = "/act/taskHandle/listNextCandidateTask.json";
        }
        jQuery.ajax({
            type: "POST",
            url: url,
            data: {
                taskId: options.taskId
            },
            success: function (data) {
                if (data.ret) {
                    tree.jstree("destroy");
                    tree.jstree({
                        'core': {
                            'data': data.data,
                            "multiple": options.multiple,
                            "themes": {
                                "responsive": false
                            }
                        },
                        "checkbox": {
                            "keep_selected_style": false,
                            "three_state": options.multiple
                        },
                        "plugins": ["wholerow", "checkbox"]
                    });


                    if (!options.send) {

                        tree.on('changed.jstree', function (e, data) {
                            if (data !== undefined && data.node !== undefined && data.action === "select_node") {
                                var selectedNodes = data.instance.get_selected(true);
                                var taskKey = data.node.original.taskKey;
                                $.each(selectedNodes, function (i, nd) {
                                    if (nd.original.taskKey !== taskKey) {
                                        data.instance.uncheck_node(nd);
                                    }
                                });
                            }
                        });

                    }


                }
            },
            dataType: "json"
        });
    }

    $.showActTransferModal = function (config, beforeCall, closeCallback) {
        return init(config, beforeCall, closeCallback);
    }

})(jQuery);


