/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Joel_Ward
 */
public class InfoTableModel extends AbstractTableModel
{

    private final Object[][] data;
    private final Object[] columnNames;

    public InfoTableModel(Object[] columnNames, Object[][] data)
    {
        super();
        this.columnNames = columnNames;
        this.data = data;
    }

    @Override
    public int getRowCount()
    {
        return data.length;
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return false;
    }

    @Override
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column].toString();
    }
}
