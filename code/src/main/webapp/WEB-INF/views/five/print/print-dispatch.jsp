<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style>
    .td{
        color: red;

    }
</style>
<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area">
    <h2 style="text-align: center;">中国五洲工程设计集团有限公司</h2>
    <table class="print_table">
        <tr>
            <td style="width:10%;height: 30px;">发文标题</td>
            <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.dispatchTitle"></td>
        </tr>
        <tr>
            <td style="width:20%;height: 30px;" colspan="1">签发人</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.signerName"></td>
            <td style="width: 20%;" colspan="1">会签人</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.countersignName"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">秘密等级</td>
            <td style="width: 35%;" ng-bind="vm.printData.secretGrade"></td>
            <td style="width: 15%;">期限</td>
            <td style="width: 35%;" ng-bind="vm.printData.allottedTime"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">公司办人员</td>
            <td style="width: 35%;" ng-bind="vm.printData.companyOfficeName"></td>
            <td style="width: 15%;">公司保密人员</td>
            <td style="width: 35%;" ng-bind="vm.printData.companySecurityName"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">发文</td>
            <td style="width: 35%;" ng-bind="vm.printData.dispatch"></td>
            <td style="width: 15%;">发文类型</td>
            <td style="width: 35%;" ng-bind="vm.printData.dispatchType"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">主题词</td>
            <td style="width: 35%;" ng-bind="vm.printData.subjectTerm"></td>
            <td style="width: 15%;">主办单位</td>
            <td style="width: 35%;" ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">主送</td>
            <td style="width: 35%;" ng-bind="vm.printData.realSendManName"></td>
            <td style="width: 15%;">抄送</td>
            <td style="width: 35%;" ng-bind="vm.printData.copySendManName"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">创建人</td>
            <td style="width: 35%;" ng-bind="vm.printData.creatorName"></td>
            <td style="width: 15%;">创建时间</td>
            <td style="width: 35%;" ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none" ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="width: 20%;border-top: none" ng-bind="node.activityName"></td>
            <td style="width: 30%;border-top: none">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="width: 20%;border-top: none" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="width: 30%;border-top: none">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
    <p style="font-size: 12px;">注:本表由项目负责人和信息化建设与管理部负责人填写。</p>
</div>