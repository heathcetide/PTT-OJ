CREATE TABLE `groups` (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                          name VARCHAR(255) NOT NULL, -- 组织名称
                          description TEXT, -- 组织描述
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
                          status ENUM('active', 'inactive', 'archived') DEFAULT 'active', -- 状态：活跃、不活跃、归档
                          creator_id BIGINT NOT NULL, -- 创建者ID
                          logo_url VARCHAR(255), -- 组织Logo URL
                          website VARCHAR(255), -- 组织网站
                          contact_email VARCHAR(255), -- 联系邮箱
                          phone_number VARCHAR(20), -- 联系电话
                          address TEXT -- 地址
);

CREATE TABLE `group_members` (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                 group_id BIGINT NOT NULL, -- 组织ID
                                 user_id BIGINT NOT NULL, -- 用户ID
                                 role ENUM('admin', 'moderator', 'member') DEFAULT 'member', -- 角色：管理员、版主、成员
                                 joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 加入时间
                                 left_at TIMESTAMP NULL, -- 离开时间
                                 status ENUM('active', 'inactive', 'banned') DEFAULT 'active' -- 状态：活跃、不活跃、封禁
);

CREATE TABLE `group_permissions` (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                     group_id BIGINT NOT NULL, -- 组织ID
                                     permission_name VARCHAR(255) NOT NULL, -- 权限名称
                                     description TEXT, -- 权限描述
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);

CREATE TABLE `group_member_permissions` (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                            member_id BIGINT NOT NULL, -- 成员ID
                                            permission_id BIGINT NOT NULL, -- 权限ID
                                            granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 授予权限的时间
                                            revoked_at TIMESTAMP NULL -- 撤销权限的时间
);

CREATE TABLE `group_invitations` (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                     group_id BIGINT NOT NULL, -- 组织ID
                                     inviter_id BIGINT NOT NULL, -- 邀请者ID
                                     invitee_email VARCHAR(255) NOT NULL, -- 被邀请者的邮箱
                                     token VARCHAR(255) NOT NULL, -- 邀请令牌
                                     expires_at TIMESTAMP NOT NULL, -- 过期时间
                                     status ENUM('pending', 'accepted', 'rejected', 'expired') DEFAULT 'pending', -- 状态：待处理、已接受、已拒绝、已过期
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 创建时间
);

CREATE TABLE `group_settings` (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                  group_id BIGINT NOT NULL, -- 组织ID
                                  setting_key VARCHAR(255) NOT NULL, -- 设置键
                                  setting_value TEXT, -- 设置值
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);

CREATE TABLE `group_events` (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                group_id BIGINT NOT NULL, -- 组织ID
                                event_type ENUM('member_joined', 'member_left', 'permission_granted', 'permission_revoked', 'group_updated') NOT NULL, -- 事件类型
                                event_data JSON, -- 事件数据
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 创建时间
);

CREATE TABLE `group_tags` (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                              group_id BIGINT NOT NULL, -- 组织ID
                              tag_name VARCHAR(255) NOT NULL, -- 标签名称
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 创建时间
);

CREATE TABLE `group_discussions` (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                     group_id BIGINT NOT NULL, -- 组织ID
                                     title VARCHAR(255) NOT NULL, -- 讨论标题
                                     content TEXT, -- 讨论内容
                                     created_by BIGINT NOT NULL, -- 创建者ID
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
                                     status ENUM('open', 'closed') DEFAULT 'open' -- 状态：开放、关闭
);

CREATE TABLE `group_discussion_comments` (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键ID
                                             discussion_id BIGINT NOT NULL, -- 讨论ID
                                             user_id BIGINT NOT NULL, -- 用户ID
                                             content TEXT, -- 评论内容
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);

# 以上设计涵盖了组织的基本信息、成员管理、权限管理、邀请机制、设置管理、事件记录、标签管理、讨论及评论等多个方面。
