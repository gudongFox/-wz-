<%--
  Created by IntelliJ IDEA.
  User: luodong
  Date: 2019/11/19
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="/m/admin/pages/css/profile.css" rel="stylesheet"/>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>其他功能</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>我的日程</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet box green-meadow calendar">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-gift"></i>日程
                </div>
            </div>
            <div class="portlet-body">
                <div class="row">
                    <div class="col-md-3 col-sm-12">
                        <div id="external-events">
                            <form class="inline-form">
                                <input type="text" value="" class="form-control" placeholder="请输入常用日程内容" id="event_title"/><br/>
                                <a href="javascript:;" id="event_add" class="btn default">
                                   新增常用日程 </a>
                                <a href="javascript:;"style="float:right" id="addEvent" ng-click="vm.addEvent()" class="btn default">新增日程 </a>
                            </form>
                            <hr/>
                            <div id="event_box">

                            </div>
                            <%--<label for="drop-remove">
                                <input type="checkbox" id="drop-remove"/>拖动后删除</label>
                            <hr class="visible-xs"/>--%>
                        </div>
                        <!-- END DRAGGABLE EVENTS PORTLET-->
                    </div>
                    <div class="col-md-9 col-sm-12">
                        <div id="calendar" class="has-toolbar">
                        </div>
                    </div>
                </div>
                <!-- END CALENDAR PORTLET-->
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="scheduleInformationModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">日程详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">日程内容</label>
                            <div class="col-md-9">
                                <textarea  rows="3" class="form-control" ng-model="vm.schedule.title" ></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">开始时间</label>
                            <div class="col-md-9">
                                <div class="input-group date form_datetime" id="startTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.schedule.start"  required="true" disabled="disabled">
                                    <span class="input-group-btn">
                                            <button class="btn default date-set" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">结束时间</label>
                            <div class="col-md-9">
                                <div class="input-group date form_datetime" id="endTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.schedule.end"    required="true" disabled="disabled">
                                    <span class="input-group-btn">
                                            <button class="btn default date-set" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">日程类型</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'日程类型'}:true"
                                        ng-model="vm.schedule.type" class="form-control" ></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">是否为全天事件</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                        ng-model="vm.schedule.allDay" class="form-control" ></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">是否删除</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                        ng-model="vm.schedule.deleted" class="form-control" ></select>
                            </div>
                        </div>

                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateSchedule(vm.schedule);">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


