angular.module('controllers.hr2', [])
    .controller("HrIncomeCertificateController", function ($state, $scope, hrIncomeService) {
        var vm = this;
        toastr.success("welcome income certificate");
        vm.params = {q: "", qq: ""};
        vm.pageInfo = {pageNum: 1, pageSize: 10};

        vm.init = function () {
            $("#incomeModal").draggable({
                handle: ".modal-header"
            });
        }

        vm.queryData = function () {
            vm.pageInfo.pageNum = 1;
            vm.params.q = vm.params.qq;
            vm.loadPagedData();
        };

        vm.loadPagedData = function () {
            var params = {q: vm.params.q, pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrIncomeService.listPagedData(params).then(function (value) {
                vm.pageInfo = value.data.data;
            });
        };

        vm.queryData();


        vm.showDetail = function (id) {
            hrIncomeService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.income = value.data.data;
                    $("#incomeModal").modal("show");
                }
            })
        }

        vm.print = function (id) {
            hrIncomeService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.income = value.data.data;
                    lodop = getLodop();
                    lodop.PRINT_INIT("个人收入证明打印任务");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("income_printArea").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(150, 80, "80%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }


        vm.init();

        return vm;
    })

    .controller("HrEmployeeDetailController", function ($state, $scope, $stateParams, commonCodeService, hrEmployeeService, hrEducationService, hrContractService) {
        var vm = this;
        var userLogin = $stateParams.userLogin;

        vm.init = function () {
            $("#btnUploadHead").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'json',
                add: function (e, data) {
                    var file = data.files[0];

                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source: "web", jwt: user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {

                        vm.item.headUrl = result.data.id;
                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });

            $("#btnUploadSign").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'json',
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.name.indexOf(".dwg") <= 0) {
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source: "web", jwt: user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    var result = JSON.parse(data.result);
                    if (result.ret) {

                        vm.item.signUrl = result.data.id;
                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });

            commonCodeService.listDataByCatalog(user.enLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            });


            $scope.basicInit();


            vm.loadData();

            vm.loadContract(userLogin);

            vm.loadEducation(userLogin);


        }

        vm.loadData = function () {
            hrEmployeeService.getModelByUserLogin(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $("#birthDay").datepicker('setDate', new Date());
                    $("#politicsTime").datepicker('setDate', new Date());
                    $("#joinWorkTime").datepicker('setDate', new Date());
                    $("#joinCompanyTime").datepicker('setDate', new Date());
                    $("#leaveCompanyTime").datepicker('setDate', new Date());
                }
            });

        }


        vm.loadContract = function (userLogin) {
            hrEducationService.listData(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.educationList = value.data.data;
                }
            })
        }

        vm.loadEducation = function (userLogin) {
            hrContractService.listData(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.contractList = value.data.data;
                }
            })
        }


        vm.save = function () {
            if ($("#detail_form").validate().form()) {
                vm.item.operateUserLogin = user.userLogin;
                hrEmployeeService.update(vm.item).then(function (value) {
                    if (value.data.ret) {
                        var employee = value.data.data;

                        userLogin = employee.userLogin;
                        toastr.success("保存成功!");
                    }
                })
            }
        };

        vm.addEducation = function () {
            if (userLogin != "" && userLogin != null) {
                hrEmployeeService.getNewEducationModel(employeeId).then(function (value) {
                    if (value.data.ret) {
                        vm.education = value.data.data;

                    }
                });
                $("#educationModel").modal("show");
            } else {
                toastr.error("请先填写基础信息");
            }
        };
        vm.addContract = function () {
            if (userLogin != "" && userLogin != null) {
                hrEmployeeService.getNewContractModel(employeeId).then(function (value) {
                    if (value.data.ret) {
                        vm.contract = value.data.data;

                    }
                });
                $("#contractModel").modal("show");
            } else {
                toastr.error("请先填写基础信息");
            }
        };

        vm.updateEducation = function () {
            hrEmployeeService.updateEducation(vm.education).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData();
                }
            })
            $("#educationModel").modal("hide");
        };

        vm.updateContract = function () {
            hrEmployeeService.updateContract(vm.contract).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData();
                }
            })
            $("#contractModel").modal("hide");
        };

        vm.removeEducation = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    hrEmployeeService.removeEducation(id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        };

        vm.removeContract = function (id) {
            bootbox.confirm("确认要删除该条数据吗", function (result) {
                if (result) {
                    hrEmployeeService.removeContract(id).then(function (value) {
                        if (value.data.ret) {
                            vm.loadData();
                        }
                    })
                }
            })
        };

        vm.showEducation = function (id) {
            hrEducationService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.education = value.data.data;
                }
            });
            $("#educationModel").modal("show");
        };
        vm.showContract = function (id) {
            hrContractService.getModelById(id).then(function (value) {
                if (value.data.ret) {
                    vm.contract = value.data.data;
                }
            });
            $("#contractModel").modal("show");
        };


        vm.init();

        return vm;
    })

    .controller("HrEducationController", function ($state, $scope, hrEducationService) {
        var vm = this;

        vm.loadData = function (userLogin) {
            hrEducationService.listData(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.educationList = value.data.data;
                }
            })
        };

        vm.show = function (educationId) {
            var v = educationId;

            $state.go("hr.educationDetail", {educationId: educationId});
        }

        vm.add = function () {
            hrEducationService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrEducationService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadData(user.userLogin);
                    });
                }
            })
        }

        vm.loadData(user.userLogin);

        return vm;

    })

    .controller("HrEducationDetailController", function ($state, $stateParams, $scope, hrEducationService) {
        var vm = this;
        var educationId = $stateParams.educationId;


        vm.loadData = function (loadProcess) {
            hrEducationService.getModelById(educationId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    vm.contract = value.data.data;
                    $("#beginTime").datepicker('setDate', new Date());
                    $("#endTime").datepicker('setDate', new Date());
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrEducationService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    hrEducationService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        }

        vm.loadData(true);
        return vm;

    })

    .controller("HrContractController", function ($state, $scope, hrContractService) {
        var vm = this;

        vm.loadData = function (userLogin) {
            hrContractService.listData(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.contractList = value.data.data;
                }
            })
        };


        vm.show = function (contractId) {
            $state.go("hr.contractDetail", {contractId: contractId});
        }


        vm.add = function () {
            hrContractService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrContractService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadData(user.userLogin);
                    });
                }
            })
        }

        vm.loadData(user.userLogin);

        return vm;

    })

    .controller("HrContractDetailController", function ($state, $stateParams, $scope, hrContractService) {
        var vm = this;
        var contractId = $stateParams.contractId;

        vm.loadData = function () {
            $scope.basicInit();
            hrContractService.getModelById(contractId).then(function (value) {
                if (value.data.ret) {
                    vm.contract = value.data.data;
                    $("#beginTime").datepicker('setDate', new Date());
                    $("#endTime").datepicker('setDate', new Date());
                    $("#noticeTime").datepicker('setDate', new Date());
                    $("#receiveTime").datepicker('setDate', new Date());
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrContractService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    hrContractService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        }

        vm.loadData();
        return vm;

    })
    //个人信息
    .controller("SysMeController", function ($state, $scope, commonCodeService, hrEmployeeService,sysAttachService,myActService,businessContractService) {
        var vm = this;

        vm.init = function () {
            $("#headUpload").fileupload({
                maxNumberOfFiles: 1,
                url:'/common/attach/receive.json',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 1 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制1MB!');
                        return false;
                    }
                    data.formData = {source: "web", jwt: user.jwt,creator:user.userLogin};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    if (data.result.ret) {
                        vm.item.headAttachId = data.result.data;
                        $scope.$apply();
                        vm.save();
                    }
                }
            });

            $("#btnUploadSign").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if(file.name.indexOf(".dwg")<=0){
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt,creator:user.userLogin};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        vm.item.signUrl=result.data.name;
                        vm.item.signAttachId = result.data.id;
                        $scope.$apply();
                        vm.save();
                    }
                }
            });

            $("#btnUploadSignPic").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];

                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt,creator:user.userLogin};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result=JSON.parse(data.result);

                    if (result.ret) {
                        vm.item.signPicUrl=result.data.name;
                        vm.item.signPicAttachId = result.data.id;
                        $scope.$apply();
                        vm.save();
                    }
                }
            });

            commonCodeService.listDataByCatalog(user.enLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            });



            vm.loadData(user.userLogin);

        }

        vm.download=function(id){

            sysAttachService.download(id).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9);

                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        vm.loadData = function (userLogin) {
            hrEmployeeService.getModelByUserLogin(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.basicInit();
                    $("#birthDay").datepicker('setDate', vm.item.birthDay);
                    $("#politicsTime").datepicker('setDate', vm.item.politicsTime);
                    $("#joinWorkTime").datepicker('setDate', vm.item.joinWorkTime);
                    $("#joinCompanyTime").datepicker('setDate', vm.item.joinCompanyTime);
                    $("#leaveCompanyTime").datepicker('setDate', vm.item.leaveCompanyTime);
                    $("#rankTime").datepicker('setDate', vm.item.rankTime);
                }
            });
            //待办任务
            myActService.listTask(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.tasks = value.data.data;
                }
            })
            //参与项目
            var params = {pageNum: 1, pageSize: 999,userLogin:user.userLogin,uiSref:"me.edProject"};
            businessContractService.listAttendPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrEmployeeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(user.userLogin);
                }
            })
        }

        vm.init();

        return vm;

    })
    //我的日程
    .controller("SysScheduleController", function ($state, $scope,sysScheduleService) {
        var vm = this;
        vm.init = function () {
            $scope.basicInit("");
            vm.schedule={start:"",end:"",allDay:true,title:"",deleted:false};
            vm.loadData();
        }

        vm.loadData = function(){
            sysScheduleService.selectAll(user.userLogin).then(function (value) {
                vm.events = value.data.data;
                vm.initCalender();
            })
        }

        vm.initCalender=function(){
            if (!jQuery().fullCalendar) {
                return;
            }
            if (Metronic.isRTL()) {
                if ($('#calendar').parents(".portlet").width() <= 720) {
                    $('#calendar').addClass("mobile");
                    h = {
                        right: 'title, prev, next',
                        center: '',
                        left: 'agendaDay, agendaWeek, month, today'
                    };
                } else {
                    $('#calendar').removeClass("mobile");
                    h = {
                        right: 'title',
                        center: '',
                        left: 'agendaDay, agendaWeek, month, today, prev,next'
                    };
                }
            } else {
                if ($('#calendar').parents(".portlet").width() <= 720) {
                    $('#calendar').addClass("mobile");
                    h = {
                        left: 'title, prev, next',
                        center: '',
                        right: 'today,month,agendaWeek,agendaDay'
                    };
                } else {
                    $('#calendar').removeClass("mobile");
                    h = {
                        left: 'title',
                        center: '',
                        right: 'prev,next,today,month,agendaWeek,agendaDay'
                    };
                }
            }
            var initDrag = function(el) {
                // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
                // it doesn't need to have a start or end
                var eventObject = {
                    title: $.trim(el.text()) // use the element's text as the event title
                };
                // store the Event Object in the DOM element so we can get to it later
                el.data('eventObject', eventObject);
                // make the event draggable using jQuery UI
                el.draggable({
                    zIndex: 999,
                    revert: true, // will cause the event to go back to its
                    revertDuration: 0 //  original position after the drag
                });
            };

            var addEvent = function(title) {
                title = title.length === 0 ? "Untitled Event" : title;
                var html = $('<div class="external-event label label-default" style="font-size: 16px">' + title + '</div>');
                jQuery('#event_box').append(html);
                initDrag(html);
            };

            $('#external-events div.external-event').each(function() {
                initDrag($(this));
            });

            $('#event_add').unbind('click').click(function() {
                var title = $('#event_title').val();
                addEvent(title);
            });

            //predefined events
            $('#event_box').html("");
            addEvent("开会");
            addEvent("出差");
            addEvent("活动");

            $('#calendar').fullCalendar('destroy'); // destroy the calendar
            $('#calendar').fullCalendar({ //re-initialize the calendar
                header: h,
                defaultView: 'month', // change default view with available options from http://arshaw.com/fullcalendar/docs/views/Available_Views/
                slotMinutes: 15,
                editable: true,
                timezone:'local',
                droppable: true, // this allows things to be dropped onto the calendar !!!
                drop: function(date, allDay) { // this function is called when something is dropped
                    // retrieve the dropped element's stored Event Object
                    var originalEventObject = $(this).data('eventObject');
                    // we need to copy it, so that multiple events don't have a reference to the same object
                    var copiedEventObject = $.extend({}, originalEventObject);
                    // assign it the date that was reported
                    copiedEventObject.start = date;
                    copiedEventObject.allDay = allDay;
                    copiedEventObject.className = $(this).attr("data-class");

                    //新增数据到数据库
                    var start = copiedEventObject.start._d.format('yyyy-MM-dd hh:mm');
                    if(event.end!=null){
                        var end = copiedEventObject.end._d.format('yyyy-MM-dd hh:mm');
                    }
                    sysScheduleService.insert(start,end,'个人日程',copiedEventObject.title,user.userLogin).then(function (value) {
                        copiedEventObject.id = value.data.data.id;
                        toastr.success("新增成功！");
                        // render the event on the calendar
                        // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
                        $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

                        // is the "remove after drop" checkbox checked?
                        if ($('#drop-remove').is(':checked')) {
                            // if so, remove the element from the "Draggable Events" list
                            $(this).remove();
                        }
                    })
                },
                dayClick: function(date, jsEvent, view) {//空白的日期区，单击时触发
                    //alert("点击日期");
                   // $(this).css('background-color', 'red');
                    $scope.$apply(function() {
                        vm.schedule = {
                            start: date._d.format('yyyy-MM-dd hh:mm'),
                            end: "",
                            allDay: true,
                            title: "",
                            type: "个人日程",
                            deleted: false
                        };
                    })
                    $("#scheduleInformationModal").modal("show");
                },
                eventClick: function(calEvent, jsEvent, view) {//日程区块，单击时触发
                    // alert("点击日程");
                    $scope.$apply(function() {
                        vm.schedule.id = calEvent.id;
                        vm.schedule.title = calEvent.title;
                        vm.schedule.start = calEvent.start._d.format('yyyy-MM-dd hh:mm');
                        if (calEvent.end != null) {
                            vm.schedule.end = calEvent.end._d.format('yyyy-MM-dd hh:mm');
                        }
                        vm.schedule.type = calEvent.type;
                        vm.schedule.deleted = false;
                    });
                    $("#scheduleInformationModal").modal("show");
                    return false;  //return false可以阻止点击后续事件发生（比如event中的url跳转事件）
                },
                eventDrop : function(event, delta, revertFunc, jsEvent, ui, view){  //日程拖拽停止并且已经拖拽到其它位置了
                    // alert("拖拽停止到位");
                    //修改数据
                    vm.schedule.id = event.id;
                    vm.schedule.title = event.title;
                    vm.schedule.start = event.start._d.format('yyyy-MM-dd hh:mm');
                    if(event.end!=null){
                        vm.schedule.end = event.end._d.format('yyyy-MM-dd hh:mm');
                    }
                    vm.schedule.allDay = event.allDay;
                    sysScheduleService.update(vm.schedule).then(function (value) {
                        //vm.loadData();
                        toastr.success("更新成功！")
                    })
                },
                eventDragStart : function(event, jsEvent, ui, view){    //日程开始拖拽时触发
                    //alert("拖拽开始");
                },
                eventDragStop : function(event, jsEvent, ui, view){     //日程拖拽停止时触发
                    //alert("拖拽停止");
                },
                eventRender : function(event, element, view) {          //当Event对象开始渲染时触发
                    // alert("开始渲染");
                },
                eventResize : function(event, delta, revertFunc, jsEvent, ui, view){    //日程大小调整完成并已经执行更新时触发
                    //alert("当天拉扯到位");
                    //修改数据
                    $scope.$apply(function() {
                        vm.schedule.id = event.id;
                        vm.schedule.title = event.title;
                        vm.schedule.start = event.start._d.format('yyyy-MM-dd hh:mm');
                        if (event.end != null) {
                            vm.schedule.end = event.end._d.format('yyyy-MM-dd hh:mm');
                        }
                        vm.schedule.allDay = event.allDay;
                    })
                    sysScheduleService.update(vm.schedule).then(function (value) {
                        //vm.loadData();
                        toastr.success("更新成功！")
                    })
                },
                events: vm.events,
            });

        }

        vm.updateSchedule=function(schedule){
            if(schedule.id!=null){
                if(schedule.deleted){
                    sysScheduleService.remove(schedule.id,user.userLogin).then(function (value) {
                        toastr.success("删除成功！")
                        vm.loadData();
                    })
                }else{
                    sysScheduleService.update(schedule).then(function (value) {
                        toastr.success("更新成功！")
                        vm.loadData();
                    })
                }
            }else{
                sysScheduleService.insert(schedule.start,schedule.end,schedule.type,schedule.title,user.userLogin).then(function (value) {
                    toastr.success("新增成功！")
                    vm.loadData();
                })
            }

            $("#scheduleInformationModal").modal("hide");
        }

        vm.addEvent = function(){
            vm.schedule={start:"",end:"",allDay:true,title:"",type:"个人日程",deleted:false};
            $("#scheduleInformationModal").modal("show");
        }

        Date.prototype.format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }

        vm.init();

        return vm;

    })

    .controller("HrBasicController", function ($state, $scope, commonCodeService, hrEmployeeService,sysAttachService) {
        var vm = this;
        vm.init = function () {
            $("#btnUploadHead").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source: "web", jwt: user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        vm.item.headUrl = result.data.name;
                        vm.item.headAttachId = result.data.id;
                        $scope.$apply();
                        vm.save();
                    }
                }
            });
            $("#btnUploadSign").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if(file.name.indexOf(".dwg")<=0){
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        vm.item.signUrl=result.data.name;
                        vm.item.signAttachId = result.data.id;
                        $scope.$apply();
                        vm.save();
                    }
                }
            });
            $("#btnUploadSignPic").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];

                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        vm.item.signPicUrl=result.data.name;
                        vm.item.signPicAttachId = result.data.id;
                        $scope.$apply();
                        vm.save();
                    }
                }
            });
            commonCodeService.listDataByCatalog(user.enLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            });
            $scope.basicInit();

            vm.loadData(user.userLogin);

        }
        vm.download=function(id){

            sysAttachService.download(id).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9);

                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };
        vm.loadData = function (userLogin) {
            hrEmployeeService.getModelByUserLogin(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $("#birthDay").datepicker('setDate', new Date());
                    $("#politicsTime").datepicker('setDate', new Date());
                    $("#joinWorkTime").datepicker('setDate', new Date());
                    $("#joinCompanyTime").datepicker('setDate', new Date());
                    $("#leaveCompanyTime").datepicker('setDate', new Date());
                }
            });

        };
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrEmployeeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(user.userLogin);
                }
            })
        }

        vm.init();
        return vm;

    })

    .controller("HrBasicDetailController", function ($state, $stateParams,$scope, commonCodeService, hrEmployeeService,sysAttachService) {
        var vm = this;
        var userLogin = $stateParams.userLogin;
        vm.init = function () {
            $("#btnUploadHead").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source: "web", jwt: user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result = JSON.parse(data.result);
                    if (result.ret) {
                        vm.item.headUrl = result.data.name;
                        vm.item.headAttachId = result.data.id;

                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });
            $("#btnUploadSign").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];
                    if(file.name.indexOf(".dwg")<=0){
                        toastr.error('请上传dwg格式文件!');
                        return false;
                    }
                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        vm.item.signUrl=result.data.name;
                        vm.item.signAttachId = result.data.id;
                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });
            $("#btnUploadSignPic").fileupload({
                maxNumberOfFiles: 1,
                dataType: 'text',
                url:'/sys/attach/receive.do',
                send:function(e,data){
                    Metronic.blockUI({
                        boxed: true
                    });
                },
                add: function (e, data) {
                    var file = data.files[0];

                    if (file.size > 10 * 1024 * 1024) {
                        toastr.error('文件大小超过最大限制10MB!');
                        return false;
                    }
                    data.formData = {source:"web",jwt:user.jwt};
                    data.submit();
                },
                done: function (e, data) {
                    Metronic.unblockUI();
                    var result=JSON.parse(data.result);
                    if (result.ret) {
                        vm.item.signPicUrl=result.data.name;
                        vm.item.signPicAttachId = result.data.id;
                        $scope.$apply();
                        toastr.success("上传成功!");
                    }
                }
            });
            commonCodeService.listDataByCatalog(user.enLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            });
            /*            $("#birthDay").datepicker('setDate', new Date());
                        $("#politicsTime").datepicker('setDate', new Date());
                        $("#joinWorkTime").datepicker('setDate', new Date());
                        $("#joinCompanyTime").datepicker('setDate', new Date());
                        $("#leaveCompanyTime").datepicker('setDate', new Date());*/
            $scope.basicInit();
            vm.loadData(userLogin);

        }
        vm.download=function(id){

            sysAttachService.download(id).then(function (response) {
                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9);

                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };
        vm.loadData = function (userLogin) {
            hrEmployeeService.getModelByUserLogin(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                }
            });
        };
        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrEmployeeService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!");
                    vm.loadData(userLogin);
                }
            })
        }

        vm.init();
        return vm;

    })

    .controller("HrQualifyController", function ($state, $scope, $rootScope, hrQualifyService, hrDeptService,sysCodeService) {
        var vm = this;
        var key = $state.current.name + "_" + user.userLogin;
        var selectId=0;

        vm.init = function () {
            vm.params =getCacheParams(key,{qDeptName: "", qUserName: "", deptName: "", userName: "",parentDeptId:0, pageNum: 1, pageSize: $scope.pageSize,total:0 }) ;
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize: vm.params.pageSize,total:vm.params.total};
            vm.buildTree();

            sysCodeService.listAllMajorName().then(function (value) {
                if (value.data.ret) {
                    vm.majorNames = value.data.data;
                }
            })

            $scope.basicInit();
        }
        vm.queryData = function () {
            vm.params.parentDeptId=selectId;

            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.buildTree = function () {
            selectId=parseInt(vm.params.parentDeptId);
            hrDeptService.selectAll().then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        state: {opened: true, disabled: false, selected: false}
                    };
                    if (item.id === selectId||(selectId===0&&item.parentId===0)) {
                        node.state.selected = true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_tree').jstree("destroy");
                $('#dept_tree')
                    .on('changed.jstree', function (e, data) {
                        var node=data.instance.get_node(data.selected[0]);
                        if(selectId===parseInt(node.id)){
                            vm.loadPagedData();
                        }else{
                            selectId=node.id;
                            vm.params.qUserName = "";
                            vm.params.qDeptName = "";
                            vm.queryData();

                        }
                    })
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        }
                    });

            });
        };

        vm.loadPagedData = function () {
            hrQualifyService.listPagedData({
                parentDeptId:vm.params.parentDeptId,
                deptName: vm.params.qDeptName,
                q: vm.params.qUserName,
                pageNum: vm.pageInfo.pageNum,
                pageSize: vm.pageInfo.pageSize
            }).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                    setCacheParams(key,vm.params,vm.pageInfo);
                }
            });
        };


        vm.show = function (qualifyId) {
            $state.go("hr.qualifyDetail", {qualifyId: qualifyId});
        }


        vm.initWuzhouData = function () {
            hrQualifyService.initData().then(function (value) {
                if (value.data.ret) {
                    vm.queryData();
                    toastr.success("初始化成功!");
                } else {
                    toastr.error(value.data.msg);
                }
            });
        }

        vm.toggleEnable = function (id, role) {
            hrQualifyService.toggleEnable(id, role).then(function (value) {
                if (value.data.ret) {
                    vm.loadPagedData();
                } else {
                    toastr.error(value.data.msg);
                }
            });
        }

        vm.showSelectMajor = function () {
            singleCheckBox(".cb_major", "data-name");
            $("#selectMajorModal").modal("show");
        }

        vm.saveSelectMajor = function () {
            var id = vm.qualify.id;
            var majorName = $(".cb_major:checked").first().attr("data-name");
            hrQualifyService.updateMajor(id, majorName).then(function (value) {
                if (value.data.ret) {
                    vm.loadPagedData();
                    $("#selectMajorModal").modal("hide");
                } else {
                    toastr.error(value.data.msg);
                }
            })
        }

        vm.add = function () {
            hrQualifyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrQualifyService.remove(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            })
        }

        vm.copy = function (item) {
            bootbox.confirm("您确定要新增用户"+item.userName+"设计资格数据吗?", function (result) {
                if (result) {
                    hrQualifyService.copy(item.id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("复制成功!")
                            vm.loadPagedData();
                        }
                    });
                }
            });
        }



        vm.init();

        return vm;
    })

    .controller("HrMyQualifyController", function ($state, $scope, hrQualifyService) {
        var vm = this;
        vm.listData = function (userLogin) {
            hrQualifyService.listData(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;

                }
            })
        };
        vm.queryData = function () {
            vm.params.name = vm.params.qName;
            vm.pageInfo.pageNum = 1;
            vm.loadPagedData();
        };

        vm.show = function (qualifyId) {
            $state.go("hr.qualifyDetail", {qualifyId: qualifyId});
        }


        vm.add = function () {
            hrQualifyService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrQualifyService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadData(user.userLogin);
                    });
                }
            })
        }

        vm.listData(user.userLogin);

        return vm;

    })

    .controller("HrQualifyDetailController", function ($state, $stateParams, $scope, sysCodeService,hrQualifyService) {
        var vm = this;
        var qualifyId = $stateParams.qualifyId;

        vm.init=function(){
            vm.loadData();


            sysCodeService.listAllMajorName().then(function (value) {
                if(value.data.ret){
                    vm.majorNames=value.data.data;
                }
            })

        }

        vm.loadData = function () {
            hrQualifyService.getModelById(qualifyId).then(function (value) {
                if (value.data.ret) {
                    vm.qualify = value.data.data;
                    $scope.processInstance={myTaskFirst:true};
                    $scope.basicInit(vm.qualify.businessKey);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.qualify.operateUserLogin = user.userLogin;
            hrQualifyService.update(vm.qualify).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    hrQualifyService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        }


        vm.showSelectMajor=function() {
            singleCheckBox(".cb_major", "data-name");
            $("#selectMajorModal").modal("show");
        }
        vm.saveSelectMajor=function(){
            var id=vm.qualify.id;
            var majorName= $(".cb_major:checked").first().attr("data-name");
            hrQualifyService.updateMajor(id,majorName).then(function (value) {
                if (value.data.ret) {
                    vm.loadData();
                    $("#selectMajorModal").modal("hide");
                }else{
                    toastr.error(value.data.msg);
                }
            })
        }

        //选择项目类型
        vm.showProjectTypeModel=function(){
            sysCodeService.listDataByCatalog("设计作业类型").then(function (value) {
                if (value.data.ret) {
                    vm.projectTypes = value.data.data;
                    $("#selectProjectTypeModal").modal("show");
                }
            })

        }
        vm.saveSelectProjectType=function(){
            var value = [];
            $(".cb_projectType:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.qualify.projectType = value.join(',');
            $("#selectProjectTypeModal").modal("hide");
        };





        vm.init();


        return vm;
    })

    .controller("HrCertificationController", function ($state, $scope, hrCertificationService) {
        var vm = this;

        vm.loadData = function (userLogin) {
            hrCertificationService.listData(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.certificationList = value.data.data;

                }
            })
        };


        vm.show = function (certificationId) {
            $state.go("hr.certificationDetail", {certificationId: certificationId});
        }


        vm.add = function () {
            hrCertificationService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrCertificationService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadData(user.userLogin);
                    });
                }
            })
        }

        vm.loadData(user.userLogin);

        return vm;

    })

    .controller("HrCertificationDetailController", function ($state, $stateParams, $scope, hrCertificationService) {
        var vm = this;
        var certificationId = $stateParams.certificationId;


        vm.loadData = function (loadProcess) {
            hrCertificationService.getModelById(certificationId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);

                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrCertificationService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    hrCertificationService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        }

        vm.loadData(true);
        return vm;

    })

    .controller("HrPositionController", function ($state, $scope, $rootScope, hrPositionService,hrDeptService,hrEmployeeService) {

        var vm = this;
        vm.params = {name: "", qName: ""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:999};
        var key = $state.current.name + "_" + user.userLogin;
        vm.params = getCacheParams(key,{qUserName: "",qAclName:"", currentPositionId: 1,pageNum: 1, pageSize: $scope.pageSize,total:0});
        vm.currentPositionId=vm.params.currentPositionId;
        var tableName = $rootScope.loadTableName("hr.position");

        vm.buildTree = function () {
            hrPositionService.listSortedAll().then(function (value) {
                var list = value.data.data;
                var treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: "#",
                        text: item.positionName,
                        state: {opened: false, disabled: false, selected: false}
                    };
                    if (vm.params.currentPositionId == parseInt(item.id)) {
                        node.state.selected = true;
                    }
                    if(item.deleted){
                        node.text=node.text+"(作废)";
                    }
                    treeData.push(node);
                }
                $('#position_tree').jstree("destroy");
                $('#position_tree')
                    .on('changed.jstree', function (e, data) {
                        var node = data.instance.get_node(data.selected[0]);
                        if( vm.params.currentRoleId==parseInt(node.id)){
                            vm.loadAcl();
                            vm.loadEmployee();
                        }else{
                            vm.params.currentPositionId = node.id;
                            vm.params.currentPositionName = node.text;
                            vm.params.qUserName="";
                            vm.queryData();
                        }
                    })
                    .jstree({
                        'core': {
                            'data': treeData
                        }
                    });

            });
        };
        vm.init = function () {
            vm.buildTree();
            $scope.basicInit();
        };

        vm.queryData = function () {
            vm.params.name = vm.params.qName;
            vm.pageInfo.pageNum = 1;
            vm.loadEmployee();
        };

        vm.loadEmployee=function(){
            var params={positionId:vm.params.currentPositionId,q:vm.params.qUserName,pageNum:vm.pageInfo.pageNum,pageSize:vm.pageInfo.pageSize}
            hrEmployeeService.listPagedDataByPositionId(params).then(function (value) {
                vm.pageInfo=value.data.data;
                setCacheParams(key,vm.params,vm.pageInfo);
            })
        }
        vm.showUserSelectTree = function () {
            hrPositionService.getUserTree(vm.params.currentPositionId).then(function (value) {
                if (value.data.ret) {
                    var treeData = value.data.data;
                    $('#user_select_tree').jstree("destroy");
                    $('#user_select_tree')
                        .jstree({
                            'core': {
                                'data': treeData
                            },
                            "checkbox" : {
                                "keep_selected_style" : false
                            },
                            "plugins" : [ "wholerow", "checkbox" ]
                        });
                    $("#userSelectModal").modal("show");
                }
            });
        };

        vm.saveSelectUser = function () {
            var userList = $('#user_select_tree').jstree(true).get_selected().join(',');
            bootbox.confirm("您确认要保存数据吗?", function (result) {
                if (result) {
                    hrPositionService.savePositionUserList(vm.params.currentPositionId,userList).then(function (value) {
                        if(value.data.ret) {
                            toastr.success("更新成功!");
                            $("#userSelectModal").modal("hide");
                            vm.loadEmployee();
                        }
                    });
                }
            });
        };

        vm.show = function () {
            hrPositionService.getModelById(vm.params.currentPositionId).then(function (value) {
                vm.position = value.data.data;
                $("#positionModal").modal("show");
            });
        }

        vm.addPosition = function () {
            hrPositionService.getNewModel().then(function (value) {
                if (value.data.ret) {
                    vm.params.currentPositionId = value.data.data;
                    vm.show();
                }
            })
        }


        vm.showDeptModal=function(ids) {
            hrDeptService.selectAll().then(function (value) {
                var list = value.data.data;
                vm.treeData = [];
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var node = {
                        id: item.id,
                        parent: (item.parentId === 0 ? "#" : item.parentId.toString()),
                        text: item.name,
                        state: {opened: item.parentId === 0, disabled: false, selected: false}
                    };
                    if(ids.indexOf(","+item.id+",")>=0){
                        node.state.selected=true;
                    }
                    vm.treeData.push(node);
                }
                $('#dept_select_tree').jstree("destroy");
                $('#dept_select_tree')
                    .jstree({
                        'core': {
                            'data': vm.treeData
                        },
                        "checkbox" : {
                            "keep_selected_style" : false
                        },
                        "plugins" : [ "wholerow", "checkbox" ]
                    });
            });
            $("#deptSelectModal").modal("show");
        }

        vm.saveSelectDept=function(){
            var deptList= $('#dept_select_tree').jstree(true).get_selected();
            vm.position.childDeptIds=","+deptList.join(',')+",";
            $("#deptSelectModal").modal("hide");
        }

        vm.save = function () {
            if ($("#detail_form").valid()) {
                hrPositionService.update(vm.position).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!")
                        vm.buildTree();
                        $("#positionModal").modal("hide");
                    }
                })
            } else {
                toastr.warning("请准确填写数据!")
            }
        }

        vm.init();

        return vm;

    })

    .controller("HrInventController", function ($state, $scope, hrInventService) {
        var vm = this;

        vm.loadData = function (userLogin) {
            hrInventService.listData(userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.invnetList = value.data.data;
                }
            })
        };


        vm.show = function (inventId) {
            $state.go("hr.inventDetail", {inventId: inventId});
        }


        vm.add = function () {
            hrInventService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrInventService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadData(user.userLogin);
                    });
                }
            })
        }

        vm.loadData(user.userLogin);

        return vm;

    })

    .controller("HrInventDetailController", function ($state, $stateParams, $scope, hrInventService) {
        var vm = this;
        var inventId = $stateParams.inventId;

        vm.loadData = function (loadProcess) {
            hrInventService.getModelById(inventId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrInventService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }


        vm.loadData(true);
        return vm;

    })

    .controller("HrHonorController", function ($state, $scope, hrHonorService) {
        var vm = this;
        vm.params = {name: "", qName: "",deptName:""};
        vm.pageInfo = {pageNum: 1, pageSize: $scope.pageSize,total:0,total:0};
        vm.loadData = function () {
            var params = {q: vm.params.qName,userLogin:user.userLogin,pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize};
            hrHonorService.listPagedData(params).then(function (value) {
                if (value.data.ret) {
                    vm.pageInfo = value.data.data;
                }
            })
        };

        vm.show = function (honorId) {
            $state.go("hr.honorDetail", {honorId: honorId});
        }

        vm.add = function () {
            hrHonorService.getNewModel(user.userLogin).then(function (value) {
                if (value.data.ret) {
                    vm.show(value.data.data);
                }
            })
        }

        vm.remove = function (id) {
            bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                if (result) {
                    hrHonorService.remove(id, user.userLogin).then(function (value) {
                        toastr.success("删除成功!")
                        vm.loadData(user.userLogin);
                    });
                }
            })
        }

        vm.loadData();

        return vm;

    })

    .controller("HrHonorDetailController", function ($state, $stateParams, $scope, hrHonorService) {
        var vm = this;
        var honorId = $stateParams.honorId;

        vm.loadData = function (loadProcess) {
            hrHonorService.getModelById(honorId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    if (loadProcess) {
                        $scope.loadProcessInstance(vm.item.processInstanceId);
                        $scope.basicInit(vm.item.businessKey);
                    }
                    $("#honorDate").datepicker('setDate', vm.item.honorDate);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            hrHonorService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    hrHonorService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        }

        vm.loadData(true);
        return vm;

    })

    .controller("FiveHrChiefQualifyApplyDetailController", function ($state, $stateParams, $scope, commonCodeService,fiveHrChiefQualifyApplyService, hrEmployeeService) {
        var vm = this;
        var chiefQualifyApplyId = $stateParams.chiefQualifyApplyId;

        vm.loadData = function () {
            $scope.basicInit();
            fiveHrChiefQualifyApplyService.getModelById(chiefQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);
                    $("#majorTime").datepicker('setDate', vm.item.majorTime);
                    $("#titleTime").datepicker('setDate', vm.item.titleTime);
                }
            })
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveHrChiefQualifyApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    fiveHrChiefQualifyApplyService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        }

        vm.showEmployeeModel = function () {
            vm.qEmployeeDeptName = '开发测试部门';
            hrEmployeeService.selectAll().then(function (value) {
                vm.employees = value.data.data;
                singleCheckBox(".cb_employee", "data-name");
            });
            $("#selectEmployeeModel").modal("show");
        }

        vm.saveSelectEmployee = function () {

            var login;
            var name;
            var position;
            var graduateCollege;
            var graduateMajor;
            $(".cb_employee:checked").each(function () {
                var info = $(this).attr("data-name");
                login = info.split('_')[0];
                name = info.split('_')[1];
                position = info.split('_')[2];
                graduateCollege = info.split('_')[3];
                graduateMajor = info.split('_')[4];
            });
            vm.item.userName = name;
            vm.item.userLogin = login;
            vm.item.position = position;
            vm.item.graduateCollege = graduateCollege;
            vm.item.graduateMajor = graduateMajor;

            $("#selectEmployeeModel").modal("hide");
        };
        vm.loadData();
        vm.print = function () {
            fiveHrChiefQualifyApplyService.getPrintData(chiefQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    lodop = getLodop();
                    vm.printData = value.data.data;
                    lodop.PRINT_INIT("总设计师资格认定申报表");
                    var strBodyStyle = "<style>" + document.getElementById("print_style").innerHTML + "</style>";
                    setTimeout(function () {
                        var strFormHtml = strBodyStyle + "<body>" + document.getElementById("print_areaChief").innerHTML + "</body>";
                        lodop.ADD_PRINT_HTM(50, 25, "94%", "100%", strFormHtml);
                        lodop.PREVIEW();
                    }, 500);
                }
            })
        }
        //选择专业
        vm.showMajorModel=function(){

            commonCodeService.listDataByCatalog(user.enLogin,"设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majors = value.data.data;
                    $("#selectMajorModal").modal("show");
                }
            })

        }
        vm.saveSelectMajor=function(){
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.item.major = value.join(',');

            $("#selectMajorModal").modal("hide");
        }

        //选择现承担项目类型
        vm.showProjectTypeNowModel=function(){
            $("#selectProjectTypeNowModal").modal("show");
        }
        vm.saveSelectProjectTypeNow=function(){
            var value = [];
            $(".cb_projectTypeNow:checked").each(function () {
                value.push($(this).attr("data-name"));
            });

            $("#selectProjectTypeNowModal").modal("hide");
        }
        vm.downloadModel=function(){
            fiveHrChiefQualifyApplyService.downloadModel().then(function (response) {

                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=","");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };
        vm.loadData();
        return vm;

    })

    .controller("FiveHrQualifyApplyController", function ($state, $scope, $rootScope, fiveHrChiefQualifyApplyService) {
        var vm = this;
        // var tableName = $rootScope.loadTableName("five.hrQualifyApply");

        alert(1);

        return vm;
    })

    .controller("FiveHrQualifyApplyDetailController", function ($state, $stateParams, $scope, fiveHrQualifyApplyService,sysCodeService, hrEmployeeService) {
        var vm = this;
        var qualifyApplyId = $stateParams.id;

        vm.loadData = function () {
            $scope.basicInit();
            fiveHrQualifyApplyService.getModelById(designQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.item = value.data.data;
                    $scope.loadProcessInstance(vm.item.processInstanceId);
                    $scope.basicInit(vm.item.businessKey);

                }
            })


            vm.loadDetail();
        };

        /**
         * 保存基础信息
         */
        vm.save = function () {
            vm.item.operateUserLogin = user.userLogin;
            fiveHrQualifyApplyService.update(vm.item).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功!")
                    vm.loadData(false);
                }
            })
        }

        /**
         * 发送流程
         */
        vm.showSendFlow = function () {
            if ($scope.processInstance.myTaskFirst) {
                if ($("#detail_form").validate().form()) {
                    vm.item.operateUserLogin = user.userLogin;
                    fiveHrQualifyApplyService.update(vm.item).then(function (value) {
                        if (value.data.ret) {
                            $scope.showSendFlow();
                        }
                    })
                } else {
                    toastr.warning("请准确填写数据!")
                    return;
                }
            } else {
                $scope.showSendFlow();
            }
        }

        vm.loadDetail = function () {
            fiveHrQualifyApplyService.listDetailByQualifyId(designQualifyApplyId).then(function (value) {
                if (value.data.ret) {
                    vm.list = value.data.data;

                    vm.designs = [];
                    $.each(vm.list, function (index, value) {
                        vm.designs.push(value.userLogin);
                    })

                    $("table tr").find('td').each(function(){
                        if($(this).html()=="√"){
                            s
                            $(this).css("color","red");
                        }
                    })
                }
            })
        }

        vm.showDetailModel = function (id) {
            $("#detailModel").modal("show");
            fiveHrQualifyApplyService.getDetailById(id).then(function (value) {
                if (value.data.ret) {
                    vm.detail = value.data.data;
                    vm.loadDetail();
                }
            })
        }

        vm.removeDetail = function (id) {
            bootbox.confirm("确定要删除该数据吗?", function (result) {
                if (result) {
                    fiveHrQualifyApplyService.removeDetail(id, user.userLogin).then(function (value) {
                        if (value.data.ret) {
                            toastr.success("删除成功");
                            vm.loadDetail();
                            vm.loadData(false);
                        }
                    })
                }
            });

        };

        vm.saveDetail = function () {
            if ($("#qualifyApply_form").validate().form()) {
                fiveHrQualifyApplyService.updateDetail(vm.detail).then(function (value) {
                    if (value.data.ret) {
                        toastr.success("保存成功!");
                        vm.loadDetail();
                    }
                    $("#detailModel").modal("hide");
                })
            }
        };
        vm.showEmployeeModel = function () {
            vm.qEmployeeDeptName = '开发测试部门';
            hrEmployeeService.selectAll().then(function (value) {
                vm.employees = value.data.data;
                singleCheckBox(".cb_employee", "data-name");
            });
            $("#selectEmployeeModel").modal("show");
        }
        vm.saveSelectEmployee = function () {

            var login;
            var name;
            var deptId;
            var deptName;
            $(".cb_employee:checked").each(function () {
                var info = $(this).attr("data-name");
                login = info.split('_')[0];
                name = info.split('_')[1];
                deptId = info.split('_')[2];
                deptName = info.split('_')[3];

            });
            vm.detail.userName = name;
            vm.detail.userLogin = login;
            vm.detail.deptId = deptId;
            vm.detail.deptName = deptName;

            $("#selectEmployeeModel").modal("hide");
        };
        vm.selectDesign = function () {
            vm.qEmployeeDeptName = vm.item.deptName;
            hrEmployeeService.selectAll().then(function (value) {
                vm.employees = value.data.data;
            });
            $("#selectDesignModel").modal("show");
        }
        vm.saveSelectDesign = function () {
            var login = [];
            $(".cb_design:checked").each(function () {
                var info = $(this).attr("data-name");
                login.push(info.split('_')[0]);
            });
            vm.designs = login;

            fiveHrQualifyApplyService.updateDesign(vm.designs, designQualifyApplyId).then(function (value) {
                vm.loadDetail();
            });
            $("#selectDesignModel").modal("hide");
        };
        vm.changeDesign = function (detailId) {
            if ($scope.processInstance.myTaskFirst){
                fiveHrQualifyApplyService.changeDesign(detailId).then(function (value) {
                    vm.loadDetail();
                });
            }else{
                toastr.warning("只允许创建人修改！")
            }

        }
        vm.changeProofread = function (detailId) {

            if ($scope.processInstance.myTaskFirst){
                fiveHrQualifyApplyService.changeProofread(detailId).then(function (value) {
                    vm.loadDetail();
                });
            }else{
                toastr.warning("只允许创建人修改！")
            }
        }
        vm.changeAudit = function (detailId) {

            if ($scope.processInstance.myTaskFirst){
                fiveHrQualifyApplyService.changeAudit(detailId).then(function (value) {
                    vm.loadDetail();
                });
            }else{
                toastr.warning("只允许创建人修改！")
            }
        }
        vm.changeProChief= function (detailId) {

            if ($scope.processInstance.myTaskFirst){
                fiveHrQualifyApplyService.changeProChief(detailId).then(function (value) {
                    vm.loadDetail();
                });
            }else{
                toastr.warning("只允许创建人修改！")
            }
        }


        //选择专业
        vm.showMajorModel=function(){
            sysCodeService.listDataByCatalog("设计专业").then(function (value) {
                if (value.data.ret) {
                    vm.majors = value.data.data;
                    $("#selectMajorModal").modal("show");
                }
            })

        }
        vm.saveSelectMajor=function(){
            var value = [];
            $(".cb_major:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.detail.majorName = value.join(',');
            $("#selectMajorModal").modal("hide");
        }

        //选择项目类型
        vm.showProjectTypeModel=function(){
            sysCodeService.listDataByCatalog("设计作业类型").then(function (value) {
                if (value.data.ret) {
                    vm.projectTypes = value.data.data;
                    $("#selectProjectTypeModal").modal("show");
                }
            })

        }
        vm.saveSelectProjectType=function(){
            var value = [];
            $(".cb_projectType:checked").each(function () {
                value.push($(this).attr("data-name"));
            });
            vm.detail.projectType = value.join(',');
            $("#selectProjectTypeModal").modal("hide");
        }

        vm.downloadModel=function(){
            fiveHrQualifyApplyService.downloadModel().then(function (response) {

                var objectUrl = URL.createObjectURL(new Blob([response.data.data], {type: response.data.data.type}));
                var contentDisposition = response.data.headers['content-disposition'];
                var fileName = contentDisposition.substring(contentDisposition.indexOf("=")).replace("=","");
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = decodeURI(fileName);
                a.href = objectUrl;
                a.click();
            });
        };

        vm.loadData();
        return vm;

    })
