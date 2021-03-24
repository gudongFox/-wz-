<div id="print_area" style="display: none">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发输入清单及评审记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 10%">合同号</td>
            <td colspan="2" ng-bind="vm.printData.contractNo">ed-03</td>
            <td style="width: 10%">工程名称</td>
            <td colspan="4" ng-bind="vm.printData.projectName">市政项目</td>
            <td style="width: 10%">阶段</td>
            <td colspan="2" ng-bind="vm.printData.stageName">初期</td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none">
            <td colspan="4" style="border-top: none">项 目 输 入 要 求</td>
            <td style="border-top: none">依 据 文 件</td>
            <td style="border-top: none">收文日期</td>
        </tr>
        <tr>
            <td style="width: 15%">道路长度</td>
            <td style="width: 15%" ng-bind="vm.printData.wayLength">100m</td>
            <td style="width: 15%">填方最大高度</td>
            <td style="width: 15%" ng-bind="vm.printData.inMaxHeight">100m</td>
            <td style="width: 20%">合同/标书/委托</td>
            <td style="width: 20%" ng-bind="vm.printData.contractReceiveTime">2019.06.13</td>
        </tr>
        <tr>
            <td>道路等级</td>
            <td ng-bind="vm.printData.wayLevel">优秀</td>
            <td>挖方最大高度</td>
            <td ng-bind="vm.printData.outMaxHeight">100m</td>
            <td>产品要求评审记录</td>
            <td ng-bind="vm.printData.reviewReceiveTime">2019.06.13</td>
        </tr>
        <tr>
            <td>设计速度</td>
            <td ng-bind="vm.printData.designSpeed">10m/s</td>
            <td>支挡最大高度</td>
            <td ng-bind="vm.printData.keepMaxHeight">100m</td>
            <td>项目立项批文</td>
            <td ng-bind="vm.printData.projectApproveReceiveTime">2019.06.13</td>
        </tr>
        <tr>
            <td>路幅宽度</td>
            <td ng-bind="vm.printData.wayWidth">20m</td>
            <td>桥梁最大跨径</td>
            <td ng-bind="vm.printData.bridgeMax">100m</td>
            <td>用地红线图</td>
            <td ng-bind="vm.printData.redLineReceiveTime">2019.06.13</td>
        </tr>
        <tr>
            <td>行车道数</td>
            <td ng-bind="vm.printData.carNumber">8</td>
            <td>涵洞最大跨径</td>
            <td ng-bind="vm.printData.holeMax">100m</td>
            <td>规划许可证</td>
            <td ng-bind="vm.printData.planLicenseReceiveTime">2019.06.13</td>
        </tr>
        <tr>
            <td>最大纵坡</td>
            <td ng-bind="vm.printData.maxGradient">20m</td>
            <td>隧道最大长度</td>
            <td ng-bind="vm.printData.tunnelLength">100m</td>
            <td>顾客要求</td>
            <td ng-bind="vm.printData.customerQualityReceiveTime">2019.06.13</td>
        </tr>
        <tr>
            <td>红线面积</td>
            <td ng-bind="vm.printData.redLineArea"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td colspan="6">市 政 专 业 法 律 法 规 输 入 清 单</td>
        </tr>
        <tr>
            <td style="height: 60px;word-wrap:break-word;word-break:break-all;text-align: left;" colspan="6">
                <p ng-bind="vm.printData.lawList"></p>
            </td>
        </tr>
        <tr>
            <td colspan="6">市 政 专 业 节 能 环 保 材 料 输 入 清 单</td>
        </tr>
        <tr>
            <td style="height: 60px;word-wrap:break-word;word-break:break-all;text-align: left;" colspan="6">
                <p ng-bind="vm.printData.greenMaterialList"></p>
            </td>
        </tr>
        <tr>
            <td colspan="6">现 场 探 勘 情 况</td>
        </tr>
        <tr>
            <td style="height: 60px;word-wrap:break-word;word-break:break-all;text-align: left;" colspan="6">
                <p ng-bind="vm.printData.addressExploreDesc"></p>
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
    <p style="font-size: 12px;">注：① 道路专业负责人应按照各依据性文件要求，转化为相应的质量特性并填入“项目输入要求”栏内；<br/>
        &nbsp; &nbsp;  &nbsp;&nbsp;&nbsp;② 项目负责人对方案阶段的设计输入作评审结论，并填入“评审意见”栏里；若项目负责人为道路专业负责人时，应由道路审核人评审。
    </p>
</div>