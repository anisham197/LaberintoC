var map; 
// var coordinates = buildingCoordinates;
// var floorplanInfo = null;

// $(document).ready(function(){
//   // // ajax call
//   // getFloorplanInfo(function(result){
//   //   floorplanInfo = result.floorplans;
//   //   console.log(floorplanInfo);
//   //   console.log(result.msg);
//   //   console.log(result);
//   // });
// });


function initMap() {
  var location = {lat: 13.030860, lng: 77.565230 };
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 20,
    center: location,
    clickableIcons: false,
    zoomControl: true,
    mapTypeControl: false,
    scaleControl: true,
    streetViewControl: false,
    rotateControl: true,
    fullscreenControl: true
  });
  showFloorplanWithMarkersForLevel(1);
}

// function getFloorplanInfo(callback) {
//   jQuery.ajax({
//       url: '/addfloorplan/getFloorplanInfo?id=' + buildingEncryptId,
//       cache: false,
//       method: 'GET',
//       type: 'GET', // For jQuery < 1.9
//       success: function(data){
//         callback(data);
//       },
//       error : function(error) {
//         console.log(error);
//       }
//   });
// }

google.maps.event.addDomListener(window, 'load', initMap);