<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
	<h3 style="text-align: center;">勘探点性质一览表</h3>
	<p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	
<table class="print_table">
        <tr>
            <td  rowspan="2" colspan="1";style="width:10%;height: 20px;">  </td>
            <td  rowspan="2" colspan="1";style="width:10%;height: 20px;">编号</td>
			<td  colspan="3" rowspan="1";style="width: 30%;height: 10px;">类型</td>
            <td  rowspan="2" colspan="1";style="width:10%;height: 20px;"> 深度（m） </td>
			<td  colspan="2" rowspan="1";style="width: 20%;height: 10px;">取样（件）</td>
			<td  rowspan="2" colspan="1" ;style="width:10%;height: 20px;"> 原位测试（次） </td>
			<td  rowspan="2" colspan="1" ;style="width:10%;height: 20px;">备注 </td>
				
	   <tr>
		    <td style="width:10%;height: 10px;">一般孔</td>
		    <td style="width:10%;height: 10px;">井槽、坑探</td>
		    <td style="width:10%;height: 10px;">控制孔</td>
		    <td style="width:10%;height: 10px;">土/件</td>
	        <td style="width:10%;height: 10px;">岩/组</td>
		</tr>
			
				
        </tr>
	<tr ng-repeat="explorePointDetail in vm.printData.explorePointDetails">
		<td ng-bind="$index+1"></td>
		<td style="height: 5px;" ng-bind="explorePointDetail.itemCode"> </td>
		<td style="height: 5px;" ng-if="explorePointDetail.itemType=='一般孔'"> √</td>
		<td style="height: 5px;" ng-if="explorePointDetail.itemType!='一般孔'"> </td>
		<td style="height: 5px;" ng-if="explorePointDetail.itemType=='井槽、坑探'"> √</td>
		<td style="height: 5px;" ng-if="explorePointDetail.itemType!='井槽、坑探'"> </td>
		<td style="height: 5px;" ng-if="explorePointDetail.itemType=='控制孔'"> √</td>
		<td style="height: 5px;" ng-if="explorePointDetail.itemType!='控制孔'"> </td>
		<td style="height: 5px;" ng-bind="explorePointDetail.itemHeight"> </td>
		<td style="height: 5px;" ng-bind="explorePointDetail.itemSoilsample"></td>
		<td style="height: 5px;" ng-bind="explorePointDetail.itemRocksample"></td>
		<td style="height: 5px;" ng-bind="explorePointDetail.itemTest"> </td>
		<td style="height: 5px;" ng-bind="explorePointDetail.itemRemark"> </td>
	</tr>
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
</table>
		<table class="print_table">
        <tr>
            <td style="text-align: left;width: 100%;border-top: none;">注：具体取样或测试位置可另外附表说明中风化。</td>

	</table>	
	</div>