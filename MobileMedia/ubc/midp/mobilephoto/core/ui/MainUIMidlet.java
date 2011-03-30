package ubc.midp.mobilephoto.core.ui;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import ubc.midp.mobilephoto.core.ui.controller.AlbumController;
import ubc.midp.mobilephoto.core.ui.controller.BaseController;
import ubc.midp.mobilephoto.core.ui.controller.PhotoListController;
import ubc.midp.mobilephoto.core.ui.datamodel.AlbumData;
import ubc.midp.mobilephoto.core.ui.screens.AlbumListScreen;

//Following are pre-processor statements to include the required
//classes for device specific features. They must be commented out
//if they aren't used, otherwise it will throw exceptions trying to
//load classes that aren't available for a given platform.


/* 
 * @author trevor
 *
 * This is the main Midlet class for the core J2ME application
 * It contains all the basic functionality that should be executable
 * in any standard J2ME device that supports MIDP 1.0 or higher. 
 * Any additional J2ME features for this application that are dependent
 * upon a particular device (ie. optional or proprietary library) are
 * de-coupled from the core application so they can be conditionally included
 * depending on the target platform 
 * 
 * This Application provides a basic Photo Album interface that allows a user to view
 * images on their mobile device. 
 * */
public class MainUIMidlet extends MIDlet {

	//(m v C) Controller 
	private BaseController rootController;

	//Model (M v c)
	private AlbumData model;

	/**
	 * Constructor -
	 */
	public MainUIMidlet() {
	    //do nothing
	}

	/**
	 * Start the MIDlet by creating new model and controller classes, and
	 * initialize them as necessary
	 */
	public void startApp() throws MIDletStateChangeException {
		model = new AlbumData();
		AlbumListScreen album = new AlbumListScreen();
		rootController = new BaseController(this, model, album);
		
		// [EF] Add in scenario 04: initialize sub-controllers
		PhotoListController photoListController = new PhotoListController(this, model, album);
		photoListController.setNextController(rootController);
		
		AlbumController albumController = new AlbumController(this, model, album);
		albumController.setNextController(photoListController);
		album.setCommandListener(albumController);
		
		//Only the first (last?) controller needs to be initialized (?)
		rootController.init(model);
	}

	/**
	 * Pause the MIDlet
	 * This method does nothing at the moment.
	 */
	public void pauseApp() {
		//do nothing
	}

	/**
	 * Destroy the MIDlet
	 */
	public void destroyApp(boolean unconditional) {
		notifyDestroyed();
	}
}
