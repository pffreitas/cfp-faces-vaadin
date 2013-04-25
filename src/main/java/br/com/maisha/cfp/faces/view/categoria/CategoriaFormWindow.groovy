package br.com.maisha.cfp.faces.view.categoria



import br.com.maisha.cfp.business.OrcamentoBusiness
import br.com.maisha.cfp.context.BeanContext
import br.com.maisha.cfp.context.BeanContextAware
import br.com.maisha.cfp.faces.ui.event.OpenCloseWindowEvent
import br.com.maisha.cfp.faces.ui.event.RepositoryChangedEvent
import br.com.maisha.cfp.faces.ui.listener.GenericListener
import br.com.maisha.cfp.model.Categoria
import br.com.maisha.cfp.model.Orcamento

import com.google.common.eventbus.EventBus
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.ObjectProperty
import com.vaadin.data.util.PropertysetItem
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Window.CloseEvent


/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class CategoriaFormWindow extends Window {

	private TextField nomeTf;

	def PropertysetItem item

	def FieldGroup binder

	def EventBus eventBus

	def OrcamentoBusiness oBusiness

	private Orcamento orcParent

	/**
	 * 
	 */
	public CategoriaFormWindow(Orcamento orcParent){
		super("Nova Categoria")

		eventBus = BeanContextAware.get().getBean("eventBus")
		oBusiness = BeanContext.get().getBean("orcamentoBusiness")

		setModal(true)
		setResizable(false)
		setWidth("450px")
		setHeight("210px")
		center()
		init()

		this.orcParent = orcParent
	}

	private void init(){
		VerticalLayout contents = new VerticalLayout()
		contents.setSizeFull()

		//form
		def body = createBody()
		contents.addComponent(body)
		contents.setExpandRatio(body, 1f)

		//button bar
		HorizontalLayout hl  = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setMargin(true)
		hl.addStyleName("window-button-bar")

		Button cancelBt = new Button("Cancelar")
		cancelBt.addListener(new GenericListener (ClickEvent, cancel))
		hl.addComponent(cancelBt)
		hl.setComponentAlignment(cancelBt, Alignment.MIDDLE_LEFT)

		Button saveBt = new Button("Salvar")
		saveBt.addStyleName("default")
		saveBt.addListener(new GenericListener (ClickEvent, save))
		hl.addComponent(saveBt)
		hl.setComponentAlignment(saveBt, Alignment.MIDDLE_RIGHT)

		contents.addComponent(hl)
		contents.setComponentAlignment(hl, Alignment.BOTTOM_LEFT)

		setContent(contents)
		addListener(new GenericListener(CloseEvent, cancel))
	}

	private VerticalLayout createBody(){
		VerticalLayout body = new VerticalLayout()
		body.setSizeFull()
		body.setMargin(true)

		nomeTf = new TextField("Nome")
		nomeTf.setWidth("100%")
		nomeTf.setRequired(true)
		nomeTf.setImmediate(false)
		nomeTf.setNullRepresentation("")
		nomeTf.setValidationVisible(true)
		nomeTf.focus()
		body.addComponent(nomeTf)

		item = new PropertysetItem()
		item.addItemProperty("nome", new ObjectProperty(""));

		binder = new FieldGroup(item);
		binder.bind(nomeTf, "nome");

		return body;
	}


	def getAt(String prop){
		item.getItemProperty(prop).value
	}


	public cancel = { close() }

	public close = {
		eventBus.post(new OpenCloseWindowEvent(this, false))
	}

	public open = {
		eventBus.post(new OpenCloseWindowEvent(this))
	}

	public save = {
		binder.commit()
		Categoria c = new Categoria(orcamento: orcParent, nome: this["nome"])
		orcParent.addToCategorias(c)
		oBusiness.criarCategoria(c);
		eventBus.post(new RepositoryChangedEvent("categoriaRepository", [orcamento: orcParent]))
		close()
	}
}

