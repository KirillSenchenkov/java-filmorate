package ru.yandex.practicum.filmorate.service.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSetExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> userMap = new HashMap<>();

        while (rs.next()) {
            User user = new User(
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("birthday").toLocalDate()
            );
            user.setId(rs.getInt("id"));
            if (rs.getInt("friend_id") != 0) {
                user.getFriends().add(rs.getInt("friend_id"));
            }

            if (userMap.containsKey(rs.getInt("id"))) {
                if (rs.getInt("friend_id") != 0) {
                    userMap.get(rs.getInt("id")).getFriends().add(rs.getInt("friend_id"));
                }
            } else if (!userMap.containsKey(rs.getInt("id"))) {
                userMap.put(rs.getInt("id"), user);
            }
        }
        return new ArrayList<>(userMap.values());
    }
}
