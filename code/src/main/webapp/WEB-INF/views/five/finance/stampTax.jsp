<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">财务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>印花税申报</span>
        </li>
    </ul>
</div>


<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> 印花税申报
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">关键字：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="项目名称|项目号"
                                                                           ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                            <a class="btn btn-sm green" ng-click="vm.downData()" target="_blank">
                                <i class="fa fa-cloud-download"></i> 导出数据</a>

                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>合同名称</th>
                                <th style="width: 120px;">合同号</th>
                                <th>对方单位</th>
                                <th style="width: 120px;">签订时间</th>
                                <th>合同金额</th>
                                <th style="width: 80px;">税目</th>
                                <th style="width: 30px;">税率(%)</th>
                                <th >印花税额</th>
                                <th style="width: 30px;">印花税状态</th>
                                <th style="width: 160px;">合同所属部门</th>
                               <%-- <th style="width: 60px">创建人</th>--%>
                              <%--  <th style="width: 100px;">创建时间</th>--%>
                               <%-- <th style="width: 150px">流程状态</th>--%>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.contractName" class="data_title"  ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.contractNo"></td>
                                <td ng-bind="item.customerName"></td>
                                <td ng-bind="item.signTime"></td>
                                <td ng-bind="item.contractMoney"></td>
                                <td ng-bind="item.taxType"></td>
                                <td ng-bind="item.taxNum"></td>
                                <td ng-bind="item.stampTaxMoney"></td>
                                  <td class="text-center" ng-if="rightData.selectOpts.indexOf('修改')>=0" ng-click="vm.changeOpenStamp(item.id)">
                                    <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.openStamp">已开票</span>
                                    <span class="label label-sm label-default" ng-if="!item.openStamp">未开票</span>
                                </td>
                                <td ng-bind="item.contractDeptName"></td>
                              <%--  <td ng-bind="item.creatorName"></td>--%>
                              <%--  <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>--%>
                               <%-- <td><span ng-bind="item.processName"></span></td>--%>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5"  ng-click="vm.remove(item.id);" title="删除"
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
