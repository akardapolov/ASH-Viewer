/*
 *
 */

package ext.egantt.component.holder;

import com.egantt.model.component.ComponentList;
import java.awt.Component;
import javax.swing.*;

// Referenced classes of package ext.egantt.component.holder:
//            SplitComponentHolder

public class SplitLayeredHolder extends JLayeredPane
{

    public SplitLayeredHolder()
    {
        setLayout(new OverlayLayout(this));
        add(holder.getComponent(), JLayeredPane.DEFAULT_LAYER);
    }

    public void setComponentList(ComponentList list)
    {
        holder.setComponentList(list);
    }

    public void setRangeModel(BoundedRangeModel model)
    {
        holder.setRangeModel(model);
    }
/*
    public void setDividerLocation(double proportionalLocation)
    {
        holder.setDividerLocation(proportionalLocation);
    }

    public void setDividerLocation(int location)
    {
        holder.setDividerLocation(location);
    }

    public void setDividerSize(int newSize)
    {
        holder.setDividerSize(newSize);
    }*/

    public Component getComponent()
    {
        return this;
    }

    private static final long serialVersionUID = 0xd36dba3297604e60L;
    protected final SplitComponentHolder holder = new SplitComponentHolder();
}
