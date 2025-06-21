INSERT INTO TB_STATUS_GROUP (name) VALUES
  ('회원 역할'),
  ('진료 상태');

INSERT INTO TB_STATUS_CODE (id, group_id, name) VALUES
  ('R001', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '관리자'),
  ('R002', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '의사'),
  ('R003', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '간호사'),
  ('R004', (SELECT id FROM TB_STATUS_GROUP WHERE name = '회원 역할'), '환자')
  ('S001', (SELECT id FROM TB_STATUS_GROUP WHERE name = '진료 상태'), '예약'),
  ('S002', (SELECT id FROM TB_STATUS_GROUP WHERE name = '진료 상태'), '예약 취소'),
  ('S003', (SELECT id FROM TB_STATUS_GROUP WHERE name = '진료 상태'), '대기'),
  ('S004', (SELECT id FROM TB_STATUS_GROUP WHERE name = '진료 상태'), '진료 중'),
  ('S005', (SELECT id FROM TB_STATUS_GROUP WHERE name = '진료 상태'), '진료 완료'),
  ('S006', (SELECT id FROM TB_STATUS_GROUP WHERE name = '진료 상태'), '결제 완료'),
  ('S007', (SELECT id FROM TB_STATUS_GROUP WHERE name = '진료 상태'), '예약 홀드');

INSERT INTO TB_MEMBER (uuid, member_role, name, rrn, phone)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'R001', '김철수', '900101-1234567', '010-1234-5678'),
  ('550e8400-e29b-41d4-a716-446655440001', 'R001', '이영희', '850505-2345678', '010-2345-6789'),
  ('550e8400-e29b-41d4-a716-446655440002', 'R002', '박민수', '920202-3456789', '010-3456-7890'),
  ('660e8400-e29b-41d4-a716-446655440010', 'R002', '최민지', '830303-4567890', '010-4567-8901'),
  ('660e8400-e29b-41d4-a716-446655440011', 'R003', '정영훈', '780707-5678901', '010-5678-9012'),
  ('660e8400-e29b-41d4-a716-446655440012', 'R003', '한지우',   '910909-6789012', '010-6789-0123');
