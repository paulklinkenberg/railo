package railo.runtime.text.xml.struct;

import java.lang.reflect.Method;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import railo.runtime.exp.PageRuntimeException;
import railo.runtime.op.Caster;
import railo.runtime.type.Collection;
import railo.runtime.type.util.ArrayUtil;


/**
 * 
 */
public class XMLElementStruct extends XMLNodeStruct implements Element {
	
	
	private Element element;
    
	/**
	 * constructor of the class
	 * @param element
	 * @param caseSensitive
	 */
	protected XMLElementStruct(Element element, boolean caseSensitive) {
		super(element instanceof XMLElementStruct?element=((XMLElementStruct)element).getElement():element, caseSensitive);
        this.element=element;
	}
	
	@Override
	public String getTagName() {
		return element.getTagName();
	}
	@Override
	public void removeAttribute(String name) throws DOMException {
		element.removeAttribute(name);
	}
	@Override
	public boolean hasAttribute(String name) {
		return element.hasAttribute(name);
	}
	@Override
	public String getAttribute(String name) {
		return element.getAttribute(name);
	}
	@Override
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		element.removeAttributeNS(namespaceURI,localName);
	}
	@Override
	public void setAttribute(String name, String value) throws DOMException {
		element.setAttribute(name,value);
	}
	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName) {
		return element.hasAttributeNS(namespaceURI,localName);
	}
	@Override
	public Attr getAttributeNode(String name) {
		return element.getAttributeNode(name);
	}
	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		return element.removeAttributeNode(oldAttr);
	}
	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		return element.setAttributeNode(newAttr);
	}
	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		return element.setAttributeNodeNS(newAttr);
	}
	@Override
	public NodeList getElementsByTagName(String name) {
		return element.getElementsByTagName(name);
	}
	@Override
	public String getAttributeNS(String namespaceURI, String localName) {
		return element.getAttributeNS(namespaceURI,localName);
	}
	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName,String value) throws DOMException {
		element.setAttributeNS(namespaceURI,qualifiedName,value);
	}
	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName) {
		return element.getAttributeNodeNS(namespaceURI,localName);
	}
	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return element.getElementsByTagNameNS(namespaceURI,localName);
	}
	
    @Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
    	// dynamic load to support jre 1.4 and 1.5
		try {
			Method m = element.getClass().getMethod("setIdAttribute", new Class[]{name.getClass(),boolean.class});
			m.invoke(element, new Object[]{name,Caster.toBoolean(isId)});
		} 
		catch (Exception e) {
			throw new PageRuntimeException(Caster.toPageException(e));
		}
    }
    
    @Override
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
    	// dynamic load to support jre 1.4 and 1.5
		try {
			Method m = element.getClass().getMethod("setIdAttributeNS", new Class[]{namespaceURI.getClass(),localName.getClass(),boolean.class});
			m.invoke(element, new Object[]{namespaceURI,localName,Caster.toBoolean(isId)});
		} 
		catch (Exception e) {
			throw new PageRuntimeException(Caster.toPageException(e));
		}
    }
    
    @Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
    	// dynamic load to support jre 1.4 and 1.5
		try {
			Method m = element.getClass().getMethod("setIdAttributeNode", new Class[]{idAttr.getClass(),boolean.class});
			m.invoke(element, new Object[]{idAttr,Caster.toBoolean(isId)});
		} 
		catch (Exception e) {
	        element.setAttributeNodeNS(idAttr);
		}
    }
    
	@Override
	public TypeInfo getSchemaTypeInfo() {
    	// dynamic load to support jre 1.4 and 1.5
		try {
			Method m = element.getClass().getMethod("getSchemaTypeInfo", new Class[]{});
			return (TypeInfo) m.invoke(element, ArrayUtil.OBJECT_EMPTY);
		} 
		catch (Exception e) {
			throw new PageRuntimeException(Caster.toPageException(e));
		}
	}
	/**
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}
	
	@Override
	public Collection duplicate(boolean deepCopy) {
		return new XMLElementStruct((Element)element.cloneNode(deepCopy),caseSensitive);
	}
	

	@Override
	public Node cloneNode(boolean deep) {
		return new XMLElementStruct((Element)element.cloneNode(deep),caseSensitive);
	}
}