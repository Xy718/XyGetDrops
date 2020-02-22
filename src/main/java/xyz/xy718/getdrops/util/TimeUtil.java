package xyz.xy718.getdrops.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 时间工具类！
 * @author Xy718
 *
 */
public class TimeUtil {

	/**
	 * 判断一个时间戳是否过期
	 * @param dropTime 记载时间
	 * @param seconds 有效期（秒）
	 * @return
	 * 		true(已过期)
	 */
	public static boolean isExpired(Date dropTime,int seconds) {
		return new Date().after(new Date(dropTime.getTime()+seconds*1000));
	}
}
