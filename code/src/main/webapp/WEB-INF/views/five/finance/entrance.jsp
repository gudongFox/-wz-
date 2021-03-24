<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:62})">财务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>财务报销入口</span>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span>财务报销入口</span>
        </div>
    </div>

    <div class="portlet-body">
        <div class="row justify-content-center">
            <div class="justify-content-center"  style="display: flex; flex-flow: wrap; padding-left: 5px;float: left">
                <div ng-click="vm.init()"  style=" text-align:center;vertical-align:bottom;border-radius:12px !important;box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1);width: 110px;height: 110px;color:#369; margin: 2px 5px 5px 2px;">
                    <img ng-src="/common/attach/download/455" style="width: 72px;height: 72px;margin-top: 10px">
                    <p style=";font-size: 15px;color: #369">财务报销入口</p>
                </div>
            </div>
        </div>

    </div>
</div>

