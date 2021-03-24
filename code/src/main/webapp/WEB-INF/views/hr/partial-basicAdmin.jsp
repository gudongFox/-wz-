<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--人事管理可以修改员工登录名和登录账号   组织机构管理->个人基础信息 --%>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>个人基础信息</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class=" icon-user"></i>个人基础信息
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">

            <div class="tab-content">

                <form class="form-horizontal form" role="form" id="detail_form">
                    <div class="form-body">
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
                                        ng-model="vm.item.man" class="form-control"></select>
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
                                <div class="input-group date date-picker" >
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

                                <div class="input-group date date-picker" >
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.joinWorkTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>

                            </div>
                            <label class="col-md-1 control-label">入职时间</label>
                            <div class="col-md-3">

                                <div class="input-group date date-picker" >
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.joinCompanyTime">
                                    <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>

                            </div>
                            <label class="col-md-1 control-label">离职时间</label>
                            <div class="col-md-3">

                                <div class="input-group date date-picker" >
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.leaveCompanyTime">
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

                                <input type="text" class="form-control" ng-model="vm.item.email"
                                       name="email" maxlength="100"/>

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
                            <label class="col-md-1 control-label">个人头像</label>
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.headUrl"
                                           name="headUrl" maxlength="100" disabled="disabled"/>
                                    <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadHead" class="btn blue fileinput-button btn-sm">
                            <i class="fa fa-cloud-upload" style="height:15px "></i>
                            <span>上传</span>
                            <input type="file" name="multipartFile" id="uploadHeadFile"
                                   accept="*" multiple="multiple"/>
                                   </span>
                                        <a ng-href="{{'/sys/attach/download/'+vm.item.headAttachId}}" target="_blank">
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
                                <span class="help-block" style="font-size :14px;color: red;letter-spacing: 2px">请使用签名模板 在规定位置合理签名,保存为2007格式</span>
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
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>



