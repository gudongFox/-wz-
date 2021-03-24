
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计与开发输入清单及评审记录(部门批文)</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
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
<script src="/assets/js/weui.min.js" type="text/javascript"></script>
</body>
</html>