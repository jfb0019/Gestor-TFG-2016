package ubu.digit.ui.columngenerators;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.ColumnGenerator;

import ubu.digit.ui.beans.ProjectBean;

public class TutorsColumnGenerator implements ColumnGenerator {

	private static final long serialVersionUID = -3597867904954299136L;

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		BeanItem<?> item = (BeanItem<?>) source.getItem(itemId);
		ProjectBean bean = (ProjectBean) item.getBean();

		String tutor1 = bean.getTutor1();
		String tutor2 = bean.getTutor2();
		String tutor3 = bean.getTutor3();

		VerticalLayout cellContent = new VerticalLayout();
		cellContent.setSpacing(false);
		cellContent.setMargin(false);

		createCell(tutor1, cellContent);
		createCell(tutor2, cellContent);
		createCell(tutor3, cellContent);

		return cellContent;
	}

	private void createCell(String field, VerticalLayout cellContent) {
		if (!"".equals(field) && field != null)
			cellContent.addComponent(new Label(field));
	}
}
