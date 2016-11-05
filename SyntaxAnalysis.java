import java.util.Stack;
public class SyntaxAnalysis
{
    Stack<String>  stack;
    SyntaxAnalysis()
    {
	stack = new Stack<String>();
    }


    public int getInfixTokenEnd(String sentence,int startIndex)
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
		    return 3;
    }
     
    public String  convertToPostfix(String sentence)
    {
	String postfixSentence = "";
	int length = sentence.length();
	int i=0;
	while(i<length)
	    {
		int tokenEnd= getInfixTokenEnd(sentence,i);
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
}
