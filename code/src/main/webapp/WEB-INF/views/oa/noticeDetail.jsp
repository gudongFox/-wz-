<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">信息管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ui-sref="oa.noticeApply">信息发布</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.noticeTitle"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i>  <span ng-bind="vm.item.noticeLevel"></span>
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.save();"ng-show="user.userLogin==vm.item.creator" >
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" id="previewId"
               ng-click="vm.showPublishWX('preview');" ng-show="vm.showWX" >
                <i class="fa fa-save"></i> 预览 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" id="publishId"
               ng-click="vm.showPublishWX('publish');" ng-show="vm.showWX" >
                <i class="fa fa-save"></i> 企业微信 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 380px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">公告标题</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="公告标题" required="true"
                                           ng-model="vm.item.noticeTitle"   name="noticeTitle"/>
                                </div>
                                <label class="col-md-2 control-label required">发布板块</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true"
                                           ng-model="vm.item.noticeType"   name="noticeType" disabled/>
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required" >公告分类</label>
                                <div class="col-md-4" ng-show="vm.showType">
                                    <select ng-options="s.name as s.name for s in  vm.listCode"
                                            ng-model="vm.item.noticeLevel" name="noticeLevel" class="form-control" required="true" ></select>
                                </div>
                                <div class="col-md-4" ng-show="!vm.showType">
                                    <input type="text" class="form-control" placeholder="" required="true" disabled
                                           ng-model="vm.item.noticeLevel" />
                                </div>

                                <label class="col-md-2 control-label required" ng-if="vm.item.noticeType=='公司制度'">制度类别</label>
                                <div class="col-md-4" ng-if="vm.item.noticeType=='公司制度'">
                                    <select ng-options="s.name as s.name for s  in sysCodes | filter:{codeCatalog:('制度类别')}:true"
                                            ng-model="vm.item.noticeSystemType" class="form-control" required="true"ng-disabled="!processInstance.firstTask&&!vm.showType" ></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">发布范围</label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.viewMansName" name="viewMansName" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('viewMans');" ><i class="fa fa-user"  ></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否置顶</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('是否置顶')}:true"
                                            ng-model="vm.item.top" class="form-control" required="true"></select>
                                </div>

                                <label class="col-md-2 control-label required">是否发布</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('是否发布')}:true"
                                            ng-model="vm.item.publish" class="form-control" required="true" ></select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">发布部门</label>
                                <div class="col-md-4">
                                    <input type="text" required="true" class="form-control" ng-model="vm.item.publishDeptName"  name="publishDeptName" disabled  />
                                </div>
                                <label class="col-md-2 control-label required">发布人</label>
                                <div class="col-md-4">
                                    <input type="text" required="true" class="form-control" ng-model="vm.item.publishUserName" name="publishUserName" disabled  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" >公告文档</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.attachName"
                                               name="attachName" maxlength="100" disabled="disabled"/>
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadWord" class="btn blue fileinput-button btn-sm">
                                      <i class="fa fa-cloud-upload" style="height:15px "></i>
                                     <span>上传</span>
                                     <input type="file" name="multipartFile" id="uploadWordFile"
                                            accept="*" multiple="multiple"/>
                                    </span>
                                            <a ng-href="{{'/sys/attach/download/'+vm.item.attachIds}}"
                                               target="_blank">
                                        <span id="btnDownloadWord" class="btn green fileoutput-button btn-sm">
                                     <i class="fa fa-cloud-download"
                                        title="下载"></i>
                                            <span>下载</span>
                                        </span>
                                            </a>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" ng-show="vm.item.noticeType=='图片新闻'">新闻图片</label>
                                <div class="col-md-4" ng-show="vm.item.noticeType=='图片新闻'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.photoAttachName"
                                               name="photoAttachName" maxlength="100" disabled="disabled"/>
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadPhoto" class="btn blue fileinput-button btn-sm">
                                      <i class="fa fa-cloud-upload" style="height:15px "></i>
                                     <span>上传</span>
                                     <input type="file" name="multipartFile" id="uploadPicFile"
                                            accept=".png,.jpg,.bmp,.tif,.jpge" multiple="multiple"/>
                                    </span>
                                            <a ng-href="{{'/sys/attach/download/'+vm.item.photoAttachId}}"
                                               target="_blank">
                                        <span id="btnDownloadPic" class="btn green fileoutput-button btn-sm">
                                     <i class="fa fa-cloud-download"
                                        title="下载"></i>
                                            <span>下载</span>
                                        </span>
                                            </a>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group"  ng-if="vm.showHtml">
                                <label class="col-md-2 control-label required ">公告内容</label>
                                <div class="col-md-10" id="wmwrapper" style="width: 100%;height: 85%;margin: 10px auto;text-align: center;">
                                    <iframe  style="max-width:800px;width: 100%;min-height: 800px"  ng-src="{{vm.hrefOWA}}" src="" frameborder="0" referrerpolicy="no-referrer-when-downgrade">
                                    </iframe>
                                </div>
                            </div>
                            <div class="form-group"  ng-if="!vm.showHtml">
                                <label class="col-md-2 control-label required ">公告内容</label>
                                <div class="col-md-10" >
                                    <textarea ckeditor ng-model="vm.item.noticeContent" cols="80" ></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
                                </div>

                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">发送范围确认 :<span style="color: red;font-size: 12px">（公告在企业微信发送后无法取回，请谨慎操作！）</span></h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">发送范围</label>
                            <div class="col-md-10">
                                <input type="text" class="form-control" ng-model="vm.item.viewMansName" name="viewMansName" required="true"  readonly/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">接受人员</label>
                        <div class="col-md-10">
                            <textarea type="text" rows="5" class="form-control" ng-model="vm.acceptUser" name="acceptUser" disabled />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn blue" ng-click="vm.publishWX();">发送</button>
                    </div>
                </form>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>


    </div>
</div>