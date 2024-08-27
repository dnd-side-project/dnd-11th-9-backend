package com._119.wepro.alarm.service;

import com._119.wepro.alarm.domain.Alarm;
import com._119.wepro.alarm.domain.repository.AlarmRepository;
import com._119.wepro.global.enums.AlarmType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.UserErrorCode;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

  private final MemberRepository memberRepository;
  private final AlarmRepository alarmRepository;

  @Transactional
  public void createAlarm(Member sender, Long receiverId, AlarmType alarmType, Long targetId) {
    Member receiver = memberRepository.findById(receiverId).orElseThrow(()
        -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

    alarmRepository.save(Alarm.of(sender, receiver, alarmType, targetId));
  }

}
