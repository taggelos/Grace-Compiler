JAVAC=javac
JAVACC=javacc
JVM=java
CLASSPATH = lib/iris-0.60.jar:lib/iris-parser-0.60.jar:.

all: compile

compile: 
	$(JVM) -jar jtb132di.jar -te spiglet.jj
	$(JAVACC) spiglet-jtb.jj
	$(JAVAC) syntaxtree/*.java
	$(JAVAC) facts_gen/*.java
	$(JAVAC) optimizer/*.java
	$(JAVAC) -cp $(CLASSPATH) Main.java 

clean:
	rm -r *.class syntaxtree/*.class visitor/*.class facts_rules/Facts/* facts_gen/*.class optimizer/*.class
	
