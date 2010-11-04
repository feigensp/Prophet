package test;

/*
 * HTMLFormular.java
 */
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;


public class HTMLFormular extends JEditorPane {
	private final static String VORNAME = "Vorname";
	private final static String NACHNAME = "Nachname";
	private final static String GESCHLECHT = "Geschlecht";
	private final static String BEMERKUNG = "Bemerkung";
	

	public HTMLFormular() {
		setEditable(false);
		setEditorKit(new HTMLEditorKit() {
			public ViewFactory getViewFactory() {
				return new HTMLEditorKit.HTMLFactory() {
					public View create(Element elem) {
						Object o = elem.getAttributes().getAttribute(
								StyleConstants.NameAttribute);
						if (o instanceof HTML.Tag) {
							HTML.Tag kind = (HTML.Tag) o;
							if (kind == HTML.Tag.INPUT)
								return new FormView(elem) {
									protected void submitData(String data) {
										System.out.println(data);
										System.out.println("___");
										setData(data);
										showData();
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});
	}

	private void showData() {
		JOptionPane.showMessageDialog(null,
				VORNAME + ": " + resultMap.get(VORNAME) + "\n" + NACHNAME
						+ ": " + resultMap.get(NACHNAME) + "\n" + GESCHLECHT
						+ ": " + resultMap.get(GESCHLECHT) + "\n" + BEMERKUNG
						+ ": " + resultMap.get(BEMERKUNG));
	}

	private void setData(String data) {
		resultMap = new HashMap<String, String>();
		StringTokenizer st = new StringTokenizer(data, "&");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			String key = null;
			String value = null;
			try {
				key = URLDecoder.decode(token.substring(0, token.indexOf("=")),
						"ISO-8859-1");
				value = URLDecoder
						.decode(token.substring(token.indexOf("=") + 1,
								token.length()), "ISO-8859-1");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			resultMap.put(key, value);
		}
	}

	public Map<String, String> getResult() {
		return resultMap;
	}

	private Map<String, String> resultMap;
}
