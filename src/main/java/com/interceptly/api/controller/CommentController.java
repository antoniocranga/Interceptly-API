package com.interceptly.api.controller;

import com.interceptly.api.dao.CommentDao;
import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.dto.delete.CommentDeleteDto;
import com.interceptly.api.dto.patch.CommentPatchDto;
import com.interceptly.api.dto.post.CommentPostDto;
import com.interceptly.api.repository.CommentRepository;
import com.interceptly.api.repository.IssueRepository;
import com.interceptly.api.repository.PermissionRepository;
import com.interceptly.api.util.PermissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@RequestMapping("/comments")
@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    PermissionUtil permissionUtil;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDao postComment(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid CommentPostDto comment) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, comment.getProjectId());
        CommentDao commentDao = CommentDao.builder().comment(comment.getComment()).userId(permissionDao.getUserId()).issueId(comment.getIssueId()).build();
        commentRepository.saveAndFlush(commentDao);
        return commentDao;
    }

    @PatchMapping
    public CommentDao patchComment(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid CommentPatchDto comment) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, comment.getProjectId());
        Optional<CommentDao> commentDao = commentRepository.findById(comment.getId());
        if (commentDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        if (!Objects.equals(commentDao.get().getUserId(), permissionDao.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        commentDao.get().setComment(comment.getComment());
        commentRepository.saveAndFlush(commentDao.get());
        return commentDao.get();
    }

    @DeleteMapping
    public String deleteComment(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid CommentDeleteDto comment) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, comment.getProjectId());
        Optional<CommentDao> commentDao = commentRepository.findById(comment.getId());
        if (commentDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        if (!Objects.equals(commentDao.get().getUserId(), permissionDao.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        commentRepository.delete(commentDao.get());
        return "Comment removed.";
    }
}
