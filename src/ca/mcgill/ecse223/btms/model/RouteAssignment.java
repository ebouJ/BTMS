/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.22.0.5146 modeling language!*/

package ca.mcgill.ecse223.btms.model;
import java.io.Serializable;
import java.sql.Date;
import java.util.*;

// line 37 "../../../../../BTMSPersistence.ump"
// line 55 "../../../../../BTMS.ump"
public class RouteAssignment implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RouteAssignment Attributes
  private Date date;

  //RouteAssignment Associations
  private BusVehicle bus;
  private Route route;
  private BTMS bTMS;
  private List<DriverSchedule> driverSchedules;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RouteAssignment(Date aDate, BusVehicle aBus, Route aRoute, BTMS aBTMS)
  {
    date = aDate;
    boolean didAddBus = setBus(aBus);
    if (!didAddBus)
    {
      throw new RuntimeException("Unable to create routeAssignment due to bus");
    }
    boolean didAddRoute = setRoute(aRoute);
    if (!didAddRoute)
    {
      throw new RuntimeException("Unable to create routeAssignment due to route");
    }
    boolean didAddBTMS = setBTMS(aBTMS);
    if (!didAddBTMS)
    {
      throw new RuntimeException("Unable to create assignment due to bTMS");
    }
    driverSchedules = new ArrayList<DriverSchedule>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public BusVehicle getBus()
  {
    return bus;
  }

  public Route getRoute()
  {
    return route;
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

  public boolean setBus(BusVehicle aBus)
  {
    boolean wasSet = false;
    if (aBus == null)
    {
      return wasSet;
    }

    BusVehicle existingBus = bus;
    bus = aBus;
    if (existingBus != null && !existingBus.equals(aBus))
    {
      existingBus.removeRouteAssignment(this);
    }
    bus.addRouteAssignment(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setRoute(Route aRoute)
  {
    boolean wasSet = false;
    if (aRoute == null)
    {
      return wasSet;
    }

    Route existingRoute = route;
    route = aRoute;
    if (existingRoute != null && !existingRoute.equals(aRoute))
    {
      existingRoute.removeRouteAssignment(this);
    }
    route.addRouteAssignment(this);
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
      existingBTMS.removeAssignment(this);
    }
    bTMS.addAssignment(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfDriverSchedules()
  {
    return 0;
  }

  public DriverSchedule addDriverSchedule(Driver aDriver, BTMS aBTMS)
  {
    return new DriverSchedule(aDriver, this, aBTMS);
  }

  public boolean addDriverSchedule(DriverSchedule aDriverSchedule)
  {
    boolean wasAdded = false;
    if (driverSchedules.contains(aDriverSchedule)) { return false; }
    RouteAssignment existingAssignment = aDriverSchedule.getAssignment();
    boolean isNewAssignment = existingAssignment != null && !this.equals(existingAssignment);
    if (isNewAssignment)
    {
      aDriverSchedule.setAssignment(this);
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
    //Unable to remove aDriverSchedule, as it must always have a assignment
    if (!this.equals(aDriverSchedule.getAssignment()))
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
    BusVehicle placeholderBus = bus;
    this.bus = null;
    placeholderBus.removeRouteAssignment(this);
    Route placeholderRoute = route;
    this.route = null;
    placeholderRoute.removeRouteAssignment(this);
    BTMS placeholderBTMS = bTMS;
    this.bTMS = null;
    placeholderBTMS.removeAssignment(this);
    for(int i=driverSchedules.size(); i > 0; i--)
    {
      DriverSchedule aDriverSchedule = driverSchedules.get(i - 1);
      aDriverSchedule.delete();
    }
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "bus = "+(getBus()!=null?Integer.toHexString(System.identityHashCode(getBus())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "route = "+(getRoute()!=null?Integer.toHexString(System.identityHashCode(getRoute())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bTMS = "+(getBTMS()!=null?Integer.toHexString(System.identityHashCode(getBTMS())):"null")
     + outputString;
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 40 ../../../../../BTMSPersistence.ump
  private static final long serialVersionUID = -3900912597282882073L ;

  
}