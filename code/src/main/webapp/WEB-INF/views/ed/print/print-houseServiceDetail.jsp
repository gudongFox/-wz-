<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计与开发现场服务记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">项目名称</td>
            <td colspan="4" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td style="width: 20%;">服务时间</td>
            <td style="width: 30%" ng-bind="vm.printData.serviceTime"></td>
            <td style="width: 20%;">施工单位</td>
            <td style="width: 30%;"ng-bind="vm.printData.constructionOrg"></td>
        </tr>
    </table>
   <table  class="print_table">
       <tr  style="border-top: none">
           <td  style="border-top: none;text-align: left;padding-left: 20px">一、服务内容描述：</td>
       </tr>
       <tr >
           <td style="height: 200px;text-align: left;padding: 10px 20px 160px 20px" ng-bind="vm.printData.serviceContent"></td>
       </tr>
       <tr>
           <td style="text-align: left;padding-left: 20px">二、存在问题及产生原因：</td>
       </tr>
       <tr>
           <td style="height: 200px;text-align: left;padding: 10px 20px 160px 20px" ng-bind="vm.printData.serviceIssue"></td>
       </tr>
       <tr>
           <td style="text-align: left;padding-left: 20px">三、采取的对策措施：</td>
       </tr>
       <tr >
           <td style="height: 200px;text-align: left;padding: 10px 20px 160px 20px" ng-bind="vm.printData.serviceSolve"></td>
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
    <p style="font-size: 12px;">注：用作内部存档，作为更改设计的依据</p>
</div>