/**
 * 
 */
/**
 * @author ZribaY
 *
 */
package fr.fms.trainingstore.dao.jdbc;


import fr.fms.trainingstore.config.ConnectionFactory;
import fr.fms.trainingstore.dao.CategoryDao;
import fr.fms.trainingstore.domain.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoJdbc implements CategoryDao {

    private final ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    @Override
    public Category findById(int id) {
        String sql = "SELECT id_category, label FROM category WHERE id_category = ?";

        try (Connection cn = connectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCategory(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("Error CategoryDaoJdbc.findById: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT id_category, label FROM category ORDER BY id_category";

        List<Category> categories = new ArrayList<>();

        try (Connection cn = connectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapCategory(rs));
            }
        } catch (Exception e) {
            System.err.println("Error CategoryDaoJdbc.findAll: " + e.getMessage());
        }

        return categories;
    }

    private Category mapCategory(ResultSet rs) throws Exception {
        Category category = new Category();
        category.setId(rs.getInt("id_category"));
        category.setLabel(rs.getString("label"));
        return category;
    }
}