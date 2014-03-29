/*  Class: TokenA.java
 *  Token Annotation: The system will annotate each token span in 
 *  each question and answer (break on whitespace and punctuation).
 *  
 */

package mx.itam.hw3;

import edu.cmu.deiis.types.*;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

// API Reference: http://docs.oracle.com/javase/7/docs/api/

import java.util.regex.Matcher;   //from java.util.regex.Matcher  
import java.util.regex.Pattern;   //from java.util.regex.Pattern 

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;



public class TokenA extends JCasAnnotator_ImplBase {
	
	final String annotatorName = "TokenA";
	Pattern  p_pattern = Pattern.compile("[\\., \\!\\?']+");
	
	
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		System.out.println("Display Annotation Name: " +annotatorName);
		
		AnnotationIndex<Annotation> aiq = aJCas.getAnnotationIndex(Question.type);
		AnnotationIndex<Annotation> aia = aJCas.getAnnotationIndex(Answer.type);

		String text = aJCas.getDocumentText();
		this.iterateAnnotations(aiq, text, aJCas);
		this.iterateAnnotations(aia, text, aJCas);
		
		System.out.println("Finish: " +annotatorName);
	}
	
	private void iterateAnnotations(AnnotationIndex<Annotation> aiq, String text, JCas aJCas) {
		
		FSIterator<Annotation> it1 = aiq.iterator();
		Annotation an;
		
		while(it1.hasNext()) {
			an = it1.next();
			this.createTokens(an, text, aJCas);			
		}
	}
	
	private void createTokens(Annotation a, String text, JCas jcas)
	{
		int begin = a.getBegin();
		int end = a.getEnd();
		String phrase = text.substring(begin, end);
		String[] strings = phrase.split(p_pattern.pattern());
		int i=0, j;
		Token token;
		for(String s: strings) {
			System.out.print(s + " ");
			i = phrase.indexOf(s, i);
			j = i + s.length();
			token = new Token(jcas);
			token.setBegin(i + begin);
			token.setEnd(j + begin);
			token.setCasProcessorId(this.annotatorName);
			token.setConfidence(1.0);
			token.addToIndexes();
		}
	
	}

}

