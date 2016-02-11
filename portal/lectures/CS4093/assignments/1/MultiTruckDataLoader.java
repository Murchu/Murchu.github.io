import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class MultiTruckDataLoader {
	
	private int nItems; //number of items
	private int nTrucks; //number of trucks
	private int capacity; //max items per truck
    private int nPairs; //number of forbidden pairs
    private int badpairs[][]; //array of forbidden item pairs
    
    public MultiTruckDataLoader(String filename) throws IOException {
    	/*
    	 * Assumes data is in file in the following format:
    	 *    nItems, nTrucks, capacity, nPairs
    	 * followed by a sequence of forbidden pairs, one pair to each line
    	 */
	    Scanner scanner = new Scanner(new File(filename));
	    nItems = scanner.nextInt();
	    nTrucks = scanner.nextInt();
	    capacity = scanner.nextInt();
	    nPairs = scanner.nextInt();
	    badpairs = new int[nPairs][2];
	    for (int i=0;i<nPairs;i++){
	        badpairs[i][0] = scanner.nextInt();
	        badpairs[i][1] = scanner.nextInt();
	    }
	    scanner.close();
    }
    
    public int getnItems() {
    	return nItems;
    }
    
    public int getnTrucks() {
    	return nTrucks;
    }
    
    public int getCapacity() {
    	return capacity;
    }
    
    public int getnPairs() {
    	return nPairs;
    }
    
    public int[][] getbadpairs() {
    	return badpairs;
    }
    
}
