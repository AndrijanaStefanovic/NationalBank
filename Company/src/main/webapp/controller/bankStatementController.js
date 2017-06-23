var bankStatementModule = angular.module('bankStatement.controller', []);
 

bankStatementModule.controller('bankStatementController', ['$scope','$location', '$http', '$stateParams',
  	function ($scope, $location, $http, $stateParams) {

	
	$scope.submitBankStatement = function () { 
		
		$http.post('bankStatement/create', $scope.bankStatementRequest)
	    	.then(function mySuccess(response) {
	    		if(response.data == "200"){
		    		toastr.success("Created!");
		    		$window.location.reaload();
		    	}
	    }, function myError(response) {
	    	alert(response.statusText);
	    });
		
	}
	
	angular.element(document).ready(function () {
		$http.get('/bankStatement/getAllBankStatements').then(function(response) {
		   $scope.bankStatements = response.data;
		}, function(response) {
			alert(response.statusText);
	    });
	});
	
	$scope.showBankStatementItems = function(id){
		$location.path("/bankStatementItems/"+id);
	}
	
}]);