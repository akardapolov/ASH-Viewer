/*
 *
 */

package ext.egantt.model.drawing;

import com.egantt.model.component.ComponentManager;
import com.egantt.swing.scroll.ScrollManager;

public interface ComponentUtilities
{

    public abstract ComponentManager getManager();

    public abstract ScrollManager getScrollManager(int i);
}
