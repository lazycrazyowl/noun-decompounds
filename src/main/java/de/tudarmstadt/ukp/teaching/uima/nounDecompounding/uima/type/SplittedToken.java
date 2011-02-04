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



/* First created by JCasGen Wed Feb 02 12:11:06 CET 2011 */
package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.StringArray;


/** 
 * Updated by JCasGen Wed Feb 02 15:54:02 CET 2011
 * XML source: /home/jens/programming/uima-project/de.tudarmstadt.ukp.teaching.uima.nounDecompounding/src/main/resources/desc/type/SplitType.xml
 * @generated */
public class SplittedToken extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(SplittedToken.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected SplittedToken() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SplittedToken(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SplittedToken(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SplittedToken(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: Splits

  /** getter for Splits - gets 
   * @generated */
  public StringArray getSplits() {
    if (SplittedToken_Type.featOkTst && ((SplittedToken_Type)jcasType).casFeat_Splits == null)
      jcasType.jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((SplittedToken_Type)jcasType).casFeatCode_Splits)));}
    
  /** setter for Splits - sets  
   * @generated */
  public void setSplits(StringArray v) {
    if (SplittedToken_Type.featOkTst && ((SplittedToken_Type)jcasType).casFeat_Splits == null)
      jcasType.jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    jcasType.ll_cas.ll_setRefValue(addr, ((SplittedToken_Type)jcasType).casFeatCode_Splits, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for Splits - gets an indexed value - 
   * @generated */
  public String getSplits(int i) {
    if (SplittedToken_Type.featOkTst && ((SplittedToken_Type)jcasType).casFeat_Splits == null)
      jcasType.jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((SplittedToken_Type)jcasType).casFeatCode_Splits), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((SplittedToken_Type)jcasType).casFeatCode_Splits), i);}

  /** indexed setter for Splits - sets an indexed value - 
   * @generated */
  public void setSplits(int i, String v) { 
    if (SplittedToken_Type.featOkTst && ((SplittedToken_Type)jcasType).casFeat_Splits == null)
      jcasType.jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((SplittedToken_Type)jcasType).casFeatCode_Splits), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((SplittedToken_Type)jcasType).casFeatCode_Splits), i, v);}
  }

    