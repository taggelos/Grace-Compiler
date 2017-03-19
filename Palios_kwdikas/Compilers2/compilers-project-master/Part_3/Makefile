
all: compile

compile:
	java -jar jtb132di.jar -te miniJava.jj
	javacc miniJava-jtb.jj
	javac Main.java
	javac my_type/*.java
	javac symbol_table/*.java
	javac spiglet_generator/*.java
	
execute:
	java Main

clean:
	rm -f *.class *~ my_type/*.class symbol_table/*.class syntaxtree/*.class visitor/*.class type_check/*.class ./outputs/*
