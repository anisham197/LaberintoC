function getFloorplans(callback) {
    console.log('Get Floorplans called');
    var db = firebase.firestore();
    // var locationId = Android.getLocationId();
    var locationId = 'WwcCPsNYRnqXhe4WiL8q';

    db.collection("floorplans").where("locationId", "==", locationId).get()
        .then(querySnapshot => {
            if(querySnapshot.size > 0) {
                querySnapshot.forEach(doc => {
                    console.log(doc.id);
                    floorplans[doc.id] = new Floorplan(doc.data());
                });
                console.log(floorplans);
                callback(true);
            } else {
                console.log("No such document");
                callback(false);
            }
        })
        .catch(err => {
            console.log('Error getting documents', err);
            callback(false);
        });
}

function getNumberOfFloors(callback) {
    console.log('Get Number of Floors called');
    var db = firebase.firestore();
    // var locationId = Android.getLocationId();
    var locationId = 'WwcCPsNYRnqXhe4WiL8q';

    db.collection("buildings").where("locationId", "==", locationId).get()
        .then(querySnapshot => {
            if(querySnapshot.size > 0) {
                querySnapshot.forEach(doc => {
                    console.log(doc.id);
                    console.log(doc.data().metadata.numFloors);
                    numberOfFloors[doc.id] = doc.data().metadata.numFloors;
                });
                callback(true);
            } else {
                console.log("No such document");
                callback(false);
            }
        })
        .catch(err => {
            console.log('Error getting documents', err);
            callback(false);
        });
}