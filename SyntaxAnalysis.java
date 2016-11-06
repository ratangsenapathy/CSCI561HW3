import java.util.Stack;
public class SyntaxAnalysis
{
    Stack<String>  stack;
    SyntaxAnalysis()
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

    public String performInwardNegation(String sentence)
    {
	String tempString=sentence;
	while(true)
	    {
		String pattern1="~~";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher matcher      = pattern.matcher(tempString);
		StringBuffer stringBuffer = new StringBuffer();

		while(matcher.find()){
		    matcher.appendReplacement(stringBuffer, "");
		    // System.out.println(stringBuffer.toString());
		}
		matcher.appendTail(stringBuffer);

		tempString = stringBuffer.toString();


		//--------------

		String pattern2 = "(.*?)&~";
	    }
    }

    public String convertToCNF(String sentence)   //Input Sentence is in postfix format here
    {
	String step1 = performImplicationElimination(sentence);
	String step2 = performInwardNegation(step1)
	return step2;
    }
}
