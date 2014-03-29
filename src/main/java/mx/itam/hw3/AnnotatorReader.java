/*  Class: AnnotatorReader.java
 *  
 */

package mx.itam.hw3;

import org.apache.uima.analysis_component.*;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
//import org.apache.*;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;

public class AnnotatorReader extends JCasAnnotator_ImplBase{

	private AnnotationIndex<Annotation> aanswer;
//	private Annotation at;

	public AnnotatorReader() {
		
	}

	public void process(JCas arg0) throws AnalysisEngineProcessException {
		
	AnnotationIndex<Annotation> aquestion = arg0.getAnnotationIndex(Question.type);
	setAanswer(arg0.getAnnotationIndex(Answer.type));
	String text = arg0.getDocumentText();
	aquestion.iterator();
	FSIterator<Annotation> fsi = aquestion.iterator();
    Question q;
    while (fsi.hasNext()){
    	q = (Question)fsi.next();
    	System.out.println(text.subSequence(q.getBegin(), q.getEnd())); 	
    }
	}

	public AnnotationIndex<Annotation> getAanswer() {
		return aanswer;
	}

	public void setAanswer(AnnotationIndex<Annotation> aanswer) {
		this.aanswer = aanswer;
	}

}
