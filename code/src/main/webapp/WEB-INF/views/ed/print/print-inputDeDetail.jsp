<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发输入清单及评审记录（电气）</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 10%;">合同号</td>
            <td style="width: 15%;" ng-bind="vm.printData.contractNo"></td>
            <td style="width: 15%;">工程名称</td>
            <td style="width: 30%;"ng-bind="vm.printData.projectName"></td>
            <td style="width: 10%;">阶段</td>
            <td colspan="1"style="width: 20%;" ng-bind="vm.printData.stageName">
            </td>
        </tr>
    </table>
    <table  class="print_table">
        <tr ng-repeat="edComment in vm.printData.edComments" ng-if="($index%2)===0">
            <td style="width: 25%;border-top: none" ng-bind="edComment.baseName"></td>
            <td  style="width: 25%;border-top: none" ng-bind="edComment.baseComment"></td>
            <td style="width: 25%;border-top: none" ng-bind="vm.printData.edComments[$index+1].baseName"></td>
            <td style="width: 25%;border-top: none">
                <span ng-bind="vm.printData.edComments[$index+1].baseComment"></span>
            </td>
        </tr>
    </table>
    <table  class="print_table">
        <tr>
            <td colspan="6" style="border-top: none ">设  备  专  业  法  律  法  规  输  入  清  单</td>
        </tr>
        <tr>
            <td style="height: 450px" colspan="6" >
                <span style="float:left;padding-left: 50px;padding-bottom: 400px" ng-bind="vm.printData.lawList"></span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="width: 20%;border-top: none" ng-bind="node.activityName"></td>
            <td style="width: 30%;border-top: none">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="width: 22%;border-top: none" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="width: 27%;border-top: none">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>

    <p style="font-size: 12px;">注：① 电气专业负责人根据项目的基本特性，按要求输入；<br>
        ② 电气专业负责人向电气审核人汇报项目情况，并对电气专业的输入要求进行评审；
    </p>
</div>