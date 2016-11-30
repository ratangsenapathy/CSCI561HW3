import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Database extends Compiler
{
    HashSet<HashSet<String>> entries;
    
    public Database()
    {
	entries = new HashSet<HashSet<String>>();
    }

    public boolean compareEntries()
    {
	/*int index1 = 0;
	for(HashSet<String> clause1 : entries)
	    {
		for(String literal1 : clause)
		    {
			int index2 = 0;
			for(HashSet<String> clause2 : entries)
			    {
				for(String literal2 : clause2)
				    {
					if(index2++<=index1)
					    {
						continue;
					    }

					if(literal1.equals(literal2))
					    {
					    }
				    }
			    }
		    }
		index1++;
	    }*/
	return true;
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

    public HashSet<String> normalizeClause(HashSet<String> clause)
    {
	HashMap<String,Integer> normalVariables = new HashMap<String,Integer>();
	int tokenVal=0;
	HashSet<String> newClause = new HashSet<String>();
	for(String literal : clause)
	    {
		String patternString ="`(.+?),(.+?)`(~?)";

		Pattern pattern = Pattern.compile(patternString);
        
		Matcher matcher = pattern.matcher(literal);
		String predicate="";
		StringBuffer stringBuffer = new StringBuffer();
		while(matcher.find())
		    {
			String p = matcher.group(2);
			String[] params = p.split(",");
			String[] newParams = new String[params.length];
			for(int i=0;i<params.length;i++)
			    {
				if(Character.isLowerCase(params[i].charAt(0)))
				    {
					if(normalVariables.containsKey(params[i]))
					    newParams[i] = "x" + normalVariables.get(params[i]);
					else
					    {
						normalVariables.put(params[i],tokenVal++);
						newParams[i] = "x" + normalVariables.get(params[i]); 
					    }
				    }
				else
				    newParams[i] = params[i];
			    }
			
			String replacement="`" + matcher.group(1);
			    for(int i=0;i<newParams.length;i++)
				{
				  
					replacement+= "," + newParams[i];
				   
				}
			replacement+= "`" + matcher.group(3);

			matcher.appendReplacement(stringBuffer, replacement);
		    }
		matcher.appendTail(stringBuffer);
		newClause.add(stringBuffer.toString());
	
	    }

	return newClause;
		
    }
    public void normalizeDatabase()
    {
	HashSet<HashSet<String>> newEntries = new HashSet<HashSet<String>>();
	for(HashSet<String> clause : entries)
	    {
		
		HashSet<String> newClause = normalizeClause(clause);
		newEntries.add(newClause);
		
	    }

	entries = newEntries;
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


