angular.module('services.five1', [])

    .factory('fiveEdService', function ($q, $http) {


        var listInputReviewFile = function (nodeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/five/ed/listInputReviewFile.json',
                params: {
                    nodeId: nodeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var initArrangeUserFromLatestEdTask = function (arrangeFormDataId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/five/ed/initArrangeUserFromLatestEdTask.json',
                params: {
                    arrangeFormDataId: arrangeFormDataId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        return {
            listInputReviewFile: function (nodeId) {
                return listInputReviewFile(nodeId);
            },
            initArrangeUserFromLatestEdTask: function (arrangeFormDataId) {
                return initArrangeUserFromLatestEdTask(arrangeFormDataId);
            }
        }
    })




    .factory('fiveEdTaskService', function ($q, $http) {

        var head = "/five/task";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByForeignKey = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByForeignKey.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (id, userLogin) {
                return getNewModel(id, userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listDataByForeignKey: function (stepId) {
                return listDataByForeignKey(stepId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },

        }
    })

    .factory('fiveEdTaskUserService', function ($q, $http) {

        var head = "/five/taskUser";


        var updateSelect = function (id, optUserType, userLogins) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateSelect.json',
                params: {
                    id: id,
                    optUserType: optUserType,
                    userLogins: userLogins
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByTaskId = function (taskId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByTaskId.json',
                params: {
                    taskId: taskId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            updateSelect: function (id, optUserType, userLogins) {
                return updateSelect(id, optUserType, userLogins);
            },
            listDataByTaskId: function (taskId) {
                return listDataByTaskId(taskId);
            },

        }
    })

    .factory('fiveEdArrangeService', function ($q, $http) {

        var head = "/five/arrange";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByForeignKey = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByForeignKey.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getAllCanAttendUser = function (id,stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getAllCanAttendUser.json',
                params: {
                    id: id,
                    stepId:stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (id, userLogin) {
                return getNewModel(id, userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listDataByForeignKey: function (stepId) {
                return listDataByForeignKey(stepId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            getAllCanAttendUser: function (id,stepId) {
                return getAllCanAttendUser(id,stepId);
            },

        }
    })

    .factory('fiveEdArrangeUserService', function ($q, $http) {

        var head = "/five/arrangeUser";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByDesignPlanId = function (arrangeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByArrangeId.json',
                params: {
                    arrangeId: arrangeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listMajorCode = function (arrangeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listMajorCode.json',
                params: {
                    arrangeId: arrangeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (arrangeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    arrangeId: arrangeId
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
                url: head + '/remove.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getUserTree = function (arrangeUserId, selectedUsers) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getUserTree.json',
                params: {
                    arrangeUserId: arrangeUserId,
                    selectedUsers: selectedUsers
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateMajorList = function (arrangeId, majors) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateMajorList.json',
                params: {
                    arrangeId: arrangeId,
                    majors: majors
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateSelect = function (id, optUserType, userLogins) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateSelect.json',
                params: {
                    id: id,
                    optUserType: optUserType,
                    userLogins: userLogins
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listHistoryConfig = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listHistoryConfig.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateUserByHistory = function (id, arrangeUserId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateUserByHistory.json',
                params: {
                    id: id,
                    arrangeUserId: arrangeUserId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDefaultArrangeUser = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDefaultArrangeUser.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listEdValidateUserByLogin = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listEdValidateUserByLogin.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getAllDefaultUser = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getAllDefaultUser.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {

            getModelById: function (id) {
                return getModelById(id);
            },
            listDataByDesignPlanId: function (arrangeId) {
                return listDataByDesignPlanId(arrangeId);
            },
            getNewModel: function (arrangeId) {
                return getNewModel(arrangeId);
            },
            remove: function (id) {
                return remove(id);
            },
            update: function (item) {
                return update(item);
            },
            listMajorCode: function (arrangeId) {
                return listMajorCode(arrangeId);
            },
            getUserTree: function (arrangeUserId, selectedUsers) {
                return getUserTree(arrangeUserId, selectedUsers);
            },
            updateMajorList: function (arrangeId, majors) {
                return updateMajorList(arrangeId, majors);
            },
            updateSelect: function (id, optUserType, userLogins) {
                return updateSelect(id, optUserType, userLogins);
            },
            listHistoryConfig: function (id) {
                return listHistoryConfig(id);
            },
            updateUserByHistory: function (id, arrangeUserId) {
                return updateUserByHistory(id, arrangeUserId);
            },
            listDefaultArrangeUser: function (stepId) {
                return listDefaultArrangeUser(stepId);
            },
            listEdValidateUserByLogin: function (stepId, userLogin) {
                return listEdValidateUserByLogin(stepId, userLogin);
            },
            getAllDefaultUser: function (stepId) {
                return getAllDefaultUser(stepId);
            },

        }
    })

    .factory('fiveEdArrangePlanService', function ($q, $http) {

        var head = "/five/arrangePlan";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByArrangeId = function (arrangeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByArrangeId.json',
                params: {
                    arrangeId: arrangeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listMajorCode = function (arrangeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listMajorCode.json',
                params: {
                    arrangeId: arrangeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (arrangeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    arrangeId: arrangeId
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
                url: head + '/remove.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateChoseTime = function (id, times) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateChoseTime.json',
                params: {
                    id: id,
                    times: times
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var updateMajorList = function (arrangeId, majors) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateMajorList.json',
                params: {
                    arrangeId: arrangeId,
                    majors: majors
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {

            getModelById: function (id) {
                return getModelById(id);
            },
            listDataByArrangeId: function (arrangeId) {
                return listDataByArrangeId(arrangeId);
            },
            getNewModel: function (arrangeId) {
                return getNewModel(arrangeId);
            },
            remove: function (id) {
                return remove(id);
            },
            updateChoseTime: function (id, times) {
                return updateChoseTime(id, times);
            },
            listMajorCode: function (arrangeId) {
                return listMajorCode(arrangeId);
            },

            updateMajorList: function (arrangeId, majors) {
                return updateMajorList(arrangeId, majors);
            },


        }
    })

    .factory('fiveEdArrangeTimetableService', function ($q, $http) {

        var head = "/five/arrangeTimetable";

        var listDataByArrangeId = function (arrangeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByArrangeId.json',
                params: {
                    arrangeId: arrangeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByMajor = function (arrangeId,sendMajor) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByMajor.json',
                params: {
                    arrangeId: arrangeId,
                    sendMajor:sendMajor
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var remove = function (arrangeId, majorName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    arrangeId: arrangeId,
                    majorName: majorName
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var saveTimetable = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/saveTimetable.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {

            listDataByArrangeId: function (arrangeId) {
                return listDataByArrangeId(arrangeId);
            },
            listDataByMajor: function (arrangeId,sendMajor) {
                return listDataByMajor(arrangeId,sendMajor);
            },
            remove: function (arrangeId, sendMajor) {
                return remove(arrangeId, sendMajor);
            },
            saveTimetable: function (item) {
                return saveTimetable(item);
            },
        }
    })

    .factory('fiveEdReviewSpecialService', function ($q, $http) {

        var head = "/five/reviewSpecial";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listMajorCode = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listMajorCode.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {

            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getNewModel: function (stepId, userLogin) {
                return getNewModel(stepId, userLogin);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            },
            listMajorCode: function (id) {
                return listMajorCode(id);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            }
        }
    })

    .factory('fiveEdReviewApplyService', function ($q, $http) {

        var head = "/five/reviewApply";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listMajorCode = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listMajorCode.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {

            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getNewModel: function (stepId, userLogin) {
                return getNewModel(stepId, userLogin);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            },
            listMajorCode: function (id) {
                return listMajorCode(id);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            }
        }
    })

    .factory('fiveEdHouseReferService', function ($q, $http) {

        var head = "/five/houseRefer";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getNewModel = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listMajorCode = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listMajorCode.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {

            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getNewModel: function (stepId, userLogin) {
                return getNewModel(stepId, userLogin);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            },
            listMajorCode: function (id) {
                return listMajorCode(id);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            }
        }
    })

    .factory('fiveEdHouseValidateService', function ($q, $http) {

        var head = "/five/houseValidate";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getNewModel = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listEdValidateUserByLogin = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listEdValidateUserByLogin.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var resetMajorName = function (id, arrangeUserId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/resetMajorName.json',
                params: {
                    id: id,
                    arrangeUserId: arrangeUserId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getValidateInfo = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getValidateInfo.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getCoPublishFile = function (majorName,stepId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getCoPublishFile.json',
                params: {
                    majorName: majorName,
                    stepId: stepId,
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {

            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getNewModel: function (stepId, userLogin) {
                return getNewModel(stepId, userLogin);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            listEdValidateUserByLogin: function (stepId, userLogin) {
                return listEdValidateUserByLogin(stepId, userLogin);
            },
            resetMajorName: function (id, arrangeUserId) {
                return resetMajorName(id, arrangeUserId);
            },
            getValidateInfo: function (stepId) {
                return getValidateInfo(stepId);
            },
            getCoPublishFile:function (majorName,stepId,userLogin) {
                return getCoPublishFile(majorName,stepId,userLogin);
            },
        }
    })

    .factory('fiveEdCountersignService', function ($q, $http) {

        var head = "/five/countersign";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByForeignKey = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByForeignKey.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (id, userLogin) {
                return getNewModel(id, userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listDataByForeignKey: function (stepId) {
                return listDataByForeignKey(stepId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
        }
    })

    .factory('fiveEdProductService', function ($q, $http) {

        var head = "/five/product";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getNewModel = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewDetail = function (productId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewDetail.json',
                params: {
                    productId: productId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var removeDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listDetail = function (productId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    productId: productId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var updateDetail = function (detail) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: detail
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {

            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getNewModel: function (stepId, userLogin) {
                return getNewModel(stepId, userLogin);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            },
            getNewDetail: function (productId) {
                return getNewDetail(productId);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            removeDetail: function (id,userLogin) {
                return removeDetail(id,userLogin);
            },
            listDetail: function (productId) {
                return listDetail(productId);
            },
            updateDetail: function (detail) {
                return updateDetail(detail);
            },
        }
    })

    .factory('fiveEdStampService', function ($q, $http) {

        var head = "/five/stamp";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getNewModel = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listPlotApply = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPlotApply.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var saveEdFiles = function (ids,businessId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/saveEdFiles.json',
                params: {
                    ids: ids,
                    businessId:businessId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listStampCode = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listStampCode.json',
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        return {

            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getNewModel: function (stepId, userLogin) {
                return getNewModel(stepId, userLogin);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            },
            listPagedData:function(params){
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            listStampCode: function () {
                return listStampCode();
            },
            listPlotApply: function (id) {
                return listPlotApply(id);
            },
            saveEdFiles: function (ids,businessId) {
                return saveEdFiles(ids,businessId);
            }
        }
    })

    .factory('fiveEdPlotApplyService', function ($q, $http) {

        var head = "/five/plotApply";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByForeignKey = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByForeignKey.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (id, userLogin) {
                return getNewModel(id, userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listDataByForeignKey: function (stepId) {
                return listDataByForeignKey(stepId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },

        }
    })

    .factory('fiveEdPlotApplyDetailService', function ($q, $http) {

        var head = "/five/plotApplyDetail";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (plotId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    plotId: plotId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listData = function (plotId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listData.json',
                params: {
                    plotId: plotId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (plotId) {
                return getNewModel(plotId);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listData: function (plotId) {
                return listData(plotId);
            },

        }
    })


    .factory('fiveEdQualityCheckService', function ($q, $http) {

        var head = "/five/qualityCheck";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (stepId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: stepId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewDetail = function (qualityCheckId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewDetail.json',
                params: {
                    qualityCheckId: qualityCheckId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (qualityCheckId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    qualityCheckId: qualityCheckId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (detail) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: detail
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listMajorCode = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listMajorCode.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getArrangeMajor = function (qualityCheckId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getFiveArrangeMajor.json',
                params: {
                    qualityCheckId: qualityCheckId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getNewModel: function (stepId, userLogin) {
                return getNewModel(stepId, userLogin);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            getNewDetail: function (qualityCheckId) {
                return getNewDetail(qualityCheckId);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            removeDetail: function (id,userLogin) {
                return removeDetail(id,userLogin);
            },
            listDetail: function (qualityCheckId) {
                return listDetail(qualityCheckId);
            },
            updateDetail: function (detail) {
                return updateDetail(detail);
            },
            listMajorCode: function (id) {
                return listMajorCode(id);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
        }
    })

    .factory('fiveEdQualityAnalysisService', function ($q, $http) {

        var head = "/five/qualityAnalysis";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByForeignKey = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByForeignKey.json',
                params: {
                    stepId: stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            insert: function (item) {
                return insert(item);
            },

            update: function (item) {
                return update(item);
            },

            getModelById: function (id) {
                return getModelById(id);
            },

            getNewModel: function (id, userLogin) {
                return getNewModel(id, userLogin);
            },

            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },

            listDataByForeignKey: function (stepId) {
                return listDataByForeignKey(stepId);
            },

            getPrintData: function (id) {
                return getPrintData(id);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            }
        }
    })

    .factory('fiveEdDesignDrawingCheckService', function ($q, $http) {

        var head = "/five/designDrawingCheck";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByStepId = function (stepId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (id, userLogin) {
                return getNewModel(id, userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listDataByStepId: function (stepId,userLogin) {
                return listDataByStepId(stepId,userLogin);
            },
        }
    })

    .factory('fiveEdMajorDrawingCheckService', function ($q, $http) {

        var head = "/five/majorDrawingCheck";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    stepId: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByStepId = function (stepId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByStepId.json',
                params: {
                    stepId: stepId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getBuildList = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getBuildList.json',
                params: {
                    stepId: stepId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getMajorList = function (stepId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getMajorList.json',
                params: {
                    stepId: stepId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelDetail = function (checkId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    checkId: checkId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (checkId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    checkId: checkId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var downTempleXls = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: {
                    stepId:stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getPrintData.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (id, userLogin) {
                return getNewModel(id, userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listDataByStepId: function (stepId,userLogin) {
                return listDataByStepId(stepId,userLogin);
            },

            getMajorList: function (stepId,userLogin) {
                return getMajorList(stepId,userLogin);
            },

            getBuildList: function (stepId) {
                return getBuildList(stepId);
            },

            getNewModelDetail: function (checkId) {
                return getNewModelDetail(checkId);
            },

            getModelDetailById: function (id) {
                return getModelDetailById(id);
            },

            removeDetail: function (id, userLogin) {
                return removeDetail(id, userLogin);
            },

            updateDetail: function (item) {
                return updateDetail(item);
            },

            listDetail: function (checkId) {
                return listDetail(checkId);
            },
            downTempleXls: function (stepId) {
                return downTempleXls(stepId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
        }
    })

    .factory('fiveEdDesignDrawingService', function ($q, $http) {

    var head = "/five/designDrawing";

    var getModelById = function (id) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head + '/getModelById.json',
            params: {
                id: id
            }
        }).success(function (data) {
            deferred.resolve(data);
        }).error(function (error) {
            deferred.reject(error);
        });
        return deferred.promise;
    };

    var listDataByStepId = function (stepId,userLogin) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head + '/listDataByStepId.json',
            params: {
                stepId: stepId,
                userLogin:userLogin
            }
        }).success(function (data) {
            deferred.resolve(data);
        }).error(function (error) {
            deferred.reject(error);
        });
        return deferred.promise;
    };

        var listDetail = function (drawingId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    drawingId: drawingId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
    return {
        getModelById: function (id) {
            return getModelById(id);
        },
        listDataByStepId: function (stepId,userLogin) {
            return listDataByStepId(stepId,userLogin);
        },
        listDetail: function (drawingId) {
            return listDetail(drawingId);
        },
        getModelDetailById: function (id) {
            return getModelDetailById(id);
        },
    }
})

