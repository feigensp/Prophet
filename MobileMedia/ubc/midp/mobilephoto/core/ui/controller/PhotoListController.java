/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 11 Aug 2007
 * 
 */
package ubc.midp.mobilephoto.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

import lancs.midp.mobilephoto.lib.exceptions.UnavailablePhotoAlbumException;
import ubc.midp.mobilephoto.core.ui.MainUIMidlet;
import ubc.midp.mobilephoto.core.ui.datamodel.AlbumData;
import ubc.midp.mobilephoto.core.ui.datamodel.ImageData;
import ubc.midp.mobilephoto.core.ui.screens.AlbumListScreen;
import ubc.midp.mobilephoto.core.ui.screens.PhotoListScreen;
import ubc.midp.mobilephoto.core.util.Constants;

/**
 * @author Eduardo Figueiredo
 *
 */
public class PhotoListController extends AbstractController {

	/**
	 * @param midlet
	 * @param nextController
	 * @param albumData
	 * @param albumListScreen
	 */
	public PhotoListController(MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen) {
		super(midlet, albumData, albumListScreen);
	}

	/* (non-Javadoc)
	 * @see ubc.midp.mobilephoto.core.ui.controller.ControllerInterface#handleCommand(java.lang.String)
	 */
	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		/** Case: Select PhotoAlbum to view **/
		if (label.equals("Select")) {
			// Get the name of the PhotoAlbum selected, and load image list from
			// RecordStore
			List down = (List) Display.getDisplay(midlet).getCurrent();
			ScreenSingleton.getInstance().setCurrentStoreName(down.getString(down.getSelectedIndex()));
			showImageList(getCurrentStoreName(), false, false);
			ScreenSingleton.getInstance().setCurrentScreenName( Constants.IMAGELIST_SCREEN);
			return true;
		}
		
		return false;
	}

    /**
     * Show the list of images in the record store
	 * TODO: Refactor - Move this to ImageList class
	 */
	public void showImageList(String recordName, boolean sort, boolean favorite) {

		if (recordName == null)
			recordName = getCurrentStoreName();
		
		PhotoController photoController = new PhotoController(midlet, getAlbumData(), getAlbumListScreen());
		photoController.setNextController(this);
		
		PhotoListScreen imageList = new PhotoListScreen();
		imageList.setCommandListener(photoController);
		
		//Command selectCommand = new Command("Open", Command.ITEM, 1);
		imageList.initMenu();
		
		ImageData[] images = null;
		try {
			images = getAlbumData().getImages(recordName);
		} catch (UnavailablePhotoAlbumException e) {
			Alert alert = new Alert( "Error", "The list of photos can not be recovered", null, AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
	        return;
	    }
		
		if (images==null) return;
		
		// #ifdef includeSorting
//# 		// [EF] Check if sort is true (Add in the Scenario 02)
//# 		if (sort) {
//# 			bubbleSort(images);
//# 		}
		// #endif
		
		//loop through array and add labels to list
		for (int i = 0; i < images.length; i++) {
			if (images[i] != null) {
				//Add album name to menu list
				
				// #ifdef includeFavourites
//# 				// [EF] Check if favorite is true (Add in the Scenario 03)
//# 				if (favorite) {
//# 					if (images[i].isFavorite())
//# 						imageList.append(images[i].getImageLabel(), null);
//# 				}
//# 				else 
				// #endif
					imageList.append(images[i].getImageLabel(), null);
				
			}
		}
		setCurrentScreen(imageList);
		//currentMenu = "list";
	}
	
	// #ifdef includeSorting
//# 	/**
//# 	 * @param images
//# 	 * @param pos1
//# 	 * @param pos2
//# 	 */
//# 	private void exchange(ImageData[] images, int pos1, int pos2) {
//# 		ImageData tmp = images[pos1];
//# 		images[pos1] = images[pos2];
//# 		images[pos2] = tmp;
//# 	}
//# 
//#     /**
//#      * Sorts an int array using basic bubble sort
//#      * 
//#      * @param numbers the int array to sort
//#      */
//# 	public void bubbleSort(ImageData[] images) {
//# 		System.out.print("Sorting by BubbleSort...");		
//# 		for (int end = images.length; end > 1; end --) {
//# 			for (int current = 0; current < end - 1; current ++) {
//# 				if (images[current].getNumberOfViews() > images[current+1].getNumberOfViews()) {
//# 					exchange(images, current, current+1);
//# 				}
//# 			}
//# 		}
//# 		System.out.println("done.");
//# 	}
	// #endif
}
