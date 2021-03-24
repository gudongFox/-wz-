<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div ng-controller="ActRelevanceController as act">
    <div class="portlet light" >
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-file"></i> 可关联流程
            </div>
            <div class="actions" ng-show="processInstance.firstTask">
                <a href="javascript:;" class="btn btn-sm default" ng-show="browserVersion!='ie9'"
                   ng-click="act.showDoneTask()">
                    <i class="fa fa-plus" title="新增"></i> 新增 </a>
            </div>
        </div>
        <div class="portlet-body">
            <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th style="width: 180px;">流程名称</th>
                        <th>流程描述</th>
                        <th style="width: 60px;"> 发起人</th>
                        <th style="width: 100px;"> 发起时间</th>
                        <th style="width: 80px;"> 流程状态</th>
                        <th style="width: 55px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="actRelevance in act.actRelevanceList">
                        <td ng-bind="$index+1"></td>
                        <td ng-bind="actRelevance.processName" style="color: blue" ng-click="act.showDetail(actRelevance.businessKey);">
                        </td>
                        <td ng-bind="actRelevance.processDescription"></td>
                        <td ng-bind="actRelevance.myTask"></td>
                        <td ng-bind="actRelevance.gmtModified|date:'yyyy-MM-dd'"></td>
                        <td >
                            <span ng-if="item.processEnd">已完成</span>
                            <span ng-if="!item.processEnd">进行中</span>
                        </td>
                        <td>
                            <i class="fa  fa-pencil margin-right-5" ng-click="act.showDetail(actRelevance.businessKey);"
                               title="详情"></i>
                            <i class="fa  fa-trash-o margin-right-5" ng-click="act.removeActRelevance(actRelevance.id);" ng-show="processInstance.firstTask"
                               title="删除"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <p ng-show="browserVersion!='ie9'&&files.length>1"><span style="cursor: pointer;color: #3598dc;" ng-click="selectAllEdFile();">全选</span><span style="cursor: pointer;color: #3598dc;margin-left: 10px;"  ng-click="cancelAllEdFile();" >取消</span></p>
        </div>
    </div>

    <div class="modal fade draggable-modal" id="selectDoneTaskModal" tabindex="-1" role="basic" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title margin-right-10" ng-bind="act.template.formDesc">我的任务</h4>
                </div>
                <div class="modal-body" >
                    <div class="row">
                        <div class="col-md-12">
                            <a class="btn green" style="float: right;" ng-click="act.reloadDoneTask();"><i
                                    class="fa fa-search"></i> 查询 </a>
                            <label style="float: right;margin-right: 10px;">描述：<input type="search"
                                                                                        class="form-control input-inline"
                                                                                        placeholder="描述"
                                                                                        ng-model="act.params.qName" ></label>
                        </div>
                    </div>
                    <div class="table-scrollable" style="height: {{contentHeight/2}}px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>任务名称</th>
                                <th>任务描述</th>
                                <th style="width: 60px;"> 发起人</th>
                                <th style="width: 100px;"> 发起时间</th>
                                <th style="width: 80px;"> 流程状态</th>
                                <th style="width: 50px;"> 操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in act.pageInfo.list">
                                <td ng-bind="$index+1"></td>
                                <td class="highlight" ng-bind="item.formDesc" style="font-weight: bold;cursor: pointer;"></td>
                                <td >
                                    <span ng-bind="item.propertyList[0].head+':'+item.propertyList[0].text"></span><br>
                                    <span ng-bind="item.propertyList[1].head+':'+item.propertyList[1].text"></span>
                                </td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td >
                                    <span ng-if="item.processEnd">已完成</span>
                                    <span ng-if="!item.processEnd">进行中</span>
                                </td>
                                <td>
                                    <button type="button" class="btn blue btn-sm" ng-click="act.saveSelectTask(item);">确认</button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="act.pageInfo" on-load="act.loadDoneTask()"></my-pager>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn blue" ng-click="act.saveSelectTask();">确认</button>
                </div>
            </div>
        </div>
    </div>
</div>


