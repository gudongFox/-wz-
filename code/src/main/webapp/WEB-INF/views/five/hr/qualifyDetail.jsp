<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="page-bar" id="pageBar">
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
            <span>设计资格认定</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-database"></i>设计资格认定
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form" id="detail_form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label">用户登录名</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="用户登录名" ng-model="vm.qualify.userLogin"
                               disabled="disabled"/>
                    </div>
                    <label class="col-md-2 control-label">用户名</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="用户名" ng-model="vm.qualify.userName"
                               disabled="disabled"/>
                    </div>

                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label required">从事专业</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.qualify.majorName" required="true"
                                   disabled="disabled"/>
                            <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showSelectMajor();">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                        </div>
                        <span class="help-block" ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                    </div>
                    <label class="col-md-2 control-label required">项目类型</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.qualify.projectType" required="true"
                                   disabled="disabled"/>
                            <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showProjectTypeModel();">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                        </div>
                        <span class="help-block" ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                    </div>
                </div>

                <div class="form-group">

                    <label class="col-md-2 control-label required">设计人</label>
                    <div class="col-md-4" style="text-align: center">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"
                                ng-model="vm.qualify.design" class="form-control"></select>
                    </div>
                    <label class="col-md-2 control-label required">校核人</label>
                    <div class="col-md-4" style="text-align: center">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"
                                ng-model="vm.qualify.proofread" class="form-control"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">审核人</label>
                    <div class="col-md-4" style="text-align: center">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"
                                ng-model="vm.qualify.audit" class="form-control"></select>
                    </div>

                    <label class="col-md-2 control-label required">审定人</label>
                    <div class="col-md-4" style="text-align: center">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"
                                ng-model="vm.qualify.approve" class="form-control"></select>
                    </div>

                </div>

                <div class="form-group">
                    <%--                                <label class="col-md-2 control-label">具有公司审定人资格</label>--%>
                    <%--                                <div class="col-md-4" style="text-align: center">--%>
                    <%--                                    <select  ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"--%>
                    <%--                                             ng-model="vm.qualify.companyApprove" class="form-control"></select>--%>
                    <%--                                </div>--%>
                    <label class="col-md-2 control-label required">总设计师</label>
                    <div class="col-md-4" style="text-align: center">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"
                                ng-model="vm.qualify.chiefDesigner" class="form-control"></select>
                    </div>
                    <label class="col-md-2 control-label required">兼职总设计师</label>
                    <div class="col-md-4" style="text-align: center">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"
                                ng-model="vm.qualify.proChief" class="form-control"></select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label required">专业负责人</label>
                    <div class="col-md-4" style="text-align: center">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'具备资格'}:true"
                                ng-model="vm.qualify.majorCharge" class="form-control"></select>
                    </div>
                    <label class="col-md-2 control-label">备注</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="备注" ng-model="vm.qualify.remark"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">创建时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.qualify.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                    <label class="col-md-2 control-label">修改时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.qualify.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                </div>

            </div>
        </form>
    </div>
</div>
<div class="modal fade" id="selectProjectTypeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">项目类型</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>项目类型名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="projectType in vm.projectTypes">
                            <td>
                                <input type="checkbox" ng-checked="vm.qualify.projectType.indexOf(projectType.code)>=0"
                                       class="cb_projectType" data-name="{{projectType.code}}"
                                       style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="projectType.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectProjectType();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="selectMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10" ng-bind="vm.qualify.deptName+'-'+vm.qualify.userName"></h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 300px;overflow-y: auto;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>专业名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="major in vm.majorNames" ng-if="'总设计师'.indexOf(major)<0">
                            <td>
                                <input type="checkbox" ng-checked="vm.qualify.majorName.indexOf(major)>=0"
                                       class="cb_major" data-name="{{major}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="major"></td>
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


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>