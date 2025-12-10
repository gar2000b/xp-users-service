package ca.digilogue.xp.repository;

import ca.digilogue.xp.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_USERS_SQL =
            "SELECT id, username, first_name, last_name, email FROM users";
    private static final String SELECT_USER_BY_ID_SQL =
            "SELECT id, username, first_name, last_name, email FROM users WHERE id = ?";
    private static final String INSERT_USER_SQL =
            "INSERT INTO users (username, first_name, last_name, email) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_SQL =
            "UPDATE users SET username = ?, first_name = ?, last_name = ?, email = ? WHERE id = ?";

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS_SQL, new UserRowMapper());
    }

    public User findById(String id) {
        List<User> users = jdbcTemplate.query(SELECT_USER_BY_ID_SQL, new UserRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }

    public User save(User user) {
        jdbcTemplate.update(INSERT_USER_SQL,
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());

        // Retrieve the generated ID by querying with username (which is unique)
        return jdbcTemplate.query(
                "SELECT id, username, first_name, last_name, email FROM users WHERE username = ?",
                new UserRowMapper(),
                user.getUsername()
        ).get(0);
    }

    public User update(User user) {
        int rowsAffected = jdbcTemplate.update(UPDATE_USER_SQL,
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                Long.parseLong(user.getId()));

        if (rowsAffected == 0) {
            return null; // User not found
        }

        // Return the updated user
        return findById(user.getId());
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(String.valueOf(rs.getLong("id")));
            user.setUsername(rs.getString("username"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    }
}
