package telran.view;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;

class InputOutputTest {
InputOutput io = new ConsoleInputOutput();
	@BeforeEach
	void setUp() throws Exception {
	}

//	@Test
	void employeeInputAsOneString() {
		LocalDate d = LocalDate.now();
		System.out.println(d.toString());
		Employee empl = io.readObject("Enter employee data as string <id>#<name>#<birthdate ISO>#<salary>#<department>",
				"Wrong format of employee data", InputOutputTest::toEmployeeFromStr);
		io.writeObjectLine(empl);
	}
	static Employee toEmployeeFromStr(String str) {
		String emplTokens[] = str.split("#");
		long id = Long.parseLong ( emplTokens[0] );
		String name = emplTokens[1];
		LocalDate birthDate = LocalDate.parse(emplTokens[2]);
		int salary = Integer.parseInt(emplTokens[3]);
		String department = emplTokens[4];
		return new Employee(id, name, birthDate, salary, department);
	}
	
//	@Test 
	void readPredicateTest() {
		String str = io.readStringPredicate("enter any string containing exactly 3 symbols",
				"this is no string containing exactly 3 symbols", s -> s.length() == 3);
		assertEquals(3, str.length());
	} 
	@Test
	void employeeBySeparateFields() {
		//TODO
		//enter ID by readLong method
		long id = io.readLong("Enter employee ID");
		io.writeObjectLine("employee's id="+id);
		
		//enter Name by readStringPredicate (only letters with capital first letter)
		String name = io.readStringPredicate("enter name, only the letters with capital first letter",
				"input format is incorrect", s -> s.matches("[A-Z]{1}[a-z]+")); // "[A-Z][a-z]*"
		io.writeObjectLine("employee's name="+name);
		
		//enter birth date by readDate using format "yyy-MM-dd"
		LocalDate birthDay = io.readDate("Enter employee's birthday ");
		io.writeObjectLine("Employee's birthday is " + birthDay);
		
		//enter birth date by readDate using format "yyyy MM dd"
		String formatter = "yyyy MM dd";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatter);
		birthDay = io.readDate("Enter employee's birthday using format " + formatter, dtf);
		io.writeObjectLine("Employee's birthday is " + birthDay);
		
		//enter salary by readInt(prompt, min, max)
		int salary = io.readInt("Enter employee's salary", 10000, 20000);
		io.writeObjectLine("employee's salary="+salary);
		
		//enter department by readStringOption specifying possible departments
		String listOfDepartments[] = {"Engeneering", "QA", "Development"};
		Set<String> departments = new HashSet<>(Arrays.asList(listOfDepartments));
		String department = io.readStringOption(
				"Enter one of the following departments: "+Arrays.toString(listOfDepartments), 
				departments);
		io.writeObjectLine("department="+department);
	}

}
