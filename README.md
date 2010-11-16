German Noun Decompounding Uima Annotator
========================================

Pre-requirements
---------------

* Google Web 1T 5-gram, 10 European Languages Version 1 (<http://www.ldc.upenn.edu/Catalog/CatalogEntry.jsp?catalogId=LDC2009T25>)
* Java 
* Maven


Installation
------------

Before you can use the component you need to index the Google Web1T corpus with Lucene.
Extract all German n-grams archives and put them in one folder on your medium.
In the `/bin` directory you will find an executable called `web1TLuceneIndexer`. Run:

    ./bin/web1TLuceneIndexer.sh \
        --web1t PATH/TO/FOLDER/WITH/ALL/EXTRACTED/N-GRAM/FILES \
        --outputPath PAHT/TO/LUCENE/INDEX/FOLDER
        
**CAUTION**: This task takes at least 5 hours and will write 25 GB of data to the medium.

The task will generate the index including all data. The extracted archives are not needed anymore.


More Information
----------------

More information can also be found in the `/doc` folder.