package educationalcenter.educationalcenter.dialog;

import educationalcenter.educationalcenter.entity.Teacher;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

public class TeacherDialog extends JDialog {
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField patronymicField;
    private JComboBox<String> specializationCombo;
    private JSpinner hireDateSpinner;
    private JTextField hourlyRateField;
    private JTextField experienceField;
    private JTextArea notesArea;
    private JButton saveButton;
    private JButton cancelButton;
    private Teacher teacher;
    private boolean saved = false;

    private final String[] specializations = {
            "Программирование Java",
            "Программирование Python",
            "SQL-разработчик",
            "Веб-разработка",
            "Мобильные приложения",
            "Искусственный интеллект",
            "DevOps",
            "Тестирование QA"
    };

    public TeacherDialog(JFrame parent, String title, Teacher teacher) {
        super(parent, title, true);
        this.teacher = teacher != null ? teacher : new Teacher();
        initComponents();
        if (teacher != null) {
            loadTeacherData();
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

        // Фамилия
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Фамилия:*"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        lastNameField = new JTextField(20);
        mainPanel.add(lastNameField, gbc);

        // Имя
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Имя:*"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(20);
        mainPanel.add(firstNameField, gbc);

        // Отчество
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Отчество:"), gbc);
        gbc.gridx = 1;
        patronymicField = new JTextField(20);
        mainPanel.add(patronymicField, gbc);

        // Специализация (выпадающий список)
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Специализация:*"), gbc);
        gbc.gridx = 1;
        specializationCombo = new JComboBox<>(specializations);
        mainPanel.add(specializationCombo, gbc);

        // Дата трудоустройства
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Дата трудоустройства:*"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel hireDateModel = new SpinnerDateModel();
        hireDateSpinner = new JSpinner(hireDateModel);
        hireDateSpinner.setEditor(new JSpinner.DateEditor(hireDateSpinner, "dd.MM.yyyy"));
        hireDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now()));
        mainPanel.add(hireDateSpinner, gbc);

        // Ставка за час
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Ставка за час (BYN):"), gbc);
        gbc.gridx = 1;
        hourlyRateField = new JTextField(10);
        mainPanel.add(hourlyRateField, gbc);

        // Стаж
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Стаж (лет):"), gbc);
        gbc.gridx = 1;
        experienceField = new JTextField(10);
        mainPanel.add(experienceField, gbc);

        // Многострочное поле для заметок
        /*
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        notesArea = new JTextArea(5, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notesArea);
        scrollPane.setBorder(new TitledBorder("Заметки (многострочное поле)"));
        mainPanel.add(scrollPane, gbc);

         */

        add(mainPanel, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveTeacher());

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(500, 350));
    }

    private void loadTeacherData() {
        lastNameField.setText(teacher.getLastName());
        firstNameField.setText(teacher.getFirstName());
        if (teacher.getPatronymic() != null) {
            patronymicField.setText(teacher.getPatronymic());
        }

        for (int i = 0; i < specializations.length; i++) {
            if (specializations[i].equals(teacher.getSpecialization())) {
                specializationCombo.setSelectedIndex(i);
                break;
            }
        }

        if (teacher.getHireDate() != null) {
            hireDateSpinner.setValue(java.sql.Date.valueOf(teacher.getHireDate()));
        }
        if (teacher.getHourlyRate() != null) {
            hourlyRateField.setText(teacher.getHourlyRate().toString());
        }

        experienceField.setText(String.valueOf(teacher.getExperience()));
    }

    private void saveTeacher() {
        try {
            String lastName = lastNameField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String patronymic = patronymicField.getText().trim();
            String specialization = (String) specializationCombo.getSelectedItem();
            String hourlyRateStr = hourlyRateField.getText().trim();
            String experienceStr = experienceField.getText().trim();

            if (lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Фамилия - обязательное поле!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (firstName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Имя - обязательное поле!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (specialization == null || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Специализация - обязательное поле!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate hireDate = ((java.util.Date) hireDateSpinner.getValue())
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (hireDate.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Дата трудоустройства не может быть в будущем!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int experience = 0;
            if (!experienceStr.isEmpty()) {
                experience = Integer.parseInt(experienceStr);
                if (experience < 0) {
                    JOptionPane.showMessageDialog(this, "Стаж не может быть отрицательным!",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Заполняем объект Teacher
            teacher.setLastName(lastName);
            teacher.setFirstName(firstName);
            teacher.setPatronymic(patronymic.isEmpty() ? null : patronymic);
            teacher.setSpecialization(specialization);
            teacher.setHireDate(hireDate);
            teacher.setExperience(experience);

            if (!hourlyRateStr.isEmpty()) {
                BigDecimal hourlyRate = new BigDecimal(hourlyRateStr);
                if (hourlyRate.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Ставка за час не может быть отрицательной!",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                teacher.setHourlyRate(hourlyRate);
            } else {
                teacher.setHourlyRate(null);
            }

            saved = true;
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Проверьте числовые поля (ставка, стаж)!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }
    public Teacher getTeacher() { return teacher; }
}