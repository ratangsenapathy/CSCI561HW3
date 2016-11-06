import java.io.*;

public class homework
{
    public static void main(String[] args)
    {
     
	homework hw = new homework();
	if(args.length!=0)
	    {
		LexicalAnalysis lex = new LexicalAnalysis();
		String lexString = lex.getLexicallyAnalysedString(args[0]);
		System.out.println(lexString);
		System.out.println("Perform Tokenization");
		SyntaxAnalysis syntax = new SyntaxAnalysis();
		String postfixString = syntax.convertToPostfix(lexString);
		System.out.println(postfixString);
		String cnfString = syntax.convertToCNF(postfixString);
		System.out.println("perform implication elimination");
		System.out.println(cnfString);
		
		//System.out.println(args[0]);
	    }
	    
    }
}
