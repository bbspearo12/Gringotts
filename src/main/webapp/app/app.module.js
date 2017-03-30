(function() {
    'use strict';

    angular
        .module('gringottsApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'ngCsvImport',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'hljs'
            
        ])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
