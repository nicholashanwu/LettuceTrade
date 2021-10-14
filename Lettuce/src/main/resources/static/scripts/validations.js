
function validate_signUp() {
	var firstName = document.getElementById('user-firstName');
	var lastName = document.getElementById('user-lastName');
	var email = document.getElementById('user-email');
	var password = document.getElementById('user-password');
	var confirmPassword = document.getElementById('user-confirm-password');

	var firstName_msg = document.getElementById('user-firstName-message');
	var lastName_msg = document.getElementById('user-lastName-message');
	var email_msg = document.getElementById('user-email-message');
	var password_msg = document.getElementById('user-password-message');
	var confirmPassword_msg = document.getElementById('user-confirm-password-message');

	// http://zparacha.com/validate-email-address-using-javascript-regular-expression
	var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	var passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/gm;
	var check = true;

	// First Name 
	if (firstName.value.length == 0) { // Check if null
		firstName_msg.innerText = "Valid first name is required.";
		check = false;
	} else {
		firstName_msg.innerText = "";
		check = true;
	}
	// Last Name
	if (lastName.value.length == 0) {
		lastName_msg.innerText = "Valid last name is required.";
		check = false;
	} else {
		lastName_msg.innerText = "";
		check = true;
	}
	// Email
	if (email.value.length == 0) {
		email_msg.innerText = "Valid email is required.";
		check = false;
	} else if (!email.value.match(emailRegex)) {// Check if email follows valid format 
		email_msg.innerText = "Valid email is required.";
		check = false;
	} else {
		email_msg.innerText = "";
		check = true;
	}
	// Password 
	if (password.value.length == 0) {
		password_msg.innerText = "Valid password is required.";
		check = false;
	} else if (!password.value.match(passwordRegex)) {
		password_msg.innerText = "Password must have at least 8 characters, at least one uppercase character, and one number";	
		check = false;
	} else {
		password_msg.innerText = "";
		check = true;
	}
	// Confirm Password 
	if (confirmPassword.value.length == 0) {
		confirmPassword_msg.innerText = "Valid confirmation password is required.";
		check = false;
	} else if (password.value != confirmPassword.value) {// Check if password and confirm password match 
		confirmPassword_msg.innerText = "The confirmation password does not match.";
		check = false;
	} else {
		confirmPassword_msg.innerText = "";
		check = true;
	}
	return check;
}

function validate_login() {
	var email = document.getElementById('user-email');
	var password = document.getElementById('user-password');

	var email_msg = document.getElementById('user-email-message');
	var password_msg = document.getElementById('user-password-message');

	var check = false;

	// Email
	if (email.value.length == 0) {
		email_msg.innerText = "Valid email is required.";
		check = false;
	} else {
		email_msg.innerText = "";
		check = true;
	}
	// Password 
	if (password.value.length == 0) {
		password_msg.innerText = "Valid password is required.";
		check = false;
	} else {
		password_msg.innerText = "";
		check = true;
	}
	return check;
}
function validate_change_password() {
	var email =  document.getElementById('user-email');
	var oldPsd = document.getElementById('user-oldpsd');
	var password = document.getElementById('user-password');
	var confirmpassword = document.getElementById('user-confrimpassword');
	
	var email_msg = document.getElementById('user-email-message');
	var oldpassword_msg = document.getElementById('old-password-message');
	var password_msg = document.getElementById('user-password-message');
	var confirm_password_msg = document.getElementById('confirm-password-message');
	var check = false;
	var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	var passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/gm;
	// Email
	if (email.value.length == 0) {
		email_msg.innerText = "Valid email is required.";
		check = false;
	} else if (!email.value.match(emailRegex)) {// Check if email follows valid format 
		email_msg.innerText = "Valid email is required.";
		check = false;
	} else {
		email_msg.innerText = "";
		check = true;
	}
	// old password
	if (oldPsd.value.length == 0) {
		oldpassword_msg.innerText = "Valid password is required.";
		check = false;
	} else {
		email_msg.innerText = "";
		check = true;
	}

		// Password 
	if (password.value.length == 0) {
		password_msg.innerText = "Valid password is required.";
		check = false;
	} else if (!password.value.match(passwordRegex)) {
		password_msg.innerText = "Password must have at least 8 characters, at least one uppercase character, and one number";	
	} else {
		password_msg.innerText = "";
		check = true;
	}
		// Password 
	if (confirmpassword.value.length == 0) {
		confirm_password_msg.innerText = "Valid password is required.";
		check = false;
	} else {
		confirm_password_msg.innerText = "";
		check = true;
	}
	if(password.value===confirmpassword.value){
		confirm_password_msg.innerText = "";
		check=true;

	}else{
		confirm_password_msg.innerText = "two passwords are not identical. Please re-enter your password."
		check=false;
	}

	return check;
}
function validate_reset_password() {
	var password = document.getElementById('user-password');
	var confirmpassword = document.getElementById('user-confirmpassword');
	
	var password_msg = document.getElementById('user-password-message');
	var confirm_password_msg = document.getElementById('confirm-password-message');
	var passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/gm;
	var check = false;

		// Password 
	if (password.value.length == 0) {
		password_msg.innerText = "Valid password is required.";
		check = false;
	} else if (!password.value.match(passwordRegex)) {
		password_msg.innerText = "Password must have at least 8 characters, at least one uppercase character, and one number";	
	} else {
		password_msg.innerText = "";
		check = true;
	}
		// Password 
	if (confirmpassword.value.length == 0) {
		confirm_password_msg.innerText = "Valid password is required.";
		check = false;
	} else {
		confirm_password_msg.innerText = "";
		check = true;
	}
	if(password.value===confirmpassword.value){
		confirm_password_msg.innerText = "";
		check=true;

	}else{
		confirm_password_msg.innerText = "two passwords are not identical. Please re-enter your password."
		check=false;
	}

	return check;
}