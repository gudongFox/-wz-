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
            <i class="fa fa-file"></i>
            <span class="caption-subject" >处理意见</span>
        </div>
    </div>
    <div class="portlet-body ">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                    <div class="col-md-12" >
                        <div class="col-md-5" style="text-align:center;border-right: 1px red solid">
                            <div style="padding-bottom: 10px" ng-if="optionlistCountSign.length>0"><span style="color: red;font-size: 16px;">会签节点意见</span></div>
                            <div class="form-group" ng-repeat="task in optionlistCountSign" ng-if="task.latestComment!=''" >
                                <div class="col-md-12" ng-if="task.comments.length ==1">
                                    <span class="col-md-4" style="text-align: right" ng-bind="task.name"></span>
                                    <span class="col-md-2" ng-bind="task.assigneeName"  style="cursor: pointer;"></span>
                                    <img  class="col-md-1" style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;"  ng-src="{{'/sys/attach/downloadPic/'+task.assignee}}">
                                    <div>
                                        <span class="" ng-bind="task.latestComment"  style="cursor: pointer;"></span>
                                    </div>
                                    <div>
                                        <span class="" ng-bind="task.endTime|date:'yyyy-MM-dd HH:mm'"></span>
                                    </div>
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
                        <div class="col-md-7" style="text-align:center;border-left: 1px red solid">
                            <div style="padding-bottom: 10px" ng-if="optionlistLeader.length>0"><span style="color: red;font-size: 16px">领导节点意见</span></div>

                            <div class="form-group" ng-repeat="task in optionlistLeader" ng-if="task.latestComment!=''">
                                <div class="col-md-12" ng-if="task.comments.length ==1">
                                    <span class="col-md-4" style="text-align: right" ng-bind="task.name"></span>
                                    <span class="col-md-2" ng-bind="task.assigneeName"  style="cursor: pointer;"></span>
                                    <img  class="col-md-1" style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;"  ng-src="{{'/sys/attach/downloadPic/'+task.assignee}}">
                                    <div>
                                        <span class="" ng-bind="task.latestComment"  style="cursor: pointer;"></span>
                                    </div>
                                    <div>
                                        <span class="" ng-bind="task.endTime|date:'yyyy-MM-dd HH:mm'"></span>
                                    </div>
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
                    </div>
            </div>
        </form>
    </div>

</div>
