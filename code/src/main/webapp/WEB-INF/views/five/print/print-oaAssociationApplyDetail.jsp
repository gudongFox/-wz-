<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 60px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">入会申请表</h3>
    <p style="text-align:right;padding-right:25px;" ng-bind="'编协会编码:'+vm.printData.associationNo"></p>
    <table class="print_table">
        <tr>
            <td style="width:15%;">协（学）会名称</td>
            <td colspan="4" ng-bind="vm.item.associationName"></td>
        </tr>
        <tr>
            <td style="width:15%;">申请部门/单位</td>
            <td ng-bind="vm.item.deptName"></td>
            <td style="width:15%;">申请人</td>
            <td ng-bind="vm.item.handleManName"></td>
        </tr>

        <tr>
            <td style="width:15%;">主管单位名称</td>
            <td ng-bind="vm.item.deptChargeName"></td>
            <td style="width:15%;">公司领导</td>
            <td ng-bind="vm.item.companyLeaderName"></td>
        </tr>
        <tr>
            <td style="width:15%;border-bottom: none;">协会类型</td>
            <td style="border-bottom: none;" ng-bind="vm.item.associationType"></td>
            <td style="width:15%;border-bottom: none;">协会级别</td>
            <td style="border-bottom: none;" ng-bind="vm.item.associationLevel"></td>
        </tr>
        <tr>
            <td style="width:15%;border-bottom: none;">推荐负责人</td>
            <td style="border-bottom: none;" ng-bind="vm.item.recommendManName"></td>
            <td style="width:15%;border-bottom: none;">担任角色</td>
            <td style="border-bottom: none;" ng-bind="vm.item.holdRole"></td>
        </tr>
        <tr>
            <td style="width:15%;border-bottom: none;">联系人</td>
            <td style="border-bottom: none;" ng-bind="vm.item.recommendManName"></td>
            <td style="width:15%;border-bottom: none;">会费（元）</td>
            <td style="border-bottom: none;" ng-bind="vm.item.holdRole"></td>
        </tr>
        <tr>
            <td style="width:15%;">公司在协会中担任角色</td>
            <td style="text-align: left;padding-right: 15px;padding-left: 15px;" colspan="3" ng-bind="vm.item.associationRole"></td>
        </tr>
        <tr>
            <td style="width:15%;height:180px;">协会概况</td>
            <td colspan="3" style="text-align: left;vertical-align: top;padding-right: 15px;padding-left: 15px;padding-top: 15px;"  ng-bind="vm.item.associationSummarize"></td>
        </tr>
        <tr>
            <td style="width:15%;height:180px;">参加理由</td>
            <td style="text-align: left;vertical-align: top;padding-right: 15px;padding-left: 15px;padding-top: 15px;" colspan="3" ng-bind="vm.item.attendReason"></td>
        </tr>
    </table>
</div>