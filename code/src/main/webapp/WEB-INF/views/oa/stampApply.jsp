
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:56})">用印管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>用印申请</a>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 用印申请<span ng-bind=" vm.technologyType">s</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showStamp();">
                <i class="fa fa-plus"></i> 新建</a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >
                <label style="margin-left: 20px;">标题：<input type="search"
                                                            class="form-control input-inline input-sm"
                                                            placeholder="标题"
                                                            ng-model="vm.params.fileNames"></label>

                <label style="margin-left: 20px;margin-right: 20px;"> 用印类别：
                    <select class="form-control input-inline input-sm"  ng-model="vm.params.types">
                        <option value="公司章,法人章,法人签名">全部</option>
                        <option value="公司章">公司章</option>
                        <option value="法人章">法人章</option>
                        <option value="法人签名">法人签名</option>
                    </select>
                </label>

                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>标题</th>
                    <th>类别</th>
                    <th>所属部门</th>
                    <th style="width: 50px;">创建者</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 130px;">任务状态</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.fileName"  class="data_title" ng-click="vm.show(item.id);"></td>
                    <td ng-bind="item.stampName"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.processName"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

<div class="modal fade" id="stampModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择用印类型</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="stamp in vm.stampList">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_stamp"
                           ng-checked="vm.item.stampName.indexOf(stamp.name)>=0"
                           data-name="{{stamp.name}}" data-id="{{'file_'+id}}" /> <span ng-bind="stamp.name"
                                                                                        class="margin-right-10"
                                                                                        style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%4==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveStamp();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


