import java.util.HashSet;
import java.util.LinkedList;

public class Database extends Compiler
{
    HashSet<HashSet<String>> entries;
    
    public Database()
    {
	entries = new HashSet<HashSet<String>>();
    }

    public void add(HashSet<String> clause)
    {
	if(!entries.contains(clause))
	    entries.add(clause);
    }

    public void displayDatabase()
    {
	int i=1;
	for(HashSet<String> entry : entries)
	    {
		System.out.println("Entry "+i++);
		for(String literal : entry)
		    {
			System.out.print(literal + " ");
		    }
		System.out.println("");
	    }
    }

    public HashSet<String> splitIntoLiterals(String clauseString)
    {
	int i=0;
	int length = clauseString.length();
	HashSet<String> clause = new HashSet<String>();
	String literal="";
	while(i<length)
	    {
		int tokenEnd= getTokenEnd(clauseString,i);
		String token = clauseString.substring(i,tokenEnd);
	
		i=tokenEnd;
	
		
		if(token.charAt(0)=='`')
		    {
			if(!literal.equals(""))
			    {
				if(!clause.contains(literal))
				    clause.add(literal);
				literal="";
			    }
			literal+=token;
		    }
		else
		    if(token.equals("~"))
			{
			    literal+=token;
			    if(!clause.contains(literal))
				clause.add(literal);
			    literal="";
			}
		    else
			{
			    if(!literal.equals("") && !clause.contains(literal))
				clause.add(literal);
			    literal="";
			}
		   
	    }

	if(!literal.equals(""))
	    {
		if(!clause.contains(literal))
		    clause.add(literal);
	    }

	return clause;
	
    }

    public void addSentenceToDatabase(String sentence)
    {
	int i=0;
	int length = sentence.length();
	//String modSentence="";
	//	LinkedList<String> clause = new LinkedList<String>();
	while(i<length)
	    {
		int tokenEnd= getTokenEnd(sentence,i);
		String token = sentence.substring(i,tokenEnd);
		i=tokenEnd;
		//	System.out.println("Token: " + token);
		
		if(token.charAt(0)=='`')
		    {
			stack.push(token);
		    }
		else
		    if(token.equals("~"))
			{
			    String op=stack.pop();
			    String modSentence = op+token;
			    stack.push(modSentence);
			}
		    else
			if(token.equals("&"))
			    {
				String op2 = stack.pop();
				String op1 = stack.pop();
				HashSet<String> clause = null;
				if(op1.charAt(op1.length()-1)!='&')
				    {
					clause = splitIntoLiterals(op1);
					add(clause);
				    }
				clause = splitIntoLiterals(op2);
				add(clause);

				
			        //database.add();
				//clause = new LinkedList<String>();
				String modSentence = op1+op2+"&";
				stack.push(modSentence);
			    }
			else if(!token.equals('|'))
			    {   String op1="",op2="";
				op2 = stack.pop();
				op1 = stack.pop();
				String modSentence = op1+op2+"|";
				stack.push(modSentence);
	      
			    }
	    }
	
	String op = stack.pop();
	if(op.charAt(op.length()-1)!='&')
	    {  // System.out.println("Database entry to be pushed: "+op);
		HashSet<String> clause = splitIntoLiterals(op);
		add(clause);
	    }
    }


  
}


