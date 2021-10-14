
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
	//var check = false;
	var check_first_password = false;
	var check_confirm_password = false;
	var check_first_name = false;
	var check_last_name = false; 
	var check_email = false; 
	// First Name 
	if (firstName.value.length == 0) { // Check if null
		firstName_msg.innerText = "Valid first name is required.";
		check_first_name= false;
	} else {
		firstName_msg.innerText = "";
		check_first_name = true;
	}
	// Last Name
	if (lastName.value.length == 0) {
		lastName_msg.innerText = "Valid last name is required.";
		check_last_name = false;
	} else {
		lastName_msg.innerText = "";
		check_last_name = true;
	}
	// Email
	if (email.value.length == 0) {
		email_msg.innerText = "Valid email is required.";
		check_email = false;
	} else if (!email.value.match(emailRegex)) {// Check if email follows valid format 
		email_msg.innerText = "Valid email is required.";
		check_email = false;
	} else {
		email_msg.innerText = "";
		check_email = true;
	}
	// Password 
	if (password.value.length == 0) {
		password_msg.innerText = "Valid password is required.";
		check_first_password = false;
	} else if (!password.value.match(passwordRegex)) {
		password_msg.innerText = "Password must have at least 8 characters, at least one uppercase character, and one number";	
		check_first_password = false;
	} else {
		password_msg.innerText = "";
		check_first_password = true;
	}
	// Confirm Password 
	if (confirmPassword.value.length == 0) {
		confirmPassword_msg.innerText = "Valid confirmation password is required.";
		check_confirm_password= false;
	} else if (password.value != confirmPassword.value) {// Check if password and confirm password match 
		confirmPassword_msg.innerText = "The confirmation password does not match.";
		check_confirm_password= false;
	} else {
		confirmPassword_msg.innerText = "";
		check_confirm_password = true;
	}
	return (check_first_name && check_last_name&& check_email&&check_first_password && check_confirm_password);
}

function validate_login() {
	var email = document.getElementById('user-email');
	var password = document.getElementById('user-password');

	var email_msg = document.getElementById('user-email-message');
	var password_msg = document.getElementById('user-password-message');
	var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	var check_password = false;
	var check_email = false; 
	
	// Email
	if (email.value.length == 0) {
		email_msg.innerText = "Valid email is required.";
		check_email = false;
	} else if (!email.value.match(emailRegex)) {// Check if email follows valid format 
		email_msg.innerText = "Valid email is required.";
		check_email = false;
	} else {
		email_msg.innerText = "";
		check_email = true;
	}
	// Password 
	if (password.value.length == 0) {
		password_msg.innerText = "Valid password is required.";
		check_password = false;
	} else {
		password_msg.innerText = "";
		check_password = true;
	}
	return (check_email && check_password );
}
function validate_change_password() {
	var email =  document.getElementById('user-email');
	var oldPsd = document.getElementById('user-oldpsd');
	var password = document.getElementById('user-password');
	var confirmPassword = document.getElementById('user-confirmpassword');
	
	var email_msg = document.getElementById('user-email-message');
	var oldpassword_msg = document.getElementById('old-password-message');
	var password_msg = document.getElementById('user-password-message');
	var confirmPassword_msg = document.getElementById('confirm-password-message');
	var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	var passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/gm;
	var check_first_password = false;
	var check_confirm_password = false;
	var check_email = false; 
	// Email
	if (email.value.length == 0) {
		email_msg.innerText = "Valid email is required.";
		check_email = false;
	} else if (!email.value.match(emailRegex)) {// Check if email follows valid format 
		email_msg.innerText = "Valid email is required.";
		check_email = false;
	} else {
		email_msg.innerText = "";
		check_email = true;
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
		check_first_password = false;
	} else if (!password.value.match(passwordRegex)) {
		password_msg.innerText = "Password must have at least 8 characters, at least one uppercase character, and one number";	
		check_first_password = false;
	} else {
		password_msg.innerText = "";
		check_first_password = true;
	}
	// Confirm Password 
	if (confirmPassword.value.length == 0) {
		confirmPassword_msg.innerText = "Valid confirmation password is required.";
		check_confirm_password= false;
	} else if (password.value != confirmPassword.value) {// Check if password and confirm password match 
		confirmPassword_msg.innerText = "The confirmation password does not match.";
		check_confirm_password= false;
	} else {
		confirmPassword_msg.innerText = "";
		check_confirm_password = true;
	}

	return (check_email&&check_first_password && check_confirm_password);
}
function validate_reset_password() {
	var password = document.getElementById('user-password');
	var confirmPassword = document.getElementById('user-confirmpassword');
	
	var password_msg = document.getElementById('user-password-message');
	var confirmPassword_msg = document.getElementById('confirm-password-message');
	var passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/gm;
	var check_first_password = false;
	var check_confirm_password = false;

		// Password 
	if (password.value.length == 0) {
		password_msg.innerText = "Valid password is required.";
		check_first_password  = false;
	} else if (!password.value.match(passwordRegex)) {
		password_msg.innerText = "Password must have at least 8 characters, at least one uppercase character, and one number";	
		check_first_password= false; 
	} else {
		password_msg.innerText = "";
		check_first_password  = true;
	}
	// Confirm Password 
	if (confirmPassword.value.length == 0) {
		confirmPassword_msg.innerText = "Valid confirmation password is required.";
		check_confirm_password= false;
	} else if (password.value != confirmPassword.value) {// Check if password and confirm password match 
		confirmPassword_msg.innerText = "The confirmation password does not match.";
		check_confirm_password= false;
	} else {
		confirmPassword_msg.innerText = "";
		check_confirm_password = true;
	}
	return (check_first_password && check_confirm_password);
}