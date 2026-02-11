package vip.geekclub.internship.application.query.dto;

import java.util.List;

/**
 * 选题详情查询结果
 */
public record ThesisSelectionDetailResult(
        /**
         * 学生姓名
         */
        String studentName,

        /**
         * 是否结组
         */
        Boolean isGroup,

        /**
         * 指导老师
         */
        String advisorName,

        /**
         * 成果形式
         */
        String achievementType,

        /**
         * 结组信息（仅当isGroup为true时有值）
         */
        TeamInfo teamInfo
) {
    /**
     * 结组信息
     */
    public record TeamInfo(
            /**
             * 结组原因
             */
            String reason,

            /**
             * 组员列表
             */
            List<TeamMemberInfo> members
    ) {
    }

    /**
     * 组员信息
     */
    public record TeamMemberInfo(
            /**
             * 组员姓名
             */
            String name,

            /**
             * 职责
             */
            String responsibility
    ) {
    }
}
