/**
 * Alternative parser class that converts between
 * String and long integer representation of gene sequences.
 *
 * @author afoy
 */
public class Parser {

	public Parser(BTree btree){
		//Just to initialize the Parser
	}
	
	public String longtosubseq(long key,int k){
	String res = " ";
	long tmp;
	char gene;
	for(int i=0;i<k;i++)
	{
		tmp=key;
		tmp=tmp>>(2*i);
		tmp=tmp&0x03;
		gene=genomeOP(tmp);
		res+=gene;
	}
	
	return res;
}
public long subseqtolong(String ss,int k){
	long key =0x000000;
	long pos;
	byte tbits = 0x00;
	for(int i=0;i<k;i++)
	{
		switch (ss.charAt(i))
		{
		case 'a':
			tbits=0x00;
			break;
		case 'c':
			tbits=0x01;
			break;
		case 'g':
			tbits=0x02;
			break;
		case 't':
			tbits=0x03;
			break;
		}
	pos=tbits<<(2*i);
	key=key|pos;
	}
	return key;
}
private char genomeOP(long key){
	 char tmp = ' ';
	 if(key==0x00)
	 {
	 tmp='a';
	 }
	 if(key==0x01)
	 {
	 tmp='c';
	 }
	 if(key==0x02)
	 {
	 tmp='g';
	 }
	 if(key==0x03)
	 {
	 tmp='t';
	 }
	 return tmp;
 }

}
