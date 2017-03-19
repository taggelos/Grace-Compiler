all: compile

compile:
	java -jar ../jtb132di.jar -te miniJava.jj
	java -jar ../javacc5.jar miniJava-jtb.jj
	javac Main.java
	javac types/*.java
	javac symbol_table/*.java
	javac TypeCheckVisitor.java
	
execute:
	java Main

clean:
	rm -f *.class *~ types/*.class symbol_table/*.class syntaxtree/*.class visitor/*.class TypeCheckVisitor.class
