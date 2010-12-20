package experimentGUI.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * this class contains zip methods
 * 
 * @author Markus Köppen, Andreas Hasselberg
 *
 */
public class ZipFile {
	
	private static ZipOutputStream zipOut;
	private static String path;

	/**
	 * a main method to test the zip-method
	 * @param args
	 */
	public static void main(String[] args) {
		zipFiles("C:\\Users\\hasselbe\\workspace\\QuelltextProj\\", "cow.zip");
	}

	/**
	 * zips a file or directory, ignores zip-files
	 * @param zipPath path to the file or directory which should be zipped
	 * @param outputName name of the zip file which is created
	 */
	public static void zipFiles(String zipPath, String outputName) {
		path = zipPath;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(zipPath + outputName));
			new ZipFile().zipDir(new File(zipPath));
			zipOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method to zip a directory
	 * @param zipDir the directory
	 */
	private void zipDir(File zipDir) {
		File[] fileArray = zipDir.listFiles();
		for (int i = 0; i < fileArray.length; i++) {
			if (fileArray[i].isDirectory()) {
				zipDir(fileArray[i]);
			} else {
				zipFile(fileArray[i]);
			}
		}
	}

	/**
	 * method to zip a file
	 * @param file the file
	 */
	private void zipFile(File file) {
		byte[] buf = new byte[4096];
		if (file.getName().endsWith(".zip")) return;
		try {
			String zipFilePath = file.getAbsolutePath();
			if(zipFilePath.startsWith(path)) {
				zipFilePath = zipFilePath.substring(path.length());
			}
			FileInputStream inFile = new FileInputStream(file);
			zipOut.putNextEntry(new ZipEntry(zipFilePath));
			int len;
			while ((len = inFile.read(buf)) > 0) {
				zipOut.write(buf, 0, len);
			}
			inFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
