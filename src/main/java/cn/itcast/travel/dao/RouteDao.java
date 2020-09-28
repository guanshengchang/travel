package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根据cid查询总记录数
     */
    int findTotalCount(int cid,String rname);

    /**
     * 根据cid，start,pageSize查询当前页的数据集合
     */
    List<Route> findByPage(int cid , int start , int pageSize,String rname);

    /**
     * 根据id查询
     * @param rid
     * @return
     */
    Route findOne(int rid);

    /**
     * 根据id更新收藏次数
     * @param rid
     */
    void updateCount(int rid);

    /**
     * 查询所有收藏数Count大于0的线路
     * @return
     * @param start
     * @param pageSize
     * @param rname
     * @param lowPrice
     * @param highPrice
     */
    List<Route> findByCount(int start, int pageSize, String rname, double lowPrice, double highPrice);

    /**
     * 查询有收藏数的线路的总和
     * @return
     * @param rname
     * @param low
     * @param high
     */
    int findTotalFavorites(String rname, double low, double high);

    /**
     * 查询国内游的最受欢迎的前6条路线
     * @param cid
     * @return
     */
    List<Route> findByCid(int cid);

    /**
     * 根据日期查询最新的n条记录
     * @return
     */
    List<Route> findByDate();

    /**
     * 查询主题旅游线路
     * @return
     */
    List<Route> findByThemeTour();
}
