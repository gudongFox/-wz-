angular.module('services.oa1', [])

    .factory('oaNoticeService', function ($q, $http) {
        var head="/oa/notice";

        var getModelById = function (contractId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId,
                    userLogin:userLogin
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
                url: head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModelByType = function (userLogin,type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModelByType.json',
                params: {
                    userLogin:userLogin,
                    type:type,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listDateByType = function (type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listDateByType.json',
                params: {
                    type:type,
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
                url: head+ '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var fuzzySearch = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/fuzzySearch.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listPagedDataByType = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listPagedDataByType.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listPagedDataByUserLogin = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listPagedDataByUserLogin.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listPagedDataOrderType = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listPagedDataOrderType.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var selectAll = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/selectAll.json',
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModelByDispatch = function (businessKey,types,noticeLevel,noticeSystemType) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModelByDispatch.json',
                params: {
                    businessKey:businessKey,
                    types:types,
                    noticeLevel:noticeLevel,
                    noticeSystemType:noticeSystemType,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var sendToWx = function (oaNoticeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/sendToWx.json',
                params: {
                    oaNoticeId:oaNoticeId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var sendToWxTest = function (oaNoticeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/sendToWxTest.json',
                params: {
                    oaNoticeId:oaNoticeId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getAcceptUser = function (oaNoticeId,type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getAcceptUser.json',
                params: {
                    oaNoticeId:oaNoticeId,
                    type:type
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId,userLogin){
                return getModelById(contractId,userLogin);
            },
            listDateByType:function(type){
                return listDateByType(type);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            getNewModelByType:function(userLogin,type){
                return getNewModelByType(userLogin,type);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            listPagedDataByType: function (params) {
                return listPagedDataByType(params);
            },
            fuzzySearch: function (params) {
                return fuzzySearch(params);
            },
            selectAll: function () {
                return selectAll();
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            listPagedDataByUserLogin: function (params) {
                return listPagedDataByUserLogin(params);
            },
            listPagedDataOrderType: function (params) {
                return listPagedDataOrderType(params);
            },
            getNewModelByDispatch:function(businessKey,types,noticeLevel,noticeSystemType){
                return getNewModelByDispatch(businessKey,types,noticeLevel,noticeSystemType);
            },
            sendToWx:function(oaNoticeId){
                return sendToWx(oaNoticeId);
            },
            sendToWxTest:function(oaNoticeId){
                return sendToWxTest(oaNoticeId);
            },
            getAcceptUser:function(oaNoticeId,type){
                return getAcceptUser(oaNoticeId,type);
            },
        }
    })

    .factory('oaKnowledgeService', function ($q, $http) {
        var head="/oa/knowledge";

        var getModelById = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId
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
                url: head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    userLogin:userLogin,
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
                url: head+ '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            }
        }
    })

    .factory('oaTechnologyService', function ($q, $http) {
        var head="/oa/technology";
        var getModelById = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId
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
                url: head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (userLogin,type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    userLogin:userLogin,
                    type:type
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
                url: head+ '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(userLogin,type){
                return getNewModel(userLogin,type);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('oaSoftwareApplyService', function ($q, $http) {
        var head="/oa/softwareApply";
        var getModelById = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId
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
                url: head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    userLogin:userLogin,

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
                url: head+ '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('oaArchiveService', function ($q, $http) {
        var head="/oa/archive";
        var getModelById = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId
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
                url: head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    userLogin:userLogin,

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
                url: head+ '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('oaArchiveDetailService', function ($q, $http) {
        var head="/oa/archiveDetail";
        var getModelById = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId
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
                url: head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (archiveId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    archiveId:archiveId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var selectAll = function (attachId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/selectAll.json',
                params: {
                    attachId:attachId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listDetail = function (archiveId,detailIds) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listDetail.json',
                params: {
                    archiveId:archiveId,
                    detailIds:detailIds,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(archiveId,userLogin){
                return getNewModel(archiveId,userLogin);
            },
            selectAll: function (attachId) {
                return selectAll(attachId);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            listDetail:function(archiveId,detailIds){
                return listDetail(archiveId,detailIds);
            },
        }
    })

    .factory('oaArchiveApplyService', function ($q, $http) {
        var head="/oa/archiveApply";
        var getModelById = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId
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
                url: head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    userLogin:userLogin,
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
                url: head+ '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('oaNoticeApplyService', function ($q, $http) {

        var head = "/oa/noticeApply";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
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

        var manualTurnNotice = function (id, types) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/manualTurnNotice.json',
                params: {
                    id: id,
                    types: types
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelByType = function (userLogin,type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelByType.json',
                params: {
                    userLogin: userLogin,
                    type:type
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

            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },

            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },

            manualTurnNotice: function (id, types) {
                return manualTurnNotice(id, types);
            },

            getNewModelByType: function (userLogin,type) {
                return getNewModelByType(userLogin,type);
            },

        }
    })
