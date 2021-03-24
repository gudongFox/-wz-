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
            <span>专家校审意见库</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class=" icon-bubbles"></i> <span>专家校审意见库</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 创建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-12">
                <label style="margin-right: 10px;">关键字：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="关键字"
                                                               ng-model="vm.params.q"></label>
                <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                <a class="btn btn-sm green" href="/assets/doc/导出模板/专家校审意见库.xls" target="_blank"><i class="fa fa-cloud-download"></i> 下载模板 </a>
                <span id="btnUpload" class="btn btn-sm blue fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传数据</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadModelFile"
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
                    <th>意见内容</th>
                    <th style="width: 100px;">专业名称</th>
                    <th style="width: 150px;">意见类型</th>
                    <th style="width: 150px;">关键字</th>
                    <th style="width: 50px;">创建人</th>
                    <th style="width: 140px;">创建时间</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.questionText" class="data_title" ng-click="vm.edit(item.id);"></td>
                    <td ng-bind="item.majorName"></td>
                    <td ng-bind="item.questionType"></td>
                    <td ng-bind="item.keyWord"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.edit(item.id);" title="详情"></i>

                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


<div class="modal fade draggable-modal" id="sysEdQuestionModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">数据详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detail_form">
                    <div class="form-body">

                        <div class="form-group">
                            <label class="col-md-3 control-label">专业名称</label>
                            <div class="col-md-9">
                                <select ng-options="s.name as s.name for s in vm.majors"
                                        ng-model="vm.sysEdQuestion.majorName" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">意见类型</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'校审意见类型'}:true"
                                        ng-model="vm.sysEdQuestion.questionType" class="form-control" ></select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">关键字</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysEdQuestion.keyWord" required="true"
                                       maxlength="200" name="keyWord" placeholder="逗号分隔">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">意见内容</label>
                            <div class="col-md-9">
                                <textarea type="text" class="form-control" ng-model="vm.sysEdQuestion.questionText" required="true"
                                          maxlength="200" name="questionText" placeholder="意见内容"></textarea>
                            </div>
                        </div>



                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <textarea id="maxlength_textarea" ng-model="vm.sysEdQuestion.remark" name="remark"  rows="1" class="form-control" maxlength="225" placeholder="">
                                </textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.sysEdQuestion.seq" required="true"
                                       maxlength="200" name="seq" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建人</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysEdQuestion.creatorName" disabled="disabled" placeholder="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">创建时间</label>
                            <div class="col-md-9">
                                <div class="input-icon right">
                                    <i class="fa fa-clock-o"></i>
                                    <input type="text" class="form-control"
                                           value="{{vm.sysEdQuestion.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
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
                                           value="{{vm.sysEdQuestion.gmtModified |date:'yyyy-MM-dd HH:mm'}}"
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
    </div>
</div>

