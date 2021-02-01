/**
 * Copyright 2021 Sebastian Proksch
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package jpm;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class CommentPrintingVisitor extends VoidVisitorWithDefaults<Object> {

	private String currentClassName = null;

	@Override
	public void defaultAction(Node n, Object arg) {
		for (Node c : n.getChildNodes()) {

			if (c.getComment().isPresent()) {
				print(c.getComment().get().getContent());
			}

			c.accept(this, arg);
		}
	}

	private void print(String c) {
		System.out.printf("%s\n", c.trim());
	}

	@Override
	public void defaultAction(NodeList n, Object arg) {
		// ??
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		String oldClassName = currentClassName;

		currentClassName = n.getNameAsString();
		
		defaultAction(n, arg);

		currentClassName = oldClassName;
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		System.out.printf("-- %s.%s --\n", currentClassName, n.getName());
		defaultAction(n, arg);
	}

	@Override
	public void visit(LineComment n, Object arg) {
		print(n.getContent());
	}

	@Override
	public void visit(LambdaExpr n, Object arg) {
		// do not traverse?!
	}

}