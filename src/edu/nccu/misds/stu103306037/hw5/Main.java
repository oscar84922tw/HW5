	package edu.nccu.misds.stu103306037.hw5;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		String url = scan.next();
		HtmlMatcher test1 = new HtmlMatcher(url);
		test1.match();
		scan.close();
	}
}
