package gui.detail.explainplan;

public interface ExplainPlanModel {
    int getChildCount(Object node);
    public Object getChild(Object node, int i);
    public boolean isLeaf(Object node);
    public int getColumnCount();
    public String getColumnName(int column);
    public Class<?> getColumnClass(int column);
    public Object getValueAt(Object node, int column);
    public int getIndexOfChild(Object parent, Object child);
}
