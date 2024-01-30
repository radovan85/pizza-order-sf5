function validatePizza() {

	var name = document.getElementById("name").value;
	var description = document.getElementById("description").value;
	var imageUrl = document.getElementById("imageUrl").value;
	
	var nameError = document.getElementById("nameError");
	var descriptionError = document.getElementById("descriptionError");
	var imageUrlError = document.getElementById("imageUrlError");

	var returnValue = true;

	if (name === "" || name.length > 40) {
		nameError.style.visibility = "visible";
		returnValue = false;
	} else {
		nameError.style.visibility = "hidden";
	}

	if (description === "" || description.length > 90) {
		descriptionError.style.visibility = "visible";
		returnValue = false;
	} else {
		descriptionError.style.visibility = "hidden";
	}
	
	if (imageUrl === "" || imageUrl.length > 255) {
		imageUrlError.style.visibility = "visible";
		returnValue = false;
	} else {
		imageUrlError.style.visibility = "hidden";
	}

	return returnValue;

};

function validatePizzaSize() {
	var pizzaId = document.getElementById("pizzaId").value;
	var name = document.getElementById("name").value;
	var price = document.getElementById("price").value;
	
	var pizzaIdError = document.getElementById("pizzaIdError");
	var nameError = document.getElementById("nameError");
	var priceError = document.getElementById("priceError");

	var priceNum = Number(price);
	var returnValue = true;

	if (name === "" || name.length > 40) {
		nameError.style.visibility = "visible";
		returnValue = false;
	} else {
		nameError.style.visibility = "hidden";
	}

	if (pizzaId === "") {
		pizzaIdError.style.visibility = "visible";
		returnValue = false;
	} else {
		pizzaIdError.style.visibility = "hidden";
	}

	if (price = "" || priceNum <= 0) {
		priceError.style.visibility = "visible";
		returnValue = false;
	} else {
		priceError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateRegForm() {
	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	var address = document.getElementById("address").value;
	var city = document.getElementById("city").value;
	var postcode = document.getElementById("postcode").value;
	var phone = document.getElementById("phone").value;
	
	var firstNameError = document.getElementById("firstNameError");
	var lastNameError = document.getElementById("lastNameError");
	var emailError = document.getElementById("emailError");
	var passwordError = document.getElementById("passwordError");
	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var postcodeError = document.getElementById("postcodeError");
	var phoneError = document.getElementById("phoneError");

	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;
	var returnValue = true;

	if (firstName === "" || firstName.length > 30) {
		firstNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		firstNameError.style.visibility = "hidden";
	}
	

	if (lastName === "" || lastName.length > 30) {
		lastNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		lastNameError.style.visibility = "hidden";
	}

	if (email === "" || !regEmail.test(email) || email.length > 50) {
		emailError.style.visibility = "visible";
		returnValue = false;
	} else {
		emailError.style.visibility = "hidden";
	}

	if(password.length < 6 || password.length > 30 ){
		passwordError.style.visibility = "visible";
		returnValue = false;
	}else {
		passwordError.style.visibility = "hidden";
	}

	if (address === "" || address.length > 75) {
		addressError.style.visibility = "visible";
		returnValue = false;
	} else {
		addressError.style.visibility = "hidden";
	};

	if (city === "" || city.length > 40) {
		cityError.style.visibility = "visible";
		returnValue = false;
	} else {
		cityError.style.visibility = "hidden";
	};
	

	if(postcode.length < 5 || postcode.length > 10){
		postcodeError.style.visibility = "visible";
		returnValue = false;
	}else {
		postcodeError.style.visibility = "hidden";
	}

	if(phone.length < 9 || phone.length > 15){
		phoneError.style.visibility = "visible";
		returnValue = false;
	}else {
		phoneError.style.visibility = "hidden";
	}

	return returnValue;
}

function validateCartItem() {
	var quantity = document.getElementById("quantity").value;
	var pizzaSizeId = document.getElementById("pizzaSizeId").value;
	
	var quantityError = document.getElementById("quantityError");
	var pizzaSizeIdError = document.getElementById("pizzaSizeIdError");

	var quantityNum = Number(quantity);
	var returnValue = true;

	if (quantity === "" || quantityNum < 1 || quantityNum > 20) {
		quantityError.style.visibility = "visible";
		returnValue = false;
	} else {
		quantityError.style.visibility = "hidden";
	}

	if (pizzaSizeId === "") {
		pizzaSizeIdError.style.visibility = "visible";
		returnValue = false;
	} else {
		pizzaSizeIdError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateShippingAddress() {
	var address = document.getElementById("address").value;
	var city = document.getElementById("city").value;
	var postcode = document.getElementById("postcode").value;
	
	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var postcodeError = document.getElementById("postcodeError");

	var returnValue = true;

	if (address === "" || address.length > 75) {
		addressError.style.visibility = "visible";
		returnValue = false;
	} else {
		addressError.style.visibility = "hidden";
	};

	if (city === "" || city.length > 40) {
		cityError.style.visibility = "visible";
		returnValue = false;
	} else {
		cityError.style.visibility = "hidden";
	};
	

	if(postcode.length < 5 || postcode.length > 10){
		postcodeError.style.visibility = "visible";
		returnValue = false;
	}else {
		postcodeError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateCustomer() {
	var customerPhone = document.getElementById("customerPhone").value;
	var customerPhoneError = document.getElementById("customerPhoneError");
	var returnValue = true;

	if (customerPhone === "" || customerPhone.length < 9 || customerPhone.length > 15) {
		customerPhoneError.style.visibility = "visible";
		returnValue = false;
	} else {
		customerPhoneError.style.visibility = "hidden";
	}

	return returnValue;

};

function validatePassword() {
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	if (password != confirmpass) {
		alert("Password does Not Match.");
		return false;
	}
	return true;
};

function validateNumber(e) {
	var pattern = /^\d{0,4}(\.\d{0,4})?$/g;

	return pattern.test(e.key)
};

function validateKeyword() {
	var returnValue = true;
	var keyword = $("#keyword").val();

	if (keyword === "") {
		returnValue = false;
	}

	return returnValue;
}