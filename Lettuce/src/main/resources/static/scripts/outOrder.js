/**
 * 
 */


function total(rate){
	var r = rate.substring(14,20);
	var quant = document.getElementById(rate).cells[3].firstChild.value;
	var s = document.getElementById(rate).cells[4];
	/*var total = quant*r;*/
	s.innerText = r*quant;
	console.log(s)
	console.log(r)
	console.log(quant)
	console.log(rate)
	}
