package educationalcenter.educationalcenter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Teacher {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherId;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private String patronymic;

    @Column
    private String specialization;

    @Column
    private LocalDate hireDate;

    @Column
    private BigDecimal hourlyRate;

    @Column
    private int experience;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    public String getFullName() {
        if (patronymic != null && !patronymic.isEmpty()) {
            return lastName + " " + firstName + " " + patronymic;
        }
        return lastName + " " + firstName;
    }

    public int getCourseCount() {
        return courses != null ? courses.size() : 0;
    }

}
