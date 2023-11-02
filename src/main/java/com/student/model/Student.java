package com.student.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.student.validator.Id;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="student")
@Getter
@Setter
@ToString
public class Student {
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    @jakarta.persistence.Id
    @Column(name="student_id")
    private int studentId;

    @Column(name="fist_name")
    private String firstName;

    @Column(name="last_name")
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String lastName;

    @Column(name="year")
    @NotNull(message = "is required")
    @Min(value = 1, message = "must be greater than or equal to 1")
    @Max(value = 4, message = "must be less than or equal to 4")
    private Integer year;

    @Column(name = "id")
    @GeneratedValue(strategy  = GenerationType.AUTO)
    @Id(value = {"TOP", "STD"}, message = "must start with TOP or STD")
    @Pattern(regexp = "^[a-zA-Z0-9]{5}", message = "only 5 char/digit")
    private String id;

    @Column(name="country")
    private String country;

    @Transient
    private LinkedHashMap<String, String> countryOptions;

    @Column(name="favorite_language")
    private String favoriteLanguage;

    @Transient
    private String[] operatingSystems;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_detail_id")
    private StudentDetail studentDetail;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Course> courses;

    public void add(Course course) {
        if (courses == null) courses = new ArrayList<>();

        this.courses.add(course);

        course.setStudent(this);        
    }

    public Student() {
        countryOptions = new LinkedHashMap<>();

        countryOptions.put("ET", "Ethiopia");
        countryOptions.put("SUD", "Sudan");
        countryOptions.put("TUR", "Turkey");

    }

}
