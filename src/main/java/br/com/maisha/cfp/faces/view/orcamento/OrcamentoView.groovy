package br.com.maisha.cfp.faces.view.orcamento

import br.com.maisha.cfp.business.OrcamentoBusiness
import br.com.maisha.cfp.context.BeanContext
import br.com.maisha.cfp.context.BeanContextAware
import br.com.maisha.cfp.faces.ui.ConfirmWindow
import br.com.maisha.cfp.faces.ui.listener.ClosureBasedListener
import br.com.maisha.cfp.model.Orcamento

import com.google.common.eventbus.EventBus
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Layout
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

	/** */
	def categoriaPanel

	/** Corpo desta view. */
	def Layout body

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
		loadOrcamentos();

		eventBus = BeanContextAware.get().getBean("eventBus")
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
		final Label title = new Label("Orcamento");
		title.setSizeUndefined();
		title.addStyleName("h1");
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
	}

	private void createToolbar(HorizontalLayout top){
		HorizontalLayout toolbar = new HorizontalLayout()

		def add = new Button("Add")
		add.addListener(new ClosureBasedListener(ClickEvent, {
			new ConfirmWindow("Novo Orcamento", new OrcamentoForm()).open()
		}))
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

	private void loadOrcamentos(){
		body.removeAllComponents()

		def orcamentos = oBusiness.getOrcamentoRepository().findAll();

		orcamentos.each { Orcamento o ->
			def ow = new OrcamentoWidget(o)
			body.addComponent(ow)
		}
	}


	@Override
	public void enter(ViewChangeEvent event) {
	}
}
