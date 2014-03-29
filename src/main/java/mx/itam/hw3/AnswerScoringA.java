/*  Answer Scoring: The system will incorporate a component that 
 *  assign an answer score annotation to each answer. 
 *  The answer score annotation record the score assigned to the answer.
 * 
 * */

package mx.itam.hw3;

import java.util.*;
import edu.cmu.deiis.types.*;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;


public class AnswerScoringA
	extends JCasAnnotator_ImplBase 
{

	public void process(JCas aJCas)
			throws AnalysisEngineProcessException 
	{
		System.out.println("Analysing AnswerScoring");
		
		AnnotationIndex<Annotation> aia = aJCas.getAnnotationIndex(Answer.type);
		AnnotationIndex<Annotation> ain = aJCas.getAnnotationIndex(NGram.type);
		FSIterator<Annotation> ait = aia.iterator();
		FSIterator<Annotation> nit = ain.iterator();
		NGram ngram = null;
		
		
		ArrayList<NGram> question_ngrams = new ArrayList<NGram>();
		ArrayList<NGram> answer_ngrams = new ArrayList<NGram>();
		
		while(nit.hasNext()) {
			ngram = (NGram)nit.next();
			if(ngram.getElementType().equals("Question")) {
				question_ngrams.add(ngram);
			} else if(ngram.getElementType().equals("Answer")) {
				answer_ngrams.add(ngram);
			} else {
				System.err.println("Odd N-gramm appears!");
			}
		}
		//Analyze which answer corresponds to the question 
		Answer ans = null;
		ArrayList<NGram> curr_ans_ngrams = new ArrayList<NGram>();
		int i = 0;
		double score = 0;
		AnswerScore ans_score = null;
		while(ait.hasNext()) {
			ans = (Answer)ait.next();
			curr_ans_ngrams.clear();
			while(i<answer_ngrams.size() && inAnnotation(ans, answer_ngrams.get(i))) {
				curr_ans_ngrams.add(answer_ngrams.get(i));
				i++;
			}
			score = scoreQuestionAnswer(question_ngrams, curr_ans_ngrams, aJCas.getDocumentText());
			//annotation for the score
			ans_score = new AnswerScore(aJCas);
			ans_score.setBegin(ans.getBegin());
			ans_score.setEnd(ans.getEnd());
			ans_score.setAnswer(ans);
			ans_score.setScore(score);
			ans_score.addToIndexes();
			ans_score.setConfidence(1.0);
			ans_score.setCasProcessorId("AnswerScoringAnnotator");
		}
		System.out.println(" AnswerScoringAnnotator");
	}
	
	private boolean inAnnotation(Answer ans, NGram nGram) {
		// TODO Auto-generated method stub
		return false;
	}

	private double scoreQuestionAnswer(ArrayList<NGram> question_ngrams, ArrayList<NGram> answer_ngrams, String text)
	{
		HashSet<String> question_tokens = new HashSet<String>();
		HashSet<String> answer_tokens = new HashSet<String>();
		
		for(NGram n : question_ngrams)
			for(int i=0; i<n.getElements().size(); i++)
				question_tokens.add(
						text.substring(
								n.getElements(i).getBegin(), 
								n.getElements(i).getEnd()
						) );

		for(NGram n : answer_ngrams)
			for(int i=0; i<n.getElements().size(); i++)
				answer_tokens.add(
						text.substring(
								n.getElements(i).getBegin(), 
								n.getElements(i).getEnd()
						) );
		return 0;
	
		/*How to calculate the score for every answer??*/
		
	 }
  }
