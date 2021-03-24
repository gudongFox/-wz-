
<div id="print_area" hidden>
    <h2 style="text-align: center;">中国五洲工程设计集团有限公司</h2>
    <h3 style="text-align: center;">人员安排表</h3>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">项目名称</td>
            <td style="width: 35%;" ng-bind="vm.printData.projectName"></td>
            <td style="width: 15%;">合同号</td>
            <td style="width: 25%;"  ng-bind="vm.printData.contractNo"></td>

        </tr>
        <tr>
            <td >子项</td>
            <td ng-bind="vm.printData.stepName"></td>
            <td >设计阶段</td>
            <td   ng-bind="vm.printData.stageName"></td>
          <%--  <td >编号</td>
            <td ng-bind="vm.printData.formNo"></td>
            <td >时间</td>
            <td ng-bind="vm.printData.gmtModified|date:'yyyy-MM-dd'"></td>--%>
        </tr>
    </table>
    <table class="print_table">
    <tr>
        <td style="width: 5%;border-top: none" rowspan="2">#</td>
        <td style="width: 10%;border-top: none" rowspan="2">专业</td>
        <td colspan="2" style="border-top: none">专业负责人</td>
        <td rowspan="2" style="border-top: none">设计</td>
        <td rowspan="2" style="border-top: none">校核</td>
        <td rowspan="2" style="border-top: none">标准审查</td>
        <td rowspan="2" style="border-top: none">审核</td>
        <td rowspan="2" style="border-top: none">审定</td>
    </tr>
    <tr>
        <td>姓名</td>
        <td>电话</td>
    </tr>
    <tr ng-repeat="user in vm.printData.arrangeUser">
        <td ng-bind="$index+1">1</td>
        <td ng-bind="user.majorName">建筑</td>
        <td ng-bind="user.majorChargeMenName">黄南州</td>
        <td ng-bind="user.majorChargeMenMobile">13983310705</td>
        <td ng-bind="user.designMenName">黄南州</td>
        <td ng-bind="user.proofreadMenName">黄南州</td>
        <td ng-bind="user.criterionExamineMenName">黄南州</td>
        <td ng-bind="user.auditMenName">黄南州</td>
        <td ng-bind="user.approveMenName">黄南州</td>
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
    <p style="font-size: 12px;">注:每个部门可根据审批权限及项目组构成，对本表的内容适当调整。</p>
    <div style="page-break-before:always">
        <h2 style="text-align: center;">中国五洲工程设计集团有限公司</h2>
        <h3 style="text-align: center;">项目进度计划表</h3>

        <table class="print_table">
            <tr>
                <td rowspan="5" style="width: 5%">主<br>计<br>划</td>
                <td style="width: 15%;">项目名称</td>
                <td style="width: 35%;" ng-bind="vm.printData.projectName"></td>
                <td style="width: 15%;">合同号</td>
                <td style="width: 30%;"  ng-bind="vm.printData.contractNo"></td>

            </tr>
            <tr>
                <td >子项</td>
                <td ng-bind="vm.printData.stepName"></td>
                <td >设计阶段</td>
                <td  ng-bind="vm.printData.stageName"></td>
                <%--<td >编号</td>
                <td ng-bind="vm.printData.formNo"></td>
                <td >时间</td>
                <td ng-bind="vm.printData.gmtModified|date:'yyyy-MM-dd'"></td>--%>
            </tr>
            <tr>
                <td >方案讨论日期</td>
                <td ng-bind="vm.printData.planDiscussTime"></td>
                <td >交总师时间</td>
                <td ng-bind="vm.printData.submitTotalDesignerTime"></td>
            </tr>
            <tr>
                <td >设计评审方式</td>
                <td ng-bind="vm.printData.designReviewType"></td>
                <td >设计评审日期</td>
                <td ng-bind="vm.printData.designReviewTime"></td>
            </tr>
            <tr>
                <td >验收日期</td>
                <td ng-bind="vm.printData.acceptTime"></td>
                <td >发图日期</td>
                <td ng-bind="vm.printData.sendPictureTime"></td>
            </tr>
        </table>
        <table class="print_table">
            <tr>
                <td style="width: 5%;border-top: none" >#</td>
                <td style="width: 8%;border-top: none" >专业</td>
                <td style="width: 10%;border-top: none" ng-bind="major" ng-repeat="major in vm.printData.planTimes.majors" >专业</td>
            </tr>
            <tr ng-repeat="list in vm.printData.planTimes.listPlanDto">
                <td ng-bind="$index+1">建筑</td>
                <td ng-bind="list.majorName">建筑</td>
                <td  ng-repeat=" value in list.planValues track by $index" ng-bind="value"></td>

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
        <p style="font-size: 12px;">注:每个部门可根据审批权限及项目组构成，对本表的内容适当调整。</p>

    </div>
</div>
