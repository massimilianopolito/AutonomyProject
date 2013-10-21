<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>

<body>
	<script>
	$( "#cngPwd" ).click(function() {
		  var oldPwd = $('#oldPwd').val();
		  var newPwd = $('#newPwd').val();
		  
		  $.ajax({
			   type: 'POST',
			   url:'ManageUser',
			   dataType: 'json',
			   data: {'oldPwd':oldPwd , 'newPwd':newPwd, 'operation': '2' },
			   error: function (xhr, ajaxOptions, thrownError){
		            alert(thrownError);
		       },   
			   success: function(res){
				   var type = res.type;
				   var msg = res.msg;
				   var classMsg;
				   if(type=="0"){
					   classMsg = "info";
					   $('#formCngPwd').trigger( "reset" );
				   }else if(type=="1"){
					   classMsg = "errore";
				   }else if(type=="2"){
					   classMsg = "info";					   
					   msg = "<B><font style=\"color:#ff0000;\">ATTENZIONE!</font></B> " + msg;
				   }
				   $("#msgContainer").removeClass("info").addClass(classMsg);
				   $('#msg').html(msg);
			   }
			 });

		});
	</script>

	<div id="wrapper">
			<div id=msgContainer>
				<p id="msg"></p>
			</div>
			<div class="box boxForm shadow">
				<div class="title">
					<p>Compilare la scheda che segue con i valori richiesti</p>
				</div>
				<div class="content">
					<form id="formCngPwd" action="ManageUser" method="post">
						<p>
						<label>Vecchia Password: </label>
						<input type="password" id="oldPwd" name="oldPwd"/>
						</p>
						<p>
						<label>Nuova Password: </label>
						<input type="password" id="newPwd" name="newPwd"/>
						</p>
			
						<input type="button" id="cngPwd" class="btnSubmit" value="Cambia"/>
					</form>
				</div>
			</div>
	</div>
	
</body>