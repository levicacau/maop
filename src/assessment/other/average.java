import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
Created to calculate the average of IGD and other metrics per generation over the independent runs
Use N files with the same size and return the average
Can be used with blank lines to separate runs
*/

public class average {
	public static void main(String[] args) throws IOException{
		if(args.length < 2){ //deals with parameters
			System.err.println("use: average file1 ... fileN");
			System.exit(1);
		}
		
		BufferedReader[] bf = new BufferedReader[args.length];
		for(int f=0;f<args.length;f++){ //converts filenames to readers
			bf[f] = new BufferedReader(new FileReader(args[f]));
		}
		int lineCount=0; // count the number of lines
		while (true) {
			String[] line = new String[bf.length];
			boolean endFile=false;
			
			for(int f=0;f<bf.length;f++){ //put the readlines in strings and check for files with different sizes
				line[f]=bf[f].readLine();
				if(line[f] == null){ //if reaches the end of any file
					endFile=true;
					
					if(f > 0){ //if this file is not the first, then the first is larger than any other
						System.err.print("\nERROR, FILE "+args[0]+" LARGER THAN "+args[f]+"\n");
						System.exit(1);
					}else{ // if the file is the first, but any other did not reach the end, than this other is larger, and there is an error
						for(int i=1;i<bf.length;i++){ //does not check 0
							if(bf[i].readLine() != null){
								System.err.print("\nERROR, FILE "+args[i]+" LARGER THAN "+args[0]+"\n");
								System.exit(1);
							}
						}
					}
					break;
				}else
					if(line[f].toLowerCase().contains("inf")){
						System.err.print("\nERROR, \"inf\" in FILE "+args[f]+"\n");
						System.exit(1);
					}
			}
			if(endFile)// if endFile and no errors
				break;
			
			double[] avgLine = lineToDoubleString(line[0]);
			if(avgLine != null){ //if if avg is null, the line is blank
				for(int f=1;f<bf.length;f++){
					double[] tmp = lineToDoubleString(line[f]);
					if(tmp == null){
						System.err.print("\nERROR, FILES WITH BLANK LINES IN DIFFERENT PLACES. line: "+lineCount+"\n");
						System.exit(1);
					}
					
					if(avgLine.length != tmp.length){
							System.err.print("\nERROR, FILES WITH DIFFERENT NUMBERS OF COLLUMNS. line: "+lineCount+"\n");
							System.exit(1);
					}
					for(int i=0;i<avgLine.length;i++)
						avgLine[i]+=tmp[i];
				}
				for(int i=0;i<avgLine.length;i++)
					System.out.print(avgLine[i]/bf.length+" ");
			}else{ //if one line is null, then all others must be as well
				for(int f=1;f<bf.length;f++){
					if(lineToDoubleString(line[f]) != null){
						System.err.print("\nERROR, FILES WITH BLANK LINES IN DIFFERENT PLACES. line: "+lineCount+"\n");
						System.exit(1);
					}
				}
			}
			lineCount++;
			System.out.println();
		}
	}
	
	public static double[] lineToDoubleString(String line){
		String[] objs = line.split("\\s+"); //split for one or more spaces
		if(objs.length==0) //if is not split
			objs = line.split("\t");//split for tabs
			
		double[] d=null;
		if ((objs.length != 1 || !(objs[0].equals(""))) && !objs[0].equals("#") ) { //if has a nonempty character different than #
			d = new double[objs.length];
			for(int i = 0; i < objs.length; i++){
				d[i] = Double.parseDouble(objs[i].replace(",",".")); // converts line to number, both with point or comma as separator
			}
		}
		return d;
	}
}
