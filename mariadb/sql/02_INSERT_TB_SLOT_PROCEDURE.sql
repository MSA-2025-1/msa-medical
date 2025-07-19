DELIMITER //

CREATE PROCEDURE generate_slots(IN days_ahead INT)
BEGIN
  DECLARE i INT DEFAULT 0;
  DECLARE target_date DATE;
  DECLARE time_cursor TIME;
  DECLARE current_slot DATETIME;

  WHILE i < days_ahead DO
    SET target_date = DATE_ADD(CURDATE(), INTERVAL i DAY);

    IF DAYOFWEEK(target_date) != 1 THEN
      SET time_cursor = '09:00:00';

      WHILE time_cursor < '18:00:00' DO

        IF time_cursor < '13:00:00' OR time_cursor >= '14:00:00' THEN
          SET current_slot = CONCAT(target_date, ' ', time_cursor);

          INSERT INTO TB_SLOT (SLOT, CREATE_DATE)
          VALUES (current_slot, NOW());
        END IF;

        SET time_cursor = ADDTIME(time_cursor, '00:15:00');
      END WHILE;
    END IF;

    SET i = i + 1;
  END WHILE;
END;
//

DELIMITER ;

CALL generate_slots(7);
