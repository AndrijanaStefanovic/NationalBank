var bankStatementItemModule = angular.module('bankStatementItem.controller', []);
 

bankStatementModule.controller('bankStatementItemController', ['$scope','$location', '$http', '$stateParams',
  	function ($scope, $location, $http, $stateParams) {

	
	$scope.submitBankStatementItem = function () { 
		
		$http.post('bankStatementItems/create/', $scope.bankStatementItem)
	    	.then(function mySuccess(response) {
	    		if(response.data == "200"){
		    		toastr.success("Created!");
		    	}
	    }, function myError(response) {
	    	alert(response.statusText);
	    });
		
	}
	
	angular.element(document).ready(function () {
		$http.get('/bankStatementItems/getAllBankStatementItems/'+ $stateParams.id)
			.then(function(response) {
				$scope.bankStatementItems = response.data;
		}, function(response) {
			alert(response.statusText);
	    });
	});
	
}]);