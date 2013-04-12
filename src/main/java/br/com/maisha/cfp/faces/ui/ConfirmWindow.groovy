package br.com.maisha.cfp.faces.ui

import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener


class ConfirmWindow extends Window {

	private VerticalLayout body
	
	private Map listeners

	public ConfirmWindow(String caption){
		super(caption)
		setModal(true)
		setResizable(false)
		setWidth("450px")
		setHeight("270px")
		
		center()

		init()
		
	}

	private void init(){
		VerticalLayout contents = new VerticalLayout()
		contents.setSizeFull()

		//form
		body = new VerticalLayout()
		body.setSizeFull()
		contents.addComponent(body)
		contents.setExpandRatio(body, 1f)

		//button bar
		HorizontalLayout hl  = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setMargin(true)
		hl.addStyleName("window-button-bar")

		Button cancel = new Button("Cancelar")
		Button save = new Button("Salvar")
		save.addClickListener(new ClickListener(){
			void buttonClick(ClickEvent evt) {
				listeners.save.call(evt)
			};
		});
		save.addStyleName("default")

		hl.addComponent(cancel)
		hl.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT)
		hl.addComponent(save)
		hl.setComponentAlignment(save, Alignment.MIDDLE_RIGHT)


		contents.addComponent(hl)
		contents.setComponentAlignment(hl, Alignment.BOTTOM_LEFT)

		setContent(contents)
	}

	public void when(map){
		this.listeners = map;
	}
	
	public void setBody(Component c){
		body.addComponent(c)
	}
}
