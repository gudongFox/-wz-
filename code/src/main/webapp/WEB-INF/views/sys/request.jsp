
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="sys.code">系统管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>访问日志</span>
        </li>
    </ul>
</div>

<span ng-include="'/web/v1/tpl/request.html'"></span>