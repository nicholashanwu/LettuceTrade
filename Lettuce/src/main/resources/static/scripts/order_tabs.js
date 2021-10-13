/**
 * 
 */

function openSpotTabByDefault() {
	document.getElementsByClassName('tablinks')[0].click();
	checkDate();
}


/*updates the value in the label to reflect changes in currencies selected*/
function changeLabelBasedOnFirstCurrency(rates, portfolio) {
	
	//Converts the rates and portfolio Objects to arrays
	var rates = Object.entries(rates).map(([key, value]) => ({key,value}));				
	var portfolio = Object.entries(portfolio).map(([key, value]) => ({key,value}));
	var value = document.getElementById("buy-sell-choice").value;
	
	var label = document.getElementById("second-currency-quantity-label");
	var first_currency_picker = document.getElementById("first-currency-choice").value;
	var first_currency_quantity = document.getElementById("first-currency").value;
	
	var second_currency_picker = document.getElementById("second-currency-choice").value;

	let rate_one = rates.find(o => o.key === first_currency_picker);		//grabs both key and value pair
	
	
	let rate_two = rates.find(o => o.key === second_currency_picker);
	let inverse_rate_two = 1*1.0/rate_two.value;
	
	var result_rate = rate_one.value * inverse_rate_two;


	label.innerText = Math.round(first_currency_quantity / result_rate * 1000) / 1000;
	
}

function fwdChangeLabelBasedOnFirstCurrency(rates, portfolio) {
	
	//Converts the rates and portfolio Objects to arrays
	var rates = Object.entries(rates).map(([key, value]) => ({key,value}));				
	var portfolio = Object.entries(portfolio).map(([key, value]) => ({key,value}));
	var value = document.getElementById("fwd-buy-sell-choice").value;
	
	var label = document.getElementById("fwd-second-currency-quantity-label");
	var first_currency_picker = document.getElementById("fwd-first-currency-choice").value;
	var first_currency_quantity = document.getElementById("fwd-first-currency").value;
	
	var second_currency_picker = document.getElementById("fwd-second-currency-choice").value;
	
	
	let rate_one = rates.find(o => o.key === first_currency_picker);		//grabs both key and value pair
	
	let rate_two = rates.find(o => o.key === second_currency_picker);
	let inverse_rate_two = 1*1.0/rate_two.value;

	var result_rate = rate_one.value * inverse_rate_two;

	label.innerText = Math.round(first_currency_quantity / result_rate * 1000) / 1000;
	
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
    tablinks[i].className = tablinks[i].className.replace("active", "");
  }

  // Show the current tab, and add an "active" class to the button that opened the tab
  document.getElementById(cityName).style.display = "block";
  evt.currentTarget.className += " active";

/*  document.getElementById("defaultOpen").click();*/
}






function changeCurrenciesBasedOnBuySellChosen(rates, portfolio) {
	
    var value = document.getElementById("buy-sell-choice").value;
	var for_using = document.getElementById("for-using-label");
	
	// convert objects back into maps
	var rates = Object.entries(rates).map(([key, value]) => ({key,value}));
	var portfolio = Object.entries(portfolio).map(([key, value]) => ({key,value}));
	
	if(value == "Buy") {					//show all available currencies

		console.log("buy"); 
		
		for_using.innerHTML = "using";

		var first = document.getElementById("first-currency-choice"); 
		var second = document.getElementById("second-currency-choice");
		var temp = second.innerHTML;
		second.innerHTML = first.innerHTML;
		first.innerHTML = temp;
		
	} else if (value == "Sell") {			//show currencies from user's portfolio only
		
		console.log("sell");		
		
		for_using.innerHTML = "for";
		
		var first = document.getElementById("first-currency-choice"); 
		var second = document.getElementById("second-currency-choice");
		var temp = first.innerHTML;
		first.innerHTML = second.innerHTML;
		second.innerHTML = temp;
	}
}

function fwdChangeCurrenciesBasedOnBuySellChosen(rates, portfolio) {
	
    var value = document.getElementById("fwd-buy-sell-choice").value;
	var for_using = document.getElementById("fwd-for-using-label");
	
	// convert objects back into maps
	var rates = Object.entries(rates).map(([key, value]) => ({key,value}));
	var portfolio = Object.entries(portfolio).map(([key, value]) => ({key,value}));
	
	if(value == "Buy") {					//show all available currencies

		console.log("buy"); 
		
		for_using.innerHTML = "using";

		var first = document.getElementById("fwd-first-currency-choice"); 
		var second = document.getElementById("fwd-second-currency-choice");
		var temp = second.innerHTML;
		second.innerHTML = first.innerHTML;
		first.innerHTML = temp;
		
	} else if (value == "Sell") {			//show currencies from user's portfolio only
		
		console.log("sell");		
		
		for_using.innerHTML = "for";
		
		var first = document.getElementById("fwd-first-currency-choice"); 
		var second = document.getElementById("fwd-second-currency-choice");
		var temp = first.innerHTML;
		first.innerHTML = second.innerHTML;
		second.innerHTML = temp;
	}
}



function checkDate() {

	var datePicker = document.getElementById("expiryDatePicker");

	var today = new Date();
	var dd = today.getDate();
	var ddT = today.getDate() + 1;	//tomorrow
	var mm = today.getMonth() + 1; //January is 0!
	var yyyy = today.getFullYear();
	 if(dd<10){
	        dd='0'+dd
	    } 
	    if(mm<10){
	        mm='0'+mm
	    } 
	
	today = yyyy+'-'+mm+'-'+dd;
	tomorrow = yyyy+'-'+mm+'-'+ddT;

	var todayTime = new Date();
	var todayEndOfDay = new Date().setHours(17,00,0); //5:00pm

	/*if it's past 5pm, then force expiry date to be tomorrow or later*/
	// bug: 
	if(todayTime < todayEndOfDay) {
		datePicker.value = today;
		datePicker.setAttribute("min", today);
		console.log("today");
	} else {
		datePicker.setAttribute('min', tomorrow);
		datePicker.value = tomorrow;
		datePicker.setAttribute('min', tomorrow);
		console.log("tomorrow");
	}	
}

function fwdCheckDate() {

	var sDatePicker = document.getElementById("fwd-scheduleDatePicker");
	var eDatePicker = document.getElementById("fwd-expiryDatePicker");

	var today = new Date();
	var dd = today.getDate();
	var ddT = today.getDate() + 1;	//tomorrow
	var mm = today.getMonth() + 1; //January is 0!
	var yyyy = today.getFullYear();
	 if(dd<10){
	        dd='0'+dd
	    } 
	    if(mm<10){
	        mm='0'+mm
	    } 
	
	today = yyyy+'-'+mm+'-'+dd;
	tomorrow = yyyy+'-'+mm+'-'+ddT;

	var todayTime = new Date();
	var todayEndOfDay = new Date().setHours(17,00,0); //5:00pm

	/*if it's past 5pm, then force expiry date to be tomorrow or later*/
	// bug: 
	if(todayTime < todayEndOfDay) {
		sDatePicker.value = today;
		sDatePicker.setAttribute("min", today);
		eDatePicker.value = today;
		eDatePicker.setAttribute("min", today);
		console.log("today");
	} else {
		sDatePicker.value = tomorrow;
		sDatePicker.setAttribute('min', tomorrow);
		eDatePicker.value = tomorrow;
		eDatePicker.setAttribute('min', tomorrow);
		console.log("tomorrow");
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



