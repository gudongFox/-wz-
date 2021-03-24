<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">勘察输出清单</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号:'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 20%;">工程名称</td>
            <td style="width: 40%;" ng-bind="vm.printData.projectName"></td>
            <td style="width: 20%;">工程号</td>
            <td style="width: 20%;" ng-bind="vm.printData.projectNo"></td>
        </tr>

    </table>

    <table class="print_table">
        <tr>
            <td style="width: 20%;border-top: none;border-bottom: none;">勘察阶段</td>
            <td style="width: 80%;border-top: none;border-bottom: none;">
			<span style="float:left;padding-left:10px;text-align: left;"
                  ng-repeat="stageNames in sysCodes | filter:{codeCatalog:'勘察阶段'}:true">
                    &nbsp;&nbsp;&nbsp<span ng-bind="stageNames.codeValue"></span>
                    <input type="checkbox" ng-checked="stageNames.codeValue==vm.printData.stageName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </span>
            </td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width: 20%;">机号</td>
            <td style="width: 30%;" ng-bind="vm.printData.machineCode"></td>
            <td style="width: 20%;">所用软件</td>
            <td style="width: 30%;" ng-bind="vm.printData.softwareName"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width: 15%;border-top: none;border-bottom: none;">输出人</td>
            <td style="width: 15%;border-top: none;border-bottom: none;" ng-bind="vm.printData.userCreateTaskMan"></td>
            <td style="width: 15%;border-top: none;border-bottom: none;">审查人</td>
            <td style="width: 15%;border-top: none;border-bottom: none;" ng-bind="vm.printData.nextUserTaskMan"></td>
            <td style="width: 20%;border-top: none;border-bottom: none;">输出日期</td>
            <td style="width: 20%;border-top: none;border-bottom: none;" ng-bind="vm.printData.userCreateTaskTime"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width: 20%;">序号</td>
            <td style="width: 60%;">输 出 内 容</td>
            <td style="width: 20%;">备 注</td>
        </tr>
        <tr ng-repeat="file in vm.printData.files">
            <td ng-bind="$index+1"></td>
            <td ng-bind="file.fileName"></td>
            <td ng-bind="file.remark"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style=" border-top: none;width: 20%;height:45px"
                ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
</div>