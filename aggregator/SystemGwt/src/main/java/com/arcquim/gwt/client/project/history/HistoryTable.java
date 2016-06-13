package com.arcquim.gwt.client.project.history;

import com.arcquim.gwt.client.project.history.HistoryCompositeWidget.Row;
import com.arcquim.gwt.client.table.DataGridTable;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class HistoryTable extends DataGridTable<Row> {
    
    private static final String ORDER_COLUMN_HEADER = "No";
    private static final String MODEL_COLUMN_HEADER = "Model";
    private static final String PROPERTY_COLUMN_HEADER = "Property";
    private static final String RESULT_COLUMN_HEADER = "Result";
    
    public Column<Row, String> createOrderColumn() {
        return addColumn(new TextCell(), ORDER_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.rowId.toString();
            }
        }, null);
    }
    
    public Column<Row, String> createModelColumn() {
        return addColumn(new TextCell(), MODEL_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.listing;
            }
        }, null);
    }
    
    public Column<Row, String> createPropertyColumn() {
        return addColumn(new TextCell(), PROPERTY_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.property;
            }
        }, null);
    }
    
    public Column<Row, String> createResultColumn() {
        return addColumn(new TextCell(), RESULT_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.result;
            }
        }, null);
    }
    
}
