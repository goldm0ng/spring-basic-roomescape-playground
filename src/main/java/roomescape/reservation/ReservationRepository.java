package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByName(String name);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.date = :date AND r.time.id = :timeId AND r.theme.id = :themeId")
    boolean existsByDateAndTimeIdAndThemeId(@Param("date") String date,
                                            @Param("timeId") Long timeId,
                                            @Param("themeId") Long themeId);
}
