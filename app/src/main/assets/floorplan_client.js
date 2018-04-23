var map; 
var buttons = [];
var numFloors = 5;

function initMap() {
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
	displayLevelPicker(); 
	showFloorplanWithMarkersForLevel(1);
	try_firestore();
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
	for(var i = 1; i <= numFloors; i++){
		console.log("Button " + i);
		buttons.push(document.createElement("button"));
		buttons[i].setAttribute('id', i);
		buttons[i].style.display = 'block';
		buttons[i].innerHTML = i;        
		buttons[i].addEventListener('click', function(event){
			console.log("Floor clicked " + event.target.id);
			var level = event.target.id;
			showFloorplanWithMarkersForLevel(level);
		});
	}
	for(var i = numFloors; i >= 1; i--){
		console.log("Button " + i);
		div.appendChild(buttons[i]);
	}
}

function pickerSelectUI(level){
	console.log(level);
	for(var i = 1; i <= numFloors; i++) {
		if( i == level){
			buttons[i].style['background-color'] = '#4CAF50';
		}
		else {
			buttons[i].style['background-color'] = "buttonface";
		}
	}   
}

google.maps.event.addDomListener(window, 'load', initMap);