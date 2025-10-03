CREATE TABLE IF NOT EXISTS execution_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    us_id VARCHAR(100),              -- ✅ Added: User Story ID for traceability
    tc_id VARCHAR(100),              -- ✅ Added: Test Case ID for traceability
    suite_id VARCHAR(100),
    run_id VARCHAR(100),
    test_name VARCHAR(255),
    status VARCHAR(50),
    test_type VARCHAR(50),
    start_time DATETIME,
    end_time DATETIME,
    logs TEXT
);
