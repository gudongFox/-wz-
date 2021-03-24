<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_areaChief" style="display: none;" >
    <h3 style="text-align: center;">总设计师资格认定申请表</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'时间: '+vm.printData.gmtModified"></p>
    <table class="print_table">
        <tr>
            <td style="width: 10%">单位</td>
            <td style="width: 20%" ng-bind="vm.printData.deptName"></td>
            <td style="width: 10%">姓名</td>
            <td style="width: 15%" ng-bind="vm.printData.userName"></td>
			<td style="width: 20%">职务</td>
			<td style="width: 25%" ng-bind="vm.printData.position"></td>
        </tr>
		<tr>
			<td colspan="2">毕业院校及所学专业</td>
			<td colspan="4" ng-bind="vm.printData.graduateCollege+'  '+vm.printData.graduateMajor"></td>
		</tr>
		<tr>
			<td colspan="2">现职称及任职时间</td>
			<td colspan="2" ng-bind="vm.printData.title+'  '+vm.printData.titleTime"></td>
			<td>执业注册资格</td>
			<td ng-bind="vm.printData.certification"></td>
		</tr>
		<tr>
			<td colspan="2">现从事专业及起始时间</td>
			<td colspan="2" ng-bind="vm.printData.major+'  '+vm.printData.majorTime"></td>
			<td>现技术工作岗位</td>
			<td ng-bind="vm.printData.workPost"></td>
		</tr>
		<tr>
			<td colspan="2">现承担设计项目类型</td>
			<td colspan="2" ng-bind="vm.printData.projectTypeNow"></td>
			<td>申请承担设计项目类型</td>
			<td ng-bind="vm.printData.projectTypeApply"></td>
		</tr>
    </table>
    <table class="print_table">
        <tr style="">
			<td style="height: 120px;width: 10%;border-top: 0px;">业绩表现</td>
			<td style="width: 90%;border-top: 0px;text-align:left">
				<div style="margin-left: 10px;margin-top: 0px">被考核人在现技术岗位承担的主要工作内容、所起作用、完成情况及效果：</div>
				<div style="margin-left: 30px;margin-top: 20px" ng-bind="vm.printData.performance"></div>
			</td>
		</tr>
		<tr >
			<td style="height: 120px;width: 10%;border-top: 0px;">申报单位意见</td>
			<td style="width: 90%;border-top: 0px;text-align:left">
				<div style="margin-left: 30px;margin-top: 10px" ng-bind="vm.printData.departmentOpinion"></div>
			</td>
		</tr>
		<tr >
			<td style="height: 120px;width: 10%;border-top: 0px;">初审意见</td>
			<td style="width: 90%;border-top: 0px;text-align:left">
				<div style="margin-left: 10px;margin-top: 0px">是否符合基本任职条件:</div>
				<div style="margin-left: 30px;margin-top: 20px" ng-bind="vm.printData.firstOpinion"></div>
			</td>
		</tr>
		<tr >
			<td style="height: 120px;width: 10%;border-top: 0px;">考核评审意见</td>
			<td style="width: 90%;border-top: 0px;text-align:left">
				<div style="margin-left: 10px;margin-top: 0px">是否具备履职能力:</div>
				<div style="margin-left: 30px;margin-top: 20px" ng-bind="vm.printData.assessOpinion"></div>
			</td>
		</tr>
		<tr >
			<td style="height: 120px;width: 10%;border-top: 0px;">审定意见</td>
			<td style="width: 90%;border-top: 0px;text-align:left">
				<div style="margin-left: 30px;margin-top: 0px" ng-bind="vm.printData.approveOpinion"></div>
			</td>
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