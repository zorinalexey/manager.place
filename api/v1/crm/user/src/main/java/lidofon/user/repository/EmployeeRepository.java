package lidofon.user.repository;


import lidofon.user.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    @Query(value = """
    SELECT COALESCE(MAX(e.intId), 0) FROM Employee e
""")
    Optional<Long> getMaxId();
}
