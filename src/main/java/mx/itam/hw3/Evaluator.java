 package mx.itam.hw3;

 import java.util.*;
 import edu.cmu.deiis.types.*;
 import org.apache.uima.UimaContext;
 import org.apache.uima.jcas.JCas;
 import org.apache.uima.cas.FSIterator;
 import org.apache.uima.jcas.tcas.Annotation;
 import org.apache.uima.cas.text.AnnotationIndex;
 import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
 import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
 import org.apache.uima.resource.ResourceInitializationException;


     // import edu.cmu.deiis.types.Answer;
     // import edu.cmu.deiis.types.AnswerScore;  
     // import edu.cmu.deiis.types.Question;

 public class Evaluator extends JCasAnnotator_ImplBase 
 {
      //private static DecimalFormat twoPlace = new DecimalFormat( "0.00" );
      //private static AnswerScoreComparator scoreComparator = new AnswerScoreComparator();
	  //private double questionsProcessed;
	  //private double precisionAccumulator;
	  
 	 Float totalCorrect = null;
	
 	 

 	
 public void initialize(UimaContext uimaContext)
 			 throws ResourceInitializationException
 	{
 		
  super.initialize(uimaContext);
 		this.totalCorrect = (Float)uimaContext.getConfigParameterValue("totalCorrect");
 	
 }
 	
 	
 public void process(JCas aJCas)
 			 throws AnalysisEngineProcessException 
 	{
 		AnnotationIndex<Annotation> aNis = aJCas.getAnnotationIndex(AnswerScore.type);
 		FSIterator<Annotation> Fit = aNis.iterator();
 		ArrayList<AnswerScore> scores_questions = new ArrayList<AnswerScore>();
 		AnswerScore as = null;
 		int out = 0;
 		while(Fit.hasNext()) {
 			as = (AnswerScore)Fit.next();
 			scores_questions.add(as);
 			out++;
 		}
 		
 if(out<this.totalCorrect)
 			 System.out.println("Ordering each question!");
 		else {
 			
            //order each question
 			Collections.sort(scores_questions, new Comparator<AnswerScore>() {
 				
 				public int compare(AnswerScore asA, AnswerScore asB) {
 					
 					int res = 0;
 					
 					if(Math.abs(asA.getScore() - asB.getScore()) <= 0.00001)
 						res = 0;
 					
 					if(asA.getScore() < asB.getScore())
 						res = -1;
 					
 					else
 					
 						res = 1;
 					return res;
 				}
 			});
 			
 			int foundCorrect = 0;
 			
 			Boolean asScore;
 			
 			// precision @ N where N = total # correct answers
 			for ( int i = 0 ; i < totalCorrect ; i++ ){
 				if (asScore = scores_questions.get(i).getAnswer().getIsCorrect())
 					foundCorrect++;
 			System.out.printf( "\nAverage Precision: ",this.totalCorrect, (float)foundCorrect/this.totalCorrect);
 		 }
 		}
 	}
 }
 

