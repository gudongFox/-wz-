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
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">出国培训申请表</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">申请单位</td>
            <td style="width:40%;" ng-bind="vm.item.applyDeptName"></td>
            <td style="width:15%;">申报日期</td>
            <td style="width:30%;" ng-bind="vm.item.declareTime"></td>
        </tr>
        <tr>
            <td>通知名称</td>
            <td colspan="3" ng-bind="vm.item.noticName"></td>
        </tr>
        <tr>
            <td>培训组织单位</td>
            <td ng-bind="vm.item.trainDeptName"></td>
            <td>相关通知</td>
            <td ng-bind="vm.item.otherNotic"></td>
        </tr>
        <tr>
            <td>培训名称（参加通知中的具体培训名称）</td>
            <td colspan="3" ng-bind="vm.item.trainName"></td>
        </tr>
        <tr>
            <td>拟派人员</td>
            <td colspan="3" ng-bind="vm.item.attendManName"></td>
        </tr>
        <tr>
            <td style="height:100px">选派理由</td>
            <td colspan="3" ng-bind="vm.item.attendReason"></td>
        </tr>
        <tr>
            <td rowspan="2">单位意见</td>
            <td colspan="3" style="height:80px" ng-bind="vm.item.deptComment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">单位领导签字：</td>
            <td colspan="2">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
                <span ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
        <tr>
            <td rowspan="2">信息化建设与管理部意见：</td>
            <td colspan="3" style="height:80px" ng-bind="vm.item.technologyComment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">信息化建设与管理部领导签字：</td>
            <td colspan="2">
                <span ng-bind="vm.printData.informationEquipmentMen.userName"></span>
                <span ng-if="vm.printData.informationEquipmentMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationEquipmentMen.userLogin}}"></span>
                <span ng-bind="vm.printData.informationEquipmentMen.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
</div>