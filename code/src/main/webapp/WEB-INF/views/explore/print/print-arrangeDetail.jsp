<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">勘察与开发策划书</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">项目名称</td>
            <td style="width: 40%;" colspan="2" ng-bind="vm.printData.projectName"></td>
			<td style="width: 15%;height: 45px;">工 程 号</td>
            <td style="width: 30%;" ng-bind="vm.printData.projectNo"></td>
        </tr>
        <tr>
            <td style="height: 45px">建设单位</td>
            <td colspan="2" ng-bind="vm.printData.constructionOrg"></td>
            <td style="width: 15%;">联 系 人</td>
            <td style="width: 15%;" ng-bind="vm.printData.constructionLink">    </td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td colspan="7" style="border-top: none;">勘察和开发阶段策划</td>
        </tr>
        <tr>
            <td style="height: 45px;width: 16%;"colspan="1">专业</td>
            <td style="width: 28%;"colspan="2">野外测量</td>
            <td style="width: 28%;"colspan="2">野外钻探</td>
            <td style="width: 28%;"colspan="2">资料整理</td>
        </tr>

	
		<tr>
            <td style="width: 16%;" rowspan="6">参加人员</td>
            <td style="height: 45px;width: 14% ;" colspan="1">测量</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.measureUser"></td>
			<td style="width: 14% ;" colspan="1">钻探机长</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.exploreCommander"></td>
			<td style="width: 14% ;" colspan="1">技术员</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.docWriter"></td>
        </tr>
        <tr>
            <td style="height: 45px;width: 14% ;" colspan="1">校核</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.measureProofread"></td>
			<td style="width: 14% ;" colspan="1">现场录编</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.exploreWriter"></td>
			<td style="width: 14% ;" colspan="1">校核</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.docProofread"></td>

        </tr>
		<tr>
            <td style="height: 45px;width: 14% ;" colspan="1">审核</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.measureAudit"></td>
			<td style="width: 14% ;" colspan="1">现场负责</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.exploreOnCharge"></td>
			<td style="width: 14% ;" colspan="1">审核</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.docAudit"></td>
        </tr>
		
		<tr>
            <td style="height: 45px;width: 14% ;" colspan="1"></td>
			<td style="width: 14% ;" colspan="1" ></td>
			<td style="width: 14% ;" colspan="1">项目负责</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.exploreCharge"></td>
			<td style="width: 14% ;" colspan="1">项目负责</td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.docCharge"></td>
        </tr>
        <tr>
            <td style="height: 45px;width: 16%;"colspan="1">审查人</td>
            <td colspan="2" ng-bind="vm.printData.examineMan"></td>
            <td style="height: 45px;width: 16%;"colspan="1">审定人</td>
            <td colspan="2" ng-bind="vm.printData.approveMan"></td>
        </tr>
		<tr>
		 <td style="height: 45px;width: 16%;">采用软件</td>
		 <td colspan="5" ng-bind="vm.printData.softwareName"></td>
		</tr>
	</table>
		
 
    <table class="print_table">
        <tr>
            <td colspan="7" style="border-top: none;">勘察和开发阶段策划</td>
        </tr>
		<tr>
            <td colspan="2" style="border-top: none;">勘察阶段</td>
			<td  colspan="5" style="border-top: none; text-align:left">
                <span ng-repeat="exploreStage in sysCodes | filter:{codeCatalog:'勘察阶段'}:true">
                    &nbsp;&nbsp;&nbsp<span ng-bind="exploreStage.codeValue"></span>
                    <input type="checkbox" ng-checked="exploreStage.codeValue==vm.printData.exploreStage"/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </span>
            </td>
        </tr>
		<tr>
            <td style="width: 5%;height: 225px" rowspan="5" ;>进</br>度</br>安</br>排</td>
            <td  style="height: 45%;" colspan="1">野外钻探时间</td>
			<td  colspan="6" ng-bind="vm.printData.exploreTime"></td>
        </tr>
        <tr>
            <td  style="height: 45%;" colspan="1">资料整理时间</td>
			<td  colspan="6" ng-bind="vm.printData.docTime"></td>
        </tr>
			<tr>
            <td style="width: 14% ;height: 45%;" colspan="1">计划评审时间</td>
			<td  colspan="6" ng-bind="vm.printData.planTime"></td>
        </tr>
			<tr>
            <td style="width: 14% ;height: 45%;" colspan="1">计划验收时间</td>
			<td  colspan="6" ng-bind="vm.printData.planCheckTime"></td>
        </tr>
			<tr>
            <td style="width: 14% ;height: 45%;" colspan="1">计划确认时间</td>
			<td style="width: 66% ;" colspan="6" ng-bind="vm.printData.planConfirmTime"></td>
    </table>	
    <table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style=" border-top: none;width: 20%;height: 45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
</div>