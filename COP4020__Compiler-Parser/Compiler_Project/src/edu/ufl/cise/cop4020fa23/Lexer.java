/*Copyright 2023 by Beverly A Sanders
* 
* This code is provided for solely for use of students in COP4020 Programming Language Concepts at the 
* University of Florida during the fall semester 2023 as part of the course project.  
* 
* No other use is authorized. 
* 
* This code may not be posted on a public web site either during or after the course.  
*/
package edu.ufl.cise.cop4020fa23;

import static edu.ufl.cise.cop4020fa23.Kind.EOF;
import java.util.ArrayList;
import java.util.HashMap;
import edu.ufl.cise.cop4020fa23.exceptions.LexicalException;


public class Lexer implements ILexer {

	String input;
	static HashMap<String, Kind> reserved = new HashMap<String,Kind>();
	ArrayList<Token> tokens = new ArrayList<Token>();
	int counter = -1;
	boolean passThrough = false;
	
	public enum STATE {
		START, IN_IDENT, HAVE_ZERO, HAVE_DOT, IN_FLOAT, IN_NUM, HAVE_EQ, HAVE_MINUS,
		HAVE_LARROW, HAVE_RARROW, HAVE_AND, HAVE_VERTLINE, HAVE_STAR, HAVE_COLON, 
		HAVE_LBLOCK, HAVE_HASHTAG, IN_STRING, IN_COMMENT, ERROR
	}
	
	static {
		reserved.put("TRUE", Kind.BOOLEAN_LIT);
        reserved.put("FALSE", Kind.BOOLEAN_LIT);
        reserved.put("Z", Kind.CONST);
        reserved.put("BLACK", Kind.CONST);
        reserved.put("BLUE", Kind.CONST);
        reserved.put("CYAN", Kind.CONST);
        reserved.put("DARK_GRAY", Kind.CONST);
        reserved.put("GRAY", Kind.CONST);
        reserved.put("GREEN", Kind.CONST);
        reserved.put("LIGHT_GRAY", Kind.CONST);
        reserved.put("MAGENTA", Kind.CONST);
        reserved.put("ORANGE", Kind.CONST);
        reserved.put("PINK", Kind.CONST);
        reserved.put("RED", Kind.CONST);
        reserved.put("WHITE", Kind.CONST);
        reserved.put("YELLOW", Kind.CONST);
        reserved.put("image", Kind.RES_image);
        reserved.put("pixel", Kind.RES_pixel);
        reserved.put("int", Kind.RES_int);
        reserved.put("string", Kind.RES_string);
        reserved.put("void", Kind.RES_void);
        reserved.put("boolean", Kind.RES_boolean);
        reserved.put("write", Kind.RES_write);
        reserved.put("height", Kind.RES_height);
        reserved.put("width", Kind.RES_width);
        reserved.put("if", Kind.RES_if);
        reserved.put("fi", Kind.RES_fi);
        reserved.put("do", Kind.RES_do);
        reserved.put("od", Kind.RES_od);
        reserved.put("red", Kind.RES_red);
        reserved.put("green", Kind.RES_green);
        reserved.put("blue", Kind.RES_blue);
	}

	public Lexer(String input) {
		this.input = input;
	}

	@Override
	public IToken next() throws LexicalException {
		//ArrayList<Token> tokens = new ArrayList<Token>();
		ArrayList<Character> source = new ArrayList<Character>();
		
		//System.out.print(this.input + "\n");
		char[] chars = this.input.toCharArray();
		/*for(int i = 0; i<chars.length; i++) {
			System.out.print(chars[i]);
		}*/
		int pos = 0;
		STATE state = STATE.START;
		int column = 1;
		int line = 1;
		int tokenPos = 0;
		//System.out.print(chars.length);
		
		// should this be while(pos <= chars.length) ????
		while (true) {
			// loop over chars–assumes we have added a sentinel that will cause loop to terminate
			// each iteration increments pos (
			char ch = 0;
			//System.out.print(pos);
			if(pos < chars.length) {
				ch = chars[pos]; // get current char
				source.add(ch);
			}
			if (passThrough == true) {
				ch = 0;
				state = STATE.START;
			}
			/*if (passThrough == true) {
				System.out.print(tokens.size());
				for(int i = 0; i<tokens.size(); i++) {
					System.out.print(tokens.get(i).text());
				}
			}*/
			switch (state) {
				case START -> {
					int startPos = pos; //save position of first char in token
					switch (ch) {
						case ' ', '\r' -> {
							pos++;
							column++;
							//System.out.print(column);
							source.clear();
						}
						case '\n' -> {
							pos++;
							line++;
							column = 1;
							source.clear();
						}
						case '+' -> { //handle all single char tokens like this
							char[] source2 = {'+'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.PLUS, 0, 1, source2, location);
							tokens.add(token);
							column++;
							pos++;
							source.clear();
						}
						case ',' -> {
							char[] source2 = {','};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.COMMA, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case ';' -> {
							char[] source2 = {';'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.SEMI, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '?' -> {
							char[] source2 = {'?'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.QUESTION, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '(' -> {
							char[] source2 = {'('};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LPAREN, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case ')' -> {
							char[] source2 = {')'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RPAREN, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case ']' -> {
							char[] source2 = {']'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RSQUARE, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '!' -> {
							char[] source2 = {'!'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BANG, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '/' -> {
							char[] source2 = {'/'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.DIV, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '%' -> {
							char[] source2 = {'%'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.MOD, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '^' -> {
							char[] source2 = {'^'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RETURN, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '0' -> {
							char[] source2 = {'0'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.NUM_LIT, 0, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '[' -> {
							state = STATE.HAVE_LBLOCK;
							pos++;
						}
						case ':' -> {
							state = STATE.HAVE_COLON;
							pos++;
						}
						case '*' -> {
							state = STATE.HAVE_STAR;
							pos++;
						}
						case '|' -> {
							state = STATE.HAVE_VERTLINE;
							pos++;
						}
						case '&' -> {
							state = STATE.HAVE_AND;
							pos++;
						}
						case '<' -> {
							state = STATE.HAVE_LARROW;
							pos++;
						}
						case '>' -> {
							state = STATE.HAVE_RARROW;
							pos++;
						}
						case '-' -> {
							state = STATE.HAVE_MINUS;
							pos++;
						}
						case '=' -> {
							state = STATE.HAVE_EQ;
							pos++;
						}
						case '#' -> {
							state = STATE.HAVE_HASHTAG;
							tokenPos = pos;
							pos++;
						}
						case '"' -> {
							state = STATE.IN_STRING;
							tokenPos = pos;
							pos++;
						}
						case '1','2','3','4','5','6','7','8','9' -> {
							state = STATE.IN_NUM;
							tokenPos = pos;
							pos++;
						}
						case 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
							 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '$', '_'  -> {
							state = STATE.IN_IDENT;
							tokenPos = pos;
							pos++;
						}
						case 0 -> {//this is the end of the input, add an EOF token and return;
							Token token = new Token(Kind.EOF, 0, 0, null, new SourceLocation(line,column));
							tokens.add(token);
							source.clear();
							counter++;
							passThrough = true;
							//System.out.print(counter + " " + tokens.size());
							/*for(int i = 0; i<tokens.size(); i++) {
								if (tokens.get(i) == null) {
									System.out.print("bruh what");
								}
								System.out.print(tokens.get(i).sourceLocation() + "\n");
							}*/
							if (tokens.get(counter).kind == Kind.ERROR) {
								throw new LexicalException("Error Token");
							}
							return tokens.get(counter);
							}
						default -> { //cases like @
							Token token = new Token (Kind.ERROR, 0, 0, null, new SourceLocation (line,column));
							tokens.add(token);
							source.clear();
							pos++;
							}
						}
					}
				case IN_STRING -> {
					if (ch == '"') { // if the string is completed 
						// it is apart of the string
						int length = pos-tokenPos +1;
						SourceLocation location = new SourceLocation(line,column);
						char [] source2 = new char[source.size()];
						for (int i = 0; i < source.size(); i++) {
				            source2[i] = source.get(i);
				        }
						Token token = new Token(Kind.STRING_LIT, 0, length, source2, location);
						tokens.add(token);
						source.clear();
						state = STATE.START;
						pos++;
					}
					else if((int)ch >= 32 && (int)ch < 127) { // if ch is printable and not DEL
						pos++; //still in number,
						//increment pos to read next char
					}
					else { // throw error
						Token token = new Token(Kind.ERROR, 0, 0, null, new SourceLocation(line,column));
						tokens.add(token);
						source.clear();
						state = STATE.START;
						pos++;
					}
				}
				case IN_IDENT-> {
					switch(ch) {
						case '0','1','2','3','4','5','6','7','8','9','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
						     'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '$', '_' -> {
						    pos++; //still in number,
							//increment pos to read next char
						}
						default -> {
							int length = pos-tokenPos;
							SourceLocation location = new SourceLocation(line,column);
							String word = "";
							if (source.size() != 1) {
								char [] source2 = new char[source.size() - 1];
								for (int i = 0; i < source.size() - 1; i++) {
						            source2[i] = source.get(i);
						            word = word + source.get(i);
						        }
								Kind value = reserved.get(word);
								/*System.out.print(value);
								System.out.print(word);
								System.out.print(word.length());*/
								if (value != null) { // this is a reserved word
									Token token = new Token(value, 0, length, source2, location);
									tokens.add(token);
								}
								else { // not a reserved word
									Token token = new Token(Kind.IDENT, 0, length, source2, location);
									tokens.add(token);
								}
								//next char is not part of this token,
								//so do not increment pos
								column += length;
								state = STATE.START;
								source.clear();
							}
							else {
								char [] source2 = new char[source.size()];
								for (int i = 0; i < source.size(); i++) {
						            source2[i] = source.get(i);
						            word = word + source.get(i);
						        }
								Kind value = reserved.get(word);
								/*System.out.print(value);
								System.out.print(word);
								System.out.print(word.length());*/
								if (value != null) { // this is a reserved word
									Token token = new Token(value, 0, length, source2, location);
									tokens.add(token);
								}
								else { // not a reserved word
									Token token = new Token(Kind.IDENT, 0, length, source2, location);
									tokens.add(token);
								}
								//next char is not part of this token,
								//so do not increment pos
								column += length;
								state = STATE.START;
								source.clear();
							}
							
						}
					}
				}
				case HAVE_ZERO -> {
				}
				case HAVE_DOT -> {
					switch(ch) {
						case '0','1','2','3','4','5','6','7','8','9' -> {
							pos++; //still in number,
							//increment pos to read next char
						}
						default -> {
							int length = pos-tokenPos;
							char [] source2 = new char[source.size()];
							for (int i = 0; i < source.size(); i++) {
					            source2[i] = source.get(i);
					        }
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.NUM_LIT, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_RARROW -> {
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '=' -> {
							int length = 2;
							char[] source2 = {'>','='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.GE, 0, length, source2, location);
							tokens.add(token);
							state = STATE.START;
							pos++;
							column+=2;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'>'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.GT, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_LARROW -> {
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '=' -> {
							int length = 2;
							char[] source2 = {'<','='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LE, 0, length, source2, location);
							tokens.add(token);
							pos++;
							column+=2;
							state = STATE.START;
							source.clear();
						}
						case ':' -> {
							int length = 2;
							char[] source2 = {'<',':'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BLOCK_OPEN, 0, length, source2, location);
							tokens.add(token);
							pos++;
							column+=2;
							state = STATE.START;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'<'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LT, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_MINUS -> {
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '>' -> {
							int length = 2;
							char[] source2 = {'-','>'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RARROW, 0, length, source2, location);
							tokens.add(token);
							state = STATE.START;
							pos++;
							column+=2;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'-'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.MINUS, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_AND ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '&' -> {
							int length = 2;
							char[] source2 = {'&','&'};
							//System.out.print(line + " " + column);
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.AND, 0, length, source2, location);
							tokens.add(token);
							pos++;
							column += 2;
							source.clear();
							state = STATE.START;
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'&'};
							//System.out.print(line + " " + column);
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BITAND, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_VERTLINE ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '|' -> {
							int length = 2;
							char[] source2 = {'|','|'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.OR, 0, length, source2, location);
							tokens.add(token);
							state = STATE.START;
							pos++;
							column+=2;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'|'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BITOR, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_STAR ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '*' -> {
							int length = 2;
							char[] source2 = {'*','*'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.EXP, 0, length, source2, location);
							tokens.add(token);
							state = STATE.START;
							pos++;
							column+=2;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'*'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.TIMES, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_COLON ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '>' -> {
							int length = 2;
							char[] source2 = {':','>'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BLOCK_CLOSE, 0, length, source2, location);
							tokens.add(token);
							pos++;
							column+=2;
							state = STATE.START;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {':'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.COLON, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_LBLOCK ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case ']' -> {
							int length = 2;
							char[] source2 = {'[',']'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BOX, 0, length, source2, location);
							tokens.add(token);
							pos++;
							column+=2;
							source.clear();
							state = STATE.START;
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'['};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LSQUARE, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_HASHTAG ->{
					switch(ch) {
						case '#' -> {
							state = STATE.IN_COMMENT;
							pos++;
							column++;
						}
						default -> {
							//handle error, this time it is not a valid token
							Token token = new Token(Kind.ERROR, 0, 0, null, new SourceLocation(line,column));
							tokens.add(token);
							source.clear();
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case IN_COMMENT -> {
					if((int)ch >= 32 && (int)ch < 127) { // if ch is printable and not DEL
						pos++; //still in number,
						//increment pos to read next char
					}
					else if (ch == '\n'){ // end of comment
						/*int length = pos-tokenPos;
						SourceLocation location = new SourceLocation(line,column);
						char [] source2 = new char[source.size()];
						for (int i = 0; i < source.size(); i++) {
				            source2[i] = source.get(i);
				        }
						Token token = new Token(Kind., tokenPos, length, source2, location);
						tokens.add(token);
						source.clear();
						state = STATE.START;
						pos++;*/
						state = STATE.START;
					}
					else {
						// handle error
						state = STATE.START;
						
					}
				}
				case HAVE_EQ ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '=' -> {
							int length = 2;
							char[] source2 = {'=','='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.EQ, 0, length, source2, location);
							tokens.add(token);
							state = STATE.START;
							pos++;
							column+=2;
							source.clear();
						}
						default -> { // create an equal sign but don't change pos
							//handle error
							int length = 1;
							char[] source2 = {'='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.ASSIGN, 0, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column++;
							state = STATE.START;
							source.clear();
						}
					}
				}
				case IN_NUM -> {
					switch(ch) {
						case '0','1','2','3','4','5','6','7','8','9' -> {
							pos++; //still in number,
							//increment pos to read next char
						}
						case '.' -> {
							state = STATE.HAVE_DOT;
							pos++;
							column++;
						}
						default -> {
							boolean overflow = false;
							int length = pos - tokenPos;
							String number = "";
							//System.out.print(length + " ");
							char [] source2 = new char[source.size()];
							for (int i = 0; i < source.size(); i++) {
								if (source.get(i) != ' ' && source.get(i) != '\n' && (int)source.get(i) >= 48 && (int)source.get(i) <= 57) {
									source2[i] = source.get(i);
						            number = number + source.get(i);
						            //System.out.print(source2[i] +"\n");
						            //System.out.print((int)source2[i]);
								}
					            
					        }
							int number2 = 0;
							//System.out.print(number);
							try { // if this results in overflow, catch the exception
								number2 = Integer.parseInt(number);
							}
							catch (Exception e) {
								Token token = new Token(Kind.ERROR, 0, length, source2, new SourceLocation(line,column));
								tokens.add(token);
								overflow = true;
							}
							if (overflow == false) {
								Token token = new Token(Kind.NUM_LIT, 0, length, source2, new SourceLocation(line,column));
								tokens.add(token);
							}
							//next char is not part of this token,
							//so do not increment pos
							// increment col to start new token
							column += length;
							state = STATE.START;
							source.clear();
						}
					}
				}

				default -> throw new IllegalStateException("lexer bug");
				}//end of switch on state
			}//end of while
		//return new Token(EOF, 0, 0, null, new SourceLocation(1, 1));
	}
	
	public ArrayList<Token> Tokenize() {
		ArrayList<Token> tokens = new ArrayList<Token>();
		ArrayList<Character> source = new ArrayList<Character>();
		
		char[] chars = this.input.toCharArray();
		int pos = 0;
		STATE state = STATE.START;
		int column = 0;
		int line = 0;
		int tokenPos = 0;
		
		while (true) {
			// loop over chars–assumes we have added a sentinel that will cause loop to terminate
			// each iteration increments pos (
			char ch = chars[pos]; // get current char
			source.add(ch);
			switch (state) {
				case START -> {
					int startPos = pos; //save position of first char in token
					switch (ch) {
						case ' ', '\t', '\r' -> {
							pos++;
							column++;
						}
						case '\n' -> {
							pos++;
							line++;
							column = 0;
						}
						case '+' -> { //handle all single char tokens like this
							char[] source2 = {'+'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.PLUS, startPos, 1, source2, location);
							tokens.add(token);
							column++;
							pos++;
							source.clear();
						}
						case ',' -> {
							char[] source2 = {','};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.COMMA, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case ';' -> {
							char[] source2 = {';'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.SEMI, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '?' -> {
							char[] source2 = {'?'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.QUESTION, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '(' -> {
							char[] source2 = {'('};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LPAREN, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case ')' -> {
							char[] source2 = {')'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RPAREN, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case ']' -> {
							char[] source2 = {']'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RSQUARE, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '!' -> {
							char[] source2 = {'!'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BANG, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '/' -> {
							char[] source2 = {'/'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.DIV, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '%' -> {
							char[] source2 = {'%'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.MOD, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '^' -> {
							char[] source2 = {'^'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RETURN, startPos, 1, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case '[' -> {
							state = STATE.HAVE_LBLOCK;
							pos++;
							column++;
						}
						case ':' -> {
							state = STATE.HAVE_COLON;
							pos++;
							column++;
						}
						case '*' -> {
							state = STATE.HAVE_STAR;
							pos++;
							column++;
						}
						case '|' -> {
							state = STATE.HAVE_VERTLINE;
							pos++;
							column++;
						}
						case '&' -> {
							state = STATE.HAVE_AND;
							pos++;
							column++;
						}
						case '<' -> {
							state = STATE.HAVE_LARROW;
							pos++;
							column++;
						}
						case '>' -> {
							state = STATE.HAVE_LARROW;
							pos++;
							column++;
						}
						case '-' -> {
							state = STATE.HAVE_MINUS;
							pos++;
							column++;
						}
						case '=' -> {
							state = STATE.HAVE_EQ;
							pos++;
							column++;
						}
						case '"' -> {
							state = STATE.IN_STRING;
							tokenPos = pos;
							pos++;
							column++;
						}
						case '0','1','2','3','4','5','6','7','8','9' -> {
							state = STATE.IN_NUM;
							tokenPos = pos;
							pos++;
							column++;
						}
						case 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
							 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '$', '_'  -> {
							state = STATE.IN_IDENT;
							tokenPos = pos;
							pos++;
							column++;
						}
						case 0 -> {//this is the end of the input, add an EOF token and return;
							Token token = new Token(Kind.EOF, 0, 0, null, new SourceLocation(line,column));
							tokens.add(token);
							source.clear();
							return tokens;
							}
						}
					}
				case IN_STRING -> {
					if((int)ch >= 32 && (int)ch < 127) { // if ch is printable and not DEL
						pos++; //still in number,
						//increment pos to read next char
					}
					else if (ch == '"') { // if the string is completed 
						// it is apart of the string
						int length = pos-tokenPos;
						SourceLocation location = new SourceLocation(line,column);
						char [] source2 = new char[source.size()];
						for (int i = 0; i < source.size(); i++) {
				            source2[i] = source.get(i);
				        }
						Token token = new Token(Kind.STRING_LIT, tokenPos, length, source2, location);
						tokens.add(token);
						source.clear();
						state = STATE.START;
						pos++;
					}
					else { // throw error
						//Token token
					}
				}
				case IN_IDENT-> {
					switch(ch) {
						case '0','1','2','3','4','5','6','7','8','9','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
						     'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '$', '_' -> {
						    pos++; //still in number,
							//increment pos to read next char
						}
						default -> {
							int length = pos-tokenPos;
							SourceLocation location = new SourceLocation(line,column);
							String word = "";
							char [] source2 = new char[source.size()];
							for (int i = 0; i < source.size(); i++) {
					            source2[i] = source.get(i);
					            word = word + source.get(i);
					        }
							Kind value = reserved.get(word);
							if (value != null) { // this is a reserved word
								Token token = new Token(value, tokenPos, length, source2, location);
								tokens.add(token);
							}
							else { // not a reserved word
								Token token = new Token(Kind.IDENT, tokenPos, length, source2, location);
								tokens.add(token);
							}
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_ZERO -> {
				}
				case HAVE_DOT -> {
					switch(ch) {
						case '0','1','2','3','4','5','6','7','8','9' -> {
							pos++; //still in number,
							//increment pos to read next char
						}
						default -> {
							int length = pos-tokenPos;
							char [] source2 = new char[source.size()];
							for (int i = 0; i < source.size(); i++) {
					            source2[i] = source.get(i);
					        }
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.NUM_LIT, tokenPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_RARROW -> {
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '=' -> {
							int length = 2;
							char[] source2 = {'>','='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.GE, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'>'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.GT, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_LARROW -> {
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '=' -> {
							int length = 2;
							char[] source2 = {'<','='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LE, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						case ':' -> {
							int length = 2;
							char[] source2 = {'<',':'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BLOCK_OPEN, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'<'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LT, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_MINUS -> {
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '>' -> {
							int length = 2;
							char[] source2 = {'-','>'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.RARROW, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'-'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.MINUS, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_AND ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '&' -> {
							int length = 2;
							char[] source2 = {'&','&'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.AND, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'&'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BITAND, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_VERTLINE ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '|' -> {
							int length = 2;
							char[] source2 = {'|','|'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.OR, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'|'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BITOR, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_STAR ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '*' -> {
							int length = 2;
							char[] source2 = {'*','*'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.EXP, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'*'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.TIMES, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_COLON ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '>' -> {
							int length = 2;
							char[] source2 = {':','>'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BLOCK_CLOSE, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {':'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.COLON, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_LBLOCK ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case ']' -> {
							int length = 2;
							char[] source2 = {'[',']'};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.BOX, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> {
							//handle error
							int length = 1;
							char[] source2 = {'['};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.LSQUARE, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case HAVE_EQ ->{
					int startPos = pos - 1; //save position of first char in token
					switch(ch) {
						case '=' -> {
							int length = 2;
							char[] source2 = {'=','='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.EQ, startPos, length, source2, location);
							tokens.add(token);
							pos++;
							column++;
							source.clear();
						}
						default -> { // create an equal sign but don't change pos
							//handle error
							int length = 1;
							char[] source2 = {'='};
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.ASSIGN, startPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}
				case IN_NUM -> {
					switch(ch) {
						case '0','1','2','3','4','5','6','7','8','9' -> {
							pos++; //still in number,
							//increment pos to read next char
						}
						case '.' -> {
							state = STATE.HAVE_DOT;
							pos++;
							column++;
						}
						default -> {
							int length = pos - tokenPos;
							char [] source2 = new char[source.size()];
							for (int i = 0; i < source.size(); i++) {
					            source2[i] = source.get(i);
					        }
							SourceLocation location = new SourceLocation(line,column);
							Token token = new Token(Kind.NUM_LIT, tokenPos, length, source2, location);
							tokens.add(token);
							//next char is not part of this token,
							//so do not increment pos
							state = STATE.START;
							source.clear();
						}
					}
				}

				default -> throw new IllegalStateException("lexer bug");
				}//end of switch on state
			}//end of while
		
	}
}
