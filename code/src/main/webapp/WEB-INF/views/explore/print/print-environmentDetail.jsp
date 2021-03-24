<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;" >
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">重要环境因素清单</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	
	    <table class="print_table">
        <tr>
            <td style="width: 5%;height: 45px;"colspan="1">序号</td>
            <td style="width: 19%;">  产品/活动/服务</td>
			<td style="width: 19%;">环境因素名称</td>
            <td style="width: 19%;"> 环 境 影 响 </td>
		    <td style="width: 19%;">时态/状态</td>
		    <td style="width: 19%;">管 理 方 式</td>
          
        </tr>
			<tr ng-repeat="environment in vm.printEnvironmentDetails">
				<td ng-bind="$index+1"></td>
				<td ng-bind="environment.productName"></td>
				<td ng-bind="environment.environmentName"></td>
				<td ng-bind="environment.environmentImpact"></td>
				<td ng-bind="environment.environmentState"></td>
				<td ng-bind="environment.environmentMis"></td>
			</tr>


			</tr>
	</table >
	<table class="print_table">
		<tr>
			<td style="width: 13%;border-top: none;">制表人:
			</td>
			<td style="width: 20%;border-top: none;" ng-bind="vm.printData.creatorName+' '+vm.printData.creatorTime">
			</td>
			<td style="width: 13%;border-top: none;">审核人:
			</td>
			<td style="width: 20%;border-top: none;" ng-bind="vm.printData.approveUserName+' '+vm.printData.approveTime">
			</td>
			<td style="width: 13%;border-top: none;">批准人:
			</td>
			<td style="width: 21%;border-top: none;" ng-bind="vm.printData.approveUserName+' '+vm.printData.approveTime">
			</td>
		</tr>

	</table>
</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	