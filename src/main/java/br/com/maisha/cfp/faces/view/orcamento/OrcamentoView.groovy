package br.com.maisha.cfp.faces.view.orcamento

import br.com.maisha.cfp.business.OrcamentoBusiness
import br.com.maisha.cfp.context.BeanContext
import br.com.maisha.cfp.faces.ui.ConfirmWindow
import br.com.maisha.cfp.faces.ui.listener.ClosureBasedListener
import br.com.maisha.cfp.model.Orcamento
import br.com.maisha.cfp.model.Orcamento.Tipo

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.UserError
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Layout
import com.vaadin.ui.Panel
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
		def toolbar = createToolbar()
		top.addComponent(toolbar)
		top.setExpandRatio(toolbar, 1);

		//body
		body = createBody();
		loadOrcamentos();

	}

	private void loadOrcamentos(){
		body.removeAllComponents()

		def orcamentos = oBusiness.getOrcamentoRepository().findAll();

		orcamentos.each { Orcamento o ->
			def ow = new OrcamentoWidget(o)
			ow.onClick orcamentoClicked
			body.addComponent(ow)
		}
	}

	private CssLayout createBody(){
		def CssLayout body = new CssLayout()
		body.setSizeFull()
		addComponent(body)
		setExpandRatio(body, 1.5)

		return body;
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

	private HorizontalLayout createToolbar(){
		HorizontalLayout toolbar = new HorizontalLayout()

		def add = new Button("Add")
		add.addListener(new ClosureBasedListener(addButtonClicked))
		toolbar.addComponent(add);
		toolbar.setComponentAlignment(add, Alignment.MIDDLE_LEFT);

		return toolbar;
	}


	private void createHeader(HorizontalLayout top){
		final Label title = new Label("Orcamento");
		title.setSizeUndefined();
		title.addStyleName("h1");
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}


	def addButtonClicked = { ClickEvent evt ->
		def ui = evt.getButton().getUI()

		def ConfirmWindow w = new ConfirmWindow("Novo Orcamento")
		def oForm = new OrcamentoForm()
		w.setBody(oForm)

		w.when(
				save: {
					if(oForm.binder.isValid()){
						Orcamento o = new Orcamento()
						o.nome = oForm["nome"].value
						o.tipo =  Tipo.ENTRADA

						oBusiness.criarOrcamento(o)
						loadOrcamentos()

						ui.removeWindow(w)
					} else {
					}
				},

				cancel: { ui.removeWindow(w) }
				)

		ui.addWindow(w)
	}

	def orcamentoClicked = { ow, evt ->
		if(categoriaPanel)
			body.removeComponent(categoriaPanel)

		categoriaPanel = new Panel()
		categoriaPanel.with {
			setData("categoria")
			setWidth("100%")
			setStyleName("categoria-panel")
		}

		def idx = body.getComponentIndex(ow)
		body.addComponent(categoriaPanel, idx+1)
	}
}
