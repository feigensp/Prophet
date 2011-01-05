package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;

public class ExternalJarTest {

	public static void main(String[] args) {
//		File file = new File("testBla.jar");
//
//		if (!file.exists())
//			throw new RuntimeException(
//					"Working Directory falsch gesetzt");
//
//		URL extLibUrl = null;
//		try {
//			extLibUrl = new URL("file", "localhost", file.getAbsolutePath());
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//		}
//		URLClassLoader loader = new URLClassLoader(new URL[] { extLibUrl });
//
//		InputStream is = loader.getResourceAsStream("janet2.xml");
//		System.out.println(is);
//		is = loader.getResourceAsStream("META-INF/MANIFEST.MF");
//		System.out.println(is);
		
		
		try {
            File file = new File("testBla.jar");
            URL url = file.toURI().toURL();
            URLClassLoader ucl = new URLClassLoader(new URL[] { url });
    		InputStream is = ucl.getResourceAsStream("janet2.xml");
    		QuestionTreeNode node = QuestionTreeXMLHandler.loadXMLTree(is);
    		Enumeration e = node.breadthFirstEnumeration();
    		while(e.hasMoreElements()) {
    			System.out.println(e.nextElement());
    		}
//            Class root = ucl.loadClass("Dummy");
//            URL res = root.getResource("test/junit.jar");
//            System.out.println(res);
//            String lookForClass = "junit/framework/Test.class";
//            JarInputStream jis = new JarInputStream(res.openStream());
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            JarEntry entry = null;
//            while ((entry = jis.getNextJarEntry()) != null) {
//                String entryName = entry.getName();
//                if (entryName.equals(lookForClass)) {
//                    byte[] buffer = new byte[1024];
//                    int len = 0;
//                    while ((len = jis.read(buffer)) != -1) {
//                        baos.write(buffer, 0, len);
//                        break;
//                    }
//                }
//            }
// 
//            jis.closeEntry();
//            jis.close();
//            byte[] classBytes = baos.toByteArray();
//            baos.close();
// 
//            Class clazz = new ClassLoader() {
//                public Class defineClass(byte[] data) {
//                    return super.defineClass("junit.framework.Test", data, 0,
//                            data.length);
//                }
//            }.defineClass(classBytes);
// 
//            System.out.println(clazz.getName());
 
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
}
