var paymentOrderModule = angular.module('paymentOrder.controller', []);

paymentOrderModule.controller('paymentOrderController', ['$scope','$location', '$http', '$stateParams',
    function ($scope, $location, $http, $stateParams) {

        angular.element(document).ready(function () {

            $http.get('/paymentOrder/getAllPaymentOrders').then(function(response) {
                $scope.paymentOrders = response.data;
            }, function(response) {
                alert(response.statusText);
            });

        });

    }]);