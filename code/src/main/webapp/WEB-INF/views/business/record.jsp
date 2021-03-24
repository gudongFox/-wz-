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
            <span>项目信息备案</span>
        </li>
    </ul>
</div>


<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> 项目信息备案
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>
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
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>

                            <a class="btn btn-sm green" href="/assets/doc/导出模板/项目信息备案.xls" target="_blank"><i class="fa fa-cloud-download"></i> 下载模板 </a>

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
                                <th>项目名称</th>
                                <th class="hidden-md hidden-sm">客户名称</th>
                                <th>部门名称</th>
                                <th style="width: 100px" title="业务内容">业务内容</th>
                                <th style="width: 100px" title="项目类型">项目类型</th>
                                <th style="width: 60px">经办人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 130px">流程状态</th>
                                <th style="width: 80px;">中标结果</th>
                                <th style="width: 75px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="item.customerName" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.businessContent"></td>
                                <td ng-bind="item.projectType"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <span ng-show="item.processEnd" ng-bind="item.win?'已中标':'未中标'"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                                    <i class="fa fa-plus" ng-click=" vm.addApply(item.id);"
                                       ng-show="item.creator==user.userLogin" title="创建投标报名申请表" ></i>
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
