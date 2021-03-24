<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:56})">用印管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">用印审批</a>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-doc"></i> 用印审批
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 550px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用印事由</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                              ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">文件/图纸名称</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                              ng-model="vm.item.fileName" name="fileName" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">用印时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="stampTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.stampDate" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                                    <button class="btn default" type="button"
                                                            ng-disabled="!processInstance.firstTask"><i
                                                            class="fa fa-calendar"></i></button>
                                            </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">文件份数</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.fileCount"
                                           placeholder="文件份数"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                    <label class="col-md-2 control-label required">印章类型</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control ng-pristine ng-untouched ng-empty ng-invalid ng-invalid-required" ng-model="vm.item.stampName"
                                               name="stampName" required="true" disabled>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showStampModel();"
                                                    disabled><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">是否需法律审核</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.legalReview" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>

                            <div class="form-group" ng-if="vm.item.stampName.indexOf('合同章')>-1">
                                <label class="col-md-2 control-label required">评审级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'评审级别'}:true"
                                            ng-model="vm.item.companyLevel" class="form-control" required
                                            ng-disabled="!processInstance.firstTask"></select>
                                    <span style="color: red" ng-show="processInstance.firstTask">合同章</span>
                                </div>

                                <label class="col-md-2 control-label required">副总审批</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.viceLeaderName" name="viceLeaderName"  required   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('viceLeader');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.firstTask">点击右侧按钮选择副总审批-合同章</span>
                                </div>

                            </div>
                            <div class="form-group required" >
                                <div ng-if="vm.item.companyLevel.indexOf('公司级')>-1&&vm.item.stampName.indexOf('合同章')>-1">
                                    <label class="col-md-2 control-label">合同评审人员</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.contractSealManName" name="contractSealManName"  required   readonly/>
                                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('contractSealMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                        </div>
                                        <span style="color: red" ng-show="processInstance.firstTask">合同章</span>
                                    </div>
                                </div>
<%--
                                <div  ng-show="vm.item.stampName.indexOf('公司章')>-1||vm.item.stampName.indexOf('法人')>-1">
                                    <label class="col-md-2 control-label required">归口管理部门</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.functionDeptName" name="functionDeptName"   ng-disabled="!processInstance.firstTask"/>
                                            <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                     </span>
                                        </div>
                                        <span style="color: red" ng-show="processInstance.firstTask">公司章，法人章，法人签名</span>
                                    </div>
                                </div>--%>

                            </div>
                            <div class="form-group" ng-if="vm.item.stampName.indexOf('压力')>-1">
                                <label class="col-md-2 control-label required">压力管道技术员</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.qualityCompanyManName" name="qualityCompanyManName" required="true"    readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('qualityCompanyMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.firstTask">压力管道章</span>
                                </div>
                                    <label class="col-md-2 control-label required">评审方式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'评审方式'}:true" required="true"
                                            ng-model="vm.item.online" class="form-control" name="online"
                                            ng-disabled="!processInstance.firstTask"></select>
                                    <span style="color: red" ng-show="processInstance.firstTask">压力管道章</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">申请部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" readonly/>
                                </div>

                                <label class="col-md-2 control-label">申请人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" name="creatorName" readonly/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>


<div class="modal fade" id="stampModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择用印类型</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="stamp in vm.stampList">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_stamp"
                           ng-checked="vm.item.stampName.indexOf(stamp.name)>=0"
                           data-name="{{stamp.name}}" data-id="{{'file_'+id}}" /> <span ng-bind="stamp.name"
                                                                                          class="margin-right-10"
                                                                                          style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%4==0"/>
               </span>
                <div style="padding-left: 5px;padding-top: 10px">
                    <span class="data_title" style="font-size: 8px;margin-top: 2px" title="全选"
                          ng-click="vm.selectAllFile();">全选</span><span
                        style="margin-left: 10px;font-size: 8px;" class="data_title" title="反选"
                        ng-click="vm.toggleSelectFile();">反选</span>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveStamp();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<jsp:include page="../five/print/print-oaStampApplyDetail.jsp"/>

