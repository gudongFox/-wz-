<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" >
    <h2 style="text-align: center;">中冶建工集团有限公司勘察设计研究总院</h2>
    <h3 style="text-align: center;">经营(经济)合同评审表</h3>
	<p style="float: left;margin-left:50px;font-size: 12px;">编号</p>
    <p style="float: right;font-size: 12px;"><span>时间：</span><span ng-bind="vm.printData.gmtCreate|date:'yyyy年MM月dd日'"></span></p>
    <table class="print_table">
        <tr>
            <td colspan='2' style="width: 25%;">报审部门</td>
            <td style="width:25%" ng-bind="vm.printData.deptName"></td>
            <td style="width: 25%;">经办人</td>
            <td style="width: 25%;" ng-bind="vm.printData.creatorName"></td>
        </tr>
		<tr>
			<td rowspan="6" style="width:5%">工<br/>程<br/>概<br/>况</td>
			<td>项目名称</td>
			<td ng-bind="vm.printData.projectName"></td>
			<td>建设单位</td>
			<td ng-bind="vm.printData.creatorName"></td>
		</tr>
		<tr>
			<td>项目地点</td>
			<td ng-bind="vm.printData.projectAddress"></td>
			<td>合同工期</td>
			<td ng-bind="vm.printData.projectTime"></td>
		</tr>
		<tr>
			<td>工程类别规模</td>
			<td ng-bind="vm.printData.projectScale"></td>
			<td>质量要求</td>
			<td ng-bind="vm.printData.projectQuality"></td>
		</tr>
		<tr>
			<td>工程价格</td>
			<td ><span ng-bind="vm.printData.contractMoney"></span > 费率:<span ng-bind="vm.printData.contractRate"></span></td>
			<td>合同类型</td>
			<td ng-bind="vm.printData.contractType"></td>
		</tr>
		<tr>
			<td>保证金（保函）条件</td>
			<td ng-bind="vm.printData.guaranteeRule"></td>
			<td>经营性质</td>
			<td ng-bind="vm.printData.businessType"></td>
		</tr>
		<tr>
			<td style="height:50px">工程内容</td>
			<td colspan="3" ng-bind="vm.printData.projectContent"></td>
		</tr>
		<tr>
			<td colspan="2" style="height:50px;">工程款支付条件</td>
			<td colspan="3" ng-bind="vm.printData.paymentRule"></td>
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

</div>