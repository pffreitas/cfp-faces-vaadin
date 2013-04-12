package br.com.maisha.cfp.faces.ui.event

import com.vaadin.ui.Window

/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class OpenCloseWindowEvent {

	private Window window

	private Boolean openClose

	public OpenCloseWindowEvent(Window pWindowToBeOpened, Boolean openClose = true){
		this.window = pWindowToBeOpened
		this.openClose = openClose
	}

	public Window getWindow(){
		window
	}

	public Boolean mustOpen(){
		return openClose
	}
}
