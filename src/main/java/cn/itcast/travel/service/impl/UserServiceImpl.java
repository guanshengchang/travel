package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11587
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao = new RouteDaoImpl();

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        //判断u是否为null
        if (u != null) {
            //用户名存在，注册失败
            return false;
        }

        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        userDao.save(user);

        //3.激活邮件发送，邮件正文？
        String content = "<a href='http://localhost/travel/activeUserServlet?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");

        return true;
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 登录方法
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Override
    public PageBean<Route> findFavoriteListByUid(int uid, String currentPage) {
        List<Favorite> favoriteList = favoriteDao.findByUid(uid);
        PageBean<Route> pageBean = new PageBean<>();
        int totalCount = favoriteList.size();
        int totalPage = totalCount % 12 == 0 ? totalCount / 12 : totalCount / 12 + 1;
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setPageSize(12);
        pageBean.setCurrentPage(Integer.parseInt(currentPage));

        List<Route> routeList = new ArrayList<>();


        int start = (Integer.parseInt(currentPage) - 1) * 12;
        int end = start + 11;
        if (end >= totalCount) {
            end = totalCount - 1;
        }

        for (int i = start; i <= end; ++i) {
            int rid = favoriteList.get(i).getRid();
            Route route = routeDao.findOne(rid);
            routeList.add(route);
        }

        pageBean.setList(routeList);
        return pageBean;
    }

}
