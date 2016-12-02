import java.io.*;
//import java.io.File;
//import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Stack;
import java.util.Map;
import java.util.Arrays;
import java.util.Iterator;

class Compiler
{
    Stack<String>  stack;
    Compiler()
    {
	stack = new Stack<String>();
    }
    
    public int getTokenEnd(String sentence,int startIndex)
    {
	char initialChar = sentence.charAt(startIndex);
	if(initialChar == '`')
	    {
		int index = startIndex+1;
		while(true)
		    {
			char curChar = sentence.charAt(index);
			if(curChar=='`')
			    return index+1;
			index++;
		    }
	    }
	else
	    if(initialChar == '=')
		{
		    int index = startIndex+1;
		    if(sentence.charAt(index)=='>')
			{
			    return index+1;
			}
		    else
			return index+1;
		}
	    else
		{
		    return startIndex+1;
		}

    }

}


class Literal
{
    String predicate;
    String[] parameters;
    boolean isPositive;

    Literal(String predicate, String parameterString,String type)
    {
	this.predicate = predicate;
	this.parameters = parameterString.split(",");
	for(int i=0;i<parameters.length;i++)
	    {
		this.parameters[i] = this.parameters[i].trim();
	    }
	this.isPositive = type.contains("~")?false:true;
    }

    Literal(String predicate,String[] parameters, String type)
    {
	this.predicate = predicate;
	int length=parameters.length;
	this.parameters = new String[length];
	for(int i=0;i<length;i++)
	    {
		this.parameters[i] = parameters[i].trim();
	    }
	this.isPositive = type.contains("~")?false:true;
    }

    Literal(String predicate,String[] parameters, boolean isPositive)
    {
	this.predicate = predicate;
	int length=parameters.length;
	this.parameters = new String[length];
	for(int i=0;i<length;i++)
	    {
		this.parameters[i] = parameters[i].trim();
	    }
	this.isPositive = isPositive;
    }

    Literal(String predicate,String parameter,boolean isPositive)
    {
	this.predicate = predicate;
	this.parameters = new String[1];
	this.parameters[0] = parameter.trim();
	this.isPositive = isPositive;
    }

    public String convertToString()
    {
	String parametersString = "";
	int len = parameters.length;
	for(int i=0;i<len;i++)
	    {
		parametersString += ","+ parameters[i];
	    }
	return "`" + predicate + parametersString + "`" + (isPositive ? "" : "~");
    }
}


class Resolution
{
    //Stack<String> stack;
    Database database;
    public Resolution()
    {
	//stack = new Stack<String>();
	this.database = new Database();
	
    }

    public String getNegatedQuery(String query)
    {
	int length=query.length()-1;
	String negatedQuery="";
	if(query.charAt(length)=='~')
	    {
		negatedQuery = query.substring(0,length);
	     
	    }
	else
	    {
		negatedQuery = query+"~";
	    }

	//	System.out.println("Negated query:" + negatedQuery);
	return negatedQuery;
	//database.addSentenceToDatabase(negatedQuery);
	    
	
    }

    public void assignDatabase(Database dbMain, Database db)
    {
	for(HashSet<String> entry : db.entries)
	    {
		dbMain.entries.add(entry);
	    }
    }

    
    public String performResolution(String negatedQuery,Database db)
    {
	//System.out.println(negatedQuery);
	assignDatabase(database,db);
	//String negatedQuery = getNegatedQuery(query);
	database.addSentenceToDatabase(negatedQuery);

	HashSet<HashSet<String>> newClauses = new HashSet<HashSet<String>>();
	HashSet<String> resolvedSet = new HashSet<String>();
	//HashSet<HashSet<String>> usedClauses = new HashSet<HashSet<String>>();
	int noOfResolutions = 0;
	int itNo=1;
	while(true)
	    {
		// Iterator<HashSet<String>> it1 = database.entries.iterator();
		int index1 = 0;
		/*	System.out.println("-------------------Iteration+" + (itNo++) + "-------------------\n");
			System.out.println("Display database:");
			database.displayDatabase();
			System.out.println("Dislayed database");*/
		if(System.currentTimeMillis()>homework.endTime)
		    return "FALSE";
		
		for(HashSet<String> clause1 : database.entries)
		    {

			int index2 = 0;
			
			for(HashSet<String> clause2 : database.entries)
			    {
				if (index2++ <= index1)
				    {
					continue;
				    }

				String entryLit1="";
				String entryLit2="";
				for(String lit : clause1)
				    {
					entryLit1+=lit;
				    }

				for(String lit : clause2)
				    {
					entryLit2+=lit;
				    }
				String c1=entryLit1+entryLit2;
				String c2 = entryLit2+entryLit1;
				if(!resolvedSet.contains(c1) && !resolvedSet.contains(c2))
				    {
					HashSet<HashSet<String>> resolvents = resolve(clause1,clause2);                       
					//	if(noOfResolutions++ >200000){
					// System.out.println("No of resolutions=" +noOfResolutions);
					//  return "FALSE";}
					if(resolvents == null)
					    {// System.out.println("No of resolutions=" +noOfResolutions);
						return "TRUE";}
					if(System.currentTimeMillis()>homework.endTime)
					    return "FALSE";
					resolvedSet.add(c1);
					resolvedSet.add(c2);
				
					newClauses.addAll(resolvents);
				    }

				//index2++;
			    }

			index1++;
		    }

		boolean isSubset = true;
		for(HashSet<String> clause : newClauses)
		    {
			if(!database.entries.contains(clause))
			    {
				database.entries.add(clause);
				isSubset = false;
			    }
			
		    }
		//	database.normalizeDatabase();
        
		if(isSubset)
		    return "FALSE";
	    }


    }

    public Literal getLiteralInfo(String text)
    {
	Literal literal = null;
	String patternString ="`(.+?),(.+?)`(~?)";

	Pattern pattern = Pattern.compile(patternString);
        
	Matcher matcher = pattern.matcher(text);
        String predicate="";
	while(matcher.find())
	    {
		literal = new Literal(matcher.group(1),matcher.group(2),matcher.group(3));
	    }
	
	return literal;
    }
    
    public HashSet<HashSet<String>> resolve(HashSet<String> clause1, HashSet<String> clause2)
    {
	HashSet<HashSet<String>> resolvents = new HashSet<HashSet<String>>();
	for(String literal1 : clause1)
	    {
		for(String literal2 : clause2)
		    {
		
			Literal l1=getLiteralInfo(literal1);

			Literal l2=getLiteralInfo(literal2);
		
			String predicate1 = l1.predicate;
			String predicate2 = l2.predicate;
			
			if(predicate1.equals(predicate2) && l1.isPositive != l2.isPositive)
			    {
				HashMap<String,String> bindings = new HashMap<String,String>();

				bindings = unify(l1.parameters,l2.parameters,bindings);
				if(System.currentTimeMillis()>homework.endTime)
				    return new HashSet<HashSet<String>>();
			
				if(bindings==null)
				    continue;

				/*	System.out.println("Binding: ");
					for(Map.Entry<String,String> entry : bindings.entrySet())
					{
					String key = entry.getKey();
					String value = entry.getValue();
					System.out.print(key+"/"+value+" ");
					}
					System.out.println("");*/
				HashSet<String> resolvent=new HashSet<String>();

				String clause1Val="";
				for(String literal : clause1)
				    {clause1Val+=literal+" ";
					if(!literal.equals(literal1))
					    {
						Literal l = getLiteralInfo(literal);
						int len = l.parameters.length;
						for(int i=0;i<len;i++)
						    {
							String p = l.parameters[i];
							if(bindings.containsKey(p))
							    {
								String val = getBindingValue(p,bindings,new HashSet<String>());
								l.parameters[i] = val;
							
							    }
							    
						    }

						
						
						resolvent.add(l.convertToString());
					    }
				    }
				String clause2Val="";
				for(String literal : clause2)
				    { clause2Val+=literal+" ";
					if(!literal.equals(literal2))
					    {
						Literal l = getLiteralInfo(literal);
						int len = l.parameters.length;
						for(int i=0;i<len;i++)
						    {
							String p = l.parameters[i];
							if(bindings.containsKey(p))
							    {
								String val = getBindingValue(p,bindings,new HashSet<String>());
								l.parameters[i] = val;
							    }
							    
						    }
						resolvent.add(l.convertToString());
					    }
				    }
				
				
				if(resolvent.size()==0)
				    {
					
					return null;
				    }

				/*	if(resolvent.contains("`H,Alice`~"))
					{
					System.out.println("The hell is happending");
					System.out.println(clause1Val + "\n" + clause2Val);
					}*/
				
				resolvents.add(resolvent);
			    }
		    }
	    }
	return resolvents;
    }

    public HashMap<String,String> unify(Literal literal1, Literal literal2, HashMap<String,String> bindings)
    {
	if(bindings == null)
	    return null;
	else if(Arrays.equals(literal1.parameters,literal2.parameters))
	    return bindings;
	else if(literal1.parameters.length == 1 && Character.isLowerCase(literal1.parameters[0].charAt(0)))
	    return unifyVar(literal1.parameters[0],literal2,bindings);
	else if(literal2.parameters.length == 1 && Character.isLowerCase(literal2.parameters[0].charAt(0)))
	    return unifyVar(literal2.parameters[0],literal1,bindings);
	else
	    if(literal1.parameters.length > 1 && literal2.parameters.length > 1)
		{
		    String[] r1 = Arrays.copyOfRange(literal1.parameters,1,literal1.parameters.length);
		    String[] r2 = Arrays.copyOfRange(literal2.parameters,1,literal2.parameters.length);

		    Literal first1 = new Literal(literal1.predicate,literal1.parameters[0],literal1.isPositive);
		    Literal first2 = new Literal(literal2.predicate,literal2.parameters[0],literal2.isPositive);
		    
		    Literal rest1 = new Literal(literal1.predicate,r1,literal1.isPositive);
		    Literal rest2 = new Literal(literal2.predicate,r2,literal2.isPositive);
		    
		    return unify(rest1,rest2,unify(first1,first2,bindings));
		}
	    else
		return null;
    }

    public HashMap<String,String> unifyVar(String var, Literal lit, HashMap<String,String> bindings)
    {
	if(bindings.containsKey(var))
	    {
		String val = bindings.get(var);
		Literal valLiteral = new Literal(lit.predicate,val,!lit.isPositive);
		return unify(valLiteral,lit,bindings);
	    }
	else
	    if(lit.parameters.length == 1 && bindings.containsKey(lit.parameters[0]))
		{
		    String val = bindings.get(lit.parameters[0]);
		    Literal l1 = new Literal(lit.predicate,var,!lit.isPositive);
		    Literal l2 = new Literal(lit.predicate,bindings.get(lit.parameters[0]),lit.isPositive);
		    return unify(l1,l2,bindings);
		}
	    else
		{
		    bindings.put(var,lit.parameters[0]);
		    return bindings;
		}
    }


    public String getBindingValue(String var,HashMap<String,String> bindings,HashSet<String> explored)
    {
	if(bindings.containsKey(var))
	    {
		//explored.add(var);
		String val=bindings.get(var);
		explored.add(var);
		if(!explored.contains(val))
		    return getBindingValue(val,bindings,explored);
		else
		    return var;
	    }
	else
	    return var;
    }

    public boolean isVariable(String token)
    {
	if(Character.isLowerCase(token.charAt(0)))
	    return true;
	else
	    return false;
    }

    public String[] subst(HashMap<String,String> theta,String[] p)
    {
	int N = p.length;
	for(int i=0;i<N;i++)
	    {
		if(theta.containsKey(p[i]))
		    p[i] = theta.get(p[i]);
	    }

	return p;
	
    }

    public HashMap<String,String> unify(String[] p,String[] q, HashMap<String,String> theta)
    {
	if(System.currentTimeMillis()>homework.endTime)
	    return null;

        int N= p.length;
	boolean mismatchExists = false;
	String r="";
	String s="";
	for(int i=0;i<N;i++)
	    {
		if(!p[i].equals(q[i]))
		    {
			mismatchExists = true;
			r=p[i];
			s=q[i];
		    }
	    }

	if(!mismatchExists)
	    return theta;

	if(isVariable(r))
	    {
		theta.put(r,s);
		unify(subst(theta,p),subst(theta,q),theta);
	    }
	else if(isVariable(s))
	    {
		theta.put(s,r);
		unify(subst(theta,p),subst(theta,q),theta);
	    }
	else
	    return null;

	return theta;
    }

   
}

class Database extends Compiler
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





class SyntaxAnalysis extends Compiler
{
    //Stack<String>  stack;
    boolean sentModInCNFCalc;
    SyntaxAnalysis()
    {
	//stack = new Stack<String>();
	sentModInCNFCalc = false;
    }


    public int getTokenEnd(String sentence,int startIndex)
    {
	char initialChar = sentence.charAt(startIndex);
	if(initialChar == '`')
	    {
		int index = startIndex+1;
		while(true)
		    {
			char curChar = sentence.charAt(index);
			if(curChar=='`')
			    return index+1;
			index++;
		    }
	    }
	else
	    if(initialChar == '=')
		{
		    int index = startIndex+1;
		    if(sentence.charAt(index)=='>')
			{
			    return index+1;
			}
		    else
			return index+1;
		}
	    else
		{
		    return startIndex+1;
		}

    }

    public int getOperatorPriority(String operator)       //greater the no, greater the priority
    {
	if(operator.equals("("))
	    return 0;
	else if(operator.equals("=>"))
	    return 1;
	else
	    if(operator.equals("&") || operator.equals("|"))
		return 2;
	    else
		if(operator.equals("~"))
		    return 3;
		else
		    return 4;
    }
     
    public String  convertToPostfix(String sentence)      //input sentence is in infix format
    {
	String postfixSentence = "";
	int length = sentence.length();
	int i=0;
	while(i<length)
	    {
		int tokenEnd= getTokenEnd(sentence,i);
		String token = sentence.substring(i,tokenEnd);
		i=tokenEnd;
		if(token.equals(" "))
		    continue;
		//System.out.println("Token: " + token);
		if(token.charAt(0)=='`')
		    postfixSentence += token;
		else
		    if(token.equals("("))
			stack.push(token);
		    else
			if(token.equals(")"))
			    {
				while(true)
				    { String poppedElement = stack.pop();
					if(!poppedElement.equals("("))
					    {
						postfixSentence+=poppedElement;
					    }
					else
					    break;
				    }
			    }
			else
			    {
				int currentOperatorPriority = getOperatorPriority(token);
				while(true)
				    {
					if(!stack.isEmpty())
					    {
						
						String topElement = stack.peek();
						//	System.out.println("Top of stack: " + topElement);
						int topElementPriority = getOperatorPriority(topElement);
						if(currentOperatorPriority>topElementPriority)
						    {
							stack.push(token);
							break;
						    }
						else
						    {
							if(!stack.isEmpty())
							    {String poppedElement = stack.pop();
							postfixSentence+=poppedElement;
							    }
						    }
					
					    }
					else
					    {
						stack.push(token);
						break;
					    }
				    }
			    }
	    }

	while(!stack.isEmpty())
	    {
		postfixSentence+=stack.pop();
	    }

	return postfixSentence;
    }

  

    public String performImplicationElimination(String sentence)
    {
	int i=0;
	int length = sentence.length();
	//String modSentence="";
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
			if(token.equals("=>"))
			    {
				String op2 = stack.pop();
				String op1 = stack.pop();
				String modSentence = op1+"~"+op2+"|";
				stack.push(modSentence);
			    }
			else
			    {
				String op2 = stack.pop();
				String op1 = stack.pop();
				String modSentence = op1+op2+token;
				stack.push(modSentence);
			    }
	    }
	
	return stack.pop();
    }

    public String applyDeMorgansLaw(String sentence)
    {
	int i=0;
	int length = sentence.length();
	//String modSentence="";
	String alpha="";
	String beta="";
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
		else if(token.equals("&") && tokenEnd<length && sentence.charAt(tokenEnd)=='~')
		    {
			String op2 = stack.pop();
			String op1 = stack.pop();
			String modSentence = op1+"~"+op2+"~"+"|";
			stack.push(modSentence);
			i+=1;
			sentModInCNFCalc = true;
		    }
		else if(token.equals("|") && tokenEnd<length && sentence.charAt(tokenEnd)=='~')
		    {
			String op2 = stack.pop();
			String op1 = stack.pop();
			String modSentence = op1+"~"+op2+"~"+"&";
			stack.push(modSentence);
			i+=1;
			sentModInCNFCalc = true;
		    }
		else
		    if(token.equals("~"))
			{
			    String op=stack.pop();
			    String modSentence = op+token;
			    stack.push(modSentence);
			}
		  
		    else
			{
			    String op2 = stack.pop();
			    String op1 = stack.pop();
			    String modSentence = op1+op2+token;
			    stack.push(modSentence);
			}
	    }
	
	return stack.pop();
    }

    public String applyDoubleNegation(String sentence)
    {
		String pattern="~~";
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(sentence);
		StringBuffer stringBuffer = new StringBuffer();

		while(matcher.find()){
		    matcher.appendReplacement(stringBuffer, "");
		    // System.out.println(stringBuffer.toString());
		    sentModInCNFCalc = true;
		}
		matcher.appendTail(stringBuffer);

		return stringBuffer.toString();
    }
    
    public String performInwardNegation(String sentence)
    {
	String tempString=sentence;
	
	    
	do{
	    sentModInCNFCalc = false;
	    tempString = applyDeMorgansLaw(tempString);
	    tempString = applyDoubleNegation(tempString);

	}while(sentModInCNFCalc);

	return tempString;
    }

    public String applyDistributiveLaw(String sentence)
    {
	
	/*	String patternString1 = "(.+?)[&](.+?)\\|";
		String newSentence=sentence;

		do
		{
		sentModInCNFCalc=false;
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher  = pattern.matcher(sentence);
		StringBuffer stringBuffer = new StringBuffer();

		while(matcher.find())
		{
		matcher.appendReplacement(stringBuffer,matcher.group(2)+matcher.group(1)+"&|");
		sentModInCNFCalc=true;
           
		}
		matcher.appendTail(stringBuffer);


		newSentence= stringBuffer.toString();
		}while(sentModInCNFCalc);*/
	String newSentence = sentence;
	//int count =1;
	do{
	    int i=0;
	    sentModInCNFCalc = false;
	    int length = newSentence.length();
	    while(i<length)
		{
		    int tokenEnd= getTokenEnd(newSentence,i);
		    String token = newSentence.substring(i,tokenEnd);
		    i=tokenEnd;
		    //	System.out.println("Token: " + token);
		
		    if(token.charAt(0)=='`')
			{
			    stack.push(token);
			}
		    else if(token.equals("&") && tokenEnd<length && newSentence.charAt(tokenEnd)=='|')
			{
			    String op3 = stack.pop();
			    String op2 = stack.pop();
			    String op1 = stack.pop();
			    String modSentence = op1+op2+"|"+op1+op3+"|"+"&";
			    stack.push(modSentence);
			    i+=1;
			    sentModInCNFCalc = true;
			}
		    else if(token.equals("|"))
			{
			    String op2 = stack.pop();
			    String op1 = stack.pop();
			    if(op1.charAt(op1.length()-1)=='&')
				{
				    int splitPosition = findParts(op1);
				    String beta = op1.substring(0,splitPosition);
				    String gamma = op1.substring(splitPosition,op1.length()-1);
				    String modSentence = beta+op2+"|"+gamma+op2+"|&";
				    stack.push(modSentence);
				    sentModInCNFCalc = true;
				}
			    else
				{
				    String modSentence = op1+op2+token;
				    stack.push(modSentence);
				}
			}
		    else if(token.equals("~"))
			{
			    String op = stack.pop();
			    String modSentence = op+"~";
			    stack.push(modSentence);
			}
		    else
			{
			    String op2 = stack.pop();
			    String op1 = stack.pop();
			    String modSentence = op1+op2+token;
			    stack.push(modSentence);
			}
		}
	    //   sentModInCNFCalc = false;
	    // count++;
	    newSentence = stack.pop();
	}while(sentModInCNFCalc);

	return newSentence;
	
    }

    public int findParts(String sentence)
    {
	Stack <String> s2 = new Stack<String>();
	int length =sentence.length()-1;
	int i=0;
	while(i<length)
	    {
		int tokenEnd= getTokenEnd(sentence,i);
		String token = sentence.substring(i,tokenEnd);
		i=tokenEnd;
		//	System.out.println("Token: " + token);
		
		if(token.charAt(0)=='`')
		    {
			s2.push(token);
		    }
		else if(token.equals("~"))
		    {
			String op = s2.pop();
			String modSentence = op+"~";
			s2.push(modSentence);
		    }
		else
		    {
			String op2 = s2.pop();
			String op1 = s2.pop();
			String modSentence = op1+op2+token;
			s2.push(modSentence);
		    }
	    }

	s2.pop();
	return s2.pop().length();
	
    }

    public String convertToCNF(String sentence)   //Input Sentence is in postfix format here
    {
	String step1 = performImplicationElimination(sentence);
	String step2 = performInwardNegation(step1);
	String step3 = applyDistributiveLaw(step2);
	return step3;
    }

    /*   public HashSet<String> splitIntoLiterals(String clauseString)
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

	return clause;
	
    }
    public void addSentenceToDatabase(String sentence, Database database)
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
					database.add(clause);
				    }
				clause = splitIntoLiterals(op2);
				database.add(clause);

				
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
	    {
		HashSet<String> clause = splitIntoLiterals(op);
		database.add(clause);
	    }
    }*/
}



class PredicateIdentifier
{
    int id;
    int noOfArgs;

    PredicateIdentifier()
    {
	id = -1;
	noOfArgs=0;
    }
    PredicateIdentifier(int id,int noOfArgs)
    {
	this.id = id;
	this.noOfArgs = noOfArgs;
    }
    
}

class LexicalAnalysis
{
    HashMap<String,PredicateIdentifier> lexicalTable;
    int totalNoOfPredicates;
    
    LexicalAnalysis()
    {
	lexicalTable = new HashMap<String,PredicateIdentifier>();
	totalNoOfPredicates = 0;
    }

    public int insertToLexicalTable(String predicateName,String[] parameters)
    {
	if(lexicalTable.containsKey(predicateName))
	    return lexicalTable.get(predicateName).id;
	else
	    {
		PredicateIdentifier id = new PredicateIdentifier(totalNoOfPredicates++,parameters.length);
		lexicalTable.put(predicateName,id);
		return id.id;
	    }
    }
    
    public String getLexicallyAnalysedString(String sentence)
    {
	String predicatePatternString= "([a-zA-Z]+?)\\s*([(])(.+?)([)])";
        Pattern predicatePattern = Pattern.compile(predicatePatternString);
        Matcher matcher = predicatePattern.matcher(sentence);
	StringBuffer analysedString = new StringBuffer();
	
        while(matcher.find())
	    {
		String sep = ",";
		Pattern seperator = Pattern.compile(sep);
		String params = matcher.group(3);
		String[] parameters = seperator.split(params);
		String predicateName = matcher.group(1);
		int id = insertToLexicalTable(predicateName,parameters);
		/*	String p = "";
			for(int i=0;i<parameters.length;i++)
			{
			p+= "," + parameters[i]+"@"+id;
			}*/
		String identifier = "`" + predicateName + "," + params +"`";
		matcher.appendReplacement(analysedString,identifier);
	    }
	matcher.appendTail(analysedString);
	return analysedString.toString();
    }
}


public class homework
{
    int noOfQueries;
    int noOfFacts;
    String[] queries;
    String[] facts;
    static long timePerQuery = 30000;   //milliseconds
    static long endTime;

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
		//	System.out.println("Postfix: " + cnfString);
		database.addSentenceToDatabase(cnfString);
	    }

	

	try{
	    File outputFile = new File("output.txt");
	    FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    String outputVal="";
	    for(int i=0;i<hw.noOfQueries;i++)
		{
		    homework.endTime = System.currentTimeMillis() + homework.timePerQuery;
		    String lexString = lex.getLexicallyAnalysedString(hw.queries[i]);
		    //	System.out.println(lexString);
		    Resolution res = new Resolution();
		    SyntaxAnalysis syntax = new SyntaxAnalysis();
		    String postfixString = syntax.convertToPostfix(lexString);
		    postfixString+="~";
		    String cnfString = syntax.convertToCNF(postfixString);
		    String result = res.performResolution(cnfString,database);
		   
		    bw.write(result);
		    bw.write("\n");
		    // System.out.println(result);
		}
	    //System.out.print(outputVal);
	    bw.close();
	}
	catch(IOException e)
	    {
		e.printStackTrace();
	    }
	    
    }
}
