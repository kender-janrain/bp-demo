(function ($) {
	$(document).ready(function() {
		$("#w2-go").click(function() {
			console.log("w2 button clicked");
			$.ajax({
				url: "/w2", type: "POST", cache: false, data: {
					"channelId": Backplane.getChannelID(),
					"messageType": "urn:w2:click"
				}
			}).done(function() {
				console.log("w2 published success", arguments);
				Backplane.expectMessages("w2.click");
			});
		});

		Backplane(function() {
			Backplane.subscribe(function(msg) {
				$.ajax({
					url: "/w2/msg", type: "POST", cache: false, data: {
						messageUrl: msg["messageURL"]
					}
				}).done(function(data) {
					$("#w2-saw").append(
						$("<pre></pre>").text("Widget #1 said " + data)
					);
				});
			});
		});
	});
})(jQuery);