package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        //接受rname线路名称
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        if (rname.equals("null")) {
            rname = "";
        }

        //2.处理参数
        //类别id
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        //当前页码，如果不传递，则默认为第一页
        int currentPage = 1;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        }
        //每页显示条数，如果不传递，默认每页显示5条记录
        int pageSize = 5;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        //3. 调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);

        //4. 将pageBean对象序列化为json，返回
        writeValue(pb, response);
    }

    /**
     * 根据id查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路id
        String rid = request.getParameter("rid");

        //2. 获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null) {
            //用户尚未登录
            uid = 0;
        } else {
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4. 写回客户端
        writeValue(flag, response);
    }

    /**
     * 添加收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路rid
        String rid = request.getParameter("rid");

        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null) {
            //用户尚未登录
            return;
        } else {
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用service添加
        routeService.updateCount(rid);
        favoriteService.add(rid, uid);
    }

    /**
     * 查询收藏排行榜
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void favoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        String currentPage = request.getParameter("currentPage");
        String rname = request.getParameter("rname");
        if (rname != null && !rname.equalsIgnoreCase("")) {
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }
        String lowPrice = request.getParameter("lowPrice");
        String highPrice = request.getParameter("highPrice");
        //设置每页显示数目
        int pageSize = 10;
        //调用service查询
        PageBean<Route> pageBean = routeService.findByCount(currentPage, pageSize, rname, lowPrice, highPrice);
        //写回客户端
        writeValue(pageBean, response);
    }

    /**
     * 查询线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        String cid = request.getParameter("cid");
        //调用service查询
        List<Route> routeList = routeService.findRoute(cid);

        writeValue(routeList, response);
    }

    /**
     * 查询最新的旅游线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findNewest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Route> routeList = routeService.findNewest();

        writeValue(routeList, response);
    }

    /**
     * 查询主题旅游线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findTheme(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Route> routeList = routeService.findTheme();

        writeValue(routeList, response);
    }
}