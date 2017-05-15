
(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .controller('UploadController', UploadController);

    UploadController.$inject = ['$scope', '$state', '$parse', '$http'];

    function UploadController ($scope, $state, $parse, $http) {
        var vm = this;
        $scope.csv = {
            	content: null,
            	header: true,
            	headerVisible: true,
            	separator: ',',
            	separatorVisible: true,
            	result: null,
            	encoding: 'ISO-8859-1',
            	encodingVisible: false,
                uploadButtonLabel: "upload a csv file"
            };
			console.log('foo');

            var _lastGoodResult = '';
            $scope.toPrettyJSON = function (json, tabWidth) {
            		//console.log('called toPretty');
        			var objStr = JSON.stringify(json);
        			//console.log(objStr.indexOf('Ê'));
        			objStr = objStr.replace('/Ê/g', '');
        			//console.log(objStr.indexOf('Ê'));
        			//console.log(objStr);
        			var obj = null;
        			try {
        				obj = $parse(objStr)({});
        			} catch(e){
        				// eat $parse error
        				return _lastGoodResult;
        			}
        			var result = JSON.stringify(obj, null, Number(tabWidth));
        			_lastGoodResult = result;
        			//console.log("returning ");
        			//console.log(result);
        			return result;
            };
           
            $scope.hello = function () {
            	return 'hello from controller';
            }
            $scope.bulkUpload = function(json) {
            	console.log("posting to bulkupload");
                $http.post('/api/bulk/upload', JSON.stringify(json)).success(function(data) {
                	//console.log(data);
                	$scope.returned_data=JSON.stringify(data);
                    return data;
                }).error(function(err, status) {
                	console.log(err);
                	console.log(status);

                	$scope.returned_data='Failed to upload csv, Please validate input. Error from server: '+JSON.stringify(err);
                });
            };
        }
})();