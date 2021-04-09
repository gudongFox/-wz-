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
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">合同评审记录表</h3>
    <table class="print_table">
        <tr>
            <td style="width:10%;">项目类别：</td>
            <td colspan="2" ng-bind="vm.printData.projectType"></td>
            <td style="width:20%;">日期：</td>
            <td style="width:30%;" ng-bind="vm.printData.contractTime"></td>
        </tr>
        <tr>
            <td colspan="2" style="width:20%;">项目名称：</td>
            <td colspan="3" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td colspan="2">合同编号:</td>
            <td ng-bind="vm.printData.contractNo"></td>
            <td>合同额：(单位万元)</td>
            <td ng-bind="vm.printData.contractMoney"></td>
        </tr>
        <tr>
            <td colspan="2" style="width:20%;">评审级别：</td>
            <td colspan="3" ng-bind="vm.printData.reviewLevel"></td>
        </tr>
        <tr>
            <td colspan="2" style="width:20%;">甲方：</td>
            <td colspan="3" ng-bind="vm.printData.firstParty"></td>
        </tr>
        <tr>
            <td colspan="2">乙方:</td>
            <td ng-bind="vm.printData.secondParty"></td>
            <td>项目经理/总设计师：</td>
            <td ng-bind="vm.printData.totalDesignerName"></td>
        </tr>
        <tr>
            <td colspan="2">评审时间:</td>
            <td ng-bind="vm.printData.reviewTime"></td>
            <td>评审地点：</td>
            <td ng-bind="vm.printData.reviewAddress"></td>
        </tr>
        <tr>
            <td colspan="2" style="width:20%;">参加评审人员：</td>
            <td colspan="3" ng-bind="vm.printData.reviewUserName"></td>
        </tr>
        <tr>
            <td colspan="5" style="height:30px;border-bottom:none;">合同内容:</td>
        </tr>
        <tr>
            <td colspan="5" style="height:80px;border-top:none;vertical-align:top;padding-left: 5px;padding-right: 5px;" ng-bind="vm.printData.contractContent"></td>
        </tr>
        <tr>
            <td colspan="5" style="height:30px;border-bottom:none;">评审内容:</td>
        </tr>
        <tr>
            <td colspan="5" style="height:80px;border-top:none;vertical-align:top;padding-left: 5px;padding-right: 5px;" ng-bind="vm.printData.reviewContent"></td>
        </tr>
        <tr>
            <td colspan="5" style="height:30px;border-bottom:none;">评审结论:</td>
        </tr>
        <tr>
            <td colspan="5" style="height:80px;border-top:none;border-bottom:none;vertical-align:top;padding-left: 5px;padding-right: 5px;" ng-bind="vm.printData.reviewConclusion"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="height:30px;" colspan="2">经营发展部领导</td>
            <td colspan="2">承办单位领导</td>
            <td colspan="2">项目经理/总设计师</td>
        </tr>
        <tr>
            <td style="height:50px;width:17%;">
                <p ng-bind="vm.printData.manageLeader.userName"></p>
                <p ng-if="vm.printData.manageLeader.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.manageLeader.userLogin}}"></p>
            </td>
            <td style="width:17%;" ng-bind="vm.printData.manageLeader.end|date:'yyyy-MM-dd'"></td>
            <td style="width:17%;">
                <p ng-bind="vm.printData.deptChargeMen.userName"></p>
                <p ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></p>
            </td>
            <td style="width:17%;" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
            <td style="width:16%;">
                <p ng-bind="vm.printData.projectManager.userName"></p>
                <p ng-if="vm.printData.projectManager.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.projectManager.userLogin}}"></p>
            </td>
            <td style="width:16%;" ng-bind="vm.printData.projectManager.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="6" style="height:30px;">批准: (主管公司领导)</td>
        </tr>
        <tr>
            <td colspan="6" style="height:80px;border-top:none;vertical-align:top;" ng-bind="vm.printData.manageSupervisorLeader.comment"></td>
        </tr>
        <tr>
            <td style="height:50px;">签字：</td>
            <td colspan="2">
                <p ng-bind="vm.printData.manageSupervisorLeader.userName"></p>
                <p ng-if="vm.printData.manageSupervisorLeader.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.manageSupervisorLeader.userLogin}}"></p>
            </td>
            <td>日期：</td>
            <td colspan="2" ng-bind="vm.printData.manageSupervisorLeader.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>