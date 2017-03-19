JAVAC=javac
JAVACC=javacc
JVM=java
CLASSPATH = lib/iris-0.60.jar:lib/iris-parser-0.60.jar:.

all: compile

compile:
	java -jar jtb132di.jar -te spiglet.jj
	javacc spiglet-jtb.jj
	$(JAVAC) -cp $(CLASSPATH) Main.java 

# run-spiglet-factorial:
# 	$(JVM) -cp $(CLASSPATH) Main Factorial.spg

# run-spiglet-binary_tree:
# 	$(JVM) -cp $(CLASSPATH) Main BinaryTree.spg

clean:
	rm -f *.class *~