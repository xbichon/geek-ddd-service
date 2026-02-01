package vip.geekclub.education.thesis.domain.model;
import jakarta.persistence.*;

@Entity
@Table(name = "education_student")
public class Student {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * 姓名
     */
    public String name;

    /**
     * 学号
     */
    private String studentId;
    
    /**
     * 年级/届
     */
    private Integer grade;
    
    /**
     * 专业
     */
    private String major;
    
    /**
     * 班级
     */
    private String className;

}