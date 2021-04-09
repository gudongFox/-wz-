<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 45px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">计算机及外设维修服务单 </h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;border-right:none;">设备基本情况</td>
            <td colspan="3" style="border-left:none;"></td>
        </tr>
        <tr>
            <td style="width:15%;">申请单位</td>
            <td style="width:35%;" ng-bind="vm.item.deptName"></td>
            <td style="width:15%;">设备所在房间号</td>
            <td style="width:35%;" ng-bind="vm.item.deviceRoom"></td>
        </tr>
        <tr>
            <td>设备编号</td>
            <td ng-bind="vm.item.deviceNo"></td>
            <td>设备名称</td>
            <td ng-bind="vm.item.deviceName"></td>
        </tr>
        <tr>
            <td>设备密级</td>
            <td ng-bind="vm.item.deviceLevel"></td>
            <td>型号</td>
            <td ng-bind="vm.item.deviceType"></td>
        </tr>
        <tr>
            <td>责任人</td>
            <td ng-bind="vm.item.chargeMan"></td>
            <td>职工号</td>
            <td ng-bind="vm.item.chargeNo"></td>
        </tr>
        <tr>
            <td>联系电话</td>
            <td ng-bind="vm.item.chargeTel"></td>
            <td>保修时间</td>
            <td ng-bind="vm.item.repairTime"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:145px;text-align:left;padding-left:15px;padding-left:15px;padding-top:15px;vertical-align:top;">故障及需求：<p ng-bind="vm.item.chargeComment"></p></td>
        </tr>
        <tr>
            <td rowspan="2">计算机所审核</td>
            <td colspan="3" style="height:100px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px;padding-top:15px;vertical-align:top;" ng-bind="vm.item.computerComment"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-top:none;"></td>
        </tr>
        <tr>
            <td>维修人</td>
            <td ng-bind="vm.item.maintainManName"></td>
            <td>完成时间</td>
            <td ng-bind="vm.item.maintainTime"></td>
        </tr>
        <tr>
            <td>设备领取人</td>
            <td ng-bind="vm.item.receiveManName"></td>
            <td>领取时间</td>
            <td ng-bind="vm.item.receiveTime"></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:left;padding-right:20px;">反馈（意见、建议等）:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:145px;text-align:left;padding-left:15px;padding-left:15px;padding-top:15px;vertical-align:top" ng-bind="vm.item.comment"></td>
        </tr>
        <tr>
            <td style="width:15%;">备注</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px;padding-top:15px;vertical-align:top" ng-bind="vm.item.remark">&emsp;&emsp;</td>
        </tr>
    </table>
</div>