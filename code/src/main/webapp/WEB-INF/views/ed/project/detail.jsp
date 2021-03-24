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
            <a ui-sref="dashboard">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="me.edProject">我参与的项目</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <i class=" icon-bulb" title="贯标项目" ng-show="vm.businessContract.ed"></i>
            <span>项目详情</span>
        </li>
    </ul>
</div>
<div class="row">
    <div class="col-md-4 col-lg-3" style="padding-right: 0px;">
        <div class="portlet  box blue">
            <div class="portlet-title">
                <div class="caption" ng-click="vm.projectShow()" title="点击查看项目详情" style="cursor: pointer;">
                    <i class="fa fa-info-circle" ></i> <span ng-bind="vm.businessContract.projectName"></span>
                </div>
                <div class="tools">
                    <i class="fa fa-refresh" title="刷新" ng-click="loadTree();"></i>
                </div>
            </div>
            <div class="portlet-body" style="padding-left: 0px;padding-right: 0px;">
                <div id="taskTree"></div>
            </div>
        </div>
    </div>

    <div class="col-md-8 col-lg-9" >
        <div ui-view></div>
    </div>
</div>




<div class="modal fade" id="sendFlowModal" aria-hidden="true">
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
                                           ng-model="processInstance.myTaskName" disabled="disabled"/>
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
                                    <input type="text" class="form-control" value="{{processInstance.myTaskTime | date:'yyyy-MM-dd HH:mm:ss'}}"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">审核意见<span
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
    </div>
</div>

<div class="modal fade" id="backFlowModal" aria-hidden="true">
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
                                           ng-model="processInstance.myTaskName" disabled="disabled"/>
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
                                <label class="col-md-4 control-label">打回意见
                                    <span style="color: red;margin-left: 2px;">*</span>
                                </label>
                                <div class="col-md-7">
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
    </div>
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
                                <label class="col-md-2 control-label">占地面积</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.floorArea" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">工期</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.constructionTime" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">工程地址</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.projectAddress" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目负责人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model=" vm.businessContractdetail.chargeMenName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">执行负责人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.businessContractdetail.exeChargeMenName" disabled="disabled"/>
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



