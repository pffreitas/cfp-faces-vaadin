package br.com.maisha.cfp.faces.ui

import br.com.maisha.cfp.context.BeanContextAware
import br.com.maisha.cfp.faces.ui.event.OpenCloseWindowEvent
import br.com.maisha.cfp.faces.ui.listener.ClosureBasedListener

import com.google.common.eventbus.EventBus
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Window.CloseEvent
import com.vaadin.ui.Window.CloseListener



class ConfirmWindow extends Window {

	def EventBus eventBus

	public ConfirmWindow(String caption, Component body){
		super(caption)

		eventBus = BeanContextAware.get().getBean("eventBus")

		setModal(true)
		setResizable(false)
		setWidth("450px")
		setHeight("270px")
		center()
		init(body)
	}

	private void init(Component body){
		VerticalLayout contents = new VerticalLayout()
		contents.setSizeFull()

		//form
		body.setSizeFull()
		contents.addComponent(body)
		contents.setExpandRatio(body, 1f)

		//button bar
		HorizontalLayout hl  = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setMargin(true)
		hl.addStyleName("window-button-bar")

		Button cancel = new Button("Cancelar")
		cancel.addListener(new ClosureBasedListener (ClickEvent, close))

		Button save = new Button("Salvar")
		save.addStyleName("default")

		hl.addComponent(cancel)
		hl.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT)
		hl.addComponent(save)
		hl.setComponentAlignment(save, Alignment.MIDDLE_RIGHT)


		contents.addComponent(hl)
		contents.setComponentAlignment(hl, Alignment.BOTTOM_LEFT)

		setContent(contents)
		addListener(new ClosureBasedListener(CloseEvent, close))
	}

	public close = {
		eventBus.post(new OpenCloseWindowEvent(this, false))
	}

	public open = {
		eventBus.post(new OpenCloseWindowEvent(this))
	}
}
