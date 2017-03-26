(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company', {
            parent: 'entity',
            url: '/company',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Companies'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company/companies.html',
                    controller: 'CompanyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('company-detail', {
            parent: 'company',
            url: '/company/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Company'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company/company-detail.html',
                    controller: 'CompanyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Company', function($stateParams, Company) {
                    return Company.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-detail.edit', {
            parent: 'company-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-dialog.html',
                    controller: 'CompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company.new', {
            parent: 'company',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-dialog.html',
                    controller: 'CompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                addressLine1: null,
                                addressLine2: null,
                                city: null,
                                state: null,
                                zip: null,
                                primaryContact: null,
                                phoneNumber: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: 'company' });
                }, function() {
                    $state.go('company');
                });
            }]
        })
        .state('company.edit', {
            parent: 'company',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-dialog.html',
                    controller: 'CompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: 'company' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company.delete', {
            parent: 'company',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company/company-delete-dialog.html',
                    controller: 'CompanyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: 'company' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
