import java.util.Stack;

public class Compiler
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
