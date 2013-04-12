package br.com.maisha.cfp.faces.ui.listener

import com.vaadin.ui.Component.Event
import com.vaadin.ui.Component.Listener

class ClosureBasedListener implements Listener{

	def Closure callback

	public ClosureBasedListener(Closure cl){
		this.callback  = cl
	}

	@Override
	public void componentEvent(Event event) {
		callback.call(event)
	}
}
