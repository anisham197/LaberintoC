function getFloorplans(callback) {
    var db = firebase.firestore();
    var locationId = Android.getLocationId();
    // var locationId = 'WwcCPsNYRnqXhe4WiL8q';

    db.collection("floorplans").where("locationId", "==", locationId).get()
        .then(querySnapshot => {
            if(querySnapshot.size > 0) {
                querySnapshot.forEach(doc => {
                    floorplans[doc.id] = new Floorplan(doc.data());
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

function getNumberOfFloors(callback) {
    var db = firebase.firestore();
    var locationId = Android.getLocationId();
    // var locationId = 'WwcCPsNYRnqXhe4WiL8q';

    db.collection("buildings").where("locationId", "==", locationId).get()
        .then(querySnapshot => {
            if(querySnapshot.size > 0) {
                querySnapshot.forEach(doc => {
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


function getCurrentLocation() {
    var db = firebase.firestore();
    var userId = Android.getUserId();
//     var userId = '3T3LV2w65RUUQl24MsHBrhu2t7G2';

    var currentLocationListener = db.collection('users').doc(userId)
        .onSnapshot(function(doc) {
            if (doc.exists) {
                console.log("Document data:", doc.data());
                var currentLocation = doc.data().currentLocation;
                var position = {lat: currentLocation.lat, lng: currentLocation.lng };
                console.log(position);
                var label = currentLocation.roomLabel;
                console.log(label);
                marker.setPosition(position);
                marker.setTitle(label);
                marker.setVisible(true);
            } else {
                console.log("No such document!");
            }
        }, function(error) {
            console.log("Error getting document:", error);
        });
}