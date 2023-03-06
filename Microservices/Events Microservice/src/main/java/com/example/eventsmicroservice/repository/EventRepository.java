package com.example.eventsmicroservice.repository;

import com.example.eventsmicroservice.dao.EventDao;
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
    Page<EventDao> findAllByIssueId(Integer issueId, Pageable pageable);
    List<EventDao> findAllByIssueId(Integer issueId);

}
