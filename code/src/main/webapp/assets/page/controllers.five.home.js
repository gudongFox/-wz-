angular.module('controllers.five.home', [])


        .controller("FiveHomeController", function ($state, $stateParams,$rootScope,$scope,fiveHomeService,actService,hrDeptService,actTaskQueryService,fiveSuperviseService,actProcessQueryService,oaLinkService,oaNoticeService) {
        var vm = this;
        vm.params = { qName: "",pageNum: 1, pageSize:99,total:0,showSign:false};



        vm.convertTime=function(timeStamp,type){
            var temporyDate=new Date(timeStamp);
            if(type=="yyyy/MM/dd")
            {
                var year=temporyDate.getFullYear();
                var month=temporyDate.getMonth()+1;
                var day=temporyDate.getDate();
                var strMonth="";
                var strDay="";
                if(month<10){
                    strMonth="0"+month;
                }else {
                    strMonth=month;
                }
                if(day<10){
                    strDay="0"+day;
                }else {
                    strDay=day;
                }
                return  year+"/"+strMonth+"/"+strDay;
            }
            else if(type=="yyyyMMdd")
            {

                var year=temporyDate.getFullYear();
                var month=temporyDate.getMonth()+1;
                var day=temporyDate.getDate();
                var strMonth="";
                var strDay="";
                if(month<10){
                    strMonth="0"+month;
                }
                if(day<10){
                    strDay="0"+day;
                }
                return  year+"年"+strMonth+"月"+strDay+"日";
            }
            else  if(type=="yyMMdd")
            {
                var year=temporyDate.getFullYear();
                var month=temporyDate.getMonth()+1;
                var day=temporyDate.getDate();
                var strYear=year%100;
                var strMonth="";
                var strDay="";

                if(month<10){
                    strMonth="0"+month;
                }
                if(day<10){
                    strDay="0"+day;
                }
                return  strYear+"年"+strMonth+"月"+strDay+"日";
            }
            else  if(type=="MMdd")
            {
                var year=temporyDate.getFullYear();
                var month=temporyDate.getMonth()+1;
                var day=temporyDate.getDate();
                var strMonth="";
                var strDay="";
                if(month<10){
                    strMonth="0"+month;
                }
                if(day<10){
                    strDay="0"+day;
                }
                return strMonth+"月"+strDay+"日";
            }
            else if(type=="human")
            {
                var now=new Date().valueOf();
                var temporyValue=(parseInt(now)-parseInt(timeStamp))/1000;
                if(temporyValue<60){
                    return "刚刚";
                }
                else if(temporyValue<3600){
                    var m=Math.round(temporyValue/60);
                    return  m+"分钟前"
                }
                else if(temporyValue<(86400))
                {
                    var h=Math.round(temporyValue/3600);
                    return  h+"小时前"
                }
                else if(temporyValue<(2592000)){
                    var d=Math.round(temporyValue/86400);
                    return  d+"天前"
                }
                else{
                    var month=Math.round(temporyValue/2592000);
                    return  month+"月前";
                }
            }


        };

        vm.jugeOutTime=function(timeStamp){
            var yellow=60*60*24*2;//2天后变为黄色
            var red=60*60*24*15;//15天超期
            var now=new Date().valueOf();
            var temporyValue=(parseInt(now)-parseInt(timeStamp))/1000;


            if(temporyValue<yellow){
                return 1;
            }
            else if(temporyValue<red){
                return 2;
            }
            else
                return 3;
        }

        vm.icoClass=function(outTime){
            if(outTime==1)
                return "label label-sm label-success";
            else  if(outTime==2)
                return  "label label-sm label-warning";
            else
                return  "label label-sm label-danger";
        }


        /**
         * 加载待办任务
         */
        vm.loadTask=function () {
            actTaskQueryService.listPagedTask({enLogin:user.enLogin,pageNum:1,pageSize:8}).then(function (value){
                if (value.data.ret) {
                    var data=value.data.data;
                    vm.taskTotal=data.total;
                    vm.tasks =data.list;
                    vm.tasks.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.initiatorTime,"yyyy/MM/dd");
                        item.outTime=vm.jugeOutTime(item.initiatorTime);
                    })
                }
            })
        };

        /**
         * 加载参与任务
         */
        vm.loadDoneTask=function () {
            var pa={pageNum:vm.params.pageNum,pageSize:vm.params.pageSize,involvedUser:user.userLogin}
            actProcessQueryService.listPagedProcessInstance(pa).then(function (value) {
                if(value.data.ret){
                    var data=value.data.data;
                    vm.taskDoneList=data.list;
                    vm.taskDoneList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.initiatorTime,"yyyy/MM/dd");
                        item.outTime=vm.jugeOutTime(item.initiatorTime);
                    })
                }
            })

        };

        vm.loadCopyTask=function(){
            vm.copyTaskList="";
        }

        vm.loadSuperviseTask=function () {
            fiveSuperviseService.listPageTask(user.userLogin).then(function (value){
                if (value.data.ret) {
                    vm.superviseTasks = value.data.data;
                    vm.superviseTasks.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtCreate,"yyyy/MM/dd");
                    })
                }
            })
        }

        vm.changeClass=function(id){
            var attribute = document.getElementById(id).getAttribute("class");
            if (attribute=='checked'){
                document.getElementById(id).setAttribute("class","");
                vm.allType=vm.allType.replace(id,'');
            }else {
                document.getElementById(id).setAttribute("class","checked");
                vm.allType=vm.allType+','+id;
            }
            vm.loadNotice(vm.allType);
        }
        /**
         * 下载中心
         */
        vm.loadDownLoad=function () {
            fiveHomeService.listDownLoad(user.userLogin,1,99).then(function (value) {
                if (value.data.ret) {
                    vm.downLoads = value.data.data.list;
                    vm.downLoads.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyyy/MM/dd");
                    })
                }
            })
        };

        vm.loadNotice=function(allType){
            fiveHomeService.listFirmDateByType(allType,user.userLogin).then(function (item) {
                if (item.data.ret){
                    vm.noticeList=item.data.data;
                    vm.noticeList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyyy/MM/dd")
                    })
                }
            })

        }

        vm.getNoticeType=function(){
            fiveHomeService.getOtherType().then(function (value) {
                if (value.data.ret){
                    vm.noticeTypes=value.data.data;
                    vm.allType= vm.noticeTypes.toString();

                    vm.loadNotice(vm.allType);
                }
            })

        }

        /**
         * 集团制度
         */
        vm.loadRegulatory=function(){
            fiveHomeService.listFirmDateByType("集团制度",user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.regulatoryList = value.data.data;
                    vm.regulatoryList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                    })
                }
            })
        }
        /**
         * 公司制度
         */
        vm.loadNoticeCompany=function(){
            fiveHomeService.listFirmDateByType("公司制度",user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.companyList = value.data.data;
                    vm.companyList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                    })
                }
            })
        }
        /**
         * 公司办工作
         */
        vm.loadOffice=function(){
            fiveHomeService.listDateByDeptName("公司办公室(董事会办公室）",user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.partyOfficeList = value.data.data;
                    vm.partyOfficeList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd")
                    })
                }
            })
        }
        /**
         * 保密办公室
         */
        vm.loadPartyBuilding=function(){
            fiveHomeService.listDateByDeptName("保密办公室",user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.partyBuildingList = value.data.data;
                    vm.partyBuildingList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd")
                    })
                }
            })
        }
        /**
         * 党群工作部
         */
        vm.loadBusiness=function(){
            fiveHomeService.listDateByDeptName("党群工作部",user.userLogin).then(function (value) {
                if(value.data.ret){
                    vm.newsBusinessList=value.data.data;
                    vm.newsBusinessList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd")
                    })
                }
            })
        }
        /**
         * 经营发展部
         */
        vm.loadMessage=function(){
            fiveHomeService.listDateByDeptName("经营发展部",user.userLogin).then(function (value) {
                if(value.data.ret){
                    vm.newsMessageList=value.data.data;
                    vm.newsMessageList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd")
                    })
                }
            })
        }
        /**
         * 信息化建设与管理部
         */
        vm.loadSecrecy=function(){
            fiveHomeService.listDateByDeptName("信息化建设与管理部",user.userLogin).then(function (value) {
                if(value.data.ret){
                    vm.newsSecrecyList=value.data.data;
                    vm.newsSecrecyList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd")
                    })
                }
            })
        }
        /**
         * 财务金融部
         */
        vm.loadFinance=function(){
            fiveHomeService.listDateByDeptName("财务金融部",user.userLogin).then(function (value) {
                if(value.data.ret){
                    vm.newsFinanceList=value.data.data;
                    vm.newsFinanceList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                    })
                }
            })
        }
        /**
         * 人力资源
         */
        vm.loadPolitics=function(){
            fiveHomeService.listDateByDeptName("人力资源部",user.userLogin).then(function (value) {
                if(value.data.ret){
                    vm.newsPoliticsList=value.data.data;
                    vm.newsPoliticsList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                    })
                }
            })
        }
        /**
         * 工程管理部
         */
        vm.loadLogistics=function(){
            fiveHomeService.listDateByDeptName("工程管理部",user.userLogin).then(function (value) {
                if(value.data.ret){
                    vm.newsLogisticsList=value.data.data;
                    vm.newsLogisticsList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                    })
                }
            })
        }
        /**
         * 纪检工作部、审计与风险管理部
         */
        vm.loadTrain=function(){
            fiveHomeService.listDateByDeptName("纪检工作部、审计与风险管理部",user.userLogin).then(function (value) {
                if(value.data.ret){
                    vm.newsTrainList=value.data.data;
                    vm.newsTrainList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd")
                    })
                }
            })
        }

        /**
         * 行政事务部
         */
        vm.loadLaborUnion=function(){
            fiveHomeService.listDateByDeptName("行政事务部",user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.laborUnionList = value.data.data;
                    vm.laborUnionList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                    })
                }
            })
        }
        /**
         * 科研与技术质量部
         */
        vm.laborScientific=function(){
            fiveHomeService.listDateByDeptName("科研与技术质量部",user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.laborScientificList = value.data.data;
                    vm.laborScientificList.forEach(function (item) {
                        item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                    })
                }
            })
        }
        /**
         * 图片新闻
         * @constructor
         */
        vm.LoadNewsPhoto=function(){
           fiveHomeService.getNewsPhoto().then(function (value) {
                 if(value.data.ret){
                     vm.newsPhotoList=value.data.data;
                     $('#myCarousel').carousel();
                 }
           })
        }

        vm.loadDesignNotice=function(){
            hrDeptService.getDefaultDept(user.deptId).then(function (value) {
                if (value.data.ret) {
                    vm.showDept=value.data.data;
                    if ('钢结构技术研究中心,五源现代项目管理中心,建筑规划设计研究院,环境与能源设计研究院,第二设计研究院,第一设计研究院,轨道交通设计研究院,石油化工设计研究院,五洲中兴,五特公司,五环监理'.indexOf(vm.showDept.deptName)>0){
                      vm.params.showSign=true;
                        fiveHomeService.listDateByDeptName(vm.showDept.deptName,user.userLogin).then(function (value) {
                            if (value.data.ret) {
                                vm.designNoticeList = value.data.data;
                                vm.designNoticeList.forEach(function (item) {
                                    item.convertTime=vm.convertTime(item.gmtModified,"yyMMdd");
                                })
                            }
                        })
                     }
                }
            })



        }
        /*友情链接*/
        vm.loadLink=function(){
            vm.linkPageInfo = {pageNum: 1, pageSize: 6,total:999};
            var params = {pageNum: vm.linkPageInfo.pageNum, pageSize: vm.linkPageInfo.pageSize};
            oaLinkService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.linkPageInfo = value.data.data;

                }
            })
        }

        vm.openLink=function(item){
            window.open(item.linkUrl,"_blank");
        }

        vm.showMoreLink=function(){
            $state.go("oa.link");
        }

        vm.showDetail=function(businessKey){
            actService.getNgRedirectUrl(businessKey).then(function (value) {
                if(value.data.ret){
                    var result=value.data.data;
                     $state.go(result.url,result.params);
                }
            })
        }

        vm.showTaskDetail=function(){
            $state.go("five.dashboard");
        }
        //下载中心
        vm.showLoadDetail=function(){
            $state.go("sys.software");
        }
        //电子公告-更多
        vm.showNoticeList=function(type1){
            if (type1=='企业动态'){
                $state.go("oa.article",{type:type1});
            }else if (type1=='集团制度') {
                $state.go("oa.articleRegime",{regime:type1});
            }else if (type1=='公司制度') {
                $state.go("oa.articleRegime",{regime:type1});
            }else {
                $state.go("oa.articleDept",{deptName:type1});
            }

        }

        vm.showNoticeDetail = function (id,attachId,businessKey) {
            $state.go("oa.articleDetail", {id: id});
        }

        vm.init=function () {
            vm.loadTask();
            vm.loadDoneTask();
            vm.loadDownLoad();
            //vm.loadNotice();
            vm.loadPartyBuilding();
            vm.loadRegulatory();
            vm.loadLaborUnion();
            vm.getNoticeType();
            vm.LoadNewsPhoto();
            vm.loadTrain();
            vm.loadLogistics();
            vm.loadPolitics();
            vm.loadFinance();
            vm.loadSecrecy();
            vm.loadMessage();
            vm.loadBusiness();
            vm.laborScientific();
            vm.loadNoticeCompany();
            vm.loadSuperviseTask();
            vm.loadLink();
            vm.loadDesignNotice();
            vm.loadOffice();
        };


        vm.init();
        return vm;
    })
     /*
     功能入口页面更改 蒋
     */
    .controller("FunctionEntranceController", function ($stateParams,$rootScope,$scope,fiveHomeService,actService) {
        var vm = this;
        vm.moduleId=$stateParams.moduleId;
        //最外层展示
        vm.entranceMenus=[];
        vm.recursionGetMenuById=function(menu){
            for(var i=0;i<menus.length;i++) {
                var menu=menus[i];
                if(menu.id==vm.moduleId){
                    vm.entranceMenus.push(menu);
                }
            }
            //第二次遍历 是否存在第二层目录
                for(var j=0;j<vm.entranceMenus.length;j++){
                    //第二层[]
                    var menuList=[];
                    for(var i=0;i<menus.length;i++) {
                        var menu=menus[i];
                        if(menu.parentId==vm.moduleId){
                            menuList.push(menu);
                        }
                    }
                    vm.entranceMenus[j].menuList = menuList;
                 }
        };

        vm.init=function(){
            vm.recursionGetMenuById();
        };

        vm.init();
        return vm;
    })



