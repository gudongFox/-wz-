<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 16px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 65px;
        text-align: left;
        padding-left:5px;
        padding-right:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">业务合同签发单</h3>
    <p style="float:right" ng-bind="vm.printData.signTime"></p>
    <table class="print_table">
        <tr>
            <td style="width:15%;">委托方</td>
            <td colspan="3" ng-bind="vm.printData.clientName"></td>
        </tr>
        <tr>
            <td>委托内容</td>
            <td colspan="3" ng-bind="vm.printData.clientContent"></td>
        </tr>
        <tr>
            <td>合同编号</td>
            <td colspan="3" ng-bind="vm.printData.contractNo"></td>
        </tr>
        <tr>
            <td colspan="2" rowspan="3" style="vertical-align:top;">签发：<p ng-bind="vm.printData.signerName"></p></td>
            <td style="width:25%">核稿：</td>
            <td style="width:25%" ng-bind="vm.printData.auditManName"></td>
        </tr>
        <tr>
            <td rowspan="2">主办单位和拟稿人</td>
            <td ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td ng-bind="vm.printData.drafterName"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:400px;vertical-align:top;">会签:<span ng-bind="vm.printData.countersignName"></span></td>
        </tr>
        <tr>
            <td>合同评审单</td>
            <td colspan="3" ng-bind="vm.printData.reviewContractName"></td>
        </tr>
    </table>
</div>