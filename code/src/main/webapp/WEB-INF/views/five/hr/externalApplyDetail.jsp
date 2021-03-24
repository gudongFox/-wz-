<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar"  id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>人资管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>外聘技术人员录用审批表</span>
        </li>

    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>外聘技术人员录用审批表
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
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
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 600px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">聘用部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);vm.opt='所属部门'"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">聘用人员类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'聘用人员类型'}:true"
                                            ng-model="vm.item.userType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">聘用人姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="聘用人姓名" name="userName" required="true"  ng-model="vm.item.userName"ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">工号（登录名）</label>
                                <div class="col-md-4" style="text-align: center" >
                                    <input type="text" class="form-control" placeholder="人力资源部填写" required="true"  ng-model="vm.item.userLogin"ng-disabled="processInstance.myRunningTaskName!='人力资源部'"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">性别</label>
                                <div class="col-md-4" style="text-align: center" >
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'性别'}:true"
                                            ng-model="vm.item.man" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>

                                <label class="col-md-2 control-label required">年龄</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="聘用人年龄" name="age" required="true"  ng-model="vm.item.age"ng-disabled="!processInstance.firstTask"
                                    />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">最高学历</label>
                                <div class="col-md-4" style="text-align: center">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'学历'}:true"
                                            ng-model="vm.item.educationBackground" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">毕业院校</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="毕业院校" name="graduateCollege" required="true"  ng-model="vm.item.graduateCollege"ng-disabled="!processInstance.firstTask"
                                           />
                                </div>
                                <label class="col-md-2 control-label required">所学专业</label>
                                <div class="col-md-4" style="text-align: center">
                                    <input type="text" class="form-control" placeholder="所学专业" name="graduateMajor" required="true"  ng-model="vm.item.graduateMajor"ng-disabled="!processInstance.firstTask"
                                           />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">拟聘专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.planMajor" required="true"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showMajorModel();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">拟聘岗位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.planPost" required="true"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showPostModel();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">职称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="职称" name="title" required="true"  ng-model="vm.item.title" ng-disabled="!processInstance.firstTask"
                                    />
                                </div>
                                <label class="col-md-2 control-label required">资质</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="资质" name="qualificationCertificate" required="true"  ng-model="vm.item.qualificationCertificate" ng-disabled="!processInstance.firstTask"
                                    />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">拟聘报酬</label>
                                <div class="col-md-4" style="text-align: center">
                                    <input type="text" class="form-control" placeholder="拟聘报酬" name="planSalary" required="true"  ng-model="vm.item.planSalary" ng-disabled="!processInstance.firstTask"
                                    />
                                </div>
                                <label class="col-md-2 control-label required">试用期工资</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'试用期工资'}:true"
                                            ng-model="vm.item.probationSalary" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">工作经历</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" placeholder="工作经历" name="workExperience" required="true"  ng-model="vm.item.workExperience" ng-disabled="!processInstance.firstTask"
                                    />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">工作业绩</label>
                                <div class="col-md-10">
                                    <textarea type="text" rows="5" class="form-control" placeholder="工作业绩及相关资质证明（上传附件）" name="performance" required="true"  ng-model="vm.item.performance" ng-disabled="!processInstance.firstTask"
                                    />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用人单位意见</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" placeholder="用人单位意见（含聘用理由）" name="userDepartmentOpinion" required="true"  ng-model="vm.item.userDepartmentOpinion"
                                              ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
<%--                            <div class="form-group">
                                <label class="col-md-2 control-label">人力资源部意见</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" placeholder="人力资源部意见" name="hrDepartmentOpinion" required="true"  ng-model="vm.item.hrDepartmentOpinion"
                                              ng-disabled="processInstance.myRunningTaskName!='人力资源部审批'"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">信息化建设与管理部意见（针对技术岗位认定）</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" placeholder="信息化建设与管理部意见（针对技术岗位认定）" name="technologyDepartmentOpinion" required="true"  ng-model="vm.item.technologyDepartmentOpinion"
                                              ng-disabled="processInstance.myRunningTaskName!='信息化建设与管理部审批'"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">公司主管领导意见</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" placeholder="公司主管领导意见" name="chargeLeaderOpinion" required="true"  ng-model="vm.item.chargeLeaderOpinion"
                                              ng-disabled="processInstance.myRunningTaskName!='公司主管领导审批'"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">公司主管人事工作领导意见</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" placeholder="公司主管人事工作领导意见" name="chargeHrLeaderOpinion" required="true"  ng-model="vm.item.chargeHrLeaderOpinion"
                                              ng-disabled="processInstance.myRunningTaskName!='公司主管人事工作领导审批'"/>
                                </div>
                            </div>--%>

<%--                            <div class="form-group">--%>
<%--                                <label class="col-md-2 control-label">备注</label>--%>
<%--                                <div class="col-md-10">--%>
<%--                                    <input type="text" class="form-control" ng-disabled="!processInstance.firstTask" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>--%>
<%--                                </div>--%>
<%--                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>


                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 600px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 600px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="selectMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">从事专业</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height:430px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer" >
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>专业名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="major in vm.majors" ng-show="major.name!='总设计师'">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.planMajor.indexOf(major.name)>=0" class="cb_major" data-name="{{major.name}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="major.name"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectMajor();">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="selectPostModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">所有岗位</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>岗位名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="post in vm.posts">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.planPost.indexOf(post.code)>=0" class="cb_post" data-name="{{post.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="post.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectPost();">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择执行所室</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../../common/common-actFlow.jsp"/>
<jsp:include page="../../hr/print/print_five_hr_external_technician_apply.jsp"/>