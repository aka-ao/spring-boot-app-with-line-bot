package login.app.entity;

import lombok.Data;

/**
 * ログインユーザのユーザ名、パスワードを格納するためのEntity
 * @author aoi
 *
 */
@Data
public class LoginUser {
	private String username;
	private String password;
}
