<%--
  Created by IntelliJ IDEA.
  User: luodong
  Date: 2019/11/19
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="/m/admin/pages/css/profile.css" rel="stylesheet"/>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>其他功能</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>个人信息</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN PROFILE SIDEBAR -->
        <div class="profile-sidebar">
            <!-- PORTLET MAIN -->
            <div class="portlet light profile-sidebar-portlet">
                <!-- SIDEBAR USERPIC -->
                <div class="profile-userpic">
                    <span class="btn  fileinput-button ">
                        <img class="img-responsive" alt=""
                             ng-src="{{'/common/attach/download/'+vm.item.headAttachId}}"
                             onerror="this.src='/m/admin/pages/media/profile/profile_user.jpg'"/>
                        <input id="headUpload" type="file" accept="*" name="file" multiple="multiple"/>
                    </span>
                </div>
                <!-- END SIDEBAR USERPIC -->
                <!-- SIDEBAR USER TITLE -->
                <div class="profile-usertitle" style="padding-bottom: 20px;">
                    <div class="profile-usertitle-name" ng-bind="vm.item.userName">
                    </div>
                    <div class="profile-usertitle-job" ng-bind="vm.item.userLogin" style="color: grey;">
                    </div>
                </div>
                <!-- END SIDEBAR USER TITLE -->
                <!-- SIDEBAR BUTTONS -->
                <!-- END SIDEBAR BUTTONS -->
                <!-- SIDEBAR MENU -->
                <!-- END MENU -->
            </div>
            <!-- END PORTLET MAIN -->
            <!-- PORTLET MAIN -->
            <div class="portlet light">
                <!-- STAT -->
                <div class="row list-separated profile-stat">
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <div class="uppercase profile-stat-title">
                            <a ui-sref="me.edProject" ng-bind="vm.pageInfo.total">37</a>
                        </div>
                        <div class="uppercase profile-stat-text">
                            我的项目
                        </div>
                    </div>
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <div class="uppercase profile-stat-title">
                            <a ui-sref="dashboard" ng-bind="vm.tasks.length">37</a>
                        </div>
                        <div class="uppercase profile-stat-text">
                            待办任务
                        </div>
                    </div>
                </div>
                <!-- END STAT -->
                <div style="padding-bottom: 50px;">
                    <%--                    <h4 class="profile-desc-title"></h4>--%>
                    <span class="profile-desc-text">协同设计&设计管理</span>
                    <div class="margin-top-20 profile-desc-link">
                        <i class="fa fa-dashboard"></i>
                        <a ui-sref="dashboard">系统首页</a>
                    </div>
                    <div class="margin-top-20 profile-desc-link">
                        <i class="fa fa-pencil-square-o"></i>
                        <a ui-sref="me.cpStep">协同项目</a>
                    </div>
                    <div class="margin-top-20 profile-desc-link">
                        <i class="fa fa-cloud"></i>
                        <a ui-sref="sys.software">下载中心</a>
                    </div>
                    <div class="margin-top-20 profile-desc-link">
                        <i class="fa  fa-key"></i>
                        <a href="/out">安全退出</a>
                    </div>
                </div>
            </div>
            <!-- END PORTLET MAIN -->
        </div>
        <!-- END BEGIN PROFILE SIDEBAR -->
        <!-- BEGIN PROFILE CONTENT -->
        <div class="profile-content">
            <div class="row">
                <div class="col-md-12">
                    <div class="portlet light">
                        <div class="portlet-title tabbable-line">
                            <div class="caption caption-md">
                                <i class="icon-globe theme-font hide"></i>
                                <span class="caption-subject font-blue-madison bold uppercase">用户档案</span>
                            </div>
                            <ul class="nav nav-tabs">
                                <li class="active">
                                    <a href="#tab_1_1" data-toggle="tab">基础信息</a>
                                </li>

                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div class="tab-content">
                               <form role="form" class="form-horizontal form" action="#">
                                     <div class="form-group">
                                         <label class="col-md-1 control-label">姓名</label>
                                         <div class="col-md-3">
                                             <input type="text" class="form-control" ng-model="vm.item.userName"
                                                    name="userName" required="true" maxlength="20" disabled/>
                                         </div>

                                         <label class="col-md-1 control-label">登录名</label>
                                         <div class="col-md-3">
                                             <input type="text" class="form-control" ng-model="vm.item.userLogin"
                                                    name="userLogin" required="true" maxlength="20" disabled/>
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
                                             <input type="text" class="form-control" ng-model="vm.item.age" name="age"
                                                    maxlength="20"/>
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
                                         <label class="col-md-1 control-label">参加工作</label>
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
                                                        ng-model="vm.item.joinCompanyTime" >
                                                 <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                             </div>

                                         </div>
                                         <label class="col-md-1 control-label">离职时间</label>
                                         <div class="col-md-3">
                                             <div class="input-group date date-picker" id="leaveCompanyTime">
                                                 <input type="text" class="form-control"
                                                        ng-model="vm.item.leaveCompanyTime " disabled>
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

                                         <label class="col-md-1 control-label">职称</a>
                                         </label>
                                         <div class="col-md-3">
                                             <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'职称'}:true"
                                                     ng-model="vm.item.rank" class="form-control"></select>
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
                                         <label class="col-md-1 control-label">职称认定时间</label>
                                         <div class="col-md-3">
                                             <div class="input-group date date-picker" id="rankTime">
                                                 <input type="text" class="form-control"
                                                        ng-model="vm.item.rankTime">
                                                 <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                                </span>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="form-group">
                                         <label class="col-md-1 control-label">个人签名</label>
                                         <div class="col-md-5">
                                             <div class="input-group">
                                                 <input type="text" class="form-control" ng-model="vm.item.signUrl"
                                                        name="signUrl" maxlength="100" disabled="disabled"/>
                                                 <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadSign" class="btn blue fileinput-button btn-sm">
                            <i class="fa fa-cloud-upload" style="height:15px "></i>
                            <span>上传签名</span>
                            <input type="file" name="multipartFile" id="uploadSignFile"
                                   accept="*" multiple="multiple"/>
                                   </span>
                                         <a ng-href="{{'/sys/attach/download/'+vm.item.signAttachId}}" target="_blank">
                                                <span id="btnDownloadSign" class="btn green fileoutput-button btn-sm">
                                                     <i class="fa fa-cloud-download" title="下载"></i>
                                                <span>下载</span>
                                                </span>
                                          </a>
                                        </span>
                                         </div>
                                             <span class="help-block" style="font-size :14px;color: red;">请使用
                                                 <a href="/assets/doc/std/sign.dwg"><strong style="color: blue">签名模板</strong></a>在规定位置合理签名,保存为2007格式。DWG出图电子签名使用</span>
                                         </div>

                                         <label class="col-md-1 control-label">签名图片</label>
                                         <div class="col-md-5">
                                             <div class="input-group">
                                                 <input type="text" class="form-control" ng-model="vm.item.signPicUrl"
                                                        name="signUrl" maxlength="100" disabled="disabled"/>
                                                 <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadSignPic" class="btn blue fileinput-button btn-sm">
                            <i class="fa fa-cloud-upload" style="height:15px "></i>
                            <span>上传签名图片</span>
                            <input type="file" name="multipartFile" id="uploadSignPicFile"
                                   accept="*" multiple="multiple"/>
                                   </span>
                                        <a ng-href="{{'/sys/attach/download/'+vm.item.signPicAttachId}}"
                                           target="_blank">
                                        <span id="btnDownloadSignPic" class="btn green fileoutput-button btn-sm">
                                     <i class="fa fa-cloud-download"
                                        title="下载"></i>
                                            <span>下载</span>
                                        </span>
                                            </a>
                                        </span>
                                             </div>
                                             <span class="help-block" style="font-size :14px;color: red;">请上传png图片签名。WEB电子签名打印使用</span>
                                         </div>

                                     </div>



                                 <div class="margiv-top-10">
                                     <a href="javascript:;" ng-click="vm.save()" class="btn green-haze">
                                         保存 </a>
                                     <a href="javascript:;" class="btn default">
                                         取消 </a>
                                 </div>
                                </form>


                                <!-- PERSONAL INFO TAB -->
                                <!-- END PERSONAL INFO TAB -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END PROFILE CONTENT -->
    </div>
</div>
