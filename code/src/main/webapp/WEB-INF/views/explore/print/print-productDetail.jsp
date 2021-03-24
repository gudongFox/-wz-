<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style id="print_style">

</style>
<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">岩土工程勘察成果文件确认记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 20%;height: 45px">项目名称</td>
            <td style="width: 30%;" ng-bind="vm.printData.projectName"></td>
            <td style="width: 20%;">工程号</td>
            <td style="width: 30%;" ng-bind="vm.printData.projectNo"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width: 20%;height: 45px;border-top: none;border-bottom: none;">勘察成果文件</td>
            <td style="width: 80%;border-top: none;border-bottom: none;">
                可研勘察 <input type="checkbox" ng-checked="vm.printData.stageName=='可研勘察'"/>
                初步勘察 <input type="checkbox" ng-checked="vm.printData.stageName=='初步勘察'"/>
                (直接)详细勘察 <input type="checkbox" ng-checked="vm.printData.stageName=='(直接)详细勘察'"/>
                施工勘察 <input type="checkbox" ng-checked="vm.printData.stageName=='施工勘察'"/>

            </td>

        </tr>
    </table>

        <table class="print_table">
            <tr>
                <td style="width: 85%;height: 55px;" colspan="1">勘察成果文件清单</td>
                <td style="width: 15%;"> 备注</td>

            </tr>

            <tr>
                <td style="width: 85%;height: 420px;" colspan="1">
			<span style="float:left;padding-left:20px;padding-bottom: 10px;text-align: left;">
				    <span ng-repeat="exploreDetailSatisfy in vm.printData.exploreDetailSatisfys">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span ng-bind="exploreDetailSatisfy.contentDesc"></span>
			<input type="checkbox" ng-checked="exploreDetailSatisfy.satisfy"/>
			<br>
                </span>
			</span>


                </td>
                <td style="width: 15%;">
                <span style="float: left;padding-right: 40px;padding-left:40px;text-align: left;" ng-bind="vm.printData.remark">
                </span>
                </td>

            </tr>
        </table>

        <table class="print_table">
            <tr>
                <td style="height: 250px;border-top: none;">
                    <span style="float:left;padding-top: 10px;padding-bottom:100px;padding-left:20px;text-align: left;">确认意见：</br>
                    <p style="float:left;padding-bottom:80px;padding-left:20px;padding-right: 20px;text-align: left;" ng-bind="vm.printData.confirmResult"></p></br>

                    <p style="float:right;text-align: left;padding-top:80px;padding-right: 80px" ng-bind="'确认人签字: '+vm.printData.receiveUser+'  '+vm.printData.receiveDate">

                    </p>
                 </span>
                </td>
            </tr>
        </table>

        </div>
	
	