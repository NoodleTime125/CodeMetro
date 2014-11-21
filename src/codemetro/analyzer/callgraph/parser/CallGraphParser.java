package codemetro.analyzer.callgraph.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import codemetro.analyzer.callgraph.*;

/**
 * Parses a java source repository to create a call graph.
 * 
 * Uses the JDT provided by Eclipse.
 * 
 * @author kenshen
 *
 */
public class CallGraphParser {

	ASTParser parser = ASTParser.newParser(AST.JLS8);
	Collection<File> srcFiles;
	ArrayList<String> fileStr = new ArrayList<String>();
	Path projectRoot;
	String[] srcPaths;
	ArrayList<String> classPaths = new ArrayList<String>();

	Map <String, CallGraphNode> nodeList = new HashMap<String, CallGraphNode>();

	/**
	 * Generate a call graph from source files rooted at the target path.
	 * @param target
	 * @return
	 * @throws IOException 
	 */
	public Map<String, CallGraphNode> generateGraph(String target) throws IOException{
		projectRoot = new File(target).toPath().toAbsolutePath().normalize();
		// Figure out the project root.
		Path path = projectRoot.resolve("src");
		System.out.println("Project root is " + projectRoot);
		srcPaths = new String[]{path.toString()};
		System.out.println("Project src path is " + srcPaths[0]);

		if (projectRoot.resolve("lib").toFile().exists()){
			Collection<File> classJars = FileUtils.listFiles(projectRoot.resolve("lib").toFile(), new String[]{"jar"}, true);

			for (File file : classJars){
				classPaths.add(file.toPath().toAbsolutePath().normalize().toString());
			}
		}

		parseSource(path);
		return nodeList;
	}

	/**
	 * Steps through the java source files starting from the root directory and creates an AST.
	 * @throws IOException 
	 */
	public void parseSource(Path srcRoot) throws IOException{
		System.out.println("Walking the tree.");
		srcFiles = FileUtils.listFiles(srcRoot.toFile(), new String[]{"java"}, true);
		// Prepare the environment.
		for (File file : srcFiles){
			fileStr.add(FilenameUtils.normalize(file.getAbsolutePath()));
		}

		//FileVisitor<Path> visitor = new ParseJavaSourceVisitor();
		//Files.walkFileTree(srcRoot, visitor);

		for(File file : srcFiles){
			System.out.println("Parsing file " + file.toPath().toAbsolutePath().normalize());
			Path projectParent = projectRoot.getParent();

			Path unitName = projectParent.relativize(file.toPath().toAbsolutePath().normalize());
			System.out.println("Setting unit name to /" + unitName.toString());

			// Unit name must be in format specified in ASTParser.setUnitName
			parser.setUnitName("/" + unitName.toString());
			byte[] arr = Files.readAllBytes(file.toPath());
			createAST(new String(arr).toCharArray());
		}
	}

	/**
	 * Parses a java file for the source code to create an AST.
	 * @param srcfile
	 * @throws JavaModelException
	 */
	public void createAST(char[] javasrc){

		System.out.println("Creating AST.");

		System.out.println(Arrays.toString(srcPaths));
		
		/*
		 * The environment must be set up according to 
		 * http://help.eclipse.org/luna/index.jsp?topic=/org.eclipse.jdt.doc.isv/reference/api/org/eclipse/jdt/core/dom/ASTParser.html
		 */
		
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(javasrc);
		parser.setEnvironment(classPaths.toArray(new String[classPaths.size()]), srcPaths, null, true);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		System.out.println("Calling parser.createAST");
		CompilationUnit parse = (CompilationUnit) parser.createAST(null);
		MethodVisitor visitor = new MethodVisitor();
		parse.accept(visitor);

		// This is the caller method.
		for (MethodDeclaration caller : visitor.getCallers()) {
			IMethodBinding callerName = caller.resolveBinding();
			String callerClass = callerName.getDeclaringClass().getQualifiedName();
			if (callerClass.equals("")){
				continue;
			}
			//System.err.println("Testing declaring class: " + callerClass);	

			//System.out.println("Method name: " + caller.getName()
			//		+ " Return type: " + caller.getReturnType2());

			//System.out.println("Getting callees.");
			MethodVisitor callerVisit = new MethodVisitor();
			caller.accept(callerVisit);

			for(MethodInvocation callee : callerVisit.getCallees()){
				//System.out.println("Testing on callee " + callee.getName() + " on line "+
				//		parse.getLineNumber(callee.getStartPosition() - 1));
				//System.out.println("Callee expression: " + callee.getExpression());

				// These are null when it's a library. 
				// We don't want to enter library calls.
				if (callee.getExpression() != null && callee.resolveMethodBinding() != null ) {
					ITypeBinding type = callee.getExpression().resolveTypeBinding();
					IMethodBinding  method = callee.resolveMethodBinding();
					
					// Type erasure on the type so we don't get duplicate entries for the same generic class.
					String typeName = type.getQualifiedName();
					if (type.isParameterizedType()){
						typeName = type.getErasure().getQualifiedName();
					}
					
					//System.out.println("Called " + method.getName() + " in class " + type.getQualifiedName());

					addCall(callerClass, caller.getName().toString(), typeName, method.getName());
				}
				//System.out.println("Callees done.");
			}
		}

	}

	/**
	 * Adds a call to the call graph and reuses nodes if needed.
	 * @param callerClass
	 * @param caller
	 * @param calleeClass
	 * @param callee
	 */
	public void addCall(String callerClass, String caller, String calleeClass, String callee){

		CallGraphNode callerNode;
		CallGraphNode calleeNode;

		if (nodeList.containsKey(callerClass)){
			callerNode = nodeList.get(callerClass);
		} else {
			callerNode = new CallGraphNode(callerClass);
			nodeList.put(callerClass, callerNode);
		}

		if (nodeList.containsKey(calleeClass)){
			calleeNode = nodeList.get(calleeClass);
		} else {
			calleeNode = new CallGraphNode(calleeClass);
			nodeList.put(calleeClass, calleeNode);
		}

		CallGraphEdge edge = new CallGraphEdge(caller, callee);
		edge.addEdgeBetween(callerNode, calleeNode);

		//System.out.println(caller + " in " + callerClass + " calls method " + callee + " in " + calleeClass);

	}
}
