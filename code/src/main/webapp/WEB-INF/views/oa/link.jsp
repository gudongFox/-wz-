
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>友情链接</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span>友情链接</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="vm.edit">
                <i class="fa fa-plus"></i> 新建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row justify-content-center">
            <div ng-if="vm.edit"  ng-repeat="item in vm.pageInfo.list">

                <div class="justify-content-center"  style="display: flex; flex-flow: wrap; padding-left: 5px;float: left">
                    <div ng-click="vm.showModal(item)"  style=" text-align:center;vertical-align:bottom;border-radius:12px !important;box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1);width: 110px;height: 110px;color:#369; margin: 2px 5px 5px 2px;">
                        <%-- <img ng-src="/common/attach/download/1078" style="width: 80px;height: 60px;margin-top: 10px">--%>
                        <%--<img src="/m/wuzhoulogo80x80.png" style="width: 80px;height: 60px;margin-top: 10px">--%>
                        <img ng-src="/common/attach/download/{{item.linkLogo}}" style="width: 72px;height: 72px;margin-top: 10px">
                        <p style=";font-size: 15px;color: #369">{{item.linkName}}</p>
                    </div>
                </div>
            </div>
            <div ng-if="!vm.edit"  ng-repeat="item in vm.pageInfo.list">

                <div class="justify-content-center"  style="display: flex; flex-flow: wrap; padding-left: 5px;float: left">
                    <div ng-click="vm.openLink(item)"  style=" text-align:center;vertical-align:bottom;border-radius:12px !important;box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1);width: 110px;height: 110px;color:#369; margin: 2px 5px 5px 2px;">
                        <%-- <img ng-src="/common/attach/download/1078" style="width: 80px;height: 60px;margin-top: 10px">--%>
                        <%--<img src="/m/wuzhoulogo80x80.png" style="width: 80px;height: 60px;margin-top: 10px">--%>
                        <img ng-src="/common/attach/download/{{item.linkLogo}}" style="width: 72px;height: 72px;margin-top: 10px">
                        <p style=";font-size: 15px;color: #369">{{item.linkName}}</p>
                    </div>
                </div>
            </div>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>
<div class="modal fade" id="detailModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">链接详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-group">
                        <label class="col-md-3 control-label required">链接Logo图标</label>
                        <div class="col-md-9">
                            <div class="input-group">
                                <input id="fileName" type="text" class="form-control"
                                       required="true"
                                       maxlength="20" placeholder="请上传链接图表，以80x60为最佳" readonly>
                                <span class="input-group-addon" style="padding: 0;border: none;" readonly="">
                                        <span id="btnUpload" class="btn btn-sm blue fileinput-button" >
                                        <i class="fa fa-cloud-upload"></i>
                                        <span>上传</span>
                                        <span id="uploadProgress"></span>
                                        <input type="file" name="file" id="uploadModelFile"
                                               accept="*"
                                               multiple="multiple"/>
                                         </span>
                                    </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">链接名称</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.currentLink.linkName"  required="true"
                                   placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">链接ULR地址</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.currentLink.linkUrl"  required="true"
                                   placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">是否有效</label>
                        <div class="col-md-9">
                            <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('是否发布')}:true"
                                    ng-model="vm.currentLink.visible" class="form-control" required="true">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">创建人</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.currentLink.creatorName"
                                   placeholder="" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">修改时间</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.currentLink.shownDate"
                                    readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">备注</label>
                        <div class="col-md-9">
                            <textarea type="text" class="form-control" ng-model="vm.currentLink.remark"
                                      name="remark" placeholder=""></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateItem();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


