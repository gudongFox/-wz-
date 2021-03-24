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


<div id="print_area" hidden>
    <h3 style="text-align: center;">个人非密信息导出审批单</h3>
    <p style="float:right" ng-bind="'申请日期:'+vm.printData.gmtCreate|date:'yyyy-MM-dd'"></p>
    <table class="print_table">
        <tr>
            <td colspan="6" style="height:35px;text-align:left;background: #dcdcdc;">个人非密信息基础信息:</td>
        </tr>
        <tr>
            <td style="width:12%">姓名</td>
            <td style="width:22%" ng-bind="vm.printData.userName"></td>
            <td style="width:12%">职工号</td>
            <td style="width:21%" ng-bind="vm.printData.userLogin"></td>
            <td style="width:12%">单位</td>
            <td style="width:21%" ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td>导出硬盘序列号</td>
            <td ng-bind="vm.printData.hardDiskNo"></td>
            <td>导出服务器地址</td>
            <td ng-bind="vm.printData.serviceAddress"></td>
            <td>电话</td>
            <td ng-bind="vm.printData.phone"></td>
        </tr>
        <tr>
            <td colspan="6" style="text-align:left;"><span style="color:red;">(请携带硬盘更换确认单进行信息导出)</span></br><span>拟导出文件名称(多个文件请把文件名称全部列出)：</span></td>
        </tr>
        <tr>
            <td colspan="6" style="height:180px;text-align:left;vertical-align:top;padding-top:5px;padding-left:5px;padding-right:5px;" ng-bind="vm.printData.fileName"></td>
        </tr>
        <tr>
            <td colspan="6" style="height:35px;text-align:left;background: #dcdcdc;">部门审批:</td>
        </tr>
        <tr>
            <td colspan="6" style="height:135px;text-align:left;vertical-align:top;padding-top:5px;padding-left:5px;padding-right:5px;border-bottom:none;" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width:12%">部门领导签字：</td>
            <td style="width:38%">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td style="width:12%">日期：</td>
            <td style="width:38%" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;background: #dcdcdc;">网络运维中心办理:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:135px;text-align:left;vertical-align:top;padding-top:5px;padding-left:5px;padding-right:5px;border-bottom:none;" ng-bind="vm.printData.computerEquipmentMen.comment"></td>
        </tr>
        <tr>
            <td style="width:12%">部门领导签字：</td>
            <td style="width:38%">
                <span ng-bind="vm.printData.computerEquipmentMen.userName"></span>
                <span ng-if="vm.printData.computerEquipmentMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.computerEquipmentMen.userLogin}}"></span>
            </td>
            <td style="width:12%">日期：</td>
            <td style="width:38%" ng-bind="vm.printData.computerEquipmentMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>