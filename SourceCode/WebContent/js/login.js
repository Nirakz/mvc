function init(){
	URL_USER = "/WebChat/account";
	inputUsername = document.getElementById('input-username');

	inputUsername.onkeyup = function(event){
		 enterForLogin(event);
	};
}
	

function enterForLogin(event) {
	//if(event.keyCode == 13)
		//login();
}

function login(){
			var username = $('[name="username"]').val();
			var password =$('[name="password"]').val();
			console.log(username);
			console.log(password);
  			$.ajax({
  				type : 'POST',
  				url : 'account?action=login',
  				data : {username:username,password:password},
  				dataType : 'text',
  				success : function(data) {
  					console.log(data);
  					
  				}
  			});	
  	return false;
}
