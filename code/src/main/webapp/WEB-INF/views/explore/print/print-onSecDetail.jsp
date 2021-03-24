
<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">钻探现场安全检查记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width: 8%;height: 45px;"colspan="1">序号</td>
            <td style="width:25%;"> 检 查 项 目 名 称 </td>
			<td style="width: 25%;">检  查  情  况</td>
            <td style="width: 25%;">检  查  时  间  </td>
		    <td style="width: 17%;">备  注</td>

        </tr>

        <tr ng-repeat="OnSecDetail in vm.OnSecDetails">
            <td ng-bind="$index+1"></td>
            <td style="padding-left: 10px;text-align: left;" ng-bind="OnSecDetail.itemName"></td>
            <td style="padding-left: 10px;text-align: left;" ng-bind="OnSecDetail.checkDesc"></td>
            <td ng-bind="OnSecDetail.checkTime"></td>
            <td style="padding-left: 10px;text-align: left;" ng-bind="OnSecDetail.checkRemark"></td>
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