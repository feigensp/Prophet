/*
 * Created on Sep 28, 2004
 *
 */
package ubc.midp.mobilephoto.core.ui.controller;

import javax.microedition.lcdui.Command;

import ubc.midp.mobilephoto.core.ui.MainUIMidlet;
import ubc.midp.mobilephoto.core.ui.datamodel.AlbumData;
import ubc.midp.mobilephoto.core.ui.screens.AlbumListScreen;
import ubc.midp.mobilephoto.core.util.Constants;

/**
 * @author tyoung
 *
 * This is the base controller class used in the MVC architecture.
 * It controls the flow of screens for the MobilePhoto application.
 * Commands handled by this class should only be for the core application
 * that runs on any MIDP platform. Each device or class of devices that supports
 * optional features will extend this class to handle feature specific commands.
 * 
 */
public class BaseController extends AbstractController {
    
	// [EF] Attributes albumController and photoController were commented because 
	// I'm not sure which one is the best solution: 
	// [EF] (i) Declare controllers here and have only one instance or
	// [EF] (ii) create controllerns when needed (current solution)
//	private AlbumController albumController;
//	private PhotoController photoController;
	
	/**
	 * Pass a handle to the main Midlet for this controller
	 * @param midlet
	 */
	public BaseController(MainUIMidlet midlet, AlbumData model, AlbumListScreen albumListScreen) {
		super(midlet, model, albumListScreen);
	}

	/**
	 * Initialize the controller
	 */
	public void init(AlbumData model) {
		//Get all MobilePhoto defined albums from the record store
		String[] albumNames = model.getAlbumNames();
		for (int i = 0; i < albumNames.length; i++) {
			if (albumNames[i] != null) {
				//Add album name to menu list
				getAlbumListScreen().append(albumNames[i], null);
				
			}
		}
		getAlbumListScreen().initMenu();
		
		//Set the current screen to the photo album listing
		setCurrentScreen(getAlbumListScreen());
	}

    /* 
     * TODO [EF] Why this method receives Displayable and never uses?
     */
    public boolean handleCommand(Command command) {
		String label = command.getLabel();

		//Can this controller handle the desired action?
		//If yes, handleCommand will return true, and we're done
		//If no, handleCommand will return false, and postCommand
		//will pass the request to the next controller in the chain if one exists.
		
      	System.out.println( this.getClass().getName() + "::handleCommand: " + label);

		/** Case: Exit Application **/
		if (label.equals("Exit")) {
			midlet.destroyApp(true);
			return true;
		
		/** Case: Go to the Previous or Fallback screen * */
		} else if (label.equals("Back")) {
			return goToPreviousScreen();

			/** Case: Cancel the current screen and go back one* */
		} else if (label.equals("Cancel")) {
			return goToPreviousScreen();

		}

		//If we couldn't handle the current command, return false
        return false;
    }
    
    private boolean goToPreviousScreen() {
	    System.out.println("<* AlbumController.goToPreviousScreen() *>");
		String currentScreenName = ScreenSingleton.getInstance().getCurrentScreenName();
		if ( (currentScreenName.equals(Constants.IMAGELIST_SCREEN)) || 
				(currentScreenName.equals(Constants.NEWALBUM_SCREEN)) ||
				(currentScreenName.equals(Constants.CONFIRMDELETEALBUM_SCREEN)) ){
			getAlbumListScreen().repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen( getAlbumListScreen() );
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
    	
		return false;
    }
}
