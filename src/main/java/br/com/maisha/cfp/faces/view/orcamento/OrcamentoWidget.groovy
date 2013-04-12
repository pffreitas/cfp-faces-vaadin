package br.com.maisha.cfp.faces.view.orcamento


import br.com.maisha.cfp.model.Orcamento

import com.vaadin.event.LayoutEvents.LayoutClickEvent
import com.vaadin.event.LayoutEvents.LayoutClickListener
import com.vaadin.server.Page
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.ProgressIndicator
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.BaseTheme;


/**
 * Widget para representacao de um Orcamento.
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class OrcamentoWidget extends VerticalLayout implements LayoutClickListener, ClickListener{

	
	def List<Closure> listeners = []
	
	/** Orcamento a ser representado pelo widget. */
	def Orcamento orcamento

	/**
	 * Construtor configura o Orcamento a ser representado.
	 * 
	 * @param pOrcamento Orcamento.
	 */
	public OrcamentoWidget(Orcamento pOrcamento){
		this.orcamento = pOrcamento
		
		init()
	}

	private void init(){
		addStyleName("orcamento-wgt")
		setSpacing(false)
		setWidth("100px")

		createIconArea()
		createLabel()
	}

	private void createLabel() {
		Button b = new Button(orcamento.nome)
		b.setStyleName(BaseTheme.BUTTON_LINK)
		b.addClickListener(this)
		addComponent(b)
		setComponentAlignment(b, Alignment.TOP_CENTER)
	}

	private void createIconArea() {
		VerticalLayout iconArea = new VerticalLayout()
		iconArea.with {
			addStyleName("orcamento-wgt-icon")
			setSpacing(false)
			setWidth("100px")
			setHeight("100px")
		}

		ProgressIndicator pi = new ProgressIndicator(0.0f)
		pi.with {
			setValue(0.7f)
			setWidth("80px")
		}
		iconArea.addComponent(pi)
		iconArea.setComponentAlignment(pi, Alignment.BOTTOM_CENTER)

		iconArea.addLayoutClickListener(this)
		
		addComponent(iconArea)
	} 

	def void clicked(event){
		new Notification("aaaa").show(Page.getCurrent())
		addStyleName("orcamento-wgt-selected")
		listeners.each { it.call(this, event) }
	}
	
	def void onClick(Closure cl){
		listeners << cl
	}
	
	/**
	 * 
	 */
	public void layoutClick(LayoutClickEvent event) {
		clicked(event) 
	}

	/**  
	 * 
	 */
	public void buttonClick(ClickEvent event) {
		clicked(event)
	}
	
	
}
