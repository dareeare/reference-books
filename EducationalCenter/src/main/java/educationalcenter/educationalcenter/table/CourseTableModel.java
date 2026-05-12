package educationalcenter.educationalcenter.table;

import educationalcenter.educationalcenter.entity.Course;
import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CourseTableModel extends AbstractTableModel {
    private final List<Course> courses;
    private final String[] columns = {
            "Название курса", "Преподаватель", "Дата старта", "Стоимость курса (BYN)", "Количество часов", "Описание"
    };

    public CourseTableModel(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public int getRowCount() {
        return courses.size();
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
        Course course = courses.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return course.getCourseName();
            case 1:
                return course.getTeacher() != null ? course.getTeacher().getFullName() : "Не назначен";
            case 2:
                return course.getStartDate();
            case 3:
                return course.getCourseCost() != null ? course.getCourseCost() : BigDecimal.ZERO;
            case 4:
                return course.getTotalHours() != null ? course.getTotalHours() : 0;
            case 5:
                return course.getDescription() != null ? course.getDescription() : "";
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
        if (columnIndex == 4) {
            return Integer.class;
        }
        if (columnIndex == 5) {
            return String.class;
        }
        return String.class;
    }

    public Course getCourseAt(int rowIndex) {
        return courses.get(rowIndex);
    }

    public void setCourses(List<Course> newCourses) {
        courses.clear();
        courses.addAll(newCourses);
        fireTableDataChanged();
    }

    public void addCourse(Course course) {
        courses.add(course);
        fireTableRowsInserted(courses.size() - 1, courses.size() - 1);
    }

    public void updateCourse(int rowIndex, Course course) {
        courses.set(rowIndex, course);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeCourse(int rowIndex) {
        courses.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}