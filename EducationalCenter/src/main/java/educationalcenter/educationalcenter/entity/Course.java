package educationalcenter.educationalcenter.entity;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_name", nullable = false, length = 200)
    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "course_cost", precision = 10, scale = 2)
    private BigDecimal courseCost;

    @Column(name = "total_hours")
    private Integer totalHours;

    /** Многострочное описание курса */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Course() {
    }

    public Course(Integer courseId, String courseName, Teacher teacher, LocalDate startDate,
                  BigDecimal courseCost, Integer totalHours, String description, Boolean isDeleted) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacher = teacher;
        this.startDate = startDate;
        this.courseCost = courseCost;
        this.totalHours = totalHours;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getCourseCost() {
        return courseCost;
    }

    public void setCourseCost(BigDecimal courseCost) {
        this.courseCost = courseCost;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}