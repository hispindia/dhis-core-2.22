jQuery(document).ready(function()
{
	var r = getValidationRules();

	var rules = {
		name : {
			required : true,
			range : r.organisationUnitGroup.name.range
		}
	};

	validation2('updateOrganisationUnitGroupForm', function( form )
	{
		form.submit()
	}, {
		'rules' : rules
	});

	jQuery("#name").attr("maxlength", r.organisationUnitGroup.name.range[1]);
});
