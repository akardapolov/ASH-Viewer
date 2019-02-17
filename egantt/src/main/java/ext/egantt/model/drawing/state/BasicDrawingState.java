package ext.egantt.model.drawing.state;

/**
 * Created with IntelliJ IDEA.
 * User: XTender
 * Date: 01.11.13
 * Time: 2:22
 * To change this template use File | Settings | File Templates.
 */
public class BasicDrawingState extends com.egantt.model.drawing.state.BasicDrawingState
        {
    private String textValue;

    public synchronized void setTextValue(String textValue)
    {
        this.textValue = textValue;
    }

    @Override
    public String toString()
    {
        return this.textValue;
    }
}
