/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.22.0.5146 modeling language!*/

package ca.mcgill.ecse223.btms.model;
import java.io.Serializable;
import java.util.*;
import java.sql.Date;

// line 3 "../../../../../BTMSPersistence.ump"
// line 5 "../../../../../BTMS.ump"
public class BTMS implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //BTMS Associations
  private List<BusVehicle> vehicles;
  private List<Route> routes;
  private List<RouteAssignment> assignments;
  private List<Driver> drivers;
  private List<DriverSchedule> schedule;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public BTMS()
  {
    vehicles = new ArrayList<BusVehicle>();
    routes = new ArrayList<Route>();
    assignments = new ArrayList<RouteAssignment>();
    drivers = new ArrayList<Driver>();
    schedule = new ArrayList<DriverSchedule>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public BusVehicle getVehicle(int index)
  {
    BusVehicle aVehicle = vehicles.get(index);
    return aVehicle;
  }

  public List<BusVehicle> getVehicles()
  {
    List<BusVehicle> newVehicles = Collections.unmodifiableList(vehicles);
    return newVehicles;
  }

  public int numberOfVehicles()
  {
    int number = vehicles.size();
    return number;
  }

  public boolean hasVehicles()
  {
    boolean has = vehicles.size() > 0;
    return has;
  }

  public int indexOfVehicle(BusVehicle aVehicle)
  {
    int index = vehicles.indexOf(aVehicle);
    return index;
  }

  public Route getRoute(int index)
  {
    Route aRoute = routes.get(index);
    return aRoute;
  }

  public List<Route> getRoutes()
  {
    List<Route> newRoutes = Collections.unmodifiableList(routes);
    return newRoutes;
  }

  public int numberOfRoutes()
  {
    int number = routes.size();
    return number;
  }

  public boolean hasRoutes()
  {
    boolean has = routes.size() > 0;
    return has;
  }

  public int indexOfRoute(Route aRoute)
  {
    int index = routes.indexOf(aRoute);
    return index;
  }

  public RouteAssignment getAssignment(int index)
  {
    RouteAssignment aAssignment = assignments.get(index);
    return aAssignment;
  }

  public List<RouteAssignment> getAssignments()
  {
    List<RouteAssignment> newAssignments = Collections.unmodifiableList(assignments);
    return newAssignments;
  }

  public int numberOfAssignments()
  {
    int number = assignments.size();
    return number;
  }

  public boolean hasAssignments()
  {
    boolean has = assignments.size() > 0;
    return has;
  }

  public int indexOfAssignment(RouteAssignment aAssignment)
  {
    int index = assignments.indexOf(aAssignment);
    return index;
  }

  public Driver getDriver(int index)
  {
    Driver aDriver = drivers.get(index);
    return aDriver;
  }

  public List<Driver> getDrivers()
  {
    List<Driver> newDrivers = Collections.unmodifiableList(drivers);
    return newDrivers;
  }

  public int numberOfDrivers()
  {
    int number = drivers.size();
    return number;
  }

  public boolean hasDrivers()
  {
    boolean has = drivers.size() > 0;
    return has;
  }

  public int indexOfDriver(Driver aDriver)
  {
    int index = drivers.indexOf(aDriver);
    return index;
  }

  public DriverSchedule getSchedule(int index)
  {
    DriverSchedule aSchedule = schedule.get(index);
    return aSchedule;
  }

  public List<DriverSchedule> getSchedule()
  {
    List<DriverSchedule> newSchedule = Collections.unmodifiableList(schedule);
    return newSchedule;
  }

  public int numberOfSchedule()
  {
    int number = schedule.size();
    return number;
  }

  public boolean hasSchedule()
  {
    boolean has = schedule.size() > 0;
    return has;
  }

  public int indexOfSchedule(DriverSchedule aSchedule)
  {
    int index = schedule.indexOf(aSchedule);
    return index;
  }

  public static int minimumNumberOfVehicles()
  {
    return 0;
  }

  public BusVehicle addVehicle(String aLicencePlate, boolean aInRepairShop)
  {
    return new BusVehicle(aLicencePlate, aInRepairShop, this);
  }

  public boolean addVehicle(BusVehicle aVehicle)
  {
    boolean wasAdded = false;
    if (vehicles.contains(aVehicle)) { return false; }
    BTMS existingBTMS = aVehicle.getBTMS();
    boolean isNewBTMS = existingBTMS != null && !this.equals(existingBTMS);
    if (isNewBTMS)
    {
      aVehicle.setBTMS(this);
    }
    else
    {
      vehicles.add(aVehicle);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeVehicle(BusVehicle aVehicle)
  {
    boolean wasRemoved = false;
    //Unable to remove aVehicle, as it must always have a bTMS
    if (!this.equals(aVehicle.getBTMS()))
    {
      vehicles.remove(aVehicle);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addVehicleAt(BusVehicle aVehicle, int index)
  {  
    boolean wasAdded = false;
    if(addVehicle(aVehicle))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfVehicles()) { index = numberOfVehicles() - 1; }
      vehicles.remove(aVehicle);
      vehicles.add(index, aVehicle);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveVehicleAt(BusVehicle aVehicle, int index)
  {
    boolean wasAdded = false;
    if(vehicles.contains(aVehicle))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfVehicles()) { index = numberOfVehicles() - 1; }
      vehicles.remove(aVehicle);
      vehicles.add(index, aVehicle);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addVehicleAt(aVehicle, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfRoutes()
  {
    return 0;
  }

  public Route addRoute(int aNumber)
  {
    return new Route(aNumber, this);
  }

  public boolean addRoute(Route aRoute)
  {
    boolean wasAdded = false;
    if (routes.contains(aRoute)) { return false; }
    BTMS existingBTMS = aRoute.getBTMS();
    boolean isNewBTMS = existingBTMS != null && !this.equals(existingBTMS);
    if (isNewBTMS)
    {
      aRoute.setBTMS(this);
    }
    else
    {
      routes.add(aRoute);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRoute(Route aRoute)
  {
    boolean wasRemoved = false;
    //Unable to remove aRoute, as it must always have a bTMS
    if (!this.equals(aRoute.getBTMS()))
    {
      routes.remove(aRoute);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addRouteAt(Route aRoute, int index)
  {  
    boolean wasAdded = false;
    if(addRoute(aRoute))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRoutes()) { index = numberOfRoutes() - 1; }
      routes.remove(aRoute);
      routes.add(index, aRoute);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRouteAt(Route aRoute, int index)
  {
    boolean wasAdded = false;
    if(routes.contains(aRoute))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRoutes()) { index = numberOfRoutes() - 1; }
      routes.remove(aRoute);
      routes.add(index, aRoute);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRouteAt(aRoute, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfAssignments()
  {
    return 0;
  }

  public RouteAssignment addAssignment(Date aDate, BusVehicle aBus, Route aRoute)
  {
    return new RouteAssignment(aDate, aBus, aRoute, this);
  }

  public boolean addAssignment(RouteAssignment aAssignment)
  {
    boolean wasAdded = false;
    if (assignments.contains(aAssignment)) { return false; }
    BTMS existingBTMS = aAssignment.getBTMS();
    boolean isNewBTMS = existingBTMS != null && !this.equals(existingBTMS);
    if (isNewBTMS)
    {
      aAssignment.setBTMS(this);
    }
    else
    {
      assignments.add(aAssignment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAssignment(RouteAssignment aAssignment)
  {
    boolean wasRemoved = false;
    //Unable to remove aAssignment, as it must always have a bTMS
    if (!this.equals(aAssignment.getBTMS()))
    {
      assignments.remove(aAssignment);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addAssignmentAt(RouteAssignment aAssignment, int index)
  {  
    boolean wasAdded = false;
    if(addAssignment(aAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignments()) { index = numberOfAssignments() - 1; }
      assignments.remove(aAssignment);
      assignments.add(index, aAssignment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAssignmentAt(RouteAssignment aAssignment, int index)
  {
    boolean wasAdded = false;
    if(assignments.contains(aAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignments()) { index = numberOfAssignments() - 1; }
      assignments.remove(aAssignment);
      assignments.add(index, aAssignment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAssignmentAt(aAssignment, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfDrivers()
  {
    return 0;
  }

  public Driver addDriver(String aName, boolean aOnSickLeave)
  {
    return new Driver(aName, aOnSickLeave, this);
  }

  public boolean addDriver(Driver aDriver)
  {
    boolean wasAdded = false;
    if (drivers.contains(aDriver)) { return false; }
    BTMS existingBTMS = aDriver.getBTMS();
    boolean isNewBTMS = existingBTMS != null && !this.equals(existingBTMS);
    if (isNewBTMS)
    {
      aDriver.setBTMS(this);
    }
    else
    {
      drivers.add(aDriver);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDriver(Driver aDriver)
  {
    boolean wasRemoved = false;
    //Unable to remove aDriver, as it must always have a bTMS
    if (!this.equals(aDriver.getBTMS()))
    {
      drivers.remove(aDriver);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addDriverAt(Driver aDriver, int index)
  {  
    boolean wasAdded = false;
    if(addDriver(aDriver))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDrivers()) { index = numberOfDrivers() - 1; }
      drivers.remove(aDriver);
      drivers.add(index, aDriver);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDriverAt(Driver aDriver, int index)
  {
    boolean wasAdded = false;
    if(drivers.contains(aDriver))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDrivers()) { index = numberOfDrivers() - 1; }
      drivers.remove(aDriver);
      drivers.add(index, aDriver);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDriverAt(aDriver, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfSchedule()
  {
    return 0;
  }

  public DriverSchedule addSchedule(Driver aDriver, RouteAssignment aAssignment)
  {
    return new DriverSchedule(aDriver, aAssignment, this);
  }

  public boolean addSchedule(DriverSchedule aSchedule)
  {
    boolean wasAdded = false;
    if (schedule.contains(aSchedule)) { return false; }
    BTMS existingBTMS = aSchedule.getBTMS();
    boolean isNewBTMS = existingBTMS != null && !this.equals(existingBTMS);
    if (isNewBTMS)
    {
      aSchedule.setBTMS(this);
    }
    else
    {
      schedule.add(aSchedule);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSchedule(DriverSchedule aSchedule)
  {
    boolean wasRemoved = false;
    //Unable to remove aSchedule, as it must always have a bTMS
    if (!this.equals(aSchedule.getBTMS()))
    {
      schedule.remove(aSchedule);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addScheduleAt(DriverSchedule aSchedule, int index)
  {  
    boolean wasAdded = false;
    if(addSchedule(aSchedule))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSchedule()) { index = numberOfSchedule() - 1; }
      schedule.remove(aSchedule);
      schedule.add(index, aSchedule);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveScheduleAt(DriverSchedule aSchedule, int index)
  {
    boolean wasAdded = false;
    if(schedule.contains(aSchedule))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSchedule()) { index = numberOfSchedule() - 1; }
      schedule.remove(aSchedule);
      schedule.add(index, aSchedule);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addScheduleAt(aSchedule, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (vehicles.size() > 0)
    {
      BusVehicle aVehicle = vehicles.get(vehicles.size() - 1);
      aVehicle.delete();
      vehicles.remove(aVehicle);
    }
    
      
    while (routes.size() > 0)
    {
      Route aRoute = routes.get(routes.size() - 1);
      aRoute.delete();
      routes.remove(aRoute);
    }
    
      
    while (assignments.size() > 0)
    {
      RouteAssignment aAssignment = assignments.get(assignments.size() - 1);
      aAssignment.delete();
      assignments.remove(aAssignment);
    }
    
      
    while (drivers.size() > 0)
    {
      Driver aDriver = drivers.get(drivers.size() - 1);
      aDriver.delete();
      drivers.remove(aDriver);
    }
    
      
    while (schedule.size() > 0)
    {
      DriverSchedule aSchedule = schedule.get(schedule.size() - 1);
      aSchedule.delete();
      schedule.remove(aSchedule);
    }
    
      
  }

  // line 13 "../../../../../BTMS.ump"
   public java.util.Date getCurrentDate(){
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    java.util.Date date = cal.getTime();
    return date;
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 6 ../../../../../BTMSPersistence.ump
  private static final long serialVersionUID = -2683593616927798071L ;

  
}