<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 55px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">流程开发申请 </h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">标题</td>
            <td style="width:40%;text-align:left;padding-left:15px;padding-left:15px;" ng-bind="vm.item.title"></td>
            <td style="width:15%;">申请单位</td>
            <td style="width:30%;text-align:left;padding-left:15px;padding-left:15px;" ng-bind="vm.item.deptName"></td>
        </tr>
        <tr>
            <td style="height:140px;">内容说明</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px;" ng-bind="vm.item.content"></td>
        </tr>
        <tr>
            <td style="height:140px;">表单信息</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px;" ng-bind="vm.item.formMessage"></td>
        </tr>
        <tr>
            <td style="height:140px;">流程信息</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px;" ng-bind="vm.item.processMessage"></td>
        </tr>
        <tr>
            <td>备注</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px;" ng-bind="vm.item.remark"></td>
        </tr>
    </table>
</div>