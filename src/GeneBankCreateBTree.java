  * Creates a BTree from a user provided gbk file.
   * @author nabilr
   */
 import java.io.File;
 import java.io.IOException;
 import java.io.RandomAccessFile;
 import java.util.Scanner;
  public class GeneBankCreateBTree
  {
 	static int degree;
	static int subseq;
	static int cacheSize;
	static String fname;
	static File readFile;
	static BTree myBTree;
	static Parser myparse;
	static Boolean Flag = false;
	static Boolean cacheFlag = false;

	public static void main(String[] args) throws IOException {
		Scanner scan;
		GeneBankConvert gbc=new GeneBankConvert();
		if (Integer.parseInt(args[0]) == 0) {
			degree = getOptimalDegree();
		} else {
			degree = Integer.parseInt(args[0]);
		}
		if (args[1].contains(".gbk")) {
			fname = args[1];
			readFile = new File(fname);
		} else {
			badUsage();
			System.exit(-1);
		}
		if (Integer.parseInt(args[2]) >= 1 && (Integer.parseInt(args[2]) <= 31)) {
			subseq = Integer.parseInt(args[2]);
		} else {
			badUsage();
			System.exit(-1);
		}
		if (Integer.parseInt(args[3]) == 0) {
			cacheFlag = false;
		} else {
			cacheFlag = true;
			cacheSize = Integer.parseInt(args[3]);
		}

		myparse = new Parser();
		StringBuilder s = null;
		scan = new Scanner(new File("tree.gbk"));
		s = new StringBuilder();
		while (!scan.hasNext("//")&&scan.hasNext()) {
			if (!scan.hasNext("ORIGIN")&&Flag==false) {
				scan.nextLine();
			} else {
				Flag=true;
				if (scan.hasNext("ORIGIN")) {
					scan.nextLine();
				}
				if (scan.hasNext()) {
					while (scan.hasNext()) {
						if (scan.hasNextInt()) {
							scan.next();
						}
						s = s.append(scan.next());
					}
				} 
				
			}
			
		}
		myBTree = new BTree(degree, fname, cacheFlag, cacheSize);
		int i = 0;
		while (i < (s.length() - subseq)){
			myBTree.insert(myparse.subseqtolong(s.substring(i, subseq + i),subseq));
			i++;
		}
		myBTree.inOrderPrint(myBTree.getRoot());
		scan.close();
	}
	
	private static void badUsage() {
		System.err.println(
				"Usage: java GeneBankCreateBTree <degree> <gbk file> <sequence length> <cache size> <test gbk file> [<debuglevel>]");
		System.err.println("<cache>: 1 to use cache, 0 for no cache");
		System.err.println("<degree>: degree of the BTree (0 for default)");
		System.err.println("<gbk file>: file with sequence data");
		System.err.println("<sequence length>: length of a sequence (1-31)");
		System.err.println("[<cache size>]: if cache enabled, size of cache");
		System.err.println("[<debug level>]: debugging level (0-1)");
		System.exit(1);
	}

	public static int getOptimalDegree() {
		double optimum;
		int sizeOfPointer = 4;
		int sizeOfObject = 12;
		int sizeOfMetadata = 5;
		optimum = 4096;
		optimum += sizeOfObject;
		optimum -= sizeOfPointer;
		optimum -= sizeOfMetadata;
		optimum /= (2 * (sizeOfObject + sizeOfPointer));
		return (int) Math.floor(optimum);
	}

  }
