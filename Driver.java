package detectors;

/*
 * 
 * @author joachimvanneste - 2460800V
 * 
 */

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Driver {

	public static String FILE_PATH;

	public static void main(String a[]) {

		try {
			// used for command line interface
			Scanner scanner = new Scanner(new InputStreamReader(System.in));
			// optional argument for command line
			if (a.length > 0) {
				FILE_PATH = a[0];
			} else {
				// if no argument is used the user is asked to input one
				System.out.println("Please enter the filepath: ");
				String input = scanner.nextLine();
				if (input.equals("")) {
					System.out.println("Filepath cannot be empty");
				}
				FILE_PATH = input;
			}

			// start parser
			CompilationUnit cu = JavaParser.parse(new FileInputStream(FILE_PATH));

			// create the two visitors
			UselessControlFlowDetector uselessControlVisitor = new UselessControlFlowDetector();
			RecursionDetector recursionVisitor = new RecursionDetector();

			// create list of breakpoints
			List<Breakpoints> uselessControlBP = new ArrayList<>();
			List<Breakpoints> recursionBP = new ArrayList<>();

			// first call to start visit
			uselessControlVisitor.visit(cu, uselessControlBP);
			recursionVisitor.visit(cu, recursionBP);

			// display breakpoints found
			System.out.println("\n------- Useless Control Flows -------\n");
			for (Breakpoints b : uselessControlBP) {
				System.out.println(b);
			}

			System.out.println("\n------- Recursions -------\n");
			for (Breakpoints b : recursionBP) {
				System.out.println(b);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
