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

import static edu.ufl.cise.cop4020fa23.Kind.AND;
import static edu.ufl.cise.cop4020fa23.Kind.BANG;
import static edu.ufl.cise.cop4020fa23.Kind.BITAND;
import static edu.ufl.cise.cop4020fa23.Kind.BITOR;
import static edu.ufl.cise.cop4020fa23.Kind.COLON;
import static edu.ufl.cise.cop4020fa23.Kind.COMMA;
import static edu.ufl.cise.cop4020fa23.Kind.DIV;
import static edu.ufl.cise.cop4020fa23.Kind.EOF;
import static edu.ufl.cise.cop4020fa23.Kind.EQ;
import static edu.ufl.cise.cop4020fa23.Kind.EXP;
import static edu.ufl.cise.cop4020fa23.Kind.GE;
import static edu.ufl.cise.cop4020fa23.Kind.GT;
import static edu.ufl.cise.cop4020fa23.Kind.IDENT;
import static edu.ufl.cise.cop4020fa23.Kind.LE;
import static edu.ufl.cise.cop4020fa23.Kind.LPAREN;
import static edu.ufl.cise.cop4020fa23.Kind.LSQUARE;
import static edu.ufl.cise.cop4020fa23.Kind.LT;
import static edu.ufl.cise.cop4020fa23.Kind.MINUS;
import static edu.ufl.cise.cop4020fa23.Kind.MOD;
import static edu.ufl.cise.cop4020fa23.Kind.NUM_LIT;
import static edu.ufl.cise.cop4020fa23.Kind.OR;
import static edu.ufl.cise.cop4020fa23.Kind.PLUS;
import static edu.ufl.cise.cop4020fa23.Kind.QUESTION;
import static edu.ufl.cise.cop4020fa23.Kind.RARROW;
import static edu.ufl.cise.cop4020fa23.Kind.RES_blue;
import static edu.ufl.cise.cop4020fa23.Kind.RES_green;
import static edu.ufl.cise.cop4020fa23.Kind.RES_height;
import static edu.ufl.cise.cop4020fa23.Kind.RES_red;
import static edu.ufl.cise.cop4020fa23.Kind.RES_width;
import static edu.ufl.cise.cop4020fa23.Kind.RPAREN;
import static edu.ufl.cise.cop4020fa23.Kind.RSQUARE;
import static edu.ufl.cise.cop4020fa23.Kind.STRING_LIT;
import static edu.ufl.cise.cop4020fa23.Kind.BOOLEAN_LIT;
import static edu.ufl.cise.cop4020fa23.Kind.TIMES;
import static edu.ufl.cise.cop4020fa23.Kind.CONST;

import java.util.Arrays;

import edu.ufl.cise.cop4020fa23.ast.AST;
import edu.ufl.cise.cop4020fa23.ast.BinaryExpr;
import edu.ufl.cise.cop4020fa23.ast.BooleanLitExpr;
import edu.ufl.cise.cop4020fa23.ast.ChannelSelector;
import edu.ufl.cise.cop4020fa23.ast.ConditionalExpr;
import edu.ufl.cise.cop4020fa23.ast.ConstExpr;
import edu.ufl.cise.cop4020fa23.ast.ExpandedPixelExpr;
import edu.ufl.cise.cop4020fa23.ast.Expr;
import edu.ufl.cise.cop4020fa23.ast.IdentExpr;
import edu.ufl.cise.cop4020fa23.ast.NumLitExpr;
import edu.ufl.cise.cop4020fa23.ast.PixelSelector;
import edu.ufl.cise.cop4020fa23.ast.PostfixExpr;
import edu.ufl.cise.cop4020fa23.ast.StringLitExpr;
import edu.ufl.cise.cop4020fa23.ast.UnaryExpr;
import edu.ufl.cise.cop4020fa23.exceptions.LexicalException;
import edu.ufl.cise.cop4020fa23.exceptions.PLCCompilerException;
import edu.ufl.cise.cop4020fa23.exceptions.SyntaxException;
/**
Expr::=  ConditionalExpr | LogicalOrExpr    
ConditionalExpr ::=  ?  Expr  :  Expr  :  Expr 
LogicalOrExpr ::= LogicalAndExpr (    (   |   |   ||   ) LogicalAndExpr)*
LogicalAndExpr ::=  ComparisonExpr ( (   &   |  &&   )  ComparisonExpr)*
ComparisonExpr ::= PowExpr ( (< | > | == | <= | >=) PowExpr)*
PowExpr ::= AdditiveExpr ** PowExpr |   AdditiveExpr
AdditiveExpr ::= MultiplicativeExpr ( ( + | -  ) MultiplicativeExpr )*
MultiplicativeExpr ::= UnaryExpr (( * |  /  |  % ) UnaryExpr)*
UnaryExpr ::=  ( ! | - | length | width) UnaryExpr  |  UnaryExprPostfix
UnaryExprPostfix::= PrimaryExpr (PixelSelector | ε ) (ChannelSelector | ε )
PrimaryExpr ::=STRING_LIT | NUM_LIT |  IDENT | ( Expr ) | Z 
    ExpandedPixel  
ChannelSelector ::= : red | : green | : blue
PixelSelector  ::= [ Expr , Expr ]
ExpandedPixel ::= [ Expr , Expr , Expr ]
Dimension  ::=  [ Expr , Expr ]                         

 */

public class ExpressionParser implements IParser {
	
	final ILexer lexer;
	private IToken t;
	

	/**
	 * @param lexer
	 * @throws LexicalException 
	 */
	public ExpressionParser(ILexer lexer) throws LexicalException {
		super();
		this.lexer = lexer;
		t = lexer.next();
	}
	
	protected boolean isKind(Kind... kinds) {
		for (Kind k : kinds) {
			if (k == t.kind())
				return true;
		}
		return false;
	}
	
	void match(Kind kind) throws PLCCompilerException{
		boolean matched = false;
		if (isKind(kind)) {
			matched = true;
		}
		consume();
		if (matched == false) {
			throw new SyntaxException("Match Failed");
		}
	}
	
	void consume() throws LexicalException {
		this.t = lexer.next();
	}


	@Override
	public AST parse() throws PLCCompilerException {
		Expr e = Expr();
		return e;
	}


	/*private Expr expr() throws PLCCompilerException { // ConditionalExpr | LogicalOrExpr
		IToken firstToken = t;
		throw new UnsupportedOperationException("THE PARSER HAS NOT BEEN IMPLEMENTED YET");
	}*/
	
	PixelSelector PixelSelector() throws PLCCompilerException { // [ Expr, Expr ]
		IToken firstToken = t;
		Expr expr1 = null;
		Expr expr2 = null;
		PixelSelector expr3 = null;
		if(isKind(LSQUARE)) {
			consume();
			expr1 = Expr();
			match(COMMA);
			expr2 = Expr();
			match(RSQUARE);
			expr3 = new PixelSelector(firstToken, expr1, expr2);
			
		}
		//error??
		return expr3;
		
	}
	
	Expr ExpandedPixelSelector() throws PLCCompilerException { // [ Expr, Expr, Expr ]
		IToken firstToken = t;
		Expr red = null;
		Expr grn = null;
		Expr blu = null;
		Expr expr = null;
		if(isKind(LSQUARE)) {
			consume();
			red = Expr();
			match(COMMA);
			grn = Expr();
			match(COMMA);
			blu = Expr();
			match(RSQUARE);
			expr = new ExpandedPixelExpr(firstToken, red, grn, blu);
			
		}
		return expr;
		
	}

    Expr PrimaryExpr() throws PLCCompilerException { // String | Num | Bool | Ident | (Expr) | Const | ExpandedPixel
    	IToken firstToken = t;
    	Expr expr = null;
    	if (isKind(STRING_LIT)) {
    		expr = new StringLitExpr(firstToken);
    		consume();
    	}
    	else if (isKind(NUM_LIT)) {
    		expr = new NumLitExpr(firstToken);
    		consume();
    	}
    	else if (isKind(BOOLEAN_LIT)) {
    		expr = new BooleanLitExpr(firstToken);
    		consume();
    	}
    	else if (isKind(IDENT)) {
    		expr = new IdentExpr(firstToken);
    		consume();
    	}
    	else if (isKind(CONST)) {
    		expr = new ConstExpr(firstToken);
    		consume();
    	}
    	else if (isKind(LPAREN)) {
    		consume();
    		expr = Expr();
    		match(RPAREN);
    	}
    	else if (isKind(LSQUARE)) { //ExpandedPixel
    		expr = ExpandedPixelSelector();
    		//consume();
    	}
    	else {
    		throw new SyntaxException("EOF or invalid token");
    	}
		return expr;
    	
    }
    
    ChannelSelector ChannelSelector() throws PLCCompilerException {
    	IToken firstToken = t;
    	ChannelSelector channel = null;
    	if (isKind(COLON)) {
    		consume();
    		if (isKind(RES_red, RES_blue, RES_green)) {
    			IToken color = t;
    			consume();
    			channel = new ChannelSelector(firstToken, color);
    		}
    		else {
    			throw new SyntaxException("No color follows Colon");
    		}
    	}
    	return channel;
    }
    
    Expr PostfixExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr primary = null;
    	PixelSelector pixel = null;
    	ChannelSelector channel = null;
    	primary = PrimaryExpr();
    	if (isKind(LSQUARE)) { // pixel selector
    		pixel = PixelSelector();
    		//consume();
    	}
    	//System.out.print(t.kind());
    	if (isKind(COLON)) { // channel selector
    		channel = ChannelSelector();
    		consume();
    	}
    	if (pixel == null && channel == null) {
    		return primary;
    	}
    	PostfixExpr expr = new PostfixExpr(firstToken, primary, pixel, channel);
    	
    	return expr;
    }
    
    Expr UnaryExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr expr = null;
    	if (isKind(BANG, MINUS, RES_width, RES_height)) {
    		IToken op = t;
    		consume();
    		expr = UnaryExpr();
    		Expr unary = new UnaryExpr(firstToken, op, expr);
    		return unary;
    		//consume();
    	}
    	else { 
    		expr = PostfixExpr();
    	}
    	
    	return expr;
    }
    
    Expr MultiplicativeExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr expr1 = null;
    	Expr expr2 = null;
    	expr1 = UnaryExpr();
    	while (isKind(TIMES, DIV, MOD)) {
    		IToken op = t;
    		consume();
    		expr2 = UnaryExpr();
    		expr1 = new BinaryExpr(firstToken, expr1, op, expr2); // is this right?
    	}
    	return expr1;
    }
    
    Expr AdditiveExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr expr1 = null;
    	Expr expr2 = null;
    	expr1 = MultiplicativeExpr();
    	while (isKind(PLUS, MINUS)) {
    		IToken op = t;
    		consume();
    		expr2 = MultiplicativeExpr();
    		expr1 = new BinaryExpr(firstToken, expr1, op, expr2); // is this right?
    	}
    	return expr1;
    }
    
    Expr PowExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr expr1 = null;
    	Expr expr2 = null;
    	expr1 = AdditiveExpr();
    	if (isKind(EXP)) {
    		IToken op = t;
    		consume();
    		expr2 = PowExpr();
    		expr1 = new BinaryExpr(firstToken, expr1, op, expr2);
    	}
    	return expr1;
    }
    
    Expr ComparisonExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr expr1 = null;
    	Expr expr2 = null;
    	expr1 = PowExpr();
    	while (isKind(LT, GT, EQ, LE, GE)) {
    		IToken op = t;
    		consume();
    		expr2 = PowExpr();
    		expr1 = new BinaryExpr(firstToken, expr1, op, expr2); // is this right?
    	}
    	return expr1;
    }
    
    Expr LogicalAndExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr expr1 = null;
    	Expr expr2 = null;
    	expr1 = ComparisonExpr();
    	while (isKind(BITAND, AND)) {
    		IToken op = t;
    		consume();
    		expr2 = ComparisonExpr();
    		expr1 = new BinaryExpr(firstToken, expr1, op, expr2); // is this right?
    		
    	}
    	return expr1;
    }
    
    Expr LogicalOrExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr expr1 = null;
    	Expr expr2 = null;
    	expr1 = LogicalAndExpr();
    	while (isKind(BITOR, OR)) {
    		IToken op = t;
    		consume();
    		expr2 = LogicalAndExpr();
    		expr1 = new BinaryExpr(firstToken, expr1, op, expr2); // is this right?
    	}
    	return expr1;
    }
    
    Expr ConditionalExpr() throws PLCCompilerException {
    	IToken firstToken = t;
    	Expr guard = null;
    	Expr trueExpr = null;
    	Expr falseExpr = null;
    	if (isKind(QUESTION)) {
    		consume();
    		guard = Expr();
    		match(RARROW);
    		trueExpr = Expr();
    		match(COMMA);
    		falseExpr = Expr();
    	}
    	Expr expr = new ConditionalExpr(firstToken, guard, trueExpr, falseExpr);
    	return expr;
    }
    
    Expr Expr() throws PLCCompilerException {
    	Expr expr = null;
    	if (isKind(QUESTION)) {
    		expr = ConditionalExpr();
    	}
    	else {
    		expr = LogicalOrExpr();
    	}
    	return expr;
    }

}
