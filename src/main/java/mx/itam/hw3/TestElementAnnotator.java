/* Class: TestElementAnnotation.java
 * TestElementAnnotation: The system will read in the input file as a UIMACAS 
 * and annotate the question and answer spans. 
 * Each answer annotation will also record whether or 
 * not the answer is correct.
 * 
 */

package mx.itam.hw3;

import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;


public class TestElementAnnotator extends JCasAnnotator_ImplBase {

	/* constructor
	 * public TestElementAnnotator() {
		// TODO Auto-generated constructor stub
	}*/

	@Override
	//JCas contains the document
	public void process(JCas arg0) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		
	  arg0.getDocumentText();
	  
	  //all the text that goes from Q to the last A
	  String texto = arg0.getDocumentText();
	  //print text
	  System.out.print(texto);
	 
	  //initialize the annotation for type question
	  Question q = new Question(arg0);
	  q.setBegin(10); 
	  q.setEnd(20);
	  
	  //store in JCas
	  q.addToIndexes();
	  
	  Answer a1 = new Answer (arg0);
	  a1.setBegin(15);
	  a1.setEnd(25);

	  a1.addToIndexes();
	  
	}
}
