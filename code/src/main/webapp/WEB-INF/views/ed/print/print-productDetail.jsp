<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none">
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发确认记录</h3>
	<p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 15%">合同号</td>
			<td style="width: 25%"  ng-bind="vm.printData.contractNo"></td>
			<td style="width: 10%">阶段</td>
			<td style="width: 50%"  ng-bind="vm.printData.stageName"></td>
        </tr>
		<tr>
			<td  style="width: 20%">工程名称</td>
			<td colspan="3" style="width: 80%"  ng-bind="vm.printData.projectName"></td>
		</tr>
        <tr> 
            <td >业主单位</td>
			<td   ng-bind="vm.printData.customerName"></td>
			<td >签字盖章</td>
			<td ></td>
        </tr>
		<tr> 
            <td >总包单位</td>
			<td  ng-bind="vm.printData.constructionName"></td>
			<td>签字盖章</td>
			<td></td>
        </tr>
		<tr> 
            <td >已完成合同工作量比例</td>
			<td ng-bind="vm.printData.finishPercent">30%</td>
			<td>备注</td>
			<td  ng-bind="vm.printData.finishRemark"></td>
        </tr>
		<tr>
			<td >本次结算金额</td>
			<td ng-bind="vm.printData.payMoney">20000.00</td>
			<td >备注</td>
			<td  ng-bind="vm.printData.payRemark"> </td>
		</tr>
	</table>
	<table style="border-top:0px" class="print_table">
		<tr style="border-top:0px">
			<td style="border-top:0px;width: 8%">序号</td>
			<td style="border-top:0px;width: 16% ">日期</td>
			<td style="border-top:0px;width: 21%">成品名称</td>
			<td style="border-top:0px;width: 7%">份数/每张份数</td>
			<td style="border-top:0px;width: 16%">交付人</td>
			<td style="border-top:0px;width: 16%">接收人</td>
			<td style="border-top:0px;width: 16%">交付方式</td>
		</tr>
		<tr ng-repeat="productDetail in vm.printData.productDetailList ">
			<td ng-bind="$index+1">1</td>
			<td ng-bind="productDetail.productDate">2019-06-13</td>
			<td ng-bind="productDetail.productName">勘察设计书</td>
			<td ng-bind="productDetail.copyCount">10</td>
			<td ng-bind="productDetail.senderName">熊鑫</td>
			<td ng-bind="productDetail.receiverName">黄南州</td>
			<td ng-bind="productDetail.sendMethod">当面交付</td>
		</tr>
	</table>
	<p style="font-size: 12px;">注：本表由设计分院/设计所填写并保存。</p>
</div>