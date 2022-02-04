package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);
	void writeObject(Object obj);
	default void writeObjectLine(Object obj) {
		writeObject(obj.toString() + "\n");
	}

	default <R> R readObject(String prompt, String errorPrompt, Function<String, R> mapper) {
		
		while(true) {
			String string = readString(prompt);
			try {
				R result = mapper.apply(string);
				return result;
			} catch (Exception e) {
				writeObjectLine(errorPrompt);
			}
		}
		
	}

	default String readStringPredicate(String prompt, String errorMessage, Predicate<String> predicate) {
		
		return readObject(prompt, errorMessage, str -> {
			if (predicate.test(str)) {
				return str;
			}
			throw new IllegalArgumentException();
		});
	}
	//TODO write all default methods from UML schema 
	// #1
	default Integer readInt(String prompt) {
		try {
			return readObject(prompt, "Input has to include digits only", str -> {
				return Integer.parseInt(str);
			});
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException();
		}
	}
	// #2
	default Integer readInt(String prompt, int min, int max) {
		String errorMessage = String.format("Digits only, integer in range[%d,%d]", min, max);
		try {
			return readObject(prompt, errorMessage, str -> {
				int value = Integer.parseInt(str);
				if(value<min || value>max) {
					throw new IllegalArgumentException();
				}
				return value;
			});
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException();
		}
	}
	// #3
	default Long readLong(String prompt) {
		try {
			return readObject(prompt, "Input has to include digits only", str -> {
				return Long.parseLong(str);
			});
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException();
		}
	}
	// #4 readStringOption
	default String readStringOption(String prompt, Set<String> options) {
		return readStringPredicate(prompt, "Out of options", str -> {
			return options.contains(str);
		});
	}
	// # 5 readDate in format YYYY-MM-DD
	default LocalDate readDate(String prompt) {
		String formatter = "yyyy-MM-dd";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatter);
		return readDate(prompt + " using format " + formatter, dtf);
	}
	// # 6 readDate by DateTimeFormatter
	default LocalDate readDate(String prompt, DateTimeFormatter formatter) {
		try {
			return readObject(prompt, "incorrect data format", str -> {
				return LocalDate.parse(str, formatter);
			});
		} catch (DateTimeParseException ex) {
			throw new IllegalArgumentException();
		}
		
	}
}
