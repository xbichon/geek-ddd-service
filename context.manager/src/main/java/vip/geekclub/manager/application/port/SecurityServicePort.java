package vip.geekclub.manager.application.port;

import vip.geekclub.manager.application.port.dto.TeacherCredential;

public interface SecurityServicePort {

    /**
     * 创建用户凭证
     *
     * @param username 用户名
     * @param password 密码
     */
    void createCredential(TeacherCredential teacherCredential);
}
