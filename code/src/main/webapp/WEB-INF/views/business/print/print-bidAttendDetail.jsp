<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area">
    <h2 style="text-align: center;">中冶建工集团有限公司勘察设计研究总院</h2>
    <h3 style="text-align: center;">招标文件标前评审表</h3>
    <p style="float: right;font-size: 12px;"><span>时间：</span><span
            ng-bind="vm.printData.gmtCreate|date:'yyyy年MM月dd日'"></span></p>
    <table class="print_table_big">
        <tr>
            <td colspan='2'>报审单位</td>
            <td style="width:25%" ng-bind="vm.printData.deptName"></td>
            <td style="width:25%;">经办人</td>
            <td style="width:25%;" ng-bind="vm.printData.creatorName"></td>
        </tr>
        <tr>
            <td rowspan="6" style="width:5%">工<br/>程<br/>概<br/>况</td>
            <td>项目名称</td>
            <td ng-bind="vm.printData.projectName"></td>
            <td>招标名称</td>
            <td ng-bind="vm.printData.customerName"></td>
        </tr>
        <tr>
            <td>项目地点</td>
            <td ng-bind="vm.printData.projectAddress"></td>
            <td>合同工期</td>
            <td ng-bind="vm.printData.projectTime"></td>
        </tr>
        <tr>
            <td>工程类别、规模</td>
            <td ng-bind="vm.printData.projectScale"></td>
            <td>质量要求</td>
            <td ng-bind="vm.printData.qualityRequest"></td>
        </tr>
        <tr>
            <td>投标保证金或保函</td>
            <td ng-bind="vm.printData.tenderBond"></td>
            <td>技术标准</td>
            <td ng-bind="vm.printData.techStandard"></td>
        </tr>
        <tr>
            <td>履约保证金或保函</td>
            <td ng-bind="vm.printData.performanceBond"></td>
            <td>经营性质</td>
            <td ng-bind="vm.printData.businessType"></td>
        </tr>
        <tr>
            <td>工程款支付条件</td>
            <td colspan="3" ng-bind="vm.printData.paymentRule"></td>
        </tr>
        <tr>
            <td colspan="2">拟派项目经理</td>
            <td colspan="3" ng-bind="vm.printData.chargeMan"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
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


	<p>备注：<br/>
		1、勘察、设计项目不需要技术质量部、工程管理部、安全生产监督部和总经济师签署意见。<br/>
        2、项目招标文件中若有合同内容,需要合同填报部门签署意见;<br/>
        3、施工和EPC总承包项目需上述所有部门和领导签署意见。</p>
</div>