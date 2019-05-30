package com.github.kuangcp.hi.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartCommand {

  private Date startTime;

  private String place;

  private int scale;
}
