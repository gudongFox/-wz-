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
        text-align: left;
        padding-left:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">专利项目交费申请</h3>
    <p style="float:right" ng-bind="'日期'+vm.printData.paymentTime"></p>
    <table class="print_table">
        <tr>
            <td style="width:20%;">专利项目产生或使用单位</td>
            <td colspan="4" ng-bind="vm.printData.deptName"></td>

        </tr>
        <tr>
            <td colspan="5" style="text-align: center;background: #dcdcdc;">缴费项目清单</td>
        </tr>
        <tr>
            <td style="width:20%;">序号</td>
            <td style="width:20%;">专利号</td>
            <td style="width:20%;">专利名称</td>
            <td style="width:20%;">维持放弃意见</td>
            <td style="width:20%;">论述放弃理由</td>
        </tr>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td ng-bind="$index+1"></td>
            <td ng-bind="printDetail.inventNo"></td>
            <td ng-bind="printDetail.inventName"></td>
            <td ng-bind="printDetail.keepGiveUp"></td>
            <td ng-bind="printDetail.reason"></td>
        </tr>
        <tr>
            <td colspan="6" style="border-bottom:none;">依托单位负责人意见</td>
        </tr>
        <tr>
            <td colspan="6" style="height:90px;border-top:none;border-bottom:none;vertical-align:top;" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align:right;border-top:none;border-right:none;">签字</td>
            <td style="border-top:none;border-right:none;border-left:none;">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td style="text-align:right;border-top:none;border-right:none;border-left:none;">日期</td>
            <td style="border-top:none;border-left:none;" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>

        <tr>
            <td colspan="6" style="border-bottom:none;">专家委员会审批意见</td>
        </tr>
        <tr>
            <td colspan="6" style="height:90px;border-top:none;border-bottom:none;vertical-align:top;" ng-bind="vm.printData.expertCommittee.comment"></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align:right;border-top:none;border-right:none;">签字</td>
            <td style="border-top:none;border-right:none;border-left:none;">
                <span ng-bind="vm.printData.expertCommittee.userName"></span>
                <span ng-if="vm.printData.expertCommittee.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.expertCommittee.userLogin}}"></span>
            </td>
            <td style="text-align:right;border-top:none;border-right:none;border-left:none;">日期</td>
            <td style="border-top:none;border-left:none;" ng-bind="vm.printData.expertCommittee.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="6" style="border-bottom:none;">公司总工程师审批意见</td>
        </tr>
        <tr>
            <td colspan="6" style="height:90px;border-top:none;border-bottom:none;vertical-align:top;" ng-bind="vm.printData.chiefEngineer.comment"></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align:right;border-top:none;border-right:none;">签字</td>
            <td style="border-top:none;border-right:none;border-left:none;">
                <span ng-bind="vm.printData.chiefEngineer.userName"></span>
                <span ng-if="vm.printData.chiefEngineer.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.chiefEngineer.userLogin}}"></span>
            </td>
            <td style="text-align:right;border-top:none;border-right:none;border-left:none;">日期</td>
            <td style="border-top:none;border-left:none;" ng-bind="vm.printData.chiefEngineer.end|date:'yyyy-MM-dd'"></td>
        </tr>

    </table>
</div>