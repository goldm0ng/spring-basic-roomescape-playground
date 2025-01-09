package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.authentication.MemberAuthInfo;
import roomescape.exception.MemberNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.TimeNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationResponse save(ReservationRequest reservationRequest, MemberAuthInfo memberAuthInfo) {

        if (reservationRequest.name() == null) {
            reservationRequest = new ReservationRequest(
                    memberAuthInfo.name(),
                    reservationRequest.date(),
                    reservationRequest.theme(),
                    reservationRequest.time());
        }

        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new ThemeNotFoundException("해당 테마를 찾을 수 없습니다."));
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new TimeNotFoundException("예약 시간을 찾을 수 없습니다."));

        Member member = null;
        if (memberAuthInfo.id() != null) { //관리자가 아닌, 사용자일 경우
            member = memberRepository.findById(memberAuthInfo.id())
                    .orElseThrow(() -> new MemberNotFoundException("가입된 회원이 아닙니다."));
        }

        Reservation reservation = new Reservation (
                member,
                reservationRequest.name(),
                reservationRequest.date(),
                time,
                theme
        );

        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<ReservationResponse> findAllByMemberName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();

    }
}
