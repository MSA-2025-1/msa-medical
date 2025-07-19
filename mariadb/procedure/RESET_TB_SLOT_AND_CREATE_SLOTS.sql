DELIMITER //

CREATE PROCEDURE reset_tb_slot_and_create_slots()
BEGIN
  DECLARE slot_date DATE;
  DECLARE cur_time TIME;
  DECLARE slot_datetime DATETIME;

  UPDATE TB_RESERVATION
  SET STATUS = 'RS02'
  WHERE SLOT_ID IN (
    SELECT ID FROM TB_SLOT
    WHERE DATE(SLOT) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)
  )
  AND STATUS IN ('RS01');

  SET slot_date = DATE_ADD(CURDATE(), INTERVAL 7 DAY);

  IF DAYOFWEEK(slot_date) != 1 THEN  
    SET cur_time = '09:00:00';

    WHILE cur_time < '18:00:00' DO
      IF cur_time < '13:00:00' OR cur_time >= '14:00:00' THEN
        SET slot_datetime = STR_TO_DATE(CONCAT(slot_date, ' ', cur_time), '%Y-%m-%d %H:%i:%s');
        INSERT INTO TB_SLOT (SLOT, CREATE_DATE)
        VALUES (slot_datetime, NOW());
      END IF;
      SET cur_time = ADDTIME(cur_time, '00:15:00');
    END WHILE;
  END IF;

  COMMIT;

END;
//

DELIMITER ;

