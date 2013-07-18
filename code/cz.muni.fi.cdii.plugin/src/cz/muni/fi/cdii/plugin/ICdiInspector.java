package cz.muni.fi.cdii.plugin;

import org.eclipse.e4.ui.di.UIEventTopic;
import org.osgi.service.event.EventHandler;

import cz.muni.fi.cdii.plugin.common.model.CdiInspection;

/**
 * Interface for classes consuming {@link CdiInspection#INSPECT_TOPIC} e4 events.
 * <br>
 * Extension of {@link EventHandler} is hack to overcome malfunctioning {@link UIEventTopic}
 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=412554"> https://bugs.eclipse.org/bugs/show_bug.cgi?id=412554</a>
 */
public interface ICdiInspector extends EventHandler {
	
	public void inspect(CdiInspection inspection);
}