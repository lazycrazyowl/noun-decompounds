#!/bin/bash

args=''

for var in "$@"
do
    args="$args $var"
done

mvn exec:java -Dexec.mainClass="de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.LuceneIndexer" -Dexec.args="$args"

