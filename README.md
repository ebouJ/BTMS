# BTMS
Bus Transportation Management System (BTMS)
Given the problem description below, define a domain model using a 
class diagram for the concepts of the proposed Bus Transportation
Management System and their relationships.
Problem Description
A city is using the Bus Transportation Management System (BTMS) to simplify the day-to-day
activities related to the city’s public bus system.
The BTMS keeps track of a driver’s name and automatically assigns a unique ID to each driver. 
A bus route is identified by a unique number that is determined by city staff, while a bus is 
identified by its unique licence plate. For up to a year in advance, city staff assigns buses 
to routes. Several buses may be assigned to a route per day. Each bus serves at the most one route 
per day but may be assigned to different routes on different days. Similarly, for up to a year 
in advance, city staff posts the schedule for its bus drivers. For each route, there is a
morning shift, an afternoon shift, and a night shift. A driver is assigned by city staff 
to a shift for a particular bus on a particular day. The BTMS offers city staff great 
flexibility, i.e., there are no restrictions in terms of how many shifts a bus driver has
per day. It is even possible to assign a bus driver to two shifts at the same time.
BTMS does not support the deletion or update of bus drivers, routes, or buses – only
adding is supported. However, BTMS does support indicating whether a bus driver is on
sick leave and whether a bus is in the repair shop. If that is the case, the driver 
cannot be scheduled or the bus cannot be assigned to a route. For a given day, an overview 
shows – for each route number – the licence plate number of each assigned bus, the entered
shifts and the IDs and names of the assigned drivers. If a driver is currently sick or a bus 
is in the repair shop, the driver or bus, respectively, is highlighted in the overview.
