-- Insert sample admin user
-- Note: In a real application, passwords should be hashed
INSERT INTO users (username, password, email, role)
VALUES ('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'admin@example.com', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Insert sample regular user
INSERT INTO users (username, password, email, role)
VALUES ('user', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'user@example.com', 'USER')
ON CONFLICT (username) DO NOTHING;

-- Insert sample customers
INSERT INTO customers (first_name, last_name, email)
VALUES ('John', 'Doe', 'john.doe@example.com')
ON CONFLICT (email) DO NOTHING;

INSERT INTO customers (first_name, last_name, email)
VALUES ('Jane', 'Smith', 'jane.smith@example.com')
ON CONFLICT (email) DO NOTHING;

INSERT INTO customers (first_name, last_name, email)
VALUES ('Bob', 'Johnson', 'bob.johnson@example.com')
ON CONFLICT (email) DO NOTHING;