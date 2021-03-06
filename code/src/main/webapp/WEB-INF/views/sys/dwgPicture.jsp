<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>协同设置</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>图框管理</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span>图框管理</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 创建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-2">
                <input type="text" class="form-control input-sm" placeholder="关键字"
                       ng-model="vm.params.q"/>
            </div>
            <div class="col-md-8">
                <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                <span class="btn btn-sm blue fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>批量上传</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadDwgList"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>
            </div>
        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>图框类别</th>
                    <th>图框尺寸</th>
                    <th>图框加长</th>
                    <th>图框方向</th>
                    <th>默认比例</th>
                    <th style="width: 140px;">创建时间</th>
                    <th style="width: 140px;">修改时间</th>
                    <th style="width: 75px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.pictureName" class="data_title" ng-click="vm.edit(item.id)"></td>
                    <td ng-bind="item.pictureSize"></td>
                    <td ng-bind="item.expandSize"></td>
                    <td ng-bind="item.pictureDirection"></td>
                    <td ng-bind="item.pictureScale"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="item.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.edit(item.id);" title="详情"></i>
                        <i class="fa fa-download margin-right-5" ng-click="download(item.attachId);"
                           ng-show="item.attachId>0"
                           title="下载"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


<div class="modal fade drag" id="sysDwgPictureModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">图框详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">图框类别</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'图框类别'}:true"
                                            ng-model="vm.item.pictureName" class="form-control">
                                    </select>

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
                            <label class="col-md-3 control-label">图框尺寸</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'图框尺寸'}:true"
                                        ng-model="vm.item.pictureSize" class="form-control">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">图框加长</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'图框加长'}:true"
                                        ng-model="vm.item.expandSize" class="form-control">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">图框方向</label>
                            <div class="col-md-9">
                                <select ng-init="directions=['横式','立式'];" ng-options="s for s in directions"
                                        ng-model="vm.item.pictureDirection" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">默认比例</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.item.pictureScale"
                                       name="pictureScale"
                                       placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">顺序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.item.seq" name="seq"
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
                <button type="button" class="btn blue" ng-click="vm.update();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

