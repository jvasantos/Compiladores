import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LexAnalyzer {

	static int lines = 0;
	static List<String> literals;
	static List<String> keywords;

	public enum Type {

		OPEN_PAR("("), CLOSE_PAR(")"), OPEN_BRA("{"), CLOSE_BRA("}"), OPEN_COM("/*"),
		OPEN_BAR("["), CLOSE_BAR("]"), CLOSE_COM("*/"), TER(";"), SEP(","),
		OP_MULT("*"), OP_DIV("/"), OP_ADD("+"), OP_SUB("-"), OP_ATT("="),
		LOG_OR("||"), LOG_AND("&&"), LOG_NOT("!="), EQ("=="), NE("<>"), GT(">"),
		LT("<"), GE(">="), LE("<="), QUOT("\""), KEYWORD(""), LITERAL(""), PN("");

		private final String content;

		Type(String content) {
			this.content = content;
		}
	}

	public static void initialize() {
		literals = new ArrayList<>();
		literals.add("int");
		literals.add("real");
		literals.add("string");
		literals.add("bool");

		keywords = new ArrayList<>();
		keywords.add("string");
		keywords.add("while");
		keywords.add("do");
		keywords.add("for");
		keywords.add("var");
		keywords.add("void");
		keywords.add("if");
		keywords.add("else");
		keywords.add("const");
		keywords.add("return");
		keywords.add("false");
		keywords.add("true");
		keywords.add("main");
		keywords.add("input");
		keywords.add("print");
	}

	public static class Token {
		public final Type t;
		public final String c;
		public final int column;
		public final int line;

		public Token(Type t, String c, int column, int line) {
			this.t = t;
			this.c = c;
			this.column = column;
			this.line = line;
		}

		public String toString(Token t) {
			String s;
			s = t.t + " " + t.c + " " + "<" + t.column + "," + t.line + ">";

			return s;
		}
	}

	public static Token getAtom(String s, int i) {
		int j = i;
		Token t;
		for (; j < s.length();) {
			if (Character.isLetterOrDigit(s.charAt(j))) {
				j++;
			} else {
				for (String string : literals) {
					if (s.substring(i, j).equals(string)) {
						t = new Token(Type.LITERAL, s.substring(i, j), i + 1, lines + 1);
						return t;
					}
				}
				for (String string : keywords) {
					if (s.substring(i, j).equals(string)) {
						t = new Token(Type.KEYWORD, s.substring(i, j), i + 1, lines + 1);
						return t;
					}
				}
				t = new Token(Type.PN, s.substring(i, j), i + 1, lines + 1);
				return t;
			}
		}
		t = new Token(Type.KEYWORD, s.substring(i, j), i + 1, lines + 1);
		return t;
	}

	public static List<Token> analyzer(String input) {
		List<Token> result = new ArrayList<Token>();

		for (int i = 0; i < input.length();) {
			switch (input.charAt(i)) {
			case '(':
				result.add(new Token(Type.OPEN_PAR, "(", i + 1, lines + 1));
				i++;
				break;
			case ')':
				result.add(new Token(Type.CLOSE_PAR, ")", i + 1, lines + 1));
				i++;
				break;
			case '{':
				result.add(new Token(Type.OPEN_BRA, "{", i + 1, lines + 1));
				i++;
				break;
			case '}':
				result.add(new Token(Type.CLOSE_BRA, "}", i + 1, lines + 1));
				i++;
				break;
			case '[':
				result.add(new Token(Type.OPEN_BAR, "[", i + 1, lines + 1));
				i++;
				break;
			case ']':
				result.add(new Token(Type.CLOSE_BAR, "]", i + 1, lines + 1));
				i++;
				break;
			case ';':
				result.add(new Token(Type.TER, ";", i + 1, lines + 1));
				i++;
				break;
			case ',':
				result.add(new Token(Type.SEP, ",", i + 1, lines + 1));
				i++;
				break;
			case '*':
				result.add(new Token(Type.OP_MULT, "*", i + 1, lines + 1));
				i++;
				break;
			case '/':
				result.add(new Token(Type.OP_DIV, "/", i + 1, lines + 1));
				i++;
				break;
			case '+':
				result.add(new Token(Type.OP_ADD, "+", i + 1, lines + 1));
				i++;
				break;
			case '-':
				result.add(new Token(Type.OP_SUB, "-", i + 1, lines + 1));
				i++;
				break;
			case '\"':
				result.add(new Token(Type.QUOT, "\"", i + 1, lines + 1));
				i++;
				while (input.charAt(i) != '\"')
					i++;
				result.add(new Token(Type.QUOT, "\"", i + 1, lines + 1));
				i++;
				break;
			case '=':
				if (input.charAt(i + 1) == '=') {
					result.add(new Token(Type.EQ, "==", i + 1, lines + 1));
					i = i + 2;
					break;
				} else
					result.add(new Token(Type.OP_ATT, "=", i + 1, lines + 1));
				i++;
				break;
			case '>':
				if (input.charAt(i + 1) == '=') {
					result.add(new Token(Type.GE, ">=", i + 1, lines + 1));
					i = i + 2;
					break;
				} else
					result.add(new Token(Type.GT, ">", i + 1, lines + 1));
				i++;
				break;
			case '<':
				if (input.charAt(i + 1) == '=') {
					result.add(new Token(Type.LE, "<=", i + 1, lines + 1));
					i = i + 2;
					break;
				}
				if (input.charAt(i + 1) == '>') {
					result.add(new Token(Type.NE, "<>", i + 1, lines + 1));
					i = i + 2;
					break;
				} else
					result.add(new Token(Type.LT, "<", i + 1, lines + 1));
				i++;
				break;
			case '&':
				result.add(new Token(Type.LOG_AND, "&&", i + 1, lines + 1));
				i = i + 2;
				break;
			case '|':
				result.add(new Token(Type.LOG_OR, "||", i + 1, lines + 1));
				i = i + 2;
				break;
			case '!':
				result.add(new Token(Type.LOG_NOT, "!=", i + 1, lines + 1));
				i = i + 2;
				break;
			default:
				if (Character.isWhitespace(input.charAt(i))) {
					i++;
				} else {
					Token atom = getAtom(input, i);
					result.add(atom);
					i += atom.c.length();
				}
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) {

		initialize();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("shellsort.txt"));

			String input = reader.readLine();
			while (input != null) {
				List<Token> tokens = analyzer(input);
				for (Token t : tokens) {
					if (t.t != Type.PN)
						System.out.println(t.toString(t));
				}
				lines++;
				input = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}

	}

}
