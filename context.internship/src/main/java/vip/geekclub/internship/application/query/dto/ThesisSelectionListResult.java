package vip.geekclub.internship.application.query.dto;

import java.util.List;

/**
 * 论文选择结果列表查询结果
 */
public record ThesisSelectionListResult(
        /*
          选题记录ID
         */
        Long selectionId,

        /*
          论文ID
         */
        Long thesisId,

        /*
          论文标题
         */
        String thesisTitle,

        /*
          成果形式
         */
        String achievementType,

        /*
          选择类型（INDIVIDUAL-个人，GROUP-小组）
         */
        String selectionType,

        /*
          学生姓名
         */
        String studentName,

                /*
          学生学号
         */
        String studentNumber,

        String advisorName,

        /*
          组员
         */
        String groupMembers
) {
}