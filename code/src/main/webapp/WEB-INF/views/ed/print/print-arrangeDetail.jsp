<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发策划书</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td>合同号</td>
            <td colspan="2" ng-bind="vm.printData.contractNo"></td>
            <td style="width: 10%;">工程名称</td>
            <td colspan="3" ng-bind="vm.printData.projectName">重庆易行科技大厦工程</td>
        </tr>
        <tr>
            <td style="width: 10%;">工程等级</td>
            <td ng-bind="vm.printData.projectLevel" style="width: 10%;"></td>
            <td style="width: 10%;">工程类别</td>
            <td colspan="2">
                <span ng-if="vm.printData.edType.indexOf('房建')>=0" ng-repeat="quality in sysCodes | filter:{codeCatalog:'房建工程类别'}:true">
                    <span ng-bind="quality.codeValue" style="font-size: 12px;"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.projectType"/>
                </span>

                <span ng-if="vm.printData.edType.indexOf('市政')>=0" ng-repeat="quality in sysCodes | filter:{codeCatalog:'市政工程类别'}:true">
                    <span ng-bind="quality.codeValue" style="font-size: 12px;"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.projectType" />
                </span>
            </td>
            <td style="width: 10%;">建设地点</td>
            <td ng-bind="vm.printData.constructionAddress" style="width: 20%;font-size: 12px;"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="border-top: 0px;" width="4%" rowspan="3">审批</td>
            <td style="border-top: 0px;" width="12%">岗位\专业</td>
            <td style="border-top: 0px;" ng-repeat="majorName in vm.printData.majorNames track by $index" ng-bind="majorName"></td>
        </tr>
        <tr>
            <td>审 定 人</td>
            <td ng-repeat="approve in vm.printData.approves track by $index" ng-bind="approve"></td>
        </tr>
        <tr>
            <td>审 核 人</td>
            <td ng-repeat="audit in vm.printData.audits track by $index" ng-bind="audit"></td>
        </tr>
        <tr>
            <td rowspan="4">设计团队</td>
            <td>专业负责人</td>
            <td style="border-top: 0px" ng-repeat="majorCharge in vm.printData.majorCharges track by $index" ng-bind="majorCharge"></td>
        </tr>
        <tr>
            <td>校队人</td>
            <td ng-repeat="proofread in vm.printData.proofreads track by $index" ng-bind="proofread"></td>
        </tr>
        <tr>
            <td>设计人</td>
            <td ng-repeat="design in vm.printData.designs track by $index" ng-bind="design"></td>
        </tr>
        <tr>
            <td>后期服务人</td>
            <td ng-repeat="service in vm.printData.services track by $index" ng-bind="service"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width: 20%;border-top: none;">设计规模</td>
            <td style="border-top: none;">
                    <span ng-if="vm.printData.edType.indexOf('房建')>=0" style="text-align: left">
                         <span >建筑面积:&nbsp;</span>
                         <span ng-bind="vm.printData.designScaleArea">
                         </span><span>平方米&nbsp;   &nbsp;</span>
                    </span>

                    <span ng-if="vm.printData.edType.indexOf('房建')>=0" style="text-align: center">
                             <span>层数: &nbsp;</span>
                             <span ng-bind="vm.printData.designScaleFloor"></span>
                             <span> 层 &nbsp;   &nbsp;</span>
                    </span>

                   <span ng-if="vm.printData.edType.indexOf('市政')>=0" style="text-align: left">
                         <span >设计速度:&nbsp;</span>
                         <span ng-bind="vm.printData.designScaleSpeed">
                         </span><span>Km/h&nbsp;   &nbsp;</span>
                    </span>

                    <span ng-if="vm.printData.edType.indexOf('市政')>=0" style="text-align: center">
                             <span>里程长度: &nbsp;</span>
                             <span ng-bind="vm.printData.designScaleLength"></span>
                             <span> Km &nbsp;   &nbsp;</span>
                    </span>

                    <span ng-if="vm.printData.edType.indexOf('市政')>=0" style="text-align: center">
                             <span>路幅宽度: &nbsp;</span>
                             <span ng-bind="vm.printData.designScaleWidth"></span>
                             <span> m &nbsp;   &nbsp;</span>
                    </span>
                    <span style="text-align: right">
                        <span>投资额:&nbsp;</span>
                        <span ng-bind="vm.printData.designScaleInvest"></span>
                        <span>万元</span>
                    </span>
            </td>
        </tr>
        <tr>
            <td>设计阶段</td>
            <td>
                 <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.stageName"/>

                </span>
            </td>
        </tr>
        <tr>
            <td>设计内容</td>
            <td>
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'房建工程专业'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="vm.printData.designMajorList.indexOf(quality.codeValue)>=0"/><span>&nbsp;&nbsp;</span>
                    <br ng-if="$index==3"/>
                </span>
            </td>
        </tr>
        <tr >
            <td>合作设计</td>
            <td style="text-align: left">
                <div style="">
                    <span>合作单位：</span><span ng-bind="vm.printData.cooperationName"></span>
                </div>
                <br>
                <div>
                    <span>分工及要求：</span><span ng-bind="vm.printData.cooperationContent"></span>
                </div>
            </td>
        </tr>

        <tr ng-if="vm.printData.edType.indexOf('市政')>=0">
            <td>质量要求</td>
            <td ng-bind="vm.printData.designQuality"></td>
        </tr>
        <tr>
            <td>"四新"要求</td>
            <td ng-bind="vm.printData.fourNewRule"></td>
        </tr>
        <tr>
            <td>特殊设计验证方法</td>
            <td>
                 <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'特殊设计验证方法'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.specialValidate"/>
                </span>
            </td>
        </tr>
        <tr style="border-bottom: none">
            <td >设计评审安排</td>
            <td style="text-align: left">
                <div style="text-align: center">
                    <span></span>
                    <span>
                        <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设计评审安排'}:true">
                        <span ng-bind="quality.codeValue"></span>
                        <input type="checkbox" ng-checked="quality.codeValue==vm.printData.approveArrange"/>
                    </span>
                    </span>
                </div>
                <br>
                <div>
                    <span>评审阶段：</span>
                    <span>
                        <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.stageName"/>
                </span>
                    </span>
                </div>
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


    <p style="font-size: 12px;">注:本表由项目负责人和技术质量部负责人填写。</p>
</div>