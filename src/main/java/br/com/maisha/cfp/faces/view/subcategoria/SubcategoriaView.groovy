package br.com.maisha.cfp.faces.view.subcategoria

import br.com.maisha.cfp.model.Categoria

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class SubcategoriaView extends VerticalLayout{

	private Categoria categoria
	
	public SubcategoriaView(Categoria pCategoria){
		categoria = pCategoria
		init()
	}

	private void init(){
		addStyleName("subcategoria-panel");
		setMargin(true)
		setWidth("97%")
		
		def l = new Label(categoria.nome)
		l.addStyleName("h4")
		addComponent(l)
	}
}
