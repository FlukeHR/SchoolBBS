-- 数据库更新脚本
-- 用于添加贴子类型管理和媒体文件支持功能

USE forumsys;

-- 1. 创建贴子类型表
CREATE TABLE IF NOT EXISTS `post_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
  `creator` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类型描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_name` (`type_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='贴子类型表';

-- 2. 插入默认的贴子类型
INSERT INTO `post_type` (`type_name`, `creator`, `create_time`, `description`) VALUES
('学习讨论', 'admin', NOW(), '学习相关的讨论和分享'),
('校园生活', 'admin', NOW(), '校园生活相关的话题'),
('表白墙', 'admin', NOW(), '表白和情感相关的内容')
ON DUPLICATE KEY UPDATE type_name=type_name;

-- 3. 为post表添加media_files字段（如果不存在）
-- 检查字段是否存在，如果不存在则添加
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'forumsys' 
AND TABLE_NAME = 'post' 
AND COLUMN_NAME = 'media_files';

SET @query = IF(@col_exists = 0, 
    'ALTER TABLE `post` ADD COLUMN `media_files` text CHARACTER SET gbk COLLATE gbk_chinese_ci COMMENT ''媒体文件路径，多个文件用逗号分隔''', 
    'SELECT ''Column media_files already exists'' AS message');

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. 创建uploads目录的说明
-- 注意：需要在webapp目录下手动创建uploads文件夹，用于存储上传的图片和视频文件
-- 路径：src/main/webapp/uploads/

SELECT '数据库更新完成！' AS message;
