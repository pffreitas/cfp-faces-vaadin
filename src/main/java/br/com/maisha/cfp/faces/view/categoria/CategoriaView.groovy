package br.com.maisha.cfp.faces.view.categoria

import br.com.maisha.cfp.context.BeanContext
import br.com.maisha.cfp.context.BeanContextAware
import br.com.maisha.cfp.faces.ui.event.RepositoryChangedEvent
import br.com.maisha.cfp.faces.ui.listener.GenericListener
import br.com.maisha.cfp.faces.view.subcategoria.SubcategoriaView
import br.com.maisha.cfp.model.Orcamento
import br.com.maisha.cfp.repositories.CategoriaRepository

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent

class CategoriaView extends CssLayout{
	private VerticalLayout categoriesContainer

	def Orcamento orcParent

	def EventBus eventBus

	def HorizontalLayout body

	public CategoriaView(Orcamento o){
		orcParent = o

		init()

		eventBus = BeanContextAware.get().getBean("eventBus")
		eventBus.register(this)
	}

	private void init(){
		addStyleName("categoria-panel")
		setWidth("100%")
		setHeight("350px")


		def top = createTopbar()
		createHeader(top)
		createToolbar(top)

		createBody()

		loadCategorias(new RepositoryChangedEvent('categoriaRepository', [orcamento: orcParent]))
	}

	private HorizontalLayout createTopbar(){
		HorizontalLayout top = new HorizontalLayout();
		top.with{
			setWidth("100%")
			addStyleName("toolbar");
		}
		addComponent(top);

		return top;
	}

	private void createHeader(HorizontalLayout top){
		final Label title = new Label(orcParent.nome);
		title.setSizeUndefined();
		title.addStyleName("h1");
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
	}

	private void createToolbar(HorizontalLayout top){
		HorizontalLayout toolbar = new HorizontalLayout()

		def add = new Button("add")
		add.setStyleName("small")
		add.addListener(new GenericListener(ClickEvent, {
			new CategoriaFormWindow(orcParent).open()
		}
		))
		toolbar.addComponent(add);
		toolbar.setComponentAlignment(add, Alignment.MIDDLE_LEFT);

		top.addComponent(toolbar)
		top.setExpandRatio(toolbar, 1);
	}

	private void createBody(){
		body = new HorizontalLayout()
		body.setSizeFull()
		addComponent(body)

		categoriesContainer = new VerticalLayout()
		categoriesContainer.setWidth("100%")
		categoriesContainer.setMargin(true)
		body.addComponent(categoriesContainer)
		body.setExpandRatio(categoriesContainer, 1)
	}

	@Subscribe public void loadCategorias(RepositoryChangedEvent rce){
		if(rce.repoName == "categoriaRepository"){
			categoriesContainer.removeAllComponents()

			def CategoriaRepository repo = BeanContext.get().getBean(rce.repoName)
			def categorias = repo.findByOrcamento(rce.data.orcamento)
			categorias.each {
				def catBt = new Button(it.nome)
				catBt.setStyleName(getPrimaryStyleName())
				catBt.addStyleName("categoria-panel-item")
				catBt.setData(it)
				catBt.addListener(new GenericListener(ClickEvent, categoriaClicked))
				categoriesContainer.addComponent(catBt)
			}
		}
	}

	def categoriaClicked = { ClickEvent e ->
		def styleActive = "categoria-panel-item-active"

		if(e.source in Button){
			def Button clickedBt = e.source

			def Button activeBt = categoriesContainer.getComponentIterator().find { it in Button && it.getStyleName().contains(styleActive) }
			if(activeBt || activeBt != clickedBt){
				activeBt?.removeStyleName(styleActive)
				clickedBt.addStyleName(styleActive)
			}

			def subcategoriaView = new SubcategoriaView(clickedBt.getData())
			body.addComponent(subcategoriaView)
			body.setExpandRatio(subcategoriaView, 3)
		}
	}
}
