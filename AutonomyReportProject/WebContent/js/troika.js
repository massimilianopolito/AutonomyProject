	 function onChangeFirst(first, second, third){
		  elem = $(first).val();
		   
		  $(second + ' option').each(function(){$(second + ' option').remove();});
		  $(second).append('<option value="--" selected="selected">In caricamento....</option>');

		  $.ajax({
		   type: 'POST',
		   url:'ManageCombo',
		   dataType: 'json',
		   data: {'first':elem},
		   error: function (xhr, ajaxOptions, thrownError){
	            alert(thrownError);
	       },   
		   success: function(res){
		    $(second + ' option').each(function(){$(second + ' option').remove();});
		    $(second).append('<option value="--" selected="selected">- Selezionare -</option>');
		    $(third + ' option').each(function(){$(third + ' option').remove();});
		    $(third).append('<option  value="--" selected="selected">- Selezionare -</option>');
		    $.each(res, function(i, value){
		    	for(var i=0; i < value.length; i++){	
		    		$(second).append($('<option>').text(value[i]).attr('value', value[i]));
		    	}
		    });
		   }
		 });
	}
	 
	 function onChangeSecond(first, second, third){
		  elem = $(second).val();

		  $(third + ' option').each(function(){$(third + ' option').remove();});
		  
		  if(elem=="--"){
			  $(third).append('<option  value="--" selected="selected">- Selezionare -</option>');
		  }else{
			  $(third).append('<option  value="--" selected="selected">In caricamento....</option>');

			  $.ajax({
			   type: 'POST',
			   url:'ManageCombo',
			   dataType: 'json',
			   data: {'first': $(first).val() , 'second':elem},
			   error: function (xhr, ajaxOptions, thrownError){
		            alert(thrownError);
		       },   
		       success: function(res){
			    $(third + ' option').each(function(){$(third + ' option').remove();});
			    $(third).append('<option  value="--" selected="selected">- Selezionare -</option>');
			    $.each(res, function(i, value){
			    	for(var i=0; i < value.length; i++){	
			    		$(third).append($('<option>').text(value[i]).attr('value', value[i]));
			    	}
			    });
			   }
			  });
		  }
	}

	$(document).ready(function(){
		$('#first').change(function(){onChangeFirst('#first', '#second', '#third');});
		$('#second').change(function(){onChangeSecond('#first', '#second', '#third');});

		$('#firstGR').change(function(){onChangeFirst('#firstGR', '#secondGR', '#thirdGR');});
		$('#secondGR').change(function(){onChangeSecond('#firstGR', '#secondGR', '#thirdGR');});

		$('#firstSP').change(function(){onChangeFirst('#firstSP', '#secondSP', '#thirdSP');});
		$('#secondSP').change(function(){onChangeSecond('#firstSP', '#secondSP', '#thirdSP');});

	});
