package edu.cmu.deiis.types;

 //classic imports: input-output, net
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


//interesting ones about org.apache.uima.cas, collection, resources
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.impl.XCASDeserializer;
import org.apache.uima.cas.impl.XmiCasDeserializer;

//do not find the current CasInitializer 
import org.apache.uima.collection.CasInitializer;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;
 
/*
 *  Task1.2  
 *  Section 1
 *  Simple Collection Reader: reads documents 
 *  from a directory in the filesystem.
 *  @author: Tania Patino
 * 
 */

// Method FileSystemCollectionReader: that is for making a connection 
// to the file source and open a stream to read the content.

public abstract class FileSystemCollectionReader extends CollectionReader_ImplBase{
	
	// PARAMETER_IN is the configuration parameter set to the path of a directory containing input files
	public static final String PARAMETER_IN = "Input_Directory";
	
	//PARAMETER_EN contains the character enconding used by the input files
	public static final String PARAMETER_EN = "Encoding";
    
	//PARAMETER_LANGUAGE that contains the language of the document in the input directory
	public static final String PARAMETER_LANGUAGE = "Language";

    //PARAMETER_XCAS 
	public static final String PARAMETER_XCAS ="XCAS";
	
	//PARAM_LANGUAGE_M
	public static final String PARAMETER_LANGUAGE_M ="";
	
	//String mXCAS
	private String mXCAS;
	
	//Integer mCurrentIndex
	private int mCurrentIndex;
	
	//ArrayList  for mFiles
	private ArrayList mFiles;
	 
	//Two strings mLanguage and language_M
	private String mLanguage, language_M;
	
	//Boolean mTEXT
	private boolean mTEXT;
	 
	//String mEnconding
	private String mEncoding;   
	
    //Method initialize
	public void initialize() throws ResourceInitializationException {
		 
		
			String directoryPath = ((String) getConfigParameterValue(PARAMETER_IN)).trim();
		    File directory = new File(directoryPath);
		    mEncoding = (String) getConfigParameterValue(PARAMETER_EN);
		    mLanguage = (String) getConfigParameterValue(PARAMETER_LANGUAGE);
		    mXCAS = (String) getConfigParameterValue(PARAMETER_XCAS);
		    
		   
		    //The parameter could be xcas or xmi. Any other input file is treated as a text
		    
		    mTEXT = !("xcas".equalsIgnoreCase(mXCAS) || "xmi".equalsIgnoreCase(mXCAS) || "true".equalsIgnoreCase(mXCAS));
		    String language_M = (String) getConfigParameterValue(PARAMETER_LANGUAGE_M);
		   
			
		mCurrentIndex = 0;
		
		    // if the directory does not exist sends an exception
		    if (!directory.exists() || !directory.isDirectory()) {
		      throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND,
		              new Object[] {  PARAMETER_IN, this.getMetaData().getName(), directory.getPath() });
		    }
		
		    // List of files in the specified directory
		    mFiles = new ArrayList();
		    File[] files = directory.listFiles();
		    for (int i = 0; i < files.length; i++) {
		    	if (!files[i].isDirectory()) {
		    		mFiles.add(files[i]);
		      }
		    }
		  }
	
	
	//Method getNext 
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		    JCas jcas;
		    try {
		      jcas = aCAS.getJCas();
		    } catch (CASException e) {
		      throw new CollectionException(e);
		    }
		
		    // Open input stream to file
		    File file = (File) mFiles.get(mCurrentIndex++);
		    FileInputStream fis = new FileInputStream(file);
		    if (mTEXT) {
		      try {
		        
		    	  // Call it if there is a CAS, but do not found current version CasInitializer  
		        CasInitializer initializer = getCasInitializer();
				if (initializer != null) {
		          initializer.initializeCas(fis, aCAS);
		        } else // read file and set document text
		      {
		          String text = FileUtils.file2String(file, mEncoding);      
		          // document text in JCas
		          jcas.setDocumentText(text);
		        }
		      } finally {
		        if (fis != null)
		          fis.close();
		      }
		
		      // set language if it was explicitly specified as a configuration parameter
	     if (mLanguage != null) {
		        jcas.setDocumentLanguage(mLanguage);
		      }
		
		      
	          //Task 1.2
	          // Section 2:  create a Cas Consumer based on the Evaluator
	     	  // component of homework 2, and include it CPE pipeline.
	     
	          // Storing location of source document in CAS. CAS Consumers need to know
		      // where the original document contents are located.
		   
		      
	     	  SourceDocumentInformation srcDocInfo = new SourceDocumentInformation(jcas);
		      srcDocInfo.setUri(file.getAbsoluteFile().toURL().toString());
		      srcDocInfo.setOffsetInSource(0);
		      srcDocInfo.setDocumentSize((int) file.length());
		      srcDocInfo.setLastSegment(mCurrentIndex == mFiles.size());
		      srcDocInfo.addToIndexes();
		    }
		    // Analyzing XCas about the input files
		    else {
		      try {
		        if (mXCAS.equalsIgnoreCase("xmi")) {
		          XmiCasDeserializer.deserialize(fis, aCAS);
		        }
		        else {
		          XCASDeserializer.deserialize(fis, aCAS);
		        }
		      } catch (SAXException e) {
		        UIMAFramework.getLogger(FileSystemCollectionReader.class).log(Level.WARNING,
		                "Problem with file XML: " + file.getAbsolutePath());
		        throw new CollectionException(e);
		      } finally {
		        fis.close();
		      }
		    }
		  }

		 //Exceptions
		  public void close() throws IOException {
		  }

	      //Progress ENTITIES
		  public Progress[] getProgress() {
		    return new Progress[] { new ProgressImpl(mCurrentIndex, mFiles.size(), Progress.ENTITIES) };
		  }

		  
		  /*  Returns: the number of documents in the collection, reads the resource
		   *  CollectionReaderDescriptor.xml
		   */
	
		  public int getNumberOfDocuments() {
		    return mFiles.size();
		  }
	
		  public static CollectionReaderDescription getDescription() throws InvalidXMLException {
			     InputStream descStream = FileSystemCollectionReader.class
			              .getResourceAsStream("CollectionReaderDescriptor.xml");
			      return UIMAFramework.getXMLParser().parseCollectionReaderDescription(
			              new XMLInputSource(descStream, null));
			    }
			    
			    public static URL getDescriptorURL() {
			      return FileSystemCollectionReader.class.getResource("CollectionReaderDescriptor.xml");
			    }
			}
	
