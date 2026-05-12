package educationalcenter.educationalcenter;

import educationalcenter.educationalcenter.dialog.CourseDialog;
import educationalcenter.educationalcenter.dialog.TeacherDialog;
import educationalcenter.educationalcenter.entity.*;
import educationalcenter.educationalcenter.repository.*;
import educationalcenter.educationalcenter.table.*;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class MainFrame extends JFrame {
    private JComboBox<String> dictionaryCombo;
    private JTable dataTable;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private TeacherRepository teacherRepository;
    private CourseRepository courseRepository;
    private TeacherTableModel teacherModel;
    private CourseTableModel courseModel;
    private List<Teacher> teachers;
    private List<Course> courses;

    private final MultiLineTableCellRenderer courseDescriptionCellRenderer = new MultiLineTableCellRenderer();

    public MainFrame() {
        teacherRepository = new TeacherRepository();
        courseRepository = new CourseRepository();
        initComponents();
        loadDictionaries();
        loadTeachers();

        setTitle("Система управления справочниками");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Верхняя панель с информацией
        JPanel headerPanel = new JPanel(new BorderLayout(0, 8));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        headerPanel.add(topPanel, BorderLayout.NORTH);

        // Панель выбора справочника
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        selectorPanel.setBorder(BorderFactory.createTitledBorder("Выбор справочника"));
        selectorPanel.add(new JLabel("Выберите справочник:"));

        dictionaryCombo = new JComboBox<>();
        dictionaryCombo.setPreferredSize(new Dimension(200, 25));
        dictionaryCombo.addActionListener(e -> {
            if ("Преподаватели".equals(dictionaryCombo.getSelectedItem())) {
                loadTeachers();
            } else {
                loadCourses();
            }
        });
        selectorPanel.add(dictionaryCombo);

        refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(e -> refreshTable());
        selectorPanel.add(refreshButton);

        headerPanel.add(selectorPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Таблица
        dataTable = new JTable();
        dataTable.setRowHeight(25);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        dataTable.getTableHeader().setBackground(new Color(240, 240, 240));
        configureRenderers();

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Данные справочника"));
        add(scrollPane, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Добавить");
        addButton.setBackground(new Color(50, 205, 50));
        addButton.addActionListener(e -> addRecord());

        editButton = new JButton("Редактировать");
        editButton.setBackground(new Color(255, 215, 0));
        editButton.addActionListener(e -> editRecord());

        deleteButton = new JButton("Удалить");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.addActionListener(e -> deleteRecord());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDictionaries() {
        dictionaryCombo.addItem("Преподаватели");
        dictionaryCombo.addItem("Курсы обучения");
    }

    private void loadTeachers() {
        try {
            teachers = teacherRepository.findAll();
            teacherModel = new TeacherTableModel(teachers);
            dataTable.setModel(teacherModel);

            TableRowSorter<TeacherTableModel> sorter = new TableRowSorter<>(teacherModel);
            configureTeacherSorting(sorter);
            dataTable.setRowSorter(sorter);
            configureRenderers();
            dataTable.setRowHeight(25);
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки преподавателей: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCourses() {
        try {
            courses = courseRepository.findAllActive();
            courseModel = new CourseTableModel(courses);
            dataTable.setModel(courseModel);

            TableRowSorter<CourseTableModel> sorter = new TableRowSorter<>(courseModel);
            configureCourseSorting(sorter);
            dataTable.setRowSorter(sorter);
            configureRenderers();
            configureCourseTableColumns();
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки курсов: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configureRenderers() {
        dataTable.setDefaultRenderer(LocalDate.class, (table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel();
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            } else {
                label.setBackground(table.getBackground());
                label.setForeground(table.getForeground());
            }
            if (value instanceof LocalDate) {
                LocalDate date = (LocalDate) value;
                label.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            } else {
                label.setText("");
            }
            return label;
        });
    }

    /** Колонка «Описание» — многострочное отображение и увеличенная высота строк. */
    private void configureCourseTableColumns() {
        TableColumnModel cm = dataTable.getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++) {
            TableColumn tc = cm.getColumn(i);
            if (tc.getModelIndex() == 5) {
                tc.setCellRenderer(courseDescriptionCellRenderer);
                tc.setPreferredWidth(220);
                break;
            }
        }
        dataTable.setRowHeight(44);
    }

    private void configureTeacherSorting(TableRowSorter<TeacherTableModel> sorter) {
        Comparator<String> textComparator = Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER);
        Comparator<LocalDate> dateComparator = Comparator.nullsLast(Comparator.naturalOrder());
        Comparator<Number> numberComparator = Comparator.nullsLast(
                Comparator.comparingDouble(Number::doubleValue)
        );

        sorter.setComparator(0, textComparator);
        sorter.setComparator(1, textComparator);
        sorter.setComparator(2, dateComparator);
        sorter.setComparator(3, numberComparator);
        sorter.setComparator(4, numberComparator);
        sorter.setComparator(5, numberComparator);
    }

    private void configureCourseSorting(TableRowSorter<CourseTableModel> sorter) {
        Comparator<String> textComparator = Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER);
        Comparator<LocalDate> dateComparator = Comparator.nullsLast(Comparator.naturalOrder());
        Comparator<BigDecimal> decimalComparator = Comparator.nullsLast(Comparator.naturalOrder());
        Comparator<Integer> intComparator = Comparator.nullsLast(Comparator.naturalOrder());

        sorter.setComparator(0, textComparator);
        sorter.setComparator(1, textComparator);
        sorter.setComparator(2, dateComparator);
        sorter.setComparator(3, (a, b) -> decimalComparator.compare((BigDecimal) a, (BigDecimal) b));
        sorter.setComparator(4, (a, b) -> intComparator.compare((Integer) a, (Integer) b));
        sorter.setComparator(5, textComparator);
    }

    private void refreshTable() {
        if ("Преподаватели".equals(dictionaryCombo.getSelectedItem())) {
            loadTeachers();
        } else {
            loadCourses();
        }
    }

    private void addRecord() {
        if ("Преподаватели".equals(dictionaryCombo.getSelectedItem())) {
            TeacherDialog dialog = new TeacherDialog(this, "Добавление преподавателя", null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    teacherRepository.save(dialog.getTeacher());
                    loadTeachers();
                    JOptionPane.showMessageDialog(this, "Преподаватель успешно добавлен!",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ошибка при добавлении: " + e.getMessage(),
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            try {
                List<Teacher> teacherList = teacherRepository.findAll();
                CourseDialog dialog = new CourseDialog(this, "Добавление курса", null, teacherList);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    courseRepository.save(dialog.getCourse());
                    loadCourses();
                    JOptionPane.showMessageDialog(this, "Курс успешно добавлен!",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка при добавлении: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editRecord() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите запись для редактирования!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Преподаватели".equals(dictionaryCombo.getSelectedItem())) {
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            Teacher teacher = teacherModel.getTeacherAt(modelRow);
            TeacherDialog dialog = new TeacherDialog(this, "Редактирование преподавателя", teacher);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                try {
                    teacherRepository.save(dialog.getTeacher());
                    loadTeachers();
                    JOptionPane.showMessageDialog(this, "Преподаватель успешно обновлен!",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ошибка при обновлении: " + e.getMessage(),
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            Course course = courseModel.getCourseAt(modelRow);
            try {
                List<Teacher> teacherList = teacherRepository.findAll();
                CourseDialog dialog = new CourseDialog(this, "Редактирование курса", course, teacherList);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    courseRepository.save(dialog.getCourse());
                    loadCourses();
                    JOptionPane.showMessageDialog(this, "Курс успешно обновлен!",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка при обновлении: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteRecord() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите запись для удаления!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить эту запись?\nПри удалении преподавателя связанные курсы удалятся.",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if ("Преподаватели".equals(dictionaryCombo.getSelectedItem())) {
                    int modelRow = dataTable.convertRowIndexToModel(selectedRow);
                    Teacher teacher = teacherModel.getTeacherAt(modelRow);
                    teacherRepository.deleteById(teacher.getTeacherId());
                    loadTeachers();
                    JOptionPane.showMessageDialog(this, "Преподаватель успешно удален!",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int modelRow = dataTable.convertRowIndexToModel(selectedRow);
                    Course course = courseModel.getCourseAt(modelRow);
                    courseRepository.softDelete(course.getCourseId());
                    loadCourses();
                    JOptionPane.showMessageDialog(this, "Курс успешно удален (мягкое удаление)!",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка при удалении: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}