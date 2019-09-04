package com.xush.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlFunc {
  private XmlFunc() {
  }
  
  /**
   * ʹ�õ�ǰ���õĲ�������һ���µ� DocumentBuilder ʵ��
   * @return ����DocumentBuilderʵ��
   * @throws Exception
   */
  public static final DocumentBuilder getDocumentBuilder() throws Exception{
    return DocumentBuilderFactory.newInstance().newDocumentBuilder();
  }
  
  /**
   * ��Ӧ���Լ�ָ�����룬������xml�ļ���ָ��encoding��
   * @param is
   * @return
   * @throws Exception
   */
  public static final Document getDocument(InputStream is) throws Exception{
    if ("weblogic.apache.xerces.parsers.SAXParser".equals(System.getProperty("org.xml.sax.driver"))) {//��Weblogic ϵ�а汾��
      return null;
    }
  	InputSource source = new InputSource(is);
    return getDocumentBuilder().parse(source);
  }
  

  
  public static final Document getDocument(Reader is) throws Exception{
    InputSource source = new InputSource(is);
    return getDocumentBuilder().parse(source);
  }
  
  public static final Document getDocument(String xml) throws Exception {
    InputSource source = new InputSource(new StringReader(xml));
    return getDocumentBuilder().parse(source);
  }
  
  /**
   * ��һ�������ж������Դ�й���һ��dom����
   */
  public static final Document getDocumentFrom(String resourceName, Class cls) throws Exception{
    InputStream in = cls.getResourceAsStream(resourceName);
    try {
      return getDocument(in);
    }
    finally {
      in.close();
    }
  }
  
  /**
   * ����һ�����ڵ�ΪrootTagName��Document����
   * ���硰reports��
   * Ҳ������һ��������xml��"<?xml-stylesheet type=\"text/xsl\" href=\"log.xsl\"?><reports></reports>"
   * @param rootTagName
   * @return
   * @throws Exception
   */
  public static final Document createDocument(String rootTagName) throws Exception{
  	if(!rootTagName.startsWith("<") || !rootTagName.endsWith(">")){
  		rootTagName = "<"+rootTagName+"></"+rootTagName+">";
  	}
		
		return XmlFunc.getDocument(rootTagName);
  }
  
  /**
   * ���Ƽ�ʹ�ô˷���,Ӧ����ȷָ������.
   * @deprecated ʹ��saveDocument(Document doc, OutputStream os,String encoding)
   */
  public static final void saveDocument(Document doc, OutputStream os)
      throws Exception {
    saveDocument(doc, os, null);
  }

  /**
   * ����ָ���ı��뽫Document���浽����
   * @param doc Document����
   * @param os  ������
   * @param encoding ָ������
   * @throws Exception
   */
  public static final void saveDocument(Document doc, OutputStream os,String encoding) throws Exception{
    Result result =new StreamResult(os);
    saveDocument(doc, result, encoding);
  }
  
  /**
   * Mar 20, 2009 5:45:38 PM
   * @usage ��������������UTF-8�����XML֮ǰ�����3
   *        ��BOM�ֽ�, ������BOM�ֽڷֱ�ΪEF, BB, BF.
   *        ��ĳЩ����½���XMLʱ, ���û��������BOM�ֽ�, 
   *        �ᵼ��INVALID XML����. ����FUSIONCHART
   *        ��ʹ��setDataURL������ȡXML��ʱ��.
   * @param doc
   */
  public static final void saveDocument_BOM(Document doc, OutputStream os) throws Exception{
    os.write(0xEF);
    os.write(0xBB);
    os.write(0xBF);
    saveDocument(doc, os, StrFunc.UTF8);
  }
  
  /**
   * indent���ڿ��������xml�Ƿ��������ͻ��У�
   *       �����xml����������лس��Ļ�����ô��ff��childNodes������Ҳ�����س�Ԫ�أ���ɿͻ���װ��xmlʱ��Ҫ���ˣ����Է���������xml��ö���û�л��з���
   */
  public static final void saveDocument(Document doc, OutputStream os,String encoding, boolean indent) throws Exception{
    Result result =new StreamResult(os);
    saveDocument(doc, result, encoding,indent);
  }
  /**
   * ��Document���ݰ���ָ���ı���д��Writer����
   * @param doc Document����
   * @param os  Writer��
   * @param encoding ����
   * @throws Exception
   */
  public static final void saveDocument(Document doc, Writer os,String encoding) throws Exception{
    Result result =new StreamResult(os);
    saveDocument(doc, result, encoding);
  }
  /**
   * ��Document����д������ ,indent���ڿ��������xml�Ƿ��������ͻ���
   * @param doc Document����
   * @param os Writer��
   * @param encoding ����
   * @param indent Ϊtrue����������
   * @throws Exception
   */
  public static final void saveDocument(Document doc, Writer os,String encoding, boolean indent) throws Exception{
    Result result =new StreamResult(os);
    saveDocument(doc, result, encoding, indent);
  }
  
  private static void saveDocument(Document doc, Result result, String encoding) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
    /**
     * 20090704 ��Ϊ"Ĭ��������"�������Ķ�����������ռ�ö��ռ䡣
     */
    saveDocument(doc,result,encoding,true);
  }
  /**
   * indent���ڿ��������xml�Ƿ��������ͻ��У�
   *       �����xml����������лس��Ļ�����ô��ff��childNodes������Ҳ�����س�Ԫ�أ���ɿͻ���װ��xmlʱ��Ҫ���ˣ����Է���������xml��ö���û�л��з���
   */
  private static void saveDocument(Document doc, Result result, String encoding, boolean indent) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		saveNode(doc, result, encoding, indent);
  }
  
  /**
   * ���ؽڵ��·�������硰c/b/a/root/#document/��
   * @param node
   * @return
   */
  public static String getNodePath(Node node){
	  StringBuffer sb = new StringBuffer(32);
	  while(node!=null){
		  sb.append(node.getNodeName());
		  sb.append("/");
		  node = node.getParentNode();
	  }
	  return sb.toString();
  }

  	/**
  	 * ������ֻ������Ƿ�Ϊnull��
  	 * �������ֻ�����Ϊnull���������Щxml������������xml�ַ���ʱ���׿�ָ���쳣�����Ҵ��쳣��ջ���ѿ������ĸ��ط������⡣
  	 * ���������ָ���쳣�����ñ��������Բ������λ�á�
  	 * @param node
  	 */
	public static void checkNullNode(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			String nodeValue = node.getNodeValue();
			if (nodeValue == null) {
				//throw new RuntimeException("��������Ϊnull��text�ڵ㣺" + getNodePath(node));
				throw new RuntimeException(I18N.getString("com.esen.util.XmlFunc.1", "��������Ϊnull��text�ڵ㣺") + getNodePath(node));
			}
		}

		if(node.getNodeType() == Node.ELEMENT_NODE){
			int length = node.getAttributes().getLength();
			for (int i = 0; i < length; i++) {
				String attr = node.getAttributes().item(i).getNodeName();
				String value = ((Element)node).getAttribute(attr);
				if(value == null){
					//throw new RuntimeException("��������"+attr+"ֵΪnull�Ľڵ㣺" + getNodePath(node));
					throw new RuntimeException(I18N.getString("com.esen.util.XmlFunc.2", "��������{0}ֵΪnull�Ľڵ㣺") + getNodePath(node));
				}
			}
		}

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			checkNullNode(childNodes.item(i));
		}
	}
  
  /**
   * ֧�����һ��Node�ڵ������,DocumentҲ��Node�ڵ�,Ҳ����������������
   * 
   * �� XML Source ת��Ϊ Result��
   * 
   * @param node
   * @param result
   * @param encoding
   * @param indent
   * @throws TransformerConfigurationException
   * @throws TransformerFactoryConfigurationError
   * @throws TransformerException
   */
  public static void saveNode(Node node, Result result, String encoding, boolean indent) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
	//checkNullNode(node);//�������xmlʱ���ֿ�ָ���쳣�����Ե���checkNullNode�������λ�á�

	Transformer tf = TransformerFactory.newInstance().newTransformer();
    Properties properties = tf.getOutputProperties();

    if(StrFunc.isNull(encoding)) encoding = StrFunc.GB2312;
    properties.setProperty(OutputKeys.ENCODING, encoding);
    properties.setProperty(OutputKeys.INDENT, indent?"yes":"no");

    //����XSLT�����������ԡ�
    tf.setOutputProperties(properties);
    
    Source xmlSource = new DOMSource(node);
    tf.transform(xmlSource, result);
  }

  /**
   * ���Ƽ���documentתΪstring�����Ҫ����Ļ���Ӧ�ñ���Ϊbyte[]
   * @deprecated
   * @param doc
   * @return
   * @throws Exception
   */
  public static final String document2str(Document doc) throws Exception{
    return document2str(doc,null);
  }

  /**
   * ���Ƽ���documentתΪstring�����Ҫ����Ļ���Ӧ�ñ���Ϊbyte[]
   * @deprecated
   * @param doc
   * @return
   * @throws Exception
   */
  public static final String document2str(Document doc, String encoding)
      throws Exception {
    ByteArrayOutputStream result = new ByteArrayOutputStream(512);
    try {
      XmlFunc.saveDocument(doc, result, encoding);
    }
    finally {
      result.close();
    }
    String enc = StrFunc.isNull(encoding)?StrFunc.GB2312:encoding;
    return result.toString(enc);
  }
  
  /**
   * ����node��ֱ���ӽڵ㣬����нڵ����Ƶ���name���򷵻���ֵ�����򷵻�def
   * @param node �����Ľڵ�
   * @param name �ڵ������
   * @param def Ĭ�ϵķ���ֵ
   * @return
   */
  public static final String getNodeValue(Node node, String name, String def) {
    Node c = (node != null) ? node.getFirstChild() : null;
    while (c != null) {
      String s = c.getNodeName();
      if (s != null && s.compareTo(name) == 0) {
        return getNodeValue(c, def);
      }
      c = c.getNextSibling();
    }
    return def;
  }
  /**
   * ����node��ֱ���ӽڵ㣬����нڵ����Ƶ���name���򷵻���ֵ�����򷵻�def
   * @param node  �����Ľڵ�
   * @param name �ڵ������
   * @param def ȱʡ�ķ���ֵ
   * @return
   */

  public static final boolean getNodeBoolValue(Node node, String name,boolean def){
    String s = getNodeValue(node,name,null);
    if (s==null){
      return def;
    }
    if (s.equalsIgnoreCase("true")){
      return true;
    }
    if (s.equalsIgnoreCase("false")){
      return false;
    }
    return def;
  }
  /**
   * ��node�������ӽڵ㣬����ӽڵ������Ϊname��ֵΪvalue
   * 
   * @param node ��Ҫ����ӽڵ�ĵ�
   * @param name ��ӵ��ֽڵ�����
   * @param value ��ӵ��ֽڵ��ֵ
   */

  public static final void addNodeValue(Node node, String name,String value){
    if (value!=null){
      Document doc = node.getOwnerDocument();
      Element e = doc.createElement(name);
      e.appendChild(doc.createTextNode(value));
      node.appendChild(e);
    }
  }
  
  /**
   * ��node�����ӽڵ�,�ڵ�����Ϊname,�ڵ��´��CDATA�ڵ�,CDATA������Ϊvalue
   */
  public static final void addNodeCDATAValue(Node node, String name,String value){
    if (value!=null){
      Document doc = node.getOwnerDocument();
      Element e = doc.createElement(name);
      e.appendChild(doc.createCDATASection(value));
      node.appendChild(e);
    }
  }
  /**
   * ��node�������ӽڵ㣬����ӽڵ������Ϊname��ֵΪvalue
   * @param node  �����Ľڵ�
   * @param name �ӽڵ������
   * @param value �ӽڵ��ֵ
   */

  public static final void addNodeValue(Node node, String name,boolean value){
    addNodeValue(node,name,value?"true":"false");
  }

  /**
   * �õ�node��Ӧ��ֵ������
   * <data>abc</data>����abc
   * <data><![CDATA[123]]></data>����123
   * 
   * 20081206 ԭ���������Node.getTextContent()��ȡֵ���������������jdk1.4��û�У�����ֱ�ӷ���null��
   * 
   * @param node
   * @param def
   * @return
   */
	public static final String getNodeValue(Node node, String def) {
		if (node == null)
			return def;
		//�������Ѿ����ı����,����Ҫ���ӽڵ��л���ı�,ֱ�ӷ��ؽڵ������
		if (node.getNodeType() == Node.CDATA_SECTION_NODE || node.getNodeType() == Element.TEXT_NODE) {
			return node.getNodeValue();
		}
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node n = childNodes.item(i);
			if (n.getNodeType() == Element.CDATA_SECTION_NODE || n.getNodeType() == Element.TEXT_NODE) {
				return n.getNodeValue();
			}
		}

		/**
		 * 20081124 ���ﲻ�ܵ���node.getNodeValue()����һ��Element��������nodevalueΪnull��
		 * TODO �������getTextContent��jdk1.4�б���ͨ������
		 */
		return def;
	}

	/**
	 * ��ȡĳһ�ڵ��µĴ��ı��ֶ���Ϣ��ͬgetNodeValue���������������ֻ��ȡCDATA_SECTION_NODE���͵��ı���Ϣ
	 * 20100903 ����ж��������CDATA�ӽڵ�,�������Щ�ӽڵ���������.��Ϊ�����CDATAǶ�׵��������,���CDATA�µ�CDATA�ֳɶ��CDATA.
	 *    ��:<test><![CDATA[a]]></test>���뵽һ��CDATA�к����<test><![CDATA[a]]]]><![CDATA[></test>]]>   
	 * 
	 * ���ﴦ��ķ�ʽ��:��node������CDATA�ڵ�,���û���ҵ�,�򷵻�defֵ,����ҵ�,�򽫵�һ��������CDATA�����ݷ���.������CDATA��ָCDATA�ڵ��û���������͵Ľڵ�
	 *    
	 * <data><![CDATA[content]]></data>
	 * @param node
	 * @param def
	 * @return
	 */
	public static final String getItemCDATAContent(Node node, String def) {
		NodeList childNodes = node.getChildNodes();
		StringBuffer buf = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.CDATA_SECTION_NODE) {
				if (buf == null) {
					buf = new StringBuffer();
				}
				buf.append(((CDATASection) item).getData());
			}
			else if (buf != null) {
				break;//CDATA�ж�,������������CDATA�ڵ�
			}
		}
		return buf == null ? def : buf.toString();
	}
  
  /**
   * ����ĳ�ڵ��µĴ��ı��ڵ����ݣ���������Ϊ 123
   * <data></data> ���ú�Ϊ <data><![CDATA[123]]></data>
   * <data><![CDATA[aaa]]></data>���ú�Ϊ<data><![CDATA[123]]></data>
   * @param node
   * @param content
   */
  public static final void setItemCDATAContent(Node node, String content){
    NodeList childNodes = node.getChildNodes();
    for (int i = 0, len = childNodes == null ? 0 : childNodes.getLength();  i <len ; i++) {
      Node item = childNodes.item(i);
      if(item.getNodeType() == Node.CDATA_SECTION_NODE){
        ((CDATASection)item).setData(content);
        return ;
      }
    }
    
    /**
     * ����ڵ��²����ڴ��ı���Ϣ����ô���ڸýڵ������һ�����ı��ڵ�
     */
    Document doc = node.getOwnerDocument();
    CDATASection cdata = doc.createCDATASection(content);
    node.appendChild(cdata);
  }
  
  /**
   * ����node��Ӧ��ֵ,���value=null��ת��Ϊvalue=""
   * ��:value="123" 
   *   <data>abc</data> ���ú�Ϊ<data>123</data>
   *   <data><![CDATA[abc]]></data>���ú�Ϊ<data><![CDATA[123]]></data>
   *   <data><innerNode>abc</innerNode></data> ���ú���Ч
   *   ע��:<data><![CDATA[abc]]></data>���ú�value=""��Ϊ<data></data>
   */
  public static final void setNodeValue(Node node, String value) {
    if (value == null)
      value = "";
    NodeList childNodes = node.getChildNodes();
    int len = childNodes == null ? 0 : childNodes.getLength();
    for (int i = 0; i < len; i++) {
      Node n = childNodes.item(i);
      n.setNodeValue(value);
    }
  }

  /**
   * ����Ԫ�ص����ԣ����ֵΪ�գ������ã���ô��ɾ��������
   *  
   * @param e
   * @param name
   * @param value
   */
  public static final void setElementAttribute(Element e, String name, String value){
	//wuhao 2011/11/20 ���ַ�����������������ģ�������cloneʱ����ø÷�������˴˴�ֻ�ж�null
    if(value != null){
      e.setAttribute(name, value);
    }else{
      e.removeAttribute(name);
    }
  }
  
  private static final String JAVAX_XML_TRANSFORM_TRANSFORMER_FACTORY = "javax.xml.transform.TransformerFactory";
  
  public static final void fixTransformerFactory(){
    //���û���������д��룬��jdk1.5�ϻ����
    try {
      if (System.getProperty("java.version").startsWith("1.5")) {
        /**
         * ��websphere��ԭ����javax.xml.transform.TransformerFactory�Ϳ����ã�
         * ��tomcat����Ҫ�ĳ�com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl
         */
        String oldcls = System.getProperty(JAVAX_XML_TRANSFORM_TRANSFORMER_FACTORY);
        try {
          Class.forName(oldcls);
        }
        catch (Throwable ta) {
          //ta.printStackTrace();
          if (System.getProperty("java.vendor").startsWith("IBM")) {
            System.setProperty(JAVAX_XML_TRANSFORM_TRANSFORMER_FACTORY,
                "org.apache.xalan.processor.TransformerFactoryImpl");
          } else {
            System.setProperty(JAVAX_XML_TRANSFORM_TRANSFORMER_FACTORY,
                "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
          }
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
	/**
	 * ��¡һ���ڵ㣬����¡�ýڵ㱾�����Ըýڵ��µĺ��ӽڵ㡣
	 * @param doc ���ڴ����½ڵ��Document����
	 * @param srcNode ԭ�ڵ�
	 * @return �÷�����֧�ֿ�¡���ֽڵ㣺Ԫ�ؽڵ㡢�ı��ڵ㡢ע�ͽڵ㡢CDATA�ڵ��Լ����Խڵ㡣
	 */
	public static Node cloneNode(Document doc, Node srcNode) {
		return cloneNode(doc, srcNode, 0);
	}

	/**
	 * ��¡һ���ڵ�
	 * @param doc ���ڴ����½ڵ��Document����
	 * @param srcNode ԭ�ڵ�
	 * @param recur �Ƿ�ݹ��¡�ӽڵ㣬�ò���Ϊtrueʱ���ݹ��¡������ڵ㣬���ֽڵ�ṹ����
	 * @return
	 */
	public static Node cloneNode(Document doc, Node srcNode, boolean recur) {
		int level = recur ? Integer.MAX_VALUE : 0;
		return cloneNode(doc, srcNode, level);
	}

	/**
	 * ��¡һ���ڵ�
	 * @param doc ���ڴ����½ڵ��Document����
	 * @param srcNode ԭ�ڵ�
	 * @param level �ݹ��¡������Σ�0��ʾ��ǰ�㡣
	 * @return
	 */
	public static Node cloneNode(Document doc, Node srcNode, int level) {
		int nodeType = srcNode.getNodeType();
		Node destNode = null;
		switch (nodeType) {
			case Node.ELEMENT_NODE:
				destNode = doc.createElement(srcNode.getNodeName());
				break;
			case Node.TEXT_NODE:
				destNode = doc.createTextNode(srcNode.getNodeValue());
				break;
			case Node.ATTRIBUTE_NODE:
				destNode = doc.createAttribute(srcNode.getNodeName());
				destNode.setNodeValue(srcNode.getNodeValue());
				break;
			case Node.COMMENT_NODE:
				destNode = doc.createComment(srcNode.getNodeValue());
				break;
			case Node.CDATA_SECTION_NODE:
				destNode = doc.createCDATASection(srcNode.getNodeValue());
				break;
			default:
				return null;
		}
		if (srcNode.getNodeType() == Node.ELEMENT_NODE) {
			if (srcNode.hasAttributes()) {
				NamedNodeMap nodeMap = srcNode.getAttributes();
				int len = nodeMap.getLength();
				int i = 0;
				while (i < len) {
					Node att = nodeMap.item(i);
					XmlFunc.setElementAttribute((Element) destNode, att.getNodeName(), att.getNodeValue());
					i++;
				}
			}
			if (level > 0 && srcNode.hasChildNodes()) {
				Node child = srcNode.getFirstChild();
				while (child != null) {
					Node newChild = cloneNode(doc, child, level - 1);
					if (newChild != null) {
						destNode.appendChild(newChild);
					}
					child = child.getNextSibling();
				}
			}
		}
		return destNode;
	}

	/**
	 * ���ҵ�ǰ�ĵ��´���ָ��id ���Ե� Element����������Ȳ�ѯ
	 * <br>
	 * ע��DOM 1.0��û������ʵ��getElementById�������ʿ�ͨ���÷���ʵ�֡�
	 * @param doc
	 * @param id
	 * @return ���ҵ��ĵ�һ��Ԫ�أ����Ҳ���ʱ������null��
	 */
	public static Element getElementByIdInDepth(Document doc, String id) {
		return getElementByAttributeInDepth(doc, "id", id);
	}
	
	/**
	 * ��ָ��Ԫ�ؽڵ��²��Ҵ���id���Ե�����ڵ㣬������Ȳ�ѯ
	 * @param parent
	 * @param id
	 * @return ���ҵ��ĵ�һ��Ԫ�أ����Ҳ���ʱ������null��
	 */
	public static Element getChildNodeByIdInDepth(Element parent, String id) {
		return getChildNodeByAttributeInDepth(parent, "id", id);
	}
	
	/**
	 * ���ҵ�ǰ�ĵ��¾���ָ�����ԣ��Ҹ�����ֵ��ָ��ֵ��ȵĽڵ㣬������Ȳ�ѯ
	 * @param doc
	 * @param attName
	 * @param attValue
	 * @return ���ҵ��ĵ�һ��Ԫ�أ����Ҳ���ʱ������null��
	 */
	public static Element getElementByAttributeInDepth(Document doc, String attName, String attValue) {
		if (StrFunc.isNull(attName) || StrFunc.isNull(attValue)) {
			//throw new RuntimeException("��������Ϊ��");
			throw new RuntimeException(I18N.getString("com.esen.util.XmlFunc.3", "��������Ϊ��"));
		}
		Element elem = XmlFunc.getRootElement(doc);
		if (elem == null) {
			return null;
		}
		String value = elem.getAttribute(attName);
		if (!StrFunc.isNull(value) && value.equals(attValue)) {
			return elem;
		}
		else {
			return getChildNodeByAttributeInDepth(elem, attName, attValue);
		}
	}
	
	/**
	 * ��ָ��Ԫ�ؽڵ��²��Ҿ���ָ�����ԣ��Ҹ�����ֵ��ָ��ֵ��ȵĽڵ㣬������Ȳ�ѯ
	 * @param parent
	 * @param id
	 * @return ���ҵ��ĵ�һ��Ԫ�أ����Ҳ���ʱ������null��
	 */
	public static Element getChildNodeByAttributeInDepth(Element parent, String attName, String attValue) {
		if (StrFunc.isNull(attName) || StrFunc.isNull(attValue)) {
			//throw new RuntimeException("��������Ϊ��");
			throw new RuntimeException(I18N.getString("com.esen.util.XmlFunc.4", "��������Ϊ�� "));
		}
		if (!parent.hasChildNodes()) {
			return null;
		}
		Node node = parent.getFirstChild();
		while (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.hasAttributes()) {
					String value = ((Element) node).getAttribute(attName);
					if (!StrFunc.isNull(value) && value.equals(attValue)) {
						return (Element) node;
					}
				}
				if (node.hasChildNodes()) {
					Element elem = getChildNodeByAttributeInDepth((Element) node, attName, attValue);
					if (elem != null) {
						return elem;
					}
				}
			}
			node = node.getNextSibling();
		}
		return null;
	}
  
  public static Element getRootElement(Document doc){
	  Node node = doc.getFirstChild();
	  while (node != null) {
		  if (node.getNodeType() == Node.ELEMENT_NODE)
			  return (Element) node;
		  node = node.getNextSibling(); 
	  }
	  return null;
  }
  
	public static final List<Element> getChildElements(Element element) {
		List<Element> childElements = new ArrayList<Element>();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				childElements.add((Element) item);
			}
		}
		return childElements;
	}
}
