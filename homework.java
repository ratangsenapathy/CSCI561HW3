import java.io.*;

public class homework
{
    public static void main(String[] args)
    {
     
	homework hw = new homework();
	if(args.length!=0)
	    {
		LexicalAnalysis lex = new LexicalAnalysis();
		System.out.println(lex.getLexicallyAnalysedString(args[0]));
		//System.out.println(args[0]);
	    }
	    
    }
}
