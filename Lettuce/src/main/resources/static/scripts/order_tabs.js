/**
 * 
 */
function changeCurrenciesBasedOnBuySellChosen(rates, portfolio) {
	
	
    var value = document.getElementById("buy-sell-choice").value;
	
	// convert objects back into maps
	var rates = Object.entries(rates).map(([key, value]) => ({key,value}));
	var portfolio = Object.entries(portfolio).map(([key, value]) => ({key,value}));
	
	
	
	if(value == "Buy") {
		
		console.log(rates);
		var str = '';
		for (const rate of rates) {  
			str += '<option value="'+ rate.key +'" />'; // Storing options in variable
		}
		
		var currencySelection = document.getElementById("currency-choices");
		currencySelection.innerHTML = str;
		
		console.log("buy"); 	//show any currency
	} else if (value == "Sell") {
		console.log("sell");	//show currencies from user's portfolio only
	
		
		console.log(portfolio);
		
		/*var str = '';
		for (const currency of portfolio) {  
			str += '<option value="'+ currency +'" />'; // Storing options in variable
		}
		
		var currencySelection = document.getElementById("currency-choices");
		currencySelection.innerHTML = str;*/
		
		
		var str = '';
		
		// Populate list with options:
		for (const currency of portfolio) {  
			str += '<option value="'+ currency.key +'" />'; // Storing options in variable
		}
		
		var currencySelection = document.getElementById("currency-choices");
		currencySelection.innerHTML = str;

	}

 
  
	
}

function chooseOrderType(evt, cityName) {
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



