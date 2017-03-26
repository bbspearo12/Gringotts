(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .controller('ContractDialogController', ContractDialogController);

    ContractDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Contract', 'Company'];

    function ContractDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Contract, Company) {
        var vm = this;

        vm.contract = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.companies = Company.query({filter: 'contract-is-null'});
        $q.all([vm.contract.$promise, vm.companies.$promise]).then(function() {
            if (!vm.contract.company || !vm.contract.company.id) {
                return $q.reject();
            }
            return Company.get({id : vm.contract.company.id}).$promise;
        }).then(function(company) {
            vm.companies.push(company);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contract.id !== null) {
                Contract.update(vm.contract, onSaveSuccess, onSaveError);
            } else {
                Contract.save(vm.contract, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gringottsApp:contractUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startOfContract = false;
        vm.datePickerOpenStatus.endOfContract = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
