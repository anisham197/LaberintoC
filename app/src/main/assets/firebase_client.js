var floorplans = {};

function getFloorplans() {
    var db = firebase.firestore();

    var locationID = Android.getLocationId();
//    var locationID = 'WwcCPsNYRnqXhe4WiL8q';

    var docRef = db.collection("floorplans");
        var query = docRef.where("locationId", "==", locationID).get()
            .then(querySnapshot => {
                if(querySnapshot.size > 0) {
                    querySnapshot.forEach(doc => {
                        console.log(doc.id);
                        floorplans[doc.id] = new Floorplan(doc.data());
                        showFloorplanWithMarkersForLevel(floorplans[doc.id], 1);
                        console.log(floorplans[doc.id].getFloorplanInfo(1));
                    });
                    console.log(floorplans);

                } else {
                    console.log("No such document");
                }
            })
            .catch(err => {
                console.log('Error getting documents', err);
            });
}