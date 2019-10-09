/*
 *
 */

package ext.egantt.component.holder;

import com.egantt.model.component.ComponentList;
import com.egantt.swing.pane.basic.SplitScrollPane;
import ext.egantt.component.ComponentHolder;
import java.awt.Component;
import javax.swing.BoundedRangeModel;
import javax.swing.JSplitPane;

public class SplitComponentHolder extends SplitScrollPane
    implements ComponentHolder
{

    public SplitComponentHolder()
    {
        super(1);
    }

    public void setComponentList(ComponentList list)
    {
        setComponent(list.get(0));
        //setRightComponent(list.get(1));
    }

    public void setRangeModel(BoundedRangeModel model)
    {
        super.setRangeModel(model);
    }

    public Component getComponent()
    {
        return this;
    }

    private static final long serialVersionUID = 0x640484338e10888aL;
}
