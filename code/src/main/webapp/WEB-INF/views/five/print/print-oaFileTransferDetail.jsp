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
    }
</style>


<div id="print_area" hidden>
    <h3 style="text-align: center;">非密网文件传送流程记录单</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">发送人：</td>
            <td style="width:35%;" ng-bind="vm.item.senderName"></td>
            <td style="width:15%;">发送时间：</td>
            <td style="width:35%;" ng-bind="vm.item.sendTime"></td>
        </tr>
        <tr>
            <td>文件名称：</td>
            <td ng-bind="vm.item.fileName"></td>
            <td>所在部门：</td>
            <td ng-bind="vm.item.deptName"></td>
        </tr>
        <tr>
            <td rowspan="2">文件类别</td>
            <td colspan="2">发起人本人确认本流程附带的所有文件均为非密文件</td>
            <td ng-bind="vm.item.fileType"></td>
        </tr>
        <tr>
            <td colspan="3" style="height:60px;text-align:left;padding-left:15px;padding-left:15px">注:非密网仅允许传送非密文件。对于个人不能确定是否涉密的文件，不得导入非密网不得擅自发送给其他人员。</td>
        </tr>
        <tr>
            <td rowspan="2">接收人：</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.item.receiverName"></td>

        </tr>
        <tr>
            <td colspan="3" style="height:60px;text-align:left;padding-left:15px;padding-left:15px"><b>注：</b></br>1、接收人在接收文件前，须确认文件类别是否为非密文件，选择。 是”即确认为非密文件。</br>2、未经发送人确认为非密的文件，接收人不得打开或下载附带的文件，否则一切后果自负。</td>
        </tr>
    </table>
</div>