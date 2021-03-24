
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计与开发验证记录</title>
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
                            <label class="weui-label">项目名称</label>
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
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">专业</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="majorName" value="${data.majorName}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">设计阶段</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="stageName" value="${data.stageName}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">校对人</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="proofreadManName" value="${data.proofreadManName}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">审核人</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="auditManName" value="${data.auditManName}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>备注</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="remark" placeholder="请输入备注内容" >${data.remark}</textarea>
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