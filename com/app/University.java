package app;
import java.io.Console;
import java.util.Scanner;


public interface University
{
	Scanner scanner =new Scanner(System.in).useDelimiter(System.lineSeparator());
	String display();

	static char[] getPasswordFromInput() {
		Console console = System.console();
		if (console == null) {
			return null;
		}
		return console.readPassword("\nEnter your password: (Password is not visible)");
	}

	static int getIntegerFromInput() {
		int i = 0;
		while (true) {
			try {
				int integer = Integer.parseInt(scanner.next());
				if (integer < 0) {
					throw new Exception();
				}
				return integer;
			} catch (Exception e) {
				if(i >5){
					return -1;
				}
				UniversityApp.getError(1);
				i++;
			}
		}
	}

	static double getDoubleFromInput() {
		int i = 0;
		while (true) {
			try {
				double integer = Double.parseDouble(scanner.next());
				if (integer < 0) {
					throw new Exception();
				}
				return integer;
			} catch (Exception e) {
				if(i > 5){
					return -1;
				}
				i++;
				UniversityApp.getError(1);
			}
		}
	}

	static long getLongFromInput() {
		int i = 0;
		while (true) {
			try {
				long integer = Long.parseLong(scanner.next());
				if (integer < 0) {
					throw new Exception();
				}
				return integer;
			} catch (Exception e) {
				if (i > 5) {
					return -1;
				}
				i++;
				UniversityApp.getError(1);
			}
		}
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