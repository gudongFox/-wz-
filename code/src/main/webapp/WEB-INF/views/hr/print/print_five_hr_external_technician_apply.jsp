<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area" style="display: none;">
    <h3 style="text-align: center;">兼职总设计师资格认定申请表</h3>
    <p style="margin-left: 50px;font-size: 12px;" ng-bind="'申请部门：'+vm.printData.deptName">申请部门： </p>
    <table class="print_table">
        <tr>
            <td style="width: 10%">姓名</td>
            <td style="width: 10%">性别</td>
            <td style="width: 10%">学历</td>
            <td style="width: 10%">年龄</td>
			<td style="width: 10%">毕业院校</td>
			<td style="width: 15%">所学专业</td>
			<td style="width: 15%">拟聘专业</td>
			<td style="width: 20%">拟聘岗位（设计、校核、审核、总设计师及其他）</td>
        </tr>
		<tr>
			<td ng-bind="vm.printData.userName"></td>
			<td ng-bind="vm.printData.sex"></td>
			<td ng-bind="vm.printData.educationBackground"></td>
			<td ng-bind="vm.printData.age"></td>
			<td ng-bind="vm.printData.graduateCollege"></td>
			<td ng-bind="vm.printData.graduateMajor"></td>
			<td ng-bind="vm.printData.planMajor"></td>
			<td ng-bind="vm.printData.planPost"></td>
		</tr>
		<tr>
			<td>职称</td>
			<td colspan="2">资质</td>
			<td colspan="2">相关工作经历</td>
			<td colspan="3">工作业绩及相关资质证明（另附页）</td>
		</tr>
		<tr>
			<td ng-bind="vm.printData.title"></td>
			<td colspan="2" ng-bind="vm.printData.qualificationCertificate"></td>
			<td colspan="2" ng-bind="vm.printData.workExperience"></td>
			<td colspan="3" ng-bind="vm.printData.performance"></td>
		</tr>
		<tr>
			<td colspan="2">聘用人员类型</td>
			<td colspan="3">
                <span ng-repeat="item in sysCodes | filter:{codeCatalog:'聘用人员类型'}:true">
                    <span ng-bind="item.codeValue"></span>
                    <input type="checkbox" ng-checked="item.codeValue==vm.printData.userType"/>
                    <br/>
                </span>
			</td>
			<td>拟聘报酬</td>
			<td ng-bind="vm.printData.planSalary+'元/月'"> </td>
			<td>试用期工资:<br>
				<span ng-repeat="item in sysCodes | filter:{codeCatalog:'试用期工资'}:true">
                    <span ng-bind="item.codeValue"></span>
                    <input type="checkbox" ng-checked="item.codeValue==vm.printData.probationSalary"/>
                    <br/>
                </span>
			</td>
		</tr>
    </table>
    <table class="print_table">
        <tr >
			<td style=" height:100px;width: 15%;border-top: 0px;">用人单位意见（含聘用理由）</td>
			<td style="width: 85%;border-top: 0px;text-align:left">
				<div style="margin-left: 30px;margin-top: 20px"ng-bind="vm.printData.userDepartmentOpinion">1111111</div>
			</td>
		</tr>
		<tr >
			<td style=" height:100px;width: 15%;border-top: 0px;">人力资源部意见</td>
			<td style="width: 85%;border-top: 0px;text-align:left">
				<div style="margin-left: 30px;margin-top: 20px" ng-bind="vm.printData.hrDepartmentOpinion">1111111</div>
			</td>
		</tr>
		<tr >
			<td style=" height:100px;width: 15%;border-top: 0px;">信息化建设与管理部意见（针对技术岗位认定）</td>
			<td style="width: 85%;border-top: 0px;text-align:left">
				<div style="margin-left: 30px;margin-top: 20px" ng-bind="vm.printData.technologyDepartmentOpinion">1111111</div>
			</td>
		</tr>
		<tr >
			<td style=" height:100px;width: 15%;border-top: 0px;">公司主管领导意见</td>
			<td style="width: 85%;border-top: 0px;text-align:left">
				<div style="margin-left: 30px;margin-top: 20px" ng-bind="vm.printData.chargeLeaderOpinion">1111111</div>
			</td>
		</tr>
		<tr >
			<td style=" height:100px;width: 15%;border-top: 0px;">公司主管人事工作领导意见</td>
			<td style="width: 85%;border-top: 0px;text-align:left">
				<div style="margin-left: 30px;margin-top: 20px" ng-bind="vm.printData.chargeHrLeaderOpinion">1111111</div>
			</td>
		</tr>

    </table>
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
</div>