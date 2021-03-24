<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>专业岗位资格管理</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="row">

    <div class="col-md-3" style="padding-right: 0px;">
        <div class="portlet box blue" >
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 组织机构
                </div>
                <div class="tools">
                    <i class="fa fa-refresh margin-right-5" title="刷新" ng-click="vm.buildTree();"></i>
                </div>
            </div>
            <div class="portlet-body">
                <div id="dept_tree" ></div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-database"></i> 专业岗位资格管理
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click=" vm.loadData(user.userLogin);">
                        <i class="fa fa-refresh"></i> 刷新 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">用户名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="用户名称"
                                                                           ng-model="vm.params.qUserName"></label>

                            <label style="margin-right: 10px;">部门名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="部门名称"
                                                                           ng-model="vm.params.qDeptName"></label>
                            <label style="margin-left: 20px;margin-right: 20px;"> 视图：
                                <select class="form-control input-inline input-sm" ng-model="vm.params.seqType" ng-change="vm.changeType()">
                                    <option value="工程设计">工程设计</option>
                                    <option value="工程咨询" >工程咨询</option>
                                    <option value="工程承包" >工程承包</option>
                                    <option value="勘察" >勘察</option>
                                    <option value="监理" >监理</option>
                                </select>
                            </label>

                            <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i
                                    class="fa fa-search"></i> 查询 </a>

                            <a class="btn blue btn-sm" ng-click="vm.initWuzhouData();" ng-if="rightData.add"><i
                                    class="fa fa-search"></i> 初始化数据 </a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">#</th>
                                <th style="width: 100px">姓名</th>
                                <th>部门名称</th>
                                <th style="width: 100px">专业</th>
                                <%--工程设计--%>
                                <th style="width: 100px" ng-if="vm.showDate.design">设计人</th>
                                <th style="width: 100px" ng-if="vm.showDate.design">校核人</th>
                                <th style="width: 100px" ng-if="vm.showDate.design">审核人</th>
                                <%--工程咨询--%>
                                <th style="width: 100px" ng-if="vm.showDate.consult">设计人</th>
                                <th style="width: 100px" ng-if="vm.showDate.consult">校核人</th>
                                <th style="width: 100px" ng-if="vm.showDate.consult">审核人</th>
                                <%--工程承办--%>
                                <th style="width: 100px" ng-if="vm.showDate.contract">设计人</th>
                                <th style="width: 100px" ng-if="vm.showDate.contract">校核人</th>
                                <th style="width: 100px" ng-if="vm.showDate.contract">审核人</th>
                                <%--勘察--%>
                                <th style="width: 100px" ng-if="vm.showDate.explore">项目工程师</th>
                                <th style="width: 100px" ng-if="vm.showDate.explore">项目负责人</th>
                                <th style="width: 100px" ng-if="vm.showDate.explore">项目审核人</th>
                                <%--监理--%>
                                <th style="width: 100px" ng-if="vm.showDate.supervisor">设计人</th>
                                <th style="width: 100px" ng-if="vm.showDate.supervisor">校核人</th>
                                <th style="width: 100px" ng-if="vm.showDate.supervisor">审核人</th>

                                <th style="width: 100px">审定-院级</th>
                                <th style="width: 100px">审定-公司级</th>
                                <th style="width: 100px">总设计师</th>
                                <th style="width: 100px;"  ng-if="rightData.edit">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.userName" class="data_title" ng-click="vm.show(item.id,rightData.edit);"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-click="vm.qualify=item;vm.showSelectMajor(rightData.edit);" >
                                    <span ng-bind="item.majorName" ></span>
                                    <i class="fa fa-cog"></i>
                                </td>
                                <%--工程设计--%>
                                <td ng-click="vm.toggleEnable(item.id,'设计人',rightData.edit)" ng-if="vm.showDate.design">
                                    <span ng-bind="item.design?'√':'×'" style="color:{{item.design?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'校核人',rightData.edit)" ng-if="vm.showDate.design">
                                    <span ng-bind="item.proofread?'√':'×'" style="color:{{item.proofread?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'审核人',rightData.edit)" ng-if="vm.showDate.design">
                                    <span ng-bind="item.audit?'√':'×'" style="color:{{item.audit?'green':'red'}}"></span>
                                </td>
                                <%--工程咨询--%>
                                <td ng-click="vm.toggleEnable(item.id,'consultDesign',rightData.edit)" ng-if="vm.showDate.consult">
                                    <span ng-bind="item.consultDesign?'√':'×'" style="color:{{item.consultDesign?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'consultProofread',rightData.edit)" ng-if="vm.showDate.consult">
                                    <span ng-bind="item.consultProofread?'√':'×'" style="color:{{item.consultProofread?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'consultAudit',rightData.edit)" ng-if="vm.showDate.consult">
                                    <span ng-bind="item.consultAudit?'√':'×'" style="color:{{item.consultAudit?'green':'red'}}"></span>
                                </td>
                                <%--工程承包--%>
                                <td ng-click="vm.toggleEnable(item.id,'contractDesign',rightData.edit)" ng-if="vm.showDate.contract">
                                    <span ng-bind="item.contractDesign?'√':'×'" style="color:{{item.contractDesign?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'contractProofread',rightData.edit)" ng-if="vm.showDate.contract">
                                    <span ng-bind="item.contractProofread?'√':'×'" style="color:{{item.contractProofread?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'contractAudit',rightData.edit)" ng-if="vm.showDate.contract">
                                    <span ng-bind="item.contractAudit?'√':'×'" style="color:{{item.contractAudit?'green':'red'}}"></span>
                                </td>
                                <%--勘察--%>
                                <td ng-click="vm.toggleEnable(item.id,'exploreEngineer',rightData.edit)" ng-if="vm.showDate.explore">
                                    <span ng-bind="item.exploreEngineer?'√':'×'" style="color:{{item.exploreEngineer?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'exploreAudit',rightData.edit)" ng-if="vm.showDate.explore">
                                    <span ng-bind="item.exploreAudit?'√':'×'" style="color:{{item.exploreAudit?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'explorePrincipal',rightData.edit)" ng-if="vm.showDate.explore">
                                    <span ng-bind="item.explorePrincipal?'√':'×'" style="color:{{item.explorePrincipal?'green':'red'}}"></span>
                                </td>
                                <%--监理--%>
                                <td ng-click="vm.toggleEnable(item.id,'supervisorDesign',rightData.edit)" ng-if="vm.showDate.supervisor">
                                    <span ng-bind="item.supervisorDesign?'√':'×'" style="color:{{item.supervisorDesign?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'supervisorProofread',rightData.edit)" ng-if="vm.showDate.supervisor">
                                    <span ng-bind="item.supervisorProofread?'√':'×'" style="color:{{item.supervisorProofread?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'supervisorAudit',rightData.edit)" ng-if="vm.showDate.supervisor">
                                    <span ng-bind="item.supervisorAudit?'√':'×'" style="color:{{item.supervisorAudit?'green':'red'}}"></span>
                                </td>


                                <td ng-click="vm.toggleEnable(item.id,'审定人-院级',rightData.edit)">
                                    <span ng-bind="item.approve?'√':'×'" style="color:{{item.approve?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'审定人-公司级',rightData.edit)">
                                    <span ng-bind="item.majorCharge?'√':'×'" style="color:{{item.majorCharge?'green':'red'}}"></span>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'总设计师',rightData.edit)">
                                    <span ng-bind="item.chiefDesigner?'√':'×'" style="color:{{item.chiefDesigner?'green':'red'}}"></span>
                                </td>
                                <td ng-if="rightData.edit">
                                    <i class="fa fa-edit margin-right-5" title="编辑" ng-click="vm.show(item.id)" ></i>
                                    <i class="fa fa-copy margin-right-5" title="复制" ng-click="vm.copy(item)"></i>
                                    <i class="fa fa-trash" title="删除" ng-click="vm.remove(item.id);" ng-if="rightData.deleted"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
                </div>
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
                                <input type="checkbox" ng-checked="vm.qualify.majorName.indexOf(major)>=0" class="cb_major" data-name="{{major}}" style="width: 15px;height: 15px;"/>
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

