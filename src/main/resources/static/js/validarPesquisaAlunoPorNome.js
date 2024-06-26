$(document).ready( function() {
	$("#erroCampoVazio").hide();
	
	$("#formPesquisa").submit(function( event ) {
		var nomeAluno = $( "#nome").val();
		if (nomeAluno == "") {
			$("#erroCampoVazio").show();
			event.preventDefault();
		} else {
			$("#formPesquisa").submit();
			$("#erroCampoVazio").hide();
		}
	});
});
