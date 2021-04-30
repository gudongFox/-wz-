<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css">
    .span {
        font-size: 16px;
        display: -moz-inline-box;
        display: inline-block;
        width: 150px;
    }
    .form-group.form-md-line-input {
        position: relative;
        margin: 0 0 10px 0 ;
        padding-top: 12px;
    }

</style>

<div class="portlet light">
    <div class="portlet-title " style="border-bottom:1px solid #eee;">
        <div class="caption">
            <i class="fa fa-file"></i>
            <span class="caption-subject">处理意见</span>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6 ">
            <div class="portlet light bordered">
                <div class="portlet-body form">
                    <form role="form">
                        <div class="form-body">
                            <div class="form-group" style=" text-align: center;">
                                <span style="  color: red;font-size: 16px">会签节点意见</span>
                            </div>
                            <div class="form-group form-md-line-input has-danger " ng-repeat="task in optionlistCountSign" ng-if="task.latestComment!=''">
                                <div class="input-group left-addon right-addon" >
                                    <span class="input-group-addon" style="width: 201px;vertical-align: middle">
                                           <span style="width: 42px;display:inline-block;"  ng-bind="task.assigneeName">李宝林</span>
                                           <img style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;" ng-src="{{'/sys/attach/downloadPic/'+task.assignee}}">
                                    </span>
                                    <textarea type="text" rows ="{{task.rows}} " style="font-size: 14px;color: black" class="form-control edited" ng-bind="task.latestComment" disabled="true"></textarea>
                                    <span class="input-group-addon" style="color: black" >
                                         <span ng-bind="task.endTime|date:'yyyy-MM-dd '" style="vertical-align:middle;display: block;"></span>
                                        <span ng-bind="task.endTime|date:'HH:mm'" style="vertical-align:middle;display: block;"></span>
                                    </span>
                                    <label  ng-bind="task.name" style="color: black;"></label>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-6 ">
            <div class="portlet light bordered">
                <div class="portlet-body form">
                    <form role="form">
                        <div class="form-body">
                            <div class="form-group" style=" text-align: center;">
                                <span style="  color: red;font-size: 16px">领导节点意见</span>
                            </div>
                            <div class="form-group form-md-line-input has-error  " ng-repeat="task in optionlistLeader" ng-if="task.latestComment!=''">
                                <div class="input-group left-addon right-addon">
                                    <span class="input-group-addon">
                                           <span style="width: 42px;display:inline-block; color: black"  ng-bind="task.assigneeName">李宝林</span>
                                           <img style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;" ng-src="{{'/sys/attach/downloadPic/'+task.assignee}}">
                                    </span>
                                    <textarea rows="{{task.rows}}" style="font-size: 14px; color: black" type="text" class="form-control edited" ng-bind="task.latestComment" disabled="true">同意</textarea>
                                    <span class="input-group-addon" style="color: black;" >
                                        <span ng-bind="task.endTime|date:'yyyy-MM-dd '" style="vertical-align:middle;display: block;"></span>
                                        <span ng-bind="task.endTime|date:'HH:mm'" style="vertical-align:middle;display: block;"></span>
                                    </span>
                                    <label class="form_control" style="color: black;" ng-bind="task.name"></label>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
