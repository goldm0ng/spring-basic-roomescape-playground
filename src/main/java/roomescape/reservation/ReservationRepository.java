package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByName(String name);

    boolean existsByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);
}
