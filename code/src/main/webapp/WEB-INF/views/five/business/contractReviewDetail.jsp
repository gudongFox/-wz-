<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">合同评审</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">合同评审</span>
<!--              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
       -->
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        合同信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_4" data-toggle="tab" aria-expanded="true">
                        评审信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_5" data-toggle="tab" aria-expanded="true">
                        财务相关 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息</a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required" >客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" readonly class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true"  />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">客户编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" ng-disabled="!processInstance.firstTask"
                                           required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否为补充合同</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.main" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">合同额(万元)</label>
                                <div class="col-md-4">
                                    <input ng-model="vm.item.contractMoney" name="contractMoney" class="form-control" required ng-disabled="!processInstance.firstTask">
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.main">
                                <label class="col-md-2 control-label required" >主合同名称</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.mainContractLibraryName"
                                               name="mainContractLibraryName" required="true"
                                               disabled/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectMainLibraryModal()"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required" >主合同号</label>
                                <div class="col-md-4" >
                                    <input type="text" class="form-control" ng-model="vm.item.mainContractLibraryNo" name="mainContractLibraryNo" required="true"    disabled/>
                                </div>
                            </div>
                            <div class="form-group" ng-if="!vm.item.main">
                                <label class="col-md-2 control-label required">备案项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" required="true"
                                               ng-disabled="!vm.item.main||!processInstance.firstTask"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showRecordModal();"
                                                    ng-disabled="!processInstance.firstTask||vm.item.preContractId!=0"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask" >请点击后方按钮选择项目备案</span>
                                </div>

                                    <label class="col-md-2 control-label required">项目号</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                               name="projectNo" required="true" maxlength="20" placeholder="项目号" disabled/>
                                    </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>合同类型</strong></label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                            ng-model="vm.item.contractType" class="form-control"
                                            ng-disabled="!(processInstance.firstTask)"></select>
                                </div>
                                <label class="col-md-2 control-label required"><strong>预计签约日期</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planSignTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.planSignTime" required="true"
                                               ng-disabled="!(processInstance.firstTask)">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="!(processInstance.firstTask)"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>合同名称</strong></label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractName"
                                           name="contractName" required="true"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true"    readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.getContractNo();"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>承接部门</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.opt='deptId')" ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">投资额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.investMoney" name="investMoney" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                            <%--                                <label class="col-md-2 control-label required">合同额（万元）</label>
                                                            <div class="col-md-4">
                                                                <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney" required="true"    ng-disabled="!processInstance.firstTask"/>
                                                            </div>--%>
                            <label class="col-md-2 control-label required">投资来源</label>
                            <div class="col-md-4">
                                <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'投资来源'}:true"
                                        ng-model="vm.item.investSource" class="form-control"
                                        ng-disabled="!processInstance.firstTask"></select> </div>
                            <label class="col-md-2 control-label required">项目性质</label>
                            <div class="col-md-4">
                                <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                        ng-model="vm.item.projectNature" class="form-control"
                                        ng-disabled="!(processInstance.firstTask)"></select>
                            </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">建设规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionScale" name="constructionScale"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">建设地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionArea" name="constructionArea" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否开具保函</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否开具保函'}:true"
                                            ng-model="vm.item.backletter" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label" ng-if="vm.item.backletter">保函金额(万元)</label>
                                <div class="col-md-4">
                                    <input  ng-if="vm.item.backletter" ng-model="vm.item.backletterMoney" class="form-control" ng-disabled="!processInstance.firstTask" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input ng-model="vm.item.remark" class="form-control" ng-disabled="!(processInstance.firstTask)">
                                </div>
                            </div>
<%--                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否加盖法人签字章</label>
                                <div class="col-md-2">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.legalPerson" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否加盖财务法人签字章</label>
                                <div class="col-md-2">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.businessLegalPerson" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否加盖公章</label>
                                <div class="col-md-2">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.commonSeal" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否加盖电子法人签字章</label>
                                <div class="col-md-2">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.eleLegalPerson" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否加盖电子财务法人签字章</label>
                                <div class="col-md-2">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.eleBusinessLegalPerson" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否含合同附件</label>
                                <div class="col-md-2">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.attach" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>--%>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_4"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form4">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'评审级别'}:true"
                                            ng-model="vm.item.reviewLevel" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.reviewLevel=='公司级'">公司级评审人员</label>
                                <div class="col-md-4" ng-if="vm.item.reviewLevel=='公司级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.reviewUserName" required="true" name="reviewUserName"   disabled />
                                        <span class="input-group-btn" ng-if="user.userLogin!='2623'">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUser');" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部人员（合同）')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                        <span class="input-group-btn" ng-if="user.userLogin=='2623'">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUser');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">经营发展部人员（合同）选择评审人员</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">院级评审人员</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptReviewUserName" required="true" name="deptReviewUserName"   readonly/>
                                        <span class="input-group-btn" ng-if="user.userLogin!='2623'">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptReviewUser');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                        <span class="input-group-btn" ng-if="user.userLogin=='2623'">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptReviewUser');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">请发起人选择院级评审人员</span>
                                </div>
                                <label class="col-md-2 control-label required">总设计师</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.totalDesignerName" name="totalDesignerName"  required="true" readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('totalDesigner');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同分管副总</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessChargeLeaderName" name="businessChargeLeaderName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('businessChargeLeader');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目经理</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input  type="text" class="form-control" ng-model="vm.item.projectManagerName" name="projectManagerName"  required="true" readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('projectManager');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">外贸标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'外贸标记'}:true"
                                            ng-model="vm.item.foreignTradeMark" class="form-control"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                                <label class="col-md-2 control-label required">民用标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'民用标记'}:true"
                                            ng-model="vm.item.civilMark" class="form-control"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">军品标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'军品标记'}:true"
                                            ng-model="vm.item.militaryMark" class="form-control"
                                            ng-disabled="!(processInstance.firstTask)"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否含合同附件</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.attach" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>

                            </div>
                            <%--<div class="form-group">
                                <label class="col-md-2 control-label required">项目专业分类 一级</label>
                                <div class="col-md-4">
                                    <select id="select" ng-options="s.name as s.name for s in vm.projectMajorTypeFirst" &lt;%&ndash;id="editable-select"&ndash;%&gt;
                                            ng-model="vm.item.projectMajorTypeFirst" class="form-control"
                                            ng-disabled="!processInstance.firstTask">
                                    </select>
                                </div>
                                <label class="col-md-2 control-label required">项目专业分类 二级</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectMajorTypeSecond" name="projectMajorTypeSecond"   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部人员')>=0)"/>
                                </div>

                            </div>--%>
                           <%-- <div class="form-group">
                                <label class="col-md-2 control-label required">项目专业分类 三级</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectMajorTypeThird" name="projectMajorTypeThird"    ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部人员')>=0)"/>
                                </div>
                            </div>--%>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_5"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form5">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>签约日期</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="signTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.signTime" required="true"
                                               ng-disabled="processInstance.myRunningTaskName.indexOf('合同上传')<0&&processInstance.myRunningTaskName.indexOf('确认')<0">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('合同上传')<0&&processInstance.myRunningTaskName.indexOf('确认')<0"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">合同盖章页扫描附件</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractAttachUrl"
                                               name="contractAttachUrl" maxlength="100" disabled="disabled"/>
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadHead" class="btn blue fileinput-button btn-sm" ng-disabled="processInstance.myRunningTaskName.indexOf('合同上传')<0&&processInstance.myRunningTaskName.indexOf('确认')<0">
                                          <i class="fa fa-cloud-upload" style="height:15px "></i>
                                        <span>上传</span>
                                             <input type="file" name="multipartFile" id="contractAttachUrl" accept="*" multiple="multiple"/>
                                   </span>
                                        <a ng-href="{{vm.item.contractAttachUrl}}" target="_blank">
                                        <span id="btnDownloadHead" class="btn green fileoutput-button btn-sm">
                                     <i class="fa fa-cloud-download"
                                        title="下载"></i>
                                            <span>下载</span>
                                        </span>
                                            </a>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">合同额(万元)</label>
                                <div class="col-md-4">
                                    <input ng-model="vm.item.contractMoney" class="form-control" ng-disabled="processInstance.myRunningTaskName.indexOf('印花税')<0&&processInstance.myRunningTaskName.indexOf('盖章合同上传')<0">
                                </div>
                                <label class="col-md-2 control-label required" style="color: red">税目</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同印花税税目'}:true"
                                            ng-model="vm.item.taxType" class="form-control" ng-change="vm.save()"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('印花税')<0"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" >税率</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.taxNum"  ng-disabled="vm.item.taxType!='其他'||processInstance.myRunningTaskName.indexOf('印花税')<0">
                                        <span class="input-group-addon">%</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label" >印花税额(万元)</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">￥</span>
                                        <input type="text" class="form-control" ng-model="vm.item.stampTaxMoney"  readonly>
                                        <span class="input-group-addon">万元</span>
                                    </div>
                                     <span style=" margin-left:40px;font-size: 13px;color: red" >{{(vm.item.stampTaxMoney*10000).toFixed(2)+'元'}}</span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 客户信息
        </div>
        <div  class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.addCustomer(vm.item.id);" >
                <i class="fa fa-save"></i> 保存客户信息 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.showCustomer(vm.item.customerId);" >
                <i class="fa fa-save"></i> 完善客户信息 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" ng-disabled="!processInstance.firstTask" />
                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                        </div>
                    </div>
                    <label class="col-md-2 control-label required">客户编号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" ng-disabled="!processInstance.firstTask"
                               required="true" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">客户地址</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="" ng-model="vm.item.customerAddress"  ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">联系人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true" ng-disabled="!processInstance.firstTask" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人职务</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTitle" name="linkTitle" required="true"   ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">联系电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTel" name="linkTel" required="true"   ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>--%>
<%--<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 评审记录
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDetail();"
               ng-if="processInstance.myRunningTaskName!=''">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th >评审内容</th>
                    <th style="width: 100px">评审结果</th>
                    <th style="width: 60px;" ng-if="processInstance.myRunningTaskName!=''">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.contractReviewDetails">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.reviewContent" style="color: blue" ng-click="vm.showDetail(detail.id);" ></td>
                    <td ng-bind="detail.reviewResult"></td>
                    <td ng-if="processInstance.myRunningTaskName!=''">
                        <i class="fa fa-edit margin-right-10" ng-click="vm.showDetail(detail.id);"
                           title="详情" ></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除"
                           ></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>--%>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">评审记录</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="comment_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">评审内容</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="评审内容" name="baseName"
                                       ng-model="vm.detail.reviewContent" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">评审结果</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="评审结果" name="baseComment"
                                       ng-model="vm.detail.reviewResult" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">顺序</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       ng-model="vm.detail.seq" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">创建时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.detail.gmtCreate | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">修改时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.detail.gmtModified | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();" ng-show="processInstance.myRunningTaskName!=''">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="selectCustomerModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">客户名称</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>客户名称</th>
                            <th>客户编号</th>
                            <th>客户地址</th>
                            <th>录入部门</th>
                            <th>联系人</th>
                            <th>联系电话</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.customers|filter:{name:vm.qCustomer}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.name"></td>
                            <td ng-bind="c.code"></td>
                            <td ng-bind="c.address"></td>
                            <td ng-bind="c.deptName"></td>
                            <td ng-bind="c.linkMan"></td>
                            <td ng-bind="c.linkTel"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectCustomer();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="selectLibraryModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="portlet  box blue" style="width: 60%; margin-left: 20% ;margin-top: 5%">
        <div class="portlet-title">
            <div class="caption">
                <i class="icon-clock"></i>选择合同库中的合同
                <small >当前已选择合同：<span ng-bind="vm.item.mainContractLibraryName"></span></small>
            </div>
        </div>
        <div class="portlet-body" >
            <div class="tabbable-custom">
                <ul class="nav nav-tabs ">
                    <li class="active">
                        <a href="#tab_15_review" data-toggle="tab" aria-expanded="true">
                            合同库
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab_15_review" style="min-height: 400px;">
                        <div class="modal-body" >
                            <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th>项目名称</th>
                                        <th class="hidden-md hidden-sm">项目号</th>
                                        <th>合同名称</th>
                                        <th class="hidden-md hidden-sm">合同号</th>
                                        <th>合同额（万元）</th>
                                        <th style="width: 90px">签约日期</th>
                                        <th style="width: 100px;">创建时间</th>
                                        <th style="width: 100px;">合同来源</th>
                                        <th style="width: 30px;">补充合同标识</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.librarys">
                                        <td>
                                            <input type="checkbox" ng-checked="vm.item.mainContractLibraryId==item.id" class="cb_library"
                                                   data-name="{{item}}" style="width: 15px;height: 15px;"/>
                                        </td>
                                        <td ng-bind="item.projectName"  class="data_title" ><strong ></strong></td>
                                        <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                                        <td ng-bind="item.contractName"  class="data_title" ><strong ></strong></td>
                                        <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>

                                        <td ng-bind="item.contractMoney " class="hidden-md hidden-sm"></td>
                                        <td ng-bind="item.signTime"></td>
                                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId==0"><span style="color: red" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单</span></strong>
                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId!=0"><span style="color: green" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单(已补录)</span></strong>
                                            <strong   ng-if="item.contractReviewId!=0&&item.contractPreId==0"><span style="color: blue" ng-click="vm.opt=0;vm.showNew(item.contractReviewId)">合同评审</span></strong>
                                            <strong  ng-if="item.contractReviewId==0&&item.preContractId==0" ><span style="color: grey">其他</span></strong>
                                        </td>
                                        <td class="text-center">
                                            <span class="label label-sm label-success"  ng-if="item.main">是</span>
                                            <span class="label label-sm label-default" ng-if="!item.main">否</span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn blue" ng-click="vm.saveSelectModel();">确认</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal fade draggable-modal" id="selectRecordModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4>选择项目信息备案</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProject"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>项目名称</th>
                            <th style="width: 80px;">项目类型</th>
                            <th>客户名称</th>
                            <th>所属部门</th>
                            <th style="width: 110px;">投资额(万元)</th>
                            <th>参与人</th>
                            <th style="width: 40px;">是否已录入合同</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="record in vm.listRecord|filter:{projectName:vm.qProject}" >
                            <td>
                                <input type="checkbox" ng-checked="vm.item.recordId==record.id" class="cb_record"
                                       data-name="{{record}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="record.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                            <td ng-bind="record.projectType"></td>
                            <td ng-bind="record.customerName"></td>
                            <td ng-bind="record.deptName"></td>
                            <td ng-bind="record.projectInvest|currency : '￥'"></td>
                            <td ng-bind="record.businessUserName"></td>
                            <td class="text-center">
                                <span class="label label-sm label-success" style="cursor: pointer;" ng-if="record.contractReviewId!=0">是</span>
                                <span class="label label-sm label-default" ng-if="record.contractReviewId==0">否</span>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>


            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectRecordModel()">确认</button>
            </div>
        </div>
    </div>

</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>