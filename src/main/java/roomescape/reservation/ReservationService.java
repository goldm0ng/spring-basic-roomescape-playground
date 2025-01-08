package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                new Time(reservationRequest.getTime()),
                new Theme(reservationRequest.getTheme(),"")
        );

        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
