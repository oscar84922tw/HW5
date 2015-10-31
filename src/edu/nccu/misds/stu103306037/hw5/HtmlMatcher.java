package edu.nccu.misds.stu103306037.hw5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;

public class HtmlMatcher {
	private String urlStr;
	private String content;

	public HtmlMatcher(String urlStr) {
		this.urlStr = urlStr;
	}

	/* from HW3 */
	private String fetchContent() throws IOException {
		URL url = new URL(this.urlStr);
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));

		String retVal = "";
		String line = null;
		while ((line = br.readLine()) != null) {

			retVal += (line + "\n");

		}
		return retVal;

	}

	public void match() throws IOException {

		if (content == null) {
			content = fetchContent();
		}

		/* Create a stack to store the tag */
		Stack<String> tagStack = new Stack<String>();
		int indexOfOpen = 0;

		while ((indexOfOpen = content.indexOf("<", indexOfOpen)) != -1)
		/* we can find next '<' character in content */
		{
			// 1. Get full tag. e.g. "<div id="abcdefg">","</a>","</div>"...
			int indexOfClosed = content.indexOf(">", indexOfOpen);
			String fullTag = content.substring(indexOfOpen, indexOfClosed + 1);

			// 2. Extract tag name from fullTag. e.g. "div","/a","/div"...
			String tagName = null;
			int indexOfSpace = -1;
			if ((indexOfSpace = fullTag.indexOf(" ")) == -1/*
															 * there is no space
															 * in fullTag
															 */) {
				// If there is no space in the fullTag (e.g.
				// "<li>","</a>","</div>")
				// then the tag name will be the words between first and last
				// character.
				// For example, if fullTag is "<li>" then the tagName will be
				// "li";
				// For example, if fullTag is "</li>" then the tagName will be
				// "/li" (Note that we preserve the slash'/' so we can tell that
				// this is a close tag in the future)
				tagName = fullTag.substring(1, fullTag.length() - 1);

			} else {
				// If there are some space in the fullTag (e.g
				// "<li id='theID'>","<a href='http://www.google.com.tw/'>")
				// then the tag name will be the words between first character
				// and the first space.
				// For example, if fullTag is "<li id='theID'>" the tagName will
				// be "li";
				// For example, if fullTag is
				// "<a href='http://www.google.com.tw/'>" the tagName will be
				// "a"
				tagName = fullTag.substring(1, fullTag.length() - 1);
			}

			// 3. Determine whether this tag is an open tag (e.g. "<div>") or
			// close tag (e.g. "</div>")
			int indexOfSlash = -1;

			if ((indexOfSlash = tagName.indexOf("/")) == -1/* it is an open tag */) {
				// This is an open tag, so simply push it into stack
				tagStack.push(tagName);
			} else {
				// "/div"
				tagName = tagName.substring(indexOfSlash + 1);
				// This is an close tag, so we should compare it to the topmost
				// tag in the stack

				// Remove the slash '/' (the first character of tagName), so
				// that we can compare it with the open tag name in stack

				// But...what if there is no topmost tag in the stack
				if (tagStack.isEmpty()/* stack is empty */) {
					// stack is empty, this tag is an invalid tag
					System.out.println("False");
					return;
				}

				// Compare to topmost tag in the stack
				String topMosttag = tagStack.peek();
				if (topMosttag.equals(tagName)/* topmost tag is equals to tagName */) {
					// This tagName is equal to the tag name in the stack!
					// Pop out the top tag in the stack.
					tagStack.pop();

				} else {
					// This tagName is not equal to the tag name in the stack!
					// So we found that this tag is an invalid tag
					System.out.println("False" + getStackString(tagStack));
					return;
				}
			}

			// Move the searching start point, so that we can search the next
			// tag in htmlContent
			// ...
		}

		// After search and compare all the tag in the htmlContent,
		// We should also check whether the stack is empty or not.
		if (!tagStack.isEmpty()/* stack is not empty */) {
			// The stack is not empty, this mean the tags is invalid
			System.out.println("False" + getStackString(tagStack));
		} else {
			// The stack is empty, all tag successfully matched.
			System.out.println("True");
		}

	}

	private String getStackString(Stack<String> stack) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stack.size(); i++) {
			if (i > 0) {
				sb.append(stack.get(i));
			}
			sb.append(stack.get(i));
		}
		return sb.toString();
	}

}
