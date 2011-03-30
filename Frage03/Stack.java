public class Stack {

	private int [] elements;
	private int index;

	public Stack (int[] elements, int index) {
		this.elements = elements;
		this.index = index;
	}

  	 public void push (int item) { 
    		elements[index] = item; 
    		index++; 
    	} 

    	public int pop () { 
    		index--; 
        	return elements[index]; 
    	}
    
    	public int get (int index) {
    		return elements[index];
   	 }
}