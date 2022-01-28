package telran.employees.services;

import telran.employees.dto.Employee;

public class EmployeesMethodsMapsExample extends EmployeesMethodsMapsImpl {
	public Employee getEmployee(long id) {
		return mapEmployees.get(id);
	}

}
