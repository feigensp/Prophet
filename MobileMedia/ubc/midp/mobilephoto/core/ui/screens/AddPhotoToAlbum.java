package ubc.midp.mobilephoto.core.ui.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public class AddPhotoToAlbum extends Form {
	
	TextField labeltxt = new TextField("Photo label", "", 15, TextField.ANY);
	TextField photopathtxt = null;
	
	Command ok;
	Command cancel;

	public AddPhotoToAlbum(String title) {
		super(title);
		this.append(labeltxt);
		this.append(photopathtxt);
		ok = new Command("Save Photo", Command.SCREEN, 0);
		cancel = new Command("Cancel", Command.EXIT, 1);
		this.addCommand(ok);
		this.addCommand(cancel);
	}
	
	public String getPhotoName(){
		return labeltxt.getString();
	}
	
	/**
	 * [EF] Added in scenario 05 in order to reuse this screen in the Copy Photo functionality
	 * @param photoName
	 */
	public void setPhotoName(String photoName) {
		labeltxt.setString(photoName);
	}
	
	public String getPath() {
		return photopathtxt.getString();
	}

	/**
	 * [EF] Added in scenario 05 in order to reuse this screen in the Copy Photo functionality
	 * @param photoName
	 */
	public void setLabePhotoPath(String label) {
		photopathtxt.setLabel(label);
	}
}
