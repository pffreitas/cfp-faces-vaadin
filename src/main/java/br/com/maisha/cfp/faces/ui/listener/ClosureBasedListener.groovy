package br.com.maisha.cfp.faces.ui.listener

import com.vaadin.ui.Component.Event
import com.vaadin.ui.Component.Listener

class ClosureBasedListener implements Listener{

	def Class eventType
	def Closure callback

	public ClosureBasedListener(Class eventType, Closure cl){
		this.callback  = cl
		this.eventType = eventType
	}

	@Override
	public void componentEvent(Event event) {
		if(event in eventType)
			callback.call(event)
	}
}
