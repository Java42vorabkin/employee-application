package telran.employees.services;

import telran.employees.dto.Employee;
import telran.employees.dto.ReturnCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
public class EmployeesMethodsMapsImpl implements EmployeesMethods {
 protected HashMap<Long, Employee> mapEmployees = new HashMap<>(); //key employee's id, value - employee
 private TreeMap<Integer, List<Employee>> employeesAge= new TreeMap<>(); //key - age, value - list of employees with the same age
 private TreeMap<Integer, List<Employee>> employeesSalary = new TreeMap<>(); //key - salary,
 //value - list of employees with the same salary
 private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
	@Override
	public ReturnCode addEmployee(Employee empl) {
		if (mapEmployees.containsKey(empl.id)) {
			return ReturnCode.EMPLOYEE_ALREADY_EXISTS;
		}
		Employee emplS = new Employee(empl.id, empl.name, empl.birthDate, empl.salary, empl.department);
		mapEmployees.put(emplS.id, emplS);
		employeesAge.computeIfAbsent(getAge(emplS), k -> new LinkedList<Employee>()).add(emplS);
		employeesSalary.computeIfAbsent(emplS.salary, k -> new LinkedList<Employee>()).add(emplS);
		employeesDepartment.computeIfAbsent(emplS.department, k -> new LinkedList<Employee>()).add(emplS);
		
		return ReturnCode.OK;
	}

	private Integer getAge(Employee emplS) {
		
		return (int)ChronoUnit.YEARS.between(emplS.birthDate, LocalDate.now());
	}

	@Override
	public ReturnCode removeEmployee(long id) {
		// TODO Done
		Employee emp = mapEmployees.get(id);
		if(emp==null) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		removeEmployeesSalary(emp);
		removeEmployeesDepartment(emp);
		removeEmployeesAge(emp);
		mapEmployees.remove(id);
		return ReturnCode.OK;
	}
	
	private void removeEmployeesDepartment(Employee employee) {
		List<Employee> emps = employeesDepartment.get(employee.department);
		emps.remove(employee);
		if(emps.isEmpty()) {
			employeesDepartment.remove(employee.department);
		}
	}
	
	private void removeEmployeesSalary(Employee employee) {
		List<Employee> emps = employeesSalary.get(employee.salary);
		emps.remove(employee);
		if(emps.isEmpty()) {
			employeesSalary.remove(employee.salary);
		}
	}
	
	private void removeEmployeesAge(Employee employee) {
		Integer age = getAge(employee);
		List<Employee> emps = employeesAge.get(age);
		emps.remove(employee);
		if(emps.isEmpty()) {
			employeesAge.remove(age);
		}
	}
	
	@Override
	public Iterable<Employee> getAllEmployees() {
		// TODO Done
		return getEmployeesFromList(mapEmployees.values().stream().toList());
	}

	@Override
	public Employee getEmployee(long id) {
		// TODO Done
		Employee emp = mapEmployees.get(id);
		return emp==null ? null : new Employee(emp.id, emp.name, emp.birthDate, emp.salary, emp.department);
	}

	@Override
	public Iterable<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		// TODO Done
		return joinEmployeeLists(employeesAge.subMap(ageFrom, true, ageTo, true));
	}

	@Override
	public Iterable<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		// TODO Done
		return joinEmployeeLists(employeesSalary.subMap(salaryFrom, true, salaryTo, true));
	}
	private Iterable<Employee> joinEmployeeLists(NavigableMap<Integer, List<Employee>> empSubMap) {
		return getEmployeesFromList(empSubMap.values().stream().flatMap(l -> l.stream()).toList());
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartment(String department) {
		List<Employee> employees = employeesDepartment.getOrDefault(department, Collections.emptyList());
		
		return employees.isEmpty() ? employees : getEmployeesFromList(employees);
	}

	private Iterable<Employee> getEmployeesFromList(List<Employee> employees) {
		
		return employees.stream().map(e ->
		new Employee(e.id, e.name, e.birthDate, e.salary, e.department)).toList();
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartmentAndSalary(String department, int salaryFrom, int salaryTo) {
		// TODO Done
		HashSet<Employee> empSalarySet = new HashSet<>();
		for (Employee empl: getEmployeesBySalary(salaryFrom, salaryTo)) {
			empSalarySet.add(empl);
		}
		List<Employee> result = new ArrayList<>();
		for (Employee empl: getEmployeesByDepartment(department)) {
			if (empSalarySet.contains(empl)) {
				result.add(empl);
			}
		}
		return getEmployeesFromList(result);
		/*
		return getEmployeesFromList(
						employeesDepartment.get(department).stream()
						.filter(e -> e.salary >= salaryFrom && e.salary <= salaryTo).toList()
						);
		*/
	}

	@Override
	public ReturnCode updateSalary(long id, int newSalary) {
		// TODO Done
		Employee emp = mapEmployees.get(id);
		if(emp == null) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		if(emp.salary == newSalary) {
			return ReturnCode.SALARY_NOT_UPDATED;
		}
		removeEmployeesSalary(emp);
		emp.salary = newSalary;
		employeesSalary.computeIfAbsent(emp.salary, k -> new LinkedList<Employee>()).add(emp);
		return ReturnCode.OK;
	}

	@Override
	public ReturnCode updateDepartment(long id, String newDepartment) {
		// TODO Done
		Employee emp = mapEmployees.get(id);
		if(emp == null) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		if(emp.department.equalsIgnoreCase(newDepartment)) {
			return ReturnCode.DEPARTMENT_NOT_UPDATED;
		}
		removeEmployeesDepartment(emp);
		emp.department = newDepartment;
		employeesDepartment.computeIfAbsent(emp.department, k -> new LinkedList<Employee>()).add(emp);
		return ReturnCode.OK;
	}

}
