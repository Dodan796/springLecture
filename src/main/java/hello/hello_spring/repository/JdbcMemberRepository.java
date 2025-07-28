package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC의 설정의 가장 큰 이유는 바로 OCP원칙에 의해 Data를 관리할 수 있다
 * OCP원칙: (Open-Closed Principle)
 * => 확장에는 열려있고 수정에는 닫혀있다.
 * Spring의 DI(Dependencies Injection)을 사용 => 기존의 코드를 수정하지 않고, 설정만으로 구현클래스 변경 가능
 *
 */
public class JdbcMemberRepository implements MemberRepository {

    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    // 1. DB에 저장하는 JDBC 로직
    @Override
    public Member save(Member member) {

        String sql = "insert into member(name) values(?)";

        Connection conn = null;

        PreparedStatement pstmt = null;
        // 결과를 받는 부분
        ResultSet rs = null;

        try {
            // 1. getConnection()을 통해서 Connection을 가져온다
            conn = getConnection();

            // 2. RETURN_GENERATED_KEYS => 자동 생성된 키 값을 조회가능
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            // 3. sql = "insert into member(name) values(?)"; 의 ? 부분에 member.getName() 값을 넣음
            pstmt.setString(1, member.getName());

            // 4. DB에 실제 쿼리가 전송
            pstmt.executeUpdate();

            // 5. getGeneratedKeys(); => 1번, 2번의 값을 꺼내준다.
            rs = pstmt.getGeneratedKeys();

            // 6. DB에 1번으로 값이 저장이 된다
            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                // 7. 실패시 예외처리
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }


    // 2. DB를 조회하는 로직
    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            // 1. executeQuery()를 통해서 쿼리를 조회한다.
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 3. DB에 저장되어있는 모든 값을 조회하는 로직
    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}