<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;" >
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">一体化管理体系内部审核检查及记录表</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号:'+vm.printData.formNo"></p>
	
    <table class="print_table">
        <tr>
            <td style="width: 12%;height: 45px;"colspan="1">受审核部门</td>
            <td style="width: 12%;"ng-bind="vm.printData.checkDepartment">  </td>
			<td style="width: 12%;">负责人</td>
            <td style="width: 12%;"ng-bind="vm.printData.checkCharge">  </td>
		    <td style="width: 12%;">内审员</td>
            <td style="width: 12%;" ng-bind="vm.printData.reviewUser">  </td>
			<td style="width: 12%;">受审日期</td>
            <td style="width: 16%;" ng-bind="vm.printData.reviewDate">  </td>
			
        </tr>

	</table>		
	
	 <table cellspacing="0" class="print_table">
        <tr>
            <td style="width: 6%;border-top: none;" colspan="1">手册</br>条款</td>
      
			<td style="width: 6%;border-top: none;">标准</br>条款</td>
            <td style="width: 30%;border-top: none;">检查要点  </td>
		    <td style="width: 46%;border-top: none;">审核客观记录</td>
            <td style="width: 12%;border-top: none;">是否</br>符合  </td>			
			
			
        </tr>
         <tr ng-repeat="exploreMisReviewDetail in vm.exploreMisReviews">
             <td ng-bind="exploreMisReviewDetail.bookRule"></td>
             <td ng-bind="exploreMisReviewDetail.standardRule"></td>
             <td ng-bind="exploreMisReviewDetail.checkPoint"></td>
             <td ng-bind="exploreMisReviewDetail.checkResult"></td>
             <td><input type="checkbox"ng-checked="exploreMisReviewDetail.passed"></td>
         </tr>

	</table>
	 <p style="font-size: 12px;">备注：“是否符合”一栏中，画“√”表示符合，不符合的直接写明“不符合”；</br>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; Q-质量，E-环境，OHS-职业健康安全，QJ-《工程建设施工企业质量管理规范》
	
	 </p>
    <table class="print_table">
        <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style=" border-top: none;width: 20%;height:45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
</div>
		

	