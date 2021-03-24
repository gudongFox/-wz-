<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发输入清单及评审记录(部门批文)</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 20%">合同号</td>
            <td ng-bind="vm.printData.contractNo"></td>
            <td style="width: 15%">工程名称</td>
            <td ng-bind="vm.printData.projectName"></td>

        </tr>
        <tr>
            <td style="width: 20%">阶段</td>
            <td colspan="3">
                <span ng-repeat="stageName in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="stageName.codeValue"></span>
                    <input type="checkbox" ng-checked="stageName.codeValue==vm.printData.stageName"/>
                </span>
            </td>
        </tr>
        <tr ng-repeat="edComment in vm.edComments" style=" border-bottom: none">
            <td  style="height:60px;padding-left: 10px;text-align: left;width: 20%" ng-bind="edComment.baseName"></td>
            <td colspan="3" style="height:60px"ng-bind="edComment.baseComment"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
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
    <p style="font-size: 12px;">注：① 项目负责人应将各部门批文/意见填写在此表上。<br>
        &nbsp;     &nbsp;&nbsp;② 各专业负责人应协同项目负责人对输入要求进行评审，确保各专业明确政府各部门批文和顾客的要求。<br>
        &nbsp;     &nbsp;&nbsp;③ 若批文未到，又需先行设计时，项目负责人和各专业审核人应提出输入意见， 待批文到后再同各专业进行会签。

    </p>

</div>