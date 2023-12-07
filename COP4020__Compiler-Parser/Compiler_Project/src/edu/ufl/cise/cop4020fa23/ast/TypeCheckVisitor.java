package edu.ufl.cise.cop4020fa23.ast;

import edu.ufl.cise.cop4020fa23.exceptions.PLCCompilerException;
import edu.ufl.cise.cop4020fa23.exceptions.TypeCheckException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.ufl.cise.cop4020fa23.Kind;
import edu.ufl.cise.cop4020fa23.ast.Block.BlockElem;


public class TypeCheckVisitor implements ASTVisitor{
	SymbolTable st = new SymbolTable();
	
	private void check(boolean b, Object dimension, String string) throws PLCCompilerException {
		// TODO Auto-generated method stub
		if (b == false) {
			throw new TypeCheckException("Check returned false!");
		}
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignmentStatement, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Boolean AS = false;
		st.enterScope();
		st.printStack();
		Type lvalType = (Type) assignmentStatement.getlValue().visit(this, arg);
		Type expr = (Type) assignmentStatement.getE().visit(this, arg);
		switch (lvalType) {
			case PIXEL -> {
				if (expr == Type.INT) {
					AS = true;
				}
				else if (expr == lvalType) {
					AS = true;
				}
				else {
					throw new TypeCheckException("AS was not true!");
				}
			}
			case IMAGE -> {
				if (expr == Type.PIXEL || expr == Type.INT || expr == Type.STRING) {
					AS = true;
				}
				else if (expr == lvalType) {
					AS = true;
				}
				else {
					throw new TypeCheckException("AS was not true!");
				}
			}
		}
		st.leaveScope();
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
		return AS;
	}

	@Override
	public Object visitBinaryExpr(BinaryExpr binaryExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = null;
		Type left =  (Type) binaryExpr.getLeftExpr().visit(this, arg);
		Type right = (Type) binaryExpr.getRightExpr().visit(this,arg);
		Kind opKind = binaryExpr.getOpKind();
		switch(left) {
			case PIXEL -> {
				switch(opKind) {
					case BITAND, BITOR -> {
						if (right == Type.PIXEL) {
							binaryExpr.setType(Type.PIXEL);
							return type = Type.PIXEL;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case EXP -> {
						if (right == Type.INT) {
							binaryExpr.setType(Type.PIXEL);
							return type = Type.PIXEL;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case TIMES, DIV, MOD ->{
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else if (right == Type.INT) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case MINUS->{
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case EQ -> {
						if (left == right) {
							binaryExpr.setType(Type.BOOLEAN);
							return type = Type.BOOLEAN;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case PLUS -> {
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
				}
			}
			case BOOLEAN -> {
				switch (opKind) {
					case AND, OR -> {
						if (right == Type.BOOLEAN) {
							binaryExpr.setType(Type.BOOLEAN);
							return type = Type.BOOLEAN;
						}
					}
					case EQ -> {
						if (left == right) {
							binaryExpr.setType(Type.BOOLEAN);
							return type = Type.BOOLEAN;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case PLUS -> {
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
				}
			}
			case INT -> {
				switch (opKind) {
					case LT, GT, LE, GE -> {
						if (right == Type.INT) {
							binaryExpr.setType(Type.BOOLEAN);
							return type = Type.BOOLEAN;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case EQ -> {
						if (left == right) {
							binaryExpr.setType(Type.BOOLEAN);
							return type = Type.BOOLEAN;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case EXP -> {
						if (right == Type.INT) {
							binaryExpr.setType(Type.INT);
							return type = Type.INT;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case PLUS -> {
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case MINUS, TIMES, DIV, MOD ->{
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
				}
			}
			case IMAGE -> {
				switch (opKind) {
					case TIMES, DIV, MOD ->{
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else if (right == Type.INT) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case MINUS->{
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case EQ -> {
						if (left == right) {
							binaryExpr.setType(Type.BOOLEAN);
							return type = Type.BOOLEAN;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case PLUS -> {
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
				}
			}
			default -> {
				switch (opKind) {
					case EQ -> {
						if (left == right) {
							binaryExpr.setType(Type.BOOLEAN);
							return type = Type.BOOLEAN;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
					case PLUS -> {
						if (left == right) {
							binaryExpr.setType(left);
							return type = left;
						}
						else {
							throw new TypeCheckException("Something in BinaryExpr was wrong!");
						}
					}
				}
			}
		}
		
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		st.enterScope();
		st.printStack();
		List<BlockElem> blockElems = block.getElems();
		for (BlockElem elem : blockElems) {
			elem.visit(this, arg);
		}
		st.leaveScope();
		st.printStack();
		return block;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitBlockStatement(StatementBlock statementBlock, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Block block = (Block) statementBlock.getBlock().visit(this, arg);
		return block;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitChannelSelector(ChannelSelector channelSelector, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
		return channelSelector;
	}

	@Override
	public Object visitConditionalExpr(ConditionalExpr conditionalExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type typeG = (Type) conditionalExpr.getGuardExpr().visit(this, arg);
		check(typeG == Type.BOOLEAN, conditionalExpr, "image width must be int");
		Type typeF = (Type) conditionalExpr.getFalseExpr().visit(this, arg);
		//check(typeF == Type.BOOLEAN, conditionalExpr, "image height must be int");
		Type typeT = (Type) conditionalExpr.getTrueExpr().visit(this, arg);
		check(typeT == typeF, conditionalExpr, "image height must be int");
		conditionalExpr.setType(typeT);
		return conditionalExpr;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitDeclaration(Declaration declaration, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		if (declaration.getInitializer() != null) {
			//System.out.print(declaration.getInitializer().getType());
			//System.out.print(declaration.getInitializer());
			declaration.getInitializer().visit(this, arg);
			if (declaration.getInitializer().getType() == declaration.getNameDef().getType() || (declaration.getInitializer().getType() == Type.STRING && declaration.getNameDef().getType() == Type.IMAGE)) {
				declaration.getNameDef().visit(this, arg);
			}
			else {
				throw new TypeCheckException("Declaration was not right");
			}
		}
		else {
			declaration.getNameDef().visit(this, arg);
		}
		
		return declaration;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitDimension(Dimension dimension, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type typeW = (Type) dimension.getWidth().visit(this, arg);
		check(typeW == Type.INT, dimension, "image width must be int");
		Type typeH = (Type) dimension.getHeight().visit(this, arg);
		check(typeH == Type.INT, dimension, "image height must be int");
		return dimension;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitDoStatement(DoStatement doStatement, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		List<GuardedBlock> list = doStatement.getGuardedBlocks();
		for (GuardedBlock block : list) {
			block.visit(this, arg);
		}
		return doStatement;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitExpandedPixelExpr(ExpandedPixelExpr expandedPixelExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		//System.out.print(expandedPixelExpr.getRed());
		Type typeR = (Type) expandedPixelExpr.getRed().visit(this, arg);
		check(typeR == Type.INT, expandedPixelExpr, "image width must be int");
		Type typeG = (Type) expandedPixelExpr.getGreen().visit(this, arg);
		System.out.print(typeG);
		check(typeG == Type.INT, expandedPixelExpr, "image height must be int");
		Type typeB = (Type) expandedPixelExpr.getBlue().visit(this, arg);
		check(typeB == Type.INT, expandedPixelExpr, "image height must be int");
		expandedPixelExpr.setType(Type.PIXEL);
		return expandedPixelExpr.getType();
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitGuardedBlock(GuardedBlock guardedBlock, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type expr = (Type) guardedBlock.getGuard().visit(this, arg);
		if (expr != Type.BOOLEAN) {
			throw new TypeCheckException("Expr was not BOOL in Guarded Block!");
		}
		Block block = (Block) guardedBlock.getBlock().visit(this, arg);
		return block;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitIdentExpr(IdentExpr identExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		System.out.print(identExpr.getName());
		if (st.lookup(identExpr.getName()) != null) {
			identExpr.setNameDef(st.lookup(identExpr.getName()));
			identExpr.setType(identExpr.getNameDef().getType());
		}
		else {
			//throw new TypeCheckException("IdentExpr is wrong!");
		}
		return identExpr.getType();
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		List<GuardedBlock> list = ifStatement.getGuardedBlocks();
		for (GuardedBlock block : list) {
			block.visit(this, arg);
		}
		return ifStatement;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitLValue(LValue lValue, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = null;
		//System.out.print(st.lookup(lValue.getName()));
		lValue.setNameDef(st.lookup(lValue.getName()));
		Object pixel = null;
		Object channel = null;
		if (lValue.getPixelSelector() != null) {
			//lValue.setType(Type.IMAGE);
			check(lValue.getVarType() == Type.IMAGE, lValue, "blah");
			pixel = lValue.getPixelSelector().visit(this, true);
		}
		if (lValue.getChannelSelector() != null) {
			//lValue.setType(Type.PIXEL, Type.IMAGE);
			if (lValue.getVarType() != Type.IMAGE && lValue.getVarType() == Type.PIXEL) {
				throw new TypeCheckException("Something wrong with channel selector!");
			}
			channel = lValue.getChannelSelector().visit(this, arg);
		}
		switch(lValue.getVarType()) {
			case IMAGE -> {
				if (pixel != null) {
					if (channel != null) {
						lValue.setType(Type.INT);
						return type = Type.INT;
					}
					else {
						lValue.setType(Type.PIXEL);
						return type = Type.PIXEL;
					}
				}
				else {
					if (channel != null ) {
						lValue.setType(Type.IMAGE);
						return type = Type.IMAGE;
					}
					else {
						lValue.setType(lValue.getVarType());
						return type = lValue.getVarType();
					}
				}
			}
			case PIXEL -> {
				if (pixel == null) {
					if (channel != null) {
						lValue.setType(Type.INT);
						return type = Type.INT;
					}
					else {
						lValue.setType(lValue.getVarType());
						return type = lValue.getVarType();
					}
				}
			}
			default -> {
				if (pixel == null && channel == null) {
					lValue.setType(lValue.getVarType());
					return type = lValue.getVarType();
				}
				else {
					throw new TypeCheckException("PostFixExpr is wrong somewhere!");
				}
			}
		}
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitNameDef(NameDef nameDef, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = null;
		if (nameDef.getDimension() != null) {
			nameDef.getDimension().visit(this, arg);
			type = Type.IMAGE;
		}
		else {
			type = nameDef.getType();
		}
		System.out.print(type);
		if (type == Type.VOID) {
			throw new TypeCheckException("blah");
		}
		nameDef.setType(type);
		st.insert(nameDef);
		return nameDef;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitNumLitExpr(NumLitExpr numLitExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = Type.INT;
		numLitExpr.setType(type);
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitPixelSelector(PixelSelector pixelSelector, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Boolean isNum = false;
		NumLitExpr exprXNum = null;
		NumLitExpr exprYNum = null;
		IdentExpr exprX = null;
		IdentExpr exprY = null;
		if (pixelSelector.xExpr() instanceof NumLitExpr) {
			exprXNum = (NumLitExpr) pixelSelector.xExpr();
			exprYNum = (NumLitExpr) pixelSelector.yExpr();
			isNum = true;
			exprXNum.visit(this, arg);
			exprYNum.visit(this, arg);
		}
		else {
			exprX = (IdentExpr) pixelSelector.xExpr();
			exprY = (IdentExpr) pixelSelector.yExpr();
			//System.out.print(exprX);
			exprX.visit(this, arg);
			exprY.visit(this, arg);
		}
		//System.out.print(arg);
		if ((Boolean) arg) {
			if ((pixelSelector.xExpr() instanceof IdentExpr || pixelSelector.xExpr() instanceof NumLitExpr) && (pixelSelector.yExpr() instanceof IdentExpr || pixelSelector.yExpr() instanceof NumLitExpr)) {
				if (pixelSelector.xExpr() instanceof IdentExpr && st.lookup(exprX.getName()) == null) {
					//st.enterScope();
					SyntheticNameDef x = new SyntheticNameDef(exprX.getName());
					x.setType(Type.INT);
					System.out.print(exprX.getName());
					st.insert(x);
					Type expr = (Type) pixelSelector.xExpr().visit(this, arg);
					//x.visit(this, arg);
					//st.leaveScope();
				}
				if (pixelSelector.yExpr() instanceof IdentExpr && st.lookup(exprY.getName()) == null) {
					//st.enterScope();
					SyntheticNameDef y = new SyntheticNameDef(exprY.getName());
					y.setType(Type.INT);
					//System.out.print(exprY.getName());
					st.insert(y);
					Type expr = (Type) pixelSelector.yExpr().visit(this, arg);
					//y.visit(this, arg);
					//st.leaveScope();
				}
			}
			else {
				throw new TypeCheckException("ExprX or Y is wrong!");
			}
		}
		if (isNum == true) {
			//System.out.print(exprXNum.getType());
			if (exprXNum.getType() != Type.INT && exprYNum.getType() != Type.INT) {
				throw new TypeCheckException("ExprX or Y is wrong!");
			}
		}
		else {
			if (exprX.getType() != Type.INT && exprY.getType() != Type.INT) {
				throw new TypeCheckException("ExprX or Y is wrong!");
			}
		}
		
		return pixelSelector;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitPostfixExpr(PostfixExpr postfixExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = null;
		Object pixel = null;
		Object channel = null;
		pixel = postfixExpr.pixel().visit(this, false);
		channel = postfixExpr.channel().visit(this, arg);
		Type expr =  (Type) postfixExpr.primary().visit(this, arg);
		switch (expr) {
			case IMAGE -> {
				if (pixel != null) {
					if (channel != null) {
						postfixExpr.setType(Type.INT);
						return type = Type.INT;
					}
					else {
						postfixExpr.setType(Type.PIXEL);
						return type = Type.PIXEL;
					}
				}
				else {
					if (channel != null ) {
						postfixExpr.setType(Type.IMAGE);
						return type = Type.IMAGE;
					}
					else {
						postfixExpr.setType(expr);
						return type = expr;
					}
				}
			}
			case PIXEL -> {
				if (pixel == null) {
					if (channel != null) {
						postfixExpr.setType(Type.INT);
						return type = Type.INT;
					}
					else {
						postfixExpr.setType(expr);
						return type = expr;
					}
				}
			}
			default -> {
				if (pixel == null && channel == null) {
					postfixExpr.setType(expr);
					return type = expr;
				}
				else {
					throw new TypeCheckException("PostFixExpr is wrong somewhere!");
				}
			}
		}
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Program root = program;
		Type type = Type.kind2type(program.getTypeToken().kind());
		program.setType(type);
		st.enterScope();
		//st.printStack();
		List<NameDef> params = program.getParams();
		for (NameDef param : params) {
			param.visit(this, arg);
		}
		program.getBlock().visit(this, type);
		st.leaveScope();
		//st.printStack();
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitReturnStatement(ReturnStatement returnStatement, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type expr = (Type) returnStatement.getE().visit(this, arg);
		if (expr != arg) {
			throw new TypeCheckException("Something in ReturnStatement was wrong!");
		}
		return returnStatement;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitStringLitExpr(StringLitExpr stringLitExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = Type.STRING;
		stringLitExpr.setType(type);
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitUnaryExpr(UnaryExpr unaryExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = null;
		Type expr =  (Type) unaryExpr.getExpr().visit(this, arg);
		Kind opKind = unaryExpr.getOp();
		switch (expr) {
			case BOOLEAN ->{
				if (opKind == Kind.BANG){
					unaryExpr.setType(Type.BOOLEAN);
					return type = Type.BOOLEAN;
				}
				else {
					throw new TypeCheckException("Something in BinaryExpr was wrong!");
				}
			}
			case INT -> {
				if (opKind == Kind.MINUS) {
					unaryExpr.setType(Type.INT);
					return type = Type.INT;
				}
				else {
					throw new TypeCheckException("Something in BinaryExpr was wrong!");
				}
			}
			case IMAGE -> {
				if (opKind == Kind.RES_width || opKind == Kind.RES_height) {
					unaryExpr.setType(Type.INT);
					return type = Type.INT;
				}
				else {
					throw new TypeCheckException("Something in BinaryExpr was wrong!");
				}
			}
		}
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitWriteStatement(WriteStatement writeStatement, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		writeStatement.getExpr().visit(this, arg);
		return writeStatement;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitBooleanLitExpr(BooleanLitExpr booleanLitExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		Type type = Type.BOOLEAN;
		booleanLitExpr.setType(type);
		return type;
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

	@Override
	public Object visitConstExpr(ConstExpr constExpr, Object arg) throws PLCCompilerException {
		// TODO Auto-generated method stub
		if (constExpr.getName().equals("Z")) {
			constExpr.setType(Type.INT);
		}
		else {
			constExpr.setType(Type.PIXEL);
		}
		return constExpr.getType();
		//throw new UnsupportedOperationException("Unimplemented Method ASTVisitor.visitAssignmentStatement invoked.");
	}

}
