package app;
import java.io.Console;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public interface University
{
	Scanner scanner = new Scanner(System.in);//.useDelimiter(System.lineSeparator());
	Console console = System.console();
	String display();
	Pattern SPECIAL_CHARACTERS_REGEX = Pattern.compile("[^a-zA-Z0-9 ]");

	static char[] getPasswordFromInput() {
		Console console = System.console();
		if (console == null) {
			return null;
		}
		return console.readPassword("\nEnter your password: (Password is not visible)");
	}

		static String getStringFromInput(boolean allowSpecials) {
		for(int i = 0;i < 5;i++) {
			try {
				String input;
				do {
					input = scanner.nextLine();
				} while (input.isEmpty() || input.isBlank());
				if(!allowSpecials) {
					Matcher matcher = SPECIAL_CHARACTERS_REGEX.matcher(input);
					if (matcher.find()) {
						throw new Exception();
					}
				}
//				System.out.println("got input : " + input);
//				UniversityApp.holdNextSlide();
				return input;
			} catch (Exception e) {
				UniversityApp.getError(7);
			}
		}
		return "";
	}
	static int getIntegerFromInput() {
		for(int i = 0;i < 5;i++) {
			try {
				int integer = Integer.parseInt(scanner.nextLine());
				if (integer < 0) {
					throw new Exception();
				}

//				scanner.nextLine();
				return integer;
			} catch (Exception e) {
				UniversityApp.getError(1);
			}
		}
		return 0;
	}

	static double getDoubleFromInput() {
		for(int i=0;i < 5; i++) {
			try {
				double integer = Double.parseDouble(scanner.nextLine());
				if (integer < 0) {
					throw new Exception();
				}
				return integer;
			} catch (Exception e) {
				UniversityApp.getError(1);
			}
		}
		return 0;
	}

	static long getLongFromInput() {
		for (int i = 0; i < 5; i++) {
			try {
				long integer = Long.parseLong(scanner.nextLine());
				if (integer < 0) {
					throw new Exception();
				}
				return integer;
			} catch (Exception e) {
				UniversityApp.getError(1);
			}
		}
		return 0;
	}

	static int getKeyPress() {
		return getIntegerFromInput();
/*		while (true) {
  			try {
  				int integer = Integer.parseInt(AppKeyListener.getKeyPressed());
  				if (integer < 0) {
  					throw new Exception();
  				}
  				return integer;
  			} catch (Exception e) {
  				UniversityApp.getError(1);
  			}
  		}
*/
	}

	void printHeader(String out);
}