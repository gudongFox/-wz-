<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">岩土工程勘察现场踏勘</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>

    <table class="print_table">
        <tr>
            <td style="height:40px;text-align:left;" ng-bind="'建设单位：'+vm.printData.constructionOrg"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="height:40px;width: 60%;text-align: left;border-top: none;"
                ng-bind="'工程名称：'+vm.printData.projectName"></td>
            <td style="width: 40%;text-align: left;border-top: none;" ng-bind="'工程号：'+vm.printData.projectNo"></td>
        </tr>
        <tr>
            <td style="height:40px;width: 60%;text-align: left;border-top: none;"
                ng-bind="'踏勘人员：'+vm.printData.onUser"></td>
            <td style="width: 40%;text-align: left;border-top: none;" ng-bind="'踏勘日期：'+vm.printData.onTime"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="height:45px;text-align:left;border-top: none;"
                ng-bind="'工程地点：'+vm.printData.constructionAddress"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width: 7%;text-align: left;text-align: left;border-top: none;" rowspan="9" ;></td>
        </tr>
        <tr>
            <td style="width: 93%;height: 100px;text-align: left;border-top: none;">
                <span style="float:left;padding-bottom:80px;padding-left:10px"
                      ng-bind="'拟建工程简况及踏勘范围：'+vm.printData.projectDesc"></span>
            </td>
        </tr>
        <tr>
            <td style="width: 90%;height: 100px;text-align: left">
                <span style="float:left;padding-bottom:80px;padding-left:10px"
                      ng-bind="'地形地貌：'+vm.printData.landDesc"></span>
            </td>
        </tr>
        <tr>
            <td style="width: 90%;height: 100px;text-align: left">
                <span style="float:left;padding-bottom:80px;padding-left:10px"
                      ng-bind="'地质构造：'+vm.printData.landStructure"></span>
            </td>
        </tr>
        <tr>
            <td style="width: 90%;height: 100px;text-align: left">
                <span style="float:left;padding-bottom:80px;padding-left:10px"
                      ng-bind="'地层及岩性：'+vm.printData.landLayer"></span>
            </td>
        </tr>
    </table>

    <div style="page-break-before:always">
        <h2 style="text-align: center;">中冶建工集团有限公司</h2>
        <h3 style="text-align: center;">岩土工程勘察现场踏勘</h3>
        <p style="float: right;font-size: 12px;" ng-bind="'续：'+vm.printData.formNo">编号：Q/MCC-KS-KC-03</p>
        <table class="print_table">
            <tr>
                <td style="width: 90%;height: 100px;text-align: left">
                    <span style="float:left;padding-bottom:80px;padding-left:10px"
                          ng-bind="'不良地质现象及其发育程度：'+vm.printData.badDesc"></span>
                </td>
            </tr>
            <tr>
                <td style="width: 90%;height: 100px;text-align: left">
                    <span style="float:left;padding-bottom:80px;padding-left:10px"
                          ng-bind="'水文地质条件：'+vm.printData.waterDesc"></span>
                </td>
            </tr>
            <tr>
                <td style="width: 90%;height: 100px;text-align: left">
                    <span style="float:left;padding-bottom:80px;padding-left:10px"
                          ng-bind="'“三通一平”状况：'+vm.printData.threeDesc"></span>
                </td>
            </tr>
        </table>
    </div>
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
</div>