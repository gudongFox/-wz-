<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">勘察各专业协作表</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width: 20%;height: 60px;">工程名称</td>
            <td style="width: 30%;" ng-bind="vm.printData.projectName">  </td>
			<td style="width: 20%;">工程号</td>
            <td style="width: 30%;" ng-bind="vm.printData.projectNo">  </td>
        </tr>

	</table>	
	<table class="print_table">
        <tr>
            <td style="width: 20%;height: 60px;border-top: none;border-bottom: none">勘察阶段</td>
            <td style="width: 80%;border-top: none;border-bottom: none">
                <span ng-repeat="stageNames in sysCodes | filter:{codeCatalog:'勘察阶段'}:true">
                    &nbsp;&nbsp;&nbsp<span ng-bind="stageNames.codeValue"></span>
                    <input type="checkbox" ng-checked="stageNames.codeValue==vm.printData.stageName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </span>
			</td>

</tr>
	</table>
	<table class="print_table">
        <tr>
            <td style="width: 20%;height: 60px;">提交专业</td>
            <td style="width: 30%;" ng-bind="vm.printData.sourceMajor">  </td>
			<td style="width: 20%;">提交人</td>
            <td style="width: 30%;" ng-bind="vm.printData.sourceUser">  </td>
        </tr>
  <tr>
            <td style="width: 20%;height: 60px;">提交时间</td>
            <td style="width: 30%;" ng-bind="vm.printData.sourceTime">  </td>
			<td style="width: 20%;">专业负责人</td>
            <td style="width: 30%;" ng-bind="vm.printData.sourceMajorCharge">  </td>
        </tr>

<tr>
            <td style="width: 20%;height: 60px;">接收专业</td>
            <td style="width: 30%;" ng-bind="vm.printData.destMajor">  </td>
			<td style="width: 20%;"> 接收人</td>
            <td style="width: 30%;" ng-bind="vm.printData.destReceiver">  </td>
        </tr>

<tr>
            <td style="width: 20%;height: 60px;">接收时间</td>
            <td style="width: 30%;" ng-bind="vm.printData.destTime">  </td>
			<td style="width: 20%;">专业负责人</td>
            <td style="width: 30%;" ng-bind="vm.printData.destMajorCharge">  </td>
        </tr>

	</table>	
	<table class="print_table">
        <tr>
            <td style="text-align: left;width: 100%;border-top: none;border-bottom: none">
                技术要求及内容：
            </td>
        </tr>
        <tr>
            <td style="text-align: left;height:200px;border-top: none"ng-bind-html="vm.html"></td>
        </tr>
        <tr>
            <td style="text-align: left;height:50px;border-top: none"ng-bind="'提交内容： '+vm.printData.sourceContent"></td>
        </tr>
	</table>
	<table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="border-top: none;width: 20%;height: 60px" ng-bind="node.activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style=" border-top: none;width: 20%;height:60px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>

	</table>	
	</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	