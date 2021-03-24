angular.module('controllers.oa.notice', [])

    //信息管理 列表
    .controller("OaNoticeController", function ($state,$stateParams,$scope, oaNoticeService,sysRoleService) {
        var vm = this;
        var paramType= $stateParams.type;
        console.log("1");
        vm.init=function(){
            vm.keyParamMap=new Map();
            vm.keyParamMap.set("noticeMessage",{type:"通知公告",modelName:"通知公告",uiSref:"oa.noticeMessage"});
            vm.keyParamMap.set("noticeParty",{type:"公司新闻",modelName:"公司新闻",uiSref:"oa.noticeParty"});
            vm.keyParamMap.set("noticeFile",{type:"文件简报",modelName:"文件简报",uiSref:"oa.noticeFile"});
            vm.keyParamMap.set("noticeGroup",{type:"集团制度",modelName:"集团制度",uiSref:"oa.noticeGroup"});
            vm.keyParamMap.set("noticeCompany",{type:"公司制度",modelName:"公司制度",uiSref:"oa.noticeCompany"});
            vm.keyParamMap.set("noticePhoto",{type:"图片新闻",modelName:"图片新闻",uiSref:"oa.noticePhoto"});
            vm.keyParamMap.set("noticeQuality",{dept:"信息化建设与管理部",modelName:"信息化建设",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticePartyAffairs",{dept:"党群工作部",modelName:"党建园地",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeSecrecyOffice",{dept:"保密办公室",modelName:"保密工作",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeBusiness",{dept:"经营发展部",modelName:"经营咨询",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeFinance",{dept:"财务金融部",modelName:"财金专栏",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeHuman",{dept:"人力资源部",modelName:"人资信息",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeProject",{dept:"工程管理部",modelName:"工程管理",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeAdministration",{dept:"行政事务部",modelName:"后勤行管",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeScientific",{dept:"科研与技术质量部",modelName:"科研管理",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeDiscipline",{dept:"纪检工作部、审计与风险管理部",modelName:"纪检、审计与风险管理",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeOffice",{dept:"公司办公室(董事会办公室）",modelName:"公司办工作",uiSref:"oa.noticeTrain"});
            vm.keyParamMap.set("noticeFirstCourtyard",{dept:"第一设计研究院",modelName:"第一设计研究院",uiSref:"oa.noticeFirstCourtyard"});
            vm.keyParamMap.set("noticeSecondCourtyard",{dept:"第二设计研究院",modelName:"第二设计研究院",uiSref:"oa.noticeSecondCourtyard"});
            vm.keyParamMap.set("noticeEnergy",{dept:"环境与能源设计研究院",modelName:"环境与能源设计研究院",uiSref:"oa.noticeEnergy"});
            vm.keyParamMap.set("noticeArchitecture",{dept:"建筑规划设计研究院",modelName:"建筑规划设计研究院",uiSref:"oa.noticeArchitecture"});
            vm.keyParamMap.set("noticeSteelStructure",{dept:"钢结构技术研究中心",modelName:"钢结构技术研究中心",uiSref:"oa.noticeSteelStructure"});
            vm.keyParamMap.set("noticeModernProject",{dept:"五源现代项目管理中心",modelName:"五源现代项目管理中心",uiSref:"oa.noticeModernProject"});
            vm.keyParamMap.set("noticePathwayTraffic",{dept:"轨道交通设计研究院",modelName:"轨道交通设计研究院",uiSref:"oa.noticePathwayTraffic"});
            vm.keyParamMap.set("noticePetroleum",{dept:"石油化工设计研究院",modelName:"石油化工设计研究院",uiSref:"oa.noticePetroleum"});
            vm.keyParamMap.set("noticeFiveCentre",{dept:"五洲中兴",modelName:"五洲中兴",uiSref:"oa.noticeFiveCentre"});
            vm.keyParamMap.set("noticeFiveParticularly",{dept:"五特公司",modelName:"五特公司",uiSref:"oa.noticeFiveParticularly"});
            vm.keyParamMap.set("noticeFiveLoop",{dept:"五环监理",modelName:"五环监理",uiSref:"oa.noticeFiveLoop"});

            vm.params=vm.keyParamMap.get(paramType);
            vm.params.userLogin=user.userLogin;
            vm.addRight=false;
            vm.deleteRight=false;
            vm.editRight=false;
            vm.multiple=false;
            vm.pageInfo = {pageNum:1, pageSize:20};
            $scope.loadRightData();
            vm.loadPagedData();
        };
        vm.initRight=function(){
            sysRoleService.getAclInfoByUserLogin(user.userLogin,vm.params.uiSref).then(function (value) {
                let rightString=value.data.data;
                if(rightString.indexOf("新增")>-1){
                    vm.addRight=false;
                }
                else if(rightString.indexOf("删除")>-1){
                    vm.deleteRight=false;
                }
                else if(rightString.indexOf("修改")>-1){
                    vm.editRight=false;
                }
            })
        }
        vm.loadPagedData = function () {
            vm.params.pageNum= vm.pageInfo.pageNum;
            vm.params.pageSize=vm.pageInfo.pageSize;
            oaNoticeService.listPagedDataByType(vm.params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    vm.overWriteData( vm.pageInfo.list);
                }
            })
        };



        vm.show = function (id) {
            $state.go("oa.articleDetail",{id: id});
        }
        vm.edit=function(id){
            $state.go("oa.noticeDetail",{id: id});
        };
        vm.add = function () {
            oaNoticeService.getNewModelByType(user.userLogin,vm.params.type).then(function (value) {
                if (value.data.ret) {
                    vm.edit(value.data.data);
                }
            })
        }
        vm.remove=function(id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if(result){
                    oaNoticeService.remove(id,user.userLogin).then(function (value) {
                        if (value.data.ret){
                            toastr.success("删除成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            })
        }
        vm.overWriteData=function(list){
            var temporyMap=new Map();
            for(let i=0;i<list.length;i++){
                let item=list[i];
                let key=item.noticeLevel;
                if(temporyMap.has(key)){
                    let array=temporyMap.get(key);
                    array.push(item);
                }
                else{
                    temporyMap.set(key,[item])
                }
            }
            if(temporyMap.size>1){
                vm.multiple=true;
            };
            vm.pageInfo.categoryRows=[];
            temporyMap.forEach(function (value, key, map) {
                let  row={rowType:"category",content:key};
                vm.pageInfo.categoryRows.push(row);
                value.forEach(function (item) {
                    let  row={rowType:"data",content:item};
                    vm.pageInfo.categoryRows.push(row);
                })
            });
        }

        vm.fuzzySearch = function (params) {
            oaNoticeService.fuzzySearch(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    vm.overWriteData( vm.pageInfo.list);
                }
            })
        };
        $scope.$on('ngRepeatFinished', function( ngRepeatFinishedEvent ) {
            var option={filterColumns:{
                    1:{type:"input",colName:'noticeTitle',placeholder:'请输信息标题..'},
                    2:{type:"input",colName:'deptName',placeholder:'请输发布部门..'},
                    3:{type:"input",colName:'noticeLevel',placeholder:'公告类别'},
                    4:{type:"input",colName:'publishUserName'},
                    5:{type:"input",colName:'gmtCreate'},
                    6:{type:"select",colName:'is_publish',placeholder:'流程状态..',option:[{title:"全部",value:""},{title:"审核中",value:"0"},{title:"已发布",value:"1"}]}
                    //注：当type为select时 会根据option创建下拉列表 option中
                },handleColumn:7};
            tablefilter.queryFunction=vm.fuzzySearch;
            tablefilter.params=vm.params;
            tablefilter.initializeFilter(option);
        });

        vm.init();
        return vm;

    })

    //信息管理 修改详情
    .controller("OaNoticeDetailController", function ($state,$sce,$stateParams,$rootScope ,$scope, oaNoticeService) {
        var vm = this;
        var id = $stateParams.id;

        vm.showType=false;
        vm.showHtml=true;//html 或者owa显示状态
        vm.showWX=false;//企业微信通知按钮
        var showPic=true;//图片新闻上传验证

        vm.init=function(){
            vm.loadData(true);
            vm.loadFile();

            //水印 HNZ
            setTimeout(function(){
                __canvasWM({
                    content: user.userName + ' ' + user.userLogin
                });},0);

        }

        /*加载上传控件*/
        vm.loadFile=function(){
            $("#btnUploadWord").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator:user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star=  file.name.lastIndexOf(".");
                    var  fileType=file.name.substring(star,file.name.length);
                    if(".doc,.docx,.pdf,.xls,.xlsx,.ppt,.pptx".indexOf(fileType)==-1){
                        toastr.error('请上传.doc,.docx,.pdf,.xls,.xlsx,.ppt,.pptx格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.attachIds = result.data.id;
                        vm.item.attachName=data.files[0].name;
                        vm.save();
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });

            $("#btnUploadPhoto").fileupload({
                maxFileSize: 99 * 1024 * 1024,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                formData: {
                    source: "sys",
                    creator:user.userLogin,
                },
                add: function (e, data) {
                    var file = data.files[0];
                    var star=  file.name.lastIndexOf(".");
                    var  fileType=file.name.substring(star,file.name.length);
                    if(".png,.jpg,.bmp,.tif,.jpge".indexOf(fileType)==-1){
                        toastr.error('请上传.png,.jpg,.bmp,.tif,.jpge 格式文件!');
                        return false;
                    }
                    if (file.size > 100 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制100MB!');
                        return false;
                    }
                    data.submit();
                },
                done: function (e, data) {
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        toastr.success("上传成功!");
                        vm.item.photoAttachId = result.data.id;
                        vm.item.photoAttachName=data.files[0].name;
                        vm.save();
                        $scope.$apply();
                    } else {
                        toastr.error("上传失败!");
                    }
                }
            });
        }


        vm.loadData = function (loadProcess) {
            oaNoticeService.getModelById(id,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.basicInit(vm.item.businessKey);
                        $rootScope.getTplConfig("",vm.item.businessKey,user.enLogin);
                    }
                    vm.hrefOWA="https://owa.wuzhou.com.cn/op/embed.aspx?src="+encodeURIComponent("https://co.wuzhou.com.cn/common/attach/download/"+vm.item.attachIds);
                    $sce.trustAsResourceUrl(vm.hrefOWA);
                    vm.showStatus();
                }
            })

        };
        //2020-12-28HNZ 详情显示状态设置
        vm.showStatus=function(){
            if ("图片新闻".indexOf(vm.item.noticeType)>-1) {
                showPic=false;
                vm.loadFile();
            }else {
                showPic=true;
                vm.loadFile();
            }

            if (vm.item.noticeType=='图片新闻'||vm.item.attachIds==''){
                vm.showHtml=false;
            }else {
                vm.showHtml=true;
            }
            //通知公告和文件简报且登录人作为发起人才能发送企业微信通知
            if ("通知公告,文件简报".indexOf(vm.item.noticeType)>=0&&user.enLogin==vm.item.creator){
                vm.showWX=true;
            }
        }

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            oaNoticeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        vm.publishWX=function(){
            $("#publishId").hide();
            $("#detailModal").modal("hide");
            Metronic.blockUI({
                boxed: true
            });
            if(vm.type=='publish'){
                oaNoticeService.sendToWx(id).then(function(value){
                    if (value.data.ret){
                        Metronic.unblockUI();
                        toastr.success("推送企业微信成功!  系统整在处理，请耐心等待");
                    }
                })
            }else{
                oaNoticeService.sendToWxTest(id).then(function(value){
                    if (value.data.ret){
                        Metronic.unblockUI();
                        toastr.success("预览发送成功!  系统整在处理，请耐心等待");
                    }
                })
            }
        }

        vm.showPublishWX=function(type){
            vm.type = type;
            oaNoticeService.getAcceptUser(id,type).then(function(value){
                if (value.data.ret){
                    vm.acceptUser = value.data.data;
                    $("#detailModal").modal("show");
                }
            })

        }

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='viewMans'){
                $scope.showOaSelectEmployeeModal_({ title: "请选择发布范围",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.viewMans,
                    multiple: true});
            }
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='viewMans'){
                vm.item.viewMans = ','+$scope.selectedOaDeptIds_+',';
                vm.item.viewMansName = $scope.selectedOaDeptNames_;
            }
        };


        vm.init();
        return vm;
    })


    //主页跳转 企业动态 详情页面
    .controller("OaArticleController", function ($state,$rootScope, $scope,$stateParams, oaNoticeService) {
        var vm = this;
        vm.pageInfo = {pageNum:  1, pageSize: $scope.pageSize,total:0,searchKey:''};
        vm.params={noticeTitle:"",publishDeptName:"",publishUserName:"",type:""}
        vm.init=function(){
            vm.loadPagedData();

        }

        vm.loadPagedData = function () {
            var params={userLogin:user.userLogin,noticeTitles:vm.params.noticeTitle,publishDeptNames:vm.params.publishDeptName,
                publishUserNames:vm.params.publishUserName,
                pageNum:vm.pageInfo.pageNum,pageSize:vm.pageInfo.pageSize,noticeType:vm.params.type,noticeTypeList:"公司新闻,通知公告,文件简报"};

            oaNoticeService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("oa.articleDetail",{id: id});
        }

        vm.showNoticeDetail = function (id,attachId,businessKey) {
            $state.go("oa.articleDetail",{id: id});
        }

        vm.init();

        return vm;

    })

    //首页详情暂时 owa html
    .controller("OaArticleDetailController", function ($state,$stateParams,$rootScope,$sce ,$scope, oaNoticeService,commonFileService) {
        var vm = this;
        var id = $stateParams.id;
        vm.showHtml=true;

        vm.init=function(){
            vm.loadData(true);
            setTimeout(function(){
                __canvasWM({
                    content: user.userName + ' ' + user.userLogin
                });},0);

        }

        vm.loadData = function (loadProcess) {
            oaNoticeService.getModelById(id,user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.basicInit(vm.item.businessKey);
                    }
                    vm.hrefOWA="https://owa.wuzhou.com.cn/op/embed.aspx?src="+encodeURIComponent("https://co.wuzhou.com.cn/common/attach/download/"+vm.item.attachIds);
                    $sce.trustAsResourceUrl(vm.hrefOWA);
                    commonFileService.listData(vm.item.businessKey, 0).then(function (value) {
                        vm.files = value.data.data;
                    })
                    //2020-12-17 HNZ 判断html 还是owa显示方式
                    if (vm.item.attachIds==''){
                        vm.showHtml=false;
                    }

                }
            })
        };

        vm.downloadFile=function(id){
            $scope.commonDownload("/common/attach/download/file/"+id);
        }

        vm.htmlConvertText= function(content) {
            return $sce.trustAsHtml(content);
        };

        vm.showUserModel = function (status) {
            vm.status=status;
            if (vm.status=='viewMans'){
                $scope.showOaSelectEmployeeModal_({ title: "请选择发布范围",
                    type: '选部门',
                    deptIds: "1",
                    deptIdList: vm.item.viewMans,
                    multiple: true});
            }
        }

        $rootScope.saveSelectDept_ = function () {
            $scope.closeOaSelectEmployeeModal_();
            if ( vm.status=='viewMans'){
                vm.item.viewMans = ','+$scope.selectedOaDeptIds_+',';
                vm.item.viewMansName = $scope.selectedOaDeptNames_;
            }
        };

        setTimeout(function(){
            Layout.initContent();
        }, 2000);

        vm.init();

        return vm;
    })

    //主页跳转 部门公告 详情页面
    .controller("OaArticleDeptController", function ($state, $scope,$stateParams, oaNoticeService) {
        var vm = this;
        var deptName=$stateParams.deptName;

        vm.params = {noticeTitles:"",noticeTypes:"",pageNum: 1, pageSize: $scope.pageSize,total:0,userLogin:user.userLogin,deptName:deptName,seqType:"已发布"};
        vm.pageInfo = {publish:"",pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.init=function(){
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:vm.params.userLogin,deptName:vm.params.deptName,
                noticeTitles:vm.params.noticeTitles,noticeTypes:vm.params.noticeTypes,publish:vm.params.publish};

            oaNoticeService.listPagedDataByUserLogin(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })

        };

        vm.reloadPageData=function(){

            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,userLogin:vm.params.userLogin,deptName:vm.params.deptName,
                noticeTitles:vm.params.noticeTitles,noticeTypes:vm.params.noticeTypes,publish:vm.params.publish};
            oaNoticeService.listPagedDataByUserLogin(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        }

        vm.show = function (id) {
            $state.go("oa.articleDetail",{id: id});
        }

        vm.init();

        return vm;

    })

    //主页跳转 公司制度 集团制度 详情页面
    .controller("OaArticleRegimeController", function ($state, $scope,$stateParams, oaNoticeService) {
        var vm = this;
        var regime=$stateParams.regime;

        vm.params = {noticeTitles:"",pageNum: 1, pageSize: $scope.pageSize,total:0,type:regime,noticeLevel:""};
        vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
        vm.init=function(){
            vm.loadPagedData();
        }

        vm.loadPagedData = function () {
            var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,type:vm.params.type,
                noticeTitles:vm.params.noticeTitles,publish:1,noticeLevel:vm.params.noticeLevel,userLogin:user.userLogin};
            oaNoticeService.listPagedDataByType(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (id) {
            $state.go("oa.articleDetail",{id: id});
        }

        vm.init();

        return vm;

    })

