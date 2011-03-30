/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 17 Jun 2007
 * 
 */
package ubc.midp.mobilephoto.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStoreFullException;

import lancs.midp.mobilephoto.lib.exceptions.ImageNotFoundException;
import lancs.midp.mobilephoto.lib.exceptions.ImagePathNotValidException;
import lancs.midp.mobilephoto.lib.exceptions.InvalidImageDataException;
import lancs.midp.mobilephoto.lib.exceptions.NullAlbumDataReference;
import lancs.midp.mobilephoto.lib.exceptions.PersistenceMechanismException;
import ubc.midp.mobilephoto.core.ui.MainUIMidlet;
import ubc.midp.mobilephoto.core.ui.datamodel.AlbumData;
import ubc.midp.mobilephoto.core.ui.datamodel.ImageData;
import ubc.midp.mobilephoto.core.ui.screens.AddPhotoToAlbum;
import ubc.midp.mobilephoto.core.ui.screens.AlbumListScreen;
import ubc.midp.mobilephoto.core.ui.screens.NewLabelScreen;
import ubc.midp.mobilephoto.core.ui.screens.PhotoViewScreen;
import ubc.midp.mobilephoto.core.util.Constants;

/**
 * @author Eduardo Figueiredo
 * Added in the Scenario 02
 */
public class PhotoController extends PhotoListController {

	private ImageData image;
	private NewLabelScreen screen;

	public PhotoController (MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen) {
		super(midlet, albumData, albumListScreen);
	}

	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		System.out.println( "<* PhotoController.handleCommand() *> " + label);

		if (label.equals("View")) {
			String selectedImageName = getSelectedImageName();
			showImage(selectedImageName);
			
			// #ifdef includeSorting
//# 			// [EF] Added in the scenario 02
//# 			try {
//# 				ImageData image = getAlbumData().getImageAccessor().getImageInfo(selectedImageName);
//# 				image.increaseNumberOfViews();
//# 				updateImage(image);
//# 				System.out.println("<* BaseController.handleCommand() *> Image = " + selectedImageName + "; # views = " + image.getNumberOfViews());
//# 			} catch (ImageNotFoundException e) {
//# 				Alert alert = new Alert("Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
//# 				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
//# 			} catch (NullAlbumDataReference e) {
//# 				this.setAlbumData( new AlbumData() );
//# 				Alert alert = new Alert( "Error", "The operation is not available. Try again later !", null, AlertType.ERROR);
//# 				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
//# 			}
			// #endif
			
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGE_SCREEN);
			return true;

			/** Case: Add photo * */
		} else if (label.equals("Add")) {
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ADDPHOTOTOALBUM_SCREEN);
			AddPhotoToAlbum form = new AddPhotoToAlbum("Add new Photo to Album");
			form.setCommandListener(this);
			setCurrentScreen(form);
			return true;
			/** Case: Save Add photo * */
		} else if (label.equals("Save Photo")) {
			try {
				getAlbumData().addNewPhotoToAlbum(((AddPhotoToAlbum) getCurrentScreen()).getPhotoName(), 
						((AddPhotoToAlbum) getCurrentScreen()).getPath(), getCurrentStoreName());
			} catch (InvalidImageDataException e) {
				Alert alert = null;
				if (e instanceof ImagePathNotValidException)
					alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
				else
					alert = new Alert("Error", "The image file format is not valid", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
				return true;
				// alert.setTimeout(5000);
			} catch (PersistenceMechanismException e) {
				Alert alert = null;
				if (e.getCause() instanceof RecordStoreFullException)
					alert = new Alert("Error", "The mobile database is full", null, AlertType.ERROR);
				else
					alert = new Alert("Error", "The mobile database can not add a new photo", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
			}
			return goToPreviousScreen();
			/** Case: Delete selected Photo from recordstore * */
		} else if (label.equals("Delete")) {
			String selectedImageName = getSelectedImageName();
			try {
				getAlbumData().deleteImage(getCurrentStoreName(), selectedImageName);
			} catch (PersistenceMechanismException e) {
				Alert alert = new Alert("Error", "The mobile database can not delete this photo", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
				return true;
			} catch (ImageNotFoundException e) {
				Alert alert = new Alert("Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
				return true;
			}
			showImageList(getCurrentStoreName(), false, false);
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
			return true;

		/** Case: Edit photo label
		 *  [EF] Added in the scenario 02 */
		} else if (label.equals("Edit Label")) {
			String selectedImageName = getSelectedImageName();
			try {
				image = getAlbumData().getImageAccessor().getImageInfo(
						selectedImageName);
				// PhotoController photoController = new PhotoController(image,
				// this);
				NewLabelScreen formScreen = new NewLabelScreen(
						"Edit Label Photo", NewLabelScreen.LABEL_PHOTO);
				formScreen.setCommandListener(this);
				this.setScreen(formScreen);
				setCurrentScreen(formScreen);
				formScreen = null;
			} catch (ImageNotFoundException e) {
				Alert alert = new Alert(
						"Error",
						"The selected photo was not found in the mobile device",
						null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,
						Display.getDisplay(midlet).getCurrent());
			} catch (NullAlbumDataReference e) {
				this.setAlbumData( new AlbumData() );
				Alert alert = new Alert( "Error", "The operation is not available. Try again later !", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
			}
			return true;

		// #ifdef includeSorting
//# 		/**
//# 		 * Case: Sort photos by number of views 
//# 		 * [EF] Added in the scenario 02 */
//# 		} else if (label.equals("Sort by Views")) {
//# 			showImageList(getCurrentStoreName(), true, false);
//# 			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
//# 
//# 			return true;
		// #endif

		// #ifdef includeFavourites
//# 		/**
//# 		 * Case: Set photo as favorite
//# 		 * [EF] Added in the scenario 03 */
//# 		} else if (label.equals("Set Favorite")) {
//# 			String selectedImageName = getSelectedImageName();
//# 			try {
//# 				ImageData image = getAlbumData().getImageAccessor().getImageInfo(selectedImageName);
//# 				image.toggleFavorite();
//# 				updateImage(image);
//# 				System.out.println("<* BaseController.handleCommand() *> Image = "+ selectedImageName + "; Favorite = " + image.isFavorite());
//# 			} catch (ImageNotFoundException e) {
//# 				Alert alert = new Alert("Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
//# 				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
//# 			} catch (NullAlbumDataReference e) {
//# 				this.setAlbumData(new AlbumData());
//# 				Alert alert = new Alert( "Error", "The operation is not available. Try again later !", null, AlertType.ERROR);
//# 				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
//# 			}
//# 			return true;
//# 
//# 			/**
//# 			 * Case: View favorite photos 
//# 			 * [EF] Added in the scenario 03 */
//# 		} else if (label.equals("View Favorites")) {
//# 			showImageList(getCurrentStoreName(), false, true);
//# 			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
//# 
//# 			return true;
		// #endif
			
			/** Case: Save new Photo Label */
		} else if (label.equals("Save")) {
				System.out
						.println("<* PhotoController.handleCommand() *> Save Photo Label = "
								+ this.screen.getLabelName());
				this.getImage().setImageLabel(this.screen.getLabelName());
				updateImage(image);
				return goToPreviousScreen();
		
		/** Case: Go to the Previous or Fallback screen * */
		} else if (label.equals("Back")) {
			return goToPreviousScreen();

			/** Case: Cancel the current screen and go back one* */
		} else if (label.equals("Cancel")) {
			return goToPreviousScreen();

		}

		return false;
	}

	void updateImage(ImageData image) {
		try {
			getAlbumData().getImageAccessor().updateImageInfo(image, image);
		} catch (InvalidImageDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceMechanismException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     * Get the last selected image from the Photo List screen.
	 * TODO: This really only gets the selected List item. 
	 * So it is only an image name if you are on the PhotoList screen...
	 * Need to fix this
	 */
	public String getSelectedImageName() {
		List selected = (List) Display.getDisplay(midlet).getCurrent();
		if (selected == null)
		    System.out.println("Current List from display is NULL!");
		String name = selected.getString(selected.getSelectedIndex());
		return name;
	}
	
    /**
     * Show the current image that is selected
	 */
	public void showImage(String name) {
// [EF] Instead of replicating this code, I change to use the method "getSelectedImageName()". 		
		Image storedImage = null;
		try {
			storedImage = getAlbumData().getImageFromRecordStore(getCurrentStoreName(), name);
		} catch (ImageNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
	        return;
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "The mobile database can open this photo", null, AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
	        return;
		}		
		
		//We can pass in the image directly here, or just the name/model pair and have it loaded
		PhotoViewScreen canv = new PhotoViewScreen(storedImage);
		canv.setCommandListener( this );
		
		// #ifdef includeCopyPhoto
//# 		PhotoViewController controller = new PhotoViewController(midlet, getAlbumData(), getAlbumListScreen(), name);
//# 		controller.setNextController(this);
//# 		canv.setCommandListener(controller);
		// #endif
		
		setCurrentScreen(canv);
	}

   /**
    * TODO [EF] update this method or merge with method of super class.
     * Go to the previous screen
	 */
    private boolean goToPreviousScreen() {
	    System.out.println("<* PhotoController.goToPreviousScreen() *>");
		String currentScreenName = ScreenSingleton.getInstance().getCurrentScreenName();
	    if (currentScreenName.equals(Constants.ALBUMLIST_SCREEN)) {
		    System.out.println("Can't go back here...Should never reach this spot");
		} else if (currentScreenName.equals(Constants.IMAGE_SCREEN)) {		    
		    //Go to the image list here, not the main screen...
		    showImageList(getCurrentStoreName(), false, false);
		    ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
		    return true;
		}
    	else if (currentScreenName.equals(Constants.ADDPHOTOTOALBUM_SCREEN)) {
    		showImageList(getCurrentStoreName(), false, false);
		    ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
		    return true;
    	}
	    return false;
    } 

	/**
	 * @param image the image to set
	 */
	public void setImage(ImageData image) {
		this.image = image;
	}

	/**
	 * @return the image
	 */
	public ImageData getImage() {
		return image;
	}

	public void setScreen(NewLabelScreen screen) {
		this.screen = screen;
	}

	public NewLabelScreen getScreen() {
		return screen;
	}

}
