<%--
  Created by IntelliJ IDEA.
  User: Roy
  Date: 2019/2/22
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>人资管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>用户详情</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-3" style="padding-right: 0px;">
        <div class="portlet  box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-user"></i> <span ng-bind="'人员信息子集（'+ownerInfo.userName+'）'"></span>
                </div>
                <div class="tools">
                    <i class="fa fa-refresh" title="刷新" ng-click="loadTree();"></i>
                </div>
            </div>
            <div class="portlet-body" style="height: {{contentHeight-150}}px;">
                <div id="detailTree"></div>
            </div>
        </div>
    </div>

    <div class="col-md-9" >
        <div ui-view></div>
    </div>
</div>






