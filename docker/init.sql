-- Create the transactions table
CREATE TABLE transactions
(
    transaction_id       UUID PRIMARY KEY,
    card_number          VARCHAR(19)    NOT NULL,
    merchant_id          VARCHAR(20)    NOT NULL,
    amount               DECIMAL(10, 2) NOT NULL,
    timestamp            TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    authorization_status VARCHAR(20)    NOT NULL,
    currency             VARCHAR(3)     NOT NULL  DEFAULT 'USD',
    CONSTRAINT check_amount_positive CHECK (amount > 0)
);

-- Create the card_limits table
CREATE TABLE card_limits
(
    card_number      VARCHAR(19) PRIMARY KEY,
    credit_limit     DECIMAL(12, 2) NOT NULL,
    available_credit DECIMAL(12, 2) NOT NULL,
    CONSTRAINT check_credit_limit_positive CHECK (credit_limit > 0),
    CONSTRAINT check_available_credit_non_negative CHECK (available_credit >= 0),
    CONSTRAINT check_available_credit_lte_limit CHECK (available_credit <= credit_limit)
);

-- Create the authorization_logs table
CREATE TABLE authorization_logs
(
    log_id         SERIAL PRIMARY KEY,
    transaction_id UUID    NOT NULL REFERENCES transactions (transaction_id),
    authorized     BOOLEAN NOT NULL,
    reason         VARCHAR(255),
    timestamp      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_transactions_card_number ON transactions (card_number);
CREATE INDEX idx_transactions_merchant_id ON transactions (merchant_id);
CREATE INDEX idx_transactions_timestamp ON transactions (timestamp);
CREATE INDEX idx_authorization_logs_transaction_id ON authorization_logs (transaction_id);

-- Insert initial credit limit data
INSERT INTO card_limits (card_number, credit_limit, available_credit)
VALUES ('1234567890123456', 1000, 1000),
       ('2345678901234567', 2000, 2000),
       ('3456789012345678', 3000, 3000);