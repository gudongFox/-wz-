<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area">
    <h2 style="text-align: center;">中冶建工集团有限公司勘察设计研究总院</h2>
    <h3 style="text-align: center;">项目投标报名申请表 </h3>
    <table class="print_table">
        <tr>
            <td  style="width: 20%;height: 60px;"><span style="color: red">★</span>投标所室</td>
            <td  style="width: 30%" ng-bind="vm.printData.deptName"></td>
			<td  style="width: 20%"><span style="color: red">★</span>经办人</td>
            <td  style="width: 30%" ng-bind="vm.printData.creatorName"></td>
        </tr>
        <tr>
            <td  style="width: 20%;height: 60px;"><span style="color: red">★</span>项目名称</td>
            <td colspan="3" ng-bind="vm.printData.projectName" ></td>
        </tr>
        <tr>
            <td  style="width: 20%;height: 60px;"><span style="color: red">★</span>客户名称</td>
            <td colspan="3"  ng-bind="vm.printData.customerName"></td>
        </tr>
        <tr>
            <td  style="width: 20%;height: 60px;">代理公司名称</td>
            <td colspan="3" ng-bind="vm.printData.agentName" ></td>
        </tr>
        <tr >
            <td  style="width: 20%;height: 60px;">项目规模</td>
            <td colspan="3" ng-bind="vm.printData.projectScale" ></td>
        </tr>
        <tr>
            <td  style="width: 20%;height: 60px;"><span style="color: red">★</span>业务内容</td>
            <td  style="width: 30%" ng-bind="vm.printData.projectType"></td>
            <td  style="width: 20%"><span style="color: red">★</span>经营模式</td>
            <td  style="width: 30%" ng-bind="vm.printData.businessType"></td>
        </tr>
        <tr>
            <td  style="width: 20%;height: 60px;"><span style="color: red">★</span>投标方式</td>
            <td  style="width: 30%;" ng-bind="vm.printData.attendType"></td>
            <td  style="width: 20%;">开标时间</td>
            <td  style="width: 30%;" ng-bind="vm.printData.openTime"></td>
        </tr>
	</table>
    <table class="print_table">
        <tr ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="width: 20%;height: 60px;border-top: none" ng-bind="node.activityName"></td>
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
	<p style="font-size: 15px;color: red">
        备注：1、以上内容如果是比选、邀标或者未挂网项目，只需要填写带星号的项目，如果是已经挂网的项目以上所有内容都必须按填写完整；<br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2、对于单纯的勘察设计项目可否设置成不需要分管领导签字，如果是施工或者EPC项目才需要分管领导签字
    </p>

</div>