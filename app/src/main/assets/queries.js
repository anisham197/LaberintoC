function getFloorplans(callback) {
    var db = firebase.firestore();
    var locationId = Android.getLocationId();
//    var locationId = 'WwcCPsNYRnqXhe4WiL8q';

    db.collection("floorplans").where("locationId", "==", locationId).get()
        .then(querySnapshot => {
            if(querySnapshot.size > 0) {
                querySnapshot.forEach(doc => {
                    floorplans[doc.id] = new Floorplan(doc.data());
                    showFloorplanForLevel(floorplans[doc.id], pickerLevel);
                });
                // showFloorplanForLevel(floorplans, pickerLevel)
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
    // var userId = 'hg8Rcv2fQkfGZPoz9kFOpnfVpJx2';

    var currentLocationListener = db.collection('customers').doc(userId)
        .onSnapshot(function(doc) {
            if (doc.exists) {
                console.log("Document data:", doc.data());
                var currentLocation = doc.data().currentLocation;
                if (currentLocation != null){
                    if( currentFloor != currentLocation.floorNum) {
                        currentFloor = currentLocation.floorNum;
                        for ( key in floorplans) {
                            showFloorplanForLevel(floorplans[key], currentFloor);
                        }                        
                    }
                   currentPosition = {lat: currentLocation.lat, lng: currentLocation.lng };
                   currentLabel = currentLocation.roomLabel;
                   setMarker();                
                }
                else {
                    currentFloor = 2;
                    for ( key in floorplans) {
                        showFloorplanForLevel(floorplans[key], currentFloor);
                    } 
                    marker.setVisible(false);
                }
            } else {
                console.log("No such document!");
            }
        }, function(error) {
            console.log("Error getting document:", error);
        });
}