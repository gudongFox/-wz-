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
    <h3 style="text-align: center;">合同法律审查工作单</h3>
    <p ng-bind="'编号:'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width:13%;">合同号:</td>
            <td style="width:21%;" ng-bind="vm.printData.contractNo"></td>
            <td style="width:12%;text-align:left;padding-left:16px;">主办单位:</td>
            <td style="width:21%;" ng-bind="vm.printData.deptName"></td>
            <td style="width:12%;text-align:left;padding-left:10px;">是否领导审批:</td>
            <td style="width:21%;" ng-bind="vm.printData.flag"></td>
        </tr>
        <tr>
            <td  colspan="2" rowspan="2">签约方姓名</td>
            <td style="text-align:left;padding-left:16px;">甲方:</td>
            <td ng-bind="vm.printData.firstParty"></td>
            <td style="text-align:left;padding-left:10px;">乙方:</td>
            <td ng-bind="vm.printData.secondParty"></td>
        </tr>
        <tr>
            <td style="text-align:left;padding-left:16px;">丙方:</td>
            <td ng-bind="vm.printData.thirdParty"></td>
            <td style="text-align:left;padding-left:10px;">丁方:</td>
            <td ng-bind="vm.printData.fourthParty"></td>
        </tr>
        <tr>
            <td>送审人:</td>
            <td colspan="2" ng-bind="vm.printData.submitManName"></td>
            <td>联系方式:</td>
            <td colspan="2" ng-bind="vm.printData.link"></td>
        </tr>
        <tr>
            <td rowspan="5">合同概要:</td>
            <td>要素</td>
            <td colspan="3">（由送审人填写）</td>
            <td>备注说明</td>
        </tr>
        <tr>
            <td>项目名称</td>
            <td colspan="3" ng-bind="vm.printData.projectName"></td>
            <td rowspan="4"></td>
        </tr>
        <tr>
            <td>项目地址</td>
            <td colspan="3" ng-bind="vm.printData.projectAddress"></td>
        </tr>
        <tr>
            <td>履行期限</td>
            <td colspan="3" ng-bind="vm.printData.performanceDeadline"></td>
        </tr>
        <tr>
            <td>送审日期</td>
            <td colspan="3" ng-bind="vm.printData.examineTime"></td>
        </tr>
        <tr>
            <td style="height: 80px">法律审查意见</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" colspan="5" ng-bind="vm.printData.lawExamineComment"></td>
        </tr>
        <tr>
            <td style="height: 80px">同意修改或不能修改说明</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" colspan="5" ng-bind="vm.printData.changeReason"></td>
        </tr>
        <tr>
            <td style="height: 80px">法律审查意见</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" colspan="5" ng-bind="vm.printData.lawAuditComment"></td>
        </tr>
        <tr>
            <td style="height: 80px">送审单位领导意见</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" colspan="5" ng-bind="vm.printData.submitDeptLeaderComment"></td>
        </tr>
        <tr>
            <td style="height: 80px">公司主管领导意见</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" colspan="5" ng-bind="vm.printData.companyLeaderComment"></td>
        </tr>

    </table>
</div>