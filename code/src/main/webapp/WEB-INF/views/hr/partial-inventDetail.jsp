<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.employee">人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.employee">个人信息子集</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>发明专利</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span >专利申请</span>
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
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
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">公司内申请单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"    disabled/>
                                </div>

                                <label class="col-md-2 control-label required">公司外合作单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.cooperator" name="cooperator"    disabled/>
                                </div>

                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">申请人顺序</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applyNameSort" name="applyNameSort"    disabled/>
                                </div>
                                <label class="col-md-2 control-label required">发明名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.inventName" name="inventName"    disabled/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">所属专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majorName" name="majorName"   disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" disabled><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;">第一发明人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.firstInventManName" name="firstInventManName"  required="true" disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">其他发明人顺序</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.otherInventMen" name="otherInventMen" required="true"    disabled/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">身份证号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.idNumber" name="idNumber" required="true"    disabled/>
                                </div>

                                <label class="col-md-2 control-label required">日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.applyTime" name="applyTime" required="true" disabled >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" disabled><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">与关键技术或创新点有关保密情</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.securityDec" required="true" name="securityDec" placeholder="" disabled></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label required">关键技术或创新点</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.technologyAndInnovate" required="true" name="technologyAndInnovate" placeholder="" disabled></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">检索查新情况</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.retruevalDec" required="true" name="retruevalDec" placeholder="" disabled></textarea>
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

                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>

            </div>
        </div>
    </div>
</div>



<jsp:include page="../common/common-actFlow.jsp"/>

<jsp:include page="../common/common-edFile2.jsp"/>


