<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >技术质量</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>科技质量管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >QC提交</a>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> <span >QC提交</span>
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction1.jsp"/>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 350px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">标题</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="标题"
                                           ng-model="vm.item.technologyTitle" ng-disabled="!processInstance.firstTask" required/>
                                </div>

                                <label class="col-md-2 control-label">类别</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="类别"
                                           ng-model="vm.item.technologyType" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName"  name="publishDeptName" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">描述</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                              ng-model="vm.item.technologyDesc" name="technologyDesc" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
                                </div>

                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 350px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 350px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>




