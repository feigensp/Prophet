/*
 * Created on Sep 28, 2004
 */
package ubc.midp.mobilephoto.core.ui.datamodel;

import java.util.Hashtable;

import javax.microedition.lcdui.Image;

import lancs.midp.mobilephoto.lib.exceptions.ImageNotFoundException;
import lancs.midp.mobilephoto.lib.exceptions.InvalidImageDataException;
import lancs.midp.mobilephoto.lib.exceptions.InvalidPhotoAlbumNameException;
import lancs.midp.mobilephoto.lib.exceptions.NullAlbumDataReference;
import lancs.midp.mobilephoto.lib.exceptions.PersistenceMechanismException;
import lancs.midp.mobilephoto.lib.exceptions.UnavailablePhotoAlbumException;

/**
 * @author tyoung
 * 
 * This class represents the data model for Photo Albums. A Photo Album object
 * is essentially a list of photos or images, stored in a Hashtable. Due to
 * constraints of the J2ME RecordStore implementation, the class stores a table
 * of the images, indexed by an identifier, and a second table of image metadata
 * (ie. labels, album name etc.)
 * 
 * This uses the ImageAccessor class to retrieve the image data from the
 * recordstore (and eventually file system etc.)
 */
public class AlbumData {

	private ImageAccessor imageAccessor;

	//imageInfo holds image metadata like label, album name and 'foreign key' index to
	// corresponding RMS entry that stores the actual Image object
	protected Hashtable imageInfoTable = new Hashtable();

	public boolean existingRecords = false; //If no records exist, try to reset

	/**
	 *  Constructor. Creates a new instance of ImageAccessor
	 */
	public AlbumData() {
		imageAccessor = new ImageAccessor(this);
	}

	/**
	 *  Load any photo albums that are currently defined in the record store
	 */
	public String[] getAlbumNames() {

		//Shouldn't load all the albums each time
		//Add a check somewhere in ImageAccessor to see if they've been
		//loaded into memory already, and avoid the extra work...
		try {
			imageAccessor.loadAlbums();
		} catch (InvalidImageDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceMechanismException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageAccessor.getAlbumNames();
	}

	/**
	 *  Get all images for a given Photo Album that exist in the Record Store.
	 * @throws UnavailablePhotoAlbumException 
	 * @throws InvalidImageDataException 
	 * @throws PersistenceMechanismException 
	 */
	public ImageData[] getImages(String recordName) throws UnavailablePhotoAlbumException  {

		ImageData[] result;
		try {
			result = imageAccessor.loadImageDataFromRMS(recordName);
		} catch (PersistenceMechanismException e) {
			throw new UnavailablePhotoAlbumException(e);
			
		} catch (InvalidImageDataException e) {
			throw new UnavailablePhotoAlbumException(e);
		}

		return result;

	}

	/**
	 *  Define a new user photo album. This results in the creation of a new
	 *  RMS Record store.
	 * @throws PersistenceMechanismException 
	 * @throws InvalidPhotoAlbumNameException 
	 */
	public void createNewPhotoAlbum(String albumName) throws PersistenceMechanismException, InvalidPhotoAlbumNameException {
		imageAccessor.createNewPhotoAlbum(albumName);
	}
	
	public void deletePhotoAlbum(String albumName) throws PersistenceMechanismException{
		imageAccessor.deletePhotoAlbum(albumName);
	}

	/**
	 *  Get a particular image (by name) from a photo album. The album name corresponds
	 *  to a record store.
	 * @throws ImageNotFoundException 
	 * @throws PersistenceMechanismException 
	 */
	public Image getImageFromRecordStore(String recordStore, String imageName) throws ImageNotFoundException, PersistenceMechanismException {

		ImageData imageInfo = null;
		try {
			imageInfo = imageAccessor.getImageInfo(imageName);
		} catch (NullAlbumDataReference e) {
			imageAccessor = new ImageAccessor(this);
		}
		//Find the record ID and store name of the image to retrieve
		int imageId = imageInfo.getForeignRecordId();
		String album = imageInfo.getParentAlbumName();
		//Now, load the image (on demand) from RMS and cache it in the hashtable
		Image imageRec = imageAccessor.loadSingleImageFromRMS(album, imageName, imageId); //rs.getRecord(recordId);
		return imageRec;

	}
	public void addNewPhotoToAlbum(String label, String path, String album) throws InvalidImageDataException, PersistenceMechanismException{
		imageAccessor.addImageData(label, path, album);
	}

	/**
	 *  Delete a photo from the photo album. This permanently deletes the image from the record store
	 * @throws ImageNotFoundException 
	 * @throws PersistenceMechanismException 
	 */
	public void deleteImage(String imageName, String storeName) throws PersistenceMechanismException, ImageNotFoundException {
		try {
			imageAccessor.deleteSingleImageFromRMS(imageName, storeName);
		}
		catch (NullAlbumDataReference e) {
			imageAccessor = new ImageAccessor(this);
			e.printStackTrace();
		} 
	}
	
	/**
	 *  Reset the image data for the application. This is a wrapper to the ImageAccessor.resetImageRecordStore
	 *  method. It is mainly used for testing purposes, to reset device data to the default album and photos.
	 * @throws PersistenceMechanismException 
	 * @throws InvalidImageDataException 
	 */
	public void resetImageData() throws PersistenceMechanismException {
		try {
			imageAccessor.resetImageRecordStore();
		} catch (InvalidImageDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the hashtable that stores the image metadata in memory.
	 * @return Returns the imageInfoTable.
	 */
	public Hashtable getImageInfoTable() {
		return imageInfoTable;
	}

	/**
	 * Update the hashtable that stores the image metadata in memory
	 * @param imageInfoTable
	 *            The imageInfoTable to set.
	 */
	public void setImageInfoTable(Hashtable imageInfoTable) {
		this.imageInfoTable = imageInfoTable;
	}

	/**
	 * [EF] Added in order to have access to ImageData
	 * @param imageAccessor
	 */
	public void setImageAccessor(ImageAccessor imageAccessor) {
		this.imageAccessor = imageAccessor;
	}

	/**
	 * [EF] Added in order to have access to ImageData
	 * @return
	 */
	public ImageAccessor getImageAccessor() {
		return imageAccessor;
	}
}
