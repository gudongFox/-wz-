<%--
  Created by IntelliJ IDEA.
  User: luodong
  Date: 2019/7/15
  Time: 18:52
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
            <a ui-sref="sys.role">系统设置</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>系统参数设置</span>
        </li>
    </ul>
</div>
<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class=" icon-settings"></i> 系统参数设置
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.update();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.clearCache1();" >
                <i class="glyphicon glyphicon-refresh "></i> 缓存清理 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form" id="detail_form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label">平台代码</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.appCode"/>
                    </div>
                    <label class="col-md-2 control-label">密码</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.defaultPwd"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">存盘路径</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.dirPath"/>
                    </div>
                    <label class="col-md-2 control-label">默认网址</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.defaultUrl"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label">公司名称</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.companyName"/>
                    </div>
                    <label class="col-md-2 control-label">平台名称</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.appName"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label">公司联系人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.companyLinkMan"/>
                    </div>
                    <label class="col-md-2 control-label">公司联系电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.companyTel"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">企业微信ID</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.wxCorpId"/>
                    </div>
                    <label class="col-md-2 control-label">企业微信密钥 </label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.wxCorpSecret"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">企业微信应用ID</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.wxAgentId"/>
                    </div>
                    <label class="col-md-2 control-label">公司地址</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.companyAddress"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">创建时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                    <label class="col-md-2 control-label">修改时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>


