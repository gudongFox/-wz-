<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="{{vm.treeData.icon}}"></i> <span ng-bind="vm.treeData.nodeName"></span>（设计与输入评审汇总）
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="dataTables_wrapper no-footer">

            <div class="table-scrollable">

                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;" >#</th>
                            <th>期次名称</th>
                            <th>文件名</th>
                            <th style="width: 100px;" >文件类型</th>
                            <th style="width: 80px;">上传人</th>
                            <th style="width: 150px;">创建时间</th>
                            <th style="width: 120px;">大小</th>
                            <th style="width: 100px;">审核状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="file in vm.files">
                            <td ng-bind="$index+1"></td>
                            <td ng-bind="file.stepName"></td>
                            <td style="word-break:break-all;"  >
                                <img ng-src="{{'/assets/img/'+file.commonAttach.extensionName+'.png'}}"
                                     onerror="this.src='/assets/img/doc.png'"
                                     style="width: 25px;height: 25px;" alt="">
                                <span style="font-size: 10px;margin-right: 5px;"></span>
                                <span ng-bind="file.fileName" ng-click="downloadFile(file.id);" style="color: blue;cursor: pointer;"></span>
                                <span class="label label-sm label-success" ng-show="file.businessKey.indexOf('co_')<0&&file.sourceId>0">协同</span>
                            </td>
                            <td ng-bind="file.fileType" ></td>
                            <td ng-bind="file.creatorName"></td>
                            <td ng-bind="file.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                            <td ng-bind="file.commonAttach.sizeName"></td>
                            <td ng-bind="file.stateName"></td>
                        </tr>
                        </tbody>
                    </table>

            </div>
        </div>
    </div>
</div>


