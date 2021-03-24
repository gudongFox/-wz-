<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">勘察与开发任务书</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="height: 45px;">项目名称</td>
            <td style="height: 45px;" colspan="2" ng-bind="vm.printData.projectName"></td>
			<td style="width: 20%; height: 45px;">工 程 号</td>
            <td style="width: 20%; height: 45px;"ng-bind="vm.printData.projectNo"></td>
        </tr>
        <tr>
            <td style="height: 45px;">建设单位</td>
            <td style="height: 45px;" colspan="2" ng-bind="vm.printData.constructionOrg"></td>
            <td style="width: 15%;height: 45px;">联 系 人</td>
            <td style="width: 15%;height: 45px;" ng-bind="vm.printData.constructionLink"></td>
        </tr>
        <tr>
            <td style="height: 45px;">工程地址</td>
            <td style="height: 45px;" colspan="2" ng-bind="vm.printData.constructionAddress"></td>
            <td style="height: 45px;">联系电话</td>
            <td  style="height: 45px;" ng-bind="vm.printData.constructionPhone"></td>
        </tr>
        <tr>
            <td style="height: 45px;">工程规模</td>
            <td style="height: 45px;" colspan="2" ng-bind="vm.printData.constructionScale">一般</td>
            <td style="height: 45px;">投资规模</td>
            <td style="height: 45px;" ng-bind="vm.printData.investScale"></td>
        </tr>
        <tr>
            <td style="width: 15%;" rowspan="2">工期<br/>要求</td>
            <td style="width: 25%;height: 45px;">初步勘察提交资料时间</td>
            <td style="height: 45px;" colspan="3" ng-bind="vm.printData.firstSubmit"></td>
        </tr>
        <tr>
            <td style="height: 45px;">（直接）详勘提交资料时间</td>
            <td colspan="3" ng-bind="vm.printData.detailSubmit"></td>
        </tr>
        <tr>
            <td style="height: 45px">质量要求</td>
            <td colspan="4" style="height: 45px;text-align: left;padding-left: 40px;">
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'质量要求'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.constructionQuality"/>
                </span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <td style="width: 10%;height: 45px; border-top: none;">序号</td>
        <td style="width: 55%;height: 45px; border-top: none;">名称</td>
        <td style="width: 15%;height: 45px; border-top: none;">数量</td>
        <td style="width: 20%;height: 45px; border-top: none;">备注</td>
        </tr>
        <tr ng-repeat="fileType in vm.fileTypes">
            <td ng-bind="$index+1"></td>
            <td style="padding-left: 10px;text-align: left;" ng-bind="fileType.fileType"></td>
            <td ng-bind="fileType.fileCount"></td>
            <td></td>
        </tr>
    </table>
    <table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
        <td style="border-top: none" style="width: 20%" ng-bind="node.activityName"></td>
        <td style="border-top: none;width: 30%">
            <span ng-bind="node.userName" style="margin-right: 10px;"></span>
            <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
        </td>
        <td style=" border-top: none;width: 20%" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
        <td style="border-top: none;width: 30%">
            <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
            <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
        </td>
    </tr>
    </table>
    <p style="font-size: 12px;">注：项目负责人填写。</p>
</div>