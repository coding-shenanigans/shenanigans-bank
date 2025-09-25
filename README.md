# Shenanigans Bank

An API that simulates some basic functionalities of a bank.

Design doc: https://docs.google.com/document/d/1IHVJ3YjF0wK9xh3exj78S7IKIXNZAvfz0QVVmE5itGI

## Local MySQL with Docker

Create a local MySQL docker container:
```bash
docker run -d --name  local-mysql -e MYSQL_ROOT_PASSWORD=your_root_password -p 3306:3306 mysql
```

Shell into the MySQL container:
```bash
docker exec -it local-mysql /bin/bash
```

Connect to the MySQL database:
```bash
mysql -u root -p

# you'll be prompted for the root password used in the docker run command
```

Set up database:
```sql
CREATE DATABASE IF NOT EXISTS shenanigans_bank;

USE shenanigans_bank;

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS sessions (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS accounts (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    new_balance DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Create stored procedure to apply interest payments.
-- Cloud SQL Studio (the Cloud SQL query UI) doesn't support the DELIMITER commands.
-- Removing the DELIMITER commands should work on Cloud SQL Studio.
DELIMITER $$

CREATE PROCEDURE apply_interest_to_savings()
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;

    START TRANSACTION;

    CREATE TEMPORARY TABLE IF NOT EXISTS temp_interest AS (
        SELECT
            id AS account_id,
            ROUND(balance * 0.0001, 4) AS interest_amount
        FROM accounts
        WHERE type = 'SAVINGS'
    );

    UPDATE accounts a
    INNER JOIN temp_interest ti
        ON a.id = ti.account_id
    SET a.balance = a.balance + ti.interest_amount;

    INSERT INTO transactions (account_id, title, amount, new_balance)
    SELECT
        a.id,
        'Interest earned',
        ti.interest_amount,
        a.balance
    FROM accounts a
    INNER JOIN temp_interest ti
        ON a.id = ti.account_id;

    DROP TEMPORARY TABLE IF EXISTS temp_interest;

    COMMIT;
END;$$

DELIMITER ;

-- Event to cleanup expired sessions.
-- Runs daily at 1AM.
CREATE EVENT IF NOT EXISTS cleanup_sessions
ON SCHEDULE EVERY 1 DAY
STARTS '2025-09-23 01:00:00'
DO
    DELETE FROM sessions
    WHERE updated_at < NOW() - INTERVAL 24 HOUR;

-- Event to apply interest to savings accounts.
-- Runs daily at 2AM.
CREATE EVENT IF NOT EXISTS pay_interest
ON SCHEDULE EVERY 1 DAY
STARTS '2025-09-23 02:00:00'
DO
    CALL apply_interest_to_savings();
```

## Run Locally with Docker

Build a docker image for local dev:
```bash
docker build -t shenanigans-bank:v0.0.1 .
```

Build a docker image for prod:
```bash
docker build -t us-central1-docker.pkg.dev/shenanigans-bank/images/shenanigans-bank:v0.0.1 .
```

Push a docker image to prod:
```bash
docker push us-central1-docker.pkg.dev/shenanigans-bank/images/shenanigans-bank:v0.0.1
```

Run a docker container:
```bash
docker run --name shenanigans-bank -p 8080:8080 shenanigans-bank:v0.0.1
```
