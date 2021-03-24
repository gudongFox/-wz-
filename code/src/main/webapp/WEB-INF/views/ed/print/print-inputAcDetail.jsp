<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="print_area" style="display: none">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发输入清单及评审记录(建筑)</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"> </p>
    <table class="print_table">
        <tr>
            <td style="width: 10%;">合同号</td>
            <td style="width: 15%;"  ng-bind="vm.printData.contractNo"></td>
            <td style="width: 10%;">工程名称</td>
            <td  ng-bind="vm.printData.projectName"></td>
            <td style="width: 10%;">阶段</td>
            <td style="width: 15%;"  ng-bind="vm.printData.stageName"></td>
        </tr>
    </table>
    <table style="border-top:  solid #000 0px" class="print_table">
        <tr style="border-top:  solid #000 0px">
            <td colspan="4" style="width: 55%;border-top:  solid #000 0px">项目输入要求</td>
            <td style="border-top:  solid #000 0px">依据文件</td>
            <td style="width: 25%;border-top:  solid #000 0px">收文日期</td>
        </tr>
        <tr>
            <td style="width: 15%">总 面 积</td>
            <td style="width: 15%" ng-bind="vm.printData.totalArea"></td>
            <td style="width: 15%">总高度</td>
            <td style="width: 15%" ng-bind="vm.printData.totalHeight"></td>
            <td style="width: 20%">合同/标书/委托</td>
            <td style="width: 20%" ng-bind="vm.printData.contractReceiveTime"></td>
        </tr>
        <tr>
            <td style="width: 15%">层    数</td>
            <td  ng-bind="vm.printData.floorNum"></td>
            <td style="width: 15%">容积率</td>
            <td  ng-bind="vm.printData.plotRatio"></td>
            <td>产品要求评审记录</td>
            <td  ng-bind="vm.printData.reviewReceiveTime"></td>
        </tr>
        <tr>
            <td style="width: 15%">建筑类别</td>
            <td  ng-bind="vm.printData.buildCategory"></td>
            <td style="width: 15%">覆盖率</td>
            <td  ng-bind="vm.printData.coverPercent"></td>
            <td>项目立项批文</td>
            <td  ng-bind="vm.printData.projectApproveReceiveTime"></td>
        </tr>
        <tr>
            <td style="width: 15%">耐火等级</td>
            <td  ng-bind="vm.printData.fireLevel"></td>
            <td style="width: 15%">停车数</td>
            <td  ng-bind="vm.printData.parkNum"></td>
            <td>用地红线图</td>
            <td  ng-bind="vm.printData.redLineReceiveTime"></td>
        </tr>
        <tr>
            <td style="width: 15%">人防等级</td>
            <td  ng-bind="vm.printData.peopleLevel"></td>
            <td style="width: 15%">绿地率</td>
            <td  ng-bind="vm.printData.greenPercent"></td>
            <td>规划许可证</td>
            <td  ng-bind="vm.printData.planLicenseReceiveTime"></td>
        </tr>
        <tr>
            <td style="width: 15%">退 红 线</td>
            <td  ng-bind="vm.printData.redLine"></td>
            <td style="width: 15%">装修标准</td>
            <td  ng-bind="vm.printData.decorateGrade"></td>
            <td>顾客要求</td>
            <td  ng-bind="vm.printData.customerQualityReceiveTime"></td>
        </tr>
        <tr>
            <td colspan="6">建  筑/人 防/景 观/装 修  专  业  法  律  法  规  输  入  清  单</td>
        </tr>
        <tr >
            <td style="height: 120px;padding-bottom:100px;word-wrap:break-word;word-break:break-all;text-align: left;" colspan="6" >
                <span ng-bind="vm.printData.lawList"></span>
            </td>
        </tr>
        <tr>
            <td colspan="6"  >节  能  环  保  材  料   输  入  清  单</td>
        </tr>
        <tr >
            <td style="height: 90px;padding-bottom:100px;word-wrap:break-word;word-break:break-all;text-align: left;" colspan="6" >
                <span ng-bind="vm.printData.greenMaterialList"></span>
            </td>
        </tr>
        <tr>
            <td colspan="6">现 场 探 勘 情 况 </td>
        </tr>
        <tr>
            <td style="height: 90px;padding-bottom:100px;word-wrap:break-word;word-break:break-all;text-align: left;" colspan="6" >
                <span ng-bind="vm.printData.addressExploreDesc"></span>
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
    <p style="font-size: 12px;">注：① 建筑专业负责人应按照各依据性文件要求，转化为相应的质量特性并填入“项目输入要求”栏内；<br>
        &nbsp;     &nbsp;&nbsp;② 项目负责人对方案阶段的设计输入作评审结论，并填入“评审意见”栏里；若项目负责人为建筑专业负责人时，应由建筑审核人评审。
    </p>
</div>