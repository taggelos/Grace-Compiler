all: compile

compile:
	java -jar jtb132di.jar -te minijava.jj
	javacc minijava-jtb.jj
	javac Main.java
	javac attribute/*.java
	javac symboltable/*.java
clean:
	rm -f *.class *~