<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>其他功能</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>下载中心</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span>下载中心</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.queryData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                <i class="fa fa-plus"></i> 创建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >
                <label style="margin-left: 20px;">文件名称：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="文件名称"
                                                              ng-model="vm.params.softwareNames"></label>
                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>

        <div class="table-scrollable" >
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 400px;">文件名称</th>
                    <th>文件简介</th>
                    <th style="width: 100px;">大小</th>
                    <th style="width: 80px;">创建者</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 100px;">修改时间</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td>
                        <a href="/sys/attach/download/{{item.attachId}}" class="a_title" target="_blank" ng-bind="item.softwareName" ></a>
                    </td>
                    <td ng-bind="item.softwareDesc"></td>
                    <td ng-bind="item.sizeName"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.gmtModified|date:'yyyy-MM-dd'"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.edit(item.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除"  ng-show="item.creator==user.userLogin&&rightData.selectOpts.indexOf('删除')>=0"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


<div class="modal fade drag" id="softwareModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">文件详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 15px;" id="detail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">文件名称</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.softwareName" required="true"
                                           name="softwareName"
                                           placeholder="">
                                    <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUpload" class="btn blue fileinput-button">
                            <i class="fa fa-cloud-upload" style="height:15px "></i>
                            <span>上传</span>
                            <input type="file" name="multipartFile" id="uploadFile"
                                   accept="*"/>
                                   </span>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">文件简介</label>
                            <div class="col-md-9">
                                <textarea  ng-model="vm.item.softwareDesc" name="softwareDesc"
                                          class="form-control" maxlength="225" rows="1" placeholder="文件的简单介绍" required>
                                </textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">顺序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.item.seq" name="seq" required="true"
                                       placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <textarea id="maxlength_textarea" ng-model="vm.item.remark" name="remark"
                                          class="form-control" maxlength="225" rows="1" placeholder="">
                                </textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建者</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                       disabled="disabled">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">所属部门</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.item.deptName"
                                       name="deptName" disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建时间</label>
                            <div class="col-md-9">
                                <div class="input-icon right">
                                    <i class="fa fa-clock-o"></i>
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">修改时间</label>
                            <div class="col-md-9">
                                <div class="input-icon right">
                                    <i class="fa fa-clock-o"></i>
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtModified |date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.update();" ng-show="vm.item.creator==user.userLogin&&rightData.selectOpts.indexOf('修改')>=0">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

