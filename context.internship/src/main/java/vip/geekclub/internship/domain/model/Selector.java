package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 选择者模型
 * 用于记录选题记录中的选择者信息
 */
@Entity
@Table(name = "internship_selector")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Selector {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生ID
     */
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    /**
     * 选题记录ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_selection_id", nullable = false)
    private ThesisSelection thesisSelection;

    /**
     * 创建选择者
     *
     * @param studentId      学生ID
     * @param thesisSelection 选题记录
     */
    public Selector(Long studentId, ThesisSelection thesisSelection) {
        AssertUtil.notNull(studentId, () -> "学生ID不能为空");
        AssertUtil.notNull(thesisSelection, () -> "选题记录不能为空");

        this.studentId = studentId;
        this.thesisSelection = thesisSelection;
    }
}
