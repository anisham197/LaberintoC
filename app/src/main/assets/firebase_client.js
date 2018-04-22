function try_firestore() {
    var db = firebase.firestore();

    var locationID = Android.getLocationId();
//    var locationID = 'WwcCPsNYRnqXhe4WiL8q';
    var floorplans = {};

    var docRef = db.collection("floorplans");
    var query = docRef.where("locationId", "==", locationID).get()
        .then(querySnapshot => {
            if(querySnapshot.size > 0) {
                querySnapshot.forEach(doc => {
                    console.log(doc.id);
                    // Get floorplan details for each building and overlay image on map
                });
            } else {
                console.log("No such document");
            }

        })
        .catch(err => {
            console.log('Error getting documents', err);
        });
}