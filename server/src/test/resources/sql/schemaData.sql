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
