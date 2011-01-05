package experimentGUI;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class FileHandler {

	public static URL getRessource(String path) {
		URL url = FileHandler.class.getResource(path);		
		if(url == null) {
			File f = new File(path);
			if(f.exists()) {
				try {
					url = f.toURI().toURL();		
				} catch(MalformedURLException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return null;
			}
		}
		return url;
	}
}
