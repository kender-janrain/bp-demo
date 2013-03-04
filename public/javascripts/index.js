console.log("initializing backplane");

var IndexPage = (function() {
	Backplane(function() {
		Backplane.subscribe(function(msg) {
			console.log(msg);
		})
	});

	Backplane.init({
		serverBaseURL: "http://localhost:9000/v2",
		busName: "b1"
	});
	return {};
})();