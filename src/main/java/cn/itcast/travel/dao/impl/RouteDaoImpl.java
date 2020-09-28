package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";
        //1.定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        //条件们
        List params = new ArrayList();
        //2.判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid = ? ");
            //添加？对应的值
            params.add(cid);
        }
        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //String sql = "select * from tab_route where cid = ? and rname like ?  limit ? , ?";
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);
        //条件们
        List params = new ArrayList();
        //2.判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid = ? ");
            //添加？对应的值
            params.add(cid);
        }

        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        //分页条件
        sb.append(" limit ? , ? ");
        sql = sb.toString();
        params.add(start);
        params.add(pageSize);

        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
    }

    @Override
    public void updateCount(int rid) {
        String sql = "update tab_route set count = count + 1 where rid = ?";
        template.update(sql, rid);
    }

    @Override
    public List<Route> findByCount(int start, int pageSize, String rname, double lowPrice, double highPrice) {
        String sql = "select * from tab_route where count > 0 and price >= ? and price <= ? ";
        StringBuilder sb = new StringBuilder(sql);
        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? order by count desc");
            sql = sb.toString();
            return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), lowPrice, highPrice, "%" + rname + "%");
        }
        sb.append(" order by count desc");
        sql = sb.toString();
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), lowPrice, highPrice);
    }

    @Override
    public int findTotalFavorites(String rname, double low, double high) {
        String sql = "select count(*) from tab_route where count > 0 and price >= ? and price <= ? ";
        StringBuilder sb = new StringBuilder(sql);
        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            return template.queryForObject(sb.toString(), Integer.class, low, high, "%" + rname + "%");
        }
        return template.queryForObject(sql, Integer.class, low, high);
    }

    @Override
    public List<Route> findByCid(int cid) {
        String sql = "";
        List<Route> routeList = null;
        if (cid == 0) {
            sql = "select * from tab_route where cid in (4, 5, 6, 7, 8) order by count desc limit 0, 4";
            routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        } else {
            sql = "select * from tab_route where cid = ? order by count desc limit 0, 6";
            routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cid);
        }
        return routeList;
    }

    @Override
    public List<Route> findByDate() {
        String sql = "select * from tab_route where cid in (4, 5, 6, 7, 8) order by rdate desc limit 0, 4";
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        return routeList;
    }

    @Override
    public List<Route> findByThemeTour() {
        String sql = "select * from tab_route where cid in (4, 5, 6, 7, 8) and isThemeTour = 1 order by rdate desc limit 0, 4";
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        return routeList;
    }
}
