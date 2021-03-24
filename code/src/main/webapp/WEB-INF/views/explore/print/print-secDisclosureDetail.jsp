<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;" >
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">安全技术交底记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width: 13%;height: 50px;">工程名称</td>
            <td style="width: 23%;" ng-bind="vm.printData.projectName">  </td>
			<td style="width: 13%;">分部分项工程</td>
            <td style="width: 19%;" ng-bind="vm.printData.partName">  </td>
		    <td style="width: 13%;">工种</td>
            <td style="width: 19%;" ng-bind="vm.printData.workType">  </td>
        </tr>

	</table>	
	<table class="print_table">
        <tr>
            <td style="text-align: left;height:500px;border-top: none;">
			<span style="float:left;padding-bottom:470px;padding-left:10px" ng-bind="'交底内容：'+vm.printData.disclosureDesc"></span>
			</td>
		</tr>
	</table>

	<table class="print_table">
		<tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
			<td style="border-top: none;width: 20%;height: 50px" ng-bind="node.activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="node.userName" style="margin-right: 10px;"></span>
				<span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
			</td>
			<td style="border-top: none;width: 20% ;height: 50px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
				<span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
			</td>
		</tr>
	</table>

	    <p style="font-size: 12px;">注:本交底必须一式二份,钻探队,钻探队长各一份</p>
	</div>