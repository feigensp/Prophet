package experimentGUI.util.questionTreeNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import experimentGUI.util.QuestionViewPane;

public class QuestionTreeNodeHTMLHandler {

	public static final int EXPERIMENT_LEVEL = 0;
	public static final int CATEGORY_LEVEL = 1;
	public static final int QUESTION_LEVEL = 2;

	public static void saveAsHTMLFile(File file, QuestionTreeNode node) {
		String nodeName = node.getName();
		String htmlContent = createHTMLContent(new StringBuffer(), node).toString();
		
		if (node.isExperiment()) {
			FileWriter fw;
			BufferedWriter bw;
			String newline = System.getProperty("line.separator");
			try {
				String path = file.getAbsolutePath();
				System.out.println(path);
				if(!path.endsWith(".htm") && !path.endsWith(".html")) {
					file = new File(file.getAbsolutePath() + ".html");
				}
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("<HTML>" + newline);
				bw.write("<HEAD>" + newline);
				bw.write("<TITLE>" + newline);
				bw.write(nodeName + newline);
				bw.write("</TITLE>" + newline);
				bw.write("</HEAD>" + newline);
				bw.write("<BODY>" + newline);
				bw.write(htmlContent + newline);
				bw.write("</BODY>" + newline);
				bw.write("</HTML>" + newline);
				bw.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	public static StringBuffer createHTMLContent(StringBuffer htmlContent, QuestionTreeNode node) {
		String nodeName = node.getName();
		String nodeValue = node.getValue();
		if (node.isExperiment()) {
			htmlContent.append(getHTMLString(nodeName, nodeValue, EXPERIMENT_LEVEL));
		} else if (node.isCategory()) {
			htmlContent.append(getHTMLString(nodeName, nodeValue, CATEGORY_LEVEL));
		} else if (node.isQuestion()) {
			htmlContent.append(getHTMLString(nodeName, nodeValue, QUESTION_LEVEL));
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			createHTMLContent(htmlContent, (QuestionTreeNode) node.getChildAt(i));
		}
		return htmlContent;
	}

	private static String getHTMLString(String name, String content, int level) {
		String headline = "";
		String newline = System.getProperty("line.separator");
		String bottomLine = QuestionViewPane.HTML_DIVIDER;
		if (name != null && content != null) {
			switch (level) {
			case EXPERIMENT_LEVEL:
				headline = "<h1>" + name + "</h1>";
				bottomLine = QuestionViewPane.HTML_DIVIDER + QuestionViewPane.FOOTER_EXPERIMENT_CODE + bottomLine;
				break;
			case CATEGORY_LEVEL:
				headline = "<h2>" + name + "</h2>";
				break;
			case QUESTION_LEVEL:
				headline = "<h3>" + name + "</h3>";
				break;
			}
			return newline + newline + headline + newline + "<br><br>" + newline
					+ content + bottomLine;
		}
		return "";
	}

	public static void saveAsHTMLFiles(File file, QuestionTreeNode node) {
		String nodeName = node.getName();
		String nodeValue = node.getValue();
		if (node.isExperiment()) {
			file.mkdir();
		} else if (node.isCategory()) {
			file = getFreeFile(new File(file.getAbsolutePath() + System.getProperty("file.separator")
					+ nodeName));
			file.mkdir();
		}
		if (nodeName != null && nodeValue != null) {
			writeFiles(file.getAbsolutePath(), nodeName, nodeValue);
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			saveAsHTMLFiles(file, (QuestionTreeNode) node.getChildAt(i));
		}
	}

	private static void writeFiles(String path, String name, String content) {
		FileWriter fw;
		BufferedWriter bw;
		String newline = System.getProperty("line.separator");
		try {
			File f = getFreeFile(new File(path + System.getProperty("file.separator") + name + ".html"));
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
			bw.write("<HTML>" + newline);
			bw.write("<HEAD>" + newline);
			bw.write("<TITLE>" + newline);
			bw.write(name + newline);
			bw.write("</TITLE>" + newline);
			bw.write("</HEAD>" + newline);
			bw.write("<BODY>" + newline);
			bw.write(content + newline);
			bw.write("</BODY>" + newline);
			bw.write("</HTML>" + newline);
			bw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private static File getFreeFile(File f) {
		while (f.exists()) {
			f = new File(f.getAbsoluteFile() + "_");
		}
		return f;
	}

}
