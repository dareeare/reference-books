package educationalcenter.educationalcenter.table;

import educationalcenter.educationalcenter.entity.Teacher;
import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TeacherTableModel extends AbstractTableModel {
    private final List<Teacher> teachers;
    private final String[] columns = {"ФИО", "Специализация", "Дата трудоустройства", "Ставка за час (BYN)", "Стаж (лет)", "Кол-во курсов"};

    public TeacherTableModel(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public int getRowCount() {
        return teachers.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Teacher teacher = teachers.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return teacher.getFullName();
            case 1:
                return teacher.getSpecialization();
            case 2:
                return teacher.getHireDate();
            case 3:
                return teacher.getHourlyRate() != null ? teacher.getHourlyRate() : BigDecimal.ZERO;
            case 4:
                return teacher.getExperience();
            case 5:
                return teacher.getCourseCount();
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2) {
            return LocalDate.class;
        }
        if (columnIndex == 3) {
            return BigDecimal.class;
        }
        if (columnIndex == 4 || columnIndex == 5) {
            return Integer.class;
        }
        return String.class;
    }

    public Teacher getTeacherAt(int rowIndex) {
        return teachers.get(rowIndex);
    }

    public void setTeachers(List<Teacher> newTeachers) {
        teachers.clear();
        teachers.addAll(newTeachers);
        fireTableDataChanged();
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        fireTableRowsInserted(teachers.size() - 1, teachers.size() - 1);
    }

    public void updateTeacher(int rowIndex, Teacher teacher) {
        teachers.set(rowIndex, teacher);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeTeacher(int rowIndex) {
        teachers.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}