package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

class SyntaxDocument extends DefaultStyledDocument {
	public static final String STRING_QUOTE_DELIMITER = "\"";
	public static final String CHAR_QUOTE_DELIMITER = "'";
	public static final String SINGLE_LINE_COMMENT_DELIMITER = "//";
	public static final String MULTI_LINE_COMMENT_OPEN_DELIMITER = "/*";
	public static final String MULTI_LINE_COMMENT_CLOSE_DELIMITER = "*/";
	
	public static final Color SINGLE_LINE_COMMENT_COLOR = new Color(63, 127, 95);
	public static final Color QUOTE_COLOR = new Color(0, 0, 192);
	public static final Color KEYWORD_COLOR = new Color(127, 0, 85);

	private DefaultStyledDocument doc;
	private Element rootElement;

	private MutableAttributeSet normal;
	private MutableAttributeSet keyword;
	private MutableAttributeSet comment;
	private MutableAttributeSet quote;

	private HashSet<String> keywords;

	public SyntaxDocument() {
		doc = this;
		rootElement = doc.getDefaultRootElement();
		putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

		normal = new SimpleAttributeSet();
		StyleConstants.setForeground(normal, Color.black);

		comment = new SimpleAttributeSet();
		StyleConstants.setForeground(comment, SINGLE_LINE_COMMENT_COLOR);

		keyword = new SimpleAttributeSet();
		StyleConstants.setForeground(keyword, KEYWORD_COLOR);
		StyleConstants.setBold(keyword, true);

		quote = new SimpleAttributeSet();
		StyleConstants.setForeground(quote, QUOTE_COLOR);

		keywords = new HashSet<String>();
		keywords.add("abstract");
		keywords.add("boolean");
		keywords.add("break");
		keywords.add("byte");
		keywords.add("byvalue");
		keywords.add("case");
		keywords.add("cast");
		keywords.add("catch");
		keywords.add("char");
		keywords.add("class");
		keywords.add("const");
		keywords.add("continue");
		keywords.add("default");
		keywords.add("do");
		keywords.add("double");
		keywords.add("else");
		keywords.add("extends");
		keywords.add("false");
		keywords.add("final");
		keywords.add("finally");
		keywords.add("float");
		keywords.add("for");
		keywords.add("future");
		keywords.add("generic");
		keywords.add("goto");
		keywords.add("if");
		keywords.add("implements");
		keywords.add("import");
		keywords.add("inner");
		keywords.add("instanceof");
		keywords.add("int");
		keywords.add("interface");
		keywords.add("long");
		keywords.add("native");
		keywords.add("new");
		keywords.add("null");
		keywords.add("operator");
		keywords.add("outer");
		keywords.add("package");
		keywords.add("private");
		keywords.add("protected");
		keywords.add("public");
		keywords.add("rest");
		keywords.add("return");
		keywords.add("short");
		keywords.add("static");
		keywords.add("super");
		keywords.add("switch");
		keywords.add("synchronized");
		keywords.add("this");
		keywords.add("throw");
		keywords.add("throws");
		keywords.add("transient");
		keywords.add("true");
		keywords.add("try");
		keywords.add("var");
		keywords.add("void");
		keywords.add("volatile");
		keywords.add("while");
	}

	/*
	 * Override to apply syntax highlighting after the document has been updated
	 */
	public void insertString(int offset, String str, AttributeSet a)
			throws BadLocationException {
		super.insertString(offset, str, a);
		processChangedLines(offset, str.length(), str);
	}

	/*
	 * Override to apply syntax highlighting after the document has been updated
	 */
	public void remove(int offset, int length) throws BadLocationException {
		super.remove(offset, length);
		processAllLines(offset, 0);
	}

	/*
	 * Determine how many lines have been changed, then apply highlighting to
	 * each line
	 */
	public void processAllLines(int offset, int length)
			throws BadLocationException {
		String content = doc.getText(0, doc.getLength());

		// The lines affected by the latest document update
		int startLine = rootElement.getElementIndex(0);
		int endLine = rootElement.getElementIndex(content.length());
		// Do the actual highlighting
		for (int i = startLine; i <= endLine; i++) {
			applyHighlighting(content, i);
		}
		//test whole document for multiLineComments
		int startIndex = content.indexOf(MULTI_LINE_COMMENT_OPEN_DELIMITER, 0);
		int endIndex = 0;
		while(startIndex != -1) {
			//wenn foreground == quote style ignorier
			if(!doc.getCharacterElement(startIndex).getAttributes().containsAttributes(quote)) {
				endIndex = content.indexOf(MULTI_LINE_COMMENT_CLOSE_DELIMITER, startIndex);
				if(endIndex == -1) {
					doc.setCharacterAttributes(startIndex, content.length()-startIndex, comment, true);		
					break;
				} else {
					doc.setCharacterAttributes(startIndex, endIndex-startIndex+2, comment, true);
					startIndex = content.indexOf(MULTI_LINE_COMMENT_OPEN_DELIMITER, endIndex+2);
				}
			} else {
				startIndex = content.indexOf(MULTI_LINE_COMMENT_OPEN_DELIMITER, startIndex+2);
			}
		}
	}

	/*
	 * Determine how many lines have been changed, then apply highlighting to
	 * each line
	 */
	public void processChangedLines(int offset, int length, String str)
			throws BadLocationException {
		String content = doc.getText(0, doc.getLength());

		// The lines affected by the latest document update
		int startLine = rootElement.getElementIndex(offset);
		int endLine = rootElement.getElementIndex(offset + length);
		//wenn veränderte zeilen mehrzeiligen kommentar enthalten alle Zeilen anpassen
		if(str.contains("/")) {
			processAllLines(offset, length);
			return;
		}
		// Do the actual highlighting
		for (int i = startLine; i <= endLine; i++) {
			applyHighlighting(content, i);
		}
		//test whole document for multiLineComments
		int startIndex = content.indexOf(MULTI_LINE_COMMENT_OPEN_DELIMITER, 0);
		int endIndex = 0;
		while(startIndex != -1) {
			//wenn foreground == quote style ignorier
			if(!doc.getCharacterElement(startIndex).getAttributes().containsAttributes(quote)) {
				endIndex = content.indexOf(MULTI_LINE_COMMENT_CLOSE_DELIMITER, startIndex);
				if(endIndex == -1) {
					doc.setCharacterAttributes(startIndex, content.length()-startIndex, comment, true);		
					break;
				} else {
					doc.setCharacterAttributes(startIndex, endIndex-startIndex+2, comment, true);
					startIndex = content.indexOf(MULTI_LINE_COMMENT_OPEN_DELIMITER, endIndex+2);
				}
			} else {
				startIndex = content.indexOf(MULTI_LINE_COMMENT_OPEN_DELIMITER, startIndex+2);
			}
		}
	}

	/*
	 * Parse the line to determine the appropriate highlighting
	 */
	private void applyHighlighting(String content, int line)
			throws BadLocationException {
		int startOffset = rootElement.getElement(line).getStartOffset();
		int endOffset = rootElement.getElement(line).getEndOffset() - 1;
		int lineLength = endOffset - startOffset;
		if (endOffset >= content.length()) {
			endOffset = content.length() - 1;
		}
		int tempOffset = startOffset;
		// set normal attributes for the line
		doc.setCharacterAttributes(startOffset, lineLength, normal, true);

		// check for special highlighting
		while (startOffset <= endOffset) {
			// skip the delimiters to find the start of a new token
			while (isDelimiter(content.substring(startOffset, startOffset + 1))) {
				if (startOffset < endOffset)
					startOffset++;
				else
					return;
			}
			if (STRING_QUOTE_DELIMITER.equals(content.substring(
					startOffset, startOffset + 1))) {
				tempOffset = startOffset + 1;
				// go on until you find the closing delimiter
				while (tempOffset < endOffset
						&& (!STRING_QUOTE_DELIMITER.equals(content.substring(
								tempOffset, tempOffset + 1)) || "\\".equals(content.substring(tempOffset-1, tempOffset)))) {
					tempOffset++;
				}
				doc.setCharacterAttributes(startOffset, tempOffset
						- startOffset+1, quote, true);
				startOffset = tempOffset + 1;
			}
			// else look if it starts with a charQuote
			else if (CHAR_QUOTE_DELIMITER.equals(content.substring(startOffset,
					startOffset + 1))) {
				tempOffset = startOffset + 1;
				// go on until you find the closing delimiter
				while (tempOffset < endOffset
						&& (!CHAR_QUOTE_DELIMITER.equals(content.substring(
								tempOffset, tempOffset + 1)) || "\\".equals(content.substring(tempOffset-1, tempOffset)))) {
					tempOffset++;
				}
//				// go on until you find the closing delimiter
//				while (tempOffset < endOffset
//						&& (!CHAR_QUOTE_DELIMITER.equals(content.substring(
//								tempOffset, tempOffset + 1)) || (!CHAR_QUOTE_DELIMITER.equals(content.substring(
//										tempOffset, tempOffset + 1)) && "\\".equals(content.substring(tempOffset-1, tempOffset))))) {
//					tempOffset++;
//				}
				doc.setCharacterAttributes(startOffset, tempOffset
						- startOffset+1, quote, true);
				startOffset = tempOffset + 1;
			}
			// else look if the next token is a keywort
			else {
				tempOffset = startOffset + 1;
				// get end of token
				while (tempOffset < endOffset) {
					if (isDelimiter(content.substring(tempOffset,
							tempOffset + 1))) {
						break;
					}
					tempOffset++;
				}

				String token = content.substring(startOffset, tempOffset);
				//token nach kommentaren durchsuchen
				int singleLineCommentPos = token.indexOf(SINGLE_LINE_COMMENT_DELIMITER);
				if(singleLineCommentPos != -1) {
					doc.setCharacterAttributes(startOffset+singleLineCommentPos, endOffset-(startOffset+singleLineCommentPos), comment, true);
					return;
				} 
				//token auf schlüsselwort prüfen
				if (keywords.contains(token)) {
					doc.setCharacterAttributes(startOffset, tempOffset
							- startOffset, keyword, false);
				}
				startOffset = tempOffset + 1;
			}
		}
	}

	/*
	 * Override for other languages
	 */
	protected boolean isDelimiter(String character) {
		String operands = ";:{}()[]+-*%<=>!&|^~";

		if (Character.isWhitespace(character.charAt(0))
				|| operands.indexOf(character) != -1)
			return true;
		else
			return false;
	}

	public static void main(String a[]) {

		EditorKit editorKit = new StyledEditorKit() {
			public Document createDefaultDocument() {
				return new SyntaxDocument();
			}
		};

		final JEditorPane edit = new JEditorPane();
		edit.setEditorKit(editorKit);
		// edit.setEditorKit(new StyledEditorKit());
		// edit.setDocument(new SyntaxDocument());

		JButton button = new JButton("Load SyntaxDocument.java");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileInputStream fis = new FileInputStream(
							"src\\test\\SyntaxDocument.java");
					// FileInputStream fis = new FileInputStream(
					// "C:\\Java\\jdk1.4.1\\src\\javax\\swing\\JComponent.java"
					// );
					edit.read(fis, null);
					edit.requestFocus();
				} catch (Exception e2) {
				}
			}
		});

		JFrame frame = new JFrame("Syntax Highlighting");
		frame.getContentPane().add(new JScrollPane(edit));
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 300);
		frame.setVisible(true);
	}
}
