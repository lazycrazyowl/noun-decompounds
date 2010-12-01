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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.igerman98;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Affix {

	public enum TYPE {
		PREFIX, SUFFIX
	}
	
	private static final String PREFIX_KEY = "PFX";
	private static final String SUFFIX_KEY = "SFX";
	
	private static final String PREFIX_CONDITION_REGEX_PATTERN = "%s.*";
    private static final String SUFFIX_CONDITION_REGEX_PATTERN = ".*%s";
	
	private TYPE type;
	private char flag;
	private String stripping;
	private String affix;
	private String condition;
	private Pattern conditionPattern; 
	private boolean crossProduct;
	
	public Affix(TYPE type) {
		this.type = type;
	}
	
	public Affix(String key) {
		if (key.equals(PREFIX_KEY)) {
			this.type = TYPE.PREFIX;
		} else if (key.equals(SUFFIX_KEY)) {
			this.type = TYPE.SUFFIX;
		} else {
			throw new RuntimeException(key + " do not exist");
		}
	}
	
	public boolean isCrossProduct() {
		return crossProduct;
	}

	public void setCrossProduct(boolean crossProduct) {
		this.crossProduct = crossProduct;
	}

	public TYPE getType() {
		return type;
	}
	
	public void setType(TYPE type) {
		this.type = type;
	}
	
	public char getFlag() {
		return flag;
	}
	
	public void setFlag(char flag) {
		this.flag = flag;
	}
	
	public String getStripping() {
		return stripping;
	}
	
	public void setStripping(String stripping) {
		this.stripping = stripping;
	}
	
	public String getAffix() {
		return affix;
	}
	
	public void setAffix(String affix) {
		this.affix = affix;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
		
		String regExp;
		
		switch (this.type) {
		case PREFIX:
			regExp = String.format(PREFIX_CONDITION_REGEX_PATTERN, condition);
			break;
		case SUFFIX:
			regExp = String.format(SUFFIX_CONDITION_REGEX_PATTERN, condition);
			break;
		default:
			throw new RuntimeException(this.type.toString() + " is not supported");
		}
		
		this.conditionPattern = Pattern.compile(regExp);
	}

	public String handleWord(String word) {
		Matcher m = this.conditionPattern.matcher(word);

		if (m != null && m.matches()) {
			if (this.type.equals(TYPE.PREFIX)) {
				return this.handlePrefix(word);
			} else if (this.type.equals(TYPE.SUFFIX)) {
				return this.handleSuffix(word);
			}
		}
		
		return null;
	}
	
	private String handlePrefix(String word) {
		if (this.stripping.equals("0") || word.startsWith(this.stripping)) {
			int start = 0;
			if (!this.stripping.equals("0") && word.startsWith(this.stripping)) {
				start = word.length() - this.stripping.length();
			}
		
			return this.affix + word.substring(start);
		}
		
		return null;
	}
	
	private String handleSuffix(String word) {
		if (this.stripping.equals("0") || word.endsWith(this.stripping)) {
			int end = word.length();
			if (!this.stripping.equals("0") && word.endsWith(this.stripping)) {
				end = word.length() - this.stripping.length();
			}
			
			return word.substring(0, end) + this.affix;
		}
		
		return null;
	}
}
