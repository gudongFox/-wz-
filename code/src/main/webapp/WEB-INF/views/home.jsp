<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .cssFont{
        font-size: 16px;
        width: calc(100% - 120px);
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .cssTask
    {
        font-size: 16px;
    }
    .cssDate{

        min-width: 100px;
        font-size: 16px;
    }
    .cssCaptionFont{
        font-size: 18px;
    }
    .cssShadow {
        box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1);
    }
</style>
<div class="row justify-content-center" ng-controller="RedHeaderController">
    <div class="col-md-6 mb-5 " >
        <div class="portlet light cssShadow ">
            <div class="portlet-title">
                <div class="cssCaptionFont caption">
                    <i class="icon-share font-blue-steel"></i>
                    <span class="caption-subject font-blue-steel bold uppercase">企业动态</span>
                </div>
                <div class="actions">
                    <div class="btn-group">
                        <a class="cssCaptionFont btn btn-sm btn-default btn-circle" href="javascript:;" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                            过滤 <i class="fa fa-angle-down"></i>
                        </a>
                        <div class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">
                            <label ng-repeat="value in vm.noticeTypes">
                                <div class="checker"><span class="checked" id="{{value}}" ng-click="vm.changeClass(value);" ><input type="checkbox"   data-name="value" ></span></div> <span ng-bind="value"></span>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="portlet-body">
                <div class="slimScrollDiv" style=" height: 300px;">
                    <div class="scroller" >
                        <ul class="feeds">
                            <li ng-repeat=" notice in vm.noticeList">
                                <a href="javascript:;">
                                    <div class="col1" >
                                        <div class="cssFont desc" style="line-height: 2;vertical-align:bottom"  >
                                            <span ng-bind="notice.noticeTitle" ng-click="vm.showNoticeDetail(notice.id,notice.attachId,notice.businessKey)"></span>
                                            <span ng-show="notice.top"  class="label label-sm label-danger ">置顶</span>
                                            <span ng-show="notice.latest" class="label label-sm label-danger"> 新 </span>
                                        </div>
                                    </div>
                                    <div class="col2">
                                        <div class="cssDate date" ng-bind="notice.gmtCreate|date:'yyyy/MM/dd'"></div>
                                    </div>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="scroller-footer">
                    <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('企业动态')">
                        <a href="javascript:;" >更多</a>
                        <i class="icon-arrow-right"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-6 mb-5">
        <div id="myCarousel" class="carousel slide">
            <!-- 轮播（Carousel）指标 -->
           <%-- <ol class="carousel-indicators">
                <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                <li data-target="#myCarousel" data-slide-to="1"></li>
                <li data-target="#myCarousel" data-slide-to="2"></li>
                <li data-target="#myCarousel" data-slide-to="3"></li>
                <li data-target="#myCarousel" data-slide-to="4"></li>
            </ol>--%>
            <ol class="carousel-indicators" >
                <li ng-repeat="newphoto in vm.newsPhotoList" data-target="#myCarousel" data-slide-to="{{$index}}" ng-class="$index==0?'active':''"></li>
            </ol>
            <!-- 轮播（Carousel）项目 -->
            <div class="carousel-inner">
                <div class="item active " ng-repeat="newphoto in vm.newsPhotoList" ng-if="$index==0" ng-click="vm.showNoticeDetail(newphoto.id)">
                    <img style="height: 410px;width:100%"  class="img-responsive" ng-src="{{'/sys/attach/download/'+newphoto.photoAttachId}}" alt="First slide">
                    <div class="cssFont carousel-caption" style="margin-right: 0px;" ng-bind="newphoto.noticeTitle">

                    </div>
                </div>
                <div class="item text-center " ng-repeat="newphoto in vm.newsPhotoList" ng-if="$index!=0" ng-click="vm.showNoticeDetail(newphoto.id)">
                    <img style="height: 410px;width:100%"  class="img-responsive" ng-src="{{'/sys/attach/download/'+newphoto.photoAttachId}}" alt="First slide">
                    <div class="cssFont carousel-caption" style="margin-right: 0px;" ng-bind="newphoto.noticeTitle">

                    </div>
                </div>
            </div>
            <!-- 轮播（Carousel）导航 -->
            <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>

</div>
<div class="row justify-content-center">
    <div class="col-md-6 mb-5">
    <!-- BEGIN PORTLET-->
        <div class="portlet light cssShadow">
        <div class="portlet-title tabbable-steel">
            <div class="caption">
                <i class="fa fa-tasks font-blue-steel"></i>
                <span class="cssCaptionFont caption-subject font-blue-dark bold uppercase">我的任务</span>
            </div>
            <div class="tabbable-line"  >
                <ul class="nav nav-tabs" >

                    <li class="cssCaptionFont" style="float: right" >
                        <a href="#tab_1_2" data-toggle="tab" aria-expanded="false">
                            督办 <span style="font-size: 10px" class="badge badge-danger" ng-bind="vm.superviseTasks.length"></span> </a>
                    </li>
                    <li class="cssCaptionFont active"  style="float: right">
                        <a href="#tab_1_1" data-toggle="tab" aria-expanded="true">
                            待办  <span style="font-size: 10px" class="badge badge-success" ng-bind="vm.taskTotal"></span> </a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="portlet-body">
            <!--BEGIN TABS-->
            <div class="tab-content">
                <div class="tab-pane active" id="tab_1_1">
                    <div class="scroller" style="height: 245px">
                            <ul class="feeds">
                                <li ng-repeat="task in vm.tasks" repeat-finish="initMetronic()">
                                    <a  href="javascript:;"  ng-click="vm.showDetail(task.businessKey);">
                                        <div class="col1" >
                                            <div class="cssFont desc" style="line-height: 2;vertical-align:bottom">
                                                <div ng-if="task.processDescription!=''">
                                                    <span ng-bind="task.processDescription"></span>
                                                    <span ng-show="task.ccTask"  class="label label-sm label-default ">抄送</span>
                                                    <span ng-hide="task.outTime" class="label label-sm label-danger ">超期</span>
                                                </div>
                                                <div ng-if="task.processDescription==''">
                                                    <span ng-bind="task.processDefinitionName"></span>
                                                     <span ng-show="task.ccTask"  class="label label-sm label-default ">抄送</span>
                                                    <span ng-hide="task.outTime" class="label label-sm label-danger ">超期</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col2">
<%--                                            <span ng-bind="task.createTime|date:'yyyy-MM-dd HH:mm'"></span>--%>
                                            <div class="cssDate date" ng-bind="task.createTime|date:'yyyy/MM/dd'">

                                            </div>
                                        </div>
                                    </a>

                                </li>
                            </ul>
                        </div>
                    <div class="scroller-footer">
                        <div class="btn-arrow-link pull-right" ng-click="vm.showTaskDetail()">
                            <a href="javascript:;" >更多</a>
                            <i class="icon-arrow-right"></i>
                        </div>
                    </div>
                </div>

                <div class="tab-pane" id="tab_1_2">
                    <div class="scroller" style="height: 245px">
                            <ul class="feeds">
                                <li ng-repeat="task in vm.superviseTasks" repeat-finish="initMetronic()">
                                    <a  href="javascript:;"  ng-click="vm.showDetail(task.businessKey);">
                                        <div class="col1" >
                                            <div class="cssFont desc" style="line-height: 2;vertical-align:bottom"  >
                                                <span ng-bind="task.fileHeader"></span>
                                                <span ng-bind="task.superviseType"></span>
                                                <span ng-bind="task.feedbackTime"></span>
                                            </div>
                                        </div>
                                        <div class="col2">
                                            <span ng-bind="task.gmtCreate|date:'yyyy-MM-dd HH:mm'"></span>
                                        </div>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    <div class="scroller-footer" >
                        <div class="btn-arrow-link pull-right" ng-click="vm.showTaskDetail()">
                            <a href="javascript:;" >更多</a>
                            <i class="icon-arrow-right"></i>
                        </div>
                    </div>
                </div>

            </div>
            <!--END TABS-->
        </div>
    </div>
    <!-- END PORTLET-->
    </div>
    <div class="col-md-6 mb-5">
        <div class="portlet light cssShadow">
            <div class="portlet-title tabbable-steel">
                <div class="tabbable-line"  >
                    <ul class="nav nav-tabs" >
                        <li class="cssCaptionFont active" style="float: left" >
                            <a href="#tab_2_2" data-toggle="tab" aria-expanded="false">
                                公司制度 <span style="font-size: 10px" class="badge badge-danger" ></span> </a>
                        </li>
                        <li class="cssCaptionFont " style="float: left">
                            <a  href="#tab_2_1" data-toggle="tab" aria-expanded="true">
                                集团制度 <span style="font-size: 10px" class="badge badge-default" ></span> </a>
                        </li>

                    </ul>
                </div>
            </div>
            <div class="portlet-body">
                <!--BEGIN TABS-->
                <div class="tab-content">
                    <div class="tab-pane " id="tab_2_1">
                        <div class="scroller" style=" height: 245px;">
                                <ul class="feeds">
                                    <li ng-repeat="regulatory in vm.regulatoryList" repeat-finish="initMetronic()">
                                        <a  href="javascript:;"  ng-click="vm.showNoticeDetail(regulatory.id,regulatory.attachId)">
                                            <div class="col1" >
                                                <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="regulatory.noticeTitle" title="{{regulatory.noticeTitle}}" >

                                                </div>
                                            </div>
                                            <div class="col2">
                                                <div class="cssDate date" ng-bind="regulatory.gmtModified|date:'yyyy/MM/dd'">
                                                </div>
                                            </div>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        <div class="scroller-footer">
                            <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('集团制度')">
                                <a href="javascript:;" >更多</a>
                                <i class="icon-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane active" id="tab_2_2">
                        <div class="scroller1" style=" height: 245px;">
                                <ul class="feeds">
                                    <li ng-repeat="item in vm.companyList" repeat-finish="initMetronic()">
                                        <a  href="javascript:;"  ng-click="vm.showNoticeDetail(item.id,item.attachId)">
                                            <div class="col1" >
                                                <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="item.noticeTitle" title="{{item.noticeTitle}}" >
                                                </div>
                                            </div>
                                            <div class="col2">
                                                <div class="cssDate date" ng-bind="item.gmtModified|date:'yyyy/MM/dd'">
                                                </div>
                                            </div>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        <div class="scroller-footer">
                            <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('公司制度')">
                                <a href="javascript:;" >更多</a>
                                <i class="icon-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <!--END TABS-->
            </div>
        </div>
    </div>
</div>
<div class="row justify-content-center">
    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-pencil-square font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">公司办工作</span>
                </div>
            </div>
            <div class="portlet-body"  style="height: 240px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="item in vm.partyOfficeList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(item.id,item.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="item.noticeTitle"  title="{{item.noticeTitle}}">
                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="item.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('公司办公室(董事会办公室）')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>

    </div>

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow ">
            <div class="portlet-title">
                <div class="caption">
                    <img src="/m/ccp.png" style="margin-bottom: 4px" />
                    <span class="caption-subject font-red-soft bold uppercase">党建园地</span>
                </div>
            </div>
            <div class="portlet-body"  style="height: 240px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="item in vm.newsBusinessList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(item.id,item.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="item.noticeTitle" title="{{item.noticeTitle}}" >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="item.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('党群工作部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="glyphicon glyphicon-globe font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">经营资讯</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 240px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="newsMessage in vm.newsMessageList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(newsMessage.id,newsMessage.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="newsMessage.noticeTitle" title="{{newsMessage.noticeTitle}}" >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="newsMessage.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('经营发展部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row justify-content-center">
    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-star font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">保密管理</span>
                </div>
            </div>
            <div class="portlet-body"  style="height: 240px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="item in vm.partyBuildingList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(item.id,item.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="item.noticeTitle" title="{{item.noticeTitle}}" >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="item.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('保密办公室')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>

    </div>


    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-mortar-board font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">网信建设</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="newsSecrecy in vm.newsSecrecyList">
                            <a  href="javascript:;" ng-click="vm.showNoticeDetail(newsSecrecy.id,newsSecrecy.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="newsSecrecy.noticeTitle" title="{{newsSecrecy.noticeTitle}}">

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="newsSecrecy.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('信息化建设与管理部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>

    </div>

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="glyphicon glyphicon-usd font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">财金专栏</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="regulatory in vm.newsFinanceList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(regulatory.id,regulatory.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="regulatory.noticeTitle"  title="{{regulatory.noticeTitle}}">

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="regulatory.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('财务金融部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>


</div>
<div class="row justify-content-center">

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow ">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-user font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">人资信息</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="regulatory in vm.newsPoliticsList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(regulatory.id,regulatory.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="regulatory.noticeTitle" title="{{regulatory.noticeTitle}}" >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="regulatory.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('人力资源部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4 mb-4">
        <div class="portlet light  cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-speedometer font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">工程管理</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="regulatory in vm.newsLogisticsList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(regulatory.id,regulatory.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="regulatory.noticeTitle" title="{{regulatory.noticeTitle}}" >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="regulatory.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('工程管理部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>

    </div>

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-size-actual font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">纪检、审计与风险管理</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="regulatory in vm.newsTrainList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(regulatory.id,regulatory.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="regulatory.noticeTitle" title="{{regulatory.noticeTitle}}" >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="regulatory.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('纪检工作部、审计与风险管理部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>


</div>
<div class="row justify-content-center">
    <div class="col-md-4 mb-4">
        <div class="portlet light  cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-rocket font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">科研管理</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="laborScientific in vm.laborScientificList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(laborScientific.id,laborScientific.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="laborScientific.noticeTitle" title="{{laborScientific.noticeTitle}}"  >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="laborScientific.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('科研与技术质量部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>

    </div>

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-sitemap font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">后勤行管</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="laborUnion in vm.laborUnionList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(laborUnion.id,laborUnion.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="laborUnion.noticeTitle" title="{{laborUnion.noticeTitle}}" >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="laborUnion.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList('行政事务部')">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-book font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">友情链接</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">

                <div class="row justify-content-center">
                    <div  ng-repeat="item in vm.linkPageInfo.list">
                        <div class="justify-content-center"  style="display: flex; flex-flow: wrap; padding-left: 5px;float: left">
                            <div ng-click="vm.openLink(item)"  style=" text-align:center;vertical-align:bottom;border-radius:12px !important;box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1);width: 110px;height: 110px;color:#369; margin: 2px 5px 5px 2px;">
                                <%-- <img ng-src="/common/attach/download/1078" style="width: 80px;height: 60px;margin-top: 10px">--%>
                                <%--<img src="/m/wuzhoulogo80x80.png" style="width: 80px;height: 60px;margin-top: 10px">--%>
                                <img ng-src="/common/attach/download/{{item.linkLogo}}" style="width: 72px;height: 72px;margin-top: 10px">
                                <p style=";font-size: 15px;color: #369">{{item.linkName}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showMoreLink()">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>
</div>
<div  class="row justify-content-center">

    <div class="col-md-4 mb-4">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-cloud-download font-blue-dark"></i>
                    <span class="caption-subject font-blue-dark bold uppercase">下载中心</span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="download in vm.downLoads">
                            <a  href="/sys/attach/download/{{download.attachId}}">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="download.softwareName"  >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="download.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showLoadDetail()">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4 mb-4" ng-if="vm.params.showSign">
        <div class="portlet light  cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-book-open font-blue"></i>
                    <span class="caption-subject font-blue-dark bold uppercase" ng-bind="vm.showDept.deptName"></span>
                </div>
            </div>
            <div class="portlet-body" style="height: 245px">
                <div class="scroller1">
                    <ul class="feeds">
                        <li ng-repeat="laborScientific in vm.designNoticeList">
                            <a  href="javascript:;"  ng-click="vm.showNoticeDetail(laborScientific.id,laborScientific.attachId)">
                                <div class="col1" >
                                    <div class="cssFont desc" style="line-height: 2;vertical-align:bottom" ng-bind="laborScientific.noticeTitle"  >

                                    </div>
                                </div>
                                <div class="col2">
                                    <div class="cssDate date" ng-bind="laborScientific.gmtModified|date:'yyyy/MM/dd'">
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
            <div class="scroller-footer">
                <div class="btn-arrow-link pull-right" ng-click="vm.showNoticeList(vm.showDept.deptName)">
                    <a href="javascript:;" >更多</a>
                    <i class="icon-arrow-right"></i>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>
