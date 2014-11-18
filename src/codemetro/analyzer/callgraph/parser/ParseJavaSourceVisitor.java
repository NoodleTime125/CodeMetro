package codemetro.analyzer.callgraph.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ParseJavaSourceVisitor implements FileVisitor<Path> {

	ASTParser parser = ASTParser.newParser(AST.JLS8);
	ArrayList<String> srcFiles = new ArrayList<String>();
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException attr)
			throws IOException {
		System.out.println("After visiting " + dir.getFileName());
		// Ignore any errors and continue.
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr)
			throws IOException {
		System.out.println("Before visiting " + dir.getFileName());

		// Ignore any errors and continue.
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
			throws IOException {
		System.out.println("Visiting file " + file.getFileName());

		// Parse if java source.
		if (file.toString().endsWith(".java")){
			System.out.println("Is java. Parsing file.");
			byte[] arr = Files.readAllBytes(file);
			parser.setUnitName(file.toString());
			createAST(new String(arr).toCharArray());
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException attr)
			throws IOException {
		// Ignore the error and continue.
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Parses a java file for the source code to create an AST.
	 * @param srcfile
	 * @throws JavaModelException
	 */
	public void createAST(char[] javasrc){

		System.out.println("Creating AST.");

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(javasrc);
		parser.setEnvironment(null, (String[]) srcFiles.toArray(), null, true);
		parser.setResolveBindings(true);
		System.out.println("Calling parser.createAST");
		CompilationUnit parse = (CompilationUnit) parser.createAST(null);
		MethodVisitor visitor = new MethodVisitor();
	    parse.accept(visitor);

	    // This is the caller method.
	    for (MethodDeclaration caller : visitor.getCallers()) {
	    	System.out.println("Method name: " + caller.getName()
	    			+ " Return type: " + caller.getReturnType2());
	    	IMethodBinding callerName = caller.resolveBinding();
	    	String fullName = callerName.getDeclaringClass().getQualifiedName();
	    	System.out.println("Testing declaring class: " + fullName);
	    	MethodVisitor callerVisit = new MethodVisitor();
	    	caller.accept(callerVisit);
	    	for(MethodInvocation callee : callerVisit.getCallees()){
	    		ITypeBinding type = callee.getExpression().resolveTypeBinding();
	    		IMethodBinding  method = callee.resolveMethodBinding();
	    		System.out.println("Called " + method);
	    	}
	      }
	}

}
