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
    <h3 style="text-align: center;">专业技术培新申请表</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">申请部门(单位)</td>
            <td style="width:35%;" ng-bind="vm.item.applyDeptName"></td>
            <td style="width:15%;">培训类别</td>
            <td style="width:35%;" ng-bind="vm.item.trainType"></td>
        </tr>
        <tr>
            <td>申请人</td>
            <td ng-bind="vm.item.applyManName"></td>
            <td>培训时间</td>
            <td ng-bind="vm.item.trainTime"></td>
        </tr>
        <tr>
            <td>培训地点</td>
            <td ng-bind="vm.item.trainAddress"></td>
            <td>培训费用（元）</td>
            <td ng-bind="vm.item.trainPrice"></td>
        </tr>
        <tr>
            <td style="height:105px;">培训内容</td>
            <td colspan="3" style="text-align:left;vertical-align: top;padding-left:15px;padding-left:15px;padding-top: 15px;" ng-bind="vm.item.trainContent"></td>

        </tr>
        <tr>
            <td rowspan="2">单位意见</td>
            <td colspan="3" style="height:60px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-top:none;text-align: right;padding-right: 12%">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
                <span ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
        <tr>
            <td rowspan="2">信息化建设与管理部意见</td>
            <td colspan="3" style="height:60px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.printData.informationEquipmentChargeMen.comment"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-top:none;text-align: right;padding-right: 12%">
                <span ng-bind="vm.printData.informationEquipmentChargeMen.userName"></span>
                <span ng-if="vm.printData.informationEquipmentChargeMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationEquipmentChargeMen.userLogin}}"></span>
                <span ng-bind="vm.printData.informationEquipmentChargeMen.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>

    </table>
</div>