var invoiceXMLModule = angular.module('getXML.controller', []);

invoiceXMLModule.controller('getXMLController', ['$scope','$window', '$location', '$http', '$stateParams',
  	function ($scope, $window, $location, $http, $stateParams) {
	
	angular.element(document).ready(function () {
			$http.get('/invoice/getXML').then(function(response) {
				   $scope.messageId = response.data[0];
				   $scope.supplierName = response.data[1];
				   $scope.supplierAddress = response.data[2];
				   $scope.supplierPIB = response.data[3];
				   $scope.buyerName = response.data[4];
				   $scope.buyerAddress = response.data[5];
				   $scope.buyerPIB = response.data[6];
				   $scope.accountNumber = response.data[7];
				   $scope.dateOfInvoice = response.data[8];
				   $scope.merchandiseValue = response.data[9];
				   $scope.servicesValue = response.data[10];
				   $scope.totalValue = response.data[11];
				   $scope.totalDiscount = response.data[12];
				   $scope.totalTax = response.data[13];
				   $scope.currency = response.data[14];
				   $scope.totalDue = response.data[15];
				   $scope.billingAccountNumber = response.data[16];
				   $scope.dateOfValue = response.data[17];
				   $scope.number = response.data[18];
				   $scope.name = response.data[19];
				   $scope.amount = response.data[20];
				   $scope.measurmentUnit = response.data[21];
				   $scope.unitPrice = response.data[22];
				   $scope.value = response.data[23];
				   $scope.discountPercent = response.data[24];
				   $scope.totalDiscount =response.data[25];
				   $scope.subtractedDiscount = response.data[26];
				   $scope.totalTax = response.data[27];
				}, function(response) {
					alert(response.statusText);
				});
	});
	
}]);