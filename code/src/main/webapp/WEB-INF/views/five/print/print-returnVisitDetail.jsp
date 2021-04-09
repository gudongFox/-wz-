<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="page_index" hidden>
	<p>表单编号：{{vm.printData.tableNo}}</p>
</div>

<div id="print_area" style="display: none;">
    <h3 style="text-align: center;">工程设计回访记录</h3>
    <table class="print_table">
        <tr>
            <td colspan="1" style="width: 20%">工程名称</td>
            <td colspan="2" style="width: 30%" ng-bind="vm.printData.projectName"></td>
			<td colspan="1" style="width: 20%">建设地址</td>
            <td colspan="2" style="width: 30%" ng-bind="vm.printData.constructionAddress"></td>
        </tr>
		<tr>
            <td colspan="1">建设单位</td>
            <td colspan="2" ng-bind="vm.printData.constructionOrg"></td>
			<td colspan="1">竣工日期</td>
            <td colspan="2" ng-bind="vm.printData.constructionFinishTime"></td>
        </tr>
		<tr>
            <td colspan="1">设计完成日期</td>
            <td colspan="1" style="width: 15%"ng-bind="vm.printData.designFinishTime"></td>
			<td colspan="1">回访人</td>
			<td colspan="1" ng-bind="vm.printData.visitor"></td>
			<td colspan="1">回访日期</td>
            <td colspan="1" style="width: 15%" ng-bind="vm.printData.visitTime"></td>
        </tr>
	</table>
	<table class="print_table">
		<tr>
			<td style="border-top: 0px;">序号</td>
			<td style="border-top: 0px;">内容\满意度</td>
			<td style="border-top: 0px;">很满意</td>
			<td style="border-top: 0px;">满意</td>
			<td style="border-top: 0px;">较满意</td>
			<td style="border-top: 0px;">基本满意</td>
			<td style="border-top: 0px;">不满意</td>
		</tr>
		<tr ng-repeat="fiveEdReturnVisitDetail in vm.printData.fiveEdReturnVisitDetails">
			<td style="width: 8%"ng-bind="$index+1"></td>
			<td style="width: 17%;padding-left: 10px;text-align: left;" ng-bind="fiveEdReturnVisitDetail.appraiseType"></td>
			<td style="width: 15%" ng-if="fiveEdReturnVisitDetail.appraiseResult=='很满意'">√</td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult!='很满意'"></td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult=='满意'">√</td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult!='满意'"></td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult=='较满意'">√</td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult!='较满意'"></td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult=='基本满意'">√</td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult!='基本满意'"></td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult=='不满意'">√</td>
			<td style="width: 15%"ng-if="fiveEdReturnVisitDetail.appraiseResult!='不满意'"></td>
		</tr>

		<tr>
			<td colspan="7" style="text-align: left;padding-left: 20px;border-bottom: none">业主对工程设计的建议和意见：</td>
		</tr>
		<tr>
			<td colspan="7" style="height:150px;text-align:left;padding-left: 40px;padding-bottom: 120px;border-bottom: none;border-top: none"
				ng-bind="vm.printData.ownerComment"></td>
		</tr>
		<tr>
			<td colspan="7" style="text-align: right;padding-right: 20px;border-bottom: none;border-top: none">受访人（单位）签章:</td>
		</tr>
		<tr>
			<td colspan="7" style="text-align: right;padding-right: 40px;border-bottom: none;border-top: none" ng-bind="vm.printData.ownerName"></td>
		</tr>

		<tr>
			<td colspan="7" style="text-align:left;padding-left: 20px;border-bottom: none">设计单位处理意见：</td>
		</tr>
		<tr>
			<td colspan="7" style="height:200px;text-align:left;padding-left: 40px;padding-bottom:160px;border-top: none" ng-bind="vm.printData.designSolve"></td>
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
	</table>
        

</div>