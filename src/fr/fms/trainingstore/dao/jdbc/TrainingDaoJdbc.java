/**
 * 
 */
package fr.fms.trainingstore.dao.jdbc;

/**
 * @author ZribaY
 *
 */
import fr.fms.trainingstore.config.ConnectionFactory;
import fr.fms.trainingstore.dao.TrainingDao;
import fr.fms.trainingstore.domain.Category;
import fr.fms.trainingstore.domain.Modality;
import fr.fms.trainingstore.domain.Training;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TrainingDaoJdbc implements TrainingDao {

    private final ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    @Override
    public Training findById(int id) {
        String sql = """
            SELECT t.id_training, t.name, t.description, t.duration_days, t.modality, t.price,
                   c.id_category, c.label
            FROM training t
            LEFT JOIN category c ON c.id_category = t.fk_id_category
            WHERE t.id_training = ?
        """;

        try (Connection cn = connectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTraining(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("Error TrainingDaoJdbc.findById: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Training> findAll() {
        String sql = """
            SELECT t.id_training, t.name, t.description, t.duration_days, t.modality, t.price,
                   c.id_category, c.label
            FROM training t
            LEFT JOIN category c ON c.id_category = t.fk_id_category
            ORDER BY t.id_training
        """;

        List<Training> trainings = new ArrayList<>();

        try (Connection cn = connectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                trainings.add(mapTraining(rs));
            }
        } catch (Exception e) {
            System.err.println("Error TrainingDaoJdbc.findAll: " + e.getMessage());
        }

        return trainings;
    }

    @Override
    public List<Training> findByCategoryId(int categoryId) {
        String sql = """
            SELECT t.id_training, t.name, t.description, t.duration_days, t.modality, t.price,
                   c.id_category, c.label
            FROM training t
            JOIN category c ON c.id_category = t.fk_id_category
            WHERE t.fk_id_category = ?
            ORDER BY t.id_training
        """;

        List<Training> trainings = new ArrayList<>();

        try (Connection cn = connectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trainings.add(mapTraining(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Error TrainingDaoJdbc.findByCategoryId: " + e.getMessage());
        }

        return trainings;
    }

    @Override
    public List<Training> findByModality(Modality modality) {
        String sql = """
            SELECT t.id_training, t.name, t.description, t.duration_days, t.modality, t.price,
                   c.id_category, c.label
            FROM training t
            LEFT JOIN category c ON c.id_category = t.fk_id_category
            WHERE t.modality = ?
            ORDER BY t.id_training
        """;

        List<Training> trainings = new ArrayList<>();

        try (Connection cn = connectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, modality.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trainings.add(mapTraining(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Error TrainingDaoJdbc.findByModality: " + e.getMessage());
        }

        return trainings;
    }

    @Override
    public List<Training> searchByKeyword(String keyword) {
        String sql = """
            SELECT t.id_training, t.name, t.description, t.duration_days, t.modality, t.price,
                   c.id_category, c.label
            FROM training t
            LEFT JOIN category c ON c.id_category = t.fk_id_category
            WHERE LOWER(t.name) LIKE ? OR LOWER(t.description) LIKE ?
            ORDER BY t.id_training
        """;

        List<Training> trainings = new ArrayList<>();
        String pattern = "%" + keyword.toLowerCase() + "%";

        try (Connection cn = connectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trainings.add(mapTraining(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Error TrainingDaoJdbc.searchByKeyword: " + e.getMessage());
        }

        return trainings;
    }

    private Training mapTraining(ResultSet rs) throws Exception {
        // 1) Category peut être NULL (LEFT JOIN)
        Category category = null;
        int categoryId = rs.getInt("id_category");
        
        		//technique standard pour détecter un NULL SQL sur un int
        if (!rs.wasNull()) {
            category = new Category(categoryId, rs.getString("label"));
        }

        // 2) Mapping Training
        Training training = new Training();
        training.setId(rs.getInt("id_training"));
        training.setName(rs.getString("name"));
        training.setDescription(rs.getString("description"));
        training.setDurationDays(rs.getInt("duration_days"));
        training.setModality(Modality.valueOf(rs.getString("modality")));//convertit "ONSITE" / "REMOTE" (DB) vers enum Java !
        training.setPrice(rs.getDouble("price"));
        training.setCategory(category);

        return training;
    }
}
