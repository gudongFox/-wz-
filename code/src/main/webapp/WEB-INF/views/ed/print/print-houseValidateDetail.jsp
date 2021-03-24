<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" >
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
		<h3 style="text-align: center;">设计与开发验证记录</h3>
		<p style="float: right;font-size: 12px;" ng-bind="vm.printData.formNo"></p>
		<table class="print_table">
			<tr>
				<td colspan="1">工程名称</td>
				<td colspan="4" ng-bind="vm.printData.projectName">歇台子小学扩建项目</td>
				<td colspan="1">  校核 <input type="checkbox" checked/> 审核 <input type="checkbox" /> </td>
			</tr>
			<tr>
				<td >专业</td>
				<td  ng-bind="vm.printData.majorName"></td>
				<td >设计阶段</td>
				<td colspan="3">
				<span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.stageName"/>
                </span>
				</td>
			</tr>
			<tr>
				<td style="width:10% ">校审人</td>
				<td style="width:20% " ng-bind="vm.printData.proofreadManName">熊鑫</td>
				<td style="width:10% ">电话</td>
				<td style="width:25% "  ng-bind="vm.printData.proofreadManPhone"></td>
				<td style="width:10% ">日期</td>
				<td style="width:25% " ng-bind="vm.printData.create|date:'yyyy-MM-dd'">2019-06-13</td>
			</tr>
		</table>

		<table  style="border-top: 0px" class="print_table">
			<tr  style="border-top: 0px">
				<td style="width: 5%;border-top: 0px">序号</td>
				<td style="width: 10%;border-top: 0px">图名<br>（图号）</td>
				<td style="width: 65%;border-top: 0px">校审意见</td>
				<td style="width: 20%;border-top: 0px">意见类型</td>
			</tr>
			<tr style="height: 60px" ng-repeat="audit in vm.printData.proofreadValidateMarkList">
				<td  class="dt-right" ng-bind="$index+1">001</td>
				<td ng-bind="audit.drawNo">1</td>
				<td style="text-align: left;">
					<span ng-if="audit.markContent!=null"></span><span style="margin-left: 15px"  ng-bind="audit.markContent"></span>
					<br>
					<span ng-if="audit.markContent!=null">答:</span><span style="margin-left: 15px" ng-bind="audit.markAnswer"></span>
				</td>
				<td ng-bind="audit.markLevel">强条</td>
			</tr>
		</table>
		<table class="print_table">
			<tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
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
			<tr>
				<td colspan="1">填表说明</td>
				<td colspan="3" style="height:50px;text-align:left">1. 设计人回复意见应按序号逐条回复在每条意见后，应回复如何修改的，不可直接回复“已修改”   <br>2. 校审人员需填上姓名和电话  <br>3.专业负责人对校对结果进行检查，校审人应对处理结果进行验证。</td>
			</tr>
		</table>

   <div  style="page-break-before:always">
	<h2 style="text-align: center;">中冶建工集团有限公司</h2>
	<h3 style="text-align: center;">设计与开发验证记录</h3>
	<p style="float: right;font-size: 12px;" ng-bind="vm.printData.formNo">编号：Q/MCC-KS-SZ-07</p>
	<table class="print_table">
		<tr>
			<td colspan="1">工程名称</td>
			<td colspan="4" ng-bind="vm.printData.projectName">歇台子小学扩建项目</td>
			<td colspan="1">  校核 <input type="checkbox"/> 审核 <input type="checkbox" checked/> </td>
		</tr>
		<tr>
			<td >专业</td>
			<td ng-bind="vm.printData.majorName"></td>
			<td >设计阶段</td>
			<td colspan="3">
				<span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.stageName"/>
                </span>
			</td>
		</tr>
		<tr>
			<td style="width:10% ">校审人</td>
			<td style="width:20% " ng-bind="vm.printData.auditManName">熊鑫</td>
			<td style="width:10% ">电话</td>
			<td style="width:25% "  ng-bind="vm.printData.auditManPhone"></td>
			<td style="width:10% ">日期</td>
			<td style="width:25% " ng-bind="vm.printData.create|date:'yyyy-MM-dd'">2019-06-13</td>
		</tr>
	</table>
	<table style="border-top: 0px" class="print_table">
		<tr style="border-top: 0px">
			<td style="width: 5%;border-top: 0px">序号</td>
			<td style="width: 10%;border-top: 0px">图名<br>（图号）</td>
			<td style="width: 65%;border-top: 0px">校审意见</td>
			<td style="width: 20%;border-top: 0px">意见类型</td>
		</tr>
		<tr style="height: 60px" ng-repeat="audit in vm.printData.auditValidateMarkList">
			<td  class="dt-right" ng-bind="$index+1">001</td>
			<td ng-bind="audit.drawNo">1</td>
			<td style="text-align: left;">
				<span ng-if="audit.markContent!=null"></span><span style="margin-left: 15px"  ng-bind="audit.markContent"></span>
				<br>
				<span ng-if="audit.markContent!=null">答:</span><span style="margin-left: 15px" ng-bind="audit.markAnswer"></span>
			</td>
			<td ng-bind="audit.markLevel">强条</td>
		</tr>

	</table>
	<table class="print_table">
		<tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
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
		<tr>
			<td colspan="1">填表说明</td>
			<td colspan="3" style="height:50px;text-align:left">1. 设计人回复意见应按序号逐条回复在每条意见后，应回复如何修改的，不可直接回复“已修改”   <br>2. 校审人员需填上姓名和电话  <br>3.专业负责人对校对结果进行检查，校审人应对处理结果进行验证。</td>
		</tr>
	</table>
   </div>
</div>

