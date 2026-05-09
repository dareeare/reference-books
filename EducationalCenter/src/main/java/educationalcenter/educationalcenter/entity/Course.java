package educationalcenter.educationalcenter.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}