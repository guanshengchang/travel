package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.*;
import cn.itcast.travel.dao.impl.*;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import javax.lang.model.element.VariableElement;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();

    private RouteImgDao routeImgDao = new RouteImgDaoImpl();

    private SellerDao sellerDao = new SellerDaoImpl();

    private CategoryDao categoryDao = new CategoryDaoImpl();

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        //开始的记录数
        int start = (currentPage - 1) * pageSize;
        List<Route> list = routeDao.findByPage(cid, start, pageSize, rname);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1.根据id去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2.根据route的id 查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route的sid（商家id）查询商家对象
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        //4.根据route的cid查询Category对象
        Category category = categoryDao.findByCid(route.getCid());
        route.setCategory(category);

        /*//4. 查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);*/

        return route;
    }

    @Override
    public void updateCount(String rid) {
        routeDao.updateCount(Integer.parseInt(rid));
    }

    @Override
    public PageBean<Route> findByCount(String currentPage, int pageSize, String rname, String lowPrice, String highPrice) {
        //参数类型转换
        int curPage = Integer.parseInt(currentPage);
        double low = Double.MIN_VALUE;
        double high = Double.MAX_VALUE;
        if (lowPrice != null && !lowPrice.equals("")) {
            low = Double.parseDouble(lowPrice);
        }
        if (highPrice != null && !highPrice.equals("")) {
            high = Double.parseDouble(highPrice);
        }

        PageBean<Route> pageBean = new PageBean<>();
        //设置当前页
        pageBean.setCurrentPage(curPage);
        //设置每页显示数目
        pageBean.setPageSize(pageSize);

        if (low > high) {
            return pageBean;
        }

        //开始位置索引
        int start = (curPage - 1) * pageSize;
        //当前页的数据
        List<Route> routeList = routeDao.findByCount(start, pageSize, rname, low, high);
        pageBean.setList(routeList);
        //收藏数大于0的线路总记录数
        int totalCount = routeDao.findTotalFavorites(rname, low, high);
        pageBean.setTotalCount(totalCount);
        //总页面数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pageBean.setTotalPage(totalPage);

        return pageBean;
    }

    @Override
    public List<Route> findRoute(String cid) {
        List<Route> routeList = routeDao.findByCid(Integer.parseInt(cid));
        return routeList;
    }

    @Override
    public List<Route> findNewest() {
        List<Route> routeList = routeDao.findByDate();
        return routeList;
    }

    @Override
    public List<Route> findTheme() {
        List<Route> routeList = routeDao.findByThemeTour();
        return routeList;
    }
}
