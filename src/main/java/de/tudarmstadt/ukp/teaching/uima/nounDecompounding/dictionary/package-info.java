/**
 * Copyright (c) 2010 Jens Haase <je.haase@googlemail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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