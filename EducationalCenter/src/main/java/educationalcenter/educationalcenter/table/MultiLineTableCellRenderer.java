package educationalcenter.educationalcenter.table;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Renders multiline text in a JTable cell (e.g. course description).
 */
public class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {

    public MultiLineTableCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setBorder(new EmptyBorder(2, 4, 2, 4));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        String text = value != null ? String.valueOf(value) : "";
        setText(text);
        setToolTipText(text.isEmpty() ? null : text);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        setFont(table.getFont());
        setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);
        return this;
    }
}
