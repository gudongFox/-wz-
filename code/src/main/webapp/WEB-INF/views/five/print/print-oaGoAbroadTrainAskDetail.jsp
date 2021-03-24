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

<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">参加出国培训的请示</h3>
    <p style="text-align:right;padding-right:25px;" ng-bind="'年度:'+vm.printData.applyTime|date:'yyyy'"></p>
    <table class="print_table">
        <tr>
            <td style="width:15%;">通知名称</td>
            <td style="width:40%;" ng-bind="vm.printData.askTitle"></td>
            <td style="width:15%;">组织单位</td>
            <td style="width:30%;" ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td>出国培新通知</td>
            <td colspan="3" ng-bind="vm.printData.goAbroadTitle"></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:left;padding-left:15px;border-bottom: none;">拟派人员名单</td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td>培训名称</td>
            <td>参加单位名称</td>
            <td>参加人员</td>
        </tr>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td ng-bind="printDetail.trainName"></td>
            <td ng-bind="printDetail.deptName"></td>
            <td ng-bind="printDetail.attendUserName"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td rowspan="2" style="width:15%;border-top: none">信息化建设与管理部意见</td>
            <td colspan="3" style="height:80px;text-align: left;padding-left: 15px;padding-right: 15px;border-top: none;" ng-bind="vm.printData.technologyDeptComment"></td>
        </tr>
        <tr>
            <td style="width:29%;border-right:none;"></td>
            <td style="width:28%;border-right:none;border-left:none;">
                <span ng-bind="vm.printData.informationTechnologyChargeMen.userName"></span>
                <span ng-if="vm.printData.informationTechnologyChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationTechnologyChargeMen.userLogin}}"></span>
            </td>
            <td style="width:28%;border-left:none;" ng-bind="vm.printData.informationTechnologyChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">专家委意见</td>
            <td colspan="3" style="height:80px;text-align: left;padding-left: 15px;padding-right: 15px;" ng-bind="vm.printData.specialistComment"></td>
        </tr>
        <tr>
            <td style="border-right:none;"></td>
            <td style="border-right:none;border-left:none;">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td style="border-left:none;" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">公司总工程师意见</td>
            <td colspan="3" style="height:80px;text-align: left;padding-left: 15px;padding-right: 15px;" ng-bind="vm.printData.totalEngineerComment"></td>
        </tr>
        <tr>
            <td style="border-right:none;"></td>
            <td style="border-right:none;border-left:none;">
                <span ng-bind="vm.printData.informationTechnologyLeader.userName"></span>
                <span ng-if="vm.printData.informationTechnologyLeader.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationTechnologyLeader.userLogin}}"></span>
            </td>
            <td style="border-left:none;" ng-bind="vm.printData.informationTechnologyLeader.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>