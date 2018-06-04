function showFloorplanForLevel(floorplan, level){
    clearFloorPlan();
    pickerSelectUI(level);

    var floorplanInfo = floorplan.getFloorplanInfo(level);
    console.log("Floorplan Info");
    console.log(floorplanInfo);
    if(floorplanInfo == null) {
        return;
    }

    var image = new Image();
    image.src = floorplanInfo.imageFilepath+'?nocache='+(new Date().getTime());

    var coordinates = {
        nw: floorplanInfo.nw,
        ne: floorplanInfo.ne,
        sw: floorplanInfo.sw,
        se: floorplanInfo.se
    }
    var bearingY = LatLon(coordinates.nw.lat, coordinates.nw.lng).bearingTo(LatLon(coordinates.sw.lat, coordinates.sw.lng));
    var bearingX = LatLon(coordinates.nw.lat, coordinates.nw.lng).bearingTo(LatLon(coordinates.ne.lat, coordinates.ne.lng));

    google.maps.event.clearListeners(image, 'load');
    google.maps.event.addDomListener(image,'load',function(){
        console.log("Image load listener called");
        var canvas = new FPOverlay(
            image,
            map,
            {x: bearingX, y: bearingY},
            {sw: coordinates.sw, nw: coordinates.nw, ne: coordinates.ne, se: coordinates.se}
        );
        canvasArray.push(canvas);
        console.log(canvas);
    });       

}

function clearFloorPlan(){
    for(var i = 0; i < canvasArray.length; i++) {
        var canvas = canvasArray[i];
        if(canvas) {
            canvas.setMap(null);
            canvas = null;
        }
    }
    canvasArray = [];
}