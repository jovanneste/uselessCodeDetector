package detectors;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/*
 * 
 * @author joachimvanneste - 2460800V
 * 
 */

public class RecursionDetector extends VoidVisitorAdapter<List<Breakpoints>> {

	private String methodName;
	private String className;
	// create list of method declarations
	private List<MethodDeclaration> mdList = new ArrayList<>();
	// list of all recursive breakpoints
	private List<Breakpoints> recursionBP;
	// boolean variable to keep track if the method is recursive or not
	private boolean recursive;

	public void addBreakpoint(Node n) {
		// add a given node to the list of breakpoints
		this.recursionBP.add(new Breakpoints(className, methodName, n.getRange().get().begin.line, n.getRange().get().end.line));
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration cid, List<Breakpoints> recursionBP) {
		// set name of class 
		this.className = cid.getName().asString();
		this.recursionBP = recursionBP;
		super.visit(cid, recursionBP);
	}

	@Override
	public void visit(MethodDeclaration md, List<Breakpoints> recursionBP) {
		// add each method declaration to the list
		mdList.add(md);
		super.visit(md, recursionBP);

		if (recursive) {
			// if the method is recursive we create a new breakpoint
			this.methodName = md.getName().asString();
			int index = mdList.size() - 1;
			addBreakpoint(mdList.get(index));
			// remove this method declaration from the list
			mdList.remove(index);
			recursive=false;
		}
	}

	@Override
	public void visit(MethodCallExpr me, List<Breakpoints> recursionBP) {
		// if the method call expr is the same as the method name the we have recursion
		if (me.getName().asString().equals(mdList.get(mdList.size() - 1).getName().asString())) {
			recursive=true;
		}

	}

}
