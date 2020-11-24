package at.jku.restservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface HandlebarRepository extends JpaRepository<HandlebarConfig, BigInteger> {

}
