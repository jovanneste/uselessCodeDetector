package detectors;

/*
 * 
 * @author joachimvanneste - 2460800V
 * 
 */

import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class UselessControlFlowDetector extends VoidVisitorAdapter<List<Breakpoints>> {

	private String className;
	private String methodName;
	// list of all useless control flow breakpoints 
	private List<Breakpoints> uselessControlBP;

	// helper method
	public void addBreakpoint(Node n) {
		// add a given node to the list of breakpoints
		this.uselessControlBP.add(new Breakpoints(className, methodName, n.getRange().get().begin.line, n.getRange().get().end.line));
	}

	// set a class name
	@Override
	public void visit(ClassOrInterfaceDeclaration cid, List<Breakpoints> uselessControlBP) {
		this.uselessControlBP = uselessControlBP;
		this.className = cid.getName().asString();
		super.visit(cid, uselessControlBP);

	}

	// set a method name
	@Override
	public void visit(MethodDeclaration md, List<Breakpoints> uselessControlBP) {
		this.methodName = md.getName().asString();
		super.visit(md, uselessControlBP);
	}

	// check all block statements i.e. if, for, while, do ... while
	@Override
	public void visit(BlockStmt bs, List<Breakpoints> uselessControlBP) {
		if (bs.isEmpty()) {
			// if the statement is empty, create a new breakpoint
			addBreakpoint(bs);
		}
		super.visit(bs, uselessControlBP);
	}

	// check switch statements
	@Override
	public void visit(SwitchStmt ss, List<Breakpoints> uselessControlBP) {
		
		if (ss.getEntries().isEmpty()) {
			addBreakpoint(ss);
		}
		super.visit(ss, uselessControlBP);
	}

}
