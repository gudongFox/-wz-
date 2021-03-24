angular.module('services.act', [])

    .factory('actModelService', function ($q, $http) {

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/model/listPagedData.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/model/getNewModel.json',
                params:{
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var deployment = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/model/deployment.json',
                params:{
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/model/remove.json',
                params:{
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listModelCategory = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/model/listModelCategory.json',
                params:{
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getNewModel: function (enLogin) {
                return getNewModel(enLogin);
            },
            remove: function (id) {
                return remove(id);
            },
            deployment: function (id) {
                return deployment(id);
            },
            listModelCategory:function (enLogin) {
                return listModelCategory(enLogin);
            }
        }
    })

    .factory('actProcessDefinitionService', function ($q, $http) {

        var listPagedDefinition = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processDefinition/listPagedDefinition.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDefinition = function (deploymentId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processDefinition/removeDefinition.json',
                params:{
                    deploymentId:deploymentId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            listPagedDefinition: function (params) {
                return listPagedDefinition(params);
            },
            removeDefinition: function (deploymentId) {
                return removeDefinition(deploymentId);
            }
        }
    })

    .factory('actProcessQueryService', function ($q, $http) {

        var listPagedProcessInstance = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/listPagedProcessInstance.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listProcessInstanceByH5 = function (initiator, involvedUser, q, firstResult, maxResults) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/listProcessInstanceByH5.json',
                params:{
                    initiator:initiator,
                    involvedUser:involvedUser,
                    q:q,
                    firstResult:firstResult,
                    maxResults:maxResults
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getProcessInstance = function (processInstanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/getProcessInstance.json',
                params:{
                    processInstanceId:processInstanceId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listFormDataGroup = function (taskId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/listFormDataGroup.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listBusinessFile = function (taskId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/listBusinessFile.json',
                params:{
                    taskId:taskId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getBusinessFile = function (fileId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/getBusinessFile.json',
                params:{
                    fileId:fileId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getCustomProcessInstance = function (processInstanceId,businessKey,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/getCustomProcessInstance.json',
                params:{
                    processInstanceId:processInstanceId,
                    businessKey:businessKey,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getAdminCustomProcessInstance = function (processInstanceId,businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/getAdminCustomProcessInstance.json',
                params:{
                    processInstanceId:processInstanceId,
                    businessKey:businessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNgDirectUrl = function (businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/getNgDirectUrl.json',
                params:{
                    businessKey:businessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listProcessCategoryTree = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processQuery/listProcessCategoryTree.json',
                params:{
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listPagedProcessInstance: function (params) {
                return listPagedProcessInstance(params);
            },
            listProcessInstanceByH5: function (initiator, involvedUser, q, firstResult, maxResults) {
                return listProcessInstanceByH5(initiator, involvedUser, q, firstResult, maxResults);
            },
            getProcessInstance: function (processInstanceId) {
                return getProcessInstance(processInstanceId);
            },
            getCustomProcessInstance:function(processInstanceId,businessKey,enLogin){
                return getCustomProcessInstance(processInstanceId,businessKey,enLogin);
            },
            getAdminCustomProcessInstance:function(processInstanceId,businessKey){
                return getAdminCustomProcessInstance(processInstanceId,businessKey);
            },
            listFormDataGroup: function (taskId,enLogin) {
                return listFormDataGroup(taskId,enLogin);
            },
            listBusinessFile: function (taskId) {
                return listBusinessFile(taskId);
            },
            getBusinessFile: function (fileId) {
                return getBusinessFile(fileId);
            },
            getNgDirectUrl:function (businessKey) {
                return getNgDirectUrl(businessKey);
            },
            listProcessCategoryTree:function (enLogin) {
                return listProcessCategoryTree(enLogin);
            }
        }
    })

    .factory('actProcessHandleService', function ($q, $http) {

        var toggleStar = function (processInstanceId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processHandle/toggleStar.json',
                params:{
                    processInstanceId:processInstanceId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var suspendProcessInstanceById = function (processInstanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processHandle/suspendProcessInstanceById.json',
                params:{
                    processInstanceId:processInstanceId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var activateProcessInstanceById = function (processInstanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processHandle/activateProcessInstanceById.json',
                params:{
                    processInstanceId:processInstanceId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var deleteProcessInstanceById = function (processInstanceId,enLogin,deleteReason) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processHandle/deleteProcessInstanceById.json',
                params:{
                    processInstanceId:processInstanceId,
                    enLogin:enLogin,
                    deleteReason:deleteReason
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var deleteProcessDataById = function (processInstanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/processHandle/deleteProcessDataById.json',
                params:{
                    processInstanceId:processInstanceId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            toggleStar:function(processInstanceId,enLogin){
              return toggleStar(processInstanceId,enLogin);
            },
            suspendProcessInstanceById: function (processInstanceId) {
                return suspendProcessInstanceById(processInstanceId);
            },
            activateProcessInstanceById: function (processInstanceId) {
                return activateProcessInstanceById(processInstanceId);
            },
            deleteProcessInstanceById: function (processInstanceId,enLogin,deleteReason) {
                return deleteProcessInstanceById(processInstanceId,enLogin,deleteReason);
            },
            deleteProcessDataById: function (processInstanceId) {
                return deleteProcessDataById(processInstanceId);
            }

        }
    })

    .factory('actTaskHandleService', function ($q, $http) {

        var listNextCandidateTask = function (taskId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/listNextCandidateTask.json',
                params:{
                    taskId:taskId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listBackCandidateTask = function (taskId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/listBackCandidateTask.json',
                params:{
                    taskId:taskId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var sendTaskSimple = function (taskId,comment,enLogin,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/sendTaskSimple.json',
                params:{
                    taskId:taskId,
                    comment:comment,
                    enLogin:enLogin,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var takeTask = function (taskId,enLogin,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/takeTask.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var fetchTask = function (taskId,enLogin,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/fetchTask.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var ccFinishTask = function (taskId,comment,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/ccFinishTask.json',
                params:{
                    taskId:taskId,
                    comment:comment,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var sendTask = function (taskId,enLogin,ccUser,comment,candidateUsers,completeTask,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/sendTask.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin,
                    comment:comment,
                    ccUser:ccUser,
                    candidateUsers:candidateUsers,
                    completeTask:completeTask,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var backTask = function (taskId,enLogin,comment,candidateUsers,completeTask,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/backTask.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin,
                    comment:comment,
                    candidateUsers:candidateUsers,
                    completeTask:completeTask,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var backTaskSimple = function (taskId,comment,enLogin,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/backTaskSimple.json',
                params:{
                    taskId:taskId,
                    comment:comment,
                    enLogin:enLogin,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var delegateTask = function (taskId,enLogin,newAssignee,comment,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/delegateTask.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin,
                    newAssignee:newAssignee,
                    comment:comment,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var resolveTask = function (taskId,enLogin,comment,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/resolveTask.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin,
                    comment:comment,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var transferTask = function (taskId,enLogin,newOwner,comment,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/transferTask.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin,
                    newOwner:newOwner,
                    comment:comment,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var forceNewAssignee = function (taskId,newAssignee,agent) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskHandle/forceNewAssignee.json',
                params:{
                    taskId:taskId,
                    newAssignee:newAssignee,
                    agent:agent
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {

            sendTask:function (taskId,enLogin,ccUser,comment,candidateUsers,completeTask) {
                return sendTask(taskId,enLogin,ccUser,comment,candidateUsers,completeTask);
            },
            sendTaskSimple:function(taskId,comment,enLogin,agent){
                return sendTaskSimple(taskId,comment,enLogin,agent);
            },
            backTaskSimple:function(taskId,comment,enLogin,agent){
                return backTaskSimple(taskId,comment,enLogin,agent);
            },
            resolveTask:function(taskId,enLogin,comment,agent){
                return resolveTask(taskId,enLogin,comment,agent);
            },
            transferTask:function(taskId,enLogin,newOwner,comment,agent){
                return transferTask(taskId,enLogin,newOwner,comment,agent);
            },
            delegateTask:function(taskId,enLogin,newAssignee,comment,agent){
                return delegateTask(taskId,enLogin,newAssignee,comment,agent);
            },
            forceNewAssignee:function(taskId,newAssignee,agent){
                return forceNewAssignee(taskId,newAssignee,agent);
            },

            backTask:function (taskId,enLogin,comment,candidateUsers,completeTask,agent) {
                return backTask(taskId,enLogin,comment,candidateUsers,completeTask,agent);
            },
            takeTask:function(taskId,enLogin,agent){
                return takeTask(taskId,enLogin,agent);
            },
            ccFinishTask:function(taskId,comment,agent){
                return ccFinishTask(taskId,comment,agent);
            },
            fetchTask:function(taskId,enLogin,agent){
                return fetchTask(taskId,enLogin,agent);
            },
            listNextCandidateTask: function (taskId) {
                return listNextCandidateTask(taskId);
            },
            listBackCandidateTask: function (taskId) {
                return listBackCandidateTask(taskId);
            }
        }
    })

    .factory('actTaskQueryService', function ($q, $http) {

        var listPagedCcTask = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listPagedCcTask.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getCcTaskCount = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/getCcTaskCount.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedTask = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listPagedTask.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getTaskCount = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/getTaskCount.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listTaskByH5 = function (enLogin,q,firstResult,maxResults) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listTaskByH5.json',
                params:{
                    enLogin: enLogin,
                    q:q,
                    firstResult:firstResult,
                    maxResults:maxResults
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listRunningTaskByH5 = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listRunningTaskByH5.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedHistoricTask = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listPagedHistoricTask.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listHistoricTaskInstanceByTaskId = function (taskId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listHistoricTaskInstanceByTaskId.json',
                params: {
                    taskId:taskId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listHistoricTaskInstance = function (processInstanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listHistoricTaskInstance.json',
                params: {
                    processInstanceId:processInstanceId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getHistoricTaskInstance = function (taskId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/getHistoricTaskInstance.json',
                params:{
                    taskId:taskId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listHistoricTaskInstanceByInstanceId = function (processInstanceId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/listHistoricTaskInstanceByInstanceId.json',
                params: {
                    processInstanceId:processInstanceId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getHistoricTaskInstanceByInstanceId = function (processInstanceId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/act/taskQuery/getHistoricTaskInstanceByInstanceId.json',
                params:{
                    processInstanceId:processInstanceId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listPagedCcTask: function (params) {
                return listPagedCcTask(params);
            },
            getCcTaskCount: function (enLogin) {
                return getCcTaskCount(enLogin);
            },
            listPagedTask: function (params) {
                return listPagedTask(params);
            },
            getTaskCount: function (params) {
                return getTaskCount(params);
            },
            listTaskByH5: function (enLogin, q, firstResult, maxResults) {
                return listTaskByH5(enLogin, q, firstResult, maxResults);
            },
            listPagedHistoricTask: function (params) {
                return listPagedHistoricTask(params);
            },
            listHistoricTaskInstance: function (processInstanceId) {
                return listHistoricTaskInstance(processInstanceId);
            },
            listHistoricTaskInstanceByTaskId: function (taskId, enLogin) {
                return listHistoricTaskInstanceByTaskId(taskId, enLogin);
            },
            listHistoricTaskInstanceByInstanceId: function (processInstanceId, enLogin) {
                return listHistoricTaskInstanceByInstanceId(processInstanceId, enLogin);
            },
            getHistoricTaskInstanceByInstanceId: function (processInstanceId, enLogin) {
                return getHistoricTaskInstanceByInstanceId(processInstanceId, enLogin);
            },
            getHistoricTaskInstance: function (taskId, enLogin) {
                return getHistoricTaskInstance(taskId, enLogin);
            },
            //必须参数：enLogin,firstResult,maxResults
            listRunningTaskByH5: function (params) {
                return listRunningTaskByH5(params);
            }
        }
    })


