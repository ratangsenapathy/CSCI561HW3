import java.io.*;

public class homework
{
    public static void main(String[] args)
    {
     
	homework hw = new homework();
	LexicalAnalysis lex = new LexicalAnalysis();
	Database database = new Database();
	for(int i=0;i<args.length;i++)
	    {
		if(args.length!=0)
		    {
			System.out.println("Current sentence: " + args[i]);
			String lexString = lex.getLexicallyAnalysedString(args[i]);
			System.out.println(lexString);
			System.out.println("Perform Tokenization");
			SyntaxAnalysis syntax = new SyntaxAnalysis();
			String postfixString = syntax.convertToPostfix(lexString);
			System.out.println(postfixString);
			String cnfString = syntax.convertToCNF(postfixString);
			System.out.println("Convert to CNF");
			System.out.println(cnfString);
			System.out.println("Store into database");
			syntax.addSentenceToDatabase(cnfString,database);
			//System.out.println(args[0]);
			database.displayDatabase();
		    }
	    }
	    
    }
}
