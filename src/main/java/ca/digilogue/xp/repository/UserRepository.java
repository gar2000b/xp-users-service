package ca.digilogue.xp.repository;

import ca.digilogue.xp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_USERS_SQL =
            "SELECT id, username, first_name, last_name, email FROM users";
    private static final String SELECT_USER_BY_ID_SQL =
            "SELECT id, username, first_name, last_name, email FROM users WHERE id = ?";
    private static final String INSERT_USER_SQL =
            "INSERT INTO users (username, first_name, last_name, email) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_SQL =
            "UPDATE users SET username = ?, first_name = ?, last_name = ?, email = ? WHERE id = ?";
    private static final String DELETE_USER_SQL =
            "DELETE FROM users WHERE id = ?";

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS_SQL, new UserRowMapper());
    }

    public List<User> findByFilters(String firstName, String lastName) {
        List<String> whereClauses = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (firstName != null && !firstName.trim().isEmpty()) {
            String trimmed = firstName.trim();
            if (trimmed.contains("*")) {
                // Wildcard pattern - use LIKE
                String likePattern = buildLikePattern(trimmed);
                log.info("firstName wildcard pattern: '{}' -> LIKE pattern: '{}'", trimmed, likePattern);
                whereClauses.add("first_name LIKE ?");
                params.add(likePattern);
            } else {
                // Exact match
                log.info("firstName exact match: '{}'", trimmed);
                whereClauses.add("first_name = ?");
                params.add(trimmed);
            }
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            String trimmed = lastName.trim();
            if (trimmed.contains("*")) {
                // Wildcard pattern - use LIKE
                String likePattern = buildLikePattern(trimmed);
                log.info("lastName wildcard pattern: '{}' -> LIKE pattern: '{}'", trimmed, likePattern);
                whereClauses.add("last_name LIKE ?");
                params.add(likePattern);
            } else {
                // Exact match
                log.info("lastName exact match: '{}'", trimmed);
                whereClauses.add("last_name = ?");
                params.add(trimmed);
            }
        }

        if (whereClauses.isEmpty()) {
            // No filters provided, return all users
            return findAll();
        }

        String sql = SELECT_ALL_USERS_SQL + " WHERE " + String.join(" AND ", whereClauses);
        return jdbcTemplate.query(sql, new UserRowMapper(), params.toArray());
    }

    /**
     * Converts wildcard pattern with * to SQL LIKE pattern with %
     * Examples:
     *   "John*" -> "John%"
     *   "*Smith" -> "%Smith"
     *   "*Doe*" -> "%Doe%"
     *   "John" -> "John" (no wildcards)
     */
    private String buildLikePattern(String pattern) {
        // Replace * with % for SQL LIKE wildcard
        String likePattern = pattern.replace("*", "%");
        log.info("buildLikePattern: input='{}', output='{}'", pattern, likePattern);
        return likePattern;
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

    public User partialUpdate(String id, Map<String, Object> updates) {
        if (updates.isEmpty()) {
            // No fields to update, just return the existing user
            return findById(id);
        }

        // Build dynamic SQL based on provided fields
        List<String> setClauses = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        if (updates.containsKey("username")) {
            setClauses.add("username = ?");
            values.add(updates.get("username"));
        }
        if (updates.containsKey("firstName")) {
            setClauses.add("first_name = ?");
            values.add(updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            setClauses.add("last_name = ?");
            values.add(updates.get("lastName"));
        }
        if (updates.containsKey("email")) {
            setClauses.add("email = ?");
            values.add(updates.get("email"));
        }

        if (setClauses.isEmpty()) {
            // No valid fields to update, return existing user
            return findById(id);
        }

        // Build the SQL statement
        String sql = "UPDATE users SET " + String.join(", ", setClauses) + " WHERE id = ?";
        values.add(Long.parseLong(id));

        int rowsAffected = jdbcTemplate.update(sql, values.toArray());

        if (rowsAffected == 0) {
            return null; // User not found
        }

        // Return the updated user
        return findById(id);
    }

    public boolean deleteById(String id) {
        int rowsAffected = jdbcTemplate.update(DELETE_USER_SQL, Long.parseLong(id));
        return rowsAffected > 0;
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
