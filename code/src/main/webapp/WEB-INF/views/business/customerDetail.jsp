<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="business.customer">经营管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="business.customer">客户信息管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.name"></span>
        </li>

    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user-following"></i> 客户信息管理
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();" ng-show="rightData.selectOpts.indexOf('修改')>=0">
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
                    <a href="#tab_basic" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li>
                    <a href="#tab_contract" data-toggle="tab" aria-expanded="true">
                        合作项目 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_basic" style="height: 460px;overflow-y: auto;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">客户编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.code"
                                           required="true" maxlength="50"/>
                                </div>
                                <label class="col-md-2 control-label required">客户名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户名称" ng-model="vm.item.name"
                                           required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人姓名" ng-model="vm.item.linkMan"
                                           name="linkMan" required="true" maxlength="50"/>
                                </div>
                                <label class="col-md-2 control-label required">客户地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户地址" ng-model="vm.item.address" name="address" required
                                           maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">


                                <label class="col-md-2 control-label required">联系人职务</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人职务"
                                           ng-model="vm.item.linkTitle" name="linkTitle" required="true"
                                           maxlength="20"/>
                                </div>
                                <label class="col-md-2 control-label required">联系人电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人电话"
                                           ng-model="vm.item.linkTel" name="linkTel" required="true" maxlength="20"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">行业类别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户行业类别'}:true"
                                            ng-model="vm.item.customerScope" class="form-control"
                                            name="customerScope"></select>
                                </div>
                                <label class="col-md-2 control-label required">单位类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户单位类型'}:true"
                                            ng-model="vm.item.customerType" class="form-control"
                                            name="customerType"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">传真号码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="传真号码" ng-model="vm.item.linkFax"
                                           name="linkFax" maxlength="20"/>
                                </div>

                                <label class="col-md-2 control-label">电子邮件</label>
                                <div class="col-md-4">
                                    <input type="email" class="form-control" placeholder="Email"
                                           ng-model="vm.item.linkMail" name="linkMail" maxlength="20"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">开户银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="开户银行"
                                           ng-model="vm.item.depositBank" name="depositBank" maxlength="20"/>
                                </div>
                                <label class="col-md-2 control-label">银行账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="银行账号"
                                           ng-model="vm.item.bankAccount" name="bankAccount" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">纳税主体资格</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'纳税主体资格'}:true"
                                            ng-model="vm.item.taxType" class="form-control" name="taxType"></select>
                                </div>
                                <label class="col-md-2 control-label">纳税人识别号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="纳税人识别号"
                                           ng-model="vm.item.taxNo" name="taxNo" maxlength="20"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">修改时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">录入部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);vm.opt='所属部门'"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="备注" ng-model="vm.item.remark"
                                           name="remark" maxlength="100"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " style="height: 460px;overflow-y: auto;" id="tab_contract">
                    <div class="table-scrollable">
                        <table class="table table-striped table-bordered table-advance table-hover">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th>部门名称</th>
                                <th>客户名称</th>
                                <th style="width: 130px;">合同金额（万元）</th>
                                <th style="width: 90px;">签订日期</th>
                                <th style="width: 50px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="project in vm.list">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="project.projectName"></td>
                                <td ng-bind="project.deptName"></td>
                                <td ng-bind="project.customerName"></td>
                                <td ng-bind="project.contractMoney|currency : '￥'"></td>
                                <td ng-bind="project.signDate"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.showContract(project.id);"
                                       title="详情"></i>
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

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 附件列表
        </div>
        <div class="actions">
              <span id="btnUpload" class="btn btn-sm default fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadEdFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>名称</th>
                    <th style="width: 90px;">大小</th>
                    <th style="width: 60px;">创建人</th>
                    <th style="width: 130px;">创建时间</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="file in files">
                    <td ng-bind="$index+1"></td>
                    <td>
                        <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png" onerror="this.src='/m/img/filetype/file.png'"/>
                        <span ng-bind="file.fileName" ng-show="browserVersion!='ie9'"
                              ng-click="download(file.sysAttach.id);" class="data_title"></span>
                        <a ng-href="{{'/sys/attach/download/'+file.sysAttach.id}}" class="a_title" ng-show="browserVersion=='ie9'" ng-bind="file.fileName" ></a>
                    </td>
                    <td ng-bind="file.sizeName"></td>
                    <td ng-bind="file.creatorName"></td>
                    <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa  fa-cloud margin-right-5" ng-click="showVersion(file.id);"
                           title="历史"></i>
                        <i class="fa fa-trash" ng-show="file.creator==user.userLogin" ng-click="removeEdFile(file);"
                           title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>



<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择录入部门</h4>
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




