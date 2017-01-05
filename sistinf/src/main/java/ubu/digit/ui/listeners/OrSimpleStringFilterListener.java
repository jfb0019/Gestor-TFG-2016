package ubu.digit.ui.listeners;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Table;

public class OrSimpleStringFilterListener implements TextChangeListener {

	private static final long serialVersionUID = -8855180815378564739L;

	private Or filter = null;

	private Table table;

	private String propertyId1;

	private String propertyId2;

	private String propertyId3;

	public OrSimpleStringFilterListener(Table table, String propertyId1, String propertyId2, String propertyId3) {
		this.propertyId1 = propertyId1;
		this.propertyId2 = propertyId2;
		this.propertyId3 = propertyId3;
		this.table = table;
	}

	@Override
	public void textChange(TextChangeEvent event) {
		Filterable f = (Filterable) table.getContainerDataSource();
		
		if (filter != null) {
			f.removeContainerFilter(filter);
		}

		filter = new Or(new SimpleStringFilter(propertyId1, event.getText(), true, false),
				new SimpleStringFilter(propertyId2, event.getText(), true, false),
				new SimpleStringFilter(propertyId3, event.getText(), true, false));
		f.addContainerFilter(filter);
	}
}