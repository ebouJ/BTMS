package ca.mcgill.ecse223.btms.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import ca.mcgill.ecse223.btms.controller.BtmsController;
import ca.mcgill.ecse223.btms.controller.InvalidInputException;
import ca.mcgill.ecse223.btms.model.BusVehicle;
import ca.mcgill.ecse223.btms.model.Driver;
import ca.mcgill.ecse223.btms.model.DriverSchedule;
import ca.mcgill.ecse223.btms.model.DriverSchedule.Shift;
import ca.mcgill.ecse223.btms.model.Route;
import ca.mcgill.ecse223.btms.model.RouteAssignment;

public class BtmsPage extends JFrame {

	private static final long serialVersionUID = -4426310869335015542L;
	
	// UI elements
	private JLabel errorMessage;
	// driver
	private JTextField driverNameTextField;
	private JLabel driverNameLabel;
	private JButton addDriverButton;
	private JComboBox<String> driverToggleList;
	private JLabel driverToggleLabel;
	private JButton sickButton;
	// route
	private JTextField routeNumberTextField;
	private JLabel routeNumberLabel;
	private JButton addRouteButton;
	// bus
	private JTextField busLicencePlateTextField;
	private JLabel busLicencePlateLabel;
	private JButton addBusButton;
	private JComboBox<String> busToggleList;
	private JLabel busToggleLabel;
	private JButton repairButton;
	// bus assignment
	private JComboBox<String> busList;
	private JLabel busLabel;
	private JComboBox<String> routeList;
	private JLabel routeLabel;
	private JDatePickerImpl assignmentDatePicker;
	private JLabel assignmentDateLabel;
	private JButton assignButton;
	// schedule driver
	private JComboBox<String> driverList;
	private JLabel driverLabel;
	private JComboBox<String> assignmentList;
	private JLabel assignmentLabel;
	private JComboBox<String> shiftList;
	private JLabel shiftLabel;
	private JButton scheduleButton;
	// daily overview
	private JDatePickerImpl overviewDatePicker;
	private JLabel overviewDateLabel; 
	private JTable overviewTable;
	private JScrollPane overviewScrollPane;

	// data elements
	private String error = null;
	// toggle sick status
	private Integer selectedSickDriver = -1;
	private HashMap<Integer, Driver> drivers;
	// toggle repairs status
	private Integer selectedRepairBus= -1;
	private HashMap<Integer, BusVehicle> buses;
	// bus assignment
	private Integer selectedBus = -1;
	private HashMap<Integer, BusVehicle> availableBuses;
	private Integer selectedRoute = -1;
	private HashMap<Integer, Route> routes;
	// schedule driver
	private Integer selectedDriver = -1;
	private HashMap<Integer, Driver> availableDrivers;
	private Integer selectedAssignment = -1;
	private HashMap<Integer, RouteAssignment> assignments;
	private Integer selectedShift = -1;
	private HashMap<Integer, Shift> shifts;
	// daily overview
	private DefaultTableModel overviewDtm;
	private String overviewColumnNames[] = {"Route", "Bus", "Shift", "Driver"};
	private static final int HEIGHT_OVERVIEW_TABLE = 200;
	
	/** Creates new form BtmsPage */
	public BtmsPage() {
		initComponents();
		refreshData();
	}

	/** This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// elements for error message
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		
		// elements for driver
		driverNameTextField = new JTextField();
		driverNameLabel = new JLabel();
		addDriverButton = new JButton();
		driverToggleList = new JComboBox<String>(new String[0]);
		driverToggleList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		        JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedSickDriver = cb.getSelectedIndex();
			}
		});
		driverToggleLabel = new JLabel();
		sickButton = new JButton();
		
		// elements for route
		routeNumberTextField = new JTextField();
		routeNumberLabel = new JLabel();
		addRouteButton = new JButton();
		
		// elements for bus
		busLicencePlateTextField = new JTextField();
		busLicencePlateLabel = new JLabel();
		addBusButton = new JButton();
		busToggleList = new JComboBox<String>(new String[0]);
		busToggleList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		        JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedRepairBus = cb.getSelectedIndex();
			}
		});
		busToggleLabel = new JLabel();
		repairButton = new JButton();
		
		// elements for bus assignment
		busList = new JComboBox<String>(new String[0]);
		busList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		        JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedBus = cb.getSelectedIndex();
			}
		});
		busLabel = new JLabel();
		routeList = new JComboBox<String>(new String[0]);
		routeList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		        JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedRoute = cb.getSelectedIndex();
			}
		});
		routeLabel = new JLabel();
		
		SqlDateModel model = new SqlDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		assignmentDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		assignmentDateLabel = new JLabel();

		assignButton = new JButton();
		
		// elements for schedule driver
		driverList = new JComboBox<String>(new String[0]);
		driverList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		        JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedDriver = cb.getSelectedIndex();
			}
		});
		driverLabel = new JLabel();
		assignmentList = new JComboBox<String>(new String[0]);
		assignmentList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		        JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedAssignment = cb.getSelectedIndex();
			}
		});
		assignmentLabel = new JLabel();
		shiftList = new JComboBox<String>();
		shiftList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		        JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		        selectedShift = cb.getSelectedIndex();
			}
		});
		shiftLabel = new JLabel();
		
		scheduleButton = new JButton();
		
		// elements for daily overview
		SqlDateModel overviewModel = new SqlDateModel();
		LocalDate now = LocalDate.now();
		overviewModel.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModel.setSelected(true);
		Properties pO = new Properties();
		pO.put("text.today", "Today");
		pO.put("text.month", "Month");
		pO.put("text.year", "Year");
		JDatePanelImpl overviewDatePanel = new JDatePanelImpl(overviewModel, pO);
		overviewDatePicker = new JDatePickerImpl(overviewDatePanel, new DateLabelFormatter());
		overviewDateLabel = new JLabel();
		
		overviewTable = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!c.getBackground().equals(getSelectionBackground())) {
					Object obj = getModel().getValueAt(row, column);
					if (obj instanceof java.lang.String) {
						String str = (String)obj;
						c.setBackground(str.endsWith("sick)") ? Color.RED : str.endsWith("repair)") ? Color.YELLOW : Color.WHITE);
					}
					else {
						c.setBackground(Color.WHITE);
					}
				}
				return c;
			}
		};

		overviewScrollPane = new JScrollPane(overviewTable);
		this.add(overviewScrollPane);
		Dimension d = overviewTable.getPreferredSize();
		overviewScrollPane.setPreferredSize(new Dimension(d.width, HEIGHT_OVERVIEW_TABLE));
		overviewScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		// global settings and listeners
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Bus Transportation Management System");

		driverNameLabel.setText("Name:");
		addDriverButton.setText("Add Driver");
		addDriverButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addDriverButtonActionPerformed(evt);
			}
		});
		driverToggleLabel.setText("Select Driver:");
		sickButton.setText("Toggle Sick");
		sickButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sickButtonActionPerformed(evt);
			}
		});
		
		routeNumberLabel.setText("Number:");
		addRouteButton.setText("Add Route");
		addRouteButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addRouteButtonActionPerformed(evt);
			}
		});

		busLicencePlateLabel.setText("Licence Plate:");
		addBusButton.setText("Add Bus");
		addBusButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addBusButtonActionPerformed(evt);
			}
		});
		busToggleLabel.setText("Select Bus:");
		repairButton.setText("Toggle Repair");
		repairButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				repairButtonActionPerformed(evt);
			}
		});
		
		busLabel.setText("Select Bus:");
		routeLabel.setText("Select Route:");
		assignmentDateLabel.setText("Date:");
		assignButton.setText("Assign");
		assignButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				assignButtonActionPerformed(evt);
			}
		});
		
		driverLabel.setText("Select Driver:");
		assignmentLabel.setText("Select Assignment:");
		shiftLabel.setText("Select Shift");
		scheduleButton.setText("Schedule");
		scheduleButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				scheduleButtonActionPerformed(evt);
			}
		});
		
		overviewDateLabel.setText("Date for Overview:");
		overviewDatePicker.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshData();
			}
		});
		
		// horizontal line elements
		JSeparator horizontalLineTop = new JSeparator();
		JSeparator horizontalLineMiddle1 = new JSeparator();
		JSeparator horizontalLineMiddle2 = new JSeparator();
		JSeparator horizontalLineBottom = new JSeparator();

		// layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(errorMessage)
				.addComponent(horizontalLineTop)
				.addComponent(horizontalLineMiddle1)
				.addComponent(horizontalLineMiddle2)
				.addComponent(horizontalLineBottom)
				.addComponent(overviewScrollPane)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(driverNameLabel)
								.addComponent(driverToggleLabel)
								.addComponent(busLabel)
								.addComponent(driverLabel))
						.addGroup(layout.createParallelGroup()
								.addComponent(driverNameTextField, 200, 200, 400)
								.addComponent(addDriverButton)
								.addComponent(driverToggleList)
								.addComponent(sickButton)
								.addComponent(busList)
								.addComponent(driverList))
						.addGroup(layout.createParallelGroup()
								.addComponent(routeNumberLabel)
								.addComponent(busToggleLabel)
								.addComponent(routeLabel)
								.addComponent(assignmentLabel))
						.addGroup(layout.createParallelGroup()
								.addComponent(routeNumberTextField, 200, 200, 400)
								.addComponent(addRouteButton)
								.addComponent(busToggleList)
								.addComponent(repairButton)
								.addComponent(routeList)
								.addComponent(assignmentList))
						.addGroup(layout.createParallelGroup()
								.addComponent(busLicencePlateLabel)
								.addComponent(assignmentDateLabel)
								.addComponent(shiftLabel)
								.addComponent(overviewDateLabel))
						.addGroup(layout.createParallelGroup()
								.addComponent(busLicencePlateTextField, 200, 200, 400)
								.addComponent(addBusButton)
								.addComponent(assignmentDatePicker)
								.addComponent(assignButton)
								.addComponent(shiftList)
								.addComponent(scheduleButton)
								.addComponent(overviewDatePicker)))
				);

		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {assignButton, assignmentDatePicker});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {sickButton, driverNameTextField});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {repairButton, routeNumberTextField});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addDriverButton, driverNameTextField});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addRouteButton, routeNumberTextField});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addBusButton, busLicencePlateTextField});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {scheduleButton, routeNumberTextField});
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(errorMessage)
				.addGroup(layout.createParallelGroup()
						.addComponent(driverNameLabel)
						.addComponent(driverNameTextField)
						.addComponent(routeNumberLabel)
						.addComponent(routeNumberTextField)
						.addComponent(busLicencePlateLabel)
						.addComponent(busLicencePlateTextField))		
				.addGroup(layout.createParallelGroup()
						.addComponent(addDriverButton)
						.addComponent(addRouteButton)
						.addComponent(addBusButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(horizontalLineTop))
				.addGroup(layout.createParallelGroup()
						.addComponent(driverToggleLabel)
						.addComponent(driverToggleList)
						.addComponent(busToggleLabel)
						.addComponent(busToggleList))
				.addGroup(layout.createParallelGroup()
						.addComponent(sickButton)
						.addComponent(repairButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(horizontalLineMiddle1))
				.addGroup(layout.createParallelGroup()
						.addComponent(busLabel)
						.addComponent(busList)
						.addComponent(routeLabel)
						.addComponent(routeList)
						.addComponent(assignmentDateLabel)
						.addComponent(assignmentDatePicker))
				.addComponent(assignButton)
				.addGroup(layout.createParallelGroup()
						.addComponent(horizontalLineMiddle2))
				.addGroup(layout.createParallelGroup()
						.addComponent(driverLabel)
						.addComponent(driverList)
						.addComponent(assignmentLabel)
						.addComponent(assignmentList)
						.addComponent(shiftLabel)
						.addComponent(shiftList))
				.addComponent(scheduleButton)
				.addGroup(layout.createParallelGroup()
						.addComponent(horizontalLineBottom))
				.addGroup(layout.createParallelGroup()
						.addComponent(overviewDateLabel)
						.addComponent(overviewDatePicker))
				.addComponent(overviewScrollPane)
				);
		
		pack();
	}

	private void refreshData() {
		// error
		errorMessage.setText(error);
		BtmsController bc = new BtmsController();
		if (error == null || error.length() == 0) {
			// populate page with data
			// driver
			driverNameTextField.setText("");
			// route
			routeNumberTextField.setText("");
			// bus
			busLicencePlateTextField.setText("");
			
			// toggle sick status
			drivers = new HashMap<Integer, Driver>();
			driverToggleList.removeAllItems();
			Integer index = 0;
			for (Driver driver : bc.getDrivers()) {
				drivers.put(index, driver);
				driverToggleList.addItem("#" + driver.getId() + " " + driver.getName());
				index++;
			};
			selectedSickDriver = -1;
			driverToggleList.setSelectedIndex(selectedSickDriver);

			// toggle repair status
			buses = new HashMap<Integer, BusVehicle>();
			busToggleList.removeAllItems();
			index = 0;
			for (BusVehicle bus : bc.getBuses()) {
				buses.put(index, bus);
				busToggleList.addItem(bus.getLicencePlate());
				index++;
			};
			selectedRepairBus = -1;
			busToggleList.setSelectedIndex(selectedRepairBus);
		
			// bus assignment - bus
			availableBuses = new HashMap<Integer, BusVehicle>();
			busList.removeAllItems();
			index = 0;
			for (BusVehicle bus : bc.getAvailableBuses()) {
				availableBuses.put(index, bus);
				busList.addItem(bus.getLicencePlate());
				index++;
			};
			selectedBus = -1;
			busList.setSelectedIndex(selectedBus);
			// bus assignment - route
			routes = new HashMap<Integer, Route>();
			routeList.removeAllItems();
			index = 0;
			for (Route route : bc.getRoutes()) {
				routes.put(index, route);
				routeList.addItem("#" + route.getNumber());
				index++;
			};
			selectedRoute = -1;
			routeList.setSelectedIndex(selectedRoute);
			// bus assignment - date
			assignmentDatePicker.getModel().setValue(null);
			
			// schedule driver - driver
			availableDrivers = new HashMap<Integer, Driver>();
			driverList.removeAllItems();
			index = 0;
			for (Driver driver : bc.getAvailableDrivers()) {
				availableDrivers.put(index, driver);
				driverList.addItem("#" + driver.getId() + " " + driver.getName());
				index++;
			};
			selectedDriver = -1;
			driverList.setSelectedIndex(selectedDriver);
			// schedule driver - assignment
			assignments = new HashMap<Integer, RouteAssignment>();
			assignmentList.removeAllItems();
			index = 0;
			for (RouteAssignment assignment : bc.getAssignments()) {
				assignments.put(index, assignment);
				assignmentList.addItem(assignment.getDate() + ": Route #" + assignment.getRoute().getNumber() + ", Bus " + assignment.getBus().getLicencePlate());
				index++;
			};
			selectedAssignment = -1;
			assignmentList.setSelectedIndex(selectedAssignment);
			// schedule driver - shift
			shifts = new HashMap<Integer, Shift>();
			shiftList.removeAllItems();
			index = 0;
			for (Shift shift : Shift.values()) {
				shifts.put(index, shift);
				shiftList.addItem(shift.toString());
				index++;
			};
			selectedShift = 0;
			shiftList.setSelectedIndex(selectedShift);
		}

		// daily overview
		overviewDtm = new DefaultTableModel(0, 0);
		overviewDtm.setColumnIdentifiers(overviewColumnNames);
		overviewTable.setModel(overviewDtm);
		if (overviewDatePicker.getModel().getValue() != null) {
			for (RouteAssignment assignment : bc.getAssignmentsForDate((Date) overviewDatePicker.getModel().getValue())) {
				BusVehicle bus = assignment.getBus();
				String busText = bus.getLicencePlate();
				if (bus.isInRepairShop()) {
					busText = busText + " (in repair)";
				}
				if (assignment.getDriverSchedules().size() == 0) {
					Object[] obj = {assignment.getRoute().getNumber(), busText, "---", "---"};
					overviewDtm.addRow(obj);
				}
				else {
					for (DriverSchedule schedule : assignment.getDriverSchedules()) {
						Driver driver = schedule.getDriver();
						String driverText = "#" + driver.getId() + " " + driver.getName();
						if (driver.isOnSickLeave()) {
							driverText = driverText + " (sick)";
						}
						Object[] obj = {assignment.getRoute().getNumber(), busText, schedule.getShiftFullName(), driverText};
						overviewDtm.addRow(obj);
					}
				}
			}
		}
		Dimension d = overviewTable.getPreferredSize();
		overviewScrollPane.setPreferredSize(new Dimension(d.width, HEIGHT_OVERVIEW_TABLE));
		
		// this is needed because the size of the window changes depending on whether an error message is shown or not
		pack();
	}

	private void addDriverButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message
		error = null;
		
		// call the controller
		BtmsController bc = new BtmsController();
		try {
			bc.createDriver(driverNameTextField.getText());
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// update visuals
		refreshData();
	}
	
	private void sickButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = "";
		if (selectedSickDriver < 0)
			error = "Driver needs to be selected to toggle sick status!";
		
		if (error.length() == 0) {
			// call the controller
			BtmsController bc = new BtmsController();
			try {
				bc.toggleSickStatus(drivers.get(selectedSickDriver));
			} catch (InvalidInputException e) {
				error = e.getMessage();
			}
		}
		
		// update visuals
		refreshData();
	}

	private void addRouteButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = "";
		int routeNumber = 0;
		try {
			routeNumber = Integer.parseInt(routeNumberTextField.getText());
		}
		catch (NumberFormatException e) {
			error = "Route number needs to be a numerical value!";
		}

		if (error.length() == 0) {
			// call the controller
			BtmsController bc = new BtmsController();
			try {
				bc.createRoute(routeNumber);
			} catch (InvalidInputException e) {
				error = e.getMessage();
			}
		}

		// update visuals
		refreshData();
	}
	
	private void addBusButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = null;

		// call the controller
		BtmsController bc = new BtmsController();
		try {
			bc.createBus(busLicencePlateTextField.getText());
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// update visuals
		refreshData();
	}
	
	private void repairButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = "";
		if (selectedRepairBus < 0)
			error = "Bus needs to be selected to toggle repair status!";

		if (error.length() == 0) {
			// call the controller
			BtmsController bc = new BtmsController();
			try {
				bc.toggleRepairStatus(buses.get(selectedRepairBus));
			} catch (InvalidInputException e) {
				error = e.getMessage();
			}
		}
		
		// update visuals
		refreshData();
	}

	private void assignButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = "";
		if (selectedBus < 0)
			error = "Bus needs to be selected for assignment! ";
		if (selectedRoute < 0)
			error = error + "Route needs to be selected for assignment! ";
		if (assignmentDatePicker.getModel().getValue() == null)
			error = error + "Date needs to entered for assignment!";
		error = error.trim();

		if (error.length() == 0) {
			// call the controller
			BtmsController bc = new BtmsController();
			try {
				bc.assign(availableBuses.get(selectedBus), routes.get(selectedRoute), (Date) assignmentDatePicker.getModel().getValue());
			} catch (InvalidInputException e) {
				error = e.getMessage();
			}
		}

		// update visuals
		refreshData();			
	}
	
	private void scheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = "";
		if (selectedDriver < 0)
			error = "Driver needs to be selected for schedule! ";
		if (selectedAssignment < 0)
			error = error + "Assignment needs to be selected for schedule! ";
		if (selectedShift < 0)
			error = error + "Shift needs to be selected for schedule! ";
		error = error.trim();

		if (error.length() == 0) {
			// call the controller
			BtmsController bc = new BtmsController();
			try {
				bc.schedule(availableDrivers.get(selectedDriver), assignments.get(selectedAssignment), shifts.get(selectedShift));
			} catch (InvalidInputException e) {
				error = e.getMessage();
			}
		}

		// update visuals
		refreshData();			
	}

}
