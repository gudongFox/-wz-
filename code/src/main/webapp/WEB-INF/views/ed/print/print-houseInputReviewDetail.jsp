<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发输入清单及评审记录（审核意见)</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 20%">合同号</td>
            <td style="width: 20%" ng-bind="vm.printData.contractNo"></td>
			<td style="width: 15%">工程名称</td>
            <td  ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td  style="width: 20%;border-bottom: none">阶段</td>
            <td colspan="3" style="border-bottom: none">
                <span ng-repeat="stageName in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="stageName.codeValue"></span>
                    <input type="checkbox" ng-checked="stageName.codeValue==vm.printData.stageName"/>
                </span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr ng-repeat="edComment in vm.printData.edComments">
            <td colspan="2" style="width:20%; height:50px;padding-left: 10px;text-align: left;" ng-bind="edComment.baseName"></td>
            <td colspan="4" style="height:50px;"ng-bind="edComment.baseComment"></td>
        </tr>
	</table>
    <table class="print_table" ng-if="vm.printData.stageName=='施工图设计阶段'">
       <tr >
           <td style="width:20%;border-top: none" rowspan="2">岩土工程勘察报告</td>
           <td style="width:40%;text-align: left;border: none"><span>建议基础放在 &nbsp;</span><span ng-bind="vm.printData.foundationSoil"></span><span>&nbsp;土层</span></td>
           <td style="width:40%;text-align: left;border-top: none;border-left: none;border-bottom: none" ><span>承载力&nbsp;</span><span ng-bind="vm.printData.foundationBear"></span><span>&nbsp;KN/㎡</span></td>
       </tr>
        <tr >
            <td style="width:40%;text-align: left;border-top: none;border-right: none"><span>建议选用的基础类型： &nbsp;&nbsp;</span><span ng-bind="vm.printData.foundationType"></span></td>
            <td style="width:40%;text-align: left;border-top: none;border-left: none" ></td>
        </tr>
    </table>
    <table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="width: 20%;border-top: none" ng-bind="node.activityName"></td>
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

	<p style="font-size: 12px;  margin-left:20px">注：① 项目负责人应将各部门批文/意见填写在此表上。<br>
    ② 各专业负责人应协同项目负责人对输入要求进行评审，确保各专业明确政府各部门批文和顾客的要求。<br>
③ 若批文未到，又需先行设计时，项目负责人和各专业审核人应提出输入意见， 待批文到后再同各专业进行会签。

</p>

     

</div>