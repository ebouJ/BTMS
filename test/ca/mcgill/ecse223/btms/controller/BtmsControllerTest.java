package ca.mcgill.ecse223.btms.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.ecse223.btms.application.BtmsApplication;
import ca.mcgill.ecse223.btms.model.BTMS;
import ca.mcgill.ecse223.btms.model.BusVehicle;
import ca.mcgill.ecse223.btms.model.Driver;
import ca.mcgill.ecse223.btms.model.DriverSchedule;
import ca.mcgill.ecse223.btms.model.DriverSchedule.Shift;
import ca.mcgill.ecse223.btms.model.Route;
import ca.mcgill.ecse223.btms.model.RouteAssignment;

public class BtmsControllerTest {
	
	private static int nextDriverID = 1;
	
	@BeforeClass
	public static void setUpOnce() {
		String filename = "testdata.btms";
		BtmsApplication.setFilename(filename);
		File f = new File(filename);
		f.delete();
	}
	
	@Before
	public void setUp() {
		// clear all data
		BTMS btms = BtmsApplication.getBtms();
		btms.delete();
	}
	
	@Test
	public void testCreateDriverSuccess() {
		BTMS btms = BtmsApplication.getBtms();
		String name = "driver";
		int id = nextDriverID++;
		
		BtmsController bc = new BtmsController();
		try {
			bc.createDriver(name);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
		
		// check model in memory
		checkResultDriver(name, btms, 1, false, id);
	}
	
	@Test
	public void testCreateDriverNull() {
		BTMS btms = BtmsApplication.getBtms();		
		String name = null;

		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.createDriver(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("The name of a driver cannot be empty.", error);
		// check no change in memory
		checkResultDriver(name, btms, 0, false, 1);
	}
	
	@Test
	public void testCreateDriverEmpty() {
		BTMS btms = BtmsApplication.getBtms();		
		String name = "";

		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.createDriver(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("The name of a driver cannot be empty.", error);
		// check no change in memory
		checkResultDriver(name, btms, 0, false, 1);
	}
	
	@Test
	public void testToggleSickStatus() {
		BTMS btms = BtmsApplication.getBtms();
		String name = "driver";
		int id = nextDriverID++;
		Driver driver = btms.addDriver(name, false);
		// check that sick status is false
		checkResultDriver(name, btms, 1, false, id);

		BtmsController bc = new BtmsController();
		try {
			bc.toggleSickStatus(driver);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
		
		// check that sick status is true
		checkResultDriver(name, btms, 1, true, id);
	}

	private void checkResultDriver(String name, BTMS btms, int numberDrivers, boolean sickStatus, int id) {
		assertEquals(numberDrivers, btms.getDrivers().size());
		if (numberDrivers > 0) {
			assertEquals(name, btms.getDriver(0).getName());
			assertEquals(sickStatus, btms.getDriver(0).getOnSickLeave());
			assertEquals(id, btms.getDriver(0).getId());
			assertEquals(btms, btms.getDriver(0).getBTMS());
			assertEquals(0, btms.getDriver(0).getDriverSchedules().size());
		}
		assertEquals(0, btms.getRoutes().size());
		assertEquals(0, btms.getVehicles().size());
		assertEquals(0, btms.getAssignments().size());
		assertEquals(0, btms.getSchedule().size());
	}
	
	@Test
	public void testCreateRouteSuccess() {
		BTMS btms = BtmsApplication.getBtms();
		int number = 1;
		
		BtmsController bc = new BtmsController();
		try {
			bc.createRoute(number);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
		
		// check model in memory
		checkResultRoute(number, btms, 1);
	}
	
	@Test
	public void testCreateRouteNotPositive() {
		BTMS btms = BtmsApplication.getBtms();		
		int number = 0;

		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.createRoute(number);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("The number of a route must be greater than zero.", error);
		// check no change in memory
		checkResultRoute(number, btms, 0);
	}
	
	@Test
	public void testCreateRouteDuplicate() {
		BTMS btms = BtmsApplication.getBtms();		
		int number = 1;
		
		BtmsController bc = new BtmsController();
		try {
			bc.createRoute(number);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}

		String error = null;
		try {
			bc.createRoute(number);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("A route with this number already exists. Please use a different number.", error);
		// check no change in memory
		checkResultRoute(number, btms, 1);
	}
	
	private void checkResultRoute(int number, BTMS btms, int numberRoutes) {
		assertEquals(numberRoutes, btms.getRoutes().size());
		if (numberRoutes > 0) {
			assertEquals(number, btms.getRoute(0).getNumber());
			assertEquals(btms, btms.getRoute(0).getBTMS());
			assertEquals(0, btms.getRoute(0).getRouteAssignments().size());
		}
		assertEquals(0, btms.getDrivers().size());
		assertEquals(0, btms.getVehicles().size());
		assertEquals(0, btms.getAssignments().size());
		assertEquals(0, btms.getSchedule().size());
	}

	@Test
	public void testCreateBusSuccess() {
		BTMS btms = BtmsApplication.getBtms();
		String licencePlate = "XYZ123";
		
		BtmsController bc = new BtmsController();
		try {
			bc.createBus(licencePlate);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
		
		// check model in memory
		checkResultBus(licencePlate, btms, 1, false);
	}
	
	@Test
	public void testCreateBusNull() {
		BTMS btms = BtmsApplication.getBtms();		
		String licencePlate = null;

		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.createBus(licencePlate);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("The licence plate of a bus cannot be empty.", error);
		// check no change in memory
		checkResultBus(licencePlate, btms, 0, false);
	}
	
	@Test
	public void testCreateBusEmpty() {
		BTMS btms = BtmsApplication.getBtms();		
		String licencePlate = "";

		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.createBus(licencePlate);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("The licence plate of a bus cannot be empty.", error);
		// check no change in memory
		checkResultBus(licencePlate, btms, 0, false);
	}
	
	@Test
	public void testCreateBusDuplicate() {
		BTMS btms = BtmsApplication.getBtms();
		String licencePlate = "XYZ123";
		
		BtmsController bc = new BtmsController();
		try {
			bc.createBus(licencePlate);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}

		String error = null;
		try {
			bc.createBus(licencePlate);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("A bus with this licence plate already exists. Please use a different licence plate.", error);
		// check no change in memory
		checkResultBus(licencePlate, btms, 1, false);
	}
	
	@Test
	public void testToggleRepairStatus() {
		BTMS btms = BtmsApplication.getBtms();
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		// check that repair status is false
		checkResultBus(licencePlate, btms, 1, false);

		BtmsController bc = new BtmsController();
		try {
			bc.toggleRepairStatus(bus);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
		
		// check that repair status is true
		checkResultBus(licencePlate, btms, 1, true);
	}

	private void checkResultBus(String licencePlate, BTMS btms, int numberBuses, boolean repairStatus) {
		assertEquals(numberBuses, btms.getVehicles().size());
		if (numberBuses > 0) {
			assertEquals(licencePlate, btms.getVehicle(0).getLicencePlate());
			assertEquals(repairStatus, btms.getVehicle(0).getInRepairShop());
			assertEquals(btms, btms.getVehicle(0).getBTMS());
			assertEquals(0, btms.getVehicle(0).getRouteAssignments().size());
		}
		assertEquals(0, btms.getRoutes().size());
		assertEquals(0, btms.getDrivers().size());
		assertEquals(0, btms.getAssignments().size());
		assertEquals(0, btms.getSchedule().size());
	}

	@Test
	public void testAssignSuccess() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date date = new java.sql.Date(tempDate.getTime());
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		int number = 1;
		Route route = btms.addRoute(number);
		
		BtmsController bc = new BtmsController();
		try {
			bc.assign(bus, route, date);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
		
		// check model in memory
		checkResultAssignment(bus, route, date, btms, 1, 1, 1);
	}
	
	@Test
	public void testAssignBusSameDateDifferentRoute() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date date = new java.sql.Date(tempDate.getTime());
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		int number = 1;
		Route route = btms.addRoute(number);
		int number2 = 2;
		Route route2 = btms.addRoute(number2);
		
		BtmsController bc = new BtmsController();
		try {
			bc.assign(bus, route, date);
			bc.assign(bus, route2, date);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
				
		// check model in memory
		checkResultAssignment(bus, route2, date, btms, 1, 2, 1);
	}
	
	@Test
	public void testAssignDateMoreThan365Days() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.util.Date tempFutureDate = new java.util.Date(tempDate.getTime() + 366*24*60*60*1000L);
		java.sql.Date date = new java.sql.Date(tempFutureDate.getTime());
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		int number = 1;
		Route route = btms.addRoute(number);
		
		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.assign(bus, route, date);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("The date must be within a year from today.", error);
		// check no change in memory
		checkResultAssignment(bus, route, date, btms, 0, 1, 1);
	}
	
	@Test
	public void testAssignBusNull() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date date = new java.sql.Date(tempDate.getTime());
		BusVehicle bus = null;
		int number = 1;
		Route route = btms.addRoute(number);
		
		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.assign(bus, route, date);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("A bus must be specified for the assignment.", error);
		// check no change in memory
		checkResultAssignment(bus, route, date, btms, 0, 1, 0);
	}
	
	@Test
	public void testAssignRouteNull() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date date = new java.sql.Date(tempDate.getTime());
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		Route route = null;
		
		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.assign(bus, route, date);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("A route must be specified for the assignment.", error);
		// check no change in memory
		checkResultAssignment(bus, route, date, btms, 0, 0, 1);
	}
	
	private void checkResultAssignment(BusVehicle bus, Route route, Date date, BTMS btms, int numberAssignments, int numberRoutes, int numberBuses) {
		assertEquals(numberAssignments, btms.getAssignments().size());
		if (numberAssignments > 0) {
			assertEquals(date, btms.getAssignment(0).getDate());
			assertEquals(bus, btms.getAssignment(0).getBus());
			assertEquals(route, btms.getAssignment(0).getRoute());
			assertEquals(btms, btms.getAssignment(0).getBTMS());
			assertEquals(0, btms.getAssignment(0).getDriverSchedules().size());
		}
		assertEquals(numberRoutes, btms.getRoutes().size());
		assertEquals(0, btms.getDrivers().size());
		assertEquals(numberBuses, btms.getVehicles().size());
		assertEquals(0, btms.getSchedule().size());
	}

	@Test
	public void testScheduleSuccess() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date date = new java.sql.Date(tempDate.getTime());
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		int number = 1;
		Route route = btms.addRoute(number);
		RouteAssignment assignment = btms.addAssignment(date, bus, route);
		String name = "driver";
		int id = nextDriverID++;
		Driver driver = btms.addDriver(name, false);
		Shift shift = Shift.Afternoon;
				
		BtmsController bc = new BtmsController();
		try {
			bc.schedule(driver, assignment, shift);
		} catch (InvalidInputException e) {
			// check that no error occurred
			fail();
		}
		
		// check model in memory
		checkResultSchedule(driver, assignment, shift, btms, 1, 1, 1, 1, 1);
	}
	
	@Test
	public void testScheduleDriverNull() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date date = new java.sql.Date(tempDate.getTime());
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		int number = 1;
		Route route = btms.addRoute(number);
		RouteAssignment assignment = btms.addAssignment(date, bus, route);
		Driver driver = null;
		Shift shift = Shift.Afternoon;
		
		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.schedule(driver, assignment, shift);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("A driver must be specified for the schedule.", error);
		// check no change in memory
		checkResultSchedule(driver, assignment, shift, btms, 0, 1, 1, 1, 0);
	}
	
	@Test
	public void testScheduleAssignmentNull() {
		BTMS btms = BtmsApplication.getBtms();
		RouteAssignment assignment = null;
		String name = "driver";
		int id = nextDriverID++;
		Driver driver = btms.addDriver(name, false);
		Shift shift = Shift.Afternoon;
		
		String error = null;
		BtmsController bc = new BtmsController();
		try {
			bc.schedule(driver, assignment, shift);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("An assignment must be specified for the schedule.", error);
		// check no change in memory
		checkResultSchedule(driver, assignment, shift, btms, 0, 0, 0, 0, 1);
	}
	
	private void checkResultSchedule(Driver driver, RouteAssignment assignment, Shift shift, BTMS btms, int numberSchedules, int numberAssignments, int numberRoutes, int numberBuses, int numberDrivers) {
		assertEquals(numberSchedules, btms.getSchedule().size());
		if (numberSchedules > 0) {
			assertEquals(assignment, btms.getSchedule(0).getAssignment());
			assertEquals(driver, btms.getSchedule(0).getDriver());
			assertEquals(shift, btms.getSchedule(0).getShift());
			assertEquals(btms, btms.getSchedule(0).getBTMS());
		}
		assertEquals(numberAssignments, btms.getAssignments().size());
		assertEquals(numberRoutes, btms.getRoutes().size());
		assertEquals(numberDrivers, btms.getDrivers().size());
		assertEquals(numberBuses, btms.getVehicles().size());
	}

	@Test
	public void testGetAssignmentsForDateSuccessTwoAssignments() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date today = new java.sql.Date(tempDate.getTime());
		java.sql.Date tomorrow = new java.sql.Date(tempDate.getTime() + 24*60*60*1000);
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		String licencePlate2 = "123XYZ";
		BusVehicle bus2 = btms.addVehicle(licencePlate2, false);
		int number = 1;
		Route route = btms.addRoute(number);
		RouteAssignment assignment = btms.addAssignment(today, bus, route);
		RouteAssignment assignment2 = btms.addAssignment(tomorrow, bus2, route);
		RouteAssignment assignment3 = btms.addAssignment(today, bus, route);
		String name = "driver";
		int id = nextDriverID++;
		Driver driver = btms.addDriver(name, false);
		Shift shift1 = Shift.Morning;
		Shift shift2 = Shift.Afternoon;
		Shift shift3 = Shift.Night;
		DriverSchedule schedule = btms.addSchedule(driver, assignment);
		schedule.setShift(shift1);
		DriverSchedule schedule2 = btms.addSchedule(driver, assignment2);
		schedule2.setShift(shift2);
		DriverSchedule schedule3 = btms.addSchedule(driver, assignment3);
		schedule3.setShift(shift3);
		
		BtmsController bc = new BtmsController();
		ArrayList<RouteAssignment> result = (ArrayList<RouteAssignment>) bc.getAssignmentsForDate(today);
		
		// check model in memory
		assertEquals(2, result.size());
		assertEquals(assignment, result.get(0));
		assertEquals(assignment3, result.get(1));
	}

	@Test
	public void testGetAssignmentsForDateSuccessOneAssignment() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date today = new java.sql.Date(tempDate.getTime());
		java.sql.Date tomorrow = new java.sql.Date(tempDate.getTime() + 24*60*60*1000);
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		String licencePlate2 = "123XYZ";
		BusVehicle bus2 = btms.addVehicle(licencePlate2, false);
		int number = 1;
		Route route = btms.addRoute(number);
		RouteAssignment assignment = btms.addAssignment(today, bus, route);
		RouteAssignment assignment2 = btms.addAssignment(tomorrow, bus2, route);
		RouteAssignment assignment3 = btms.addAssignment(today, bus, route);
		String name = "driver";
		int id = nextDriverID++;
		Driver driver = btms.addDriver(name, false);
		Shift shift1 = Shift.Morning;
		Shift shift2 = Shift.Afternoon;
		Shift shift3 = Shift.Night;
		DriverSchedule schedule = btms.addSchedule(driver, assignment);
		schedule.setShift(shift1);
		DriverSchedule schedule2 = btms.addSchedule(driver, assignment2);
		schedule2.setShift(shift2);
		DriverSchedule schedule3 = btms.addSchedule(driver, assignment3);
		schedule3.setShift(shift3);
		
		BtmsController bc = new BtmsController();
		ArrayList<RouteAssignment> result = (ArrayList<RouteAssignment>) bc.getAssignmentsForDate(tomorrow);
		
		// check model in memory
		assertEquals(1, result.size());
		assertEquals(assignment2, result.get(0));
	}

	@Test
	public void testGetAssignmentsForDateSuccessNoAssignment() {
		BTMS btms = BtmsApplication.getBtms();
		java.util.Date tempDate = btms.getCurrentDate();
		java.sql.Date today = new java.sql.Date(tempDate.getTime());
		java.sql.Date tomorrow = new java.sql.Date(tempDate.getTime() + 24*60*60*1000);
		java.sql.Date dayAfterTomorrow = new java.sql.Date(tempDate.getTime() + 2*24*60*60*1000);
		String licencePlate = "XYZ123";
		BusVehicle bus = btms.addVehicle(licencePlate, false);
		String licencePlate2 = "123XYZ";
		BusVehicle bus2 = btms.addVehicle(licencePlate2, false);
		int number = 1;
		Route route = btms.addRoute(number);
		RouteAssignment assignment = btms.addAssignment(today, bus, route);
		RouteAssignment assignment2 = btms.addAssignment(tomorrow, bus2, route);
		RouteAssignment assignment3 = btms.addAssignment(today, bus, route);
		String name = "driver";
		int id = nextDriverID++;
		Driver driver = btms.addDriver(name, false);
		Shift shift1 = Shift.Morning;
		Shift shift2 = Shift.Afternoon;
		Shift shift3 = Shift.Night;
		DriverSchedule schedule = btms.addSchedule(driver, assignment);
		schedule.setShift(shift1);
		DriverSchedule schedule2 = btms.addSchedule(driver, assignment2);
		schedule2.setShift(shift2);
		DriverSchedule schedule3 = btms.addSchedule(driver, assignment3);
		schedule3.setShift(shift3);
		
		BtmsController bc = new BtmsController();
		ArrayList<RouteAssignment> result = (ArrayList<RouteAssignment>) bc.getAssignmentsForDate(dayAfterTomorrow);
		
		// check model in memory
		assertEquals(0, result.size());
	}
	
}
