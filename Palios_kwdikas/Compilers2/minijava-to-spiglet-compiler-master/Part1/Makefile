all: compile

compile:
	java -jar jtb132di.jar -te minijava.jj
	javacc minijava-jtb.jj
	javac Main.java
	
clean:
	rm -f *.class *~