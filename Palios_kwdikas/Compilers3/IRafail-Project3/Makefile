all: compile

compile:
	java -jar jtb132di.jar -te miniJava.jj
	java -jar javacc5.jar miniJava-jtb.jj
	javac Main.java
	javac types/*.java
	javac symbol_table/*.java
	javac Labels.java
	javac SpigletVisitor.java
	
execute:
	java Main