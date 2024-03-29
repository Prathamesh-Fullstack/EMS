package com.maze.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maze.helper.ExcelHelper;
import com.maze.model.Employee;
import com.maze.response.ResponseMessage;
import com.maze.service.EmployeeService;
import com.maze.serviceImpl.ExcelService;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

	private final EmployeeService employeeService;
	
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService=employeeService;
	}

	@Autowired
	ExcelService fileService;

	@PostMapping("/addEmployee")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
		Employee addEmployee = employeeService.addEmployee(employee);
		return new ResponseEntity<>(addEmployee, HttpStatus.CREATED);
	}

	@DeleteMapping("/deleteEmployee/{employeeId}")
	public String deleteEmployee(@PathVariable int employeeId) {
		employeeService.delete(employeeId);
		return "Employee Deleted Successfully with employeeId " + employeeId;
	}

	@GetMapping("/getEmployeeById/{employeeId}")
	public ResponseEntity<Employee> getEmployee(@PathVariable int employeeId) {
		Employee getEmployeeById = employeeService.getEmployee(employeeId);
		return new ResponseEntity<>(getEmployeeById, HttpStatus.OK);
	}

	@GetMapping("/getAllEmployee")
	public ResponseEntity<List<Employee>> getAllEmployee() {
		List<Employee> allEmployee = employeeService.getAllEmployee();
		return new ResponseEntity<>(allEmployee, HttpStatus.OK);
	}

	@PutMapping("/updateEmployee/{employeeId}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable int employeeId, @RequestBody Employee employee) {
		Employee updateEmployee = employeeService.updateEmployee(employeeId, employee);
		return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
	}

	@PostMapping("/uploadExcel")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (ExcelHelper.hasExcelFormat(file)) {
			try {
				fileService.save(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	@GetMapping("/empOnPool")
	public ResponseEntity<List<Employee>> employeeOnPool(String status){
		
		List<Employee> employeeOnPool = employeeService.employyOnPool(status);
		return new ResponseEntity<>(employeeOnPool,HttpStatus.OK);
	}
	

}
