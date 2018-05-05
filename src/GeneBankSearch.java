import java.io.File;
import java.util.Scanner;


/**
 * GeneBankSearch, driver class
 *
 * The driver class for the GeneBankSearch program that
 * searches a BTree file created by GeneBankCreateBTree
 * for sequences specified in a query file
 *
 * @author marufahmed
 */
public class GeneBankSearch {

	private static boolean useCache = false;
	private static String btreeFile, queryFile;
	private static int cacheSize, debugLevel1 = 0;


	public static void main(String[] args) {

		if(args.length < 3 || args.length > 5) {
			printUsage();
		}

		if (args[0].equals("1")) {
			useCache = true;
		} else if (!(args[0].equals("0") || args[0].equals("1"))) {
			printUsage();
		}

		btreeFile = args[1];
		queryFile = args[2];

		if (useCache && args.length >= 4) {
			cacheSize = Integer.parseInt(args[3]);
		}

		if(args.length == 5)
			debugLevel1 = Integer.parseInt(args[4]);

		String seq = "", deg = "";

		for(int i = btreeFile.length()-1; i >= 0; i--) {
			if(btreeFile.charAt(i) != '.')
				deg += btreeFile.charAt(i);
			else break;
		}
		deg = reverseString(deg);

		for (int i = btreeFile.length()-deg.length()-2; i >= 0; i--) {
			if(btreeFile.charAt(i) != '.')
				seq += btreeFile.charAt(i);
			else break;
		}
		seq = reverseString(seq);

		int degree = Integer.parseInt(deg);
		int sequence = Integer.parseInt(seq);
		System.out.println("degree: " + degree);
		System.out.println("sequence length: " + sequence);
		
		try {
			Boolean flag=false;
			GeneBankConvert gbc = new GeneBankConvert();
			BTree tree = new BTree(degree, new File(btreeFile.substring(0, args[1].indexOf(".btree"))), useCache, cacheSize);
			Scanner scan = new Scanner(new File(queryFile));
			
			while(scan.hasNext()) {
				String query = scan.nextLine(); //sequence to search for
				
				long q = gbc.convertStringToLong(query);
				String converted = gbc.convertLongToString(q, sequence);
				TreeObject result = tree.search(tree.getRoot(), q);
				
				if(result != null) 
					System.out.println(gbc.convertLongToString(result.getData(), Integer.parseInt(seq))+": "+ result.getFrequency());
					
			}
			
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String reverseString(String s) {
		if(s.length() == 1)
			return s;
		return "" + s.charAt(s.length() - 1) + reverseString(s.substring(0, s.length() - 1));
	}

	private static void printUsage() {
		System.err.println("Usage: java GeneBankSearch "
				+ "<0/1(no/with Cache)> <btree file> <query file> "
				+ "[<cache size>] [<debug level>]\n");
		System.exit(1); 
	}
}
