package rabbit.ui.internal.pages;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

import rabbit.core.storage.IAccessor;
import rabbit.core.storage.xml.CommandDataAccessor;
import rabbit.ui.DisplayPreference;
import rabbit.ui.TableLabelComparator;

/**
 * A page for displaying command usage.
 */
public class CommandPage extends AbstractTableViewerPage {

	private IAccessor accessor;
	private ICommandService service;

	/** The data model, the values are usage counts of the commands. */
	private Map<Command, Long> dataMapping;

	/**
	 * Constructor.
	 */
	public CommandPage() {
		super();
		accessor = new CommandDataAccessor();
		dataMapping = new HashMap<Command, Long>();
		service = (ICommandService)
				PlatformUI.getWorkbench().getService(ICommandService.class);
	}

	@Override
	public void createColumns(TableViewer viewer) {
		TableLabelComparator textSorter = new TableLabelComparator(viewer);
		TableLabelComparator valueSorter = createValueSorterForTable(viewer);

		int[] widths = new int[] { 150, 200, 100 };
		int[] styles = new int[] { SWT.LEFT, SWT.LEFT, SWT.RIGHT };
		String[] names = new String[] { "Name", "Description", "Usage Count" };
		for (int i = 0; i < names.length; i++) {
			TableColumn column = new TableColumn(viewer.getTable(), styles[i]);
			column.setText(names[i]);
			column.setWidth(widths[i]);
			column.addSelectionListener(
					(names.length - 1 == i) ? valueSorter : textSorter);
		}
	}

	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_CUT);
	}

	@Override
	public long getValue(Object o) {
		Long value = dataMapping.get(o);
		return (value == null) ? 0 : value;
	}

	@Override
	public void update(DisplayPreference p) {
		setMaxValue(0);
		dataMapping.clear();

		Map<String, Long> map = accessor.getData(p.getStartDate(), p.getEndDate());
		for (Entry<String, Long> item : map.entrySet()) {
			dataMapping.put(service.getCommand(item.getKey()), item.getValue());

			if (item.getValue() > getMaxValue()) {
				setMaxValue(item.getValue());
			}
		}
		getViewer().setInput(dataMapping.keySet());
	}

	@Override
	protected IContentProvider createContentProvider() {
		return new CollectionContentProvider();
	}

	@Override
	protected ITableLabelProvider createLabelProvider() {
		return new CommandPageLabelProvider(this);
	}
}
