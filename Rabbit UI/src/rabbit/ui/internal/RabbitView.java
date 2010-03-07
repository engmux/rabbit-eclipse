package rabbit.ui.internal;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import rabbit.core.RabbitCore;
import rabbit.ui.DisplayPreference;
import rabbit.ui.IPage;

/**
 * A view to show metrics.
 */
public class RabbitView extends ViewPart {

	/**
	 * A map containing page status (updated or not), if a page is not updated
	 * (value return false), then it will be updated before it's displayed (when
	 * a user clicks on a tree node).
	 */
	private Map<IPage, Boolean> pageStatus;

	/** A map containing pages and the root composite of the page. */
	private Map<IPage, Composite> pages;

	/** A map containing pages and their tool bar items. */
	private Map<IPage, IContributionItem[]> pageToolItems;

	/** A tool bar for pages to create their tool bar items. */
	private IToolBarManager extensionToolBar;

	private FormToolkit toolkit;
	private StackLayout stackLayout;
	private Composite displayPanel;

	private final DisplayPreference preferences;

	/**
	 * Constructs a new view.
	 */
	public RabbitView() {
		pages = new HashMap<IPage, Composite>();
		pageStatus = new HashMap<IPage, Boolean>();
		pageToolItems = new HashMap<IPage, IContributionItem[]>();

		toolkit = new FormToolkit(PlatformUI.getWorkbench().getDisplay());
		stackLayout = new StackLayout();
		preferences = new DisplayPreference();
	}

	@Override
	public void createPartControl(Composite parent) {
		Form form = toolkit.createForm(parent);
		form.getBody().setLayout(new FormLayout());

		FormData fd = new FormData();
		fd.width = 1;
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 200);
		fd.bottom = new FormAttachment(100, 0);
		final Sash sash = new Sash(form.getBody(), SWT.VERTICAL);
		sash.setBackground(toolkit.getColors().getBorderColor());
		sash.setLayoutData(fd);
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				((FormData) sash.getLayoutData()).left = new FormAttachment(0, e.x);
				sash.getParent().layout();
			}
		});

		// Extension list:
		FormData leftData = new FormData();
		leftData.top = new FormAttachment(0, 0);
		leftData.left = new FormAttachment(0, 0);
		leftData.right = new FormAttachment(sash, 0);
		leftData.bottom = new FormAttachment(100, 0);
		Form left = toolkit.createForm(form.getBody());
		left.setText("Metrics");
		left.setLayoutData(leftData);
		left.getBody().setLayout(new FillLayout());
		MetricsPanel list = new MetricsPanel(this);
		list.createContents(left.getBody());

		// Displaying area:
		FormData rightData = new FormData();
		rightData.top = new FormAttachment(0, 0);
		rightData.left = new FormAttachment(sash, 0);
		rightData.right = new FormAttachment(100, 0);
		rightData.bottom = new FormAttachment(100, 0);

		Composite right = toolkit.createComposite(form.getBody());
		right.setLayoutData(rightData);
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(right);

		// Header:
		Composite header = toolkit.createComposite(right);
		header.setLayout(new GridLayout(3, false));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(header);
		{
			Label label = toolkit.createLabel(header, "Statistics");
			label.setFont(left.getHead().getFont());
			label.setForeground(left.getHead().getForeground());

			ToolBar bar = new ToolBar(header, SWT.FLAT);
			bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			bar.setBackground(header.getBackground());
			extensionToolBar = new ToolBarManager(bar);

			bar = new ToolBar(header, SWT.FLAT);
			bar.setBackground(header.getBackground());
			createToolBarItems(new ToolBarManager(bar));
		}
		displayPanel = toolkit.createComposite(right);
		displayPanel.setLayout(stackLayout);
		GridDataFactory.fillDefaults().grab(true, true).span(3, 1).applyTo(displayPanel);

		// Greeting message:
		Composite cmp = toolkit.createComposite(displayPanel);
		cmp.setLayout(new GridLayout());
		{
			Label imgLabel = toolkit.createLabel(cmp, "", SWT.CENTER);
			imgLabel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true));
			imgLabel.setImage(getTitleImage());

			Label helloLabel = toolkit.createLabel(cmp, "Welcome!", SWT.CENTER);
			helloLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		}
		stackLayout.topControl = cmp;
		displayPanel.layout();
	}

	/**
	 * Creates the tool bar items.
	 * 
	 * @param toolBar
	 *            The tool bar.
	 */
	private void createToolBarItems(IToolBarManager toolBar) {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			createToolBarForWindowsOS(toolBar);
		} else {
			createToolBarForNonWindowsOS(toolBar);
		}

		createSpace(toolBar);
		toolBar.add(new Action("Refresh", getRefreshImageDescriptor()) {
			@Override
			public void run() {
				updateView();
			}
		});
		toolBar.update(true);
	}

	/**
	 * Creates tool bar items for Windows operating system.
	 * 
	 * @param toolBar
	 *            The tool bar.
	 */
	private void createToolBarForWindowsOS(IToolBarManager toolBar) {
		toolBar.add(new ControlContribution("rabbit.ui.fromDateTime") {
			@Override
			protected Control createControl(Composite parent) {
				final Calendar dateToBind = preferences.getStartDate();
				final DateTime fromDateTime = new DateTime(parent, SWT.DROP_DOWN | SWT.BORDER);
				fromDateTime.setToolTipText("Select the start date for the data to be displayed");
				updateDateTime(fromDateTime, dateToBind);
				fromDateTime.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event event) {
						updateDate(dateToBind, fromDateTime);
					}
				});
				return fromDateTime;
			}
		});
		createSpace(toolBar);
		toolBar.add(new ControlContribution("rabbit.ui.toDateTime") {
			@Override
			protected Control createControl(Composite parent) {
				final Calendar dateToBind = preferences.getEndDate();
				final DateTime toDateTime = new DateTime(parent, SWT.DROP_DOWN | SWT.BORDER);
				toDateTime.setToolTipText("Select the end date for the data to be displayed");
				updateDateTime(toDateTime, dateToBind);
				toDateTime.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event event) {
						updateDate(dateToBind, toDateTime);
					}
				});
				return toDateTime;
			}
		});
	}

	/**
	 * Creates tool bar items for non windows operating systems.
	 * 
	 * @param toolBar
	 *            The tool bar.
	 */
	private void createToolBarForNonWindowsOS(IToolBarManager toolBar) {
		CalendarAction.create(toolBar, getSite().getShell(),
				preferences.getStartDate(), " From: ", " ");
		CalendarAction.create(toolBar, getSite().getShell(),
				preferences.getEndDate(), " To: ", " ");
	}

	private void createSpace(IToolBarManager toolBar) {
		createString(toolBar, "  ");
	}

	private void createString(IToolBarManager toolBar, final String str) {
		toolBar.add(new ControlContribution(null) {
			@Override
			protected Control createControl(Composite parent) {
				return toolkit.createLabel(parent, str);
			}
		});
	}

	/**
	 * Gets the image descriptor of the refresh image.
	 * 
	 * @return The image descriptor, or null if not found.
	 */
	private ImageDescriptor getRefreshImageDescriptor() {
		return RabbitUI.imageDescriptorFromPlugin(
				RabbitUI.PLUGIN_ID, "resources/refresh.gif");
	}

	/**
	 * Displays the given page.
	 * 
	 * @param page
	 *            The page to display.
	 */
	public void display(IPage page) {
		// Removes the extension tool bar items:
		for (IContributionItem item : extensionToolBar.getItems()) {
			item.setVisible(false);
		}

		Composite cmp = null;
		if (page != null) {

			// Updates the page:
			cmp = pages.get(page);
			if (cmp == null) {
				cmp = toolkit.createComposite(displayPanel);
				cmp.setLayout(new FillLayout());
				page.createContents(cmp);
				pages.put(page, cmp);
			}

			// Updates the extension tool bar items:
			IContributionItem[] items = pageToolItems.get(page);
			if (items == null) {
				items = page.createToolBarItems(extensionToolBar);
				pageToolItems.put(page, items);
			} else {
				for (IContributionItem item : items) {
					item.setVisible(true);
				}
			}

			// Updates the current visible page, mark others as not updated:
			Boolean updated = pageStatus.get(page);
			if (updated == null || updated == false) {
				page.update(preferences);
				pageStatus.put(page, Boolean.TRUE);
			}
		}

		extensionToolBar.update(true);
		stackLayout.topControl = cmp;
		displayPanel.layout();
	}

	/**
	 * Updates the widget with the data from the date.
	 * 
	 * @param widget
	 *            The widget to be updated.
	 * @param date
	 *            The date to get the data from.
	 */
	static void updateDateTime(DateTime widget, Calendar date) {
		widget.setYear(date.get(Calendar.YEAR));
		widget.setMonth(date.get(Calendar.MONTH));
		widget.setDay(date.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Updates the date with the data from the widget.
	 * 
	 * @param date
	 *            The date to be updated.
	 * @param widget
	 *            The widget to get the data from.
	 */
	static void updateDate(Calendar date, DateTime widget) {
		date.set(Calendar.YEAR, widget.getYear());
		date.set(Calendar.MONTH, widget.getMonth());
		date.set(Calendar.DAY_OF_MONTH, widget.getDay());
	}

	/**
	 * Checks whether the two calendars has the same year, month, and day of
	 * month.
	 * 
	 * @param date1
	 *            The calendar.
	 * @param date2
	 *            The other calendar.
	 * @return True if the dates has the same year, month, and day of month,
	 *         false otherwise.
	 */
	static boolean isSameDate(Calendar date1, Calendar date2) {
		return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
				&& date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
				&& date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	/**
	 * Updates the pages to current preference.
	 */
	private void updateView() {
		// Sync with today's data:
		Calendar today = Calendar.getInstance();
		if (isSameDate(today, preferences.getEndDate()) || today.before(preferences.getEndDate())) {
			RabbitCore.getDefault().saveCurrentData();
		}

		// Mark all invisible pages as "not yet updated":
		for (Map.Entry<IPage, Composite> entry : pages.entrySet()) {
			boolean isVisible = stackLayout.topControl == entry.getValue();
			if (isVisible) {
				// update current visible page.
				entry.getKey().update(preferences);
			}
			pageStatus.put(entry.getKey(), Boolean.valueOf(isVisible));
		}
	}

	@Override
	public void setFocus() {
	}
}
