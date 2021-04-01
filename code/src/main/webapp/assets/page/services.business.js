angular.module('services.business', [])

    .factory('businessCustomerService', function ($q, $http) {

        var head = "/business/customer";

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
        var checkCustomer = function (name,customerId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/checkCustomer.json',
                params: {
                    name: name,
                    customerId:customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var checkTaxNo=function (taxNo,customerId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/checkTaxNo.json',
                params: {
                    taxNo: taxNo,
                    customerId:customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }


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




        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var loadPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/loadPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }



        var listCooperationProject = function (customerId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listCooperationProject.json',
                params: {
                    customerId: customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listUsedNamesById = function (customerId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listUsedNamesById.json',
                params: {
                    customerId: customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getUsedNameById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getUsedNameById.json',
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
        var getNewUsedName = function ( customerId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewUsedName.json',
                params: {
                    userLogin: userLogin,
                    customerId:customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var removeUsedName = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeUsedName.json',
                params: {
                    userLogin: userLogin,
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var updateUsedName = function (usedName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateUsedName.json',
                data: usedName
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelByNameAndLinMan = function (customerName,linkMan) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelByNameAndLinMan.json',
                params: {
                    customerName: customerName,
                    linkMan:linkMan
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downloadModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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
            checkCustomer: function (name,customerId) {
                return checkCustomer(name,customerId);
            },
            checkTaxNo: function (taxNo,customerId) {
                return checkTaxNo(taxNo,customerId);
            },

            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            loadPagedData: function (params) {
                return loadPagedData(params);
            },
            selectAll: function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            listCooperationProject: function (customerId) {
                return listCooperationProject(customerId);
            },
            listUsedNamesById: function (customerId) {
                return listUsedNamesById(customerId);
            },
            getUsedNameById: function (id) {
                return getUsedNameById(id);
            },
            updateUsedName: function (usedName) {
                return updateUsedName(usedName);
            },
            getNewUsedName: function (customerId,userLogin) {
                return getNewUsedName(customerId,userLogin);
            },
            removeUsedName: function (id,userLogin) {
                return removeUsedName(id,userLogin);
            },

            downloadModel:function(userLogin){
                return downloadModel(userLogin);
            },
            getModelByNameAndLinMan:function(customerName,linkMan){
                return getModelByNameAndLinMan(customerName,linkMan);
            },

        }
    })
    .factory('businessChangeCustomerService', function ($q, $http) {

        var head = "/business/changeCustomer";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var loadPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/loadPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listCooperationProject = function (customerId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listCooperationProject.json',
                params: {
                    customerId: customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getModelByNameAndLinMan = function (customerName,linkMan) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelByNameAndLinMan.json',
                params: {
                    customerName: customerName,
                    linkMan:linkMan
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downloadModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            loadPagedData: function (params) {
                return loadPagedData(params);
            },
            selectAll: function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            listCooperationProject: function (customerId) {
                return listCooperationProject(customerId);
            },
            downloadModel:function(userLogin){
                return downloadModel(userLogin);
            },
            getModelByNameAndLinMan:function(customerName,linkMan){
                return getModelByNameAndLinMan(customerName,linkMan);
            },

        }
    })
    .factory('businessChangeSupplierService', function ($q, $http) {

        var head = "/business/changeSupplier";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var loadPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/loadPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listCooperationProject = function (customerId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listCooperationProject.json',
                params: {
                    customerId: customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getModelByNameAndLinMan = function (customerName,linkMan) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelByNameAndLinMan.json',
                params: {
                    customerName: customerName,
                    linkMan:linkMan
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downloadModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            loadPagedData: function (params) {
                return loadPagedData(params);
            },
            selectAll: function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            listCooperationProject: function (customerId) {
                return listCooperationProject(customerId);
            },
            downloadModel:function(userLogin){
                return downloadModel(userLogin);
            },
            getModelByNameAndLinMan:function(customerName,linkMan){
                return getModelByNameAndLinMan(customerName,linkMan);
            },

        }
    })

    .factory('businessPreContractService', function ($q, $http) {

        var head = "/business/preContract";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var loadPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/loadPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getProcessInstanceDto = function (processInstanceId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getProcessInstanceDto.json',
                params: {
                    processInstanceId: processInstanceId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var sendFlow = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/sendFlow.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var backFlow = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/backFlow.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getProjectNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getProjectNo.json',
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

        var selectNotHaveContract = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectNotHaveContract.json',
                params: {
                    contractId:contractId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectNotHaveInput = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectNotHaveInput.json',
                params: {
                    contractId:contractId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var insertContractLibrary = function (preContractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractLibrary.json',
                params: {
                    preContractId:preContractId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var addReviewContract = function (preContractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addReviewContract.json',
                params: {
                    preContractId:preContractId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var addCustomer = function (userLogin,preContractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addCustomer.json',
                params: {
                    userLogin: userLogin,
                    preContractId:preContractId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        return {
            getProcessInstanceDto: function (processInstanceId, userLogin) {
                return getProcessInstanceDto(processInstanceId, userLogin);
            },
            sendFlow: function (params) {
                return sendFlow(params);
            },
            backFlow: function (params) {
                return backFlow(params);
            },
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            loadPagedData: function (params) {
                return loadPagedData(params);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            selectNotHaveContract: function (contractId) {
            return selectNotHaveContract(contractId);
            },
            selectNotHaveInput: function (contractId) {
                return selectNotHaveInput(contractId);
            },
            insertContractLibrary:function(preContractId){
                return insertContractLibrary(preContractId);
            },
            addReviewContract:function(preContractId){
                return addReviewContract(preContractId);
            },
            addCustomer:function(userLogin,preContractId){
                return addCustomer(userLogin,preContractId);
            },
        }
    })
    .factory('businessInputContractService', function ($q, $http) {

        var head = "/business/inputContract";

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

        var getContractNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getContractNo.json',
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

        var getProjectNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getProjectNo.json',
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var loadPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/loadPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getProcessInstanceDto = function (processInstanceId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getProcessInstanceDto.json',
                params: {
                    processInstanceId: processInstanceId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var sendFlow = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/sendFlow.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var backFlow = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/backFlow.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var insertContractLibrary = function (inputContractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractLibrary.json',
                params: {
                    inputContractId:inputContractId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        return {
            getProcessInstanceDto: function (processInstanceId, userLogin) {
                return getProcessInstanceDto(processInstanceId, userLogin);
            },
            sendFlow: function (params) {
                return sendFlow(params);
            },
            backFlow: function (params) {
                return backFlow(params);
            },
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            loadPagedData: function (params) {
                return loadPagedData(params);
            },
            getContractNo: function (id) {
                return getContractNo(id);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            insertContractLibrary:function(inputContractId){
                return insertContractLibrary(inputContractId);
            },
        }
    })

    .factory('businessContractService', function ($q, $http) {

        var head = "/business/contract";

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

        var saveCustomer = function (id, item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/saveCustomer.json',
                data: item,
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var loadPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/loadPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listAttendPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAttendPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listAllPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAllPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getProcessInstanceDto = function (processInstanceId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getProcessInstanceDto.json',
                params: {
                    processInstanceId: processInstanceId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var sendFlow = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/sendFlow.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var backFlow = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/backFlow.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listBusinessBidContract = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listBusinessBidContract.json',
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

        var getNewContractNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewContractNo.json',
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

        var getNewModelById = function (userLogin,bidContractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelById.json',
                params: {
                    userLogin:userLogin,
                    bidContractId: bidContractId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var statisticByUserLogin = function (userLogin,searchTime) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/statisticByUserLogin.json',
                params: {
                    userLogin:userLogin,
                    searchTime: searchTime,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downloadModelExcel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModelExcel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

        var downloadModelSimpleExcel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModelSimpleExcel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

        var downloadModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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
        return {
            getProcessInstanceDto: function (processInstanceId, userLogin) {
                return getProcessInstanceDto(processInstanceId, userLogin);
            },
            sendFlow: function (params) {
                return sendFlow(params);
            },
            backFlow: function (params) {
                return backFlow(params);
            },
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            loadPagedData: function (params) {
                return loadPagedData(params);
            },
            listAllPagedData: function (params) {
                return listAllPagedData(params);
            },
            listAttendPagedData:function (params) {
                return listAttendPagedData(params);
            },
            listBusinessBidContract: function (id) {
                return listBusinessBidContract(id);
            },
            getNewContractNo: function (id) {
                return getNewContractNo(id);
            },
            getNewModelById:function (userLogin,bigContractId) {
                return getNewModelById(userLogin,bigContractId);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            downloadModelExcel: function (userLogin) {
                return downloadModelExcel(userLogin);
            },
            downloadModelSimpleExcel: function (userLogin) {
                return downloadModelSimpleExcel(userLogin);
            },
            statisticByUserLogin: function (userLogin,searchTime) {
                return statisticByUserLogin(userLogin,searchTime);
            },
        }
    })

    .factory('businessBidApplyService', function ($q, $http) {

        var head = "/business/bigApply";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
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

        var listAgent = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAgent.json',
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
        var getNewModelById = function (recordId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelById.json',
                params: {
                    recordId: recordId,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listAllUnExistRecord = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAllUnExistRecord.json',
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
            insert: function (item) {
                return insert(item);
            },
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
            listAgent: function () {
                return listAgent();
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            listAllUnExistRecord: function (id) {
                return listAllUnExistRecord(id);
            },
            getNewModelById: function (recordId,userLogin) {
                return getNewModelById(recordId,userLogin);
            }
        }
    })

    .factory('businessBidAttendService', function ($q, $http) {

        var head = "/business/bidAttend";

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

        var updateById = function (bidApplyId, bidAttendId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateById.json',
                params: {
                    bidApplyId: bidApplyId,
                    bidAttendId: bidAttendId
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
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelByApplyId = function (bidApplyId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelByApplyId.json',
                params: {
                    userLogin: userLogin,
                    bidApplyId: bidApplyId
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

        var listAllUnExistApply = function (applyId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAllUnExistApply.json',
                params: {
                    applyId: applyId
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
            updateById: function (bidApplyId, bidAttendId) {
                return updateById(bidApplyId, bidAttendId);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            getNewModelByApplyId: function (bidApplyId, userLogin) {
                return getNewModelByApplyId(bidApplyId, userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            listAllUnExistApply: function (applyId) {
                return listAllUnExistApply(applyId);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            }
        }
    })

    .factory('businessBidProjectChargeService', function ($q, $http) {

        var head = "/business/bidProjectCharge";

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

        var updateById = function (attendId, id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateById.json',
                params: {
                    attendId: attendId,
                    id: id
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
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelById = function (attendId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelId.json',
                params: {
                    userLogin: userLogin,
                    attendId: attendId
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

        var listAllUnExistAttend = function (attendId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAllUnExistAttend.json',
                params: {
                    attendId: attendId
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
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },

            updateById: function (attendId, userLogin) {
                return updateById(attendId, userLogin);
            },
            getNewModelById: function (attendId, id) {
                return getNewModelById(attendId, id);
            },
            listAllUnExistAttend: function (attendId) {
                return listAllUnExistAttend(attendId);
            },
        }
    })

    .factory('businessBidContractService', function ($q, $http) {

        var head = "/business/bidContract";

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

        var updateById = function (projectChargeId, id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateById.json',
                params: {
                    projectChargeId: projectChargeId,
                    id: id
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
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelById = function (projectChargeId, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelById.json',
                params: {
                    userLogin: userLogin,
                    projectChargeId: projectChargeId
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

        var listAllUnExistProjectCharge = function (chargeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',//chargeId
                url: head + '/listAllUnExistProjectCharge.json',
                params: {
                    chargeId: chargeId
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
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            updateById: function (projectChargeId, userLogin) {
                return updateById(projectChargeId, userLogin);
            },
            getNewModelById: function (projectChargeId, id) {
                return getNewModelById(projectChargeId, id);
            },
            listAllUnExistProjectCharge: function (chargeId) {
                return listAllUnExistProjectCharge(chargeId);
            },
        }
    })

    .factory('businessRecordService', function ($q, $http) {

        var head = "/business/record";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
        var listPagedDataCommon = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedDataCommon.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listRecordByUserLogin = function (userLogin,id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listRecordByUserLogin.json',
                params: {
                    userLogin: userLogin,
                    recordId:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var downloadModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

        var addCustomer = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addCustomer.json',
                params: {
                    userLogin: userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getProjectNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getProjectNo.json',
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

        var selectReview = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectReview.json',
                params: {
                    userLogin:userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectPre = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectPre.json',
                params: {
                    userLogin:userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            listRecordByUserLogin: function (userLogin,id) {
                return listRecordByUserLogin(userLogin,id);
            },
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            listPagedDataCommon: function (params) {
                return listPagedDataCommon(params);
            },

            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            addCustomer:function(userLogin,recordId){
                return addCustomer(userLogin,recordId);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            selectReview:function(userLogin,recordId){
                return selectReview(userLogin,recordId);
            },
            selectPre:function(userLogin,recordId){
                return selectPre(userLogin,recordId);
            },
        }
    })

    .factory('fiveBusinessContractReviewService', function ($q, $http) {

        var head = "/business/contractReview";

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

        var changeOpen = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpen.json',
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
        var changeOpenStamp = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpenStamp.json',
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

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateDetail.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/removeDetail.json',
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

        var listDetailById = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDetailById.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewDetailModel = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewDetailModel.json',
                params: {
                    contractReviewId:contractReviewId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDetailModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getDetailModelById.json',
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

        var addCustomer = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addCustomer.json',
                params: {
                    userLogin: userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertContractLibrary = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractLibrary.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectNotHaveContract = function (contractReviewId) {
                var deferred = $q.defer();
                return $http({
                    method: 'POST',
                    url: head+'/selectNotHaveContract.json',
                    params: {
                        contractReviewId:contractReviewId
                    }
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(function (error) {
                    deferred.reject(error);
                });
                return deferred.promise;
            }
        var getContractNo = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getContractNo.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getMainContractNo = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getMainContractNo.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var addSuccessContractDir = function (businessKey,attachId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addSuccessContractDir.json',
                params: {
                    businessKey:businessKey,
                    attachId:attachId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }




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
            changeOpen: function (id, userLogin) {
                return changeOpen(id, userLogin);
            },
            changeOpenStamp: function (id, userLogin) {
                return changeOpenStamp(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },

            updateDetail:function(item){
                return updateDetail(item);
            },
            removeDetail:function(id){
                return removeDetail(id);
            },
            listDetailById: function (contractReviewId) {
                return listDetailById(contractReviewId);
            },
            getNewDetailModel:function (contractReviewId) {
                return getNewDetailModel(contractReviewId);
            },
            getDetailModelById:function(id){
                return getDetailModelById(id);
            },

            addCustomer:function(userLogin,recordId){
                return addCustomer(userLogin,recordId);
            },
            insertContractLibrary:function(contractReviewId){
                return insertContractLibrary(contractReviewId);
            },
            selectNotHaveContract:function(contractReviewId){
                return selectNotHaveContract(contractReviewId);
            },
            getContractNo:function(contractReviewId){
                return getContractNo(contractReviewId);
            },
            getMainContractNo:function(contractReviewId){
                return getMainContractNo(contractReviewId);
            },
            addSuccessContractDir:function(businessKey,attachId,userLogin){
                return addSuccessContractDir(businessKey,attachId,userLogin);
            },






        }
    })

    .factory('fiveBusinessContractLibraryService', function ($q, $http) {

        var head = "/business/contractLibrary";

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
        var selectAllNotHaveStampTax = function (contractId,userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAllNotHaveStampTax.json',
                params: {
                    contractId:contractId,
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectSubpackage = function (userLogin,uiSref,libraryId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectSubpackage.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref,
                    libraryId:libraryId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectMainReviewByIds = function (userLogin,uiSref,libraryId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectMainReviewByIds.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref,
                    libraryId:libraryId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectIncome = function (userLogin,uiSref,libraryId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectIncome.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref,
                    libraryId:libraryId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectInvoice = function (userLogin,uiSref,libraryId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectInvoice.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref,
                    libraryId:libraryId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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
        var listPagedDataCommon = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedDataCommon.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var addCustomer = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addCustomer.json',
                params: {
                    userLogin: userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectNotHaveContract = function (contractLibraryId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectNotHaveContract.json',
                params: {
                    contractLibraryId:contractLibraryId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectMainContract = function (contractType) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectMainContract.json',
                params: {
                    contractType:contractType
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectAllByIncome = function (contractLibraryId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectAllByIncome.json',
                params: {
                    contractLibraryId:contractLibraryId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getIncomeMoney = function (incomeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getIncomeMoney.json',
                params: {
                    incomeId:incomeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var downTempleXls = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+'/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

        var statisticalData = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+'/statisticalData.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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
        var changeOpen = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpen.json',
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
            listPagedDataCommon: function (params) {
                return listPagedDataCommon(params);
            },
            selectAllNotHaveStampTax: function (contractId,userLogin,uiSref) {
                return selectAllNotHaveStampTax(contractId,userLogin,uiSref);
            },
            selectAll: function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            selectSubpackage: function (userLogin,uiSref,libraryId) {
                return selectSubpackage(userLogin,uiSref,libraryId);
            },
            selectMainReviewByIds: function (userLogin,uiSref,libraryId) {
                return selectMainReviewByIds(userLogin,uiSref,libraryId);
            },
            selectIncome: function (userLogin,uiSref,libraryId) {
                return selectIncome(userLogin,uiSref,libraryId);
            },
            selectInvoice: function (userLogin,uiSref,libraryId) {
                return selectInvoice(userLogin,uiSref,libraryId);
            },
            addCustomer:function(userLogin,recordId){
                return addCustomer(userLogin,recordId);
            },
            selectNotHaveContract:function(contractLibraryId,userLogin){
                return selectNotHaveContract(contractLibraryId,userLogin);
            },
            selectNotFullIncome:function(contractLibraryIds){
                return selectNotFullIncome(contractLibraryIds);
            },
            selectMainContract:function(contractType){
                return selectMainContract(contractType);
            },
            selectAllByIncome:function(contractLibraryId){
                return selectAllByIncome(contractLibraryId);
            },
            getIncomeMoney:function(incomeId){
                return getIncomeMoney(incomeId);
            },
            changeOpen: function (id, userLogin) {
                return changeOpen(id, userLogin);
            },
            downTempleXls:function(userLogin){
                return downTempleXls(userLogin);
            },
            statisticalData:function(userLogin){
                return statisticalData(userLogin);
            },



        }
    })

    .factory('fiveBusinessContractLibrarySubpackageService', function ($q, $http) {

        var head = "/business/contractLibrarySubpackage";

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
        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectSubpackage = function (userLogin,uiSref,librarySubpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectSubpackage.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref,
                    librarySubpackageId:librarySubpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var listPagedDataSelect = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedDataSelect.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var addCustomer = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addCustomer.json',
                params: {
                    userLogin: userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectNotHaveContract = function (contractLibrarySubpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectNotHaveContract.json',
                params: {
                    contractLibrarySubpackageId:contractLibrarySubpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listSubpackageByUserLogin = function (userLogin,id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listSubpackageByUserLogin.json',
                params: {
                    userLogin: userLogin,
                    subpackageId:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var downTempleXls = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+'/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            listPagedDataSelect: function (params) {
                return listPagedDataSelect(params);
            },

            selectAll: function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            selectSubpackage: function (userLogin,uiSref,librarySubpackageId) {
                return selectSubpackage(userLogin,uiSref,librarySubpackageId);
            },
            addCustomer:function(userLogin,recordId){
                return addCustomer(userLogin,recordId);
            },
            selectNotHaveContract:function(contractLibrarySubpackageId){
                return selectNotHaveContract(contractLibrarySubpackageId);
            },
            listSubpackageByUserLogin: function (userLogin,id) {
                return listSubpackageByUserLogin(userLogin,id);
            },
            downTempleXls: function (userLogin) {
                return downTempleXls(userLogin);
            },

        }
    })

    .factory('fiveBusinessSupplierService', function ($q, $http) {

        var head = "/business/supplier";

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
        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateDetail.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/removeDetail.json',
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

        var listDetailById = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDetailById.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewDetailModel = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewDetailModel.json',
                params: {
                    supplierId:supplierId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDetailModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getDetailModelById.json',
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

        var addCustomer = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addCustomer.json',
                params: {
                    userLogin: userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertContractLibrary = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractLibrary.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectNotHaveContract = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectNotHaveContract.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getContractNo = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getContractNo.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listCooperationProject = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listCooperationProject.json',
                params: {
                    supplierId: supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var checkSupplier = function (name,supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/checkSupplier.json',
                params: {
                    name: name,
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var checkTaxNo=function (taxNo,supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/checkTaxNo.json',
                params: {
                    taxNo: taxNo,
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listUsedNamesById = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listUsedNamesById.json',
                params: {
                    supplierId: supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getUsedNameById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getUsedNameById.json',
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
        var getNewUsedName = function ( supplierId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewUsedName.json',
                params: {
                    userLogin: userLogin,
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var removeUsedName = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeUsedName.json',
                params: {
                    userLogin: userLogin,
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var updateUsedName = function (usedName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateUsedName.json',
                data: usedName
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
            updateDetail:function(item){
                return updateDetail(item);
            },
            removeDetail:function(id){
                return removeDetail(id);
            },
            listDetailById: function (supplierId) {
                return listDetailById(supplierId);
            },
            getNewDetailModel:function (supplierId) {
                return getNewDetailModel(supplierId);
            },
            getDetailModelById:function(id){
                return getDetailModelById(id);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            addCustomer:function(userLogin,recordId){
                return addCustomer(userLogin,recordId);
            },
            insertContractLibrary:function(supplierId){
                return insertContractLibrary(supplierId);
            },
            selectNotHaveContract:function(supplierId){
                return selectNotHaveContract(supplierId);
            },
            getContractNo:function(supplierId){
                return getContractNo(supplierId);
            },
            listCooperationProject: function (supplierId) {
                return listCooperationProject(supplierId);
            },
            checkSupplier: function (name,supplierId) {
                return checkSupplier(name,supplierId);
            },
            checkTaxNo: function (taxNo,customerId) {
                return checkTaxNo(taxNo,customerId);
            },
            listUsedNamesById: function (customerId) {
                return listUsedNamesById(customerId);
            },
            getUsedNameById: function (id) {
                return getUsedNameById(id);
            },
            updateUsedName: function (usedName) {
                return updateUsedName(usedName);
            },
            getNewUsedName: function (customerId,userLogin) {
                return getNewUsedName(customerId,userLogin);
            },
            removeUsedName: function (id,userLogin) {
                return removeUsedName(id,userLogin);
            },
        }
    })

    .factory('fiveBusinessSubpackageService', function ($q, $http) {

        var head = "/business/subpackage";

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

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateDetail.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/removeDetail.json',
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

        var listDetailById = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDetailById.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewDetailModel = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewDetailModel.json',
                params: {
                    supplierId:supplierId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDetailModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getDetailModelById.json',
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

        var addSupplier = function (userLogin,subpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addSupplier.json',
                params: {
                    userLogin: userLogin,
                    subpackageId:subpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertContractLibrary = function (subpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractLibrary.json',
                params: {
                    subpackageId:subpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertContractReview = function (subpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractReview.json',
                params: {
                    subpackageId:subpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectNotHaveContract = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectNotHaveContract.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getSubContractNo = function (subpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getSubContractNo.json',
                params: {
                    subpackageId:subpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var changeOpenStamp = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpenStamp.json',
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

            updateDetail:function(item){
                return updateDetail(item);
            },
            removeDetail:function(id){
                return removeDetail(id);
            },
            listDetailById: function (supplierId) {
                return listDetailById(supplierId);
            },
            getNewDetailModel:function (supplierId) {
                return getNewDetailModel(supplierId);
            },
            getDetailModelById:function(id){
                return getDetailModelById(id);
            },
            changeOpenStamp: function (id, userLogin) {
                return changeOpenStamp(id, userLogin);
            },
            addSupplier:function(userLogin,subpackageId){
                return addSupplier(userLogin,subpackageId);
            },
            insertContractLibrary:function(subpackageId){
                return insertContractLibrary(subpackageId);
            },
            insertContractReview:function(subpackageId){
                return insertContractReview(subpackageId);
            },
            selectNotHaveContract:function(supplierId){
                return selectNotHaveContract(supplierId);
            },
            getSubContractNo:function(subpackageId){
                return getSubContractNo(subpackageId);
            },

        }
    })

    .factory('fiveBusinessPurchaseService', function ($q, $http) {

        var head = "/business/purchase";

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

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateDetail.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/removeDetail.json',
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

        var listDetailById = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDetailById.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewDetailModel = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewDetailModel.json',
                params: {
                    supplierId:supplierId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDetailModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getDetailModelById.json',
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

        var addSupplier = function (userLogin,subpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addSupplier.json',
                params: {
                    userLogin: userLogin,
                    subpackageId:subpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertContractLibrary = function (subpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractLibrary.json',
                params: {
                    subpackageId:subpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertContractReview = function (subpackageId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractReview.json',
                params: {
                    subpackageId:subpackageId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectNotHaveContract = function (supplierId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectNotHaveContract.json',
                params: {
                    supplierId:supplierId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getPurContractNo = function (purchaseId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getPurContractNo.json',
                params: {
                    purchaseId:purchaseId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var changeOpenStamp = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpenStamp.json',
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
            changeOpenStamp: function (id, userLogin) {
                return changeOpenStamp(id, userLogin);
            },
            updateDetail:function(item){
                return updateDetail(item);
            },
            removeDetail:function(id){
                return removeDetail(id);
            },
            listDetailById: function (supplierId) {
                return listDetailById(supplierId);
            },
            getNewDetailModel:function (supplierId) {
                return getNewDetailModel(supplierId);
            },
            getDetailModelById:function(id){
                return getDetailModelById(id);
            },

            addSupplier:function(userLogin,subpackageId){
                return addSupplier(userLogin,subpackageId);
            },
            insertContractLibrary:function(subpackageId){
                return insertContractLibrary(subpackageId);
            },
            insertContractReview:function(subpackageId){
                return insertContractReview(subpackageId);
            },
            selectNotHaveContract:function(supplierId){
                return selectNotHaveContract(supplierId);
            },
            getPurContractNo:function(purchaseId){
                return getPurContractNo(purchaseId);
            },

        }
    })

    .factory('fiveBusinessAdvanceService', function ($q, $http) {

        var head = "/business/advance";

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

        var downTempleXls = function (params) {
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
                params: params
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
        var getNewModelDetail = function (advanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    advanceId:advanceId,
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

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (advanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    advanceId: advanceId,
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
            getNewModelDetail: function (advanceId) {
                return getNewModelDetail(advanceId);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            downTempleXls:function(params){
                return downTempleXls(params);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (advanceId) {
                return listDetail(advanceId);
            },
        }
    })
    
    .factory('fiveBusinessAdvanceCollectService', function ($q, $http) {

        var head = "/business/AdvanceCollect";

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

        var downCollect = function (collectId,collectMonth) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downCollect.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: {
                    collectId:collectId,
                    collectMonth: collectMonth,
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

        var downTempleXls = function (params) {
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
                params: params
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
        var getNewModelDetail = function (AdvanceCollectId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    AdvanceCollectId:AdvanceCollectId,
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

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (AdvanceCollectId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    AdvanceCollectId: AdvanceCollectId,
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
            downCollect: function (collectId,collectMonth) {
                return downCollect(collectId,collectMonth);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            getNewModelDetail: function (AdvanceCollectId) {
                return getNewModelDetail(AdvanceCollectId);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            downTempleXls:function(params){
                return downTempleXls(params);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (AdvanceCollectId) {
                return listDetail(AdvanceCollectId);
            },
        }
    })


    .factory('businessIncomeService', function ($q, $http) {

        var head = "/business/income";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listRecordByUserLogin = function (userLogin,id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listRecordByUserLogin.json',
                params: {
                    userLogin: userLogin,
                    recordId:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var downloadModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

        var getProjectNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getProjectNo.json',
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
        var moneyTurnCapital = function (money) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/moneyTurnCapital.json',
                params: {
                    money: money,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getIncomesByIncomeConfirmId = function (incomeConfirmId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getIncomesByIncomeConfirmId.json',
                params: {
                    incomeConfirmId: incomeConfirmId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getNewModelByConfirm = function (incomeConfirmId,userLogin, uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelByConfirm.json',
                params: {
                    incomeConfirmId: incomeConfirmId,
                    userLogin: userLogin,
                    uiSref: uiSref,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getNewModelByConfirm2 = function (incomeConfirmId,libraryId,userLogin, uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelByConfirm2.json',
                params: {
                    incomeConfirmId: incomeConfirmId,
                    libraryId:libraryId,
                    userLogin: userLogin,
                    uiSref: uiSref,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var downTempleXls = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+'/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

        var getNotarizeIncome = function (incomeId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNotarizeIncome.json',
                params: {
                    incomeId: incomeId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            listRecordByUserLogin: function (userLogin,id) {
                return listRecordByUserLogin(userLogin,id);
            },
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
            },
            getNewModelByConfirm: function (incomeConfirmId,userLogin, uiSref) {
                return getNewModelByConfirm(incomeConfirmId,userLogin, uiSref);
            },
            getNewModelByConfirm2: function (incomeConfirmId,libraryId,userLogin, uiSref) {
                return getNewModelByConfirm2(incomeConfirmId,libraryId,userLogin, uiSref);
            },
            getIncomesByIncomeConfirmId: function (incomeConfirmId) {
                return getIncomesByIncomeConfirmId(incomeConfirmId);
            },
            downTempleXls:function(userLogin){
                return downTempleXls(userLogin);
            },
            getNotarizeIncome: function (incomeId,userLogin) {
                return getNotarizeIncome(incomeId,userLogin);
            },
        }
    })

    .factory('fiveBusinessOutAssistService', function ($q, $http) {

        var head = "/five/business/outAssist";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var updateDetail = function (item,stampTaxId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: item,
                params: {
                    stampTaxId: stampTaxId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downloadModelExcel = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModelExcel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var changeOpenStamp = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpenStamp.json',
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

        var downData = function (userLogin,outAssistId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+'/downData.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: {
                    outAssistId:outAssistId,
                    userLogin:userLogin,
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
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },

            changeOpenStamp: function (id, userLogin) {
                return changeOpenStamp(id, userLogin);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            downData: function (userLogin,outAssistId) {
                return downData(userLogin,outAssistId);
            },

            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            updateDetail: function (item,stampTaxId) {
                return updateDetail(item,stampTaxId);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            getNewModelDetail: function (id,userLogin) {
                return getNewModelDetail(id,userLogin);
            },
            downloadModelExcel: function (params) {
                return downloadModelExcel(params);
            },


        }
    })

    .factory('fiveBusinessCooperationDelegationService', function ($q, $http) {

        var head = "/five/business/cooperationDelegation";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var downloadModelExcel = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModelExcel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var changeOpenStamp = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpenStamp.json',
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

        var downData = function (userLogin,outAssistId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+'/downData.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: {
                    outAssistId:outAssistId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getInteriorProjectNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getInteriorProjectNo.json',
                params: {
                    id: id,
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
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            changeOpenStamp: function (id, userLogin) {
                return changeOpenStamp(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            downData: function (userLogin,outAssistId) {
                return downData(userLogin,outAssistId);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
            },
            downloadModelExcel: function (params) {
                return downloadModelExcel(params);
            },
            getInteriorProjectNo:function (id) {
                return getInteriorProjectNo(id);
            },
        }
    })

    .factory('fiveBusinessCooperationContractService', function ($q, $http) {

        var head = "/five/business/cooperationContract";

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
        var getInteriorContractNo = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getInteriorContractNo.json',
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var downloadModelExcel = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModelExcel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var changeOpenStamp = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpenStamp.json',
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

        var downData = function (userLogin,outAssistId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+'/downData.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: {
                    outAssistId:outAssistId,
                    userLogin:userLogin,
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
            getInteriorContractNo: function (id) {
                return getInteriorContractNo(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            changeOpenStamp: function (id, userLogin) {
                return changeOpenStamp(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            downData: function (userLogin,outAssistId) {
                return downData(userLogin,outAssistId);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
            },
            downloadModelExcel: function (params) {
                return downloadModelExcel(params);
            },
        }
    })

    .factory('fiveBusinessTenderDocumentReviewService', function ($q, $http) {

        var head = "/business/tenderDocumentReview";

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

        var downTempleXls = function (params) {
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
                params: params
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
        var getNewModelDetail = function (tenderId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    tenderId:tenderId,
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

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (tenderId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    tenderId: tenderId,
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
            getNewModelDetail: function (tenderId) {
                return getNewModelDetail(tenderId);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            downTempleXls:function(params){
                return downTempleXls(params);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (tenderId) {
                return listDetail(tenderId);
            },
        }
    })


