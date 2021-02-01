/**
 * Copyright 2020 Sebastian Proksch
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

public class CommentParser {

	private final CommentPrintingVisitor visitor = new CommentPrintingVisitor();

	public void run() {

		Map<String, CompilationUnit> cus = parseInAndBelowPackage("jpm.a");

		for (String type : cus.keySet()) {
			CompilationUnit cu = cus.get(type);
			cu.accept(visitor, null);
		}

	}

	private Map<String, CompilationUnit> parseInAndBelowPackage(String basePkg) {
		try {
			SourceRoot sourceRoot = findSourceRoot();
			List<ParseResult<CompilationUnit>> parsedCus = sourceRoot.tryToParse(basePkg);

			return parsedCus.stream()//
					.map(pr -> pr.isSuccessful() ? pr.getResult().get() : null)//
					.filter(p -> p != null) //
					.collect(Collectors.toMap(cu -> cu.getPrimaryTypeName().get(), cu -> cu));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private SourceRoot findSourceRoot() {
		Path root = CodeGenerationUtils.mavenModuleRoot(CommentParser.class);
		Path src = root.resolve("src/main/java");
		SourceRoot sourceRoot = new SourceRoot(src);
		return sourceRoot;
	}
}