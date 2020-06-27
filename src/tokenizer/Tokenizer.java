package tokenizer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tokenizer.tokens.*;

public class Tokenizer {
	
	private static final String[] regexes = new String[] {                    //The actual regexes of given 
			"\"[^\"]*\"", //This is for Strings, since they have the highest precedence
			"[\\{\\}\\[\\]\\(\\);]", //punctuation
			"(0[a-z][0-9]*)|([0-9])",//Number
			
			"(==)|(>=)|(<=)|(!=)|(&&)|(\\|\\|)|" + 
			"(>)|(<)|" + 
			"(\\+)|(-)|(\\*)|(/)|(%)|(\\^)" +
			"(=)",                   //Binary operators
			
			"!",                     //Unary operators
			
			"[a-zA-Z]+\\w*"          //Unrecognized Sequence
	};
	private static final TokenGenerator[] generators = new TokenGenerator[] { //The returned tokens of respective regexes
			(contents) -> new Token("String", contents),
			(contents) -> new Token("Punct", contents),
			(contents) -> new Token("Number", contents),
			
			(contents) -> new Token("BinaryOperator", contents),
			(contents) -> new Token("UnaryOperator", contents),
			
			(contents) -> new Token("Sequence", contents),
	};
	
	public static ArrayList<Token> stepOneTokenize(String s) {
		ArrayList<Token> tokens = new ArrayList<>();
		
		String t = s;
		
		boolean matched = true;
		
		while(matched) {
			
			matched = false;
			
			int length = -1;
			int index  = Integer.MAX_VALUE;
			
			int regex  = -1;
			
			String foundString = null;
			
			for(int i  = 0; i < regexes.length; i++) {
				
				Pattern reg = Pattern.compile(regexes[i], Pattern.DOTALL);
				Matcher match = reg.matcher(t);
				
				if(match.find()) {
					
					String found = match.group();
					int indexOfFound = t.indexOf(found);
					
					if(indexOfFound < index) {                     //closest to the beginning of the string
						index = indexOfFound;
						length = found.length();
						regex = i;
						
						foundString = found;
					}
					else if(indexOfFound == index) {               //If match has same pos of another
						
						if(found.length() > length || i < regex) { //The longest match, or the highest regex precedence
							index = indexOfFound;
							length = found.length();
							regex = i;
							
							foundString = found;
						}
						
					}
				}
				
			}
			
			if(length != -1) {
				
				matched = true;                                      //we have matched at least one regex here
				
				t = t.substring(index + length);                     //snip out the code that isn't utilized
				tokens.add(generators[regex].getToken(foundString)); //Then get the token that applies
				
			}
			
		}
		
		return tokens;
	}
	
	@FunctionalInterface
	interface TokenGenerator {
		public Token getToken(String contents);
	}
	
	public static void main(String args[]) {
		
		ArrayList<Token> tokens = Tokenizer.stepOneTokenize(
					"\"Hello world\" what is this? != !hello () [ heeeellllloooooo ] a; ;;; ; bug lol \"\""
				);
		for(Token token : tokens) {
			System.out.println(token);
		}
		
	}
}
