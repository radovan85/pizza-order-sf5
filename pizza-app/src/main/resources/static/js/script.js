window.onload = redirectHome;

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");
}

function redirectAllPizzasAdmin() {
	$("#ajaxLoadedContent").load("/admin/allPizzas");
}

function redirectAddPizza() {
	$("#ajaxLoadedContent").load("/admin/createPizza");
}

function redirectInvalidPath() {
	$("#ajaxLoadedContent").load("/admin/invalidPath");
}

function redirectPizzaDetails(pizzaId) {
	$("#ajaxLoadedContent").load("/admin/pizzaDetails/" + pizzaId);
}

function redirectUpdatePizza(pizzaId) {
	$("#ajaxLoadedContent").load("/admin/updatePizza/" + pizzaId);
}

function redirectAllSizesByPizza(pizzaId) {
	$("#ajaxLoadedContent").load("/admin/allSizes/" + pizzaId);
}

function redirectAllSizes() {
	$("#ajaxLoadedContent").load("/admin/allSizes");
}

function redirectAddPizzaSize() {
	$("#ajaxLoadedContent").load("/admin/createPizzaSize");
}

function redirectUpdatePizzaSize(pizzaSizeId) {
	$("#ajaxLoadedContent").load("/admin/updatePizzaSize/" + pizzaSizeId);
}

function redirectPizzaSizeDetails(pizzaSizeId) {
	$("#ajaxLoadedContent").load("/admin/pizzaSizeDetails/" + pizzaSizeId);
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/register");
}

function redirectAllPizzasUser() {
	$("#ajaxLoadedContent").load("/pizzas/allPizzas");
}

function redirectPizzaDetailsUser(pizzaId) {
	$("#ajaxLoadedContent").load("/pizzas/pizzaDetails/" + pizzaId);
}

function redirectAddPizzaToCart(pizzaId) {
	$("#ajaxLoadedContent").load("/carts/addToCart/" + pizzaId);
}

function redirectCart() {
	$("#ajaxLoadedContent").load("/carts/cart");
}

function redirectAddressConfirm() {
	$("#ajaxLoadedContent").load("/orders/confirmUserData");
}

function redirectCartError() {
	$("#ajaxLoadedContent").load("/carts/cartError");
}

function redirectPhoneConfirmation() {
	$("#ajaxLoadedContent").load("/orders/phoneConfirmation");
}

function redirectOrderConfirmation() {
	$("#ajaxLoadedContent").load("/orders/orderConfirmation");
}

function redirectCancelOrder() {
	$("#ajaxLoadedContent").load("/orders/cancelOrder");
}

function redirectOrderCompleted() {
	$("#ajaxLoadedContent").load("/orders/orderCompleted");
}

function redirectAllCustomers() {
	$("#ajaxLoadedContent").load("/admin/allCustomers");
}

function redirectCustomerDetails(customerId) {
	$("#ajaxLoadedContent").load("/admin/customerDetails/" + customerId);
}

function redirectAllOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrders");
}

function redirectOrderDetails(orderId) {
	$("#ajaxLoadedContent").load("/admin/orderDetails/" + orderId);
}



function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST"
	})
	.done(function(){
		checkForSuspension();
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/loginErrorPage");
	})
};

function checkForSuspension() {
	$.ajax({
		url : "http://localhost:8080/suspensionChecker",
		type : "POST"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Account suspended!");
		redirectLogout();
	})
};

function redirectLogout() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loggedout"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Logout error!");
	})
};

function deletePizza(pizzaId) {
	if (confirm("Are you sure you want to remove this pizza?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deletePizza/" + pizzaId
		})
		.done(function(){
			redirectAllPizzasAdmin();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};


function deletePizzaSize(pizzaSizeId) {
	if (confirm("Are you sure you want to remove this pizza size?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deletePizzaSize/" + pizzaSizeId
		})
		.done(function(){
			redirectAllSizes();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function eraseItem(itemId) {
	if (confirm("Remove this item from cart?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/carts/deleteItem/" + itemId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function eraseAllItems(cartId) {
	if (confirm("Are you sure you want to clear your cart?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/carts/deleteAllItems/" + cartId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function redirectValidateCart(cartId) {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/carts/validateCart/" + cartId
	})
	.done(function(){
		redirectAddressConfirm();
	})
	.fail(function(){
		redirectCartError();
	})
};

function executeOrder() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/orders/createOrder"
	})
	.done(function(){
		redirectOrderCompleted();
	})
	.fail(function(){
		alert("Failed!");
	})
};

function suspendUser(userId) {
	if (confirm("Are you sure you want to suspend this user?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/suspendUser/" + userId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function reactivateUser(userId) {
	if (confirm("Are you sure you want to reactivate this user?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/clearSuspension/" + userId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteOrder(orderId) {
	if (confirm("Are you sure you want to clear this order?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteOrder/" + orderId
		})
		.done(function(){
			redirectAllOrders();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteCustomer(customerId) {
	if (confirm("Are you sure you want to remove this customer?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteCustomer/" + customerId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed");
		})
	}
};


$("#searchForm").submit(function(event) {
	event.preventDefault();
});

$("#searchButton").on(
		"click",
		function() {
			var keyword = $("#keyword").val();
			if (validateKeyword()) {

				$("#ajaxLoadedContent").load(
						"/pizzas/searchPizza" + "?keyword=" + keyword);
			}
			;
		});
