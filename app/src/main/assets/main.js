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
var canvasArray = [];
var levels; // number of levels for current building in focus
var marker;
var infowindow;
var pickerLevel = 2;
var currentPosition;
var currentLabel;
var currentFloor = 0;

function initMap() {
	var latitude = parseFloat(Android.getLatitude());
	var longitude = parseFloat(Android.getLongitude());
	var location = {lat: latitude , lng: longitude };
	// var location = {lat: 13.0304619 , lng: 77.56468619999998 };

	map = new google.maps.Map(document.getElementById('map'), {
		zoom: 18,
		center: location,
		clickableIcons: false,
		zoomControl: false,
		mapTypeControl: false,
		scaleControl: true,
		streetViewControl: false,
		rotateControl: true,
		fullscreenControl: true
	});

	google.maps.event.addListener(map, 'tilesloaded', function() {

		marker = new google.maps.Marker({
			map: map,
			position: {lat: 0, lng: 0},
			title: "Empty",
			visible: false,
			clickable: true,
			icon: {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 5,
                fillColor: '#4285f4',
                fillOpacity: 1,
                strokeColor: '#4285f4',
                strokeOpacity: 1
            }
		});

		infowindow = new google.maps.InfoWindow();
		google.maps.event.addListener(marker , 'click', function(){
			infowindow.open(map); 
		});
	
		getNumberOfFloors(function(result){
			if( result == true) {
				// levels = numberOfFloors[buildingId];
				levels = 5;
				displayLevelPicker();
			}
			else {
				console.log("Unable to retrieve floor numbers");
			}
		});
		
		getFloorplans(function(result){
			if(result == true){
				//implement listener to keep track of current location
				getCurrentLocation();
			}
			else {
				console.log("Unable to retrieve floorplans");
			}
		});

		//clear the listener, we only need it once
		google.maps.event.clearListeners(map, 'tilesloaded');
	 });
}

function setMarker() {
	marker.setPosition(currentPosition);
	marker.setTitle(currentLabel);
	marker.setVisible(true);
	infowindow.setPosition(currentPosition);
	infowindow.setContent(currentLabel);
}

function displayLevelPicker() {
	var picker = document.createElement('level_picker');
	picker.style['padding-left'] = '20px';
	picker.style['padding-bottom'] = '10px';

	var levelPickerControl = new LevelPickerControl(picker);
	picker.index = 1;
	map.controls[google.maps.ControlPosition.LEFT_BOTTOM].push(picker);
}


function LevelPickerControl(div) {
	buttons.push(0);
	for(var i = 1; i <= levels; i++){
		buttons.push(document.createElement("button"));
		buttons[i].setAttribute('id', i);
		buttons[i].innerHTML = i; 
		setButtonUI(buttons[i]);       
		buttons[i].addEventListener('click', function(event){
			console.log("Floor clicked " + event.target.id);
			pickerLevel = event.target.id;
			for ( key in floorplans) {
				showFloorplanForLevel(floorplans[key], pickerLevel);
			} 

			if(currentFloor != pickerLevel){
				marker.setVisible(false);
			}
			if(currentFloor == pickerLevel){
				setMarker();
			}
		});
	}
	for(var i = levels; i >= 1; i--){
		div.appendChild(buttons[i]);
	}
}

function setButtonUI(button){	
	button.style.display = 'block';
	button.style.backgroundColor =  '#009688';
	button.style.color = 'ffffff';
	button.style.border = '1px solid #00796B';
}

function pickerSelectUI(level){
	for(var i = 1; i <= levels; i++) {
		if( i == level){
			buttons[i].style.backgroundColor = '#ffffff';
			buttons[i].style.color = '#009688';
		}
		else {
			buttons[i].style.backgroundColor = '#009688';
			buttons[i].style.color = '#ffffff';
		}
	}   
}

google.maps.event.addDomListener(window, 'load', initMap);