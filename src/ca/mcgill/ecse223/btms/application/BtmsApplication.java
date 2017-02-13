package ca.mcgill.ecse223.btms.application;

import ca.mcgill.ecse223.btms.model.BTMS;
import ca.mcgill.ecse223.btms.model.BusVehicle;
import ca.mcgill.ecse223.btms.model.Driver;
import ca.mcgill.ecse223.btms.model.Route;
import ca.mcgill.ecse223.btms.persistence.PersistenceObjectStream;
import ca.mcgill.ecse223.btms.view.BtmsPage;

public class BtmsApplication {
	
	private static BTMS btms;
	private static String filename = "data.btms";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// start UI
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BtmsPage().setVisible(true);
            }
        });
        
	}

	public static BTMS getBtms() {
		if (btms == null) {
			// load model
			btms = load();
		}
 		return btms;
	}
	
	public static void save() {
		PersistenceObjectStream.serialize(btms);
	}
	
	public static BTMS load() {
		PersistenceObjectStream.setFilename(filename);
		btms = (BTMS) PersistenceObjectStream.deserialize();
		// model cannot be loaded - create empty BTMS
		if (btms == null) {
			btms = new BTMS();
		}
		else {
			Driver.reinitializeAutouniqueID(btms.getDrivers());
			BusVehicle.reinitializeUniqueLicencePlate(btms.getVehicles());
			Route.reinitializeUniqueNumber(btms.getRoutes());
		}
		return btms;
	}
	
	public static void setFilename(String newFilename) {
		filename = newFilename;
	}
	
}
