CREATE VIEW get_all_loan AS
	SELECT l.id, l.created_at, lt.type AS loan_type, it.installment_type, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, 
		u.email, l.nominal AS loan_nominal, l.interest_rate, l.approval_status, l.approved_at, l.approved_by, ld.transaction_date, 
		ld.nominal AS payment, ld.loan_status, ld.updated_at
	FROM trx_loan l 
	JOIN t_loan_type lt ON l.loan_type_id = lt.loan_type_id
	JOIN t_installment_type it ON l.installment_type_id = it.id
	JOIN mst_customer c ON l.customer_id = c.id
	JOIN mst_user u ON u.id = c.user_id
	JOIN trx_loan_detail ld ON l.id = ld.trx_loan_id;

SELECT * FROM get_all_loan WHERE id = '4a63888a-91cf-420e-b5c7-f36e278acced';

CREATE VIEW get_all_customer AS
	SELECT c.id, c.date_of_birth, c.first_name, c.last_name, c.phone, c.status, c.user_id, u,email, r.name
	FROM mst_customer c 
	JOIN mst_user u ON c.user_id = u.id
	JOIN t_user_role ur ON u.id = ur.user_id
	JOIN t_role r ON ur.role_id = r.role_id;

SELECT * FROM get_all_customer WHERE id = '14505866-320b-4817-9afa-ed2239864aa9';

SELECT * FROM t_loan_type;
SELECT * FROM t_installment_type;
SELECT * FROM mst_customer;
SELECT * FROM mst_user;
SELECT * FROM trx_loan;
SELECT * FROM trx_loan_detail;
SELECT * FROM t_role;
SELECT * FROM t_user_role;