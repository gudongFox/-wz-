<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:43})">协会学会审批</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">协会缴费</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">协会缴费</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <%----%>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%> style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">协会名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.associationName" name="associationName"  disabled/>
                                        <span class="input-group-btn" ng-disabled="!processInstance.firstTask">
                                            <button class="btn default" type="button" ng-click="vm.showAssociation();" ng-disabled="!processInstance.firstTask" ><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">协会级别</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.associationLevel" name="associationLevel" required="true"   disabled/>
                                </div>
                            </div>


                            <div class="form-group">
                            <label class="col-md-2 control-label required">申请人</label>
                            <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.handleManName" name="handleManName"  ng-disabled="!processInstance.firstTask" />
                                    <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showUserModel('handleMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                     </span>
                                </div>
                            </div>

                            <label class="col-md-2 control-label required">主管单位名称</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.deptChargeName" name="deptChargeName" required="true"   disabled/>
                            </div>


                        </div>


                        <div class="form-group">

                            <label class="col-md-2 control-label required">公司代表</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.recommendMan" name="recommendMan"    disabled/>
                            </div>
                            <label class="col-md-2 control-label required">公司在协会担任角色</label>
                            <div class="col-md-4">
                                <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'协会角色'}:true"
                                        ng-model="vm.item.associationRole" class="form-control"
                                        disabled></select>
                            </div>

                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label required">联系人</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true"   disabled/>
                            </div>
                            <label class="col-md-2 control-label required">会费</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.associationFee" name="associationFee" required="true"   disabled/>
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">缴费金额（元）</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.paymentMoney" name="paymentMoney" required="true" ng-disabled="!processInstance.firstTask"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">说明</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.remark" name="remark" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                        <label class="col-md-2 control-label">创建人</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                   disabled="disabled"/>
                        </div>
                        <label class="col-md-2 control-label">创建时间</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control"
                                   value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                        </div>
                    </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>



<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="selectAssociationModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">协会列表</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>协会名称</th>
                            <th>协会级别</th>
                            <th>协会类型</th>
                            <th>负责人</th>
                            <th>会费</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="association in vm.listAssociation|filter:{name:vm.qCustomer}" ng-show="association.name!=''">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.associationName==association.associationName" class="cb_association"
                                       data-name="{{association.id}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="association.associationName"></td>
                            <td ng-bind="association.associationLevel"></td>
                            <td ng-bind="association.associationType"></td>
                            <td ng-bind="association.recommendManName"></td>
                            <td ng-bind="association.associationFee"></td>
                            <td ng-bind="association.gmtModified|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>

                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveAssociation();">确认</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../print/print-oaAssociationPaymentDetail.jsp"/>