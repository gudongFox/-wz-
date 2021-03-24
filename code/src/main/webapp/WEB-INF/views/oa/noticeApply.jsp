
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:40})">公文管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">信息发布</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span ng-bind="tableName">信息发布</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add(); " ng-show="rightData.selectOpts.indexOf('增加')>=0">
                <i class="fa fa-plus"></i> 新建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >

                <label style="margin-left: 20px;">公告标题：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="公告标题"
                                                              ng-model="vm.params.noticeTitles"></label>
                <label style="margin-right: 20px;">公告类别：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="公告类别"
                                                               ng-model="vm.params.noticeTypes">
                    <a ng-click="vm.listCommonType()" ><i class="fa fa-plus" ></i></a>
                </label>


                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>

        <div class="table-scrollable">
            <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>公告标题</th>
                    <th>发布部门</th>
                    <th style="width: 90px">发布板块</th>
                    <th style="width: 200px">发布人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 130px;">任务状态</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list" on-finish-render>
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.noticeTitle"  class="data_title" ng-click="vm.show(item.id,item.stampType);"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.noticeType"></td>
                    <td ng-bind="item.publishUserName"></td>
                    <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.processName"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id,item.stampType);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除"ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0" ></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

<div class="modal fade draggable-modal" id="commonTypeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">公告类别</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qName"/>
                    </div>
                    <div class="col-md-4">
                        <a href="javascript:;" class="btn btn-sm btn-default"  ng-click="vm.addCommonType();">
                            <i class="fa fa-plus"></i> 新增 </a>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>名称</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 90px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="commonType in vm.commonList|filter:{name:vm.qName}" >
                            <td ng-bind="$index+pageInfo.startRow+1"></td>
                            <td ng-bind="commonType.name"></td>
                            <td ng-bind="commonType.gmtModified|date:'yyyy-MM-dd'"></td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.showCommonType(commonType.id);" title="详情" ></i>
                                <i class="fa fa-trash-o" ng-click="vm.removeCommonType(commonType.id);" title="删除" ng-show="commonType.name!='部门发文'"></i>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="itemModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">字典详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detail_form">
                    <div class="form-body">

                        <div class="form-group">
                            <label class="col-md-3 control-label required">名字</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.commonType.name" required="true"
                                       maxlength="200" name="name" placeholder="">
                            </div>
                        </div>
                        <%--   <div class="form-group">
                               <label class="col-md-3 control-label required">数据类型</label>
                               <div class="col-md-9">
                                   <select ng-model="vm.commonType.codeType" name="codeType" class="form-control" >
                                       <option value="String" >String</option>
                                   </select>
                               </div>
                           </div>--%>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">顺序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.commonType.seq" required name="seq" placeholder="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <textarea id="maxlength_textarea" ng-model="vm.commonType.remark" name="remark"  class="form-control" maxlength="225" rows="1" placeholder="">
                                </textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">创建时间</label>
                            <div class="col-md-9">
                                <div class="input-icon right">
                                    <i class="fa fa-clock-o"></i>
                                    <input type="text" class="form-control"
                                           value="{{vm.commonType.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">修改时间</label>
                            <div class="col-md-9">
                                <div class="input-icon right">
                                    <i class="fa fa-clock-o"></i>
                                    <input type="text" class="form-control"
                                           value="{{vm.commonType.gmtModified |date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveCommonType();" ng-show="vm.commonType.name!='部门发文'">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
</div>

