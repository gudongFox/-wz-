<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">岩土工程勘察成果文件归档清单</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号:'+vm.printData.formNo"></p>
	
	<table class="print_table">
        <tr>
            <td style="width: 10%;height: 50px">工程号</td>
            <td style="width: 35%;" ng-bind="vm.printData.projectNo">  </td>
			<td style="width: 10%;">项目名称</td>
            <td style="width: 45%;" ng-bind="vm.printData.projectName">  </td>
        </tr>

	</table>	
	<table class="print_table">
		<tr>
		     <td style="width: 10%;height: 50px;border-top: none" >序号</td>
			<td colspan="2" style="width: 45%;border-top: none;">程序性资料</td>
            <td colspan="2" style="width: 45%;border-top: none;">技术性资料</td>
        </tr>
        <tr ng-repeat="docDetail in vm.printData.docDetails">
            <td style="height: 50px" ng-bind="$index+1"></td>
            <td ng-bind="docDetail.procDes"></td>
            <td>有<input type="checkbox" ng-checked="procSatisfy"/>&nbsp;&nbsp;无<input type="checkbox" ng-checked="!procSatisfy"/></td>
            <td ng-bind="docDetail.techDes"></td>
            <td>有<input type="checkbox" ng-checked="techSatisfy"/>&nbsp;&nbsp;无<input type="checkbox" ng-checked="!techSatisfy"/></td>

        </tr>
	</table>	
	<table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="border-top: none;width: 20%;height: 50px" ng-bind="node.activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style=" border-top: none;width: 20%;height:50px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
	</table>
		</div>
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		