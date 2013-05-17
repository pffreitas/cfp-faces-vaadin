package br.com.maisha.cfp.faces

import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class MainLayout extends HorizontalLayout{

	public MainLayout(UI ui) {
		build(ui)
	}

	private void build(ui){
		setSizeFull();
		addStyleName("main-view");

		// view area
		def viewContainer = new CssLayout()
		viewContainer.setSizeFull()
		viewContainer.addStyleName("view-content")
		addComponent(viewContainer)
		setExpandRatio(viewContainer, 1)

		//sidebar
		addComponentAsFirst(new VerticalLayout() {
					{
						addStyleName("sidebar")
						setWidth(null)
						setHeight("100%")

						addComponent(new Branding())

						def menu = new Menu(ui, viewContainer)
						addComponent(menu)
						setExpandRatio(menu, 1)

						addComponent(new UserInfo())
					}
				});
	}
}
