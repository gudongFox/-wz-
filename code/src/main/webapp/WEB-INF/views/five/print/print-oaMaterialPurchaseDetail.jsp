<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table1 {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table1 td {
        border: solid #000 1px;
        height: 30px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
    .print_table2 {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table2 td {
        border: solid #000 1px;
        height: 35px;
        text-align: left;
        padding-left:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">资料采购</h3>
    <table class="print_table1">
        <tr>
            <td style="width:10%;">序号</td>
            <td style="width:25%;">标准号</td>
            <td style="width:25%;">资料名称</td>
            <td style="width:20%;">本数</td>
            <td style="width:20%;">备注</td>
        </tr>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td ng-bind="$index+1"></td>
            <td ng-bind="printDetail.standardNo"></td>
            <td ng-bind="printDetail.dataName"></td>
            <td ng-bind="printDetail.bookNumber"></td>
            <td ng-bind="printDetail.remark"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-bottom:none;">总计</td>
            <td style="border-bottom:none;" ng-bind="vm.printData.sum"></td>
            <td style="border-bottom:none;"></td>
        </tr>
    </table>
    <table class="print_table2">
        <tr>
            <td style="width:10%;">申请人：</td>
            <td style="width:25%;" ng-bind="vm.printData.applicantManName"></td>
            <td style="width:7%;">职工号:</td>
            <td style="width:18%;" ng-bind="vm.printData.applicantNo"></td>
            <td style="width:7%;">电话:</td>
            <td style="width:13%;" ng-bind="vm.printData.applicantTel"></td>
            <td style="width:7%;">日期:</td>
            <td style="width:13%;" ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td>单位负责人：</td>
            <td ng-bind="vm.printData.companyChargeName"></td>
            <td>档案图书室:</td>
            <td colspan="3" ng-bind="vm.printData.library"></td>
            <td>日期:</td>
            <td ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>

