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
	});
	     
	function customRange(dates) { 
	    if (this.id == 'DATA_CREAZIONE_DA') { 
	        $('#DATA_CREAZIONE_A').datepick('option', 'minDate', dates[0] || null); 
	    } 
	    else { 
	        $('#DATA_CREAZIONE_DA').datepick('option', 'maxDate', dates[0] || null); 
	    } 
	}
