package com.arcquim.gwt.client.project;

import com.arcquim.gwt.client.project.ProjectsCompositeWidget.Row;
import com.arcquim.gwt.client.table.DataGridTable;
import com.github.gwtbootstrap.client.ui.ButtonCell;
//import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectsTable extends DataGridTable<Row> {
    
    private static final String ORDER_COLUMN_HEADER = "No";
    private static final String TITLE_COLUMN_HEADER = "Title";
    private static final String STATUS_COLUMN_HEADER = "Status";
    
    public static final String ACTIVE_PROJECT = "Active";
    public static final String INACTIVE_PROJECT = "Inactive";
    
    private static final String CHECK_COLUMN = "Check";
    private static final String HISTORY_COLUMN = "View history";
    
    private static final List<String> OPTIONS;
    
    static {
        OPTIONS = new ArrayList<>();
        OPTIONS.add(ACTIVE_PROJECT);
        OPTIONS.add(INACTIVE_PROJECT);
    }
    
    public static interface RowChangeHandler<C> extends ChangeHandler<Row, C> {
        
    }
    
    public static interface RowStringChangeHandler extends RowChangeHandler<String> {
        
    }
    
    public static interface RowButtonClickedHandler extends ButtonClickedHandler<Row> {
        
    }
    
    public Column<Row, String> createOrderColumn() {
        return addColumn(new TextCell(), ORDER_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.rowId.toString();
            }
        }, null);
    }
    
    public Column<Row, String> createTitleColumn(final RowStringChangeHandler handler) {
        return addColumn(new EditTextCell(), TITLE_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.title;
            }
        }, new FieldUpdater<Row, String>() {
            @Override
            public void update(int index, Row object, String value) {
                handler.commitChange(object, value);
            }
        });
    }
    
    public Column<Row, String> createStatusColumn(final RowStringChangeHandler handler) {
        return addColumn(new SelectionCell(OPTIONS), STATUS_COLUMN_HEADER, new GetValue<Row, String>() {
            @Override
            public String getValue(Row row) {
                return row.active ? ACTIVE_PROJECT : INACTIVE_PROJECT;
            }
        }, new FieldUpdater<Row, String>() {
            @Override
            public void update(int index, Row object, String value) {
                handler.commitChange(object, value);
            }
        });
    }
    
    public Column<Row, String> createRunCheckColumn(final RowButtonClickedHandler handler) {
        return addColumn(new ButtonCell(), CHECK_COLUMN, new GetValue<Row, String>() {
                @Override
                public String getValue(Row row) {
                    return CHECK_COLUMN;
                }
            }, new FieldUpdater<Row, String>() {
                @Override
                public void update(int index, Row object, String value) {
                    handler.handle(object);
                }
        });
    }
    
    public Column<Row, String> createViewHistoryColumn(final RowButtonClickedHandler handler) {
        return addColumn(new ButtonCell(), HISTORY_COLUMN, new GetValue<Row, String>() {
                @Override
                public String getValue(Row row) {
                    return HISTORY_COLUMN;
                }
            }, new FieldUpdater<Row, String>() {
                @Override
                public void update(int index, Row object, String value) {
                    handler.handle(object);
                }
        });
    }
    
}
