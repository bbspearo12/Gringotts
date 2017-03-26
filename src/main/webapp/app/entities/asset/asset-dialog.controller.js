(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .controller('AssetDialogController', AssetDialogController);

    AssetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Asset', 'Contract', 'Provider'];

    function AssetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Asset, Contract, Provider) {
        var vm = this;

        vm.asset = entity;
        vm.clear = clear;
        vm.save = save;
        vm.contracts = Contract.query({filter: 'asset-is-null'});
        $q.all([vm.asset.$promise, vm.contracts.$promise]).then(function() {
            if (!vm.asset.contract || !vm.asset.contract.id) {
                return $q.reject();
            }
            return Contract.get({id : vm.asset.contract.id}).$promise;
        }).then(function(contract) {
            vm.contracts.push(contract);
        });
        vm.providers = Provider.query({filter: 'asset-is-null'});
        $q.all([vm.asset.$promise, vm.providers.$promise]).then(function() {
            if (!vm.asset.provider || !vm.asset.provider.id) {
                return $q.reject();
            }
            return Provider.get({id : vm.asset.provider.id}).$promise;
        }).then(function(provider) {
            vm.providers.push(provider);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.asset.id !== null) {
                Asset.update(vm.asset, onSaveSuccess, onSaveError);
            } else {
                Asset.save(vm.asset, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gringottsApp:assetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
