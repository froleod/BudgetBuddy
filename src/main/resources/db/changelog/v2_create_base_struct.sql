INSERT INTO users (username, password)
VALUES
    ('user1', 'password1'),
    ('user2', 'password2'),
    ('user3', 'password3');

INSERT INTO bank_account (account_number, balance, user_id)
VALUES
    ('123456789', 1000.00, 1),
    ('987654321', 5000.00, 2),
    ('456789123', 2500.00, 3);


INSERT INTO transaction_categories (name, type)
VALUES
    ('Rent', 'EXPENSE'),
    ('Groceries', 'EXPENSE'),
    ('Salary', 'INCOME'),
    ('Utilities', 'EXPENSE'),
    ('Entertainment', 'EXPENSE');

INSERT INTO budgets (name, amount, start_date, end_date, status, user_id)
VALUES
    ('Monthly Budget', 3000.00, '2023-01-01', '2023-12-31', 'ACTIVE', 1),
    ('Quarterly Budget', 10000.00, '2023-04-01', '2023-06-30', 'ACTIVE', 2),
    ('Yearly Budget', 50000.00, '2023-01-01', '2023-12-31', 'ACTIVE', 3);


INSERT INTO financial_goals (name, target_amount, start_date, end_date, status, user_id)
VALUES
    ('Save for a down payment', 25000.00, '2023-01-01', '2024-12-31', 'ACTIVE', 1),
    ('Pay off credit card debt', 5000.00, '2023-03-01', '2023-12-31', 'ACTIVE', 2),
    ('Retirement fund', 100000.00, '2023-01-01', '2028-12-31', 'ACTIVE', 3);


INSERT INTO transactions (amount, transaction_date, description, bank_account_id, category_id)
VALUES
    (1000.00, '2023-05-01 10:00:00', 'Salary', 1, 3),
    (500.00, '2023-05-05 14:30:00', 'Grocery shopping', 1, 2),
    (800.00, '2023-05-10 18:20:00', 'Rent payment', 1, 1),
    (200.00, '2023-05-15 09:45:00', 'Electricity bill', 1, 4),
    (50.00, '2023-05-20 16:10:00', 'Movie tickets', 1, 5),
    (2500.00, '2023-06-01 11:00:00', 'Salary', 2, 3),
    (300.00, '2023-06-05 13:40:00', 'Grocery shopping', 2, 2),
    (1000.00, '2023-06-10 19:15:00', 'Rent payment', 2, 1),
    (150.00, '2023-06-15 08:30:00', 'Water bill', 2, 4),
    (75.00, '2023-06-20 17:25:00', 'Dinner out', 2, 5);