package br.com.maisha.cfp.faces.view.subcategoria



import br.com.maisha.cfp.business.OrcamentoBusiness
import br.com.maisha.cfp.context.BeanContextAware
import br.com.maisha.cfp.faces.ui.event.OpenCloseWindowEvent
import br.com.maisha.cfp.faces.ui.event.RepositoryChangedEvent
import br.com.maisha.cfp.faces.ui.listener.GenericListener
import br.com.maisha.cfp.model.Categoria
import br.com.maisha.cfp.model.Recorrencia
import br.com.maisha.cfp.model.SubCategoria
import br.com.maisha.cfp.model.Recorrencia.Granularidade

import com.google.common.eventbus.EventBus
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.ObjectProperty
import com.vaadin.data.util.PropertysetItem
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBox
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Field.ValueChangeEvent
import com.vaadin.ui.Window.CloseEvent


/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class SubcategoriaFormWindow extends Window {
	
	private TextField diaVencimentoTf
	
	private CheckBox gerarAlerta

	private TextField nomeTf;

	def PropertysetItem item

	def FieldGroup binder

	def EventBus eventBus

	def OrcamentoBusiness oBusiness

	def Categoria cParent

	/**
	 * 
	 */
	public SubcategoriaFormWindow(Categoria parent){
		super("Nova Subcategoria")
		this.cParent = parent

		eventBus = BeanContextAware.get().getBean("eventBus")
		oBusiness = BeanContextAware.get().getBean("orcamentoBusiness")

		setModal(true)
		setResizable(false)
		setWidth("450px")
		setHeight("320px")
		center()
		init()
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
		nomeTf.focus()
		body.addComponent(nomeTf)

		def valorTf = new TextField("Valor")
		valorTf.setWidth("230px")
		valorTf.setRequired(true)
		valorTf.setConverter(BigDecimal)
		body.addComponent(valorTf)

		HorizontalLayout recorrencia = new  HorizontalLayout()
		recorrencia.setSpacing(true)
		body.addComponent(recorrencia)

		VerticalLayout vl = new VerticalLayout()
		recorrencia.addComponent(vl)
		recorrencia.setComponentAlignment(vl, Alignment.BOTTOM_LEFT)

		CheckBox mensal = new CheckBox("Mensal")
		mensal.addListener(new GenericListener(ValueChangeEvent, mensalBlured))
		mensal.setImmediate(true)
		vl.addComponent(mensal)

		gerarAlerta = new CheckBox("Gerar Alerta")
		vl.addComponent(gerarAlerta)

		diaVencimentoTf = new TextField("Dia de Vencimento")
		diaVencimentoTf.setWidth("40px")
		recorrencia.addComponent(diaVencimentoTf)


		item = new PropertysetItem()
		item.addItemProperty("nome", new ObjectProperty(""));
		item.addItemProperty("valor", new ObjectProperty(0, BigDecimal));
		item.addItemProperty("gerarAlerta", new ObjectProperty(false, Boolean));
		item.addItemProperty("diaVencimento", new ObjectProperty(1, Integer));
		item.addItemProperty("mensal", new ObjectProperty(false, Boolean));

		binder = new FieldGroup(item);
		binder.bind(nomeTf, "nome");
		binder.bind(valorTf, "valor");
		binder.bind(gerarAlerta, "gerarAlerta");
		binder.bind(diaVencimentoTf, "diaVencimento");
		binder.bind(mensal, "mensal");

		gerarAlerta.setEnabled(false)
		diaVencimentoTf.setEnabled(false)
		
		return body;
	}


	def getAt(String prop){
		item.getItemProperty(prop).value
	}


	def mensalBlured = { ValueChangeEvent evt ->
		def val = evt.property.value
		gerarAlerta.setEnabled(val)
		diaVencimentoTf.setEnabled(val)
			
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

		SubCategoria sc = new SubCategoria(categoria: cParent, nome: this["nome"], valor: this["valor"], gerarAlerta: this["gerarAlerta"])
		cParent.addToSubcategorias(sc)
		
		if(this["mensal"]) {
			sc.recorrencia = new Recorrencia(subcategoria: sc, vencimento: this["diaVencimento"], granularidade: Granularidade.MENSAL)
		}
		
		oBusiness.criarSubCategoria(sc);
		eventBus.post(new RepositoryChangedEvent("subcategoriaRepository", [categoria: cParent]))

		close()
	}

}

