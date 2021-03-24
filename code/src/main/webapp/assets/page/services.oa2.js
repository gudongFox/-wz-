angular.module('services.oa2', [])
    .factory('oaMeetingRoomService', function ($q, $http) {
        var head="/oa/meetingRoom";

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
                    userLogin:userLogin
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

        var listAllRoom = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listAllRoom.json',
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

            listAllRoom:function () {
                return listAllRoom();
            }
        }
    })

    .factory('oaMeetingRoomApplyService', function ($q, $http) {
        var head="/oa/meetingRoomApply";

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
                    userLogin:userLogin
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


    .factory('oaCarService', function ($q, $http) {
        var head="/oa/car";

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
                    userLogin:userLogin
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

        var listAllCar = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listAllCar.json',
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
            listAllCar:function () {
                return listAllCar();
            }
        }
    })

    .factory('oaCarApplyService', function ($q, $http) {
    var head="/oa/carApply";

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
                userLogin:userLogin
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

    .factory('oaStampApplyService', function ($q, $http) {
        var head="/oa/stampApply";

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
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModelByStampName = function (userLogin,stampName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModelByStampName.json',
                params: {
                    userLogin:userLogin,
                    stampName:stampName
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
        var getUserByDeptName = function (deptName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getUserByDeptName.json',
                params: {
                    deptName:deptName
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
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            getNewModelByStampName:function(userLogin,stampName){
                return getNewModelByStampName(userLogin,stampName);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getUserByDeptName: function (deptName) {
                return getUserByDeptName(deptName);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
        }
    })

    .factory('oaHardwareService', function ($q, $http) {
        var head="/oa/hardware";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
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
                    userLogin:userLogin
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
            getModelById:function(id){
                return getModelById(id);
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

    .factory('oaSoftwareService', function ($q, $http) {
        var head="/oa/software";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
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
                    userLogin:userLogin
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
            getModelById:function(id){
                return getModelById(id);
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

    .factory('oaLinkService', function ($q, $http) {
        var head="/oa/link";
        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
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
        var add = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/add.json',
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
        var getLogoAttachById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getLogoAttachById.json',
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
            update:function(item){
                return update(item);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            add:function(item){
                return add(item);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getLogoAttachById:function(id){
                return getLogoAttachById(id);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },

        }
    })