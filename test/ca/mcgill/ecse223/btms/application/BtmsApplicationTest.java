package ca.mcgill.ecse223.btms.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.ecse223.btms.model.BTMS;
import ca.mcgill.ecse223.btms.model.BusVehicle;
import ca.mcgill.ecse223.btms.model.Driver;
import ca.mcgill.ecse223.btms.model.DriverSchedule;
import ca.mcgill.ecse223.btms.model.DriverSchedule.Shift;
import ca.mcgill.ecse223.btms.model.Route;
import ca.mcgill.ecse223.btms.model.RouteAssignment;

public class BtmsApplicationTest {
	
	private static int nextDriverID = 1;
	private static String filename = "testdata.btms";
	
	@BeforeClass
	public static void setUpOnce() {
		BtmsApplication.setFilename(filename);		
	}
	
	@Before
	public void setUp() {
		// remove test file
		File f = new File(filename);
		f.delete();
		// clear all data
		BTMS btms = BtmsApplication.getBtms();
		btms.delete();
	}
	
	@Test
	public void testPersistence() {
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
		DriverSchedule schedule = btms.addSchedule(driver, assignment);
		schedule.setShift(shift);
		BtmsApplication.save();
		
		// load model again and check it
		BTMS btms2 = (BTMS) BtmsApplication.load();
		checkResultSchedule(driver.getName(), driver.isOnSickLeave(), driver.getId(), 
							assignment.getDate(), assignment.getRoute().getNumber(), assignment.getBus().getLicencePlate(), assignment.getBus().isInRepairShop(), 
							shift, btms2, 1, 1, 1, 1, 1);
	}
	
	@Test
	public void testPersistenceReinitialization() {
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
		DriverSchedule schedule = btms.addSchedule(driver, assignment);
		schedule.setShift(shift);
		BtmsApplication.save();
		
		// simulate shutting down the application
		btms.delete();
		Driver.reinitializeAutouniqueID(new ArrayList<Driver>());
		BusVehicle.reinitializeUniqueLicencePlate(new ArrayList<BusVehicle>());
		Route.reinitializeUniqueNumber(new ArrayList<Route>());
		checkResultSchedule(name, false, id, date, number, licencePlate, false, shift, btms, 0, 0, 0, 0, 0);
		
		// load model again and add further model elements
		btms = BtmsApplication.load();
		checkResultSchedule(name, false, id, date, number, licencePlate, false, shift, btms, 1, 1, 1, 1, 1);
	
		String error = null;
		try {
			btms.addVehicle(licencePlate, false);
		} catch (RuntimeException e) {
			error = e.getMessage();
		}
		// check error
		assertEquals("Cannot create due to duplicate licencePlate", error);

		error = null;
		try {
			btms.addRoute(number);
		} catch (RuntimeException e) {
			error = e.getMessage();
		}
		// check error
		assertEquals("Cannot create due to duplicate number", error);
		
		btms.addDriver(name + name, true);
		BtmsApplication.save();
		
		// load model again and check it
		btms = BtmsApplication.load();
		checkResultScheduleTwoDrivers(name, false, id, date, number, licencePlate, false, shift, btms, 1, 1, 1, 1, 2, name + name, true, id + 1);
	}
	
	private void checkResultScheduleTwoDrivers(String driverName, boolean driverOnSickLeave, int driverId, 
			Date assignmentDate, int assignmentRouteNumber, String assignmentBusLicencePlate, boolean assignmentBusInRepairShop, 
			Shift shift, BTMS btms, int numberSchedules, int numberAssignments, int numberRoutes, int numberBuses, int numberDrivers,
			String driver2Name, boolean driver2OnSickLeave, int driver2Id) {
		checkResultSchedule(driverName, driverOnSickLeave, driverId, assignmentDate, assignmentRouteNumber, assignmentBusLicencePlate, assignmentBusInRepairShop, shift, 
							btms, numberSchedules, numberAssignments, numberRoutes, numberBuses, numberDrivers);
		if (numberDrivers > 1) {
			assertEquals(driver2Name, btms.getDriver(1).getName());
			assertEquals(driver2OnSickLeave, btms.getDriver(1).getOnSickLeave());
			assertEquals(driver2Id, btms.getDriver(1).getId());
			assertEquals(0, btms.getDriver(1).getDriverSchedules().size());
			assertEquals(btms, btms.getDriver(1).getBTMS());
		}
	}

	private void checkResultSchedule(String driverName, boolean driverOnSickLeave, int driverId, 
									Date assignmentDate, int assignmentRouteNumber, String assignmentBusLicencePlate, boolean assignmentBusInRepairShop, 
									Shift shift, BTMS btms, int numberSchedules, int numberAssignments, int numberRoutes, int numberBuses, int numberDrivers) {
		assertEquals(numberSchedules, btms.getSchedule().size());
		if (numberSchedules > 0) {
			assertEquals(shift, btms.getSchedule(0).getShift());
			assertNotNull(btms.getSchedule(0).getAssignment());
			assertNotNull(btms.getSchedule(0).getDriver());
			assertEquals(btms, btms.getSchedule(0).getBTMS());
		}
		assertEquals(numberAssignments, btms.getAssignments().size());
		if (numberAssignments > 0) {
			assertEquals(assignmentDate, btms.getAssignment(0).getDate());
			assertNotNull(btms.getAssignment(0).getBus());
			assertNotNull(btms.getAssignment(0).getRoute());
			assertEquals(numberSchedules, btms.getAssignment(0).getDriverSchedules().size());
			assertEquals(btms, btms.getAssignment(0).getBTMS());
		}
		assertEquals(numberRoutes, btms.getRoutes().size());
		if (numberRoutes > 0) {
			assertEquals(assignmentRouteNumber, btms.getRoute(0).getNumber());
			assertEquals(numberAssignments, btms.getRoute(0).getRouteAssignments().size());
			assertEquals(btms, btms.getRoute(0).getBTMS());
		}
		assertEquals(numberDrivers, btms.getDrivers().size());
		if (numberDrivers > 0) {
			assertEquals(driverName, btms.getDriver(0).getName());
			assertEquals(driverOnSickLeave, btms.getDriver(0).getOnSickLeave());
			assertEquals(driverId, btms.getDriver(0).getId());
			assertEquals(numberSchedules, btms.getDriver(0).getDriverSchedules().size());
			assertEquals(btms, btms.getDriver(0).getBTMS());
		}
		assertEquals(numberBuses, btms.getVehicles().size());
		if (numberBuses > 0) {
			assertEquals(assignmentBusLicencePlate, btms.getVehicle(0).getLicencePlate());
			assertEquals(assignmentBusInRepairShop, btms.getVehicle(0).getInRepairShop());
			assertEquals(numberAssignments, btms.getVehicle(0).getRouteAssignments().size());
			assertEquals(btms, btms.getVehicle(0).getBTMS());
		}
	}
	
}
