/**
 * This package contains dictionary classes.
 * Currently you have to options. You can work with
 * your own dictionary or with popular IGerman98 Dictionary
 * which is part of nearly all spell checkers.
 * 
 * If you want to use your own dictionary you have to create a
 * file that contains your words. Each word in one line.
 * Than you can use the {@see SimpleDictionary} class.
 * 
 * If you want to use the IGerman98 dictionary you can use 
 * the {@see IGerman98Dictionary}. The files are located
 * under /src/main/resources/de_DE.dic and /src/main/resources/de_DE.aff
 * 
 * Additional this package contains the {@see LinkingMorphemes} class.
 * This is a simple dictionary and hold all possible morphemes.
 * Currently the file /src/main/resources/linkingMorphemes.txt is used.
 * 
 * If you want to code you own dictionary use the {@see IDictionary}
 * interface.
 */
package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;