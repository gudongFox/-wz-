<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>经营管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">月度预支绩效工资上报</span>
        </li>
    </ul>
</div>


<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">月度预支绩效工资上报</span>
                </div>
                <%--<div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>--%>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">开始时间：
                                <input type="search" class="form-control input-inline input-sm date-picker" id="startTime"
                                       ng-model="vm.params.startTime1" value="{{vm.params.startTime1|date:'yyyy-MM-dd'}}"></label>
                            <label style="margin-right: 10px;">结束时间：
                                <input type="search"  class="form-control input-inline input-sm date-picker" id="endTime"
                                       ng-model="vm.params.endTime1" value="{{vm.params.endTime1|date:'yyyy-MM-dd'}}"></label>

                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>


                            <%--                            <a class="btn btn-sm green" ng-click="vm.downTempleXml()" target="_blank">
                                                            <i class="fa fa-cloud-download"></i> 下载模板 </a>--%>
                            <%--                            <span id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-show="rightData.selectOpts.indexOf('修改')>=0">
                                                                <i class="fa fa-cloud-upload"></i>
                                                                <span>上传数据</span>
                                                                <span id="uploadProgress"></span>
                                                                <input type="file" name="multipartFile" id="uploadModelFile"
                                                                       data-url="/business/income/receive.json"  type="file" name="file" accept="application/vnd.sealed.xls"
                                                                       multiple="multiple"/>
                                                             </span>--%>

                        </div>
                    </div>
                    <div class="table-scrollable">
                        <%--<table  filter  class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 20px" >报表类型</th>
                                <th style="min-width: 20px" >开始时间</th>
                                <th style="width: 180px" >结束时间</th>
                                <th style="width: 130px">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list" on-finish-render="">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.deptName" ng-click="vm.show(item.id);" class="data_title"><strong ></strong></td>
                                <td ng-bind="item.startTime"></td>
                                <td ng-bind="item.endTime"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>--%>
                        <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 35px;">序号</th>
                                    <th>单位</th>
                                    <th>咨询合同目标</th>
                                    <th>咨询合同完成</th>
                                    <th>去年同期</th>
                                    <th>同比增减</th>
                                    <th>年度指标完成率</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="item in vm.pageInfo.list" on-finish-render="">
                                    <td class="dt-right" ng-bind="$index+1" >
                                    </td>
                                    <td ng-bind="item.deptName"></td>
                                    <td ng-bind="item.target"></td>
                                    <td ng-bind="item.completeCount"></td>
                                    <td ng-bind="item.lastYear"></td>
                                    <td ng-bind="item.increase"></td>
                                    <td ng-bind="item.completeErcent"></td>
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
<%--<div class="row">
    <div class="col-md-3">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> <span ng-bind="tableName">行政职务管理</span>
                </div>
                <div class="tools">
                    <i class="fa fa-plus margin-right-5" title="增加" ng-click="vm.addPosition();"></i>
                    <i class="fa fa-edit" title="编辑" ng-click="vm.show();"></i>
                </div>

            </div>
            <div class="portlet-body">
                <div id="position_tree"></div>
            </div>
        </div>

    </div>

    <div class="col-md-9" style="padding-left: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title tabbable">
                <div class="caption">
                    <i class="fa fa-user"></i> <span ng-bind="vm.params.currentPositionName"></span>
                </div>
                <ul class="nav nav-tabs">
                    <li class="active">
                        <a href="#Position_user_tab" data-toggle="tab" aria-expanded="false">
                            用户列表 </a>
                    </li>
                </ul>
            </div>
            <div class="portlet-body">
                <div class="tab-content">
                    <div class="tab-pane active" id="Position_user_tab">
                        <div class="dataTables_wrapper no-footer">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="margin-right: 10px;">用户名称：<input type="search"
                                                                                   class="form-control input-inline input-sm"
                                                                                   placeholder="用户名称"
                                                                                   ng-model="vm.params.qUserName"></label>
                                    <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i
                                            class="fa fa-search"></i> 查询 </a>
                                    <a class="btn blue btn-sm" ng-click="vm.showUserSelectTree();"><i
                                            class="fa fa-cog"></i> 配置用户 </a>
                                </div>
                            </div>
                            <div class="table-scrollable" style="max-height: {{contentHeight-200}}px;overflow-y: auto;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th style="width: 100px;">姓名</th>
                                        <th style="width: 120px;">登录名</th>
                                        <th>所属部门</th>
                                        <th>系统角色</th>
                                        <th>员工职务</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.pageInfo.list">
                                        <td ng-bind="$index+1"></td>
                                        <td ng-bind="item.userName"></td>
                                        <td ng-bind="item.userLogin"></td>
                                        <td>
                                            <span ng-bind="item.deptName"></span>
                                        </td>
                                        <td>
                                            <span  ng-bind="item.PositionNames" class="data_title" ng-click="vm.showPositionModal(item);"></span>
                                        </td>
                                        <td>
                                            <span ng-bind="item.positionNames"></span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                            <my-pager data-page-info="vm.pageInfo" on-load="vm.loadEmployee()"></my-pager>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>--%>
