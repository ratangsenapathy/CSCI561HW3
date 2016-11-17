import java.util.HashSet;
import java.util.LinkedList;

public class Database
{
    HashSet<LinkedList<String>> entries;
    
    public Database()
    {
	entries = new HashSet<LinkedList<String>>();
    }

    public void add(LinkedList<String> clause)
    {
	if(!entries.contains(clause))
	    entries.add(clause);
    }

    public void displayDatabase()
    {
	int i=1;
	for(LinkedList<String> entry : entries)
	    {
		System.out.println("Entry "+i++);
		for(String literal : entry)
		    {
			System.out.print(literal + " ");
		    }
			    System.out.println("");
	    }
    }
    
}


