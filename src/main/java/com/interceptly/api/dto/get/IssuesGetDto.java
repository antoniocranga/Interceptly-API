package com.interceptly.api.dto.get;

import com.interceptly.api.dao.IssueDao;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IssuesGetDto {
   private List<IssueDao> issues;

   private Integer page;

   private Integer perPage;

   private Integer pageCount;

   private Integer totalCount;

}
