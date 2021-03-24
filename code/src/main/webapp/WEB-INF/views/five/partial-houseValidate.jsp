<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 设计文件审校
            <span ng-show="vm.validateInfo.notSubmit!=''">未提交:</span>
            <strong ng-show="vm.validateInfo.notSubmit!=''" ng-bind="vm.validateInfo.notSubmit" ></strong>
            <span ng-show="vm.validateInfo.notFinish!=''">正在校审:</span>
            <strong ng-show="vm.validateInfo.notFinish!=''" ng-bind="vm.validateInfo.notFinish" ></strong>
            <span ng-show="vm.validateInfo.finish!=''">已完成校审:</span>
            <strong ng-show="vm.validateInfo.finish!=''" ng-bind="vm.validateInfo.finish" ></strong>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 创建 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="alert alert-info">
            <strong>使用前重要提示：请确保人员与计划安排中,参与专业,校核人、审核人不能为空!(一校两审审定人不能为空)</strong>
        </div>
        <div class="dataTables_wrapper no-footer">
            <div class="table-scrollable">
                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th style="width: 90px;">评审专业</th>
                        <th style="width: 90px;">评审方式</th>
                        <th>校核人</th>
                        <th>审核人</th>
                        <th style="width: 60px;">审定人</th>
                        <th style="width: 60px;">文件数</th>
                        <th style="width: 60px;">意见数</th>
                        <th style="width: 60px;">创建人</th>
                        <th style="width: 95px;">创建时间</th>
                        <th style="width: 150px;">任务状态</th>
                        <th style="width: 50px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.list">
                        <td class="dt-right" ng-bind="$index+1"></td>
                        <td ng-bind="item.majorName"  class="data_title" ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.designReviewType"></td>
                        <td ng-bind="item.proofreadMenName"></td>
                        <td ng-bind="item.auditMenName"></td>
                        <td ng-bind="item.approveMenName"></td>
                        <td ng-bind="item.fileCount"></td>
                        <td ng-bind="item.markFinishCount+'/'+item.markCount"></td>
                        <td ng-bind="item.creatorName"></td>
                        <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                        <td ng-bind="item.processName"></td>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin&&!item.processEnd"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


