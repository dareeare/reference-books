package educationalcenter.educationalcenter.dialog;

import educationalcenter.educationalcenter.entity.Course;
import educationalcenter.educationalcenter.entity.Teacher;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class CourseDialog extends JDialog {
    private JTextField courseNameField;
    private JComboBox<Teacher> teacherCombo;
    private JSpinner startDateSpinner;
    private JCheckBox hasStartDateCheck;
    private JTextField courseCostField;
    private JTextField totalHoursField;
    private JTextArea descriptionArea;
    private JButton saveButton;
    private JButton cancelButton;
    private Course course;
    private List<Teacher> teachers;
    private boolean saved = false;

    public CourseDialog(JFrame parent, String title, Course course, List<Teacher> teachers) {
        super(parent, title, true);
        this.course = course != null ? course : new Course();
        this.teachers = teachers;
        initComponents();
        if (course != null && course.getCourseId() != null) {
            loadCourseData();
        }
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Название курса
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Название курса:*"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        courseNameField = new JTextField(25);
        mainPanel.add(courseNameField, gbc);

        // Преподаватель (выпадающий список с хранением ID)
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Преподаватель:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        teacherCombo = new JComboBox<>();
        teacherCombo.addItem(null);
        for (Teacher teacher : teachers) {
            teacherCombo.addItem(teacher);
        }
        mainPanel.add(teacherCombo, gbc);

        // Дата старта
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Дата старта:"), gbc);
        gbc.gridx = 1;
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hasStartDateCheck = new JCheckBox("Указать дату");
        hasStartDateCheck.setSelected(false);
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd.MM.yyyy"));
        startDateSpinner.setEnabled(false);
        startDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now()));
        hasStartDateCheck.addActionListener(e -> startDateSpinner.setEnabled(hasStartDateCheck.isSelected()));
        startDatePanel.add(hasStartDateCheck);
        startDatePanel.add(Box.createHorizontalStrut(10));
        startDatePanel.add(startDateSpinner);
        mainPanel.add(startDatePanel, gbc);

        // Стоимость курса
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Стоимость курса (BYN):"), gbc);
        gbc.gridx = 1;
        courseCostField = new JTextField(10);
        mainPanel.add(courseCostField, gbc);

        // Количество часов
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Количество часов:"), gbc);
        gbc.gridx = 1;
        totalHoursField = new JTextField(10);
        mainPanel.add(totalHoursField, gbc);

        // Многострочное поле для описания
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(new TitledBorder("Описание курса (многострочное поле)"));
        mainPanel.add(scrollPane, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveCourse());

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(500, 500));
    }

    private void loadCourseData() {
        courseNameField.setText(course.getCourseName());
        if (course.getTeacher() != null) {
            teacherCombo.setSelectedItem(course.getTeacher());
        }
        if (course.getStartDate() != null) {
            hasStartDateCheck.setSelected(true);
            startDateSpinner.setEnabled(true);
            startDateSpinner.setValue(java.sql.Date.valueOf(course.getStartDate()));
        }
        if (course.getCourseCost() != null) {
            courseCostField.setText(course.getCourseCost().toString());
        }
        if (course.getTotalHours() != null) {
            totalHoursField.setText(String.valueOf(course.getTotalHours()));
        }
        if (course.getDescription() != null) {
            descriptionArea.setText(course.getDescription());
        }
    }

    private void saveCourse() {
        try {
            String courseName = courseNameField.getText().trim();
            Teacher selectedTeacher = (Teacher) teacherCombo.getSelectedItem();
            String courseCostStr = courseCostField.getText().trim();
            String totalHoursStr = totalHoursField.getText().trim();

            if (courseName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Название курса - обязательное поле!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate startDate = null;
            if (hasStartDateCheck.isSelected()) {
                startDate = ((java.util.Date) startDateSpinner.getValue())
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }

            BigDecimal courseCost = null;
            if (!courseCostStr.isEmpty()) {
                courseCost = new BigDecimal(courseCostStr);
                if (courseCost.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Стоимость курса не может быть отрицательной!",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Integer totalHours = null;
            if (!totalHoursStr.isEmpty()) {
                totalHours = Integer.parseInt(totalHoursStr);
                if (totalHours <= 0) {
                    JOptionPane.showMessageDialog(this, "Количество часов должно быть больше нуля!",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            course.setCourseName(courseName);
            course.setTeacher(selectedTeacher);
            course.setStartDate(startDate);
            course.setCourseCost(courseCost);
            course.setTotalHours(totalHours);
            String desc = descriptionArea.getText().trim();
            course.setDescription(desc.isEmpty() ? null : desc);

            saved = true;
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Проверьте числовые поля!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }
    public Course getCourse() { return course; }
}