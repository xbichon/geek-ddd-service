package vip.geekclub.test.internship;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandContext;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.command.IdResult;
import vip.geekclub.framework.exception.BusinessLogicException;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.internship.application.command.thesisselection.CreateThesisSelectionCommand;
import vip.geekclub.internship.application.command.thesisselection.CreateThesisSelectionCommandHandler;
import vip.geekclub.internship.domain.model.Thesis;
import vip.geekclub.internship.domain.model.ThesisSelection;
import vip.geekclub.internship.domain.repository.ThesisRepository;
import vip.geekclub.internship.domain.repository.ThesisSelectionRepository;
import vip.geekclub.internship.domain.value.SelectionType;
import vip.geekclub.internship.domain.value.TeamApplicationValue;
import vip.geekclub.internship.domain.value.TeamMemberValue;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建选题命令处理器单元测试
 * 主要测试按组生成时的逻辑
 */
@SpringBootTest
public class CreateThesisSelectionCommandHandlerTest {

    @Autowired
    private CreateThesisSelectionCommandHandler commandHandler;

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private ThesisSelectionRepository thesisSelectionRepository;

    /**
     * 测试小组形式创建选题成功
     * 成员数量3人（在2-5人范围内）
     */
    @Test
    @Transactional()
    @Rollback(false)
    public void testCreateGroupSelection_Success() {

        UserPrincipal currentUser = new UserPrincipal("cd5bbda6-5494-4cdd-9229-8e43d2895888","student" );
        CommandContext.setCurrentUser(currentUser);

        List<TeamMemberValue> members = List.of(
                new TeamMemberValue(1L, "负责需求分析"),
                new TeamMemberValue(2L, "负责系统设计"),
                new TeamMemberValue(3L, "负责代码开发")
        );

        // 3. 构建结组申请
        TeamApplicationValue teamApplication = new TeamApplicationValue(
                "我们希望共同完成这个课题",
                members
        );

        // 4. 构建命令
        CreateThesisSelectionCommand command = new CreateThesisSelectionCommand(
                1L,
                "论文",
                SelectionType.GROUP,
                teamApplication
        );

        // 5. 执行命令
        commandHandler.execute(command);

        // 6. 验证结果

//        // 7. 验证论文选择人数已更新
//        Optional<Thesis> updatedThesis = thesisRepository.findById(4);
//        assertTrue(updatedThesis.isPresent(), "论文应该存在");
//        assertEquals(1, updatedThesis.get().getCurrentSelections(), "论文选择人数应该为1");
//
//        // 8. 验证选题记录已创建
//        Optional<ThesisSelection> thesisSelection = thesisSelectionRepository.findById(result.data().id());
//        assertTrue(thesisSelection.isPresent(), "选题记录应该存在");
//        assertEquals(SelectionType.GROUP, thesisSelection.get().getSelectionType(), "选择者类型应该是小组");
//        assertEquals(3, thesisSelection.get().getSelectors().size(), "选择者数量应该是3");
    }

}