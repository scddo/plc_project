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

import edu.ufl.cise.cop4020fa23.ast.*;
import edu.ufl.cise.cop4020fa23.exceptions.LexicalException;
import edu.ufl.cise.cop4020fa23.exceptions.PLCCompilerException;
import edu.ufl.cise.cop4020fa23.exceptions.SyntaxException;

import static edu.ufl.cise.cop4020fa23.Kind.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser implements IParser {
	
	final ILexer lexer;
	private IToken t;

	public Parser(ILexer lexer) throws LexicalException {
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
		AST e = Program();
		return e;
	}

	/*private AST program() throws PLCCompilerException {
		throw new UnsupportedOperationException();
	}*/
	
	Dimension Dimension() throws PLCCompilerException { // [ Expr, Expr ]
		IToken firstToken = t;
		Expr expr1 = null;
		Expr expr2 = null;
		Dimension expr3 = null;
		if(isKind(LSQUARE)) {
			consume();
			expr1 = Expr();
			match(COMMA);
			expr2 = Expr();
			match(RSQUARE);
			expr3 = new Dimension(firstToken, expr1, expr2);
			
		}
		//error??
		return expr3;
		
	}
	
	LValue LValue() throws PLCCompilerException { 
		IToken firstToken = t;
    	IToken name = null;
    	PixelSelector pixel = null;
    	ChannelSelector channel = null;
    	if (isKind(IDENT)) {
    		name = t;
    		consume();
    	}
    	if (isKind(LSQUARE)) { // pixel selector
    		pixel = PixelSelector();
    		//consume();
    	}
    	//System.out.print(t.kind());
    	if (isKind(COLON)) { // channel selector
    		channel = ChannelSelector();
    		//consume();
    	}
    	LValue expr = new LValue(firstToken, name, pixel, channel);
    	
    	return expr;
	}
	
	Statement BlockStatement() throws PLCCompilerException {
		IToken firstToken = t;
		Block block = Block();
		return new StatementBlock(firstToken, block);
	}
	
	GuardedBlock GuardedBlock() throws PLCCompilerException {
		IToken firstToken = t;
		Expr guard = null;
		Block block = null;
		guard = Expr();
		match(RARROW);
		block = Block();
		
		GuardedBlock guarded = new GuardedBlock(firstToken, guard, block);
		return guarded;
	}
	
	Statement Statement() throws PLCCompilerException {
		IToken firstToken = t;
		LValue lValue = null;
		Expr expr = null;
		GuardedBlock guarded = null;
		List<GuardedBlock> list = new ArrayList<GuardedBlock>();
		Statement block = null;
		if(isKind(IDENT)) { // it is an LValue
			lValue = LValue();
			match(ASSIGN);
			expr = Expr();
			AssignmentStatement assign = new AssignmentStatement(firstToken, lValue, expr);
			return assign;
		}
		else if(isKind(RES_write)) {
			//IToken write = t;
			consume();
			expr = Expr();
			WriteStatement writeStmt = new WriteStatement(firstToken, expr);
			return writeStmt;
		}
		else if (isKind(RES_do)) {
			//IToken Do = t;
			consume();
			guarded = GuardedBlock();
			list.add(guarded);
			while(isKind(BOX)) {
				IToken box = t;
				consume();
				GuardedBlock guard = GuardedBlock()
;				list.add(guard);
			}
			match(RES_od);
			DoStatement doStmt = new DoStatement(firstToken, list);
			return doStmt;
		}
		else if(isKind(RES_if)) {
			//IToken ifToken = t;
			consume();
			guarded = GuardedBlock();
			list.add(guarded);
			while(isKind(BOX)) {
				IToken box = t;
				consume();
				GuardedBlock guard = GuardedBlock()
;				list.add(guard);
			}
			match(RES_fi);
			IfStatement ifStmt = new IfStatement(firstToken, list);
			return ifStmt;
		}
		else if (isKind(RETURN)) {
			consume();
			expr = Expr();
			return new ReturnStatement(firstToken, expr);
		}
		else {
			block = BlockStatement();
		}
		return block;
	}
	
	IToken Type() throws PLCCompilerException {
		IToken firstToken = t;
		//System.out.print(t);
		if (isKind(RES_image, RES_pixel, RES_int, RES_string, RES_void, RES_boolean)) {
			consume();
			return firstToken;
		}
		else {
			throw new SyntaxException("Type is not of specified type!");
		}
	}
	
	NameDef NameDef() throws PLCCompilerException {
		IToken firstToken = t;
		IToken typeToken = null;
		Dimension dimension = null;
		IToken identToken = null;
		typeToken = Type();
		if (isKind(IDENT)) {
			identToken = t;
			consume();
		}
		else if(isKind(LSQUARE)) {
			dimension = Dimension();
			if (isKind(IDENT)) {
				identToken = t;
				consume();
			}
		}
		else {
			throw new SyntaxException("Cannot verify token in NameDef");
		}
		return new NameDef(firstToken, typeToken, dimension, identToken);
	}
	
	Declaration Declaration() throws PLCCompilerException {
		IToken firstToken = t;
		NameDef nameDef = null;
		Expr expr = null;
		nameDef = NameDef();
		if (isKind(ASSIGN)) {
			consume();
			expr = Expr();
		}
		
		return new Declaration(firstToken, nameDef, expr);
	}
	
	List<NameDef> ParamList() throws PLCCompilerException {
		IToken firstToken = t;
		NameDef nameDef = null;
		List<NameDef> params = new ArrayList<NameDef>();
		//System.out.print(t);
		if (isKind(RES_image, RES_pixel, RES_int, RES_string, RES_void, RES_boolean)) { // if its a type
			nameDef = NameDef();
			params.add(nameDef);
			while (isKind(COMMA)) {
				consume();
				nameDef = NameDef();
				params.add(nameDef);
			}
		}
		return params;
	}
	
	Block Block() throws PLCCompilerException {
		IToken firstToken = t;
		List<Block.BlockElem> elems = new ArrayList<Block.BlockElem>();
		Block.BlockElem elem = null;
		if (isKind(BLOCK_OPEN)) {
			consume();
			while (isKind(RES_image, RES_pixel, RES_int, RES_string, RES_void, RES_boolean, IDENT, RES_write, RES_do, RES_if, RETURN, BLOCK_OPEN)) { // while declaration or statement
				if (isKind(RES_image, RES_pixel, RES_int, RES_string, RES_void, RES_boolean)) { // if declaration
					elem = Declaration();
					elems.add(elem);
					if (t.kind() != Kind.BLOCK_CLOSE) {
						consume(); // consumes the semicolon
					}
				}
				else {
					elem = Statement();
					elems.add(elem);
					consume(); // consumes the semicolon
				}
			}
			System.out.print(t);
			match(BLOCK_CLOSE);
		}
		return new Block(firstToken, elems);
	}
	
	Program Program() throws PLCCompilerException {
		IToken firstToken = t;
		IToken typeToken = null;
		IToken name = null;
		List<NameDef> params = new ArrayList<NameDef>();
		Block block = null;
		typeToken = Type();
		if (isKind(IDENT)) {
			name = t;
			//System.out.print(name);
			consume();
			//System.out.print(t);
			if (isKind(LPAREN)) {
				consume();
				params = ParamList();
				//System.out.print(t);
				match(RPAREN);
				block = Block();
			}
		}
		// Check EOF token
		if (isKind(EOF) != true) {
			throw new SyntaxException("Not EOF!");
		}

		if (name == null) {
			throw new SyntaxException("Name is null!");
		}

		return new Program(firstToken, typeToken, name, params, block);
	}
	
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
