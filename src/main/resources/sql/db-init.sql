CREATE TABLE IF NOT EXISTS execution_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    suite_id VARCHAR(100),
    run_id VARCHAR(100),
    test_name VARCHAR(255),
    status VARCHAR(50),
    test_type VARCHAR(50),   -- <--- Add this column
    start_time DATETIME,
    end_time DATETIME,
    logs TEXT
);
