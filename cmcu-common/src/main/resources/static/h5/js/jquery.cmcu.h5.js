(function ($) {
    var defaults = {
        contentFormId:'contentForm',
        contentFormDeptId:'contentFormSelectDept',
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
        $("#"+options.contentFormId).css("display","none");
        $("#"+options.contentFormDeptId).css("display","block");
        $("#"+options.contentFormDeptId+"_title").html(options.title + (options.multiple ? '（多选）' : '（单选）'));
        $("#"+options.contentFormDeptId+"_back").unbind("click").on('click', function () {
            backMain();
        });

        $("#"+options.contentFormDeptId+"_ok").unbind("click").on('click', function () {
            backMain();
            var tree = $("#" + options.treeId);
            var idList = [];
            var nameList = [];
            var chargeList=[];
            var nodes=[];
            for(var i=0;i<treeData.length;i++) {
                var node=tree.jstree().get_node(treeData[i].id);
                var currentIcon=tree.jstree().get_icon(node).toString();
                if(currentIcon.indexOf('custom-selected')>=0) {
                    idList.push(node.id);
                    nameList.push(node.text);
                    nodes.push(node);
                    chargeList=chargeList.concat(node.data.deptChargeMen);
                }
            }
            ;
            closeCallback(nodes,idList.join(','),nameList.join(','),chargeList);
        });

        $("#" + options.treeId).jstree()


        loadData();
    }

    var backMain=function () {
        $("#"+options.contentFormId).css("display","block");
        $("#"+options.contentFormDeptId).css("display","none");
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
            tree.jstree().set_icon(node, originalIcon + " mui-icon mui-icon-checkmarkempty custom-selected");
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

    $.showJsTreeSelectDept = function (config, closeCallback) {
        return init(config, closeCallback);
    }

})(jQuery);

