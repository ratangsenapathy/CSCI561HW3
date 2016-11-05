import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;

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
public class LexicalAnalysis
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
	String predicatePatternString= "([a-zA-Z]+?)([(])(.+?)([)])";
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
		String identifier = "`" + id + "," + params +"`";
		matcher.appendReplacement(analysedString,identifier);
	    }
	matcher.appendTail(analysedString);
	return analysedString.toString();
    }
}
