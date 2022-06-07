package uz.yt.springdata.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String> {

}
