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
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Feb 02 15:54:03 CET 2011
 * @generated */
public class SplittedToken_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SplittedToken_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SplittedToken_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SplittedToken(addr, SplittedToken_Type.this);
  			   SplittedToken_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SplittedToken(addr, SplittedToken_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = SplittedToken.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
 
  /** @generated */
  final Feature casFeat_Splits;
  /** @generated */
  final int     casFeatCode_Splits;
  /** @generated */ 
  public int getSplits(int addr) {
        if (featOkTst && casFeat_Splits == null)
      jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Splits);
  }
  /** @generated */    
  public void setSplits(int addr, int v) {
        if (featOkTst && casFeat_Splits == null)
      jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    ll_cas.ll_setRefValue(addr, casFeatCode_Splits, v);}
    
   /** @generated */
  public String getSplits(int addr, int i) {
        if (featOkTst && casFeat_Splits == null)
      jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Splits), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Splits), i);
  return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Splits), i);
  }
   
  /** @generated */ 
  public void setSplits(int addr, int i, String v) {
        if (featOkTst && casFeat_Splits == null)
      jcas.throwFeatMissing("Splits", "de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken");
    if (lowLevelTypeChecks)
      ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Splits), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Splits), i);
    ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Splits), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SplittedToken_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Splits = jcas.getRequiredFeatureDE(casType, "Splits", "uima.cas.StringArray", featOkTst);
    casFeatCode_Splits  = (null == casFeat_Splits) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Splits).getCode();

  }
}



    