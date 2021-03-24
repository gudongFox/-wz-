<%--
  Created by IntelliJ IDEA.
  User: luodong
  Date: 2019/11/7
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--版本信息--%>
<div class="modal fade draggable-modal" id="edFileHistoryModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">历史版本信息</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 500px;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 30px;">#</th>
                            <th>文件名称</th>
                            <th style="width: 80px;">大小</th>
                            <th style="width: 60px;">创建人</th>
                            <th style="width: 130px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in edFileHistoryList">
                            <td class="dt-right" ng-bind="$index+1"></td>
                            <td>
                                <a ng-href="{{'/sys/attach/download/'+item.id}}" class="a_title" ng-bind="item.name" target="_blank"></a>
                            </td>
                            <td ng-bind="item.sizeName"></td>
                            <td ng-bind="item.creatorName"></td>
                            <td ng-bind="item.gmtCreate| date:'yyyy-MM-dd HH:mm'"></td>
                        </tr>
                        </tbody>
                    </table>
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

