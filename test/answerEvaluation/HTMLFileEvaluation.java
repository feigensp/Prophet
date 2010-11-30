package test.answerEvaluation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class HTMLFileEvaluation {

	public static final String[] ATTRIBUTES = { "id", "type", "weight", "answer" };
	public static final int[] PRIMARY_ATTRIBUTES = { 0, 2, 3 };

	public static ArrayList<HashMap<String, String>> storeAnswerSpecifications(String htmlContent) {
		ArrayList<HashMap<String, String>> elements = new ArrayList<HashMap<String, String>>();
		// prepare analysis
		HTMLEditorKit kit = new HTMLEditorKit();
		HTMLDocument doc = (javax.swing.text.html.HTMLDocument) kit.createDefaultDocument();
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		try {
			StringReader sr = new StringReader(htmlContent);
			kit.read(sr, doc, 0);
			HTMLDocument.Iterator tagIterator = doc.getIterator(HTML.Tag.INPUT);
			while (tagIterator.isValid()) {
				// store attributes of the current tag
				AttributeSet as = tagIterator.getAttributes();
				Enumeration e = as.getAttributeNames();
				String[] attributesContent = new String[ATTRIBUTES.length];
				while (e.hasMoreElements()) {
					Object name = e.nextElement();
					// System.out.print(name + ":" + as.getAttribute(name) +
					// " - ");
					for (int i = 0; i < ATTRIBUTES.length; i++) {
						if (name.toString().equals(ATTRIBUTES[i])) {
							attributesContent[i] = as.getAttribute(name).toString();
						}
					}
				}
				// System.out.println();
				// check if primary attributes are available
				boolean store = true;
				for (int i = 0; i < PRIMARY_ATTRIBUTES.length; i++) {
					if (attributesContent[PRIMARY_ATTRIBUTES[i]] == null) {
						store = false;
						break;
					}
				}
				// if primary attributes are present store the important
				// attributes
				if (store) {
					HashMap<String, String> hm = new HashMap<String, String>();
					for (int i = 0; i < ATTRIBUTES.length; i++) {
						hm.put(ATTRIBUTES[i], attributesContent[i]);
					}
					elements.add(hm);
				}
				tagIterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

}
