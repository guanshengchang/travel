package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryDao {
    /**
     * 查询所有
     * @return
     */
    public List<Category> findAll();

    /**
     * 根据id查询
     * @param cid
     * @return
     */
    Category findByCid(int cid);
}
