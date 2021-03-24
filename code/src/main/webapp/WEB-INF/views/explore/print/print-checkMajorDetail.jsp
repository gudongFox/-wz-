<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
	<h3 style="text-align: center;">野外各专业检查验收表</h3>
	<p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width: 20%;height: 45px">项目名称</td>
            <td style="width: 30%;" ng-bind="vm.printData.projectName">  </td>
			<td style="width: 20%;">工程号</td>
            <td style="width: 30%;" ng-bind="vm.printData.projectNo">  </td>
        </tr>

	</table>	
		<table class="print_table">
        <tr>
            <td style="width: 20%;height: 45px;border-top:none;border-bottom:none;">勘察阶段</td>
            <td style="width: 80%;border-top:none;border-bottom:none;">可研勘察<input type="checkbox"/>
				<span ng-repeat="stageNames in sysCodes | filter:{codeCatalog:'勘察阶段'}:true">
                    &nbsp;&nbsp;&nbsp<span ng-bind="stageNames.codeValue"></span>
                    <input type="checkbox" ng-checked="stageNames.codeValue==vm.printData.stageName"/>&nbsp;
				</span>
			</td>
       </tr>
	</table>
	
	<table class="print_table">
        <tr>
            <td style="width: 20%;height: 45px;border-bottom:none;">项目负责人</td>
            <td style="width: 17%;border-bottom:none;" ng-bind="vm.printData.projectCharge">  </td>
			<td style="width: 16%;border-bottom:none;">校核人</td>
            <td style="width: 17%;border-bottom:none;" ng-bind="vm.printData.checkUser">  </td>
			<td style="width: 14%;border-bottom:none;">检查日期</td>
            <td style="width: 16%;border-bottom:none;" ng-bind="vm.printData.checkTime">  </td>
       </tr>
	</table>
	<table class="print_table">
        <tr>
            <td style="width: 20%;height: 480px;" rowspan="12">检</br>查</br>内</br>容</td>
            <td style="width: 50%;height: 120px;text-align: left;" rowspan="3">
				<span style="float:left;padding-top:5px;padding-left:20px;padding-right: 20px;text-align: left;" > 一、勘察测量：<p ng-bind-html="vm.explorehtml"></p></span>
			</td>
			 <td style="width: 30%;height: 40px;">合格<input type="checkbox"  ng-checked="vm.printData.exploreResult=='合格'"/></td>
		<tr>

			 <td style="width: 30%;height: 40px;">基本合格<input type="checkbox" ng-checked="vm.printData.exploreResult=='基本合格'"/></td>
	</tr>
		<tr>

			 <td style="width: 30%;height: 40px;">不合格 <input type="checkbox" ng-checked="vm.printData.exploreResult=='不合格'"/></td>
		</tr>

			 <tr>
			  <td  style="width: 50%;height: 120px;text-align: left;" rowspan="3">
				  <span style="float:left;padding-top:5px;padding-left:20px;padding-right: 20px;text-align: left;">二、野外原位测试：<p ng-bind-html="vm.outhtml"></p></span>

			  </td>
			
			
			
			 <td style="width: 30%;height: 40px;">合格<input type="checkbox" ng-checked="vm.printData.outResult=='合格'"/></td>
			<tr><td style="width: 30%;height: 40px;">基本合格<input type="checkbox" ng-checked="vm.printData.outResult=='基本合格'"/></td></tr>
			 <tr><td style="width: 30%;height: 40px;">不合格 <input type="checkbox" ng-checked="vm.printData.outResult=='不合格'"/></td></tr>
			 
			 </tr>

			  <td rowspan="3";style="width: 50%;height: 120px;text-align: left;">
				  <span style="float:left;padding-top:5px;padding-left:20px;padding-right: 20px;text-align: left;">三、野外地质钻探：<p ng-bind-html="vm.dightml"></p></span>
				</td>
			 <td style="width: 30%;height: 40px;">良好   <input type="checkbox" ng-checked="vm.printData.digResult=='良好'"/></td>
			 
			 <tr><td style="width: 30%;height: 40px;">一般 <input type="checkbox" ng-checked="vm.printData.digResult=='一般'"/></td></tr>
			
			 <tr><td style="width: 30%;height: 40px;">差  <input type="checkbox" ng-checked="vm.printData.digResult=='差'"/></td></tr>
			 
			 
		 </tr>

<tr>
	<td  style="width: 50%;height: 120px;text-align: left;" rowspan="3">
	        <span style="float:left;padding-top:5px;padding-left:20px;padding-right: 20px;text-align: left;">四、原始记录整理：</br><p ng-bind-html="vm.originalhtml"></p></span>
			</td>

		<td style="width: 30%;height: 40px;">良好   <input type="checkbox" ng-checked="vm.printData.originalResult=='良好'"/></td>

		<tr><td style="width: 30%;height: 40px;">一般 <input type="checkbox" ng-checked="vm.printData.originalResult=='一般'"/></td></tr>

		<tr><td style="width: 30%;height: 40px;">差  <input type="checkbox" ng-checked="vm.printData.originalResult=='差'"/></td></tr>
			 </tr>
	</table>
		<table class="print_table">
        <tr>
            <td style="width: 100%;height: 180px;text-align: left;border-top:none;padding-left: 10px;padding-right: 10px;padding-top: 10px">
				<span style="float: left;text-align: left;padding-right: 15px;padding-left: 15px;padding-top: 5px;padding-bottom: 5px" ng-bind="'处理意见：'+vm.printData.reviewResult"></span></td>
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	