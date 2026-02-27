package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import vip.geekclub.internship.application.query.dto.DailySelectionCountResult;
import vip.geekclub.internship.application.query.dto.InternSelectionStatisticsResult;
import vip.geekclub.internship.application.query.dto.ThesisSelectionCountResult;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.InternTable;
import vip.geekclub.internship.generated.tables.SelectorTable;
import vip.geekclub.internship.generated.tables.ThesisSelectionTable;
import vip.geekclub.internship.generated.tables.ThesisTable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 实习生统计查询服务
 * 用于首页图表数据统计
 */
@Service
@AllArgsConstructor
public class InternStatisticsQueryService {

    private final DSLContext dsl;
    private final InternTable internTable = Tables.Intern;
    private final SelectorTable selectorTable = Tables.Selector;
    private final ThesisTable thesisTable = Tables.Thesis;
    private final ThesisSelectionTable thesisSelectionTable = Tables.ThesisSelection;

    /**
     * 获取实习生选题统计
     *
     * @return 统计结果
     */
    public InternSelectionStatisticsResult findSelectionStatistics() {
        long totalCount = Optional.ofNullable(dsl
                        .selectCount()
                        .from(internTable)
                        .fetchSingleInto(Long.class))
                .orElse(0L);

        long selectedCount = Optional.ofNullable(dsl
                        .selectCount()
                        .from(selectorTable)
                        .fetchSingleInto(Long.class))
                .orElse(0L);

        int completionRate = totalCount == 0
                ? 0
                : (int) Math.round((double) selectedCount * 100 / totalCount);

        long uncompletedCount = totalCount - selectedCount;

        return new InternSelectionStatisticsResult(
                totalCount,
                selectedCount,
                completionRate,
                uncompletedCount
        );
    }

    /**
     * 获取论文选题人数统计列表
     * 按已选人数降序排列
     *
     * @return 论文选题人数统计列表
     */
    public List<ThesisSelectionCountResult> findThesisSelectionCountList() {
        return dsl
                .select(
                        thesisTable.ID,
                        thesisTable.TITLE,
                        thesisTable.CURRENT_SELECTIONS,
                        thesisTable.MAX_SELECTIONS
                )
                .from(thesisTable)
                .orderBy(thesisTable.CURRENT_SELECTIONS.desc())
                .fetchInto(ThesisSelectionCountResult.class);
    }

    /**
     * 获取每日选题数量统计
     * 按日期分组，统计每天的选题记录数
     *
     * @return 每日选题数量统计列表
     */
    public List<DailySelectionCountResult> findDailySelectionCountList() {
        return dsl
                .select(
                        thesisSelectionTable.CREATE_TIME.cast(LocalDate.class).as("date"),
                        DSL.count().as("selectionCount")
                )
                .from(thesisSelectionTable)
                .groupBy(thesisSelectionTable.CREATE_TIME.cast(LocalDate.class))
                .orderBy(DSL.field("date").asc())
                .fetchInto(DailySelectionCountResult.class);
    }
}
