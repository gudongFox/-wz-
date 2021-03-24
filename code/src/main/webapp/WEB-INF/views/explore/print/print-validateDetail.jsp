<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div style="display: none;"  id="print_area">
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
	<h3 style="text-align: center;">勘察成果文件审校记录</h3>
	<p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
		<tr>
			<td style="width: 60%;height: 45px">
				<span style="float:left;padding-left:10px">
			    校核		<input type="checkbox" checked/>&thinsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp;   &nbsp;  &nbsp;
				审核		<input type="checkbox"/> &thinsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;
				审定		<input type="checkbox"/></span>
			</td>
			<td style="width: 20%;">第 1 页 </td>
			<td style="width: 20%;">共 3 页</td>
		</tr>

	</table>
	<table class="print_table">
		<tr>
			<td style="width: 20%;height: 45px;border-top: none;border-bottom:none">项目名称</td>
			<td style="width: 40%;border-top: none;border-bottom:none" ng-bind="vm.printData.projectName"> </td>
			<td style="width: 20%;border-top: none;border-bottom:none"> 工程号</td>
			<td style="width: 20%;border-top: none;border-bottom:none" ng-bind="vm.printData.projectNo">  </td>
		</tr>
	</table>
	<table class="print_table">
		<tr>
			<td style="width: 10%;">序号</td>
			<td style="width: 10%;">图名图号 </td>
			<td style="width: 40%;">审定意见</td>
			<td style="width: 40%;">设计回复</td>
		</tr>
		<tr style="height: 45px" ng-repeat="Validate in vm.validateList1">
			<td ng-bind="$index+1"></td>
			<td ng-bind="Validate.drawNo"></td>
			<td ng-bind="Validate.markLevel"></td>
			<td ng-bind="Validate.markAnswer"></td>
		</tr>
	</table>


	<table class="print_table">
		<tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
			<td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="node.userName" style="margin-right: 10px;"></span>
				<span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
			</td>
			<td style=" border-top: none;width: 20%;height:45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
				<span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
			</td>
		</tr>
	</table>


<div style="page-break-before:always">
	<h2 style="text-align: center;">中冶建工集团有限公司</h2>
	<h3 style="text-align: center;">勘察成果文件审校记录</h3>
	<p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
		<tr>
			<td style="width: 60%;height: 45px">
				<span style="float:left;padding-left:10px">
			    校核		<input type="checkbox"/>&thinsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp;   &nbsp;  &nbsp;
				审核		<input type="checkbox" checked/> &thinsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;
				审定		<input type="checkbox"/></span>
			</td>
			<td style="width: 20%;">第 2 页 </td>
			<td style="width: 20%;">共 3 页</td>
		</tr>

	</table>
	<table class="print_table">
		<tr>
			<td style="width: 20%;height: 45px;border-top: none;border-bottom:none">项目名称</td>
			<td style="width: 40%;border-top: none;border-bottom:none" ng-bind="vm.printData.projectName"> </td>
			<td style="width: 20%;border-top: none;border-bottom:none"> 工程号</td>
			<td style="width: 20%;border-top: none;border-bottom:none" ng-bind="vm.printData.projectNo">  </td>
		</tr>
	</table>
	<table class="print_table">
		<tr>
			<td style="width: 10%;">序号</td>
			<td style="width: 10%;">图名图号 </td>
			<td style="width: 40%;">审定意见</td>
			<td style="width: 40%;">设计回复</td>
		</tr>
		<tr style="height: 45px" ng-repeat="Validate in vm.validateList2">
			<td ng-bind="$index+1"></td>
			<td ng-bind="Validate.drawNo"></td>
			<td ng-bind="Validate.markLevel"></td>
			<td ng-bind="Validate.markAnswer"></td>
		</tr>
	</table>


	<table class="print_table">
		<tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
			<td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="node.userName" style="margin-right: 10px;"></span>
				<span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
			</td>
			<td style=" border-top: none;width: 20%;height:45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
				<span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
			</td>
		</tr>
	</table>
</div>
<div style="page-break-before:always">
	<h2 style="text-align: center;">中冶建工集团有限公司</h2>
	<h3 style="text-align: center;">勘察成果文件审校记录</h3>
	<p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
		<tr>
			<td style="width: 60%;height: 45px">
				<span style="float:left;padding-left:10px">
			    校核		<input type="checkbox"/>&thinsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp;   &nbsp;  &nbsp;
				审核		<input type="checkbox" /> &thinsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;
				审定		<input type="checkbox" checked/></span>
			</td>
			<td style="width: 20%;">第 3 页 </td>
			<td style="width: 20%;">共 3 页</td>
		</tr>

	</table>
	<table class="print_table">
		<tr>
			<td style="width: 20%;height: 45px;border-top: none;border-bottom:none">项目名称</td>
			<td style="width: 40%;border-top: none;border-bottom:none" ng-bind="vm.printData.projectName"> </td>
			<td style="width: 20%;border-top: none;border-bottom:none"> 工程号</td>
			<td style="width: 20%;border-top: none;border-bottom:none" ng-bind="vm.printData.projectNo">  </td>
		</tr>
	</table>
	<table class="print_table">

		<tr>
			<td style="width: 10%;">序号</td>
			<td style="width: 10%;">图名图号 </td>
			<td style="width: 40%;">审定意见</td>
			<td style="width: 40%;">设计回复</td>
		</tr>
		<tr style="height: 45px" ng-repeat="Validate in vm.validateList3">
			<td ng-bind="$index+1"></td>
			<td ng-bind="Validate.drawNo"></td>
			<td ng-bind="Validate.markLevel"></td>
			<td ng-bind="Validate.markAnswer"></td>
		</tr>

	</table>


	<table class="print_table">
		<tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
			<td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="node.userName" style="margin-right: 10px;"></span>
				<span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
			</td>
			<td style=" border-top: none;width: 20%;height:45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
				<span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
			</td>
		</tr>
	</table>
</div>
</div>