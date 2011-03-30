public class Class3 {

	private int [] array;
	private int index;

	public Class3(int[] array, int index) {
		this.array = array;
		this.index = index;
	}

  	 public void method1 (int variable3) { 
    		array[index] = variable3; 
    		index++; 
    	} 

    	public int method2 () { 
    		index--; 
        	return array[index]; 
    	}
    
    	public int methode3(int index) {
    		return array[index];
   	 }
}
