<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.employee">人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>员工信息详情</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-user"></i> <span ng-bind="vm.item.deptName+' '+vm.item.userName"></span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        教育经历
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        合同信息 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-1 control-label">姓名</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" ng-model="vm.item.userName"
                                           name="userName" required="true" maxlength="20"/>
                                </div>

                                <label class="col-md-1 control-label">登录名</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" ng-model="vm.item.userLogin"
                                           name="userLogin" required="true" maxlength="20"/>
                                </div>

                                <label class="col-md-1 control-label">员工编号</label>
                                <div class="col-md-3">
                                    <input class="form-control" ng-model="vm.item.userNo"
                                           name="userNo" maxlength="20"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-1 control-label">证件类型</label>
                                <div class="col-md-3">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'证件类型'}:true"
                                            ng-model="vm.item.idCardType" class="form-control"></select>
                                </div>

                                <label class="col-md-1 control-label">证件编号</label>
                                <div class="col-md-3">
                                    <input class="form-control" ng-model="vm.item.idCardNo"
                                           name="idCardNo" maxlength="20"/>
                                </div>

                                <label class="col-md-1 control-label">现住地址</label>
                                <div class="col-md-3" style="text-align: center">

                                    <input type="text" class="form-control" ng-model="vm.item.liveAddress"
                                           name="liveAddress" maxlength="100"/>

                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-1 control-label">性别</label>
                                <div class="col-md-3" style="text-align: center">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'性别'}:true"
                                            ng-model="vm.item.gender" class="form-control"></select>
                                </div>

                                <label class="col-md-1 control-label">年龄</label>
                                <div class="col-md-3">

                                    <input type="text" class="form-control" ng-model="vm.item.age"
                                           name="age" maxlength="20"/>

                                </div>
                                <label class="col-md-1 control-label">出生日期</label>
                                <div class="col-md-3">
                                    <div class="input-group date date-picker" id="birthDay">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.birthDay">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-1 control-label">出生地</label>
                                <div class="col-md-3">

                                    <input type="text" class="form-control" ng-model="vm.item.birthPlace"
                                           name="birthPlace" maxlength="20"/>

                                </div>

                                <label class="col-md-1 control-label">居住城市</label>
                                <div class="col-md-3">

                                    <input type="text" class="form-control" ng-model="vm.item.country"
                                           name="country" maxlength="100"/>

                                </div>
                                <label class="col-md-1 control-label">国籍</label>
                                <div class="col-md-3" style="text-align: center">

                                    <input type="text" class="form-control" ng-model="vm.item.nation"
                                           name="nation" maxlength="100"/>

                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-1 control-label">政治面貌</label>
                                <div class="col-md-3">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'政治面貌'}:true"
                                            ng-model="vm.item.politics" class="form-control"></select>

                                </div>

                                <label class="col-md-1 control-label">加入时间</label>
                                <div class="col-md-3">
                                    <div class="input-group date date-picker" id="politicsTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.politicsTime">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>

                                <label class="col-md-1 control-label">最高学历</label>
                                <div class="col-md-3">

                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'学历'}:true"
                                            ng-model="vm.item.educationBackground" class="form-control"></select>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-1 control-label">参加工作时间</label>
                                <div class="col-md-3" style="text-align: center">

                                    <div class="input-group date date-picker" id="joinWorkTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.joinWorkTime">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>

                                </div>
                                <label class="col-md-1 control-label">入职时间</label>
                                <div class="col-md-3">

                                    <div class="input-group date date-picker" id="joinCompanyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.joinCompanyTime">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>

                                </div>
                                <label class="col-md-1 control-label">离职时间</label>
                                <div class="col-md-3">

                                    <div class="input-group date date-picker" id="leaveCompanyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.leaveCompanyTime">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>

                                </div>

                            </div>

                            <div class="form-group">

                                <label class="col-md-1 control-label">移动电话</label>
                                <div class="col-md-3">

                                    <input type="text" class="form-control" ng-model="vm.item.mobile"
                                           name="mobile" maxlength="100"/>

                                </div>
                                <label class="col-md-1 control-label">工作电话</label>
                                <div class="col-md-3">

                                    <input type="text" class="form-control" ng-model="vm.item.officeTel"
                                           name="officeTel" maxlength="100"/>

                                </div>
                                <label class="col-md-1 control-label">家庭电话</label>
                                <div class="col-md-3">

                                    <input type="text" class="form-control" ng-model="vm.item.homeTel"
                                           name="homeTel" maxlength="100"/>

                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-1 control-label">电子邮箱</label>
                                <div class="col-md-3" style="text-align: center">

                                    <input type="text" class="form-control" ng-model="vm.item.enEmail"
                                           name="enEmail" maxlength="100"/>

                                </div>
                                <label class="col-md-1 control-label">城镇户口</label>
                                <div class="col-md-3" style="text-align: center">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'城镇户口'}:true"
                                            ng-model="vm.item.cityId" class="form-control"></select>

                                </div>

                                <label class="col-md-1 control-label">车牌号</label>
                                <div class="col-md-3">

                                    <input type="text" class="form-control" ng-model="vm.item.carNo"
                                           name="carNo" maxlength="100"/>

                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-1 control-label">员工类别</label>
                                <div class="col-md-3" style="text-align: center">

                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'员工类型'}:true"
                                            ng-model="vm.item.userType" class="form-control"></select>

                                </div>
                                <label class="col-md-1 control-label">员工状态</label>
                                <div class="col-md-3">

                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'员工状态'}:true"
                                            ng-model="vm.item.userStatus" class="form-control"></select>

                                </div>
                                <label class="col-md-1 control-label">
                                    <a ng-href="{{vm.item.avatar}}" target="_blank" style="cursor: pointer;">用户头像</a>
                                </label>
                                <div class="col-md-3">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.avatar"
                                               maxlength="200" name="avatar" placeholder="">
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span class="btn blue fileinput-button">
                                      <i class="fa fa-cloud-upload" style="height:15px "></i>
                                      <span>上传</span>
                                      <input type="file" id="btnUploadHead" name="multipartFile"
                                             data-url="/sys/attach/receive.json"
                                             accept="*"/>
                                    </span>
                                        </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-1 control-label">专业</label>
                                <div class="col-md-3" style="text-align: center">

                                    <select ng-options="s.name as s.name for s in vm.majorNames"
                                            ng-model="vm.item.specialty" class="form-control"></select>

                                </div>
                                <label class="col-md-1 control-label">其他专业</label>
                                <div class="col-md-3">

                                    <select ng-options="s.name as s.name for s in vm.majorNames"
                                            ng-model="vm.item.otherSpecialty" class="form-control"></select>

                                </div>

                                <label class="col-md-1 control-label">
                                    <a ng-href="{{vm.item.signUrl}}" target="_blank" style="cursor: pointer;">签名文件</a>
                                </label>
                                <div class="col-md-3">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.signUrl"
                                               maxlength="200" name="signUrl" placeholder="">
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span class="btn blue fileinput-button">
                                      <i class="fa fa-cloud-upload" style="height:15px "></i>
                                      <span>上传</span>
                                      <input type="file" id="btnUploadSign" name="multipartFile"
                                             data-url="/sys/attach/receive.json"
                                             accept="*"/>
                                    </span>
                                        </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-1 control-label">
                                    <a ng-href="{{vm.item.signUrl}}" target="_blank">其他信息</a>
                                </label>
                                <div class="col-md-11">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"
                                           name="remark" maxlength="100"/>
                                </div>
                            </div>

                        </div>
                        <div class="form-actions right" style="padding: 10px;">
                            <button type="button" class="btn blue" ng-click="vm.save();">保存
                            </button>
                            <button type="button" class="btn default" ng-click="vm.loadData();">重置
                            </button>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">序号</th>
                                <th>用户名</th>
                                <th>学校名称</th>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>教育类型</th>
                                <th>学历</th>
                                <th>学制</th>
                                <th>学位</th>
                                <th>第一专业</th>
                                <th>其他专业</th>
                                <th>任务状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.educationList">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.userLogin" class="font-blue2" ng-click="vm.showDetail(item.id);"></td>
                                <td ng-bind="item.schoolName"></td>
                                <td ng-bind="item.beginTime|date:'yyyy-MM-dd HH:mm'">
                                <td ng-bind="item.endTime|date:'yyyy-MM-dd HH:mm'">
                                <td ng-bind="item.educationType"></td>
                                <td ng-bind="item.educationName"></td>
                                <td ng-bind="item.educationYear"></td>
                                <td ng-bind="item.educationDegree"></td>
                                <td ng-bind="item.primarySpecialty"></td>
                                <td ng-bind="item.otherSpecialty"></td>
                                <td ng-bind="item.processName"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-10" ng-click="vm.showEducation(item.id);"
                                       title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-10" ng-click="vm.removeEducation(item.id);"
                                       title="删除"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">

                    <div class="row">

                        <div class="col-md-12">
                            <a href="javascript:;" class="btn btn-sm blue"
                               ng-click="back();">
                                <i class="fa fa-plus"></i> 新增合同 </a>

                        </div>

                        <div class="col-md-12">

                            <div class="table-scrollable">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 50px;">序号</th>
                                        <th>用户名</th>
                                        <th>合同性质</th>
                                        <th>用工形式</th>
                                        <th>合同区域</th>
                                        <th>社保缴纳地</th>
                                        <th>合同年限</th>
                                        <th>开始时间</th>
                                        <th>结束时间</th>

                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.contractList">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td ng-bind="item.userLogin" class="font-blue2"
                                            ng-click="vm.showDetail(item.id);"></td>
                                        <td ng-bind="item.contractType"></td>
                                        <td ng-bind="item.workType">
                                        <td ng-bind="item.contractLocation">
                                        <td ng-bind="item.insureLocation"></td>
                                        <td ng-bind="item.contractYear"></td>
                                        <td ng-bind="item.beginTime|date:'yyyy-MM-dd HH:mm'"></td>
                                        <td ng-bind="item.endTime|date:'yyyy-MM-dd HH:mm'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-10" ng-click="vm.showContract(item.id);"
                                               title="详情"></i>
                                            <i class="fa fa-trash-o margin-right-10"
                                               ng-click="vm.removeContract(item.id);" title="删除"></i>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>


                </div>
            </div>

        </div>

    </div>

</div>


<div class="modal fade drag" id="educationModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">教育经历</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">用户名</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" placeholder="用户名"
                                       ng-model="vm.education.userLogin"
                                       disabled="disabled"/>
                            </div>
                            <label class="col-md-2 control-label">学校名称</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" required="true"
                                       ng-model="vm.education.schoolName"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">教育类型</label>
                            <div class="col-md-4">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'教育类型'}:true"
                                        ng-model="vm.education.educationType" class="form-control"></select>
                            </div>
                            <label class="col-md-2 control-label">学历</label>
                            <div class="col-md-4">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'学历'}:true"
                                        ng-model="vm.education.educationName" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">学制</label>
                            <div class="col-md-4">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'学制'}:true"
                                        ng-model="vm.education.educationYear" class="form-control"></select>
                            </div>
                            <label class="col-md-2 control-label">学位</label>
                            <div class="col-md-4">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'学位'}:true"
                                        ng-model="vm.education.educationDegree" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">第一专业</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.education.primarySpecialty"
                                       name="primarySpecialty" required="true" maxlength="30"/>
                            </div>
                            <label class="col-md-2 control-label">其他专业</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.education.otherSpecialty"
                                       name="otherSpecialty" required="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">开始时间</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker" id="beginTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.education.beginTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                            <label class="col-md-2 control-label">结束时间</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker" id="endTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.education.endTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>


                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <%--  <button type="button" class="btn blue" ng-click="vm.updateEducation();">确认</button>--%>
            </div>
        </div>
    </div>
</div>

<div class="modal fade drag" id="contractModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">合同详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">用户名</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" placeholder="用户名"
                                       ng-model="vm.contract.userLogin"
                                       disabled="disabled"/>
                            </div>
                            <label class="col-md-2 control-label">合同性质</label>
                            <div class="col-md-4">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'合同性质'}:true"
                                        ng-model="vm.contract.contractType" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">用工形式</label>
                            <div class="col-md-4">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'用工形式'}:true"
                                        ng-model="vm.contract.workType" class="form-control"></select>
                            </div>
                            <label class="col-md-2 control-label">合同区域</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" placeholder="合同区域"
                                       ng-model="vm.contract.contractLocation"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">社保缴纳地</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" placeholder="社保缴纳地"
                                       ng-model="vm.contract.insureLocation"/>
                            </div>
                            <label class="col-md-2 control-label">合同年限</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" placeholder="合同年限"
                                       ng-model="vm.contract.contractYear"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">开始时间</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker">
                                    <input type="text" class="form-control"
                                           ng-model="vm.contract.beginTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                            <label class="col-md-2 control-label">结束时间</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker">
                                    <input type="text" class="form-control"
                                           ng-model="vm.contract.endTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">通知领取时间</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker" id="noticeTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.contract.noticeTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                            <label class="col-md-2 control-label">用户领取时间</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker" id="receiveTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.contract.receiveTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>


                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <%--                <button type="button" class="btn blue" ng-click="vm.updateContract();">确认</button>--%>
            </div>
        </div>
    </div>
</div>










