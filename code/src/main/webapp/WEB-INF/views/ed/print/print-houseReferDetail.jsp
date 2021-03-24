<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area"  style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计信息联系单</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号:'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td colspan="1">工程名称</td>
			<td colspan="5" ng-bind="vm.printData.projectName">歇台子小学扩建项目</td>
        </tr>
        <tr> 
            <td colspan="1">设计阶段</td>
			<td colspan="5">
              <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设计评审阶段'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.stageName"/>
                </span>
            </td>
        </tr>
	    <tr>
            <td colspan="6"style="height:50px;text-align:left;padding-left:50px;border-bottom: none"
                ng-bind="vm.printData.sourceMajor+'专业向 '+vm.printData.destMajor+'专业提交资料/文件/图纸其名称、内容和具体要求分别说明如下：'">
			</td>
        </tr>
        <tr>
            <td colspan="6"style="height:50px;padding-left:80px;text-align:left;border-top: none;border-bottom: none"
                ng-bind="vm.printData.referDesc"></td>
        </tr>
        <tr>
            <td colspan="6"style="height:300px;padding-left:80px;padding-bottom:350px;text-align:left;border-top: none;">
                <div ng-repeat=" fileName in vm.printData.files">
                    <span style='margin-left:60px;' ng-bind="fileName"></span><br>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="6" style="padding-left:20px;text-align:left;border-bottom: none">接收专业处理意见</td>
        </tr>
        <tr>
            <td colspan="6" style="height:200px;padding-left:40px;padding-bottom:150px;text-align:left;border-top: none" ng-bind="vm.printData.destResult"></td>
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
    <p style="font-size: 12px;  margin-left:10px">备注：①本表由资料提出人填写，接受专业负责人保存，设计完成后交项目负责人归档。<br>
     ②校对人一般应由专业负责人签署，当资料提出人是专业负责人时则应由本专业其他人校对。<br>
     ③“终提”资料后，个别专业仍需相互提资料时，可注明“补提”二字。

</p>
</div>