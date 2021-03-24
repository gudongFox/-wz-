<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">应急情况演练记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	
	<table class="print_table">
        <tr>
            <td style="width: 30%;height: 45px;"colspan="1">演习应急情况</td>
            <td style="width: 70%;text-align: left;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.actName"></td>
        </tr>
		
		<tr>
            <td style="height: 45px;"colspan="1">参与人员</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.attendUser"></td>
        </tr>
		
		<tr>
            <td style="height: 230px;"colspan="1">预演安排</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.actDesc">  </td>
        </tr>
		
		<tr>
            <td style="height: 230px;"colspan="1">应急情况处理过程</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.actProcess"></td>
        </tr>
		
		<tr>
            <td style="height: 230px;"colspan="1">演习效果</td>
            <td style="text-align: left;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.actResult"></td>
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