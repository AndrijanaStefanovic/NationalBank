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
		   
		   for(i=0; i<$scope.bankStatements.length; i++){
	   			   var date = new Date($scope.bankStatements[i].date);
	   			   var month = date.getMonth() + 1;
	   			   var day = date.getDate();
	   			   var year = date.getFullYear();
	   			   var shortStartDate = day + "/" + month + "/" + year;
	   			   $scope.bankStatements[i].date = shortStartDate;
			   }
		   
		}, function(response) {
			alert(response.statusText);
	    });
	});
	
	$scope.showBankStatementItems = function(id){
		$location.path("/bankStatementItems/"+id);
	}
	
}]);