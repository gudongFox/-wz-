<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">技术质量</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span >设计质量剖析</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span>设计质量剖析</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>
          <%--          <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" >
                        <i class="fa fa-plus"></i> 创建 </a>--%>
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">项目名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="项目名称"
                                                                           ng-model="vm.params.qName"></label>

                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>

                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th style="width: 110px">阶段</th>
                                <th >分期名称</th>
                                <th >质量部负责人</th>
                                <th style="width: 60px">创建人</th>
                                <th style="width: 110px;">创建时间</th>
                                <th style="width: 190px;">任务状态</th>
                                <th style="width: 50px;">操作</th>
                            </tr>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td ng-bind="$index+1"></td>
                                <td ng-bind="item.projectName"  class="data_title" ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.stageName"></td>
                                <td ng-bind="item.stepName"></td>
                                <td ng-bind="item.qualityDepartmentMenName" ></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                                <td ng-bind="item.processName"></td>

                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
                </div>
            </div>
        </div>
    </div>

</div>
