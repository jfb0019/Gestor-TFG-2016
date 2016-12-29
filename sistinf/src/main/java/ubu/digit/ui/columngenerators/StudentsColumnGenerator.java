package ubu.digit.ui.columngenerators;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import ubu.digit.ui.beans.ActiveProjectBean;

public class StudentsColumnGenerator implements ColumnGenerator {

	private static final long serialVersionUID = 9038820422992945181L;

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		BeanItem<?> item = (BeanItem<?>) source.getItem(itemId);
		ActiveProjectBean bean = (ActiveProjectBean) item.getBean();

		String student1 = bean.getStudent1();
		String student2 = bean.getStudent2();
		String student3 = bean.getStudent3();

		VerticalLayout cellContent = new VerticalLayout();
		cellContent.setSpacing(false);
		cellContent.setMargin(false);

		createCell(student1, cellContent);
		createCell(student2, cellContent);
		createCell(student3, cellContent);

		return cellContent;
	}

	private void createCell(String field, VerticalLayout cellContent) {
		if (!"".equals(field) && field != null)
			cellContent.addComponent(new Label(field));
	}
}
