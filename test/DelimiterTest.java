package test;

public class DelimiterTest {
	
	public static void main(String[] args) {
		if(System.getProperty("file.separator").equals("\\")) {
			System.out.println("yay");
		} else {
			System.out.println("nooo");
		}
	}

}
