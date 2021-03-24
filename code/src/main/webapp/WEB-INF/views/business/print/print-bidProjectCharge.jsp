<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none">
    <h2 style="text-align: center;">中冶建工集团有限公司勘设总院项目投标勘察、设计、施工</h2>
    <h3 style="text-align: center;">项目负责人确认表</h3>
    <p style="float: right;font-size: 12px;"><span>时间：</span><span ng-bind="vm.printData.gmtCreate|date:'yyyy年MM月dd日'"></span></p>
    <table class="print_table">
        <tr>
			<td colspan='1'style="width:15%;border-right: none">工程名称:</td>
			<td colspan='3' style="border-left: none"><span ng-bind="vm.printData.projectName"></span></td>
		</tr>
		<tr>
			<td style="width:15%">经办人</td>
			<%--<td style="width:15%">发包人</td>--%>
			<td colspan='3' ng-bind="vm.printData.creatorName"></td>
		</tr>
		<tr>
			<td style="width:15%;height:50px">工程地点</td>
			<td colspan='3' ng-bind="vm.printData.projectAddress"></td>
		</tr>
		<tr>
			<td style="width:15%;height:50px">工程规模</td>
			<td colspan='3' ng-bind="vm.printData.projectScale"></td>
		</tr>
		<tr>
			<td style="width:15%;height:50px">工程内容</td>
			<td colspan='3' ng-bind="vm.printData.projectContent"></td>
		</tr>
		<tr>
			<td style="width:15%;height:50px">项目负责人资格要求</td>
			<td colspan='3' ng-bind="vm.printData.chargeRule"></td>
		</tr>
		<tr>
			<td style="width:15%;height:50px">开标时间</td>
			<td colspan='3' ng-bind="vm.printData.openTime"></td>
		</tr>
		<tr>
			<td rowspan="3" style="width:15%;height:120px">项目负责人确认</td>
			<td colspan='1' style="width:15%">勘察负责人</td>
			<td colspan="2" ng-bind="vm.printData.exploreManName">
		</tr>
		<tr>
			<td colspan='1' style="width:15%">设计负责人</td>
			<td colspan="2" ng-bind="vm.printData.designManName">
		</tr>
		<tr>
			<td colspan='1' style="width:15%">施工负责人</td>
			<td colspan="2" ng-bind="vm.printData.constructionManName">
		</tr>
		<tr>
			<td style="width:15%;height:50px;">各专业负责人</td>
			<td colspan='3' ng-bind="vm.printData.majorMen"></td>
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