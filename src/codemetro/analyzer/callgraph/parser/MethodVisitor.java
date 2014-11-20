package codemetro.analyzer.callgraph.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodVisitor extends ASTVisitor {
  List<MethodDeclaration> callers = new ArrayList<MethodDeclaration>();
  List<MethodInvocation> callees = new ArrayList<MethodInvocation>();

  @Override
  public boolean visit(MethodDeclaration node) {
    callers.add(node);
    return super.visit(node);
  }

  @Override
  public boolean visit(MethodInvocation node) {
	callees.add(node);
	return super.visit(node);
  }
  
  public List<MethodDeclaration> getCallers() {
    return callers;
  }
  
  public List<MethodInvocation> getCallees(){
	  return callees;
  }
} 
