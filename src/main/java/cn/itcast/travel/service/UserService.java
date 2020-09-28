package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;

/**
 * @author 11587
 */
public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean regist(User user);

    boolean active(String code);

    User login(User user);

    /**
     * 根据用户id查询该用户的收藏列表
     * @param uid
     * @param currentPage
     * @return
     */
    PageBean<Route> findFavoriteListByUid(int uid, String currentPage);
}
