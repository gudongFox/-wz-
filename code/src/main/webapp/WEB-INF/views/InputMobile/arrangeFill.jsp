
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计与开发策划书提交版</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
   <%-- <link rel="stylesheet" href="https://cdn.bootcss.com/jquery-weui/1.2.1/css/jquery-weui.min.css">--%>
    <style>
        body{
            background-color:#efeff4;
        }
        .weui-cell__bd{
            padding-right: 6px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="weui-panel">
        <div class="weui-panel__hd">基础信息</div>
        <div class="weui-panel__bd">
            <div class="weui-media-box weui-media-box_small-appmsg">
                <div class="weui-cells">
                    <a class="weui-cell weui-cell_access" href="/mobile/flow">
                        <div class="weui-cell__hd">
                            <label class="weui-label">流程</label>
                        </div>
                        <div class="weui-cell__bd">项目负责人(罗冬)</div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">编号</label>
                        </div>
                        <div class="weui-cell__bd">${data.formNo}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">工程名称</label>
                        </div>
                        <div class="weui-cell__bd">${data.projectName}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">合同号</label>
                        </div>
                        <div class="weui-cell__bd">${data.contractNo}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">评审阶段</label>
                        </div>
                        <div class="weui-cell__bd">${data.stageName}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="weui-panel__hd">填写信息</div>
        <div class="weui-panel__bd">
            <div class="weui-media-box weui-media-box_small-appmsg">
                <div class="weui-cells">
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">工程等级</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="projectLevel" value="${data.projectLevel}">
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker1">
                        <div class="weui-cell__hd">
                            <label class="weui-label">工程类别</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="projectType" id="projectType" placeholder="请选择" value="${data.projectType}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">建筑面积</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="designScaleArea" value="${data.designScaleArea}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">建筑层数</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="designScaleFloor" value="${data.designScaleFloor}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">投资额(万元)</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="designScaleInvest" value="${data.designScaleInvest}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">四新要求</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="fourNewRule" value="${data.fourNewRule}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">合作单位</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="cooperationName" value="${data.cooperationName}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">分工及要求</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="cooperationContent" value="${data.cooperationContent}">
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker2">
                        <div class="weui-cell__hd">
                            <label class="weui-label">特殊设计</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="specialValidate" id="specialValidate" placeholder="请选择" value="${data.specialValidate}">
                        </div>
                    </a>
                    <a class="weui-cell weui-cell_access" id="picker3">
                        <div class="weui-cell__hd">
                            <label class="weui-label">设计评审</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="approveArrange" id="approveArrange" placeholder="请选择" value="${data.approveArrange}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">备注</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="remark" value="${data.remark}">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="part-actFileFill.jsp"/>
</div>

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<%--<script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>--%>
<script src="/assets/js/weui.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        $('#picker1').on('click', function () {
            weui.picker([
                {
                    label: '公共建筑',
                    value: '公共建筑'
                },
                {
                    label: '住宅建筑',
                    value: '住宅建筑'
                },
                {
                    label: '工业建筑',
                    value: '工业建筑'
                }
            ], {
                className: 'projectType',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#projectType').val(result);
                },
                id: 'projectType'
            });
        });
    });
    $(function(){
        $('#picker2').on('click', function () {
            weui.picker([
                {
                    label: '不采用',
                    value: '不采用'
                },
                {
                    label: '对比计算',
                    value: '对比计算'
                },
                {
                    label: '设计类比',
                    value: '设计类比'
                },
                {
                    label: '模型试验',
                    value: '模型试验'
                }
            ], {
                className: 'specialValidate',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#specialValidate').val(result);
                },
                id: 'specialValidate'
            });
        });
    });
    $(function(){
        $('#picker3').on('click', function () {
            weui.picker([
                {
                    label: '不需进行设计评审',
                    value: '不需进行设计评审'
                },
                {
                    label: '需要进行设计评审',
                    value: '需要进行设计评审'
                }
            ], {
                className: 'approveArrange',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#approveArrange').val(result);
                },
                id: 'approveArrange'
            });
        });
    });
</script>
</body>
</html>