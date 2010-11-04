package htmlTest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JEditorPane;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class HTMLFormular extends JEditorPane{
    
    private Map<String, String> resultMap;
    HTMLEditorKit htmlEditKit;
    
    public HTMLFormular(){
        setEditable(false);
        
        htmlEditKit = new HTMLEditorKit() {
            public ViewFactory getViewFactory() {
                return new HTMLEditorKit.HTMLFactory() {
                    public View create(Element elem) {
                        Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
                        if (o instanceof HTML.Tag) {
                            HTML.Tag kind = (HTML.Tag) o;
                            if (kind == HTML.Tag.INPUT )
                                return new FormView(elem) {
                                    protected void submitData(String data) {
                                        setData(data);
                                    }
                                };
                        }
                        return super.create(elem);
                    }
                };
            }
        };
        setEditorKit(htmlEditKit);
    }
    
    private void setData(String data){
        resultMap = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(data, "&");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String key = null;
            String value = null;
            try {
                key = URLDecoder.decode(token.substring(0, token.indexOf("=")), "ISO-8859-1");
                value = URLDecoder.decode(token.substring(token.indexOf("=") + 1, token.length()), "ISO-8859-1");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            resultMap.put(key,value);
        }
        firePropertyChange("data", null, data);
    }
    
    public Map<String, String> getResult() {
        return resultMap;
    }
}