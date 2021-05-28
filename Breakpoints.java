package detectors;

/*
 * 
 * @author joachimvanneste - 2460800V
 * 
 */

public class Breakpoints {
	private String className;
	private String methodName;
	private int startLine;
	private int finishLine;

	// constructor  
	public Breakpoints(String className, String methodName, int startLine, int endLine) {
		this.className = className;
		this.methodName = methodName;
		this.startLine = startLine;
		this.finishLine = endLine;
	}

	@Override
	public String toString() {
		return "className = " + className + ", methodName = " + methodName + ", startLine = " + startLine + ", endLine = " + finishLine;
	}
	
}