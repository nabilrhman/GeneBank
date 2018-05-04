  * Creates a BTree from a user provided gbk file.
   * @author nabilr
   */
 +import java.io.File;
 +import java.io.IOException;
 +import java.io.RandomAccessFile;
 +import java.util.Scanner;
  public class GeneBankCreateBTree
  {
 +static int degree;
 +	static int subseq;
 +	static String fname;
 +	static File readFile;
 +	static Scanner scan;
 +	static BTree myBTree;
 +	static Parser myparse;
 +	static Boolean Flag=false;
 +	public static void main(String[] args) throws IOException {
 +		readFile=new File("treefile");
 +		scan=new Scanner(readFile);
 +		if (Integer.parseInt(args[0]) == 0) {
 +			degree = 510;
 +		} else {
 +			degree = Integer.parseInt(args[0]);
 +		}
 +		if (args[1].contains(".gbk")) {
 +				fname=args[1];
 +		} else {
 +			System.err.println("ERROR: Invalid Gene Bank File (must include .gbk)");
 +			System.exit(-1);
 +		}
 +		if (Integer.parseInt(args[2]) >= 1 && (Integer.parseInt(args[2]) <= 31)) {
 +			subseq = Integer.parseInt(args[2]);
 +		} else {
 +			System.err.println("ERROR: Sub-Sequence OverFlow to apply this program correctly please input" + "\n"
 +					+ "an integer vale between the values of 1 and 31 inclusively");
 +			System.exit(-1);
 +		}
 +
 +		myBTree = new BTree(degree, fname);
 +		myparse=new Parser(myBTree);
 +		StringBuilder s = null;
 +		while(scan.hasNext()&&!scan.hasNext("//"))
 +		{
 +			
 +			if(!scan.hasNext("ORIGIN")&&Flag==false)
 +			{
 +				scan.nextLine();
 +			}else{
 +				Flag=true;
 +				while(scan.hasNext("ORIGIN")||scan.hasNextInt())
 +				{
 +					scan.next();
 +				}
 +				if(s==null||scan.hasNext(" "))
 +				{
 +					s=new StringBuilder();
 +					while(s.length()<subseq)
 +					{
 +					s=s.append(scan.next());
 +					}
 +				}
 +				else if(s.length()<subseq)
 +				{
 +				while(s.length()<subseq){
 +					if(scan.hasNextInt())
 +					{
 +						scan.next();
 +					}
 +					s=s.append(scan.next());
 +				}
 +				System.out.println(s);
 +				}
 +				int i=0;
 +				while(i<(s.length()-subseq))
 +						{
 +				myBTree.BTreeInsert(myparse.subseqtolong(s.substring(i, subseq+i),subseq));
 +					i++;
 +						}
 +				s=s.delete(0,s.length());
 +			}
 +		}
 +	
 +	}
  }