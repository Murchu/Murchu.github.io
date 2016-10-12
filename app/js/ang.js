
/**
 * Main AngularJS Web Application
 */
var app = angular.module('tutorialWebApp', [
  'ngRoute'
]);



/**
 * Configure the Routes
 */
app.config(['$routeProvider', function ($routeProvider) {
  $routeProvider
    // Home
    .when("/", {templateUrl: "dist/partials/home.html", controller: "PageCtrl"})
    // Pages
    .when("/portfolio", {templateUrl: "dist/partials/portfolio.html", controller: "PageCtrl"})
    .when("/projects", {templateUrl: "dist/partials/projects.html", controller: "PageCtrl"})
    // else 404
    .otherwise("/404", {templateUrl: "dist/partials/404.html", controller: "PageCtrl"});
}]);



/**
 * Portfolio Controller
 */
var app = angular.module('portfolioCtrl', [
  'ngRoute'
]);

/**
 * Configure the Routes
 */
app.config(['$routeProvider', function ($routeProvider) {
  $routeProvider
    // Home
    .when("/", {templateUrl: "dist/partials/home.html", controller: "PageCtrl"})
    // Pages
    .when("/portfolio", {templateUrl: "dist/partials/portfolio.html", controller: "PageCtrl"})
    // else 404
    .otherwise("/404", {templateUrl: "dist/partials/404.html", controller: "PageCtrl"});
}]);



app.controller('PageCtrl', function (/* $scope, $location, $http */) {
  console.log("Ready");

  // Activates the Carousel
  $('.carousel').carousel({
    interval: 5000
  });

  // Activates Tooltips for Social Links
  $('.tooltip-social').tooltip({
    selector: "a[data-toggle=tooltip]"
  })
});