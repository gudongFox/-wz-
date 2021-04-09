<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 80px;
        text-align: center;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">报告文单</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">文件标题</td>
            <td colspan="5" ng-bind="vm.printData.fileTitle"></td>
        </tr>
        <tr>
            <td>签收人</td>
            <td style="width:20%;" ng-bind="vm.printData.signerName"></td>
            <td style="width:15%;">签收日期</td>
            <td style="width:22%;" ng-bind="vm.printData.receiveTime"></td>
            <td style="width:15%;">批示领导</td>
            <td style="width:23%;" ng-bind="vm.printData.companyLeaderName"></td>
        </tr>
        <tr>
            <td>承办单位</td>
            <td ng-bind="vm.printData.undertakeDeptName"></td>
            <td>来文单位</td>
            <td ng-bind="vm.printData.sendDeptName"></td>
            <td>来文号</td>
            <td ng-bind="vm.printData.sendWordSize"></td>
        </tr>
        <tr>
            <td>收文号</td>
            <td ng-bind="vm.printData.receiveWordSize"></td>
            <td>密级等级</td>
            <td ng-bind="vm.printData.security"></td>
            <td>正文份数</td>
            <td ng-bind="vm.printData.textNumber"></td>
        </tr>
        <tr>
            <td>附件</td>
            <td colspan="5">
                <div  ng-repeat="file in vm.printData.fileList">
                    <span ng-bind="file.fileName+'_'+file.creatorName"></span>
                </div>
            </td>
        </tr>
    </table>
</div>