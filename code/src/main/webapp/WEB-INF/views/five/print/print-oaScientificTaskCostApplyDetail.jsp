<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 40px;
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
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">科研课题费用申请</h3>
    <p style="float:right;" ng-bind="'年度：'+vm.printData.gmtCreate"></p>
    <table class="print_table">
        <tr>
            <td style="width:25%;">课程名称</td>
            <td style="width:25%;" ng-bind="vm.printData.taskName"></td>
            <td style="width:25%;">课题编号</td>
            <td style="width:25%;" ng-bind="vm.printData.taskNo"></td>
        </tr>
        <tr>
            <td>经费用途</td>
            <td colspan="3" ng-bind="vm.printData.costUse"></td>
        </tr>
        <tr>
            <td style="border-bottom: none;">经费明细</td>
            <td style="text-align: center;">事项</td>
            <td style="text-align: center;">金额(元)</td>
            <td style="text-align: center;">备注</td>
        </tr>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td style="border-bottom: none;border-top: none;"></td>
            <td ng-bind="printDetail.item"></td>
            <td ng-bind="printDetail.price"></td>
            <td ng-bind="printDetail.remark"></td>
        </tr>
        <tr>
            <td style="border-top: none;"></td>
            <td style="text-align: center;">合计</td>
            <td ng-bind="vm.printData.sum"></td>
            <td></td>
        </tr>
        <tr>
            <td>是否列入课题费用计划</td>
            <td colspan="3" ng-bind="vm.printData.taskCostPlan"></td>
        </tr>
        <tr>
            <td>经费使用时间</td>
            <td colspan="3" ng-bind="vm.printData.costUseTime"></td>
        </tr>
        <tr>
            <td>技术服务单位</td>
            <td colspan="3" ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td rowspan="2">课题负责人</td>
            <td style="border-bottom:none;">
                <span ng-bind="vm.printData.taskChargeMen.userName"></span>
                <span ng-if="vm.printData.taskChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.taskChargeMen.userLogin}}"></span>
            </td>
            <td rowspan="2">申请单位负责人</td>
            <td style="border-bottom:none;">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
        </tr>
        <tr>
            <td style="border-top:none;" ng-bind="vm.printData.taskChargeMen.end|date:'yyyy-MM-dd'"></td>
            <td style="border-top:none;" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">信息化建设与管理部意见</td>
            <td colspan="3" style="height:80px;vertical-align:top;padding-top:5px;padding-left:5px;" ng-bind="vm.printData.scientificChargeMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">签字：</td>
            <td>
                <span ng-bind="vm.printData.scientificChargeMen.userName"></span>
                <span ng-if="vm.printData.scientificChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.scientificChargeMen.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.scientificChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">主管领导意见</td>
            <td colspan="3" style="height:80px;vertical-align:top;padding-top:5px;padding-left:5px;" ng-bind="vm.printData.scientificVicePresident.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">签字：</td>
            <td>
                <span ng-bind="vm.printData.scientificVicePresident.userName"></span>
                <span ng-if="vm.printData.scientificVicePresident.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.scientificVicePresident.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.scientificVicePresident.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">总会计师意见</td>
            <td colspan="3" style="height:80px;vertical-align:top;padding-top:5px;padding-left:5px;" ng-bind="vm.printData.chiefAccountant.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">签字：</td>
            <td>
                <span ng-bind="vm.printData.chiefAccountant.userName"></span>
                <span ng-if="vm.printData.chiefAccountant.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.chiefAccountant.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.chiefAccountant.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">总经理意见</td>
            <td colspan="3" style="height:80px;vertical-align:top;padding-top:5px;padding-left:5px;" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">签字：</td>
            <td>
                <span ng-bind="vm.printData.generalManager.userName"></span>
                <span ng-if="vm.printData.generalManager.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.generalManager.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.generalManager.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>