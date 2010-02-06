package rabbit.ui.internal.pages;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import rabbit.ui.DisplayPreference;
import rabbit.ui.pages.IPage;

/**
 * An empty category page used to contain other pages.
 */
public class EclipseUsagePage implements IPage {

	public EclipseUsagePage() {
	}

	@Override
	public void createContents(Composite parent) {
	}

	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}

	@Override
	public void update(DisplayPreference preference) {
	}

}
