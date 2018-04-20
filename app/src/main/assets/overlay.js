var canvas;

function showFloorplanWithMarkersForLevel(level){
    //TODO: add part to display POIs
    var image = new Image();
    image.src = "lhc3.png";

    // nocache path param to prevent loading cached image
    // getFloorplanInfo(function(result){
        
        // floorplanInfo = result.floorplans;  

        // image.src = floorplanInfo[level].imageFilepath+'?nocache='+(new Date().getTime());
        // var coordinates = {
        //     sw: floorplanInfo[level].sw,
        //     se: floorplanInfo[level].se,
        //     nw: floorplanInfo[level].nw,
        //     ne: floorplanInfo[level].ne
        // };

        
        var coordinates = {
            nw: {lat: 13.03166944394184, lng: 77.56451206233078 },
            ne: {lat: 13.031627633854, lng: 77.5650753262239 },
            sw: {lat: 13.03117784912634, lng: 77.56445607121759 },
            se: {lat: 13.0311360390385 , lng: 77.56501933512023}
        }
        var bearingY = LatLon(coordinates.nw.lat, coordinates.nw.lng).bearingTo(LatLon(coordinates.sw.lat, coordinates.sw.lng));
        var bearingX = LatLon(coordinates.nw.lat, coordinates.nw.lng).bearingTo(LatLon(coordinates.ne.lat, coordinates.ne.lng));

        google.maps.event.clearListeners(image, 'load');
        google.maps.event.addDomListener(image,'load',function(){
            console.log("Image load listener called");
            canvas = new FPOverlay( 
                image, 
                map,
                {x: bearingX, y: bearingY},
                {sw: coordinates.sw, nw: coordinates.nw, ne: coordinates.ne, se: coordinates.se}
            );
        });
    // });
}


function clearFloorPlan(){
    if(canvas){
        canvas.setMap(null);
        canvas = null;
    }
}
