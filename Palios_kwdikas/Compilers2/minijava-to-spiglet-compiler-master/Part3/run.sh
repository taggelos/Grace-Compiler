#!/bin/bash

for file in $@ 
do
	java -cp lib/iris-0.60.jar:lib/iris-parser-0.60.jar:. Main $file
done