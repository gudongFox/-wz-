<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>项目启动</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.projectName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 项目启动
            <small ng-bind="vm.item.contractNo"></small>
            <small ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="!disableFlag||processInstance.passAble||vm.item.processInstanceId=='0'"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.fetchAble"
               ng-click="commonFetchFlow();">
                <i class="fa fa-send-o" title="后续流程用户未接收任务"></i> 取回任务 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
               ng-click="vm.showSendFlow();">
                <i class="fa fa-send"></i> {{processInstance.firstTask?'提交审批':'同意通过'}} </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.backAble"
               ng-click="commonBackFlow();">
                <i class="fa fa-reply"></i> 打回修改 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" ng-show="processInstance.printAble">
                <i class="fa fa-print"></i> 打印 </a>

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
<%--                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>--%>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">工程名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" required="true"
                                               ng-disabled="disableFlag"/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectPreOrReviewModal()"
                                                ng-disabled="disableFlag"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目类型</label>
                                <div class="col-md-4 ">
                                    <select   class="form-control" ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                             ng-disabled="disableFlag" ng-model="vm.item.contractType">
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractNo"
                                               name="contractNo" required="true" maxlength="20" placeholder="合同编号"
                                               ng-disabled="disableFlag"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-disabled="disableFlag"
                                                    ng-click="vm.refreshContractNo();"> <i
                                                    class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目行业</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'项目行业'}:true"
                                            ng-model="vm.item.contractScope" name="contractScope" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同额(万元)</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.contractMoney"
                                           placeholder="单位(万元),保留2位小数"
                                           name="contractMoney" maxlength="20" required
                                           ng-disabled="disableFlag"/>
                                </div>
                                <label class="col-md-2 control-label required">投资额(万元)</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.investMoney"
                                           placeholder="单位(万元),保留2位小数"
                                           name="investMoney" maxlength="20" required
                                           ng-disabled="disableFlag"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">执行所室</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                              disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);vm.opt='所属部门'"
                                                    ng-disabled="disableFlag"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">合作部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptNames"
                                               name="deptNames" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showCooperDeptModal(vm.item.deptIds);vm.opt='合作部门'"
                                                    ng-disabled="disableFlag">
                                                <i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否有中标通知书</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否默认'}:true"
                                            ng-model="vm.item.bidNotice" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                                <label class="col-md-2 control-label required">中标通知书是否返回市场部</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否默认'}:true"
                                            ng-model="vm.item.bidBack" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">业务承接方式</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.acceptMode"
                                           name="acceptMode" maxlength="20"
                                           ng-disabled="disableFlag"/>
                                </div>

                                <label class="col-md-2 control-label required">是否为集团内部签订</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否默认'}:true"
                                            ng-model="vm.item.group" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">主合同是否签订</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否默认'}:true"
                                            ng-model="vm.item.signed" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否院签合同</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否默认'}:true"
                                            ng-model="vm.item.company" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">签订人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.signerName"
                                           name="signerName" required="true" maxlength="20"
                                           ng-disabled="disableFlag"/>
                                </div>
                                <label class="col-md-2 control-label required">签订日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="signDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.signDate" ng-disabled="disableFlag">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"
                                                        ng-disabled="disableFlag"><i
                                                        class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">
                                    <strong>启用设计管理</strong>
                                </label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否EPC'}:true"
                                            ng-model="vm.item.ed" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                                <label class="col-md-2 control-label required">设计管理类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'设计作业类型'}:true"
                                            ng-model="vm.item.projectType" class="form-control"
                                            ng-disabled="disableFlag"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" >项目阶段</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.stageNames"
                                               name="stageNames" maxlength="100" required="true"
                                               ng-disabled="disableFlag"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showStageModel(vm.item.stageNames);"
                                                    ng-disabled="disableFlag"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.chargeMenName"
                                                name="chargeMenName"
                                             disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.opt ='chargeMen';vm.optUser=vm.item.chargeMen;vm.showUserModel();"
                                                ng-disabled="disableFlag"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">执行负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.exeChargeMenName"
                                               name="exeChargeMenName" disabled
                                               />
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.optUser=vm.item.exeChargeMen;vm.opt='exeChargeMen';vm.showUserModel();"
                                                ng-disabled="disableFlag">
                                            <i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">经营经理</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessManagerName"
                                               name="businessManager"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.opt ='businessManager';vm.optUser=vm.item.businessManager;vm.showUserModel();"
                                                ng-disabled="disableFlag"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>

                                </div>
                            </div>

                        </div>
                    </form>
                </div>
<%--                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>--%>
            </div>

        </div>
    </div>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 项目信息
        </div>
        <div class="tools">
            <a href="javascript:;" class="collapse" data-original-title="" title="">
            </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required">省份</label>
                    <div class="col-md-4">
                        <select name="province" class="form-control"
                                ng-model="vm.provinceId" ng-change="vm.provinceChange(vm.provinceId);"
                                ng-options="id as name for (id,name) in vm.provinceArr" ng-disabled="disableFlag"></select>
                    </div>

                    <label class="col-md-2 control-label required">城市</label>
                    <div class="col-md-4">
                        <select name="city" class="form-control" ng-init="city.id='0'"
                                ng-model="vm.cityId" ng-change="vm.cityChange(vm.cityId);"
                                ng-options="id as name for (id,name) in vm.getCityArr" ng-disabled="disableFlag"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">工程地址</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" required="true"
                               ng-model="vm.item.projectAddress"
                               name="projectAddress" maxlength="450"
                               ng-disabled="disableFlag"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">建筑面积(万㎡)</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.constructionArea"
                               placeholder="单位(万㎡),保留2位小数"
                               name="constructionArea" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                    <label class="col-md-2 control-label">占地面积(万㎡)</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.floorArea"
                               placeholder="单位(万㎡),保留2位小数"
                               name="floorArea" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label ">道路长度(Km)</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.roadLength"
                               placeholder="单位(Km),保留2位小数"
                               name="roadLength" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                    <label class="col-md-2 control-label">其他规模</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.otherScale"
                               name="otherScale" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">工期（月）</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.constructionTime"
                               placeholder=""
                               name="constructionTime" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>


                    <label class="col-md-2 control-label">报量情况</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.reportAmount"
                               placeholder=""
                               name="reportAmount" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>


<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 客户信息
        </div>
        <div class="tools">
            <a href="javascript:;" class="collapse" data-original-title="" title="">
            </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required">客户名称</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.customerName"
                                   name="customerName" required="true" ng-disabled="disableFlag"/>
                            <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showCustomerModal();"
                                                    ng-disabled="disableFlag"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                        </div>
                    </div>
                    <label class="col-md-2 control-label required">单位性质</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户单位类型'}:true"
                                ng-model="vm.item.customerType" class="form-control"
                                ng-disabled="disableFlag"></select>
                    </div>
                </div>
                <div class="form-group">

                    <label class="col-md-2 control-label required">联系人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" required="true" ng-model="vm.item.linkMan"
                               ng-disabled="disableFlag"/>
                    </div>
                    <label class="col-md-2 control-label required">联系电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" id="linkTel" ng-model="vm.item.linkTel"
                               name="linkTel" maxlength="30" ng-disabled="disableFlag"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">电子邮件</label>
                    <div class="col-md-4">
                        <input type="email" class="form-control" ng-model="vm.item.linkMail"
                               name="linkMail" placeholder="Email" maxlength="50"
                               ng-disabled="disableFlag"/>
                    </div>
                    <label class="col-md-2 control-label">传真</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkFax"
                               ng-disabled="disableFlag"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">开户银行</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.depositBank"
                               name="depositBank" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                    <label class="col-md-2 control-label">银行账号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.bankAccount"
                               name="bankAccount" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">纳税人识别号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.taxNo" name="taxNo"
                               maxlength="20" ng-disabled="disableFlag"/>
                    </div>

                    <label class="col-md-2 control-label">纳税主体资格</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'纳税主体资格'}:true"
                                ng-model="vm.item.taxType" class="form-control"
                                ng-disabled="disableFlag"></select>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 其他
        </div>
        <div class="tools">
            <a href="javascript:;" class="collapse" data-original-title="" title="">
            </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label">用印时间</label>
                    <div class="col-md-4">
                        <div class="input-group date date-picker" id="stampTime">
                            <input type="text" class="form-control"
                                   ng-model="vm.item.stampTime" ng-disabled="disableFlag">
                            <span class="input-group-btn">
												<button class="btn default" type="button"
                                                        ng-disabled="disableFlag"><i
                                                        class="fa fa-calendar"></i></button>
                                        </span>
                        </div>
                    </div>
                    <label class="col-md-2 control-label">返回时间</label>
                    <div class="col-md-4">
                        <div class="input-group date date-picker" id="backTime">
                            <input type="text" class="form-control"
                                   ng-model="vm.item.backTime" ng-disabled="disableFlag">
                            <span class="input-group-btn">
												<button class="btn default" type="button"
                                                        ng-disabled="disableFlag"><i
                                                        class="fa fa-calendar"></i></button>
                                        </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">原件份数</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.originalCount"
                               placeholder=""
                               name="originalCount" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                    <label class="col-md-2 control-label">复印件份数</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.copyCount"
                               placeholder=""
                               name="copyCount" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">用印份数</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.stampCount"
                               placeholder=""
                               name="stampCount" maxlength="20"
                               ng-disabled="disableFlag"/>
                    </div>
                    <label class="col-md-2 control-label">备注</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"
                               maxlength="100" ng-disabled="disableFlag"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label">创建人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.creatorName"
                               disabled="disabled"/>
                    </div>

                    <label class="col-md-2 control-label">修改时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>

<div class="modal fade draggable-modal" id="selectPreOrReviewModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="portlet  box blue" style="width: 60%; margin-left: 20% ;margin-top: 5%">
        <div class="portlet-title">
            <div class="caption">
                <i class="icon-clock"></i>选择合同的前置项目
                <small >当前已选择前置项目：<span ng-bind="vm.showChooseStatus"></span></small>
            </div>
        </div>
        <div class="portlet-body" >
            <div class="tabbable-custom">
                <ul class="nav nav-tabs ">
                    <li class="active">
                        <a href="#tab_15_record" data-toggle="tab" aria-expanded="true">
                            项目备案
                        </a>
                    </li>
                    <li class="">
                        <a href="#tab_15_pre" data-toggle="tab" aria-expanded="false">
                            超前任务单 </a>
                    </li>

                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab_15_record"
                         style="min-height: 400px;">
                        <div class="modal-body" >
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
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="record in vm.listRecord" >
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
                                    </tr>

                                    </tbody>
                                </table>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn blue" ng-click="vm.saveSelectModel(1);">确认</button>
                        </div>
                    </div>

                    <div class="tab-pane" id="tab_15_pre"
                         style="min-height: 400px;">
                        <div class="modal-body">
                            <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th>项目名称</th>
                                        <th>承接部门</th>
                                        <th style="width: 110px;">投资额(万元)</th>
                                        <th>委托方</th>
                                        <th>项目负责人(总师或项目经理)</th>
                                        <th style="width: 80px;">设计任务类型</th>
                                        <th style="width: 100px;">设计阶段</th>
                                        <th style="width: 75px;">设计管理</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.preContracts" ng-show="item.contractId!=vm.item.contractId&&item.projectName!=''">
                                        <td>
                                            <input type="checkbox" ng-checked="vm.item.projectName==item.projectName" class="cb_preContract"
                                                   data-name="{{item}}" style="width: 15px;height: 15px;"/>
                                        </td>
                                        <td ng-bind="item.projectName"  class="data_title"  ></td>
                                        <td ng-bind="item.deptName"></td>
                                        <td ng-bind="item.investMoney|currency : '￥'"></td>
                                        <td ng-bind="item.customerName"></td>
                                        <td ng-bind="item.chargeMenName"></td>
                                        <td ng-bind="item.designType"></td>
                                        <td ng-bind="item.stageName"></td>
                                        <td class="text-center">
                                            <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.ed" >启用</span>
                                            <span class="label label-sm label-default" ng-if="!item.ed">关闭</span>
                                        </td>
                                    </tr>

                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn blue" ng-click="vm.saveSelectModel(0);">确认</button>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="contractStageNameModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">项目阶段</h4>
            </div>
            <div class="modal-body">

                <div class="table-scrollable">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>名字</th>
                            <th>编码</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.stageNames">
                            <td>
                                <input type="checkbox" ng-checked="item.selected" class="cb_stage"
                                       style="width: 15px;height: 15px;"
                                       name="{{item.code}}"/>
                            </td>
                            <td ng-bind="item.name"></td>
                            <td ng-bind="item.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectStageName();">确认</button>
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

<div class="modal fade draggable-modal" id="cooperDeptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择合作部门</h4>
            </div>
            <div class="modal-body">
                <div id="cooper_dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveCooperDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="selectEmployeeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.titleName">人员任命</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qEmployee"/>
                        </div>
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="部门名称"
                                   ng-model="vm.qEmployeeDeptName"/>
                        </div>
                        <div class="col-md-8">
                            <a class="btn green btn-sm" ng-click="vm.loadEmployee();"><i class="fa fa-search"></i> 查询
                            </a>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">序号</th>
                                <th>姓名</th>
                                <th>登录名</th>
                                <th>所属部门</th>
                                <th>员工专业</th>
                                <th>联系电话</th>
                                <th>员工类别</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.employees|filter:{deptName:vm.qEmployeeDeptName}|filter:{userName:vm.qEmployee}">
                                <td>
                                    <input type="checkbox" class="cb_employee"
                                           ng-checked="vm.optUser.indexOf(item.userLogin)>=0"
                                           data-name="{{item.userLogin+'_'+item.userName}}"
                                           style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="item.userName"></td>
                                <td ng-bind="item.userLogin"></td>
                                <td>
                                    <span ng-bind="item.deptName"></span>
                                </td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.mobile"></td>
                                <td ng-bind="item.userType">
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectUser();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="selectCustomerModal" tabindex="-1" role="basic" aria-hidden="true">
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
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>客户名称</th>
                            <th>客户地址</th>
                            <th>单位性质</th>
                            <th>联系人</th>
                            <th>联系电话</th>
                            <th>录入部门</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.customers|filter:{name:vm.qCustomer}" ng-show="c.name!=''">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.name"></td>
                            <td ng-bind="c.address"></td>
                            <td ng-bind="c.customerType"></td>
                            <td ng-bind="c.linkMan"></td>
                            <td ng-bind="c.linkTel"></td>
                            <td ng-bind="c.deptName"></td>
                            <td ng-bind="c.gmtCreate|date:'yyyy-MM-dd'"></td>
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


<jsp:include page="../ed/project/part-actFlow.jsp"/>

