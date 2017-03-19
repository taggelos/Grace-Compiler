#!/bin/bash

for file in $@ 
do
	java Main $file
	filename="${file%.*}"
	filename_spig="$filename.spg"
	# echo $filename_spig
	java -jar spp.jar < $filename_spig	
	java -jar pgiv2.jar < $filename_spig > my_output
	javac $file

	java $filename > java_output

	if diff my_output java_output >/dev/null ; then
	  echo Succes for $file!
	else
	  echo Fail for $file
	  diff my_output java_output
	fi

done