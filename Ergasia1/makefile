all:
	java -jar sablecc.jar parser.grammar
	javac compiler/*/*.java *.java

clean:
	rm -f *.class *~
	rm -rf compiler/* *~