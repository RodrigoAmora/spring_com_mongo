$(function() {
	$( document ).ready(function() {
		var d = new Date();
		d.setFullYear( d.getFullYear() - 1000 );

		$('.datepicker').pickadate({
			selectMonths: true,
			selectYears: 100,
			format: 'dd/mm/yyyy',
			min: d,
			max: true,
			closeOnSelect: true,
			closeOnClear: true,
		});

		$('select').material_select();
	});
})