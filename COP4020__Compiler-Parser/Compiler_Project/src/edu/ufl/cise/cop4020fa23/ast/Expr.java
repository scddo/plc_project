/*Copyright 2023 by Beverly A Sanders
 *
 * This code is provided for solely for use of students in COP4020 Programming Language Concepts at the
 * University of Florida during the fall semester 2023 as part of the course project.
 *
 * No other use is authorized.
 *
 * This code may not be posted on a public web site either during or after the course.
 */
package edu.ufl.cise.cop4020fa23.ast;

import edu.ufl.cise.cop4020fa23.IToken;

public abstract class Expr extends AST {

	public Expr(IToken firstToken) {
		super(firstToken);
	}
	
	Type type;  

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
