public class Class2 {
	
	public static void main (String args[]){
	    int [] variable1 = new int[128]; 
	    Class3 variable2 = new Class3(variable1, 0);
	    
	    variable2.method1(7);
	    variable2.method1(2);
	    variable2.method1(8);
	    
	    System.out.println(variable2.method2());
	    System.out.println(variable2.method2());
	    System.out.println(variable2.method2());
	}
} 
