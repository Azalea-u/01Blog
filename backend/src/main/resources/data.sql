-- ---------------------------
-- Admin & Test Users
-- ---------------------------
INSERT INTO users (id, username, email, password_hash, role, created_at, is_banned)
VALUES
    (1, 'admin', 'admin@01blog.com', '$2a$10$PLACEHOLDER_HASH', 'ADMIN', NOW(), FALSE),
    (2, 'alice', 'alice@example.com', '$2a$10$PLACEHOLDER_HASH', 'USER', NOW(), FALSE),
    (3, 'bob', 'bob@example.com', '$2a$10$PLACEHOLDER_HASH', 'USER', NOW(), FALSE);

-- ---------------------------
-- Example Posts
-- ---------------------------
INSERT INTO posts (id, user_id, title, content, media_url, media_type, created_at, updated_at)
VALUES
    (1, 2, 'My First Post', 'Hello world! This is Alice\'s first post.', NULL, NULL, NOW(), NOW()),
    (2, 3, 'Bob\'s Thoughts', 'This is Bob sharing his ideas.', NULL, NULL, NOW(), NOW());

-- ---------------------------
-- Example Comments
-- ---------------------------
INSERT INTO comments (id, post_id, user_id, content, created_at)
VALUES
    (1, 1, 3, 'Great post, Alice!', NOW()),
    (2, 2, 2, 'Interesting thoughts, Bob.', NOW());

-- ---------------------------
-- Example Likes
-- ---------------------------
INSERT INTO likes (user_id, post_id, created_at)
VALUES
    (3, 1, NOW()),
    (2, 2, NOW());

-- ---------------------------
-- Example Subscriptions (Alice follows Bob)
-- ---------------------------
INSERT INTO subscriptions (subscriber_id, target_id, created_at)
VALUES
    (2, 3, NOW());

-- ---------------------------
-- Example Notifications
-- ---------------------------
INSERT INTO notifications (user_id, message, type, is_read, reference_id, created_at)
VALUES
    (2, 'Bob published a new post!', 'NEW_POST', FALSE, 2, NOW());
