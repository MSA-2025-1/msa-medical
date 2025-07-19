INSERT INTO TB_STATUS_GROUP (name)
VALUES
  ('회원 역할'),
  ('예약 상태'),
  ('접수 상태'),
  ('결재 상태');

INSERT INTO TB_STATUS_CODE (id, group_id, name)
VALUES
  ('R001', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '관리자'),
  ('R002', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '의사'),
  ('R003', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '간호사'),
  ('R004', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '환자'),
  ('RS01', (SELECT id FROM TB_STATUS_GROUP WHERE name = '예약 상태'), '예약'),
  ('RS02', (SELECT id FROM TB_STATUS_GROUP WHERE name = '예약 상태'), '예약 취소'),
  ('RS03', (SELECT id FROM TB_STATUS_GROUP WHERE name = '예약 상태'), '예약 홀드'),
  ('RS04', (SELECT id FROM TB_STATUS_GROUP WHERE name = '예약 상태'), '예약 완료'),
  ('RS05', (SELECT id FROM TB_STATUS_GROUP WHERE name = '예약 상태'), '예약 홀드 취소'),
  ('RE01', (SELECT id FROM TB_STATUS_GROUP WHERE name = '접수 상태'), '대기'),
  ('RE02', (SELECT id FROM TB_STATUS_GROUP WHERE name = '접수 상태'), '접수 취소'),
  ('RE03', (SELECT id FROM TB_STATUS_GROUP WHERE name = '접수 상태'), '진료 중'),
  ('RE04', (SELECT id FROM TB_STATUS_GROUP WHERE name = '접수 상태'), '진료 완료'),
  ('RR01', (SELECT id FROM TB_STATUS_GROUP WHERE name = '결재 상태'), '결제 대기중'),
  ('RR02', (SELECT id FROM TB_STATUS_GROUP WHERE name = '결재 상태'), '결제 완료');

INSERT INTO TB_MEMBER (uuid, member_role, name, rrn, phone)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'R001', '김철수', '900101-1234567', '010-1234-5678'),
  ('550e8400-e29b-41d4-a716-446655440001', 'R002', '이영희', '850505-2345678', '010-2345-6789'),
  ('550e8400-e29b-41d4-a716-446655440002', 'R004', '박민수', '920202-3456789', '010-3456-7890'),
  ('660e8400-e29b-41d4-a716-446655440010', 'R002', '최민지', '830303-4567890', '010-4567-8901'),
  ('660e8400-e29b-41d4-a716-446655440011', 'R003', '정영훈', '780707-5678901', '010-5678-9012'),
  ('660e8400-e29b-41d4-a716-446655440012', 'R003', '한지우', '910909-6789012', '010-6789-0123'),
  ('660e8400-e29b-41d4-a716-446655440013', 'R004', '한강민', '910709-1789112', '010-6889-0543');

INSERT INTO TB_MEMBER_CREDENTIALS (user_uuid, id, password)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'admin', '$2a$12$.yzQA6.4kSYxGUPMSleHRuHd0QLtbLntTuu9k7hQQXkDDmLLe83we'),
  ('550e8400-e29b-41d4-a716-446655440001', 'doctor', '$2a$12$.yzQA6.4kSYxGUPMSleHRuHd0QLtbLntTuu9k7hQQXkDDmLLe83we'),
  ('660e8400-e29b-41d4-a716-446655440011', 'nurse', '$2a$12$.yzQA6.4kSYxGUPMSleHRuHd0QLtbLntTuu9k7hQQXkDDmLLe83we'),
  ('550e8400-e29b-41d4-a716-446655440002', 'patient', '$2a$12$.yzQA6.4kSYxGUPMSleHRuHd0QLtbLntTuu9k7hQQXkDDmLLe83we');


LOAD DATA INFILE '/docker-entrypoint-initdb.d/medicine.csv'
INTO TABLE TB_MEDICINE
FIELDS TERMINATED BY ',' 
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(code, name, company_name)
SET create_date = NOW(), update_date = NULL;
  
LOAD DATA INFILE '/docker-entrypoint-initdb.d/disease.csv'
INTO TABLE TB_DISEASE
FIELDS TERMINATED BY ',' 
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(name, code)
SET create_date = NOW(), update_date = NULL;