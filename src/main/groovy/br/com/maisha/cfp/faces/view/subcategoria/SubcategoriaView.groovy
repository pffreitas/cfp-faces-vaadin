package br.com.maisha.cfp.faces.view.subcategoria

import org.springframework.format.number.CurrencyFormatter

import br.com.maisha.cfp.context.BeanContextAware
import br.com.maisha.cfp.faces.ui.event.RepositoryChangedEvent
import br.com.maisha.cfp.faces.ui.listener.GenericListener
import br.com.maisha.cfp.model.Categoria
import br.com.maisha.cfp.repositories.SubcategoriaRepository

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent

/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class SubcategoriaView extends VerticalLayout{

	private Categoria categoria

	def EventBus eventBus
	
	def VerticalLayout body

	public SubcategoriaView(Categoria pCategoria){
		categoria = pCategoria

		eventBus = BeanContextAware.get().getBean("eventBus")
		eventBus.register(this)

		init()
		loadSubcategorias(new RepositoryChangedEvent('subcategoriaRepository', [categoria: categoria]))
	}

	private void init(){
		addStyleName("subcategoria-panel");
		setMargin(true)
		setWidth("97%")

		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		addComponent(hl)

		Button addSubcategoriaBt = new Button("ADD")
		hl.addComponent(addSubcategoriaBt)
		hl.setComponentAlignment(addSubcategoriaBt, Alignment.MIDDLE_RIGHT)
		addSubcategoriaBt.addListener(new GenericListener(ClickEvent, addClicked))
		
		body = new VerticalLayout()
		body.setSpacing(true)
		addComponent(body)
	}

	def addClicked = { ClickEvent evt ->
		new SubcategoriaFormWindow(categoria	).open()
	}
	
	
	@Subscribe public void loadSubcategorias(RepositoryChangedEvent rce){
		if(rce.repoName == "subcategoriaRepository"){
			body.removeAllComponents()
			
			def SubcategoriaRepository repo = BeanContextAware.get().getBean(rce.repoName)
			def subcategorias = repo.findByCategoria(rce.data.categoria)
			subcategorias.each {
				def valor = new CurrencyFormatter().print(it.valor, new Locale("pt", "BR"))
				def l = new Label("${it.nome} - $valor")
				l.addStyleName("h4")
				body.addComponent(l)
			}
		}
	}
}
