<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="/assets/liMarquee/css/liMarquee.css">
<div class="row" ng-show="vm.noticeTops.length>0">
    <div class="col-md-12" style="padding-bottom: 10px;">
        <div id="noticeList" style="background: #fff;height: 40px;line-height: 40px;font-weight: bold;">
            <a ng-click="vm.showDetail(notice.id)" ng-repeat="notice in vm.noticeTops" style="margin-left: 100px;">
                <span ng-show="notice.top" style="color: red;">置顶</span>
                <span ng-bind="'【'+notice.noticeType+'】'"></span>
                <span ng-bind="notice.gmtCreate|date:'yyyy-MM-dd'"></span>
                <span ng-bind="notice.noticeTitle"></span>
            </a>
        </div>
    </div>
</div>

<div class="row ">
    <div class="col-md-12">
        <!-- BEGIN PORTLET -->
        <div class="portlet box blue ">
            <div class="portlet-title">
                <div class="caption">
                    <span class="caption-subject bold uppercase">电子公告</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-default btn-sm" ng-click="vm.init();">
                        <i class="fa fa-refresh"></i> 刷新 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="tabbable-line">
                    <ul class="nav nav-tabs ">
                        <li class="active">
                            <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                                系统公告 <span style="font-size: 10px" ng-bind="vm.tasks.length"></span> </a>
                        </li>
                        <li >
                            <a href="#tab_15_2" data-toggle="tab" aria-expanded="true">
                                国内新闻 <span style="font-size: 10px" ng-bind="vm.tasks.length"></span> </a>
                        </li>
                        <li>
                            <a href="#tab_15_3" data-toggle="tab" aria-expanded="true">
                                企业新闻 </a>
                        </li>
                        <li>
                            <a href="#tab_15_4" data-toggle="tab" aria-expanded="false">
                                行业新闻 </a>
                        </li>
                        <li>
                            <a href="#tab_15_5" data-toggle="tab" aria-expanded="false">
                                规章制度 </a>
                        </li>
                        <li>
                            <a href="#tab_15_6" data-toggle="tab" aria-expanded="false">
                                法律宣传 </a>
                        </li>
                        <li>
                            <a href="#tab_15_7" data-toggle="tab" aria-expanded="false">
                                党建园地 </a>
                        </li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="tab_15_1">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="float: left;">公告描述：<input type="search"
                                                                             class="form-control input-inline"
                                                                             placeholder="公告描述"
                                                                             ng-model="vm.qDescription"></label>

                                </div>
                            </div>
                            <div class="table-scrollable"
                                 style="height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 公告标题</th>
                                        <th><i class="fa fa-tasks"></i> 发布部门</th>
                                        <th style="width: 180px;"><i class="fa fa-user"></i> 发布人</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 发布时间</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="notice in vm.notices|filter:{description:vm.qDescription}|filter:{noticeType:'系统公告'}">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td class="data_title" ng-bind="notice.noticeTitle"
                                            ng-click="vm.showDetail(notice.id);"></td>
                                        <td ng-bind="notice.publishDeptName"></td>
                                        <td ng-bind="notice.creatorName"></td>
                                        <td ng-bind="notice.gmtModified|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(notice.id);"></i>
                                        </td>
                                    </tr>
 
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane " id="tab_15_2">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="float: left;">公告描述：<input type="search"
                                                                             class="form-control input-inline"
                                                                             placeholder="公告描述"
                                                                             ng-model="vm.qDescription"></label>

                                </div>
                            </div>
                            <div class="table-scrollable"
                                 style="height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 公告标题</th>
                                        <th><i class="fa fa-tasks"></i> 发布部门</th>
                                        <th style="width: 180px;"><i class="fa fa-user"></i> 发布人</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 发布时间</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="notice in vm.notices|filter:{description:vm.qDescription}|filter:{noticeType:'国内新闻'}">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td class="data_title" ng-bind="notice.noticeTitle"
                                            ng-click="vm.showDetail(notice.id);"></td>
                                        <td ng-bind="notice.publishDeptName"></td>
                                        <td ng-bind="notice.creatorName"></td>
                                        <td ng-bind="notice.gmtModified|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(notice.id);"></i>
                                        </td>
                                    </tr>
 
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane " id="tab_15_3">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="float:  left;">公告描述：<input type="search"
                                                                             class="form-control input-inline"
                                                                             placeholder="公告描述"
                                                                             ng-model="vm.qDescription"></label>

                                </div>
                            </div>
                            <div class="table-scrollable"
                                 style="height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 公告标题</th>
                                        <th><i class="fa fa-tasks"></i> 发布部门</th>
                                        <th style="width: 180px;"><i class="fa fa-user"></i> 发布人</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 发布时间</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="notice in vm.notices|filter:{description:vm.qDescription}|filter:{noticeType:'企业新闻'}">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td class="data_title" ng-bind="notice.noticeTitle"
                                            ng-click="vm.showDetail(notice.id);"></td>
                                        <td ng-bind="notice.publishDeptName"></td>
                                        <td ng-bind="notice.creatorName"></td>
                                        <td ng-bind="notice.gmtModified|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(notice.id);"></i>
                                        </td>
                                    </tr>
 
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane " id="tab_15_4">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="float: left;">公告描述：<input type="search"
                                                                             class="form-control input-inline"
                                                                             placeholder="公告描述"
                                                                             ng-model="vm.qDescription"></label>

                                </div>
                            </div>
                            <div class="table-scrollable"
                                 style="height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 公告标题</th>
                                        <th><i class="fa fa-tasks"></i> 发布部门</th>
                                        <th style="width: 180px;"><i class="fa fa-user"></i> 发布人</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 发布时间</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="notice in vm.notices|filter:{description:vm.qDescription}|filter:{noticeType:'行业新闻'}">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td class="data_title" ng-bind="notice.noticeTitle"
                                            ng-click="vm.showDetail(notice.id);"></td>
                                        <td ng-bind="notice.publishDeptName"></td>
                                        <td ng-bind="notice.creatorName"></td>
                                        <td ng-bind="notice.gmtModified|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(notice.id);"></i>
                                        </td>
                                    </tr>
 
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane " id="tab_15_5">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="float: left;">公告描述：<input type="search"
                                                                             class="form-control input-inline"
                                                                             placeholder="公告描述"
                                                                             ng-model="vm.qDescription"></label>

                                </div>
                            </div>
                            <div class="table-scrollable"
                                 style="height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 公告标题</th>
                                        <th><i class="fa fa-tasks"></i> 发布部门</th>
                                        <th style="width: 180px;"><i class="fa fa-user"></i> 发布人</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 发布时间</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="notice in vm.notices|filter:{description:vm.qDescription}|filter:{noticeType:'规章制度'}">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td class="data_title" ng-bind="notice.noticeTitle"
                                            ng-click="vm.showDetail(notice.id);"></td>
                                        <td ng-bind="notice.publishDeptName"></td>
                                        <td ng-bind="notice.creatorName"></td>
                                        <td ng-bind="notice.gmtModified|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(notice.id);"></i>
                                        </td>
                                    </tr>
 
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane " id="tab_15_6">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="float: left;">公告描述：<input type="search"
                                                                             class="form-control input-inline"
                                                                             placeholder="公告描述"
                                                                             ng-model="vm.qDescription"></label>

                                </div>
                            </div>
                            <div class="table-scrollable"
                                 style="height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 公告标题</th>
                                        <th><i class="fa fa-tasks"></i> 发布部门</th>
                                        <th style="width: 180px;"><i class="fa fa-user"></i> 发布人</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 发布时间</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="notice in vm.notices|filter:{description:vm.qDescription}|filter:{noticeType:'法制宣传'}">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td class="data_title" ng-bind="notice.noticeTitle"
                                            ng-click="vm.showDetail(notice.id);"></td>
                                        <td ng-bind="notice.publishDeptName"></td>
                                        <td ng-bind="notice.creatorName"></td>
                                        <td ng-bind="notice.gmtModified|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(notice.id);"></i>
                                        </td>
                                    </tr>
 
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane " id="tab_15_7">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="float: left;">公告描述：<input type="search"
                                                                             class="form-control input-inline"
                                                                             placeholder="公告描述"
                                                                             ng-model="vm.qDescription"></label>

                                </div>
                            </div>
                            <div class="table-scrollable"
                                 style="height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 公告标题</th>
                                        <th><i class="fa fa-tasks"></i> 发布部门</th>
                                        <th style="width: 180px;"><i class="fa fa-user"></i> 发布人</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 发布时间</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="notice in vm.notices|filter:{description:vm.qDescription}|filter:{noticeType:'党建园地'}">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td class="data_title" ng-bind="notice.noticeTitle"
                                            ng-click="vm.showDetail(notice.id);"></td>
                                        <td ng-bind="notice.publishDeptName"></td>
                                        <td ng-bind="notice.creatorName"></td>
                                        <td ng-bind="notice.gmtModified|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(notice.id);"></i>
                                        </td>
                                    </tr>
 
                                    </tbody>
                                </table>
                            </div>
                        </div>


                    </div>
                </div>
            </div>
        </div>
        <!-- END PORTLET -->
    </div>
</div>
<div class="modal fade" id="noticeShowModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">电子公告详情-<span ng-bind="vm.notice.noticeTitle"></span></h4>
            </div>
            <div class="modal-body">
                <textarea name="content" style="width:100%;height: 400px;" id="editorText"  ></textarea>
            </div>
            <div class="portlet light">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-file"></i> 附件列表
                    </div>
                    <div class="actions" >
                        <a href="javascript:;" class="btn btn-sm default" ng-show="browserVersion!='ie9'"
                           ng-click="vm.batchDownloadEdFile();">
                            <i class="fa fa-download" title="批量下载"></i> 下载 </a>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>名称</th>
                                <th style="width: 90px;">大小</th>
                                <th style="width: 60px;">创建人</th>
                                <th style="width: 130px;">创建时间</th>
                                <th style="width: 130px;">修改时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="file in vm.files">
                                <td>
                                    <input type="checkbox" class="cb_edFile" data-index="{{$index}}" ng-show="browserVersion!='ie9'"/>
                                    <span ng-bind="$index+1" ng-show="browserVersion=='ie9'"></span>
                                </td>
                                <td>
                                    <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png" onerror="this.src='/m/img/filetype/file.png'"/>
                                    <span ng-bind="file.fileName" ng-show="browserVersion!='ie9'" class="data_title"
                                          ng-click="downloadFileWithXml(file.sysAttach.id);"></span>
                                    <a ng-href="{{'/sys/attach/download/'+file.sysAttach.id}}" class="a_title" ng-show="browserVersion=='ie9'" ng-bind="file.fileName" ></a>
                                    <span class="label label-sm label-success" ng-show="processInstance.preHandleTime<file.gmtModified">新</span>
                                </td>
                                <td ng-bind="file.sizeName"></td>
                                <td ng-bind="file.creatorName"></td>
                                <td ng-bind="file.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                                <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <p ng-show="browserVersion!='ie9'&&vm.files.length>1"><span style="cursor: pointer;color: #3598dc;" ng-click="selectAllEdFile();">全选</span><span style="cursor: pointer;color: #3598dc;margin-left: 10px;"  ng-click="cancelAllEdFile();" >取消</span></p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<script src="/assets/liMarquee/js/jquery.liMarquee.js"></script>





