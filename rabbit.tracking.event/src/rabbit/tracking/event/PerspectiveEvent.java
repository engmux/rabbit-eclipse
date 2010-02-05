package rabbit.tracking.event;

import java.util.Calendar;

import org.eclipse.ui.IPerspectiveDescriptor;

/**
 * Represents a perspective event.
 */
public class PerspectiveEvent extends ContinuousEvent {

	private IPerspectiveDescriptor perspective;

	/**
	 * Constructs a perspective event.
	 * 
	 * @param time The end time of the event.
	 * @param duration The duration in milliseconds.
	 * @param p The perspective.
	 */
	public PerspectiveEvent(Calendar time, long duration, IPerspectiveDescriptor p) {
		super(time, duration);
		setPerspective(p);
	}

	/**
	 * Gets the perspective.
	 * 
	 * @return The perspective.
	 */
	public IPerspectiveDescriptor getPerspective() {
		return perspective;
	}

	/**
	 * Sets the perspective. TODO: check for null?
	 * 
	 * @param perspective The perspective.
	 */
	public void setPerspective(IPerspectiveDescriptor perspective) {
		this.perspective = perspective;
	}

}