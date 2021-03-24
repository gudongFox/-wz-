
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="zh-cmn-Hans">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
    <title>中国五洲工程设计集团有限公司</title>
    <link href="/nochange/bootstrap-4.1.0-dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="shortcut icon"   href="/m/wuzhou.ico"/>
</head>
<body>
<div>
    <nav class="navbar navbar-light bg-light">
        <a class="navbar-brand" href="#">
            图纸认证信息
        </a>
    </nav>

    <div class="container">
        <br/>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">项目名称</span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default"  value="${paper.projectName}" />
        </div>
<%--        <div class="input-group mb-3">--%>
<%--            <div class="input-group-prepend">--%>
<%--                <span class="input-group-text">项目负责</span>--%>
<%--            </div>--%>
<%--            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" value="${paper.chargeName}"   />--%>
<%--        </div>--%>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">图纸图名</span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" value="${paper.drawingName}" />
        </div>

        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">图纸图号</span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" value="${paper.drawingNo}"   />
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">图纸校核</span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" value="${paper.proofreadName}"   />
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">图纸审核</span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" value="${paper.auditName}"   />
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">总设计师</span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" value="${paper.chargeName}"   />
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">审核日期</span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" value="${gmtCreate}"   />
        </div>
    </div>
    <nav class="navbar fixed-bottom navbar-light bg-light">
        <a class="navbar-brand" href="#">中国五洲工程设计集团有限公司</a>
    </nav>
</div>
</body>
</html>