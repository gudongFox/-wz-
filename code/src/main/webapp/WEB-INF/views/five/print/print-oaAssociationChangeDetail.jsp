<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 40px;
        text-align: center;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">协会信息变更流程</h3>
    <p style="text-align:right;padding-right:25px;" ng-bind="'日期:'+vm.printData.gmtCreate"></p>
    <table class="print_table">
        <tr>
            <td style="width:15%;">协会编号</td>
            <td ng-bind="vm.item.associationNo"></td>
            <td style="width:15%;">申请人</td>
            <td ng-bind="vm.item.handleManName"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:30px;background: #dcdcdc;">协会基本信息:</td>
        </tr>
        <tr>
            <td>协会（学会）名称</td>
            <td colspan="3" ng-bind="vm.item.associationName"></td>
        </tr>
        <tr>
            <td>主管单位</td>
            <td colspan="3" ng-bind="vm.item.deptChargeName"></td>
        </tr>
        <tr>
            <td>协会级别及类型</td>
            <td colspan="1" ng-bind="vm.item.associationLevel"></td>
            <td colspan="2" ng-bind="vm.item.associationType"></td>
        </tr>
        <tr>
            <td>协会概况</td>
            <td colspan="3" ng-bind="vm.item.associationSummarize"></td>
        </tr>
        <tr>
            <td>参加理由</td>
            <td colspan="3" ng-bind="vm.item.attendReason"></td>
        </tr>
        <tr>
            <td>主管单位名称</td>
            <td colspan="3" ng-bind="vm.item.deptChargeName"></td>
        </tr>
        <tr>
            <td>公司领导</td>
            <td ng-bind="vm.item.companyLeaderName"></td>
            <td>公司在协会担任角色</td>
            <td ng-bind="vm.item.associationRole"></td>
        </tr>
        <tr>
            <td>推荐负责人</td>
            <td ng-bind="vm.item.recommendManName"></td>
            <td>推荐人公司角色</td>
            <td ng-bind="vm.item.holdRole"></td>
        </tr>
        <tr>
            <td>联系人</td>
            <td ng-bind="vm.item.linkMan"></td>
            <td>会费</td>
            <td ng-bind="vm.item.associationFee"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:30px;background: #dcdcdc;">变更信息:</td>
        </tr>
        <tr>
            <td>变更后协会名称</td>
            <td colspan="3" ng-bind="vm.item.changeAssociationName"></td>
        </tr>
        <tr>
            <td>主管单位</td>
            <td colspan="3" ng-bind="vm.item.changeDeptChargeName"></td>
        </tr>
        <tr>
            <td>协会级别及类型</td>
            <td colspan="1" ng-bind="vm.item.changeAssociationLevel"></td>
            <td colspan="2" ng-bind="vm.item.changeAssociationType"></td>
        </tr>
        <tr>
            <td>协会概况</td>
            <td colspan="3" ng-bind="vm.item.changeAssociationSummarize"></td>
        </tr>
        <tr>
            <td>推荐负责人</td>
            <td ng-bind="vm.item.changeRecommendManName"></td>
            <td>推荐人公司角色</td>
            <td ng-bind="vm.item.changeHoldRole"></td>
        </tr>
        <tr>
            <td>联系人</td>
            <td ng-bind="vm.item.changeLinkMan"></td>
            <td>会费</td>
            <td ng-bind="vm.item.changeAssociationFee"></td>
        </tr>
    </table>
</div>