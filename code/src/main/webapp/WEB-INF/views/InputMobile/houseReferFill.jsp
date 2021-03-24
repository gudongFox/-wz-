
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计信息联系单</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
    <style>
        body{
            background-color:#efeff4;
        }
        textarea{
            font-family: Arial;
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
                            <label class="weui-label">创建人</label>
                        </div>
                        <div class="weui-cell__bd">${data.creatorName}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">创建时间</label>
                        </div>
                        <div class="weui-cell__bd"><fmt:formatDate value="${data.gmtCreate }" type="date" pattern="yyyy-MM-dd HH:mm"/></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="weui-panel__hd">填写信息</div>
        <div class="weui-panel__bd">
            <div class="weui-media-box weui-media-box_small-appmsg">
                <div class="weui-cells">
                    <a class="weui-cell weui-cell_access" id="picker1">
                        <div class="weui-cell__hd">
                            <label class="weui-label">提出专业</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="sourceMajor" id="sourceMajor" placeholder="请选择" value="${data.sourceMajor}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <p>提出专业说明</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="referDesc" placeholder="请提出专业说明" >${data.referDesc}</textarea>
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker2">
                        <div class="weui-cell__hd">
                            <label class="weui-label">接收专业</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="destMajor" id="destMajor" placeholder="请选择" value="${data.destMajor}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <p>接收专业处理意见</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="destResult" placeholder="请填写接收专业处理意见" >${data.destResult}</textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="part-actFileFill.jsp"/>
</div>
<script src="/assets/js/weui.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="/assets/js/weui.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        $('#picker1').on('click', function () {
            weui.picker([
                {
                    label: '建筑',
                    value: '建筑'
                },
                {
                    label: '结构',
                    value: '结构'
                },
                {
                    label: '道桥',
                    value: '道桥'
                }
            ], {
                className: 'sourceMajor',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#sourceMajor').val(result);
                },
                id: 'sourceMajor'
            });
        });
    });
    $(function(){
        $('#picker2').on('click', function () {
            weui.picker([
                {
                    label: '建筑',
                    value: '建筑'
                },
                {
                    label: '结构',
                    value: '结构'
                },
                {
                    label: '道桥',
                    value: '道桥'
                }
            ], {
                className: 'destMajor',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#destMajor').val(result);
                },
                id: 'destMajor'
            });
        });
    });
</script>
</body>
</html>