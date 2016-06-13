package com.arcquim.gwt.client.project.check;

import com.arcquim.gwt.client.project.check.ProjectCheckCompositeWidget.Row;
import com.arcquim.gwt.client.table.DataGridTable;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class AtomicPredicatesTable extends DataGridTable<Row> {
    
    private static final String ORDER_COLUMN_HEADER = "No";
    private static final String PREDICATE_COLUMN_HEADER = "Predicate";
    
    public Column<Row, String> createOrderColumn() {
        return addColumn(new TextCell(), ORDER_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.rowId.toString();
            }
        }, null);
    }
    
    public Column<Row, String> createPredicateColumn() {
        return addColumn(new TextCell(), PREDICATE_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.predicate;
            }
        }, null);
    }
    
}
