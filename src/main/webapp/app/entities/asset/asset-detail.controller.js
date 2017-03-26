(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .controller('AssetDetailController', AssetDetailController);

    AssetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Asset', 'Contract', 'Provider'];

    function AssetDetailController($scope, $rootScope, $stateParams, previousState, entity, Asset, Contract, Provider) {
        var vm = this;

        vm.asset = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gringottsApp:assetUpdate', function(event, result) {
            vm.asset = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
