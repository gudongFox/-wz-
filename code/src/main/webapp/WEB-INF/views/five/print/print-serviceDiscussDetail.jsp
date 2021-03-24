<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none">
    <h2 style="text-align: center;">中国五洲工程设计集团有限公司</h2>
    <h3 style="text-align: center;">设计变更通知单</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">项目名称</td>
            <td ng-bind="vm.printData.projectName"></td>
            <td style="width: 20%;" ng-bind="'更改编号:'+vm.printData.changeNo"></td>
            <td style="width: 20%;" ng-bind="'图号:'+vm.printData.drawNo"></td>
        </tr>
        <tr>
            <td>更改涉及专业</td>
            <td ng-bind="vm.printData.needChangeMajor" colspan="3"></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:left;padding-left: 20px;padding-top: 10px;">
                更改原因：<br>
                <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'设计变更更改原因'}:true">
                    <span style="padding-left: 20px" ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.discussContent"/>
                    <br ng-if="$index/2>0"/>
                </span>
                <p ng-bind="'更改原因补充说明：'+vm.printData.changeReasonDetail"></p>
            </td>
        </tr>
        <tr>
            <td colspan="4" style="padding-left: 20px;padding-top:10px;height:300px;text-align:left;vertical-align: top; border-bottom: none;">
                <p ng-bind="'洽商变更内容（所在图号：'+vm.printData.drawNo+'）:'"></p>
                <p ng-bind="vm.printData.changeContent"></p>
            </td>
        </tr>

        <tr>
            <td colspan="4" style="padding-left: 20px;height: 60px;text-align: left;border-bottom: none;border-top: none;">
                <p ng-bind="'附图'+vm.printData.attachCount+'张  附图编号为：'+vm.printData.attachNo"></p>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <p style="text-align: left;padding-left: 20px">对已施工、安装部分的影响已经考虑在内，更改符合要求。 </p>
                <p style="text-align: left;padding-left: 20px">更改内容已考虑更改涉及的专业:
                    <span ng-repeat="needChange in sysCodes | filter:{codeCatalog:'更改内容已考虑更改涉及的专业'}:true">
                     <input type="checkbox" ng-checked="needChange.codeValue==vm.printData.needChange"/>
                     <span ng-bind="needChange.name"></span>
                   </span>
                </p>
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
    <p style="font-size: 12px;  margin-left:20px">
        备注：①每发出一份“设计变更通知单”，更改专业的负责人均应及时填写本专业的“更改记录表”。<br>
        ②当更改涉及两个或两个以上专业时，应由所涉及专业的专业负责人会签。<br>
        ③当更改内容写完时，更改单还有空白处，应注明“以下空白” <br>
    </p>
    </p>


</div>