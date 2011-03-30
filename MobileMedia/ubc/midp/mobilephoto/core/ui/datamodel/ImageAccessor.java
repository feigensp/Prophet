/*
 * Created on Sep 13, 2004
 *
 */
package ubc.midp.mobilephoto.core.ui.datamodel;

import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import lancs.midp.mobilephoto.lib.exceptions.ImageNotFoundException;
import lancs.midp.mobilephoto.lib.exceptions.ImagePathNotValidException;
import lancs.midp.mobilephoto.lib.exceptions.InvalidImageDataException;
import lancs.midp.mobilephoto.lib.exceptions.InvalidImageFormatException;
import lancs.midp.mobilephoto.lib.exceptions.InvalidPhotoAlbumNameException;
import lancs.midp.mobilephoto.lib.exceptions.NullAlbumDataReference;
import lancs.midp.mobilephoto.lib.exceptions.PersistenceMechanismException;
import ubc.midp.mobilephoto.core.util.ImageUtil;

/**
 * @author trevor
 * 
 * This is the main data access class. It handles all the connectivity with the
 * RMS record stores to fetch and save data associated with MobilePhoto TODO:
 * Refactor into stable interface for future updates. We may want to access data
 * from RMS, or eventually direct from the 'file system' on devices that support
 * the FileConnection optional API.
 * 
 */
public class ImageAccessor {

	// Note: Our midlet only ever has access to Record Stores it created
	// For now, use naming convention to create record stores used by
	// MobilePhoto
	public static final String ALBUM_LABEL = "mpa-"; // "mpa- all album names
														// are prefixed with
														// this label
	public static final String INFO_LABEL = "mpi-"; // "mpi- all album info
													// stores are prefixed with
													// this label
	public static final String DEFAULT_ALBUM_NAME = "My Photo Album"; // default
																		// album
																		// name

	public static final String IMAGE_LABEL = "ImageList"; // RecordStore name
															// prefixed

	protected String[] albumNames; // User defined names of photo albums

	protected AlbumData model;

	// Record Stores
	private RecordStore imageRS = null;
	private RecordStore imageInfoRS = null;

	/*
	 * Constructor
	 */
	public ImageAccessor(AlbumData mod) {
		model = mod;
	}

	/**
	 * Load all existing photo albums that are defined in the record store.
	 * 

	 * @throws InvalidImageDataException
	 * @throws PersistenceMechanismException
	 */
	public void loadAlbums() throws InvalidImageDataException,
			PersistenceMechanismException {

		// Try to find any existing Albums (record stores)

		String[] currentStores = RecordStore.listRecordStores();

		if (currentStores != null) {
			System.out.println("ImageAccessor::loadAlbums: Found: " + currentStores.length + " existing record stores");
			model.existingRecords = true;
			String[] temp = new String[currentStores.length];
			int count = 0;

			// Only use record stores that follow the naming convention defined
			for (int i = 0; i < currentStores.length; i++) {
				String curr = currentStores[i];

				// If this record store is a photo album...
				if (curr.startsWith(ALBUM_LABEL)) {

					// Strip out the mpa- identifier
					curr = curr.substring(4);
					// Add the album name to the array
					temp[i] = curr;
					count++;
				}
			}

			// Re-copy the contents into a smaller array now that we know the
			// size
			albumNames = new String[count];
			int count2 = 0;
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != null) {
					albumNames[count2] = temp[i];
					count2++;
				}
			}
		} else {
			System.out.println("ImageAccessor::loadAlbums: 0 record stores exist. Creating default one.");
			resetImageRecordStore();
			loadAlbums();
		}
//		System.out.println("ImageAccessor::loadAlbums: Finished ok! ");
	}

	/**
	 * Reset the album data for MobilePhoto. This will delete all existing photo
	 * data from the record store and re-create the default album and photos.
	 * 
	 * @throws InvalidImageFormatException
	 * @throws ImagePathNotValidException
	 * @throws InvalidImageDataException
	 * @throws PersistenceMechanismException
	 * 
	 */
	public void resetImageRecordStore() throws InvalidImageDataException, PersistenceMechanismException {

		String storeName = null;
		String infoStoreName = null;

		// remove any existing album stores...
		if (albumNames != null) {
			for (int i = 0; i < albumNames.length; i++) {
				try {
					// Delete all existing stores containing Image objects as
					// well as the associated ImageInfo objects
					// Add the prefixes labels to the info store

					storeName = ALBUM_LABEL + albumNames[i];
					infoStoreName = INFO_LABEL + albumNames[i];

					System.out.println("<* ImageAccessor.resetImageRecordStore() *> delete "+storeName);
					
					RecordStore.deleteRecordStore(storeName);
					RecordStore.deleteRecordStore(infoStoreName);

				} catch (RecordStoreException e) {
					System.out.println("No record store named " + storeName
							+ " to delete.");
					System.out.println("...or...No record store named "
							+ infoStoreName + " to delete.");
					System.out.println("Ignoring Exception: " + e);
					// ignore any errors...
				}
			}
		} else {
			// Do nothing for now
			System.out
					.println("ImageAccessor::resetImageRecordStore: albumNames array was null. Nothing to delete.");
		}

		// Now, create a new default album for testing
		addImageData("Tucan Sam", "/images/Tucan.png",
				ImageAccessor.DEFAULT_ALBUM_NAME);
		// Add Penguin
		addImageData("Linux Penguin", "/images/Penguin.png",
				ImageAccessor.DEFAULT_ALBUM_NAME);
		// Add Duke
		addImageData("Duke (Sun)", "/images/Duke1.PNG",
				ImageAccessor.DEFAULT_ALBUM_NAME);
		addImageData("UBC Logo", "/images/ubcLogo.PNG",
				ImageAccessor.DEFAULT_ALBUM_NAME);
		// Add Gail
		addImageData("Gail", "/images/Gail1.PNG",
				ImageAccessor.DEFAULT_ALBUM_NAME);
		// Add JG
		addImageData("J. Gosling", "/images/Gosling1.PNG",
				ImageAccessor.DEFAULT_ALBUM_NAME);
		// Add GK
		addImageData("Gregor", "/images/Gregor1.PNG",
				ImageAccessor.DEFAULT_ALBUM_NAME);
		// Add KDV
		addImageData("Kris", "/images/Kdvolder1.PNG",
				ImageAccessor.DEFAULT_ALBUM_NAME);

	}

	public void addImageData(String photoname, String path, String albumname)
			throws InvalidImageDataException, PersistenceMechanismException {

		try {
			imageRS = RecordStore
					.openRecordStore(ALBUM_LABEL + albumname, true);
			imageInfoRS = RecordStore.openRecordStore(INFO_LABEL + albumname,
					true);

			int rid; // new record ID for Image (bytes)
			int rid2; // new record ID for ImageData (metadata)

			ImageUtil converter = new ImageUtil();

			// NOTE: For some Siemen's phone, all images have to be less than
			// 16K
			// May have to check for this, or try to convert to a lesser format
			// for display on Siemen's phones (Could put this in an Aspect)

			// Add Tucan
			byte[] data1 = converter.readImageAsByteArray(path);
			rid = imageRS.addRecord(data1, 0, data1.length);
			ImageData ii = new ImageData(rid, ImageAccessor.ALBUM_LABEL
					+ albumname, photoname);
			rid2 = imageInfoRS.getNextRecordID();
			ii.setRecordId(rid2);
			data1 = converter.getBytesFromImageInfo(ii);
			imageInfoRS.addRecord(data1, 0, data1.length);

			imageRS.closeRecordStore();

			imageInfoRS.closeRecordStore();
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}
	}

	// #ifdef includeCopyPhoto
//# 	/**
//# 	 * [EF] Add in scenario 05
//# 	 * @param photoname
//# 	 * @param imageData
//# 	 * @param albumname
//# 	 * @throws InvalidImageDataException
//# 	 * @throws PersistenceMechanismException
//# 	 */
//# 	public void addImageData(String photoname, ImageData imageData, String albumname) throws InvalidImageDataException, PersistenceMechanismException {
//# 		try {
//# 			imageRS = RecordStore.openRecordStore(ALBUM_LABEL + albumname, true);
//# 			imageInfoRS = RecordStore.openRecordStore(INFO_LABEL + albumname, true);
//# 			int rid2; // new record ID for ImageData (metadata)
//# 			ImageUtil converter = new ImageUtil();
//# 			rid2 = imageInfoRS.getNextRecordID();
//# 			imageData.setRecordId(rid2);
//# 			byte[] data1 = converter.getBytesFromImageInfo(imageData);
//# 			imageInfoRS.addRecord(data1, 0, data1.length);
//# 		} catch (RecordStoreException e) {
//# 			throw new PersistenceMechanismException();
//# 		}finally{
//# 			try {
//# 				imageRS.closeRecordStore();
//# 				imageInfoRS.closeRecordStore();
//# 			} catch (RecordStoreNotOpenException e) {
//# 				// TODO Auto-generated catch block
//# 				e.printStackTrace();
//# 			} catch (RecordStoreException e) {
//# 				// TODO Auto-generated catch block
//# 				e.printStackTrace();
//# 			}
//# 		}
//# 	}
	// #endif

	/**
	 * This will populate the imageInfo hashtable with the ImageInfo object,
	 * referenced by label name and populate the imageTable hashtable with Image
	 * objects referenced by the RMS record Id
	 * 
	 * @throws PersistenceMechanismException
	 */
	public ImageData[] loadImageDataFromRMS(String recordName)
			throws PersistenceMechanismException, InvalidImageDataException {

		Vector imagesVector = new Vector();

		try {

// [EF] not used			String storeName = ImageAccessor.ALBUM_LABEL + recordName;
			String infoStoreName = ImageAccessor.INFO_LABEL + recordName;

			RecordStore infoStore = RecordStore.openRecordStore(infoStoreName,
					false);
			RecordEnumeration isEnum = infoStore.enumerateRecords(null, null,
					false);

			while (isEnum.hasNextElement()) {
				// Get next record
				int currentId = isEnum.nextRecordId();
				byte[] data = infoStore.getRecord(currentId);

				// Convert the data from a byte array into our ImageData
				// (metadata) object
				ImageUtil converter = new ImageUtil();
				ImageData iiObject = converter.getImageInfoFromBytes(data);

				// Add the info to the metadata hashtable
				String label = iiObject.getImageLabel();
				imagesVector.addElement(iiObject);
				model.getImageInfoTable().put(label, iiObject);

			}

			infoStore.closeRecordStore();

		}catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}

		// Re-copy the contents into a smaller array
		ImageData[] labelArray = new ImageData[imagesVector.size()];
		imagesVector.copyInto(labelArray);
		return labelArray;
	}

	/**
	 * Update the Image metadata associated with this named photo
	 * @throws InvalidImageDataException 
	 * @throws PersistenceMechanismException 
	 */
	public boolean updateImageInfo(ImageData oldData, ImageData newData) throws InvalidImageDataException, PersistenceMechanismException {

		boolean success = false;
		RecordStore infoStore = null;
		try {

			// Parse the Data store name to get the Info store name
			String infoStoreName = oldData.getParentAlbumName();
			infoStoreName = ImageAccessor.INFO_LABEL + infoStoreName.substring(ImageAccessor.ALBUM_LABEL.length());
			infoStore = RecordStore.openRecordStore(infoStoreName, false);

			ImageUtil converter = new ImageUtil();
			byte[] imageDataBytes = converter.getBytesFromImageInfo(newData);

			infoStore.setRecord(oldData.getRecordId(), imageDataBytes, 0, imageDataBytes.length);

		} catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}

		// Update the Hashtable 'cache'
		setImageInfo(oldData.getImageLabel(), newData);

		try {
			infoStore.closeRecordStore();
		} catch (RecordStoreNotOpenException e) {
			//No problem if the RecordStore is not Open
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException(e);
		}

		return success;
	}

	/**
	 * Retrieve the metadata associated with a specified image (by name)
	 * @throws ImageNotFoundException 
	 * @throws NullAlbumDataReference 
	 */
	public ImageData getImageInfo(String imageName) throws ImageNotFoundException, NullAlbumDataReference {

		if (model == null)
			throw new NullAlbumDataReference("Null reference to the Album data");

		ImageData ii = (ImageData) model.getImageInfoTable().get(imageName);

		if (ii == null)
			throw new ImageNotFoundException(imageName +" was NULL in ImageAccessor Hashtable.");
			

		return ii;

	}

	/**
	 * Update the hashtable with new ImageInfo data
	 */
	public void setImageInfo(String imageName, ImageData newData) {

		model.getImageInfoTable().put(newData.getImageLabel(), newData);

	}

	/**
	 * Fetch a single image from the Record Store This should be used for
	 * loading images on-demand (only when they are viewed or sent via SMS etc.)
	 * to reduce startup time by loading them all at once.
	 * @throws PersistenceMechanismException 
	 */
	public Image loadSingleImageFromRMS(String recordName, String imageName,
			int recordId) throws PersistenceMechanismException {

		Image img = null;
		byte[] imageData = loadImageBytesFromRMS(recordName, imageName,
				recordId);
		img = Image.createImage(imageData, 0, imageData.length);
		return img;
	}

	/**
	 * Get the data for an Image as a byte array. This is useful for sending
	 * images via SMS or HTTP
	 * @throws PersistenceMechanismException 
	 */
	public byte[] loadImageBytesFromRMS(String recordName, String imageName,
			int recordId) throws PersistenceMechanismException {

		byte[] imageData = null;

		try {

			RecordStore albumStore = RecordStore.openRecordStore(recordName,
					false);
			imageData = albumStore.getRecord(recordId);
			albumStore.closeRecordStore();

		} catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}

		return imageData;
	}

	/**
	 * Delete a single (specified) image from the (specified) record store. This
	 * will permanently delete the image data and metadata from the device.
	 * @throws PersistenceMechanismException 
	 * @throws NullAlbumDataReference 
	 * @throws ImageNotFoundException 
	 */
	public boolean deleteSingleImageFromRMS(String storeName, String imageName) throws PersistenceMechanismException, ImageNotFoundException, NullAlbumDataReference {

		boolean success = false;

		// Open the record stores containing the byte data and the meta data
		// (info)
		try {

			// Verify storeName is name without pre-fix
			imageRS = RecordStore
					.openRecordStore(ALBUM_LABEL + storeName, true);
			imageInfoRS = RecordStore.openRecordStore(INFO_LABEL + storeName,
					true);

			ImageData imageData = getImageInfo(imageName);
			int rid = imageData.getForeignRecordId();

			imageRS.deleteRecord(rid);
			imageInfoRS.deleteRecord(rid);

			imageRS.closeRecordStore();
			imageInfoRS.closeRecordStore();

		} catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}

		// TODO: It's not clear from the API whether the record store needs to
		// be closed or not...

		return success;
	}

	/**
	 * Define a new photo album for mobile photo users. This creates a new
	 * record store to store photos for the album.
	 * @throws PersistenceMechanismException 
	 * @throws InvalidPhotoAlbumNameException 
	 */
	public void createNewPhotoAlbum(String albumName) throws PersistenceMechanismException, InvalidPhotoAlbumNameException {
		
		RecordStore newAlbumRS = null;
		RecordStore newAlbumInfoRS = null;
		if (albumName.equals("")){
			throw new InvalidPhotoAlbumNameException();
		}
		String[] names  = getAlbumNames();
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(albumName))
				throw new InvalidPhotoAlbumNameException();
		}
		
		try {
			newAlbumRS = RecordStore.openRecordStore(ALBUM_LABEL + albumName,
					true);
			newAlbumInfoRS = RecordStore.openRecordStore(
					INFO_LABEL + albumName, true);
			newAlbumRS.closeRecordStore();
			newAlbumInfoRS.closeRecordStore();
		} catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}

	}

	public void deletePhotoAlbum(String albumName) throws PersistenceMechanismException {

		try {
			RecordStore.deleteRecordStore(ALBUM_LABEL + albumName);
			RecordStore.deleteRecordStore(INFO_LABEL + albumName);
		} catch (RecordStoreException rse) {
			throw new PersistenceMechanismException(rse);
		}

	}

	/**
	 * Get the list of photo album names currently loaded.
	 * 
	 * @return Returns the albumNames.
	 */
	public String[] getAlbumNames() {
		return albumNames;
	}
}
