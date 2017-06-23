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
				
				for(i=0; i<$scope.bankStatementItems.length; i++){
	 	   			   var date = new Date($scope.bankStatementItems[i].dateOfAccount);
	 	   			   var month = date.getMonth() + 1;
	 	   			   var day = date.getDate();
	 	   			   var year = date.getFullYear();
	 	   			   var shortStartDate = day + "/" + month + "/" + year;
	 	   			   $scope.bankStatementItems[i].dateOfAccount = shortStartDate;
	 	   			   
	 	   			   var date2 = new Date($scope.bankStatementItems[i].dateOfValue);
	 	   			   var month2 = date2.getMonth() + 1;
	 	   			   var day2 = date2.getDate();
	 	   			   var year2 = date2.getFullYear();
	 	   			   var shortStartDate2 = day2 + "/" + month2 + "/" + year2;
	 	   			   $scope.bankStatementItems[i].dateOfValue = shortStartDate2;
	 			   }
				
		}, function(response) {
			alert(response.statusText);
	    });
	});
	
}]);