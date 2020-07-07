$(document).ready(function() {
	$("#addComputer").validate({
		rules: {
			computerName: "required",
			introduced: {date: true},
			discontinued: {date: true},
			companyID: {}			
		},
		messages: {
			computerName: {
				required: "The name field is required.",
				introduced: "Please enter a valid date.",
				discontinued: "Please enter a valid date."
			}
		}
	})
});
