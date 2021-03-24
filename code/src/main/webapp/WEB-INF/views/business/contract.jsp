<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="business.contract">经营管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>项目启动</span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-star"></i> 项目启动
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                <i class="fa fa-plus"></i> 创建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-12">
                <label style="margin-right: 10px;">项目名称：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="项目名称"
                                                               ng-model="vm.params.qName"></label>
                <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>

                <a class="btn btn-sm green" href="/assets/doc/导出模板/合同台账.xls" target="_blank"><i class="fa fa-cloud-download"></i> 下载模板 </a>

                <span id="btnUpload" class="btn btn-sm blue fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传数据</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadModelFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>

                <a class="btn btn-sm green" ng-click="vm.downloadModelExcel()" target="_blank"><i class="fa fa-cloud-download"></i> 导出合同 </a>

                <label style="margin-right: 10px;">统计时间：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="2020 2020-01"
                                                               ng-model="vm.params.searchTime"></label>
                <a class="btn green btn-sm defaultBtn" ng-click="vm.showStatisticByUserLogin();"><i class="fa fa-search"></i> 统计 </a>
            </div>

        </div>
        <div class="dataTables_wrapper no-footer">
            <div class="table-scrollable">
                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th>工程名称</th>
                        <th>部门名称</th>
                        <th style="width: 110px;">合同金额（万元）</th>
                        <th>客户名称</th>
                        <th style="width: 80px;">项目类型</th>
                        <th style="width: 100px;">签订日期</th>
                        <th style="width: 160px;">任务状态</th>
                        <th style="width: 75px;">设计管理</th>
                        <th style="width: 60px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.pageInfo.list">
                        <td ng-bind="$index+vm.pageInfo.startRow"></td>
                        <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.deptName"></td>
                        <td ng-bind="item.contractMoney|currency : '￥'"></td>
                        <td ng-bind="item.customerName"></td>
                        <td ng-bind="item.projectType"></td>
                        <td ng-bind="item.signDate"></td>
                        <td ng-bind="item.processName"></td>
                        <td class="text-center">
                            <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.ed" ng-click="vm.showEd(item);">启用</span>
                            <span class="label label-sm label-default" ng-if="!item.ed">关闭</span>
                        </td>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.processInstanceId=='0'||
                            (item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0)"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


<div class="modal fade draggable-modal" id="statisticContract" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">统计信息</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 460px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>年份</th>
                            <th>月份</th>
                            <th>合同数</th>
                            <th>合同额（万元）</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.contractMap">
                            <td ng-bind="$index+1"></td>
                            <td ng-bind="c.year"></td>
                            <td ng-bind="c.month"></td>
                            <td ng-bind="c.contractNum"></td>
                            <td ng-bind="'￥'+c.contractMoney"></td>
                        </tr>
                        </tbody>
                    </table>

                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>


