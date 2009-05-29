package org.hisp.dhis.vn.chr.object.action;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.ElementService;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormService;
import org.hisp.dhis.vn.chr.jdbc.FormManager;
import com.opensymphony.xwork.Action;

public class ListObjectAction implements Action{
	
	// -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

	private FormManager formManager;
	
	private FormService formService;
	
	private ElementService elementService;
	
	// -----------------------------------------------------------------------------------------------
    // Input && Output
    // -----------------------------------------------------------------------------------------------

	private Integer formId;
	
	private Form form;
	
	private ArrayList data;
	
	private Collection<Element> formLinks;
	
	// -----------------------------------------------------------------------------------------------
    // Getter && Setter
    // -----------------------------------------------------------------------------------------------
	
	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public void setFormManager(FormManager formManager) {
		this.formManager = formManager;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}
	
	public ArrayList getData() {
		return data;
	}

	public void setData(ArrayList data) {
		this.data = data;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
	
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}
	
	public Collection<Element> getFormLinks() {
		return formLinks;
	}

	public void setFormLinks(Collection<Element> formLinks) {
		this.formLinks = formLinks;
	}

	// -----------------------------------------------------------------------------------------------
    // Implement : process Select SQL 
    // -----------------------------------------------------------------------------------------------

	public String execute() throws Exception {
		
		form = formService.getForm(formId.intValue());
		
		formLinks =  elementService.getElementsByFormLink(formId.intValue());
				
		data = formManager.listObject(form, 1);

		return SUCCESS;
	}
}
