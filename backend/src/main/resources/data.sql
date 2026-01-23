-- Users
INSERT INTO users (username, email, password_hash, role, is_banned, created_at)
VALUES
('alice', 'alice@example.com', 'hash1', 'USER', false, NOW()),
('bob', 'bob@example.com', 'hash2', 'USER', false, NOW()),
('admin', 'admin@example.com', 'hash3', 'ADMIN', false, NOW());

-- Posts
INSERT INTO posts (title, content, user_id, created_at)
VALUES
('First Post', 'Hello world!', 1, NOW()),
('Another Post', 'Learning Spring Boot', 2, NOW());

-- Comments
INSERT INTO comments (content, post_id, user_id, created_at)
VALUES
('Nice post!', 1, 2, NOW()),
('Thanks!', 1, 1, NOW());

-- Likes
INSERT INTO likes (post_id, user_id, created_at)
VALUES
(1, 2, NOW()),
(2, 1, NOW());

-- Reports
INSERT INTO reports (reason, reported_user_id, reported_post_id, reporter_id, status, created_at)
VALUES
('Spam content', 2, 1, 1, 'PENDING', NOW());

-- Subscriptions
INSERT INTO subscriptions (subscriber_id, target_id, created_at)
VALUES
(1, 2, NOW()),
(2, 1, NOW());

-- Notifications
INSERT INTO notifications (user_id, type, message, is_read, created_at)
VALUES
(1, 'NEW_COMMENT', 'Bob commented on your post', false, NOW());
