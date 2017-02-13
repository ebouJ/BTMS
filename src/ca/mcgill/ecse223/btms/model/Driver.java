/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.22.0.5146 modeling language!*/

package ca.mcgill.ecse223.btms.model;
import java.io.Serializable;
import java.util.*;

// line 43 "../../../../../BTMSPersistence.ump"
// line 61 "../../../../../BTMS.ump"
public class Driver implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Driver Attributes
  private String name;
  private boolean onSickLeave;

  //Autounique Attributes
  private int id;

  //Driver Associations
  private BTMS bTMS;
  private List<DriverSchedule> driverSchedules;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Driver(String aName, boolean aOnSickLeave, BTMS aBTMS)
  {
    // line 65 "../../../../../BTMS.ump"
    if (aName == null || aName.length() == 0) {
    	  throw new RuntimeException("The name of a driver cannot be empty.");
    	}
    name = aName;
    onSickLeave = aOnSickLeave;
    id = nextId++;
    boolean didAddBTMS = setBTMS(aBTMS);
    if (!didAddBTMS)
    {
      throw new RuntimeException("Unable to create driver due to bTMS");
    }
    driverSchedules = new ArrayList<DriverSchedule>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    // line 70 "../../../../../BTMS.ump"
    if (aName == null || aName.length() == 0) {
    	  return false;
    	}
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setOnSickLeave(boolean aOnSickLeave)
  {
    boolean wasSet = false;
    onSickLeave = aOnSickLeave;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public boolean getOnSickLeave()
  {
    return onSickLeave;
  }

  public int getId()
  {
    return id;
  }

  public boolean isOnSickLeave()
  {
    return onSickLeave;
  }

  public BTMS getBTMS()
  {
    return bTMS;
  }

  public DriverSchedule getDriverSchedule(int index)
  {
    DriverSchedule aDriverSchedule = driverSchedules.get(index);
    return aDriverSchedule;
  }

  public List<DriverSchedule> getDriverSchedules()
  {
    List<DriverSchedule> newDriverSchedules = Collections.unmodifiableList(driverSchedules);
    return newDriverSchedules;
  }

  public int numberOfDriverSchedules()
  {
    int number = driverSchedules.size();
    return number;
  }

  public boolean hasDriverSchedules()
  {
    boolean has = driverSchedules.size() > 0;
    return has;
  }

  public int indexOfDriverSchedule(DriverSchedule aDriverSchedule)
  {
    int index = driverSchedules.indexOf(aDriverSchedule);
    return index;
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
      existingBTMS.removeDriver(this);
    }
    bTMS.addDriver(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfDriverSchedules()
  {
    return 0;
  }

  public DriverSchedule addDriverSchedule(RouteAssignment aAssignment, BTMS aBTMS)
  {
    return new DriverSchedule(this, aAssignment, aBTMS);
  }

  public boolean addDriverSchedule(DriverSchedule aDriverSchedule)
  {
    boolean wasAdded = false;
    if (driverSchedules.contains(aDriverSchedule)) { return false; }
    Driver existingDriver = aDriverSchedule.getDriver();
    boolean isNewDriver = existingDriver != null && !this.equals(existingDriver);
    if (isNewDriver)
    {
      aDriverSchedule.setDriver(this);
    }
    else
    {
      driverSchedules.add(aDriverSchedule);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDriverSchedule(DriverSchedule aDriverSchedule)
  {
    boolean wasRemoved = false;
    //Unable to remove aDriverSchedule, as it must always have a driver
    if (!this.equals(aDriverSchedule.getDriver()))
    {
      driverSchedules.remove(aDriverSchedule);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addDriverScheduleAt(DriverSchedule aDriverSchedule, int index)
  {  
    boolean wasAdded = false;
    if(addDriverSchedule(aDriverSchedule))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDriverSchedules()) { index = numberOfDriverSchedules() - 1; }
      driverSchedules.remove(aDriverSchedule);
      driverSchedules.add(index, aDriverSchedule);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDriverScheduleAt(DriverSchedule aDriverSchedule, int index)
  {
    boolean wasAdded = false;
    if(driverSchedules.contains(aDriverSchedule))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDriverSchedules()) { index = numberOfDriverSchedules() - 1; }
      driverSchedules.remove(aDriverSchedule);
      driverSchedules.add(index, aDriverSchedule);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDriverScheduleAt(aDriverSchedule, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    BTMS placeholderBTMS = bTMS;
    this.bTMS = null;
    placeholderBTMS.removeDriver(this);
    for(int i=driverSchedules.size(); i > 0; i--)
    {
      DriverSchedule aDriverSchedule = driverSchedules.get(i - 1);
      aDriverSchedule.delete();
    }
  }

  // line 49 "../../../../../BTMSPersistence.ump"
   public static  void reinitializeAutouniqueID(List<Driver> drivers){
    nextId = 0; 
    for (Driver driver : drivers) {
      if (driver.getId() > nextId) {
        nextId = driver.getId();
      }
    }
    nextId++;
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "onSickLeave" + ":" + getOnSickLeave()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bTMS = "+(getBTMS()!=null?Integer.toHexString(System.identityHashCode(getBTMS())):"null")
     + outputString;
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 46 ../../../../../BTMSPersistence.ump
  private static final long serialVersionUID = 2045406856025012133L ;

  
}