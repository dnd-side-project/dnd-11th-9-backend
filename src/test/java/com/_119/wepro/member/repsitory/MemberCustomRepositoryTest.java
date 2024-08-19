package com._119.wepro.member.repsitory;

import static com._119.wepro.member.domain.QMember.member;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com._119.wepro.member.domain.Member;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class MemberCustomRepositoryTest {

  @Mock
  private JPAQueryFactory queryFactory;

  @Mock
  private JPAQuery<Member> jpaQuery;

  @InjectMocks
  private MemberCustomRepository memberCustomRepository;

  private CacheManager cacheManager;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    cacheManager = new ConcurrentMapCacheManager("findMemberCache");
  }

  @Test
  public void testFindMembers_Cacheable() {
    // Mock the query result
    Member mockMember = new Member();
    mockMember.setName("Test");
    List<Member> mockResult = Collections.singletonList(mockMember);


//    / Set up the JPAQueryFactory mock
    given(queryFactory.select(member)).willReturn(jpaQuery);
    given(jpaQuery.from(member)).willReturn(jpaQuery);
    given(jpaQuery.where(any(Predicate.class))).willReturn(jpaQuery);
    given(jpaQuery.fetch()).willReturn(mockResult);


    // First call, should hit the database
    List<Member> result1 = memberCustomRepository.findMembers("Test");
    verify(queryFactory, times(1)).selectFrom(member);

    // Second call with the same keyword, should return cached result
    List<Member> result2 = memberCustomRepository.findMembers("Test");
    verify(queryFactory, times(1)).selectFrom(member); // Still 1, as second call should be cached

    // Check if the result is from the cache
    assertSame(result1, result2);
  }

  @Test
  public void testFindMembers_CacheEviction() {
    // Mock the query result
    Member mockMember1 = new Member();
    mockMember1.setName("Test");
    List<Member> mockResult1 = Collections.singletonList(mockMember1);

    Member mockMember2 = new Member();
    mockMember2.setName("Test2");
    List<Member> mockResult2 = Collections.singletonList(mockMember2);

    when(queryFactory.selectFrom(member)
        .where(member.name.contains("Test"))
        .fetch()).thenReturn(mockResult1);

    // First call
    List<Member> result1 = memberCustomRepository.findMembers("Test");

    // Simulate cache eviction by clearing the cache
    cacheManager.getCache("findMemberCache").clear();

    // Mock a different result for the next query
    when(queryFactory.selectFrom(member)
        .where(member.name.contains("Test"))
        .fetch()).thenReturn(mockResult2);

    // Second call after cache eviction, should hit the database again
    List<Member> result2 = memberCustomRepository.findMembers("Test");
    verify(queryFactory, times(2)).selectFrom(member);

    // Ensure the results are different
    assertNotSame(result1, result2);
  }
}