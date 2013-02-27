(function ($) {
	$(document).ready(function() {
		$("#w1-go").click(function() {
			console.log("w1 button clicked");
			$.ajax({
				url: "/w1", type: "POST", cache: false, data: {
					"channelId": Backplane.getChannelID(),
					"messageType": "w1.click",
					"text": $("#w1-text").val()
				}
			}).done(function() {
					console.log("w1 published success", arguments);
					Backplane.expectMessages("w1.click");
				});
		});
	});
})(jQuery);