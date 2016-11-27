import java.io.*;
//import java.io.File;
//import java.io.FileNotFoundException;
import java.util.Scanner;

public class homework
{
    int noOfQueries;
    int noOfFacts;
    String[] queries;
    String[] facts;

    public void readFromFile(String path)
    {
	File inputFile = new File(path);
	try
	    {
		Scanner sc =new Scanner(inputFile);
		noOfQueries = Integer.parseInt(sc.nextLine());
		queries = new String[noOfQueries];
		for(int i=0;i<noOfQueries;i++)
		    {
			queries[i] = sc.nextLine();
		    }

		noOfFacts = Integer.parseInt(sc.nextLine());
		facts = new String[noOfFacts];
		for(int i=0;i<noOfFacts;i++)
		    {
			facts[i] = sc.nextLine();
			//System.out.println(facts[i]);
		    }
		
		
	    }
	catch(FileNotFoundException e)
	    {
		e.printStackTrace();
	    }
    }
    public static void main(String[] args)
    {
	
	homework hw = new homework();
	hw.readFromFile("input.txt");

	LexicalAnalysis lex = new LexicalAnalysis();
	Database database = new Database();

	for(int i=0;i<hw.noOfFacts;i++)
	    {
		String lexString = lex.getLexicallyAnalysedString(hw.facts[i]);
		SyntaxAnalysis syntax = new SyntaxAnalysis();
		String postfixString = syntax.convertToPostfix(lexString);
		//	System.out.println(postfixString);
		String cnfString = syntax.convertToCNF(postfixString);
		database.addSentenceToDatabase(cnfString);
	    }

	for(int i=0;i<hw.noOfQueries;i++)
	    {
		String lexString = lex.getLexicallyAnalysedString(hw.queries[i]);
		//	System.out.println(lexString);
		Resolution res = new Resolution();
		SyntaxAnalysis syntax = new SyntaxAnalysis();
		String postfixString = syntax.convertToPostfix(lexString);
		String result = res.performResolution(postfixString,database);
		System.out.println(result);
	    }
	/*for(int i=0;i<args.length;i++)
	    {
		if(args.length!=0)
		    {
			if(i!=args.length-1)
			    {
				//	System.out.println("Current sentence: " + args[i]);
				String lexString = lex.getLexicallyAnalysedString(args[i]);
				//	System.out.println(lexString);
				//	System.out.println("Perform Tokenization");
				SyntaxAnalysis syntax = new SyntaxAnalysis();
				String postfixString = syntax.convertToPostfix(lexString);
				//	System.out.println(postfixString);
				String cnfString = syntax.convertToCNF(postfixString);
				//	System.out.println("Convert to CNF");
				//	System.out.println(cnfString);
				//	System.out.println("Store into database");
				//	syntax.addSentenceToDatabase(cnfString,database);
				database.addSentenceToDatabase(cnfString);
				//System.out.println(args[0]);
			
			    }
			else
			    {
				//	System.out.println("Current sentence: " + args[i]);
				String lexString = lex.getLexicallyAnalysedString(args[i]);
				//	System.out.println(lexString);
				Resolution res = new Resolution();
				SyntaxAnalysis syntax = new SyntaxAnalysis();
				String postfixString = syntax.convertToPostfix(lexString);
				//	System.out.println(postfixString);
				String result = res.performResolution(postfixString,database);
				System.out.println(result);
			    }
		    }

		
		    }*/
	//database.displayDatabase();
	    
    }
}
