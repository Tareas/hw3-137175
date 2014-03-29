

/* First created by JCasGen Tue Mar 25 19:49:41 PDT 2014 */
package some.typename.you;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FloatList;


/** type Lista Flotante
 * Updated by JCasGen Tue Mar 25 19:49:41 PDT 2014
 * XML source: C:/Users/Tania/hw3-137175/src/main/resources/descriptors/CollectionReaderDescriptor.xml
 * @generated */
public class Choose extends FloatList {
  /** @generated
   * @ordered 
   */
 // @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Choose.class);
  /** @generated
   * @ordered 
   */
  //@SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Choose() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Choose(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Choose(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
}

    