<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">岩土工程勘察质量审查验收表</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号:'+vm.printData.formNo"></p>

    <table class="print_table">
        <tr>
            <td style="width: 10%;height: 260px;" colspan="1">勘</br>察</br>成</br>果</br>资</br>料</br>质</br>量</td>

            <td style="width: 90%;height: 260px;" colspan="3">
				<span style="float:left;padding-top:5px;padding-left:10px;text-align: left;">
				    <span ng-repeat="exploreDetailSatisfy in vm.printData.exploreDetailSatisfys">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span ng-bind="exploreDetailSatisfy.contentDesc"></span>
			<input type="checkbox" ng-checked="exploreDetailSatisfy.satisfy"/></br></br>
			</span>
				</span>
            </td>
        </tr>

        <tr>
            <td style="width: 10%;height: 260px;" colspan="1">勘</br>察</br>结</br>论</br>与</br>建</br>议</td>
            </br>
            <td style="width: 90%;height: 260px;" colspan="3">
			   <span style="float:left;padding-top:5px;padding-left:10px;text-align: left;">
				   <span ng-repeat="exploreDetailSatisfy in vm.printData.exploreDetailSatisfys1">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span ng-bind="exploreDetailSatisfy.contentDesc"></span>
			<input type="checkbox" ng-checked="exploreDetailSatisfy.satisfy"/></br></br>
			   </span>
			   </span>
            </td>
        </tr>
        <tr>
            <td style="width: 10%;height: 260px;" colspan="1">所主</br>任工</br>程师</br>审查</br>结论</td>
            </br>
            <td style="width: 40%;height: 260px;" colspan="1">
				<span style="float:left;padding-top:5px;padding-left:10px;padding-right: 10px;text-align: left;" ng-bind="vm.printData.departChiefEngineerResult">
				</span>
                <span style="float:right;padding-top:230px;padding-right: 10px;text-align: left;" ng-bind="vm.printData.departChiefEngineer+' '+vm.printData.departChiefEngineerDate">
                </span>
            </td>

            <td style="width: 10%;height: 260px;" colspan="1">副总</br>工意</br>见</td>
            </br>
            <td style="width: 40%;height: 260px;" colspan="1">
				<span style="float:left;padding-top:5px;padding-left:10px;padding-right:10px;text-align: left;" ng-bind="vm.printData.viceChiefEngineerResult">
				</span>
                <span style="float:right;padding-top:230px;padding-right: 10px;text-align: left;" ng-bind="vm.printData.viceChiefEngineer+' '+vm.printData.viceChiefEngineerDate">
                </span>
            </td>

        </tr>
    </table>
</div>
			
			
			
			
			