import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class GeneBankSearch{
	static RandomAccessFile Qfile;
	static RandomAccessFile Bfile;

	public static void main(String[] args) throws FileNotFoundException {
		Qfile = new RandomAccessFile(args[1],"r" );
		Bfile = new RandomAccessFile(args[0],"r" );
		if (args[0].contains(".gbk")) {

		} else {
			System.err.println("ERROR: Invalid Gene Bank (Tree) File (must include .gbk and/or valid filepath)");
			System.exit(-1);
		}
		if (args[1].contains(".gbk")) {

		} else {
			System.err.println("ERROR: Invalid Gene Bank (Query) File (must include .gbk and/or valid filepath)");
			System.exit(-1);
		}

	}

}
