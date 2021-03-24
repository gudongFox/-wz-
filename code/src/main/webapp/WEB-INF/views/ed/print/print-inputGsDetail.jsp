<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发输入清单及评审记录(结构)</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 10%;">合同号</td>
            <td style="width: 15%;" ng-bind="vm.printData.contractNo"></td>
            <td style="width: 10%;">工程名称</td>
            <td ng-bind="vm.printData.projectName"></td>
            <td style="width: 10%;">阶段</td>
            <td style="width: 15%;" ng-bind="vm.printData.stageName"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td colspan="6" style="border-top: none">结 构 专 业 输 入 要 求</td>
        </tr>
        <tr>
            <td style="width: 10%" rowspan="5">结构类型<br/>与体系</td>
            <td style="width: 15%;">混凝土</td>
            <td style="text-align: left">
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'混凝土结构类型'}:true" class="margin-right-10">
                     <input type="checkbox" ng-checked="quality.codeValue==vm.printData.betonType"/>
                     <span ng-bind="quality.codeValue"></span>
                    <br ng-if="$index==3"/>
                </span>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;">混合</td>
            <td style="text-align: left">
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'混合结构类型'}:true" class="margin-right-10">

                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.mixType"/>
                     <span ng-bind="quality.codeValue"></span>
                    <br ng-if="$index==3"/>
                </span>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;">砌体</td>
            <td style="text-align: left">
                  <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'砌体结构类型'}:true" class="margin-right-10">

                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.brickworkType"/>
                      <span ng-bind="quality.codeValue"></span>
                    <br ng-if="$index>3"/>
                </span>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;">钢</td>
            <td style="text-align: left">
                 <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'钢结构类型'}:true" class="margin-right-10">

                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.steelType"/>
                     <span ng-bind="quality.codeValue"></span>
                    <br ng-if="$index>3"/>
                </span>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;">其他</td>
            <td ng-bind="vm.printData.otherType"></td>
        </tr>
        <tr>
            <td colspan="2">设防烈度/基本加速度</td>
            <td colspan="4" style="text-align: left;">
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设防烈度/基本加速度'}:true" class="margin-right-10">
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.intensityLevel"/>
                    <span ng-bind="quality.codeValue"></span>

                </span>
            </td>
        </tr>
        <tr>
            <td colspan="2">建筑抗震类别</td>
            <td colspan="4" style="text-align: left;">
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'建筑抗震类别'}:true" class="margin-right-20">
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.earthquakeLevel"/>
                    <span ng-bind="quality.codeValue"></span>
                </span>
            </td>
        </tr>
        <tr>
            <td colspan="2">基本风压/重现期<br/>（KN/㎡）/年</td>
            <td colspan="4" style="text-align: left">
                <span>地名：</span><span ng-bind="vm.printData.windAddress"></span>&nbsp;
               <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> <span ng-bind="vm.printData.windLevel"></span><span>KN/㎡</span>
            </td>
        </tr>
        <tr>
            <td colspan="6">结 构 专 业 法 律 法 规 输 入 清 单</td>
        </tr>
        <tr>
            <td style="height: 300px; " colspan="6" >
                <p style="text-align: left;margin-left: 50px;margin-bottom: 200px" ng-bind="vm.printData.lawList"></p>
            </td>
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
    <p style="font-size: 12px;">注：①
        结构专业负责人根据项目的输入要求，确定项目的结构的基本特性，并填入“结构专业输入要求”栏；②结构专业负责人向结构审核人汇报项目情况，并对结构专业的输入要求进行评审；</p>
</div>