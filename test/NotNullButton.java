package test;

import javax.swing.JButton;

public class NotNullButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button;
	
	public NotNullButton() {
		super();
	}
	
	public void setButton(JButton button) {
		this.button = button;
		callChange();
	}
	
	public JButton getButton() {
		return button;
	}
	
	private void callChange() {
		this.firePropertyChange("button", null, button);
	}
	

}
