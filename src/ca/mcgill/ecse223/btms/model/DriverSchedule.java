/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.22.0.5146 modeling language!*/

package ca.mcgill.ecse223.btms.model;
import java.io.Serializable;

// line 60 "../../../../../BTMSPersistence.ump"
// line 77 "../../../../../BTMS.ump"
public class DriverSchedule implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //DriverSchedule State Machines
  public enum Shift { Morning, Afternoon, Night }
  private Shift shift;

  //DriverSchedule Associations
  private Driver driver;
  private RouteAssignment assignment;
  private BTMS bTMS;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public DriverSchedule(Driver aDriver, RouteAssignment aAssignment, BTMS aBTMS)
  {
    boolean didAddDriver = setDriver(aDriver);
    if (!didAddDriver)
    {
      throw new RuntimeException("Unable to create driverSchedule due to driver");
    }
    boolean didAddAssignment = setAssignment(aAssignment);
    if (!didAddAssignment)
    {
      throw new RuntimeException("Unable to create driverSchedule due to assignment");
    }
    boolean didAddBTMS = setBTMS(aBTMS);
    if (!didAddBTMS)
    {
      throw new RuntimeException("Unable to create schedule due to bTMS");
    }
    setShift(Shift.Morning);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getShiftFullName()
  {
    String answer = shift.toString();
    return answer;
  }

  public Shift getShift()
  {
    return shift;
  }

  public boolean setShift(Shift aShift)
  {
    shift = aShift;
    return true;
  }

  public Driver getDriver()
  {
    return driver;
  }

  public RouteAssignment getAssignment()
  {
    return assignment;
  }

  public BTMS getBTMS()
  {
    return bTMS;
  }

  public boolean setDriver(Driver aDriver)
  {
    boolean wasSet = false;
    if (aDriver == null)
    {
      return wasSet;
    }

    Driver existingDriver = driver;
    driver = aDriver;
    if (existingDriver != null && !existingDriver.equals(aDriver))
    {
      existingDriver.removeDriverSchedule(this);
    }
    driver.addDriverSchedule(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setAssignment(RouteAssignment aAssignment)
  {
    boolean wasSet = false;
    if (aAssignment == null)
    {
      return wasSet;
    }

    RouteAssignment existingAssignment = assignment;
    assignment = aAssignment;
    if (existingAssignment != null && !existingAssignment.equals(aAssignment))
    {
      existingAssignment.removeDriverSchedule(this);
    }
    assignment.addDriverSchedule(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setBTMS(BTMS aBTMS)
  {
    boolean wasSet = false;
    if (aBTMS == null)
    {
      return wasSet;
    }

    BTMS existingBTMS = bTMS;
    bTMS = aBTMS;
    if (existingBTMS != null && !existingBTMS.equals(aBTMS))
    {
      existingBTMS.removeSchedule(this);
    }
    bTMS.addSchedule(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Driver placeholderDriver = driver;
    this.driver = null;
    placeholderDriver.removeDriverSchedule(this);
    RouteAssignment placeholderAssignment = assignment;
    this.assignment = null;
    placeholderAssignment.removeDriverSchedule(this);
    BTMS placeholderBTMS = bTMS;
    this.bTMS = null;
    placeholderBTMS.removeSchedule(this);
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 63 ../../../../../BTMSPersistence.ump
  private static final long serialVersionUID = -7403802774454467836L ;

  
}