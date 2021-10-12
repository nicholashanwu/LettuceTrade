/**
 * 
 */

// TODO : prevent exchanging for the same currency


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
	
	/*console.log(first_currency_picker);			//prints the currency the first currency picker
	console.log(first_currency_quantity);*/
	
	//multiply first value 
	
	console.log("first currency picker " + first_currency_picker);
	let rate_one = rates.find(o => o.key === first_currency_picker);		//grabs both key and value pair
	
	console.log(rate_one.value);											//obj.value is the vaule
	
	let rate_two = rates.find(o => o.key === second_currency_picker);
	let inverse_rate_two = 1*1.0/rate_two.value;
	
	console.log(inverse_rate_two);
	
	var result_rate = rate_one.value * inverse_rate_two;
	console.log("multiply " + result_rate);

	console.log("resulting amount " + first_currency_quantity / result_rate); //print the resulting amount
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
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }

  // Show the current tab, and add an "active" class to the button that opened the tab
  document.getElementById(cityName).style.display = "block";
  evt.currentTarget.className += " active";

  document.getElementById("defaultOpen").click();
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



