<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">设计过程函件</h3>
    <%--<p style="text-align: center;">CHINA METALLURGICAL CONSTRUCTION ENGINEERING GROUP CO.LTD</p><hr>--%>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p><br><hr>
    <table class="print_table" >
        <tr>
            <td style="width: 15%;border:  0px;">收件方：</td>
            <td style="width: 35%;border:  0px;" ng-bind="vm.printData.receiveCompany"></td>
            <td style="width: 15%;border:  0px;">收件人：</td>
            <td style="width: 35%;border:  0px;" ng-bind="vm.printData.receiver"></td>
        </tr>
        <tr>
            <td style="border:  0px;">发件方：</td>
            <td style="border:  0px;"ng-bind="vm.printData.sendCompany"></td>
            <td style="border:  0px;" >发件人：</td>
            <td style="border:  0px;"ng-bind="vm.printData.sender"></td>
        </tr>
        <tr>
            <td style="width: 15%;border:  0px;">传真号：</td>
            <td style="border:  0px;" ng-bind="vm.printData.fax"></td>
            <td style="width: 15%;border: solid #000 0px;">电话号：</td>
            <td style="border:  0px;"ng-bind="vm.printData.tel"></td>
        </tr>
        <tr>
            <td style="width: 15%;border:  0px;">日期：</td>
            <td style="border:  0px;"ng-bind="vm.printData.letterDate"></td>
            <td style="width: 15%;border:  0px;">编号：</td>
            <td style="border:  0px;"ng-bind="vm.printData.letterNo"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td  style="width: 15%;border-bottom:none;border-left:none;border-right: none">项  目：</td>
            <td style="border-bottom: none;border-left:none;border-right: none;text-align: left" ng-bind="vm.printData.projectName" colspan="3"></td>
        </tr>
        <tr>
            <td  style="width: 15%;border-top:none;border-left:none;border-right: none">主  题：</td>
            <td  style="border-top:none;border-left:none;border-right: none;text-align: left" ng-bind="vm.printData.subject" colspan="3"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr style="border-top:none">
            <td  style="width: 15%;border-top:none;border-left:none;border-right: none">紧急程度：</td>
            <td style="border-top:none;border-left:none;border-right: none">
                <span ng-repeat="level in sysCodes | filter:{codeCatalog:'紧急程度'}:true">
                    <span ng-bind="level.codeValue"></span>
                    <input type="checkbox" ng-checked="level.codeValue==vm.printData.letterLevel"/>
                </span>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="height: 100px; border: none;text-align: left;padding-left:50px;"ng-bind="vm.printData.receiveCompany">：</td>
        </tr>
        <tr>
            <td colspan="2" style="border: none;height: 300px;text-align: left;padding-left:70px;padding-bottom:250px;" ng-bind="vm.printData.letterContent"></td>
        </tr>
        <tr>
            <td colspan="2" style="border: none;text-align:right">中冶建工集团有限公司勘察设计研究总院</td>
        </tr>
        <tr>
            <td colspan="2" style="border: none;text-align:right" ng-bind="vm.printData.letterDate"></td>
        </tr>
        <tr>
            <td colspan="2" style="border: none;text-align:left">地址：重庆市大渡口区西城大道1号</td>
        </tr>
        <tr>
            <td colspan="2" style="border: none;text-align:left">邮政编码：400051</td>
        </tr>
        <tr>
            <td colspan="2" style="border: none;text-align:left">电话：023-68421691（传真）   023-68421691</td>
        </tr>

    </table>
</div>