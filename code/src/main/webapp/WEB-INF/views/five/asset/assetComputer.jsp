<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:86})">资产管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">公司非密计算机及信息化设备台帐</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">公司非密计算机及信息化设备台帐</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;color: green">导出内容根据下列查询条件判断</label>
                           <%-- <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>--%>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.downExcel();"><i class="fa fa-search"></i> 导出EXCL </a>

                            <a class="btn btn-sm green" ng-click="vm.downTempleXml()" target="_blank"><i class="fa fa-cloud-download"></i> 下载模板 </a>

                            <span id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-show="rightData.selectOpts.indexOf('修改')>=0">
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
                        <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 90px;">责任人</th>
                                <th style="width: 90px;">使用人</th>
                                <th>所属部门</th>
                                <th style="width: 100px;">设备编号</th>
                                <th>设备名称</th>
                                <th style="width: 110px;">设备类型</th>
                                <th style="width: 90px;">联网类型</th>
                                <th style="width: 90px;">使用类别</th>
                                <th style="width: 90px;">使用情况</th>
                                <th style="width: 100px;">创建时间</th>
                             <%--   <th style="width: 80px">流程状态</th>--%>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list"  on-finish-render>
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.chargeManName"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="item.useName" ></td>
                                <td ng-bind="item.deptName" ></td>
                                <td ng-bind="item.computerNo"></td>
                                <td ng-bind="item.computerName"></td>
                                <td ng-bind="item.equipmentType"></td>
                                <td ng-bind="item.network"></td>
                                <td ng-bind="item.securityLevel"></td>
                                <td ng-bind="item.useStatus"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                              <%--  <td>
                                    <span ng-bind="item.processName"></span>
                                </td>--%>
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
