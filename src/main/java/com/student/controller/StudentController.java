package com.student.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.student.model.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {
@Value("#{countryOptions}") 
private Map<String, String> countryOptions = new HashMap<>();

private SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").
addAnnotatedClass(Student.class)
.addAnnotatedClass(StudentDetail.class)
.addAnnotatedClass(Course.class)
.addAnnotatedClass(Review.class)
.addAnnotatedClass(Instructor.class)
.buildSessionFactory();

@InitBinder
public void initBinder(WebDataBinder dataBinder) {
    StringTrimmerEditor editor = new StringTrimmerEditor(true);
    dataBinder.registerCustomEditor(String.class, editor);
}

    @RequestMapping("/showForm")
    public String studentForm(Model model) {
        Student student = new Student();

        model.addAttribute("student", student);
        model.addAttribute("countryOptions", countryOptions); 

        return "student-form";
    }

    @RequestMapping("/processForm")
    public String processForm(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult) throws Exception {
        // System.out.println("Last Name: |" + student.getYear() + "|");
        // System.out.println(bindingResult);
        // System.out.println("=======================================");

        if(bindingResult.hasErrors()) return "student-form";

        String dbUrl = "jdbc:mysql://localhost:3306/student?useSSL=false";
        // String user = "root";
        // String pwd = "accountingsoftwareapp";

        // try {
        //     Class.forName("com.mysql.cj.jdbc.Driver");
        //     Connection conn = DriverManager.getConnection(dbUrl, user, pwd);
        //     System.out.println("Connection successfully");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        Session session = factory.openSession();

        session.beginTransaction();

        session.persist(student);

        System.out.println(student);
        session.getTransaction().commit();
        // session.close();

        // session.beginTransaction();

        // if (student.getYear() != 2)
        // throw new Exception("To test unit of work");

        Student studentInDB = session.get(Student.class, student.getStudentId());

        System.out.println("In DB ------->"+studentInDB);

        // session.getTransaction().commit();

        System.out.println(studentInDB);

        return "student-confirmation";
    }

    @RequestMapping("showAll")
    public void showAllStudents() {
        Session session = factory.openSession();

        session.beginTransaction();

        List<Student> students = session.createQuery("from Student", Student.class).getResultList();

        System.out.println("======= Test session close with lazy loading: ===========");
        System.out.println("abdi: "+students.get(0));
        
        Query<Student> query = session.createQuery(
            "from Student s "+
            "join fetch s.courses c "+
            "join fetch s.studentDetail d "+
            "where s.studentId=:studentId", Student.class);
        query.setParameter("studentId", 1).getSingleResult();

        session.close();
        System.out.println("abdi: "+students.get(0).getStudentDetail());
        System.out.println("======= ===========\n\n");

        System.out.println("======= Get All Students: ===========");
        displayStudents(students);
session = factory.getCurrentSession();
session.beginTransaction();
        students = session.createQuery("from Student s where s.firstName='Abdella'", Student.class).getResultList();
        System.out.println("======= Students Where first name is abdella: ===========");
        displayStudents(students);
        
        students = session.createQuery("from Student s where s.firstName='Abdella' and s.lastName='Nurahmed'", Student.class).getResultList();
        System.out.println("======= Students Where first name abdella and last name nurahmed: ===========");
        displayStudents(students);

        students = session.createQuery("from Student s where s.lastName like '%med'", Student.class).getResultList();
System.out.println("======= Students Where last name like med: ===========");
        displayStudents(students);
    }

    
    @RequestMapping("update")
    public void updateStudent() {
        Session session = factory.openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, 1);

        student.setLastName("NuurAhmed");

        MutationQuery query = session.createMutationQuery("update Student set firstName=:n");
        query.setParameter("n", "sudentId");
        int status = query.executeUpdate();
        System.out.println("================= status " + status + " ==============");

        session.getTransaction().commit();
    }

    @RequestMapping("delete")
    public void deleteStudent() {
        Session session = factory.openSession();
        Student student = session.get(Student.class, 20);

        session.beginTransaction();
        session.remove(student);

        session.createMutationQuery("delete Student where studentId=2").executeUpdate();
        session.getTransaction().commit();
    }

    @RequestMapping("remove_child")
    public void deleteChild() {
        Session session = factory.openSession();
        StudentDetail detail = session.get(StudentDetail.class, 2);
        detail.getStudent().setStudentDetail(null);
        
        try {
        Transaction transaction = session.beginTransaction();
        System.out.println("Student detail "+detail);
        System.out.println("Student  "+detail.getStudent());


            session.remove(detail);

            transaction.commit();
        } finally{
            session.close();
        }
    }

    @RequestMapping("add_course")
    public void addCourse() {
        Session session = factory.getCurrentSession();

        
        Course course1 = new Course("Course one");
        Course course2 = new Course("Course two");

        
        try{
            session.beginTransaction();
            Student student = session.get(Student.class, 1);

            course1.setStudent(student);
            student.add(course2);

        
            session.persist(course1);
            session.persist(course2);

        session.getTransaction().commit();
        } finally{
            session.close();
            factory.close();
        }
    }

    @RequestMapping("add_reviews")
    public void addReview() {
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            Course course = new Course("Advance Hibernate and spring");

            Review review1 = new Review("Good course, well done");
            Review review2 = new Review("Nice Course, I like it");
            Review review3 = new Review("What course is it, you are idiot");

            course.addReview(review1);
            course.addReview(review2);
            course.addReview(review3);

            System.out.println("Saving the course");
            System.out.println("abdi: " + course);
            System.out.println("abdi: " + course.getReviews());

            session.persist(course);

            System.out.println("abdi: " + course);
            System.out.println("abdi: " + course.getReviews());

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }
    }

    @RequestMapping("delete_course")
    public void deleteCourse() {
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            Query<Course> query = session.createQuery("FROM Course c WHERE c.id=:courseId", Course.class);

            query.setParameter("courseId", 3);

            Course course = query.getSingleResult();

            System.out.println("Deleting the course:");
            System.out.println(course);
            System.out.println(course.getReviews());

            session.remove(course);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @RequestMapping("add_instructor")
    public void addInstructor() {
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            // Instructor instructor = new Instructor("John", "john@gmail.com");

            // instructor.addCourse(new Course("Introduction to hibernate and spring"));
            // instructor.addCourse(new Course("Gaming programming"));

            // System.out.println("Saving instructor:");
            // session.persist(instructor);
            // System.out.println("Saved Instructor: "+ instructor);    

            Instructor instructor = new Instructor("John", "john@gmail.com");

            System.out.println("Saving instructor:");
            session.persist(instructor);
            System.out.println("Saved Instructor: "+ instructor);

            Course course1 = new Course("Introduction to hibernate and spring");
            Course course2 = new Course("Gaming programming");

            instructor.addCourse(course1);
            instructor.addCourse(course2);

            System.out.println("Saving courses:");
            session.persist(course1);
            session.persist(course2);
            System.out.println("Saved courses: "+instructor.getCourses());

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println("abd Student: "+student);
            System.out.println("abd detail: "+student.getStudentDetail());
            System.out.println("abd courses: "+student.getCourses());
        }
        System.out.println("\n\n");
    }

}
