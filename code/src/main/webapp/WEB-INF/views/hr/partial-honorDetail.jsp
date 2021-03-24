<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a>其他功能</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>个人信息</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>个人荣誉</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ng-bind="vm.item.userName"></a>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-diamond"></i>个人荣誉
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
               ng-show="processInstance.passAble">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
                <i class="fa fa-print"></i> 打印 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
               ng-click="vm.showSendFlow();">
                <i class="fa fa-send"></i> 发送 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.backAble"
               ng-click="showBackFlow();">
                <i class="fa fa-reply"></i> 打回 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
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
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">员工姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.userName" disabled
                                           />
                                </div>
                                <label class="col-md-2 control-label">所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" disabled
                                    />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">奖励名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.honorName" ng-disabled="!processInstance.firstTask"  />
                                </div>
                                <label class="col-md-2 control-label">颁奖单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.honorUnits" ng-disabled="!processInstance.firstTask"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">荣誉级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'荣誉级别'}:true"
                                            ng-model="vm.item.honorLevel" class="form-control" ng-disabled="!processInstance.firstTask" ></select>
                                </div>
                                <label class="col-md-2 control-label">奖项类别</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.honorType" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">获奖时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="honorDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.honorDate" name="honorDate" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>


                            </div>
                            <div class="form-group">
                            <label class="col-md-2 control-label">备注</label>
                            <div class="col-md-10">
                                <textarea rows="6" class="form-control" ng-model="vm.item.remark" required="true" name="applyReason" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                            </div>
                        </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../ed/project/part-actFlow.jsp"/>
<jsp:include page="../common/common-edFile.jsp"/>
