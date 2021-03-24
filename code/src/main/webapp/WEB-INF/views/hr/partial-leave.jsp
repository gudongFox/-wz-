<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a>人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <i class="fa fa-home"></i>
            <a>便捷服务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>离职申请</a>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-database"></i> 员工个人离职申请
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                        <i class="fa fa-plus"></i> 创建 </a>
                </div>            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.params.qName"/>
                        </div>
                        <div class="col-md-4">
                            <a class="btn green btn-sm" ng-click="vm.listPagedData();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 100px">姓名</th>
                                <th style="width: 90px">入职时间</th>
                                <th style="width: 90px">申请离职时间</th>
                                <th style="width: 90px">批准离职时间</th>
                                <th >离职原因</th>
                                <th style="width: 90px">创建日期</th>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 50px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.userName" class="data_title" ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.joinCompanyTime"></td>
                                <td ng-bind="item.applyLeaveTime"></td>
                                <td ng-bind="item.approveLeaveTime"></td>
                                <td ng-bind="item.leaveReason"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o " ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin&&!item.processEnd"></i>
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

<%--
<div class="col-md-12">
    <div class="portlet box blue">
        <div class="portlet-title tabbable">
            <div class="caption">
                <i class="fa fa-user"></i> <span ng-bind="vm.currentRoleName"></span>
            </div>
            <ul class="nav nav-tabs">
                <li class="active">
                    <a href="#tab1" data-toggle="tab" aria-expanded="false">
                        办理进度 </a>
                </li>
                <li class="">
                    <a href="#tab2" data-toggle="tab" aria-expanded="false">
                        流程图  </a>
                </li>
            </ul>
        </div>
        <div class="portlet-body">
            <div class="tab-content">
                <div class="tab-pane active" id="tab1">
                    <div class="dataTables_wrapper no-footer">
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 50px;">序号</th>
                                    <th>名称</th>
                                    <th>编码</th>
                                    <th>类型</th>
                                    <th>url</th>
                                    <th>ui-sref</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="item in vm.tasks">
                                    <td class="dt-right" ng-bind="$index+1"></td>
                                    <td ng-bind="item.name"></td>
                                    <td ng-bind="item.code"></td>
                                    <td>
                                        <span ng-if="item.type==1">菜单</span>
                                        <span ng-if="item.type==2">按钮</span>
                                        <span ng-if="item.type==3">其他</span>
                                    </td>
                                    <td ng-bind="item.url"></td>
                                    <td ng-bind="item.uiSref"></td>
                                    <td>
                                        <span class="label label-sm label-success" ng-if="!item.isDeleted"> 有效 </span>
                                        <span class="label label-sm label-default" ng-if="item.isDeleted"> 无效 </span>
                                    </td>
                                    <td>
                                        <i class="fa fa-trash-o mr-5" ng-click="vm.deleteAcl(item.id);" title="删除"></i>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="tab2">

                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="incomeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">Start Dragging Here</h4>
            </div>
            <div class="modal-body">
                Modal body goes here
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">Close</button>
                <button type="button" class="btn blue">Save changes</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div id="income_printArea" class="hidden">
    <div class="print_area">
        <h3 class="print_text-center" style="margin-bottom: 25px;">个人收入证明</h3>
        <p style="text-indent:2em;" class="print_content">兹证明
            <span class="print_span-input" ng-bind="vm.income.userName"></span>（身份证号码：<span ng-bind="vm.income.userIdcard"></span>），系我公司员工，已工作
            <span class="print_span-input" ng-bind="vm.income.workYearNumber">叁</span>年，职务<span ng-bind="vm.income.workPosition"></span>。
            <span class="print_span-input" ng-bind="vm.income.requestIncomeYear"></span>年年收入为<span ng-bind="vm.income.yearIncome"></span>人民币，平均月收入为<span ng-bind="vm.income.monthIncome"></span>人民币。</p>
        <p style="text-indent:2em;margin-top: 50px;margin-bottom: 50px;">特此证明</p>
        <p>单位地址：<span ng-bind="vm.income.companyAddress"></span></p>
        <p>单位电话：<span ng-bind="vm.income.companyTel"></span></p>
        <p>单位联系人：<span ng-bind="vm.income.companyLinkMan"></span></p>
        <p class="print_text-right" style="margin-top: 30px;" ng-bind="vm.income.companyName"></p>
        <p class="print_text-right" style="margin-top: 30px;">2019-03-06</p>
    </div>
</div>--%>
