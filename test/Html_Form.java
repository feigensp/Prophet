package test;
/*
 * Html_Form.java
 *
 * Das HTML Formular ist eine interessante Alternative zu einem reinen Swing Formular.
 *
 */
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
public class Html_Form extends JFrame {
    public Html_Form() {
        super("Html Formular");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700,300);
        setLocationRelativeTo(null);
        formular = new HTMLFormular();
        formular.setText(FORM_TEXT);
        formular.addPropertyChangeListener("data", new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                Map<String, String> result = formular.getResult();
                vorname = result.get(VORNAME);
                nachname = result.get(NACHNAME);
                geschlecht = result.get(GESCHLECHT);
                bemerkung = result.get(BEMERKUNG);
                showData();
            }
        });
        getContentPane().add(new JScrollPane(formular));
    }
    private void showData(){
        JOptionPane.showMessageDialog(null,
                VORNAME+": "+vorname+"\n"+
                NACHNAME+": "+nachname+"\n"+
                GESCHLECHT+": "+geschlecht+"\n"+
                BEMERKUNG+": "+bemerkung);
    }
    public static void main(String args[]) {new Html_Form().setVisible(true);}
    private HTMLFormular formular;
    private String vorname, nachname, geschlecht, bemerkung;
    private final static String VORNAME = "Vorname";
    private final static String NACHNAME = "Nachname";
    private final static String GESCHLECHT = "Geschlecht";
    private final static String BEMERKUNG = "Bemerkung";
    private final static String FORM_TEXT =
            "<form>" +
            "<table>"+
            "<tr>"+
            "       <td>"+VORNAME+":</td>"+
            "       <td><input name='"+VORNAME+"' type='text' value='Andre'></td>"+
            "</tr>"+
            "<tr>"+
            "       <td>"+NACHNAME+":</td>"+
            "       <td><input name='"+NACHNAME+"' type='text' value='Uhres'></td>"+
            "</tr>"+
            "<tr>"+
            "       <td>"+GESCHLECHT+":</td>"+
            "       <td><select name='"+GESCHLECHT+"'> <option>Männlich<option>Weiblich</select></td>"+
            "</tr>"+
            "<tr>"+
            "       <td>"+BEMERKUNG+":</td>"+
            "       <td><textarea name='"+BEMERKUNG+"' rows=5 cols=50 >" +
                   "Ich spreche deutsch, französisch und englisch</textarea></td>"+
            "</tr>"+
            "<tr>"+
            "       <td></td>"+
            "       <td><hr></td>"+
            "</tr>"+
            "<tr>"+
            "       <td></td>"+
            "       <td><input type='submit'></td>"+
            "</tr>"+
            "</table>" +
            "</form>";
}