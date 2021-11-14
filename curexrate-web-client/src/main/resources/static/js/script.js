var app = angular.module('currencyConverterApp', []),
    KEY = "selCurrencies";

//app.controller("mainCtrl", function($scope, $timeout){
//});

app.controller('mainCtrl', function($scope, $http, webService, storageService) {
  $scope.appName = "Exchange Rates";
  $scope.inr = "0.0";
  $scope.euro = "0.0";
  $scope.btnRefreshText = "Refresh";
  $scope.currencies = [
    {symbol : "EUR", isselected : true, value : "0.0"},
    {symbol : "AUD", isselected : true, value : "0.0"},
    {symbol : "GBP", isselected : true, value : "0.0"},
    {symbol : "JPY", isselected : true, value : "0.0"},
    {symbol : "SGD", isselected : true, value : "0.0"},
    {symbol : "PHP", isselected : true, value : "0.0"}
  ];
  $scope.resourceId = "";
  $scope.processingTime = 0;
  
  $scope.refresh = function() {
    $scope.btnRefreshText = "Loading..";
    
    // Data format returned
    // {"base":"USD","date":"2015-01-22","rates":{"AUD":1.2308,"BGN":1.6834,"BRL":2.5783,"CAD":1.2354,"CHF":0.8558,"CNY":6.2097,"CZK":24.014,"DKK":6.4085,"GBP":0.6579,"HKD":7.753,"HRK":6.6264,"HUF":271.61,"IDR":12464.68,"ILS":3.9324,"INR":61.559,"JPY":117.66,"KRW":1083.99,"MXN":14.687,"MYR":3.5821,"NOK":7.6011,"NZD":1.3207,"PHP":44.218,"PLN":3.7009,"RON":3.8753,"RUB":64.366,"SEK":8.1258,"SGD":1.3323,"THB":32.58,"TRY":2.335,"ZAR":11.479,"EUR":0.8607}}
    
    // Get USD - INR Rate
    webService.getRequestRates()
      .then(function(headers) {
        $scope.resourceId = headers('resource-id');
        $scope.processingTime = headers('processing-time');
        // $scope.currencies[0].value = data.rates.INR;
        // $scope.currencies[1].value = data.rates.EUR;
        // $scope.currencies[2].value = data.rates.AUD;
        // $scope.currencies[3].value = data.rates.BGN;
        // $scope.currencies[4].value = data.rates.JPY;
        // $scope.currencies[5].value = data.rates.TRY;
        //$timeout(callAtTimeout, $scope.processingTime);
        //sleep($scope.processingTime * 2);
        //$timeout(callAtTimeout, $scope.processingTime);
        setTimeout(() => {  callAtTimeout(); }, $scope.processingTime);
        // webService.getResponseRates($scope.resourceId)
        //     .then(function(data) {
        //         console.log(data);

        //         $scope.currencies[0].value = data.rates.INR;
        //         $scope.currencies[1].value = data.rates.EUR;
        //         $scope.currencies[2].value = data.rates.AUD;
        //         $scope.currencies[3].value = data.rates.BGN;
        //         $scope.currencies[4].value = data.rates.JPY;
        //         $scope.currencies[5].value = data.rates.TRY;
        //     }
        // );
        // $scope.btnRefreshText = "Refresh";
      },
      function(error) {
        $scope.btnRefreshText = "Refresh";
        console.log("Error getting exchange rates! " + error);
      });
    
  }

  $scope.saveCurrencyChoice = function() {
    storageService.save(KEY, JSON.stringify($scope.currencies));
  }

  function init() {
    var temp = storageService.get(KEY);
    
    if(temp !== null) {
      $scope.currencies = JSON.parse(temp);
    }
  }

  function callAtTimeout() {
    console.log("Timeout occurred");

    webService.getResponseRates($scope.resourceId)
        .then(function(data) {
            console.log(data);

            $scope.currencies[0].value = data.rates.EUR;
            $scope.currencies[1].value = data.rates.AUD;
            $scope.currencies[2].value = data.rates.GBP;
            $scope.currencies[3].value = data.rates.JPY;
            $scope.currencies[4].value = data.rates.SGD;
            $scope.currencies[5].value = data.rates.PHP;

            $scope.btnRefreshText = "Refresh";
        }
    );
  }
  
  init();
  $scope.refresh();
});

// Web service
app.service('webService', function ($http, $q) {
  // Return Public API
  return( {
    getRequestRates : getRequestRates,
    getResponseRates : getResponseRates
  });

  // Public methods
  function getRequestRates() {
    var deferred = $q.defer();
    var request = $http.get('http://localhost:18180/exchangeRate?base=USD');

    return ( request.then(requestRatesHandleSuccess, requestRatesHandleError));
  }

  // Private Methods
  function requestRatesHandleError(response) {
    if(
      !angular.isObject(response.data) ||
      !response.data.message
      ) {
        return ($q.reject("An unknown error occurred."));
      }

    return($q.reject(response.data.message));
  }

  function requestRatesHandleSuccess(response) {
    return(response.headers);
  }

  function getResponseRates(resourceId) {
    var deferred = $q.defer();
    var request = $http.get('http://localhost:18280/exchangeRateResponse?resourceId='+resourceId);

    return ( request.then(responseRatesHandleSuccess, responseRatesHandleError));
  }

  // Private Methods
  function responseRatesHandleError(response) {
      if (response.status == 404) {
        setTimeout(() => {  callAtTimeout(); }, $scope.processingTime);
      }
    if(
      !angular.isObject(response.data) ||
      !response.data.message
      ) {
        return ($q.reject("An unknown error occurred."));
      }

    return($q.reject(response.data.message));
  }

  function responseRatesHandleSuccess(response) {
    return(response.data);
  }

});

// Local storage factory
app.factory('storageService', function ($rootScope) {

    return {
        
        get: function (key) {
           return localStorage.getItem(key);
        },

        save: function (key, data) {
           localStorage.setItem(key, data);
        },

        remove: function (key) {
            localStorage.removeItem(key);
        },
        
        clearAll : function () {
            localStorage.clear();
        }
    };
});