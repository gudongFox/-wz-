<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">分包/采购合同库</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">分包/采购合同库</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 补录历史合同 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
<%--                            <label style="margin-right: 10px;">关键字：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="项目名称|合同号|项目号"
                                                                           ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>--%>
                            <a class="btn btn-sm green" ng-click="vm.downTempleXml()" target="_blank">
                                <i class="fa fa-cloud-download"></i> 下载模板 </a>
                            <span id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-show="rightData.selectOpts.indexOf('修改')>=0">
                                    <i class="fa fa-cloud-upload"></i>
                                    <span>上传数据</span>
                                    <span id="uploadProgress"></span>
                                    <input type="file" name="multipartFile" id="uploadModelFile"
                                           accept="*"
                                           multiple="multiple"/>
                                 </span>
<%--                            <a class="btn btn-sm green" ng-click="vm.downloadModelExcel('purchase')" target="_blank"><i class="fa fa-cloud-download"></i> 导出采购合同 </a>
                            <a class="btn btn-sm green" ng-click="vm.downloadModelExcel('subpackage')" target="_blank"><i class="fa fa-cloud-download"></i> 导出分包合同 </a>--%>
                            <span style="vertical-align:bottom; margin-left:5px;font-size: 12px;color: red">金额：(万元)</span>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px;">分包/采购合同名称</th>
                                <th>分包/采购合同号</th>
                                <th class="hidden-md hidden-sm">供方名称</th>
                                <th style="width: 160px">发包部门名称</th>
                                <th style="width: 100px">主合同号</th>
                                <th style="width: 100px">分包/采购合同额</th>
                              <%--  <th style="width: 80px">是否对内分包</th>--%>
                                <th style="width: 30px;">标识</th>
                                <th style="width: 30px;">数据来源</th>
                                <th style="width: 60px">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list" on-finish-render>
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="item.subContractNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.supplierName" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.contractNo"></td>
                                <td ng-bind="item.subContractMoney"></td>
                               <%-- <td class="text-center">
                                    <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.inCompany">是</span>
                                    <span class="label label-sm " style=" background-color: #ff8aca;" ng-if="!item.inCompany">否</span>
                                </td>--%>
                                <td class="text-center">
                                    <span class="label label-sm label-success"  ng-if="item.purchase">采购</span>
                                    <span class="label label-sm label-info"  ng-if="!item.purchase">分包</span>
                                </td>
                                <td>
                                    <strong   ng-if="item.subpackageId!=0"><span style="color: blue" >评审录入</span></strong>
                                    <strong   ng-if="item.subpackageId==0&&item.insertType==1"><span style="color: green" >补录合同</span></strong>
                                    <strong   ng-if="item.subpackageId==0&&item.insertType==2"><span style="color: grey" >导入合同</span></strong>
                                </td>
                                </td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
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

<div class="modal fade draggable-modal" id="subpackageModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="portlet  box blue" style="width: 60%; margin-left: 20% ;margin-top: 5%">
        <div class="portlet-title">
            <div class="caption">
                <i class="icon-clock"></i> <span ng-bind="vm.subpackages[0].contractName"></span> &nbsp;&nbsp;&nbsp;&nbsp;分包/采购情况
            </div>
        </div>
        <div class="portlet-body" >
            <div class="tabbable-custom">
                <ul class="nav nav-tabs ">
                    <li class="active">
                        <a href="#tab_15_review" data-toggle="tab" aria-expanded="true">
                            分包/采购 情况
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab_15_review"
                         style="min-height: 400px;">
                        <div class="modal-body" >
                            <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th>分包/采购合同名称</th>
                                        <th class="hidden-md hidden-sm">供方名称</th>
                                        <th>发包部门名称</th>
                                        <th>主合同名称</th>
                                        <th style="width: 100px">主合同金额(万元)</th>
                                        <th style="width: 60px">经办人</th>
                                        <th style="width: 100px;">创建时间</th>

                                        <th style="width: 55px">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.subpackages">
                                        <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                        <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.jumpSubpackage(item.id);"><strong ></strong>
                                        </td>
                                        <td ng-bind="item.supplierName" class="hidden-md hidden-sm"></td>
                                        <td ng-bind="item.deptName"></td>
                                        <td ng-bind="item.contractName"></td>
                                        <td ng-bind="item.contractMoney"></td>
                                        <td ng-bind="item.creatorName"></td>
                                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpSubpackage(item.id);" title="详情"></i>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
