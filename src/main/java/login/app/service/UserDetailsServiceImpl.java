package login.app.service;

import java.util.ArrayList;
import java.util.List;

import login.app.entity.LoginUser;
import login.app.mapper.LoginUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Spring Securityのユーザ検索用のサービスの実装クラス
 * DataSourceの引数として指定することで認証にDBを利用できるようになる
 * @author aoi
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private LoginUserMapper loginUserMapper;
	
	/**
	 * UserDetailsServiceインタフェースの実装メソッド
	 * フォームから取得したユーザ名でDBを検索し、合致するものが存在したとき、
	 * パスワード、権限情報と共にUserDetailsオブジェクトを生成
	 * コンフィグクラスで上入力値とDBから取得したパスワードと比較し、ログイン判定を行う
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		LoginUser user = loginUserMapper.getLoginUserByUserId(userName);
		
		if (user == null) {
			throw new UsernameNotFoundException("User" + userName + "was not found in the database");
		}
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("USER");
		grantList.add(authority);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		UserDetails userDetails = (UserDetails)new User(user.getUsername(), encoder.encode(user.getPassword()),grantList);
		
		return userDetails;
	}

}
