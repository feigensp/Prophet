package test;

public class PilotTest {
		public static void main(String[] args) {
			int array[] = {14,5,7};
			
			for (int counter1 = 0; counter1 < array.length; counter1++) {
				for (int counter2 = counter1; counter2 > 0; counter2--) {
					if (array[counter2 - 1] > array[counter2]) {
						int variable1 = array[counter2];
						array[counter2] = array[counter2 - 1];
						array[counter2 - 1] = variable1;
					}
				}
			}

			for (int counter3 = 0; counter3 < array.length; counter3++)
				System.out.println(array[counter3]);
		}
}
