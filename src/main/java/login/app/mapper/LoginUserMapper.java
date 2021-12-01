package login.app.mapper;

import login.app.entity.LoginUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginUserMapper {
    LoginUser getLoginUserByUserId(String userId);
}
