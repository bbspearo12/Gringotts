(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('upload', {
            parent: 'app',
            url: '/upload',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/upload/upload.html',
                    controller: 'UploadController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();