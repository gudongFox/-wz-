<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发评审记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">工程名称</td>
            <td colspan="6" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td style="width: 15%;">主持人</td>
            <td style="width: 20%;"colspan="2" ng-bind="vm.printData.compereName">hnz</td>
            <td style="width: 15%;">项目负责人</td>
            <td  style="width: 15%;"ng-bind="vm.printData.projectChargeName"></td>
            <td style="width: 15%;"style="width: 15%;">评审日期</td>
            <td style="width: 20%;" ng-bind="vm.printData.reviewTime"></td>
        </tr>
        <tr>
            <td>参会人</td>
            <td colspan="6" ng-bind="vm.printData.attendName"></td>
        </tr>
        <tr>
            <td >评审阶段:</td>
            <td colspan="6">
                <span ng-repeat="stageName in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="stageName.codeValue"></span>
                    <input type="checkbox" ng-checked="stageName.codeValue==vm.printData.stageName"/>
                </span>
            </td>

        </tr>
        <tr>
            <td colspan="7" style="border-bottom: none;padding-left: 20px">评审要点：</td>
        </tr>
        <tr >
            <td colspan="7" style="text-align:left;margin-left: 40px" ng-bind-html="vm.html"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none">
            <td style="border-top: none;padding-left: 20px">评审结论：</td>
        </tr>
        <tr style="height: 60px;">
            <td>
                <span ng-repeat="reviewResult in sysCodes | filter:{codeCatalog:'设计与开发评审结论'}:true">
                    <span ng-bind="reviewResult.codeValue"></span>
                    <input type="checkbox" ng-checked="reviewResult.codeValue==vm.printData.reviewResult"/>
                    <br ng-if="$index/2>0"/>
                </span>
            </td>
        </tr>
        <tr>
            <td style="border-bottom: none;padding-left: 20px">存在的问题及改进意见：</td>
        </tr>
        <tr>
            <td style="height: 200px" >
                <span style="float:left ;text-align:left;padding-bottom: 100px;padding-left: 50px;padding-right: 50px" ng-bind="vm.printData.reviewSuggestion"></span>
            </td>
        </tr>
        <tr>
            <td style="border-bottom: none;padding-left: 20px">对纠正、改进的跟踪验证结果：</td>
        </tr>
        <tr >
            <td style="height: 200px" >
                <span style="float:left; text-align:left;padding-bottom: 100px;padding-left: 50px;padding-right: 50px" ng-bind="vm.printData.reviewSolved"></span>
            </td>
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
    <p style="font-size: 12px;">注：①由项目负责人含同专业负责人填写。<br>
        &nbsp;&nbsp;②各设计阶段开始前评审。
    </p>
</div>