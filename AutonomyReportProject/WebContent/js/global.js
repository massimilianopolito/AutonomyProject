	function viewContent(reference, idoldb){
		document.getElementById("reference").value = reference;
		document.getElementById("idoldb").value = idoldb;
		myform.target='POPUPW';
		POPUPW = window.open('about:blank','POPUPW','width=600,height=600');
		document.forms[0].submit();	
	}
	
	function viewContentTripletta(reference, idoldb, query){
		document.getElementById("reference").value = reference;
		document.getElementById("idoldb").value = idoldb;
		document.getElementById("query").value = query;
		myform.target='POPUPW';
		POPUPW = window.open('about:blank','POPUPW','width=600,height=600');
		document.forms[0].submit();	
	}
	
	function viewContentStruttura(reference, idoldb, query){
		document.getElementById("reference").value = reference;
		document.getElementById("idoldb").value = idoldb;
		document.getElementById("query").value = query;
		myformS.target='POPUPW';
		POPUPW = window.open('about:blank','POPUPW','width=600,height=600');
		document.myformS.submit();	
	}
	
	
	function apri(titolo, url) {
		var newin = window.open(url,"","scrollbars=no,resizable=yes,width=600,height=600,status=no,location=no,toolbar=no,directories=no");
	} 
	
	function manage(code, operation, formId, operationId, triplettaCorrenteID){
		document.getElementById(operationId).value = operation;
		document.getElementById(triplettaCorrenteID).value = code;
		document.getElementById(formId).submit();	
	}

	function manageAndCheck(code, operation, formId, operationId, triplettaCorrenteID){
		var object = document.getElementById("caratterizzazione_" + code);
		var val = object.options[object.selectedIndex].value;
		if(val=="--"){
			alert('La selezione del silos e\' obbligaria');
		}else{
			manage(code, operation, formId, operationId, triplettaCorrenteID);
		}
	}

	function manageAndCheckGlobal(formId){
		var object = document.getElementById("caratterizzazione_" + document.getElementById("codiceF").value);
		var val = object.options[object.selectedIndex].value;
		if(val=="--"){
			alert('La selezione del silos e\' obbligaria');
		}else{
			document.getElementById(formId).submit();	
		}
	}

	