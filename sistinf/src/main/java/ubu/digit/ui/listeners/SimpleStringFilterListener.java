package ubu.digit.ui.listeners;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Table;

public class SimpleStringFilterListener implements TextChangeListener {

	private static final long serialVersionUID = 9041696136649951139L;

	private SimpleStringFilter filter = null;

	private Table table;

	private String propertyId;

	public SimpleStringFilterListener(Table table, String propertyId) {
		this.propertyId = propertyId;
		this.table = table;
	}

	@Override
	public void textChange(TextChangeEvent event) {
		Filterable f = (Filterable) table.getContainerDataSource();

		if (filter != null) {
			f.removeContainerFilter(filter);
		}

		filter = new SimpleStringFilter(propertyId, (String) event.getText(), true, false);
		f.addContainerFilter(filter);
	}
}
