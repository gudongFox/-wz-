<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发任务书</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">项目名称</td>
            <td colspan="4" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td>建设单位</td>
            <td colspan="2" ng-bind="vm.printData.constructionOrg"></td>
            <td style="width: 15%;">工 程 号</td>
            <td style="width: 15%;" ng-bind="vm.printData.projectNo"></td>
        </tr>
        <tr>
            <td>工程地址</td>
            <td colspan="2" ng-bind="vm.printData.constructionAddress"></td>
            <td>联 系 人</td>
            <td ng-bind="vm.printData.constructionLink"></td>
        </tr>
        <tr>
            <td>工程规模</td>
            <td colspan="2" ng-bind="vm.printData.constructionScale"></td>
            <td>联系电话</td>
            <td ng-bind="vm.printData.constructionTel"></td>
        </tr>
        <tr>
            <td style="width: 15%;" rowspan="3">工 期<br/>要 求</td>
            <td style="width: 25%;">方案设计交图时间</td>
            <td colspan="3">
                <span ng-bind="vm.printData.planDesignFinish"></span>
            </td>
        </tr>
        <tr>
            <td>初步设计交图时间</td>
            <td colspan="3">
                <span ng-bind="vm.printData.firstDesignFinish"></span>
            </td>
        </tr>
        <tr>
            <td>施工图交图时间</td>
            <td colspan="3">
                <span ng-bind="vm.printData.constructionDesignFinish"></span>
            </td>
        </tr>
        <tr>
            <td style="height: 60px;">质 量<br/>要 求</td>
            <td colspan="4" style="text-align: left;padding-left: 40px;">
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'房建设计质量'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.constructionQuality"/>
                    <br ng-if="$index/2>0"/>
                </span>

            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td colspan="4" style="border-top: none;">现有设计资料交接清单</td>
        </tr>
        <tr>
            <td style="width: 10%;">序号</td>
            <td style="width: 55%;">名称</td>
            <td style="width: 15%;">数量</td>
            <td style="width: 20%;">备注</td>
        </tr>
        <tr ng-repeat="fileType in vm.printData.fileTypes">
            <td ng-bind="$index+1"></td>
            <td style="padding-left: 10px;text-align: left;" ng-bind="fileType.fileType"></td>
            <td ng-bind="fileType.fileCount"></td>
            <td></td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none" ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
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
    <p style="font-size: 12px;">注：项目负责人填写。</p>
</div>