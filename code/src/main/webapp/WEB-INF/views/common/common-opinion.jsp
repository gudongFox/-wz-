<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css">
    .span{
        font-size: 16px;
        display:-moz-inline-box;
        display:inline-block;
        width:150px;
    }
</style>

<div class="portlet light"  >
    <div class="portlet-title " style="border-bottom:1px solid #eee;">
        <div class="caption">
            <i class="fa fa-comments"></i>
            <span class="caption-subject" >处理意见</span>
        </div>
    </div>
    <div class="portlet-body ">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group" ng-repeat="task in optionlist" ng-if="task.latestComment!=''">
                    <div class="col-md-12" ng-if="task.comments.length ==1"><%--意见为空时不显示--%>
                        <span class="col-md-2" style="text-align: right" ng-bind="task.name"></span>
                        <span class="col-md-1" ng-bind="task.assigneeName"  style="cursor: pointer;"></span>
                        <img  style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;"  ng-src="{{'/sys/attach/downloadPic/'+task.assignee}}">
                        <span class="" ng-bind="task.latestComment"  style="cursor: pointer;"></span>
                        <span class="" ng-bind="task.endTime|date:'yyyy-MM-dd HH:mm'"></span>
                    </div>
                    <div class="col-md-12" ng-repeat="op in task.comments.slice().reverse()" ng-if="task.comments.length >1">
                        <span class="col-md-4" style="text-align: right" ng-bind="task.name" ng-if="$index+1==1"></span>
                        <span class="col-md-4" style="text-align: right"  ng-if="$index+1>1"></span>
                        <span class="col-md-2" ng-bind="op.cnName"  style="cursor: pointer;"></span>
                        <img  class="col-md-1" style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;"  ng-src="{{'/sys/attach/downloadPic/'+op.userId}}">
                        <div>
                            <span class="" ng-bind="op.message"  style="cursor: pointer;"></span>
                        </div>
                        <div>
                            <span class="" ng-bind="task.endTime|date:'yyyy-MM-dd HH:mm'"></span>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>

</div>

