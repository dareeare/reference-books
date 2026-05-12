package educationalcenter.educationalcenter.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

@Table(name = "Teachers")
@Entity
public class Teacher {
    @Id
    @Column(name = "teacher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "experience")
    private int experience;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(int teacherId, String lastName, String firstName, String patronymic, String specialization,
                   LocalDate hireDate, BigDecimal hourlyRate, int experience, List<Course> courses) {
        this.teacherId = teacherId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.hourlyRate = hourlyRate;
        this.experience = experience;
        this.courses = courses;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getFullName() {
        if (patronymic != null && !patronymic.isEmpty()) {
            return new StringBuilder(lastName).append(" ").append(firstName).append(" ").append(patronymic).toString();
        }
        return new StringBuilder(lastName).append(" ").append(firstName).toString();
    }

    public int getCourseCount() {
        return courses != null ? courses.size() : 0;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return teacherId == teacher.teacherId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId);
    }

}
