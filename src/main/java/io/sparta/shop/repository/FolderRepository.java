package io.sparta.shop.repository;

import io.sparta.shop.entity.Folder;
import io.sparta.shop.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    // select * from folder where user_id = ? and name in (?, ?, ?);
    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);

    List<Folder> findAllByUser(User user);
}
