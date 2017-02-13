/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.22.0.5146 modeling language!*/

package ca.mcgill.ecse223.btms.model;
import java.io.Serializable;
import java.util.*;
import java.sql.Date;

// line 23 "../../../../../BTMSPersistence.ump"
// line 51 "../../../../../BTMS.ump"
public class Route implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Route> routesByNumber = new HashMap<Integer, Route>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Route Attributes
  private int number;

  //Route Associations
  private BTMS bTMS;
  private List<RouteAssignment> routeAssignments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Route(int aNumber, BTMS aBTMS)
  {
    if (!setNumber(aNumber))
    {
      throw new RuntimeException("Cannot create due to duplicate number");
    }
    boolean didAddBTMS = setBTMS(aBTMS);
    if (!didAddBTMS)
    {
      throw new RuntimeException("Unable to create route due to bTMS");
    }
    routeAssignments = new ArrayList<RouteAssignment>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumber(int aNumber)
  {
    boolean wasSet = false;
    Integer anOldNumber = getNumber();
    if (hasWithNumber(aNumber)) {
      return wasSet;
    }
    number = aNumber;
    wasSet = true;
    if (anOldNumber != null) {
      routesByNumber.remove(anOldNumber);
    }
    routesByNumber.put(aNumber, this);
    return wasSet;
  }

  public int getNumber()
  {
    return number;
  }

  public static Route getWithNumber(int aNumber)
  {
    return routesByNumber.get(aNumber);
  }

  public static boolean hasWithNumber(int aNumber)
  {
    return getWithNumber(aNumber) != null;
  }

  public BTMS getBTMS()
  {
    return bTMS;
  }

  public RouteAssignment getRouteAssignment(int index)
  {
    RouteAssignment aRouteAssignment = routeAssignments.get(index);
    return aRouteAssignment;
  }

  public List<RouteAssignment> getRouteAssignments()
  {
    List<RouteAssignment> newRouteAssignments = Collections.unmodifiableList(routeAssignments);
    return newRouteAssignments;
  }

  public int numberOfRouteAssignments()
  {
    int number = routeAssignments.size();
    return number;
  }

  public boolean hasRouteAssignments()
  {
    boolean has = routeAssignments.size() > 0;
    return has;
  }

  public int indexOfRouteAssignment(RouteAssignment aRouteAssignment)
  {
    int index = routeAssignments.indexOf(aRouteAssignment);
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
      existingBTMS.removeRoute(this);
    }
    bTMS.addRoute(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfRouteAssignments()
  {
    return 0;
  }

  public RouteAssignment addRouteAssignment(Date aDate, BusVehicle aBus, BTMS aBTMS)
  {
    return new RouteAssignment(aDate, aBus, this, aBTMS);
  }

  public boolean addRouteAssignment(RouteAssignment aRouteAssignment)
  {
    boolean wasAdded = false;
    if (routeAssignments.contains(aRouteAssignment)) { return false; }
    Route existingRoute = aRouteAssignment.getRoute();
    boolean isNewRoute = existingRoute != null && !this.equals(existingRoute);
    if (isNewRoute)
    {
      aRouteAssignment.setRoute(this);
    }
    else
    {
      routeAssignments.add(aRouteAssignment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRouteAssignment(RouteAssignment aRouteAssignment)
  {
    boolean wasRemoved = false;
    //Unable to remove aRouteAssignment, as it must always have a route
    if (!this.equals(aRouteAssignment.getRoute()))
    {
      routeAssignments.remove(aRouteAssignment);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addRouteAssignmentAt(RouteAssignment aRouteAssignment, int index)
  {  
    boolean wasAdded = false;
    if(addRouteAssignment(aRouteAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRouteAssignments()) { index = numberOfRouteAssignments() - 1; }
      routeAssignments.remove(aRouteAssignment);
      routeAssignments.add(index, aRouteAssignment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRouteAssignmentAt(RouteAssignment aRouteAssignment, int index)
  {
    boolean wasAdded = false;
    if(routeAssignments.contains(aRouteAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRouteAssignments()) { index = numberOfRouteAssignments() - 1; }
      routeAssignments.remove(aRouteAssignment);
      routeAssignments.add(index, aRouteAssignment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRouteAssignmentAt(aRouteAssignment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    routesByNumber.remove(getNumber());
    BTMS placeholderBTMS = bTMS;
    this.bTMS = null;
    placeholderBTMS.removeRoute(this);
    for(int i=routeAssignments.size(); i > 0; i--)
    {
      RouteAssignment aRouteAssignment = routeAssignments.get(i - 1);
      aRouteAssignment.delete();
    }
  }

  // line 29 "../../../../../BTMSPersistence.ump"
   public static  void reinitializeUniqueNumber(List<Route> routes){
    routesByNumber = new HashMap<Integer, Route>();
    for (Route route : routes) {
      routesByNumber.put(route.getNumber(), route);
    }
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "number" + ":" + getNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bTMS = "+(getBTMS()!=null?Integer.toHexString(System.identityHashCode(getBTMS())):"null")
     + outputString;
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 26 ../../../../../BTMSPersistence.ump
  private static final long serialVersionUID = 8896099581655989380L ;

  
}