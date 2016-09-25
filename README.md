Run by entering the mygame folder and runnint this in the terminal
$ export MAVEN_OPTS=-Djava.library.path=target/natives
$ mvn compile exec:java -Dexec.bezierdemo=myproject.bezierdemo

This thing is kinda neat. Mess around with the control points and see if it is fun.
Don't let the Box get to close to Y=0 or it will never come back!
