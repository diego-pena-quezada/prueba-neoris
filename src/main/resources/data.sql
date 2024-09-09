INSERT INTO users (id, email, role, name, status, last_login_date, created_date,modified_date) VALUES
    ('a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3','admin@admin.com', 'ROLE_ADMIN', 'admin', 'INACTIVE', NOW(), NOW(), NOW());
INSERT INTO passwords(user_id, password) VALUES
    ('a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3','$2a$12$C0gNawpaEWWtJ6Lqw1bRVuaspjVEhMzIzjTlniNf1J849S3HrXxXK');