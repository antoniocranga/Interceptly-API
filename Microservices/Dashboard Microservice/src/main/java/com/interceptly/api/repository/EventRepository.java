package com.interceptly.api.repository;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.utils.TagsOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventDao, UUID> {

    @Query("select count(*) from EventDao e where e.projectId = ?1 and e.createdAt between ?2 and ?3")
    long countByProjectIdAndCreatedAtBetweenStartAndEnd(Integer projectId, LocalDateTime start, LocalDateTime end);

    @Query("select count(*) from EventDao  e where e.projectId = ?1")
    long countByProjectId(Integer projectId);

    @Query("select distinct coalesce(e.browser, 'Unknown') from EventDao e where e.projectId = ?1")
    List<String> selectBrowsers(Integer projectId);

    @Query("select distinct coalesce(e.deviceType, 'Unknown') from EventDao e where e.projectId = ?1")
    List<String> selectDeviceTypes(Integer projectId);

    @Query("select coalesce(e.browser,'Unknown') as tag, count(*) as value, cast(e.createdAt as date) as date from EventDao  e where e.projectId = ?1 and e.createdAt between ?2 and ?3 group by cast(e.createdAt as date), e.browser order by cast(e.createdAt as date)")
    List<TagsOnly> countByBrowserAndFormattedDate(Integer projectId, LocalDateTime start, LocalDateTime end);

    @Query("select coalesce(e.deviceType,'Unknown') as tag, count(*) as value, cast(e.createdAt as date) as date from EventDao  e where e.projectId = ?1 and e.createdAt between ?2 and ?3 group by cast(e.createdAt as date), e.deviceType order by cast(e.createdAt as date)")
    List<TagsOnly> countByDeviceTypeAndFormattedDate(Integer projectId, LocalDateTime start, LocalDateTime end);

    @Query("select 'events' as tag, count(*) as value, cast(e.createdAt as date) as date from EventDao  e where e.projectId = ?1 and e.createdAt between ?2 and ?3 group by cast(e.createdAt as date) order by cast(e.createdAt as date)")
    List<TagsOnly> countByEventsAndFormattedDate(Integer projectId, LocalDateTime start, LocalDateTime end);

    Page<EventDao> findAllByIssueId(Integer issueId, Pageable pageable);
    List<EventDao> findAllByIssueId(Integer issueId);

}
