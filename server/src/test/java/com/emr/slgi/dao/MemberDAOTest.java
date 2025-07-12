package com.emr.slgi.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.emr.slgi.member.dao.MemberDAO;
import com.emr.slgi.member.domain.Member;
import com.emr.slgi.member.dto.MemberCreateDTO;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
    scripts = { "/sql/schema.sql", "/sql/schemaData.sql" }, 
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class MemberDAOTest {

    @Autowired
    private MemberDAO memberDAO;

    private String memberUuid;

    @BeforeEach
    void setup() {
        memberUuid = UUID.randomUUID().toString();
        MemberCreateDTO createDto = new MemberCreateDTO(
                memberUuid,
                "환자이름",
                "900101-1234567",
                "010-1111-2222"
        );
        memberDAO.createPatient(createDto);
    }

    @Nested
    @DisplayName("getByUuid 테스트")
    class GetByUuidTests {
        @Test
        @DisplayName("존재하는 UUID 조회 시 Member 반환")
        void whenExistsReturnsMember() {
            Member m = memberDAO.getByUuid(memberUuid);
            assertThat(m).isNotNull();
            assertThat(m.getUuid()).isEqualTo(memberUuid);
        }

        @Test
        @DisplayName("존재하지 않는 UUID 조회 시 null 반환")
        void whenNotFoundReturnsNull() {
            Member m = memberDAO.getByUuid("non-existent-uuid");
            assertThat(m).isNull();
        }
    }

}
