<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 18px;
        color:red;
    }

    .print_table td {
        border: solid red 1px;
        height:90px;
        text-align: left;
        vertical-align:top;
        color:red;
        border-left: none;
        border-right: none;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h2 style="text-align: center;color:red;">因公出国内部审批文单</h2>
        <table class="print_table">
            <tr>
                <td style="width:15%;border-left: none;">批示：</td>
                <td style="width:35%;text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.instructions"></td>
                <td style="width:15%;border-left: solid 1px;">因公出国</br>主管领导：</td>
                <td style="width:35%;text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.chargeLeaderName"></td>
            </tr>
            <tr>
                <td style="border-left: none;">业务主</br>管领导：</td>
                <td style="text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.businessChargeLeaderName"></td>
                <td style="border-left: solid 1px;">单位负责人:</td>
                <td style="text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.departmentChargeMenName"></td>
            </tr>
            <tr>
                <td style="border-left: none;">主办单位</br>和拟稿人：</td>
                <td style="text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.deptName"></td>
                <td colspan="2" style="text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.drafterName"></td>

            </tr>
            <tr>
                <td style="border-left: none;">标题：</td>
                <td colspan="3" style="text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.title"></td>
            </tr>
            <tr>
                <td style="border-left: none;">部门办</br>理意见：</td>
                <td colspan="3" style="text-align:left;padding-left:15px;color:black;" ng-bind="vm.printData.departmentComment"></td>
            </tr>
        </table>
</div>