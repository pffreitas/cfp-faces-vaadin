package br.com.maisha.cfp.faces.view.orcamento



import com.vaadin.data.Validator.InvalidValueException
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.ObjectProperty
import com.vaadin.data.util.PropertysetItem
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout


/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class OrcamentoForm extends VerticalLayout {

	private TextField nomeTf;

	private ComboBox tipoCb;

	private VerticalLayout messages

	def PropertysetItem item

	def FieldGroup binder

	/**
	 * 
	 */
	public OrcamentoForm(){
		init()
	}

	private void init(){
		setSizeFull()
		setMargin(true)

		nomeTf = new TextField("Nome")
		nomeTf.setWidth("100%")
		nomeTf.setRequired(true)
		nomeTf.setImmediate(false)
		nomeTf.setNullRepresentation("")
		nomeTf.setValidationVisible(true)
		addComponent(nomeTf)

		tipoCb = new ComboBox("Tipo", ["Entrada", "Saida"])
		tipoCb.setRequired(true)
		addComponent(tipoCb)

		item = new PropertysetItem()
		item.addItemProperty("nome", new ObjectProperty(""));
		item.addItemProperty("tipo", new ObjectProperty("Entrada"));

		binder = new FieldGroup(item);
		binder.bind(nomeTf, "nome");
		binder.bind(tipoCb, "tipo");
	}


	def getAt(String prop){
		item.getItemProperty(prop).value
	}
}

