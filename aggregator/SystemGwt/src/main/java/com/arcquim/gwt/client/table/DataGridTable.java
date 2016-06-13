package com.arcquim.gwt.client.table;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.DataGrid;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
//import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class DataGridTable<T> extends CellTable<T> {
    
    public static interface GetValue<T, C> {
        C getValue(T row);
    }
    
    public static interface ChangeHandler<T, C> {
        void commitChange(T row, C newValue);
    }
    
    public static interface ButtonClickedHandler<T> {
        void handle(T row);
    }
    
    protected final List<AbstractEditableCell<?, ?>> editableCells = 
            new ArrayList<>();
    
    private final ListDataProvider<T> dataProvider;
    
    public DataGridTable() {
        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(this);
    }
    
    public <C> Column<T, C> addColumn(Cell<C> cell, String headerText,
      final GetValue<T, C> getter, FieldUpdater<T, C> fieldUpdater) {
        Column<T, C> column = new Column<T, C>(cell) {
            @Override
            public C getValue(T object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
        if (cell instanceof AbstractEditableCell<?, ?>) {
            editableCells.add((AbstractEditableCell<?, ?>) cell);
        }
        addColumn(column, headerText);
        return column;
    }
    
    public void addRow(T row) {
        dataProvider.getList().add(row);
        dataProvider.flush();
    }
    
    public void removeRow(T row) {
        dataProvider.getList().remove(row);
        dataProvider.flush();
    }
    
    public void setData(List<T> data) {
        dataProvider.setList(data);
        dataProvider.flush();
    }
    
}
