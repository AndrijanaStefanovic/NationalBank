var companyModule = angular.module('company.controller', []);

companyModule.controller('companyController', ['$scope', '$window', '$location', '$http', '$stateParams',
	function ($scope, $window, $location, $http, $stateParams) {

	angular.element(document).ready(function() {
		$http.get('/company/getAllCompanies').then(function(response) {
			$scope.companies = response.data;
		}, function(response) {
			alert(response.statusText);
		});
	});	

	$scope.submitCompany = function () {
		$http.post('company/create', $scope.company).then(function mySuccess(response) {
			if(response.data == "200") {
				toastr.success("Created!");
			}
			$window.location.reload();
		}, function myError(response) {
			alert(response.statusText);
		});
	}

	$scope.showCompanies = function(id) {
		$location.path("/companies/"+id);
	}

	$scope.deleteCompany = function(id) {
		$http.post('company/delete', id).then(function mySuccess(response) {
			if(response.data == "200"){
				toastr.success("Company deleted!");
			}
			$window.location.reload();
		}, function myError(response) {
			alert(response.statusText);
		});
	}
}]);
