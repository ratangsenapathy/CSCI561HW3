import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Map;

class Literal
{
    String predicate;
    String[] parameters;
    boolean isPositive;

    Literal(String predicate, String parameterString,String type)
    {
	this.predicate = predicate;
	this.parameters = parameterString.split(",");
	this.isPositive = type.contains("~")?false:true;
    }

    Literal(String predicate,String[] parameters, String type)
    {
	this.predicate = predicate;
	int length=parameters.length;
	this.parameters = new String[length];
	for(int i=0;i<length;i++)
	    {
		this.parameters[i] = parameters[i];
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
		this.parameters[i] = parameters[i];
	    }
	this.isPositive = isPositive;
    }

    Literal(String predicate,String parameter,boolean isPositive)
    {
	this.predicate = predicate;
	this.parameters = new String[1];
	this.parameters[0] = parameter;
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
public class Resolution
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
	database.normalizeDatabase();
	Database tempDatabase = new Database();
	assignDatabase(tempDatabase,database);
	//	System.out.println("Display database:");
	//	database.displayDatabase();
	//	System.out.println("Dislayed database");
	HashSet<HashSet<String>> newClauses = new HashSet<HashSet<String>>();
	HashSet<String> resolvedSet = new HashSet<String>();
	//HashSet<HashSet<String>> usedClauses = new HashSet<HashSet<String>>();
	
	while(true)
	    {
		// Iterator<HashSet<String>> it1 = database.entries.iterator();
		int index1 = 0;
		//	System.out.println("Display database:");
		//	database.displayDatabase();
		//	System.out.println("Dislayed database");
		for(HashSet<String> clause1 : database.entries)
		    {
			//HashSet<String> clause1 = it1.next();

			//Iterator<HashSet<String>> it2=database.entries.iterator(it1.nextIndex());

			int index2 = 0;
			
			for(HashSet<String> clause2 : database.entries)
			    {
				if (index2++ <= index1)
				    {
					continue;
				    }
				
				//HashSet<String> clause2 =it2.next();
				/*	System.out.println("Resolve");
					for(String lit : clause1)
					{
					System.out.print(lit + "   ");
					}

					System.out.println("");
					for(String lit : clause2)
					{
					System.out.print(lit + "   ");
					}*/

				//	Scanner sc = new Scanner(System.in);
				//	sc.next();

				String entryLit="";
				for(String lit : clause1)
				    {
					entryLit+=lit;
				    }

				for(String lit : clause2)
				    {
					entryLit+=lit;
				    }
				if(!resolvedSet.contains(entryLit))
				    {
					HashSet<HashSet<String>> resolvents = resolve(clause1,clause2);
				
					if(resolvents == null)
					    return "True";
					//resolvedSet.add(entryLit);
					//	usedClauses.add(clause1);
					//usedClauses.add(clause2);
					newClauses.addAll(resolvents);
				    }

				//index2++;
			    }

			index1++;
		    }

		boolean isSubset = true;
		tempDatabase.entries.clear();
		//tempDatabase.addSentenceToDatabase(negatedQuery);
		assignDatabase(tempDatabase,database);
		for(HashSet<String> clause : newClauses)
		    {
			if(!database.entries.contains(clause))
			    {
				database.entries.add(clause);
				isSubset = false;
			    }
			if(!tempDatabase.entries.contains(clause))
			    {
				tempDatabase.entries.add(clause);
			    }
			
		    }
		database.normalizeDatabase();
		//database.addAll(newClauses);
		//database.displayDatabase();
		if(isSubset)
		    return "False";
	    }

	//return "False";
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
	/*	System.out.println("Resolve");
	for(String e1 : clause1)
	    {
		System.out.print(e1 + " ");
	    }
	System.out.println("");
	for(String e2 : clause2)
	    {
		System.out.print(e2 + " ");
	    }
	    System.out.println("");*/
	HashSet<HashSet<String>> resolvents = new HashSet<HashSet<String>>();
	for(String literal1 : clause1)
	    {
		for(String literal2 : clause2)
		    {
			//int literal1PredicateSeperatorIndex = literal1.indexOf(',');
			//int literal2PredicateSeperatorIndex = literal2.indexOf(',');
			
		

			Literal l1=getLiteralInfo(literal1);

			Literal l2=getLiteralInfo(literal2);
			//System.out.println(literal1+ " " + literal2);
			String predicate1 = l1.predicate;
			String predicate2 = l2.predicate;
			
			if(predicate1.equals(predicate2) && l1.isPositive != l2.isPositive)
			    {
				HashMap<String,String> bindings = new HashMap<String,String>();

				bindings = unify(l1,l2,bindings);
			
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
								String val = getBindingValue(p,bindings);
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
								String val = getBindingValue(p,bindings);
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


    public String getBindingValue(String var,HashMap<String,String> bindings)
    {
	if(bindings.containsKey(var))
	    {
		return bindings.get(var);//getBindingValue(bindings.get(var),bindings);
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

    public HashMap<String,String> unifyLiterals(Literal l1, Literal l2, HashMap<String,String> theta)
    {
	for(int i=0;i<l1.parameters.length;i++)
	    {
		String r = l1.parameters[i];
		String s = l2.parameters[i];
		
		if(!r.equals(s))
		    {
			if(isVariable(r))
			    {
				theta = union(theta,r,s);
				//	return unifyLiterals(substitution(theta,l1),substitution(theta,l2),theta);
			    }
			else if(isVariable(s))
			    {
				theta = union(theta,s,r);
				//	return unifyLiterals(substitution(theta,l1),substitution(theta,l2),theta);
			    }
			//else
			//return null;
		    }

		if(theta==null)
		    return theta;
	    }

	return theta;
    }

    public HashMap<String,String> union(HashMap<String,String> theta, String key, String value)
    {
	if(theta.containsKey(key))
	    {
		String v =theta.get(key);
		if(!(v.equals(value)))
		    {
			if(isVariable(v))
			    {
				theta = union(theta,v,value);
			    }
			else if(isVariable(value))
			    {
				theta = union(theta,value,v);
			    }
			else
			    theta=null;
		    }
		    
	    }
	else
	    {
		theta.put(key,value);
	    }

	return theta;
    }
}

//Likes(John,x) ~Likes(x,Elizabeth)
/*
Like(x,y) ~Like(Joe,Boa)
Like (x,y) ~Like(y,x)
Knows(John,x) Knows(y,z)
 */
