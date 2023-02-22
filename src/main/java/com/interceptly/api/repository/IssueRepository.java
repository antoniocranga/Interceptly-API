package com.interceptly.api.repository;

import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.dao.utils.TagsOnly;
import com.interceptly.api.util.enums.IssueStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<IssueDao, Integer> {
    Optional<IssueDao> findByProjectIdAndTypeContainingIgnoreCaseAndMessageContainingIgnoreCase(Integer projectId, String type, String message);

    @Query("select count(*) from IssueDao issue where issue.projectId = ?1 and issue.status = ?2 and issue.createdAt between ?3 and ?4")
    long countByProjectIdAndStatusAndUpdatedAtBetweenStartAndEnd(Integer projectId, IssueStatusEnum status, LocalDateTime start, LocalDateTime end);

    @Query("select count(*) from IssueDao issue where issue.projectId = ?1 and issue.status != ?2")
    long countByProjectIdAndStatusNot(Integer projectId, IssueStatusEnum status);

    @Query("select count(*) from IssueDao issue where issue.projectId = ?1 and issue.status = ?2")
    long countByProjectIdAndStatus(Integer projectId, IssueStatusEnum status);

    Page<IssueDao> findAllByStatusAndProjectIdAndTitleContainsIgnoreCaseAndTypeContainsIgnoreCase(IssueStatusEnum status, Integer projectId, String title, String type, Pageable pageable);

    Page<IssueDao> findAllByStatusAndLastSeenAfterAndProjectIdAndTitleContainsIgnoreCaseAndTypeContainsIgnoreCase(IssueStatusEnum status, LocalDateTime lastSeen, Integer projectId, String title, String type, Pageable pageable);

    @Query(value = "update IssueDao issue set issue.status = ?1 where issue.id in (?2)")
    @Modifying
    void updateIssuesBulk(IssueStatusEnum status, List<Integer> ids);

    @Query("select 'issues' as tag,count(*) as value,cast(e.createdAt as date) as date from IssueDao e where e.projectId = ?1 and e.createdAt between ?2 and ?3 group by cast(e.createdAt as date) order by cast(e.createdAt as date)")
    List<TagsOnly> countByIssuesAndFormattedDate(Integer projectId, LocalDateTime start, LocalDateTime end);

    @Query("select 'issues' as tag,count(*) as value,cast(e.createdAt as date) as date from IssueDao e where e.projectId = ?1 and e.status = ?2 and e.createdAt between ?3 and ?4 group by cast(e.createdAt as date) order by cast(e.createdAt as date)")
    List<TagsOnly> countByIssuesAndStatusAndFormattedDate(Integer projectId, IssueStatusEnum status, LocalDateTime start, LocalDateTime end);
}


