<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">重大危险源控制措施计划</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号:'+vm.printData.formNo"></p>

    <table class="print_table">
        <tr>
            <td style="width: 10%;height: 35px;text-align: left;padding-left:10px" colspan="1">序号</td>
            <td style="width: 20%;text-align: left;padding-left:10px"> 场所/设施/活动</td>
            <td style="width: 20%;text-align: left;padding-left:10px">危 险 因 素</td>
            <td style="width: 20%;text-align: left;padding-left:10px"> 可能导致的事故</td>
            <td style="width: 30%;text-align: left;padding-left:10px">控 制 措 施 计 划（a—g）</td>

        </tr>
        <tr ng-repeat=" list in vm.printDangerPlanDetails">
            <td style="width: 10%;height: 35px;" colspan="1" ng-bind="$index+1"></td>
            <td style="width: 20%;" ng-bind="list.addressName"></td>
            <td style="width: 20%;" ng-bind="list.dangerName"></td>
            <td style="width: 20%;" ng-bind="list.resultAccident"></td>
            <td style="width: 30%;" ng-bind="list.controlPlan"></td>
        </tr>


    </table>
    <table class="print_table">
        <tr>
            <td style="width: 10%;height: 70px;border-top: none;text-align: left" colspan="1">
                备注：<br>
                &nbsp;&nbsp;a——制定职业健康安全目标和职业健康安全管理方案；
                &nbsp;&nbsp;b——制定管理程序或控制程序、规程、制度；<br>
                &nbsp;&nbsp;c——制定并落实安全技术措施方案；
                &nbsp;&nbsp;d——培训与教育、安全技术交底；<br>
                &nbsp;&nbsp;e——配发个人劳动保护用品；
                &nbsp;&nbsp;f——加强现场监督检查；<br>
                &nbsp;&nbsp;g——制定应急准备与响应程序、计划、预案。
            </td>

        </tr>
    </table>
    <table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="border-top: none;width: 20%;height: 35px" ng-bind="node.activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style=" border-top: none;width: 20%;height:35px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>

    </table>


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	