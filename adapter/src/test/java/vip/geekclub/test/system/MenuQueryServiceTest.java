//package vip.geekclub.test.system;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import vip.geekclub.system.application.query.MenuQueryService;
//import vip.geekclub.system.application.query.dao.MenuDao;
//import vip.geekclub.system.application.query.dsl.tables.pojos.MenuDto;
//import vip.geekclub.system.application.dto.MenuTreeResult;
//import vip.geekclub.system.domain.model.MenuType;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class MenuQueryServiceTest {
//
//    @Mock
//    private MenuDao menuDao;
//
//    @InjectMocks
//    private MenuQueryService menuQueryService;
//
//    @Test
//    public void should_return_empty_list_when_no_menus_exist() {
//        // Given
//        when(menuDao.findEnabledMenus()).thenReturn(Collections.emptyList());
//        Set<String> permissionCodes = Set.of("user:read", "user:write");
//
//        // When
//        List<MenuTreeResult> result = menuQueryService.getMenuTree(permissionCodes);
//
//        // Then
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    public void should_return_menu_tree_with_proper_hierarchy() {
//        // Given
//        List<MenuDto> mockMenus = createMockMenuData();
//        when(menuDao.findEnabledMenus()).thenReturn(mockMenus);
//        Set<String> permissionCodes = Set.of("system:read", "user:read", "user:write", "role:read");
//
//        // When
//        List<MenuTreeResult> result = menuQueryService.getMenuTree(permissionCodes);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(2, result.size()); // 系统管理 和 用户管理 两个根目录
//
//        // 验证系统管理目录
//        MenuTreeResult systemMenu = result.stream()
//                .filter(menu -> "系统管理".equals(menu.name()))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(systemMenu);
//        assertEquals(MenuType.DIRECTORY.toString(), systemMenu.type());
//        assertEquals(1, systemMenu.level());
//        assertEquals(2, systemMenu.children().size()); // 用户管理 和 角色管理
//
//        // 验证用户管理子目录
//        MenuTreeResult userManagement = systemMenu.children().stream()
//                .filter(menu -> "用户管理".equals(menu.name()))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(userManagement);
//        assertEquals(MenuType.DIRECTORY.toString(), userManagement.type());
//        assertEquals(2, userManagement.level());
//        assertEquals(1, userManagement.children().size()); // 用户列表菜单
//
//        // 验证用户列表菜单
//        MenuTreeResult userList = userManagement.children().get(0);
//        assertEquals("用户列表", userList.name());
//        assertEquals(MenuType.MENU.toString(), userList.type());
//        assertEquals(3, userList.level());
//        assertEquals("/system/user", userList.path());
//        assertTrue(userList.children().isEmpty()); // 菜单类型没有子节点
//    }
//
//    @Test
//    public void should_filter_menus_by_permission_codes() {
//        // Given
//        List<MenuDto> mockMenus = createMockMenuData();
//        when(menuDao.findEnabledMenus()).thenReturn(mockMenus);
//        Set<String> limitedPermissions = Set.of("user:read"); // 只有用户读权限
//
//        // When
//        List<MenuTreeResult> result = menuQueryService.getMenuTree(limitedPermissions);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1, result.size()); // 只有系统管理根目录
//
//        MenuTreeResult systemMenu = result.get(0);
//        assertEquals("系统管理", systemMenu.name());
//        assertEquals(1, systemMenu.children().size()); // 只有用户管理子目录
//
//        MenuTreeResult userManagement = systemMenu.children().get(0);
//        assertEquals("用户管理", userManagement.name());
//        assertEquals(1, userManagement.children().size()); // 只有用户列表菜单
//    }
//
//    @Test
//    public void should_remove_empty_directories() {
//        // Given
//        List<MenuDto> mockMenus = createMockMenuDataWithEmptyDirectory();
//        when(menuDao.findEnabledMenus()).thenReturn(mockMenus);
//        Set<String> permissionCodes = Set.of("user:read");
//
//        // When
//        List<MenuTreeResult> result = menuQueryService.getMenuTree(permissionCodes);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1, result.size()); // 空的角色管理目录应该被移除
//
//        MenuTreeResult systemMenu = result.get(0);
//        assertEquals("系统管理", systemMenu.name());
//        assertEquals(1, systemMenu.children().size()); // 只有用户管理，角色管理被移除
//        assertEquals("用户管理", systemMenu.children().get(0).name());
//    }
//
//    @Test
//    public void should_handle_menus_without_permission_codes() {
//        // Given
//        List<MenuDto> mockMenus = createMockMenuDataWithoutPermissions();
//        when(menuDao.findEnabledMenus()).thenReturn(mockMenus);
//        Set<String> permissionCodes = Set.of(); // 空权限集合
//
//        // When
//        List<MenuTreeResult> result = menuQueryService.getMenuTree(permissionCodes);
//
//        // Then
//        assertNotNull(result);
//        assertFalse(result.isEmpty()); // 没有权限码的菜单应该显示
//    }
//
//    /**
//     * 创建模拟菜单数据 - 4级层次结构
//     * 系统管理(1级目录)
//     *   ├── 用户管理(2级目录)
//     *   │   └── 用户列表(3级菜单)
//     *   └── 角色管理(2级目录)
//     *       └── 角色列表(3级菜单)
//     * 报表管理(1级目录)
//     *   └── 数据报表(2级菜单)
//     */
//    private List<MenuDto> createMockMenuData() {
//        List<MenuDto> menus = new ArrayList<>();
//
//        // 1级目录 - 系统管理
//        MenuDto systemManagement = createMenuDto(1L, "系统管理", MenuType.DIRECTORY, null, 1,
//                null, "system", 1, "");
//        menus.add(systemManagement);
//
//        // 1级目录 - 报表管理
//        MenuDto reportManagement = createMenuDto(2L, "报表管理", MenuType.DIRECTORY, null, 1,
//                null, "report", 2, null);
//        menus.add(reportManagement);
//
//        // 2级目录 - 用户管理
//        MenuDto userManagement = createMenuDto(3L, "用户管理", MenuType.DIRECTORY, 1L, 2,
//                null, "user", 1, null);
//        menus.add(userManagement);
//
//        // 2级目录 - 角色管理
//        MenuDto roleManagement = createMenuDto(4L, "角色管理", MenuType.DIRECTORY, 1L, 2,
//                null, "role", 2, null);
//        menus.add(roleManagement);
//
//        // 2级菜单 - 数据报表
//        MenuDto dataReport = createMenuDto(5L, "数据报表", MenuType.MENU, 2L, 2,
//                "/report/data", "chart", 1, "report:read");
//        menus.add(dataReport);
//
//        // 3级菜单 - 用户列表
//        MenuDto userList = createMenuDto(6L, "用户列表", MenuType.MENU, 3L, 3,
//                "/system/user", "user", 1, "user:read");
//        menus.add(userList);
//
//        // 3级菜单 - 角色列表
//        MenuDto roleList = createMenuDto(7L, "角色列表", MenuType.MENU, 4L, 3,
//                "/system/role", "role", 1, "role:read");
//        menus.add(roleList);
//
//        return menus;
//    }
//
//    /**
//     * 创建包含空目录的模拟菜单数据
//     */
//    private List<MenuDto> createMockMenuDataWithEmptyDirectory() {
//        List<MenuDto> menus = new ArrayList<>();
//
//        // 1级目录 - 系统管理
//        MenuDto systemManagement = createMenuDto(1L, "系统管理", MenuType.DIRECTORY, null, 1,
//                null, "system", 1, null);
//        menus.add(systemManagement);
//
//        // 2级目录 - 用户管理
//        MenuDto userManagement = createMenuDto(2L, "用户管理", MenuType.DIRECTORY, 1L, 2,
//                null, "user", 1, null);
//        menus.add(userManagement);
//
//        // 2级目录 - 角色管理 (空目录，没有子菜单)
//        MenuDto roleManagement = createMenuDto(3L, "角色管理", MenuType.DIRECTORY, 1L, 2,
//                null, "role", 2, null);
//        menus.add(roleManagement);
//
//        // 3级菜单 - 用户列表
//        MenuDto userList = createMenuDto(4L, "用户列表", MenuType.MENU, 2L, 3,
//                "/system/user", "user", 1, "user:read");
//        menus.add(userList);
//
//        // 注意：角色管理目录下没有任何菜单，应该被移除
//
//        return menus;
//    }
//
//    /**
//     * 创建没有权限码的模拟菜单数据
//     */
//    private List<MenuDto> createMockMenuDataWithoutPermissions() {
//        List<MenuDto> menus = new ArrayList<>();
//
//        // 1级目录 - 首页
//        MenuDto dashboard = createMenuDto(1L, "首页", MenuType.DIRECTORY, null, 1,
//                null, "dashboard", 1, null);
//        menus.add(dashboard);
//
//        // 2级菜单 - 工作台
//        MenuDto workspace = createMenuDto(2L, "工作台", MenuType.MENU, 1L, 2,
//                "/dashboard/workspace", "workspace", 1, null);
//        menus.add(workspace);
//
//        return menus;
//    }
//
//    /**
//     * 创建MenuDto辅助方法
//     */
//    private MenuDto createMenuDto(Long id, String name, MenuType type, Long parentId,
//                                 Integer level, String path, String icon, Integer sortOrder,
//                                 String permissionCode) {
//        return new MenuDto(
//            id,                    // id
//            name,                  // name
//            path,                  // path
//            icon,                  // icon
//            sortOrder,             // sortOrder
//            parentId,              // parentId
//            level,                 // level
//            type.toString(),       // type
//            (byte) 1,              // enabled
//            permissionCode,        // permissionCode
//            null,                  // description
//            null,                  // version
//            null,                  // createTime
//            null                   // updateTime
//        );
//    }
//}
