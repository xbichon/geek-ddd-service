package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.ThesisAchievementTypeTable;
import vip.geekclub.internship.generated.tables.ThesisTable;
import org.springframework.stereotype.Service;
import vip.geekclub.internship.application.query.dto.ThesisItemResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 论文查询服务
 */
@Service
@AllArgsConstructor
public class ThesisQueryService {

    private final DSLContext dslContext;
    private final ThesisTable thesisTable = Tables.Thesis;
    private final ThesisAchievementTypeTable achievementTypeTable = Tables.ThesisAchievementType;

    /**
     * 获取论文列表
     * 返回论文及论文的成果集合，一条论文一条记录，成果为一个集合
     *
     * @return 论文列表
     */
    public List<ThesisItemResult> getThesisList() {
        // 查询所有论文
        var thesisRecords = dslContext
                .select(thesisTable.ID, thesisTable.TITLE, thesisTable.MAX_SELECTIONS, thesisTable.CURRENT_SELECTIONS)
                .from(thesisTable)
                .fetch();

        // 查询所有论文的成果形式
        var achievementRecords = dslContext
                .select(achievementTypeTable.THESIS_ID, achievementTypeTable.TYPE)
                .from(achievementTypeTable)
                .fetch();

        // 按论文ID分组成果形式
        var achievementMap = achievementRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> record.get(achievementTypeTable.THESIS_ID),
                        Collectors.mapping(
                                record -> record.get(achievementTypeTable.TYPE),
                                Collectors.toList()
                        )
                ));

        // 组装结果并排序
        return thesisRecords.stream()
                .map(record -> {
                    Long thesisId = record.get(thesisTable.ID);
                    List<String> types = achievementMap.getOrDefault(thesisId, List.of());
                    return new ThesisItemResult(
                            thesisId,
                            record.get(thesisTable.TITLE),
                            record.get(thesisTable.MAX_SELECTIONS),
                            record.get(thesisTable.CURRENT_SELECTIONS),
                            types
                    );
                })
                .sorted((thesis1, thesis2) -> {
                    // 判断是否已选满：currentSelections >= maxSelections
                    boolean isFull1 = thesis1.currentSelections() >= thesis1.maxSelections();
                    boolean isFull2 = thesis2.currentSelections() >= thesis2.maxSelections();
                    
                    // 已选满的排在后面
                    if (isFull1 && !isFull2) {
                        return 1; // thesis1 排在后面
                    } else if (!isFull1 && isFull2) {
                        return -1; // thesis2 排在后面
                    } else {
                        return 0; // 两者相同，保持原有顺序
                    }
                })
                .toList();
    }
}