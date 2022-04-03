package polytech.RBNN.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import polytech.RBNN.entity.ImageData;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Long> {

    List<ImageData> findAllByUsername(String username);

}