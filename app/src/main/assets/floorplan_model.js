function Floorplan(object) {

    var floorplan = {};
    for( var key in object) {
        if(key != 'locationId') {
            floorplan[key] = {
                imageFilepath: object[key].imageFilepath,
                sw: object[key].sw,
                nw: object[key].nw,
                ne: object[key].ne,
                se: object[key].se
            }
        }
    }
    this.floorplan = floorplan;
}

Floorplan.prototype.getFloorplanInfo = function(level) {
    return this.floorplan[level];
};
