package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * 线路Service
 */
public interface RouteService {

    /**
     * 根据类别进行分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageBean<Route> pageQuery(int cid,int currentPage,int pageSize,String rname);

    /**
     * 根据id查询
     * @param rid
     * @return
     */
    public Route findOne(String rid);

    /**
     * 根据id更新收藏次数
     * @param rid
     */
    void updateCount(String rid);

    /**
     * 查询所有收藏数大于0的路线
     * @return
     * @param currentPage
     * @param pageSize
     * @param rname
     * @param lowPrice
     * @param highPrice
     */
    PageBean<Route> findByCount(String currentPage, int pageSize, String rname, String lowPrice, String highPrice);

    /**
     * 查询最受欢迎的前n条路线
     * @param cid
     * @return
     */
    List<Route> findRoute(String cid);

    /**
     * 查询最新的线路
     * @return
     */
    List<Route> findNewest();

    /**
     * 查询主题旅游线路
     * @return
     */
    List<Route> findTheme();
}
