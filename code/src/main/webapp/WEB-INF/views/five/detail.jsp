<%--
  Created by IntelliJ IDEA.
  User: Roy
  Date: 2019/2/22
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="five.meContract">设计管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>质量管理详情</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-3" style="padding-right: 0px;">
        <div class="portlet  box blue">
            <div class="portlet-title">
                <div class="caption" ng-click="vm.projectShow()" title="点击查看项目详情" style="cursor: pointer;">
                    <i class="fa fa-tree"></i> <span ng-bind="vm.businessContract.projectName"></span>
                </div>
                <div class="tools">
                    <i class="fa fa-refresh" title="刷新" ng-click="loadTree();"></i>
                </div>
            </div>
            <div class="portlet-body">
                <div id="taskTree"></div>
            </div>
        </div>
    </div>

    <div class="col-md-9" >
        <div ui-view></div>
    </div>
</div>

<div class="modal fade" id="sendFlowModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">发送任务</h4>
            </div>
            <div class="modal-body row">
                <div class="col-md-7">
                    <form class="form-horizontal" role="form" id="sendFlowForm">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-4 control-label">当前节点</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="当前节点"
                                           ng-model="processInstance.myRunningTaskName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">办理人</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="办理人"
                                           ng-model="processInstance.userName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">指派时间</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" value="{{processInstance.myTaskTime | date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">处理意见<span
                                        style="color: red;margin-left: 2px;">*</span></label>
                                <div class="col-md-7">
                                    <textarea rows="3" class="form-control" placeholder="您的审核意见" name="flowComment" id="sendFlowComment"
                                              ng-model="flowComment" required="true" maxlength="50"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-5" style="border: 1px;">
                    <div id="sendFlowTree"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="sendFlow();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade" id="backFlowModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">打回任务</h4>
            </div>
            <div class="modal-body row">
                <div class="col-md-7">
                    <form class="form-horizontal" role="form" id="backFlowForm">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-4 control-label">当前节点</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="当前节点"
                                           ng-model="processInstance.myRunningTaskName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">办理人</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="办理人"
                                           ng-model="processInstance.userName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">指派时间</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" value="{{processInstance.myTaskTime | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">处理意见
                                    <span style="color: red;margin-left: 2px;">*</span>
                                </label>
                                <div class="col-md-8">
                                    <textarea class="form-control" ng-model="flowComment" name="flowComment" placeholder="您的打回意见" id="backFlowComment"
                                              required="true" maxlength="20"></textarea>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-5">
                    <div id="backFlowTree"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="backFlow();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>








<div class="modal fade draggable-modal" id="projectMessageModel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">项目信息</h4>
            </div>
            <div class="modal-body row">
                <div class="col-md-12">
                    <form class="form-horizontal" role="form" id="projectMessageForm">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">工程名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.projectName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">项目类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.contractType" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">执行所室</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.deptName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合作部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.deptNames" disabled="disabled"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">合同额(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.contractMoney" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">投资额(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.investMoney" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">客户名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.customerName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.linkMan" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">工程地址</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.projectAddress" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目总师</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.businessContractDetail.totalDesignerName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">主管院长/所长</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.chargeMenName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目经理</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.exeChargeMenName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">审图级别</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.businessContractDetail.reviewType" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>








