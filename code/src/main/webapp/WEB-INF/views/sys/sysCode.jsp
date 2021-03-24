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
            <span>数据字典</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span>数据字典</span>
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

                <label>关键字：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="文件名称"
                                                              ng-model="vm.params.qName"></label>
                <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
            </div>
        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 45px;">#</th>
                    <th>名字</th>
                    <th>编码</th>
                    <th>类别</th>
                    <th style="width: 80px">数据类型</th>
                    <th style="width: 60px">是否默认</th>
                    <th style="width: 140px;">修改时间</th>
                    <th style="width: 60px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td  ng-bind="item.name" class="data_title" ng-click="vm.edit(item.id);" title="详情"></td>
                    <td ng-bind="item.code"></td>
                    <td ng-bind="item.codeCatalog"></td>
                    <td ng-bind="item.codeType"></td>
                    <td >
                        <span ng-if="item.defaulted">是</span>
                        <span ng-if="!item.defaulted">否</span>
                    </td>
                    <td ng-bind="item.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
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


<div class="modal fade" id="sysCodeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">字典详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detail_form">
                    <div class="form-body">

                        <div class="form-group">
                            <label class="col-md-3 control-label required">名字</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.item.name" required="true"
                                       maxlength="200" name="name" placeholder="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label required">编码</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.item.code" required="true"
                                       maxlength="200" name="code" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">类别</label>
                            <div class="col-md-9">
                                <select ng-options="s for s in vm.codeCatalogs"
                                        ng-model="vm.item.codeCatalog" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">数据类型</label>
                            <div class="col-md-9">
                                <select ng-model="vm.item.codeType" name="codeType" class="form-control" >
                                    <option value="String" >String</option>
                                    <option value="Integer" >Integer</option>
                                    <option value="Long" >Long</option>
                                    <option value="Boolean" >Boolean</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">顺序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.item.seq" required name="seq" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">是否默认</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否默认'}:true"
                                        ng-model="vm.item.defaulted" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <textarea id="maxlength_textarea" ng-model="vm.item.remark" name="remark"  class="form-control" maxlength="225" rows="1" placeholder="">
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

