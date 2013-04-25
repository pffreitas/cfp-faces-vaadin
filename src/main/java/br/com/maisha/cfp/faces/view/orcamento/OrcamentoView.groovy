package br.com.maisha.cfp.faces.view.orcamento

import br.com.maisha.cfp.business.OrcamentoBusiness
import br.com.maisha.cfp.context.BeanContext
import br.com.maisha.cfp.context.BeanContextAware
import br.com.maisha.cfp.faces.ui.event.RepositoryChangedEvent
import br.com.maisha.cfp.faces.ui.listener.GenericListener
import br.com.maisha.cfp.faces.view.categoria.CategoriaView
import br.com.maisha.cfp.model.Orcamento
import br.com.maisha.cfp.repositories.OrcamentoRepository

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent


/**
 * Visualizacao dos orcamentos do usuario.
 * <p/>
 * Permite o gerenciamento dos orcamentos, categorias e subcategorias.
 *    
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class OrcamentoView extends VerticalLayout implements View{

	/** Categoria View. */
	def CategoriaView categoriaView

	/** Corpo desta view. */
	def CssLayout body

	/** Servicos para o gerenciamento de Orcamentos. */
	def OrcamentoBusiness oBusiness;

	/** Guava's Event Bus. */
	def EventBus eventBus;

	/**
	 * Constroi os elementos graficos desta view 
	 */
	public OrcamentoView(){
		oBusiness = BeanContext.get().getBean(OrcamentoBusiness)

		setSizeFull();
		addStyleName("dashboard-view");

		//topbar
		def top = createTopbar();

		//header
		createHeader(top);

		//toolbar
		createToolbar(top)

		//body
		body = createBody();
		loadOrcamentos(new RepositoryChangedEvent("orcamentoRepository"));

		eventBus = BeanContextAware.get().getBean("eventBus")
		eventBus.register(this)
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
		final Label title = new Label("Orcamentos");
		title.setSizeUndefined();
		title.addStyleName("h1");
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
	}

	private void createToolbar(HorizontalLayout top){
		HorizontalLayout toolbar = new HorizontalLayout()

		def add = new Button("+")
		add.setStyleName("small")
		add.addListener(new GenericListener(ClickEvent, {
			new OrcamentoFormWindow().open()
		}
		))
		toolbar.addComponent(add);
		toolbar.setComponentAlignment(add, Alignment.MIDDLE_LEFT);

		top.addComponent(toolbar)
		top.setExpandRatio(toolbar, 1);
	}


	private CssLayout createBody(){
		def CssLayout body = new CssLayout()
		body.setSizeFull()
		addComponent(body)
		setExpandRatio(body, 1.5)

		return body;
	}

	@Subscribe public void loadOrcamentos(RepositoryChangedEvent rce){
		if(rce.repoName == "orcamentoRepository"){
			def OrcamentoRepository repo = BeanContext.get().getBean(rce.repoName)
			body.removeAllComponents()
			def orcamentos = repo.findAll();

			orcamentos.each { Orcamento o ->
				def ow = new OrcamentoWidget(o)
				ow.onClick orcamentoWidgetClicked
				body.addComponent(ow)
			}
		}
	}

	def orcamentoWidgetClicked = { OrcamentoWidget ow, event ->
		if(categoriaView)
			body.removeComponent(categoriaView)

		categoriaView = new CategoriaView(ow.orcamento)
		def owIdx = body.getComponentIndex(ow)
		body.addComponent(categoriaView, owIdx + 1)
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
