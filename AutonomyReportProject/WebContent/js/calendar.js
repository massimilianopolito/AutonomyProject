/*	$(function() {
		$('#dataDa').datepick({dateFormat: 'dd/mm/yyyy', maxDate: $.datepick.today()});
	});

	$(function() {
		$('#dataA').datepick({dateFormat: 'dd/mm/yyyy', maxDate: $.datepick.today()});
	});
*/

/*	$(function() {
		$('#DATA_CREAZIONE_DA').datepick({dateFormat: 'dd/mm/yyyy'});//,rangeSelect: true});
	});
*/
/*	$(function() {
		$('#DATA_CHIUSURA').datepick({dateFormat: 'dd/mm/yyyy'});
	});
*/
	$(function() {
		$('#DATA_CHIUSURA_DA,#DATA_CHIUSURA_A').datepick({onSelect: customRangeChiusura, dateFormat: 'dd/mm/yyyy'}); 
	});

	function customRangeChiusura(dates) { 
	    if (this.id == 'DATA_CHIUSURA_DA') { 
	        $('#DATA_CHIUSURA_A').datepick('option', 'minDate', dates[0] || null); 
	    } 
	    else { 
	        $('#DATA_CHIUSURA_DA').datepick('option', 'maxDate', dates[0] || null); 
	    } 
	}

	$(function() {
		$('#DATA_CREAZIONE_DA,#DATA_CREAZIONE_A').datepick({onSelect: customRange, dateFormat: 'dd/mm/yyyy'}); 
		setCalendar();
		if(!$('#DATA_CREAZIONE_DA').prop('disabled')){
	        manageGap(false);
	        if($("#DATA_CREAZIONE_DA").datepick( 'getDate' )!=''){
	        	manageGap(true);
	        }
		}
	});
	
	function setCalendar(){
    	var dataDA = $("#DATA_CREAZIONE_DA").datepick({dateFormat: 'dd/mm/yyyy'}).val();
    	var dataA = $("#DATA_CREAZIONE_A").datepick({dateFormat: 'dd/mm/yyyy'}).val();
	    $('#DATA_CREAZIONE_DA').datepick('option', 'maxDate', dataA);
	    $('#DATA_CREAZIONE_A').datepick('option', 'minDate', dataDA);
		
	}
	
	function manageGap(value){
    	if(value==true) document.getElementById("GAP").selectedIndex = 0;
    	document.getElementById("GAP").disabled = value;
	}
	
	function customRange(dates) {
		var data = dates[0];
		if(typeof data === "undefined") data = null;
	    if (this.id == 'DATA_CREAZIONE_DA') {
	    	if(data==null){
		    	$('#DATA_CREAZIONE_A').datepick('option', 'minDate', "01/01/1970");
	    	}else{
		    	$('#DATA_CREAZIONE_A').datepick('option', 'minDate', data);
	    	}
	    	
	        manageGap(false);
	        if($("#DATA_CREAZIONE_DA").datepick( 'getDate' )!=''){
	        	manageGap(true);
	        }
	    } 
	    else { 
	    	if(data==null){
		    	$('#DATA_CREAZIONE_DA').datepick('option', 'maxDate', '31/12/9999'); 
	    	}else{
		    	$('#DATA_CREAZIONE_DA').datepick('option', 'maxDate', data); 
	    	}
	    } 
	}
