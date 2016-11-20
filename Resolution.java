import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.HashSet;

public class Resolution
{
    //Stack<String> stack;
    Database database;
    public Resolution()
    {
	//stack = new Stack<String>();
	this.database = new Database();
	
    }

    public void addQuery(String query)
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
	database.addSentenceToDatabase(negatedQuery);
	    
	
    }

    public void assignDatabase(Database db)
    {
	for(HashSet<String> entry : db.entries)
	    {
		database.entries.add(entry);
	    }
    }

    public String performResolution(String query,Database db)
    {
	assignDatabase(db);
	addQuery(query);
		System.out.println("Display database:");
		database.displayDatabase();
		System.out.println("Dislayed database");
	HashSet<HashSet<String>> newClauses = new HashSet<HashSet<String>>();

	while(true)
	    {
		// Iterator<HashSet<String>> it1 = database.entries.iterator();
		int index1 = 0;
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

				HashSet<HashSet<String>> resolvents = resolve(clause1,clause2);
				if(resolvents == null)
				    return "True";
				newClauses.addAll(resolvents);

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
		//database.addAll(newClauses);
		//database.displayDatabase();
		if(isSubset)
		    return "False";
	    }

	//return "False";
    }

    public HashSet<HashSet<String>> resolve(HashSet<String> clause1, HashSet<String> clause2)
    {
	HashSet<HashSet<String>> resolvents = new HashSet<HashSet<String>>();
	for(String literal1 : clause1)
	    {
		for(String literal2 : clause2)
		    {

			String predicate1 = literal1.substring(1,literal1.indexOf(','));
			String predicate2 = literal2.substring(1,literal2.indexOf(','));
			//	System.out.println("literal 1: " + literal1 + "literal 2: " + literal2);
			//System.out.println("Predicate 1: " + predicate1 + "Predicate 2: " + predicate2);
			
			if(predicate1.equals(predicate2) && literal1.charAt(literal1.length()-1) != literal2.charAt(literal2.length()-1))
			    {
				
				HashSet<String> resolvent=new HashSet<String>();

				for(String literal : clause1)
				    {
					if(!literal.equals(literal1))
					    {
						resolvent.add(literal);
					    }
				    }

				for(String literal : clause2)
				    {
					if(!literal.equals(literal2))
					    {
						resolvent.add(literal);
					    }
				    }
				
				
				if(resolvent.size()==0)
				    return null;
				
				resolvents.add(resolvent);
			    }
		    }
	    }
	return resolvents;
    }
}
