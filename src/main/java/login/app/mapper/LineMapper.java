package login.app.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LineMapper {
    void insertLine(String userName, String nonce);
    void connectUser(String lineId, String nonce);
    String getUserNameByLineId(String lineId);
}
