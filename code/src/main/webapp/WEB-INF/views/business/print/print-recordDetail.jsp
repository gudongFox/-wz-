<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area">
    <h2 style="text-align: center;">中冶建工集团有限公司勘察设计研究总院</h2>
    <h3 style="text-align: center;">项目信息备案</h3>
    <p style="float: right;font-size: 12px;"><span>时间：</span><span
            ng-bind="vm.printData.gmtCreate|date:'yyyy年MM月dd日'"></span></p>
    <table class="print_table">
        <tr>
            <td style="width: 20%;">所属部门</td>
            <td style="width:30%" ng-bind="vm.printData.deptName"></td>
            <td style="width: 20%;">经办人</td>
            <td style="width: 30%;" ng-bind="vm.printData.creatorName"></td>
        </tr>
        <tr>
            <td colspan="4" style="height: 50px">客&nbsp;&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;&nbsp;信&nbsp;&nbsp;&nbsp;&nbsp;息</td>
        </tr>
        <tr>

            <td>客户名称</td>
            <td colspan="3" ng-bind="vm.printData.customerName"></td>
        </tr>
        <tr>
            <td>客户地址</td>
            <td colspan="3" ng-bind="vm.printData.customerAddress"></td>
        </tr>
        <tr>
            <td style="width:20%">联系人</td>
            <td ng-bind="vm.printData.linkMan"></td>
            <td style="width:20%">联系人职务</td>
            <td ng-bind="vm.printData.linkTitle"></td>
        </tr>
        <tr>
            <td>联系人电话</td>
            <td ng-bind="vm.printData.linkTel"></td>
            <td></td>
            <td></td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none">
            <td colspan="4" style="border-top: none;height: 50px">项 &nbsp;&nbsp;&nbsp; 目 &nbsp;&nbsp;&nbsp; 信 &nbsp;&nbsp;&nbsp;
                息
            </td>
        </tr>
        <tr>
            <td>项目名称</td>
            <td colspan="3" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td>项目地址</td>
            <td colspan="3" ng-bind="vm.printData.projectAddress"></td>
        </tr>
        <tr>
            <td style="width: 20%">项目规模</td>
            <td style="width: 30%" ng-bind="vm.printData.projectScale"></td>
            <td style="width: 20%">项目类型</td>
            <td style="width: 30%" ng-bind="vm.printData.projectType"></td>
        </tr>
        <tr>
            <td>业务内容</td>
            <td ng-bind="vm.printData.businessContent"></td>
            <td>投资来源</td>
            <td ng-bind="vm.printData.investSource"></td>
        </tr>
        <tr>
            <td>投资额度</td>
            <td ng-bind="vm.printData.projectInvest"></td>
            <td>是否开具保函</td>
            <td>
                <span ng-if="vm.printData.tenderBond">是</span>
                <span ng-if="!vm.printData.tenderBond">否</span>
            </td>
        </tr>
        <tr>
            <td>保函方式</td>
            <td ng-bind="vm.printData.tenderBondType"></td>
            <td>保函额度</td>
            <td ng-bind="vm.printData.tenderBondMoney"></td>
        </tr>
        <tr>
            <td>参与经营人员</td>
            <td colspan="3" ng-bind="vm.printData.businessUserName"></td>
        </tr>
        <tr>
            <td>计划开标时间</td>
            <td ng-bind="vm.printData.bidPlanOpen"></td>
            <td>实际开标时间</td>
            <td ng-bind="vm.printData.bidRealOpen"></td>
        </tr>
        <tr>
            <td>是否投标</td>
            <td>
                <span ng-bind="vm.printData.attend?'是':'否'"></span>
            </td>
            <td>是否中标</td>
            <td>
                <span ng-bind="vm.printData.win?'是':'否'"></span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none">
            <td style="border-top: none;" colspan="4">各部门意见</td>
        </tr>
        <tr ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="width: 20%" ng-bind="node.activityName"></td>
            <td style="width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="width: 20%" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
    <p style="font-size: 12px;">注：</p>
</div>