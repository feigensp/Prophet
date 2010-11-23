package test;
import java.awt.*;
import javax.swing.*;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class Rsyntaxtest extends JFrame {

   private static final long serialVersionUID = 1L;


   public Rsyntaxtest() {

      JPanel cp = new JPanel(new BorderLayout());

      RSyntaxTextArea textArea = new RSyntaxTextArea();
      textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
      RTextScrollPane sp = new RTextScrollPane(textArea);
      cp.add(sp);

      setContentPane(cp);
      setTitle("RSyntaxTextArea 1.4 - Example 1 - Text Editor Demo");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      pack();
      setLocationRelativeTo(null);

   }

   public static void main(String[] args) {
      // Start all Swing applications on the EDT.
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new Rsyntaxtest().setVisible(true);
         }
      });
   }

}

