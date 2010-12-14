package experimentGUI.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile {
	
	private static ZipOutputStream zipOut;
	private static String path;

	public static void main(String[] args) {
		zipFiles("C:\\Users\\hasselbe\\workspace\\QuelltextProj\\", "cow.zip");
	}

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
