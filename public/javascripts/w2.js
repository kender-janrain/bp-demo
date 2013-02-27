(function ($) {
	$(document).ready(function() {
		$("#w2-go").click(function() {
			console.log("w2 button clicked");
			$.ajax({
				url: "/w2", type: "POST", cache: false, data: {
					"channelId": Backplane.getChannelID(),
					"messageType": "w2.click",
					"hello": "world"
				}
			}).done(function() {
					console.log("w2 published success", arguments);
					Backplane.expectMessages("w2.click");
				});
		});

		Backplane(function() {
			Backplane.subscribe(function(msg) {
				$("#w2-saw").append(
					$("<pre></pre>").text(JSON.stringify(msg, null, "\t"))
				);
			});
		});
	});
})(jQuery);