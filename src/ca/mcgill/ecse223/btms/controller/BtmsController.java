package ca.mcgill.ecse223.btms.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.mcgill.ecse223.btms.application.BtmsApplication;
import ca.mcgill.ecse223.btms.model.BTMS;
import ca.mcgill.ecse223.btms.model.BusVehicle;
import ca.mcgill.ecse223.btms.model.Driver;
import ca.mcgill.ecse223.btms.model.DriverSchedule;
import ca.mcgill.ecse223.btms.model.DriverSchedule.Shift;
import ca.mcgill.ecse223.btms.model.Route;
import ca.mcgill.ecse223.btms.model.RouteAssignment;

public class BtmsController {

	public BtmsController() {
	}
	
	public void createDriver(String name) throws InvalidInputException {
		BTMS btms = BtmsApplication.getBtms();
		try {
			btms.addDriver(name, false);
			BtmsApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}
	
	public void createRoute(int number) throws InvalidInputException {
		if (number <= 0) {
			throw new InvalidInputException("The number of a route must be greater than zero.");
		}

		BTMS btms = BtmsApplication.getBtms();
		try {
			btms.addRoute(number);
			BtmsApplication.save();
		}
		catch (RuntimeException e) {
			String error = e.getMessage();
			if (error.equals("Cannot create due to duplicate number")) {
				error = "A route with this number already exists. Please use a different number.";
			}
			throw new InvalidInputException(error);
		}		
	}
	
	public void createBus(String licencePlate) throws InvalidInputException {
		BTMS btms = BtmsApplication.getBtms();
		try {
			btms.addVehicle(licencePlate, false);
			BtmsApplication.save();
		}
		catch (RuntimeException e) {
			String error = e.getMessage();
			if (error.equals("Cannot create due to duplicate licencePlate")) {
				error = "A bus with this licence plate already exists. Please use a different licence plate.";
			}
			throw new InvalidInputException(error);
		}
	}
	
	public void toggleSickStatus(Driver driver) throws InvalidInputException {
		driver.setOnSickLeave(!driver.getOnSickLeave());
		try {
			BtmsApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public void toggleRepairStatus(BusVehicle bus) throws InvalidInputException {
		bus.setInRepairShop(!bus.getInRepairShop());
		try {
			BtmsApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}
	
	public void assign(BusVehicle bus, Route route, Date date) throws InvalidInputException {
		date = cleanDate(date);
		String error = "";
		if (!isWithin365DaysFromToday(date)) {
			error = "The date must be within a year from today. ";
		}
		if (bus == null) {
			error = error + "A bus must be specified for the assignment. ";
		}
		if (route == null) {
			error = error + "A route must be specified for the assignment.";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		BTMS btms = BtmsApplication.getBtms();
		try {
			RouteAssignment assignment = bus.getAssignmentOnDate(date);
			if (assignment == null) {
				btms.addAssignment(date, bus, route);				
			}
			else {
				assignment.setRoute(route);
			}
			BtmsApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	private Date cleanDate(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(date.getTime());
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    java.util.Date tempCleanedDate = cal.getTime();
	    java.sql.Date cleanedDate = new java.sql.Date(tempCleanedDate.getTime());
	    return cleanedDate;
	}

	private boolean isWithin365DaysFromToday(Date date) {
		java.util.Date tempToday = BtmsApplication.getBtms().getCurrentDate();
		java.util.Date tempInOneYear = new java.util.Date(tempToday.getTime() + 366*24*60*60*1000L);
		java.sql.Date inOneYear = new java.sql.Date(tempInOneYear.getTime());
		return date.before(inOneYear);
	}
	
	public void schedule(Driver driver, RouteAssignment routeAssignment, Shift shift) throws InvalidInputException {
		String error = "";
		if (driver == null) {
			error = "A driver must be specified for the schedule. ";
		}
		if (routeAssignment == null) {
			error = error + "An assignment must be specified for the schedule.";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		BTMS btms = BtmsApplication.getBtms();
		try {
			DriverSchedule schedule = btms.addSchedule(driver, routeAssignment);
			schedule.setShift(shift);
			BtmsApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public List<RouteAssignment> getAssignmentsForDate(Date date) {
		date = cleanDate(date);
		BTMS btms = BtmsApplication.getBtms();
		ArrayList<RouteAssignment> result = new ArrayList<RouteAssignment>();
		for (RouteAssignment assignment : btms.getAssignments()) {
			if (assignment.getDate().equals(date)) 
				result.add(assignment);
		}
		return result;
	}
	
	public List<Driver> getDrivers() {
		return BtmsApplication.getBtms().getDrivers();
	}
	
	public List<Driver> getAvailableDrivers() {
		ArrayList<Driver> result = new ArrayList<Driver>();
		for (Driver driver : BtmsApplication.getBtms().getDrivers()) {
			if (!driver.isOnSickLeave()) {
				result.add(driver);				
			}
		}
		return result;
	}
	
	public List<BusVehicle> getBuses() {
		return BtmsApplication.getBtms().getVehicles();
	}
	
	public List<BusVehicle> getAvailableBuses() {
		ArrayList<BusVehicle> result = new ArrayList<BusVehicle>();
		for (BusVehicle bus : BtmsApplication.getBtms().getVehicles()) {
			if (!bus.isInRepairShop()) {
				result.add(bus);				
			}
		}
		return result;
	}

	public List<Route> getRoutes() {
		return BtmsApplication.getBtms().getRoutes();
	}

	public List<RouteAssignment> getAssignments() {
		return BtmsApplication.getBtms().getAssignments();
	}

}
