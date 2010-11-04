package questionViewer;

import java.util.ArrayList;

public class AttributeArrayList<T> extends ArrayList<T>{

	ArrayList<StringTupel> attributes;
	
	public AttributeArrayList() {
		super();
		
		attributes = new ArrayList<StringTupel>();
	}
	
	public void addAttribute(StringTupel st) {
		attributes.add(st);
	}
	
	public StringTupel getAttribute(int index) {
		return attributes.get(index);
	}
	
	public ArrayList<StringTupel> getAttributes() {
		return attributes;
	}
}
