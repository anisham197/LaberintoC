var config = {
    apiKey: "AIzaSyB4IYLbkUq1KOhMYVus1WoPy4wxgmeHm0A",
    authDomain: "datastore-9fd58.firebaseapp.com",
    projectId: "datastore-9fd58" 
};
firebase.initializeApp(config);

var map; 
var buttons = [];
var floorplans = {};
var numberOfFloors = {};
var canvasArray = {};
var levels; // number of levels for current building in focus
var buildingId = 'iB19c3KlJEgrSKHmnyDK';


function initMap() {
	//TODO: retrieve location from Android
	var location = {lat: 13.030860, lng: 77.565230 };

	map = new google.maps.Map(document.getElementById('map'), {
		zoom: 20,
		center: location,
		clickableIcons: false,
		zoomControl: false,
		mapTypeControl: false,
		scaleControl: true,
		streetViewControl: false,
		rotateControl: true,
		fullscreenControl: true
	});
    getNumberOfFloors(function(result){
        if( result == true) {
            levels = numberOfFloors[buildingId];
            console.log("calling display Pickers");
            displayLevelPicker();
        }
        else {
            console.log("Unable to retrieve floor numbers");
        }
    });
	getFloorplans(function(result){
		if(result == true){
			for(var key in floorplans){
                console.log("Calling show floorplan with marker for level");
                showFloorplanWithMarkersForLevel(key, floorplans[key], 1);
			}
		}
		else {
			console.log("Unable to retrieve floorplans");
		}
	});

	//TODO: implement listener to keep track of current location
}


function displayLevelPicker() {
	console.log("Level Picker called");
	var picker = document.createElement('level_picker');
	picker.style['padding-right'] = '40px';
	picker.style['padding-top'] = '20px';
	picker.style['padding-bottom'] = '20px';

	var levelPickerControl = new LevelPickerControl(picker);
	picker.index = 1;
	map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(picker);
}


function LevelPickerControl(div) {
	buttons.push(0);
	for(var i = 1; i <= levels; i++){
		buttons.push(document.createElement("button"));
		buttons[i].setAttribute('id', i);
		buttons[i].style.display = 'block';
		buttons[i].innerHTML = i;        
		buttons[i].addEventListener('click', function(event){
			console.log("Floor clicked " + event.target.id);
			var level = event.target.id;
			// TODO: make it specific to a building based on zoom level
            showFloorplanWithMarkersForLevel(buildingId, floorplans[buildingId], level);
		});
	}
	for(var i = levels; i >= 1; i--){
		div.appendChild(buttons[i]);
	}
}

function pickerSelectUI(level){
	console.log(level);
	for(var i = 1; i <= levels; i++) {
		if( i == level){
			buttons[i].style['background-color'] = '#4CAF50';
		}
		else {
			buttons[i].style['background-color'] = "buttonface";
		}
	}   
}

google.maps.event.addDomListener(window, 'load', initMap);