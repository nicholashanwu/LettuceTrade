/**
 * 
 */


function openCity(evt, cityName) {
  // Declare all variables
  var i, tabcontent, tablinks;

  // Get all elements with class="tabcontent" and hide them
  tabcontent = document.getElementsByClassName("tabcontent");
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }

  // Get all elements with class="tablinks" and remove the class "active"
  tablinks = document.getElementsByClassName("tablinks");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }

  // Show the current tab, and add an "active" class to the button that opened the tab
  document.getElementById(cityName).style.display = "block";
  evt.currentTarget.className += " active";

  document.getElementById("defaultOpen").click();
}



function checkDate() {
	
	
	
	var today = new Date().toISOString().split('T')[0];
	// var todayTime = new Date().toLocaleTimeString();
	var todayTime = new Date();
	var todayEndOfDay = new Date().setHours(17,00,0); //5:00pm
	var tomorrow = new Date(todayTime);
	tomorrow.setDate(tomorrow.getDate() + 1);
	
	console.log(today);
	console.log(todayTime);
	console.log(tomorrow);
	
	
	/*if it's past 5pm, then force expiry date to be tomorrow or later*/
	// bug: 
	if(todayTime < todayEndOfDay) {
		document.getElementById("expiryDatePicker").setAttribute('min', today);
		document.getElementById("expiryDatePicker").valueAsDate = new Date();
	} else {
		document.getElementById("expiryDatePicker").setAttribute('min', tomorrow);
		document.getElementById("expiryDatePicker").valueAsDate = tomorrow;
	}

	
}

function today() {
	return new Date();
}

function tomorrow() {
	return today().getTime() + 24 * 60 * 60 * 1000;
}

function getFormattedDate(date) {
    return date.getFullYear()
        + "-"
        + ("0" + (date.getMonth() + 1)).slice(-2)
        + "-"
        + ("0" + date.getDate()).slice(-2);
}



