# Backplane Demo #

This is a Play 2 web applications demonstrating 2 web "widgets" which can
communicate with one another via the Backplane protocol.

## Running ##

You will need:
* Redis, running on localhost:6379
* Backplane, running on localhost:9000
* A Backplane user with the credentials kender1:kender1
* A Backplane bus named "test1" that has POST granted to kender1
* Play 2

When you have gathered the required reagents, execute:
	play -Dhttp.port=$port run

Navigate to:
	http://localhost:$port/

Revel.